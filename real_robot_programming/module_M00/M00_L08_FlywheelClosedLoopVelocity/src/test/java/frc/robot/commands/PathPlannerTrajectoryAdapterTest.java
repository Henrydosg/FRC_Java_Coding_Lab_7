// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.path.EventMarker;
import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.trajectory.PathPlannerTrajectory;
import com.pathplanner.lib.trajectory.PathPlannerTrajectoryState;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.Filesystem;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.json.simple.parser.ParseException;

class PathPlannerTrajectoryAdapterTest {
  private static final double kTolerance = 1.0e-9;
  private static final String kPathFileName = "A01_L06_OneMeter_Forward.path";
  private static final String kEventPathFileName = "A01_L09_OneMeter_With_Learning_Event.path";

  private String previousUserDirectory;
  private Path temporaryLaunchDirectory;
  private Path temporaryAsset;
  private Path temporaryEventAsset;

  @BeforeEach
  void installTemporaryDeployment(@TempDir Path temporaryDirectory) throws IOException {
    previousUserDirectory = System.getProperty("user.dir");
    temporaryLaunchDirectory = temporaryDirectory;
    temporaryAsset =
        temporaryLaunchDirectory
            .resolve("src/main/deploy/pathplanner/paths")
            .resolve(kPathFileName);
    Files.createDirectories(temporaryAsset.getParent());
    Files.copy(sourceAsset(), temporaryAsset, StandardCopyOption.REPLACE_EXISTING);
    temporaryEventAsset =
        temporaryLaunchDirectory
            .resolve("src/main/deploy/pathplanner/paths")
            .resolve(kEventPathFileName);
    Files.copy(sourceEventAsset(), temporaryEventAsset, StandardCopyOption.REPLACE_EXISTING);
    System.setProperty("user.dir", temporaryLaunchDirectory.toString());
    PathPlannerPath.clearCache();
  }

  @AfterEach
  void restoreDeployment() {
    PathPlannerPath.clearCache();
    System.setProperty("user.dir", previousUserDirectory);
  }

  @Test
  void validAssetLoadsAndProducesCanonicalFiniteTrajectory() {
    Trajectory trajectory = createAdapter().createCanonicalTrajectory();

    assertNotNull(trajectory);
    assertTrue(trajectory.getTotalTimeSeconds() > 0.0);
    assertTrue(Double.isFinite(trajectory.getTotalTimeSeconds()));
    assertPoseEquals(
        Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose,
        trajectory.getInitialPose());
    assertPoseEquals(
        Constants.PathPlannerLearningConstants.kCanonicalPathEndingPose,
        trajectory.sample(trajectory.getTotalTimeSeconds()).poseMeters);

    double previousTimeSeconds = Double.NEGATIVE_INFINITY;
    for (Trajectory.State state : trajectory.getStates()) {
      assertNotNull(state);
      assertTrue(Double.isFinite(state.timeSeconds));
      assertTrue(state.timeSeconds > previousTimeSeconds);
      assertTrue(Double.isFinite(state.velocityMetersPerSecond));
      assertTrue(Double.isFinite(state.accelerationMetersPerSecondSq));
      assertTrue(Double.isFinite(state.curvatureRadPerMeter));
      assertTrue(Double.isFinite(state.poseMeters.getX()));
      assertTrue(Double.isFinite(state.poseMeters.getY()));
      assertTrue(Double.isFinite(state.poseMeters.getRotation().getRadians()));
      previousTimeSeconds = state.timeSeconds;
    }
  }

