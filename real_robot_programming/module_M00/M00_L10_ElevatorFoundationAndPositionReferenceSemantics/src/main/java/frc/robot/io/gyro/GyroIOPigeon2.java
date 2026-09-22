// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.gyro;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.AngularVelocity;
import frc.robot.Constants.SwerveConstants;

/**
 * Reads raw orientation and angular velocity from the verified CTRE Pigeon2.
 */
public class GyroIOPigeon2 implements GyroIO {
  private final Pigeon2 pigeon = new Pigeon2(SwerveConstants.kPigeonCanId);

  private final StatusSignal<Angle> yawSignal = pigeon.getYaw();
  private final StatusSignal<Angle> pitchSignal = pigeon.getPitch();
  private final StatusSignal<Angle> rollSignal = pigeon.getRoll();
  private final StatusSignal<AngularVelocity> angularVelocityXSignal =
      pigeon.getAngularVelocityXDevice();
  private final StatusSignal<AngularVelocity> angularVelocityYSignal =
      pigeon.getAngularVelocityYDevice();
  private final StatusSignal<AngularVelocity> angularVelocityZSignal =
      pigeon.getAngularVelocityZDevice();

  private final boolean configurationHealthy;

  /**
   * Creates the Pigeon2 read-only hardware boundary.
   */
  public GyroIOPigeon2() {
    configurationHealthy =
        pigeon
            .getConfigurator()
            .refresh(new Pigeon2Configuration())
            .isOK();
  }

  /**
   * Updates raw gyro signals without offset or inversion processing.
   *
   * @param inputs snapshot to update
   */
  @Override
  public void updateInputs(GyroIOInputs inputs) {
    StatusCode refreshStatus =
        BaseStatusSignal.refreshAll(
            yawSignal,
            pitchSignal,
            rollSignal,
            angularVelocityXSignal,
            angularVelocityYSignal,
            angularVelocityZSignal);

    inputs.yawDegrees = yawSignal.getValue().in(Units.Degrees);
    inputs.pitchDegrees = pitchSignal.getValue().in(Units.Degrees);
    inputs.rollDegrees = rollSignal.getValue().in(Units.Degrees);
    inputs.angularVelocityXDegreesPerSecond =
        angularVelocityXSignal.getValue().in(Units.DegreesPerSecond);
    inputs.angularVelocityYDegreesPerSecond =
        angularVelocityYSignal.getValue().in(Units.DegreesPerSecond);
    inputs.angularVelocityZDegreesPerSecond =
        angularVelocityZSignal.getValue().in(Units.DegreesPerSecond);
    inputs.connected = refreshStatus.isOK();
    inputs.configurationHealthy = configurationHealthy;
  }
}
