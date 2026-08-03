// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.FeederConstants;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIO.FeederIOInputs;
import frc.robot.observation.feeder.FeederObservation;
import frc.robot.observation.feeder.FeederObservation.FeederMode;
import java.util.Objects;

/**
 * Provides high-level feeder behavior.
 */
public class FeederSubsystem extends SubsystemBase {
  private final FeederIO io;
  private final FeederIOInputs inputs = new FeederIOInputs();
  private FeederMode mode = FeederMode.STOPPED;

  /**
   * Creates the feeder subsystem.
   *
   * @param io real, simulated, or fallback feeder hardware
   */
  public FeederSubsystem(FeederIO io) {
    this.io =
        Objects.requireNonNull(
            io,
            "io");
    stop();
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
  }

  /**
   * Runs the feeder at a bounded normalized output.
   *
   * @param output normalized motor output
   */
  public void setOutput(double output) {
    double safeOutput =
        MathUtil.clamp(
            output,
            FeederConstants.kFeederPeakReverseOutput,
            FeederConstants.kFeederPeakForwardOutput);

    io.setOutput(safeOutput);

    if (safeOutput > FeederConstants.kStoppedFeederOutput) {
      mode = FeederMode.FEEDING;
    } else if (safeOutput < FeederConstants.kStoppedFeederOutput) {
      mode = FeederMode.REVERSING;
    } else {
      mode = FeederMode.STOPPED;
    }
  }

  /**
   * Returns the latest feeder observation.
   *
   * @return latest feeder observation
   */
  public FeederObservation getObservation() {
    return new FeederObservation(
        inputs.appliedOutput,
        inputs.positionRotations,
        inputs.velocityRpm,
        inputs.supplyCurrentAmps,
        inputs.statorCurrentAmps,
        inputs.temperatureCelsius,
        inputs.connected,
        inputs.configurationHealthy,
        mode);
  }

  /**
   * Stops the feeder.
   */
  public void stop() {
    io.stop();
    mode = FeederMode.STOPPED;
  }
}
