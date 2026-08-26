// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.observation.autonomous.AutonomousPreparationObservation;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.AllianceIdentity;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Reason;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.ReturnedCommand;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Routine;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.util.FieldAllianceTransform;
import java.util.Objects;
import java.util.Optional;

/** Owns the deterministic autonomous preparation, provenance, and execution lifecycle. */
public final class AutonomousPreparationCoordinator {
  /** Immutable preview used for Option 3 command construction and atomic claim. */
  public record PreparationClaim(
      long attemptId,
      AutonomousStartContext startContext,
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Alliance alliance,
      FieldVariant fieldVariant,
      String pathIdentity,
      long headingReferenceAttemptId) {
    public PreparationClaim {
      if (attemptId <= 0L || headingReferenceAttemptId != attemptId) {
        throw new IllegalArgumentException("claim attempt provenance is invalid");
      }
      startContext = Objects.requireNonNull(startContext, "startContext");
      routineId = Objects.requireNonNull(routineId, "routineId");
      alliance = Objects.requireNonNull(alliance, "alliance");
      fieldVariant = Objects.requireNonNull(fieldVariant, "fieldVariant");
      pathIdentity = Objects.requireNonNull(pathIdentity, "pathIdentity");
    }
  }

  /** Injectable preparation actions used to keep lifecycle tests deterministic. */
  interface PreparationActions {
    boolean isDisabled();

    boolean captureFieldHeadingReference();

    Pose2d canonicalStartingPose();

    boolean resetKnownFieldPose(Pose2d pose);

    AutoBuilderContractAdapter.PreflightResult preflight(AutonomousStartContext context);

    boolean isAutoBuilderConfigured();

    boolean isAdapterFaulted();

    String firstFatalReason();

    AutoBuilderContractAdapter.ExecutionOutcome executionOutcome();

    void latchStaticPreparationFault(String reason, Throwable failure);

    default void latchSchedulerBoundaryFault(RuntimeException failure) {
      latchStaticPreparationFault("scheduler boundary failure", failure);
    }

    default void stop() {}
  }

  private record PreparedAttempt(
      long attemptId,
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Alliance alliance,
      FieldVariant fieldVariant,
      String pathIdentity,
      long headingReferenceAttemptId,
      Optional<AutonomousStartContext> startContext) {}

  private record PendingAttempt(
      long attemptId,
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Alliance alliance,
      AutonomousStartContext startContext) {}

  private final PreparationActions actions;
  private final FieldVariant fieldVariant;
  private final String pathIdentity;

  private long latestAttemptId;
  private PendingAttempt pendingAttempt;
  private PreparedAttempt preparedAttempt;
  private AutonomousPreparationObservation latestObservation;

  /** Creates the production preparation lifecycle owner. */
  public AutonomousPreparationCoordinator(
      frc.robot.subsystems.SwerveSubsystem swerveSubsystem,
      PathPlannerTrajectoryAdapter pathPlannerTrajectoryAdapter,
      AutoBuilderContractAdapter autoBuilderContractAdapter,
      FieldVariant fieldVariant,
      String pathIdentity) {
    this(
        new PreparationActions() {
          @Override
          public boolean isDisabled() {
            return DriverStation.isDisabled();
          }

          @Override
          public boolean captureFieldHeadingReference() {
            return swerveSubsystem.captureFieldHeadingReference();
          }

          @Override
          public Pose2d canonicalStartingPose() {
            Trajectory trajectory = pathPlannerTrajectoryAdapter.createCanonicalTrajectory();
            return trajectory.getInitialPose();
          }

          @Override
          public boolean resetKnownFieldPose(Pose2d pose) {
            return swerveSubsystem.resetKnownFieldPose(pose);
          }

          @Override
          public AutoBuilderContractAdapter.PreflightResult preflight(
              AutonomousStartContext context) {
            return autoBuilderContractAdapter.preflight(context);
          }

          @Override
          public boolean isAutoBuilderConfigured() {
            return autoBuilderContractAdapter.isConfigured();
          }

          @Override
          public boolean isAdapterFaulted() {
            return autoBuilderContractAdapter.isFaulted();
          }

          @Override
          public String firstFatalReason() {
            return autoBuilderContractAdapter.firstFaultReason();
          }

          @Override
          public AutoBuilderContractAdapter.ExecutionOutcome executionOutcome() {
            return autoBuilderContractAdapter.executionOutcome();
          }

          @Override
          public void latchStaticPreparationFault(String reason, Throwable failure) {
            autoBuilderContractAdapter.latchStaticPreparationFault(reason, failure);
          }

          @Override
          public void latchSchedulerBoundaryFault(RuntimeException failure) {
            autoBuilderContractAdapter.latchSchedulerBoundaryFault(failure);
          }

          @Override
          public void stop() {
            swerveSubsystem.stop();
          }
        },
        fieldVariant,
        pathIdentity);
  }

