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

import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import frc.robot.io.flywheel.FlywheelIONoop;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.observation.flywheel.FlywheelObservation.RequestedState;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Locks the M00_L07 Flywheel foundation to its approved one-mechanism boundary. */
class FlywheelArchitectureBoundaryTest {
  private static final Pattern IMPORT_DECLARATION =
      Pattern.compile(
          "(?m)^\\s*import\\s+(?:static\\s+)?([A-Za-z_$][A-Za-z0-9_$]*(?:\\s*\\.\\s*[A-Za-z_$][A-Za-z0-9_$]*)*(?:\\s*\\.\\s*\\*)?)\\s*;");
  private static final Pattern TYPE_DECLARATION =
      Pattern.compile("\\b(?:class|interface|record|enum)\\s+([A-Za-z_$][A-Za-z0-9_$]*)");

  @Test
  void flywheelFoundationContainsNoVendorImports() throws IOException {
    for (String relativePath :
        new String[] {
          "frc/robot/io/flywheel/FlywheelIO.java",
          "frc/robot/io/flywheel/FlywheelIONoop.java",
          "frc/robot/subsystems/FlywheelSubsystem.java",
          "frc/robot/observation/flywheel/FlywheelObservation.java",
          "frc/robot/telemetry/flywheel/FlywheelTelemetryFacade.java"
        }) {
      Set<String> imports = importDeclarations(relativePath);
      assertFalse(imports.stream().anyMatch(name -> name.startsWith("com.ctre.")));
      assertFalse(imports.stream().anyMatch(name -> name.startsWith("com.revrobotics.")));
    }
  }

  @Test
  void flywheelIoPreservesTheExactContract() throws ReflectiveOperationException {
    Set<String> semanticFieldNames = new HashSet<>();
    for (Field field : FlywheelIOInputs.class.getDeclaredFields()) {
      if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
        semanticFieldNames.add(field.getName());
      }
    }

