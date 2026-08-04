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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveFrontLeftOpenLoopCommissioningCommandTest {
  private static final String[] DASHBOARD_KEYS = {
    "FL Drive Positive", "FL Drive Negative", "FL Steer Positive", "FL Steer Negative"
  };

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
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
    setDisabledMode();
    for (String dashboardKey : DASHBOARD_KEYS) {
      NetworkTableInstance.getDefault()
          .getTable("SmartDashboard")
          .getEntry(dashboardKey)
          .unpublish();
    }
  }

  @Test
  void publishesExactlyFourCommandIdentities() {
    Rig rig = new Rig();

    new SwerveFrontLeftCommissioningDashboard(rig.subsystem);

    for (String dashboardKey : DASHBOARD_KEYS) {
      assertInstanceOf(Command.class, SmartDashboard.getData(dashboardKey));
    }
  }

  @Test
  void testModeAcceptsAllFourFixedFactories() {
    setTestMode();
    Rig rig = new Rig();

    Command[] commands = {
      SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(rig.subsystem),
      SwerveFrontLeftOpenLoopCommissioningCommand.driveNegative(rig.subsystem),
      SwerveFrontLeftOpenLoopCommissioningCommand.steerPositive(rig.subsystem),
      SwerveFrontLeftOpenLoopCommissioningCommand.steerNegative(rig.subsystem)
    };

    double[] expectedDriveOutputs = {0.05, -0.05, 0.0, 0.0};
    double[] expectedSteerOutputs = {0.0, 0.0, 0.05, -0.05};
    for (int commandIndex = 0; commandIndex < commands.length; commandIndex++) {
      Command command = commands[commandIndex];
      command.initialize();
      assertEquals(expectedDriveOutputs[commandIndex], rig.frontLeft.lastDriveOutput);
      assertEquals(expectedSteerOutputs[commandIndex], rig.frontLeft.lastSteerOutput);
      command.end(true);
      rig.frontLeft.clearOutputHistory();
    }
  }

  @Test
  void disabledTeleopAndAutonomousRejectCommandsWithZeroOutput() {
    for (Runnable modeSetter : new Runnable[] {
      SwerveFrontLeftOpenLoopCommissioningCommandTest::setDisabledMode,
      SwerveFrontLeftOpenLoopCommissioningCommandTest::setTeleopMode,
      SwerveFrontLeftOpenLoopCommissioningCommandTest::setAutonomousMode
    }) {
      modeSetter.run();
      Rig rig = new Rig();
      SwerveFrontLeftOpenLoopCommissioningCommand command =
          SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(rig.subsystem);

      command.initialize();
      command.execute();
      command.end(false);

      assertTrue(rig.frontLeft.driveOutputs.stream().noneMatch(output -> output != 0.0));
      assertTrue(rig.frontLeft.steerOutputs.stream().noneMatch(output -> output != 0.0));
      assertNoOtherModuleActuation(rig);
    }
  }

  @Test
  void constructionIsPrivateAndFactoriesUseFixedBounds() throws ReflectiveOperationException {
    Constructor<?>[] constructors =
        SwerveFrontLeftOpenLoopCommissioningCommand.class.getDeclaredConstructors();

    assertEquals(1, constructors.length);
    assertTrue(Modifier.isPrivate(constructors[0].getModifiers()));
    assertEquals(0.05, Constants.SwerveConstants.kFrontLeftDriveCommissioningDutyCycle);
    assertEquals(0.05, Constants.SwerveConstants.kFrontLeftSteerCommissioningDutyCycle);
    assertEquals(0.25, Constants.SwerveConstants.kFrontLeftCommissioningPulseDurationSeconds);
  }

  @Test
  void subsystemClampsEveryCommissioningOutputToSafetyLimit() {
    setTestMode();
    Rig rig = new Rig();

    for (SwerveSubsystem.FrontLeftCommissioningAction action :
        SwerveSubsystem.FrontLeftCommissioningAction.values()) {
      assertTrue(rig.subsystem.startFrontLeftCommissioning(action));
      assertTrue(Math.abs(rig.frontLeft.lastDriveOutput) <= 0.05);
      assertTrue(Math.abs(rig.frontLeft.lastSteerOutput) <= 0.05);
      rig.subsystem.stopFrontLeftCommissioning();
    }
  }

  @Test
  void subsystemWatchdogStopsFrontLeftAfterFixedDuration() throws InterruptedException {
    setTestMode();
    Rig rig = new Rig();

    assertTrue(
        rig.subsystem.startFrontLeftCommissioning(
            SwerveSubsystem.FrontLeftCommissioningAction.DRIVE_POSITIVE));
    Thread.sleep(300);
    rig.subsystem.periodic();

    assertEquals(1, rig.frontLeft.stopCount);
    assertEquals(0.0, rig.frontLeft.lastDriveOutput);
    assertEquals(0.0, rig.frontLeft.lastSteerOutput);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void driveAndSteerMutualExclusionIsMaintainedBySubsystem() {
    setTestMode();
    Rig rig = new Rig();

    assertTrue(
        rig.subsystem.startFrontLeftCommissioning(
            SwerveSubsystem.FrontLeftCommissioningAction.DRIVE_POSITIVE));
    assertEquals(0.0, rig.frontLeft.lastSteerOutput);
    assertFalse(
        rig.subsystem.startFrontLeftCommissioning(
            SwerveSubsystem.FrontLeftCommissioningAction.STEER_POSITIVE));
    assertEquals(1, rig.frontLeft.stopCount);

    assertTrue(
        rig.subsystem.startFrontLeftCommissioning(
            SwerveSubsystem.FrontLeftCommissioningAction.STEER_NEGATIVE));
    assertEquals(0.0, rig.frontLeft.lastDriveOutput);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void schedulerCancellationStopsFrontLeft() {
    setTestMode();
    Rig rig = new Rig();
    Command command = SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(rig.subsystem);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(command);
    scheduler.run();
    scheduler.cancel(command);
    scheduler.run();

    assertEquals(1, rig.frontLeft.stopCount);
    assertEquals(0.0, rig.frontLeft.lastDriveOutput);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void schedulerInterruptionStopsPreviousFrontLeftCommand() {
    setTestMode();
    Rig rig = new Rig();
    Command first = SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(rig.subsystem);
    Command second = SwerveFrontLeftOpenLoopCommissioningCommand.steerPositive(rig.subsystem);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(first);
    scheduler.run();
    scheduler.schedule(second);
    scheduler.run();

    assertEquals(1, rig.frontLeft.stopCount);
    assertEquals(0.05, rig.frontLeft.lastSteerOutput);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void modeExitStopsActiveCommand() {
    setTestMode();
    Rig rig = new Rig();
    SwerveFrontLeftOpenLoopCommissioningCommand command =
        SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(rig.subsystem);
    command.initialize();

    setTeleopMode();
    command.execute();

    assertEquals(1, rig.frontLeft.stopCount);
    assertEquals(0.0, rig.frontLeft.lastDriveOutput);
    assertEquals(0.0, rig.frontLeft.lastSteerOutput);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void subsystemModeExitStopsWithoutCommandLifecycle() {
    setTestMode();
    Rig rig = new Rig();
    assertTrue(
        rig.subsystem.startFrontLeftCommissioning(
            SwerveSubsystem.FrontLeftCommissioningAction.STEER_POSITIVE));

    setAutonomousMode();
    rig.subsystem.periodic();

    assertEquals(1, rig.frontLeft.stopCount);
    assertEquals(0.0, rig.frontLeft.lastDriveOutput);
    assertEquals(0.0, rig.frontLeft.lastSteerOutput);
    assertNoOtherModuleActuation(rig);
  }

  @Test
  void outputFailureStopsFrontLeft() {
    setTestMode();
    Rig rig = new Rig();
    rig.frontLeft.throwOnNonZeroDriveOutput = true;
    SwerveFrontLeftOpenLoopCommissioningCommand command =
        SwerveFrontLeftOpenLoopCommissioningCommand.drivePositive(rig.subsystem);

    assertThrows(RuntimeException.class, command::initialize);

    assertEquals(1, rig.frontLeft.stopCount);
    assertNoOtherModuleActuation(rig);
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
    assertEquals(0, rig.frontRight.driveOutputs.size());
    assertEquals(0, rig.frontRight.steerOutputs.size());
    assertEquals(0, rig.frontRight.stopCount);
    assertEquals(0, rig.backLeft.driveOutputs.size());
    assertEquals(0, rig.backLeft.steerOutputs.size());
    assertEquals(0, rig.backLeft.stopCount);
    assertEquals(0, rig.backRight.driveOutputs.size());
    assertEquals(0, rig.backRight.steerOutputs.size());
    assertEquals(0, rig.backRight.stopCount);
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new RecordingGyroIO());
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final java.util.List<Double> driveOutputs = new java.util.ArrayList<>();
    private final java.util.List<Double> steerOutputs = new java.util.ArrayList<>();
    private double lastDriveOutput;
    private double lastSteerOutput;
    private int stopCount;
    private boolean throwOnNonZeroDriveOutput;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {
      driveOutputs.add(output);
      if (throwOnNonZeroDriveOutput && output != 0.0) {
        throw new RuntimeException("simulated Front Left drive output failure");
      }
      lastDriveOutput = output;
    }

    @Override
    public void setSteerOutput(double output) {
      steerOutputs.add(output);
      lastSteerOutput = output;
    }

    @Override
    public void stop() {
      stopCount++;
      lastDriveOutput = 0.0;
      lastSteerOutput = 0.0;
    }

    private void clearOutputHistory() {
      driveOutputs.clear();
      steerOutputs.clear();
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
