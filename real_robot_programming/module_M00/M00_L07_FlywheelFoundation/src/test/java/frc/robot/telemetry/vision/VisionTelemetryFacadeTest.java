// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.vision.VisionObservation;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Verifies that vision telemetry remains a write-only diagnostic publisher. */
class VisionTelemetryFacadeTest {
  @Test
  void publishesObservationValuesWithoutAControlReturnPath() {
    NetworkTableInstance networkTableInstance = NetworkTableInstance.create();
    networkTableInstance.startLocal();
    try (VisionTelemetryFacade facade =
        new VisionTelemetryFacade(networkTableInstance.getTable("Vision"))) {
      facade.publish(new VisionObservation(VisionObservation.State.NO_TARGETS, List.of()));

      assertTrue(networkTableInstance.getTable("Vision").getEntry("Available").getBoolean(false));
      assertTrue(
          networkTableInstance.getTable("Vision").getEntry("SampleValid").getBoolean(false));
      assertEquals("NO_TARGETS", networkTableInstance.getTable("Vision").getEntry("State").getString(""));
      assertEquals(0L, networkTableInstance.getTable("Vision").getEntry("TargetCount").getInteger(-1L));
    } finally {
      networkTableInstance.close();
    }
  }

  @Test
  void declaredOperationsDoNotReturnControlOrMechanismState() {
    for (Method method : VisionTelemetryFacade.class.getDeclaredMethods()) {
      assertEquals(void.class, method.getReturnType(), method.getName());
    }
  }
}
