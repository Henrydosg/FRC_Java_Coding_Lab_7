// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.AllianceStationID;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.AutonomousPreparationCoordinator;
import frc.robot.commands.AutonomousStartContext;
import frc.robot.commands.AutonomousRoutineFactory;
import frc.robot.commands.AutonomousSafetyHoldCommand;
import frc.robot.commands.PathPlannerTrajectoryAdapter;
import frc.robot.commands.PrepareAutonomousCommand;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.util.FieldAllianceTransform;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RobotContainerPathPlannerIntegrationTest {
  private static final double kTolerance = 1.0e-9;
  private static final String kPathFileName = "A01_L06_OneMeter_Forward.path";

  private static CommandScheduler scheduler;
  private RobotContainer robotContainer;
  private Command autonomousCommand;
  private SwerveSubsystem swerveSubsystem;
  private PrepareAutonomousCommand prepareCommand;
  private AutonomousPreparationCoordinator preparationCoordinator;
  private String previousUserDirectory;
  private Path temporaryAsset;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
    scheduler = CommandScheduler.getInstance();
  }

  @BeforeEach
  void createCompositionRootWithTemporaryDeployment(@TempDir Path temporaryDirectory)
      throws IOException {
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    previousUserDirectory = System.getProperty("user.dir");
    temporaryAsset =
        temporaryDirectory
            .resolve("src/main/deploy/pathplanner/paths")
            .resolve(kPathFileName);
    Files.createDirectories(temporaryAsset.getParent());
    Files.copy(sourceAsset(), temporaryAsset, StandardCopyOption.REPLACE_EXISTING);
    System.setProperty("user.dir", temporaryDirectory.toString());
    PathPlannerPath.clearCache();

    scheduler.cancelAll();
    setDisabledMode(AllianceStationID.Blue1);
    robotContainer = new RobotContainer();
    autonomousCommand = robotContainer.getAutonomousCommand();
    swerveSubsystem =
        (SwerveSubsystem)
            autonomousCommand.getRequirements().stream().findFirst().orElseThrow();
    prepareCommand =
        (PrepareAutonomousCommand)
            SmartDashboard.getData("Prepare Autonomous");
    preparationCoordinator = preparationCoordinator();

    scheduler.run();
  }

  @AfterEach
  void cleanUp() {
    scheduler.cancelAll();
    setDisabledMode(AllianceStationID.Blue1);
    scheduler.run();
    PathPlannerPath.clearCache();
    AutoBuilder.resetForTesting();
    NamedCommands.clearAll();
    System.setProperty("user.dir", previousUserDirectory);
  }

  @AfterAll
  static void finalSchedulerCleanup() {
    scheduler.cancelAll();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    scheduler.run();
  }

  @Test
  void preservesTheSingleSwerveRequirement() {
    assertEquals(1, autonomousCommand.getRequirements().size());
    assertTrue(autonomousCommand.getRequirements().contains(swerveSubsystem));
  }

  @Test
  void configuresAutoBuilderWithFlippingDisabled() {
    assertTrue(AutoBuilder.isConfigured());
    assertFalse(AutoBuilder.shouldFlip());
  }

  @Test
  void blueStartContextIsCanonicalStart() {
    Optional<AutonomousStartContext> context = acceptReset(AllianceStationID.Blue1);

    assertTrue(context.isPresent());
    assertEquals(DriverStation.Alliance.Blue, context.orElseThrow().alliance());
    assertPoseEquals(
        Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose,
        context.orElseThrow().executionStartPose());
  }

  @Test
  void redStartContextIsExactlyOneL04TransformOfCanonicalStart() {
    Optional<AutonomousStartContext> context = acceptReset(AllianceStationID.Red2);
    Pose2d expectedRedStart =
        FieldAllianceTransform.fromCanonicalBluePose(
            Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose,
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            DriverStation.Alliance.Red);

    assertTrue(context.isPresent());
    assertEquals(DriverStation.Alliance.Red, context.orElseThrow().alliance());
    assertPoseEquals(expectedRedStart, context.orElseThrow().executionStartPose());

    Trajectory canonical = adapter().createCanonicalTrajectory();
    Trajectory red =
        FieldAllianceTransform.fromCanonicalBlueTrajectory(
            canonical,
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            DriverStation.Alliance.Red);
    assertPoseEquals(red.getInitialPose(), context.orElseThrow().executionStartPose());

    Trajectory doubleTransformed =
        FieldAllianceTransform.fromCanonicalBlueTrajectory(
            red,
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            DriverStation.Alliance.Red);
    assertPoseEquals(canonical.getInitialPose(), doubleTransformed.getInitialPose());
    assertFalse(
        posesEqual(
            red.getInitialPose(),
            doubleTransformed.getInitialPose()));
  }

  @Test
  void bluePathTransformIsIdentityAndRedPathTransformIsSingleL04Transform() {
    Trajectory canonical = adapter().createCanonicalTrajectory();
    Trajectory blue =
        FieldAllianceTransform.fromCanonicalBlueTrajectory(
            canonical,
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            DriverStation.Alliance.Blue);
    Trajectory red =
        FieldAllianceTransform.fromCanonicalBlueTrajectory(
            canonical,
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            DriverStation.Alliance.Red);

    assertPoseEquals(canonical.getInitialPose(), blue.getInitialPose());
    assertEquals(
        Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant.fieldLengthMeters()
            - canonical.getInitialPose().getX(),
        red.getInitialPose().getX(),
        kTolerance);
    assertEquals(
        Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant.fieldWidthMeters()
            - canonical.getInitialPose().getY(),
        red.getInitialPose().getY(),
        kTolerance);
  }

  @Test
  void unknownAllianceProducesStopOnlyAutonomousBranch() {
    assertTrue(acceptReset(AllianceStationID.Unknown).isEmpty());

    setAutonomousMode();
    Command selectedCommand = requestOneMeterPathCommand();
    assertTrue(selectedCommand instanceof AutonomousSafetyHoldCommand);
    scheduler.schedule(selectedCommand);
    scheduler.run();

    assertZeroFinalModuleStates();
    selectedCommand.cancel();
  }

  @Test
  void adapterFailureProducesStopOnlyAutonomousBranch() throws IOException {
    Files.delete(temporaryAsset);
    PathPlannerPath.clearCache();
    assertTrue(acceptReset(AllianceStationID.Blue1).isEmpty());

    setAutonomousMode();
    Command selectedCommand = requestOneMeterPathCommand();
    assertTrue(selectedCommand instanceof AutonomousSafetyHoldCommand);
    scheduler.schedule(selectedCommand);
    scheduler.run();

    assertZeroFinalModuleStates();
    selectedCommand.cancel();
  }

  @Test
  void modeLossStopsAutonomousAndPreventsRestartWithoutFreshReset() {
    completeResetWithoutConsuming(AllianceStationID.Blue1);
    setAutonomousMode();
    Command selectedCommand = requestOneMeterPathCommand();
    scheduler.schedule(selectedCommand);
    scheduler.run();
    assertTrue(selectedCommand.isScheduled());

    setTeleoperatedMode();
    scheduler.run();

    assertFalse(selectedCommand.isScheduled());
    assertZeroFinalModuleStates();
    setAutonomousMode();
    scheduler.run();
    assertFalse(selectedCommand.isScheduled());
    assertZeroFinalModuleStates();
  }

  @Test
  void cancellationStopsAndOneShotReadinessIsConsumed() {
    completeResetWithoutConsuming(AllianceStationID.Blue1);
    setAutonomousMode();
    Command selectedCommand = requestOneMeterPathCommand();
    scheduler.schedule(selectedCommand);
    scheduler.run();
    assertTrue(selectedCommand.isScheduled());

    selectedCommand.cancel();
    scheduler.run();
    assertFalse(selectedCommand.isScheduled());
    assertZeroFinalModuleStates();

    setDisabledMode(AllianceStationID.Blue1);
    scheduler.run();
    setAutonomousMode();
    Command secondCommand = requestOneMeterPathCommand();
    assertTrue(secondCommand instanceof AutonomousSafetyHoldCommand);
    scheduler.schedule(secondCommand);
    scheduler.run();
    assertZeroFinalModuleStates();
    secondCommand.cancel();
  }

  private Command requestOneMeterPathCommand() {
    selectOneMeterPath();
    return robotContainer.getAutonomousCommand();
  }

  private void selectOneMeterPath() {
    @SuppressWarnings("unchecked")
    SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId> chooser =
        (SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId>)
            SmartDashboard.getData("Autonomous Routine");
    try {
      Field selectedField = SendableChooser.class.getDeclaredField("m_selected");
      selectedField.setAccessible(true);
      selectedField.set(chooser, "ONE_METER_PATH");
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(exception);
    }
  }

  private void completeResetWithoutConsuming(AllianceStationID station) {
    setDisabledMode(station);
    selectOneMeterPath();
    scheduler.schedule(prepareCommand);
    scheduler.run();
    assertFalse(prepareCommand.isScheduled());
    assertPoseEquals(
        Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose,
        swerveSubsystem.getEstimatedPose().orElseThrow());
  }

  private Optional<AutonomousStartContext> acceptReset(AllianceStationID station) {
    setDisabledMode(station);
    selectOneMeterPath();
    scheduler.schedule(prepareCommand);
    scheduler.run();
    assertFalse(prepareCommand.isScheduled());
    Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
    return preparationCoordinator
        .previewDrivingPreparation(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            alliance)
        .map(AutonomousPreparationCoordinator.PreparationClaim::startContext);
  }

  private AutonomousPreparationCoordinator preparationCoordinator() {
    try {
      Field field =
          RobotContainer.class.getDeclaredField("autonomousPreparationCoordinator");
      field.setAccessible(true);
      return (AutonomousPreparationCoordinator) field.get(robotContainer);
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(exception);
    }
  }

  private PathPlannerTrajectoryAdapter adapter() {
    try {
      Field field = RobotContainer.class.getDeclaredField("pathPlannerTrajectoryAdapter");
      field.setAccessible(true);
      return (PathPlannerTrajectoryAdapter) field.get(robotContainer);
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(exception);
    }
  }

  private void assertZeroFinalModuleStates() {
    for (var state : swerveSubsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.0, state.angle.getRadians(), kTolerance);
    }
  }

  private static Path sourceAsset() {
    return Path.of(
            System.getProperty("user.dir"),
            "src",
            "main",
            "deploy",
            "pathplanner",
            "paths",
            kPathFileName)
        .toAbsolutePath()
        .normalize();
  }

  private static void setDisabledMode(AllianceStationID station) {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.setAllianceStationId(station);
    DriverStationSim.notifyNewData();
  }

  private static void setAutonomousMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void setTeleoperatedMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static boolean posesEqual(Pose2d first, Pose2d second) {
    return Math.abs(first.getX() - second.getX()) <= kTolerance
        && Math.abs(first.getY() - second.getY()) <= kTolerance
        && Math.abs(
                MathUtil.angleModulus(
                    first.getRotation().getRadians() - second.getRotation().getRadians()))
            <= kTolerance;
  }

  private static void assertPoseEquals(Pose2d expected, Pose2d actual) {
    assertNotNull(actual);
    assertEquals(expected.getX(), actual.getX(), kTolerance);
    assertEquals(expected.getY(), actual.getY(), kTolerance);
    assertEquals(
        0.0,
        MathUtil.angleModulus(expected.getRotation().getRadians() - actual.getRotation().getRadians()),
        kTolerance);
  }
}
