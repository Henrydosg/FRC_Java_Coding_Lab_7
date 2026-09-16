# M00_L01 to M00_L02 - Step-by-Step Transition Guide

## Guide state

- **Current lesson:** `M00_L02 - Mechanism Hardware Evidence Audit`
- **Directory:** `M00_L02_MechanismHardwareEvidenceAudit`
- **Predecessor:** `M00_L01 - Mechanism Architecture Reuse`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE`
- **Active state:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Design Lock:** `PASS_M00_L02_FINAL_DESIGN_LOCK`
- **Production code authorization:** `NONE`
- **Documentation implementation authorization:** `PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`
- **Documentation implementation:** `COMPLETE`
- **Independent documentation rereview:** `PASS`
- **Final User build:** `PASS`
- **Final Closure Review:** `PASS`
- **Documentation reconciliation:** `COMPLETE / RECORDED`
- **Independent reconciliation review:** `PASS`
- **Freeze authorization:** `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`
- **COMPLETE / FROZEN / READ-ONLY:** `COMPLETE / RECORDED`
- **Publication:** `PENDING USER GIT PUBLICATION`
- **Guide status:** `PASS / FREEZE RECORDED / PUBLICATION STEPS PENDING`
- **M00_L03:** `NOT ACTIVE / NOT CREATED`

This guide preserves the preparation defect and controlled repair as part of the factual lesson history. It does not hide or rewrite that evidence.

## Step 1 - Confirm final M00_L01 publication

**Objective:** Establish the frozen predecessor.

**Why:** M00_L02 must inherit from a complete, published lesson.

**Action:** Accept M00_L01 primary publication `83907ab`, metadata publication `f523118`, and `PASS_M00_L01_FINAL_PUBLICATION_COMPLETE`.

**Files Changed:** None by this historical step.

**Verification:** M00_L01 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

**Expected Result:** One authoritative predecessor is established.

## Step 2 - Complete the M00_L02 activation audit

**Objective:** Confirm the proposed lesson identity and boundary before lifecycle activation.

**Why:** Activation cannot precede a reviewed lesson scope.

**Action:** Audit the canonical title, filesystem identity, predecessor, one-new-concept rule, and roadmap position.

**Files Changed:** None.

**Verification:** M00_L02 is the second lesson in the locked 16-lesson roadmap.

**Expected Result:** The candidate is eligible for preparation and later Design Lock review.

## Step 3 - Accept the Architect preparation decision

**Objective:** Lock the candidate preparation boundary.

**Why:** Copying a lesson does not itself authorize implementation or activation.

**Action:** Accept `M00_L02 - Mechanism Hardware Evidence Audit`, directory `M00_L02_MechanismHardwareEvidenceAudit`, predecessor M00_L01, and production/test/configuration/runtime authorization `NONE`.

**Files Changed:** None.

**Verification:** Sole preparation concept is `MECHANISM HARDWARE EVIDENCE AUDIT`.

**Expected Result:** Preparation has one approved identity and scope.

## Step 4 - Perform the User copy and preparation attempt

**Objective:** Create the inherited candidate.

**Why:** Repository policy requires inheritance development rather than recreation.

**Action:** The User attempted to copy the complete frozen M00_L01 project to the approved M00_L02 destination.

**Files Changed:** The new candidate tree and generated build artifacts only.

**Verification:** The outer candidate contained an inherited WPILib project and later built successfully.

**Expected Result:** A candidate exists for read-only preparation review.

## Step 5 - Detect the preexisting destination

**Objective:** Preserve evidence that the destination existed before the intended copy.

**Why:** Existing destination content makes copy provenance uncertain until audited.

**Action:** Record the User's `Test-Path` result and stop message showing that the destination already existed.

**Files Changed:** None.

**Verification:** The pre-copy destination check returned true.

**Expected Result:** Preparation provenance is placed on hold instead of assumed clean.

## Step 6 - Detect the accidental nested copy

**Objective:** Determine the effect of the later interactive `Copy-Item` command.

**Why:** Copying a source directory into an existing destination can create an extra project boundary.

**Action:** Inspect the current filesystem for duplicate project roots and project markers.

**Files Changed:** None.

**Verification:** A full nested `M00_L01_MechanismArchitectureReuse` project containing 1,209 files was found inside M00_L02.

**Expected Result:** The defect is precisely isolated.

## Step 7 - Complete the read-only forensic audit

**Objective:** Determine whether the outer candidate is trustworthy and repairable.

**Why:** Deletion is unsafe until the intended root and accidental subtree are distinguished.

**Action:** Compare paths, hashes, timestamps, project markers, protected content, Constants.java, and root build evidence without modifying files.

**Files Changed:** None.

**Verification:** The outer root was a valid M00_L01 inheritance copy; the nested copy was isolated; outcome was `REPAIRABLE PREPARATION`.

**Expected Result:** One exact minimal repair is identified.

## Step 8 - Receive controlled repair authorization

**Objective:** Authorize removal of only the isolated accidental subtree.

**Why:** Repository directories must not be deleted without exact approval.

**Action:** Consume `PASS_M00_L02_CONTROLLED_NESTED_COPY_REPAIR_AUTHORIZED` and the Architect's accepted repair boundary.

**Files Changed:** None during authorization.

**Verification:** The authorized target was the nested M00_L01 directory only.

**Expected Result:** The User may perform one bounded repair.

## Step 9 - Remove the nested project

**Objective:** Restore one independent project at the M00_L02 root.

**Why:** One lesson must equal one WPILib project.

**Action:** The User removed only the authorized nested `M00_L01_MechanismArchitectureReuse` subtree.

**Files Changed:** The accidental nested copy was deleted; the outer candidate and predecessor were preserved.

**Verification:** `PASS_M00_L02_NESTED_COPY_REPAIR_COMPLETED` and `PASS_M00_L02_CONTROLLED_NESTED_COPY_REPAIR_ACCEPTED`.

**Expected Result:** No nested project remains.

## Step 10 - Complete post-repair verification

**Objective:** Prove the repair did not disturb the outer candidate.

**Why:** A successful deletion alone does not establish inheritance integrity.

**Action:** Recheck the destination structure, full hashes, protected scope, Constants.java, and duplicate-project markers.

**Files Changed:** None.

**Verification:** 604/604 comparable files and 173/173 protected files matched with zero missing, extra, or different files; nested project absent.

**Expected Result:** `PASS_M00_L02_POST_REPAIR_PREPARATION_VERIFIED_READY_FOR_ARCHITECTURE_INHERITANCE_AUDIT`.

## Step 11 - Retain inherited baseline build evidence

**Objective:** Preserve valid build evidence from the outer candidate.

**Why:** The isolated nested copy was not part of the root Gradle project.

**Action:** Accept the User-owned WPILib Java 17 baseline without rerunning Gradle.

**Files Changed:** None by this documentation step.

**Verification:** `BUILD SUCCESSFUL in 58s`; 7 actionable tasks, 637 tests, zero failures, errors, or skips.

**Expected Result:** Baseline build remains `PASS` after repair.

## Step 12 - Complete the Architecture / Inheritance Audit

**Objective:** Verify roadmap, Frozen Backbone, ownership, evidence semantics, and one-new-concept compliance.

**Why:** A clean copy still requires an approved lesson architecture.

**Action:** Review governance, inherited source boundaries, roadmap order, future ownership, and documentation-only scope.

**Files Changed:** None.

**Verification:** `PASS_M00_L02_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`.

**Expected Result:** The lesson is ready for a separate Architect Design Lock.

## Step 13 - Consume the Architect Final Design Lock

**Objective:** Lock evidence semantics and prohibit technical implementation.

**Why:** Controlled activation requires an explicit final lesson boundary.

**Action:** Consume `PASS_M00_L02_FINAL_DESIGN_LOCK`.

**Files Changed:** None during the Architect decision.

**Verification:** The lock defines exactly four fact states and authorizes no code, tests, configuration, runtime behavior, or mechanism APIs.

**Expected Result:** Documentation-only lifecycle activation is authorized.

## Step 14 - Record controlled lifecycle activation

**Objective:** Make M00_L02 the sole editable lesson.

**Why:** Preparation and Design Lock do not independently alter lifecycle state.

**Action:** Record M00_L02 as `IN_PROGRESS / EDITABLE`, freeze state `EDITABLE`, and active lesson count `1` across the eight authorized targets.

**Files Changed:** `AGENTS.md`, root `README.md`, M00 ADR, M00_L02 `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this guide.

