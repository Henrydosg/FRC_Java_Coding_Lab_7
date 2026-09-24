// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.elevator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import frc.robot.io.elevator.ElevatorIO.ElevatorIOInputs;
import org.junit.jupiter.api.Test;

/** Verifies deterministic safe behavior from the vendor-neutral Elevator Noop. */
class ElevatorIONoopTest {
  @Test
  void reportsCanonicalUnavailablePlaceholderEveryCycle() {
    ElevatorIONoop elevatorIO = new ElevatorIONoop();
    ElevatorIOInputs inputs = new ElevatorIOInputs();
    inputs.available = true;
    inputs.connected = true;
    inputs.positionValid = true;
    inputs.positionReferenced = true;
    inputs.positionMeters = 3.5;

    elevatorIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.positionValid);
    assertFalse(inputs.positionReferenced);
    assertEquals(0.0, inputs.positionMeters);
  }

  @Test
  void repeatedUpdatesRemainDeterministicAndStopIsSafe() {
    ElevatorIONoop elevatorIO = new ElevatorIONoop();
    ElevatorIOInputs inputs = new ElevatorIOInputs();

    elevatorIO.updateInputs(inputs);
    double firstPosition = inputs.positionMeters;
    elevatorIO.updateInputs(inputs);

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.positionValid);
    assertFalse(inputs.positionReferenced);
    assertEquals(firstPosition, inputs.positionMeters);
    assertDoesNotThrow(elevatorIO::stop);
  }

  @Test
  void positionRequestIsASafeNoop() {
    ElevatorIONoop elevatorIO = new ElevatorIONoop();
    ElevatorIOInputs inputs = new ElevatorIOInputs();
    elevatorIO.updateInputs(inputs);

    assertDoesNotThrow(() -> elevatorIO.requestPositionMeters(2.5));
    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.positionValid);
    assertFalse(inputs.positionReferenced);
    assertEquals(0.0, inputs.positionMeters);
  }

  @Test
  void homingRequestIsATruthfulNoop() {
    ElevatorIONoop elevatorIO = new ElevatorIONoop();
    ElevatorIOInputs inputs = new ElevatorIOInputs();
    elevatorIO.updateInputs(inputs);

    assertDoesNotThrow(elevatorIO::requestHoming);
    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.positionValid);
    assertFalse(inputs.positionReferenced);
    assertEquals(0.0, inputs.positionMeters);
  }
}
