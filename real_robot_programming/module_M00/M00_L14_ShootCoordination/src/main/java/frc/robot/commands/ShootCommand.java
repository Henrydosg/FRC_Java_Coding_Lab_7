// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.observation.feeder.FeederObservation;
import frc.robot.observation.feeder.FeederObservation.RequestedState;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import java.util.Objects;

/** Coordinates Feeder requests with the Flywheel subsystem's ready-at-speed observation. */
public final class ShootCommand extends Command {
  private final FlywheelSubsystem flywheel;
  private final FeederSubsystem feeder;
  private final double targetVelocityRpm;

  /**
   * Creates a scheduler-managed Flywheel and Feeder coordination command.
   *
   * @param flywheel existing Flywheel behavior owner
   * @param feeder existing Feeder behavior owner
   * @param targetVelocityRpm caller-supplied semantic velocity request in RPM
   */
  public ShootCommand(
      FlywheelSubsystem flywheel, FeederSubsystem feeder, double targetVelocityRpm) {
    this.flywheel = Objects.requireNonNull(flywheel, "flywheel");
    this.feeder = Objects.requireNonNull(feeder, "feeder");
    if (!Double.isFinite(targetVelocityRpm) || targetVelocityRpm <= 0.0) {
      throw new IllegalArgumentException("targetVelocityRpm must be finite and greater than zero");
    }
    this.targetVelocityRpm = targetVelocityRpm;
    addRequirements(flywheel, feeder);
  }

  @Override
  public void initialize() {
    try {
      feeder.stop();
    } catch (RuntimeException failure) {
      stopFlywheelAfterFailure(failure);
      throw failure;
    }

    try {
      flywheel.requestVelocity(targetVelocityRpm);
    } catch (RuntimeException failure) {
      stopFlywheelAfterFailure(failure);
      throw failure;
    }
  }

  @Override
  public void execute() {
    FlywheelObservation flywheelObservation = flywheel.getObservation();
    FeederObservation feederObservation = feeder.getObservation();
    boolean shouldFeed =
        flywheelObservation.readyAtSpeed()
            && feederObservation.available()
            && feederObservation.connected();
    RequestedState feederRequestedState = feederObservation.requestedState();

    if (shouldFeed && feederRequestedState != RequestedState.FEED_REQUESTED) {
      try {
        feeder.requestFeed();
      } catch (RuntimeException failure) {
        stopOutputsAfterFailure(failure);
        throw failure;
      }
    } else if (!shouldFeed && feederRequestedState == RequestedState.FEED_REQUESTED) {
      try {
        feeder.stop();
      } catch (RuntimeException failure) {
        stopFlywheelAfterFailure(failure);
        throw failure;
      }
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  @Override
  public void end(boolean interrupted) {
    RuntimeException feederFailure = null;
    RuntimeException flywheelFailure = null;

    try {
      feeder.stop();
    } catch (RuntimeException failure) {
      feederFailure = failure;
    }

    try {
      flywheel.stop();
    } catch (RuntimeException failure) {
      flywheelFailure = failure;
    }

    if (feederFailure != null) {
      addSuppressedIfDistinct(feederFailure, flywheelFailure);
      throw feederFailure;
    }
    if (flywheelFailure != null) {
      throw flywheelFailure;
    }
  }

  private void stopFlywheelAfterFailure(RuntimeException primaryFailure) {
    try {
      flywheel.stop();
    } catch (RuntimeException cleanupFailure) {
      addSuppressedIfDistinct(primaryFailure, cleanupFailure);
    }
  }

  private void stopOutputsAfterFailure(RuntimeException primaryFailure) {
    try {
      feeder.stop();
    } catch (RuntimeException cleanupFailure) {
      addSuppressedIfDistinct(primaryFailure, cleanupFailure);
    }

    try {
      flywheel.stop();
    } catch (RuntimeException cleanupFailure) {
      addSuppressedIfDistinct(primaryFailure, cleanupFailure);
    }
  }

  private static void addSuppressedIfDistinct(
      RuntimeException primaryFailure, RuntimeException cleanupFailure) {
    if (cleanupFailure != null && cleanupFailure != primaryFailure) {
      primaryFailure.addSuppressed(cleanupFailure);
    }
  }
}
