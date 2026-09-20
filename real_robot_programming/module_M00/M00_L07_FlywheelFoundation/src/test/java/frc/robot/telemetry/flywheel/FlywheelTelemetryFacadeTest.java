// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.flywheel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.observation.flywheel.FlywheelObservation.RequestedState;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/** Verifies read-only typed publication from immutable Flywheel observations. */
class FlywheelTelemetryFacadeTest {
  private static final double VALID_VELOCITY_RPM = 4200.0;

  @Test
  void publishesOnlyObservationValues() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Flywheel");
    try (FlywheelTelemetryFacade facade = new FlywheelTelemetryFacade(table)) {
      facade.publish(
          new FlywheelObservation(
              true, true, true, VALID_VELOCITY_RPM, RequestedState.SPIN_REQUESTED));

      assertTrue(table.getEntry("Available").getBoolean(false));
      assertTrue(table.getEntry("Connected").getBoolean(false));
      assertTrue(table.getEntry("VelocityValid").getBoolean(false));
      assertEquals(VALID_VELOCITY_RPM, table.getEntry("VelocityRpm").getDouble(0.0));
      assertEquals("SPIN_REQUESTED", table.getEntry("RequestedState").getString(""));
    } finally {
      instance.close();
    }
  }

  @Test
  void declaredOperationsProvideNoControlReturnPath() {
    for (Method method : FlywheelTelemetryFacade.class.getDeclaredMethods()) {
      assertEquals(void.class, method.getReturnType(), method.getName());
    }
  }
}
