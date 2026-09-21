// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.observation.DriverInputObservation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class XboxDriverInputSourceTest {
  private static final double kTolerance = 1.0e-12;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void mapsAxesAndPreservesRawSemanticAndProcessedValues() {
    StubXboxController controller = new StubXboxController(0.40, -0.30, 0.55);
    XboxDriverInputSource source = new XboxDriverInputSource(controller);

    DriverInputObservation observation = source.read();

    assertNotNull(observation);
    assertEquals(0.40, observation.rawLeftY(), kTolerance);
    assertEquals(-0.30, observation.rawLeftX(), kTolerance);
    assertEquals(0.55, observation.rawRightX(), kTolerance);
    assertEquals(-0.40, observation.semanticRawForward(), kTolerance);
    assertEquals(0.30, observation.semanticRawStrafe(), kTolerance);
    assertEquals(-0.55, observation.semanticRawRotation(), kTolerance);
    assertEquals(
        -Math.pow(
            (0.40 - Constants.DriverInputConstants.kAxisDeadband)
                / (Constants.DriverInputConstants.kNormalizedMaximum
                    - Constants.DriverInputConstants.kAxisDeadband),
            2),
        observation.processedForward(), kTolerance);
    assertEquals(
        Math.pow(
            (0.30 - Constants.DriverInputConstants.kAxisDeadband)
                / (Constants.DriverInputConstants.kNormalizedMaximum
                    - Constants.DriverInputConstants.kAxisDeadband),
            2),
        observation.processedStrafe(), kTolerance);
    assertEquals(
        -Math.pow(
            (0.55 - Constants.DriverInputConstants.kAxisDeadband)
                / (Constants.DriverInputConstants.kNormalizedMaximum
                    - Constants.DriverInputConstants.kAxisDeadband),
            2),
        observation.processedRotation(), kTolerance);
  }

  @Test
  void readsEachAxisIndependentlyAndReturnsAStableSnapshot() {
    StubXboxController controller = new StubXboxController(-1.0, 0.0, 1.0);
    XboxDriverInputSource source = new XboxDriverInputSource(controller);

    DriverInputObservation observation = source.read();

    assertEquals(-1.0, observation.rawLeftY(), kTolerance);
    assertEquals(0.0, observation.rawLeftX(), kTolerance);
    assertEquals(1.0, observation.rawRightX(), kTolerance);
    assertEquals(1.0, observation.semanticRawForward(), kTolerance);
    assertEquals(0.0, observation.semanticRawStrafe(), kTolerance);
    assertEquals(-1.0, observation.semanticRawRotation(), kTolerance);
    assertEquals(1.0, observation.processedForward(), kTolerance);
    assertEquals(0.0, observation.processedStrafe(), kTolerance);
    assertEquals(-1.0, observation.processedRotation(), kTolerance);

    controller.setAxes(0.0, 0.0, 0.0);

    assertEquals(1.0, observation.processedForward(), kTolerance);
    assertEquals(0.0, observation.processedStrafe(), kTolerance);
    assertEquals(-1.0, observation.processedRotation(), kTolerance);
  }

  /** Test-only controller double at the injected XboxController boundary. */
  private static final class StubXboxController extends XboxController {
    private double leftY;
    private double leftX;
    private double rightX;

    private StubXboxController(double leftY, double leftX, double rightX) {
      super(Constants.DriverInputConstants.kXboxControllerPort);
      setAxes(leftY, leftX, rightX);
    }

    @Override
    public double getLeftY() {
      return leftY;
    }

    @Override
    public double getLeftX() {
      return leftX;
    }

    @Override
    public double getRightX() {
      return rightX;
    }

    private void setAxes(double leftY, double leftX, double rightX) {
      this.leftY = leftY;
      this.leftX = leftX;
      this.rightX = rightX;
    }
  }
}
