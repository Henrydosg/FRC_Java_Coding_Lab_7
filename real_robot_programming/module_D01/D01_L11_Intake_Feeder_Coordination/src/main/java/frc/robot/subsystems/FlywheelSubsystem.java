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
import frc.robot.Constants.FlywheelConstants;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIO.FlywheelIOInputs;
import frc.robot.observation.flywheel.FlywheelObservation;
import frc.robot.observation.flywheel.FlywheelObservation.FlywheelMode;
import java.util.Objects;

/**
 * Provides high-level flywheel behavior.
 */
public class FlywheelSubsystem extends SubsystemBase {
  private final FlywheelIO io;
  private final FlywheelIOInputs inputs = new FlywheelIOInputs();
  private FlywheelMode mode = FlywheelMode.STOPPED;

  /**
   * Creates the flywheel subsystem.
   *
   * @param io real, simulated, or fallback flywheel hardware
   */
  public FlywheelSubsystem(FlywheelIO io) {
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
   * Runs the flywheel at a normalized output.
   *
   * @param output normalized motor output
   */
  public void setOutput(double output) {
    double safeOutput =
        MathUtil.clamp(
            output,
            FlywheelConstants.kFlywheelPeakReverseDutyCycle,
            FlywheelConstants.kFlywheelPeakForwardDutyCycle);

    io.setOutput(safeOutput);

    if (safeOutput > FlywheelConstants.kStoppedFlywheelOutput) {
      mode = FlywheelMode.RUNNING;
    } else {
      mode = FlywheelMode.STOPPED;
    }
  }

  /**
   * Returns the latest flywheel observation.
   *
   * @return latest flywheel observation
   */
  public FlywheelObservation getObservation() {
    return new FlywheelObservation(
        inputs.appliedOutput,
        inputs.velocityRpm,
        inputs.supplyCurrentAmps,
        inputs.statorCurrentAmps,
        inputs.temperatureCelsius,
        inputs.connected,
        inputs.configurationHealthy,
        mode);
  }

  /**
   * Stops the flywheel.
   */
  public void stop() {
    io.stop();
    mode = FlywheelMode.STOPPED;
  }
}
