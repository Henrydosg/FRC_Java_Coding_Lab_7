// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.observation.DriveThreeMeterValidationObservation;
import frc.robot.observation.SwerveObservation;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.validation.DriveThreeMeterValidationTelemetry;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleSupplier;

/** Drives robot-relative forward in Test mode while validating measured module travel. */
public final class DriveThreeMeterValidationCommand extends Command {
  private static final int MODULE_COUNT = 4;
  private static final int FRONT_LEFT_INDEX = 0;
  private static final int FRONT_RIGHT_INDEX = 1;
  private static final int BACK_LEFT_INDEX = 2;
  private static final int BACK_RIGHT_INDEX = 3;

  private enum TerminalReason {
    NONE,
    COMPLETE,
    TEST_MODE_REQUIRED,
    TEST_MODE_EXIT,
    SNAPSHOT_UNAVAILABLE,
    MODULE_UNHEALTHY,
    INVALID_MEASUREMENT,
    NEGATIVE_DELTA,
    MODULE_DISAGREEMENT,
    TIMEOUT,
    SUBMISSION_FAILURE,
    INTERRUPTED,
    TELEMETRY_FAILURE
  }

  private final SwerveSubsystem swerveSubsystem;
  private final DriveThreeMeterValidationTelemetry telemetry;
  private final DoubleSupplier timeSeconds;
  private final double targetMeters =
      Constants.SwerveConstants.kDriveThreeMeterValidationTargetMeters;
  private final double timeoutSeconds =
      Constants.SwerveConstants.kDriveThreeMeterValidationTimeoutSeconds;
  private final double disagreementToleranceMeters =
      Constants.SwerveConstants.kDriveThreeMeterValidationModuleDisagreementToleranceMeters;

  private final double[] previousDistancesMeters = new double[MODULE_COUNT];
  private double[] accumulatedForwardDistancesMeters = new double[MODULE_COUNT];
  private double measuredMeters;
  private double[] latestDeltasMeters = new double[MODULE_COUNT];
  private double startTimeSeconds;
  private TerminalReason terminalReason = TerminalReason.NONE;
  private boolean initialized;
  private boolean running;
  private boolean complete;
  private boolean stopIssued;

  /** Creates the production command using the FPGA clock. */
  public DriveThreeMeterValidationCommand(
      SwerveSubsystem swerveSubsystem,
      DriveThreeMeterValidationTelemetry telemetry) {
    this(swerveSubsystem, telemetry, Timer::getFPGATimestamp);
  }

  /** Creates the command with an injectable clock for deterministic tests. */
  DriveThreeMeterValidationCommand(
      SwerveSubsystem swerveSubsystem,
      DriveThreeMeterValidationTelemetry telemetry,
      DoubleSupplier timeSeconds) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.telemetry = Objects.requireNonNull(telemetry, "telemetry");
    this.timeSeconds = Objects.requireNonNull(timeSeconds, "timeSeconds");
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    initialized = true;
    running = false;
    complete = false;
    stopIssued = false;
    terminalReason = TerminalReason.NONE;
    measuredMeters = 0.0;
    accumulatedForwardDistancesMeters = new double[MODULE_COUNT];
    latestDeltasMeters = new double[MODULE_COUNT];
    publishState();
    if (terminalReason == TerminalReason.TELEMETRY_FAILURE) {
      return;
    }

    if (!DriverStation.isTestEnabled() || !DriverStation.isEnabled()) {
      abort(TerminalReason.TEST_MODE_REQUIRED);
      return;
    }

    startTimeSeconds = timeSeconds.getAsDouble();
    if (!Double.isFinite(startTimeSeconds)) {
      abort(TerminalReason.INVALID_MEASUREMENT);
      return;
    }

