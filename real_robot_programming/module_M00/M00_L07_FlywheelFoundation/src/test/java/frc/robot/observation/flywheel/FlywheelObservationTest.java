// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.flywheel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.observation.flywheel.FlywheelObservation.RequestedState;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

/** Verifies immutable value semantics and explicit Flywheel velocity meaning. */
class FlywheelObservationTest {
  private static final double VALID_VELOCITY_RPM = 4200.0;
  private static final double DIFFERENT_VELOCITY_RPM = 3600.0;

  @Test
  void equalValuesProduceEqualObservations() {
    FlywheelObservation first =
        new FlywheelObservation(
            true, true, true, VALID_VELOCITY_RPM, RequestedState.SPIN_REQUESTED);
    FlywheelObservation second =
        new FlywheelObservation(
            true, true, true, VALID_VELOCITY_RPM, RequestedState.SPIN_REQUESTED);

    assertEquals(first, second);
    assertEquals(first.hashCode(), second.hashCode());
    assertNotEquals(
        first,
        new FlywheelObservation(
            true, true, true, DIFFERENT_VELOCITY_RPM, RequestedState.SPIN_REQUESTED));
  }

  @Test
  void availabilityConnectionAndRequestedStateAreRequired() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new FlywheelObservation(false, true, false, 0.0, RequestedState.STOPPED));
    assertThrows(
        NullPointerException.class,
        () -> new FlywheelObservation(false, false, false, 0.0, null));
  }

  @Test
  void validVelocityRequiresAvailableConnectedAndFiniteMeasurement() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new FlywheelObservation(false, false, true, VALID_VELOCITY_RPM, RequestedState.STOPPED));
    assertThrows(
        IllegalArgumentException.class,
        () -> new FlywheelObservation(true, false, true, VALID_VELOCITY_RPM, RequestedState.STOPPED));
    assertThrows(
        IllegalArgumentException.class,
        () -> new FlywheelObservation(true, true, true, Double.NaN, RequestedState.STOPPED));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new FlywheelObservation(
                true, true, true, Double.POSITIVE_INFINITY, RequestedState.STOPPED));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new FlywheelObservation(
                true, true, true, Double.NEGATIVE_INFINITY, RequestedState.STOPPED));
  }

  @Test
  void invalidVelocityUsesCanonicalZeroAndRemainsDistinguishableByItsFlag() {
    FlywheelObservation invalid =
        new FlywheelObservation(false, false, false, 0.0, RequestedState.STOPPED);
    FlywheelObservation validZero =
        new FlywheelObservation(true, true, true, 0.0, RequestedState.STOPPED);

    assertEquals(0.0, invalid.velocityRpm());
    assertFalse(invalid.velocityValid());
    assertTrue(validZero.velocityValid());
    assertNotEquals(invalid, validZero);
    assertThrows(
        IllegalArgumentException.class,
        () -> new FlywheelObservation(false, false, false, -0.0, RequestedState.STOPPED));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new FlywheelObservation(
                false, false, false, VALID_VELOCITY_RPM, RequestedState.STOPPED));
  }

  @Test
  void recordExposesNoWritablePublicState() {
    for (var field : FlywheelObservation.class.getDeclaredFields()) {
      assertTrue(
          !Modifier.isPublic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()),
          field.getName());
    }
  }

  @Test
  void exposesOnlyTheLockedComponentsAndRequestedStates() {
    assertEquals(5, FlywheelObservation.class.getRecordComponents().length);
    assertEquals("available", FlywheelObservation.class.getRecordComponents()[0].getName());
    assertEquals("connected", FlywheelObservation.class.getRecordComponents()[1].getName());
    assertEquals("velocityValid", FlywheelObservation.class.getRecordComponents()[2].getName());
    assertEquals("velocityRpm", FlywheelObservation.class.getRecordComponents()[3].getName());
    assertEquals("requestedState", FlywheelObservation.class.getRecordComponents()[4].getName());
    assertEquals(2, RequestedState.values().length);
    assertEquals(RequestedState.STOPPED, RequestedState.values()[0]);
    assertEquals(RequestedState.SPIN_REQUESTED, RequestedState.values()[1]);
  }
}
