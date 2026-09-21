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
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.telemetry.autonomous.AutonomousEventTelemetryFacade;
import frc.robot.telemetry.autonomous.AutonomousPreparationTelemetryFacade;
import frc.robot.telemetry.feeder.FeederTelemetryFacade;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;
import frc.robot.telemetry.intake.IntakeTelemetryFacade;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;
import frc.robot.telemetry.vision.VisionTelemetryFacade;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/** Coordinates read-only robot telemetry publication from immutable observations. */
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
  private final Optional<VisionSubsystem> visionSubsystem;
  private final Optional<VisionTelemetryFacade> visionTelemetryFacade;
  private final Optional<IntakeSubsystem> intakeSubsystem;
  private final Optional<IntakeTelemetryFacade> intakeTelemetryFacade;
  private final Optional<FeederSubsystem> feederSubsystem;
  private final Optional<FeederTelemetryFacade> feederTelemetryFacade;
  private final Optional<FlywheelSubsystem> flywheelSubsystem;
  private final Optional<FlywheelTelemetryFacade> flywheelTelemetryFacade;

  /**
   * Creates the runtime telemetry coordinator.
   *
   * @param swerveSubsystem Swerve observation source
   * @param swerveTelemetryFacade Swerve telemetry publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade) {
    this(swerveSubsystem, swerveTelemetryFacade, null, null, null, null, null, null);
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
        null,
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
    this(
        swerveSubsystem,
        swerveTelemetryFacade,
        autonomousPreparationObservationSupplier,
        autonomousPreparationTelemetryFacade,
        autonomousEventObservationSupplier,
        autonomousEventTelemetryFacade,
        null,
        null);
  }

  /**
   * Creates the runtime telemetry coordinator with optional autonomous and vision diagnostics.
   *
   * @param visionSubsystem observation-only vision runtime owner
   * @param visionTelemetryFacade vision observation publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      Supplier<AutonomousPreparationObservation> autonomousPreparationObservationSupplier,
      AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade,
      Supplier<Optional<AutonomousEventObservation>> autonomousEventObservationSupplier,
      AutonomousEventTelemetryFacade autonomousEventTelemetryFacade,
      VisionSubsystem visionSubsystem,
      VisionTelemetryFacade visionTelemetryFacade) {
    this(
        swerveSubsystem,
        swerveTelemetryFacade,
        autonomousPreparationObservationSupplier,
        autonomousPreparationTelemetryFacade,
        autonomousEventObservationSupplier,
        autonomousEventTelemetryFacade,
        visionSubsystem,
        visionTelemetryFacade,
        null,
        null,
        null,
        null);
  }

  /**
   * Creates the runtime telemetry coordinator with optional Intake diagnostics.
   *
   * @param intakeSubsystem immutable Intake observation source
   * @param intakeTelemetryFacade Intake observation publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      Supplier<AutonomousPreparationObservation> autonomousPreparationObservationSupplier,
      AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade,
      Supplier<Optional<AutonomousEventObservation>> autonomousEventObservationSupplier,
      AutonomousEventTelemetryFacade autonomousEventTelemetryFacade,
      VisionSubsystem visionSubsystem,
      VisionTelemetryFacade visionTelemetryFacade,
      IntakeSubsystem intakeSubsystem,
      IntakeTelemetryFacade intakeTelemetryFacade) {
    this(
        swerveSubsystem,
        swerveTelemetryFacade,
        autonomousPreparationObservationSupplier,
        autonomousPreparationTelemetryFacade,
        autonomousEventObservationSupplier,
        autonomousEventTelemetryFacade,
        visionSubsystem,
        visionTelemetryFacade,
        intakeSubsystem,
        intakeTelemetryFacade,
        null,
        null);
  }

  /**
   * Creates the runtime telemetry coordinator with optional Intake and Feeder diagnostics.
   *
   * @param intakeSubsystem immutable Intake observation source
   * @param intakeTelemetryFacade Intake observation publisher
   * @param feederSubsystem immutable Feeder observation source
   * @param feederTelemetryFacade Feeder observation publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      Supplier<AutonomousPreparationObservation> autonomousPreparationObservationSupplier,
      AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade,
      Supplier<Optional<AutonomousEventObservation>> autonomousEventObservationSupplier,
      AutonomousEventTelemetryFacade autonomousEventTelemetryFacade,
      VisionSubsystem visionSubsystem,
      VisionTelemetryFacade visionTelemetryFacade,
      IntakeSubsystem intakeSubsystem,
      IntakeTelemetryFacade intakeTelemetryFacade,
      FeederSubsystem feederSubsystem,
      FeederTelemetryFacade feederTelemetryFacade) {
    this(
        swerveSubsystem,
        swerveTelemetryFacade,
        autonomousPreparationObservationSupplier,
        autonomousPreparationTelemetryFacade,
        autonomousEventObservationSupplier,
        autonomousEventTelemetryFacade,
        visionSubsystem,
        visionTelemetryFacade,
        intakeSubsystem,
        intakeTelemetryFacade,
        feederSubsystem,
        feederTelemetryFacade,
        null,
        null);
  }

  /**
   * Creates the runtime telemetry coordinator with optional mechanism diagnostics.
   *
   * @param intakeSubsystem immutable Intake observation source
   * @param intakeTelemetryFacade Intake observation publisher
   * @param feederSubsystem immutable Feeder observation source
   * @param feederTelemetryFacade Feeder observation publisher
   * @param flywheelSubsystem immutable Flywheel observation source
   * @param flywheelTelemetryFacade Flywheel observation publisher
   */
  public RobotTelemetry(
      SwerveSubsystem swerveSubsystem,
      SwerveTelemetryFacade swerveTelemetryFacade,
      Supplier<AutonomousPreparationObservation> autonomousPreparationObservationSupplier,
      AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade,
      Supplier<Optional<AutonomousEventObservation>> autonomousEventObservationSupplier,
      AutonomousEventTelemetryFacade autonomousEventTelemetryFacade,
      VisionSubsystem visionSubsystem,
      VisionTelemetryFacade visionTelemetryFacade,
      IntakeSubsystem intakeSubsystem,
      IntakeTelemetryFacade intakeTelemetryFacade,
      FeederSubsystem feederSubsystem,
      FeederTelemetryFacade feederTelemetryFacade,
      FlywheelSubsystem flywheelSubsystem,
      FlywheelTelemetryFacade flywheelTelemetryFacade) {
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
    if ((visionSubsystem == null) != (visionTelemetryFacade == null)) {
      throw new IllegalArgumentException(
          "vision observation source and facade must be supplied together");
    }
    this.visionSubsystem = Optional.ofNullable(visionSubsystem);
    this.visionTelemetryFacade = Optional.ofNullable(visionTelemetryFacade);
    if ((intakeSubsystem == null) != (intakeTelemetryFacade == null)) {
      throw new IllegalArgumentException(
          "Intake observation source and facade must be supplied together");
    }
    this.intakeSubsystem = Optional.ofNullable(intakeSubsystem);
    this.intakeTelemetryFacade = Optional.ofNullable(intakeTelemetryFacade);
    if ((feederSubsystem == null) != (feederTelemetryFacade == null)) {
      throw new IllegalArgumentException(
          "Feeder observation source and facade must be supplied together");
    }
    this.feederSubsystem = Optional.ofNullable(feederSubsystem);
    this.feederTelemetryFacade = Optional.ofNullable(feederTelemetryFacade);
    if ((flywheelSubsystem == null) != (flywheelTelemetryFacade == null)) {
      throw new IllegalArgumentException(
          "Flywheel observation source and facade must be supplied together");
    }
    this.flywheelSubsystem = Optional.ofNullable(flywheelSubsystem);
    this.flywheelTelemetryFacade = Optional.ofNullable(flywheelTelemetryFacade);
  }

  /**
   * Publishes the latest complete observation when one exists.
   */
  public void periodic() {
    swerveSubsystem
        .getObservation()
        .ifPresent(
            observation ->
                swerveTelemetryFacade.publish(
                    observation, swerveSubsystem.getVisionFusionObservation()));
    if (visionSubsystem.isPresent()) {
      visionTelemetryFacade.orElseThrow().publish(visionSubsystem.orElseThrow().getObservation());
    }
    if (intakeSubsystem.isPresent()) {
      intakeTelemetryFacade.orElseThrow().publish(intakeSubsystem.orElseThrow().getObservation());
    }
    if (feederSubsystem.isPresent()) {
      feederTelemetryFacade.orElseThrow().publish(feederSubsystem.orElseThrow().getObservation());
    }
    if (flywheelSubsystem.isPresent()) {
      flywheelTelemetryFacade
          .orElseThrow()
          .publish(flywheelSubsystem.orElseThrow().getObservation());
    }
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
