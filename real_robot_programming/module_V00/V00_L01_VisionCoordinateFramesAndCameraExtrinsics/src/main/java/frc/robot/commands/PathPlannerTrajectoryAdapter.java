// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.Waypoint;
import com.pathplanner.lib.path.EventMarker;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.json.simple.parser.ParseException;

/**
 * Loads the single approved PathPlanner path and exposes only native WPILib trajectory data.
 *
 * <p>This adapter owns no alliance policy, localization, drivetrain, command requirements, or
 * hardware access. The caller applies the A01/L04 alliance transform exactly once after receiving
 * the canonical Blue-frame trajectory.
 */
public final class PathPlannerTrajectoryAdapter {
  private static final double kNumericalTolerance = 1.0e-9;

  private final RobotConfig robotConfig;

  /**
   * Creates an adapter with one composition-root-owned PathPlanner RobotConfig.
   *
   * @param robotConfig learning-only RobotConfig supplied by the composition root
   */
  public PathPlannerTrajectoryAdapter(RobotConfig robotConfig) {
    this.robotConfig = Objects.requireNonNull(robotConfig, "robotConfig");
    validateRobotConfig(this.robotConfig);
  }

  private static void validateRobotConfig(RobotConfig robotConfig) {
    ModuleConfig moduleConfig = robotConfig.moduleConfig;
    if (!robotConfig.isHolonomic
        || robotConfig.numModules != 4
        || !isFinitePositive(robotConfig.massKG)
        || !isFinitePositive(robotConfig.MOI)
        || moduleConfig == null
        || !isFinitePositive(moduleConfig.wheelRadiusMeters)
        || !isFinitePositive(moduleConfig.maxDriveVelocityMPS)
        || !isFinitePositive(moduleConfig.wheelCOF)
        || !isFinitePositive(moduleConfig.driveCurrentLimit)
        || !isFinitePositive(moduleConfig.maxDriveVelocityRadPerSec)
        || !Double.isFinite(moduleConfig.torqueLoss)
        || moduleConfig.torqueLoss < 0.0
        || !isValidDriveMotor(moduleConfig.driveMotor)
        || !isValidModuleGeometry(robotConfig)) {
      throw new IllegalArgumentException(
          "robotConfig must contain a finite four-module holonomic configuration");
    }
    if (!isFinitePositive(robotConfig.wheelFrictionForce)
        || !isFinitePositive(robotConfig.maxTorqueFriction)) {
      throw new IllegalArgumentException("robotConfig derived friction values must be finite");
    }
  }

  private static boolean isValidDriveMotor(DCMotor driveMotor) {
    return driveMotor != null
        && isFinitePositive(driveMotor.nominalVoltageVolts)
        && isFinitePositive(driveMotor.stallTorqueNewtonMeters)
        && isFinitePositive(driveMotor.stallCurrentAmps)
        && isFinitePositive(driveMotor.freeCurrentAmps)
        && isFinitePositive(driveMotor.freeSpeedRadPerSec)
        && isFinitePositive(driveMotor.rOhms)
        && isFinitePositive(driveMotor.KvRadPerSecPerVolt)
        && isFinitePositive(driveMotor.KtNMPerAmp);
  }

