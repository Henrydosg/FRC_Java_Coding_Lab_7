// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import java.util.List;

/**
 * Stores robot-wide configuration constants.
 */
public final class Constants {
  private Constants() {}

  /** Stores the normalized driver-input processing contract. */
  public static final class DriverInputConstants {
    /** USB port used by the L19 Xbox input source. */
    public static final int kXboxControllerPort = 0;

    /** Deadband applied independently to each semantic driver axis. */
    public static final double kAxisDeadband = 0.10;

    /** Minimum normalized processed-intent value. */
    public static final double kNormalizedMinimum = -1.0;

    /** Maximum normalized processed-intent value. */
    public static final double kNormalizedMaximum = 1.0;

    private DriverInputConstants() {}
  }

  /** Stores provisional field-frame values used only for L24 learning exercises. */
  public static final class FieldConstants {
    /**
     * Provisional learning-frame origin, not an official competition starting coordinate.
     *
     * <p>The frame is established by the existing Disabled field-heading capture.
     */
    public static final Pose2d kLearningStartingPose = Pose2d.kZero;

    private FieldConstants() {}
  }

  /**
   * Stores the official 2026 field dimensions required for explicit alliance transforms.
   *
   * <p>The values are from the installed WPILib 2026.2.1 AprilTag field-layout resources
   * {@code 2026-rebuilt-welded.json} and {@code 2026-rebuilt-andymark.json}. No default field
   * variant is selected because callers must make the field construction explicit before an
   * alliance transform is applied.
   */
  public static final class FieldTransformConstants {
    /** Enumerates the official 2026 rebuilt field construction variants. */
    public enum FieldVariant {
      /** Rebuilt welded 2026 field: 16.541 m long by 8.069 m wide. */
      REBUILT_WELDED(16.541, 8.069),

      /** Rebuilt AndyMark 2026 field: 16.518 m long by 8.043 m wide. */
      REBUILT_ANDYMARK(16.518, 8.043);

      private final double fieldLengthMeters;
      private final double fieldWidthMeters;

      FieldVariant(double fieldLengthMeters, double fieldWidthMeters) {
        this.fieldLengthMeters = fieldLengthMeters;
        this.fieldWidthMeters = fieldWidthMeters;
      }

      /** Returns the official field length for this construction variant, in meters. */
      public double fieldLengthMeters() {
        return fieldLengthMeters;
      }

      /** Returns the official field width for this construction variant, in meters. */
      public double fieldWidthMeters() {
        return fieldWidthMeters;
      }
    }

    private FieldTransformConstants() {}
  }

  /** Stores L03 trajectory-generation learning constraints. */
  public static final class TrajectoryGenerationConstants {
    /** Interior field-relative waypoint for the L03 learning trajectory, in meters. */
    public static final List<Translation2d> kLearningInteriorWaypoints =
        List.of(new Translation2d(0.50, 0.25));

    /** Terminal field-relative pose for the L03 learning trajectory. */
    public static final Pose2d kLearningGoalPose =
        new Pose2d(1.00, 0.00, new Rotation2d());

    /** Maximum trajectory-generation velocity, in meters per second. */
    public static final double kMaxVelocityMetersPerSecond = 1.0;

    /** Maximum trajectory-generation acceleration, in meters per second squared. */
    public static final double kMaxAccelerationMetersPerSecondSquared = 1.0;

    private TrajectoryGenerationConstants() {}
  }

  /** Stores autonomous command lifecycle configuration. */
  public static final class AutonomousConstants {
    /** Simulation learning baseline robot-relative forward speed, in meters per second. */
    public static final double kBoundedRobotRelativeForwardSpeedMetersPerSecond = 0.30;

    /** Simulation learning baseline robot-relative lateral speed, in meters per second. */
    public static final double kBoundedRobotRelativeLateralSpeedMetersPerSecond = 0.0;

    /** Simulation learning baseline robot-relative angular speed, in radians per second. */
    public static final double kBoundedRobotRelativeAngularSpeedRadiansPerSecond = 0.0;

    /** Simulation learning baseline bounded-motion duration, in seconds. */
    public static final double kBoundedRobotRelativeMotionDurationSeconds = 1.0;

    /**
     * Bounded interval for one repeated zero-motion safety-hold lifecycle, in seconds.
     *
     * <p>This interval is not the autonomous-mode ownership limit. The repeated command retains
     * the drivetrain requirement until Robot mode transition cancels it.
     */
    public static final double kSafetyHoldLifecycleDurationSeconds = 1.0;

    private AutonomousConstants() {}
  }

