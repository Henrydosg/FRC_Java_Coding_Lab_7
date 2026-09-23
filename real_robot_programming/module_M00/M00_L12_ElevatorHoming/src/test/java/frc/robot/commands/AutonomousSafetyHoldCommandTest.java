// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify and/or share it under the terms of
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
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutonomousSafetyHoldCommandTest {
  private static final double kTolerance = 1.0e-9;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableAutonomous() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
  }

  @AfterEach
  void resetRuntimeState() {
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void ownsSwerveForAutonomousAndRejectsIncomingRequirementConflicts() {
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand hold = new AutonomousSafetyHoldCommand(rig.subsystem);
    Command incoming = Commands.run(() -> {}, rig.subsystem);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    assertTrue(hold.getRequirements().contains(rig.subsystem));
    assertFalse(hold.runsWhenDisabled());
    assertEquals(Command.InterruptionBehavior.kCancelIncoming, hold.getInterruptionBehavior());

    scheduler.schedule(hold);
    scheduler.run();
    scheduler.run();
    scheduler.schedule(incoming);
    scheduler.run();

    assertTrue(hold.isScheduled());
    assertFalse(incoming.isScheduled());
    assertEquals(1, rig.subsystem.stopCount);
    assertEquals(0, rig.subsystem.acceptCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void modeExitEndsSessionHoldAndStopsAgain() {
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand hold = new AutonomousSafetyHoldCommand(rig.subsystem);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(hold);
    scheduler.run();
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    scheduler.run();

    assertFalse(hold.isScheduled());
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void explicitCancellationStopsAndReleasesSwerve() {
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand hold = new AutonomousSafetyHoldCommand(rig.subsystem);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(hold);
    scheduler.run();
    scheduler.cancel(hold);

    assertFalse(hold.isScheduled());
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void disabledSchedulingDoesNotInitialize() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand hold = new AutonomousSafetyHoldCommand(rig.subsystem);

    CommandScheduler.getInstance().schedule(hold);
    CommandScheduler.getInstance().run();

    assertFalse(hold.isScheduled());
    assertEquals(0, rig.subsystem.stopCount);
    assertEquals(0, rig.subsystem.acceptCount);
  }

  @Test
  void holdDoesNotMutateSensorsAndTeleopCanLaterSubmitAValidRequest() {
    Rig rig = new Rig();
    double initialFrontLeftPosition = rig.frontLeft.inputs.drivePositionRotations;
    Optional<Pose2d> initialPose = rig.subsystem.getCurrentPose();
    AutonomousSafetyHoldCommand hold = new AutonomousSafetyHoldCommand(rig.subsystem);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(hold);
    scheduler.run();
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    scheduler.run();

    assertEquals(initialFrontLeftPosition, rig.frontLeft.inputs.drivePositionRotations, kTolerance);
    assertEquals(initialPose, rig.subsystem.getCurrentPose());
    assertZeroFinalStates(rig.subsystem);

    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));
    assertEquals(1, rig.subsystem.acceptCount);
    assertTrue(rig.subsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  private static void assertZeroFinalStates(SwerveSubsystem subsystem) {
    for (var state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.0, state.angle.getRadians(), kTolerance);
    }
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO(12.0);
    private final RecordingModuleIO frontRight = new RecordingModuleIO(13.0);
    private final RecordingModuleIO backLeft = new RecordingModuleIO(14.0);
    private final RecordingModuleIO backRight = new RecordingModuleIO(15.0);
    private final RecordingSwerveSubsystem subsystem =
        new RecordingSwerveSubsystem(frontLeft, frontRight, backLeft, backRight);
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int acceptCount;
    private int stopCount;

    private RecordingSwerveSubsystem(
        SwerveModuleIO frontLeft,
        SwerveModuleIO frontRight,
        SwerveModuleIO backLeft,
        SwerveModuleIO backRight) {
      super(frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
    }

    @Override
    public void acceptChassisSpeeds(ChassisSpeeds chassisSpeeds) {
      acceptCount++;
      super.acceptChassisSpeeds(chassisSpeeds);
    }

    @Override
    public void stop() {
      stopCount++;
      super.stop();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final SwerveModuleIOInputs inputs = new SwerveModuleIOInputs();
    private int updateCount;

    private RecordingModuleIO(double drivePositionRotations) {
      inputs.drivePositionRotations = drivePositionRotations;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      updateCount++;
    }

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

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
