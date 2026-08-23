// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.Constants;
import org.junit.jupiter.api.Test;

class LearningTrajectoryFactoryTest {
  private static final double kNumericalTolerance = 1.0e-9;
  private static final double kNegativeSampleTimeSeconds = -1.0;
  private static final double kOverrunSampleOffsetSeconds = 1.0;
  private static final double kMinimumInteriorExcursionMeters = 1.0e-3;

  @Test
  void createsNonemptyTrajectoryWithFinitePositiveDuration() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();

    assertNotNull(trajectory);
    assertFalse(trajectory.getStates().isEmpty());
    assertTrue(Double.isFinite(trajectory.getTotalTimeSeconds()));
    assertTrue(trajectory.getTotalTimeSeconds() > 0.0);
  }

  @Test
  void startSampleMatchesAuthoritativeLearningStartPose() {
    Trajectory.State startState =
        LearningTrajectoryFactory.createLearningTrajectory().sample(0.0);

    assertPoseEquals(Constants.FieldConstants.kLearningStartingPose, startState.poseMeters);
  }

  @Test
  void terminalSampleMatchesConfiguredLearningGoalPose() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();

    assertPoseEquals(
        Constants.TrajectoryGenerationConstants.kLearningGoalPose,
        trajectory.sample(trajectory.getTotalTimeSeconds()).poseMeters);
  }

  @Test
  void generatedStatesAreFiniteAndTimesAreMonotonic() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();
    double previousTimeSeconds = Double.NEGATIVE_INFINITY;

    for (Trajectory.State state : trajectory.getStates()) {
      assertStateIsFinite(state);
      assertTrue(state.timeSeconds >= previousTimeSeconds);
      previousTimeSeconds = state.timeSeconds;
    }
  }

  @Test
  void generatedStatesRespectLockedVelocityAndAccelerationConstraints() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();

    for (Trajectory.State state : trajectory.getStates()) {
      assertTrue(
          state.velocityMetersPerSecond
              <= Constants.TrajectoryGenerationConstants.kMaxVelocityMetersPerSecond
                  + kNumericalTolerance);
      assertTrue(
          Math.abs(state.accelerationMetersPerSecondSq)
              <= Constants.TrajectoryGenerationConstants.kMaxAccelerationMetersPerSecondSquared
                  + kNumericalTolerance);
    }
  }

  @Test
  void trajectoryUsesPositiveInteriorWaypointExcursionWithoutFixingSplineDiscretization() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();

    assertTrue(
        trajectory.getStates().stream()
            .anyMatch(state -> state.poseMeters.getY() > kMinimumInteriorExcursionMeters));
  }

  @Test
  void samplesStartMidpointAndTerminalTimeWithFiniteStates() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();
    double totalTimeSeconds = trajectory.getTotalTimeSeconds();

    assertStateIsFinite(trajectory.sample(0.0));
    assertStateIsFinite(trajectory.sample(totalTimeSeconds / 2.0));
    assertStateIsFinite(trajectory.sample(totalTimeSeconds));
  }

  @Test
  void samplingBeforeStartClampsToFirstState() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();

    assertStateEquals(
        trajectory.getStates().get(0), trajectory.sample(kNegativeSampleTimeSeconds));
  }

  @Test
  void samplingAfterEndClampsToFinalState() {
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();
    double overrunTimeSeconds =
        trajectory.getTotalTimeSeconds() + kOverrunSampleOffsetSeconds;

    assertStateEquals(
        trajectory.getStates().get(trajectory.getStates().size() - 1),
        trajectory.sample(overrunTimeSeconds));
  }

  @Test
  void repeatedGenerationProducesEquivalentDurationAndSamples() {
    Trajectory first = LearningTrajectoryFactory.createLearningTrajectory();
    Trajectory second = LearningTrajectoryFactory.createLearningTrajectory();

    assertEquals(first.getTotalTimeSeconds(), second.getTotalTimeSeconds(), kNumericalTolerance);
    assertStateEquals(first.sample(0.0), second.sample(0.0));
    assertStateEquals(
        first.sample(first.getTotalTimeSeconds() / 2.0),
        second.sample(second.getTotalTimeSeconds() / 2.0));
    assertStateEquals(
        first.sample(first.getTotalTimeSeconds()),
        second.sample(second.getTotalTimeSeconds()));
  }

  private static void assertStateIsFinite(Trajectory.State state) {
    assertNotNull(state);
    assertTrue(Double.isFinite(state.timeSeconds));
    assertTrue(Double.isFinite(state.velocityMetersPerSecond));
    assertTrue(Double.isFinite(state.accelerationMetersPerSecondSq));
    assertTrue(Double.isFinite(state.curvatureRadPerMeter));
    assertNotNull(state.poseMeters);
    assertTrue(Double.isFinite(state.poseMeters.getX()));
    assertTrue(Double.isFinite(state.poseMeters.getY()));
    assertTrue(Double.isFinite(state.poseMeters.getRotation().getRadians()));
  }

  private static void assertStateEquals(Trajectory.State expected, Trajectory.State actual) {
    assertEquals(expected.timeSeconds, actual.timeSeconds, kNumericalTolerance);
    assertEquals(
        expected.velocityMetersPerSecond,
        actual.velocityMetersPerSecond,
        kNumericalTolerance);
    assertEquals(
        expected.accelerationMetersPerSecondSq,
        actual.accelerationMetersPerSecondSq,
        kNumericalTolerance);
    assertEquals(expected.curvatureRadPerMeter, actual.curvatureRadPerMeter, kNumericalTolerance);
    assertPoseEquals(expected.poseMeters, actual.poseMeters);
  }

  private static void assertPoseEquals(Pose2d expected, Pose2d actual) {
    assertEquals(expected.getX(), actual.getX(), kNumericalTolerance);
    assertEquals(expected.getY(), actual.getY(), kNumericalTolerance);
    assertEquals(
        0.0,
        MathUtil.angleModulus(expected.getRotation().minus(actual.getRotation()).getRadians()),
        kNumericalTolerance);
  }
}
