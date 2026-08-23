// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import java.util.Objects;

/**
 * Provides pure, vendor-neutral coordinate-frame operations for one camera mounting transform.
 *
 * <p>All translations use meters. All rotations use WPILib radians and the NWU convention:
 * positive X is forward, positive Y is left, and positive Z is up.
 */
public final class VisionFrameTransform {
  private VisionFrameTransform() {}

  /**
   * Computes the camera pose in the canonical field frame.
   *
   * <p>The fixed {@code robotToCamera} mounting transform is applied in the robot pose frame:
   * {@code fieldToCamera = fieldToRobot.transformBy(robotToCamera)}.
   *
   * @param fieldToRobot robot pose in the canonical field frame
   * @param robotToCamera camera mounting transform from robot frame to camera frame
   * @return camera pose in the canonical field frame
   * @throws NullPointerException when either argument or one of its geometry components is null
   * @throws IllegalArgumentException when either argument contains a nonfinite value
   */
  public static Pose3d fieldToCamera(Pose3d fieldToRobot, Transform3d robotToCamera) {
    Pose3d validFieldToRobot = requireFinitePose(fieldToRobot, "fieldToRobot");
    Transform3d validRobotToCamera =
        requireFiniteTransform(robotToCamera, "robotToCamera");
    return requireFinitePose(
        validFieldToRobot.transformBy(validRobotToCamera), "fieldToCamera result");
  }

  /**
   * Returns the inverse of the fixed robot-to-camera mounting transform.
   *
   * @param robotToCamera camera mounting transform from robot frame to camera frame
   * @return mounting transform from camera frame to robot frame
   * @throws NullPointerException when the argument or one of its geometry components is null
   * @throws IllegalArgumentException when the argument contains a nonfinite value
   */
  public static Transform3d cameraToRobot(Transform3d robotToCamera) {
    Transform3d validRobotToCamera =
        requireFiniteTransform(robotToCamera, "robotToCamera");
    return requireFiniteTransform(validRobotToCamera.inverse(), "cameraToRobot result");
  }

  /**
   * Recovers the robot pose in the canonical field frame from a camera pose and mounting transform.
   *
   * <p>This applies {@link #cameraToRobot(Transform3d)} in the camera pose frame.
   *
   * @param fieldToCamera camera pose in the canonical field frame
   * @param robotToCamera camera mounting transform from robot frame to camera frame
   * @return robot pose in the canonical field frame
   * @throws NullPointerException when either argument or one of its geometry components is null
   * @throws IllegalArgumentException when either argument contains a nonfinite value
   */
  public static Pose3d fieldToRobotFromCamera(
      Pose3d fieldToCamera, Transform3d robotToCamera) {
    Pose3d validFieldToCamera = requireFinitePose(fieldToCamera, "fieldToCamera");
    Pose3d fieldToRobot = validFieldToCamera.transformBy(cameraToRobot(robotToCamera));
    return requireFinitePose(fieldToRobot, "fieldToRobot result");
  }

  private static Pose3d requireFinitePose(Pose3d pose, String name) {
    Pose3d requiredPose = Objects.requireNonNull(pose, name);
    Translation3d translation =
        Objects.requireNonNull(requiredPose.getTranslation(), name + ".translation");
    Rotation3d rotation =
        Objects.requireNonNull(requiredPose.getRotation(), name + ".rotation");
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
