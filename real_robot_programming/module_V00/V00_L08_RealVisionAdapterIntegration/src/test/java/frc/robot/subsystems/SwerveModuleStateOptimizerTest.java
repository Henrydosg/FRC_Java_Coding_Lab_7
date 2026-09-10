// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import org.junit.jupiter.api.Test;

class SwerveModuleStateOptimizerTest {
  private static final double TOLERANCE = 1.0e-9;

  private final SwerveModuleStateOptimizer optimizer = new SwerveModuleStateOptimizer();

  @Test
  void rejectsNullDesiredState() {
    assertThrows(
        NullPointerException.class,
        () -> optimizer.optimize(null, new Rotation2d()));
  }

  @Test
  void rejectsNullCurrentAngle() {
    SwerveModuleState desiredState =
        new SwerveModuleState(2.0, Rotation2d.fromDegrees(20.0));

    assertThrows(
        NullPointerException.class,
        () -> optimizer.optimize(desiredState, null));
  }

  @Test
  void targetWithinNinetyDegreesKeepsSpeedDirection() {
    SwerveModuleState desiredState =
        new SwerveModuleState(2.0, Rotation2d.fromDegrees(45.0));

    SwerveModuleState optimizedState =
        optimizer.optimize(desiredState, Rotation2d.fromDegrees(0.0));

    assertState(optimizedState, 2.0, 45.0);
  }

  @Test
  void targetBeyondNinetyDegreesReversesSpeedAndRotatesTarget() {
    SwerveModuleState desiredState =
        new SwerveModuleState(2.0, Rotation2d.fromDegrees(120.0));

    SwerveModuleState optimizedState =
        optimizer.optimize(desiredState, Rotation2d.fromDegrees(0.0));

    assertState(optimizedState, -2.0, -60.0);
  }

  @Test
  void positiveWraparoundNearOneEightyKeepsDirection() {
    SwerveModuleState desiredState =
        new SwerveModuleState(1.5, Rotation2d.fromDegrees(-179.0));

    SwerveModuleState optimizedState =
        optimizer.optimize(desiredState, Rotation2d.fromDegrees(179.0));

    assertState(optimizedState, 1.5, -179.0);
  }

  @Test
  void negativeWraparoundNearOneEightyKeepsDirection() {
    SwerveModuleState desiredState =
        new SwerveModuleState(1.5, Rotation2d.fromDegrees(179.0));

    SwerveModuleState optimizedState =
        optimizer.optimize(desiredState, Rotation2d.fromDegrees(-179.0));

    assertState(optimizedState, 1.5, 179.0);
  }

  @Test
  void exactlyNinetyDegreesKeepsSpeedDirection() {
    SwerveModuleState desiredState =
        new SwerveModuleState(2.0, Rotation2d.fromDegrees(90.0));

    SwerveModuleState optimizedState =
        optimizer.optimize(desiredState, Rotation2d.fromDegrees(0.0));

    assertState(optimizedState, 2.0, 90.0);
  }

  @Test
  void inputDesiredStateIsNotMutatedAndResultIsIndependent() {
    SwerveModuleState desiredState =
        new SwerveModuleState(2.0, Rotation2d.fromDegrees(120.0));

    SwerveModuleState optimizedState =
        optimizer.optimize(desiredState, Rotation2d.fromDegrees(0.0));

    assertNotSame(desiredState, optimizedState);
    assertState(desiredState, 2.0, 120.0);
    assertState(optimizedState, -2.0, -60.0);
  }

  private static void assertState(
      SwerveModuleState state, double expectedSpeed, double expectedAngleDegrees) {
    assertEquals(expectedSpeed, state.speedMetersPerSecond, TOLERANCE);
    assertEquals(
        expectedAngleDegrees,
        state.angle.getDegrees(),
        TOLERANCE);
  }
}
