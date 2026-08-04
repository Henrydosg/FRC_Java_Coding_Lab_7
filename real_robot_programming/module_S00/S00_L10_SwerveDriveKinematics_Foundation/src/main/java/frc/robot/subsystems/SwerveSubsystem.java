// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
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
  private final SwerveModuleIO frontLeft;
  private final SwerveModuleIO frontRight;
  private final SwerveModuleIO backLeft;
  private final SwerveModuleIO backRight;
  private final GyroIO gyro;

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
    frontLeft.stop();
    frontRight.stop();
    backLeft.stop();
    backRight.stop();
  }
}
