// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.autonomous;

import edu.wpi.first.networktables.BooleanPublisher;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.IntegerPublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.StringPublisher;
import frc.robot.observation.autonomous.AutonomousPreparationObservation;
import java.util.Objects;

/** Publishes immutable autonomous-preparation diagnostics without controlling behavior. */
public final class AutonomousPreparationTelemetryFacade implements AutoCloseable {
  private final StringPublisher statePublisher;
  private final BooleanPublisher readyPublisher;
  private final IntegerPublisher attemptIdPublisher;
  private final StringPublisher reasonPublisher;
  private final StringPublisher routinePublisher;
  private final StringPublisher alliancePublisher;
  private final StringPublisher fieldVariantPublisher;
  private final StringPublisher pathIdentityPublisher;
  private final BooleanPublisher contextConsumedPublisher;
  private final BooleanPublisher stalePublisher;
  private final BooleanPublisher headingReferenceValidPublisher;
  private final BooleanPublisher poseAvailablePublisher;
  private final DoublePublisher translationErrorMetersPublisher;
  private final DoublePublisher headingErrorDegreesPublisher;
  private final BooleanPublisher measuredSpeedsAvailablePublisher;
  private final BooleanPublisher pathValidPublisher;
  private final BooleanPublisher autoBuilderConfiguredPublisher;
  private final BooleanPublisher adapterFatalFaultedPublisher;
  private final StringPublisher firstFatalReasonPublisher;
  private final StringPublisher returnedCommandPublisher;
  private final BooleanPublisher runningPublisher;

  /** Creates stable typed publishers below the supplied table. */
  public AutonomousPreparationTelemetryFacade(NetworkTable table) {
    Objects.requireNonNull(table, "table");
    statePublisher = table.getStringTopic("State").publish();
    readyPublisher = table.getBooleanTopic("Ready").publish();
    attemptIdPublisher = table.getIntegerTopic("AttemptId").publish();
    reasonPublisher = table.getStringTopic("Reason").publish();
    routinePublisher = table.getStringTopic("Routine").publish();
    alliancePublisher = table.getStringTopic("Alliance").publish();
    fieldVariantPublisher = table.getStringTopic("FieldVariant").publish();
    pathIdentityPublisher = table.getStringTopic("PathIdentity").publish();
    contextConsumedPublisher = table.getBooleanTopic("ContextConsumed").publish();
    stalePublisher = table.getBooleanTopic("Stale").publish();
    headingReferenceValidPublisher =
        table.getBooleanTopic("HeadingReferenceValid").publish();
    poseAvailablePublisher = table.getBooleanTopic("PoseAvailable").publish();
    translationErrorMetersPublisher =
        table.getDoubleTopic("PoseTranslationErrorMeters").publish();
    headingErrorDegreesPublisher =
        table.getDoubleTopic("PoseHeadingErrorDegrees").publish();
    measuredSpeedsAvailablePublisher =
        table.getBooleanTopic("MeasuredSpeedsAvailable").publish();
    pathValidPublisher = table.getBooleanTopic("PathValid").publish();
    autoBuilderConfiguredPublisher =
        table.getBooleanTopic("AutoBuilderConfigured").publish();
    adapterFatalFaultedPublisher =
        table.getBooleanTopic("AdapterFatalFaulted").publish();
    firstFatalReasonPublisher = table.getStringTopic("FirstFatalReason").publish();
    returnedCommandPublisher = table.getStringTopic("ReturnedCommand").publish();
    runningPublisher = table.getBooleanTopic("Running").publish();
  }

  /** Publishes exactly one immutable diagnostic sample. */
  public void publish(AutonomousPreparationObservation observation) {
    Objects.requireNonNull(observation, "observation");
    statePublisher.set(observation.state().name());
    readyPublisher.set(observation.ready());
    attemptIdPublisher.set(observation.attemptId());
    reasonPublisher.set(observation.reason().name());
    routinePublisher.set(observation.routine().name());
    alliancePublisher.set(observation.alliance().name());
    fieldVariantPublisher.set(observation.fieldVariant());
    pathIdentityPublisher.set(observation.pathIdentity());
    contextConsumedPublisher.set(observation.contextConsumed());
    stalePublisher.set(observation.stale());
    headingReferenceValidPublisher.set(observation.headingReferenceValid());
    poseAvailablePublisher.set(observation.poseAvailable());
    translationErrorMetersPublisher.set(observation.translationErrorMeters());
    headingErrorDegreesPublisher.set(Math.toDegrees(observation.headingErrorRadians()));
    measuredSpeedsAvailablePublisher.set(observation.measuredSpeedsAvailable());
    pathValidPublisher.set(observation.pathValid());
    autoBuilderConfiguredPublisher.set(observation.autoBuilderConfigured());
    adapterFatalFaultedPublisher.set(observation.adapterFatalFaulted());
    firstFatalReasonPublisher.set(observation.firstFatalReason());
    returnedCommandPublisher.set(observation.returnedCommand().name());
    runningPublisher.set(observation.running());
  }

  @Override
  public void close() {
    statePublisher.close();
    readyPublisher.close();
    attemptIdPublisher.close();
    reasonPublisher.close();
    routinePublisher.close();
    alliancePublisher.close();
    fieldVariantPublisher.close();
    pathIdentityPublisher.close();
    contextConsumedPublisher.close();
    stalePublisher.close();
    headingReferenceValidPublisher.close();
    poseAvailablePublisher.close();
    translationErrorMetersPublisher.close();
    headingErrorDegreesPublisher.close();
    measuredSpeedsAvailablePublisher.close();
    pathValidPublisher.close();
    autoBuilderConfiguredPublisher.close();
    adapterFatalFaultedPublisher.close();
    firstFatalReasonPublisher.close();
    returnedCommandPublisher.close();
    runningPublisher.close();
  }
}
