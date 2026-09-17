# M00_L02 to M00_L03 Step-by-Step Transition Guide

This guide records the controlled transition from published `M00_L02 - Mechanism Hardware Evidence Audit` to frozen `M00_L03 - Intake Foundation`. Steps 1-36 are complete through independent reconciliation review, Architect freeze authorization, lifecycle freeze recording, primary publication, and publication-metadata reconciliation. Step 37 remains pending for independent metadata review, User-owned metadata publication, final remote verification, and final publication completion. M00_L03 remains `COMPLETE / FROZEN / READ-ONLY`.

Current state: freeze state `FROZEN`; active lesson count `0`; implementation,
focused tests, full regression, final closure build, bounded Simulation, student
documentation, final closure review, lifecycle reconciliation, independent
reconciliation review, and Architect freeze authorization are PASS or VERIFIED
as applicable. Primary Git publication is complete at
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, primary remote alignment is
`PASS`, and publication metadata reconciliation is complete. Metadata Git
publication and final publication verification remain pending. M00_L04 remains
`NOT ACTIVE / NOT CREATED`.

## Step 1 - Confirm final M00_L02 publication

- **Objective:** Establish the authoritative predecessor snapshot.
- **Why:** A new lesson must inherit from the final published predecessor.
- **Action:** Confirmed M00_L02 as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` at `65a92a4a5806fd5134e0114e851c4e4cc093c58e`, with metadata reconciliation at `84010ff5022a33a946888fafedbbca0d67439e0c`.
- **Files Changed:** None.
- **Verification:** `PASS_M00_L02_FINAL_PUBLICATION_COMPLETE`.
- **Expected Result:** The M00_L03 preparation source is unambiguous. **COMPLETE / PASS**

## Step 2 - Confirm roadmap identity

- **Objective:** Confirm the authorized lesson name, order, and concept.
- **Why:** Lessons may not be renamed, reordered, inserted, or skipped without governance approval.
- **Action:** Confirmed M00_L03 as `Intake Foundation`, the third lesson in the 16-lesson M00 roadmap.
- **Files Changed:** None.
- **Verification:** Roadmap and governance review.
- **Expected Result:** M00_L03 identity and sequence are authoritative. **COMPLETE / PASS**

## Step 3 - Prepare the candidate by user copy

- **Objective:** Create the candidate through the required predecessor-copy workflow.
- **Why:** Lessons must inherit rather than be recreated from scratch.
- **Action:** The User copied the published M00_L02 snapshot to exactly one `M00_L03_IntakeFoundation` candidate.
- **Files Changed:** Candidate tree prepared by the User before activation.
- **Verification:** Candidate inventory review.
- **Expected Result:** One inherited M00_L03 candidate exists. **COMPLETE / PASS**

## Step 4 - Remove generated artifacts

- **Objective:** Prevent build outputs and caches from becoming inherited lesson content.
- **Why:** Generated artifacts are not source-of-truth lesson files.
- **Action:** The User completed the authorized generated-artifact cleanup.
- **Files Changed:** Generated artifacts only, before this controlled activation.
- **Verification:** Preparation evidence review.
- **Expected Result:** The candidate is ready for a clean baseline build. **COMPLETE / PASS**

## Step 5 - Record the erroneous lesson-local AGENTS check

- **Objective:** Preserve the preparation-script diagnostic accurately.
- **Why:** A tool defect must not be misreported as a candidate defect.
- **Action:** Recorded that the script incorrectly expected a lesson-local `AGENTS.md`.
- **Files Changed:** Lifecycle documentation only.
- **Verification:** Script-result and repository-layout review.
- **Expected Result:** The failed check is traceable without changing repository rules. **COMPLETE / PASS**

## Step 6 - Classify the script defect

- **Objective:** Determine whether the diagnostic invalidated preparation.
- **Why:** Recopy is required only when candidate integrity is affected.
- **Action:** Classified the condition as `SCRIPT CHECK DEFECT ONLY`; repository-root `AGENTS.md` remains the governing file.
- **Files Changed:** Lifecycle documentation only.
- **Verification:** Governance structure and candidate review.
- **Expected Result:** The candidate is not falsely rejected. **COMPLETE / PASS**

## Step 7 - Retain the candidate without recopy

- **Objective:** Preserve the verified candidate.
- **Why:** Unnecessary recopy could discard valid evidence or introduce drift.
- **Action:** Retained the existing candidate because the script defect did not affect its contents.
- **Files Changed:** None.
- **Verification:** Candidate integrity audit.
- **Expected Result:** No recopy is required. **COMPLETE / PASS**

## Step 8 - Confirm Java 17

- **Objective:** Confirm the required Java toolchain used for baseline evidence.
- **Why:** WPILib lesson builds require the governed Java version.
- **Action:** The User verified Temurin Java `17.0.16`.
- **Files Changed:** None.
- **Verification:** User-supplied Java version evidence.
- **Expected Result:** The baseline environment uses Java 17. **COMPLETE / PASS**

## Step 9 - Run the inherited baseline build

- **Objective:** Verify the unimplemented candidate builds as inherited.
- **Why:** Baseline failure must stop activation.
- **Action:** The User ran the baseline build before this documentation-only activation.
- **Files Changed:** No governed source changes.
- **Verification:** User-supplied build result.
- **Expected Result:** The inherited candidate builds successfully. **COMPLETE / PASS**

## Step 10 - Confirm baseline exit code

- **Objective:** Record machine-level build completion evidence.
- **Why:** A success narrative without the exit result is incomplete.
- **Action:** Confirmed baseline build exit code `0`.
- **Files Changed:** Lifecycle documentation only.
- **Verification:** `PASS_M00_L03_BASELINE_BUILD_EXIT_CODE_CONFIRMED`.
- **Expected Result:** Baseline success is explicitly evidenced. **COMPLETE / PASS**

## Step 11 - Verify the post-copy candidate

- **Objective:** Confirm exact inherited content before activation edits.
- **Why:** Preparation must not silently alter predecessor content.
- **Action:** Accepted comparable comparison 607/607 and protected comparison 173/173, each with zero missing, extra, or different files.
- **Files Changed:** None.
- **Verification:** Accepted post-copy comparison evidence.
- **Expected Result:** Candidate inheritance is exact before activation documentation. **COMPLETE / PASS**

## Step 12 - Complete architecture and inheritance audit

- **Objective:** Verify Frozen Backbone, package, IO, Observation, and composition-root compliance.
- **Why:** Architecture must pass before design lock and activation.
- **Action:** Completed the independent read-only Architecture / Inheritance Audit.
- **Files Changed:** None.
- **Verification:** Accepted audit PASS gate.
- **Expected Result:** The candidate is eligible for Final Design Lock. **COMPLETE / PASS**

## Step 13 - Accept Final Design Lock

- **Objective:** Lock the single-concept implementation boundary.
- **Why:** Implementation scope must be explicit before any authorization.
- **Action:** Accepted `PASS_M00_L03_FINAL_DESIGN_LOCK` for an independently owned, vendor-neutral Intake mechanism foundation.
- **Files Changed:** Lifecycle planning documentation only.
- **Verification:** Architect Final Design Lock.
- **Expected Result:** Future implementation scope and exclusions are fixed. **COMPLETE / PASS**

## Step 14 - Record controlled activation

- **Objective:** Make M00_L03 the sole editable lesson.
- **Why:** Exactly one lesson may be `IN_PROGRESS / EDITABLE`.
- **Action:** Recorded M00_L03 as `IN_PROGRESS / EDITABLE`, freeze state `EDITABLE`, active lesson count `1`, with implementation still unauthorized.
- **Files Changed:** The seven authorized existing lifecycle files and this transition guide.
- **Verification:** Cross-file lifecycle consistency review.
- **Expected Result:** M00_L03 is active for separately authorized work; M00_L04 remains inactive. **COMPLETE / PASS**

## Step 15 - Obtain production and test implementation authorization

- **Objective:** Authorize the exact future production and test boundary.
- **Why:** Final Design Lock does not itself authorize implementation.
- **Action:** Accepted `PASS_M00_L03_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED` for the exact locked boundary.
- **Files Changed:** Authorization/lifecycle records only at this gate.
- **Verification:** Explicit Architect/User authorization record.
- **Expected Result:** Production and test work may proceed only within the locked boundary. **COMPLETE / PASS**

## Step 16 - Implement IntakeIO

- **Objective:** Define the vendor-neutral hardware abstraction and mutable one-cycle input snapshot.
- **Why:** Subsystems must not access vendor hardware directly.
- **Action:** Implemented `IntakeIO` and its owned input snapshot within the locked boundary.
- **Files Changed:** Authorized Intake IO production and focused-test files.
- **Verification:** Focused retest and full clean regression passed.
- **Expected Result:** A vendor-neutral Intake IO contract exists. **COMPLETE / PASS**

## Step 17 - Implement IntakeIONoop

- **Objective:** Provide the authorized non-vendor implementation.
- **Why:** The foundation must remain usable without selecting physical hardware.
- **Action:** Implemented deterministic `IntakeIONoop` safe no-op behavior.
- **Files Changed:** Authorized Noop production and focused-test files.
- **Verification:** Focused retest, full regression, and bounded Simulation passed.
- **Expected Result:** Composition can use Intake without a vendor adapter. **COMPLETE / PASS**

## Step 18 - Implement IntakeSubsystem

- **Objective:** Establish independent Intake behavior and state ownership.
- **Why:** The subsystem owns mechanism requests and safe stop.
- **Action:** Implemented `requestIntake()` and `stop()`; `stop()` records `STOPPED` and invokes `IntakeIO.stop()`.
- **Files Changed:** Authorized subsystem production and focused-test files.
- **Verification:** Focused retest and full clean regression passed; no physical-stop claim is made.
- **Expected Result:** Intake behavior is independently owned without commands or bindings. **COMPLETE / PASS**

## Step 19 - Implement IntakeObservation

- **Objective:** Provide the required immutable vendor-neutral read model.
- **Why:** Telemetry must consume Observations rather than mutable IO inputs.
- **Action:** Implemented immutable `IntakeObservation` with bounded availability, connectivity, and requested-state semantics.
- **Files Changed:** Authorized observation production and focused-test files.
- **Verification:** Immutability and semantic tests passed in the focused retest and full regression.
- **Expected Result:** Requested state is observable without hardware or control dependencies. **COMPLETE / PASS**

## Step 20 - Implement read-only telemetry

- **Objective:** Publish Intake state without controlling it.
- **Why:** Telemetry is a read-only consumer in the Frozen Backbone.
- **Action:** Implemented telemetry that consumes only immutable Intake observations.
- **Files Changed:** Authorized telemetry production and focused-test files.
- **Verification:** Dependency/publication tests and bounded Simulation telemetry presence passed.
- **Expected Result:** Intake information is published without behavior authority. **COMPLETE / PASS**

## Step 21 - Compose Intake in RobotContainer

- **Objective:** Select and inject the authorized Intake implementation.
- **Why:** `RobotContainer` is the composition root.
- **Action:** Created and wired the Noop Intake components without commands, bindings, or business logic.
- **Files Changed:** Authorized `RobotContainer.java` change and focused composition tests.
- **Verification:** Composition tests, bounded Simulation, and independent architecture review passed.
- **Expected Result:** Intake is composed while M00_L04 ownership remains excluded. **COMPLETE / PASS**

## Step 22 - Run focused tests

- **Objective:** Verify IO, Noop, subsystem, observation, telemetry, and composition contracts.
- **Why:** The new concept requires independently verifiable results.
- **Action:** Ran 16 focused tests. The initial result was 15 pass and 1 fail in `IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`; a `NullPointerException` (NPE) occurred because an optional callback was dereferenced at line 69. The failure was classified as a test defect. A bounded repair guarded the optional callback in `IntakeSubsystemTest.java` and removed no assertions; the focused set was rerun.
- **Files Changed:** Only authorized test files; the repair changed `IntakeSubsystemTest.java` only.
- **Verification:** Focused retest `BUILD SUCCESSFUL in 4s`, exit code `0`; full clean regression `BUILD SUCCESSFUL in 20s`, exit code `0`, 7 of 7 actionable tasks executed.
- **Expected Result:** The bounded Intake foundation behaves as designed, with the initial HOLD and repair history preserved. **COMPLETE / PASS**

## Step 23 - Run software Simulation

- **Objective:** Verify Fake/Noop software and composition behavior in Simulation.
- **Why:** Simulation is applicable to software contracts but not physical hardware facts.
- **Action:** The User ran the bounded Simulation workflow with the robot Disabled.
- **Files Changed:** None.
- **Verification:** Startup, Noop composition, subsystem integration, and Intake NetworkTables/telemetry presence passed.
- **Expected Result:** Bounded software architecture passed; wiring, CAN identity, direction, motion, current, load, force, and physical stopping remain unverified. **COMPLETE / PASS**

## Step 24 - Perform independent post-implementation review

- **Objective:** Confirm the implementation matches the locked architecture and scope.
- **Why:** Implementation success alone does not establish architecture compliance.
- **Action:** Conducted a read-only review of all authorized changes and evidence.
- **Files Changed:** None.
- **Verification:** Independent implementation review passed and was accepted.
- **Expected Result:** The implementation is eligible for documentation authorization. **COMPLETE / PASS**

## Step 25 - Obtain documentation implementation authorization

- **Objective:** Authorize student-facing documentation work.
- **Why:** Activation and production authorization do not authorize final learning guides.
- **Action:** Accepted `PASS_M00_L03_DOCUMENTATION_IMPLEMENTATION_AND_LIFECYCLE_RECONCILIATION_AUTHORIZED`.
- **Files Changed:** Authorization/lifecycle records only at this gate.
- **Verification:** Explicit documentation authorization record.
- **Expected Result:** Student guide and lifecycle-reconciliation work may proceed. **COMPLETE / PASS**

## Step 26 - Implement the English guide

- **Objective:** Teach the completed Intake foundation accurately.
- **Why:** English is normative and the lesson requires student documentation.
- **Action:** Created the English guide from verified implementation and evidence.
- **Files Changed:** `M00_L03_Intake_Foundation_Learning_Guide_EN.md`.
- **Verification:** 34 numbered sections, 15 review questions, 15 answers, and required architecture/evidence boundaries recorded.
- **Expected Result:** The normative guide matches the completed authorized scope. **COMPLETE / VERIFIED AFTER INDEPENDENT DOCUMENTATION REREVIEW**

## Step 27 - Implement the Vietnamese guide

- **Objective:** Provide an explanatory translation aligned with the English guide.
- **Why:** Students require semantically consistent bilingual documentation.
- **Action:** Created the Vietnamese guide without adding claims absent from English.
- **Files Changed:** `M00_L03_Intake_Foundation_Learning_Guide_VI.md`.
- **Verification:** Matching 34-section structure, 15 review questions, 15 answers, and semantic parity with the normative English guide.
- **Expected Result:** The Vietnamese guide faithfully explains the normative content. **COMPLETE / VERIFIED AFTER INDEPENDENT DOCUMENTATION REREVIEW**

## Step 28 - Reconcile this transition guide

- **Objective:** Replace future-tense planning with actual completed evidence where appropriate.
- **Why:** A final transition guide must reflect what was implemented and verified.
- **Action:** Reconciled implementation, initial test HOLD, bounded test repair, focused retest, full regression, bounded Simulation, independent implementation review, and student-documentation evidence. The initial independent documentation review then returned `HOLD` because Step 16 incorrectly described `IntakeIOInputs` as an `immutable input snapshot`. The authorized bounded one-line repair changed that wording to `mutable one-cycle input snapshot`. The repair passed at `PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_ONE_LINE_REPAIR_READY_FOR_INDEPENDENT_REREVIEW` and was accepted at `PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_DOCUMENTATION_REREVIEW`. Independent documentation rereview passed at `PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_CLOSURE_BUILD` and was accepted at `PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_CLOSURE_BUILD`.
- **Files Changed:** This transition guide.
- **Verification:** Step 16 now states the correct mutable-transport semantics; the English and Vietnamese student guides remain unchanged, structurally equivalent, and verified.
- **Expected Result:** Documentation history preserves the HOLD, exact terminology defect, bounded repair, and rereview PASS. **COMPLETE / PASS**

## Step 29 - Run the final User build

- **Objective:** Establish final compile, regression, and clean-build evidence.
- **Why:** Closure requires current User-owned verification.
- **Action:** The User ran the required final Java 17 closure build/regression after documentation rereview completed.
- **Files Changed:** None.
- **Verification:** `BUILD SUCCESSFUL in 42s`; 7 actionable tasks executed; exit code `0`; `PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION`; Architect acceptance `PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
- **Expected Result:** Final closure build is current and verified. **COMPLETE / PASS**

