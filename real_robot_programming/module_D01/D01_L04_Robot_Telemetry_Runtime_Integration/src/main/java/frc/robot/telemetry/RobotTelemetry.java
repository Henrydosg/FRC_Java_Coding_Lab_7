// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.telemetry.drive.DriveTelemetryFacade;
import java.util.Objects;

/**
 * Coordinates read-only robot telemetry publishing.
 */
public final class RobotTelemetry {
  private final DriveSubsystem driveSubsystem;
  private final DriveTelemetryFacade driveTelemetryFacade;

  /**
   * Creates the robot telemetry coordinator.
   *
   * @param driveSubsystem drivetrain observation source
   * @param driveTelemetryFacade drivetrain telemetry publisher
   */
  public RobotTelemetry(
      DriveSubsystem driveSubsystem,
      DriveTelemetryFacade driveTelemetryFacade) {
    this.driveSubsystem =
        Objects.requireNonNull(
            driveSubsystem,
            "driveSubsystem");
    this.driveTelemetryFacade =
        Objects.requireNonNull(
            driveTelemetryFacade,
            "driveTelemetryFacade");
  }

  /**
   * Publishes observations from the latest completed subsystem periodic update.
   */
  public void periodic() {
    driveTelemetryFacade.publish(
        driveSubsystem.getObservation());
  }
}
