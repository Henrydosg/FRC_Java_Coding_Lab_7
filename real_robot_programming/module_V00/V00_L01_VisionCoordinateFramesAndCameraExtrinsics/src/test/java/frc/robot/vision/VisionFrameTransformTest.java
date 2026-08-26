// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.vision;

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
  void identityTransformLeavesRobotPoseUnchanged() {
    Pose3d fieldToRobot =
        new Pose3d(2.0, -1.5, 0.25, new Rotation3d(0.10, -0.20, 0.70));

    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, Transform3d.kZero);

    assertPoseEquals(fieldToRobot, fieldToCamera);
  }

  @Test
  void translationOnlyExtrinsicIsComposedInRobotFrame() {
    Pose3d fieldToRobot = new Pose3d(2.0, 3.0, 0.10, Rotation3d.kZero);
    Transform3d robotToCamera = new Transform3d(0.25, 0.30, 0.60, Rotation3d.kZero);

    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(new Pose3d(2.25, 3.30, 0.70, Rotation3d.kZero), fieldToCamera);
  }

  @Test
  void rotatedRobotFrameUsesIndependentNumericTranslationOracle() {
    Pose3d fieldToRobot =
        new Pose3d(1.0, 2.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d robotToCamera = new Transform3d(1.0, 0.0, 0.0, Rotation3d.kZero);

    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertEquals(1.0, fieldToCamera.getX(), kTolerance);
    assertEquals(3.0, fieldToCamera.getY(), kTolerance);
    assertEquals(0.0, fieldToCamera.getZ(), kTolerance);
    assertEquals(0.0, fieldToCamera.getRotation().getX(), kTolerance);
    assertEquals(0.0, fieldToCamera.getRotation().getY(), kTolerance);
    assertEquals(Math.PI / 2.0, fieldToCamera.getRotation().getZ(), kTolerance);
  }

  @Test
  void rotationOnlyExtrinsicIsPreserved() {
    Rotation3d cameraRotation = new Rotation3d(0.20, -0.30, 0.40);

    Pose3d fieldToCamera =
        VisionFrameTransform.fieldToCamera(
            Pose3d.kZero, new Transform3d(0.0, 0.0, 0.0, cameraRotation));

    assertRotationEquals(cameraRotation, fieldToCamera.getRotation());
    assertEquals(0.0, fieldToCamera.getX(), kTolerance);
    assertEquals(0.0, fieldToCamera.getY(), kTolerance);
    assertEquals(0.0, fieldToCamera.getZ(), kTolerance);
  }

  @Test
  void combinedTranslationAndRotationUsesLockedComposition() {
    Pose3d fieldToRobot =
        new Pose3d(1.0, 2.0, 0.15, new Rotation3d(-0.10, 0.05, Math.PI / 2.0));
    Transform3d robotToCamera =
        new Transform3d(0.50, 0.20, 0.30, new Rotation3d(0.10, -0.15, 0.25));

    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(fieldToRobot.transformBy(robotToCamera), fieldToCamera);
  }

  @Test
  void cameraToRobotIsTheInverseOfRobotToCamera() {
    Transform3d robotToCamera =
        new Transform3d(0.42, -0.18, 0.77, new Rotation3d(0.12, -0.24, 0.31));

    Transform3d cameraToRobot = VisionFrameTransform.cameraToRobot(robotToCamera);

    assertTransformEquals(robotToCamera.inverse(), cameraToRobot);
  }

  @Test
  void cameraToRobotUsesIndependentNumericInverseOracle() {
    Transform3d robotToCamera =
        new Transform3d(1.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));

    Transform3d cameraToRobot = VisionFrameTransform.cameraToRobot(robotToCamera);

    assertEquals(0.0, cameraToRobot.getX(), kTolerance);
    assertEquals(1.0, cameraToRobot.getY(), kTolerance);
    assertEquals(0.0, cameraToRobot.getZ(), kTolerance);
    assertEquals(0.0, cameraToRobot.getRotation().getX(), kTolerance);
    assertEquals(0.0, cameraToRobot.getRotation().getY(), kTolerance);
    assertEquals(-Math.PI / 2.0, cameraToRobot.getRotation().getZ(), kTolerance);
  }

  @Test
  void forwardAndReverseTransformsRoundTrip() {
    Pose3d fieldToRobot =
        new Pose3d(4.2, 1.7, 0.15, new Rotation3d(-0.10, 0.05, 1.10));
    Transform3d robotToCamera =
        new Transform3d(0.42, -0.18, 0.77, new Rotation3d(0.12, -0.24, 0.31));

    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    Pose3d recoveredFieldToRobot =
        VisionFrameTransform.fieldToRobotFromCamera(fieldToCamera, robotToCamera);

    assertPoseEquals(fieldToRobot, recoveredFieldToRobot);
  }

  @Test
  void reverseReconstructionUsesIndependentNumericPoseOracle() {
    Pose3d fieldToCamera =
        new Pose3d(1.0, 3.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d robotToCamera =
        new Transform3d(1.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));

    Pose3d fieldToRobot =
        VisionFrameTransform.fieldToRobotFromCamera(fieldToCamera, robotToCamera);

    assertEquals(0.0, fieldToRobot.getX(), kTolerance);
    assertEquals(3.0, fieldToRobot.getY(), kTolerance);
    assertEquals(0.0, fieldToRobot.getZ(), kTolerance);
    assertEquals(0.0, fieldToRobot.getRotation().getX(), kTolerance);
    assertEquals(0.0, fieldToRobot.getRotation().getY(), kTolerance);
    assertEquals(0.0, fieldToRobot.getRotation().getZ(), kTolerance);
  }

  @Test
  void compositionMatchesDirectTransformBy() {
    Pose3d fieldToRobot =
        new Pose3d(2.0, -1.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d robotToCamera =
        new Transform3d(0.50, 0.20, 0.30, new Rotation3d(0.0, 0.0, 0.25));

    Pose3d expected = fieldToRobot.transformBy(robotToCamera);
    Pose3d actual = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);

    assertPoseEquals(expected, actual);
  }

  @Test
  void transformCompositionIsNoncommutative() {
    Transform3d first = new Transform3d(1.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d second = new Transform3d(0.0, 1.0, 0.0, Rotation3d.kZero);

    Pose3d firstThenSecond = Pose3d.kZero.transformBy(first).transformBy(second);
    Pose3d secondThenFirst = Pose3d.kZero.transformBy(second).transformBy(first);

    assertNotEquals(firstThenSecond.getX(), secondThenFirst.getX(), kTolerance);
    assertNotEquals(firstThenSecond.getY(), secondThenFirst.getY(), kTolerance);
  }

  @Test
  void nwuAxisSignsAreForwardLeftAndUp() {
    Pose3d result =
        VisionFrameTransform.fieldToCamera(
            Pose3d.kZero, new Transform3d(0.80, 0.55, 0.93, Rotation3d.kZero));

    assertEquals(0.80, result.getX(), kTolerance);
    assertEquals(0.55, result.getY(), kTolerance);
    assertEquals(0.93, result.getZ(), kTolerance);
  }

  @Test
  void translationsUseMetersWithoutConversion() {
    Transform3d robotToCamera =
        new Transform3d(1.234, -0.567, 0.891, Rotation3d.kZero);

    Pose3d result = VisionFrameTransform.fieldToCamera(Pose3d.kZero, robotToCamera);

    assertEquals(1.234, result.getX(), kTolerance);
    assertEquals(-0.567, result.getY(), kTolerance);
    assertEquals(0.891, result.getZ(), kTolerance);
  }

  @Test
  void rotationsUseRadiansWithoutConversion() {
    double yawRadians = Math.PI / 3.0;
    Transform3d robotToCamera =
        new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, yawRadians));

    Pose3d result = VisionFrameTransform.fieldToCamera(Pose3d.kZero, robotToCamera);

    assertEquals(yawRadians, result.getRotation().getZ(), kTolerance);
  }

  @Test
  void rejectsNullPoseInputs() {
    Transform3d robotToCamera = new Transform3d(0.2, 0.1, 0.7, Rotation3d.kZero);

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
    for (double nonfinite : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.fieldToCamera(
                  new Pose3d(nonfinite, 0.0, 0.0, Rotation3d.kZero), Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.fieldToCamera(
                  Pose3d.kZero, new Transform3d(0.0, nonfinite, 0.0, Rotation3d.kZero)));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              VisionFrameTransform.cameraToRobot(
                  new Transform3d(0.0, 0.0, nonfinite, Rotation3d.kZero)));
    }
  }

  @Test
  void rejectsNonfiniteRotations() {
    for (double nonfinite : List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
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
  void rejectsNonfiniteComputedPoseResult() {
    Pose3d fieldToRobot = new Pose3d(Double.MAX_VALUE, 0.0, 0.0, Rotation3d.kZero);
    Transform3d robotToCamera =
        new Transform3d(Double.MAX_VALUE, 0.0, 0.0, Rotation3d.kZero);

    assertThrows(
        IllegalArgumentException.class,
        () -> VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera));
  }

  @Test
  void repeatedCallsAreDeterministicAndReturnFreshResults() {
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
  void operationsDoNotMutateCallerOwnedGeometry() {
    Pose3d fieldToRobot =
        new Pose3d(2.7, 1.4, 0.25, new Rotation3d(0.11, -0.07, 0.62));
    Transform3d robotToCamera =
        new Transform3d(0.31, -0.19, 0.68, new Rotation3d(-0.09, 0.16, -0.28));
    Pose3d originalFieldToRobot =
        new Pose3d(2.7, 1.4, 0.25, new Rotation3d(0.11, -0.07, 0.62));
    Transform3d originalRobotToCamera =
        new Transform3d(0.31, -0.19, 0.68, new Rotation3d(-0.09, 0.16, -0.28));

    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
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
