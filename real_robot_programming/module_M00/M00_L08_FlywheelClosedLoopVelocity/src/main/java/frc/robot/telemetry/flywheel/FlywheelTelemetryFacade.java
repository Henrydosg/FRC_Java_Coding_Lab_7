// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.flywheel;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.flywheel.FlywheelObservation;
import java.util.Objects;

/** Publishes immutable Flywheel software state without controlling mechanism behavior. */
public final class FlywheelTelemetryFacade implements AutoCloseable {
  private final BooleanPublisher availablePublisher;
  private final BooleanPublisher connectedPublisher;
  private final BooleanPublisher velocityValidPublisher;
  private final DoublePublisher velocityRpmPublisher;
  private final StringPublisher requestedStatePublisher;

  /** Creates stable typed publishers below the supplied Flywheel table. */
  public FlywheelTelemetryFacade(NetworkTable flywheelTable) {
    NetworkTable requiredTable = Objects.requireNonNull(flywheelTable, "flywheelTable");
    availablePublisher = requiredTable.getBooleanTopic("Available").publish();
    connectedPublisher = requiredTable.getBooleanTopic("Connected").publish();
    velocityValidPublisher = requiredTable.getBooleanTopic("VelocityValid").publish();
    velocityRpmPublisher = requiredTable.getDoubleTopic("VelocityRpm").publish();
    requestedStatePublisher = requiredTable.getStringTopic("RequestedState").publish();
  }

  /** Publishes exactly one immutable Flywheel observation. */
  public void publish(FlywheelObservation observation) {
    FlywheelObservation requiredObservation =
        Objects.requireNonNull(observation, "observation");
    availablePublisher.set(requiredObservation.available());
    connectedPublisher.set(requiredObservation.connected());
    velocityValidPublisher.set(requiredObservation.velocityValid());
    velocityRpmPublisher.set(requiredObservation.velocityRpm());
    requestedStatePublisher.set(requiredObservation.requestedState().name());
  }

  @Override
  public void close() {
    availablePublisher.close();
    connectedPublisher.close();
    velocityValidPublisher.close();
    velocityRpmPublisher.close();
    requestedStatePublisher.close();
  }
}
