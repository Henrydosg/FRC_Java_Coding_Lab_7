// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;
import com.ctre.phoenix6.signals.DeviceEnableValue;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorOutputStatusValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants.SwerveConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Reads and controls one CTRE-based Swerve module without interpreting module state.
 */
public class SwerveModuleIOCTRE implements SwerveModuleIO {
  /** Strict tolerance for base configuration values without Phoenix readback quantization. */
  private static final double kBaseConfigurationComparisonTolerance = 1.0e-9;
  /** Relative tolerance for Phoenix feedback-ratio readback representation. */
  private static final double kFeedbackRatioRelativeTolerance = 1.0e-6;
  /** Absolute floor for Phoenix feedback-ratio readback comparison. */
  private static final double kFeedbackRatioAbsoluteTolerance = 1.0e-6;
  /** Absolute tolerance for Phoenix float32 Slot 0 gain readback representation. */
  private static final double kSlot0GainFloat32ReadbackTolerance = 5.0e-9;
  /** Build identity for the concise failure-only configuration diagnostic. */
  private static final String kDriveConfigurationFailureBuildMarker =
      "S00_L17_DRIVE_CONFIGURATION";
  private static final double kCANcoderMagnetOffsetQuantizationStepRotations = 1.0 / 4096.0;
  private static final double kRotationsPerTurn = 1.0;
  private static final double kMinimumNormalizedOutput = -1.0;
  private static final double kMaximumNormalizedOutput = 1.0;
  private static final double kStoppedOutput = 0.0;
  private static final int kClosedLoopSlot = 0;
  private static final double kWheelCircumferenceMeters =
      2.0 * Math.PI * SwerveConstants.kWheelRadiusMeters;

  private final TalonFX driveMotor;
  private final TalonFX steerMotor;
  private final CANcoder absoluteEncoder;
  private final String moduleName;
  private final boolean closedLoopEnabled;

  private final StatusSignal<Double> driveDutyCycleSignal;
  private final StatusSignal<Angle> drivePositionSignal;
  private final StatusSignal<AngularVelocity> driveVelocitySignal;
  private final StatusSignal<AngularVelocity> driveMechanismVelocitySignal;
  private final StatusSignal<Angle> driveMechanismPositionSignal;
  private final StatusSignal<Voltage> driveSupplyVoltageSignal;
  private final StatusSignal<Voltage> driveMotorVoltageSignal;
  private final StatusSignal<Current> driveSupplyCurrentSignal;
  private final StatusSignal<Current> driveTorqueCurrentSignal;
  private final StatusSignal<Current> driveStatorCurrentSignal;
  private final StatusSignal<Temperature> driveTemperatureSignal;
  private final StatusSignal<Double> driveDutyCycleStatusSignal;
  private final StatusSignal<ControlModeValue> driveControlModeSignal;
  private final StatusSignal<Double> driveClosedLoopReferenceSignal;
  private final StatusSignal<Double> driveClosedLoopOutputSignal;
  private final StatusSignal<Double> driveClosedLoopErrorSignal;
  private final StatusSignal<Integer> driveClosedLoopSlotSignal;
  private final StatusSignal<DeviceEnableValue> driveDeviceEnableSignal;
  private final StatusSignal<MotorOutputStatusValue> driveMotorOutputStatusSignal;

  private final StatusSignal<Double> steerDutyCycleSignal;
  private final StatusSignal<Angle> steerPositionSignal;
  private final StatusSignal<AngularVelocity> steerVelocitySignal;
  private final StatusSignal<AngularVelocity> steerMechanismVelocitySignal;
  private final StatusSignal<Angle> steerMechanismPositionSignal;
  private final StatusSignal<Voltage> steerSupplyVoltageSignal;
  private final StatusSignal<Voltage> steerMotorVoltageSignal;
  private final StatusSignal<Current> steerSupplyCurrentSignal;
  private final StatusSignal<Current> steerTorqueCurrentSignal;
  private final StatusSignal<Current> steerStatorCurrentSignal;
  private final StatusSignal<Temperature> steerTemperatureSignal;
  private final StatusSignal<ControlModeValue> steerControlModeSignal;
  private final StatusSignal<Double> steerClosedLoopReferenceSignal;
  private final StatusSignal<Double> steerClosedLoopOutputSignal;
  private final StatusSignal<Double> steerClosedLoopErrorSignal;
  private final StatusSignal<Integer> steerClosedLoopSlotSignal;
  private final StatusSignal<DeviceEnableValue> steerDeviceEnableSignal;
  private final StatusSignal<MotorOutputStatusValue> steerMotorOutputStatusSignal;

  private final StatusSignal<Angle> encoderAbsolutePositionSignal;
  private final StatusSignal<AngularVelocity> encoderVelocitySignal;

  private final DutyCycleOut driveOutputRequest = new DutyCycleOut(kStoppedOutput);
  private final DutyCycleOut steerOutputRequest = new DutyCycleOut(kStoppedOutput);
  private final VelocityVoltage driveVelocityRequest = createDriveVelocityRequest(kStoppedOutput);
  private final PositionVoltage steerPositionRequest = createSteerPositionRequest(kStoppedOutput);
  private final VoltageOut driveStaticFrictionVoltageRequest =
      createDriveStaticFrictionVoltageRequest(kStoppedOutput);

  /** S00_L16 base configuration health used by open-loop output requests. */
  private final boolean driveBaseConfigurationHealthy;
  private final boolean steerBaseConfigurationHealthy;
  private final boolean encoderBaseConfigurationHealthy;

  /** Full configuration health, including S00_L17 closed-loop fields when enabled. */
  private final boolean driveClosedLoopConfigurationHealthy;
  private final boolean steerClosedLoopConfigurationHealthy;
  private final DriveConfigurationComparison driveConfigurationComparison;

  /** Existing telemetry-compatible full configuration health flags. */
  private final boolean driveConfigurationHealthy;
  private final boolean steerConfigurationHealthy;
  private final boolean encoderConfigurationHealthy;
  private boolean lastDriveConnected;
  private boolean lastSteerConnected;
  private boolean lastEncoderConnected;
  private boolean frontLeftDriveStaticFrictionCharacterizationActive;
  private double frontLeftDriveStaticFrictionRequestedVoltageVolts = Double.NaN;
  private StatusCode frontLeftDriveStaticFrictionSetControlStatus;
  private double frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps;
  private double frontLeftDriveStaticFrictionPeakAbsoluteMechanismVelocityRps;
  private double frontLeftDriveStaticFrictionPeakAbsoluteSupplyCurrentAmps;
  private double frontLeftDriveStaticFrictionPeakAbsoluteTorqueCurrentAmps;

