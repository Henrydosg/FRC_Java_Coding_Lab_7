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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Locks vendor isolation, safe-stop ordering, the M00_L04 ownership boundary, and the M01_L02
 * zero-motion Real IO boundary.
 */
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
  void runIntakeCommandDependsOnlyOnTheSubsystemBoundary() throws IOException {
    String command = source("frc/robot/commands/RunIntakeCommand.java");
    String lowerCommand = command.toLowerCase(Locale.ROOT);

    assertTrue(command.contains("IntakeSubsystem"));
    assertTrue(command.contains("addRequirements(intakeSubsystem)"));
    assertFalse(command.contains("frc.robot.io.intake"));
    assertFalse(command.contains("IntakeIO"));
    assertFalse(command.contains("NetworkTable"));
    assertFalse(lowerCommand.contains("telemetry"));
    assertFalse(lowerCommand.contains("com.ctre"));
    assertFalse(lowerCommand.contains("com.revrobotics"));
    assertFalse(command.contains("new IntakeSubsystem"));
    assertFalse(command.contains("RobotContainer"));
    assertFalse(command.contains("Constants"));
  }

  @Test
  void robotContainerKeepsTeleopIntakeAndAllowsOneAutonomousEventIntegration()
      throws IOException {
    String robotContainer = source("frc/robot/RobotContainer.java");
    String code = withoutCommentsAndLiterals(robotContainer);
    assertTrue(robotContainer.contains("new IntakeIONoop()"));
    assertTrue(robotContainer.contains("new RunIntakeCommand(intakeSubsystem)"));
    assertTrue(
        robotContainer.contains("driverController.rightBumper().whileTrue(runIntakeCommand)"));
    assertEquals(
        1L,
        robotContainer.lines()
            .filter(
                line ->
                    line.contains(
                        "driverController.rightBumper().whileTrue(runIntakeCommand)"))
            .count());
    assertTrue(code.contains("driverController.leftBumper().whileTrue(runFeederCommand)"));
    assertTrue(
        code.contains(
            "new JoystickButton(driverController.getHID(), XboxController.Button.kBack.value)"));
    assertTrue(code.contains(".onTrue(prepareAutonomousCommand)"));

    Pattern authorizedEventBinding =
        Pattern.compile(
            "autonomousEventRegistration\\.register\\s*\\(\\s*"
                + "new\\s+AutonomousEventBinding\\s*\\(\\s*"
                + "AutonomousEventId\\.LEARNING_EVENT\\s*,\\s*"
                + "\\(\\s*\\)\\s*->\\s*new\\s+IntakeToFeederCommand\\s*"
                + "\\(\\s*intakeSubsystem\\s*,\\s*feederSubsystem\\s*\\)\\s*,\\s*"
                + "Set\\.of\\s*\\(\\s*intakeSubsystem\\s*,\\s*feederSubsystem\\s*\\)"
                + "\\s*\\)\\s*\\);");
    Matcher eventBinding = authorizedEventBinding.matcher(code);
    assertTrue(eventBinding.find());
    String outsideEventBinding =
        eventBinding
            .replaceFirst("")
            .replace("import frc.robot.commands.IntakeToFeederCommand;", "");
    assertFalse(outsideEventBinding.contains("IntakeToFeederCommand"));
    assertFalse(outsideEventBinding.contains("AutonomousEventId.LEARNING_EVENT"));
    assertFalse(code.contains("NamedCommands"));
    assertFalse(code.contains("CommandScheduler"));
    assertFalse(Pattern.compile("\\.schedule\\s*\\(").matcher(code).find());
    Pattern triggerBinding =
        Pattern.compile(
            "\\.(?:whileTrue|whileFalse|onTrue|onFalse|"
                + "toggleOnTrue|toggleOnFalse|onChange)\\s*\\(");
    assertEquals(3L, triggerBinding.matcher(code).results().count());
    assertTrue(code.contains("swerveSubsystem.setDefaultCommand(fieldRelativeTeleopDriveCommand)"));
    assertEquals(
        1L, Pattern.compile("\\.setDefaultCommand\\s*\\(").matcher(code).results().count());
    assertFalse(robotContainer.contains("intakeSubsystem.setDefaultCommand"));
    assertFalse(robotContainer.contains("intakeSubsystem.requestIntake"));
    assertFalse(robotContainer.contains("intakeSubsystem.stop"));

    Path commands = Path.of("src", "main", "java", "frc", "robot", "commands");
    Set<String> intakeNamedCommandFiles;
    try (Stream<Path> commandFiles = Files.walk(commands)) {
      intakeNamedCommandFiles =
          commandFiles
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().endsWith(".java"))
              .filter(path -> path.getFileName().toString().contains("Intake"))
              .map(path -> normalizedRelativePath(commands, path))
              .collect(Collectors.toSet());
    }
    assertEquals(
        Set.of("RunIntakeCommand.java", "IntakeToFeederCommand.java"), intakeNamedCommandFiles);
  }

  @Test
  void vendorImportsAreConfinedToIntakeIOReal() throws IOException {
    Path intakeIo = Path.of("src", "main", "java", "frc", "robot", "io", "intake");
    Set<String> vendorImportingFiles;
    try (Stream<Path> intakeFiles = Files.walk(intakeIo)) {
      vendorImportingFiles =
          intakeFiles
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().endsWith(".java"))
              .filter(path -> readUnchecked(path).contains("import com.ctre"))
              .map(path -> normalizedRelativePath(intakeIo, path))
              .collect(Collectors.toSet());
    }
    assertEquals(Set.of("IntakeIOReal.java"), vendorImportingFiles);
    assertFalse(
        source("frc/robot/io/intake/IntakeIOReal.java")
            .toLowerCase(Locale.ROOT)
            .contains("import com.revrobotics"));
  }

  @Test
  void intakeIORealIssuesNeutralOutputOnly() throws IOException {
    String code = withoutCommentsAndLiterals(source("frc/robot/io/intake/IntakeIOReal.java"));

    Set<String> controlImports =
        Pattern.compile("import\\s+com\\.ctre\\.phoenix6\\.controls\\.(\\w+)\\s*;")
            .matcher(code)
            .results()
            .map(result -> result.group(1))
            .collect(Collectors.toSet());
    assertEquals(Set.of("NeutralOut"), controlImports);

    long setControlCalls = Pattern.compile("\\.setControl\\s*\\(").matcher(code).results().count();
    long neutralSetControlCalls =
        Pattern.compile("\\.setControl\\s*\\(\\s*neutralRequest\\s*\\)")
            .matcher(code)
            .results()
            .count();
    assertEquals(2L, setControlCalls);
    assertEquals(setControlCalls, neutralSetControlCalls);

    assertFalse(Pattern.compile("\\.set\\s*\\(").matcher(code).find());
    assertFalse(Pattern.compile("\\.setVoltage\\s*\\(").matcher(code).find());
    assertFalse(code.contains("phoenix6.controls.*"));
  }

  @Test
  void robotContainerSelectsRealIntakeOnlyOnTheRealRobot() throws IOException {
    String code = withoutCommentsAndLiterals(source("frc/robot/RobotContainer.java"));

    assertTrue(
        Pattern.compile(
                "new\\s+IntakeSubsystem\\s*\\(\\s*RobotBase\\.isReal\\s*\\(\\s*\\)\\s*\\?\\s*"
                    + "new\\s+IntakeIOReal\\s*\\(\\s*\\)\\s*:\\s*"
                    + "new\\s+IntakeIONoop\\s*\\(\\s*\\)\\s*\\)")
            .matcher(code)
            .find());
    assertEquals(
        1L, Pattern.compile("new\\s+IntakeIOReal\\s*\\(").matcher(code).results().count());
    assertEquals(
        1L, Pattern.compile("new\\s+IntakeSubsystem\\s*\\(").matcher(code).results().count());
  }

  private static String readUnchecked(Path path) {
    try {
      return Files.readString(path);
    } catch (IOException exception) {
      throw new java.io.UncheckedIOException(exception);
    }
  }

  private static String normalizedRelativePath(Path root, Path path) {
    return root.relativize(path).toString().replace('\\', '/');
  }

  private static String withoutCommentsAndLiterals(String source) {
    return source
        .replaceAll("(?s)/\\*.*?\\*/", " ")
        .replaceAll("(?m)//.*$", " ")
        .replaceAll("\"(?:\\\\.|[^\"\\\\])*\"", "\"\"")
        .replaceAll("'(?:\\\\.|[^'\\\\])*'", "''");
  }

  private static String source(String relativePath) throws IOException {
    return Files.readString(Path.of("src", "main", "java").resolve(relativePath));
  }
}
