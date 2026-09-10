// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/**
 * Runs one bounded, Front Left-only open-loop commissioning pulse.
 *
 * <p>This command is an explicit commissioning/test interface. It is never scheduled
 * automatically by the robot lifecycle or RobotContainer.
 */
public final class SwerveFrontLeftOpenLoopCommissioningCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveSubsystem.FrontLeftCommissioningAction action;
  private final Timer pulseTimer = new Timer();
  private boolean initialized;
  private boolean outputStopped;
  private boolean timedOut;
  private boolean rejected;

  /**
   * Creates a bounded Front Left commissioning pulse.
   *
   * @param swerveSubsystem swerve subsystem that owns Front Left output delegation
   * @param action explicit drive or steer direction
   * @throws NullPointerException when a required reference is null
   */
  private SwerveFrontLeftOpenLoopCommissioningCommand(
      SwerveSubsystem swerveSubsystem,
      SwerveSubsystem.FrontLeftCommissioningAction action) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.action = Objects.requireNonNull(action, "action");
    addRequirements(swerveSubsystem);
  }

  /**
   * Creates the default positive Front Left drive pulse.
   *
   * @param swerveSubsystem swerve subsystem
   * @return bounded commissioning command
   */
  public static SwerveFrontLeftOpenLoopCommissioningCommand drivePositive(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftOpenLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftCommissioningAction.DRIVE_POSITIVE);
  }

  /**
   * Creates the default negative Front Left drive pulse.
   *
   * @param swerveSubsystem swerve subsystem
   * @return bounded commissioning command
   */
  public static SwerveFrontLeftOpenLoopCommissioningCommand driveNegative(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftOpenLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftCommissioningAction.DRIVE_NEGATIVE);
  }

  /**
   * Creates the default positive Front Left steer pulse.
   *
   * @param swerveSubsystem swerve subsystem
   * @return bounded commissioning command
   */
  public static SwerveFrontLeftOpenLoopCommissioningCommand steerPositive(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftOpenLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftCommissioningAction.STEER_POSITIVE);
  }

  /**
   * Creates the default negative Front Left steer pulse.
   *
   * @param swerveSubsystem swerve subsystem
   * @return bounded commissioning command
   */
  public static SwerveFrontLeftOpenLoopCommissioningCommand steerNegative(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftOpenLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftCommissioningAction.STEER_NEGATIVE);
  }

  @Override
  public void initialize() {
    initialized = true;
    outputStopped = false;
    timedOut = false;
    rejected = false;

    if (!DriverStation.isTestEnabled()) {
      swerveSubsystem.stopFrontLeftCommissioning();
      outputStopped = true;
      rejected = true;
      return;
    }

    pulseTimer.restart();
    try {
      if (!swerveSubsystem.startFrontLeftCommissioning(action)) {
        pulseTimer.stop();
        outputStopped = true;
        rejected = true;
      }
    } catch (RuntimeException failure) {
      initialized = false;
      pulseTimer.stop();
      outputStopped = true;
      throw failure;
    }
  }

  @Override
  public void execute() {
    if (initialized && !outputStopped && !DriverStation.isTestEnabled()) {
      swerveSubsystem.stopFrontLeftCommissioning();
      outputStopped = true;
      rejected = true;
      return;
    }

    if (initialized
        && !outputStopped
        && pulseTimer.hasElapsed(
            Constants.SwerveConstants.kFrontLeftCommissioningPulseDurationSeconds)) {
      swerveSubsystem.stopFrontLeftCommissioning();
      outputStopped = true;
      timedOut = true;
    }
  }

  @Override
  public boolean isFinished() {
    return initialized
        && (rejected
            || !DriverStation.isTestEnabled()
            || timedOut
            || pulseTimer.hasElapsed(
                Constants.SwerveConstants.kFrontLeftCommissioningPulseDurationSeconds));
  }

  @Override
  public void end(boolean interrupted) {
    initialized = false;
    pulseTimer.stop();
    if (!outputStopped) {
      swerveSubsystem.stopFrontLeftCommissioning();
      outputStopped = true;
    }
  }

}
