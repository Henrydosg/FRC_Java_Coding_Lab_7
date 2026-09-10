// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.SwerveConstants;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import java.util.Objects;
import java.util.function.DoubleSupplier;

/**
 * Deterministic vendor-neutral Swerve module simulation at the existing IO boundary.
 *
 * <p>This intentionally models only idealized module kinematics. It is not a drivetrain, battery,
 * current, thermal, traction, or vendor-controller simulation.
 */
public final class SwerveModuleIOSim implements SwerveModuleIO {
  private static final double kMinimumNormalizedOutput = -1.0;
  private static final double kMaximumNormalizedOutput = 1.0;
  private static final double kStoppedOutput = 0.0;
  private static final double kMinimumWrappedRotation = 0.0;
  private static final double kMaximumWrappedRotation = 1.0;
  private static final double kMinimumSteerErrorRotations = -0.5;
  private static final double kMaximumSteerErrorRotations = 0.5;
  private static final double kMaximumSteerVelocityRotationsPerSecond = 1.0;
  private static final double kNominalSupplyVoltageVolts = 12.0;
  private static final double kNominalTemperatureCelsius = 25.0;
  private static final double kWheelCircumferenceMeters =
      2.0 * Math.PI * SwerveConstants.kWheelRadiusMeters;

  private enum DriveControlMode {
    STOPPED,
    OPEN_LOOP,
    VELOCITY
  }

  private enum SteerControlMode {
    STOPPED,
    OPEN_LOOP,
    POSITION
  }

  private final double physicalForwardRawDriveSign;
  private final DoubleSupplier timeSeconds;
  private final SwerveSimulationState simulationState;
  private final ModuleIdentity moduleIdentity;

  private DriveControlMode driveControlMode = DriveControlMode.STOPPED;
  private SteerControlMode steerControlMode = SteerControlMode.STOPPED;
  private double driveOpenLoopOutput;
  private double driveVelocityMetersPerSecond;
  private double steerOpenLoopOutput;
  private double steerTargetRotations;

  private double rawDriveRotorPositionRotations;
  private double rawDriveRotorVelocityRotationsPerSecond;
  private double wheelVelocityMetersPerSecond;
  private double moduleSteerPositionRotations;
  private double moduleSteerVelocityRotationsPerSecond;
  private double driveAppliedOutput;
  private double steerAppliedOutput;

  private boolean clockInitialized;
  private boolean clockHealthy = true;
  private double lastUpdateTimeSeconds;

  /**
   * Creates one module simulation using the FPGA monotonic clock.
   *
   * @param physicalForwardRawDriveSign raw rotor-position sign for physical-forward travel
   */
  public SwerveModuleIOSim(double physicalForwardRawDriveSign) {
    this(physicalForwardRawDriveSign, Timer::getFPGATimestamp, null, null);
  }

  /**
   * Creates one module simulation with an injected monotonic clock.
   *
   * @param physicalForwardRawDriveSign raw rotor-position sign for physical-forward travel
   * @param timeSeconds monotonic time source in seconds
   */
  public SwerveModuleIOSim(
      double physicalForwardRawDriveSign, DoubleSupplier timeSeconds) {
    this(physicalForwardRawDriveSign, timeSeconds, null, null);
  }

  /** Creates one shared-state module simulation using the FPGA monotonic clock. */
  public SwerveModuleIOSim(
      double physicalForwardRawDriveSign,
      SwerveSimulationState simulationState,
      ModuleIdentity moduleIdentity) {
    this(
        physicalForwardRawDriveSign,
        Timer::getFPGATimestamp,
        simulationState,
        moduleIdentity);
  }

  /** Creates one shared-state module simulation with an injected deterministic clock. */
  public SwerveModuleIOSim(
      double physicalForwardRawDriveSign,
      DoubleSupplier timeSeconds,
      SwerveSimulationState simulationState,
      ModuleIdentity moduleIdentity) {
    if (physicalForwardRawDriveSign != 1.0 && physicalForwardRawDriveSign != -1.0) {
      throw new IllegalArgumentException("Physical-forward raw drive sign must be +1.0 or -1.0");
    }
    if (!Double.isFinite(SwerveConstants.kDriveGearRatio)
        || SwerveConstants.kDriveGearRatio <= 0.0
        || !Double.isFinite(kWheelCircumferenceMeters)
        || kWheelCircumferenceMeters <= 0.0) {
      throw new IllegalStateException("Swerve simulation requires valid drive geometry and ratio");
    }
    this.physicalForwardRawDriveSign = physicalForwardRawDriveSign;
    this.timeSeconds = Objects.requireNonNull(timeSeconds, "timeSeconds");
    if ((simulationState == null) != (moduleIdentity == null)) {
      throw new IllegalArgumentException(
          "Shared simulation state and module identity must be supplied together");
    }
    this.simulationState = simulationState;
    this.moduleIdentity = moduleIdentity;
  }

