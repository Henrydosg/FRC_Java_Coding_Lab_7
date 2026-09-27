// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

/** Defines the vendor-neutral hardware contract for the Flywheel mechanism. */
public interface FlywheelIO {
  /** Mutable one-cycle transport snapshot owned by the Flywheel IO contract. */
  class FlywheelIOInputs {
    /** True when the selected IO implementation can provide Flywheel service. */
    public boolean available;

    /** True when the available Flywheel implementation reports a connected source. */
    public boolean connected;

    /** True when {@link #velocityRpm} is a current measured rotational speed. */
    public boolean velocityValid;

    /** Measured Flywheel rotational speed in RPM when {@link #velocityValid} is true. */
    public double velocityRpm;
  }

  /** Updates the complete mutable transport snapshot for the current cycle. */
  void updateInputs(FlywheelIOInputs inputs);

  /** Requests a semantic Flywheel mechanism velocity in RPM. */
  void requestVelocity(double targetRpm);

  /** Requests the IO implementation's safe stopped output state. */
  void stop();
}
