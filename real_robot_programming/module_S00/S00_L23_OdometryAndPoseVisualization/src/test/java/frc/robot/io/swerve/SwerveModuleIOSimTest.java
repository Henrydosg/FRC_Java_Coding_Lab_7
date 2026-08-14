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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.Test;

class SwerveModuleIOSimTest {
  private static final double kTolerance = 1.0e-9;
  private static final double kWheelCircumferenceMeters =
      2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters;

  @Test
  void initialStateIsZeroAndHealthy() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    assertEquals(0.0, inputs.driveAppliedOutput, kTolerance);
    assertEquals(0.0, inputs.drivePositionRotations, kTolerance);
    assertEquals(0.0, inputs.driveVelocityRotationsPerSecond, kTolerance);
    assertEquals(0.0, inputs.steerAppliedOutput, kTolerance);
    assertEquals(0.0, inputs.steerPositionRotations, kTolerance);
    assertEquals(0.0, inputs.steerVelocityRotationsPerSecond, kTolerance);
    assertEquals(0.0, inputs.encoderAbsolutePositionRotations, kTolerance);
    assertEquals(0.0, inputs.encoderVelocityRotationsPerSecond, kTolerance);
    assertHealthy(inputs);
  }

  @Test
  void integratesForwardVelocityInRawRotorUnitsUsingConfiguredRatio() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.30);

    clock.seconds = 2.0;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    double expectedRotorVelocity = toRawRotorVelocity(0.30, 1.0);
    assertEquals(6.75, Constants.SwerveConstants.kDriveGearRatio, kTolerance);
    assertEquals(expectedRotorVelocity, inputs.driveVelocityRotationsPerSecond, kTolerance);
    assertEquals(expectedRotorVelocity * 2.0, inputs.drivePositionRotations, kTolerance);
    assertEquals(
        0.30 * 2.0,
        toNormalizedDistanceMeters(inputs.drivePositionRotations, 1.0),
        kTolerance);
  }

  @Test
  void integratesReverseVelocityWithoutResettingPosition() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.40);
    clock.seconds = 1.0;
    update(simulation);

    simulation.setDriveVelocityMetersPerSecond(-0.20);
    clock.seconds = 2.0;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    assertEquals(toRawRotorVelocity(-0.20, 1.0),
        inputs.driveVelocityRotationsPerSecond, kTolerance);
    assertEquals(
        toRawRotorVelocity(0.40, 1.0) + toRawRotorVelocity(-0.20, 1.0),
        inputs.drivePositionRotations,
        kTolerance);
  }

  @Test
  void physicalForwardUsesVerifiedRawSignForEveryModule() {
    double[] expectedSigns = {1.0, 1.0, 1.0, 1.0};
    double[] configuredSigns = {
      Constants.SwerveConstants.kFrontLeftDrivePositionSign,
      Constants.SwerveConstants.kFrontRightDrivePositionSign,
      Constants.SwerveConstants.kBackLeftDrivePositionSign,
      Constants.SwerveConstants.kBackRightDrivePositionSign
    };

    for (int moduleIndex = 0; moduleIndex < configuredSigns.length; moduleIndex++) {
      double sign = configuredSigns[moduleIndex];
      assertEquals(expectedSigns[moduleIndex], sign, kTolerance);
      MutableClock clock = new MutableClock();
      SwerveModuleIOSim simulation = new SwerveModuleIOSim(sign, clock);
      update(simulation);
      simulation.setDriveVelocityMetersPerSecond(0.30);
      clock.seconds = 1.0;
      SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

      assertEquals(toRawRotorVelocity(0.30, sign),
          inputs.drivePositionRotations, kTolerance);
      assertEquals(0.30,
          toNormalizedDistanceMeters(inputs.drivePositionRotations, sign), kTolerance);
    }
  }

  @Test
  void openLoopDriveMapsToConfiguredMaximumWheelSpeedAndClamps() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveOutput(2.0);

    clock.seconds = 0.25;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    double expectedVelocity = Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond;
    assertEquals(1.0, inputs.driveAppliedOutput, kTolerance);
    assertEquals(toRawRotorVelocity(expectedVelocity, 1.0),
        inputs.driveVelocityRotationsPerSecond, kTolerance);
    assertEquals(expectedVelocity * 0.25,
        toNormalizedDistanceMeters(inputs.drivePositionRotations, 1.0), kTolerance);
  }

  @Test
  void openLoopSteerAdvancesModuleAndRotorMeasurements() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setSteerOutput(0.50);

    clock.seconds = 0.40;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    assertEquals(0.50, inputs.steerAppliedOutput, kTolerance);
    assertEquals(0.20, inputs.encoderAbsolutePositionRotations, kTolerance);
    assertEquals(0.50, inputs.encoderVelocityRotationsPerSecond, kTolerance);
    assertEquals(0.20 * Constants.SwerveConstants.kSteerGearRatio,
        inputs.steerPositionRotations, kTolerance);
    assertEquals(0.50 * Constants.SwerveConstants.kSteerGearRatio,
        inputs.steerVelocityRotationsPerSecond, kTolerance);
  }

  @Test
  void closedLoopSteerUsesFiniteRateShortestPathAcrossWrap() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setSteerOutput(-0.05);
    clock.seconds = 1.0;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);
    assertEquals(0.95, inputs.encoderAbsolutePositionRotations, kTolerance);

    simulation.setSteerAngle(Rotation2d.fromRotations(0.05));
    clock.seconds = 1.05;
    inputs = update(simulation);
    assertEquals(0.0, inputs.encoderAbsolutePositionRotations, kTolerance);
    assertEquals(1.0, inputs.encoderVelocityRotationsPerSecond, kTolerance);

    clock.seconds = 1.10;
    inputs = update(simulation);
    assertEquals(0.05, inputs.encoderAbsolutePositionRotations, kTolerance);
    assertEquals(1.0, inputs.encoderVelocityRotationsPerSecond, kTolerance);
  }

  @Test
  void stopPreservesAccumulatedPositionsAndZerosMotion() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.30);
    simulation.setSteerOutput(0.25);
    clock.seconds = 1.0;
    SwerveModuleIO.SwerveModuleIOInputs moving = update(simulation);

    simulation.stop();
    clock.seconds = 2.0;
    SwerveModuleIO.SwerveModuleIOInputs stopped = update(simulation);

    assertEquals(moving.drivePositionRotations, stopped.drivePositionRotations, kTolerance);
    assertEquals(moving.steerPositionRotations, stopped.steerPositionRotations, kTolerance);
    assertEquals(0.0, stopped.driveVelocityRotationsPerSecond, kTolerance);
    assertEquals(0.0, stopped.steerVelocityRotationsPerSecond, kTolerance);
    assertEquals(0.0, stopped.driveAppliedOutput, kTolerance);
    assertEquals(0.0, stopped.steerAppliedOutput, kTolerance);
  }

  @Test
  void nonfiniteRequestFailsClosedWithoutCorruptingPosition() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.30);
    clock.seconds = 1.0;
    SwerveModuleIO.SwerveModuleIOInputs moving = update(simulation);

    simulation.setDriveVelocityMetersPerSecond(Double.NaN);
    clock.seconds = 2.0;
    SwerveModuleIO.SwerveModuleIOInputs stopped = update(simulation);

    assertEquals(moving.drivePositionRotations, stopped.drivePositionRotations, kTolerance);
    assertEquals(0.0, stopped.driveVelocityRotationsPerSecond, kTolerance);
    assertEquals(0.0, stopped.steerVelocityRotationsPerSecond, kTolerance);
    assertHealthy(stopped);
  }

  @Test
  void backwardAndNonfiniteClockSamplesFailClosed() {
    MutableClock clock = new MutableClock();
    clock.seconds = 1.0;
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.30);

    clock.seconds = 0.5;
    SwerveModuleIO.SwerveModuleIOInputs backward = update(simulation);
    assertUnhealthy(backward);
    assertEquals(0.0, backward.drivePositionRotations, kTolerance);
    assertEquals(0.0, backward.driveVelocityRotationsPerSecond, kTolerance);

    clock.seconds = Double.NaN;
    SwerveModuleIO.SwerveModuleIOInputs nonfinite = update(simulation);
    assertUnhealthy(nonfinite);
    assertEquals(0.0, nonfinite.driveVelocityRotationsPerSecond, kTolerance);

    clock.seconds = 0.6;
    SwerveModuleIO.SwerveModuleIOInputs recovered = update(simulation);
    assertHealthy(recovered);
    assertEquals(0.0, recovered.driveVelocityRotationsPerSecond, kTolerance);
  }

  @Test
  void overflowingElapsedTimeFailsClosedWithoutCorruptingState() {
    MutableClock clock = new MutableClock();
    clock.seconds = -Double.MAX_VALUE;
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.30);

    clock.seconds = Double.MAX_VALUE;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    assertUnhealthy(inputs);
    assertEquals(0.0, inputs.drivePositionRotations, kTolerance);
    assertEquals(0.0, inputs.driveVelocityRotationsPerSecond, kTolerance);
  }

  @Test
  void staticFrictionCharacterizationRemainsUnsupportedAndStopsMotion() {
    MutableClock clock = new MutableClock();
    SwerveModuleIOSim simulation = new SwerveModuleIOSim(1.0, clock);
    update(simulation);
    simulation.setDriveVelocityMetersPerSecond(0.30);

    assertFalse(simulation.setDriveStaticFrictionCharacterizationVoltageVolts(0.50));
    clock.seconds = 1.0;
    SwerveModuleIO.SwerveModuleIOInputs inputs = update(simulation);

    assertEquals(0.0, inputs.drivePositionRotations, kTolerance);
    assertEquals(0.0, inputs.driveVelocityRotationsPerSecond, kTolerance);
  }

  @Test
  void constructorRejectsAnySignOtherThanPositiveOrNegativeOne() {
    MutableClock clock = new MutableClock();

    assertThrows(IllegalArgumentException.class, () -> new SwerveModuleIOSim(0.0, clock));
    assertThrows(IllegalArgumentException.class, () -> new SwerveModuleIOSim(2.0, clock));
    assertThrows(IllegalArgumentException.class, () -> new SwerveModuleIOSim(Double.NaN, clock));
  }

  @Test
  void sharedStateReceivesActualVelocityAndFiniteRateSteerPosition() {
    MutableClock clock = new MutableClock();
    SwerveSimulationState sharedState = new SwerveSimulationState();
    SwerveModuleIOSim[] simulations = {
      new SwerveModuleIOSim(1.0, clock, sharedState, ModuleIdentity.FRONT_LEFT),
      new SwerveModuleIOSim(1.0, clock, sharedState, ModuleIdentity.FRONT_RIGHT),
      new SwerveModuleIOSim(1.0, clock, sharedState, ModuleIdentity.BACK_LEFT),
      new SwerveModuleIOSim(-1.0, clock, sharedState, ModuleIdentity.BACK_RIGHT)
    };
    for (SwerveModuleIOSim simulation : simulations) {
      update(simulation);
      simulation.setDriveVelocityMetersPerSecond(1.0);
      simulation.setSteerAngle(Rotation2d.fromRotations(0.25));
    }

    clock.seconds = 0.10;
    for (SwerveModuleIOSim simulation : simulations) {
      update(simulation);
    }

    edu.wpi.first.math.kinematics.SwerveModuleState[] publishedStates =
        sharedState.latestSnapshot().orElseThrow().moduleStates();
    for (edu.wpi.first.math.kinematics.SwerveModuleState state : publishedStates) {
      assertEquals(1.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.10, state.angle.getRotations(), kTolerance);
    }
  }

  private static SwerveModuleIO.SwerveModuleIOInputs update(SwerveModuleIOSim simulation) {
    SwerveModuleIO.SwerveModuleIOInputs inputs =
        new SwerveModuleIO.SwerveModuleIOInputs();
    simulation.updateInputs(inputs);
    return inputs;
  }

  private static double toRawRotorVelocity(
      double velocityMetersPerSecond,
      double physicalForwardRawSign) {
    return physicalForwardRawSign
        * velocityMetersPerSecond
        / kWheelCircumferenceMeters
        * Constants.SwerveConstants.kDriveGearRatio;
  }

  private static double toNormalizedDistanceMeters(
      double rawRotorPositionRotations,
      double physicalForwardRawSign) {
    return physicalForwardRawSign
        * rawRotorPositionRotations
        / Constants.SwerveConstants.kDriveGearRatio
        * kWheelCircumferenceMeters;
  }

  private static void assertHealthy(SwerveModuleIO.SwerveModuleIOInputs inputs) {
    assertTrue(inputs.driveConnected);
    assertTrue(inputs.steerConnected);
    assertTrue(inputs.encoderConnected);
    assertTrue(inputs.driveConfigurationHealthy);
    assertTrue(inputs.steerConfigurationHealthy);
    assertTrue(inputs.encoderConfigurationHealthy);
  }

  private static void assertUnhealthy(SwerveModuleIO.SwerveModuleIOInputs inputs) {
    assertFalse(inputs.driveConnected);
    assertFalse(inputs.steerConnected);
    assertFalse(inputs.encoderConnected);
    assertFalse(inputs.driveConfigurationHealthy);
    assertFalse(inputs.steerConfigurationHealthy);
    assertFalse(inputs.encoderConfigurationHealthy);
  }

  private static final class MutableClock implements DoubleSupplier {
    private double seconds;

    @Override
    public double getAsDouble() {
      return seconds;
    }
  }
}
