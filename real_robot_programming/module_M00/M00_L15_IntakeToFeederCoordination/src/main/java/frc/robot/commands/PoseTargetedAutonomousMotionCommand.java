// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.observation.SwerveObservation;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleSupplier;

/** Drives one finite field-relative target pose using bounded proportional control. */
public final class PoseTargetedAutonomousMotionCommand extends Command {
  private final SwerveSubsystem swerveSubsystem;
  private final Pose2d targetPose;
  private final double translationKpPerSecond;
  private final double headingKpPerSecond;
  private final double maxTranslationSpeedMetersPerSecond;
  private final double maxAngularSpeedRadiansPerSecond;
  private final double translationToleranceMeters;
  private final double headingToleranceRadians;
  private final double timeoutSeconds;
  private final DoubleSupplier monotonicClock;

  private double startTimeSeconds = Double.NaN;
  private boolean initialized;
  private boolean finished;

  /**
   * Creates one pose-targeted autonomous motion command.
   *
   * @param swerveSubsystem drivetrain subsystem that owns pose feedback, actuation, and stopping
   * @param targetPose finite field-relative target pose
   * @param translationKpPerSecond positive translation proportional gain
   * @param headingKpPerSecond positive heading proportional gain
   * @param maxTranslationSpeedMetersPerSecond positive translation vector magnitude limit
   * @param maxAngularSpeedRadiansPerSecond positive angular speed limit
   * @param translationToleranceMeters nonnegative translation completion tolerance
   * @param headingToleranceRadians nonnegative heading completion tolerance
   * @param timeoutSeconds finite positive command timeout
   * @param monotonicClock monotonic time source in seconds
   */
  public PoseTargetedAutonomousMotionCommand(
      SwerveSubsystem swerveSubsystem,
      Pose2d targetPose,
      double translationKpPerSecond,
      double headingKpPerSecond,
      double maxTranslationSpeedMetersPerSecond,
      double maxAngularSpeedRadiansPerSecond,
      double translationToleranceMeters,
      double headingToleranceRadians,
      double timeoutSeconds,
      DoubleSupplier monotonicClock) {
    this.swerveSubsystem = Objects.requireNonNull(swerveSubsystem, "swerveSubsystem");
    this.targetPose = copyAndValidatePose(targetPose);
    this.translationKpPerSecond =
        requirePositiveFinite(translationKpPerSecond, "translationKpPerSecond");
    this.headingKpPerSecond = requirePositiveFinite(headingKpPerSecond, "headingKpPerSecond");
    this.maxTranslationSpeedMetersPerSecond =
        requirePositiveFinite(
            maxTranslationSpeedMetersPerSecond, "maxTranslationSpeedMetersPerSecond");
    this.maxAngularSpeedRadiansPerSecond =
        requirePositiveFinite(
            maxAngularSpeedRadiansPerSecond, "maxAngularSpeedRadiansPerSecond");
    this.translationToleranceMeters =
        requireNonnegativeFinite(translationToleranceMeters, "translationToleranceMeters");
    this.headingToleranceRadians =
        requireNonnegativeFinite(headingToleranceRadians, "headingToleranceRadians");
    this.timeoutSeconds = requirePositiveFinite(timeoutSeconds, "timeoutSeconds");
    this.monotonicClock = Objects.requireNonNull(monotonicClock, "monotonicClock");
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
      finished = true;
    }
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
    if (!Double.isFinite(nowSeconds)
        || !Double.isFinite(elapsedSeconds)
        || elapsedSeconds < 0.0
        || elapsedSeconds >= timeoutSeconds) {
      failClosed();
      return;
    }

    if (!hasValidEstimatedPoseObservation()) {
      failClosed();
      return;
    }

    Optional<Pose2d> estimatedPose = swerveSubsystem.getEstimatedPose();
    if (estimatedPose.isEmpty() || !isFinitePose(estimatedPose.orElseThrow())) {
      failClosed();
      return;
    }

    Pose2d currentPose = estimatedPose.orElseThrow();
    double errorX = targetPose.getX() - currentPose.getX();
    double errorY = targetPose.getY() - currentPose.getY();
    double errorHeading =
        MathUtil.angleModulus(
            targetPose.getRotation().getRadians() - currentPose.getRotation().getRadians());
    if (!Double.isFinite(errorX)
        || !Double.isFinite(errorY)
        || !Double.isFinite(errorHeading)) {
      failClosed();
      return;
    }

    double translationErrorMagnitude = Math.hypot(errorX, errorY);
    boolean translationWithinTolerance =
        translationErrorMagnitude <= translationToleranceMeters;
    boolean headingWithinTolerance = Math.abs(errorHeading) <= headingToleranceRadians;
    if (translationWithinTolerance && headingWithinTolerance) {
      finished = true;
      swerveSubsystem.stop();
      return;
    }

    double vxMetersPerSecond =
        translationWithinTolerance ? 0.0 : errorX * translationKpPerSecond;
    double vyMetersPerSecond =
        translationWithinTolerance ? 0.0 : errorY * translationKpPerSecond;
    double translationCommandMagnitude =
        Math.hypot(vxMetersPerSecond, vyMetersPerSecond);
    if (translationCommandMagnitude > maxTranslationSpeedMetersPerSecond) {
      double scale = maxTranslationSpeedMetersPerSecond / translationCommandMagnitude;
      vxMetersPerSecond *= scale;
      vyMetersPerSecond *= scale;
    }

    double omegaRadiansPerSecond =
        headingWithinTolerance ? 0.0 : errorHeading * headingKpPerSecond;
    omegaRadiansPerSecond =
        MathUtil.clamp(
            omegaRadiansPerSecond,
            -maxAngularSpeedRadiansPerSecond,
            maxAngularSpeedRadiansPerSecond);

    if (!Double.isFinite(vxMetersPerSecond)
        || !Double.isFinite(vyMetersPerSecond)
        || !Double.isFinite(omegaRadiansPerSecond)) {
      failClosed();
      return;
    }

    swerveSubsystem.acceptFieldRelativeChassisSpeeds(
        new ChassisSpeeds(vxMetersPerSecond, vyMetersPerSecond, omegaRadiansPerSecond));
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

  private boolean hasValidEstimatedPoseObservation() {
    Optional<SwerveObservation> observation = swerveSubsystem.getObservation();
    if (observation.isEmpty()) {
      return false;
    }
    Optional<SwerveObservation.EstimatedPoseObservation> estimatedPose =
        observation.orElseThrow().estimatedPose();
    return estimatedPose.isPresent() && estimatedPose.orElseThrow().measurementSampleValid();
  }

  private static Pose2d copyAndValidatePose(Pose2d pose) {
    Objects.requireNonNull(pose, "targetPose");
    if (!isFinitePose(pose)) {
      throw new IllegalArgumentException("targetPose must be finite");
    }
    return new Pose2d(
        pose.getX(), pose.getY(), pose.getRotation());
  }

  private static boolean isFinitePose(Pose2d pose) {
    return pose != null
        && Double.isFinite(pose.getX())
        && Double.isFinite(pose.getY())
        && Double.isFinite(pose.getRotation().getRadians());
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
