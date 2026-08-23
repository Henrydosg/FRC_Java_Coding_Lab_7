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
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;

/** Integrates PathPlanner AutoBuilder with the frozen Swerve contracts. */
public final class AutoBuilderContractAdapter {
  private static final double kPoseToleranceMeters = 1.0e-9;
  private static final double kHeadingToleranceRadians = 1.0e-9;

  private final SwerveSubsystem swerveSubsystem;
  private final PathPlannerTrajectoryAdapter pathPlannerTrajectoryAdapter;
  private final RobotConfig robotConfig;
  private final Pose2d fallbackPose = Pose2d.kZero;
  private final ChassisSpeeds fallbackSpeeds = new ChassisSpeeds();

  private boolean configured;
  private boolean faulted;
  private String firstFaultReason = "";

  /**
   * Creates the AutoBuilder integration boundary.
   *
   * @param swerveSubsystem existing drivetrain, localization, and stop authority
   * @param pathPlannerTrajectoryAdapter existing canonical PathPlanner loader/validator
   * @param robotConfig composition-root-owned provisional RobotConfig
   */
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
   * Creates one safely decorated command for a previously accepted autonomous reset context.
   *
   * @param context accepted Disabled-only reset provenance
   * @return a command that follows the fresh transformed path or stops safely
   */
  public Command createPathCommand(AutonomousStartContext context) {
    if (context == null || faulted || !configured) {
      if (context == null) {
        latchFault("autonomous start context was null");
      }
      return stopCommand();
    }

    try {
      Pose2d currentPose = requirePoseForCommand();
      Optional<ChassisSpeeds> measuredSpeeds = readMeasuredSpeeds();
      if (measuredSpeeds.isEmpty()) {
        return stopCommand();
      }
      if (!samePose(currentPose, context.executionStartPose())) {
        latchFault("current pose does not match the accepted reset context");
        return stopCommand();
      }

      PathPlannerPath canonicalPath = pathPlannerTrajectoryAdapter.createCanonicalPath();
      PathPlannerPath executionPath =
          PathPlannerExecutionPathFactory.createExecutionPath(
              canonicalPath, context.fieldVariant(), context.alliance());
      Command followPath = AutoBuilder.followPath(executionPath);
      double timeoutSeconds = pathTimeoutSeconds(executionPath);
      return new SafeAutoBuilderCommand(followPath, timeoutSeconds, swerveSubsystem);
    } catch (RuntimeException failure) {
      latchFault("AutoBuilder path command creation failed", failure);
      return stopCommand();
    }
  }

  /** Creates one safely decorated command for the L09 event-enabled learning path. */
  public Command createEventPathCommand(AutonomousStartContext context) {
    if (context == null || faulted || !configured) {
      if (context == null) {
        latchFault("autonomous start context was null");
      }
      return stopCommand();
    }

    try {
      Pose2d currentPose = requirePoseForCommand();
      Optional<ChassisSpeeds> measuredSpeeds = readMeasuredSpeeds();
      if (measuredSpeeds.isEmpty()) {
        return stopCommand();
      }
      if (!samePose(currentPose, context.executionStartPose())) {
        latchFault("current pose does not match the accepted reset context");
        return stopCommand();
      }

      PathPlannerPath canonicalPath = pathPlannerTrajectoryAdapter.createCanonicalEventPath();
      PathPlannerPath executionPath =
          PathPlannerExecutionPathFactory.createExecutionPathWithEvents(
              canonicalPath, context.fieldVariant(), context.alliance());
      Command followPath = AutoBuilder.followPath(executionPath);
      double timeoutSeconds = pathTimeoutSeconds(executionPath);
      return new SafeAutoBuilderCommand(followPath, timeoutSeconds, swerveSubsystem);
    } catch (RuntimeException failure) {
      latchFault("AutoBuilder event path command creation failed", failure);
      return stopCommand();
    }
  }

  /** @return whether this adapter completed its one-time configuration. */
  public boolean isConfigured() {
    return configured;
  }

  /** @return whether this process-session adapter is latched faulted. */
  public boolean isFaulted() {
    return faulted;
  }

  /** @return the first fault reason, or an empty string before any fault. */
  public String firstFaultReason() {
    return firstFaultReason;
  }

  private Pose2d requirePoseForCommand() {
    Optional<Pose2d> pose = readPose();
    if (pose.isEmpty()) {
      throw new IllegalStateException("no valid estimated pose is available");
    }
    return pose.orElseThrow();
  }

  private Pose2d supplyPose() {
    return readPose().orElse(fallbackPose);
  }

  private Optional<Pose2d> readPose() {
    if (faulted) {
      return Optional.empty();
    }
    try {
      Optional<Pose2d> pose = swerveSubsystem.getEstimatedPose();
      if (pose.isEmpty() || !isFinitePose(pose.orElseThrow())) {
        latchFault("estimated pose was empty or nonfinite");
        return Optional.empty();
      }
      return Optional.of(copyPose(pose.orElseThrow()));
    } catch (RuntimeException failure) {
      latchFault("estimated pose callback failed", failure);
      return Optional.empty();
    }
  }

  private void resetPose(Pose2d requestedPose) {
    if (faulted || !isFinitePose(requestedPose) || DriverStation.isEnabled()) {
      latchFault("AutoBuilder reset rejected by pose or Disabled-only contract");
      return;
    }
    try {
      if (!swerveSubsystem.resetKnownFieldPose(copyPose(requestedPose))) {
        latchFault("SwerveSubsystem rejected the AutoBuilder reset");
      }
    } catch (RuntimeException failure) {
      latchFault("AutoBuilder reset callback failed", failure);
    }
  }

