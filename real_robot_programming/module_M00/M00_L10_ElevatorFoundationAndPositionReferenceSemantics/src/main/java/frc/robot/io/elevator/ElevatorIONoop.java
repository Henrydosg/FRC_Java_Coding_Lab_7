// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.elevator;

/** Deterministic safe Elevator implementation that never produces physical output. */
public final class ElevatorIONoop implements ElevatorIO {
  @Override
  public void updateInputs(ElevatorIOInputs inputs) {
    inputs.available = false;
    inputs.connected = false;
    inputs.positionValid = false;
    inputs.positionReferenced = false;
    inputs.positionMeters = 0.0;
  }

  @Override
  public void stop() {}
}
