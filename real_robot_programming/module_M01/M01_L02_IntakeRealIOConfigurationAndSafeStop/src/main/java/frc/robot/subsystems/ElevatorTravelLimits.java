// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

/** Immutable logical-meter configuration for Elevator position-request admission. */
public record ElevatorTravelLimits(double minPositionMeters, double maxPositionMeters) {
  /** Validates finite, strictly ordered logical-meter bounds. */
  public ElevatorTravelLimits {
    if (!Double.isFinite(minPositionMeters)) {
      throw new IllegalArgumentException("minPositionMeters must be finite");
    }
    if (!Double.isFinite(maxPositionMeters)) {
      throw new IllegalArgumentException("maxPositionMeters must be finite");
    }
    if (minPositionMeters >= maxPositionMeters) {
      throw new IllegalArgumentException("minPositionMeters must be less than maxPositionMeters");
    }
  }
}
