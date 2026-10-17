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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import frc.robot.observation.feeder.FeederObservation.RequestedState;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies locked Shoot coordination semantics, failure handling, and architecture boundaries. */
class ShootCommandTest {
  private static final double TEST_TARGET_RPM = 1_637.25;
  private static final Set<String> FLYWHEEL_OBSERVATION_ACCESSORS =
      Arrays.stream(FlywheelObservation.class.getRecordComponents())
          .map(RecordComponent::getName)
          .collect(Collectors.toUnmodifiableSet());

  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetScheduler() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.unregisterAllSubsystems();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @AfterEach
  void cleanScheduler() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.unregisterAllSubsystems();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void productionIdentityConstructorOrderAndExactRequirementsAreLocked()
      throws ReflectiveOperationException {
    assertEquals("ShootCommand", ShootCommand.class.getSimpleName());
    assertEquals("frc.robot.commands", ShootCommand.class.getPackageName());
    assertEquals(Command.class, ShootCommand.class.getSuperclass());

    Constructor<ShootCommand> constructor =
        ShootCommand.class.getDeclaredConstructor(
            FlywheelSubsystem.class, FeederSubsystem.class, double.class);
    assertTrue(Modifier.isPublic(constructor.getModifiers()));
    assertEquals(
        List.of(FlywheelSubsystem.class, FeederSubsystem.class, double.class),
        List.of(constructor.getParameterTypes()));

    Rig rig = new Rig();
    ShootCommand command = rig.command;
    assertEquals(2, command.getRequirements().size());
    assertTrue(command.getRequirements().contains(rig.flywheel));
    assertTrue(command.getRequirements().contains(rig.feeder));
    assertFalse(
        command
            .getRequirements()
            .stream()
            .anyMatch(
                requirement ->
                    requirement.getClass().getSimpleName().contains("Elevator")
                        || requirement.getClass().getSimpleName().contains("Intake")
                        || requirement.getClass().getSimpleName().contains("Swerve")));
    assertFalse(command.runsWhenDisabled());
  }

  @Test
  void constructorRejectsNullDependenciesAndInvalidTargetsWithoutSideEffects() {
    Rig rig = new Rig();
    assertThrows(
        NullPointerException.class, () -> new ShootCommand(null, rig.feeder, TEST_TARGET_RPM));
    assertThrows(
        NullPointerException.class, () -> new ShootCommand(rig.flywheel, null, TEST_TARGET_RPM));

    for (double invalidTarget :
        new double[] {
          Double.NaN,
          Double.POSITIVE_INFINITY,
          Double.NEGATIVE_INFINITY,
          0.0,
          -1.0
        }) {
      assertThrows(
          IllegalArgumentException.class,
          () -> new ShootCommand(rig.flywheel, rig.feeder, invalidTarget));
    }

    assertEquals(0, rig.flywheelIO.requestVelocityCount);
    assertEquals(0, rig.flywheelIO.stopCount);
    assertEquals(0, rig.feederIO.requestFeedCount);
    assertEquals(0, rig.feederIO.stopCount);
    assertEquals(
        frc.robot.observation.flywheel.FlywheelObservation.RequestedState.STOPPED,
        rig.flywheel.getObservation().requestedState());
    assertEquals(RequestedState.STOPPED, rig.feeder.getObservation().requestedState());
  }

  @Test
  void constructionAndInitializeUseOnlySemanticInputsAndPreserveNormalOutputOrder() {
    Rig rig = new Rig();
    ShootCommand command = rig.command;

    assertTrue(rig.events.isEmpty());
    assertEquals(
        frc.robot.observation.flywheel.FlywheelObservation.RequestedState.STOPPED,
        rig.flywheel.getObservation().requestedState());
    assertEquals(RequestedState.STOPPED, rig.feeder.getObservation().requestedState());

    command.initialize();

    assertEquals(List.of("feeder.stop", "flywheel.requestVelocity"), rig.events);
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(0, rig.feederIO.requestFeedCount);
    assertEquals(1, rig.flywheelIO.requestVelocityCount);
    assertEquals(TEST_TARGET_RPM, rig.flywheelIO.lastRequestedVelocityRpm);
    assertEquals(
        frc.robot.observation.flywheel.FlywheelObservation.RequestedState.VELOCITY_REQUESTED,
        rig.flywheel.getObservation().requestedState());

    command.execute();
    command.execute();
    assertEquals(1, rig.flywheelIO.requestVelocityCount);
    assertEquals(0, rig.feederIO.requestFeedCount);
  }

