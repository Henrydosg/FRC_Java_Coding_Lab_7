# A00_L01 - Autonomous Command Lifecycle Foundation

## Lesson State

- Module: `A00 - Autonomous Command Foundation`
- Lesson: `A00_L01_AutonomousCommandLifecycleFoundation`
- Previous lesson: `S00_L24_PoseEstimationAndAutonomousReadiness` -
  `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Real robot: `PASS` for the user-supplied A00_L01 lifecycle/zero-motion
  evidence only
- Git: user-owned; not run by Codex

This project is the first post-S00 lesson. It inherits the frozen S00_L24
project directly and introduces one new concept: autonomous command lifecycle
and stop ownership.

The reusable architecture abstraction for later A00 lessons is documented in
[A00 Robot Autonomous Architecture Layers](docs/A00_Robot_Autonomous_Architecture_Layers.md).

## Learning Objective

Establish a production-useful, zero-motion command boundary for future
autonomous drivetrain commands. The lesson proves command requirements,
bounded lifecycle, deterministic time handling, interruption behavior, and
centralized drivetrain stop ownership without adding autonomous motion.

## Inherited Architecture

The frozen architecture remains:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

The observation path remains:

```text
hardware or simulation IOInputs
-> SwerveSubsystem
-> immutable observation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

SwerveSubsystem continues to own localization and actuation. RobotContainer
remains the composition root. Telemetry remains read-only, observation models
remain immutable and vendor-neutral, and vendor APIs remain confined to real
IO.

## Production Implementation

`AutonomousSafetyHoldCommand` is the bounded zero-motion lifecycle command.
It:

- requires `SwerveSubsystem`;
- accepts an explicit finite positive duration;
- uses an injected monotonic `DoubleSupplier` clock for deterministic timing;
- calls `SwerveSubsystem.stop()` from `initialize()`;
- performs no drivetrain request, IO access, or telemetry logic in
  `execute()`;
- finishes when the bounded duration expires;
- fails closed and finishes for invalid, backward, nonfinite, or throwing
  clock behavior;
- returns `runsWhenDisabled() == false`;
- calls `stop()` from both `end(false)` and `end(true)`.

No nonzero `acceptChassisSpeeds(...)` request is issued by A00_L01.
The subsystem remains the only owner of drive stopping.

## A00 Motion Boundary

A00_L01 and A00_L02 are strictly zero-motion lessons. A00_L03 is the first
lesson permitted to generate a nonzero autonomous drivetrain request. A00_L01
does not wire `AutonomousSafetyHoldCommand` into Robot autonomous selection;
mode scheduling and composition belong to A00_L02.

## Verification Evidence

The supplied verification record is:

- direct inheritance from frozen S00_L24: `PASS`;
- generated-artifact cleanup: `PASS`;
- Java 17 baseline build before implementation: `PASS`;
- focused `AutonomousSafetyHoldCommandTest`: `PASS`;
- full Java 17 regression: `PASS`;
- final Java 17 clean build: `PASS`;
- Simulation Disabled baseline: `PASS`;
- Autonomous Enabled zero-motion/non-regression: `PASS`;
- Teleop fresh-input recovery after Autonomous/Disabled transition:
  `PASS`;
- A00_L01 real-robot lifecycle/zero-motion verification: `PASS`.

### Real-Robot Verification Amendment

The user supplied the following A00_L01 hardware evidence, recorded as
`PASS` for this lesson's lifecycle and zero-motion scope:

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

This evidence is limited to A00_L01 lifecycle and zero-motion hardware
behavior. It does not verify A00_L02 scheduler ownership or repeating
autonomous ownership, A00_L03 bounded motion, A00_L04 mode gating,
pose/odometry/estimator behavior, PathPlanner, AutoBuilder, or autonomous
competition readiness.

The command is intentionally not dashboard-registered or selected by
RobotContainer in this lesson.

## Explicitly Out of Scope

A00_L01 does not add:

- nonzero autonomous motion;
- PathPlanner or AutoBuilder;
- trajectory generation, path following, or pose-targeting;
- field or alliance transforms;
- vision or AprilTag integration;
- multi-step autonomous routines;
- hardware calibration, drive tuning, or gain changes;
- Robot.java mode composition changes;
- RobotContainer autonomous wiring or business logic;
- changes to frozen S00_L24, L22, the Frozen Backbone, or IO contracts.

## Next Lesson

A00_L02 introduces autonomous mode scheduling while preserving the zero-motion
boundary. A00_L03 remains the first permitted nonzero-motion lesson.

## Finalization State

A00_L01 is `COMPLETE / FROZEN / READ-ONLY`. The Frozen Backbone and interface
contracts are preserved. The recorded real-robot PASS is limited to the
user-supplied A00_L01 lifecycle/zero-motion evidence. A00_L02 is the next
lesson and remains zero-motion; A00_L03 remains the first lesson permitted
to issue nonzero autonomous drivetrain motion.
