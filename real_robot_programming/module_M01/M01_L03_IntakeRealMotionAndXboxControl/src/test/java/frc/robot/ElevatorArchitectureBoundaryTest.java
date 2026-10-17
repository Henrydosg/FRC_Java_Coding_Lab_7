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

import frc.robot.commands.HomeElevatorCommand;
import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIO.ElevatorIOInputs;
import frc.robot.observation.elevator.ElevatorObservation;
import frc.robot.observation.elevator.ElevatorRequestedState;
import frc.robot.subsystems.ElevatorTravelLimits;
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
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Locks inherited Elevator contracts and M00_L13 travel-limit boundaries. */
class ElevatorArchitectureBoundaryTest {
  private static final Set<String> AUTHORIZED_ELEVATOR_REFERENCE_FILES =
      Set.of(
          "frc/robot/io/elevator/ElevatorIO.java",
          "frc/robot/io/elevator/ElevatorIONoop.java",
          "frc/robot/observation/elevator/ElevatorObservation.java",
          "frc/robot/observation/elevator/ElevatorRequestedState.java",
          "frc/robot/subsystems/ElevatorTravelLimits.java",
          "frc/robot/subsystems/ElevatorSubsystem.java",
          "frc/robot/commands/HomeElevatorCommand.java",
          "frc/robot/telemetry/elevator/ElevatorTelemetryFacade.java",
          "frc/robot/RobotContainer.java",
          "frc/robot/telemetry/RobotTelemetry.java");
  private static final Pattern ELEVATOR_PRODUCTION_REFERENCE =
      Pattern.compile(
          "\\b(?:ElevatorSubsystem|ElevatorTravelLimits|ElevatorIO|ElevatorIONoop|"
              + "ElevatorObservation|"
              + "ElevatorRequestedState|ElevatorTelemetryFacade|frc\\.robot\\.(?:io\\.elevator|"
              + "observation\\.elevator|telemetry\\.elevator))\\b");
  private static final Pattern VENDOR_API_PACKAGE_ROOT =
      Pattern.compile("\\b(?:com\\.ctre|com\\.revrobotics)\\.");
  private static final Pattern ELEVATOR_IO_IMPLEMENTATION =
      Pattern.compile("\\bimplements\\s+ElevatorIO\\b");
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
  private static final Pattern FORBIDDEN_HOME_IMPLEMENTATION_IDENTIFIER =
      Pattern.compile("\\b(?:homing|home)[A-Za-z0-9_$]+\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern JAVA_IDENTIFIER = Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*");
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
                  if (!isConcreteElevatorIoImplementation(relativePath, source)) {
                    assertFalse(
                        VENDOR_API_PACKAGE_ROOT.matcher(source).find(),
                        "Vendor API package reference outside a concrete ElevatorIO implementation: "
                            + relativePath);
                  }
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
    Method[] ioMethods = ElevatorIO.class.getDeclaredMethods();
    assertEquals(4, ioMethods.length);
    assertEquals(
        Set.of(
            new MethodSignature("updateInputs", void.class, List.of(ElevatorIOInputs.class)),
            new MethodSignature("requestPositionMeters", void.class, List.of(double.class)),
            new MethodSignature("requestHoming", void.class, List.of()),
            new MethodSignature("stop", void.class, List.of())),
        Arrays.stream(ioMethods)
            .map(ElevatorArchitectureBoundaryTest::signatureOf)
            .collect(Collectors.toSet()));
    assertEquals(
        List.of("STOPPED", "POSITION_REQUESTED", "HOMING"),
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
        Set.of("getObservation", "periodic", "requestPositionMeters", "requestHoming", "stop"),
        declaredPublicInstanceMethodNames(ElevatorSubsystem.class));
    assertEquals(
        Set.of("close", "publish"), declaredPublicInstanceMethodNames(ElevatorTelemetryFacade.class));
  }

  @Test
  void travelLimitsRemainAnImmutableVendorNeutralLogicalMeterRecord() {
    assertTrue(ElevatorTravelLimits.class.isRecord());
    assertTrue(Modifier.isFinal(ElevatorTravelLimits.class.getModifiers()));
    assertEquals(
        List.of("minPositionMeters", "maxPositionMeters"),
        Arrays.stream(ElevatorTravelLimits.class.getRecordComponents())
            .map(component -> component.getName())
            .toList());
    assertEquals(
        List.of(double.class, double.class),
        Arrays.stream(ElevatorTravelLimits.class.getRecordComponents())
            .map(component -> component.getType())
            .toList());
  }

  @Test
  void hardwareLimitIdentifierGuardRejectsHardwareNamesAndAllowsSoftwareEnvelopeNames() {
    for (String identifier :
        List.of(
            "limitSwitch",
            "lowerLimitSwitch",
            "upperLimitSwitch",
            "forwardLimit",
            "reverseLimit",
            "hardwareSoftLimit",
            "softLimitThreshold")) {
      assertTrue(
          containsForbiddenHardwareLimitIdentifier(identifier),
          "Expected hardware-limit identifier to be rejected: " + identifier);
    }

    for (String identifier :
        List.of(
            "ElevatorTravelLimits",
            "travelLimits",
            "minPositionMeters",
            "maxPositionMeters",
            "limits",
            "travel",
            "limit")) {
      assertFalse(
          containsForbiddenHardwareLimitIdentifier(identifier),
          "Expected software-envelope identifier to remain allowed: " + identifier);
    }
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
      assertEquals(
          Set.of("HomeElevatorCommand.java"),
          commandFiles
              .map(path -> path.getFileName().toString())
              .filter(name -> name.toLowerCase().contains("elevator"))
              .collect(Collectors.toSet()));
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
              path -> {
                String source = withoutCommentsAndLiterals(read(path));
                String normalizedPath = path.toString().replace('\\', '/');
                if (normalizedPath.endsWith("/commands/HomeElevatorCommand.java")
                    || normalizedPath.endsWith("/subsystems/ElevatorSubsystem.java")
                    || normalizedPath.endsWith("/io/elevator/ElevatorIO.java")
                    || normalizedPath.endsWith("/io/elevator/ElevatorIONoop.java")) {
                  source = source.replaceAll("\\brequestHoming\\b", "referenceRequest");
                }
                if (normalizedPath.endsWith("/subsystems/ElevatorSubsystem.java")) {
                  source =
                      source.replace(
                          "ElevatorRequestedState.HOMING",
                          "ElevatorRequestedState.REFERENCE_REQUESTED");
                }
                if (normalizedPath.endsWith("/observation/elevator/ElevatorRequestedState.java")) {
                  source = source.replaceAll("\\bHOMING\\b", "REFERENCE_REQUESTED");
                }
                boolean homeImplementationLayer =
                    normalizedPath.endsWith("/subsystems/ElevatorSubsystem.java")
                        || normalizedPath.endsWith("/commands/HomeElevatorCommand.java");
                if (normalizedPath.endsWith("/commands/HomeElevatorCommand.java")) {
                  source =
                      source.replaceAll("\\bHomeElevatorCommand\\b", "ElevatorLifecycleCommand");
                }
                if (homeImplementationLayer) {
                  assertFalse(
                      FORBIDDEN_HOME_IMPLEMENTATION_IDENTIFIER.matcher(source).find(),
                      path + " contains hardware-specific home or homing identifier");
                }
                assertFalse(
                    FORBIDDEN_ELEVATOR_API.matcher(source).find(),
                    path + " contains prohibited control or travel-limit API");
                assertFalse(
                    containsForbiddenHardwareLimitIdentifier(source),
                    path + " contains a prohibited hardware-limit identifier");
              });
    }
  }

  @Test
  void homingCommandDependsOnlyOnElevatorSubsystemSemantics() throws IOException {
    String source =
        withoutCommentsAndLiterals(
            read(PRODUCTION_ROOT.resolve("commands/HomeElevatorCommand.java")));
    assertFalse(source.contains("ElevatorIO"));
    assertFalse(source.contains("frc.robot.telemetry"));
    assertFalse(source.contains("NetworkTable"));
    assertFalse(source.contains("TalonFX"));
    assertFalse(source.contains("SparkMax"));
    assertFalse(source.contains("new ElevatorIO"));
    assertFalse(
        Arrays.stream(HomeElevatorCommand.class.getDeclaredFields())
            .anyMatch(field -> ElevatorIO.class.isAssignableFrom(field.getType())));
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

  private static boolean isConcreteElevatorIoImplementation(String relativePath, String source) {
    return relativePath.startsWith("frc/robot/io/elevator/")
        && !relativePath.equals("frc/robot/io/elevator/ElevatorIO.java")
        && ELEVATOR_IO_IMPLEMENTATION.matcher(source).find();
  }

  private static boolean containsForbiddenHardwareLimitIdentifier(String source) {
    var matcher = JAVA_IDENTIFIER.matcher(source);
    while (matcher.find()) {
      String identifier = matcher.group().toLowerCase(Locale.ROOT);
      if (identifier.contains("limitswitch")
          || identifier.contains("softlimit")
          || identifier.contains("forwardlimit")
          || identifier.contains("reverselimit")) {
        return true;
      }
    }
    return false;
  }

  private static MethodSignature signatureOf(Method method) {
    return new MethodSignature(
        method.getName(),
        method.getReturnType(),
        Arrays.stream(method.getParameterTypes()).toList());
  }

  private record MethodSignature(String name, Class<?> returnType, List<Class<?>> parameterTypes) {}

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