  AutonomousPreparationCoordinator(
      PreparationActions actions, FieldVariant fieldVariant, String pathIdentity) {
    this.actions = Objects.requireNonNull(actions, "actions");
    this.fieldVariant = Objects.requireNonNull(fieldVariant, "fieldVariant");
    this.pathIdentity = Objects.requireNonNull(pathIdentity, "pathIdentity");
    latestObservation =
        AutonomousPreparationObservation.unprepared(
            actions.isAutoBuilderConfigured(),
            actions.isAdapterFaulted(),
            Objects.requireNonNull(actions.firstFatalReason(), "firstFatalReason"));
  }

  /** Executes one complete preparation attempt for deterministic non-scheduler callers. */
  public synchronized AutonomousPreparationObservation prepare(
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Optional<Alliance> selectedAlliance) {
    AutonomousPreparationObservation observation =
        beginPreparation(routineId, selectedAlliance);
    return observation.state() == State.VALIDATING
        ? completePreparation()
        : observation;
  }

  /** Starts one preparation attempt and captures its heading/start-context provenance. */
  public synchronized AutonomousPreparationObservation beginPreparation(
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Optional<Alliance> selectedAlliance) {
    Objects.requireNonNull(selectedAlliance, "selectedAlliance");
    if (actions.isAdapterFaulted()) {
      return transitionToFaulted(routineId, selectedAlliance);
    }

    long attemptId = ++latestAttemptId;
    pendingAttempt = null;
    preparedAttempt = null;
    Routine routine = toObservationRoutine(routineId);
    AllianceIdentity allianceIdentity = toObservationAlliance(selectedAlliance);
    latestObservation =
        observation(
            State.VALIDATING,
            false,
            attemptId,
            Reason.PREPARATION_REQUESTED,
            routine,
            allianceIdentity,
            false,
            false,
            false,
            false,
            0.0,
            0.0,
            false,
            false,
            latestObservation.returnedCommand(),
            false);

    if (!actions.isDisabled()) {
      return transitionNotReady(
          attemptId,
          Reason.PREPARE_REQUIRES_DISABLED,
          routine,
          allianceIdentity,
          false,
          false,
          0.0,
          0.0,
          false,
          false);
    }
    if (routineId == null) {
      return transitionNotReady(
          attemptId,
          Reason.UNKNOWN_ROUTINE,
          Routine.UNKNOWN,
          allianceIdentity,
          false,
          false,
          0.0,
          0.0,
          false,
          false);
    }
    if (routineId == AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP) {
      preparedAttempt =
          new PreparedAttempt(
              attemptId,
              routineId,
              selectedAlliance.orElse(null),
              fieldVariant,
              pathIdentity,
              attemptId,
              Optional.empty());
      latestObservation =
          observation(
              State.READY,
              true,
              attemptId,
              Reason.SAFE_STOP_SELECTED,
              Routine.SAFE_STOP,
              allianceIdentity,
              false,
              false,
              false,
              false,
              0.0,
              0.0,
              false,
              true,
              latestObservation.returnedCommand(),
              false);
      return latestObservation;
    }
    if (selectedAlliance.isEmpty()) {
      return transitionNotReady(
          attemptId,
          Reason.ALLIANCE_UNAVAILABLE,
          routine,
          AllianceIdentity.UNKNOWN,
          false,
          false,
          0.0,
          0.0,
          false,
          false);
    }
    if (!actions.captureFieldHeadingReference()) {
      return transitionNotReady(
          attemptId,
          Reason.HEADING_CAPTURE_REJECTED,
          routine,
          allianceIdentity,
          false,
          false,
          0.0,
          0.0,
          false,
          false);
    }

    AutonomousStartContext context;
    try {
      Pose2d executionStartPose =
          FieldAllianceTransform.fromCanonicalBluePose(
              actions.canonicalStartingPose(), fieldVariant, selectedAlliance.orElseThrow());
      context =
          new AutonomousStartContext(
              fieldVariant, selectedAlliance.orElseThrow(), executionStartPose);
    } catch (RuntimeException failure) {
      actions.latchStaticPreparationFault(
          "autonomous start-context construction failed", failure);
      return transitionToFaulted(routineId, selectedAlliance);
    }

    pendingAttempt =
        new PendingAttempt(
            attemptId,
            routineId,
            selectedAlliance.orElseThrow(),
            context);
    latestObservation =
        observation(
            State.VALIDATING,
            false,
            attemptId,
            Reason.PREPARATION_REQUESTED,
            routine,
            allianceIdentity,
            false,
            false,
            true,
            false,
            0.0,
            0.0,
            false,
            false,
            latestObservation.returnedCommand(),
            false);
    return latestObservation;
  }

