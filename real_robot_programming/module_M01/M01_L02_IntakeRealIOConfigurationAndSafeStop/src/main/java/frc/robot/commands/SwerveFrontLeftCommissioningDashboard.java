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
import java.util.Locale;
import java.util.Objects;

/**
 * Publishes fixed Front Left commissioning commands for Glass/SmartDashboard.
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
    SmartDashboard.putData(
        "FL Closed-Loop Drive Positive",
        SwerveFrontLeftClosedLoopCommissioningCommand.drivePositive(acceptedSubsystem));
    SmartDashboard.putData(
        "FL Closed-Loop Drive Negative",
        SwerveFrontLeftClosedLoopCommissioningCommand.driveNegative(acceptedSubsystem));
    SmartDashboard.putData(
        "FL Closed-Loop Steer Positive",
        SwerveFrontLeftClosedLoopCommissioningCommand.steerPositive(acceptedSubsystem));
    SmartDashboard.putData(
        "FL Closed-Loop Steer Negative",
        SwerveFrontLeftClosedLoopCommissioningCommand.steerNegative(acceptedSubsystem));

    for (int step = 1;
        step <= Constants.SwerveConstants.kFrontLeftDriveStaticFrictionVoltageStepCount;
        step++) {
      double voltageVolts =
          step * Constants.SwerveConstants.kFrontLeftDriveStaticFrictionVoltageIncrementVolts;
      SmartDashboard.putData(
          String.format(Locale.ROOT, "FL Drive Static Friction +%.2f V", voltageVolts),
          SwerveFrontLeftDriveStaticFrictionCharacterizationCommand.atVoltage(
              acceptedSubsystem, voltageVolts));
    }
  }
}
