// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.SwerveObservation;
import java.util.Objects;
import java.util.Optional;

/**
 * Owns Swerve IO dependencies and refreshes their raw input snapshots.
 */
public class SwerveSubsystem extends SubsystemBase {
  private static final int MODULE_COUNT = 4;

  /** Explicit Front Left-only open-loop commissioning actions. */
  public enum FrontLeftCommissioningAction {
    DRIVE_POSITIVE,
    DRIVE_NEGATIVE,
    STEER_POSITIVE,
    STEER_NEGATIVE
  }

  private final SwerveModuleIO frontLeft;
  private final SwerveModuleIO frontRight;
  private final SwerveModuleIO backLeft;
  private final SwerveModuleIO backRight;
  private final GyroIO gyro;
  private final SwerveOutputPipeline outputPipeline = new SwerveOutputPipeline();
  private final Timer frontLeftCommissioningWatchdog = new Timer();

  /** Final module states owned by this subsystem in FL, FR, BL, BR order. */
  private final SwerveModuleState[] finalModuleStates = createZeroModuleStates();

  private final SwerveModuleIO.SwerveModuleIOInputs frontLeftInputs =
      new SwerveModuleIO.SwerveModuleIOInputs();
  private final SwerveModuleIO.SwerveModuleIOInputs frontRightInputs =
      new SwerveModuleIO.SwerveModuleIOInputs();
  private final SwerveModuleIO.SwerveModuleIOInputs backLeftInputs =
      new SwerveModuleIO.SwerveModuleIOInputs();
  private final SwerveModuleIO.SwerveModuleIOInputs backRightInputs =
      new SwerveModuleIO.SwerveModuleIOInputs();
  private final GyroIO.GyroIOInputs gyroInputs =
      new GyroIO.GyroIOInputs();

  private Optional<SwerveObservation> latestObservation = Optional.empty();
  private ChassisIntent chassisIntent = ChassisIntent.zero();
  private boolean frontLeftCommissioningActive;

  /** Immutable robot-relative chassis velocity intent snapshot. */
  private record ChassisIntent(
      double vxMetersPerSecond,
      double vyMetersPerSecond,
      double omegaRadiansPerSecond) {
    private static ChassisIntent zero() {
      return new ChassisIntent(0.0, 0.0, 0.0);
    }
  }

  /**
   * Creates the Swerve subsystem with vendor-neutral hardware dependencies.
   *
   * @param frontLeft front-left module IO
   * @param frontRight front-right module IO
   * @param backLeft back-left module IO
   * @param backRight back-right module IO
   * @param gyro gyro IO
   */
  public SwerveSubsystem(
      SwerveModuleIO frontLeft,
      SwerveModuleIO frontRight,
      SwerveModuleIO backLeft,
      SwerveModuleIO backRight,
      GyroIO gyro) {
    this.frontLeft = frontLeft;
    this.frontRight = frontRight;
    this.backLeft = backLeft;
    this.backRight = backRight;
    this.gyro = gyro;
  }

  /**
   * Refreshes each module and gyro input snapshot once per cycle.
   */
  @Override
  public void periodic() {
    enforceFrontLeftCommissioningWatchdog();
    frontLeft.updateInputs(frontLeftInputs);
    frontRight.updateInputs(frontRightInputs);
    backLeft.updateInputs(backLeftInputs);
    backRight.updateInputs(backRightInputs);
    gyro.updateInputs(gyroInputs);

    latestObservation =
        Optional.of(
            new SwerveObservation(
                toModuleObservation(frontLeftInputs),
                toModuleObservation(frontRightInputs),
                toModuleObservation(backLeftInputs),
                toModuleObservation(backRightInputs),
                toGyroObservation(gyroInputs)));

    updateFinalModuleStates();
  }

  /**
   * Returns the latest complete immutable observation.
   *
   * @return empty before the first complete periodic refresh
   */
  public Optional<SwerveObservation> getObservation() {
    return latestObservation;
  }

  /**
   * Accepts robot-relative chassis velocity intent without retaining the mutable WPILib value.
   *
   * @param chassisSpeeds robot-relative velocity intent in meters per second and radians per
   *     second
   * @throws NullPointerException when chassisSpeeds is null
   */
  public void acceptChassisSpeeds(ChassisSpeeds chassisSpeeds) {
    ChassisSpeeds acceptedSpeeds =
        Objects.requireNonNull(chassisSpeeds, "chassisSpeeds");
    chassisIntent =
        new ChassisIntent(
            acceptedSpeeds.vxMetersPerSecond,
            acceptedSpeeds.vyMetersPerSecond,
            acceptedSpeeds.omegaRadiansPerSecond);
    updateFinalModuleStates();
  }

