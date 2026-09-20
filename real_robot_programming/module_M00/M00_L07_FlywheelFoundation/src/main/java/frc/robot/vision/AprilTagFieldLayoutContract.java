// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.vision;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Provides immutable canonical field-to-AprilTag poses from an official 2026 WPILib layout.
 *
 * <p>Returned poses use the canonical WPILib Blue-origin field frame, meters, radians, and the
 * right-handed NWU convention. This contract does not apply an alliance transform or retain the
 * mutable WPILib field layout and tag objects used during loading.
 */
public final class AprilTagFieldLayoutContract {
  private final Map<Integer, Pose3d> fieldToTagById;

  private AprilTagFieldLayoutContract(Map<Integer, Pose3d> fieldToTagById) {
    this.fieldToTagById = Map.copyOf(fieldToTagById);
  }

  /**
   * Loads and snapshots one explicitly selected official WPILib 2026 field layout.
   *
   * @param fieldVariant physical 2026 rebuilt-field construction variant
   * @return immutable canonical field-to-tag reference contract
   * @throws NullPointerException when {@code fieldVariant} is null
   * @throws IllegalStateException when official field data is malformed or inconsistent with the
   *     selected field variant
   */
  public static AprilTagFieldLayoutContract loadOfficial2026(
      Constants.FieldTransformConstants.FieldVariant fieldVariant) {
    Constants.FieldTransformConstants.FieldVariant requiredFieldVariant =
        Objects.requireNonNull(fieldVariant, "fieldVariant");

    AprilTagFields officialField =
        switch (requiredFieldVariant) {
          case REBUILT_WELDED -> AprilTagFields.k2026RebuiltWelded;
          case REBUILT_ANDYMARK -> AprilTagFields.k2026RebuiltAndymark;
        };

    AprilTagFieldLayout layout =
        Objects.requireNonNull(
            AprilTagFieldLayout.loadField(officialField), "official AprilTag field layout");
    validateFieldDimensions(layout, requiredFieldVariant);

    Map<Integer, Pose3d> ownedPoses = new HashMap<>();
    List<AprilTag> tags =
        Objects.requireNonNull(layout.getTags(), "official AprilTag field layout tags");
    for (AprilTag tag : tags) {
      if (tag == null) {
        throw new IllegalStateException("Official AprilTag field layout contains a null tag");
      }
      if (tag.ID <= 0) {
        throw new IllegalStateException("Official AprilTag ID must be positive: " + tag.ID);
      }

      Pose3d ownedPose = snapshotFinitePose(tag.pose, tag.ID);
      if (ownedPoses.putIfAbsent(tag.ID, ownedPose) != null) {
        throw new IllegalStateException("Official AprilTag field layout contains duplicate ID " + tag.ID);
      }
    }

    if (ownedPoses.isEmpty()) {
      throw new IllegalStateException("Official AprilTag field layout contains no tags");
    }
    return new AprilTagFieldLayoutContract(ownedPoses);
  }

  /**
   * Returns the canonical Blue-origin field-to-tag pose for a positive tag ID.
   *
   * @param tagId positive AprilTag ID
   * @return owned tag pose, or an empty optional when the positive ID is not in the selected field
   * @throws IllegalArgumentException when {@code tagId} is nonpositive
   */
  public Optional<Pose3d> getTagPose(int tagId) {
    if (tagId <= 0) {
      throw new IllegalArgumentException("tagId must be positive");
    }
    return Optional.ofNullable(fieldToTagById.get(tagId));
  }

  private static void validateFieldDimensions(
      AprilTagFieldLayout layout,
      Constants.FieldTransformConstants.FieldVariant fieldVariant) {
    double fieldLengthMeters = layout.getFieldLength();
    double fieldWidthMeters = layout.getFieldWidth();
    requireFinitePositive(fieldLengthMeters, "official field length");
    requireFinitePositive(fieldWidthMeters, "official field width");

    if (Double.compare(fieldLengthMeters, fieldVariant.fieldLengthMeters()) != 0
        || Double.compare(fieldWidthMeters, fieldVariant.fieldWidthMeters()) != 0) {
      throw new IllegalStateException(
          "Official AprilTag field dimensions do not match " + fieldVariant);
    }
  }

  private static Pose3d snapshotFinitePose(Pose3d pose, int tagId) {
    if (pose == null) {
      throw new IllegalStateException("Official AprilTag " + tagId + " has a null pose");
    }

    Translation3d translation =
        Objects.requireNonNull(pose.getTranslation(), "official AprilTag translation");
    Rotation3d rotation = Objects.requireNonNull(pose.getRotation(), "official AprilTag rotation");
    Quaternion quaternion =
        Objects.requireNonNull(rotation.getQuaternion(), "official AprilTag rotation quaternion");

    requireFinite(translation.getX(), "AprilTag " + tagId + " translation X");
    requireFinite(translation.getY(), "AprilTag " + tagId + " translation Y");
    requireFinite(translation.getZ(), "AprilTag " + tagId + " translation Z");
    requireFinite(quaternion.getW(), "AprilTag " + tagId + " quaternion W");
    requireFinite(quaternion.getX(), "AprilTag " + tagId + " quaternion X");
    requireFinite(quaternion.getY(), "AprilTag " + tagId + " quaternion Y");
    requireFinite(quaternion.getZ(), "AprilTag " + tagId + " quaternion Z");

    double quaternionNormSquared =
        quaternion.getW() * quaternion.getW()
            + quaternion.getX() * quaternion.getX()
            + quaternion.getY() * quaternion.getY()
            + quaternion.getZ() * quaternion.getZ();
    if (!Double.isFinite(quaternionNormSquared) || quaternionNormSquared <= 0.0) {
      throw new IllegalStateException(
          "Official AprilTag " + tagId + " rotation quaternion must have finite nonzero norm");
    }

    return new Pose3d(
        new Translation3d(translation.getX(), translation.getY(), translation.getZ()),
        new Rotation3d(
            new Quaternion(
                quaternion.getW(), quaternion.getX(), quaternion.getY(), quaternion.getZ())));
  }

  private static void requireFinitePositive(double value, String name) {
    requireFinite(value, name);
    if (value <= 0.0) {
      throw new IllegalStateException(name + " must be positive");
    }
  }

  private static void requireFinite(double value, String name) {
    if (!Double.isFinite(value)) {
      throw new IllegalStateException(name + " must be finite");
    }
  }
}
