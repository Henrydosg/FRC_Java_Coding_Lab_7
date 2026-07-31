// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;
import frc.robot.io.drive.DriveIO;
import frc.robot.io.drive.DriveIO.DriveIOInputs;
import frc.robot.observation.drive.DriveObservation;

/**
 * Provides high-level drivetrain behavior.
 */
public class DriveSubsystem extends SubsystemBase {
  private final DriveIO io;
  private final DriveIOInputs inputs = new DriveIOInputs();

  /**
   * Creates the drive subsystem.
   *
   * @param io real or simulated drivetrain hardware
   */
  public DriveSubsystem(DriveIO io) {
    this.io = io;
    stop();
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
  }

  /**
   * Returns an immutable observation from the latest completed periodic update.
   *
   * <p>Before the first periodic update, the observation may contain default values.
   *
   * @return latest drivetrain observation
   */
  public DriveObservation getObservation() {
    return new DriveObservation(
        inputs.leftAppliedOutput,
        inputs.rightAppliedOutput);
  }

  /**
   * Drives the left and right sides independently.
   *
   * @param leftOutput left-side output
   * @param rightOutput right-side output
   */
  public void tankDrive(
      double leftOutput,
      double rightOutput) {
    double safeLeftOutput =
        MathUtil.clamp(
            leftOutput,
            DriveConstants.kMinimumDriveOutput,
            DriveConstants.kMaximumDriveOutput);

    double safeRightOutput =
        MathUtil.clamp(
            rightOutput,
            DriveConstants.kMinimumDriveOutput,
            DriveConstants.kMaximumDriveOutput);

    io.setTankOutputs(
        safeLeftOutput,
        safeRightOutput);
  }

  /**
   * Stops the complete drivetrain.
   */
  public void stop() {
    io.stop();
  }
}
