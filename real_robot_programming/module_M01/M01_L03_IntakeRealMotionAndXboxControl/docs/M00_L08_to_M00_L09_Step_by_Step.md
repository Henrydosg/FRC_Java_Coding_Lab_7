# M00_L08 to M00_L09 Step-by-Step Transition Guide

## Scope and stopping point

This guide records only the controlled preparation and activation of the
M00_L09 Flywheel Ready-at-Speed candidate. It stops before independent
activation review, production implementation, test changes, Simulation,
closure review, freeze, and publication. No future event is represented as
complete.

## Step 1 — Confirm the frozen predecessor

**Objective:** Establish the authoritative inheritance source.

**Why:** The frozen workflow requires the next lesson to inherit from the
immediately preceding completed lesson.

**Action:** Confirmed canonical M00_L08 as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

**Files Changed:** None; predecessor inspection only.

**Verification:** Canonical external publication evidence is the primary
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` and metadata
`a76dc33c2b485b4988e7058cbfed0fa3362cc560` (metadata parent =
`5daecd970ff95fb906d6de9d5bc22a5cb094877d`), with
final remote-aligned HEAD equal to the metadata SHA and verdict
`PUBLICATION_VERIFIED`. The passing two-commit Historical Snapshot Model
allows frozen M00_L08 local snapshots to retain historical pending-publication
wording; that wording is not the canonical external state.

**Expected Result:** M00_L08 remains untouched and authoritative. **PASS**

## Step 2 — Copy and rename the predecessor

**Objective:** Prepare M00_L09 by inheritance rather than recreation.

**Why:** One lesson is one independent WPILib project and the lifecycle requires
copying the previous completed lesson.

**Action:** The User copied
`M00_L08_FlywheelClosedLoopVelocity` to
`M00_L09_FlywheelReadyAtSpeed`.

**Files Changed:** New inherited candidate prepared by the User.

**Verification:** Candidate contains the inherited source, tests,
configuration, deployment content, and lesson documentation.

**Expected Result:** One M00_L09 candidate exists. **PASS**

## Step 3 — Remove copied generated artifacts

**Objective:** Keep generated outputs outside governed inheritance content.

**Why:** Build caches and generated outputs are not source or design evidence.

**Action:** The User removed copied generated artifacts before the baseline.

**Files Changed:** Candidate generated-artifact state only.

**Verification:** Preparation record identifies an untouched governed copy.

**Expected Result:** Governed source, tests, configuration, deployment, and
documentation remain inherited. **PASS**

## Step 4 — Run the untouched-copy baseline

**Objective:** Establish a usable inherited baseline before design activation.

**Why:** Architecture and design activation require a verified starting point.

**Action:** The User ran the untouched-copy baseline build.

**Files Changed:** None by lesson design; generated build outputs are excluded.

**Verification:** `PASS_M00_L09_PREPARATION_BASELINE`; `BUILD SUCCESSFUL in
34s`; six actionable tasks, six executed.

**Expected Result:** Baseline is accepted before activation. **PASS**

## Step 5 — Confirm inheritance and architecture gates

**Objective:** Confirm the candidate preserves the predecessor and governance
architecture.

**Why:** The new lesson may add only its approved concept and must preserve the
Frozen Backbone, Observation architecture, IO boundary, and composition root.

**Action:** Reviewed the candidate against frozen M00_L08 and the applicable
governance documents.

**Files Changed:** None by audit.

**Verification:** Production inheritance `103 / 103 / 0 / 0 / 0`; test
inheritance `96 / 96 / 0 / 0 / 0`; deploy/configuration inheritance
`24 / 24 / 0 / 0 / 0`; `PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT`.

**Expected Result:** Inheritance and architecture are preserved. **PASS**

## Step 6 — Record the accepted Final Design Lock

**Objective:** Bound the lesson before activation.

**Why:** Controlled activation may proceed only after a written, reviewable
design boundary.

**Action:** Accepted `PASS_M00_L09_FINAL_DESIGN_LOCK` with verdict
`READY_FOR_CONTROLLED_ACTIVATION` for exactly one concept: instantaneous,
vendor-neutral Flywheel Ready-at-Speed classification. The locked predicate
uses positive finite velocity intent, valid connected measurement, and the
inclusive symmetric `Math.abs(measured-target) <= 50.0` mechanism-RPM tolerance.
The tolerance is provisional software policy only, not hardware-tuned or
real-robot validated. Readiness is subsystem-owned, immutable in Observation,
published as exactly `ReadyAtSpeed`, and has no dwell, debounce, history,
command, coordination, or automatic action.

**Files Changed:** Governance and candidate documentation only.

**Verification:** Final Design Lock gate accepted; implementation remains
`NOT STARTED`.

**Expected Result:** The exact activation boundary is recorded. **PASS**

## Step 7 — Controlled Activation

**Objective:** Make M00_L09 the sole active editable lesson within the locked
boundary.

**Why:** Activation establishes lifecycle identity; it does not implement the
lesson.

**Action:** Updated only the approved activation records and created this
transition guide. M00_L09 is now `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL
DESIGN LOCK`; M00_L08 remains frozen and M00_L10 remains inactive/not created.

**Files Changed:** `AGENTS.md`, `README.md`,
`docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`,
the M00_L09 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`,
`LESSON_CHECKLIST.md`, and this guide.