  @Test
  void unreadyFlywheelDoesNotFeed() {
    Rig rig = startedRig();
    rig.setFlywheelNotReady();
    rig.setFeederState(true, true);
    rig.refreshObservations();

    rig.command.execute();

    assertEquals(0, rig.feederIO.requestFeedCount);
  }

  @Test
  void unavailableFeederDoesNotFeed() {
    Rig rig = startedRig();
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(false, false);
    rig.refreshObservations();

    rig.command.execute();

    assertEquals(0, rig.feederIO.requestFeedCount);
  }

  @Test
  void disconnectedFeederDoesNotFeed() {
    Rig rig = startedRig();
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(true, false);
    rig.refreshObservations();

    rig.command.execute();

    assertEquals(0, rig.feederIO.requestFeedCount);
  }

  @Test
  void readyAvailableConnectedRequestsFeedOnceAndUsesSoftwareRequestState() {
    Rig rig = startedRig();
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(true, true);
    rig.refreshObservations();
    ShootCommand command = rig.command;

    command.execute();
    command.execute();
    command.execute();

    assertEquals(1, rig.feederIO.requestFeedCount);
    assertEquals(RequestedState.FEED_REQUESTED, rig.feeder.getObservation().requestedState());
    assertEquals(1, rig.flywheelIO.requestVelocityCount);
    assertEquals(0, rig.flywheelIO.stopCount);
  }

  @Test
  void readinessLossStopsOnceRecoveryRequestsAgainAndFlywheelIntentRemainsActive() {
    Rig rig = startedRig();
    ShootCommand command = rig.command;
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(true, true);
    rig.refreshObservations();

    command.execute();
    assertEquals(1, rig.feederIO.requestFeedCount);

    rig.setFlywheelNotReady();
    rig.refreshObservations();
    command.execute();
    assertEquals(2, rig.feederIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.feeder.getObservation().requestedState());
    assertEquals(
        frc.robot.observation.flywheel.FlywheelObservation.RequestedState.VELOCITY_REQUESTED,
        rig.flywheel.getObservation().requestedState());
    assertEquals(0, rig.flywheelIO.stopCount);

    command.execute();
    assertEquals(2, rig.feederIO.stopCount);

    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.refreshObservations();
    command.execute();
    command.execute();
    assertEquals(2, rig.feederIO.requestFeedCount);
    assertEquals(1, rig.flywheelIO.requestVelocityCount);
    assertEquals(0, rig.flywheelIO.stopCount);
  }

  @Test
  void feederAvailabilityAndConnectionLossStopOnlyOnTheirTransitions() {
    Rig rig = startedRig();
    ShootCommand command = rig.command;
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(true, true);
    rig.refreshObservations();
    command.execute();
    assertEquals(1, rig.feederIO.requestFeedCount);

    rig.setFeederState(false, false);
    rig.refreshObservations();
    command.execute();
    command.execute();
    assertEquals(2, rig.feederIO.stopCount);
    assertEquals(1, rig.feederIO.requestFeedCount);

    rig.setFeederState(true, true);
    rig.refreshObservations();
    command.execute();
    assertEquals(2, rig.feederIO.requestFeedCount);

    rig.setFeederState(true, false);
    rig.refreshObservations();
    command.execute();
    command.execute();
    assertEquals(3, rig.feederIO.stopCount);
    assertEquals(2, rig.feederIO.requestFeedCount);
    assertEquals(1, rig.flywheelIO.requestVelocityCount);
    assertEquals(0, rig.flywheelIO.stopCount);
  }

  @Test
  void existingFeedRequestIsDeduplicatedFromObservationSoftwareState() {
    Rig rig = startedRig();
    ShootCommand command = rig.command;
    rig.feeder.requestFeed();
    assertEquals(RequestedState.FEED_REQUESTED, rig.feeder.getObservation().requestedState());
    rig.setFlywheelNotReady();
    rig.setFeederState(true, true);
    rig.refreshObservations();

    command.execute();

    assertEquals(1, rig.feederIO.requestFeedCount);
    assertEquals(2, rig.feederIO.stopCount);
    assertEquals(RequestedState.STOPPED, rig.feeder.getObservation().requestedState());
  }

  @Test
  void commandNeverFinishesOnItsOwn() {
    Rig rig = new Rig();
    ShootCommand command = rig.command;

    assertFalse(command.isFinished());
    command.initialize();
    command.execute();
    assertFalse(command.isFinished());
    command.end(false);
  }

