// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIONoop;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.elevator.ElevatorTelemetryFacade;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies Noop Elevator composition without command or binding ownership. */
class RobotContainerElevatorCompositionTest {
  private static final Pattern ELEVATOR_SUBSYSTEM_DECLARATION =
      Pattern.compile("\\bElevatorSubsystem\\s+([A-Za-z_$][A-Za-z0-9_$]*)\\b");
  private static final Pattern ELEVATOR_SUBSYSTEM_CONSTRUCTION =
      Pattern.compile(
          "\\b([A-Za-z_$][A-Za-z0-9_$]*)\\s*=\\s*new\\s+ElevatorSubsystem\\b");
  private static final String ELEVATOR_TYPE_TOKEN =
      "\\b(?:ElevatorSubsystem|ElevatorIO|ElevatorIONoop|ElevatorObservation|"
          + "ElevatorTelemetryFacade)\\b";

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void composesNoopElevatorAndConnectsReadOnlyTelemetry()
      throws ReflectiveOperationException {
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    try {
      RobotContainer robotContainer = new RobotContainer();
      ElevatorSubsystem elevatorSubsystem =
          (ElevatorSubsystem) field(RobotContainer.class, "elevatorSubsystem").get(robotContainer);
      ElevatorIO elevatorIO =
          (ElevatorIO) field(ElevatorSubsystem.class, "elevatorIO").get(elevatorSubsystem);
      RobotTelemetry robotTelemetry = robotContainer.getRobotTelemetry();

      assertInstanceOf(ElevatorIONoop.class, elevatorIO);
      assertNull(elevatorSubsystem.getDefaultCommand());

      @SuppressWarnings("unchecked")
      Optional<ElevatorSubsystem> telemetryElevatorSubsystem =
          (Optional<ElevatorSubsystem>)
              field(RobotTelemetry.class, "elevatorSubsystem").get(robotTelemetry);
      @SuppressWarnings("unchecked")
      Optional<ElevatorTelemetryFacade> telemetryFacade =
          (Optional<ElevatorTelemetryFacade>)
              field(RobotTelemetry.class, "elevatorTelemetryFacade").get(robotTelemetry);

      assertTrue(telemetryElevatorSubsystem.isPresent());
      assertSame(elevatorSubsystem, telemetryElevatorSubsystem.orElseThrow());
      assertTrue(telemetryFacade.isPresent());

      elevatorSubsystem.periodic();
      robotTelemetry.periodic();
      NetworkTable elevatorTable = NetworkTableInstance.getDefault().getTable("Elevator");
      assertFalse(elevatorTable.getEntry("Available").getBoolean(true));
      assertFalse(elevatorTable.getEntry("Connected").getBoolean(true));
      assertFalse(elevatorTable.getEntry("PositionValid").getBoolean(true));
      assertFalse(elevatorTable.getEntry("PositionReferenced").getBoolean(true));
      assertEquals(0.0, elevatorTable.getEntry("PositionMeters").getDouble(1.0));
    } finally {
      AutoBuilder.resetForTesting();
      NamedCommands.clearAll();
    }
  }

  @Test
  void containsNoElevatorControlBindingOrHardwareSelection() throws IOException {
    String source =
        ElevatorArchitectureBoundaryTest.withoutCommentsAndLiterals(
            Files.readString(
                Path.of("src", "main", "java", "frc", "robot", "RobotContainer.java")));
    Set<String> elevatorIdentifiers = elevatorSubsystemIdentifiers(source);
    assertFalse(elevatorIdentifiers.isEmpty(), "RobotContainer must compose an ElevatorSubsystem");
    assertTrue(
        ELEVATOR_SUBSYSTEM_CONSTRUCTION.matcher(source).find(),
        "RobotContainer must construct ElevatorSubsystem explicitly");
    for (String identifier : elevatorIdentifiers) {
      assertNoElevatorObjectParticipation(source, identifier);
    }
    assertNoElevatorTypeParticipation(source);
    assertFalse(source.contains("ElevatorIOSim"));
    assertFalse(source.contains("ElevatorIOCTRE"));
  }

  private static Set<String> elevatorSubsystemIdentifiers(String source) {
    Set<String> identifiers = new HashSet<>();
    Matcher declaration = ELEVATOR_SUBSYSTEM_DECLARATION.matcher(source);
    while (declaration.find()) {
      identifiers.add(declaration.group(1));
    }
    Matcher construction = ELEVATOR_SUBSYSTEM_CONSTRUCTION.matcher(source);
    while (construction.find()) {
      identifiers.add(construction.group(1));
    }
    return identifiers;
  }

