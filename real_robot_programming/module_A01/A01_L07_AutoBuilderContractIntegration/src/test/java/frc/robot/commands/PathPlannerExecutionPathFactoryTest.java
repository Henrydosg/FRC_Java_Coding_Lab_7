// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.path.EventMarker;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.IdealStartingState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.path.RotationTarget;
import com.pathplanner.lib.path.Waypoint;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.util.FieldAllianceTransform;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PathPlannerExecutionPathFactoryTest {
  private static final double kTolerance = 1.0e-9;
  private static final FieldVariant kFieldVariant = FieldVariant.REBUILT_WELDED;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void blueExecutionPathIsFreshAndPreservesCanonicalGeometry() {
    PathPlannerPath canonical = createPath(List.of(), List.of(), false);

    PathPlannerPath execution =
        PathPlannerExecutionPathFactory.createExecutionPath(
            canonical, kFieldVariant, DriverStation.Alliance.Blue);

    assertNotSame(canonical, execution);
    assertEquals(canonical.name, execution.name);
    assertTrue(execution.preventFlipping);
    assertEquals(canonical.getWaypoints(), execution.getWaypoints());
    assertEquals(canonical.getGlobalConstraints(), execution.getGlobalConstraints());
    assertEquals(canonical.getIdealStartingState(), execution.getIdealStartingState());
    assertEquals(canonical.getGoalEndState(), execution.getGoalEndState());
    assertTrue(canonical.getRotationTargets().isEmpty());
    assertTrue(canonical.getEventMarkers().isEmpty());
  }

  @Test
  void redExecutionPathAppliesExactlyOneL04Transform() {
    PathPlannerPath canonical = createPath(List.of(), List.of(), false);

    PathPlannerPath execution =
        PathPlannerExecutionPathFactory.createExecutionPath(
            canonical, kFieldVariant, DriverStation.Alliance.Red);

    for (int index = 0; index < canonical.getWaypoints().size(); index++) {
      Translation2d canonicalAnchor = canonical.getWaypoints().get(index).anchor();
      Translation2d expectedAnchor =
          FieldAllianceTransform.fromCanonicalBluePose(
                  new edu.wpi.first.math.geometry.Pose2d(
                      canonicalAnchor, Rotation2d.kZero), kFieldVariant, DriverStation.Alliance.Red)
              .getTranslation();
      Translation2d executionAnchor = execution.getWaypoints().get(index).anchor();
      assertEquals(expectedAnchor.getX(), executionAnchor.getX(), kTolerance);
      assertEquals(expectedAnchor.getY(), executionAnchor.getY(), kTolerance);
    }

    assertEquals(
        FieldAllianceTransform.fromCanonicalBlueHeading(
            canonical.getIdealStartingState().rotation(), kFieldVariant, DriverStation.Alliance.Red),
        execution.getIdealStartingState().rotation());
    assertEquals(
        FieldAllianceTransform.fromCanonicalBlueHeading(
            canonical.getGoalEndState().rotation(), kFieldVariant, DriverStation.Alliance.Red),
        execution.getGoalEndState().rotation());
  }

  @Test
  void unsupportedRotationTargetsAndEventsAreRejected() {
    PathPlannerPath rotationTargetPath =
        createPath(List.of(new RotationTarget(0.5, Rotation2d.fromDegrees(20.0))), List.of(), false);
    PathPlannerPath eventPath = createPath(List.of(), List.of(new EventMarker("event", 0.5)), false);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            PathPlannerExecutionPathFactory.createExecutionPath(
                rotationTargetPath, kFieldVariant, DriverStation.Alliance.Blue));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            PathPlannerExecutionPathFactory.createExecutionPath(
                eventPath, kFieldVariant, DriverStation.Alliance.Blue));
  }

  @Test
  void reversedAndUnboundedPathsAreRejected() {
    PathPlannerPath reversedPath = createPath(List.of(), List.of(), true);
    PathPlannerPath unboundedPath =
        new PathPlannerPath(
            waypoints(),
            PathConstraints.unlimitedConstraints(12.0),
            new IdealStartingState(0.0, Rotation2d.kZero),
            new GoalEndState(0.0, Rotation2d.kZero));

    assertThrows(
        IllegalArgumentException.class,
        () ->
            PathPlannerExecutionPathFactory.createExecutionPath(
                reversedPath, kFieldVariant, DriverStation.Alliance.Blue));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            PathPlannerExecutionPathFactory.createExecutionPath(
                unboundedPath, kFieldVariant, DriverStation.Alliance.Blue));
  }

  private static PathPlannerPath createPath(
      List<RotationTarget> rotationTargets, List<EventMarker> eventMarkers, boolean reversed) {
    PathPlannerPath path =
        new PathPlannerPath(
            waypoints(),
            rotationTargets,
            List.of(),
            List.of(),
            eventMarkers,
            new PathConstraints(3.0, 4.0, 5.0, 6.0, 12.0),
            new IdealStartingState(0.5, Rotation2d.fromRadians(0.2)),
            new GoalEndState(0.0, Rotation2d.fromRadians(-0.3)),
            reversed);
    path.name = "L07_Focused_Test_Path";
    return path;
  }

  private static List<Waypoint> waypoints() {
    return List.of(
        new Waypoint(
            null, new Translation2d(1.0, 2.0), new Translation2d(1.5, 2.0)),
        new Waypoint(
            new Translation2d(2.5, 2.0), new Translation2d(3.0, 2.0), null));
  }
}
