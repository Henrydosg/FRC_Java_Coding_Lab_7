// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.TelemetryConstants;
import frc.robot.commands.drive.DefaultDriveCommand;
import frc.robot.commands.drive.DriveTestCommand;
import frc.robot.controls.DriveInputProcessor;
import frc.robot.io.drive.DriveIO;
import frc.robot.io.drive.DriveIOSim;
import frc.robot.io.drive.DriveIOSparkMax;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.drive.DriveTelemetryFacade;

/**
 * Creates robot components and configures controller bindings.
 */
public class RobotContainer {
  private final CommandXboxController driverController =
      new CommandXboxController(
          OperatorConstants.kDriverControllerPort);

  private final DriveInputProcessor driveInputProcessor =
      new DriveInputProcessor();

  private final DriveIO driveIO;

  private final DriveSubsystem driveSubsystem;

  private final DriveTelemetryFacade driveTelemetryFacade;

  private final RobotTelemetry robotTelemetry;

  private final DefaultDriveCommand defaultDriveCommand;

  /**
   * Creates the robot container and configures commands.
   */
  public RobotContainer() {
    driveIO = createDriveIO();
    driveSubsystem = new DriveSubsystem(driveIO);
    driveTelemetryFacade =
        new DriveTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable(
                    TelemetryConstants.kDriveTableName));
    robotTelemetry =
        new RobotTelemetry(
            driveSubsystem,
            driveTelemetryFacade);
    defaultDriveCommand =
        new DefaultDriveCommand(
            driveSubsystem,
            driveInputProcessor,
            driverController::getLeftY,
            driverController::getRightY);

    configureDefaultCommands();
    configureBindings();
  }

  /**
   * Creates the drivetrain IO implementation for the runtime environment.
   *
   * @return real or simulated drivetrain IO
   */
  private DriveIO createDriveIO() {
    if (RobotBase.isReal()) {
      return new DriveIOSparkMax();
    }

    return new DriveIOSim();
  }

  /**
   * Configures the drivetrain default command.
   */
  private void configureDefaultCommands() {
    driveSubsystem.setDefaultCommand(defaultDriveCommand);
  }

  /**
   * Configures the forward and reverse drivetrain test.
   */
  private void configureBindings() {
    driverController
        .a()
        .whileTrue(
            new DriveTestCommand(
                driveSubsystem,
                DriveConstants.kDriveTestOutput));

    driverController
        .b()
        .whileTrue(
            new DriveTestCommand(
                driveSubsystem,
                -DriveConstants.kDriveTestOutput));
  }

  /**
   * Returns an empty autonomous command.
   */
  public Command getAutonomousCommand() {
    return Commands.none();
  }

  /**
   * Returns the robot telemetry coordinator.
   *
   * @return robot telemetry coordinator
   */
  public RobotTelemetry getRobotTelemetry() {
    return robotTelemetry;
  }
}
