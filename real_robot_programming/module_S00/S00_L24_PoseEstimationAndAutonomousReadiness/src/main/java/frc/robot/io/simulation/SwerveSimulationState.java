// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.simulation;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import java.util.Arrays;
import java.util.Optional;

/**
 * Owns coherent vendor-neutral module-state frames for one simulated Swerve drivetrain.
 *
 * <p>Publishers must update in Front Left, Front Right, Back Left, Back Right order. A frame is
 * exposed only after all four healthy finite samples commit atomically.
 */
public final class SwerveSimulationState {
  private static final int MODULE_COUNT = 4;

  /** Fixed module identity and publication order. */
  public enum ModuleIdentity {
    FRONT_LEFT(0),
    FRONT_RIGHT(1),
    BACK_LEFT(2),
    BACK_RIGHT(3);

    private final int index;

    ModuleIdentity(int index) {
      this.index = index;
    }
  }

  /** Immutable coherent four-module state snapshot. */
  public static final class Snapshot {
    private final long generation;
    private final ModuleSample frontLeft;
    private final ModuleSample frontRight;
    private final ModuleSample backLeft;
    private final ModuleSample backRight;

    private Snapshot(
        long generation,
        ModuleSample frontLeft,
        ModuleSample frontRight,
        ModuleSample backLeft,
        ModuleSample backRight) {
      this.generation = generation;
      this.frontLeft = frontLeft;
      this.frontRight = frontRight;
      this.backLeft = backLeft;
      this.backRight = backRight;
    }

    /** Returns the monotonically increasing committed-frame generation. */
    public long generation() {
      return generation;
    }

    /** Returns defensive module-state copies in fixed FL, FR, BL, BR order. */
    public SwerveModuleState[] moduleStates() {
      return new SwerveModuleState[] {
        frontLeft.toModuleState(),
        frontRight.toModuleState(),
        backLeft.toModuleState(),
        backRight.toModuleState()
      };
    }
  }

  private record ModuleSample(
      double wheelVelocityMetersPerSecond, double moduleAngleRotations) {
    private SwerveModuleState toModuleState() {
      return new SwerveModuleState(
          wheelVelocityMetersPerSecond,
          Rotation2d.fromRotations(moduleAngleRotations));
    }
  }

  private final ModuleSample[] pendingSamples = new ModuleSample[MODULE_COUNT];
  private int nextExpectedModuleIndex;
  private long committedGeneration;
  private Snapshot committedSnapshot;

  /**
   * Stages one actual simulated module state and commits after a valid BR publication.
   *
   * @param identity fixed module identity
   * @param wheelVelocityMetersPerSecond actual physical wheel velocity in meters per second
   * @param calibratedModuleAngle actual calibrated module angle
   * @param healthy whether the publisher's simulated sensor/configuration state is healthy
   * @return true when the sample was accepted; false when the frame failed closed
   */
  public synchronized boolean publish(
      ModuleIdentity identity,
      double wheelVelocityMetersPerSecond,
      Rotation2d calibratedModuleAngle,
      boolean healthy) {
    if (identity == null
        || calibratedModuleAngle == null
        || !healthy
        || !Double.isFinite(wheelVelocityMetersPerSecond)
        || !Double.isFinite(calibratedModuleAngle.getRotations())
        || identity.index != nextExpectedModuleIndex) {
      invalidate();
      return false;
    }

    pendingSamples[identity.index] =
        new ModuleSample(
            wheelVelocityMetersPerSecond,
            calibratedModuleAngle.getRotations());
    nextExpectedModuleIndex++;

    if (nextExpectedModuleIndex == MODULE_COUNT) {
      if (committedGeneration == Long.MAX_VALUE) {
        invalidate();
        return false;
      }
      committedGeneration++;
      committedSnapshot =
          new Snapshot(
              committedGeneration,
              pendingSamples[ModuleIdentity.FRONT_LEFT.index],
              pendingSamples[ModuleIdentity.FRONT_RIGHT.index],
              pendingSamples[ModuleIdentity.BACK_LEFT.index],
              pendingSamples[ModuleIdentity.BACK_RIGHT.index]);
      clearPendingFrame();
    }
    return true;
  }

  /** Returns the latest fully committed coherent snapshot, if one remains valid. */
  public synchronized Optional<Snapshot> latestSnapshot() {
    return Optional.ofNullable(committedSnapshot);
  }

  private void invalidate() {
    committedSnapshot = null;
    clearPendingFrame();
  }

  private void clearPendingFrame() {
    Arrays.fill(pendingSamples, null);
    nextExpectedModuleIndex = 0;
  }
}
