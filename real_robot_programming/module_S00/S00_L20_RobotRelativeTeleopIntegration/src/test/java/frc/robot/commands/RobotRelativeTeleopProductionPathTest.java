// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you may modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import frc.robot.Constants;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.DriverInputObservation;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the complete robot-relative production path using recording test IO boundaries. */
class RobotRelativeTeleopProductionPathTest {
  private static final double kTolerance = 1.0e-9;
  private static final int kModuleCount = 4;

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

  @Test
  void forwardUsesOneCoherentSampleAndDispatchesPositiveRobotRelativeVx() {
    Rig rig = new Rig(-1.0, 0.0, 0.0);

    rig.executeAndDispatch();

    assertEquals(1, rig.controller.leftYReadCount);
    assertEquals(1, rig.controller.leftXReadCount);
    assertEquals(1, rig.controller.rightXReadCount);
    assertEquals(1, rig.publisher.publishCount);
    assertEquals(1.0, rig.publisher.observation.processedForward(), kTolerance);
    assertEquals(0.0, rig.publisher.observation.processedStrafe(), kTolerance);
    assertEquals(0.0, rig.publisher.observation.processedRotation(), kTolerance);
    assertEquals(1.0, Constants.SwerveConstants.kTeleopMaxTranslationMetersPerSecond);
    assertEquals(1.0, Constants.SwerveConstants.kTeleopMaxAngularSpeedRadiansPerSecond);
    assertDispatchOrderAndPipelineMatch(rig);
    for (RecordingModuleIO module : rig.modules()) {
      assertState(module.lastState(), 1.0, 0.0);
    }
  }

  @Test
  void strafeDispatchesPositiveRobotRelativeVyWithExpectedModuleDirections() {
    Rig rig = new Rig(0.0, -1.0, 0.0);

    rig.executeAndDispatch();

    assertEquals(0.0, rig.publisher.observation.processedForward(), kTolerance);
    assertEquals(1.0, rig.publisher.observation.processedStrafe(), kTolerance);
    assertEquals(0.0, rig.publisher.observation.processedRotation(), kTolerance);
    assertDispatchOrderAndPipelineMatch(rig);
    for (RecordingModuleIO module : rig.modules()) {
      assertState(module.lastState(), 1.0, 90.0);
    }
  }

  @Test
  void rotationDispatchesPositiveOmegaWithFourIdentitySpecificStates() {
    Rig rig = new Rig(0.0, 0.0, -1.0);

    rig.executeAndDispatch();

    assertEquals(0.0, rig.publisher.observation.processedForward(), kTolerance);
    assertEquals(0.0, rig.publisher.observation.processedStrafe(), kTolerance);
    assertEquals(1.0, rig.publisher.observation.processedRotation(), kTolerance);
    assertDispatchOrderAndPipelineMatch(rig);

    double wheelSpeed = Math.abs(rig.frontLeft.lastState().speedMetersPerSecond);
    assertState(rig.frontLeft.lastState(), -wheelSpeed, -45.0);
    assertState(rig.frontRight.lastState(), wheelSpeed, 45.0);
    assertState(rig.backLeft.lastState(), -wheelSpeed, 45.0);
    assertState(rig.backRight.lastState(), wheelSpeed, -45.0);
    assertTrue(wheelSpeed > 0.0);
  }

  @Test
  void combinedForwardAndStrafeDispatchesDiagonalTranslation() {
    Rig rig = new Rig(-1.0, -1.0, 0.0);

    rig.executeAndDispatch();

    assertDispatchOrderAndPipelineMatch(rig);
    for (RecordingModuleIO module : rig.modules()) {
      assertState(module.lastState(), Math.sqrt(2.0), 45.0);
    }
  }

  @Test
  void combinedTranslationAndRotationTraversesTheRealOutputPipeline() {
    Rig rig = new Rig(-1.0, -1.0, -1.0);

    rig.executeAndDispatch();

    assertEquals(1.0, rig.publisher.observation.processedForward(), kTolerance);
    assertEquals(1.0, rig.publisher.observation.processedStrafe(), kTolerance);
    assertEquals(1.0, rig.publisher.observation.processedRotation(), kTolerance);
    assertDispatchOrderAndPipelineMatch(rig);
    assertTrue(
        Math.abs(
                rig.frontLeft.lastState().speedMetersPerSecond
                    - rig.frontRight.lastState().speedMetersPerSecond)
            > kTolerance);
    assertTrue(
        Math.abs(
                rig.frontLeft.lastState().angle.getRadians()
                    - rig.backRight.lastState().angle.getRadians())
            > kTolerance);
  }

  @Test
  void zeroInputDispatchesFourZeroSpeedRequests() {
    Rig rig = new Rig(0.0, 0.0, 0.0);

    rig.executeAndDispatch();

    assertDispatchOrderAndPipelineMatch(rig);
    for (RecordingModuleIO module : rig.modules()) {
      assertState(module.lastState(), 0.0, 0.0);
    }
  }