  /**
   * Creates the verified front-left module implementation.
   *
   * @return front-left CTRE module IO
   */
  public static SwerveModuleIOCTRE createFrontLeft() {
    return new SwerveModuleIOCTRE(
        "Front Left",
        SwerveConstants.kFrontLeftDriveCanId,
        SwerveConstants.kFrontLeftSteerCanId,
        SwerveConstants.kFrontLeftEncoderCanId,
        SwerveConstants.kFrontLeftDriveInverted,
        SwerveConstants.kFrontLeftSteerInverted,
        SwerveConstants.kFrontLeftEncoderInverted,
        SwerveConstants.kFrontLeftEncoderOffsetRotations,
        true);
  }

  /**
   * Creates the verified front-right module implementation.
   *
   * @return front-right CTRE module IO
   */
  public static SwerveModuleIOCTRE createFrontRight() {
    return new SwerveModuleIOCTRE(
        "Front Right",
        SwerveConstants.kFrontRightDriveCanId,
        SwerveConstants.kFrontRightSteerCanId,
        SwerveConstants.kFrontRightEncoderCanId,
        SwerveConstants.kFrontRightDriveInverted,
        SwerveConstants.kFrontRightSteerInverted,
        SwerveConstants.kFrontRightEncoderInverted,
        SwerveConstants.kFrontRightEncoderOffsetRotations,
        true);
  }

  /**
   * Creates the verified back-left module implementation.
   *
   * @return back-left CTRE module IO
   */
  public static SwerveModuleIOCTRE createBackLeft() {
    return new SwerveModuleIOCTRE(
        "Back Left",
        SwerveConstants.kBackLeftDriveCanId,
        SwerveConstants.kBackLeftSteerCanId,
        SwerveConstants.kBackLeftEncoderCanId,
        SwerveConstants.kBackLeftDriveInverted,
        SwerveConstants.kBackLeftSteerInverted,
        SwerveConstants.kBackLeftEncoderInverted,
        SwerveConstants.kBackLeftEncoderOffsetRotations,
        true);
  }

  /**
   * Creates the verified back-right module implementation.
   *
   * @return back-right CTRE module IO
   */
  public static SwerveModuleIOCTRE createBackRight() {
    return new SwerveModuleIOCTRE(
        "Back Right",
        SwerveConstants.kBackRightDriveCanId,
        SwerveConstants.kBackRightSteerCanId,
        SwerveConstants.kBackRightEncoderCanId,
        SwerveConstants.kBackRightDriveInverted,
        SwerveConstants.kBackRightSteerInverted,
        SwerveConstants.kBackRightEncoderInverted,
        SwerveConstants.kBackRightEncoderOffsetRotations,
        true);
  }

  /**
   * Creates one module from its verified CTRE CAN identifiers.
   *
   * @param driveMotorCanId drive Talon FX CAN identifier
   * @param steerMotorCanId steer Talon FX CAN identifier
   * @param encoderCanId absolute CANcoder CAN identifier
   */
  public SwerveModuleIOCTRE(
      int driveMotorCanId,
      int steerMotorCanId,
      int encoderCanId,
      boolean driveMotorInverted,
      boolean steerMotorInverted,
      boolean encoderInverted,
      double encoderOffsetRotations) {
    this(
        "CANcoder " + encoderCanId,
        driveMotorCanId,
        steerMotorCanId,
        encoderCanId,
        driveMotorInverted,
        steerMotorInverted,
        encoderInverted,
        encoderOffsetRotations,
        false);
  }

