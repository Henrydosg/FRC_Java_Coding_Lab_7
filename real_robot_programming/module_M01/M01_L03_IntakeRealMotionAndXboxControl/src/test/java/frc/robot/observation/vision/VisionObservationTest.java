// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.observation.vision.VisionObservation.State;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies immutable, vendor-neutral V00_L03 VisionObservation semantics. */
class VisionObservationTest {
  private static final double kTolerance = 1.0e-9;

  @Test
  void distinguishesEveryLockedAbsenceAndValidityState() {
    for (State state : List.of(State.UNAVAILABLE, State.DISCONNECTED, State.INVALID_SAMPLE, State.NO_TARGETS)) {
      VisionObservation observation = new VisionObservation(state, List.of());
      assertEquals(state, observation.state());
      assertTrue(observation.targets().isEmpty());
    }

    TargetObservation target = target(3, 0.4, -0.2, 0.8);
    VisionObservation present = new VisionObservation(State.TARGETS_PRESENT, List.of(target));
    assertEquals(State.TARGETS_PRESENT, present.state());
    assertEquals(List.of(target), present.targets());
  }

  @Test
  void rejectsInconsistentStateAndTargetCombinations() {
    TargetObservation target = target(2, 0.1, 0.2, 0.3);

    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionObservation(State.TARGETS_PRESENT, List.of()));
    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionObservation(State.NO_TARGETS, List.of(target)));
    assertThrows(
        IllegalArgumentException.class,
        () -> new VisionObservation(State.INVALID_SAMPLE, List.of(target)));
  }

  @Test
  void preservesMultipleTargetsInInputOrderAndDefensivelyCopiesTheCollection() {
    TargetObservation first = target(8, 0.2, 0.3, 0.4);
    TargetObservation second = target(5, -0.5, 0.6, 0.7);
    List<TargetObservation> callerOwnedTargets = new ArrayList<>(List.of(first, second));

    VisionObservation observation =
        new VisionObservation(State.TARGETS_PRESENT, callerOwnedTargets);
    callerOwnedTargets.clear();

    assertEquals(List.of(first, second), observation.targets());
    assertThrows(
        UnsupportedOperationException.class,
        () -> observation.targets().add(target(9, 0.0, 0.0, 0.0)));
  }

  @Test
  void targetObservationDefensivelyOwnsCameraRelativeTransform() {
    Transform3d callerOwnedTransform =
        new Transform3d(0.25, -0.50, 1.25, new Rotation3d(0.1, -0.2, 0.3));
    TargetObservation target = new TargetObservation(6, callerOwnedTransform);

    assertNotSame(callerOwnedTransform, target.cameraToTarget());
    assertTransformEquals(callerOwnedTransform, target.cameraToTarget());
  }

  @Test
  void preservesCameraToTargetValuesWithoutInvertingOrEstimatingPose() {
    TargetObservation target = target(4, 1.2, -0.4, 0.9);

    assertEquals(1.2, target.cameraToTarget().getX(), kTolerance);
    assertEquals(-0.4, target.cameraToTarget().getY(), kTolerance);
    assertEquals(0.9, target.cameraToTarget().getZ(), kTolerance);
    assertEquals(0.0, target.cameraToTarget().getRotation().getAngle(), kTolerance);
  }

  @Test
  void rejectsNullAndInvalidTargetValues() {
    assertThrows(NullPointerException.class, () -> new VisionObservation(null, List.of()));
    assertThrows(NullPointerException.class, () -> new VisionObservation(State.NO_TARGETS, null));
    assertThrows(NullPointerException.class, () -> new TargetObservation(1, null));
    assertThrows(
        IllegalArgumentException.class,
        () -> new TargetObservation(0, Transform3d.kZero));
    assertThrows(
        IllegalArgumentException.class,
        () -> new TargetObservation(-1, Transform3d.kZero));
    assertThrows(
        IllegalArgumentException.class,
        () -> target(1, Double.NaN, 0.0, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> target(1, 0.0, Double.POSITIVE_INFINITY, 0.0));
  }

  @Test
  void acceptsIdentityRotationAtTheTransformBoundary() {
    Transform3d identityRotationTransform =
        new Transform3d(0.35, -0.15, 0.80, Rotation3d.kZero);

    TargetObservation target = new TargetObservation(1, identityRotationTransform);

    assertTransformEquals(identityRotationTransform, target.cameraToTarget());
    assertEquals(0.0, target.cameraToTarget().getRotation().getAngle(), kTolerance);
  }

  @Test
  void providesDeterministicValueEqualityAndNoFutureFields() {
    TargetObservation first = target(11, 0.1, 0.2, 0.3);
    TargetObservation second = target(11, 0.1, 0.2, 0.3);
    VisionObservation firstObservation = new VisionObservation(State.TARGETS_PRESENT, List.of(first));
    VisionObservation secondObservation = new VisionObservation(State.TARGETS_PRESENT, List.of(second));

    assertEquals(firstObservation, secondObservation);
    assertEquals(List.of("state", "targets"), recordComponentNames(VisionObservation.class));
    assertEquals(List.of("tagId", "cameraToTarget"), recordComponentNames(TargetObservation.class));
    assertFalse(recordComponentNames(VisionObservation.class).contains("timestampSeconds"));
    assertFalse(recordComponentNames(VisionObservation.class).contains("latencySeconds"));
    assertFalse(recordComponentNames(TargetObservation.class).contains("ambiguity"));
    assertFalse(recordComponentNames(TargetObservation.class).contains("quality"));
  }

  private static TargetObservation target(int tagId, double xMeters, double yMeters, double zMeters) {
    return new TargetObservation(
        tagId, new Transform3d(xMeters, yMeters, zMeters, Rotation3d.kZero));
  }

  private static List<String> recordComponentNames(Class<?> recordClass) {
    return List.of(recordClass.getRecordComponents()).stream()
        .map(component -> component.getName())
        .toList();
  }

  private static void assertTransformEquals(Transform3d expected, Transform3d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(expected.getZ(), actual.getZ(), kTolerance);
    assertEquals(0.0, expected.getRotation().minus(actual.getRotation()).getAngle(), kTolerance);
  }
}
