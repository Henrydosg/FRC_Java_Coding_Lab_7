// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllianceAwareAutonomousStartPoseResetCommandTest {
  private static final AutonomousStartContext kBlueContext =
      new AutonomousStartContext(
          FieldVariant.REBUILT_WELDED, Alliance.Blue, new Pose2d(0.0, 0.0, new Rotation2d()));
  private static final AutonomousStartContext kRedContext =
      new AutonomousStartContext(
          FieldVariant.REBUILT_ANDYMARK,
          Alliance.Red,
          new Pose2d(16.518, 8.043, Rotation2d.kPi));

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void acceptedBlueAndRedContextsResetTheirExactExecutionPosesOnce() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    AllianceAwareAutonomousStartPoseResetCommand command =
        new AllianceAwareAutonomousStartPoseResetCommand(subsystem, () -> Optional.of(kBlueContext));

    command.initialize();
    assertEquals(kBlueContext, command.consumeAcceptedStartContext().orElseThrow());
    assertFalse(command.consumeAcceptedStartContext().isPresent());
    assertEquals(kBlueContext.executionStartPose(), subsystem.requestedPose);

    AllianceAwareAutonomousStartPoseResetCommand redCommand =
        new AllianceAwareAutonomousStartPoseResetCommand(subsystem, () -> Optional.of(kRedContext));
    redCommand.initialize();
    assertEquals(kRedContext, redCommand.consumeAcceptedStartContext().orElseThrow());
    assertEquals(kRedContext.executionStartPose(), subsystem.requestedPose);
  }

  @Test
  void unknownOrRejectedContextCreatesNoReadinessAndEnabledModeDoesNotQuerySupplier() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(false);
    AllianceAwareAutonomousStartPoseResetCommand rejected =
        new AllianceAwareAutonomousStartPoseResetCommand(subsystem, () -> Optional.of(kBlueContext));
    rejected.initialize();
    assertTrue(rejected.consumeAcceptedStartContext().isEmpty());
    assertEquals(1, subsystem.resetCalls);

    AllianceAwareAutonomousStartPoseResetCommand unknown =
        new AllianceAwareAutonomousStartPoseResetCommand(subsystem, Optional::empty);
    unknown.initialize();
    assertTrue(unknown.consumeAcceptedStartContext().isEmpty());
    assertEquals(1, subsystem.resetCalls);

    AtomicInteger supplierCalls = new AtomicInteger();
    AllianceAwareAutonomousStartPoseResetCommand enabled =
        new AllianceAwareAutonomousStartPoseResetCommand(
            subsystem,
            () -> {
              supplierCalls.incrementAndGet();
              return Optional.of(kBlueContext);
            });
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    enabled.initialize();
    assertEquals(0, supplierCalls.get());
    assertTrue(enabled.consumeAcceptedStartContext().isEmpty());
  }

  @Test
  void requiresSwerveAndRunsWhenDisabled() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    AllianceAwareAutonomousStartPoseResetCommand command =
        new AllianceAwareAutonomousStartPoseResetCommand(subsystem, () -> Optional.of(kBlueContext));
    assertTrue(command.getRequirements().contains(subsystem));
    assertTrue(command.runsWhenDisabled());
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private final boolean resetResult;
    private int resetCalls;
    private Pose2d requestedPose;

    private RecordingSwerveSubsystem(boolean resetResult) {
      super(new Module(), new Module(), new Module(), new Module(), new Gyro());
      this.resetResult = resetResult;
    }

    @Override
    public boolean resetKnownFieldPose(Pose2d requestedPose) {
      resetCalls++;
      this.requestedPose = requestedPose;
      return resetResult;
    }
  }

  private static final class Module implements SwerveModuleIO {
    @Override public void updateInputs(SwerveModuleIOInputs inputs) {}
    @Override public void setDriveOutput(double output) {}
    @Override public void setSteerOutput(double output) {}
    @Override public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}
    @Override public void setSteerAngle(Rotation2d angle) {}
    @Override public void stop() {}
  }

  private static final class Gyro implements GyroIO {
    @Override public void updateInputs(GyroIOInputs inputs) {}
  }
}