    Optional<SwerveObservation> observationOptional = swerveSubsystem.getObservation();
    SwerveModulePosition[] positions = swerveSubsystem.getMeasuredModulePositions();
    if (observationOptional.isEmpty() || positions.length != MODULE_COUNT) {
      abort(TerminalReason.SNAPSHOT_UNAVAILABLE);
      return;
    }
    if (!healthy(observationOptional.orElseThrow()) || !finite(positions)) {
      abort(healthy(observationOptional.orElseThrow())
          ? TerminalReason.INVALID_MEASUREMENT
          : TerminalReason.MODULE_UNHEALTHY);
      return;
    }

    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      previousDistancesMeters[moduleIndex] = positions[moduleIndex].distanceMeters;
    }
    running = true;
    publishState();
    if (terminalReason == TerminalReason.TELEMETRY_FAILURE) {
      return;
    }

    try {
      swerveSubsystem.acceptChassisSpeeds(
          new ChassisSpeeds(
              Constants.SwerveConstants.kFourModuleTestTranslationSpeedMetersPerSecond,
              0.0,
              0.0));
    } catch (RuntimeException failure) {
      abort(TerminalReason.SUBMISSION_FAILURE);
    }
  }

  @Override
  public void execute() {
    if (!initialized || !running) {
      return;
    }

    if (!DriverStation.isTestEnabled() || !DriverStation.isEnabled()) {
      abort(TerminalReason.TEST_MODE_EXIT);
      return;
    }

    double nowSeconds = timeSeconds.getAsDouble();
    if (!Double.isFinite(nowSeconds) || nowSeconds < startTimeSeconds) {
      abort(TerminalReason.INVALID_MEASUREMENT);
      return;
    }
    if (nowSeconds - startTimeSeconds >= timeoutSeconds) {
      abort(TerminalReason.TIMEOUT);
      return;
    }

    Optional<SwerveObservation> observationOptional = swerveSubsystem.getObservation();
    SwerveModulePosition[] positions = swerveSubsystem.getMeasuredModulePositions();
    if (observationOptional.isEmpty() || positions.length != MODULE_COUNT) {
      abort(TerminalReason.SNAPSHOT_UNAVAILABLE);
      return;
    }
    if (!healthy(observationOptional.orElseThrow())) {
      abort(TerminalReason.MODULE_UNHEALTHY);
      return;
    }
    if (!finite(positions)) {
      abort(TerminalReason.INVALID_MEASUREMENT);
      return;
    }

    double[] nextForwardDistancesMeters = new double[MODULE_COUNT];
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      double signedDistanceIncrementMeters =
          positions[moduleIndex].distanceMeters - previousDistancesMeters[moduleIndex];
      double forwardIncrementMeters =
          signedDistanceIncrementMeters * positions[moduleIndex].angle.getCos();
      nextForwardDistancesMeters[moduleIndex] =
          accumulatedForwardDistancesMeters[moduleIndex] + forwardIncrementMeters;
    }
    latestDeltasMeters = nextForwardDistancesMeters;
    if (!finite(nextForwardDistancesMeters)) {
      abort(TerminalReason.INVALID_MEASUREMENT);
      return;
    }
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      previousDistancesMeters[moduleIndex] = positions[moduleIndex].distanceMeters;
    }
    accumulatedForwardDistancesMeters = nextForwardDistancesMeters;
    if (containsNegative(accumulatedForwardDistancesMeters)) {
      abort(TerminalReason.NEGATIVE_DELTA);
      return;
    }

    double medianMeters = median(accumulatedForwardDistancesMeters);
    double maximumDeviationMeters = 0.0;
    for (double deltaMeters : accumulatedForwardDistancesMeters) {
      maximumDeviationMeters =
          Math.max(maximumDeviationMeters, Math.abs(deltaMeters - medianMeters));
    }
    if (maximumDeviationMeters > disagreementToleranceMeters) {
      abort(TerminalReason.MODULE_DISAGREEMENT);
      return;
    }

    measuredMeters = medianMeters;
    publishState();
    if (terminalReason == TerminalReason.TELEMETRY_FAILURE) {
      return;
    }
    if (measuredMeters >= targetMeters) {
      complete = true;
      running = false;
      terminalReason = TerminalReason.COMPLETE;
      stopOnce();
      publishState();
    }
  }

  @Override
  public boolean isFinished() {
    return initialized && !running;
  }

  @Override
  public void end(boolean interrupted) {
    if (interrupted && initialized && running) {
      terminalReason = TerminalReason.INTERRUPTED;
      running = false;
      stopOnce();
      publishState();
    } else {
      stopOnce();
    }
    initialized = false;
  }

  private void abort(TerminalReason reason) {
    terminalReason = reason;
    running = false;
    complete = false;
    stopOnce();
    publishState();
  }

  private void stopOnce() {
    if (!stopIssued) {
      stopIssued = true;
      swerveSubsystem.stop();
    }
  }

  private void publishState() {
    try {
      telemetry.publish(
          new DriveThreeMeterValidationObservation(
              targetMeters,
              measuredMeters,
              latestDeltasMeters[FRONT_LEFT_INDEX],
              latestDeltasMeters[FRONT_RIGHT_INDEX],
              latestDeltasMeters[BACK_LEFT_INDEX],
              latestDeltasMeters[BACK_RIGHT_INDEX],
              running,
              complete,
              terminalReason.name()));
    } catch (RuntimeException failure) {
      if (terminalReason != TerminalReason.TELEMETRY_FAILURE) {
        terminalReason = TerminalReason.TELEMETRY_FAILURE;
        running = false;
        complete = false;
        stopOnce();
      }
    }
  }

  private static boolean healthy(SwerveObservation observation) {
    return healthy(observation.frontLeft())
        && healthy(observation.frontRight())
        && healthy(observation.backLeft())
        && healthy(observation.backRight());
  }

  private static boolean healthy(SwerveObservation.ModuleObservation module) {
    return module.driveConnected()
        && module.steerConnected()
        && module.encoderConnected()
        && module.driveConfigurationHealthy()
        && module.steerConfigurationHealthy()
        && module.encoderConfigurationHealthy();
  }

  private static boolean finite(SwerveModulePosition[] positions) {
    for (SwerveModulePosition position : positions) {
      if (!Double.isFinite(position.distanceMeters)
          || !Double.isFinite(position.angle.getRadians())) {
        return false;
      }
    }
    return true;
  }

  private static boolean finite(double[] values) {
    for (double value : values) {
      if (!Double.isFinite(value)) {
        return false;
      }
    }
    return true;
  }

  private static boolean containsNegative(double[] values) {
    for (double value : values) {
      if (value < 0.0) {
        return true;
      }
    }
    return false;
  }

  private static double median(double[] values) {
    double[] sortedValues = Arrays.copyOf(values, values.length);
    Arrays.sort(sortedValues);
    return (sortedValues[1] + sortedValues[2]) * 0.5;
  }
}
