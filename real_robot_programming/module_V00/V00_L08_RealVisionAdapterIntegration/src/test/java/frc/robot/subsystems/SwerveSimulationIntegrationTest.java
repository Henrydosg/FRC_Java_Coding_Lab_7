// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.commands.PoseTargetedAutonomousMotionCommand;
import frc.robot.commands.HolonomicTrajectoryFollowingCommand;
import frc.robot.io.gyro.GyroIOSim;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.io.swerve.SwerveModuleIOSim;
import frc.robot.observation.SwerveObservation;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.util.FieldAllianceTransform;
import frc.robot.util.LearningTrajectoryFactory;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveSimulationIntegrationTest {
  private static final double PERIOD_SECONDS = 0.02;
  private static final double TOLERANCE = 1.0e-6;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetDriverStation() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void rotationalChassisIntentAdvancesCounterclockwiseYawFromActualModuleStates() {
    Rig rig = new Rig();
    rig.periodic();
    enableTeleop();
    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.0, 0.0, 1.0));
    rig.periodic();

    rig.advanceOneCycle();
    double transientRate = observation(rig).gyro().angularVelocityZDegreesPerSecond();
    assertTrue(transientRate > 0.0);
    assertTrue(transientRate < Math.toDegrees(1.0));

    for (int cycle = 0; cycle < 10; cycle++) {
      rig.advanceOneCycle();
    }
    SwerveObservation settled = observation(rig);
    assertEquals(Math.toDegrees(1.0),
        settled.gyro().angularVelocityZDegreesPerSecond(), TOLERANCE);
    assertTrue(settled.gyro().yawDegrees() > 0.0);
    assertTrue(settled.gyro().connected());
    assertTrue(settled.gyro().configurationHealthy());

    double movingYaw = settled.gyro().yawDegrees();
    rig.subsystem.stop();
    rig.advanceOneCycle();
    SwerveObservation stopped = observation(rig);
    assertEquals(0.0, stopped.gyro().angularVelocityZDegreesPerSecond(), TOLERANCE);
    assertEquals(movingYaw, stopped.gyro().yawDegrees(), TOLERANCE);
  }

  @Test
  void poseTargetedAutonomousMotionConvergesToTheKnownLearningTarget() {
    Rig rig = new Rig();
    rig.periodic();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.periodic();
    enableAutonomous();

    PoseTargetedAutonomousMotionCommand command =
        new PoseTargetedAutonomousMotionCommand(
            rig.subsystem,
            Constants.PoseTargetedAutonomousConstants.kLearningTargetPose,
            Constants.PoseTargetedAutonomousConstants.kTranslationKpPerSecond,
            Constants.PoseTargetedAutonomousConstants.kHeadingKpPerSecond,
            Constants.PoseTargetedAutonomousConstants.kMaxTranslationSpeedMetersPerSecond,
            Constants.PoseTargetedAutonomousConstants.kMaxAngularSpeedRadiansPerSecond,
            Constants.PoseTargetedAutonomousConstants.kTranslationToleranceMeters,
            Constants.PoseTargetedAutonomousConstants.kHeadingToleranceRadians,
            Constants.PoseTargetedAutonomousConstants.kTimeoutSeconds,
            rig.clock);
    command.initialize();

    for (int cycle = 0; cycle < 300 && !command.isFinished(); cycle++) {
      command.execute();
      if (!command.isFinished()) {
        rig.advanceOneCycle();
      }
    }

    assertTrue(command.isFinished());
    Pose2d estimatedPose = rig.subsystem.getEstimatedPose().orElseThrow();
    assertEquals(
        Constants.PoseTargetedAutonomousConstants.kLearningTargetPose.getX(),
        estimatedPose.getX(),
        Constants.PoseTargetedAutonomousConstants.kTranslationToleranceMeters);
    assertEquals(0.0, estimatedPose.getY(), 0.05);
    assertEquals(0.0, estimatedPose.getRotation().getRadians(), Math.toRadians(2.0));
    command.end(false);
  }

  @Test
  void holonomicFollowerConvergesForBlueAndStopsImmediatelyWhenDisabled() {
    Rig rig = initializedAutonomousRig(Pose2d.kZero);
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();
    HolonomicTrajectoryFollowingCommand command =
        new HolonomicTrajectoryFollowingCommand(
            rig.subsystem, trajectory, Rotation2d.kZero, followerConfiguration(), rig.clock);
    command.initialize();

    for (int cycle = 0; cycle < 300 && !command.isFinished(); cycle++) {
      command.execute();
      if (!command.isFinished()) {
        rig.advanceOneCycle();
      }
    }

    assertTrue(command.isFinished());
    Pose2d finalPose = rig.subsystem.getEstimatedPose().orElseThrow();
    assertEquals(1.0, finalPose.getX(), 0.08);
    assertEquals(0.0, finalPose.getY(), 0.08);

    enableAutonomous();
    HolonomicTrajectoryFollowingCommand moving =
        new HolonomicTrajectoryFollowingCommand(
            rig.subsystem, trajectory, Rotation2d.kZero, followerConfiguration(), rig.clock);
    moving.initialize();
    moving.execute();
    disableRobot();
    moving.execute();
    assertTrue(moving.isFinished());
    assertZeroFinalModuleStates(rig.subsystem);
  }

  @Test
  void redExecutionTrajectoryUsesTheL04TransformAndASeparateHolonomicHeading() {
    Trajectory redTrajectory =
        FieldAllianceTransform.fromCanonicalBlueTrajectory(
            LearningTrajectoryFactory.createLearningTrajectory(), FieldVariant.REBUILT_WELDED, Alliance.Red);
    Rotation2d redHeading =
        FieldAllianceTransform.fromCanonicalBlueHeading(
            Rotation2d.kZero, FieldVariant.REBUILT_WELDED, Alliance.Red);
    Rig rig = initializedAutonomousRig(redTrajectory.getInitialPose());
    HolonomicTrajectoryFollowingCommand command =
        new HolonomicTrajectoryFollowingCommand(
            rig.subsystem, redTrajectory, redHeading, followerConfiguration(), rig.clock);
    command.initialize();
    command.execute();

    assertFalse(command.isFinished());
    assertEquals(Math.PI, redHeading.getRadians(), TOLERANCE);
    assertTrue(
        Math.abs(
                redTrajectory.sample(0.5).poseMeters.getRotation().getRadians()
                    - redHeading.getRadians())
            > 0.1);
    command.end(true);
    assertZeroFinalModuleStates(rig.subsystem);
  }

  private static Rig initializedAutonomousRig(Pose2d startPose) {
    Rig rig = new Rig();
    rig.periodic();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    rig.periodic();
    assertTrue(rig.subsystem.resetKnownFieldPose(startPose));
    rig.periodic();
    enableAutonomous();
    return rig;
  }

  private static HolonomicTrajectoryFollowingCommand.Configuration followerConfiguration() {
    return new HolonomicTrajectoryFollowingCommand.Configuration(
        Constants.HolonomicTrajectoryFollowingConstants.kXKpPerSecond,
        Constants.HolonomicTrajectoryFollowingConstants.kYKpPerSecond,
        Constants.HolonomicTrajectoryFollowingConstants.kThetaKpPerSecond,
        Constants.HolonomicTrajectoryFollowingConstants.kMaxTranslationSpeedMetersPerSecond,
        Constants.HolonomicTrajectoryFollowingConstants.kMaxAngularSpeedRadiansPerSecond,
        Constants.HolonomicTrajectoryFollowingConstants.kThetaProfileMaxVelocityRadiansPerSecond,
        Constants.HolonomicTrajectoryFollowingConstants.kThetaProfileMaxAccelerationRadiansPerSecondSquared,
        Constants.HolonomicTrajectoryFollowingConstants.kTranslationToleranceMeters,
        Constants.HolonomicTrajectoryFollowingConstants.kHeadingToleranceRadians,
        Constants.HolonomicTrajectoryFollowingConstants.kTimeoutMarginSeconds);
  }

  private static void disableRobot() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void assertZeroFinalModuleStates(SwerveSubsystem subsystem) {
    for (var state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, TOLERANCE);
    }
  }

  private static SwerveObservation observation(Rig rig) {
    return rig.subsystem.getObservation().orElseThrow();
  }

  private static void enableTeleop() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void enableAutonomous() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static final class Rig {
    private final MutableClock clock = new MutableClock();
    private final SwerveSimulationState simulationState = new SwerveSimulationState();
    private final SwerveKinematics kinematics = new SwerveKinematics();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(
            module(Constants.SwerveConstants.kFrontLeftDrivePositionSign, ModuleIdentity.FRONT_LEFT),
            module(Constants.SwerveConstants.kFrontRightDrivePositionSign, ModuleIdentity.FRONT_RIGHT),
            module(Constants.SwerveConstants.kBackLeftDrivePositionSign, ModuleIdentity.BACK_LEFT),
            module(Constants.SwerveConstants.kBackRightDrivePositionSign, ModuleIdentity.BACK_RIGHT),
            new GyroIOSim(simulationState, kinematics::toChassisSpeeds, clock));

    private SwerveModuleIOSim module(double sign, ModuleIdentity identity) {
      return new SwerveModuleIOSim(sign, clock, simulationState, identity);
    }

    private void periodic() {
      subsystem.periodic();
    }

    private void advanceOneCycle() {
      clock.seconds += PERIOD_SECONDS;
      periodic();
    }
  }

  private static final class MutableClock implements DoubleSupplier {
    private double seconds;

    @Override
    public double getAsDouble() {
      return seconds;
    }
  }
}
