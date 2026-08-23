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
import frc.robot.Constants;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;

/** Deterministic, mechanism-independent bounded event command for L09 learning. */
public final class AutonomousEventDemonstrationCommand extends Command {
  private final Consumer<AutonomousEventObservation> observationSink;
  private final DoubleSupplier monotonicClock;
  private final double durationSeconds = Constants.PathPlannerLearningConstants.kLearningEventDurationSeconds;

  private double startTimeSeconds;
  private boolean initialized;
  private boolean running;
  private boolean terminalObservationPublished;

  /** Creates a production command using the WPILib FPGA clock. */
  public AutonomousEventDemonstrationCommand(
      Consumer<AutonomousEventObservation> observationSink) {
    this(observationSink, Timer::getFPGATimestamp);
  }

  /** Creates a command with an injectable monotonic clock for deterministic tests. */
  AutonomousEventDemonstrationCommand(
      Consumer<AutonomousEventObservation> observationSink, DoubleSupplier monotonicClock) {
    this.observationSink = Objects.requireNonNull(observationSink, "observationSink");
    this.monotonicClock = Objects.requireNonNull(monotonicClock, "monotonicClock");
    if (!Double.isFinite(durationSeconds) || durationSeconds <= 0.0) {
      throw new IllegalStateException("learning event duration must be finite and positive");
    }
  }

  @Override
  public void initialize() {
    initialized = true;
    running = false;
    terminalObservationPublished = false;

    double nowSeconds = readClock();
    if (!Double.isFinite(nowSeconds)) {
      publishTerminal(LifecycleState.FACTORY_FAILURE);
      return;
    }

    startTimeSeconds = nowSeconds;
    running = true;
    publish(new AutonomousEventObservation(AutonomousEventId.LEARNING_EVENT, LifecycleState.STARTED, true));
  }

  @Override
  public void execute() {
    if (!initialized || !running) {
      return;
    }

    double nowSeconds = readClock();
    if (!Double.isFinite(nowSeconds) || nowSeconds < startTimeSeconds) {
      publishTerminal(LifecycleState.FACTORY_FAILURE);
      return;
    }

    if (nowSeconds - startTimeSeconds >= durationSeconds) {
      running = false;
      publishTerminal(LifecycleState.COMPLETED);
      return;
    }

    publish(new AutonomousEventObservation(AutonomousEventId.LEARNING_EVENT, LifecycleState.ACTIVE, true));
  }

  @Override
  public boolean isFinished() {
    return initialized && !running;
  }

  @Override
  public void end(boolean interrupted) {
    if (interrupted && initialized && running) {
      running = false;
      publishTerminal(LifecycleState.CANCELLED);
    }
    initialized = false;
  }

  private double readClock() {
    try {
      return monotonicClock.getAsDouble();
    } catch (RuntimeException failure) {
      return Double.NaN;
    }
  }

  private void publishTerminal(LifecycleState state) {
    running = false;
    if (!terminalObservationPublished) {
      terminalObservationPublished = true;
      publish(new AutonomousEventObservation(AutonomousEventId.LEARNING_EVENT, state, false));
    }
  }

  private void publish(AutonomousEventObservation observation) {
    try {
      observationSink.accept(observation);
    } catch (RuntimeException failure) {
      running = false;
      if (!terminalObservationPublished) {
        terminalObservationPublished = true;
        try {
          observationSink.accept(
              new AutonomousEventObservation(
                  AutonomousEventId.LEARNING_EVENT, LifecycleState.FACTORY_FAILURE, false));
        } catch (RuntimeException ignored) {
          // A failing observation sink cannot safely be recovered by this command.
        }
      }
    }
  }
}
