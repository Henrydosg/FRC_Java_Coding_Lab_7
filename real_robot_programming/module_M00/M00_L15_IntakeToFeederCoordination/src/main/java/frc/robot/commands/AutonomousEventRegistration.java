// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.Objects;
import java.util.function.Consumer;

/** Registers fresh scheduler-owned event commands with PathPlanner NamedCommands. */
public final class AutonomousEventRegistration {
  private final Consumer<AutonomousEventObservation> observationSink;

  public AutonomousEventRegistration(Consumer<AutonomousEventObservation> observationSink) {
    this.observationSink = Objects.requireNonNull(observationSink, "observationSink");
  }

  /** Registers one event once; duplicate names are rejected before registry mutation. */
  public void register(AutonomousEventBinding binding) {
    AutonomousEventBinding acceptedBinding =
        Objects.requireNonNull(binding, "binding");
    if (NamedCommands.hasCommand(acceptedBinding.pathPlannerName())) {
      throw new IllegalStateException(
          "NamedCommands event is already registered: " + acceptedBinding.pathPlannerName());
    }

    Command deferredCommand =
        Commands.defer(
            () -> createFreshCommand(acceptedBinding), acceptedBinding.requirements());
    NamedCommands.registerCommand(acceptedBinding.pathPlannerName(), deferredCommand);
  }

  private Command createFreshCommand(AutonomousEventBinding binding) {
    try {
      Command command =
          Objects.requireNonNull(
              binding.commandSupplier().get(), "event command supplier returned null");
      if (!command.getRequirements().equals(binding.requirements())) {
        throw new IllegalStateException("event command requirements did not match its binding");
      }
      return command;
    } catch (RuntimeException failure) {
      observationSink.accept(
          new AutonomousEventObservation(
              binding.eventId(), LifecycleState.FACTORY_FAILURE, false));
      return Commands.none();
    }
  }
}
