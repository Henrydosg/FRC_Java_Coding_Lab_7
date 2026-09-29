# M00_L15 to M00_L16 — Step by Step Transition Guide

## Current CFG-H01 governance registration — 2026-09-29

This dated record controls current repair status and future scope. The
2026-09-28 P3-H01 registration, initial pending gates, original decision,
and earlier lifecycle passages below are preserved historical stage records.
Their former NOT STARTED/PENDING statements do not describe today's P3-H01
implementation or supplied fresh verification. Standing governance rules and
unresolved separate closure gates remain in force.

- Status: IN_PROGRESS.
- Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Repository Active Lesson Count: 1; Active M00 Lesson Count: 1.
- Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Stage 1 — P3-H01: IMPLEMENTED / FRESHLY VERIFIED / NOT REOPENED.
- Stage 2 — CFG-H01: GOVERNANCE REGISTERED / DESIGN APPROVED.
- CFG-H01 Implementation Authorization: PENDING / NOT AUTHORIZED.
- CFG-H01 Implementation: NOT STARTED.
- Accepted CFG-H01 design: `PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.
- Architect/User approval for this turn: the nine existing documentation
  files only; no production or test implementation permission.
- Governing amendment: [existing exceptional-repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md), dated CFG-H01 supplement.

### Preserved P3-H01 fresh evidence

The current User-supplied evidence establishes implementation complete,
focused test PASS, full suite PASS, clean build PASS, and fresh Simulation PASS.
Vision qualification/fusion, event-path runtime, LEARNING_EVENT Intake/Feeder
semantic behavior, cleanup, consumed-readiness fail-closed, and normal
Simulation exit are PASS within the supplied observation scope.
No test count, exit code, hardware result, or new publication identity is inferred.

Localization initialization precedes expected accepted Vision fusion.
Successful LEARNING_EVENT runtime evidence is
`/Intake/RequestedState` and `/Feeder/RequestedState`; successful
AutonomousEvent lifecycle telemetry is not the evidence.
Preserve these procedure corrections and P3-H01 runtime PASS without
relabeling them as post-CFG-H01 execution.

### CFG-H01 registered future boundary

CFG-H01 is ONE inherited MISPLACED_CONFIGURATION finding: the real adapter's
private `kLimelightTableName = "limelight"` owns robot/device table identity.
The earliest surviving occurrence is V00_L08, propagated through V00_L09 and
M00_L01-M00_L16 (18 lessons, one finding). No endpoint error, runtime failure,
or hardware safety failure is established. Historical lessons remain untouched.

All following paths are relative to existing M00_L16:

| Future file | Exact bounded change; separately authorized later |
| --- | --- |
| `src/main/java/frc/robot/Constants.java` | Add `public static final String kLimelightTableName = "limelight";` first in existing VisionConstants, before calibration fields; narrow class JavaDoc if needed; preserve every current declaration/value. |
| `src/main/java/frc/robot/io/vision/VisionIOLimelight.java` | Import Constants, remove the private endpoint default, and make the public default constructor consume `Constants.VisionConstants.kLimelightTableName`; preserve the injected NetworkTable constructor and all protocol/parsing/session behavior. |
| `src/test/java/frc/robot/VisionConfigurationAuthorityTest.java` | Extend this existing test only: declaration shape, independent value, executable source origin/removal, and actual default `/limelight/json` endpoint proof. |

Exactly two existing production files and one existing test file form the
future CFG-H01 boundary; zero new production/test files. RobotContainer and
VisionIOLimelightTest remain protected. P3-H01's original authorization did
not cover VisionIOLimelight or this subsequent existing-test extension.

### Approved CFG-H01 verification applicability

| Gate | Disposition / current result |
| --- | --- |
| Focused VisionConfigurationAuthorityTest | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Unchanged VisionIOLimelight tests | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Relevant inherited Vision regressions | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Full test suite and clean build | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Independent exact-delta/static review | REQUIRED / PENDING |
| Fresh Simulation | NOT REQUIRED: Simulation selects VisionIOSim and does not exercise real adapter endpoint ownership. |
| Glass | NOT APPLICABLE |
| Driver Station | NOT APPLICABLE |
| Real hardware | NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE: endpoint remains exactly `"limelight"`; only configuration authority changes. |

These are approved CFG-H01 dispositions only. They do not erase P3-H01 evidence
or silently resolve separate earlier P3-H01 applicability/closure requirements.

### Lifecycle, publication and remaining gates

The existing AGENTS Sections 8/14 exception is amended for CFG-H01;
P3-H01 is not reopened and no new curriculum concept is added.
Separate implementation authorization, future verification, documentation
finalization, final architecture/independent closure review, explicit
Architect/User re-freeze, independent freeze review, User-owned repaired
primary/metadata commits and push, and external publication verification
remain PENDING. The transition repair appendix is IN PROGRESS / NOT FINAL.

Original publication remains historical fact:
primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`,
metadata `3667290180fe1a9fd96265383e7412c142c18129`,
gate `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION`.
No old PASS becomes fresh CFG-H01 evidence; no repaired publication is claimed.

