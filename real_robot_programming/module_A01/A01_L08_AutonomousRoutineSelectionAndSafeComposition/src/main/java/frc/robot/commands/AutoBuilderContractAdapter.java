// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;

/** Integrates PathPlanner AutoBuilder with the frozen Swerve contracts. */
public final class AutoBuilderContractAdapter {
  /** Preflight outcome without constructing or scheduling an autonomous command. */
  public enum PreflightStatus {
    READY,
    NOT_READY,
    FAULTED
  }

  /** Stable preflight reason code. */
  public enum PreflightReason {
    NONE,
    MISSING_CONTEXT,
    POSE_UNAVAILABLE,
    MEASURED_SPEEDS_UNAVAILABLE,
    POSE_MISMATCH,
    AUTOBUILDER_NOT_CONFIGURED,
    STATIC_PATH_INVALID,
    FATAL_ADAPTER_FAULT
  }

  /** Immutable result of one Disabled preparation preflight. */
  public record PreflightResult(
      PreflightStatus status,
      PreflightReason reason,
      boolean poseAvailable,
      double translationErrorMeters,
      double headingErrorRadians,
      boolean measuredSpeedsAvailable,
      boolean pathValid,
      String detail) {
    public PreflightResult {
      status = Objects.requireNonNull(status, "status");
      reason = Objects.requireNonNull(reason, "reason");
      detail = Objects.requireNonNull(detail, "detail");
      if (!Double.isFinite(translationErrorMeters) || translationErrorMeters < 0.0) {
        throw new IllegalArgumentException(
            "translationErrorMeters must be finite and nonnegative");
      }
      if (!Double.isFinite(headingErrorRadians) || headingErrorRadians < 0.0) {
        throw new IllegalArgumentException(
            "headingErrorRadians must be finite and nonnegative");
      }
    }
  }

  /** Result of constructing one fresh execution command. */
  public enum CommandCreationStatus {
    CREATED,
    NOT_READY,
    FAULTED
  }

  /** Immutable command-construction result. */
  public record CommandCreationResult(
      CommandCreationStatus status, Optional<Command> command, String reason) {
    public CommandCreationResult {
      status = Objects.requireNonNull(status, "status");
      command = Objects.requireNonNull(command, "command");
      reason = Objects.requireNonNull(reason, "reason");
      if ((status == CommandCreationStatus.CREATED) != command.isPresent()) {
        throw new IllegalArgumentException("only CREATED may contain a command");
      }
    }
  }

  /** Latest terminal or recoverable execution outcome. */
  public enum ExecutionOutcome {
    NONE,
    COMPLETE,
    INTERRUPTED,
    MODE_LOSS,
    TIMEOUT,
    INPUT_UNAVAILABLE,
    FAULTED
  }

  private final SwerveSubsystem swerveSubsystem;
  private final PathPlannerTrajectoryAdapter pathPlannerTrajectoryAdapter;
  private final RobotConfig robotConfig;
  private final Pose2d fallbackPose = Pose2d.kZero;
  private final ChassisSpeeds fallbackSpeeds = new ChassisSpeeds();

  private volatile boolean configured;
  private volatile boolean faulted;
  private volatile String firstFaultReason = "";
  private volatile ExecutionOutcome executionOutcome = ExecutionOutcome.NONE;

