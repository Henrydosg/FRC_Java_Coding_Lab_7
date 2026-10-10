// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the disabled-only lifecycle of the field-heading capture command. */
class CaptureFieldHeadingReferenceCommandTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetDriverStation() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void requiresSwerveRunsWhenDisabledAndFinishesAfterOneCaptureAttempt() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(true);
    CaptureFieldHeadingReferenceCommand command =
        new CaptureFieldHeadingReferenceCommand(subsystem);

    assertTrue(command.runsWhenDisabled());
    assertTrue(command.getRequirements().contains(subsystem));

    command.initialize();

    assertTrue(command.isFinished());
    assertTrue(command.wasCaptured());
    assertTrue(subsystem.captureCalled);
  }

  @Test
  void reportsRejectedCaptureWithoutAddingBehavior() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem(false);
    CaptureFieldHeadingReferenceCommand command =
        new CaptureFieldHeadingReferenceCommand(subsystem);

    command.initialize();

    assertTrue(command.isFinished());
    assertFalse(command.wasCaptured());
    assertTrue(subsystem.captureCalled);
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private final boolean captureResult;
    private boolean captureCalled;

    private RecordingSwerveSubsystem(boolean captureResult) {
      super(
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopGyroIO());
      this.captureResult = captureResult;
    }

    @Override
    public boolean captureFieldHeadingReference() {
      captureCalled = true;
      return captureResult;
    }
  }

  private static final class NoopModuleIO implements SwerveModuleIO {
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

  private static final class NoopGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
