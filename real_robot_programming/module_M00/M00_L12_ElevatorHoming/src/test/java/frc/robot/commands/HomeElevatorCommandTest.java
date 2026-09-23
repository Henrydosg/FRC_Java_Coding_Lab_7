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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.observation.elevator.ElevatorRequestedState;
import frc.robot.subsystems.ElevatorSubsystem;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Verifies the bounded command lifecycle using an injected deterministic clock. */
class HomeElevatorCommandTest {
  @Test
  void requiresElevatorAndRejectsInvalidTimeouts() {
    RecordingElevatorIO io = new RecordingElevatorIO();
    ElevatorSubsystem elevator = new ElevatorSubsystem(io);

    assertThrows(NullPointerException.class, () -> new HomeElevatorCommand(null, 1.0));
    for (double timeout :
        new double[] {
          Double.NaN,
          Double.POSITIVE_INFINITY,
          Double.NEGATIVE_INFINITY,
          0.0,
          -0.1
        }) {
      assertThrows(IllegalArgumentException.class, () -> new HomeElevatorCommand(elevator, timeout));
    }

    HomeElevatorCommand command = new HomeElevatorCommand(elevator, 1.0, () -> 0.0);
    assertEquals(Set.of(elevator), command.getRequirements());
  }

  @Test
  void alreadyReferencedAtInitializeFinishesWithoutRequestAndStopsOnce() {
    Rig rig = new Rig(true, true, true, true, 0.0, 2.0);

    rig.command.initialize();

    assertEquals(0, rig.io.homingRequestCount);
    assertTrue(rig.command.isFinished());
    rig.command.execute();
    assertEquals(0, rig.io.homingRequestCount);
    assertEquals(0, rig.io.positionRequestCount);
    assertEquals(0, rig.io.stopCount);
    rig.command.end(false);
    assertEquals(1, rig.io.stopCount);
  }

  @Test
  void unavailableOrDisconnectedAtInitializeFinishesWithoutRequestAndStopsOnce() {
    for (boolean[] state : new boolean[][] {{false, false}, {true, false}}) {
      Rig rig = new Rig(state[0], state[1], false, false, 0.0, 2.0);

      rig.command.initialize();

      assertEquals(0, rig.io.homingRequestCount);
      assertTrue(rig.command.isFinished());
      rig.command.end(false);
      assertEquals(1, rig.io.stopCount);
    }
  }

  @Test
  void availableConnectedUnreferencedElevatorGetsOneRequestAndNoExecuteOutputs() {
    Rig rig = new Rig(true, true, false, false, 0.0, 2.0);
    rig.io.homingRequestHook =
        () -> {
          assertEquals(ElevatorRequestedState.HOMING, rig.elevator.getObservation().requestedState());
          assertEquals(0.0, rig.elevator.getObservation().targetPositionMeters());
          assertEquals(0.0, rig.elevator.getObservation().positionErrorMeters());
        };

    rig.command.initialize();
    rig.command.execute();
    rig.command.execute();

    assertEquals(1, rig.io.homingRequestCount);
    assertEquals(0, rig.io.positionRequestCount);
    assertEquals(0, rig.io.stopCount);
    assertEquals(ElevatorRequestedState.HOMING, rig.elevator.getObservation().requestedState());
  }

  @Test
  void finishesOnlyAfterNormalizedReferenceAppearsAndStopsOnce() {
    Rig rig = new Rig(true, true, true, false, 0.0, 2.0);
    rig.command.initialize();

    assertFalse(rig.command.isFinished());
    rig.io.positionReferenced = true;
    rig.elevator.periodic();

    assertTrue(rig.command.isFinished());
    rig.command.end(false);
    assertEquals(1, rig.io.homingRequestCount);
    assertEquals(1, rig.io.stopCount);
    assertEquals(ElevatorRequestedState.STOPPED, rig.elevator.getObservation().requestedState());
  }

