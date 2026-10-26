// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.IntakeConstants;
import frc.robot.io.intake.IntakeIO;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the bounded Test-mode Intake commissioning lifecycle with a deterministic clock. */
class IntakeCommissioningCommandTest {
  private static final double kHalfDurationSeconds =
      IntakeConstants.kCommissioningMaximumDurationSeconds / 2.0;

  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetSchedulerAndEnableTest() {
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    setMode(true, false, true);
  }

  @AfterEach
  void cleanSchedulerAndDisable() {
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    setMode(false, false, false);
  }

  @Test
  void requiresOnlyIntakeAndDoesNotRunWhenDisabled() {
    Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);

    assertEquals(Set.of(rig.intake), rig.command.getRequirements());
    assertFalse(rig.command.runsWhenDisabled());
  }

  @Test
  void forwardAndReverseFactoriesRequestTheApprovedVoltages() {
    Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);

    scheduler.schedule(IntakeCommissioningCommand.forward(rig.intake));
    scheduler.cancelAll();
    scheduler.schedule(IntakeCommissioningCommand.reverse(rig.intake));
    scheduler.cancelAll();

    assertEquals(
        List.of(
            IntakeConstants.kCommissioningVoltageVolts,
            -IntakeConstants.kCommissioningVoltageVolts),
        rig.io.voltageRequests);
  }

  @Test
  void admittedOutputStopsAtTheMaximumDuration() {
    Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);

    scheduler.schedule(rig.command);
    assertEquals(
        RequestedState.COMMISSIONING_REQUESTED, rig.intake.getObservation().requestedState());
    rig.clock[0] = kHalfDurationSeconds;
    scheduler.run();
    assertTrue(rig.command.isScheduled());

    rig.clock[0] = IntakeConstants.kCommissioningMaximumDurationSeconds;
    scheduler.run();

    assertFalse(rig.command.isScheduled());
    assertFalse(rig.intake.isCommissioningActive());
    assertEquals(RequestedState.STOPPED, rig.intake.getObservation().requestedState());
    assertEquals(List.of(IntakeConstants.kCommissioningVoltageVolts), rig.io.voltageRequests);
    assertTrue(rig.io.stopCount >= 1);
  }

  @Test
  void interruptionStopsOutput() {
    Rig rig = new Rig(-IntakeConstants.kCommissioningVoltageVolts);
    scheduler.schedule(rig.command);
    int stopsBefore = rig.io.stopCount;

    scheduler.cancel(rig.command);

    assertFalse(rig.command.isScheduled());
    assertFalse(rig.intake.isCommissioningActive());
    assertTrue(rig.io.stopCount > stopsBefore);
  }

  @Test
  void disableCancelsAndStopsOutput() {
    Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);
    scheduler.schedule(rig.command);
    setMode(false, false, true);

    assertDoesNotThrow(scheduler::run);

    assertFalse(rig.command.isScheduled());
    assertFalse(rig.intake.isCommissioningActive());
    assertEquals(RequestedState.STOPPED, rig.intake.getObservation().requestedState());
  }

  @Test
  void teleopAndAutonomousDenyOutputAndFinish() {
    boolean[] autonomousModes = {false, true};
    for (boolean autonomous : autonomousModes) {
      Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);
      setMode(true, autonomous, false);

      scheduler.schedule(rig.command);
      scheduler.run();

      assertFalse(rig.command.isScheduled());
      assertTrue(rig.io.voltageRequests.isEmpty());
      scheduler.unregisterAllSubsystems();
    }
  }

  @Test
  void failingStopDuringEndDoesNotEscapeAndLatchesFault() {
    Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);
    scheduler.schedule(rig.command);
    rig.io.stopFailure = new IllegalStateException("neutral request rejected");

    assertDoesNotThrow(() -> scheduler.cancel(rig.command));

    assertFalse(rig.command.isScheduled());
    assertTrue(rig.intake.getObservation().faultLatched());
    rig.io.stopFailure = null;
  }

  @Test
  void failedCommissioningDoesNotRestartAutomaticallyOrOnReschedule() {
    Rig rig = new Rig(IntakeConstants.kCommissioningVoltageVolts);
    rig.io.voltageFailure = new IllegalStateException("voltage request rejected");

    scheduler.schedule(rig.command);
    for (int cycle = 0; cycle < 3; cycle++) {
      assertDoesNotThrow(scheduler::run);
    }
    assertFalse(rig.command.isScheduled());
    assertTrue(rig.intake.getObservation().faultLatched());

    rig.io.voltageFailure = null;
    scheduler.schedule(rig.command);
    scheduler.run();

    assertFalse(rig.command.isScheduled());
    assertEquals(1, rig.io.voltageRequests.size());
    assertEquals(RequestedState.STOPPED, rig.intake.getObservation().requestedState());
  }

  private static void setMode(boolean enabled, boolean autonomous, boolean test) {
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.setAutonomous(autonomous);
    DriverStationSim.setTest(test);
    DriverStationSim.notifyNewData();
  }

  private static final class Rig {
    private final double[] clock = {0.0};
    private final RecordingIntakeIO io = new RecordingIntakeIO();
    private final IntakeSubsystem intake = new IntakeSubsystem(io, () -> clock[0]);
    private final IntakeCommissioningCommand command;

    private Rig(double volts) {
      command = IntakeCommissioningCommand.withClock(intake, volts, () -> clock[0]);
      intake.periodic();
    }
  }

  /** Healthy, connected Intake IO with a valid measurement that records output requests. */
  private static final class RecordingIntakeIO implements IntakeIO {
    private final List<Double> voltageRequests = new ArrayList<>();
    private int stopCount;
    private RuntimeException stopFailure;
    private RuntimeException voltageFailure;

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
      inputs.available = true;
      inputs.connected = true;
      inputs.measurementValid = true;
      inputs.timestampSeconds = 0.0;
      inputs.motorPositionRotations = 0.0;
      inputs.motorVelocityRotationsPerSecond = 0.0;
      inputs.statorCurrentAmps = 0.0;
      inputs.supplyCurrentAmps = 0.0;
      inputs.appliedVolts = 0.0;
    }

    @Override
    public void requestIntake() {}

    @Override
    public void stop() {
      stopCount++;
      if (stopFailure != null) {
        throw stopFailure;
      }
    }

    @Override
    public void setVoltage(double volts) {
      voltageRequests.add(volts);
      if (voltageFailure != null) {
        throw voltageFailure;
      }
    }
  }
}
