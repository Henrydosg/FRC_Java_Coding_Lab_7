// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.vision;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.vision.VisionObservation;
import frc.robot.observation.vision.VisionObservation.TargetObservation;
import java.util.Objects;

/** Publishes a read-only diagnostic subset of the immutable vision observation. */
public final class VisionTelemetryFacade implements AutoCloseable {
  private static final int kNoTargetId = -1;
  private static final double kNoTargetCoordinate = Double.NaN;

  private final BooleanPublisher availablePublisher;
  private final BooleanPublisher connectedPublisher;
  private final BooleanPublisher sampleValidPublisher;
  private final StringPublisher statePublisher;
  private final IntegerPublisher targetCountPublisher;
  private final IntegerPublisher firstTargetIdPublisher;
  private final DoublePublisher firstTargetXMetersPublisher;
  private final DoublePublisher firstTargetYMetersPublisher;
  private final DoublePublisher firstTargetZMetersPublisher;

  /**
   * Creates typed publishers under the Vision telemetry table.
   *
   * @param visionTable root Vision telemetry table
   */
  public VisionTelemetryFacade(NetworkTable visionTable) {
    NetworkTable requiredTable = Objects.requireNonNull(visionTable, "visionTable");
    availablePublisher = requiredTable.getBooleanTopic("Available").publish();
    connectedPublisher = requiredTable.getBooleanTopic("Connected").publish();
    sampleValidPublisher = requiredTable.getBooleanTopic("SampleValid").publish();
    statePublisher = requiredTable.getStringTopic("State").publish();
    targetCountPublisher = requiredTable.getIntegerTopic("TargetCount").publish();
    firstTargetIdPublisher = requiredTable.getIntegerTopic("FirstTargetId").publish();
    firstTargetXMetersPublisher = requiredTable.getDoubleTopic("FirstTargetXMeters").publish();
    firstTargetYMetersPublisher = requiredTable.getDoubleTopic("FirstTargetYMeters").publish();
    firstTargetZMetersPublisher = requiredTable.getDoubleTopic("FirstTargetZMeters").publish();
  }

  /** Publishes only immutable observation values; it never controls behavior. */
  public void publish(VisionObservation observation) {
    VisionObservation requiredObservation = Objects.requireNonNull(observation, "observation");
    availablePublisher.set(
        requiredObservation.state() != VisionObservation.State.UNAVAILABLE);
    connectedPublisher.set(
        requiredObservation.state() != VisionObservation.State.UNAVAILABLE
            && requiredObservation.state() != VisionObservation.State.DISCONNECTED);
    sampleValidPublisher.set(
        requiredObservation.state() == VisionObservation.State.NO_TARGETS
            || requiredObservation.state() == VisionObservation.State.TARGETS_PRESENT);
    statePublisher.set(requiredObservation.state().name());
    targetCountPublisher.set(requiredObservation.targets().size());

    if (requiredObservation.targets().isEmpty()) {
      firstTargetIdPublisher.set(kNoTargetId);
      firstTargetXMetersPublisher.set(kNoTargetCoordinate);
      firstTargetYMetersPublisher.set(kNoTargetCoordinate);
      firstTargetZMetersPublisher.set(kNoTargetCoordinate);
      return;
    }

    TargetObservation target = requiredObservation.targets().get(0);
    firstTargetIdPublisher.set(target.tagId());
    firstTargetXMetersPublisher.set(target.cameraToTarget().getX());
    firstTargetYMetersPublisher.set(target.cameraToTarget().getY());
    firstTargetZMetersPublisher.set(target.cameraToTarget().getZ());
  }

  /** Closes every publisher handle owned by this facade. */
  @Override
  public void close() {
    availablePublisher.close();
    connectedPublisher.close();
    sampleValidPublisher.close();
    statePublisher.close();
    targetCountPublisher.close();
    firstTargetIdPublisher.close();
    firstTargetXMetersPublisher.close();
    firstTargetYMetersPublisher.close();
    firstTargetZMetersPublisher.close();
  }
}
