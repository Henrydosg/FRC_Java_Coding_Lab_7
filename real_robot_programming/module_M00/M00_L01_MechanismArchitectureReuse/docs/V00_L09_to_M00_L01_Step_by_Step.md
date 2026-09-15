# V00_L09 to M00_L01 - Step-by-Step Transition Guide

## Guide state

- **Current lesson:** `M00_L01 - Mechanism Architecture Reuse`
- **Current directory:** `M00_L01_MechanismArchitectureReuse`
- **Previous lesson:** `V00_L09_SwervePoseEstimatorVisionFusion @ 5d36529`
- **Previous lesson state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Guide status:** `FINAL / TECHNICAL AND LIFECYCLE CLOSURE RECORDED`
- **Lesson status:** `COMPLETE / FROZEN / READ-ONLY`
- **Freeze state:** `FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Design Lock:** `PASS_M00_L01_FINAL_DESIGN_LOCK`
- **Implementation authorization:** `NONE`
- **Technical / content closure readiness:** `PASS`
- **Final reconciliation review:** `PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`
- **Freeze authorization:** `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`
- **Git publication:** `PENDING USER GIT`

This engineering transition record is separate from the student-facing
learning guides. The authorized English and Vietnamese guides now exist with
identical 21-section structure, identity, meaning, evidence, and architecture
rules.

## Step 1 - Confirm final V00_L09 publication and metadata reconciliation

**Objective:** Establish the authoritative predecessor publication boundary.

**Why:** M00 must inherit from the final published main-line V00 lesson rather than D01 or an earlier V00 state.

**Action:** Accept V00_L09 implementation/freeze publication `6548c98`, metadata-reconciliation state `5d36529`, and `PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`.

**Files Changed:** None in V00_L09.

**Verification:** Current governance identifies V00_L09 as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

**Expected Result:** V00_L09 is the protected predecessor for M00_L01.

## Step 2 - Confirm M00 roadmap and governance authorization

**Objective:** Confirm that the successor module and its preparation were authorized.

**Why:** A copied directory cannot establish a new module or lesson lifecycle by itself.

**Action:** Accept the approved M00 ADR, `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, and `PASS_M00_GOVERNANCE_PREPARATION_PUBLICATION_CONFIRMED`.

**Files Changed:** None during the accepted authorization and publication gates.

**Verification:** The M00 ADR is `APPROVED / ROADMAP AUTHORIZED` and preserves the locked 16-lesson sequence.

**Expected Result:** M00 preparation has a valid governance basis without changing the roadmap.

## Step 3 - Lock the canonical M00_L01 identity

**Objective:** Use the exact approved lesson and directory identity.

**Why:** Lesson identity must be unambiguous across governance and lesson-local records.

**Action:** Lock `M00_L01 - Mechanism Architecture Reuse` and directory `M00_L01_MechanismArchitectureReuse`.

**Files Changed:** Current M00_L01 lifecycle and documentation records during controlled activation.

**Verification:** Every current activation record uses the canonical lesson and directory identity.

**Expected Result:** No alternate M00_L01 identity exists.

## Step 4 - Lock the predecessor and source

**Objective:** Record the exact inheritance source.

**Why:** D01 is historical reference material, not the post-V00 main-line predecessor.

**Action:** Set `V00_L09_SwervePoseEstimatorVisionFusion` as the predecessor and source.

**Files Changed:** Current M00_L01 lifecycle and documentation records only.

**Verification:** The predecessor is recorded as complete, frozen, read-only, published, and verified.

**Expected Result:** M00_L01 has one valid inheritance parent.

## Step 5 - Copy the frozen V00_L09 project

**Objective:** Create the M00_L01 candidate by inheritance development.

**Why:** Repository policy prohibits recreating a lesson from scratch.

**Action:** The User copied the complete frozen V00_L09 project to `module_M00`.

**Files Changed:** The new destination candidate only; no V00_L09 file.

**Verification:** User preparation evidence and the later hash comparison confirm a complete inherited candidate.

**Expected Result:** M00_L01 starts from the final V00_L09 architecture.

## Step 6 - Rename only the destination

**Objective:** Apply the locked M00_L01 filesystem identity.

**Why:** The frozen predecessor must retain its published name and location.

**Action:** The User renamed only the destination copy to `M00_L01_MechanismArchitectureReuse`.

**Files Changed:** Destination filesystem identity only.