  @Test
  void zeroPositionWithoutReferenceDoesNotFinishAndTimeoutStopsOnce() {
    Rig rig = new Rig(true, true, true, false, 0.0, 2.0);
    rig.command.initialize();

    assertFalse(rig.command.isFinished());
    rig.clock.value = 1.999;
    assertFalse(rig.command.isFinished());
    rig.clock.value = 2.0;
    assertTrue(rig.command.isFinished());
    rig.command.end(false);

    assertFalse(rig.elevator.getObservation().positionReferenced());
    assertEquals(0.0, rig.elevator.getObservation().positionMeters());
    assertEquals(1, rig.io.stopCount);
  }

  @Test
  void interruptionStopsOnce() {
    Rig rig = new Rig(true, true, true, false, 0.3, 2.0);
    rig.command.initialize();

    rig.command.end(true);

    assertEquals(1, rig.io.homingRequestCount);
    assertEquals(1, rig.io.stopCount);
  }

  @Test
  void stopFailurePropagatesFromEndWithoutRetry() {
    Rig rig = new Rig(true, true, true, false, 0.3, 2.0);
    rig.command.initialize();
    RuntimeException failure = new RuntimeException("stop failure");
    rig.io.stopFailure = failure;

    RuntimeException thrown =
        assertThrows(RuntimeException.class, () -> rig.command.end(true));

    assertSame(failure, thrown);
    assertEquals(1, rig.io.stopCount);
  }

  @Test
  void reinitializationResetsCommandLocalLifecycleAndTimeout() {
    Rig rig = new Rig(true, true, true, false, 0.3, 2.0);
    rig.command.initialize();
    rig.clock.value = 2.0;
    assertTrue(rig.command.isFinished());
    rig.command.end(true);

    rig.io.positionReferenced = false;
    rig.elevator.periodic();
    rig.clock.value = 10.0;
    rig.command.initialize();

    assertEquals(2, rig.io.homingRequestCount);
    assertFalse(rig.command.isFinished());
    rig.clock.value = 11.999;
    assertFalse(rig.command.isFinished());
    rig.clock.value = 12.0;
    assertTrue(rig.command.isFinished());
    rig.command.end(false);
    assertEquals(2, rig.io.stopCount);
  }

  private static final class Rig {
    private final RecordingElevatorIO io = new RecordingElevatorIO();
    private final ElevatorSubsystem elevator = new ElevatorSubsystem(io);
    private final MutableClock clock = new MutableClock();
    private final HomeElevatorCommand command;

    private Rig(
        boolean available,
        boolean connected,
        boolean positionValid,
        boolean positionReferenced,
        double positionMeters,
        double timeoutSeconds) {
      io.available = available;
      io.connected = connected;
      io.positionValid = positionValid;
      io.positionReferenced = positionReferenced;
      io.positionMeters = positionMeters;
      elevator.periodic();
      command = new HomeElevatorCommand(elevator, timeoutSeconds, clock::get);
    }
  }

  private static final class MutableClock {
    private double value;

    private double get() {
      return value;
    }
  }

  private static final class RecordingElevatorIO implements ElevatorIO {
    private boolean available;
    private boolean connected;
    private boolean positionValid;
    private boolean positionReferenced;
    private double positionMeters;
    private int homingRequestCount;
    private int positionRequestCount;
    private int stopCount;
    private Runnable homingRequestHook;
    private RuntimeException stopFailure;

    @Override
    public void updateInputs(ElevatorIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
      inputs.positionValid = positionValid;
      inputs.positionReferenced = positionReferenced;
      inputs.positionMeters = positionMeters;
    }

    @Override
    public void requestPositionMeters(double targetPositionMeters) {
      positionRequestCount++;
    }

    @Override
    public void requestHoming() {
      homingRequestCount++;
      if (homingRequestHook != null) {
        homingRequestHook.run();
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
