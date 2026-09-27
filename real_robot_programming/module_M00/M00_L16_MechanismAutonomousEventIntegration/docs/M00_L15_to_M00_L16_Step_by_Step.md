# M00_L15 to M00_L16 — Step by Step Transition Guide

Status: FINAL / PASS FOR FREEZE RECONCILIATION / READY FOR INDEPENDENT FREEZE REVIEW  
Current lesson: M00_L16 — Mechanism Autonomous Event Integration  
Current lifecycle: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED  
Previous lesson: M00_L15 — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED  
Active M00 lesson count: 0  
Current active M00 lesson: NONE  
Real hardware: DEFERRED

This guide records accepted evidence separately from planned gates. The
one new concept is scheduler-managed dispatch of the existing
`IntakeToFeederCommand` through the inherited `LEARNING_EVENT` named-event
boundary. Implementation, focused tests, clean regression, bounded
Simulation, documentation reconciliation, independent closure review, and
freeze reconciliation are complete under accepted evidence. Each numbered
step records one change or gate and its reviewable result. Independent Freeze
Review and publication remain pending.

## Step 1 — Verify M00_L15 final publication

- **Objective:** Establish the frozen predecessor.
- **Why:** L16 must inherit only the final published M00_L15 snapshot.
- **Action:** Accept User-owned primary snapshot `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`, metadata publication `0d3685ce67a0b985459392621e003611eaa6dc35`, and `PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`.
- **Files Changed:** None by this L16 activation; M00_L15 remains frozen.
- **Verification:** Accepted predecessor publication evidence; COMPLETE.
- **Expected Result:** M00_L15 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.

## Step 2 — Copy frozen M00_L15 into M00_L16

- **Objective:** Prepare the independent L16 WPILib project.
- **Why:** The inheritance workflow forbids recreating the lesson from scratch.
- **Action:** User-prepared copy and rename of frozen M00_L15 to `M00_L16_MechanismAutonomousEventIntegration`.
- **Files Changed:** New M00_L16 lesson copy; no predecessor file modified.
- **Verification:** Accepted untouched-copy inventory; 345/345 authored files later confirmed identical.
- **Expected Result:** Prepared L16 candidate with inherited architecture and no L16 implementation.

## Step 3 — Remove copied generated artifacts

- **Objective:** Keep generated build state out of the authored baseline.
- **Why:** The standard lesson workflow requires cleanup after copying.
- **Action:** User removed copied `build/` and `.gradle/` artifacts before baseline verification.
- **Files Changed:** Generated artifacts in the new candidate only; no authored source, test, or deploy file changed.
- **Verification:** Accepted preparation and untouched-copy audit evidence; COMPLETE.
- **Expected Result:** Clean candidate ready for its own baseline build.

## Step 4 — Run untouched-copy baseline build

- **Objective:** Verify the inherited project before L16 changes.
- **Why:** The baseline separates inherited behavior from future implementation.
- **Action:** User ran the accepted baseline workflow.
- **Files Changed:** None in authored lesson files.
- **Verification:** `PASS_M00_L16_UNTOUCHED_COPY_BASELINE_BUILD`; User-reported BUILD SUCCESSFUL in 1m 1s; BUILD_EXIT_CODE=0.
- **Expected Result:** Untouched inherited candidate builds successfully.

## Step 5 — Complete architecture and inheritance audit

- **Objective:** Confirm frozen provenance and the event integration boundary.
- **Why:** The new concept must preserve the Frozen Backbone and M00 roadmap.
- **Action:** Independently audit governance, copied files, relevant source, tests, and event path.
- **Files Changed:** None.
- **Verification:** `PASS_M00_L16_ARCHITECTURE_INHERITANCE_AUDIT`; 345/345 authored, 113/113 production Java, and 106/106 test Java files identical.
- **Expected Result:** L16 remains an untouched, architecture-compatible copy.

## Step 6 — Approve the Final Design Lock

- **Objective:** Select exactly one command and event integration design.
- **Why:** Implementation needs a bounded concept, file budget, and safety contract.
- **Action:** Lock `IntakeToFeederCommand` on existing `LEARNING_EVENT`; plan a fresh child per dispatch with exact IntakeSubsystem and FeederSubsystem requirements through inherited `Supplier<Command>` and `Commands.defer(...)`. Exclude `ShootCommand`, new event IDs, paths, timing policy, and helper changes.
- **Files Changed:** None by the design review.
- **Verification:** `PASS_M00_L16_FINAL_DESIGN_LOCK`; `M00_L16_FINAL_DESIGN_LOCK_PASS_READY_FOR_CONTROLLED_ACTIVATION`.
- **Expected Result:** Future production delta limited to `RobotContainer.java`; no implementation authorized yet.

Normal hold-event cleanup was verified through `FollowPathCommand.end(...)`
→ `EventScheduler.end()` → active event `end(true)` → wrapped registered
event → `DeferredCommand.end(true)` → `IntakeToFeederCommand.end(...)`
→ `feeder.stop()` → `intake.stop()`. This covers normal path completion
and interruption. It does not guarantee cleanup after an arbitrary
uncaught library exception. The inherited supplier-`RuntimeException`
boundary publishes `FACTORY_FAILURE` and returns a safe no-op without
failed-child actuation.

