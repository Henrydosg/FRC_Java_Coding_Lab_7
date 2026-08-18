// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Pure transformations from canonical Blue-origin field data to one explicit alliance frame.
 *
 * <p>The caller owns alliance resolution and must apply this contract exactly once. This utility
 * deliberately does not read Driver Station state or choose a default field variant.
 */
public final class FieldAllianceTransform {
  private FieldAllianceTransform() {}

  /**
   * Transforms a canonical Blue-origin pose for the requested alliance.
   *
   * <p>Blue preserves geometry. Red rotates the pose 180 degrees about the selected field centre.
   *
   * @param canonicalBluePose finite pose expressed in the canonical Blue-origin field frame
   * @param fieldVariant explicit official 2026 field construction variant
   * @param alliance definite target alliance
   * @return a fresh transformed pose
   * @throws NullPointerException if an object argument is null
   * @throws IllegalArgumentException if the pose is nonfinite
   */
  public static Pose2d fromCanonicalBluePose(
      Pose2d canonicalBluePose, FieldVariant fieldVariant, Alliance alliance) {
    requireFinitePose(canonicalBluePose);
    requireFieldVariant(fieldVariant);
    requireAlliance(alliance);

    if (alliance == Alliance.Blue) {
      return copyPose(canonicalBluePose);
    }

    return new Pose2d(
        fieldVariant.fieldLengthMeters() - canonicalBluePose.getX(),
        fieldVariant.fieldWidthMeters() - canonicalBluePose.getY(),
        fromCanonicalBlueHeading(canonicalBluePose.getRotation(), fieldVariant, alliance));
  }

  /**
   * Transforms a canonical Blue-origin heading for the requested alliance.
   *
   * @param canonicalBlueHeading finite rotation expressed in the canonical Blue-origin field frame
   * @param fieldVariant explicit official 2026 field construction variant
   * @param alliance definite target alliance
   * @return a fresh transformed heading
   * @throws NullPointerException if an object argument is null
   * @throws IllegalArgumentException if the heading is nonfinite
   */
  public static Rotation2d fromCanonicalBlueHeading(
      Rotation2d canonicalBlueHeading, FieldVariant fieldVariant, Alliance alliance) {
    requireFiniteRotation(canonicalBlueHeading);
    requireFieldVariant(fieldVariant);
    requireAlliance(alliance);

    if (alliance == Alliance.Blue) {
      return new Rotation2d(canonicalBlueHeading.getRadians());
    }

    return canonicalBlueHeading.plus(Rotation2d.kPi);
  }

  /**
   * Transforms a canonical Blue-origin field-relative translation or velocity vector.
   *
   * @param canonicalBlueFieldVelocity finite field-relative vector in the canonical Blue frame
   * @param fieldVariant explicit official 2026 field construction variant
   * @param alliance definite target alliance
   * @return a fresh transformed vector
   * @throws NullPointerException if an object argument is null
   * @throws IllegalArgumentException if the vector is nonfinite
   */
  public static Translation2d fromCanonicalBlueFieldVelocity(
      Translation2d canonicalBlueFieldVelocity, FieldVariant fieldVariant, Alliance alliance) {
    requireFiniteTranslation(canonicalBlueFieldVelocity);
    requireFieldVariant(fieldVariant);
    requireAlliance(alliance);

    if (alliance == Alliance.Blue) {
      return copyTranslation(canonicalBlueFieldVelocity);
    }

    return new Translation2d(
        -canonicalBlueFieldVelocity.getX(), -canonicalBlueFieldVelocity.getY());
  }

  /**
   * Transforms a canonical Blue-origin angular velocity for the requested alliance.
   *
   * <p>A 180-degree planar rotation preserves the signed scalar angular velocity.
   *
   * @param canonicalBlueAngularVelocity finite counterclockwise-positive angular velocity
   * @param fieldVariant explicit official 2026 field construction variant
   * @param alliance definite target alliance
   * @return the unchanged angular velocity
   * @throws NullPointerException if an object argument is null
   * @throws IllegalArgumentException if the angular velocity is nonfinite
   */
  public static double fromCanonicalBlueAngularVelocity(
      double canonicalBlueAngularVelocity, FieldVariant fieldVariant, Alliance alliance) {
    requireFinite(canonicalBlueAngularVelocity, "canonicalBlueAngularVelocity");
    requireFieldVariant(fieldVariant);
    requireAlliance(alliance);
    return canonicalBlueAngularVelocity;
  }

