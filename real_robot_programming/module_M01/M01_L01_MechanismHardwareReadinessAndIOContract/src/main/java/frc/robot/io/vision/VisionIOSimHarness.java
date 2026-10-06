// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.vision.AprilTagFieldLayoutContract;
import java.util.List;
import java.util.Objects;

/**
 * Applies one-shot, deterministic simulation fixtures to the simulation-only vision source.
 *
 * <p>This class is a commissioning aid for L09 Simulation. It owns neither vision qualification
 * nor fusion policy and has no NetworkTables or scheduler behavior.
 */
public final class VisionIOSimHarness {
  private static final int FIXTURE_TAG_ID = 1;
  private static final double SIMULATION_LATENCY_SECONDS = 0.020;

  private final VisionIOSim visionIO;
  private final AprilTagFieldLayoutContract fieldLayout;
  private FixtureSelection lastAppliedFixture = FixtureSelection.UNAVAILABLE;
  private double lastInjectedMeasurementTimestampSeconds = Double.NEGATIVE_INFINITY;

  /** Selectable simulation fixture states exposed by the simulation-only chooser. */
  public enum FixtureSelection {
    UNAVAILABLE,
    VALID_FRAME_A,
    VALID_FRAME_B
  }

  /**
   * Creates a harness bound specifically to the explicit-frame simulation implementation.
   *
   * @param visionIO simulation vision source controlled by this harness
   * @param fieldLayout official field-layout snapshot used to derive fixture geometry
   */
  public VisionIOSimHarness(
      VisionIOSim visionIO,
      AprilTagFieldLayoutContract fieldLayout) {
    this.visionIO = Objects.requireNonNull(visionIO, "visionIO");
    this.fieldLayout = Objects.requireNonNull(fieldLayout, "fieldLayout");
  }

  /**
   * Applies a selected fixture only when its effective state changes.
   *
   * <p>Invalid selections, geometry failures, and timing failures fail closed to an unavailable
   * frame. A failed valid selection remains retryable because the effective applied state returns
   * to {@link FixtureSelection#UNAVAILABLE}.
   *
   * @param selection simulation fixture selected by the user
   */
  public void apply(FixtureSelection selection) {
    if (selection == null) {
      applyUnavailable();
      return;
    }
    if (selection == lastAppliedFixture) {
      return;
    }

    if (selection == FixtureSelection.UNAVAILABLE) {
      applyUnavailable();
      return;
    }

    try {
      double receiveTimestampSeconds = Timer.getFPGATimestamp();
      double measurementTimestampSeconds =
          receiveTimestampSeconds - SIMULATION_LATENCY_SECONDS;
      if (!Double.isFinite(receiveTimestampSeconds)
          || receiveTimestampSeconds < SIMULATION_LATENCY_SECONDS
          || !Double.isFinite(measurementTimestampSeconds)
          || measurementTimestampSeconds <= lastInjectedMeasurementTimestampSeconds) {
        applyUnavailable();
        return;
      }

      Pose3d fieldToRobotGroundTruth = createFixturePose(selection);
      VisionIOSim.Frame frame =
          VisionIOSim.Frame.targetsPresent(
              fieldToRobotGroundTruth,
              List.of(FIXTURE_TAG_ID),
              receiveTimestampSeconds,
              SIMULATION_LATENCY_SECONDS);
      visionIO.setFrame(frame);
      lastInjectedMeasurementTimestampSeconds = measurementTimestampSeconds;
      lastAppliedFixture = selection;
    } catch (RuntimeException failure) {
      applyUnavailable();
    }
  }

  private Pose3d createFixturePose(FixtureSelection selection) {
    Pose3d fieldToTag =
        fieldLayout
            .getTagPose(FIXTURE_TAG_ID)
            .orElseThrow(() -> new IllegalStateException("fixture tag is absent from the field"));
    Transform3d tagLocalOffset =
        switch (selection) {
          case VALID_FRAME_A -> new Transform3d(1.0, 0.0, 0.0, Rotation3d.kZero);
          case VALID_FRAME_B -> new Transform3d(1.5, 0.5, 0.0, Rotation3d.kZero);
          case UNAVAILABLE -> throw new IllegalArgumentException("unavailable has no pose");
        };
    return fieldToTag.transformBy(tagLocalOffset);
  }

  private void applyUnavailable() {
    if (lastAppliedFixture == FixtureSelection.UNAVAILABLE) {
      return;
    }
    visionIO.setFrame(VisionIOSim.Frame.unavailable());
    lastAppliedFixture = FixtureSelection.UNAVAILABLE;
  }
}
