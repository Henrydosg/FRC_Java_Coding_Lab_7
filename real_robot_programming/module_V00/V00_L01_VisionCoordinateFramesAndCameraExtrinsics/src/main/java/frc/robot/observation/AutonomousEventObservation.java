// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

import frc.robot.commands.AutonomousEventId;
import java.util.Objects;

/** Immutable, vendor-neutral lifecycle observation for one autonomous event. */
public record AutonomousEventObservation(
    AutonomousEventId eventId, LifecycleState state, boolean active) {
  /** Validates one complete event observation. */
  public AutonomousEventObservation {
    Objects.requireNonNull(eventId, "eventId");
    Objects.requireNonNull(state, "state");
  }

  /** Lifecycle states exposed to telemetry and deterministic tests. */
  public enum LifecycleState {
    STARTED,
    ACTIVE,
    COMPLETED,
    CANCELLED,
    FACTORY_FAILURE
  }
}
