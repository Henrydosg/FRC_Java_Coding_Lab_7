// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.Objects;

/** Coordinates Intake and Feeder semantic requests for one scheduler-owned lifecycle. */
public final class IntakeToFeederCommand extends Command {
  private final IntakeSubsystem intake;
  private final FeederSubsystem feeder;

  /**
   * Creates the command that requests Intake and Feeder behavior together.
   *
   * @param intake existing Intake behavior owner
   * @param feeder existing Feeder behavior owner
   */
  public IntakeToFeederCommand(IntakeSubsystem intake, FeederSubsystem feeder) {
    this.intake = Objects.requireNonNull(intake, "intake");
    this.feeder = Objects.requireNonNull(feeder, "feeder");
    addRequirements(this.intake, this.feeder);
  }

  @Override
  public void initialize() {
    try {
      intake.requestIntake();
    } catch (RuntimeException failure) {
      stopOutputsAfterFailure(failure);
      throw failure;
    }

    try {
      feeder.requestFeed();
    } catch (RuntimeException failure) {
      stopOutputsAfterFailure(failure);
      throw failure;
    }
  }

  @Override
  public void execute() {}

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    RuntimeException feederFailure = null;
    RuntimeException intakeFailure = null;

    try {
      feeder.stop();
    } catch (RuntimeException failure) {
      feederFailure = failure;
    }

    try {
      intake.stop();
    } catch (RuntimeException failure) {
      intakeFailure = failure;
    }

    if (feederFailure != null) {
      addSuppressedIfDistinct(feederFailure, intakeFailure);
      throw feederFailure;
    }
    if (intakeFailure != null) {
      throw intakeFailure;
    }
  }

  private void stopOutputsAfterFailure(RuntimeException primaryFailure) {
    try {
      feeder.stop();
    } catch (RuntimeException cleanupFailure) {
      addSuppressedIfDistinct(primaryFailure, cleanupFailure);
    }

    try {
      intake.stop();
    } catch (RuntimeException cleanupFailure) {
      addSuppressedIfDistinct(primaryFailure, cleanupFailure);
    }
  }

  private static void addSuppressedIfDistinct(
      RuntimeException primaryFailure, RuntimeException cleanupFailure) {
    if (cleanupFailure != null && cleanupFailure != primaryFailure) {
      primaryFailure.addSuppressed(cleanupFailure);
    }
  }
}
