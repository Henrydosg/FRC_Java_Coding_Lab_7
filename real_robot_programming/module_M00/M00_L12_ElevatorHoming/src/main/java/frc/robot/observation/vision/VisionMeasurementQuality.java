// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import java.util.Objects;

/**
 * Immutable, vendor-neutral quality decision for one vision target measurement.
 *
 * @param acceptance whether the measurement satisfies the quality policy
 * @param uncertaintyClass qualitative uncertainty assigned to the measurement
 * @param rejectionReason explicit reason for a rejected measurement
 */
public record VisionMeasurementQuality(
    Acceptance acceptance,
    UncertaintyClass uncertaintyClass,
    RejectionReason rejectionReason) {

  /** Whether a structurally valid measurement satisfies the current quality policy. */
  public enum Acceptance {
    ACCEPTED,
    REJECTED
  }

  /** Qualitative uncertainty class without covariance or standard-deviation semantics. */
  public enum UncertaintyClass {
    LOW,
    MEDIUM,
    HIGH,
    UNUSABLE
  }

  /** Explicit reason for a rejected measurement. */
  public enum RejectionReason {
    NONE,
    TARGET_TOO_FAR
  }

  /** Rejects null or contradictory quality states. */
  public VisionMeasurementQuality {
    acceptance = Objects.requireNonNull(acceptance, "acceptance");
    uncertaintyClass = Objects.requireNonNull(uncertaintyClass, "uncertaintyClass");
    rejectionReason = Objects.requireNonNull(rejectionReason, "rejectionReason");

    boolean acceptedStateIsValid =
        acceptance == Acceptance.ACCEPTED
            && uncertaintyClass != UncertaintyClass.UNUSABLE
            && rejectionReason == RejectionReason.NONE;
    boolean rejectedStateIsValid =
        acceptance == Acceptance.REJECTED
            && uncertaintyClass == UncertaintyClass.UNUSABLE
            && rejectionReason == RejectionReason.TARGET_TOO_FAR;

    if (!acceptedStateIsValid && !rejectedStateIsValid) {
      throw new IllegalArgumentException("Inconsistent vision measurement quality state");
    }
  }
}
