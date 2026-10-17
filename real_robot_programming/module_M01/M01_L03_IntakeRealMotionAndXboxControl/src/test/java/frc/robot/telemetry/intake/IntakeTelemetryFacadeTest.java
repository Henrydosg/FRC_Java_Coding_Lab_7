// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.intake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/** Verifies read-only typed publication from immutable Intake observations. */
class IntakeTelemetryFacadeTest {
  @Test
  void publishesOnlyObservationValues() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Intake");
    try (IntakeTelemetryFacade facade = new IntakeTelemetryFacade(table)) {
      facade.publish(new IntakeObservation(true, true, RequestedState.INTAKE_REQUESTED));

      assertTrue(table.getEntry("Available").getBoolean(false));
      assertTrue(table.getEntry("Connected").getBoolean(false));
      assertEquals(
          "INTAKE_REQUESTED", table.getEntry("RequestedState").getString(""));
    } finally {
      instance.close();
    }
  }

  @Test
  void declaredOperationsProvideNoControlReturnPath() {
    for (Method method : IntakeTelemetryFacade.class.getDeclaredMethods()) {
      assertEquals(void.class, method.getReturnType(), method.getName());
    }
  }
}
