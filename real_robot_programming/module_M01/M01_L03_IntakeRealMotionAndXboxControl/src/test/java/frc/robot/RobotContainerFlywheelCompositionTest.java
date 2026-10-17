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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIONoop;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies Noop Flywheel composition without command or binding ownership. */
class RobotContainerFlywheelCompositionTest {
  private static final Path ROBOT_CONTAINER_SOURCE =
      Path.of("src", "main", "java", "frc", "robot", "RobotContainer.java");
  private static final Pattern FLYWHEEL_DIRECT_CONTROL =
      Pattern.compile(
          "\\bflywheelSubsystem\\s*(?:\\.\\s*(?:requestVelocity|stop)\\s*\\(|::\\s*(?:requestVelocity|stop)\\b)");
  private static final Pattern FLYWHEEL_TRIGGER_BINDING =
      Pattern.compile(
          "(?s)\\.(?:whileTrue|onTrue|onFalse|toggleOnTrue|toggleOnFalse)\\s*\\([^;]*\\bflywheelSubsystem\\b");
  private static final Pattern FLYWHEEL_AUTONOMOUS_REGISTRATION =
      Pattern.compile(
          "(?s)(?:NamedCommands\\s*\\.\\s*registerCommand|new\\s+AutonomousEventBinding)\\s*\\([^;]*\\bflywheelSubsystem\\b");
  private static final Pattern FEEDER_FLYWHEEL_COMMAND_CONSTRUCTION =
      Pattern.compile(
          "(?s)new\\s+\\w*Command\\s*\\([^;]*(?:\\bfeederSubsystem\\b[^;]*\\bflywheelSubsystem\\b|\\bflywheelSubsystem\\b[^;]*\\bfeederSubsystem\\b)");

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void composesNoopFlywheelAndConnectsReadOnlyTelemetry()
      throws ReflectiveOperationException {
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    try {
      RobotContainer robotContainer = new RobotContainer();
      FlywheelSubsystem flywheelSubsystem =
          (FlywheelSubsystem) field(RobotContainer.class, "flywheelSubsystem").get(robotContainer);
      FlywheelIO flywheelIO =
          (FlywheelIO) field(FlywheelSubsystem.class, "flywheelIO").get(flywheelSubsystem);
      RobotTelemetry robotTelemetry = robotContainer.getRobotTelemetry();

      assertInstanceOf(FlywheelIONoop.class, flywheelIO);
      assertNull(flywheelSubsystem.getDefaultCommand());

      @SuppressWarnings("unchecked")
      Optional<FlywheelSubsystem> telemetryFlywheelSubsystem =
          (Optional<FlywheelSubsystem>)
              field(RobotTelemetry.class, "flywheelSubsystem").get(robotTelemetry);
      @SuppressWarnings("unchecked")
      Optional<FlywheelTelemetryFacade> telemetryFacade =
          (Optional<FlywheelTelemetryFacade>)
              field(RobotTelemetry.class, "flywheelTelemetryFacade").get(robotTelemetry);

      assertTrue(telemetryFlywheelSubsystem.isPresent());
      assertSame(flywheelSubsystem, telemetryFlywheelSubsystem.orElseThrow());
      assertTrue(telemetryFacade.isPresent());

      flywheelSubsystem.periodic();
      robotTelemetry.periodic();
      NetworkTable flywheelTable = NetworkTableInstance.getDefault().getTable("Flywheel");
      assertFalse(flywheelTable.getEntry("Available").getBoolean(true));
      assertFalse(flywheelTable.getEntry("Connected").getBoolean(true));
      assertFalse(flywheelTable.getEntry("VelocityValid").getBoolean(true));
      assertEquals(0.0, flywheelTable.getEntry("VelocityRpm").getDouble(-1.0));
      assertEquals("STOPPED", flywheelTable.getEntry("RequestedState").getString(""));
    } finally {
      AutoBuilder.resetForTesting();
      NamedCommands.clearAll();
    }
  }

  @Test
  void containsNoFlywheelBindingAutonomousRegistrationOrFeederCoordination()
      throws IOException {
    String activeRobotContainerSource = robotContainerSourceWithoutComments();

    assertFalse(FLYWHEEL_DIRECT_CONTROL.matcher(activeRobotContainerSource).find());
    assertFalse(FLYWHEEL_TRIGGER_BINDING.matcher(activeRobotContainerSource).find());
    assertFalse(FLYWHEEL_AUTONOMOUS_REGISTRATION.matcher(activeRobotContainerSource).find());
    assertFalse(FEEDER_FLYWHEEL_COMMAND_CONSTRUCTION.matcher(activeRobotContainerSource).find());
  }

  private static Field field(Class<?> owner, String name) throws NoSuchFieldException {
    Field field = owner.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  private static String robotContainerSourceWithoutComments() throws IOException {
    return withoutComments(Files.readString(ROBOT_CONTAINER_SOURCE));
  }

  private static String withoutComments(String source) {
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
          result.append(current);
          if (escaped) {
            escaped = false;
          } else if (current == '\\') {
            escaped = true;
          } else if (current == '"') {
            state = SourceState.CODE;
          }
        }
        case CHAR_LITERAL -> {
          result.append(current);
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
}
