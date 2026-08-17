# Lesson Status

## Identity

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L04_AutonomousMotionSafetyGating`
- Previous Lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Active Status: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Freeze State: `FROZEN`
- Lesson Goal: implement and verify Test/global autonomous-motion mode gating within the locked L04 scope.
- Architecture Review: `PASS`
- Implementation: `COMPLETE`
- Baseline Build: `PASS` - inherited Java 17 baseline only
- Build: `PASS`
- Java Verification: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `NOT SEPARATELY TESTED`
- Real Robot: `PASS` - supplied A00_L04 autonomous-mode safety-gating and
  lifecycle evidence
- Transition Guide: `FINAL / PASS`
- Git Commit: `NOT TESTED` - user-owned; Git not run by Codex
- Git Push: `NOT TESTED` - user-owned; Git not run by Codex

- Next Lesson: `NONE AUTHORIZED BY CURRENT A00 ROADMAP ADR`

## Activation Evidence

- A00_L03 was complete, frozen, read-only, and published to `origin/main`.
- A00_L03 was copied into A00_L04.
- Generated build artifacts were cleaned.
- `.wpilib` was initially removed accidentally.
- The baseline then failed because the WPILib team number became unavailable.
- `.wpilib` was restored from frozen A00_L03.
- The Java 17 inherited baseline was rerun successfully: `BUILD SUCCESSFUL`.
- The user supplied that only A00_L04 is untracked.

`.wpilib` must not be treated as disposable build output for this repository.
The current Gradle team-number configuration requires
`.wpilib/wpilib_preferences.json`.

## Authorized Concept and Implementation

Test/global autonomous-motion mode gating.

Safety invariant:

> Nonzero autonomous drivetrain motion is permitted only while
> `DriverStation.isAutonomousEnabled() == true`. Otherwise autonomous motion
> must fail closed through centralized drivetrain stop.

The locked implementation uses scheduler-managed WPILib composition around
the inherited bounded robot-relative motion and repeating zero-motion safety
hold. The frozen A00_L03 command is preserved.

## Disabled Scheduler Correction

The focused test now expects `assertEquals(0, subsystem.stopCount)` when the
composed autonomous command is scheduled while already Disabled. WPILib
rejects the command before initialization because its `runsWhenDisabled()` is
false. Consequently no command lifecycle method or stop branch runs, making
`stopCount == 0` correct for this scheduling attempt. The test still proves
the command is not scheduled, `acceptCount == 0`, no autonomous motion occurs,
and final module states remain zero.

## Java and Build Verification

- Focused Java 17 regression: `PASS`.
- Full Java 17 regression: `PASS`.
- Clean build: `PASS`.

## Simulation Evidence

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

## Real-Robot Evidence Amendment

The user supplied the following A00_L04 real-robot evidence, recorded as
`PASS` only for autonomous-mode safety gating and lifecycle behavior:

1. **Disabled baseline:** `PASS`.
2. **Valid Autonomous bounded motion:** `PASS`; motion completed, the
   drivetrain stopped, and motion did not restart while Autonomous remained
   enabled.
3. **Autonomous -> Disabled:** `PASS`; motion terminated safely with no stale
   output.
4. **Autonomous -> Teleop:** `PASS`; autonomous output cleared and fresh
   Teleop control recovered normally.
5. **Autonomous -> Test:** `PASS`; no stale or restarted autonomous output
   appeared.
6. **Test initial gate:** `PASS`; Test Enabled did not permit autonomous
   motion.
7. **Teleop initial gate:** `PASS`; Teleop Enabled with neutral input did not
   permit autonomous motion, while normal Teleop remained available with fresh
   input.

A temporary CommandScheduler loop-overrun observation occurred during Teleop
testing. It is recorded as an observation requiring further evidence and is
not classified as an A00_L04 defect.

This evidence is limited to A00_L04 autonomous-mode safety gating and
lifecycle behavior. It does not claim pose/localization, PathPlanner,
AutoBuilder, trajectory following, or competition readiness.

## Scope Boundaries

Explicitly out of scope:

- subsystem health-policy expansion;
- CAN/configuration fault gating;
- pose gating;
- odometry/estimator gating;
- observation freshness contracts;
- PathPlanner;
- AutoBuilder;
- trajectories;
- vision / AprilTags;
- alliance transforms;
- multi-step autonomous routines;
- drivetrain tuning;
- hardware changes; and
- Frozen Interface Contract changes.

A00_L03, A00_L02, A00_L01, and S00 remain frozen and unchanged. `Robot.java`,
`SwerveSubsystem`, IO, observation, telemetry, hardware configuration, and
Gradle remain unchanged. Only the active L04 autonomous composition and its
focused test expectation were changed during implementation.

## Remaining Non-Blocking Debt

- Driver Station / Glass verification has not been separately tested.
- Broader real-robot capability beyond the supplied A00_L04 evidence remains
  outside this lesson.
- Inherited commissioning tests retain sleep-based timing; those tests were
  not added by A00_L04.

## Final State

A00_L04 is the final lesson currently authorized by the existing A00 roadmap
ADR. It is `COMPLETE / FROZEN / READ-ONLY`. The supplied A00_L04 real-robot
autonomous-mode safety-gating and lifecycle evidence is recorded as `PASS`.
No A00_L05 is authorized by that ADR. User-owned Git commit and push remain
pending; Codex did not run Git.
