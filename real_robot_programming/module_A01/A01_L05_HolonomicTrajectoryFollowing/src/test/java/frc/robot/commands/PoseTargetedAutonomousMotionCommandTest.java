// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.SwerveObservation;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoseTargetedAutonomousMotionCommandTest {
  private static final double kTolerance = 1.0e-12;
  private static final double kTranslationKp = 1.0;
  private static final double kHeadingKp = 1.0;
  private static final double kMaxTranslation = 0.20;
  private static final double kMaxAngular = 0.35;
  private static final double kTranslationTolerance = 0.03;
  private static final double kHeadingTolerance = Math.toRadians(2.0);
  private static final double kTimeout = 4.0;

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
  void requiresSwerveAndDoesNotRunWhenDisabled() {
    Rig rig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand command = createCommand(rig);

    assertTrue(command.getRequirements().contains(rig.subsystem));
    assertFalse(command.runsWhenDisabled());
    assertFalse(command.isFinished());
  }

  @Test
  void commandsBoundedFieldRelativeTranslationAndHeading() {
    Rig rig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand command =
        createCommand(rig, new Pose2d(0.40, 0.0, Rotation2d.fromDegrees(30.0)));

    enableAutonomous();
    command.initialize();
    command.execute();

    assertEquals(kMaxTranslation, Math.hypot(rig.accepted.vxMetersPerSecond,
        rig.accepted.vyMetersPerSecond), kTolerance);
    assertEquals(0.0, rig.accepted.vyMetersPerSecond, kTolerance);
    assertEquals(kMaxAngular, rig.accepted.omegaRadiansPerSecond, kTolerance);
    assertFalse(command.isFinished());
  }

  @Test
  void limitsTranslationByVectorMagnitudeRatherThanEachAxis() {
    Rig rig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand command =
        createCommand(rig, new Pose2d(0.30, 0.40, new Rotation2d()));

    enableAutonomous();
    command.initialize();
    command.execute();

    assertEquals(0.12, rig.accepted.vxMetersPerSecond, kTolerance);
    assertEquals(0.16, rig.accepted.vyMetersPerSecond, kTolerance);
    assertEquals(kMaxTranslation,
        Math.hypot(rig.accepted.vxMetersPerSecond, rig.accepted.vyMetersPerSecond),
        kTolerance);
  }

  @Test
  void wrapsHeadingErrorToTheShortestDirection() {
    Rig rig = new Rig(new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(-179.0)));
    PoseTargetedAutonomousMotionCommand command =
        createCommand(rig, new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(178.0)));

    enableAutonomous();
    command.initialize();
    command.execute();

    assertEquals(-Math.toRadians(3.0), rig.accepted.omegaRadiansPerSecond, kTolerance);
    assertEquals(0.0, rig.accepted.vxMetersPerSecond, kTolerance);
    assertEquals(0.0, rig.accepted.vyMetersPerSecond, kTolerance);
  }

  @Test
  void suppressesTranslationPerCycleAndReactivatesItWhenErrorLeavesTolerance() {
    Rig rig = new Rig(new Pose2d(0.38, 0.0, Rotation2d.fromDegrees(10.0)));
    PoseTargetedAutonomousMotionCommand command = createCommand(rig);

    enableAutonomous();
    command.initialize();
    command.execute();

    assertEquals(0.0, rig.accepted.vxMetersPerSecond, kTolerance);
    assertTrue(Math.abs(rig.accepted.omegaRadiansPerSecond) > 0.0);

    rig.setPose(new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(10.0)));
    command.execute();

    assertTrue(rig.accepted.vxMetersPerSecond > 0.0);
  }

  @Test
  void finishesOnlyWhenTranslationAndHeadingAreBothWithinTolerance() {
    Rig rig = new Rig(new Pose2d(0.38, 0.0, Rotation2d.fromDegrees(10.0)));
    PoseTargetedAutonomousMotionCommand command = createCommand(rig);

    enableAutonomous();
    command.initialize();
    command.execute();
    assertFalse(command.isFinished());

    rig.setPose(new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(1.0)));
    command.execute();
    assertFalse(command.isFinished());

    rig.setPose(new Pose2d(0.38, 0.0, Rotation2d.fromDegrees(1.0)));
    command.execute();
    assertTrue(command.isFinished());
  }

  @Test
  void timeoutFailsClosedAndEndAlwaysStops() {
    Rig rig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand command = createCommand(rig);

    enableAutonomous();
    command.initialize();
    rig.clock = kTimeout;
    command.execute();

    assertTrue(command.isFinished());
    assertEquals(0, rig.acceptCount);
    int stopsBeforeEnd = rig.stopCount;
    command.end(false);
    assertTrue(rig.stopCount > stopsBeforeEnd);
  }

  @Test
  void unavailableOrInvalidEstimatedPoseFailsClosed() {
    Rig missingPoseRig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand missingPoseCommand = createCommand(missingPoseRig);
    enableAutonomous();
    missingPoseCommand.initialize();
    missingPoseRig.estimatedPose = Optional.empty();
    missingPoseCommand.execute();
    assertTrue(missingPoseCommand.isFinished());
    assertEquals(0, missingPoseRig.acceptCount);

    Rig invalidObservationRig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand invalidObservationCommand =
        createCommand(invalidObservationRig);
    invalidObservationCommand.initialize();
    invalidObservationRig.measurementSampleValid = false;
    invalidObservationCommand.execute();
    assertTrue(invalidObservationCommand.isFinished());
    assertEquals(0, invalidObservationRig.acceptCount);
  }

  @Test
  void modeLossAndBackwardTimeFailClosed() {
    Rig modeLossRig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand modeLossCommand = createCommand(modeLossRig);
    enableAutonomous();
    modeLossCommand.initialize();
    disableAndNotify();
    modeLossCommand.execute();
    assertTrue(modeLossCommand.isFinished());
    assertEquals(0, modeLossRig.acceptCount);

    Rig backwardTimeRig = new Rig(Pose2d.kZero);
    PoseTargetedAutonomousMotionCommand backwardTimeCommand = createCommand(backwardTimeRig);
    enableAutonomous();
    backwardTimeCommand.initialize();
    backwardTimeRig.clock = -0.01;
    backwardTimeCommand.execute();
    assertTrue(backwardTimeCommand.isFinished());
    assertEquals(0, backwardTimeRig.acceptCount);
  }

  @Test
  void rejectsInvalidImmutableConfigurationBeforeRuntime() {
    Rig rig = new Rig(Pose2d.kZero);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            new PoseTargetedAutonomousMotionCommand(
                rig.subsystem,
                new Pose2d(Double.NaN, 0.0, new Rotation2d()),
                kTranslationKp,
                kHeadingKp,
                kMaxTranslation,
                kMaxAngular,
                kTranslationTolerance,
                kHeadingTolerance,
                kTimeout,
                () -> 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> createCommand(rig, Pose2d.kZero, 0.0, kHeadingKp));
    assertThrows(
        IllegalArgumentException.class,
        () -> createCommand(rig, Pose2d.kZero, kTranslationKp, kHeadingKp, 0.0));
    assertThrows(
        IllegalArgumentException.class,
        () -> createCommandWithTolerances(rig, -0.01, kHeadingTolerance));
  }

  private static PoseTargetedAutonomousMotionCommand createCommand(Rig rig) {
    return createCommand(rig, new Pose2d(0.40, 0.0, new Rotation2d()));
  }

  private static PoseTargetedAutonomousMotionCommand createCommand(
      Rig rig, Pose2d targetPose) {
    return new PoseTargetedAutonomousMotionCommand(
        rig.subsystem,
        targetPose,
        kTranslationKp,
        kHeadingKp,
        kMaxTranslation,
        kMaxAngular,
        kTranslationTolerance,
        kHeadingTolerance,
        kTimeout,
        () -> rig.clock);
  }

  private static PoseTargetedAutonomousMotionCommand createCommand(
      Rig rig, Pose2d targetPose, double translationKp, double headingKp) {
    return new PoseTargetedAutonomousMotionCommand(
        rig.subsystem,
        targetPose,
        translationKp,
        headingKp,
        kMaxTranslation,
        kMaxAngular,
        kTranslationTolerance,
        kHeadingTolerance,
        kTimeout,
        () -> rig.clock);
  }

  private static PoseTargetedAutonomousMotionCommand createCommand(
      Rig rig, Pose2d targetPose, double translationKp, double headingKp, double maxTranslation) {
    return new PoseTargetedAutonomousMotionCommand(
        rig.subsystem,
        targetPose,
        translationKp,
        headingKp,
        maxTranslation,
        kMaxAngular,
        kTranslationTolerance,
        kHeadingTolerance,
        kTimeout,
        () -> rig.clock);
  }

  private static PoseTargetedAutonomousMotionCommand createCommandWithTolerances(
      Rig rig, double translationTolerance, double headingTolerance) {
    return new PoseTargetedAutonomousMotionCommand(
        rig.subsystem,
        Pose2d.kZero,
        kTranslationKp,
        kHeadingKp,
        kMaxTranslation,
        kMaxAngular,
        translationTolerance,
        headingTolerance,
        kTimeout,
        () -> rig.clock);
  }

  private static void enableAutonomous() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void disableAndNotify() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static SwerveObservation validObservation(
      Pose2d pose, boolean measurementSampleValid) {
    SwerveObservation.ModuleObservation module =
        new SwerveObservation.ModuleObservation(
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            0.0,
            true,
            true,
            true,
            true,
            true,
            true);
    SwerveObservation.GyroObservation gyro =
        new SwerveObservation.GyroObservation(
            0.0, 0.0, 0.0, 0.0, 0.0, 0.0, true, true);
    SwerveObservation.PoseObservation currentPose =
        new SwerveObservation.PoseObservation(
            pose.getX(), pose.getY(), pose.getRotation().getRadians(), measurementSampleValid);
    SwerveObservation.EstimatedPoseObservation estimatedPose =
        new SwerveObservation.EstimatedPoseObservation(
            pose.getX(), pose.getY(), pose.getRotation().getRadians(), measurementSampleValid);
    return new SwerveObservation(
        module,
        module,
        module,
        module,
        gyro,
        Optional.of(currentPose),
        Optional.of(estimatedPose));
  }

  private static final class Rig {
    private final RecordingSwerveSubsystem subsystem;
    private double clock;
    private Optional<Pose2d> estimatedPose;
    private boolean measurementSampleValid = true;
    private ChassisSpeeds accepted = new ChassisSpeeds();
    private int acceptCount;
    private int stopCount;

    private Rig(Pose2d initialPose) {
      subsystem = new RecordingSwerveSubsystem(this);
      estimatedPose = Optional.of(initialPose);
    }

    private void setPose(Pose2d pose) {
      estimatedPose = Optional.of(pose);
    }
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private final Rig rig;

    private RecordingSwerveSubsystem(Rig rig) {
      super(
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopGyroIO());
      this.rig = rig;
    }

    @Override
    public Optional<Pose2d> getEstimatedPose() {
      return rig.estimatedPose;
    }

    @Override
    public Optional<SwerveObservation> getObservation() {
      return rig.estimatedPose.map(
          pose -> Optional.of(validObservation(pose, rig.measurementSampleValid))).orElse(Optional.empty());
    }

    @Override
    public void acceptFieldRelativeChassisSpeeds(ChassisSpeeds fieldRelativeSpeeds) {
      rig.acceptCount++;
      rig.accepted =
          new ChassisSpeeds(
              fieldRelativeSpeeds.vxMetersPerSecond,
              fieldRelativeSpeeds.vyMetersPerSecond,
              fieldRelativeSpeeds.omegaRadiansPerSecond);
    }

    @Override
    public void stop() {
      rig.stopCount++;
    }
  }

  private static final class NoopModuleIO implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {}
  }

  private static final class NoopGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