Phase 2 remains
`PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
Phase 4: NOT STARTED / FORBIDDEN. Audit resumption requires its separate
reviewed-removal/authorization gate. No M00_L17.
STOP/HOLD on any CFG-H01 third production file, second test file, new
production/test file, changed endpoint/threshold/behavior, architecture
expansion, other lesson edit, or unauthorized drift.

## Current P3-H01 exceptional-repair governance registration — 2026-09-28

- Status: IN_PROGRESS.
- Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Reopen reason: P3-H01 post-freeze architecture/configuration-authority defect.
- Repository Active Lesson Count: 1.
- Active M00 Lesson Count: 1.
- Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Architect and User authorization: APPROVED for governance/documentation and
  exceptional bounded repair workflow only, by the supplied registration request.
- Accepted design: `PASS_P3_H01_M00_L16_EXCEPTIONAL_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.
- Implementation Authorization: PENDING / NOT AUTHORIZED.
- P3-H01 Implementation: NOT STARTED.
- Fresh repair baseline, static review, focused tests, full inherited suite,
  clean build, Simulation, Driver Station, closure, re-freeze, and repaired
  publication: PENDING; no fresh execution PASS is claimed.
- Glass and real-hardware applicability decisions: PENDING.
- Governing repair ADR: [P3-H01 configuration-authority repair](../../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md).

This is the narrow exception under AGENTS Sections 8 and 14. REOPENED is a
provenance qualifier for IN_PROGRESS, not a new generic status. Editability
does not authorize Java/test changes. All other lessons remain read-only.
M00's original sixteen-lesson curriculum is closed; this repair adds no concept
or lesson and does not authorize M00_L17.

### Original published snapshot — preserved historical evidence

- Original lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.
- Original primary snapshot: `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- Original publication metadata: `3667290180fe1a9fd96265383e7412c142c18129`.
- Original final gate: `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION`.
- Original evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

These identities and the accepted external publication verification are historical
truth. Earlier pending-publication passages below describe the pre-Commit-2
record; they do not revoke the accepted original publication. Original steps,
closure HOLD/repair/rereview, freeze evidence, and publication history remain
preserved. No original commit is rewritten and no old PASS becomes fresh repair
evidence. The repair working state has no repaired publication identity.

### Registered future boundary and audit state

The future production boundary is exactly L16 `src/main/java/frc/robot/Constants.java`
and `src/main/java/frc/robot/RobotContainer.java`. Only one new test is proposed:
`src/test/java/frc/robot/VisionConfigurationAuthorityTest.java`; zero existing
test edits. No Java/test implementation is authorized by this registration.

Constants.VisionConstants will own primitive double defaults:
`kLowUncertaintyMaxDistanceMeters = 1.0`,
`kMediumUncertaintyMaxDistanceMeters = 2.0`,
`kMaximumAcceptedDistanceMeters = 3.0`, and
`kMaximumFreshAgeSeconds = 0.250`. RobotContainer will continue constructing
and injecting the existing Policy. Inclusive comparisons and runtime semantics
remain unchanged. VisionSubsystem and both evaluator production files remain
protected. V00_L09 and M00_L01-L15 retain their historical P3-H01 finding and
unchanged files. Estimator/fusion, telemetry, PathPlanner/events, mechanisms,
controllers, CAN/hardware configuration, dependencies, and assets are excluded.

Phase 2 remains `PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
Phase 4 is NOT STARTED / FORBIDDEN. Governance registration cannot resume the
audit: a reviewed implementation/static-removal gate and separate authorization
are required. D2A, H01, historical R1, A01_L07, and historical-byte limits remain
qualified as previously accepted.

STOP/HOLD if a third production file, an existing test edit, numerical behavior
change, architecture expansion, or another lesson is required. Return for
governance review; do not silently expand scope. Required gates remain pending
until fresh evidence is accepted.

## Historical original transition record — through 2026-09-27

The original introductory fields and Steps 1-27 below are preserved verbatim as
the historical transition/pre-Commit-2 record. The accepted original external
publication identities are recorded above. The separately dated appendix at
the end plans the repair workflow; it records no repair implementation result.

