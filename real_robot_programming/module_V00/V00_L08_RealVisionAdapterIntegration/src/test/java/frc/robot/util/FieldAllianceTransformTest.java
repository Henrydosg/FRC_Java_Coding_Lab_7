// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class FieldAllianceTransformTest {
  private static final double kTolerance = 1.0e-9;
  private static final Pose2d kOffCenterPose =
      new Pose2d(2.75, 1.60, Rotation2d.fromDegrees(37.0));
  private static final Translation2d kFieldVelocity = new Translation2d(0.70, -1.10);
  private static final double kAngularVelocityRadiansPerSecond = -0.45;

  @Test
  void officialFieldVariantsExposeLocked2026Dimensions() {
    assertEquals(16.541, FieldVariant.REBUILT_WELDED.fieldLengthMeters(), kTolerance);
    assertEquals(8.069, FieldVariant.REBUILT_WELDED.fieldWidthMeters(), kTolerance);
    assertEquals(16.518, FieldVariant.REBUILT_ANDYMARK.fieldLengthMeters(), kTolerance);
    assertEquals(8.043, FieldVariant.REBUILT_ANDYMARK.fieldWidthMeters(), kTolerance);
  }

  @Test
  void blueTransformsPreservePoseHeadingVelocityAndAngularVelocityGeometry() {
    for (FieldVariant fieldVariant : FieldVariant.values()) {
      Pose2d transformedPose =
          FieldAllianceTransform.fromCanonicalBluePose(kOffCenterPose, fieldVariant, Alliance.Blue);
      Rotation2d transformedHeading =
          FieldAllianceTransform.fromCanonicalBlueHeading(
              kOffCenterPose.getRotation(), fieldVariant, Alliance.Blue);
      Translation2d transformedVelocity =
          FieldAllianceTransform.fromCanonicalBlueFieldVelocity(
              kFieldVelocity, fieldVariant, Alliance.Blue);
      double transformedAngularVelocity =
          FieldAllianceTransform.fromCanonicalBlueAngularVelocity(
              kAngularVelocityRadiansPerSecond, fieldVariant, Alliance.Blue);

      assertPoseEquals(kOffCenterPose, transformedPose);
      assertRotationEquals(kOffCenterPose.getRotation(), transformedHeading);
      assertTranslationEquals(kFieldVelocity, transformedVelocity);
      assertEquals(kAngularVelocityRadiansPerSecond, transformedAngularVelocity, kTolerance);
    }
  }

  @Test
  void blueTrajectoryPreservesStateDataWithFreshTrajectoryAndStates() {
    Trajectory canonicalTrajectory = createCanonicalTrajectory();

    for (FieldVariant fieldVariant : FieldVariant.values()) {
      Trajectory transformedTrajectory =
          FieldAllianceTransform.fromCanonicalBlueTrajectory(
              canonicalTrajectory, fieldVariant, Alliance.Blue);

      assertNotSame(canonicalTrajectory, transformedTrajectory);
      assertTrajectoryDataEquals(canonicalTrajectory, transformedTrajectory);
      for (int index = 0; index < canonicalTrajectory.getStates().size(); index++) {
        assertNotSame(
            canonicalTrajectory.getStates().get(index), transformedTrajectory.getStates().get(index));
      }
    }
  }

  @Test
  void redTransformsOffCenterPoseAndHeadingByFieldCentreRotation() {
    for (FieldVariant fieldVariant : FieldVariant.values()) {
      Pose2d transformedPose =
          FieldAllianceTransform.fromCanonicalBluePose(kOffCenterPose, fieldVariant, Alliance.Red);
      Rotation2d transformedHeading =
          FieldAllianceTransform.fromCanonicalBlueHeading(
              kOffCenterPose.getRotation(), fieldVariant, Alliance.Red);

      assertEquals(fieldVariant.fieldLengthMeters() - kOffCenterPose.getX(), transformedPose.getX(), kTolerance);
      assertEquals(fieldVariant.fieldWidthMeters() - kOffCenterPose.getY(), transformedPose.getY(), kTolerance);
      assertRotationEquals(kOffCenterPose.getRotation().plus(Rotation2d.kPi), transformedPose.getRotation());
      assertRotationEquals(kOffCenterPose.getRotation().plus(Rotation2d.kPi), transformedHeading);
    }
  }

  @Test
  void redVelocityNegatesTranslationAndPreservesAngularVelocity() {
    for (FieldVariant fieldVariant : FieldVariant.values()) {
      Translation2d transformedVelocity =
          FieldAllianceTransform.fromCanonicalBlueFieldVelocity(
              kFieldVelocity, fieldVariant, Alliance.Red);
      double transformedAngularVelocity =
          FieldAllianceTransform.fromCanonicalBlueAngularVelocity(
              kAngularVelocityRadiansPerSecond, fieldVariant, Alliance.Red);

      assertTranslationEquals(
          new Translation2d(-kFieldVelocity.getX(), -kFieldVelocity.getY()), transformedVelocity);
      assertEquals(kAngularVelocityRadiansPerSecond, transformedAngularVelocity, kTolerance);
    }
  }

  @Test
  void redTrajectoryTransformsEveryPoseAndPreservesAllScalarStateData() {
    Trajectory canonicalTrajectory = createCanonicalTrajectory();

    for (FieldVariant fieldVariant : FieldVariant.values()) {
      Trajectory transformedTrajectory =
          FieldAllianceTransform.fromCanonicalBlueTrajectory(
              canonicalTrajectory, fieldVariant, Alliance.Red);

      assertNotSame(canonicalTrajectory, transformedTrajectory);
      assertEquals(
          canonicalTrajectory.getTotalTimeSeconds(),
          transformedTrajectory.getTotalTimeSeconds(),
          kTolerance);
      assertEquals(canonicalTrajectory.getStates().size(), transformedTrajectory.getStates().size());

      for (int index = 0; index < canonicalTrajectory.getStates().size(); index++) {
        Trajectory.State canonicalState = canonicalTrajectory.getStates().get(index);
        Trajectory.State transformedState = transformedTrajectory.getStates().get(index);

        assertNotSame(canonicalState, transformedState);
        assertEquals(canonicalState.timeSeconds, transformedState.timeSeconds);
        assertEquals(canonicalState.velocityMetersPerSecond, transformedState.velocityMetersPerSecond);
        assertEquals(
            canonicalState.accelerationMetersPerSecondSq,
            transformedState.accelerationMetersPerSecondSq);
        assertEquals(canonicalState.curvatureRadPerMeter, transformedState.curvatureRadPerMeter);
        assertEquals(
            fieldVariant.fieldLengthMeters() - canonicalState.poseMeters.getX(),
            transformedState.poseMeters.getX(),
            kTolerance);
        assertEquals(
            fieldVariant.fieldWidthMeters() - canonicalState.poseMeters.getY(),
            transformedState.poseMeters.getY(),
            kTolerance);
        assertRotationEquals(
            canonicalState.poseMeters.getRotation().plus(Rotation2d.kPi),
            transformedState.poseMeters.getRotation());
      }
    }
  }

  @Test
  void doubleRedTransformMisuseSignatureReturnsCanonicalGeometryMathematically() {
    for (FieldVariant fieldVariant : FieldVariant.values()) {
      Pose2d onceTransformed =
          FieldAllianceTransform.fromCanonicalBluePose(kOffCenterPose, fieldVariant, Alliance.Red);
      Pose2d twiceTransformed =
          FieldAllianceTransform.fromCanonicalBluePose(onceTransformed, fieldVariant, Alliance.Red);
      Translation2d onceVelocity =
          FieldAllianceTransform.fromCanonicalBlueFieldVelocity(
              kFieldVelocity, fieldVariant, Alliance.Red);
      Translation2d twiceVelocity =
          FieldAllianceTransform.fromCanonicalBlueFieldVelocity(
              onceVelocity, fieldVariant, Alliance.Red);

      assertPoseEquals(kOffCenterPose, twiceTransformed);
      assertTranslationEquals(kFieldVelocity, twiceVelocity);
    }
  }

  @Test
  void publicApisRejectNullObjectArguments() {
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBluePose(null, FieldVariant.REBUILT_WELDED, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBluePose(kOffCenterPose, null, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBluePose(kOffCenterPose, FieldVariant.REBUILT_WELDED, null));

    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueHeading(null, FieldVariant.REBUILT_WELDED, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueHeading(kOffCenterPose.getRotation(), null, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueHeading(kOffCenterPose.getRotation(), FieldVariant.REBUILT_WELDED, null));

    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueFieldVelocity(null, FieldVariant.REBUILT_WELDED, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueFieldVelocity(kFieldVelocity, null, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueFieldVelocity(kFieldVelocity, FieldVariant.REBUILT_WELDED, null));

    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueAngularVelocity(0.0, null, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueAngularVelocity(0.0, FieldVariant.REBUILT_WELDED, null));

    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(null, FieldVariant.REBUILT_WELDED, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(createCanonicalTrajectory(), null, Alliance.Blue));
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(createCanonicalTrajectory(), FieldVariant.REBUILT_WELDED, null));
  }

  @Test
  void publicApisRejectNaNAndBothInfinities() {
    for (double nonfiniteValue : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBluePose(
              new Pose2d(nonfiniteValue, 0.0, new Rotation2d()), FieldVariant.REBUILT_WELDED, Alliance.Blue));
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBluePose(
              new Pose2d(0.0, nonfiniteValue, new Rotation2d()), FieldVariant.REBUILT_WELDED, Alliance.Blue));
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBluePose(
              new Pose2d(0.0, 0.0, new Rotation2d(nonfiniteValue)), FieldVariant.REBUILT_WELDED, Alliance.Blue));
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBlueHeading(
              new Rotation2d(nonfiniteValue), FieldVariant.REBUILT_WELDED, Alliance.Blue));
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBlueFieldVelocity(
              new Translation2d(nonfiniteValue, 0.0), FieldVariant.REBUILT_WELDED, Alliance.Blue));
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBlueFieldVelocity(
              new Translation2d(0.0, nonfiniteValue), FieldVariant.REBUILT_WELDED, Alliance.Blue));
      assertThrows(
          IllegalArgumentException.class,
          () -> FieldAllianceTransform.fromCanonicalBlueAngularVelocity(
              nonfiniteValue, FieldVariant.REBUILT_WELDED, Alliance.Blue));
    }
  }

  @Test
  void trajectoryValidationRejectsEveryInvalidTrajectoryCase() {
    assertThrows(
        IllegalArgumentException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(new Trajectory(List.of()), FieldVariant.REBUILT_WELDED, Alliance.Blue));
    assertThrows(
        IllegalArgumentException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(
            new Trajectory(List.of(new Trajectory.State(0.0, 0.0, 0.0, Pose2d.kZero, 0.0))),
            FieldVariant.REBUILT_WELDED,
            Alliance.Blue));
    assertThrows(
        IllegalArgumentException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(
            new Trajectory(List.of(new Trajectory.State(-0.5, 0.0, 0.0, Pose2d.kZero, 0.0))),
            FieldVariant.REBUILT_WELDED,
            Alliance.Blue));
    assertThrows(
        IllegalArgumentException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(
            new Trajectory(
                List.of(
                    new Trajectory.State(0.5, 0.0, 0.0, Pose2d.kZero, 0.0),
                    new Trajectory.State(0.25, 0.0, 0.0, Pose2d.kZero, 0.0))),
            FieldVariant.REBUILT_WELDED,
            Alliance.Blue));

    List<Trajectory.State> nullStateList = new ArrayList<>();
    nullStateList.add(null);
    assertThrows(
        NullPointerException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(
            new Trajectory(nullStateList), FieldVariant.REBUILT_WELDED, Alliance.Blue));

    for (double nonfiniteValue : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertTrajectoryRejected(new Trajectory.State(nonfiniteValue, 0.0, 0.0, Pose2d.kZero, 0.0));
      assertTrajectoryRejected(new Trajectory.State(1.0, nonfiniteValue, 0.0, Pose2d.kZero, 0.0));
      assertTrajectoryRejected(new Trajectory.State(1.0, 0.0, nonfiniteValue, Pose2d.kZero, 0.0));
      assertTrajectoryRejected(new Trajectory.State(1.0, 0.0, 0.0, Pose2d.kZero, nonfiniteValue));
      assertTrajectoryRejected(
          new Trajectory.State(
              1.0, 0.0, 0.0, new Pose2d(nonfiniteValue, 0.0, new Rotation2d()), 0.0));
      assertTrajectoryRejected(
          new Trajectory.State(
              1.0, 0.0, 0.0, new Pose2d(0.0, nonfiniteValue, new Rotation2d()), 0.0));
      assertTrajectoryRejected(
          new Trajectory.State(
              1.0, 0.0, 0.0, new Pose2d(0.0, 0.0, new Rotation2d(nonfiniteValue)), 0.0));
    }
  }

  private static Trajectory createCanonicalTrajectory() {
    return new Trajectory(
        List.of(
            new Trajectory.State(0.0, 0.20, 0.10, kOffCenterPose, 0.15),
            new Trajectory.State(
                0.75,
                0.80,
                -0.20,
                new Pose2d(4.20, 2.30, Rotation2d.fromDegrees(-58.0)),
                -0.35),
            new Trajectory.State(
                1.50,
                0.0,
                -0.40,
                new Pose2d(5.10, 3.40, Rotation2d.fromDegrees(121.0)),
                0.0)));
  }

  private static void assertTrajectoryRejected(Trajectory.State state) {
    assertThrows(
        IllegalArgumentException.class,
        () -> FieldAllianceTransform.fromCanonicalBlueTrajectory(
            new Trajectory(List.of(state)), FieldVariant.REBUILT_WELDED, Alliance.Blue));
  }

  private static void assertTrajectoryDataEquals(Trajectory expected, Trajectory actual) {
    assertEquals(expected.getTotalTimeSeconds(), actual.getTotalTimeSeconds(), kTolerance);
    assertEquals(expected.getStates().size(), actual.getStates().size());
    for (int index = 0; index < expected.getStates().size(); index++) {
      Trajectory.State expectedState = expected.getStates().get(index);
      Trajectory.State actualState = actual.getStates().get(index);
      assertEquals(expectedState.timeSeconds, actualState.timeSeconds);
      assertEquals(expectedState.velocityMetersPerSecond, actualState.velocityMetersPerSecond);
      assertEquals(expectedState.accelerationMetersPerSecondSq, actualState.accelerationMetersPerSecondSq);
      assertEquals(expectedState.curvatureRadPerMeter, actualState.curvatureRadPerMeter);
      assertPoseEquals(expectedState.poseMeters, actualState.poseMeters);
    }
  }

  private static void assertPoseEquals(Pose2d expected, Pose2d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertRotationEquals(expected.getRotation(), actual.getRotation());
  }

  private static void assertTranslationEquals(Translation2d expected, Translation2d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
  }

  private static void assertRotationEquals(Rotation2d expected, Rotation2d actual) {
    assertEquals(
        0.0,
        MathUtil.angleModulus(expected.minus(actual).getRadians()),
        kTolerance);
  }
}
