// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies subsystem-owned field-relative conversion and gyro fail-safe behavior. */
class SwerveSubsystemFieldRelativeTest {
  private static final double kTolerance = 1.0e-9;
  private static final int kModuleCount = 4;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableProductionMode() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void convertsZeroDegreeFieldRequestBeforeExistingPipeline() {
    assertConversion(0.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void convertsPositiveNinetyDegreeFieldRequestBeforeExistingPipeline() {
    assertConversion(90.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void convertsNegativeNinetyDegreeFieldRequestBeforeExistingPipeline() {
    assertConversion(-90.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void convertsOneHundredEightyDegreeFieldRequestBeforeExistingPipeline() {
    assertConversion(180.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void fieldRequestBeforeReferenceInitializationFailsClosed() {
    Rig rig = new Rig();
    rig.primeGyro();

    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();

    assertFalse(rig.subsystem.hasFieldHeadingReference());
    assertEquals(0, rig.dispatchLog.size());
    assertEachModuleStoppedOnce(rig);
    assertAllFinalSpeedsZero(rig);
  }

  @Test
  void arbitraryRawYawCaptureBecomesFieldZero() {
    assertCapturedConversion(
        137.25, 137.25, 0.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void positiveNinetyDegreesAfterCaptureIsPositiveFieldHeading() {
    assertCapturedConversion(
        137.25, 227.25, 90.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void negativeNinetyDegreesAfterCaptureIsNegativeFieldHeading() {
    assertCapturedConversion(
        137.25, 47.25, -90.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void wrapBoundariesUseTheDeterministicHalfOpenRange() {
    assertCapturedConversion(
        179.0, -179.0, 2.0, new ChassisSpeeds(1.0, 0.5, 0.25));
    assertCapturedConversion(
        -179.0, 179.0, -2.0, new ChassisSpeeds(1.0, 0.5, 0.25));
  }

  @Test
  void captureIsRejectedWhileEnabled() {
    Rig rig = new Rig();
    rig.gyro.yawDegrees = 22.0;
    rig.primeGyro();

    assertFalse(rig.subsystem.captureFieldHeadingReference());
    assertFalse(rig.subsystem.hasFieldHeadingReference());
  }

  @Test
  void invalidGyroPreventsCapture() {
    Rig rig = new Rig();
    setEnabled(false);
    rig.gyro.connected = false;
    rig.primeGyro();

    assertFalse(rig.subsystem.captureFieldHeadingReference());
    assertFalse(rig.subsystem.hasFieldHeadingReference());
    assertEachModuleStoppedOnce(rig);
  }

  @Test
  void rejectsNullFieldRelativeSpeeds() {
    Rig rig = new Rig();

    assertThrows(
        NullPointerException.class,
        () -> rig.subsystem.acceptFieldRelativeChassisSpeeds(null));
  }

  @Test
  void disconnectedHeadingStopsAndDisarms() {
    assertInvalidHeadingStopsAndDisarms(0.0, false, true);
  }

  @Test
  void configurationUnhealthyHeadingStopsAndDisarms() {
    assertInvalidHeadingStopsAndDisarms(0.0, true, false);
  }

  @Test
  void nanHeadingStopsAndDisarms() {
    assertInvalidHeadingStopsAndDisarms(Double.NaN, true, true);
  }

  @Test
  void positiveInfinityHeadingStopsAndDisarms() {
    assertInvalidHeadingStopsAndDisarms(Double.POSITIVE_INFINITY, true, true);
  }

  @Test
  void negativeInfinityHeadingStopsAndDisarms() {
    assertInvalidHeadingStopsAndDisarms(Double.NEGATIVE_INFINITY, true, true);
  }

  @Test
  void headingInvalidatedAfterAcceptancePreventsStaleDispatch() {
    Rig rig = new Rig();
    setEnabled(false);
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    setEnabled(true);
    rig.clearActuationEvents();
    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));

    rig.gyro.connected = false;
    rig.subsystem.periodic();

    assertEquals(0, rig.dispatchLog.size());
    assertEachModuleStoppedOnce(rig);
    assertAllFinalSpeedsZero(rig);
    assertTrue(rig.subsystem.hasFieldHeadingReference());
  }

  @Test
  void recoveryRequiresFreshValidHeadingAndFreshFieldRelativeRequest() {
    Rig rig = new Rig();
    setEnabled(false);
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    setEnabled(true);
    rig.clearActuationEvents();
    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());

    rig.clearActuationEvents();
    rig.gyro.connected = false;
    rig.subsystem.periodic();
    assertEquals(0, rig.dispatchLog.size());
    assertEachModuleStoppedOnce(rig);

    rig.gyro.connected = true;
    rig.subsystem.periodic();
    assertEquals(0, rig.dispatchLog.size());

    assertTrue(rig.subsystem.hasFieldHeadingReference());
    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());
    for (RecordingModuleIO module : rig.modules()) {
      assertEquals(1, module.driveRequestCount);
      assertEquals(1, module.steerRequestCount);
    }
  }

  @Test
  void recaptureDisarmsPreviousRequestAndRequiresFreshRequest() {
    Rig rig = new Rig();
    setEnabled(false);
    rig.gyro.yawDegrees = 10.0;
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());

    setEnabled(true);
    rig.primeGyro();
    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());

    rig.clearActuationEvents();
    setEnabled(false);
    rig.gyro.yawDegrees = 100.0;
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    assertTrue(rig.subsystem.hasFieldHeadingReference());
    assertEquals(0, rig.dispatchLog.size());
    assertAllFinalSpeedsZero(rig);

    setEnabled(true);
    rig.primeGyro();
    assertEquals(0, rig.dispatchLog.size());
    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());
  }

  @Test
  void referenceSurvivesNormalDisableEnable() {
    Rig rig = new Rig();
    setEnabled(false);
    rig.gyro.yawDegrees = 45.0;
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());

    setEnabled(true);
    rig.primeGyro();
    assertTrue(rig.subsystem.hasFieldHeadingReference());
    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());

    rig.subsystem.stop();
    rig.clearActuationEvents();
    setEnabled(false);
    rig.subsystem.periodic();
    setEnabled(true);
    rig.subsystem.periodic();
    assertTrue(rig.subsystem.hasFieldHeadingReference());
    assertEquals(0, rig.dispatchLog.size());

    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());
  }

  @Test
  void robotRelativeProductionIntentDoesNotDependOnGyroValidity() {
    Rig rig = new Rig();
    rig.gyro.yawDegrees = Double.NaN;
    rig.gyro.connected = false;
    rig.gyro.configurationHealthy = false;
    rig.primeGyro();

    ChassisSpeeds robotRelativeSpeeds = new ChassisSpeeds(1.0, 0.5, 0.25);
    rig.subsystem.acceptChassisSpeeds(robotRelativeSpeeds);
    rig.subsystem.periodic();

    assertEquals(kModuleCount * 2, rig.dispatchLog.size());
    SwerveModuleState[] expectedStates =
        new SwerveOutputPipeline().toModuleStates(robotRelativeSpeeds, rig.currentAngles());
    assertStatesEqual(expectedStates, rig.subsystem.getFinalModuleStates());
  }

  private static void assertConversion(
      double yawDegrees, ChassisSpeeds fieldRelativeSpeeds) {
    assertCapturedConversion(
        0.0, yawDegrees, yawDegrees == 180.0 ? -180.0 : yawDegrees, fieldRelativeSpeeds);
  }

  private static void assertCapturedConversion(
      double referenceYawDegrees,
      double currentYawDegrees,
      double expectedFieldHeadingDegrees,
      ChassisSpeeds fieldRelativeSpeeds) {
    Rig rig = new Rig();
    setEnabled(false);
    rig.gyro.yawDegrees = referenceYawDegrees;
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    setEnabled(true);
    rig.gyro.yawDegrees = currentYawDegrees;
    rig.primeGyro();

    rig.subsystem.acceptFieldRelativeChassisSpeeds(fieldRelativeSpeeds);
    rig.subsystem.periodic();

    ChassisSpeeds expectedRobotRelativeSpeeds =
        ChassisSpeeds.fromFieldRelativeSpeeds(
            fieldRelativeSpeeds, Rotation2d.fromDegrees(expectedFieldHeadingDegrees));
    SwerveModuleState[] expectedStates =
        new SwerveOutputPipeline().toModuleStates(expectedRobotRelativeSpeeds, rig.currentAngles());
    assertStatesEqual(expectedStates, rig.subsystem.getFinalModuleStates());
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());
  }

  private static void assertInvalidHeadingStopsAndDisarms(
      double yawDegrees, boolean connected, boolean configurationHealthy) {
    Rig rig = new Rig();
    rig.gyro.yawDegrees = yawDegrees;
    rig.gyro.connected = connected;
    rig.gyro.configurationHealthy = configurationHealthy;
    rig.primeGyro();

    rig.subsystem.acceptFieldRelativeChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();

    assertEquals(0, rig.dispatchLog.size());
    assertEachModuleStoppedOnce(rig);
    assertAllFinalSpeedsZero(rig);
  }

  private static void assertEachModuleStoppedOnce(Rig rig) {
    for (RecordingModuleIO module : rig.modules()) {
      assertEquals(1, module.stopCount);
    }
  }

  private static void assertAllFinalSpeedsZero(Rig rig) {
    for (SwerveModuleState state : rig.subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
    }
  }

  private static void assertStatesEqual(
      SwerveModuleState[] expectedStates, SwerveModuleState[] actualStates) {
    assertEquals(expectedStates.length, actualStates.length);
    for (int moduleIndex = 0; moduleIndex < expectedStates.length; moduleIndex++) {
      assertEquals(
          expectedStates[moduleIndex].speedMetersPerSecond,
          actualStates[moduleIndex].speedMetersPerSecond,
          kTolerance);
      assertEquals(
          expectedStates[moduleIndex].angle.getRadians(),
          actualStates[moduleIndex].angle.getRadians(),
          kTolerance);
    }
  }

  private static void setEnabled(boolean enabled) {
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.notifyNewData();
  }

  private static final class Rig {
    private final List<String> dispatchLog = new ArrayList<>();
    private final RecordingModuleIO frontLeft = new RecordingModuleIO("FL", dispatchLog);
    private final RecordingModuleIO frontRight = new RecordingModuleIO("FR", dispatchLog);
    private final RecordingModuleIO backLeft = new RecordingModuleIO("BL", dispatchLog);
    private final RecordingModuleIO backRight = new RecordingModuleIO("BR", dispatchLog);
    private final RecordingGyroIO gyro = new RecordingGyroIO();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, gyro);

    private void primeGyro() {
      subsystem.periodic();
    }

    private RecordingModuleIO[] modules() {
      return new RecordingModuleIO[] {frontLeft, frontRight, backLeft, backRight};
    }

    private Rotation2d[] currentAngles() {
      return new Rotation2d[] {
        new Rotation2d(), new Rotation2d(), new Rotation2d(), new Rotation2d()
      };
    }

    private void clearActuationEvents() {
      dispatchLog.clear();
      for (RecordingModuleIO module : modules()) {
        module.stopCount = 0;
        module.driveRequestCount = 0;
        module.steerRequestCount = 0;
      }
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final String name;
    private final List<String> dispatchLog;
    private int driveRequestCount;
    private int steerRequestCount;
    private int stopCount;

    private RecordingModuleIO(String name, List<String> dispatchLog) {
      this.name = name;
      this.dispatchLog = dispatchLog;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.encoderAbsolutePositionRotations = 0.0;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveRequestCount++;
      dispatchLog.add(name + ".drive");
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerRequestCount++;
      dispatchLog.add(name + ".steer");
    }

    @Override
    public void stop() {
      stopCount++;
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    private double yawDegrees;
    private boolean connected = true;
    private boolean configurationHealthy = true;

    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.yawDegrees = yawDegrees;
      inputs.connected = connected;
      inputs.configurationHealthy = configurationHealthy;
    }
  }
}
