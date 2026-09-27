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
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants.FieldTransformConstants.FieldVariant;
import frc.robot.commands.AutoBuilderContractAdapter.CommandCreationResult;
import frc.robot.commands.AutoBuilderContractAdapter.CommandCreationStatus;
import frc.robot.commands.AutoBuilderContractAdapter.ExecutionOutcome;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightReason;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightResult;
import frc.robot.commands.AutoBuilderContractAdapter.PreflightStatus;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.subsystems.SwerveSubsystem;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AutonomousRoutineFactoryTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void enableAutonomous() {
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
  }

  @AfterEach
  void resetRuntimeState() {
    CommandScheduler.getInstance().cancelAll();
    CommandScheduler.getInstance().run();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
  }

  @Test
  void nullSelectionFailsClosedToFreshSafeStop() {
    Rig rig = new Rig();
    AutonomousRoutineFactory factory =
        createFactory(rig, context -> created(Commands.runOnce(() -> {}, rig.subsystem)));

    Command first = factory.create(null, Optional.of(Alliance.Blue));
    Command second = factory.create(null, Optional.of(Alliance.Blue));

    assertInstanceOf(AutonomousSafetyHoldCommand.class, first);
    assertInstanceOf(AutonomousSafetyHoldCommand.class, second);
    assertNotSame(first, second);
    assertTrue(first.getRequirements().contains(rig.subsystem));
  }

  @Test
  void safeStopDoesNotConsumeDrivingReadiness() {
    Rig rig = new Rig();
    AtomicInteger pathFactoryCalls = new AtomicInteger();
    AutonomousRoutineFactory factory =
        createFactory(
            rig,
            context -> {
              pathFactoryCalls.incrementAndGet();
              return created(Commands.runOnce(() -> {}, rig.subsystem));
            });
    prepareBlue(rig.coordinator);

    Command command =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP,
            Optional.of(Alliance.Blue));

    assertInstanceOf(AutonomousSafetyHoldCommand.class, command);
    assertFalse(rig.coordinator.getObservation().contextConsumed());
    assertTrue(rig.coordinator.getObservation().state() == State.STALE);
    assertTrue(pathFactoryCalls.get() == 0);
  }

  @Test
  void recoverableConstructionFailureDoesNotBurnValidReadiness() {
    Rig rig = new Rig();
    AutonomousRoutineFactory factory =
        createFactory(
            rig,
            context ->
                new CommandCreationResult(
                    CommandCreationStatus.NOT_READY,
                    Optional.empty(),
                    "temporary input unavailable"));
    prepareBlue(rig.coordinator);

    Command fallback =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));

    assertInstanceOf(AutonomousSafetyHoldCommand.class, fallback);
    assertTrue(rig.coordinator.getObservation().ready());
    assertFalse(rig.coordinator.getObservation().contextConsumed());
  }

  @Test
  void successfulConstructionClaimsExactlyOnceAndNewPrepareCreatesFreshCommand() {
    Rig rig = new Rig();
    AtomicInteger commandRuns = new AtomicInteger();
    AutonomousRoutineFactory factory =
        createFactory(
            rig,
            context ->
                created(
                    Commands.runOnce(commandRuns::incrementAndGet, rig.subsystem)));
    prepareBlue(rig.coordinator);

    Command first =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));
    Command secondWithoutPrepare =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));

    assertTrue(first.getRequirements().contains(rig.subsystem));
    assertInstanceOf(AutonomousSafetyHoldCommand.class, secondWithoutPrepare);
    assertTrue(rig.coordinator.getObservation().contextConsumed());

    prepareBlue(rig.coordinator);
    Command second =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));
    assertNotSame(first, second);
    assertFalse(second instanceof AutonomousSafetyHoldCommand);
  }

  @Test
  void invalidRequirementOwnershipBecomesFatalAndFailsClosed() {
    Rig rig = new Rig();
    AutonomousRoutineFactory factory =
        createFactory(rig, context -> created(Commands.none()));
    prepareBlue(rig.coordinator);

    Command command =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));

    assertInstanceOf(AutonomousSafetyHoldCommand.class, command);
    assertTrue(rig.coordinator.getObservation().adapterFatalFaulted());
    assertTrue(rig.coordinator.getObservation().state() == State.FAULTED);
  }

  @Test
  void oneMeterPathTransitionsToHoldingWithoutDefaultCommandLeak() {
    Rig rig = new Rig();
    AtomicInteger defaultRuns = new AtomicInteger();
    AutonomousRoutineFactory factory =
        createFactory(
            rig,
            context ->
                created(
                    Commands.runOnce(
                        () -> rig.actions.executionOutcome = ExecutionOutcome.COMPLETE,
                        rig.subsystem)));
    Command defaultCommand = Commands.run(defaultRuns::incrementAndGet, rig.subsystem);
    rig.subsystem.setDefaultCommand(defaultCommand);
    prepareBlue(rig.coordinator);
    Command autonomous =
        factory.create(
            AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            Optional.of(Alliance.Blue));
    CommandScheduler scheduler = CommandScheduler.getInstance();

    scheduler.schedule(autonomous);
    scheduler.run();
    scheduler.run();
    scheduler.run();

    assertTrue(autonomous.isScheduled());
    assertEquals(State.HOLDING, rig.coordinator.getObservation().state());
    assertFalse(defaultCommand.isScheduled());
    assertEquals(0, defaultRuns.get());
    assertEquals(1, rig.subsystem.stopCount);

    Command prepare =
        new PrepareAutonomousCommand(
            rig.subsystem,
            rig.coordinator,
            () -> AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
            () -> Optional.of(Alliance.Blue));
    scheduler.schedule(prepare);
    scheduler.run();
    assertTrue(autonomous.isScheduled());
    assertFalse(prepare.isScheduled());

    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    scheduler.run();
    scheduler.run();

    assertFalse(autonomous.isScheduled());
    assertEquals(State.COMPLETE, rig.coordinator.getObservation().state());
    assertTrue(defaultCommand.isScheduled());
    assertTrue(defaultRuns.get() > 0);
  }

  private static AutonomousRoutineFactory createFactory(
      Rig rig, Function<AutonomousStartContext, CommandCreationResult> pathFactory) {
    return new AutonomousRoutineFactory(
        rig.subsystem, pathFactory, rig.coordinator);
  }

  private static CommandCreationResult created(Command command) {
    return new CommandCreationResult(
        CommandCreationStatus.CREATED, Optional.of(command), "");
  }

  private static void prepareBlue(AutonomousPreparationCoordinator coordinator) {
    coordinator.prepare(
        AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
        Optional.of(Alliance.Blue));
  }

  private static final class Rig {
    private final RecordingSwerveSubsystem subsystem = new RecordingSwerveSubsystem();
    private final Actions actions = new Actions();
    private final AutonomousPreparationCoordinator coordinator =
        new AutonomousPreparationCoordinator(
            actions, FieldVariant.REBUILT_WELDED, "test-path");
  }

  private static final class Actions
      implements AutonomousPreparationCoordinator.PreparationActions {
    private boolean faulted;
    private String firstFatalReason = "";
    private ExecutionOutcome executionOutcome = ExecutionOutcome.NONE;

    @Override
    public boolean isDisabled() {
      return true;
    }

    @Override
    public boolean captureFieldHeadingReference() {
      return true;
    }

    @Override
    public Pose2d canonicalStartingPose() {
      return Pose2d.kZero;
    }

    @Override
    public boolean resetKnownFieldPose(Pose2d pose) {
      return true;
    }

    @Override
    public PreflightResult preflight(AutonomousStartContext context) {
      return new PreflightResult(
          PreflightStatus.READY,
          PreflightReason.NONE,
          true,
          0.0,
          0.0,
          true,
          true,
          "");
    }

    @Override
    public boolean isAutoBuilderConfigured() {
      return true;
    }

    @Override
    public boolean isAdapterFaulted() {
      return faulted;
    }

    @Override
    public String firstFatalReason() {
      return firstFatalReason;
    }

    @Override
    public ExecutionOutcome executionOutcome() {
      return faulted ? ExecutionOutcome.FAULTED : executionOutcome;
    }

    @Override
    public void latchStaticPreparationFault(String reason, Throwable failure) {
      if (!faulted) {
        faulted = true;
        firstFatalReason = reason;
      }
    }
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
