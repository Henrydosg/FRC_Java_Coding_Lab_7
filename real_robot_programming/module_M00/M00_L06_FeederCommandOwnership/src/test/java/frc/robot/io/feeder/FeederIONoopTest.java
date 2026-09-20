// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.feeder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import org.junit.jupiter.api.Test;

/** Verifies deterministic safe behavior from the vendor-neutral Feeder Noop. */
class FeederIONoopTest {
  @Test
  void reportsUnavailableAndDisconnectedEveryCycle() {
    FeederIONoop feederIO = new FeederIONoop();
    FeederIOInputs inputs = new FeederIOInputs();
    inputs.available = true;
    inputs.connected = true;

    feederIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);

    inputs.available = true;
    inputs.connected = true;
    feederIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
  }

  @Test
  void semanticRequestsAreSafeToCall() {
    FeederIONoop feederIO = new FeederIONoop();

    assertDoesNotThrow(feederIO::requestFeed);
    assertDoesNotThrow(feederIO::stop);
  }
}
