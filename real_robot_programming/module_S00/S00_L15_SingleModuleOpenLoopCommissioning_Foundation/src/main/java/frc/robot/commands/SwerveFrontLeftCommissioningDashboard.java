// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/**
 * Publishes the four explicit Front Left commissioning commands for Glass/SmartDashboard.
 */
public final class SwerveFrontLeftCommissioningDashboard {
  /**
   * Publishes exactly one button for each fixed Front Left commissioning action.
   *
   * @param swerveSubsystem subsystem that owns the Front Left commissioning boundary
   */
  public SwerveFrontLeftCommissioningDashboard(SwerveSubsystem swerveSubsystem) {
    SwerveSubsystem acceptedSubsystem =
        Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    SmartDashboard.putData(
        "FL Drive Positive",
        SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(acceptedSubsystem));
    SmartDashboard.putData(
        "FL Drive Negative",
        SwerveFrontLeftOpenLoopCommissioningCommand.driveNegative(acceptedSubsystem));
    SmartDashboard.putData(
        "FL Steer Positive",
        SwerveFrontLeftOpenLoopCommissioningCommand.steerPositive(acceptedSubsystem));
    SmartDashboard.putData(
        "FL Steer Negative",
        SwerveFrontLeftOpenLoopCommissioningCommand.steerNegative(acceptedSubsystem));
  }
}