Status: FINAL / PASS THROUGH PUBLICATION METADATA RECONCILIATION / READY FOR USER METADATA COMMIT 2  
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
Review passed, and the User created Primary Frozen Snapshot Commit 1 at
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata
reconciliation is complete. Metadata Commit 2, publication push, and final
external publication verification remain pending; M00_L16 is NOT PUBLISHED.
Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

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
- **Action:** Independent reviewer examined the reconciled frozen snapshot.
- **Files Changed:** None by review.
- **Verification:** `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW`; verdict `M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
- **Expected Result:** Freeze review PASS; M00_L16 remains COMPLETE / FROZEN / READ-ONLY.

## Step 23 — Record User-owned primary frozen snapshot commit

- **Objective:** Record Commit 1 of the two-commit Historical Snapshot model.
- **Why:** The frozen lesson snapshot precedes the separate metadata commit.
- **Action:** Accept the User-provided canonical primary snapshot hash `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- **Files Changed:** None in this metadata reconciliation step.
- **Verification:** User-created Primary Frozen Snapshot Commit 1; canonical SHA `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- **Expected Result:** Local primary snapshot recorded without claiming metadata commit, push, or remote publication.

## Step 24 — Reconcile publication metadata

- **Objective:** Prepare the accurate current record for User-owned Metadata Publication Commit 2.
- **Why:** The successful freeze review and primary snapshot require publication metadata before Commit 2.
- **Action:** Reconcile only the current lifecycle and publication fields, preserving historical HOLD, repair, rereview, technical evidence, and frozen source.
- **Files Changed:** `AGENTS.md`; repository `README.md`; M00 roadmap ADR; L16 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide. No source, test, deploy, build, or configuration file changed.
- **Verification:** `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`; governance validation PASS with 12 VERIFIED mirrors, 12 matching PDF hashes, 12 matching Markdown hashes, and zero deterministic findings.
- **Expected Result:** Metadata is prepared for Commit 2; M00_L16 remains COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED.

## Step 25 — User-owned metadata commit pending

- **Objective:** Preserve Metadata Publication Commit 2 as a separate User-owned gate.
- **Why:** Commit 2 follows the completed primary snapshot and metadata reconciliation.
- **Action:** PENDING; await the User-owned metadata-only commit.
- **Files Changed:** None by this reconciliation step.
- **Verification:** Metadata Commit 2 and its SHA PENDING.
- **Expected Result:** The two-commit Historical Snapshot model remains intact; no third verification-only commit.

## Step 26 — Publication push pending

- **Objective:** Preserve push as a separate User-owned gate.
- **Why:** Remote publication follows Metadata Commit 2.
- **Action:** PENDING; await the User-owned publication push.
- **Files Changed:** None by this reconciliation step.
- **Verification:** Push PENDING; no remote identity claimed.
- **Expected Result:** M00_L16 remains NOT PUBLISHED until accepted publication evidence.

## Step 27 — Final external publication verification pending

- **Objective:** Preserve final publication verification as its own external gate.
- **Why:** Verification follows Commit 2 and push.
- **Action:** PENDING; await external evidence.
- **Files Changed:** None by this reconciliation step.
- **Verification:** Final External Publication Verification PENDING.
- **Expected Result:** No PUBLISHED / VERIFIED claim before accepted external evidence; no third verification-only commit.

## P3-H01 exceptional-repair appendix — 2026-09-28

Status: IN PROGRESS / NOT FINAL. The following steps describe the future sequence
only. All step statuses, verification and evidence remain PENDING. Governance
registration is recorded at the top; no repair implementation or execution result
is claimed here. Paths in step scopes refer to the existing M00_L16 project.
Original Steps 1-27 above remain unchanged.

### Repair Step R1 — Establish fresh baseline and provenance

- **Objective:** Establish fresh baseline and provenance.
- **Why:** Separate inherited published code from future repair.
- **Action:** User will verify Java 17, original L16 source/provenance, inherited focused/full suite and clean build before implementation.
- **Files Changed / Files and Scope:** Unchanged original L16; baseline evidence only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Fresh User evidence and baseline/scope review required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Verified unchanged baseline; repair still not implemented.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R2 — Obtain exact implementation authorization

- **Objective:** Obtain exact implementation authorization.
- **Why:** Release only the accepted future file boundary.
- **Action:** Architect/User will confirm the design and separately authorize exactly two production files and one new test.
- **Files Changed / Files and Scope:** Constants.java, RobotContainer.java and proposed VisionConfigurationAuthorityTest.java only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Explicit exact-file authorization required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Bounded implementation may start; no wider editability implied.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R3 — Place defaults in Constants

- **Objective:** Place defaults in Constants.
- **Why:** Restore default configuration authority.
- **Action:** Later authorized engineer will add the four locked public static final doubles to VisionConstants and narrowly clarify JavaDoc.
- **Files Changed / Files and Scope:** src/main/java/frc/robot/Constants.java only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Independent source/value and changed-file review required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Configuration ownership restored without changed values.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R4 — Inject named defaults in RobotContainer

- **Objective:** Inject named defaults in RobotContainer.
- **Why:** Preserve valid composition using authoritative defaults.
- **Action:** Later authorized engineer will replace the three Policy arguments and one freshness argument with named constants.
- **Files Changed / Files and Scope:** src/main/java/frc/robot/RobotContainer.java only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Constructor-origin/injected-configuration review required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Same Policy, constructor, timestamp supplier and runtime semantics.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R5 — Add one configuration authority guard

- **Objective:** Add one configuration authority guard.
- **Why:** Prove origin, actual wiring and unchanged boundaries.
- **Action:** Later authorized engineer will add independent default, constructor-reference, actual fixture/injection, inclusive-boundary and evaluator-dependency proof.
- **Files Changed / Files and Scope:** src/test/java/frc/robot/VisionConfigurationAuthorityTest.java only; zero existing test edits. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Focused execution later required; no current result.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** One bounded new guard protects the accepted repair.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R6 — Review static removal and file isolation

- **Objective:** Review static removal and file isolation.
- **Why:** Confirm P3-H01 is removed at the selected target.
- **Action:** Independent reviewer will inspect exact production/test delta and historical preservation.
- **Files Changed / Files and Scope:** Read-only two-file repair/new-test review and protected-source comparisons. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Static-removal and scope PASS required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Correction reviewed; verification/closure/publication not implied.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R7 — Run fresh focused and full verification

- **Objective:** Run fresh focused and full verification.
- **Why:** Protect inherited architecture and behavior.
- **Action:** User will run focused/guard tests, inherited/full suite and clean Gradle build; retain all existing tests.
- **Files Changed / Files and Scope:** User execution in repaired L16 only; no additional authored edits. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. New results required; original PASS cannot satisfy repair gates.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Fresh focused/full/build evidence accepted.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R8 — Verify bounded Simulation and Driver Station

- **Objective:** Verify bounded Simulation and Driver Station.
- **Why:** Confirm integrated runtime composition after repair.
- **Action:** User will verify startup, valid-frame qualification/fusion, unavailable/stale handling, event/event-free controls, Disabled cleanup, no observed fatal exception and normal exit.
- **Files Changed / Files and Scope:** Existing Simulation fixtures and runtime contracts only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Fresh Simulation and Driver Station evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Bounded software/runtime verification; no physical-performance claim.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R9 — Resolve Glass and real-hardware applicability

- **Objective:** Resolve Glass and real-hardware applicability.
- **Why:** Avoid silent waiver or reuse of historical evidence.
- **Action:** Architect/User will explicitly decide applicability and User will provide any required bounded evidence.
- **Files Changed / Files and Scope:** Existing observation/hardware contracts; no calibration, tuning or configuration change. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Explicit decisions and applicable fresh evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Applicability resolved with scoped results or approved disposition.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R10 — Finalize repair documentation and closure

- **Objective:** Finalize repair documentation and closure.
- **Why:** Record supported evidence and preserved provenance.
- **Action:** A separately authorized documentation action will reconcile repair records; independent architecture/closure review will follow.
- **Files Changed / Files and Scope:** This ADR and only later explicitly authorized existing governance/lesson records. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Documentation and final architecture/closure PASS required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Repair appendix final only after applicable gates complete.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R11 — Authorize re-freeze

- **Objective:** Authorize re-freeze.
- **Why:** Make the verified repaired lesson immutable again.
- **Action:** Architect/User will explicitly approve COMPLETE / FROZEN / READ-ONLY; independent freeze review will follow.
- **Files Changed / Files and Scope:** Authorized lifecycle records only; source remains fixed. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Explicit re-freeze and independent freeze-review evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Active count 0; repaired publication still pending.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R12 — Publish a new repaired primary snapshot

- **Objective:** Publish a new repaired primary snapshot.
- **Why:** Establish an identity separate from the original.
- **Action:** User alone will create the new repaired frozen-snapshot commit after accepted re-freeze.
- **Files Changed / Files and Scope:** New repaired snapshot; original ff4de5ab... and 36672901... preserved. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. User-supplied new primary identity required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** New primary snapshot recorded without metadata/push claims.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R13 — Reconcile and publish repair metadata

- **Objective:** Reconcile and publish repair metadata.
- **Why:** Link original and repaired identities without rewriting history.
- **Action:** Separately authorized metadata action will record new-primary provenance; User will create the separate metadata commit and push.
- **Files Changed / Files and Scope:** Only explicitly authorized repair metadata; no original commit amendment. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. User new metadata identity and successful push evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Two-commit repaired publication prepared for external verification.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R14 — Verify repaired publication externally

- **Objective:** Verify repaired publication externally.
- **Why:** Accept the new publication and its bounded supersession role.
- **Action:** Independent external review will check new identities, scope and push evidence.
- **Files Changed / Files and Scope:** Read-only publication evidence; no third verification-only commit. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. External final repaired-publication verification required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Repaired snapshot PUBLISHED / VERIFIED for approved correction role.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R15 — Seek separate Phase-3 resumption authorization

- **Objective:** Seek separate Phase-3 resumption authorization.
- **Why:** Keep audit resumption distinct from registration and publication.
- **Action:** After reviewed static removal, seek a separate audit instruction; report actual verification/freeze/publication state.
- **Files Changed / Files and Scope:** Remaining read-only Architecture Consistency Matrix only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Reviewed static-removal gate and separate authorization required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Phase 3 may resume only when separately authorized; Phase 4 stays forbidden.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.


## CFG-H01 exceptional-repair appendix — 2026-09-29

Status: IN PROGRESS / NOT FINAL. This appendix follows all pre-existing material.
Original Steps 1-27 and the complete 2026-09-28 P3-H01 appendix remain byte-for-byte
preserved as historical records. Their initially pending P3-H01 gates are
superseded for current status by this dated record and the current registration
above; P3-H01 is IMPLEMENTED / FRESHLY VERIFIED / NOT REOPENED.

The User-supplied P3-H01 focused/full/clean-build/fresh-Simulation PASS and bounded
runtime results remain preserved. Localization initialization precedes expected
accepted Vision fusion. Successful LEARNING_EVENT evidence is
`/Intake/RequestedState` and `/Feeder/RequestedState`, not successful
AutonomousEvent lifecycle telemetry. No post-CFG-H01 execution is claimed.

All future code paths refer only to the existing M00_L16 project.
The current implementation gate remains PENDING / NOT AUTHORIZED.

### CFG-H01 Step C1 — Record the inherited configuration-authority finding

- **Objective:** Record the inherited configuration-authority finding.
- **Why:** Identify device configuration ownership without inventing failure.
- **Action:** Record ONE CFG-H01 MISPLACED_CONFIGURATION finding, earliest surviving V00_L08 and propagation through V00_L09/M00_L01-M00_L16.
- **Files Changed / Files and Scope:** None; read-only audit evidence.
- **Verification:** Established audit/source evidence; no endpoint/runtime/hardware safety failure.
- **Expected Result:** One inherited finding; historical source preserved.
- **Status:** COMPLETE.

### CFG-H01 Step C2 — Register the reviewed bounded design

- **Objective:** Register the reviewed bounded design.
- **Why:** Keep correction within the independently reviewed ownership boundary.
- **Action:** Preserve the accepted design token and exact two-existing-production/one-existing-test future scope.
- **Files Changed / Files and Scope:** None by design review; design registration recorded in C3.
- **Verification:** PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION; reviewed design.
- **Expected Result:** Design approved; implementation still unauthorized.
- **Status:** COMPLETE.

### CFG-H01 Step C3 — Register governance and stage status

- **Objective:** Register governance and stage status.
- **Why:** Extend the existing exception explicitly while preserving Stage 1 evidence.
- **Action:** Add the dated CFG-H01 record to the exact nine existing documents listed in ADR Section 16.7; preserve all original bytes and append this appendix after existing material.
- **Files Changed / Files and Scope:** Exactly the nine existing governance/lesson documents; zero source/test edits; zero new ADRs.
- **Verification:** Current governance integrity and documentation/scope checks; independent registration review remains pending.
- **Expected Result:** GOVERNANCE REGISTERED / DESIGN APPROVED / IMPLEMENTATION AUTHORIZATION PENDING.
- **Status:** COMPLETE.

### CFG-H01 Step C4 — Obtain separate implementation authorization

- **Objective:** Obtain separate implementation authorization.
- **Why:** Documentation registration does not authorize source changes.
- **Action:** After independent registration review, obtain exact-file Architect/User approval for Constants.java, io/vision/VisionIOLimelight.java and existing VisionConfigurationAuthorityTest.java.
- **Files Changed / Files and Scope:** None by this gate; future implementation scope only.
- **Verification:** PENDING; explicit exact-file authorization required.
- **Expected Result:** Bounded source/test work may begin only after approval.
- **Status:** PENDING.

### CFG-H01 Step C5 — Relocate endpoint configuration authority

- **Objective:** Relocate endpoint configuration authority.
- **Why:** Restore Constants ownership with the same endpoint behavior.
- **Action:** Later authorized engineer adds the String field first in VisionConstants, imports/consumes it in the default adapter constructor and removes the private endpoint default; extends only the existing guard as designed.
- **Files Changed / Files and Scope:** Future only: the exact two existing production files and one existing test; zero new files.
- **Verification:** PENDING; no implementation performed by this registration.
- **Expected Result:** Endpoint remains limelight; constructors/protocol/session and completed P3-H01 guards preserved.
- **Status:** PENDING.

### CFG-H01 Step C6 — Review exact delta and static removal

- **Objective:** Review exact delta and static removal.
- **Why:** Prove source origin and preserve protected boundaries.
- **Action:** Independent reviewer checks executable constant consumption/removal, independent declaration/endpoint assertions and unchanged RobotContainer, IO tests and historical files.
- **Files Changed / Files and Scope:** None by read-only review.
- **Verification:** PENDING; independent exact-delta/static review required.
- **Expected Result:** Bounded CFG-H01 source correction accepted without claiming runtime or closure.
- **Status:** PENDING.

### CFG-H01 Step C7 — Obtain fresh automated verification

- **Objective:** Obtain fresh automated verification.
- **Why:** Protect real adapter behavior and inherited project contracts.
- **Action:** User runs focused authority guard, unchanged Limelight tests, relevant inherited Vision regressions, full suite and clean build.
- **Files Changed / Files and Scope:** No authored edits; User-owned verification only.
- **Verification:** PENDING; all listed post-CFG-H01 gates required; no old PASS reused.
- **Expected Result:** Fresh automated evidence accepted. Simulation NOT REQUIRED; Glass/DS NOT APPLICABLE; hardware NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE under approved scope.
- **Status:** PENDING.

### CFG-H01 Step C8 — Complete documentation and independent closure review

- **Objective:** Complete documentation and independent closure review.
- **Why:** Close only with supported evidence and resolved applicable gates.
- **Action:** Obtain separately scoped documentation authorization, finalize repair appendices, and complete final architecture/independent closure review; explicitly resolve any separate earlier outstanding applicability gate.
- **Files Changed / Files and Scope:** Only later explicitly authorized existing documentation; no current finalization claim.
- **Verification:** PENDING; required evidence, final documentation and independent closure PASS.
- **Expected Result:** Repair ready to seek explicit re-freeze; still IN_PROGRESS until approved.
- **Status:** PENDING.

### CFG-H01 Step C9 — Obtain explicit re-freeze and freeze review

- **Objective:** Obtain explicit re-freeze and freeze review.
- **Why:** Return the repaired lesson to immutable status only by approval.
- **Action:** Architect/User explicitly authorizes re-freeze; independent freeze review confirms supported repaired state.
- **Files Changed / Files and Scope:** Later explicitly authorized lifecycle records only.
- **Verification:** PENDING; explicit approval and independent freeze review required.
- **Expected Result:** COMPLETE / FROZEN / READ-ONLY and active count 0 only after approval; repaired publication pending.
- **Status:** PENDING.

### CFG-H01 Step C10 — Publish and externally verify repaired identities

- **Objective:** Publish and externally verify repaired identities.
- **Why:** Preserve original publication and establish distinct repaired provenance.
- **Action:** User creates new repaired primary and separate metadata commits, pushes, and obtains independent external publication verification under separately authorized workflow.
- **Files Changed / Files and Scope:** Later scoped repaired snapshot/metadata only; no original history rewrite.
- **Verification:** PENDING; new identities, push and external final verification required.
- **Expected Result:** Repaired PUBLISHED / VERIFIED only after accepted evidence; no third verification-only commit; Phase-3 resumption still separately authorized and Phase 4 forbidden.
- **Status:** PENDING.

STOP/HOLD on unauthorized scope, altered endpoint/behavior, failed required
verification, or missing applicable closure evidence. No automatic rollback.
Original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11` and metadata
`3667290180fe1a9fd96265383e7412c142c18129` remain historical truth.
No M00_L17, audit resumption, repaired publication, source edit or User execution
is authorized by this documentation registration.

