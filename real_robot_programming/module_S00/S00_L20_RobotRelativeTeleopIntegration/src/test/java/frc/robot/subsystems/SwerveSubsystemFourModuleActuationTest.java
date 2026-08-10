// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveSubsystemFourModuleActuationTest {
  private static final double kTolerance = 1.0e-9;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableProductionMode() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void dispatchesOneDriveAndSteerRequestPerModuleInFixedOrder() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", 0.0, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.0, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.0, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", 0.0, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());

    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.5, 1.0));
    subsystem.periodic();

    assertIterableEquals(
        List.of("FL.drive", "FL.steer", "FR.drive", "FR.steer", "BL.drive", "BL.steer",
            "BR.drive", "BR.steer"),
        dispatchLog);
    assertEquals(1, frontLeft.driveVelocityCount);
    assertEquals(1, frontRight.driveVelocityCount);
    assertEquals(1, backLeft.driveVelocityCount);
    assertEquals(1, backRight.driveVelocityCount);
    assertEquals(1, frontLeft.steerAngleCount);
    assertEquals(1, frontRight.steerAngleCount);
    assertEquals(1, backLeft.steerAngleCount);
    assertEquals(1, backRight.steerAngleCount);
  }

  @Test
  void enabledPeriodicDoesNotDispatchWithoutAcceptedProductionIntent() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", 0.0, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.0, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.0, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", 0.0, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());

    subsystem.periodic();

    assertEquals(0, dispatchLog.size());
    assertEquals(1, frontLeft.updateCount);
    assertEquals(1, frontRight.updateCount);
    assertEquals(1, backLeft.updateCount);
    assertEquals(1, backRight.updateCount);
  }

  @Test
  void dispatchUsesPipelineStatesWithoutReoptimizingOrRedesaturating() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", 0.0, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.25, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.5, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", 0.75, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1.0, 0.5, 1.0);

    subsystem.acceptChassisSpeeds(chassisSpeeds);
    subsystem.periodic();

    SwerveModuleState[] expected =
        new SwerveOutputPipeline()
            .toModuleStates(
                chassisSpeeds,
                new Rotation2d[] {
                  Rotation2d.fromRotations(0.0),
                  Rotation2d.fromRotations(0.25),
                  Rotation2d.fromRotations(0.5),
                  Rotation2d.fromRotations(0.75)
                });
    assertStateEquals(expected[0], frontLeft.lastState());
    assertStateEquals(expected[1], frontRight.lastState());
    assertStateEquals(expected[2], backLeft.lastState());
    assertStateEquals(expected[3], backRight.lastState());
  }

  @Test
  void enabledZeroDemandDispatchesZeroDriveAndCurrentMeasuredSteerAngles() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", -0.10, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.05, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.35, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", -0.40, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());

    subsystem.acceptChassisSpeeds(new ChassisSpeeds());
    subsystem.periodic();

    assertIterableEquals(
        List.of("FL.drive", "FL.steer", "FR.drive", "FR.steer", "BL.drive", "BL.steer",
            "BR.drive", "BR.steer"),
        dispatchLog);
    assertStateEquals(
        new SwerveModuleState(0.0, Rotation2d.fromRotations(-0.10)), frontLeft.lastState());
    assertStateEquals(
        new SwerveModuleState(0.0, Rotation2d.fromRotations(0.05)), frontRight.lastState());
    assertStateEquals(
        new SwerveModuleState(0.0, Rotation2d.fromRotations(0.35)), backLeft.lastState());
    assertStateEquals(
        new SwerveModuleState(0.0, Rotation2d.fromRotations(-0.40)), backRight.lastState());
  }

  @Test
  void disabledPeriodicRefreshesInputsAndPipelineWithoutActuation() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", 0.0, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.0, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.0, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", 0.0, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.5, 1.0));

    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
    subsystem.periodic();

    assertEquals(1, frontLeft.updateCount);
    assertEquals(1, frontRight.updateCount);
    assertEquals(1, backLeft.updateCount);
    assertEquals(1, backRight.updateCount);
    assertEquals(0, frontLeft.driveVelocityCount);
    assertEquals(0, frontRight.driveVelocityCount);
    assertEquals(0, backLeft.driveVelocityCount);
    assertEquals(0, backRight.driveVelocityCount);
    assertEquals(0, frontLeft.steerAngleCount);
    assertEquals(0, frontRight.steerAngleCount);
    assertEquals(0, backLeft.steerAngleCount);
    assertEquals(0, backRight.steerAngleCount);
    assertEquals(0, dispatchLog.size());
  }

  @Test
  void commissioningOwnsFrontLeftAndRequiresNewProductionIntentAfterEnding() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", 0.0, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.0, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.0, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", 0.0, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());

    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
    assertEquals(
        true,
        subsystem.startFrontLeftDriveVelocityCommissioning(
            SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.DRIVE_POSITIVE));
    dispatchLog.clear();

    subsystem.periodic();

    assertEquals(0, dispatchLog.size());
    assertEquals(1, frontLeft.driveVelocityCount);
    assertEquals(0, frontRight.driveVelocityCount);
    assertEquals(0, backLeft.driveVelocityCount);
    assertEquals(0, backRight.driveVelocityCount);

    subsystem.stopFrontLeftCommissioning();
    subsystem.periodic();
    assertEquals(0, dispatchLog.size());

    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.5, 1.0));
    subsystem.periodic();

    assertIterableEquals(
        List.of("FL.drive", "FL.steer", "FR.drive", "FR.steer", "BL.drive", "BL.steer",
            "BR.drive", "BR.steer"),
        dispatchLog);
  }

  @Test
  void explicitStopDoesNotReissueStatesWhileDisabled() {
    List<String> dispatchLog = new ArrayList<>();
    RecordingModuleIO frontLeft = new RecordingModuleIO("FL", 0.0, dispatchLog);
    RecordingModuleIO frontRight = new RecordingModuleIO("FR", 0.0, dispatchLog);
    RecordingModuleIO backLeft = new RecordingModuleIO("BL", 0.0, dispatchLog);
    RecordingModuleIO backRight = new RecordingModuleIO("BR", 0.0, dispatchLog);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft, frontRight, backLeft, backRight, new RecordingGyroIO());
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.5, 1.0));
    subsystem.periodic();
    int dispatchCountBeforeStop = dispatchLog.size();

    subsystem.stop();
    subsystem.periodic();
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
    subsystem.periodic();

    assertEquals(dispatchCountBeforeStop, dispatchLog.size());
    assertEquals(1, frontLeft.stopCount);
    assertEquals(1, frontRight.stopCount);
    assertEquals(1, backLeft.stopCount);
    assertEquals(1, backRight.stopCount);
  }

  private static void assertStateEquals(
      SwerveModuleState expected, SwerveModuleState actual) {
    assertEquals(expected.speedMetersPerSecond, actual.speedMetersPerSecond, kTolerance);
    assertEquals(expected.angle.getRadians(), actual.angle.getRadians(), kTolerance);
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final String name;
    private final double encoderAbsolutePositionRotations;
    private final List<String> dispatchLog;
    private int driveVelocityCount;
    private int steerAngleCount;
    private int updateCount;
    private int stopCount;
    private double lastDriveVelocityMetersPerSecond;
    private Rotation2d lastSteerAngle = new Rotation2d();

    private RecordingModuleIO(String name) {
      this(name, 0.0, new ArrayList<>());
    }

    private RecordingModuleIO(
        String name,
        double encoderAbsolutePositionRotations,
        List<String> dispatchLog) {
      this.name = name;
      this.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
      this.dispatchLog = dispatchLog;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      updateCount++;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveVelocityCount++;
      lastDriveVelocityMetersPerSecond = velocityMetersPerSecond;
      dispatchLog.add(name + ".drive");
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerAngleCount++;
      lastSteerAngle = angle;
      dispatchLog.add(name + ".steer");
    }

    @Override
    public void stop() {
      stopCount++;
    }

    private SwerveModuleState lastState() {
      return new SwerveModuleState(lastDriveVelocityMetersPerSecond, lastSteerAngle);
    }

  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
