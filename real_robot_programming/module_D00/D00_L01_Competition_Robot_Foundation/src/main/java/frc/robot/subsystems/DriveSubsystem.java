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
import frc.robot.io.drive.DriveIO;

/**
 * EN: Provides high-level drivetrain behavior.
 * VI: Cung cấp các chức năng điều khiển drivetrain ở mức cao.
 */
public class DriveSubsystem extends SubsystemBase {
  // EN: Hardware implementation used by this subsystem.
  // VI: Lớp phần cứng được subsystem sử dụng.
  private final DriveIO io;

  /**
   * EN: Creates the drive subsystem.
   * VI: Khởi tạo DriveSubsystem.
   *
   * @param io real or simulated drivetrain hardware
   */
  public DriveSubsystem(DriveIO io) {
    this.io = io;
    stop();
  }

  /**
   * EN: Drives the left and right sides independently.
   * VI: Điều khiển độc lập bên trái và bên phải.
   *
   * @param leftOutput left-side output
   * @param rightOutput right-side output
   */
  public void tankDrive(
      double leftOutput,
      double rightOutput) {
    // EN: Prevent values outside the legal motor range.
    // VI: Giới hạn công suất trong phạm vi hợp lệ của motor.
    double safeLeftOutput =
        MathUtil.clamp(leftOutput, -1.0, 1.0);

    double safeRightOutput =
        MathUtil.clamp(rightOutput, -1.0, 1.0);

    io.setTankOutputs(
        safeLeftOutput,
        safeRightOutput);
  }

  /**
   * EN: Stops the complete drivetrain.
   * VI: Dừng toàn bộ drivetrain.
   */
  public void stop() {
    io.stop();
  }
}