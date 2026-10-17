// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.observation.feeder.FeederObservation;
import frc.robot.observation.feeder.FeederObservation.RequestedState;
import java.util.Objects;

/** Owns the vendor-neutral Feeder capability, requested state, and safe-stop call path. */
public final class FeederSubsystem extends SubsystemBase {
  private final FeederIO feederIO;
  private final FeederIOInputs inputs = new FeederIOInputs();
  private RequestedState requestedState = RequestedState.STOPPED;
  private FeederObservation latestObservation =
      new FeederObservation(false, false, RequestedState.STOPPED);

  /** Creates the Feeder owner with exactly one injected IO implementation. */
  public FeederSubsystem(FeederIO feederIO) {
    this.feederIO = Objects.requireNonNull(feederIO, "feederIO");
  }

  /** Refreshes IO and atomically replaces the immutable Feeder observation. */
  @Override
  public void periodic() {
    feederIO.updateInputs(inputs);
    updateObservation();
  }

  /** Records Feeder software intent before forwarding the semantic request to IO. */
  public void requestFeed() {
    requestedState = RequestedState.FEED_REQUESTED;
    updateObservation();
    feederIO.requestFeed();
  }

  /** Records stopped software intent before immediately forwarding safe stop to IO. */
  public void stop() {
    requestedState = RequestedState.STOPPED;
    updateObservation();
    feederIO.stop();
  }

  /** Returns the latest immutable Feeder observation. */
  public FeederObservation getObservation() {
    return latestObservation;
  }

  private void updateObservation() {
    latestObservation =
        new FeederObservation(inputs.available, inputs.connected, requestedState);
  }
}