## Step 30 - Perform final closure review

- **Objective:** Review architecture, scope, documentation, and verification together.
- **Why:** Freeze requires a complete, internally consistent record.
- **Action:** Conducted the final read-only closure review across governance, architecture, implementation, tests, documentation, and accepted evidence.
- **Files Changed:** None.
- **Verification:** `PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`; Architect acceptance `PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
- **Expected Result:** No technical or substantive documentation defect remains; M00_L03 is eligible for bounded lifecycle reconciliation. **COMPLETE / PASS**

## Step 31 - Reconcile documentation and lifecycle metadata

- **Objective:** Align every governed record with the accepted final evidence.
- **Why:** Contradictory lifecycle records prevent closure.
- **Action:** Reconciled the eight explicitly authorized lifecycle/history records with the accepted documentation-review, final-build, and final-closure evidence. M00_L03 remains `IN_PROGRESS / EDITABLE`, freeze state `EDITABLE`, active lesson count `1`; M00_L04 remains inactive and uncreated.
- **Files Changed:** `AGENTS.md`, root `README.md`, the M00 ADR, M00_L03 `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this transition guide only.
- **Verification:** Post-edit read-only document consistency and protection review; independent reconciliation review remains the next gate.
- **Expected Result:** All M00_L03 lifecycle records agree without freezing or publishing the lesson. **COMPLETE / READY FOR INDEPENDENT RECONCILIATION REVIEW**

