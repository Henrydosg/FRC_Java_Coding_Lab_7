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
  private final DoublePublisher supplyVoltageVoltsPublisher;
  private final DoublePublisher supplyCurrentAmpsPublisher;
  private final DoublePublisher statorCurrentAmpsPublisher;
  private final DoublePublisher temperatureCelsiusPublisher;
  private final DoublePublisher positionRotationsPublisher;
  private final DoublePublisher velocityRpmPublisher;
  private final BooleanPublisher configurationHealthyPublisher;

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
    supplyVoltageVoltsPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakeSupplyVoltageVoltsKey)
            .publish();
    supplyCurrentAmpsPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakeSupplyCurrentAmpsKey)
            .publish();
    statorCurrentAmpsPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakeStatorCurrentAmpsKey)
            .publish();
    temperatureCelsiusPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakeTemperatureCelsiusKey)
            .publish();
    positionRotationsPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakePositionRotationsKey)
            .publish();
    velocityRpmPublisher =
        intakeTable
            .getDoubleTopic(
                TelemetryConstants.kIntakeVelocityRpmKey)
            .publish();
    configurationHealthyPublisher =
        intakeTable
            .getBooleanTopic(
                TelemetryConstants.kIntakeConfigurationHealthyKey)
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
    supplyVoltageVoltsPublisher.set(
        observation.supplyVoltageVolts());
    supplyCurrentAmpsPublisher.set(
        observation.supplyCurrentAmps());
    statorCurrentAmpsPublisher.set(
        observation.statorCurrentAmps());
    temperatureCelsiusPublisher.set(
        observation.temperatureCelsius());
    positionRotationsPublisher.set(
        observation.positionRotations());
    velocityRpmPublisher.set(
        observation.velocityRpm());
    configurationHealthyPublisher.set(
        observation.configurationHealthy());
  }

  /**
   * Closes the publisher handles owned by this facade.
   */
  @Override
  public void close() {
    appliedOutputPublisher.close();
    modePublisher.close();
    connectedPublisher.close();
    supplyVoltageVoltsPublisher.close();
    supplyCurrentAmpsPublisher.close();
    statorCurrentAmpsPublisher.close();
    temperatureCelsiusPublisher.close();
    positionRotationsPublisher.close();
    velocityRpmPublisher.close();
    configurationHealthyPublisher.close();
  }
}
