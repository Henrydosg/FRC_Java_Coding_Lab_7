// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import java.util.List;
import java.util.Objects;

/**
 * Immutable, vendor-neutral vision meaning for one coherent logical acquisition sample.
 *
 * <p>Target transforms are target-relative-to-camera values in WPILib right-handed NWU geometry,
 * with translation in meters and rotation in radians. This model does not estimate robot pose,
 * evaluate target quality, or contain timing, telemetry, vendor, or hardware behavior.
 *
 * @param state explicit availability, connection, and structural-validity meaning
 * @param targets immutable target observations in acquisition order
 */
public record VisionObservation(State state, List<TargetObservation> targets) {
  private static final double kMinimumQuaternionNorm = 1.0e-12;

  /** Explicit immutable observation availability and sample states. */
  public enum State {
    UNAVAILABLE,
    DISCONNECTED,
    INVALID_SAMPLE,
    NO_TARGETS,
    TARGETS_PRESENT
  }

  /**
   * Rejects nulls and state/list combinations that would make unavailable or invalid data appear
   * valid.
   */
  public VisionObservation {
    state = Objects.requireNonNull(state, "state");
    targets = List.copyOf(Objects.requireNonNull(targets, "targets"));

    boolean targetsPresent = !targets.isEmpty();
    if (state == State.TARGETS_PRESENT && !targetsPresent) {
      throw new IllegalArgumentException("TARGETS_PRESENT requires at least one target");
    }
    if (state != State.TARGETS_PRESENT && targetsPresent) {
      throw new IllegalArgumentException(state + " must not contain targets");
    }
  }

  /**
   * Immutable target observation with validated AprilTag identity and camera-relative geometry.
   *
   * @param tagId positive AprilTag identity
   * @param cameraToTarget target pose relative to the camera
   */
  public record TargetObservation(int tagId, Transform3d cameraToTarget) {
    /** Validates and defensively owns the camera-relative transform. */
    public TargetObservation {
      if (tagId <= 0) {
        throw new IllegalArgumentException("tagId must be positive");
      }
      cameraToTarget = copyFiniteTransform(cameraToTarget, "cameraToTarget");
    }
  }

  private static Transform3d copyFiniteTransform(Transform3d transform, String name) {
    Transform3d requiredTransform = Objects.requireNonNull(transform, name);
    Translation3d translation =
        Objects.requireNonNull(requiredTransform.getTranslation(), name + ".translation");
    Rotation3d rotation = Objects.requireNonNull(requiredTransform.getRotation(), name + ".rotation");
    Quaternion quaternion =
        Objects.requireNonNull(rotation.getQuaternion(), name + ".rotation.quaternion");

    double xMeters = translation.getX();
    double yMeters = translation.getY();
    double zMeters = translation.getZ();
    double w = quaternion.getW();
    double x = quaternion.getX();
    double y = quaternion.getY();
    double z = quaternion.getZ();
    if (!Double.isFinite(xMeters)
        || !Double.isFinite(yMeters)
        || !Double.isFinite(zMeters)
        || !Double.isFinite(w)
        || !Double.isFinite(x)
        || !Double.isFinite(y)
        || !Double.isFinite(z)) {
      throw new IllegalArgumentException(name + " values must be finite");
    }

    double quaternionNorm = Math.hypot(Math.hypot(w, x), Math.hypot(y, z));
    if (quaternionNorm <= kMinimumQuaternionNorm) {
      throw new IllegalArgumentException(name + " quaternion norm must be valid");
    }

    return new Transform3d(
        new Translation3d(xMeters, yMeters, zMeters), new Rotation3d(new Quaternion(w, x, y, z)));
  }
}
