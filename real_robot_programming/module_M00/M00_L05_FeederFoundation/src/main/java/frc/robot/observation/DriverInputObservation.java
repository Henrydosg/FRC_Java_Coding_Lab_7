// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation;

/** Immutable, vendor-neutral observation of one driver-input sample. */
public record DriverInputObservation(
    double rawLeftY,
    double rawLeftX,
    double rawRightX,
    double semanticRawForward,
    double semanticRawStrafe,
    double semanticRawRotation,
    double processedForward,
    double processedStrafe,
    double processedRotation) {}
