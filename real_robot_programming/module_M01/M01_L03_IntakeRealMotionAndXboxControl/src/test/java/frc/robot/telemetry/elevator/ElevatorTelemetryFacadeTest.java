// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.elevator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.elevator.ElevatorObservation;
import frc.robot.observation.elevator.ElevatorRequestedState;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Verifies read-only typed publication from immutable Elevator observations. */
class ElevatorTelemetryFacadeTest {
  @Test
  void publishesExactlyEightObservationValues() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Elevator");
    try (ElevatorTelemetryFacade facade = new ElevatorTelemetryFacade(table)) {
      facade.publish(
          new ElevatorObservation(
              true, true, true, true, 1.25, ElevatorRequestedState.POSITION_REQUESTED, 2.0, 0.75));

      assertTrue(table.getEntry("Available").getBoolean(false));
      assertTrue(table.getEntry("Connected").getBoolean(false));
      assertTrue(table.getEntry("PositionValid").getBoolean(false));
      assertTrue(table.getEntry("PositionReferenced").getBoolean(false));
      assertEquals(1.25, table.getEntry("PositionMeters").getDouble(-1.0));
      assertEquals("POSITION_REQUESTED", table.getEntry("RequestedState").getString(""));
      assertEquals(2.0, table.getEntry("TargetPositionMeters").getDouble(-1.0));
      assertEquals(0.75, table.getEntry("PositionErrorMeters").getDouble(-1.0));
      assertEquals(
          Set.of(
              "Available",
              "Connected",
              "PositionValid",
              "PositionReferenced",
              "PositionMeters",
              "RequestedState",
              "TargetPositionMeters",
              "PositionErrorMeters"),
          table.getKeys());
    } finally {
      instance.close();
    }
  }

  @Test
  void publishesStoppedCanonicalPlaceholders() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Elevator");
    try (ElevatorTelemetryFacade facade = new ElevatorTelemetryFacade(table)) {
      facade.publish(
          new ElevatorObservation(
              false, false, false, false, 0.0, ElevatorRequestedState.STOPPED, 0.0, 0.0));

      assertEquals("STOPPED", table.getEntry("RequestedState").getString(""));
      assertEquals(0.0, table.getEntry("TargetPositionMeters").getDouble(-1.0));
      assertEquals(0.0, table.getEntry("PositionErrorMeters").getDouble(-1.0));
    } finally {
      instance.close();
    }
  }

  @Test
  void declaredOperationsProvideNoControlReturnPath() {
    for (Method method : ElevatorTelemetryFacade.class.getDeclaredMethods()) {
      assertEquals(void.class, method.getReturnType(), method.getName());
    }
  }
}
