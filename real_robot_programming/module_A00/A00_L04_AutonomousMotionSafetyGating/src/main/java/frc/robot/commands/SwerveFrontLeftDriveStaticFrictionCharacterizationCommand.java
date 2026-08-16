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
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/**
 * Runs one manually triggered, bounded Front Left drive static-friction voltage pulse.
 *
 * <p>The wheel must be securely raised off the floor before scheduling this Test-mode-only command.
 * Each dashboard button creates one independent fixed-voltage pulse; this command never sweeps.
 */
public final class SwerveFrontLeftDriveStaticFrictionCharacterizationCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final double requestedVoltageVolts;
  private final Timer timeoutTimer = new Timer();
  private boolean initialized;
  private boolean outputStopped;
  private boolean rejected;
  private boolean timedOut;

  private SwerveFrontLeftDriveStaticFrictionCharacterizationCommand(
      SwerveSubsystem swerveSubsystem, double requestedVoltageVolts) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.requestedVoltageVolts = requestedVoltageVolts;
    addRequirements(swerveSubsystem);
  }

  /**
   * Creates one manual Front Left drive static-friction characterization pulse.
   *
   * @param swerveSubsystem subsystem owning Front Left commissioning
   * @param voltageVolts requested positive pulse voltage in volts
   * @return bounded Test-mode-only characterization command
   */
  public static SwerveFrontLeftDriveStaticFrictionCharacterizationCommand atVoltage(
      SwerveSubsystem swerveSubsystem, double voltageVolts) {
    return new SwerveFrontLeftDriveStaticFrictionCharacterizationCommand(
        swerveSubsystem, voltageVolts);
  }

  @Override
  public void initialize() {
    initialized = true;
    outputStopped = false;
    rejected = false;
    timedOut = false;

    if (!DriverStation.isTestEnabled() || !DriverStation.isEnabled()) {
      swerveSubsystem.stopFrontLeftStaticFrictionCharacterization(
          requestedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.REJECTED);
      outputStopped = true;
      rejected = true;
      return;
    }

    timeoutTimer.restart();
    try {
      if (!swerveSubsystem.startFrontLeftDriveStaticFrictionCharacterization(requestedVoltageVolts)) {
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
    if (initialized && !outputStopped && (!DriverStation.isTestEnabled() || !DriverStation.isEnabled())) {
      swerveSubsystem.stopFrontLeftStaticFrictionCharacterization(
          requestedVoltageVolts,
          !DriverStation.isEnabled()
              ? SwerveModuleIO.StaticFrictionStopReason.DISABLE
              : SwerveModuleIO.StaticFrictionStopReason.MODE_EXIT);
      outputStopped = true;
      rejected = true;
      return;
    }

    if (initialized
        && !outputStopped
        && timeoutTimer.hasElapsed(
            Constants.SwerveConstants.kFrontLeftDriveStaticFrictionPulseDurationSeconds)) {
      swerveSubsystem.stopFrontLeftStaticFrictionCharacterization(
          requestedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.TIMEOUT);
      outputStopped = true;
      timedOut = true;
    }
  }

  @Override
  public boolean isFinished() {
    return initialized
        && (rejected
            || !DriverStation.isTestEnabled()
            || !DriverStation.isEnabled()
            || timedOut
            || timeoutTimer.hasElapsed(
                Constants.SwerveConstants.kFrontLeftDriveStaticFrictionPulseDurationSeconds));
  }

  @Override
  public void end(boolean interrupted) {
    initialized = false;
    timeoutTimer.stop();
    if (!outputStopped) {
      swerveSubsystem.stopFrontLeftStaticFrictionCharacterization(
          requestedVoltageVolts,
          interrupted
              ? SwerveModuleIO.StaticFrictionStopReason.INTERRUPTED
              : !DriverStation.isEnabled()
                  ? SwerveModuleIO.StaticFrictionStopReason.DISABLE
                  : !DriverStation.isTestEnabled()
                      ? SwerveModuleIO.StaticFrictionStopReason.MODE_EXIT
                      : SwerveModuleIO.StaticFrictionStopReason.TIMEOUT);
      outputStopped = true;
    }
  }
}
