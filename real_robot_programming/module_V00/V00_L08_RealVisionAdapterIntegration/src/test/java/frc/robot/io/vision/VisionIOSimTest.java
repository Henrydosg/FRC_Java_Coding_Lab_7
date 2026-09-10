// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of the WPILib BSD license file in the
// root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.Constants;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import frc.robot.vision.AprilTagFieldLayoutContract;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Verifies deterministic, vendor-neutral VisionIO simulation behavior and its locked API. */
class VisionIOSimTest {
  private static final double kTolerance = 1.0e-9;
  private static final int kFirstTagId = 1;
  private static final int kSecondTagId = 2;
  private static final int kUnknownPositiveTagId = 999;

  @Test
  void initialStateIsUnavailable() {
    VisionIOInputs inputs = new VisionIOInputs();
    inputs.available = true;
    inputs.connected = true;
    inputs.sampleValid = true;
    inputs.targets = List.of(new VisionTargetInputs(kUnknownPositiveTagId, Transform3d.kZero));

    new VisionIOSim(weldedField(), Transform3d.kZero).updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void constructorRequiresNoPose() {
    assertDoesNotThrow(() -> new VisionIOSim(weldedField(), Transform3d.kZero));
    assertDoesNotThrow(VisionIOSim.Frame::unavailable);
    assertDoesNotThrow(VisionIOSim.Frame::disconnected);
    assertDoesNotThrow(VisionIOSim.Frame::invalidSample);
    assertDoesNotThrow(VisionIOSim.Frame::noTargets);
  }

  @Test
  void onlyTargetsPresentRequiresPoseAndVisibleIds() {
    assertThrows(
        NullPointerException.class,
        () -> VisionIOSim.Frame.targetsPresent(null, List.of(kFirstTagId)));
    assertThrows(
        NullPointerException.class,
        () -> VisionIOSim.Frame.targetsPresent(Pose3d.kZero, null));
    assertDoesNotThrow(
        () -> VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));
  }

