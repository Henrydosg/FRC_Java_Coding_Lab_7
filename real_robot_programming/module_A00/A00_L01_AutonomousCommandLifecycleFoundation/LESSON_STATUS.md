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
- Real Robot: `PASS` - user-supplied A00_L01 lifecycle/zero-motion hardware
  evidence
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
| Real Robot | PASS | User-supplied A00_L01 lifecycle/zero-motion hardware evidence |
| Transition Guide | PASS | Final guide records the complete S00_L24 -> A00_L01 evolution |
| Git Commit | NOT TESTED | User-owned; Git not run by Codex |
| Git Push | NOT TESTED | User-owned; Git not run by Codex |

## Real-Robot Evidence Amendment

The user supplied the following A00_L01 hardware evidence, recorded as
`PASS` only for lifecycle and zero-motion behavior:

1. **Disabled baseline:** The robot was Disabled and the drivetrain remained
   stationary. Drive and steer applied outputs and velocities were zero, and
   module/gyro connectivity and configuration were healthy.
2. **Autonomous + Enabled zero-motion:** Driver Station Autonomous + Enabled
   was held for approximately 51 seconds. Drive and steer applied outputs and
   velocities remained zero, with no autonomous drivetrain motion observed.
3. **Autonomous -> Disabled:** The drivetrain remained at zero and no stale
   output reappeared.
4. **Autonomous -> Teleoperated:** After transitioning through Disabled into
   Teleop Enabled, neutral driver input produced zero drive/steer output and
   no autonomous output persisted.
5. **Autonomous -> Test:** After transitioning through Disabled into Test
   Enabled, no test or commissioning command was intentionally activated; no
   autonomous drivetrain motion persisted and zero-motion safety was
   preserved.

This amendment does not claim A00_L02 scheduler ownership or repeating
autonomous ownership, A00_L03 bounded autonomous motion, A00_L04 mode-gating
verification, pose/odometry/estimator verification, PathPlanner, AutoBuilder,
or autonomous competition readiness.

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
outside A00_L01. No broader real-robot capability is claimed beyond the
recorded A00_L01 lifecycle/zero-motion evidence.

## Current State

A00_L01 is `COMPLETE / FROZEN / READ-ONLY`. Its zero-motion lifecycle scope,
supplied Java/Simulation evidence, and architecture review are complete.

## Known Issues

- User-supplied A00_L01 lifecycle/zero-motion real-robot evidence is recorded
  as `PASS`; no broader hardware capability is claimed.
- A00_L01 intentionally does not wire the lifecycle command into autonomous
  selection. That zero-motion mode-composition concept belongs to A00_L02.
