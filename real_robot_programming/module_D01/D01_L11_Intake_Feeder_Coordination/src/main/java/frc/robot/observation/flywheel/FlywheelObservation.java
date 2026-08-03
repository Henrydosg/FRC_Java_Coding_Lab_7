// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.flywheel;

/**
 * Provides an immutable flywheel observation.
 *
 * @param appliedOutput normalized applied output
 * @param velocityRpm rotor velocity in revolutions per minute
 * @param supplyCurrentAmps supply current in amperes
 * @param statorCurrentAmps stator current in amperes
 * @param temperatureCelsius motor temperature in degrees Celsius
 * @param connected whether the flywheel controller is connected
 * @param configurationHealthy whether the hardware configuration applied successfully
 * @param mode current flywheel mode
 */
public record FlywheelObservation(
    double appliedOutput,
    double velocityRpm,
    double supplyCurrentAmps,
    double statorCurrentAmps,
    double temperatureCelsius,
    boolean connected,
    boolean configurationHealthy,
    FlywheelMode mode) {
  /**
   * Describes the current flywheel behavior.
   */
  public enum FlywheelMode {
    RUNNING,
    STOPPED
  }
}
