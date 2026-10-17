// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import java.util.Objects;

/**
 * Provides pure desired-state optimization for one Swerve module.
 *
 * <p>The caller's mutable desired state is copied before WPILib optimization is applied.
 */
public final class SwerveModuleStateOptimizer {
  /** Creates the stateless optimizer. */
  public SwerveModuleStateOptimizer() {}

  /**
   * Optimizes one desired state against the current module angle.
   *
   * @param desiredState desired module speed and angle
   * @param currentAngle current module angle
   * @return a new optimized module state
   * @throws NullPointerException when either input is null
   */
  public SwerveModuleState optimize(
      SwerveModuleState desiredState, Rotation2d currentAngle) {
    SwerveModuleState copiedState =
        Objects.requireNonNull(desiredState, "desiredState");
    Rotation2d acceptedCurrentAngle =
        Objects.requireNonNull(currentAngle, "currentAngle");
    SwerveModuleState optimizedState =
        new SwerveModuleState(copiedState.speedMetersPerSecond, copiedState.angle);
    optimizedState.optimize(acceptedCurrentAngle);
    return optimizedState;
  }
}