  private SwerveModuleIOCTRE(
      String moduleName,
      int driveMotorCanId,
      int steerMotorCanId,
      int encoderCanId,
      boolean driveMotorInverted,
      boolean steerMotorInverted,
      boolean encoderInverted,
      double encoderOffsetRotations,
      boolean closedLoopEnabled) {
    driveMotor = new TalonFX(driveMotorCanId);
    steerMotor = new TalonFX(steerMotorCanId);
    absoluteEncoder = new CANcoder(encoderCanId);
    this.moduleName = moduleName;
    this.closedLoopEnabled = closedLoopEnabled;

    driveDutyCycleSignal = driveMotor.getDutyCycle();
    drivePositionSignal = driveMotor.getRotorPosition();
    driveVelocitySignal = driveMotor.getRotorVelocity();
    driveMechanismVelocitySignal = driveMotor.getVelocity();
    driveMechanismPositionSignal = driveMotor.getPosition();
    driveSupplyVoltageSignal = driveMotor.getSupplyVoltage();
    driveMotorVoltageSignal = driveMotor.getMotorVoltage();
    driveSupplyCurrentSignal = driveMotor.getSupplyCurrent();
    driveTorqueCurrentSignal = driveMotor.getTorqueCurrent();
    driveStatorCurrentSignal = driveMotor.getStatorCurrent();
    driveTemperatureSignal = driveMotor.getDeviceTemp();
    driveDutyCycleStatusSignal = driveMotor.getDutyCycle();
    driveControlModeSignal = driveMotor.getControlMode();
    driveClosedLoopReferenceSignal = driveMotor.getClosedLoopReference();
    driveClosedLoopOutputSignal = driveMotor.getClosedLoopOutput();
    driveClosedLoopErrorSignal = driveMotor.getClosedLoopError();
    driveClosedLoopSlotSignal = driveMotor.getClosedLoopSlot();
    driveDeviceEnableSignal = driveMotor.getDeviceEnable();
    driveMotorOutputStatusSignal = driveMotor.getMotorOutputStatus();

    steerDutyCycleSignal = steerMotor.getDutyCycle();
    steerPositionSignal = steerMotor.getRotorPosition();
    steerVelocitySignal = steerMotor.getRotorVelocity();
    steerMechanismVelocitySignal = steerMotor.getVelocity();
    steerMechanismPositionSignal = steerMotor.getPosition();
    steerSupplyVoltageSignal = steerMotor.getSupplyVoltage();
    steerMotorVoltageSignal = steerMotor.getMotorVoltage();
    steerSupplyCurrentSignal = steerMotor.getSupplyCurrent();
    steerTorqueCurrentSignal = steerMotor.getTorqueCurrent();
    steerStatorCurrentSignal = steerMotor.getStatorCurrent();
    steerTemperatureSignal = steerMotor.getDeviceTemp();
    steerControlModeSignal = steerMotor.getControlMode();
    steerClosedLoopReferenceSignal = steerMotor.getClosedLoopReference();
    steerClosedLoopOutputSignal = steerMotor.getClosedLoopOutput();
    steerClosedLoopErrorSignal = steerMotor.getClosedLoopError();
    steerClosedLoopSlotSignal = steerMotor.getClosedLoopSlot();
    steerDeviceEnableSignal = steerMotor.getDeviceEnable();
    steerMotorOutputStatusSignal = steerMotor.getMotorOutputStatus();

    encoderAbsolutePositionSignal = absoluteEncoder.getAbsolutePosition();
    encoderVelocitySignal = absoluteEncoder.getVelocity();

    stop();

    TalonFXConfiguration driveConfiguration =
        createDriveConfiguration(driveMotorInverted, closedLoopEnabled);
    TalonFXConfiguration steerConfiguration =
        createSteerConfiguration(steerMotorInverted, encoderCanId, closedLoopEnabled);
    CANcoderConfiguration encoderConfiguration =
        createEncoderConfiguration(encoderInverted, encoderOffsetRotations);

    StatusCode driveApplyStatus =
        driveMotor.getConfigurator().apply(driveConfiguration);
    StatusCode steerApplyStatus =
        steerMotor.getConfigurator().apply(steerConfiguration);
    StatusCode encoderApplyStatus =
        absoluteEncoder.getConfigurator().apply(encoderConfiguration);

    TalonFXConfiguration driveReadback = new TalonFXConfiguration();
    TalonFXConfiguration steerReadback = new TalonFXConfiguration();
    CANcoderConfiguration encoderReadback = new CANcoderConfiguration();

    StatusCode driveRefreshStatus =
        driveMotor.getConfigurator().refresh(driveReadback);
    StatusCode steerRefreshStatus =
        steerMotor.getConfigurator().refresh(steerReadback);
    StatusCode encoderRefreshStatus =
        absoluteEncoder.getConfigurator().refresh(encoderReadback);

    driveConfigurationComparison =
        compareDriveConfiguration(
            driveApplyStatus.isOK(),
            driveRefreshStatus.isOK(),
            driveConfiguration,
            driveReadback,
            closedLoopEnabled);
    DriveConfigurationComparison driveBaseConfigurationComparison =
        compareDriveConfiguration(
            driveApplyStatus.isOK(),
            driveRefreshStatus.isOK(),
            driveConfiguration,
            driveReadback,
            false);
    driveBaseConfigurationHealthy = driveBaseConfigurationComparison.healthy;
    driveClosedLoopConfigurationHealthy = driveConfigurationComparison.healthy;
    driveConfigurationHealthy = driveClosedLoopConfigurationHealthy;

    steerBaseConfigurationHealthy =
        steerApplyStatus.isOK()
            && steerRefreshStatus.isOK()
            && steerConfigurationMatches(steerConfiguration, steerReadback, false);
    steerClosedLoopConfigurationHealthy =
        steerApplyStatus.isOK()
            && steerRefreshStatus.isOK()
            && steerConfigurationMatches(steerConfiguration, steerReadback, closedLoopEnabled);
    steerConfigurationHealthy = steerClosedLoopConfigurationHealthy;

    encoderBaseConfigurationHealthy =
        encoderApplyStatus.isOK()
            && encoderRefreshStatus.isOK()
            && encoderConfigurationMatches(encoderConfiguration, encoderReadback);
    encoderConfigurationHealthy =
        encoderBaseConfigurationHealthy;

    reportDriveConfigurationFailure(
        moduleName,
        driveConfigurationComparison);
    reportTalonConfigurationFailure(
        moduleName,
        "steer",
        steerApplyStatus,
        steerRefreshStatus,
        steerConfiguration,
        steerReadback,
        steerConfigurationHealthy);
    reportEncoderConfigurationFailure(
        moduleName,
        encoderCanId,
        encoderApplyStatus,
        encoderRefreshStatus,
        encoderConfiguration,
        encoderReadback,
        encoderConfigurationHealthy);

    stop();
  }

  private static void reportTalonConfigurationFailure(
      String moduleName,
      String motorName,
      StatusCode applyStatus,
      StatusCode refreshStatus,
      TalonFXConfiguration expected,
      TalonFXConfiguration actual,
      boolean configurationHealthy) {
    if (configurationHealthy) {
      return;
    }

    DriverStation.reportError(
        String.format(
            Locale.ROOT,
            "Swerve %s %s TalonFX configuration unhealthy: apply=%s, refresh=%s, "
                + "expectedInverted=%s, actualInverted=%s, expectedFeedback=%s, "
                + "actualFeedback=%s, expectedRemoteId=%d, actualRemoteId=%d, "
                + "expectedRotorToSensorRatio=%.12f, actualRotorToSensorRatio=%.12f, "
                + "expectedSensorToMechanismRatio=%.12f, "
                + "actualSensorToMechanismRatio=%.12f, expectedContinuousWrap=%b, "
                + "actualContinuousWrap=%b, expectedSlot0=(%.6f,%.6f,%.6f), "
                + "actualSlot0=(%.6f,%.6f,%.6f), healthy=%b",
            moduleName,
            motorName,
            applyStatus,
            refreshStatus,
            expected.MotorOutput.Inverted,
            actual.MotorOutput.Inverted,
            expected.Feedback.FeedbackSensorSource,
            actual.Feedback.FeedbackSensorSource,
            expected.Feedback.FeedbackRemoteSensorID,
            actual.Feedback.FeedbackRemoteSensorID,
            expected.Feedback.RotorToSensorRatio,
            actual.Feedback.RotorToSensorRatio,
            expected.Feedback.SensorToMechanismRatio,
            actual.Feedback.SensorToMechanismRatio,
            expected.ClosedLoopGeneral.ContinuousWrap,
            actual.ClosedLoopGeneral.ContinuousWrap,
            expected.Slot0.kP,
            expected.Slot0.kI,
            expected.Slot0.kD,
            actual.Slot0.kP,
            actual.Slot0.kI,
            actual.Slot0.kD,
            configurationHealthy),
        false);
  }

  private static void reportDriveConfigurationFailure(
      String moduleName,
      DriveConfigurationComparison comparison) {
    if (comparison.healthy || !"Front Left".equals(moduleName)) {
      return;
    }

    DriverStation.reportWarning(
        String.format(
            Locale.ROOT,
            "S00_L17 DRIVE CONFIG FAIL: build=%s, %s",
            kDriveConfigurationFailureBuildMarker,
            comparison.formatFirstFailure()),
        false);
  }

