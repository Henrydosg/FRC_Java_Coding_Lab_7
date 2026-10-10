// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/** Runs one fixed, bounded, Test-mode four-module chassis-speed verification request. */
public final class SwerveFourModuleTestCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final ChassisSpeeds chassisSpeeds;
  private final boolean stopOnly;
  private final Timer durationTimer = new Timer();
  private boolean initialized;
  private boolean outputStopped;

  private SwerveFourModuleTestCommand(
      SwerveSubsystem swerveSubsystem, ChassisSpeeds chassisSpeeds, boolean stopOnly) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.chassisSpeeds = chassisSpeeds;
    this.stopOnly = stopOnly;
    addRequirements(swerveSubsystem);
  }

  /** Creates the fixed positive robot-forward verification command. */
  public static SwerveFourModuleTestCommand forward(SwerveSubsystem swerveSubsystem) {
    return new SwerveFourModuleTestCommand(
        swerveSubsystem,
        new ChassisSpeeds(
            Constants.SwerveConstants.kFourModuleTestTranslationSpeedMetersPerSecond,
            0.0,
            0.0),
        false);
  }

  /** Creates the fixed positive robot-left verification command. */
  public static SwerveFourModuleTestCommand robotLeft(SwerveSubsystem swerveSubsystem) {
    return new SwerveFourModuleTestCommand(
        swerveSubsystem,
        new ChassisSpeeds(
            0.0,
            Constants.SwerveConstants.kFourModuleTestTranslationSpeedMetersPerSecond,
            0.0),
        false);
  }

  /** Creates the fixed positive counterclockwise rotation verification command. */
  public static SwerveFourModuleTestCommand rotateCcw(SwerveSubsystem swerveSubsystem) {
    return new SwerveFourModuleTestCommand(
        swerveSubsystem,
        new ChassisSpeeds(
            0.0,
            0.0,
            Constants.SwerveConstants.kFourModuleTestRotationSpeedRadiansPerSecond),
        false);
  }

  /** Creates the explicit all-module stop command. */
  public static SwerveFourModuleTestCommand stop(SwerveSubsystem swerveSubsystem) {
    return new SwerveFourModuleTestCommand(swerveSubsystem, null, true);
  }

  @Override
  public void initialize() {
    initialized = true;
    outputStopped = false;

    if (stopOnly) {
      stopOnce();
      return;
    }

    if (!DriverStation.isTestEnabled() || !DriverStation.isEnabled()) {
      stopOnce();
      return;
    }

    durationTimer.restart();
    try {
      swerveSubsystem.acceptChassisSpeeds(chassisSpeeds);
    } catch (RuntimeException failure) {
      durationTimer.stop();
      initialized = false;
      stopAfterFailure(failure);
      throw failure;
    }
  }

  @Override
  public void execute() {
    if (!initialized || outputStopped || stopOnly) {
      return;
    }

    if (!DriverStation.isTestEnabled()
        || !DriverStation.isEnabled()
        || durationTimer.hasElapsed(Constants.SwerveConstants.kFourModuleTestCommandDurationSeconds)) {
      stopOnce();
    }
  }

  @Override
  public boolean isFinished() {
    return initialized
        && (outputStopped
            || stopOnly
            || !DriverStation.isTestEnabled()
            || !DriverStation.isEnabled()
            || durationTimer.hasElapsed(
                Constants.SwerveConstants.kFourModuleTestCommandDurationSeconds));
  }

  @Override
  public void end(boolean interrupted) {
    durationTimer.stop();
    if (initialized) {
      stopOnce();
    }
    initialized = false;
  }

  private void stopOnce() {
    if (!outputStopped) {
      outputStopped = true;
      swerveSubsystem.stop();
    }
  }

  private void stopAfterFailure(RuntimeException failure) {
    try {
      stopOnce();
    } catch (RuntimeException stopFailure) {
      failure.addSuppressed(stopFailure);
    }
  }
}
