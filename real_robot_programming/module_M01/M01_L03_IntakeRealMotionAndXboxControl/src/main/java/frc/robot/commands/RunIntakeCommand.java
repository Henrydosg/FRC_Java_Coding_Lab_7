// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IntakeSubsystem.ManualDirection;
import java.util.Objects;

/**
 * Retains scheduler ownership of Intake while one held-button manual Intake request is active.
 *
 * <p>The request is made once at initialize; the subsystem owns admission, the approved voltage and
 * continuous safety checks. The command finishes as soon as the subsystem rejects or stops the
 * output, so it never re-requests output on its own: a new intentional button press is required.
 * Every end path stops the Intake through the non-throwing subsystem stop.
 */
public final class RunIntakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;
  private final ManualDirection direction;
  private boolean admitted;

  /**
   * Creates the manual forward Intake command.
   *
   * @param intakeSubsystem existing Intake behavior owner
   */
  public RunIntakeCommand(IntakeSubsystem intakeSubsystem) {
    this(intakeSubsystem, ManualDirection.FORWARD);
  }

  private RunIntakeCommand(IntakeSubsystem intakeSubsystem, ManualDirection direction) {
    this.intakeSubsystem = Objects.requireNonNull(intakeSubsystem, "intakeSubsystem");
    this.direction = Objects.requireNonNull(direction, "direction");
    addRequirements(intakeSubsystem);
  }

  /**
   * Creates the manual reverse Intake command.
   *
   * @param intakeSubsystem existing Intake behavior owner
   * @return manual reverse command
   */
  public static RunIntakeCommand reverse(IntakeSubsystem intakeSubsystem) {
    return new RunIntakeCommand(intakeSubsystem, ManualDirection.REVERSE);
  }

  @Override
  public void initialize() {
    admitted = intakeSubsystem.startManual(direction);
  }

  @Override
  public void execute() {
    // The subsystem already owns the requested state; no repeated request is made.
  }

  @Override
  public boolean isFinished() {
    return !admitted || !intakeSubsystem.isManualActive();
  }

  @Override
  public void end(boolean interrupted) {
    admitted = false;
    intakeSubsystem.stop();
  }
}