**Verification:** The candidate exists at the exact approved destination and V00_L09 retains its identity.

**Expected Result:** The new lesson has an independent, correctly named project boundary.

## Step 7 - Remove copied generated artifacts

**Objective:** Establish a clean inherited baseline candidate.

**Why:** Copied build output must not be treated as source or verification evidence.

**Action:** The User removed only destination `build/` and `.gradle/` before the baseline build.

**Files Changed:** Generated destination artifacts only.

**Verification:** Accepted User preparation evidence records destination-only cleanup.

**Expected Result:** The inherited baseline build starts from source rather than copied output.

## Step 8 - Verify the inherited baseline build

**Objective:** Confirm that the unchanged inherited project builds before lesson documentation work.

**Why:** A new lesson must not conceal an inherited baseline failure.

**Action:** The User ran the locked WPILib Java 17 clean build.

**Files Changed:** Regenerated build output only.

**Verification:** `BUILD SUCCESSFUL in 20s`; 7 actionable tasks executed; gate `PASS_M00_L01_PREPARATION_BASELINE_BUILD`.

**Expected Result:** M00_L01 begins from a verified inherited baseline.

## Step 9 - Complete the Architecture / Inheritance Audit

**Objective:** Prove faithful inheritance without using Git or modifying files.

**Why:** Controlled activation requires evidence that preparation did not silently alter the frozen baseline.

**Action:** Compare all non-generated predecessor and candidate files by relative path and SHA-256.

**Files Changed:** None.

**Verification:** 601 files on each side; zero missing, candidate-only, or hash-different files; gate `PASS_M00_L01_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_CONTROLLED_ACTIVATION`.

**Expected Result:** The candidate is a legal, faithful V00_L09 inheritance copy.

## Step 10 - Complete the Frozen Backbone audit

**Objective:** Confirm that inherited package responsibilities and dependency direction remain intact.

**Why:** Starting M00 cannot redesign existing robot architecture.

**Action:** Review RobotContainer, IO/IOInputs, subsystem ownership, immutable Observations, telemetry, Constants, safe stop, and protected Swerve/Vision/autonomous boundaries.

**Files Changed:** None.

**Verification:** Frozen Backbone, Frozen Interface Contract, and Observation architecture audits are PASS.

**Expected Result:** M00_L01 extends curriculum without changing the backbone.

## Step 11 - Complete the M00-specific ownership audit

**Objective:** Confirm future independent mechanism and shooting ownership.

**Why:** M00 must not collapse separate capabilities into an unauthorized Shooter owner.

**Action:** Preserve `IntakeSubsystem + IntakeIO`, `FeederSubsystem + FeederIO`, `FlywheelSubsystem + FlywheelIO`, and `ElevatorSubsystem + ElevatorIO`; preserve shooting as `FlywheelSubsystem + FeederSubsystem + ShootCommand requiring both`.

**Files Changed:** None.

**Verification:** No Intake, Feeder, Flywheel, Elevator, `ShooterSubsystem`, or `ShooterIO` implementation exists in the candidate.

**Expected Result:** Future mechanism ownership remains explicit and independently governed.

## Step 12 - Complete the one-new-concept audit

**Objective:** Limit M00_L01 to Mechanism Architecture Reuse.

**Why:** Later mechanism, control, safety, coordination, and autonomous concepts belong to M00_L02-L16.

**Action:** Exclude mechanism implementation, hardware selection, closed-loop control, readiness, homing, travel limits, coordination, autonomous events, and new vendor APIs.

**Files Changed:** None.

**Verification:** The audit found no out-of-scope candidate content.

**Expected Result:** M00_L01 teaches exactly one new curriculum concept.

## Step 13 - Consume the Architect Design Lock

**Objective:** Lock the documentation-only lesson boundary before activation.

**Why:** Lifecycle activation must follow an explicit, reviewed Design Lock.

**Action:** Record `PASS_M00_L01_FINAL_DESIGN_LOCK`, with production Java, tests, configuration, vendordeps, deploy assets, runtime behavior, and mechanism hardware APIs all set to `NONE`.

**Files Changed:** Authorized lifecycle and governance documentation only.

**Verification:** The Design Lock is recorded consistently across current M00 governance and lesson-local records.

**Expected Result:** Future work cannot infer implementation authority from activation.

## Step 14 - Record controlled lifecycle activation

**Objective:** Make M00_L01 the sole editable lesson.