  /** Stores the conservative L02 pose-targeted autonomous learning configuration. */
  public static final class PoseTargetedAutonomousConstants {
    /** One small field-relative learning target from the inherited known origin. */
    public static final Pose2d kLearningTargetPose =
        new Pose2d(0.40, 0.0, new Rotation2d());

    /** Translation proportional gain, in inverse seconds. */
    public static final double kTranslationKpPerSecond = 1.0;

    /** Heading proportional gain, in inverse seconds. */
    public static final double kHeadingKpPerSecond = 1.0;

    /** Maximum field-relative translation speed, in meters per second. */
    public static final double kMaxTranslationSpeedMetersPerSecond = 0.20;

    /** Maximum angular speed, in radians per second. */
    public static final double kMaxAngularSpeedRadiansPerSecond = 0.35;

    /** Simultaneous translation completion tolerance, in meters. */
    public static final double kTranslationToleranceMeters = 0.03;

    /** Simultaneous heading completion tolerance, in radians. */
    public static final double kHeadingToleranceRadians = Units.degreesToRadians(2.0);

    /** Maximum pose-targeted motion time, in seconds. */
    public static final double kTimeoutSeconds = 4.0;

    private PoseTargetedAutonomousConstants() {}
  }

  /** Stores conservative L05 holonomic trajectory-following learning configuration. */
  public static final class HolonomicTrajectoryFollowingConstants {
    /** Provisional Simulation field construction; physical use requires field confirmation. */
    public static final FieldTransformConstants.FieldVariant kLearningFieldVariant =
        FieldTransformConstants.FieldVariant.REBUILT_WELDED;

    /** Fixed canonical robot-heading target for the learning trajectory. */
    public static final Rotation2d kCanonicalHolonomicHeading = Rotation2d.kZero;

    public static final double kXKpPerSecond = 1.0;
    public static final double kYKpPerSecond = 1.0;
    public static final double kThetaKpPerSecond = 1.0;
    public static final double kMaxTranslationSpeedMetersPerSecond = 0.50;
    public static final double kMaxAngularSpeedRadiansPerSecond = 0.75;
    public static final double kThetaProfileMaxVelocityRadiansPerSecond = 0.75;
    public static final double kThetaProfileMaxAccelerationRadiansPerSecondSquared = 1.50;
    public static final double kTranslationToleranceMeters = 0.05;
    public static final double kHeadingToleranceRadians = Units.degreesToRadians(3.0);
    public static final double kTimeoutMarginSeconds = 3.0;

    private HolonomicTrajectoryFollowingConstants() {}
  }

  /**
   * Stores the narrow A01_L06 PathPlanner learning contract.
   *
   * <p>The four RobotConfig values marked {@code PROVISIONAL — LEARNING/SIMULATION ONLY — NOT
   * MEASURED — NOT FINAL} are permitted only for conservative learning and Simulation. They are
   * not calibrated or competition-authoritative.
   */
  public static final class PathPlannerLearningConstants {
    /** PathPlanner asset name without the {@code .path} suffix. */
    public static final String kPathAssetName = "A01_L06_OneMeter_Forward";

    /** L09 event-marker learning path asset name without the {@code .path} suffix. */
    public static final String kLearningEventPathAssetName =
        "A01_L09_OneMeter_With_Learning_Event";

    /** Bounded duration of the L09 demonstration event, in seconds. */
    public static final double kLearningEventDurationSeconds = 0.50;

    /** PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL. */
    public static final double kProvisionalRobotMassKg = 45.0;

    /** PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL. */
    public static final double kProvisionalRobotMoiKgMetersSquared = 5.0;

    /** PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL. */
    public static final double kProvisionalMaxDriveVelocityMetersPerSecond = 4.0;

    /** PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL. */
    public static final double kProvisionalWheelCof = 1.0;

    /** Locked one-meter learning-path maximum translation velocity, in meters per second. */
    public static final double kPathMaxTranslationVelocityMetersPerSecond = 0.50;

    /** Locked one-meter learning-path maximum translation acceleration, in meters per second squared. */
    public static final double kPathMaxTranslationAccelerationMetersPerSecondSquared = 1.0;

    /** Locked one-meter learning-path maximum angular velocity, in radians per second. */
    public static final double kPathMaxAngularVelocityRadiansPerSecond = 0.75;

    /** Locked one-meter learning-path maximum angular acceleration, in radians per second squared. */
    public static final double kPathMaxAngularAccelerationRadiansPerSecondSquared = 1.50;

    /** PathPlanner's standard nominal voltage stored in the path asset, in volts. */
    public static final double kPathNominalVoltageVolts = 12.0;

    /** Canonical Blue-frame starting pose for the locked learning path. */
    public static final Pose2d kCanonicalPathStartingPose = Pose2d.kZero;

