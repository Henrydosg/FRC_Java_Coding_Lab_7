// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.io.vision.VisionIO;
import frc.robot.io.vision.VisionIOSim;
import frc.robot.io.vision.VisionIOSimHarness;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies that the interactive fixture is confined to simulation composition. */
class RobotSimulationHarnessCompositionTest {
  private static final String FIXTURE_KEY = "Simulation/VisionFusionHarness/Fixture";

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void simulationPublishesChooserAndStartsUnavailable()
      throws ReflectiveOperationException {
    assertTrue(RobotBase.isSimulation());
    RobotContainer robotContainer = new RobotContainer();

    @SuppressWarnings("unchecked")
    SendableChooser<VisionIOSimHarness.FixtureSelection> chooser =
        assertInstanceOf(SendableChooser.class, SmartDashboard.getData(FIXTURE_KEY));
    assertSame(VisionIOSimHarness.FixtureSelection.UNAVAILABLE, chooser.getSelected());

    Field visionIoField = RobotContainer.class.getDeclaredField("visionIO");
    visionIoField.setAccessible(true);
    VisionIO visionIO = (VisionIO) visionIoField.get(robotContainer);
    assertInstanceOf(VisionIOSim.class, visionIO);

    robotContainer.runSimulationHarness();
    VisionIO.VisionIOInputs inputs = new VisionIO.VisionIOInputs();
    visionIO.updateInputs(inputs);
    assertFalse(inputs.available);

    Field harnessField = RobotContainer.class.getDeclaredField("visionIOSimHarness");
    harnessField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Optional<VisionIOSimHarness> harness =
        (Optional<VisionIOSimHarness>) harnessField.get(robotContainer);
    assertTrue(harness.isPresent());
    assertTrue(RobotBase.isSimulation());
  }
}
