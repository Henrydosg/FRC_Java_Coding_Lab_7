// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.AutoBuilderContractAdapter.CommandCreationResult;
import frc.robot.commands.AutoBuilderContractAdapter.CommandCreationStatus;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.ReturnedCommand;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/** Creates one fresh, fail-closed autonomous command from a snapshotted routine identity. */
public final class AutonomousRoutineFactory {
  /** The complete L08 routine identity set. */
  public enum AutonomousRoutineId {
    SAFE_STOP,
    ONE_METER_PATH,
    ONE_METER_WITH_EVENT
  }

  private final SwerveSubsystem swerveSubsystem;
  private final Function<AutonomousStartContext, CommandCreationResult> oneMeterPathFactory;
  private final Function<AutonomousStartContext, CommandCreationResult>
      oneMeterEventPathFactory;
  private final AutonomousPreparationCoordinator preparationCoordinator;

  /** Creates a production routine factory using the WPILib monotonic clock. */
  public AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      AutoBuilderContractAdapter autoBuilderContractAdapter,
      AutonomousPreparationCoordinator preparationCoordinator) {
    this(
        swerveSubsystem,
        Objects.requireNonNull(autoBuilderContractAdapter, "autoBuilderContractAdapter")
            ::createPathCommandResult,
        autoBuilderContractAdapter::createEventPathCommandResult,
        preparationCoordinator);
  }

  AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      Function<AutonomousStartContext, CommandCreationResult> oneMeterPathFactory,
      AutonomousPreparationCoordinator preparationCoordinator) {
    this(
        swerveSubsystem,
        oneMeterPathFactory,
        oneMeterPathFactory,
        preparationCoordinator);
  }

  AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      Function<AutonomousStartContext, CommandCreationResult> oneMeterPathFactory,
      Function<AutonomousStartContext, CommandCreationResult> oneMeterEventPathFactory,
      AutonomousPreparationCoordinator preparationCoordinator) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.oneMeterPathFactory =
        Objects.requireNonNull(oneMeterPathFactory, "oneMeterPathFactory");
    this.oneMeterEventPathFactory =
        Objects.requireNonNull(oneMeterEventPathFactory, "oneMeterEventPathFactory");
    this.preparationCoordinator =
        Objects.requireNonNull(preparationCoordinator, "preparationCoordinator");
  }

  /**
   * Creates a fresh command from one chooser snapshot and current alliance snapshot.
   *
   * <p>Driving readiness is previewed, the command is constructed, and then the exact preparation
   * is atomically consumed. SAFE_STOP never consumes driving readiness.
   */
  public Command create(
      AutonomousRoutineId routineId, Optional<Alliance> currentAlliance) {
    Optional<Alliance> alliance =
        currentAlliance == null ? Optional.empty() : currentAlliance;
    if (routineId == null) {
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }

    if (routineId == AutonomousRoutineId.SAFE_STOP) {
      preparationCoordinator.observeSafeStopSelection(alliance);
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP);
      return createSafeStop();
    }

    Optional<AutonomousPreparationCoordinator.PreparationClaim> preview =
        preparationCoordinator.previewDrivingPreparation(routineId, alliance);
    if (preview.isEmpty()) {
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }

    Function<AutonomousStartContext, CommandCreationResult> pathFactory =
        routineId == AutonomousRoutineId.ONE_METER_WITH_EVENT
            ? oneMeterEventPathFactory
            : oneMeterPathFactory;
    ReturnedCommand returnedCommand =
        routineId == AutonomousRoutineId.ONE_METER_WITH_EVENT
            ? ReturnedCommand.ONE_METER_WITH_EVENT
            : ReturnedCommand.ONE_METER_PATH;
    CommandCreationResult creationResult;
    try {
      creationResult =
          Objects.requireNonNull(
              pathFactory.apply(preview.orElseThrow().startContext()),
              "autonomous path factory result");
    } catch (RuntimeException failure) {
      preparationCoordinator.recordFatalInvariant(
          "autonomous path factory threw during command construction");
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }

    if (creationResult.status() == CommandCreationStatus.NOT_READY) {
      preparationCoordinator.recordRecoverableConstructionFailure();
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }
    if (creationResult.status() == CommandCreationStatus.FAULTED) {
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }

    Command pathCommand = creationResult.command().orElse(null);
    if (pathCommand == null
        || !pathCommand.getRequirements().contains(swerveSubsystem)) {
      preparationCoordinator.recordFatalInvariant(
          "constructed autonomous command did not own SwerveSubsystem");
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }

    AutonomousPreparationCoordinator.PreparationClaim claim = preview.orElseThrow();
    if (!preparationCoordinator.claim(claim, alliance)) {
      preparationCoordinator.recordReturnedCommand(ReturnedCommand.SAFE_STOP_FALLBACK);
      return createSafeStop();
    }

    Command lifecycleCommand =
        preparationCoordinator.wrapClaimedDrivingCommand(
            pathCommand, createSafeStop(), claim.attemptId());
    preparationCoordinator.recordReturnedCommand(returnedCommand);
    return lifecycleCommand;
  }

  private Command createSafeStop() {
    return new AutonomousSafetyHoldCommand(swerveSubsystem);
  }
}
