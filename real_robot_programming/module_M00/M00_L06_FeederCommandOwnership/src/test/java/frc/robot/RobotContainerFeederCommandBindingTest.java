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

/** Locks the bounded semantic Left Bumper Feeder command composition. */
class RobotContainerFeederCommandBindingTest {
  @Test
  void reusesExistingFeederSubsystemForExactlyOneRunFeederCommand() throws IOException {
    String source = robotContainerSource();

    assertTrue(source.contains("feederSubsystem = new FeederSubsystem(new FeederIONoop())"));
    assertTrue(
        source.contains("RunFeederCommand runFeederCommand = new RunFeederCommand(feederSubsystem)"));
    assertEqualsOneOccurrence(source, "new RunFeederCommand(feederSubsystem)");
    assertFalse(source.contains("feederSubsystem.setDefaultCommand"));
  }

  @Test
  void usesSemanticLeftBumperWhileTrueWithoutDirectSubsystemCalls() throws IOException {
    String source = robotContainerSource();

    assertTrue(source.contains("driverController.leftBumper().whileTrue(runFeederCommand)"));
    assertFalse(source.contains("Button.kLeftBumper"));
    assertFalse(source.contains("new JoystickButton(driverController.getHID(), 5)"));
    assertFalse(source.contains("feederSubsystem.requestFeed()"));
    assertFalse(source.contains("feederSubsystem.stop()"));
  }

  @Test
  void preservesRightBumperIntakeAndExistingControllerConfiguration() throws IOException {
    String source = robotContainerSource();

    assertTrue(source.contains("new RunIntakeCommand(intakeSubsystem)"));
    assertTrue(source.contains("driverController.rightBumper().whileTrue(runIntakeCommand)"));
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
