// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.vision;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.Constants;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Verifies the immutable official 2026 AprilTag field-layout reference contract. */
class AprilTagFieldLayoutContractTest {
  private static final double kTolerance = 1.0e-9;
  private static final int kOracleTagId = 1;
  private static final int kQuarterTurnTagId = 2;
  private static final int kUnknownPositiveTagId = 999;

  // Independent numeric oracles copied from the installed WPILib 2026.2.1 official resources.
  private static final double kWeldedTag1XMeters = 11.8779798;
  private static final double kWeldedTag1YMeters = 7.4247756;
  private static final double kAndyMarkTag1XMeters = 11.863959;
  private static final double kAndyMarkTag1YMeters = 7.411491399999999;
  private static final double kTag1ZMeters = 0.889;
  private static final double kWeldedFieldLengthMeters = 16.541;
  private static final double kWeldedFieldWidthMeters = 8.069;

  @Test
  void weldedFieldLoadsThroughExplicitVariant() {
    AprilTagFieldLayoutContract contract =
        assertDoesNotThrow(
            () ->
                AprilTagFieldLayoutContract.loadOfficial2026(
                    Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED));

    assertTrue(contract.getTagPose(kOracleTagId).isPresent());
  }

  @Test
  void andyMarkFieldLoadsThroughExplicitVariant() {
    AprilTagFieldLayoutContract contract =
        assertDoesNotThrow(
            () ->
                AprilTagFieldLayoutContract.loadOfficial2026(
                    Constants.FieldTransformConstants.FieldVariant.REBUILT_ANDYMARK));

    assertTrue(contract.getTagPose(kOracleTagId).isPresent());
  }

  @Test
  void nullFieldVariantIsRejected() {
    assertThrows(
        NullPointerException.class,
        () -> AprilTagFieldLayoutContract.loadOfficial2026(null));
  }

  @Test
  void knownTagIdReturnsPresentPose() {
    Optional<Pose3d> pose = weldedContract().getTagPose(kOracleTagId);

    assertTrue(pose.isPresent());
    assertNotNull(pose.orElseThrow());
  }

  @Test
  void unknownPositiveTagIdReturnsEmpty() {
    assertEquals(Optional.empty(), weldedContract().getTagPose(kUnknownPositiveTagId));
  }

