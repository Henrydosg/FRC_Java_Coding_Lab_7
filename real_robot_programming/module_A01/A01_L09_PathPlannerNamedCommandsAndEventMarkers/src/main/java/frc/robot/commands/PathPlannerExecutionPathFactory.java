// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.IdealStartingState;
import com.pathplanner.lib.path.EventMarker;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.util.FieldAllianceTransform;
import java.util.List;
import java.util.Objects;

/** Builds a fresh, already transformed PathPlanner execution path. */
final class PathPlannerExecutionPathFactory {
  private PathPlannerExecutionPathFactory() {}

  /**
   * Validates the supported canonical path contract and creates a fresh execution path.
   *
   * <p>The canonical path is never mutated. Blue receives a fresh geometric copy. Red receives a
   * fresh copy whose Bezier anchors/control points and holonomic headings are transformed once by
   * the existing {@code FieldAllianceTransform} owner. The returned path always prevents vendor
   * flipping.
   *
   * @param canonicalPath validated canonical Blue-frame PathPlanner path
   * @param fieldVariant explicit field variant used by the L04 transform
   * @param alliance definite execution alliance
   * @return fresh execution path
   */
  static PathPlannerPath createExecutionPath(
      PathPlannerPath canonicalPath, FieldVariant fieldVariant, Alliance alliance) {
    return createExecutionPath(canonicalPath, fieldVariant, alliance, false);
  }

  /** Creates an execution copy while preserving the one explicitly approved L09 event marker. */
  static PathPlannerPath createExecutionPathWithEvents(
      PathPlannerPath canonicalPath, FieldVariant fieldVariant, Alliance alliance) {
    return createExecutionPath(canonicalPath, fieldVariant, alliance, true);
  }

  private static PathPlannerPath createExecutionPath(
      PathPlannerPath canonicalPath,
      FieldVariant fieldVariant,
      Alliance alliance,
      boolean preserveLearningEvent) {
    validateSupportedPath(canonicalPath, fieldVariant, alliance, preserveLearningEvent);

    List<Waypoint> executionWaypoints =
        canonicalPath.getWaypoints().stream()
            .map(waypoint -> transformWaypoint(waypoint, fieldVariant, alliance))
            .toList();

    IdealStartingState canonicalStart = canonicalPath.getIdealStartingState();
    GoalEndState canonicalGoal = canonicalPath.getGoalEndState();
    IdealStartingState executionStart =
        new IdealStartingState(
            canonicalStart.velocityMPS(),
            FieldAllianceTransform.fromCanonicalBlueHeading(
                canonicalStart.rotation(), fieldVariant, alliance));
    GoalEndState executionGoal =
        new GoalEndState(
            canonicalGoal.velocityMPS(),
            FieldAllianceTransform.fromCanonicalBlueHeading(
                canonicalGoal.rotation(), fieldVariant, alliance));

    PathPlannerPath executionPath =
        new PathPlannerPath(
            executionWaypoints,
            List.of(),
            List.of(),
            List.of(),
            preserveLearningEvent ? canonicalPath.getEventMarkers() : List.of(),
            canonicalPath.getGlobalConstraints(),
            executionStart,
            executionGoal,
            false);
    executionPath.name = canonicalPath.name;
    executionPath.preventFlipping = true;
    return executionPath;
  }

  private static Waypoint transformWaypoint(
      Waypoint waypoint, FieldVariant fieldVariant, Alliance alliance) {
    Objects.requireNonNull(waypoint, "waypoint");
    return new Waypoint(
        transformPosition(waypoint.prevControl(), fieldVariant, alliance),
        transformPosition(waypoint.anchor(), fieldVariant, alliance),
        transformPosition(waypoint.nextControl(), fieldVariant, alliance));
  }

  private static Translation2d transformPosition(
      Translation2d canonicalPosition, FieldVariant fieldVariant, Alliance alliance) {
    if (canonicalPosition == null) {
      return null;
    }
    requireFiniteTranslation(canonicalPosition, "path position");
    return FieldAllianceTransform.fromCanonicalBluePose(
            new Pose2d(canonicalPosition, Rotation2d.kZero), fieldVariant, alliance)
        .getTranslation();
  }

