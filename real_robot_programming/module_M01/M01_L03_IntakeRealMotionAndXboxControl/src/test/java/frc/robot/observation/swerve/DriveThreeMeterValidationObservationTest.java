// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.observation.swerve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

/** Guards the mechanism-specific package placement of the drive validation observation. */
class DriveThreeMeterValidationObservationTest {

  @Test
  void usesSwervePackageWithoutLegacyClass() {
    assertEquals(
        "frc.robot.observation.swerve.DriveThreeMeterValidationObservation",
        DriveThreeMeterValidationObservation.class.getName());
    assertTrue(
        Files.isRegularFile(
            Path.of(
                "src", "main", "java", "frc", "robot", "observation", "swerve",
                "DriveThreeMeterValidationObservation.java")));
    assertFalse(
        Files.exists(
            Path.of(
                "src", "main", "java", "frc", "robot", "observation",
                "DriveThreeMeterValidationObservation.java")));
    assertThrows(
        ClassNotFoundException.class,
        () ->
            Class.forName(
                "frc.robot.observation.DriveThreeMeterValidationObservation",
                false,
                DriveThreeMeterValidationObservation.class.getClassLoader()));
  }
}
