// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/** Performs one Disabled-only known-field-pose reset request. */
public final class ResetKnownFieldPoseCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final Pose2d requestedPose;

  /**
   * Creates a one-shot known-field-pose reset command.
   *
   * <p>The requested pose is passed unchanged to the subsystem, which owns all pose and
   * measurement validation.
   *
   * @param swerveSubsystem subsystem that owns localization state
   * @param requestedPose requested known field pose
   */
  public ResetKnownFieldPoseCommand(
      SwerveSubsystem swerveSubsystem, Pose2d requestedPose) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.requestedPose = requestedPose;
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    if (DriverStation.isEnabled()) {
      return;
    }

    swerveSubsystem.resetKnownFieldPose(requestedPose);
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