**Why:** Preparation, audit, and Design Lock do not independently change lifecycle state.

**Action:** Reconcile M00_L01 to `IN_PROGRESS / EDITABLE`, freeze state `EDITABLE`, and active lesson count `1`; preserve implementation and production-code authorization as `NONE`.

**Files Changed:** `AGENTS.md`, root `README.md`, the M00 ADR lifecycle record, four M00_L01 current metadata files, and this transition guide.

**Verification:** Current records agree on identity, predecessor, Design Lock, lifecycle state, active count, evidence classification, and authorization boundary.

**Expected Result:** M00_L01 is legally active for a later separately authorized documentation task.

## Step 15 - Implement student-facing English and Vietnamese documentation

**Objective:** Create the paired student learning guides for Mechanism Architecture Reuse.

**Why:** The architecture-only lesson requires student-facing explanation in both course languages without adding runtime behavior.

**Action:** Create separate English and Vietnamese guides with identical 21-section structure, lesson identity, architecture meaning, evidence, exercise, and knowledge-check boundaries.

**Files Changed:** The two authorized M00_L01 student learning guides only, plus the separately permitted checklist completion marks.

**Verification:** Both guides exist, preserve English as normative, and contain no Java implementation or hardware selection.

**Expected Result:** Student documentation is ready for independent review.

## Step 16 - Run the initial independent documentation review

**Objective:** Independently verify bilingual parity, architecture accuracy, teaching quality, evidence, and protected-content integrity.

**Why:** Implementation reporting cannot substitute for review of the actual files.

**Action:** Read both guides completely and inspect protected content without Git or Gradle.

**Files Changed:** None.

**Verification:** All reviewed areas passed except one shared Constants-authority omission.

**Expected Result:** The exact remaining documentation defect is isolated without changing files.

## Step 17 - Record the Constants-authority HOLD

**Objective:** Preserve the factual first-review result.

**Why:** Verification history must not be rewritten as an immediate PASS.

**Action:** Record `HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`: both guides omitted that `Constants.java` remains the default configuration authority.

**Files Changed:** None during the read-only review.

**Verification:** The omission was confirmed in both Section 8 configuration-boundary explanations; all other reviewed areas remained PASS.

**Expected Result:** A single minimal bilingual repair is authorized without scope expansion.

## Step 18 - Repair the Constants authority statement

**Objective:** Correct the sole documentation omission.

**Why:** Students must know the established default configuration authority without receiving invented mechanism values.

**Action:** Add semantically equivalent Section 8 statements that `Constants.java` remains the default configuration authority and M00_L01 defines no mechanism configuration values.

**Files Changed:** The English and Vietnamese learning guides only.

**Verification:** `PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`; no configuration value, safe-stop change, future API, or later-lesson content was introduced.

**Expected Result:** The prior HOLD is ready for independent rereview.

## Step 19 - Complete the independent documentation rereview

**Objective:** Confirm the repair and revalidate the complete guides.

**Why:** A repaired defect requires fresh independent evidence before advancing.

**Action:** Reread both guides, verify structure and technical parity, and repeat protected-content inspection.

**Files Changed:** None.

