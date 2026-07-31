// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.drive;

/**
 * Defines the hardware operations required by the drive subsystem.
 */
public interface DriveIO {
  /**
   * Stores drivetrain observations for one periodic cycle.
   */
  class DriveIOInputs {
    public double leftAppliedOutput;
    public double rightAppliedOutput;
  }

  /**
   * Updates the drivetrain observation snapshot.
   *
   * @param inputs snapshot to update
   */
  void updateInputs(DriveIOInputs inputs);

  /**
   * Sets the output of the left and right drivetrain sides.
   *
   * @param leftOutput left-side output from -1.0 to 1.0
   * @param rightOutput right-side output from -1.0 to 1.0
   */
  void setTankOutputs(
      double leftOutput,
      double rightOutput);

  /**
   * Stops the complete drivetrain.
   */
  void stop();
}
