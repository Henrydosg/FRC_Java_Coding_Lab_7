// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.Constants.IntakeConstants;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

/**
 * Verifies the Real Intake configuration, health and input mapping without a physical device.
 *
 * <p>Only configuration objects and status codes are used; no TalonFX is constructed.
 */
class IntakeIORealConfigurationTest {
  private static final StatusCode kFailureStatus = StatusCode.RxTimeout;
  private static final double kMismatchedCurrentLimitAmps = 120.0;

  @Test
  void createsOnlyTheApprovedConfiguration() {
    TalonFXConfiguration configuration = IntakeIOReal.createConfiguration();

    assertEquals(NeutralModeValue.Brake, configuration.MotorOutput.NeutralMode);
    assertEquals(
        IntakeConstants.kStatorCurrentLimitAmps, configuration.CurrentLimits.StatorCurrentLimit);
    assertTrue(configuration.CurrentLimits.StatorCurrentLimitEnable);
    assertEquals(
        IntakeConstants.kSupplyCurrentLimitAmps, configuration.CurrentLimits.SupplyCurrentLimit);
    assertTrue(configuration.CurrentLimits.SupplyCurrentLimitEnable);
  }

  @Test
  void leavesInversionAndFeedbackAtPhoenixDefaults() {
    TalonFXConfiguration configuration = IntakeIOReal.createConfiguration();
    TalonFXConfiguration defaults = new TalonFXConfiguration();

    assertEquals(defaults.MotorOutput.Inverted, configuration.MotorOutput.Inverted);
    assertEquals(InvertedValue.CounterClockwise_Positive, configuration.MotorOutput.Inverted);
    assertEquals(
        defaults.Feedback.SensorToMechanismRatio, configuration.Feedback.SensorToMechanismRatio);
    assertEquals(defaults.Slot0.kP, configuration.Slot0.kP);
  }

  @Test
  void healthyOnlyWhenApplyRefreshAndReadbackAllSucceed() {
    TalonFXConfiguration expected = IntakeIOReal.createConfiguration();

    assertTrue(
        IntakeIOReal.isConfigurationHealthy(
            StatusCode.OK, StatusCode.OK, expected, IntakeIOReal.createConfiguration()));
  }

  @Test
  void unhealthyWhenApplyFails() {
    TalonFXConfiguration expected = IntakeIOReal.createConfiguration();

    assertFalse(
        IntakeIOReal.isConfigurationHealthy(
            kFailureStatus, StatusCode.OK, expected, IntakeIOReal.createConfiguration()));
  }

  @Test
  void unhealthyWhenReadbackFails() {
    TalonFXConfiguration expected = IntakeIOReal.createConfiguration();

    assertFalse(
        IntakeIOReal.isConfigurationHealthy(
            StatusCode.OK, kFailureStatus, expected, IntakeIOReal.createConfiguration()));
  }

  @Test
  void unhealthyWhenNeutralModeReadbackDiffers() {
    assertMismatchIsUnhealthy(actual -> actual.MotorOutput.NeutralMode = NeutralModeValue.Coast);
  }

  @Test
  void unhealthyWhenStatorLimitReadbackDiffers() {
    assertMismatchIsUnhealthy(
        actual -> actual.CurrentLimits.StatorCurrentLimit = kMismatchedCurrentLimitAmps);
  }

  @Test
  void unhealthyWhenStatorLimitIsDisabledOnReadback() {
    assertMismatchIsUnhealthy(actual -> actual.CurrentLimits.StatorCurrentLimitEnable = false);
  }

  @Test
  void unhealthyWhenSupplyLimitReadbackDiffers() {
    assertMismatchIsUnhealthy(
        actual -> actual.CurrentLimits.SupplyCurrentLimit = kMismatchedCurrentLimitAmps);
  }

  @Test
  void unhealthyWhenSupplyLimitIsDisabledOnReadback() {
    assertMismatchIsUnhealthy(actual -> actual.CurrentLimits.SupplyCurrentLimitEnable = false);
  }

  @Test
  void healthyAndConnectedReportsAvailableAndConnected() {
    IntakeIOInputs inputs = new IntakeIOInputs();

    IntakeIOReal.fillInputs(inputs, true, true);

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
  }

  @Test
  void healthyButDisconnectedReportsAvailableOnly() {
    IntakeIOInputs inputs = new IntakeIOInputs();

    IntakeIOReal.fillInputs(inputs, true, false);

    assertTrue(inputs.available);
    assertFalse(inputs.connected);
  }

  @Test
  void unhealthyConfigurationFailsClosedRegardlessOfConnectivity() {
    IntakeIOInputs connectedInputs = new IntakeIOInputs();
    IntakeIOInputs disconnectedInputs = new IntakeIOInputs();
    connectedInputs.available = true;
    connectedInputs.connected = true;

    IntakeIOReal.fillInputs(connectedInputs, false, true);
    IntakeIOReal.fillInputs(disconnectedInputs, false, false);

    assertFalse(connectedInputs.available);
    assertFalse(connectedInputs.connected);
    assertFalse(disconnectedInputs.available);
    assertFalse(disconnectedInputs.connected);
  }

  @Test
  void everyInputMappingSatisfiesTheObservationInvariant() {
    boolean[] values = {false, true};
    for (boolean configurationHealthy : values) {
      for (boolean deviceConnected : values) {
        IntakeIOInputs inputs = new IntakeIOInputs();
        IntakeIOReal.fillInputs(inputs, configurationHealthy, deviceConnected);

        assertDoesNotThrow(
            () -> new IntakeObservation(inputs.available, inputs.connected, RequestedState.STOPPED));
      }
    }
  }

  private static void assertMismatchIsUnhealthy(Consumer<TalonFXConfiguration> mismatch) {
    TalonFXConfiguration expected = IntakeIOReal.createConfiguration();
    TalonFXConfiguration actual = IntakeIOReal.createConfiguration();
    mismatch.accept(actual);

    assertFalse(IntakeIOReal.isConfigurationHealthy(StatusCode.OK, StatusCode.OK, expected, actual));
  }
}
