// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.gyro;

/**
 * Deterministic no-op gyro IO for simulation without hardware behavior.
 */
public final class GyroIONoop implements GyroIO {
  @Override
  public void updateInputs(GyroIOInputs inputs) {
    inputs.yawDegrees = 0.0;
    inputs.pitchDegrees = 0.0;
    inputs.rollDegrees = 0.0;
    inputs.angularVelocityXDegreesPerSecond = 0.0;
    inputs.angularVelocityYDegreesPerSecond = 0.0;
    inputs.angularVelocityZDegreesPerSecond = 0.0;
    inputs.connected = false;
    inputs.configurationHealthy = false;
  }
}