  @Test
  void PathPlannerHeadingBecomesNativePathTangentAndHolonomicHeadingRemainsSeparate()
      throws IOException, ParseException {
    PathPlannerTrajectory sourceTrajectory = loadSourceTrajectory();
    Trajectory nativeTrajectory = createAdapter().createCanonicalTrajectory();

    assertEquals(sourceTrajectory.getStates().size(), nativeTrajectory.getStates().size());
    for (int index = 0; index < sourceTrajectory.getStates().size(); index++) {
      PathPlannerTrajectoryState sourceState = sourceTrajectory.getStates().get(index);
      Trajectory.State nativeState = nativeTrajectory.getStates().get(index);
      assertEquals(
          0.0,
          MathUtil.angleModulus(
              sourceState.heading.getRadians() - nativeState.poseMeters.getRotation().getRadians()),
          kTolerance);
    }

    PathPlannerPath path = PathPlannerPath.fromPathFile(Constants.PathPlannerLearningConstants.kPathAssetName);
    assertTrue(path.getRotationTargets().isEmpty());
    assertEquals(
        0.0,
        Constants.HolonomicTrajectoryFollowingConstants.kCanonicalHolonomicHeading.getRadians(),
        kTolerance);
    assertEquals(
        Math.PI,
        Math.abs(
            MathUtil.angleModulus(
                frc.robot.util.FieldAllianceTransform.fromCanonicalBlueHeading(
                        Constants.HolonomicTrajectoryFollowingConstants.kCanonicalHolonomicHeading,
                        Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
                        edu.wpi.first.wpilibj.DriverStation.Alliance.Red)
                    .getRadians())),
        kTolerance);
  }

  @Test
  void accelerationAndCurvatureAreFiniteAndDeterministic() {
    Trajectory first = createAdapter().createCanonicalTrajectory();
    Trajectory second = createAdapter().createCanonicalTrajectory();

    assertEquals(first.getStates().size(), second.getStates().size());
    assertEquals(first.getTotalTimeSeconds(), second.getTotalTimeSeconds(), kTolerance);
    for (int index = 0; index < first.getStates().size(); index++) {
      Trajectory.State firstState = first.getStates().get(index);
      Trajectory.State secondState = second.getStates().get(index);
      assertTrue(Double.isFinite(firstState.accelerationMetersPerSecondSq));
      assertTrue(Double.isFinite(firstState.curvatureRadPerMeter));
      assertEquals(
          firstState.accelerationMetersPerSecondSq,
          secondState.accelerationMetersPerSecondSq,
          kTolerance);
      assertEquals(firstState.curvatureRadPerMeter, secondState.curvatureRadPerMeter, kTolerance);
    }
  }

  @Test
  void robotConfigUsesVerifiedGeometryAndOneDriveReduction() throws ReflectiveOperationException {
    RobotConfig config = createRobotConfig();

    assertEquals(Constants.PathPlannerLearningConstants.kProvisionalRobotMassKg, config.massKG);
    assertEquals(
        Constants.PathPlannerLearningConstants.kProvisionalRobotMoiKgMetersSquared, config.MOI);
    assertEquals(4, config.numModules);
    assertTrue(config.isHolonomic);
    assertEquals(0.27305, config.moduleLocations[0].getX(), kTolerance);
    assertEquals(0.27305, config.moduleLocations[0].getY(), kTolerance);
    assertEquals(0.27305, config.moduleLocations[1].getX(), kTolerance);
    assertEquals(-0.27305, config.moduleLocations[1].getY(), kTolerance);
    assertEquals(-0.27305, config.moduleLocations[2].getX(), kTolerance);
    assertEquals(0.27305, config.moduleLocations[2].getY(), kTolerance);
    assertEquals(-0.27305, config.moduleLocations[3].getX(), kTolerance);
    assertEquals(-0.27305, config.moduleLocations[3].getY(), kTolerance);

    assertEquals(Constants.SwerveConstants.kWheelRadiusMeters, config.moduleConfig.wheelRadiusMeters);
    assertEquals(
        Constants.PathPlannerLearningConstants.kProvisionalMaxDriveVelocityMetersPerSecond,
        config.moduleConfig.maxDriveVelocityMPS);
    assertEquals(Constants.PathPlannerLearningConstants.kProvisionalWheelCof, config.moduleConfig.wheelCOF);
    assertEquals(Constants.SwerveConstants.kDriveSupplyCurrentLimitAmps, config.moduleConfig.driveCurrentLimit);

    DCMotor onceReduced = DCMotor.getKrakenX60(1).withReduction(Constants.SwerveConstants.kDriveGearRatio);
    DCMotor twiceReduced = onceReduced.withReduction(Constants.SwerveConstants.kDriveGearRatio);
    assertMotorEquals(onceReduced, config.moduleConfig.driveMotor);
    assertNotEquals(twiceReduced.freeSpeedRadPerSec, config.moduleConfig.driveMotor.freeSpeedRadPerSec);
  }

