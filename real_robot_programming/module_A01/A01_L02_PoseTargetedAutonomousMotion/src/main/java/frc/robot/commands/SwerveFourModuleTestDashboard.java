// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/** Publishes the fixed four-module Test-mode verification commands. */
public final class SwerveFourModuleTestDashboard {
  /** Publishes exactly three motion commands and one explicit stop command. */
  public SwerveFourModuleTestDashboard(SwerveSubsystem swerveSubsystem) {
    SwerveSubsystem acceptedSubsystem =
        Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    SmartDashboard.putData(
        "Four Module Forward", SwerveFourModuleTestCommand.forward(acceptedSubsystem));
    SmartDashboard.putData(
        "Four Module Robot Left", SwerveFourModuleTestCommand.robotLeft(acceptedSubsystem));
    SmartDashboard.putData(
        "Four Module Rotate CCW", SwerveFourModuleTestCommand.rotateCcw(acceptedSubsystem));
    SmartDashboard.putData(
        "Four Module Stop", SwerveFourModuleTestCommand.stop(acceptedSubsystem));
  }
}