  @Test
  void initializeFeederStopFailurePreservesPrimaryAndAttemptsOnlyFlywheelCleanup() {
    Rig rig = new Rig();
    RuntimeException primary = new RuntimeException("feeder baseline stop");
    RuntimeException cleanup = new RuntimeException("flywheel cleanup");
    rig.feederIO.stopFailure = primary;
    rig.flywheelIO.stopFailure = cleanup;
    ShootCommand command = rig.command;

    RuntimeException thrown = assertThrows(RuntimeException.class, command::initialize);

    assertSame(primary, thrown);
    assertEquals(List.of(cleanup), List.of(thrown.getSuppressed()));
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(0, rig.flywheelIO.requestVelocityCount);
    assertEquals(1, rig.flywheelIO.stopCount);
  }

  @Test
  void initializeFlywheelRequestFailurePreservesPrimaryAndDoesNotRetry() {
    Rig rig = new Rig();
    RuntimeException primary = new RuntimeException("flywheel request");
    RuntimeException cleanup = new RuntimeException("flywheel cleanup");
    rig.flywheelIO.requestVelocityFailure = primary;
    rig.flywheelIO.stopFailure = cleanup;
    ShootCommand command = rig.command;

    RuntimeException thrown = assertThrows(RuntimeException.class, command::initialize);

    assertSame(primary, thrown);
    assertEquals(List.of(cleanup), List.of(thrown.getSuppressed()));
    assertEquals(1, rig.feederIO.stopCount);
    assertEquals(1, rig.flywheelIO.requestVelocityCount);
    assertEquals(1, rig.flywheelIO.stopCount);
  }

  @Test
  void requestFeedFailureAttemptsBothCleanupsAndSuppressesTheirFailures() {
    Rig rig = startedRig();
    ShootCommand command = rig.command;
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(true, true);
    rig.refreshObservations();
    RuntimeException primary = new RuntimeException("feed request");
    RuntimeException feederCleanup = new RuntimeException("feeder cleanup");
    RuntimeException flywheelCleanup = new RuntimeException("flywheel cleanup");
    rig.feederIO.requestFeedFailure = primary;
    rig.feederIO.stopFailure = feederCleanup;
    rig.flywheelIO.stopFailure = flywheelCleanup;

    RuntimeException thrown = assertThrows(RuntimeException.class, command::execute);

    assertSame(primary, thrown);
    assertEquals(List.of(feederCleanup, flywheelCleanup), List.of(thrown.getSuppressed()));
    assertEquals(1, rig.feederIO.requestFeedCount);
    assertEquals(2, rig.feederIO.stopCount);
    assertEquals(1, rig.flywheelIO.stopCount);
    assertEquals(
        List.of(
            "feeder.stop",
            "flywheel.requestVelocity",
            "feeder.requestFeed",
            "feeder.stop",
            "flywheel.stop"),
        rig.events);
  }

  @Test
  void transitionStopFailureAttemptsFlywheelCleanupWithoutRetryingFeeder() {
    Rig rig = startedRig();
    ShootCommand command = rig.command;
    rig.setFlywheelReady(TEST_TARGET_RPM);
    rig.setFeederState(true, true);
    rig.refreshObservations();
    command.execute();
    RuntimeException primary = new RuntimeException("transition feeder stop");
    RuntimeException cleanup = new RuntimeException("flywheel cleanup");
    rig.feederIO.stopFailure = primary;
    rig.flywheelIO.stopFailure = cleanup;
    rig.setFlywheelNotReady();
    rig.refreshObservations();

    RuntimeException thrown = assertThrows(RuntimeException.class, command::execute);

    assertSame(primary, thrown);
    assertEquals(List.of(cleanup), List.of(thrown.getSuppressed()));
    assertEquals(2, rig.feederIO.stopCount);
    assertEquals(1, rig.flywheelIO.stopCount);
    assertEquals("flywheel.stop", rig.events.get(rig.events.size() - 1));
  }

  @Test
  void endAttemptsBothStopsForNormalAndInterruptedCallsAcrossAllFailureCombinations() {
    for (boolean interrupted : new boolean[] {false, true}) {
      for (int failureCase = 0; failureCase < 4; failureCase++) {
        scheduler.unregisterAllSubsystems();
        Rig rig = startedRig();
        ShootCommand command = rig.command;
        RuntimeException feederFailure = new RuntimeException("terminal feeder stop");
        RuntimeException flywheelFailure = new RuntimeException("terminal flywheel stop");
        rig.feederIO.stopFailure = (failureCase & 1) != 0 ? feederFailure : null;
        rig.flywheelIO.stopFailure = (failureCase & 2) != 0 ? flywheelFailure : null;

        RuntimeException thrown = null;
        try {
          command.end(interrupted);
        } catch (RuntimeException failure) {
          thrown = failure;
        }

        assertEquals(2, rig.feederIO.stopCount);
        assertEquals(1, rig.flywheelIO.stopCount);
        assertEquals(
            List.of("feeder.stop", "flywheel.stop"),
            rig.events.subList(rig.events.size() - 2, rig.events.size()));
        if (failureCase == 0) {
          assertNull(thrown);
        } else if (failureCase == 1 || failureCase == 3) {
          assertSame(feederFailure, thrown);
          if (failureCase == 3) {
            assertEquals(List.of(flywheelFailure), List.of(thrown.getSuppressed()));
          } else {
            assertEquals(0, thrown.getSuppressed().length);
          }
        } else {
          assertSame(flywheelFailure, thrown);
          assertEquals(0, thrown.getSuppressed().length);
        }
      }
    }
  }