  @Override
  public void updateInputs(SwerveModuleIOInputs inputs) {
    SwerveModuleIOInputs acceptedInputs = Objects.requireNonNull(inputs, "inputs");
    double nowSeconds = timeSeconds.getAsDouble();
    double elapsedSeconds = elapsedSeconds(nowSeconds);
    if (clockHealthy) {
      updateDrive(elapsedSeconds);
      updateSteer(elapsedSeconds);
    }
    publishSharedState();

    acceptedInputs.driveAppliedOutput = driveAppliedOutput;
    acceptedInputs.drivePositionRotations = rawDriveRotorPositionRotations;
    acceptedInputs.driveVelocityRotationsPerSecond =
        rawDriveRotorVelocityRotationsPerSecond;
    acceptedInputs.driveSupplyVoltageVolts =
        clockHealthy ? kNominalSupplyVoltageVolts : 0.0;
    acceptedInputs.driveSupplyCurrentAmps =
        Math.abs(driveAppliedOutput) * SwerveConstants.kDriveSupplyCurrentLimitAmps;
    acceptedInputs.driveStatorCurrentAmps = acceptedInputs.driveSupplyCurrentAmps;
    acceptedInputs.driveTemperatureCelsius = kNominalTemperatureCelsius;

    acceptedInputs.steerAppliedOutput = steerAppliedOutput;
    acceptedInputs.steerPositionRotations =
        moduleSteerPositionRotations * SwerveConstants.kSteerGearRatio;
    acceptedInputs.steerVelocityRotationsPerSecond =
        moduleSteerVelocityRotationsPerSecond * SwerveConstants.kSteerGearRatio;
    acceptedInputs.steerSupplyVoltageVolts =
        clockHealthy ? kNominalSupplyVoltageVolts : 0.0;
    acceptedInputs.steerSupplyCurrentAmps =
        Math.abs(steerAppliedOutput) * SwerveConstants.kSteerStatorCurrentLimitAmps;
    acceptedInputs.steerStatorCurrentAmps = acceptedInputs.steerSupplyCurrentAmps;
    acceptedInputs.steerTemperatureCelsius = kNominalTemperatureCelsius;

    acceptedInputs.encoderAbsolutePositionRotations =
        wrapRotations(moduleSteerPositionRotations);
    acceptedInputs.encoderVelocityRotationsPerSecond =
        moduleSteerVelocityRotationsPerSecond;

    acceptedInputs.driveConnected = clockHealthy;
    acceptedInputs.steerConnected = clockHealthy;
    acceptedInputs.encoderConnected = clockHealthy;
    acceptedInputs.driveConfigurationHealthy = clockHealthy;
    acceptedInputs.steerConfigurationHealthy = clockHealthy;
    acceptedInputs.encoderConfigurationHealthy = clockHealthy;
  }

  @Override
  public void setDriveOutput(double output) {
    if (!Double.isFinite(output)) {
      stop();
      return;
    }
    driveOpenLoopOutput =
        MathUtil.clamp(output, kMinimumNormalizedOutput, kMaximumNormalizedOutput);
    if (driveOpenLoopOutput == kStoppedOutput) {
      stopDrive();
      return;
    }
    driveControlMode = DriveControlMode.OPEN_LOOP;
  }

  @Override
  public void setSteerOutput(double output) {
    if (!Double.isFinite(output)) {
      stop();
      return;
    }
    steerOpenLoopOutput =
        MathUtil.clamp(output, kMinimumNormalizedOutput, kMaximumNormalizedOutput);
    if (steerOpenLoopOutput == kStoppedOutput) {
      stopSteer();
      return;
    }
    steerControlMode = SteerControlMode.OPEN_LOOP;
  }

