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
import com.ctre.phoenix6.Timestamp;
import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.IntakeConstants;
import java.util.Locale;

/**
 * Real Intake adapter for one Kraken X44 integrated Talon FX.
 *
 * <p>Scope: configuration, readback, truthful availability, measured feedback and one voltage
 * output path. {@link #requestIntake()} and {@link #stop()} issue neutral output only; nonzero
 * output is possible only through {@link #setVoltage(double)}, whose admission, bounds and duration
 * are owned by the Intake subsystem. The configured hardware peak output bounds every request.
 * Configuration failure is reported once and fails closed for the session without runtime retry.
 *
 * <p>Measured values are rotor quantities: no sensor-to-mechanism ratio is configured.
 *
 * <p>Freshness: each measured signal's Phoenix System timestamp (receive time, in the
 * {@link Utils#getCurrentTimeSeconds()} status-signal clock) must be valid, finite, not in the
 * future and no older than {@link #kMaximumMeasuredSampleAgeSeconds}. The published timestamp is
 * the oldest accepted sample, converted to robot FPGA time by subtracting its age from
 * {@link Timer#getFPGATimestamp()}, so the two clock domains are never compared directly.
 */
public final class IntakeIOReal implements IntakeIO {
  private static final double kCurrentLimitComparisonToleranceAmps = 1.0e-9;
  private static final double kPeakVoltageComparisonToleranceVolts = 1.0e-9;

  /** Update rate requested for the measured feedback signals on the roboRIO CAN bus. */
  private static final double kMeasuredSignalUpdateFrequencyHz = 50.0;

  /**
   * Accepted sample age in update periods: one expected period, one robot loop between a frame
   * and its non-blocking read, and margin for one missed frame plus CAN and loop jitter.
   */
  private static final double kMaximumMeasuredSampleAgeUpdatePeriods = 5.0;

  /** Oldest accepted measured sample age in seconds (100 ms at 50 Hz). */
  static final double kMaximumMeasuredSampleAgeSeconds =
      kMaximumMeasuredSampleAgeUpdatePeriods / kMeasuredSignalUpdateFrequencyHz;

  private final TalonFX motor = new TalonFX(IntakeConstants.kMotorCanId);
  private final NeutralOut neutralRequest = new NeutralOut();
  private final VoltageOut voltageRequest = new VoltageOut(0.0).withEnableFOC(false);
  private final StatusSignal<Voltage> supplyVoltageSignal = motor.getSupplyVoltage();
  private final StatusSignal<Angle> positionSignal = motor.getPosition();
  private final StatusSignal<AngularVelocity> velocitySignal = motor.getVelocity();
  private final StatusSignal<Current> statorCurrentSignal = motor.getStatorCurrent();
  private final StatusSignal<Current> supplyCurrentSignal = motor.getSupplyCurrent();
  private final StatusSignal<Voltage> motorVoltageSignal = motor.getMotorVoltage();
  private final BaseStatusSignal[] measuredSignals = {
    positionSignal, velocitySignal, statorCurrentSignal, supplyCurrentSignal, motorVoltageSignal
  };
  private final BaseStatusSignal[] refreshedSignals = {
    supplyVoltageSignal,
    positionSignal,
    velocitySignal,
    statorCurrentSignal,
    supplyCurrentSignal,
    motorVoltageSignal
  };
  private final boolean[] measuredTimestampValid = new boolean[measuredSignals.length];
  private final double[] measuredSampleTimesSeconds = new double[measuredSignals.length];
  private final boolean configurationHealthy;
  private final boolean measuredSignalRatesConfigured;

  /**
   * Creates the Intake motor, applies the approved configuration, verifies its readback and
   * requests the measured-signal update rate.
   */
  public IntakeIOReal() {
    boolean initialNeutralAccepted = attemptConstructionNeutral();

    TalonFXConfiguration expected = createConfiguration();
    StatusCode applyStatus = motor.getConfigurator().apply(expected);
    TalonFXConfiguration actual = new TalonFXConfiguration();
    StatusCode refreshStatus = motor.getConfigurator().refresh(actual);

    boolean configurationMatchesReadback =
        isConfigurationHealthy(applyStatus, refreshStatus, expected, actual);
    if (!configurationMatchesReadback) {
      reportConfigurationFailure(applyStatus, refreshStatus, expected, actual);
    }

    StatusCode signalRateStatus =
        BaseStatusSignal.setUpdateFrequencyForAll(
            kMeasuredSignalUpdateFrequencyHz, measuredSignals);
    measuredSignalRatesConfigured = signalRateStatus.isOK();
    if (!measuredSignalRatesConfigured) {
      reportSignalRateFailure(signalRateStatus);
    }

    boolean finalNeutralAccepted = attemptConstructionNeutral();
    configurationHealthy =
        isConstructionHealthy(
            configurationMatchesReadback, initialNeutralAccepted, finalNeutralAccepted);
  }

