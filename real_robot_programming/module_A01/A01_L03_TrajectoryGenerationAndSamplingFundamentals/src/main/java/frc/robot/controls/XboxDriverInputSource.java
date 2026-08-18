// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.observation.DriverInputObservation;
import java.util.Objects;

/** Reads one Xbox controller sample and converts it into an immutable observation. */
public final class XboxDriverInputSource {
  private final XboxController controller;

  /**
   * Creates an input source around an injected Xbox controller.
   *
   * @param controller controller boundary used to read raw axes
   */
  public XboxDriverInputSource(XboxController controller) {
    this.controller = Objects.requireNonNull(controller, "controller");
  }

  /**
   * Reads the current controller axes and returns one immutable observation.
   *
   * @return raw, semantic, and processed driver-input values
   */
  public DriverInputObservation read() {
    double rawLeftY = controller.getLeftY();
    double rawLeftX = controller.getLeftX();
    double rawRightX = controller.getRightX();

    double semanticRawForward = -rawLeftY;
    double semanticRawStrafe = -rawLeftX;
    double semanticRawRotation = -rawRightX;

    DriverInputProcessor.ProcessedDriverIntent processedIntent =
        DriverInputProcessor.process(
            semanticRawForward,
            semanticRawStrafe,
            semanticRawRotation);

    return new DriverInputObservation(
        rawLeftY,
        rawLeftX,
        rawRightX,
        semanticRawForward,
        semanticRawStrafe,
        semanticRawRotation,
        processedIntent.forward(),
        processedIntent.strafe(),
        processedIntent.rotation());
  }
}
