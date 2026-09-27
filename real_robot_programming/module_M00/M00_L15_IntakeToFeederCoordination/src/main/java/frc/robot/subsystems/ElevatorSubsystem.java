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
import frc.robot.observation.elevator.ElevatorRequestedState;
import java.util.Objects;

/** Owns the vendor-neutral Elevator position observation and safe-stop call path. */
public final class ElevatorSubsystem extends SubsystemBase {
  private final ElevatorIO elevatorIO;
  private final ElevatorTravelLimits travelLimits;
  private final ElevatorIOInputs inputs = new ElevatorIOInputs();
  private ElevatorRequestedState requestedState = ElevatorRequestedState.STOPPED;
  private double targetPositionMeters = 0.0;
  private ElevatorObservation latestObservation =
      new ElevatorObservation(
          false, false, false, false, 0.0, ElevatorRequestedState.STOPPED, 0.0, 0.0);

  /** Creates the Elevator owner without a configured operational travel envelope. */
  public ElevatorSubsystem(ElevatorIO elevatorIO) {
    this.elevatorIO = Objects.requireNonNull(elevatorIO, "elevatorIO");
    this.travelLimits = null;
  }

  /** Creates the Elevator owner with an explicit operational position-request envelope. */
  public ElevatorSubsystem(ElevatorIO elevatorIO, ElevatorTravelLimits travelLimits) {
    this.elevatorIO = Objects.requireNonNull(elevatorIO, "elevatorIO");
    this.travelLimits = Objects.requireNonNull(travelLimits, "travelLimits");
  }

  /** Refreshes IO and atomically replaces the immutable Elevator observation. */
  @Override
  public void periodic() {
    elevatorIO.updateInputs(inputs);
    updateObservation();
  }

  /** Records one validated position request and forwards it exactly once to IO. */
  public void requestPositionMeters(double targetPositionMeters) {
    if (!Double.isFinite(targetPositionMeters)) {
      throw new IllegalArgumentException("targetPositionMeters must be finite");
    }
    if (!latestObservation.positionValid() || !latestObservation.positionReferenced()) {
      throw new IllegalStateException("Elevator position must be valid and referenced");
    }
    if (travelLimits == null) {
      throw new IllegalStateException("Elevator operational travel limits are not configured");
    }
    if (targetPositionMeters < travelLimits.minPositionMeters()
        || targetPositionMeters > travelLimits.maxPositionMeters()) {
      throw new IllegalArgumentException("targetPositionMeters is outside the configured envelope");
    }

    requestedState = ElevatorRequestedState.POSITION_REQUESTED;
    this.targetPositionMeters = targetPositionMeters;
    updateObservation();
    elevatorIO.requestPositionMeters(targetPositionMeters);
  }

  /** Records homing intent and requests reference acquisition through the IO contract. */
  public void requestHoming() {
    if (!latestObservation.available() || !latestObservation.connected()) {
      throw new IllegalStateException("Elevator must be available and connected to request homing");
    }

    requestedState = ElevatorRequestedState.HOMING;
    targetPositionMeters = 0.0;
    updateObservation();
    elevatorIO.requestHoming();
  }

  /** Records stopped intent and immediately forwards one safe-stop request to IO. */
  public void stop() {
    requestedState = ElevatorRequestedState.STOPPED;
    targetPositionMeters = 0.0;
    updateObservation();
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
    double positionErrorMeters =
        requestedState == ElevatorRequestedState.POSITION_REQUESTED
                && positionValid
                && positionReferenced
            ? targetPositionMeters - positionMeters
            : 0.0;
    latestObservation =
        new ElevatorObservation(
            available,
            connected,
            positionValid,
            positionReferenced,
            positionMeters,
            requestedState,
            targetPositionMeters,
            positionErrorMeters);
  }
}
