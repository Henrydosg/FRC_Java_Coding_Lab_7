// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.telemetry.RobotTelemetry;

/**
 * Manages the robot lifecycle and CommandScheduler.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private final RobotTelemetry m_robotTelemetry;

  /**
   * Creates the robot container when robot code starts.
   */
  public Robot() {
    m_robotContainer = new RobotContainer();
    m_robotTelemetry = m_robotContainer.getRobotTelemetry();
  }

  /**
   * Runs the command scheduler every robot loop.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    m_robotTelemetry.periodic();
  }

  /**
   * Runs once when the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {}

  /**
   * Runs periodically while the robot is Disabled.
   */
  @Override
  public void disabledPeriodic() {}

  /**
   * Schedules the selected autonomous command.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  /**
   * Runs periodically during Autonomous mode.
   */
  @Override
  public void autonomousPeriodic() {}

  /**
   * Cancels autonomous when Teleop starts.
   */
  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * Runs periodically during Teleop mode.
   */
  @Override
  public void teleopPeriodic() {}

  /**
   * Cancels all commands when Test mode starts.
   */
  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * Runs periodically during Test mode.
   */
  @Override
  public void testPeriodic() {}

  /**
   * Runs once when simulation starts.
   */
  @Override
  public void simulationInit() {}

  /**
   * Runs periodically during simulation.
   */
  @Override
  public void simulationPeriodic() {}
}
