// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.autonomous.AutonomousEventId;
import frc.robot.commands.AutonomousPreparationCoordinator;
import frc.robot.commands.AutonomousEventBinding;
import frc.robot.commands.AutonomousEventDemonstrationCommand;
import frc.robot.commands.AutonomousEventRegistration;
import frc.robot.commands.AutonomousRoutineFactory;
import frc.robot.commands.AutoBuilderContractAdapter;
import frc.robot.commands.DriveThreeMeterValidationDashboard;
import frc.robot.commands.FieldRelativeTeleopDriveCommand;
import frc.robot.commands.PathPlannerTrajectoryAdapter;
import frc.robot.commands.PrepareAutonomousCommand;
import frc.robot.commands.RunFeederCommand;
import frc.robot.commands.RunIntakeCommand;
import frc.robot.commands.SwerveFourModuleTestDashboard;
import frc.robot.commands.SwerveFrontLeftCommissioningDashboard;
import frc.robot.commands.VisionFusionCoordinator;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.gyro.GyroIOSim;
import frc.robot.io.gyro.GyroIOPigeon2;
import frc.robot.io.feeder.FeederIONoop;
import frc.robot.io.flywheel.FlywheelIONoop;
import frc.robot.io.intake.IntakeIONoop;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.io.swerve.SwerveModuleIOCTRE;
import frc.robot.io.swerve.SwerveModuleIOSim;
import frc.robot.io.vision.VisionIO;
import frc.robot.io.vision.VisionIOLimelight;
import frc.robot.io.vision.VisionIOSim;
import frc.robot.io.vision.VisionIOSimHarness;
import frc.robot.subsystems.SwerveKinematics;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.subsystems.FlywheelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.autonomous.AutonomousEventTelemetryFacade;
import frc.robot.telemetry.autonomous.AutonomousPreparationTelemetryFacade;
import frc.robot.telemetry.driver.DriverInputTelemetryFacade;
import frc.robot.telemetry.feeder.FeederTelemetryFacade;
import frc.robot.telemetry.flywheel.FlywheelTelemetryFacade;
import frc.robot.telemetry.intake.IntakeTelemetryFacade;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;
import frc.robot.telemetry.validation.DriveThreeMeterValidationTelemetryFacade;
import frc.robot.telemetry.vision.VisionTelemetryFacade;
import frc.robot.vision.AprilTagFieldLayoutContract;
import frc.robot.observation.vision.VisionMeasurementQualityEvaluator.Policy;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Creates robot components and configures command bindings.
 */
public class RobotContainer {
  private final SwerveSubsystem swerveSubsystem;
  private final VisionIO visionIO;
  private final Optional<SendableChooser<VisionIOSimHarness.FixtureSelection>>
      simulationFixtureChooser;
  private final Optional<VisionIOSimHarness> visionIOSimHarness;
  private final VisionSubsystem visionSubsystem;
  private final IntakeSubsystem intakeSubsystem;
  private final FeederSubsystem feederSubsystem;
  private final FlywheelSubsystem flywheelSubsystem;
  private final VisionFusionCoordinator visionFusionCoordinator;
  private final SwerveFrontLeftCommissioningDashboard commissioningDashboard;
  private final SwerveFourModuleTestDashboard fourModuleTestDashboard;
  private final DriveThreeMeterValidationDashboard driveThreeMeterValidationDashboard;
  private final PathPlannerTrajectoryAdapter pathPlannerTrajectoryAdapter;
  private final AutoBuilderContractAdapter autoBuilderContractAdapter;
  private final AutonomousPreparationCoordinator autonomousPreparationCoordinator;
  private final PrepareAutonomousCommand prepareAutonomousCommand;
  private final AutonomousRoutineFactory autonomousRoutineFactory;
  private final SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId>
      autonomousRoutineChooser;
  private final RobotTelemetry robotTelemetry;