**Verification:** Active Lesson Count is `1`; Current Active M00 Lesson is
`M00_L09`; implementation is `NOT STARTED`; Independent Activation Review is
`PENDING`.

**Expected Result:** Controlled Activation is complete and ready for independent
activation review. **PASS**

## Explicit stopping point

No production source, test source, Constants implementation, build, test,
Simulation, real-robot verification, closure, freeze, publication, or Git
operation occurred in this transition.

## Documentation repair reconciliation — 2026-09-21

The accepted Architect gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`; the
engineer-owned Controlled Activation verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
Independent Activation Review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW` and requires re-review. This
repair records the locked measured-zero clarification, forwarding-exception
semantics, telemetry contract and exclusions, automatic-action prohibition,
truth-table and floating-point policies, focused-test matrix, evidence plan,
and deferred hardware boundary without changing the Design Lock or adding
implementation. M00_L09 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL
DESIGN LOCK`, implementation `NOT STARTED`, and M00_L10 remains inactive/not
created.

**Verification:** Documentation repair only; no source, tests, build,
Simulation, freeze, publication, or Git operation. **PASS — READY FOR
INDEPENDENT RE-REVIEW**

## Final implementation and verification reconciliation — 2026-09-21

## Step 8 — Accept implementation authorization

**Objective:** Record the exact implementation boundary authorized after the
Design Lock.

**Why:** Implementation evidence must remain attributable to the approved
scope and must not expand the lesson.

**Action:** Accepted `PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED` for exactly
four production files and four focused test files. No source change was made by
this documentation reconciliation.

**Files Changed:** Documentation records only.

**Verification:** The implementation boundary and one-concept scope remain
unchanged. **PASS**

**Expected Result:** The implementation gate is recorded without authorizing
any unrelated change. **PASS**

## Step 9 — Record implementation integrity

**Objective:** Reconcile the completed Ready-at-Speed implementation.

**Why:** The current documentation must identify the exact inherited delta.

**Action:** Recorded production `103 / 99 / 4 / 0 / 0` and test
`96 / 92 / 4 / 0 / 0` counts. Production changes are limited to
`Constants.java`, `FlywheelObservation.java`, `FlywheelSubsystem.java`, and
`FlywheelTelemetryFacade.java`; test changes are limited to the four authorized
Ready-at-Speed test files. Deploy/configuration is `4 / 4 / 0 / 0 / 0`; no new
files or unrelated boundaries changed.

**Files Changed:** Documentation records only.

**Verification:** IO, Noop, RobotTelemetry, RobotContainer, commands,
autonomous, deployment/configuration, and frozen M00_L08 remain protected.
**PASS**

**Expected Result:** Implementation scope and inheritance are reconciled.
**PASS**

## Step 10 — Preserve initial Independent Static Review HOLD

**Objective:** Preserve the initial static-review chronology.

**Why:** A later PASS must not erase the earlier findings or review state.

**Action:** Recorded the initial `HOLD` for cached-input, telemetry,
Observation, architecture, and policy-document findings.

**Files Changed:** Documentation records only.

**Verification:** The initial HOLD remains historical and is not the current
review verdict. **PASS**

**Expected Result:** Review chronology remains auditable. **PASS**

## Step 11 — Record bounded static-review repair PASS

**Objective:** Record the bounded repair that resolved the initial findings.

**Why:** The final static review depends on the bounded repair evidence.

**Action:** Recorded the approved Constants documentation clarification and the
four focused test repairs, with no production runtime expansion.

**Files Changed:** Documentation records only.

**Verification:** Bounded static-review repair passed. **PASS**

**Expected Result:** The exact repair scope is preserved. **PASS**

## Step 12 — Record final Independent Static Re-review PASS

**Objective:** Reconcile the final static-review verdict.

**Why:** User-focused testing proceeded only after the final re-review passed.

**Action:** Recorded final Independent Static Re-review `PASS` for the locked
Ready-at-Speed implementation and its bounded tests.

**Files Changed:** Documentation records only.

**Verification:** Final static re-review passed. **PASS**

**Expected Result:** The lesson is eligible for user-focused verification.
**PASS**

## Step 13 — Record user focused-test evidence

**Objective:** Reconcile the focused test gate supplied by the User.

**Why:** Codex records user evidence but does not claim to have run it.

**Action:** Recorded `PASS_M00_L09_USER_FOCUSED_TESTS`, `BUILD SUCCESSFUL in
22s`, four actionable tasks, three executed and one up-to-date.

**Files Changed:** Documentation records only.

**Verification:** Exact user-supplied focused-test evidence. **PASS**

**Expected Result:** Focused verification is accepted without unsupported
execution claims. **PASS**

## Step 14 — Record clean full-regression evidence

**Objective:** Reconcile the clean full-regression gate supplied by the User.

**Why:** Full regression confirms inherited behavior remained intact.

**Action:** Recorded `PASS_M00_L09_CLEAN_FULL_REGRESSION`, `BUILD SUCCESSFUL`,
seven actionable tasks, all seven executed.

**Files Changed:** Documentation records only.

**Verification:** Exact user-supplied clean regression evidence. **PASS**

**Expected Result:** Full regression is accepted without unsupported execution
claims. **PASS**

## Step 15 — Record Simulation checkpoint 1

**Objective:** Record the Disabled baseline checkpoint.

**Why:** The bounded Simulation must establish safe idle behavior before and
after Teleop.

**Action:** Recorded `PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`: Disabled,
FMS Robot Enabled=No, DS Attached=Yes; Available=false, Connected=false,
ReadyAtSpeed=false, RequestedState=STOPPED, VelocityRpm=0.0,
VelocityValid=false.

**Files Changed:** Documentation records only.

**Verification:** Exact user-supplied checkpoint. **PASS**

**Expected Result:** Disabled Noop state is safe and observable. **PASS**

## Step 16 — Record Simulation checkpoint 2

**Objective:** Record the Teleop-idle checkpoint.

**Why:** Teleop must not request or actuate the flywheel without operator input.

**Action:** Recorded `PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`:
Teleoperated enabled, Robot Enabled=Yes, no controller/joystick action, with
the same Flywheel values as checkpoint 1.

**Files Changed:** Documentation records only.

**Verification:** Exact user-supplied checkpoint. **PASS**

**Expected Result:** Teleop idle remains stopped and fail-safe. **PASS**

## Step 17 — Record Simulation checkpoint 3

**Objective:** Record the return-to-Disabled checkpoint.

**Why:** The bounded lifecycle must remain safe after Teleop.

**Action:** Recorded `PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`: Disabled
again, FMS Robot Enabled=No, with the same Flywheel values.

**Files Changed:** Documentation records only.

**Verification:** Exact user-supplied checkpoint. **PASS**

**Expected Result:** Disabled state remains stopped and observable. **PASS**

## Step 18 — Accept bounded Simulation interpretation

**Objective:** Bound the meaning of the three checkpoints.

**Why:** Noop lifecycle evidence cannot certify physical mechanism behavior.

**Action:** Accepted `PASS_M00_L09_BOUNDED_SIMULATION` for Noop composition,
telemetry presence, fail-safe `ReadyAtSpeed=false`, `STOPPED` idle, no
automatic Teleop request, no readiness-triggered actuation, and
Disabled→Teleop→Disabled persistence.

**Files Changed:** Documentation records only.

**Verification:** The evidence does not claim `ReadyAtSpeed=true`, runtime
50-RPM boundary exercise, physical convergence, sensor fidelity, CAN behavior,
or physical hardware. **PASS**

**Expected Result:** Simulation scope is explicit and bounded. **PASS**

## Step 19 — Complete Documentation Reconciliation

**Objective:** Reconcile the current lifecycle and stop before closure.

**Why:** Documentation must reflect completed implementation and user evidence
without authorizing freeze or publication.

**Action:** Updated only the eight authorized paths: `AGENTS.md`, root
`README.md`, the M00 ADR, the M00_L09 `README.md`, `LESSON_STATUS.md`,
`LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this transition guide. Recorded
evidence classification exactly as `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`; hardware and CAN 50–54 remain unknown/deferred.

