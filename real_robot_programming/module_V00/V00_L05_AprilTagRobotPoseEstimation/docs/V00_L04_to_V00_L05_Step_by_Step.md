# V00_L04 to V00_L05 - Step-by-Step Transition Guide

## Purpose and current state

This guide explains how V00_L05 was prepared from the final authoritative
V00_L04 lesson, implemented, verified, and closed. It is written for a student
returning to the project later.

The official lesson identity is:

```text
V00_L05 - AprilTag Robot Pose Estimation
V00_L05_AprilTagRobotPoseEstimation
```

Current state:

```text
DESIGN LOCK: APPROVED
LESSON: COMPLETE / FROZEN / READ-ONLY
IMPLEMENTATION: COMPLETE
TEST-ONLY NONCOMMUTATIVITY FIXTURE REPAIR: COMPLETED
POST-REPAIR USER VERIFICATION: PASS / WPILib Java 17
POST-HARDENING USER VERIFICATION: PASS / WPILib Java 17
FINAL ARCHITECT CLOSURE: APPROVED / COMPLETE
ACTIVE V00 LESSON COUNT: 0
GIT PUBLICATION: PENDING USER GIT PUBLICATION
```

The inherited `V00_L03_to_V00_L04_Step_by_Step.md` remains historical L04
documentation and was not modified.

## The architectural idea

V00_L04 performs forward measurement synthesis:

```text
known fieldToRobot
    + robotToCamera
    + fieldToTag
    -> cameraToTarget
```

V00_L05 is the opposite mathematical direction:

```text
fieldToTag
    + cameraToTarget
    + robotToCamera
    -> canonical fieldToRobot robot-pose candidate
```

The result is only a structurally valid pose candidate. It is not a quality-
approved measurement, timestamped measurement, fused estimator pose, or
permission to mutate odometry.

## Step 1 - Confirm final V00_L04 authority

- **Objective:** Identify the only valid predecessor.
- **Why:** A successor must inherit from the immediately preceding completed,
  frozen, and published lesson.