**Verification:** Design Lock is recorded; technical authorizations remain `NONE`; documentation implementation remains pending.

**Expected Result:** M00_L02 is active for later separately authorized documentation work only.

## Step 15 - Authorize documentation implementation

**State:** `COMPLETE`

**Objective:** Separately authorize the bounded student-documentation task.

**Why:** Lifecycle activation does not authorize content implementation.

**Action:** Consume `PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`.

**Files Changed:** None yet.

**Verification:** The authorization is recorded in the lesson lifecycle documents.

**Expected Result:** The exact guide and matrix scope is authorized without technical implementation.

## Step 16 - Create the English learning guide

**State:** `COMPLETE`

**Objective:** Teach the evidence method in normative English.

**Why:** English is the normative course language.

**Action:** Create the authorized 25-section English guide with its 80-row evidence matrix.

**Files Changed:** `M00_L02_Mechanism_Hardware_Evidence_Audit_Learning_Guide_EN.md`.

**Verification:** 25 sections, 15 questions, 15 answers, and 80 matrix rows are present.

**Expected Result:** The normative English guide is complete.

## Step 17 - Create the Vietnamese learning guide

**State:** `COMPLETE`

**Objective:** Provide a student-friendly, semantically equivalent Vietnamese guide.

**Why:** M00 student-facing Markdown requires paired EN/VI delivery.

