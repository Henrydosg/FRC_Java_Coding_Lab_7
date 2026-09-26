# M00_L13 to M00_L14 — Shoot Coordination Step by Step

This guide records M00_L14 preparation through accepted implementation,
verification, closure rereview, and Freeze Reconciliation. Steps 1-12 preserve
the state at their respective gates. M00_L14 is now COMPLETE / FROZEN /
READ-ONLY / NOT PUBLISHED. Active Lesson Count is 0 and Current Active M00
Lesson is NONE. Independent Freeze Review is next; publication and Final
Publication Verification remain pending.

## Step 1 — Accept the frozen predecessor and prepare the candidate

**Objective:** Establish the authorized inherited starting point.  
**Why:** M00_L14 must inherit the immediately preceding frozen lesson.  
**Action:** The User copied M00_L13 to M00_L14 and removed copied build/ and
.gradle/ state. M00_L13 remained COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED.  
**Files Changed:** User-prepared candidate copy; no authored lesson content
changed during preparation. M00_L13 was not modified.  
**Verification:** Accepted predecessor primary SHA
5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704; metadata SHA
658d1e44c417763df3689b9b52e409161446c593. PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD;
6 actionable tasks, 6 executed; BASELINE_BUILD_EXIT_CODE=0.  
**Expected Result:** Clean inherited M00_L14 candidate with baseline build accepted.

## Step 2 — Architecture / Inheritance Audit

**Objective:** Confirm the candidate is a safe inheritance point before activation.  
**Why:** The Design Lock and activation depend on preserving the frozen predecessor.  
**Action:** Independently compare authored files and review architecture boundaries.  
**Files Changed:** None.  
**Verification:** PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT; 339/339 authored
files identical to M00_L13.  
**Expected Result:** Inheritance and architecture accepted.

## Step 3 — Final Design Lock

**Objective:** Lock one bounded Shoot Coordination concept.  
**Why:** Implementation scope must be settled before activation.  
**Action:** Approve scheduler-managed frc.robot.commands.ShootCommand using the
existing Flywheel and Feeder semantic APIs, exact subsystem requirements,
readiness authority, transition behavior, and exception cleanup contract.  
**Files Changed:** None.  
**Verification:** PASS_M00_L14_FINAL_DESIGN_LOCK.  
**Expected Result:** Exact command behavior and scope approved for activation.

## Step 4 — Controlled Activation

**Objective:** Make M00_L14 the sole active lesson and record its locked design.  
**Why:** Activation follows accepted inheritance and Final Design Lock.  
**Action:** Record lifecycle, command contract, test proof plan, and L15/L16 and
hardware boundaries.  
**Files Changed:** Governance and lesson documentation only; no source or test
implementation in this step.  
**Verification:** PASS_M00_L14_CONTROLLED_ACTIVATION; M00_L14 ACTIVE / IN_PROGRESS.  
**Expected Result:** Active lesson prepared for independent activation review.

## Step 5 — Independent Activation Review HOLD

**Objective:** Independently verify activated records and locked scope.  
**Why:** Activation review precedes implementation authorization.  
**Action:** Review lifecycle consistency, exact constructor contract, test plan,
and transition chronology.  
**Files Changed:** None by the review.  
**Verification:** HOLD_M00_L14_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_RECONCILIATION_REQUIRED.
Findings were documentation consistency only; no architecture, source, test,
implementation, predecessor, or governance-trust defect was found.  
**Expected Result:** Bounded documentation repair and independent re-review.

## Step 6 — Activation Documentation Repair and Re-review

**Objective:** Resolve the bounded activation documentation findings.  
**Why:** The current lifecycle, constructor contract, and focused-test plan must
be explicit before implementation authorization.  
**Action:** Reconcile the ADR lifecycle, constructor signature, test plan, and
checklist while preserving the original HOLD.  
**Files Changed:** M00 roadmap ADR, lesson plan, checklist, and this transition
guide. No production or test implementation was changed.  
**Verification:** PASS_M00_L14_ACTIVATION_DOCUMENTATION_REPAIR and
PASS_M00_L14_INDEPENDENT_ACTIVATION_REREVIEW.  
**Expected Result:** Activation documentation accepted; implementation may be
separately authorized.

## Step 7 — Implementation Authorization and Handoff

