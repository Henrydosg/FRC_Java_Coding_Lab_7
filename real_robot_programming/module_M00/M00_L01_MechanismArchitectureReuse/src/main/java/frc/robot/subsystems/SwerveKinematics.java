// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import java.util.Objects;

/**
 * Converts robot-relative chassis speeds into ordered Swerve module states.
 *
 * <p>This class performs kinematics only. It does not access hardware, publish telemetry, or
 * control module outputs.
 */
public final class SwerveKinematics {
  private static final int MODULE_COUNT = 4;

  private final SwerveDriveKinematics kinematics;

  /** Creates the kinematics model from the verified robot geometry constants. */
  public SwerveKinematics() {
    kinematics = createDriveKinematics();
  }

  /** Creates an independent WPILib kinematics instance from the single geometry authority. */
  static SwerveDriveKinematics createDriveKinematics() {
    double halfWheelbaseMeters = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidthMeters = Constants.SwerveConstants.kTrackWidthMeters / 2.0;

    return new SwerveDriveKinematics(
        new Translation2d(halfWheelbaseMeters, halfTrackWidthMeters),
        new Translation2d(halfWheelbaseMeters, -halfTrackWidthMeters),
        new Translation2d(-halfWheelbaseMeters, halfTrackWidthMeters),
        new Translation2d(-halfWheelbaseMeters, -halfTrackWidthMeters));
  }

  /**
   * Converts robot-relative chassis speeds into FrontLeft, FrontRight, BackLeft, BackRight states.
   *
   * @param chassisSpeeds robot-relative chassis speeds
   * @return newly calculated module states in fixed FL, FR, BL, BR order
   * @throws NullPointerException when chassisSpeeds is null
   */
  public SwerveModuleState[] toModuleStates(ChassisSpeeds chassisSpeeds) {
    return kinematics.toSwerveModuleStates(
        Objects.requireNonNull(chassisSpeeds, "chassisSpeeds"));
  }

  /**
   * Converts actual module states into robot-relative chassis speeds using the same geometry.
   *
   * @param moduleStates module states in fixed FL, FR, BL, BR order
   * @return a newly allocated robot-relative chassis-speed value
   */
  public ChassisSpeeds toChassisSpeeds(SwerveModuleState[] moduleStates) {
    SwerveModuleState[] acceptedStates = Objects.requireNonNull(moduleStates, "moduleStates");
    if (acceptedStates.length != MODULE_COUNT) {
      throw new IllegalArgumentException("moduleStates must contain exactly four states");
    }

    SwerveModuleState[] copiedStates = new SwerveModuleState[MODULE_COUNT];
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      SwerveModuleState acceptedState =
          Objects.requireNonNull(acceptedStates[moduleIndex], "moduleStates element");
      if (!Double.isFinite(acceptedState.speedMetersPerSecond)
          || acceptedState.angle == null
          || !Double.isFinite(acceptedState.angle.getRadians())) {
        throw new IllegalArgumentException("moduleStates must contain only finite states");
      }
      copiedStates[moduleIndex] =
          new SwerveModuleState(
              acceptedState.speedMetersPerSecond,
              new Rotation2d(acceptedState.angle.getRadians()));
    }

    ChassisSpeeds calculatedSpeeds = kinematics.toChassisSpeeds(copiedStates);
    if (!Double.isFinite(calculatedSpeeds.vxMetersPerSecond)
        || !Double.isFinite(calculatedSpeeds.vyMetersPerSecond)
        || !Double.isFinite(calculatedSpeeds.omegaRadiansPerSecond)) {
      throw new IllegalStateException("Inverse Swerve kinematics produced nonfinite speeds");
    }
    return new ChassisSpeeds(
        calculatedSpeeds.vxMetersPerSecond,
        calculatedSpeeds.vyMetersPerSecond,
        calculatedSpeeds.omegaRadiansPerSecond);
  }
}
