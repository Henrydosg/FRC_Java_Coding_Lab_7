// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.intake;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.MathUtil;
import frc.robot.Constants.IntakeConstants;

/**
 * Controls the intake Talon FX with normalized open-loop output.
 */
public class IntakeIOTalonFX implements IntakeIO {
  private static final double kStoppedOutput = 0.0;
  private static final double kUnsupportedPositionRotations = 0.0;
  private static final double kUnsupportedVelocityRpm = 0.0;

  private final TalonFX intakeMotor =
      new TalonFX(
          IntakeConstants.kIntakeMotorCanId,
          CANBus.roboRIO());

  private final DutyCycleOut outputRequest =
      new DutyCycleOut(kStoppedOutput);

  private double commandedOutput;

  /**
   * Creates the intake hardware in a safe stopped state.
   */
  public IntakeIOTalonFX() {
    stop();
  }

  /**
   * Updates observations without polling hardware status signals.
   *
   * <p>Encoder observation is deferred to a later lesson.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(IntakeIOInputs inputs) {
    inputs.appliedOutput = commandedOutput;
    inputs.positionRotations =
        kUnsupportedPositionRotations;
    inputs.velocityRpm =
        kUnsupportedVelocityRpm;
    inputs.connected = intakeMotor.isConnected();
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
