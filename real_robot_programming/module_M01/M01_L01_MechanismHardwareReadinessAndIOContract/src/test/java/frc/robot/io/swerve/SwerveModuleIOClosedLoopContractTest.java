// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.swerve;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.first.math.geometry.Rotation2d;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class SwerveModuleIOClosedLoopContractTest {
  @Test
  void exposesVendorNeutralDriveVelocityMethodInMetersPerSecond() throws NoSuchMethodException {
    Method method =
        SwerveModuleIO.class.getMethod("setDriveVelocityMetersPerSecond", double.class);

    assertEquals(void.class, method.getReturnType());
  }

  @Test
  void exposesVendorNeutralSteerAngleMethodUsingRotation2d() throws NoSuchMethodException {
    Method method = SwerveModuleIO.class.getMethod("setSteerAngle", Rotation2d.class);

    assertEquals(void.class, method.getReturnType());
  }

  @Test
  void exposesVendorNeutralStaticFrictionVoltageMethodInVolts() throws NoSuchMethodException {
    Method method =
        SwerveModuleIO.class.getMethod(
            "setDriveStaticFrictionCharacterizationVoltageVolts", double.class);

    assertEquals(boolean.class, method.getReturnType());
  }

  @Test
  void exposesTypedStaticFrictionFinalizationReason() throws NoSuchMethodException {
    Method method =
        SwerveModuleIO.class.getMethod(
            "finishDriveStaticFrictionCharacterization",
            double.class,
            SwerveModuleIO.StaticFrictionStopReason.class);

    assertEquals(void.class, method.getReturnType());
  }
}
