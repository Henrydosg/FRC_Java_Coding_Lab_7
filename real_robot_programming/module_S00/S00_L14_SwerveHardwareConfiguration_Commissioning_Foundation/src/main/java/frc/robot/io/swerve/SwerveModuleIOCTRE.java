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
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.Constants.SwerveConstants;

/**
 * Reads and controls one CTRE-based Swerve module without interpreting module state.
 */
public class SwerveModuleIOCTRE implements SwerveModuleIO {
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
        SwerveConstants.kFrontLeftDriveCanId,
        SwerveConstants.kFrontLeftSteerCanId,
        SwerveConstants.kFrontLeftEncoderCanId);
  }

  /**
   * Creates the verified front-right module implementation.
   *
   * @return front-right CTRE module IO
   */
  public static SwerveModuleIOCTRE createFrontRight() {
    return new SwerveModuleIOCTRE(
        SwerveConstants.kFrontRightDriveCanId,
        SwerveConstants.kFrontRightSteerCanId,
        SwerveConstants.kFrontRightEncoderCanId);
  }

  /**
   * Creates the verified back-left module implementation.
   *
   * @return back-left CTRE module IO
   */
  public static SwerveModuleIOCTRE createBackLeft() {
    return new SwerveModuleIOCTRE(
        SwerveConstants.kBackLeftDriveCanId,
        SwerveConstants.kBackLeftSteerCanId,
        SwerveConstants.kBackLeftEncoderCanId);
  }

  /**
   * Creates the verified back-right module implementation.
   *
   * @return back-right CTRE module IO
   */
  public static SwerveModuleIOCTRE createBackRight() {
    return new SwerveModuleIOCTRE(
        SwerveConstants.kBackRightDriveCanId,
        SwerveConstants.kBackRightSteerCanId,
        SwerveConstants.kBackRightEncoderCanId);
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
      int encoderCanId) {
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

    driveConfigurationHealthy =
        driveMotor
            .getConfigurator()
            .refresh(new TalonFXConfiguration())
            .isOK();
    steerConfigurationHealthy =
        steerMotor
            .getConfigurator()
            .refresh(new TalonFXConfiguration())
            .isOK();
    encoderConfigurationHealthy =
        absoluteEncoder
            .getConfigurator()
            .refresh(new CANcoderConfiguration())
            .isOK();
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
