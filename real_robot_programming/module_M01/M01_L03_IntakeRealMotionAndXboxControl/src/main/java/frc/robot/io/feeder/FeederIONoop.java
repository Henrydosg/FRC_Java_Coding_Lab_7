// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.feeder;

/** Deterministic safe Feeder implementation that never produces physical output. */
public final class FeederIONoop implements FeederIO {
  @Override
  public void updateInputs(FeederIOInputs inputs) {
    inputs.available = false;
    inputs.connected = false;
  }

  @Override
  public void requestFeed() {}

  @Override
  public void stop() {}
}