**Objective:** Authorize the exact implementation boundary and prepare static review.  
**Why:** Activation approval alone did not authorize source changes.  
**Action:** Accept the separate implementation authorization and handoff to
static review.  
**Files Changed:** None by the authorization or handoff.  
**Verification:** PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION and
PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW.  
**Expected Result:** Implement the locked command and its focused tests only.

## Step 8 — Implement ShootCommand and focused tests

**Objective:** Implement the locked Flywheel-to-Feeder coordination behavior.  
**Why:** This is M00_L14's single new concept.  
**Action:** Add frc.robot.commands.ShootCommand and focused tests for validation,
requirements, transitions, readiness loss/recovery, exception cleanup, terminal
stops, and architecture boundaries. Constants, RobotContainer, existing
subsystems, IO, Observations, telemetry, vendor adapters, and deploy files
remain unchanged.  
**Files Changed:** Added src/main/java/frc/robot/commands/ShootCommand.java;
added/updated src/test/java/frc/robot/commands/ShootCommandTest.java.  
**Verification:** Implementation completed within the accepted design boundary.  
**Expected Result:** One scheduler-managed command with exception-safe cleanup
and no additional shooting or timing authority.

## Step 9 — Test-only architecture-guard HOLD and repair chronology

**Objective:** Make the focused architecture guard prove the locked implementation shape.  
**Why:** Static review identified false-pass paths in the test guard.  
**Action:** Preserve and repair each bounded guard finding in the focused test:

1. HOLD_M00_L14_STATIC_REVIEW_TEST_GUARD_DEFECT ->
   PASS_M00_L14_TEST_GUARD_REPAIR.
2. HOLD_M00_L14_STATIC_REREVIEW_BRANCH_CONDITION_GUARD_DEFECT ->
   PASS_M00_L14_BRANCH_CONDITION_GUARD_REPAIR.
3. HOLD_M00_L14_STATIC_REREVIEW_DATAFLOW_MUTATION_GUARD_DEFECT ->
   PASS_M00_L14_DATAFLOW_MUTATION_GUARD_REPAIR.
4. HOLD_M00_L14_STATIC_REREVIEW_CONTROL_FLOW_GATE_GUARD_DEFECT ->
   PASS_M00_L14_CONTROL_FLOW_GUARD_REPAIR.
5. HOLD_M00_L14_STATIC_REREVIEW_DIRECT_OUTPUT_CALL_REACHABILITY_GUARD_DEFECT ->
   PASS_M00_L14_DIRECT_CALL_REACHABILITY_GUARD_REPAIR.

Every HOLD in this sequence was a TEST_ONLY_ARCHITECTURE_GUARD_DEFECT with
NO_PRODUCTION_DEFECT.  
**Files Changed:** Bounded test-only guard repairs in
src/test/java/frc/robot/commands/ShootCommandTest.java. Production remained
unchanged during the guard repairs.  
**Verification:** PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW /
INDEPENDENT_STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS. All 18 @Test
methods were preserved.  
**Expected Result:** No remaining static finding before User-owned focused tests.

## Step 10 — User focused tests and clean regression

**Objective:** Verify the focused command tests and inherited project suite.  
**Why:** Static review is followed by User-owned executable verification.  
**Action:** The User ran `gradlew test --tests "*ShootCommandTest"` and
`gradlew clean test`. Luna did not run Gradle or tests.  
**Files Changed:** None by verification.  
**Verification:** PASS_M00_L14_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 10s;
4 actionable tasks (3 executed, 1 up-to-date); FOCUSED_TEST_EXIT_CODE=0.
PASS_M00_L14_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 30s; 5 actionable
tasks (5 executed); CLEAN_REGRESSION_EXIT_CODE=0.  
**Expected Result:** Focused and clean regression gates pass.

## Step 11 — Bounded WPILib Simulation

**Objective:** Verify bounded runtime startup, mode transitions, safe semantic
state, and normal shutdown.  
**Why:** Simulation is required after tests but has an explicit scope limit.  
**Action:** The User launched WPILib Java Simulation and observed Disabled
startup (not enabled, DS attached, not E-stopped), Teleoperated enable (DS
attached, not E-stopped), and return to Disabled (not enabled, DS attached,
not E-stopped) with Feeder, Flywheel, and Intake RequestedState STOPPED, then
normal shutdown. RobotContainer remains unchanged and contains no ShootCommand
binding.  
**Files Changed:** None by verification.  
**Verification:** PASS_M00_L14_USER_BOUNDED_SIMULATION;
LAST_NATIVE_EXIT_CODE=0. No application crash or scheduler/runtime exception
was observed.  
**Expected Result:** Simulation supports application startup, mode transitions,
scheduler/integration stability, safe semantic state, and clean exit. It does
not directly execute ShootCommand through a RobotContainer binding or establish
a physical shot. Real hardware remains deferred.

