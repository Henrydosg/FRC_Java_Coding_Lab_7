// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.drive;

import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import frc.robot.observation.drive.DriveObservation;
import java.util.Objects;

/**
 * Publishes immutable drivetrain observations without changing drivetrain behavior.
 */
public final class DriveTelemetryFacade implements AutoCloseable {
  private static final String kLeftAppliedOutputKey =
      "leftAppliedOutput";
  private static final String kRightAppliedOutputKey =
      "rightAppliedOutput";

  private final DoublePublisher leftAppliedOutputPublisher;
  private final DoublePublisher rightAppliedOutputPublisher;

  /**
   * Creates publishers for the supplied drivetrain table.
   *
   * @param driveTable drivetrain telemetry table
   */
  public DriveTelemetryFacade(NetworkTable driveTable) {
    Objects.requireNonNull(
        driveTable,
        "driveTable");

    leftAppliedOutputPublisher =
        driveTable
            .getDoubleTopic(kLeftAppliedOutputKey)
            .publish();

    rightAppliedOutputPublisher =
        driveTable
            .getDoubleTopic(kRightAppliedOutputKey)
            .publish();
  }

  /**
   * Publishes both applied outputs from one immutable observation.
   *
   * @param observation immutable drivetrain observation
   */
  public void publish(DriveObservation observation) {
    Objects.requireNonNull(
        observation,
        "observation");

    leftAppliedOutputPublisher.set(
        observation.leftAppliedOutput());

    rightAppliedOutputPublisher.set(
        observation.rightAppliedOutput());
  }

  /**
   * Closes the publisher handles owned by this facade.
   */
  @Override
  public void close() {
    leftAppliedOutputPublisher.close();
    rightAppliedOutputPublisher.close();
  }
}
