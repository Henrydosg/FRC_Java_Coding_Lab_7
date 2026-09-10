// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.io.simulation.SwerveSimulationState.Snapshot;
import org.junit.jupiter.api.Test;

class SwerveSimulationStateTest {
  private static final double TOLERANCE = 1.0e-9;

  @Test
  void commitsOnlyAfterCoherentFrontLeftFrontRightBackLeftBackRightFrame() {
    SwerveSimulationState state = new SwerveSimulationState();

    assertTrue(publish(state, ModuleIdentity.FRONT_LEFT, 1.0, 0.10, true));
    assertTrue(state.latestSnapshot().isEmpty());
    assertTrue(publish(state, ModuleIdentity.FRONT_RIGHT, 2.0, 0.20, true));
    assertTrue(state.latestSnapshot().isEmpty());
    assertTrue(publish(state, ModuleIdentity.BACK_LEFT, 3.0, 0.30, true));
    assertTrue(state.latestSnapshot().isEmpty());
    assertTrue(publish(state, ModuleIdentity.BACK_RIGHT, 4.0, 0.40, true));

    Snapshot snapshot = state.latestSnapshot().orElseThrow();
    assertEquals(1L, snapshot.generation());
    assertStates(snapshot.moduleStates(), 1.0, 2.0, 3.0, 4.0);
  }

  @Test
  void outOfOrderOrDuplicatePublicationInvalidatesCommittedAndPendingState() {
    SwerveSimulationState state = new SwerveSimulationState();
    publishFrame(state, 1.0);
    assertTrue(state.latestSnapshot().isPresent());

    assertFalse(publish(state, ModuleIdentity.FRONT_RIGHT, 2.0, 0.0, true));
    assertTrue(state.latestSnapshot().isEmpty());
    assertTrue(publish(state, ModuleIdentity.FRONT_LEFT, 2.0, 0.0, true));
    assertFalse(publish(state, ModuleIdentity.FRONT_LEFT, 2.0, 0.0, true));
    assertTrue(state.latestSnapshot().isEmpty());
  }

  @Test
  void nonfiniteOrUnhealthyPublicationFailsClosed() {
    SwerveSimulationState state = new SwerveSimulationState();
    publishFrame(state, 1.0);

    assertFalse(publish(state, ModuleIdentity.FRONT_LEFT, Double.NaN, 0.0, true));
    assertTrue(state.latestSnapshot().isEmpty());
    assertFalse(publish(state, ModuleIdentity.FRONT_LEFT, 1.0, Double.NaN, true));
    assertTrue(state.latestSnapshot().isEmpty());
    assertFalse(publish(state, ModuleIdentity.FRONT_LEFT, 1.0, 0.0, false));
    assertTrue(state.latestSnapshot().isEmpty());
  }

  @Test
  void completeReplacementIsAtomicAndGenerationIsMonotonic() {
    SwerveSimulationState state = new SwerveSimulationState();
    publishFrame(state, 1.0);
    Snapshot first = state.latestSnapshot().orElseThrow();

    publish(state, ModuleIdentity.FRONT_LEFT, 5.0, 0.0, true);
    publish(state, ModuleIdentity.FRONT_RIGHT, 6.0, 0.0, true);
    publish(state, ModuleIdentity.BACK_LEFT, 7.0, 0.0, true);
    Snapshot duringReplacement = state.latestSnapshot().orElseThrow();
    assertEquals(first.generation(), duringReplacement.generation());
    assertStates(duringReplacement.moduleStates(), 1.0, 2.0, 3.0, 4.0);

    publish(state, ModuleIdentity.BACK_RIGHT, 8.0, 0.0, true);
    Snapshot second = state.latestSnapshot().orElseThrow();
    assertEquals(first.generation() + 1L, second.generation());
    assertStates(second.moduleStates(), 5.0, 6.0, 7.0, 8.0);
  }

  @Test
  void snapshotsDefensivelyOwnEveryModuleState() {
    SwerveSimulationState state = new SwerveSimulationState();
    publishFrame(state, 1.0);
    Snapshot snapshot = state.latestSnapshot().orElseThrow();

    SwerveModuleState[] firstRead = snapshot.moduleStates();
    firstRead[0].speedMetersPerSecond = 99.0;
    firstRead[0].angle = Rotation2d.fromRotations(0.75);
    firstRead[1] = new SwerveModuleState(88.0, Rotation2d.fromRotations(0.50));

    SwerveModuleState[] secondRead = snapshot.moduleStates();
    assertStates(secondRead, 1.0, 2.0, 3.0, 4.0);
    assertEquals(0.0, secondRead[0].angle.getRotations(), TOLERANCE);
  }

  private static void publishFrame(SwerveSimulationState state, double firstVelocity) {
    assertTrue(publish(state, ModuleIdentity.FRONT_LEFT, firstVelocity, 0.0, true));
    assertTrue(publish(state, ModuleIdentity.FRONT_RIGHT, firstVelocity + 1.0, 0.0, true));
    assertTrue(publish(state, ModuleIdentity.BACK_LEFT, firstVelocity + 2.0, 0.0, true));
    assertTrue(publish(state, ModuleIdentity.BACK_RIGHT, firstVelocity + 3.0, 0.0, true));
  }

  private static boolean publish(
      SwerveSimulationState state,
      ModuleIdentity identity,
      double velocityMetersPerSecond,
      double angleRotations,
      boolean healthy) {
    return state.publish(
        identity,
        velocityMetersPerSecond,
        Rotation2d.fromRotations(angleRotations),
        healthy);
  }

  private static void assertStates(
      SwerveModuleState[] states, double frontLeft, double frontRight, double backLeft, double backRight) {
    assertEquals(4, states.length);
    assertEquals(frontLeft, states[0].speedMetersPerSecond, TOLERANCE);
    assertEquals(frontRight, states[1].speedMetersPerSecond, TOLERANCE);
    assertEquals(backLeft, states[2].speedMetersPerSecond, TOLERANCE);
    assertEquals(backRight, states[3].speedMetersPerSecond, TOLERANCE);
  }
}
