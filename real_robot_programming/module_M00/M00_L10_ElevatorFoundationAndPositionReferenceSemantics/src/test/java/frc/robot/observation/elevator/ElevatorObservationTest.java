// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.elevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies immutable value semantics and reference prerequisites. */
class ElevatorObservationTest {
  @Test
  void equalValuesProduceEqualObservations() {
    ElevatorObservation first = new ElevatorObservation(true, true, true, false, -0.25);
    ElevatorObservation second = new ElevatorObservation(true, true, true, false, -0.25);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
    assertNotEquals(first, new ElevatorObservation(true, true, true, true, -0.25));
  }

  @Test
  void exactFiveComponentsRemainImmutable() {
    assertEquals(
        List.of("available", "connected", "positionValid", "positionReferenced", "positionMeters"),
        Arrays.stream(ElevatorObservation.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    for (var field : ElevatorObservation.class.getDeclaredFields()) {
      assertTrue(
          !Modifier.isPublic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()),
          field.getName());
    }
  }

  @Test
  void referenceRequiresAllMeasurementPrerequisites() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorObservation(false, true, false, false, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorObservation(true, true, false, true, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new ElevatorObservation(true, true, true, true, Double.NaN));
  }
}
