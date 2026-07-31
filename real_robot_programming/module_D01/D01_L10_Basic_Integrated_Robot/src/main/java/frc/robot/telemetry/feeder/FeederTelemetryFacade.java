// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.feeder;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.Constants.TelemetryConstants;
import frc.robot.observation.feeder.FeederObservation;
import java.util.Objects;

/**
 * Publishes immutable feeder observations.
 */
public final class FeederTelemetryFacade implements AutoCloseable {
  private final DoublePublisher appliedOutputPublisher;
  private final DoublePublisher positionRotationsPublisher;
  private final DoublePublisher velocityRpmPublisher;
  private final DoublePublisher supplyCurrentAmpsPublisher;
  private final DoublePublisher statorCurrentAmpsPublisher;
  private final DoublePublisher temperatureCelsiusPublisher;
  private final BooleanPublisher connectedPublisher;
  private final BooleanPublisher configurationHealthyPublisher;
  private final StringPublisher modePublisher;

  /**
   * Creates publishers for the supplied feeder table.
   *
   * @param feederTable feeder telemetry table
   */
  public FeederTelemetryFacade(NetworkTable feederTable) {
    Objects.requireNonNull(
        feederTable,
        "feederTable");

    appliedOutputPublisher =
        feederTable
            .getDoubleTopic(
                TelemetryConstants.kFeederAppliedOutputKey)
            .publish();
    positionRotationsPublisher =
        feederTable
            .getDoubleTopic(
                TelemetryConstants.kFeederPositionRotationsKey)
            .publish();
    velocityRpmPublisher =
        feederTable
            .getDoubleTopic(
                TelemetryConstants.kFeederVelocityRpmKey)
            .publish();
    supplyCurrentAmpsPublisher =
        feederTable
            .getDoubleTopic(
                TelemetryConstants.kFeederSupplyCurrentAmpsKey)
            .publish();
    statorCurrentAmpsPublisher =
        feederTable
            .getDoubleTopic(
                TelemetryConstants.kFeederStatorCurrentAmpsKey)
            .publish();
    temperatureCelsiusPublisher =
        feederTable
            .getDoubleTopic(
                TelemetryConstants.kFeederTemperatureCelsiusKey)
            .publish();
    connectedPublisher =
        feederTable
            .getBooleanTopic(
                TelemetryConstants.kFeederConnectedKey)
            .publish();
    configurationHealthyPublisher =
        feederTable
            .getBooleanTopic(
                TelemetryConstants.kFeederConfigurationHealthyKey)
            .publish();
    modePublisher =
        feederTable
            .getStringTopic(
                TelemetryConstants.kFeederModeKey)
            .publish();
  }

  /**
   * Publishes the supplied feeder observation.
   *
   * @param observation immutable feeder observation
   */
  public void publish(FeederObservation observation) {
    Objects.requireNonNull(
        observation,
        "observation");

    appliedOutputPublisher.set(
        observation.appliedOutput());
    positionRotationsPublisher.set(
        observation.positionRotations());
    velocityRpmPublisher.set(
        observation.velocityRpm());
    supplyCurrentAmpsPublisher.set(
        observation.supplyCurrentAmps());
    statorCurrentAmpsPublisher.set(
        observation.statorCurrentAmps());
    temperatureCelsiusPublisher.set(
        observation.temperatureCelsius());
    connectedPublisher.set(
        observation.connected());
    configurationHealthyPublisher.set(
        observation.configurationHealthy());
    modePublisher.set(
        observation.mode().name());
  }

  /**
   * Closes the publisher handles owned by this facade.
   */
  @Override
  public void close() {
    appliedOutputPublisher.close();
    positionRotationsPublisher.close();
    velocityRpmPublisher.close();
    supplyCurrentAmpsPublisher.close();
    statorCurrentAmpsPublisher.close();
    temperatureCelsiusPublisher.close();
    connectedPublisher.close();
    configurationHealthyPublisher.close();
    modePublisher.close();
  }
}
