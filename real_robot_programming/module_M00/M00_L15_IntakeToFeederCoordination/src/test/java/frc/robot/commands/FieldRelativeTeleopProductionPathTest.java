// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.DriverInputObservation;
import frc.robot.subsystems.SwerveOutputPipeline;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the complete field-relative production path through recording IO boundaries. */
class FieldRelativeTeleopProductionPathTest {
  private static final double kTolerance = 1.0e-9;
  private static final int kModuleCount = 4;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableTeleop() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void zeroHeadingPreservesPositiveFieldXAsRobotForward() {
    assertFieldConversion(0.0, new ChassisSpeeds(1.0, 0.0, 0.0));
  }

  @Test
  void positiveNinetyHeadingConvertsPositiveFieldXToRobotRight() {
    assertFieldConversion(90.0, new ChassisSpeeds(1.0, 0.0, 0.0));
  }

  @Test
  void negativeNinetyHeadingConvertsPositiveFieldXToRobotLeft() {
    assertFieldConversion(-90.0, new ChassisSpeeds(1.0, 0.0, 0.0));
  }

  @Test
  void oneHundredEightyHeadingConvertsPositiveFieldXToRobotBackward() {
    assertFieldConversion(180.0, new ChassisSpeeds(1.0, 0.0, 0.0));
  }

  @Test
  void combinedTranslationAndPositiveOmegaTraverseExistingOutputPipeline() {
    Rig rig = new Rig(-1.0, -1.0, -1.0, 37.0);

    rig.executeAndDispatch();

    assertEquals(1.0, rig.publisher.observation.processedForward(), kTolerance);
    assertEquals(1.0, rig.publisher.observation.processedStrafe(), kTolerance);
    assertEquals(1.0, rig.publisher.observation.processedRotation(), kTolerance);
    ChassisSpeeds fieldRelativeSpeeds = new ChassisSpeeds(1.0, 1.0, 1.0);
    assertPipelineAndDispatchMatch(rig, fieldRelativeSpeeds);

    ChassisSpeeds expectedRobotRelativeSpeeds =
        ChassisSpeeds.fromFieldRelativeSpeeds(
            fieldRelativeSpeeds, Rotation2d.fromDegrees(37.0));
    assertEquals(1.0, expectedRobotRelativeSpeeds.omegaRadiansPerSecond, kTolerance);
  }

  @Test
  void positiveOmegaSignIsUnchangedAtEveryHeading() {
    for (double yawDegrees : new double[] {0.0, 90.0, -90.0, 180.0}) {
      Rig rig = new Rig(0.0, 0.0, -1.0, yawDegrees);

      rig.executeAndDispatch();

      assertEquals(1.0, rig.publisher.observation.processedRotation(), kTolerance);
      assertPipelineAndDispatchMatch(rig, new ChassisSpeeds(0.0, 0.0, 1.0));
    }
  }

  @Test
  void usesOneCoherentDriverSampleAndFixedModuleDispatchOrder() {
    Rig rig = new Rig(-1.0, -1.0, -1.0, 25.0);

    rig.executeAndDispatch();

    assertEquals(1, rig.controller.leftYReadCount);
    assertEquals(1, rig.controller.leftXReadCount);
    assertEquals(1, rig.controller.rightXReadCount);
    assertEquals(1, rig.publisher.publishCount);
    assertEquals(
        List.of(
            "FL.drive",
            "FL.steer",
            "FR.drive",
            "FR.steer",
            "BL.drive",
            "BL.steer",
            "BR.drive",
            "BR.steer"),
        rig.dispatchLog);
  }

  @Test
  void centeredXboxDispatchesZeroDriveAndPreservesCurrentModuleAngles() {
    Rig rig = new Rig(0.0, 0.0, 0.0, 90.0);
    double[] currentAngleRotations = {-0.10, 0.05, 0.35, -0.40};
    rig.setModuleAnglesRotations(currentAngleRotations);

    rig.executeAndDispatch();

    RecordingModuleIO[] modules = rig.modules();
    for (int moduleIndex = 0; moduleIndex < kModuleCount; moduleIndex++) {
      assertState(
          modules[moduleIndex].lastState(),
          0.0,
          Rotation2d.fromRotations(currentAngleRotations[moduleIndex]).getRadians());
    }
  }

