# M00_L05 to M00_L06 Step-by-Step Transition Guide

## Guide state

- Previous lesson: `M00_L05 - Feeder Foundation`
- Current lesson: `M00_L06 - Feeder Command Ownership`
- Current lifecycle: `COMPLETE / FROZEN / READ-ONLY`
- Implementation and accepted verification: complete
- Documentation phase and authorized bounded documentation/lifecycle repair: complete
- Initial Independent Closure Review: `HOLD` with three findings
- Latest Independent Closure Rereview: `HOLD` solely for this guide's stale transition history
- Current bounded transition-history repair: complete
- Final Independent Closure Rereview: `PASS / READY_FOR_FREEZE / NONE REMAINING`
- Architect acceptance: `PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED`
- Freeze authorization: `AUTHORIZED_FOR_FREEZE / CONSUMED`
- Freeze: complete
- Publication: pending / User-owned

This guide records the controlled evolution from the frozen Feeder foundation through final closure and lifecycle freeze of driver-command ownership. It does not claim real-hardware verification or publication.

## Step 1 — Confirm the predecessor boundary

**Objective:** Establish M00_L05 as the immutable starting point.

**Why:** A lesson must inherit its frozen predecessor and add only one concept.

**Action:** Confirm the M00_L05 Feeder IO, subsystem, observation, telemetry, Noop composition, and lifecycle evidence before considering M00_L06 changes.

**Files Changed:** None.

**Verification:** Predecessor inheritance review passed.

**Expected Result:** M00_L05 remains unchanged and authoritative.

## Step 2 — Copy and rename the lesson project

**Objective:** Create the M00_L06 candidate from the frozen predecessor.

**Why:** Repository workflow requires lesson inheritance rather than recreation from scratch.

**Action:** The User copied `M00_L05_FeederFoundation` to `M00_L06_FeederCommandOwnership` and renamed the project identity without changing the inherited lesson behavior.

**Files Changed:** New M00_L06 project copy and identity metadata only.

**Verification:** Preparation review confirmed the expected predecessor lineage.

**Expected Result:** M00_L06 begins as an inherited candidate of frozen M00_L05.

## Step 3 — Remove generated build artifacts

**Objective:** Prevent predecessor-generated state from becoming lesson evidence.

**Why:** `build/` and `.gradle/` are generated and must not be treated as authoritative inheritance.

**Action:** The User removed inherited generated artifacts before the preparation baseline.

**Files Changed:** Generated `build/` and `.gradle/` content only.

**Verification:** Preparation audit accepted the cleaned project state.

**Expected Result:** The baseline build starts from source and configuration, not copied outputs.

## Step 4 — Run the preparation baseline build

**Objective:** Prove the copied lesson builds before adding the new concept.

**Why:** A passing baseline separates inherited health from later implementation effects.

**Action:** The User ran the accepted Java 17 preparation baseline build.

**Files Changed:** None; generated build output only.

**Verification:** PASS — `BUILD SUCCESSFUL in 40s`; seven actionable tasks, six executed and one up-to-date; exit code 0.

**Expected Result:** The clean inherited candidate is ready for inheritance and architecture audit.

## Step 5 — Establish the M00_L06 lesson boundary

**Objective:** Define Feeder command ownership as the only new concept.

**Why:** Narrow scope keeps command responsibility separate from mechanism foundation and future coordination.

**Action:** Limit the lesson to one Feeder command, one driver binding, focused tests, and lesson documentation.

**Files Changed:** Lesson lifecycle documents only.

**Verification:** Architecture and roadmap-scope review passed.

**Expected Result:** No hardware adapter, mechanism sequencing, or future lesson behavior enters M00_L06.

## Step 6 — Preserve the Frozen Backbone

**Objective:** Keep dependency and observation flow intact.

**Why:** A command may request subsystem behavior but may not access Feeder IO, mutable inputs, hardware, or telemetry directly.

**Action:** Require the command to depend only on `FeederSubsystem`; retain `hardware -> IO -> IOInputs -> subsystem -> immutable Observation -> telemetry`.

**Files Changed:** None.

**Verification:** Read-only architecture review passed.

**Expected Result:** Subsystem ownership and read-only telemetry remain intact.

## Step 7 — Define the command lifecycle contract

**Objective:** Specify safe held-trigger behavior before implementation.

**Why:** Start, continued ownership, interruption, release, and disable behavior must be deterministic.