**Files Changed:** The eight authorized documentation/lifecycle paths only.

**Verification:** M00_L09 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN
FINAL DESIGN LOCK`; implementation, static review, focused tests, clean
regression, bounded Simulation, and Documentation Reconciliation are complete.
Independent Closure Review is pending; Freeze and Publication are not
authorized. Active Lesson Count is `1`, current active lesson is M00_L09, and
M00_L10 is inactive/not created. **PASS**

**Expected Result:** Documentation reconciliation is complete and ready for
Independent Closure Review, with no closure, freeze, publication, or successor
activation performed. **PASS**

## Focused-test execution clarification — 2026-09-21

**Objective:** Make the accepted focused-test invocation explicit without
changing any implementation or verification result.

**Why:** Four modified test files and four actionable Gradle tasks must not be
mistaken for four executed test classes.

**Action:** The User ran one focused Gradle invocation selecting all six
Flywheel-focused classes:

1. `frc.robot.FlywheelArchitectureBoundaryTest`
2. `frc.robot.observation.flywheel.FlywheelObservationTest`
3. `frc.robot.subsystems.FlywheelSubsystemTest`
4. `frc.robot.telemetry.flywheel.FlywheelTelemetryFacadeTest`
5. `frc.robot.io.flywheel.FlywheelIONoopTest`
6. `frc.robot.RobotContainerFlywheelCompositionTest`

The invocation ended `BUILD SUCCESSFUL in 22s` with 4 actionable tasks: 3
executed and 1 up-to-date. Gate: `PASS_M00_L09_USER_FOCUSED_TESTS`. The four
M00_L09 test files were modified; the two inherited protected tests were
intentionally included as regression checks while remaining byte-identical to
frozen M00_L08. Gradle task counts are not test-class counts, and six Gradle
tasks are not claimed.
Independent Closure Review remains `HOLD — RE-REVIEW REQUIRED`; Freeze and
Publication remain `NOT AUTHORIZED`.

**Files Changed:** Documentation records only.

**Verification:** All six selectors and the exact user-supplied result are now
explicitly recorded; no build, test, Simulation, Git, closure, freeze,
publication, or successor operation was performed by this repair. **PASS**

**Expected Result:** The focused-test documentation HOLD is resolved and the
record is ready for independent closure re-review. **PASS**

## Controlled freeze transition — 2026-09-21

## Step 21 — Record Final Independent Closure Re-review

**Objective:** Record the completed closure gate before freezing the lesson.

**Why:** Freeze requires an independent closure PASS.

**Action:** Recorded `PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW` with
verdict `READY_FOR_FREEZE_AUTHORIZATION`.

**Files Changed:** Authorized lifecycle documentation only.

**Verification:** Final closure re-review passed; no source, tests, build,
Simulation, Git, or publication operation occurred. **PASS**

**Expected Result:** Architect freeze authorization may be consumed. **PASS**

## Step 22 — Record Architect Freeze Authorization

**Objective:** Record the authorization that permits controlled freeze.

**Why:** The transition must distinguish authorization from the applied freeze
state.

**Action:** Recorded Architect authorization under
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`.

