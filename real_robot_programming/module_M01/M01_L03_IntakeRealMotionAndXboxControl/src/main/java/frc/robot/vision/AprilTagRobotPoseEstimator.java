// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import java.util.Objects;

/**
 * Computes one deterministic robot-pose candidate from one AprilTag geometry relationship.
 *
 * <p>All geometry uses the canonical WPILib Blue-origin field frame, meters, radians, and the
 * right-handed NWU convention. This pure calculator does not evaluate measurement quality,
 * choose between candidates, attach timestamps, or fuse a pose into an estimator.
 */
public final class AprilTagRobotPoseEstimator {
  private AprilTagRobotPoseEstimator() {}

  /**
   * Computes a canonical field-to-robot pose candidate from one known field-to-tag pose, one
   * camera-to-target measurement, and one fixed robot-to-camera extrinsic.
   *
   * <p>The locked geometry is {@code fieldToCamera = fieldToTag.transformBy(
   * cameraToTarget.inverse())}, followed by the frozen L04 recovery boundary {@link
   * VisionFrameTransform#fieldToRobotFromCamera(Pose3d, Transform3d)}.
   *
   * @param fieldToTag canonical field-to-tag pose
   * @param cameraToTarget measured transform from camera frame to target frame
   * @param robotToCamera fixed transform from robot frame to camera frame
   * @return a newly computed canonical field-to-robot candidate
   * @throws NullPointerException when an argument or one of its geometry components is null
   * @throws IllegalArgumentException when an argument, an intermediate, or the result is
   *     nonfinite
   */
  public static Pose3d estimateFieldToRobotCandidate(
      Pose3d fieldToTag, Transform3d cameraToTarget, Transform3d robotToCamera) {
    Pose3d validFieldToTag = requireFinitePose(fieldToTag, "fieldToTag");
    Transform3d validCameraToTarget =
        requireFiniteTransform(cameraToTarget, "cameraToTarget");
    Transform3d validRobotToCamera = requireFiniteTransform(robotToCamera, "robotToCamera");

    Transform3d targetToCamera =
        requireFiniteTransform(validCameraToTarget.inverse(), "targetToCamera");
    Pose3d fieldToCamera =
        requireFinitePose(validFieldToTag.transformBy(targetToCamera), "fieldToCamera result");
    Pose3d fieldToRobot =
        VisionFrameTransform.fieldToRobotFromCamera(fieldToCamera, validRobotToCamera);
    return requireFinitePose(fieldToRobot, "fieldToRobot result");
  }

  private static Pose3d requireFinitePose(Pose3d pose, String name) {
    Pose3d requiredPose = Objects.requireNonNull(pose, name);
    Translation3d translation =
        Objects.requireNonNull(requiredPose.getTranslation(), name + ".translation");
    Rotation3d rotation = Objects.requireNonNull(requiredPose.getRotation(), name + ".rotation");
    requireFiniteTranslation(translation, name);
    requireFiniteRotation(rotation, name);
    return requiredPose;
  }

  private static Transform3d requireFiniteTransform(Transform3d transform, String name) {
    Transform3d requiredTransform = Objects.requireNonNull(transform, name);
    Translation3d translation =
        Objects.requireNonNull(requiredTransform.getTranslation(), name + ".translation");
    Rotation3d rotation =
        Objects.requireNonNull(requiredTransform.getRotation(), name + ".rotation");
    requireFiniteTranslation(translation, name);
    requireFiniteRotation(rotation, name);
    return requiredTransform;
  }

  private static void requireFiniteTranslation(Translation3d translation, String name) {
    if (!Double.isFinite(translation.getX())
        || !Double.isFinite(translation.getY())
        || !Double.isFinite(translation.getZ())) {
      throw new IllegalArgumentException(name + " translation must be finite");
    }
  }

  private static void requireFiniteRotation(Rotation3d rotation, String name) {
    Quaternion quaternion =
        Objects.requireNonNull(rotation.getQuaternion(), name + ".rotation.quaternion");
    if (!Double.isFinite(quaternion.getW())
        || !Double.isFinite(quaternion.getX())
        || !Double.isFinite(quaternion.getY())
        || !Double.isFinite(quaternion.getZ())) {
      throw new IllegalArgumentException(name + " rotation must be finite");
    }
  }
}
