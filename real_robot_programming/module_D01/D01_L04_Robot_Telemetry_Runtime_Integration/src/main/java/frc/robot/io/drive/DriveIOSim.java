// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.drive;

/**
 * Stores simulated drivetrain outputs without accessing physical hardware.
 */
public class DriveIOSim implements DriveIO {
  private static final double kStoppedOutput = 0.0;

  private double leftAppliedOutput;
  private double rightAppliedOutput;

  /**
   * Updates the drivetrain observation snapshot from the simulated outputs.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(DriveIOInputs inputs) {
    inputs.leftAppliedOutput = leftAppliedOutput;
    inputs.rightAppliedOutput = rightAppliedOutput;
  }

  /**
   * Stores the requested left and right simulated drivetrain outputs.
   *
   * @param leftOutput left-side output from -1.0 to 1.0
   * @param rightOutput right-side output from -1.0 to 1.0
   */
  @Override
  public void setTankOutputs(
      double leftOutput,
      double rightOutput) {
    leftAppliedOutput = leftOutput;
    rightAppliedOutput = rightOutput;
  }

  /**
   * Stops both simulated drivetrain sides.
   */
  @Override
  public void stop() {
    leftAppliedOutput = kStoppedOutput;
    rightAppliedOutput = kStoppedOutput;
  }
}