  static TalonFXConfiguration createDriveConfiguration(
      boolean driveMotorInverted,
      boolean closedLoopEnabled) {
    TalonFXConfiguration configuration = new TalonFXConfiguration();
    configuration.MotorOutput.Inverted =
        driveMotorInverted
            ? InvertedValue.Clockwise_Positive
            : InvertedValue.CounterClockwise_Positive;
    configuration.CurrentLimits =
        new CurrentLimitsConfigs()
            .withSupplyCurrentLimit(SwerveConstants.kDriveSupplyCurrentLimitAmps)
            .withSupplyCurrentLimitEnable(
                SwerveConstants.kDriveSupplyCurrentLimitEnabled);
    if (closedLoopEnabled) {
      configuration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
      configuration.Feedback.RotorToSensorRatio = kRotationsPerTurn;
      configuration.Feedback.SensorToMechanismRatio = SwerveConstants.kDriveGearRatio;
      configuration.Slot0 = createDriveSlot0Configuration();
    }
    return configuration;
  }

  static TalonFXConfiguration createSteerConfiguration(
      boolean steerMotorInverted,
      int encoderCanId,
      boolean closedLoopEnabled) {
    TalonFXConfiguration configuration = new TalonFXConfiguration();
    configuration.MotorOutput.Inverted =
        steerMotorInverted
            ? InvertedValue.Clockwise_Positive
            : InvertedValue.CounterClockwise_Positive;
    configuration.CurrentLimits =
        new CurrentLimitsConfigs()
            .withStatorCurrentLimit(SwerveConstants.kSteerStatorCurrentLimitAmps)
            .withStatorCurrentLimitEnable(
                SwerveConstants.kSteerStatorCurrentLimitEnabled);
    if (closedLoopEnabled) {
      configuration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RemoteCANcoder;
      configuration.Feedback.FeedbackRemoteSensorID = encoderCanId;
      configuration.Feedback.RotorToSensorRatio = kRotationsPerTurn;
      configuration.Feedback.SensorToMechanismRatio = kRotationsPerTurn;
      configuration.ClosedLoopGeneral.ContinuousWrap = true;
      configuration.Slot0 = createSteerSlot0Configuration();
    }
    return configuration;
  }

  private static CANcoderConfiguration createEncoderConfiguration(
      boolean encoderInverted,
      double encoderOffsetRotations) {
    CANcoderConfiguration configuration = new CANcoderConfiguration();
    configuration.MagnetSensor =
        new MagnetSensorConfigs()
            .withMagnetOffset(encoderOffsetRotations)
            .withSensorDirection(
                encoderInverted
                    ? SensorDirectionValue.Clockwise_Positive
                    : SensorDirectionValue.CounterClockwise_Positive);
    return configuration;
  }

  static boolean driveConfigurationMatches(
      TalonFXConfiguration expected,
      TalonFXConfiguration actual,
      boolean closedLoopEnabled) {
    return compareDriveConfiguration(true, true, expected, actual, closedLoopEnabled).healthy;
  }