  @Test
  void commandArchitectureHasOnlyApprovedImportsAndNoDuplicateControlState() throws IOException {
    String productionSource = source("frc/robot/commands/ShootCommand.java");
    String meaningfulProductionSource = withoutCommentsAndLiterals(productionSource);
    Set<String> imports = imports(meaningfulProductionSource);
    assertEquals(
        Set.of(
            "edu.wpi.first.wpilibj2.command.Command",
            "frc.robot.observation.feeder.FeederObservation",
            "frc.robot.observation.feeder.FeederObservation.RequestedState",
            "frc.robot.observation.flywheel.FlywheelObservation",
            "frc.robot.subsystems.FeederSubsystem",
            "frc.robot.subsystems.FlywheelSubsystem",
            "java.util.Objects"),
        imports);

    Set<String> fieldNames =
        Arrays.stream(ShootCommand.class.getDeclaredFields())
            .filter(field -> !field.isSynthetic())
            .filter(field -> !Modifier.isStatic(field.getModifiers()))
            .map(Field::getName)
            .collect(Collectors.toSet());
    assertEquals(Set.of("flywheel", "feeder", "targetVelocityRpm"), fieldNames);
    assertOnlyApprovedObservationAccesses(meaningfulProductionSource);
    assertFalse(meaningfulProductionSource.contains("velocityRpm"));
    assertFalse(meaningfulProductionSource.contains("velocityValid"));
    assertFalse(meaningfulProductionSource.contains("Tolerance"));
    assertFalse(meaningfulProductionSource.contains("Timer"));
    assertFalse(meaningfulProductionSource.contains("feedingRequested"));
    assertFalse(meaningfulProductionSource.contains("catch (Error"));
  }

  @Test
  void commandAndCompositionRootPreserveTheM00L14L15AndL16Firewalls() throws IOException {
    Path alternateCommand =
        Path.of("src", "main", "java", "frc", "robot", "commands", "ShootCoordinationCommand.java");
    assertFalse(Files.exists(alternateCommand));

    String productionSource =
        withoutCommentsAndLiterals(source("frc/robot/commands/ShootCommand.java"));
    String lowerSource = productionSource.toLowerCase(java.util.Locale.ROOT);
    for (String excluded :
        new String[] {
          "intake",
          "elevator",
          "swerve",
          "pathplanner",
          "namedcommands",
          "constants",
          "timer",
          "feederio",
          "flywheelio",
          "telemetry",
          "networktables",
          "com.ctre",
          "com.revrobotics",
          "frc.robot.constants"
        }) {
      assertFalse(lowerSource.contains(excluded), excluded);
    }

    String robotContainer =
        withoutCommentsAndLiterals(
            Files.readString(Path.of("src", "main", "java", "frc", "robot", "RobotContainer.java")));
    assertFalse(robotContainer.contains("ShootCommand"));
    assertTrue(
        robotContainer.contains("driverController.leftBumper().whileTrue(runFeederCommand);"));
    assertTrue(robotContainer.contains("new RunFeederCommand(feederSubsystem)"));
  }

  private static Rig startedRig() {
    Rig rig = new Rig();
    rig.command.initialize();
    return rig;
  }

  private static int occurrences(String source, Pattern pattern) {
    Matcher matcher = pattern.matcher(source);
    int count = 0;
    while (matcher.find()) {
      count++;
    }
    return count;
  }

  private static Set<String> imports(String source) {
    return source
        .lines()
        .map(String::trim)
        .filter(line -> line.startsWith("import "))
        .map(line -> line.substring("import ".length(), line.length() - 1))
        .collect(Collectors.toSet());
  }

