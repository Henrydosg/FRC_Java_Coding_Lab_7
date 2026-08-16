// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIOSim;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.io.swerve.SwerveModuleIOSim;
import frc.robot.observation.SwerveObservation;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveSimulationIntegrationTest {
  private static final double PERIOD_SECONDS = 0.02;
  private static final double TOLERANCE = 1.0e-6;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetDriverStation() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void rotationalChassisIntentAdvancesCounterclockwiseYawFromActualModuleStates() {
    Rig rig = new Rig();
    rig.periodic();
    enableTeleop();
    rig.subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.0, 0.0, 1.0));
    rig.periodic();

    rig.advanceOneCycle();
    double transientRate = observation(rig).gyro().angularVelocityZDegreesPerSecond();
    assertTrue(transientRate > 0.0);
    assertTrue(transientRate < Math.toDegrees(1.0));

    for (int cycle = 0; cycle < 10; cycle++) {
      rig.advanceOneCycle();
    }
    SwerveObservation settled = observation(rig);
    assertEquals(Math.toDegrees(1.0),
        settled.gyro().angularVelocityZDegreesPerSecond(), TOLERANCE);
    assertTrue(settled.gyro().yawDegrees() > 0.0);
    assertTrue(settled.gyro().connected());
    assertTrue(settled.gyro().configurationHealthy());

    double movingYaw = settled.gyro().yawDegrees();
    rig.subsystem.stop();
    rig.advanceOneCycle();
    SwerveObservation stopped = observation(rig);
    assertEquals(0.0, stopped.gyro().angularVelocityZDegreesPerSecond(), TOLERANCE);
    assertEquals(movingYaw, stopped.gyro().yawDegrees(), TOLERANCE);
  }

  private static SwerveObservation observation(Rig rig) {
    return rig.subsystem.getObservation().orElseThrow();
  }

  private static void enableTeleop() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static final class Rig {
    private final MutableClock clock = new MutableClock();
    private final SwerveSimulationState simulationState = new SwerveSimulationState();
    private final SwerveKinematics kinematics = new SwerveKinematics();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(
            module(Constants.SwerveConstants.kFrontLeftDrivePositionSign, ModuleIdentity.FRONT_LEFT),
            module(Constants.SwerveConstants.kFrontRightDrivePositionSign, ModuleIdentity.FRONT_RIGHT),
            module(Constants.SwerveConstants.kBackLeftDrivePositionSign, ModuleIdentity.BACK_LEFT),
            module(Constants.SwerveConstants.kBackRightDrivePositionSign, ModuleIdentity.BACK_RIGHT),
            new GyroIOSim(simulationState, kinematics::toChassisSpeeds, clock));

    private SwerveModuleIOSim module(double sign, ModuleIdentity identity) {
      return new SwerveModuleIOSim(sign, clock, simulationState, identity);
    }

    private void periodic() {
      subsystem.periodic();
    }

    private void advanceOneCycle() {
      clock.seconds += PERIOD_SECONDS;
      periodic();
    }
  }

  private static final class MutableClock implements DoubleSupplier {
    private double seconds;

    @Override
    public double getAsDouble() {
      return seconds;
    }
  }
}
