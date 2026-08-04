// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import org.junit.jupiter.api.Test;

class SwerveKinematicsTest {
  private static final double TOLERANCE = 1.0e-9;

  private final SwerveKinematics kinematics = new SwerveKinematics();

  @Test
  void zeroChassisSpeedsProduceFourZeroStates() {
    SwerveModuleState[] states = kinematics.toModuleStates(new ChassisSpeeds());

    assertEquals(4, states.length);
    for (SwerveModuleState state : states) {
      assertEquals(0.0, state.speedMetersPerSecond, TOLERANCE);
      assertEquals(0.0, state.angle.getRadians(), TOLERANCE);
    }
  }

  @Test
  void positiveRobotForwardTranslationProducesForwardStates() {
    SwerveModuleState[] states =
        kinematics.toModuleStates(new ChassisSpeeds(1.0, 0.0, 0.0));

    assertAllStates(states, 1.0, 0.0);
  }

  @Test
  void positiveRobotLeftTranslationProducesLeftStates() {
    SwerveModuleState[] states =
        kinematics.toModuleStates(new ChassisSpeeds(0.0, 1.0, 0.0));

    assertAllStates(states, 1.0, Math.PI / 2.0);
  }

  @Test
  void positiveCounterclockwiseRotationProducesExpectedOrderedStates() {
    SwerveModuleState[] states =
        kinematics.toModuleStates(new ChassisSpeeds(0.0, 0.0, 1.0));
    double halfWheelbase = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidth = Constants.SwerveConstants.kTrackWidthMeters / 2.0;
    double expectedSpeed = Math.hypot(halfWheelbase, halfTrackWidth);

    assertState(states[0], expectedSpeed, 3.0 * Math.PI / 4.0);
    assertState(states[1], expectedSpeed, Math.PI / 4.0);
    assertState(states[2], expectedSpeed, -3.0 * Math.PI / 4.0);
    assertState(states[3], expectedSpeed, -Math.PI / 4.0);
  }

  @Test
  void combinedTranslationAndRotationUsesRobotGeometry() {
    double vx = 1.0;
    double vy = 0.5;
    double omega = 1.0;
    SwerveModuleState[] states =
        kinematics.toModuleStates(new ChassisSpeeds(vx, vy, omega));
    double halfWheelbase = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidth = Constants.SwerveConstants.kTrackWidthMeters / 2.0;

    assertVectorState(
        states[0],
        -halfTrackWidth + vx,
        halfWheelbase + vy);
    assertVectorState(
        states[1],
        halfTrackWidth + vx,
        halfWheelbase + vy);
    assertVectorState(
        states[2],
        -halfTrackWidth + vx,
        -halfWheelbase + vy);
    assertVectorState(
        states[3],
        halfTrackWidth + vx,
        -halfWheelbase + vy);
  }

  @Test
  void moduleStatesRemainInFrontLeftFrontRightBackLeftBackRightOrder() {
    SwerveModuleState[] states =
        kinematics.toModuleStates(new ChassisSpeeds(0.0, 0.0, 1.0));

    assertEquals(3.0 * Math.PI / 4.0, states[0].angle.getRadians(), TOLERANCE);
    assertEquals(Math.PI / 4.0, states[1].angle.getRadians(), TOLERANCE);
    assertEquals(-3.0 * Math.PI / 4.0, states[2].angle.getRadians(), TOLERANCE);
    assertEquals(-Math.PI / 4.0, states[3].angle.getRadians(), TOLERANCE);
  }

  @Test
  void rejectsNullChassisSpeeds() {
    assertThrows(NullPointerException.class, () -> kinematics.toModuleStates(null));
  }

  private static void assertAllStates(
      SwerveModuleState[] states, double expectedSpeed, double expectedAngleRadians) {
    assertEquals(4, states.length);
    for (SwerveModuleState state : states) {
      assertState(state, expectedSpeed, expectedAngleRadians);
    }
  }

  private static void assertVectorState(
      SwerveModuleState state, double expectedVx, double expectedVy) {
    double expectedSpeed = Math.hypot(expectedVx, expectedVy);
    double expectedAngle = Math.atan2(expectedVy, expectedVx);
    assertState(state, expectedSpeed, expectedAngle);
  }

  private static void assertState(
      SwerveModuleState state, double expectedSpeed, double expectedAngleRadians) {
    assertEquals(expectedSpeed, state.speedMetersPerSecond, TOLERANCE);
    assertEquals(expectedAngleRadians, state.angle.getRadians(), TOLERANCE);
  }
}
