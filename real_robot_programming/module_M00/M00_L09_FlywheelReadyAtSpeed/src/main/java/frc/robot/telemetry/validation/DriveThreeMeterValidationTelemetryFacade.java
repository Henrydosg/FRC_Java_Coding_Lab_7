// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.validation;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.DriveThreeMeterValidationObservation;
import java.util.Objects;

/** Publishes the L23 three-meter validation observation for Glass/NT4. */
public final class DriveThreeMeterValidationTelemetryFacade
    implements DriveThreeMeterValidationTelemetry, AutoCloseable {
  private final DoublePublisher targetMetersPublisher;
  private final DoublePublisher measuredMetersPublisher;
  private final DoublePublisher frontLeftDeltaMetersPublisher;
  private final DoublePublisher frontRightDeltaMetersPublisher;
  private final DoublePublisher backLeftDeltaMetersPublisher;
  private final DoublePublisher backRightDeltaMetersPublisher;
  private final BooleanPublisher runningPublisher;
  private final BooleanPublisher completePublisher;
  private final StringPublisher faultOrAbortReasonPublisher;

  /** Creates typed publishers under the supplied validation table. */
  public DriveThreeMeterValidationTelemetryFacade(NetworkTable validationTable) {
    NetworkTable acceptedTable = Objects.requireNonNull(validationTable, "validationTable");
    targetMetersPublisher = acceptedTable.getDoubleTopic("TargetMeters").publish();
    measuredMetersPublisher = acceptedTable.getDoubleTopic("MeasuredMeters").publish();
    frontLeftDeltaMetersPublisher = acceptedTable.getDoubleTopic("FLDeltaMeters").publish();
    frontRightDeltaMetersPublisher = acceptedTable.getDoubleTopic("FRDeltaMeters").publish();
    backLeftDeltaMetersPublisher = acceptedTable.getDoubleTopic("BLDeltaMeters").publish();
    backRightDeltaMetersPublisher = acceptedTable.getDoubleTopic("BRDeltaMeters").publish();
    runningPublisher = acceptedTable.getBooleanTopic("Running").publish();
    completePublisher = acceptedTable.getBooleanTopic("Complete").publish();
    faultOrAbortReasonPublisher = acceptedTable.getStringTopic("FaultAbortReason").publish();
  }

  @Override
  public void publish(DriveThreeMeterValidationObservation observation) {
    DriveThreeMeterValidationObservation acceptedObservation =
        Objects.requireNonNull(observation, "observation");
    targetMetersPublisher.set(acceptedObservation.targetMeters());
    measuredMetersPublisher.set(acceptedObservation.measuredMeters());
    frontLeftDeltaMetersPublisher.set(acceptedObservation.frontLeftDeltaMeters());
    frontRightDeltaMetersPublisher.set(acceptedObservation.frontRightDeltaMeters());
    backLeftDeltaMetersPublisher.set(acceptedObservation.backLeftDeltaMeters());
    backRightDeltaMetersPublisher.set(acceptedObservation.backRightDeltaMeters());
    runningPublisher.set(acceptedObservation.running());
    completePublisher.set(acceptedObservation.complete());
    faultOrAbortReasonPublisher.set(acceptedObservation.faultOrAbortReason());
  }

  @Override
  public void close() {
    targetMetersPublisher.close();
    measuredMetersPublisher.close();
    frontLeftDeltaMetersPublisher.close();
    frontRightDeltaMetersPublisher.close();
    backLeftDeltaMetersPublisher.close();
    backRightDeltaMetersPublisher.close();
    runningPublisher.close();
    completePublisher.close();
    faultOrAbortReasonPublisher.close();
  }
}
