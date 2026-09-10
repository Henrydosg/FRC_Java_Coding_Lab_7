// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.telemetry.swerve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.observation.SwerveObservation;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;

/** Verifies read-only pose publication from immutable Swerve observations. */
class SwerveTelemetryFacadeTest {
  private static final double kTolerance = 1.0e-12;
  private static final double kUnsetNumericSentinel = -9876.5;

  @Test
  void publishesOdometryAndEstimatedPoseValuesAndValidityToStableKeys() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    NetworkTable swerveTable = instance.getTable("Swerve");
    Field2d field = new Field2d();
    AtomicReference<Field2d> registeredField = new AtomicReference<>();
    AtomicInteger registrationCount = new AtomicInteger();
    SwerveTelemetryFacade facade =
        new SwerveTelemetryFacade(
            swerveTable,
            field,
            registered -> {
              registeredField.set(registered);
              registrationCount.incrementAndGet();
            });

    facade.publish(
        observation(
            Optional.of(
                new SwerveObservation.PoseObservation(
                    1.25, -0.75, Math.PI / 2.0, true)),
            Optional.of(
                new SwerveObservation.EstimatedPoseObservation(
                    4.0, -5.0, Math.PI / 6.0, true))));

    NetworkTable poseTable = swerveTable.getSubTable("Pose");
    NetworkTable estimatedPoseTable = swerveTable.getSubTable("EstimatedPose");
    assertTrue(poseTable.getBooleanTopic("Available").getEntry(false).get());
    assertEquals(
        1.25,
        poseTable.getDoubleTopic("XMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        -0.75,
        poseTable.getDoubleTopic("YMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        90.0,
        poseTable.getDoubleTopic("HeadingDegrees").getEntry(0.0).get(),
        kTolerance);
    assertTrue(
        poseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(false)
            .get());
    assertTrue(estimatedPoseTable.getBooleanTopic("Available").getEntry(false).get());
    assertEquals(
        4.0,
        estimatedPoseTable.getDoubleTopic("XMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        -5.0,
        estimatedPoseTable.getDoubleTopic("YMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        30.0,
        estimatedPoseTable.getDoubleTopic("HeadingDegrees").getEntry(0.0).get(),
        kTolerance);
    assertTrue(
        estimatedPoseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(false)
            .get());
    assertSame(field, registeredField.get());
    assertEquals(1, registrationCount.get());
    assertEquals(1.25, field.getRobotPose().getX(), kTolerance);
    assertEquals(-0.75, field.getRobotPose().getY(), kTolerance);
    assertEquals(90.0, field.getRobotPose().getRotation().getDegrees(), kTolerance);

    facade.publish(
        observation(
            Optional.of(
                new SwerveObservation.PoseObservation(
                    2.0, 0.5, -Math.PI / 4.0, false)),
            Optional.of(
                new SwerveObservation.EstimatedPoseObservation(
                    4.0, -5.0, Math.PI / 6.0, false))));

    assertEquals(1, registrationCount.get());
    assertFalse(
        poseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(true)
            .get());
    assertTrue(estimatedPoseTable.getBooleanTopic("Available").getEntry(false).get());
    assertEquals(
        4.0,
        estimatedPoseTable.getDoubleTopic("XMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        -5.0,
        estimatedPoseTable.getDoubleTopic("YMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        30.0,
        estimatedPoseTable.getDoubleTopic("HeadingDegrees").getEntry(0.0).get(),
        kTolerance);
    assertFalse(
        estimatedPoseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(true)
            .get());

    facade.publish(
        observation(
            Optional.of(
                new SwerveObservation.PoseObservation(
                    2.0, 0.5, -Math.PI / 4.0, true)),
            Optional.of(
                new SwerveObservation.EstimatedPoseObservation(
                    4.5, 6.0, -Math.PI / 3.0, true))));

    assertTrue(poseTable.getBooleanTopic("MeasurementSampleValid").getEntry(false).get());
    assertTrue(estimatedPoseTable.getBooleanTopic("Available").getEntry(false).get());
    assertEquals(
        4.5,
        estimatedPoseTable.getDoubleTopic("XMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        6.0,
        estimatedPoseTable.getDoubleTopic("YMeters").getEntry(0.0).get(),
        kTolerance);
    assertEquals(
        -60.0,
        estimatedPoseTable.getDoubleTopic("HeadingDegrees").getEntry(0.0).get(),
        kTolerance);
    assertTrue(
        estimatedPoseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(false)
            .get());
    assertEquals(2.0, field.getRobotPose().getX(), kTolerance);
    assertEquals(0.5, field.getRobotPose().getY(), kTolerance);
    assertEquals(-45.0, field.getRobotPose().getRotation().getDegrees(), kTolerance);

    facade.close();
    instance.close();
  }

  @Test
  void publishesUnavailableWithoutFabricatingNumericPoseValues() {
    NetworkTableInstance instance = NetworkTableInstance.create();
    NetworkTable swerveTable = instance.getTable("Swerve");
    Field2d field = new Field2d();
    field.setRobotPose(new Pose2d(8.0, 9.0, Rotation2d.fromDegrees(30.0)));
    AtomicReference<Field2d> registeredField = new AtomicReference<>();
    SwerveTelemetryFacade facade =
        new SwerveTelemetryFacade(swerveTable, field, registeredField::set);

    facade.publish(observation(Optional.empty()));

    NetworkTable poseTable = swerveTable.getSubTable("Pose");
    NetworkTable estimatedPoseTable = swerveTable.getSubTable("EstimatedPose");
    assertFalse(poseTable.getBooleanTopic("Available").getEntry(true).get());
    assertFalse(
        poseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(true)
            .get());
    assertFalse(estimatedPoseTable.getBooleanTopic("Available").getEntry(true).get());
    assertFalse(
        estimatedPoseTable
            .getBooleanTopic("MeasurementSampleValid")
            .getEntry(true)
            .get());
    assertEquals(
        kUnsetNumericSentinel,
        poseTable
            .getDoubleTopic("XMeters")
            .getEntry(kUnsetNumericSentinel)
            .get(),
        kTolerance);
    assertEquals(
        kUnsetNumericSentinel,
        estimatedPoseTable
            .getDoubleTopic("XMeters")
            .getEntry(kUnsetNumericSentinel)
            .get(),
        kTolerance);
    assertEquals(
        kUnsetNumericSentinel,
        estimatedPoseTable
            .getDoubleTopic("YMeters")
            .getEntry(kUnsetNumericSentinel)
            .get(),
        kTolerance);
    assertEquals(
        kUnsetNumericSentinel,
        estimatedPoseTable
            .getDoubleTopic("HeadingDegrees")
            .getEntry(kUnsetNumericSentinel)
            .get(),
        kTolerance);
    assertEquals(
        kUnsetNumericSentinel,
        poseTable
            .getDoubleTopic("YMeters")
            .getEntry(kUnsetNumericSentinel)
            .get(),
        kTolerance);
    assertEquals(
        kUnsetNumericSentinel,
        poseTable
            .getDoubleTopic("HeadingDegrees")
            .getEntry(kUnsetNumericSentinel)
            .get(),
        kTolerance);
    assertNull(registeredField.get());
    assertEquals(8.0, field.getRobotPose().getX(), kTolerance);
    assertEquals(9.0, field.getRobotPose().getY(), kTolerance);
    assertEquals(30.0, field.getRobotPose().getRotation().getDegrees(), kTolerance);

    facade.close();
    instance.close();
  }

  private static SwerveObservation observation(
      Optional<SwerveObservation.PoseObservation> currentPose) {
    return observation(currentPose, Optional.empty());
  }

  private static SwerveObservation observation(
      Optional<SwerveObservation.PoseObservation> currentPose,
      Optional<SwerveObservation.EstimatedPoseObservation> estimatedPose) {
    SwerveObservation.ModuleObservation module =
        new SwerveObservation.ModuleObservation(
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            true,
            true,
            true,
            true,
            true,
            true);
    SwerveObservation.GyroObservation gyro =
        new SwerveObservation.GyroObservation(
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true, true);
    return new SwerveObservation(
        module, module, module, module, gyro, currentPose, estimatedPose);
  }
}
