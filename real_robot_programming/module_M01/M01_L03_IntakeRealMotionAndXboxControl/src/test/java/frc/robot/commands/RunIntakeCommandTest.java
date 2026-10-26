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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.IntakeConstants;
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

/**
 * Verifies held-button manual Intake ownership, subsystem admission, safe stop, button
 * arbitration and the absence of automatic restart.
 */
class RunIntakeCommandTest {
  private static final double kForwardVolts = IntakeConstants.kManualForwardVoltageVolts;
  private static final double kReverseVolts = -IntakeConstants.kManualReverseVoltageMagnitudeVolts;

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
    setMode(true, false, false);
    scheduler.cancelAll();
    scheduler.run();
    rig = new Rig();
    command = new RunIntakeCommand(rig.subsystem);
  }

  @AfterEach
  void resetRuntimeState() {
    scheduler.cancelAll();
    scheduler.getDefaultButtonLoop().clear();
    scheduler.run();
    scheduler.unregisterSubsystem(rig.subsystem);
    setMode(false, false, false);
  }

  @Test
  void rejectsNullSubsystem() {
    assertThrows(NullPointerException.class, () -> new RunIntakeCommand(null));
    assertThrows(NullPointerException.class, () -> RunIntakeCommand.reverse(null));
  }

  @Test
  void requiresExactlyTheSuppliedSubsystemAndDoesNotRunWhenDisabled() {
    assertEquals(1, command.getRequirements().size());
    assertTrue(command.getRequirements().contains(rig.subsystem));
    assertFalse(command.runsWhenDisabled());
    assertFalse(RunIntakeCommand.reverse(rig.subsystem).runsWhenDisabled());
  }

  @Test
  void forwardPressRequestsPlusFourVoltsOnceAndExecuteDoesNotRepeat() {
    command.initialize();
    command.execute();
    command.execute();

    assertEquals(List.of(kForwardVolts), rig.intakeIO.voltageRequests);
    assertEquals(0, rig.intakeIO.requestCount);
    assertFalse(command.isFinished());
    assertEquals(
        RequestedState.MANUAL_FORWARD_REQUESTED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void reversePressRequestsMinusThreeVolts() {
    RunIntakeCommand reverse = RunIntakeCommand.reverse(rig.subsystem);

    reverse.initialize();

    assertEquals(List.of(kReverseVolts), rig.intakeIO.voltageRequests);
    assertEquals(
        RequestedState.MANUAL_REVERSE_REQUESTED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void releaseOrInterruptionStopsThroughNeutralSafeStop() {
    command.initialize();
    command.end(false);
    assertEquals(1, rig.intakeIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());

    RunIntakeCommand reverse = RunIntakeCommand.reverse(rig.subsystem);
    reverse.initialize();
    reverse.end(true);
    assertEquals(2, rig.intakeIO.stopCount);
    assertFalse(rig.subsystem.isManualActive());
  }

  @Test
  void schedulingRequestsOnceAndCancellationStopsIntake() {
    scheduler.schedule(command);
    scheduler.run();
    scheduler.run();

    assertTrue(command.isScheduled());
    assertEquals(1, rig.intakeIO.voltageRequests.size());

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

  @Test
  void disableCancelsAndStops() {
    scheduler.schedule(command);
    setMode(false, false, false);

    scheduler.run();

    assertFalse(command.isScheduled());
    assertFalse(rig.subsystem.isManualActive());
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void autonomousAndTestDenyManualOutput() {
    boolean[][] modes = {{true, false}, {false, true}};
    for (boolean[] mode : modes) {
      setMode(true, mode[0], mode[1]);
      RunIntakeCommand forward = new RunIntakeCommand(rig.subsystem);

      forward.initialize();

      assertTrue(forward.isFinished());
      assertTrue(rig.intakeIO.voltageRequests.isEmpty());
      assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
    }
  }

  @Test
  void invalidOrStaleFeedbackBlocksOutput() {
    rig.intakeIO.measurementValid = false;
    rig.subsystem.periodic();

    command.initialize();

    assertTrue(command.isFinished());
    assertTrue(rig.intakeIO.voltageRequests.isEmpty());
  }

  @Test
  void faultLatchBlocksOutput() {
    rig.subsystem.latchFault();

    command.initialize();

    assertTrue(command.isFinished());
    assertTrue(rig.intakeIO.voltageRequests.isEmpty());
  }

  @Test
  void outputRejectionLatchesFaultAndFinishes() {
    rig.intakeIO.voltageFailure = new IllegalStateException("voltage request rejected");

    assertDoesNotThrow(command::initialize);

    assertTrue(command.isFinished());
    assertTrue(rig.subsystem.getObservation().faultLatched());
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void stopRejectionPreservesInhibition() {
    command.initialize();
    rig.intakeIO.stopFailure = new IllegalStateException("neutral request rejected");

    assertDoesNotThrow(() -> command.end(false));
    rig.intakeIO.stopFailure = null;
    RunIntakeCommand nextPress = new RunIntakeCommand(rig.subsystem);
    nextPress.initialize();

    assertTrue(rig.subsystem.getObservation().faultLatched());
    assertTrue(nextPress.isFinished());
    assertEquals(1, rig.intakeIO.voltageRequests.size());
  }

  @Test
  void forcedSafetyStopNeedsAFreshPressBeforeOutputResumes() {
    boolean[] rightBumper = {false};
    new Trigger(() -> rightBumper[0]).whileTrue(command);
    rightBumper[0] = true;
    scheduler.run();
    assertTrue(command.isScheduled());

    rig.intakeIO.measurementValid = false;
    scheduler.run();
    assertFalse(command.isScheduled());

    rig.intakeIO.measurementValid = true;
    for (int cycle = 0; cycle < 3; cycle++) {
      scheduler.run();
    }
    assertFalse(command.isScheduled());
    assertEquals(1, rig.intakeIO.voltageRequests.size());

    rightBumper[0] = false;
    scheduler.run();
    rightBumper[0] = true;
    scheduler.run();

    assertTrue(command.isScheduled());
    assertEquals(List.of(kForwardVolts, kForwardVolts), rig.intakeIO.voltageRequests);
  }

  @Test
  void laterPressWinsAndInterruptedHeldCommandIsNotRestarted() {
    boolean[] rightBumper = {false};
    boolean[] reverseButton = {false};
    RunIntakeCommand reverse = RunIntakeCommand.reverse(rig.subsystem);
    new Trigger(() -> rightBumper[0]).whileTrue(command);
    new Trigger(() -> reverseButton[0]).whileTrue(reverse);

    rightBumper[0] = true;
    scheduler.run();
    reverseButton[0] = true;
    scheduler.run();

    assertFalse(command.isScheduled());
    assertTrue(reverse.isScheduled());
    assertEquals(
        RequestedState.MANUAL_REVERSE_REQUESTED, rig.subsystem.getObservation().requestedState());

    reverseButton[0] = false;
    scheduler.run();
    scheduler.run();

    assertFalse(command.isScheduled());
    assertFalse(reverse.isScheduled());
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
    assertEquals(List.of(kForwardVolts, kReverseVolts), rig.intakeIO.voltageRequests);
  }

  @Test
  void sameCyclePressOfBothButtonsResolvesDeterministicallyToReverse() {
    boolean[] rightBumper = {false};
    boolean[] reverseButton = {false};
    RunIntakeCommand reverse = RunIntakeCommand.reverse(rig.subsystem);
    // Same registration order as RobotContainer: Right Bumper first, then B.
    new Trigger(() -> rightBumper[0]).whileTrue(command);
    new Trigger(() -> reverseButton[0]).whileTrue(reverse);

    rightBumper[0] = true;
    reverseButton[0] = true;
    scheduler.run();

    assertFalse(command.isScheduled());
    assertTrue(reverse.isScheduled());
    assertEquals(
        RequestedState.MANUAL_REVERSE_REQUESTED, rig.subsystem.getObservation().requestedState());
    assertEquals(List.of(kForwardVolts, kReverseVolts), rig.intakeIO.voltageRequests);
  }

  private static void setMode(boolean enabled, boolean autonomous, boolean test) {
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.setAutonomous(autonomous);
    DriverStationSim.setTest(test);
    DriverStationSim.notifyNewData();
  }

  private static final class Rig {
    private final RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    private final IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);

    private Rig() {
      subsystem.periodic();
    }
  }

  /** Healthy, connected Intake IO whose measurement validity can be switched by the test. */
  private static final class RecordingIntakeIO implements IntakeIO {
    private final List<String> events = new ArrayList<>();
    private final List<Double> voltageRequests = new ArrayList<>();
    private boolean measurementValid = true;
    private int requestCount;
    private int stopCount;
    private RuntimeException stopFailure;
    private RuntimeException voltageFailure;

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
      inputs.available = true;
      inputs.connected = true;
      inputs.measurementValid = measurementValid;
      double value = measurementValid ? 0.0 : Double.NaN;
      inputs.timestampSeconds = value;
      inputs.motorPositionRotations = value;
      inputs.motorVelocityRotationsPerSecond = value;
      inputs.statorCurrentAmps = value;
      inputs.supplyCurrentAmps = value;
      inputs.appliedVolts = value;
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
      if (stopFailure != null) {
        throw stopFailure;
      }
    }

    @Override
    public void setVoltage(double volts) {
      voltageRequests.add(volts);
      events.add("voltage");
      if (voltageFailure != null) {
        throw voltageFailure;
      }
    }
  }
}
