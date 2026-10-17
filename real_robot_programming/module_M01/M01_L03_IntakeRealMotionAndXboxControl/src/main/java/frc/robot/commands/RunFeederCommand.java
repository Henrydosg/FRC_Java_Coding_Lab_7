// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FeederSubsystem;
import java.util.Objects;

/** Retains scheduler ownership of Feeder while one manual feed request is active. */
public final class RunFeederCommand extends Command {
  private final FeederSubsystem feederSubsystem;

  /**
   * Creates the manual Feeder ownership command.
   *
   * @param feederSubsystem existing Feeder behavior owner
   */
  public RunFeederCommand(FeederSubsystem feederSubsystem) {
    this.feederSubsystem = Objects.requireNonNull(feederSubsystem, "feederSubsystem");
    addRequirements(feederSubsystem);
  }

  @Override
  public void initialize() {
    feederSubsystem.requestFeed();
  }

  @Override
  public void execute() {
    // The subsystem already owns the requested state; no repeated request is required.
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    feederSubsystem.stop();
  }
}
