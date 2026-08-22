# A01_L07 - AutoBuilder Contract Integration - Plan and Activation Record

## Activation State

- Lesson: `A01_L07_AutoBuilderContractIntegration`.
- Title: `A01_L07 - AutoBuilder Contract Integration`.
- Previous lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration - COMPLETE / FROZEN / READ-ONLY`.
- Status: `IN_PROGRESS / EDITABLE`.
- Strict inheritance: `PASS` - copied from frozen L06.
- User baseline: `PASS` - compileJava, compileTestJava, tests, and clean build.
- Architecture Review: `PASS`.
- Alliance-transform Design Lock: `PASS`.
- Production implementation: `NOT STARTED`.

Activation is complete for the documentation boundary only. No AutoBuilder
source, test, asset, IO, hardware, SwerveSubsystem, CTRE, or frozen predecessor
change is authorized by this record.

## One New Concept

Configure AutoBuilder against the repository's existing pose, reset,
measured-speed, output, PathFollowingController, RobotConfig, requirement,
alliance, and fail-closed lifecycle contracts. AutoBuilder is an integration
boundary and does not become the owner of drivetrain, localization, hardware,
IO, telemetry, or alliance transformation.

## Locked Alliance and Execution Contract

`A01/L04 FieldAllianceTransform` remains the sole alliance-transform owner.
Automatic AutoBuilder flipping is disabled: `shouldFlipPath = false` and the
fresh execution path has `preventFlipping = true`.

```text
canonical Blue PathPlannerPath
    -> L04 transformation exactly once
    -> fresh transformed execution PathPlannerPath
    -> AutoBuilder follows directly with vendor flipping disabled
```

The canonical path is never mutated. A second transformation is forbidden.

## Preserved Pre-Activation Design Contracts

The copied pre-activation record remains the detailed design reference. L07
must preserve:

1. the `Optional<Pose2d>` and `Optional<ChassisSpeeds>` to vendor callback
   mismatch through explicit, fail-closed bridges;
2. finite pose reset and robot-relative output validation;
3. measured robot-relative speed ownership by `SwerveSubsystem`;
4. exactly-once process-session AutoBuilder configuration;
5. named Constants-owned RobotConfig reuse, including provisional-value labels;
6. the existing `SwerveSubsystem` scheduler requirement;
7. a monotonic session fault latch;
8. unconditional terminal stop on success, interruption, cancellation, and
   fault; and
9. no automatic restart after mode loss or cancellation.

## Current Swerve Authority

- Drive ratio: `6.75:1`.
- FL CANcoder offset: `+0.068603515625`.
- FR CANcoder offset: `+0.014404296875`.
- BL CANcoder offset: `+0.46240234375`.
- BR CANcoder offset: `-0.057373046875`.

No CAN IDs, steer ratio, wheel radius, wheelbase, trackwidth, inversion,
CANcoder direction, current limits, PID/FF, FOC, IO, or SwerveSubsystem values
may change in the activation phase.

## Future Implementation Gate

The next authorized phase may add only the single AutoBuilder contract concept
after an implementation design review. It must independently test configuration
exactly once, pose/reset/speed/output bridges, finite validation, requirement
ownership, transform ownership, no second flip, fault latching, terminal stop,
and no restart. It must not add chooser, multiple routines, NamedCommands,
event markers, mechanism coordination, vision, AprilTags, pathfinding, or
replanning.

The future implementation phase must then run focused tests, full regression,
clean build, Simulation, Driver Station / Glass checks where applicable, and
user-owned real-robot verification before lesson completion.
