// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.commands.AutonomousEventBinding;
import frc.robot.commands.AutonomousEventRegistration;
import frc.robot.commands.IntakeToFeederCommand;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIO.IntakeIOInputs;
import frc.robot.observation.AutonomousEventObservation;
import frc.robot.observation.AutonomousEventObservation.LifecycleState;
import frc.robot.observation.feeder.FeederObservation;
import frc.robot.observation.intake.IntakeObservation;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Exercises the RobotContainer named event through the real command scheduler. */
class RobotContainerMechanismAutonomousEventIntegrationTest {
  private static final String EVENT_NAME = AutonomousEventId.LEARNING_EVENT.pathPlannerName();
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableAutonomousWithCleanRegistration() {
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @AfterEach
  void clearSchedulerAndRegistration() {
    scheduler.cancelAll();
    scheduler.unregisterAllSubsystems();
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void learningEventRequiresExactlyIntakeAndFeeder() throws Exception {
    Rig rig = new Rig();

    assertTrue(NamedCommands.hasCommand(EVENT_NAME));
    assertEquals(
        Set.of(rig.intake, rig.feeder), NamedCommands.getCommand(EVENT_NAME).getRequirements());
    assertEquals(Set.of(rig.intake, rig.feeder), registeredDeferredCommand().getRequirements());
  }

  @Test
  void eachDispatchCreatesFreshIntakeToFeederChild() throws Exception {
    new Rig();
    DeferredCommand deferred = registeredDeferredCommand();
    Command firstEvent = NamedCommands.getCommand(EVENT_NAME);

    scheduler.schedule(firstEvent);
    IntakeToFeederCommand firstChild =
        assertInstanceOf(IntakeToFeederCommand.class, activeChild(deferred));
    scheduler.cancel(firstEvent);

    Command secondEvent = NamedCommands.getCommand(EVENT_NAME);
    scheduler.schedule(secondEvent);
    IntakeToFeederCommand secondChild =
        assertInstanceOf(IntakeToFeederCommand.class, activeChild(deferred));

    assertNotSame(firstEvent, secondEvent);
    assertNotSame(firstChild, secondChild);
    assertTrue(secondEvent.isScheduled());
  }

  @Test
  void schedulerDispatchStartsBothSemanticRequests() throws Exception {
    Rig rig = new Rig();
    Command event = NamedCommands.getCommand(EVENT_NAME);

    scheduler.schedule(event);
    scheduler.run();

    assertTrue(event.isScheduled());
    assertEquals(List.of("intake.request", "feeder.request"), rig.events);
    assertEquals(
        IntakeObservation.RequestedState.INTAKE_REQUESTED,
        rig.intake.getObservation().requestedState());
    assertEquals(
        FeederObservation.RequestedState.FEED_REQUESTED,
        rig.feeder.getObservation().requestedState());
  }

  @Test
  void repeatedSchedulerCyclesDoNotRecreateOrReinitializeChild() throws Exception {
    Rig rig = new Rig();
    DeferredCommand deferred = registeredDeferredCommand();
    Command event = NamedCommands.getCommand(EVENT_NAME);
    scheduler.schedule(event);
    Command child = activeChild(deferred);

    scheduler.run();
    scheduler.run();
    scheduler.run();

    assertTrue(event.isScheduled());
    assertSame(child, activeChild(deferred));
    assertEquals(1, rig.intakeIO.requestCount);
    assertEquals(1, rig.feederIO.requestCount);
  }

  @Test
  void cancellationStopsFeederBeforeIntake() throws Exception {
    Rig rig = new Rig();
    Command event = NamedCommands.getCommand(EVENT_NAME);
    scheduler.schedule(event);
    scheduler.run();
    rig.events.clear();

    scheduler.cancel(event);

    assertFalse(event.isScheduled());
    assertEquals(List.of("feeder.stop", "intake.stop"), rig.events);
    assertEquals(
        FeederObservation.RequestedState.STOPPED, rig.feeder.getObservation().requestedState());
    assertEquals(
        IntakeObservation.RequestedState.STOPPED, rig.intake.getObservation().requestedState());
  }

  @Test
  void intakeContentionIsResolvedByScheduler() throws Exception {
    Rig rig = new Rig();
    Command event = NamedCommands.getCommand(EVENT_NAME);
    scheduler.schedule(event);
    scheduler.run();
    rig.events.clear();
    Command competitor = requirementCompetitor(rig.intake, rig.events, "intake.competitor");

    scheduler.schedule(competitor);

    assertFalse(event.isScheduled());
    assertTrue(competitor.isScheduled());
    assertEquals(List.of("feeder.stop", "intake.stop", "intake.competitor"), rig.events);
  }

  @Test
  void feederContentionIsResolvedByScheduler() throws Exception {
    Rig rig = new Rig();
    Command event = NamedCommands.getCommand(EVENT_NAME);
    scheduler.schedule(event);
    scheduler.run();
    rig.events.clear();
    Command competitor = requirementCompetitor(rig.feeder, rig.events, "feeder.competitor");

    scheduler.schedule(competitor);

    assertFalse(event.isScheduled());
    assertTrue(competitor.isScheduled());
    assertEquals(List.of("feeder.stop", "intake.stop", "feeder.competitor"), rig.events);
  }

  @Test
  void nonemptyRequirementFactoryFailurePublishesObservationWithoutActuation() {
    List<String> events = new ArrayList<>();
    IntakeSubsystem intake = new IntakeSubsystem(new RecordingIntakeIO(events));
    FeederSubsystem feeder = new FeederSubsystem(new RecordingFeederIO(events));
    List<AutonomousEventObservation> observations = new ArrayList<>();
    AutonomousEventRegistration registration = new AutonomousEventRegistration(observations::add);
    registration.register(
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT,
            () -> {
              throw new IllegalStateException("factory failure");
            },
            Set.of(intake, feeder)));
    Command event = NamedCommands.getCommand(EVENT_NAME);

    assertEquals(Set.of(intake, feeder), event.getRequirements());
    scheduler.schedule(event);
    scheduler.run();

    assertEquals(1, observations.size());
    assertEquals(LifecycleState.FACTORY_FAILURE, observations.get(0).state());
    assertTrue(events.isEmpty());
    assertEquals(
        IntakeObservation.RequestedState.STOPPED, intake.getObservation().requestedState());
    assertEquals(
        FeederObservation.RequestedState.STOPPED, feeder.getObservation().requestedState());
  }

  private static Command requirementCompetitor(
      Subsystem requirement, List<String> events, String name) {
    return new Command() {
      {
        addRequirements(requirement);
      }

      @Override
      public void initialize() {
        events.add(name);
      }

      @Override
      public boolean isFinished() {
        return false;
      }
    };
  }

  private static DeferredCommand registeredDeferredCommand() throws Exception {
    Field registryField = NamedCommands.class.getDeclaredField("namedCommands");
    registryField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, Command> registry = (Map<String, Command>) registryField.get(null);
    return assertInstanceOf(DeferredCommand.class, registry.get(EVENT_NAME));
  }

  private static Command activeChild(DeferredCommand deferred) throws Exception {
    Field childField = DeferredCommand.class.getDeclaredField("m_command");
    childField.setAccessible(true);
    return (Command) childField.get(deferred);
  }

  private static <T> T field(Object owner, String name, Class<T> type) throws Exception {
    Field declaredField = owner.getClass().getDeclaredField(name);
    declaredField.setAccessible(true);
    return type.cast(declaredField.get(owner));
  }

  private static void replaceIo(Object subsystem, String name, Object io) throws Exception {
    Field ioField = subsystem.getClass().getDeclaredField(name);
    ioField.setAccessible(true);
    ioField.set(subsystem, io);
  }

  private static final class Rig {
    private final List<String> events = new ArrayList<>();
    private final RobotContainer robotContainer = new RobotContainer();
    private final IntakeSubsystem intake;
    private final FeederSubsystem feeder;
    private final RecordingIntakeIO intakeIO = new RecordingIntakeIO(events);
    private final RecordingFeederIO feederIO = new RecordingFeederIO(events);

    private Rig() throws Exception {
      intake = field(robotContainer, "intakeSubsystem", IntakeSubsystem.class);
      feeder = field(robotContainer, "feederSubsystem", FeederSubsystem.class);
      replaceIo(intake, "intakeIO", intakeIO);
      replaceIo(feeder, "feederIO", feederIO);
    }
  }

  private static final class RecordingIntakeIO implements IntakeIO {
    private final List<String> events;
    private int requestCount;

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
    }

    @Override
    public void stop() {
      events.add("intake.stop");
    }
  }

  private static final class RecordingFeederIO implements FeederIO {
    private final List<String> events;
    private int requestCount;

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
    }

    @Override
    public void stop() {
      events.add("feeder.stop");
    }
  }
}
