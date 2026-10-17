// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.elevator;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.elevator.ElevatorObservation;
import java.util.Objects;

/** Publishes immutable Elevator observations without controlling mechanism behavior. */
public final class ElevatorTelemetryFacade implements AutoCloseable {
  private final BooleanPublisher availablePublisher;
  private final BooleanPublisher connectedPublisher;
  private final BooleanPublisher positionValidPublisher;
  private final BooleanPublisher positionReferencedPublisher;
  private final DoublePublisher positionMetersPublisher;
  private final StringPublisher requestedStatePublisher;
  private final DoublePublisher targetPositionMetersPublisher;
  private final DoublePublisher positionErrorMetersPublisher;

  /** Creates stable typed publishers below the supplied Elevator table. */
  public ElevatorTelemetryFacade(NetworkTable elevatorTable) {
    NetworkTable requiredTable = Objects.requireNonNull(elevatorTable, "elevatorTable");
    availablePublisher = requiredTable.getBooleanTopic("Available").publish();
    connectedPublisher = requiredTable.getBooleanTopic("Connected").publish();
    positionValidPublisher = requiredTable.getBooleanTopic("PositionValid").publish();
    positionReferencedPublisher = requiredTable.getBooleanTopic("PositionReferenced").publish();
    positionMetersPublisher = requiredTable.getDoubleTopic("PositionMeters").publish();
    requestedStatePublisher = requiredTable.getStringTopic("RequestedState").publish();
    targetPositionMetersPublisher =
        requiredTable.getDoubleTopic("TargetPositionMeters").publish();
    positionErrorMetersPublisher = requiredTable.getDoubleTopic("PositionErrorMeters").publish();
  }

  /** Publishes exactly one immutable Elevator observation. */
  public void publish(ElevatorObservation observation) {
    ElevatorObservation requiredObservation = Objects.requireNonNull(observation, "observation");
    availablePublisher.set(requiredObservation.available());
    connectedPublisher.set(requiredObservation.connected());
    positionValidPublisher.set(requiredObservation.positionValid());
    positionReferencedPublisher.set(requiredObservation.positionReferenced());
    positionMetersPublisher.set(requiredObservation.positionMeters());
    requestedStatePublisher.set(requiredObservation.requestedState().name());
    targetPositionMetersPublisher.set(requiredObservation.targetPositionMeters());
    positionErrorMetersPublisher.set(requiredObservation.positionErrorMeters());
  }

  @Override
  public void close() {
    availablePublisher.close();
    connectedPublisher.close();
    positionValidPublisher.close();
    positionReferencedPublisher.close();
    positionMetersPublisher.close();
    requestedStatePublisher.close();
    targetPositionMetersPublisher.close();
    positionErrorMetersPublisher.close();
  }
}
