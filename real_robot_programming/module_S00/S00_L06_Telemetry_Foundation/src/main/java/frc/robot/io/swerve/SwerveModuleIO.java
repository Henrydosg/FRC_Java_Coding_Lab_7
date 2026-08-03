// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

/**
 * Defines raw hardware operations for one Swerve module.
 */
public interface SwerveModuleIO {
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
   * Stops both module motors.
   */
  void stop();
}