  /** Completes the pending attempt after one refresh observes the new heading reference. */
  public synchronized AutonomousPreparationObservation completePreparation() {
    if (actions.isAdapterFaulted()) {
      return transitionToFaulted(
          pendingAttempt == null ? null : pendingAttempt.routineId(),
          pendingAttempt == null
              ? Optional.empty()
              : Optional.of(pendingAttempt.alliance()));
    }
    if (pendingAttempt == null || latestObservation.state() != State.VALIDATING) {
      return latestObservation;
    }

    PendingAttempt attempt = pendingAttempt;
    Routine routine = toObservationRoutine(attempt.routineId());
    AllianceIdentity allianceIdentity =
        toObservationAlliance(Optional.of(attempt.alliance()));
    if (!actions.isDisabled()) {
      return transitionNotReady(
          attempt.attemptId(),
          Reason.PREPARE_REQUIRES_DISABLED,
          routine,
          allianceIdentity,
          true,
          false,
          0.0,
          0.0,
          false,
          false);
    }

    if (!actions.resetKnownFieldPose(attempt.startContext().executionStartPose())) {
      return transitionNotReady(
          attempt.attemptId(),
          Reason.RESET_REJECTED,
          routine,
          allianceIdentity,
          true,
          false,
          0.0,
          0.0,
          false,
          false);
    }

    AutoBuilderContractAdapter.PreflightResult preflight =
        actions.preflight(attempt.startContext());
    if (preflight.status() == AutoBuilderContractAdapter.PreflightStatus.FAULTED
        || actions.isAdapterFaulted()) {
      return transitionToFaulted(
          attempt.routineId(), Optional.of(attempt.alliance()));
    }
    if (preflight.status() == AutoBuilderContractAdapter.PreflightStatus.NOT_READY) {
      return transitionNotReady(
          attempt.attemptId(),
          mapPreflightReason(preflight.reason()),
          routine,
          allianceIdentity,
          true,
          preflight.poseAvailable(),
          preflight.translationErrorMeters(),
          preflight.headingErrorRadians(),
          preflight.measuredSpeedsAvailable(),
          preflight.pathValid());
    }

    preparedAttempt =
        new PreparedAttempt(
            attempt.attemptId(),
            attempt.routineId(),
            attempt.alliance(),
            fieldVariant,
            pathIdentity,
            attempt.attemptId(),
            Optional.of(attempt.startContext()));
    pendingAttempt = null;
    latestObservation =
        observation(
            State.READY,
            true,
            attempt.attemptId(),
            Reason.NONE,
            routine,
            allianceIdentity,
            false,
            false,
            true,
            preflight.poseAvailable(),
            preflight.translationErrorMeters(),
            preflight.headingErrorRadians(),
            preflight.measuredSpeedsAvailable(),
            preflight.pathValid(),
            latestObservation.returnedCommand(),
            false);
    return latestObservation;
  }

