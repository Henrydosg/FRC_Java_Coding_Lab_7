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
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

/** Deterministic, non-mechanism demonstration command for the lesson event marker. */
public final class AutonomousEventDemonstrationCommand extends Command {
  private final AutonomousEventId eventId;
  private final Consumer<AutonomousEventObservation> observationSink;
  private final DoubleSupplier clock;
  private final double durationSeconds;

  private double startTimeSeconds;

  /** Creates a demonstration event using the WPILib FPGA timestamp and lesson duration. */
  public AutonomousEventDemonstrationCommand(
      AutonomousEventId eventId,
      Consumer<AutonomousEventObservation> observationSink,
      double durationSeconds) {
    this(eventId, observationSink, Timer::getFPGATimestamp, durationSeconds);
  }

  /** Creates a demonstration event with an injectable monotonic clock for deterministic tests. */
  public AutonomousEventDemonstrationCommand(
      AutonomousEventId eventId,
      Consumer<AutonomousEventObservation> observationSink,
      DoubleSupplier clock,
      double durationSeconds) {
    this.eventId = Objects.requireNonNull(eventId, "eventId");
    this.observationSink = Objects.requireNonNull(observationSink, "observationSink");
    this.clock = Objects.requireNonNull(clock, "clock");
    if (!Double.isFinite(durationSeconds) || durationSeconds <= 0.0) {
      throw new IllegalArgumentException("durationSeconds must be finite and positive");
    }
    this.durationSeconds = durationSeconds;
  }

  @Override
  public void initialize() {
    startTimeSeconds = readClock();
    publish(LifecycleState.STARTED, true);
  }

  @Override
  public void execute() {
    double currentTimeSeconds = readClock();
    validateForwardTime(currentTimeSeconds);
    publish(LifecycleState.ACTIVE, true);
  }

  @Override
  public boolean isFinished() {
    double currentTimeSeconds = readClock();
    validateForwardTime(currentTimeSeconds);
    return currentTimeSeconds - startTimeSeconds >= durationSeconds;
  }

  @Override
  public void end(boolean interrupted) {
    publish(interrupted ? LifecycleState.CANCELLED : LifecycleState.COMPLETED, false);
  }

  private double readClock() {
    double value = clock.getAsDouble();
    if (!Double.isFinite(value)) {
      throw new IllegalStateException("event clock must be finite");
    }
    return value;
  }

  private void validateForwardTime(double currentTimeSeconds) {
    if (currentTimeSeconds < startTimeSeconds) {
      throw new IllegalStateException("event clock moved backwards");
    }
  }

  private void publish(LifecycleState state, boolean active) {
    observationSink.accept(new AutonomousEventObservation(eventId, state, active));
  }
}
