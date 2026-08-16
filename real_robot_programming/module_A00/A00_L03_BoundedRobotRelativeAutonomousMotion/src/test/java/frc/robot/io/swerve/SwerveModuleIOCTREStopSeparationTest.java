// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SwerveModuleIOCTREStopSeparationTest {
  @Test
  void driveOnlyStopDoesNotStopOrInterruptSteerControl() {
    RecordingModuleControl control = new RecordingModuleControl();
    control.setSteerPosition();

    SwerveModuleIOCTRE.stopDriveMotorOnly(control::stopDrive);

    assertEquals(1, control.driveStopCount);
    assertEquals(0, control.steerStopCount);
    assertTrue(control.steerPositionControlActive);

    control.setSteerPosition();

    assertEquals(2, control.steerPositionRequestCount);
    assertEquals(0, control.steerStopCount);
    assertTrue(control.steerPositionControlActive);
  }

  @Test
  void moduleStopStopsBothDriveAndSteer() {
    RecordingModuleControl control = new RecordingModuleControl();
    control.setSteerPosition();

    SwerveModuleIOCTRE.stopModuleMotors(
        control::stopDrive,
        control::stopSteer);

    assertEquals(1, control.driveStopCount);
    assertEquals(1, control.steerStopCount);
    assertFalse(control.steerPositionControlActive);
  }

  @Test
  void nonfiniteDriveRequestsRemainFullModuleStopConditions() {
    assertTrue(
        SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(true, Double.NaN));
    assertTrue(
        SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(
            true, Double.POSITIVE_INFINITY));
    assertTrue(
        SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(
            true, Double.NEGATIVE_INFINITY));
  }

  @Test
  void unhealthyConfigurationRemainsFailClosedForEveryDriveRequest() {
    assertTrue(SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(false, 0.0));
    assertTrue(SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(false, 1.0));
    assertFalse(SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(true, 0.0));
    assertFalse(SwerveModuleIOCTRE.driveVelocityRequestRequiresFullModuleStop(true, 1.0));
  }

  private static final class RecordingModuleControl {
    private int driveStopCount;
    private int steerStopCount;
    private int steerPositionRequestCount;
    private boolean steerPositionControlActive;

    private void stopDrive() {
      driveStopCount++;
    }

    private void stopSteer() {
      steerStopCount++;
      steerPositionControlActive = false;
    }

    private void setSteerPosition() {
      steerPositionRequestCount++;
      steerPositionControlActive = true;
    }
  }
}