## Step 32 - Obtain freeze authorization

- **Objective:** Authorize the final lifecycle transition.
- **Why:** Passing reviews does not automatically freeze a lesson.
- **Action:** The independent reconciliation review passed at `PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`, and the Architect issued `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.
- **Files Changed:** Lifecycle documentation only.
- **Verification:** Independent reconciliation review PASS and explicit Architect freeze authorization.
- **Expected Result:** The `COMPLETE / FROZEN / READ-ONLY` transition is authorized. **COMPLETE / PASS**

## Step 33 - Record COMPLETE / FROZEN / READ-ONLY

- **Objective:** Close editing for M00_L03.
- **Why:** Completed lessons are immutable snapshots.
- **Action:** Recorded the lifecycle transition from `IN_PROGRESS / EDITABLE` to `COMPLETE / FROZEN / READ-ONLY` in the eight authorized lifecycle/history records.
- **Files Changed:** `AGENTS.md`, root `README.md`, the M00 ADR, M00_L03 `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this transition guide only.
- **Verification:** Post-freeze cross-file lifecycle consistency and protection review.
- **Expected Result:** M00_L03 is `COMPLETE / FROZEN / READ-ONLY` and no longer editable. **COMPLETE / PASS**

## Step 34 - Transition active lesson count to zero

