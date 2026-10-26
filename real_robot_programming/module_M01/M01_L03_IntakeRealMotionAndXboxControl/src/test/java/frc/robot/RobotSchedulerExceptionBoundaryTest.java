// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.auto.AutonomousPreparationCoordinator;
import frc.robot.commands.VisionFusionCoordinator;
import frc.robot.io.elevator.ElevatorIO;
import frc.robot.io.elevator.ElevatorIONoop;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIONoop;
import frc.robot.io.flywheel.FlywheelIO;
import frc.robot.io.flywheel.FlywheelIONoop;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIONoop;
import frc.robot.io.swerve.SwerveModuleIONoop;
import frc.robot.io.vision.VisionIOSimHarness;
import frc.robot.observation.autonomous.AutonomousPreparationObservation.State;
import frc.robot.observation.intake.IntakeObservation.RequestedState;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class RobotSchedulerExceptionBoundaryTest {
  private static final double TEST_DRIVE_SPEED_METERS_PER_SECOND = 1.0;

  private CommandScheduler scheduler;
  private Robot robot;
  private AutonomousPreparationCoordinator coordinator;
  private SwerveSubsystem swerveSubsystem;

  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @BeforeEach
  void createRobotInAutonomous() throws ReflectiveOperationException {
    AutoBuilder.resetForTesting();
    NamedCommands.clearAll();
    scheduler = CommandScheduler.getInstance();
    recoverSchedulerUsingPublicLifecycle();
    DriverStationSim.resetData();
    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.setTest(false);
    DriverStationSim.notifyNewData();

    robot = new Robot();
    RobotContainer container = readRobotContainer(robot);
    coordinator = readCoordinator(container);
    swerveSubsystem = readSwerveSubsystem(container);
  }

  @AfterEach
  void cleanScheduler() {
    recoverSchedulerUsingPublicLifecycle();
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
  }

  private void recoverSchedulerUsingPublicLifecycle() {
    scheduler.cancelAll();
    scheduler.run();
    scheduler.cancelAll();
  }

  @ParameterizedTest
  @EnumSource(FailurePoint.class)
  void schedulerLifecycleFailureFailsClosedAtRobotBoundary(FailurePoint failurePoint) {
    FailingCommand failingCommand = new FailingCommand(failurePoint);
    swerveSubsystem.acceptChassisSpeeds(
        new ChassisSpeeds(TEST_DRIVE_SPEED_METERS_PER_SECOND, 0.0, 0.0));
    assertTrue(hasNonzeroFinalModuleSpeed());

    if (failurePoint == FailurePoint.INITIALIZE) {
      scheduler.schedule(new ScheduleDuringExecuteCommand(scheduler, failingCommand));
    } else {
      scheduler.schedule(failingCommand);
    }

    assertDoesNotThrow(() -> robot.robotPeriodic());

    assertSwerveStopped();
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertTrue(coordinator.getObservation().adapterFatalFaulted());
    assertTrue(
        coordinator.getObservation().firstFatalReason().contains("scheduler boundary failure"));
    assertTrue(
        coordinator.getObservation().firstFatalReason().contains(failurePoint.name()));

    if (failurePoint == FailurePoint.INITIALIZE) {
      String firstFatalReason = coordinator.getObservation().firstFatalReason();
      assertDoesNotThrow(() -> robot.autonomousInit());
      assertDoesNotThrow(() -> robot.robotPeriodic());
      assertEquals(State.FAULTED, coordinator.getObservation().state());
      assertEquals(firstFatalReason, coordinator.getObservation().firstFatalReason());
    }
  }

  @Test
  void directScheduleInitializeFailurePropagatesBeforeRobotPeriodic() {
    IllegalStateException failure =
        assertThrows(
            IllegalStateException.class,
            () -> scheduler.schedule(new FailingCommand(FailurePoint.INITIALIZE)));

    assertEquals("scheduler-INITIALIZE", failure.getMessage());
    assertEquals(State.UNPREPARED, coordinator.getObservation().state());
  }

  @Test
  void fatalSchedulerStateRejectsASecondAutonomousRestart() {
    scheduler.schedule(new FailingCommand(FailurePoint.EXECUTE));
    assertDoesNotThrow(() -> robot.robotPeriodic());
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    String firstFatalReason = coordinator.getObservation().firstFatalReason();

    assertDoesNotThrow(() -> robot.autonomousInit());
    assertDoesNotThrow(() -> robot.robotPeriodic());
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertEquals(firstFatalReason, coordinator.getObservation().firstFatalReason());
  }

  @Test
  void schedulerFailureSkipsFusionAndStillPublishesTelemetryInFinally()
      throws ReflectiveOperationException {
    RobotContainer container = readRobotContainer(robot);
    VisionSubsystem visionSubsystem = readVisionSubsystem(container);
    VisionIOSimHarness visionHarness = readVisionIOSimHarness(container);
    initializeSwerveObservationForTelemetry();

    visionHarness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    visionSubsystem.periodic();
    assertTrue(visionSubsystem.getLatestQualifiedMeasurement().isPresent());
    visionHarness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_B);

    var poseAvailableEntry =
        NetworkTableInstance.getDefault()
            .getTable("Swerve")
            .getSubTable("Pose")
            .getBooleanTopic("Available")
            .getEntry(false);
    poseAvailableEntry.set(false);

    scheduler.schedule(new FailingCommand(FailurePoint.EXECUTE));
    assertDoesNotThrow(() -> robot.robotPeriodic());

    assertEquals(0, swerveSubsystem.getVisionFusionObservation().qualifiedHandoffCount());
    assertEquals(0, swerveSubsystem.getVisionFusionObservation().acceptedFusionCount());
    assertTrue(poseAvailableEntry.get());
    assertEquals(State.FAULTED, coordinator.getObservation().state());
  }

  @Test
  void coordinatorFailureUsesRobotFailClosedBoundary() throws ReflectiveOperationException {
    RobotContainer container = readRobotContainer(robot);
    VisionSubsystem visionSubsystem = readVisionSubsystem(container);
    VisionIOSimHarness visionHarness = readVisionIOSimHarness(container);
    visionHarness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
    visionSubsystem.periodic();
    assertTrue(visionSubsystem.getLatestQualifiedMeasurement().isPresent());
    visionHarness.apply(VisionIOSimHarness.FixtureSelection.VALID_FRAME_B);

    swerveSubsystem.acceptChassisSpeeds(
        new ChassisSpeeds(TEST_DRIVE_SPEED_METERS_PER_SECOND, 0.0, 0.0));
    assertTrue(hasNonzeroFinalModuleSpeed());
    replaceCoordinatorSwerve(
        container.getVisionFusionCoordinator(), new ThrowingAdmissionSwerveSubsystem());

    assertDoesNotThrow(() -> robot.robotPeriodic());

    assertSwerveStopped();
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertTrue(coordinator.getObservation().adapterFatalFaulted());
    assertTrue(
        coordinator.getObservation().firstFatalReason().contains("scheduler boundary failure"));
  }

  @Test
  void schedulerFatalStopsEveryMechanismAndLatchesIntake() throws ReflectiveOperationException {
    RobotContainer container = readRobotContainer(robot);
    MechanismStopRecorder recorder = installMechanismStopRecorder(container);
    IntakeSubsystem intake = readField(container, "intakeSubsystem", IntakeSubsystem.class);
    swerveSubsystem.acceptChassisSpeeds(
        new ChassisSpeeds(TEST_DRIVE_SPEED_METERS_PER_SECOND, 0.0, 0.0));
    assertTrue(hasNonzeroFinalModuleSpeed());

    scheduler.schedule(new FailingCommand(FailurePoint.EXECUTE));
    assertDoesNotThrow(() -> robot.robotPeriodic());

    assertSwerveStopped();
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertEquals(List.of("intake", "feeder", "flywheel", "elevator"), recorder.stops);
    assertTrue(intake.getObservation().faultLatched());

    intake.requestIntake();
    assertEquals(RequestedState.STOPPED, intake.getObservation().requestedState());
    assertEquals(0, recorder.intakeRequests);
  }

  @Test
  void oneFailingMechanismStopDoesNotSkipTheOthers() throws ReflectiveOperationException {
    RobotContainer container = readRobotContainer(robot);
    MechanismStopRecorder recorder = installMechanismStopRecorder(container);
    recorder.failingStops.add("feeder");
    recorder.failingStops.add("flywheel");
    swerveSubsystem.acceptChassisSpeeds(
        new ChassisSpeeds(TEST_DRIVE_SPEED_METERS_PER_SECOND, 0.0, 0.0));

    assertDoesNotThrow(
        () -> container.handleSchedulerRuntimeException(new IllegalStateException("fatal")));

    assertSwerveStopped();
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    assertEquals(List.of("intake", "feeder", "flywheel", "elevator"), recorder.stops);
  }

  @Test
  void repeatedFatalNotificationsRemainSafeAndKeepIntakeLatched()
      throws ReflectiveOperationException {
    RobotContainer container = readRobotContainer(robot);
    MechanismStopRecorder recorder = installMechanismStopRecorder(container);
    recorder.failingStops.add("intake");
    recorder.failingStops.add("elevator");
    IntakeSubsystem intake = readField(container, "intakeSubsystem", IntakeSubsystem.class);

    for (int notification = 0; notification < 3; notification++) {
      assertDoesNotThrow(
          () -> container.handleSchedulerRuntimeException(new IllegalStateException("fatal")));
    }
    assertDoesNotThrow(() -> robot.robotPeriodic());

    assertEquals(3, recorder.stops.stream().filter("elevator"::equals).count());
    assertEquals(3, recorder.stops.stream().filter("intake"::equals).count());
    assertTrue(intake.getObservation().faultLatched());
    assertEquals(State.FAULTED, coordinator.getObservation().state());
    intake.requestIntake();
    assertEquals(0, recorder.intakeRequests);
    assertFalse(intake.getObservation().requestedState() == RequestedState.INTAKE_REQUESTED);
  }

  private static MechanismStopRecorder installMechanismStopRecorder(RobotContainer container)
      throws ReflectiveOperationException {
    MechanismStopRecorder recorder = new MechanismStopRecorder();
    replaceField(
        readField(container, "intakeSubsystem", IntakeSubsystem.class),
        "intakeIO",
        recorder.intakeIO());
    replaceField(
        readField(container, "feederSubsystem", FeederSubsystem.class),
        "feederIO",
        recorder.feederIO());
    replaceField(
        readField(container, "flywheelSubsystem", FlywheelSubsystem.class),
        "flywheelIO",
        recorder.flywheelIO());
    replaceField(
        readField(container, "elevatorSubsystem", ElevatorSubsystem.class),
        "elevatorIO",
        recorder.elevatorIO());
    return recorder;
  }

  private static <T> T readField(Object owner, String name, Class<T> type)
      throws ReflectiveOperationException {
    Field field = owner.getClass().getDeclaredField(name);
    field.setAccessible(true);
    return type.cast(field.get(owner));
  }

  private static void replaceField(Object owner, String name, Object replacement)
      throws ReflectiveOperationException {
    Field field = owner.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.set(owner, replacement);
  }

  /** Records mechanism stop order and optionally fails selected stops; inputs stay Noop. */
  private static final class MechanismStopRecorder {
    private final List<String> stops = new ArrayList<>();
    private final List<String> failingStops = new ArrayList<>();
    private int intakeRequests;

    private void recordStop(String mechanism) {
      stops.add(mechanism);
      if (failingStops.contains(mechanism)) {
        throw new IllegalStateException(mechanism + " stop failed");
      }
    }

    private IntakeIO intakeIO() {
      IntakeIONoop noop = new IntakeIONoop();
      return new IntakeIO() {
        @Override
        public void updateInputs(IntakeIOInputs inputs) {
          noop.updateInputs(inputs);
        }

        @Override
        public void requestIntake() {
          intakeRequests++;
        }

        @Override
        public void stop() {
          recordStop("intake");
        }
      };
    }

    private FeederIO feederIO() {
      FeederIONoop noop = new FeederIONoop();
      return new FeederIO() {
        @Override
        public void updateInputs(FeederIOInputs inputs) {
          noop.updateInputs(inputs);
        }

        @Override
        public void requestFeed() {}

        @Override
        public void stop() {
          recordStop("feeder");
        }
      };
    }

    private FlywheelIO flywheelIO() {
      FlywheelIONoop noop = new FlywheelIONoop();
      return new FlywheelIO() {
        @Override
        public void updateInputs(FlywheelIOInputs inputs) {
          noop.updateInputs(inputs);
        }

        @Override
        public void requestVelocity(double targetRpm) {}

        @Override
        public void stop() {
          recordStop("flywheel");
        }
      };
    }

    private ElevatorIO elevatorIO() {
      ElevatorIONoop noop = new ElevatorIONoop();
      return new ElevatorIO() {
        @Override
        public void updateInputs(ElevatorIOInputs inputs) {
          noop.updateInputs(inputs);
        }

        @Override
        public void requestPositionMeters(double targetPositionMeters) {}

        @Override
        public void requestHoming() {}

        @Override
        public void stop() {
          recordStop("elevator");
        }
      };
    }
  }

  private static RobotContainer readRobotContainer(Robot robot)
      throws ReflectiveOperationException {
    Field field = Robot.class.getDeclaredField("m_robotContainer");
    field.setAccessible(true);
    return (RobotContainer) field.get(robot);
  }

  private static AutonomousPreparationCoordinator readCoordinator(RobotContainer container)
      throws ReflectiveOperationException {
    Field field = RobotContainer.class.getDeclaredField("autonomousPreparationCoordinator");
    field.setAccessible(true);
    return (AutonomousPreparationCoordinator) field.get(container);
  }

  private static SwerveSubsystem readSwerveSubsystem(RobotContainer container)
      throws ReflectiveOperationException {
    Field field = RobotContainer.class.getDeclaredField("swerveSubsystem");
    field.setAccessible(true);
    return (SwerveSubsystem) field.get(container);
  }

  private static VisionSubsystem readVisionSubsystem(RobotContainer container)
      throws ReflectiveOperationException {
    Field field = RobotContainer.class.getDeclaredField("visionSubsystem");
    field.setAccessible(true);
    return (VisionSubsystem) field.get(container);
  }

  @SuppressWarnings("unchecked")
  private static VisionIOSimHarness readVisionIOSimHarness(RobotContainer container)
      throws ReflectiveOperationException {
    Field field = RobotContainer.class.getDeclaredField("visionIOSimHarness");
    field.setAccessible(true);
    Optional<VisionIOSimHarness> harness = (Optional<VisionIOSimHarness>) field.get(container);
    return harness.orElseThrow(() -> new AssertionError("simulation vision harness is absent"));
  }

  private static void replaceCoordinatorSwerve(
      VisionFusionCoordinator coordinator, SwerveSubsystem replacement)
      throws ReflectiveOperationException {
    Field field = VisionFusionCoordinator.class.getDeclaredField("swerveSubsystem");
    field.setAccessible(true);
    field.set(coordinator, replacement);
  }

  private void initializeSwerveObservationForTelemetry() {
    DriverStationSim.setEnabled(false);
    DriverStationSim.setAutonomous(false);
    DriverStationSim.notifyNewData();
    swerveSubsystem.periodic();
    assertTrue(swerveSubsystem.captureFieldHeadingReference());
    swerveSubsystem.periodic();

    DriverStationSim.setEnabled(true);
    DriverStationSim.setAutonomous(true);
    DriverStationSim.notifyNewData();
  }

  private boolean hasNonzeroFinalModuleSpeed() {
    for (var moduleState : swerveSubsystem.getFinalModuleStates()) {
      if (moduleState.speedMetersPerSecond != 0.0) {
        return true;
      }
    }
    return false;
  }

  private void assertSwerveStopped() {
    for (var moduleState : swerveSubsystem.getFinalModuleStates()) {
      assertEquals(0.0, moduleState.speedMetersPerSecond);
    }
  }

  private enum FailurePoint {
    INITIALIZE,
    EXECUTE,
    IS_FINISHED,
    END
  }

  private static final class ThrowingAdmissionSwerveSubsystem extends SwerveSubsystem {
    private ThrowingAdmissionSwerveSubsystem() {
      super(
          new SwerveModuleIONoop(),
          new SwerveModuleIONoop(),
          new SwerveModuleIONoop(),
          new SwerveModuleIONoop(),
          new GyroIO() {
            @Override
            public void updateInputs(GyroIOInputs inputs) {}
          });
    }

    @Override
    public boolean admitVisionMeasurement(
        frc.robot.observation.vision.QualifiedVisionMeasurement measurement) {
      throw new IllegalStateException("coordinator admission failure");
    }
  }

  private static final class ScheduleDuringExecuteCommand extends Command {
    private final CommandScheduler scheduler;
    private final Command commandToSchedule;
    private boolean scheduleRequested;

    private ScheduleDuringExecuteCommand(
        CommandScheduler scheduler, Command commandToSchedule) {
      this.scheduler = scheduler;
      this.commandToSchedule = commandToSchedule;
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
      if (!scheduleRequested) {
        scheduleRequested = true;
        scheduler.schedule(commandToSchedule);
      }
    }

    @Override
    public boolean isFinished() {
      return scheduleRequested;
    }

    @Override
    public void end(boolean interrupted) {}
  }

  private static final class FailingCommand extends Command {
    private final FailurePoint failurePoint;
    private boolean failureThrown;

    private FailingCommand(FailurePoint failurePoint) {
      this.failurePoint = failurePoint;
    }

    @Override
    public void initialize() {
      failIf(FailurePoint.INITIALIZE);
    }

    @Override
    public void execute() {
      failIf(FailurePoint.EXECUTE);
    }

    @Override
    public boolean isFinished() {
      failIf(FailurePoint.IS_FINISHED);
      return failurePoint == FailurePoint.END;
    }

    @Override
    public void end(boolean interrupted) {
      failIf(FailurePoint.END);
    }

    private void failIf(FailurePoint point) {
      if (failurePoint == point && !failureThrown) {
        failureThrown = true;
        throw new IllegalStateException("scheduler-" + point.name());
      }
    }
  }
}
