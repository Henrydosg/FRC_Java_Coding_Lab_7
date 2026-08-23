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
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.function.Consumer;

/** Performs narrow, fail-closed registration of one typed event binding. */
public final class AutonomousEventRegistration {
  private AutonomousEventRegistration() {}

  /** Registers one event before its PathPlanner asset is loaded or executed. */
  public static synchronized void register(
      AutonomousEventBinding binding, Consumer<AutonomousEventObservation> observationSink) {
    AutonomousEventBinding acceptedBinding = Objects.requireNonNull(binding, "binding");
    Consumer<AutonomousEventObservation> acceptedSink =
        Objects.requireNonNull(observationSink, "observationSink");
    String eventName = acceptedBinding.eventName();

    if (NamedCommands.hasCommand(eventName)) {
      throw new IllegalStateException("duplicate NamedCommands name: " + eventName);
    }

    Command deferredCommand =
        Commands.defer(
            () -> resolve(acceptedBinding, acceptedSink), acceptedBinding.requirements());
    NamedCommands.registerCommand(eventName, deferredCommand);
  }

  private static Command resolve(
      AutonomousEventBinding binding, Consumer<AutonomousEventObservation> observationSink) {
    final Command command;
    try {
      command = binding.commandSupplier().get();
    } catch (RuntimeException failure) {
      publishFactoryFailure(binding.eventId(), observationSink);
      return Commands.none();
    }
    if (command == null
        || !command.getRequirements().equals(binding.requirements())
        || command.getRequirements().stream().anyMatch(SwerveSubsystem.class::isInstance)) {
      publishFactoryFailure(binding.eventId(), observationSink);
      return Commands.none();
    }
    return command;
  }

  private static void publishFactoryFailure(
      AutonomousEventId eventId, Consumer<AutonomousEventObservation> observationSink) {
    try {
      observationSink.accept(new AutonomousEventObservation(eventId, LifecycleState.FACTORY_FAILURE, false));
    } catch (RuntimeException ignored) {
      // A telemetry failure cannot safely change the autonomous command contract.
    }
  }
}
