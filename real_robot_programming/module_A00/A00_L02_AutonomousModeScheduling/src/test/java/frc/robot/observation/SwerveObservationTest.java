// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Verifies immutable pose meaning and explicit availability in Swerve observations. */
class SwerveObservationTest {

  @Test
  void representsUninitializedPoseAsExplicitlyUnavailable() {
    SwerveObservation observation = observation(Optional.empty());

    assertTrue(observation.currentPose().isEmpty());
    assertTrue(observation.estimatedPose().isEmpty());
  }

  @Test
  void preservesFinitePoseUnitsAndCurrentSampleValidityAsImmutableValues() {
    SwerveObservation.PoseObservation pose =
        new SwerveObservation.PoseObservation(1.25, -0.5, Math.PI / 3.0, false);
    SwerveObservation observation = observation(Optional.of(pose));

    SwerveObservation.PoseObservation observedPose =
        observation.currentPose().orElseThrow();
    assertEquals(1.25, observedPose.xMeters());
    assertEquals(-0.5, observedPose.yMeters());
    assertEquals(Math.PI / 3.0, observedPose.headingRadians());
    assertFalse(observedPose.measurementSampleValid());
    assertEquals(pose, observedPose);
  }

  @Test
  void preservesFiniteEstimatedPoseUnitsAndIndependentValidity() {
    SwerveObservation.PoseObservation currentPose =
        new SwerveObservation.PoseObservation(1.25, -0.5, Math.PI / 3.0, true);
    SwerveObservation.EstimatedPoseObservation estimatedPose =
        new SwerveObservation.EstimatedPoseObservation(-2.0, 0.75, -Math.PI / 4.0, false);
    SwerveObservation observation =
        observation(Optional.of(currentPose), Optional.of(estimatedPose));

    assertEquals(currentPose, observation.currentPose().orElseThrow());
    assertEquals(estimatedPose, observation.estimatedPose().orElseThrow());
    assertEquals(-2.0, observation.estimatedPose().orElseThrow().xMeters());
    assertEquals(0.75, observation.estimatedPose().orElseThrow().yMeters());
    assertEquals(-Math.PI / 4.0, observation.estimatedPose().orElseThrow().headingRadians());
    assertFalse(observation.estimatedPose().orElseThrow().measurementSampleValid());
  }

  @Test
  void rejectsNullAvailabilityAndNonfinitePoseValues() {
    assertThrows(NullPointerException.class, () -> observation(null));
    assertThrows(
        NullPointerException.class,
        () -> observation(Optional.empty(), null));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new SwerveObservation.PoseObservation(
                Double.NaN, 0.0, 0.0, true));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new SwerveObservation.PoseObservation(
                0.0, Double.POSITIVE_INFINITY, 0.0, true));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new SwerveObservation.PoseObservation(
                0.0, 0.0, Double.NEGATIVE_INFINITY, true));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new SwerveObservation.EstimatedPoseObservation(
                Double.NaN, 0.0, 0.0, true));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new SwerveObservation.EstimatedPoseObservation(
                0.0, Double.POSITIVE_INFINITY, 0.0, true));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new SwerveObservation.EstimatedPoseObservation(
                0.0, 0.0, Double.NEGATIVE_INFINITY, true));
  }

  @Test
  void estimatedPoseIsPrimitiveOnlyAndHasNoTimestampField() {
    String[] components =
        Arrays.stream(SwerveObservation.EstimatedPoseObservation.class.getRecordComponents())
            .map(component -> component.getName())
            .toArray(String[]::new);

    assertEquals(4, components.length);
    assertEquals("xMeters", components[0]);
    assertEquals("yMeters", components[1]);
    assertEquals("headingRadians", components[2]);
    assertEquals("measurementSampleValid", components[3]);
  }

  private static SwerveObservation observation(
      Optional<SwerveObservation.PoseObservation> currentPose) {
    return observation(currentPose, Optional.empty());
  }

  private static SwerveObservation observation(
      Optional<SwerveObservation.PoseObservation> currentPose,
      Optional<SwerveObservation.EstimatedPoseObservation> estimatedPose) {
    SwerveObservation.ModuleObservation module =
        new SwerveObservation.ModuleObservation(
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            true,
            true,
            true,
            true,
            true,
            true);
    SwerveObservation.GyroObservation gyro =
        new SwerveObservation.GyroObservation(
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true, true);
    return new SwerveObservation(
        module, module, module, module, gyro, currentPose, estimatedPose);
  }
}
