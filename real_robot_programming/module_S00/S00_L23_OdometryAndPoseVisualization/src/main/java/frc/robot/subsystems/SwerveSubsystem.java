// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
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
  private static final double FIELD_HEADING_MIN_DEGREES = -180.0;
  private static final double FIELD_HEADING_MAX_DEGREES = 180.0;

  private enum ChassisReferenceFrame {
    ROBOT_RELATIVE,
    FIELD_RELATIVE
  }

  /** Explicit Front Left-only open-loop commissioning actions. */
  public enum FrontLeftCommissioningAction {
    DRIVE_POSITIVE,
    DRIVE_NEGATIVE,
    STEER_POSITIVE,
    STEER_NEGATIVE
  }

  /** Explicit Front Left-only closed-loop commissioning actions. */
  public enum FrontLeftClosedLoopCommissioningAction {
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
  private final SwerveDriveKinematics odometryKinematics =
      SwerveKinematics.createDriveKinematics();
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
  private Optional<Pose2d> currentPose = Optional.empty();
  private SwerveDriveOdometry odometry;
  private boolean odometryContinuityValid;
  private ChassisIntent chassisIntent = ChassisIntent.zero();
  private ChassisReferenceFrame chassisReferenceFrame = ChassisReferenceFrame.ROBOT_RELATIVE;
  /** True only after an accepted production chassis intent requests actuation. */
  private boolean productionIntentArmed;
  private boolean gyroSnapshotInitialized;
  private boolean fieldHeadingReferenceValid;
  private double capturedRawYawDegrees;
  private boolean frontLeftCommissioningActive;
  private boolean frontLeftStaticFrictionCommissioningActive;
  private boolean frontLeftDriveVelocityIOCalled;
  private double frontLeftCommissioningTimeoutSeconds;
  private double frontLeftStaticFrictionRequestedVoltageVolts = Double.NaN;

  /** Immutable chassis velocity intent snapshot in the selected reference frame. */
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
    gyroSnapshotInitialized = true;

    SwerveObservation.ModuleObservation frontLeftObservation =
        toModuleObservation(frontLeftInputs);
    SwerveObservation.ModuleObservation frontRightObservation =
        toModuleObservation(frontRightInputs);
    SwerveObservation.ModuleObservation backLeftObservation =
        toModuleObservation(backLeftInputs);
    SwerveObservation.ModuleObservation backRightObservation =
        toModuleObservation(backRightInputs);
    boolean poseMeasurementSampleValid =
        updateOdometry(
            frontLeftObservation,
            frontRightObservation,
            backLeftObservation,
            backRightObservation);

    latestObservation =
        Optional.of(
            new SwerveObservation(
                frontLeftObservation,
                frontRightObservation,
                backLeftObservation,
                backRightObservation,
                toGyroObservation(gyroInputs),
                toPoseObservation(poseMeasurementSampleValid)));

    if (!isGyroHeadingValid()) {
      if (productionIntentArmed
          && chassisReferenceFrame == ChassisReferenceFrame.FIELD_RELATIVE) {
        stop();
        return;
      }
    }

    updateFinalModuleStates();
    if (DriverStation.isEnabled()
        && !frontLeftCommissioningActive
        && productionIntentArmed) {
      dispatchFinalModuleStates();
    }
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
   * Returns a defensive snapshot of the current odometry pose.
   *
   * @return empty until odometry initializes from one complete valid measurement sample
   */
  public Optional<Pose2d> getCurrentPose() {
    return currentPose.map(SwerveSubsystem::copyPose);
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
    chassisIntent = toChassisIntent(acceptedSpeeds);
    chassisReferenceFrame = ChassisReferenceFrame.ROBOT_RELATIVE;
    productionIntentArmed = true;
    updateFinalModuleStates();
  }

  /**
   * Accepts field-relative chassis velocity intent using the latest subsystem-owned gyro snapshot.
   *
   * <p>Invalid heading data causes a full stop and disarms production intent. No zero-heading
   * substitution or robot-relative fallback is performed.
   *
   * @param fieldRelativeSpeeds field-relative velocity intent in meters per second and radians per
   *     second
   * @throws NullPointerException when fieldRelativeSpeeds is null
   */
  public void acceptFieldRelativeChassisSpeeds(ChassisSpeeds fieldRelativeSpeeds) {
    ChassisSpeeds acceptedSpeeds =
        Objects.requireNonNull(fieldRelativeSpeeds, "fieldRelativeSpeeds");
    if (!fieldHeadingReferenceValid || !isGyroHeadingValid()) {
      stop();
      return;
    }

    chassisIntent = toChassisIntent(acceptedSpeeds);
    chassisReferenceFrame = ChassisReferenceFrame.FIELD_RELATIVE;
    productionIntentArmed = true;
    updateFinalModuleStates();
  }

  /**
   * Captures the latest valid raw gyro yaw as the field-heading zero reference.
   *
   * <p>The capture is accepted only while Disabled and after at least one complete gyro input
   * refresh. A valid capture disarms any existing production intent and leaves the robot stopped;
   * the next field-relative request must be new.
   *
   * @return true when a new reference was captured; false when the mode, snapshot, or gyro validity
   *     contract rejected the capture
   */
  public boolean captureFieldHeadingReference() {
    if (DriverStation.isEnabled()) {
      return false;
    }
    if (!gyroSnapshotInitialized || !isGyroHeadingValid()) {
      stop();
      return false;
    }

    capturedRawYawDegrees = gyroInputs.yawDegrees;
    fieldHeadingReferenceValid = true;
    stop();
    return true;
  }

  /**
   * Reports whether a valid software field-heading reference is available.
   *
   * @return true after a valid Disabled-only capture and before invalidation
   */
  public boolean hasFieldHeadingReference() {
    return fieldHeadingReferenceValid;
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
   * Returns measured module positions in FrontLeft, FrontRight, BackLeft, BackRight order.
   *
   * <p>Drive position is converted from raw drive-rotor rotations to wheel rotations using the
   * configured drive ratio, then to meters using the configured wheel circumference. The angle is
   * the existing measured absolute-encoder angle used by the subsystem.
   *
   * <p>This method exposes measurement data for future odometry without owning odometry state.
   * The returned array is newly allocated for every call.
   *
   * @return measured module positions, or an empty array before the first observation refresh
   */
  public SwerveModulePosition[] getMeasuredModulePositions() {
    if (latestObservation.isEmpty()) {
      return new SwerveModulePosition[0];
    }

    SwerveObservation observation = latestObservation.orElseThrow();
    return toMeasuredModulePositions(
        observation.frontLeft(),
        observation.frontRight(),
        observation.backLeft(),
        observation.backRight());
  }

  private static SwerveModulePosition[] toMeasuredModulePositions(
      SwerveObservation.ModuleObservation frontLeftObservation,
      SwerveObservation.ModuleObservation frontRightObservation,
      SwerveObservation.ModuleObservation backLeftObservation,
      SwerveObservation.ModuleObservation backRightObservation) {
    return new SwerveModulePosition[] {
      toMeasuredModulePosition(
          frontLeftObservation, Constants.SwerveConstants.kFrontLeftDrivePositionSign),
      toMeasuredModulePosition(
          frontRightObservation, Constants.SwerveConstants.kFrontRightDrivePositionSign),
      toMeasuredModulePosition(
          backLeftObservation, Constants.SwerveConstants.kBackLeftDrivePositionSign),
      toMeasuredModulePosition(
          backRightObservation, Constants.SwerveConstants.kBackRightDrivePositionSign)
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

    double dutyCycle = commissioningDutyCycle(acceptedAction);
    if (!startFrontLeftCommissioningSession(
        Constants.SwerveConstants.kFrontLeftCommissioningPulseDurationSeconds)) {
      return false;
    }

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
   * Starts one fixed, Front Left-only closed-loop drive velocity request while Test mode is enabled.
   *
   * @param action explicit positive or negative drive action
   * @return true when the request was started; false when mode or ownership safety rejected it
   * @throws NullPointerException when action is null
   */
  public boolean startFrontLeftDriveVelocityCommissioning(
      FrontLeftClosedLoopCommissioningAction action) {
    FrontLeftClosedLoopCommissioningAction acceptedAction =
        Objects.requireNonNull(action, "action");
    if (acceptedAction != FrontLeftClosedLoopCommissioningAction.DRIVE_POSITIVE
        && acceptedAction != FrontLeftClosedLoopCommissioningAction.DRIVE_NEGATIVE) {
      throw new IllegalArgumentException("Drive velocity commissioning requires a drive action");
    }
    frontLeftDriveVelocityIOCalled = false;
    if (!startFrontLeftCommissioningSession(
        Constants.SwerveConstants.kFrontLeftClosedLoopCommissioningTimeoutSeconds)) {
      return false;
    }

    try {
      frontLeftDriveVelocityIOCalled = true;
      frontLeft.setDriveVelocityMetersPerSecond(
          clampedDriveVelocityMetersPerSecond(acceptedAction));
      return true;
    } catch (RuntimeException failure) {
      stopAfterFrontLeftCommissioningFailure(failure);
      throw failure;
    }
  }

  /** Returns whether the most recent Front Left drive request reached the module IO call site. */
  public boolean wasFrontLeftDriveVelocityIOCalled() {
    return frontLeftDriveVelocityIOCalled;
  }

  /**
   * Starts one fixed-voltage, Front Left-only static-friction characterization pulse in Test mode.
   *
   * <p>The wheel must be securely raised off the floor before this method is used. Each caller
   * supplies one manual voltage step; this method never sweeps voltages automatically.
   *
   * @param requestedVoltageVolts requested positive drive voltage in volts
   * @return true when the bounded characterization pulse was started
   */
  public boolean startFrontLeftDriveStaticFrictionCharacterization(
      double requestedVoltageVolts) {
    if (!DriverStation.isTestEnabled() || !DriverStation.isEnabled()) {
      stopFrontLeftStaticFrictionCharacterization(
          requestedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.REJECTED);
      return false;
    }

    double clampedVoltageVolts = clampFrontLeftDriveStaticFrictionVoltageVolts(requestedVoltageVolts);
    if (!Double.isFinite(requestedVoltageVolts) || clampedVoltageVolts <= 0.0) {
      stopFrontLeftStaticFrictionCharacterization(
          requestedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.REJECTED);
      return false;
    }
    if (!startFrontLeftCommissioningSession(
        Constants.SwerveConstants.kFrontLeftDriveStaticFrictionPulseDurationSeconds)) {
      stopFrontLeftStaticFrictionCharacterization(
          clampedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.REJECTED);
      return false;
    }

    try {
      if (!frontLeft.setDriveStaticFrictionCharacterizationVoltageVolts(clampedVoltageVolts)) {
        stopFrontLeftStaticFrictionCharacterization(
            clampedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.REJECTED);
        return false;
      }
      frontLeftStaticFrictionCommissioningActive = true;
      frontLeftStaticFrictionRequestedVoltageVolts = clampedVoltageVolts;
      return true;
    } catch (RuntimeException failure) {
      try {
        stopFrontLeftStaticFrictionCharacterization(
            clampedVoltageVolts, SwerveModuleIO.StaticFrictionStopReason.EXCEPTION);
      } catch (RuntimeException stopFailure) {
        failure.addSuppressed(stopFailure);
      }
      throw failure;
    }
  }

  /**
   * Starts one fixed, Front Left-only relative closed-loop steer request while Test mode is enabled.
   *
   * @param action explicit positive or negative steer action
   * @return true when the request was started; false when mode or ownership safety rejected it
   * @throws NullPointerException when action is null
   */
  public boolean startFrontLeftSteerAngleCommissioning(
      FrontLeftClosedLoopCommissioningAction action) {
    FrontLeftClosedLoopCommissioningAction acceptedAction =
        Objects.requireNonNull(action, "action");
    if (acceptedAction != FrontLeftClosedLoopCommissioningAction.STEER_POSITIVE
        && acceptedAction != FrontLeftClosedLoopCommissioningAction.STEER_NEGATIVE) {
      throw new IllegalArgumentException("Steer angle commissioning requires a steer action");
    }
    if (!startFrontLeftCommissioningSession(
        Constants.SwerveConstants.kFrontLeftClosedLoopCommissioningTimeoutSeconds)) {
      return false;
    }

    try {
      frontLeft.updateInputs(frontLeftInputs);
      Rotation2d currentAngle =
          Rotation2d.fromRotations(frontLeftInputs.encoderAbsolutePositionRotations);
      Rotation2d requestedStep = Rotation2d.fromRotations(steerStepRotations(acceptedAction));
      frontLeft.setSteerAngle(frontLeftRelativeSteerTarget(currentAngle, requestedStep));
      return true;
    } catch (RuntimeException failure) {
      stopAfterFrontLeftCommissioningFailure(failure);
      throw failure;
    }
  }

  /**
   * Stops both Front Left actuators without touching the other modules.
   */
  public void stopFrontLeftCommissioning() {
    if (frontLeftStaticFrictionCommissioningActive) {
      stopFrontLeftStaticFrictionCharacterization(
          frontLeftStaticFrictionRequestedVoltageVolts,
          implicitStaticFrictionStopReason());
      return;
    }
    try {
      frontLeft.stop();
    } finally {
      frontLeftCommissioningActive = false;
      frontLeftCommissioningTimeoutSeconds = 0.0;
      frontLeftCommissioningWatchdog.stop();
    }
  }

  /** Finalizes a manual Front Left static-friction pulse with its explicit stop reason. */
  public void stopFrontLeftStaticFrictionCharacterization(
      double requestedVoltageVolts,
      SwerveModuleIO.StaticFrictionStopReason stopReason) {
    try {
      frontLeft.finishDriveStaticFrictionCharacterization(requestedVoltageVolts, stopReason);
    } finally {
      frontLeftStaticFrictionCommissioningActive = false;
      frontLeftStaticFrictionRequestedVoltageVolts = Double.NaN;
      frontLeftCommissioningActive = false;
      frontLeftCommissioningTimeoutSeconds = 0.0;
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

  private static SwerveModulePosition toMeasuredModulePosition(
      SwerveObservation.ModuleObservation module,
      double physicalForwardSign) {
    double wheelRotations =
        physicalForwardSign
            * module.drivePositionRotations()
            / Constants.SwerveConstants.kDriveGearRatio;
    double distanceMeters =
        wheelRotations * (2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters);

    return new SwerveModulePosition(
        distanceMeters,
        Rotation2d.fromRotations(module.encoderAbsolutePositionRotations()));
  }

  private boolean updateOdometry(
      SwerveObservation.ModuleObservation frontLeftObservation,
      SwerveObservation.ModuleObservation frontRightObservation,
      SwerveObservation.ModuleObservation backLeftObservation,
      SwerveObservation.ModuleObservation backRightObservation) {
    Optional<Rotation2d> fieldHeading = currentFieldHeading();
    SwerveModulePosition[] modulePositions =
        toMeasuredModulePositions(
            frontLeftObservation,
            frontRightObservation,
            backLeftObservation,
            backRightObservation);
    if (fieldHeading.isEmpty()
        || !odometryMeasurementsValid(
            frontLeftObservation,
            frontRightObservation,
            backLeftObservation,
            backRightObservation,
            modulePositions)) {
      odometryContinuityValid = false;
      return false;
    }

    Rotation2d acceptedHeading = fieldHeading.orElseThrow();
    if (odometry == null) {
      Pose2d initialPose = new Pose2d(0.0, 0.0, acceptedHeading);
      odometry =
          new SwerveDriveOdometry(
              odometryKinematics,
              acceptedHeading,
              modulePositions,
              initialPose);
      currentPose = Optional.of(copyPose(initialPose));
      odometryContinuityValid = true;
      return true;
    }

    if (!odometryContinuityValid) {
      Pose2d heldPose = currentPose.orElseThrow();
      odometry =
          new SwerveDriveOdometry(
              odometryKinematics,
              acceptedHeading,
              modulePositions,
              heldPose);
      odometryContinuityValid = true;
      return true;
    }

    currentPose = Optional.of(copyPose(odometry.update(acceptedHeading, modulePositions)));
    return true;
  }

  private static boolean odometryMeasurementsValid(
      SwerveObservation.ModuleObservation frontLeftObservation,
      SwerveObservation.ModuleObservation frontRightObservation,
      SwerveObservation.ModuleObservation backLeftObservation,
      SwerveObservation.ModuleObservation backRightObservation,
      SwerveModulePosition[] modulePositions) {
    if (!moduleHealthy(frontLeftObservation)
        || !moduleHealthy(frontRightObservation)
        || !moduleHealthy(backLeftObservation)
        || !moduleHealthy(backRightObservation)
        || modulePositions.length != MODULE_COUNT) {
      return false;
    }

    for (SwerveModulePosition modulePosition : modulePositions) {
      if (modulePosition == null
          || modulePosition.angle == null
          || !Double.isFinite(modulePosition.distanceMeters)
          || !Double.isFinite(modulePosition.angle.getRadians())) {
        return false;
      }
    }
    return true;
  }

  private Optional<SwerveObservation.PoseObservation> toPoseObservation(
      boolean measurementSampleValid) {
    return currentPose.map(
        pose ->
            new SwerveObservation.PoseObservation(
                pose.getX(),
                pose.getY(),
                pose.getRotation().getRadians(),
                measurementSampleValid));
  }

  private static boolean moduleHealthy(SwerveObservation.ModuleObservation module) {
    return module.driveConnected()
        && module.steerConnected()
        && module.encoderConnected()
        && module.driveConfigurationHealthy()
        && module.steerConfigurationHealthy()
        && module.encoderConfigurationHealthy();
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
    chassisReferenceFrame = ChassisReferenceFrame.ROBOT_RELATIVE;
    productionIntentArmed = false;
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      finalModuleStates[moduleIndex] = new SwerveModuleState();
    }
    stopFrontLeftCommissioning();
    frontRight.stop();
    backLeft.stop();
    backRight.stop();
  }

  private void updateFinalModuleStates() {
    SwerveModuleState[] pipelineStates =
        outputPipeline.toModuleStates(toRobotRelativeChassisSpeeds(), currentModuleAngles());
    for (int moduleIndex = 0; moduleIndex < MODULE_COUNT; moduleIndex++) {
      finalModuleStates[moduleIndex] = copyState(pipelineStates[moduleIndex]);
    }
  }

  /** Dispatches the already optimized and desaturated states in fixed module order. */
  private void dispatchFinalModuleStates() {
    dispatchModuleState(frontLeft, finalModuleStates[0]);
    dispatchModuleState(frontRight, finalModuleStates[1]);
    dispatchModuleState(backLeft, finalModuleStates[2]);
    dispatchModuleState(backRight, finalModuleStates[3]);
  }

  private static void dispatchModuleState(
      SwerveModuleIO moduleIO, SwerveModuleState moduleState) {
    moduleIO.setDriveVelocityMetersPerSecond(moduleState.speedMetersPerSecond);
    moduleIO.setSteerAngle(moduleState.angle);
  }

  private static ChassisIntent toChassisIntent(ChassisSpeeds chassisSpeeds) {
    return new ChassisIntent(
        chassisSpeeds.vxMetersPerSecond,
        chassisSpeeds.vyMetersPerSecond,
        chassisSpeeds.omegaRadiansPerSecond);
  }

  private ChassisSpeeds toRobotRelativeChassisSpeeds() {
    ChassisSpeeds requestedSpeeds = new ChassisSpeeds(
        chassisIntent.vxMetersPerSecond(),
        chassisIntent.vyMetersPerSecond(),
        chassisIntent.omegaRadiansPerSecond());
    if (chassisReferenceFrame == ChassisReferenceFrame.ROBOT_RELATIVE) {
      return requestedSpeeds;
    }
    Optional<Rotation2d> fieldHeading = currentFieldHeading();
    if (fieldHeading.isEmpty()) {
      throw new IllegalStateException("Field-relative intent requires a valid gyro heading");
    }
    return ChassisSpeeds.fromFieldRelativeSpeeds(
        requestedSpeeds, fieldHeading.orElseThrow());
  }

  private Optional<Rotation2d> currentFieldHeading() {
    if (!fieldHeadingReferenceValid || !isGyroHeadingValid()) {
      return Optional.empty();
    }
    double fieldHeadingDegrees =
        MathUtil.inputModulus(
            gyroInputs.yawDegrees - capturedRawYawDegrees,
            FIELD_HEADING_MIN_DEGREES,
            FIELD_HEADING_MAX_DEGREES);
    return Optional.of(Rotation2d.fromDegrees(fieldHeadingDegrees));
  }

  private boolean isGyroHeadingValid() {
    return gyroSnapshotInitialized
        && gyroInputs.connected
        && gyroInputs.configurationHealthy
        && Double.isFinite(gyroInputs.yawDegrees);
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

  private static Pose2d copyPose(Pose2d pose) {
    return new Pose2d(
        pose.getX(),
        pose.getY(),
        new Rotation2d(pose.getRotation().getRadians()));
  }

  private void enforceFrontLeftCommissioningWatchdog() {
    if (frontLeftCommissioningActive
        && (!DriverStation.isTestEnabled()
            || frontLeftCommissioningWatchdog.hasElapsed(
                frontLeftCommissioningTimeoutSeconds))) {
      if (frontLeftStaticFrictionCommissioningActive) {
        SwerveModuleIO.StaticFrictionStopReason reason =
            !DriverStation.isEnabled()
                ? SwerveModuleIO.StaticFrictionStopReason.DISABLE
                : !DriverStation.isTestEnabled()
                    ? SwerveModuleIO.StaticFrictionStopReason.MODE_EXIT
                    : SwerveModuleIO.StaticFrictionStopReason.TIMEOUT;
        stopFrontLeftStaticFrictionCharacterization(
            frontLeftStaticFrictionRequestedVoltageVolts, reason);
      } else {
        stopFrontLeftCommissioning();
      }
    }
  }

  private SwerveModuleIO.StaticFrictionStopReason implicitStaticFrictionStopReason() {
    if (!DriverStation.isEnabled()) {
      return SwerveModuleIO.StaticFrictionStopReason.DISABLE;
    }
    if (!DriverStation.isTestEnabled()) {
      return SwerveModuleIO.StaticFrictionStopReason.MODE_EXIT;
    }
    return SwerveModuleIO.StaticFrictionStopReason.INTERRUPTED;
  }

  private boolean startFrontLeftCommissioningSession(double timeoutSeconds) {
    if (!DriverStation.isTestEnabled() || frontLeftCommissioningActive) {
      stopFrontLeftCommissioning();
      return false;
    }

    frontLeftCommissioningActive = true;
    productionIntentArmed = false;
    frontLeftCommissioningTimeoutSeconds = timeoutSeconds;
    frontLeftCommissioningWatchdog.restart();
    return true;
  }

  private void stopAfterFrontLeftCommissioningFailure(RuntimeException failure) {
    try {
      stopFrontLeftCommissioning();
    } catch (RuntimeException stopFailure) {
      failure.addSuppressed(stopFailure);
    }
  }

  private static double clampedDriveVelocityMetersPerSecond(
      FrontLeftClosedLoopCommissioningAction action) {
    double requestedVelocityMetersPerSecond =
        switch (action) {
          case DRIVE_POSITIVE ->
              Constants.SwerveConstants.kFrontLeftPositiveDriveTestVelocityMetersPerSecond;
          case DRIVE_NEGATIVE ->
              Constants.SwerveConstants.kFrontLeftNegativeDriveTestVelocityMetersPerSecond;
          default -> throw new IllegalArgumentException("Drive velocity commissioning requires a drive action");
        };
    return clampFrontLeftDriveVelocityMetersPerSecond(
        requestedVelocityMetersPerSecond);
  }

  static double clampFrontLeftDriveVelocityMetersPerSecond(
      double requestedVelocityMetersPerSecond) {
    return MathUtil.clamp(
        requestedVelocityMetersPerSecond,
        -Constants.SwerveConstants.kFrontLeftMaximumDriveVelocityMetersPerSecond,
        Constants.SwerveConstants.kFrontLeftMaximumDriveVelocityMetersPerSecond);
  }

  static double clampFrontLeftDriveStaticFrictionVoltageVolts(double requestedVoltageVolts) {
    return MathUtil.clamp(
        requestedVoltageVolts,
        0.0,
        Constants.SwerveConstants.kFrontLeftDriveStaticFrictionMaximumVoltageVolts);
  }

  private static double steerStepRotations(FrontLeftClosedLoopCommissioningAction action) {
    double requestedStepRotations =
        switch (action) {
          case STEER_POSITIVE ->
              Constants.SwerveConstants.kFrontLeftPositiveSteerTestStepRotations;
          case STEER_NEGATIVE ->
              Constants.SwerveConstants.kFrontLeftNegativeSteerTestStepRotations;
          default -> throw new IllegalArgumentException("Steer angle commissioning requires a steer action");
        };
    return limitFrontLeftSteerStep(Rotation2d.fromRotations(requestedStepRotations)).getRotations();
  }

  static Rotation2d limitFrontLeftSteerStep(Rotation2d requestedStep) {
    Rotation2d acceptedStep = Objects.requireNonNull(requestedStep, "requestedStep");
    return Rotation2d.fromRotations(
        MathUtil.clamp(
            acceptedStep.getRotations(),
            -Constants.SwerveConstants.kFrontLeftMaximumSteerStepRotations,
            Constants.SwerveConstants.kFrontLeftMaximumSteerStepRotations));
  }

  static Rotation2d frontLeftRelativeSteerTarget(
      Rotation2d currentAngle,
      Rotation2d requestedStep) {
    Rotation2d acceptedCurrentAngle = Objects.requireNonNull(currentAngle, "currentAngle");
    return acceptedCurrentAngle.plus(limitFrontLeftSteerStep(requestedStep));
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
