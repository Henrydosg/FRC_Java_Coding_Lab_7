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
import com.ctre.phoenix6.configs.MagnetSensorConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.SensorDirectionValue;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants.SwerveConstants;
import java.util.Locale;

/**
 * Reads and controls one CTRE-based Swerve module without interpreting module state.
 */
public class SwerveModuleIOCTRE implements SwerveModuleIO {
  private static final double kConfigurationComparisonTolerance = 1.0e-9;
  private static final double kCANcoderMagnetOffsetQuantizationStepRotations = 1.0 / 4096.0;
  private static final double kRotationsPerTurn = 1.0;
  private static final double kMinimumNormalizedOutput = -1.0;
  private static final double kMaximumNormalizedOutput = 1.0;
  private static final double kStoppedOutput = 0.0;

  private final TalonFX driveMotor;
  private final TalonFX steerMotor;
  private final CANcoder absoluteEncoder;

  private final StatusSignal<Double> driveDutyCycleSignal;
  private final StatusSignal<Angle> drivePositionSignal;
  private final StatusSignal<AngularVelocity> driveVelocitySignal;
  private final StatusSignal<Voltage> driveSupplyVoltageSignal;
  private final StatusSignal<Current> driveSupplyCurrentSignal;
  private final StatusSignal<Current> driveStatorCurrentSignal;
  private final StatusSignal<Temperature> driveTemperatureSignal;

  private final StatusSignal<Double> steerDutyCycleSignal;
  private final StatusSignal<Angle> steerPositionSignal;
  private final StatusSignal<AngularVelocity> steerVelocitySignal;
  private final StatusSignal<Voltage> steerSupplyVoltageSignal;
  private final StatusSignal<Current> steerSupplyCurrentSignal;
  private final StatusSignal<Current> steerStatorCurrentSignal;
  private final StatusSignal<Temperature> steerTemperatureSignal;

  private final StatusSignal<Angle> encoderAbsolutePositionSignal;
  private final StatusSignal<AngularVelocity> encoderVelocitySignal;

  private final DutyCycleOut driveOutputRequest = new DutyCycleOut(kStoppedOutput);
  private final DutyCycleOut steerOutputRequest = new DutyCycleOut(kStoppedOutput);

