// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.IntakeConstants;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.Objects;
import java.util.function.DoubleSupplier;

/**
 * Runs one bounded Test-mode Intake commissioning voltage output.
 *
 * <p>This command is an explicit commissioning interface started only by a User dashboard action.
 * It is never bound to a controller, scheduled by autonomous code or restarted automatically.
 * Admission and continuous safety checks belong to {@link IntakeSubsystem}; this command bounds
 * the active output to {@link IntakeConstants#kCommissioningMaximumDurationSeconds} and always
 * stops on finish, timeout, interruption, Disable or mode change. The time bound is enforced by
 * scheduler execution, not by hardware.
 */
public final class IntakeCommissioningCommand extends Command {
  private final IntakeSubsystem intake;
  private final double volts;
  private final DoubleSupplier clockSeconds;

  private boolean admitted;
  private double startTimeSeconds = Double.NaN;

  private IntakeCommissioningCommand(
      IntakeSubsystem intake, double volts, DoubleSupplier clockSeconds) {
    this.intake = Objects.requireNonNull(intake, "intake");
    this.volts = volts;
    this.clockSeconds = Objects.requireNonNull(clockSeconds, "clockSeconds");
    addRequirements(intake);
  }

  /**
   * Creates the forward commissioning command at the approved positive voltage.
   *
   * @param intake Intake behavior owner
   * @return bounded commissioning command
   */
  public static IntakeCommissioningCommand forward(IntakeSubsystem intake) {
    return new IntakeCommissioningCommand(
        intake, IntakeConstants.kCommissioningVoltageVolts, Timer::getFPGATimestamp);
  }

  /**
   * Creates the reverse commissioning command at the approved negative voltage.
   *
   * @param intake Intake behavior owner
   * @return bounded commissioning command
   */
  public static IntakeCommissioningCommand reverse(IntakeSubsystem intake) {
    return new IntakeCommissioningCommand(
        intake, -IntakeConstants.kCommissioningVoltageVolts, Timer::getFPGATimestamp);
  }

  static IntakeCommissioningCommand withClock(
      IntakeSubsystem intake, double volts, DoubleSupplier clockSeconds) {
    return new IntakeCommissioningCommand(intake, volts, clockSeconds);
  }

  @Override
  public void initialize() {
    startTimeSeconds = clockSeconds.getAsDouble();
    admitted = intake.startCommissioningVoltage(volts);
  }

  @Override
  public boolean isFinished() {
    double elapsedSeconds = clockSeconds.getAsDouble() - startTimeSeconds;
    // The negated comparison also finishes when the elapsed time is NaN.
    return !admitted
        || !intake.isCommissioningActive()
        || !(elapsedSeconds < IntakeConstants.kCommissioningMaximumDurationSeconds);
  }

  /** Stops the Intake; the subsystem stop path never throws and reports its own failures. */
  @Override
  public void end(boolean interrupted) {
    admitted = false;
    intake.stop();
  }
}
