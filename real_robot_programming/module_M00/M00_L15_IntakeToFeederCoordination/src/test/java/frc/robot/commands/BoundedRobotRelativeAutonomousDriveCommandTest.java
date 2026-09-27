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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoundedRobotRelativeAutonomousDriveCommandTest {
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
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void requiresSwerveSubsystemAndDoesNotRunWhenDisabled() {
    Rig rig = new Rig();
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig);

    assertTrue(command.getRequirements().contains(rig.subsystem));
    assertFalse(command.runsWhenDisabled());
  }

  @Test
  void initializeStopsThenSubmitsOneDefensiveRobotRelativeRequest() {
    Rig rig = new Rig();
    ChassisSpeeds request = new ChassisSpeeds(0.30, 0.0, 0.0);
    MutableClock clock = new MutableClock(2.0);
    BoundedRobotRelativeAutonomousDriveCommand command =
        new BoundedRobotRelativeAutonomousDriveCommand(
            rig.subsystem, request, kDurationSeconds, clock);

    command.initialize();
    request.vxMetersPerSecond = 9.0;
    command.execute();

    assertEquals(1, rig.subsystem.stopCount);
    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(0.30, rig.subsystem.lastAccepted.vxMetersPerSecond, kTolerance);
    assertEquals(0.0, rig.subsystem.lastAccepted.vyMetersPerSecond, kTolerance);
    assertEquals(0.0, rig.subsystem.lastAccepted.omegaRadiansPerSecond, kTolerance);
    assertEquals(0.30, rig.subsystem.getFinalModuleStates()[0].speedMetersPerSecond, kTolerance);
  }

  @Test
  void boundedCompletionStopsNormally() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(10.0);
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig, clock);

    command.initialize();
    clock.set(11.0);

    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void interruptionStopsAndLaterValidRequestRecovers() {
    Rig rig = new Rig();
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig);

    command.initialize();
    command.end(true);

    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);

    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.20, 0.0, 0.0));
    assertTrue(rig.subsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  @Test
  void schedulerCancellationStopsCommand() {
    Rig rig = new Rig();
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig);
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(command);
    scheduler.run();
    scheduler.cancel(command);

    assertFalse(command.isScheduled());
    assertEquals(2, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void invalidConfigurationIsRejected() {
    Rig rig = new Rig();

    assertThrows(
        NullPointerException.class,
        () -> new BoundedRobotRelativeAutonomousDriveCommand(rig.subsystem, null, 1.0, () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new BoundedRobotRelativeAutonomousDriveCommand(
                rig.subsystem,
                new ChassisSpeeds(Double.NaN, 0.0, 0.0),
                1.0,
                () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new BoundedRobotRelativeAutonomousDriveCommand(
                rig.subsystem,
                new ChassisSpeeds(0.0, Double.POSITIVE_INFINITY, 0.0),
                1.0,
                () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new BoundedRobotRelativeAutonomousDriveCommand(
                rig.subsystem,
                new ChassisSpeeds(0.0, 0.0, Double.NEGATIVE_INFINITY),
                1.0,
                () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new BoundedRobotRelativeAutonomousDriveCommand(
                rig.subsystem, new ChassisSpeeds(), 0.0, () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new BoundedRobotRelativeAutonomousDriveCommand(
                rig.subsystem, new ChassisSpeeds(), Double.POSITIVE_INFINITY, () -> 0.0));
  }

  @Test
  void invalidStartClockFailsClosedWithoutRequest() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(Double.NaN);
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig, clock);

    command.initialize();

    assertTrue(command.isFinished());
    assertEquals(0, rig.subsystem.acceptCount);
    assertEquals(1, rig.subsystem.stopCount);
    command.end(false);
    assertEquals(2, rig.subsystem.stopCount);
  }

  @Test
  void nonfiniteBackwardAndThrowingClockValuesFailClosed() {
    assertInvalidClockValue(Double.POSITIVE_INFINITY);
    assertInvalidClockValue(Double.NEGATIVE_INFINITY);
    assertBackwardClockValue();
    assertThrowingClockValue();
  }

  @Test
  void disabledSchedulingDoesNotInitializeOrRequestMotion() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    Rig rig = new Rig();
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig);

    CommandScheduler.getInstance().schedule(command);
    CommandScheduler.getInstance().run();

    assertFalse(command.isScheduled());
    assertEquals(0, rig.subsystem.acceptCount);
    assertEquals(0, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  @Test
  void commandDoesNotDependOnOrMutatePoseOrSensors() {
    Rig rig = new Rig();
    Optional<Pose2d> initialPose = rig.subsystem.getCurrentPose();
    double initialPosition = rig.frontLeft.inputs.drivePositionRotations;
    MutableClock clock = new MutableClock(4.0);
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig, clock);

    command.initialize();
    clock.set(5.0);
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(initialPose, rig.subsystem.getCurrentPose());
    assertEquals(initialPosition, rig.frontLeft.inputs.drivePositionRotations, kTolerance);
    assertEquals(0, rig.frontLeft.updateCount);
    assertZeroFinalStates(rig.subsystem);
  }

  private static void assertInvalidClockValue(double invalidValue) {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(2.0);
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig, clock);

    command.initialize();
    clock.set(invalidValue);
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(3, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  private static void assertBackwardClockValue() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(2.0);
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig, clock);

    command.initialize();
    clock.set(1.0);
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(3, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  private static void assertThrowingClockValue() {
    Rig rig = new Rig();
    MutableClock clock = new MutableClock(2.0);
    BoundedRobotRelativeAutonomousDriveCommand command = createCommand(rig, clock);

    command.initialize();
    clock.throwOnRead = true;
    assertTrue(command.isFinished());
    command.end(false);

    assertEquals(3, rig.subsystem.stopCount);
    assertZeroFinalStates(rig.subsystem);
  }

  private static BoundedRobotRelativeAutonomousDriveCommand createCommand(Rig rig) {
    return createCommand(rig, new MutableClock(0.0));
  }

  private static BoundedRobotRelativeAutonomousDriveCommand createCommand(
      Rig rig, MutableClock clock) {
    return new BoundedRobotRelativeAutonomousDriveCommand(
        rig.subsystem, new ChassisSpeeds(0.30, 0.0, 0.0), kDurationSeconds, clock);
  }

  private static void assertZeroFinalStates(SwerveSubsystem subsystem) {
    for (var state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.0, state.angle.getRadians(), kTolerance);
    }
  }

  private static final class MutableClock implements DoubleSupplier {
    private double value;
    private boolean throwOnRead;

    private MutableClock(double value) {
      this.value = value;
    }

    @Override
    public double getAsDouble() {
      if (throwOnRead) {
        throw new IllegalStateException("clock failure");
      }
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
    private ChassisSpeeds lastAccepted = new ChassisSpeeds();

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
      lastAccepted =
          new ChassisSpeeds(
              chassisSpeeds.vxMetersPerSecond,
              chassisSpeeds.vyMetersPerSecond,
              chassisSpeeds.omegaRadiansPerSecond);
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
