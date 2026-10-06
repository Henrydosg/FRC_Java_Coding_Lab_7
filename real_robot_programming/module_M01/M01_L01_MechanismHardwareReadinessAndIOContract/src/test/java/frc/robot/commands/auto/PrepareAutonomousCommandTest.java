// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.commands.AutonomousStartContext;
import frc.robot.commands.auto.AutoBuilderContractAdapter.ExecutionOutcome;
import frc.robot.commands.auto.AutoBuilderContractAdapter.PreflightReason;
import frc.robot.commands.auto.AutoBuilderContractAdapter.PreflightResult;
import frc.robot.commands.auto.AutoBuilderContractAdapter.PreflightStatus;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Reason;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.Routine;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class PrepareAutonomousCommandTest {
  private final Fixture fixture = new Fixture();

  @AfterEach
  void unregisterSubsystem() {
    CommandScheduler.getInstance().unregisterSubsystem(fixture.subsystem);
  }

  @Test
  void oneShotCommandOwnsSwerveAndSnapshotsRoutineAndAlliance() {
    AtomicReference<AutonomousRoutineFactory.AutonomousRoutineId> routine =
        new AtomicReference<>(AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    AtomicReference<Optional<Alliance>> alliance =
        new AtomicReference<>(Optional.of(Alliance.Blue));
    PrepareAutonomousCommand command =
        new PrepareAutonomousCommand(
            fixture.subsystem, fixture.coordinator, routine::get, alliance::get);

    command.initialize();
    long attemptId = fixture.coordinator.getObservation().attemptId();
    routine.set(AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP);
    alliance.set(Optional.of(Alliance.Red));
    assertEquals(Set.of(fixture.subsystem), command.getRequirements());
    assertTrue(command.runsWhenDisabled());
    assertFalse(command.isFinished());
    command.execute();

    assertFalse(command.isFinished());
    assertEquals(State.VALIDATING, fixture.coordinator.getObservation().state());
    assertFalse(fixture.coordinator.getObservation().ready());
    assertEquals(attemptId, fixture.coordinator.getObservation().attemptId());
    assertEquals(1, fixture.actions.captureCount);
    assertEquals(0, fixture.actions.resetCount);
    assertEquals(0, fixture.actions.preflightCount);

    fixture.actions.refreshAfterHeadingCapture();
    command.execute();

    assertTrue(command.isFinished());
    assertEquals(State.READY, fixture.coordinator.getObservation().state());
    assertTrue(fixture.coordinator.getObservation().ready());
    assertEquals(attemptId, fixture.coordinator.getObservation().attemptId());
    assertEquals(Routine.ONE_METER_PATH, fixture.coordinator.getObservation().routine());
    assertEquals("BLUE", fixture.coordinator.getObservation().alliance().name());
    assertEquals(1, fixture.actions.captureCount);
    assertEquals(1, fixture.actions.resetCount);
    assertEquals(1, fixture.actions.preflightCount);

    command.end(false);
    command.execute();
    assertEquals(1, fixture.actions.resetCount);
    assertEquals(1, fixture.actions.preflightCount);
  }

  @Test
  void safeStopFinishesWithoutHeadingCaptureOrRefreshWait() {
    PrepareAutonomousCommand command =
        fixture.command(AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP);

    command.initialize();

    assertTrue(command.isFinished());
    assertEquals(State.READY, fixture.coordinator.getObservation().state());
    assertTrue(fixture.coordinator.getObservation().ready());
    assertEquals(Reason.SAFE_STOP_SELECTED, fixture.coordinator.getObservation().reason());
    command.execute();
    assertEquals(0, fixture.actions.captureCount);
    assertEquals(0, fixture.actions.resetCount);
    assertEquals(0, fixture.actions.preflightCount);
  }

  @Test
  void interruptionBeforeFirstExecuteCannotCompleteAbandonedAttempt() {
    assertInterruptedAttemptCannotComplete(false);
  }

  @Test
  void interruptionAfterFirstExecuteCannotCompleteAbandonedAttempt() {
    assertInterruptedAttemptCannotComplete(true);
  }

  @Test
  void freshAttemptAfterInterruptionWaitsAgainAndCanComplete() {
    PrepareAutonomousCommand command =
        fixture.command(AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    command.initialize();
    command.execute();
    long abandonedAttemptId = fixture.coordinator.getObservation().attemptId();
    command.end(true);
    fixture.actions.refreshAfterHeadingCapture();
    command.execute();
    assertFalse(fixture.coordinator.getObservation().ready());
    assertEquals(0, fixture.actions.resetCount);

    command.initialize();
    long freshAttemptId = fixture.coordinator.getObservation().attemptId();
    assertTrue(freshAttemptId > abandonedAttemptId);
    command.execute();
    assertFalse(command.isFinished());
    assertEquals(State.VALIDATING, fixture.coordinator.getObservation().state());
    assertEquals(2, fixture.actions.captureCount);
    assertEquals(0, fixture.actions.resetCount);

    fixture.actions.refreshAfterHeadingCapture();
    command.execute();
    assertTrue(command.isFinished());
    assertTrue(fixture.coordinator.getObservation().ready());
    assertEquals(freshAttemptId, fixture.coordinator.getObservation().attemptId());
    assertEquals(1, fixture.actions.resetCount);
    assertEquals(1, fixture.actions.preflightCount);
  }

  @Test
  void freshAttemptAfterCompletionWaitsAgain() {
    PrepareAutonomousCommand command =
        fixture.command(AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    command.initialize();
    command.execute();
    fixture.actions.refreshAfterHeadingCapture();
    command.execute();
    command.end(false);
    long completedAttemptId = fixture.coordinator.getObservation().attemptId();

    command.initialize();
    command.execute();
    assertFalse(command.isFinished());
    assertFalse(fixture.coordinator.getObservation().ready());
    assertTrue(fixture.coordinator.getObservation().attemptId() > completedAttemptId);
    assertEquals(2, fixture.actions.captureCount);
    assertEquals(1, fixture.actions.resetCount);
    assertEquals(1, fixture.actions.preflightCount);

    fixture.actions.refreshAfterHeadingCapture();
    command.execute();
    assertTrue(command.isFinished());
    assertTrue(fixture.coordinator.getObservation().ready());
    assertEquals(2, fixture.actions.resetCount);
    assertEquals(2, fixture.actions.preflightCount);
  }

  @Test
  void losingDisabledWhileWaitingRejectsCompletionWithoutReset() {
    PrepareAutonomousCommand command =
        fixture.command(AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    command.initialize();
    command.execute();
    fixture.actions.refreshAfterHeadingCapture();
    fixture.actions.disabled = false;

    command.execute();

    assertTrue(command.isFinished());
    assertEquals(State.NOT_READY, fixture.coordinator.getObservation().state());
    assertEquals(Reason.PREPARE_REQUIRES_DISABLED, fixture.coordinator.getObservation().reason());
    assertFalse(fixture.coordinator.getObservation().ready());
    assertEquals(0, fixture.actions.resetCount);
    assertEquals(0, fixture.actions.preflightCount);
  }

  @Test
  void fatalFaultWhileWaitingRemainsFaultedWithoutReset() {
    PrepareAutonomousCommand command =
        fixture.command(AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    command.initialize();
    command.execute();
    fixture.actions.refreshAfterHeadingCapture();
    fixture.actions.faulted = true;

    command.execute();

    assertTrue(command.isFinished());
    assertEquals(State.FAULTED, fixture.coordinator.getObservation().state());
    assertFalse(fixture.coordinator.getObservation().ready());
    assertEquals("existing fatal fault", fixture.coordinator.getObservation().firstFatalReason());
    assertEquals(0, fixture.actions.resetCount);
    assertEquals(0, fixture.actions.preflightCount);
  }

  @Test
  void supplierFailureProducesNotReadyInsteadOfThrowing() {
    PrepareAutonomousCommand command =
        new PrepareAutonomousCommand(
            fixture.subsystem,
            fixture.coordinator,
            () -> {
              throw new IllegalStateException("chooser unavailable");
            },
            () -> Optional.of(Alliance.Blue));

    command.initialize();

    assertEquals(State.NOT_READY, fixture.coordinator.getObservation().state());
    assertTrue(command.isFinished());
    command.execute();
    assertEquals(0, fixture.actions.captureCount);
    assertEquals(0, fixture.actions.resetCount);
    assertEquals(0, fixture.actions.preflightCount);
  }

  private void assertInterruptedAttemptCannotComplete(boolean executeBeforeInterruption) {
    PrepareAutonomousCommand command =
        fixture.command(AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    command.initialize();
    if (executeBeforeInterruption) {
      command.execute();
    }
    long attemptId = fixture.coordinator.getObservation().attemptId();
    command.end(true);
    fixture.actions.refreshAfterHeadingCapture();
    command.execute();
    command.execute();

    assertTrue(command.isFinished());
    assertEquals(State.VALIDATING, fixture.coordinator.getObservation().state());
    assertFalse(fixture.coordinator.getObservation().ready());
    assertEquals(attemptId, fixture.coordinator.getObservation().attemptId());
    assertEquals(1, fixture.actions.captureCount);
    assertEquals(0, fixture.actions.resetCount);
    assertEquals(0, fixture.actions.preflightCount);
  }

  private static final class Fixture {
    private final RecordingActions actions = new RecordingActions();
    private final AutonomousPreparationCoordinator coordinator =
        new AutonomousPreparationCoordinator(actions, FieldVariant.REBUILT_WELDED, "test-path");
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(
            new Module(), new Module(), new Module(), new Module(), new Gyro());

    private PrepareAutonomousCommand command(
        AutonomousRoutineFactory.AutonomousRoutineId routine) {
      return new PrepareAutonomousCommand(
          subsystem, coordinator, () -> routine, () -> Optional.of(Alliance.Blue));
    }
  }

  private static final class RecordingActions
      implements AutonomousPreparationCoordinator.PreparationActions {
    private int captureCount;
    private int resetCount;
    private int preflightCount;
    private boolean refreshedAfterCapture;
    private boolean disabled = true;
    private boolean faulted;

    private void refreshAfterHeadingCapture() {
      // Represents the intervening subsystem-refresh boundary for this command unit fixture.
      assertTrue(captureCount > 0);
      refreshedAfterCapture = true;
    }

    @Override
    public boolean isDisabled() {
      return disabled;
    }

    @Override
    public boolean captureFieldHeadingReference() {
      captureCount++;
      refreshedAfterCapture = false;
      return true;
    }

    @Override
    public Pose2d canonicalStartingPose() {
      return Pose2d.kZero;
    }

    @Override
    public boolean resetKnownFieldPose(Pose2d pose) {
      resetCount++;
      return refreshedAfterCapture;
    }

    @Override
    public PreflightResult preflight(AutonomousStartContext context) {
      preflightCount++;
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

    @Override
    public boolean isAutoBuilderConfigured() {
      return true;
    }

    @Override
    public boolean isAdapterFaulted() {
      return faulted;
    }

    @Override
    public String firstFatalReason() {
      return faulted ? "existing fatal fault" : "";
    }

    @Override
    public ExecutionOutcome executionOutcome() {
      return ExecutionOutcome.NONE;
    }

    @Override
    public void latchStaticPreparationFault(String reason, Throwable failure) {}
  }

  private static final class Module implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {}
  }

  private static final class Gyro implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
