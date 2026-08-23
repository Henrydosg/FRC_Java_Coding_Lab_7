// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
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
import java.util.Optional;
import java.util.function.Supplier;

/** Performs one Disabled-only alliance-aware pose reset and records its accepted provenance. */
public final class AllianceAwareAutonomousStartPoseResetCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final Supplier<Optional<AutonomousStartContext>> startContextSupplier;
  private Optional<AutonomousStartContext> acceptedStartContext = Optional.empty();

  public AllianceAwareAutonomousStartPoseResetCommand(
      SwerveSubsystem swerveSubsystem,
      Supplier<Optional<AutonomousStartContext>> startContextSupplier) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.startContextSupplier = Objects.requireNonNull(startContextSupplier, "startContextSupplier");
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    acceptedStartContext = Optional.empty();
    if (DriverStation.isEnabled()) {
      return;
    }

    Optional<AutonomousStartContext> requestedContext =
        Objects.requireNonNull(startContextSupplier.get(), "startContextSupplier result");
    if (requestedContext.isEmpty()) {
      return;
    }

    AutonomousStartContext context = requestedContext.orElseThrow();
    if (swerveSubsystem.resetKnownFieldPose(context.executionStartPose())) {
      acceptedStartContext = Optional.of(context);
    }
  }

  /** Consumes the one accepted context, if a Disabled reset succeeded. */
  public Optional<AutonomousStartContext> consumeAcceptedStartContext() {
    Optional<AutonomousStartContext> consumedContext = acceptedStartContext;
    acceptedStartContext = Optional.empty();
    return consumedContext;
  }

  @Override
  public boolean isFinished() {
    return true;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
