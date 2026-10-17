// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.feeder;

import java.util.Objects;

/**
 * Immutable, vendor-neutral Feeder meaning for one coherent periodic snapshot.
 *
 * <p>The requested state describes software intent only. It does not assert physical transport or
 * a physically stopped mechanism.
 *
 * @param available whether the selected IO implementation can provide Feeder service
 * @param connected whether the available implementation reports a connected source
 * @param requestedState current software-requested Feeder state
 */
public record FeederObservation(
    boolean available, boolean connected, RequestedState requestedState) {
  /** Software intent owned by the Feeder subsystem. */
  public enum RequestedState {
    STOPPED,
    FEED_REQUESTED
  }

  /** Validates availability semantics and the required requested state. */
  public FeederObservation {
    requestedState = Objects.requireNonNull(requestedState, "requestedState");
    if (connected && !available) {
      throw new IllegalArgumentException("connected Feeder must also be available");
    }
  }
}
