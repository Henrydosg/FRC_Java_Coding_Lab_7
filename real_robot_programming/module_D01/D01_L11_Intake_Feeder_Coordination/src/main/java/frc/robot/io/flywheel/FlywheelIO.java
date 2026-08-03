// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

/**
 * Defines the hardware operations required by the flywheel mechanism.
 */
public interface FlywheelIO {
  /**
   * Stores flywheel hardware observations for one periodic cycle.
   */
  class FlywheelIOInputs {
    public double appliedOutput;
    public double velocityRpm;
    public double supplyCurrentAmps;
    public double statorCurrentAmps;
    public double temperatureCelsius;
    public boolean connected;
    public boolean configurationHealthy;
  }

  /**
   * Updates the flywheel observation snapshot.
   *
   * @param inputs snapshot to update
   */
  void updateInputs(FlywheelIOInputs inputs);

  /**
   * Sets the normalized flywheel motor output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  void setOutput(double output);

  /**
   * Stops the flywheel motor.
   */
  void stop();
}
