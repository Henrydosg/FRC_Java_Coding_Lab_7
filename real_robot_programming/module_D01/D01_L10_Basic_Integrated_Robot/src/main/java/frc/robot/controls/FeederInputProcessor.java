// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import frc.robot.Constants.FeederConstants;

/**
 * Converts driver button requests into feeder intent.
 */
public final class FeederInputProcessor {
  /**
   * Selects feed, reverse, or stopped output from the driver requests.
   *
   * @param feedRequested whether the driver requests forward feeding
   * @param reverseRequested whether the driver requests reverse feeding
   * @return normalized feeder output
   */
  public double process(
      boolean feedRequested,
      boolean reverseRequested) {
    if (feedRequested == reverseRequested) {
      return FeederConstants.kStoppedFeederOutput;
    }

    if (feedRequested) {
      return FeederConstants.kManualFeedOutput;
    }

    return FeederConstants.kManualReverseOutput;
  }
}
