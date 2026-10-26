// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import java.util.Objects;
import java.util.function.DoubleSupplier;

/**
 * Owns the vendor-neutral Intake capability, requested state, measured feedback, sticky fault
 * latch, and safe-stop call path.
 *
 * <p>Once latched, the fault rejects every later Intake request for the rest of the robot-code
 * session. Nothing in this class clears it; recovery requires a controlled robot-code restart.
 * {@link #stop()} never throws and stays available after a fault.
 *
 * <p>Nonzero output is possible only through two admitted paths, each requiring a healthy,
 * connected Intake, a valid (fresh) measurement, no latched fault and an in-bounds voltage:
 * {@link #startCommissioningVoltage(double)} in Test-enabled mode, and
 * {@link #startManual(ManualDirection)} in Teleop-enabled mode. Every periodic cycle re-checks the
 * active path's conditions (and the commissioning time budget) and stops output when any of them
 * fails; a stopped path never restarts by itself. The legacy {@link #requestIntake()} stays
 * neutral. These checks run in the scheduler loop; Driver Station Disable/E-stop remains the
 * physical backstop.
 */
public final class IntakeSubsystem extends SubsystemBase {
  /** Direction of a Teleop manual Intake request; the subsystem owns the approved voltages. */
  public enum ManualDirection {
    FORWARD,
    REVERSE
  }

  private final IntakeIO intakeIO;
  private final DoubleSupplier clockSeconds;
  private final IntakeIOInputs inputs = new IntakeIOInputs();
  private RequestedState requestedState = RequestedState.STOPPED;
  private boolean faultLatched;
  private boolean faultReported;
  private boolean stopFailureReported;
  private boolean commissioningActive;
  private boolean manualActive;
  private double commissioningStartSeconds = Double.NaN;
  private IntakeObservation latestObservation =
      new IntakeObservation(false, false, RequestedState.STOPPED);

  /** Creates the Intake owner with exactly one injected IO implementation. */
  public IntakeSubsystem(IntakeIO intakeIO) {
    this(intakeIO, Timer::getFPGATimestamp);
  }

  /**
   * Creates the Intake owner with an explicit monotonic clock for the commissioning time budget.
   *
   * @param intakeIO the single injected IO implementation
   * @param clockSeconds monotonic robot time source in seconds
   */
  public IntakeSubsystem(IntakeIO intakeIO, DoubleSupplier clockSeconds) {
    this.intakeIO = Objects.requireNonNull(intakeIO, "intakeIO");
    this.clockSeconds = Objects.requireNonNull(clockSeconds, "clockSeconds");
  }

  /**
   * Refreshes IO, atomically replaces the immutable Intake observation and stops an active
   * commissioning or manual output whose admission conditions or time budget no longer hold.
   */
  @Override
  public void periodic() {
    intakeIO.updateInputs(inputs);
    updateObservation();
    enforceCommissioningBounds();
    enforceManualAdmission();
  }

  /**
   * Records Intake software intent before forwarding the semantic request to IO.
   *
   * <p>A latched fault rejects the request and keeps the requested state STOPPED. An IO request
   * failure latches the fault and attempts a safe stop instead of propagating.
   */
  public void requestIntake() {
    commissioningActive = false;
    manualActive = false;
    if (faultLatched) {
      return;
    }
    requestedState = RequestedState.INTAKE_REQUESTED;
    updateObservation();
    try {
      intakeIO.requestIntake();
    } catch (RuntimeException failure) {
      latchFault("Intake request failed: " + failure);
    }
  }

  /**
   * Records stopped software intent before immediately forwarding safe stop to IO.
   *
   * <p>Never throws. An IO stop failure latches the fault and is reported once; it is not treated
   * as proof that the hardware stopped.
   */
  public void stop() {
    commissioningActive = false;
    manualActive = false;
    requestedState = RequestedState.STOPPED;
    try {
      updateObservation();
    } catch (RuntimeException observationFailure) {
      // Only invalid IO inputs can fail here; periodic() surfaces the same failure through the
      // scheduler-fatal path. The stop request below must still be attempted.
    }
    try {
      intakeIO.stop();
    } catch (RuntimeException failure) {
      faultLatched = true;
      if (!stopFailureReported) {
        stopFailureReported = true;
        DriverStation.reportError(
            "Intake stop request failed; neutral output not confirmed; "
                + "Intake requests inhibited until robot-code restart: "
                + failure,
            false);
      }
    }
    try {
      updateObservation();
    } catch (RuntimeException observationFailure) {
      // The previous observation remains published; the stop attempt has already been made.
    }
  }

  /**
   * Latches the session fault after a scheduler-fatal or command failure, then attempts a safe
   * stop. Repeated calls are safe and report only once.
   */
  public void latchFault() {
    latchFault("scheduler-fatal or command failure");
  }