  private ChassisSpeeds supplyMeasuredRobotRelativeSpeeds() {
    return readMeasuredSpeeds().orElse(fallbackSpeeds);
  }

  private Optional<ChassisSpeeds> readMeasuredSpeeds() {
    if (faulted) {
      return Optional.empty();
    }
    try {
      Optional<ChassisSpeeds> speeds = swerveSubsystem.getMeasuredRobotRelativeSpeeds();
      if (speeds.isEmpty() || !isFiniteChassisSpeeds(speeds.orElseThrow())) {
        latchFault("measured robot-relative speeds were empty or nonfinite");
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
      latchFault("AutoBuilder output arrived outside Autonomous+Enabled");
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
            .orElseThrow(() -> new IllegalStateException("execution path has no ideal trajectory"));
    double durationSeconds = trajectory.getTotalTimeSeconds();
    double timeoutSeconds =
        durationSeconds + Constants.HolonomicTrajectoryFollowingConstants.kTimeoutMarginSeconds;
    if (!Double.isFinite(durationSeconds) || durationSeconds <= 0.0 || !Double.isFinite(timeoutSeconds)) {
      throw new IllegalStateException("execution path duration is invalid");
    }
    return timeoutSeconds;
  }

  private void latchFault(String reason) {
    latchFault(reason, null);
  }

  private synchronized void latchFault(String reason, Throwable failure) {
    if (!faulted) {
      faulted = true;
      firstFaultReason = reason;
      if (failure != null) {
        firstFaultReason = reason + ": " + failure.getClass().getSimpleName();
      }
    }
    safeStop();
  }

  private void safeStop() {
    try {
      swerveSubsystem.stop();
    } catch (RuntimeException ignored) {
      // Stop is the final safety authority; no further action is safe here.
    }
  }

  private static boolean samePose(Pose2d first, Pose2d second) {
    return first.getTranslation().getDistance(second.getTranslation()) <= kPoseToleranceMeters
        && Math.abs(
                first.getRotation().getRadians() - second.getRotation().getRadians())
            <= kHeadingToleranceRadians;
  }

  private static boolean isFinitePose(Pose2d pose) {
    return pose != null
        && Double.isFinite(pose.getX())
        && Double.isFinite(pose.getY())
        && pose.getRotation() != null
        && Double.isFinite(pose.getRotation().getRadians());
  }

  private static boolean isFiniteChassisSpeeds(ChassisSpeeds speeds) {
    return speeds != null
        && Double.isFinite(speeds.vxMetersPerSecond)
        && Double.isFinite(speeds.vyMetersPerSecond)
        && Double.isFinite(speeds.omegaRadiansPerSecond);
  }

  private static Pose2d copyPose(Pose2d pose) {
    return new Pose2d(
        pose.getX(), pose.getY(),
        new edu.wpi.first.math.geometry.Rotation2d(pose.getRotation().getRadians()));
  }

  private static ChassisSpeeds copySpeeds(ChassisSpeeds speeds) {
    return new ChassisSpeeds(
        speeds.vxMetersPerSecond, speeds.vyMetersPerSecond, speeds.omegaRadiansPerSecond);
  }

  /** Adds terminal safety and a bounded timeout around the vendor command. */
  private final class SafeAutoBuilderCommand extends Command {
    private final Command delegate;
    private final double timeoutSeconds;
    private final Timer timer = new Timer();
    private boolean finished;

    private SafeAutoBuilderCommand(
        Command delegate, double timeoutSeconds, SwerveSubsystem requirement) {
      this.delegate = Objects.requireNonNull(delegate, "delegate");
      this.timeoutSeconds = timeoutSeconds;
      addRequirements(requirement);
    }

    @Override
    public void initialize() {
      finished = false;
      timer.restart();
      if (faulted || !DriverStation.isAutonomousEnabled()) {
        if (!faulted) {
          safeStop();
        }
        finished = true;
        return;
      }
      try {
        delegate.initialize();
      } catch (RuntimeException failure) {
        latchFault("AutoBuilder command initialization failed", failure);
        finished = true;
      }
    }

    @Override
    public void execute() {
      if (finished || faulted) {
        return;
      }
      if (!DriverStation.isAutonomousEnabled()) {
        safeStop();
        finished = true;
        return;
      }
      if (timer.hasElapsed(timeoutSeconds)) {
        latchFault("AutoBuilder command timed out");
        finished = true;
        return;
      }
      try {
        delegate.execute();
      } catch (RuntimeException failure) {
        latchFault("AutoBuilder command execution failed", failure);
        finished = true;
      }
    }

    @Override
    public boolean isFinished() {
      if (finished || faulted) {
        return true;
      }
      if (!DriverStation.isAutonomousEnabled()) {
        safeStop();
        finished = true;
        return true;
      }
      if (timer.hasElapsed(timeoutSeconds)) {
        latchFault("AutoBuilder command timed out");
        finished = true;
        return true;
      }
      try {
        finished = delegate.isFinished();
      } catch (RuntimeException failure) {
        latchFault("AutoBuilder command completion check failed", failure);
        finished = true;
      }
      return finished;
    }

    @Override
    public void end(boolean interrupted) {
      try {
        delegate.end(interrupted || faulted || !DriverStation.isAutonomousEnabled());
      } catch (RuntimeException failure) {
        latchFault("AutoBuilder command termination failed", failure);
      } finally {
        timer.stop();
        safeStop();
      }
    }
  }
}
