// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

import frc.robot.autonomous.AutonomousEventId;
import java.util.Objects;

/** Immutable, vendor-neutral read model of one autonomous event lifecycle. */
public record AutonomousEventObservation(
    AutonomousEventId eventId, LifecycleState state, boolean active) {
  /** Stable lifecycle states published by the event boundary. */
  public enum LifecycleState {
    STARTED,
    ACTIVE,
    COMPLETED,
    CANCELLED,
    FACTORY_FAILURE
  }

  public AutonomousEventObservation {
    eventId = Objects.requireNonNull(eventId, "eventId");
    state = Objects.requireNonNull(state, "state");
  }
}
