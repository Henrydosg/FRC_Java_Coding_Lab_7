// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.feeder;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.FeederConstants;

/**
 * Provides deterministic feeder IO without physical hardware or a mechanism model.
 */
public class FeederIOSim implements FeederIO {
  private static final double kSimulatedPositionRotations = 0.0;
  private static final double kSimulatedVelocityRpm = 0.0;
  private static final double kSimulatedSupplyCurrentAmps = 0.0;
  private static final double kStatorCurrentNotApplicableAmps = 0.0;
  private static final double kSimulatedTemperatureCelsius = 25.0;
  private static final boolean kSimulatedConnected = true;
  private static final boolean kSimulatedConfigurationHealthy = true;

  private double appliedOutput;

  /**
   * Updates the deterministic feeder observation snapshot.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(FeederIOInputs inputs) {
    inputs.appliedOutput = appliedOutput;
    inputs.positionRotations =
        kSimulatedPositionRotations;
    inputs.velocityRpm = kSimulatedVelocityRpm;
    inputs.supplyCurrentAmps =
        kSimulatedSupplyCurrentAmps;
    inputs.statorCurrentAmps =
        kStatorCurrentNotApplicableAmps;
    inputs.temperatureCelsius =
        kSimulatedTemperatureCelsius;
    inputs.connected = kSimulatedConnected;
    inputs.configurationHealthy =
        kSimulatedConfigurationHealthy;
  }

  /**
   * Stores the bounded normalized feeder output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {
    appliedOutput =
        MathUtil.clamp(
            output,
            FeederConstants.kFeederPeakReverseOutput,
            FeederConstants.kFeederPeakForwardOutput);
  }

  /**
   * Stops the simulated feeder motor.
   */
  @Override
  public void stop() {
    appliedOutput =
        FeederConstants.kStoppedFeederOutput;
  }
}
