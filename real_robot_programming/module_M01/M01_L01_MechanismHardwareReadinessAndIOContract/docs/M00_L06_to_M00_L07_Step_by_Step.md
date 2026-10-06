# M00_L06 to M00_L07 Step-by-Step Transition Guide

## Current scope

This guide preserves the complete chronological transition from canonical
published M00_L06 through the controlled M00_L07 freeze. M00_L07 is now
`COMPLETE / FROZEN / READ-ONLY`. Implementation, final static review, focused
tests, clean regression, bounded Simulation, documentation reconciliation,
Independent Closure Review, and the controlled freeze transition are complete
and accepted. User-owned publication remains pending, and M00_L08 remains
inactive/uncreated.

## Step 1 — Confirm the frozen predecessor

**Objective:** Establish the authorized inheritance source.

**Why:** A new lesson must inherit from the immediately previous completed and published lesson.

**Action:** Confirmed M00_L06 as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

**Files Changed:** None; predecessor inspection only.

**Verification:** Accepted preparation evidence and canonical publication record.

**Expected Result:** M00_L06 remains untouched and authoritative. **COMPLETE / PASS**

## Step 2 — Copy the predecessor

**Objective:** Create M00_L07 through inheritance, not recreation.

**Why:** The governed lifecycle requires copy-and-rename development.

**Action:** The User copied M00_L06 to `M00_L07_FlywheelFoundation`.

**Files Changed:** New inherited candidate prepared by the User.

**Verification:** Candidate preparation was accepted.

**Expected Result:** One inherited M00_L07 candidate exists. **COMPLETE / PASS**

## Step 3 — Remove generated artifacts

**Objective:** Exclude copied build outputs and caches from lesson evidence.

**Why:** Generated files are not authoritative lesson content.

**Action:** The User removed copied `build/` and `.gradle/` artifacts.

**Files Changed:** Generated artifacts only.

**Verification:** Preparation review accepted the cleaned candidate.

**Expected Result:** The candidate is ready for a fresh baseline. **COMPLETE / PASS**

## Step 4 — Run the inherited baseline build

**Objective:** Verify the untouched inherited candidate before M00_L07 changes.

**Why:** Baseline evidence separates inherited health from new implementation.

**Action:** The User ran the accepted baseline build.

**Files Changed:** None; generated output only.

**Verification:** `BUILD SUCCESSFUL in 38s`; 6 actionable tasks, all 6 executed.

**Expected Result:** The inherited snapshot is buildable; no M00_L07 implementation is yet claimed. **COMPLETE / PASS**

## Step 5 — Complete the Architecture / Inheritance Audit

**Objective:** Confirm exact inheritance and architecture eligibility.

**Why:** Design work requires a clean architecture-preserving candidate.

**Action:** Audited candidate content and applicable governance.

**Files Changed:** None; read-only audit.

**Verification:** `PASS_M00_L07_ARCHITECTURE_INHERITANCE_AUDIT`; 306/306 governed files byte-identical.

**Expected Result:** The candidate is eligible for Design Lock. **COMPLETE / PASS**

## Step 6 — Accept the Final Design Lock

**Objective:** Lock one exact concept and bounded design.

**Why:** Activation and implementation need an explicit architecture boundary.

**Action:** Accepted `PASS_M00_L07_FINAL_DESIGN_LOCK` for independent Flywheel rotational-speed mechanism ownership.

**Files Changed:** None; design review only.

**Verification:** Architect gate accepted.

**Expected Result:** Future implementation scope and exclusions are fixed. **COMPLETE / PASS**

## Step 7 — Perform Controlled Activation

**Objective:** Make M00_L07 the sole active editable lesson without implementing Flywheel.

**Why:** Activation and implementation are separate gates.

**Action:** Reconciled lifecycle identity to `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`.

**Files Changed:** The eight authorized activation lifecycle records.

**Verification:** `PASS_M00_L07_CONTROLLED_ACTIVATION`.

**Expected Result:** Active lesson count is 1; M00_L08 remains inactive/uncreated. **COMPLETE / PASS**

## Step 8 — Complete governance adjudication and activation review

**Objective:** Resolve activation-governance questions before implementation.

**Why:** Implementation cannot proceed from a disputed activation.

**Action:** The Architect adjudicated governance and an independent activation review rechecked the result.

**Files Changed:** None; read-only review.

**Verification:** `PASS_M00_L07_GOVERNANCE_ADJUDICATION`; `PASS_M00_L07_INDEPENDENT_ACTIVATION_REVIEW_AFTER_ADJUDICATION`.

**Expected Result:** Activation is accepted for separately authorized implementation. **COMPLETE / PASS**

## Step 9 — Authorize the exact implementation boundary

**Objective:** Constrain production and focused-test work.

**Why:** Design Lock does not itself authorize file edits.

**Action:** Authorized five new production types, bounded `RobotTelemetry` and `RobotContainer` integration, and six focused tests; inherited tests remained protected.

**Files Changed:** Authorization records only.

