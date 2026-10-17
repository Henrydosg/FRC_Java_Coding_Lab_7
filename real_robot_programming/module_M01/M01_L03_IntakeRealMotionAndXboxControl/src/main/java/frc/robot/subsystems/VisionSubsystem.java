// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.io.vision.VisionIO;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import frc.robot.observation.vision.QualifiedVisionMeasurement;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import frc.robot.observation.vision.VisionObservation;
import frc.robot.observation.vision.VisionObservation.State;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import frc.robot.observation.vision.VisionTiming;
import frc.robot.observation.vision.VisionTimingEvaluator;
import frc.robot.vision.AprilTagFieldLayoutContract;
import frc.robot.vision.AprilTagRobotPoseEstimator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleSupplier;

/**
 * Owns one selected VisionIO source and publishes raw observations plus qualified measurements.
 *
 * <p>Qualification is kept here because it joins the raw observation with the field layout, fixed
 * camera extrinsic, inherited quality policy, and inherited timing policy. This subsystem never
 * depends on SwerveSubsystem and never mutates an estimator.
 */
public final class VisionSubsystem extends SubsystemBase {
  private final VisionIO visionIO;
  private final AprilTagFieldLayoutContract fieldLayout;
  private final Transform3d robotToCamera;
  private final Policy qualityPolicy;
  private final double maximumFreshAgeSeconds;
  private final DoubleSupplier referenceTimestampSupplier;
  private final VisionIOInputs inputs = new VisionIOInputs();
  private VisionObservation latestObservation = new VisionObservation(State.UNAVAILABLE, List.of());
  private Optional<QualifiedVisionMeasurement> latestQualifiedMeasurement = Optional.empty();
  private double lastQualifiedTimestampSeconds = Double.NEGATIVE_INFINITY;

  /**
   * Creates the inherited observation-only owner without an enabled qualification pipeline.
   *
   * <p>This compatibility constructor remains useful for raw-observation tests. Runtime fusion
   * wiring uses the fully configured constructor below.
   *
   * @param visionIO selected real or simulation vision source
   */
  public VisionSubsystem(VisionIO visionIO) {
    this(visionIO, null, null, null, Double.NaN, Timer::getFPGATimestamp);
  }

  /**
   * Creates the observation and qualification owner for one selected vision implementation.
   *
   * @param visionIO selected real or simulation vision source
   * @param fieldLayout canonical field-to-tag lookup
   * @param robotToCamera fixed robot-to-camera extrinsic
   * @param qualityPolicy inherited L06 distance policy
   * @param maximumFreshAgeSeconds inherited L07 freshness policy
   * @param referenceTimestampSupplier explicit robot-time reference source
   */
  public VisionSubsystem(
      VisionIO visionIO,
      AprilTagFieldLayoutContract fieldLayout,
      Transform3d robotToCamera,
      Policy qualityPolicy,
      double maximumFreshAgeSeconds,
      DoubleSupplier referenceTimestampSupplier) {
    this.visionIO = Objects.requireNonNull(visionIO, "visionIO");
    this.fieldLayout = fieldLayout;
    this.robotToCamera = robotToCamera;
    this.qualityPolicy = qualityPolicy;
    this.maximumFreshAgeSeconds = maximumFreshAgeSeconds;
    this.referenceTimestampSupplier =
        Objects.requireNonNull(referenceTimestampSupplier, "referenceTimestampSupplier");
    if ((fieldLayout == null)
        || (robotToCamera == null)
        || (qualityPolicy == null)
        || !Double.isFinite(maximumFreshAgeSeconds)
        || maximumFreshAgeSeconds < 0.0) {
      if (fieldLayout != null
          || robotToCamera != null
          || qualityPolicy != null
          || Double.isFinite(maximumFreshAgeSeconds)) {
        throw new IllegalArgumentException(
            "qualification dependencies must be complete and freshness must be finite");
      }
    }
  }

  /** Refreshes IO once and replaces the immutable observation and qualification snapshots. */
  @Override
  public void periodic() {
    visionIO.updateInputs(inputs);
    latestObservation = toObservation(inputs);
    latestQualifiedMeasurement = qualify(inputs, latestObservation);
  }

