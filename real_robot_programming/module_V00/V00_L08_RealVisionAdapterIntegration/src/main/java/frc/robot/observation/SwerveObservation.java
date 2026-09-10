// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

import java.util.Objects;
import java.util.Optional;

/**
 * Immutable, vendor-neutral Swerve state from one completed periodic refresh.
 */
public record SwerveObservation(
    ModuleObservation frontLeft,
    ModuleObservation frontRight,
    ModuleObservation backLeft,
    ModuleObservation backRight,
    GyroObservation gyro,
    Optional<PoseObservation> currentPose,
    Optional<EstimatedPoseObservation> estimatedPose) {

  /**
   * Preserves the pre-estimator construction contract with no estimated pose available.
   *
   * @param frontLeft front-left module observation
   * @param frontRight front-right module observation
   * @param backLeft back-left module observation
   * @param backRight back-right module observation
   * @param gyro gyro observation
   * @param currentPose current L23 odometry pose observation
   */
  public SwerveObservation(
      ModuleObservation frontLeft,
      ModuleObservation frontRight,
      ModuleObservation backLeft,
      ModuleObservation backRight,
      GyroObservation gyro,
      Optional<PoseObservation> currentPose) {
    this(frontLeft, frontRight, backLeft, backRight, gyro, currentPose, Optional.empty());
  }

  /** Validates required immutable components without retaining nullable aliases. */
  public SwerveObservation {
    Objects.requireNonNull(frontLeft, "frontLeft");
    Objects.requireNonNull(frontRight, "frontRight");
    Objects.requireNonNull(backLeft, "backLeft");
    Objects.requireNonNull(backRight, "backRight");
    Objects.requireNonNull(gyro, "gyro");
    currentPose = Objects.requireNonNull(currentPose, "currentPose");
    estimatedPose = Objects.requireNonNull(estimatedPose, "estimatedPose");
  }

  /**
   * Immutable current odometry pose in field coordinates.
   *
   * @param xMeters field X position in meters
   * @param yMeters field Y position in meters
   * @param headingRadians field heading in radians, positive counterclockwise
   * @param measurementSampleValid true when the current periodic measurement sample was accepted;
   *     false when this pose is the last valid value held across an invalid sample
   */
  public record PoseObservation(
      double xMeters,
      double yMeters,
      double headingRadians,
      boolean measurementSampleValid) {

    /** Rejects nonfinite pose values so invalid numbers cannot enter the observation boundary. */
    public PoseObservation {
      if (!Double.isFinite(xMeters)
          || !Double.isFinite(yMeters)
          || !Double.isFinite(headingRadians)) {
        throw new IllegalArgumentException("Pose values must be finite");
      }
    }
  }

  /**
   * Immutable estimated pose in field coordinates.
   *
   * <p>This is additive to {@link PoseObservation}; {@code currentPose} retains its existing L23
   * odometry meaning.
   *
   * @param xMeters estimated field X position in meters
   * @param yMeters estimated field Y position in meters
   * @param headingRadians estimated field heading in radians, positive counterclockwise
   * @param measurementSampleValid true when the current periodic measurement sample was accepted;
   *     false when this estimate is the last valid value held across an invalid sample
   */
  public record EstimatedPoseObservation(
      double xMeters,
      double yMeters,
      double headingRadians,
      boolean measurementSampleValid) {

    /** Rejects nonfinite estimated-pose values at the immutable observation boundary. */
    public EstimatedPoseObservation {
      if (!Double.isFinite(xMeters)
          || !Double.isFinite(yMeters)
          || !Double.isFinite(headingRadians)) {
        throw new IllegalArgumentException("Estimated pose values must be finite");
      }
    }
  }

  /** Immutable scalar state for one Swerve module. */
  public record ModuleObservation(
      double driveAppliedOutput,
      double drivePositionRotations,
      double driveVelocityRotationsPerSecond,
      double driveSupplyVoltageVolts,
      double driveSupplyCurrentAmps,
      double driveStatorCurrentAmps,
      double driveTemperatureCelsius,
      double steerAppliedOutput,
      double steerPositionRotations,
      double steerVelocityRotationsPerSecond,
      double steerSupplyVoltageVolts,
      double steerSupplyCurrentAmps,
      double steerStatorCurrentAmps,
      double steerTemperatureCelsius,
      double encoderAbsolutePositionRotations,
      double encoderVelocityRotationsPerSecond,
      boolean driveConnected,
      boolean steerConnected,
      boolean encoderConnected,
      boolean driveConfigurationHealthy,
      boolean steerConfigurationHealthy,
      boolean encoderConfigurationHealthy) {}

  /** Immutable scalar state for the robot gyro. */
  public record GyroObservation(
      double yawDegrees,
      double pitchDegrees,
      double rollDegrees,
      double angularVelocityXDegreesPerSecond,
      double angularVelocityYDegreesPerSecond,
      double angularVelocityZDegreesPerSecond,
      boolean connected,
      boolean configurationHealthy) {}
}