  private static void assertOnlyApprovedObservationAccesses(String meaningfulSource) {
    String flywheelObservation =
        observationVariable(meaningfulSource, "FlywheelObservation", "flywheel");
    String feederObservation =
        observationVariable(meaningfulSource, "FeederObservation", "feeder");
    assertEquals(
        2,
        occurrences(meaningfulSource, Pattern.compile("\\bgetObservation\\s*\\(")),
        "one Flywheel and one Feeder Observation read");

    String accessorAlternation =
        FLYWHEEL_OBSERVATION_ACCESSORS.stream()
            .map(Pattern::quote)
            .collect(Collectors.joining("|"));
    assertFalse(
        Pattern.compile("::\\s*(?:" + accessorAlternation + ")\\b")
            .matcher(meaningfulSource)
            .find(),
        "Observation accessor method references cannot stand in for calls");

    for (String accessor : FLYWHEEL_OBSERVATION_ACCESSORS) {
      int expectedCalls =
          accessor.equals("readyAtSpeed")
                  || Set.of("available", "connected", "requestedState").contains(accessor)
              ? 1
              : 0;
      assertEquals(
          expectedCalls,
          occurrences(
              meaningfulSource,
              Pattern.compile("\\.\\s*" + Pattern.quote(accessor) + "\\s*\\(")),
          accessor);
      if (expectedCalls == 1) {
        String receiver = accessor.equals("readyAtSpeed") ? flywheelObservation : feederObservation;
        assertEquals(
            1,
            occurrences(
                meaningfulSource,
                Pattern.compile(
                    "\\b"
                        + Pattern.quote(receiver)
                        + "\\s*\\.\\s*"
                        + Pattern.quote(accessor)
                        + "\\s*\\(")),
            receiver + "." + accessor);
      }
    }
    assertFeedAdmissionDecision(meaningfulSource, flywheelObservation, feederObservation);
  }

  private static void assertFeedAdmissionDecision(
      String meaningfulSource, String flywheelObservation, String feederObservation) {
    Matcher booleanAssignments =
        Pattern.compile("(?s)\\bboolean\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\s*=\\s*([^;]+);")
            .matcher(meaningfulSource);
    int admissionDecisions = 0;
    while (booleanAssignments.find()) {
      String decisionVariable = booleanAssignments.group(1);
      String expression = booleanAssignments.group(2);
      if (occurrences(expression, Pattern.compile("\\.\\s*readyAtSpeed\\s*\\(")) == 0) {
        continue;
      }
      String normalized = expression.replaceAll("\\s+", "").replace("(", "").replace(")", "");
      String[] terms = normalized.split("&&", -1);
      assertEquals(3, terms.length);
      assertEquals(
          Set.of(
              flywheelObservation + ".readyAtSpeed",
              feederObservation + ".available",
              feederObservation + ".connected"),
          Set.copyOf(Arrays.asList(terms)));
      assertExactExecuteTransitions(
          meaningfulSource,
          flywheelObservation,
          feederObservation,
          decisionVariable,
          expression);
      admissionDecisions++;
    }
    assertEquals(1, admissionDecisions, "Flywheel readiness must be used in feed admission");
  }

