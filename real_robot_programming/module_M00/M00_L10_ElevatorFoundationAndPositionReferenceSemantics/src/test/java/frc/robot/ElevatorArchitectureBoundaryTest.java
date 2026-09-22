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
import static org.junit.jupiter.api.Assertions.assertTrue;

import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.observation.elevator.ElevatorObservation;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.telemetry.elevator.ElevatorTelemetryFacade;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Locks M00_L10 Elevator to its vendor-neutral observation-only boundary. */
class ElevatorArchitectureBoundaryTest {
  private static final Set<String> AUTHORIZED_ELEVATOR_REFERENCE_FILES =
      Set.of(
          "frc/robot/io/elevator/ElevatorIO.java",
          "frc/robot/io/elevator/ElevatorIONoop.java",
          "frc/robot/observation/elevator/ElevatorObservation.java",
          "frc/robot/subsystems/ElevatorSubsystem.java",
          "frc/robot/telemetry/elevator/ElevatorTelemetryFacade.java",
          "frc/robot/RobotContainer.java",
          "frc/robot/telemetry/RobotTelemetry.java");
  private static final Pattern ELEVATOR_PRODUCTION_REFERENCE =
      Pattern.compile(
          "\\b(?:ElevatorSubsystem|ElevatorIO|ElevatorIONoop|ElevatorObservation|"
              + "ElevatorTelemetryFacade|frc\\.robot\\.(?:io\\.elevator|observation\\.elevator|"
              + "telemetry\\.elevator))\\b");
  private static final Pattern ELEVATOR_FORBIDDEN_CONTROL_OR_LIMIT_TOKEN =
      Pattern.compile(
          "\\b(?:requestPosition|setPosition|targetPosition|positionError|setVoltage|"
              + "setPercent|setDutyCycle|PID|PIDController|ProfiledPIDController|PIDF|"
              + "SimpleMotorFeedforward|ElevatorFeedforward|feedforward|MotionMagic|"
              + "Motion\\s+Magic|limitSwitch|forwardLimit|reverseLimit|softLimit|hardLimit|"
              + "minimumTravel|maximumTravel|minTravel|maxTravel|topStop|bottomStop|"
              + "travelLimit|travelRange|heightLimit|minimumPosition|maximumPosition|"
              + "minPosition|maxPosition|limit|clamp|home|homing|zeroing|establishReference|"
              + "referencePosition|setReference|ElevatorController|PositionController|"
              + "MotionController|controller)\\b",
          Pattern.CASE_INSENSITIVE);
  private static final Pattern IMPORT_DECLARATION =
      Pattern.compile(
          "(?m)^\\s*import\\s+(?:static\\s+)?([A-Za-z_$][A-Za-z0-9_$]*(?:\\s*\\.\\s*[A-Za-z_$][A-Za-z0-9_$]*)*(?:\\s*\\.\\s*\\*)?)\\s*;");
  private static final Pattern ELEVATOR_FORBIDDEN_QUALIFIED_COMMAND_REFERENCE =
      Pattern.compile(
          "\\bedu\\.wpi\\.first\\.wpilibj2\\.command\\.(?!SubsystemBase\\b)[A-Za-z_$][A-Za-z0-9_$]*");
  private static final String ELEVATOR_COMMAND_PACKAGE_PREFIX =
      "edu.wpi.first.wpilibj2.command.";
  private static final String ELEVATOR_ALLOWED_SUBSYSTEM_IMPORT =
      ELEVATOR_COMMAND_PACKAGE_PREFIX + "SubsystemBase";
  private static final Pattern ELEVATOR_FORBIDDEN_INTEGRATION =
      Pattern.compile(
          "(?s)\\b(?:whileTrue|whileFalse|onTrue|onFalse|toggleOnTrue|toggleOnFalse|"
              + "setDefaultCommand|Commands\\s*\\.\\s*(?:run|runOnce|startEnd)|"
              + "new\\s+FunctionalCommand|NamedCommands\\s*\\.\\s*registerCommand|"
              + "AutonomousEventBinding|AutonomousEventRegistration|PathPlanner|"
              + "getAutonomousCommand)\\b[^;]*\\b(?:elevator|Elevator)");
  private static final Path ELEVATOR_IO_DIRECTORY =
      Path.of("src", "main", "java", "frc", "robot", "io", "elevator");
  private static final Path ELEVATOR_PRODUCTION_ROOT =
      Path.of("src", "main", "java", "frc", "robot");
  private static final Path PRODUCTION_JAVA_ROOT = Path.of("src", "main", "java");

