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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.IntakeConstants;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.Arrays;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verifies the Real Intake configuration, health, input and measurement mapping without a
 * physical device.
 *
 * <p>Only configuration objects and status codes are used; no TalonFX is constructed.
 */
class IntakeIORealConfigurationTest {
  private static final StatusCode kFailureStatus = StatusCode.RxTimeout;
  private static final double kMismatchedCurrentLimitAmps = 120.0;
  private static final double kMismatchedPeakVoltageVolts = 16.0;
  private static final double kExpectedPeakOutputVolts = 6.0;
  private static final double kExpectedMaximumSampleAgeSeconds = 0.1;
  private static final double kPositionRotations = 12.5;
  private static final double kVelocityRotationsPerSecond = -3.25;
  private static final double kStatorCurrentAmps = 7.5;
  private static final double kSupplyCurrentAmps = 2.25;
  private static final double kAppliedVolts = 0.125;
  private static final int kMeasuredValueCount = 5;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }
  private static final double kTimestampSeconds = 42.5;
  private static final double kStatusSignalTimeSeconds = 100.0;
  private static final double kRobotTimeSeconds = 42.0;
  private static final double kRecentSampleAgeSeconds = 0.015;
  private static final double kOldestSampleAgeSeconds = 0.035;
  private static final double kAgeBeyondBoundarySeconds = 0.01;
  private static final double kRobotLoopPeriodSeconds = 0.02;
  private static final int kFramelessLoopsInsideWindow = 4;
  private static final int kOldestSignalIndex = 3;
  private static final double kTimeToleranceSeconds = 1.0e-9;

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
    assertEquals(IntakeConstants.kPeakOutputVolts, configuration.Voltage.PeakForwardVoltage);
    assertEquals(-IntakeConstants.kPeakOutputVolts, configuration.Voltage.PeakReverseVoltage);
  }

  @Test
  void hardwarePeakOutputIsSixVoltsAndAboveTheCommissioningVoltage() {
    assertEquals(kExpectedPeakOutputVolts, IntakeConstants.kPeakOutputVolts);
    assertTrue(IntakeConstants.kCommissioningVoltageVolts < IntakeConstants.kPeakOutputVolts);
  }

  @Test
  void unhealthyWhenPeakForwardVoltageReadbackDiffers() {
    assertMismatchIsUnhealthy(
        actual -> actual.Voltage.PeakForwardVoltage = kMismatchedPeakVoltageVolts);
  }

  @Test
  void unhealthyWhenPeakReverseVoltageReadbackDiffers() {
    assertMismatchIsUnhealthy(
        actual -> actual.Voltage.PeakReverseVoltage = -kMismatchedPeakVoltageVolts);
  }

  @Test
  void onlyOkVoltageStatusIsAccepted() {
    assertDoesNotThrow(
        () ->
            IntakeIOReal.requireVoltageAccepted(
                StatusCode.OK, IntakeConstants.kCommissioningVoltageVolts));
    for (StatusCode status : StatusCode.values()) {
      if (status != StatusCode.OK) {
        assertThrows(
            IllegalStateException.class,
            () ->
                IntakeIOReal.requireVoltageAccepted(
                    status, IntakeConstants.kCommissioningVoltageVolts),
            status.toString());
      }
    }
  }

  @Test
  void existingOneHundredMillisecondFreshnessWindowIsUnchanged() {
    assertEquals(
        kExpectedMaximumSampleAgeSeconds,
        IntakeIOReal.kMaximumMeasuredSampleAgeSeconds,
        kTimeToleranceSeconds);
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

  @Test
  void trustedFiniteMeasurementsAreCopiedInRotorUnits() {
    IntakeIOInputs inputs = new IntakeIOInputs();

    fillValidMeasurement(inputs);

    assertTrue(inputs.measurementValid);
    assertEquals(kTimestampSeconds, inputs.timestampSeconds);
    assertEquals(kPositionRotations, inputs.motorPositionRotations);
    assertEquals(kVelocityRotationsPerSecond, inputs.motorVelocityRotationsPerSecond);
    assertEquals(kStatorCurrentAmps, inputs.statorCurrentAmps);
    assertEquals(kSupplyCurrentAmps, inputs.supplyCurrentAmps);
    assertEquals(kAppliedVolts, inputs.appliedVolts);
  }

  @Test
  void trustedPhysicalZeroRemainsAValidMeasurement() {
    IntakeIOInputs inputs = new IntakeIOInputs();

    IntakeIOReal.fillMeasurements(inputs, true, kTimestampSeconds, 0.0, 0.0, 0.0, 0.0, 0.0);

    assertTrue(inputs.measurementValid);
    assertEquals(kTimestampSeconds, inputs.timestampSeconds);
    assertEquals(0.0, inputs.motorVelocityRotationsPerSecond);
    assertEquals(0.0, inputs.appliedVolts);
  }

  @Test
  void untrustedMeasurementIsInvalidAndClearsStaleValues() {
    IntakeIOInputs inputs = new IntakeIOInputs();
    fillValidMeasurement(inputs);

    IntakeIOReal.fillMeasurements(
        inputs,
        false,
        kTimestampSeconds,
        kPositionRotations,
        kVelocityRotationsPerSecond,
        kStatorCurrentAmps,
        kSupplyCurrentAmps,
        kAppliedVolts);

    assertMeasurementInvalid(inputs);
  }

  @Test
  void anyNonFiniteValueInvalidatesTheWholeMeasurement() {
    double[] nonFiniteValues = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    for (double nonFinite : nonFiniteValues) {
      for (int invalidIndex = 0; invalidIndex < kMeasuredValueCount; invalidIndex++) {
        double[] values = {
          kPositionRotations,
          kVelocityRotationsPerSecond,
          kStatorCurrentAmps,
          kSupplyCurrentAmps,
          kAppliedVolts
        };
        values[invalidIndex] = nonFinite;
        IntakeIOInputs inputs = new IntakeIOInputs();

        IntakeIOReal.fillMeasurements(
            inputs, true, kTimestampSeconds, values[0], values[1], values[2], values[3], values[4]);

        assertMeasurementInvalid(inputs);
      }
    }
  }

  @Test
  void nonFiniteMeasurementTimestampInvalidatesAndClearsTheMeasurement() {
    double[] nonFiniteTimestamps = {
      Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
    };
    for (double timestampSeconds : nonFiniteTimestamps) {
      IntakeIOInputs inputs = new IntakeIOInputs();
      fillValidMeasurement(inputs);

      IntakeIOReal.fillMeasurements(
          inputs,
          true,
          timestampSeconds,
          kPositionRotations,
          kVelocityRotationsPerSecond,
          kStatorCurrentAmps,
          kSupplyCurrentAmps,
          kAppliedVolts);

      assertMeasurementInvalid(inputs);
    }
  }

  @Test
  void recentValidSampleReportsItsAge() {
    assertEquals(
        kRecentSampleAgeSeconds,
        IntakeIOReal.freshSampleAgeSeconds(
            true, kStatusSignalTimeSeconds - kRecentSampleAgeSeconds, kStatusSignalTimeSeconds),
        kTimeToleranceSeconds);
  }

  @Test
  void sampleAtTheAcceptedAgeBoundaryIsFresh() {
    // Doubling and the subtraction below are exact, so the age equals the boundary exactly.
    double boundarySampleTime = IntakeIOReal.kMaximumMeasuredSampleAgeSeconds;
    double statusSignalTime = 2.0 * IntakeIOReal.kMaximumMeasuredSampleAgeSeconds;

    assertEquals(
        IntakeIOReal.kMaximumMeasuredSampleAgeSeconds,
        IntakeIOReal.freshSampleAgeSeconds(true, boundarySampleTime, statusSignalTime));
  }

  @Test
  void sampleOlderThanTheAcceptedAgeIsStaleDespiteAnOkStatus() {
    double staleSampleTime =
        kStatusSignalTimeSeconds
            - IntakeIOReal.kMaximumMeasuredSampleAgeSeconds
            - kAgeBeyondBoundarySeconds;

    assertTrue(
        Double.isNaN(
            IntakeIOReal.freshSampleAgeSeconds(true, staleSampleTime, kStatusSignalTimeSeconds)));
    assertTrue(IntakeIOReal.isMeasurementTrusted(true, true, true, true));
  }

  @Test
  void missingOrInvalidTimestampIsRejected() {
    assertTrue(
        Double.isNaN(
            IntakeIOReal.freshSampleAgeSeconds(
                false, kStatusSignalTimeSeconds - kRecentSampleAgeSeconds,
                kStatusSignalTimeSeconds)));
  }

  @Test
  void nonFiniteSampleOrCurrentTimeIsRejected() {
    double[] nonFiniteTimes = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY};
    for (double nonFinite : nonFiniteTimes) {
      assertTrue(
          Double.isNaN(
              IntakeIOReal.freshSampleAgeSeconds(true, nonFinite, kStatusSignalTimeSeconds)));
      assertTrue(
          Double.isNaN(
              IntakeIOReal.freshSampleAgeSeconds(
                  true, kStatusSignalTimeSeconds - kRecentSampleAgeSeconds, nonFinite)));
    }
  }

  @Test
  void futureSampleTimestampIsRejectedAsClockInconsistency() {
    assertTrue(
        Double.isNaN(
            IntakeIOReal.freshSampleAgeSeconds(
                true, kStatusSignalTimeSeconds + kAgeBeyondBoundarySeconds,
                kStatusSignalTimeSeconds)));
  }

  @Test
  void oldestFreshSampleAgeIsTheMaximumAgeOfAllFiveSignals() {
    boolean[] timestampValid = allValidTimestamps();
    double[] sampleTimes = recentSampleTimes();
    sampleTimes[kOldestSignalIndex] = kStatusSignalTimeSeconds - kOldestSampleAgeSeconds;

    assertEquals(
        kOldestSampleAgeSeconds,
        IntakeIOReal.oldestFreshSampleAgeSeconds(
            kStatusSignalTimeSeconds, timestampValid, sampleTimes),
        kTimeToleranceSeconds);
  }

  @Test
  void oneStaleOrInvalidSignalAmongFiveInvalidatesTheGroup() {
    for (int staleIndex = 0; staleIndex < kMeasuredValueCount; staleIndex++) {
      double[] staleTimes = recentSampleTimes();
      staleTimes[staleIndex] =
          kStatusSignalTimeSeconds
              - IntakeIOReal.kMaximumMeasuredSampleAgeSeconds
              - kAgeBeyondBoundarySeconds;
      boolean[] invalidTimestamps = allValidTimestamps();
      invalidTimestamps[staleIndex] = false;

      assertTrue(
          Double.isNaN(
              IntakeIOReal.oldestFreshSampleAgeSeconds(
                  kStatusSignalTimeSeconds, allValidTimestamps(), staleTimes)));
      assertTrue(
          Double.isNaN(
              IntakeIOReal.oldestFreshSampleAgeSeconds(
                  kStatusSignalTimeSeconds, invalidTimestamps, recentSampleTimes())));
    }
  }

  @Test
  void repeatedReadsWithoutNewFramesStayValidOnlyInsideTheWindow() {
    double frameTime = kStatusSignalTimeSeconds;

    for (int loop = 0; loop <= kFramelessLoopsInsideWindow; loop++) {
      double readTime = frameTime + loop * kRobotLoopPeriodSeconds;
      assertFalse(
          Double.isNaN(IntakeIOReal.freshSampleAgeSeconds(true, frameTime, readTime)),
          "loop " + loop);
    }
    double readAfterWindow =
        frameTime + IntakeIOReal.kMaximumMeasuredSampleAgeSeconds + kRobotLoopPeriodSeconds;
    assertTrue(Double.isNaN(IntakeIOReal.freshSampleAgeSeconds(true, frameTime, readAfterWindow)));
  }

  @Test
  void staleGroupAgeProducesAnInvalidMeasurementWithNaNTimestamp() {
    double[] staleTimes = recentSampleTimes();
    staleTimes[kOldestSignalIndex] =
        kStatusSignalTimeSeconds
            - IntakeIOReal.kMaximumMeasuredSampleAgeSeconds
            - kAgeBeyondBoundarySeconds;
    double oldestAge =
        IntakeIOReal.oldestFreshSampleAgeSeconds(
            kStatusSignalTimeSeconds, allValidTimestamps(), staleTimes);
    IntakeIOInputs inputs = new IntakeIOInputs();
    fillValidMeasurement(inputs);

    IntakeIOReal.fillMeasurements(
        inputs,
        true,
        kRobotTimeSeconds - oldestAge,
        kPositionRotations,
        kVelocityRotationsPerSecond,
        kStatorCurrentAmps,
        kSupplyCurrentAmps,
        kAppliedVolts);

    assertMeasurementInvalid(inputs);
  }

  @Test
  void acceptedGroupTimestampIsRobotTimeMinusTheOldestSampleAge() {
    double[] sampleTimes = recentSampleTimes();
    sampleTimes[kOldestSignalIndex] = kStatusSignalTimeSeconds - kOldestSampleAgeSeconds;
    double oldestAge =
        IntakeIOReal.oldestFreshSampleAgeSeconds(
            kStatusSignalTimeSeconds, allValidTimestamps(), sampleTimes);
    IntakeIOInputs inputs = new IntakeIOInputs();

    IntakeIOReal.fillMeasurements(
        inputs,
        true,
        kRobotTimeSeconds - oldestAge,
        kPositionRotations,
        kVelocityRotationsPerSecond,
        kStatorCurrentAmps,
        kSupplyCurrentAmps,
        kAppliedVolts);

    assertTrue(inputs.measurementValid);
    assertEquals(
        kRobotTimeSeconds - kOldestSampleAgeSeconds, inputs.timestampSeconds, kTimeToleranceSeconds);
  }

  @Test
  void measurementIsTrustedOnlyWhenEveryConditionHolds() {
    boolean[] values = {false, true};
    for (boolean configurationHealthy : values) {
      for (boolean signalRatesConfigured : values) {
        for (boolean deviceConnected : values) {
          for (boolean signalsOk : values) {
            assertEquals(
                configurationHealthy && signalRatesConfigured && deviceConnected && signalsOk,
                IntakeIOReal.isMeasurementTrusted(
                    configurationHealthy, signalRatesConfigured, deviceConnected, signalsOk));
          }
        }
      }
    }
  }

  @Test
  void everyMeasurementMappingSatisfiesTheObservationInvariant() {
    boolean[] values = {false, true};
    double[] timestamps = {kTimestampSeconds, Double.NaN};
    for (boolean configurationHealthy : values) {
      for (boolean signalRatesConfigured : values) {
        for (boolean deviceConnected : values) {
          for (boolean signalsOk : values) {
            for (double timestampSeconds : timestamps) {
              IntakeIOInputs inputs = new IntakeIOInputs();
              IntakeIOReal.fillInputs(inputs, configurationHealthy, deviceConnected);
              IntakeIOReal.fillMeasurements(
                  inputs,
                  IntakeIOReal.isMeasurementTrusted(
                      configurationHealthy, signalRatesConfigured, deviceConnected, signalsOk),
                  timestampSeconds,
                  kPositionRotations,
                  kVelocityRotationsPerSecond,
                  kStatorCurrentAmps,
                  kSupplyCurrentAmps,
                  kAppliedVolts);

              assertDoesNotThrow(
                  () ->
                      new IntakeObservation(
                          inputs.available,
                          inputs.connected,
                          RequestedState.STOPPED,
                          false,
                          inputs.measurementValid,
                          inputs.timestampSeconds,
                          inputs.motorPositionRotations,
                          inputs.motorVelocityRotationsPerSecond,
                          inputs.statorCurrentAmps,
                          inputs.supplyCurrentAmps,
                          inputs.appliedVolts));
            }
          }
        }
      }
    }
  }

  @Test
  void onlyOkNeutralStatusIsAccepted() {
    assertDoesNotThrow(() -> IntakeIOReal.requireNeutralAccepted(StatusCode.OK, "stop"));
    assertDoesNotThrow(
        () -> IntakeIOReal.requireNeutralAccepted(StatusCode.OK, "requestIntake"));
  }

  @Test
  void everyNonOkNeutralStatusIsRejectedIncludingWarnings() {
    StatusCode[] rejectedStatuses = {
      kFailureStatus,
      StatusCode.TxFailed,
      StatusCode.ControlModeNotValid,
      StatusCode.ControlModeNotSupportedYet,
      StatusCode.GeneralWarning
    };
    for (StatusCode status : rejectedStatuses) {
      assertThrows(
          IllegalStateException.class,
          () -> IntakeIOReal.requireNeutralAccepted(status, "stop"),
          status.toString());
    }
    for (StatusCode status : StatusCode.values()) {
      if (status != StatusCode.OK) {
        assertThrows(
            IllegalStateException.class,
            () -> IntakeIOReal.requireNeutralAccepted(status, "requestIntake"),
            status.toString());
      }
    }
  }

  @Test
  void runtimeNeutralRejectionReachesIntakeFaultHandling() {
    IntakeSubsystem subsystem =
        new IntakeSubsystem(new ClassifyingIntakeIO(StatusCode.ControlModeNotValid));
    try {
      assertDoesNotThrow(subsystem::stop);
      assertDoesNotThrow(subsystem::requestIntake);

      assertTrue(subsystem.getObservation().faultLatched());
      assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    } finally {
      CommandScheduler.getInstance().unregisterSubsystem(subsystem);
    }
  }

  @Test
  void acceptedRuntimeNeutralPreservesNormalRequestAndStop() {
    IntakeSubsystem subsystem = new IntakeSubsystem(new ClassifyingIntakeIO(StatusCode.OK));
    try {
      subsystem.requestIntake();
      assertEquals(RequestedState.INTAKE_REQUESTED, subsystem.getObservation().requestedState());
      subsystem.stop();

      assertFalse(subsystem.getObservation().faultLatched());
      assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    } finally {
      CommandScheduler.getInstance().unregisterSubsystem(subsystem);
    }
  }

  @Test
  void constructionNeutralRejectionFailsClosed() {
    boolean[] values = {false, true};
    for (boolean configurationMatches : values) {
      for (boolean initialNeutralAccepted : values) {
        for (boolean finalNeutralAccepted : values) {
          boolean healthy =
              IntakeIOReal.isConstructionHealthy(
                  configurationMatches, initialNeutralAccepted, finalNeutralAccepted);
          assertEquals(
              configurationMatches && initialNeutralAccepted && finalNeutralAccepted, healthy);

          IntakeIOInputs inputs = new IntakeIOInputs();
          IntakeIOReal.fillInputs(inputs, healthy, true);
          assertEquals(healthy, inputs.available);
          assertEquals(healthy, inputs.connected);
        }
      }
    }
  }

  @Test
  void rejectedNeutralStatusThrowsInsteadOfClaimingNeutralization() {
    IllegalStateException rejected =
        assertThrows(
            IllegalStateException.class,
            () -> IntakeIOReal.requireNeutralAccepted(kFailureStatus, "stop"));

    assertTrue(rejected.getMessage().contains("stop"));
    assertTrue(rejected.getMessage().contains(kFailureStatus.toString()));
  }

  @Test
  void freshInputsStartWithAnInvalidNaNMeasurement() {
    assertMeasurementInvalid(new IntakeIOInputs());
  }

  private static void fillValidMeasurement(IntakeIOInputs inputs) {
    IntakeIOReal.fillMeasurements(
        inputs,
        true,
        kTimestampSeconds,
        kPositionRotations,
        kVelocityRotationsPerSecond,
        kStatorCurrentAmps,
        kSupplyCurrentAmps,
        kAppliedVolts);
  }

  private static boolean[] allValidTimestamps() {
    boolean[] timestampValid = new boolean[kMeasuredValueCount];
    Arrays.fill(timestampValid, true);
    return timestampValid;
  }

  private static double[] recentSampleTimes() {
    double[] sampleTimes = new double[kMeasuredValueCount];
    Arrays.fill(sampleTimes, kStatusSignalTimeSeconds - kRecentSampleAgeSeconds);
    return sampleTimes;
  }

  private static void assertMeasurementInvalid(IntakeIOInputs inputs) {
    assertFalse(inputs.measurementValid);
    assertTrue(Double.isNaN(inputs.timestampSeconds));
    assertTrue(Double.isNaN(inputs.motorPositionRotations));
    assertTrue(Double.isNaN(inputs.motorVelocityRotationsPerSecond));
    assertTrue(Double.isNaN(inputs.statorCurrentAmps));
    assertTrue(Double.isNaN(inputs.supplyCurrentAmps));
    assertTrue(Double.isNaN(inputs.appliedVolts));
  }

  private static void assertMismatchIsUnhealthy(Consumer<TalonFXConfiguration> mismatch) {
    TalonFXConfiguration expected = IntakeIOReal.createConfiguration();
    TalonFXConfiguration actual = IntakeIOReal.createConfiguration();
    mismatch.accept(actual);

    assertFalse(IntakeIOReal.isConfigurationHealthy(StatusCode.OK, StatusCode.OK, expected, actual));
  }

  /** Routes every neutral request through the production StatusCode classification. */
  private static final class ClassifyingIntakeIO implements IntakeIO {
    private final StatusCode neutralStatus;

    private ClassifyingIntakeIO(StatusCode neutralStatus) {
      this.neutralStatus = neutralStatus;
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {}

    @Override
    public void requestIntake() {
      IntakeIOReal.requireNeutralAccepted(neutralStatus, "requestIntake");
    }

    @Override
    public void stop() {
      IntakeIOReal.requireNeutralAccepted(neutralStatus, "stop");
    }
  }
}
