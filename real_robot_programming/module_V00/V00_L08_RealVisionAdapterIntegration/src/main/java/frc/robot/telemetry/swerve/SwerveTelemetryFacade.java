// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.swerve;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.observation.SwerveObservation;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Publishes selected immutable Swerve observations without controlling robot behavior.
 */
public final class SwerveTelemetryFacade implements AutoCloseable {
  private static final String FIELD_DASHBOARD_KEY = "Swerve/Field";

  private final ModulePublishers frontLeft;
  private final ModulePublishers frontRight;
  private final ModulePublishers backLeft;
  private final ModulePublishers backRight;

  private final DoublePublisher yawDegreesPublisher;
  private final DoublePublisher pitchDegreesPublisher;
  private final DoublePublisher rollDegreesPublisher;
  private final DoublePublisher angularVelocityZDegreesPerSecondPublisher;
  private final BooleanPublisher gyroConnectedPublisher;
  private final BooleanPublisher gyroConfigurationHealthyPublisher;

  private final BooleanPublisher poseAvailablePublisher;
  private final DoublePublisher poseXMetersPublisher;
  private final DoublePublisher poseYMetersPublisher;
  private final DoublePublisher poseHeadingDegreesPublisher;
  private final BooleanPublisher poseMeasurementSampleValidPublisher;
  private final BooleanPublisher estimatedPoseAvailablePublisher;
  private final DoublePublisher estimatedPoseXMetersPublisher;
  private final DoublePublisher estimatedPoseYMetersPublisher;
  private final DoublePublisher estimatedPoseHeadingDegreesPublisher;
  private final BooleanPublisher estimatedPoseMeasurementSampleValidPublisher;
  private final Field2d field;
  private final Consumer<Field2d> fieldRegistrar;

  private boolean fieldPublished;

  /**
   * Creates typed publishers under the Swerve telemetry table.
   *
   * @param swerveTable root Swerve telemetry table
   */
  public SwerveTelemetryFacade(NetworkTable swerveTable) {
    this(
        swerveTable,
        new Field2d(),
        field -> SmartDashboard.putData(FIELD_DASHBOARD_KEY, field));
  }

  SwerveTelemetryFacade(
      NetworkTable swerveTable,
      Field2d field,
      Consumer<Field2d> fieldRegistrar) {
    Objects.requireNonNull(swerveTable, "swerveTable");
    this.field = Objects.requireNonNull(field, "field");
    this.fieldRegistrar = Objects.requireNonNull(fieldRegistrar, "fieldRegistrar");

    frontLeft = new ModulePublishers(swerveTable.getSubTable("FrontLeft"));
    frontRight = new ModulePublishers(swerveTable.getSubTable("FrontRight"));
    backLeft = new ModulePublishers(swerveTable.getSubTable("BackLeft"));
    backRight = new ModulePublishers(swerveTable.getSubTable("BackRight"));

    NetworkTable gyroTable = swerveTable.getSubTable("Gyro");
    yawDegreesPublisher = gyroTable.getDoubleTopic("YawDegrees").publish();
    pitchDegreesPublisher = gyroTable.getDoubleTopic("PitchDegrees").publish();
    rollDegreesPublisher = gyroTable.getDoubleTopic("RollDegrees").publish();
    angularVelocityZDegreesPerSecondPublisher =
        gyroTable.getDoubleTopic("AngularVelocityZDegreesPerSecond").publish();
    gyroConnectedPublisher = gyroTable.getBooleanTopic("Connected").publish();
    gyroConfigurationHealthyPublisher =
        gyroTable.getBooleanTopic("ConfigurationHealthy").publish();

    NetworkTable poseTable = swerveTable.getSubTable("Pose");
    poseAvailablePublisher = poseTable.getBooleanTopic("Available").publish();
    poseXMetersPublisher = poseTable.getDoubleTopic("XMeters").publish();
    poseYMetersPublisher = poseTable.getDoubleTopic("YMeters").publish();
    poseHeadingDegreesPublisher = poseTable.getDoubleTopic("HeadingDegrees").publish();
    poseMeasurementSampleValidPublisher =
        poseTable.getBooleanTopic("MeasurementSampleValid").publish();

    NetworkTable estimatedPoseTable = swerveTable.getSubTable("EstimatedPose");
    estimatedPoseAvailablePublisher =
        estimatedPoseTable.getBooleanTopic("Available").publish();
    estimatedPoseXMetersPublisher = estimatedPoseTable.getDoubleTopic("XMeters").publish();
    estimatedPoseYMetersPublisher = estimatedPoseTable.getDoubleTopic("YMeters").publish();
    estimatedPoseHeadingDegreesPublisher =
        estimatedPoseTable.getDoubleTopic("HeadingDegrees").publish();
    estimatedPoseMeasurementSampleValidPublisher =
        estimatedPoseTable.getBooleanTopic("MeasurementSampleValid").publish();
  }

