// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AutonomousSafetyHoldCommand;
import frc.robot.commands.BoundedRobotRelativeAutonomousDriveCommand;
import frc.robot.commands.CaptureFieldHeadingReferenceCommand;
import frc.robot.commands.DriveThreeMeterValidationDashboard;
import frc.robot.commands.FieldRelativeTeleopDriveCommand;
import frc.robot.commands.KnownFieldPoseResetDashboard;
import frc.robot.commands.ResetKnownFieldPoseCommand;
import frc.robot.commands.SwerveFourModuleTestDashboard;
import frc.robot.commands.SwerveFrontLeftCommissioningDashboard;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.gyro.GyroIOSim;
import frc.robot.io.gyro.GyroIOPigeon2;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.io.swerve.SwerveModuleIOCTRE;
import frc.robot.io.swerve.SwerveModuleIOSim;
import frc.robot.subsystems.SwerveKinematics;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.driver.DriverInputTelemetryFacade;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;
import frc.robot.telemetry.validation.DriveThreeMeterValidationTelemetryFacade;

/**
 * Creates robot components and configures command bindings.
 */
public class RobotContainer {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveFrontLeftCommissioningDashboard commissioningDashboard;
  private final SwerveFourModuleTestDashboard fourModuleTestDashboard;
  private final DriveThreeMeterValidationDashboard driveThreeMeterValidationDashboard;
  private final ResetKnownFieldPoseCommand resetKnownFieldPoseCommand;
  private final KnownFieldPoseResetDashboard knownFieldPoseResetDashboard;
  private final Command autonomousCommand;
  private final RobotTelemetry robotTelemetry;

  /**
   * Creates the composition root.
   */
  public RobotContainer() {
    SwerveModuleIO frontLeft;
    SwerveModuleIO frontRight;
    SwerveModuleIO backLeft;
    SwerveModuleIO backRight;
    GyroIO gyro;

    if (RobotBase.isReal()) {
      frontLeft = SwerveModuleIOCTRE.createFrontLeft();
      frontRight = SwerveModuleIOCTRE.createFrontRight();
      backLeft = SwerveModuleIOCTRE.createBackLeft();
      backRight = SwerveModuleIOCTRE.createBackRight();
      gyro = new GyroIOPigeon2();
    } else {
      SwerveSimulationState simulationState = new SwerveSimulationState();
      frontLeft =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kFrontLeftDrivePositionSign,
              simulationState,
              ModuleIdentity.FRONT_LEFT);
      frontRight =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kFrontRightDrivePositionSign,
              simulationState,
              ModuleIdentity.FRONT_RIGHT);
      backLeft =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kBackLeftDrivePositionSign,
              simulationState,
              ModuleIdentity.BACK_LEFT);
      backRight =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kBackRightDrivePositionSign,
              simulationState,
              ModuleIdentity.BACK_RIGHT);
      SwerveKinematics simulationKinematics = new SwerveKinematics();
      gyro = new GyroIOSim(simulationState, simulationKinematics::toChassisSpeeds);
    }

    swerveSubsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gyro);
    resetKnownFieldPoseCommand =
        new ResetKnownFieldPoseCommand(
            swerveSubsystem, Constants.FieldConstants.kLearningStartingPose);
    Command boundedRobotRelativeMotion =
        new BoundedRobotRelativeAutonomousDriveCommand(
            swerveSubsystem,
            new ChassisSpeeds(
                Constants.AutonomousConstants.kBoundedRobotRelativeForwardSpeedMetersPerSecond,
                Constants.AutonomousConstants.kBoundedRobotRelativeLateralSpeedMetersPerSecond,
                Constants.AutonomousConstants.kBoundedRobotRelativeAngularSpeedRadiansPerSecond),
            Constants.AutonomousConstants.kBoundedRobotRelativeMotionDurationSeconds,
            Timer::getFPGATimestamp);
    Command repeatingSafetyHold =
        new AutonomousSafetyHoldCommand(
                swerveSubsystem,
                Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds,
                Timer::getFPGATimestamp)
            .repeatedly();
    Command inheritedAutonomousSession =
        boundedRobotRelativeMotion.andThen(repeatingSafetyHold);
    Command inheritedA00L04Session =
        Commands.either(
                inheritedAutonomousSession,
                Commands.runOnce(swerveSubsystem::stop, swerveSubsystem),
                DriverStation::isAutonomousEnabled)
            .onlyWhile(DriverStation::isAutonomousEnabled);
    commissioningDashboard = new SwerveFrontLeftCommissioningDashboard(swerveSubsystem);
    fourModuleTestDashboard = new SwerveFourModuleTestDashboard(swerveSubsystem);
    driveThreeMeterValidationDashboard =
        new DriveThreeMeterValidationDashboard(
            swerveSubsystem,
            new DriveThreeMeterValidationTelemetryFacade(
                NetworkTableInstance
                    .getDefault()
                     .getTable("DriveThreeMeterValidation")));
    knownFieldPoseResetDashboard = new KnownFieldPoseResetDashboard(resetKnownFieldPoseCommand);

    autonomousCommand =
        Commands.either(
            inheritedA00L04Session,
            Commands.runOnce(swerveSubsystem::stop, swerveSubsystem),
            resetKnownFieldPoseCommand::consumeAcceptedStartPose);

    SwerveTelemetryFacade swerveTelemetryFacade =
        new SwerveTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable("Swerve"));
    XboxController driverController =
        new XboxController(Constants.DriverInputConstants.kXboxControllerPort);
    XboxDriverInputSource driverInputSource = new XboxDriverInputSource(driverController);
    DriverInputTelemetryFacade driverInputTelemetryFacade =
        new DriverInputTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable("DriverInput"));
    FieldRelativeTeleopDriveCommand fieldRelativeTeleopDriveCommand =
        new FieldRelativeTeleopDriveCommand(
            swerveSubsystem,
            driverInputSource,
            driverInputTelemetryFacade);
    swerveSubsystem.setDefaultCommand(fieldRelativeTeleopDriveCommand);

    // Xbox Back/View is unused by the inherited L22 bindings and captures field zero only Disabled.
    new JoystickButton(driverController, XboxController.Button.kBack.value)
        .onTrue(new CaptureFieldHeadingReferenceCommand(swerveSubsystem));
    robotTelemetry = new RobotTelemetry(swerveSubsystem, swerveTelemetryFacade);
  }

  /**
   * Returns bounded robot-relative motion followed by the repeating zero-motion safety hold.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autonomousCommand;
  }

  /**
   * Returns the runtime telemetry coordinator.
   *
   * @return runtime telemetry coordinator
   */
  public RobotTelemetry getRobotTelemetry() {
    return robotTelemetry;
  }
}
