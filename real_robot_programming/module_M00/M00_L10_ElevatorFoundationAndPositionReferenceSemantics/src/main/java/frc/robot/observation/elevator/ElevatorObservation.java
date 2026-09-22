// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.elevator;

/** Immutable, vendor-neutral Elevator meaning for one coherent periodic snapshot. */
public record ElevatorObservation(
    boolean available,
    boolean connected,
    boolean positionValid,
    boolean positionReferenced,
    double positionMeters) {
  /** Validates the dependency ordering of availability, validity, and reference trust. */
  public ElevatorObservation {
    if (connected && !available) {
      throw new IllegalArgumentException("connected Elevator must also be available");
    }
    if (positionValid && (!available || !connected)) {
      throw new IllegalArgumentException(
          "valid Elevator position requires availability and connection");
    }
    if (!Double.isFinite(positionMeters)) {
      throw new IllegalArgumentException("Elevator position must be finite");
    }
    if (positionReferenced && (!available || !connected || !positionValid)) {
      throw new IllegalArgumentException(
          "referenced Elevator position requires valid connected input");
    }
  }
}
