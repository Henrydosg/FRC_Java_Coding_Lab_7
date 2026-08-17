// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import java.util.Objects;

/**
 * Converts robot-relative chassis speeds into final ordered Swerve module outputs.
 *
 * <p>An exact zero chassis request produces zero-speed states at the current measured module
 * angles. Nonzero requests perform kinematics, module-state optimization, and wheel-speed
 * desaturation in that order. The pipeline is deterministic and independent of hardware access.
 */
public final class SwerveOutputPipeline {
  private static final int MODULE_COUNT = 4;

  private final SwerveKinematics kinematics;
  private final SwerveModuleStateOptimizer optimizer;
  private final double maximumWheelSpeedMetersPerSecond;

  /** Creates a pipeline using the configured maximum wheel speed. */
  public SwerveOutputPipeline() {
    this(Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond);
  }

  /**
   * Creates a pipeline with an explicit maximum wheel speed.
   *
   * @param maximumWheelSpeedMetersPerSecond positive finite maximum wheel speed
   * @throws IllegalArgumentException when the maximum wheel speed is not positive and finite
   */
  public SwerveOutputPipeline(double maximumWheelSpeedMetersPerSecond) {
    if (!Double.isFinite(maximumWheelSpeedMetersPerSecond)
        || maximumWheelSpeedMetersPerSecond <= 0.0) {
      throw new IllegalArgumentException(
          "maximumWheelSpeedMetersPerSecond must be positive and finite");
    }

    kinematics = new SwerveKinematics();
    optimizer = new SwerveModuleStateOptimizer();
    this.maximumWheelSpeedMetersPerSecond = maximumWheelSpeedMetersPerSecond;
  }

  /**
   * Converts robot-relative chassis speeds into optimized and desaturated module states.
   *
   * <p>Returned states are always ordered Front Left, Front Right, Back Left, Back Right. The
   * caller-owned chassis speeds and current-angle array are not mutated or retained.
   *
   * @param chassisSpeeds robot-relative chassis speeds
   * @param currentAngles current module angles in FL, FR, BL, BR order
   * @return newly allocated optimized and desaturated module states
   * @throws NullPointerException when the chassis speeds, angle array, or an angle is null
   * @throws IllegalArgumentException when the angle array does not contain four angles
   */
  public SwerveModuleState[] toModuleStates(
      ChassisSpeeds chassisSpeeds, Rotation2d[] currentAngles) {
    ChassisSpeeds acceptedSpeeds = Objects.requireNonNull(chassisSpeeds, "chassisSpeeds");
    Rotation2d[] acceptedAngles = Objects.requireNonNull(currentAngles, "currentAngles");
    if (acceptedAngles.length != MODULE_COUNT) {
      throw new IllegalArgumentException("currentAngles must contain exactly four angles");
    }

    ChassisSpeeds copiedSpeeds =
        new ChassisSpeeds(
            acceptedSpeeds.vxMetersPerSecond,
            acceptedSpeeds.vyMetersPerSecond,
            acceptedSpeeds.omegaRadiansPerSecond);
    Rotation2d[] copiedAngles = new Rotation2d[MODULE_COUNT];
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      Rotation2d acceptedAngle =
          Objects.requireNonNull(acceptedAngles[moduleIndex], "currentAngles element");
      copiedAngles[moduleIndex] = new Rotation2d(acceptedAngle.getRadians());
    }

    if (isExactlyZero(copiedSpeeds)) {
      SwerveModuleState[] zeroStates = new SwerveModuleState[MODULE_COUNT];
      for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
        zeroStates[moduleIndex] =
            new SwerveModuleState(
                0.0, new Rotation2d(copiedAngles[moduleIndex].getRadians()));
      }
      return zeroStates;
    }

    SwerveModuleState[] desiredStates = kinematics.toModuleStates(copiedSpeeds);
    SwerveModuleState[] finalStates = new SwerveModuleState[MODULE_COUNT];

    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      finalStates[moduleIndex] =
          optimizer.optimize(desiredStates[moduleIndex], copiedAngles[moduleIndex]);
    }

    SwerveDriveKinematics.desaturateWheelSpeeds(
        finalStates, maximumWheelSpeedMetersPerSecond);
    return finalStates;
  }

  private static boolean isExactlyZero(ChassisSpeeds chassisSpeeds) {
    return chassisSpeeds.vxMetersPerSecond == 0.0
        && chassisSpeeds.vyMetersPerSecond == 0.0
        && chassisSpeeds.omegaRadiansPerSecond == 0.0;
  }
}
