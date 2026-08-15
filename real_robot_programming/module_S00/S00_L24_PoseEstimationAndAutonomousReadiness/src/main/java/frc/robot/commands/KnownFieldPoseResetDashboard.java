// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/** Publishes the provisional Disabled-only known-starting-pose reset command. */
public final class KnownFieldPoseResetDashboard {
  private static final String DASHBOARD_LABEL = "Reset Known Starting Pose";

  /** Registers the known-starting-pose reset command for Glass/SmartDashboard. */
  public KnownFieldPoseResetDashboard(SwerveSubsystem swerveSubsystem) {
    SwerveSubsystem acceptedSubsystem =
        Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    SmartDashboard.putData(
        DASHBOARD_LABEL,
        new ResetKnownFieldPoseCommand(
            acceptedSubsystem, Constants.FieldConstants.kLearningStartingPose));
  }
}