- **Action:** Confirm V00_L04 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
  5461555 / USER VERIFIED`.
- **Files Changed:** None.
- **Verification:** User-supplied V00_L04 lifecycle and publication evidence.
- **Expected Result:** V00_L04 is the authoritative L05 parent.
- **Status:** `PASS`.

## Step 2 - Copy the final L04 project into the L05 candidate

- **Objective:** Preserve the final L04 source and frozen architecture while
  preparing the next independent project.
- **Why:** Lesson inheritance uses copy/rename rather than reconstruction from
  scratch.
- **Action:** The User copied V00_L04 into an untracked L05 candidate.
- **Files Changed:** New untracked candidate directory only.
- **Verification:** User preparation evidence.
- **Expected Result:** The candidate begins as a faithful L04 snapshot.
- **Status:** `PASS / USER OWNED`.

## Step 3 - Remove copied generated artifacts

- **Objective:** Keep generated machine state out of the inherited source
  baseline.
- **Why:** Build output and local Gradle state are reproducible and are not
  lesson authority.
- **Action:** The User removed copied generated artifacts from the candidate.
- **Files Changed:** Generated artifacts in the candidate only.
- **Verification:** User preparation evidence.
- **Expected Result:** Baseline verification starts from lesson source and
  configuration rather than copied output.
- **Status:** `PASS / USER OWNED`.

## Step 4 - Run the inherited baseline build

- **Objective:** Prove the copied project builds before L05 work.
- **Why:** A baseline failure must not be confused with an L05 implementation
  failure.
- **Action:** The User ran the inherited WPILib Java 17 baseline workflow.
- **Files Changed:** Generated build output only.
- **Verification:** User supplied `BUILD SUCCESSFUL` evidence.
- **Expected Result:** The inherited L05 candidate is technically buildable.
- **Status:** `PASS / USER OWNED`.

## Step 5 - Record the preparation facts

- **Objective:** Establish an auditable inheritance baseline.
- **Why:** A copied directory must be compared against its authoritative parent
  before activation.
- **Action:** Record the prior read-only comparison: 229 comparable
  non-generated files, zero differences, 74 identical production Java files,
  61 identical test Java files, unchanged build/config/wrapper, vendordeps,
  deploy/resources/PathPlanner content, and inherited documentation.
- **Files Changed:** None during the audit.
- **Verification:** Prior repository-aware inheritance audit.
- **Expected Result:** No implementation or architectural drift exists.
- **Status:** `PASS`.

## Step 6 - Record User-owned Git status evidence

- **Objective:** Distinguish the prepared candidate from published repository
  history.
- **Why:** Git publication and lifecycle activation are separate gates.
- **Action:** Record the User-supplied status: `HEAD` is `96dcb4d`, no Git add,
  commit, or push was performed, and the only untracked content was the L05
  candidate directory.
- **Files Changed:** None by this step.
- **Verification:** User-supplied Git status evidence.
- **Expected Result:** The candidate is untracked and publication remains User-owned.
- **Status:** `PASS / USER OWNED`.

## Step 7 - Perform the initial inheritance and architecture audit

- **Objective:** Confirm that the candidate can preserve the V00 architecture.
- **Why:** L05 must not alter frozen predecessor lessons, the Backbone, the
  Interface Contract, or Document C.
- **Action:** Inspect the inherited V00_L01-L04 APIs and tests, including
  `VisionFrameTransform`, `AprilTagFieldLayoutContract`, `VisionIO`,
  `VisionObservation`, and `VisionIOSim`.
- **Files Changed:** None.
- **Verification:** Frozen Backbone, Frozen Interface Contract, Document C,
  predecessor protection, and L04-to-L05 inheritance all passed.
- **Expected Result:** The candidate is a valid technical starting point.
- **Status:** `PASS`.

## Step 8 - Discover the initial naming HOLD

- **Objective:** Check the candidate directory against the locked V00 roadmap.
- **Why:** Content inheritance alone does not authorize a different lesson
  identity.
- **Action:** Compare the first candidate name,
  `V00_L05_CanonicalAprilTagRobotPoseCandidateEstimation`, with the frozen ADR
  identity, `V00_L05_AprilTagRobotPoseEstimation`.
- **Files Changed:** None.
- **Verification:** The mismatch was documented as a governance naming HOLD.
- **Expected Result:** No lifecycle activation occurs while identity is unresolved.
- **Status:** `HOLD / HISTORICAL`.

## Step 9 - Retain the frozen ADR identity

- **Objective:** Resolve the naming HOLD without changing the roadmap.
- **Why:** The technical phrase “canonical AprilTag robot-pose candidate
  estimation” describes the lesson concept but is not its official identity.
- **Action:** The Architect decided not to amend the ADR and to use the existing
  official title and directory identity.
- **Files Changed:** None.
- **Verification:** Architect decision: ADR amendment not required.
- **Expected Result:** L05 keeps the frozen roadmap identity.
- **Status:** `APPROVED`.

## Step 10 - Rename the untracked candidate directory

- **Objective:** Make the candidate directory match the authorized identity.
- **Why:** Lifecycle records must refer to the exact ADR-locked lesson path.
- **Action:** The User renamed the untracked candidate to
  `V00_L05_AprilTagRobotPoseEstimation`.
- **Files Changed:** Untracked candidate directory name only.
- **Verification:** Old path absent; new path present; no Git publication performed.
- **Expected Result:** The candidate has the official V00_L05 identity.
- **Status:** `PASS / USER OWNED`.

## Step 11 - Re-audit governance identity

- **Objective:** Confirm that the naming blocker is fully resolved.
- **Why:** Activation requires identity, predecessor, and frozen-boundary
  agreement.
- **Action:** Verify the directory name, copied L04 metadata, active lesson
  count, V00_L04 publication, and predecessor protection.
- **Files Changed:** None.
- **Verification:** The new directory matches the ADR; no amendment is required;
  L05 remains not activated and active V00 count remains zero before this task.
- **Expected Result:** The candidate is ready for Design-Lock decision and
  controlled activation.
- **Status:** `PASS`.

## Step 12 - Complete the Design-Lock planning audit

- **Objective:** Give the Architect repository-grounded options without
  implementing them.
- **Why:** The public boundary must be decided before source authorization.
- **Action:** Compare explicit geometry, layout-owning, and
  Observation-oriented ownership options.
- **Files Changed:** None.
- **Verification:** Option A, the explicit geometry calculator, was recommended
  because it keeps lookup, acquisition, Observation, and quality concerns out
  of the one-concept L05 calculation.
- **Expected Result:** The Architect receives a minimal design recommendation.
- **Status:** `PASS`.

## Step 13 - Record the Architect-approved Design Lock

- **Objective:** Freeze the exact design boundary before implementation.
- **Why:** Design approval and implementation authorization are separate gates.
- **Action:** Record the approved package, class, method, mathematics,
  dependency ownership, output semantics, structural validation, test matrix,
  and L06 separation.
- **Files Changed:** The authorized L05 documentation targets during this
  activation task.
- **Verification:** Architect approval supplied in the activation authorization.
- **Expected Result:** The design is approved, but no Java implementation is
  authorized yet.
- **Status:** `APPROVED`.

## Step 14 - Activate V00_L05 under the approved Design Lock

- **Objective:** Make exactly one lesson editable for the next separately
  authorized engineering phase.
- **Why:** Repository governance permits implementation changes only in the
  sole `IN_PROGRESS / EDITABLE` lesson.
- **Action:** Record V00_L05 as `IN_PROGRESS / EDITABLE`, set active V00 lesson
  count to `1`, retain V00_L04 as frozen, and preserve the then-current
  pre-implementation state until separate authorization.
- **Files Changed:** Authorized documentation targets only:
  `AGENTS.md`, repository `README.md`, this lesson's `README.md`,
  `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide.
