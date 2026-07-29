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
import frc.robot.Constants.IntakeConstants;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.IntakeMode;

/**
 * Provides high-level intake behavior.
 */
public class IntakeSubsystem extends SubsystemBase {
  private final IntakeIO io;
  private final IntakeIOInputs inputs = new IntakeIOInputs();
  private IntakeMode mode = IntakeMode.STOPPED;

  /**
   * Creates the intake subsystem.
   *
   * @param io real or simulated intake hardware
   */
  public IntakeSubsystem(IntakeIO io) {
    this.io = io;
    stop();
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
  }

  /**
   * Runs the intake at a normalized output.
   *
   * @param output normalized motor output
   */
  public void setOutput(double output) {
    double safeOutput =
        MathUtil.clamp(
            output,
            IntakeConstants.kMinimumIntakeOutput,
            IntakeConstants.kMaximumIntakeOutput);

    io.setOutput(safeOutput);

    if (safeOutput > IntakeConstants.kStoppedIntakeOutput) {
      mode = IntakeMode.INTAKE;
    } else if (safeOutput < IntakeConstants.kStoppedIntakeOutput) {
      mode = IntakeMode.OUTTAKE;
    } else {
      mode = IntakeMode.STOPPED;
    }
  }

  /**
   * Returns the latest intake observation.
   *
   * @return latest intake observation
   */
  public IntakeObservation getObservation() {
    return new IntakeObservation(
        inputs.appliedOutput,
        mode,
        inputs.connected);
  }

  /**
   * Stops the intake.
   */
  public void stop() {
    io.stop();
    mode = IntakeMode.STOPPED;
  }
}
