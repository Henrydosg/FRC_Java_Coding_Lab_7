// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.gyro;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.Snapshot;
import java.util.Objects;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import java.util.function.Function;

/** Derives idealized simulated yaw from coherent actual simulated Swerve module states. */
public final class GyroIOSim implements GyroIO {
  private final SwerveSimulationState simulationState;
  private final Function<SwerveModuleState[], ChassisSpeeds> inverseKinematics;
  private final DoubleSupplier timeSeconds;

  private boolean timingInitialized;
  private long lastIntegratedGeneration;
  private double lastUpdateTimeSeconds;
  private double yawDegrees;
  private double angularVelocityZDegreesPerSecond;
  private boolean healthy;

  /** Creates a gyro simulation using the FPGA monotonic clock. */
  public GyroIOSim(
      SwerveSimulationState simulationState,
      Function<SwerveModuleState[], ChassisSpeeds> inverseKinematics) {
    this(simulationState, inverseKinematics, Timer::getFPGATimestamp);
  }

  /** Creates a gyro simulation with an injected deterministic monotonic clock. */
  public GyroIOSim(
      SwerveSimulationState simulationState,
      Function<SwerveModuleState[], ChassisSpeeds> inverseKinematics,
      DoubleSupplier timeSeconds) {
    this.simulationState = Objects.requireNonNull(simulationState, "simulationState");
    this.inverseKinematics = Objects.requireNonNull(inverseKinematics, "inverseKinematics");
    this.timeSeconds = Objects.requireNonNull(timeSeconds, "timeSeconds");
  }

  @Override
  public void updateInputs(GyroIOInputs inputs) {
    GyroIOInputs acceptedInputs = Objects.requireNonNull(inputs, "inputs");
    double nowSeconds = timeSeconds.getAsDouble();
    Optional<Snapshot> snapshotOptional = simulationState.latestSnapshot();
    if (!Double.isFinite(nowSeconds) || snapshotOptional.isEmpty()) {
      failClosed();
      publishInputs(acceptedInputs);
      return;
    }

    Snapshot snapshot = snapshotOptional.orElseThrow();
    ChassisSpeeds simulatedSpeeds;
    try {
      simulatedSpeeds = inverseKinematics.apply(snapshot.moduleStates());
    } catch (RuntimeException failure) {
      failClosed();
      publishInputs(acceptedInputs);
      return;
    }
    if (!finite(simulatedSpeeds)) {
      failClosed();
      publishInputs(acceptedInputs);
      return;
    }

    double derivedAngularVelocityDegreesPerSecond =
        Math.toDegrees(simulatedSpeeds.omegaRadiansPerSecond);
    if (!Double.isFinite(derivedAngularVelocityDegreesPerSecond)) {
      failClosed();
      publishInputs(acceptedInputs);
      return;
    }

    if (!timingInitialized) {
      timingInitialized = true;
      lastIntegratedGeneration = snapshot.generation();
      lastUpdateTimeSeconds = nowSeconds;
      angularVelocityZDegreesPerSecond = derivedAngularVelocityDegreesPerSecond;
      healthy = true;
      publishInputs(acceptedInputs);
      return;
    }

    if (nowSeconds < lastUpdateTimeSeconds
        || snapshot.generation() < lastIntegratedGeneration) {
      failClosed();
      publishInputs(acceptedInputs);
      return;
    }

    double elapsedSeconds = nowSeconds - lastUpdateTimeSeconds;
    if (!Double.isFinite(elapsedSeconds)) {
      failClosed();
      publishInputs(acceptedInputs);
      return;
    }

    if (snapshot.generation() > lastIntegratedGeneration) {
      double nextYawDegrees =
          yawDegrees + derivedAngularVelocityDegreesPerSecond * elapsedSeconds;
      if (!Double.isFinite(nextYawDegrees)) {
        failClosed();
        publishInputs(acceptedInputs);
        return;
      }
      yawDegrees = nextYawDegrees;
      lastIntegratedGeneration = snapshot.generation();
      lastUpdateTimeSeconds = nowSeconds;
    }

    angularVelocityZDegreesPerSecond = derivedAngularVelocityDegreesPerSecond;
    healthy = true;
    publishInputs(acceptedInputs);
  }

  private void failClosed() {
    timingInitialized = false;
    angularVelocityZDegreesPerSecond = 0.0;
    healthy = false;
  }

  private void publishInputs(GyroIOInputs inputs) {
    inputs.yawDegrees = yawDegrees;
    inputs.pitchDegrees = 0.0;
    inputs.rollDegrees = 0.0;
    inputs.angularVelocityXDegreesPerSecond = 0.0;
    inputs.angularVelocityYDegreesPerSecond = 0.0;
    inputs.angularVelocityZDegreesPerSecond =
        healthy ? angularVelocityZDegreesPerSecond : 0.0;
    inputs.connected = healthy;
    inputs.configurationHealthy = healthy;
  }

  private static boolean finite(ChassisSpeeds speeds) {
    return speeds != null
        && Double.isFinite(speeds.vxMetersPerSecond)
        && Double.isFinite(speeds.vyMetersPerSecond)
        && Double.isFinite(speeds.omegaRadiansPerSecond);
  }
}
