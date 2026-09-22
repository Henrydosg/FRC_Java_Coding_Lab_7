// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.observation.elevator.ElevatorObservation;
import java.util.Objects;

/** Owns the vendor-neutral Elevator position observation and safe-stop call path. */
public final class ElevatorSubsystem extends SubsystemBase {
  private final ElevatorIO elevatorIO;
  private final ElevatorIOInputs inputs = new ElevatorIOInputs();
  private ElevatorObservation latestObservation =
      new ElevatorObservation(false, false, false, false, 0.0);

  /** Creates the Elevator owner with exactly one injected IO implementation. */
  public ElevatorSubsystem(ElevatorIO elevatorIO) {
    this.elevatorIO = Objects.requireNonNull(elevatorIO, "elevatorIO");
  }

  /** Refreshes IO and atomically replaces the immutable Elevator observation. */
  @Override
  public void periodic() {
    elevatorIO.updateInputs(inputs);
    updateObservation();
  }

  /** Immediately forwards one safe-stop request without changing observation semantics. */
  public void stop() {
    elevatorIO.stop();
  }

  /** Returns the latest immutable Elevator observation. */
  public ElevatorObservation getObservation() {
    return latestObservation;
  }

  private void updateObservation() {
    boolean available = inputs.available;
    boolean connected = available && inputs.connected;
    boolean finitePosition = Double.isFinite(inputs.positionMeters);
    boolean positionValid = connected && inputs.positionValid && finitePosition;
    boolean positionReferenced = positionValid && inputs.positionReferenced;
    double positionMeters = finitePosition ? inputs.positionMeters : 0.0;
    latestObservation =
        new ElevatorObservation(
            available,
            connected,
            positionValid,
            positionReferenced,
            positionMeters);
  }
}