**Action:** Approve a command that requests feed in `initialize()`, does nothing in `execute()`, returns `false` from `isFinished()`, stops in `end(...)`, and does not run while disabled.

**Files Changed:** Design and lifecycle documentation.

**Verification:** Design Lock passed.

**Expected Result:** The Feeder runs only while the scheduled command owns it.

## Step 8 — Define composition and binding

**Objective:** Keep `RobotContainer` as composition root only.

**Why:** Controller binding belongs in composition, while behavior belongs in command and subsystem layers.

**Action:** Approve a left-bumper `whileTrue(...)` binding to one `RunFeederCommand` instance and retain no default Feeder command.

**Files Changed:** Design documentation.

**Verification:** Activation architecture review passed.

**Expected Result:** Left-bumper hold schedules the command; release cancels it and invokes safe stop.

## Step 9 — Define focused verification

**Objective:** Make command semantics and boundaries independently testable.

**Why:** Lifecycle behavior and architecture constraints require different evidence.

**Action:** Authorize command lifecycle, RobotContainer binding, command architecture, and inherited Feeder architecture boundary tests.

**Files Changed:** Test plan documentation.

**Verification:** Test boundary review passed.

**Expected Result:** Each required behavior has a focused verification owner.

## Step 10 — Confirm controlled activation and independent activation review

**Objective:** Make M00_L06 the sole editable lesson and independently confirm that activation.

**Why:** Repository governance permits only one active lesson.

**Action:** Reconcile status, plan, checklist, README, and transition-guide metadata for controlled activation.

