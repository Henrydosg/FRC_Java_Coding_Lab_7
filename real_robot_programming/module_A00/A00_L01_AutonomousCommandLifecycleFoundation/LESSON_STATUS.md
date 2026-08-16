# Lesson Status

## Identity

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L01_AutonomousCommandLifecycleFoundation`
- Previous Lesson: `S00_L24_PoseEstimationAndAutonomousReadiness`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: establish autonomous command lifecycle and centralized stop
  ownership without issuing nonzero autonomous drivetrain motion.
- Architecture Review: `PASS`
- Baseline Build: `PASS`
- Build: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `PASS`
- Real Robot: `HOLD`
- Transition Guide: `FINAL / PASS`
- Next Lesson: `A00_L02_AutonomousModeScheduling`
- Git: user-owned; not run by Codex

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| Direct inheritance from frozen S00_L24 | PASS | User-supplied inheritance evidence |
| Generated artifacts cleaned | PASS | User-supplied inheritance evidence |
| Java 17 baseline build | PASS | User-supplied baseline evidence |
| Focused command regression | PASS | User-supplied Java 17 evidence |
| Full Java 17 regression | PASS | User-supplied Java 17 evidence |
| Final clean build | PASS | User-supplied Java 17 clean-build evidence |
| Simulation Disabled baseline | PASS | User-supplied Simulation evidence |
| Autonomous Enabled zero-motion/non-regression | PASS | User-supplied Simulation evidence |
| Teleop fresh-input recovery | PASS | User-supplied Simulation evidence |
| Driver Station / Glass | PASS | Supplied zero-motion/non-regression and recovery evidence; command not registered |
| Real Robot | HOLD | A00_L01 hardware evidence remains pending |
| Transition Guide | PASS | Final guide records the complete S00_L24 -> A00_L01 evolution |
| Git Commit | NOT TESTED | User-owned; Git not run by Codex |
| Git Push | NOT TESTED | User-owned; Git not run by Codex |

## Implemented Scope

`AutonomousSafetyHoldCommand` is a production-useful bounded lifecycle
command. It requires `SwerveSubsystem`, accepts a finite positive duration,
uses an injected monotonic clock, calls `stop()` at initialization and on
both normal and interrupted termination, performs no actuation in
`execute()`, returns `runsWhenDisabled() == false`, and fails closed for
invalid, backward, nonfinite, or throwing clock behavior.

No nonzero chassis-speed request is issued. The command is not selected by
Robot autonomous mode and is not registered through RobotContainer; mode
composition belongs to A00_L02.

## Zero-Motion Safety Boundary

A00_L01 and A00_L02 are zero-motion lessons. A00_L03 is the first A00 lesson
permitted to issue a nonzero autonomous drivetrain request. Existing
finite-request, centralized stop, and Disabled-transition disarm contracts
inherited from S00_L24 remain preserved.

## Architecture Map

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

The inherited observation path remains:

```text
hardware or simulation IOInputs
-> SwerveSubsystem
-> immutable observation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

RobotContainer remains composition-only. The command does not access IO,
telemetry, estimator internals, or vendor APIs.

## Out of Scope and Deferred

Nonzero autonomous motion, PathPlanner, AutoBuilder, trajectories, path
following, pose targeting, field/alliance transforms, vision, AprilTags,
multi-step autonomous routines, hardware calibration, and gain tuning are
outside A00_L01. Real-robot verification remains `HOLD`.

## Current State

A00_L01 is `COMPLETE / FROZEN / READ-ONLY`. Its zero-motion lifecycle scope,
supplied Java/Simulation evidence, and architecture review are complete.

## Known Issues

- Real-robot A00_L01 verification remains `HOLD`; no hardware PASS is
  claimed.
- A00_L01 intentionally does not wire the lifecycle command into autonomous
  selection. That zero-motion mode-composition concept belongs to A00_L02.