  /**
   * Transforms every pose in a canonical Blue-origin trajectory for the requested alliance.
   *
   * <p>The returned trajectory and all of its states are fresh copies. For Red, only
   * {@link Trajectory.State#poseMeters} geometry changes. Time, scalar velocity, scalar
   * acceleration, and curvature are preserved exactly. A state pose rotation remains the path
   * tangent/path geometry; it is not a holonomic robot-heading target.
   *
   * @param canonicalBlueTrajectory validated canonical Blue-origin trajectory
   * @param fieldVariant explicit official 2026 field construction variant
   * @param alliance definite target alliance
   * @return a fresh trajectory containing fresh transformed states
   * @throws NullPointerException if an object argument or a trajectory state is null
   * @throws IllegalArgumentException if the trajectory is unusable or contains nonfinite data
   */
  public static Trajectory fromCanonicalBlueTrajectory(
      Trajectory canonicalBlueTrajectory, FieldVariant fieldVariant, Alliance alliance) {
    Objects.requireNonNull(canonicalBlueTrajectory, "canonicalBlueTrajectory");
    requireFieldVariant(fieldVariant);
    requireAlliance(alliance);

    List<Trajectory.State> canonicalStates = canonicalBlueTrajectory.getStates();
    validateTrajectory(canonicalStates, canonicalBlueTrajectory.getTotalTimeSeconds());

    List<Trajectory.State> transformedStates = new ArrayList<>(canonicalStates.size());
    for (Trajectory.State canonicalState : canonicalStates) {
      transformedStates.add(
          new Trajectory.State(
              canonicalState.timeSeconds,
              canonicalState.velocityMetersPerSecond,
              canonicalState.accelerationMetersPerSecondSq,
              fromCanonicalBluePose(canonicalState.poseMeters, fieldVariant, alliance),
              canonicalState.curvatureRadPerMeter));
    }
    return new Trajectory(transformedStates);
  }

  private static void validateTrajectory(
      List<Trajectory.State> states, double totalTimeSeconds) {
    if (states == null || states.isEmpty()) {
      throw new IllegalArgumentException("Trajectory must contain states");
    }
    if (!Double.isFinite(totalTimeSeconds) || totalTimeSeconds <= 0.0) {
      throw new IllegalArgumentException("Trajectory must have finite positive duration");
    }

    double previousTimeSeconds = Double.NEGATIVE_INFINITY;
    for (Trajectory.State state : states) {
      Objects.requireNonNull(state, "trajectory state");
      requireFinite(state.timeSeconds, "trajectory state timeSeconds");
      requireFinite(state.velocityMetersPerSecond, "trajectory state velocityMetersPerSecond");
      requireFinite(
          state.accelerationMetersPerSecondSq, "trajectory state accelerationMetersPerSecondSq");
      requireFinite(state.curvatureRadPerMeter, "trajectory state curvatureRadPerMeter");
      requireFinitePose(state.poseMeters);
      if (state.timeSeconds < previousTimeSeconds) {
        throw new IllegalArgumentException("Trajectory state times must not decrease");
      }
      previousTimeSeconds = state.timeSeconds;
    }
  }

  private static Pose2d copyPose(Pose2d pose) {
    return new Pose2d(pose.getX(), pose.getY(), new Rotation2d(pose.getRotation().getRadians()));
  }

  private static Translation2d copyTranslation(Translation2d translation) {
    return new Translation2d(translation.getX(), translation.getY());
  }

  private static void requireFinitePose(Pose2d pose) {
    Objects.requireNonNull(pose, "canonicalBluePose");
    requireFinite(pose.getX(), "pose x");
    requireFinite(pose.getY(), "pose y");
    requireFiniteRotation(pose.getRotation());
  }

  private static void requireFiniteRotation(Rotation2d rotation) {
    Objects.requireNonNull(rotation, "canonicalBlueHeading");
    requireFinite(rotation.getRadians(), "rotation radians");
  }

  private static void requireFiniteTranslation(Translation2d translation) {
    Objects.requireNonNull(translation, "canonicalBlueFieldVelocity");
    requireFinite(translation.getX(), "field velocity x");
    requireFinite(translation.getY(), "field velocity y");
  }

  private static void requireFieldVariant(FieldVariant fieldVariant) {
    Objects.requireNonNull(fieldVariant, "fieldVariant");
  }

  private static void requireAlliance(Alliance alliance) {
    Objects.requireNonNull(alliance, "alliance");
  }

  private static void requireFinite(double value, String name) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException(name + " must be finite");
    }
  }
}