- **Verification:** Cross-document activation validation after editing.
- **Expected Result:** V00_L05 is the sole editable lesson and all frozen
  boundaries remain protected.
- **Status:** `PASS / ACTIVATION COMPLETE`.

## Step 15 - Obtain separate implementation authorization

- **Objective:** Authorize only the exact production and focused-test files.
- **Why:** Activation and Design Lock do not authorize source changes.
- **Action:** Explicit authorization was supplied for
  `AprilTagRobotPoseEstimator.java` and
  `AprilTagRobotPoseEstimatorTest.java`.
- **Files Changed:** Authorization did not itself change source.
- **Verification:** Architect/User implementation authorization.
- **Expected Result:** Only the locked two-file implementation boundary became
  editable within the active lesson.
- **Status:** `PASS`.

## Step 16 - Implement the estimator

- **Objective:** Add the approved pure geometry calculation.
- **Why:** This is the one new L05 concept.
- **Action:** Implement the exact approved API and structural validation.
- **Files Changed:** `src/main/java/frc/robot/vision/AprilTagRobotPoseEstimator.java`.
- **Verification:** Focused tests, source review, and User-supplied Java 17
  verification.
- **Expected Result:** A deterministic canonical robot-pose candidate is returned.
- **Status:** `PASS`.

## Step 17 - Implement and repair focused tests

- **Objective:** Verify the locked mathematical and structural contract.
- **Why:** Independent numeric tests must prevent tautological success.
- **Action:** Add the approved focused test matrix, including independent
  translation, rotation, order, and nontrivial 3D oracles. The Java 17
  compatibility adjustment changed `getFirst()` to `get(0)` in the test. The
  original noncommutativity fixture was then found to be degenerate: the two
  inverse transforms being compared were pure translations, so they commuted
  and reversing their order did not expose the required defect. Repair the
  fixture in the focused test with nonzero rotations and translations.
- **Files Changed:** `src/test/java/frc/robot/vision/AprilTagRobotPoseEstimatorTest.java` only.
- **Verification:** Focused User-supplied Java 17 verification passed. The
  repaired fixture produces locked-order translation `(3, 2, 1.5)` and
  reversed-order translation `(0, 1, 1.5)`. The repair was test-only; no
  production repair was required.
