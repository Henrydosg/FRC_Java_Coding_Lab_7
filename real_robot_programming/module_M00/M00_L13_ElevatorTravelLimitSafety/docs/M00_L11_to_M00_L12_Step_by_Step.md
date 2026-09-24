# M00_L11 to M00_L12 — Step by Step

## Current frozen lifecycle and evidence — 2026-09-23

- **Lesson:** `M00_L12 - Elevator Homing`
- **Previous lesson:** `M00_L11 - Elevator Closed-Loop Position`
- **Lifecycle:** `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`
- **Active Lesson Count:** `0`
- **Current Active M00 Lesson:** `NONE`
- **Implementation Authorization:** `PASS_M00_L12_IMPLEMENTATION_AUTHORIZATION`
- **Implementation:** `COMPLETE / IMPLEMENTATION_COMPLETE_READY_FOR_INDEPENDENT_STATIC_REVIEW`
- **Final Independent Static Re-review:** `PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW`
- **Focused Tests:** `PASS_M00_L12_USER_FOCUSED_TESTS / FRESH --rerun-tasks / BUILD SUCCESSFUL in 35s / 4 ACTIONABLE TASKS EXECUTED`
- **Clean Regression:** `PASS_M00_L12_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 24s / 5 ACTIONABLE TASKS EXECUTED`
- **Simulation:** `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION / THREE CHECKPOINTS`
- **Evidence:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Independent Closure Review:** `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`
- **Freeze Reconciliation:** `COMPLETE / FREEZE_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_FREEZE_REVIEW`
- **Independent Freeze Review:** `PENDING / NEXT GATE`
- **Publication:** `NOT PUBLISHED / PENDING / USER-OWNED`
- **Publication SHA:** `NONE / NOT YET ESTABLISHED`
- **Real hardware:** `DEFERRED`

M00_L11 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.
M00_L13 remains `INACTIVE / NOT CREATED`.

## Step 1 — Inherit the frozen predecessor

**Objective:** Establish M00_L12 from the canonical M00_L11 snapshot.

**Why:** The repository requires one lesson to inherit the immediately preceding completed lesson.

**Action:** The User copied M00_L11 into `M00_L12_ElevatorHoming` from frozen snapshot `385bf1d4ff6550dafb3e15de06ceaa9f1e9edc03`. M00_L11 remains untouched.

**Files Changed:** M00_L12 candidate documentation only; no predecessor files.

**Verification:** Accepted frozen predecessor state and publication metadata SHA `1a4f2540ab1680f5745c9e1e7cc432e757e3a74d`.

**Expected Result:** A separate M00_L12 project exists without changing M00_L11.

## Step 2 — Remove generated copy artifacts

**Objective:** Remove generated build artifacts from the copied project before baseline verification.

**Why:** Generated output is not lesson source or inheritance evidence.

**Action:** The User removed generated artifacts from the copied baseline before the accepted baseline build.

**Files Changed:** Generated artifacts only during preparation; no production, test, deploy, or lesson design change.

**Verification:** Preparation gate accepted as `PASS_M00_L12_UNTOUCHED_COPY_BASELINE_BUILD`.

**Expected Result:** The candidate is an untouched source/documentation copy for comparison.

## Step 3 — Establish the untouched-copy baseline

**Objective:** Confirm the copied project builds before the new concept is introduced.

**Why:** Baseline evidence separates inherited project health from later M00_L12 implementation work.

**Action:** The User ran the baseline build.

**Files Changed:** None by the baseline build.

**Verification:** `BUILD SUCCESSFUL in 27s`; 7 actionable tasks, 6 executed, 1 up-to-date.

**Expected Result:** Preparation/inheritance evidence only; no M00_L12 implementation evidence.

## Step 4 — Complete the read-only Architecture / Inheritance Audit

**Objective:** Confirm that the candidate inherits M00_L11 without semantic drift.

**Why:** The new lesson must begin from the frozen predecessor while preserving the Frozen Backbone and package responsibilities.

**Action:** The independent audit compared non-generated candidate content with M00_L11.

**Files Changed:** None.

**Verification:** `PASS_M00_L12_ARCHITECTURE_INHERITANCE_AUDIT`; production 109/109 identical, tests 102/102 identical, deploy/config/support 24/24 identical, and non-generated documentation 95/95 identical.

**Expected Result:** The candidate is an untouched inherited baseline ready for the locked design concept.

## Step 5 — Accept the Final Design Lock

**Objective:** Define exactly one new M00_L12 concept and its boundaries.

**Why:** Design must be reviewed before any implementation authorization.

**Action:** The read-only independent Final Design Lock review accepted the bounded homing lifecycle.

**Files Changed:** None.

**Verification:** `PASS_M00_L12_FINAL_DESIGN_LOCK`.

**Expected Result:** The concept is locked for documentation and future implementation review.

The locked concept is:

> A bounded, scheduler-managed Elevator homing lifecycle that requests vendor-neutral reference acquisition and recognizes success only from the normalized IO-reported `positionReferenced` semantic.

