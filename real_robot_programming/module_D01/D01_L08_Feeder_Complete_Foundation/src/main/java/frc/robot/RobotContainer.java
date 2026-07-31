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
import frc.robot.commands.intake.ManualIntakeCommand;
import frc.robot.commands.flywheel.ManualFlywheelCommand;
import frc.robot.controls.DriveInputProcessor;
import frc.robot.controls.IntakeInputProcessor;
import frc.robot.controls.FlywheelInputProcessor;
import frc.robot.io.drive.DriveIO;
import frc.robot.io.drive.DriveIOSim;
import frc.robot.io.drive.DriveIOSparkMax;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIOSim;
import frc.robot.io.intake.IntakeIOTalonFX;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIOSim;
import frc.robot.io.flywheel.FlywheelIOTalonFX;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.drive.DriveTelemetryFacade;
import frc.robot.telemetry.intake.IntakeTelemetryFacade;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;

/**
 * Creates robot components and configures controller bindings.
 */
public class RobotContainer {
  private final CommandXboxController driverController =
      new CommandXboxController(
          OperatorConstants.kDriverControllerPort);

  private final DriveInputProcessor driveInputProcessor =
      new DriveInputProcessor();

  private final IntakeInputProcessor intakeInputProcessor =
      new IntakeInputProcessor();

  private final FlywheelInputProcessor flywheelInputProcessor =
      new FlywheelInputProcessor();

  private final DriveIO driveIO;

  private final DriveSubsystem driveSubsystem;

  private final IntakeIO intakeIO;

  private final IntakeSubsystem intakeSubsystem;

  private final FlywheelIO flywheelIO;

  private final FlywheelSubsystem flywheelSubsystem;

  private final DriveTelemetryFacade driveTelemetryFacade;

  private final IntakeTelemetryFacade intakeTelemetryFacade;

  private final FlywheelTelemetryFacade flywheelTelemetryFacade;

  private final RobotTelemetry robotTelemetry;

  private final DefaultDriveCommand defaultDriveCommand;

  /**
   * Creates the robot container and configures commands.
   */
  public RobotContainer() {
    driveIO = createDriveIO();
    driveSubsystem = new DriveSubsystem(driveIO);
    intakeIO = createIntakeIO();
    intakeSubsystem = new IntakeSubsystem(intakeIO);
    flywheelIO = createFlywheelIO();
    flywheelSubsystem = new FlywheelSubsystem(flywheelIO);
    driveTelemetryFacade =
        new DriveTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable(
                    TelemetryConstants.kDriveTableName));
    intakeTelemetryFacade =
        new IntakeTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable(
                    TelemetryConstants.kIntakeTableName));
    flywheelTelemetryFacade =
        new FlywheelTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable(
                    TelemetryConstants.kFlywheelTableName));
    robotTelemetry =
        new RobotTelemetry(
            driveSubsystem,
            driveTelemetryFacade,
            intakeSubsystem,
            intakeTelemetryFacade,
            flywheelSubsystem,
            flywheelTelemetryFacade);
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
   * Creates the intake IO implementation for the runtime environment.
   *
   * @return real or simulated intake IO
   */
  private IntakeIO createIntakeIO() {
    if (RobotBase.isReal()) {
      return new IntakeIOTalonFX();
    }

    return new IntakeIOSim();
  }

  /**
   * Creates the flywheel IO implementation for the runtime environment.
   *
   * @return real or simulated flywheel IO
   */
  private FlywheelIO createFlywheelIO() {
    if (RobotBase.isReal()) {
      return new FlywheelIOTalonFX();
    }

    return new FlywheelIOSim();
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

    driverController
        .rightTrigger(
            OperatorConstants.kIntakeTriggerThreshold)
        .or(
            driverController.leftTrigger(
                OperatorConstants.kIntakeTriggerThreshold))
        .whileTrue(
            new ManualIntakeCommand(
                intakeSubsystem,
                intakeInputProcessor,
                driverController::getRightTriggerAxis,
                driverController::getLeftTriggerAxis));

    driverController
        .y()
        .whileTrue(
            new ManualFlywheelCommand(
                flywheelSubsystem,
                flywheelInputProcessor,
                driverController.getHID()::getYButton));
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
