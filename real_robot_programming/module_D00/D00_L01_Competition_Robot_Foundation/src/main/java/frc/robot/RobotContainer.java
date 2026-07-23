// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.io.drive.DriveIOSparkMax;
import frc.robot.subsystems.DriveSubsystem;

/**
 * EN: Owns robot subsystems, controls, and button bindings.
 * VI: Quản lý subsystem, tay điều khiển và các nút bấm của robot.
 */
public class RobotContainer {
  // EN: Xbox controller used by the driver.
  // VI: Tay cầm Xbox được người lái sử dụng.
  private final CommandXboxController driverController =
      new CommandXboxController(
          OperatorConstants.kDriverControllerPort);

  // EN: Real drivetrain subsystem using SPARK MAX controllers.
  // VI: Drivetrain thật sử dụng các bộ điều khiển SPARK MAX.
  private final DriveSubsystem driveSubsystem =
      new DriveSubsystem(new DriveIOSparkMax());

  /**
   * EN: Creates the robot container and button bindings.
   * VI: Khởi tạo RobotContainer và thiết lập các nút bấm.
   */
  public RobotContainer() {
    configureBindings();
    printTestInstructions();
  }

  /**
   * EN: Configures the first forward/reverse drivetrain test.
   * VI: Thiết lập bài kiểm tra tiến/lùi đầu tiên.
   */
  private void configureBindings() {
    // EN: Hold A to move forward.
    // VI: Giữ nút A để robot chạy tiến.
    driverController
        .a()
        .whileTrue(
            Commands.startEnd(
                this::driveForward,
                driveSubsystem::stop,
                driveSubsystem));

    // EN: Hold B to move backward.
    // VI: Giữ nút B để robot chạy lùi.
    driverController
        .b()
        .whileTrue(
            Commands.startEnd(
                this::driveReverse,
                driveSubsystem::stop,
                driveSubsystem));
  }

  /**
   * EN: Runs both drivetrain sides forward at 50%.
   * VI: Cho cả hai bên drivetrain chạy tiến ở mức 50%.
   */
  private void driveForward() {
    driveSubsystem.tankDrive(
        DriveConstants.kDriveTestOutput,
        DriveConstants.kDriveTestOutput);
  }

  /**
   * EN: Runs both drivetrain sides backward at 50%.
   * VI: Cho cả hai bên drivetrain chạy lùi ở mức 50%.
   */
  private void driveReverse() {
    driveSubsystem.tankDrive(
        -DriveConstants.kDriveTestOutput,
        -DriveConstants.kDriveTestOutput);
  }

  /**
   * EN: Prints the active test controls to RioLog.
   * VI: In hướng dẫn điều khiển hiện tại ra RioLog.
   */
  private void printTestInstructions() {
    System.out.println("=================================");
    System.out.println("Leader/Follower Drive Test");
    System.out.println("A: Forward at 50%");
    System.out.println("B: Reverse at 50%");
    System.out.println("Release Button: Stop");
    System.out.println("Left: CAN 11 Leader -> CAN 8 Follower");
    System.out.println("Right: CAN 10 Leader -> CAN 7 Follower");
    System.out.println("=================================");
  }

  /**
   * EN: Returns an empty autonomous command.
   * VI: Trả về autonomous rỗng để giữ robot an toàn.
   */
  public Command getAutonomousCommand() {
    return Commands.none();
  }
}