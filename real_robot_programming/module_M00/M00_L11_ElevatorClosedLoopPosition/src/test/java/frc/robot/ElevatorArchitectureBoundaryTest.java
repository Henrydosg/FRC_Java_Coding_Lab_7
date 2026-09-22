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
import frc.robot.observation.elevator.ElevatorRequestedState;
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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Locks M00_L11 Elevator request ownership to its approved vendor-neutral boundary. */
class ElevatorArchitectureBoundaryTest {
  private static final Set<String> AUTHORIZED_ELEVATOR_REFERENCE_FILES =
      Set.of(
          "frc/robot/io/elevator/ElevatorIO.java",
          "frc/robot/io/elevator/ElevatorIONoop.java",
          "frc/robot/observation/elevator/ElevatorObservation.java",
          "frc/robot/observation/elevator/ElevatorRequestedState.java",
          "frc/robot/subsystems/ElevatorSubsystem.java",
          "frc/robot/telemetry/elevator/ElevatorTelemetryFacade.java",
          "frc/robot/RobotContainer.java",
          "frc/robot/telemetry/RobotTelemetry.java");
  private static final Pattern ELEVATOR_PRODUCTION_REFERENCE =
      Pattern.compile(
          "\\b(?:ElevatorSubsystem|ElevatorIO|ElevatorIONoop|ElevatorObservation|"
              + "ElevatorRequestedState|ElevatorTelemetryFacade|frc\\.robot\\.(?:io\\.elevator|"
              + "observation\\.elevator|telemetry\\.elevator))\\b");
  private static final Pattern FORBIDDEN_ELEVATOR_API =
      Pattern.compile(
          "\\b(?:setVoltage|setPercent|setDutyCycle|PID|PIDController|"
              + "ProfiledPIDController|PIDF|SimpleMotorFeedforward|ElevatorFeedforward|"
              + "feedforward|MotionMagic|limitSwitch|forwardLimit|reverseLimit|softLimit|"
              + "hardLimit|minimumTravel|maximumTravel|minTravel|maxTravel|topStop|bottomStop|"
              + "travelLimit|travelRange|heightLimit|minimumPosition|maximumPosition|minPosition|"
              + "maxPosition|clamp|home|homing|zeroing|establishReference|setReference|"
              + "ElevatorController|PositionController|MotionController)\\b",
          Pattern.CASE_INSENSITIVE);
  private static final Path PRODUCTION_ROOT = Path.of("src", "main", "java", "frc", "robot");
  private static final Path ELEVATOR_IO_DIRECTORY = PRODUCTION_ROOT.resolve("io/elevator");

