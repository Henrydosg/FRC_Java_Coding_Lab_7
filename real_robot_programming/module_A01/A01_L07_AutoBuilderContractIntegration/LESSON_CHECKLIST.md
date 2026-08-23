# A01_L07 - AutoBuilder Contract Integration - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
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
- [x] L07 was the single active lesson during implementation and is now
      COMPLETE / FROZEN / READ-ONLY.
- [x] Transition record `docs/A01_L06_to_A01_L07_Step_by_Step.md` exists.
- [x] Pre-activation AutoBuilder design knowledge is preserved by copied record
      and explicit cross-reference.

## Alliance and Safety Design Lock

- [x] Transform owner is `A01/L04 FieldAllianceTransform`.
- [x] AutoBuilder vendor flipping is disabled.
- [x] `shouldFlipPath` is locked to `false`.
- [x] The fresh execution path uses `preventFlipping = true`.
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

## Implementation Gates

- [x] Add `AutoBuilderContractAdapter`.
- [x] Invoke `AutoBuilder.configure(...)` exactly once.
- [x] Obtain commands through `AutoBuilder.followPath(...)`.
- [x] Add the fresh transformed execution-path factory.
- [x] Test Optional-to-vendor callback bridges and fail-closed behavior.
- [x] Test fault latch, terminal stop, requirement ownership, no second flip,
      and no automatic restart.
- [x] Run focused tests, full regression, and clean build.
- [x] User supplied Blue Simulation PASS: starting readiness, valid pose, and
      known one-meter autonomous execution.
- [x] User supplied Red Simulation PASS: final pose and heading are consistent
      with the exactly-one L04 alliance transform.
- [x] User supplied disable/mode-loss stop PASS.
- [x] User supplied no-automatic-restart PASS after re-enable without fresh
      readiness.
- [x] User supplied pose-validity and heading-stability PASS evidence.
- [x] User supplied Simulation telemetry/EstimatedPose evidence for the
      Driver Station / Glass boundary; this is not real-robot evidence.
- [x] User-owned L07 real-robot evidence is confirmed PASS for the current
      AutoBuilder Contract Integration lesson.

## Evidence Boundary

Simulation is `PASS / USER-SUPPLIED`, and the user-owned Real Robot gate is
`PASS`. The accumulated evidence satisfies the closure gates; L07 is now
`COMPLETE / FROZEN / READ-ONLY`.

## Final Closure

- [x] Implementation, focused tests, inherited regression, and clean build are
      PASS.
- [x] Blue and Red Simulation evidence is PASS.
- [x] Disable/mode-loss stop and no-automatic-restart evidence is PASS.
- [x] User-confirmed L07 Real Robot evidence is PASS.
- [x] No exact endpoint-accuracy, final PID/feedforward, or final physical-model
      characterization claim is made.
- [x] Transition guide is `FINAL / PASS`.
- [x] L07 is `COMPLETE / FROZEN / READ-ONLY`.

## Out-of-Scope Boundary

Chooser, multiple routines, routine composition, NamedCommands, event markers,
mechanisms, vision, AprilTags, pathfinding, replanning, CTRE/CAN changes, IO
redesign, SwerveSubsystem redesign, telemetry changes, and L01-L06 modification
remain out of scope.
