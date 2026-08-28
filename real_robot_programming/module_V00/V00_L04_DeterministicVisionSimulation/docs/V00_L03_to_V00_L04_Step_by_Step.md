# V00_L03 to V00_L04 - Step-by-Step Transition Guide

## Purpose and current state

This guide explains how `V00_L04_DeterministicVisionSimulation` was derived
from the authoritative published
`V00_L03_VisionIOAndImmutableObservationContract @ cc20d62`. It records the
actual controlled lifecycle, the inherited architecture, the one new L04
concept, verification evidence, the resolved local verification discrepancy,
and the remaining closure boundary.

Current state:

`COMPLETE / FROZEN / READ-ONLY / IMPLEMENTED / VERIFIED / DOCUMENTATION
COMPLETE / CLOSURE AUTHORIZED / PUBLICATION PENDING USER GIT`

V00_L04 lesson content and lifecycle state are complete, frozen, and read-only.
Git publication remains User-owned and pending. No commit hash is claimed.

## Architecture learned in this transition

V00_L03 established the vendor-neutral `VisionIO` transport contract and the
immutable `VisionObservation` contract. V00_L04 adds a deterministic
simulation implementation of the existing IO contract without changing it.

```text
V00_L02 official fieldToTag geometry
                +
V00_L04 known fieldToRobot ground truth
                +
fixed robotToCamera
                |
                v
fieldToCamera
                |
                v
cameraToTarget
                |
                v
VisionIOInputs
```

This direction is forward measurement synthesis: a known robot pose generates
camera-relative target measurements. L04 does not estimate robot pose. V00_L05
will later work in the opposite conceptual direction by consuming vision
measurements to create robot-pose candidates.

## Step 1 - Confirm final V00_L03 authority

- **Objective:** Identify the only valid predecessor.
- **Why:** Inheritance development requires the immediately preceding lesson
  to be complete, frozen, and published before a successor is prepared.
