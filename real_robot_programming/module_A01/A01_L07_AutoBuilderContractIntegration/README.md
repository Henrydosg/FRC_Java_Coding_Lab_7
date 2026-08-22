# A01_L07 - AutoBuilder Contract Integration

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L07_AutoBuilderContractIntegration`
- Title: `A01_L07 - AutoBuilder Contract Integration`
- Previous lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration - COMPLETE / FROZEN / READ-ONLY`
- Status: `IN_PROGRESS`
- Active State: `IN_PROGRESS / EDITABLE`
- Inheritance baseline: `PASS` - copied from the complete, frozen A01_L06 project.
- User baseline evidence: `PASS` - `compileJava`, `compileTestJava`, tests, and clean build.
- Architecture Review: `PASS` - approved A01 ADR and alliance-transform Design Lock.
- Implementation: `NOT STARTED` - activation only; no AutoBuilder production code exists.
- Simulation: `NOT TESTED` for L07.
- Driver Station / Glass: `NOT TESTED` for L07.
- Real Robot: `NOT TESTED` for L07; verification remains user-owned.
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

## Activation Boundary

This activation creates the single editable L07 project through strict
inheritance from frozen L06. It does not implement AutoBuilder. The following
remain deferred to the separately authorized implementation phase:

- `AutoBuilderContractAdapter`;
- `AutoBuilder.configure(...)`;
- `AutoBuilder.followPath(...)`;
- transformed execution-path construction;
- chooser, multiple routines, or routine composition;
- NamedCommands or event markers;
- mechanisms, vision, AprilTags, pathfinding, or replanning; and
- CTRE, CAN, IO, SwerveSubsystem, or frozen predecessor changes.

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
authority. The future adapter must preserve the pre-activation design record's
contracts for the `Optional` pose/speed values versus vendor-required callbacks,
finite reset/output validation, exact-once static AutoBuilder configuration,
shared RobotConfig reuse, the monotonic session fault latch, terminal stop on
every completion/interruption/fault path, and no automatic restart.

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

The supplied inherited baseline is PASS. L07 implementation, focused tests,
Simulation, Driver Station / Glass, and real-robot verification are not yet
performed. The transition guide records activation and the future
implementation gate; it is not a lesson-completion record.