  @Test
  void adapterPreventsPathPlannerFlipMirrorUse() throws IOException, ParseException {
    Trajectory canonical = createAdapter().createCanonicalTrajectory();
    PathPlannerPath path = PathPlannerPath.fromPathFile(Constants.PathPlannerLearningConstants.kPathAssetName);

    assertTrue(path.preventFlipping);
    assertPoseEquals(Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose, canonical.getInitialPose());
    assertPoseEquals(
        Constants.PathPlannerLearningConstants.kCanonicalPathEndingPose,
        canonical.sample(canonical.getTotalTimeSeconds()).poseMeters);
  }

  @Test
  void eventAssetLoadsWithExactlyOneStableMarkerAtHalfway() {
    PathPlannerPath path = createAdapter().createCanonicalEventPath();

    assertTrue(path.preventFlipping);
    assertEquals(1, path.getEventMarkers().size());
    EventMarker marker = path.getEventMarkers().get(0);
    assertEquals("LEARNING_EVENT", marker.triggerName());
    assertEquals(0.5, marker.position(), kTolerance);
  }

  @Test
  void missingAssetFailsClosed() throws IOException {
    Files.delete(temporaryAsset);
    PathPlannerPath.clearCache();

    assertThrows(IllegalStateException.class, () -> createAdapter().createCanonicalTrajectory());
  }

  @Test
  void malformedAssetFailsClosed() throws IOException {
    Files.writeString(temporaryAsset, "{");
    PathPlannerPath.clearCache();

    assertThrows(IllegalStateException.class, () -> createAdapter().createCanonicalTrajectory());
  }

  @Test
  void unsupportedFeatureFailsClosed() throws IOException {
    String asset = Files.readString(temporaryAsset);
    asset =
        asset.replace(
            "\"rotationTargets\": []",
            "\"rotationTargets\": [{\"waypointRelativePos\": 0.5, \"rotationDegrees\": 0.0}]");
    Files.writeString(temporaryAsset, asset);
    PathPlannerPath.clearCache();

    assertThrows(IllegalStateException.class, () -> createAdapter().createCanonicalTrajectory());
  }

  @Test
  void invalidNonfiniteAndNonmonotonicStatesFailClosed() throws ReflectiveOperationException {
    assertValidationRejects(
        new PathPlannerTrajectory(
            List.of(
                createState(0.0, 0.0, 0.0, 0.0),
                createState(1.0, Double.NaN, 1.0, 0.0))));
    assertValidationRejects(
        new PathPlannerTrajectory(
            List.of(
                createState(0.0, 0.0, 0.0, 0.0),
                createState(1.0, 0.0, 0.0, 0.0),
                createState(0.5, 0.0, 1.0, 0.0))));
    assertValidationRejects(
        new PathPlannerTrajectory(
            List.of(
                createState(0.0, 0.0, 0.0, 0.0),
                createState(1.0, 0.0, 0.0, Math.PI / 2.0))));
  }