  /** Revalidates READY provenance without consuming it. */
  public synchronized Optional<PreparationClaim> previewDrivingPreparation(
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Optional<Alliance> currentAlliance) {
    Objects.requireNonNull(currentAlliance, "currentAlliance");
    if (actions.isAdapterFaulted()) {
      transitionToFaulted(routineId, currentAlliance);
      return Optional.empty();
    }
    if (latestObservation.state() != State.READY || preparedAttempt == null) {
      if (latestObservation.state() != State.STALE
          && latestObservation.state() != State.FAULTED
          && latestObservation.state() != State.RUNNING) {
        Reason reason =
            latestObservation.contextConsumed()
                ? Reason.READINESS_CONSUMED
                : Reason.MISSING_READINESS;
        latestObservation =
            observation(
                State.NOT_READY,
                false,
                latestObservation.attemptId(),
                reason,
                toObservationRoutine(routineId),
                toObservationAlliance(currentAlliance),
                latestObservation.contextConsumed(),
                false,
                latestObservation.headingReferenceValid(),
                latestObservation.poseAvailable(),
                latestObservation.translationErrorMeters(),
                latestObservation.headingErrorRadians(),
                latestObservation.measuredSpeedsAvailable(),
                latestObservation.pathValid(),
                latestObservation.returnedCommand(),
                false);
      }
      return Optional.empty();
    }
    if (routineId != preparedAttempt.routineId()) {
      transitionStale(Reason.ROUTINE_CHANGED, routineId, currentAlliance);
      return Optional.empty();
    }
    if (currentAlliance.isEmpty()) {
      transitionStale(Reason.ALLIANCE_UNAVAILABLE, routineId, currentAlliance);
      return Optional.empty();
    }
    if (preparedAttempt.alliance() != currentAlliance.orElseThrow()) {
      transitionStale(Reason.ALLIANCE_CHANGED, routineId, currentAlliance);
      return Optional.empty();
    }
    if (preparedAttempt.fieldVariant() != fieldVariant) {
      transitionStale(Reason.FIELD_PROVENANCE_CHANGED, routineId, currentAlliance);
      return Optional.empty();
    }
    if (!preparedAttempt.pathIdentity().equals(pathIdentity)) {
      transitionStale(Reason.PATH_PROVENANCE_CHANGED, routineId, currentAlliance);
      return Optional.empty();
    }
    if (preparedAttempt.headingReferenceAttemptId() != preparedAttempt.attemptId()) {
      transitionStale(Reason.HEADING_PROVENANCE_CHANGED, routineId, currentAlliance);
      return Optional.empty();
    }
    if (preparedAttempt.startContext().isEmpty()) {
      transitionStale(Reason.MISSING_READINESS, routineId, currentAlliance);
      return Optional.empty();
    }
    return Optional.of(
        new PreparationClaim(
            preparedAttempt.attemptId(),
            preparedAttempt.startContext().orElseThrow(),
            preparedAttempt.routineId(),
            preparedAttempt.alliance(),
            preparedAttempt.fieldVariant(),
            preparedAttempt.pathIdentity(),
            preparedAttempt.headingReferenceAttemptId()));
  }

  /** Atomically consumes the exact preparation preview after command construction succeeds. */
  public synchronized boolean claim(
      PreparationClaim claim, Optional<Alliance> currentAlliance) {
    Objects.requireNonNull(claim, "claim");
    Objects.requireNonNull(currentAlliance, "currentAlliance");
    if (latestObservation.state() != State.READY
        || preparedAttempt == null
        || preparedAttempt.attemptId() != claim.attemptId()
        || preparedAttempt.routineId() != claim.routineId()
        || preparedAttempt.fieldVariant() != claim.fieldVariant()
        || !preparedAttempt.pathIdentity().equals(claim.pathIdentity())
        || preparedAttempt.headingReferenceAttemptId()
            != claim.headingReferenceAttemptId()
        || preparedAttempt.startContext().isEmpty()
        || !preparedAttempt.startContext().orElseThrow().equals(claim.startContext())
        || currentAlliance.isEmpty()
        || currentAlliance.orElseThrow() != claim.alliance()) {
      if (latestObservation.state() == State.READY) {
        transitionStale(Reason.ATOMIC_CLAIM_FAILED, claim.routineId(), currentAlliance);
      }
      return false;
    }

    latestObservation =
        observation(
            State.CONSUMED,
            false,
            claim.attemptId(),
            Reason.NONE,
            toObservationRoutine(claim.routineId()),
            toObservationAlliance(currentAlliance),
            true,
            false,
            true,
            latestObservation.poseAvailable(),
            latestObservation.translationErrorMeters(),
            latestObservation.headingErrorRadians(),
            latestObservation.measuredSpeedsAvailable(),
            latestObservation.pathValid(),
            latestObservation.returnedCommand(),
            false);
    return true;
  }

