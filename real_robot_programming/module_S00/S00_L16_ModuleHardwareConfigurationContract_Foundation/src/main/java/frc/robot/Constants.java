// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import edu.wpi.first.math.util.Units;

/**
 * Stores robot-wide configuration constants.
 */
public final class Constants {
  private Constants() {}

  /**
   * Stores verified Swerve hardware identifiers, geometry, and output limits.
   */
  public static final class SwerveConstants {
    public static final String kPhoenix6Version = "26.3.0";

    public static final int kPigeonCanId = 20;

    public static final int kFrontLeftDriveCanId = 21;
    public static final int kFrontLeftSteerCanId = 22;
    public static final int kFrontLeftEncoderCanId = 23;

    public static final int kFrontRightDriveCanId = 24;
    public static final int kFrontRightSteerCanId = 25;
    public static final int kFrontRightEncoderCanId = 26;

    public static final int kBackLeftDriveCanId = 27;
    public static final int kBackLeftSteerCanId = 28;
    public static final int kBackLeftEncoderCanId = 29;

    public static final int kBackRightDriveCanId = 30;
    public static final int kBackRightSteerCanId = 31;
    public static final int kBackRightEncoderCanId = 32;

    public static final double kDriveGearRatio = 7.846153846153847;
    public static final double kSteerGearRatio = 15.42857142857143;

    public static final boolean kFrontLeftDriveInverted = false;
    public static final boolean kFrontRightDriveInverted = true;
    public static final boolean kBackLeftDriveInverted = false;
    public static final boolean kBackRightDriveInverted = true;

    public static final boolean kFrontLeftSteerInverted = true;
    public static final boolean kFrontRightSteerInverted = true;
    public static final boolean kBackLeftSteerInverted = true;
    public static final boolean kBackRightSteerInverted = true;

    public static final boolean kFrontLeftEncoderInverted = false;
    public static final boolean kFrontRightEncoderInverted = false;
    public static final boolean kBackLeftEncoderInverted = false;
    public static final boolean kBackRightEncoderInverted = false;

    public static final double kFrontLeftEncoderOffsetRotations = 0.067138671875;
    public static final double kFrontRightEncoderOffsetRotations = 0.02099609375;
    public static final double kBackLeftEncoderOffsetRotations = 0.464599609375;
    public static final double kBackRightEncoderOffsetRotations = -0.052978515625;

    public static final double kDriveSupplyCurrentLimitAmps = 70.0;
    public static final boolean kDriveSupplyCurrentLimitEnabled = true;
    public static final double kSteerStatorCurrentLimitAmps = 60.0;
    public static final boolean kSteerStatorCurrentLimitEnabled = true;
    public static final double kWheelDiameterMeters = Units.inchesToMeters(4.0);
    public static final double kWheelRadiusMeters = Units.inchesToMeters(2.0);
    public static final double kWheelbaseMeters = Units.inchesToMeters(21.5);
    public static final double kTrackWidthMeters = Units.inchesToMeters(21.5);

    /** Configured maximum wheel speed used by the pure output pipeline, in meters per second. */
    public static final double kMaxWheelSpeedMetersPerSecond = 4.0;

    /** Provisional positive magnitude for the Front Left drive commissioning pulse. */
    public static final double kFrontLeftDriveCommissioningDutyCycle = 0.05;

    /** Provisional positive magnitude for the Front Left steer commissioning pulse. */
    public static final double kFrontLeftSteerCommissioningDutyCycle = 0.05;

    /** Provisional maximum duration for a Front Left commissioning pulse, in seconds. */
    public static final double kFrontLeftCommissioningPulseDurationSeconds = 0.25;

    private SwerveConstants() {}
  }
}