    /** Canonical Blue-frame ending pose for the locked learning path. */
    public static final Pose2d kCanonicalPathEndingPose =
        new Pose2d(1.0, 0.0, Rotation2d.kZero);

    /** Distance below which a zero-distance state transition is considered coincident. */
    public static final double kTrajectoryValidationDistanceEpsilonMeters = 1.0e-9;

    /** Heading delta below which a coincident state transition is considered unchanged. */
    public static final double kTrajectoryValidationHeadingEpsilonRadians = 1.0e-9;

    private PathPlannerLearningConstants() {}
  }

  /**
   * Stores verified Swerve hardware identifiers, geometry, and output limits.
   */
  public static final class SwerveConstants {
    public static final String kPhoenix6Version = "26.3.0";

    public static final int kPigeonCanId = 20;

    public static final int kFrontLeftDriveCanId = 21;
    public static final int kFrontLeftSteerCanId = 22;
    public static final int kFrontLeftEncoderCanId = 23;

    public static final int kFrontRightDriveCanId = 24;
    public static final int kFrontRightSteerCanId = 25;
    public static final int kFrontRightEncoderCanId = 26;

    public static final int kBackLeftDriveCanId = 27;
    public static final int kBackLeftSteerCanId = 28;
    public static final int kBackLeftEncoderCanId = 29;

    public static final int kBackRightDriveCanId = 30;
    public static final int kBackRightSteerCanId = 31;
    public static final int kBackRightEncoderCanId = 32;

    public static final double kDriveGearRatio = 6.75;
    public static final double kSteerGearRatio = 15.42857142857143;

    public static final boolean kFrontLeftDriveInverted = false;
    public static final boolean kFrontRightDriveInverted = true;
    public static final boolean kBackLeftDriveInverted = false;
    public static final boolean kBackRightDriveInverted = true;

    /** Physical-forward sign measured for each raw drive position in FL, FR, BL, BR order. */
    public static final double kFrontLeftDrivePositionSign = 1.0;
    public static final double kFrontRightDrivePositionSign = 1.0;
    public static final double kBackLeftDrivePositionSign = 1.0;
    public static final double kBackRightDrivePositionSign = 1.0;

    public static final boolean kFrontLeftSteerInverted = true;
    public static final boolean kFrontRightSteerInverted = true;
    public static final boolean kBackLeftSteerInverted = true;
    public static final boolean kBackRightSteerInverted = true;

    public static final boolean kFrontLeftEncoderInverted = false;
    public static final boolean kFrontRightEncoderInverted = false;
    public static final boolean kBackLeftEncoderInverted = false;
    public static final boolean kBackRightEncoderInverted = false;

    public static final double kFrontLeftEncoderOffsetRotations = 0.068603515625;
    public static final double kFrontRightEncoderOffsetRotations = 0.014404296875;
    public static final double kBackLeftEncoderOffsetRotations = 0.46240234375;
    public static final double kBackRightEncoderOffsetRotations = -0.057373046875;

    public static final double kDriveSupplyCurrentLimitAmps = 70.0;
    public static final boolean kDriveSupplyCurrentLimitEnabled = true;
    public static final double kSteerStatorCurrentLimitAmps = 60.0;
    public static final boolean kSteerStatorCurrentLimitEnabled = true;
    public static final double kWheelDiameterMeters = Units.inchesToMeters(4.0);
    public static final double kWheelRadiusMeters = Units.inchesToMeters(2.0);
    public static final double kWheelbaseMeters = Units.inchesToMeters(21.5);
    public static final double kTrackWidthMeters = Units.inchesToMeters(21.5);

    /** Configured maximum wheel speed used by the pure output pipeline, in meters per second. */
    public static final double kMaxWheelSpeedMetersPerSecond = 4.0;

    /** Temporary safe L20 robot-relative teleop translation scale, in meters per second. */
    public static final double kTeleopMaxTranslationMetersPerSecond = 1.0;

    /** Temporary safe L20 robot-relative teleop rotation scale, in radians per second. */
    public static final double kTeleopMaxAngularSpeedRadiansPerSecond = 1.0;

    /** Fixed four-module Test-mode translation verification speed, in meters per second. */
    public static final double kFourModuleTestTranslationSpeedMetersPerSecond = 0.30;

    /** Fixed four-module Test-mode CCW rotation verification speed, in radians per second. */
    public static final double kFourModuleTestRotationSpeedRadiansPerSecond = 0.75;

    /** Fixed duration for one four-module Test-mode verification command, in seconds. */
    public static final double kFourModuleTestCommandDurationSeconds = 1.0;

