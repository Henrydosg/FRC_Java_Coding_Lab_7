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
import frc.robot.observation.elevator.ElevatorObservation;
import java.util.Objects;

/** Publishes immutable Elevator observations without controlling mechanism behavior. */
public final class ElevatorTelemetryFacade implements AutoCloseable {
  private final BooleanPublisher availablePublisher;
  private final BooleanPublisher connectedPublisher;
  private final BooleanPublisher positionValidPublisher;
  private final BooleanPublisher positionReferencedPublisher;
  private final DoublePublisher positionMetersPublisher;

  /** Creates stable typed publishers below the supplied Elevator table. */
  public ElevatorTelemetryFacade(NetworkTable elevatorTable) {
    NetworkTable requiredTable = Objects.requireNonNull(elevatorTable, "elevatorTable");
    availablePublisher = requiredTable.getBooleanTopic("Available").publish();
    connectedPublisher = requiredTable.getBooleanTopic("Connected").publish();
    positionValidPublisher = requiredTable.getBooleanTopic("PositionValid").publish();
    positionReferencedPublisher = requiredTable.getBooleanTopic("PositionReferenced").publish();
    positionMetersPublisher = requiredTable.getDoubleTopic("PositionMeters").publish();
  }

  /** Publishes exactly one immutable Elevator observation. */
  public void publish(ElevatorObservation observation) {
    ElevatorObservation requiredObservation = Objects.requireNonNull(observation, "observation");
    availablePublisher.set(requiredObservation.available());
    connectedPublisher.set(requiredObservation.connected());
    positionValidPublisher.set(requiredObservation.positionValid());
    positionReferencedPublisher.set(requiredObservation.positionReferenced());
    positionMetersPublisher.set(requiredObservation.positionMeters());
  }

  @Override
  public void close() {
    availablePublisher.close();
    connectedPublisher.close();
    positionValidPublisher.close();
    positionReferencedPublisher.close();
    positionMetersPublisher.close();
  }
}
