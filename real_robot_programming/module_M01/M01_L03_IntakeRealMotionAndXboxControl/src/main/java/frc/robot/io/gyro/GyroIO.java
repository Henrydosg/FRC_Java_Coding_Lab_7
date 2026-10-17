// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.gyro;

/**
 * Defines raw read-only operations for the robot gyro.
 */
public interface GyroIO {
  /**
   * Stores one periodic cycle of raw gyro observations.
   */
  class GyroIOInputs {
    /**
     * Continuous robot heading in degrees using the adapter's established zero reference.
     * Counterclockwise is positive. The value is valid for control only when it is finite and both
     * {@link #connected} and {@link #configurationHealthy} are true.
     */
    public double yawDegrees;
    public double pitchDegrees;
    public double rollDegrees;
    public double angularVelocityXDegreesPerSecond;
    public double angularVelocityYDegreesPerSecond;
    public double angularVelocityZDegreesPerSecond;
    public boolean connected;
    public boolean configurationHealthy;
  }

  /**
   * Updates the raw gyro snapshot.
   *
   * @param inputs snapshot to update
   */
  void updateInputs(GyroIOInputs inputs);
}
