# V00_L02 Lesson Checklist — AprilTag Field Layout Contract

## Governance and lineage

- [x] AGENTS.md and root README read.
- [x] Authoritative English Documents A/B/C read.
- [x] Relevant ADRs reviewed.
- [x] Final A01_L09 identified as published at `6b243bb`.
- [x] Canonical V00_L01 identified as published at `7d52ebf`.
- [x] V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.
- [x] Historical stale V00_L02 distinguished from canonical reconstructed V00_L02.
- [x] A01_L10 remains prohibited.
- [x] Controlled activation historically established V00_L02 as the sole active lesson before implementation.

## Reconstruction and baseline

- [x] Historical stale V00_L02 preserved outside the active repository lineage.
- [x] Canonical V00_L02 reconstructed from final V00_L01.
- [x] Generated artifacts cleaned before baseline verification.
- [x] WPILib Java 17 used.
- [x] Baseline build PASS evidence accepted.
- [x] Full inherited baseline suite PASS evidence accepted.
- [x] Inherited production/test/config/dependency/resource baseline preserved.

## Architecture and design lock

- [x] Frozen Backbone preserved.
- [x] Frozen Interface Contract preserved.
- [x] Frozen Development Workflow followed.
- [x] Document C / Observation boundary preserved.
- [x] Package locked to `frc.robot.vision`.
- [x] Class locked to `AprilTagFieldLayoutContract`.
- [x] Public API locked to `loadOfficial2026(FieldVariant)` and
  `getTagPose(int)`.
- [x] `REBUILT_WELDED` maps to `k2026RebuiltWelded`.
- [x] `REBUILT_ANDYMARK` maps to `k2026RebuiltAndymark`.
- [x] `kDefaultField` prohibited and absent.
- [x] Canonical Blue-origin `fieldToTag` semantics preserved.
- [x] Alliance flip and Red mirroring absent.
- [x] `tagToField` inversion absent.
- [x] Unknown positive ID returns `Optional.empty()`.
- [x] Nonpositive ID throws `IllegalArgumentException`.
- [x] Raw mutable layout/tag exposure prohibited.
- [x] `fromLayout(...)` seam prohibited and absent.

## Implementation

- [x] Exact production file added:
  `AprilTagFieldLayoutContract.java`.
- [x] Exact focused test file added:
  `AprilTagFieldLayoutContractTest.java`.
- [x] Official resources loaded through current WPILib API.
- [x] Field dimensions validated.
- [x] Reachable tag IDs and poses validated.
- [x] Translation and quaternion finiteness validated.
- [x] Zero-norm quaternion rejected where reachable.
- [x] Duplicate and empty layout fail-closed branches present.
- [x] Tag poses deep-snapshotted into an immutable private map.
- [x] Raw layout and mutable tag objects discarded after loading.
- [x] No runtime wiring added.
- [x] No other production or test file changed.

## Authoritative User verification

- [x] `AprilTagFieldLayoutContractTest`: PASS.
- [x] Inherited `VisionFrameTransformTest`: PASS.
- [x] Full test suite: PASS.
- [x] Clean full build: PASS.
- [x] Build record: `BUILD SUCCESSFUL in 24s`.
- [x] Build record: 7 actionable tasks, 7 executed.
- [x] Successful verification attributed to the User, not Codex.
- [x] Earlier Codex classpath discrepancy classified as environment/process-only.

## N/A runtime gates

- [x] Simulation: NOT APPLICABLE.
- [x] Driver Station: NOT APPLICABLE.
- [x] Glass: NOT APPLICABLE.
- [x] Real Robot: NOT APPLICABLE.
- [x] Physical Camera: NOT APPLICABLE.
- [x] N/A reason recorded: immutable deterministic reference geometry only.

## Documentation and pre-closure

- [x] Repository AGENTS.md reconciled.
- [x] Repository README.md reconciled.
- [x] Lesson README.md completed.
- [x] LESSON_STATUS.md completed.
- [x] LESSON_PLAN.md completed.
- [x] LESSON_CHECKLIST.md completed.
- [x] 21-step transition guide completed for pre-closure.
- [x] English learning guide completed.
- [x] Vietnamese explanatory learning guide completed.
- [x] Exact implementation and verification record preserved.
- [x] Pre-closure architecture audit PASS.
- [x] V00_L01 protection PASS.
- [x] Dependency/configuration/asset drift: NONE.
- [x] Document C boundary PASS.

## Closure and publication gates

- [x] Final read-only Architecture Review PASS.
- [x] Final Closure Authorization PASS.
- [x] Freeze metadata update completed.
- [x] Lesson marked `COMPLETE / FROZEN / READ-ONLY`.
- [ ] User Git add.
- [ ] User Git commit.
- [ ] User Git push.
- [ ] User confirms Git publication.

## Current result

`COMPLETE / FROZEN / READ-ONLY / IMPLEMENTATION VERIFIED / DOCUMENTATION
COMPLETE / FINAL ARCHITECTURE REVIEW PASS / FINAL CLOSURE PASS / GIT
PUBLICATION PENDING USER COMMIT/PUSH`
