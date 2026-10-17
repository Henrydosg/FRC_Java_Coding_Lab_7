// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ElevatorSubsystem;
import java.util.Objects;
import java.util.function.DoubleSupplier;

/** Runs one bounded, scheduler-managed request for trusted Elevator reference acquisition. */
public final class HomeElevatorCommand extends Command {
  private final ElevatorSubsystem elevator;
  private final double timeoutSeconds;
  private final DoubleSupplier clockSeconds;

  private boolean terminalAtInitialize;
  private boolean timeoutTrackingStarted;
  private double startTimeSeconds;

  /** Creates a homing lifecycle bounded by the supplied software timeout. */
  public HomeElevatorCommand(ElevatorSubsystem elevator, double timeoutSeconds) {
    this(elevator, timeoutSeconds, Timer::getFPGATimestamp);
  }

  HomeElevatorCommand(
      ElevatorSubsystem elevator, double timeoutSeconds, DoubleSupplier clockSeconds) {
    this.elevator = Objects.requireNonNull(elevator, "elevator");
    if (!Double.isFinite(timeoutSeconds) || timeoutSeconds <= 0.0) {
      throw new IllegalArgumentException("timeoutSeconds must be finite and positive");
    }
    this.timeoutSeconds = timeoutSeconds;
    this.clockSeconds = Objects.requireNonNull(clockSeconds, "clockSeconds");
    addRequirements(elevator);
  }

  @Override
  public void initialize() {
    terminalAtInitialize = false;
    timeoutTrackingStarted = false;
    startTimeSeconds = 0.0;

    var observation = elevator.getObservation();
    if (observation.positionReferenced()) {
      terminalAtInitialize = true;
      return;
    }
    if (!observation.available() || !observation.connected()) {
      terminalAtInitialize = true;
      return;
    }

    elevator.requestHoming();
    startTimeSeconds = clockSeconds.getAsDouble();
    timeoutTrackingStarted = true;
  }

  @Override
  public void execute() {}

  @Override
  public boolean isFinished() {
    if (terminalAtInitialize) {
      return true;
    }
    if (elevator.getObservation().positionReferenced()) {
      return true;
    }
    return timeoutTrackingStarted
        && clockSeconds.getAsDouble() - startTimeSeconds >= timeoutSeconds;
  }

  @Override
  public void end(boolean interrupted) {
    elevator.stop();
  }
}
