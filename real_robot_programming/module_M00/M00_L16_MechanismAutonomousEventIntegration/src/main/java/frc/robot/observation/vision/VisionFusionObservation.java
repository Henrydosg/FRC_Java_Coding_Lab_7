// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

/**
 * Immutable, vendor-neutral evidence for the L09 vision-to-estimator handoff.
 *
 * @param qualifiedHandoffCount number of statically valid measurements reaching Swerve admission
 * @param lastQualifiedHandoffTimestampSeconds timestamp of the latest qualified handoff
 * @param acceptedFusionCount number of measurements accepted by the pose estimator
 * @param lastAcceptedFusionTimestampSeconds timestamp of the latest accepted fusion
 */
public record VisionFusionObservation(
    long qualifiedHandoffCount,
    double lastQualifiedHandoffTimestampSeconds,
    long acceptedFusionCount,
    double lastAcceptedFusionTimestampSeconds) {

  /** Validates count and timestamp meaning at the immutable observation boundary. */
  public VisionFusionObservation {
    if (qualifiedHandoffCount < 0 || acceptedFusionCount < 0) {
      throw new IllegalArgumentException("fusion counts must be nonnegative");
    }
    validateTimestamp(
        qualifiedHandoffCount,
        lastQualifiedHandoffTimestampSeconds,
        "lastQualifiedHandoffTimestampSeconds");
    validateTimestamp(
        acceptedFusionCount,
        lastAcceptedFusionTimestampSeconds,
        "lastAcceptedFusionTimestampSeconds");
  }

  /** Returns the initial observation before any measurement reaches Swerve. */
  public static VisionFusionObservation empty() {
    return new VisionFusionObservation(0, Double.NaN, 0, Double.NaN);
  }

  private static void validateTimestamp(long count, double timestampSeconds, String name) {
    if (count == 0) {
      if (!Double.isNaN(timestampSeconds)) {
        throw new IllegalArgumentException(name + " must be NaN when its count is zero");
      }
      return;
    }
    if (!Double.isFinite(timestampSeconds)) {
      throw new IllegalArgumentException(name + " must be finite when its count is positive");
    }
  }
}
