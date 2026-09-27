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
import edu.wpi.first.math.geometry.Rotation2d;
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

/** Verifies the subsystem-owned estimator foundation against the L23 odometry contract. */
class SwerveSubsystemPoseEstimatorTest {
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
  void estimatedPoseIsUnavailableBeforeValidInitialization() {
    Rig rig = new Rig();

    rig.periodic();

    assertTrue(rig.subsystem.getEstimatedPose().isEmpty());
    assertTrue(rig.subsystem.captureFieldHeadingReference());

    rig.frontLeft.driveConnected = false;
    rig.periodic();

    assertTrue(rig.subsystem.getEstimatedPose().isEmpty());
  }

  @Test
  void initializesAtAcceptedFieldHeading() {
    Rig rig = new Rig();
    rig.gyro.yawDegrees = 30.0;
    rig.periodic();

    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.gyro.yawDegrees = 75.0;
    rig.periodic();

    Pose2d pose = rig.subsystem.getEstimatedPose().orElseThrow();
    assertEquals(0.0, pose.getX(), kTolerance);
    assertEquals(0.0, pose.getY(), kTolerance);
    assertEquals(45.0, pose.getRotation().getDegrees(), kTolerance);
  }

  @Test
  void observationExposesEstimatedPoseOnlyAfterEstimatorInitialization() {
    Rig rig = new Rig();
    rig.periodic();

    SwerveObservation unavailable = rig.subsystem.getObservation().orElseThrow();
    assertTrue(unavailable.currentPose().isEmpty());
    assertTrue(unavailable.estimatedPose().isEmpty());

    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.periodic();

    SwerveObservation initialized = rig.subsystem.getObservation().orElseThrow();
    SwerveObservation.PoseObservation currentPose =
        initialized.currentPose().orElseThrow();
    SwerveObservation.EstimatedPoseObservation estimatedPose =
        initialized.estimatedPose().orElseThrow();
    assertEquals(currentPose.xMeters(), estimatedPose.xMeters(), kTolerance);
    assertEquals(currentPose.yMeters(), estimatedPose.yMeters(), kTolerance);
    assertEquals(currentPose.headingRadians(), estimatedPose.headingRadians(), kTolerance);
    assertTrue(currentPose.measurementSampleValid());
    assertTrue(estimatedPose.measurementSampleValid());
  }

  @Test
  void tracksPositiveXInMetersAndMatchesL23Odometry() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(1.25, 0.0);

    rig.periodic();

