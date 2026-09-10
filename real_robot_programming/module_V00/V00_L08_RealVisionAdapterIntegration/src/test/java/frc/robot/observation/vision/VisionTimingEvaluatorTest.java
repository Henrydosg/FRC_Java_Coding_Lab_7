// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.observation.vision.VisionTimingEvaluator.Freshness;
import frc.robot.observation.vision.VisionTimingEvaluator.Ordering;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies pure deterministic V00_L07 freshness and ordering semantics. */
class VisionTimingEvaluatorTest {
  private static final VisionTiming kMeasurement = new VisionTiming(20.400, 0.375);

  @Test
  void ageBelowTheLimitIsFresh() {
    assertEquals(
        Freshness.FRESH,
        VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.200, 0.250));
  }

  @Test
  void ageEqualToTheLimitIsFresh() {
    assertEquals(
        Freshness.FRESH,
        VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.275, 0.250));
  }

  @Test
  void ageAboveTheLimitIsStale() {
    assertEquals(
        Freshness.STALE,
        VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.400, 0.250));
  }

  @Test
  void zeroAgeMeasurementIsFresh() {
    VisionTiming timing = new VisionTiming(20.0, 0.0);

    assertEquals(Freshness.FRESH, VisionTimingEvaluator.classifyFreshness(timing, 20.0, 0.0));
  }

  @Test
  void zeroMaximumAgeAcceptsOnlyZeroAge() {
    assertEquals(Freshness.FRESH, VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.025, 0.0));
    assertEquals(Freshness.STALE, VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.026, 0.0));
  }

  @Test
  void negativeFreshnessPolicyIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.400, -0.001));
  }

  @Test
  void nonfiniteFreshnessPolicyIsRejected() {
    for (double nonfinite : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () -> VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.400, nonfinite));
    }
  }

  @Test
  void nonfiniteReferenceTimestampIsRejected() {
    for (double nonfinite : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () -> VisionTimingEvaluator.classifyFreshness(kMeasurement, nonfinite, 0.250));
    }
  }

  @Test
  void referenceTimestampEarlierThanMeasurementIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.024, 0.250));
  }

  @Test
  void greaterMeasurementTimestampIsNewer() {
    VisionTiming newer = new VisionTiming(21.0, 0.0);
    VisionTiming previous = new VisionTiming(20.0, 0.0);

    assertEquals(Ordering.NEWER, VisionTimingEvaluator.classifyOrdering(newer, previous));
  }

  @Test
  void equalMeasurementTimestampIsDuplicate() {
    VisionTiming current = new VisionTiming(20.400, 0.375);
    VisionTiming previous = new VisionTiming(20.200, 0.175);

    assertEquals(Ordering.DUPLICATE, VisionTimingEvaluator.classifyOrdering(current, previous));
  }

  @Test
  void smallerMeasurementTimestampIsOutOfOrder() {
    VisionTiming current = new VisionTiming(20.0, 0.0);
    VisionTiming previous = new VisionTiming(20.0, 0.0);
    VisionTiming later = new VisionTiming(21.0, 0.0);

    assertEquals(Ordering.OUT_OF_ORDER, VisionTimingEvaluator.classifyOrdering(current, later));
    assertEquals(Ordering.NEWER, VisionTimingEvaluator.classifyOrdering(later, current));
    assertEquals(Ordering.DUPLICATE, VisionTimingEvaluator.classifyOrdering(current, previous));
  }

  @Test
  void repeatedClassificationIsDeterministic() {
    VisionTiming newer = new VisionTiming(21.0, 0.0);
    VisionTiming previous = new VisionTiming(20.0, 0.0);

    assertEquals(
        VisionTimingEvaluator.classifyOrdering(newer, previous),
        VisionTimingEvaluator.classifyOrdering(newer, previous));
    assertEquals(
        VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.400, 0.250),
        VisionTimingEvaluator.classifyFreshness(kMeasurement, 20.400, 0.250));
  }

  @Test
  void nullTimingArgumentsAreProgrammingErrors() {
    assertThrows(
        NullPointerException.class,
        () -> VisionTimingEvaluator.classifyFreshness(null, 20.400, 0.250));
    assertThrows(
        NullPointerException.class,
        () -> VisionTimingEvaluator.classifyOrdering(null, kMeasurement));
    assertThrows(
        NullPointerException.class,
        () -> VisionTimingEvaluator.classifyOrdering(kMeasurement, null));
  }

  @Test
  void evaluatorIsAStatelessNonInstantiableUtility() {
    assertTrue(Modifier.isFinal(VisionTimingEvaluator.class.getModifiers()));
    assertEquals(1, VisionTimingEvaluator.class.getDeclaredConstructors().length);
    Constructor<?> constructor = VisionTimingEvaluator.class.getDeclaredConstructors()[0];
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
  }
}
