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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.AllianceStationID;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AutonomousEventRegistrationTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @org.junit.jupiter.api.BeforeEach
  void enableAutonomousScheduling() {
    DriverStationSim.setAllianceStationId(AllianceStationID.Blue1);
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.notifyNewData();
  }

  @AfterEach
  void clearNamedCommands() {
    CommandScheduler.getInstance().cancelAll();
    NamedCommands.clearAll();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void registersExactlyOneStableLearningEventAndRejectsDuplicates() {
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventBinding binding =
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT, Commands::none, Set.of());

    AutonomousEventRegistration.register(binding, observations::add);
    assertTrue(NamedCommands.hasCommand("LEARNING_EVENT"));
    assertThrows(
        IllegalStateException.class,
        () -> AutonomousEventRegistration.register(binding, observations::add));
  }

  @Test
  void deferredRegistrationCreatesFreshInnerCommandsAndPublishesFactoryFailure() {
    AtomicInteger factoryCalls = new AtomicInteger();
    AtomicInteger runs = new AtomicInteger();
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventBinding binding =
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT,
            () -> {
              factoryCalls.incrementAndGet();
              return Commands.runOnce(runs::incrementAndGet);
            },
            Set.of());

    AutonomousEventRegistration.register(binding, observations::add);
    Command first = NamedCommands.getCommand("LEARNING_EVENT");
    Command second = NamedCommands.getCommand("LEARNING_EVENT");
    assertNotSame(first, second);
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(first);
    scheduler.run();
    scheduler.schedule(second);
    scheduler.run();
    assertEquals(2, factoryCalls.get());
    assertEquals(2, runs.get());
  }

  @Test
  void supplierNullAndSupplierThrowBecomeNoOpFactoryFailures() {
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventBinding nullBinding =
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT, () -> null, Set.of());
    AutonomousEventRegistration.register(nullBinding, observations::add);
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(NamedCommands.getCommand("LEARNING_EVENT"));
    scheduler.run();
    assertFalse(observations.isEmpty());
    assertEquals(LifecycleState.FACTORY_FAILURE, observations.get(0).state());

    NamedCommands.clearAll();
    observations.clear();
    AutonomousEventBinding throwingBinding =
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT,
            () -> {
              throw new IllegalStateException("factory failure");
            },
            Set.of());
    AutonomousEventRegistration.register(throwingBinding, observations::add);
    scheduler.schedule(NamedCommands.getCommand("LEARNING_EVENT"));
    scheduler.run();
    assertEquals(LifecycleState.FACTORY_FAILURE, observations.get(0).state());
  }
}
