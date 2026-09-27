// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;
import org.junit.jupiter.api.Test;

class AutoBuilderContractAdapterRecoveryTest {
  private static final double kTolerance = 1.0e-12;

  @Test
  void poseValidationAcceptsExactUnderAndExactThreshold() {
    Pose2d expected = Pose2d.kZero;

    assertTrue(AutoBuilderContractAdapter.isPoseWithinTolerance(expected, expected));
    assertTrue(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                Constants.AutonomousPreparationConstants.kTranslationToleranceMeters - 1.0e-6,
                0.0,
                Rotation2d.fromRadians(
                    Constants.AutonomousPreparationConstants.kHeadingToleranceRadians
                        - 1.0e-6)),
            expected));
    assertTrue(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                Constants.AutonomousPreparationConstants.kTranslationToleranceMeters,
                0.0,
                Rotation2d.fromRadians(
                    Constants.AutonomousPreparationConstants.kHeadingToleranceRadians)),
            expected));
  }

  @Test
  void poseValidationRejectsEitherErrorAboveThreshold() {
    Pose2d expected = Pose2d.kZero;

    assertFalse(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                Constants.AutonomousPreparationConstants.kTranslationToleranceMeters + 1.0e-6,
                0.0,
                Rotation2d.kZero),
            expected));
    assertFalse(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                0.0,
                0.0,
                Rotation2d.fromRadians(
                    Constants.AutonomousPreparationConstants.kHeadingToleranceRadians
                        + 1.0e-6)),
            expected));
  }

  @Test
  void wrappedPositiveAndNegativePiAreEquivalent() {
    Pose2d positivePi = new Pose2d(0.0, 0.0, Rotation2d.fromRadians(Math.PI));
    Pose2d negativePi = new Pose2d(0.0, 0.0, Rotation2d.fromRadians(-Math.PI));

    assertEquals(
        0.0,
        AutoBuilderContractAdapter.headingErrorRadians(positivePi, negativePi),
        kTolerance);
    assertTrue(
        AutoBuilderContractAdapter.isPoseWithinTolerance(positivePi, negativePi));
  }

  @Test
  void nonfinitePoseCannotPassValidation() {
    Pose2d nonfinite = new Pose2d(Double.NaN, 0.0, Rotation2d.kZero);

    assertThrows(
        IllegalArgumentException.class,
        () -> AutoBuilderContractAdapter.isPoseWithinTolerance(nonfinite, Pose2d.kZero));
  }
}
