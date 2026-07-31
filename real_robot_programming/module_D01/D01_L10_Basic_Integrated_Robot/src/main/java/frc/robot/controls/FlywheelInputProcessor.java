// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import frc.robot.Constants.FlywheelConstants;

/**
 * Converts the driver button request into flywheel intent.
 */
public final class FlywheelInputProcessor {
  /**
   * Selects the test or stopped output from the driver request.
   *
   * @param flywheelRequested whether the driver requests flywheel output
   * @return normalized flywheel output
   */
  public double process(boolean flywheelRequested) {
    if (flywheelRequested) {
      return FlywheelConstants.kManualShootOutput;
    }

    return FlywheelConstants.kStoppedFlywheelOutput;
  }
}
