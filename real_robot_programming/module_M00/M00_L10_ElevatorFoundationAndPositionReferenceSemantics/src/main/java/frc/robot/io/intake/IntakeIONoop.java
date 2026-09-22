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
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.available = false;
    inputs.connected = false;
  }

  @Override
  public void requestIntake() {}

  @Override
  public void stop() {}
}
