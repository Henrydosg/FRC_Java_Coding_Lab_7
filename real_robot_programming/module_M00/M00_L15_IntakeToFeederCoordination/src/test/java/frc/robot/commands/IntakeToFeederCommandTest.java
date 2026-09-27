// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies Intake-to-Feeder command lifecycle, failures, and scheduler ownership. */
class IntakeToFeederCommandTest {
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetSchedulerAndEnableTeleop() {
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @AfterEach
  void cleanSchedulerAndDisable() {
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void constructorRejectsNullIntake() {
    Rig rig = new Rig();

    assertThrows(
        NullPointerException.class, () -> new IntakeToFeederCommand(null, rig.feeder));
  }

  @Test
  void constructorRejectsNullFeeder() {
    Rig rig = new Rig();

    assertThrows(
        NullPointerException.class, () -> new IntakeToFeederCommand(rig.intake, null));
  }

  @Test
  void requirementsAreExactlyIntakeAndFeeder() {
    Rig rig = new Rig();

    assertEquals(2, rig.command.getRequirements().size());
    assertTrue(rig.command.getRequirements().contains(rig.intake));
    assertTrue(rig.command.getRequirements().contains(rig.feeder));
  }

  @Test
  void constructionProducesNoMechanismOutput() {
    Rig rig = new Rig();

    assertTrue(rig.events.isEmpty());
    assertEquals(
        frc.robot.observation.intake.IntakeObservation.RequestedState.STOPPED,
        rig.intake.getObservation().requestedState());
    assertEquals(
        frc.robot.observation.feeder.FeederObservation.RequestedState.STOPPED,
        rig.feeder.getObservation().requestedState());
  }

  @Test
  void initializeRequestsIntakeExactlyOnce() {
    Rig rig = new Rig();

    rig.command.initialize();

    assertEquals(1, rig.intakeIO.requestCount);
    assertEquals(List.of("intake.request", "feeder.request"), rig.events);
  }

  @Test
  void initializeRequestsFeederExactlyOnce() {
    Rig rig = new Rig();

    rig.command.initialize();

    assertEquals(1, rig.feederIO.requestCount);
  }

  @Test
  void executeDoesNotRepeatRequests() {
    Rig rig = new Rig();
    rig.command.initialize();

    rig.command.execute();
    rig.command.execute();
    rig.command.execute();

    assertEquals(1, rig.intakeIO.requestCount);
    assertEquals(1, rig.feederIO.requestCount);
  }

  @Test
  void isFinishedReturnsFalse() {
    Rig rig = new Rig();

    assertFalse(rig.command.isFinished());
  }

  @Test
  void normalEndStopsFeederThenIntake() {
    Rig rig = new Rig();

    rig.command.end(false);

    assertEquals(List.of("feeder.stop", "intake.stop"), rig.events);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  @Test
  void directInterruptedEndStopsFeederThenIntake() {
    Rig rig = new Rig();

    rig.command.end(true);

    assertEquals(List.of("feeder.stop", "intake.stop"), rig.events);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  @Test
  void intakeRequestFailurePreventsFeederRequest() {
    Rig rig = new Rig();
    rig.intakeIO.requestFailure = new RuntimeException("intake request failed");

    RuntimeException thrown = assertThrows(RuntimeException.class, rig.command::initialize);

    assertSame(rig.intakeIO.requestFailure, thrown);
    assertEquals(0, rig.feederIO.requestCount);
  }

  @Test
  void intakeRequestFailureAttemptsBothCleanupOperations() {
    Rig rig = new Rig();
    rig.intakeIO.requestFailure = new RuntimeException("intake request failed");

    assertThrows(RuntimeException.class, rig.command::initialize);

    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
    assertEquals(
        List.of("intake.request", "feeder.stop", "intake.stop"), rig.events);
  }

  @Test
  void feederRequestFailureAttemptsBothCleanupOperations() {
    Rig rig = new Rig();
    rig.feederIO.requestFailure = new RuntimeException("feeder request failed");

    assertThrows(RuntimeException.class, rig.command::initialize);

    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
    assertEquals(
        List.of("intake.request", "feeder.request", "feeder.stop", "intake.stop"),
        rig.events);
  }

  @Test
  void initializeFailurePreservesPrimaryAndSuppressedCleanupOrder() {
    assertInitializeFailurePreservesCleanupOrder(true);
    assertInitializeFailurePreservesCleanupOrder(false);
  }

  @Test
  void feederStopFailureStillAttemptsIntakeStop() {
    Rig rig = new Rig();
    RuntimeException feederFailure = new RuntimeException("feeder stop failed");
    rig.feederIO.stopFailure = feederFailure;

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> rig.command.end(false));

    assertSame(feederFailure, thrown);
    assertEquals(List.of("feeder.stop", "intake.stop"), rig.events);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  @Test
  void dualStopFailurePreservesPrimaryAndSuppressedOrdering() {
    Rig rig = new Rig();
    RuntimeException feederFailure = new RuntimeException("feeder stop failed");
    RuntimeException intakeFailure = new RuntimeException("intake stop failed");
    rig.feederIO.stopFailure = feederFailure;
    rig.intakeIO.stopFailure = intakeFailure;

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> rig.command.end(true));

    assertSame(feederFailure, thrown);
    assertArrayEquals(new Throwable[] {intakeFailure}, thrown.getSuppressed());
    assertEquals(List.of("feeder.stop", "intake.stop"), rig.events);
  }

  @Test
  void hasNoFlywheelRequirement() {
    Rig rig = new Rig();

    assertFalse(
        rig.command.getRequirements().stream()
            .anyMatch(requirement -> requirement.getClass().getSimpleName().contains("Flywheel")));
  }

  @Test
  void hasNoElevatorRequirement() {
    Rig rig = new Rig();

    assertFalse(
        rig.command.getRequirements().stream()
            .anyMatch(requirement -> requirement.getClass().getSimpleName().contains("Elevator")));
  }

  @Test
  void schedulerSchedulesCommandOnceAndItRemainsScheduled() {
    Rig rig = new Rig();

    assertEquals(0, rig.intakeIO.requestCount);
    assertEquals(0, rig.feederIO.requestCount);

    scheduler.schedule(rig.command);
    assertTrue(rig.command.isScheduled());
    assertEquals(1, rig.intakeIO.requestCount);
    assertEquals(1, rig.feederIO.requestCount);

    scheduler.run();
    scheduler.run();

    assertTrue(rig.command.isScheduled());
    assertEquals(1, rig.intakeIO.requestCount);
    assertEquals(1, rig.feederIO.requestCount);
  }

  @Test
  void schedulerCancellationRunsInterruptedCleanup() {
    Rig rig = new Rig();
    scheduler.schedule(rig.command);
    scheduler.run();
    rig.events.clear();

    scheduler.cancel(rig.command);

    assertFalse(rig.command.isScheduled());
    assertEquals(List.of("feeder.stop", "intake.stop"), rig.events);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  @Test
  void intakeRequirementContentionUsesSchedulerArbitration() {
    Rig rig = new Rig();
    scheduler.schedule(rig.command);
    scheduler.run();
    rig.events.clear();
    Command competitor = createRequirementCompetitor(rig.intake, rig.events, "intake.competitor");

    scheduler.schedule(competitor);

    assertFalse(rig.command.isScheduled());
    assertTrue(competitor.isScheduled());
    assertEquals(
        List.of("feeder.stop", "intake.stop", "intake.competitor"), rig.events);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  @Test
  void feederRequirementContentionUsesSchedulerArbitration() {
    Rig rig = new Rig();
    scheduler.schedule(rig.command);
    scheduler.run();
    rig.events.clear();
    Command competitor = createRequirementCompetitor(rig.feeder, rig.events, "feeder.competitor");

    scheduler.schedule(competitor);

    assertFalse(rig.command.isScheduled());
    assertTrue(competitor.isScheduled());
    assertEquals(
        List.of("feeder.stop", "intake.stop", "feeder.competitor"), rig.events);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.intakeIO.stopCount);
  }

  private static void assertInitializeFailurePreservesCleanupOrder(boolean intakeRequestFails) {
    Rig rig = new Rig();
    String failureMessage = intakeRequestFails ? "intake request failed" : "feeder request failed";
    RuntimeException primaryFailure = new RuntimeException(failureMessage);
    RuntimeException feederCleanupFailure = new RuntimeException("feeder cleanup failed");
    RuntimeException intakeCleanupFailure = new RuntimeException("intake cleanup failed");
    if (intakeRequestFails) {
      rig.intakeIO.requestFailure = primaryFailure;
    } else {
      rig.feederIO.requestFailure = primaryFailure;
    }
    rig.feederIO.stopFailure = feederCleanupFailure;
    rig.intakeIO.stopFailure = intakeCleanupFailure;

    RuntimeException thrown = assertThrows(RuntimeException.class, rig.command::initialize);

    assertSame(primaryFailure, thrown);
    assertArrayEquals(
        new Throwable[] {feederCleanupFailure, intakeCleanupFailure}, thrown.getSuppressed());
    if (intakeRequestFails) {
      assertEquals(
          List.of("intake.request", "feeder.stop", "intake.stop"), rig.events);
      assertEquals(0, rig.feederIO.requestCount);
    } else {
      assertEquals(
          List.of("intake.request", "feeder.request", "feeder.stop", "intake.stop"),
          rig.events);
    }
  }

  private static Command createRequirementCompetitor(
      Subsystem requirement, List<String> events, String eventName) {
    return new Command() {
      {
        addRequirements(requirement);
      }

      @Override
      public void initialize() {
        events.add(eventName);
      }

      @Override
      public boolean isFinished() {
        return false;
      }
    };
  }

  private static final class Rig {
    private final List<String> events = new ArrayList<>();
    private final RecordingIntakeIO intakeIO = new RecordingIntakeIO(events);
    private final RecordingFeederIO feederIO = new RecordingFeederIO(events);
    private final IntakeSubsystem intake = new IntakeSubsystem(intakeIO);
    private final FeederSubsystem feeder = new FeederSubsystem(feederIO);
    private final IntakeToFeederCommand command = new IntakeToFeederCommand(intake, feeder);
  }

  private static final class RecordingIntakeIO implements IntakeIO {
    private final List<String> events;
    private int requestCount;
    private int stopCount;
    private RuntimeException requestFailure;
    private RuntimeException stopFailure;

    private RecordingIntakeIO(List<String> events) {
      this.events = events;
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
      inputs.available = true;
      inputs.connected = true;
    }

    @Override
    public void requestIntake() {
      requestCount++;
      events.add("intake.request");
      if (requestFailure != null) {
        throw requestFailure;
      }
    }

    @Override
    public void stop() {
      stopCount++;
      events.add("intake.stop");
      if (stopFailure != null) {
        throw stopFailure;
      }
    }
  }

  private static final class RecordingFeederIO implements FeederIO {
    private final List<String> events;
    private int requestCount;
    private int stopCount;
    private RuntimeException requestFailure;
    private RuntimeException stopFailure;

    private RecordingFeederIO(List<String> events) {
      this.events = events;
    }

    @Override
    public void updateInputs(FeederIOInputs inputs) {
      inputs.available = true;
      inputs.connected = true;
    }

    @Override
    public void requestFeed() {
      requestCount++;
      events.add("feeder.request");
      if (requestFailure != null) {
        throw requestFailure;
      }
    }

    @Override
    public void stop() {
      stopCount++;
      events.add("feeder.stop");
      if (stopFailure != null) {
        throw stopFailure;
      }
    }
  }
}
