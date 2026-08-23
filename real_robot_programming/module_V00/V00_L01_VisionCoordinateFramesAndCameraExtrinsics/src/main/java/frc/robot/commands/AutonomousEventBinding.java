// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/** Immutable typed contract for one PathPlanner NamedCommands binding. */
public record AutonomousEventBinding(
    AutonomousEventId eventId, Supplier<Command> commandSupplier, Set<Subsystem> requirements) {
  /** Validates and defensively snapshots one event binding. */
  public AutonomousEventBinding {
    Objects.requireNonNull(eventId, "eventId");
    Objects.requireNonNull(commandSupplier, "commandSupplier");
    Objects.requireNonNull(requirements, "requirements");

    LinkedHashSet<Subsystem> copiedRequirements = new LinkedHashSet<>();
    for (Subsystem requirement : requirements) {
      if (requirement == null) {
        throw new IllegalArgumentException("requirements cannot contain null");
      }
      if (requirement instanceof SwerveSubsystem) {
        throw new IllegalArgumentException("autonomous events cannot require SwerveSubsystem");
      }
      copiedRequirements.add(requirement);
    }
    requirements = Set.copyOf(copiedRequirements);
  }

  /** Returns the authoritative NamedCommands string derived from the event ID. */
  public String eventName() {
    return eventId.stableName();
  }
}