  private static void assertExactExecuteTransitions(
      String meaningfulSource,
      String flywheelObservation,
      String feederObservation,
      String decisionVariable,
      String decisionExpression) {
    Matcher execute =
        Pattern.compile("\\bpublic\\s+void\\s+execute\\s*\\(\\s*\\)\\s*\\{")
            .matcher(meaningfulSource);
    assertTrue(execute.find(), "execute() method must exist");
    int executeOpen = meaningfulSource.indexOf('{', execute.start());
    int executeClose = matchingDelimiter(meaningfulSource, executeOpen, '{', '}');
    assertTrue(executeClose > executeOpen, "execute() body must be balanced");
    assertFalse(execute.find(), "only one execute() method is allowed");
    String executeBody = meaningfulSource.substring(executeOpen + 1, executeClose);
    assertEquals(
        0,
        occurrences(
            executeBody,
            Pattern.compile("\\b(?:return|break|continue|for|while|do|switch)\\b")),
        "execute() must not bypass its normal transitions");

    Matcher decisionInitializer =
        Pattern.compile(
                "(?s)\\bboolean\\s+"
                    + Pattern.quote(decisionVariable)
                    + "\\s*=\\s*([^;]+);")
            .matcher(executeBody);
    assertTrue(decisionInitializer.find(), "feed decision must be initialized in execute()");
    assertEquals(
        decisionExpression.replaceAll("\\s+", ""),
        decisionInitializer.group(1).replaceAll("\\s+", ""));
    assertNoLaterWrite(executeBody, decisionVariable, decisionInitializer.end());
    assertFalse(decisionInitializer.find(), "feed decision must have one initializer");

    Matcher stateInitializer =
        Pattern.compile(
                "\\b(?:RequestedState|var)\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\s*=\\s*"
                    + Pattern.quote(feederObservation)
                    + "\\s*\\.\\s*requestedState\\s*\\(\\s*\\)\\s*;")
            .matcher(executeBody);
    assertTrue(stateInitializer.find(), "Feeder state must be captured in execute()");
    String stateVariable = stateInitializer.group(1);
    assertNoLaterWrite(executeBody, stateVariable, stateInitializer.end());
    assertFalse(stateInitializer.find(), "Feeder state must have one initializer");

    Pattern ifPattern = Pattern.compile("\\bif\\s*\\(");
    Pattern elseIfPattern = Pattern.compile("\\belse\\s+if\\s*\\(");
    assertEquals(2, occurrences(executeBody, ifPattern), "exactly two execute() transitions");
    assertEquals(1, occurrences(executeBody, elseIfPattern), "stop must be the else-if branch");
    Matcher positive = ifPattern.matcher(executeBody);
    Matcher negative = elseIfPattern.matcher(executeBody);
    assertTrue(positive.find());
    assertTrue(negative.find());

    int positiveConditionOpen = executeBody.indexOf('(', positive.start());
    int positiveConditionClose = matchingDelimiter(executeBody, positiveConditionOpen, '(', ')');
    assertTrue(positiveConditionClose > positiveConditionOpen, "feed condition must be balanced");
    int positiveBodyOpen = nextNonWhitespace(executeBody, positiveConditionClose + 1);
    assertEquals('{', executeBody.charAt(positiveBodyOpen));
    int positiveBodyClose = matchingDelimiter(executeBody, positiveBodyOpen, '{', '}');
    assertTrue(positiveBodyClose > positiveBodyOpen, "feed branch body must be balanced");

    int negativeConditionOpen = executeBody.indexOf('(', negative.start());
    int negativeConditionClose = matchingDelimiter(executeBody, negativeConditionOpen, '(', ')');
    assertTrue(negativeConditionClose > negativeConditionOpen, "stop condition must be balanced");
    int negativeBodyOpen = nextNonWhitespace(executeBody, negativeConditionClose + 1);
    assertEquals('{', executeBody.charAt(negativeBodyOpen));
    int negativeBodyClose = matchingDelimiter(executeBody, negativeBodyOpen, '{', '}');
    assertTrue(negativeBodyClose > negativeBodyOpen, "stop branch body must be balanced");
    assertTrue(positiveBodyClose < negative.start(), "feed branch must precede stop branch");
    String expectedPrefix =
        "FlywheelObservation"
            + flywheelObservation
            + "=flywheel.getObservation();FeederObservation"
            + feederObservation
            + "=feeder.getObservation();boolean"
            + decisionVariable
            + "="
            + decisionExpression.replaceAll("\\s+", "")
            + ";RequestedState"
            + stateVariable
            + "="
            + feederObservation
            + ".requestedState();";
    assertEquals(
        expectedPrefix,
        executeBody.substring(0, positive.start()).replaceAll("\\s+", ""),
        "normal transitions must follow only the four locked initializers");
    assertTrue(
        executeBody.substring(positiveBodyClose + 1, negative.start()).isBlank(),
        "stop must directly follow feed as else-if");
    assertTrue(
        executeBody.substring(negativeBodyClose + 1).isBlank(),
        "no statement may follow the normal transitions");

    assertExactConditionTerms(
        executeBody.substring(positiveConditionOpen + 1, positiveConditionClose),
        decisionVariable,
        stateVariable + "!=RequestedState.FEED_REQUESTED");
    assertExactConditionTerms(
        executeBody.substring(negativeConditionOpen + 1, negativeConditionClose),
        "!" + decisionVariable,
        stateVariable + "==RequestedState.FEED_REQUESTED");
    String positiveBody = executeBody.substring(positiveBodyOpen + 1, positiveBodyClose);
    String negativeBody = executeBody.substring(negativeBodyOpen + 1, negativeBodyClose);
    assertEquals(1, occurrences(positiveBody, Pattern.compile("\\bfeeder\\s*\\.\\s*requestFeed\\s*\\(")));
    assertEquals(0, occurrences(positiveBody, Pattern.compile("\\bfeeder\\s*\\.\\s*stop\\s*\\(")));
    assertEquals(1, occurrences(negativeBody, Pattern.compile("\\bfeeder\\s*\\.\\s*stop\\s*\\(")));
    assertEquals(0, occurrences(negativeBody, Pattern.compile("\\bfeeder\\s*\\.\\s*requestFeed\\s*\\(")));
    assertEquals(
        "try{feeder.requestFeed();}catch(RuntimeExceptionfailure){"
            + "stopOutputsAfterFailure(failure);throwfailure;}",
        positiveBody.replaceAll("\\s+", ""),
        "feed transition must directly request feed before exception cleanup");
    assertEquals(
        "try{feeder.stop();}catch(RuntimeExceptionfailure){"
            + "stopFlywheelAfterFailure(failure);throwfailure;}",
        negativeBody.replaceAll("\\s+", ""),
        "readiness-loss transition must directly stop feeder before exception cleanup");
  }