The lock preserves the five-field `ElevatorIOInputs` contract, adds only vendor-neutral `requestHoming()`, keeps `ElevatorObservation` at eight fields, adds only `HOMING` to the requested-state enum, preserves the normal position-reference guard, and protects M00_L13 travel-limit scope. It does not invent a sensor, direction, speed, control mode, physical timeout, real adapter, or `ElevatorIOSim`.

## Step 6 — Controlled Activation

**Objective:** Make M00_L12 the sole active lesson in documentation.

**Why:** Activation establishes identity and editable lifecycle; it does not authorize implementation.

**Action:** Reconcile the authorized documentation surfaces with M00_L12 identity, the accepted gates, the locked concept, planned production/test boundaries, and truthful pre-implementation evidence.

**Files Changed:** `AGENTS.md`, root `README.md`, the M00 roadmap ADR, M00_L12 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide.

**Verification:** Governance validation PASS; production, tests, deploy/config/support remain unchanged; M00_L11 remains frozen and M00_L13 remains inactive/not created.

**Expected Result:** `CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.

## Step 7 — Repair duplicate current activation documentation

**Objective:** Keep exactly one authoritative current M00_L12 state in each activation document.

**Action:** The activation re-review held on duplicate current activation blocks under `HOLD_M00_L12_INDEPENDENT_ACTIVATION_REVIEW_DUPLICATE_CURRENT_ACTIVATION_BLOCKS`. The bounded documentation repair updated the existing current blocks in place while preserving historical snapshots. The independent activation re-review then passed as `ACTIVATION_REREVIEW_PASS_READY_FOR_IMPLEMENTATION_AUTHORIZATION`.

**Expected Result:** One current M00_L12 lifecycle block per document; historical lesson records remain marked historical.

## Step 8 — Authorize and implement the locked concept

**Objective:** Implement only the bounded, scheduler-managed homing lifecycle from the accepted Design Lock.

**Action:** Under `PASS_M00_L12_IMPLEMENTATION_AUTHORIZATION`, implementation added vendor-neutral `requestHoming()`, the `HOMING` requested state, subsystem normalization and state ordering, and `HomeElevatorCommand` with a finite positive software timeout. No hardware sensor mechanism, real adapter, `ElevatorIOSim`, binding, travel-limit behavior, or physical homing strategy was introduced.

**Verification:** `IMPLEMENTATION_COMPLETE_READY_FOR_INDEPENDENT_STATIC_REVIEW`. Production delta is `109 / 105 / 4 / 0 / 1`; tests are `102 / 98 / 4 / 0 / 1`; deploy/config/support is `24 / 24 / 0 / 0 / 0` (compared / identical / changed / missing / added).

## Step 9 — Repair architecture-test false positive

**Objective:** Permit the approved `ElevatorRequestedState.HOMING` and `requestHoming()` semantics while retaining detection of prohibited hardware-specific homing identifiers.

**History:** Static review held under `HOLD_M00_L12_STATIC_REVIEW_ARCHITECTURE_TEST_FALSE_POSITIVE_HOMING_TOKEN`. Classification was `TEST_IMPLEMENTATION_DEFECT / NO_PRODUCTION_DEFECT / NO_ARCHITECTURE_DEFECT`. The test-only repair passed as `PASS_M00_L12_ARCHITECTURE_TEST_FALSE_POSITIVE_REPAIR`.

## Step 10 — Repair compound homing identifier coverage

**Objective:** Detect compound hardware-specific identifiers such as `homingVoltage`, `homingSpeed`, `homingDirection`, `homingCurrent`, `homingThreshold`, `homingLimitSwitch`, `homeSwitch`, and `homeSensor` without rejecting the authorized command or method names.

**History:** Static re-review held under `HOLD_M00_L12_STATIC_REREVIEW_ARCHITECTURE_TEST_COMPOUND_HOMING_IDENTIFIER_GAP`. Classification was `TEST_IMPLEMENTATION_DEFECT / NO_PRODUCTION_DEFECT / NO_ARCHITECTURE_DEFECT`. The bounded test repair passed as `PASS_M00_L12_ARCHITECTURE_TEST_COMPOUND_HOMING_GUARD_REPAIR`.

## Step 11 — Add vendor package/API ownership guard and complete static review

**Objective:** Guard vendor package imports and fully qualified references in non-adapter Elevator semantic layers.

**History:** Static re-review held under `HOLD_M00_L12_STATIC_REREVIEW_ARCHITECTURE_TEST_VENDOR_IMPORT_GUARD_GAP`. The test-only repair guards repository-supported `com.ctre.` and `com.revrobotics.` roots while allowing vendor references only in concrete ElevatorIO implementations. It passed as `PASS_M00_L12_ARCHITECTURE_TEST_VENDOR_BOUNDARY_REPAIR`. Final independent static re-review passed as `STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`. All three HOLDs were test implementation defects; no production defect or current production architecture defect was found.

## Step 12 — Run User focused tests

**Objective:** Verify the accepted seven-class Elevator focused suite with a fresh run.

**Verification:** `PASS_M00_L12_USER_FOCUSED_TESTS`; User ran with `--rerun-tasks`; `BUILD SUCCESSFUL in 35s`; four actionable tasks, all executed. Classes were `ElevatorArchitectureBoundaryTest`, `ElevatorIONoopTest`, `ElevatorObservationTest`, `ElevatorSubsystemTest`, `HomeElevatorCommandTest`, `ElevatorTelemetryFacadeTest`, and `RobotContainerElevatorCompositionTest`. No total test count was supplied.

## Step 13 — Run User clean regression

**Objective:** Verify the full project regression from a clean build.

**Verification:** `PASS_M00_L12_USER_CLEAN_REGRESSION`; `BUILD SUCCESSFUL in 24s`; five actionable tasks, all executed.

## Step 14 — Complete bounded User Simulation

**Objective:** Verify Noop composition and safe Elevator telemetry through the accepted robot mode checkpoints.

**Checkpoint 1:** `PASS_M00_L12_SIMULATION_DISABLED_BASELINE`; Disabled, Driver Station attached, `FMSControlData = 32`. Elevator reported `Available=false`, `Connected=false`, `PositionValid=false`, `PositionReferenced=false`, `PositionMeters=0.0`, `RequestedState=STOPPED`, `TargetPositionMeters=0.0`, and `PositionErrorMeters=0.0`.

**Checkpoint 2:** `PASS_M00_L12_SIMULATION_TELEOP_ENABLED_NO_UNCOMMANDED_HOMING`; Teleoperated, Robot Enabled = Yes, `FMSControlData = 33`. The same Noop values remained; no automatic homing or uncommanded Elevator output occurred.

**Checkpoint 3:** Return-to-Disabled checkpoint; Robot Enabled = No, Driver Station attached, `FMSControlData = 32`. The same Noop values remained, with no fabricated reference or motion and a safe return to Disabled. No separate checkpoint-3 gate name was supplied.

**Verification:** The complete bounded run passed as `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION`. It is software/runtime and Noop-composition evidence only. There is no ElevatorIOSim, real Elevator adapter, or HomeElevatorCommand binding, so it does not prove physical homing.

## Step 15 — Reconcile current documentation

**Objective:** Record implementation, accepted test and Simulation evidence, and the correct current lifecycle in the existing current-state blocks.

**Action:** Update the current M00_L12 records in governance summaries, roadmap ADR, lesson README/status/plan/checklist, and this guide in place. Preserve the activation block and all prior lesson snapshots as history. Record evidence as `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED` and explicitly limit Simulation claims to the checkpoints above.

**Expected Result:** `DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`.

## Step 16 — Reconcile the accepted closure review and controlled freeze

**Objective:** Record the accepted closure verdict and transition lifecycle to `COMPLETE / FROZEN / READ-ONLY` without claiming publication.

**Why:** The independent closure review passed and authorized the separate freeze step. Freeze is a documentation/lifecycle transition; publication remains a later User-owned Git workflow.

**Action:** Update the existing current M00_L12 records in the governance summaries, roadmap ADR, lesson README/status/plan/checklist, and this guide. Preserve the full implementation and verification chronology. Record Active Lesson Count `0`, Current Active M00 Lesson `NONE`, M00_L13 `INACTIVE / NOT CREATED`, Independent Freeze Review `PENDING`, and publication `NOT PUBLISHED / PENDING` with no publication SHA.

**Verification:** Accepted gate `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW`, verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`, and remaining legitimate findings `NONE`.

**Expected Result:** `FREEZE_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_FREEZE_REVIEW`.

## Current gate sequence

Implementation, final static review, User focused tests, User clean regression, bounded Simulation, documentation reconciliation, and Independent Closure Review are complete and accepted. Freeze Reconciliation is complete. M00_L12 is `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`, Active Lesson Count `0`, Current Active M00 Lesson `NONE`. Independent Freeze Review is the next gate and remains `PENDING`. Publication and final publication verification remain pending User-owned Git workflow; no M00_L12 publication SHA is established. M00_L13 is `INACTIVE / NOT CREATED`. Real hardware remains `DEFERRED`.

## Evidence boundary

`THEORY VERIFIED` means the accepted design, implementation, static review, focused tests, and clean regression support the software contract. `SIMULATION VERIFIED` refers only to bounded software/runtime safety, truthful Noop observations, no automatic/uncommanded homing, safe Disabled → Teleop enabled → Disabled lifecycle, and the zero-is-not-home software semantic. It does not prove physical homing, motor movement, sensor activation, physical calibration or zero, reference accuracy, hardware communication, convergence, or mechanism safety. The real sensor/reference method and other hardware facts remain unknown/deferred. `REAL HARDWARE DEFERRED` remains explicit.
