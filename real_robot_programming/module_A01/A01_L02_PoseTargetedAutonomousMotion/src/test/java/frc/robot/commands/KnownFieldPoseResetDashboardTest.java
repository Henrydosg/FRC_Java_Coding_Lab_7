// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the provisional learning pose source and dashboard registration. */
class KnownFieldPoseResetDashboardTest {
  private static final String DASHBOARD_LABEL = "Reset Known Starting Pose";

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

  @AfterEach
  void removeDashboardCommand() {
    NetworkTableInstance.getDefault()
        .getTable("SmartDashboard")
        .getEntry(DASHBOARD_LABEL)
        .unpublish();
  }

  @Test
  void learningPoseIsFiniteAndAtTheLearningFrameOrigin() {
    Pose2d learningPose = Constants.FieldConstants.kLearningStartingPose;

    assertEquals(0.0, learningPose.getX());
    assertEquals(0.0, learningPose.getY());
    assertEquals(0.0, learningPose.getRotation().getRadians());
    assertTrue(Double.isFinite(learningPose.getX()));
    assertTrue(Double.isFinite(learningPose.getY()));
    assertTrue(Double.isFinite(learningPose.getRotation().getRadians()));
  }

  @Test
  void registersTheExactDashboardLabelAndSubsystemRequirement() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand resetCommand =
        new ResetKnownFieldPoseCommand(subsystem, Constants.FieldConstants.kLearningStartingPose);
    new KnownFieldPoseResetDashboard(resetCommand);

    Command registeredCommand =
        assertInstanceOf(Command.class, SmartDashboard.getData(DASHBOARD_LABEL));

    assertSame(resetCommand, registeredCommand);
    assertTrue(registeredCommand.getRequirements().contains(subsystem));
  }

  @Test
  void passesTheConfiguredPoseUnchangedAndResetsOnceWhileDisabled() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand resetCommand =
        new ResetKnownFieldPoseCommand(subsystem, Constants.FieldConstants.kLearningStartingPose);
    new KnownFieldPoseResetDashboard(resetCommand);
    Command registeredCommand =
        assertInstanceOf(Command.class, SmartDashboard.getData(DASHBOARD_LABEL));

    CommandScheduler.getInstance().schedule(registeredCommand);
    CommandScheduler.getInstance().run();

    assertEquals(1, subsystem.resetCalls);
    assertSame(Constants.FieldConstants.kLearningStartingPose, subsystem.requestedPose);

    CommandScheduler.getInstance().schedule(registeredCommand);
    CommandScheduler.getInstance().run();

    assertEquals(2, subsystem.resetCalls);
    assertSame(Constants.FieldConstants.kLearningStartingPose, subsystem.requestedPose);
  }

  @Test
  void enabledExecutionDoesNotInvokeTheRegisteredReset() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand resetCommand =
        new ResetKnownFieldPoseCommand(subsystem, Constants.FieldConstants.kLearningStartingPose);
    new KnownFieldPoseResetDashboard(resetCommand);
    Command registeredCommand =
        assertInstanceOf(Command.class, SmartDashboard.getData(DASHBOARD_LABEL));

    registeredCommand.initialize();

    assertEquals(0, subsystem.resetCalls);
  }

  @Test
  void dashboardCommandDoesNotIssueDriveOrStopOutputs() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    ResetKnownFieldPoseCommand resetCommand =
        new ResetKnownFieldPoseCommand(subsystem, Constants.FieldConstants.kLearningStartingPose);
    new KnownFieldPoseResetDashboard(resetCommand);
    Command registeredCommand =
        assertInstanceOf(Command.class, SmartDashboard.getData(DASHBOARD_LABEL));

    registeredCommand.initialize();

    assertEquals(0, subsystem.totalOutputCalls());
    assertEquals(0, subsystem.totalStopCalls());
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private final RecordingModuleIO frontLeft;
    private final RecordingModuleIO frontRight;
    private final RecordingModuleIO backLeft;
    private final RecordingModuleIO backRight;
    private final boolean resetResult;
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
      super(frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
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

    private int totalOutputCalls() {
      return frontLeft.outputCalls
          + frontRight.outputCalls
          + backLeft.outputCalls
          + backRight.outputCalls;
    }

    private int totalStopCalls() {
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
