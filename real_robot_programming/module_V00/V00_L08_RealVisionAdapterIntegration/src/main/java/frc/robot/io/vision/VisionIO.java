// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import edu.wpi.first.math.geometry.Transform3d;
import java.util.List;

/** Defines vendor-neutral, one-cycle vision acquisition transport. */
public interface VisionIO {
  /**
   * Mutable transport snapshot populated completely by one future VisionIO implementation cycle.
   *
   * <p>The implementation replaces every field and the target collection on every update. This
   * type is not an immutable Observation and must not be retained as a public domain model.
   */
  class VisionIOInputs {
    /** True when the selected implementation can supply vision data. */
    public boolean available;

    /** True when the available source or camera is connected. */
    public boolean connected;

    /** True when this acquisition cycle is structurally coherent, not quality-approved. */
    public boolean sampleValid;

    /** Targets observed during this acquisition cycle, in adapter acquisition order. */
    public List<VisionTargetInputs> targets = List.of();
  }

  /**
   * One raw target transport value from an acquisition cycle.
   *
   * <p>{@code cameraToTarget} is the target pose relative to the camera using WPILib NWU geometry,
   * meters, and radians. Domain validation occurs at the immutable Observation boundary.
   *
   * @param tagId raw AprilTag identity supplied by the adapter
   * @param cameraToTarget target-relative-to-camera transform
   */
  record VisionTargetInputs(int tagId, Transform3d cameraToTarget) {}

  /**
   * Updates one complete mutable vision acquisition snapshot.
   *
   * @param inputs transport snapshot to replace for the current acquisition cycle
   */
  void updateInputs(VisionIOInputs inputs);
}
