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
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Verifies read-only typed publication from immutable Elevator observations. */
class ElevatorTelemetryFacadeTest {
  @Test
  void publishesExactlyFiveObservationValuesIncludingValidZeroUnreferenced() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Elevator");
    try (ElevatorTelemetryFacade facade = new ElevatorTelemetryFacade(table)) {
      facade.publish(new ElevatorObservation(true, true, true, false, 0.0));

      assertTrue(table.getEntry("Available").getBoolean(false));
      assertTrue(table.getEntry("Connected").getBoolean(false));
      assertTrue(table.getEntry("PositionValid").getBoolean(false));
      assertEquals(false, table.getEntry("PositionReferenced").getBoolean(true));
      assertEquals(0.0, table.getEntry("PositionMeters").getDouble(-1.0));
      assertEquals(
          Set.of("Available", "Connected", "PositionValid", "PositionReferenced", "PositionMeters"),
          table.getKeys());
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
