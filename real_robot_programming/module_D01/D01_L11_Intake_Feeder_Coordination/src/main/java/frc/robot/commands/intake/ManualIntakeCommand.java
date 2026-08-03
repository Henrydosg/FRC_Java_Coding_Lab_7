// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.FeederConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.controls.IntakeInputProcessor;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.function.DoubleSupplier;

/**
 * Coordinates the intake and feeder from the driver's trigger requests.
 */
public class ManualIntakeCommand extends Command {
  private final IntakeSubsystem intakeSubsystem;
  private final FeederSubsystem feederSubsystem;
  private final IntakeInputProcessor intakeInputProcessor;
  private final DoubleSupplier intakeTrigger;
  private final DoubleSupplier outtakeTrigger;
  private final Timer feederDelayTimer = new Timer();
  private int lastDirection;

  /**
   * Creates the manual intake command.
   *
   * @param intakeSubsystem intake behavior dependency
   * @param feederSubsystem feeder behavior dependency
   * @param intakeInputProcessor trigger request processor
   * @param intakeTrigger supplies the right-trigger value
   * @param outtakeTrigger supplies the left-trigger value
   */
  public ManualIntakeCommand(
      IntakeSubsystem intakeSubsystem,
      FeederSubsystem feederSubsystem,
      IntakeInputProcessor intakeInputProcessor,
      DoubleSupplier intakeTrigger,
      DoubleSupplier outtakeTrigger) {
    this.intakeSubsystem = intakeSubsystem;
    this.feederSubsystem = feederSubsystem;
    this.intakeInputProcessor = intakeInputProcessor;
    this.intakeTrigger = intakeTrigger;
    this.outtakeTrigger = outtakeTrigger;
    addRequirements(
        intakeSubsystem,
        feederSubsystem);
  }

  @Override
  public void initialize() {
    feederDelayTimer.reset();
    lastDirection = 0;
  }

  @Override
  public void execute() {
    double intakeOutput =
        intakeInputProcessor.process(
            intakeTrigger.getAsDouble(),
            outtakeTrigger.getAsDouble());

    if (intakeOutput > IntakeConstants.kStoppedIntakeOutput) {
      intakeSubsystem.setOutput(intakeOutput);
      applyDelayedFeederOutput(1, FeederConstants.kFeederAcquireOutput);
    } else if (
        intakeOutput
            < IntakeConstants.kStoppedIntakeOutput) {
      intakeSubsystem.setOutput(intakeOutput);
      applyDelayedFeederOutput(-1, FeederConstants.kFeederOuttakeOutput);
    } else {
      intakeSubsystem.stop();
      feederSubsystem.stop();
      feederDelayTimer.reset();
      lastDirection = 0;
    }
  }

  private void applyDelayedFeederOutput(int direction, double feederOutput) {
    if (direction != lastDirection) {
      feederDelayTimer.restart();
      lastDirection = direction;
    }

    if (feederDelayTimer.hasElapsed(
        IntakeConstants.kIntakeFeederDelaySeconds)) {
      feederSubsystem.setOutput(feederOutput);
    } else {
      feederSubsystem.stop();
    }
  }

  @Override
  public void end(boolean interrupted) {
    intakeSubsystem.stop();
    feederSubsystem.stop();
    feederDelayTimer.stop();
    feederDelayTimer.reset();
    lastDirection = 0;
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
