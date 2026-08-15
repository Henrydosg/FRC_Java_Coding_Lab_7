# ADR: A00 Autonomous Command Foundation Roadmap

- Status: APPROVED
- Date: 2026-08-16
- Scope: Post-S00 roadmap authorization and `module_A00` repository location
- Authority: Referenced by `AGENTS.md` Section 14. The repository authority order remains unchanged.

## Context

`S00_L24_PoseEstimationAndAutonomousReadiness` is complete, frozen, and
read-only. S00 officially ends at L24. No S00_L25 lesson is authorized.

The frozen L24 project provides the minimum pose, measured-speed, finite-request,
mode-transition, command-stop, and observation contracts required as a starting
point for a later autonomous-command learning module. It does not implement an
autonomous command or any path-planning or vision feature.

## Decision

Authorize the post-S00 module:

`A00 - Autonomous Command Foundation`

Authorize `real_robot_programming/module_A00/` as the independent module
location. A00_L01 shall inherit directly from the frozen
`S00_L24_PoseEstimationAndAutonomousReadiness` project. Each later A00 lesson
shall inherit from the previous A00 lesson only after that predecessor is
`COMPLETE / FROZEN / READ-ONLY`.

The locked lesson order is:

1. `A00_L01_AutonomousCommandLifecycleFoundation`
2. `A00_L02_AutonomousModeScheduling`
3. `A00_L03_BoundedRobotRelativeAutonomousMotion`
4. `A00_L04_AutonomousMotionSafetyGating`

Each lesson introduces exactly one new architectural or learning concept.

## Motion Boundary

`A00_L01` and `A00_L02` are strictly zero-motion lessons. They shall not
generate or dispatch a nonzero autonomous drivetrain request.

`A00_L03` is the first A00 lesson permitted to generate a nonzero autonomous
drivetrain request. Its motion must be bounded, robot-relative, finite, and
stopped on completion, interruption, cancellation, timeout, invalid state, or
mode transition.

## Architectural Constraints

A00 shall preserve:

- the Frozen Backbone and dependency direction;
- the existing IO interfaces and vendor boundary;
- `SwerveSubsystem` ownership of localization and actuation;
- the immutable observation boundary and read-only telemetry;
- `RobotContainer` as the composition root only;
- centralized `SwerveSubsystem.stop()` authority;
- finite-request fail-closed behavior; and
- Disabled-transition stale-intent disarm and fresh-request recovery.

A00 shall not modify `S00_L24` or any earlier frozen lesson. A00 changes belong
only in the new independent A00 projects.

## Explicit Exclusions

A00 does not authorize:

- PathPlanner or AutoBuilder;
- trajectory generation or path following;
- pose-targeted autonomous behavior;
- field or alliance transforms;
- vision or AprilTag integration;
- multi-step autonomous routines;
- hardware calibration, drive tuning, or gain changes; or
- changes to frozen S00 source, tests, contracts, or documentation.

Those subjects require separate roadmap authorization and architecture review.

## Consequences

- S00 remains complete and frozen through L24.
- A00 may be created as a separate module without renaming or rescoping an
  existing S00 lesson.
- A00_L01 may now be created after the normal inheritance, artifact-cleanup,
  baseline-build, and transition-guide workflow is applied.
- Real-robot verification remains user-owned. No hardware PASS is implied by
  this roadmap authorization.
- This ADR authorizes roadmap scope and location only; it does not authorize
  implementation changes to S00_L24 or any A00 lesson beyond its approved
  lesson scope.

## Verification Implications

Before A00_L01 is marked complete, its zero-motion behavior, command lifecycle,
subsystem requirement, stop semantics, and Simulation/Glass evidence must be
verified. Any real-robot motion remains out of scope for A00_L01 and A00_L02.
