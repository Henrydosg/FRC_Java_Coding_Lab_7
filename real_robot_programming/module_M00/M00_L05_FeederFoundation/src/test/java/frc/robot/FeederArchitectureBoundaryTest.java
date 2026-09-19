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

import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Locks vendor isolation, read-only telemetry, and the M00_L05 Feeder boundary. */
class FeederArchitectureBoundaryTest {
  @Test
  void feederFoundationContainsNoVendorImports() throws IOException {
    for (String source :
        new String[] {
          source("frc/robot/io/feeder/FeederIO.java"),
          source("frc/robot/io/feeder/FeederIONoop.java"),
          source("frc/robot/subsystems/FeederSubsystem.java"),
          source("frc/robot/observation/feeder/FeederObservation.java"),
          source("frc/robot/telemetry/feeder/FeederTelemetryFacade.java")
        }) {
      String lowerSource = source.toLowerCase(Locale.ROOT);
      assertFalse(lowerSource.contains("import com.ctre"));
      assertFalse(lowerSource.contains("import com.revrobotics"));
    }
  }

  @Test
  void feederIoInputsContainOnlyAvailabilityAndConnectionFacts() {
    Set<String> semanticFieldNames = new HashSet<>();
    for (Field field : FeederIOInputs.class.getDeclaredFields()) {
      if (!field.isSynthetic() && !Modifier.isStatic(field.getModifiers())) {
        assertEquals(boolean.class, field.getType(), field.getName());
        semanticFieldNames.add(field.getName());
      }
    }

    assertEquals(Set.of("available", "connected"), semanticFieldNames);
  }

  @Test
  void observationAndTelemetryPreserveReadOnlyDependencies() throws IOException {
    String observation = source("frc/robot/observation/feeder/FeederObservation.java");
    assertFalse(observation.contains("frc.robot.io"));
    assertFalse(observation.contains("frc.robot.subsystems"));
    assertFalse(observation.contains("networktables"));
    assertFalse(observation.contains("wpilibj2.command"));
    assertFalse(observation.contains("RobotContainer"));

    String telemetry = source("frc/robot/telemetry/feeder/FeederTelemetryFacade.java");
    assertTrue(telemetry.contains("FeederObservation"));
    assertFalse(telemetry.contains("frc.robot.io.feeder"));
    assertFalse(telemetry.contains("frc.robot.subsystems"));
    assertFalse(telemetry.contains("wpilibj2.command"));
    assertFalse(telemetry.contains("requestFeed"));
    assertFalse(telemetry.contains("stop("));
    assertFalse(telemetry.contains("schedule("));
  }

  @Test
  void stopRecordsStoppedIntentBeforeForwardingToIo() throws IOException {
    String subsystem = source("frc/robot/subsystems/FeederSubsystem.java");
    int stopMethod = subsystem.indexOf("public void stop()");
    int stoppedAssignment = subsystem.indexOf("requestedState = RequestedState.STOPPED", stopMethod);
    int observationUpdate = subsystem.indexOf("updateObservation();", stoppedAssignment);
    int ioStop = subsystem.indexOf("feederIO.stop()", stopMethod);

    assertTrue(stopMethod >= 0);
    assertTrue(stoppedAssignment > stopMethod);
    assertTrue(observationUpdate > stoppedAssignment);
    assertTrue(ioStop > observationUpdate);
  }

  @Test
  void robotContainerRemainsCompositionOnlyWithoutFeederOwnershipBindings() throws IOException {
    String robotContainer = source("frc/robot/RobotContainer.java");

    assertTrue(robotContainer.contains("new FeederIONoop()"));
    assertTrue(robotContainer.contains("new FeederSubsystem(new FeederIONoop())"));
    assertFalse(robotContainer.contains("RunFeederCommand"));
    assertFalse(robotContainer.contains("feederSubsystem.setDefaultCommand"));
    assertFalse(robotContainer.contains("feederSubsystem.requestFeed"));
    assertFalse(robotContainer.contains("feederSubsystem.stop"));
  }

  @Test
  void feederScopeContainsOnlyNoopIoAndNoHardwareAssignment() throws IOException {
    Path feederIoDirectory =
        Path.of("src", "main", "java", "frc", "robot", "io", "feeder");
    try (Stream<Path> feederFiles = Files.list(feederIoDirectory)) {
      assertEquals(
          Set.of("FeederIO.java", "FeederIONoop.java"),
          feederFiles.map(path -> path.getFileName().toString()).collect(Collectors.toSet()));
    }
    assertFalse(Files.exists(feederIoDirectory.resolve("FeederIOSim.java")));

    Path commands = Path.of("src", "main", "java", "frc", "robot", "commands");
    try (Stream<Path> commandFiles = Files.walk(commands)) {
      assertTrue(
          commandFiles
              .filter(Files::isRegularFile)
              .noneMatch(path -> path.getFileName().toString().contains("Feeder")));
    }

    assertFalse(source("frc/robot/Constants.java").contains("Feeder"));
  }

  private static String source(String relativePath) throws IOException {
    return Files.readString(Path.of("src", "main", "java").resolve(relativePath));
  }
}
