// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.DriverInputObservation;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.function.Consumer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldRelativeTeleopDriveCommandTest {
  private static final double kTolerance = 1.0e-12;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableTeleop() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @AfterEach
  void disableRobot() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void requiresSwerveAndNeverFinishes() {
    Rig rig = new Rig(0.0, 0.0, 0.0);

    assertTrue(rig.command.getRequirements().contains(rig.subsystem));
    assertFalse(rig.command.isFinished());
  }

  @Test
  void readsExactlyOneCoherentSamplePerExecuteAndPublishesTheSampleUsedForControl() {
    Rig rig = new Rig(0.40, -0.30, 0.55);

    rig.command.execute();

    assertEquals(1, rig.controller.leftYReadCount);
    assertEquals(1, rig.controller.leftXReadCount);
    assertEquals(1, rig.controller.rightXReadCount);
    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(1, rig.publisher.publishCount);
    assertEquals(
        rig.publisher.observation.processedForward()
            * Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond,
        rig.subsystem.acceptedFieldRelativeSpeeds.vxMetersPerSecond,
        kTolerance);
    assertEquals(
        rig.publisher.observation.processedStrafe()
            * Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond,
        rig.subsystem.acceptedFieldRelativeSpeeds.vyMetersPerSecond,
        kTolerance);
    assertEquals(
        rig.publisher.observation.processedRotation()
            * Constants.SwerveConstants.kTeleopMaxAngularSpeedRadiansPerSecond,
        rig.subsystem.acceptedFieldRelativeSpeeds.omegaRadiansPerSecond,
        kTolerance);

    rig.command.execute();

    assertEquals(2, rig.controller.leftYReadCount);
    assertEquals(2, rig.controller.leftXReadCount);
    assertEquals(2, rig.controller.rightXReadCount);
    assertEquals(2, rig.subsystem.acceptCount);
    assertEquals(2, rig.publisher.publishCount);
  }

  @Test
  void mapsZeroIntentToZeroFieldRelativeSpeeds() {
    assertSpeeds(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
  }

  @Test
  void mapsPositiveForwardIntent() {
    assertSpeeds(-1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
  }

  @Test
  void mapsPositiveStrafeIntent() {
    assertSpeeds(0.0, -1.0, 0.0, 0.0, 1.0, 0.0);
  }

  @Test
  void mapsPositiveRotationIntentWithoutChangingItsSign() {
    assertSpeeds(0.0, 0.0, -1.0, 0.0, 0.0, 1.0);
  }

  @Test
  void mapsCombinedIntentAndPreservesExistingScaling() {
    assertEquals(1.0, Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond);
    assertEquals(1.0, Constants.SwerveConstants.kTeleopMaxAngularSpeedRadiansPerSecond);
    assertSpeeds(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
    assertSpeeds(1.0, -1.0, 1.0, -1.0, 1.0, -1.0);
  }

  @Test
  void normalAndInterruptedEndAlwaysStop() {
    Rig normalRig = new Rig(0.0, 0.0, 0.0);
    Rig interruptedRig = new Rig(0.0, 0.0, 0.0);

    normalRig.command.end(false);
    interruptedRig.command.end(true);

    assertEquals(1, normalRig.subsystem.stopCount);
    assertEquals(1, interruptedRig.subsystem.stopCount);
  }

  @Test
  void outsideTeleopStopsWithoutReadingPublishingOrSubmitting() {
    assertModeGate(false, false, false);
    assertModeGate(true, true, false);
    assertModeGate(true, false, true);
    assertModeGate(true, true, false, 0.0, 0.0, 0.0);
  }

  @Test
  void acquisitionFailureStopsBeforeRethrow() {
    Rig rig = new Rig(0.0, 0.0, 0.0);
    rig.controller.throwOnRead = true;

    assertThrows(RuntimeException.class, rig.command::execute);

    assertEquals(1, rig.subsystem.stopCount);
    assertEquals(0, rig.subsystem.acceptCount);
    assertEquals(0, rig.publisher.publishCount);
  }

  @Test
  void submissionFailureStopsBeforeRethrow() {
    Rig rig = new Rig(0.0, 0.0, 0.0);
    rig.subsystem.throwOnAccept = true;

    assertThrows(RuntimeException.class, rig.command::execute);

    assertEquals(1, rig.subsystem.stopCount);
    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(0, rig.publisher.publishCount);
  }

  @Test
  void telemetryFailureStopsBeforeRethrow() {
    Rig rig = new Rig(0.0, 0.0, 0.0);
    rig.publisher.throwOnPublish = true;

    assertThrows(RuntimeException.class, rig.command::execute);

    assertEquals(1, rig.subsystem.stopCount);
    assertEquals(1, rig.subsystem.acceptCount);
    assertEquals(1, rig.publisher.publishCount);
  }

  @Test
  void throwingSwerveStopDoesNotEscapeNormalOrInterruptedEnd() {
    Rig rig = new Rig(0.0, 0.0, 0.0);
    rig.subsystem.throwOnStop = true;

    assertDoesNotThrow(() -> rig.command.end(false));
    assertDoesNotThrow(() -> rig.command.end(true));

    assertEquals(2, rig.subsystem.stopCount);
  }

  @Test
  void cleanupStopFailureInhibitsLaterDriverIntentAndOnlyRetriesStop() {
    Rig rig = new Rig(0.40, -0.30, 0.55);
    rig.subsystem.throwOnStop = true;
    rig.command.end(true);
    rig.subsystem.throwOnStop = false;

    assertDoesNotThrow(rig.command::execute);
    assertDoesNotThrow(rig.command::execute);

    assertEquals(0, rig.subsystem.acceptCount);
    assertEquals(0, rig.controller.leftYReadCount);
    assertEquals(0, rig.publisher.publishCount);
    assertEquals(3, rig.subsystem.stopCount);
  }

  @Test
  void realSwerveStopStillAttemptsEveryModuleWhenOneModuleStopFails() {
    CountingModuleIO frontLeft = new CountingModuleIO();
    CountingModuleIO frontRight = new CountingModuleIO();
    CountingModuleIO backLeft = new CountingModuleIO();
    CountingModuleIO backRight = new CountingModuleIO();
    frontRight.throwOnStop = true;
    SwerveSubsystem swerve =
        new SwerveSubsystem(frontLeft, frontRight, backLeft, backRight, new NoopGyroIO());
    FieldRelativeTeleopDriveCommand command =
        new FieldRelativeTeleopDriveCommand(
            swerve,
            new XboxDriverInputSource(new RecordingXboxController(0.0, 0.0, 0.0)),
            new RecordingPublisher());
    try {
      assertDoesNotThrow(() -> command.end(true));

      assertEquals(1, frontLeft.stopCount);
      assertEquals(1, frontRight.stopCount);
      assertEquals(1, backLeft.stopCount);
      assertEquals(1, backRight.stopCount);
    } finally {
      frontRight.throwOnStop = false;
      CommandScheduler.getInstance().unregisterSubsystem(swerve);
    }
  }

  @Test
  void schedulerCancellationCompletesAfterStopFailureAndReleasesRequirement() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Rig rig = new Rig(0.0, 0.0, 0.0);
    try {
      scheduler.schedule(rig.command);
      scheduler.run();
      rig.subsystem.throwOnStop = true;

      assertDoesNotThrow(() -> scheduler.cancel(rig.command));
      assertFalse(rig.command.isScheduled());

      Command competitor = Commands.idle(rig.subsystem);
      scheduler.schedule(competitor);
      assertTrue(competitor.isScheduled());
    } finally {
      rig.subsystem.throwOnStop = false;
      scheduler.cancelAll();
      scheduler.unregisterSubsystem(rig.subsystem);
    }
  }

  @Test
  void disabledCancellationWithFailingStopIsSafe() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Rig rig = new Rig(0.0, 0.0, 0.0);
    try {
      scheduler.schedule(rig.command);
      scheduler.run();
      rig.subsystem.throwOnStop = true;
      DriverStationSim.setEnabled(false);
      DriverStationSim.notifyNewData();

      assertDoesNotThrow(scheduler::run);

      assertFalse(rig.command.isScheduled());
      assertTrue(rig.subsystem.stopCount >= 1);
    } finally {
      rig.subsystem.throwOnStop = false;
      scheduler.cancelAll();
      scheduler.unregisterSubsystem(rig.subsystem);
    }
  }

  @Test
  void defaultCommandDoesNotReArmDriveOrLoopAfterCleanupFailure() {
    CommandScheduler scheduler = CommandScheduler.getInstance();
    Rig rig = new Rig(0.40, -0.30, 0.55);
    try {
      rig.subsystem.setDefaultCommand(rig.command);
      scheduler.run();
      scheduler.run();
      assertTrue(rig.command.isScheduled());
      assertEquals(1, rig.subsystem.acceptCount);

      rig.subsystem.throwOnStop = true;
      assertDoesNotThrow(() -> scheduler.cancel(rig.command));
      for (int cycle = 0; cycle < 3; cycle++) {
        assertDoesNotThrow(scheduler::run);
      }

      assertTrue(rig.command.isScheduled());
      assertEquals(1, rig.subsystem.acceptCount);
      // One failed cleanup stop plus one retried stop per rescheduled execution; never a drive.
      assertTrue(rig.subsystem.stopCount >= 3);
    } finally {
      rig.subsystem.throwOnStop = false;
      scheduler.cancelAll();
      scheduler.unregisterSubsystem(rig.subsystem);
    }
  }

  private static void assertSpeeds(
      double leftY,
      double leftX,
      double rightX,
      double expectedVx,
      double expectedVy,
      double expectedOmega) {
    Rig rig = new Rig(leftY, leftX, rightX);

    rig.command.execute();

    assertEquals(
        expectedVx, rig.subsystem.acceptedFieldRelativeSpeeds.vxMetersPerSecond, kTolerance);
    assertEquals(
        expectedVy, rig.subsystem.acceptedFieldRelativeSpeeds.vyMetersPerSecond, kTolerance);
    assertEquals(
        expectedOmega,
        rig.subsystem.acceptedFieldRelativeSpeeds.omegaRadiansPerSecond,
        kTolerance);
  }

  private static void assertModeGate(boolean enabled, boolean autonomous, boolean test) {
    assertModeGate(enabled, autonomous, test, -1.0, -1.0, -1.0);
  }

  private static void assertModeGate(
      boolean enabled,
      boolean autonomous,
      boolean test,
      double leftY,
      double leftX,
      double rightX) {
    DriverStationSim.setEnabled(enabled);
    DriverStationSim.setAutonomous(autonomous);
    DriverStationSim.setTest(test);
    DriverStationSim.notifyNewData();
    Rig rig = new Rig(leftY, leftX, rightX);

    rig.command.execute();

    assertEquals(1, rig.subsystem.stopCount);
    assertEquals(0, rig.controller.leftYReadCount);
    assertEquals(0, rig.controller.leftXReadCount);
    assertEquals(0, rig.controller.rightXReadCount);
    assertEquals(0, rig.subsystem.acceptCount);
    assertEquals(0, rig.publisher.publishCount);
  }

  private static final class Rig {
    private final RecordingXboxController controller;
    private final RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    private final RecordingPublisher publisher = new RecordingPublisher();
    private final FieldRelativeTeleopDriveCommand command;

    private Rig(double leftY, double leftX, double rightX) {
      controller = new RecordingXboxController(leftY, leftX, rightX);
      command =
          new FieldRelativeTeleopDriveCommand(
              subsystem,
              new XboxDriverInputSource(controller),
              publisher);
    }
  }

  private static final class RecordingXboxController extends XboxController {
    private final double leftY;
    private final double leftX;
    private final double rightX;
    private int leftYReadCount;
    private int leftXReadCount;
    private int rightXReadCount;
    private boolean throwOnRead;

    private RecordingXboxController(double leftY, double leftX, double rightX) {
      super(Constants.DriverInputConstants.kXboxControllerPort);
      this.leftY = leftY;
      this.leftX = leftX;
      this.rightX = rightX;
    }

    @Override
    public double getLeftY() {
      leftYReadCount++;
      if (throwOnRead) {
        throw new RuntimeException("simulated driver-input acquisition failure");
      }
      return leftY;
    }

    @Override
    public double getLeftX() {
      leftXReadCount++;
      return leftX;
    }

    @Override
    public double getRightX() {
      rightXReadCount++;
      return rightX;
    }
  }

  private static final class RecordingPublisher implements Consumer<DriverInputObservation> {
    private int publishCount;
    private DriverInputObservation observation;
    private boolean throwOnPublish;

    @Override
    public void accept(DriverInputObservation observation) {
      publishCount++;
      this.observation = observation;
      if (throwOnPublish) {
        throw new RuntimeException("simulated driver-input telemetry failure");
      }
    }
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int acceptCount;
    private int stopCount;
    private ChassisSpeeds acceptedFieldRelativeSpeeds;
    private boolean throwOnAccept;
    private boolean throwOnStop;

    private RecordingSwerveSubsystem() {
      super(
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopGyroIO());
    }

    @Override
    public void acceptFieldRelativeChassisSpeeds(ChassisSpeeds fieldRelativeSpeeds) {
      acceptCount++;
      acceptedFieldRelativeSpeeds = fieldRelativeSpeeds;
      if (throwOnAccept) {
        throw new RuntimeException("simulated field-relative submission failure");
      }
    }

    @Override
    public void stop() {
      stopCount++;
      if (throwOnStop) {
        throw new RuntimeException("simulated Swerve stop failure");
      }
    }
  }

  private static final class CountingModuleIO implements SwerveModuleIO {
    private int stopCount;
    private boolean throwOnStop;

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {}

    @Override
    public void setSteerAngle(Rotation2d angle) {}

    @Override
    public void stop() {
      stopCount++;
      if (throwOnStop) {
        throw new RuntimeException("simulated module stop failure");
      }
    }
  }

  private static final class NoopModuleIO implements SwerveModuleIO {
    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {}

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

  private static final class NoopGyroIO implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
