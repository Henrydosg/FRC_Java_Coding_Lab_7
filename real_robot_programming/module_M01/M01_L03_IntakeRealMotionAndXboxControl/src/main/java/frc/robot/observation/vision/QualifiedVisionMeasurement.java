// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import edu.wpi.first.math.geometry.Pose2d;
import java.util.Objects;

/**
 * Immutable, vendor-neutral vision measurement ready for guarded estimator admission.
 *
 * @param fieldRobotPose canonical field-relative robot pose
 * @param measurementTimestampSeconds canonical measurement time on the robot timebase
 * @param quality inherited qualitative L06 quality decision
 */
public record QualifiedVisionMeasurement(
    Pose2d fieldRobotPose,
    double measurementTimestampSeconds,
    VisionMeasurementQuality quality) {
  /** Validates the immutable qualified-measurement boundary. */
  public QualifiedVisionMeasurement {
    fieldRobotPose = Objects.requireNonNull(fieldRobotPose, "fieldRobotPose");
    quality = Objects.requireNonNull(quality, "quality");
    if (!Double.isFinite(fieldRobotPose.getX())
        || !Double.isFinite(fieldRobotPose.getY())
        || !Double.isFinite(fieldRobotPose.getRotation().getRadians())) {
      throw new IllegalArgumentException("fieldRobotPose must be finite");
    }
    if (!Double.isFinite(measurementTimestampSeconds)) {
      throw new IllegalArgumentException("measurementTimestampSeconds must be finite");
    }
  }
}