**Verification:** Exact implementation authorization accepted.

**Expected Result:** Only the locked Flywheel Foundation boundary may be implemented. **COMPLETE / PASS**

## Step 10 — Implement the bounded Flywheel Foundation

**Objective:** Add the single approved mechanism-foundation concept.

**Why:** The lesson requires vendor-neutral ownership without physical hardware selection.

**Action:** Implemented Flywheel IO/IOInputs, Noop, immutable Observation, subsystem, read-only telemetry, and composition-root integration.

**Files Changed:** Five new production files, `RobotTelemetry.java`, `RobotContainer.java`, and six new focused tests.

**Verification:** `PASS_M00_L07_IMPLEMENTATION_REPORT_ACCEPTED`.

**Expected Result:** The Noop-only Flywheel Foundation is implemented; later roadmap scope remains absent. **COMPLETE / PASS**

## Step 11 — Record the initial Independent Static Review HOLD

**Objective:** Preserve the first review result and exact repair need.

**Why:** A failed quality gate must not be flattened into later success.

**Action:** Reviewed production and all focused tests; production passed, but four test-quality defects were found: first-throw request ordering, negative-infinity coverage, brittle architecture source checks, and missing RobotContainer absence boundaries.

**Files Changed:** None; read-only review.

**Verification:** `HOLD_M00_L07_INDEPENDENT_STATIC_REVIEW_TEST_QUALITY`.

**Expected Result:** Verification stops pending bounded test-only repair. **COMPLETE / PASS**

## Step 12 — Complete the bounded four-test repair

**Objective:** Close only the four authorized test-quality findings.

**Why:** Production already passed and was outside repair scope.

**Action:** Repaired `FlywheelSubsystemTest`, `FlywheelObservationTest`, `FlywheelArchitectureBoundaryTest`, and `RobotContainerFlywheelCompositionTest`.

**Files Changed:** Exactly the four authorized focused test files.

**Verification:** `PASS_M00_L07_BOUNDED_TEST_REPAIR`.

**Expected Result:** Three findings close; the repaired set is ready for rereview. **COMPLETE / PASS**

## Step 13 — Record the architecture-test rereview HOLD

**Objective:** Preserve the one remaining static blocker.

**Why:** The first repair still left comment-sensitive architecture assertions.

**Action:** Rereviewed all repairs; retained a HOLD because `FlywheelArchitectureBoundaryTest` still used raw-source dependency/import checks.

**Files Changed:** None; read-only rereview.

**Verification:** `HOLD_M00_L07_STATIC_REREVIEW_ARCHITECTURE_TEST_SOURCE_PARSING`.

**Expected Result:** Only the architecture test requires one final bounded repair. **COMPLETE / PASS**

## Step 14 — Complete the final single-file test repair

**Objective:** Eliminate comment-sensitive architecture assertions.

**Why:** Architecture tests must inspect semantic constructs rather than arbitrary comment text.

**Action:** Replaced raw-source dependency checks with reflection/type inspection and parsed actual imports from comment-free source.

**Files Changed:** `src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java` only.

**Verification:** `PASS_M00_L07_FINAL_ARCHITECTURE_TEST_REPAIR_REPORT`.

**Expected Result:** The final test-quality blocker is repaired without production or inherited-test changes. **COMPLETE / PASS**

## Step 15 — Complete the final Independent Static Rereview

**Objective:** Determine whether any static blocker remains.

**Why:** User-owned tests should run only after the repaired implementation and tests pass independent review.

**Action:** Reinspected production integrity, all six focused tests, Final Design Lock scope, and protected future lessons.

**Files Changed:** None; read-only review.

**Verification:** `PASS_M00_L07_FINAL_INDEPENDENT_STATIC_REREVIEW`; remaining findings `NONE`.

**Expected Result:** The lesson is ready for User focused tests. **COMPLETE / PASS**

## Step 16 — Run the six focused test classes

**Objective:** Verify the exact M00_L07 contracts.

**Why:** Focused evidence directly exercises the new concept.

**Action:** The User ran exactly the six M00_L07 focused test classes.

**Files Changed:** None; generated test output only.

**Verification:** `PASS_M00_L07_USER_FOCUSED_TESTS`; `BUILD SUCCESSFUL`.

**Expected Result:** `FOCUSED TESTS: PASS`. **COMPLETE / PASS**

## Step 17 — Run the clean full regression

**Objective:** Confirm inherited behavior and M00_L07 tests together.

**Why:** Focused success does not replace the complete lesson build.

**Action:** The User ran `gradlew clean build`.

**Files Changed:** None; generated build output only.

**Verification:** `PASS_M00_L07_CLEAN_FULL_REGRESSION`; `BUILD SUCCESSFUL in 36s`; 7 actionable tasks, 7 executed.

**Expected Result:** `CLEAN REGRESSION: PASS`. **COMPLETE / PASS**

## Step 18 — Verify bounded Simulation

**Objective:** Confirm Noop runtime composition and safe mode-transition behavior.

