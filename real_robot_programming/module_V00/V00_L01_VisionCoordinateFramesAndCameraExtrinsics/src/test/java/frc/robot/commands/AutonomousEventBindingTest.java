// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AutonomousEventBindingTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void derivesTheOnlyStableNamedCommandsString() {
    AutonomousEventBinding binding =
        new AutonomousEventBinding(AutonomousEventId.LEARNING_EVENT, Commands::none, Set.of());

    assertEquals("LEARNING_EVENT", binding.eventName());
    assertTrue(binding.requirements().isEmpty());
  }

  @Test
  void rejectsNullInputsAndNullRequirementEntries() {
    assertThrows(
        NullPointerException.class,
        () -> new AutonomousEventBinding(null, Commands::none, Set.of()));
    assertThrows(
        NullPointerException.class,
        () -> new AutonomousEventBinding(AutonomousEventId.LEARNING_EVENT, null, Set.of()));
    assertThrows(
        NullPointerException.class,
        () -> new AutonomousEventBinding(AutonomousEventId.LEARNING_EVENT, Commands::none, null));

    Set<Subsystem> requirements = new HashSet<>();
    requirements.add(null);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new AutonomousEventBinding(
                AutonomousEventId.LEARNING_EVENT, Commands::none, requirements));
  }

  @Test
  void requirementsAreDefensivelyCopiedAndImmutable() {
    Subsystem subsystem = new Subsystem() {};
    Set<Subsystem> original = new HashSet<>();
    original.add(subsystem);
    AutonomousEventBinding binding =
        new AutonomousEventBinding(AutonomousEventId.LEARNING_EVENT, Commands::none, original);

    original.clear();
    assertEquals(Set.of(subsystem), binding.requirements());
    assertThrows(UnsupportedOperationException.class, () -> binding.requirements().clear());
  }

  @Test
  void rejectsSwerveRequirement() {
    SwerveSubsystem swerve =
        new SwerveSubsystem(new Module(), new Module(), new Module(), new Module(), new Gyro());

    assertThrows(
        IllegalArgumentException.class,
        () ->
            new AutonomousEventBinding(
                AutonomousEventId.LEARNING_EVENT, Commands::none, Set.of(swerve)));
  }

  private static final class Module implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {}
  }

  private static final class Gyro implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
