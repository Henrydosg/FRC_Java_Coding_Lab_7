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
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.function.DoubleSupplier;

/** Runs one bounded, robot-relative autonomous chassis-speed request. */
public final class BoundedRobotRelativeAutonomousDriveCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final ChassisSpeeds robotRelativeSpeeds;
  private final double durationSeconds;
  private final DoubleSupplier monotonicClock;

  private double startTimeSeconds = Double.NaN;
  private boolean initialized;
  private boolean finished;

  /**
   * Creates one bounded robot-relative autonomous motion command.
   *
   * @param swerveSubsystem drivetrain subsystem that owns actuation and stop behavior
   * @param robotRelativeSpeeds finite robot-relative chassis-speed request
   * @param durationSeconds finite positive motion duration in seconds
   * @param monotonicClock monotonic time source in seconds
   */
  public BoundedRobotRelativeAutonomousDriveCommand(
      SwerveSubsystem swerveSubsystem,
      ChassisSpeeds robotRelativeSpeeds,
      double durationSeconds,
      DoubleSupplier monotonicClock) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.robotRelativeSpeeds = copyAndValidate(robotRelativeSpeeds);
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
    finished = false;
    startTimeSeconds = readClock();
    swerveSubsystem.stop();

    if (!Double.isFinite(startTimeSeconds) || !DriverStation.isEnabled()) {
      finished = true;
      return;
    }

    try {
      swerveSubsystem.acceptChassisSpeeds(copy(robotRelativeSpeeds));
    } catch (RuntimeException failure) {
      finished = true;
      stopAfterFailure(failure);
    }
  }

  @Override
  public void execute() {
    // The finite robot-relative request is submitted once during initialize().
  }

  @Override
  public boolean isFinished() {
    if (!initialized || finished) {
      return initialized;
    }

    if (!DriverStation.isEnabled()) {
      finished = true;
      swerveSubsystem.stop();
      return true;
    }

    double currentTimeSeconds = readClock();
    double elapsedSeconds = currentTimeSeconds - startTimeSeconds;
    if (!Double.isFinite(currentTimeSeconds)
        || !Double.isFinite(elapsedSeconds)
        || elapsedSeconds < 0.0) {
      finished = true;
      swerveSubsystem.stop();
      return true;
    }

    if (elapsedSeconds >= durationSeconds) {
      finished = true;
      return true;
    }
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.stop();
    initialized = false;
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

  private void stopAfterFailure(RuntimeException failure) {
    try {
      swerveSubsystem.stop();
    } catch (RuntimeException stopFailure) {
      failure.addSuppressed(stopFailure);
    }
  }

  private static ChassisSpeeds copyAndValidate(ChassisSpeeds speeds) {
    Objects.requireNonNull(speeds, "robotRelativeSpeeds");
    if (!isFinite(speeds)) {
      throw new IllegalArgumentException("robotRelativeSpeeds must be finite");
    }
    return copy(speeds);
  }

  private static ChassisSpeeds copy(ChassisSpeeds speeds) {
    return new ChassisSpeeds(
        speeds.vxMetersPerSecond,
        speeds.vyMetersPerSecond,
        speeds.omegaRadiansPerSecond);
  }

  private static boolean isFinite(ChassisSpeeds speeds) {
    return Double.isFinite(speeds.vxMetersPerSecond)
        && Double.isFinite(speeds.vyMetersPerSecond)
        && Double.isFinite(speeds.omegaRadiansPerSecond);
  }
}