  @Test
  void disabledModeAcceptsTheSampleButDoesNotDispatchProductionOutputs() {
    Rig rig = new Rig(-1.0, -1.0, -1.0);
    DriverStationSim.setEnabled(false);
    DriverStationSim.notifyNewData();

    rig.command.execute();
    rig.subsystem.periodic();

    assertEquals(1, rig.controller.leftYReadCount);
    assertEquals(1, rig.controller.leftXReadCount);
    assertEquals(1, rig.controller.rightXReadCount);
    assertEquals(1, rig.publisher.publishCount);
    assertEquals(0, rig.dispatchLog.size());
    for (RecordingModuleIO module : rig.modules()) {
      assertEquals(0, module.driveRequestCount);
      assertEquals(0, module.steerRequestCount);
    }
  }

  @Test
  void stopAndInterruptedEndPreventStaleProductionReissue() {
    Rig explicitStopRig = new Rig(-1.0, 0.0, 0.0);
    explicitStopRig.executeAndDispatch();

    explicitStopRig.subsystem.stop();
    explicitStopRig.subsystem.periodic();

    assertStoppedWithoutReissue(explicitStopRig);

    Rig interruptedRig = new Rig(-1.0, -1.0, -1.0);
    interruptedRig.executeAndDispatch();

    interruptedRig.command.end(true);
    interruptedRig.subsystem.periodic();

    assertStoppedWithoutReissue(interruptedRig);
  }

  @Test
  void commissioningDisarmsProductionUntilANewDriverSampleIsAccepted() {
    Rig rig = new Rig(-1.0, 0.0, 0.0);
    rig.executeAndDispatch();
    rig.clearRecordedRequests();

    DriverStationSim.setTest(true);
    DriverStationSim.notifyNewData();
    assertTrue(
        rig.subsystem.startFrontLeftDriveVelocityCommissioning(
            SwerveSubsystem.FrontLeftClosedLoopCommissioningAction.DRIVE_POSITIVE));
    rig.clearRecordedRequests();

    rig.subsystem.periodic();
    rig.subsystem.stopFrontLeftCommissioning();
    rig.clearRecordedRequests();
    rig.subsystem.periodic();

    assertEquals(0, rig.dispatchLog.size());

    rig.command.execute();
    rig.subsystem.periodic();

    assertDispatchOrderAndPipelineMatch(rig);
  }

  @Test
  void gyroHeadingDoesNotCreateAFieldRelativeConversion() {
    Rig zeroYawRig = new Rig(-1.0, 0.0, 0.0, 0.0);
    Rig quarterTurnYawRig = new Rig(-1.0, 0.0, 0.0, 90.0);

    zeroYawRig.executeAndDispatch();
    quarterTurnYawRig.executeAndDispatch();

    RecordingModuleIO[] zeroYawModules = zeroYawRig.modules();
    RecordingModuleIO[] quarterTurnYawModules = quarterTurnYawRig.modules();
    for (int moduleIndex = 0; moduleIndex < kModuleCount; moduleIndex++) {
      assertStateEquals(zeroYawModules[moduleIndex].lastState(), quarterTurnYawModules[moduleIndex].lastState());
    }
  }

  private static void assertDispatchOrderAndPipelineMatch(Rig rig) {
    assertEquals(
        List.of(
            "FL.drive",
            "FL.steer",
            "FR.drive",
            "FR.steer",
            "BL.drive",
            "BL.steer",
            "BR.drive",
            "BR.steer"),
        rig.dispatchLog);

    SwerveModuleState[] finalStates = rig.subsystem.getFinalModuleStates();
    RecordingModuleIO[] modules = rig.modules();
    assertEquals(kModuleCount, finalStates.length);
    for (int moduleIndex = 0; moduleIndex < kModuleCount; moduleIndex++) {
      assertEquals(1, modules[moduleIndex].driveRequestCount);
      assertEquals(1, modules[moduleIndex].steerRequestCount);
      assertStateEquals(finalStates[moduleIndex], modules[moduleIndex].lastState());
    }
  }

  private static void assertStoppedWithoutReissue(Rig rig) {
    assertEquals(kModuleCount * 2, rig.dispatchLog.size());
    for (RecordingModuleIO module : rig.modules()) {
      assertEquals(1, module.driveRequestCount);
      assertEquals(1, module.steerRequestCount);
      assertEquals(1, module.stopCount);
    }
    for (SwerveModuleState state : rig.subsystem.getFinalModuleStates()) {
      assertEquals(0.0, state.speedMetersPerSecond, kTolerance);
    }
  }

