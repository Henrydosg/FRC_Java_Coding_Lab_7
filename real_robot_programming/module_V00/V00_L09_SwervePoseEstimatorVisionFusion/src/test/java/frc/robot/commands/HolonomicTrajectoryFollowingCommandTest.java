// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.util.LearningTrajectoryFactory;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HolonomicTrajectoryFollowingCommandTest {
  @BeforeAll
  static void initializeHal() { HAL.initialize(500, 0); }

  @BeforeEach
  void disableRobot() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void rejectsInvalidConfigurationAndDoesNotRunWhenDisabled() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new HolonomicTrajectoryFollowingCommand.Configuration(
            0.0, 1.0, 1.0, 0.5, 0.75, 0.75, 1.5, 0.05, Math.toRadians(3.0), 3.0));

    HolonomicTrajectoryFollowingCommand command =
        new HolonomicTrajectoryFollowingCommand(
            new SwerveSubsystem(new Module(), new Module(), new Module(), new Module(), new Gyro()),
            LearningTrajectoryFactory.createLearningTrajectory(),
            Rotation2d.kZero,
            configuration(),
            () -> 0.0);
    assertTrue(!command.runsWhenDisabled());
  }

  @Test
  void rejectsNullTrajectoryHeadingAndClock() {
    SwerveSubsystem subsystem = new SwerveSubsystem(new Module(), new Module(), new Module(), new Module(), new Gyro());
    DoubleSupplier clock = () -> 0.0;
    assertThrows(NullPointerException.class, () -> new HolonomicTrajectoryFollowingCommand(subsystem, null, Rotation2d.kZero, configuration(), clock));
    Trajectory trajectory = LearningTrajectoryFactory.createLearningTrajectory();
    assertThrows(NullPointerException.class, () -> new HolonomicTrajectoryFollowingCommand(subsystem, trajectory, null, configuration(), clock));
    assertThrows(NullPointerException.class, () -> new HolonomicTrajectoryFollowingCommand(subsystem, trajectory, Rotation2d.kZero, configuration(), null));
  }

  private static HolonomicTrajectoryFollowingCommand.Configuration configuration() {
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

  private static final class Module implements SwerveModuleIO {
    @Override public void updateInputs(SwerveModuleIOInputs inputs) {}
    @Override public void setDriveOutput(double output) {}
    @Override public void setSteerOutput(double output) {}
    @Override public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}
    @Override public void setSteerAngle(Rotation2d angle) {}
    @Override public void stop() {}
  }

  private static final class Gyro implements GyroIO {
    @Override public void updateInputs(GyroIOInputs inputs) {}
  }
}
