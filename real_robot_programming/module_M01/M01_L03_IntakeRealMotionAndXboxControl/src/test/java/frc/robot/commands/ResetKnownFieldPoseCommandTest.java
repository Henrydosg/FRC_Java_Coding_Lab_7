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
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the Disabled-only one-shot known-field-pose reset command. */
class ResetKnownFieldPoseCommandTest {
  private static final Pose2d kRequestedPose =
      new Pose2d(2.5, -1.25, Rotation2d.fromDegrees(35.0));

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    CommandScheduler.getInstance().cancelAll();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void requiresSwerveSubsystemAndRunsWhenDisabled() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    assertTrue(command.getRequirements().contains(subsystem));
    assertTrue(command.runsWhenDisabled());
  }

  @Test
  void disabledExecutionCallsResetOnceAndFinishes() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    command.initialize();
    command.execute();
    command.execute();

    assertTrue(command.isFinished());
    assertEquals(1, subsystem.resetCalls);
    assertEquals(kRequestedPose, subsystem.requestedPose);
  }

  @Test
  void acceptedResetProvidesOneShotStartingPoseReadiness() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    command.initialize();

    assertTrue(command.consumeAcceptedStartPose());
    assertFalse(command.consumeAcceptedStartPose());
  }

  @Test
  void rejectedResetProvidesNoStartingPoseReadiness() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(false);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    command.initialize();

    assertFalse(command.consumeAcceptedStartPose());
  }

  @Test
  void enabledAttemptClearsPreviouslyAcceptedReadiness() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    command.initialize();
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    command.initialize();

    assertFalse(command.consumeAcceptedStartPose());
    assertEquals(1, subsystem.resetCalls);
  }

  @Test
  void reschedulingClearsPreviousReadinessBeforeARejectedAttempt() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    command.initialize();
    subsystem.setResetResult(false);
    command.initialize();

    assertFalse(command.consumeAcceptedStartPose());
    assertEquals(2, subsystem.resetCalls);
  }

  @Test
  void sameCommandInstanceCanBeScheduledAgainWithoutRepeatingWithinOneSchedule() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(command);
    scheduler.run();
    scheduler.run();
    assertEquals(1, subsystem.resetCalls);

    scheduler.schedule(command);
    scheduler.run();
    assertEquals(2, subsystem.resetCalls);
  }

  @Test
  void enabledExecutionDoesNotInvokeReset() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    CommandScheduler.getInstance().schedule(command);
    CommandScheduler.getInstance().run();

    assertTrue(command.isFinished());
    assertEquals(0, subsystem.resetCalls);
  }

  @Test
  void rejectedSubsystemResultDoesNotRetry() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(false);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(command);
    scheduler.run();
    scheduler.run();

    assertTrue(command.isFinished());
    assertEquals(1, subsystem.resetCalls);

    scheduler.schedule(command);
    scheduler.run();
    assertEquals(2, subsystem.resetCalls);
  }

  @Test
  void commandDoesNotIssueDriveOrOutputRequests() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand command =
        new ResetKnownFieldPoseCommand(subsystem, kRequestedPose);

    command.initialize();

    assertEquals(0, subsystem.totalModuleOutputCalls());
    assertEquals(0, subsystem.totalModuleStopCalls());
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private final RecordingModuleIO frontLeft;
    private final RecordingModuleIO frontRight;
    private final RecordingModuleIO backLeft;
    private final RecordingModuleIO backRight;
    private boolean resetResult;
    private int resetCalls;
    private Pose2d requestedPose;

    private RecordingSwerveSubsystem(boolean resetResult) {
      this(
          resetResult,
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingModuleIO());
    }

    private RecordingSwerveSubsystem(
        boolean resetResult,
        RecordingModuleIO frontLeft,
        RecordingModuleIO frontRight,
        RecordingModuleIO backLeft,
        RecordingModuleIO backRight) {
      super(
          frontLeft,
          frontRight,
          backLeft,
          backRight,
          new RecordingGyroIO());
      this.frontLeft = frontLeft;
      this.frontRight = frontRight;
      this.backLeft = backLeft;
      this.backRight = backRight;
      this.resetResult = resetResult;
    }

    @Override
    public boolean resetKnownFieldPose(Pose2d requestedPose) {
      resetCalls++;
      this.requestedPose = requestedPose;
      return resetResult;
    }

    private void setResetResult(boolean resetResult) {
      this.resetResult = resetResult;
    }

    private int totalModuleOutputCalls() {
      return frontLeft.outputCalls
          + frontRight.outputCalls
          + backLeft.outputCalls
          + backRight.outputCalls;
    }

    private int totalModuleStopCalls() {
      return frontLeft.stopCalls
          + frontRight.stopCalls
          + backLeft.stopCalls
          + backRight.stopCalls;
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private int outputCalls;
    private int stopCalls;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {
      outputCalls++;
    }

    @Override
    public void setSteerOutput(double output) {
      outputCalls++;
    }

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      outputCalls++;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      outputCalls++;
    }

    @Override
    public void stop() {
      stopCalls++;
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
