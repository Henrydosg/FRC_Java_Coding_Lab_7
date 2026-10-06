// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies the immutable vendor-neutral V00_L07 timing value object. */
class VisionTimingTest {
  private static final double kTolerance = 1.0e-12;

  @Test
  void zeroLatencyUsesTheReceiveTimestampAsMeasurementTimestamp() {
    VisionTiming timing = new VisionTiming(20.0, 0.0);

    assertEquals(20.0, timing.measurementTimestampSeconds(), kTolerance);
  }

  @Test
  void positiveLatencyDerivesTheMeasurementTimestampInSeconds() {
    VisionTiming timing = new VisionTiming(20.400, 0.375);

    assertEquals(20.025, timing.measurementTimestampSeconds(), kTolerance);
  }

  @Test
  void finiteReceiveTimestampsAreAccepted() {
    assertDoesNotThrow(() -> new VisionTiming(-10.0, 0.0));
    assertDoesNotThrow(() -> new VisionTiming(Double.MAX_VALUE, 0.0));
  }

  @Test
  void everyNonfiniteReceiveTimestampIsRejected() {
    for (double nonfinite : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(IllegalArgumentException.class, () -> new VisionTiming(nonfinite, 0.0));
    }
  }

  @Test
  void everyNonfiniteLatencyIsRejected() {
    for (double nonfinite : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(IllegalArgumentException.class, () -> new VisionTiming(20.0, nonfinite));
    }
  }

  @Test
  void negativeLatencyIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new VisionTiming(20.0, -0.001));
  }

  @Test
  void nonfiniteDerivedMeasurementTimestampIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionTiming(-Double.MAX_VALUE, Double.MAX_VALUE));
  }

  @Test
  void repeatedMeasurementTimestampEvaluationIsDeterministic() {
    VisionTiming timing = new VisionTiming(20.400, 0.375);

    assertEquals(
        timing.measurementTimestampSeconds(), timing.measurementTimestampSeconds(), kTolerance);
  }

  @Test
  void recordStoresOnlyTheTwoIndependentTimingFacts() {
    assertTrue(Modifier.isFinal(VisionTiming.class.getModifiers()));
    assertEquals(
        List.of("receiveTimestampSeconds", "totalLatencySeconds"),
        Arrays.stream(VisionTiming.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    assertEquals(2, VisionTiming.class.getRecordComponents().length);
  }
}
