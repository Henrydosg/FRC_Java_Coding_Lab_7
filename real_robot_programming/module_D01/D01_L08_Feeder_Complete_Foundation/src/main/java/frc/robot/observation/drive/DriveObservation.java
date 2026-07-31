// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.drive;

/**
 * Provides an immutable drivetrain observation from the latest completed subsystem periodic
 * update.
 *
 * @param leftAppliedOutput normalized, dimensionless left-side applied output
 * @param rightAppliedOutput normalized, dimensionless right-side applied output
 */
public record DriveObservation(
    double leftAppliedOutput,
    double rightAppliedOutput) {}
