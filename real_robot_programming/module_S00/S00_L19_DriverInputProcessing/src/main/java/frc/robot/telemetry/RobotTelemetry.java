// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry;

import frc.robot.controls.XboxDriverInputSource;
import frc.robot.observation.DriverInputObservation;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.driver.DriverInputTelemetryFacade;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;
import java.util.Objects;

/**
 * Coordinates read-only Swerve telemetry publication.
 */
public final class RobotTelemetry {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveTelemetryFacade swerveTelemetryFacade;
  private final XboxDriverInputSource driverInputSource;
  private final DriverInputTelemetryFacade driverInputTelemetryFacade;

  /**
   * Creates the runtime telemetry coordinator.
   *
   * @param swerveSubsystem Swerve observation source
   * @param swerveTelemetryFacade Swerve telemetry publisher
   * @param driverInputSource driver-input observation source
   * @param driverInputTelemetryFacade driver-input telemetry publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      XboxDriverInputSource driverInputSource,
      DriverInputTelemetryFacade driverInputTelemetryFacade) {
    this.swerveSubsystem =
        Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.swerveTelemetryFacade =
        Objects.requireNonNull(
            swerveTelemetryFacade,
            "swerveTelemetryFacade");
    this.driverInputSource =
        Objects.requireNonNull(driverInputSource, "driverInputSource");
    this.driverInputTelemetryFacade =
        Objects.requireNonNull(
            driverInputTelemetryFacade,
            "driverInputTelemetryFacade");
  }

  /**
   * Publishes the latest complete observation when one exists.
   */
  public void periodic() {
    DriverInputObservation driverInputObservation = driverInputSource.read();
    driverInputTelemetryFacade.publish(driverInputObservation);

    swerveSubsystem
        .getObservation()
        .ifPresent(swerveTelemetryFacade::publish);
  }
}
