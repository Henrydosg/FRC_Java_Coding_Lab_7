// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.io.gyro.GyroIONoop;
import frc.robot.io.swerve.SwerveModuleIONoop;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AutonomousEventBindingTest {
  @Test
  void ownsStableIdentityAndDefensivelyCopiesRequirements() {
    Set<edu.wpi.first.wpilibj2.command.Subsystem> requirements = new HashSet<>();
    AutonomousEventBinding binding =
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT, Commands::none, requirements);

    requirements.add(new edu.wpi.first.wpilibj2.command.SubsystemBase() {});

    assertEquals("LEARNING_EVENT", binding.pathPlannerName());
    assertEquals(Set.of(), binding.requirements());
  }

  @Test
  void rejectsSwerveRequirement() {
    SwerveSubsystem swerveSubsystem =
        new SwerveSubsystem(
            new SwerveModuleIONoop(),
            new SwerveModuleIONoop(),
            new SwerveModuleIONoop(),
            new SwerveModuleIONoop(),
            new GyroIONoop());

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new AutonomousEventBinding(
                    AutonomousEventId.LEARNING_EVENT,
                    Commands::none,
                    Set.of(swerveSubsystem)));

    assertInstanceOf(IllegalArgumentException.class, exception);
  }
}
