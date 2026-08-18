# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L02_PoseTargetedAutonomousMotion`
- Previous Lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: closed-loop movement toward one finite field-relative target pose.
- Architecture Review: `PASS`
- Architecture Audit: `PASS`
- Design Lock: `PASS`
- Implementation: `COMPLETE`
- Baseline Build: `PASS` - inherited A01_L01 baseline verified before L02 activation
- Build: `PASS` - production compile and clean build in the user's WPILib Java 17 environment
- Java Verification: `PASS` - `compileTestJava` passed in the user's WPILib Java 17 environment
- Full Tests: `PASS` - 373 tests passed; 0 failures, errors, or skips
- Full Build: `PASS` - clean build passed
- Simulation: `PASS` - user-supplied end-to-end evidence recorded below
- Driver Station / Glass: `PASS` - user-supplied NT4, telemetry, and Field2d evidence recorded below
- Real Robot: `PASS` - user-supplied L02 scope evidence recorded below
- Transition Guide: `FINAL / PASS`
- Git Commit: `NOT TESTED` - user-owned; not run by Codex
- Git Push: `NOT TESTED` - user-owned; not run by Codex
- Known Issues: no L02 defect established. The expected tolerance-completion position near X=0.370 m is valid for the configured 0.030 m translation tolerance. L03+ capabilities remain deferred.

## Implemented Architecture

`PoseTargetedAutonomousMotionCommand` requires `SwerveSubsystem`, uses
`getEstimatedPose()` feedback, and drives one finite field-relative target.
It calculates field-frame X/Y error and wrapped heading error, applies bounded
proportional translation/heading control, and submits only field-relative
`ChassisSpeeds` through `acceptFieldRelativeChassisSpeeds(...)`. The subsystem
retains field-to-robot conversion, localization, actuation, and centralized
`stop()` authority.

The command validates immutable target/configuration values before runtime.
At runtime, unavailable or invalid estimated-pose observations, nonfinite
feedback/output/time, backward time, timeout, and Autonomous-mode loss fail
closed through `SwerveSubsystem.stop()`. `end(...)` also always stops.

Translation and heading output suppression are evaluated independently every
cycle and are not latched. Completion requires translation and heading to be
within tolerance simultaneously. The inherited L01 accepted-reset token is
consumed once at autonomous dispatch; without a fresh accepted Disabled reset,
the composition selects a stop-only branch and does not restart automatically.

## Locked Configuration

| Item | Value |
|---|---:|
| Starting pose | `(0.00 m, 0.00 m, 0 deg)` |
| Target pose | `(0.40 m, 0.00 m, 0 deg)` |
| Translation kP | `1.0 s^-1` |
| Heading kP | `1.0 s^-1` |
| Maximum translation speed | `0.20 m/s` |
| Maximum angular speed | `0.35 rad/s` |
| Translation tolerance | `0.030 m` |
| Heading tolerance | `2.0 deg` |
| Timeout | `4.0 s` |

Stopping at approximately X=`0.370 m` leaves approximately `0.030 m`
translation error and is therefore within the locked completion contract. It is
not recorded as a 3 cm accuracy defect.

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| Focused command tests | PASS | `PoseTargetedAutonomousMotionCommandTest`: 10/10 passed. |
| Scheduler/readiness tests | PASS | `RobotContainerAutonomousModeSchedulingTest`: 17/17 passed. |
| Simulation integration tests | PASS | `SwerveSimulationIntegrationTest`: 2/2 passed. |
| Full regression | PASS | 373 tests passed; 0 failures, errors, or skips. |
| Clean build | PASS | User-supplied clean build passed. |
| Simulation | PASS | Disabled Back/View heading capture, valid pose/estimate, accepted reset, production dispatch, convergence near X=0.370 m, immediate disable stop, no restart without reset, and a fresh-reset second session passed. |
| Driver Station / Glass | PASS | Glass/NT4 connection to `10.109.51.2`, full telemetry, and Field2d visualization passed. |
| Real Robot | PASS | Accepted Disabled reset enabled one target run; disable stopped immediately; no-reset and no-restart cases stayed stopped; repeatable target runs stopped near X=0.370 m with no unexpected strafe or rotation. |
| Transition Guide | FINAL / PASS | `docs/A01_L01_to_A01_L02_Step_by_Step.md` finalized with inheritance, implementation, and verification evidence. |
| User Git commit | NOT TESTED | User-owned. |
| User Git push | NOT TESTED | User-owned. |

## Frozen Boundaries and Deferred Scope

- A01_L01 remains `COMPLETE / FROZEN / READ-ONLY`.
- Frozen Backbone, Frozen Interface Contract, A00_L04 safety invariant, and
  `SwerveSubsystem.stop()` authority remain unchanged.
- `SwerveSubsystem`, IO, observation contracts, telemetry, kinematics/output
  pipeline, Gradle, vendordeps, and hardware configuration are unchanged.
- No trajectory generation/sampling/following, PathPlanner, AutoBuilder,
  alliance transforms, vision/AprilTags, multi-waypoint logic, mechanism
  events, drivetrain retuning, or new localization architecture entered L02.
- L03 and later remain unauthorized until separately activated.