**Files Changed:** `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, `README.md`, and this guide.

**Verification:** Controlled Activation passed; Independent Activation Review passed.

**Expected Result:** M00_L06 is `IN_PROGRESS / EDITABLE`; M00_L05 remains frozen.

## Step 11 — Authorize the exact implementation boundary

**Objective:** Prevent implementation scope drift.

**Why:** Authorization must identify production and direct-test files before editing.

**Action:** Authorize creation of `RunFeederCommand.java`, modification of `RobotContainer.java`, creation of three focused tests, and only the necessary inherited Feeder boundary-test reconciliation.

**Files Changed:** Authorization records only.

**Verification:** Exact implementation authorization granted.

**Expected Result:** No unrelated production or test file is in scope.

## Step 12 — Implement command ownership

**Objective:** Add safe command-based Feeder control.

**Why:** The subsystem must remain the behavior owner while the command owns the driver request lifecycle.

**Action:** Create `RunFeederCommand`; require `FeederSubsystem`; request feed once in `initialize()`; keep `execute()` empty; remain scheduled; stop in `end(...)`; bind driver left bumper with `whileTrue(...)` in `RobotContainer`.

**Files Changed:** `src/main/java/frc/robot/commands/RunFeederCommand.java`; `src/main/java/frc/robot/RobotContainer.java`.

**Verification:** Independent static implementation rereview passed.

**Expected Result:** Hold requests feed, release or interruption requests stop, and no default Feeder command exists.

## Step 13 — Add focused tests

**Objective:** Verify behavior, binding, and architectural isolation.

**Why:** Command scheduling behavior alone cannot prove package boundaries.

**Action:** Add `RunFeederCommandTest`, `RobotContainerFeederCommandBindingTest`, and `FeederCommandArchitectureBoundaryTest` with isolated scheduler cleanup.

**Files Changed:** The three named test classes.

**Verification:** Static test review passed.

**Expected Result:** Lifecycle, binding, requirements, disabled behavior, and prohibited dependencies are covered.

## Step 14 — Reconcile the inherited test contract

**Objective:** Update a predecessor-derived test whose assumption changed legitimately.

**Why:** The inherited test originally prohibited Feeder command presence before M00_L06; the authorized roadmap step now requires that command.

**Action:** Modify only `FeederArchitectureBoundaryTest` so it preserves all valid foundation boundaries while accepting the authorized command package.

**Files Changed:** `src/test/java/frc/robot/FeederArchitectureBoundaryTest.java`.

**Verification:** Classified `EXPECTED INHERITED TEST CONTRACT EVOLUTION`; subsequent static rereview passed.

**Expected Result:** The test protects current architecture without treating authorized lesson growth as a defect.

## Step 15 — Run the focused verification set

**Objective:** Verify the exact M00_L06 change set.

**Why:** Focused tests give direct evidence before broader regression.

**Action:** Run `RunFeederCommandTest`, `RobotContainerFeederCommandBindingTest`, `FeederCommandArchitectureBoundaryTest`, and `FeederArchitectureBoundaryTest`.

**Files Changed:** None.

**Verification:** PASS — `BUILD SUCCESSFUL in 7s`, four tasks up-to-date, exit code 0.

**Expected Result:** All four focused test classes pass.

## Step 16 — Run the full clean regression

**Objective:** Confirm no inherited behavior regressed.

**Why:** Focused success does not replace repository-wide lesson verification.

**Action:** Run `gradlew clean build`.

**Files Changed:** None; generated build output only.

**Verification:** PASS — `BUILD SUCCESSFUL in 37s`, seven tasks executed, exit code 0.

**Expected Result:** The complete inherited suite and clean build pass.

## Step 17 — Verify bounded Simulation behavior

**Objective:** Observe scheduler behavior through enable, hold, release, repeated hold, and disable.

**Why:** Simulation supplies runtime evidence that tests alone do not show.

**Action:** Record checkpoints A–G: disabled stopped; controller attached while disabled stopped; Teleop idle stopped; left bumper held gives Feeder `FEED_REQUESTED` and Intake `STOPPED`; release stops Feeder; second hold repeats feed request; disabling while held stops both mechanisms.

**Files Changed:** None.

**Verification:** `PASS_M00_L06_SIMULATION_LEFT_BUMPER_RELEASE_STOPPED`; `PASS_M00_L06_SIMULATION_DISABLE_WHILE_HELD_STOPPED`; `PASS_M00_L06_BOUNDED_SIMULATION_COMPLETE`.

**Expected Result:** Release and disable both produce fail-safe Feeder stop behavior.

## Step 18 — Record evidence limitations

**Objective:** Prevent Simulation evidence from being mistaken for hardware proof.

**Why:** `FeederIONoop` cannot validate physical integration.

**Action:** Record `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`; document that `available = false` and `connected = false` are expected and that CAN IDs 45–49 remain planning-only.

**Files Changed:** Lesson documentation.

**Verification:** Documentation evidence audit completed.

**Expected Result:** No motor, sensor, wiring, tuning, or real-robot claim is implied.

## Step 19 — Complete bilingual learning documentation

**Objective:** Give students structurally equivalent English and Vietnamese explanations.

**Why:** English is normative, while Vietnamese must preserve the same concepts and evidence without inventing claims.

**Action:** Create paired learning guides and reconcile lesson status, plan, checklist, README, and this transition guide.

**Files Changed:** Lesson documentation only.

**Verification:** Structural and semantic parity audit performed after editing.

**Expected Result:** Documentation is ready for independent closure review.

## Step 20 — Record the initial Independent Closure Review HOLD

**Objective:** Record why the first closure attempt stopped.

**Why:** A transition guide must preserve rejected gates and their repair requirements instead of implying that closure remained a future event.

**Action:** Submit the completed documentation phase to the Initial Independent Closure Review while keeping mechanism coordination, real Feeder hardware integration, and M00_L14 through M00_L16 outside M00_L06.

**Files Changed:** None.

**Verification:** The Initial Independent Closure Review returned `HOLD` for exactly three findings:

1. the root README and M00 roadmap ADR contained stale lifecycle metadata;
2. the English and Vietnamese learning guides claimed unsupported Intake `STOPPED` observations at Simulation checkpoints A, B, C, and E; and
3. this guide used `src/test/java/frc/robot/architecture/FeederArchitectureBoundaryTest.java` instead of `src/test/java/frc/robot/FeederArchitectureBoundaryTest.java`.

**Expected Result:** Closure stops pending a bounded documentation/lifecycle repair; `COMPLETE`, `FROZEN`, and `PUBLISHED` remain unclaimed.

## Step 21 — Complete the bounded documentation and lifecycle repair

**Objective:** Resolve only the three findings from the Initial Independent Closure Review.

**Why:** Closure may be reconsidered only after each recorded contradiction or unsupported claim is repaired without changing accepted technical evidence.

**Action:** Preserve the ADR's historical activation record and use its append-only lifecycle addendum and revision history; reconcile the root README current state while retaining M00_L06 as `IN_PROGRESS / EDITABLE`; remove the unsupported A/B/C/E Intake claims from both learning guides while preserving accepted Intake `STOPPED` evidence at D/F/G; and correct the inherited-test path to `src/test/java/frc/robot/FeederArchitectureBoundaryTest.java`.

**Files Changed:** Authorized documentation and lifecycle records only; no production Java or tests.

**Verification:** All three findings were repaired, and the bounded repair verdict was `READY_FOR_INDEPENDENT_CLOSURE_REREVIEW`.

**Expected Result:** The repaired documentation advances only to independent closure rereview, not to freeze authorization.

## Step 22 — Record the Independent Closure Rereview HOLD

**Objective:** Preserve the result of the fresh closure rereview after the three-item repair.

**Why:** Passing technical and evidence checks does not override a remaining lifecycle-history defect.

**Action:** Independently rereview governance and mirror validation, the Frozen Backbone and roadmap scope, production source against the Final Design Lock, the test contract and inherited-test reconciliation, accepted build/test and bounded Simulation evidence, safe-stop semantics, Feeder foundation and hardware scope, protected future scope, all three repaired findings, English/Vietnamese parity, evidence classification, and transition-guide lifecycle accuracy.

**Files Changed:** None.

**Verification:** Every listed technical, evidence, governance, repair, and bilingual-parity area passed, but transition-guide lifecycle accuracy remained `HOLD` because this guide had not recorded the documentation phase, Initial Independent Closure Review `HOLD`, bounded repair, and current closure-rereview sequence. The overall Independent Closure Rereview verdict was therefore `HOLD` for that sole remaining documentation-history gap.

**Expected Result:** M00_L06 remains `IN_PROGRESS / ACTIVE / EDITABLE`; closure PASS and freeze remain unclaimed pending repair and another independent rereview.

## Step 23 — Repair the transition history

**Objective:** Reconcile this guide with the actual completed lifecycle through the latest closure rereview.

**Why:** The transition guide must distinguish completed history from future gates and must not imply that the initial closure review is still pending.

**Action:** Record the Initial Independent Closure Review `HOLD`, its three findings, the bounded repair and `READY_FOR_INDEPENDENT_CLOSURE_REREVIEW` verdict, the latest Independent Closure Rereview `HOLD`, and its sole transition-history reason.

**Files Changed:** `docs/M00_L05_to_M00_L06_Step_by_Step.md` only.

**Verification:** Read-only self-review confirms the recorded history, exact inherited-test path, accepted technical evidence, protected future scope, and `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED` classification remain accurate.

**Expected Result:** The bounded transition-history repair is ready for another Independent Closure Rereview. No closure PASS, `READY_FOR_FREEZE`, `COMPLETE`, `FROZEN`, publication, Git publication, or M00_L07 start is claimed.

## Step 24 — Complete the final Independent Closure Rereview

**Objective:** Determine whether the repaired lesson has any remaining closure blocker.

**Why:** Lifecycle freeze requires a fresh independent PASS after the final bounded history repair.

**Action:** Rereview governance, architecture, scope, source, tests, accepted build and Simulation evidence, all three original repairs, the transition history, bilingual parity, lifecycle consistency, and evidence classification.

**Files Changed:** None.

**Verification:** The final Independent Closure Rereview returned `READY_FOR_FREEZE`; exact remaining findings were `NONE`.

**Expected Result:** The Architect may accept the result and authorize lifecycle freeze. **COMPLETE / PASS**

## Step 25 — Reconcile lifecycle and freeze M00_L06

**Objective:** Close the lesson as a protected snapshot while keeping publication separate.

**Why:** A completed lesson must become read-only after its accepted final closure gate.

**Action:** Consume `PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and `AUTHORIZED_FOR_FREEZE`; reconcile the canonical lifecycle records to `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, no active M00 lesson, and M00_L07 inactive/uncreated.

**Files Changed:** The eight canonical lifecycle/documentation files only.

**Verification:** Post-edit read-only consistency, roadmap, evidence, source/test integrity, publication-boundary, and protected-scope review.

**Expected Result:** M00_L06 is frozen and ready for the separate User-owned publication gate. **COMPLETE / PASS**

## Future governed work — PENDING

Primary publication, publication metadata reconciliation, metadata publication,
and final publication verification remain separate User-owned gates. M00_L07
preparation or activation requires separate authorization. No publication, Git
commit, Git push, or M00_L07 lifecycle event is claimed here.