  @Test
  void invalidRobotConfigFailsClosedBeforeGeneration() throws ReflectiveOperationException {
    Method factory = RobotContainer.class.getDeclaredMethod("createPathPlannerRobotConfig");
    factory.setAccessible(true);
    RobotConfig validConfig = (RobotConfig) factory.invoke(null);
    RobotConfig invalidConfig =
        new RobotConfig(
            0.0,
            validConfig.MOI,
            validConfig.moduleConfig,
            validConfig.moduleLocations);

    assertThrows(IllegalArgumentException.class, () -> new PathPlannerTrajectoryAdapter(invalidConfig));
  }

  private PathPlannerTrajectoryAdapter createAdapter() {
    try {
      return new PathPlannerTrajectoryAdapter(createRobotConfig());
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(exception);
    }
  }

  private static RobotConfig createRobotConfig() throws ReflectiveOperationException {
    Method factory = RobotContainer.class.getDeclaredMethod("createPathPlannerRobotConfig");
    factory.setAccessible(true);
    return (RobotConfig) factory.invoke(null);
  }

  private static PathPlannerTrajectory loadSourceTrajectory() {
    try {
      PathPlannerPath path =
          PathPlannerPath.fromPathFile(Constants.PathPlannerLearningConstants.kPathAssetName);
      return path.generateTrajectory(new ChassisSpeeds(), Rotation2d.kZero, createRobotConfig());
    } catch (Exception exception) {
      throw new AssertionError(exception);
    }
  }

  private static void assertValidationRejects(PathPlannerTrajectory trajectory)
      throws ReflectiveOperationException {
    Method validator =
        PathPlannerTrajectoryAdapter.class.getDeclaredMethod(
            "validatePathPlannerTrajectory", PathPlannerTrajectory.class);
    validator.setAccessible(true);
    InvocationTargetException exception =
        assertThrows(
            InvocationTargetException.class,
            () -> validator.invoke(null, trajectory));
    assertTrue(exception.getCause() instanceof IllegalStateException);
  }

  private static PathPlannerTrajectoryState createState(
      double timeSeconds, double linearVelocity, double xMeters, double headingRadians) {
    PathPlannerTrajectoryState state = new PathPlannerTrajectoryState();
    state.timeSeconds = timeSeconds;
    state.linearVelocity = linearVelocity;
    state.fieldSpeeds = new ChassisSpeeds();
    state.pose = new Pose2d(xMeters, 0.0, new Rotation2d(headingRadians));
    state.heading = new Rotation2d(headingRadians);
    return state;
  }

  private static Path sourceAsset() {
    return sourceAsset(kPathFileName);
  }

  private static Path sourceEventAsset() {
    return sourceAsset(kEventPathFileName);
  }

  private static Path sourceAsset(String fileName) {
    return Path.of(
            System.getProperty("user.dir"),
            "src",
            "main",
            "deploy",
            "pathplanner",
            "paths",
            fileName)
        .toAbsolutePath()
        .normalize();
  }

  private static void assertMotorEquals(DCMotor expected, DCMotor actual) {
    assertEquals(expected.nominalVoltageVolts, actual.nominalVoltageVolts, kTolerance);
    assertEquals(expected.stallTorqueNewtonMeters, actual.stallTorqueNewtonMeters, kTolerance);
    assertEquals(expected.stallCurrentAmps, actual.stallCurrentAmps, kTolerance);
    assertEquals(expected.freeCurrentAmps, actual.freeCurrentAmps, kTolerance);
    assertEquals(expected.freeSpeedRadPerSec, actual.freeSpeedRadPerSec, kTolerance);
    assertEquals(expected.rOhms, actual.rOhms, kTolerance);
    assertEquals(expected.KvRadPerSecPerVolt, actual.KvRadPerSecPerVolt, kTolerance);
    assertEquals(expected.KtNMPerAmp, actual.KtNMPerAmp, kTolerance);
  }

  private static void assertPoseEquals(Pose2d expected, Pose2d actual) {
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(
        0.0,
        MathUtil.angleModulus(expected.getRotation().getRadians() - actual.getRotation().getRadians()),
        kTolerance);
  }
}