  /** Marks a non-driving routine selection without consuming driving readiness. */
  public synchronized void observeSafeStopSelection(Optional<Alliance> currentAlliance) {
    Objects.requireNonNull(currentAlliance, "currentAlliance");
    if (latestObservation.state() == State.READY
        && preparedAttempt != null
        && preparedAttempt.routineId()
            != AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP) {
      transitionStale(
          Reason.ROUTINE_CHANGED,
          AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP,
          currentAlliance);
    }
  }

  /** Records the command classification returned to {@code Robot}. */
  public synchronized void recordReturnedCommand(ReturnedCommand returnedCommand) {
    latestObservation =
        copyObservation(
            latestObservation.state(),
            latestObservation.ready(),
            latestObservation.reason(),
            Objects.requireNonNull(returnedCommand, "returnedCommand"),
            latestObservation.running());
  }

  /** Records a recoverable command-construction rejection without consuming READY. */
  public synchronized void recordRecoverableConstructionFailure() {
    if (latestObservation.state() == State.READY) {
      latestObservation =
          copyObservation(
              State.READY,
              true,
              Reason.COMMAND_CONSTRUCTION_FAILED,
              latestObservation.returnedCommand(),
              false);
    }
  }

  /** Records an impossible command/factory invariant as a process-latched fatal fault. */
  public synchronized void recordFatalInvariant(String reason) {
    actions.latchStaticPreparationFault(
        Objects.requireNonNull(reason, "reason"), new IllegalStateException(reason));
        transitionToFaulted(
        preparedAttempt == null ? null : preparedAttempt.routineId(),
        preparedAttempt == null
            ? Optional.empty()
            : Optional.ofNullable(preparedAttempt.alliance()));
  }

  /** Latches an unexpected Robot-level scheduler failure and publishes FAULTED. */
  public synchronized void recordSchedulerFatal(RuntimeException failure) {
    Objects.requireNonNull(failure, "failure");
    actions.latchSchedulerBoundaryFault(failure);
    try {
      actions.stop();
    } catch (RuntimeException ignored) {
      // The adapter's independent stop attempt remains the authoritative safety action.
    }
    transitionToFaulted(
        preparedAttempt == null ? null : preparedAttempt.routineId(),
        preparedAttempt == null
            ? Optional.empty()
            : Optional.ofNullable(preparedAttempt.alliance()));
  }

  /**
   * Composes one claimed driving command with scheduler-native lifecycle callbacks and terminal
   * Swerve ownership.
   */
  public Command wrapClaimedDrivingCommand(
      Command drivingCommand, Command terminalHold, long attemptId) {
    Command acceptedDrivingCommand =
        Objects.requireNonNull(drivingCommand, "drivingCommand");
    Command acceptedTerminalHold =
        Objects.requireNonNull(terminalHold, "terminalHold");
    return Commands.sequence(
            Commands.runOnce(() -> markRunning(attemptId)),
            acceptedDrivingCommand,
            Commands.runOnce(() -> markHolding(attemptId)),
            acceptedTerminalHold)
        .finallyDo(interrupted -> finishExecution(attemptId, interrupted))
        .onlyWhile(DriverStation::isAutonomousEnabled)
        .withInterruptBehavior(Command.InterruptionBehavior.kCancelIncoming);
  }

  /** Returns the latest immutable preparation observation. */
  public synchronized AutonomousPreparationObservation getObservation() {
    return latestObservation;
  }

