// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies finite logical-meter bounds and immutable record semantics. */
class ElevatorTravelLimitsTest {
  @Test
  void acceptsPositiveNegativeAndZeroCrossingFiniteBounds() {
    assertEquals(new ElevatorTravelLimits(0.25, 2.0), new ElevatorTravelLimits(0.25, 2.0));
    assertEquals(-2.0, new ElevatorTravelLimits(-2.0, -0.25).minPositionMeters());
    assertEquals(1.0, new ElevatorTravelLimits(-1.0, 1.0).maxPositionMeters());
  }

  @Test
  void rejectsNonFiniteMinimumAndMaximum() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorTravelLimits(Double.NaN, 1.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorTravelLimits(0.0, Double.NaN));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorTravelLimits(Double.POSITIVE_INFINITY, 2.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorTravelLimits(Double.NEGATIVE_INFINITY, 2.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorTravelLimits(-2.0, Double.POSITIVE_INFINITY));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorTravelLimits(-2.0, Double.NEGATIVE_INFINITY));
  }

  @Test
  void rejectsEqualAndReversedBounds() {
    assertThrows(IllegalArgumentException.class, () -> new ElevatorTravelLimits(1.0, 1.0));
    assertThrows(IllegalArgumentException.class, () -> new ElevatorTravelLimits(2.0, 1.0));
  }

  @Test
  void retainsRecordComponentsAndUsesImmutableRecordSemantics() {
    ElevatorTravelLimits limits = new ElevatorTravelLimits(-0.75, 1.25);

    assertTrue(ElevatorTravelLimits.class.isRecord());
    assertTrue(Modifier.isFinal(ElevatorTravelLimits.class.getModifiers()));
    assertEquals(
        List.of("minPositionMeters", "maxPositionMeters"),
        Arrays.stream(ElevatorTravelLimits.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    assertEquals(-0.75, limits.minPositionMeters());
    assertEquals(1.25, limits.maxPositionMeters());
    assertEquals(limits, new ElevatorTravelLimits(-0.75, 1.25));
  }
}
