// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import edu.wpi.first.units.measure.Voltage;
import frc.robot.Constants.IntakeConstants;

/**
 * Controls the intake Talon FX with normalized open-loop output.
 */
public class IntakeIOTalonFX implements IntakeIO {
  private static final double kStoppedOutput = 0.0;

  private final TalonFX intakeMotor =
      new TalonFX(
          IntakeConstants.kIntakeMotorCanId,
          CANBus.roboRIO());

  private final StatusSignal<Double> dutyCycleSignal =
      intakeMotor.getDutyCycle();
  private final StatusSignal<Angle> rotorPositionSignal =
      intakeMotor.getRotorPosition();
  private final StatusSignal<AngularVelocity> rotorVelocitySignal =
      intakeMotor.getRotorVelocity();
  private final StatusSignal<Voltage> supplyVoltageSignal =
      intakeMotor.getSupplyVoltage();
  private final StatusSignal<Current> supplyCurrentSignal =
      intakeMotor.getSupplyCurrent();
  private final StatusSignal<Current> statorCurrentSignal =
      intakeMotor.getStatorCurrent();
  private final StatusSignal<Temperature> temperatureSignal =
      intakeMotor.getDeviceTemp();

  private final DutyCycleOut outputRequest =
      new DutyCycleOut(kStoppedOutput);

  private final boolean configurationHealthy;

  private double commandedOutput;

  /**
   * Creates the intake hardware in a safe stopped state.
   */
  public IntakeIOTalonFX() {
    TalonFXConfiguration configuration =
        new TalonFXConfiguration();

    configuration.MotorOutput.Inverted =
        IntakeConstants.kIntakeMotorInverted
            ? InvertedValue.Clockwise_Positive
            : InvertedValue.CounterClockwise_Positive;
    configuration.MotorOutput.NeutralMode =
        IntakeConstants.kIntakeBrakeModeEnabled
            ? NeutralModeValue.Brake
            : NeutralModeValue.Coast;
    configuration.MotorOutput.PeakForwardDutyCycle =
        IntakeConstants.kIntakePeakForwardDutyCycle;
    configuration.MotorOutput.PeakReverseDutyCycle =
        IntakeConstants.kIntakePeakReverseDutyCycle;

    configuration.CurrentLimits.SupplyCurrentLimit =
        IntakeConstants.kIntakeSupplyCurrentLimitAmps;
    configuration.CurrentLimits.SupplyCurrentLimitEnable =
        IntakeConstants.kIntakeSupplyCurrentLimitEnabled;
    configuration.CurrentLimits.StatorCurrentLimit =
        IntakeConstants.kIntakeStatorCurrentLimitAmps;
    configuration.CurrentLimits.StatorCurrentLimitEnable =
        IntakeConstants.kIntakeStatorCurrentLimitEnabled;

    configuration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod =
        IntakeConstants.kIntakeOpenLoopRampSeconds;

    StatusCode configurationStatus =
        intakeMotor
            .getConfigurator()
            .apply(configuration);
    configurationHealthy = configurationStatus.isOK();

    stop();
  }

  /**
   * Updates observations from one grouped hardware refresh.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    StatusCode refreshStatus =
        BaseStatusSignal.refreshAll(
            dutyCycleSignal,
            rotorPositionSignal,
            rotorVelocitySignal,
            supplyVoltageSignal,
            supplyCurrentSignal,
            statorCurrentSignal,
            temperatureSignal);

    inputs.appliedOutput = dutyCycleSignal.getValue();
    inputs.positionRotations =
        rotorPositionSignal
            .getValue()
            .in(Units.Rotations);
    inputs.velocityRpm =
        rotorVelocitySignal
            .getValue()
            .in(Units.RPM);
    inputs.supplyVoltageVolts =
        supplyVoltageSignal
            .getValue()
            .in(Units.Volts);
    inputs.supplyCurrentAmps =
        supplyCurrentSignal
            .getValue()
            .in(Units.Amps);
    inputs.statorCurrentAmps =
        statorCurrentSignal
            .getValue()
            .in(Units.Amps);
    inputs.temperatureCelsius =
        temperatureSignal
            .getValue()
            .in(Units.Celsius);
    inputs.connected = refreshStatus.isOK();
    inputs.configurationHealthy = configurationHealthy;
  }

  /**
   * Sets the normalized intake motor output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {
    commandedOutput =
        MathUtil.clamp(
            output,
            IntakeConstants.kMinimumIntakeOutput,
            IntakeConstants.kMaximumIntakeOutput);

    intakeMotor.setControl(
        outputRequest.withOutput(commandedOutput));
  }

  /**
   * Stops the intake motor.
   */
  @Override
  public void stop() {
    intakeMotor.stopMotor();
    commandedOutput = kStoppedOutput;
  }
}
