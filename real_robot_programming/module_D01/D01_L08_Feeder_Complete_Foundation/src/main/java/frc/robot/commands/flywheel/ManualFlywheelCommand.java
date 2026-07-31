// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.flywheel;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.controls.FlywheelInputProcessor;
import frc.robot.subsystems.FlywheelSubsystem;
import java.util.function.BooleanSupplier;

/**
 * Runs the flywheel from the driver's hold request.
 */
public class ManualFlywheelCommand extends Command {
  private final FlywheelSubsystem flywheelSubsystem;
  private final FlywheelInputProcessor flywheelInputProcessor;
  private final BooleanSupplier flywheelRequest;

  /**
   * Creates the manual flywheel command.
   *
   * @param flywheelSubsystem flywheel behavior dependency
   * @param flywheelInputProcessor driver request processor
   * @param flywheelRequest supplies the flywheel button state
   */
  public ManualFlywheelCommand(
      FlywheelSubsystem flywheelSubsystem,
      FlywheelInputProcessor flywheelInputProcessor,
      BooleanSupplier flywheelRequest) {
    this.flywheelSubsystem = flywheelSubsystem;
    this.flywheelInputProcessor = flywheelInputProcessor;
    this.flywheelRequest = flywheelRequest;
    addRequirements(flywheelSubsystem);
  }

  @Override
  public void execute() {
    double output =
        flywheelInputProcessor.process(
            flywheelRequest.getAsBoolean());

    flywheelSubsystem.setOutput(output);
  }

  @Override
  public void end(boolean interrupted) {
    flywheelSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
