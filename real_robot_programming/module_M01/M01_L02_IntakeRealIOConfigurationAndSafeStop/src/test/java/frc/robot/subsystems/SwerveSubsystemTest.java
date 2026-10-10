// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.vision.QualifiedVisionMeasurement;
import frc.robot.observation.vision.VisionFusionObservation;
import frc.robot.observation.vision.VisionMeasurementQuality;
import frc.robot.observation.vision.VisionMeasurementQuality.Acceptance;
import frc.robot.observation.vision.VisionMeasurementQuality.RejectionReason;
import frc.robot.observation.vision.VisionMeasurementQuality.UncertaintyClass;
import java.lang.reflect.Field;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class SwerveSubsystemTest {
  private static final double VISION_TIMESTAMP_SETTLE_SECONDS = 0.010;

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
  void stopAttemptsEveryModuleAfterAnyIndividualRuntimeException()
      throws ReflectiveOperationException {
    for (int failingModuleIndex = 0; failingModuleIndex < 4; failingModuleIndex++) {
      List<String> stopAttemptOrder = new ArrayList<>();
      RecordingModuleIO[] modules = createModules();
      SwerveSubsystem subsystem = createSubsystem(modules);
      configureStopTracking(modules, subsystem, stopAttemptOrder);
      RuntimeException expectedFailure = new RuntimeException("module stop failure " + failingModuleIndex);
      modules[failingModuleIndex].stopFailure = expectedFailure;
      subsystem.acceptChassisSpeeds(new ChassisSpeeds(1.0, 2.0, 3.0));

      RuntimeException actualFailure = assertThrows(RuntimeException.class, subsystem::stop);

      assertSame(expectedFailure, actualFailure);
      assertEquals(0, actualFailure.getSuppressed().length);
      assertIterableEquals(List.of("FL", "FR", "BL", "BR"), stopAttemptOrder);
      assertIntent(subsystem, 0.0, 0.0, 0.0);
      assertAllFinalStatesZero(subsystem);
      assertEachModuleStoppedOnce(modules);
      for (RecordingModuleIO module : modules) {
        assertTrue(module.sawSafeStateBeforeStop);
      }
    }
  }

  @Test
  void stopPreservesFirstFailureAndSuppressesLaterFailuresInEncounterOrder() {
    List<String> stopAttemptOrder = new ArrayList<>();
    RecordingModuleIO[] modules = createModules();
    SwerveSubsystem subsystem = createSubsystem(modules);
    configureStopTracking(modules, subsystem, stopAttemptOrder);
    RuntimeException frontLeftFailure = new RuntimeException("front left failure");
    RuntimeException backLeftFailure = new RuntimeException("back left failure");
    RuntimeException backRightFailure = new RuntimeException("back right failure");
    modules[0].stopFailure = frontLeftFailure;
    modules[2].stopFailure = backLeftFailure;
    modules[3].stopFailure = backRightFailure;

    RuntimeException actualFailure = assertThrows(RuntimeException.class, subsystem::stop);

    assertSame(frontLeftFailure, actualFailure);
    assertEquals(2, actualFailure.getSuppressed().length);
    assertSame(backLeftFailure, actualFailure.getSuppressed()[0]);
    assertSame(backRightFailure, actualFailure.getSuppressed()[1]);
    assertIterableEquals(List.of("FL", "FR", "BL", "BR"), stopAttemptOrder);
    assertEachModuleStoppedOnce(modules);
  }

  @Test
  void stopAvoidsSelfSuppressionWhenModulesThrowTheSameThrowableInstance() {
    List<String> stopAttemptOrder = new ArrayList<>();
    RecordingModuleIO[] modules = createModules();
    SwerveSubsystem subsystem = createSubsystem(modules);
    configureStopTracking(modules, subsystem, stopAttemptOrder);
    RuntimeException sharedFailure = new RuntimeException("shared module stop failure");
    RuntimeException laterDistinctFailure =
        new IllegalStateException("later module stop failure");
    modules[0].stopFailure = sharedFailure;
    modules[1].stopFailure = sharedFailure;
    modules[2].stopFailure = laterDistinctFailure;

    RuntimeException actualFailure = assertThrows(RuntimeException.class, subsystem::stop);

    assertSame(sharedFailure, actualFailure);
    assertEquals(1, actualFailure.getSuppressed().length);
    assertSame(laterDistinctFailure, actualFailure.getSuppressed()[0]);
    assertIterableEquals(List.of("FL", "FR", "BL", "BR"), stopAttemptOrder);
    assertEachModuleStoppedOnce(modules);
    for (RecordingModuleIO module : modules) {
      assertTrue(module.sawSafeStateBeforeStop);
    }
  }

  @Test
  void recordsQualifiedHandoffBeforeRejectingUnavailableEstimator() {
    SwerveSubsystem subsystem =
        createSubsystem(createModules(true), new RecordingGyroIO(true));
    double timestampSeconds = Timer.getFPGATimestamp();
    QualifiedVisionMeasurement measurement =
        new QualifiedVisionMeasurement(
            new Pose2d(1.0, 0.0, new Rotation2d()),
            timestampSeconds,
            new VisionMeasurementQuality(
                Acceptance.ACCEPTED, UncertaintyClass.LOW, RejectionReason.NONE));

    assertTrue(subsystem.getCurrentPose().isEmpty());
    assertTrue(subsystem.getEstimatedPose().isEmpty());
    assertTrue(!subsystem.admitVisionMeasurement(measurement));

    VisionFusionObservation fusionObservation = subsystem.getVisionFusionObservation();
    assertEquals(1, fusionObservation.qualifiedHandoffCount());
    assertEquals(
        timestampSeconds,
        fusionObservation.lastQualifiedHandoffTimestampSeconds(),
        1.0e-9);
    assertEquals(0, fusionObservation.acceptedFusionCount());
    assertTrue(Double.isNaN(fusionObservation.lastAcceptedFusionTimestampSeconds()));
    assertTrue(subsystem.getCurrentPose().isEmpty());
    assertTrue(subsystem.getEstimatedPose().isEmpty());
  }

  @Test
  void rejectsInvalidMeasurementBeforeRecordingQualifiedHandoff() {
    SwerveSubsystem subsystem =
        createSubsystem(createModules(true), new RecordingGyroIO(true));
    QualifiedVisionMeasurement rejectedMeasurement =
        new QualifiedVisionMeasurement(
            new Pose2d(1.0, 0.0, new Rotation2d()),
            Timer.getFPGATimestamp(),
            new VisionMeasurementQuality(
                Acceptance.REJECTED,
                UncertaintyClass.UNUSABLE,
                RejectionReason.TARGET_TOO_FAR));

    assertTrue(!subsystem.admitVisionMeasurement(null));
    assertTrue(!subsystem.admitVisionMeasurement(rejectedMeasurement));

    VisionFusionObservation fusionObservation = subsystem.getVisionFusionObservation();
    assertEquals(0, fusionObservation.qualifiedHandoffCount());
    assertTrue(Double.isNaN(fusionObservation.lastQualifiedHandoffTimestampSeconds()));
    assertEquals(0, fusionObservation.acceptedFusionCount());
    assertTrue(Double.isNaN(fusionObservation.lastAcceptedFusionTimestampSeconds()));
  }

  @Test
  void admitsOneFreshVisionMeasurementAndRejectsItsDuplicate() {
    SwerveSubsystem subsystem = createSubsystem(createModules(true), new RecordingGyroIO(true));
    subsystem.periodic();
    assertTrue(subsystem.captureFieldHeadingReference());
    subsystem.periodic();
    Timer.delay(VISION_TIMESTAMP_SETTLE_SECONDS);
    double timestampSeconds = Timer.getFPGATimestamp();
    Timer.delay(VISION_TIMESTAMP_SETTLE_SECONDS);
    subsystem.periodic();

    QualifiedVisionMeasurement measurement =
        new QualifiedVisionMeasurement(
            new Pose2d(1.0, 0.0, new Rotation2d()),
            timestampSeconds,
            new VisionMeasurementQuality(
                Acceptance.ACCEPTED, UncertaintyClass.LOW, RejectionReason.NONE));

    assertTrue(subsystem.admitVisionMeasurement(measurement));
    VisionFusionObservation fusionObservation = subsystem.getVisionFusionObservation();
    assertEquals(1, fusionObservation.qualifiedHandoffCount());
    assertEquals(
        timestampSeconds,
        fusionObservation.lastQualifiedHandoffTimestampSeconds(),
        1.0e-9);
    assertEquals(1, fusionObservation.acceptedFusionCount());
    assertEquals(
        timestampSeconds,
        fusionObservation.lastAcceptedFusionTimestampSeconds(),
        1.0e-9);
    assertTrue(!subsystem.admitVisionMeasurement(measurement));
    fusionObservation = subsystem.getVisionFusionObservation();
    assertEquals(2, fusionObservation.qualifiedHandoffCount());
    assertEquals(1, fusionObservation.acceptedFusionCount());
    assertEquals(
        timestampSeconds,
        fusionObservation.lastAcceptedFusionTimestampSeconds(),
        1.0e-9);
    assertEquals(0.0, subsystem.getCurrentPose().orElseThrow().getX(), 1.0e-9);
  }

  @ParameterizedTest
  @EnumSource(RejectedVisionTimestamp.class)
  void rejectsInvalidTimestampAtSwerveAdmissionWithoutMutatingAcceptedEstimatorState(
      RejectedVisionTimestamp rejection) {
    SwerveSubsystem subsystem = createEstimatorReadySubsystem();
    Timer.delay(VISION_TIMESTAMP_SETTLE_SECONDS);
    double acceptedTimestampSeconds = Timer.getFPGATimestamp();
    Timer.delay(VISION_TIMESTAMP_SETTLE_SECONDS);
    subsystem.periodic();

    QualifiedVisionMeasurement acceptedMeasurement =
        acceptedMeasurement(acceptedTimestampSeconds, 1.0);
    assertTrue(subsystem.admitVisionMeasurement(acceptedMeasurement));
    Pose2d acceptedPose = subsystem.getEstimatedPose().orElseThrow();
    VisionFusionObservation acceptedObservation = subsystem.getVisionFusionObservation();

    double rejectedTimestampSeconds =
        switch (rejection) {
          case STALE -> acceptedTimestampSeconds - 2.0;
          case FUTURE -> Timer.getFPGATimestamp() + 1.0;
          case OUT_OF_ORDER -> acceptedTimestampSeconds - VISION_TIMESTAMP_SETTLE_SECONDS;
        };

    assertFalse(
        subsystem.admitVisionMeasurement(
            acceptedMeasurement(rejectedTimestampSeconds, 2.0)));

    VisionFusionObservation afterRejection = subsystem.getVisionFusionObservation();
    assertEquals(acceptedObservation.acceptedFusionCount(), afterRejection.acceptedFusionCount());
    assertEquals(
        acceptedObservation.lastAcceptedFusionTimestampSeconds(),
        afterRejection.lastAcceptedFusionTimestampSeconds(),
        1.0e-9);
    assertEquals(acceptedPose, subsystem.getEstimatedPose().orElseThrow());
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
    return createSubsystem(createModules(), new RecordingGyroIO());
  }

  private static SwerveSubsystem createSubsystem(RecordingModuleIO[] modules) {
    return createSubsystem(modules, new RecordingGyroIO());
  }

  private static SwerveSubsystem createSubsystem(
      RecordingModuleIO[] modules, GyroIO gyro) {
    return new SwerveSubsystem(
        modules[0], modules[1], modules[2], modules[3], gyro);
  }

  private static SwerveSubsystem createEstimatorReadySubsystem() {
    SwerveSubsystem subsystem = createSubsystem(createModules(true), new RecordingGyroIO(true));
    subsystem.periodic();
    assertTrue(subsystem.captureFieldHeadingReference());
    subsystem.periodic();
    assertTrue(subsystem.getEstimatedPose().isPresent());
    return subsystem;
  }

  private static QualifiedVisionMeasurement acceptedMeasurement(
      double timestampSeconds, double xMeters) {
    return new QualifiedVisionMeasurement(
        new Pose2d(xMeters, 0.0, new Rotation2d()),
        timestampSeconds,
        new VisionMeasurementQuality(
            Acceptance.ACCEPTED, UncertaintyClass.LOW, RejectionReason.NONE));
  }

  private static RecordingModuleIO[] createModules() {
    return createModules(false);
  }

  private static RecordingModuleIO[] createModules(boolean valid) {
    return new RecordingModuleIO[] {
      new RecordingModuleIO(0.0, valid),
      new RecordingModuleIO(0.0, valid),
      new RecordingModuleIO(0.0, valid),
      new RecordingModuleIO(0.0, valid)
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

  private enum RejectedVisionTimestamp {
    STALE,
    FUTURE,
    OUT_OF_ORDER
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

  private static void configureStopTracking(
      RecordingModuleIO[] modules, SwerveSubsystem subsystem, List<String> stopAttemptOrder) {
    String[] moduleNames = {"FL", "FR", "BL", "BR"};
    for (int moduleIndex = 0; moduleIndex < modules.length; moduleIndex++) {
      modules[moduleIndex].configureStopTracking(
          moduleNames[moduleIndex], stopAttemptOrder, subsystem);
    }
  }

  private static boolean safeStopStateWasEstablished(SwerveSubsystem subsystem) {
    try {
      Field intentField = SwerveSubsystem.class.getDeclaredField("chassisIntent");
      intentField.setAccessible(true);
      Object intent = intentField.get(subsystem);
      for (RecordComponent component : intent.getClass().getRecordComponents()) {
        component.getAccessor().setAccessible(true);
        if ((double) component.getAccessor().invoke(intent) != 0.0) {
          return false;
        }
      }
      for (SwerveModuleState state : subsystem.getFinalModuleStates()) {
        if (state.speedMetersPerSecond != 0.0 || state.angle.getRadians() != 0.0) {
          return false;
        }
      }
      return true;
    } catch (ReflectiveOperationException failure) {
      throw new AssertionError(failure);
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final double encoderAbsolutePositionRotations;
    private final boolean valid;
    private int updateCount;
    private int driveOutputCount;
    private int driveVelocityCount;
    private int steerAngleCount;
    private int stopCount;
    private String stopName;
    private List<String> stopAttemptOrder;
    private SwerveSubsystem observedSubsystem;
    private RuntimeException stopFailure;
    private boolean sawSafeStateBeforeStop;

    private RecordingModuleIO() {
      this(0.0);
    }

    private RecordingModuleIO(double encoderAbsolutePositionRotations) {
      this(encoderAbsolutePositionRotations, false);
    }

    private RecordingModuleIO(double encoderAbsolutePositionRotations, boolean valid) {
      this.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
      this.valid = valid;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      updateCount++;
      inputs.encoderAbsolutePositionRotations = encoderAbsolutePositionRotations;
      inputs.driveConnected = valid;
      inputs.steerConnected = valid;
      inputs.encoderConnected = valid;
      inputs.driveConfigurationHealthy = valid;
      inputs.steerConfigurationHealthy = valid;
      inputs.encoderConfigurationHealthy = valid;
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
      if (stopAttemptOrder != null) {
        stopAttemptOrder.add(stopName);
        sawSafeStateBeforeStop = safeStopStateWasEstablished(observedSubsystem);
      }
      if (stopFailure != null) {
        throw stopFailure;
      }
    }

    private void configureStopTracking(
        String stopName, List<String> stopAttemptOrder, SwerveSubsystem observedSubsystem) {
      this.stopName = stopName;
      this.stopAttemptOrder = stopAttemptOrder;
      this.observedSubsystem = observedSubsystem;
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    private final boolean valid;

    private RecordingGyroIO() {
      this(false);
    }

    private RecordingGyroIO(boolean valid) {
      this.valid = valid;
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.connected = valid;
      inputs.configurationHealthy = valid;
      inputs.yawDegrees = 0.0;
    }
  }
}