  private static void validateSupportedPath(
      PathPlannerPath path,
      FieldVariant fieldVariant,
      Alliance alliance,
      boolean preserveLearningEvent) {
    Objects.requireNonNull(path, "canonicalPath");
    Objects.requireNonNull(fieldVariant, "fieldVariant");
    Objects.requireNonNull(alliance, "alliance");
    if (path.isChoreoPath()) {
      throw new IllegalArgumentException("Choreo paths are unsupported in A01_L07");
    }
    if (path.isReversed()) {
      throw new IllegalArgumentException("Reversed paths are unsupported in A01_L07");
    }
    if (path.getRotationTargets() == null || !path.getRotationTargets().isEmpty()) {
      throw new IllegalArgumentException("Rotation targets are unsupported in A01_L07");
    }
    if (path.getPointTowardsZones() == null || !path.getPointTowardsZones().isEmpty()) {
      throw new IllegalArgumentException("Point-towards zones are unsupported in A01_L07");
    }
    if (path.getConstraintZones() == null || !path.getConstraintZones().isEmpty()) {
      throw new IllegalArgumentException("Constraint zones are unsupported in A01_L07");
    }
    if (path.getEventMarkers() == null
        || (!preserveLearningEvent && !path.getEventMarkers().isEmpty())) {
      throw new IllegalArgumentException("Event markers are unsupported in A01_L07");
    }
    if (preserveLearningEvent) {
      if (path.getEventMarkers().size() != 1) {
        throw new IllegalArgumentException("L09 requires exactly one event marker");
      }
      EventMarker marker = path.getEventMarkers().get(0);
      if (marker == null
          || !AutonomousEventId.LEARNING_EVENT.stableName().equals(marker.triggerName())
          || !Double.isFinite(marker.position())
          || Math.abs(marker.position() - 0.5) > 1.0e-9
          || marker.command() == null) {
        throw new IllegalArgumentException("L09 event marker is invalid");
      }
    }
    if (path.name == null) {
      throw new IllegalArgumentException("Path name must be non-null");
    }
    if (path.getWaypoints() == null || path.getWaypoints().size() < 2) {
      throw new IllegalArgumentException("Path must contain at least two waypoints");
    }
    for (Waypoint waypoint : path.getWaypoints()) {
      if (waypoint == null || waypoint.anchor() == null) {
        throw new IllegalArgumentException("Path waypoints must contain finite anchors");
      }
      requireFiniteTranslation(waypoint.anchor(), "waypoint anchor");
      if (waypoint.prevControl() != null) {
        requireFiniteTranslation(waypoint.prevControl(), "waypoint previous control");
      }
      if (waypoint.nextControl() != null) {
        requireFiniteTranslation(waypoint.nextControl(), "waypoint next control");
      }
    }

    PathConstraints constraints = path.getGlobalConstraints();
    if (constraints == null
        || constraints.unlimited()
        || !isFinitePositive(constraints.maxVelocityMPS())
        || !isFinitePositive(constraints.maxAccelerationMPSSq())
        || !isFinitePositive(constraints.maxAngularVelocityRadPerSec())
        || !isFinitePositive(constraints.maxAngularAccelerationRadPerSecSq())
        || !isFinitePositive(constraints.nominalVoltageVolts())) {
      throw new IllegalArgumentException("Path constraints must be finite and bounded");
    }

    validateStartingState(path.getIdealStartingState());
    validateGoalState(path.getGoalEndState());
  }

  private static void validateStartingState(IdealStartingState state) {
    if (state == null
        || !isFiniteNonnegative(state.velocityMPS())
        || !isFiniteRotation(state.rotation())) {
      throw new IllegalArgumentException("Ideal starting state must be finite");
    }
  }

  private static void validateGoalState(GoalEndState state) {
    if (state == null
        || !isFiniteNonnegative(state.velocityMPS())
        || !isFiniteRotation(state.rotation())) {
      throw new IllegalArgumentException("Goal end state must be finite");
    }
  }

  private static void requireFiniteTranslation(Translation2d translation, String name) {
    if (translation == null
        || !Double.isFinite(translation.getX())
        || !Double.isFinite(translation.getY())) {
      throw new IllegalArgumentException(name + " must be finite");
    }
  }

  private static boolean isFiniteRotation(Rotation2d rotation) {
    return rotation != null && Double.isFinite(rotation.getRadians());
  }

  private static boolean isFinitePositive(double value) {
    return Double.isFinite(value) && value > 0.0;
  }

  private static boolean isFiniteNonnegative(double value) {
    return Double.isFinite(value) && value >= 0.0;
  }
}
