// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry;

import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.telemetry.drive.DriveTelemetryFacade;
import frc.robot.telemetry.feeder.FeederTelemetryFacade;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;
import frc.robot.telemetry.intake.IntakeTelemetryFacade;
import java.util.Objects;

/**
 * Coordinates read-only robot telemetry publishing.
 */
public final class RobotTelemetry {
  private final DriveSubsystem driveSubsystem;
  private final DriveTelemetryFacade driveTelemetryFacade;
  private final IntakeSubsystem intakeSubsystem;
  private final IntakeTelemetryFacade intakeTelemetryFacade;
  private final FlywheelSubsystem flywheelSubsystem;
  private final FlywheelTelemetryFacade flywheelTelemetryFacade;
  private final FeederSubsystem feederSubsystem;
  private final FeederTelemetryFacade feederTelemetryFacade;

  /**
   * Creates the robot telemetry coordinator.
   *
   * @param driveSubsystem drivetrain observation source
   * @param driveTelemetryFacade drivetrain telemetry publisher
   * @param intakeSubsystem intake observation source
   * @param intakeTelemetryFacade intake telemetry publisher
   * @param flywheelSubsystem flywheel observation source
   * @param flywheelTelemetryFacade flywheel telemetry publisher
   * @param feederSubsystem feeder observation source
   * @param feederTelemetryFacade feeder telemetry publisher
   */
  public RobotTelemetry(
      DriveSubsystem driveSubsystem,
      DriveTelemetryFacade driveTelemetryFacade,
      IntakeSubsystem intakeSubsystem,
      IntakeTelemetryFacade intakeTelemetryFacade,
      FlywheelSubsystem flywheelSubsystem,
      FlywheelTelemetryFacade flywheelTelemetryFacade,
      FeederSubsystem feederSubsystem,
      FeederTelemetryFacade feederTelemetryFacade) {
    this.driveSubsystem =
        Objects.requireNonNull(
            driveSubsystem,
            "driveSubsystem");
    this.driveTelemetryFacade =
        Objects.requireNonNull(
            driveTelemetryFacade,
            "driveTelemetryFacade");
    this.intakeSubsystem =
        Objects.requireNonNull(
            intakeSubsystem,
            "intakeSubsystem");
    this.intakeTelemetryFacade =
        Objects.requireNonNull(
            intakeTelemetryFacade,
            "intakeTelemetryFacade");
    this.flywheelSubsystem =
        Objects.requireNonNull(
            flywheelSubsystem,
            "flywheelSubsystem");
    this.flywheelTelemetryFacade =
        Objects.requireNonNull(
            flywheelTelemetryFacade,
            "flywheelTelemetryFacade");
    this.feederSubsystem =
        Objects.requireNonNull(
            feederSubsystem,
            "feederSubsystem");
    this.feederTelemetryFacade =
        Objects.requireNonNull(
            feederTelemetryFacade,
            "feederTelemetryFacade");
  }

  /**
   * Publishes observations from the latest completed subsystem periodic update.
   */
  public void periodic() {
    driveTelemetryFacade.publish(
        driveSubsystem.getObservation());
    intakeTelemetryFacade.publish(
        intakeSubsystem.getObservation());
    flywheelTelemetryFacade.publish(
        flywheelSubsystem.getObservation());
    feederTelemetryFacade.publish(
        feederSubsystem.getObservation());
  }
}
