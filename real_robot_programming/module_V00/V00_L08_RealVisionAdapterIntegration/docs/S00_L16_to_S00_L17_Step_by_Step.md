# S00_L16 to S00_L17 Step by Step

## Lesson Objective

S00_L17 extends frozen `S00_L16_ModuleHardwareConfigurationContract_Foundation` with the smallest
vendor-neutral closed-loop contract for one representative module: Front Left drive velocity and
steer angle. The transition preserves the Frozen Backbone, RobotContainer boundary, vendor
isolation, read-only telemetry, open-loop commissioning, configuration health, and fail-closed
behavior.

Final state: `COMPLETE / FROZEN / READ-ONLY`.

## Architecture Constraints

- Preserve `Driver -> controls -> commands -> subsystems -> IO -> hardware`.
- Keep Phoenix request construction inside `SwerveModuleIOCTRE`.
- Keep `SwerveModuleIO` vendor-neutral and retain all S00_L16 open-loop methods and `stop()`.
- Keep RobotContainer as composition root only and telemetry read-only.
- Actuate Front Left only; do not add teleop input, four-module actuation, kinematics, odometry,
  pose estimation, FusedCANcoder, Motion Magic, or generated CTRE drivetrain architecture.

## Step 1 - Copy the Frozen Source

- Objective: Start from the verified hardware-configuration lesson.
- Why: Each lesson is an independent WPILib project and completed lessons are immutable.
- Action: Copy `S00_L16_ModuleHardwareConfigurationContract_Foundation` to
  `S00_L17_SingleModuleClosedLoopControl`; do not create or modify S00_L18.
- Files Changed: New S00_L17 directory.
- Verification: Delete copied `build/` and `.gradle/` artifacts and run the baseline build.
- Expected Result: The copied S00_L16 baseline builds before the new concept is introduced.

## Step 2 - Establish Lesson Metadata and Architecture Gate

- Objective: Make S00_L17 the active editable lesson.
- Why: Only the active lesson may change; S00_L16 remains frozen.
- Action: Set S00_L17 identity, record S00_L16 as source, review governance, Frozen Backbone,
  RobotContainer, IO, observation, telemetry, and commissioning boundaries.
- Files Changed: S00_L17 metadata.
- Verification: Architecture Audit, Frozen Backbone, and vendor-isolation reviews PASS.
- Expected Result: S00_L17 is active while S00_L16 remains read-only.

## Step 3 - Add the Vendor-Neutral Closed-Loop Contract

- Objective: Add only the methods required for one module.
- Why: Control intent belongs above IO; vendor-specific requests belong below the IO boundary.
- Action: Add `setDriveVelocityMetersPerSecond(double)` and `setSteerAngle(Rotation2d)` while
  preserving open-loop methods and `stop()`.
- Files Changed: `SwerveModuleIO.java`, `SwerveModuleIONoop.java`, `SwerveSubsystem.java`.
- Verification: Contract tests cover units, fail-closed behavior, stop behavior, and Front Left-only
  routing.
- Expected Result: The public contract remains vendor-neutral.

## Step 4 - Map Approved CTRE Configuration and Requests

- Objective: Map Phoenix calibration evidence without copying generated drivetrain architecture.
- Why: Tuner X is hardware evidence, not the Coding Lab architecture.
- Action: Configure drive feedback and ratios; configure steer RemoteCANcoder feedback and continuous
  wrap; apply and refresh configuration; verify readback; preserve base versus closed-loop health.
  Use `VelocityVoltage` and `PositionVoltage`, Slot 0, FOC false. Preserve drive Slot 0
  `kP=0.1`, `kI=0`, `kD=0`, `kS=0`, `kV=0.124`, `kA=0`.
- Files Changed: `Constants.java`, `SwerveModuleIOCTRE.java`.
- Verification: StatusCodes, feedback fields, ratios, gains, and health gates are verified.
- Expected Result: Healthy configuration accepts requests; unhealthy required configuration rejects
  nonzero requests.

## Step 5 - Add Bounded Front Left Commissioning

- Objective: Exercise the closed-loop contract safely in Test mode.
- Why: Fixed one-module commands provide measurable evidence without teleop or four-module control.
- Action: Add Front Left drive `+0.30/-0.30 m/s`, retain the `±0.50 m/s` clamp, add steer relative
  steps, use a `1.0 s` timeout, and stop on interruption, disable, mode exit, exception, rejection,
  or normal completion.
- Files Changed: Closed-loop commissioning command, dashboard, subsystem, and focused tests.
- Verification: Scheduling, ownership, timeout, stop, conversion, wrap, and regression checks PASS.
- Expected Result: Only Front Left receives the fixed commissioning requests.

## Step 6 - Add Manual Static-Friction Characterization

- Objective: Measure breakaway without changing the velocity controller.
- Why: The characterization is a bounded measurement tool; it does not promote one observation to a
  production feedforward value.
- Action: Add one dashboard command per positive step from `+0.10` through `+1.00 V`. Require Test
  and Enabled, raise and secure the wheel, use VoltageOut with FOC false, pulse for `0.25 s`, sample
  peak rotor/mechanism velocity and currents, and stop automatically.
- Files Changed: `Constants.java`, static-friction command, subsystem, IO contract, Noop IO, CTRE IO,
  dashboard, and focused tests.
- Verification: Each click is independent; no automatic sweep exists; all unsafe exits stop safely.
- Expected Result: One bounded measurement is produced for each manual click.