  @Test
  void allFrameMappingsAreExplicitAndComplete() {
    assertFrame(VisionIOSim.Frame.unavailable(), false, false, false);
    assertFrame(VisionIOSim.Frame.disconnected(), true, false, false);
    assertFrame(VisionIOSim.Frame.invalidSample(), true, true, false);
    assertFrame(VisionIOSim.Frame.noTargets(), true, true, true);

    VisionIOInputs inputs =
        update(
            simulator(),
            VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));
    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertEquals(List.of(kFirstTagId), targetIds(inputs));
  }

  @Test
  void repeatedSameFrameIsDeterministic() {
    VisionIOSim.Frame frame =
        VisionIOSim.Frame.targetsPresent(
            new Pose3d(2.0, 1.0, 0.2, new Rotation3d(0.1, -0.2, 0.7)),
            List.of(kSecondTagId, kFirstTagId));
    VisionIOSim first = simulator();
    VisionIOSim second = simulator();
    first.setFrame(frame);
    second.setFrame(frame);

    VisionIOInputs firstInputs = new VisionIOInputs();
    VisionIOInputs secondInputs = new VisionIOInputs();
    first.updateInputs(firstInputs);
    second.updateInputs(secondInputs);

    assertEquals(firstInputs.available, secondInputs.available);
    assertEquals(firstInputs.connected, secondInputs.connected);
    assertEquals(firstInputs.sampleValid, secondInputs.sampleValid);
    assertEquals(firstInputs.targets, secondInputs.targets);
  }

  @Test
  void explicitFrameReplacementChangesOutput() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));
    assertEquals(List.of(kFirstTagId), targetIds(update(simulator)));

    simulator.setFrame(VisionIOSim.Frame.noTargets());
    assertTrue(update(simulator).targets.isEmpty());
  }

  @Test
  void updateDoesNotAutoAdvanceOrExhaustTheCurrentFrame() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));

    VisionIOInputs first = update(simulator);
    VisionIOInputs second = update(simulator);

    assertEquals(first.targets, second.targets);
    assertEquals(targetIds(first), targetIds(second));
  }

  @Test
  void updateFullyOverwritesStaleTransportFields() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(VisionIOSim.Frame.noTargets());
    VisionIOInputs inputs = new VisionIOInputs();
    inputs.available = true;
    inputs.connected = true;
    inputs.sampleValid = true;
    inputs.targets = List.of(new VisionTargetInputs(kFirstTagId, Transform3d.kZero));

    simulator.updateInputs(inputs);

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void targetsToNoTargetsClearsTargets() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));
    assertFalse(update(simulator).targets.isEmpty());

    simulator.setFrame(VisionIOSim.Frame.noTargets());
    VisionIOInputs inputs = update(simulator);

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void targetsToInvalidSampleClearsTargets() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));
    assertFalse(update(simulator).targets.isEmpty());

    simulator.setFrame(VisionIOSim.Frame.invalidSample());
    VisionIOInputs inputs = update(simulator);

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void cameraToTargetUsesTheCorrectDirection() {
    AprilTagFieldLayoutContract fieldLayout = weldedField();
    Pose3d fieldToTag = fieldLayout.getTagPose(kFirstTagId).orElseThrow();
    Pose3d fieldToRobot =
        fieldToTag.transformBy(new Transform3d(1.0, 0.0, 0.0, Rotation3d.kZero));
    VisionIOSim simulator = new VisionIOSim(fieldLayout, Transform3d.kZero);
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(fieldToRobot, List.of(kFirstTagId)));

    Transform3d cameraToTarget = update(simulator).targets.get(0).cameraToTarget();

    assertEquals(-1.0, cameraToTarget.getX(), kTolerance);
    assertEquals(0.0, cameraToTarget.getY(), kTolerance);
    assertEquals(0.0, cameraToTarget.getZ(), kTolerance);
    assertEquals(0.0, cameraToTarget.getRotation().getAngle(), kTolerance);
  }

  @Test
  void geometryMatchesAnIndependentNumericOracle() {
    AprilTagFieldLayoutContract fieldLayout = weldedField();
    Pose3d fieldToTag = fieldLayout.getTagPose(kFirstTagId).orElseThrow();
    Pose3d fieldToRobot =
        fieldToTag.transformBy(new Transform3d(2.0, 0.0, 0.0, Rotation3d.kZero));
    Transform3d robotToCamera =
        new Transform3d(0.5, 0.0, 0.0, Rotation3d.kZero);
    VisionIOSim simulator = new VisionIOSim(fieldLayout, robotToCamera);
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(fieldToRobot, List.of(kFirstTagId)));

    Transform3d actual = update(simulator).targets.get(0).cameraToTarget();

    assertEquals(-2.5, actual.getX(), kTolerance);
    assertEquals(0.0, actual.getY(), kTolerance);
    assertEquals(0.0, actual.getZ(), kTolerance);
    assertEquals(0.0, actual.getRotation().getAngle(), kTolerance);
  }

  @Test
  void nonzeroExtrinsicIsAppliedInTheRobotPoseFrame() {
    AprilTagFieldLayoutContract fieldLayout = weldedField();
    Pose3d fieldToTag = fieldLayout.getTagPose(kFirstTagId).orElseThrow();
    Pose3d fieldToRobot =
        fieldToTag.transformBy(new Transform3d(1.0, 0.0, 0.0, Rotation3d.kZero));
    Transform3d robotToCamera =
        new Transform3d(0.25, 0.0, 0.0, Rotation3d.kZero);
    VisionIOSim simulator = new VisionIOSim(fieldLayout, robotToCamera);
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(fieldToRobot, List.of(kFirstTagId)));

    Transform3d actual = update(simulator).targets.get(0).cameraToTarget();

    assertEquals(-1.25, actual.getX(), kTolerance);
    assertEquals(0.0, actual.getY(), kTolerance);
    assertEquals(0.0, actual.getZ(), kTolerance);
  }

  @Test
  void orderedMultiTargetOutputPreservesCallerOrder() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(
            Pose3d.kZero, List.of(kSecondTagId, kFirstTagId)));

    assertEquals(List.of(kSecondTagId, kFirstTagId), targetIds(update(simulator)));
  }

  @Test
  void duplicateVisibleIdsAreRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            VisionIOSim.Frame.targetsPresent(
                Pose3d.kZero, List.of(kFirstTagId, kFirstTagId)));
  }

  @Test
  void nonpositiveVisibleIdIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(0)));
    assertThrows(
        IllegalArgumentException.class,
        () -> VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(-1)));
  }

  @Test
  void unknownPositiveVisibleIdIsRejectedBySelectedField() {
    VisionIOSim simulator = simulator();
    VisionIOSim.Frame unknownFrame =
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kUnknownPositiveTagId));

    assertThrows(IllegalArgumentException.class, () -> simulator.setFrame(unknownFrame));
  }

  @Test
  void nullArgumentsAreRejected() {
    assertThrows(NullPointerException.class, () -> new VisionIOSim(null, Transform3d.kZero));
    assertThrows(NullPointerException.class, () -> new VisionIOSim(weldedField(), null));

    VisionIOSim simulator = simulator();
    assertThrows(NullPointerException.class, () -> simulator.setFrame(null));
    assertThrows(NullPointerException.class, () -> simulator.updateInputs(null));
  }

  @Test
  void nonfinitePoseIsRejected() {
    Pose3d nonfinitePose = new Pose3d(Double.NaN, 0.0, 0.0, Rotation3d.kZero);

    assertThrows(
        IllegalArgumentException.class,
        () -> VisionIOSim.Frame.targetsPresent(nonfinitePose, List.of(kFirstTagId)));
  }

  @Test
  void nonfiniteExtrinsicIsRejected() {
    Transform3d nonfiniteTransform =
        new Transform3d(Double.NaN, 0.0, 0.0, Rotation3d.kZero);

    assertThrows(
        IllegalArgumentException.class, () -> new VisionIOSim(weldedField(), nonfiniteTransform));
  }

  @Test
  void visibleIdCollectionIsDefensivelyOwned() {
    List<Integer> callerIds = new ArrayList<>(List.of(kFirstTagId, kSecondTagId));
    VisionIOSim.Frame frame = VisionIOSim.Frame.targetsPresent(Pose3d.kZero, callerIds);
    callerIds.clear();
    VisionIOSim simulator = simulator();

    simulator.setFrame(frame);

    assertEquals(List.of(kFirstTagId, kSecondTagId), targetIds(update(simulator)));
  }

  @Test
  void rejectedFrameDoesNotReplacePreviousValidFrame() {
    VisionIOSim simulator = simulator();
    simulator.setFrame(
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kFirstTagId)));
    VisionIOInputs before = update(simulator);
    VisionIOSim.Frame invalidFrame =
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(kUnknownPositiveTagId));

    assertThrows(IllegalArgumentException.class, () -> simulator.setFrame(invalidFrame));
    VisionIOInputs after = update(simulator);

    assertEquals(before.available, after.available);
    assertEquals(before.connected, after.connected);
    assertEquals(before.sampleValid, after.sampleValid);
    assertEquals(before.targets, after.targets);
  }

  @Test
  void publicApiMatchesTheLockedBoundary() throws NoSuchMethodException {
    Set<String> simulatorPublicMethods =
        Arrays.stream(VisionIOSim.class.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .map(Method::getName)
            .collect(Collectors.toSet());
    Set<String> framePublicMethods =
        Arrays.stream(VisionIOSim.Frame.class.getDeclaredMethods())
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .map(Method::getName)
            .collect(Collectors.toSet());

    assertTrue(Modifier.isFinal(VisionIOSim.class.getModifiers()));
    assertTrue(Modifier.isFinal(VisionIOSim.Frame.class.getModifiers()));
    assertEquals(Set.of("setFrame", "updateInputs"), simulatorPublicMethods);
    assertEquals(
        Set.of("unavailable", "disconnected", "invalidSample", "noTargets", "targetsPresent"),
        framePublicMethods);
    assertNotNull(
        VisionIOSim.class.getConstructor(AprilTagFieldLayoutContract.class, Transform3d.class));
    assertNotNull(
        VisionIOSim.class
            .getMethod("setFrame", VisionIOSim.Frame.class));
    assertNotNull(
        VisionIOSim.class
            .getMethod("updateInputs", VisionIOInputs.class));
    assertTrue(
        Arrays.stream(VisionIOSim.Frame.class.getDeclaredConstructors())
            .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers())));
  }

  @Test
  void implementationStateHasNoTimeRandomVendorOrRuntimeControlFields() {
    Set<String> forbiddenTypeNames =
        Set.of(
            "Timer",
            "DriverStation",
            "CommandScheduler",
            "NetworkTable",
            "Random",
            "Thread");

    Set<String> declaredFieldTypeNames =
        Arrays.stream(VisionIOSim.class.getDeclaredFields())
            .map(field -> field.getType().getSimpleName())
            .collect(Collectors.toSet());

    assertTrue(declaredFieldTypeNames.stream().noneMatch(forbiddenTypeNames::contains));
    assertTrue(declaredFieldTypeNames.contains(AprilTagFieldLayoutContract.class.getSimpleName()));
    assertTrue(declaredFieldTypeNames.contains(Transform3d.class.getSimpleName()));
  }

  private static void assertFrame(
      VisionIOSim.Frame frame, boolean available, boolean connected, boolean sampleValid) {
    VisionIOInputs inputs = update(simulator(), frame);

    assertEquals(available, inputs.available);
    assertEquals(connected, inputs.connected);
    assertEquals(sampleValid, inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  private static VisionIOInputs update(VisionIOSim simulator) {
    VisionIOInputs inputs = new VisionIOInputs();
    simulator.updateInputs(inputs);
    return inputs;
  }

  private static VisionIOInputs update(VisionIOSim simulator, VisionIOSim.Frame frame) {
    simulator.setFrame(frame);
    return update(simulator);
  }

  private static List<Integer> targetIds(VisionIOInputs inputs) {
    return inputs.targets.stream().map(VisionTargetInputs::tagId).toList();
  }

  private static VisionIOSim simulator() {
    return new VisionIOSim(weldedField(), Transform3d.kZero);
  }

  private static AprilTagFieldLayoutContract weldedField() {
    return AprilTagFieldLayoutContract.loadOfficial2026(
        Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
  }
}
