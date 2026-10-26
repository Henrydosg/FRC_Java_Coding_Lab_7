// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.intake;

import java.util.Objects;

/**
 * Immutable, vendor-neutral Intake meaning for one coherent periodic snapshot.
 *
 * <p>The requested state describes software intent only. It does not assert physical motion or a
 * physically stopped mechanism.
 *
 * <p>Measured values are motor rotor quantities from the same periodic IO refresh. {@code
 * timestampSeconds} is the robot FPGA time of the oldest accepted measured sample in that group.
 * When {@code measurementValid} is false every measured value and the timestamp are {@link
 * Double#NaN}, so an invalid reading is never mistaken for a valid physical zero.
 *
 * @param available whether the selected IO implementation can provide Intake service
 * @param connected whether the available implementation reports a connected source
 * @param requestedState current software-requested Intake state
 * @param faultLatched whether the session Intake fault is latched; it rejects later requests
 *     until a controlled robot-code restart
 * @param measurementValid whether every measured value is a fresh, coherent device measurement
 * @param timestampSeconds robot FPGA time in seconds of the oldest accepted measured sample, or
 *     NaN when invalid
 * @param motorPositionRotations measured motor rotor position in rotations, or NaN when invalid
 * @param motorVelocityRotationsPerSecond measured motor rotor velocity in rotations per second, or
 *     NaN when invalid
 * @param statorCurrentAmps measured signed motor stator current in amps, or NaN when invalid
 * @param supplyCurrentAmps measured signed motor supply current in amps, or NaN when invalid
 * @param appliedVolts measured voltage applied to the motor in volts, or NaN when invalid
 */
public record IntakeObservation(
    boolean available,
    boolean connected,
    RequestedState requestedState,
    boolean faultLatched,
    boolean measurementValid,
    double timestampSeconds,
    double motorPositionRotations,
    double motorVelocityRotationsPerSecond,
    double statorCurrentAmps,
    double supplyCurrentAmps,
    double appliedVolts) {
  /** Software intent owned by the Intake subsystem. */
  public enum RequestedState {
    STOPPED,
    INTAKE_REQUESTED,
    /** An admitted Test-mode commissioning voltage request is active. */
    COMMISSIONING_REQUESTED,
    /** An admitted Teleop manual forward voltage request is active. */
    MANUAL_FORWARD_REQUESTED,
    /** An admitted Teleop manual reverse voltage request is active. */
    MANUAL_REVERSE_REQUESTED
  }

  /** Validates availability, measurement-validity and requested-state semantics. */
  public IntakeObservation {
    requestedState = Objects.requireNonNull(requestedState, "requestedState");
    if (connected && !available) {
      throw new IllegalArgumentException("connected Intake must also be available");
    }
    if (measurementValid && !connected) {
      throw new IllegalArgumentException("valid Intake measurement requires a connected source");
    }
    double[] measuredValues = {
      timestampSeconds,
      motorPositionRotations,
      motorVelocityRotationsPerSecond,
      statorCurrentAmps,
      supplyCurrentAmps,
      appliedVolts
    };
    for (double measuredValue : measuredValues) {
      if (measurementValid && !Double.isFinite(measuredValue)) {
        throw new IllegalArgumentException("valid Intake measurement values must be finite");
      }
      if (!measurementValid && !Double.isNaN(measuredValue)) {
        throw new IllegalArgumentException("invalid Intake measurement values must be NaN");
      }
    }
  }

  /** Creates an observation whose measurement is explicitly invalid. */
  public IntakeObservation(boolean available, boolean connected, RequestedState requestedState) {
    this(
        available,
        connected,
        requestedState,
        false,
        false,
        Double.NaN,
        Double.NaN,
        Double.NaN,
        Double.NaN,
        Double.NaN,
        Double.NaN);
  }
}