  /**
   * Publishes the approved diagnostic subset from one immutable observation.
   *
   * @param observation immutable Swerve observation
   */
  public void publish(SwerveObservation observation) {
    Objects.requireNonNull(observation, "observation");

    frontLeft.publish(observation.frontLeft());
    frontRight.publish(observation.frontRight());
    backLeft.publish(observation.backLeft());
    backRight.publish(observation.backRight());

    SwerveObservation.GyroObservation gyro = observation.gyro();
    yawDegreesPublisher.set(gyro.yawDegrees());
    pitchDegreesPublisher.set(gyro.pitchDegrees());
    rollDegreesPublisher.set(gyro.rollDegrees());
    angularVelocityZDegreesPerSecondPublisher.set(
        gyro.angularVelocityZDegreesPerSecond());
    gyroConnectedPublisher.set(gyro.connected());
    gyroConfigurationHealthyPublisher.set(gyro.configurationHealthy());

    publishPose(observation);
    publishEstimatedPose(observation);
  }

  private void publishPose(SwerveObservation observation) {
    Optional<SwerveObservation.PoseObservation> currentPose = observation.currentPose();
    if (currentPose.isEmpty()) {
      poseAvailablePublisher.set(false);
      poseMeasurementSampleValidPublisher.set(false);
      return;
    }

    SwerveObservation.PoseObservation pose = currentPose.orElseThrow();
    poseAvailablePublisher.set(true);
    poseXMetersPublisher.set(pose.xMeters());
    poseYMetersPublisher.set(pose.yMeters());
    poseHeadingDegreesPublisher.set(Math.toDegrees(pose.headingRadians()));
    poseMeasurementSampleValidPublisher.set(pose.measurementSampleValid());

    field.setRobotPose(
        new Pose2d(
            pose.xMeters(),
            pose.yMeters(),
            Rotation2d.fromRadians(pose.headingRadians())));
    if (!fieldPublished) {
      fieldRegistrar.accept(field);
      fieldPublished = true;
    }
  }

  private void publishEstimatedPose(SwerveObservation observation) {
    Optional<SwerveObservation.EstimatedPoseObservation> estimatedPose =
        observation.estimatedPose();
    if (estimatedPose.isEmpty()) {
      estimatedPoseAvailablePublisher.set(false);
      estimatedPoseMeasurementSampleValidPublisher.set(false);
      return;
    }

    SwerveObservation.EstimatedPoseObservation pose = estimatedPose.orElseThrow();
    estimatedPoseAvailablePublisher.set(true);
    estimatedPoseXMetersPublisher.set(pose.xMeters());
    estimatedPoseYMetersPublisher.set(pose.yMeters());
    estimatedPoseHeadingDegreesPublisher.set(Math.toDegrees(pose.headingRadians()));
    estimatedPoseMeasurementSampleValidPublisher.set(pose.measurementSampleValid());
  }

  /**
   * Closes every publisher handle owned by this facade.
   */
  @Override
  public void close() {
    frontLeft.close();
    frontRight.close();
    backLeft.close();
    backRight.close();
    yawDegreesPublisher.close();
    pitchDegreesPublisher.close();
    rollDegreesPublisher.close();
    angularVelocityZDegreesPerSecondPublisher.close();
    gyroConnectedPublisher.close();
    gyroConfigurationHealthyPublisher.close();
    poseAvailablePublisher.close();
    poseXMetersPublisher.close();
    poseYMetersPublisher.close();
    poseHeadingDegreesPublisher.close();
    poseMeasurementSampleValidPublisher.close();
    estimatedPoseAvailablePublisher.close();
    estimatedPoseXMetersPublisher.close();
    estimatedPoseYMetersPublisher.close();
    estimatedPoseHeadingDegreesPublisher.close();
    estimatedPoseMeasurementSampleValidPublisher.close();
    field.close();
  }

