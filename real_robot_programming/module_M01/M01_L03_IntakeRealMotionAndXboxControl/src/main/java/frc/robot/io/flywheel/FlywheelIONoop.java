// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

/** Deterministic safe Flywheel implementation that never produces physical output. */
public final class FlywheelIONoop implements FlywheelIO {
  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
    inputs.available = false;
    inputs.connected = false;
    inputs.velocityValid = false;
    inputs.velocityRpm = 0.0;
  }

  @Override
  public void requestVelocity(double targetRpm) {}

  @Override
  public void stop() {}
}
