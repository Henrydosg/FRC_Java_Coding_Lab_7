// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.intake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/** Verifies read-only typed publication from immutable Intake observations. */
class IntakeTelemetryFacadeTest {
  private static final double kTimestampSeconds = 42.5;
  private static final double kPositionRotations = 12.5;
  private static final double kVelocityRotationsPerSecond = -3.25;
  private static final double kStatorCurrentAmps = 7.5;
  private static final double kSupplyCurrentAmps = 2.25;
  private static final double kAppliedVolts = 0.125;

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
      assertFalse(table.getEntry("FaultLatched").getBoolean(true));
    } finally {
      instance.close();
    }
  }

  @Test
  void publishesValidMeasuredFeedback() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Intake");
    try (IntakeTelemetryFacade facade = new IntakeTelemetryFacade(table)) {
      facade.publish(
          new IntakeObservation(
              true,
              true,
              RequestedState.STOPPED,
              false,
              true,
              kTimestampSeconds,
              kPositionRotations,
              kVelocityRotationsPerSecond,
              kStatorCurrentAmps,
              kSupplyCurrentAmps,
              kAppliedVolts));

      assertTrue(table.getEntry("MeasurementValid").getBoolean(false));
      assertEquals(kPositionRotations, table.getEntry("PositionRotations").getDouble(Double.NaN));
      assertEquals(
          kVelocityRotationsPerSecond,
          table.getEntry("VelocityRotationsPerSecond").getDouble(Double.NaN));
      assertEquals(kStatorCurrentAmps, table.getEntry("StatorCurrentAmps").getDouble(Double.NaN));
      assertEquals(kSupplyCurrentAmps, table.getEntry("SupplyCurrentAmps").getDouble(Double.NaN));
      assertEquals(kAppliedVolts, table.getEntry("AppliedVolts").getDouble(Double.NaN));
    } finally {
      instance.close();
    }
  }

  @Test
  void publishesInvalidMeasuredFeedbackAsNaNWithValidityFalse() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Intake");
    try (IntakeTelemetryFacade facade = new IntakeTelemetryFacade(table)) {
      facade.publish(new IntakeObservation(true, true, RequestedState.STOPPED));

      assertFalse(table.getEntry("MeasurementValid").getBoolean(true));
      assertTrue(Double.isNaN(table.getEntry("PositionRotations").getDouble(0.0)));
      assertTrue(Double.isNaN(table.getEntry("VelocityRotationsPerSecond").getDouble(0.0)));
      assertTrue(Double.isNaN(table.getEntry("StatorCurrentAmps").getDouble(0.0)));
      assertTrue(Double.isNaN(table.getEntry("SupplyCurrentAmps").getDouble(0.0)));
      assertTrue(Double.isNaN(table.getEntry("AppliedVolts").getDouble(0.0)));
    } finally {
      instance.close();
    }
  }

  @Test
  void publishesLatchedFault() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    instance.startLocal();
    NetworkTable table = instance.getTable("Intake");
    try (IntakeTelemetryFacade facade = new IntakeTelemetryFacade(table)) {
      facade.publish(
          new IntakeObservation(
              true,
              true,
              RequestedState.STOPPED,
              true,
              false,
              Double.NaN,
              Double.NaN,
              Double.NaN,
              Double.NaN,
              Double.NaN,
              Double.NaN));

      assertTrue(table.getEntry("FaultLatched").getBoolean(false));
      assertEquals("STOPPED", table.getEntry("RequestedState").getString(""));
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
