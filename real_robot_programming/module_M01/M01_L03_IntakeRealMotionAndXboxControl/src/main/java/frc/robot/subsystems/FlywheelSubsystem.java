// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.observation.flywheel.FlywheelObservation.RequestedState;
import java.util.Objects;

/** Owns the Flywheel capability, requested state, and safe-stop call path. */
public final class FlywheelSubsystem extends SubsystemBase {
  private final FlywheelIO flywheelIO;
  private final FlywheelIOInputs inputs = new FlywheelIOInputs();
  private double requestedVelocityRpm = 0.0;
  private RequestedState requestedState = RequestedState.STOPPED;
  private FlywheelObservation latestObservation =
      new FlywheelObservation(false, false, false, 0.0, RequestedState.STOPPED, false);

  /** Creates the Flywheel owner with exactly one injected IO implementation. */
  public FlywheelSubsystem(FlywheelIO flywheelIO) {
    this.flywheelIO = Objects.requireNonNull(flywheelIO, "flywheelIO");
  }

  /** Refreshes IO and atomically replaces the immutable Flywheel observation. */
  @Override
  public void periodic() {
    flywheelIO.updateInputs(inputs);
    updateObservation();
  }

  /** Records a validated mechanism-RPM request before forwarding it to IO. */
  public void requestVelocity(double targetRpm) {
    if (!Double.isFinite(targetRpm) || targetRpm < 0.0) {
      requestedVelocityRpm = 0.0;
      requestedState = RequestedState.STOPPED;
      updateObservation();
      IllegalArgumentException failure =
          new IllegalArgumentException("targetRpm must be finite and nonnegative");
      try {
        flywheelIO.stop();
      } catch (RuntimeException stopFailure) {
        failure.addSuppressed(stopFailure);
      }
      throw failure;
    }

    if (targetRpm == 0.0) {
      requestedVelocityRpm = 0.0;
      requestedState = RequestedState.STOPPED;
      updateObservation();
      flywheelIO.stop();
      return;
    }

    requestedVelocityRpm = targetRpm;
    requestedState = RequestedState.VELOCITY_REQUESTED;
    updateObservation();
    flywheelIO.requestVelocity(targetRpm);
  }

  /** Records stopped software intent before immediately forwarding safe stop to IO. */
  public void stop() {
    requestedVelocityRpm = 0.0;
    requestedState = RequestedState.STOPPED;
    updateObservation();
    flywheelIO.stop();
  }

  /** Returns the latest immutable Flywheel observation. */
  public FlywheelObservation getObservation() {
    return latestObservation;
  }

  private void updateObservation() {
    latestObservation =
        new FlywheelObservation(
            inputs.available,
            inputs.connected,
            inputs.velocityValid,
            inputs.velocityRpm,
            requestedState,
            computeReadyAtSpeed());
  }

  private boolean computeReadyAtSpeed() {
    return requestedState == RequestedState.VELOCITY_REQUESTED
        && Double.isFinite(requestedVelocityRpm)
        && requestedVelocityRpm > 0.0
        && inputs.available
        && inputs.connected
        && inputs.velocityValid
        && Double.isFinite(inputs.velocityRpm)
        && Math.abs(inputs.velocityRpm - requestedVelocityRpm)
            <= Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm;
  }
}
