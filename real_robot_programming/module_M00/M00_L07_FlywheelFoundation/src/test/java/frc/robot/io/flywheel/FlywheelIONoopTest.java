// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import org.junit.jupiter.api.Test;

/** Verifies deterministic safe behavior from the vendor-neutral Flywheel Noop. */
class FlywheelIONoopTest {
  private static final double NONZERO_VELOCITY_RPM = 1234.0;

  @Test
  void reportsUnavailableDisconnectedAndInvalidZeroVelocityEveryCycle() {
    FlywheelIONoop flywheelIO = new FlywheelIONoop();
    FlywheelIOInputs inputs = new FlywheelIOInputs();
    inputs.available = true;
    inputs.connected = true;
    inputs.velocityValid = true;
    inputs.velocityRpm = NONZERO_VELOCITY_RPM;

    flywheelIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.velocityValid);
    assertEquals(0.0, inputs.velocityRpm);

    inputs.available = true;
    inputs.connected = true;
    inputs.velocityValid = true;
    inputs.velocityRpm = NONZERO_VELOCITY_RPM;
    flywheelIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.velocityValid);
    assertEquals(0.0, inputs.velocityRpm);
  }

  @Test
  void semanticRequestsAreSafeToCall() {
    FlywheelIONoop flywheelIO = new FlywheelIONoop();

    assertDoesNotThrow(flywheelIO::requestSpin);
    assertDoesNotThrow(flywheelIO::stop);
  }
}
