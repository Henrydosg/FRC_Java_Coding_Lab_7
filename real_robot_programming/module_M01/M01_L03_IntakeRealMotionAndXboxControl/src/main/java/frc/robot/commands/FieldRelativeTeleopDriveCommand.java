// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.observation.DriverInputObservation;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.driver.DriverInputTelemetryFacade;
import java.util.Objects;
import java.util.function.Consumer;

/** Converts one coherent driver-input sample into field-relative Swerve velocity intent. */
public final class FieldRelativeTeleopDriveCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final XboxDriverInputSource driverInputSource;
  private final DriverInputTelemetryFacade driverInputTelemetryFacade;
  private final Consumer<DriverInputObservation> driverInputPublisher;
  private boolean cleanupStopFailed;

  /**
   * Creates the field-relative teleop command.
   *
   * @param swerveSubsystem Swerve behavior owner
   * @param driverInputSource coherent Xbox observation source
   * @param driverInputTelemetryFacade read-only driver-input telemetry publisher
   */
  public FieldRelativeTeleopDriveCommand(
      SwerveSubsystem swerveSubsystem,
      XboxDriverInputSource driverInputSource,
      DriverInputTelemetryFacade driverInputTelemetryFacade) {
    this(
        swerveSubsystem,
        driverInputSource,
        Objects.requireNonNull(
            driverInputTelemetryFacade,
            "driverInputTelemetryFacade"),
        null);
  }

  FieldRelativeTeleopDriveCommand(
      SwerveSubsystem swerveSubsystem,
      XboxDriverInputSource driverInputSource,
      Consumer<DriverInputObservation> driverInputPublisher) {
    this(
        swerveSubsystem,
        driverInputSource,
        null,
        Objects.requireNonNull(driverInputPublisher, "driverInputPublisher"));
  }

  private FieldRelativeTeleopDriveCommand(
      SwerveSubsystem swerveSubsystem,
      XboxDriverInputSource driverInputSource,
      DriverInputTelemetryFacade driverInputTelemetryFacade,
      Consumer<DriverInputObservation> driverInputPublisher) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.driverInputSource = Objects.requireNonNull(driverInputSource, "driverInputSource");
    this.driverInputTelemetryFacade = driverInputTelemetryFacade;
    this.driverInputPublisher = driverInputPublisher;
    addRequirements(swerveSubsystem);
  }

  /**
   * Submits field-relative driver intent while Teleop is enabled.
   *
   * <p>After a cleanup stop failure, driver intent is no longer submitted for the rest of the
   * robot-code session; every execution only re-attempts the Swerve stop, so rescheduling this
   * default command cannot re-arm drive output.
   */
  @Override
  public void execute() {
    if (cleanupStopFailed) {
      attemptInhibitedStop();
      return;
    }
    if (!DriverStation.isTeleopEnabled()) {
      swerveSubsystem.stop();
      return;
    }
    try {
      DriverInputObservation observation = driverInputSource.read();
      ChassisSpeeds fieldRelativeSpeeds =
          new ChassisSpeeds(
              observation.processedForward()
                  * Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond,
              observation.processedStrafe()
                  * Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond,
              observation.processedRotation()
                  * Constants.SwerveConstants.kTeleopMaxAngularSpeedRadiansPerSecond);

      swerveSubsystem.acceptFieldRelativeChassisSpeeds(fieldRelativeSpeeds);
      publishDriverInput(observation);
    } catch (RuntimeException failure) {
      stopAfterFailure(failure);
      throw failure;
    }
  }

  /**
   * Attempts the Swerve stop without letting a failure escape, so scheduler cancellation and
   * interruption bookkeeping always completes.
   *
   * <p>A stop failure is reported once, is not treated as proof that the modules stopped, and
   * inhibits later driver intent from this command until robot-code restart.
   */
  @Override
  public void end(boolean interrupted) {
    try {
      swerveSubsystem.stop();
    } catch (RuntimeException stopFailure) {
      if (!cleanupStopFailed) {
        cleanupStopFailed = true;
        DriverStation.reportError(
            "Swerve stop failed during teleop drive command cleanup; neutral output not confirmed; "
                + "teleop drive intent inhibited until robot-code restart: "
                + stopFailure,
            false);
      }
    }
  }

  @Override
  public boolean isFinished() {
    return false;
  }

  private void attemptInhibitedStop() {
    try {
      swerveSubsystem.stop();
    } catch (RuntimeException repeatedStopFailure) {
      // Already reported once when the inhibition latched; the next execution retries the stop.
    }
  }

  private void stopAfterFailure(RuntimeException failure) {
    try {
      swerveSubsystem.stop();
    } catch (RuntimeException stopFailure) {
      failure.addSuppressed(stopFailure);
    }
  }

  private void publishDriverInput(DriverInputObservation observation) {
    if (driverInputTelemetryFacade != null) {
      driverInputTelemetryFacade.publish(observation);
      return;
    }
    driverInputPublisher.accept(observation);
  }
}
