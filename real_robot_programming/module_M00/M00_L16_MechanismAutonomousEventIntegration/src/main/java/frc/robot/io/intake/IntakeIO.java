// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

/** Defines the vendor-neutral hardware contract for the Intake mechanism. */
public interface IntakeIO {
  /** Mutable one-cycle transport snapshot owned by the Intake IO contract. */
  class IntakeIOInputs {
    /** True when the selected IO implementation can provide Intake service. */
    public boolean available;

    /** True when the available Intake implementation reports a connected source. */
    public boolean connected;
  }

  /** Updates the complete mutable transport snapshot for the current cycle. */
  void updateInputs(IntakeIOInputs inputs);

  /** Requests the semantic Intake action without exposing vendor-specific output units. */
  void requestIntake();

  /** Requests the IO implementation's safe stopped output state. */
  void stop();
}
