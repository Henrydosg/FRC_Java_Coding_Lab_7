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
import frc.robot.io.vision.VisionIOSim;
import frc.robot.observation.vision.QualifiedVisionMeasurement;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import frc.robot.observation.vision.VisionObservation.State;
import frc.robot.vision.AprilTagFieldLayoutContract;
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

  @Test
  void qualificationUsesFieldLookupQualityAndSingleLatencySubtraction() {
    AprilTagFieldLayoutContract layout =
        AprilTagFieldLayoutContract.loadOfficial2026(
            frc.robot.Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
    VisionIOSim visionIO = new VisionIOSim(layout, Transform3d.kZero);
    visionIO.setFrame(
        VisionIOSim.Frame.targetsPresent(
            edu.wpi.first.math.geometry.Pose3d.kZero,
            List.of(1),
            20.0,
            0.125));
    VisionSubsystem subsystem =
        new VisionSubsystem(
            visionIO,
            layout,
            Transform3d.kZero,
            new Policy(100.0, 100.0, 100.0),
            1.0,
            () -> 20.0);

    subsystem.periodic();

    QualifiedVisionMeasurement measurement =
        subsystem.getLatestQualifiedMeasurement().orElseThrow();
    assertEquals(19.875, measurement.measurementTimestampSeconds(), 1.0e-9);
    assertEquals(State.TARGETS_PRESENT, subsystem.getObservation().state());
  }

  @Test
  void invalidTimingCannotBecomeAQualifiedMeasurement() {
    // Fresh, in-range, known-tag sample: only timingValid=false can block qualification.
    VisionSubsystem subsystem = qualifyingFixtureWithTimingValidity(false);

    subsystem.periodic();

    assertTrue(subsystem.getLatestQualifiedMeasurement().isEmpty());
  }

  @Test
  void validTimingWithTheSameFixtureQualifies() {
    // Positive control: the identical fixture qualifies once timingValid is true.
    VisionSubsystem subsystem = qualifyingFixtureWithTimingValidity(true);

    subsystem.periodic();

    QualifiedVisionMeasurement measurement =
        subsystem.getLatestQualifiedMeasurement().orElseThrow();
    assertEquals(20.0, measurement.measurementTimestampSeconds(), 1.0e-9);
  }

  private static VisionSubsystem qualifyingFixtureWithTimingValidity(boolean timingValid) {
    FakeVisionIO visionIO = new FakeVisionIO();
    visionIO.inputs.available = true;
    visionIO.inputs.connected = true;
    visionIO.inputs.sampleValid = true;
    visionIO.inputs.timingValid = timingValid;
    visionIO.inputs.receiveTimestampSeconds = 20.0;
    visionIO.inputs.totalLatencySeconds = 0.0;
    visionIO.inputs.targets =
        List.of(new VisionTargetInputs(1, new Transform3d(1.0, 0.0, 0.0, new Rotation3d())));
    return new VisionSubsystem(
        visionIO,
        AprilTagFieldLayoutContract.loadOfficial2026(
            frc.robot.Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED),
        Transform3d.kZero,
        new Policy(100.0, 100.0, 100.0),
        1.0,
        () -> 20.0);
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
      destination.timingValid = inputs.timingValid;
      destination.receiveTimestampSeconds = inputs.receiveTimestampSeconds;
      destination.totalLatencySeconds = inputs.totalLatencySeconds;
      destination.targets = inputs.targets;
    }
  }
}
