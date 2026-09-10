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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveOutputPipeline;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveFourModuleTestCommandTest {
  private static final double kTolerance = 1.0e-9;
  private static final List<String> kDashboardKeys =
      List.of(
          "Four Module Forward",
          "Four Module Robot Left",
          "Four Module Rotate CCW",
          "Four Module Stop");

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableTestMode() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
  }

  @AfterEach
  void resetRuntimeState() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
    for (String key : kDashboardKeys) {
      NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry(key).unpublish();
    }
  }

  @Test
  void fixedMotionCommandsArmPipelineExactlyOnceWithoutDirectIO() {
    assertFixedCommand(
        SwerveFourModuleTestCommand::forward,
        new ChassisSpeeds(
            Constants.SwerveConstants.kFourModuleTestTranslationSpeedMetersPerSecond,
            0.0,
            0.0));
    assertFixedCommand(
        SwerveFourModuleTestCommand::robotLeft,
        new ChassisSpeeds(
            0.0,
            Constants.SwerveConstants.kFourModuleTestTranslationSpeedMetersPerSecond,
            0.0));
    assertFixedCommand(
        SwerveFourModuleTestCommand::rotateCcw,
        new ChassisSpeeds(
            0.0,
            0.0,
            Constants.SwerveConstants.kFourModuleTestRotationSpeedRadiansPerSecond));
  }

  @Test
  void disabledAndNonTestModesRejectMotionWithoutArmingIntent() {
    Rig disabledRig = new Rig();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
    SwerveFourModuleTestCommand disabledCommand =
        SwerveFourModuleTestCommand.forward(disabledRig.subsystem);
    disabledCommand.initialize();
    disabledCommand.end(false);
    assertEquals(0, disabledRig.subsystem.acceptCount);
    assertEquals(1, disabledRig.subsystem.stopCount);

    Rig teleopRig = new Rig();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    SwerveFourModuleTestCommand teleopCommand =
        SwerveFourModuleTestCommand.forward(teleopRig.subsystem);
    teleopCommand.initialize();
    teleopCommand.end(false);
    assertEquals(0, teleopRig.subsystem.acceptCount);
    assertEquals(1, teleopRig.subsystem.stopCount);
  }

  @Test
  void timeoutAndInterruptionStopAllModulesOnce() throws InterruptedException {
    Rig timeoutRig = new Rig();
    SwerveFourModuleTestCommand timeoutCommand =
        SwerveFourModuleTestCommand.forward(timeoutRig.subsystem);
    timeoutCommand.initialize();
    Thread.sleep(1050);
    timeoutCommand.execute();
    assertTrue(timeoutCommand.isFinished());
    timeoutCommand.end(false);
    assertEquals(1, timeoutRig.subsystem.stopCount);

    Rig interruptionRig = new Rig();
    SwerveFourModuleTestCommand interruptionCommand =
        SwerveFourModuleTestCommand.robotLeft(interruptionRig.subsystem);
    interruptionCommand.initialize();
    interruptionCommand.end(true);
    assertEquals(1, interruptionRig.subsystem.stopCount);
  }

  @Test
  void exceptionDuringAcceptStopsTheSubsystem() {
    Rig rig = new Rig();
    rig.subsystem.throwOnAccept = true;
    SwerveFourModuleTestCommand command =
        SwerveFourModuleTestCommand.forward(rig.subsystem);

    assertThrows(RuntimeException.class, command::initialize);
    assertEquals(1, rig.subsystem.stopCount);
  }

  @Test
  void commandRequirementsEnforceMutualExclusion() {
    Rig rig = new Rig();
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Command first = SwerveFourModuleTestCommand.forward(rig.subsystem);
    Command second = SwerveFourModuleTestCommand.robotLeft(rig.subsystem);

    scheduler.schedule(first);
    scheduler.run();
    scheduler.schedule(second);
    scheduler.run();

    assertFalse(first.isScheduled());
    assertTrue(second.isScheduled());
    scheduler.cancelAll();
    scheduler.run();
  }

  @Test
  void stopCommandCallsSubsystemStopOnce() {
    Rig rig = new Rig();
    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.30, 0.0, 0.0));
    rig.subsystem.periodic();
    int driveCountBeforeStop = rig.frontLeft.driveVelocityCount;
    SwerveFourModuleTestCommand stopCommand =
        SwerveFourModuleTestCommand.stop(rig.subsystem);

    stopCommand.initialize();
    assertTrue(stopCommand.isFinished());
    stopCommand.end(false);

    assertEquals(1, rig.subsystem.stopCount);
    rig.subsystem.periodic();
    assertEquals(driveCountBeforeStop, rig.frontLeft.driveVelocityCount);
    assertEquals(1, rig.frontRight.driveVelocityCount);
    assertEquals(1, rig.backLeft.driveVelocityCount);
    assertEquals(1, rig.backRight.driveVelocityCount);
  }

  @Test
  void dashboardPublishesExactlyFourFixedCommands() {
    Rig rig = new Rig();
    new SwerveFourModuleTestDashboard(rig.subsystem);

    for (String key : kDashboardKeys) {
      assertInstanceOf(Command.class, SmartDashboard.getData(key));
    }
  }

  private static void assertFixedCommand(
      CommandFactory factory, ChassisSpeeds expectedChassisSpeeds) {
    Rig rig = new Rig();
    Command command = factory.create(rig.subsystem);

    command.initialize();
    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(0, rig.frontLeft.driveVelocityCount);
    assertEquals(0, rig.frontLeft.steerAngleCount);
    assertEquals(0, rig.frontRight.driveVelocityCount);
    assertEquals(0, rig.frontRight.steerAngleCount);
    assertEquals(0, rig.backLeft.driveVelocityCount);
    assertEquals(0, rig.backLeft.steerAngleCount);
    assertEquals(0, rig.backRight.driveVelocityCount);
    assertEquals(0, rig.backRight.steerAngleCount);

    rig.subsystem.periodic();
    SwerveModuleState[] expectedStates =
        new SwerveOutputPipeline()
            .toModuleStates(
                expectedChassisSpeeds,
                new Rotation2d[] {
                  new Rotation2d(), new Rotation2d(), new Rotation2d(), new Rotation2d()
                });
    SwerveModuleState[] actualStates = rig.subsystem.getFinalModuleStates();
    for (int index = 0; index < actualStates.length; index++) {
      assertEquals(expectedStates[index].speedMetersPerSecond, actualStates[index].speedMetersPerSecond,
          kTolerance);
      assertEquals(expectedStates[index].angle.getRadians(), actualStates[index].angle.getRadians(),
          kTolerance);
    }
    assertEquals(1, rig.frontLeft.driveVelocityCount);
    assertEquals(1, rig.frontLeft.steerAngleCount);
    assertEquals(1, rig.frontRight.driveVelocityCount);
    assertEquals(1, rig.frontRight.steerAngleCount);
    assertEquals(1, rig.backLeft.driveVelocityCount);
    assertEquals(1, rig.backLeft.steerAngleCount);
    assertEquals(1, rig.backRight.driveVelocityCount);
    assertEquals(1, rig.backRight.steerAngleCount);
    command.end(false);
  }

  @FunctionalInterface
  private interface CommandFactory {
    Command create(SwerveSubsystem subsystem);
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final RecordingSwerveSubsystem subsystem =
        new RecordingSwerveSubsystem(frontLeft, frontRight, backLeft, backRight);
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int acceptCount;
    private int stopCount;
    private boolean throwOnAccept;

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
      if (throwOnAccept) {
        throw new RuntimeException("simulated chassis-speed acceptance failure");
      }
      super.acceptChassisSpeeds(chassisSpeeds);
    }

    @Override
    public void stop() {
      stopCount++;
      super.stop();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private int driveVelocityCount;
    private int steerAngleCount;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveVelocityCount++;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerAngleCount++;
    }

    @Override
    public void stop() {}
  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