## Step 7 — Perform controlled activation (historical gate)

- **Objective:** Make M00_L16 the sole active editable M00 lesson.
- **Why:** L16 must enter the normal lifecycle before separately authorized implementation.
- **Action:** Reconcile current lifecycle and locked design in AGENTS.md, root README.md, the M00 ADR, four L16 lesson-local documents, and this guide. Preserve M00_L15 and mark later gates pending.
- **Files Changed:** `AGENTS.md`; `README.md`; `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`; L16 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide.
- **Verification:** `PASS_M00_L16_CONTROLLED_ACTIVATION`; documentation self-review and governance mirror validation; production, tests, deploy, vendordeps, and Gradle/config untouched at this historical gate.
- **Expected Result:** M00_L16 IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK; Independent Activation Review PENDING; Implementation Authorization PENDING.

## Step 8 — Complete independent activation review

- **Objective:** Review the active lesson boundary.
- **Why:** Implementation requires an independent gate.
- **Action:** Reviewer examined activation scope and frozen protection.
- **Files Changed:** None by review.
- **Verification:** `PASS_M00_L16_INDEPENDENT_ACTIVATION_REVIEW`, accepted.
- **Expected Result:** L16 remained the sole editable M00 lesson.

## Step 9 — Obtain implementation authorization

- **Objective:** Release the exact locked implementation scope.
- **Why:** Activation did not itself authorize source changes.
- **Action:** Architect/User separately authorized the production and test boundary.
- **Files Changed:** None by authorization.
- **Verification:** `PASS_M00_L16_IMPLEMENTATION_AUTHORIZATION`, accepted.
- **Expected Result:** Only the bounded L16 delta was released.

## Step 10 — Bind the existing event in RobotContainer

- **Objective:** Dispatch the existing mechanism command through LEARNING_EVENT.
- **Why:** This is L16's one new concept.
- **Action:** Replace the demonstration supplier with a fresh IntakeToFeederCommand(intakeSubsystem, feederSubsystem) per dispatch and exact Intake + Feeder requirements.
- **Files Changed:** L16 src/main/java/frc/robot/RobotContainer.java only.
- **Verification:** `PASS_M00_L16_IMPLEMENTATION`; source comparison and later static rereview PASS.
- **Expected Result:** Existing deferred registration dispatches a fresh child; teleop and chooser remain unchanged.

## Step 11 — Add focused integration tests

- **Objective:** Check the event binding contract.
- **Why:** L16 must verify dispatch, requirements, lifecycle, contention, and failure behavior.
- **Action:** Add exactly eight integration test methods.
- **Files Changed:** Added L16 src/test/java/frc/robot/RobotContainerMechanismAutonomousEventIntegrationTest.java.
- **Verification:** File inspection confirms eight tests; User focused run later passed 8/8.
- **Expected Result:** The new concept has direct scheduler and factory-failure coverage.

## Step 12 — Update the inherited architecture guard

- **Objective:** Align the copied guard with the approved event reference.
- **Why:** The L15 prohibition on any RobotContainer reference is stale for L16.
- **Action:** Update the event-reference expectation while preserving teleop, default-command, and direct-access protections.
- **Files Changed:** L16 src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java only.
- **Verification:** Initial implementation review found a remaining guard issue; Step 14 records its bounded repair.
- **Expected Result:** The guard allows only the approved integration.

## Step 13 — Record initial independent static review

- **Objective:** Audit the implementation before executable verification.
- **Why:** The exact file boundary and architecture protections require review.
- **Action:** Reviewer returned HOLD for the architecture guard.
- **Files Changed:** None by review.
- **Verification:** Initial independent static review HOLD, preserved as historical finding.
- **Expected Result:** Only the identified guard issue proceeds to bounded repair.

## Step 14 — Repair the architecture guard

- **Objective:** Clear the bounded static finding.
- **Why:** The test must enforce the approved event boundary without weakening inherited protections.
- **Action:** Repair only the guard identified by the initial review.
- **Files Changed:** L16 src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java only.
- **Verification:** `PASS_M00_L16_STATIC_REVIEW_REPAIR`; no production change.
- **Expected Result:** Guard is ready for fresh independent review.

## Step 15 — Complete independent static rereview

- **Objective:** Confirm the repaired implementation boundary.
- **Why:** Verification proceeds after static findings are cleared.
- **Action:** Reviewer reinspected the source and repaired guard.
- **Files Changed:** None by review.
- **Verification:** Fresh independent static rereview PASS, accepted.
- **Expected Result:** L16 proceeds to User execution gates.

## Step 16 — Run focused tests

- **Objective:** Verify the L16 integration behavior.
- **Why:** Scheduler and architecture claims require executable evidence.
- **Action:** User ran the architecture guard focused methods and the eight integration tests.
- **Files Changed:** None in authored files by the run.
- **Verification:** PASS; all focused methods and 8/8 integration tests passed; BUILD SUCCESSFUL; exit code 0.
- **Expected Result:** Exact requirements, fresh child, semantic requests, lifecycle, contention, and safe factory failure pass.

