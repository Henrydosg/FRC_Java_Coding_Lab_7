// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import frc.robot.Constants.IntakeConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Verifies Intake state ownership, semantic forwarding, measured feedback, and immutable
 * snapshots.
 */
class IntakeSubsystemTest {
  private static final double kTimestampSeconds = 42.5;
  private static final double kPositionRotations = 12.5;
  private static final double kVelocityRotationsPerSecond = -3.25;
  private static final double kStatorCurrentAmps = 7.5;
  private static final double kSupplyCurrentAmps = 2.25;
  private static final double kAppliedVolts = 0.125;
  private static final double kSmallStepSeconds = 0.01;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @AfterEach
  void disableRobot() {
    setMode(false, false, false);
  }

  @Test
  void testEnabledValidMeasurementAdmitsPositiveAndNegativeCommissioningVoltage() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, true);

    assertTrue(rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));
    assertEquals(
        RequestedState.COMMISSIONING_REQUESTED, rig.subsystem.getObservation().requestedState());
    assertTrue(rig.subsystem.isCommissioningActive());
    rig.subsystem.stop();
    assertTrue(
        rig.subsystem.startCommissioningVoltage(-IntakeConstants.kCommissioningVoltageVolts));

    assertEquals(
        List.of(
            IntakeConstants.kCommissioningVoltageVolts,
            -IntakeConstants.kCommissioningVoltageVolts),
        rig.io.voltageRequests);
  }

  @Test
  void teleopAutonomousAndDisabledDenyCommissioningOutput() {
    boolean[][] modes = {{true, false, false}, {true, true, false}, {false, false, true}};
    for (boolean[] mode : modes) {
      CommissioningRig rig = new CommissioningRig();
      setMode(mode[0], mode[1], mode[2]);

      assertFalse(
          rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));

      assertTrue(rig.io.voltageRequests.isEmpty());
      assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
      assertEquals(1, rig.io.stopCount);
    }
  }

  @Test
  void invalidOrStaleMeasurementOrUnhealthyDeviceDeniesCommissioningOutput() {
    for (int variant = 0; variant < 3; variant++) {
      CommissioningRig rig = new CommissioningRig();
      if (variant == 0) {
        rig.io.makeMeasurementInvalid();
      } else if (variant == 1) {
        rig.io.connected = false;
        rig.io.makeMeasurementInvalid();
      } else {
        rig.io.available = false;
        rig.io.connected = false;
        rig.io.makeMeasurementInvalid();
      }
      rig.subsystem.periodic();
      setMode(true, false, true);

      assertFalse(
          rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));
      assertTrue(rig.io.voltageRequests.isEmpty());
    }
  }

  @Test
  void outOfBoundsVoltageIsDenied() {
    double[] rejected = {
      0.0,
      IntakeConstants.kCommissioningVoltageVolts + 0.5,
      -IntakeConstants.kPeakOutputVolts,
      Double.NaN,
      Double.POSITIVE_INFINITY
    };
    for (double volts : rejected) {
      CommissioningRig rig = new CommissioningRig();
      setMode(true, false, true);

      assertFalse(rig.subsystem.startCommissioningVoltage(volts));
      assertTrue(rig.io.voltageRequests.isEmpty());
    }
  }

  @Test
  void stickyFaultDeniesCommissioningOutputWithoutAutomaticRestart() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, true);
    rig.subsystem.latchFault();

    assertFalse(rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));
    rig.subsystem.periodic();
    assertFalse(rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));

    assertTrue(rig.io.voltageRequests.isEmpty());
    assertTrue(rig.subsystem.getObservation().faultLatched());
  }

  @Test
  void rejectedVoltageRequestLatchesFaultAndStops() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, true);
    rig.io.voltageFailure = new IllegalStateException("voltage request rejected");

    assertDoesNotThrow(
        () ->
            assertFalse(
                rig.subsystem.startCommissioningVoltage(
                    IntakeConstants.kCommissioningVoltageVolts)));

    assertTrue(rig.subsystem.getObservation().faultLatched());
    assertFalse(rig.subsystem.isCommissioningActive());
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
    assertEquals(1, rig.io.stopCount);
  }

  @Test
  void periodicStopsOutputAtTheMaximumDurationAndOnModeOrMeasurementLoss() {
    CommissioningRig timeout = new CommissioningRig();
    setMode(true, false, true);
    assertTrue(
        timeout.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));
    timeout.clock[0] = IntakeConstants.kCommissioningMaximumDurationSeconds - kSmallStepSeconds;
    timeout.subsystem.periodic();
    assertTrue(timeout.subsystem.isCommissioningActive());
    timeout.clock[0] = IntakeConstants.kCommissioningMaximumDurationSeconds;
    timeout.subsystem.periodic();
    assertFalse(timeout.subsystem.isCommissioningActive());
    assertEquals(RequestedState.STOPPED, timeout.subsystem.getObservation().requestedState());

    CommissioningRig modeChange = new CommissioningRig();
    assertTrue(
        modeChange.subsystem.startCommissioningVoltage(
            -IntakeConstants.kCommissioningVoltageVolts));
    setMode(true, false, false);
    modeChange.subsystem.periodic();
    assertFalse(modeChange.subsystem.isCommissioningActive());

    setMode(true, false, true);
    CommissioningRig staleFeedback = new CommissioningRig();
    assertTrue(
        staleFeedback.subsystem.startCommissioningVoltage(
            IntakeConstants.kCommissioningVoltageVolts));
    staleFeedback.io.makeMeasurementInvalid();
    staleFeedback.subsystem.periodic();
    assertFalse(staleFeedback.subsystem.isCommissioningActive());
  }

  @Test
  void teleopValidMeasurementAdmitsManualForwardAndReverse() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, false);

    assertTrue(rig.subsystem.startManual(IntakeSubsystem.ManualDirection.FORWARD));
    assertEquals(
        RequestedState.MANUAL_FORWARD_REQUESTED, rig.subsystem.getObservation().requestedState());
    assertTrue(rig.subsystem.startManual(IntakeSubsystem.ManualDirection.REVERSE));
    assertEquals(
        RequestedState.MANUAL_REVERSE_REQUESTED, rig.subsystem.getObservation().requestedState());

    assertEquals(
        List.of(
            IntakeConstants.kManualForwardVoltageVolts,
            -IntakeConstants.kManualReverseVoltageMagnitudeVolts),
        rig.io.voltageRequests);
  }

  @Test
  void disabledAutonomousAndTestDenyManualOutput() {
    boolean[][] modes = {{false, false, false}, {true, true, false}, {true, false, true}};
    for (boolean[] mode : modes) {
      CommissioningRig rig = new CommissioningRig();
      setMode(mode[0], mode[1], mode[2]);

      assertFalse(rig.subsystem.startManual(IntakeSubsystem.ManualDirection.FORWARD));
      assertFalse(rig.subsystem.startManual(IntakeSubsystem.ManualDirection.REVERSE));

      assertTrue(rig.io.voltageRequests.isEmpty());
      assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
    }
  }

  @Test
  void invalidFeedbackOrLatchedFaultDeniesManualOutput() {
    CommissioningRig invalid = new CommissioningRig();
    invalid.io.makeMeasurementInvalid();
    invalid.subsystem.periodic();
    setMode(true, false, false);
    assertFalse(invalid.subsystem.startManual(IntakeSubsystem.ManualDirection.FORWARD));

    CommissioningRig faulted = new CommissioningRig();
    faulted.subsystem.latchFault();
    assertFalse(faulted.subsystem.startManual(IntakeSubsystem.ManualDirection.REVERSE));

    assertTrue(invalid.io.voltageRequests.isEmpty());
    assertTrue(faulted.io.voltageRequests.isEmpty());
  }

  @Test
  void manualOutputRejectionLatchesFaultAndStops() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, false);
    rig.io.voltageFailure = new IllegalStateException("voltage request rejected");

    assertFalse(rig.subsystem.startManual(IntakeSubsystem.ManualDirection.FORWARD));

    assertTrue(rig.subsystem.getObservation().faultLatched());
    assertFalse(rig.subsystem.isManualActive());
    assertEquals(RequestedState.STOPPED, rig.subsystem.getObservation().requestedState());
  }

  @Test
  void periodicStopsManualOutputOnModeOrFeedbackLossWithoutRestart() {
    setMode(true, false, false);
    CommissioningRig modeLoss = new CommissioningRig();
    assertTrue(modeLoss.subsystem.startManual(IntakeSubsystem.ManualDirection.FORWARD));
    setMode(true, true, false);
    modeLoss.subsystem.periodic();
    setMode(true, false, false);
    modeLoss.subsystem.periodic();
    assertFalse(modeLoss.subsystem.isManualActive());
    assertEquals(1, modeLoss.io.voltageRequests.size());

    CommissioningRig feedbackLoss = new CommissioningRig();
    assertTrue(feedbackLoss.subsystem.startManual(IntakeSubsystem.ManualDirection.REVERSE));
    feedbackLoss.io.makeMeasurementInvalid();
    feedbackLoss.subsystem.periodic();
    assertFalse(feedbackLoss.subsystem.isManualActive());
    assertEquals(RequestedState.STOPPED, feedbackLoss.subsystem.getObservation().requestedState());
  }

  @Test
  void manualVoltagesStayWithinTheApprovedRangeAndPeak() {
    assertEquals(
        IntakeConstants.kManualForwardVoltageVolts,
        IntakeSubsystem.manualVoltage(IntakeSubsystem.ManualDirection.FORWARD));
    assertEquals(
        -IntakeConstants.kManualReverseVoltageMagnitudeVolts,
        IntakeSubsystem.manualVoltage(IntakeSubsystem.ManualDirection.REVERSE));
    assertTrue(IntakeSubsystem.isManualVoltageAllowed(IntakeConstants.kManualForwardVoltageVolts));
    assertFalse(IntakeSubsystem.isManualVoltageAllowed(0.0));
    assertFalse(IntakeSubsystem.isManualVoltageAllowed(IntakeConstants.kPeakOutputVolts));
    assertFalse(IntakeSubsystem.isManualVoltageAllowed(-IntakeConstants.kPeakOutputVolts));
    assertFalse(IntakeSubsystem.isManualVoltageAllowed(Double.NaN));
    assertTrue(IntakeConstants.kManualForwardVoltageVolts < IntakeConstants.kPeakOutputVolts);
  }

  @Test
  void commissioningStaysTestOnlyAndManualStaysTeleopOnly() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, false);
    assertFalse(rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));
    setMode(true, false, true);
    assertFalse(rig.subsystem.startManual(IntakeSubsystem.ManualDirection.FORWARD));
    assertTrue(rig.subsystem.startCommissioningVoltage(IntakeConstants.kCommissioningVoltageVolts));

    assertEquals(List.of(IntakeConstants.kCommissioningVoltageVolts), rig.io.voltageRequests);
  }

  @Test
  void legacyIntakeRequestStaysUnpoweredInTeleopWithValidFeedback() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, false);

    rig.subsystem.requestIntake();
    rig.subsystem.periodic();

    assertEquals(RequestedState.INTAKE_REQUESTED, rig.subsystem.getObservation().requestedState());
    assertEquals(1, rig.io.requestCount);
    assertTrue(rig.io.voltageRequests.isEmpty());
  }

  @Test
  void rightBumperIntakeRequestStaysUnpoweredEvenInTestMode() {
    CommissioningRig rig = new CommissioningRig();
    setMode(true, false, true);

    rig.subsystem.requestIntake();
    rig.subsystem.periodic();

    assertEquals(RequestedState.INTAKE_REQUESTED, rig.subsystem.getObservation().requestedState());
    assertEquals(1, rig.io.requestCount);
    assertTrue(rig.io.voltageRequests.isEmpty());
  }

  private static void setMode(boolean enabled, boolean autonomous, boolean test) {
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.setAutonomous(autonomous);
    DriverStationSim.setTest(test);
    DriverStationSim.notifyNewData();
  }

  /** Healthy, connected Intake with a valid measurement and a deterministic clock. */
  private static final class CommissioningRig {
    private final double[] clock = {0.0};
    private final RecordingIntakeIO io = new RecordingIntakeIO();
    private final IntakeSubsystem subsystem = new IntakeSubsystem(io, () -> clock[0]);

    private CommissioningRig() {
      io.available = true;
      io.connected = true;
      io.measurementValid = true;
      io.timestampSeconds = kTimestampSeconds;
      io.motorPositionRotations = 0.0;
      io.motorVelocityRotationsPerSecond = 0.0;
      io.statorCurrentAmps = 0.0;
      io.supplyCurrentAmps = 0.0;
      io.appliedVolts = 0.0;
      io.observationSupplier = subsystem::getObservation;
      subsystem.periodic();
    }
  }

  @Test
  void latchedFaultRejectsLaterAndSameCycleRequestsAndStopsOutput() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    subsystem.requestIntake();
    assertEquals(1, intakeIO.requestCount);

    subsystem.latchFault();
    subsystem.requestIntake();

    assertTrue(subsystem.getObservation().faultLatched());
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(1, intakeIO.requestCount);
    assertEquals(1, intakeIO.stopCount);
  }

  @Test
  void repeatedLatchIsSafeAndStopRemainsCallable() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;

    assertDoesNotThrow(subsystem::latchFault);
    assertDoesNotThrow(subsystem::latchFault);
    assertDoesNotThrow(subsystem::stop);

    assertTrue(subsystem.getObservation().faultLatched());
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(3, intakeIO.stopCount);
  }

  @Test
  void latchSurvivesDisableEnableAndPeriodicWithoutAutomaticRestart() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    intakeIO.available = true;
    intakeIO.connected = true;
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    subsystem.latchFault();
    try {
      DriverStationSim.setEnabled(false);
      DriverStationSim.notifyNewData();
      subsystem.periodic();
      DriverStationSim.setEnabled(true);
      DriverStationSim.notifyNewData();
      subsystem.periodic();

      subsystem.requestIntake();
      subsystem.periodic();

      assertTrue(subsystem.getObservation().faultLatched());
      assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
      assertEquals(0, intakeIO.requestCount);
    } finally {
      DriverStationSim.setEnabled(false);
      DriverStationSim.notifyNewData();
    }
  }

  @Test
  void ioStopFailureLatchesFaultWithoutThrowing() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    intakeIO.stopFailure = new IllegalStateException("neutral request rejected");

    assertDoesNotThrow(subsystem::stop);
    assertDoesNotThrow(subsystem::stop);
    subsystem.requestIntake();

    assertTrue(subsystem.getObservation().faultLatched());
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(2, intakeIO.stopCount);
    assertEquals(0, intakeIO.requestCount);
  }

  @Test
  void ioRequestFailureLatchesFaultAndAttemptsStopWithoutThrowing() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    intakeIO.requestFailure = new IllegalStateException("neutral request rejected");

    assertDoesNotThrow(subsystem::requestIntake);

    assertTrue(subsystem.getObservation().faultLatched());
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(1, intakeIO.requestCount);
    assertEquals(1, intakeIO.stopCount);
  }

  @Test
  void latchPreservesMeasuredFeedbackAndTimestamp() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    intakeIO.available = true;
    intakeIO.connected = true;
    intakeIO.measurementValid = true;
    intakeIO.timestampSeconds = kTimestampSeconds;
    intakeIO.motorPositionRotations = kPositionRotations;
    intakeIO.motorVelocityRotationsPerSecond = kVelocityRotationsPerSecond;
    intakeIO.statorCurrentAmps = kStatorCurrentAmps;
    intakeIO.supplyCurrentAmps = kSupplyCurrentAmps;
    intakeIO.appliedVolts = kAppliedVolts;
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    subsystem.periodic();

    subsystem.latchFault();

    assertTrue(subsystem.getObservation().faultLatched());
    assertTrue(subsystem.getObservation().measurementValid());
    assertEquals(kTimestampSeconds, subsystem.getObservation().timestampSeconds());
    assertEquals(kAppliedVolts, subsystem.getObservation().appliedVolts());
  }

  @Test
  void beginsStoppedWithoutAutomaticallyRequestingIntake() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);

    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
    assertEquals(0, intakeIO.requestCount);
    assertEquals(0, intakeIO.stopCount);
  }

  @Test
  void requestIntakeUpdatesIntentBeforeForwardingSemanticRequest() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;

    subsystem.requestIntake();

    assertEquals(1, intakeIO.requestCount);
    assertEquals(RequestedState.INTAKE_REQUESTED, intakeIO.stateObservedOnRequest);
    assertEquals(RequestedState.INTAKE_REQUESTED, subsystem.getObservation().requestedState());
  }

  @Test
  void stopUpdatesIntentBeforeImmediatelyForwardingStop() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    intakeIO.observationSupplier = subsystem::getObservation;
    subsystem.requestIntake();

    subsystem.stop();

    assertEquals(1, intakeIO.stopCount);
    assertEquals(RequestedState.STOPPED, intakeIO.stateObservedOnStop);
    assertEquals(RequestedState.STOPPED, subsystem.getObservation().requestedState());
  }

  @Test
  void periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    intakeIO.available = true;
    intakeIO.connected = true;
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);
    subsystem.requestIntake();

    subsystem.periodic();
    IntakeObservation first = subsystem.getObservation();

    assertTrue(first.available());
    assertTrue(first.connected());
    assertEquals(RequestedState.INTAKE_REQUESTED, first.requestedState());

    intakeIO.available = false;
    intakeIO.connected = false;
    subsystem.periodic();
    IntakeObservation second = subsystem.getObservation();

    assertNotSame(first, second);
    assertTrue(first.available());
    assertTrue(first.connected());
    assertFalse(second.available());
    assertFalse(second.connected());
  }

  @Test
  void periodicCopiesMeasuredFeedbackAndKeepsInvalidMeasurementsNaN() {
    RecordingIntakeIO intakeIO = new RecordingIntakeIO();
    intakeIO.available = true;
    intakeIO.connected = true;
    intakeIO.measurementValid = true;
    intakeIO.timestampSeconds = kTimestampSeconds;
    intakeIO.motorPositionRotations = kPositionRotations;
    intakeIO.motorVelocityRotationsPerSecond = kVelocityRotationsPerSecond;
    intakeIO.statorCurrentAmps = kStatorCurrentAmps;
    intakeIO.supplyCurrentAmps = kSupplyCurrentAmps;
    intakeIO.appliedVolts = kAppliedVolts;
    IntakeSubsystem subsystem = new IntakeSubsystem(intakeIO);

    subsystem.periodic();
    IntakeObservation measured = subsystem.getObservation();

    assertTrue(measured.measurementValid());
    assertEquals(kTimestampSeconds, measured.timestampSeconds());
    assertEquals(kPositionRotations, measured.motorPositionRotations());
    assertEquals(kVelocityRotationsPerSecond, measured.motorVelocityRotationsPerSecond());
    assertEquals(kStatorCurrentAmps, measured.statorCurrentAmps());
    assertEquals(kSupplyCurrentAmps, measured.supplyCurrentAmps());
    assertEquals(kAppliedVolts, measured.appliedVolts());
    assertEquals(RequestedState.STOPPED, measured.requestedState());

    intakeIO.measurementValid = false;
    intakeIO.timestampSeconds = Double.NaN;
    intakeIO.motorPositionRotations = Double.NaN;
    intakeIO.motorVelocityRotationsPerSecond = Double.NaN;
    intakeIO.statorCurrentAmps = Double.NaN;
    intakeIO.supplyCurrentAmps = Double.NaN;
    intakeIO.appliedVolts = Double.NaN;
    subsystem.periodic();
    IntakeObservation invalid = subsystem.getObservation();

    assertFalse(invalid.measurementValid());
    assertTrue(Double.isNaN(invalid.timestampSeconds()));
    assertTrue(Double.isNaN(invalid.motorPositionRotations()));
    assertTrue(Double.isNaN(invalid.appliedVolts()));
    assertEquals(kAppliedVolts, measured.appliedVolts());
    assertEquals(kTimestampSeconds, measured.timestampSeconds());
  }

  private static final class RecordingIntakeIO implements IntakeIO {
    private boolean available;
    private boolean connected;
    private boolean measurementValid;
    private double timestampSeconds = Double.NaN;
    private double motorPositionRotations = Double.NaN;
    private double motorVelocityRotationsPerSecond = Double.NaN;
    private double statorCurrentAmps = Double.NaN;
    private double supplyCurrentAmps = Double.NaN;
    private double appliedVolts = Double.NaN;
    private int requestCount;
    private int stopCount;
    private RuntimeException requestFailure;
    private RuntimeException stopFailure;
    private RuntimeException voltageFailure;
    private final List<Double> voltageRequests = new ArrayList<>();
    private Supplier<IntakeObservation> observationSupplier;
    private RequestedState stateObservedOnRequest;
    private RequestedState stateObservedOnStop;

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
      inputs.measurementValid = measurementValid;
      inputs.timestampSeconds = timestampSeconds;
      inputs.motorPositionRotations = motorPositionRotations;
      inputs.motorVelocityRotationsPerSecond = motorVelocityRotationsPerSecond;
      inputs.statorCurrentAmps = statorCurrentAmps;
      inputs.supplyCurrentAmps = supplyCurrentAmps;
      inputs.appliedVolts = appliedVolts;
    }

    @Override
    public void requestIntake() {
      requestCount++;
      if (observationSupplier != null) {
        stateObservedOnRequest = observationSupplier.get().requestedState();
      }
      if (requestFailure != null) {
        throw requestFailure;
      }
    }

    @Override
    public void stop() {
      stopCount++;
      stateObservedOnStop = observationSupplier.get().requestedState();
      if (stopFailure != null) {
        throw stopFailure;
      }
    }

    @Override
    public void setVoltage(double volts) {
      voltageRequests.add(volts);
      if (voltageFailure != null) {
        throw voltageFailure;
      }
    }

    private void makeMeasurementInvalid() {
      measurementValid = false;
      timestampSeconds = Double.NaN;
      motorPositionRotations = Double.NaN;
      motorVelocityRotationsPerSecond = Double.NaN;
      statorCurrentAmps = Double.NaN;
      supplyCurrentAmps = Double.NaN;
      appliedVolts = Double.NaN;
    }
  }
}