    /** Target travel distance for the bounded L23 drive validation command, in meters. */
    public static final double kDriveThreeMeterValidationTargetMeters = 3.0;

    /** Maximum runtime for the bounded L23 drive validation command, in seconds. */
    public static final double kDriveThreeMeterValidationTimeoutSeconds = 15.0;

    /** Maximum allowed spread from the median module delta during L23 drive validation, in meters. */
    public static final double kDriveThreeMeterValidationModuleDisagreementToleranceMeters = 0.15;

    /** Front Left-only maximum absolute closed-loop drive velocity, in meters per second. */
    public static final double kFrontLeftMaximumDriveVelocityMetersPerSecond = 0.50;

    /** Fixed positive Front Left closed-loop drive test velocity, in meters per second. */
    public static final double kFrontLeftPositiveDriveTestVelocityMetersPerSecond = 0.30;

    /** Fixed negative Front Left closed-loop drive test velocity, in meters per second. */
    public static final double kFrontLeftNegativeDriveTestVelocityMetersPerSecond = -0.30;

    /** Front Left maximum shortest closed-loop steer step, in rotations. */
    public static final double kFrontLeftMaximumSteerStepRotations = 0.125;

    /** Fixed positive Front Left closed-loop steer test step, in rotations. */
    public static final double kFrontLeftPositiveSteerTestStepRotations = 0.0625;

    /** Fixed negative Front Left closed-loop steer test step, in rotations. */
    public static final double kFrontLeftNegativeSteerTestStepRotations = -0.0625;

    /** Front Left closed-loop commissioning timeout, in seconds. */
    public static final double kFrontLeftClosedLoopCommissioningTimeoutSeconds = 1.0;

    /** Maximum Front Left static-friction characterization voltage, in volts. */
    public static final double kFrontLeftDriveStaticFrictionMaximumVoltageVolts = 1.0;

    /** Manual Front Left static-friction characterization voltage increment, in volts. */
    public static final double kFrontLeftDriveStaticFrictionVoltageIncrementVolts = 0.10;

    /** Number of independently triggered Front Left static-friction voltage steps. */
    public static final int kFrontLeftDriveStaticFrictionVoltageStepCount = 10;

    /** Maximum duration of one Front Left static-friction characterization pulse, in seconds. */
    public static final double kFrontLeftDriveStaticFrictionPulseDurationSeconds = 0.25;

    /** Minimum absolute drive rotor speed indicating Front Left breakaway, in rotations per second. */
    public static final double
        kFrontLeftDriveStaticFrictionBreakawayRotorVelocityRotationsPerSecond = 0.10;

    /** Drive velocity Slot 0 proportional gain, in volts per rotation per second. */
    public static final double kDriveVelocitySlot0KpVoltsPerRotationPerSecond = 0.675;

    /** Drive velocity Slot 0 integral gain, in volts per rotation. */
    public static final double kDriveVelocitySlot0KiVoltsPerRotation = 0.0;

    /** Drive velocity Slot 0 derivative gain, in volts per rotation per second squared. */
    public static final double kDriveVelocitySlot0KdVoltsPerRotationPerSecondSquared = 0.0;

    /** Drive velocity Slot 0 static feedforward gain, in volts. */
    public static final double kDriveVelocitySlot0KsVolts = 0.15;

    /** Drive velocity Slot 0 velocity feedforward gain, in volts per rotation per second. */
    public static final double kDriveVelocitySlot0KvVoltsPerRotationPerSecond = 0.837;

    /** Drive velocity Slot 0 acceleration feedforward gain, in volts per rotation per second squared. */
    public static final double kDriveVelocitySlot0KaVoltsPerRotationPerSecondSquared = 0.0;

    /** Steer position Slot 0 proportional gain, in volts per rotation. */
    public static final double kSteerPositionSlot0KpVoltsPerRotation = 100.0;

    /** Steer position Slot 0 integral gain, in volts per rotation second. */
    public static final double kSteerPositionSlot0KiVoltsPerRotationSecond = 0.0;

    /** Steer position Slot 0 derivative gain, in volts per rotation per second. */
    public static final double kSteerPositionSlot0KdVoltsPerRotationPerSecond = 0.5;

    /** Provisional positive magnitude for the Front Left drive commissioning pulse. */
    public static final double kFrontLeftDriveCommissioningDutyCycle = 0.05;

    /** Provisional positive magnitude for the Front Left steer commissioning pulse. */
    public static final double kFrontLeftSteerCommissioningDutyCycle = 0.05;

    /** Provisional maximum duration for a Front Left commissioning pulse, in seconds. */
    public static final double kFrontLeftCommissioningPulseDurationSeconds = 0.25;

    private SwerveConstants() {}
  }
}
