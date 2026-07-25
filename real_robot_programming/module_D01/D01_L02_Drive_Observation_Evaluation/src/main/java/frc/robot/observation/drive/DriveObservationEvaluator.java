// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.drive;

import java.util.Objects;

/**
 * Evaluates immutable drivetrain observations.
 */
public final class DriveObservationEvaluator {
  /**
   * Returns whether both drivetrain outputs are within the supplied stopping tolerance.
   *
   * @param observation immutable drivetrain observation
   * @param outputTolerance non-negative finite stopping tolerance
   * @return true when both finite outputs are within the inclusive tolerance
   */
  public boolean isStopped(
      DriveObservation observation,
      double outputTolerance) {
    Objects.requireNonNull(
        observation,
        "observation");

    if (!Double.isFinite(outputTolerance)
        || outputTolerance < 0.0) {
      throw new IllegalArgumentException(
          "outputTolerance must be finite and non-negative");
    }

    double leftOutput = observation.leftAppliedOutput();
    double rightOutput = observation.rightAppliedOutput();

    if (!Double.isFinite(leftOutput)
        || !Double.isFinite(rightOutput)) {
      return false;
    }

    return Math.abs(leftOutput) <= outputTolerance
        && Math.abs(rightOutput) <= outputTolerance;
  }
}