  static DriveConfigurationComparison compareDriveConfiguration(
      boolean applyStatusOK,
      boolean refreshStatusOK,
      TalonFXConfiguration expected,
      TalonFXConfiguration actual,
      boolean closedLoopEnabled) {
    List<ConfigurationFieldComparison> comparisons = new ArrayList<>();
    comparisons.add(
        ConfigurationFieldComparison.exact(
            "applyStatusOK", "true", Boolean.toString(applyStatusOK), applyStatusOK));
    comparisons.add(
        ConfigurationFieldComparison.exact(
            "refreshStatusOK", "true", Boolean.toString(refreshStatusOK), refreshStatusOK));
    comparisons.add(
        ConfigurationFieldComparison.exact(
            "inversionMatches",
            expected.MotorOutput.Inverted.toString(),
            actual.MotorOutput.Inverted.toString(),
            actual.MotorOutput.Inverted == expected.MotorOutput.Inverted));
    comparisons.add(
        ConfigurationFieldComparison.exact(
            "supplyCurrentLimitEnableMatches",
            Boolean.toString(expected.CurrentLimits.SupplyCurrentLimitEnable),
            Boolean.toString(actual.CurrentLimits.SupplyCurrentLimitEnable),
            actual.CurrentLimits.SupplyCurrentLimitEnable
                == expected.CurrentLimits.SupplyCurrentLimitEnable));
    comparisons.add(
        ConfigurationFieldComparison.numeric(
            "supplyCurrentLimitMatches",
            expected.CurrentLimits.SupplyCurrentLimit,
            actual.CurrentLimits.SupplyCurrentLimit,
            kBaseConfigurationComparisonTolerance));

    if (closedLoopEnabled) {
      comparisons.add(
          ConfigurationFieldComparison.exact(
              "feedbackSourceMatches",
              expected.Feedback.FeedbackSensorSource.toString(),
              actual.Feedback.FeedbackSensorSource.toString(),
              actual.Feedback.FeedbackSensorSource == expected.Feedback.FeedbackSensorSource));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "rotorToSensorRatioMatches",
              expected.Feedback.RotorToSensorRatio,
              actual.Feedback.RotorToSensorRatio,
              feedbackRatioTolerance(
                  expected.Feedback.RotorToSensorRatio, actual.Feedback.RotorToSensorRatio)));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "sensorToMechanismRatioMatches",
              expected.Feedback.SensorToMechanismRatio,
              actual.Feedback.SensorToMechanismRatio,
              feedbackRatioTolerance(
                  expected.Feedback.SensorToMechanismRatio,
                  actual.Feedback.SensorToMechanismRatio)));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "slot0KPMatches",
              expected.Slot0.kP,
              actual.Slot0.kP,
              kSlot0GainFloat32ReadbackTolerance));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "slot0KIMatches",
              expected.Slot0.kI,
              actual.Slot0.kI,
              kSlot0GainFloat32ReadbackTolerance));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "slot0KDMatches",
              expected.Slot0.kD,
              actual.Slot0.kD,
              kSlot0GainFloat32ReadbackTolerance));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "slot0KSMatches",
              expected.Slot0.kS,
              actual.Slot0.kS,
              kSlot0GainFloat32ReadbackTolerance));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "slot0KVMatches",
              expected.Slot0.kV,
              actual.Slot0.kV,
              kSlot0GainFloat32ReadbackTolerance));
      comparisons.add(
          ConfigurationFieldComparison.numeric(
              "slot0KAMatches",
              expected.Slot0.kA,
              actual.Slot0.kA,
              kSlot0GainFloat32ReadbackTolerance));
    }

    return new DriveConfigurationComparison(comparisons);
  }

  static final class DriveConfigurationComparison {
    private final List<ConfigurationFieldComparison> comparisons;
    final boolean healthy;
    final String firstFailingField;

    private DriveConfigurationComparison(List<ConfigurationFieldComparison> comparisons) {
      this.comparisons = List.copyOf(comparisons);
      boolean allComparisonsPassed = true;
      String firstFailure = "none";
      for (ConfigurationFieldComparison comparison : this.comparisons) {
        if (!comparison.passed && allComparisonsPassed) {
          firstFailure = comparison.fieldName;
        }
        allComparisonsPassed &= comparison.passed;
      }
      healthy = allComparisonsPassed;
      firstFailingField = firstFailure;
    }

    boolean passed(String fieldName) {
      return comparisons.stream()
          .filter(comparison -> comparison.fieldName.equals(fieldName))
          .findFirst()
          .map(comparison -> comparison.passed)
          .orElseThrow();
    }

    String formatFirstFailure() {
      for (ConfigurationFieldComparison comparison : comparisons) {
        if (!comparison.passed) {
          return comparison.formatDiagnostic();
        }
      }
      return "field=none, expected=none, actual=none, difference=0.000000000000, "
          + "tolerance=0.000000000000";
    }

    String formatFieldResults() {
      StringBuilder results = new StringBuilder();
      for (ConfigurationFieldComparison comparison : comparisons) {
        if (results.length() > 0) {
          results.append("; ");
        }
        results.append(comparison.format());
      }
      return results.toString();
    }
  }

  private static final class ConfigurationFieldComparison {
    private final String fieldName;
    private final String expectedValue;
    private final String actualValue;
    private final double numericDifference;
    private final double numericTolerance;
    private final boolean numeric;
    private final boolean passed;

    private ConfigurationFieldComparison(
        String fieldName,
        String expectedValue,
        String actualValue,
        double numericDifference,
        double numericTolerance,
        boolean numeric,
        boolean passed) {
      this.fieldName = fieldName;
      this.expectedValue = expectedValue;
      this.actualValue = actualValue;
      this.numericDifference = numericDifference;
      this.numericTolerance = numericTolerance;
      this.numeric = numeric;
      this.passed = passed;
    }

    static ConfigurationFieldComparison exact(
        String fieldName, String expectedValue, String actualValue, boolean passed) {
      return new ConfigurationFieldComparison(
          fieldName, expectedValue, actualValue, 0.0, 0.0, false, passed);
    }

    static ConfigurationFieldComparison numeric(
        String fieldName, double expectedValue, double actualValue, double tolerance) {
      double difference = actualValue - expectedValue;
      return new ConfigurationFieldComparison(
          fieldName,
          String.format(Locale.ROOT, "%.12f", expectedValue),
          String.format(Locale.ROOT, "%.12f", actualValue),
          difference,
          tolerance,
          true,
          Double.isFinite(expectedValue)
              && Double.isFinite(actualValue)
              && Math.abs(difference) <= tolerance);
    }

    String format() {
      if (numeric) {
        return String.format(
            Locale.ROOT,
            "%s expected=%s actual=%s difference(actual-expected)=%.12f tolerance=%.12f result=%s",
            fieldName,
            expectedValue,
            actualValue,
            numericDifference,
            numericTolerance,
            passed ? "PASS" : "FAIL");
      }
      return String.format(
          Locale.ROOT,
          "%s expected=%s actual=%s result=%s",
          fieldName,
          expectedValue,
          actualValue,
          passed ? "PASS" : "FAIL");
    }

    String formatDiagnostic() {
      return String.format(
          Locale.ROOT,
          "field=%s, expected=%s, actual=%s, difference=%.12f, tolerance=%.12f",
          fieldName,
          expectedValue,
          actualValue,
          numericDifference,
          numericTolerance);
    }
  }

  private static double feedbackRatioTolerance(double expected, double actual) {
    return Math.max(
        kFeedbackRatioAbsoluteTolerance,
        kFeedbackRatioRelativeTolerance * Math.max(Math.abs(expected), Math.abs(actual)));
  }

  static boolean steerConfigurationMatches(
      TalonFXConfiguration expected,
      TalonFXConfiguration actual,
      boolean closedLoopEnabled) {
    boolean baseConfigurationMatches =
        actual.MotorOutput.Inverted == expected.MotorOutput.Inverted
        && actual.CurrentLimits.StatorCurrentLimitEnable
            == expected.CurrentLimits.StatorCurrentLimitEnable
        && valuesMatch(
            actual.CurrentLimits.StatorCurrentLimit,
            expected.CurrentLimits.StatorCurrentLimit);
    return baseConfigurationMatches
        && (!closedLoopEnabled
            || (actual.Feedback.FeedbackSensorSource == expected.Feedback.FeedbackSensorSource
                && actual.Feedback.FeedbackRemoteSensorID
                    == expected.Feedback.FeedbackRemoteSensorID
                && valuesMatch(
                    actual.Feedback.RotorToSensorRatio,
                    expected.Feedback.RotorToSensorRatio)
                && valuesMatch(
                    actual.Feedback.SensorToMechanismRatio,
                    expected.Feedback.SensorToMechanismRatio)
                && actual.ClosedLoopGeneral.ContinuousWrap
                    == expected.ClosedLoopGeneral.ContinuousWrap
                && slot0Matches(actual.Slot0, expected.Slot0)));
  }

  private static Slot0Configs createDriveSlot0Configuration() {
    return new Slot0Configs()
        .withKP(SwerveConstants.kDriveVelocitySlot0KpVoltsPerRotationPerSecond)
        .withKI(SwerveConstants.kDriveVelocitySlot0KiVoltsPerRotation)
        .withKD(SwerveConstants.kDriveVelocitySlot0KdVoltsPerRotationPerSecondSquared)
        .withKV(SwerveConstants.kDriveVelocitySlot0KvVoltsPerRotationPerSecond);
  }

  private static Slot0Configs createSteerSlot0Configuration() {
    return new Slot0Configs()
        .withKP(SwerveConstants.kSteerPositionSlot0KpVoltsPerRotation)
        .withKI(SwerveConstants.kSteerPositionSlot0KiVoltsPerRotationSecond)
        .withKD(SwerveConstants.kSteerPositionSlot0KdVoltsPerRotationPerSecond);
  }

  private static boolean slot0Matches(Slot0Configs expected, Slot0Configs actual) {
    return valuesMatch(expected.kP, actual.kP)
        && valuesMatch(expected.kI, actual.kI)
        && valuesMatch(expected.kD, actual.kD)
        && valuesMatch(expected.kS, actual.kS)
        && valuesMatch(expected.kV, actual.kV)
        && valuesMatch(expected.kA, actual.kA);
  }

  static boolean encoderConfigurationMatches(
      CANcoderConfiguration expected,
      CANcoderConfiguration actual) {
    return actual.MagnetSensor.SensorDirection
            == expected.MagnetSensor.SensorDirection
        && magnetOffsetsMatch(
            expected.MagnetSensor.MagnetOffset,
            actual.MagnetSensor.MagnetOffset);
  }

  /**
   * Compares absolute-sensor offsets modulo one rotation because equivalent offsets can use
   * different wrapped representations.
   *
   * @param expected expected magnet offset in rotations
   * @param actual read-back magnet offset in rotations
   * @return whether the offsets are equivalent within the configured tolerance
   */
  private static boolean magnetOffsetsMatch(double expected, double actual) {
    return Math.abs(signedRotationDifference(expected, actual))
        <= kCANcoderMagnetOffsetQuantizationStepRotations;
  }

  private static double signedRotationDifference(double expected, double actual) {
    return Math.IEEEremainder(
        expected - actual,
        kRotationsPerTurn);
  }

  private static void reportEncoderConfigurationFailure(
      String moduleName,
      int encoderCanId,
      StatusCode applyStatus,
      StatusCode refreshStatus,
      CANcoderConfiguration expected,
      CANcoderConfiguration actual,
      boolean configurationHealthy) {
    if (configurationHealthy) {
      return;
    }

    double expectedOffset = expected.MagnetSensor.MagnetOffset;
    double actualOffset = actual.MagnetSensor.MagnetOffset;
    DriverStation.reportError(
        String.format(
            Locale.ROOT,
            "Swerve %s CANcoder %d configuration unhealthy: apply=%s, refresh=%s, "
                + "expectedDirection=%s, actualDirection=%s, expectedOffset=%.12f, "
                + "actualOffset=%.12f, rawDifference=%.12f, wrappedDifference=%.12f, healthy=%b",
            moduleName,
            encoderCanId,
            applyStatus,
            refreshStatus,
            expected.MagnetSensor.SensorDirection,
            actual.MagnetSensor.SensorDirection,
            expectedOffset,
            actualOffset,
            expectedOffset - actualOffset,
            signedRotationDifference(expectedOffset, actualOffset),
            configurationHealthy),
        false);
  }

  private static boolean valuesMatch(double expected, double actual) {
    return Math.abs(expected - actual) <= kBaseConfigurationComparisonTolerance;
  }

  static VelocityVoltage createDriveVelocityRequest(double initialVelocityRotationsPerSecond) {
    return new VelocityVoltage(initialVelocityRotationsPerSecond)
        .withSlot(kClosedLoopSlot)
        .withEnableFOC(false);
  }

  static PositionVoltage createSteerPositionRequest(double initialPositionRotations) {
    return new PositionVoltage(initialPositionRotations)
        .withSlot(kClosedLoopSlot)
        .withEnableFOC(false);
  }

  static VoltageOut createDriveStaticFrictionVoltageRequest(double initialVoltageVolts) {
    return new VoltageOut(initialVoltageVolts).withEnableFOC(false);
  }

  static double driveVelocityMetersPerSecondToWheelRotationsPerSecond(
      double velocityMetersPerSecond) {
    return velocityMetersPerSecond / kWheelCircumferenceMeters;
  }

  static double wheelRotationsPerSecondToDriveRotorRotationsPerSecond(
      double wheelRotationsPerSecond) {
    return wheelRotationsPerSecond * SwerveConstants.kDriveGearRatio;
  }

  static double normalizeSteerAngleRotations(Rotation2d angle) {
    Rotation2d acceptedAngle = Objects.requireNonNull(angle, "angle");
    return MathUtil.inputModulus(acceptedAngle.getRotations(), 0.0, kRotationsPerTurn);
  }

  static boolean closedLoopRequestAllowed(
      boolean closedLoopEnabled,
      boolean configurationHealthy) {
    return closedLoopEnabled && configurationHealthy;
  }

  static boolean openLoopRequestAllowed(boolean baseConfigurationHealthy, double output) {
    return output == kStoppedOutput || baseConfigurationHealthy;
  }

  static double clampFrontLeftDriveStaticFrictionVoltageVolts(double requestedVoltageVolts) {
    return MathUtil.clamp(
        requestedVoltageVolts,
        kStoppedOutput,
        SwerveConstants.kFrontLeftDriveStaticFrictionMaximumVoltageVolts);
  }

  static boolean frontLeftDriveStaticFrictionRequestAllowed(
      String moduleName,
      boolean driveBaseConfigurationHealthy,
      boolean testEnabled,
      boolean robotEnabled,
      double requestedVoltageVolts) {
    return "Front Left".equals(moduleName)
        && driveBaseConfigurationHealthy
        && testEnabled
        && robotEnabled
        && Double.isFinite(requestedVoltageVolts)
        && requestedVoltageVolts > kStoppedOutput;
  }

  static boolean driveStaticFrictionBreakawayDetected(double peakAbsoluteRotorVelocityRps) {
    return Double.isFinite(peakAbsoluteRotorVelocityRps)
        && peakAbsoluteRotorVelocityRps
            >= SwerveConstants
                .kFrontLeftDriveStaticFrictionBreakawayRotorVelocityRotationsPerSecond;
  }

  static boolean driveClosedLoopRequestAllowed(
      boolean closedLoopEnabled,
      boolean driveClosedLoopConfigurationHealthy,
      boolean encoderBaseConfigurationHealthy) {
    return closedLoopEnabled
        && driveClosedLoopConfigurationHealthy
        && encoderBaseConfigurationHealthy;
  }

  static boolean steerClosedLoopRequestAllowed(
      boolean closedLoopEnabled,
      boolean steerClosedLoopConfigurationHealthy,
      boolean encoderBaseConfigurationHealthy) {
    return closedLoopEnabled
        && steerClosedLoopConfigurationHealthy
        && encoderBaseConfigurationHealthy;
  }

  private boolean openLoopConfigurationHealthy() {
    return driveBaseConfigurationHealthy
        && steerBaseConfigurationHealthy
        && encoderBaseConfigurationHealthy;
  }

  /**
   * Updates raw motor and encoder signals without gear-ratio or offset conversion.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(SwerveModuleIOInputs inputs) {
    StatusCode driveRefreshStatus =
        BaseStatusSignal.refreshAll(
            driveDutyCycleSignal,
            drivePositionSignal,
            driveVelocitySignal,
            driveSupplyVoltageSignal,
            driveSupplyCurrentSignal,
            driveStatorCurrentSignal,
            driveTemperatureSignal);
    StatusCode steerRefreshStatus =
        BaseStatusSignal.refreshAll(
            steerDutyCycleSignal,
            steerPositionSignal,
            steerVelocitySignal,
            steerSupplyVoltageSignal,
            steerSupplyCurrentSignal,
            steerStatorCurrentSignal,
            steerTemperatureSignal);
    StatusCode encoderRefreshStatus =
        BaseStatusSignal.refreshAll(
            encoderAbsolutePositionSignal,
            encoderVelocitySignal);

    inputs.driveAppliedOutput = driveDutyCycleSignal.getValue();
    inputs.drivePositionRotations =
        drivePositionSignal.getValue().in(Units.Rotations);
    inputs.driveVelocityRotationsPerSecond =
        driveVelocitySignal.getValue().in(Units.RotationsPerSecond);
    inputs.driveSupplyVoltageVolts =
        driveSupplyVoltageSignal.getValue().in(Units.Volts);
    inputs.driveSupplyCurrentAmps =
        driveSupplyCurrentSignal.getValue().in(Units.Amps);
    inputs.driveStatorCurrentAmps =
        driveStatorCurrentSignal.getValue().in(Units.Amps);
    inputs.driveTemperatureCelsius =
        driveTemperatureSignal.getValue().in(Units.Celsius);

    inputs.steerAppliedOutput = steerDutyCycleSignal.getValue();
    inputs.steerPositionRotations =
        steerPositionSignal.getValue().in(Units.Rotations);
    inputs.steerVelocityRotationsPerSecond =
        steerVelocitySignal.getValue().in(Units.RotationsPerSecond);
    inputs.steerSupplyVoltageVolts =
        steerSupplyVoltageSignal.getValue().in(Units.Volts);
    inputs.steerSupplyCurrentAmps =
        steerSupplyCurrentSignal.getValue().in(Units.Amps);
    inputs.steerStatorCurrentAmps =
        steerStatorCurrentSignal.getValue().in(Units.Amps);
    inputs.steerTemperatureCelsius =
        steerTemperatureSignal.getValue().in(Units.Celsius);

    inputs.encoderAbsolutePositionRotations =
        encoderAbsolutePositionSignal.getValue().in(Units.Rotations);
    inputs.encoderVelocityRotationsPerSecond =
        encoderVelocitySignal.getValue().in(Units.RotationsPerSecond);

    inputs.driveConnected = driveRefreshStatus.isOK();
    inputs.steerConnected = steerRefreshStatus.isOK();
    inputs.encoderConnected = encoderRefreshStatus.isOK();
    lastDriveConnected = inputs.driveConnected;
    lastSteerConnected = inputs.steerConnected;
    lastEncoderConnected = inputs.encoderConnected;
    inputs.driveConfigurationHealthy = driveConfigurationHealthy;
    inputs.steerConfigurationHealthy = steerConfigurationHealthy;
    inputs.encoderConfigurationHealthy = encoderConfigurationHealthy;
    captureFrontLeftDriveStaticFrictionSample();
  }

  /**
   * Sets normalized open-loop drive output.
   *
   * @param output normalized output from -1.0 to 1.0
   */
  @Override
  public void setDriveOutput(double output) {
    double clampedOutput =
        MathUtil.clamp(output, kMinimumNormalizedOutput, kMaximumNormalizedOutput);
    if (!openLoopRequestAllowed(openLoopConfigurationHealthy(), output)) {
      stop();
      return;
    }
    driveMotor.setControl(driveOutputRequest.withOutput(clampedOutput));
  }

  /**
   * Sets normalized open-loop steer output.
   *
   * @param output normalized output from -1.0 to 1.0
   */
  @Override
  public void setSteerOutput(double output) {
    double clampedOutput =
        MathUtil.clamp(output, kMinimumNormalizedOutput, kMaximumNormalizedOutput);
    if (!openLoopRequestAllowed(openLoopConfigurationHealthy(), output)) {
      stop();
      return;
    }
    steerMotor.setControl(steerOutputRequest.withOutput(clampedOutput));
  }

  /**
   * Sets a Front Left-only drive wheel velocity request.
   *
   * <p>The configured SensorToMechanismRatio lets Phoenix interpret the request in wheel rotations
   * per second rather than raw Talon rotor rotations per second.
   *
   * @param velocityMetersPerSecond requested drive wheel velocity in meters per second
   */
  @Override
  public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
    boolean healthy =
        driveClosedLoopRequestAllowed(
            closedLoopEnabled,
            driveClosedLoopConfigurationHealthy,
            encoderBaseConfigurationHealthy);
    if (driveVelocityRequestRequiresFullModuleStop(healthy, velocityMetersPerSecond)) {
      stop();
      return;
    }

    double clampedVelocityMetersPerSecond =
        clampDriveVelocityMetersPerSecond(velocityMetersPerSecond);
    if (clampedVelocityMetersPerSecond == kStoppedOutput) {
      stopDriveMotorOnly();
      return;
    }

    double ctreTarget =
        driveVelocityMetersPerSecondToWheelRotationsPerSecond(
            clampedVelocityMetersPerSecond);
    StatusCode setControlStatus =
    driveMotor.setControl(
            driveVelocityRequest.withVelocity(ctreTarget));
  }

  /** Clamps a closed-loop drive request to the global pipeline wheel-speed limit. */
  static double clampDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
    return MathUtil.clamp(
        velocityMetersPerSecond,
        -SwerveConstants.kMaxWheelSpeedMetersPerSecond,
        SwerveConstants.kMaxWheelSpeedMetersPerSecond);
  }

  static boolean driveVelocityRequestRequiresFullModuleStop(
      boolean healthy, double velocityMetersPerSecond) {
    return !healthy || !Double.isFinite(velocityMetersPerSecond);
  }

  /**
   * Applies one manual Front Left static-friction characterization voltage request.
   *
   * <p>This deliberately uses only the S00_L16 drive-base health requirements. It is not a
   * closed-loop request and does not alter the existing VelocityVoltage path.
   *
   * @param voltageVolts requested positive drive voltage in volts
   */
  @Override
  public boolean setDriveStaticFrictionCharacterizationVoltageVolts(double voltageVolts) {
    frontLeftDriveStaticFrictionRequestedVoltageVolts = voltageVolts;
    frontLeftDriveStaticFrictionSetControlStatus = null;
    frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps = kStoppedOutput;
    frontLeftDriveStaticFrictionPeakAbsoluteMechanismVelocityRps = kStoppedOutput;
    frontLeftDriveStaticFrictionPeakAbsoluteSupplyCurrentAmps = kStoppedOutput;
    frontLeftDriveStaticFrictionPeakAbsoluteTorqueCurrentAmps = kStoppedOutput;
    if (!frontLeftDriveStaticFrictionRequestAllowed(
        moduleName,
        driveBaseConfigurationHealthy,
        DriverStation.isTestEnabled(),
        DriverStation.isEnabled(),
        voltageVolts)) {
      stopMotorsOnly();
      return false;
    }

    double clampedVoltageVolts = clampFrontLeftDriveStaticFrictionVoltageVolts(voltageVolts);
    if (clampedVoltageVolts == kStoppedOutput) {
      stopMotorsOnly();
      return false;
    }

    frontLeftDriveStaticFrictionRequestedVoltageVolts = clampedVoltageVolts;
    frontLeftDriveStaticFrictionCharacterizationActive = true;
    frontLeftDriveStaticFrictionSetControlStatus =
        driveMotor.setControl(driveStaticFrictionVoltageRequest.withOutput(clampedVoltageVolts));
    if (!frontLeftDriveStaticFrictionSetControlStatus.isOK()) {
      stopMotorsOnly();
      return false;
    }
    return true;
  }

  private void captureFrontLeftDriveStaticFrictionSample() {
    if (!frontLeftDriveStaticFrictionCharacterizationActive) {
      return;
    }

    BaseStatusSignal.refreshAll(
        driveMotorVoltageSignal,
        driveSupplyCurrentSignal,
        driveTorqueCurrentSignal,
        driveVelocitySignal,
        driveMechanismVelocitySignal);
    double rotorVelocityRps = driveVelocitySignal.getValue().in(Units.RotationsPerSecond);
    double mechanismVelocityRps =
        driveMechanismVelocitySignal.getValue().in(Units.RotationsPerSecond);
    double supplyCurrentAmps = driveSupplyCurrentSignal.getValue().in(Units.Amps);
    double torqueCurrentAmps = driveTorqueCurrentSignal.getValue().in(Units.Amps);
    frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps =
        Math.max(
            frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps,
            finiteAbsoluteValue(rotorVelocityRps));
    frontLeftDriveStaticFrictionPeakAbsoluteMechanismVelocityRps =
        Math.max(
            frontLeftDriveStaticFrictionPeakAbsoluteMechanismVelocityRps,
            finiteAbsoluteValue(mechanismVelocityRps));
    frontLeftDriveStaticFrictionPeakAbsoluteSupplyCurrentAmps =
        Math.max(
            frontLeftDriveStaticFrictionPeakAbsoluteSupplyCurrentAmps,
            finiteAbsoluteValue(supplyCurrentAmps));
    frontLeftDriveStaticFrictionPeakAbsoluteTorqueCurrentAmps =
        Math.max(
            frontLeftDriveStaticFrictionPeakAbsoluteTorqueCurrentAmps,
            finiteAbsoluteValue(torqueCurrentAmps));
  }

  @Override
  public void finishDriveStaticFrictionCharacterization(
      double requestedVoltageVolts,
      StaticFrictionStopReason stopReason) {
    if (!"Front Left".equals(moduleName)) {
      stopMotorsOnly();
      return;
    }

    StaticFrictionStopReason acceptedStopReason =
        Objects.requireNonNull(stopReason, "stopReason");
    if (!frontLeftDriveStaticFrictionCharacterizationActive) {
      frontLeftDriveStaticFrictionSetControlStatus = null;
      frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps = kStoppedOutput;
      frontLeftDriveStaticFrictionPeakAbsoluteMechanismVelocityRps = kStoppedOutput;
      frontLeftDriveStaticFrictionPeakAbsoluteSupplyCurrentAmps = kStoppedOutput;
      frontLeftDriveStaticFrictionPeakAbsoluteTorqueCurrentAmps = kStoppedOutput;
    }
    frontLeftDriveStaticFrictionRequestedVoltageVolts = requestedVoltageVolts;
    captureFrontLeftDriveStaticFrictionSample();
    System.out.println(
        String.format(
            Locale.ROOT,
            "[S00_L17] STATIC FRICTION RESULT: requestedVoltage=%.3f, setControl=%s, "
                + "peakRotorVelocity=%.6f, peakMechanismVelocity=%.6f, "
                + "peakSupplyCurrent=%.6f, peakTorqueCurrent=%.6f, breakaway=%b, stopReason=%s",
            frontLeftDriveStaticFrictionRequestedVoltageVolts,
            frontLeftDriveStaticFrictionSetControlStatus == null
                ? "notCalled"
                : frontLeftDriveStaticFrictionSetControlStatus,
            frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps,
            frontLeftDriveStaticFrictionPeakAbsoluteMechanismVelocityRps,
            frontLeftDriveStaticFrictionPeakAbsoluteSupplyCurrentAmps,
            frontLeftDriveStaticFrictionPeakAbsoluteTorqueCurrentAmps,
            driveStaticFrictionBreakawayDetected(
                frontLeftDriveStaticFrictionPeakAbsoluteRotorVelocityRps),
            acceptedStopReason.name().toLowerCase(Locale.ROOT)));
    frontLeftDriveStaticFrictionCharacterizationActive = false;
    stopMotorsOnly();
  }

  private static double finiteAbsoluteValue(double value) {
    return Double.isFinite(value) ? Math.abs(value) : kStoppedOutput;
  }


  /**
   * Sets a Front Left-only calibrated steer-angle request.
   *
   * @param angle requested calibrated module angle
   */
  @Override
  public void setSteerAngle(Rotation2d angle) {
    Rotation2d acceptedAngle = Objects.requireNonNull(angle, "angle");
    boolean healthy =
        steerClosedLoopRequestAllowed(
            closedLoopEnabled,
            steerClosedLoopConfigurationHealthy,
            encoderBaseConfigurationHealthy);
    double targetRotations = normalizeSteerAngleRotations(acceptedAngle);
    if (!closedLoopRequestAllowed(closedLoopEnabled, healthy)) {
      stop();
      return;
    }

    steerMotor.setControl(steerPositionRequest.withPosition(targetRotations));
  }

  /**
   * Stops both module motors.
   */
  @Override
  public void stop() {
    if (frontLeftDriveStaticFrictionCharacterizationActive) {
      finishDriveStaticFrictionCharacterization(
          frontLeftDriveStaticFrictionRequestedVoltageVolts,
          StaticFrictionStopReason.EXCEPTION);
      return;
    }
    stopMotorsOnly();
  }

  private void stopDriveMotorOnly() {
    stopDriveMotorOnly(driveMotor::stopMotor);
  }

  static void stopDriveMotorOnly(Runnable driveStopAction) {
    Objects.requireNonNull(driveStopAction, "driveStopAction").run();
  }

  private void stopMotorsOnly() {
    stopModuleMotors(driveMotor::stopMotor, steerMotor::stopMotor);
  }

  static void stopModuleMotors(Runnable driveStopAction, Runnable steerStopAction) {
    Objects.requireNonNull(driveStopAction, "driveStopAction").run();
    Objects.requireNonNull(steerStopAction, "steerStopAction").run();
  }
}
