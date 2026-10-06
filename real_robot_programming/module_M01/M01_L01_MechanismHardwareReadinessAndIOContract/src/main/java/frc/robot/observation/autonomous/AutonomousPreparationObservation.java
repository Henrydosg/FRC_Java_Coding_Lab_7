// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.autonomous;

import java.util.Objects;

/** Immutable, vendor-neutral diagnostic view of autonomous preparation and execution. */
public record AutonomousPreparationObservation(
    State state,
    boolean ready,
    long attemptId,
    Reason reason,
    Routine routine,
    AllianceIdentity alliance,
    String fieldVariant,
    String pathIdentity,
    boolean contextConsumed,
    boolean stale,
    boolean headingReferenceValid,
    boolean poseAvailable,
    double translationErrorMeters,
    double headingErrorRadians,
    boolean measuredSpeedsAvailable,
    boolean pathValid,
    boolean autoBuilderConfigured,
    boolean adapterFatalFaulted,
    String firstFatalReason,
    ReturnedCommand returnedCommand,
    boolean running) {

  /** Preparation and execution lifecycle states. */
  public enum State {
    UNPREPARED,
    VALIDATING,
    NOT_READY,
    READY,
    STALE,
    CONSUMED,
    RUNNING,
    HOLDING,
    COMPLETE,
    INTERRUPTED,
    FAULTED
  }

  /** Stable diagnostic reason codes. */
  public enum Reason {
    NONE,
    PREPARATION_REQUESTED,
    PREPARE_REQUIRES_DISABLED,
    UNKNOWN_ROUTINE,
    SAFE_STOP_SELECTED,
    ALLIANCE_UNAVAILABLE,
    HEADING_CAPTURE_REJECTED,
    START_CONTEXT_UNAVAILABLE,
    RESET_REJECTED,
    POSE_UNAVAILABLE,
    MEASURED_SPEEDS_UNAVAILABLE,
    POSE_MISMATCH,
    PATH_PREFLIGHT_FAILED,
    AUTOBUILDER_NOT_CONFIGURED,
    MISSING_READINESS,
    READINESS_CONSUMED,
    ALLIANCE_CHANGED,
    ROUTINE_CHANGED,
    FIELD_PROVENANCE_CHANGED,
    PATH_PROVENANCE_CHANGED,
    HEADING_PROVENANCE_CHANGED,
    COMMAND_CONSTRUCTION_FAILED,
    ATOMIC_CLAIM_FAILED,
    COMMAND_RUNNING,
    COMMAND_COMPLETED,
    COMMAND_INTERRUPTED,
    MODE_LOSS,
    EXECUTION_TIMEOUT,
    EXECUTION_INPUT_UNAVAILABLE,
    FATAL_ADAPTER_FAULT
  }

  /** Vendor-neutral routine identity used only for diagnostics. */
  public enum Routine {
    UNKNOWN,
    SAFE_STOP,
    ONE_METER_PATH,
    ONE_METER_WITH_EVENT
  }

  /** Vendor-neutral alliance identity used only for diagnostics. */
  public enum AllianceIdentity {
    UNKNOWN,
    BLUE,
    RED
  }

  /** Classification of the latest command returned to {@code Robot}. */
  public enum ReturnedCommand {
    NONE,
    SAFE_STOP,
    SAFE_STOP_FALLBACK,
    ONE_METER_PATH,
    ONE_METER_WITH_EVENT
  }

  /** Validates and defensively normalizes one immutable diagnostic sample. */
  public AutonomousPreparationObservation {
    state = Objects.requireNonNull(state, "state");
    reason = Objects.requireNonNull(reason, "reason");
    routine = Objects.requireNonNull(routine, "routine");
    alliance = Objects.requireNonNull(alliance, "alliance");
    fieldVariant = Objects.requireNonNull(fieldVariant, "fieldVariant");
    pathIdentity = Objects.requireNonNull(pathIdentity, "pathIdentity");
    firstFatalReason = Objects.requireNonNull(firstFatalReason, "firstFatalReason");
    returnedCommand = Objects.requireNonNull(returnedCommand, "returnedCommand");
    if (attemptId < 0L) {
      throw new IllegalArgumentException("attemptId must be nonnegative");
    }
    if (!Double.isFinite(translationErrorMeters) || translationErrorMeters < 0.0) {
      throw new IllegalArgumentException("translationErrorMeters must be finite and nonnegative");
    }
    if (!Double.isFinite(headingErrorRadians) || headingErrorRadians < 0.0) {
      throw new IllegalArgumentException("headingErrorRadians must be finite and nonnegative");
    }
  }

  /** Returns the initial diagnostic state before any preparation attempt. */
  public static AutonomousPreparationObservation unprepared(
      boolean autoBuilderConfigured, boolean adapterFatalFaulted, String firstFatalReason) {
    return new AutonomousPreparationObservation(
        State.UNPREPARED,
        false,
        0L,
        Reason.NONE,
        Routine.UNKNOWN,
        AllianceIdentity.UNKNOWN,
        "",
        "",
        false,
        false,
        false,
        false,
        0.0,
        0.0,
        false,
        false,
        autoBuilderConfigured,
        adapterFatalFaulted,
        Objects.requireNonNull(firstFatalReason, "firstFatalReason"),
        ReturnedCommand.NONE,
        false);
  }
}
