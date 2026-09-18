// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import frc.robot.observation.vision.VisionMeasurementQuality.Acceptance;
import frc.robot.observation.vision.VisionMeasurementQuality.RejectionReason;
import frc.robot.observation.vision.VisionMeasurementQuality.UncertaintyClass;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import java.util.Objects;

/** Pure deterministic distance policy for one immutable target observation. */
public final class VisionMeasurementQualityEvaluator {
  private VisionMeasurementQualityEvaluator() {}

  /**
   * Immutable inclusive distance thresholds in meters.
   *
   * @param lowMaxMeters inclusive upper bound for LOW uncertainty
   * @param mediumMaxMeters inclusive upper bound for MEDIUM uncertainty
   * @param maximumAcceptedMeters inclusive upper bound for any accepted measurement
   */
  public record Policy(
      double lowMaxMeters, double mediumMaxMeters, double maximumAcceptedMeters) {

    /** Validates finite, nonnegative, nondecreasing thresholds. */
    public Policy {
      if (!Double.isFinite(lowMaxMeters)
          || !Double.isFinite(mediumMaxMeters)
          || !Double.isFinite(maximumAcceptedMeters)) {
        throw new IllegalArgumentException("Policy thresholds must be finite");
      }
      if (lowMaxMeters < 0.0
          || mediumMaxMeters < 0.0
          || maximumAcceptedMeters < 0.0) {
        throw new IllegalArgumentException("Policy thresholds must be nonnegative");
      }
      if (lowMaxMeters > mediumMaxMeters
          || mediumMaxMeters > maximumAcceptedMeters) {
        throw new IllegalArgumentException("Policy thresholds must be nondecreasing");
      }
    }
  }

  /**
   * Evaluates one target using only its camera-to-target translation norm.
   *
   * @param target immutable target observation
   * @param policy immutable inclusive distance thresholds
   * @return coherent immutable quality decision
   */
  public static VisionMeasurementQuality evaluate(TargetObservation target, Policy policy) {
    TargetObservation requiredTarget = Objects.requireNonNull(target, "target");
    Policy requiredPolicy = Objects.requireNonNull(policy, "policy");

    double distanceMeters =
        requiredTarget.cameraToTarget().getTranslation().getNorm();
    if (!Double.isFinite(distanceMeters)) {
      throw new IllegalArgumentException("Target distance must be finite");
    }

    if (distanceMeters <= requiredPolicy.lowMaxMeters()) {
      return new VisionMeasurementQuality(
          Acceptance.ACCEPTED, UncertaintyClass.LOW, RejectionReason.NONE);
    }
    if (distanceMeters <= requiredPolicy.mediumMaxMeters()) {
      return new VisionMeasurementQuality(
          Acceptance.ACCEPTED, UncertaintyClass.MEDIUM, RejectionReason.NONE);
    }
    if (distanceMeters <= requiredPolicy.maximumAcceptedMeters()) {
      return new VisionMeasurementQuality(
          Acceptance.ACCEPTED, UncertaintyClass.HIGH, RejectionReason.NONE);
    }
    return new VisionMeasurementQuality(
        Acceptance.REJECTED,
        UncertaintyClass.UNUSABLE,
        RejectionReason.TARGET_TOO_FAR);
  }
}
