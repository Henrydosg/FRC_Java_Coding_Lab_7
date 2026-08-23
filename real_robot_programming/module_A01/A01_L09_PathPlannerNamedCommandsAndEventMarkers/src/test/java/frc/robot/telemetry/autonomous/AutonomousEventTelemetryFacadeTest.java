// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.autonomous;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.commands.AutonomousEventId;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import org.junit.jupiter.api.Test;

class AutonomousEventTelemetryFacadeTest {
  @Test
  void publishesLifecycleKeysAndAggregatesDispatchesOutsideObservation() {
    NetworkTable table =
        NetworkTableInstance.getDefault()
            .getTable("A01_L09_Test")
            .getSubTable("Autonomous")
            .getSubTable("Event");
    try (AutonomousEventTelemetryFacade facade = new AutonomousEventTelemetryFacade(table)) {
      facade.publish(
          new AutonomousEventObservation(
              AutonomousEventId.LEARNING_EVENT, LifecycleState.STARTED, true));
      facade.publish(
          new AutonomousEventObservation(
              AutonomousEventId.LEARNING_EVENT, LifecycleState.ACTIVE, true));
      facade.publish(
          new AutonomousEventObservation(
              AutonomousEventId.LEARNING_EVENT, LifecycleState.COMPLETED, false));

      assertEquals("LEARNING_EVENT", table.getStringTopic("LastEvent").getEntry("?").get());
      assertEquals("COMPLETED", table.getStringTopic("State").getEntry("?").get());
      assertTrue(!table.getBooleanTopic("Active").getEntry(false).get());
      assertEquals(1.0, table.getDoubleTopic("DispatchCount").getEntry(0.0).get());
    }
  }
}
