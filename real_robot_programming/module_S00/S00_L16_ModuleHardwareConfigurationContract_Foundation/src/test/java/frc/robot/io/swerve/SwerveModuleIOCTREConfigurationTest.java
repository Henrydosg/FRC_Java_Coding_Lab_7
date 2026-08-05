// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import org.junit.jupiter.api.Test;

class SwerveModuleIOCTREConfigurationTest {
  private static final double kBackRightExpectedOffsetRotations = -0.052978515625;
  private static final double kCANcoderQuantizationStepRotations = 1.0 / 4096.0;

  @Test
  void acceptsExactMagnetOffsetMatch() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);

    assertTrue(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void acceptsOneRotationWrapEquivalentOffset() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations + 1.0);

    assertTrue(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void acceptsOneCANcoderQuantizationStepDifference() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations + kCANcoderQuantizationStepRotations);

    assertTrue(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void rejectsOffsetDifferenceLargerThanOneCANcoderQuantizationStep() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations
                + (2.0 * kCANcoderQuantizationStepRotations));

    assertFalse(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void rejectsSensorDirectionMismatch() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.Clockwise_Positive,
            kBackRightExpectedOffsetRotations);

    assertFalse(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  private static CANcoderConfiguration createEncoderConfiguration(
      SensorDirectionValue sensorDirection,
      double magnetOffsetRotations) {
    CANcoderConfiguration configuration = new CANcoderConfiguration();
    configuration.MagnetSensor =
        new MagnetSensorConfigs()
            .withSensorDirection(sensorDirection)
            .withMagnetOffset(magnetOffsetRotations);
    return configuration;
  }
}