  private synchronized void markRunning(long attemptId) {
    if (latestObservation.state() != State.CONSUMED
        || latestObservation.attemptId() != attemptId) {
      return;
    }
    latestObservation =
        copyObservation(
            State.RUNNING,
            false,
            Reason.COMMAND_RUNNING,
            ReturnedCommand.ONE_METER_PATH,
            true);
  }

  private synchronized void markHolding(long attemptId) {
    if (latestObservation.state() != State.RUNNING
        || latestObservation.attemptId() != attemptId) {
      return;
    }
    if (actions.isAdapterFaulted()
        || actions.executionOutcome() == AutoBuilderContractAdapter.ExecutionOutcome.FAULTED) {
      transitionToFaulted(
          AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
          preparedAttempt == null
              ? Optional.empty()
              : Optional.ofNullable(preparedAttempt.alliance()));
      return;
    }

    Reason reason =
        switch (actions.executionOutcome()) {
          case COMPLETE -> Reason.COMMAND_COMPLETED;
          case MODE_LOSS -> Reason.MODE_LOSS;
          case TIMEOUT -> Reason.EXECUTION_TIMEOUT;
          case INPUT_UNAVAILABLE -> Reason.EXECUTION_INPUT_UNAVAILABLE;
          default -> Reason.COMMAND_INTERRUPTED;
        };
    latestObservation =
        copyObservation(
            State.HOLDING,
            false,
            reason,
            ReturnedCommand.ONE_METER_PATH,
            false);
  }

  private synchronized void finishExecution(long attemptId, boolean interrupted) {
    if (latestObservation.attemptId() != attemptId) {
      return;
    }
    if (actions.isAdapterFaulted()
        || actions.executionOutcome() == AutoBuilderContractAdapter.ExecutionOutcome.FAULTED) {
      transitionToFaulted(
          AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
          preparedAttempt == null
              ? Optional.empty()
              : Optional.ofNullable(preparedAttempt.alliance()));
      return;
    }

    AutoBuilderContractAdapter.ExecutionOutcome outcome = actions.executionOutcome();
    State state;
    Reason reason;
    if (outcome == AutoBuilderContractAdapter.ExecutionOutcome.COMPLETE
        && latestObservation.state() == State.HOLDING) {
      state = State.COMPLETE;
      reason = Reason.COMMAND_COMPLETED;
    } else {
      state = State.INTERRUPTED;
      reason =
          switch (outcome) {
            case MODE_LOSS -> Reason.MODE_LOSS;
            case TIMEOUT -> Reason.EXECUTION_TIMEOUT;
            case INPUT_UNAVAILABLE -> Reason.EXECUTION_INPUT_UNAVAILABLE;
            default ->
                !DriverStation.isAutonomousEnabled()
                    ? Reason.MODE_LOSS
                    : Reason.COMMAND_INTERRUPTED;
          };
    }
    latestObservation =
        copyObservation(
            state, false, reason, ReturnedCommand.ONE_METER_PATH, false);
  }

  private AutonomousPreparationObservation transitionNotReady(
      long attemptId,
      Reason reason,
      Routine routine,
      AllianceIdentity alliance,
      boolean headingValid,
      boolean poseAvailable,
      double translationError,
      double headingError,
      boolean speedsAvailable,
      boolean pathValid) {
    pendingAttempt = null;
    latestObservation =
        observation(
            State.NOT_READY,
            false,
            attemptId,
            reason,
            routine,
            alliance,
            false,
            false,
            headingValid,
            poseAvailable,
            translationError,
            headingError,
            speedsAvailable,
            pathValid,
            latestObservation.returnedCommand(),
            false);
    return latestObservation;
  }

  private AutonomousPreparationObservation transitionToFaulted(
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Optional<Alliance> alliance) {
    pendingAttempt = null;
    preparedAttempt = null;
    latestObservation =
        observation(
            State.FAULTED,
            false,
            latestAttemptId,
            Reason.FATAL_ADAPTER_FAULT,
            toObservationRoutine(routineId),
            toObservationAlliance(alliance),
            latestObservation.contextConsumed(),
            false,
            latestObservation.headingReferenceValid(),
            latestObservation.poseAvailable(),
            latestObservation.translationErrorMeters(),
            latestObservation.headingErrorRadians(),
            latestObservation.measuredSpeedsAvailable(),
            latestObservation.pathValid(),
            latestObservation.returnedCommand(),
            false);
    return latestObservation;
  }

