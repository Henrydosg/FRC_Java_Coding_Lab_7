// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.autonomous;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import org.junit.jupiter.api.Test;

class AutonomousEventTelemetryFacadeTest {
  @Test
  void publishesReadOnlyEventFieldsAndCountsFreshStarts() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    NetworkTable table = instance.getTable("AutonomousEvent");
    AutonomousEventTelemetryFacade facade = new AutonomousEventTelemetryFacade(table);

    assertEquals(0.0, table.getDoubleTopic("DispatchCount").getEntry(0.0).get());
    facade.publish(
        new AutonomousEventObservation(
            AutonomousEventId.LEARNING_EVENT, LifecycleState.STARTED, true));
    facade.publish(
        new AutonomousEventObservation(
            AutonomousEventId.LEARNING_EVENT, LifecycleState.ACTIVE, true));
    facade.publish(
        new AutonomousEventObservation(
            AutonomousEventId.LEARNING_EVENT, LifecycleState.COMPLETED, false));

    assertEquals("LEARNING_EVENT", table.getStringTopic("LastEvent").getEntry("").get());
    assertEquals("COMPLETED", table.getStringTopic("State").getEntry("").get());
    assertEquals(0.0, table.getBooleanTopic("Active").getEntry(true).get() ? 1.0 : 0.0);
    assertEquals(1.0, table.getDoubleTopic("DispatchCount").getEntry(0.0).get());

    facade.close();
    instance.close();
  }
}
