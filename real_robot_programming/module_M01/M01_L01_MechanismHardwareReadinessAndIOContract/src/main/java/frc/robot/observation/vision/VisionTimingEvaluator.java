// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import java.util.Objects;

/** Pure deterministic freshness and timestamp-order evaluation for vision timing. */
public final class VisionTimingEvaluator {
  private VisionTimingEvaluator() {}

  /** Result of comparing a measurement age with an explicit freshness policy. */
  public enum Freshness {
    FRESH,
    STALE
  }

  /** Result of comparing two canonical measurement timestamps. */
  public enum Ordering {
    NEWER,
    DUPLICATE,
    OUT_OF_ORDER
  }

  /**
   * Evaluates whether one measurement is fresh at an explicit reference time.
   *
   * @param timing immutable timing facts for the measurement
   * @param referenceTimestampSeconds explicit current/reference timestamp in seconds
   * @param maximumFreshAgeSeconds inclusive maximum accepted age in seconds
   * @return FRESH when age is at or below the inclusive limit, otherwise STALE
   */
  public static Freshness classifyFreshness(
      VisionTiming timing,
      double referenceTimestampSeconds,
      double maximumFreshAgeSeconds) {
    VisionTiming requiredTiming = Objects.requireNonNull(timing, "timing");
    requireFinite(referenceTimestampSeconds, "referenceTimestampSeconds");
    requireFinite(maximumFreshAgeSeconds, "maximumFreshAgeSeconds");
    if (maximumFreshAgeSeconds < 0.0) {
      throw new IllegalArgumentException("maximumFreshAgeSeconds must be nonnegative");
    }

    double measurementTimestampSeconds = requiredTiming.measurementTimestampSeconds();
    if (referenceTimestampSeconds < measurementTimestampSeconds) {
      throw new IllegalArgumentException(
          "referenceTimestampSeconds must not precede measurementTimestampSeconds");
    }

    double ageSeconds = referenceTimestampSeconds - measurementTimestampSeconds;
    if (!Double.isFinite(ageSeconds)) {
      throw new IllegalArgumentException("ageSeconds must be finite");
    }
    return ageSeconds <= maximumFreshAgeSeconds ? Freshness.FRESH : Freshness.STALE;
  }

  /**
   * Classifies ordering using only canonical measurement timestamps.
   *
   * @param current current measurement timing facts
   * @param previous previous measurement timing facts
   * @return NEWER, DUPLICATE, or OUT_OF_ORDER
   */
  public static Ordering classifyOrdering(VisionTiming current, VisionTiming previous) {
    double currentTimestamp =
        Objects.requireNonNull(current, "current").measurementTimestampSeconds();
    double previousTimestamp =
        Objects.requireNonNull(previous, "previous").measurementTimestampSeconds();
    if (currentTimestamp > previousTimestamp) {
      return Ordering.NEWER;
    }
    if (currentTimestamp == previousTimestamp) {
      return Ordering.DUPLICATE;
    }
    return Ordering.OUT_OF_ORDER;
  }

  private static void requireFinite(double value, String name) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException(name + " must be finite");
    }
  }
}
