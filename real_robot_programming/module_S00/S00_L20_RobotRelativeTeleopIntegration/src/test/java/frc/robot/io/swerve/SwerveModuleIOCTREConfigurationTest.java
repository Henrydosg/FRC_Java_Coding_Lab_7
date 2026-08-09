// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;

class SwerveModuleIOCTREConfigurationTest {
  private static final double kBackRightExpectedOffsetRotations = -0.052978515625;
  private static final double kCANcoderQuantizationStepRotations = 1.0 / 4096.0;

  @Test
  void acceptsExactMagnetOffsetMatch() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);

    assertTrue(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void acceptsOneRotationWrapEquivalentOffset() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations + 1.0);

    assertTrue(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void acceptsOneCANcoderQuantizationStepDifference() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations + kCANcoderQuantizationStepRotations);

    assertTrue(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void rejectsOffsetDifferenceLargerThanOneCANcoderQuantizationStep() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations
                + (2.0 * kCANcoderQuantizationStepRotations));

    assertFalse(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void rejectsSensorDirectionMismatch() {
    CANcoderConfiguration expected =
        createEncoderConfiguration(
            SensorDirectionValue.CounterClockwise_Positive,
            kBackRightExpectedOffsetRotations);
    CANcoderConfiguration actual =
        createEncoderConfiguration(
            SensorDirectionValue.Clockwise_Positive,
            kBackRightExpectedOffsetRotations);

    assertFalse(SwerveModuleIOCTRE.encoderConfigurationMatches(expected, actual));
  }

  @Test
  void configuresFrontLeftDriveFeedbackAndApprovedSlot0Gains() {
    TalonFXConfiguration configuration = SwerveModuleIOCTRE.createDriveConfiguration(false, true);

    assertEquals(FeedbackSensorSourceValue.RotorSensor, configuration.Feedback.FeedbackSensorSource);
    assertEquals(1.0, configuration.Feedback.RotorToSensorRatio);
    assertEquals(
        Constants.SwerveConstants.kDriveGearRatio,
        configuration.Feedback.SensorToMechanismRatio);
    assertEquals(
        Constants.SwerveConstants.kDriveVelocitySlot0KpVoltsPerRotationPerSecond,
        configuration.Slot0.kP);
    assertEquals(
        Constants.SwerveConstants.kDriveVelocitySlot0KiVoltsPerRotation,
        configuration.Slot0.kI);
    assertEquals(
        Constants.SwerveConstants.kDriveVelocitySlot0KdVoltsPerRotationPerSecondSquared,
        configuration.Slot0.kD);
    assertEquals(
        Constants.SwerveConstants.kDriveVelocitySlot0KvVoltsPerRotationPerSecond,
        configuration.Slot0.kV);
  }

  @Test
  void allFourFactoryConfigurationPathsEnableClosedLoopFields() {
    assertClosedLoopConfiguration(
        Constants.SwerveConstants.kFrontLeftDriveInverted,
        Constants.SwerveConstants.kFrontLeftSteerInverted,
        Constants.SwerveConstants.kFrontLeftEncoderCanId);
    assertClosedLoopConfiguration(
        Constants.SwerveConstants.kFrontRightDriveInverted,
        Constants.SwerveConstants.kFrontRightSteerInverted,
        Constants.SwerveConstants.kFrontRightEncoderCanId);
    assertClosedLoopConfiguration(
        Constants.SwerveConstants.kBackLeftDriveInverted,
        Constants.SwerveConstants.kBackLeftSteerInverted,
        Constants.SwerveConstants.kBackLeftEncoderCanId);
    assertClosedLoopConfiguration(
        Constants.SwerveConstants.kBackRightDriveInverted,
        Constants.SwerveConstants.kBackRightSteerInverted,
        Constants.SwerveConstants.kBackRightEncoderCanId);
  }

  @Test
  void configuresFrontLeftSteerRemoteCANcoderContinuousWrapAndApprovedSlot0Gains() {
    TalonFXConfiguration configuration =
        SwerveModuleIOCTRE.createSteerConfiguration(
            true,
            Constants.SwerveConstants.kFrontLeftEncoderCanId,
            true);

    assertEquals(
        FeedbackSensorSourceValue.RemoteCANcoder,
        configuration.Feedback.FeedbackSensorSource);
    assertEquals(
        Constants.SwerveConstants.kFrontLeftEncoderCanId,
        configuration.Feedback.FeedbackRemoteSensorID);
    assertEquals(1.0, configuration.Feedback.SensorToMechanismRatio);
    assertTrue(configuration.ClosedLoopGeneral.ContinuousWrap);
    assertEquals(
        Constants.SwerveConstants.kSteerPositionSlot0KpVoltsPerRotation,
        configuration.Slot0.kP);
    assertEquals(
        Constants.SwerveConstants.kSteerPositionSlot0KiVoltsPerRotationSecond,
        configuration.Slot0.kI);
    assertEquals(
        Constants.SwerveConstants.kSteerPositionSlot0KdVoltsPerRotationPerSecond,
        configuration.Slot0.kD);
  }

  @Test
  void rejectsClosedLoopCriticalConfigurationReadbackMismatch() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Feedback.SensorToMechanismRatio = 1.0;

    assertFalse(SwerveModuleIOCTRE.driveConfigurationMatches(expected, actual, true));
  }

  @Test
  void acceptsObservedPhoenixFeedbackRatioReadbackRepresentation() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Feedback.SensorToMechanismRatio = 7.846149921417;

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertTrue(comparison.healthy);
    assertTrue(comparison.passed("sensorToMechanismRatioMatches"));
  }

  @Test
  void rejectsMateriallyWrongFeedbackRatioReadback() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Feedback.SensorToMechanismRatio = 7.80;

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertFalse(comparison.healthy);
    assertEquals("sensorToMechanismRatioMatches", comparison.firstFailingField);
    assertFalse(comparison.passed("sensorToMechanismRatioMatches"));
  }

  @Test
  void acceptsObservedPhoenixSlot0KpReadbackRepresentation() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Slot0.kP = 0.100000001490;

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertTrue(comparison.healthy);
    assertTrue(comparison.passed("slot0KPMatches"));
  }

  @Test
  void rejectsMateriallyWrongSlot0KpReadback() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Slot0.kP = 0.100001;

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertFalse(comparison.healthy);
    assertEquals("slot0KPMatches", comparison.firstFailingField);
    assertFalse(comparison.passed("slot0KPMatches"));
  }

  @Test
  void acceptsVerifiedPhoenixFloat32Slot0KvReadback() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Slot0.kV = 0.12399999797344208;

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertTrue(comparison.healthy);
    assertTrue(comparison.passed("slot0KVMatches"));
  }

  @Test
  void rejectsMeaningfulSlot0KvReadbackMismatches() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    actual.Slot0.kV = 0.1239;

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertFalse(comparison.healthy);
    assertEquals("slot0KVMatches", comparison.firstFailingField);

    actual.Slot0.kV = 0.125;
    comparison = SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertFalse(comparison.healthy);
    assertEquals("slot0KVMatches", comparison.firstFailingField);
  }

  @Test
  void rejectsNonFiniteSlot0KvReadbackValues() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);

    actual.Slot0.kV = Double.NaN;
    assertSlot0KvMismatch(expected, actual);

    actual.Slot0.kV = Double.POSITIVE_INFINITY;
    assertSlot0KvMismatch(expected, actual);

    actual.Slot0.kV = Double.NEGATIVE_INFINITY;
    assertSlot0KvMismatch(expected, actual);
  }

  @Test
  void reportsEveryDriveComparisonWhenAllFieldsMatch() {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);

    assertTrue(comparison.healthy);
    assertEquals("none", comparison.firstFailingField);
    assertTrue(comparison.passed("applyStatusOK"));
    assertTrue(comparison.passed("refreshStatusOK"));
    assertTrue(comparison.passed("inversionMatches"));
    assertTrue(comparison.passed("supplyCurrentLimitEnableMatches"));
    assertTrue(comparison.passed("supplyCurrentLimitMatches"));
    assertTrue(comparison.passed("feedbackSourceMatches"));
    assertTrue(comparison.passed("rotorToSensorRatioMatches"));
    assertTrue(comparison.passed("sensorToMechanismRatioMatches"));
    assertTrue(comparison.passed("slot0KPMatches"));
    assertTrue(comparison.passed("slot0KIMatches"));
    assertTrue(comparison.passed("slot0KDMatches"));
    assertTrue(comparison.passed("slot0KSMatches"));
    assertTrue(comparison.passed("slot0KVMatches"));
    assertTrue(comparison.passed("slot0KAMatches"));
  }

  @Test
  void reportsApplyStatusFailureBeforeAllReadbackComparisons() {
    assertStatusFailure("applyStatusOK", false, true);
  }

  @Test
  void reportsRefreshStatusFailureAfterApplyStatus() {
    assertStatusFailure("refreshStatusOK", true, false);
  }

  @Test
  void reportsEachDriveConfigurationFieldIndependently() {
    assertFieldFailure(
        "inversionMatches",
        configuration -> configuration.MotorOutput.Inverted = InvertedValue.Clockwise_Positive);
    assertFieldFailure(
        "supplyCurrentLimitEnableMatches",
        configuration -> configuration.CurrentLimits.SupplyCurrentLimitEnable = false);
    assertFieldFailure(
        "supplyCurrentLimitMatches",
        configuration -> configuration.CurrentLimits.SupplyCurrentLimit = 1.0);
    assertFieldFailure(
        "feedbackSourceMatches",
        configuration ->
            configuration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder);
    assertFieldFailure(
        "rotorToSensorRatioMatches",
        configuration -> configuration.Feedback.RotorToSensorRatio = 2.0);
    assertFieldFailure(
        "sensorToMechanismRatioMatches",
        configuration -> configuration.Feedback.SensorToMechanismRatio = 1.0);
    assertFieldFailure("slot0KPMatches", configuration -> configuration.Slot0.kP = 1.0);
    assertFieldFailure("slot0KIMatches", configuration -> configuration.Slot0.kI = 1.0);
    assertFieldFailure("slot0KDMatches", configuration -> configuration.Slot0.kD = 1.0);
    assertFieldFailure("slot0KSMatches", configuration -> configuration.Slot0.kS = 1.0);
    assertFieldFailure("slot0KVMatches", configuration -> configuration.Slot0.kV = 1.0);
    assertFieldFailure("slot0KAMatches", configuration -> configuration.Slot0.kA = 1.0);
  }

  @Test
  void createsVoltageRequestsWithFOCExplicitlyDisabled() {
    VelocityVoltage driveRequest = SwerveModuleIOCTRE.createDriveVelocityRequest(1.0);
    PositionVoltage steerRequest = SwerveModuleIOCTRE.createSteerPositionRequest(0.25);

    assertFalse(driveRequest.EnableFOC);
    assertFalse(steerRequest.EnableFOC);
    assertEquals(0, driveRequest.Slot);
    assertEquals(0, steerRequest.Slot);
    assertEquals(0.0, driveRequest.FeedForward);
    assertEquals(0.0, steerRequest.FeedForward);
  }

  @Test
  void convertsWheelVelocityAndNormalizesRotation2dTargets() {
    double commissioningVelocityMetersPerSecond =
        Constants.SwerveConstants.kFrontLeftPositiveDriveTestVelocityMetersPerSecond;
    double expectedWheelRotationsPerSecond =
        commissioningVelocityMetersPerSecond
            / (2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters);

    assertEquals(
        expectedWheelRotationsPerSecond,
        SwerveModuleIOCTRE.driveVelocityMetersPerSecondToWheelRotationsPerSecond(
            commissioningVelocityMetersPerSecond),
        1.0e-12);
    assertEquals(
        expectedWheelRotationsPerSecond * Constants.SwerveConstants.kDriveGearRatio,
        SwerveModuleIOCTRE.wheelRotationsPerSecondToDriveRotorRotationsPerSecond(
            expectedWheelRotationsPerSecond),
        1.0e-12);
    assertEquals(
        0.99,
        SwerveModuleIOCTRE.normalizeSteerAngleRotations(Rotation2d.fromRotations(0.99)),
        1.0e-12);
    assertEquals(
        0.01,
        SwerveModuleIOCTRE.normalizeSteerAngleRotations(Rotation2d.fromRotations(-0.99)),
        1.0e-12);
  }

  @Test
  void rejectsClosedLoopRequestsWhenConfigurationIsUnhealthyOrModuleIsNotFrontLeft() {
    assertTrue(SwerveModuleIOCTRE.closedLoopRequestAllowed(true, true));
    assertFalse(SwerveModuleIOCTRE.closedLoopRequestAllowed(true, false));
    assertFalse(SwerveModuleIOCTRE.closedLoopRequestAllowed(false, true));
  }

  @Test
  void allowsOpenLoopDriveWhenOnlyClosedLoopHealthIsUnhealthy() {
    assertTrue(SwerveModuleIOCTRE.openLoopRequestAllowed(true, 0.05));
    assertTrue(SwerveModuleIOCTRE.openLoopRequestAllowed(false, 0.0));
    assertFalse(SwerveModuleIOCTRE.openLoopRequestAllowed(false, 0.05));
  }

  @Test
  void allowsOpenLoopSteerWhenOnlyClosedLoopHealthIsUnhealthy() {
    assertTrue(SwerveModuleIOCTRE.openLoopRequestAllowed(true, 0.05));
    assertTrue(SwerveModuleIOCTRE.openLoopRequestAllowed(false, 0.0));
    assertFalse(SwerveModuleIOCTRE.openLoopRequestAllowed(false, 0.05));
  }

  @Test
  void rejectsClosedLoopDriveWhenDriveClosedLoopHealthIsUnhealthy() {
    assertFalse(SwerveModuleIOCTRE.driveClosedLoopRequestAllowed(true, false, true));
    assertFalse(SwerveModuleIOCTRE.driveClosedLoopRequestAllowed(true, true, false));
    assertTrue(SwerveModuleIOCTRE.driveClosedLoopRequestAllowed(true, true, true));
  }

  @Test
  void healthyDriveConfigurationAllowsTheVelocityVoltageRequest() {
    double expectedWheelRotationsPerSecond =
        SwerveModuleIOCTRE.driveVelocityMetersPerSecondToWheelRotationsPerSecond(
            Constants.SwerveConstants.kFrontLeftPositiveDriveTestVelocityMetersPerSecond);
    VelocityVoltage request =
        SwerveModuleIOCTRE.createDriveVelocityRequest(expectedWheelRotationsPerSecond);

    assertTrue(SwerveModuleIOCTRE.driveClosedLoopRequestAllowed(true, true, true));
    assertEquals(expectedWheelRotationsPerSecond, request.Velocity);
    assertFalse(request.EnableFOC);
    assertEquals(0, request.Slot);
  }

  @Test
  void closedLoopDriveUsesGlobalWheelSpeedLimitWithoutChangingFrontLeftCommissioningValue() {
    assertEquals(
        Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond,
        SwerveModuleIOCTRE.clampDriveVelocityMetersPerSecond(10.0));
    assertEquals(
        -Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond,
        SwerveModuleIOCTRE.clampDriveVelocityMetersPerSecond(-10.0));
    assertEquals(
        Constants.SwerveConstants.kFrontLeftPositiveDriveTestVelocityMetersPerSecond,
        SwerveModuleIOCTRE.clampDriveVelocityMetersPerSecond(
            Constants.SwerveConstants.kFrontLeftPositiveDriveTestVelocityMetersPerSecond));
  }

  @Test
  void createsStaticFrictionVoltageRequestWithFOCExplicitlyDisabled() {
    VoltageOut request = SwerveModuleIOCTRE.createDriveStaticFrictionVoltageRequest(0.50);

    assertEquals(0.50, request.Output);
    assertFalse(request.EnableFOC);
  }

  @Test
  void clampsStaticFrictionVoltageToTheApprovedPositiveRange() {
    assertEquals(
        0.0, SwerveModuleIOCTRE.clampFrontLeftDriveStaticFrictionVoltageVolts(-0.10));
    assertEquals(
        0.50, SwerveModuleIOCTRE.clampFrontLeftDriveStaticFrictionVoltageVolts(0.50));
    assertEquals(
        Constants.SwerveConstants.kFrontLeftDriveStaticFrictionMaximumVoltageVolts,
        SwerveModuleIOCTRE.clampFrontLeftDriveStaticFrictionVoltageVolts(2.0));
  }

  @Test
  void allowsStaticFrictionVoltageOnlyForHealthyEnabledFrontLeftTestMode() {
    assertTrue(
        SwerveModuleIOCTRE.frontLeftDriveStaticFrictionRequestAllowed(
            "Front Left", true, true, true, 0.10));
    assertFalse(
        SwerveModuleIOCTRE.frontLeftDriveStaticFrictionRequestAllowed(
            "Front Right", true, true, true, 0.10));
    assertFalse(
        SwerveModuleIOCTRE.frontLeftDriveStaticFrictionRequestAllowed(
            "Front Left", false, true, true, 0.10));
    assertFalse(
        SwerveModuleIOCTRE.frontLeftDriveStaticFrictionRequestAllowed(
            "Front Left", true, false, true, 0.10));
    assertFalse(
        SwerveModuleIOCTRE.frontLeftDriveStaticFrictionRequestAllowed(
            "Front Left", true, true, false, 0.10));
  }

  @Test
  void classifiesBreakawayFromPeakAbsoluteRotorVelocity() {
    assertFalse(SwerveModuleIOCTRE.driveStaticFrictionBreakawayDetected(0.099));
    assertTrue(SwerveModuleIOCTRE.driveStaticFrictionBreakawayDetected(0.10));
    assertTrue(SwerveModuleIOCTRE.driveStaticFrictionBreakawayDetected(0.11));
    assertFalse(SwerveModuleIOCTRE.driveStaticFrictionBreakawayDetected(Double.NaN));
  }

  @Test
  void rejectsClosedLoopSteerWhenSteerClosedLoopHealthIsUnhealthy() {
    assertFalse(SwerveModuleIOCTRE.steerClosedLoopRequestAllowed(true, false, true));
    assertFalse(SwerveModuleIOCTRE.steerClosedLoopRequestAllowed(true, true, false));
    assertTrue(SwerveModuleIOCTRE.steerClosedLoopRequestAllowed(true, true, true));
  }

  @Test
  void rejectsAllNonzeroRequestsWhenBaseHealthIsUnhealthy() {
    assertFalse(SwerveModuleIOCTRE.openLoopRequestAllowed(false, 0.05));
    assertFalse(SwerveModuleIOCTRE.driveClosedLoopRequestAllowed(true, false, true));
    assertFalse(SwerveModuleIOCTRE.steerClosedLoopRequestAllowed(true, false, true));
  }

  private static CANcoderConfiguration createEncoderConfiguration(
      SensorDirectionValue sensorDirection,
      double magnetOffsetRotations) {
    CANcoderConfiguration configuration = new CANcoderConfiguration();
    configuration.MagnetSensor =
        new MagnetSensorConfigs()
            .withSensorDirection(sensorDirection)
            .withMagnetOffset(magnetOffsetRotations);
    return configuration;
  }

  private static void assertClosedLoopConfiguration(
      boolean driveMotorInverted,
      boolean steerMotorInverted,
      int encoderCanId) {
    TalonFXConfiguration driveConfiguration =
        SwerveModuleIOCTRE.createDriveConfiguration(driveMotorInverted, true);
    TalonFXConfiguration steerConfiguration =
        SwerveModuleIOCTRE.createSteerConfiguration(steerMotorInverted, encoderCanId, true);

    assertEquals(FeedbackSensorSourceValue.RotorSensor,
        driveConfiguration.Feedback.FeedbackSensorSource);
    assertEquals(Constants.SwerveConstants.kDriveGearRatio,
        driveConfiguration.Feedback.SensorToMechanismRatio);
    assertEquals(FeedbackSensorSourceValue.RemoteCANcoder,
        steerConfiguration.Feedback.FeedbackSensorSource);
    assertEquals(encoderCanId, steerConfiguration.Feedback.FeedbackRemoteSensorID);
    assertEquals(1.0, steerConfiguration.Feedback.SensorToMechanismRatio);
    assertTrue(steerConfiguration.ClosedLoopGeneral.ContinuousWrap);
    assertTrue(SwerveModuleIOCTRE.driveClosedLoopRequestAllowed(true, true, true));
    assertTrue(SwerveModuleIOCTRE.steerClosedLoopRequestAllowed(true, true, true));
  }

  private static void assertStatusFailure(
      String expectedFirstFailure, boolean applyStatusOK, boolean refreshStatusOK) {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(
            applyStatusOK, refreshStatusOK, expected, actual, true);

    assertFalse(comparison.healthy);
    assertEquals(expectedFirstFailure, comparison.firstFailingField);
    assertFalse(comparison.passed(expectedFirstFailure));
  }

  private static void assertFieldFailure(
      String expectedFirstFailure, Consumer<TalonFXConfiguration> mutator) {
    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        driveComparisonWithFailure(expectedFirstFailure, mutator);

    assertFalse(comparison.healthy);
    assertEquals(expectedFirstFailure, comparison.firstFailingField);
    assertFalse(comparison.passed(expectedFirstFailure));
  }

  private static SwerveModuleIOCTRE.DriveConfigurationComparison driveComparisonWithFailure(
      String expectedFirstFailure, Consumer<TalonFXConfiguration> mutator) {
    TalonFXConfiguration expected = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    TalonFXConfiguration actual = SwerveModuleIOCTRE.createDriveConfiguration(false, true);
    mutator.accept(actual);

    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);
    assertEquals(expectedFirstFailure, comparison.firstFailingField);
    return comparison;
  }

  private static void assertSlot0KvMismatch(
      TalonFXConfiguration expected, TalonFXConfiguration actual) {
    SwerveModuleIOCTRE.DriveConfigurationComparison comparison =
        SwerveModuleIOCTRE.compareDriveConfiguration(true, true, expected, actual, true);
    assertFalse(comparison.healthy);
    assertEquals("slot0KVMatches", comparison.firstFailingField);
  }

}
