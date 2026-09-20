// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.observation.flywheel.FlywheelObservation.RequestedState;
import java.util.Objects;

/** Owns the Flywheel capability, requested state, and safe-stop call path. */
public final class FlywheelSubsystem extends SubsystemBase {
  private final FlywheelIO flywheelIO;
  private final FlywheelIOInputs inputs = new FlywheelIOInputs();
  private RequestedState requestedState = RequestedState.STOPPED;
  private FlywheelObservation latestObservation =
      new FlywheelObservation(false, false, false, 0.0, RequestedState.STOPPED);

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

  /** Records Flywheel software intent before forwarding the semantic spin request to IO. */
  public void requestSpin() {
    requestedState = RequestedState.SPIN_REQUESTED;
    updateObservation();
    flywheelIO.requestSpin();
  }

  /** Records stopped software intent before immediately forwarding safe stop to IO. */
  public void stop() {
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
            requestedState);
  }
}
