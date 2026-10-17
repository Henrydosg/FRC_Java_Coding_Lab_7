// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.feeder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.feeder.FeederObservation;
import frc.robot.observation.feeder.FeederObservation.RequestedState;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/** Verifies read-only typed publication from immutable Feeder observations. */
class FeederTelemetryFacadeTest {
  @Test
  void publishesOnlyObservationValues() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Feeder");
    try (FeederTelemetryFacade facade = new FeederTelemetryFacade(table)) {
      facade.publish(new FeederObservation(true, true, RequestedState.FEED_REQUESTED));

      assertTrue(table.getEntry("Available").getBoolean(false));
      assertTrue(table.getEntry("Connected").getBoolean(false));
      assertEquals("FEED_REQUESTED", table.getEntry("RequestedState").getString(""));
    } finally {
      instance.close();
    }
  }

  @Test
  void declaredOperationsProvideNoControlReturnPath() {
    for (Method method : FeederTelemetryFacade.class.getDeclaredMethods()) {
      assertEquals(void.class, method.getReturnType(), method.getName());
    }
  }
}