  /**
   * Creates the composition root.
   */
  public RobotContainer() {
    AprilTagFieldLayoutContract fieldLayout =
        AprilTagFieldLayoutContract.loadOfficial2026(
            Constants.FieldTransformConstants.FieldVariant.REBUILT_WELDED);
    if (RobotBase.isReal()) {
      visionIO = new VisionIOLimelight();
      simulationFixtureChooser = Optional.empty();
      visionIOSimHarness = Optional.empty();
    } else {
      VisionIOSim visionIOSim =
          new VisionIOSim(fieldLayout, Constants.VisionConstants.kRobotToCamera);
      visionIO = visionIOSim;
      SendableChooser<VisionIOSimHarness.FixtureSelection> chooser = new SendableChooser<>();
      chooser.setDefaultOption(
          VisionIOSimHarness.FixtureSelection.UNAVAILABLE.name(),
          VisionIOSimHarness.FixtureSelection.UNAVAILABLE);
      chooser.addOption(
          VisionIOSimHarness.FixtureSelection.VALID_FRAME_A.name(),
          VisionIOSimHarness.FixtureSelection.VALID_FRAME_A);
      chooser.addOption(
          VisionIOSimHarness.FixtureSelection.VALID_FRAME_B.name(),
          VisionIOSimHarness.FixtureSelection.VALID_FRAME_B);
      SmartDashboard.putData("Simulation/VisionFusionHarness/Fixture", chooser);
      simulationFixtureChooser = Optional.of(chooser);
      visionIOSimHarness = Optional.of(new VisionIOSimHarness(visionIOSim, fieldLayout));
    }
    visionSubsystem =
        new VisionSubsystem(
            visionIO,
            fieldLayout,
            Constants.VisionConstants.kRobotToCamera,
            new Policy(1.0, 2.0, 3.0),
            0.250,
            Timer::getFPGATimestamp);
    intakeSubsystem = new IntakeSubsystem(new IntakeIONoop());
    feederSubsystem = new FeederSubsystem(new FeederIONoop());
    flywheelSubsystem = new FlywheelSubsystem(new FlywheelIONoop());

    SwerveModuleIO frontLeft;
    SwerveModuleIO frontRight;
    SwerveModuleIO backLeft;
    SwerveModuleIO backRight;
    GyroIO gyro;

    if (RobotBase.isReal()) {
      frontLeft = SwerveModuleIOCTRE.createFrontLeft();
      frontRight = SwerveModuleIOCTRE.createFrontRight();
      backLeft = SwerveModuleIOCTRE.createBackLeft();
      backRight = SwerveModuleIOCTRE.createBackRight();
      gyro = new GyroIOPigeon2();
    } else {
      SwerveSimulationState simulationState = new SwerveSimulationState();
      frontLeft =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kFrontLeftDrivePositionSign,
              simulationState,
              ModuleIdentity.FRONT_LEFT);
      frontRight =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kFrontRightDrivePositionSign,
              simulationState,
              ModuleIdentity.FRONT_RIGHT);
      backLeft =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kBackLeftDrivePositionSign,
              simulationState,
              ModuleIdentity.BACK_LEFT);
      backRight =
          new SwerveModuleIOSim(
              Constants.SwerveConstants.kBackRightDrivePositionSign,
              simulationState,
              ModuleIdentity.BACK_RIGHT);
      SwerveKinematics simulationKinematics = new SwerveKinematics();
      gyro = new GyroIOSim(simulationState, simulationKinematics::toChassisSpeeds);
    }

    swerveSubsystem =
        new SwerveSubsystem(
            frontLeft,
            frontRight,
            backLeft,
            backRight,
            gyro);
    visionFusionCoordinator = new VisionFusionCoordinator(visionSubsystem, swerveSubsystem);
    RobotConfig pathPlannerRobotConfig = createPathPlannerRobotConfig();
    pathPlannerTrajectoryAdapter = new PathPlannerTrajectoryAdapter(pathPlannerRobotConfig);
    autoBuilderContractAdapter =
        new AutoBuilderContractAdapter(
            swerveSubsystem, pathPlannerTrajectoryAdapter, pathPlannerRobotConfig);
    autoBuilderContractAdapter.configure();
    autonomousPreparationCoordinator =
        new AutonomousPreparationCoordinator(
            swerveSubsystem,
            pathPlannerTrajectoryAdapter,
            autoBuilderContractAdapter,
            Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
            Constants.PathPlannerLearningConstants.kPathAssetName);
    AtomicReference<Optional<frc.robot.observation.AutonomousEventObservation>>
        autonomousEventObservationReference = new AtomicReference<>(Optional.empty());
    AutonomousEventRegistration autonomousEventRegistration =
        new AutonomousEventRegistration(
            observation -> autonomousEventObservationReference.set(Optional.of(observation)));
    autonomousEventRegistration.register(
        new AutonomousEventBinding(
            AutonomousEventId.LEARNING_EVENT,
            () ->
                new AutonomousEventDemonstrationCommand(
                    AutonomousEventId.LEARNING_EVENT,
                    observation ->
                        autonomousEventObservationReference.set(Optional.of(observation)),
                    Timer::getFPGATimestamp,
                    Constants.PathPlannerLearningConstants.kLearningEventDurationSeconds),
            Set.of()));
    commissioningDashboard = new SwerveFrontLeftCommissioningDashboard(swerveSubsystem);
    fourModuleTestDashboard = new SwerveFourModuleTestDashboard(swerveSubsystem);
    driveThreeMeterValidationDashboard =
        new DriveThreeMeterValidationDashboard(
            swerveSubsystem,
            new DriveThreeMeterValidationTelemetryFacade(
                NetworkTableInstance
                    .getDefault()
                     .getTable("DriveThreeMeterValidation")));
    autonomousRoutineFactory =
        new AutonomousRoutineFactory(
            swerveSubsystem,
            autoBuilderContractAdapter,
            autonomousPreparationCoordinator);
    autonomousRoutineChooser = new SendableChooser<>();
    autonomousRoutineChooser.setDefaultOption(
        "SAFE_STOP", AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP);
    autonomousRoutineChooser.addOption(
        "ONE_METER_PATH", AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH);
    autonomousRoutineChooser.addOption(
        "ONE_METER_WITH_EVENT",
        AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_WITH_EVENT);
    SmartDashboard.putData("Autonomous Routine", autonomousRoutineChooser);
    prepareAutonomousCommand =
        new PrepareAutonomousCommand(
            swerveSubsystem,
            autonomousPreparationCoordinator,
            autonomousRoutineChooser::getSelected,
            DriverStation::getAlliance);
    SmartDashboard.putData("Prepare Autonomous", prepareAutonomousCommand);

    SwerveTelemetryFacade swerveTelemetryFacade =
        new SwerveTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable("Swerve"));
    CommandXboxController driverController =
        new CommandXboxController(Constants.DriverInputConstants.kXboxControllerPort);
    XboxDriverInputSource driverInputSource = new XboxDriverInputSource(driverController.getHID());
    DriverInputTelemetryFacade driverInputTelemetryFacade =
        new DriverInputTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable("DriverInput"));
    FieldRelativeTeleopDriveCommand fieldRelativeTeleopDriveCommand =
        new FieldRelativeTeleopDriveCommand(
            swerveSubsystem,
            driverInputSource,
            driverInputTelemetryFacade);
    swerveSubsystem.setDefaultCommand(fieldRelativeTeleopDriveCommand);

    RunIntakeCommand runIntakeCommand = new RunIntakeCommand(intakeSubsystem);
    driverController.rightBumper().whileTrue(runIntakeCommand);

    RunFeederCommand runFeederCommand = new RunFeederCommand(feederSubsystem);
    driverController.leftBumper().whileTrue(runFeederCommand);

    // Back/View is the single explicit operator preparation action and is gated before scheduling.
    new JoystickButton(driverController.getHID(), XboxController.Button.kBack.value)
        .and(DriverStation::isDisabled)
        .onTrue(prepareAutonomousCommand);
    AutonomousPreparationTelemetryFacade autonomousPreparationTelemetryFacade =
        new AutonomousPreparationTelemetryFacade(
            NetworkTableInstance.getDefault().getTable("AutonomousPreparation"));
    AutonomousEventTelemetryFacade autonomousEventTelemetryFacade =
        new AutonomousEventTelemetryFacade(
            NetworkTableInstance.getDefault().getTable("AutonomousEvent"));
    VisionTelemetryFacade visionTelemetryFacade =
        new VisionTelemetryFacade(NetworkTableInstance.getDefault().getTable("Vision"));
    IntakeTelemetryFacade intakeTelemetryFacade =
        new IntakeTelemetryFacade(NetworkTableInstance.getDefault().getTable("Intake"));
    FeederTelemetryFacade feederTelemetryFacade =
        new FeederTelemetryFacade(NetworkTableInstance.getDefault().getTable("Feeder"));
    FlywheelTelemetryFacade flywheelTelemetryFacade =
        new FlywheelTelemetryFacade(NetworkTableInstance.getDefault().getTable("Flywheel"));
    robotTelemetry =
        new RobotTelemetry(
            swerveSubsystem,
            swerveTelemetryFacade,
            autonomousPreparationCoordinator::getObservation,
            autonomousPreparationTelemetryFacade,
            autonomousEventObservationReference::get,
            autonomousEventTelemetryFacade,
            visionSubsystem,
            visionTelemetryFacade,
            intakeSubsystem,
            intakeTelemetryFacade,
            feederSubsystem,
            feederTelemetryFacade,
            flywheelSubsystem,
            flywheelTelemetryFacade);
  }

  /**
   * Returns one fresh command for the chooser and current-alliance snapshots.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    AutonomousRoutineFactory.AutonomousRoutineId selectedRoutine = null;
    try {
      selectedRoutine = autonomousRoutineChooser.getSelected();
    } catch (RuntimeException ignored) {
      // A chooser failure is an invalid selection and therefore fails closed in the factory.
    }
    Optional<DriverStation.Alliance> currentAlliance = DriverStation.getAlliance();
    return autonomousRoutineFactory.create(selectedRoutine, currentAlliance);
  }

  /** Routes an unexpected Robot-level scheduler failure to the autonomous safety owners. */
  public void handleSchedulerRuntimeException(RuntimeException failure) {
    autonomousPreparationCoordinator.recordSchedulerFatal(
        Objects.requireNonNull(failure, "failure"));
  }

  private static RobotConfig createPathPlannerRobotConfig() {
    ModuleConfig moduleConfig =
        new ModuleConfig(
            Constants.SwerveConstants.kWheelRadiusMeters,
            Constants.PathPlannerLearningConstants.kProvisionalMaxDriveVelocityMetersPerSecond,
            Constants.PathPlannerLearningConstants.kProvisionalWheelCof,
            DCMotor.getKrakenX60(1),
            Constants.SwerveConstants.kDriveGearRatio,
            Constants.SwerveConstants.kDriveSupplyCurrentLimitAmps,
            1);
    double halfWheelbaseMeters = Constants.SwerveConstants.kWheelbaseMeters / 2.0;
    double halfTrackWidthMeters = Constants.SwerveConstants.kTrackWidthMeters / 2.0;
    return new RobotConfig(
        Constants.PathPlannerLearningConstants.kProvisionalRobotMassKg,
        Constants.PathPlannerLearningConstants.kProvisionalRobotMoiKgMetersSquared,
        moduleConfig,
        new Translation2d(halfWheelbaseMeters, halfTrackWidthMeters),
        new Translation2d(halfWheelbaseMeters, -halfTrackWidthMeters),
        new Translation2d(-halfWheelbaseMeters, halfTrackWidthMeters),
        new Translation2d(-halfWheelbaseMeters, -halfTrackWidthMeters));
  }

  /**
   * Returns the runtime telemetry coordinator.
   *
   * @return runtime telemetry coordinator
   */
  public RobotTelemetry getRobotTelemetry() {
    return robotTelemetry;
  }

  /**
   * Returns the post-scheduler vision-to-Swerve fusion handoff.
   *
   * @return plain runtime fusion coordinator
   */
  public VisionFusionCoordinator getVisionFusionCoordinator() {
    return visionFusionCoordinator;
  }

  /** Applies one simulation-only vision fixture transition for the current simulation cycle. */
  void runSimulationHarness() {
    if (RobotBase.isSimulation()) {
      simulationFixtureChooser
          .ifPresent(
              chooser ->
                  visionIOSimHarness.ifPresent(
                      harness -> harness.apply(chooser.getSelected())));
    }
  }
}
