// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the L23 raw-rotor to SwerveModulePosition measurement foundation. */
class SwerveSubsystemModulePositionTest {
  private static final double kTolerance = 1.0e-9;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void returnsEmptyPositionsBeforeFirstObservationRefresh() {
    Rig rig = new Rig();

    assertEquals(0, rig.subsystem.getMeasuredModulePositions().length);
  }

  @Test
  void zeroRotorPositionProducesZeroDistance() {
    Rig rig = new Rig();
    rig.periodic();

    SwerveModulePosition[] positions = rig.subsystem.getMeasuredModulePositions();

    assertEquals(4, positions.length);
    for (SwerveModulePosition position : positions) {
      assertEquals(0.0, position.distanceMeters, kTolerance);
    }
  }

  @Test
  void positiveRotorPositionProducesPositiveDistance() {
    Rig rig = new Rig();
    rig.frontLeft.drivePositionRotations = 2.0;
    rig.periodic();

    double expectedDistanceMeters = expectedDistanceMeters(2.0);

    assertEquals(
        expectedDistanceMeters,
        rig.subsystem.getMeasuredModulePositions()[0].distanceMeters,
        kTolerance);
  }

  @Test
  void negativeRotorPositionProducesNegativeDistance() {
    Rig rig = new Rig();
    rig.frontLeft.drivePositionRotations = -2.0;
    rig.periodic();

    double expectedDistanceMeters = expectedDistanceMeters(-2.0);

    assertEquals(
        expectedDistanceMeters,
        rig.subsystem.getMeasuredModulePositions()[0].distanceMeters,
        kTolerance);
  }

  @Test
  void conversionUsesConfiguredRatioAndWheelCircumference() {
    Rig rig = new Rig();
    double wheelRotations = 1.75;
    rig.frontLeft.drivePositionRotations =
        wheelRotations * Constants.SwerveConstants.kDriveGearRatio;
    rig.periodic();

    double expectedDistanceMeters =
        wheelRotations * (2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters);

    assertEquals(
        expectedDistanceMeters,
        rig.subsystem.getMeasuredModulePositions()[0].distanceMeters,
        kTolerance);
  }

  @Test
  void preservesFrontLeftFrontRightBackLeftBackRightOrderingAndOwnAngles() {
    Rig rig = new Rig();
    rig.frontLeft.drivePositionRotations = 1.0;
    rig.frontLeft.encoderAbsolutePositionRotations = 0.10;
    rig.frontRight.drivePositionRotations = 2.0;
    rig.frontRight.encoderAbsolutePositionRotations = 0.20;
    rig.backLeft.drivePositionRotations = 3.0;
    rig.backLeft.encoderAbsolutePositionRotations = 0.30;
    rig.backRight.drivePositionRotations = 4.0;
    rig.backRight.encoderAbsolutePositionRotations = 0.40;
    rig.periodic();

    SwerveModulePosition[] positions = rig.subsystem.getMeasuredModulePositions();

    assertModulePosition(1.0, 0.10, positions[0]);
    assertModulePosition(2.0, 0.20, positions[1]);
    assertModulePosition(3.0, 0.30, positions[2]);
    assertEquals(expectedDistanceMeters(4.0), positions[3].distanceMeters, kTolerance);
    assertEquals(
        Rotation2d.fromRotations(0.40).getRadians(),
        positions[3].angle.getRadians(),
        kTolerance);
  }

  @Test
  void normalizesPhysicalForwardDistanceSignsAcrossAllFourModules() {
    Rig rig = new Rig();
    double rawForwardRotorRotations = 2.0;
    rig.frontLeft.drivePositionRotations = rawForwardRotorRotations;
    rig.frontRight.drivePositionRotations = rawForwardRotorRotations;
    rig.backLeft.drivePositionRotations = rawForwardRotorRotations;
    rig.backRight.drivePositionRotations = rawForwardRotorRotations;
    rig.periodic();

    double expectedDistance = expectedDistanceMeters(rawForwardRotorRotations);
    for (SwerveModulePosition position : rig.subsystem.getMeasuredModulePositions()) {
      assertEquals(expectedDistance, position.distanceMeters, kTolerance);
    }
  }

  @Test
  void repeatedReadsAreFiniteDeterministicAndDefensive() {
    Rig rig = new Rig();
    rig.frontLeft.drivePositionRotations = 1.25;
    rig.frontLeft.encoderAbsolutePositionRotations = 0.125;
    rig.periodic();

    SwerveModulePosition[] firstRead = rig.subsystem.getMeasuredModulePositions();
    SwerveModulePosition[] secondRead = rig.subsystem.getMeasuredModulePositions();

    assertNotSame(firstRead, secondRead);
    for (int moduleIndex = 0; moduleIndex < firstRead.length; moduleIndex++) {
      assertEquals(firstRead[moduleIndex].distanceMeters, secondRead[moduleIndex].distanceMeters);
      assertEquals(firstRead[moduleIndex].angle.getRadians(), secondRead[moduleIndex].angle.getRadians());
      assertEquals(true, Double.isFinite(firstRead[moduleIndex].distanceMeters));
      assertEquals(true, Double.isFinite(firstRead[moduleIndex].angle.getRadians()));
    }
  }

  private static double expectedDistanceMeters(double rotorPositionRotations) {
    return rotorPositionRotations / Constants.SwerveConstants.kDriveGearRatio
        * (2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters);
  }

  private static void assertModulePosition(
      double rotorPositionRotations,
      double expectedAngleRotations,
      SwerveModulePosition actual) {
    assertEquals(expectedDistanceMeters(rotorPositionRotations), actual.distanceMeters, kTolerance);
    assertEquals(
        Rotation2d.fromRotations(expectedAngleRotations).getRadians(),
        actual.angle.getRadians(),
        kTolerance);
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new NoopGyroIO());

    private void periodic() {
      subsystem.periodic();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private double drivePositionRotations;
    private double encoderAbsolutePositionRotations;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.drivePositionRotations = drivePositionRotations;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

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
