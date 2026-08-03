// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

/**
 * Deterministic no-op Swerve module IO for simulation without hardware behavior.
 */
public final class SwerveModuleIONoop implements SwerveModuleIO {
  @Override
  public void updateInputs(SwerveModuleIOInputs inputs) {
    inputs.driveAppliedOutput = 0.0;
    inputs.drivePositionRotations = 0.0;
    inputs.driveVelocityRotationsPerSecond = 0.0;
    inputs.driveSupplyVoltageVolts = 0.0;
    inputs.driveSupplyCurrentAmps = 0.0;
    inputs.driveStatorCurrentAmps = 0.0;
    inputs.driveTemperatureCelsius = 0.0;
    inputs.steerAppliedOutput = 0.0;
    inputs.steerPositionRotations = 0.0;
    inputs.steerVelocityRotationsPerSecond = 0.0;
    inputs.steerSupplyVoltageVolts = 0.0;
    inputs.steerSupplyCurrentAmps = 0.0;
    inputs.steerStatorCurrentAmps = 0.0;
    inputs.steerTemperatureCelsius = 0.0;
    inputs.encoderAbsolutePositionRotations = 0.0;
    inputs.encoderVelocityRotationsPerSecond = 0.0;
    inputs.driveConnected = false;
    inputs.steerConnected = false;
    inputs.encoderConnected = false;
    inputs.driveConfigurationHealthy = false;
    inputs.steerConfigurationHealthy = false;
    inputs.encoderConfigurationHealthy = false;
  }

  @Override
  public void setDriveOutput(double output) {}

  @Override
  public void setSteerOutput(double output) {}

  @Override
  public void stop() {}
}
