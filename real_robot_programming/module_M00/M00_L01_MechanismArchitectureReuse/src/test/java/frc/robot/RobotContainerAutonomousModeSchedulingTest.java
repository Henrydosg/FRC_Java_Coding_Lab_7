// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.hal.AllianceStationID;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.commands.AutonomousSafetyHoldCommand;
import frc.robot.commands.AutonomousRoutineFactory;
import frc.robot.commands.AutonomousPreparationCoordinator;
import frc.robot.commands.AllianceAwareAutonomousStartPoseResetCommand;
import frc.robot.commands.AutonomousStartContext;
import frc.robot.commands.CaptureFieldHeadingReferenceCommand;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.commands.PrepareAutonomousCommand;
import frc.robot.commands.PoseTargetedAutonomousMotionCommand;
import frc.robot.subsystems.SwerveSubsystem;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.DoubleSupplier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RobotContainerAutonomousModeSchedulingTest {
  private static final double kTolerance = 1.0e-9;

  private static RobotContainer robotContainer;
  private static Command autonomousCommand;
  private static SwerveSubsystem swerveSubsystem;
  private static PrepareAutonomousCommand prepareAutonomousCommand;

  @BeforeAll
  static void initializeHalAndCompositionRoot() {
    HAL.initialize(500, 0);
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    robotContainer = new RobotContainer();
    autonomousCommand = robotContainer.getAutonomousCommand();
    swerveSubsystem =
        (SwerveSubsystem)
            autonomousCommand.getRequirements().stream().findFirst().orElseThrow();
    prepareAutonomousCommand =
        assertInstanceOf(
            PrepareAutonomousCommand.class,
            SmartDashboard.getData("Prepare Autonomous"));
  }

  @BeforeEach
  void enterAutonomousAndClearScheduler() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.cancelAll();
    setDisabledMode();
    scheduler.run();
    selectOneMeterPath();
    scheduler.schedule(prepareAutonomousCommand);
    scheduler.run();
    assertFalse(prepareAutonomousCommand.isScheduled());
    assertTrue(
        preparationCoordinator().getObservation().ready(),
        preparationCoordinator().getObservation().toString());
    setAutonomousMode();
    scheduler.run();
  }

  @AfterEach
  void clearSchedulerAndDisable() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.cancelAll();
    setDisabledMode();
    scheduler.run();
  }

  @AfterAll
  static void finalSchedulerCleanup() {
    CommandScheduler.getInstance().cancelAll();
    setDisabledMode();
    CommandScheduler.getInstance().run();
    AutoBuilder.resetForTesting();
    NamedCommands.clearAll();
  }

  @Test
  void returnsGatedAutonomousSessionWithSwerveRequirement() {
    Command selectedCommand = requestOneMeterPathCommand();
    Command secondCommand = requestOneMeterPathCommand();

    assertNotSame(selectedCommand, secondCommand);
    assertEquals(1, selectedCommand.getRequirements().size());
    assertTrue(selectedCommand.getRequirements().contains(swerveSubsystem));
  }

  @Test
  void autonomousSchedulingInterruptsDefaultAndDispatchesOnlyPoseTargetedMotion() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Command selectedCommand = requestOneMeterPathCommand();

    scheduler.schedule(selectedCommand);
    scheduler.run();
    scheduler.run();

    assertTrue(
        selectedCommand.isScheduled(),
        preparationCoordinator().getObservation().toString());
    assertFalse(swerveSubsystem.getDefaultCommand().isScheduled());
    assertTrue(swerveSubsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  @Test
  void noFreshAcceptedResetCannotProduceAutonomousMotion() {
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setTeleoperatedMode();
    scheduler.schedule(prepareAutonomousCommand);
    scheduler.run();

    setAutonomousMode();
    Command selectedCommand = requestOneMeterPathCommand();
    assertInstanceOf(AutonomousSafetyHoldCommand.class, selectedCommand);
    scheduler.schedule(selectedCommand);
    scheduler.run();
    scheduler.run();

    assertZeroFinalModuleStates(swerveSubsystem);
    selectedCommand.cancel();
  }

  @Test
  void consumedReadinessRejectsASecondAutonomousSchedulingAttempt() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Command selectedCommand = requestOneMeterPathCommand();

    scheduler.schedule(selectedCommand);
    scheduler.run();
    scheduler.run();
    assertTrue(swerveSubsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);

    selectedCommand.cancel();
    scheduler.run();
    setDisabledMode();
    scheduler.run();
    setAutonomousMode();
    Command secondCommand = requestOneMeterPathCommand();
    assertInstanceOf(AutonomousSafetyHoldCommand.class, secondCommand);
    scheduler.schedule(secondCommand);
    scheduler.run();
    scheduler.run();

    assertZeroFinalModuleStates(swerveSubsystem);
    secondCommand.cancel();
  }

  @Test
  void newAcceptedResetPermitsALaterAutonomousSession() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Command firstCommand = requestOneMeterPathCommand();

    scheduler.schedule(firstCommand);
    scheduler.run();
    firstCommand.cancel();
    scheduler.run();
    setDisabledMode();
    scheduler.run();

    scheduler.schedule(prepareAutonomousCommand);
    scheduler.run();
    assertFalse(prepareAutonomousCommand.isScheduled());

    setAutonomousMode();
    Command secondCommand = requestOneMeterPathCommand();
    scheduler.schedule(secondCommand);
    scheduler.run();
    scheduler.run();

    assertTrue(secondCommand.isScheduled());
    assertTrue(swerveSubsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  @Test
  void incomingPreparationAndLegacyActionsCannotCancelActiveAutonomous() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Command selectedCommand = requestOneMeterPathCommand();
    scheduler.schedule(selectedCommand);
    scheduler.run();
    assertTrue(
        selectedCommand.isScheduled(),
        preparationCoordinator().getObservation().toString());

    scheduler.schedule(prepareAutonomousCommand);
    scheduler.run();
    assertTrue(
        selectedCommand.isScheduled(),
        preparationCoordinator().getObservation().toString());
    assertFalse(prepareAutonomousCommand.isScheduled());

    AutonomousStartContext context =
        new AutonomousStartContext(
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            DriverStation.Alliance.Blue,
            Constants.PathPlannerLearningConstants.kCanonicalPathStartingPose);
    Command legacyReset =
        new AllianceAwareAutonomousStartPoseResetCommand(
            swerveSubsystem, () -> Optional.of(context));
    scheduler.schedule(legacyReset);
    scheduler.run();
    assertTrue(selectedCommand.isScheduled());
    assertFalse(legacyReset.isScheduled());

    Command legacyHeading =
        new CaptureFieldHeadingReferenceCommand(swerveSubsystem);
    scheduler.schedule(legacyHeading);
    scheduler.run();
    assertTrue(selectedCommand.isScheduled());
    assertFalse(legacyHeading.isScheduled());
  }

  @Test
  void sessionHoldRetainsRequirementForEntireAutonomousMode() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    AutonomousSafetyHoldCommand hold = new AutonomousSafetyHoldCommand(subsystem);
    Command defaultCommand = Commands.run(() -> {}, subsystem);
    subsystem.setDefaultCommand(defaultCommand);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(hold);
    scheduler.run();
    scheduler.run();
    scheduler.run();

    assertTrue(hold.isScheduled());
    assertFalse(defaultCommand.isScheduled());
    assertEquals(0, subsystem.acceptCount);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void poseTargetedMotionTransitionsToRepeatingHoldWithoutReleasingSwerve() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    MutableClock clock = new MutableClock(0.0);
    Command autonomous = createGatedAutonomousCommand(subsystem, clock);
    Command defaultCommand = Commands.run(() -> {}, subsystem);
    subsystem.setDefaultCommand(defaultCommand);

    CommandScheduler scheduler = CommandScheduler.getInstance();
    setAutonomousMode();
    scheduler.schedule(autonomous);
    scheduler.run();
    assertTrue(autonomous.isScheduled());
    assertEquals(1, subsystem.acceptCount);
    assertFalse(defaultCommand.isScheduled());
    assertTrue(subsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);

    clock.set(Constants.PoseTargetedAutonomousConstants.kTimeoutSeconds + 0.01);
    scheduler.run();
    scheduler.run();

    assertTrue(autonomous.isScheduled());
    assertFalse(defaultCommand.isScheduled());
    assertEquals(1, subsystem.acceptCount);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void teleopInitialSchedulingCannotProduceAutonomousMotionOrRestartLater() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    Command autonomous = createGatedAutonomousCommand(subsystem, new MutableClock(0.0));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setTeleoperatedMode();
    scheduler.schedule(autonomous);
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertEquals(0, subsystem.acceptCount);
    assertTrue(subsystem.stopCount > 0);
    assertZeroFinalModuleStates(subsystem);

    setAutonomousMode();
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertEquals(0, subsystem.acceptCount);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void testInitialSchedulingCannotProduceAutonomousMotion() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    Command autonomous = createGatedAutonomousCommand(subsystem, new MutableClock(0.0));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setTestMode();
    scheduler.schedule(autonomous);
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertEquals(0, subsystem.acceptCount);
    assertTrue(subsystem.stopCount > 0);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void disabledInitialSchedulingCannotProduceAutonomousMotion() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    Command autonomous = createGatedAutonomousCommand(subsystem, new MutableClock(0.0));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setDisabledMode();
    scheduler.schedule(autonomous);
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertEquals(0, subsystem.acceptCount);
    assertTrue(subsystem.stopCount > 0);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void activeAutonomousToTeleopTerminatesMotionAndDoesNotRestart() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    Command autonomous = createGatedAutonomousCommand(subsystem, new MutableClock(0.0));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setAutonomousMode();
    scheduler.schedule(autonomous);
    scheduler.run();
    assertTrue(autonomous.isScheduled());
    assertEquals(1, subsystem.acceptCount);

    setTeleoperatedMode();
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertTrue(subsystem.stopCount > 0);
    assertZeroFinalModuleStates(subsystem);

    setAutonomousMode();
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertEquals(1, subsystem.acceptCount);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void activeAutonomousToTestTerminatesMotionAndStops() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    Command autonomous = createGatedAutonomousCommand(subsystem, new MutableClock(0.0));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setAutonomousMode();
    scheduler.schedule(autonomous);
    scheduler.run();
    assertTrue(autonomous.isScheduled());

    setTestMode();
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertTrue(subsystem.stopCount > 0);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void activeAutonomousToDisabledStopsAndClearsMotionIntent() {
    RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    Command autonomous = createGatedAutonomousCommand(subsystem, new MutableClock(0.0));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    setAutonomousMode();
    scheduler.schedule(autonomous);
    scheduler.run();
    assertTrue(autonomous.isScheduled());

    setDisabledMode();
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertTrue(subsystem.stopCount > 0);
    assertZeroFinalModuleStates(subsystem);
  }

  @Test
  void gateFailureLeavesTeleopRecoveryToFreshDefaultCommandPath() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Command selectedCommand = requestOneMeterPathCommand();
    scheduler.schedule(selectedCommand);
    scheduler.run();
    assertTrue(selectedCommand.isScheduled());

    setTeleoperatedMode();
    scheduler.run();

    assertFalse(
        selectedCommand.isScheduled(),
        preparationCoordinator().getObservation().toString());
    assertTrue(swerveSubsystem.getDefaultCommand().isScheduled());

    setAutonomousMode();
    scheduler.run();

    assertFalse(selectedCommand.isScheduled());
    assertTrue(swerveSubsystem.getDefaultCommand().isScheduled());
  }

  @Test
  void autonomousToTeleopCancelsHoldAndAllowsFreshRequestRecovery() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(autonomousCommand);
    scheduler.run();
    assertTrue(autonomousCommand.isScheduled());

    setTeleoperatedMode();
    autonomousCommand.cancel();
    scheduler.run();
    scheduler.run();

    assertFalse(autonomousCommand.isScheduled());
    assertTrue(swerveSubsystem.getDefaultCommand().isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);

    swerveSubsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));
    assertTrue(swerveSubsystem.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  @Test
  void autonomousToDisabledCancelsAndLeavesNoStaleIntent() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(autonomousCommand);
    scheduler.run();
    swerveSubsystem.acceptChassisSpeeds(new ChassisSpeeds(0.25, 0.0, 0.0));

    setDisabledMode();
    scheduler.run();

    assertFalse(autonomousCommand.isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);
  }

  @Test
  void autonomousToTestCancelsSafetyCommandAndStops() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    scheduler.schedule(autonomousCommand);
    scheduler.run();
    assertTrue(autonomousCommand.isScheduled());

    setTestMode();
    scheduler.cancelAll();
    scheduler.run();

    assertFalse(autonomousCommand.isScheduled());
    assertZeroFinalModuleStates(swerveSubsystem);
  }

  private static void assertZeroFinalModuleStates(SwerveSubsystem subsystem) {
    for (var state : subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
      assertEquals(0.0, state.angle.getRadians(), kTolerance);
    }
  }

  private static Command requestOneMeterPathCommand() {
    selectOneMeterPath();
    return robotContainer.getAutonomousCommand();
  }

  private static void selectOneMeterPath() {
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

  private static AutonomousPreparationCoordinator preparationCoordinator() {
    try {
      Field field =
          RobotContainer.class.getDeclaredField("autonomousPreparationCoordinator");
      field.setAccessible(true);
      return (AutonomousPreparationCoordinator) field.get(robotContainer);
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(exception);
    }
  }

  private static Command createGatedAutonomousCommand(
      SwerveSubsystem subsystem, DoubleSupplier clock) {
    setDisabledMode();
    subsystem.periodic();
    assertTrue(subsystem.captureFieldHeadingReference());
    subsystem.periodic();

    Command poseTargetedMotion =
        new PoseTargetedAutonomousMotionCommand(
            subsystem,
            Constants.PoseTargetedAutonomousConstants.kLearningTargetPose,
            Constants.PoseTargetedAutonomousConstants.kTranslationKpPerSecond,
            Constants.PoseTargetedAutonomousConstants.kHeadingKpPerSecond,
            Constants.PoseTargetedAutonomousConstants.kMaxTranslationSpeedMetersPerSecond,
            Constants.PoseTargetedAutonomousConstants.kMaxAngularSpeedRadiansPerSecond,
            Constants.PoseTargetedAutonomousConstants.kTranslationToleranceMeters,
            Constants.PoseTargetedAutonomousConstants.kHeadingToleranceRadians,
            Constants.PoseTargetedAutonomousConstants.kTimeoutSeconds,
            clock);
    Command repeatingSafetyHold = new AutonomousSafetyHoldCommand(subsystem);
    Command inheritedAutonomousSession =
        poseTargetedMotion.andThen(repeatingSafetyHold);
    return Commands.either(
            inheritedAutonomousSession,
            Commands.runOnce(subsystem::stop, subsystem),
            DriverStation::isAutonomousEnabled)
        .onlyWhile(DriverStation::isAutonomousEnabled);
  }

  private static void setAutonomousMode() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.setAllianceStationId(AllianceStationID.Blue1);
    DriverStationSim.notifyNewData();
  }

  private static void setTeleoperatedMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static void setTestMode() {
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
  }

  private static void setDisabledMode() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.setAllianceStationId(AllianceStationID.Blue1);
    DriverStationSim.notifyNewData();
  }

  private static final class MutableClock implements DoubleSupplier {
    private double value;

    private MutableClock(double value) {
      this.value = value;
    }

    @Override
    public double getAsDouble() {
      return value;
    }

    private void set(double value) {
      this.value = value;
    }
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int acceptCount;
    private int stopCount;

    private RecordingSwerveSubsystem() {
      super(
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingModuleIO(),
          new RecordingGyroIO());
    }

    @Override
    public void acceptFieldRelativeChassisSpeeds(ChassisSpeeds fieldRelativeSpeeds) {
      acceptCount++;
      super.acceptFieldRelativeChassisSpeeds(fieldRelativeSpeeds);
    }

    @Override
    public void stop() {
      stopCount++;
      super.stop();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.driveConnected = true;
      inputs.driveConfigurationHealthy = true;
      inputs.steerConnected = true;
      inputs.steerConfigurationHealthy = true;
      inputs.encoderConnected = true;
      inputs.encoderConfigurationHealthy = true;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {}
  }

  private static final class RecordingGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.connected = true;
      inputs.configurationHealthy = true;
    }
  }
}
