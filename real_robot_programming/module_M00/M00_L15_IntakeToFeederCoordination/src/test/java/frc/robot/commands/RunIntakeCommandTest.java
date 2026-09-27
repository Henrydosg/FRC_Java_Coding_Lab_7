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
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies scheduler ownership, one-shot request, and unconditional Intake safe stop. */
class RunIntakeCommandTest {
  private final CommandScheduler scheduler = CommandScheduler.getInstance();
  private Rig rig;
  private RunIntakeCommand command;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableTeleop() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    scheduler.cancelAll();
    scheduler.run();
    rig = new Rig();
    command = new RunIntakeCommand(rig.subsystem);
  }

  @AfterEach
  void resetRuntimeState() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.unregisterSubsystem(rig.subsystem);
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void rejectsNullSubsystem() {
    assertThrows(NullPointerException.class, () -> new RunIntakeCommand(null));
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

    assertEquals(1, rig.intakeIO.requestCount);
    assertFalse(command.isFinished());
    assertEquals(RequestedState.INTAKE_REQUESTED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void defensiveNormalEndStopsIntake() {
    command.initialize();

    command.end(false);

    assertEquals(1, rig.intakeIO.requestCount);
    assertEquals(1, rig.intakeIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void schedulingRequestsOnceAndCancellationStopsIntake() {
    scheduler.schedule(command);
    scheduler.run();
    scheduler.run();

    assertTrue(command.isScheduled());
    assertEquals(1, rig.intakeIO.requestCount);

    scheduler.cancel(command);

    assertFalse(command.isScheduled());
    assertEquals(1, rig.intakeIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void competingRequirementStopsBeforeOwnershipTransfers() {
    scheduler.schedule(command);
    scheduler.run();
    rig.intakeIO.events.clear();
    Command competing =
        Commands.runOnce(() -> rig.intakeIO.events.add("competing-initialize"), rig.subsystem);

    scheduler.schedule(competing);

    assertFalse(command.isScheduled());
    assertEquals(List.of("stop", "competing-initialize"), rig.intakeIO.events);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  private static final class Rig {
    private final RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    private final IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
  }

  private static final class RecordingIntakeIO implements IntakeIO {
    private final List<String> events = new ArrayList<>();
    private int requestCount;
    private int stopCount;

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
      inputs.available = true;
      inputs.connected = true;
    }

    @Override
    public void requestIntake() {
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
