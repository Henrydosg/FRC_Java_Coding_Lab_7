# V00_L02 Lesson Plan — AprilTag Field Layout Contract

## Current state

- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Predecessor:** `V00_L01 @ 7d52ebf`
- **Implementation:** `COMPLETE / USER VERIFIED`
- **Technical Verification:** `PASS`
- **Documentation:** `COMPLETE`
- **Final Architecture Review:** `PASS`
- **Final Closure / Freeze:** `PASS / COMPLETE`
- **Git publication:** `PENDING USER COMMIT/PUSH`

## One-concept objective

Provide an immutable vendor-neutral contract that loads an explicitly selected
official 2026 WPILib AprilTag field layout and returns canonical Blue-origin
`fieldToTag` poses by positive tag ID.

## Completed phase 1 — Authoritative reconstruction

- Confirm final A01_L09 publication at `6b243bb`.
- Confirm canonical V00_L01 publication at `7d52ebf`.
- Classify the earlier V00 lineage as stale.
- Preserve historical V00_L02 outside the active repository lineage.
- Reconstruct canonical V00_L02 from final V00_L01.
- Clean generated artifacts and verify under WPILib Java 17.
- Pass the inherited baseline build and full inherited test suite.

**Result:** PASS.

## Completed phase 2 — Architecture audit and design lock

Reviewed and locked:

- Frozen Backbone and Frozen Interface Contract preservation;
- Documents A/B/C and V00 roadmap ownership;
- package `frc.robot.vision`;
- class `AprilTagFieldLayoutContract`;
- `loadOfficial2026(FieldVariant)`;
- `getTagPose(int)`;
- explicit welded and AndyMark resource mapping;
- canonical Blue-origin `fieldToTag`;
- positive/unknown/invalid ID behavior;
- immutable snapshot ownership;
- no raw mutable layout/tag exposure;
- no `fromLayout(...)` seam; and
- no runtime camera, Observation, telemetry, drivetrain, autonomous, or fusion
  responsibility.

**Result:** PASS.

## Completed phase 3 — Controlled activation

Reconcile governance and lesson metadata so canonical V00_L02 becomes the sole
`IN_PROGRESS / EDITABLE` lesson while V00_L01 remains frozen and published.

**Result:** PASS.

## Completed phase 4 — Exact implementation

Production:

- add `AprilTagFieldLayoutContract.java`;
- map `REBUILT_WELDED` to `k2026RebuiltWelded`;
- map `REBUILT_ANDYMARK` to `k2026RebuiltAndymark`;
- load through `AprilTagFieldLayout.loadField(...)`;
- validate field dimensions and reachable tag data;
- deep-snapshot ID-to-`Pose3d` values;
- retain only a private immutable map; and
- implement deterministic positive-ID lookup.

Focused tests:

- add `AprilTagFieldLayoutContractTest.java`;
- use independent fixed numeric oracles from the installed official WPILib
  2026.2.1 resources;
- cover explicit variants, known/unknown/invalid IDs, canonical direction,
  no Red mirroring, units, right-handed rotations, deterministic ownership,
  field-variant distinction, public API, and seam absence.

**Result:** COMPLETE.

## Completed phase 5 — Authoritative verification

User verification under VS Code and WPILib Java 17:

- focused `AprilTagFieldLayoutContractTest`: PASS;
- inherited `VisionFrameTransformTest`: PASS;
- full test suite: PASS;
- clean full build: PASS;
- exact build record: `BUILD SUCCESSFUL in 24s`;
- actionable tasks: 7 executed.

Simulation, Driver Station / Glass, real robot, and physical camera remain
`NOT APPLICABLE`.

**Result:** PASS.

## Completed phase 6 — Documentation completion

- Reconcile repository lifecycle metadata.
- Reconcile lesson README, status, plan, and checklist.
- Complete the 21-step chronological transition guide.
- Complete English and Vietnamese student learning guides.
- Record exact implementation and User-owned verification evidence.
- Preserve historical states as historical rather than rewriting them.

**Result:** PASS.

## Completed phase 7 — Pre-closure audit

Confirm:

- Frozen Backbone and interface contract unchanged;
- final A01 safety/event architecture unchanged;
- V00_L01 unchanged;
- VisionFrameTransform and its test unchanged;
- exactly one production and one test file added;
- no runtime wiring;
- no dependency, configuration, or asset drift; and
- Document C observation boundary preserved.

**Result:** PASS.

## Completed phase 8 — Final read-only review and closure

1. Perform final read-only Architecture Review.
2. Obtain explicit final closure authorization.
3. Reconcile freeze metadata.
4. Mark the lesson `COMPLETE / FROZEN / READ-ONLY` only after approval.

**Result:** PASS. V00_L02 is `COMPLETE / FROZEN / READ-ONLY`.

## Pending phase 9 — User-owned publication

After final closure, the User performs Git add, commit, and push. Codex does not
perform or claim these operations.

**Current result:** PENDING USER COMMIT/PUSH.

## Stop conditions

Stop if:

- final review identifies architecture or scope drift;
- any file outside the exact two-file Java delta is found changed;
- mutable WPILib layout/tag state escapes;
- alliance transforms, inversion, camera runtime, VisionIO, Observation,
  telemetry, autonomous, Swerve, or fusion enters V00_L02;
- V00_L01 changes; or
- freeze/publication is attempted without explicit authorization.