- **Action:** Confirm V00_L03 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ cc20d62`.
- **Files Changed:** None.
- **Verification:** User publication evidence and final V00_L03 documentation.
- **Expected Result:** V00_L03 is the authoritative source for V00_L04.
- **Status:** `PASS`.

## Step 2 - Copy V00_L03 to create V00_L04

- **Objective:** Preserve the accepted V00_L03 and inherited A01 architecture.
- **Why:** A lesson must extend a verified baseline rather than recreate the
  project or skip predecessor behavior.
- **Action:** The User copied V00_L03 to the roadmap-approved V00_L04 identity.
- **Files Changed:** New V00_L04 lesson directory; V00_L03 remained protected.
- **Verification:** The later inheritance audit confirmed the source lineage.
- **Expected Result:** V00_L04 begins as a true V00_L03-derived project.
- **Status:** `PASS / USER OWNED`.

## Step 3 - Remove inherited generated artifacts

- **Objective:** Keep machine-generated state out of inheritance authority.
- **Why:** Build output and local Gradle state are reproducible and must not be
  mistaken for lesson source.
- **Action:** The User removed inherited generated build artifacts.
- **Files Changed:** Generated artifacts only.
- **Verification:** User preparation evidence.
- **Expected Result:** Baseline verification starts from authoritative source.
- **Status:** `PASS / USER OWNED`.

## Step 4 - Run the inherited baseline build

- **Objective:** Prove the copied predecessor works before L04 changes.
- **Why:** A baseline failure must not be confused with a new L04 defect.
- **Action:** The User ran the inherited project under WPILib Java 17.
- **Files Changed:** Generated build output only.
- **Verification:** User-reported baseline build `PASS`.
- **Expected Result:** The inherited project is a viable L04 baseline.
- **Status:** `PASS`.

## Step 5 - Identify the initial lifecycle metadata conflict

- **Objective:** Separate copied historical metadata from current lifecycle truth.
- **Why:** A copied lesson can still describe its predecessor or an incorrect
  editable state even when the source inheritance is sound.
- **Action:** Audit repository and lesson lifecycle records before activation.
- **Files Changed:** None during the audit.
- **Verification:** The audit found preparation and metadata state needed reconciliation.
- **Expected Result:** Lifecycle inconsistencies are known before implementation.
- **Status:** `PASS`.

## Step 6 - Perform controlled metadata reconciliation

- **Objective:** Record the prepared lesson truthfully without authorizing code.
- **Why:** Only governance metadata was authorized at this stage.
- **Action:** Reconcile the prepared-state repository and lesson records.
- **Files Changed:** Authorized documentation/status metadata only.
- **Verification:** The lesson remained not editable until controlled activation.
- **Expected Result:** Preparation history and authority are explicit.
- **Status:** `PASS`.

## Step 7 - Audit inheritance and architecture

- **Objective:** Confirm the copied baseline preserves all frozen boundaries.
- **Why:** V00_L04 must not silently alter V00_L01-L03, autonomous safety,
  configuration, dependencies, or deploy assets.
- **Action:** Perform a read-only no-Git comparison and architecture review.
- **Files Changed:** None.
- **Verification:** Inheritance, roadmap scope, Frozen Backbone, Frozen
  Interface Contract, and Document C all returned `PASS`.
- **Expected Result:** The L04 baseline is trustworthy and correctly scoped.
- **Status:** `PASS`.

## Step 8 - Refine the initial Design Lock proposal

- **Objective:** Reduce L04 to one deterministic simulation concept.
- **Why:** Vision simulation can accidentally expand into clocks, vendors,
  runtime wiring, estimation, quality, timing, or fusion.
- **Action:** Refine the design to one `VisionIOSim`, one immutable current
  frame, five explicit factories, official geometry, and full-cycle overwrite.
- **Files Changed:** Design documentation only.
- **Verification:** The refined proposal excluded V00_L05-L09 responsibilities.
- **Expected Result:** Implementation has a minimal reviewable boundary.
- **Status:** `PASS`.

## Step 9 - Obtain refined Design Lock approval

- **Objective:** Secure architecture approval before implementation.
- **Why:** Design review and implementation authorization are separate gates.
- **Action:** Submit the refined design to the ChatGPT Architect.
- **Files Changed:** None.
- **Verification:** Refined Design Lock returned `APPROVED`.
- **Expected Result:** The architecture is locked but code is not yet authorized.
- **Status:** `APPROVED`.

## Step 10 - Activate V00_L04

- **Objective:** Make exactly one lesson editable.
- **Why:** Repository governance permits changes only in the sole
  `IN_PROGRESS` lesson.
- **Action:** Controlled activation recorded V00_L04 as
  `IN_PROGRESS / EDITABLE` while V00_L01-L03 remained frozen.
- **Files Changed:** Authorized lifecycle documentation.
- **Verification:** Active lesson count became exactly one.
- **Expected Result:** L04 is eligible for separately authorized implementation.
- **Status:** `PASS`.

## Step 11 - Obtain separate implementation authorization

- **Objective:** Authorize only the exact locked Java boundary.
- **Why:** Activation and Design Lock do not themselves authorize source edits.
- **Action:** Obtain authorization for `VisionIOSim.java` and
  `VisionIOSimTest.java` only.
- **Files Changed:** None until authorization was granted.
- **Verification:** Separate implementation authorization was supplied.
- **Expected Result:** No unrelated production or test file may change.
- **Status:** `APPROVED`.

## Step 12 - Implement VisionIOSim and its focused test

- **Objective:** Add deterministic vendor-neutral camera measurement synthesis.
- **Why:** Later vision lessons need reproducible IO samples without a physical
  camera or vendor dependency.
- **Action:** Implement the approved production class and focused test class.
- **Files Changed:** `VisionIOSim.java` and `VisionIOSimTest.java` only.
- **Verification:** Source review confirmed the locked API and scope.
- **Expected Result:** L04 adds one simulation adapter without changing the frozen contract.
- **Status:** `PASS`.

## Step 13 - Verify focused behavior

- **Objective:** Check the new state, geometry, ownership, and validation rules.
- **Why:** The adapter must be deterministic and safe against stale or partial data.
- **Action:** Verify all five mappings, explicit progression, geometry,
  complete overwrite, ordering, defensive ownership, and fail-atomic rejection.
- **Files Changed:** Generated verification output only.
- **Verification:** Focused behavior verification passed.
- **Expected Result:** The implemented behavior matches the approved Design Lock.
- **Status:** `PASS`.

## Step 14 - Record the initial local Gradle/classpath failure

- **Objective:** Preserve intermediate evidence without treating it as final truth.
- **Why:** A failed local environment result must be investigated and must not
  silently disappear from the educational record.
- **Action:** Record that an initial Codex-local `compileTestJava` attempt could
  not resolve inherited project classes on its Windows test classpath.
- **Files Changed:** Historical lesson documentation at that stage.
- **Verification:** The failure occurred before repository-standard test execution.
- **Expected Result:** The discrepancy remains visible for later reconciliation.
- **Status:** `HISTORICAL / LATER SUPERSEDED`.

## Step 15 - Rerun repository-standard verification

- **Objective:** Determine whether the local failure is reproducible in the
  User-owned WPILib workflow.
- **Why:** The User owns repository verification and provides authoritative evidence.
- **Action:** The User reran the WPILib Java 17 workflow.
- **Files Changed:** Generated build/test output only.
- **Verification:** The later workflow completed successfully.
- **Expected Result:** Current verification is based on the authoritative rerun.
- **Status:** `PASS / USER OWNED`.

## Step 16 - Confirm compileTestJava PASS

- **Objective:** Prove repository-standard test compilation succeeds.
- **Why:** Focused and regression tests require successful test compilation.
- **Action:** Record the User-supplied `compileTestJava` result.
- **Files Changed:** Documentation only.
- **Verification:** `PASS / exit code 0`.
- **Expected Result:** The prior classpath failure is non-reproducible.
- **Status:** `PASS`.

## Step 17 - Confirm focused VisionIOSimTest PASS

- **Objective:** Verify the new L04 contract directly.
- **Why:** Full-suite success alone does not identify whether the new concept passed.
- **Action:** Record the User-supplied focused test result.
- **Files Changed:** Documentation only.
- **Verification:** `VisionIOSimTest PASS / exit code 0`.
- **Expected Result:** The new deterministic simulation behavior is verified.
- **Status:** `PASS`.

## Step 18 - Confirm inherited vision regressions PASS

- **Objective:** Protect the L01-L03 vision contracts.
- **Why:** L04 must not break frame transforms, official field geometry,
  VisionIO transport, or immutable Observation behavior.
- **Action:** Record the User-supplied required inherited regression result.
- **Files Changed:** Documentation only.
- **Verification:** Required inherited vision regressions `PASS / exit code 0`.
- **Expected Result:** New and inherited vision behavior pass together.
- **Status:** `PASS`.

## Step 19 - Confirm full test suite PASS

- **Objective:** Check repository-wide inherited compatibility.
- **Why:** Focused vision tests cannot detect every unrelated regression.
- **Action:** Record the User-supplied full-suite result.
- **Files Changed:** Documentation only.
- **Verification:** Full test suite `PASS / exit code 0`.
- **Expected Result:** No automated regression blocker remains.
- **Status:** `PASS`.

## Step 20 - Confirm clean build PASS

- **Objective:** Verify the complete project from a clean generated state.
- **Why:** Incremental success alone is insufficient for closure readiness.
- **Action:** Record the User-supplied clean-build result.
- **Files Changed:** Generated build output only; documentation records the result.
- **Verification:** Clean build `PASS / exit code 0`.
- **Expected Result:** Repository-standard automated verification is complete.
- **Status:** `PASS`.

## Step 21 - Perform the post-implementation read-only architecture review

- **Objective:** Confirm technical and scope compliance after implementation.
- **Why:** Passing tests do not by themselves prove architecture preservation.
- **Action:** Audit API shape, state mappings, geometry, the independent
  `-2.5 m` oracle, determinism, validation, atomicity, tests, and scope.
- **Files Changed:** None during the review.
- **Verification:** Review returned `PASS`; Frozen Backbone, Frozen Interface
  Contract, Document C, and V00_L01-L03 protection passed.
- **Expected Result:** L04 is technically ready for documentation closure work.
- **Status:** `PASS`.

## Step 22 - Audit temporary and untracked artifacts

- **Objective:** Distinguish lesson content from accidental or forensic files.
- **Why:** Untracked duplicates and temporary logs must not enter the curriculum snapshot.
- **Action:** Audit the temporary compile log and the accidental V00_L03 path copy.
- **Files Changed:** None during the forensic audit.
- **Verification:** Both artifacts were classified safe for User-owned deletion.
- **Expected Result:** Cleanup scope is exact and does not alter tracked predecessor content.
- **Status:** `PASS`.

## Step 23 - Complete User-owned cleanup

- **Objective:** Remove the two approved non-curriculum artifacts.
- **Why:** The closure candidate should contain only intended lesson and governance content.
- **Action:** The User deleted the temporary forensic log and accidental
  untracked V00_L03 path copy.
- **Files Changed:** The two untracked artifacts were removed by the User.
- **Verification:** User-reported post-cleanup state contains no remaining V00_L03 modification.
- **Expected Result:** V00_L01-L03 remain protected and L04 is the only active uncommitted lesson.
- **Status:** `PASS / USER OWNED`.

## Step 24 - Reconcile documentation for final review

- **Objective:** Make the repository understandable without chat history.
- **Why:** Current lifecycle state, architecture, verification, cleanup, and
  deferred scope must agree before the final closure review.
- **Action:** Reconcile repository lifecycle records, L04 README/status/plan/
  checklist, and this transition guide. Classify the earlier classpath failure
  as `RESOLVED / SUPERSEDED / NON-REPRODUCIBLE`.
- **Files Changed:** Documentation only.
- **Verification:** Cross-document checks confirm V00_L04 remains
  `IN_PROGRESS / EDITABLE / IMPLEMENTED / VERIFIED / CLOSURE PENDING`, with
  active lesson count one and all later V00 scope deferred.
- **Expected Result:** V00_L04 is ready for the separate final Architect review.
- **Status:** `PASS / READY FOR FINAL REVIEW`.

## Step 25 - Complete the final read-only architecture and documentation review

- **Objective:** Confirm that closure authorization may be considered without
  changing implementation.
- **Why:** Final closure requires an independent review after documentation
  reconciliation and verification are complete.
- **Action:** The final read-only review checked Design Lock conformance, API,
  states, geometry, determinism, validation, atomicity, tests, inheritance,
  frozen boundaries, deferred scope, documentation, and lifecycle state.
- **Files Changed:** None during the review.
- **Verification:** The review returned `READY FOR ARCHITECT CLOSURE
  AUTHORIZATION / FINAL RESULT: PASS`.
- **Expected Result:** V00_L04 is ready for the Architect's closure decision.
- **Status:** `PASS`.

## Step 26 - Apply Architect-authorized controlled closure and freeze

- **Objective:** Record the authorized final lesson content state.
- **Why:** A lesson becomes read-only only after explicit Architect closure
  authorization; Git publication remains a separate User-owned operation.
- **Action:** Record V00_L04 as `COMPLETE / FROZEN / READ-ONLY` and set the
  active V00 lesson count to zero. Preserve Git publication as
  `PENDING USER GIT` without inventing a commit hash.
- **Files Changed:** Closure and lifecycle documentation only.
- **Verification:** Architect authorization is recorded; all required
  implementation, verification, architecture, cleanup, documentation,
  transition, and protection gates are `PASS`.
- **Expected Result:** Lesson content/state is frozen and no V00 lesson is active.
- **Status:** `PASS / CLOSURE AUTHORIZED`.

## Final student summary

V00_L04 inherited the final published V00_L03 contracts and added one
deterministic `VisionIO` simulation adapter. It uses known robot ground truth,
the fixed camera mounting transform, and official AprilTag field geometry to
generate camera-relative targets. The adapter advances only through explicit
`setFrame(...)`, completely overwrites each IO cycle, and validates new frames
before replacing the current valid frame.

The initial local classpath failure was preserved as historical evidence but
was not reproducible in the authoritative User workflow. Test compilation,
focused testing, inherited regressions, the full suite, and clean build all
passed. Architecture review and cleanup passed. Documentation is reconciled.

The remaining separate operation is:

```text
User-owned Git add/commit/push
```

Git publication is not completed by this guide. V00_L05 remains deferred and
has not been created.