  private void transitionStale(
      Reason reason,
      AutonomousRoutineFactory.AutonomousRoutineId routineId,
      Optional<Alliance> alliance) {
    pendingAttempt = null;
    preparedAttempt = null;
    latestObservation =
        observation(
            State.STALE,
            false,
            latestObservation.attemptId(),
            reason,
            toObservationRoutine(routineId),
            toObservationAlliance(alliance),
            latestObservation.contextConsumed(),
            true,
            latestObservation.headingReferenceValid(),
            latestObservation.poseAvailable(),
            latestObservation.translationErrorMeters(),
            latestObservation.headingErrorRadians(),
            latestObservation.measuredSpeedsAvailable(),
            latestObservation.pathValid(),
            latestObservation.returnedCommand(),
            false);
  }

  private AutonomousPreparationObservation copyObservation(
      State state,
      boolean ready,
      Reason reason,
      ReturnedCommand returnedCommand,
      boolean running) {
    return observation(
        state,
        ready,
        latestObservation.attemptId(),
        reason,
        latestObservation.routine(),
        latestObservation.alliance(),
        latestObservation.contextConsumed(),
        state == State.STALE,
        latestObservation.headingReferenceValid(),
        latestObservation.poseAvailable(),
        latestObservation.translationErrorMeters(),
        latestObservation.headingErrorRadians(),
        latestObservation.measuredSpeedsAvailable(),
        latestObservation.pathValid(),
        returnedCommand,
        running);
  }

  private AutonomousPreparationObservation observation(
      State state,
      boolean ready,
      long attemptId,
      Reason reason,
      Routine routine,
      AllianceIdentity alliance,
      boolean contextConsumed,
      boolean stale,
      boolean headingValid,
      boolean poseAvailable,
      double translationError,
      double headingError,
      boolean speedsAvailable,
      boolean pathValid,
      ReturnedCommand returnedCommand,
      boolean running) {
    return new AutonomousPreparationObservation(
        state,
        ready,
        attemptId,
        reason,
        routine,
        alliance,
        fieldVariant.name(),
        pathIdentity,
        contextConsumed,
        stale,
        headingValid,
        poseAvailable,
        translationError,
        headingError,
        speedsAvailable,
        pathValid,
        actions.isAutoBuilderConfigured(),
        actions.isAdapterFaulted(),
        Objects.requireNonNull(actions.firstFatalReason(), "firstFatalReason"),
        returnedCommand,
        running);
  }

  private static Reason mapPreflightReason(
      AutoBuilderContractAdapter.PreflightReason reason) {
    return switch (reason) {
      case POSE_UNAVAILABLE -> Reason.POSE_UNAVAILABLE;
      case MEASURED_SPEEDS_UNAVAILABLE -> Reason.MEASURED_SPEEDS_UNAVAILABLE;
      case POSE_MISMATCH -> Reason.POSE_MISMATCH;
      case AUTOBUILDER_NOT_CONFIGURED -> Reason.AUTOBUILDER_NOT_CONFIGURED;
      case STATIC_PATH_INVALID -> Reason.PATH_PREFLIGHT_FAILED;
      case MISSING_CONTEXT -> Reason.START_CONTEXT_UNAVAILABLE;
      case FATAL_ADAPTER_FAULT -> Reason.FATAL_ADAPTER_FAULT;
      case NONE -> Reason.NONE;
    };
  }

  private static Routine toObservationRoutine(
      AutonomousRoutineFactory.AutonomousRoutineId routineId) {
    if (routineId == null) {
      return Routine.UNKNOWN;
    }
    return switch (routineId) {
      case SAFE_STOP -> Routine.SAFE_STOP;
      case ONE_METER_PATH -> Routine.ONE_METER_PATH;
    };
  }

  private static AllianceIdentity toObservationAlliance(Optional<Alliance> alliance) {
    if (alliance == null || alliance.isEmpty()) {
      return AllianceIdentity.UNKNOWN;
    }
    return alliance.orElseThrow() == Alliance.Blue
        ? AllianceIdentity.BLUE
        : AllianceIdentity.RED;
  }

}
