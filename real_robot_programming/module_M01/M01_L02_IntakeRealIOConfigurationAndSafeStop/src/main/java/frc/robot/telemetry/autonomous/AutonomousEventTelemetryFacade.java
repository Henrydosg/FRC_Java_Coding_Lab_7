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

/** Publishes immutable event observations without scheduling or controlling commands. */
public final class AutonomousEventTelemetryFacade implements AutoCloseable {
  private final StringPublisher lastEventPublisher;
  private final StringPublisher statePublisher;
  private final BooleanPublisher activePublisher;
  private final DoublePublisher dispatchCountPublisher;

  private long dispatchCount;

  /** Creates stable typed publishers below the supplied table. */
  public AutonomousEventTelemetryFacade(NetworkTable table) {
    Objects.requireNonNull(table, "table");
    lastEventPublisher = table.getStringTopic("LastEvent").publish();
    statePublisher = table.getStringTopic("State").publish();
    activePublisher = table.getBooleanTopic("Active").publish();
    dispatchCountPublisher = table.getDoubleTopic("DispatchCount").publish();
  }

  /** Publishes one immutable observation and counts only fresh STARTED transitions. */
  public void publish(AutonomousEventObservation observation) {
    AutonomousEventObservation acceptedObservation =
        Objects.requireNonNull(observation, "observation");
    if (acceptedObservation.state()
        == AutonomousEventObservation.LifecycleState.STARTED) {
      dispatchCount++;
    }
    lastEventPublisher.set(acceptedObservation.eventId().pathPlannerName());
    statePublisher.set(acceptedObservation.state().name());
    activePublisher.set(acceptedObservation.active());
    dispatchCountPublisher.set(dispatchCount);
  }

  @Override
  public void close() {
    lastEventPublisher.close();
    statePublisher.close();
    activePublisher.close();
    dispatchCountPublisher.close();
  }
}
