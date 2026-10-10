// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.util.Objects;

/** Owns the vendor-neutral Intake capability, requested state, and safe-stop call path. */
public final class IntakeSubsystem extends SubsystemBase {
  private final IntakeIO intakeIO;
  private final IntakeIOInputs inputs = new IntakeIOInputs();
  private RequestedState requestedState = RequestedState.STOPPED;
  private IntakeObservation latestObservation =
      new IntakeObservation(false, false, RequestedState.STOPPED);

  /** Creates the Intake owner with exactly one injected IO implementation. */
  public IntakeSubsystem(IntakeIO intakeIO) {
    this.intakeIO = Objects.requireNonNull(intakeIO, "intakeIO");
  }

  /** Refreshes IO and atomically replaces the immutable Intake observation. */
  @Override
  public void periodic() {
    intakeIO.updateInputs(inputs);
    updateObservation();
  }

  /** Records Intake software intent before forwarding the semantic request to IO. */
  public void requestIntake() {
    requestedState = RequestedState.INTAKE_REQUESTED;
    updateObservation();
    intakeIO.requestIntake();
  }

  /** Records stopped software intent before immediately forwarding safe stop to IO. */
  public void stop() {
    requestedState = RequestedState.STOPPED;
    updateObservation();
    intakeIO.stop();
  }

  /** Returns the latest immutable Intake observation. */
  public IntakeObservation getObservation() {
    return latestObservation;
  }

  private void updateObservation() {
    latestObservation =
        new IntakeObservation(inputs.available, inputs.connected, requestedState);
  }
}
