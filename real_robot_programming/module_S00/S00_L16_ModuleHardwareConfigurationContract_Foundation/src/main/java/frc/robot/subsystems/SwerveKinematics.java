// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

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
  private final Translation2d frontLeftLocation;
  private final Translation2d frontRightLocation;
  private final Translation2d backLeftLocation;
  private final Translation2d backRightLocation;
  private final SwerveDriveKinematics kinematics;

  /** Creates the kinematics model from the verified robot geometry constants. */
  public SwerveKinematics() {
    double halfWheelbaseMeters = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidthMeters = Constants.SwerveConstants.kTrackWidthMeters / 2.0;

    frontLeftLocation = new Translation2d(halfWheelbaseMeters, halfTrackWidthMeters);
    frontRightLocation = new Translation2d(halfWheelbaseMeters, -halfTrackWidthMeters);
    backLeftLocation = new Translation2d(-halfWheelbaseMeters, halfTrackWidthMeters);
    backRightLocation = new Translation2d(-halfWheelbaseMeters, -halfTrackWidthMeters);

    kinematics =
        new SwerveDriveKinematics(
            frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
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
}
