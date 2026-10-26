// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.Objects;

/**
 * Coordinates Intake and Feeder semantic requests for one scheduler-owned lifecycle.
 *
 * <p>Initialization failures fail closed: both mechanisms are stopped, the Intake fault is
 * latched and the command finishes on its next scheduler check instead of throwing. Cleanup never
 * throws: the Intake stop path is non-throwing and a Feeder stop failure is reported once and does
 * not prevent the Intake stop attempt.
 */
public final class IntakeToFeederCommand extends Command {
  private final IntakeSubsystem intake;
  private final FeederSubsystem feeder;
  private boolean initializationFailed;
  private boolean feederStopFailureReported;

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
    initializationFailed = false;

    intake.requestIntake();
    if (intake.getObservation().faultLatched()) {
      // The Intake rejected or failed the request and has already attempted its own stop.
      failClosed();
      return;
    }

    try {
      feeder.requestFeed();
    } catch (RuntimeException failure) {
      DriverStation.reportError(
          "IntakeToFeederCommand Feeder request failed; outputs stopped: " + failure, false);
      failClosed();
      intake.latchFault();
    }
  }

  @Override
  public void execute() {}

  @Override
  public boolean isFinished() {
    return initializationFailed;
  }

  @Override
  public void end(boolean interrupted) {
    stopBoth();
  }

  private void failClosed() {
    initializationFailed = true;
    stopBoth();
  }

  private void stopBoth() {
    try {
      feeder.stop();
    } catch (RuntimeException failure) {
      if (!feederStopFailureReported) {
        feederStopFailureReported = true;
        DriverStation.reportError(
            "IntakeToFeederCommand Feeder stop failed; neutral output not confirmed: " + failure,
            false);
      }
    }
    intake.stop();
  }
}
