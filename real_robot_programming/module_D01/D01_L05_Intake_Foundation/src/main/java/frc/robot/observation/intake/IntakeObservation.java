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
 */
public record IntakeObservation(
    double appliedOutput,
    IntakeMode mode,
    boolean connected) {
  /**
   * Describes the current intake behavior.
   */
  public enum IntakeMode {
    INTAKE,
    OUTTAKE,
    STOPPED
  }
}
