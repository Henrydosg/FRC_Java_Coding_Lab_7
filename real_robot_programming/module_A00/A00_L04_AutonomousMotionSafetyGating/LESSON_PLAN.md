# A00_L04 Autonomous Motion Safety Gating - Lesson Plan

## Lesson Metadata

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L04_AutonomousMotionSafetyGating`
- Predecessor: `A00_L03_BoundedRobotRelativeAutonomousMotion` - `COMPLETE / FROZEN / READ-ONLY`
- Active status: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Architecture Review: `PASS`
- Implementation: `COMPLETE`
- Baseline Build: `PASS` - inherited Java 17 baseline
- Build: `PASS`
- Java verification: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `NOT SEPARATELY TESTED`
- Real robot: `HOLD`
- Transition Guide: `FINAL / PASS`

A00_L04 is the final lesson currently authorized by the existing A00 roadmap
ADR. No A00_L05 is authorized by that ADR.

## Single Authorized Concept

Test/global autonomous-motion mode gating.

Safety invariant:

> Nonzero autonomous drivetrain motion is permitted only while
> `DriverStation.isAutonomousEnabled() == true`. Otherwise autonomous motion
> must fail closed through centralized drivetrain stop.

The locked implementation composes the inherited bounded robot-relative
motion with the repeating zero-motion safety hold and applies the independent
autonomous-enabled gate. The frozen A00_L03 command remains unchanged.

## Inherited Starting Point

A00_L04 was copied directly from the published frozen A00_L03 project. The
generated build artifacts were cleaned. The inherited baseline initially
failed after `.wpilib` was removed because the current Gradle configuration
could no longer obtain the WPILib team number. `.wpilib` was restored from
frozen A00_L03 and the Java 17 baseline was rerun successfully as
`BUILD SUCCESSFUL`.

`.wpilib` must not be treated as disposable build output in this repository.
The required `wpilib_preferences.json` supplies the team-number configuration
used by the current Gradle project.

## Implementation and Verification Record

- Production implementation: standard WPILib command composition; no custom
  command manually delegates another command's lifecycle.
- Authorized production change: active L04 autonomous composition only.
- Authorized test change: the Disabled initial-scheduling expectation is
  `assertEquals(0, subsystem.stopCount)`.
- Focused Java 17 regression: `PASS`.
- Full Java 17 regression: `PASS`.
- Clean build: `PASS`.

The Disabled expectation is scheduler-correct because WPILib rejects the
composed command before initialization when the robot is already Disabled and
the command has `runsWhenDisabled() == false`. No command lifecycle or stop
branch is entered, so `stopCount == 0`; unscheduled state, `acceptCount == 0`,
no motion, and final zero module states remain required safety assertions.

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

## Preserved Architecture

The following remain unchanged and frozen:

- Frozen Backbone and dependency direction.
- A00_L03, A00_L02, A00_L01, and S00 source and documentation.
- `Robot.java` and `RobotContainer.java` responsibilities.
- `SwerveSubsystem` actuation, localization, and centralized `stop()`.
- Existing IO contracts and hardware boundary.
- Immutable observation flow and read-only telemetry.
- `Robot.java`, `SwerveSubsystem`, IO, observation, telemetry, hardware
  configuration, and Gradle are unchanged. The active L04 autonomous
  composition is the only authorized production implementation change, and
  the focused scheduling test contains only the authorized Disabled
  expectation correction.

## Explicitly Out of Scope

- Subsystem health-policy expansion.
- CAN/configuration fault gating.
- Pose gating.
- Odometry or estimator gating.
- Observation freshness contracts.
- PathPlanner or AutoBuilder.
- Trajectories.
- Vision or AprilTags.
- Alliance transforms.
- Multi-step autonomous routines.
- Drivetrain tuning.
- Hardware changes.
- Frozen Interface Contract changes.

## Remaining Non-Blocking Debt

- Real-robot verification remains `HOLD`.
- Driver Station / Glass has no separate verification evidence.
- Inherited commissioning tests retain sleep-based timing; those tests were
  not added by A00_L04.

## Review State

Implementation and current verification are complete for the authorized L04
scope. A00_L04 is `COMPLETE / FROZEN / READ-ONLY`; Architecture Review is
`PASS`; and the Transition Guide is `FINAL / PASS`. Real-robot verification
remains `HOLD`.
