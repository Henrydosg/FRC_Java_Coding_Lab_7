// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.commands.AutoBuilderContractAdapter.ExecutionOutcome;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightReason;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightResult;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightStatus;
import frc.robot.observation.autonomous.AutonomousPreparationObservation;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Reason;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutonomousPreparationCoordinatorTest {
  private static final FieldVariant kFieldVariant = FieldVariant.REBUILT_WELDED;
  private static final String kPathIdentity = "A01_L06_OneMeter_Forward";

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableAutonomous() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
  }

  @AfterEach
  void resetRuntimeState() {
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void successfulPreparationBindsProvenanceAndUsesMonotonicAttemptIds() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);

    AutonomousPreparationObservation first =
        coordinator.prepare(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));
    AutonomousPreparationObservation second =
        coordinator.prepare(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Red));

    assertEquals(State.READY, first.state());
    assertTrue(first.ready());
    assertEquals(1L, first.attemptId());
    assertEquals("BLUE", first.alliance().name());
    assertTrue(first.headingReferenceValid());
    assertTrue(first.pathValid());
    assertEquals(2L, second.attemptId());
    assertEquals("RED", second.alliance().name());
    assertEquals(2, actions.captureCount);
    assertEquals(2, actions.resetCount);
  }

  @Test
  void recoverablePreparationFailuresCanReachReadyWithoutProcessRestart() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);

    actions.resetAccepted = false;
    assertEquals(
        State.NOT_READY,
        coordinator
            .prepare(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .state());
    actions.resetAccepted = true;
    assertEquals(State.READY, prepareBlue(coordinator).state());

    actions.preflight =
        notReady(PreflightReason.POSE_UNAVAILABLE, false, false);
    assertEquals(Reason.POSE_UNAVAILABLE, prepareBlue(coordinator).reason());
    actions.preflight = ready();
    assertEquals(State.READY, prepareBlue(coordinator).state());

    actions.preflight =
        notReady(PreflightReason.MEASURED_SPEEDS_UNAVAILABLE, true, false);
    assertEquals(Reason.MEASURED_SPEEDS_UNAVAILABLE, prepareBlue(coordinator).reason());
    actions.preflight = ready();
    assertEquals(State.READY, prepareBlue(coordinator).state());

    actions.preflight =
        new PreflightResult(
            PreflightStatus.NOT_READY,
            PreflightReason.POSE_MISMATCH,
            true,
            0.031,
            0.0,
            true,
            false,
            "mismatch");
    assertEquals(Reason.POSE_MISMATCH, prepareBlue(coordinator).reason());
    actions.preflight = ready();
    assertEquals(State.READY, prepareBlue(coordinator).state());
    assertFalse(actions.faulted);
  }

  @Test
  void allianceRoutineAndClaimProvenanceCannotBeReused() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);
    prepareBlue(coordinator);

    assertTrue(
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .isPresent());
    assertTrue(
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Red))
            .isEmpty());
    assertEquals(State.STALE, coordinator.getObservation().state());
    assertEquals(Reason.ALLIANCE_CHANGED, coordinator.getObservation().reason());

    prepareBlue(coordinator);
    coordinator.observeSafeStopSelection(Optional.of(Alliance.Blue));
    assertEquals(State.STALE, coordinator.getObservation().state());
    assertEquals(Reason.ROUTINE_CHANGED, coordinator.getObservation().reason());

    prepareBlue(coordinator);
    AutonomousPreparationCoordinator.PreparationClaim validClaim =
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .orElseThrow();
    AutonomousPreparationCoordinator.PreparationClaim wrongPath =
        new AutonomousPreparationCoordinator.PreparationClaim(
            validClaim.attemptId(),
            validClaim.startContext(),
            validClaim.routineId(),
            validClaim.alliance(),
            validClaim.fieldVariant(),
            "wrong-path",
            validClaim.headingReferenceAttemptId());
    assertFalse(coordinator.claim(wrongPath, Optional.of(Alliance.Blue)));
    assertEquals(State.STALE, coordinator.getObservation().state());
  }

  @Test
  void successfulClaimConsumesExactlyOnceAndFreshPrepareRecovers() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);
    prepareBlue(coordinator);
    AutonomousPreparationCoordinator.PreparationClaim claim =
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .orElseThrow();

    assertTrue(coordinator.claim(claim, Optional.of(Alliance.Blue)));
    assertEquals(State.CONSUMED, coordinator.getObservation().state());
    assertTrue(coordinator.getObservation().contextConsumed());
    assertFalse(coordinator.claim(claim, Optional.of(Alliance.Blue)));
    assertTrue(
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .isEmpty());

    AutonomousPreparationObservation recovered = prepareBlue(coordinator);
    assertEquals(State.READY, recovered.state());
    assertTrue(recovered.attemptId() > claim.attemptId());
  }

  @Test
  void fatalFaultRemainsFailClosedAndPreservesFirstReason() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);

    coordinator.recordFatalInvariant("first fatal reason");
    coordinator.recordFatalInvariant("later fatal reason");
    AutonomousPreparationObservation afterPrepare =
        coordinator.prepare(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));

    assertEquals(State.FAULTED, afterPrepare.state());
    assertTrue(afterPrepare.adapterFatalFaulted());
    assertEquals("first fatal reason", afterPrepare.firstFatalReason());
    assertEquals(0, actions.captureCount);
  }

  @Test
  void schedulerFatalLatchesFaultStopsAndPreservesFirstReason() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);

    coordinator.recordSchedulerFatal(new IllegalStateException("first scheduler failure"));
    coordinator.recordSchedulerFatal(new IllegalStateException("later scheduler failure"));

    AutonomousPreparationObservation observation = coordinator.getObservation();
    assertEquals(State.FAULTED, observation.state());
    assertTrue(observation.adapterFatalFaulted());
    assertEquals("scheduler boundary failure", observation.firstFatalReason());
    assertEquals(2, actions.stopCount);
  }

  @Test
  void schedulerNativeLifecycleReportsHoldingCompletionAndModeLossInterruption() {
    MutableActions actions = new MutableActions();
    AutonomousPreparationCoordinator coordinator = createCoordinator(actions);
    CommandScheduler scheduler = CommandScheduler.getInstance();
    prepareBlue(coordinator);
    AutonomousPreparationCoordinator.PreparationClaim firstClaim =
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .orElseThrow();
    assertTrue(coordinator.claim(firstClaim, Optional.of(Alliance.Blue)));
    Command first =
        coordinator.wrapClaimedDrivingCommand(
            Commands.runOnce(() -> actions.executionOutcome = ExecutionOutcome.COMPLETE),
            Commands.idle(),
            firstClaim.attemptId());

    scheduler.schedule(first);
    scheduler.run();
    scheduler.run();
    assertEquals(State.HOLDING, coordinator.getObservation().state());
    assertFalse(coordinator.getObservation().running());
    assertTrue(first.isScheduled());
    scheduler.cancel(first);
    assertEquals(State.COMPLETE, coordinator.getObservation().state());
    assertFalse(coordinator.getObservation().running());

    prepareBlue(coordinator);
    AutonomousPreparationCoordinator.PreparationClaim secondClaim =
        coordinator
            .previewDrivingPreparation(
                AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
                Optional.of(Alliance.Blue))
            .orElseThrow();
    assertTrue(coordinator.claim(secondClaim, Optional.of(Alliance.Blue)));
    Command second =
        coordinator.wrapClaimedDrivingCommand(
            Commands.run(() -> {}), Commands.idle(), secondClaim.attemptId());
    scheduler.schedule(second);
    scheduler.run();
    assertEquals(State.RUNNING, coordinator.getObservation().state());
    actions.executionOutcome = ExecutionOutcome.MODE_LOSS;
    scheduler.cancel(second);
    assertEquals(State.INTERRUPTED, coordinator.getObservation().state());
    assertEquals(Reason.MODE_LOSS, coordinator.getObservation().reason());
  }

  private static AutonomousPreparationObservation prepareBlue(
      AutonomousPreparationCoordinator coordinator) {
    return coordinator.prepare(
        AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
        Optional.of(Alliance.Blue));
  }

  private static AutonomousPreparationCoordinator createCoordinator(
      MutableActions actions) {
    return new AutonomousPreparationCoordinator(actions, kFieldVariant, kPathIdentity);
  }

  private static PreflightResult ready() {
    return new PreflightResult(
        PreflightStatus.READY,
        PreflightReason.NONE,
        true,
        0.0,
        0.0,
        true,
        true,
        "");
  }

  private static PreflightResult notReady(
      PreflightReason reason, boolean poseAvailable, boolean speedsAvailable) {
    return new PreflightResult(
        PreflightStatus.NOT_READY,
        reason,
        poseAvailable,
        0.0,
        0.0,
        speedsAvailable,
        false,
        reason.name());
  }

  private static final class MutableActions
      implements AutonomousPreparationCoordinator.PreparationActions {
    private boolean disabled = true;
    private boolean captureAccepted = true;
    private boolean resetAccepted = true;
    private boolean configured = true;
    private boolean faulted;
    private String firstFatalReason = "";
    private PreflightResult preflight = ready();
    private ExecutionOutcome executionOutcome = ExecutionOutcome.NONE;
    private int captureCount;
    private int resetCount;
    private int stopCount;

    @Override
    public boolean isDisabled() {
      return disabled;
    }

    @Override
    public boolean captureFieldHeadingReference() {
      captureCount++;
      return captureAccepted;
    }

    @Override
    public Pose2d canonicalStartingPose() {
      return Pose2d.kZero;
    }

    @Override
    public boolean resetKnownFieldPose(Pose2d pose) {
      resetCount++;
      return resetAccepted;
    }

    @Override
    public PreflightResult preflight(AutonomousStartContext context) {
      return preflight;
    }

    @Override
    public boolean isAutoBuilderConfigured() {
      return configured;
    }

    @Override
    public boolean isAdapterFaulted() {
      return faulted;
    }

    @Override
    public String firstFatalReason() {
      return firstFatalReason;
    }

    @Override
    public ExecutionOutcome executionOutcome() {
      return executionOutcome;
    }

    @Override
    public void latchStaticPreparationFault(String reason, Throwable failure) {
      if (!faulted) {
        faulted = true;
        firstFatalReason = reason;
      }
      executionOutcome = ExecutionOutcome.FAULTED;
    }

    @Override
    public void stop() {
      stopCount++;
    }
  }
}
