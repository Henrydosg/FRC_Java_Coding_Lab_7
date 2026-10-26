// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

/** Deterministic safe Intake implementation that never produces physical output. */
public final class IntakeIONoop implements IntakeIO {
  /** Reports an unavailable Intake with explicitly invalid measurements every cycle. */
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.available = false;
    inputs.connected = false;
    inputs.measurementValid = false;
    inputs.timestampSeconds = Double.NaN;
    inputs.motorPositionRotations = Double.NaN;
    inputs.motorVelocityRotationsPerSecond = Double.NaN;
    inputs.statorCurrentAmps = Double.NaN;
    inputs.supplyCurrentAmps = Double.NaN;
    inputs.appliedVolts = Double.NaN;
  }

  @Override
  public void requestIntake() {}

  @Override
  public void stop() {}
}
