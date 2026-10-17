// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.observation.feeder.FeederObservation.RequestedState;
import frc.robot.subsystems.FeederSubsystem;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies scheduler ownership, one-shot request, and unconditional Feeder safe stop. */
class RunFeederCommandTest {
  private final CommandScheduler scheduler = CommandScheduler.getInstance();
  private Rig rig;
  private RunFeederCommand command;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableTeleop() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.unregisterAllSubsystems();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    rig = new Rig();
    command = new RunFeederCommand(rig.subsystem);
  }

  @AfterEach
  void resetRuntimeState() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.unregisterAllSubsystems();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void rejectsNullSubsystem() {
    assertThrows(NullPointerException.class, () -> new RunFeederCommand(null));
  }

  @Test
  void requiresExactlyTheSuppliedSubsystemAndDoesNotRunWhenDisabled() {
    assertEquals(1, command.getRequirements().size());
    assertTrue(command.getRequirements().contains(rig.subsystem));
    assertFalse(command.runsWhenDisabled());
  }

  @Test
  void initializeRequestsOnceAndExecuteDoesNotRepeatTheRequest() {
    command.initialize();
    command.execute();
    command.execute();

    assertEquals(1, rig.feederIO.requestCount);
    assertEquals(RequestedState.FEED_REQUESTED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void neverFinishesOnItsOwn() {
    assertFalse(command.isFinished());
  }

  @Test
  void normalEndStopsFeederExactlyOnce() {
    command.initialize();

    command.end(false);

    assertEquals(1, rig.feederIO.requestCount);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void interruptedEndStopsFeederExactlyOnce() {
    command.initialize();

    command.end(true);

    assertEquals(1, rig.feederIO.requestCount);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void schedulingRequestsOnceAndCancellationStopsFeeder() {
    scheduler.schedule(command);
    scheduler.run();
    scheduler.run();

    assertTrue(command.isScheduled());
    assertEquals(1, rig.feederIO.requestCount);

    scheduler.cancel(command);

    assertFalse(command.isScheduled());
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void competingRequirementStopsBeforeOwnershipTransfers() {
    scheduler.schedule(command);
    scheduler.run();
    rig.feederIO.events.clear();
    Command competing =
        Commands.runOnce(() -> rig.feederIO.events.add("competing-initialize"), rig.subsystem);

    scheduler.schedule(competing);

    assertFalse(command.isScheduled());
    assertEquals(List.of("stop", "competing-initialize"), rig.feederIO.events);
    assertEquals(1, rig.feederIO.stopCount);
  }

  private static final class Rig {
    private final RecordingFeederIO feederIO = new RecordingFeederIO();
    private final FeederSubsystem subsystem = new FeederSubsystem(feederIO);
  }

  private static final class RecordingFeederIO implements FeederIO {
    private final List<String> events = new ArrayList<>();
    private int requestCount;
    private int stopCount;

    @Override
    public void updateInputs(FeederIOInputs inputs) {
      inputs.available = true;
      inputs.connected = true;
    }

    @Override
    public void requestFeed() {
      requestCount++;
      events.add("request");
    }

    @Override
    public void stop() {
      stopCount++;
      events.add("stop");
    }
  }
}