**Why:** Runtime evidence is required without overstating hardware proof.

**Action:** The User observed Disabled initial, Teleop Enabled idle with no Flywheel action, and return to Disabled. Each checkpoint reported unavailable, disconnected, velocity invalid, `velocityRpm = 0.0`, and `STOPPED`.

**Files Changed:** None.

**Verification:** `PASS_M00_L07_BOUNDED_SIMULATION`.

**Expected Result:** Software composition, transport, telemetry, Noop semantics, no automatic Teleop request, and safe persistence are verified. **COMPLETE / PASS**

## Step 19 — Reconcile documentation and lifecycle

**Objective:** Align the eight authorized records with accepted implementation and verification evidence.

**Why:** Closure review requires one consistent current record.

**Action:** Recorded implementation, both static HOLD/repair stages, final static PASS, focused tests, clean regression, Simulation, evidence limits, and protected future scope while retaining the active editable lifecycle.

**Files Changed:** Exactly `AGENTS.md`, root `README.md`, M00 roadmap ADR, four lesson-local lifecycle files, and this guide.

**Verification:** Non-Git read-only consistency and integrity review.

**Expected Result:** Documentation reconciliation is complete; M00_L07 remains active and unfrozen. **COMPLETE / PASS**

## Step 20 — Complete the Independent Closure Review

**Objective:** Determine whether any blocker remains before freeze authorization.

**Why:** Completion and freeze require an independent review of architecture, implementation, verification, evidence limits, documentation, and lifecycle consistency.

**Action:** Independently reviewed the Final Design Lock, seven-file production boundary, six focused tests, 90 unchanged inherited tests, accepted User verification, Simulation interpretation, roadmap protection, predecessor integrity, and reconciled documentation.

**Files Changed:** None; strict read-only review.

**Verification:** `PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW`; `CLOSURE_REVIEW_PASS`; exact remaining findings `NONE`; recommendation `READY_FOR_FREEZE_AUTHORIZATION`.

**Expected Result:** M00_L07 is eligible for explicit Architect freeze authorization. **COMPLETE / PASS**

## Step 21 — Authorize the controlled freeze

**Objective:** Authorize the exact lifecycle transition without authorizing publication.

**Why:** A passing closure review does not itself change lesson state.

**Action:** The Architect accepted the Independent Closure Review and authorized M00_L07 to transition from active/editable to complete/frozen/read-only.

**Files Changed:** Authorization record only.

**Verification:** `PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW`; `FREEZE AUTHORIZED`.

**Expected Result:** The eight lifecycle records may be reconciled to the frozen pre-publication state. **COMPLETE / PASS**

## Step 22 — Complete the controlled freeze transition

**Objective:** Protect M00_L07 as the completed lesson snapshot.

**Why:** A completed lesson must become read-only before User-owned publication.

**Action:** Reconciled exactly the eight authorized lifecycle/documentation files to `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, current active M00 lesson `NONE`, and M00_L08 inactive/uncreated.

**Files Changed:** `AGENTS.md`; root `README.md`; M00 roadmap ADR; M00_L07 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`; and this transition guide.

**Verification:** Non-Git source/test/configuration/predecessor integrity review and cross-document lifecycle consistency review.

**Expected Result:** M00_L07 is frozen and read-only while all accepted evidence and protected future scope remain unchanged. **COMPLETE / PASS**

## Step 23 — Stop before publication

**Objective:** Preserve publication as a separate User-owned gate.

**Why:** Freeze does not create a commit, push, remote alignment, or publication verification result.

**Action:** Record publication as `PENDING / NOT YET PUBLISHED`; do not run Git or create M00_L08.

**Files Changed:** Lifecycle documentation only.

**Verification:** No publication hash, push result, remote-alignment claim, or M00_L08 preparation/activation is recorded.

**Expected Result:** The frozen snapshot is ready for a later separately authorized User publication workflow. **PENDING**

## Evidence interpretation

At every bounded Simulation checkpoint, `velocityValid = false`. Therefore
`velocityRpm = 0.0` is the canonical invalid `FlywheelIONoop` representation,
not a verified physical zero-speed measurement. Evidence is classified exactly
as `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

M00_L08 retains target/setpoint, closed-loop, PID/PIDF, feedforward, regulation,
convergence, and error concepts. M00_L09 retains at-speed/readiness tolerance,
dwell, debounce, and policy. Flywheel command ownership, shooting coordination,
Feeder/Flywheel orchestration, automatic firing/staging, NamedCommands, and
autonomous mechanism integration remain later scope.

## Current endpoint

```text
M00_L06: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L07: COMPLETE / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
FINAL INDEPENDENT STATIC REREVIEW: PASS
FOCUSED TESTS: PASS
CLEAN REGRESSION: PASS
BOUNDED SIMULATION: PASS
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PASS
FREEZE: COMPLETE / FROZEN / READ-ONLY
PUBLICATION: PENDING / NOT YET PUBLISHED
M00_L08: INACTIVE / NOT CREATED
```
