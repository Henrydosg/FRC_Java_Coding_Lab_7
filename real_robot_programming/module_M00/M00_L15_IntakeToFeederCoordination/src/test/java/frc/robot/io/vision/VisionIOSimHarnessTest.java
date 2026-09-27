// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.observation.vision.QualifiedVisionMeasurement;
import frc.robot.observation.vision.VisionMeasurementQuality.Acceptance;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.vision.AprilTagFieldLayoutContract;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies the bounded one-shot L09 simulation fixture source. */
class VisionIOSimHarnessTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void startupIsUnavailableAndValidFramesAreOneShotAndFresh() {
    VisionIOSim simulator = createSimulator();
    VisionIOSimHarness harness = createHarness(simulator);
    VisionIOInputs inputs = update(simulator);

    assertFalse(inputs.available);
    harness.apply(VisionIOSimHarness.FixtureSelection.UNAVAILABLE);
    assertFalse(update(simulator).available);

    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    VisionIOInputs frameA = update(simulator);
    assertTrue(frameA.available);
    assertTrue(frameA.connected);
    assertTrue(frameA.sampleValid);
    assertTrue(frameA.timingValid);
    assertEquals(1, frameA.targets.size());
    double frameATimestamp = frameA.receiveTimestampSeconds;

    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    VisionIOInputs unchangedFrameA = update(simulator);
    assertEquals(frameATimestamp, unchangedFrameA.receiveTimestampSeconds);
    assertEquals(frameA.targets, unchangedFrameA.targets);

    Timer.delay(0.005);
    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_B);
    VisionIOInputs frameB = update(simulator);
    assertTrue(frameB.available);
    assertTrue(frameB.timingValid);
    assertTrue(frameB.receiveTimestampSeconds > frameATimestamp);
    assertEquals(1, frameB.targets.size());

    harness.apply(VisionIOSimHarness.FixtureSelection.UNAVAILABLE);
    assertFalse(update(simulator).available);

    Timer.delay(0.005);
    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    VisionIOInputs returnedFrameA = update(simulator);
    assertTrue(returnedFrameA.available);
    assertTrue(returnedFrameA.receiveTimestampSeconds > frameB.receiveTimestampSeconds);
  }

  @Test
  void generatedFramesPassExistingQualityAndTimingQualification() {
    VisionIOSim simulator = createSimulator();
    VisionIOSimHarness harness = createHarness(simulator);
    VisionSubsystem subsystem =
        new VisionSubsystem(
            simulator,
            fieldLayout(),
            Constants.VisionConstants.kRobotToCamera,
            new Policy(1.0, 2.0, 3.0),
            0.250,
            Timer::getFPGATimestamp);

    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    subsystem.periodic();
    QualifiedVisionMeasurement frameA = subsystem.getLatestQualifiedMeasurement().orElseThrow();
    assertEquals(Acceptance.ACCEPTED, frameA.quality().acceptance());

    Timer.delay(0.005);
    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_B);
    subsystem.periodic();
    QualifiedVisionMeasurement frameB = subsystem.getLatestQualifiedMeasurement().orElseThrow();
    assertEquals(Acceptance.ACCEPTED, frameB.quality().acceptance());
    assertTrue(
        frameB.measurementTimestampSeconds() > frameA.measurementTimestampSeconds());
  }

  @Test
  void nullSelectionFailsClosed() {
    VisionIOSim simulator = createSimulator();
    VisionIOSimHarness harness = createHarness(simulator);

    harness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    assertTrue(update(simulator).available);

    harness.apply(null);
    assertFalse(update(simulator).available);
  }

  private static VisionIOSim createSimulator() {
    return new VisionIOSim(fieldLayout(), Constants.VisionConstants.kRobotToCamera);
  }

  private static VisionIOSimHarness createHarness(VisionIOSim simulator) {
    return new VisionIOSimHarness(simulator, fieldLayout());
  }

  private static VisionIOInputs update(VisionIOSim simulator) {
    VisionIOInputs inputs = new VisionIOInputs();
    simulator.updateInputs(inputs);
    return inputs;
  }

  private static AprilTagFieldLayoutContract fieldLayout() {
    return AprilTagFieldLayoutContract.loadOfficial2026(
        Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
  }
}
