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
 * Controls the four drivetrain SPARK MAX motor controllers.
 */
public class DriveIOSparkMax implements DriveIO {
  private final SparkMax leftLeader =
      new SparkMax(
          DriveConstants.kLeftLeaderCanId,
          DriveConstants.kDriveMotorType);

  private final SparkMax leftFollower =
      new SparkMax(
          DriveConstants.kLeftFollowerCanId,
          DriveConstants.kDriveMotorType);

  private final SparkMax rightLeader =
      new SparkMax(
          DriveConstants.kRightLeaderCanId,
          DriveConstants.kDriveMotorType);

  private final SparkMax rightFollower =
      new SparkMax(
          DriveConstants.kRightFollowerCanId,
          DriveConstants.kDriveMotorType);

  /**
   * Creates and configures the real drivetrain hardware.
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
   * Updates applied output observations from the leaders.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(DriveIOInputs inputs) {
    inputs.leftAppliedOutput = leftLeader.getAppliedOutput();
    inputs.rightAppliedOutput = rightLeader.getAppliedOutput();
  }

  /**
   * Sends commands only to the two leaders.
   *
   * <p>The followers automatically copy their leaders.
   */
  @Override
  public void setTankOutputs(
      double leftOutput,
      double rightOutput) {
    leftLeader.set(leftOutput);
    rightLeader.set(rightOutput);
  }

  /**
   * Stops both drivetrain leaders.
   *
   * <p>The followers stop automatically with their leaders.
   */
  @Override
  public void stop() {
    leftLeader.stopMotor();
    rightLeader.stopMotor();
  }

  /**
   * Sets CAN timeouts used during startup configuration.
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
   * Configures the left leader.
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
   * Configures the right leader.
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
   * Configures the left follower.
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
   * Configures the right follower.
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
