// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;

/**
 * EN: Stores robot-wide constants.
 * VI: Lưu trữ các hằng số dùng chung cho toàn bộ robot.
 */
public final class Constants {
  private Constants() {}

  /**
   * EN: Stores driver controller constants.
   * VI: Lưu trữ thông số của tay điều khiển.
   */
  public static final class OperatorConstants {
    // EN: Driver Xbox controller is connected to USB port 0.
    // VI: Tay cầm Xbox của người lái kết nối tại cổng USB 0.
    public static final int kDriverControllerPort = 0;

    private OperatorConstants() {}
  }

  /**
   * EN: Stores drivetrain hardware and test constants.
   * VI: Lưu địa chỉ phần cứng và thông số kiểm tra drivetrain.
   */
  public static final class DriveConstants {
    // EN: Left drivetrain leader SPARK MAX.
    // VI: SPARK MAX Leader của drivetrain bên trái.
    public static final int kLeftLeaderCanId = 11;

    // EN: Left drivetrain follower SPARK MAX.
    // VI: SPARK MAX Follower của drivetrain bên trái.
    public static final int kLeftFollowerCanId = 8;

    // EN: Right drivetrain leader SPARK MAX.
    // VI: SPARK MAX Leader của drivetrain bên phải.
    public static final int kRightLeaderCanId = 10;

    // EN: Right drivetrain follower SPARK MAX.
    // VI: SPARK MAX Follower của drivetrain bên phải.
    public static final int kRightFollowerCanId = 7;

    // EN: The drivetrain uses brushed CIM motors.
    // VI: Drivetrain sử dụng động cơ CIM loại brushed.
    public static final MotorType kDriveMotorType =
        MotorType.kBrushed;

    // EN: Left and right motors are mounted in opposite directions.
    // VI: Motor bên trái và bên phải được lắp đối xứng nhau.
    public static final boolean kLeftLeaderInverted = true;
    public static final boolean kRightLeaderInverted = false;

    // EN: Limits motor current to protect motors and electrical components.
    // VI: Giới hạn dòng điện để bảo vệ motor và hệ thống điện.
    public static final int kDriveCurrentLimitAmps = 60;

    // EN: Maintains consistent motor behavior as battery voltage changes.
    // VI: Giúp motor hoạt động ổn định khi điện áp pin thay đổi.
    public static final double kVoltageCompensationVolts = 12.0;

    // EN: CAN timeout used only during startup configuration.
    // VI: Thời gian chờ CAN khi cấu hình thiết bị lúc khởi động.
    public static final int kConfigurationCanTimeoutMs = 250;

    // EN: Motor output used for the first forward/reverse test.
    // VI: Công suất motor dùng cho bài kiểm tra tiến/lùi đầu tiên.
    public static final double kDriveTestOutput = 0.50;

    private DriveConstants() {}
  }
}