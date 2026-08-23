// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class AutonomousEventDemonstrationCommandTest {
  @Test
  void publishesDeterministicStartedActiveAndCompletedLifecycle() {
    MutableClock clock = new MutableClock();
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventDemonstrationCommand command =
        new AutonomousEventDemonstrationCommand(observations::add, clock);

    assertTrue(command.getRequirements().isEmpty());
    command.initialize();
    assertEquals(LifecycleState.STARTED, observations.get(0).state());
    assertTrue(observations.get(0).active());
    assertFalse(command.isFinished());

    clock.seconds = 0.10;
    command.execute();
    assertEquals(LifecycleState.ACTIVE, observations.get(1).state());

    clock.seconds = 0.50;
    command.execute();
    assertEquals(LifecycleState.COMPLETED, observations.get(2).state());
    assertFalse(observations.get(2).active());
    assertTrue(command.isFinished());
    command.end(false);
  }

  @Test
  void cancellationPublishesCancelledAndDoesNotReuseState() {
    MutableClock clock = new MutableClock();
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventDemonstrationCommand command =
        new AutonomousEventDemonstrationCommand(observations::add, clock);

    command.initialize();
    command.end(true);

    assertEquals(LifecycleState.CANCELLED, observations.get(1).state());
    assertFalse(observations.get(1).active());

    AutonomousEventDemonstrationCommand second =
        new AutonomousEventDemonstrationCommand(observations::add, clock);
    second.initialize();
    assertEquals(LifecycleState.STARTED, observations.get(2).state());
  }

  @Test
  void invalidOrBackwardClockFailsClosed() {
    MutableClock clock = new MutableClock();
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventDemonstrationCommand command =
        new AutonomousEventDemonstrationCommand(observations::add, clock);

    command.initialize();
    clock.seconds = -1.0;
    command.execute();
    assertEquals(LifecycleState.FACTORY_FAILURE, observations.get(1).state());
    assertTrue(command.isFinished());

    MutableClock invalidClock = new MutableClock();
    invalidClock.seconds = Double.NaN;
    List<AutonomousEventObservation> invalidObservations = new ArrayList<>();
    AutonomousEventDemonstrationCommand invalidCommand =
        new AutonomousEventDemonstrationCommand(invalidObservations::add, invalidClock);
    invalidCommand.initialize();
    assertEquals(LifecycleState.FACTORY_FAILURE, invalidObservations.get(0).state());
    assertTrue(invalidCommand.isFinished());
  }

  private static final class MutableClock implements java.util.function.DoubleSupplier {
    private double seconds;

    @Override
    public double getAsDouble() {
      return seconds;
    }
  }
}
