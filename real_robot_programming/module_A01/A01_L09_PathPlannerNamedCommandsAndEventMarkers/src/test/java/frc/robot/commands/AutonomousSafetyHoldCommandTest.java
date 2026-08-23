// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutonomousSafetyHoldCommandTest {
  private static final double kDurationSeconds = 1.0;
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
  void requiresSwerveSubsystemAndDoesNotRunWhenDisabled() {
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand command = createCommand(rig);

    assertTrue(command.getRequirements().contains(rig.subsystem));
    assertFalse(command.runsWhenDisabled());
  }

  @Test
  void initializeStopsDrivetrainAndExecuteDoesNotRequestChassisSpeeds() {
    Rig rig = new Rig();
    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.5, 0.0, 0.0));
    AutonomousSafetyHoldCommand command = createCommand(rig);

    command.initialize();
    command.execute();

    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(1, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
    assertEquals(0, rig.frontLeft.updateCount);
    assertEquals(0, rig.frontRight.updateCount);
    assertEquals(0, rig.backLeft.updateCount);
    assertEquals(0, rig.backRight.updateCount);
  }

  @Test
  void normalBoundedCompletionCallsEndFalseAndStops() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(10.0);
    AutonomousSafetyHoldCommand command = createCommand(rig, clock);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(command);
    scheduler.run();
    assertTrue(command.isScheduled());

    clock.set(11.0);
    scheduler.run();

    assertFalse(command.isScheduled());
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void cancellationCallsEndTrueAndStops() {
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand command = createCommand(rig);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(command);
    scheduler.run();
    scheduler.cancel(command);

    assertFalse(command.isScheduled());
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void schedulerInterruptionCallsEndTrueAndStops() {
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand command = createCommand(rig);
    Command replacement = Commands.run(() -> {}, rig.subsystem);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(command);
    scheduler.run();
    scheduler.schedule(replacement);
    scheduler.run();

    assertFalse(command.isScheduled());
    assertTrue(replacement.isScheduled());
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void disabledSchedulingDoesNotInitializeOrExecute() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    Rig rig = new Rig();
    AutonomousSafetyHoldCommand command = createCommand(rig);

    CommandScheduler.getInstance().schedule(command);
    CommandScheduler.getInstance().run();

    assertFalse(command.isScheduled());
    assertEquals(0, rig.subsystem.stopCount);
    assertEquals(0, rig.subsystem.acceptCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void invalidStartClockFailsClosed() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(Double.NaN);
    AutonomousSafetyHoldCommand command = createCommand(rig, clock);

    command.initialize();
    command.execute();

    assertTrue(command.isFinished());
    command.end(false);
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void nonfiniteAndBackwardClockValuesFailClosed() {
    assertInvalidClockValue(Double.POSITIVE_INFINITY);
    assertInvalidClockValue(Double.NEGATIVE_INFINITY);
    assertBackwardClockValue();
  }

  @Test
  void invalidDurationIsRejected() {
    Rig rig = new Rig();

    assertThrows(
        IllegalArgumentException.class,
        () -> new AutonomousSafetyHoldCommand(rig.subsystem, 0.0, () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new AutonomousSafetyHoldCommand(rig.subsystem, Double.NaN, () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> new AutonomousSafetyHoldCommand(rig.subsystem, Double.POSITIVE_INFINITY, () -> 0.0));
  }

  @Test
  void commandDoesNotMutateSensorStateAndLaterValidRequestRecovers() {
    Rig rig = new Rig();
    double initialFrontLeftPosition = rig.frontLeft.inputs.drivePositionRotations;
    Optional<Pose2d> initialPose = rig.subsystem.getCurrentPose();
    MutableClock clock = new MutableClock(4.0);
    AutonomousSafetyHoldCommand command = createCommand(rig, clock);

    command.initialize();
    command.execute();
    clock.set(5.0);
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(initialFrontLeftPosition, rig.frontLeft.inputs.drivePositionRotations, kTolerance);
    assertEquals(initialPose, rig.subsystem.getCurrentPose());
    assertEquals(0, rig.frontLeft.updateCount);
    assertZeroFinalStates(rig.subsystem);

    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));
    assertEquals(1, rig.subsystem.acceptCount);
    assertTrue(rig.subsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  private static void assertInvalidClockValue(double invalidValue) {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(2.0);
    AutonomousSafetyHoldCommand command = createCommand(rig, clock);

    command.initialize();
    clock.set(invalidValue);
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(3, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  private static void assertBackwardClockValue() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(2.0);
    AutonomousSafetyHoldCommand command = createCommand(rig, clock);

    command.initialize();
    clock.set(1.0);
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(3, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  private static AutonomousSafetyHoldCommand createCommand(Rig rig) {
    return createCommand(rig, new MutableClock(0.0));
  }

  private static AutonomousSafetyHoldCommand createCommand(Rig rig, MutableClock clock) {
    return new AutonomousSafetyHoldCommand(rig.subsystem, kDurationSeconds, clock);
  }

  private static void assertZeroFinalStates(SwerveSubsystem subsystem) {
    for (var state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.0, state.angle.getRadians(), kTolerance);
    }
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
