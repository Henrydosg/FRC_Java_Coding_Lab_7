// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.util;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import frc.robot.Constants;
import java.util.List;

/** Creates the finite L03 learning trajectory without commanding robot behavior. */
public final class LearningTrajectoryFactory {
  private LearningTrajectoryFactory() {}

  /**
   * Creates a fresh time-parameterized trajectory from the locked L03 learning definition.
   *
   * <p>The returned trajectory owns sampling through {@link Trajectory#sample(double)}. State pose
   * rotation describes path geometry and is not a holonomic robot-heading profile.
   *
   * @return validated native WPILib trajectory data
   * @throws IllegalStateException if WPILib generation produces unusable data
   */
  public static Trajectory createLearningTrajectory() {
    TrajectoryConfig trajectoryConfig =
        new TrajectoryConfig(
            Constants.TrajectoryGenerationConstants.kMaxVelocityMetersPerSecond,
            Constants.TrajectoryGenerationConstants.kMaxAccelerationMetersPerSecondSquared);
    Trajectory trajectory =
        TrajectoryGenerator.generateTrajectory(
            Constants.FieldConstants.kLearningStartingPose,
            Constants.TrajectoryGenerationConstants.kLearningInteriorWaypoints,
            Constants.TrajectoryGenerationConstants.kLearningGoalPose,
            trajectoryConfig);
    validateGeneratedTrajectory(trajectory);
    return trajectory;
  }

  private static void validateGeneratedTrajectory(Trajectory trajectory) {
    if (trajectory == null) {
      throw new IllegalStateException("Generated trajectory must not be null");
    }

    List<Trajectory.State> states = trajectory.getStates();
    if (states == null || states.isEmpty()) {
      throw new IllegalStateException("Generated trajectory must contain states");
    }

    double totalTimeSeconds = trajectory.getTotalTimeSeconds();
    if (!Double.isFinite(totalTimeSeconds) || totalTimeSeconds <= 0.0) {
      throw new IllegalStateException("Generated trajectory must have finite positive duration");
    }

    double previousTimeSeconds = Double.NEGATIVE_INFINITY;
    for (Trajectory.State state : states) {
      if (state == null) {
        throw new IllegalStateException("Generated trajectory must not contain null states");
      }
      if (!Double.isFinite(state.timeSeconds) || state.timeSeconds < previousTimeSeconds) {
        throw new IllegalStateException("Generated trajectory state times must be finite and monotonic");
      }
      if (!Double.isFinite(state.velocityMetersPerSecond)
          || !Double.isFinite(state.accelerationMetersPerSecondSq)
          || !Double.isFinite(state.curvatureRadPerMeter)) {
        throw new IllegalStateException("Generated trajectory state kinematics must be finite");
      }

      Pose2d pose = state.poseMeters;
      if (pose == null
          || !Double.isFinite(pose.getX())
          || !Double.isFinite(pose.getY())
          || !Double.isFinite(pose.getRotation().getRadians())) {
        throw new IllegalStateException("Generated trajectory state pose must be finite");
      }
      previousTimeSeconds = state.timeSeconds;
    }
  }
}

