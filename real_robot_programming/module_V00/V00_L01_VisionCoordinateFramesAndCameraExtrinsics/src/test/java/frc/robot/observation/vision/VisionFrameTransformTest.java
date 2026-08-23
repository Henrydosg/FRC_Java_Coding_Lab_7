// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies the pure V00_L01 coordinate-frame and camera-extrinsic contract. */
class VisionFrameTransformTest {
  private static final double kTolerance = 1.0e-9;

  @Test
  void identityRobotPoseAppliesSimpleCameraTranslation() {
    Transform3d robotToCamera =
        new Transform3d(0.40, -0.15, 0.72, Rotation3d.kZero);

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(Pose3d.kZero, robotToCamera);

    assertPoseEquals(new Pose3d(0.40, -0.15, 0.72, Rotation3d.kZero), fieldToCamera);
  }

  @Test
  void translatedRobotAddsCameraTranslationInRobotFrame() {
    Pose3d fieldToRobot = new Pose3d(2.0, 3.0, 0.10, Rotation3d.kZero);
    Transform3d robotToCamera =
        new Transform3d(0.25, 0.30, 0.60, Rotation3d.kZero);

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(new Pose3d(2.25, 3.30, 0.70, Rotation3d.kZero), fieldToCamera);
  }

  @Test
  void yawedRobotRotatesCameraOffsetBeforeFieldTranslation() {
    Pose3d fieldToRobot =
        new Pose3d(1.0, 2.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d robotToCamera =
        new Transform3d(1.0, 0.0, 0.0, Rotation3d.kZero);

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(
        new Pose3d(1.0, 3.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0)),
        fieldToCamera);
  }

  @Test
  void preservesNonzeroCameraRollPitchAndYaw() {
    Rotation3d cameraRotation = new Rotation3d(0.20, -0.30, 0.40);
    Transform3d robotToCamera = new Transform3d(0.35, -0.22, 0.61, cameraRotation);

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(Pose3d.kZero, robotToCamera);

    assertRotationEquals(cameraRotation, fieldToCamera.getRotation());
  }

  @Test
  void robotToCameraThenCameraToRobotRoundTrips() {
    Pose3d fieldToRobot =
        new Pose3d(4.2, 1.7, 0.15, new Rotation3d(-0.10, 0.05, 1.10));
    Transform3d robotToCamera =
        new Transform3d(0.42, -0.18, 0.77, new Rotation3d(0.12, -0.24, 0.31));

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    Pose3d recoveredFieldToRobot =
        VisionFrameTransform.fieldToRobotFromCamera(fieldToCamera, robotToCamera);

    assertPoseEquals(fieldToRobot, recoveredFieldToRobot);
  }

  @Test
  void fieldToRobotThenRobotToCameraUsesLockedCompositionOrder() {
    Pose3d fieldToRobot =
        new Pose3d(2.0, -1.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d robotToCamera =
        new Transform3d(0.50, 0.20, 0.30, new Rotation3d(0.0, 0.0, 0.25));

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(fieldToRobot.transformBy(robotToCamera), fieldToCamera);
    assertEquals(1.80, fieldToCamera.getX(), kTolerance);
    assertEquals(-0.50, fieldToCamera.getY(), kTolerance);
  }

  @Test
  void reverseRecoveryDoesNotApplyRobotToCameraInTheForwardDirection() {
    Pose3d fieldToRobot =
        new Pose3d(1.2, 2.4, 0.0, new Rotation3d(0.0, 0.0, 0.70));
    Transform3d robotToCamera =
        new Transform3d(0.45, -0.16, 0.65, new Rotation3d(0.05, -0.12, 0.18));
    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    Pose3d recovered =
        VisionFrameTransform.fieldToRobotFromCamera(fieldToCamera, robotToCamera);
    Pose3d commonDirectionMisuse = fieldToCamera.transformBy(robotToCamera);

    assertPoseEquals(fieldToRobot, recovered);
    assertNotEquals(fieldToRobot, commonDirectionMisuse);
  }

  @Test
  void nwuPositiveXIsForward() {
    Pose3d result =
        VisionFrameTransform.fieldToCamera(
            Pose3d.kZero, new Transform3d(0.80, 0.0, 0.0, Rotation3d.kZero));

    assertEquals(0.80, result.getX(), kTolerance);
    assertEquals(0.0, result.getY(), kTolerance);
    assertEquals(0.0, result.getZ(), kTolerance);
  }

  @Test
  void nwuPositiveYIsLeft() {
    Pose3d result =
        VisionFrameTransform.fieldToCamera(
            Pose3d.kZero, new Transform3d(0.0, 0.55, 0.0, Rotation3d.kZero));

    assertEquals(0.0, result.getX(), kTolerance);
    assertEquals(0.55, result.getY(), kTolerance);
    assertEquals(0.0, result.getZ(), kTolerance);
  }

  @Test
  void nwuPositiveZIsUp() {
    Pose3d result =
        VisionFrameTransform.fieldToCamera(
            Pose3d.kZero, new Transform3d(0.0, 0.0, 0.93, Rotation3d.kZero));

    assertEquals(0.0, result.getX(), kTolerance);
    assertEquals(0.0, result.getY(), kTolerance);
    assertEquals(0.93, result.getZ(), kTolerance);
  }

  @Test
  void preservesRadiansWithoutDegreeConversion() {
    double yawRadians = Math.PI / 3.0;
    Transform3d robotToCamera =
        new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, yawRadians));

