// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

/** Creates one fresh, fail-closed autonomous command from a snapshotted routine identity. */
public final class AutonomousRoutineFactory {
  /** The complete L09 routine identity set. */
  public enum AutonomousRoutineId {
    SAFE_STOP,
    ONE_METER_PATH,
    ONE_METER_WITH_EVENT
  }

  private final SwerveSubsystem swerveSubsystem;
  private final Function<AutonomousStartContext, Command> oneMeterPathFactory;
  private final Function<AutonomousStartContext, Command> oneMeterEventPathFactory;
  private final DoubleSupplier monotonicClock;

  /** Creates a production routine factory using the WPILib monotonic clock. */
  public AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      AutoBuilderContractAdapter autoBuilderContractAdapter) {
    this(
        swerveSubsystem,
        Objects.requireNonNull(autoBuilderContractAdapter, "autoBuilderContractAdapter")
            ::createPathCommand,
        Objects.requireNonNull(autoBuilderContractAdapter, "autoBuilderContractAdapter")
            ::createEventPathCommand,
        Timer::getFPGATimestamp);
  }

  /** Test-only constructor that keeps the production adapter boundary injectable. */
  AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      Function<AutonomousStartContext, Command> oneMeterPathFactory,
      DoubleSupplier monotonicClock) {
    this(
        swerveSubsystem,
        oneMeterPathFactory,
        oneMeterPathFactory,
        monotonicClock);
  }

  /** Test-only constructor with independently injectable path factories. */
  AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      Function<AutonomousStartContext, Command> oneMeterPathFactory,
      Function<AutonomousStartContext, Command> oneMeterEventPathFactory,
      DoubleSupplier monotonicClock) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.oneMeterPathFactory =
        Objects.requireNonNull(oneMeterPathFactory, "oneMeterPathFactory");
    this.oneMeterEventPathFactory =
        Objects.requireNonNull(oneMeterEventPathFactory, "oneMeterEventPathFactory");
    this.monotonicClock = Objects.requireNonNull(monotonicClock, "monotonicClock");
  }

  /**
   * Creates a routine factory with an injectable clock for deterministic command lifecycle tests.
   *
   * @param swerveSubsystem existing drivetrain and centralized stop authority
   * @param autoBuilderContractAdapter existing L07 safe path boundary
   * @param monotonicClock monotonic time source in seconds
   */
  public AutonomousRoutineFactory(
      SwerveSubsystem swerveSubsystem,
      AutoBuilderContractAdapter autoBuilderContractAdapter,
      DoubleSupplier monotonicClock) {
    this(
        swerveSubsystem,
        Objects.requireNonNull(autoBuilderContractAdapter, "autoBuilderContractAdapter")
            ::createPathCommand,
        Objects.requireNonNull(autoBuilderContractAdapter, "autoBuilderContractAdapter")
            ::createEventPathCommand,
        monotonicClock);
  }

  /**
   * Creates a new command from one already snapshotted identity and readiness result.
   *
   * <p>Every invalid input and every construction failure resolves to a non-driving safety hold.
   * The factory deliberately performs no chooser, DriverStation, or hardware access.
   *
   * @param routineId snapshotted chooser identity
   * @param acceptedStartContext one-shot Disabled-only readiness context, when available
   * @return a fresh command instance
   */
  public Command create(
      AutonomousRoutineId routineId, Optional<AutonomousStartContext> acceptedStartContext) {
    if (routineId == null) {
      return createSafeStop();
    }

    switch (routineId) {
      case SAFE_STOP:
        return createSafeStop();
      case ONE_METER_PATH:
        return createPathOrSafeStop(oneMeterPathFactory, acceptedStartContext);
      case ONE_METER_WITH_EVENT:
        return createPathOrSafeStop(oneMeterEventPathFactory, acceptedStartContext);
      default:
        return createSafeStop();
    }
  }

  private Command createPathOrSafeStop(
      Function<AutonomousStartContext, Command> pathFactory,
      Optional<AutonomousStartContext> acceptedStartContext) {
    if (acceptedStartContext == null || acceptedStartContext.isEmpty()) {
      return createSafeStop();
    }
    try {
      Command pathCommand = pathFactory.apply(acceptedStartContext.orElseThrow());
      if (pathCommand == null || !pathCommand.getRequirements().contains(swerveSubsystem)) {
        return createSafeStop();
      }
      return pathCommand;
    } catch (RuntimeException failure) {
      return createSafeStop();
    }
  }

  private Command createSafeStop() {
    return new AutonomousSafetyHoldCommand(
        swerveSubsystem,
        Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds,
        monotonicClock);
  }
}
