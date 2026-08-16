# A00_L01 Autonomous Command Lifecycle Foundation - Lesson Plan

## Lesson Metadata

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L01_AutonomousCommandLifecycleFoundation`
- Previous: `S00_L24_PoseEstimationAndAutonomousReadiness` -
  `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Real robot: `HOLD`
- Git: user-owned; not run by Codex

## Single Learning Concept

Autonomous command lifecycle and stop ownership.

This lesson creates a useful safety boundary for future autonomous commands
without creating autonomous motion. The command lifecycle is independently
testable and uses the existing subsystem stop authority.

## Locked Architecture

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

The command layer coordinates the subsystem. It does not access IO, estimator
internals, telemetry, or vendor APIs. RobotContainer remains composition-only.
SwerveSubsystem remains the owner of actuation and centralized `stop()`.

## Production Scope

Add `frc.robot.commands.AutonomousSafetyHoldCommand` with:

1. A required `SwerveSubsystem`.
2. An explicit finite positive duration.
3. An injected monotonic clock for deterministic timing.
4. `initialize()` capturing the validated start time and calling
   `SwerveSubsystem.stop()`.
5. `execute()` containing no drivetrain request, IO access, or telemetry
   logic.
6. Completion at bounded-duration expiry.
7. Fail-closed completion for invalid, backward, nonfinite, or throwing clock
   behavior.
8. `runsWhenDisabled() == false`.
9. `end(false)` and `end(true)` both calling `SwerveSubsystem.stop()`.

The implementation must not call nonzero `acceptChassisSpeeds(...)`, mutate
pose or sensor state, or create a generic autonomous framework.

## Zero-Motion Boundary

A00_L01 and A00_L02 are zero-motion by governance lock. A00_L03 is the first
lesson permitted to issue a nonzero autonomous drivetrain request. This lesson
does not change `Robot.java`, `RobotContainer.java`, autonomous selection,
or mode composition; those concerns belong to A00_L02.

## Deterministic Test Plan

`AutonomousSafetyHoldCommandTest` proves:

- subsystem requirement ownership;
- `runsWhenDisabled() == false`;
- initialization stops the drivetrain;
- execute performs no chassis-speed request;
- final module states remain zero;
- normal bounded completion calls `end(false)` and stops;
- cancellation and scheduler interruption call `end(true)` and stop;
- Disabled scheduling does not execute motion behavior;
- invalid, nonfinite, throwing, or backward clock behavior fails closed;
- pose and module sensor state are not mutated;
- later valid requests operate after command termination;
- no Thread.sleep-based timing is used.

## Verification Record

| Gate | Result | Evidence |
|---|---|---|
| Inheritance from frozen S00_L24 | PASS | User-supplied baseline |
| Java 17 baseline build | PASS | User-supplied baseline |
| Focused command test | PASS | User-supplied Java 17 verification |
| Full regression | PASS | User-supplied Java 17 verification |
| Final Java 17 clean build | PASS | User-supplied clean-build evidence |
| Simulation Disabled baseline | PASS | User-supplied Simulation evidence |
| Autonomous Enabled zero-motion | PASS | User-supplied Simulation evidence |
| Teleop fresh-input recovery | PASS | User-supplied Simulation evidence |
| Real robot | HOLD | No A00_L01 hardware evidence supplied |

## Explicit Exclusions

The lesson does not include nonzero autonomous motion, PathPlanner,
AutoBuilder, trajectories, path following, pose targeting, field/alliance
transforms, vision, AprilTags, autonomous routines, hardware calibration,
gain tuning, Robot mode changes, RobotContainer mode composition, or changes
to frozen S00_L24/L22.

## Completion Condition

The implementation and supplied verification are complete for the locked
zero-motion scope. A00_L01 is now `COMPLETE / FROZEN / READ-ONLY`. A00_L02
must preserve the zero-motion boundary, and A00_L03 remains the first lesson
permitted to issue nonzero autonomous drivetrain motion.