  private static void assertNoElevatorObjectParticipation(String source, String identifier) {
    String token = "\\b" + Pattern.quote(identifier) + "\\b";
    assertFalse(
        Pattern.compile(
                token
                    + "\\s*\\.\\s*(?:stop|requestPosition|setPosition|setVoltage|setPercent|"
                    + "setDutyCycle|setDefaultCommand|schedule)\\s*\\(")
            .matcher(source)
            .find(),
        "Elevator object must not directly control or schedule commands");
    assertFalse(
        Pattern.compile(
                "(?s)\\b(?:whileTrue|onTrue|onFalse|toggleOnTrue|toggleOnFalse|schedule)\\s*"
                    + "\\([^;]*"
                    + token)
            .matcher(source)
            .find(),
        "Elevator object must not participate in controller or trigger APIs");
    assertFalse(
        Pattern.compile(
                "(?s)\\bCommands\\s*\\.\\s*[A-Za-z_$][A-Za-z0-9_$]*\\s*\\([^;]*"
                    + token)
            .matcher(source)
            .find(),
        "Elevator object must not participate in Commands factories");
    assertFalse(
        Pattern.compile(
                "(?s)\\bnew\\s+(?!(?:ElevatorSubsystem|ElevatorIONoop|"
                    + "ElevatorTelemetryFacade|RobotTelemetry)\\b)[A-Za-z_$][A-Za-z0-9_$.]*"
                    + "\\s*\\([^;]*"
                    + token)
            .matcher(source)
            .find(),
        "Elevator object must not be passed to a command or action constructor");
    assertFalse(
        Pattern.compile(
                "(?s)\\b(?:NamedCommands\\s*\\.\\s*registerCommand|"
                    + "AutonomousEventBinding|AutonomousEventRegistration|PathPlanner|"
                    + "getAutonomousCommand|setDefaultOption|addOption|register[A-Za-z_$]*|"
                    + "configure[A-Za-z_$]*)\\b[^;]*"
                    + token)
            .matcher(source)
            .find(),
        "Elevator object must not participate in autonomous or event registration");
    assertFalse(
        Pattern.compile(
                "(?s)\\b(?:build|create|make|get|prepare|command|action|run|runOnce|startEnd|"
                    + "defer|either)[A-Za-z_$]*\\s*\\([^;]*"
                    + token)
            .matcher(source)
            .find(),
        "Elevator object must not flow into a generic command factory");
  }

  private static void assertNoElevatorTypeParticipation(String source) {
    assertFalse(
        Pattern.compile(
                "(?s)\\bnew\\s+(?!(?:ElevatorSubsystem|ElevatorIONoop|"
                    + "ElevatorTelemetryFacade|RobotTelemetry)\\b)[A-Za-z_$][A-Za-z0-9_$.]*"
                    + "\\s*\\([^;]*"
                    + ELEVATOR_TYPE_TOKEN)
            .matcher(source)
            .find(),
        "Elevator production types must not be passed to command constructors");
    assertFalse(
        Pattern.compile(
                "(?s)\\bCommands\\s*\\.\\s*[A-Za-z_$][A-Za-z0-9_$]*\\s*\\([^;]*"
                    + ELEVATOR_TYPE_TOKEN)
            .matcher(source)
            .find(),
        "Elevator production types must not participate in Commands factories");
    assertFalse(
        Pattern.compile(
                "(?s)\\b(?:whileTrue|onTrue|onFalse|toggleOnTrue|toggleOnFalse|schedule)\\s*"
                    + "\\([^;]*"
                    + ELEVATOR_TYPE_TOKEN)
            .matcher(source)
            .find(),
        "Elevator production types must not participate in trigger APIs");
    assertFalse(
        Pattern.compile(
                "(?s)\\b(?:NamedCommands\\s*\\.\\s*registerCommand|"
                    + "AutonomousEventBinding|AutonomousEventRegistration|PathPlanner|"
                    + "getAutonomousCommand|setDefaultOption|addOption|register[A-Za-z_$]*|"
                    + "configure[A-Za-z_$]*)\\b[^;]*"
                    + ELEVATOR_TYPE_TOKEN)
            .matcher(source)
            .find(),
        "Elevator production types must not participate in autonomous or event registration");
  }

  private static Field field(Class<?> owner, String name) throws NoSuchFieldException {
    Field field = owner.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }
}
