// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of the
// WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SwerveModuleHardwareConfigurationContractTest {
  private static final double kTolerance = 1.0e-12;

  @Test
  void preservesCalibratedMechanicalRatios() {
    assertAll(
        () ->
            assertEquals(
                6.75,
                Constants.SwerveConstants.kDriveGearRatio,
                kTolerance),
        () ->
            assertEquals(
                15.42857142857143,
                Constants.SwerveConstants.kSteerGearRatio,
                kTolerance));
  }

  @Test
  void preservesPerModuleDriveInversionMapping() {
    assertAll(
        () -> assertFalse(Constants.SwerveConstants.kFrontLeftDriveInverted),
        () -> assertTrue(Constants.SwerveConstants.kFrontRightDriveInverted),
        () -> assertFalse(Constants.SwerveConstants.kBackLeftDriveInverted),
        () -> assertTrue(Constants.SwerveConstants.kBackRightDriveInverted));
  }

  @Test
  void preservesPerModuleSteerAndEncoderDirectionMapping() {
    assertAll(
        () -> assertTrue(Constants.SwerveConstants.kFrontLeftSteerInverted),
        () -> assertTrue(Constants.SwerveConstants.kFrontRightSteerInverted),
        () -> assertTrue(Constants.SwerveConstants.kBackLeftSteerInverted),
        () -> assertTrue(Constants.SwerveConstants.kBackRightSteerInverted),
        () -> assertFalse(Constants.SwerveConstants.kFrontLeftEncoderInverted),
        () -> assertFalse(Constants.SwerveConstants.kFrontRightEncoderInverted),
        () -> assertFalse(Constants.SwerveConstants.kBackLeftEncoderInverted),
        () -> assertFalse(Constants.SwerveConstants.kBackRightEncoderInverted));
  }

  @Test
  void preservesPerModuleEncoderOffsets() {
    assertAll(
        () ->
            assertEquals(
                0.068603515625,
                Constants.SwerveConstants.kFrontLeftEncoderOffsetRotations,
                kTolerance),
        () ->
            assertEquals(
                0.014404296875,
                Constants.SwerveConstants.kFrontRightEncoderOffsetRotations,
                kTolerance),
        () ->
            assertEquals(
                0.46240234375,
                Constants.SwerveConstants.kBackLeftEncoderOffsetRotations,
                kTolerance),
        () ->
            assertEquals(
                -0.057373046875,
                Constants.SwerveConstants.kBackRightEncoderOffsetRotations,
                kTolerance));
  }

  @Test
  void preservesConfirmedCurrentLimitContract() {
    assertAll(
        () ->
            assertEquals(
                70.0,
                Constants.SwerveConstants.kDriveSupplyCurrentLimitAmps,
                kTolerance),
        () -> assertTrue(Constants.SwerveConstants.kDriveSupplyCurrentLimitEnabled),
        () ->
            assertEquals(
                60.0,
                Constants.SwerveConstants.kSteerStatorCurrentLimitAmps,
                kTolerance),
        () -> assertTrue(Constants.SwerveConstants.kSteerStatorCurrentLimitEnabled));
  }
}