  /**
   * Returns the latest final module states in FrontLeft, FrontRight, BackLeft, BackRight order.
   *
   * <p>The subsystem owns the stored states. This method returns a new array containing new state
   * objects so callers cannot mutate subsystem state through the returned value.
   *
   * @return defensive copies of the final module states
   */
  public SwerveModuleState[] getFinalModuleStates() {
    SwerveModuleState[] copiedStates = new SwerveModuleState[MODULE_COUNT];
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      copiedStates[moduleIndex] = copyState(finalModuleStates[moduleIndex]);
    }
    return copiedStates;
  }

  /**
   * Returns measured module states in FrontLeft, FrontRight, BackLeft, BackRight order.
   *
   * <p>Angles are raw absolute-encoder angles and are intentionally uncalibrated. The returned
   * array and every state object are newly allocated for each call.
   *
   * @return measured module states, or an empty array before the first observation refresh
   */
  public SwerveModuleState[] getMeasuredModuleStates() {
    if (latestObservation.isEmpty()) {
      return new SwerveModuleState[0];
    }

    SwerveObservation observation = latestObservation.orElseThrow();
    return new SwerveModuleState[] {
      toMeasuredModuleState(observation.frontLeft()),
      toMeasuredModuleState(observation.frontRight()),
      toMeasuredModuleState(observation.backLeft()),
      toMeasuredModuleState(observation.backRight())
    };
  }

  /**
   * Starts one fixed, Front Left-only commissioning output while Test mode is enabled.
   *
   * <p>The subsystem owns the fixed duty cycle, mutual exclusion, mode gate, and watchdog. The
   * commissioning command remains the preferred explicit scheduler interface, but this method is
   * independently bounded for defense in depth.
   *
   * @param action explicit Front Left drive or steer direction
   * @return true when the output was started; false when mode or ownership safety rejected it
   * @throws NullPointerException when action is null
   */
  public boolean startFrontLeftCommissioning(FrontLeftCommissioningAction action) {
    FrontLeftCommissioningAction acceptedAction =
        Objects.requireNonNull(action, "action");

    if (!DriverStation.isTestEnabled() || frontLeftCommissioningActive) {
      stopFrontLeftCommissioning();
      return false;
    }

    double dutyCycle = commissioningDutyCycle(acceptedAction);
    frontLeftCommissioningActive = true;
    frontLeftCommissioningWatchdog.restart();

    try {
      frontLeft.setDriveOutput(0.0);
      frontLeft.setSteerOutput(0.0);

      switch (acceptedAction) {
        case DRIVE_POSITIVE -> frontLeft.setDriveOutput(dutyCycle);
        case DRIVE_NEGATIVE -> frontLeft.setDriveOutput(-dutyCycle);
        case STEER_POSITIVE -> frontLeft.setSteerOutput(dutyCycle);
        case STEER_NEGATIVE -> frontLeft.setSteerOutput(-dutyCycle);
        default -> throw new IllegalStateException("Unsupported Front Left commissioning action");
      }
      return true;
    } catch (RuntimeException failure) {
      try {
        stopFrontLeftCommissioning();
      } catch (RuntimeException stopFailure) {
        failure.addSuppressed(stopFailure);
      }
      throw failure;
    }
  }

  /**
   * Stops both Front Left actuators without touching the other modules.
   */
  public void stopFrontLeftCommissioning() {
    try {
      frontLeft.stop();
    } finally {
      frontLeftCommissioningActive = false;
      frontLeftCommissioningWatchdog.stop();
    }
  }

  private static SwerveModuleState toMeasuredModuleState(
      SwerveObservation.ModuleObservation module) {
    double wheelSpeedMetersPerSecond =
        module.driveVelocityRotationsPerSecond()
            / Constants.SwerveConstants.kDriveGearRatio
            * (2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters);

    return new SwerveModuleState(
        wheelSpeedMetersPerSecond,
        Rotation2d.fromRotations(module.encoderAbsolutePositionRotations()));
  }

  private static SwerveObservation.ModuleObservation toModuleObservation(
      SwerveModuleIO.SwerveModuleIOInputs inputs) {
    return new SwerveObservation.ModuleObservation(
        inputs.driveAppliedOutput,
        inputs.drivePositionRotations,
        inputs.driveVelocityRotationsPerSecond,
        inputs.driveSupplyVoltageVolts,
        inputs.driveSupplyCurrentAmps,
        inputs.driveStatorCurrentAmps,
        inputs.driveTemperatureCelsius,
        inputs.steerAppliedOutput,
        inputs.steerPositionRotations,
        inputs.steerVelocityRotationsPerSecond,
        inputs.steerSupplyVoltageVolts,
        inputs.steerSupplyCurrentAmps,
        inputs.steerStatorCurrentAmps,
        inputs.steerTemperatureCelsius,
        inputs.encoderAbsolutePositionRotations,
        inputs.encoderVelocityRotationsPerSecond,
        inputs.driveConnected,
        inputs.steerConnected,
        inputs.encoderConnected,
        inputs.driveConfigurationHealthy,
        inputs.steerConfigurationHealthy,
        inputs.encoderConfigurationHealthy);
  }

  private static SwerveObservation.GyroObservation toGyroObservation(
      GyroIO.GyroIOInputs inputs) {
    return new SwerveObservation.GyroObservation(
        inputs.yawDegrees,
        inputs.pitchDegrees,
        inputs.rollDegrees,
        inputs.angularVelocityXDegreesPerSecond,
        inputs.angularVelocityYDegreesPerSecond,
        inputs.angularVelocityZDegreesPerSecond,
        inputs.connected,
        inputs.configurationHealthy);
  }

  /**
   * Stops every Swerve module.
   */
  public void stop() {
    chassisIntent = ChassisIntent.zero();
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      finalModuleStates[moduleIndex] = new SwerveModuleState();
    }
    frontLeft.stop();
    frontRight.stop();
    backLeft.stop();
    backRight.stop();
  }

  private void updateFinalModuleStates() {
    SwerveModuleState[] pipelineStates =
        outputPipeline.toModuleStates(toChassisSpeeds(), currentModuleAngles());
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      finalModuleStates[moduleIndex] = copyState(pipelineStates[moduleIndex]);
    }
  }

  private ChassisSpeeds toChassisSpeeds() {
    return new ChassisSpeeds(
        chassisIntent.vxMetersPerSecond(),
        chassisIntent.vyMetersPerSecond(),
        chassisIntent.omegaRadiansPerSecond());
  }

  private Rotation2d[] currentModuleAngles() {
    return new Rotation2d[] {
      Rotation2d.fromRotations(frontLeftInputs.encoderAbsolutePositionRotations),
      Rotation2d.fromRotations(frontRightInputs.encoderAbsolutePositionRotations),
      Rotation2d.fromRotations(backLeftInputs.encoderAbsolutePositionRotations),
      Rotation2d.fromRotations(backRightInputs.encoderAbsolutePositionRotations)
    };
  }

  private static SwerveModuleState[] createZeroModuleStates() {
    SwerveModuleState[] states = new SwerveModuleState[MODULE_COUNT];
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      states[moduleIndex] = new SwerveModuleState();
    }
    return states;
  }

  private static SwerveModuleState copyState(SwerveModuleState state) {
    return new SwerveModuleState(
        state.speedMetersPerSecond,
        new Rotation2d(state.angle.getRadians()));
  }

  private void enforceFrontLeftCommissioningWatchdog() {
    if (frontLeftCommissioningActive
        && (!DriverStation.isTestEnabled()
            || frontLeftCommissioningWatchdog.hasElapsed(
                Constants.SwerveConstants.kFrontLeftCommissioningPulseDurationSeconds))) {
      stopFrontLeftCommissioning();
    }
  }

  private static double commissioningDutyCycle(FrontLeftCommissioningAction action) {
    double maximumDutyCycle =
        switch (action) {
          case DRIVE_POSITIVE, DRIVE_NEGATIVE ->
              Constants.SwerveConstants.kFrontLeftDriveCommissioningDutyCycle;
          case STEER_POSITIVE, STEER_NEGATIVE ->
              Constants.SwerveConstants.kFrontLeftSteerCommissioningDutyCycle;
        };
    double requestedDutyCycle = maximumDutyCycle;
    return MathUtil.clamp(requestedDutyCycle, -maximumDutyCycle, maximumDutyCycle);
  }
}
