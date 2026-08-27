# V00_L01 to V00_L02 — Step-by-Step Transition Guide

## Document state

- **Current lesson:** `V00_L02_AprilTagFieldLayoutContract`
- **Current state:** `COMPLETE / FROZEN / READ-ONLY`
- **Implementation:** `COMPLETE / USER VERIFIED`
- **Documentation:** `COMPLETE`
- **Final architecture review:** `PASS`
- **Final closure/freeze:** `PASS / COMPLETE`
- **Authoritative predecessor:**
  `V00_L01_VisionCoordinateFramesAndCameraExtrinsics @ 7d52ebf`
- **Inherited A01 foundation:**
  `A01_L09_PathPlannerNamedCommandsAndEventMarkers @ 6b243bb`
- **Git publication:** `PENDING USER COMMIT/PUSH`

This chronological guide explains why the current V00_L02 was reconstructed,
what was inherited, what the lesson added, how it was verified, and which final
closure gates remain. No unrecorded command, test count, measurement, or Git
result is invented.

## Architecture classification

### Inherited from V00_L01

- complete production, test, build, dependency, resource, and deploy-asset
  baseline;
- final A01 autonomous safety and NamedCommands/Event Marker architecture;
- Frozen Backbone, Frozen Interface Contract, and Documents A/B/C;
- `VisionFrameTransform` and its focused tests; and
- V00_L01 coordinate-frame and camera-extrinsics teaching contracts.

### New in V00_L02

- `frc.robot.vision.AprilTagFieldLayoutContract`;
- `frc.robot.vision.AprilTagFieldLayoutContractTest`;
- explicit official 2026 welded/AndyMark field-layout selection;
- immutable canonical Blue-origin `fieldToTag` lookup; and
- V00_L02-specific transition and bilingual learning documentation.

### Deferred to later V00 lessons

- VisionIO and camera acquisition;
- target Observations, quality, timestamps, and latency;
- deterministic camera simulation;
- robot pose estimation;
- real camera adapter/vendor integration;
- vision measurement acceptance and fusion;
- telemetry and NetworkTables; and
- autonomous, PathPlanner, Swerve, and RobotContainer integration.

## Step 1 — Confirm final V00_L01

**Objective:** Establish the only authoritative predecessor.

**Why:** Every lesson must inherit from the immediately preceding final frozen
lesson.