## P3-H01 + CFG-H01 post-implementation evidence reconciliation — 2026-09-29

Status: DOCUMENTATION / EVIDENCE RECONCILED / READY FOR FINAL CLOSURE REVIEW.
M00_L16 remains REOPENED / IN_PROGRESS / EDITABLE; active lesson count 1.
This appended record controls current repair stage status. All pre-existing
bytes, including original Steps 1-27, the P3-H01 repair appendix and CFG-H01
C1-C10 registration/planning appendix, remain preserved. Their then-pending
implementation/verification entries are historical planning, not current status.
Final architecture/closure review remains NEXT / PENDING.

### Accepted repair chronology

| Sequence | Stage / accepted current record |
| --- | --- |
| 1 | P3-H01 discovered: operational Vision defaults were misplaced in RobotContainer. |
| 2 | P3-H01 governed: exceptional reopen/design and exact implementation boundary registered. |
| 3 | P3-H01 implemented: four defaults moved to Constants and injected by RobotContainer; bounded authority guard added. |
| 4 | P3-H01 freshly verified and independently reviewed: supplied focused/full/clean-build and bounded Simulation PASS preserved. |
| 5 | Configuration-authority audit discovered CFG-H01: real-camera table identity privately owned by the adapter. |
| 6 | CFG-H01 bounded design reviewed: PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION. |
| 7 | CFG-H01 amendment registered in the nine existing documents; separate exact-file implementation authorization subsequently approved. |
| 8 | CFG-H01 implemented: exactly two existing production files and one existing test; endpoint/behavior preserved. |
| 9 | User automated verification PASS: authority guard, unchanged adapter tests, Vision regressions, full suite and clean build. |
| 10 | Independent post-implementation review PASS: PASS_CFG_H01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION. |
| 11 | Authorized documentation/evidence reconciliation completed on 2026-09-29. |
| 12 | INDEPENDENT FINAL ARCHITECTURE / CLOSURE REVIEW remains NEXT / PENDING; no closure, re-freeze, publication or Phase-3 resumption. |

