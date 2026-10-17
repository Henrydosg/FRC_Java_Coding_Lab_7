// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.observation.DriverInputObservation;
import org.junit.jupiter.api.Test;

class DriverInputTelemetryFacadeTest {
  private static final double kTolerance = 1.0e-12;

  @Test
  void publishesAllDriverInputObservationFieldsToApprovedKeys() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    NetworkTable driverInputTable = instance.getTable("DriverInputTest");
    DriverInputTelemetryFacade facade = new DriverInputTelemetryFacade(driverInputTable);

    facade.publish(
        new DriverInputObservation(
            0.10,
            -0.20,
            0.30,
            -0.10,
            0.20,
            -0.30,
            -0.01,
            0.04,
            -0.09));

    assertEquals(0.10, driverInputTable.getDoubleTopic("Raw/LeftY").getEntry(0.0).get(),
        kTolerance);
    assertEquals(-0.20, driverInputTable.getDoubleTopic("Raw/LeftX").getEntry(0.0).get(),
        kTolerance);
    assertEquals(0.30, driverInputTable.getDoubleTopic("Raw/RightX").getEntry(0.0).get(),
        kTolerance);
    assertEquals(-0.10,
        driverInputTable.getDoubleTopic("SemanticRaw/Forward").getEntry(0.0).get(),
        kTolerance);
    assertEquals(0.20,
        driverInputTable.getDoubleTopic("SemanticRaw/Strafe").getEntry(0.0).get(),
        kTolerance);
    assertEquals(-0.30,
        driverInputTable.getDoubleTopic("SemanticRaw/Rotation").getEntry(0.0).get(),
        kTolerance);
    assertEquals(-0.01,
        driverInputTable.getDoubleTopic("Processed/Forward").getEntry(0.0).get(),
        kTolerance);
    assertEquals(0.04,
        driverInputTable.getDoubleTopic("Processed/Strafe").getEntry(0.0).get(),
        kTolerance);
    assertEquals(-0.09,
        driverInputTable.getDoubleTopic("Processed/Rotation").getEntry(0.0).get(),
        kTolerance);

    facade.close();
    instance.close();
  }
}
