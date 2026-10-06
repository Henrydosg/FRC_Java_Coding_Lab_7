// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** Runs one explicit, scheduler-owned Disabled autonomous preparation attempt. */
public final class PrepareAutonomousCommand extends Command {
  private enum Phase {
    WAITING_FOR_REFRESH,
    COMPLETE_ON_NEXT_EXECUTE,
    FINISHED
  }

  private final AutonomousPreparationCoordinator coordinator;
  private final Supplier<AutonomousRoutineFactory.AutonomousRoutineId> routineSupplier;
  private final Supplier<Optional<Alliance>> allianceSupplier;
  private Phase phase = Phase.FINISHED;

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
    phase = Phase.FINISHED;
    AutonomousRoutineFactory.AutonomousRoutineId routine = null;
    Optional<Alliance> alliance = Optional.empty();
    try {
      routine = routineSupplier.get();
      alliance = Objects.requireNonNull(allianceSupplier.get(), "allianceSupplier result");
    } catch (RuntimeException ignored) {
      // Null/failed selection remains an explicit NOT_READY preparation result.
    }
    if (coordinator.beginPreparation(routine, alliance).state() == State.VALIDATING) {
      phase = Phase.WAITING_FOR_REFRESH;
    }
  }

  @Override
  public void execute() {
    if (phase == Phase.WAITING_FOR_REFRESH) {
      // Button polling can initialize us after this cycle's subsystem refresh. Consecutive
      // scheduler executes have a normal subsystem refresh between them, so skip the first.
      phase = Phase.COMPLETE_ON_NEXT_EXECUTE;
    } else if (phase == Phase.COMPLETE_ON_NEXT_EXECUTE) {
      phase = Phase.FINISHED;
      coordinator.completePreparation();
    }
  }

  @Override
  public void end(boolean interrupted) {
    phase = Phase.FINISHED;
  }

  @Override
  public boolean isFinished() {
    return phase == Phase.FINISHED;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
