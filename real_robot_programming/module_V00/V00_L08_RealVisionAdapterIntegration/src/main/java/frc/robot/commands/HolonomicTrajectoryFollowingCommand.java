// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.observation.SwerveObservation;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleSupplier;

/** Follows one transformed trajectory with bounded WPILib holonomic control. */
public final class HolonomicTrajectoryFollowingCommand extends Command {
  /** Immutable, validated follower-only configuration. */
  public record Configuration(
      double xKpPerSecond,
      double yKpPerSecond,
      double thetaKpPerSecond,
      double maxTranslationSpeedMetersPerSecond,
      double maxAngularSpeedRadiansPerSecond,
      double thetaProfileMaxVelocityRadiansPerSecond,
      double thetaProfileMaxAccelerationRadiansPerSecondSquared,
      double translationToleranceMeters,
      double headingToleranceRadians,
      double timeoutMarginSeconds) {
    public Configuration {
      requirePositiveFinite(xKpPerSecond, "xKpPerSecond");
      requirePositiveFinite(yKpPerSecond, "yKpPerSecond");
      requirePositiveFinite(thetaKpPerSecond, "thetaKpPerSecond");
      requirePositiveFinite(maxTranslationSpeedMetersPerSecond, "maxTranslationSpeedMetersPerSecond");
      requirePositiveFinite(maxAngularSpeedRadiansPerSecond, "maxAngularSpeedRadiansPerSecond");
      requirePositiveFinite(thetaProfileMaxVelocityRadiansPerSecond, "thetaProfileMaxVelocityRadiansPerSecond");
      requirePositiveFinite(thetaProfileMaxAccelerationRadiansPerSecondSquared, "thetaProfileMaxAccelerationRadiansPerSecondSquared");
      requireNonnegativeFinite(translationToleranceMeters, "translationToleranceMeters");
      requireNonnegativeFinite(headingToleranceRadians, "headingToleranceRadians");
      requirePositiveFinite(timeoutMarginSeconds, "timeoutMarginSeconds");
    }
  }

  private final SwerveSubsystem swerveSubsystem;
  private final Trajectory executionTrajectory;
  private final Rotation2d executionHolonomicHeading;
  private final Configuration configuration;
  private final DoubleSupplier monotonicClock;
  private final PIDController xController;
  private final PIDController yController;
  private final ProfiledPIDController thetaController;
  private final HolonomicDriveController holonomicDriveController;
  private final double trajectoryDurationSeconds;
  private final double hardTimeoutSeconds;
  private boolean initialized;
  private boolean finished;
  private double startTimeSeconds = Double.NaN;

