// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.controls.IntakeInputProcessor;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.function.DoubleSupplier;

/**
 * Runs intake or outtake from the driver's trigger requests.
 */
public class ManualIntakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;
  private final IntakeInputProcessor intakeInputProcessor;
  private final DoubleSupplier intakeTrigger;
  private final DoubleSupplier outtakeTrigger;

  /**
   * Creates the manual intake command.
   *
   * @param intakeSubsystem intake behavior dependency
   * @param intakeInputProcessor trigger request processor
   * @param intakeTrigger supplies the right-trigger value
   * @param outtakeTrigger supplies the left-trigger value
   */
  public ManualIntakeCommand(
      IntakeSubsystem intakeSubsystem,
      IntakeInputProcessor intakeInputProcessor,
      DoubleSupplier intakeTrigger,
      DoubleSupplier outtakeTrigger) {
    this.intakeSubsystem = intakeSubsystem;
    this.intakeInputProcessor = intakeInputProcessor;
    this.intakeTrigger = intakeTrigger;
    this.outtakeTrigger = outtakeTrigger;
    addRequirements(intakeSubsystem);
  }

  @Override
  public void execute() {
    double output =
        intakeInputProcessor.process(
            intakeTrigger.getAsDouble(),
            outtakeTrigger.getAsDouble());

    intakeSubsystem.setOutput(output);
  }

  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
