// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

/**
 * Converts a signed driver request into drive intent.
 */
public final class DriveInputProcessor {
  /**
   * Preserves the signed test request without scaling.
   *
   * @param rawSignedDriveRequest signed drive request
   * @return processed signed drive output
   */
  public double process(double rawSignedDriveRequest) {
    return rawSignedDriveRequest;
  }
}
