// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.io.vision.VisionIO;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import frc.robot.observation.vision.VisionObservation.State;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies the observation-only VisionIO runtime owner. */
class VisionSubsystemTest {
  @Test
  void periodicCallsIoAndReplacesObservationWithImmutableMeaning() {
    FakeVisionIO visionIO = new FakeVisionIO();
    visionIO.inputs.available = true;
    visionIO.inputs.connected = true;
    visionIO.inputs.sampleValid = true;
    visionIO.inputs.targets =
        List.of(new VisionIO.VisionTargetInputs(7, new Transform3d(1.0, 2.0, 3.0, new Rotation3d())));
    VisionSubsystem subsystem = new VisionSubsystem(visionIO);

    subsystem.periodic();

    assertEquals(1, visionIO.updateCount);
    assertEquals(State.TARGETS_PRESENT, subsystem.getObservation().state());
    assertEquals(7, subsystem.getObservation().targets().get(0).tagId());
    assertEquals(1.0, subsystem.getObservation().targets().get(0).cameraToTarget().getX());
  }

  @Test
  void invalidTransportStateCannotBecomeTargetObservation() {
    FakeVisionIO visionIO = new FakeVisionIO();
    visionIO.inputs.available = true;
    visionIO.inputs.connected = true;
    visionIO.inputs.sampleValid = false;
    visionIO.inputs.targets = List.of();
    VisionSubsystem subsystem = new VisionSubsystem(visionIO);

    subsystem.periodic();

    assertEquals(State.INVALID_SAMPLE, subsystem.getObservation().state());
    assertTrue(subsystem.getObservation().targets().isEmpty());
  }

  private static final class FakeVisionIO implements VisionIO {
    private final VisionIOInputs inputs = new VisionIOInputs();
    private int updateCount;

    @Override
    public void updateInputs(VisionIOInputs destination) {
      updateCount++;
      destination.available = inputs.available;
      destination.connected = inputs.connected;
      destination.sampleValid = inputs.sampleValid;
      destination.targets = inputs.targets;
    }
  }
}
