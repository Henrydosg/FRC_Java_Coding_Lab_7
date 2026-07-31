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
    public static final double kIntakeTriggerThreshold = 0.5;

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

  /**
   * Stores confirmed intake hardware constants.
   */
  public static final class IntakeConstants {
    public static final int kIntakeMotorCanId = 12;
    public static final boolean kIntakeMotorInverted = false;
    public static final boolean kIntakeBrakeModeEnabled = true;
    public static final double kIntakeSupplyCurrentLimitAmps = 40.0;
    public static final boolean kIntakeSupplyCurrentLimitEnabled = true;
    public static final double kIntakeStatorCurrentLimitAmps = 80.0;
    public static final boolean kIntakeStatorCurrentLimitEnabled = true;
    public static final double kIntakeOpenLoopRampSeconds = 0.20;
    public static final double kIntakePeakForwardDutyCycle = 0.50;
    public static final double kIntakePeakReverseDutyCycle = -0.50;
    public static final double kMinimumIntakeOutput = -1.0;
    public static final double kMaximumIntakeOutput = 1.0;
    public static final double kStoppedIntakeOutput = 0.0;
    public static final double kIntakeOutput = 0.5;
    public static final double kOuttakeOutput = -0.5;

    private IntakeConstants() {}
  }

  /**
   * Stores confirmed flywheel hardware and safety constants.
   */
  public static final class FlywheelConstants {
    public static final int kFlywheelMotorCanId = 9;
    public static final boolean kFlywheelMotorInverted = false;
    public static final boolean kFlywheelBrakeModeEnabled = false;
    public static final double kFlywheelSupplyCurrentLimitAmps = 25.0;
    public static final boolean kFlywheelSupplyCurrentLimitEnabled = true;
    public static final double kFlywheelStatorCurrentLimitAmps = 40.0;
    public static final boolean kFlywheelStatorCurrentLimitEnabled = true;
    public static final double kFlywheelOpenLoopRampSeconds = 1.0;
    public static final double kFlywheelPeakForwardDutyCycle = 0.20;
    public static final double kFlywheelPeakReverseDutyCycle = 0.0;
    public static final double kStoppedFlywheelOutput = 0.0;
    public static final double kFlywheelTestOutput = 0.10;

    private FlywheelConstants() {}
  }

  /**
   * Stores telemetry topic configuration.
   */
  public static final class TelemetryConstants {
    public static final String kDriveTableName = "Drive";
    public static final String kIntakeTableName = "Intake";
    public static final String kIntakeAppliedOutputKey =
        "AppliedOutput";
    public static final String kIntakeModeKey = "Mode";
    public static final String kIntakeConnectedKey = "Connected";
    public static final String kIntakeSupplyVoltageVoltsKey =
        "SupplyVoltageVolts";
    public static final String kIntakeSupplyCurrentAmpsKey =
        "SupplyCurrentAmps";
    public static final String kIntakeStatorCurrentAmpsKey =
        "StatorCurrentAmps";
    public static final String kIntakeTemperatureCelsiusKey =
        "TemperatureCelsius";
    public static final String kIntakePositionRotationsKey =
        "PositionRotations";
    public static final String kIntakeVelocityRpmKey =
        "VelocityRPM";
    public static final String kIntakeConfigurationHealthyKey =
        "ConfigurationHealthy";
    public static final String kFlywheelTableName = "Flywheel";
    public static final String kFlywheelAppliedOutputKey =
        "AppliedOutput";
    public static final String kFlywheelVelocityRpmKey =
        "VelocityRpm";
    public static final String kFlywheelSupplyCurrentAmpsKey =
        "SupplyCurrentAmps";
    public static final String kFlywheelStatorCurrentAmpsKey =
        "StatorCurrentAmps";
    public static final String kFlywheelTemperatureCelsiusKey =
        "TemperatureCelsius";
    public static final String kFlywheelConnectedKey =
        "Connected";
    public static final String kFlywheelConfigurationHealthyKey =
        "ConfigurationHealthy";
    public static final String kFlywheelModeKey = "Mode";

    private TelemetryConstants() {}
  }
}
