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
import frc.robot.Constants.TelemetryConstants;
import frc.robot.observation.flywheel.FlywheelObservation;
import java.util.Objects;

/**
 * Publishes immutable flywheel observations.
 */
public final class FlywheelTelemetryFacade implements AutoCloseable {
  private final DoublePublisher appliedOutputPublisher;
  private final DoublePublisher velocityRpmPublisher;
  private final DoublePublisher supplyCurrentAmpsPublisher;
  private final DoublePublisher statorCurrentAmpsPublisher;
  private final DoublePublisher temperatureCelsiusPublisher;
  private final BooleanPublisher connectedPublisher;
  private final BooleanPublisher configurationHealthyPublisher;
  private final StringPublisher modePublisher;

  /**
   * Creates publishers for the supplied flywheel table.
   *
   * @param flywheelTable flywheel telemetry table
   */
  public FlywheelTelemetryFacade(NetworkTable flywheelTable) {
    Objects.requireNonNull(
        flywheelTable,
        "flywheelTable");

    appliedOutputPublisher =
        flywheelTable
            .getDoubleTopic(
                TelemetryConstants.kFlywheelAppliedOutputKey)
            .publish();
    velocityRpmPublisher =
        flywheelTable
            .getDoubleTopic(
                TelemetryConstants.kFlywheelVelocityRpmKey)
            .publish();
    supplyCurrentAmpsPublisher =
        flywheelTable
            .getDoubleTopic(
                TelemetryConstants.kFlywheelSupplyCurrentAmpsKey)
            .publish();
    statorCurrentAmpsPublisher =
        flywheelTable
            .getDoubleTopic(
                TelemetryConstants.kFlywheelStatorCurrentAmpsKey)
            .publish();
    temperatureCelsiusPublisher =
        flywheelTable
            .getDoubleTopic(
                TelemetryConstants.kFlywheelTemperatureCelsiusKey)
            .publish();
    connectedPublisher =
        flywheelTable
            .getBooleanTopic(
                TelemetryConstants.kFlywheelConnectedKey)
            .publish();
    configurationHealthyPublisher =
        flywheelTable
            .getBooleanTopic(
                TelemetryConstants.kFlywheelConfigurationHealthyKey)
            .publish();
    modePublisher =
        flywheelTable
            .getStringTopic(
                TelemetryConstants.kFlywheelModeKey)
            .publish();
  }

  /**
   * Publishes the supplied flywheel observation.
   *
   * @param observation immutable flywheel observation
   */
  public void publish(FlywheelObservation observation) {
    Objects.requireNonNull(
        observation,
        "observation");

    appliedOutputPublisher.set(
        observation.appliedOutput());
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
    velocityRpmPublisher.close();
    supplyCurrentAmpsPublisher.close();
    statorCurrentAmpsPublisher.close();
    temperatureCelsiusPublisher.close();
    connectedPublisher.close();
    configurationHealthyPublisher.close();
    modePublisher.close();
  }
}
