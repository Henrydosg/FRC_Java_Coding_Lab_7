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
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.AllianceAwareAutonomousStartPoseResetCommand;
import frc.robot.commands.AutonomousStartContext;
import frc.robot.commands.AutoBuilderContractAdapter;
import frc.robot.commands.CaptureFieldHeadingReferenceCommand;
import frc.robot.commands.DriveThreeMeterValidationDashboard;
import frc.robot.commands.FieldRelativeTeleopDriveCommand;
import frc.robot.commands.KnownFieldPoseResetDashboard;
import frc.robot.commands.PathPlannerTrajectoryAdapter;
import frc.robot.commands.SwerveFourModuleTestDashboard;
import frc.robot.commands.SwerveFrontLeftCommissioningDashboard;
import frc.robot.controls.XboxDriverInputSource;
import frc.robot.io.gyro.GyroIO;
import frc.robot.io.gyro.GyroIOSim;
import frc.robot.io.gyro.GyroIOPigeon2;
import frc.robot.io.simulation.SwerveSimulationState;
import frc.robot.io.simulation.SwerveSimulationState.ModuleIdentity;
import frc.robot.io.swerve.SwerveModuleIO;
import frc.robot.io.swerve.SwerveModuleIOCTRE;
import frc.robot.io.swerve.SwerveModuleIOSim;
import frc.robot.subsystems.SwerveKinematics;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.driver.DriverInputTelemetryFacade;
import frc.robot.telemetry.swerve.SwerveTelemetryFacade;
import frc.robot.telemetry.validation.DriveThreeMeterValidationTelemetryFacade;
import frc.robot.util.FieldAllianceTransform;
import java.util.Optional;
import java.util.Set;

/**
 * Creates robot components and configures command bindings.
 */
public class RobotContainer {
  private final SwerveSubsystem swerveSubsystem;
  private final SwerveFrontLeftCommissioningDashboard commissioningDashboard;
  private final SwerveFourModuleTestDashboard fourModuleTestDashboard;
  private final DriveThreeMeterValidationDashboard driveThreeMeterValidationDashboard;
  private final AllianceAwareAutonomousStartPoseResetCommand startPoseResetCommand;
  private final KnownFieldPoseResetDashboard knownFieldPoseResetDashboard;
  private final PathPlannerTrajectoryAdapter pathPlannerTrajectoryAdapter;
  private final AutoBuilderContractAdapter autoBuilderContractAdapter;
  private final Command autonomousCommand;
  private final RobotTelemetry robotTelemetry;

  /**
   * Creates the composition root.
   */
  public RobotContainer() {
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
    RobotConfig pathPlannerRobotConfig = createPathPlannerRobotConfig();
    pathPlannerTrajectoryAdapter = new PathPlannerTrajectoryAdapter(pathPlannerRobotConfig);
    autoBuilderContractAdapter =
        new AutoBuilderContractAdapter(
            swerveSubsystem, pathPlannerTrajectoryAdapter, pathPlannerRobotConfig);
    autoBuilderContractAdapter.configure();
    startPoseResetCommand =
        new AllianceAwareAutonomousStartPoseResetCommand(
            swerveSubsystem, this::createDisabledStartContext);
    commissioningDashboard = new SwerveFrontLeftCommissioningDashboard(swerveSubsystem);
    fourModuleTestDashboard = new SwerveFourModuleTestDashboard(swerveSubsystem);
    driveThreeMeterValidationDashboard =
        new DriveThreeMeterValidationDashboard(
            swerveSubsystem,
            new DriveThreeMeterValidationTelemetryFacade(
                NetworkTableInstance
                    .getDefault()
                     .getTable("DriveThreeMeterValidation")));
    knownFieldPoseResetDashboard = new KnownFieldPoseResetDashboard(startPoseResetCommand);

    autonomousCommand = Commands.defer(this::createAutonomousSession, Set.of(swerveSubsystem));

    SwerveTelemetryFacade swerveTelemetryFacade =
        new SwerveTelemetryFacade(
            NetworkTableInstance
                .getDefault()
                .getTable("Swerve"));
    XboxController driverController =
        new XboxController(Constants.DriverInputConstants.kXboxControllerPort);
    XboxDriverInputSource driverInputSource = new XboxDriverInputSource(driverController);
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

    // Xbox Back/View is unused by the inherited L22 bindings and captures field zero only Disabled.
    new JoystickButton(driverController, XboxController.Button.kBack.value)
        .onTrue(new CaptureFieldHeadingReferenceCommand(swerveSubsystem));
    robotTelemetry = new RobotTelemetry(swerveSubsystem, swerveTelemetryFacade);
  }

  /**
   * Returns bounded robot-relative motion followed by the repeating zero-motion safety hold.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autonomousCommand;
  }

  private Optional<AutonomousStartContext> createDisabledStartContext() {
    Optional<DriverStation.Alliance> alliance = DriverStation.getAlliance();
    if (alliance.isEmpty()) {
      return Optional.empty();
    }
    try {
      Trajectory canonicalTrajectory = pathPlannerTrajectoryAdapter.createCanonicalTrajectory();
      Pose2d executionStartPose =
          FieldAllianceTransform.fromCanonicalBluePose(
              canonicalTrajectory.getInitialPose(),
              Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
              alliance.orElseThrow());
      return Optional.of(
          new AutonomousStartContext(
              Constants.HolonomicTrajectoryFollowingConstants.kLearningFieldVariant,
              alliance.orElseThrow(),
              executionStartPose));
    } catch (RuntimeException failure) {
      return Optional.empty();
    }
  }

  private Command createAutonomousSession() {
    Optional<AutonomousStartContext> context = startPoseResetCommand.consumeAcceptedStartContext();
    if (context.isEmpty()) {
      return Commands.runOnce(swerveSubsystem::stop, swerveSubsystem);
    }
    return autoBuilderContractAdapter.createPathCommand(context.orElseThrow());
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
}
