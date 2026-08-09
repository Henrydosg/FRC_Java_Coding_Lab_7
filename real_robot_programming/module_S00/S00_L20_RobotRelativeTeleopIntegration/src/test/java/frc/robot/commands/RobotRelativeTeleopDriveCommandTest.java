// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.DriverInputObservation;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RobotRelativeTeleopDriveCommandTest {
  private static final double kTolerance = 1.0e-12;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
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
        rig.subsystem.acceptedSpeeds.vxMetersPerSecond,
        kTolerance);
    assertEquals(
        rig.publisher.observation.processedStrafe()
            * Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond,
        rig.subsystem.acceptedSpeeds.vyMetersPerSecond,
        kTolerance);
    assertEquals(
        rig.publisher.observation.processedRotation()
            * Constants.SwerveConstants.kTeleopMaxAngularSpeedRadiansPerSecond,
        rig.subsystem.acceptedSpeeds.omegaRadiansPerSecond,
        kTolerance);

    rig.command.execute();

    assertEquals(2, rig.controller.leftYReadCount);
    assertEquals(2, rig.controller.leftXReadCount);
    assertEquals(2, rig.controller.rightXReadCount);
    assertEquals(2, rig.subsystem.acceptCount);
    assertEquals(2, rig.publisher.publishCount);
  }

  @Test
  void mapsZeroIntentToZeroRobotRelativeSpeeds() {
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
  void mapsPositiveRotationIntent() {
    assertSpeeds(0.0, 0.0, -1.0, 0.0, 0.0, 1.0);
  }

  @Test
  void mapsCombinedIntentAndPreservesSignsWithExactScaling() {
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

  private static void assertSpeeds(
      double leftY,
      double leftX,
      double rightX,
      double expectedVx,
      double expectedVy,
      double expectedOmega) {
    Rig rig = new Rig(leftY, leftX, rightX);

    rig.command.execute();

    assertEquals(expectedVx, rig.subsystem.acceptedSpeeds.vxMetersPerSecond, kTolerance);
    assertEquals(expectedVy, rig.subsystem.acceptedSpeeds.vyMetersPerSecond, kTolerance);
    assertEquals(
        expectedOmega,
        rig.subsystem.acceptedSpeeds.omegaRadiansPerSecond,
        kTolerance);
  }

  private static final class Rig {
    private final RecordingXboxController controller;
    private final RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    private final RecordingPublisher publisher = new RecordingPublisher();
    private final RobotRelativeTeleopDriveCommand command;

    private Rig(double leftY, double leftX, double rightX) {
      controller = new RecordingXboxController(leftY, leftX, rightX);
      command =
          new RobotRelativeTeleopDriveCommand(
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
    private ChassisSpeeds acceptedSpeeds;
    private boolean throwOnAccept;

    private RecordingSwerveSubsystem() {
      super(
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopModuleIO(),
          new NoopGyroIO());
    }

    @Override
    public void acceptChassisSpeeds(ChassisSpeeds chassisSpeeds) {
      acceptCount++;
      acceptedSpeeds = chassisSpeeds;
      if (throwOnAccept) {
        throw new RuntimeException("simulated chassis-speed submission failure");
      }
    }

    @Override
    public void stop() {
      stopCount++;
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
