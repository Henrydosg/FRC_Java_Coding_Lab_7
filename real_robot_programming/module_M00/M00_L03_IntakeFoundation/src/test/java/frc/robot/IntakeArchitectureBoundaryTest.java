// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/** Locks vendor isolation, safe-stop ordering, and the M00_L04 ownership boundary. */
class IntakeArchitectureBoundaryTest {
  @Test
  void intakeFoundationContainsNoVendorImports() throws IOException {
    for (String source :
        new String[] {
          source("frc/robot/io/intake/IntakeIO.java"),
          source("frc/robot/io/intake/IntakeIONoop.java"),
          source("frc/robot/subsystems/IntakeSubsystem.java"),
          source("frc/robot/observation/intake/IntakeObservation.java"),
          source("frc/robot/telemetry/intake/IntakeTelemetryFacade.java")
        }) {
      String lowerSource = source.toLowerCase(Locale.ROOT);
      assertFalse(lowerSource.contains("import com.ctre"));
      assertFalse(lowerSource.contains("import com.revrobotics"));
    }
  }

  @Test
  void observationAndTelemetryPreserveReadOnlyDependencies() throws IOException {
    String observation = source("frc/robot/observation/intake/IntakeObservation.java");
    assertFalse(observation.contains("frc.robot.io"));
    assertFalse(observation.contains("frc.robot.subsystems"));
    assertFalse(observation.contains("networktables"));
    assertFalse(observation.contains("wpilibj2.command"));
    assertFalse(observation.contains("RobotContainer"));

    String telemetry = source("frc/robot/telemetry/intake/IntakeTelemetryFacade.java");
    assertTrue(telemetry.contains("IntakeObservation"));
    assertFalse(telemetry.contains("frc.robot.io.intake"));
    assertFalse(telemetry.contains("frc.robot.subsystems"));
    assertFalse(telemetry.contains("wpilibj2.command"));
    assertFalse(telemetry.contains("schedule("));
  }

  @Test
  void stopRecordsStoppedIntentBeforeForwardingToIo() throws IOException {
    String subsystem = source("frc/robot/subsystems/IntakeSubsystem.java");
    int stopMethod = subsystem.indexOf("public void stop()");
    int stoppedAssignment = subsystem.indexOf("requestedState = RequestedState.STOPPED", stopMethod);
    int ioStop = subsystem.indexOf("intakeIO.stop()", stopMethod);

    assertTrue(stopMethod >= 0);
    assertTrue(stoppedAssignment > stopMethod);
    assertTrue(ioStop > stoppedAssignment);
  }

  @Test
  void robotContainerAddsNoIntakeCommandOrBinding() throws IOException {
    String robotContainer = source("frc/robot/RobotContainer.java");
    assertTrue(robotContainer.contains("new IntakeIONoop()"));
    assertFalse(robotContainer.contains("IntakeCommand"));
    assertFalse(robotContainer.contains("intakeSubsystem.setDefaultCommand"));
    assertFalse(robotContainer.contains("intakeSubsystem.requestIntake"));
    assertFalse(robotContainer.contains("intakeSubsystem.stop"));

    Path commands = Path.of("src", "main", "java", "frc", "robot", "commands");
    try (Stream<Path> commandFiles = Files.walk(commands)) {
      assertTrue(
          commandFiles
              .filter(Files::isRegularFile)
              .noneMatch(path -> path.getFileName().toString().contains("Intake")));
    }
  }

  private static String source(String relativePath) throws IOException {
    return Files.readString(Path.of("src", "main", "java").resolve(relativePath));
  }
}
