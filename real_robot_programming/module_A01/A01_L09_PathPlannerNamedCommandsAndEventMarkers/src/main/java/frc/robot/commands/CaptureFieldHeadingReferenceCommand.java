// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/** Captures the operator-selected field-heading zero while the robot is Disabled. */
public final class CaptureFieldHeadingReferenceCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private boolean captured;

  /**
   * Creates the one-shot field-heading reference capture command.
   *
   * @param swerveSubsystem Swerve behavior owner
   */
  public CaptureFieldHeadingReferenceCommand(SwerveSubsystem swerveSubsystem) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    captured = swerveSubsystem.captureFieldHeadingReference();
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }

  /**
   * Reports the result of the most recent initialize call.
   *
   * @return true when the subsystem accepted the capture
   */
  public boolean wasCaptured() {
    return captured;
  }
}
