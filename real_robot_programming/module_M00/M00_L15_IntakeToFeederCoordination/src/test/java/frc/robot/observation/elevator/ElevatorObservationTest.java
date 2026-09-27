// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.elevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies immutable value semantics and request/error invariants. */
class ElevatorObservationTest {
  @Test
  void equalValuesProduceEqualObservations() {
    ElevatorObservation first =
        new ElevatorObservation(
            true, true, true, true, 1.25, ElevatorRequestedState.POSITION_REQUESTED, 2.0, 0.75);
    ElevatorObservation second =
        new ElevatorObservation(
            true, true, true, true, 1.25, ElevatorRequestedState.POSITION_REQUESTED, 2.0, 0.75);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
    assertNotEquals(
        first,
        new ElevatorObservation(
            true, true, true, true, 1.50, ElevatorRequestedState.POSITION_REQUESTED, 2.0, 0.5));
  }

  @Test
  void exactEightComponentsRemainImmutable() {
    assertEquals(
        List.of(
            "available",
            "connected",
            "positionValid",
            "positionReferenced",
            "positionMeters",
            "requestedState",
            "targetPositionMeters",
            "positionErrorMeters"),
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
  void preservesMeasurementPrerequisitesAndCanonicalPlaceholders() {
    ElevatorObservation stopped =
        new ElevatorObservation(
            false, false, false, false, 0.0, ElevatorRequestedState.STOPPED, 0.0, 0.0);
    assertEquals(0.0, stopped.targetPositionMeters());
    assertEquals(0.0, stopped.positionErrorMeters());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            new ElevatorObservation(
                false, true, false, false, 0.0, ElevatorRequestedState.STOPPED, 0.0, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new ElevatorObservation(
                true, true, false, true, 0.0, ElevatorRequestedState.STOPPED, 0.0, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new ElevatorObservation(
                true, true, true, true, Double.NaN, ElevatorRequestedState.STOPPED, 0.0, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new ElevatorObservation(
                true, true, true, true, 1.0, ElevatorRequestedState.STOPPED, 1.0, 0.0));
  }

  @Test
  void inactiveErrorIsCanonicalWhenMeasurementCannotBeTrusted() {
    assertEquals(
        0.0,
        new ElevatorObservation(
                true, true, true, false, 1.25, ElevatorRequestedState.POSITION_REQUESTED, 2.0, 0.0)
            .positionErrorMeters());
    assertEquals(
        0.0,
        new ElevatorObservation(
                false, false, false, false, 0.0, ElevatorRequestedState.POSITION_REQUESTED, 2.0, 0.0)
            .positionErrorMeters());
  }

  @Test
  void homingUsesInactiveTargetAndErrorWithoutClaimingReference() {
    ElevatorObservation observation =
        new ElevatorObservation(
            true, true, false, false, 0.0, ElevatorRequestedState.HOMING, 0.0, 0.0);

    assertEquals(ElevatorRequestedState.HOMING, observation.requestedState());
    assertEquals(0.0, observation.targetPositionMeters());
    assertEquals(0.0, observation.positionErrorMeters());
    assertTrue(observation.available());
    assertTrue(observation.connected());
    assertFalse(observation.positionReferenced());
  }
}
