// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.intake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;

/** Verifies immutable value semantics and explicit Intake availability and measurement meaning. */
class IntakeObservationTest {
  private static final double kTimestampSeconds = 42.5;
  private static final double kPositionRotations = 12.5;
  private static final double kVelocityRotationsPerSecond = -3.25;
  private static final double kStatorCurrentAmps = 7.5;
  private static final double kSupplyCurrentAmps = 2.25;
  private static final double kAppliedVolts = 0.125;

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
  void threeArgumentConstructorCreatesAnExplicitlyInvalidNaNMeasurement() {
    IntakeObservation observation =
        new IntakeObservation(true, true, RequestedState.STOPPED);

    assertFalse(observation.measurementValid());
    assertTrue(Double.isNaN(observation.timestampSeconds()));
    assertTrue(Double.isNaN(observation.motorPositionRotations()));
    assertTrue(Double.isNaN(observation.motorVelocityRotationsPerSecond()));
    assertTrue(Double.isNaN(observation.statorCurrentAmps()));
    assertTrue(Double.isNaN(observation.supplyCurrentAmps()));
    assertTrue(Double.isNaN(observation.appliedVolts()));
    assertEquals(observation, new IntakeObservation(true, true, RequestedState.STOPPED));
  }

  @Test
  void validMeasurementExposesFiniteValuesIncludingPhysicalZero() {
    IntakeObservation measured = validMeasurement(kVelocityRotationsPerSecond);
    IntakeObservation zero = validMeasurement(0.0);

    assertTrue(measured.measurementValid());
    assertEquals(kTimestampSeconds, measured.timestampSeconds());
    assertEquals(kPositionRotations, measured.motorPositionRotations());
    assertEquals(kVelocityRotationsPerSecond, measured.motorVelocityRotationsPerSecond());
    assertEquals(kStatorCurrentAmps, measured.statorCurrentAmps());
    assertEquals(kSupplyCurrentAmps, measured.supplyCurrentAmps());
    assertEquals(kAppliedVolts, measured.appliedVolts());
    assertTrue(zero.measurementValid());
    assertEquals(0.0, zero.motorVelocityRotationsPerSecond());
    assertNotEquals(zero, new IntakeObservation(true, true, RequestedState.STOPPED));
  }

  @Test
  void validMeasurementRequiresConnectedSourceAndFiniteValues() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new IntakeObservation(
                true,
                false,
                RequestedState.STOPPED,
                false,
                true,
                kTimestampSeconds,
                kPositionRotations,
                kVelocityRotationsPerSecond,
                kStatorCurrentAmps,
                kSupplyCurrentAmps,
                kAppliedVolts));
    assertThrows(
        IllegalArgumentException.class, () -> validMeasurement(Double.NaN));
    assertThrows(
        IllegalArgumentException.class, () -> validMeasurement(Double.POSITIVE_INFINITY));
  }

  @Test
  void invalidMeasurementRejectsAnyNonNaNValueIncludingZero() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new IntakeObservation(
                true,
                true,
                RequestedState.STOPPED,
                false,
                false,
                Double.NaN,
                Double.NaN,
                0.0,
                Double.NaN,
                Double.NaN,
                Double.NaN));
  }

  @Test
  void validMeasurementRequiresAFiniteTimestamp() {
    double[] nonFiniteTimestamps = {
      Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
    };
    for (double timestampSeconds : nonFiniteTimestamps) {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              new IntakeObservation(
                  true,
                  true,
                  RequestedState.STOPPED,
                  false,
                  true,
                  timestampSeconds,
                  kPositionRotations,
                  kVelocityRotationsPerSecond,
                  kStatorCurrentAmps,
                  kSupplyCurrentAmps,
                  kAppliedVolts));
    }
  }

  @Test
  void invalidMeasurementRejectsANonNaNTimestamp() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new IntakeObservation(
                true,
                true,
                RequestedState.STOPPED,
                false,
                false,
                kTimestampSeconds,
                Double.NaN,
                Double.NaN,
                Double.NaN,
                Double.NaN,
                Double.NaN));
  }

  @Test
  void faultLatchIsExplicitAndDefaultsToFalse() {
    IntakeObservation unlatched = new IntakeObservation(true, true, RequestedState.STOPPED);
    IntakeObservation latched =
        new IntakeObservation(
            true,
            true,
            RequestedState.STOPPED,
            true,
            false,
            Double.NaN,
            Double.NaN,
            Double.NaN,
            Double.NaN,
            Double.NaN,
            Double.NaN);

    assertFalse(unlatched.faultLatched());
    assertTrue(latched.faultLatched());
    assertNotEquals(unlatched, latched);
  }

  @Test
  void recordExposesNoWritablePublicState() {
    for (var field : IntakeObservation.class.getDeclaredFields()) {
      assertTrue(
          !Modifier.isPublic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()),
          field.getName());
    }
  }

  private static IntakeObservation validMeasurement(double velocityRotationsPerSecond) {
    return new IntakeObservation(
        true,
        true,
        RequestedState.STOPPED,
        false,
        true,
        kTimestampSeconds,
        kPositionRotations,
        velocityRotationsPerSecond,
        kStatorCurrentAmps,
        kSupplyCurrentAmps,
        kAppliedVolts);
  }
}
