// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.IntakeConstants;

/**
 * Provides deterministic intake IO without physical hardware or a mechanism model.
 */
public class IntakeIOSim implements IntakeIO {
  private static final boolean kSimulatedConnected = true;
  private static final double kStoppedOutput = 0.0;
  private static final double kInitialPositionRotations = 0.0;
  private static final double kInitialVelocityRpm = 0.0;

  private double appliedOutput;

  /**
   * Updates the deterministic intake observation snapshot.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.appliedOutput = appliedOutput;
    inputs.positionRotations =
        kInitialPositionRotations;
    inputs.velocityRpm =
        kInitialVelocityRpm;
    inputs.connected = kSimulatedConnected;
  }

  /**
   * Stores the requested normalized intake output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {
    appliedOutput =
        MathUtil.clamp(
            output,
            IntakeConstants.kMinimumIntakeOutput,
            IntakeConstants.kMaximumIntakeOutput);
  }

  /**
   * Stops the simulated intake motor.
   */
  @Override
  public void stop() {
    appliedOutput = kStoppedOutput;
  }
}