  @Test
  void zeroTagIdIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> weldedContract().getTagPose(0));
  }

  @Test
  void negativeTagIdIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> weldedContract().getTagPose(-1));
  }

  @Test
  void weldedPoseMatchesIndependentOfficialResourceOracle() {
    Pose3d fieldToTag = weldedContract().getTagPose(kOracleTagId).orElseThrow();

    assertEquals(kWeldedTag1XMeters, fieldToTag.getX(), kTolerance);
    assertEquals(kWeldedTag1YMeters, fieldToTag.getY(), kTolerance);
    assertEquals(kTag1ZMeters, fieldToTag.getZ(), kTolerance);
    assertEquals(Math.PI, fieldToTag.getRotation().getZ(), kTolerance);
  }

  @Test
  void andyMarkPoseMatchesIndependentOfficialResourceOracle() {
    Pose3d fieldToTag = andyMarkContract().getTagPose(kOracleTagId).orElseThrow();

    assertEquals(kAndyMarkTag1XMeters, fieldToTag.getX(), kTolerance);
    assertEquals(kAndyMarkTag1YMeters, fieldToTag.getY(), kTolerance);
    assertEquals(kTag1ZMeters, fieldToTag.getZ(), kTolerance);
    assertEquals(Math.PI, fieldToTag.getRotation().getZ(), kTolerance);
  }

  @Test
  void returnedPoseIsFieldToTagRatherThanTagToField() {
    Pose3d fieldToTag = weldedContract().getTagPose(kQuarterTurnTagId).orElseThrow();
    Transform3d tagToField = new Transform3d(Pose3d.kZero, fieldToTag).inverse();

    assertNotEquals(tagToField.getX(), fieldToTag.getX(), kTolerance);
    assertNotEquals(tagToField.getY(), fieldToTag.getY(), kTolerance);
  }

  @Test
  void returnedPoseIsNotRedAllianceMirrored() {
    Pose3d fieldToTag = weldedContract().getTagPose(kOracleTagId).orElseThrow();
    double redMirroredXMeters = kWeldedFieldLengthMeters - kWeldedTag1XMeters;
    double redMirroredYMeters = kWeldedFieldWidthMeters - kWeldedTag1YMeters;

    assertEquals(kWeldedTag1XMeters, fieldToTag.getX(), kTolerance);
    assertEquals(kWeldedTag1YMeters, fieldToTag.getY(), kTolerance);
    assertNotEquals(redMirroredXMeters, fieldToTag.getX(), kTolerance);
    assertNotEquals(redMirroredYMeters, fieldToTag.getY(), kTolerance);
  }

  @Test
  void translationUsesMetersWithoutConversion() {
    Pose3d fieldToTag = weldedContract().getTagPose(kOracleTagId).orElseThrow();

    assertEquals(kWeldedTag1XMeters, fieldToTag.getX(), kTolerance);
    assertEquals(kWeldedTag1YMeters, fieldToTag.getY(), kTolerance);
    assertEquals(kTag1ZMeters, fieldToTag.getZ(), kTolerance);
  }

  @Test
  void rotationUsesRightHandedNwuRadians() {
    Pose3d fieldToTag = weldedContract().getTagPose(kQuarterTurnTagId).orElseThrow();

    assertTrue(fieldToTag.getY() > 0.0);
    assertTrue(fieldToTag.getZ() > 0.0);
    assertEquals(Math.PI / 2.0, fieldToTag.getRotation().getZ(), kTolerance);
  }

  @Test
  void repeatedLookupIsDeterministic() {
    AprilTagFieldLayoutContract contract = weldedContract();

    assertEquals(contract.getTagPose(kOracleTagId), contract.getTagPose(kOracleTagId));
  }

  @Test
  void callerGeometryOperationsCannotMutateStoredPose() {
    AprilTagFieldLayoutContract contract = weldedContract();
    Pose3d original = contract.getTagPose(kOracleTagId).orElseThrow();

    Pose3d transformed =
        original.transformBy(
            new Transform3d(1.0, -2.0, 0.5, new Rotation3d(0.1, -0.2, 0.3)));
    Pose3d repeated = contract.getTagPose(kOracleTagId).orElseThrow();

    assertNotEquals(transformed, repeated);
    assertPoseEquals(original, repeated);
  }

  @Test
  void weldedAndAndyMarkRemainDistinguishable() {
    Pose3d welded = weldedContract().getTagPose(kOracleTagId).orElseThrow();
    Pose3d andyMark = andyMarkContract().getTagPose(kOracleTagId).orElseThrow();

    assertNotEquals(welded.getX(), andyMark.getX(), kTolerance);
    assertNotEquals(welded.getY(), andyMark.getY(), kTolerance);
  }

  @Test
  void classExposesOnlyApprovedPublicApi() {
    Set<String> publicMethodNames =
        Arrays.stream(AprilTagFieldLayoutContract.class.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .map(Method::getName)
            .collect(Collectors.toSet());

    assertTrue(Modifier.isFinal(AprilTagFieldLayoutContract.class.getModifiers()));
    assertEquals(Set.of("loadOfficial2026", "getTagPose"), publicMethodNames);
  }

  @Test
  void unapprovedFromLayoutSeamIsAbsent() {
    boolean seamPresent =
        Arrays.stream(AprilTagFieldLayoutContract.class.getDeclaredMethods())
            .map(Method::getName)
            .anyMatch("fromLayout"::equals);

    assertFalse(seamPresent);
  }

  private static AprilTagFieldLayoutContract weldedContract() {
    return AprilTagFieldLayoutContract.loadOfficial2026(
        Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
  }

  private static AprilTagFieldLayoutContract andyMarkContract() {
    return AprilTagFieldLayoutContract.loadOfficial2026(
        Constants.FieldTransformConstants.FieldVariant.REBUILT_ANDYMARK);
  }

  private static void assertPoseEquals(Pose3d expected, Pose3d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(expected.getZ(), actual.getZ(), kTolerance);
    assertEquals(0.0, expected.getRotation().minus(actual.getRotation()).getAngle(), kTolerance);
  }
}
