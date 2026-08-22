# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L07_AutoBuilderContractIntegration`
- Title: `A01_L07 - AutoBuilder Contract Integration`
- Previous Lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `IN_PROGRESS`
- Active State: `IN_PROGRESS / EDITABLE`
- Freeze State: `NOT FROZEN`
- Lesson Goal: integrate AutoBuilder against the existing pose, reset, measured-speed, output, controller, RobotConfig, requirement, alliance, and safety contracts.

## Activation and Baseline Gates

- Governance: `PASS` - AGENTS.md, README, Documents A/B/C, the approved A01 ADR,
  frozen L01-L06, the pre-activation record, and the alliance-transform Design
  Lock were reviewed.
- Directory Identity: `PASS` - `A01_L07_AutoBuilderContractIntegration`.
- Strict Inheritance: `PASS` - copied from complete/frozen A01_L06; production
  Java and tests remain hash-identical to the L06 inheritance baseline.
- Generated Artifact Removal: `PASS` - inherited generated artifacts were
  removed from the copied L07 only.
- Baseline Build: `PASS` - user supplied Java 17 baseline evidence.
- `compileJava`: `PASS` - user supplied.
- `compileTestJava`: `PASS` - user supplied.
- Tests: `PASS` - user supplied.
- Clean Build: `PASS` - user supplied.
- Architecture Review: `PASS` - approved A01 ADR and alliance-transform Design
  Lock; ADR change not required.
- Documentation Activation: `PASS` - L07 identity and activation documents
  are present; transition record created.

## Implementation and Verification Gates

- Production Implementation: `NOT STARTED` - activation only.
- Focused L07 Tests: `NOT TESTED`.
- Full Regression After L07 Delta: `NOT TESTED` - no L07 production delta exists.
- Build After L07 Delta: `NOT TESTED` - no L07 production delta exists.
- Simulation: `NOT TESTED`.
- Driver Station / Glass: `NOT TESTED`.
- Real Robot: `NOT TESTED / HOLD` - user-owned and not authorized by activation.
- Transition Guide: `IN_PROGRESS` - activation record complete; final lesson
  closure remains pending implementation and verification.
- Git Commit: `NOT TESTED` - user-owned; Codex does not run Git.
- Git Push: `NOT TESTED` - user-owned; Codex does not run Git.

## Locked Architecture

- Transform owner: `A01/L04 FieldAllianceTransform`.
- AutoBuilder vendor flipping: `DISABLED`.
- `shouldFlipPath`: `false`.
- Execution path: fresh transformed `PathPlannerPath` with
  `preventFlipping = true`.
- Canonical path: Blue-frame and unchanged.
- Double transformation: forbidden.
- Requirement owner: existing `SwerveSubsystem`.
- Terminal safety: centralized `SwerveSubsystem.stop()` on every terminal and
  fault path, with no automatic restart.
- RobotConfig: reuse the existing named, provisional learning configuration.
- RobotContainer: composition root only.

## Current Swerve Authority

- Drive ratio: `6.75:1`.
- FL CANcoder offset: `+0.068603515625`.
- FR CANcoder offset: `+0.014404296875`.
- BL CANcoder offset: `+0.46240234375`.
- BR CANcoder offset: `-0.057373046875`.

## Known Issues and Deferred Scope

- No AutoBuilder implementation exists yet.
- The copied pre-activation design record remains historical evidence; its
  unresolved pre-activation state is superseded by the approved Design Lock
  recorded above.
- L06 post-recalibration Blue and Red one-meter autonomous execution is now
  recorded as user-supplied PASS evidence. Exact endpoint precision and final
  PID/feedforward or physical-model tuning remain deferred; L06 remains frozen
  and is not modified by this L07 implementation state.
- Chooser, multiple routines, NamedCommands, event markers, mechanisms, vision,
  AprilTags, pathfinding, replanning, CTRE changes, CAN changes, and source
  changes to L01-L06 remain excluded.
