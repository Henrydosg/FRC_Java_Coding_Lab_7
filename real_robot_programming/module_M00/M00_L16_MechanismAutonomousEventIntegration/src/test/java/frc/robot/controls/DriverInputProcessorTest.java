// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify this file under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.controls;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;
import org.junit.jupiter.api.Test;

class DriverInputProcessorTest {
  private static final double kTolerance = 1.0e-12;

  @Test
  void zeroInputProducesZeroIntent() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(0.0, 0.0, 0.0);

    assertEquals(0.0, intent.forward(), kTolerance);
    assertEquals(0.0, intent.strafe(), kTolerance);
    assertEquals(0.0, intent.rotation(), kTolerance);
  }

  @Test
  void finiteValuesInsideDeadbandProduceZero() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(0.099, -Constants.DriverInputConstants.kAxisDeadband, 0.0);

    assertEquals(0.0, intent.forward(), kTolerance);
    assertEquals(0.0, intent.strafe(), kTolerance);
    assertEquals(0.0, intent.rotation(), kTolerance);
  }

  @Test
  void exactPositiveDeadbandEdgeProducesZeroAndJustAboveEdgeIsSmallPositive() {
    DriverInputProcessor.ProcessedDriverIntent atEdge =
        DriverInputProcessor.process(Constants.DriverInputConstants.kAxisDeadband, 0.0, 0.0);
    DriverInputProcessor.ProcessedDriverIntent justAbove =
        DriverInputProcessor.process(
            Constants.DriverInputConstants.kAxisDeadband + 0.0001, 0.0, 0.0);

    assertEquals(0.0, atEdge.forward(), kTolerance);
    assertEquals(1.234567901234568e-8, justAbove.forward(), kTolerance);
  }

  @Test
  void positiveMagnitudeIsMonotonicAfterDeadband() {
    double previous = 0.0;

    for (double rawValue : new double[] {0.15, 0.25, 0.50, 0.75, 1.00}) {
      double processed = DriverInputProcessor.process(rawValue, 0.0, 0.0).forward();
      assertTrue(processed > previous);
      previous = processed;
    }
  }

  @Test
  void representativeInputsFollowApprovedTransferFunction() {
    double[] rawValues = {0.05, 0.10, 0.15, 0.25, 0.50, 0.75, 1.00};
    double[] expectedValues = {
      0.0,
      0.0,
      0.00308641975308642,
      0.027777777777777776,
      0.19753086419753085,
      0.5216049382716049,
      1.0
    };

    for (int index = 0; index < rawValues.length; index++) {
      double actual = DriverInputProcessor.process(rawValues[index], 0.0, 0.0).forward();
      assertEquals(expectedValues[index], actual, kTolerance);
    }
  }

  @Test
  void valuesOutsideDeadbandUseApplyDeadbandBeforeShaping() {
    double rawValue = 0.55;
    double remappedValue =
        MathUtil.applyDeadband(rawValue, Constants.DriverInputConstants.kAxisDeadband);
    double expectedValue = remappedValue * remappedValue;

    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(rawValue, 0.0, 0.0);

    assertEquals(0.5, remappedValue, kTolerance);
    assertEquals(expectedValue, intent.forward(), kTolerance);
  }

  @Test
  void signedSquarePreservesSignAndShapesMagnitude() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(0.55, -0.55, 0.0);

    assertEquals(0.25, intent.forward(), kTolerance);
    assertEquals(-0.25, intent.strafe(), kTolerance);
    assertEquals(0.0, intent.rotation(), kTolerance);
  }

  @Test
  void positiveAndNegativeInputsAreSymmetric() {
    DriverInputProcessor.ProcessedDriverIntent positive =
        DriverInputProcessor.process(0.35, 0.35, 0.35);
    DriverInputProcessor.ProcessedDriverIntent negative =
        DriverInputProcessor.process(-0.35, -0.35, -0.35);

    assertEquals(positive.forward(), -negative.forward(), kTolerance);
    assertEquals(positive.strafe(), -negative.strafe(), kTolerance);
    assertEquals(positive.rotation(), -negative.rotation(), kTolerance);
  }

  @Test
  void fullScaleInputsRemainAtTheNormalizedLimits() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(1.0, -1.0, 1.0);

    assertEquals(1.0, intent.forward(), kTolerance);
    assertEquals(-1.0, intent.strafe(), kTolerance);
    assertEquals(1.0, intent.rotation(), kTolerance);
  }

  @Test
  void eachAxisProcessesIndependently() {
    DriverInputProcessor.ProcessedDriverIntent forwardOnly =
        DriverInputProcessor.process(0.55, 0.0, 0.0);
    DriverInputProcessor.ProcessedDriverIntent strafeOnly =
        DriverInputProcessor.process(0.0, 0.55, 0.0);
    DriverInputProcessor.ProcessedDriverIntent rotationOnly =
        DriverInputProcessor.process(0.0, 0.0, 0.55);

    assertEquals(0.25, forwardOnly.forward(), kTolerance);
    assertEquals(0.0, forwardOnly.strafe(), kTolerance);
    assertEquals(0.0, forwardOnly.rotation(), kTolerance);
    assertEquals(0.25, strafeOnly.strafe(), kTolerance);
    assertEquals(0.0, strafeOnly.forward(), kTolerance);
    assertEquals(0.0, strafeOnly.rotation(), kTolerance);
    assertEquals(0.25, rotationOnly.rotation(), kTolerance);
    assertEquals(0.0, rotationOnly.forward(), kTolerance);
    assertEquals(0.0, rotationOnly.strafe(), kTolerance);
  }

  @Test
  void outputsRemainWithinNormalizedBounds() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(2.0, -2.0, 1.5);

    assertTrue(isNormalized(intent.forward()));
    assertTrue(isNormalized(intent.strafe()));
    assertTrue(isNormalized(intent.rotation()));
  }

  @Test
  void nanProducesSafeZero() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(Double.NaN, 0.0, 0.0);

    assertEquals(0.0, intent.forward(), kTolerance);
    assertEquals(0.0, intent.strafe(), kTolerance);
    assertEquals(0.0, intent.rotation(), kTolerance);
  }

  @Test
  void positiveAndNegativeInfinityProduceSafeZero() {
    DriverInputProcessor.ProcessedDriverIntent intent =
        DriverInputProcessor.process(Double.POSITIVE_INFINITY, 0.0, Double.NEGATIVE_INFINITY);

    assertEquals(0.0, intent.forward(), kTolerance);
    assertEquals(0.0, intent.strafe(), kTolerance);
    assertEquals(0.0, intent.rotation(), kTolerance);
  }

  @Test
  void repeatedCallsAreDeterministicAndStateless() {
    DriverInputProcessor.ProcessedDriverIntent first =
        DriverInputProcessor.process(0.42, -0.31, 0.18);
    DriverInputProcessor.ProcessedDriverIntent second =
        DriverInputProcessor.process(0.42, -0.31, 0.18);

    assertEquals(first, second);
  }

  private static boolean isNormalized(double value) {
    return Double.isFinite(value)
        && value >= Constants.DriverInputConstants.kNormalizedMinimum
        && value <= Constants.DriverInputConstants.kNormalizedMaximum;
  }
}
