// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

/**
 * Immutable, vendor-neutral timing facts for one vision measurement.
 *
 * <p>The measurement timestamp is derived from the receive timestamp and total capture-to-receive
 * latency so that there is one source of truth for the timing relationship.
 *
 * @param receiveTimestampSeconds robot-time timestamp at which the complete sample was received
 * @param totalLatencySeconds total capture-to-receive latency
 */
public record VisionTiming(
    double receiveTimestampSeconds,
    double totalLatencySeconds) {

  /** Validates the locked finite-timestamp and nonnegative-latency contract. */
  public VisionTiming {
    if (!Double.isFinite(receiveTimestampSeconds)) {
      throw new IllegalArgumentException("receiveTimestampSeconds must be finite");
    }
    if (!Double.isFinite(totalLatencySeconds)) {
      throw new IllegalArgumentException("totalLatencySeconds must be finite");
    }
    if (totalLatencySeconds < 0.0) {
      throw new IllegalArgumentException("totalLatencySeconds must be nonnegative");
    }
    double measurementTimestampSeconds = receiveTimestampSeconds - totalLatencySeconds;
    if (!Double.isFinite(measurementTimestampSeconds)) {
      throw new IllegalArgumentException("measurementTimestampSeconds must be finite");
    }
  }

  /**
   * Derives the canonical timestamp at which the vision measurement occurred.
   *
   * @return the measurement timestamp in seconds on the robot timebase
   */
  public double measurementTimestampSeconds() {
    return receiveTimestampSeconds - totalLatencySeconds;
  }
}
