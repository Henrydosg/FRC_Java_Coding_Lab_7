// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.intake;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.intake.IntakeObservation;
import java.util.Objects;

/** Publishes immutable Intake software state without controlling mechanism behavior. */
public final class IntakeTelemetryFacade implements AutoCloseable {
  private final BooleanPublisher availablePublisher;
  private final BooleanPublisher connectedPublisher;
  private final StringPublisher requestedStatePublisher;

  /** Creates stable typed publishers below the supplied Intake table. */
  public IntakeTelemetryFacade(NetworkTable intakeTable) {
    NetworkTable requiredTable = Objects.requireNonNull(intakeTable, "intakeTable");
    availablePublisher = requiredTable.getBooleanTopic("Available").publish();
    connectedPublisher = requiredTable.getBooleanTopic("Connected").publish();
    requestedStatePublisher = requiredTable.getStringTopic("RequestedState").publish();
  }

  /** Publishes exactly one immutable Intake observation. */
  public void publish(IntakeObservation observation) {
    IntakeObservation requiredObservation =
        Objects.requireNonNull(observation, "observation");
    availablePublisher.set(requiredObservation.available());
    connectedPublisher.set(requiredObservation.connected());
    requestedStatePublisher.set(requiredObservation.requestedState().name());
  }

  @Override
  public void close() {
    availablePublisher.close();
    connectedPublisher.close();
    requestedStatePublisher.close();
  }
}
