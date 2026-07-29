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
import frc.robot.Constants.TelemetryConstants;
import frc.robot.observation.intake.IntakeObservation;
import java.util.Objects;

/**
 * Publishes immutable intake observations.
 */
public final class IntakeTelemetryFacade implements AutoCloseable {
  private final DoublePublisher appliedOutputPublisher;
  private final StringPublisher modePublisher;
  private final BooleanPublisher connectedPublisher;

  /**
   * Creates publishers for the supplied intake table.
   *
   * @param intakeTable intake telemetry table
   */
  public IntakeTelemetryFacade(NetworkTable intakeTable) {
    Objects.requireNonNull(
        intakeTable,
        "intakeTable");

    appliedOutputPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakeAppliedOutputKey)
            .publish();
    modePublisher =
        intakeTable
            .getStringTopic(
                TelemetryConstants.kIntakeModeKey)
            .publish();
    connectedPublisher =
        intakeTable
            .getBooleanTopic(
                TelemetryConstants.kIntakeConnectedKey)
            .publish();
  }

  /**
   * Publishes the supplied intake observation.
   *
   * @param observation immutable intake observation
   */
  public void publish(IntakeObservation observation) {
    Objects.requireNonNull(
        observation,
        "observation");

    appliedOutputPublisher.set(
        observation.appliedOutput());
    modePublisher.set(
        observation.mode().name());
    connectedPublisher.set(
        observation.connected());
  }

  /**
   * Closes the publisher handles owned by this facade.
   */
  @Override
  public void close() {
    appliedOutputPublisher.close();
    modePublisher.close();
    connectedPublisher.close();
  }
}
