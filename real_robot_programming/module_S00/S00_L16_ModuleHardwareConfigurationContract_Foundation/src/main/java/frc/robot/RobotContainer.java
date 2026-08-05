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
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.gyro.GyroIOPigeon2;
import frc.robot.io.gyro.GyroIONoop;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.io.swerve.SwerveModuleIOCTRE;
import frc.robot.io.swerve.SwerveModuleIONoop;
import frc.robot.commands.SwerveFrontLeftCommissioningDashboard;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;

/**
 * Creates robot components and configures command bindings.
 */
public class RobotContainer {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveFrontLeftCommissioningDashboard commissioningDashboard;
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
      frontLeft = new SwerveModuleIONoop();
      frontRight = new SwerveModuleIONoop();
      backLeft = new SwerveModuleIONoop();
      backRight = new SwerveModuleIONoop();
      gyro = new GyroIONoop();
    }

    swerveSubsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gyro);
    commissioningDashboard = new SwerveFrontLeftCommissioningDashboard(swerveSubsystem);

    SwerveTelemetryFacade swerveTelemetryFacade =
        new SwerveTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable("Swerve"));
    robotTelemetry =
        new RobotTelemetry(
            swerveSubsystem,
            swerveTelemetryFacade);
  }

  /**
   * Returns an empty autonomous command until autonomous behavior is introduced.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return Commands.none();
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
