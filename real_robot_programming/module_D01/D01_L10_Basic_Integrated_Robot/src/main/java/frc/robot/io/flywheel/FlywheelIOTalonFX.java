// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.flywheel;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.Current;
import edu.wpi.first.units.measure.Temperature;
import frc.robot.Constants.FlywheelConstants;

/**
 * Controls the flywheel Talon FX with normalized open-loop output.
 */
public class FlywheelIOTalonFX implements FlywheelIO {
  private final TalonFX flywheelMotor =
      new TalonFX(
          FlywheelConstants.kFlywheelMotorCanId,
          CANBus.roboRIO());

  private final StatusSignal<Double> dutyCycleSignal =
      flywheelMotor.getDutyCycle();
  private final StatusSignal<AngularVelocity> rotorVelocitySignal =
      flywheelMotor.getRotorVelocity();
  private final StatusSignal<Current> supplyCurrentSignal =
      flywheelMotor.getSupplyCurrent();
  private final StatusSignal<Current> statorCurrentSignal =
      flywheelMotor.getStatorCurrent();
  private final StatusSignal<Temperature> temperatureSignal =
      flywheelMotor.getDeviceTemp();

  private final DutyCycleOut outputRequest =
      new DutyCycleOut(
          FlywheelConstants.kStoppedFlywheelOutput);

  private final boolean configurationHealthy;

  private double commandedOutput;

  /**
   * Creates and configures the flywheel hardware in a stopped state.
   */
  public FlywheelIOTalonFX() {
    TalonFXConfiguration configuration =
        new TalonFXConfiguration();

    configuration.MotorOutput.Inverted =
        FlywheelConstants.kFlywheelMotorInverted
            ? InvertedValue.Clockwise_Positive
            : InvertedValue.CounterClockwise_Positive;
    configuration.MotorOutput.NeutralMode =
        FlywheelConstants.kFlywheelBrakeModeEnabled
            ? NeutralModeValue.Brake
            : NeutralModeValue.Coast;
    configuration.MotorOutput.PeakForwardDutyCycle =
        FlywheelConstants.kFlywheelPeakForwardDutyCycle;
    configuration.MotorOutput.PeakReverseDutyCycle =
        FlywheelConstants.kFlywheelPeakReverseDutyCycle;

    configuration.CurrentLimits.SupplyCurrentLimit =
        FlywheelConstants.kFlywheelSupplyCurrentLimitAmps;
    configuration.CurrentLimits.SupplyCurrentLimitEnable =
        FlywheelConstants.kFlywheelSupplyCurrentLimitEnabled;
    configuration.CurrentLimits.StatorCurrentLimit =
        FlywheelConstants.kFlywheelStatorCurrentLimitAmps;
    configuration.CurrentLimits.StatorCurrentLimitEnable =
        FlywheelConstants.kFlywheelStatorCurrentLimitEnabled;

    configuration.OpenLoopRamps.DutyCycleOpenLoopRampPeriod =
        FlywheelConstants.kFlywheelOpenLoopRampSeconds;

    StatusCode configurationStatus =
        flywheelMotor
            .getConfigurator()
            .apply(configuration);
    configurationHealthy =
        configurationStatus.isOK();

    stop();
  }

  /**
   * Updates the flywheel observation snapshot.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(FlywheelIOInputs inputs) {
    StatusCode refreshStatus =
        BaseStatusSignal.refreshAll(
            dutyCycleSignal,
            rotorVelocitySignal,
            supplyCurrentSignal,
            statorCurrentSignal,
            temperatureSignal);

    inputs.appliedOutput =
        dutyCycleSignal.getValue();
    inputs.velocityRpm =
        rotorVelocitySignal
            .getValue()
            .in(Units.RPM);
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
    inputs.connected =
        refreshStatus.isOK();
    inputs.configurationHealthy =
        configurationHealthy;
  }

  /**
   * Sets the normalized flywheel motor output.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {
    commandedOutput =
        MathUtil.clamp(
            output,
            FlywheelConstants.kFlywheelPeakReverseDutyCycle,
            FlywheelConstants.kFlywheelPeakForwardDutyCycle);

    flywheelMotor.setControl(
        outputRequest.withOutput(commandedOutput));
  }

  /**
   * Stops the flywheel motor.
   */
  @Override
  public void stop() {
    flywheelMotor.stopMotor();
    commandedOutput =
        FlywheelConstants.kStoppedFlywheelOutput;
  }
}