  private static void assertNoLaterWrite(String executeBody, String variable, int initializerEnd) {
    Pattern write =
        Pattern.compile(
            "(?<![A-Za-z0-9_$.])"
                + Pattern.quote(variable)
                + "\\s*(?:>>>=|>>=|<<=|[+\\-*/%&|^]=|=(?!=))");
    assertFalse(
        write.matcher(executeBody.substring(initializerEnd)).find(),
        variable + " must not be reassigned after initialization");
  }

  private static void assertExactConditionTerms(
      String condition, String expectedFirst, String expectedSecond) {
    condition = normalizedConditionTerm(condition);
    List<String> terms = new ArrayList<>();
    int depth = 0;
    int start = 0;
    for (int index = 0; index < condition.length(); index++) {
      char current = condition.charAt(index);
      if (current == '(') {
        depth++;
      } else if (current == ')') {
        depth--;
        assertTrue(depth >= 0, "condition parentheses must be balanced");
      } else if (depth == 0 && current == '&' && index + 1 < condition.length()
          && condition.charAt(index + 1) == '&') {
        terms.add(normalizedConditionTerm(condition.substring(start, index)));
        index++;
        start = index + 1;
      }
    }
    assertEquals(0, depth, "condition parentheses must be balanced");
    terms.add(normalizedConditionTerm(condition.substring(start)));
    assertEquals(2, terms.size(), "transition condition must have exactly two predicates");
    assertEquals(Set.of(expectedFirst, expectedSecond), Set.copyOf(terms));
  }

  private static String normalizedConditionTerm(String term) {
    String normalized = term.trim();
    while (normalized.startsWith("(")
        && matchingDelimiter(normalized, 0, '(', ')') == normalized.length() - 1) {
      normalized = normalized.substring(1, normalized.length() - 1).trim();
    }
    return normalized.replaceAll("\\s+", "");
  }

  private static int nextNonWhitespace(String source, int index) {
    while (index < source.length() && Character.isWhitespace(source.charAt(index))) {
      index++;
    }
    assertTrue(index < source.length(), "expected branch body");
    return index;
  }

  private static int matchingDelimiter(String source, int openingIndex, char opening, char closing) {
    int depth = 0;
    for (int index = openingIndex; index < source.length(); index++) {
      if (source.charAt(index) == opening) {
        depth++;
      } else if (source.charAt(index) == closing && --depth == 0) {
        return index;
      }
    }
    return -1;
  }

  private static String observationVariable(
      String meaningfulSource, String observationType, String subsystemField) {
    Matcher declaration =
        Pattern.compile(
                "\\b(?:"
                    + Pattern.quote(observationType)
                    + "|var)\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\s*=\\s*(?:this\\s*\\.\\s*)?"
                    + Pattern.quote(subsystemField)
                    + "\\s*\\.\\s*getObservation\\s*\\(\\s*\\)")
            .matcher(meaningfulSource);
    assertTrue(declaration.find(), observationType + " must be read once into a local variable");
    String variable = declaration.group(1);
    assertFalse(declaration.find(), observationType + " has multiple direct reads");
    return variable;
  }