  @Test
  void disabledModeStopsWithoutReadingPublishingAcceptingOrDispatching() {
    Rig rig = new Rig(-1.0, -1.0, -1.0, 90.0);
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();

    rig.gyro.yawDegrees = 0.0;
    rig.primeGyro();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.gyro.yawDegrees = 90.0;
    rig.primeGyro();
    rig.command.execute();
    rig.subsystem.periodic();

    assertEquals(0, rig.controller.leftYReadCount);
    assertEquals(0, rig.controller.leftXReadCount);
    assertEquals(0, rig.controller.rightXReadCount);
    assertEquals(0, rig.publisher.publishCount);
    assertEquals(0, rig.dispatchLog.size());
    for (RecordingModuleIO module : rig.modules()) {
      assertEquals(0, module.driveRequestCount);
      assertEquals(0, module.steerRequestCount);
    }
  }

  private static void assertFieldConversion(
      double yawDegrees, ChassisSpeeds fieldRelativeSpeeds) {
    Rig rig =
        new Rig(
            -fieldRelativeSpeeds.vxMetersPerSecond,
            -fieldRelativeSpeeds.vyMetersPerSecond,
            -fieldRelativeSpeeds.omegaRadiansPerSecond,
            yawDegrees);

    rig.executeAndDispatch();

    assertPipelineAndDispatchMatch(rig, fieldRelativeSpeeds);
  }

  private static void assertPipelineAndDispatchMatch(
      Rig rig, ChassisSpeeds fieldRelativeSpeeds) {
    ChassisSpeeds expectedRobotRelativeSpeeds =
        ChassisSpeeds.fromFieldRelativeSpeeds(
            fieldRelativeSpeeds, Rotation2d.fromDegrees(rig.gyro.yawDegrees));
    SwerveModuleState[] expectedStates =
        new SwerveOutputPipeline().toModuleStates(expectedRobotRelativeSpeeds, rig.currentAngles());
    SwerveModuleState[] finalStates = rig.subsystem.getFinalModuleStates();
    RecordingModuleIO[] modules = rig.modules();

    assertEquals(kModuleCount, finalStates.length);
    for (int moduleIndex = 0; moduleIndex < kModuleCount; moduleIndex++) {
      assertEquals(1, modules[moduleIndex].driveRequestCount);
      assertEquals(1, modules[moduleIndex].steerRequestCount);
      assertStateEquals(expectedStates[moduleIndex], finalStates[moduleIndex]);
      assertStateEquals(finalStates[moduleIndex], modules[moduleIndex].lastState());
    }
  }

  private static void assertState(
      SwerveModuleState state, double expectedSpeedMetersPerSecond, double expectedAngleRadians) {
    assertEquals(expectedSpeedMetersPerSecond, state.speedMetersPerSecond, kTolerance);
    assertEquals(expectedAngleRadians, state.angle.getRadians(), kTolerance);
  }

  private static void assertStateEquals(SwerveModuleState expected, SwerveModuleState actual) {
    assertState(actual, expected.speedMetersPerSecond, expected.angle.getRadians());
  }

  private static final class Rig {
    private final List<String> dispatchLog = new ArrayList<>();
    private final RecordingXboxController controller;
    private final RecordingModuleIO frontLeft = new RecordingModuleIO("FL", dispatchLog);
    private final RecordingModuleIO frontRight = new RecordingModuleIO("FR", dispatchLog);
    private final RecordingModuleIO backLeft = new RecordingModuleIO("BL", dispatchLog);
    private final RecordingModuleIO backRight = new RecordingModuleIO("BR", dispatchLog);
    private final RecordingPublisher publisher = new RecordingPublisher();
    private final RecordingGyroIO gyro;
    private final double targetYawDegrees;
    private final SwerveSubsystem subsystem;
    private final FieldRelativeTeleopDriveCommand command;