  /** Creates the AutoBuilder integration boundary. */
  public AutoBuilderContractAdapter(
      SwerveSubsystem swerveSubsystem,
      PathPlannerTrajectoryAdapter pathPlannerTrajectoryAdapter,
      RobotConfig robotConfig) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.pathPlannerTrajectoryAdapter =
        Objects.requireNonNull(pathPlannerTrajectoryAdapter, "pathPlannerTrajectoryAdapter");
    this.robotConfig = Objects.requireNonNull(robotConfig, "robotConfig");
  }

  /** Configures the process-global AutoBuilder exactly once. */
  public synchronized void configure() {
    if (configured || AutoBuilder.isConfigured()) {
      latchFault("duplicate AutoBuilder configuration");
      return;
    }

    try {
      AutoBuilder.configure(
          this::supplyPose,
          this::resetPose,
          this::supplyMeasuredRobotRelativeSpeeds,
          this::acceptOutput,
          new PPHolonomicDriveController(
              new PIDConstants(
                  Constants.HolonomicTrajectoryFollowingConstants.kXKpPerSecond, 0.0, 0.0),
              new PIDConstants(
                  Constants.HolonomicTrajectoryFollowingConstants.kThetaKpPerSecond, 0.0, 0.0)),
          robotConfig,
          () -> false,
          swerveSubsystem);
      configured = true;
    } catch (RuntimeException failure) {
      latchFault("AutoBuilder configuration failed", failure);
    }
  }

  /**
   * Validates all Disabled preparation prerequisites without constructing or running a command.
   *
   * @param context proposed execution provenance
   * @return immutable preflight result
   */
  public PreflightResult preflight(AutonomousStartContext context) {
    if (faulted) {
      return faultedPreflight();
    }
    if (!configured) {
      latchFault("AutoBuilder was not configured before preflight");
      return faultedPreflight();
    }
    if (context == null) {
      return notReady(
          PreflightReason.MISSING_CONTEXT,
          false,
          0.0,
          0.0,
          false,
          false,
          "autonomous start context was unavailable");
    }

    Optional<Pose2d> pose = readPoseForValidation();
    if (faulted) {
      return faultedPreflight();
    }
    if (pose.isEmpty()) {
      return notReady(
          PreflightReason.POSE_UNAVAILABLE,
          false,
          0.0,
          0.0,
          false,
          false,
          "estimated pose was temporarily unavailable");
    }

    double translationError =
        translationErrorMeters(pose.orElseThrow(), context.executionStartPose());
    double headingError =
        headingErrorRadians(pose.orElseThrow(), context.executionStartPose());
    if (!isPoseWithinTolerance(pose.orElseThrow(), context.executionStartPose())) {
      return notReady(
          PreflightReason.POSE_MISMATCH,
          true,
          translationError,
          headingError,
          false,
          false,
          "current pose did not match the prepared execution start pose");
    }

    Optional<ChassisSpeeds> speeds = readMeasuredSpeedsForValidation();
    if (faulted) {
      return faultedPreflight();
    }
    if (speeds.isEmpty()) {
      return notReady(
          PreflightReason.MEASURED_SPEEDS_UNAVAILABLE,
          true,
          translationError,
          headingError,
          false,
          false,
          "measured robot-relative speeds were temporarily unavailable");
    }

    try {
      PathPlannerPath canonicalPath = pathPlannerTrajectoryAdapter.createCanonicalPath();
      PathPlannerPath executionPath =
          PathPlannerExecutionPathFactory.createExecutionPath(
              canonicalPath, context.fieldVariant(), context.alliance());
      if (!executionPath.preventFlipping) {
        throw new IllegalStateException("execution path did not prevent vendor flipping");
      }
      pathTimeoutSeconds(executionPath);
      return new PreflightResult(
          PreflightStatus.READY,
          PreflightReason.NONE,
          true,
          translationError,
          headingError,
          true,
          true,
          "");
    } catch (RuntimeException failure) {
      latchFault("static PathPlanner preflight failed", failure);
      return faultedPreflight();
    }
  }

  /** Creates one fresh command result after a successful preparation preview. */
  public CommandCreationResult createPathCommandResult(AutonomousStartContext context) {
    PreflightResult preflight = preflight(context);
    if (preflight.status() == PreflightStatus.NOT_READY) {
      return new CommandCreationResult(
          CommandCreationStatus.NOT_READY, Optional.empty(), preflight.detail());
    }
    if (preflight.status() == PreflightStatus.FAULTED) {
      return new CommandCreationResult(
          CommandCreationStatus.FAULTED, Optional.empty(), firstFaultReason);
    }

    try {
      PathPlannerPath canonicalPath = pathPlannerTrajectoryAdapter.createCanonicalPath();
      PathPlannerPath executionPath =
          PathPlannerExecutionPathFactory.createExecutionPath(
              canonicalPath, context.fieldVariant(), context.alliance());
      Command followPath =
          Objects.requireNonNull(
              AutoBuilder.followPath(executionPath), "AutoBuilder followPath command");
      if (!followPath.getRequirements().contains(swerveSubsystem)) {
        throw new IllegalStateException(
            "AutoBuilder command did not require SwerveSubsystem");
      }
      double timeoutSeconds = pathTimeoutSeconds(executionPath);
      executionOutcome = ExecutionOutcome.NONE;
      return new CommandCreationResult(
          CommandCreationStatus.CREATED,
          Optional.of(
              composeSchedulerOwnedPathCommand(followPath, timeoutSeconds)),
          "");
    } catch (RuntimeException failure) {
      latchFault("AutoBuilder path command creation failed", failure);
      return new CommandCreationResult(
          CommandCreationStatus.FAULTED, Optional.empty(), firstFaultReason);
    }
  }

  /** Legacy compatibility boundary; production selection uses the typed result. */
  public Command createPathCommand(AutonomousStartContext context) {
    return createPathCommandResult(context).command().orElseGet(this::stopCommand);
  }

  /** Returns whether this adapter completed its one-time configuration. */
  public boolean isConfigured() {
    return configured;
  }

  /** Returns whether this process-session adapter is latched faulted. */
  public boolean isFaulted() {
    return faulted;
  }

  /** Returns the immutable first fatal reason, or an empty string before any fault. */
  public String firstFaultReason() {
    return firstFaultReason;
  }

  /** Returns the latest execution outcome for lifecycle diagnostics. */
  public ExecutionOutcome executionOutcome() {
    return executionOutcome;
  }

  /** Records a confirmed static preparation defect discovered before adapter preflight. */
  void latchStaticPreparationFault(String reason, Throwable failure) {
    latchFault(reason, failure);
  }

  /** Records an unexpected scheduler failure at the approved Robot boundary. */
  void latchSchedulerBoundaryFault(RuntimeException failure) {
    latchFault("scheduler boundary failure", Objects.requireNonNull(failure, "failure"));
  }

  static double translationErrorMeters(Pose2d current, Pose2d expected) {
    requireFinitePose(current, "current");
    requireFinitePose(expected, "expected");
    return current.getTranslation().getDistance(expected.getTranslation());
  }

  static double headingErrorRadians(Pose2d current, Pose2d expected) {
    requireFinitePose(current, "current");
    requireFinitePose(expected, "expected");
    return Math.abs(
        MathUtil.angleModulus(
            current.getRotation().getRadians() - expected.getRotation().getRadians()));
  }

  static boolean isPoseWithinTolerance(Pose2d current, Pose2d expected) {
    return translationErrorMeters(current, expected)
            <= Constants.AutonomousPreparationConstants.kTranslationToleranceMeters
        && headingErrorRadians(current, expected)
            <= Constants.AutonomousPreparationConstants.kHeadingToleranceRadians;
  }

  private Pose2d supplyPose() {
    Optional<Pose2d> pose = readPoseForValidation();
    if (pose.isEmpty() && !faulted) {
      executionOutcome = ExecutionOutcome.INPUT_UNAVAILABLE;
      safeStop();
    }
    return pose.orElse(fallbackPose);
  }

  private Optional<Pose2d> readPoseForValidation() {
    if (faulted) {
      return Optional.empty();
    }
    try {
      Optional<Pose2d> pose =
          Objects.requireNonNull(
              swerveSubsystem.getEstimatedPose(), "estimated pose result");
      if (pose.isEmpty()) {
        return Optional.empty();
      }
      if (!isFinitePose(pose.orElseThrow())) {
        latchFault("estimated pose was nonfinite");
        return Optional.empty();
      }
      return Optional.of(copyPose(pose.orElseThrow()));
    } catch (RuntimeException failure) {
      latchFault("estimated pose callback failed", failure);
      return Optional.empty();
    }
  }

  private void resetPose(Pose2d requestedPose) {
    if (faulted) {
      safeStop();
      return;
    }
    if (!isFinitePose(requestedPose)) {
      latchFault("AutoBuilder requested a nonfinite pose reset");
      return;
    }
    if (DriverStation.isEnabled()) {
      executionOutcome = ExecutionOutcome.MODE_LOSS;
      safeStop();
      return;
    }
    try {
      if (!swerveSubsystem.resetKnownFieldPose(copyPose(requestedPose))) {
        executionOutcome = ExecutionOutcome.INPUT_UNAVAILABLE;
        safeStop();
      }
    } catch (RuntimeException failure) {
      latchFault("AutoBuilder reset callback failed", failure);
    }
  }

  private ChassisSpeeds supplyMeasuredRobotRelativeSpeeds() {
    Optional<ChassisSpeeds> speeds = readMeasuredSpeedsForValidation();
    if (speeds.isEmpty() && !faulted) {
      executionOutcome = ExecutionOutcome.INPUT_UNAVAILABLE;
      safeStop();
    }
    return speeds.orElse(fallbackSpeeds);
  }

  private Optional<ChassisSpeeds> readMeasuredSpeedsForValidation() {
    if (faulted) {
      return Optional.empty();
    }
    try {
      Optional<ChassisSpeeds> speeds =
          Objects.requireNonNull(
              swerveSubsystem.getMeasuredRobotRelativeSpeeds(),
              "measured speeds result");
      if (speeds.isEmpty()) {
        return Optional.empty();
      }
      if (!isFiniteChassisSpeeds(speeds.orElseThrow())) {
        latchFault("measured robot-relative speeds were nonfinite");
        return Optional.empty();
      }
      return Optional.of(copySpeeds(speeds.orElseThrow()));
    } catch (RuntimeException failure) {
      latchFault("measured-speed callback failed", failure);
      return Optional.empty();
    }
  }

  private void acceptOutput(ChassisSpeeds output) {
    if (faulted) {
      safeStop();
      return;
    }
    if (!DriverStation.isAutonomousEnabled()) {
      executionOutcome = ExecutionOutcome.MODE_LOSS;
      safeStop();
      return;
    }
    if (!isFiniteChassisSpeeds(output)) {
      latchFault("AutoBuilder produced nonfinite output");
      return;
    }
    try {
      swerveSubsystem.acceptChassisSpeeds(copySpeeds(output));
    } catch (RuntimeException failure) {
      latchFault("SwerveSubsystem rejected AutoBuilder output", failure);
    }
  }

  private Command stopCommand() {
    return Commands.runOnce(this::safeStop, (Subsystem) swerveSubsystem);
  }

  private double pathTimeoutSeconds(PathPlannerPath path) {
    PathPlannerTrajectory trajectory =
        path.getIdealTrajectory(robotConfig)
            .orElseThrow(
                () -> new IllegalStateException(
                    "execution path has no ideal trajectory"));
    double durationSeconds = trajectory.getTotalTimeSeconds();
    double timeoutSeconds =
        durationSeconds
            + Constants.HolonomicTrajectoryFollowingConstants.kTimeoutMarginSeconds;
    if (!Double.isFinite(durationSeconds)
        || durationSeconds <= 0.0
        || !Double.isFinite(timeoutSeconds)
        || timeoutSeconds <= durationSeconds) {
      throw new IllegalStateException("execution path duration is invalid");
    }
    return timeoutSeconds;
  }

  private PreflightResult faultedPreflight() {
    return new PreflightResult(
        PreflightStatus.FAULTED,
        PreflightReason.FATAL_ADAPTER_FAULT,
        false,
        0.0,
        0.0,
        false,
        false,
        firstFaultReason);
  }

  private static PreflightResult notReady(
      PreflightReason reason,
      boolean poseAvailable,
      double translationErrorMeters,
      double headingErrorRadians,
      boolean speedsAvailable,
      boolean pathValid,
      String detail) {
    return new PreflightResult(
        PreflightStatus.NOT_READY,
        reason,
        poseAvailable,
        translationErrorMeters,
        headingErrorRadians,
        speedsAvailable,
        pathValid,
        detail);
  }

  private void latchFault(String reason) {
    latchFault(reason, null);
  }

  private synchronized void latchFault(String reason, Throwable failure) {
    if (!faulted) {
      faulted = true;
      firstFaultReason = Objects.requireNonNull(reason, "reason");
      if (failure != null) {
        firstFaultReason = diagnosticReason(reason, failure);
      }
    }
    executionOutcome = ExecutionOutcome.FAULTED;
    safeStop();
  }

  private void safeStop() {
    try {
      swerveSubsystem.stop();
    } catch (RuntimeException ignored) {
      // Stop is the final safety authority; no further control action is safe here.
    }
  }

  private static boolean isFinitePose(Pose2d pose) {
    return pose != null
        && Double.isFinite(pose.getX())
        && Double.isFinite(pose.getY())
        && pose.getRotation() != null
        && Double.isFinite(pose.getRotation().getRadians());
  }

  private static void requireFinitePose(Pose2d pose, String name) {
    if (!isFinitePose(pose)) {
      throw new IllegalArgumentException(name + " pose must be finite");
    }
  }

  private static boolean isFiniteChassisSpeeds(ChassisSpeeds speeds) {
    return speeds != null
        && Double.isFinite(speeds.vxMetersPerSecond)
        && Double.isFinite(speeds.vyMetersPerSecond)
        && Double.isFinite(speeds.omegaRadiansPerSecond);
  }

  private static Pose2d copyPose(Pose2d pose) {
    return new Pose2d(
        pose.getX(),
        pose.getY(),
        new Rotation2d(pose.getRotation().getRadians()));
  }

  private static ChassisSpeeds copySpeeds(ChassisSpeeds speeds) {
    return new ChassisSpeeds(
        speeds.vxMetersPerSecond,
        speeds.vyMetersPerSecond,
        speeds.omegaRadiansPerSecond);
  }

  private Command composeSchedulerOwnedPathCommand(Command followPath, double timeoutSeconds) {
    Command timeout =
        Commands.sequence(
            Commands.waitSeconds(timeoutSeconds),
            Commands.runOnce(() -> executionOutcome = ExecutionOutcome.TIMEOUT));
    Command modeLoss =
        Commands.sequence(
            Commands.waitUntil(() -> !DriverStation.isAutonomousEnabled()),
            Commands.runOnce(() -> executionOutcome = ExecutionOutcome.MODE_LOSS));
    return Commands.race(Objects.requireNonNull(followPath, "followPath"), timeout, modeLoss)
        .finallyDo(this::finishSchedulerOwnedExecution)
        .withInterruptBehavior(Command.InterruptionBehavior.kCancelIncoming);
  }

  private void finishSchedulerOwnedExecution(boolean interrupted) {
    if (faulted || executionOutcome == ExecutionOutcome.FAULTED) {
      executionOutcome = ExecutionOutcome.FAULTED;
    } else if (executionOutcome == ExecutionOutcome.NONE) {
      executionOutcome = interrupted ? ExecutionOutcome.INTERRUPTED : ExecutionOutcome.COMPLETE;
    } else if (executionOutcome == ExecutionOutcome.INPUT_UNAVAILABLE) {
      // The output boundary already stopped the drivetrain and classified this outcome.
    }
    safeStop();
  }

  private static String diagnosticReason(String reason, Throwable failure) {
    String detail = failure.getMessage();
    String diagnostic = reason + ": " + failure.getClass().getSimpleName();
    return detail == null || detail.isBlank() ? diagnostic : diagnostic + ": " + detail;
  }
}