**Verification:** `PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`; the Constants defect is resolved, no collateral drift exists, and the Architect documentation review returned `PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

**Expected Result:** Documentation review is complete and ready for the final User regression.

## Step 20 - Verify the final inherited clean build and regression

**Objective:** Establish the required post-documentation final build evidence.

**Why:** The 20-second preparation baseline cannot substitute for the final closure build/regression gate.

**Action:** The User ran the inherited clean build/regression under WPILib 2026 Java 17.

**Files Changed:** Generated build output only; no source, test, configuration, dependency, or deploy file.

**Verification:** `BUILD SUCCESSFUL in 23s`; 7 actionable tasks executed; `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`.

**Expected Result:** The final build/regression prerequisite for closure review is satisfied.

## Step 21 - Complete the final architecture and documentation closure review

**Objective:** Determine technical/content readiness before lifecycle reconciliation and freeze authorization.

**Why:** Closure requires a final read-only review of architecture, documentation, evidence, roadmap scope, and protected content.

**Action:** Review governance, both guides, lifecycle records, the transition history, the accepted final build, and the protected predecessor comparison.

**Files Changed:** None.

**Verification:** `PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`; technical/content closure readiness is PASS.

**Expected Result:** Only controlled documentation/lifecycle reconciliation and the later explicit freeze action remain.

## Step 22 - Accept closure review for documentation reconciliation

**Objective:** Authorize the bounded pre-freeze record reconciliation.

**Why:** Current records must reflect accepted evidence before an Architect may separately authorize freeze.

**Action:** Consume `PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION` and reconcile only the eight authorized documentation files.

**Files Changed:** `AGENTS.md`, root `README.md`, the M00 ADR, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this transition guide.

**Verification:** All eight records preserve M00_L01 as `IN_PROGRESS / EDITABLE`, active lesson count `1`, freeze state `EDITABLE`, production-code authorization `NONE`, and pending freeze/publication.

**Expected Result:** The pre-freeze historical record is current and ready for independent reconciliation review; explicit Architect freeze authorization remains a separate later action.

## Step 23 - Complete the independent reconciliation review

**Objective:** Independently verify the eight-file pre-freeze reconciliation.

**Why:** Final freeze requires confirmation that lifecycle records, protected content, guides, and roadmap state remain consistent.

**Action:** Accept `PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION` and `PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`, then review the eight reconciled files, both learning guides, the 173-file protected comparison, the frozen predecessor, and all pending publication fields without modifying files or running Git or Gradle.

**Files Changed:** None.

**Verification:** `PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.

**Expected Result:** M00_L01 is ready for separate explicit Architect freeze authorization.

## Step 24 - Consume the Architect freeze authorization

**Objective:** Establish authority for the final documentation-only lifecycle transition.

**Why:** Technical/content readiness and reconciliation review do not independently freeze a lesson.

**Action:** Consume `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION` for M00_L01 only.

**Files Changed:** None during the Architect authorization decision.

**Verification:** The authorization permits only the eight-file lifecycle record update and does not authorize publication or technical changes.

**Expected Result:** The final M00_L01 lifecycle state may be recorded.

## Step 25 - Record the final M00_L01 lifecycle transition

**Objective:** Close the lesson's technical and documentation lifecycle.

**Why:** The authorized final state must be explicit and consistent across repository and lesson records.

**Action:** Transition M00_L01 from `IN_PROGRESS / EDITABLE` to `COMPLETE / FROZEN / READ-ONLY`, with freeze state `FROZEN / READ-ONLY`.

**Files Changed:** `AGENTS.md`, root `README.md`, the M00 ADR, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, lesson `README.md`, and this transition guide.

**Verification:** All eight records preserve the Design Lock, `THEORY VERIFIED`, runtime `NOT APPLICABLE`, production-code authorization `NONE`, and pending publication.

**Expected Result:** M00_L01 is complete, frozen, and read-only but not published.

## Step 26 - Reconcile the active lesson count

**Objective:** Record that no lesson is currently editable.

**Why:** A frozen lesson cannot continue to count as active, and this task does not activate M00_L02.

**Action:** Change the active lesson count from `1` to `0`; leave M00_L02 inactive and uncreated.

**Files Changed:** The same eight authorized lifecycle and governance documentation files.

**Verification:** Current records state active lesson count `0`, M00_L01 `COMPLETE / FROZEN / READ-ONLY`, and no active M00 lesson.

**Expected Result:** The repository has no active editable lesson after M00_L01 closure.

## Pre-freeze evidence summary

- Production Java changes: `NONE`.
- Test-code changes: `NONE`.
- Configuration/dependency changes: `NONE`.
- Deploy changes: `NONE`.
- Focused new tests: `NOT APPLICABLE`.
- Simulation: `NOT APPLICABLE`.
- Driver Station / Glass: `NOT APPLICABLE`.
- Real Hardware: `NOT APPLICABLE`.
- Evidence: `THEORY VERIFIED`.
- Technical/content closure readiness: `PASS`.

## Pending publication steps

27. User-owned precise Git staging: `PENDING`.
28. User Git commit: `PENDING USER COMMIT`.
29. User Git push: `PENDING USER PUSH`.
30. Remote publication verification: `PENDING`.
31. Publication metadata reconciliation if required: `PENDING`.

This transition guide is final for M00_L01 technical and lifecycle closure.
M00_L01 is `COMPLETE / FROZEN / READ-ONLY`, freeze state `FROZEN / READ-ONLY`,
with active lesson count `0`. It is `NOT YET PUBLISHED / PENDING USER GIT`.
M00_L02 is not active and is not created; the M00 module is not declared
complete.
