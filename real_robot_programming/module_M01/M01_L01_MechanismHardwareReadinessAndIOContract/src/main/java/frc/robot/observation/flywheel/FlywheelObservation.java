// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.flywheel;

import java.util.Objects;

/**
 * Immutable, vendor-neutral Flywheel meaning for one coherent periodic snapshot.
 *
 * <p>The requested state describes software intent only. A valid velocity is a measurement and
 * does not assert physical Flywheel behavior.
 *
 * @param available whether the selected IO implementation can provide Flywheel service
 * @param connected whether the available implementation reports a connected source
 * @param velocityValid whether {@code velocityRpm} is a valid measured rotational speed
 * @param velocityRpm measured rotational speed in RPM when {@code velocityValid} is true
 * @param requestedState current software-requested Flywheel state
 * @param readyAtSpeed whether the requested Flywheel velocity is within the locked tolerance
 */
public record FlywheelObservation(
    boolean available,
    boolean connected,
    boolean velocityValid,
    double velocityRpm,
    RequestedState requestedState,
    boolean readyAtSpeed) {
  /** Software intent owned by the Flywheel subsystem. */
  public enum RequestedState {
    STOPPED,
    VELOCITY_REQUESTED
  }

  /** Validates availability, velocity-measurement, and requested-state semantics. */
  public FlywheelObservation {
    requestedState = Objects.requireNonNull(requestedState, "requestedState");
    if (connected && !available) {
      throw new IllegalArgumentException("connected Flywheel must also be available");
    }
    if (velocityValid && (!available || !connected)) {
      throw new IllegalArgumentException("valid Flywheel velocity requires availability and connection");
    }
    if (velocityValid && !Double.isFinite(velocityRpm)) {
      throw new IllegalArgumentException("valid Flywheel velocity must be finite");
    }
    if (!velocityValid
        && Double.doubleToLongBits(velocityRpm) != Double.doubleToLongBits(0.0)) {
      throw new IllegalArgumentException("invalid Flywheel velocity must use canonical 0.0 RPM");
    }
    if (readyAtSpeed
        && (!available
            || !connected
            || !velocityValid
            || requestedState != RequestedState.VELOCITY_REQUESTED)) {
      throw new IllegalArgumentException(
          "Ready-at-Speed requires valid connected velocity intent");
    }
  }
}
