// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.feeder;

import com.revrobotics.PersistMode;
import com.revrobotics.REVLibError;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.FeederConstants;

/**
 * Controls the feeder Spark MAX and integrated NEO encoder on the roboRIO CAN bus.
 */
public class FeederIOSparkMax implements FeederIO {
  private static final double kStatorCurrentNotApplicableAmps = 0.0;

  private final SparkMax feederMotor =
      new SparkMax(
          FeederConstants.kFeederMotorCanId,
          MotorType.kBrushless);

  private final RelativeEncoder feederEncoder =
      feederMotor.getEncoder();

  private final boolean configurationHealthy;

  /**
   * Creates and configures the feeder hardware in a safe stopped state.
   */
  public FeederIOSparkMax() {
    SparkMaxConfig configuration =
        new SparkMaxConfig();

    configuration
        .smartCurrentLimit(
            FeederConstants.kFeederSupplyCurrentLimitAmps)
        .idleMode(
            FeederConstants.kFeederBrakeModeEnabled
                ? IdleMode.kBrake
                : IdleMode.kCoast)
        .openLoopRampRate(
            FeederConstants.kFeederOpenLoopRampSeconds)
        .inverted(
            FeederConstants.kFeederMotorInverted);

    configurationHealthy =
        feederMotor.configure(
                configuration,
                ResetMode.kResetSafeParameters,
                PersistMode.kPersistParameters)
            == REVLibError.kOk;

    stop();
  }

  /**
   * Updates feeder observations from the Spark MAX and integrated encoder.
   *
   * <p>REVLib does not expose a separate stator-current observation for Spark MAX. The contract
   * therefore reports stator current deterministically as 0.0 amperes.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(FeederIOInputs inputs) {
    inputs.appliedOutput = feederMotor.getAppliedOutput();
    inputs.positionRotations = feederEncoder.getPosition();
    inputs.velocityRpm = feederEncoder.getVelocity();
    inputs.supplyCurrentAmps = feederMotor.getOutputCurrent();
    inputs.statorCurrentAmps =
        kStatorCurrentNotApplicableAmps;
    inputs.temperatureCelsius =
        feederMotor.getMotorTemperature();
    inputs.connected =
        feederMotor.getLastError() == REVLibError.kOk;
    inputs.configurationHealthy = configurationHealthy;
  }

  /**
   * Sends a bounded normalized output to the feeder motor.
   *
   * @param output normalized motor output from -1.0 to 1.0
   */
  @Override
  public void setOutput(double output) {
    feederMotor.set(
        MathUtil.clamp(
            output,
            FeederConstants.kFeederPeakReverseOutput,
            FeederConstants.kFeederPeakForwardOutput));
  }

  /**
   * Stops the feeder motor.
   */
  @Override
  public void stop() {
    feederMotor.stopMotor();
  }
}
