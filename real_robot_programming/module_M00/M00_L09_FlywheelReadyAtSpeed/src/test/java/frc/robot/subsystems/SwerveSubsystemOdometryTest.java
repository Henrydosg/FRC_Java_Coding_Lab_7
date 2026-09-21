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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.SwerveObservation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies subsystem-owned odometry initialization, updates, and fail-safe holding. */
class SwerveSubsystemOdometryTest {
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
  void initializesOnlyAfterValidFieldHeadingAndCompleteHealthyModuleSample() {
    Rig rig = new Rig();
    rig.gyro.yawDegrees = 30.0;
    rig.periodic();

    assertFalse(rig.subsystem.getCurrentPose().isPresent());
    assertTrue(rig.subsystem.getObservation().orElseThrow().currentPose().isEmpty());
    assertTrue(rig.subsystem.captureFieldHeadingReference());

    rig.frontLeft.driveConnected = false;
    rig.gyro.yawDegrees = 75.0;
    rig.periodic();
    assertFalse(rig.subsystem.getCurrentPose().isPresent());
    assertTrue(rig.subsystem.getObservation().orElseThrow().currentPose().isEmpty());

    rig.frontLeft.driveConnected = true;
    rig.periodic();

    Pose2d pose = rig.subsystem.getCurrentPose().orElseThrow();
    assertEquals(0.0, pose.getX(), kTolerance);
    assertEquals(0.0, pose.getY(), kTolerance);
    assertEquals(45.0, pose.getRotation().getDegrees(), kTolerance);
    SwerveObservation.PoseObservation observedPose =
        rig.subsystem.getObservation().orElseThrow().currentPose().orElseThrow();
    assertEquals(pose.getX(), observedPose.xMeters(), kTolerance);
    assertEquals(pose.getY(), observedPose.yMeters(), kTolerance);
    assertEquals(pose.getRotation().getRadians(), observedPose.headingRadians(), kTolerance);
    assertTrue(observedPose.measurementSampleValid());
  }

  @Test
  void integratesPhysicalForwardDistanceInMetersExactlyOncePerPeriodicCycle() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(1.25, 0.0);

    rig.periodic();
    Pose2d firstRead = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d secondRead = rig.subsystem.getCurrentPose().orElseThrow();

    assertEquals(1.25, firstRead.getX(), kTolerance);
    assertEquals(0.0, firstRead.getY(), kTolerance);
    assertEquals(0.0, firstRead.getRotation().getRadians(), kTolerance);
    assertEquals(firstRead, secondRead);
    assertNotSame(firstRead, secondRead);