  @Test
  void elevatorReferencesExistOnlyInApprovedProductionLocations() throws IOException {
    Path productionRoot = PRODUCTION_JAVA_ROOT.toAbsolutePath().normalize();
    Set<String> referencedFiles = new HashSet<>();
    try (Stream<Path> files = Files.walk(productionRoot)) {
      files
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".java"))
          .forEach(
              path -> {
                String source = withoutCommentsAndLiterals(read(path));
                if (ELEVATOR_PRODUCTION_REFERENCE.matcher(source).find()) {
                  String relativePath =
                      productionRoot
                          .relativize(path.toAbsolutePath().normalize())
                          .toString()
                          .replace('\\', '/');
                  referencedFiles.add(relativePath);
                  assertTrue(
                      AUTHORIZED_ELEVATOR_REFERENCE_FILES.contains(relativePath),
                      "Unauthorized production Elevator dependency: " + relativePath);
                }
              });
    }
    assertEquals(AUTHORIZED_ELEVATOR_REFERENCE_FILES, referencedFiles);
  }

  @Test
  void elevatorArchitectureContainsNoVendorImports() throws IOException {
    try (Stream<Path> files = Files.walk(ELEVATOR_PRODUCTION_ROOT)) {
      files
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".java"))
          .filter(path -> path.toString().toLowerCase().contains("elevator"))
          .forEach(
              path -> {
                String source = read(path).toLowerCase();
                assertFalse(source.contains("com.ctre."), path.toString());
                assertFalse(source.contains("com.revrobotics."), path.toString());
              });
    }
  }

  @Test
  void elevatorIoPreservesTheExactContract() throws ReflectiveOperationException {
    Set<String> fieldNames =
        Arrays.stream(ElevatorIOInputs.class.getDeclaredFields())
            .filter(field -> !field.isSynthetic() && !Modifier.isStatic(field.getModifiers()))
            .map(Field::getName)
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("available", "connected", "positionValid", "positionReferenced", "positionMeters"),
        fieldNames);
    assertEquals(boolean.class, ElevatorIOInputs.class.getDeclaredField("available").getType());
    assertEquals(boolean.class, ElevatorIOInputs.class.getDeclaredField("connected").getType());
    assertEquals(
        boolean.class, ElevatorIOInputs.class.getDeclaredField("positionValid").getType());
    assertEquals(
        boolean.class, ElevatorIOInputs.class.getDeclaredField("positionReferenced").getType());
    assertEquals(
        double.class, ElevatorIOInputs.class.getDeclaredField("positionMeters").getType());
    assertEquals(
        Set.of("updateInputs", "stop"),
        Arrays.stream(ElevatorIO.class.getDeclaredMethods())
            .filter(method -> !method.isSynthetic())
            .map(Method::getName)
            .collect(Collectors.toSet()));
    assertEquals(void.class, ElevatorIO.class.getDeclaredMethod("updateInputs", ElevatorIOInputs.class).getReturnType());
    assertEquals(void.class, ElevatorIO.class.getDeclaredMethod("stop").getReturnType());
  }

  @Test
  void observationAndTelemetryPreserveReadOnlyBoundaries() {
    assertEquals(
        List.of("available", "connected", "positionValid", "positionReferenced", "positionMeters"),
        Arrays.stream(ElevatorObservation.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    assertEquals(
        Set.of("close", "publish"), declaredPublicInstanceMethodNames(ElevatorTelemetryFacade.class));
    assertEquals(
        Set.of("getObservation", "periodic", "stop"),
        declaredPublicInstanceMethodNames(ElevatorSubsystem.class));
  }

  @Test
  void elevatorScopeContainsOnlyNoopIoAndNoCommandsOrHardware() throws IOException {
    try (Stream<Path> files = Files.list(ELEVATOR_IO_DIRECTORY)) {
      assertEquals(
          Set.of("ElevatorIO.java", "ElevatorIONoop.java"),
          files.map(path -> path.getFileName().toString()).collect(Collectors.toSet()));
    }
    assertFalse(Files.exists(ELEVATOR_IO_DIRECTORY.resolve("ElevatorIOSim.java")));
    assertFalse(Files.exists(ELEVATOR_IO_DIRECTORY.resolve("ElevatorIOCTRE.java")));
    assertFalse(Files.exists(ELEVATOR_IO_DIRECTORY.resolve("ElevatorIOREV.java")));
    try (Stream<Path> commandFiles =
        Files.list(Path.of("src", "main", "java", "frc", "robot", "commands"))) {
      assertTrue(
          commandFiles
              .map(path -> path.getFileName().toString())
              .noneMatch(name -> name.toLowerCase().contains("elevator")));
    }
    assertFalse(
        read(Path.of("src", "main", "java", "frc", "robot", "Constants.java"))
            .toLowerCase()
            .contains("elevator"));
  }

  @Test
  void elevatorProductionUsesNoRequestedMotionOrControlState() throws IOException {
    try (Stream<Path> files = Files.walk(ELEVATOR_PRODUCTION_ROOT)) {
      files
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".java"))
          .filter(path -> path.toString().toLowerCase().contains("elevator"))
          .forEach(
              path -> {
                String source = withoutCommentsAndLiterals(read(path));
                assertFalse(source.contains("ElevatorRequestedState"), path.toString());
                assertFalse(source.contains("STOPPED"), path.toString());
                assertFalse(
                    ELEVATOR_FORBIDDEN_CONTROL_OR_LIMIT_TOKEN.matcher(source).find(),
                    path + " contains prohibited control, limit, or reference-establishment state");
                assertFalse(
                    importDeclarations(source).stream()
                        .filter(name -> name.startsWith(ELEVATOR_COMMAND_PACKAGE_PREFIX))
                        .anyMatch(name -> !name.equals(ELEVATOR_ALLOWED_SUBSYSTEM_IMPORT)),
                    path + " imports prohibited command behavior");
                assertFalse(
                    ELEVATOR_FORBIDDEN_QUALIFIED_COMMAND_REFERENCE.matcher(source).find(),
                    path + " uses prohibited fully-qualified command behavior");
                assertFalse(
                    Pattern.compile(
                            "\\b(?:NamedCommands|AutoBuilder|AutonomousEvent|CommandXboxController|"
                                + "whileTrue|whileFalse|onTrue|onFalse|toggleOnTrue|toggleOnFalse|"
                                + "setDefaultCommand|getAutonomousCommand)\\b")
                        .matcher(source)
                        .find(),
                    path + " contains command, binding, or autonomous/event integration");
              });
    }
  }

  @Test
  void elevatorHasNoRobotContainerCommandBindingOrAutonomousEventIntegration() throws IOException {
    String robotContainerSource =
        withoutCommentsAndLiterals(
            read(Path.of("src", "main", "java", "frc", "robot", "RobotContainer.java")));
    assertFalse(
        ELEVATOR_FORBIDDEN_INTEGRATION.matcher(robotContainerSource).find(),
        "RobotContainer must not bind, command, or autonomously/event-integrate Elevator");
  }

  private static Set<String> declaredPublicInstanceMethodNames(Class<?> type) {
    return Arrays.stream(type.getDeclaredMethods())
        .filter(method -> Modifier.isPublic(method.getModifiers()))
        .filter(method -> !Modifier.isStatic(method.getModifiers()))
        .filter(method -> !method.isSynthetic())
        .map(Method::getName)
        .collect(Collectors.toSet());
  }

  private static String read(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read " + path, exception);
    }
  }

  private static Set<String> importDeclarations(String source) {
    Matcher matcher = IMPORT_DECLARATION.matcher(source);
    Set<String> imports = new HashSet<>();
    while (matcher.find()) {
      imports.add(matcher.group(1).replaceAll("\\s+", ""));
    }
    return imports;
  }

  static String withoutCommentsAndLiterals(String source) {
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
          if (current == '\n' || current == '\r') state = SourceState.CODE;
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
          if (escaped) escaped = false;
          else if (current == '\\') escaped = true;
          else if (current == '"') state = SourceState.CODE;
        }
        case CHAR_LITERAL -> {
          result.append(current == '\n' || current == '\r' ? current : ' ');
          if (escaped) escaped = false;
          else if (current == '\\') escaped = true;
          else if (current == '\'') state = SourceState.CODE;
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
