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
import static org.junit.jupiter.api.Assertions.assertNotSame;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SwerveSubsystemTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void disableRobot() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

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
  void rejectsNullChassisSpeedsAndStopsEveryModule() throws ReflectiveOperationException {
    RecordingModuleIO[] modules = createModules();
    SwerveSubsystem subsystem = createSubsystem(modules);

    subsystem.acceptChassisSpeeds(null);

    assertIntent(subsystem, 0.0, 0.0, 0.0);
    assertAllFinalStatesZero(subsystem);
    assertEachModuleStoppedOnce(modules);
  }

  @Test
  void rejectsEveryNonfiniteRobotRelativeComponentAndStopsEveryModule()
      throws ReflectiveOperationException {
    for (ChassisSpeeds invalidRequest : nonfiniteRequests()) {
      RecordingModuleIO[] modules = createModules();
      SwerveSubsystem subsystem = createSubsystem(modules);

      subsystem.acceptChassisSpeeds(invalidRequest);

      assertIntent(subsystem, 0.0, 0.0, 0.0);
      assertAllFinalStatesZero(subsystem);
      assertEachModuleStoppedOnce(modules);
    }
  }

  @Test
  void invalidRobotRelativeRequestClearsStaleIntentAndValidRequestRecovers()
      throws ReflectiveOperationException {
    RecordingModuleIO[] modules = createModules();
    SwerveSubsystem subsystem = createSubsystem(modules);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();

    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    subsystem.periodic();
    clearActuationEvents(modules);

    subsystem.acceptChassisSpeeds(new ChassisSpeeds(Double.NaN, 0.0, 0.0));

    assertIntent(subsystem, 0.0, 0.0, 0.0);
    assertAllFinalStatesZero(subsystem);
    assertEachModuleStoppedOnce(modules);

    subsystem.periodic();
    assertNoActuationRequests(modules);

    clearActuationEvents(modules);
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));
    subsystem.periodic();
    assertEachModuleReceivedOneRequest(modules);
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
  void periodicRefreshesObservationsAndDispatchesWhenEnabled() {
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

    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    subsystem.periodic();

    assertEquals(1, frontLeft.updateCount);
    assertEquals(1, frontRight.updateCount);
    assertEquals(1, backLeft.updateCount);
    assertEquals(1, backRight.updateCount);
    assertEquals(1, frontLeft.driveVelocityCount);
    assertEquals(1, frontRight.driveVelocityCount);
    assertEquals(1, backLeft.driveVelocityCount);
    assertEquals(1, backRight.driveVelocityCount);
    assertEquals(1, frontLeft.steerAngleCount);
    assertEquals(1, frontRight.steerAngleCount);
    assertEquals(1, backLeft.steerAngleCount);
    assertEquals(1, backRight.steerAngleCount);
    assertEquals(0, frontLeft.stopCount);
    assertEquals(0, frontRight.stopCount);
    assertEquals(0, backLeft.stopCount);
    assertEquals(0, backRight.stopCount);
  }

  @Test
  void periodicRefreshesObservationsWithoutActuationWhenDisabled() {
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
    assertEquals(0, frontLeft.driveVelocityCount);
    assertEquals(0, frontRight.driveVelocityCount);
    assertEquals(0, backLeft.driveVelocityCount);
    assertEquals(0, backRight.driveVelocityCount);
    assertEquals(0, frontLeft.steerAngleCount);
    assertEquals(0, frontRight.steerAngleCount);
    assertEquals(0, backLeft.steerAngleCount);
    assertEquals(0, backRight.steerAngleCount);
  }

  @Test
  void disabledTransitionDisarmsRobotRelativeIntentUntilFreshRequest()
      throws ReflectiveOperationException {
    RecordingModuleIO[] modules = createModules();
    SwerveSubsystem subsystem = createSubsystem(modules);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();

    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    subsystem.periodic();
    assertEachModuleReceivedOneRequest(modules);
    clearActuationEvents(modules);

    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();
    subsystem.periodic();

    assertIntent(subsystem, 0.0, 0.0, 0.0);
    assertAllFinalStatesZero(subsystem);
    assertEachModuleStoppedOnce(modules);

    clearActuationEvents(modules);
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    subsystem.periodic();
    assertNoActuationRequests(modules);

    subsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));
    subsystem.periodic();
    assertEachModuleReceivedOneRequest(modules);
  }

  @Test
  void pipelineOutputIsOwnedInFrontLeftFrontRightBackLeftBackRightOrder() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    RecordingModuleIO frontLeft = new RecordingModuleIO(0.0);
    RecordingModuleIO frontRight = new RecordingModuleIO(0.25);
    RecordingModuleIO backLeft = new RecordingModuleIO(0.5);
    RecordingModuleIO backRight = new RecordingModuleIO(0.75);
    SwerveSubsystem subsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            new RecordingGyroIO());
    ChassisSpeeds chassisSpeeds = new ChassisSpeeds(1.0, 0.5, 1.0);

    subsystem.acceptChassisSpeeds(chassisSpeeds);
    subsystem.periodic();

    SwerveModuleState[] expected =
        new SwerveOutputPipeline()
            .toModuleStates(
                chassisSpeeds,
                new Rotation2d[] {
                  Rotation2d.fromRotations(0.0),
                  Rotation2d.fromRotations(0.25),
                  Rotation2d.fromRotations(0.5),
                  Rotation2d.fromRotations(0.75)
                });
    SwerveModuleState[] actual = subsystem.getFinalModuleStates();

    assertEquals(4, actual.length);
    for (int moduleIndex = 0; moduleIndex < actual.length; moduleIndex++) {
      assertStateEquals(expected[moduleIndex], actual[moduleIndex]);
    }
  }

  @Test
  void finalStatesAreDeterministicAcrossRepeatedReads() {
    SwerveSubsystem subsystem = createSubsystem();
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, -0.5, 0.75));
    subsystem.periodic();

    SwerveModuleState[] firstRead = subsystem.getFinalModuleStates();
    SwerveModuleState[] secondRead = subsystem.getFinalModuleStates();

    for (int moduleIndex = 0; moduleIndex < firstRead.length; moduleIndex++) {
      assertStateEquals(firstRead[moduleIndex], secondRead[moduleIndex]);
    }
  }

  @Test
  void returnedFinalStatesCannotCorruptSubsystemOwnedStates() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.notifyNewData();
    SwerveSubsystem subsystem = createSubsystem();
    subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 0.0, 0.0));
    subsystem.periodic();

    SwerveModuleState[] firstRead = subsystem.getFinalModuleStates();
    double originalFirstSpeedMetersPerSecond = firstRead[0].speedMetersPerSecond;
    double originalFirstAngleRadians = firstRead[0].angle.getRadians();
    firstRead[0].speedMetersPerSecond = 99.0;
    firstRead[0].angle = new Rotation2d(2.0);
    firstRead[1] = new SwerveModuleState(88.0, new Rotation2d(1.0));

    SwerveModuleState[] secondRead = subsystem.getFinalModuleStates();

    assertNotSame(firstRead, secondRead);
    assertNotSame(firstRead[0], secondRead[0]);
    assertEquals(
        originalFirstSpeedMetersPerSecond,
        secondRead[0].speedMetersPerSecond);
    assertEquals(originalFirstAngleRadians, secondRead[0].angle.getRadians());
    assertEquals(1.0, secondRead[1].speedMetersPerSecond);
  }

  private static SwerveSubsystem createSubsystem() {
    return createSubsystem(createModules());
  }

  private static SwerveSubsystem createSubsystem(RecordingModuleIO[] modules) {
    return new SwerveSubsystem(
        modules[0], modules[1], modules[2], modules[3], new RecordingGyroIO());
  }

  private static RecordingModuleIO[] createModules() {
    return new RecordingModuleIO[] {
      new RecordingModuleIO(),
      new RecordingModuleIO(),
      new RecordingModuleIO(),
      new RecordingModuleIO()
    };
  }

  private static ChassisSpeeds[] nonfiniteRequests() {
    return new ChassisSpeeds[] {
      new ChassisSpeeds(Double.NaN, 0.0, 0.0),
      new ChassisSpeeds(Double.POSITIVE_INFINITY, 0.0, 0.0),
      new ChassisSpeeds(Double.NEGATIVE_INFINITY, 0.0, 0.0),
      new ChassisSpeeds(0.0, Double.NaN, 0.0),
      new ChassisSpeeds(0.0, Double.POSITIVE_INFINITY, 0.0),
      new ChassisSpeeds(0.0, Double.NEGATIVE_INFINITY, 0.0),
      new ChassisSpeeds(0.0, 0.0, Double.NaN),
      new ChassisSpeeds(0.0, 0.0, Double.POSITIVE_INFINITY),
      new ChassisSpeeds(0.0, 0.0, Double.NEGATIVE_INFINITY)
    };
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

  private static void assertStateEquals(
      SwerveModuleState expected, SwerveModuleState actual) {
    assertEquals(expected.speedMetersPerSecond, actual.speedMetersPerSecond, 1.0e-9);
    assertEquals(expected.angle.getRadians(), actual.angle.getRadians(), 1.0e-9);
  }

  private static void assertAllFinalStatesZero(SwerveSubsystem subsystem) {
    for (SwerveModuleState state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond);
      assertEquals(0.0, state.angle.getRadians());
    }
  }

  private static void assertEachModuleStoppedOnce(RecordingModuleIO[] modules) {
    for (RecordingModuleIO module : modules) {
      assertEquals(1, module.stopCount);
    }
  }

  private static void clearActuationEvents(RecordingModuleIO[] modules) {
    for (RecordingModuleIO module : modules) {
      module.driveVelocityCount = 0;
      module.steerAngleCount = 0;
      module.stopCount = 0;
    }
  }

  private static void assertNoActuationRequests(RecordingModuleIO[] modules) {
    for (RecordingModuleIO module : modules) {
      assertEquals(0, module.driveVelocityCount);
      assertEquals(0, module.steerAngleCount);
    }
  }

  private static void assertEachModuleReceivedOneRequest(RecordingModuleIO[] modules) {
    for (RecordingModuleIO module : modules) {
      assertEquals(1, module.driveVelocityCount);
      assertEquals(1, module.steerAngleCount);
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final double encoderAbsolutePositionRotations;
    private int updateCount;
    private int driveOutputCount;
    private int driveVelocityCount;
    private int steerAngleCount;
    private int stopCount;

    private RecordingModuleIO() {
      this(0.0);
    }

    private RecordingModuleIO(double encoderAbsolutePositionRotations) {
      this.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      updateCount++;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
    }

    @Override
    public void setDriveOutput(double output) {
      driveOutputCount++;
    }

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveVelocityCount++;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerAngleCount++;
    }

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
