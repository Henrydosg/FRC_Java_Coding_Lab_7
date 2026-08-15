// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.validation.DriveThreeMeterValidationTelemetry;
import java.util.Objects;

/** Publishes the bounded three-meter validation command to Glass/SmartDashboard. */
public final class DriveThreeMeterValidationDashboard {
  /** Registers the Glass-triggered validation command. */
  public DriveThreeMeterValidationDashboard(
      SwerveSubsystem swerveSubsystem,
      DriveThreeMeterValidationTelemetry telemetry) {
    SmartDashboard.putData(
        "Drive 3m Validation",
        new DriveThreeMeterValidationCommand(
            Objects.requireNonNull(swerveSubsystem, "swerveSubsystem"),
            Objects.requireNonNull(telemetry, "telemetry")));
  }
}