    assertPoseEquals(new Pose2d(1.25, 0.0, new Rotation2d()), rig.subsystem.getEstimatedPose());
    assertEstimatorMatchesOdometry(rig);
  }

  @Test
  void tracksPositiveYInMetersAndMatchesL23Odometry() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(0.75, 0.25);

    rig.periodic();

    assertPoseEquals(new Pose2d(0.0, 0.75, new Rotation2d()), rig.subsystem.getEstimatedPose());
    assertEstimatorMatchesOdometry(rig);
  }

  @Test
  void tracksCounterclockwiseAndClockwiseRotationInRadians() {
    verifyPureRotation(1.0);
    verifyPureRotation(-1.0);
  }

  @Test
  void tracksCombinedTranslationAndRotationInFixedModuleOrder() {
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
    assertPoseEquals(expectedPose, rig.subsystem.getEstimatedPose());
    assertEstimatorMatchesOdometry(rig);
  }

  @Test
  void repeatedReadsAndSameSampleDoNotAdvanceEstimatorTwice() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(1.25, 0.0);

    rig.periodic();
    Pose2d firstRead = rig.subsystem.getEstimatedPose().orElseThrow();
    Pose2d secondRead = rig.subsystem.getEstimatedPose().orElseThrow();
    assertNotSame(firstRead, secondRead);

    rig.periodic();

    assertPoseEquals(firstRead, rig.subsystem.getEstimatedPose());
    assertEstimatorMatchesOdometry(rig);
  }

  @Test
  void invalidGyroOrModuleSamplesHoldAndRecoverWithoutIntegratingTheGap() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(1.0, 0.0);
    rig.periodic();
    Pose2d validPose = rig.subsystem.getEstimatedPose().orElseThrow();

    rig.setAllModulePositions(2.0, 0.0);
    rig.frontRight.encoderConfigurationHealthy = false;
    rig.periodic();
    assertPoseEquals(validPose, rig.subsystem.getEstimatedPose());

    rig.frontRight.encoderConfigurationHealthy = true;
    rig.periodic();
    assertPoseEquals(validPose, rig.subsystem.getEstimatedPose());

    rig.setAllModulePositions(2.5, 0.0);
    rig.periodic();
    Pose2d recoveredPose = rig.subsystem.getEstimatedPose().orElseThrow();
    assertEquals(1.5, recoveredPose.getX(), kTolerance);

    rig.gyro.yawDegrees = Double.NaN;
    rig.setAllModulePositions(4.0, 0.0);
    rig.periodic();
    assertPoseEquals(recoveredPose, rig.subsystem.getEstimatedPose());

    rig.gyro.yawDegrees = 0.0;
    rig.backLeft.drivePositionRotations = Double.POSITIVE_INFINITY;
    rig.periodic();
    assertPoseEquals(recoveredPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void observationPreservesCurrentPoseMeaningAndReportsEstimatedHoldAndRecoveryValidity() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(1.0, 0.0);
    rig.periodic();

    rig.setAllModulePositions(2.0, 0.0);
    rig.frontRight.encoderConfigurationHealthy = false;
    rig.periodic();

    SwerveObservation held = rig.subsystem.getObservation().orElseThrow();
    SwerveObservation.PoseObservation heldCurrentPose = held.currentPose().orElseThrow();
    SwerveObservation.EstimatedPoseObservation heldEstimatedPose =
        held.estimatedPose().orElseThrow();
    assertEquals(1.0, heldCurrentPose.xMeters(), kTolerance);
    assertEquals(1.0, heldEstimatedPose.xMeters(), kTolerance);
    assertFalse(heldCurrentPose.measurementSampleValid());
    assertFalse(heldEstimatedPose.measurementSampleValid());

    rig.frontRight.encoderConfigurationHealthy = true;
    rig.periodic();

    SwerveObservation recovered = rig.subsystem.getObservation().orElseThrow();
    assertTrue(recovered.currentPose().orElseThrow().measurementSampleValid());
    assertTrue(recovered.estimatedPose().orElseThrow().measurementSampleValid());
    assertEquals(1.0, recovered.currentPose().orElseThrow().xMeters(), kTolerance);
    assertEquals(1.0, recovered.estimatedPose().orElseThrow().xMeters(), kTolerance);

    rig.setAllModulePositions(2.5, 0.0);
    rig.periodic();
    SwerveObservation progressed = rig.subsystem.getObservation().orElseThrow();
    assertEquals(1.5, progressed.currentPose().orElseThrow().xMeters(), kTolerance);
    assertEquals(1.5, progressed.estimatedPose().orElseThrow().xMeters(), kTolerance);
    assertTrue(progressed.currentPose().orElseThrow().measurementSampleValid());
    assertTrue(progressed.estimatedPose().orElseThrow().measurementSampleValid());
  }

  @Test
  void estimatorMatchesL23OdometryForIdenticalValidInputsWithoutVision() {
    Rig rig = initializedRig();

    rig.setAllModulePositions(0.4, 0.0);
    rig.periodic();
    assertEstimatorMatchesOdometry(rig);

    rig.setAllModulePositions(0.9, 0.25);
    rig.periodic();
    assertEstimatorMatchesOdometry(rig);

    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0.5, -0.2, -0.4);
    double elapsedSeconds = 0.4;
    rig.setOrderedModuleMotion(
        new SwerveKinematics().toModuleStates(chassisSpeeds), elapsedSeconds);
    rig.gyro.yawDegrees =
        Math.toDegrees(chassisSpeeds.omegaRadiansPerSecond * elapsedSeconds);
    rig.periodic();
    assertEstimatorMatchesOdometry(rig);
  }

  @Test
  void estimatedPoseSnapshotIsDefensive() {
    Rig rig = initializedRig();
    Pose2d firstRead = rig.subsystem.getEstimatedPose().orElseThrow();
    Pose2d secondRead = rig.subsystem.getEstimatedPose().orElseThrow();

    assertNotSame(firstRead, secondRead);
    assertPoseEquals(firstRead, rig.subsystem.getEstimatedPose());
  }

  private static void verifyPureRotation(double omegaRadiansPerSecond) {
    Rig rig = initializedRig();
    double elapsedSeconds = 0.5;
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(0.0, 0.0, omegaRadiansPerSecond);
    rig.setOrderedModuleMotion(
        new SwerveKinematics().toModuleStates(chassisSpeeds), elapsedSeconds);
    rig.gyro.yawDegrees = Math.toDegrees(omegaRadiansPerSecond * elapsedSeconds);

    rig.periodic();

    Pose2d expectedPose =
        new Pose2d()
            .exp(new Twist2d(0.0, 0.0, omegaRadiansPerSecond * elapsedSeconds));
    assertPoseEquals(expectedPose, rig.subsystem.getEstimatedPose());
    assertEstimatorMatchesOdometry(rig);
  }

  private static Rig initializedRig() {
    Rig rig = new Rig();
    rig.periodic();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.periodic();
    assertTrue(rig.subsystem.getCurrentPose().isPresent());
    assertTrue(rig.subsystem.getEstimatedPose().isPresent());
    return rig;
  }

  private static void assertEstimatorMatchesOdometry(Rig rig) {
    Pose2d odometryPose = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d estimatedPose = rig.subsystem.getEstimatedPose().orElseThrow();
    assertPoseEquals(odometryPose, rig.subsystem.getEstimatedPose());
    assertEquals(odometryPose.getX(), estimatedPose.getX(), kTolerance);
    assertEquals(odometryPose.getY(), estimatedPose.getY(), kTolerance);
    assertEquals(
        odometryPose.getRotation().getRadians(),
        estimatedPose.getRotation().getRadians(),
        kTolerance);
  }

  private static void assertPoseEquals(Pose2d expected, java.util.Optional<Pose2d> actual) {
    assertTrue(actual.isPresent());
    assertPoseEquals(expected, actual.orElseThrow());
  }

  private static void assertPoseEquals(Pose2d expected, Pose2d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(
        expected.getRotation().getRadians(), actual.getRotation().getRadians(), kTolerance);
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
    public void setSteerAngle(Rotation2d angle) {}

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