    private Rig(double leftY, double leftX, double rightX, double yawDegrees) {
      controller = new RecordingXboxController(leftY, leftX, rightX);
      gyro = new RecordingGyroIO(yawDegrees);
      targetYawDegrees = yawDegrees;
      subsystem =
          new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, gyro);
      command =
          new FieldRelativeTeleopDriveCommand(
              subsystem,
              new XboxDriverInputSource(controller),
              publisher);
    }

    private void primeGyro() {
      subsystem.periodic();
    }

    private void executeAndDispatch() {
      DriverStationSim.setEnabled(false);
      DriverStationSim.notifyNewData();
      gyro.yawDegrees = 0.0;
      primeGyro();
      assertTrue(subsystem.captureFieldHeadingReference());
      DriverStationSim.setEnabled(true);
      DriverStationSim.setAutonomous(false);
      DriverStationSim.setTest(false);
      DriverStationSim.notifyNewData();
      gyro.yawDegrees = targetYawDegrees;
      primeGyro();
      command.execute();
      subsystem.periodic();
    }

    private RecordingModuleIO[] modules() {
      return new RecordingModuleIO[] {frontLeft, frontRight, backLeft, backRight};
    }

    private Rotation2d[] currentAngles() {
      RecordingModuleIO[] modules = modules();
      Rotation2d[] angles = new Rotation2d[kModuleCount];
      for (int moduleIndex = 0; moduleIndex < kModuleCount; moduleIndex++) {
        angles[moduleIndex] =
            Rotation2d.fromRotations(modules[moduleIndex].encoderAbsolutePositionRotations);
      }
      return angles;
    }

    private void setModuleAnglesRotations(double[] moduleAngleRotations) {
      if (moduleAngleRotations.length != kModuleCount) {
        throw new IllegalArgumentException("moduleAngleRotations must contain four values");
      }
      RecordingModuleIO[] modules = modules();
      for (int moduleIndex = 0; moduleIndex < kModuleCount; moduleIndex++) {
        modules[moduleIndex].encoderAbsolutePositionRotations =
            moduleAngleRotations[moduleIndex];
      }
    }
  }

  private static final class RecordingXboxController extends XboxController {
    private final double leftY;
    private final double leftX;
    private final double rightX;
    private int leftYReadCount;
    private int leftXReadCount;
    private int rightXReadCount;

    private RecordingXboxController(double leftY, double leftX, double rightX) {
      super(Constants.DriverInputConstants.kXboxControllerPort);
      this.leftY = leftY;
      this.leftX = leftX;
      this.rightX = rightX;
    }

    @Override
    public double getLeftY() {
      leftYReadCount++;
      return leftY;
    }

    @Override
    public double getLeftX() {
      leftXReadCount++;
      return leftX;
    }

    @Override
    public double getRightX() {
      rightXReadCount++;
      return rightX;
    }
  }

  private static final class RecordingPublisher implements Consumer<DriverInputObservation> {
    private int publishCount;
    private DriverInputObservation observation;

    @Override
    public void accept(DriverInputObservation observation) {
      publishCount++;
      this.observation = observation;
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final String name;
    private final List<String> dispatchLog;
    private int driveRequestCount;
    private int steerRequestCount;
    private double lastDriveVelocityMetersPerSecond;
    private Rotation2d lastSteerAngle = new Rotation2d();
    private double encoderAbsolutePositionRotations;

    private RecordingModuleIO(String name, List<String> dispatchLog) {
      this.name = name;
      this.dispatchLog = dispatchLog;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveRequestCount++;
      lastDriveVelocityMetersPerSecond = velocityMetersPerSecond;
      dispatchLog.add(name + ".drive");
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerRequestCount++;
      lastSteerAngle = angle;
      dispatchLog.add(name + ".steer");
    }

    @Override
    public void stop() {}

    private SwerveModuleState lastState() {
      return new SwerveModuleState(lastDriveVelocityMetersPerSecond, lastSteerAngle);
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    private double yawDegrees;

    private RecordingGyroIO(double yawDegrees) {
      this.yawDegrees = yawDegrees;
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.yawDegrees = yawDegrees;
      inputs.connected = true;
      inputs.configurationHealthy = true;
    }
  }
}
