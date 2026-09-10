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

import edu.wpi.first.math.geometry.Rotation2d;
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

  @Test
  void inverseConversionReusesGeometryForForwardStrafeAndRotation() {
    assertRoundTrip(new ChassisSpeeds(1.0, 0.0, 0.0));
    assertRoundTrip(new ChassisSpeeds(0.0, 1.0, 0.0));
    assertRoundTrip(new ChassisSpeeds(0.0, 0.0, 1.0));
    assertRoundTrip(new ChassisSpeeds(0.75, -0.25, -0.60));
  }

  @Test
  void inverseConversionDefensivelyCopiesAndValidatesStates() {
    SwerveModuleState[] states =
        kinematics.toModuleStates(new ChassisSpeeds(0.75, -0.25, 0.50));
    SwerveModuleState originalFirst =
        new SwerveModuleState(
            states[0].speedMetersPerSecond,
            new Rotation2d(states[0].angle.getRadians()));

    ChassisSpeeds speeds = kinematics.toChassisSpeeds(states);

    assertEquals(originalFirst.speedMetersPerSecond, states[0].speedMetersPerSecond, TOLERANCE);
    assertEquals(originalFirst.angle.getRadians(), states[0].angle.getRadians(), TOLERANCE);
    assertEquals(0.75, speeds.vxMetersPerSecond, TOLERANCE);
    assertEquals(-0.25, speeds.vyMetersPerSecond, TOLERANCE);
    assertEquals(0.50, speeds.omegaRadiansPerSecond, TOLERANCE);

    assertThrows(NullPointerException.class, () -> kinematics.toChassisSpeeds(null));
    assertThrows(
        IllegalArgumentException.class,
        () -> kinematics.toChassisSpeeds(new SwerveModuleState[3]));
    SwerveModuleState[] nullState = kinematics.toModuleStates(new ChassisSpeeds());
    nullState[2] = null;
    assertThrows(NullPointerException.class, () -> kinematics.toChassisSpeeds(nullState));
    SwerveModuleState[] nonfinite = kinematics.toModuleStates(new ChassisSpeeds());
    nonfinite[1].speedMetersPerSecond = Double.NaN;
    assertThrows(IllegalArgumentException.class, () -> kinematics.toChassisSpeeds(nonfinite));
  }

  private void assertRoundTrip(ChassisSpeeds expected) {
    ChassisSpeeds actual = kinematics.toChassisSpeeds(kinematics.toModuleStates(expected));
    assertEquals(expected.vxMetersPerSecond, actual.vxMetersPerSecond, TOLERANCE);
    assertEquals(expected.vyMetersPerSecond, actual.vyMetersPerSecond, TOLERANCE);
    assertEquals(expected.omegaRadiansPerSecond, actual.omegaRadiansPerSecond, TOLERANCE);
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
