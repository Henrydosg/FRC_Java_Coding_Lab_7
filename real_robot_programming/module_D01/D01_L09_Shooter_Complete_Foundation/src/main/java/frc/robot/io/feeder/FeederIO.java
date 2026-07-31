// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.feeder;

/**
 * Defines the hardware operations required by the feeder mechanism.
 */
public interface FeederIO {
  /**
   * Stores feeder hardware observations for one periodic cycle.
   */
  class FeederIOInputs {
    public double appliedOutput;
    public double positionRotations;
    public double velocityRpm;
    public double supplyCurrentAmps;
    public double statorCurrentAmps;
    public double temperatureCelsius;
    public boolean connected;
    public boolean configurationHealthy;
  }

  /**
   * Updates the feeder observation snapshot.
   *
   * @param inputs snapshot to update
   */
  void updateInputs(FeederIOInputs inputs);

  /**
   * Sets the normalized feeder motor output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  void setOutput(double output);

  /**
   * Stops the feeder motor.
   */
  void stop();
}
