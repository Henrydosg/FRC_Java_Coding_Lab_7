// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants.IntakeConstants;
import java.util.Locale;

/**
 * Real Intake adapter for one Kraken X44 integrated Talon FX.
 *
 * <p>M01_L02 scope: configuration, readback and truthful availability only. Both semantic
 * requests issue neutral output, so this adapter never produces motor motion. Configuration
 * failure is reported once and fails closed for the session without runtime retry.
 */
public final class IntakeIOReal implements IntakeIO {
  private static final double kCurrentLimitComparisonToleranceAmps = 1.0e-9;

  private final TalonFX motor = new TalonFX(IntakeConstants.kMotorCanId);
  private final NeutralOut neutralRequest = new NeutralOut();
  private final StatusSignal<Voltage> supplyVoltageSignal = motor.getSupplyVoltage();
  private final boolean configurationHealthy;

  /** Creates the Intake motor, applies the approved configuration and verifies its readback. */
  public IntakeIOReal() {
    stop();

    TalonFXConfiguration expected = createConfiguration();
    StatusCode applyStatus = motor.getConfigurator().apply(expected);
    TalonFXConfiguration actual = new TalonFXConfiguration();
    StatusCode refreshStatus = motor.getConfigurator().refresh(actual);

    configurationHealthy = isConfigurationHealthy(applyStatus, refreshStatus, expected, actual);
    if (!configurationHealthy) {
      reportConfigurationFailure(applyStatus, refreshStatus, expected, actual);
    }

    stop();
  }

  /** Reports availability from configuration health and connection from a fresh signal refresh. */
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    StatusCode refreshStatus = BaseStatusSignal.refreshAll(supplyVoltageSignal);
    fillInputs(inputs, configurationHealthy, refreshStatus.isOK());
  }

  /** Issues neutral output only; M01_L02 does not authorize Intake motion. */
  @Override
  public void requestIntake() {
    motor.setControl(neutralRequest);
  }

  /** Issues neutral output, which applies the configured brake neutral mode. */
  @Override
  public void stop() {
    motor.setControl(neutralRequest);
  }

  /** Builds the approved Intake configuration from vendor-neutral constants. */
  static TalonFXConfiguration createConfiguration() {
    TalonFXConfiguration configuration = new TalonFXConfiguration();
    configuration.MotorOutput.NeutralMode =
        IntakeConstants.kBrakeWhenNeutral ? NeutralModeValue.Brake : NeutralModeValue.Coast;
    configuration.CurrentLimits.StatorCurrentLimit = IntakeConstants.kStatorCurrentLimitAmps;
    configuration.CurrentLimits.StatorCurrentLimitEnable =
        IntakeConstants.kStatorCurrentLimitEnabled;
    configuration.CurrentLimits.SupplyCurrentLimit = IntakeConstants.kSupplyCurrentLimitAmps;
    configuration.CurrentLimits.SupplyCurrentLimitEnable =
        IntakeConstants.kSupplyCurrentLimitEnabled;
    return configuration;
  }

  /** Returns true only when apply and readback succeeded and every checked value matches. */
  static boolean isConfigurationHealthy(
      StatusCode applyStatus,
      StatusCode refreshStatus,
      TalonFXConfiguration expected,
      TalonFXConfiguration actual) {
    return applyStatus.isOK()
        && refreshStatus.isOK()
        && configurationMatches(expected, actual);
  }

  /** Compares the neutral mode and both current limits approved by the Design Lock. */
  static boolean configurationMatches(TalonFXConfiguration expected, TalonFXConfiguration actual) {
    return expected.MotorOutput.NeutralMode == actual.MotorOutput.NeutralMode
        && expected.CurrentLimits.StatorCurrentLimitEnable
            == actual.CurrentLimits.StatorCurrentLimitEnable
        && Math.abs(
                expected.CurrentLimits.StatorCurrentLimit
                    - actual.CurrentLimits.StatorCurrentLimit)
            <= kCurrentLimitComparisonToleranceAmps
        && expected.CurrentLimits.SupplyCurrentLimitEnable
            == actual.CurrentLimits.SupplyCurrentLimitEnable
        && Math.abs(
                expected.CurrentLimits.SupplyCurrentLimit
                    - actual.CurrentLimits.SupplyCurrentLimit)
            <= kCurrentLimitComparisonToleranceAmps;
  }

  /** Maps health and connectivity so that connected always implies available. */
  static void fillInputs(
      IntakeIOInputs inputs, boolean configurationHealthy, boolean deviceConnected) {
    inputs.available = configurationHealthy;
    inputs.connected = configurationHealthy && deviceConnected;
  }

  private static void reportConfigurationFailure(
      StatusCode applyStatus,
      StatusCode refreshStatus,
      TalonFXConfiguration expected,
      TalonFXConfiguration actual) {
    DriverStation.reportError(
        String.format(
            Locale.ROOT,
            "Intake TalonFX configuration unhealthy; Intake unavailable until restart: "
                + "apply=%s, refresh=%s, expectedNeutralMode=%s, actualNeutralMode=%s, "
                + "expectedStator=%.3f A (enabled=%b), actualStator=%.3f A (enabled=%b), "
                + "expectedSupply=%.3f A (enabled=%b), actualSupply=%.3f A (enabled=%b)",
            applyStatus,
            refreshStatus,
            expected.MotorOutput.NeutralMode,
            actual.MotorOutput.NeutralMode,
            expected.CurrentLimits.StatorCurrentLimit,
            expected.CurrentLimits.StatorCurrentLimitEnable,
            actual.CurrentLimits.StatorCurrentLimit,
            actual.CurrentLimits.StatorCurrentLimitEnable,
            expected.CurrentLimits.SupplyCurrentLimit,
            expected.CurrentLimits.SupplyCurrentLimitEnable,
            actual.CurrentLimits.SupplyCurrentLimit,
            actual.CurrentLimits.SupplyCurrentLimitEnable),
        false);
  }
}
