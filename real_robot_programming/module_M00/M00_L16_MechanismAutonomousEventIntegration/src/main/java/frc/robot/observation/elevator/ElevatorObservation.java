// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.elevator;

import java.util.Objects;

/** Immutable, vendor-neutral Elevator meaning for one coherent periodic snapshot. */
public record ElevatorObservation(
    boolean available,
    boolean connected,
    boolean positionValid,
    boolean positionReferenced,
    double positionMeters,
    ElevatorRequestedState requestedState,
    double targetPositionMeters,
    double positionErrorMeters) {
  /** Validates the dependency ordering of availability, validity, and reference trust. */
  public ElevatorObservation {
    requestedState = Objects.requireNonNull(requestedState, "requestedState");
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
    if (!Double.isFinite(targetPositionMeters)) {
      throw new IllegalArgumentException("Elevator target position must be finite");
    }
    if (requestedState == ElevatorRequestedState.STOPPED
        && Double.doubleToLongBits(targetPositionMeters)
            != Double.doubleToLongBits(0.0)) {
      throw new IllegalArgumentException("stopped Elevator target must be canonical 0.0");
    }
    double expectedError =
        requestedState == ElevatorRequestedState.POSITION_REQUESTED
                && positionValid
                && positionReferenced
            ? targetPositionMeters - positionMeters
            : 0.0;
    if (Double.doubleToLongBits(positionErrorMeters)
        != Double.doubleToLongBits(expectedError)) {
      throw new IllegalArgumentException("Elevator position error does not match observation");
    }
  }
}
