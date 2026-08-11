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

  /** Stores the normalized driver-input processing contract. */
  public static final class DriverInputConstants {
    /** USB port used by the L19 Xbox input source. */
    public static final int kXboxControllerPort = 0;

    /** Deadband applied independently to each semantic driver axis. */
    public static final double kAxisDeadband = 0.10;

    /** Minimum normalized processed-intent value. */
    public static final double kNormalizedMinimum = -1.0;

    /** Maximum normalized processed-intent value. */
    public static final double kNormalizedMaximum = 1.0;

    private DriverInputConstants() {}
  }

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

    /** Temporary safe L20 robot-relative teleop translation scale, in meters per second. */
    public static final double kTeleopMaxTranslationMetersPerSecond = 1.0;

    /** Temporary safe L20 robot-relative teleop rotation scale, in radians per second. */
    public static final double kTeleopMaxAngularSpeedRadiansPerSecond = 1.0;

    /** Fixed four-module Test-mode translation verification speed, in meters per second. */
    public static final double kFourModuleTestTranslationSpeedMetersPerSecond = 0.30;

    /** Fixed four-module Test-mode CCW rotation verification speed, in radians per second. */
    public static final double kFourModuleTestRotationSpeedRadiansPerSecond = 0.75;

    /** Fixed duration for one four-module Test-mode verification command, in seconds. */
    public static final double kFourModuleTestCommandDurationSeconds = 1.0;

    /** Front Left-only maximum absolute closed-loop drive velocity, in meters per second. */
    public static final double kFrontLeftMaximumDriveVelocityMetersPerSecond = 0.50;

    /** Fixed positive Front Left closed-loop drive test velocity, in meters per second. */
    public static final double kFrontLeftPositiveDriveTestVelocityMetersPerSecond = 0.30;

    /** Fixed negative Front Left closed-loop drive test velocity, in meters per second. */
    public static final double kFrontLeftNegativeDriveTestVelocityMetersPerSecond = -0.30;

    /** Front Left maximum shortest closed-loop steer step, in rotations. */
    public static final double kFrontLeftMaximumSteerStepRotations = 0.125;

    /** Fixed positive Front Left closed-loop steer test step, in rotations. */
    public static final double kFrontLeftPositiveSteerTestStepRotations = 0.0625;

    /** Fixed negative Front Left closed-loop steer test step, in rotations. */
    public static final double kFrontLeftNegativeSteerTestStepRotations = -0.0625;

    /** Front Left closed-loop commissioning timeout, in seconds. */
    public static final double kFrontLeftClosedLoopCommissioningTimeoutSeconds = 1.0;

    /** Maximum Front Left static-friction characterization voltage, in volts. */
    public static final double kFrontLeftDriveStaticFrictionMaximumVoltageVolts = 1.0;

    /** Manual Front Left static-friction characterization voltage increment, in volts. */
    public static final double kFrontLeftDriveStaticFrictionVoltageIncrementVolts = 0.10;

    /** Number of independently triggered Front Left static-friction voltage steps. */
    public static final int kFrontLeftDriveStaticFrictionVoltageStepCount = 10;

    /** Maximum duration of one Front Left static-friction characterization pulse, in seconds. */
    public static final double kFrontLeftDriveStaticFrictionPulseDurationSeconds = 0.25;

    /** Minimum absolute drive rotor speed indicating Front Left breakaway, in rotations per second. */
    public static final double
        kFrontLeftDriveStaticFrictionBreakawayRotorVelocityRotationsPerSecond = 0.10;

    /** Drive velocity Slot 0 proportional gain, in volts per rotation per second. */
    public static final double kDriveVelocitySlot0KpVoltsPerRotationPerSecond = 0.1;

    /** Drive velocity Slot 0 integral gain, in volts per rotation. */
    public static final double kDriveVelocitySlot0KiVoltsPerRotation = 0.0;

    /** Drive velocity Slot 0 derivative gain, in volts per rotation per second squared. */
    public static final double kDriveVelocitySlot0KdVoltsPerRotationPerSecondSquared = 0.0;

    /** Drive velocity Slot 0 velocity feedforward gain, in volts per rotation per second. */
    public static final double kDriveVelocitySlot0KvVoltsPerRotationPerSecond = 0.124;

    /** Steer position Slot 0 proportional gain, in volts per rotation. */
    public static final double kSteerPositionSlot0KpVoltsPerRotation = 100.0;

    /** Steer position Slot 0 integral gain, in volts per rotation second. */
    public static final double kSteerPositionSlot0KiVoltsPerRotationSecond = 0.0;

    /** Steer position Slot 0 derivative gain, in volts per rotation per second. */
    public static final double kSteerPositionSlot0KdVoltsPerRotationPerSecond = 0.5;

    /** Provisional positive magnitude for the Front Left drive commissioning pulse. */
    public static final double kFrontLeftDriveCommissioningDutyCycle = 0.05;

    /** Provisional positive magnitude for the Front Left steer commissioning pulse. */
    public static final double kFrontLeftSteerCommissioningDutyCycle = 0.05;

    /** Provisional maximum duration for a Front Left commissioning pulse, in seconds. */
    public static final double kFrontLeftCommissioningPulseDurationSeconds = 0.25;

    private SwerveConstants() {}
  }
}
