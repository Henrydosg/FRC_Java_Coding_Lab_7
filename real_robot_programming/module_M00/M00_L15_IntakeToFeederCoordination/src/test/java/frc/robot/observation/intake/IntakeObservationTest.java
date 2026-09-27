// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.intake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

/** Verifies immutable value semantics and explicit Intake availability meaning. */
class IntakeObservationTest {
  @Test
  void equalValuesProduceEqualObservations() {
    IntakeObservation first =
        new IntakeObservation(true, true, RequestedState.INTAKE_REQUESTED);
    IntakeObservation second =
        new IntakeObservation(true, true, RequestedState.INTAKE_REQUESTED);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
    assertNotEquals(
        first, new IntakeObservation(true, false, RequestedState.INTAKE_REQUESTED));
  }

  @Test
  void connectedStateRequiresAvailability() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new IntakeObservation(false, true, RequestedState.STOPPED));
    assertThrows(
        NullPointerException.class,
        () -> new IntakeObservation(false, false, null));
  }

  @Test
  void recordExposesNoWritablePublicState() {
    for (var field : IntakeObservation.class.getDeclaredFields()) {
      assertTrue(
          !Modifier.isPublic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()),
          field.getName());
    }
  }
}
