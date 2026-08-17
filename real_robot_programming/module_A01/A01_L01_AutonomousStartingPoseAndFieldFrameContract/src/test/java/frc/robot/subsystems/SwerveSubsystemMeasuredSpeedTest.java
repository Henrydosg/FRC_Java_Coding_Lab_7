// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies validated measured robot-relative speed derivation. */
class SwerveSubsystemMeasuredSpeedTest {
  private static final double kTolerance = 1.0e-9;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void measuredSpeedsAreUnavailableBeforeACompleteMeasurement() {
    Rig rig = new Rig();

    assertTrue(rig.subsystem.getMeasuredRobotRelativeSpeeds().isEmpty());
  }

  @Test
  void derivesPurePositiveRobotForwardSpeed() {
    assertMeasuredSpeeds(new ChassisSpeeds(0.80, 0.0, 0.0));
  }

  @Test
  void derivesPurePositiveRobotLeftSpeed() {
    assertMeasuredSpeeds(new ChassisSpeeds(0.0, 0.80, 0.0));
  }

  @Test
  void derivesPositiveAndNegativeRotation() {
    assertMeasuredSpeeds(new ChassisSpeeds(0.0, 0.0, 0.60));
    assertMeasuredSpeeds(new ChassisSpeeds(0.0, 0.0, -0.60));
  }

  @Test
  void derivesCombinedTranslationAndRotation() {
    assertMeasuredSpeeds(new ChassisSpeeds(0.75, -0.25, 0.50));
  }

  @Test
  void preservesMeasuredModuleOrderAsFrontLeftFrontRightBackLeftBackRight() {
    Rig rig = new Rig();
    ChassisSpeeds expected = new ChassisSpeeds(0.75, -0.25, 0.50);
    SwerveModuleState[] expectedStates = new SwerveKinematics().toModuleStates(expected);

    rig.setMeasuredStates(expectedStates);
    rig.subsystem.periodic();

    assertEquals(
        expectedStates[0].angle.getRotations(),
        rig.frontLeft.encoderAbsolutePositionRotations,
        kTolerance);
    assertEquals(
        expectedStates[1].angle.getRotations(),
        rig.frontRight.encoderAbsolutePositionRotations,
        kTolerance);
    assertEquals(
        expectedStates[2].angle.getRotations(),
        rig.backLeft.encoderAbsolutePositionRotations,
        kTolerance);
    assertEquals(
        expectedStates[3].angle.getRotations(),
        rig.backRight.encoderAbsolutePositionRotations,
        kTolerance);
    assertMeasuredEquals(expected, rig.subsystem.getMeasuredRobotRelativeSpeeds().orElseThrow());
  }

  @Test
  void convertsRotorVelocityUsingTheVerifiedRatioAndWheelCircumference() {
    Rig rig = new Rig();
    double expectedWheelSpeedMetersPerSecond = 0.30;
    double wheelCircumferenceMeters =
        2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters;
    double expectedRotorRps =
        expectedWheelSpeedMetersPerSecond
            / wheelCircumferenceMeters
            * Constants.SwerveConstants.kDriveGearRatio;
    SwerveModuleState[] states = {
      new SwerveModuleState(expectedWheelSpeedMetersPerSecond, Rotation2d.kZero),
      new SwerveModuleState(expectedWheelSpeedMetersPerSecond, Rotation2d.kZero),
      new SwerveModuleState(expectedWheelSpeedMetersPerSecond, Rotation2d.kZero),
      new SwerveModuleState(expectedWheelSpeedMetersPerSecond, Rotation2d.kZero)
    };

    rig.setMeasuredStates(states);
    rig.subsystem.periodic();

    assertEquals(expectedRotorRps, rig.frontLeft.driveVelocityRotationsPerSecond, kTolerance);
    ChassisSpeeds measured = rig.subsystem.getMeasuredRobotRelativeSpeeds().orElseThrow();
    assertEquals(expectedWheelSpeedMetersPerSecond, measured.vxMetersPerSecond, kTolerance);
    assertEquals(0.0, measured.vyMetersPerSecond, kTolerance);
    assertEquals(0.0, measured.omegaRadiansPerSecond, kTolerance);
  }

  @Test
  void measuredSpeedsRemainDistinctFromFinalCommandedStates() {
    Rig rig = new Rig();
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    rig.setMeasuredStates(
        new SwerveKinematics().toModuleStates(new ChassisSpeeds(0.25, 0.0, 0.0)));
    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.subsystem.periodic();

    assertEquals(1.0, rig.subsystem.getFinalModuleStates()[0].speedMetersPerSecond, kTolerance);
    assertEquals(
        0.25,
        rig.subsystem.getMeasuredRobotRelativeSpeeds().orElseThrow().vxMetersPerSecond,
        kTolerance);
  }

  @Test
  void rejectsDisconnectedModules() {
    Rig rig = new Rig();
    rig.frontRight.driveConnected = false;
    rig.setMeasuredStates(new SwerveKinematics().toModuleStates(new ChassisSpeeds(0.5, 0.0, 0.0)));
    rig.subsystem.periodic();

    assertTrue(rig.subsystem.getMeasuredRobotRelativeSpeeds().isEmpty());
  }

