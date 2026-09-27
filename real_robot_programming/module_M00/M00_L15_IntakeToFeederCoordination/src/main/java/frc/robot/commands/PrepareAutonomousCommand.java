// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** Runs one explicit, scheduler-owned Disabled autonomous preparation attempt. */
public final class PrepareAutonomousCommand extends Command {
  private final AutonomousPreparationCoordinator coordinator;
  private final Supplier<AutonomousRoutineFactory.AutonomousRoutineId> routineSupplier;
  private final Supplier<Optional<Alliance>> allianceSupplier;
  private boolean finished;

  /** Creates the single production autonomous preparation action. */
  public PrepareAutonomousCommand(
      SwerveSubsystem swerveSubsystem,
      AutonomousPreparationCoordinator coordinator,
      Supplier<AutonomousRoutineFactory.AutonomousRoutineId> routineSupplier,
      Supplier<Optional<Alliance>> allianceSupplier) {
    this.coordinator = Objects.requireNonNull(coordinator, "coordinator");
    this.routineSupplier = Objects.requireNonNull(routineSupplier, "routineSupplier");
    this.allianceSupplier = Objects.requireNonNull(allianceSupplier, "allianceSupplier");
    addRequirements(Objects.requireNonNull(swerveSubsystem, "swerveSubsystem"));
  }

  @Override
  public void initialize() {
    finished = false;
    AutonomousRoutineFactory.AutonomousRoutineId routine = null;
    Optional<Alliance> alliance = Optional.empty();
    try {
      routine = routineSupplier.get();
      alliance = Objects.requireNonNull(allianceSupplier.get(), "allianceSupplier result");
    } catch (RuntimeException ignored) {
      // Null/failed selection remains an explicit NOT_READY preparation result.
    }
    finished =
        coordinator.beginPreparation(routine, alliance).state()
            != State.VALIDATING;
  }

  @Override
  public void execute() {
    if (!finished) {
      coordinator.completePreparation();
      finished = true;
    }
  }

  @Override
  public boolean isFinished() {
    return finished;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
