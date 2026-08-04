# S00_L14 to S00_L15 Step by Step

## Step 1

- Objective: Audit the missing activation path and inherited boundaries.
- Why: A command class alone cannot be used on the deployed robot without an explicit runtime
  interface.
- Action: Inspect `Robot`, `RobotContainer`, `SwerveSubsystem`, IO, command lifecycle, and existing
  tests.
- Files Changed: None.
- Verification: `robotPeriodic()` runs `CommandScheduler`, but no commissioning command is scheduled
  and RobotContainer has no commissioning publisher.
- Expected Result: Add one composition-root-constructed publisher only.

## Step 2

- Objective: Publish exactly four explicit command identities.
- Why: Glass/SmartDashboard command buttons provide a temporary explicit interface without Xbox,
  NetworkButton booleans, or automatic startup scheduling.
- Action: Construct `SwerveFrontLeftCommissioningDashboard` from RobotContainer; publish `FL Drive
  Positive`, `FL Drive Negative`, `FL Steer Positive`, and `FL Steer Negative`.
- Files Changed: `src/main/java/frc/robot/commands/SwerveFrontLeftCommissioningDashboard.java`,
  `src/main/java/frc/robot/RobotContainer.java`.
- Verification: The publisher registers four fixed command objects and no other actuator interface.
- Expected Result: Glass/SmartDashboard button selection schedules one explicit command.

## Step 3

- Objective: Restrict command construction and mode eligibility.
- Why: A public configurable constructor allowed arbitrary duty and duration, and commands must never
  run in Disabled, Teleop, or Autonomous.
- Action: Make the command constructor private; retain only four fixed factories; gate initialize,
  execute, and completion with `DriverStation.isTestEnabled()`.
- Files Changed: `src/main/java/frc/robot/commands/SwerveFrontLeftOpenLoopCommissioningCommand.java`.
- Verification: Factories use only 0.05 duty magnitude and 0.25-second duration; non-Test modes
  reject output and command requirements preserve one active action.
- Expected Result: Only a Test-mode dashboard action can begin a pulse.

## Step 4

- Objective: Add subsystem defense in depth.
- Why: A public subsystem output API could bypass scheduler and command timeout safety.
- Action: Replace arbitrary output delegation with fixed `startFrontLeftCommissioning()`; enforce
  Test mode, active-action exclusion, output clamp, drive/steer mutual exclusion, Front Left-only
  delegation, and an independent watchdog in `periodic()`.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: The subsystem stops Front Left on mode exit, watchdog expiry, and output failure;
  FR/BL/BR remain untouched.
- Expected Result: Direct subsystem calls are still bounded and cannot produce non-Test or expired
  output.

## Step 5

- Objective: Verify activation and safety behavior.
- Why: Runtime commissioning must prove both the published interface and all stop/rejection paths.
- Action: Add tests for four identities, Test acceptance, Disabled/Teleop/Autonomous rejection,
  private construction, fixed bounds, subsystem clamp/watchdog/mutual exclusion, cancellation,
  interruption, mode exit, failure cleanup, and other-module isolation.
- Files Changed: `src/test/java/frc/robot/commands/SwerveFrontLeftOpenLoopCommissioningCommandTest.java`.
- Verification: Focused tests PASS (12/12), including output-failure cleanup and Front Left-only
  isolation.
- Expected Result: All focused commissioning safety cases pass.

## Step 6

- Objective: Record the completed controlled verification state.
- Why: The lesson requires explicit evidence for software, Glass, Driver Station, and real-robot
  commissioning behavior before it becomes read-only.
- Action: Record Architecture Review, Implementation, focused tests, full tests, full build, Glass,
  Driver Station, real-robot, and Documentation as PASS. Record Commit `193fd4a`, Push to
  `origin/main`, and Freeze as PASS.
- Files Changed: S00_L15 README, plan, status, checklist, and this guide.
- Verification: Documentation records the full Glass/SmartDashboard → CommandScheduler → command →
  subsystem → Front Left IO path and the safety checklist.
- Expected Result: Status is `COMPLETE`; Lesson is `FROZEN / READ-ONLY`; Commit `193fd4a`, Push to
  `origin/main`, and Freeze are PASS.

## Step 7

- Objective: Close the verified test-initialization and output-failure defects.
- Why: Native HAL access must be initialized safely, and one output failure must produce one
  subsystem-owned stop while preserving all failure information.
- Action: Initialize HAL before DriverStationSim use, reset Driver Station simulation data before
  each test, remove the command's duplicate stop, and finalize inactive commissioning state in a
  subsystem `finally` path while preserving stop failures as suppressed exceptions.
- Files Changed: `src/test/java/frc/robot/commands/SwerveFrontLeftOpenLoopCommissioningCommandTest.java`,
  `src/main/java/frc/robot/commands/SwerveFrontLeftOpenLoopCommissioningCommand.java`,
  `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: Focused Tests PASS (12/12); `outputFailureStopsFrontLeft()` passes with one stop
  call, zero Front Left outputs, inactive commissioning state, and no FR/BL/BR actuation.
- Expected Result: HAL test setup and all commissioning cleanup paths are deterministic and safe.
