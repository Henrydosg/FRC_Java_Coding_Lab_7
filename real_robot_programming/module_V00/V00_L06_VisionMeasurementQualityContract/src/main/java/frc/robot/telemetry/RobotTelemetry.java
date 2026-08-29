// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry;

import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.autonomous.AutonomousPreparationObservation;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.autonomous.AutonomousEventTelemetryFacade;
import frc.robot.telemetry.autonomous.AutonomousPreparationTelemetryFacade;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Coordinates read-only Swerve telemetry publication.
 */
public final class RobotTelemetry {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveTelemetryFacade swerveTelemetryFacade;
  private final Optional<Supplier<AutonomousPreparationObservation>>
      autonomousPreparationObservationSupplier;
  private final Optional<AutonomousPreparationTelemetryFacade>
      autonomousPreparationTelemetryFacade;
  private final Optional<Supplier<Optional<AutonomousEventObservation>>>
      autonomousEventObservationSupplier;
  private final Optional<AutonomousEventTelemetryFacade> autonomousEventTelemetryFacade;

  /**
   * Creates the runtime telemetry coordinator.
   *
   * @param swerveSubsystem Swerve observation source
   * @param swerveTelemetryFacade Swerve telemetry publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade) {
    this(swerveSubsystem, swerveTelemetryFacade, null, null, null, null);
  }

  /**
   * Creates the runtime telemetry coordinator with autonomous-preparation diagnostics.
   *
   * @param swerveSubsystem Swerve observation source
   * @param swerveTelemetryFacade Swerve telemetry publisher
   * @param autonomousPreparationObservationSupplier preparation observation source
   * @param autonomousPreparationTelemetryFacade preparation telemetry publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      Supplier<AutonomousPreparationObservation> autonomousPreparationObservationSupplier,
      AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade) {
    this(
        swerveSubsystem,
        swerveTelemetryFacade,
        autonomousPreparationObservationSupplier,
        autonomousPreparationTelemetryFacade,
        null,
        null);
  }

  /**
   * Creates the runtime telemetry coordinator with preparation and event observations.
   *
   * @param autonomousEventObservationSupplier optional event observation source; empty before the
   *     first event
   * @param autonomousEventTelemetryFacade optional event telemetry publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      Supplier<AutonomousPreparationObservation> autonomousPreparationObservationSupplier,
      AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade,
      Supplier<Optional<AutonomousEventObservation>> autonomousEventObservationSupplier,
      AutonomousEventTelemetryFacade autonomousEventTelemetryFacade) {
    this.swerveSubsystem =
        Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.swerveTelemetryFacade =
        Objects.requireNonNull(
            swerveTelemetryFacade,
            "swerveTelemetryFacade");
    if ((autonomousPreparationObservationSupplier == null)
        != (autonomousPreparationTelemetryFacade == null)) {
      throw new IllegalArgumentException(
          "preparation observation source and facade must be supplied together");
    }
    this.autonomousPreparationObservationSupplier =
        Optional.ofNullable(autonomousPreparationObservationSupplier);
    this.autonomousPreparationTelemetryFacade =
        Optional.ofNullable(autonomousPreparationTelemetryFacade);
    if ((autonomousEventObservationSupplier == null)
        != (autonomousEventTelemetryFacade == null)) {
      throw new IllegalArgumentException(
          "event observation source and facade must be supplied together");
    }
    this.autonomousEventObservationSupplier =
        Optional.ofNullable(autonomousEventObservationSupplier);
    this.autonomousEventTelemetryFacade = Optional.ofNullable(autonomousEventTelemetryFacade);
  }

  /**
   * Publishes the latest complete observation when one exists.
   */
  public void periodic() {
    swerveSubsystem
        .getObservation()
        .ifPresent(swerveTelemetryFacade::publish);
    if (autonomousPreparationObservationSupplier.isPresent()) {
      AutonomousPreparationObservation observation =
          Objects.requireNonNull(
              autonomousPreparationObservationSupplier.orElseThrow().get(),
              "autonomous preparation observation");
      autonomousPreparationTelemetryFacade.orElseThrow().publish(observation);
    }
    if (autonomousEventObservationSupplier.isPresent()) {
      Optional<AutonomousEventObservation> observation =
          Objects.requireNonNull(
              autonomousEventObservationSupplier.orElseThrow().get(),
              "autonomous event observation");
      observation.ifPresent(autonomousEventTelemetryFacade.orElseThrow()::publish);
    }
  }
}