### Evidence Step E1 — Preserve the completed P3-H01 correction

- **Objective:** Record completed P3-H01 ownership and fresh verification.
- **Why:** Preserve Stage 1 evidence without reopening or repeating it.
- **Action:** Record Constants defaults 1.0/2.0/3.0/0.250 and RobotContainer injection.
- **Files Changed:** Prior P3-H01 stage: Constants.java, RobotContainer.java and then-new VisionConfigurationAuthorityTest.java; zero source/test edits in this reconciliation.
- **Verification:** Accepted User focused test, full suite, clean build and fresh bounded Simulation PASS; independent review preserved.
- **Evidence:** Clean startup, Disabled baseline, gyro health, autonomous preparation, heading reference and Pose / EstimatedPose initialization; UNAVAILABLE baseline; VALID_FRAME_A qualification/accepted fusion; unchanged-frame duplicate/stale hold; VALID_FRAME_B fresh recovery; return UNAVAILABLE; ONE_METER_WITH_EVENT preparation/event-enabled autonomous execution; LEARNING_EVENT Intake/Feeder semantics; cleanup; autonomous completion; consumed-readiness fail-closed; normal exit.
- **Expected Result:** P3-H01 IMPLEMENTED / VERIFIED / PRESERVED / NOT REOPENED.
- **Status:** RECORDED / PASS EVIDENCE PRESERVED.

