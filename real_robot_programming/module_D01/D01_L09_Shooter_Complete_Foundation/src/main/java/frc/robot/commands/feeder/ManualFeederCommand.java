// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.controls.FeederInputProcessor;
import frc.robot.subsystems.FeederSubsystem;
import java.util.function.BooleanSupplier;

/**
 * Runs the feeder from the driver's hold requests.
 */
public class ManualFeederCommand extends Command {
  private final FeederSubsystem feederSubsystem;
  private final FeederInputProcessor feederInputProcessor;
  private final BooleanSupplier feedRequest;
  private final BooleanSupplier reverseRequest;

  /**
   * Creates the manual feeder command.
   *
   * @param feederSubsystem feeder behavior dependency
   * @param feederInputProcessor driver request processor
   * @param feedRequest supplies the right-bumper state
   * @param reverseRequest supplies the left-bumper state
   */
  public ManualFeederCommand(
      FeederSubsystem feederSubsystem,
      FeederInputProcessor feederInputProcessor,
      BooleanSupplier feedRequest,
      BooleanSupplier reverseRequest) {
    this.feederSubsystem = feederSubsystem;
    this.feederInputProcessor = feederInputProcessor;
    this.feedRequest = feedRequest;
    this.reverseRequest = reverseRequest;
    addRequirements(feederSubsystem);
  }

  @Override
  public void execute() {
    double output =
        feederInputProcessor.process(
            feedRequest.getAsBoolean(),
            reverseRequest.getAsBoolean());

    feederSubsystem.setOutput(output);
  }

  @Override
  public void end(boolean interrupted) {
    feederSubsystem.stop();
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