  @Test
  void elevatorReferencesExistOnlyInApprovedProductionLocations() throws IOException {
    Path productionRoot = Path.of("src", "main", "java").toAbsolutePath().normalize();
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
  void elevatorIoAndRequestedStateExposeExactContracts() throws ReflectiveOperationException {
    Set<String> fieldNames =
        Arrays.stream(ElevatorIOInputs.class.getDeclaredFields())
            .filter(field -> !field.isSynthetic() && !Modifier.isStatic(field.getModifiers()))
            .map(Field::getName)
            .collect(Collectors.toSet());
    assertEquals(
        Set.of("available", "connected", "positionValid", "positionReferenced", "positionMeters"),
        fieldNames);
    assertEquals(
        Set.of("updateInputs", "requestPositionMeters", "stop"),
        Arrays.stream(ElevatorIO.class.getDeclaredMethods())
            .filter(method -> !method.isSynthetic())
            .map(Method::getName)
            .collect(Collectors.toSet()));
    assertEquals(
        void.class,
        ElevatorIO.class.getDeclaredMethod("updateInputs", ElevatorIOInputs.class).getReturnType());
    assertEquals(
        void.class,
        ElevatorIO.class.getDeclaredMethod("requestPositionMeters", double.class).getReturnType());
    assertEquals(void.class, ElevatorIO.class.getDeclaredMethod("stop").getReturnType());
    assertEquals(
        List.of("STOPPED", "POSITION_REQUESTED"),
        Arrays.stream(ElevatorRequestedState.values()).map(Enum::name).toList());
  }

  @Test
  void observationAndPublicSurfacesExposeExactReadOnlyShape() {
    assertEquals(
        List.of(
            "available",
            "connected",
            "positionValid",
            "positionReferenced",
            "positionMeters",
            "requestedState",
            "targetPositionMeters",
            "positionErrorMeters"),
        Arrays.stream(ElevatorObservation.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    assertEquals(
        List.of(
            boolean.class,
            boolean.class,
            boolean.class,
            boolean.class,
            double.class,
            ElevatorRequestedState.class,
            double.class,
            double.class),
        Arrays.stream(ElevatorObservation.class.getRecordComponents())
            .map(component -> component.getType())
            .toList());
    assertEquals(
        Set.of("getObservation", "periodic", "requestPositionMeters", "stop"),
        declaredPublicInstanceMethodNames(ElevatorSubsystem.class));
    assertEquals(
        Set.of("close", "publish"), declaredPublicInstanceMethodNames(ElevatorTelemetryFacade.class));
  }

  @Test
  void elevatorScopeContainsOnlyNoopIoAndNoHardwareOrCommands() throws IOException {
    try (Stream<Path> files = Files.list(ELEVATOR_IO_DIRECTORY)) {
      assertEquals(
          Set.of("ElevatorIO.java", "ElevatorIONoop.java"),
          files.map(path -> path.getFileName().toString()).collect(Collectors.toSet()));
    }
    assertFalse(Files.exists(ELEVATOR_IO_DIRECTORY.resolve("ElevatorIOSim.java")));
    assertFalse(Files.exists(ELEVATOR_IO_DIRECTORY.resolve("ElevatorIOCTRE.java")));
    assertFalse(Files.exists(ELEVATOR_IO_DIRECTORY.resolve("ElevatorIOREV.java")));
    try (Stream<Path> commandFiles = Files.list(PRODUCTION_ROOT.resolve("commands"))) {
      assertTrue(
          commandFiles
              .map(path -> path.getFileName().toString())
              .noneMatch(name -> name.toLowerCase().contains("elevator")));
    }
    assertFalse(
        read(PRODUCTION_ROOT.resolve("Constants.java")).toLowerCase().contains("elevator"));
  }

  @Test
  void elevatorProductionContainsOnlyApprovedRequestAndMeasurementBehavior() throws IOException {
    try (Stream<Path> files = Files.walk(PRODUCTION_ROOT)) {
      files
          .filter(Files::isRegularFile)
          .filter(path -> path.toString().endsWith(".java"))
          .filter(path -> path.toString().toLowerCase().contains("elevator"))
          .forEach(
              path ->
                  assertFalse(
                      FORBIDDEN_ELEVATOR_API.matcher(withoutCommentsAndLiterals(read(path))).find(),
                      path + " contains prohibited control, homing, or limit API"));
    }
  }

  @Test
  void robotContainerHasNoElevatorCommandBindingOrAutonomousIntegration() throws IOException {
    String source = withoutCommentsAndLiterals(read(PRODUCTION_ROOT.resolve("RobotContainer.java")));
    assertTrue(source.contains("new ElevatorSubsystem(new ElevatorIONoop())"));
    assertFalse(source.matches("(?s).*\\b(?:requestPositionMeters|stop)\\s*\\([^;]*elevatorSubsystem.*"));
    assertFalse(source.matches("(?s).*\\b(?:whileTrue|whileFalse|onTrue|onFalse|toggleOnTrue|toggleOnFalse|setDefaultCommand|NamedCommands|PathPlanner|getAutonomousCommand)\\b[^;]*elevator.*"));
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
      throw new AssertionError("Unable to read " + path, exception);
    }
  }

  static String withoutCommentsAndLiterals(String source) {
    return source
        .replaceAll("(?s)/\\*.*?\\*/", " ")
        .replaceAll("(?m)//.*$", " ")
        .replaceAll("\\\"(?:\\\\.|[^\\\"\\\\])*\\\"", "\\\"\\\"")
        .replaceAll("'(?:\\\\.|[^'\\\\])*'", "''");
  }
}
