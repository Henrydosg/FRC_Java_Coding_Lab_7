// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.intake;

/**
 * Provides an immutable intake observation.
 *
 * @param appliedOutput normalized applied output
 * @param mode current intake mode
 * @param connected whether the intake controller is connected
 * @param supplyVoltageVolts supply voltage in volts
 * @param supplyCurrentAmps supply current in amperes
 * @param statorCurrentAmps stator current in amperes
 * @param temperatureCelsius motor temperature in degrees Celsius
 * @param positionRotations rotor position in rotations
 * @param velocityRpm rotor velocity in revolutions per minute
 * @param configurationHealthy whether the hardware configuration applied successfully
 */
public record IntakeObservation(
    double appliedOutput,
    IntakeMode mode,
    boolean connected,
    double supplyVoltageVolts,
    double supplyCurrentAmps,
    double statorCurrentAmps,
    double temperatureCelsius,
    double positionRotations,
    double velocityRpm,
    boolean configurationHealthy) {
  /**
   * Describes the current intake behavior.
   */
  public enum IntakeMode {
    INTAKE,
    OUTTAKE,
    STOPPED
  }
}
