// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import org.junit.jupiter.api.Test;

class SwerveSubsystemTest {
  @Test
  void initializesWithZeroIntent() throws ReflectiveOperationException {
    SwerveSubsystem subsystem = createSubsystem();

    assertIntent(subsystem, 0.0, 0.0, 0.0);
  }

  @Test
  void copiesAllChassisSpeedScalarsAndIsolatesCallerMutation()
      throws ReflectiveOperationException {
    SwerveSubsystem subsystem = createSubsystem();
    ChassisSpeeds speeds = new ChassisSpeeds(1.25, -0.75, 2.5);

    subsystem.acceptChassisSpeeds(speeds);
    speeds.vxMetersPerSecond = 99.0;
    speeds.vyMetersPerSecond = 98.0;
    speeds.omegaRadiansPerSecond = 97.0;

    assertIntent(subsystem, 1.25, -0.75, 2.5);
  }

  @Test
  void rejectsNullChassisSpeeds() {
    SwerveSubsystem subsystem = createSubsystem();

    assertThrows(NullPointerException.class, () -> subsystem.acceptChassisSpeeds(null));
  }

  @Test
  void stopZerosIntentAndDelegatesToEveryModule() throws ReflectiveOperationException {
    RecordingModuleIO frontLeft = new RecordingModuleIO();
    RecordingModuleIO frontRight = new RecordingModuleIO();
    RecordingModuleIO backLeft = new RecordingModuleIO();
    RecordingModuleIO backRight = new RecordingModuleIO();
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new RecordingGyroIO());
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 2.0, 3.0));

    subsystem.stop();

    assertIntent(subsystem, 0.0, 0.0, 0.0);
    assertEquals(1, frontLeft.stopCount);
    assertEquals(1, frontRight.stopCount);
    assertEquals(1, backLeft.stopCount);
    assertEquals(1, backRight.stopCount);
  }

  @Test
  void periodicRefreshesObservationsWithoutActuation() {
    RecordingModuleIO frontLeft = new RecordingModuleIO();
    RecordingModuleIO frontRight = new RecordingModuleIO();
    RecordingModuleIO backLeft = new RecordingModuleIO();
    RecordingModuleIO backRight = new RecordingModuleIO();
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new RecordingGyroIO());
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 2.0, 3.0));

    subsystem.periodic();

    assertEquals(1, frontLeft.updateCount);
    assertEquals(1, frontRight.updateCount);
    assertEquals(1, backLeft.updateCount);
    assertEquals(1, backRight.updateCount);
    assertEquals(0, frontLeft.driveOutputCount);
    assertEquals(0, frontRight.driveOutputCount);
    assertEquals(0, backLeft.driveOutputCount);
    assertEquals(0, backRight.driveOutputCount);
    assertEquals(0, frontLeft.stopCount);
    assertEquals(0, frontRight.stopCount);
    assertEquals(0, backLeft.stopCount);
    assertEquals(0, backRight.stopCount);
  }

  @Test
  void pipelineOutputIsOwnedInFrontLeftFrontRightBackLeftBackRightOrder() {
    RecordingModuleIO frontLeft = new RecordingModuleIO(0.0);
    RecordingModuleIO frontRight = new RecordingModuleIO(0.25);
    RecordingModuleIO backLeft = new RecordingModuleIO(0.5);
    RecordingModuleIO backRight = new RecordingModuleIO(0.75);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new RecordingGyroIO());
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
    SwerveModuleState[] actual = subsystem.getFinalModuleStates();

    assertEquals(4, actual.length);
    for (int moduleIndex = 0; moduleIndex < actual.length; moduleIndex++) {
      assertStateEquals(expected[moduleIndex], actual[moduleIndex]);
    }
  }

  @Test
  void finalStatesAreDeterministicAcrossRepeatedReads() {
    SwerveSubsystem subsystem = createSubsystem();
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, -0.5, 0.75));
    subsystem.periodic();

    SwerveModuleState[] firstRead = subsystem.getFinalModuleStates();
    SwerveModuleState[] secondRead = subsystem.getFinalModuleStates();

    for (int moduleIndex = 0; moduleIndex < firstRead.length; moduleIndex++) {
      assertStateEquals(firstRead[moduleIndex], secondRead[moduleIndex]);
    }
  }

  @Test
  void returnedFinalStatesCannotCorruptSubsystemOwnedStates() {
    SwerveSubsystem subsystem = createSubsystem();
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    subsystem.periodic();

    SwerveModuleState[] firstRead = subsystem.getFinalModuleStates();
    double originalFirstSpeedMetersPerSecond = firstRead[0].speedMetersPerSecond;
    double originalFirstAngleRadians = firstRead[0].angle.getRadians();
    firstRead[0].speedMetersPerSecond = 99.0;
    firstRead[0].angle = new Rotation2d(2.0);
    firstRead[1] = new SwerveModuleState(88.0, new Rotation2d(1.0));

    SwerveModuleState[] secondRead = subsystem.getFinalModuleStates();

    assertNotSame(firstRead, secondRead);
    assertNotSame(firstRead[0], secondRead[0]);
    assertEquals(
        originalFirstSpeedMetersPerSecond,
        secondRead[0].speedMetersPerSecond);
    assertEquals(originalFirstAngleRadians, secondRead[0].angle.getRadians());
    assertEquals(1.0, secondRead[1].speedMetersPerSecond);
  }

  private static SwerveSubsystem createSubsystem() {
    return new SwerveSubsystem(
        new RecordingModuleIO(),
        new RecordingModuleIO(),
        new RecordingModuleIO(),
        new RecordingModuleIO(),
        new RecordingGyroIO());
  }

  private static void assertIntent(
      SwerveSubsystem subsystem,
      double expectedVx,
      double expectedVy,
      double expectedOmega)
      throws ReflectiveOperationException {
    Field intentField = SwerveSubsystem.class.getDeclaredField("chassisIntent");
    intentField.setAccessible(true);
    Object intent = intentField.get(subsystem);
    assertNotNull(intent);

    for (RecordComponent component : intent.getClass().getRecordComponents()) {
      component.getAccessor().setAccessible(true);
      double actual = (double) component.getAccessor().invoke(intent);
      double expected =
          switch (component.getName()) {
            case "vxMetersPerSecond" -> expectedVx;
            case "vyMetersPerSecond" -> expectedVy;
            case "omegaRadiansPerSecond" -> expectedOmega;
            default -> throw new AssertionError("Unexpected intent component");
          };
      assertEquals(expected, actual);
    }
  }

  private static void assertStateEquals(
      SwerveModuleState expected, SwerveModuleState actual) {
    assertEquals(expected.speedMetersPerSecond, actual.speedMetersPerSecond, 1.0e-9);
    assertEquals(expected.angle.getRadians(), actual.angle.getRadians(), 1.0e-9);
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final double encoderAbsolutePositionRotations;
    private int updateCount;
    private int driveOutputCount;
    private int stopCount;

    private RecordingModuleIO() {
      this(0.0);
    }

    private RecordingModuleIO(double encoderAbsolutePositionRotations) {
      this.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      updateCount++;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

    @Override
    public void setDriveOutput(double output) {
      driveOutputCount++;
    }

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

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
