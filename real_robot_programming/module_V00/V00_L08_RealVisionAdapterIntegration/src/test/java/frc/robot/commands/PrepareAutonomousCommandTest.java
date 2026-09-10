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

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.commands.AutoBuilderContractAdapter.ExecutionOutcome;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightReason;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightResult;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightStatus;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PrepareAutonomousCommandTest {
  @Test
  void oneShotCommandOwnsSwerveAndSnapshotsRoutineAndAlliance() {
    RecordingActions actions = new RecordingActions();
    AutonomousPreparationCoordinator coordinator =
        new AutonomousPreparationCoordinator(
            actions, FieldVariant.REBUILT_WELDED, "test-path");
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            new Module(), new Module(), new Module(), new Module(), new Gyro());
    PrepareAutonomousCommand command =
        new PrepareAutonomousCommand(
            subsystem,
            coordinator,
            () -> AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            () -> Optional.of(Alliance.Blue));

    command.initialize();
    assertFalse(command.isFinished());
    command.execute();

    assertTrue(command.getRequirements().contains(subsystem));
    assertTrue(command.runsWhenDisabled());
    assertTrue(command.isFinished());
    assertEquals(State.READY, coordinator.getObservation().state());
    assertEquals("BLUE", coordinator.getObservation().alliance().name());
    assertEquals(1, actions.captureCount);
  }

  @Test
  void supplierFailureProducesNotReadyInsteadOfThrowing() {
    RecordingActions actions = new RecordingActions();
    AutonomousPreparationCoordinator coordinator =
        new AutonomousPreparationCoordinator(
            actions, FieldVariant.REBUILT_WELDED, "test-path");
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            new Module(), new Module(), new Module(), new Module(), new Gyro());
    PrepareAutonomousCommand command =
        new PrepareAutonomousCommand(
            subsystem,
            coordinator,
            () -> {
              throw new IllegalStateException("chooser unavailable");
            },
            () -> Optional.of(Alliance.Blue));

    command.initialize();

    assertEquals(State.NOT_READY, coordinator.getObservation().state());
  }

  private static final class RecordingActions
      implements AutonomousPreparationCoordinator.PreparationActions {
    private int captureCount;

    @Override
    public boolean isDisabled() {
      return true;
    }

    @Override
    public boolean captureFieldHeadingReference() {
      captureCount++;
      return true;
    }

    @Override
    public Pose2d canonicalStartingPose() {
      return Pose2d.kZero;
    }

    @Override
    public boolean resetKnownFieldPose(Pose2d pose) {
      return true;
    }

    @Override
    public PreflightResult preflight(AutonomousStartContext context) {
      return new PreflightResult(
          PreflightStatus.READY,
          PreflightReason.NONE,
          true,
          0.0,
          0.0,
          true,
          true,
          "");
    }

    @Override
    public boolean isAutoBuilderConfigured() {
      return true;
    }

    @Override
    public boolean isAdapterFaulted() {
      return false;
    }

    @Override
    public String firstFatalReason() {
      return "";
    }

    @Override
    public ExecutionOutcome executionOutcome() {
      return ExecutionOutcome.NONE;
    }

    @Override
    public void latchStaticPreparationFault(String reason, Throwable failure) {}
  }

  private static final class Module implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {}
  }

  private static final class Gyro implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
