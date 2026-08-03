// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.FlywheelConstants;

/**
 * Provides deterministic flywheel IO without physical hardware or a mechanism model.
 */
public class FlywheelIOSim implements FlywheelIO {
  private static final double kSimulatedVelocityRpm = 0.0;
  private static final double kSimulatedSupplyCurrentAmps = 0.0;
  private static final double kSimulatedStatorCurrentAmps = 0.0;
  private static final double kSimulatedTemperatureCelsius = 25.0;
  private static final boolean kSimulatedConnected = true;
  private static final boolean kSimulatedConfigurationHealthy = true;

  private double appliedOutput;

  /**
   * Updates the deterministic flywheel observation snapshot.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
    inputs.appliedOutput = appliedOutput;
    inputs.velocityRpm = kSimulatedVelocityRpm;
    inputs.supplyCurrentAmps =
        kSimulatedSupplyCurrentAmps;
    inputs.statorCurrentAmps =
        kSimulatedStatorCurrentAmps;
    inputs.temperatureCelsius =
        kSimulatedTemperatureCelsius;
    inputs.connected = kSimulatedConnected;
    inputs.configurationHealthy =
        kSimulatedConfigurationHealthy;
  }

  /**
   * Stores the requested normalized flywheel output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {
    appliedOutput =
        MathUtil.clamp(
            output,
            FlywheelConstants.kFlywheelPeakReverseDutyCycle,
            FlywheelConstants.kFlywheelPeakForwardDutyCycle);
  }

  /**
   * Stops the simulated flywheel motor.
   */
  @Override
  public void stop() {
    appliedOutput =
        FlywheelConstants.kStoppedFlywheelOutput;
  }
}