## Step 7 - Finalize the Result Path

- Objective: Make every characterization pulse observable.
- Why: A measurement is useful only when its request status, peaks, classification, and stop reason
  are recorded.
- Action: Finalize from command/subsystem stop paths with typed reasons and emit one result line per
  click containing requested voltage, setControl status, peak rotor/mechanism velocity, peak supply
  and torque current, breakaway classification, and stop reason.
- Files Changed: IO contract, CTRE IO, Noop IO, subsystem, static-friction command, and tests.
- Verification: Static-friction result line and repeated positive unloaded breakaway verification PASS.
- Expected Result: Every manual click has one bounded result record.

## Step 8 - Simulation and Dashboard Verification

- Objective: Verify routing and command behavior before hardware testing.
- Why: Simulation, Glass, and Driver Station checks reduce hardware risk.
- Action: Run Simulation, inspect Glass and Driver Station, schedule each Front Left commissioning
  command, and confirm only the intended module is addressed.
- Files Changed: None.
- Verification: Simulation and Glass / Driver Station PASS.
- Expected Result: Commands schedule, stop, and publish the expected read-only state.

## Step 9 - Real-Robot Verification

- Objective: Verify the approved contract on hardware.
- Why: Software tests cannot establish motor direction, physical motion, or safe stop behavior.
- Action: Deploy the verified artifact in Test and Enabled mode. Run Front Left Drive Positive at
  `+0.30 m/s` and Negative at `-0.30 m/s`; verify direction, no hunting, and automatic `1.0 s` stop.
  Raise and secure the wheel for each separate positive static-friction voltage click.
- Files Changed: None; user-owned hardware records.
- Verification: Positive and negative Drive, correct directions, automatic stop, no visible oscillation,
  static-friction result line, and repeated positive unloaded breakaway all PASS.
- Expected Result: The one-module closed-loop and characterization contract is verified.

## Step 10 - Final Documentation and Freeze

- Objective: Record only verified facts and freeze the lesson snapshot.
- Why: Completed lessons are immutable references for later lessons.
- Action: Update README, plan, checklist, status, and this transition guide to COMPLETE / FROZEN.
  Preserve technical debt and deferred scope; do not create or modify S00_L18.
- Files Changed: The five active S00_L17 documentation files.
- Verification: Architecture, interface, Simulation, Glass, Driver Station, real robot, focused tests,
  full tests, and clean build are PASS.
- Expected Result: S00_L17 is a complete read-only lesson.

## Source and Test Delta from Frozen S00_L16

### Added source files

- `src/main/java/frc/robot/commands/SwerveFrontLeftClosedLoopCommissioningCommand.java`
- `src/main/java/frc/robot/commands/SwerveFrontLeftDriveStaticFrictionCharacterizationCommand.java`

### Modified source files

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/commands/SwerveFrontLeftCommissioningDashboard.java`
- `src/main/java/frc/robot/io/swerve/SwerveModuleIO.java`
- `src/main/java/frc/robot/io/swerve/SwerveModuleIOCTRE.java`
- `src/main/java/frc/robot/io/swerve/SwerveModuleIONoop.java`
- `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`

### Removed source file

- `edu/wpi/first/wpilibj2/command/CommandScheduler.java`; the official WPILib dependency remains
  authoritative.

### Added test files

- `src/test/java/frc/robot/commands/SwerveFrontLeftClosedLoopCommissioningCommandTest.java`
- `src/test/java/frc/robot/io/swerve/SwerveModuleIOClosedLoopContractTest.java`
- `src/test/java/frc/robot/subsystems/SwerveSubsystemClosedLoopCommissioningTest.java`

### Modified test files

- `src/test/java/frc/robot/commands/SwerveFrontLeftOpenLoopCommissioningCommandTest.java`
- `src/test/java/frc/robot/io/swerve/SwerveModuleIOCTREConfigurationTest.java`
- `src/test/java/frc/robot/subsystems/SwerveSubsystemTest.java`

### Documentation files

- `README.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, `LESSON_STATUS.md`
- `docs/S00_L16_to_S00_L17_Step_by_Step.md`

`Robot.java`, `RobotContainer.java`, telemetry, output-pipeline files, Gradle files, vendor
dependencies, S00_L16, and all previous lessons remain unchanged.

## Technical Debt and Deferred Scope

- PID and feedforward values are commissioning baselines, not production-final.
- `kS` remains deferred.
- Full SysId and static-friction characterization remain future work.
- Commissioning commands must not become normal drive controls.
- FusedCANcoder, Motion Magic, closed-loop teleop, four-module state actuation, kinematics, odometry,
  and pose estimation are deferred.

## User-Owned Git Procedure

Codex does not run Git. After reviewing the frozen lesson, the user may run:

```powershell
Set-Location 'C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7'
git status --short
git diff --check
git add real_robot_programming/module_S00/S00_L17_SingleModuleClosedLoopControl
git commit -m "Complete S00_L17 single module closed-loop control"
git push origin main
git status --short
```

The final clean-working-tree check must produce no output.

## Inheritance Rule for S00_L18

Do not create or modify S00_L18 during or before this freeze. S00_L18 may be created only by copying
this frozen S00_L17 snapshot after the user completes the Git commit, push, and clean-working-tree
verification. No S00_L17 source or documentation may be edited from the S00_L18 workflow.
