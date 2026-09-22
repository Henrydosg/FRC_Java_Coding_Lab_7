// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.Objects;

/** Retains scheduler ownership of Intake while one manual Intake request is active. */
public final class RunIntakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;

  /**
   * Creates the manual Intake ownership command.
   *
   * @param intakeSubsystem existing Intake behavior owner
   */
  public RunIntakeCommand(IntakeSubsystem intakeSubsystem) {
    this.intakeSubsystem = Objects.requireNonNull(intakeSubsystem, "intakeSubsystem");
    addRequirements(intakeSubsystem);
  }

  @Override
  public void initialize() {
    intakeSubsystem.requestIntake();
  }

  @Override
  public void execute() {
    // The subsystem already owns the requested state; no repeated request is required.
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stop();
  }
}
