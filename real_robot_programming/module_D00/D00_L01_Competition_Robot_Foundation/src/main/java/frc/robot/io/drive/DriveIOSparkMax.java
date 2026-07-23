// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.drive;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import frc.robot.Constants.DriveConstants;

/**
 * EN: Controls the four drivetrain SPARK MAX motor controllers.
 * VI: Điều khiển bốn bộ điều khiển motor SPARK MAX của drivetrain.
 */
public class DriveIOSparkMax implements DriveIO {
  // EN: Main motor controller for the left drivetrain.
  // VI: Bộ điều khiển chính của drivetrain bên trái.
  private final SparkMax leftLeader =
      new SparkMax(
          DriveConstants.kLeftLeaderCanId,
          DriveConstants.kDriveMotorType);

  // EN: Copies commands from the left leader.
  // VI: Tự động sao chép lệnh từ Left Leader.
  private final SparkMax leftFollower =
      new SparkMax(
          DriveConstants.kLeftFollowerCanId,
          DriveConstants.kDriveMotorType);

  // EN: Main motor controller for the right drivetrain.
  // VI: Bộ điều khiển chính của drivetrain bên phải.
  private final SparkMax rightLeader =
      new SparkMax(
          DriveConstants.kRightLeaderCanId,
          DriveConstants.kDriveMotorType);

  // EN: Copies commands from the right leader.
  // VI: Tự động sao chép lệnh từ Right Leader.
  private final SparkMax rightFollower =
      new SparkMax(
          DriveConstants.kRightFollowerCanId,
          DriveConstants.kDriveMotorType);

  /**
   * EN: Creates and configures the real drivetrain hardware.
   * VI: Khởi tạo và cấu hình phần cứng drivetrain thật.
   */
  public DriveIOSparkMax() {
    setConfigurationTimeouts();

    configureLeftLeader();
    configureRightLeader();
    configureLeftFollower();
    configureRightFollower();

    stop();
  }

  /**
   * EN: Sends commands only to the two leaders.
   * VI: Chỉ gửi lệnh trực tiếp đến hai Leader.
   *
   * <p>The followers automatically copy their leaders.
   * Hai Follower sẽ tự động làm theo Leader tương ứng.
   */
  @Override
  public void setTankOutputs(
      double leftOutput,
      double rightOutput) {
    leftLeader.set(leftOutput);
    rightLeader.set(rightOutput);
  }

  /**
   * EN: Stops both drivetrain leaders.
   * VI: Dừng hai Leader của drivetrain.
   *
   * <p>The followers stop automatically with their leaders.
   * Hai Follower cũng tự động dừng theo Leader.
   */
  @Override
  public void stop() {
    leftLeader.stopMotor();
    rightLeader.stopMotor();
  }

  /**
   * EN: Sets CAN timeouts used during startup configuration.
   * VI: Đặt thời gian chờ CAN khi robot khởi động.
   */
  private void setConfigurationTimeouts() {
    leftLeader.setCANTimeout(
        DriveConstants.kConfigurationCanTimeoutMs);

    leftFollower.setCANTimeout(
        DriveConstants.kConfigurationCanTimeoutMs);

    rightLeader.setCANTimeout(
        DriveConstants.kConfigurationCanTimeoutMs);

    rightFollower.setCANTimeout(
        DriveConstants.kConfigurationCanTimeoutMs);
  }

  /**
   * EN: Configures the left leader.
   * VI: Cấu hình Leader bên trái.
   */
  private void configureLeftLeader() {
    SparkMaxConfig config = new SparkMaxConfig();

    config
        .smartCurrentLimit(
            DriveConstants.kDriveCurrentLimitAmps)
        .voltageCompensation(
            DriveConstants.kVoltageCompensationVolts)
        .inverted(
            DriveConstants.kLeftLeaderInverted);

    leftLeader.configure(
        config,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  /**
   * EN: Configures the right leader.
   * VI: Cấu hình Leader bên phải.
   */
  private void configureRightLeader() {
    SparkMaxConfig config = new SparkMaxConfig();

    config
        .smartCurrentLimit(
            DriveConstants.kDriveCurrentLimitAmps)
        .voltageCompensation(
            DriveConstants.kVoltageCompensationVolts)
        .inverted(
            DriveConstants.kRightLeaderInverted);

    rightLeader.configure(
        config,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  /**
   * EN: Configures CAN 8 to follow CAN 11.
   * VI: Cấu hình CAN 8 tự động làm theo CAN 11.
   */
  private void configureLeftFollower() {
    SparkMaxConfig config = new SparkMaxConfig();

    config
        .smartCurrentLimit(
            DriveConstants.kDriveCurrentLimitAmps)
        .voltageCompensation(
            DriveConstants.kVoltageCompensationVolts)
        .follow(leftLeader);

    leftFollower.configure(
        config,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }

  /**
   * EN: Configures CAN 7 to follow CAN 10.
   * VI: Cấu hình CAN 7 tự động làm theo CAN 10.
   */
  private void configureRightFollower() {
    SparkMaxConfig config = new SparkMaxConfig();

    config
        .smartCurrentLimit(
            DriveConstants.kDriveCurrentLimitAmps)
        .voltageCompensation(
            DriveConstants.kVoltageCompensationVolts)
        .follow(rightLeader);

    rightFollower.configure(
        config,
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);
  }
}