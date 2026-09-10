// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.vision.VisionIO;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import frc.robot.observation.vision.VisionObservation;
import frc.robot.observation.vision.VisionObservation.State;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Owns one selected VisionIO source and publishes its immutable observation snapshot.
 *
 * <p>This is an observation-only runtime owner. It does not estimate pose, fuse measurements,
 * schedule commands, or write mechanism outputs.
 */
public final class VisionSubsystem extends SubsystemBase {
  private final VisionIO visionIO;
  private final VisionIOInputs inputs = new VisionIOInputs();
  private VisionObservation latestObservation = new VisionObservation(State.UNAVAILABLE, List.of());

  /**
   * Creates the periodic observation owner for one selected vision implementation.
   *
   * @param visionIO selected real or simulation vision source
   */
  public VisionSubsystem(VisionIO visionIO) {
    this.visionIO = Objects.requireNonNull(visionIO, "visionIO");
  }

  /** Refreshes IO once and replaces the immutable observation snapshot. */
  @Override
  public void periodic() {
    visionIO.updateInputs(inputs);
    latestObservation = toObservation(inputs);
  }

  /**
   * Returns the latest immutable vendor-neutral observation.
   *
   * @return latest observation; initially UNAVAILABLE before the first periodic cycle
   */
  public VisionObservation getObservation() {
    return latestObservation;
  }

  private static VisionObservation toObservation(VisionIOInputs inputs) {
    if (!inputs.available) {
      return new VisionObservation(State.UNAVAILABLE, List.of());
    }
    if (!inputs.connected) {
      return new VisionObservation(State.DISCONNECTED, List.of());
    }
    if (!inputs.sampleValid) {
      return new VisionObservation(State.INVALID_SAMPLE, List.of());
    }
    if (inputs.targets == null || inputs.targets.isEmpty()) {
      return new VisionObservation(State.NO_TARGETS, List.of());
    }

    try {
      List<TargetObservation> targets = new ArrayList<>(inputs.targets.size());
      for (VisionTargetInputs target : inputs.targets) {
        if (target == null) {
          throw new IllegalArgumentException("target input must not be null");
        }
        targets.add(new TargetObservation(target.tagId(), target.cameraToTarget()));
      }
      return new VisionObservation(State.TARGETS_PRESENT, targets);
    } catch (IllegalArgumentException exception) {
      return new VisionObservation(State.INVALID_SAMPLE, List.of());
    }
  }
}
