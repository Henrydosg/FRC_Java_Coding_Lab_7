// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

/** Immutable read model for one L23 three-meter validation state. */
public record DriveThreeMeterValidationObservation(
    double targetMeters,
    double measuredMeters,
    double frontLeftDeltaMeters,
    double frontRightDeltaMeters,
    double backLeftDeltaMeters,
    double backRightDeltaMeters,
    boolean running,
    boolean complete,
    String faultOrAbortReason) {

  /** Creates the idle state published before a validation command starts. */
  public static DriveThreeMeterValidationObservation idle(double targetMeters) {
    return new DriveThreeMeterValidationObservation(
        targetMeters,
        0.0,
        0.0,
        0.0,
        0.0,
        0.0,
        false,
        false,
        "NONE");
  }
}
