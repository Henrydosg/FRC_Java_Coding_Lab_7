// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import java.util.Objects;

/** Immutable provenance for one accepted autonomous starting-pose reset. */
public record AutonomousStartContext(
    FieldVariant fieldVariant, Alliance alliance, Pose2d executionStartPose) {
  /** Validates and snapshots one definite transformed execution start pose. */
  public AutonomousStartContext {
    Objects.requireNonNull(fieldVariant, "fieldVariant");
    Objects.requireNonNull(alliance, "alliance");
    Objects.requireNonNull(executionStartPose, "executionStartPose");
    if (!Double.isFinite(executionStartPose.getX())
        || !Double.isFinite(executionStartPose.getY())
        || !Double.isFinite(executionStartPose.getRotation().getRadians())) {
      throw new IllegalArgumentException("executionStartPose must be finite");
    }
    executionStartPose =
        new Pose2d(
            executionStartPose.getX(),
            executionStartPose.getY(),
            new Rotation2d(executionStartPose.getRotation().getRadians()));
  }
}