**Action:** Create the authorized semantically equivalent 25-section Vietnamese guide.

**Files Changed:** `M00_L02_Mechanism_Hardware_Evidence_Audit_Learning_Guide_VI.md`.

**Verification:** 25 sections, 15 questions, 15 answers, and equivalent matrix structure are present.

**Expected Result:** The paired guides are complete and ready for independent review.

## Step 18 - Implement the evidence matrix

**State:** `COMPLETE`

**Objective:** Provide a structured fact-level audit table.

**Why:** Hardware uncertainty must be explicit and traceable.

**Action:** Add equivalent EN/VI matrix rows and states without inventing values.

**Files Changed:** The authorized English and Vietnamese learning guides only.

**Verification:** Each matrix has 80 rows: 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN`.

**Expected Result:** Evidence is inspectable, bounded, and parity-aligned.

## Step 19 - Complete independent documentation review

**State:** `COMPLETE / HOLD FOUND`

**Objective:** Verify correctness, parity, evidence discipline, and architecture protection.

**Why:** Implementation reporting cannot substitute for independent review.

**Action:** Review all authorized documentation read-only and preserve the resulting HOLD.

**Files Changed:** None during review.

**Verification:** `HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`.

**Expected Result:** Three bounded documentation-quality defects are isolated for repair.

## Step 20 - Complete the minimal documentation repair

**State:** `COMPLETE`

**Objective:** Resolve only the three independently identified documentation defects.

**Why:** The review HOLD must be resolved without expanding scope or changing technical files.

**Action:** Add the explicit Constants authorization boundary, complete ownership/shooting lock, and required knowledge-check coverage to both guides.

**Files Changed:** The two learning guides only.

**Verification:** `PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_READY_FOR_INDEPENDENT_REREVIEW` and Architect acceptance are recorded.

**Expected Result:** The repaired guides are ready for independent rereview.

## Step 21 - Complete independent documentation rereview

**State:** `PASS`

**Objective:** Confirm the bounded repair resolves every prior finding.

**Why:** Repair implementation cannot self-certify its own adequacy.

**Action:** Independently rereview the repaired guides read-only.

**Files Changed:** None.

**Verification:** `PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD` and Architect acceptance are recorded.

**Expected Result:** Student documentation is ready for the final User build.

## Step 22 - Complete the final User clean build/regression

**State:** `PASS`

**Objective:** Confirm the inherited project still builds after documentation completion.

**Why:** The preparation baseline does not replace the distinct final closure build.

**Action:** The User ran the final clean build/regression under WPILib Java 17.

**Files Changed:** Generated build output only; no technical source change.

**Verification:** `BUILD SUCCESSFUL in 33s`; 7 actionable tasks executed; `PASS_M00_L02_FINAL_USER_BUILD_REGRESSION` and Architect acceptance recorded.

**Expected Result:** Final build/regression evidence is PASS.

## Step 23 - Complete the Final Closure Review

**State:** `PASS`

**Objective:** Confirm architecture, inheritance, evidence, and documentation closure readiness.

**Why:** A successful build alone does not establish complete lesson closure readiness.

**Action:** Perform the independent read-only final closure review.

**Files Changed:** None.

**Verification:** `PASS_M00_L02_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and Architect acceptance are recorded.

**Expected Result:** Only lifecycle/documentation synchronization remains.

## Step 24 - Reconcile documentation and lifecycle records

**State:** `COMPLETE / RECORDED`

**Objective:** Synchronize the eight authorized records with all accepted evidence.

**Why:** Final Closure Review passed while the lifecycle records still described pre-implementation state.

**Action:** Record authorization, implementation, HOLD, repair, rereview, final build, and closure review while retaining `IN_PROGRESS / EDITABLE`.

**Files Changed:** `AGENTS.md`, root `README.md`, M00 ADR, lesson `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this guide.

**Verification:** `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.

**Expected Result:** Records are current without freezing or publishing the lesson.

## Step 25 - Complete independent reconciliation review

**State:** `PASS`

**Objective:** Independently verify the bounded reconciliation.

