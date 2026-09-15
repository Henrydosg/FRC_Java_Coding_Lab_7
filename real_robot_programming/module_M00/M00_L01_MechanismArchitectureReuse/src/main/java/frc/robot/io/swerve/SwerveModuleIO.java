// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import edu.wpi.first.math.geometry.Rotation2d;

/**
 * Defines raw hardware operations for one Swerve module.
 */
public interface SwerveModuleIO {
  /** Reasons that a manual static-friction characterization pulse ended. */
  enum StaticFrictionStopReason {
    TIMEOUT,
    DISABLE,
    MODE_EXIT,
    INTERRUPTED,
    EXCEPTION,
    REJECTED
  }

  /**
   * Stores one periodic cycle of raw module hardware observations.
   */
  class SwerveModuleIOInputs {
    public double driveAppliedOutput;
    public double drivePositionRotations;
    public double driveVelocityRotationsPerSecond;
    public double driveSupplyVoltageVolts;
    public double driveSupplyCurrentAmps;
    public double driveStatorCurrentAmps;
    public double driveTemperatureCelsius;

    public double steerAppliedOutput;
    public double steerPositionRotations;
    public double steerVelocityRotationsPerSecond;
    public double steerSupplyVoltageVolts;
    public double steerSupplyCurrentAmps;
    public double steerStatorCurrentAmps;
    public double steerTemperatureCelsius;

    public double encoderAbsolutePositionRotations;
    public double encoderVelocityRotationsPerSecond;

    public boolean driveConnected;
    public boolean steerConnected;
    public boolean encoderConnected;
    public boolean driveConfigurationHealthy;
    public boolean steerConfigurationHealthy;
    public boolean encoderConfigurationHealthy;
  }

  /**
   * Updates the raw module hardware snapshot.
   *
   * @param inputs snapshot to update
   */
  void updateInputs(SwerveModuleIOInputs inputs);

  /**
   * Sets normalized open-loop drive output.
   *
   * @param output normalized output from -1.0 to 1.0
   */
  void setDriveOutput(double output);

  /**
   * Sets normalized open-loop steer output.
   *
   * @param output normalized output from -1.0 to 1.0
   */
  void setSteerOutput(double output);

  /**
   * Sets the drive wheel velocity setpoint.
   *
   * @param velocityMetersPerSecond requested wheel velocity in meters per second
   */
  void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond);

  /**
   * Applies a fixed drive voltage for manual static-friction characterization.
   *
   * <p>This is a vendor-neutral physical-voltage request. The subsystem owns Test-mode, timing,
   * and Front Left-only commissioning safety.
   *
   * @param voltageVolts requested drive voltage in volts
   */
  default boolean setDriveStaticFrictionCharacterizationVoltageVolts(double voltageVolts) {
    return false;
  }

  /** Finalizes one static-friction characterization pulse and returns its hardware to stop. */
  default void finishDriveStaticFrictionCharacterization(
      double requestedVoltageVolts, StaticFrictionStopReason stopReason) {
    stop();
  }

  /**
   * Sets the calibrated module steer-angle setpoint.
   *
   * @param angle calibrated module angle
   */
  void setSteerAngle(Rotation2d angle);

  /**
   * Stops both module motors.
   */
  void stop();
}
