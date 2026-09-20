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
import static org.junit.jupiter.api.Assertions.assertSame;
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
    assertEquals(0, flywheelIO.requestVelocityCount);
    assertEquals(0, flywheelIO.stopCount);
  }

  @Test
  void positiveRequestUpdatesIntentBeforeForwardingExactlyOneRequest() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;

    subsystem.requestVelocity(VALID_VELOCITY_RPM);

    assertEquals(1, flywheelIO.requestVelocityCount);
    assertEquals(VALID_VELOCITY_RPM, flywheelIO.targetObservedOnRequest);
    assertSame(RequestedState.VELOCITY_REQUESTED, flywheelIO.stateObservedOnRequest);
    assertEquals(RequestedState.VELOCITY_REQUESTED, subsystem.getObservation().requestedState());
    assertEquals(0, flywheelIO.stopCount);
  }

  @Test
  void forwardingExceptionPreservesRecordedIntentAndObservation() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;
    flywheelIO.throwOnRequestVelocity = true;
    FlywheelObservation before = subsystem.getObservation();

    IllegalStateException failure =
        assertThrows(
            IllegalStateException.class,
            () -> subsystem.requestVelocity(VALID_VELOCITY_RPM));

    assertEquals("expected request failure", failure.getMessage());
    assertEquals(1, flywheelIO.requestVelocityCount);
    assertEquals(VALID_VELOCITY_RPM, flywheelIO.targetObservedOnRequest);
    assertEquals(RequestedState.VELOCITY_REQUESTED, flywheelIO.stateObservedOnRequest);
    assertEquals(RequestedState.VELOCITY_REQUESTED, subsystem.getObservation().requestedState());
    assertNotSame(before, subsystem.getObservation());
    assertEquals(0, flywheelIO.stopCount);
  }

  @Test
  void zeroRequestUsesCanonicalStopWithoutVelocityForwarding() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;

    subsystem.requestVelocity(0.0);

    assertEquals(0, flywheelIO.requestVelocityCount);
    assertEquals(1, flywheelIO.stopCount);
    assertEquals(RequestedState.STOPPED, flywheelIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  @Test
  void negativeZeroRequestUsesTheCanonicalZeroStopPath() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;

    subsystem.requestVelocity(-0.0);

    assertEquals(0, flywheelIO.requestVelocityCount);
    assertEquals(1, flywheelIO.stopCount);
    assertEquals(RequestedState.STOPPED, flywheelIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  @Test
  void invalidRequestsFailClosedWithoutForwardingInvalidVelocity() {
    for (double invalidTarget :
        new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, -1.0}) {
      RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
      FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
      flywheelIO.observationSupplier = subsystem::getObservation;

      assertThrows(
          IllegalArgumentException.class, () -> subsystem.requestVelocity(invalidTarget));

      assertEquals(0, flywheelIO.requestVelocityCount);
      assertEquals(1, flywheelIO.stopCount);
      assertEquals(RequestedState.STOPPED, flywheelIO.stateObservedOnStop);
      assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    }
  }

  @Test
  void invalidRequestKeepsIllegalArgumentExceptionPrimaryWhenStopFails() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;
    IllegalStateException stopFailure = new IllegalStateException("expected stop failure");
    flywheelIO.stopFailure = stopFailure;

    IllegalArgumentException failure =
        assertThrows(IllegalArgumentException.class, () -> subsystem.requestVelocity(-1.0));

    assertEquals(0, flywheelIO.requestVelocityCount);
    assertEquals(1, flywheelIO.stopCount);
    assertSame(stopFailure, failure.getSuppressed()[0]);
    assertEquals(RequestedState.STOPPED, flywheelIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  @Test
  void explicitStopRecordsStoppedIntentBeforeForwardingAndKeepsItWhenIoFails() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    flywheelIO.observationSupplier = subsystem::getObservation;
    subsystem.requestVelocity(VALID_VELOCITY_RPM);
    IllegalStateException stopFailure = new IllegalStateException("expected stop failure");
    flywheelIO.stopFailure = stopFailure;

    IllegalStateException failure = assertThrows(IllegalStateException.class, subsystem::stop);

    assertSame(stopFailure, failure);
    assertEquals(1, flywheelIO.stopCount);
    assertEquals(RequestedState.STOPPED, flywheelIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  @Test
  void periodicRefreshesInputsPreservesIntentAndIssuesNoOutput() {
    RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO();
    flywheelIO.available = true;
    flywheelIO.connected = true;
    flywheelIO.velocityValid = true;
    flywheelIO.velocityRpm = VALID_VELOCITY_RPM;
    FlywheelSubsystem subsystem = new FlywheelSubsystem(flywheelIO);
    subsystem.requestVelocity(VALID_VELOCITY_RPM);
    int requestsBeforePeriodic = flywheelIO.requestVelocityCount;
    int stopsBeforePeriodic = flywheelIO.stopCount;

    subsystem.periodic();
    FlywheelObservation first = subsystem.getObservation();

    assertTrue(first.available());
    assertTrue(first.connected());
    assertTrue(first.velocityValid());
    assertEquals(VALID_VELOCITY_RPM, first.velocityRpm());
    assertEquals(RequestedState.VELOCITY_REQUESTED, first.requestedState());
    assertEquals(requestsBeforePeriodic, flywheelIO.requestVelocityCount);
    assertEquals(stopsBeforePeriodic, flywheelIO.stopCount);

    flywheelIO.available = false;
    flywheelIO.connected = false;
    flywheelIO.velocityValid = false;
    flywheelIO.velocityRpm = 0.0;
    subsystem.periodic();
    FlywheelObservation second = subsystem.getObservation();

    assertNotSame(first, second);
    assertFalse(second.available());
    assertFalse(second.connected());
    assertFalse(second.velocityValid());
    assertEquals(0.0, second.velocityRpm());
    assertEquals(RequestedState.VELOCITY_REQUESTED, second.requestedState());
    assertEquals(requestsBeforePeriodic, flywheelIO.requestVelocityCount);
    assertEquals(stopsBeforePeriodic, flywheelIO.stopCount);
  }

  private static final class RecordingFlywheelIO implements FlywheelIO {
    private boolean available;
    private boolean connected;
    private boolean velocityValid;
    private double velocityRpm;
    private boolean throwOnRequestVelocity;
    private RuntimeException stopFailure;
    private int requestVelocityCount;
    private int stopCount;
    private double targetObservedOnRequest;
    private Supplier<FlywheelObservation> observationSupplier;
    private RequestedState stateObservedOnRequest;
    private RequestedState stateObservedOnStop;

    @Override
    public void updateInputs(FlywheelIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
      inputs.velocityValid = velocityValid;
      inputs.velocityRpm = velocityRpm;
    }

    @Override
    public void requestVelocity(double targetRpm) {
      requestVelocityCount++;
      targetObservedOnRequest = targetRpm;
      if (observationSupplier != null) {
        stateObservedOnRequest = observationSupplier.get().requestedState();
      }
      if (throwOnRequestVelocity) {
        throw new IllegalStateException("expected request failure");
      }
    }

    @Override
    public void stop() {
      stopCount++;
      if (observationSupplier != null) {
        stateObservedOnStop = observationSupplier.get().requestedState();
      }
      if (stopFailure != null) {
        throw stopFailure;
      }
    }
  }
}
