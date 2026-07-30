// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.OperatorConstants;

/**
 * Converts driver trigger requests into intake intent.
 */
public final class IntakeInputProcessor {
  /**
   * Selects intake, outtake, or stopped output from the trigger values.
   *
   * @param intakeTrigger right-trigger value
   * @param outtakeTrigger left-trigger value
   * @return normalized intake output
   */
  public double process(
      double intakeTrigger,
      double outtakeTrigger) {
    boolean intakeRequested =
        intakeTrigger
            >= OperatorConstants.kIntakeTriggerThreshold;
    boolean outtakeRequested =
        outtakeTrigger
            >= OperatorConstants.kIntakeTriggerThreshold;

    if (intakeRequested == outtakeRequested) {
      return IntakeConstants.kStoppedIntakeOutput;
    }

    if (intakeRequested) {
      return IntakeConstants.kIntakeOutput;
    }

    return IntakeConstants.kOuttakeOutput;
  }
}
