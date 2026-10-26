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
    assertEquals(4L, triggerBinding.matcher(code).results().count());
    assertTrue(
        code.contains(
            "RunIntakeCommand reverseIntakeCommand = RunIntakeCommand.reverse(intakeSubsystem)"));
    assertTrue(code.contains("driverController.b().whileTrue(reverseIntakeCommand)"));
    assertTrue(
        code.indexOf("driverController.rightBumper().whileTrue(runIntakeCommand)")
            < code.indexOf("driverController.b().whileTrue(reverseIntakeCommand)"));
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
        Set.of(
            "RunIntakeCommand.java",
            "IntakeToFeederCommand.java",
            "IntakeCommissioningCommand.java"),
        intakeNamedCommandFiles);
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
  void intakeIORealIssuesNeutralOutputExceptForTheSingleVoltagePath() throws IOException {
    String code = withoutCommentsAndLiterals(source("frc/robot/io/intake/IntakeIOReal.java"));

    Set<String> controlImports =
        Pattern.compile("import\\s+com\\.ctre\\.phoenix6\\.controls\\.(\\w+)\\s*;")
            .matcher(code)
            .results()
            .map(result -> result.group(1))
            .collect(Collectors.toSet());
    assertEquals(Set.of("NeutralOut", "VoltageOut"), controlImports);

    long setControlCalls = Pattern.compile("\\.setControl\\s*\\(").matcher(code).results().count();
    long neutralSetControlCalls =
        Pattern.compile("\\.setControl\\s*\\(\\s*neutralRequest\\s*\\)")
            .matcher(code)
            .results()
            .count();
    long voltageSetControlCalls =
        Pattern.compile(
                "\\.setControl\\s*\\(\\s*voltageRequest\\s*\\.\\s*withOutput\\s*\\(\\s*volts\\s*\\)\\s*\\)")
            .matcher(code)
            .results()
            .count();
    assertEquals(3L, setControlCalls);
    assertEquals(2L, neutralSetControlCalls);
    assertEquals(1L, voltageSetControlCalls);
    assertTrue(code.contains("new VoltageOut(0.0).withEnableFOC(false)"));
    assertEquals(1L, Pattern.compile("\\bnew\\s+VoltageOut\\b").matcher(code).results().count());

    int setVoltageMethod = code.indexOf("public void setVoltage(double volts)");
    int voltageControl = code.indexOf("voltageRequest.withOutput(volts)");
    assertTrue(setVoltageMethod >= 0);
    assertTrue(voltageControl > setVoltageMethod);

    assertFalse(Pattern.compile("\\.set\\s*\\(").matcher(code).find());
    assertFalse(Pattern.compile("\\.setVoltage\\s*\\(").matcher(code).find());
    assertFalse(code.contains("phoenix6.controls.*"));
  }

  @Test
  void intakeVoltageIsRequestedOnlyByTheSubsystemCommissioningPath() throws IOException {
    Path main = Path.of("src", "main", "java");
    Set<String> voltageCallers;
    try (Stream<Path> files = Files.walk(main)) {
      voltageCallers =
          files
              .filter(Files::isRegularFile)
              .filter(path -> path.getFileName().toString().endsWith(".java"))
              .filter(
                  path ->
                      Pattern.compile("intakeIO\\s*\\.\\s*setVoltage\\s*\\(")
                          .matcher(withoutCommentsAndLiterals(readUnchecked(path)))
                          .find())
              .map(path -> normalizedRelativePath(main, path))
              .collect(Collectors.toSet());
    }
    assertEquals(Set.of("frc/robot/subsystems/IntakeSubsystem.java"), voltageCallers);

    String subsystem = withoutCommentsAndLiterals(source("frc/robot/subsystems/IntakeSubsystem.java"));
    int startMethod = subsystem.indexOf("public boolean startCommissioningVoltage(double volts)");
    int testGate = subsystem.indexOf("DriverStation.isTestEnabled()");
    assertTrue(startMethod >= 0);
    assertTrue(testGate >= 0);
    assertEquals(
        2L, Pattern.compile("intakeIO\\s*\\.\\s*setVoltage\\s*\\(").matcher(subsystem).results().count());
    assertTrue(subsystem.contains("public boolean startManual(ManualDirection direction)"));
    assertTrue(subsystem.contains("DriverStation.isTeleopEnabled()"));
    assertTrue(source("frc/robot/commands/RunIntakeCommand.java").contains("startManual"));
    assertFalse(source("frc/robot/commands/IntakeToFeederCommand.java").contains("startManual"));
    assertFalse(source("frc/robot/commands/IntakeCommissioningCommand.java").contains("startManual"));
    assertFalse(robotContainer().contains("startManual"));

    for (String command :
        Set.of(
            "frc/robot/commands/RunIntakeCommand.java",
            "frc/robot/commands/IntakeToFeederCommand.java")) {
      // Neither held-button nor autonomous-event commands may use the commissioning path.
      assertFalse(source(command).contains("startCommissioningVoltage"), command);
    }
    String robotContainer = withoutCommentsAndLiterals(source("frc/robot/RobotContainer.java"));
    assertFalse(robotContainer.contains("startCommissioningVoltage"));
    assertFalse(
        Pattern.compile("(?s)\\.(?:whileTrue|onTrue|onFalse|toggleOnTrue)\\s*\\([^;]*IntakeCommissioning")
            .matcher(robotContainer)
            .find());
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

  private static String robotContainer() throws IOException {
    return withoutCommentsAndLiterals(source("frc/robot/RobotContainer.java"));
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
