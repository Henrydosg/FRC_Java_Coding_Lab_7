// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.validation;

import frc.robot.observation.DriveThreeMeterValidationObservation;

/** Consumes immutable three-meter validation observations. */
public interface DriveThreeMeterValidationTelemetry {
  /** Publishes one immutable validation state. */
  void publish(DriveThreeMeterValidationObservation observation);
}
