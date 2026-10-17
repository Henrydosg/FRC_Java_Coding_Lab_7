// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.intake;

import java.util.Objects;

/**
 * Immutable, vendor-neutral Intake meaning for one coherent periodic snapshot.
 *
 * <p>The requested state describes software intent only. It does not assert physical motion or a
 * physically stopped mechanism.
 *
 * @param available whether the selected IO implementation can provide Intake service
 * @param connected whether the available implementation reports a connected source
 * @param requestedState current software-requested Intake state
 */
public record IntakeObservation(
    boolean available, boolean connected, RequestedState requestedState) {
  /** Software intent owned by the Intake subsystem. */
  public enum RequestedState {
    STOPPED,
    INTAKE_REQUESTED
  }

  /** Validates availability semantics and the required requested state. */
  public IntakeObservation {
    requestedState = Objects.requireNonNull(requestedState, "requestedState");
    if (connected && !available) {
      throw new IllegalArgumentException("connected Intake must also be available");
    }
  }
}
