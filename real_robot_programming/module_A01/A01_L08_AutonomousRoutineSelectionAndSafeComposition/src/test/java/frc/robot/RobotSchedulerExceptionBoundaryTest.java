// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutonomousPreparationCoordinator;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.subsystems.SwerveSubsystem;
import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RobotSchedulerExceptionBoundaryTest {
  private static final double TEST_DRIVE_SPEED_METERS_PER_SECOND = 1.0;

  private CommandScheduler scheduler;
  private Robot robot;
  private AutonomousPreparationCoordinator coordinator;
  private SwerveSubsystem swerveSubsystem;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void createRobotInAutonomous() throws ReflectiveOperationException {
    AutoBuilder.resetForTesting();
    scheduler = CommandScheduler.getInstance();
    recoverSchedulerUsingPublicLifecycle();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();

    robot = new Robot();
    RobotContainer container = readRobotContainer(robot);
    coordinator = readCoordinator(container);
    swerveSubsystem = readSwerveSubsystem(container);
  }

  @AfterEach
  void cleanScheduler() {
    recoverSchedulerUsingPublicLifecycle();
    AutoBuilder.resetForTesting();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
  }

  private void recoverSchedulerUsingPublicLifecycle() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.cancelAll();
  }

  @ParameterizedTest
  @EnumSource(FailurePoint.class)
  void schedulerLifecycleFailureFailsClosedAtRobotBoundary(FailurePoint failurePoint) {
    FailingCommand failingCommand = new FailingCommand(failurePoint);
    swerveSubsystem.acceptChassisSpeeds(
        new ChassisSpeeds(TEST_DRIVE_SPEED_METERS_PER_SECOND, 0.0, 0.0));
    assertTrue(hasNonzeroFinalModuleSpeed());

    if (failurePoint == FailurePoint.INITIALIZE) {
      scheduler.schedule(new ScheduleDuringExecuteCommand(scheduler, failingCommand));
    } else {
      scheduler.schedule(failingCommand);
    }

    assertDoesNotThrow(() -> robot.robotPeriodic());

    assertSwerveStopped();
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertTrue(coordinator.getObservation().adapterFatalFaulted());
    assertTrue(
        coordinator.getObservation().firstFatalReason().contains("scheduler boundary failure"));
    assertTrue(
        coordinator.getObservation().firstFatalReason().contains(failurePoint.name()));

    if (failurePoint == FailurePoint.INITIALIZE) {
      String firstFatalReason = coordinator.getObservation().firstFatalReason();
      assertDoesNotThrow(() -> robot.autonomousInit());
      assertDoesNotThrow(() -> robot.robotPeriodic());
      assertEquals(State.FAULTED, coordinator.getObservation().state());
      assertEquals(firstFatalReason, coordinator.getObservation().firstFatalReason());
    }
  }

  @Test
  void directScheduleInitializeFailurePropagatesBeforeRobotPeriodic() {
    IllegalStateException failure =
        assertThrows(
            IllegalStateException.class,
            () -> scheduler.schedule(new FailingCommand(FailurePoint.INITIALIZE)));

    assertEquals("scheduler-INITIALIZE", failure.getMessage());
    assertEquals(State.UNPREPARED, coordinator.getObservation().state());
  }

  @Test
  void fatalSchedulerStateRejectsASecondAutonomousRestart() {
    scheduler.schedule(new FailingCommand(FailurePoint.EXECUTE));
    assertDoesNotThrow(() -> robot.robotPeriodic());
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    String firstFatalReason = coordinator.getObservation().firstFatalReason();

    assertDoesNotThrow(() -> robot.autonomousInit());
    assertDoesNotThrow(() -> robot.robotPeriodic());
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertEquals(firstFatalReason, coordinator.getObservation().firstFatalReason());
  }

  private static RobotContainer readRobotContainer(Robot robot)
      throws ReflectiveOperationException {
    Field field = Robot.class.getDeclaredField("m_robotContainer");
    field.setAccessible(true);
    return (RobotContainer) field.get(robot);
  }

  private static AutonomousPreparationCoordinator readCoordinator(RobotContainer container)
      throws ReflectiveOperationException {
    Field field = RobotContainer.class.getDeclaredField("autonomousPreparationCoordinator");
    field.setAccessible(true);
    return (AutonomousPreparationCoordinator) field.get(container);
  }

  private static SwerveSubsystem readSwerveSubsystem(RobotContainer container)
      throws ReflectiveOperationException {
    Field field = RobotContainer.class.getDeclaredField("swerveSubsystem");
    field.setAccessible(true);
    return (SwerveSubsystem) field.get(container);
  }

  private boolean hasNonzeroFinalModuleSpeed() {
    for (var moduleState : swerveSubsystem.getFinalModuleStates()) {
      if (moduleState.speedMetersPerSecond != 0.0) {
        return true;
      }
    }
    return false;
  }

  private void assertSwerveStopped() {
    for (var moduleState : swerveSubsystem.getFinalModuleStates()) {
      assertEquals(0.0, moduleState.speedMetersPerSecond);
    }
  }

  private enum FailurePoint {
    INITIALIZE,
    EXECUTE,
    IS_FINISHED,
    END
  }

  private static final class ScheduleDuringExecuteCommand extends Command {
    private final CommandScheduler scheduler;
    private final Command commandToSchedule;
    private boolean scheduleRequested;

    private ScheduleDuringExecuteCommand(
        CommandScheduler scheduler, Command commandToSchedule) {
      this.scheduler = scheduler;
      this.commandToSchedule = commandToSchedule;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
      if (!scheduleRequested) {
        scheduleRequested = true;
        scheduler.schedule(commandToSchedule);
      }
    }

    @Override
    public boolean isFinished() {
      return scheduleRequested;
    }

    @Override
    public void end(boolean interrupted) {}
  }

  private static final class FailingCommand extends Command {
    private final FailurePoint failurePoint;
    private boolean failureThrown;

    private FailingCommand(FailurePoint failurePoint) {
      this.failurePoint = failurePoint;
    }

    @Override
    public void initialize() {
      failIf(FailurePoint.INITIALIZE);
    }

    @Override
    public void execute() {
      failIf(FailurePoint.EXECUTE);
    }

    @Override
    public boolean isFinished() {
      failIf(FailurePoint.IS_FINISHED);
      return failurePoint == FailurePoint.END;
    }

    @Override
    public void end(boolean interrupted) {
      failIf(FailurePoint.END);
    }

    private void failIf(FailurePoint point) {
      if (failurePoint == point && !failureThrown) {
        failureThrown = true;
        throw new IllegalStateException("scheduler-" + point.name());
      }
    }
  }
}
