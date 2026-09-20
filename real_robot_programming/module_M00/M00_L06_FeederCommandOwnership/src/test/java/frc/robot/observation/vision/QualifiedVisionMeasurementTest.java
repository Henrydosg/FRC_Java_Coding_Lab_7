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

import edu.wpi.first.math.geometry.Pose2d;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

/** Verifies the immutable vendor-neutral L09 qualification boundary. */
class QualifiedVisionMeasurementTest {
  @Test
  void hasOnlyTheLockedVendorNeutralComponents() {
    assertEquals(3, QualifiedVisionMeasurement.class.getRecordComponents().length);
    assertEquals(
        Pose2d.class, QualifiedVisionMeasurement.class.getRecordComponents()[0].getType());
    assertEquals(
        double.class, QualifiedVisionMeasurement.class.getRecordComponents()[1].getType());
    assertEquals(
        VisionMeasurementQuality.class,
        QualifiedVisionMeasurement.class.getRecordComponents()[2].getType());
    assertEquals(
        false,
        Arrays.stream(QualifiedVisionMeasurement.class.getDeclaredFields())
            .anyMatch(field -> field.getName().contains("fidx")
                || field.getName().contains("Limelight")
                || field.getName().contains("Network")));
    assertEquals(true, Modifier.isFinal(QualifiedVisionMeasurement.class.getModifiers()));
  }

  @Test
  void rejectsNullAndNonfiniteValues() {
    VisionMeasurementQuality quality =
        new VisionMeasurementQuality(
            VisionMeasurementQuality.Acceptance.ACCEPTED,
            VisionMeasurementQuality.UncertaintyClass.LOW,
            VisionMeasurementQuality.RejectionReason.NONE);
    assertThrows(
        NullPointerException.class, () -> new QualifiedVisionMeasurement(null, 1.0, quality));
    assertThrows(
        IllegalArgumentException.class,
        () -> new QualifiedVisionMeasurement(Pose2d.kZero, Double.NaN, quality));
  }
}
