// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.drive;

/**
 * EN: Defines the hardware operations required by the drive subsystem.
 * VI: Định nghĩa các thao tác phần cứng mà DriveSubsystem cần sử dụng.
 */
public interface DriveIO {
  /**
   * EN: Sets the output of the left and right drivetrain sides.
   * VI: Đặt công suất cho bên trái và bên phải của drivetrain.
   *
   * @param leftOutput left-side output from -1.0 to 1.0
   * @param rightOutput right-side output from -1.0 to 1.0
   */
  default void setTankOutputs(
      double leftOutput,
      double rightOutput) {}

  /**
   * EN: Stops the complete drivetrain.
   * VI: Dừng toàn bộ drivetrain.
   */
  default void stop() {
    setTankOutputs(0.0, 0.0);
  }
}