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

import java.util.Optional;
import org.junit.jupiter.api.Test;

/** Verifies immutable pose meaning and explicit availability in Swerve observations. */
class SwerveObservationTest {

  @Test
  void representsUninitializedPoseAsExplicitlyUnavailable() {
    SwerveObservation observation = observation(Optional.empty());

    assertTrue(observation.currentPose().isEmpty());
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
  void rejectsNullAvailabilityAndNonfinitePoseValues() {
    assertThrows(NullPointerException.class, () -> observation(null));
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
  }

  private static SwerveObservation observation(
      Optional<SwerveObservation.PoseObservation> currentPose) {
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
    return new SwerveObservation(module, module, module, module, gyro, currentPose);
  }
}
