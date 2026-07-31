// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

/**
 * Drives both drivetrain sides at one signed output.
 */
public class DriveTestCommand extends Command {
  private final DriveSubsystem driveSubsystem;
  private final double output;

  /**
   * Creates a drivetrain test command.
   *
   * @param driveSubsystem drivetrain behavior dependency
   * @param output signed output for both drivetrain sides
   */
  public DriveTestCommand(
      DriveSubsystem driveSubsystem,
      double output) {
    this.driveSubsystem = driveSubsystem;
    this.output = output;
    addRequirements(driveSubsystem);
  }

  @Override
  public void execute() {
    driveSubsystem.tankDrive(output, output);
  }

  @Override
  public void end(boolean interrupted) {
    driveSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
