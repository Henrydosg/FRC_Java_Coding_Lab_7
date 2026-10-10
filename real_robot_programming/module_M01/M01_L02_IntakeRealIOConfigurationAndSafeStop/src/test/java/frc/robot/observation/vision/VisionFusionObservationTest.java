// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

/** Verifies immutable vendor-neutral L09 fusion evidence. */
class VisionFusionObservationTest {
  @Test
  void emptyObservationHasNoEventTimestamps() {
    VisionFusionObservation observation = VisionFusionObservation.empty();

    assertEquals(0, observation.qualifiedHandoffCount());
    assertTrue(Double.isNaN(observation.lastQualifiedHandoffTimestampSeconds()));
    assertEquals(0, observation.acceptedFusionCount());
    assertTrue(Double.isNaN(observation.lastAcceptedFusionTimestampSeconds()));
  }

  @Test
  void positiveCountsRequireFiniteEventTimestamps() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionFusionObservation(1, Double.NaN, 0, Double.NaN));
    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionFusionObservation(0, Double.NaN, 1, Double.POSITIVE_INFINITY));
    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionFusionObservation(0, 1.0, 0, Double.NaN));
  }

  @Test
  void recordIsFinalAndContainsNoRuntimeDependencies() {
    assertTrue(VisionFusionObservation.class.isRecord());
    assertTrue(Modifier.isFinal(VisionFusionObservation.class.getModifiers()));
    assertEquals(4, VisionFusionObservation.class.getRecordComponents().length);
  }
}
