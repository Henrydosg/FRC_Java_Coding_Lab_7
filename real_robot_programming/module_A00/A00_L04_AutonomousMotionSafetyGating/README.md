# A00_L04 - Autonomous Motion Safety Gating

## Lesson State

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L04_AutonomousMotionSafetyGating`
- Active status: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Predecessor: `A00_L03_BoundedRobotRelativeAutonomousMotion` - `COMPLETE / FROZEN / READ-ONLY`
- Architecture Review: `PASS`
- Implementation: `COMPLETE`
- Baseline Build: `PASS` - inherited Java 17 baseline
- Build: `PASS`
- Java verification: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `NOT SEPARATELY TESTED`
- Real robot: `HOLD`
- Transition Guide: `FINAL / PASS`
- Git: user-owned; not run by Codex

A00_L04 is the final lesson currently authorized by the existing A00 roadmap
ADR. It inherits the published, frozen A00_L03 project and preserves the Frozen Backbone,
centralized Swerve stop authority, existing IO contracts, observation flow,
and read-only telemetry. No A00_L05 is authorized by that ADR.

## Authorized Concept

The one authorized L04 concept is:

**Test/global autonomous-motion mode gating.**

Safety invariant:

> Nonzero autonomous drivetrain motion is permitted only while
> `DriverStation.isAutonomousEnabled() == true`. Otherwise autonomous motion
> must fail closed through centralized drivetrain stop.

The implementation uses standard WPILib command composition around the
inherited bounded motion and repeating zero-motion safety hold. It does not
claim any additional safety policy.

## Verification Evidence

The user supplied the following Java and build evidence:

- Focused Java 17 regression: `PASS`.
- Full Java 17 regression: `PASS`.
- Clean build: `PASS`.

The user supplied the following Simulation evidence:

1. Disabled baseline: `PASS` - zero drive/steer output and velocity.
2. Autonomous + Enabled: `PASS` - inherited bounded `+0.30 m/s`
   robot-relative motion occurs, stops after approximately `1.0 s`, and does
   not restart during the repeating safety hold.
3. Autonomous -> Teleop during motion: `PASS` - motion terminates
   immediately; neutral Teleop remains stopped.
4. Autonomous -> Disabled during motion: `PASS` - motion stops and disarms;
   no stale request remains.
5. Test gating: `PASS` - Test mode permits no autonomous motion; Autonomous
   -> Test during motion terminates motion and does not restart.

## Disabled Scheduler Expectation

When the composed autonomous command is scheduled while already Disabled,
WPILib rejects it before command initialization because the command does not
run when disabled. Therefore `stopCount == 0` is correct for that initial
Disabled scheduling attempt: no command lifecycle method or stop branch is
entered. The focused test also preserves the assertions that the command is
not scheduled, `acceptCount == 0`, no autonomous motion occurs, and final
module states remain zero.

## Inherited Baseline Evidence

The user supplied the following inheritance evidence:

- A00_L03 was `COMPLETE / FROZEN / READ-ONLY` and published to `origin/main`.
- A00_L03 was copied into A00_L04.
- Generated build artifacts were cleaned.
- `.wpilib` was initially removed accidentally, causing the baseline to fail
  because the WPILib team number became unavailable.
- `.wpilib` was restored from frozen A00_L03.
- The Java 17 baseline was rerun successfully: `BUILD SUCCESSFUL`.
- Only A00_L04 was untracked according to the user-supplied evidence.

For this repository, `.wpilib` is not disposable build output. Its
`wpilib_preferences.json` is required by the current Gradle team-number
configuration and must be preserved or restored during lesson inheritance.

## Explicitly Out of Scope

- Subsystem health-policy expansion.
- CAN/configuration fault gating.
- Pose gating.
- Odometry or estimator gating.
- Observation freshness contracts.
- PathPlanner.
- AutoBuilder.
- Trajectories.
- Vision or AprilTags.
- Alliance transforms.
- Multi-step autonomous routines.
- Drivetrain tuning.
- Hardware changes.
- Frozen Interface Contract changes.

The following remain frozen and read-only: A00_L03, A00_L02, A00_L01, and S00.
`Robot.java`, `SwerveSubsystem`, IO, observation, telemetry, hardware
configuration, and Gradle remain unchanged. The L04 implementation change was
limited to the active L04 autonomous composition and its focused
scheduling-test expectation; this documentation normalization changes
documentation only.

## Remaining Non-Blocking Debt

- Real-robot verification remains `HOLD`.
- Driver Station / Glass has no separate verification evidence.
- Inherited commissioning tests retain sleep-based timing; those tests were
  not added by A00_L04.

## Current Verification State

The focused and full Java 17 regressions, clean build, and L04 Simulation are
recorded as passed from user-supplied evidence. Driver Station / Glass has not
been separately tested. Real-robot verification remains `HOLD`. A00_L04 is
`COMPLETE / FROZEN / READ-ONLY`, with the Transition Guide `FINAL / PASS`.
