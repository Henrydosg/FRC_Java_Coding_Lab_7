// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

/**
 * Provides a safe flywheel fallback without hardware output.
 */
public class FlywheelIONoop implements FlywheelIO {
  private static final double kStoppedOutput = 0.0;
  private static final double kStoppedVelocityRpm = 0.0;
  private static final double kStoppedSupplyCurrentAmps = 0.0;
  private static final double kStoppedStatorCurrentAmps = 0.0;
  private static final double kUnavailableTemperatureCelsius = 0.0;
  private static final boolean kConnected = false;
  private static final boolean kConfigurationHealthy = false;

  /**
   * Updates the flywheel snapshot with safe fallback values.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
    inputs.appliedOutput = kStoppedOutput;
    inputs.velocityRpm = kStoppedVelocityRpm;
    inputs.supplyCurrentAmps =
        kStoppedSupplyCurrentAmps;
    inputs.statorCurrentAmps =
        kStoppedStatorCurrentAmps;
    inputs.temperatureCelsius =
        kUnavailableTemperatureCelsius;
    inputs.connected = kConnected;
    inputs.configurationHealthy =
        kConfigurationHealthy;
  }

  /**
   * Ignores the requested output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {}

  /**
   * Keeps the fallback output stopped.
   */
  @Override
  public void stop() {}
}
