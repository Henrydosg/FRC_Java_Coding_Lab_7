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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class AutonomousRoutineFactoryTest {
  private static final AutonomousStartContext kContext =
      new AutonomousStartContext(
          FieldVariant.REBUILT_WELDED, Alliance.Blue, new Pose2d(0.0, 0.0, Rotation2d.kZero));

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void nullOrMissingSelectionFailsClosedToFreshSafeStop() {
    Rig rig = new Rig();
    AutonomousRoutineFactory factory = createFactory(rig, context -> Commands.none());

    Command first = factory.create(null, Optional.of(kContext));
    Command second = factory.create(null, Optional.empty());

    assertInstanceOf(AutonomousSafetyHoldCommand.class, first);
    assertInstanceOf(AutonomousSafetyHoldCommand.class, second);
    assertNotSame(first, second);
    assertEquals(1, first.getRequirements().size());
    assertTrue(first.getRequirements().contains(rig.subsystem));
  }

  @Test
  void safeStopDoesNotInvokePathFactoryOrRequestMotion() {
    Rig rig = new Rig();
    AtomicInteger pathFactoryCalls = new AtomicInteger();
    AutonomousRoutineFactory factory =
        createFactory(rig, context -> {
          pathFactoryCalls.incrementAndGet();
          return Commands.runOnce(() -> rig.pathCommandRuns++);
        });

    Command command = factory.create(AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP, Optional.of(kContext));
    command.initialize();
    command.execute();

    assertEquals(0, pathFactoryCalls.get());
    assertEquals(0, rig.pathCommandRuns);
    assertEquals(1, rig.subsystem.stopCount);
    assertFalse(command.isFinished());
  }

  @Test
  void oneMeterPathDelegatesAcceptedContextAndCreatesFreshCommands() {
    Rig rig = new Rig();
    AtomicInteger pathFactoryCalls = new AtomicInteger();
    AutonomousRoutineFactory factory =
        createFactory(
            rig,
            context -> {
              assertEquals(kContext, context);
              pathFactoryCalls.incrementAndGet();
              return Commands.runOnce(() -> rig.pathCommandRuns++, rig.subsystem);
            });

    Command first =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH, Optional.of(kContext));
    Command second =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH, Optional.of(kContext));

    assertNotSame(first, second);
    assertEquals(2, pathFactoryCalls.get());
    assertEquals(0, rig.pathCommandRuns);
    assertTrue(first.getRequirements().contains(rig.subsystem));
  }

  @Test
  void pathConstructionFailureFallsBackToSafeStop() {
    Rig rig = new Rig();
    AutonomousRoutineFactory factory =
        createFactory(rig, context -> { throw new IllegalStateException("path unavailable"); });

    Command command =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH, Optional.of(kContext));

    assertInstanceOf(AutonomousSafetyHoldCommand.class, command);
    command.initialize();
    assertEquals(1, rig.subsystem.stopCount);
  }

  private static AutonomousRoutineFactory createFactory(
      Rig rig, java.util.function.Function<AutonomousStartContext, Command> pathFactory) {
    return new AutonomousRoutineFactory(rig.subsystem, pathFactory, () -> 0.0);
  }

  private static final class Rig {
    private int pathCommandRuns;
    private final RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
  }

  private static final class RecordingSwerveSubsystem extends SwerveSubsystem {
    private int stopCount;

    private RecordingSwerveSubsystem() {
      super(new Module(), new Module(), new Module(), new Module(), new Gyro());
    }

    @Override
    public void stop() {
      stopCount++;
      super.stop();
    }
  }

  private static final class Module implements SwerveModuleIO {
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

  private static final class Gyro implements GyroIO {
    @Override
    public void updateInputs(GyroIOInputs inputs) {}
  }
}