    Pose3d result = VisionFrameTransform.fieldToCamera(Pose3d.kZero, robotToCamera);

    assertEquals(yawRadians, result.getRotation().getZ(), kTolerance);
  }

  @Test
  void preservesMetersWithoutLengthConversion() {
    Transform3d robotToCamera =
        new Transform3d(1.234, -0.567, 0.891, Rotation3d.kZero);

    Pose3d result = VisionFrameTransform.fieldToCamera(Pose3d.kZero, robotToCamera);

    assertEquals(1.234, result.getX(), kTolerance);
    assertEquals(-0.567, result.getY(), kTolerance);
    assertEquals(0.891, result.getZ(), kTolerance);
  }

  @Test
  void rejectsNullPoseInputs() {
    Transform3d robotToCamera =
        new Transform3d(0.2, 0.1, 0.7, Rotation3d.kZero);

    assertThrows(
        NullPointerException.class,
        () -> VisionFrameTransform.fieldToCamera(null, robotToCamera));
    assertThrows(
        NullPointerException.class,
        () -> VisionFrameTransform.fieldToRobotFromCamera(null, robotToCamera));
  }

  @Test
  void rejectsNullTransformInputs() {
    assertThrows(
        NullPointerException.class,
        () -> VisionFrameTransform.fieldToCamera(Pose3d.kZero, null));
    assertThrows(NullPointerException.class, () -> VisionFrameTransform.cameraToRobot(null));
    assertThrows(
        NullPointerException.class,
        () -> VisionFrameTransform.fieldToRobotFromCamera(Pose3d.kZero, null));
  }

  @Test
  void rejectsNonfiniteTranslations() {
    for (double nonfinite :
        List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.fieldToCamera(
                  new Pose3d(nonfinite, 0.0, 0.0, Rotation3d.kZero), Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.fieldToCamera(
                  Pose3d.kZero,
                  new Transform3d(0.0, nonfinite, 0.0, Rotation3d.kZero)));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.cameraToRobot(
                  new Transform3d(0.0, 0.0, nonfinite, Rotation3d.kZero)));
    }
  }

  @Test
  void rejectsNonfiniteRotations() {
    for (double nonfinite :
        List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.fieldToCamera(
                  new Pose3d(0.0, 0.0, 0.0, new Rotation3d(nonfinite, 0.0, 0.0)),
                  Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.fieldToCamera(
                  Pose3d.kZero,
                  new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, nonfinite, 0.0))));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.cameraToRobot(
                  new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, nonfinite))));
    }
  }

  @Test
  void repeatedCallsAreDeterministic() {
    Pose3d fieldToRobot =
        new Pose3d(3.1, -0.8, 0.2, new Rotation3d(0.04, -0.08, 0.90));
    Transform3d robotToCamera =
        new Transform3d(0.38, 0.12, 0.73, new Rotation3d(-0.14, 0.22, -0.35));

    Pose3d first = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    Pose3d second = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(first, second);
    assertNotSame(first, second);
  }

  @Test
  void operationsDoNotMutateCallerInputs() {
    Pose3d fieldToRobot =
        new Pose3d(2.7, 1.4, 0.25, new Rotation3d(0.11, -0.07, 0.62));
    Transform3d robotToCamera =
        new Transform3d(0.31, -0.19, 0.68, new Rotation3d(-0.09, 0.16, -0.28));
    Pose3d originalFieldToRobot =
        new Pose3d(2.7, 1.4, 0.25, new Rotation3d(0.11, -0.07, 0.62));
    Transform3d originalRobotToCamera =
        new Transform3d(0.31, -0.19, 0.68, new Rotation3d(-0.09, 0.16, -0.28));

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    VisionFrameTransform.cameraToRobot(robotToCamera);
    VisionFrameTransform.fieldToRobotFromCamera(fieldToCamera, robotToCamera);

    assertPoseEquals(originalFieldToRobot, fieldToRobot);
    assertTransformEquals(originalRobotToCamera, robotToCamera);
  }

  private static void assertPoseEquals(Pose3d expected, Pose3d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(expected.getZ(), actual.getZ(), kTolerance);
    assertRotationEquals(expected.getRotation(), actual.getRotation());
  }

  private static void assertTransformEquals(Transform3d expected, Transform3d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(expected.getZ(), actual.getZ(), kTolerance);
    assertRotationEquals(expected.getRotation(), actual.getRotation());
  }

  private static void assertRotationEquals(Rotation3d expected, Rotation3d actual) {
    assertEquals(0.0, expected.minus(actual).getAngle(), kTolerance);
  }
}
