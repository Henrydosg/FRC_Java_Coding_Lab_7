// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.commands.ResetKnownFieldPoseCommand;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.SwerveObservation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the subsystem-owned atomic known-field-pose reset foundation. */
class SwerveSubsystemKnownFieldPoseResetTest {
  private static final double kTolerance = 1.0e-9;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    CommandScheduler.getInstance().cancelAll();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void resetsBothPosesToAnArbitraryNonzeroFieldPoseUsingNonzeroModulePositions() {
    Rig rig = initializedRig();
    rig.setModulePositions(
        new double[] {0.8, 0.9, 1.0, 1.1},
        new double[] {0.0, 0.25, 0.5, 0.75});
    rig.periodic();

    Pose2d previousPose = rig.subsystem.getCurrentPose().orElseThrow();
    SwerveObservation previousObservation = rig.subsystem.getObservation().orElseThrow();
    Pose2d requestedPose =
        new Pose2d(4.25, -1.75, Rotation2d.fromRadians(1.25));

    assertTrue(rig.subsystem.resetKnownFieldPose(requestedPose));
    assertPoseEquals(requestedPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(requestedPose, rig.subsystem.getEstimatedPose());
    assertFalse(previousPose.equals(requestedPose));
    assertSame(previousObservation, rig.subsystem.getObservation().orElseThrow());
    assertEquals(
        previousPose.getX(),
        previousObservation.currentPose().orElseThrow().xMeters(),
        kTolerance);

    rig.periodic();

    assertPoseEquals(requestedPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(requestedPose, rig.subsystem.getEstimatedPose());
    SwerveObservation resetObservation = rig.subsystem.getObservation().orElseThrow();
    assertTrue(resetObservation.currentPose().orElseThrow().measurementSampleValid());
    assertTrue(resetObservation.estimatedPose().orElseThrow().measurementSampleValid());
    assertEquals(
        requestedPose.getX(),
        resetObservation.currentPose().orElseThrow().xMeters(),
        kTolerance);
    assertEquals(
        requestedPose.getX(),
        resetObservation.estimatedPose().orElseThrow().xMeters(),
        kTolerance);
  }

  @Test
  void unchangedSensorSampleAfterResetProducesNoMovement() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(0.65, 0.0);
    rig.periodic();
    Pose2d requestedPose =
        new Pose2d(-2.0, 3.5, Rotation2d.fromDegrees(-110.0));

    assertTrue(rig.subsystem.resetKnownFieldPose(requestedPose));

    rig.periodic();

    assertPoseEquals(requestedPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(requestedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void continuesTranslationFromTheResetSensorBaseline() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(0.5, 0.0);
    rig.periodic();
    Pose2d requestedPose = new Pose2d(3.0, 4.0, new Rotation2d());

    assertTrue(rig.subsystem.resetKnownFieldPose(requestedPose));
    rig.setAllModulePositions(0.9, 0.0);
    rig.periodic();

    Pose2d actualPose = rig.subsystem.getCurrentPose().orElseThrow();
    assertEquals(3.4, actualPose.getX(), kTolerance);
    assertEquals(4.0, actualPose.getY(), kTolerance);
    assertEquals(0.0, actualPose.getRotation().getRadians(), kTolerance);
    assertPoseEquals(actualPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void continuesRotationFromTheResetSensorBaseline() {
    Rig rig = initializedRig();
    double baseDistanceMeters = 0.4;
    rig.setAllModulePositions(baseDistanceMeters, 0.0);
    rig.periodic();
    Pose2d requestedPose = new Pose2d(1.0, 2.0, new Rotation2d());
    assertTrue(rig.subsystem.resetKnownFieldPose(requestedPose));

    double elapsedSeconds = 0.5;
    double omegaRadiansPerSecond = 0.8;
    SwerveModuleState[] states =
        new SwerveKinematics()
            .toModuleStates(new ChassisSpeeds(0.0, 0.0, omegaRadiansPerSecond));
    rig.setOrderedModuleMotion(states, elapsedSeconds, baseDistanceMeters);
    rig.gyro.yawDegrees = Math.toDegrees(omegaRadiansPerSecond * elapsedSeconds);
    rig.periodic();

    Pose2d expectedPose =
        requestedPose.exp(new Twist2d(0.0, 0.0, omegaRadiansPerSecond * elapsedSeconds));
    assertPoseEquals(expectedPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(expectedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void preservesFixedFrontLeftFrontRightBackLeftBackRightModuleOrdering() {
    Rig rig = initializedRig();
    double[] distances = {0.2, 0.4, 0.6, 0.8};
    double[] angles = {0.0, 0.25, 0.5, 0.75};
    rig.setModulePositions(distances, angles);
    rig.periodic();

    SwerveModulePosition[] measured = rig.subsystem.getMeasuredModulePositions();
    assertEquals(4, measured.length);
    for (int moduleIndex = 0; moduleIndex < measured.length; moduleIndex++) {
      assertEquals(distances[moduleIndex], measured[moduleIndex].distanceMeters, kTolerance);
      assertEquals(angles[moduleIndex], measured[moduleIndex].angle.getRotations(), kTolerance);
    }

    Pose2d requestedPose = new Pose2d(2.0, 2.0, Rotation2d.fromDegrees(30.0));
    assertTrue(rig.subsystem.resetKnownFieldPose(requestedPose));
    rig.periodic();
    assertPoseEquals(requestedPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(requestedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void rejectsNullAndNonfiniteRequestedPosesWithoutChangingLocalization() {
    Rig rig = initializedRig();
    Pose2d previousPose = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d previousEstimatedPose = rig.subsystem.getEstimatedPose().orElseThrow();

    assertFalse(rig.subsystem.resetKnownFieldPose(null));
    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(Double.NaN, 0.0, new Rotation2d())));
    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(0.0, Double.POSITIVE_INFINITY, new Rotation2d())));
    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(0.0, 0.0, Rotation2d.fromRadians(Double.NaN))));

    assertPoseEquals(previousPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(previousEstimatedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void rejectsMissingFieldHeadingReference() {
    Rig rig = new Rig();
    rig.periodic();

    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(1.0, 2.0, Rotation2d.fromDegrees(15.0))));
    assertTrue(rig.subsystem.getCurrentPose().isEmpty());
    assertTrue(rig.subsystem.getEstimatedPose().isEmpty());
  }

  @Test
  void rejectsUninitializedLocalizationEvenWithAValidFieldHeading() {
    Rig rig = new Rig();
    rig.periodic();
    assertTrue(rig.subsystem.captureFieldHeadingReference());

    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(1.0, 2.0, Rotation2d.fromDegrees(15.0))));
    assertTrue(rig.subsystem.getCurrentPose().isEmpty());
    assertTrue(rig.subsystem.getEstimatedPose().isEmpty());
  }

  @Test
  void rejectsUnhealthyGyroAndPreservesPreviousPoses() {
    Rig rig = initializedRig();
    Pose2d previousPose = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d previousEstimatedPose = rig.subsystem.getEstimatedPose().orElseThrow();
    rig.gyro.connected = false;
    rig.periodic();

    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(5.0, 6.0, Rotation2d.fromDegrees(45.0))));
    assertPoseEquals(previousPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(previousEstimatedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void rejectsUnhealthyModuleAndPreservesPreviousPoses() {
    Rig rig = initializedRig();
    Pose2d previousPose = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d previousEstimatedPose = rig.subsystem.getEstimatedPose().orElseThrow();
    rig.backLeft.encoderConfigurationHealthy = false;
    rig.periodic();

    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(5.0, 6.0, Rotation2d.fromDegrees(45.0))));
    assertPoseEquals(previousPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(previousEstimatedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void rejectsNonfiniteCurrentMeasurementsAndPreservesPreviousPoses() {
    Rig rig = initializedRig();
    Pose2d previousPose = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d previousEstimatedPose = rig.subsystem.getEstimatedPose().orElseThrow();

    rig.gyro.yawDegrees = Double.NaN;
    rig.periodic();
    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(5.0, 6.0, Rotation2d.fromDegrees(45.0))));
    assertPoseEquals(previousPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(previousEstimatedPose, rig.subsystem.getEstimatedPose());

    rig.gyro.yawDegrees = 0.0;
    rig.backRight.drivePositionRotations = Double.POSITIVE_INFINITY;
    rig.periodic();
    assertFalse(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(5.0, 6.0, Rotation2d.fromDegrees(45.0))));
    assertPoseEquals(previousPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(previousEstimatedPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void repeatedResetReplacesBothLocalizationStates() {
    Rig rig = initializedRig();
    Pose2d firstPose = new Pose2d(1.0, 2.0, Rotation2d.fromDegrees(20.0));
    Pose2d secondPose = new Pose2d(-3.0, 4.0, Rotation2d.fromDegrees(-70.0));

    assertTrue(rig.subsystem.resetKnownFieldPose(firstPose));
    assertTrue(rig.subsystem.resetKnownFieldPose(secondPose));

    assertPoseEquals(secondPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(secondPose, rig.subsystem.getEstimatedPose());
    rig.periodic();
    assertPoseEquals(secondPose, rig.subsystem.getCurrentPose());
    assertPoseEquals(secondPose, rig.subsystem.getEstimatedPose());
  }

  @Test
  void returnsDefensiveSnapshotsAfterReset() {
    Rig rig = initializedRig();
    Pose2d requestedPose = new Pose2d(2.0, -1.0, Rotation2d.fromDegrees(135.0));
    assertTrue(rig.subsystem.resetKnownFieldPose(requestedPose));

    Pose2d currentFirstRead = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d currentSecondRead = rig.subsystem.getCurrentPose().orElseThrow();
    Pose2d estimatedFirstRead = rig.subsystem.getEstimatedPose().orElseThrow();
    Pose2d estimatedSecondRead = rig.subsystem.getEstimatedPose().orElseThrow();

    assertNotSame(currentFirstRead, currentSecondRead);
    assertNotSame(estimatedFirstRead, estimatedSecondRead);
    assertPoseEquals(requestedPose, currentFirstRead);
    assertPoseEquals(requestedPose, estimatedFirstRead);
  }

  @Test
  void resetDoesNotChangePhysicalSensorValuesOrInvokeSensorResetBehavior() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(0.75, 0.125);
    rig.gyro.yawDegrees = 37.0;
    rig.periodic();

    double[] rawRotorPositions = rig.rawRotorPositions();
    double rawYawDegrees = rig.gyro.yawDegrees;
    int[] stopCounts = rig.stopCounts();
    assertTrue(
        rig.subsystem.resetKnownFieldPose(
            new Pose2d(7.0, -4.0, Rotation2d.fromDegrees(95.0))));

    assertEquals(rawYawDegrees, rig.gyro.yawDegrees, kTolerance);
    assertEquals(rawRotorPositions[0], rig.frontLeft.drivePositionRotations, kTolerance);
    assertEquals(rawRotorPositions[1], rig.frontRight.drivePositionRotations, kTolerance);
    assertEquals(rawRotorPositions[2], rig.backLeft.drivePositionRotations, kTolerance);
    assertEquals(rawRotorPositions[3], rig.backRight.drivePositionRotations, kTolerance);
    assertEquals(stopCounts[0], rig.frontLeft.stopCalls);
    assertEquals(stopCounts[1], rig.frontRight.stopCalls);
    assertEquals(stopCounts[2], rig.backLeft.stopCalls);
    assertEquals(stopCounts[3], rig.backRight.stopCalls);
  }

  @Test
  void scheduledPersistentResetPreservesBaselineAndCanResetAgainAfterMotion() {
    Rig rig = initializedRig();
    rig.setAllModulePositions(0.75, 0.0);
    rig.periodic();
    assertEquals(0.75, rig.subsystem.getCurrentPose().orElseThrow().getX(), kTolerance);

    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(rig.subsystem, Pose2d.kZero);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(command);
    scheduler.run();
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getCurrentPose());
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getEstimatedPose());

    rig.periodic();
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getCurrentPose());
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getEstimatedPose());
    SwerveObservation observation = rig.subsystem.getObservation().orElseThrow();
    assertEquals(0.0, observation.currentPose().orElseThrow().xMeters(), kTolerance);
    assertEquals(0.0, observation.estimatedPose().orElseThrow().xMeters(), kTolerance);

    rig.setAllModulePositions(1.15, 0.0);
    rig.periodic();
    assertEquals(0.4, rig.subsystem.getCurrentPose().orElseThrow().getX(), kTolerance);

    scheduler.schedule(command);
    scheduler.run();
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getCurrentPose());
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getEstimatedPose());

    rig.periodic();
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getCurrentPose());
    assertPoseEquals(Pose2d.kZero, rig.subsystem.getEstimatedPose());
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
      setModulePositions(
          new double[] {distanceMeters, distanceMeters, distanceMeters, distanceMeters},
          new double[] {angleRotations, angleRotations, angleRotations, angleRotations});
    }

