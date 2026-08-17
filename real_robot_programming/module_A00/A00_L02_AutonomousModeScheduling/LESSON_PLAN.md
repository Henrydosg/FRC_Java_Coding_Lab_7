# A00_L02 Autonomous Mode Scheduling - Lesson Plan

## Lesson Metadata

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L02_AutonomousModeScheduling`
- Previous lesson: `A00_L01_AutonomousCommandLifecycleFoundation` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Freeze state: `FROZEN`
- Real robot: `PASS` for the user-supplied A00_L02 lifecycle/zero-motion
  evidence only
- Git: user-owned; not run by Codex

## Single Learning Concept

Autonomous mode composition and scheduler requirement ownership.

This lesson teaches why the selected autonomous command must own the
drivetrain subsystem for the entire mode interval. It does not add autonomous
motion.

## Problem Being Solved

The inherited `RobotContainer.getAutonomousCommand()` returned
`Commands.none()`. Since that command had no `SwerveSubsystem` requirement, the
default `FieldRelativeTeleopDriveCommand` remained eligible during Autonomous.
A finite hold that simply finished would create the same ownership gap after
its duration expired.

## Locked Design

The frozen `AutonomousSafetyHoldCommand` remains unchanged. RobotContainer
constructs it with:

1. the existing `SwerveSubsystem`;
2. the named bounded lifecycle interval
   `Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds`; and
3. `Timer::getFPGATimestamp`.

RobotContainer then returns:

```java
new AutonomousSafetyHoldCommand(...).repeatedly()
```

The repeating composition retains the child command's Swerve requirement
until external cancellation or interruption. The 1.0 second value is only a
repeat interval, not an autonomous ownership limit.

## Production Scope

Only the composition root changes for this concept:

- add the named lifecycle interval in `Constants`;
- construct and retain the repeated zero-motion autonomous command in
  `RobotContainer`; and
- return it from `getAutonomousCommand()`.

`Robot.java` remains unchanged. `SwerveSubsystem`, the IO interfaces, output
pipeline, telemetry, calibration, and inherited safety contracts remain
unchanged.

RobotContainer performs construction, injection, and command composition only.
It does not implement scheduler policy or drivetrain logic.

## Zero-Motion Contract

- No nonzero autonomous `acceptChassisSpeeds(...)` request is added.
- The wrapped command calls `stop()` at lifecycle boundaries and performs no
  actuation in `execute()`.
- Repetition does not release Swerve ownership between lifecycle intervals.
- Autonomous to Teleop, Disabled, and Test cancellation uses existing scheduler
  and command stop behavior.
- A00_L01 and A00_L02 remain zero-motion.
- A00_L03 is the first lesson permitted to issue nonzero autonomous motion.
- Test-mode global motion gating remains outside A00_L02.

## Deterministic Test Plan

`RobotContainerAutonomousModeSchedulingTest` covers:

- the returned command is the repeated zero-motion composition;
- the composition requires `SwerveSubsystem`;
- scheduling interrupts/excludes the default teleop command;
- the repeated command remains scheduled after the underlying interval expires;
- no autonomous request or nonzero final module state occurs;
- Autonomous to Teleop cancellation and fresh-request recovery;
- Autonomous to Disabled cancellation, stop, and stale-intent cleanup; and
- Autonomous to Test cancellation and stop.

The fixture uses the current `SwerveModuleIOInputs` health fields. The small
test compile correction did not change production behavior or test intent.

The inherited `AutonomousSafetyHoldCommandTest` remains a regression gate.

## Non-Blocking Technical Debt

- Inherited commissioning tests still use `Thread.sleep` timing.
- An optional stronger default-command precondition assertion could be added to
  the focused scheduler test.
- Broader real-robot capability beyond the supplied A00_L02 evidence remains
  outside this lesson.
- Test-mode global motion gating is deferred to a later lesson.

## Verification Record

| Gate | Result | Evidence |
|---|---|---|
| Direct inheritance from frozen A00_L01 | PASS | Supplied inheritance record |
| Java 17 baseline build | PASS | Supplied baseline record |
| Focused A00_L02 scheduler test | PASS | Supplied Java 17 verification |
| A00_L01 hold-command regression | PASS | Supplied Java 17 verification |
| Full Java 17 regression | PASS | Supplied Java 17 verification |
| Final clean Java 17 build | PASS | Supplied clean-build evidence |
| Simulation Disabled baseline | PASS | Supplied Simulation evidence |
| Autonomous Enabled zero-motion | PASS | Supplied Simulation evidence |
| Repeated intervals retain ownership | PASS | Supplied test/Simulation evidence |
| Nonzero joystick during Autonomous | PASS | Supplied Simulation evidence |
| Autonomous to Disabled safe stop | PASS | Supplied Simulation evidence |
| Autonomous to Teleop fresh recovery | PASS | Supplied Simulation evidence |
| Real robot | PASS | User-supplied A00_L02 lifecycle/zero-motion evidence |

### Real-Robot Verification Amendment

The user supplied and verified these five A00_L02 hardware cases:

1. **Autonomous Disabled baseline:** The drivetrain produced zero output.
2. **Autonomous + Enabled:** For approximately 7.9 seconds, the drivetrain
   remained zero-motion.
3. **Autonomous + Enabled -> Disabled:** The transition remained safely at
   zero-motion.
4. **Disabled -> Teleop Enabled:** For approximately 7.6 seconds, no stale
   autonomous drivetrain output appeared.
5. **Disabled -> Test Enabled:** For approximately 8.5 seconds, the drivetrain
   remained zero-motion.

This PASS is limited to A00_L02 autonomous composition, scheduler ownership,
and zero-motion evidence. It does not claim A00_L03/L04, pose/localization,
PathPlanner, AutoBuilder, or autonomous competition readiness.

## Explicit Exclusions

This lesson does not include nonzero autonomous motion, PathPlanner, AutoBuilder,
trajectories, path following, pose targeting, field/alliance transforms, vision,
AprilTags, multi-step routines, Test-mode global gating, hardware calibration,
gain tuning, Robot.java changes, or changes to frozen A00_L01/S00/L22.

## Completion Condition

The implementation and supplied verification satisfy the locked zero-motion
scope. The final architecture review is `PASS`, the transition guide is
`FINAL / PASS`, and A00_L02 is now `COMPLETE / FROZEN / READ-ONLY`. The
supplied A00_L02 real-robot lifecycle/zero-motion evidence is recorded as
`PASS`. A00_L03 remains the first lesson permitted to issue nonzero
autonomous drivetrain motion.