  private static boolean isValidModuleGeometry(RobotConfig robotConfig) {
    Translation2d[] locations = robotConfig.moduleLocations;
    double[] pivotDistances = robotConfig.modulePivotDistance;
    if (locations == null
        || locations.length != 4
        || pivotDistances == null
        || pivotDistances.length != 4) {
      return false;
    }

    for (int index = 0; index < locations.length; index++) {
      Translation2d location = locations[index];
      if (location == null
          || !Double.isFinite(location.getX())
          || !Double.isFinite(location.getY())
          || !isFinitePositive(location.getNorm())
          || !isFinitePositive(pivotDistances[index])) {
        return false;
      }
      for (int previousIndex = 0; previousIndex < index; previousIndex++) {
        if (location.getDistance(locations[previousIndex]) <= kNumericalTolerance) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean isFinitePositive(double value) {
    return Double.isFinite(value) && value > 0.0;
  }

  /**
   * Loads and generates the canonical Blue-frame trajectory for the approved path.
   *
   * @return a validated native WPILib trajectory
   * @throws IllegalStateException if the asset, generated data, or required semantics are invalid
   */
  public Trajectory createCanonicalTrajectory() {
    PathPlannerPath path = createCanonicalPath();

    PathPlannerTrajectory pathPlannerTrajectory =
        path.generateTrajectory(
            new edu.wpi.first.math.kinematics.ChassisSpeeds(), Rotation2d.kZero, robotConfig);
    validatePathPlannerTrajectory(pathPlannerTrajectory);
    return convertToNativeTrajectory(pathPlannerTrajectory);
  }

  /**
   * Loads and validates the canonical Blue-frame path for AutoBuilder integration.
   *
   * <p>The returned PathPlanner path is the validated source path. Callers must create a fresh
   * execution copy before applying any alliance transformation.
   *
   * @return validated canonical Blue-frame path
   * @throws IllegalStateException if the asset or locked path semantics are invalid
   */
  PathPlannerPath createCanonicalPath() {
    PathPlannerPath path = loadApprovedPath();
    validateApprovedPath(path);
    return path;
  }

  /** Loads and validates the dedicated L09 path containing exactly one learning event marker. */
  public PathPlannerPath createCanonicalEventPath() {
    PathPlannerPath path = loadApprovedEventPath();
    validateApprovedEventPath(path);
    return path;
  }

  private static PathPlannerPath loadApprovedPath() {
    return loadPath(Constants.PathPlannerLearningConstants.kPathAssetName);
  }

  private static PathPlannerPath loadApprovedEventPath() {
    return loadPath(Constants.PathPlannerLearningConstants.kLearningEventPathAssetName);
  }

  private static PathPlannerPath loadPath(String assetName) {
    try {
      PathPlannerPath path = PathPlannerPath.fromPathFile(assetName);
      path.preventFlipping = true;
      return path;
    } catch (IOException | ParseException exception) {
      throw new IllegalStateException(
          "Unable to load "
              + assetName
              + " from "
              + new java.io.File(
                      Filesystem.getDeployDirectory(),
                      "pathplanner/paths/" + assetName + ".path")
                  .getPath(),
          exception);
    }
  }

  private static void validateApprovedPath(PathPlannerPath path) {
    validateApprovedPathGeometry(path);
    if (!path.getEventMarkers().isEmpty()) {
      throw new IllegalStateException("The approved event-free path contains event markers");
    }
  }

  private static void validateApprovedEventPath(PathPlannerPath path) {
    validateApprovedPathGeometry(path);
    List<EventMarker> eventMarkers = path.getEventMarkers();
    if (eventMarkers == null || eventMarkers.size() != 1) {
      throw new IllegalStateException("The L09 event path must contain exactly one event marker");
    }
    EventMarker marker = eventMarkers.get(0);
    if (marker == null
        || !AutonomousEventId.LEARNING_EVENT.stableName().equals(marker.triggerName())
        || !close(marker.position(), 0.5)
        || marker.command() == null
        || !NamedCommands.hasCommand(marker.triggerName())) {
      throw new IllegalStateException("The L09 event marker is not the explicit learning event");
    }
  }

  private static void validateApprovedPathGeometry(PathPlannerPath path) {
    Objects.requireNonNull(path, "path");
    if (path.isChoreoPath() || path.isReversed()) {
      throw new IllegalStateException("The approved path must be a non-reversed PathPlanner path");
    }
    if (!path.getRotationTargets().isEmpty()
        || !path.getPointTowardsZones().isEmpty()
        || !path.getConstraintZones().isEmpty()) {
      throw new IllegalStateException("The approved path contains unsupported PathPlanner features");
    }

    List<Waypoint> waypoints = path.getWaypoints();
    if (waypoints == null || waypoints.size() != 2) {
      throw new IllegalStateException("The approved path must contain exactly two waypoints");
    }
    requireWaypointAnchor(
        waypoints.get(0), Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose);
    requireWaypointAnchor(
        waypoints.get(1), Constants.PathPlannerLearningConstants.kCanonicalPathEndingPose);

    PathConstraints constraints = path.getGlobalConstraints();
    if (constraints == null
        || !close(
            constraints.maxVelocityMPS(),
            Constants.PathPlannerLearningConstants.kPathMaxTranslationVelocityMetersPerSecond)
        || !close(
            constraints.maxAccelerationMPSSq(),
            Constants.PathPlannerLearningConstants.kPathMaxTranslationAccelerationMetersPerSecondSquared)
        || !close(
            constraints.maxAngularVelocityRadPerSec(),
            Constants.PathPlannerLearningConstants.kPathMaxAngularVelocityRadiansPerSecond)
        || !close(
            constraints.maxAngularAccelerationRadPerSecSq(),
            Constants.PathPlannerLearningConstants.kPathMaxAngularAccelerationRadiansPerSecondSquared)
        || !close(
            constraints.nominalVoltageVolts(),
            Constants.PathPlannerLearningConstants.kPathNominalVoltageVolts)
        || constraints.unlimited()) {
      throw new IllegalStateException("The approved path constraints are not the locked constraints");
    }

    if (!close(path.getIdealStartingState().velocityMPS(), 0.0)
        || !close(path.getIdealStartingState().rotation().getRadians(), 0.0)
        || !close(path.getGoalEndState().velocityMPS(), 0.0)
        || !close(path.getGoalEndState().rotation().getRadians(), 0.0)) {
      throw new IllegalStateException("The approved path must start and end at zero holonomic state");
    }
  }

  private static void requireWaypointAnchor(Waypoint waypoint, Pose2d expectedPose) {
    if (waypoint == null
        || waypoint.anchor() == null
        || !close(waypoint.anchor().getX(), expectedPose.getX())
        || !close(waypoint.anchor().getY(), expectedPose.getY())) {
      throw new IllegalStateException("The approved path waypoint anchors are not canonical");
    }
  }

  private static void validatePathPlannerTrajectory(PathPlannerTrajectory trajectory) {
    Objects.requireNonNull(trajectory, "pathPlannerTrajectory");
    if (trajectory.getEvents() == null || !trajectory.getEvents().isEmpty()) {
      throw new IllegalStateException("The approved trajectory must not contain events");
    }

    List<PathPlannerTrajectoryState> states = trajectory.getStates();
    if (states == null || states.size() < 2) {
      throw new IllegalStateException("The approved trajectory must contain at least two states");
    }
    double totalTimeSeconds = trajectory.getTotalTimeSeconds();
    if (!Double.isFinite(totalTimeSeconds) || totalTimeSeconds <= 0.0) {
      throw new IllegalStateException("The approved trajectory must have finite positive timing");
    }

    double previousTimeSeconds = Double.NEGATIVE_INFINITY;
    for (int index = 0; index < states.size(); index++) {
      PathPlannerTrajectoryState state = states.get(index);
      validateState(state, previousTimeSeconds);
      if (index > 0) {
        validateAdjacentGeometry(states.get(index - 1), state);
      }
      previousTimeSeconds = state.timeSeconds;
    }
    if (!close(states.get(0).timeSeconds, 0.0)
        || !close(states.get(states.size() - 1).timeSeconds, totalTimeSeconds)) {
      throw new IllegalStateException("The approved trajectory timing must start at zero and end at total time");
    }
  }

  private static void validateState(PathPlannerTrajectoryState state, double previousTimeSeconds) {
    if (state == null
        || !Double.isFinite(state.timeSeconds)
        || state.timeSeconds <= previousTimeSeconds
        || !Double.isFinite(state.linearVelocity)
        || state.fieldSpeeds == null
        || !Double.isFinite(state.fieldSpeeds.vxMetersPerSecond)
        || !Double.isFinite(state.fieldSpeeds.vyMetersPerSecond)
        || !Double.isFinite(state.fieldSpeeds.omegaRadiansPerSecond)
        || state.pose == null
        || !Double.isFinite(state.pose.getX())
        || !Double.isFinite(state.pose.getY())
        || state.heading == null
        || !Double.isFinite(state.heading.getRadians())) {
      throw new IllegalStateException("PathPlanner trajectory states must be finite and strictly timed");
    }
  }

  private static void validateAdjacentGeometry(
      PathPlannerTrajectoryState previous, PathPlannerTrajectoryState current) {
    double distanceMeters = previous.pose.getTranslation().getDistance(current.pose.getTranslation());
    double headingDeltaRadians =
        MathUtil.angleModulus(current.heading.getRadians() - previous.heading.getRadians());
    if (distanceMeters <= Constants.PathPlannerLearningConstants.kTrajectoryValidationDistanceEpsilonMeters
        && Math.abs(headingDeltaRadians)
            > Constants.PathPlannerLearningConstants.kTrajectoryValidationHeadingEpsilonRadians) {
      throw new IllegalStateException(
          "Zero-distance nonzero-heading transitions are unsupported");
    }
  }

  private static Trajectory convertToNativeTrajectory(PathPlannerTrajectory trajectory) {
    List<PathPlannerTrajectoryState> sourceStates = trajectory.getStates();
    List<Trajectory.State> nativeStates = new ArrayList<>(sourceStates.size());
    for (int index = 0; index < sourceStates.size(); index++) {
      PathPlannerTrajectoryState state = sourceStates.get(index);
      double accelerationMetersPerSecondSquared =
          deriveAcceleration(sourceStates, index);
      double curvatureRadiansPerMeter = deriveCurvature(sourceStates, index);
      Pose2d tangentPose =
          new Pose2d(
              state.pose.getX(), state.pose.getY(), new Rotation2d(state.heading.getRadians()));
      Trajectory.State nativeState =
          new Trajectory.State(
              state.timeSeconds,
              state.linearVelocity,
              accelerationMetersPerSecondSquared,
              tangentPose,
              curvatureRadiansPerMeter);
      if (!isFiniteNativeState(nativeState)) {
        throw new IllegalStateException("Converted WPILib trajectory state must be finite");
      }
      nativeStates.add(nativeState);
    }

    Trajectory nativeTrajectory = new Trajectory(nativeStates);
    if (!isExpectedEndpoint(nativeTrajectory)) {
      throw new IllegalStateException("Converted trajectory endpoints are not canonical");
    }
    return nativeTrajectory;
  }

  private static double deriveAcceleration(List<PathPlannerTrajectoryState> states, int index) {
    int firstIndex = index == 0 ? 0 : index == states.size() - 1 ? index - 1 : index - 1;
    int secondIndex = index == 0 ? 1 : index == states.size() - 1 ? index : index + 1;
    double deltaVelocity = states.get(secondIndex).linearVelocity - states.get(firstIndex).linearVelocity;
    double deltaTime = states.get(secondIndex).timeSeconds - states.get(firstIndex).timeSeconds;
    double acceleration = deltaVelocity / deltaTime;
    if (!Double.isFinite(acceleration)) {
      throw new IllegalStateException("Derived acceleration must be finite");
    }
    return acceleration;
  }

  private static double deriveCurvature(List<PathPlannerTrajectoryState> states, int index) {
    int firstIndex = index == 0 ? 0 : index == states.size() - 1 ? index - 1 : index - 1;
    int secondIndex = index == 0 ? 1 : index == states.size() - 1 ? index : index + 1;
    PathPlannerTrajectoryState first = states.get(firstIndex);
    PathPlannerTrajectoryState second = states.get(secondIndex);
    double distanceMeters = first.pose.getTranslation().getDistance(second.pose.getTranslation());
    double headingDeltaRadians =
        MathUtil.angleModulus(second.heading.getRadians() - first.heading.getRadians());
    if (distanceMeters
        <= Constants.PathPlannerLearningConstants.kTrajectoryValidationDistanceEpsilonMeters) {
      if (Math.abs(headingDeltaRadians)
          > Constants.PathPlannerLearningConstants.kTrajectoryValidationHeadingEpsilonRadians) {
        throw new IllegalStateException(
            "Cannot derive curvature from a zero-distance heading transition");
      }
      return 0.0;
    }
    double curvatureRadiansPerMeter = headingDeltaRadians / distanceMeters;
    if (!Double.isFinite(curvatureRadiansPerMeter)) {
      throw new IllegalStateException("Derived curvature must be finite");
    }
    return curvatureRadiansPerMeter;
  }

  private static boolean isExpectedEndpoint(Trajectory trajectory) {
    Trajectory.State start = trajectory.getStates().get(0);
    Trajectory.State end = trajectory.getStates().get(trajectory.getStates().size() - 1);
    return posesEqual(start.poseMeters, Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose)
        && posesEqual(end.poseMeters, Constants.PathPlannerLearningConstants.kCanonicalPathEndingPose);
  }

  private static boolean isFiniteNativeState(Trajectory.State state) {
    return state != null
        && Double.isFinite(state.timeSeconds)
        && Double.isFinite(state.velocityMetersPerSecond)
        && Double.isFinite(state.accelerationMetersPerSecondSq)
        && Double.isFinite(state.curvatureRadPerMeter)
        && state.poseMeters != null
        && Double.isFinite(state.poseMeters.getX())
        && Double.isFinite(state.poseMeters.getY())
        && Double.isFinite(state.poseMeters.getRotation().getRadians());
  }

  private static boolean posesEqual(Pose2d first, Pose2d second) {
    return close(first.getX(), second.getX())
        && close(first.getY(), second.getY())
        && close(
            MathUtil.angleModulus(
                first.getRotation().getRadians() - second.getRotation().getRadians()),
            0.0);
  }

  private static boolean close(double first, double second) {
    return Math.abs(first - second) <= kNumericalTolerance;
  }
}
