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
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private int updateCount;
    private int driveOutputCount;
    private int stopCount;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      updateCount++;
    }

    @Override
    public void setDriveOutput(double output) {
      driveOutputCount++;
    }

    @Override
    public void setSteerOutput(double output) {}

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
