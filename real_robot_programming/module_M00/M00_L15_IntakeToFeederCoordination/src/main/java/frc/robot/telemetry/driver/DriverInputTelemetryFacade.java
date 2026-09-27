// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.driver;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import frc.robot.observation.DriverInputObservation;
import java.util.Objects;

/** Publishes immutable driver-input observations without controlling robot behavior. */
public final class DriverInputTelemetryFacade implements AutoCloseable {
  private final DoublePublisher rawLeftYPublisher;
  private final DoublePublisher rawLeftXPublisher;
  private final DoublePublisher rawRightXPublisher;
  private final DoublePublisher semanticRawForwardPublisher;
  private final DoublePublisher semanticRawStrafePublisher;
  private final DoublePublisher semanticRawRotationPublisher;
  private final DoublePublisher processedForwardPublisher;
  private final DoublePublisher processedStrafePublisher;
  private final DoublePublisher processedRotationPublisher;

  /**
   * Creates typed publishers under the DriverInput table.
   *
   * @param driverInputTable DriverInput telemetry root table
   */
  public DriverInputTelemetryFacade(NetworkTable driverInputTable) {
    Objects.requireNonNull(driverInputTable, "driverInputTable");

    NetworkTable rawTable = driverInputTable.getSubTable("Raw");
    rawLeftYPublisher = rawTable.getDoubleTopic("LeftY").publish();
    rawLeftXPublisher = rawTable.getDoubleTopic("LeftX").publish();
    rawRightXPublisher = rawTable.getDoubleTopic("RightX").publish();

    NetworkTable semanticRawTable = driverInputTable.getSubTable("SemanticRaw");
    semanticRawForwardPublisher = semanticRawTable.getDoubleTopic("Forward").publish();
    semanticRawStrafePublisher = semanticRawTable.getDoubleTopic("Strafe").publish();
    semanticRawRotationPublisher = semanticRawTable.getDoubleTopic("Rotation").publish();

    NetworkTable processedTable = driverInputTable.getSubTable("Processed");
    processedForwardPublisher = processedTable.getDoubleTopic("Forward").publish();
    processedStrafePublisher = processedTable.getDoubleTopic("Strafe").publish();
    processedRotationPublisher = processedTable.getDoubleTopic("Rotation").publish();
  }

  /**
   * Publishes one complete immutable driver-input observation.
   *
   * @param observation driver-input observation to publish
   */
  public void publish(DriverInputObservation observation) {
    Objects.requireNonNull(observation, "observation");

    rawLeftYPublisher.set(observation.rawLeftY());
    rawLeftXPublisher.set(observation.rawLeftX());
    rawRightXPublisher.set(observation.rawRightX());
    semanticRawForwardPublisher.set(observation.semanticRawForward());
    semanticRawStrafePublisher.set(observation.semanticRawStrafe());
    semanticRawRotationPublisher.set(observation.semanticRawRotation());
    processedForwardPublisher.set(observation.processedForward());
    processedStrafePublisher.set(observation.processedStrafe());
    processedRotationPublisher.set(observation.processedRotation());
  }

  /** Closes every publisher handle owned by this facade. */
  @Override
  public void close() {
    rawLeftYPublisher.close();
    rawLeftXPublisher.close();
    rawRightXPublisher.close();
    semanticRawForwardPublisher.close();
    semanticRawStrafePublisher.close();
    semanticRawRotationPublisher.close();
    processedForwardPublisher.close();
    processedStrafePublisher.close();
    processedRotationPublisher.close();
  }
}
