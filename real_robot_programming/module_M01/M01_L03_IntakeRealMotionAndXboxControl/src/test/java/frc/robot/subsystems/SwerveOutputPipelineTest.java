// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import org.junit.jupiter.api.Test;

class SwerveOutputPipelineTest {
  private static final double TOLERANCE = 1.0e-9;

  @Test
  void exactZeroChassisSpeedsPreserveFourDifferentCurrentAnglesInOrder() {
    Rotation2d[] currentAngles = differentAngles();
    SwerveModuleState[] states =
        new SwerveOutputPipeline().toModuleStates(new ChassisSpeeds(), currentAngles);

    assertEquals(4, states.length);
    for (int moduleIndex = 0; moduleIndex < states.length; moduleIndex++) {
      assertEquals(0.0, states[moduleIndex].speedMetersPerSecond, TOLERANCE);
      assertEquals(
          currentAngles[moduleIndex].getRadians(),
          states[moduleIndex].angle.getRadians(),
          TOLERANCE);
    }
  }

  @Test
  void zeroAfterNonzeroRequestUsesCurrentAnglesInsteadOfPreviousKinematicHeadings() {
    SwerveOutputPipeline pipeline = new SwerveOutputPipeline();
    pipeline.toModuleStates(new ChassisSpeeds(0.0, 1.0, 0.0), zeroAngles());
    Rotation2d[] currentAngles = differentAngles();

    SwerveModuleState[] states =
        pipeline.toModuleStates(new ChassisSpeeds(), currentAngles);

    for (int moduleIndex = 0; moduleIndex < states.length; moduleIndex++) {
      assertEquals(0.0, states[moduleIndex].speedMetersPerSecond, TOLERANCE);
      assertEquals(
          currentAngles[moduleIndex].getRadians(),
          states[moduleIndex].angle.getRadians(),
          TOLERANCE);
    }
  }

  @Test
  void exactZeroOutputsDoNotExposeOrRetainCallerOwnedAngleData() {
    Rotation2d[] currentAngles = differentAngles();
    SwerveModuleState[] states =
        new SwerveOutputPipeline().toModuleStates(new ChassisSpeeds(), currentAngles);
    double expectedFrontLeftRadians = states[0].angle.getRadians();
    double expectedBackLeftRadians = states[2].angle.getRadians();

    assertNotSame(currentAngles[0], states[0].angle);
    assertNotSame(states[0], states[1]);
    currentAngles[0] = Rotation2d.fromDegrees(179.0);
    states[1].angle = Rotation2d.fromDegrees(-179.0);

    assertEquals(expectedFrontLeftRadians, states[0].angle.getRadians(), TOLERANCE);
    assertEquals(expectedBackLeftRadians, states[2].angle.getRadians(), TOLERANCE);
  }

  @Test
  void normalMotionBelowSpeedLimitPassesThroughWithoutDesaturation() {
    SwerveModuleState[] states = new SwerveOutputPipeline().toModuleStates(
        new ChassisSpeeds(1.0, 0.0, 0.0), zeroAngles());

    for (SwerveModuleState state : states) {
      assertState(state, 1.0, 0.0);
    }
  }

  @Test
  void excessiveWheelSpeedsAreProportionallyDesaturated() {
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(3.0, 1.0, 6.0);
    SwerveKinematics kinematics = new SwerveKinematics();
    SwerveModuleStateOptimizer optimizer = new SwerveModuleStateOptimizer();
    SwerveModuleState[] optimizedStates = kinematics.toModuleStates(chassisSpeeds);
    for (int moduleIndex = 0; moduleIndex < optimizedStates.length; moduleIndex++) {
      optimizedStates[moduleIndex] =
          optimizer.optimize(optimizedStates[moduleIndex], new Rotation2d());
    }

    double maximumBeforeDesaturation = maximumAbsoluteSpeed(optimizedStates);
    SwerveModuleState[] finalStates =
        new SwerveOutputPipeline().toModuleStates(chassisSpeeds, zeroAngles());
    double scale = Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond
        / maximumBeforeDesaturation;

    assertTrue(maximumBeforeDesaturation > Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond);
    for (int moduleIndex = 0; moduleIndex < finalStates.length; moduleIndex++) {
      assertEquals(
          optimizedStates[moduleIndex].speedMetersPerSecond * scale,
          finalStates[moduleIndex].speedMetersPerSecond,
          TOLERANCE);
      assertEquals(
          optimizedStates[moduleIndex].angle.getRadians(),
          finalStates[moduleIndex].angle.getRadians(),
          TOLERANCE);
    }
  }

