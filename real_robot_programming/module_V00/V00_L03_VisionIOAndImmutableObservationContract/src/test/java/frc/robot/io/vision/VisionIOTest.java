// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/** Verifies the vendor-neutral, one-cycle V00_L03 VisionIO transport contract. */
class VisionIOTest {

  @Test
  void defaultsToUnavailableDisconnectedInvalidAndEmpty() {
    VisionIOInputs inputs = new VisionIOInputs();

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void updateReplacesEveryCycleAndClearsStaleTargets() {
    VisionIOInputs inputs = new VisionIOInputs();
    DeterministicVisionIO visionIO = new DeterministicVisionIO();
    VisionTargetInputs first = target(1, 0.5, 0.0, 0.2);
    VisionTargetInputs second = target(2, -0.3, 0.1, 0.4);

    visionIO.publish(true, true, true, List.of(first, second));
    visionIO.updateInputs(inputs);
    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertEquals(List.of(first, second), inputs.targets);

    visionIO.publish(true, true, true, List.of());
    visionIO.updateInputs(inputs);
    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());

    visionIO.publish(true, false, false, List.of());
    visionIO.updateInputs(inputs);
    assertTrue(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void preservesMultipleTargetsInAdapterAcquisitionOrder() {
    VisionIOInputs inputs = new VisionIOInputs();
    DeterministicVisionIO visionIO = new DeterministicVisionIO();
    VisionTargetInputs first = target(7, 0.1, 0.2, 0.3);
    VisionTargetInputs second = target(4, -0.4, 0.5, 0.6);

    visionIO.publish(true, true, true, List.of(first, second));
    visionIO.updateInputs(inputs);

    assertEquals(List.of(first, second), inputs.targets);
    assertEquals(7, inputs.targets.get(0).tagId());
    assertEquals(4, inputs.targets.get(1).tagId());
  }

  @Test
  void publicInterfaceContainsOnlyTheLockedUpdateMethod() {
    Set<String> publicMethodNames =
        List.of(VisionIO.class.getDeclaredMethods()).stream()
            .filter(method -> Modifier.isPublic(method.getModifiers()))
            .map(Method::getName)
            .collect(Collectors.toSet());

    assertEquals(Set.of("updateInputs"), publicMethodNames);
    Method updateInputs = VisionIO.class.getDeclaredMethods()[0];
    assertEquals("updateInputs", updateInputs.getName());
    assertEquals(VisionIOInputs.class, updateInputs.getParameterTypes()[0]);
  }

  @Test
  void targetTransportUsesOnlyTagIdentityAndCameraRelativeTransform() {
    assertEquals(2, VisionTargetInputs.class.getRecordComponents().length);
    assertEquals("tagId", VisionTargetInputs.class.getRecordComponents()[0].getName());
    assertEquals(int.class, VisionTargetInputs.class.getRecordComponents()[0].getType());
    assertEquals("cameraToTarget", VisionTargetInputs.class.getRecordComponents()[1].getName());
    assertEquals(Transform3d.class, VisionTargetInputs.class.getRecordComponents()[1].getType());
  }

  private static VisionTargetInputs target(int tagId, double xMeters, double yMeters, double zMeters) {
    return new VisionTargetInputs(
        tagId, new Transform3d(xMeters, yMeters, zMeters, Rotation3d.kZero));
  }

  private static final class DeterministicVisionIO implements VisionIO {
    private boolean available;
    private boolean connected;
    private boolean sampleValid;
    private List<VisionTargetInputs> targets = List.of();

    void publish(
        boolean available,
        boolean connected,
        boolean sampleValid,
        List<VisionTargetInputs> targets) {
      this.available = available;
      this.connected = connected;
      this.sampleValid = sampleValid;
      this.targets = List.copyOf(targets);
    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
      inputs.sampleValid = sampleValid;
      inputs.targets = List.copyOf(targets);
    }
  }
}
