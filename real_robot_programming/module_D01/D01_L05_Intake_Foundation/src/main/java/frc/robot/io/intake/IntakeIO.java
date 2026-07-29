// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

/**
 * Defines the hardware operations required by the intake mechanism.
 */
public interface IntakeIO {
  /**
   * Stores intake hardware observations for one periodic cycle.
   */
  class IntakeIOInputs {
    public double appliedOutput;
    public double positionRotations;
    public double velocityRpm;
    public boolean connected;
  }

  /**
   * Updates the intake observation snapshot.
   *
   * @param inputs snapshot to update
   */
  void updateInputs(IntakeIOInputs inputs);

  /**
   * Sets the normalized intake motor output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  void setOutput(double output);

  /**
   * Stops the intake motor.
   */
  void stop();
}