  /**
   * Refreshes all signals together, reports availability from configuration health, connection
   * from the supply-voltage signal and measurement validity from the status and sample age of
   * every measured signal.
   */
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    BaseStatusSignal.refreshAll(refreshedSignals);
    double statusSignalTimeSeconds = Utils.getCurrentTimeSeconds();
    double robotTimeSeconds = Timer.getFPGATimestamp();
    for (int index = 0; index < measuredSignals.length; index++) {
      Timestamp sampleTimestamp = measuredSignals[index].getAllTimestamps().getSystemTimestamp();
      measuredTimestampValid[index] = sampleTimestamp.isValid();
      measuredSampleTimesSeconds[index] = sampleTimestamp.getTime();
    }
    double oldestSampleAgeSeconds =
        oldestFreshSampleAgeSeconds(
            statusSignalTimeSeconds, measuredTimestampValid, measuredSampleTimesSeconds);

    boolean deviceConnected = supplyVoltageSignal.getStatus().isOK();
    fillInputs(inputs, configurationHealthy, deviceConnected);
    fillMeasurements(
        inputs,
        isMeasurementTrusted(
            configurationHealthy,
            measuredSignalRatesConfigured,
            deviceConnected,
            BaseStatusSignal.isAllGood(measuredSignals)),
        robotTimeSeconds - oldestSampleAgeSeconds,
        positionSignal.getValueAsDouble(),
        velocitySignal.getValueAsDouble(),
        statorCurrentSignal.getValueAsDouble(),
        supplyCurrentSignal.getValueAsDouble(),
        motorVoltageSignal.getValueAsDouble());
  }

  /**
   * Issues neutral output only; the semantic Intake request never produces motor motion.
   *
   * @throws IllegalStateException when the device rejects the neutral control request
   */
  @Override
  public void requestIntake() {
    requireNeutralAccepted(motor.setControl(neutralRequest), "requestIntake");
  }

  /**
   * Issues neutral output, which applies the configured brake neutral mode.
   *
   * @throws IllegalStateException when the device rejects the neutral control request, so a
   *     rejected request is never reported as successful neutralization
   */
  @Override
  public void stop() {
    requireNeutralAccepted(motor.setControl(neutralRequest), "stop");
  }

  /**
   * Applies one voltage request with FOC disabled; the configured hardware peak still bounds it.
   *
   * @param volts requested motor output voltage in volts; must be finite
   * @throws IllegalArgumentException when volts is not finite
   * @throws IllegalStateException when the device rejects the voltage control request; API
   *     acceptance is not proof of physical motion
   */
  @Override
  public void setVoltage(double volts) {
    if (!Double.isFinite(volts)) {
      throw new IllegalArgumentException("Intake voltage request must be finite");
    }
    requireVoltageAccepted(motor.setControl(voltageRequest.withOutput(volts)), volts);
  }

  /** Throws unless Phoenix returned exactly {@link StatusCode#OK} for a voltage control request. */
  static void requireVoltageAccepted(StatusCode status, double volts) {
    if (!status.isOK()) {
      throw new IllegalStateException(
          String.format(
              Locale.ROOT,
              "Intake TalonFX voltage request rejected: volts=%.3f, status=%s",
              volts,
              status));
    }
  }

  /**
   * Throws unless Phoenix returned exactly {@link StatusCode#OK} for a neutral control request.
   *
   * <p>Conservative policy: errors and every warning (for example ControlModeNotValid or
   * ControlModeNotSupportedYet) are rejections. An OK status confirms only that the request was
   * accepted by the API, not that the motor physically stopped.
   */
  static void requireNeutralAccepted(StatusCode status, String operation) {
    if (!status.isOK()) {
      throw new IllegalStateException(
          "Intake TalonFX neutral request rejected during " + operation + ": status=" + status);
    }
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
    configuration.Voltage.PeakForwardVoltage = IntakeConstants.kPeakOutputVolts;
    configuration.Voltage.PeakReverseVoltage = -IntakeConstants.kPeakOutputVolts;
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

  /** Compares the neutral mode, both current limits and both hardware peak output voltages. */
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
            <= kCurrentLimitComparisonToleranceAmps
        && Math.abs(expected.Voltage.PeakForwardVoltage - actual.Voltage.PeakForwardVoltage)
            <= kPeakVoltageComparisonToleranceVolts
        && Math.abs(expected.Voltage.PeakReverseVoltage - actual.Voltage.PeakReverseVoltage)
            <= kPeakVoltageComparisonToleranceVolts;
  }

  /** Maps health and connectivity so that connected always implies available. */
  static void fillInputs(
      IntakeIOInputs inputs, boolean configurationHealthy, boolean deviceConnected) {
    inputs.available = configurationHealthy;
    inputs.connected = configurationHealthy && deviceConnected;
  }

  /**
   * Returns true only when the configuration is healthy, the measured-signal rates were applied,
   * the device is connected and every measured signal refreshed with an OK status this cycle.
   */
  static boolean isMeasurementTrusted(
      boolean configurationHealthy,
      boolean measuredSignalRatesConfigured,
      boolean deviceConnected,
      boolean measuredSignalsOk) {
    return configurationHealthy
        && measuredSignalRatesConfigured
        && deviceConnected
        && measuredSignalsOk;
  }

  /**
   * Returns the age in seconds of one sample, or NaN when its timestamp is invalid, non-finite,
   * later than the current status-signal time or older than the accepted maximum age.
   *
   * <p>Both times come from the same status-signal clock, so no tolerance for negative age is
   * needed; a negative age means the clock is inconsistent and the sample is rejected.
   */
  static double freshSampleAgeSeconds(
      boolean timestampValid, double sampleTimeSeconds, double statusSignalTimeSeconds) {
    if (!timestampValid
        || !Double.isFinite(sampleTimeSeconds)
        || !Double.isFinite(statusSignalTimeSeconds)) {
      return Double.NaN;
    }
    double ageSeconds = statusSignalTimeSeconds - sampleTimeSeconds;
    if (ageSeconds < 0.0 || ageSeconds > kMaximumMeasuredSampleAgeSeconds) {
      return Double.NaN;
    }
    return ageSeconds;
  }

  /**
   * Returns the age of the oldest sample when every sample is fresh, or NaN when any sample is
   * missing, stale or inconsistent.
   */
  static double oldestFreshSampleAgeSeconds(
      double statusSignalTimeSeconds, boolean[] timestampValid, double[] sampleTimesSeconds) {
    if (timestampValid.length == 0 || timestampValid.length != sampleTimesSeconds.length) {
      return Double.NaN;
    }
    double oldestAgeSeconds = 0.0;
    for (int index = 0; index < timestampValid.length; index++) {
      double ageSeconds =
          freshSampleAgeSeconds(
              timestampValid[index], sampleTimesSeconds[index], statusSignalTimeSeconds);
      if (Double.isNaN(ageSeconds)) {
        return Double.NaN;
      }
      oldestAgeSeconds = Math.max(oldestAgeSeconds, ageSeconds);
    }
    return oldestAgeSeconds;
  }

  /**
   * Copies measured rotor values and their timestamp only when they are trusted and finite;
   * otherwise marks the measurement invalid and replaces every value and the timestamp with NaN
   * so no stale or fabricated value remains.
   */
  static void fillMeasurements(
      IntakeIOInputs inputs,
      boolean measurementTrusted,
      double timestampSeconds,
      double motorPositionRotations,
      double motorVelocityRotationsPerSecond,
      double statorCurrentAmps,
      double supplyCurrentAmps,
      double appliedVolts) {
    boolean measurementValid =
        measurementTrusted
            && Double.isFinite(timestampSeconds)
            && Double.isFinite(motorPositionRotations)
            && Double.isFinite(motorVelocityRotationsPerSecond)
            && Double.isFinite(statorCurrentAmps)
            && Double.isFinite(supplyCurrentAmps)
            && Double.isFinite(appliedVolts);
    inputs.measurementValid = measurementValid;
    inputs.timestampSeconds = measurementValid ? timestampSeconds : Double.NaN;
    inputs.motorPositionRotations = measurementValid ? motorPositionRotations : Double.NaN;
    inputs.motorVelocityRotationsPerSecond =
        measurementValid ? motorVelocityRotationsPerSecond : Double.NaN;
    inputs.statorCurrentAmps = measurementValid ? statorCurrentAmps : Double.NaN;
    inputs.supplyCurrentAmps = measurementValid ? supplyCurrentAmps : Double.NaN;
    inputs.appliedVolts = measurementValid ? appliedVolts : Double.NaN;
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
                + "expectedSupply=%.3f A (enabled=%b), actualSupply=%.3f A (enabled=%b), "
                + "expectedPeak=+%.3f/%.3f V, actualPeak=+%.3f/%.3f V",
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
            actual.CurrentLimits.SupplyCurrentLimitEnable,
            expected.Voltage.PeakForwardVoltage,
            expected.Voltage.PeakReverseVoltage,
            actual.Voltage.PeakForwardVoltage,
            actual.Voltage.PeakReverseVoltage),
        false);
  }

  /**
   * Returns true only when the configuration readback matched and both construction-time neutral
   * requests were accepted; otherwise the adapter reports itself unavailable (fail closed).
   */
  static boolean isConstructionHealthy(
      boolean configurationMatchesReadback,
      boolean initialNeutralAccepted,
      boolean finalNeutralAccepted) {
    return configurationMatchesReadback && initialNeutralAccepted && finalNeutralAccepted;
  }

  /** Attempts neutral output during construction without throwing; fails closed on rejection. */
  private boolean attemptConstructionNeutral() {
    try {
      stop();
      return true;
    } catch (IllegalStateException rejected) {
      DriverStation.reportError(
          rejected.getMessage() + "; Intake unavailable until restart", false);
      return false;
    }
  }

  private static void reportSignalRateFailure(StatusCode signalRateStatus) {
    DriverStation.reportError(
        String.format(
            Locale.ROOT,
            "Intake TalonFX measured-signal update rate not applied; "
                + "Intake measurements invalid until restart: status=%s",
            signalRateStatus),
        false);
  }
}