Localization/heading initialization precedes expected accepted Vision estimator
fusion. Successful LEARNING_EVENT behavior is observed through
`/Intake/RequestedState` and `/Feeder/RequestedState`, not successful
AutonomousEvent lifecycle telemetry. No physical transfer/performance or
post-CFG-H01 Simulation result is inferred.

### Evidence Step E2 — Record bounded CFG-H01 implementation

- **Objective:** Record the completed configuration-authority relocation.
- **Why:** Remove the inherited misplaced table identity without changing behavior.
- **Action:** Record `public static final String kLimelightTableName = "limelight";` in existing Constants.VisionConstants, existing default adapter consumption, private default removal and bounded existing authority-test extension.
- **Files Changed:** Prior CFG-H01 implementation only: Constants.java, io/vision/VisionIOLimelight.java and existing VisionConfigurationAuthorityTest.java; zero new source/test files.
- **Verification:** Independent declaration/value, executable AST origin/removal and actual default `/limelight/json` endpoint proof; prior P3-H01 tests/helpers preserved.
- **Expected Result:** Same limelight endpoint, both constructors, adapter behavior/protocol and protected source.
- **Status:** IMPLEMENTED / VERIFIED / REMOVED.

### Evidence Step E3 — Record fresh User automated verification

- **Objective:** Reconcile all five required post-CFG-H01 automated gates.
- **Why:** Use accepted fresh execution evidence for this repair stage.
- **Action:** Record User PASS for VisionConfigurationAuthorityTest, unchanged VisionIOLimelightTest, Vision regression suite, full test suite and clean build.
- **Files Changed:** None by verification; this turn only records supplied evidence.
- **Verification:** User supplied the authorized fail-stop block completion and the final output below.
- **Expected Result:** USER AUTOMATED VERIFICATION PASS, with no invented numeric test count or exit code.
- **Status:** PASS / USER EVIDENCE ACCEPTED.