    private void setModulePositions(double[] distancesMeters, double[] anglesRotations) {
      double[] signs = physicalForwardSigns();
      RecordingModuleIO[] modules = modules();
      for (int moduleIndex = 0; moduleIndex < modules.length; moduleIndex++) {
        modules[moduleIndex].drivePositionRotations =
            driveRotorRotations(distancesMeters[moduleIndex], signs[moduleIndex]);
        modules[moduleIndex].encoderAbsolutePositionRotations = anglesRotations[moduleIndex];
      }
    }

    private void setOrderedModuleMotion(
        SwerveModuleState[] states, double elapsedSeconds, double baseDistanceMeters) {
      double[] signs = physicalForwardSigns();
      RecordingModuleIO[] modules = modules();
      for (int moduleIndex = 0; moduleIndex < modules.length; moduleIndex++) {
        modules[moduleIndex].drivePositionRotations =
            driveRotorRotations(
                baseDistanceMeters + states[moduleIndex].speedMetersPerSecond * elapsedSeconds,
                signs[moduleIndex]);
        modules[moduleIndex].encoderAbsolutePositionRotations =
            states[moduleIndex].angle.getRotations();
      }
    }

    private double[] rawRotorPositions() {
      return new double[] {
        frontLeft.drivePositionRotations,
        frontRight.drivePositionRotations,
        backLeft.drivePositionRotations,
        backRight.drivePositionRotations
      };
    }

    private int[] stopCounts() {
      return new int[] {
        frontLeft.stopCalls, frontRight.stopCalls, backLeft.stopCalls, backRight.stopCalls
      };
    }

    private RecordingModuleIO[] modules() {
      return new RecordingModuleIO[] {frontLeft, frontRight, backLeft, backRight};
    }

    private static double[] physicalForwardSigns() {
      return new double[] {
        Constants.SwerveConstants.kFrontLeftDrivePositionSign,
        Constants.SwerveConstants.kFrontRightDrivePositionSign,
        Constants.SwerveConstants.kBackLeftDrivePositionSign,
        Constants.SwerveConstants.kBackRightDrivePositionSign
      };
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private double drivePositionRotations;
    private double encoderAbsolutePositionRotations;
    private int stopCalls;
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
    public void stop() {
      stopCalls++;
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