  /**
   * Returns the latest immutable vendor-neutral observation.
   *
   * @return latest observation; initially UNAVAILABLE before the first periodic cycle
   */
  public VisionObservation getObservation() {
    return latestObservation;
  }

  /**
   * Returns the latest accepted immutable measurement, if the current cycle qualified one.
   *
   * @return latest qualified measurement or empty when no current measurement is eligible
   */
  public Optional<QualifiedVisionMeasurement> getLatestQualifiedMeasurement() {
    return latestQualifiedMeasurement;
  }

  private Optional<QualifiedVisionMeasurement> qualify(
      VisionIOInputs currentInputs, VisionObservation observation) {
    if (fieldLayout == null
        || robotToCamera == null
        || qualityPolicy == null
        || !currentInputs.available
        || !currentInputs.connected
        || !currentInputs.sampleValid
        || !currentInputs.timingValid
        || observation.state() != State.TARGETS_PRESENT
        || observation.targets().isEmpty()
        || !Double.isFinite(currentInputs.receiveTimestampSeconds)
        || !Double.isFinite(currentInputs.totalLatencySeconds)
        || currentInputs.totalLatencySeconds < 0.0) {
      return Optional.empty();
    }

    TargetObservation target = observation.targets().get(0);
    try {
      Pose2d fieldRobotPose =
          AprilTagRobotPoseEstimator
              .estimateFieldToRobotCandidate(
                  fieldLayout
                      .getTagPose(target.tagId())
                      .orElseThrow(
                          () ->
                              new IllegalArgumentException(
                                  "target tag is not present in the selected field")),
                  target.cameraToTarget(),
                  robotToCamera)
              .toPose2d();

      var quality = VisionMeasurementQualityEvaluator.evaluate(target, qualityPolicy);
      if (quality.acceptance()
          != frc.robot.observation.vision.VisionMeasurementQuality.Acceptance.ACCEPTED) {
        return Optional.empty();
      }

      VisionTiming timing =
          new VisionTiming(
              currentInputs.receiveTimestampSeconds, currentInputs.totalLatencySeconds);
      double referenceTimestampSeconds = referenceTimestampSupplier.getAsDouble();
      if (!Double.isFinite(referenceTimestampSeconds)
          || VisionTimingEvaluator.classifyFreshness(
                  timing, referenceTimestampSeconds, maximumFreshAgeSeconds)
              != VisionTimingEvaluator.Freshness.FRESH) {
        return Optional.empty();
      }

      double measurementTimestampSeconds = timing.measurementTimestampSeconds();
      if (measurementTimestampSeconds <= lastQualifiedTimestampSeconds) {
        return Optional.empty();
      }
      QualifiedVisionMeasurement qualified =
          new QualifiedVisionMeasurement(fieldRobotPose, measurementTimestampSeconds, quality);
      lastQualifiedTimestampSeconds = measurementTimestampSeconds;
      return Optional.of(qualified);
    } catch (RuntimeException failure) {
      return Optional.empty();
    }
  }

  private static VisionObservation toObservation(VisionIOInputs inputs) {
    if (!inputs.available) {
      return new VisionObservation(State.UNAVAILABLE, List.of());
    }
    if (!inputs.connected) {
      return new VisionObservation(State.DISCONNECTED, List.of());
    }
    if (!inputs.sampleValid) {
      return new VisionObservation(State.INVALID_SAMPLE, List.of());
    }
    if (inputs.targets == null || inputs.targets.isEmpty()) {
      return new VisionObservation(State.NO_TARGETS, List.of());
    }

    try {
      List<TargetObservation> targets = new ArrayList<>(inputs.targets.size());
      for (VisionTargetInputs target : inputs.targets) {
        if (target == null) {
          throw new IllegalArgumentException("target input must not be null");
        }
        targets.add(new TargetObservation(target.tagId(), target.cameraToTarget()));
      }
      return new VisionObservation(State.TARGETS_PRESENT, targets);
    } catch (IllegalArgumentException exception) {
      return new VisionObservation(State.INVALID_SAMPLE, List.of());
    }
  }
}

