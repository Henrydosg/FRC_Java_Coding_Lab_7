// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.function.DoubleSupplier;

/**
 * Holds the drivetrain stopped for one bounded autonomous command lifecycle.
 *
 * <p>This command intentionally performs no drivetrain request. It establishes the autonomous
 * command requirement, bounded timing, and stop-on-exit contract for A00_L01.
 */
public final class AutonomousSafetyHoldCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final double durationSeconds;
  private final DoubleSupplier monotonicClock;

  private double startTimeSeconds = Double.NaN;
  private boolean initialized;
  private boolean invalidClock;

  /**
   * Creates a bounded zero-motion autonomous lifecycle command.
   *
   * @param swerveSubsystem drivetrain subsystem that owns the stop authority
   * @param durationSeconds finite positive command duration in seconds
   * @param monotonicClock monotonic time source in seconds
   */
  public AutonomousSafetyHoldCommand(
      SwerveSubsystem swerveSubsystem,
      double durationSeconds,
      DoubleSupplier monotonicClock) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    if (!Double.isFinite(durationSeconds) || durationSeconds <= 0.0) {
      throw new IllegalArgumentException("durationSeconds must be finite and positive");
    }
    this.durationSeconds = durationSeconds;
    this.monotonicClock = Objects.requireNonNull(monotonicClock, "monotonicClock");
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    initialized = true;
    invalidClock = false;
    startTimeSeconds = readClock();
    if (!Double.isFinite(startTimeSeconds)) {
      invalidClock = true;
    }
    swerveSubsystem.stop();
  }

  @Override
  public void execute() {
    // Intentionally no drivetrain request, IO access, telemetry, or business logic.
  }

  @Override
  public boolean isFinished() {
    if (!initialized || invalidClock) {
      return initialized;
    }

    double currentTimeSeconds = readClock();
    double elapsedSeconds = currentTimeSeconds - startTimeSeconds;
    if (!Double.isFinite(currentTimeSeconds)
        || !Double.isFinite(elapsedSeconds)
        || elapsedSeconds < 0.0) {
      invalidClock = true;
      swerveSubsystem.stop();
      return true;
    }
    return elapsedSeconds >= durationSeconds;
  }

  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.stop();
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }

  private double readClock() {
    try {
      return monotonicClock.getAsDouble();
    } catch (RuntimeException failure) {
      return Double.NaN;
    }
  }
}