**Why:** Reconciliation completion does not self-authorize freeze.

**Action:** Review the eight reconciled records read-only.

**Files Changed:** None.

**Verification:** `PASS_M00_L02_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.

**Expected Result:** The lesson is ready for the separate Architect freeze decision.

## Step 26 - Receive Architect freeze authorization

**State:** `PASS`

**Objective:** Obtain explicit authority for lifecycle closure.

**Why:** Closure and reconciliation reviews do not independently freeze a lesson.

**Action:** Consume the separately issued Architect freeze authorization.

**Files Changed:** None.

**Verification:** `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION` is recorded.

**Expected Result:** Lifecycle closure is authorized.

## Step 27 - Record COMPLETE

**State:** `COMPLETE`

**Objective:** Transition lesson status only after freeze authorization.

**Why:** M00_L02 is currently still editable.

**Action:** Record status `COMPLETE` in the eight authorized lifecycle records.

**Files Changed:** The eight authorized lifecycle documentation files.

**Verification:** Status is `COMPLETE` after authorization.

**Expected Result:** Lesson completion is accurately recorded.

## Step 28 - Record FROZEN

**State:** `COMPLETE`

**Objective:** Freeze the completed lesson snapshot.

**Why:** Completed lessons must be protected from ordinary edits.

**Action:** Record freeze state `FROZEN / READ-ONLY` in the authorized records.

**Files Changed:** The eight authorized lifecycle documentation files.

**Verification:** Freeze state becomes `FROZEN / READ-ONLY`.

**Expected Result:** The lesson snapshot is frozen.

## Step 29 - Record READ-ONLY

**State:** `COMPLETE`

**Objective:** Remove editability after authorized closure.

**Why:** A frozen lesson cannot remain editable.

**Action:** Record `COMPLETE / FROZEN / READ-ONLY` in the authorized records.

**Files Changed:** The eight authorized lifecycle documentation files.

**Verification:** Active and freeze states agree.

**Expected Result:** The lesson is read-only.

## Step 30 - Reconcile active lesson count to zero

**State:** `COMPLETE`

**Objective:** Record that no lesson remains active after closure.

**Why:** A frozen lesson cannot count as editable.

**Action:** Change active lesson count from `1` to `0` under the closure authorization.

**Files Changed:** The eight authorized lifecycle documentation files.

**Verification:** M00_L03 remains inactive and uncreated.

**Expected Result:** Repository lifecycle is internally consistent.

## Step 31 - Complete User Git staging

**State:** `PENDING`

**Objective:** Stage only the authorized closure files.

**Why:** Git operations are User-owned.

**Action:** The User performs precise staging after freeze recording.

**Files Changed:** None by Codex.

**Verification:** The User supplies staging evidence.

**Expected Result:** The intended publication set is staged.

## Step 32 - Complete User Git commit

**State:** `PENDING`

**Objective:** Create the lesson publication commit.

**Why:** A local frozen snapshot is not yet published.

**Action:** The User commits the staged lesson closure.

**Files Changed:** None by Codex.

**Verification:** Exact commit and subject are supplied.

**Expected Result:** A publication commit exists.

## Step 33 - Complete User Git push

**State:** `PENDING`

**Objective:** Publish the commit to the remote repository.

**Why:** A local commit alone does not establish remote publication.

**Action:** The User pushes the publication commit.

**Files Changed:** None by Codex.

**Verification:** Push evidence is supplied.

**Expected Result:** The publication commit reaches the remote.

## Step 34 - Verify remote publication

**State:** `PENDING`

**Objective:** Confirm local and remote publication identity.

**Why:** Publication claims require verified remote evidence.

**Action:** The User supplies branch and remote-reference evidence.

**Files Changed:** None.

**Verification:** Exact remote publication gate is recorded.

**Expected Result:** Lesson publication is verified.

## Step 35 - Reconcile publication metadata

**State:** `PENDING`

**Objective:** Record the verified publication identity separately.

**Why:** Publication metadata must preserve exact provenance.

**Action:** Perform a separately authorized metadata reconciliation and User publication.

**Files Changed:** Future authorized metadata records only.

**Verification:** Metadata commit, push, and remote verification pass.

**Expected Result:** Publication metadata is synchronized.

## Step 36 - Record final publication completion

**State:** `PENDING`

**Objective:** Close the publication lifecycle after all publication gates pass.

**Why:** Freeze and initial publication do not alone complete metadata reconciliation.

**Action:** Record the final publication gate in a separately authorized task.

**Files Changed:** Future authorized lifecycle metadata only.

**Verification:** Final publication completion is independently verified.

**Expected Result:** The M00_L02 publication lifecycle is fully complete.
