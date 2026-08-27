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
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/** Immutable registration contract for one scheduler-owned autonomous event. */
public record AutonomousEventBinding(
    AutonomousEventId eventId, Supplier<Command> commandSupplier, Set<Subsystem> requirements) {
  public AutonomousEventBinding {
    eventId = Objects.requireNonNull(eventId, "eventId");
    commandSupplier = Objects.requireNonNull(commandSupplier, "commandSupplier");
    requirements = Objects.requireNonNull(requirements, "requirements");

    Set<Subsystem> copiedRequirements = new HashSet<>();
    for (Subsystem requirement : requirements) {
      if (requirement == null) {
        throw new NullPointerException("requirements cannot contain null");
      }
      if (requirement instanceof SwerveSubsystem) {
        throw new IllegalArgumentException("autonomous events cannot require SwerveSubsystem");
      }
      copiedRequirements.add(requirement);
    }
    requirements = Set.copyOf(copiedRequirements);
  }

  /** Returns the exact stable name expected in the PathPlanner asset. */
  public String pathPlannerName() {
    return eventId.pathPlannerName();
  }
}