  private static String withoutCommentsAndLiterals(String source) {
    StringBuilder result = new StringBuilder(source.length());
    SourceState state = SourceState.CODE;
    boolean escaped = false;
    for (int index = 0; index < source.length(); index++) {
      char current = source.charAt(index);
      char next = index + 1 < source.length() ? source.charAt(index + 1) : '\0';
      switch (state) {
        case CODE -> {
          if (current == '/' && next == '/') {
            result.append("  ");
            index++;
            state = SourceState.LINE_COMMENT;
          } else if (current == '/' && next == '*') {
            result.append("  ");
            index++;
            state = SourceState.BLOCK_COMMENT;
          } else if (
              current == '"'
                  && next == '"'
                  && index + 2 < source.length()
                  && source.charAt(index + 2) == '"') {
            result.append("   ");
            index += 2;
            escaped = false;
            state = SourceState.TEXT_BLOCK;
          } else {
            result.append(current);
            if (current == '"') {
              escaped = false;
              state = SourceState.STRING_LITERAL;
            } else if (current == '\'') {
              escaped = false;
              state = SourceState.CHAR_LITERAL;
            }
          }
        }
        case LINE_COMMENT -> {
          result.append(current == '\n' || current == '\r' ? current : ' ');
          if (current == '\n' || current == '\r') {
            state = SourceState.CODE;
          }
        }
        case BLOCK_COMMENT -> {
          if (current == '*' && next == '/') {
            result.append("  ");
            index++;
            state = SourceState.CODE;
          } else {
            result.append(current == '\n' || current == '\r' ? current : ' ');
          }
        }
        case STRING_LITERAL -> {
          result.append(current == '\n' || current == '\r' ? current : ' ');
          if (escaped) {
            escaped = false;
          } else if (current == '\\') {
            escaped = true;
          } else if (current == '"') {
            state = SourceState.CODE;
          }
        }
        case CHAR_LITERAL -> {
          result.append(current == '\n' || current == '\r' ? current : ' ');
          if (escaped) {
            escaped = false;
          } else if (current == '\\') {
            escaped = true;
          } else if (current == '\'') {
            state = SourceState.CODE;
          }
        }
        case TEXT_BLOCK -> {
          if (escaped) {
            result.append(current == '\n' || current == '\r' ? current : ' ');
            escaped = false;
          } else if (current == '\\') {
            result.append(' ');
            escaped = true;
          } else if (
              current == '"'
                  && next == '"'
                  && index + 2 < source.length()
                  && source.charAt(index + 2) == '"') {
            result.append("   ");
            index += 2;
            state = SourceState.CODE;
          } else {
            result.append(current == '\n' || current == '\r' ? current : ' ');
          }
        }
      }
    }
    return result.toString();
  }

  private enum SourceState {
    CODE,
    LINE_COMMENT,
    BLOCK_COMMENT,
    STRING_LITERAL,
    CHAR_LITERAL,
    TEXT_BLOCK
  }

  private static String source(String relativePath) throws IOException {
    return Files.readString(Path.of("src", "main", "java").resolve(relativePath));
  }

  private static final class Rig {
    private final List<String> events = new ArrayList<>();
    private final RecordingFlywheelIO flywheelIO = new RecordingFlywheelIO(events);
    private final RecordingFeederIO feederIO = new RecordingFeederIO(events);
    private final FlywheelSubsystem flywheel = new FlywheelSubsystem(flywheelIO);
    private final FeederSubsystem feeder = new FeederSubsystem(feederIO);
    private final ShootCommand command = new ShootCommand(flywheel, feeder, TEST_TARGET_RPM);

    private void setFlywheelReady(double targetRpm) {
      flywheelIO.available = true;
      flywheelIO.connected = true;
      flywheelIO.velocityValid = true;
      flywheelIO.velocityRpm = targetRpm;
    }

    private void setFlywheelNotReady() {
      flywheelIO.available = true;
      flywheelIO.connected = true;
      flywheelIO.velocityValid = false;
      flywheelIO.velocityRpm = 0.0;
    }

    private void setFeederState(boolean available, boolean connected) {
      feederIO.available = available;
      feederIO.connected = connected;
    }

    private void refreshObservations() {
      flywheel.periodic();
      feeder.periodic();
    }
  }

  private static final class RecordingFlywheelIO implements FlywheelIO {
    private final List<String> events;
    private boolean available;
    private boolean connected;
    private boolean velocityValid;
    private double velocityRpm;
    private int requestVelocityCount;
    private int stopCount;
    private double lastRequestedVelocityRpm = Double.NaN;
    private RuntimeException requestVelocityFailure;
    private RuntimeException stopFailure;

    private RecordingFlywheelIO(List<String> events) {
      this.events = events;
    }

    @Override
    public void updateInputs(FlywheelIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
      inputs.velocityValid = velocityValid;
      inputs.velocityRpm = velocityRpm;
    }

    @Override
    public void requestVelocity(double targetRpm) {
      requestVelocityCount++;
      lastRequestedVelocityRpm = targetRpm;
      events.add("flywheel.requestVelocity");
      if (requestVelocityFailure != null) {
        throw requestVelocityFailure;
      }
    }

    @Override
    public void stop() {
      stopCount++;
      events.add("flywheel.stop");
      if (stopFailure != null) {
        throw stopFailure;
      }
    }
  }

  private static final class RecordingFeederIO implements FeederIO {
    private final List<String> events;
    private boolean available;
    private boolean connected;
    private int requestFeedCount;
    private int stopCount;
    private RuntimeException requestFeedFailure;
    private RuntimeException stopFailure;

    private RecordingFeederIO(List<String> events) {
      this.events = events;
    }

    @Override
    public void updateInputs(FeederIOInputs inputs) {
      inputs.available = available;
      inputs.connected = connected;
    }

    @Override
    public void requestFeed() {
      requestFeedCount++;
      events.add("feeder.requestFeed");
      if (requestFeedFailure != null) {
        throw requestFeedFailure;
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
