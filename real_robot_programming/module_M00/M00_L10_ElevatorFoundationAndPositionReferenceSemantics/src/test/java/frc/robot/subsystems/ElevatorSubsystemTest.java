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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.observation.elevator.ElevatorObservation;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

/** Verifies Elevator observation normalization and explicit safe-stop ownership. */
class ElevatorSubsystemTest {
  @Test
  void beginsWithConservativeObservationWithoutOutput() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    assertEquals(new ElevatorObservation(false, false, false, false, 0.0), subsystem.getObservation());
    assertEquals(0, elevatorIO.updateCount);
    assertEquals(0, elevatorIO.stopCount);
  }

  @Test
  void periodicRefreshesInputsOnceAndRebuildsObservationWithoutOutput() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 1.25);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    ElevatorObservation initial = subsystem.getObservation();
    subsystem.periodic();
    ElevatorObservation refreshed = subsystem.getObservation();

    assertEquals(1, elevatorIO.updateCount);
    assertEquals(0, elevatorIO.stopCount);
    assertTrue(refreshed.available());
    assertTrue(refreshed.connected());
    assertTrue(refreshed.positionValid());
    assertTrue(refreshed.positionReferenced());
    assertEquals(1.25, refreshed.positionMeters());
    assertTrue(initial != refreshed);
  }

  @Test
  void preservesFinitePositiveNegativeAndUnreferencedPositions() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    elevatorIO.configure(true, true, true, false, -0.75);
    subsystem.periodic();
    ElevatorObservation negative = subsystem.getObservation();
    assertEquals(-0.75, negative.positionMeters());
    assertTrue(negative.positionValid());
    assertFalse(negative.positionReferenced());

    elevatorIO.configure(true, true, true, false, 0.0);
    subsystem.periodic();
    ElevatorObservation validZero = subsystem.getObservation();
    assertTrue(validZero.available());
    assertTrue(validZero.connected());
    assertTrue(validZero.positionValid());
    assertFalse(validZero.positionReferenced());
    assertEquals(0.0, validZero.positionMeters());
  }

  @Test
  void normalizesUnavailableDisconnectedAndInvalidPrerequisites() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    elevatorIO.configure(false, true, true, true, 2.0);
    subsystem.periodic();
    assertFalse(subsystem.getObservation().connected());
    assertFalse(subsystem.getObservation().positionValid());
    assertFalse(subsystem.getObservation().positionReferenced());

    elevatorIO.configure(true, false, true, true, 2.0);
    subsystem.periodic();
    assertFalse(subsystem.getObservation().positionValid());
    assertFalse(subsystem.getObservation().positionReferenced());

    elevatorIO.configure(true, true, false, true, 2.0);
    subsystem.periodic();
    assertFalse(subsystem.getObservation().positionValid());
    assertFalse(subsystem.getObservation().positionReferenced());
  }

  @Test
  void normalizesAllNonFinitePositionsToInvalidZero() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    for (double nonFinite : new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      elevatorIO.configure(true, true, true, true, nonFinite);
      subsystem.periodic();
      ElevatorObservation observation = subsystem.getObservation();
      assertFalse(observation.positionValid());
      assertFalse(observation.positionReferenced());
      assertEquals(0.0, observation.positionMeters());
    }
  }

  @Test
  void stopCallsIoExactlyOnceWithoutChangingObservation() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 0.5);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    ElevatorObservation beforeStop = subsystem.getObservation();

    subsystem.stop();

    assertEquals(1, elevatorIO.stopCount);
    assertSame(beforeStop, subsystem.getObservation());
    assertEquals(0.5, subsystem.getObservation().positionMeters());
    assertTrue(subsystem.getObservation().positionReferenced());
  }

  @Test
  void stopPropagatesOriginalExceptionWithoutRetryOrStateMutation() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, false, 0.5);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    ElevatorObservation beforeStop = subsystem.getObservation();
    RuntimeException failure = new RuntimeException("stop failure");
    elevatorIO.stopFailure = failure;

    RuntimeException thrown = assertThrows(RuntimeException.class, subsystem::stop);

    assertSame(failure, thrown);
    assertEquals(1, elevatorIO.stopCount);
    assertSame(beforeStop, subsystem.getObservation());
  }

  private static final class RecordingElevatorIO implements ElevatorIO {
    private boolean available;
    private boolean connected;
    private boolean positionValid;
    private boolean positionReferenced;
    private double positionMeters;
    private int updateCount;
    private int stopCount;
    private RuntimeException stopFailure;
    private Consumer<ElevatorIOInputs> updateHook;

    private void configure(
        boolean available,
        boolean connected,
        boolean positionValid,
        boolean positionReferenced,
        double positionMeters) {
      this.available = available;
      this.connected = connected;
      this.positionValid = positionValid;
      this.positionReferenced = positionReferenced;
      this.positionMeters = positionMeters;
    }

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
      updateCount++;
      inputs.available = available;
      inputs.connected = connected;
      inputs.positionValid = positionValid;
      inputs.positionReferenced = positionReferenced;
      inputs.positionMeters = positionMeters;
      if (updateHook != null) {
        updateHook.accept(inputs);
      }
    }

    @Override
    public void stop() {
      stopCount++;
      if (stopFailure != null) {
        throw stopFailure;
      }
    }
  }
}
