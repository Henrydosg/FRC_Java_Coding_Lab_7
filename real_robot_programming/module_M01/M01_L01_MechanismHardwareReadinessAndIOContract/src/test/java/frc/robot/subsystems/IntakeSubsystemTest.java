// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

/** Verifies Intake state ownership, semantic forwarding, and immutable snapshots. */
class IntakeSubsystemTest {
  @Test
  void beginsStoppedWithoutAutomaticallyRequestingIntake() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);

    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(0, intakeIO.requestCount);
    assertEquals(0, intakeIO.stopCount);
  }

  @Test
  void requestIntakeUpdatesIntentBeforeForwardingSemanticRequest() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;

    subsystem.requestIntake();

    assertEquals(1, intakeIO.requestCount);
    assertEquals(RequestedState.INTAKE_REQUESTED, intakeIO.stateObservedOnRequest);
    assertEquals(RequestedState.INTAKE_REQUESTED, subsystem.getObservation().requestedState());
  }

  @Test
  void stopUpdatesIntentBeforeImmediatelyForwardingStop() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    subsystem.requestIntake();

    subsystem.stop();

    assertEquals(1, intakeIO.stopCount);
    assertEquals(RequestedState.STOPPED, intakeIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  @Test
  void periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    intakeIO.available = true;
    intakeIO.connected = true;
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    subsystem.requestIntake();

    subsystem.periodic();
    IntakeObservation first = subsystem.getObservation();

    assertTrue(first.available());
    assertTrue(first.connected());
    assertEquals(RequestedState.INTAKE_REQUESTED, first.requestedState());

    intakeIO.available = false;
    intakeIO.connected = false;
    subsystem.periodic();
    IntakeObservation second = subsystem.getObservation();

    assertNotSame(first, second);
    assertTrue(first.available());
    assertTrue(first.connected());
    assertFalse(second.available());
    assertFalse(second.connected());
  }

  private static final class RecordingIntakeIO implements IntakeIO {
    private boolean available;
    private boolean connected;
    private int requestCount;
    private int stopCount;
    private Supplier<IntakeObservation> observationSupplier;
    private RequestedState stateObservedOnRequest;
    private RequestedState stateObservedOnStop;

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
    }

    @Override
    public void requestIntake() {
      requestCount++;
      if (observationSupplier != null) {
        stateObservedOnRequest = observationSupplier.get().requestedState();
      }
    }

    @Override
    public void stop() {
      stopCount++;
      stateObservedOnStop = observationSupplier.get().requestedState();
    }
  }
}