  @Test
  void rejectsUnhealthyModules() {
    Rig rig = new Rig();
    rig.backLeft.steerConfigurationHealthy = false;
    rig.setMeasuredStates(new SwerveKinematics().toModuleStates(new ChassisSpeeds(0.5, 0.0, 0.0)));
    rig.subsystem.periodic();

    assertTrue(rig.subsystem.getMeasuredRobotRelativeSpeeds().isEmpty());
  }

  @Test
  void rejectsNonfiniteVelocityAndAngle() {
    Rig velocityRig = new Rig();
    velocityRig.frontLeft.driveVelocityRotationsPerSecond = Double.NaN;
    velocityRig.publishMeasuredInputs();
    assertTrue(velocityRig.subsystem.getMeasuredRobotRelativeSpeeds().isEmpty());

    Rig angleRig = new Rig();
    angleRig.frontLeft.encoderAbsolutePositionRotations = Double.POSITIVE_INFINITY;
    angleRig.publishMeasuredInputs();
    assertTrue(angleRig.subsystem.getMeasuredRobotRelativeSpeeds().isEmpty());
  }

  @Test
  void doesNotRequireGyroOrFieldHeading() {
    Rig rig = new Rig();
    rig.gyro.yawDegrees = Double.NaN;
    rig.gyro.connected = false;
    rig.gyro.configurationHealthy = false;
    rig.setMeasuredStates(new SwerveKinematics().toModuleStates(new ChassisSpeeds(0.5, 0.0, 0.0)));
    rig.subsystem.periodic();

    assertFalse(rig.subsystem.hasFieldHeadingReference());
    assertTrue(rig.subsystem.getMeasuredRobotRelativeSpeeds().isPresent());
  }

  @Test
  void returnsDefensiveSpeedSnapshots() {
    Rig rig = new Rig();
    rig.setMeasuredStates(new SwerveKinematics().toModuleStates(new ChassisSpeeds(0.5, 0.0, 0.0)));
    rig.subsystem.periodic();

    ChassisSpeeds first = rig.subsystem.getMeasuredRobotRelativeSpeeds().orElseThrow();
    first.vxMetersPerSecond = 99.0;
    ChassisSpeeds second = rig.subsystem.getMeasuredRobotRelativeSpeeds().orElseThrow();

    assertNotSame(first, second);
    assertEquals(0.5, second.vxMetersPerSecond, kTolerance);
  }

  private static void assertMeasuredSpeeds(ChassisSpeeds expected) {
    Rig rig = new Rig();
    rig.setMeasuredStates(new SwerveKinematics().toModuleStates(expected));
    rig.subsystem.periodic();

    assertMeasuredEquals(expected, rig.subsystem.getMeasuredRobotRelativeSpeeds().orElseThrow());
  }

  private static void assertMeasuredEquals(ChassisSpeeds expected, ChassisSpeeds actual) {
    assertEquals(expected.vxMetersPerSecond, actual.vxMetersPerSecond, kTolerance);
    assertEquals(expected.vyMetersPerSecond, actual.vyMetersPerSecond, kTolerance);
    assertEquals(expected.omegaRadiansPerSecond, actual.omegaRadiansPerSecond, kTolerance);
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final RecordingGyroIO gyro = new RecordingGyroIO();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, gyro);

    private void setMeasuredStates(SwerveModuleState[] states) {
      frontLeft.setMeasuredState(states[0]);
      frontRight.setMeasuredState(states[1]);
      backLeft.setMeasuredState(states[2]);
      backRight.setMeasuredState(states[3]);
    }

    private void publishMeasuredInputs() {
      subsystem.periodic();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private double driveVelocityRotationsPerSecond;
    private double encoderAbsolutePositionRotations;
    private boolean driveConnected = true;
    private boolean steerConnected = true;
    private boolean encoderConnected = true;
    private boolean driveConfigurationHealthy = true;
    private boolean steerConfigurationHealthy = true;
    private boolean encoderConfigurationHealthy = true;

    private void setMeasuredState(SwerveModuleState state) {
      double wheelCircumferenceMeters =
          2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters;
      driveVelocityRotationsPerSecond =
          state.speedMetersPerSecond
              / wheelCircumferenceMeters
              * Constants.SwerveConstants.kDriveGearRatio;
      encoderAbsolutePositionRotations = state.angle.getRotations();
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.driveVelocityRotationsPerSecond = driveVelocityRotationsPerSecond;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
      inputs.driveConnected = driveConnected;
      inputs.steerConnected = steerConnected;
      inputs.encoderConnected = encoderConnected;
      inputs.driveConfigurationHealthy = driveConfigurationHealthy;
      inputs.steerConfigurationHealthy = steerConfigurationHealthy;
      inputs.encoderConfigurationHealthy = encoderConfigurationHealthy;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {}
  }

  private static final class RecordingGyroIO implements GyroIO {
    private double yawDegrees = Double.NaN;
    private boolean connected;
    private boolean configurationHealthy;

    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.yawDegrees = yawDegrees;
      inputs.connected = connected;
      inputs.configurationHealthy = configurationHealthy;
    }
  }
}