  @Test
  void noFinalAbsoluteWheelSpeedExceedsConfiguredLimit() {
    SwerveModuleState[] states = new SwerveOutputPipeline(2.0).toModuleStates(
        new ChassisSpeeds(8.0, 3.0, 4.0), zeroAngles());

    for (SwerveModuleState state : states) {
      assertTrue(Math.abs(state.speedMetersPerSecond) <= 2.0 + TOLERANCE);
    }
  }

  @Test
  void optimizedDirectionReversalIsPreservedThroughDesaturation() {
    SwerveModuleState[] states = new SwerveOutputPipeline(2.0).toModuleStates(
        new ChassisSpeeds(-1.0, 4.0, 0.0), zeroAngles());

    double expectedAngleDegrees = Math.toDegrees(Math.atan2(4.0, -1.0)) - 180.0;
    for (SwerveModuleState state : states) {
      assertEquals(-2.0, state.speedMetersPerSecond, TOLERANCE);
      assertEquals(expectedAngleDegrees, state.angle.getDegrees(), TOLERANCE);
    }
  }

  @Test
  void combinedTranslationAndRotationUsesOrderedRobotGeometry() {
    double vx = 1.0;
    double vy = 0.5;
    double omega = 1.0;
    double halfWheelbase = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidth = Constants.SwerveConstants.kTrackWidthMeters / 2.0;
    SwerveModuleState[] states = new SwerveOutputPipeline(100.0).toModuleStates(
        new ChassisSpeeds(vx, vy, omega), zeroAngles());

    assertVectorState(states[0], vx - omega * halfTrackWidth, vy + omega * halfWheelbase);
    assertVectorState(states[1], vx + omega * halfTrackWidth, vy + omega * halfWheelbase);
    assertVectorState(states[2], vx - omega * halfTrackWidth, vy - omega * halfWheelbase);
    assertVectorState(states[3], vx + omega * halfTrackWidth, vy - omega * halfWheelbase);
  }

  @Test
  void outputsRemainInFrontLeftFrontRightBackLeftBackRightOrder() {
    SwerveModuleState[] states = new SwerveOutputPipeline(100.0).toModuleStates(
        new ChassisSpeeds(0.0, 0.0, 1.0), zeroAngles());

    double expectedSpeed = Math.hypot(
        Constants.SwerveConstants.kWheelbaseMeters / 2.0,
        Constants.SwerveConstants.kTrackWidthMeters / 2.0);
    assertEquals(-expectedSpeed, states[0].speedMetersPerSecond, TOLERANCE);
    assertEquals(expectedSpeed, states[1].speedMetersPerSecond, TOLERANCE);
    assertEquals(-expectedSpeed, states[2].speedMetersPerSecond, TOLERANCE);
    assertEquals(expectedSpeed, states[3].speedMetersPerSecond, TOLERANCE);
    assertEquals(-45.0, states[0].angle.getDegrees(), TOLERANCE);
    assertEquals(45.0, states[1].angle.getDegrees(), TOLERANCE);
    assertEquals(45.0, states[2].angle.getDegrees(), TOLERANCE);
    assertEquals(-45.0, states[3].angle.getDegrees(), TOLERANCE);
    assertTrue(states[0].speedMetersPerSecond < 0.0);
    assertTrue(states[1].speedMetersPerSecond > 0.0);
    assertTrue(states[2].speedMetersPerSecond < 0.0);
    assertTrue(states[3].speedMetersPerSecond > 0.0);
  }

