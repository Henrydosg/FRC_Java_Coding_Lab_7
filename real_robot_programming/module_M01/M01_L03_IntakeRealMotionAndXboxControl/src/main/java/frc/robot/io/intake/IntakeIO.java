// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

/** Defines the vendor-neutral hardware contract for the Intake mechanism. */
public interface IntakeIO {
  /**
   * Mutable one-cycle transport snapshot owned by the Intake IO contract.
   *
   * <p>Measured values are meaningful only when {@link #measurementValid} is true. When it is
   * false every measured value and {@link #timestampSeconds} is {@link Double#NaN}, never a
   * fabricated zero.
   */
  class IntakeIOInputs {
    /** True when the selected IO implementation can provide Intake service. */
    public boolean available;

    /** True when the available Intake implementation reports a connected source. */
    public boolean connected;

    /** True when every measured value below is a fresh, coherent device measurement. */
    public boolean measurementValid;

    /**
     * Robot FPGA time in seconds of the oldest accepted measured sample; NaN when the measurement
     * is invalid.
     */
    public double timestampSeconds = Double.NaN;

    /** Measured motor rotor position in rotations; NaN when the measurement is invalid. */
    public double motorPositionRotations = Double.NaN;

    /** Measured motor rotor velocity in rotations per second; NaN when invalid. */
    public double motorVelocityRotationsPerSecond = Double.NaN;

    /** Measured motor stator current in amps; NaN when the measurement is invalid. */
    public double statorCurrentAmps = Double.NaN;

    /** Measured motor supply current in amps; NaN when the measurement is invalid. */
    public double supplyCurrentAmps = Double.NaN;

    /** Measured voltage applied to the motor in volts; NaN when the measurement is invalid. */
    public double appliedVolts = Double.NaN;
  }

  /** Updates the complete mutable transport snapshot for the current cycle. */
  void updateInputs(IntakeIOInputs inputs);

  /** Requests the semantic Intake action without exposing vendor-specific output units. */
  void requestIntake();

  /** Requests the IO implementation's safe stopped output state. */
  void stop();

  /**
   * Requests a motor output voltage, in volts. Admission, bounds and duration are owned by the
   * subsystem; the implementation only applies the request.
   *
   * <p>Additive contract method: implementations without an output path keep this default, which
   * rejects the request so a caller can fail closed.
   *
   * @param volts requested motor output voltage in volts
   * @throws UnsupportedOperationException when the implementation has no voltage output path
   */
  default void setVoltage(double volts) {
    throw new UnsupportedOperationException("Intake IO implementation has no voltage output");
  }
}
