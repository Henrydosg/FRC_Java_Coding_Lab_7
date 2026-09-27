// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutonomousEventDemonstrationCommandTest {
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  @BeforeAll
  static void initializeHal() {
    assertTrue(HAL.initialize(500, 0), "HAL initialization failed");
  }

  @BeforeEach
  void enableAutonomousMode() {
    DriverStationSim.resetData();
    setAutonomousMode();
  }

  @AfterEach
  void cleanup() {
    setDisabledMode();
    scheduler.cancelAll();
    scheduler.run();
  }

  @Test
  void followsStartedActiveCompletedLifecycleWithoutRequirements() {
    MutableClock clock = new MutableClock(10.0);
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventDemonstrationCommand command =
        new AutonomousEventDemonstrationCommand(
            AutonomousEventId.LEARNING_EVENT, observations::add, clock, 0.50);

    scheduler.schedule(command);
    scheduler.run();
    assertTrue(command.isScheduled());
    clock.value = 10.50;
    scheduler.run();

    assertEquals(List.of(LifecycleState.STARTED, LifecycleState.ACTIVE,
        LifecycleState.ACTIVE, LifecycleState.COMPLETED),
        observations.stream().map(AutonomousEventObservation::state).toList());
    assertTrue(command.getRequirements().isEmpty());
  }

  @Test
  void interruptionPublishesCancelled() {
    MutableClock clock = new MutableClock(0.0);
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventDemonstrationCommand command =
        new AutonomousEventDemonstrationCommand(
            AutonomousEventId.LEARNING_EVENT, observations::add, clock, 0.50);

    scheduler.schedule(command);
    scheduler.run();
    command.cancel();
    scheduler.run();

    assertEquals(LifecycleState.CANCELLED, observations.get(observations.size() - 1).state());
    assertTrue(!observations.get(observations.size() - 1).active());
  }

  private static void setAutonomousMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void setDisabledMode() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static final class MutableClock implements java.util.function.DoubleSupplier {
    private double value;

    private MutableClock(double value) {
      this.value = value;
    }

    @Override
    public double getAsDouble() {
      return value;
    }
  }
}
