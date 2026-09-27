// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
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
import org.junit.jupiter.api.Test;

/** Locks the bounded semantic Right Bumper Intake command composition. */
class RobotContainerIntakeCommandBindingTest {
  @Test
  void reusesExistingIntakeSubsystemForExactlyOneRunIntakeCommand() throws IOException {
    String source = robotContainerSource();

    assertTrue(source.contains("intakeSubsystem = new IntakeSubsystem(new IntakeIONoop())"));
    assertTrue(
        source.contains("RunIntakeCommand runIntakeCommand = new RunIntakeCommand(intakeSubsystem)"));
    assertEqualsOneOccurrence(source, "new RunIntakeCommand(intakeSubsystem)");
    assertFalse(source.contains("intakeSubsystem.setDefaultCommand"));
  }

  @Test
  void usesSemanticRightBumperWhileTrueWithoutDirectSubsystemCalls() throws IOException {
    String source = robotContainerSource();

    assertTrue(source.contains("driverController.rightBumper().whileTrue(runIntakeCommand)"));
    assertFalse(source.contains("Button.kRightBumper"));
    assertFalse(source.contains("new JoystickButton(driverController.getHID(), 6)"));
    assertFalse(source.contains("intakeSubsystem.requestIntake()"));
    assertFalse(source.contains("intakeSubsystem.stop()"));
  }

  @Test
  void preservesExistingControllerPortAndBackViewPreparationBinding() throws IOException {
    String source = robotContainerSource();

    assertTrue(
        source.contains(
            "new CommandXboxController(Constants.DriverInputConstants.kXboxControllerPort)"));
    assertTrue(
        source.contains(
            "new JoystickButton(driverController.getHID(), XboxController.Button.kBack.value)"));
    assertTrue(source.contains(".and(DriverStation::isDisabled)"));
    assertTrue(source.contains(".onTrue(prepareAutonomousCommand)"));
  }

  private static void assertEqualsOneOccurrence(String source, String expected) {
    int first = source.indexOf(expected);
    assertTrue(first >= 0);
    assertTrue(source.indexOf(expected, first + expected.length()) < 0);
  }

  private static String robotContainerSource() throws IOException {
    return Files.readString(
        Path.of("src", "main", "java", "frc", "robot", "RobotContainer.java"));
  }
}
