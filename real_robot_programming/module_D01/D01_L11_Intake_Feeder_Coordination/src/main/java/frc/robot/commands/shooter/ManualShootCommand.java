// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;

/**
 * Coordinates the flywheel and feeder for manual shooting.
 */
public class ManualShootCommand extends Command {
  private final FlywheelSubsystem flywheelSubsystem;
  private final FeederSubsystem feederSubsystem;
  private final double flywheelOutput;
  private final double feederOutput;
  private final double spinUpDelaySeconds;
  private final Timer spinUpTimer = new Timer();

  /**
   * Creates the manual shoot command.
   *
   * @param flywheelSubsystem flywheel behavior dependency
   * @param feederSubsystem feeder behavior dependency
   * @param flywheelOutput normalized flywheel output while shooting
   * @param feederOutput normalized feeder output while shooting
   * @param spinUpDelaySeconds delay before starting the feeder
   */
  public ManualShootCommand(
      FlywheelSubsystem flywheelSubsystem,
      FeederSubsystem feederSubsystem,
      double flywheelOutput,
      double feederOutput,
      double spinUpDelaySeconds) {
    this.flywheelSubsystem = flywheelSubsystem;
    this.feederSubsystem = feederSubsystem;
    this.flywheelOutput = flywheelOutput;
    this.feederOutput = feederOutput;
    this.spinUpDelaySeconds = spinUpDelaySeconds;
    addRequirements(
        flywheelSubsystem,
        feederSubsystem);
  }

  @Override
  public void initialize() {
    feederSubsystem.stop();
    flywheelSubsystem.setOutput(flywheelOutput);
    spinUpTimer.restart();
  }

  @Override
  public void execute() {
    flywheelSubsystem.setOutput(flywheelOutput);

    if (spinUpTimer.hasElapsed(spinUpDelaySeconds)) {
      feederSubsystem.setOutput(feederOutput);
    }
  }

  @Override
  public void end(boolean interrupted) {
    spinUpTimer.stop();
    spinUpTimer.reset();
    feederSubsystem.stop();
    flywheelSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