  private static void assertState(
      SwerveModuleState state, double expectedSpeedMetersPerSecond, double expectedAngleDegrees) {
    assertEquals(expectedSpeedMetersPerSecond, state.speedMetersPerSecond, kTolerance);
    assertEquals(expectedAngleDegrees, state.angle.getDegrees(), kTolerance);
  }

  private static void assertStateEquals(SwerveModuleState expected, SwerveModuleState actual) {
    assertEquals(expected.speedMetersPerSecond, actual.speedMetersPerSecond, kTolerance);
    assertEquals(expected.angle.getRadians(), actual.angle.getRadians(), kTolerance);
  }

  private static final class Rig {
    private final List<String> dispatchLog = new ArrayList<>();
    private final RecordingXboxController controller;
    private final RecordingModuleIO frontLeft = new RecordingModuleIO("FL", dispatchLog);
    private final RecordingModuleIO frontRight = new RecordingModuleIO("FR", dispatchLog);
    private final RecordingModuleIO backLeft = new RecordingModuleIO("BL", dispatchLog);
    private final RecordingModuleIO backRight = new RecordingModuleIO("BR", dispatchLog);
    private final RecordingPublisher publisher = new RecordingPublisher();
    private final SwerveSubsystem subsystem;
    private final RobotRelativeTeleopDriveCommand command;

    private Rig(double leftY, double leftX, double rightX) {
      this(leftY, leftX, rightX, 0.0);
    }

    private Rig(double leftY, double leftX, double rightX, double yawDegrees) {
      controller = new RecordingXboxController(leftY, leftX, rightX);
      subsystem =
          new SwerveSubsystem(
              frontLeft,
              frontRight,
              backLeft,
              backRight,
              new RecordingGyroIO(yawDegrees));
      command =
          new RobotRelativeTeleopDriveCommand(
              subsystem,
              new XboxDriverInputSource(controller),
              publisher);
    }

    private void executeAndDispatch() {
      command.execute();
      subsystem.periodic();
    }

    private RecordingModuleIO[] modules() {
      return new RecordingModuleIO[] {frontLeft, frontRight, backLeft, backRight};
    }

    private void clearRecordedRequests() {
      dispatchLog.clear();
      for (RecordingModuleIO module : modules()) {
        module.clearRequests();
      }
    }
  }

  private static final class RecordingXboxController extends XboxController {
    private final double leftY;
    private final double leftX;
    private final double rightX;
    private int leftYReadCount;
    private int leftXReadCount;
    private int rightXReadCount;

    private RecordingXboxController(double leftY, double leftX, double rightX) {
      super(Constants.DriverInputConstants.kXboxControllerPort);
      this.leftY = leftY;
      this.leftX = leftX;
      this.rightX = rightX;
    }

    @Override
    public double getLeftY() {
      leftYReadCount++;
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

    @Override
    public void accept(DriverInputObservation observation) {
      publishCount++;
      this.observation = observation;
    }
  }

  private static final class RecordingModuleIO implements SwerveModuleIO {
    private final String name;
    private final List<String> dispatchLog;
    private int driveRequestCount;
    private int steerRequestCount;
    private int stopCount;
    private double lastDriveVelocityMetersPerSecond;
    private Rotation2d lastSteerAngle = new Rotation2d();

    private RecordingModuleIO(String name, List<String> dispatchLog) {
      this.name = name;
      this.dispatchLog = dispatchLog;
    }

    @Override
    public void updateInputs(SwerveModuleIOInputs inputs) {
      inputs.encoderAbsolutePositionRotations = 0.0;
    }

    @Override
    public void setDriveOutput(double output) {}

    @Override
    public void setSteerOutput(double output) {}

    @Override
    public void setDriveVelocityMetersPerSecond(double velocityMetersPerSecond) {
      driveRequestCount++;
      lastDriveVelocityMetersPerSecond = velocityMetersPerSecond;
      dispatchLog.add(name + ".drive");
    }

    @Override
    public void setSteerAngle(Rotation2d angle) {
      steerRequestCount++;
      lastSteerAngle = angle;
      dispatchLog.add(name + ".steer");
    }

    @Override
    public void stop() {
      stopCount++;
    }

    private SwerveModuleState lastState() {
      return new SwerveModuleState(lastDriveVelocityMetersPerSecond, lastSteerAngle);
    }

    private void clearRequests() {
      driveRequestCount = 0;
      steerRequestCount = 0;
      lastDriveVelocityMetersPerSecond = 0.0;
      lastSteerAngle = new Rotation2d();
    }
  }

  private static final class RecordingGyroIO implements GyroIO {
    private final double yawDegrees;

    private RecordingGyroIO(double yawDegrees) {
      this.yawDegrees = yawDegrees;
    }

    @Override
    public void updateInputs(GyroIOInputs inputs) {
      inputs.yawDegrees = yawDegrees;
    }
  }
}
