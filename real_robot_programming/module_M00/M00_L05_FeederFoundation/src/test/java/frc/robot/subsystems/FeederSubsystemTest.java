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
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.observation.feeder.FeederObservation;
import frc.robot.observation.feeder.FeederObservation.RequestedState;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/** Verifies Feeder state ownership, semantic forwarding, and immutable snapshots. */
class FeederSubsystemTest {
  @AfterEach
  void unregisterTestSubsystems() {
    CommandScheduler.getInstance().unregisterAllSubsystems();
  }

  @Test
  void rejectsNullIoAndBeginsStoppedWithoutOutputRequests() {
    assertThrows(NullPointerException.class, () -> new FeederSubsystem(null));

    RecordingFeederIO feederIO = new RecordingFeederIO();
    FeederSubsystem subsystem = new FeederSubsystem(feederIO);

    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(0, feederIO.requestCount);
    assertEquals(0, feederIO.stopCount);
  }

  @Test
  void requestFeedUpdatesIntentBeforeForwardingExactlyOneSemanticRequest() {
    RecordingFeederIO feederIO = new RecordingFeederIO();
    FeederSubsystem subsystem = new FeederSubsystem(feederIO);
    feederIO.observationSupplier = subsystem::getObservation;

    subsystem.requestFeed();
    subsystem.requestFeed();

    assertEquals(2, feederIO.requestCount);
    assertEquals(RequestedState.FEED_REQUESTED, feederIO.stateObservedOnRequest);
    assertEquals(RequestedState.FEED_REQUESTED, subsystem.getObservation().requestedState());
  }

  @Test
  void periodicRefreshesInputsAndPreservesIntentWithoutIssuingOutputs() {
    RecordingFeederIO feederIO = new RecordingFeederIO();
    feederIO.available = true;
    feederIO.connected = true;
    FeederSubsystem subsystem = new FeederSubsystem(feederIO);
    subsystem.requestFeed();
    int requestsBeforePeriodic = feederIO.requestCount;
    int stopsBeforePeriodic = feederIO.stopCount;

    subsystem.periodic();
    FeederObservation first = subsystem.getObservation();

    assertTrue(first.available());
    assertTrue(first.connected());
    assertEquals(RequestedState.FEED_REQUESTED, first.requestedState());
    assertEquals(requestsBeforePeriodic, feederIO.requestCount);
    assertEquals(stopsBeforePeriodic, feederIO.stopCount);

    feederIO.available = false;
    feederIO.connected = false;
    subsystem.periodic();
    FeederObservation second = subsystem.getObservation();

    assertNotSame(first, second);
    assertTrue(first.available());
    assertTrue(first.connected());
    assertFalse(second.available());
    assertFalse(second.connected());
    assertEquals(RequestedState.FEED_REQUESTED, second.requestedState());
  }

  @Test
  void stopRecordsStoppedIntentBeforeForwardingAndKeepsItWhenIoFails() {
    RecordingFeederIO feederIO = new RecordingFeederIO();
    FeederSubsystem subsystem = new FeederSubsystem(feederIO);
    feederIO.observationSupplier = subsystem::getObservation;
    subsystem.requestFeed();
    feederIO.throwOnStop = true;

    assertThrows(IllegalStateException.class, subsystem::stop);

    assertEquals(1, feederIO.stopCount);
    assertEquals(RequestedState.STOPPED, feederIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  private static final class RecordingFeederIO implements FeederIO {
    private boolean available;
    private boolean connected;
    private boolean throwOnStop;
    private int requestCount;
    private int stopCount;
    private Supplier<FeederObservation> observationSupplier;
    private RequestedState stateObservedOnRequest;
    private RequestedState stateObservedOnStop;

    @Override
    public void updateInputs(FeederIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
    }

    @Override
    public void requestFeed() {
      requestCount++;
      if (observationSupplier != null) {
        stateObservedOnRequest = observationSupplier.get().requestedState();
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
