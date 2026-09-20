// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.gyro.GyroIONoop;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.io.swerve.SwerveModuleIOSim;
import frc.robot.observation.DriveThreeMeterValidationObservation;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.validation.DriveThreeMeterValidationTelemetry;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DriveThreeMeterValidationCommandTest {
  private static final double TOLERANCE_METERS = 1.0e-9;
  private static final double SIMULATION_PERIOD_SECONDS = 0.02;
  private static final double WHEEL_CIRCUMFERENCE_METERS =
      2.0 * Math.PI * Constants.SwerveConstants.kWheelRadiusMeters;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetDriverStation() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void snapshotsAllFourModuleDistancesBeforeRequestingMotion() {
    Rig rig = new Rig();
    setModuleDistances(rig, 0.4, 0.8, 1.2, 1.6);
    rig.periodic();
    MutableClock clock = new MutableClock();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, clock);

    enableTestMode();
    command.initialize();
    rig.periodic();

    assertTrue(telemetry.latest.running());
    assertEquals(0.0, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals(0.0, telemetry.latest.frontRightDeltaMeters(), TOLERANCE_METERS);
    assertEquals(0.0, telemetry.latest.backLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals(0.0, telemetry.latest.backRightDeltaMeters(), TOLERANCE_METERS);
    assertEquals(1, rig.frontLeft.driveVelocityRequestCount);
  }

  @Test
  void accumulatesTheMedianOfFourProjectedForwardDistances() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModuleDistances(rig, 1.90, 1.95, 2.05, 2.10);
    rig.periodic();
    command.execute();

    assertEquals(2.0, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.running());
    assertFalse(telemetry.latest.complete());
  }

  @Test
  void projectsPositiveDistanceAtZeroDegreesOntoPositiveForwardProgress() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModulePositions(rig, 0.75, 0.75, 0.75, 0.75, 0.0, 0.0, 0.0, 0.0);
    rig.periodic();
    command.execute();

    assertEquals(0.75, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(0.75, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.running());
  }

  @Test
  void projectsNegativeDistanceAtOneHundredEightyDegreesOntoPositiveForwardProgress() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModulePositions(rig, -0.75, -0.75, -0.75, -0.75, 0.5, 0.5, 0.5, 0.5);
    rig.periodic();
    command.execute();

    assertEquals(0.75, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(0.75, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.running());
    assertEquals("NONE", telemetry.latest.faultOrAbortReason());
  }

  @Test
  void accumulatesProjectedForwardIncrementsAcrossChangingModuleOrientations() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModulePositions(rig, 0.50, 0.50, 0.50, 0.50, 0.0, 0.0, 0.0, 0.0);
    rig.periodic();
    command.execute();
    assertEquals(0.50, telemetry.latest.measuredMeters(), TOLERANCE_METERS);

    setModulePositions(rig, 0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.5, 0.5);
    rig.periodic();
    command.execute();

    assertEquals(1.0, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(1.0, telemetry.latest.frontRightDeltaMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.running());
  }

  @Test
  void mixedOptimizedModuleOrientationsCompleteNormally() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModulePositions(rig, 3.0, -3.0, 3.0, -3.0, 0.0, 0.5, 0.0, 0.5);
    rig.periodic();
    command.execute();

    assertEquals(3.0, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(3.0, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals(3.0, telemetry.latest.frontRightDeltaMeters(), TOLERANCE_METERS);
    assertEquals(3.0, telemetry.latest.backLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals(3.0, telemetry.latest.backRightDeltaMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.complete());
    assertEquals("COMPLETE", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void completesAtExactlyThreeMetersAndStopsEveryModule() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModuleDistances(rig, 3.0, 3.0, 3.0, 3.0);
    rig.periodic();
    command.execute();

    assertEquals(3.0, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertFalse(telemetry.latest.running());
    assertTrue(telemetry.latest.complete());
    assertEquals("COMPLETE", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void preservesOvershootInsteadOfClampingToTarget() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModuleDistances(rig, 3.08, 3.08, 3.08, 3.08);
    rig.periodic();
    command.execute();

    assertEquals(3.08, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(3.08, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
  }

  @Test
  void abortsWhenOneModuleDisagreesBeyondNamedTolerance() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModuleDistances(rig, 1.0, 1.0, 1.0, 1.3);
    rig.periodic();
    command.execute();

    assertFalse(telemetry.latest.running());
    assertEquals("MODULE_DISAGREEMENT", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void acceptsUnequalModuleDeltasWithinTolerance() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModuleDistances(rig, 0.90, 1.00, 1.10, 1.05);
    rig.periodic();
    command.execute();

    assertEquals(1.025, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.running());
  }

  @Test
  void abortsForNegativeAndNonfiniteMeasurements() {
    Rig negativeRig = readyRig();
    RecordingTelemetry negativeTelemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand negativeCommand =
        createCommand(negativeRig, negativeTelemetry, new MutableClock());
    negativeCommand.initialize();
    setModuleDistances(negativeRig, -0.1, -0.1, -0.1, -0.1);
    negativeRig.periodic();
    negativeCommand.execute();
    assertEquals("NEGATIVE_DELTA", negativeTelemetry.latest.faultOrAbortReason());

    Rig nonfiniteRig = readyRig();
    RecordingTelemetry nonfiniteTelemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand nonfiniteCommand =
        createCommand(nonfiniteRig, nonfiniteTelemetry, new MutableClock());
    nonfiniteCommand.initialize();
    nonfiniteRig.frontLeft.drivePositionRotations = Double.NaN;
    nonfiniteRig.periodic();
    nonfiniteCommand.execute();
    assertEquals("INVALID_MEASUREMENT", nonfiniteTelemetry.latest.faultOrAbortReason());
  }

  @Test
  void abortsForActualNegativeProjectedForwardTravel() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    setModulePositions(rig, 0.10, 0.10, 0.10, 0.10, 0.5, 0.5, 0.5, 0.5);
    rig.periodic();
    command.execute();

    assertEquals(0.0, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(-0.10, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals("NEGATIVE_DELTA", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void abortsAtDeterministicTimeout() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    MutableClock clock = new MutableClock();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, clock);
    command.initialize();

    clock.seconds = Constants.SwerveConstants.kDriveThreeMeterValidationTimeoutSeconds;
    command.execute();

    assertEquals("TIMEOUT", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void abortsWhenTestModeIsLeftDuringExecution() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    command.execute();

    assertEquals("TEST_MODE_EXIT", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void abortsWhenARequiredModuleIsUnhealthy() {
    Rig rig = readyRig();
    rig.frontLeft.driveConnected = false;
    rig.periodic();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());

    command.initialize();

    assertEquals("MODULE_UNHEALTHY", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void interruptionAlwaysStopsTheDrivetrain() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();

    command.end(true);

    assertEquals("INTERRUPTED", telemetry.latest.faultOrAbortReason());
    assertStopped(rig);
  }

  @Test
  void requiresEnabledTestMode() {
    Rig rig = new Rig();
    rig.periodic();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());

    command.initialize();

    assertEquals("TEST_MODE_REQUIRED", telemetry.latest.faultOrAbortReason());
    assertFalse(telemetry.latest.running());
    assertEquals(0, rig.frontLeft.driveVelocityRequestCount);
    assertStopped(rig);
  }

  @Test
  void usesRobotRelativeMotionWithoutAValidFieldHeading() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());

    command.initialize();

    assertTrue(telemetry.latest.running());
    rig.periodic();
    assertEquals(1, rig.frontLeft.driveVelocityRequestCount);
  }

  @Test
  void productionModuleSimulationCompletesThreeMeterValidationWithoutGyroSimulation() {
    MutableClock clock = new MutableClock();
    SimulationRig rig = new SimulationRig(clock);
    rig.periodic();
    enableTestMode();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command =
        new DriveThreeMeterValidationCommand(rig.subsystem, telemetry, clock);

    command.initialize();
    int cycleCount = 0;
    int maximumCycleCount =
        (int) Math.ceil(
            Constants.SwerveConstants.kDriveThreeMeterValidationTimeoutSeconds
                / SIMULATION_PERIOD_SECONDS);
    while (!command.isFinished() && cycleCount < maximumCycleCount) {
      clock.seconds += SIMULATION_PERIOD_SECONDS;
      rig.periodic();
      command.execute();
      cycleCount++;
    }

    assertTrue(command.isFinished());
    command.end(false);
    assertTrue(telemetry.latest.complete());
    assertEquals("COMPLETE", telemetry.latest.faultOrAbortReason());
    assertTrue(telemetry.latest.measuredMeters()
        >= Constants.SwerveConstants.kDriveThreeMeterValidationTargetMeters);
    assertTrue(telemetry.latest.measuredMeters()
        <= Constants.SwerveConstants.kDriveThreeMeterValidationTargetMeters
            + Constants.SwerveConstants.kFourModuleTestTranslationSpeedMetersPerSecond
                * SIMULATION_PERIOD_SECONDS
            + TOLERANCE_METERS);
    assertTrue(clock.seconds
        < Constants.SwerveConstants.kDriveThreeMeterValidationTimeoutSeconds);

    SwerveModulePosition[] completedPositions = rig.subsystem.getMeasuredModulePositions();
    assertEquals(4, completedPositions.length);
    for (SwerveModulePosition position : completedPositions) {
      assertEquals(telemetry.latest.measuredMeters(), position.distanceMeters, TOLERANCE_METERS);
    }

    clock.seconds += SIMULATION_PERIOD_SECONDS;
    rig.periodic();
    SwerveModulePosition[] stoppedPositions = rig.subsystem.getMeasuredModulePositions();
    for (int moduleIndex = 0; moduleIndex < stoppedPositions.length; moduleIndex++) {
      assertEquals(
          completedPositions[moduleIndex].distanceMeters,
          stoppedPositions[moduleIndex].distanceMeters,
          TOLERANCE_METERS);
    }
  }

  @Test
  void dashboardRegistersTheRequiredGlassButton() {
    Rig rig = readyRig();

    new DriveThreeMeterValidationDashboard(rig.subsystem, new RecordingTelemetry());

    assertInstanceOf(Command.class, SmartDashboard.getData("Drive 3m Validation"));
  }

  @Test
  void publishesDiagnosticTelemetryForEveryState() {
    Rig rig = readyRig();
    RecordingTelemetry telemetry = new RecordingTelemetry();
    DriveThreeMeterValidationCommand command = createCommand(rig, telemetry, new MutableClock());
    command.initialize();
    setModuleDistances(rig, 0.5, 0.5, 0.5, 0.5);
    rig.periodic();
    command.execute();

    assertEquals(3.0, telemetry.latest.targetMeters(), TOLERANCE_METERS);
    assertEquals(0.5, telemetry.latest.measuredMeters(), TOLERANCE_METERS);
    assertEquals(0.5, telemetry.latest.frontLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals(0.5, telemetry.latest.frontRightDeltaMeters(), TOLERANCE_METERS);
    assertEquals(0.5, telemetry.latest.backLeftDeltaMeters(), TOLERANCE_METERS);
    assertEquals(0.5, telemetry.latest.backRightDeltaMeters(), TOLERANCE_METERS);
    assertTrue(telemetry.latest.running());
    assertFalse(telemetry.latest.complete());
  }

  private static DriveThreeMeterValidationCommand createCommand(
      Rig rig, RecordingTelemetry telemetry, MutableClock clock) {
    return new DriveThreeMeterValidationCommand(rig.subsystem, telemetry, clock);
  }

  private static Rig readyRig() {
    Rig rig = new Rig();
    rig.periodic();
    enableTestMode();
    return rig;
  }

  private static void enableTestMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
    assertTrue(DriverStation.isTestEnabled());
  }

  private static void setModuleDistances(
      Rig rig, double frontLeft, double frontRight, double backLeft, double backRight) {
    rig.frontLeft.drivePositionRotations =
        toRawDriveRotations(
            frontLeft, Constants.SwerveConstants.kFrontLeftDrivePositionSign);
    rig.frontRight.drivePositionRotations =
        toRawDriveRotations(
            frontRight, Constants.SwerveConstants.kFrontRightDrivePositionSign);
    rig.backLeft.drivePositionRotations =
        toRawDriveRotations(
            backLeft, Constants.SwerveConstants.kBackLeftDrivePositionSign);
    rig.backRight.drivePositionRotations =
        toRawDriveRotations(
            backRight, Constants.SwerveConstants.kBackRightDrivePositionSign);
  }

  private static void setModulePositions(
      Rig rig,
      double frontLeftDistanceMeters,
      double frontRightDistanceMeters,
      double backLeftDistanceMeters,
      double backRightDistanceMeters,
      double frontLeftAngleRotations,
      double frontRightAngleRotations,
      double backLeftAngleRotations,
      double backRightAngleRotations) {
    setModuleDistances(
        rig,
        frontLeftDistanceMeters,
        frontRightDistanceMeters,
        backLeftDistanceMeters,
        backRightDistanceMeters);
    rig.frontLeft.encoderAbsolutePositionRotations = frontLeftAngleRotations;
    rig.frontRight.encoderAbsolutePositionRotations = frontRightAngleRotations;
    rig.backLeft.encoderAbsolutePositionRotations = backLeftAngleRotations;
    rig.backRight.encoderAbsolutePositionRotations = backRightAngleRotations;
  }

  private static double toRawDriveRotations(
      double physicalDistanceMeters,
      double physicalForwardSign) {
    return physicalDistanceMeters / physicalForwardSign / WHEEL_CIRCUMFERENCE_METERS
        * Constants.SwerveConstants.kDriveGearRatio;
  }

  private static void assertStopped(Rig rig) {
    assertEquals(1, rig.frontLeft.stopCount);
    assertEquals(1, rig.frontRight.stopCount);
    assertEquals(1, rig.backLeft.stopCount);
    assertEquals(1, rig.backRight.stopCount);
  }

  private static final class MutableClock implements DoubleSupplier {
    private double seconds;

    @Override
    public double getAsDouble() {
      return seconds;
    }
  }

  private static final class RecordingTelemetry implements DriveThreeMeterValidationTelemetry {
    private DriveThreeMeterValidationObservation latest =
        DriveThreeMeterValidationObservation.idle(
            Constants.SwerveConstants.kDriveThreeMeterValidationTargetMeters);

    @Override
    public void publish(DriveThreeMeterValidationObservation observation) {
      latest = observation;
    }
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new InvalidGyroIO());

    private void periodic() {
      subsystem.periodic();
    }
  }

  private static final class SimulationRig {
    private final SwerveSubsystem subsystem;

    private SimulationRig(DoubleSupplier clock) {
      subsystem =
          new SwerveSubsystem(
              new SwerveModuleIOSim(
                  Constants.SwerveConstants.kFrontLeftDrivePositionSign, clock),
              new SwerveModuleIOSim(
                  Constants.SwerveConstants.kFrontRightDrivePositionSign, clock),
              new SwerveModuleIOSim(
                  Constants.SwerveConstants.kBackLeftDrivePositionSign, clock),
              new SwerveModuleIOSim(
                  Constants.SwerveConstants.kBackRightDrivePositionSign, clock),
              new GyroIONoop());
    }

    private void periodic() {
      subsystem.periodic();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private double drivePositionRotations;
    private double encoderAbsolutePositionRotations;
    private boolean driveConnected = true;
    private int driveVelocityRequestCount;
    private int stopCount;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.drivePositionRotations = drivePositionRotations;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
      inputs.driveConnected = driveConnected;
      inputs.steerConnected = true;
      inputs.encoderConnected = true;
      inputs.driveConfigurationHealthy = true;
      inputs.steerConfigurationHealthy = true;
      inputs.encoderConfigurationHealthy = true;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveVelocityRequestCount++;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {
      stopCount++;
    }
  }

  private static final class InvalidGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.connected = false;
      inputs.configurationHealthy = false;
    }
  }
}
