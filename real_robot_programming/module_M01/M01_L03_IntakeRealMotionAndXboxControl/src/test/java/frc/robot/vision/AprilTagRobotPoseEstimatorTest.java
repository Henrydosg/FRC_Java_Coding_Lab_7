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
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.Constants;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIOSim;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Verifies the pure V00_L05 AprilTag robot-pose-candidate calculation contract. */
class AprilTagRobotPoseEstimatorTest {
  private static final double kTolerance = 1.0e-9;

  @Test
  void identityGeometryReturnsTheFieldToTagPose() {
    Pose3d fieldToTag = new Pose3d(2.0, -1.5, 0.25, new Rotation3d(0.10, -0.20, 0.70));

    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, Transform3d.kZero, Transform3d.kZero);

    assertPoseEquals(fieldToTag, candidate);
  }

  @Test
  void translationOnlyGeometryUsesTheLockedInverseOrder() {
    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            new Pose3d(5.0, 4.0, 2.0, Rotation3d.kZero),
            new Transform3d(1.2, -0.8, 0.4, Rotation3d.kZero),
            new Transform3d(0.3, 0.5, 0.7, Rotation3d.kZero));

    assertPoseEquals(new Pose3d(3.5, 4.3, 0.9, Rotation3d.kZero), candidate);
  }

  @Test
  void rotationOnlyGeometryRecoversTheRobotRotation() {
    Pose3d fieldToRobot = new Pose3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, 0.35));
    Transform3d robotToCamera =
        new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, 0.20));
    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    Pose3d fieldToTag =
        fieldToCamera.transformBy(new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, -0.10)));
    Transform3d cameraToTarget = new Transform3d(fieldToCamera, fieldToTag);

    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);

    assertPoseEquals(fieldToRobot, candidate);
  }

  @Test
  void combinedTranslationAndRotationRoundTrips() {
    Pose3d fieldToRobot =
        new Pose3d(3.4, -1.2, 0.35, new Rotation3d(-0.18, 0.27, 1.03));
    Transform3d robotToCamera =
        new Transform3d(0.42, -0.16, 0.71, new Rotation3d(0.12, -0.21, 0.33));
    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    Pose3d fieldToTag =
        new Pose3d(6.3, 1.8, 1.15, new Rotation3d(0.07, -0.16, -0.72));
    Transform3d cameraToTarget = new Transform3d(fieldToCamera, fieldToTag);

    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);

    assertPoseEquals(fieldToRobot, candidate);
  }

  @Test
  void transformCompositionIsNoncommutative() {
    Pose3d fieldToTag =
        new Pose3d(2.0, 1.0, 0.5, new Rotation3d(0.0, 0.0, Math.PI / 2.0));
    Transform3d cameraToTarget =
        new Transform3d(-1.0, 0.0, 0.0, new Rotation3d(-Math.PI / 2.0, 0.0, 0.0));
    Transform3d robotToCamera =
        new Transform3d(-1.0, 0.0, -1.0, new Rotation3d(0.0, 0.0, -Math.PI / 2.0));

    Pose3d lockedOrder =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);
    Pose3d incorrectOrder =
        fieldToTag
            .transformBy(robotToCamera.inverse())
            .transformBy(cameraToTarget.inverse());

    assertNotEquals(incorrectOrder.getX(), lockedOrder.getX(), kTolerance);
    assertNotEquals(incorrectOrder.getY(), lockedOrder.getY(), kTolerance);
  }

  @Test
  void independentNontrivialThreeDimensionalOracleMatches() {
    Pose3d fieldToTag =
        new Pose3d(4.1, -1.7, 0.6, new Rotation3d(0.31, -0.27, 0.83));
    Transform3d cameraToTarget =
        new Transform3d(1.2, -0.8, 0.45, new Rotation3d(-0.22, 0.41, -0.36));
    Transform3d robotToCamera =
        new Transform3d(0.35, 0.18, 0.72, new Rotation3d(0.14, -0.19, 0.28));

    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);

    // Expected values were calculated independently from standard quaternion rigid-transform algebra.
    assertEquals(2.920178549473229, candidate.getX(), kTolerance);
    assertEquals(-1.722130009124985, candidate.getY(), kTolerance);
    assertEquals(-1.158639550571819, candidate.getZ(), kTolerance);
    assertEquals(0.870346365440157, candidate.getRotation().getQuaternion().getW(), kTolerance);
    assertEquals(0.255541091823834, candidate.getRotation().getQuaternion().getX(), kTolerance);
    assertEquals(-0.176480146644603, candidate.getRotation().getQuaternion().getY(), kTolerance);
    assertEquals(0.382165817931027, candidate.getRotation().getQuaternion().getZ(), kTolerance);
  }

  @Test
  void l04ForwardGeometryRoundTripsThroughTheL05Calculator() {
    AprilTagFieldLayoutContract layout =
        AprilTagFieldLayoutContract.loadOfficial2026(
            Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
    int tagId = 1;
    Pose3d fieldToTag = layout.getTagPose(tagId).orElseThrow();
    Pose3d fieldToRobot =
        new Pose3d(2.6, 1.4, 0.35, new Rotation3d(-0.12, 0.08, 0.91));
    Transform3d robotToCamera =
        new Transform3d(0.38, -0.17, 0.73, new Rotation3d(0.11, -0.16, 0.29));
    VisionIOSim simulator = new VisionIOSim(layout, robotToCamera);
    VisionIOInputs inputs = new VisionIOInputs();
    simulator.setFrame(VisionIOSim.Frame.targetsPresent(fieldToRobot, List.of(tagId)));
    simulator.updateInputs(inputs);

    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, inputs.targets.get(0).cameraToTarget(), robotToCamera);

    assertPoseEquals(fieldToRobot, candidate);
  }

  @Test
  void nonzeroExtrinsicIsRequiredForCorrectRecovery() {
    Pose3d fieldToRobot =
        new Pose3d(1.9, -0.6, 0.25, new Rotation3d(0.09, -0.14, 0.74));
    Transform3d robotToCamera =
        new Transform3d(0.46, 0.18, 0.69, new Rotation3d(-0.10, 0.22, -0.31));
    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    Pose3d fieldToTag =
        new Pose3d(5.8, 2.1, 1.02, new Rotation3d(0.04, -0.08, -0.48));
    Transform3d cameraToTarget = new Transform3d(fieldToCamera, fieldToTag);

    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);
    Pose3d missingExtrinsicCandidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, Transform3d.kZero);

    assertPoseEquals(fieldToRobot, candidate);
    assertNotEquals(fieldToRobot.getX(), missingExtrinsicCandidate.getX(), kTolerance);
  }

  @Test
  void independentTargetsCanRecoverTheSameRobotPose() {
    Pose3d fieldToRobot =
        new Pose3d(3.0, 1.1, 0.32, new Rotation3d(-0.05, 0.16, 1.21));
    Transform3d robotToCamera =
        new Transform3d(0.34, -0.20, 0.76, new Rotation3d(0.10, -0.13, 0.24));
    Pose3d fieldToCamera = VisionFrameTransform.fieldToCamera(fieldToRobot, robotToCamera);
    List<Pose3d> fieldToTags =
        List.of(
            new Pose3d(1.2, 6.4, 1.1, new Rotation3d(0.0, 0.0, -1.2)),
            new Pose3d(7.3, -0.8, 0.9, new Rotation3d(0.15, -0.10, 0.65)));

    for (Pose3d fieldToTag : fieldToTags) {
      Pose3d candidate =
          AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
              fieldToTag, new Transform3d(fieldToCamera, fieldToTag), robotToCamera);
      assertPoseEquals(fieldToRobot, candidate);
    }
  }

  @Test
  void geometryUsesMetersRadiansAndNwuWithoutConversion() {
    Pose3d candidate =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            new Pose3d(2.345, -1.234, 0.876, new Rotation3d(0.0, 0.0, Math.PI / 3.0)),
            Transform3d.kZero,
            Transform3d.kZero);

    assertEquals(2.345, candidate.getX(), kTolerance);
    assertEquals(-1.234, candidate.getY(), kTolerance);
    assertEquals(0.876, candidate.getZ(), kTolerance);
    assertEquals(Math.PI / 3.0, candidate.getRotation().getZ(), kTolerance);
  }

  @Test
  void repeatedCallsAreDeterministicAndReturnFreshResults() {
    Pose3d fieldToTag =
        new Pose3d(4.8, 0.7, 0.8, new Rotation3d(-0.16, 0.13, 0.88));
    Transform3d cameraToTarget =
        new Transform3d(1.4, -0.3, 0.5, new Rotation3d(0.17, -0.20, 0.11));
    Transform3d robotToCamera =
        new Transform3d(0.36, 0.12, 0.70, new Rotation3d(-0.08, 0.18, -0.26));

    Pose3d first =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);
    Pose3d second =
        AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
            fieldToTag, cameraToTarget, robotToCamera);

    assertPoseEquals(first, second);
    assertNotSame(first, second);
  }

  @Test
  void calculationDoesNotMutateCallerOwnedGeometry() {
    Pose3d fieldToTag =
        new Pose3d(4.5, -0.2, 0.7, new Rotation3d(0.13, -0.21, 0.66));
    Transform3d cameraToTarget =
        new Transform3d(1.1, 0.4, -0.2, new Rotation3d(-0.12, 0.17, -0.23));
    Transform3d robotToCamera =
        new Transform3d(0.39, -0.14, 0.75, new Rotation3d(0.07, -0.11, 0.30));
    Pose3d originalFieldToTag =
        new Pose3d(4.5, -0.2, 0.7, new Rotation3d(0.13, -0.21, 0.66));
    Transform3d originalCameraToTarget =
        new Transform3d(1.1, 0.4, -0.2, new Rotation3d(-0.12, 0.17, -0.23));
    Transform3d originalRobotToCamera =
        new Transform3d(0.39, -0.14, 0.75, new Rotation3d(0.07, -0.11, 0.30));

    AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
        fieldToTag, cameraToTarget, robotToCamera);

    assertPoseEquals(originalFieldToTag, fieldToTag);
    assertTransformEquals(originalCameraToTarget, cameraToTarget);
    assertTransformEquals(originalRobotToCamera, robotToCamera);
  }

  @Test
  void rejectsNullInputs() {
    assertThrows(
        NullPointerException.class,
        () ->
            AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                null, Transform3d.kZero, Transform3d.kZero));
    assertThrows(
        NullPointerException.class,
        () ->
            AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                Pose3d.kZero, null, Transform3d.kZero));
    assertThrows(
        NullPointerException.class,
        () ->
            AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                Pose3d.kZero, Transform3d.kZero, null));
  }

  @Test
  void rejectsNonfiniteInputTranslations() {
    for (double nonfinite :
        List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                  new Pose3d(nonfinite, 0.0, 0.0, Rotation3d.kZero),
                  Transform3d.kZero,
                  Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                  Pose3d.kZero,
                  new Transform3d(0.0, nonfinite, 0.0, Rotation3d.kZero),
                  Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                  Pose3d.kZero,
                  Transform3d.kZero,
                  new Transform3d(0.0, 0.0, nonfinite, Rotation3d.kZero)));
    }
  }

  @Test
  void rejectsNonfiniteInputRotations() {
    for (double nonfinite :
        List.of(Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY)) {
      assertThrows(
          IllegalArgumentException.class,
          () ->
              AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                  new Pose3d(0.0, 0.0, 0.0, new Rotation3d(nonfinite, 0.0, 0.0)),
                  Transform3d.kZero,
                  Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                  Pose3d.kZero,
                  new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, nonfinite, 0.0)),
                  Transform3d.kZero));
      assertThrows(
          IllegalArgumentException.class,
          () ->
              AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                  Pose3d.kZero,
                  Transform3d.kZero,
                  new Transform3d(0.0, 0.0, 0.0, new Rotation3d(0.0, 0.0, nonfinite))));
    }
  }

  @Test
  void rejectsNonfiniteComputedResult() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            AprilTagRobotPoseEstimator.estimateFieldToRobotCandidate(
                new Pose3d(Double.MAX_VALUE, 0.0, 0.0, Rotation3d.kZero),
                new Transform3d(-Double.MAX_VALUE, 0.0, 0.0, Rotation3d.kZero),
                Transform3d.kZero));
  }

  @Test
  void publicApiContainsOnlyTheLockedStaticCalculator() throws NoSuchMethodException {
    List<Method> publicDeclaredMethods =
        Arrays.stream(AprilTagRobotPoseEstimator.class.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .collect(Collectors.toList());
    Method method =
        AprilTagRobotPoseEstimator.class.getDeclaredMethod(
            "estimateFieldToRobotCandidate", Pose3d.class, Transform3d.class, Transform3d.class);
    Constructor<?>[] constructors = AprilTagRobotPoseEstimator.class.getDeclaredConstructors();

    assertTrue(Modifier.isFinal(AprilTagRobotPoseEstimator.class.getModifiers()));
    assertEquals(1, publicDeclaredMethods.size());
    assertEquals(method, publicDeclaredMethods.get(0));
    assertEquals("estimateFieldToRobotCandidate", method.getName());
    assertTrue(Modifier.isStatic(method.getModifiers()));
    assertEquals(Pose3d.class, method.getReturnType());
    assertEquals(1, constructors.length);
    assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
    assertEquals(0, constructors[0].getParameterCount());
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
