// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.elevator;

/** Defines the vendor-neutral hardware contract for the Elevator mechanism. */
public interface ElevatorIO {
  /** Mutable one-cycle transport snapshot owned by the Elevator IO contract. */
  class ElevatorIOInputs {
    /** True when the selected IO implementation can provide Elevator service. */
    public boolean available;

    /** True when the available Elevator implementation reports a connected source. */
    public boolean connected;

    /** True when {@link #positionMeters} is a valid finite measurement. */
    public boolean positionValid;

    /** True when the measurement origin is trusted against the logical reference origin. */
    public boolean positionReferenced;

    /** Vendor-neutral linear Elevator position in the logical software coordinate frame. */
    public double positionMeters;
  }

  /** Updates the complete mutable transport snapshot for the current cycle. */
  void updateInputs(ElevatorIOInputs inputs);

  /** Requests a vendor-neutral Elevator position target in meters. */
  void requestPositionMeters(double targetPositionMeters);

  /** Requests the IO implementation's safe stopped output state. */
  void stop();
}