  @Test
  void rejectsNullChassisSpeeds() {
    assertThrows(
        NullPointerException.class,
        () -> new SwerveOutputPipeline().toModuleStates(null, zeroAngles()));
  }

  @Test
  void rejectsNullCurrentAngleArray() {
    assertThrows(
        NullPointerException.class,
        () -> new SwerveOutputPipeline().toModuleStates(new ChassisSpeeds(), null));
  }

  @Test
  void rejectsNullCurrentAngleElement() {
    Rotation2d[] angles = zeroAngles();
    angles[2] = null;

    assertThrows(
        NullPointerException.class,
        () -> new SwerveOutputPipeline().toModuleStates(new ChassisSpeeds(), angles));
  }

  @Test
  void rejectsWrongCurrentAngleArrayLength() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new SwerveOutputPipeline().toModuleStates(
            new ChassisSpeeds(), new Rotation2d[3]));
  }

  @Test
  void rejectsInvalidMaximumWheelSpeed() {
    assertThrows(IllegalArgumentException.class, () -> new SwerveOutputPipeline(0.0));
    assertThrows(IllegalArgumentException.class, () -> new SwerveOutputPipeline(-1.0));
    assertThrows(IllegalArgumentException.class, () -> new SwerveOutputPipeline(Double.NaN));
    assertThrows(
        IllegalArgumentException.class,
        () -> new SwerveOutputPipeline(Double.POSITIVE_INFINITY));
  }

  @Test
  void callerOwnedInputsAreNotMutatedOrRetained() {
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1.0, 2.0, 0.5);
    Rotation2d[] currentAngles = zeroAngles();
    SwerveOutputPipeline pipeline = new SwerveOutputPipeline();

    SwerveModuleState[] states = pipeline.toModuleStates(chassisSpeeds, currentAngles);

    assertEquals(1.0, chassisSpeeds.vxMetersPerSecond, TOLERANCE);
    assertEquals(2.0, chassisSpeeds.vyMetersPerSecond, TOLERANCE);
    assertEquals(0.5, chassisSpeeds.omegaRadiansPerSecond, TOLERANCE);
    for (Rotation2d currentAngle : currentAngles) {
      assertEquals(0.0, currentAngle.getRadians(), TOLERANCE);
    }
  }

  private static Rotation2d[] zeroAngles() {
    return new Rotation2d[] {
      new Rotation2d(), new Rotation2d(), new Rotation2d(), new Rotation2d()
    };
  }

  private static Rotation2d[] differentAngles() {
    return new Rotation2d[] {
      Rotation2d.fromDegrees(-35.0),
      Rotation2d.fromDegrees(20.0),
      Rotation2d.fromDegrees(125.0),
      Rotation2d.fromDegrees(-150.0)
    };
  }

  private static double maximumAbsoluteSpeed(SwerveModuleState[] states) {
    double maximum = 0.0;
    for (SwerveModuleState state : states) {
      maximum = Math.max(maximum, Math.abs(state.speedMetersPerSecond));
    }
    return maximum;
  }

  private static void assertVectorState(
      SwerveModuleState state, double expectedVx, double expectedVy) {
    assertState(state, Math.hypot(expectedVx, expectedVy), Math.toDegrees(Math.atan2(expectedVy, expectedVx)));
  }

  private static void assertState(
      SwerveModuleState state, double expectedSpeed, double expectedAngleDegrees) {
    assertEquals(expectedSpeed, state.speedMetersPerSecond, TOLERANCE);
    assertEquals(expectedAngleDegrees, state.angle.getDegrees(), TOLERANCE);
  }
}