```text
BUILD SUCCESSFUL in 32s
7 actionable tasks: 7 executed
CFG-H01 AUTOMATED USER VERIFICATION COMPLETE
```

Seven actionable tasks are not seven tests. Fresh CFG-H01 Simulation:
NOT REQUIRED (VisionIOSim selection). Glass / Driver Station: NOT APPLICABLE.
Real hardware: NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE (same endpoint behavior).
These approved CFG-H01 decisions remain separate from P3-H01 Simulation evidence
and any separate earlier applicability/closure requirement.

### Evidence Step E4 — Record independent exact-delta review

- **Objective:** Record independent acceptance of removal and scope.
- **Why:** Confirm implementation origin, constructor/behavior preservation and isolation.
- **Action:** Record the accepted Sol post-implementation review.
- **Files Changed:** None by read-only review.
- **Verification:** `PASS_CFG_H01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`; no implementation defect or scope violation found.
- **Expected Result:** REMOVED / VERIFIED / READY_FOR_DOCUMENTATION_RECONCILIATION.
- **Status:** PASS / INDEPENDENT REVIEW ACCEPTED.

### Evidence Step E5 — Reconcile authorized documentation

- **Objective:** Make current lifecycle/evidence records agree with completed repairs.
- **Why:** Remove stale current-state pending claims while preserving chronology.
- **Action:** Replace the current registration-stage summaries and status/plan/checklist; append this evidence record and ADR Section 17.
- **Files Changed:** Only existing AGENTS.md, root README.md, existing P3-H01 repair ADR, M00 roadmap ADR, L16 README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md and this transition guide.
- **Verification:** Governance integrity and bounded documentation/static preservation checks; zero Java/test changes, new ADRs, new lessons, Git or project execution.
- **Expected Result:** DOCUMENTATION / EVIDENCE RECONCILED; original transition/repair appendix bytes preserved.
- **Status:** RECONCILED / READY FOR FINAL CLOSURE REVIEW.

