// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.autonomous;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.AutonomousEventObservation;
import java.util.Objects;

/** Publishes immutable autonomous-event observations for NetworkTables and Glass. */
public final class AutonomousEventTelemetryFacade implements AutoCloseable {
  private final StringPublisher lastEventPublisher;
  private final StringPublisher statePublisher;
  private final BooleanPublisher activePublisher;
  private final DoublePublisher dispatchCountPublisher;
  private double dispatchCount;

  /** Creates typed publishers under the supplied Autonomous/Event table. */
  public AutonomousEventTelemetryFacade(NetworkTable eventTable) {
    NetworkTable acceptedTable = Objects.requireNonNull(eventTable, "eventTable");
    lastEventPublisher = acceptedTable.getStringTopic("LastEvent").publish();
    statePublisher = acceptedTable.getStringTopic("State").publish();
    activePublisher = acceptedTable.getBooleanTopic("Active").publish();
    dispatchCountPublisher = acceptedTable.getDoubleTopic("DispatchCount").publish();
    dispatchCountPublisher.set(0.0);
  }

  /** Publishes one complete immutable event observation. */
  public synchronized void publish(AutonomousEventObservation observation) {
    AutonomousEventObservation acceptedObservation =
        Objects.requireNonNull(observation, "observation");
    if (acceptedObservation.state() == AutonomousEventObservation.LifecycleState.STARTED) {
      dispatchCount += 1.0;
    }
    lastEventPublisher.set(acceptedObservation.eventId().stableName());
    statePublisher.set(acceptedObservation.state().name());
    activePublisher.set(acceptedObservation.active());
    dispatchCountPublisher.set(dispatchCount);
  }

  /** Closes every publisher owned by this facade. */
  @Override
  public void close() {
    lastEventPublisher.close();
    statePublisher.close();
    activePublisher.close();
    dispatchCountPublisher.close();
  }
}
