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
 * Runs one bounded, Front Left-only closed-loop commissioning request.
 *
 * <p>This command is an explicit Test-mode commissioning interface. It is never scheduled
 * automatically by the robot lifecycle or RobotContainer.
 */
public final class SwerveFrontLeftClosedLoopCommissioningCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveSubsystem.FrontLeftClosedLoopCommissioningAction action;
  private final Timer timeoutTimer = new Timer();
  private boolean initialized;
  private boolean outputStopped;
  private boolean timedOut;
  private boolean rejected;

  private SwerveFrontLeftClosedLoopCommissioningCommand(
      SwerveSubsystem swerveSubsystem,
      SwerveSubsystem.FrontLeftClosedLoopCommissioningAction action) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.action = Objects.requireNonNull(action, "action");
    addRequirements(swerveSubsystem);
  }

  /** Creates the fixed positive Front Left drive-velocity test command. */
  public static SwerveFrontLeftClosedLoopCommissioningCommand drivePositive(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftClosedLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.DRIVE_POSITIVE);
  }

  /** Creates the fixed negative Front Left drive-velocity test command. */
  public static SwerveFrontLeftClosedLoopCommissioningCommand driveNegative(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftClosedLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.DRIVE_NEGATIVE);
  }

  /** Creates the fixed positive Front Left relative steer-step test command. */
  public static SwerveFrontLeftClosedLoopCommissioningCommand steerPositive(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftClosedLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.STEER_POSITIVE);
  }

  /** Creates the fixed negative Front Left relative steer-step test command. */
  public static SwerveFrontLeftClosedLoopCommissioningCommand steerNegative(
      SwerveSubsystem swerveSubsystem) {
    return new SwerveFrontLeftClosedLoopCommissioningCommand(
        swerveSubsystem,
        SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.STEER_NEGATIVE);
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

    timeoutTimer.restart();
    try {
      if (!startCommissioningRequest()) {
        timeoutTimer.stop();
        outputStopped = true;
        rejected = true;
      }
    } catch (RuntimeException failure) {
      initialized = false;
      timeoutTimer.stop();
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
        && timeoutTimer.hasElapsed(
            Constants.SwerveConstants.kFrontLeftClosedLoopCommissioningTimeoutSeconds)) {
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
            || timeoutTimer.hasElapsed(
                Constants.SwerveConstants.kFrontLeftClosedLoopCommissioningTimeoutSeconds));
  }

  @Override
  public void end(boolean interrupted) {
    initialized = false;
    timeoutTimer.stop();
    if (!outputStopped) {
      swerveSubsystem.stopFrontLeftCommissioning();
      outputStopped = true;
    }
  }

  private boolean startCommissioningRequest() {
    return switch (action) {
      case DRIVE_POSITIVE, DRIVE_NEGATIVE ->
          swerveSubsystem.startFrontLeftDriveVelocityCommissioning(action);
      case STEER_POSITIVE, STEER_NEGATIVE ->
          swerveSubsystem.startFrontLeftSteerAngleCommissioning(action);
    };
  }
}