- **Objective:** Record that no lesson remains editable after freeze.
- **Why:** A frozen lesson cannot count as active.
- **Action:** Changed the active lesson count from `1` to `0` while retaining M00_L04 as `NOT ACTIVE / NOT CREATED`.
- **Files Changed:** The eight explicitly authorized lifecycle/history records only.
- **Verification:** Repository-wide M00 active-state review and M00_L04 absence check.
- **Expected Result:** No active lesson exists until a later separately authorized controlled activation. **COMPLETE / PASS**

## Step 35 - Complete User-owned Git publication

- **Objective:** Publish the frozen lesson snapshot.
- **Why:** Git commit and push are exclusively User-owned.
- **Action:** The User completed the Architect-authorized explicit-allowlist staging, committed the frozen lesson snapshot at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, and pushed `84010ff..3d94dc6  main -> main`. Known unrelated working-tree items remained present and unstaged.
- **Files Changed:** Git history only, by the User.
- **Verification:** User-supplied evidence showed local `HEAD` and `origin/main` both at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`; remote alignment `PASS`; `PASS_M00_L03_PRIMARY_GIT_PUBLICATION`; Architect acceptance `PASS_M00_L03_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.
- **Expected Result:** The primary M00_L03 publication is remotely available. **COMPLETE / PASS**