    assertEquals(
        Set.of("available", "connected", "velocityValid", "velocityRpm"), semanticFieldNames);
    assertEquals(boolean.class, FlywheelIOInputs.class.getDeclaredField("available").getType());
    assertEquals(boolean.class, FlywheelIOInputs.class.getDeclaredField("connected").getType());
    assertEquals(boolean.class, FlywheelIOInputs.class.getDeclaredField("velocityValid").getType());
    assertEquals(double.class, FlywheelIOInputs.class.getDeclaredField("velocityRpm").getType());
    assertEquals(
        void.class,
        FlywheelIO.class.getDeclaredMethod("updateInputs", FlywheelIOInputs.class).getReturnType());
    assertEquals(
        void.class, FlywheelIO.class.getDeclaredMethod("requestVelocity", double.class).getReturnType());
    assertEquals(void.class, FlywheelIO.class.getDeclaredMethod("stop").getReturnType());
    assertEquals(
        Set.of("updateInputs", "requestVelocity", "stop"),
        Arrays.stream(FlywheelIO.class.getDeclaredMethods())
            .filter(method -> !method.isSynthetic())
            .map(Method::getName)
            .collect(Collectors.toSet()));
  }

  @Test
  void observationPreservesExactFieldsAndRequestedStates() {
    assertEquals(
        List.of(
            "available",
            "connected",
            "velocityValid",
            "velocityRpm",
            "requestedState",
            "readyAtSpeed"),
        Arrays.stream(FlywheelObservation.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    assertEquals(
        List.of(
            boolean.class,
            boolean.class,
            boolean.class,
            double.class,
            RequestedState.class,
            boolean.class),
        Arrays.stream(FlywheelObservation.class.getRecordComponents())
            .map(component -> component.getType())
            .toList());
    assertEquals(
        List.of(RequestedState.STOPPED, RequestedState.VELOCITY_REQUESTED),
        Arrays.asList(RequestedState.values()));
  }

  @Test
  void observationAndTelemetryPreserveReadOnlyDependencies()
      throws IOException, ReflectiveOperationException {
    Set<String> observationImports =
        importDeclarations("frc/robot/observation/flywheel/FlywheelObservation.java");
    assertNoImportBeginningWith(observationImports, "frc.robot.io.");
    assertNoImportBeginningWith(observationImports, "frc.robot.subsystems.");
    assertNoImportBeginningWith(observationImports, "edu.wpi.first.networktables.");
    assertNoImportBeginningWith(observationImports, "edu.wpi.first.wpilibj2.command.");
    assertFalse(observationImports.contains("frc.robot.RobotContainer"));

    Set<String> telemetryImports =
        importDeclarations("frc/robot/telemetry/flywheel/FlywheelTelemetryFacade.java");
    assertNoImportBeginningWith(telemetryImports, "frc.robot.io.flywheel.");
    assertNoImportBeginningWith(telemetryImports, "frc.robot.subsystems.");
    assertNoImportBeginningWith(telemetryImports, "edu.wpi.first.wpilibj2.command.");
    assertEquals(
        void.class,
        FlywheelTelemetryFacade.class
            .getDeclaredMethod("publish", FlywheelObservation.class)
            .getReturnType());
    assertTrue(
        Arrays.stream(RobotTelemetry.class.getDeclaredMethods())
            .map(Method::getName)
            .noneMatch(name -> name.equals("requestSpin") || name.equals("stop")));
  }

  @Test
  void publicFlywheelSurfacesExposeOnlyFoundationOperations() {
    assertEquals(
        Set.of("updateInputs", "requestVelocity", "stop"),
        declaredPublicInstanceMethodNames(FlywheelIONoop.class));
    assertEquals(
        Set.of("getObservation", "periodic", "requestVelocity", "stop"),
        declaredPublicInstanceMethodNames(FlywheelSubsystem.class));
    assertEquals(
        Set.of("close", "publish"),
        declaredPublicInstanceMethodNames(FlywheelTelemetryFacade.class));
  }

  @Test
  void flywheelScopeContainsOnlyNoopIoNoCommandsAndNoHardwareAssignment() throws IOException {
    Path flywheelIoDirectory =
        Path.of("src", "main", "java", "frc", "robot", "io", "flywheel");
    try (Stream<Path> flywheelFiles = Files.list(flywheelIoDirectory)) {
      assertEquals(
          Set.of("FlywheelIO.java", "FlywheelIONoop.java"),
          flywheelFiles.map(path -> path.getFileName().toString()).collect(Collectors.toSet()));
    }
    assertFalse(Files.exists(flywheelIoDirectory.resolve("FlywheelIOSim.java")));
    assertFalse(Files.exists(flywheelIoDirectory.resolve("FlywheelIOCTRE.java")));
    assertFalse(Files.exists(flywheelIoDirectory.resolve("FlywheelIOReal.java")));
    try (Stream<Path> commandFiles = Files.list(Path.of("src", "main", "java", "frc", "robot", "commands"))) {
      assertTrue(
          commandFiles
              .map(path -> path.getFileName().toString())
              .noneMatch(name -> name.contains("Flywheel")));
    }
    assertEquals(
        Set.of("kReadyAtSpeedToleranceRpm"),
        Arrays.stream(Constants.FlywheelConstants.class.getDeclaredFields())
            .filter(field -> !field.isSynthetic() && Modifier.isStatic(field.getModifiers()))
            .map(Field::getName)
            .collect(Collectors.toSet()));
    assertEquals(50.0, Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm);
  }

  @Test
  void flywheelReadinessHasNoSeparateEvaluatorOrRobotContainerBehavior() throws IOException {
    Path productionRoot = Path.of("src", "main", "java");
    try (Stream<Path> productionFiles = Files.walk(productionRoot)) {
      assertTrue(
          productionFiles
              .filter(Files::isRegularFile)
              .filter(path -> path.toString().endsWith(".java"))
              .filter(path -> !path.endsWith(Path.of("frc", "robot", "subsystems", "FlywheelSubsystem.java")))
              .noneMatch(FlywheelArchitectureBoundaryTest::containsUnauthorizedFlywheelReadinessType));
    }

    String robotContainerSource =
        withoutCommentsAndLiterals(
            Files.readString(Path.of("src", "main", "java", "frc", "robot", "RobotContainer.java")));
    assertTrue(
        Pattern.compile(
                "new\\s+FlywheelSubsystem\\s*\\(\\s*new\\s+FlywheelIONoop\\s*\\(\\s*\\)\\s*\\)")
            .matcher(robotContainerSource)
            .find());
    assertFalse(
        Pattern.compile(
                "\\bflywheelSubsystem\\s*\\.\\s*(?:requestVelocity|stop|setDefaultCommand)\\s*\\(")
            .matcher(robotContainerSource)
            .find());
    assertFalse(robotContainerSource.toLowerCase(Locale.ROOT).contains("readyatspeed"));
    assertFalse(
        Pattern.compile(
                "(?s)\\b(?:whileTrue|whileFalse|onTrue|onFalse|toggleOnTrue|toggleOnFalse)\\s*\\([^;]*\\bflywheel\\b")
            .matcher(robotContainerSource)
            .find());
  }

  private static Set<String> declaredPublicInstanceMethodNames(Class<?> type) {
    return Arrays.stream(type.getDeclaredMethods())
        .filter(method -> Modifier.isPublic(method.getModifiers()))
        .filter(method -> !Modifier.isStatic(method.getModifiers()))
        .filter(method -> !method.isSynthetic())
        .map(Method::getName)
        .collect(Collectors.toSet());
  }

  private static boolean containsUnauthorizedFlywheelReadinessType(Path sourcePath) {
    String source;
    try {
      source = withoutCommentsAndLiterals(Files.readString(sourcePath));
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read production source " + sourcePath, exception);
    }
    Matcher matcher = TYPE_DECLARATION.matcher(source);
    while (matcher.find()) {
      String typeName = matcher.group(1).toLowerCase(Locale.ROOT);
      boolean flywheelType = typeName.contains("flywheel");
      boolean readinessType =
          typeName.contains("readyatspeed")
              || typeName.contains("readiness")
              || typeName.contains("evaluator")
              || typeName.contains("helper");
      if (flywheelType && readinessType) {
        return true;
      }
    }
    return false;
  }

  private static void assertNoImportBeginningWith(Set<String> imports, String prefix) {
    assertFalse(imports.stream().anyMatch(name -> name.startsWith(prefix)), prefix);
  }

  private static Set<String> importDeclarations(String relativePath) throws IOException {
    String commentFreeSource =
        withoutCommentsAndLiterals(Files.readString(Path.of("src", "main", "java").resolve(relativePath)));
    Matcher matcher = IMPORT_DECLARATION.matcher(commentFreeSource);
    Set<String> imports = new HashSet<>();
    while (matcher.find()) {
      imports.add(matcher.group(1).replaceAll("\\s+", ""));
    }
    return imports;
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
}