  /** Owns the typed publishers for one module subtable. */
  private static final class ModulePublishers implements AutoCloseable {
    private final DoublePublisher driveAppliedOutputPublisher;
    private final DoublePublisher drivePositionRotationsPublisher;
    private final DoublePublisher driveVelocityRotationsPerSecondPublisher;
    private final DoublePublisher steerAppliedOutputPublisher;
    private final DoublePublisher steerPositionRotationsPublisher;
    private final DoublePublisher steerVelocityRotationsPerSecondPublisher;
    private final DoublePublisher encoderAbsolutePositionRotationsPublisher;
    private final BooleanPublisher driveConnectedPublisher;
    private final BooleanPublisher steerConnectedPublisher;
    private final BooleanPublisher encoderConnectedPublisher;
    private final BooleanPublisher driveConfigurationHealthyPublisher;
    private final BooleanPublisher steerConfigurationHealthyPublisher;
    private final BooleanPublisher encoderConfigurationHealthyPublisher;

    private ModulePublishers(NetworkTable moduleTable) {
      driveAppliedOutputPublisher =
          moduleTable.getDoubleTopic("DriveAppliedOutput").publish();
      drivePositionRotationsPublisher =
          moduleTable.getDoubleTopic("DrivePositionRotations").publish();
      driveVelocityRotationsPerSecondPublisher =
          moduleTable.getDoubleTopic("DriveVelocityRotationsPerSecond").publish();
      steerAppliedOutputPublisher =
          moduleTable.getDoubleTopic("SteerAppliedOutput").publish();
      steerPositionRotationsPublisher =
          moduleTable.getDoubleTopic("SteerPositionRotations").publish();
      steerVelocityRotationsPerSecondPublisher =
          moduleTable.getDoubleTopic("SteerVelocityRotationsPerSecond").publish();
      encoderAbsolutePositionRotationsPublisher =
          moduleTable.getDoubleTopic("EncoderAbsolutePositionRotations").publish();
      driveConnectedPublisher = moduleTable.getBooleanTopic("DriveConnected").publish();
      steerConnectedPublisher = moduleTable.getBooleanTopic("SteerConnected").publish();
      encoderConnectedPublisher = moduleTable.getBooleanTopic("EncoderConnected").publish();
      driveConfigurationHealthyPublisher =
          moduleTable.getBooleanTopic("DriveConfigurationHealthy").publish();
      steerConfigurationHealthyPublisher =
          moduleTable.getBooleanTopic("SteerConfigurationHealthy").publish();
      encoderConfigurationHealthyPublisher =
          moduleTable.getBooleanTopic("EncoderConfigurationHealthy").publish();
    }

    private void publish(SwerveObservation.ModuleObservation module) {
      driveAppliedOutputPublisher.set(module.driveAppliedOutput());
      drivePositionRotationsPublisher.set(module.drivePositionRotations());
      driveVelocityRotationsPerSecondPublisher.set(module.driveVelocityRotationsPerSecond());
      steerAppliedOutputPublisher.set(module.steerAppliedOutput());
      steerPositionRotationsPublisher.set(module.steerPositionRotations());
      steerVelocityRotationsPerSecondPublisher.set(module.steerVelocityRotationsPerSecond());
      encoderAbsolutePositionRotationsPublisher.set(module.encoderAbsolutePositionRotations());
      driveConnectedPublisher.set(module.driveConnected());
      steerConnectedPublisher.set(module.steerConnected());
      encoderConnectedPublisher.set(module.encoderConnected());
      driveConfigurationHealthyPublisher.set(module.driveConfigurationHealthy());
      steerConfigurationHealthyPublisher.set(module.steerConfigurationHealthy());
      encoderConfigurationHealthyPublisher.set(module.encoderConfigurationHealthy());
    }

    @Override
    public void close() {
      driveAppliedOutputPublisher.close();
      drivePositionRotationsPublisher.close();
      driveVelocityRotationsPerSecondPublisher.close();
      steerAppliedOutputPublisher.close();
      steerPositionRotationsPublisher.close();
      steerVelocityRotationsPerSecondPublisher.close();
      encoderAbsolutePositionRotationsPublisher.close();
      driveConnectedPublisher.close();
      steerConnectedPublisher.close();
      encoderConnectedPublisher.close();
      driveConfigurationHealthyPublisher.close();
      steerConfigurationHealthyPublisher.close();
      encoderConfigurationHealthyPublisher.close();
    }
  }
}
