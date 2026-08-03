// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

/**
 * Immutable, vendor-neutral Swerve state from one completed periodic refresh.
 */
public record SwerveObservation(
    ModuleObservation frontLeft,
    ModuleObservation frontRight,
    ModuleObservation backLeft,
    ModuleObservation backRight,
    GyroObservation gyro) {

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
