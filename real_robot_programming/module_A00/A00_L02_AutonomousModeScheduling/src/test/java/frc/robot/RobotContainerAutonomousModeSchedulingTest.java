// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import frc.robot.commands.AutonomousSafetyHoldCommand;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RobotContainerAutonomousModeSchedulingTest {
  private static final double kTolerance = 1.0e-9;

  private static RobotContainer robotContainer;
  private static Command autonomousCommand;
  private static SwerveSubsystem swerveSubsystem;

  @BeforeAll
  static void initializeHalAndCompositionRoot() {
    HAL.initialize(500, 0);
    robotContainer = new RobotContainer();
    autonomousCommand = robotContainer.getAutonomousCommand();
    swerveSubsystem =
        (SwerveSubsystem)
            autonomousCommand.getRequirements().stream().findFirst().orElseThrow();
  }

  @BeforeEach
  void enterAutonomousAndClearScheduler() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.cancelAll();
    setAutonomousMode();
    scheduler.run();
  }

  @AfterEach
  void clearSchedulerAndDisable() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.cancelAll();
    setDisabledMode();
    scheduler.run();
  }

  @AfterAll
  static void finalSchedulerCleanup() {
    CommandScheduler.getInstance().cancelAll();
    setDisabledMode();
    CommandScheduler.getInstance().run();
  }

  @Test
  void returnsRepeatedZeroMotionCompositionWithSwerveRequirement() {
    assertInstanceOf(RepeatCommand.class, autonomousCommand);
    assertSame(autonomousCommand, robotContainer.getAutonomousCommand());
    assertEquals(1, autonomousCommand.getRequirements().size());
    assertTrue(autonomousCommand.getRequirements().contains(swerveSubsystem));
  }

  @Test
  void autonomousSchedulingInterruptsDefaultAndLeavesZeroOutput() {
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(autonomousCommand);
    scheduler.run();

    assertTrue(autonomousCommand.isScheduled());
    assertFalse(swerveSubsystem.getDefaultCommand().isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);
  }

  @Test
  void repeatedHoldRetainsRequirementAfterUnderlyingIntervalExpires() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    MutableClock clock = new MutableClock(0.0);
    AutonomousSafetyHoldCommand hold =
        new AutonomousSafetyHoldCommand(
            subsystem,
            Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds,
            clock);
    Command repeated = hold.repeatedly();
    Command defaultCommand = Commands.run(() -> {}, subsystem);
    subsystem.setDefaultCommand(defaultCommand);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(repeated);
    scheduler.run();

    clock.set(Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds + 0.01);
    scheduler.run();
    scheduler.run();

    assertTrue(repeated.isScheduled());
    assertFalse(defaultCommand.isScheduled());
    assertEquals(0, subsystem.acceptCount);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void autonomousToTeleopCancelsHoldAndAllowsFreshRequestRecovery() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(autonomousCommand);
    scheduler.run();
    assertTrue(autonomousCommand.isScheduled());

    setTeleoperatedMode();
    autonomousCommand.cancel();
    scheduler.run();
    scheduler.run();

    assertFalse(autonomousCommand.isScheduled());
    assertTrue(swerveSubsystem.getDefaultCommand().isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);

    swerveSubsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));
    assertTrue(swerveSubsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  @Test
  void autonomousToDisabledCancelsAndLeavesNoStaleIntent() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(autonomousCommand);
    scheduler.run();
    swerveSubsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));

    setDisabledMode();
    scheduler.run();

    assertFalse(autonomousCommand.isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);
  }

  @Test
  void autonomousToTestCancelsSafetyCommandAndStops() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(autonomousCommand);
    scheduler.run();
    assertTrue(autonomousCommand.isScheduled());

    setTestMode();
    scheduler.cancelAll();
    scheduler.run();

    assertFalse(autonomousCommand.isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);
  }

  private static void assertZeroFinalModuleStates(SwerveSubsystem subsystem) {
    for (var state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.0, state.angle.getRadians(), kTolerance);
    }
  }

  private static void setAutonomousMode() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void setTeleoperatedMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void setTestMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
  }

  private static void setDisabledMode() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static final class MutableClock implements DoubleSupplier {
    private double value;

    private MutableClock(double value) {
      this.value = value;
    }

    @Override
    public double getAsDouble() {
      return value;
    }

    private void set(double value) {
      this.value = value;
    }
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int acceptCount;

    private RecordingSwerveSubsystem() {
      super(
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingGyroIO());
    }

    @Override
    public void acceptChassisSpeeds(ChassisSpeeds chassisSpeeds) {
      acceptCount++;
      super.acceptChassisSpeeds(chassisSpeeds);
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.driveConnected = true;
      inputs.driveConfigurationHealthy = true;
      inputs.steerConnected = true;
      inputs.steerConfigurationHealthy = true;
      inputs.encoderConnected = true;
      inputs.encoderConfigurationHealthy = true;
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
    public void updateInputs(GyroIOInputs inputs) {
      inputs.connected = true;
      inputs.configurationHealthy = true;
    }
  }
}
