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
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveSubsystemClosedLoopCommissioningTest {
  @BeforeAll
  static void initializeHal() {
    assertTrue(HAL.initialize(500, 0), "HAL initialization failed");
  }

  @BeforeEach
  void enableTestMode() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
  }

  @Test
  void clampsDriveVelocityToApprovedFrontLeftLimit() {
    assertEquals(0.50, SwerveSubsystem.clampFrontLeftDriveVelocityMetersPerSecond(4.0));
    assertEquals(-0.50, SwerveSubsystem.clampFrontLeftDriveVelocityMetersPerSecond(-4.0));
    assertEquals(0.30, SwerveSubsystem.clampFrontLeftDriveVelocityMetersPerSecond(0.30));
  }

  @Test
  void limitsShortestSteerStepToApprovedFortyFiveDegrees() {
    assertEquals(
        0.125,
        SwerveSubsystem.limitFrontLeftSteerStep(Rotation2d.fromRotations(0.25)).getRotations(),
        1.0e-12);
    assertEquals(
        -0.125,
        SwerveSubsystem.limitFrontLeftSteerStep(Rotation2d.fromRotations(-0.25)).getRotations(),
        1.0e-12);
  }

  @Test
  void generatesRelativeSteerTargetAcrossZeroRotationBoundary() {
    Rotation2d target =
        SwerveSubsystem.frontLeftRelativeSteerTarget(
            Rotation2d.fromRotations(0.98),
            Rotation2d.fromRotations(0.0625));

    assertEquals(0.0425, target.getRotations(), 1.0e-12);
  }

  @Test
  void sendsOnlyApprovedFrontLeftClosedLoopRequests() {
    Rig rig = new Rig(0.98);

    assertTrue(
        rig.subsystem.startFrontLeftDriveVelocityCommissioning(
            SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.DRIVE_POSITIVE));
    assertEquals(0.30, rig.frontLeft.lastDriveVelocityMetersPerSecond, 1.0e-12);
    rig.subsystem.stopFrontLeftCommissioning();

    assertTrue(
        rig.subsystem.startFrontLeftSteerAngleCommissioning(
            SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.STEER_POSITIVE));
    assertEquals(0.0425, rig.frontLeft.lastSteerAngle.getRotations(), 1.0e-12);
    assertEquals(0, rig.frontRight.closedLoopRequestCount);
    assertEquals(0, rig.backLeft.closedLoopRequestCount);
    assertEquals(0, rig.backRight.closedLoopRequestCount);
  }

  @Test
  void sendsOnlyOneClampedFrontLeftStaticFrictionVoltageRequest() {
    Rig rig = new Rig(0.0);

    assertTrue(rig.subsystem.startFrontLeftDriveStaticFrictionCharacterization(4.0));
    assertEquals(
        Constants.SwerveConstants.kFrontLeftDriveStaticFrictionMaximumVoltageVolts,
        rig.frontLeft.lastStaticFrictionVoltageVolts,
        1.0e-12);
    assertEquals(1, rig.frontLeft.staticFrictionRequestCount);
    assertEquals(0, rig.frontRight.staticFrictionRequestCount);
    assertEquals(0, rig.backLeft.staticFrictionRequestCount);
    assertEquals(0, rig.backRight.staticFrictionRequestCount);
  }

  @Test
  void rejectsStaticFrictionCharacterizationOutsideEnabledTestMode() {
    Rig rig = new Rig(0.0);
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();

    assertFalse(rig.subsystem.startFrontLeftDriveStaticFrictionCharacterization(0.10));
    assertEquals(0, rig.frontLeft.staticFrictionRequestCount);
    assertTrue(rig.frontLeft.stopCount > 0);
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft;
    private final RecordingModuleIO frontRight = new RecordingModuleIO(0.0);
    private final RecordingModuleIO backLeft = new RecordingModuleIO(0.0);
    private final RecordingModuleIO backRight = new RecordingModuleIO(0.0);
    private final SwerveSubsystem subsystem;

    private Rig(double frontLeftAngleRotations) {
      frontLeft = new RecordingModuleIO(frontLeftAngleRotations);
      subsystem =
          new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final double encoderAngleRotations;
    private int closedLoopRequestCount;
    private int staticFrictionRequestCount;
    private int stopCount;
    private double lastDriveVelocityMetersPerSecond;
    private double lastStaticFrictionVoltageVolts;
    private Rotation2d lastSteerAngle = new Rotation2d();

    private RecordingModuleIO(double encoderAngleRotations) {
      this.encoderAngleRotations = encoderAngleRotations;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.encoderAbsolutePositionRotations = encoderAngleRotations;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      closedLoopRequestCount++;
      lastDriveVelocityMetersPerSecond = velocityMetersPerSecond;
    }

    @Override
    public boolean setDriveStaticFrictionCharacterizationVoltageVolts(double voltageVolts) {
      staticFrictionRequestCount++;
      lastStaticFrictionVoltageVolts = voltageVolts;
      return true;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      closedLoopRequestCount++;
      lastSteerAngle = angle;
    }

    @Override
    public void stop() {
      stopCount++;
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
