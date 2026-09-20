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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.observation.flywheel.FlywheelObservation.RequestedState;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Verifies Flywheel state ownership, semantic forwarding, and immutable snapshots. */
class FlywheelSubsystemTest {
  private static final double VALID_VELOCITY_RPM = 4200.0;

  @AfterEach
  void unregisterTestSubsystems() {
    CommandScheduler.getInstance().unregisterAllSubsystems();
  }

  @Test
  void rejectsNullIoAndBeginsStoppedWithoutOutputRequests() {
    assertThrows(NullPointerException.class, () -> new FlywheelSubsystem(null));

    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);

    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertFalse(subsystem.getObservation().velocityValid());
    assertEquals(0.0, subsystem.getObservation().velocityRpm());
    assertEquals(0, flywheelIO.spinRequestCount);
    assertEquals(0, flywheelIO.stopCount);
  }

  @Test
  void requestSpinUpdatesIntentBeforeForwardingExactlyOneSemanticRequest() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;

    subsystem.requestSpin();

    assertEquals(1, flywheelIO.spinRequestCount);
    assertEquals(RequestedState.SPIN_REQUESTED, flywheelIO.stateObservedOnSpinRequest);
    assertEquals(RequestedState.SPIN_REQUESTED, subsystem.getObservation().requestedState());
  }

  @Test
  void requestSpinRecordsIntentAndObservationBeforeTheFirstThrowingIoCall() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;
    flywheelIO.throwOnSpinRequest = true;

    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());

    assertThrows(IllegalStateException.class, subsystem::requestSpin);

    assertEquals(1, flywheelIO.spinRequestCount);
    assertEquals(RequestedState.SPIN_REQUESTED, flywheelIO.stateObservedOnSpinRequest);
    assertEquals(RequestedState.SPIN_REQUESTED, subsystem.getObservation().requestedState());
  }

  @Test
  void periodicRefreshesInputsAndPreservesIntentWithoutIssuingOutputs() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    flywheelIO.available = true;
    flywheelIO.connected = true;
    flywheelIO.velocityValid = true;
    flywheelIO.velocityRpm = VALID_VELOCITY_RPM;
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    subsystem.requestSpin();
    int spinRequestsBeforePeriodic = flywheelIO.spinRequestCount;
    int stopsBeforePeriodic = flywheelIO.stopCount;

    subsystem.periodic();
    FlywheelObservation first = subsystem.getObservation();

    assertTrue(first.available());
    assertTrue(first.connected());
    assertTrue(first.velocityValid());
    assertEquals(VALID_VELOCITY_RPM, first.velocityRpm());
    assertEquals(RequestedState.SPIN_REQUESTED, first.requestedState());
    assertEquals(spinRequestsBeforePeriodic, flywheelIO.spinRequestCount);
    assertEquals(stopsBeforePeriodic, flywheelIO.stopCount);

    flywheelIO.available = false;
    flywheelIO.connected = false;
    flywheelIO.velocityValid = false;
    flywheelIO.velocityRpm = 0.0;
    subsystem.periodic();
    FlywheelObservation second = subsystem.getObservation();

    assertNotSame(first, second);
    assertTrue(first.available());
    assertTrue(first.connected());
    assertTrue(first.velocityValid());
    assertFalse(second.available());
    assertFalse(second.connected());
    assertFalse(second.velocityValid());
    assertEquals(0.0, second.velocityRpm());
    assertEquals(RequestedState.SPIN_REQUESTED, second.requestedState());
  }

  @Test
  void stopRecordsStoppedIntentBeforeForwardingAndKeepsItWhenIoFails() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;
    subsystem.requestSpin();
    flywheelIO.throwOnStop = true;

    assertThrows(IllegalStateException.class, subsystem::stop);

    assertEquals(1, flywheelIO.stopCount);
    assertEquals(RequestedState.STOPPED, flywheelIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  private static final class RecordingFlywheelIO implements FlywheelIO {
    private boolean available;
    private boolean connected;
    private boolean velocityValid;
    private double velocityRpm;
    private boolean throwOnSpinRequest;
    private boolean throwOnStop;
    private int spinRequestCount;
    private int stopCount;
    private Supplier<FlywheelObservation> observationSupplier;
    private RequestedState stateObservedOnSpinRequest;
    private RequestedState stateObservedOnStop;

    @Override
    public void updateInputs(FlywheelIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
      inputs.velocityValid = velocityValid;
      inputs.velocityRpm = velocityRpm;
    }

    @Override
    public void requestSpin() {
      spinRequestCount++;
      if (observationSupplier != null) {
        stateObservedOnSpinRequest = observationSupplier.get().requestedState();
      }
      if (throwOnSpinRequest) {
        throw new IllegalStateException("expected test failure");
      }
    }

    @Override
    public void stop() {
      stopCount++;
      if (observationSupplier != null) {
        stateObservedOnStop = observationSupplier.get().requestedState();
      }
      if (throwOnStop) {
        throw new IllegalStateException("expected test failure");
      }
    }
  }
}
