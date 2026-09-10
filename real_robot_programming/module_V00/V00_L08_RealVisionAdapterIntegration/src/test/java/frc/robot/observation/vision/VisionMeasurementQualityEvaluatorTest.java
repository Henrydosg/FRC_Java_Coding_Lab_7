// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.observation.vision.VisionMeasurementQuality.Acceptance;
import frc.robot.observation.vision.VisionMeasurementQuality.RejectionReason;
import frc.robot.observation.vision.VisionMeasurementQuality.UncertaintyClass;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies the pure V00_L06 distance-based vision measurement quality contract. */
class VisionMeasurementQualityEvaluatorTest {
  private static final Policy kStandardPolicy = new Policy(1.0, 2.0, 3.0);

  @Test
  void classifiesEveryOrderedDistanceRegionAndInclusiveBoundary() {
    assertQuality(targetAt(0.0, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.LOW, RejectionReason.NONE);
    assertQuality(targetAt(0.5, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.LOW, RejectionReason.NONE);
    assertQuality(targetAt(1.0, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.LOW, RejectionReason.NONE);
    assertQuality(targetAt(1.5, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.MEDIUM, RejectionReason.NONE);
    assertQuality(targetAt(2.0, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.MEDIUM, RejectionReason.NONE);
    assertQuality(targetAt(2.5, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.HIGH, RejectionReason.NONE);
    assertQuality(targetAt(3.0, 0.0, 0.0), kStandardPolicy, Acceptance.ACCEPTED,
        UncertaintyClass.HIGH, RejectionReason.NONE);
    assertQuality(targetAt(3.1, 0.0, 0.0), kStandardPolicy, Acceptance.REJECTED,
        UncertaintyClass.UNUSABLE, RejectionReason.TARGET_TOO_FAR);
  }

  @Test
  void usesTheFullThreeDimensionalTranslationNorm() {
    TargetObservation target =
        new TargetObservation(
            7, new Transform3d(3.0, 4.0, 12.0, new Rotation3d(0.2, -0.3, 0.4)));

    assertQuality(
        target,
        new Policy(12.5, 13.0, 14.0),
        Acceptance.ACCEPTED,
        UncertaintyClass.MEDIUM,
        RejectionReason.NONE);
  }

  @Test
  void equalLowAndMediumThresholdsUseTheEarlierInclusiveLowBranch() {
    Policy policy = new Policy(1.0, 1.0, 3.0);

    assertQuality(targetAt(1.0, 0.0, 0.0), policy, Acceptance.ACCEPTED,
        UncertaintyClass.LOW, RejectionReason.NONE);
    assertQuality(targetAt(1.5, 0.0, 0.0), policy, Acceptance.ACCEPTED,
        UncertaintyClass.HIGH, RejectionReason.NONE);
  }

  @Test
  void equalMediumAndMaximumThresholdsUseTheEarlierInclusiveMediumBranch() {
    Policy policy = new Policy(1.0, 3.0, 3.0);

    assertQuality(targetAt(3.0, 0.0, 0.0), policy, Acceptance.ACCEPTED,
        UncertaintyClass.MEDIUM, RejectionReason.NONE);
    assertQuality(targetAt(3.1, 0.0, 0.0), policy, Acceptance.REJECTED,
        UncertaintyClass.UNUSABLE, RejectionReason.TARGET_TOO_FAR);
  }

  @Test
  void allEqualThresholdsUseLowAtTheBoundaryAndRejectAboveIt() {
    Policy policy = new Policy(1.0, 1.0, 1.0);

    assertQuality(targetAt(1.0, 0.0, 0.0), policy, Acceptance.ACCEPTED,
        UncertaintyClass.LOW, RejectionReason.NONE);
    assertQuality(targetAt(Math.nextUp(1.0), 0.0, 0.0), policy, Acceptance.REJECTED,
        UncertaintyClass.UNUSABLE, RejectionReason.TARGET_TOO_FAR);
  }

  @Test
  void policyRejectsNegativeThresholds() {
    assertThrows(IllegalArgumentException.class, () -> new Policy(-0.1, 1.0, 2.0));
    assertThrows(IllegalArgumentException.class, () -> new Policy(0.0, -0.1, 2.0));
    assertThrows(IllegalArgumentException.class, () -> new Policy(0.0, 1.0, -0.1));
  }

  @Test
  void policyRejectsEveryNonfiniteThresholdPosition() {
    for (double nonfinite :
        List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(IllegalArgumentException.class, () -> new Policy(nonfinite, 1.0, 2.0));
      assertThrows(IllegalArgumentException.class, () -> new Policy(0.0, nonfinite, 2.0));
      assertThrows(IllegalArgumentException.class, () -> new Policy(0.0, 1.0, nonfinite));
    }
  }

  @Test
  void policyRejectsDecreasingThresholds() {
    assertThrows(IllegalArgumentException.class, () -> new Policy(2.0, 1.0, 3.0));
    assertThrows(IllegalArgumentException.class, () -> new Policy(1.0, 3.0, 2.0));
  }

  @Test
  void nullRequiredArgumentsAreProgrammingErrors() {
    assertThrows(
        NullPointerException.class,
        () -> VisionMeasurementQualityEvaluator.evaluate(null, kStandardPolicy));
    assertThrows(
        NullPointerException.class,
        () -> VisionMeasurementQualityEvaluator.evaluate(targetAt(1.0, 0.0, 0.0), null));
  }

  @Test
  void targetObservationRejectsNonfiniteGeometryBeforeEvaluation() {
    assertThrows(
        IllegalArgumentException.class,
        () -> targetAt(Double.NaN, 0.0, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> targetAt(0.0, Double.POSITIVE_INFINITY, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> targetAt(0.0, 0.0, Double.NEGATIVE_INFINITY));
  }

  @Test
  void evaluatorRejectsANonfiniteNormProducedFromFiniteComponents() {
    TargetObservation overflowTarget =
        targetAt(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

    assertThrows(
        IllegalArgumentException.class,
        () -> VisionMeasurementQualityEvaluator.evaluate(overflowTarget, kStandardPolicy));
  }

  @Test
  void qualityRecordAcceptsExactlyTheFourLockedStates() {
    assertEquals(
        new VisionMeasurementQuality(
            Acceptance.ACCEPTED, UncertaintyClass.LOW, RejectionReason.NONE),
        new VisionMeasurementQuality(
            Acceptance.ACCEPTED, UncertaintyClass.LOW, RejectionReason.NONE));
    new VisionMeasurementQuality(
        Acceptance.ACCEPTED, UncertaintyClass.MEDIUM, RejectionReason.NONE);
    new VisionMeasurementQuality(
        Acceptance.ACCEPTED, UncertaintyClass.HIGH, RejectionReason.NONE);
    new VisionMeasurementQuality(
        Acceptance.REJECTED,
        UncertaintyClass.UNUSABLE,
        RejectionReason.TARGET_TOO_FAR);
  }

  @Test
  void qualityRecordRejectsEveryOtherNonNullEnumTuple() {
    for (Acceptance acceptance : Acceptance.values()) {
      for (UncertaintyClass uncertaintyClass : UncertaintyClass.values()) {
        for (RejectionReason rejectionReason : RejectionReason.values()) {
          if (!isLockedValidTuple(acceptance, uncertaintyClass, rejectionReason)) {
            assertThrows(
                IllegalArgumentException.class,
                () ->
                    new VisionMeasurementQuality(
                        acceptance, uncertaintyClass, rejectionReason));
          }
        }
      }
    }
  }

  @Test
  void qualityRecordRejectsNullEnumValues() {
    assertThrows(
        NullPointerException.class,
        () ->
            new VisionMeasurementQuality(
                null, UncertaintyClass.LOW, RejectionReason.NONE));
    assertThrows(
        NullPointerException.class,
        () ->
            new VisionMeasurementQuality(
                Acceptance.ACCEPTED, null, RejectionReason.NONE));
    assertThrows(
        NullPointerException.class,
        () ->
            new VisionMeasurementQuality(
                Acceptance.ACCEPTED, UncertaintyClass.LOW, null));
  }

  @Test
  void repeatedEvaluationIsDeterministicAndDoesNotMutateTheTarget() {
    TargetObservation target =
        new TargetObservation(
            4, new Transform3d(0.6, -0.8, 0.0, new Rotation3d(0.1, -0.2, 0.3)));
    Transform3d before = target.cameraToTarget();

    VisionMeasurementQuality first =
        VisionMeasurementQualityEvaluator.evaluate(target, kStandardPolicy);
    VisionMeasurementQuality second =
        VisionMeasurementQualityEvaluator.evaluate(target, kStandardPolicy);

    assertEquals(first, second);
    assertEquals(before, target.cameraToTarget());
    assertEquals(4, target.tagId());
  }

  @Test
  void publicTypeShapesMatchTheLockedContract() {
    assertTrue(Modifier.isFinal(VisionMeasurementQualityEvaluator.class.getModifiers()));
    Constructor<?>[] constructors = VisionMeasurementQualityEvaluator.class.getDeclaredConstructors();
    assertEquals(1, constructors.length);
    assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
    assertArrayEquals(
        new String[] {"acceptance", "uncertaintyClass", "rejectionReason"},
        Arrays.stream(VisionMeasurementQuality.class.getRecordComponents())
            .map(component -> component.getName())
            .toArray(String[]::new));
    assertArrayEquals(
        new String[] {"lowMaxMeters", "mediumMaxMeters", "maximumAcceptedMeters"},
        Arrays.stream(Policy.class.getRecordComponents())
            .map(component -> component.getName())
            .toArray(String[]::new));
  }

  private static TargetObservation targetAt(double xMeters, double yMeters, double zMeters) {
    return new TargetObservation(
        1, new Transform3d(xMeters, yMeters, zMeters, Rotation3d.kZero));
  }

  private static void assertQuality(
      TargetObservation target,
      Policy policy,
      Acceptance acceptance,
      UncertaintyClass uncertaintyClass,
      RejectionReason rejectionReason) {
    assertEquals(
        new VisionMeasurementQuality(acceptance, uncertaintyClass, rejectionReason),
        VisionMeasurementQualityEvaluator.evaluate(target, policy));
  }

  private static boolean isLockedValidTuple(
      Acceptance acceptance,
      UncertaintyClass uncertaintyClass,
      RejectionReason rejectionReason) {
    return (acceptance == Acceptance.ACCEPTED
            && uncertaintyClass != UncertaintyClass.UNUSABLE
            && rejectionReason == RejectionReason.NONE)
        || (acceptance == Acceptance.REJECTED
            && uncertaintyClass == UncertaintyClass.UNUSABLE
            && rejectionReason == RejectionReason.TARGET_TOO_FAR);
  }
}