  private final boolean driveConfigurationHealthy;
  private final boolean steerConfigurationHealthy;
  private final boolean encoderConfigurationHealthy;

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
        SwerveConstants.kFrontLeftEncoderOffsetRotations);
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
        SwerveConstants.kFrontRightEncoderOffsetRotations);
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
        SwerveConstants.kBackLeftEncoderOffsetRotations);
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
        SwerveConstants.kBackRightEncoderOffsetRotations);
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
        encoderOffsetRotations);
  }

  private SwerveModuleIOCTRE(
      String moduleName,
      int driveMotorCanId,
      int steerMotorCanId,
      int encoderCanId,
      boolean driveMotorInverted,
      boolean steerMotorInverted,
      boolean encoderInverted,
      double encoderOffsetRotations) {
    driveMotor = new TalonFX(driveMotorCanId);
    steerMotor = new TalonFX(steerMotorCanId);
    absoluteEncoder = new CANcoder(encoderCanId);

    driveDutyCycleSignal = driveMotor.getDutyCycle();
    drivePositionSignal = driveMotor.getRotorPosition();
    driveVelocitySignal = driveMotor.getRotorVelocity();
    driveSupplyVoltageSignal = driveMotor.getSupplyVoltage();
    driveSupplyCurrentSignal = driveMotor.getSupplyCurrent();
    driveStatorCurrentSignal = driveMotor.getStatorCurrent();
    driveTemperatureSignal = driveMotor.getDeviceTemp();

    steerDutyCycleSignal = steerMotor.getDutyCycle();
    steerPositionSignal = steerMotor.getRotorPosition();
    steerVelocitySignal = steerMotor.getRotorVelocity();
    steerSupplyVoltageSignal = steerMotor.getSupplyVoltage();
    steerSupplyCurrentSignal = steerMotor.getSupplyCurrent();
    steerStatorCurrentSignal = steerMotor.getStatorCurrent();
    steerTemperatureSignal = steerMotor.getDeviceTemp();

    encoderAbsolutePositionSignal = absoluteEncoder.getAbsolutePosition();
    encoderVelocitySignal = absoluteEncoder.getVelocity();

    stop();

    TalonFXConfiguration driveConfiguration =
        createDriveConfiguration(driveMotorInverted);
    TalonFXConfiguration steerConfiguration =
        createSteerConfiguration(steerMotorInverted);
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

    driveConfigurationHealthy =
        driveApplyStatus.isOK()
            && driveRefreshStatus.isOK()
            && driveConfigurationMatches(driveConfiguration, driveReadback);
    steerConfigurationHealthy =
        steerApplyStatus.isOK()
            && steerRefreshStatus.isOK()
            && steerConfigurationMatches(steerConfiguration, steerReadback);
    encoderConfigurationHealthy =
        encoderApplyStatus.isOK()
            && encoderRefreshStatus.isOK()
            && encoderConfigurationMatches(encoderConfiguration, encoderReadback);

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

  private static TalonFXConfiguration createDriveConfiguration(
      boolean driveMotorInverted) {
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
    return configuration;
  }

  private static TalonFXConfiguration createSteerConfiguration(
      boolean steerMotorInverted) {
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

  private static boolean driveConfigurationMatches(
      TalonFXConfiguration expected,
      TalonFXConfiguration actual) {
    return actual.MotorOutput.Inverted == expected.MotorOutput.Inverted
        && actual.CurrentLimits.SupplyCurrentLimitEnable
            == expected.CurrentLimits.SupplyCurrentLimitEnable
        && valuesMatch(
            actual.CurrentLimits.SupplyCurrentLimit,
            expected.CurrentLimits.SupplyCurrentLimit);
  }

  private static boolean steerConfigurationMatches(
      TalonFXConfiguration expected,
      TalonFXConfiguration actual) {
    return actual.MotorOutput.Inverted == expected.MotorOutput.Inverted
        && actual.CurrentLimits.StatorCurrentLimitEnable
            == expected.CurrentLimits.StatorCurrentLimitEnable
        && valuesMatch(
            actual.CurrentLimits.StatorCurrentLimit,
            expected.CurrentLimits.StatorCurrentLimit);
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
    return Math.abs(expected - actual) <= kConfigurationComparisonTolerance;
  }

  private boolean configurationHealthy() {
    return driveConfigurationHealthy
        && steerConfigurationHealthy
        && encoderConfigurationHealthy;
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
    inputs.driveConfigurationHealthy = driveConfigurationHealthy;
    inputs.steerConfigurationHealthy = steerConfigurationHealthy;
    inputs.encoderConfigurationHealthy = encoderConfigurationHealthy;
  }

  /**
   * Sets normalized open-loop drive output.
   *
   * @param output normalized output from -1.0 to 1.0
   */
  @Override
  public void setDriveOutput(double output) {
    if (!configurationHealthy() && output != kStoppedOutput) {
      stop();
      return;
    }
    driveMotor.setControl(
        driveOutputRequest.withOutput(
            MathUtil.clamp(
                output,
                kMinimumNormalizedOutput,
                kMaximumNormalizedOutput)));
  }

  /**
   * Sets normalized open-loop steer output.
   *
   * @param output normalized output from -1.0 to 1.0
   */
  @Override
  public void setSteerOutput(double output) {
    if (!configurationHealthy() && output != kStoppedOutput) {
      stop();
      return;
    }
    steerMotor.setControl(
        steerOutputRequest.withOutput(
            MathUtil.clamp(
                output,
                kMinimumNormalizedOutput,
                kMaximumNormalizedOutput)));
  }

  /**
   * Stops both module motors.
   */
  @Override
  public void stop() {
    driveMotor.stopMotor();
    steerMotor.stopMotor();
  }
}