## Step 36 - Reconcile publication metadata

- **Objective:** Record the exact primary publication identity.
- **Why:** Frozen lifecycle records must point to the actual published snapshot.
- **Action:** Reconciled the eight explicitly authorized lifecycle/history records with primary commit `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, primary push PASS, and primary remote alignment PASS. Preserved `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, and inactive/uncreated M00_L04. Recorded metadata Git publication and final publication verification as pending.
- **Files Changed:** `AGENTS.md`, root `README.md`, the M00 ADR, M00_L03 `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this transition guide only.
- **Verification:** Cross-record metadata consistency, protected-content hash comparison, 37-step sequence check, and roadmap-integrity review. No Git, Gradle, or Simulation command was run by this task.
- **Expected Result:** Primary publication metadata is complete and consistent without claiming the metadata commit or final publication completion. **COMPLETE / READY FOR INDEPENDENT PUBLICATION-METADATA REVIEW**

## Step 37 - Verify final publication completion

- **Objective:** Close the M00_L03 lifecycle and publication sequence.
- **Why:** Publication is not complete until final metadata and remote evidence are verified.
- **Action:** After independent metadata review, the User performs metadata Git staging, commit, and push; then conduct metadata remote-alignment and final read-only publication verification.
- **Files Changed:** None unless a separately authorized metadata correction is required.
- **Verification:** Final publication completion gate.
- **Expected Result:** M00_L03 is fully published and verified; M00_L04 remains inactive until separately prepared and activated. **PENDING**
