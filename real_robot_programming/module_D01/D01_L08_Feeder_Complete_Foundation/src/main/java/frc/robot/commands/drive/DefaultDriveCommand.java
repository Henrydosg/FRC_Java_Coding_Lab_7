// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.controls.DriveInputProcessor;
import frc.robot.subsystems.DriveSubsystem;
import java.util.function.DoubleSupplier;

/**
 * Drives the drivetrain from the driver's left and right joystick inputs.
 */
public class DefaultDriveCommand extends Command {
  private final DriveSubsystem driveSubsystem;
  private final DriveInputProcessor driveInputProcessor;
  private final DoubleSupplier leftY;
  private final DoubleSupplier rightY;

  /**
   * Creates the default tank-drive command.
   *
   * @param driveSubsystem drivetrain behavior dependency
   * @param driveInputProcessor driver input processing dependency
   * @param leftY supplies the left joystick Y-axis value
   * @param rightY supplies the right joystick Y-axis value
   */
  public DefaultDriveCommand(
      DriveSubsystem driveSubsystem,
      DriveInputProcessor driveInputProcessor,
      DoubleSupplier leftY,
      DoubleSupplier rightY) {
    this.driveSubsystem = driveSubsystem;
    this.driveInputProcessor = driveInputProcessor;
    this.leftY = leftY;
    this.rightY = rightY;
    addRequirements(driveSubsystem);
  }

  @Override
  public void execute() {
    double leftInput = leftY.getAsDouble();
    double rightInput = rightY.getAsDouble();

    double leftOutput =
        driveInputProcessor.process(leftInput);
    double rightOutput =
        driveInputProcessor.process(rightInput);

    driveSubsystem.tankDrive(leftOutput, rightOutput);
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stop();
  }
}
