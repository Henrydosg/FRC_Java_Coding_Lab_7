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
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Verifies the latched runtime module-health fail-stop before production motion dispatch. */
class SwerveSubsystemRuntimeHealthFailStopTest {
  private static final double kTolerance = 1.0e-12;
  private static final int kModuleCount = 4;

  /** One runtime module-health flag that must fail closed when false. */
  private enum HealthFlag {
    DRIVE_CONNECTED,
    STEER_CONNECTED,
    ENCODER_CONNECTED,
    DRIVE_CONFIGURATION_HEALTHY,
    STEER_CONFIGURATION_HEALTHY,
    ENCODER_CONFIGURATION_HEALTHY,
    ENCODER_MEASUREMENT_VALID
  }

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    setDriverStation(false, false);
  }

  @Test
  void allHealthyModulesDispatchArmedProductionIntent() {
    Rig rig = new Rig();
    setDriverStation(true, false);

    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();

    rig.assertEveryModuleDispatched();
    rig.assertNoModuleStopped();
  }

  @ParameterizedTest
  @EnumSource(HealthFlag.class)
  void anyUnhealthyModuleLatchesFailStopOfAllFourModules(HealthFlag flag) {
    for (int failedModule = 0; failedModule < kModuleCount; failedModule++) {
      Rig rig = new Rig();
      setDriverStation(true, false);
      rig.subsystem.acceptChassisSpeeds(forwardIntent());
      rig.subsystem.periodic();
      rig.assertEveryModuleDispatched();

      rig.modules[failedModule].setHealth(flag, false);
      rig.clearCounts();
      rig.subsystem.periodic();

      rig.assertNoModuleDispatched();
      rig.assertEveryModuleStopped();
      rig.assertZeroFinalModuleStates();
    }
  }

  @Test
  void measurementValidityPropagatesIntoEveryModuleObservation() {
    Rig rig = new Rig();
    rig.modules[3].setHealth(HealthFlag.ENCODER_MEASUREMENT_VALID, false);

    rig.subsystem.periodic();

    var observation = rig.subsystem.getObservation().orElseThrow();
    assertTrue(observation.frontLeft().encoderMeasurementValid());
    assertTrue(observation.frontRight().encoderMeasurementValid());
    assertTrue(observation.backLeft().encoderMeasurementValid());
    assertFalse(observation.backRight().encoderMeasurementValid());
  }

  @Test
  void reconnectWhileEnabledDoesNotResumeMotion() {
    Rig rig = latchedRig();

    rig.modules[2].setHealth(HealthFlag.ENCODER_CONNECTED, true);
    rig.clearCounts();
    rig.subsystem.periodic();
    rig.subsystem.periodic();

    rig.assertNoModuleDispatched();
  }

  @Test
  void repeatedNewIntentsWhileLatchedAreRejected() {
    Rig rig = latchedRig();
    rig.modules[2].setHealth(HealthFlag.ENCODER_CONNECTED, true);

    for (int cycle = 0; cycle < 3; cycle++) {
      rig.clearCounts();
      rig.subsystem.acceptChassisSpeeds(forwardIntent());
      rig.subsystem.acceptFieldRelativeChassisSpeeds(forwardIntent());
      rig.subsystem.periodic();

      rig.assertNoModuleDispatched();
      rig.assertEveryModuleStopped();
      rig.assertZeroFinalModuleStates();
    }
  }

  @Test
  void disableWithAllModulesHealthyClearsLatchAndRequiresNewIntent() {
    Rig rig = latchedRig();
    rig.modules[2].setHealth(HealthFlag.ENCODER_CONNECTED, true);

    setDriverStation(false, false);
    rig.subsystem.periodic();
    setDriverStation(true, false);
    rig.clearCounts();
    rig.subsystem.periodic();
    rig.assertNoModuleDispatched();

    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();
    rig.assertEveryModuleDispatched();
  }

  @Test
  void disableWithUnhealthyModuleKeepsLatch() {
    Rig rig = latchedRig();

    setDriverStation(false, false);
    rig.subsystem.periodic();
    rig.modules[2].setHealth(HealthFlag.ENCODER_CONNECTED, true);
    setDriverStation(true, false);
    rig.clearCounts();
    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();

    rig.assertNoModuleDispatched();
  }

  @Test
  void disabledRobotNeverDispatchesProductionMotion() {
    Rig rig = new Rig();

    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();
    rig.modules[1].setHealth(HealthFlag.DRIVE_CONNECTED, false);
    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();

    rig.assertNoModuleDispatched();
  }

  @Test
  void frontLeftCommissioningRemainsAvailableAndDoesNotLatch() {
    Rig rig = new Rig();
    setDriverStation(true, true);
    rig.modules[0].setHealth(HealthFlag.ENCODER_CONNECTED, false);
    rig.subsystem.periodic();

    assertTrue(
        rig.subsystem.startFrontLeftCommissioning(
            SwerveSubsystem.FrontLeftCommissioningAction.DRIVE_POSITIVE));
    assertEquals(
        Constants.SwerveConstants.kFrontLeftDriveCommissioningDutyCycle,
        rig.modules[0].lastDriveOutput,
        kTolerance);
    rig.subsystem.periodic();
    rig.subsystem.stopFrontLeftCommissioning();

    rig.modules[0].setHealth(HealthFlag.ENCODER_CONNECTED, true);
    rig.clearCounts();
    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();
    rig.assertEveryModuleDispatched();
  }

  private static Rig latchedRig() {
    Rig rig = new Rig();
    rig.subsystem.periodic();
    assertTrue(rig.subsystem.captureFieldHeadingReference());
    setDriverStation(true, false);
    rig.subsystem.acceptChassisSpeeds(forwardIntent());
    rig.subsystem.periodic();
    rig.assertEveryModuleDispatched();

    rig.modules[2].setHealth(HealthFlag.ENCODER_CONNECTED, false);
    rig.subsystem.periodic();
    rig.assertEveryModuleStopped();
    return rig;
  }

  private static ChassisSpeeds forwardIntent() {
    return new ChassisSpeeds(1.0, 0.0, 0.0);
  }

  private static void setDriverStation(boolean enabled, boolean test) {
    DriverStationSim.resetData();
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(test);
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.notifyNewData();
  }

  private static final class Rig {
    private final FakeModuleIO[] modules = {
      new FakeModuleIO(), new FakeModuleIO(), new FakeModuleIO(), new FakeModuleIO()
    };
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(modules[0], modules[1], modules[2], modules[3], new HealthyGyroIO());

    private void clearCounts() {
      for (FakeModuleIO module : modules) {
        module.driveVelocityCount = 0;
        module.steerAngleCount = 0;
        module.stopCount = 0;
      }
    }

    private void assertEveryModuleDispatched() {
      for (FakeModuleIO module : modules) {
        assertTrue(module.driveVelocityCount > 0);
        assertTrue(module.steerAngleCount > 0);
      }
    }

    private void assertNoModuleDispatched() {
      for (FakeModuleIO module : modules) {
        assertEquals(0, module.driveVelocityCount);
        assertEquals(0, module.steerAngleCount);
      }
    }

    private void assertEveryModuleStopped() {
      for (FakeModuleIO module : modules) {
        assertTrue(module.stopCount > 0);
      }
    }

    private void assertNoModuleStopped() {
      for (FakeModuleIO module : modules) {
        assertEquals(0, module.stopCount);
      }
    }

    private void assertZeroFinalModuleStates() {
      for (SwerveModuleState state : subsystem.getFinalModuleStates()) {
        assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      }
    }
  }

  private static final class FakeModuleIO implements SwerveModuleIO {
    private boolean driveConnected = true;
    private boolean steerConnected = true;
    private boolean encoderConnected = true;
    private boolean driveConfigurationHealthy = true;
    private boolean steerConfigurationHealthy = true;
    private boolean encoderConfigurationHealthy = true;
    private boolean encoderMeasurementValid = true;
    private int driveVelocityCount;
    private int steerAngleCount;
    private int stopCount;
    private double lastDriveOutput;

    private void setHealth(HealthFlag flag, boolean healthy) {
      switch (flag) {
        case DRIVE_CONNECTED -> driveConnected = healthy;
        case STEER_CONNECTED -> steerConnected = healthy;
        case ENCODER_CONNECTED -> encoderConnected = healthy;
        case DRIVE_CONFIGURATION_HEALTHY -> driveConfigurationHealthy = healthy;
        case STEER_CONFIGURATION_HEALTHY -> steerConfigurationHealthy = healthy;
        case ENCODER_CONFIGURATION_HEALTHY -> encoderConfigurationHealthy = healthy;
        case ENCODER_MEASUREMENT_VALID -> encoderMeasurementValid = healthy;
        default -> throw new IllegalArgumentException("Unsupported health flag");
      }
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.driveConnected = driveConnected;
      inputs.steerConnected = steerConnected;
      inputs.encoderConnected = encoderConnected;
      inputs.driveConfigurationHealthy = driveConfigurationHealthy;
      inputs.steerConfigurationHealthy = steerConfigurationHealthy;
      inputs.encoderConfigurationHealthy = encoderConfigurationHealthy;
      inputs.encoderMeasurementValid = encoderMeasurementValid;
    }

    @Override
    public void setDriveOutput(double output) {
      lastDriveOutput = output;
    }

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveVelocityCount++;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerAngleCount++;
    }

    @Override
    public void stop() {
      stopCount++;
    }
  }

  private static final class HealthyGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.yawDegrees = 0.0;
      inputs.connected = true;
      inputs.configurationHealthy = true;
    }
  }
}
