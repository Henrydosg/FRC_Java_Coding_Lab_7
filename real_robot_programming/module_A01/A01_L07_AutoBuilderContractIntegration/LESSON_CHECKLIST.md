# A01_L07 - AutoBuilder Contract Integration - Checklist

Status: `IN_PROGRESS / EDITABLE`  
Previous lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Activation

- [x] AGENTS.md, repository README, authoritative Documents A/B/C, Frozen
      Backbone, Frozen Interface Contract, A01 ADR, frozen L01-L06, L06 final
      documentation, the prepared L07 copy, and the pre-activation design record
      were reviewed.
- [x] A01/L06 predecessor is COMPLETE / FROZEN / READ-ONLY.
- [x] L07 uses strict inheritance from the frozen L06 project.
- [x] L07 directory identity is `A01_L07_AutoBuilderContractIntegration`.
- [x] Inherited generated artifacts were removed from the copied L07 only.
- [x] User supplied Java 17 baseline evidence: compileJava, compileTestJava,
      tests, and clean build PASS.
- [x] L07 is the single active lesson and is IN_PROGRESS.
- [x] Transition record `docs/A01_L06_to_A01_L07_Step_by_Step.md` exists.
- [x] Pre-activation AutoBuilder design knowledge is preserved by copied record
      and explicit cross-reference.

## Alliance and Safety Design Lock

- [x] Transform owner is `A01/L04 FieldAllianceTransform`.
- [x] AutoBuilder vendor flipping is disabled.
- [x] `shouldFlipPath` is locked to `false`.
- [x] The fresh execution path will use `preventFlipping = true`.
- [x] Canonical Blue path remains unchanged.
- [x] Exactly one alliance transform is required.
- [x] `SwerveSubsystem` remains the drivetrain requirement and stop authority.
- [x] RobotConfig is reused; provisional values remain explicitly provisional.
- [x] No automatic restart and centralized stop remain mandatory.

## Current Swerve Authority

- [x] Drive ratio remains `6.75:1`.
- [x] FL offset remains `+0.068603515625`.
- [x] FR offset remains `+0.014404296875`.
- [x] BL offset remains `+0.46240234375`.
- [x] BR offset remains `-0.057373046875`.
- [x] No CAN, CTRE, IO, SwerveSubsystem, or frozen predecessor source changed.

## Future Implementation Gates

- [ ] Add `AutoBuilderContractAdapter`.
- [ ] Invoke `AutoBuilder.configure(...)` exactly once.
- [ ] Obtain commands through `AutoBuilder.followPath(...)`.
- [ ] Add the fresh transformed execution-path factory.
- [ ] Test Optional-to-vendor callback bridges and fail-closed behavior.
- [ ] Test fault latch, terminal stop, requirement ownership, no second flip,
      and no automatic restart.
- [ ] Run focused tests, full regression, clean build, and Simulation.
- [ ] Obtain user-owned Driver Station / Glass and real-robot evidence.

## Forbidden Scope

AutoBuilder implementation before the implementation gate, chooser, multiple
routines, routine composition, NamedCommands, event markers, mechanisms,
vision, AprilTags, pathfinding, replanning, CTRE/CAN changes, IO redesign,
SwerveSubsystem redesign, telemetry changes, and L01-L06 modification.
