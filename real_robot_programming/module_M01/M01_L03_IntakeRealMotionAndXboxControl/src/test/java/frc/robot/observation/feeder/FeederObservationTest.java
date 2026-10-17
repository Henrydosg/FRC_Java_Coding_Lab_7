// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.feeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.observation.feeder.FeederObservation.RequestedState;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

/** Verifies immutable value semantics and explicit Feeder availability meaning. */
class FeederObservationTest {
  @Test
  void equalValuesProduceEqualObservations() {
    FeederObservation first =
        new FeederObservation(true, true, RequestedState.FEED_REQUESTED);
    FeederObservation second =
        new FeederObservation(true, true, RequestedState.FEED_REQUESTED);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
    assertNotEquals(
        first, new FeederObservation(true, false, RequestedState.FEED_REQUESTED));
  }

  @Test
  void connectedStateRequiresAvailabilityAndRequestedState() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new FeederObservation(false, true, RequestedState.STOPPED));
    assertThrows(
        NullPointerException.class,
        () -> new FeederObservation(false, false, null));
  }

  @Test
  void recordExposesNoWritablePublicState() {
    for (var field : FeederObservation.class.getDeclaredFields()) {
      assertTrue(
          !Modifier.isPublic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()),
          field.getName());
    }
  }

  @Test
  void exposesOnlyTheLockedComponentsAndRequestedStates() {
    assertEquals(3, FeederObservation.class.getRecordComponents().length);
    assertEquals("available", FeederObservation.class.getRecordComponents()[0].getName());
    assertEquals("connected", FeederObservation.class.getRecordComponents()[1].getName());
    assertEquals("requestedState", FeederObservation.class.getRecordComponents()[2].getName());
    assertEquals(2, RequestedState.values().length);
    assertEquals(RequestedState.STOPPED, RequestedState.values()[0]);
    assertEquals(RequestedState.FEED_REQUESTED, RequestedState.values()[1]);
  }
}