**Files Changed:** Authorized lifecycle documentation only.

**Verification:** Freeze was authorized and not publication-authorized. **PASS**

**Expected Result:** Controlled freeze transition may proceed. **PASS**

## Step 23 — Apply controlled freeze lifecycle transition

**Objective:** Transition M00_L09 to its frozen read-only state.

**Why:** All pre-freeze gates passed and the lesson must no longer be editable.

**Action:** Recorded M00_L09 as `COMPLETE / FROZEN / READ-ONLY`, set Active
Lesson Count to `0`, and set Current Active M00 Lesson to `NONE`.

**Files Changed:** The eight authorized documentation/lifecycle paths only.

**Verification:** Evidence remains `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`; M00_L10 remains `INACTIVE / NOT CREATED`. **PASS**

**Expected Result:** M00_L09 is frozen without runtime or source changes. **PASS**

## Step 24 — Preserve post-freeze pending gates

**Objective:** Keep later governance stages distinct from freeze.

**Why:** Freeze does not publish the lesson or complete independent freeze
review.

**Action:** Recorded Independent Freeze Review as `PENDING` and Publication as
`PENDING / NOT YET PUBLISHED`; no publication SHA or Git event was recorded.

**Files Changed:** Authorized lifecycle documentation only.

**Verification:** Chronology stops before Independent Freeze Review result,
publication authorization, Git publication, metadata verification, and M00_L10
creation. **PASS**

**Expected Result:** Controlled Freeze Transition is complete and ready for
Independent Freeze Review. **PASS**
