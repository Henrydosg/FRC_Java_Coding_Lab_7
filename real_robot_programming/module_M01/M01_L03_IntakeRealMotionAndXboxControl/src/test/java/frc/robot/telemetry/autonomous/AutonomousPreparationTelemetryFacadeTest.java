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
import frc.robot.observation.autonomous.AutonomousPreparationObservation;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.AllianceIdentity;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Reason;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.ReturnedCommand;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Routine;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import org.junit.jupiter.api.Test;

class AutonomousPreparationTelemetryFacadeTest {
  private static final double kTolerance = 1.0e-12;

  @Test
  void publishesImmutableLifecycleAndFaultDiagnosticsToStableTopics() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    NetworkTable table = instance.getTable("AutonomousPreparation");
    AutonomousPreparationTelemetryFacade facade =
        new AutonomousPreparationTelemetryFacade(table);
    AutonomousPreparationObservation observation =
        new AutonomousPreparationObservation(
            State.FAULTED,
            false,
            7L,
            Reason.FATAL_ADAPTER_FAULT,
            Routine.ONE_METER_PATH,
            AllianceIdentity.RED,
            "REBUILT_WELDED",
            "A01_L06_OneMeter_Forward",
            true,
            false,
            true,
            true,
            0.02,
            Math.toRadians(1.5),
            true,
            true,
            true,
            true,
            "first fatal",
            ReturnedCommand.SAFE_STOP_FALLBACK,
            false);

    facade.publish(observation);

    assertEquals("FAULTED", table.getStringTopic("State").getEntry("").get());
    assertEquals(7L, table.getIntegerTopic("AttemptId").getEntry(0L).get());
    assertEquals(
        "FATAL_ADAPTER_FAULT",
        table.getStringTopic("Reason").getEntry("").get());
    assertEquals("RED", table.getStringTopic("Alliance").getEntry("").get());
    assertEquals(
        "A01_L06_OneMeter_Forward",
        table.getStringTopic("PathIdentity").getEntry("").get());
    assertTrue(table.getBooleanTopic("AdapterFatalFaulted").getEntry(false).get());
    assertEquals(
        "first fatal",
        table.getStringTopic("FirstFatalReason").getEntry("").get());
    assertEquals(
        "SAFE_STOP_FALLBACK",
        table.getStringTopic("ReturnedCommand").getEntry("").get());
    assertEquals(
        1.5,
        table.getDoubleTopic("PoseHeadingErrorDegrees").getEntry(0.0).get(),
        kTolerance);

    facade.close();
    instance.close();
  }
}
