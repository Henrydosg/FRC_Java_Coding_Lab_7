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

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.signals.MagnetHealthValue;
import org.junit.jupiter.api.Test;

/** Verifies absolute-encoder measurement-validity classification without physical devices. */
class SwerveModuleMeasurementValidityTest {
  @Test
  void greenAndOrangeMagnetHealthWithOkStatusAreUsableMeasurements() {
    assertTrue(
        SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(
            StatusCode.OK, MagnetHealthValue.Magnet_Green));
    assertTrue(
        SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(
            StatusCode.OK, MagnetHealthValue.Magnet_Orange));
  }

  @Test
  void redAndInvalidMagnetHealthAreRejectedEvenWithOkStatus() {
    assertFalse(
        SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(
            StatusCode.OK, MagnetHealthValue.Magnet_Red));
    assertFalse(
        SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(
            StatusCode.OK, MagnetHealthValue.Magnet_Invalid));
  }

  @Test
  void nonOkRefreshStatusIsRejectedForEveryMagnetHealth() {
    for (StatusCode failedStatus : new StatusCode[] {StatusCode.RxTimeout, StatusCode.EcuIsNotPresent}) {
      for (MagnetHealthValue magnetHealth : MagnetHealthValue.values()) {
        assertFalse(
            SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(failedStatus, magnetHealth));
      }
    }
  }

  @Test
  void unknownStatusOrMagnetHealthIsRejected() {
    assertFalse(
        SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(null, MagnetHealthValue.Magnet_Green));
    assertFalse(SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(StatusCode.OK, null));
    assertFalse(
        SwerveModuleIOCTRE.magnetHealthAllowsMeasurement(
            StatusCode.OK, MagnetHealthValue.valueOf(Integer.MAX_VALUE)));
  }

  @Test
  void noopModuleNeverReportsAValidMeasurement() {
    SwerveModuleIO.SwerveModuleIOInputs inputs = new SwerveModuleIO.SwerveModuleIOInputs();
    inputs.encoderMeasurementValid = true;

    new SwerveModuleIONoop().updateInputs(inputs);

    assertFalse(inputs.encoderMeasurementValid);
  }

  @Test
  void measurementValidityDefaultsToFalseUntilEvidenceExists() {
    assertFalse(new SwerveModuleIO.SwerveModuleIOInputs().encoderMeasurementValid);
  }
}