### Evidence Step E6 — Await independent final architecture/closure review

- **Objective:** Submit reconciled evidence to the remaining closure gate.
- **Why:** Documentation reconciliation does not grant closure or re-freeze.
- **Action:** Next independent final architecture/closure review must assess the reconciled repair and explicitly dispose of any separate outstanding earlier applicability/closure gate.
- **Files Changed:** None by this pending gate; no review executed or PASS claimed here.
- **Verification:** PENDING; final architecture/closure acceptance required.
- **Expected Result:** Seek explicit Architect/User re-freeze only after accepted closure; freeze review and User-owned repaired publication remain later gates.
- **Status:** NEXT / PENDING.

Original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
`3667290180fe1a9fd96265383e7412c142c18129` and
`PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION` remain historical truth.
No repaired publication is claimed. Phase 3 remains HOLD; Phase 4 remains
NOT STARTED / FORBIDDEN. No M00_L17. Unrelated A01_L06 path asset remains untouched.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## Final exceptional-repair closure and authorized re-freeze — 2026-09-29

- Independent final closure review: PASS —
  `PASS_M00_L16_EXCEPTIONAL_REPAIR_CLOSED_READY_FOR_REFREEZE_AUTHORIZATION`.
- Technical, verification, documentation and architecture closure: CLOSED.
- P3-H01: IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED / PRESERVED / CLOSED.
- CFG-H01: IMPLEMENTED / VERIFIED / REMOVED / INDEPENDENTLY REVIEWED / CLOSED.
- Remaining repair requirements: NONE.
- Explicit Architect/User re-freeze authorization: APPROVED / CONSUMED by this
  documentation transition.
- M00_L16: COMPLETE / FROZEN / READ-ONLY; exceptional repair CLOSED.
- Repository Active Lesson Count: 0; Active M00 Lesson Count: 0;
  Current Active M00 Lesson: NONE.
- Final repair Transition Guide: accepted through the independent closure review.
  All preceding steps and repair appendices are preserved historical chronology;
  their earlier current/pending/editable wording describes the recorded stage.
- Repaired publication: PENDING; no repaired Git snapshot or identity exists.
  Original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
  `3667290180fe1a9fd96265383e7412c142c18129` and original gate
  `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION` remain historical truth.
- Exact next gate: INDEPENDENT RE-FREEZE / FROZEN-CANDIDATE REVIEW.
- Phase 3: HOLD; separate review, User snapshot/publication workflow, push,
  external final publication verification and separate audit authorization remain
  prerequisites to resumption consideration. Phase 4: NOT STARTED / FORBIDDEN.
- Accepted repair evidence/applicability and deferred physical-evidence limits
  remain preserved; no new hardware/commissioning claim or M00_L17.
- Files changed in this step: the same nine existing governance/lesson documents.
  Verification: governance integrity and static documentation/protected-file checks.
  Expected result: closed, refrozen candidate ready for independent freeze review.
- No Git or project execution. Source/tests, historical lessons and unrelated
  A01_L06 path state remain unchanged. No new files/ADRs.
  github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.
