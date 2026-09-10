// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
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

class SwerveFrontLeftClosedLoopCommissioningCommandTest {
  @BeforeAll
  static void initializeHal() {
    assertTrue(HAL.initialize(500, 0), "HAL initialization failed");
  }

  @BeforeEach
  void resetDriverStationState() {
    DriverStationSim.resetData();
    setDisabledMode();
  }

  @AfterEach
  void resetRuntimeState() {
    setDisabledMode();
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
  }

  @Test
  void testModeAcceptsAllFourFixedFactories() {
    setTestMode();
    Rig rig = new Rig(0.98);

    Command[] commands = {
      SwerveFrontLeftClosedLoopCommissioningCommand.drivePositive(rig.subsystem),
      SwerveFrontLeftClosedLoopCommissioningCommand.driveNegative(rig.subsystem),
      SwerveFrontLeftClosedLoopCommissioningCommand.steerPositive(rig.subsystem),
      SwerveFrontLeftClosedLoopCommissioningCommand.steerNegative(rig.subsystem)
    };

    double[] expectedDriveVelocities = {0.30, -0.30, 0.0, 0.0};
    double[] expectedSteerTargets = {0.0, 0.0, 0.0425, -0.0825};
    for (int index = 0; index < commands.length; index++) {
      commands[index].initialize();
      assertEquals(expectedDriveVelocities[index], rig.frontLeft.lastDriveVelocity, 1.0e-12);
      assertEquals(expectedSteerTargets[index], rig.frontLeft.lastSteerAngle.getRotations(), 1.0e-12);
      commands[index].end(true);
      rig.frontLeft.clearClosedLoopHistory();
    }
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void nonTestModesRejectClosedLoopCommandsAndStopFrontLeft() {
    for (Runnable modeSetter : new Runnable[] {
      SwerveFrontLeftClosedLoopCommissioningCommandTest::setDisabledMode,
      SwerveFrontLeftClosedLoopCommissioningCommandTest::setTeleopMode,
      SwerveFrontLeftClosedLoopCommissioningCommandTest::setAutonomousMode
    }) {
      modeSetter.run();
      Rig rig = new Rig(0.0);
      Command command = SwerveFrontLeftClosedLoopCommissioningCommand.drivePositive(rig.subsystem);

      command.initialize();
      command.execute();
      command.end(false);

      assertEquals(0, rig.frontLeft.closedLoopRequestCount);
      assertEquals(1, rig.frontLeft.stopCount);
      assertNoOtherModuleActuation(rig);
    }
  }

  @Test
  void cancellationAndInterruptionStopFrontLeftWithoutActuatingOtherModules() {
    setTestMode();
    Rig rig = new Rig(0.0);
    Command first = SwerveFrontLeftClosedLoopCommissioningCommand.drivePositive(rig.subsystem);
    Command second = SwerveFrontLeftClosedLoopCommissioningCommand.steerPositive(rig.subsystem);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(first);
    scheduler.run();
    scheduler.schedule(second);
    scheduler.run();
    scheduler.cancel(second);
    scheduler.run();

    assertTrue(rig.frontLeft.stopCount >= 2);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void timeoutStopsFrontLeftAfterOneSecond() throws InterruptedException {
    setTestMode();
    Rig rig = new Rig(0.0);
    Command command = SwerveFrontLeftClosedLoopCommissioningCommand.drivePositive(rig.subsystem);

    command.initialize();
    Thread.sleep(1100);
    command.execute();

    assertEquals(1, rig.frontLeft.stopCount);
    assertTrue(command.isFinished());
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void outputExceptionStopsFrontLeft() {
    setTestMode();
    Rig rig = new Rig(0.0);
    rig.frontLeft.throwOnDriveVelocity = true;
    Command command = SwerveFrontLeftClosedLoopCommissioningCommand.drivePositive(rig.subsystem);

    assertThrows(RuntimeException.class, command::initialize);

    assertEquals(1, rig.frontLeft.stopCount);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void usesApprovedTimeoutAndFixedSetpoints() {
    assertEquals(1.0, Constants.SwerveConstants.kFrontLeftClosedLoopCommissioningTimeoutSeconds);
    assertEquals(0.50, Constants.SwerveConstants.kFrontLeftMaximumDriveVelocityMetersPerSecond);
    assertEquals(0.30, Constants.SwerveConstants.kFrontLeftPositiveDriveTestVelocityMetersPerSecond);
    assertEquals(-0.30, Constants.SwerveConstants.kFrontLeftNegativeDriveTestVelocityMetersPerSecond);
    assertEquals(0.125, Constants.SwerveConstants.kFrontLeftMaximumSteerStepRotations);
    assertEquals(0.0625, Constants.SwerveConstants.kFrontLeftPositiveSteerTestStepRotations);
    assertEquals(-0.0625, Constants.SwerveConstants.kFrontLeftNegativeSteerTestStepRotations);
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

  private static void setTeleopMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void setAutonomousMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void assertNoOtherModuleActuation(Rig rig) {
    assertEquals(0, rig.frontRight.closedLoopRequestCount);
    assertEquals(0, rig.frontRight.stopCount);
    assertEquals(0, rig.backLeft.closedLoopRequestCount);
    assertEquals(0, rig.backLeft.stopCount);
    assertEquals(0, rig.backRight.closedLoopRequestCount);
    assertEquals(0, rig.backRight.stopCount);
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft;
    private final RecordingModuleIO frontRight = new RecordingModuleIO(0.0);
    private final RecordingModuleIO backLeft = new RecordingModuleIO(0.0);
    private final RecordingModuleIO backRight = new RecordingModuleIO(0.0);
    private final SwerveSubsystem subsystem;

    private Rig(double frontLeftAngleRotations) {
      frontLeft = new RecordingModuleIO(frontLeftAngleRotations);
      subsystem =
          new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final double encoderAngleRotations;
    private int closedLoopRequestCount;
    private int stopCount;
    private double lastDriveVelocity;
    private Rotation2d lastSteerAngle = new Rotation2d();
    private boolean throwOnDriveVelocity;

    private RecordingModuleIO(double encoderAngleRotations) {
      this.encoderAngleRotations = encoderAngleRotations;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.encoderAbsolutePositionRotations = encoderAngleRotations;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      if (throwOnDriveVelocity) {
        throw new RuntimeException("simulated Front Left drive velocity failure");
      }
      closedLoopRequestCount++;
      lastDriveVelocity = velocityMetersPerSecond;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      closedLoopRequestCount++;
      lastSteerAngle = angle;
    }

    @Override
    public void stop() {
      stopCount++;
      lastDriveVelocity = 0.0;
      lastSteerAngle = new Rotation2d();
    }

    private void clearClosedLoopHistory() {
      closedLoopRequestCount = 0;
      lastDriveVelocity = 0.0;
      lastSteerAngle = new Rotation2d();
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