## Step 17 — Run clean regression

- **Objective:** Check inherited contracts after the bounded delta.
- **Why:** Event integration can affect prior autonomous and mechanism behavior.
- **Action:** User ran the clean regression.
- **Files Changed:** None in authored files by the run.
- **Verification:** PASS; BUILD SUCCESSFUL, all tests shown passed, no regression blocker. Numeric test count and exit code were not supplied.
- **Expected Result:** Inherited regression remains green within supplied evidence.

## Step 18 — Perform bounded Simulation

- **Objective:** Verify event-path runtime semantics with an event-free control.
- **Why:** Focused tests alone do not show the path marker at runtime.
- **Action:** User ran ONE_METER_WITH_EVENT and ONE_METER_PATH with Driver Station attached.
- **Files Changed:** None in authored files by the run.
- **Verification:** PASS_M00_L16_BOUNDED_SIMULATION: Autonomous RUNNING at LEARNING_EVENT with Intake INTAKE_REQUESTED, Feeder FEED_REQUESTED, Flywheel/Elevator STOPPED; Intake/Feeder STOPPED on Disable/interruption. Control path ran without event and kept both STOPPED. No fatal scheduler/runtime exception observed; normal return to PowerShell prompt. Final Gradle output: five actionable tasks, three executed, two up-to-date. No Simulation exit code supplied.
- **Expected Result:** Semantic dispatch and cleanup verified in Simulation; physical behavior remains deferred.

## Step 19 — Reconcile documentation

- **Objective:** Record the exact accepted evidence and limitations.
- **Why:** The guide and lifecycle records must reflect completed implementation and verification.
- **Action:** Update L16 local records and current repository lifecycle sections; preserve earlier gate text as historical.
- **Files Changed:** AGENTS.md, root README.md, M00 ADR, L16 README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, and this guide only.
- **Verification:** Documentation self-review; PASS_M00_L16_DOCUMENTATION_RECONCILIATION.
- **Expected Result:** THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, with closure and freeze pending.

Blank AutonomousEvent NT fields do not contradict the Simulation result. The
facade publishes only an existing observation, RobotContainer starts with
Optional.empty, the child emits no success lifecycle observation, and
registration emits only FACTORY_FAILURE. No LastEvent or DispatchCount claim
is made. No physical transfer, motor, sensor, timing, or electrical claim is
made from Noop adapters.

## Step 20 — Independent closure review

- **Objective:** Review implementation, verification, and documentation closure.
- **Why:** Documentation reconciliation does not close the lesson.
- **Action:** The first Independent Closure Review returned HOLD because the README placed completed focused-test and Simulation evidence beside same-level future-looking planning headings. The bounded repair changed only the L16 README, nesting those plan sections beneath Historical lifecycle and labeling them historical and superseded. A fresh independent closure rereview then passed.
- **Files Changed:** None by review.
- **Verification:** Initial `HOLD_M00_L16_INDEPENDENT_CLOSURE_REVIEW`; bounded `PASS_M00_L16_CLOSURE_DOCUMENTATION_REPAIR`; fresh `PASS_M00_L16_INDEPENDENT_CLOSURE_REVIEW` with final verdict `M00_L16_INDEPENDENT_CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION` and no remaining findings.
- **Expected Result:** Closure evidence and documentation are accepted for freeze reconciliation.

## Step 21 — Reconcile freeze

- **Objective:** Freeze the lesson after accepted closure.
- **Why:** COMPLETE lessons become read-only snapshots.
- **Action:** Reconcile current lifecycle records to the accepted closure state; set M00_L16 to COMPLETE / FROZEN / READ-ONLY, active lesson count 0, preserve THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, and leave Independent Freeze Review and publication pending. Preserve the closure HOLD, bounded repair, and fresh rereview chronology.
- **Files Changed:** `AGENTS.md`; repository `README.md`; M00 roadmap ADR; L16 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide. No production, test, deploy, build, or configuration file changed.
- **Verification:** `PASS_M00_L16_FREEZE_RECONCILIATION`; documentation self-review; governance mirror validation PASS (12 VERIFIED mirrors, 12 matching PDF hashes, 12 matching Markdown hashes, zero deterministic findings).
- **Expected Result:** M00_L16 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED and ready for Independent Freeze Review.

## Step 22 — Independent freeze review

- **Objective:** Verify the proposed frozen snapshot.
- **Why:** Publication follows reviewed freeze.
- **Action:** PENDING; independent reviewer examines the reconciled frozen snapshot.
- **Files Changed:** None by review.
- **Verification:** PENDING.
- **Expected Result:** Independent freeze verdict.

## Step 23 — User-owned publication

- **Objective:** Publish the final M00 lesson.
- **Why:** Git commit and push are User-owned.
- **Action:** PENDING; publication remains pending until Independent Freeze Review passes and the User-owned publication workflow is explicitly authorized and performed.
- **Files Changed:** PENDING; publication metadata only if separately authorized.
- **Verification:** PENDING / external User evidence.
- **Expected Result:** Publication claimed only after accepted commit, push, and final verification.
