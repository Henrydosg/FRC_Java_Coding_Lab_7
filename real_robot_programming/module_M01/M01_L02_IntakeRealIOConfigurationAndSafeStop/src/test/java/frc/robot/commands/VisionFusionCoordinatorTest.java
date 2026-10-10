// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Transform3d;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.io.swerve.SwerveModuleIONoop;
import frc.robot.observation.vision.QualifiedVisionMeasurement;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import frc.robot.io.vision.VisionIOSim;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.vision.AprilTagFieldLayoutContract;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies the plain post-scheduler vision-to-Swerve handoff. */
class VisionFusionCoordinatorTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void handsOffOnlyTheLatestQualifiedMeasurementOncePerInvocation() {
    AprilTagFieldLayoutContract layout =
        AprilTagFieldLayoutContract.loadOfficial2026(
            Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
    VisionIOSim visionIO = new VisionIOSim(layout, Transform3d.kZero);
    visionIO.setFrame(
        VisionIOSim.Frame.targetsPresent(Pose3d.kZero, List.of(1), 20.0, 0.0));
    VisionSubsystem visionSubsystem =
        new VisionSubsystem(
            visionIO,
            layout,
            Transform3d.kZero,
            new Policy(100.0, 100.0, 100.0),
            1.0,
            () -> 20.0);
    RecordingSwerveSubsystem swerveSubsystem = new RecordingSwerveSubsystem();
    VisionFusionCoordinator coordinator =
        new VisionFusionCoordinator(visionSubsystem, swerveSubsystem);

    visionSubsystem.periodic();
    coordinator.periodic();

    assertEquals(1, swerveSubsystem.admissionCount);
    assertNotNull(swerveSubsystem.lastMeasurement);
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int admissionCount;
    private QualifiedVisionMeasurement lastMeasurement;

    private RecordingSwerveSubsystem() {
      super(
          new SwerveModuleIONoop(),
          new SwerveModuleIONoop(),
          new SwerveModuleIONoop(),
          new SwerveModuleIONoop(),
          new GyroIO() {
            @Override
            public void updateInputs(GyroIOInputs inputs) {}
          });
    }

    @Override
    public boolean admitVisionMeasurement(QualifiedVisionMeasurement measurement) {
      admissionCount++;
      lastMeasurement = measurement;
      return true;
    }
  }
}

