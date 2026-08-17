# Lesson Status

## Identity

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Previous Lesson: `A00_L02_AutonomousModeScheduling`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: issue one bounded nonzero robot-relative autonomous request and then retain safe Swerve ownership with the repeating zero-motion hold.
- Architecture Review: `PASS`
- Baseline Build: `PASS`
- Build: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `NOT TESTED` - supplied evidence covers Simulation/Driver Station cases; no separate Glass evidence supplied
- Real Robot: `PASS` - user-supplied A00_L03 bounded-motion and transition
  evidence
- Transition Guide: `FINAL / PASS`
- Git Commit: `NOT TESTED` - user-owned; Git not run by Codex
- Git Push: `NOT TESTED` - user-owned; Git not run by Codex

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| Direct inheritance from frozen A00_L02 | PASS | User-supplied inheritance record |
| Generated artifacts cleaned before baseline | PASS | User-supplied inheritance record |
| Java 17 verification | PASS | User-supplied verification |
| Simulation Disabled baseline | PASS | User-supplied Case 1 |
| Bounded robot-relative motion | PASS | User-supplied Case 2 |
| Automatic stop after bounded interval | PASS | User-supplied Case 2 |
| Repeating hold does not restart motion | PASS | User-supplied Case 2 |
| Joystick isolation during Autonomous | PASS | User-supplied Case 3 |
| Autonomous to Disabled safe stop | PASS | User-supplied Case 4 |
| Teleop fresh-input recovery | PASS | User-supplied Case 5 |
| Real Robot | PASS | User-supplied A00_L03 bounded-motion and transition evidence |
| Architecture Review | PASS | Final architecture review completed |
| Transition Guide | PASS | Guide finalized as FINAL / PASS |

## Real-Robot Evidence Amendment

The user supplied the following A00_L03 real-robot evidence, recorded as
`PASS` only for bounded robot-relative motion and lifecycle transitions:

1. **Disabled baseline:** `PASS`.
2. **Autonomous bounded real drivetrain motion on the floor:** `PASS`; the
   command completed, the drivetrain stopped, and motion did not restart
   while Autonomous remained enabled.
3. **Autonomous -> Disabled interruption:** `PASS`; the drivetrain stopped
   with no stale output.
4. **Autonomous -> Teleop transition:** `PASS`; autonomous ownership cleared
   and fresh Teleop control recovered normally.
5. **Autonomous -> Test transition:** `PASS`; no stale or restarted
   autonomous output appeared.

A temporary E-Stop occurred during testing. The robot was rebooted and Case 3
was rerun successfully. This event is test context and is not classified as
an A00_L03 defect.

This evidence does not claim PathPlanner, AutoBuilder, localization,
autonomous path following, or competition readiness.

## Implemented Concept

`BoundedRobotRelativeAutonomousDriveCommand` accepts a finite defensive copy
of robot-relative `ChassisSpeeds`, an explicit positive duration, and an
injected monotonic clock. It stops before motion, submits one request through
`SwerveSubsystem.acceptChassisSpeeds(...)`, monitors bounded time, fails closed
on invalid clock behavior, and stops on every termination path.

`RobotContainer` composes that command with
`AutonomousSafetyHoldCommand.repeatedly()`. The bounded command provides the
first A00 nonzero autonomous motion. The repeating hold retains Swerve
requirement ownership and prevents the default field-relative Teleop command
from resuming during the remainder of Autonomous.

The named Simulation baseline is `+0.30 m/s` forward, `0.00 m/s` lateral,
`0.00 rad/s` angular, for `1.0 s`. These are learning values only and are not
real-robot commissioning values.

## Architecture Preservation

RobotContainer remains composition-only. Robot.java, SwerveSubsystem, IO
interfaces and implementations, observation, telemetry, hardware
configuration, A00_L02, and S00 remain unchanged by this lesson’s concept.
Robot-relative autonomous control remains separate from the inherited
field-relative Teleop path.

## Scope and Deferred Work

The following remain outside A00_L03: A00_L04 Test-mode/global gating,
PathPlanner, AutoBuilder, trajectories, path following, pose targeting,
field/alliance transforms, vision, AprilTags, multi-step routines, hardware
calibration, drive tuning, and IO/telemetry schema changes.

Non-blocking technical debt includes inherited sleep-based commissioning tests,
final drivetrain tuning, no new Glass-specific behavior or evidence, and
broader real-robot capability beyond the supplied A00_L03 evidence. Separate
Glass evidence remains `NOT TESTED`.

## Final State and Known Issues

A00_L03 is `COMPLETE / FROZEN / READ-ONLY`. The final architecture review is
`PASS`, and the transition guide is `FINAL / PASS`.

- User-supplied A00_L03 real-robot bounded-motion and transition evidence is
  recorded as `PASS`; no broader hardware capability is claimed.
