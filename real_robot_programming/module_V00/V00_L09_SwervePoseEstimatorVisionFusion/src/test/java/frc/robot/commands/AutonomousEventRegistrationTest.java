// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutonomousEventRegistrationTest {
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  @BeforeAll
  static void initializeHal() {
    assertTrue(HAL.initialize(500, 0), "HAL initialization failed");
  }

  @BeforeEach
  void clearNamedCommands() {
    scheduler.cancelAll();
    NamedCommands.clearAll();
    DriverStationSim.resetData();
    setAutonomousMode();
  }

  @AfterEach
  void cleanup() {
    setDisabledMode();
    scheduler.cancelAll();
    scheduler.run();
    NamedCommands.clearAll();
  }

  @Test
  void usesDeferredSchedulerOwnedConstructionForFreshDispatches() {
    List<Command> createdCommands = new ArrayList<>();
    AtomicInteger factoryCalls = new AtomicInteger();
    AutonomousEventRegistration registration =
        new AutonomousEventRegistration(observation -> {});
    registration.register(
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT,
            () -> {
              factoryCalls.incrementAndGet();
              Command command = Commands.none();
              createdCommands.add(command);
              return command;
            },
            Set.of()));

    Command deferred = NamedCommands.getCommand("LEARNING_EVENT");
    scheduler.schedule(deferred);
    scheduler.run();
    scheduler.schedule(deferred);
    scheduler.run();

    assertEquals(2, factoryCalls.get());
    assertEquals(2, createdCommands.size());
    assertNotSame(createdCommands.get(0), createdCommands.get(1));
    assertTrue(deferred.getRequirements().isEmpty());
  }

  @Test
  void factoryFailurePublishesObservationAndReturnsSafeNoOp() {
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventRegistration registration =
        new AutonomousEventRegistration(observations::add);
    registration.register(
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT,
            () -> {
              throw new IllegalStateException("factory failure");
            },
            Set.of()));

    scheduler.schedule(NamedCommands.getCommand("LEARNING_EVENT"));
    scheduler.run();

    assertEquals(1, observations.size());
    assertEquals(LifecycleState.FACTORY_FAILURE, observations.get(0).state());
    assertTrue(NamedCommands.getCommand("LEARNING_EVENT").getRequirements().isEmpty());
  }

  @Test
  void duplicateNameIsRejectedBeforeRegistration() {
    AutonomousEventRegistration registration =
        new AutonomousEventRegistration(observation -> {});
    AutonomousEventBinding binding =
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT, Commands::none, Set.of());
    registration.register(binding);

    assertThrows(IllegalStateException.class, () -> registration.register(binding));
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
}
