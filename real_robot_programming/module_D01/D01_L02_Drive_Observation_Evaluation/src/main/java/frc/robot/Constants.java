// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

/**
 * Stores robot-wide constants.
 */
public final class Constants {
  private Constants() {}

  /**
   * Stores driver controller constants.
   */
  public static final class OperatorConstants {
    public static final int kDriverControllerPort = 0;
    public static final double kDriverDeadband = 0.08;
    public static final double kDriverAxisSign = -1.0;
    public static final double kDriverMaximumOutput = 1.0;

    private OperatorConstants() {}
  }

  /**
   * Stores drivetrain hardware and test constants.
   */
  public static final class DriveConstants {
    public static final int kLeftLeaderCanId = 11;
    public static final int kLeftFollowerCanId = 8;
    public static final int kRightLeaderCanId = 10;
    public static final int kRightFollowerCanId = 7;

    public static final MotorType kDriveMotorType =
        MotorType.kBrushed;

    public static final IdleMode kDriveIdleMode =
        IdleMode.kBrake;

    public static final boolean kLeftLeaderInverted = true;
    public static final boolean kRightLeaderInverted = false;

    public static final int kDriveCurrentLimitAmps = 60;
    public static final double kVoltageCompensationVolts = 12.0;
    public static final double kOpenLoopRampRateSeconds = 0.25;
    public static final int kConfigurationCanTimeoutMs = 250;

    public static final double kMinimumDriveOutput = -1.0;
    public static final double kMaximumDriveOutput = 1.0;
    public static final double kDriveTestOutput = 0.15;

    private DriveConstants() {}
  }
}
