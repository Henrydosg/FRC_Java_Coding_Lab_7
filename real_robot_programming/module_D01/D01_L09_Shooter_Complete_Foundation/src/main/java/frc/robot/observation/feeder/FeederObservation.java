// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.feeder;

/**
 * Provides an immutable feeder observation.
 *
 * @param appliedOutput normalized applied output
 * @param positionRotations integrated encoder position in motor rotations
 * @param velocityRpm integrated encoder velocity in revolutions per minute
 * @param supplyCurrentAmps supply current in amperes
 * @param statorCurrentAmps always 0.0 because Spark MAX stator current is not applicable
 * @param temperatureCelsius motor temperature in degrees Celsius
 * @param connected whether the feeder controller is connected
 * @param configurationHealthy whether the hardware configuration applied successfully
 * @param mode current feeder mode
 */
public record FeederObservation(
    double appliedOutput,
    double positionRotations,
    double velocityRpm,
    double supplyCurrentAmps,
    double statorCurrentAmps,
    double temperatureCelsius,
    boolean connected,
    boolean configurationHealthy,
    FeederMode mode) {
  /**
   * Describes the current feeder behavior.
   */
  public enum FeederMode {
    FEEDING,
    REVERSING,
    STOPPED
  }
}
