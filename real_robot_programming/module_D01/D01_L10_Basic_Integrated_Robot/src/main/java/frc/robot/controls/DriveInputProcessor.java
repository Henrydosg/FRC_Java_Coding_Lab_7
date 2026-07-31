// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;

/**
 * Converts a signed driver request into drive intent.
 */
public final class DriveInputProcessor {
  /**
   * Applies the configured deadband, axis direction, and maximum output to a driver request.
   *
   * @param rawSignedDriveRequest signed drive request
   * @return processed signed drive output
   */
  public double process(double rawSignedDriveRequest) {
    double processedOutput =
        MathUtil.applyDeadband(
            rawSignedDriveRequest,
            OperatorConstants.kDriverDeadband)
            * OperatorConstants.kDriverAxisSign
            * OperatorConstants.kDriverMaximumOutput;

    return MathUtil.clamp(
        processedOutput,
        DriveConstants.kMinimumDriveOutput,
        DriveConstants.kMaximumDriveOutput);
  }
}
