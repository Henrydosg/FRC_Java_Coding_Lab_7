# A01_L07 - AutoBuilder Contract Integration

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L07_AutoBuilderContractIntegration`
- Title: `A01_L07 - AutoBuilder Contract Integration`
- Previous lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Inheritance baseline: `PASS` - copied from the complete, frozen A01_L06 project.
- User baseline evidence: `PASS` - `compileJava`, `compileTestJava`, tests, and clean build.
- Architecture Review: `PASS` - approved A01 ADR and alliance-transform Design Lock.
- Implementation: `PASS` - the approved AutoBuilder contract adapter, execution-path factory, and RobotContainer wiring are implemented; no out-of-scope autonomous features were added.
- Simulation: `PASS / USER-SUPPLIED` - Blue and Red known one-meter execution,
  pose validity, heading stability, exactly-one alliance transform, disable
  stop, and no automatic restart were verified in Simulation.
- Driver Station / Glass: `PASS / USER-SUPPLIED SIMULATION EVIDENCE` - the
  supplied EstimatedPose and heading observations are recorded; this is not
  real-robot evidence.
- Real Robot: `PASS / USER-CONFIRMED` for the current L07 AutoBuilder
  Contract Integration lesson.
- Git Commit / Push: `NOT TESTED` - user-owned; Codex does not run Git.

## Lesson Objective

Integrate PathPlanner AutoBuilder with the existing robot contracts:

- pose supplier;
- localization reset callback;
- measured robot-relative speed supplier;
- drive output callback;
- PathFollowingController;
- shared RobotConfig;
- SwerveSubsystem requirement ownership;
- exactly-one alliance-transform policy; and
- fail-closed lifecycle behavior.

AutoBuilder remains an adapter/configuration boundary. It must not own the
drivetrain, IO, hardware, localization authority, telemetry, or alliance
transformation.

## Implementation Boundary

This lesson was activated through strict inheritance from frozen L06 and now
contains the approved single AutoBuilder contract concept. The implementation
is deliberately limited to the adapter/configuration boundary:

- `AutoBuilderContractAdapter` configures the verified Consumer overload exactly once;
- `AutoBuilder.followPath(...)` receives a fresh validated execution path;
- `PathPlannerExecutionPathFactory` performs the single L04 alliance transform;
- pose, reset, measured-speed, output, fault, lifecycle, timeout, and stop contracts are bridged fail-closed;
- chooser, multiple routines, or routine composition;
- NamedCommands or event markers;
- mechanisms, vision, AprilTags, pathfinding, or replanning; and
- CTRE, CAN, IO, SwerveSubsystem, or frozen predecessor changes.

The implementation does not add chooser or routine selection. Simulation,
supplied telemetry evidence, and the user-confirmed real-robot gate all pass.
L07 is now `COMPLETE / FROZEN / READ-ONLY`; no endpoint-accuracy, final
PID/feedforward, or final physical-model characterization claim is made.

## Locked Alliance-Transform Design

The approved owner is `A01/L04 FieldAllianceTransform`.

```text
canonical Blue PathPlannerPath
        -> L04 transformation exactly once
        -> fresh transformed execution PathPlannerPath
        -> preventFlipping = true
        -> AutoBuilder shouldFlipPath = false
        -> FollowPathCommand
        -> SwerveSubsystem
```

The canonical path remains unchanged. AutoBuilder vendor flipping is disabled,
`shouldFlipPath` is `false`, and the execution path uses
`preventFlipping = true`. Double transformation is forbidden.

## Preserved Robot Contracts

The copied L06 contracts remain authoritative. `RobotContainer` remains the
composition root, `SwerveSubsystem` remains the localization and drivetrain
requirement owner, and centralized `SwerveSubsystem.stop()` remains the safety
authority. The implemented adapter preserves the pre-activation design
record's contracts for the `Optional` pose/speed values versus vendor-required
callbacks, finite reset/output validation, exact-once static AutoBuilder
configuration, shared RobotConfig reuse, the monotonic session fault latch,
terminal stop on every completion/interruption/fault path, and no automatic
restart.

The copied pre-activation record remains at
`docs/A01_L07_AutoBuilder_Contract_Integration_PreActivation_Design_Record.md`
as historical design evidence. This README records the post-review ownership
lock that supersedes its unresolved pre-activation state.

## Current Swerve Authority

- Drive ratio: `6.75:1`
- FL CANcoder offset: `+0.068603515625`
- FR CANcoder offset: `+0.014404296875`
- BL CANcoder offset: `+0.46240234375`
- BR CANcoder offset: `-0.057373046875`

These values, the PathPlanner asset, IO architecture, CTRE configuration, and
all frozen L01-L06 source remain unchanged. RobotConfig physical values that
are marked provisional remain provisional.

## Verification Boundary

The inherited baseline and the approved L07 implementation verification are
PASS: focused L07 tests, the full 424-test suite with zero failures, and a clean
build all passed under Java 17 with `-PteamNumber=0`. The user supplied the
following Simulation evidence:

- Blue autonomous: PASS; heading reference established while Disabled,
  known starting pose accepted, valid EstimatedPose, and successful known
  one-meter execution.
- Red autonomous: PASS; final EstimatedPose was `(15.535553 m, 8.069000 m,
  -180.000000 deg)`, consistent with the locked one-transform geometry for the
  `REBUILT_WELDED` field.
- Disable/mode-loss stop: PASS; Blue stopped near `(0.400765 m, 0 m, 0 deg)`.
- No automatic restart: PASS; re-enabling without BACK, a new reset, or fresh
  readiness left the simulated robot stopped.

The Red endpoint difference is retained as Simulation geometry evidence, not
precision characterization. The user explicitly confirmed PASS for current L07
physical-robot execution. Final PID/feedforward and physical-model tuning remain
deferred. The transition guide is finalized as `FINAL / PASS`.