## Step 12 — Post-verification documentation reconciliation

**Objective:** Reconcile current records with accepted implementation and User
verification evidence without closing the lesson.  
**Why:** Independent Closure Review requires truthful current documentation.  
**Action:** Record accepted implementation/static/test/regression/Simulation
gates, preserve the test-only HOLD/repair chronology, state Simulation limits,
and keep M00_L14 ACTIVE / IN_PROGRESS.  
**Files Changed:** AGENTS.md; repository README.md; M00 roadmap ADR; M00_L14
README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, and this guide.
No production, test, deploy, configuration, or support files changed.  
**Verification:** Canonical governance mirror validation PASS.  
**Expected Result:** Documentation reconciliation complete; Independent
Closure Review pending. M00_L14 remains NOT COMPLETE / NOT FROZEN / NOT
PUBLISHED. Freeze and User-owned publication remain pending.

## Step 13 — Initial Independent Closure Review HOLD

**Objective:** Review the completed implementation, evidence, and lesson records.  
**Why:** Closure review must confirm documentation states only proven behavior.  
**Action:** Independently inspect the current source, tests, accepted User
verification, and documentation.  
**Files Changed:** None by the read-only review.  
**Verification:**
HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED.
The findings concerned the missing Build field and overstated scheduler proof
in the plan and checklist; no production, architecture, runtime, test, or
Simulation defect was identified.  
**Expected Result:** Bounded documentation proof reconciliation before closure
rereview.

## Step 14 — Bounded documentation proof reconciliation

**Objective:** Repair the exact documentation and evidence-language findings.  
**Why:** The Build field and direct `end(true)` proof must be represented
accurately without claiming scheduler-driven cancellation.  
**Action:** Add the accepted User clean-regression Build field, correct the
focused-test proof language, and record the accepted governance mirror PASS.  
**Files Changed:** LESSON_STATUS.md, LESSON_PLAN.md, and LESSON_CHECKLIST.md
only. No production, test, deploy, configuration, or support file changed.  
**Verification:** PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION.  
**Expected Result:** Direct interrupted-end cleanup proof is documented;
actual CommandScheduler scheduling/cancellation of ShootCommand remains
unproven.

## Step 15 — Independent Closure Rereview PASS

**Objective:** Independently confirm the bounded repair and closure evidence.  
**Why:** Freeze Reconciliation requires an accepted closure rereview.  
**Action:** Reinspect governance, the repaired documentation, source/test
boundaries, and the accepted User verification record.  
**Files Changed:** None by the read-only rereview.  
**Verification:** PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW; verdict
INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION.  
**Expected Result:** M00_L14 is ready for documentation-only Freeze
Reconciliation.

## Step 16 — Freeze Reconciliation

**Objective:** Record the completed lesson as frozen without claiming
publication.  
**Why:** The accepted closure gate permits the transition to a read-only
lesson before Independent Freeze Review and User-owned publication.  
**Action:** Reconcile current lifecycle metadata to COMPLETE / FROZEN /
READ-ONLY / NOT PUBLISHED, Active Lesson Count 0, and Current Active M00 Lesson
NONE. Keep M00_L15/L16 future and inactive, preserve theory/Simulation/deferred
hardware evidence, and finalize this guide.  
**Files Changed:** AGENTS.md, repository README.md, M00 roadmap ADR, M00_L14
README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, and this
transition guide. No production, test, deploy, configuration, or support file
changed.  
**Verification:** Freeze Reconciliation COMPLETE; canonical governance mirror
validation PASS with 12 VERIFIED mirrors, 12 matching PDF hashes, 12 matching
Markdown hashes, and zero deterministic findings. No Gradle, test, build,
Simulation, or Git action was performed in this step.  
**Expected Result:** M00_L14 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED.
Independent Freeze Review remains PENDING; User-owned publication and Final
Publication Verification remain PENDING.

## Remaining gates

- Independent Freeze Review: PENDING.
- User-owned publication: NOT PUBLISHED / PENDING.
- Final Publication Verification: PENDING.
- Real hardware: DEFERRED.
