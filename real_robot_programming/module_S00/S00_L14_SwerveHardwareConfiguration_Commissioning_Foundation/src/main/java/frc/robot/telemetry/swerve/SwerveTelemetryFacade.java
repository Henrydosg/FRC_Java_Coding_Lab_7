// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.swerve;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import frc.robot.observation.SwerveObservation;
import java.util.Objects;

/**
 * Publishes selected immutable Swerve observations without controlling robot behavior.
 */
public final class SwerveTelemetryFacade implements AutoCloseable {
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

  /**
   * Creates typed publishers under the Swerve telemetry table.
   *
   * @param swerveTable root Swerve telemetry table
   */
  public SwerveTelemetryFacade(NetworkTable swerveTable) {
    Objects.requireNonNull(swerveTable, "swerveTable");

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
