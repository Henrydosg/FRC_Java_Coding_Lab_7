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
import frc.robot.observation.elevator.ElevatorRequestedState;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

/** Verifies Elevator request semantics, normalization, and explicit safe-stop ownership. */
class ElevatorSubsystemTest {
  @Test
  void beginsStoppedWithCanonicalInactivePlaceholdersWithoutOutput() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    ElevatorObservation observation = subsystem.getObservation();
    assertEquals(ElevatorRequestedState.STOPPED, observation.requestedState());
    assertEquals(0.0, observation.targetPositionMeters());
    assertEquals(0.0, observation.positionErrorMeters());
    assertEquals(0, elevatorIO.updateCount);
    assertEquals(0, elevatorIO.requestCount);
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
    assertEquals(0, elevatorIO.requestCount);
    assertEquals(0, elevatorIO.stopCount);
    assertTrue(refreshed.available());
    assertTrue(refreshed.connected());
    assertTrue(refreshed.positionValid());
    assertTrue(refreshed.positionReferenced());
    assertEquals(1.25, refreshed.positionMeters());
    assertEquals(ElevatorRequestedState.STOPPED, refreshed.requestedState());
    assertEquals(0.0, refreshed.targetPositionMeters());
    assertEquals(0.0, refreshed.positionErrorMeters());
    assertTrue(initial != refreshed);
  }

  @Test
  void acceptsFinitePositiveZeroAndNegativeTargets() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 1.25);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();

    subsystem.requestPositionMeters(2.0);
    assertRequest(subsystem.getObservation(), 2.0, 0.75);
    assertEquals(1, elevatorIO.requestCount);
    assertEquals(2.0, elevatorIO.lastRequestedTarget);

    subsystem.requestPositionMeters(0.0);
    assertRequest(subsystem.getObservation(), 0.0, -1.25);
    assertEquals(2, elevatorIO.requestCount);

    subsystem.requestPositionMeters(-0.5);
    assertRequest(subsystem.getObservation(), -0.5, -1.75);
    assertEquals(3, elevatorIO.requestCount);
    assertEquals(-0.5, elevatorIO.lastRequestedTarget);
  }

  @Test
  void rejectsNonFiniteTargetsWithoutMutationOrIoRequest() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 1.25);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    ElevatorObservation before = subsystem.getObservation();

    for (double target : new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      assertThrows(IllegalArgumentException.class, () -> subsystem.requestPositionMeters(target));
      assertSame(before, subsystem.getObservation());
      assertEquals(0, elevatorIO.requestCount);
    }
  }

  @Test
  void rejectsUnusableOrUnreferencedMeasurementsWithoutMutationOrIoRequest() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    elevatorIO.configure(true, true, false, true, 1.25);
    subsystem.periodic();
    ElevatorObservation invalid = subsystem.getObservation();
    assertThrows(IllegalStateException.class, () -> subsystem.requestPositionMeters(2.0));
    assertSame(invalid, subsystem.getObservation());
    assertEquals(0, elevatorIO.requestCount);

    elevatorIO.configure(true, true, true, false, 1.25);
    subsystem.periodic();
    ElevatorObservation unreferenced = subsystem.getObservation();
    assertThrows(IllegalStateException.class, () -> subsystem.requestPositionMeters(2.0));
    assertSame(unreferenced, subsystem.getObservation());
    assertEquals(0, elevatorIO.requestCount);
  }

  @Test
  void periodicPreservesIntentRecomputesErrorAndDoesNotReissueRequest() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 1.25);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    subsystem.requestPositionMeters(2.0);

    elevatorIO.configure(true, true, true, true, 1.5);
    subsystem.periodic();

    assertEquals(2, elevatorIO.updateCount);
    assertEquals(1, elevatorIO.requestCount);
    assertRequest(subsystem.getObservation(), 2.0, 0.5);
  }

  @Test
  void normalizesMeasurementAndReferenceSemanticsWithoutInventingTrust() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);

    elevatorIO.configure(false, true, true, true, 2.0);
    subsystem.periodic();
    assertFalse(subsystem.getObservation().connected());
    assertFalse(subsystem.getObservation().positionValid());
    assertFalse(subsystem.getObservation().positionReferenced());

    elevatorIO.configure(true, true, true, false, -0.75);
    subsystem.periodic();
    assertTrue(subsystem.getObservation().positionValid());
    assertFalse(subsystem.getObservation().positionReferenced());
    assertEquals(-0.75, subsystem.getObservation().positionMeters());

    for (double nonFinite : new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      elevatorIO.configure(true, true, true, true, nonFinite);
      subsystem.periodic();
      assertFalse(subsystem.getObservation().positionValid());
      assertFalse(subsystem.getObservation().positionReferenced());
      assertEquals(0.0, subsystem.getObservation().positionMeters());
    }
  }

  @Test
  void stopRecordsStoppedZeroTargetAndZeroErrorBeforeOneIoStop() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 0.5);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    subsystem.requestPositionMeters(2.0);

    subsystem.stop();

    ElevatorObservation observation = subsystem.getObservation();
    assertEquals(ElevatorRequestedState.STOPPED, observation.requestedState());
    assertEquals(0.0, observation.targetPositionMeters());
    assertEquals(0.0, observation.positionErrorMeters());
    assertEquals(0.5, observation.positionMeters());
    assertEquals(1, elevatorIO.stopCount);
    subsystem.periodic();
    assertEquals(1, elevatorIO.stopCount);
  }

  @Test
  void requestIoExceptionPropagatesAndPreservesRecordedIntent() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 1.0);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    RuntimeException failure = new RuntimeException("request failure");
    elevatorIO.requestFailure = failure;

    RuntimeException thrown =
        assertThrows(RuntimeException.class, () -> subsystem.requestPositionMeters(2.0));

    assertSame(failure, thrown);
    assertEquals(1, elevatorIO.requestCount);
    assertRequest(subsystem.getObservation(), 2.0, 1.0);
  }

  @Test
  void stopIoExceptionPropagatesAndPreservesStoppedIntent() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, true, true, 1.0);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    subsystem.requestPositionMeters(2.0);
    RuntimeException failure = new RuntimeException("stop failure");
    elevatorIO.stopFailure = failure;

    RuntimeException thrown = assertThrows(RuntimeException.class, subsystem::stop);

    assertSame(failure, thrown);
    assertEquals(1, elevatorIO.stopCount);
    assertEquals(ElevatorRequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(0.0, subsystem.getObservation().targetPositionMeters());
    assertEquals(0.0, subsystem.getObservation().positionErrorMeters());
  }

  @Test
  void homingRejectsUnavailableOrDisconnectedWithoutChangingObservation() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    elevatorIO.configure(true, true, true, true, 1.0);
    subsystem.periodic();
    subsystem.requestPositionMeters(2.0);

    elevatorIO.configure(false, false, false, false, 0.0);
    subsystem.periodic();
    ElevatorObservation unavailable = subsystem.getObservation();
    int unavailableUpdateCount = elevatorIO.updateCount;
    int unavailablePositionRequestCount = elevatorIO.requestCount;
    int unavailableStopCount = elevatorIO.stopCount;
    assertThrows(IllegalStateException.class, subsystem::requestHoming);
    assertSame(unavailable, subsystem.getObservation());
    assertEquals(unavailableUpdateCount, elevatorIO.updateCount);
    assertEquals(unavailablePositionRequestCount, elevatorIO.requestCount);
    assertEquals(unavailableStopCount, elevatorIO.stopCount);
    assertEquals(0, elevatorIO.homingRequestCount);
    subsystem.periodic();
    assertEquals(ElevatorRequestedState.POSITION_REQUESTED, subsystem.getObservation().requestedState());
    assertEquals(2.0, subsystem.getObservation().targetPositionMeters());

    elevatorIO.configure(true, false, false, false, 0.0);
    subsystem.periodic();
    ElevatorObservation disconnected = subsystem.getObservation();
    int disconnectedUpdateCount = elevatorIO.updateCount;
    int disconnectedPositionRequestCount = elevatorIO.requestCount;
    int disconnectedStopCount = elevatorIO.stopCount;
    assertThrows(IllegalStateException.class, subsystem::requestHoming);
    assertSame(disconnected, subsystem.getObservation());
    assertEquals(disconnectedUpdateCount, elevatorIO.updateCount);
    assertEquals(disconnectedPositionRequestCount, elevatorIO.requestCount);
    assertEquals(disconnectedStopCount, elevatorIO.stopCount);
    assertEquals(0, elevatorIO.homingRequestCount);
    subsystem.periodic();
    assertEquals(ElevatorRequestedState.POSITION_REQUESTED, subsystem.getObservation().requestedState());
    assertEquals(2.0, subsystem.getObservation().targetPositionMeters());
  }

  @Test
  void homingNeedsAvailabilityAndConnectionButNotValidOrReferencedPosition() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, false, false, 0.0);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    elevatorIO.homingRequestHook =
        () -> {
          ElevatorObservation observation = subsystem.getObservation();
          assertEquals(ElevatorRequestedState.HOMING, observation.requestedState());
          assertEquals(0.0, observation.targetPositionMeters());
          assertEquals(0.0, observation.positionErrorMeters());
          assertFalse(observation.positionValid());
          assertFalse(observation.positionReferenced());
        };

    subsystem.requestHoming();

    assertEquals(1, elevatorIO.homingRequestCount);
    assertEquals(0, elevatorIO.requestCount);
    assertEquals(0, elevatorIO.stopCount);
    assertEquals(ElevatorRequestedState.HOMING, subsystem.getObservation().requestedState());
    subsystem.periodic();
    assertEquals(1, elevatorIO.homingRequestCount);
    assertEquals(0, elevatorIO.requestCount);
    assertEquals(0, elevatorIO.stopCount);
    assertEquals(ElevatorRequestedState.HOMING, subsystem.getObservation().requestedState());
    assertFalse(subsystem.getObservation().positionReferenced());

    elevatorIO.configure(true, true, true, true, 0.4);
    subsystem.periodic();
    assertEquals(1, elevatorIO.homingRequestCount);
    assertTrue(subsystem.getObservation().positionReferenced());
    assertEquals(0.4, subsystem.getObservation().positionMeters());
    assertEquals(0.0, subsystem.getObservation().positionErrorMeters());
  }

  @Test
  void homingIoExceptionPropagatesAfterRecordingIntentWithoutFabricatingReference() {
    RecordingElevatorIO elevatorIO = new RecordingElevatorIO();
    elevatorIO.configure(true, true, false, false, 0.0);
    ElevatorSubsystem subsystem = new ElevatorSubsystem(elevatorIO);
    subsystem.periodic();
    RuntimeException failure = new RuntimeException("homing request failure");
    elevatorIO.homingRequestFailure = failure;

    RuntimeException thrown = assertThrows(RuntimeException.class, subsystem::requestHoming);

    assertSame(failure, thrown);
    assertEquals(1, elevatorIO.homingRequestCount);
    assertEquals(0, elevatorIO.requestCount);
    assertEquals(0, elevatorIO.stopCount);
    assertEquals(ElevatorRequestedState.HOMING, subsystem.getObservation().requestedState());
    assertEquals(0.0, subsystem.getObservation().targetPositionMeters());
    assertEquals(0.0, subsystem.getObservation().positionErrorMeters());
    assertFalse(subsystem.getObservation().positionReferenced());
    assertEquals(0.0, subsystem.getObservation().positionMeters());
  }

  private static void assertRequest(
      ElevatorObservation observation, double targetPositionMeters, double positionErrorMeters) {
    assertEquals(ElevatorRequestedState.POSITION_REQUESTED, observation.requestedState());
    assertEquals(targetPositionMeters, observation.targetPositionMeters());
    assertEquals(positionErrorMeters, observation.positionErrorMeters());
  }

  private static final class RecordingElevatorIO implements ElevatorIO {
    private boolean available;
    private boolean connected;
    private boolean positionValid;
    private boolean positionReferenced;
    private double positionMeters;
    private int updateCount;
    private int requestCount;
    private int homingRequestCount;
    private int stopCount;
    private double lastRequestedTarget;
    private RuntimeException requestFailure;
    private RuntimeException homingRequestFailure;
    private RuntimeException stopFailure;
    private Consumer<ElevatorIOInputs> updateHook;
    private Runnable homingRequestHook;

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
    public void requestPositionMeters(double targetPositionMeters) {
      requestCount++;
      lastRequestedTarget = targetPositionMeters;
      if (requestFailure != null) {
        throw requestFailure;
      }
    }

    @Override
    public void requestHoming() {
      homingRequestCount++;
      if (homingRequestHook != null) {
        homingRequestHook.run();
      }
      if (homingRequestFailure != null) {
        throw homingRequestFailure;
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
