# S00_L15 Single Module Open Loop Commissioning Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L15_SingleModuleOpenLoopCommissioning_Foundation
- Previous Lesson: S00_L14_SwerveHardwareConfiguration_Commissioning_Foundation
- Previous Lesson Status: COMPLETE / FROZEN / READ-ONLY
- Source: S00_L14_SwerveHardwareConfiguration_Commissioning_Foundation
- Status: COMPLETE — FROZEN / READ-ONLY
- Architecture Review: PASS
- Baseline Build: PASS
- Focused Tests: PASS (12/12)
- Full Test Suite: PASS (48/48)
- Build: PASS
- Simulation: NOT TESTED
- Driver Station / Glass: PASS
- Real Robot: PASS
- Documentation: PASS
- Commit/Push/Freeze: NOT TESTED
- Date: 2026-08-04

## Exact Lesson Goal

Expose four explicit Glass/SmartDashboard Test-mode commands for Front Left-only open-loop
commissioning, with fixed 0.05 duty magnitude, fixed 0.25-second duration, and defense-in-depth
mode, ownership, mutual-exclusion, and watchdog protections.

## Architecture Decision

APPROVED. `RobotContainer` constructs one small `SwerveFrontLeftCommissioningDashboard` publisher.
The publisher only registers four fixed command factories. The command layer owns scheduling
lifecycle; `SwerveSubsystem` owns fixed Front Left delegation and independent safety enforcement;
existing IO remains the hardware boundary.

## Activation Path

Glass/SmartDashboard button → WPILib command scheduling → `Robot.robotPeriodic()` →
`CommandScheduler` → commissioning command → `SwerveSubsystem.startFrontLeftCommissioning()` →
Front Left `SwerveModuleIO`.

The four published identities are `FL Drive Positive`, `FL Drive Negative`, `FL Steer Positive`,
and `FL Steer Negative`. Commands are accepted only while `DriverStation.isTestEnabled()` is true.
The default command disabled gate rejects Disabled scheduling; command and subsystem mode checks
reject Teleop and Autonomous.

## Hardening

- The command constructor is private; only four fixed factories are public.
- Factories use only the named 0.05 drive/steer constants and 0.25-second duration constant.
- SwerveSubsystem clamps absolute output, rejects non-Test mode, rejects a second active pulse,
  clears the non-selected actuator, and watchdog-stops Front Left at the fixed duration.
- Command end, cancellation, interruption, mode exit, and failure stop Front Left.
- FR/BL/BR remain untouched.

## Resolved Verification Defects

- HAL initialization now precedes all `DriverStationSim` use.
- `DriverStationSim.resetData()` runs before each test's mode setup.
- The command no longer duplicates the subsystem's output-failure stop.
- `SwerveSubsystem` remains the sole owner of output-failure cleanup.
- A `finally` path guarantees inactive commissioning state and watchdog cleanup.
- Stop failures are retained as suppressed exceptions on the original output failure.

## Verification Plan

- Test published command identities and Glass/SmartDashboard registration.
- Test Test-mode acceptance and Disabled/Teleop/Autonomous rejection.
- Test private/factory-only construction and fixed bounds.
- Test subsystem duty clamp, mutual exclusion, watchdog, mode-exit stop, cancellation,
  interruption, failure cleanup, and FR/BL/BR isolation.
- Run focused tests and full build when Java is available.
- User owns Driver Station, Glass, real-robot, and Git verification.

## Verification Record

- Architecture Review: PASS.
- Implementation: PASS.
- Baseline Build: PASS.
- Focused Tests: PASS (12/12).
- Full Test Suite: PASS (48/48).
- Full Build: PASS.
- Simulation: NOT TESTED; no simulation result was supplied for this lesson.
- Glass Commissioning: PASS.
- Driver Station: PASS.
- Real Robot: PASS.
- Documentation: PASS.
- Commit: NOT TESTED.
- Push: NOT TESTED.
- Freeze: NOT TESTED.