**Action:** Confirm V00_L01 at `7d52ebf` is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED`, and that it descends from final A01_L09 at `6b243bb`.

**Files Changed:** None.

**Verification:** Repository governance, User publication evidence, and
predecessor metadata agree.

**Expected Result:** V00_L01 at `7d52ebf` is the canonical source baseline.

## Step 2 — Diagnose the stale historical V00_L02

**Objective:** Prevent an obsolete lesson lineage from becoming authoritative.

**Why:** Historical V00_L02 predated reconstructed final V00_L01 and therefore
did not include the accepted final A01 inheritance chain.

**Action:** Classify historical V00_L02 as stale and non-authoritative rather
than resuming its implementation.

**Files Changed:** None.

**Verification:** Governance records distinguish the historical and canonical
lineages.

**Expected Result:** Historical code/results are not treated as current proof.

## Step 3 — Preserve the historical lesson outside the repository

**Objective:** Retain historical work without contaminating the active lesson.

**Why:** History can remain useful even though it is not an authoritative
predecessor.

**Action:** The User backed up stale V00_L02 outside the repository before
replacement. This guide does not invent the backup command or location.

**Files Changed:** User-owned historical placement only; no canonical source
change in this step.

**Verification:** Stale historical implementation paths are absent from the
active lesson baseline.

**Expected Result:** Historical evidence remains separate from canonical work.

## Step 4 — Reconstruct V00_L02 from final V00_L01

**Objective:** Build the current lesson through required inheritance.

**Why:** Repository rules require copy-and-rename inheritance, not recreation
from scratch.

**Action:** Copy final V00_L01 into the V00_L02 lesson location and reconcile
lesson identity without adding the new feature.

**Files Changed:** Canonical V00_L02 project baseline created from V00_L01.

**Verification:** Before feature implementation, shared production, tests,
vendordeps, configuration, resources, and assets matched V00_L01.

**Expected Result:** V00_L02 starts as a faithful final V00_L01 snapshot.

## Step 5 — Clean generated artifacts

**Objective:** Remove stale generated state before baseline verification.

**Why:** Copied `build/` or `.gradle/` state could conceal stale outputs.

**Action:** The User removed generated/operator artifacts before the baseline
build.

**Files Changed:** Generated artifacts only.

**Verification:** Later build outputs were regenerated from the reconstructed
lesson.

**Expected Result:** Baseline evidence represents current source.

## Step 6 — Establish WPILib Java 17

**Objective:** Use the supported WPILib toolchain.

**Why:** An unsupported JDK would not satisfy the repository build gate.

**Action:** Use the WPILib 2026 Java 17 environment.

**Files Changed:** None.

**Verification:** Authoritative User baseline and final verification identify
WPILib Java 17.

**Expected Result:** Toolchain compatibility is established without changing
Gradle.

## Step 7 — Verify the inherited baseline

**Objective:** Prove reconstruction did not break the predecessor.

**Why:** New lesson work may begin only from a passing inherited baseline.

**Action:** Run the inherited baseline build and full inherited test suite.

**Files Changed:** Generated build outputs only.

**Verification:** Authoritative baseline build and inherited full-suite results
are PASS.

**Expected Result:** A healthy inherited baseline exists before V00_L02 code.

## Step 8 — Perform the architecture and inheritance audit

**Objective:** Verify frozen architecture and final A01 behavior survived
reconstruction.

**Why:** Build success alone does not prove package ownership or safety
preservation.

**Action:** Audit Frozen Backbone, Frozen Interface Contract, Documents A/B/C,
RobotContainer role, final A01 safety/event behavior, V00 roadmap order, and
predecessor protection.

**Files Changed:** None.

**Verification:** Architecture and inheritance audit result is PASS; unexpected
drift is NONE.

**Expected Result:** Design work may proceed within V00_L02 only.

## Step 9 — Audit the installed WPILib 2026.2.1 API

**Objective:** Base the contract on actual installed source and resources.

**Why:** API names, mutability, field variants, and coordinates are
version-specific.

**Action:** Inspect `AprilTagFields`, `AprilTagFieldLayout`, `AprilTag`, and
both official 2026 Rebuilt JSON resources.

**Files Changed:** None.

**Verification:** The current loader is
`AprilTagFieldLayout.loadField(AprilTagFields)`; the older enum loader is
deprecated. Layout origin and raw tag objects are mutable.

**Expected Result:** The design uses current APIs and protects mutable state.

## Step 10 — Lock the V00_L02 design

**Objective:** Define one precise lesson responsibility before implementation.

**Why:** A narrow contract prevents premature expansion into camera runtime,
Observation, estimation, or fusion.

**Action:** Lock package `frc.robot.vision`, class
`AprilTagFieldLayoutContract`, public load/lookup APIs, ID behavior, immutable
ownership, and all exclusions.

**Files Changed:** Documentation/lifecycle metadata only.

**Verification:** Design Lock review is PASS.

**Expected Result:** Future implementation has an exact two-file boundary.

## Step 11 — Activate canonical V00_L02

**Objective:** Make reconstructed V00_L02 the sole editable lesson.

**Why:** Implementation requires an active `IN_PROGRESS` lesson and explicit
authorization.

**Action:** Complete controlled lifecycle activation while preserving V00_L01
as frozen and published.

**Files Changed:** Authorized governance and V00_L02 lifecycle documentation.

**Verification:** Controlled activation result is PASS.

**Expected Result:** V00_L02 is `IN_PROGRESS / EDITABLE`; implementation still
requires a separate action.

## Step 12 — Implement AprilTagFieldLayoutContract

**Objective:** Add authoritative immutable tag-reference geometry.

**Why:** Later vision lessons need official `fieldToTag` poses without owning
a mutable WPILib layout.

**Action:** Add
`src/main/java/frc/robot/vision/AprilTagFieldLayoutContract.java`. Map welded
and AndyMark variants explicitly, load the matching official resource, validate
reachable field/tag data, snapshot ID-to-`Pose3d`, and retain only an immutable
private map.

**Files Changed:** `AprilTagFieldLayoutContract.java` only.

**Verification:** Source audit confirms approved API, explicit mappings,
canonical semantics, validation, immutable ownership, and no runtime wiring.

**Expected Result:** Positive tag IDs can be looked up deterministically without
exposing mutable layout/tag state.

## Step 13 — Add independent official-resource tests

**Objective:** Verify semantics without repeating production calculations.

**Why:** A test that obtains expected values through the production helper would
not be an independent oracle.

**Action:** Add
`src/test/java/frc/robot/vision/AprilTagFieldLayoutContractTest.java`. Use fixed
numeric examples read from installed WPILib 2026.2.1 welded and AndyMark JSON
resources. Test variants, lookup behavior, direction, mirroring, units,
rotation, determinism, ownership, API, and seam absence.

**Files Changed:** `AprilTagFieldLayoutContractTest.java` only.

**Verification:** Test source records the WPILib 2026.2.1 oracle origin and does
not call production code to calculate expected values.

**Expected Result:** The new contract has focused deterministic semantic tests.

## Step 14 — Verify the focused AprilTag contract

**Objective:** Prove the new lesson concept.

**Why:** The exact production delta requires its own focused gate.

**Action:** The User ran `AprilTagFieldLayoutContractTest` in VS Code with
WPILib Java 17.

**Files Changed:** Generated test outputs only.

**Verification:** Authoritative User result: `PASS`.

**Expected Result:** Variant mapping, lookup, frame semantics, ownership, and API
tests pass.

## Step 15 — Verify inherited VisionFrameTransform

**Objective:** Protect V00_L01 coordinate-frame behavior.

**Why:** Adding field tag geometry must not regress inherited camera-extrinsic
mathematics.

**Action:** The User ran inherited `VisionFrameTransformTest` unchanged.

**Files Changed:** None outside generated test outputs.

**Verification:** Authoritative User result: `PASS`.

**Expected Result:** V00_L01 geometry behavior remains intact.

## Step 16 — Run the full test suite

**Objective:** Detect regressions outside the focused vision package.

**Why:** One-concept work must preserve the entire inherited project.

**Action:** The User ran the full V00_L02 test suite under WPILib Java 17.

**Files Changed:** Generated test outputs only.

**Verification:** Authoritative User result: full test suite `PASS`. No test
count is invented.

**Expected Result:** The two-file delta introduces no inherited regression.

## Step 17 — Run a clean full build

**Objective:** Verify all code from clean generated state.

**Why:** Incremental success alone cannot prove clean reproducibility.

**Action:** The User ran the clean full build in the known-working VS Code /
WPILib Java 17 environment.

**Files Changed:** Generated build outputs only.

**Verification:** `BUILD SUCCESSFUL in 24s`; 7 actionable tasks, 7 executed.

**Expected Result:** Production, tests, packaging, and build lifecycle pass from
clean state.

## Step 18 — Complete documentation

**Objective:** Preserve the implemented lesson as a durable student record.

**Why:** Students must understand inheritance, design, implementation,
verification, and deferred scope without chat history.

**Action:** Reconcile repository lifecycle metadata, lesson README/status/plan/
checklist, this transition guide, and both learning guides.

**Files Changed:** Exactly the nine authorized documentation/lifecycle files.

**Verification:** Cross-document review records implementation verified,
documentation complete, and final closure pending.

**Expected Result:** Documentation is ready for final read-only review.

## Step 19 — Perform final read-only architecture review

**Objective:** Independently confirm closure readiness.

**Why:** Documentation completion does not authorize lesson closure.

**Action:** A later task must audit Frozen Backbone, interface contract,
Documents A/B/C, exact delta, runtime wiring, dependency/config/asset boundary,
and predecessor protection without modifying implementation.

**Files Changed:** None in the current task.

**Verification:** Final read-only Architecture Review: `PASS`.

**Expected Result:** Achieved; no technical, architectural, semantic,
documentation, inheritance, or scope blocker remains.

## Step 20 — Obtain closure and freeze authorization

**Objective:** Move the verified lesson into a frozen snapshot only after
explicit approval.

**Why:** `COMPLETE / FROZEN / READ-ONLY` is a separate lifecycle decision.

**Action:** After final review PASS, obtain Architect/User closure authorization
and perform the separately authorized freeze-metadata update.

**Files Changed:** Authorized repository and V00_L02 lifecycle documentation only.

**Verification:** Final Closure Review: `PASS`; freeze metadata: `COMPLETE`.

**Expected Result:** Achieved; V00_L02 is `COMPLETE / FROZEN / READ-ONLY`.

## Step 21 — Publish through the User-owned Git workflow

**Objective:** Publish the final frozen lesson.

**Why:** Git add, commit, and push are exclusively User-owned.

**Action:** After closure and freeze metadata, the User reviews the boundary and
performs Git add, commit, and push.

**Files Changed:** User-owned repository metadata after future closure.

**Verification:** `PENDING USER COMMIT/PUSH`; Codex performed no Git operation.

**Expected Result:** Publication is claimed only after User confirmation.

## Current transition conclusion

Canonical V00_L02 was reconstructed from final V00_L01, implemented through
exactly one production and one focused test file, independently verified by the
User, and fully documented. Frozen architecture and predecessor boundaries are
preserved. Final Architecture Review and Final Closure Review are PASS, and the
lesson is `COMPLETE / FROZEN / READ-ONLY`. User-owned Git publication remains
`PENDING USER COMMIT/PUSH`.