  /**
   * Starts one Test-mode commissioning voltage output when every admission condition holds.
   *
   * <p>Rejected requests stop the Intake and return false. An IO rejection of the voltage request
   * latches the session fault and attempts a safe stop. A true result means the request was
   * accepted by the software path, not that the motor physically moved.
   *
   * @param volts requested output in volts; nonzero, finite and within the approved magnitude
   * @return true when the commissioning output was admitted and accepted by IO
   */
  public boolean startCommissioningVoltage(double volts) {
    if (!isCommissioningVoltageAllowed(volts) || !commissioningConditionsHold()) {
      stop();
      return false;
    }
    manualActive = false;
    commissioningActive = true;
    commissioningStartSeconds = clockSeconds.getAsDouble();
    requestedState = RequestedState.COMMISSIONING_REQUESTED;
    updateObservation();
    try {
      intakeIO.setVoltage(volts);
    } catch (RuntimeException failure) {
      latchFault("Intake commissioning voltage request failed: " + failure);
      return false;
    }
    return true;
  }

  /** Returns true while an admitted commissioning output has not been stopped. */
  public boolean isCommissioningActive() {
    return commissioningActive;
  }

  /** Returns true only for a finite, nonzero voltage within the approved commissioning magnitude. */
  static boolean isCommissioningVoltageAllowed(double volts) {
    return Double.isFinite(volts)
        && volts != 0.0
        && Math.abs(volts) <= IntakeConstants.kCommissioningVoltageVolts;
  }

  /**
   * Starts one Teleop manual voltage output in the given direction when every admission condition
   * holds. Intended only for the held-button manual command.
   *
   * <p>Rejected requests stop the Intake and return false. An IO rejection of the voltage request
   * latches the session fault and attempts a safe stop. A true result means the request was
   * accepted by the software path, not that the motor physically moved.
   *
   * @param direction requested manual direction
   * @return true when the manual output was admitted and accepted by IO
   */
  public boolean startManual(ManualDirection direction) {
    ManualDirection acceptedDirection = Objects.requireNonNull(direction, "direction");
    double volts = manualVoltage(acceptedDirection);
    if (!isManualVoltageAllowed(volts) || !manualConditionsHold()) {
      stop();
      return false;
    }
    commissioningActive = false;
    manualActive = true;
    requestedState =
        acceptedDirection == ManualDirection.FORWARD
            ? RequestedState.MANUAL_FORWARD_REQUESTED
            : RequestedState.MANUAL_REVERSE_REQUESTED;
    updateObservation();
    try {
      intakeIO.setVoltage(volts);
    } catch (RuntimeException failure) {
      latchFault("Intake manual voltage request failed: " + failure);
      return false;
    }
    return true;
  }

  /** Returns true while an admitted manual output has not been stopped. */
  public boolean isManualActive() {
    return manualActive;
  }

  /** Returns the approved manual voltage for a direction: forward positive, reverse negative. */
  static double manualVoltage(ManualDirection direction) {
    return direction == ManualDirection.FORWARD
        ? IntakeConstants.kManualForwardVoltageVolts
        : -IntakeConstants.kManualReverseVoltageMagnitudeVolts;
  }

  /** Returns true only for a finite, nonzero manual voltage within the approved range and peak. */
  static boolean isManualVoltageAllowed(double volts) {
    return Double.isFinite(volts)
        && volts != 0.0
        && volts <= IntakeConstants.kManualForwardVoltageVolts
        && volts >= -IntakeConstants.kManualReverseVoltageMagnitudeVolts
        && Math.abs(volts) <= IntakeConstants.kPeakOutputVolts;
  }

  /** Returns the latest immutable Intake observation. */
  public IntakeObservation getObservation() {
    return latestObservation;
  }

  private boolean commissioningConditionsHold() {
    return !faultLatched
        && DriverStation.isTestEnabled()
        && inputs.available
        && inputs.connected
        && inputs.measurementValid;
  }

  private boolean manualConditionsHold() {
    return !faultLatched
        && DriverStation.isTeleopEnabled()
        && inputs.available
        && inputs.connected
        && inputs.measurementValid;
  }

  private void enforceManualAdmission() {
    if (manualActive && !manualConditionsHold()) {
      stop();
    }
  }

  private void enforceCommissioningBounds() {
    if (!commissioningActive) {
      return;
    }
    double elapsedSeconds = clockSeconds.getAsDouble() - commissioningStartSeconds;
    // The negated comparison also stops output when the elapsed time is NaN.
    if (!commissioningConditionsHold()
        || !(elapsedSeconds < IntakeConstants.kCommissioningMaximumDurationSeconds)) {
      stop();
    }
  }

  private void latchFault(String reason) {
    faultLatched = true;
    if (!faultReported) {
      faultReported = true;
      DriverStation.reportError(
          "Intake fault latched (" + reason + "); Intake requests inhibited until robot-code restart",
          false);
    }
    stop();
  }

  private void updateObservation() {
    latestObservation =
        new IntakeObservation(
            inputs.available,
            inputs.connected,
            requestedState,
            faultLatched,
            inputs.measurementValid,
            inputs.timestampSeconds,
            inputs.motorPositionRotations,
            inputs.motorVelocityRotationsPerSecond,
            inputs.statorCurrentAmps,
            inputs.supplyCurrentAmps,
            inputs.appliedVolts);
  }
}