  @Override
  public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
    if (!Double.isFinite(velocityMetersPerSecond)) {
      stop();
      return;
    }
    driveVelocityMetersPerSecond =
        MathUtil.clamp(
            velocityMetersPerSecond,
            -SwerveConstants.kMaxWheelSpeedMetersPerSecond,
            SwerveConstants.kMaxWheelSpeedMetersPerSecond);
    if (driveVelocityMetersPerSecond == kStoppedOutput) {
      stopDrive();
      return;
    }
    driveControlMode = DriveControlMode.VELOCITY;
  }

  /** Static-friction characterization is unsupported by this idealized kinematic simulation. */
  @Override
  public boolean setDriveStaticFrictionCharacterizationVoltageVolts(double voltageVolts) {
    stop();
    return false;
  }

  @Override
  public void setSteerAngle(Rotation2d angle) {
    Rotation2d acceptedAngle = Objects.requireNonNull(angle, "angle");
    if (!Double.isFinite(acceptedAngle.getRotations())) {
      stop();
      return;
    }
    steerTargetRotations = wrapRotations(acceptedAngle.getRotations());
    steerControlMode = SteerControlMode.POSITION;
  }

  @Override
  public void stop() {
    stopDrive();
    stopSteer();
  }

  private double elapsedSeconds(double nowSeconds) {
    if (!Double.isFinite(nowSeconds)) {
      clockHealthy = false;
      clockInitialized = false;
      stop();
      return 0.0;
    }
    if (!clockInitialized) {
      clockInitialized = true;
      clockHealthy = true;
      lastUpdateTimeSeconds = nowSeconds;
      return 0.0;
    }
    if (nowSeconds < lastUpdateTimeSeconds) {
      clockHealthy = false;
      lastUpdateTimeSeconds = nowSeconds;
      stop();
      return 0.0;
    }

    double elapsedSeconds = nowSeconds - lastUpdateTimeSeconds;
    lastUpdateTimeSeconds = nowSeconds;
    if (!Double.isFinite(elapsedSeconds)) {
      clockHealthy = false;
      stop();
      return 0.0;
    }
    clockHealthy = true;
    return elapsedSeconds;
  }

  private void updateDrive(double elapsedSeconds) {
    wheelVelocityMetersPerSecond =
        switch (driveControlMode) {
          case STOPPED -> 0.0;
          case OPEN_LOOP ->
              driveOpenLoopOutput * SwerveConstants.kMaxWheelSpeedMetersPerSecond;
          case VELOCITY -> driveVelocityMetersPerSecond;
        };
    driveAppliedOutput =
        wheelVelocityMetersPerSecond / SwerveConstants.kMaxWheelSpeedMetersPerSecond;
    rawDriveRotorVelocityRotationsPerSecond =
        physicalForwardRawDriveSign
            * wheelVelocityMetersPerSecond
            / kWheelCircumferenceMeters
            * SwerveConstants.kDriveGearRatio;
    rawDriveRotorPositionRotations +=
        rawDriveRotorVelocityRotationsPerSecond * elapsedSeconds;
  }

  private void updateSteer(double elapsedSeconds) {
    switch (steerControlMode) {
      case STOPPED -> {
        moduleSteerVelocityRotationsPerSecond = 0.0;
        steerAppliedOutput = 0.0;
      }
      case OPEN_LOOP -> {
        moduleSteerVelocityRotationsPerSecond =
            steerOpenLoopOutput * kMaximumSteerVelocityRotationsPerSecond;
        moduleSteerPositionRotations +=
            moduleSteerVelocityRotationsPerSecond * elapsedSeconds;
        steerAppliedOutput = steerOpenLoopOutput;
      }
      case POSITION -> updateSteerPositionControl(elapsedSeconds);
      default -> throw new IllegalStateException("Unsupported simulated steer control mode");
    }
  }

  private void updateSteerPositionControl(double elapsedSeconds) {
    if (elapsedSeconds == 0.0) {
      moduleSteerVelocityRotationsPerSecond = 0.0;
      steerAppliedOutput = 0.0;
      return;
    }
    double errorRotations =
        MathUtil.inputModulus(
            steerTargetRotations - moduleSteerPositionRotations,
            kMinimumSteerErrorRotations,
            kMaximumSteerErrorRotations);
    double maximumStepRotations =
        kMaximumSteerVelocityRotationsPerSecond * elapsedSeconds;
    double stepRotations =
        MathUtil.clamp(errorRotations, -maximumStepRotations, maximumStepRotations);
    moduleSteerPositionRotations += stepRotations;
    moduleSteerVelocityRotationsPerSecond = stepRotations / elapsedSeconds;
    steerAppliedOutput =
        moduleSteerVelocityRotationsPerSecond
            / kMaximumSteerVelocityRotationsPerSecond;
  }

  private void stopDrive() {
    driveControlMode = DriveControlMode.STOPPED;
    driveOpenLoopOutput = 0.0;
    driveVelocityMetersPerSecond = 0.0;
    wheelVelocityMetersPerSecond = 0.0;
    rawDriveRotorVelocityRotationsPerSecond = 0.0;
    driveAppliedOutput = 0.0;
  }

  private void stopSteer() {
    steerControlMode = SteerControlMode.STOPPED;
    steerOpenLoopOutput = 0.0;
    steerTargetRotations = wrapRotations(moduleSteerPositionRotations);
    moduleSteerVelocityRotationsPerSecond = 0.0;
    steerAppliedOutput = 0.0;
  }

  private static double wrapRotations(double rotations) {
    double wrappedRotations =
        MathUtil.inputModulus(
            rotations,
            kMinimumWrappedRotation,
            kMaximumWrappedRotation);
    return wrappedRotations >= kMaximumWrappedRotation
        ? kMinimumWrappedRotation
        : wrappedRotations;
  }

  private void publishSharedState() {
    if (simulationState != null) {
      simulationState.publish(
          moduleIdentity,
          wheelVelocityMetersPerSecond,
          Rotation2d.fromRotations(moduleSteerPositionRotations),
          clockHealthy);
    }
  }
}