- **Expected Result:** Focused tests verify behavior without using the same
  implementation as the oracle.
- **Status:** `PASS`.

## Step 18 - Run focused and inherited verification

- **Objective:** Verify L05 and protect L01-L04 regressions.
- **Why:** Both new behavior and inherited contracts must pass.
- **Action:** Run focused L05 tests and inherited vision regression tests.
- **Files Changed:** Generated outputs only.
- **Verification:** User-supplied WPILib Java 17 results: focused L05 tests
  PASS and inherited vision regressions PASS.
- **Expected Result:** Focused and inherited tests pass.
- **Status:** `PASS / USER VERIFIED`.

## Step 19 - Run full suite and clean build

- **Objective:** Complete automated verification before closure.
- **Why:** Focused tests cannot establish repository-wide compatibility.
- **Action:** The User runs the full test suite and clean build.
- **Files Changed:** Generated outputs only.
- **Verification:** User-supplied WPILib Java 17 full-suite PASS and clean-build
  PASS results.
- **Expected Result:** Full suite and clean build pass.
- **Status:** `PASS / USER VERIFIED`.

## Step 20 - Reconcile post-implementation documentation and request closure review

- **Objective:** Reconcile final evidence and request closure approval.
- **Why:** A lesson may freeze only after implementation, verification, and
  documentation are complete.
- **Action:** Reconcile the active records, record the post-implementation
  architecture PASS, and harden the reflection test to protect final-class and
  exactly-one-public-declared-method behavior. Submit the completed lesson for
  final Architect closure review.
- **Files Changed:** Authorized L05 lifecycle records and the focused test only.
- **Verification:** Documentation reconciliation and reflection-test hardening
  are complete. Post-hardening User verification is PASS.
- **Expected Result:** L05 has complete implementation, verification, and
  documentation evidence ready for closure authorization.
- **Status:** `PASS`.

## Step 21 - Obtain final closure authorization and freeze the lesson

- **Objective:** Record the approved final lesson state.
- **Why:** Architect closure authorization is required before a lesson becomes
  frozen and read-only.
- **Action:** Record the Architect's `APPROVED` closure decision and transition
  V00_L05 to `COMPLETE / FROZEN / READ-ONLY` with no active V00 lesson.
- **Files Changed:** Authorized lifecycle metadata only.
- **Verification:** Architect final closure authorization.
- **Expected Result:** L05 is technically closed, frozen, and read-only.
- **Status:** `PASS / APPROVED`.

## Step 22 - User Git publication and later metadata reconciliation - PENDING

- **Objective:** Publish the frozen lesson without taking ownership of Git.
- **Why:** Lesson closure and Git publication are separate lifecycle gates.
- **Action:** The User performs Git add, commit, and push, then supplies the
  actual commit hash and push result for later publication-metadata
  reconciliation if required by repository governance.
- **Files Changed:** User-owned Git state; no Codex Git operation.
- **Verification:** Pending User Git publication evidence.
- **Expected Result:** Publication is recorded only after a real User commit and
  push; no fabricated hash is used.
- **Status:** `PENDING / USER OWNED`.

## Student boundary summary

Inherited and unchanged from L04:

- Frozen Backbone and dependency direction;
- Frozen Interface Contract;
- Document C Observation architecture;
- V00_L01-L04 implementations;
- vendor-neutral IO and immutable Observation contracts;
- canonical Blue-origin/NWU geometry ownership; and
- A01_L04 sole alliance-transform ownership.

New and implemented for L05:

- one pure robot-pose candidate calculation;
- `AprilTagRobotPoseEstimator` in `frc.robot.vision`;
- the single `estimateFieldToRobotCandidate(...)` method;
- structural validation only; and
- focused independent-oracle and API-reflection verification.

Still deferred:

- quality and acceptance to V00_L06;
- timestamps and latency to V00_L07;
- real camera integration to V00_L08; and
- Swerve estimator fusion to V00_L09.
