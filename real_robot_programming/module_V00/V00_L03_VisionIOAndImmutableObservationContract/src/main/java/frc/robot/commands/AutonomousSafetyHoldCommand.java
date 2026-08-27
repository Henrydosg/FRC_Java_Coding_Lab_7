// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;

/**
 * Retains stopped drivetrain ownership for one active autonomous session.
 *
 * <p>This command submits no motion request. It prevents the default Teleop command from
 * reacquiring Swerve until the Driver Station leaves Autonomous Enabled.
 */
public final class AutonomousSafetyHoldCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;

  /**
   * Creates a zero-motion autonomous-session hold.
   *
   * @param swerveSubsystem drivetrain subsystem that owns the stop authority
   */
  public AutonomousSafetyHoldCommand(SwerveSubsystem swerveSubsystem) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    swerveSubsystem.stop();
  }

  @Override
  public void execute() {
    // Intentionally no drivetrain request, IO access, telemetry, or business logic.
  }

  @Override
  public boolean isFinished() {
    return !DriverStation.isAutonomousEnabled();
  }

  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.stop();
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }

  @Override
  public InterruptionBehavior getInterruptionBehavior() {
    return InterruptionBehavior.kCancelIncoming;
  }
}