  public HolonomicTrajectoryFollowingCommand(
      SwerveSubsystem swerveSubsystem,
      Trajectory executionTrajectory,
      Rotation2d executionHolonomicHeading,
      Configuration configuration,
      DoubleSupplier monotonicClock) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.executionTrajectory = validateTrajectory(executionTrajectory);
    this.executionHolonomicHeading = copyAndValidateRotation(executionHolonomicHeading);
    this.configuration = Objects.requireNonNull(configuration, "configuration");
    this.monotonicClock = Objects.requireNonNull(monotonicClock, "monotonicClock");
    trajectoryDurationSeconds = this.executionTrajectory.getTotalTimeSeconds();
    hardTimeoutSeconds = trajectoryDurationSeconds + configuration.timeoutMarginSeconds();
    if (!Double.isFinite(hardTimeoutSeconds) || hardTimeoutSeconds <= trajectoryDurationSeconds) {
      throw new IllegalArgumentException("hard timeout must be finite and after trajectory duration");
    }
    xController = new PIDController(configuration.xKpPerSecond(), 0.0, 0.0);
    yController = new PIDController(configuration.yKpPerSecond(), 0.0, 0.0);
    thetaController =
        new ProfiledPIDController(
            configuration.thetaKpPerSecond(),
            0.0,
            0.0,
            new TrapezoidProfile.Constraints(
                configuration.thetaProfileMaxVelocityRadiansPerSecond(),
                configuration.thetaProfileMaxAccelerationRadiansPerSecondSquared()));
    holonomicDriveController = new HolonomicDriveController(xController, yController, thetaController);
    addRequirements(swerveSubsystem);
  }

  @Override
  public void initialize() {
    initialized = true;
    finished = false;
    startTimeSeconds = Double.NaN;
    swerveSubsystem.stop();
    if (!DriverStation.isAutonomousEnabled()) {
      finished = true;
      return;
    }
    startTimeSeconds = monotonicClock.getAsDouble();
    if (!Double.isFinite(startTimeSeconds)) {
      failClosed();
      return;
    }
    Optional<Pose2d> estimatedPose = validEstimatedPose();
    if (estimatedPose.isEmpty() || !matchesStartPose(estimatedPose.orElseThrow())) {
      failClosed();
      return;
    }
    Pose2d currentPose = estimatedPose.orElseThrow();
    xController.reset();
    yController.reset();
    thetaController.reset(currentPose.getRotation().getRadians());
  }

  @Override
  public void execute() {
    if (!initialized || finished) {
      return;
    }
    if (!DriverStation.isAutonomousEnabled()) {
      failClosed();
      return;
    }
    double nowSeconds = monotonicClock.getAsDouble();
    double elapsedSeconds = nowSeconds - startTimeSeconds;
    if (!Double.isFinite(nowSeconds) || !Double.isFinite(elapsedSeconds) || elapsedSeconds < 0.0) {
      failClosed();
      return;
    }
    Optional<Pose2d> estimatedPose = validEstimatedPose();
    if (estimatedPose.isEmpty()) {
      failClosed();
      return;
    }
    Pose2d currentPose = estimatedPose.orElseThrow();
    if (elapsedSeconds >= trajectoryDurationSeconds && isAtFinalReference(currentPose)) {
      failClosed();
      return;
    }
    if (elapsedSeconds >= hardTimeoutSeconds) {
      failClosed();
      return;
    }
    Trajectory.State desiredState = executionTrajectory.sample(Math.min(elapsedSeconds, trajectoryDurationSeconds));
    if (!isFiniteState(desiredState)) {
      failClosed();
      return;
    }
    ChassisSpeeds requestedSpeeds =
        holonomicDriveController.calculate(currentPose, desiredState, executionHolonomicHeading);
    if (!isFiniteSpeeds(requestedSpeeds)) {
      failClosed();
      return;
    }
    ChassisSpeeds boundedSpeeds = boundRobotRelativeSpeeds(requestedSpeeds);
    try {
      swerveSubsystem.acceptChassisSpeeds(boundedSpeeds);
    } catch (RuntimeException exception) {
      failClosed();
      throw exception;
    }
  }

  @Override
  public boolean isFinished() {
    return initialized && finished;
  }

  @Override
  public void end(boolean interrupted) {
    swerveSubsystem.stop();
    initialized = false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return false;
  }

  private void failClosed() {
    finished = true;
    swerveSubsystem.stop();
  }

  private Optional<Pose2d> validEstimatedPose() {
    Optional<SwerveObservation> observation = swerveSubsystem.getObservation();
    if (observation.isEmpty() || observation.orElseThrow().estimatedPose().isEmpty()) {
      return Optional.empty();
    }
    SwerveObservation.EstimatedPoseObservation estimatedObservation =
        observation.orElseThrow().estimatedPose().orElseThrow();
    if (!estimatedObservation.measurementSampleValid()) {
      return Optional.empty();
    }
    Optional<Pose2d> estimatedPose = swerveSubsystem.getEstimatedPose();
    return estimatedPose.filter(HolonomicTrajectoryFollowingCommand::isFinitePose);
  }

  private boolean matchesStartPose(Pose2d currentPose) {
    return translationErrorMeters(currentPose, executionTrajectory.getInitialPose())
            <= configuration.translationToleranceMeters()
        && Math.abs(
                MathUtil.angleModulus(
                    executionTrajectory.getInitialPose().getRotation().getRadians()
                        - currentPose.getRotation().getRadians()))
            <= configuration.headingToleranceRadians();
  }

  private boolean isAtFinalReference(Pose2d currentPose) {
    Pose2d finalPose = executionTrajectory.sample(trajectoryDurationSeconds).poseMeters;
    return translationErrorMeters(currentPose, finalPose) <= configuration.translationToleranceMeters()
        && Math.abs(
                MathUtil.angleModulus(
                    executionHolonomicHeading.getRadians() - currentPose.getRotation().getRadians()))
            <= configuration.headingToleranceRadians();
  }

  private ChassisSpeeds boundRobotRelativeSpeeds(ChassisSpeeds speeds) {
    double vx = speeds.vxMetersPerSecond;
    double vy = speeds.vyMetersPerSecond;
    double magnitude = Math.hypot(vx, vy);
    if (magnitude > configuration.maxTranslationSpeedMetersPerSecond()) {
      double scale = configuration.maxTranslationSpeedMetersPerSecond() / magnitude;
      vx *= scale;
      vy *= scale;
    }
    return new ChassisSpeeds(
        vx,
        vy,
        MathUtil.clamp(
            speeds.omegaRadiansPerSecond,
            -configuration.maxAngularSpeedRadiansPerSecond(),
            configuration.maxAngularSpeedRadiansPerSecond()));
  }

  private static Trajectory validateTrajectory(Trajectory trajectory) {
    Objects.requireNonNull(trajectory, "executionTrajectory");
    double totalTime = trajectory.getTotalTimeSeconds();
    List<Trajectory.State> states = trajectory.getStates();
    if (!Double.isFinite(totalTime) || totalTime <= 0.0 || states == null || states.isEmpty()) {
      throw new IllegalArgumentException("executionTrajectory must be finite and nonempty");
    }
    double previousTime = Double.NEGATIVE_INFINITY;
    for (Trajectory.State state : states) {
      if (!isFiniteState(state) || state.timeSeconds < previousTime) {
        throw new IllegalArgumentException("executionTrajectory states must be finite and monotonic");
      }
      previousTime = state.timeSeconds;
    }
    return trajectory;
  }

  private static Rotation2d copyAndValidateRotation(Rotation2d rotation) {
    Objects.requireNonNull(rotation, "executionHolonomicHeading");
    if (!Double.isFinite(rotation.getRadians())) {
      throw new IllegalArgumentException("executionHolonomicHeading must be finite");
    }
    return new Rotation2d(rotation.getRadians());
  }

  private static boolean isFiniteState(Trajectory.State state) {
    return state != null
        && Double.isFinite(state.timeSeconds)
        && Double.isFinite(state.velocityMetersPerSecond)
        && Double.isFinite(state.accelerationMetersPerSecondSq)
        && Double.isFinite(state.curvatureRadPerMeter)
        && isFinitePose(state.poseMeters);
  }

  private static boolean isFinitePose(Pose2d pose) {
    return pose != null
        && Double.isFinite(pose.getX())
        && Double.isFinite(pose.getY())
        && Double.isFinite(pose.getRotation().getRadians());
  }

  private static boolean isFiniteSpeeds(ChassisSpeeds speeds) {
    return speeds != null
        && Double.isFinite(speeds.vxMetersPerSecond)
        && Double.isFinite(speeds.vyMetersPerSecond)
        && Double.isFinite(speeds.omegaRadiansPerSecond);
  }

  private static double translationErrorMeters(Pose2d first, Pose2d second) {
    return first.getTranslation().getDistance(second.getTranslation());
  }

  private static double requirePositiveFinite(double value, String name) {
    if (!Double.isFinite(value) || value <= 0.0) {
      throw new IllegalArgumentException(name + " must be finite and positive");
    }
    return value;
  }

  private static double requireNonnegativeFinite(double value, String name) {
    if (!Double.isFinite(value) || value < 0.0) {
      throw new IllegalArgumentException(name + " must be finite and nonnegative");
    }
    return value;
  }
}
