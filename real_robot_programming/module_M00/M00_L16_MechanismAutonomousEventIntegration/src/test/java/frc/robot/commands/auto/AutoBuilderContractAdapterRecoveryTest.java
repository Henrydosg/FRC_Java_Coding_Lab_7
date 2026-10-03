// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands.auto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.commands.auto.AutoBuilderContractAdapter.ExecutionOutcome;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutoBuilderContractAdapterRecoveryTest {
  private static final double kTolerance = 1.0e-12;
  private static final double kPathTimeoutSeconds = 10.0;
  private Rig rig;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void resetDriverStation() {
    DriverStationSim.resetData();
    setAutonomousEnabled(false);
  }

  @AfterEach
  void cleanUp() {
    if (rig != null) {
      rig.endPath(true);
      rig.swerve.stop();
      CommandScheduler.getInstance().unregisterSubsystem(rig.swerve);
    }
    setAutonomousEnabled(false);
  }

  @Test
  void poseValidationAcceptsExactUnderAndExactThreshold() {
    Pose2d expected = Pose2d.kZero;

    assertTrue(AutoBuilderContractAdapter.isPoseWithinTolerance(expected, expected));
    assertTrue(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                Constants.AutonomousPreparationConstants.kTranslationToleranceMeters - 1.0e-6,
                0.0,
                Rotation2d.fromRadians(
                    Constants.AutonomousPreparationConstants.kHeadingToleranceRadians
                        - 1.0e-6)),
            expected));
    assertTrue(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                Constants.AutonomousPreparationConstants.kTranslationToleranceMeters,
                0.0,
                Rotation2d.fromRadians(
                    Constants.AutonomousPreparationConstants.kHeadingToleranceRadians)),
            expected));
  }

  @Test
  void poseValidationRejectsEitherErrorAboveThreshold() {
    Pose2d expected = Pose2d.kZero;

    assertFalse(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                Constants.AutonomousPreparationConstants.kTranslationToleranceMeters + 1.0e-6,
                0.0,
                Rotation2d.kZero),
            expected));
    assertFalse(
        AutoBuilderContractAdapter.isPoseWithinTolerance(
            new Pose2d(
                0.0,
                0.0,
                Rotation2d.fromRadians(
                    Constants.AutonomousPreparationConstants.kHeadingToleranceRadians
                        + 1.0e-6)),
            expected));
  }

  @Test
  void wrappedPositiveAndNegativePiAreEquivalent() {
    Pose2d positivePi = new Pose2d(0.0, 0.0, Rotation2d.fromRadians(Math.PI));
    Pose2d negativePi = new Pose2d(0.0, 0.0, Rotation2d.fromRadians(-Math.PI));

    assertEquals(
        0.0,
        AutoBuilderContractAdapter.headingErrorRadians(positivePi, negativePi),
        kTolerance);
    assertTrue(
        AutoBuilderContractAdapter.isPoseWithinTolerance(positivePi, negativePi));
  }

  @Test
  void nonfinitePoseCannotPassValidation() {
    Pose2d nonfinite = new Pose2d(Double.NaN, 0.0, Rotation2d.kZero);

    assertThrows(
        IllegalArgumentException.class,
        () -> AutoBuilderContractAdapter.isPoseWithinTolerance(nonfinite, Pose2d.kZero));
  }

  @Test
  void unavailableSpeedsBlockSameCycleOutputAndTerminateThePath() {
    rig = new Rig();
    rig.frontLeft.driveVelocityRotationsPerSecond = Double.NaN;
    rig.swerve.periodic();
    assertTrue(rig.estimatedSampleValid());
    assertTrue(rig.swerve.getMeasuredRobotRelativeSpeeds().isEmpty());
    List<String> callbackOrder = new ArrayList<>();
    rig.startPath(
        () -> {
          callbackOrder.add("pose");
          rig.supplyPose();
          callbackOrder.add("speeds");
          rig.supplySpeeds();
          assertEquals(ExecutionOutcome.INPUT_UNAVAILABLE, rig.adapter.executionOutcome());
          assertTrue(rig.frontLeft.stopCount > 0);
          callbackOrder.add("output");
          rig.acceptOutput(new ChassisSpeeds(1.0, 0.0, 0.0));
        });

    rig.command.execute();

    assertEquals(List.of("pose", "speeds", "output"), callbackOrder);
    assertEquals(0, rig.swerve.acceptedRequestCount);
    assertInputUnavailableAndStopped();
    assertTrue(rig.command.isFinished());
    rig.endPath(false);
    assertTrue(rig.followPath.interrupted);
    assertEquals(1, rig.followPath.endCount);
    assertEquals(ExecutionOutcome.INPUT_UNAVAILABLE, rig.adapter.executionOutcome());
  }

  @Test
  void laterCallbacksCannotResumeTheFailedExecutionWhenSpeedsRecover() {
    rig = new Rig();
    rig.frontLeft.driveVelocityRotationsPerSecond = Double.NaN;
    rig.swerve.periodic();
    rig.startPath(rig::runHealthyCallbackOrder);
    rig.command.execute();

    rig.frontLeft.driveVelocityRotationsPerSecond = 0.0;
    rig.swerve.periodic();
    assertTrue(rig.swerve.getMeasuredRobotRelativeSpeeds().isPresent());
    rig.supplyPose();
    rig.supplySpeeds();
    rig.acceptOutput(new ChassisSpeeds(0.5, 0.0, 0.0));
    assertEquals(0, rig.swerve.acceptedRequestCount);
    assertInputUnavailableAndStopped();

    rig.endPath(false);
    rig.acceptOutput(new ChassisSpeeds(0.25, 0.0, 0.0));
    assertEquals(0, rig.swerve.acceptedRequestCount);
    assertInputUnavailableAndStopped();
  }

  @Test
  void invalidHeldEstimatedPoseBlocksOutputWithOtherwiseUsableSpeeds() {
    rig = new Rig();
    Pose2d preparedPose = new Pose2d(0.25, 0.0, Rotation2d.kZero);
    rig.prepareKnownPose(preparedPose);
    rig.startPath(rig::runHealthyCallbackOrder);
    rig.command.execute();
    assertEquals(1, rig.swerve.acceptedRequestCount);

    rig.gyro.connected = false;
    rig.swerve.periodic();
    assertEquals(preparedPose, rig.swerve.getEstimatedPose().orElseThrow());
    assertFalse(rig.estimatedSampleValid());
    assertTrue(rig.swerve.getMeasuredRobotRelativeSpeeds().isPresent());
    int acceptedBeforeFailure = rig.swerve.acceptedRequestCount;
    int stopsBeforeFailure = rig.frontLeft.stopCount;

    rig.command.execute();

    assertEquals(Pose2d.kZero, rig.lastSuppliedPose);
    assertEquals(acceptedBeforeFailure, rig.swerve.acceptedRequestCount);
    assertTrue(rig.frontLeft.stopCount > stopsBeforeFailure);
    assertInputUnavailableAndStopped();
    assertTrue(rig.command.isFinished());
    rig.endPath(false);
  }

  @Test
  void healthyFeedbackAcceptsOrdinaryFiniteOutput() {
    rig = new Rig();
    rig.startPath(rig::runHealthyCallbackOrder);

    rig.command.execute();
    rig.swerve.periodic();

    assertEquals(ExecutionOutcome.NONE, rig.adapter.executionOutcome());
    assertFalse(rig.adapter.isFaulted());
    assertFalse(rig.command.isFinished());
    assertEquals(1, rig.swerve.acceptedRequestCount);
    for (RecordingModuleIO module : rig.modules()) {
      assertEquals(1, module.driveRequestCount);
    }
    assertTrue(rig.swerve.getFinalModuleStates()[0].speedMetersPerSecond > 0.0);
  }

  @Test
  void freshExecutionRecoversAfterValidDisabledPreparation() {
    rig = new Rig();
    rig.frontLeft.driveVelocityRotationsPerSecond = Double.NaN;
    rig.swerve.periodic();
    rig.startPath(rig::runHealthyCallbackOrder);
    rig.command.execute();
    rig.endPath(false);
    assertInputUnavailableAndStopped();

    rig.frontLeft.driveVelocityRotationsPerSecond = 0.0;
    rig.prepareKnownPose(Pose2d.kZero);
    Command freshPath = rig.composePath(rig::runHealthyCallbackOrder);
    // Construction/preparation must not clear the failed execution's output barrier.
    rig.acceptOutput(new ChassisSpeeds(1.0, 0.0, 0.0));
    assertEquals(0, rig.swerve.acceptedRequestCount);
    assertEquals(ExecutionOutcome.INPUT_UNAVAILABLE, rig.adapter.executionOutcome());

    rig.startComposedPath(freshPath);
    rig.command.execute();
    rig.swerve.periodic();

    assertEquals(ExecutionOutcome.NONE, rig.adapter.executionOutcome());
    assertFalse(rig.adapter.isFaulted());
    assertFalse(rig.command.isFinished());
    assertEquals(1, rig.swerve.acceptedRequestCount);
    assertEquals(1, rig.frontLeft.driveRequestCount);
  }

  @Test
  void inputUnavailableOutcomeSurvivesModeLossDuringCleanup() {
    rig = new Rig();
    rig.frontLeft.driveVelocityRotationsPerSecond = Double.NaN;
    rig.swerve.periodic();
    rig.startPath(rig::runHealthyCallbackOrder);
    rig.command.execute();

    setAutonomousEnabled(false);
    rig.acceptOutput(new ChassisSpeeds(1.0, 0.0, 0.0));
    rig.endPath(false);

    assertInputUnavailableAndStopped();
  }

  @Test
  void nonfiniteOutputStillLatchesFatalFaultAndFreshExecutionCannotClearIt() {
    rig = new Rig();
    rig.startPath(() -> rig.acceptOutput(new ChassisSpeeds(Double.NaN, 0.0, 0.0)));
    rig.command.execute();

    assertTrue(rig.adapter.isFaulted());
    assertEquals(ExecutionOutcome.FAULTED, rig.adapter.executionOutcome());
    String firstReason = rig.adapter.firstFaultReason();
    assertTrue(firstReason.contains("nonfinite output"));
    rig.endPath(true);
    rig.prepareKnownPose(Pose2d.kZero);
    rig.startPath(rig::runHealthyCallbackOrder);
    rig.command.execute();

    assertEquals(0, rig.swerve.acceptedRequestCount);
    assertTrue(rig.adapter.isFaulted());
    assertEquals(ExecutionOutcome.FAULTED, rig.adapter.executionOutcome());
    assertEquals(firstReason, rig.adapter.firstFaultReason());
  }

  @Test
  void callbackExceptionRetainsThePermanentFatalFaultDistinction() {
    rig = new Rig();
    rig.swerve.speedFailure = new IllegalStateException("speed callback failure");
    rig.startPath(rig::runHealthyCallbackOrder);

    rig.command.execute();

    assertTrue(rig.adapter.isFaulted());
    assertEquals(ExecutionOutcome.FAULTED, rig.adapter.executionOutcome());
    assertTrue(rig.adapter.firstFaultReason().contains("measured-speed callback failed"));
    assertEquals(0, rig.swerve.acceptedRequestCount);
    assertTrue(rig.frontLeft.stopCount > 0);
  }

  private void assertInputUnavailableAndStopped() {
    assertEquals(ExecutionOutcome.INPUT_UNAVAILABLE, rig.adapter.executionOutcome());
    assertFalse(rig.adapter.isFaulted());
    for (SwerveModuleState state : rig.swerve.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
    }
    int requestsBeforePeriodic = rig.totalDriveRequests();
    rig.swerve.periodic();
    assertEquals(requestsBeforePeriodic, rig.totalDriveRequests());
  }

  private static void setAutonomousEnabled(boolean enabled) {
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.setAutonomous(enabled);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  private static RobotConfig createRobotConfig() {
    ModuleConfig module =
        new ModuleConfig(
            Constants.SwerveConstants.kWheelRadiusMeters,
            Constants.PathPlannerLearningConstants.kProvisionalMaxDriveVelocityMetersPerSecond,
            Constants.PathPlannerLearningConstants.kProvisionalWheelCof,
            DCMotor.getKrakenX60(1),
            Constants.SwerveConstants.kDriveGearRatio,
            Constants.SwerveConstants.kDriveSupplyCurrentLimitAmps,
            1);
    double halfWheelbase = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidth = Constants.SwerveConstants.kTrackWidthMeters / 2.0;
    return new RobotConfig(
        Constants.PathPlannerLearningConstants.kProvisionalRobotMassKg,
        Constants.PathPlannerLearningConstants.kProvisionalRobotMoiKgMetersSquared,
        module,
        new Translation2d(halfWheelbase, halfTrackWidth),
        new Translation2d(halfWheelbase, -halfTrackWidth),
        new Translation2d(-halfWheelbase, halfTrackWidth),
        new Translation2d(-halfWheelbase, -halfTrackWidth));
  }

  private static final class Rig {
    private final RecordingModuleIO frontLeft = new RecordingModuleIO();
    private final RecordingModuleIO frontRight = new RecordingModuleIO();
    private final RecordingModuleIO backLeft = new RecordingModuleIO();
    private final RecordingModuleIO backRight = new RecordingModuleIO();
    private final RecordingGyroIO gyro = new RecordingGyroIO();
    private final RecordingSwerveSubsystem swerve =
        new RecordingSwerveSubsystem(frontLeft, frontRight, backLeft, backRight, gyro);
    private final RobotConfig robotConfig = createRobotConfig();
    private final AutoBuilderContractAdapter adapter =
        new AutoBuilderContractAdapter(
            swerve, new PathPlannerTrajectoryAdapter(robotConfig), robotConfig);
    private Command command;
    private CallbackPath followPath;
    private Pose2d lastSuppliedPose;

    private Rig() {
      prepareKnownPose(Pose2d.kZero);
      for (RecordingModuleIO module : modules()) {
        module.stopCount = 0;
      }
    }

    private void prepareKnownPose(Pose2d pose) {
      setAutonomousEnabled(false);
      swerve.periodic();
      assertTrue(swerve.captureFieldHeadingReference());
      // Initialize localization with the captured heading before requesting a known-pose reset.
      swerve.periodic();
      assertTrue(swerve.resetKnownFieldPose(pose));
      setAutonomousEnabled(true);
    }

    private boolean estimatedSampleValid() {
      return swerve.getObservation().orElseThrow().estimatedPose().orElseThrow()
          .measurementSampleValid();
    }

    private Pose2d supplyPose() {
      lastSuppliedPose = (Pose2d) invoke("supplyPose", new Class<?>[0]);
      return lastSuppliedPose;
    }

    private ChassisSpeeds supplySpeeds() {
      return (ChassisSpeeds) invoke("supplyMeasuredRobotRelativeSpeeds", new Class<?>[0]);
    }

    private void acceptOutput(ChassisSpeeds output) {
      invoke("acceptOutput", new Class<?>[] {ChassisSpeeds.class}, output);
    }

    private void runHealthyCallbackOrder() {
      // Mirrors FollowPathCommand 2026.1.2: pose, speeds, then finite target output.
      supplyPose();
      supplySpeeds();
      acceptOutput(new ChassisSpeeds(1.0, 0.0, 0.0));
    }

    private Command composePath(Runnable callbacks) {
      followPath = new CallbackPath(swerve, callbacks);
      return (Command)
          invoke(
              "composeSchedulerOwnedPathCommand",
              new Class<?>[] {Command.class, double.class},
              followPath,
              kPathTimeoutSeconds);
    }

    private void startPath(Runnable callbacks) {
      startComposedPath(composePath(callbacks));
    }

    private void startComposedPath(Command path) {
      command = path;
      assertEquals(1, command.getRequirements().size());
      assertTrue(command.getRequirements().contains(swerve));
      command.initialize();
    }

    private void endPath(boolean interrupted) {
      if (command != null) {
        command.end(interrupted);
        command = null;
      }
    }

    private Object invoke(String name, Class<?>[] parameterTypes, Object... arguments) {
      try {
        Method method = AutoBuilderContractAdapter.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        return method.invoke(adapter, arguments);
      } catch (ReflectiveOperationException failure) {
        throw new AssertionError("adapter callback/lifecycle invocation failed: " + name, failure);
      }
    }

    private RecordingModuleIO[] modules() {
      return new RecordingModuleIO[] {frontLeft, frontRight, backLeft, backRight};
    }

    private int totalDriveRequests() {
      int count = 0;
      for (RecordingModuleIO module : modules()) {
        count += module.driveRequestCount;
      }
      return count;
    }
  }

  private static final class CallbackPath extends Command {
    private final Runnable callbacks;
    private boolean interrupted;
    private int endCount;

    private CallbackPath(SwerveSubsystem swerve, Runnable callbacks) {
      this.callbacks = callbacks;
      addRequirements(swerve);
    }

    @Override
    public void execute() {
      callbacks.run();
    }

    @Override
    public void end(boolean wasInterrupted) {
      interrupted = wasInterrupted;
      endCount++;
    }
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int acceptedRequestCount;
    private RuntimeException speedFailure;

    private RecordingSwerveSubsystem(
        SwerveModuleIO frontLeft,
        SwerveModuleIO frontRight,
        SwerveModuleIO backLeft,
        SwerveModuleIO backRight,
        GyroIO gyro) {
      super(frontLeft, frontRight, backLeft, backRight, gyro);
    }

    @Override
    public void acceptChassisSpeeds(ChassisSpeeds speeds) {
      acceptedRequestCount++;
      super.acceptChassisSpeeds(speeds);
    }

    @Override
    public Optional<ChassisSpeeds> getMeasuredRobotRelativeSpeeds() {
      if (speedFailure != null) {
        throw speedFailure;
      }
      return super.getMeasuredRobotRelativeSpeeds();
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private double driveVelocityRotationsPerSecond;
    private int driveRequestCount;
    private int stopCount;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.drivePositionRotations = 0.0;
      inputs.driveVelocityRotationsPerSecond = driveVelocityRotationsPerSecond;
      inputs.encoderAbsolutePositionRotations = 0.0;
      inputs.driveConnected = true;
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
      driveRequestCount++;
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {
      stopCount++;
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    private boolean connected = true;

    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.yawDegrees = 0.0;
      inputs.connected = connected;
      inputs.configurationHealthy = true;
    }
  }
}
