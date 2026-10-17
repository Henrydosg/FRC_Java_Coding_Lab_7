// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.feeder;

/** Defines the vendor-neutral hardware contract for the Feeder mechanism. */
public interface FeederIO {
  /** Mutable one-cycle transport snapshot owned by the Feeder IO contract. */
  class FeederIOInputs {
    /** True when the selected IO implementation can provide Feeder service. */
    public boolean available;

    /** True when the available Feeder implementation reports a connected source. */
    public boolean connected;
  }

  /** Updates the complete mutable transport snapshot for the current cycle. */
  void updateInputs(FeederIOInputs inputs);

  /** Requests the semantic Feeder action without exposing vendor-specific output units. */
  void requestFeed();

  /** Requests the IO implementation's safe stopped output state. */
  void stop();
}
