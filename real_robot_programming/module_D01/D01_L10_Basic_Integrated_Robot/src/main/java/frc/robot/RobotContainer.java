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
import frc.robot.Constants.FeederConstants;
import frc.robot.Constants.FlywheelConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.TelemetryConstants;
import frc.robot.commands.drive.DefaultDriveCommand;
import frc.robot.commands.feeder.ManualFeederCommand;
import frc.robot.commands.flywheel.ManualFlywheelCommand;
import frc.robot.commands.intake.ManualIntakeCommand;
import frc.robot.commands.shooter.ManualShootCommand;
import frc.robot.controls.DriveInputProcessor;
import frc.robot.controls.FeederInputProcessor;
import frc.robot.controls.FlywheelInputProcessor;
import frc.robot.controls.IntakeInputProcessor;
import frc.robot.io.drive.DriveIO;
import frc.robot.io.drive.DriveIOSim;
import frc.robot.io.drive.DriveIOSparkMax;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIOSim;
import frc.robot.io.feeder.FeederIOSparkMax;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIOSim;
import frc.robot.io.flywheel.FlywheelIOTalonFX;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIOSim;
import frc.robot.io.intake.IntakeIOTalonFX;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.drive.DriveTelemetryFacade;
import frc.robot.telemetry.feeder.FeederTelemetryFacade;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;
import frc.robot.telemetry.intake.IntakeTelemetryFacade;

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

  private final FeederInputProcessor feederInputProcessor =
      new FeederInputProcessor();

  private final FlywheelInputProcessor flywheelInputProcessor =
      new FlywheelInputProcessor();

  private final DriveIO driveIO;

  private final DriveSubsystem driveSubsystem;

  private final IntakeIO intakeIO;

  private final IntakeSubsystem intakeSubsystem;

  private final FlywheelIO flywheelIO;

  private final FlywheelSubsystem flywheelSubsystem;

  private final FeederIO feederIO;

  private final FeederSubsystem feederSubsystem;

  private final DriveTelemetryFacade driveTelemetryFacade;

  private final IntakeTelemetryFacade intakeTelemetryFacade;

  private final FlywheelTelemetryFacade flywheelTelemetryFacade;

  private final FeederTelemetryFacade feederTelemetryFacade;

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
    feederIO = createFeederIO();
    feederSubsystem = new FeederSubsystem(feederIO);
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
    feederTelemetryFacade =
        new FeederTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable(
                    TelemetryConstants.kFeederTableName));
    robotTelemetry =
        new RobotTelemetry(
            driveSubsystem,
            driveTelemetryFacade,
            intakeSubsystem,
            intakeTelemetryFacade,
            flywheelSubsystem,
            flywheelTelemetryFacade,
            feederSubsystem,
            feederTelemetryFacade);
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
   * Creates the feeder IO implementation for the runtime environment.
   *
   * @return real or simulated feeder IO
   */
  private FeederIO createFeederIO() {
    if (RobotBase.isReal()) {
      return new FeederIOSparkMax();
    }

    return new FeederIOSim();
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
        .x()
        .whileTrue(
            new ManualFlywheelCommand(
                flywheelSubsystem,
                flywheelInputProcessor,
                driverController.getHID()::getXButton));

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
            new ManualShootCommand(
                flywheelSubsystem,
                feederSubsystem,
                FlywheelConstants.kManualShootOutput,
                FeederConstants.kManualFeedOutput,
                ShooterConstants.kFlywheelSpinUpDelaySeconds));

    driverController
        .rightBumper()
        .or(
            driverController.leftBumper())
        .whileTrue(
            new ManualFeederCommand(
                feederSubsystem,
                feederInputProcessor,
                driverController.getHID()::getRightBumperButton,
                driverController.getHID()::getLeftBumperButton));
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
