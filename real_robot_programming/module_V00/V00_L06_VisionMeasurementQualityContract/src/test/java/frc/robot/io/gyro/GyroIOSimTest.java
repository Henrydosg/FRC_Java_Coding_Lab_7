// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.gyro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.subsystems.SwerveKinematics;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.Test;

class GyroIOSimTest {
  private static final double TOLERANCE = 1.0e-9;

  @Test
  void initialCoherentZeroStateIsHealthyAndStationary() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds());

    GyroIO.GyroIOInputs inputs = rig.update();

    assertHealthy(inputs);
    assertEquals(0.0, inputs.yawDegrees, TOLERANCE);
    assertEquals(0.0, inputs.angularVelocityZDegreesPerSecond, TOLERANCE);
    assertEquals(0.0, inputs.pitchDegrees, TOLERANCE);
    assertEquals(0.0, inputs.rollDegrees, TOLERANCE);
    assertEquals(0.0, inputs.angularVelocityXDegreesPerSecond, TOLERANCE);
    assertEquals(0.0, inputs.angularVelocityYDegreesPerSecond, TOLERANCE);
  }

  @Test
  void pureForwardAndStrafeReportZeroOmega() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds(1.0, 0.0, 0.0));
    assertEquals(0.0, rig.update().angularVelocityZDegreesPerSecond, TOLERANCE);

    rig.clock.seconds = 1.0;
    rig.publish(new ChassisSpeeds(0.0, 1.0, 0.0));
    GyroIO.GyroIOInputs inputs = rig.update();
    assertEquals(0.0, inputs.angularVelocityZDegreesPerSecond, TOLERANCE);
    assertEquals(0.0, inputs.yawDegrees, TOLERANCE);
  }

  @Test
  void positiveCounterclockwiseAndNegativeClockwiseUseWpilibSignAndUnits() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    GyroIO.GyroIOInputs initial = rig.update();
    assertEquals(Math.toDegrees(1.0), initial.angularVelocityZDegreesPerSecond, TOLERANCE);

    rig.clock.seconds = 2.0;
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    GyroIO.GyroIOInputs counterclockwise = rig.update();
    assertEquals(Math.toDegrees(2.0), counterclockwise.yawDegrees, TOLERANCE);

    rig.clock.seconds = 3.0;
    rig.publish(new ChassisSpeeds(0.0, 0.0, -1.0));
    GyroIO.GyroIOInputs clockwise = rig.update();
    assertEquals(Math.toDegrees(1.0), clockwise.yawDegrees, TOLERANCE);
    assertEquals(-Math.toDegrees(1.0), clockwise.angularVelocityZDegreesPerSecond, TOLERANCE);
  }

  @Test
  void deterministicYawRemainsContinuousThroughOneHundredEightyAndThreeHundredSixtyDegrees() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds(0.0, 0.0, Math.PI));
    rig.update();

    rig.clock.seconds = 1.1;
    rig.publish(new ChassisSpeeds(0.0, 0.0, Math.PI));
    assertEquals(198.0, rig.update().yawDegrees, TOLERANCE);

    rig.clock.seconds = 2.1;
    rig.publish(new ChassisSpeeds(0.0, 0.0, Math.PI));
    assertEquals(378.0, rig.update().yawDegrees, TOLERANCE);
  }

  @Test
  void stoppedFrameHoldsYawAndReportsZeroRate() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    rig.update();
    rig.clock.seconds = 1.0;
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    double movingYaw = rig.update().yawDegrees;

    rig.clock.seconds = 2.0;
    rig.publish(new ChassisSpeeds());
    GyroIO.GyroIOInputs stopped = rig.update();
    assertEquals(movingYaw, stopped.yawDegrees, TOLERANCE);
    assertEquals(0.0, stopped.angularVelocityZDegreesPerSecond, TOLERANCE);
  }

  @Test
  void sameGenerationNeverReintegratesYaw() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    rig.update();
    rig.clock.seconds = 1.0;
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    double integratedYaw = rig.update().yawDegrees;

    rig.clock.seconds = 10.0;
    GyroIO.GyroIOInputs repeated = rig.update();
    assertEquals(integratedYaw, repeated.yawDegrees, TOLERANCE);
  }

  @Test
  void invalidBackwardAndOverflowingClockFailClosedAndPreserveYaw() {
    Rig rig = new Rig();
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    rig.update();
    rig.clock.seconds = 1.0;
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    double validYaw = rig.update().yawDegrees;

    rig.clock.seconds = 0.5;
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    assertFailedClosed(rig.update(), validYaw);

    rig.clock.seconds = Double.NaN;
    rig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    assertFailedClosed(rig.update(), validYaw);

    Rig overflowRig = new Rig();
    overflowRig.clock.seconds = -Double.MAX_VALUE;
    overflowRig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    overflowRig.update();
    overflowRig.clock.seconds = Double.MAX_VALUE;
    overflowRig.publish(new ChassisSpeeds(0.0, 0.0, 1.0));
    assertFailedClosed(overflowRig.update(), 0.0);
  }

  @Test
  void missingCoherentFrameFailsClosed() {
    Rig rig = new Rig();
    rig.state.publish(ModuleIdentity.FRONT_LEFT, 0.0, new SwerveModuleState().angle, true);

    assertFailedClosed(rig.update(), 0.0);
  }

  private static void assertHealthy(GyroIO.GyroIOInputs inputs) {
    assertTrue(inputs.connected);
    assertTrue(inputs.configurationHealthy);
  }

  private static void assertFailedClosed(GyroIO.GyroIOInputs inputs, double expectedYaw) {
    assertFalse(inputs.connected);
    assertFalse(inputs.configurationHealthy);
    assertEquals(expectedYaw, inputs.yawDegrees, TOLERANCE);
    assertEquals(0.0, inputs.angularVelocityZDegreesPerSecond, TOLERANCE);
  }

  private static final class Rig {
    private final MutableClock clock = new MutableClock();
    private final SwerveSimulationState state = new SwerveSimulationState();
    private final SwerveKinematics kinematics = new SwerveKinematics();
    private final GyroIOSim gyro = new GyroIOSim(state, kinematics::toChassisSpeeds, clock);

    private void publish(ChassisSpeeds speeds) {
      SwerveModuleState[] states = kinematics.toModuleStates(speeds);
      ModuleIdentity[] identities = ModuleIdentity.values();
      for (int moduleIndex = 0; moduleIndex < states.length; moduleIndex++) {
        state.publish(
            identities[moduleIndex],
            states[moduleIndex].speedMetersPerSecond,
            states[moduleIndex].angle,
            true);
      }
    }

    private GyroIO.GyroIOInputs update() {
      GyroIO.GyroIOInputs inputs = new GyroIO.GyroIOInputs();
      gyro.updateInputs(inputs);
      return inputs;
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
