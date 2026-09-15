// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;

/** Converts semantic raw driver axes into bounded normalized driver intent. */
public final class DriverInputProcessor {
  private DriverInputProcessor() {}

  /**
   * Immutable normalized driver intent.
   *
   * @param forward normalized forward intent
   * @param strafe normalized strafe intent
   * @param rotation normalized rotation intent
   */
  public record ProcessedDriverIntent(double forward, double strafe, double rotation) {}

  /**
   * Processes three semantic raw axes independently.
   *
   * @param forward raw forward axis
   * @param strafe raw strafe axis
   * @param rotation raw rotation axis
   * @return finite, shaped, clamped normalized driver intent
   */
  public static ProcessedDriverIntent process(
      double forward, double strafe, double rotation) {
    return new ProcessedDriverIntent(
        processAxis(forward), processAxis(strafe), processAxis(rotation));
  }

  private static double processAxis(double rawAxis) {
    if (!Double.isFinite(rawAxis)) {
      return 0.0;
    }

    double deadbandedAxis =
        MathUtil.applyDeadband(
            rawAxis, Constants.DriverInputConstants.kAxisDeadband);
    double shapedAxis = Math.copySign(deadbandedAxis * deadbandedAxis, deadbandedAxis);
    return MathUtil.clamp(
        shapedAxis,
        Constants.DriverInputConstants.kNormalizedMinimum,
        Constants.DriverInputConstants.kNormalizedMaximum);
  }
}
