// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.intake;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.intake.IntakeObservation;
import java.util.Objects;

/**
 * Publishes immutable Intake software state and measured feedback without controlling mechanism
 * behavior. Invalid measured values are published as NaN together with MeasurementValid false.
 */
public final class IntakeTelemetryFacade implements AutoCloseable {
  private final BooleanPublisher availablePublisher;
  private final BooleanPublisher connectedPublisher;
  private final StringPublisher requestedStatePublisher;
  private final BooleanPublisher faultLatchedPublisher;
  private final BooleanPublisher measurementValidPublisher;
  private final DoublePublisher positionRotationsPublisher;
  private final DoublePublisher velocityRotationsPerSecondPublisher;
  private final DoublePublisher statorCurrentAmpsPublisher;
  private final DoublePublisher supplyCurrentAmpsPublisher;
  private final DoublePublisher appliedVoltsPublisher;

  /** Creates stable typed publishers below the supplied Intake table. */
  public IntakeTelemetryFacade(NetworkTable intakeTable) {
    NetworkTable requiredTable = Objects.requireNonNull(intakeTable, "intakeTable");
    availablePublisher = requiredTable.getBooleanTopic("Available").publish();
    connectedPublisher = requiredTable.getBooleanTopic("Connected").publish();
    requestedStatePublisher = requiredTable.getStringTopic("RequestedState").publish();
    faultLatchedPublisher = requiredTable.getBooleanTopic("FaultLatched").publish();
    measurementValidPublisher = requiredTable.getBooleanTopic("MeasurementValid").publish();
    positionRotationsPublisher = requiredTable.getDoubleTopic("PositionRotations").publish();
    velocityRotationsPerSecondPublisher =
        requiredTable.getDoubleTopic("VelocityRotationsPerSecond").publish();
    statorCurrentAmpsPublisher = requiredTable.getDoubleTopic("StatorCurrentAmps").publish();
    supplyCurrentAmpsPublisher = requiredTable.getDoubleTopic("SupplyCurrentAmps").publish();
    appliedVoltsPublisher = requiredTable.getDoubleTopic("AppliedVolts").publish();
  }

  /** Publishes exactly one immutable Intake observation. */
  public void publish(IntakeObservation observation) {
    IntakeObservation requiredObservation =
        Objects.requireNonNull(observation, "observation");
    availablePublisher.set(requiredObservation.available());
    connectedPublisher.set(requiredObservation.connected());
    requestedStatePublisher.set(requiredObservation.requestedState().name());
    faultLatchedPublisher.set(requiredObservation.faultLatched());
    measurementValidPublisher.set(requiredObservation.measurementValid());
    positionRotationsPublisher.set(requiredObservation.motorPositionRotations());
    velocityRotationsPerSecondPublisher.set(
        requiredObservation.motorVelocityRotationsPerSecond());
    statorCurrentAmpsPublisher.set(requiredObservation.statorCurrentAmps());
    supplyCurrentAmpsPublisher.set(requiredObservation.supplyCurrentAmps());
    appliedVoltsPublisher.set(requiredObservation.appliedVolts());
  }

  @Override
  public void close() {
    availablePublisher.close();
    connectedPublisher.close();
    requestedStatePublisher.close();
    faultLatchedPublisher.close();
    measurementValidPublisher.close();
    positionRotationsPublisher.close();
    velocityRotationsPerSecondPublisher.close();
    statorCurrentAmpsPublisher.close();
    supplyCurrentAmpsPublisher.close();
    appliedVoltsPublisher.close();
  }
}