    rig.periodic();
    assertEquals(1.25, rig.subsystem.getCurrentPose().orElseThrow().getX(), kTolerance);
  }

  @Test
  void integratesPurePositiveFieldYTranslationInMeters() {
    Rig rig = initializedRig();
    double distanceMeters = 0.75;
    rig.setAllModulePositions(distanceMeters, 0.25);

    rig.periodic();

    Pose2d pose = rig.subsystem.getCurrentPose().orElseThrow();
    assertEquals(0.0, pose.getX(), kTolerance);
    assertEquals(distanceMeters, pose.getY(), kTolerance);
    assertEquals(0.0, pose.getRotation().getRadians(), kTolerance);
  }

  @Test
  void integratesCombinedTranslationAndCounterclockwiseRotationInFixedOrder() {
    Rig rig = initializedRig();
    double elapsedSeconds = 0.5;
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0.8, 0.35, 0.6);
    SwerveModuleState[] orderedStates =
        new SwerveKinematics().toModuleStates(chassisSpeeds);
    rig.setOrderedModuleMotion(orderedStates, elapsedSeconds);
    rig.gyro.yawDegrees =
        Math.toDegrees(chassisSpeeds.omegaRadiansPerSecond * elapsedSeconds);

    rig.periodic();

    Pose2d expectedPose =
        new Pose2d()
            .exp(
                new Twist2d(
                    chassisSpeeds.vxMetersPerSecond * elapsedSeconds,
                    chassisSpeeds.vyMetersPerSecond * elapsedSeconds,
                    chassisSpeeds.omegaRadiansPerSecond * elapsedSeconds));
    Pose2d actualPose = rig.subsystem.getCurrentPose().orElseThrow();
    assertEquals(expectedPose.getX(), actualPose.getX(), kTolerance);
    assertEquals(expectedPose.getY(), actualPose.getY(), kTolerance);
    assertEquals(
        expectedPose.getRotation().getRadians(),
        actualPose.getRotation().getRadians(),
        kTolerance);
  }

  @Test
  void gyroHeadingUpdatesPoseRotationWithoutInventingTranslation() {
    Rig rig = initializedRig();
    rig.gyro.yawDegrees = 90.0;

    rig.periodic();

    Pose2d pose = rig.subsystem.getCurrentPose().orElseThrow();
    assertEquals(0.0, pose.getX(), kTolerance);
    assertEquals(0.0, pose.getY(), kTolerance);
    assertEquals(90.0, pose.getRotation().getDegrees(), kTolerance);
  }

  @Test
  void fixedModuleOrderingProducesPureCounterclockwiseRotation() {
    Rig rig = initializedRig();
    double elapsedSeconds = 0.5;
    double omegaRadiansPerSecond = 1.0;
    SwerveModuleState[] orderedStates =
        new SwerveKinematics()
            .toModuleStates(new ChassisSpeeds(0.0, 0.0, omegaRadiansPerSecond));
    rig.setOrderedModuleMotion(orderedStates, elapsedSeconds);
    rig.gyro.yawDegrees = Math.toDegrees(omegaRadiansPerSecond * elapsedSeconds);

    rig.periodic();

    Pose2d pose = rig.subsystem.getCurrentPose().orElseThrow();
    assertEquals(0.0, pose.getX(), kTolerance);
    assertEquals(0.0, pose.getY(), kTolerance);
    assertEquals(
        omegaRadiansPerSecond * elapsedSeconds,
        pose.getRotation().getRadians(),
        kTolerance);
  }

  @Test
  void invalidGyroOrModuleSamplesHoldTheLastValidPose() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(1.0, 0.0);
    rig.periodic();
    Pose2d validPose = rig.subsystem.getCurrentPose().orElseThrow();

    rig.setAllModulePositions(2.0, 0.0);
    rig.frontRight.encoderConfigurationHealthy = false;
    rig.periodic();
    assertEquals(validPose, rig.subsystem.getCurrentPose().orElseThrow());
    SwerveObservation.PoseObservation heldObservation =
        rig.subsystem.getObservation().orElseThrow().currentPose().orElseThrow();
    assertEquals(validPose.getX(), heldObservation.xMeters(), kTolerance);
    assertEquals(validPose.getY(), heldObservation.yMeters(), kTolerance);
    assertEquals(
        validPose.getRotation().getRadians(), heldObservation.headingRadians(), kTolerance);
    assertFalse(heldObservation.measurementSampleValid());

    rig.frontRight.encoderConfigurationHealthy = true;
    rig.periodic();
    assertEquals(validPose, rig.subsystem.getCurrentPose().orElseThrow());
    assertTrue(
        rig.subsystem
            .getObservation()
            .orElseThrow()
            .currentPose()
            .orElseThrow()
            .measurementSampleValid());

    rig.setAllModulePositions(2.5, 0.0);
    rig.periodic();
    assertEquals(1.5, rig.subsystem.getCurrentPose().orElseThrow().getX(), kTolerance);
    Pose2d recoveredPose = rig.subsystem.getCurrentPose().orElseThrow();

    rig.gyro.yawDegrees = Double.NaN;
    rig.periodic();
    assertEquals(recoveredPose, rig.subsystem.getCurrentPose().orElseThrow());

    rig.gyro.yawDegrees = 0.0;
    rig.backLeft.drivePositionRotations = Double.POSITIVE_INFINITY;
    rig.periodic();
    assertEquals(recoveredPose, rig.subsystem.getCurrentPose().orElseThrow());
  }

  private static Rig initializedRig() {
    Rig rig = new Rig();
    rig.periodic();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.periodic();
    assertTrue(rig.subsystem.getCurrentPose().isPresent());
    return rig;
  }

  private static double driveRotorRotations(double distanceMeters, double physicalForwardSign) {
    double wheelCircumferenceMeters =
        2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters;
    return distanceMeters
        / wheelCircumferenceMeters
        * Constants.SwerveConstants.kDriveGearRatio
        / physicalForwardSign;
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final RecordingGyroIO gyro = new RecordingGyroIO();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, gyro);

    private void periodic() {
      subsystem.periodic();
    }

    private void setAllModulePositions(double distanceMeters, double angleRotations) {
      double[] signs = {
        Constants.SwerveConstants.kFrontLeftDrivePositionSign,
        Constants.SwerveConstants.kFrontRightDrivePositionSign,
        Constants.SwerveConstants.kBackLeftDrivePositionSign,
        Constants.SwerveConstants.kBackRightDrivePositionSign
      };
      RecordingModuleIO[] modules = modules();
      for (int moduleIndex = 0; moduleIndex < modules.length; moduleIndex++) {
        modules[moduleIndex].drivePositionRotations =
            driveRotorRotations(distanceMeters, signs[moduleIndex]);
        modules[moduleIndex].encoderAbsolutePositionRotations = angleRotations;
      }
    }

    private void setOrderedModuleMotion(SwerveModuleState[] states, double elapsedSeconds) {
      double[] signs = {
        Constants.SwerveConstants.kFrontLeftDrivePositionSign,
        Constants.SwerveConstants.kFrontRightDrivePositionSign,
        Constants.SwerveConstants.kBackLeftDrivePositionSign,
        Constants.SwerveConstants.kBackRightDrivePositionSign
      };
      RecordingModuleIO[] modules = modules();
      for (int moduleIndex = 0; moduleIndex < modules.length; moduleIndex++) {
        modules[moduleIndex].drivePositionRotations =
            driveRotorRotations(
                states[moduleIndex].speedMetersPerSecond * elapsedSeconds,
                signs[moduleIndex]);
        modules[moduleIndex].encoderAbsolutePositionRotations =
            states[moduleIndex].angle.getRotations();
      }
    }

    private RecordingModuleIO[] modules() {
      return new RecordingModuleIO[] {frontLeft, frontRight, backLeft, backRight};
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private double drivePositionRotations;
    private double encoderAbsolutePositionRotations;
    private boolean driveConnected = true;
    private boolean steerConnected = true;
    private boolean encoderConnected = true;
    private boolean driveConfigurationHealthy = true;
    private boolean steerConfigurationHealthy = true;
    private boolean encoderConfigurationHealthy = true;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.drivePositionRotations = drivePositionRotations;
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
    public void setSteerAngle(edu.wpi.first.math.geometry.Rotation2d angle) {}

    @Override
    public void stop() {}
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
