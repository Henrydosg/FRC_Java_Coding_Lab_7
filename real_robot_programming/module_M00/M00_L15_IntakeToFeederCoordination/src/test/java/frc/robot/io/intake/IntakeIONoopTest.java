// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import org.junit.jupiter.api.Test;

/** Verifies deterministic safe behavior from the vendor-neutral Intake Noop. */
class IntakeIONoopTest {
  @Test
  void reportsUnavailableAndDisconnectedEveryCycle() {
    IntakeIONoop intakeIO = new IntakeIONoop();
    IntakeIOInputs inputs = new IntakeIOInputs();
    inputs.available = true;
    inputs.connected = true;

    intakeIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
  }

  @Test
  void semanticRequestsAreSafeToCall() {
    IntakeIONoop intakeIO = new IntakeIONoop();

    assertDoesNotThrow(intakeIO::requestIntake);
    assertDoesNotThrow(intakeIO::stop);
  }
}
