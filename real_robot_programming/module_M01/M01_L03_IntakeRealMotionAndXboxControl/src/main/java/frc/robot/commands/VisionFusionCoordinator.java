// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import java.util.Objects;

/**
 * Performs the narrow post-scheduler handoff from the latest qualified vision result to Swerve.
 *
 * <p>This is a plain Java coordinator. It owns no estimator, vendor API, NetworkTables object,
 * command lifecycle, or telemetry publication.
 */
public final class VisionFusionCoordinator {
  private final VisionSubsystem visionSubsystem;
  private final SwerveSubsystem swerveSubsystem;

  /**
   * Creates one vendor-neutral fusion handoff.
   *
   * @param visionSubsystem qualified-measurement source
   * @param swerveSubsystem guarded estimator-admission owner
   */
  public VisionFusionCoordinator(
      VisionSubsystem visionSubsystem, SwerveSubsystem swerveSubsystem) {
    this.visionSubsystem = Objects.requireNonNull(visionSubsystem, "visionSubsystem");
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
  }

  /**
   * Attempts at most the current latest qualified measurement once.
   *
   * <p>Repeated invocation is safe because Swerve owns timestamp duplicate and ordering guards.
   */
  public void periodic() {
    visionSubsystem
        .getLatestQualifiedMeasurement()
        .ifPresent(swerveSubsystem::admitVisionMeasurement);
  }
}

