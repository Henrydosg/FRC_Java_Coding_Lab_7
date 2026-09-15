# V00_L08 to V00_L09 - Step-by-Step Transition Guide

## Guide state

- **Current lesson:** `V00_L09 - Swerve Pose Estimator Vision Fusion`
- **Previous lesson:** `V00_L08 - Real Vision Adapter Integration @ f34b210`
- **Previous lesson state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`
- **Guide status:** `FINAL / FROZEN / READ-ONLY`
- **Lesson status:** `COMPLETE / FROZEN / READ-ONLY`
- **Active lesson count:** `0`

This guide records the completed transition through implementation, automated
verification, Simulation, Driver Station / Glass, approved real-hardware
verification, bounded autonomous verification, final architecture review,
documentation reconciliation, final documentation review, and freeze
authorization. Only User Git publication remains pending.

## Step 1 - Preserve the frozen L08 predecessor

**Objective:** Establish the authoritative predecessor boundary.

**Why:** A new V00 lesson must inherit from the immediately preceding frozen
lesson and must not reopen or rewrite that predecessor.

**Action:** Preserve V00_L08 as `COMPLETE / FROZEN / READ-ONLY`. Use its
implementation publication `f34b210` as the predecessor implementation
identity. Retain the User-reported later publication-metadata reconciliation
`6415b17` as separate publication history.

**Files Changed:** None in V00_L08.

**Verification:** User/Architect evidence identifies V00_L08 as frozen and
published.

**Expected Result:** L08 remains the immutable parent of L09.

## Step 2 - Copy the L08 candidate to L09

**Objective:** Create an independent L09 project from the frozen L08 baseline.

**Why:** The standard workflow is inheritance development; L09 must not be
created as an unrelated project.

**Action:** Copy the prepared V00_L08 candidate into the L09 project boundary.

**Files Changed:** The prepared L09 candidate during the earlier User-owned
preparation; no L08 file.

**Verification:** User supplied candidate-preparation evidence.

**Expected Result:** L09 has an independent inherited project boundary.

## Step 3 - Rename to the locked roadmap identity

**Objective:** Make the directory and lesson identity agree with the V00 ADR.

**Why:** A stale or alternate identity can make the active lesson ambiguous.

**Action:** Use `V00_L09_SwervePoseEstimatorVisionFusion` as the directory and
lesson identity.

**Files Changed:** The prepared candidate identity during User-owned setup.

**Verification:** Roadmap identity reconciliation PASS was supplied.

**Expected Result:** The candidate matches the ADR-locked L09 identity.

## Step 4 - Remove copied generated artifacts

**Objective:** Establish a clean inherited baseline boundary.

**Why:** Generated output must not be mistaken for inherited lesson content or
source evidence.

**Action:** Remove copied generated artifacts during candidate preparation.

**Files Changed:** Generated artifacts in the prepared candidate only.

**Verification:** User supplied generated-artifact cleanup evidence.

**Expected Result:** The baseline comparison is made against lesson content,
not copied build output.

## Step 5 - Establish the inherited baseline

**Objective:** Confirm that the prepared L09 candidate builds before the new
concept is implemented.

**Why:** A feature must not hide an inherited baseline failure.

**Action:** Run the inherited baseline workflow in the User-owned preparation
phase.

**Files Changed:** None to lesson source; generated build output is not lesson
content.

**Verification:** User supplied baseline clean-build PASS.

**Expected Result:** L09 begins from a verified inherited baseline.

## Step 6 - Reconcile the roadmap identity

**Objective:** Confirm that L09 is the ninth V00 lesson and follows L08.

**Why:** The V00 ADR locks lesson order and one-concept progression.

**Action:** Reconcile the candidate metadata to V00_L09 and record V00_L08 as
the frozen predecessor.

**Files Changed:** L09 lifecycle metadata during the activation reconciliation.

**Verification:** Roadmap identity reconciliation PASS was supplied.

**Expected Result:** No L08 identity remains in current L09 metadata.

## Step 7 - Complete the pre-implementation architecture audit

**Objective:** Confirm that the proposed lesson preserves the Frozen Backbone.

**Why:** Fusion must enter through the approved Swerve ownership boundary.

**Action:** Review the L09 boundary against the Frozen Backbone, Frozen
Interface Contract, Document C, V00 ADR, and frozen L08 inheritance.

**Files Changed:** None during the audit.

**Verification:** User/Architect-supplied architecture audit PASS.

**Expected Result:** The design is ready for lifecycle activation without
changing global architecture.

## Step 8 - Complete the timing-source investigation

**Objective:** Establish the approved timing source for qualified measurements.

**Why:** Timestamped fusion requires an explicit, coherent time contract.

**Action:** Complete the real timing-source investigation before implementation.

**Files Changed:** None during the investigation.

**Verification:** User/Architect-supplied timing-source investigation PASS.

**Expected Result:** Timing is treated as a locked prerequisite, not guessed
during implementation.

## Step 9 - Approve the L09 Design Lock

**Objective:** Lock one new concept before implementation.

**Why:** The lesson must add guarded measurement fusion without moving estimator
ownership or introducing unrelated vision behavior.

**Action:** Lock the concept as:

```text
qualified timestamped AprilTag measurement
    -> post-scheduler VisionFusionCoordinator requirement
    -> guarded Swerve-owned admission
    -> SwerveDrivePoseEstimator.addVisionMeasurement(...)
```

Preserve Swerve ownership, vendor-neutral Vision above IO, RobotContainer as
composition root, and exclude MegaTag migration, dynamic quality covariance,
Constants tuning, and additional concepts.

**Files Changed:** None during Design Lock.

**Verification:** Architect gate `PASS_V00_L09_FINAL_DESIGN_LOCK`.

**Expected Result:** Implementation has one reviewed, bounded responsibility.

## Step 10 - Complete the runtime orchestration micro-audit

**Objective:** Confirm the orchestration boundary before implementation.

**Why:** The coordinator must not bypass scheduler ownership or Swerve's
estimator boundary.

**Action:** Review the post-scheduler `VisionFusionCoordinator` requirement and
its relationship to guarded Swerve admission.

**Files Changed:** None during the audit.

**Verification:** User/Architect-supplied runtime orchestration micro-audit
PASS.

**Expected Result:** The orchestration boundary is accepted for implementation.

## Step 11 - Accept the coordinator requirement

**Objective:** Record the explicit coordinator decision.

**Why:** The coordinator requirement is part of the approved L09 design and
must not be inferred later from implementation details.

**Action:** Accept the post-scheduler `VisionFusionCoordinator` requirement as
the L09 orchestration boundary.

**Files Changed:** None during the decision.

**Verification:** Coordinator requirement accepted in the supplied design
evidence.

**Expected Result:** The implementation authorization can reference one clear
coordination boundary.

## Step 12 - Perform documentation-only controlled activation

**Objective:** Make L09 the one and only active V00 lesson.

**Why:** Preparation and Design Lock do not themselves change lifecycle state.
Only an explicit activation reconciles the repository's editable-lesson rule.

**Action:** Reconcile AGENTS.md, root README.md, the four L09 lesson metadata
files, and this guide. Set L09 to `IN_PROGRESS / EDITABLE` with active lesson
count `1`. Keep L08 frozen.

**Files Changed:** The seven-file documentation/lifecycle activation scope
authorized for this task.

**Verification:** Cross-file read-only self-audit confirms L09 is the sole
active lesson and no implementation/runtime PASS is claimed.

**Expected Result:** L09 is legally active and editable for the next separately
authorized implementation step.

## Step 13 - Implement the locked fusion boundary

**Objective:** Add the single L09 estimator-fusion concept.

**Why:** Qualified vision measurements require a guarded handoff into the
estimator without moving state ownership out of Swerve.

**Action:** Implement the post-scheduler `VisionFusionCoordinator`, guarded
`SwerveSubsystem` admission, runtime ordering, and immutable fusion telemetry
observation under the Architect-controlled workflow.

**Files Changed:** L09 production and test files within the authorized
implementation boundary.

**Verification:** Current source inspection confirms the Design Lock flow and
Swerve-owned `SwerveDrivePoseEstimator.addVisionMeasurement(...)` mutation.

**Expected Result:** Qualified timestamped measurements can reach the estimator
without bypassing Swerve ownership or changing odometry semantics.

## Step 14 - Complete automated implementation verification

**Objective:** Verify the implemented behavior and inherited robot baseline.

**Why:** L09 must prove its new admission behavior without regressing inherited
Swerve, autonomous, vision, or telemetry contracts.

**Action:** Run focused L09 tests, focused Swerve/estimator/reset tests,
inherited regression, `compileTestJava`, the full suite, and a clean build.

**Files Changed:** Test and generated build output only; no additional
production behavior change is recorded by this verification step.

**Verification:** User fail-fast verification completed without an earlier
failure. The preserved generated evidence records 637 tests, 0 failures, 0
errors, and 0 skipped tests. The final clean build reported `BUILD SUCCESSFUL in
38s` with 7 actionable tasks executed.

**Expected Result:** Automated implementation and inherited regression evidence
are PASS.

## Step 15 - Close the direct A-G evidence gap

**Objective:** Directly prove every previously missing failure and admission
boundary.

**Why:** Indirect coverage is insufficient for stale, future, out-of-order,
reset-barrier, scheduler-failure, coordinator-failure, and telemetry-finally
contracts.

**Action:** Add the narrowly authorized direct tests. After the initial Robot
boundary tests exposed an invalid `Pose3d.kZero` quality fixture, preserve all
assertions and repair only the test setup by reusing deterministic,
quality-valid `VisionIOSimHarness` Frame A/B fixtures.

**Files Changed:** L09 test source only for the authorized evidence additions
and fixture repair; no production Java.

**Verification:** User verification passed stale, future, out-of-order,
reset-barrier, scheduler-suppression, coordinator-failure, and
telemetry-finally evidence. Gates
`PASS_V00_L09_NARROW_TEST_EVIDENCE_AUTHORIZED`,
`PASS_V00_L09_FAILURE_BOUNDARY_TEST_DEFECT_IDENTIFIED`,
`PASS_V00_L09_FAILURE_BOUNDARY_TEST_FIXTURE_REPAIR_ACCEPTED`, and
`PASS_V00_L09_AUTOMATED_EVIDENCE_CLOSURE_VERIFIED` are preserved.

**Expected Result:** Direct automated evidence A-G is PASS without weakening
production policy or assertions.

## Step 16 - Verify Simulation startup and estimator initialization

**Objective:** Establish the runtime foundation for simulated fusion.

**Why:** Frame-level fusion evidence is meaningful only after Robot startup and
the Swerve estimator initialize correctly.

**Action:** Run Simulation Gate 1 and verify estimator initialization using the
approved independent simulation ground-truth path.

**Files Changed:** None.

**Verification:** `PASS_V00_L09_SIMULATION_GATE_1_RUNTIME_STARTUP` and
`PASS_V00_L09_SIMULATION_ESTIMATOR_INITIALIZATION_RUNTIME_VERIFIED`.

**Expected Result:** The simulated robot and estimator are ready for controlled
vision-frame verification.

## Step 17 - Complete Simulation Gate 2

**Objective:** Verify accepted fusion, one-shot behavior, loss, and
reacquisition.

**Why:** L09 must demonstrate that fresh qualified measurements fuse exactly
once and that vision state transitions do not replay stale measurements.

**Action:** Apply deterministic Frame A and Frame B, verify each accepted
one-shot fusion, then verify vision loss and reacquisition behavior.

**Files Changed:** None.

**Verification:** Frame A accepted and one-shot PASS; Frame B accepted and
one-shot PASS; loss one-shot PASS; reacquisition PASS; architect gate
`PASS_V00_L09_SIMULATION_GATE_2_COMPLETE`.

**Expected Result:** Simulation verification is complete for the L09 concept.

## Step 18 - Verify Driver Station and Glass

**Objective:** Confirm operator-visible L09 runtime telemetry.

**Why:** The immutable observation and read-only telemetry flow must remain
visible without influencing robot behavior.

**Action:** Observe the accepted L09 telemetry through Driver Station and Glass.

**Files Changed:** None.

**Verification:** `PASS_V00_L09_DRIVER_STATION_GLASS_VERIFICATION`.

**Expected Result:** Driver Station / Glass verification is PASS while
telemetry remains read-only.

## Step 19 - Complete final review and freeze L09

**Objective:** Record final review and the authorized documentation-only
lifecycle transition to the frozen L09 state.

**Why:** L09 could be frozen only after its separate real-hardware, bounded
autonomous, architecture, and documentation gates passed. V00_L08 acquisition
evidence remained distinct from L09 fusion evidence.

**Action:** The deployed `/limelight/json` investigation identified the
flat-root result mismatch. The authorized narrow parser repair was followed by
focused repair verification, inherited/full regression, clean build,
redeployment, real acquisition, estimator initialization, stationary accepted
fusion, geometry consistency, controlled translation/rotation, target
loss/reacquisition, and camera disconnect/recovery. The bounded BLUE
`ONE_METER_PATH` Gate 9 then completed. Complete the independent final
documentation review and consume the explicit Architect authorization
`PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`. Record the documentation-only
transition of `V00_L09_SwervePoseEstimatorVisionFusion` to
`COMPLETE / FROZEN / READ-ONLY`.

**Files Changed:** The authorized seven lifecycle/documentation records only;
production source, tests, deploy assets, vendordeps, and V00_L08 remain
unchanged.

**Verification:** Deployed V00_L09 parser and real acquisition PASS; real
timing/result, estimator initialization, stationary accepted fusion, geometry
audit, controlled translation/rotation, loss/reacquisition, and disconnect/
recovery PASS. The BLUE `ONE_METER_PATH` preparation reached READY and the run
completed with `Reason = COMMAND_COMPLETED`, `State = COMPLETE`, and no adapter
fatal fault. The observed final pose was X `1.078895 m`, Y `-0.000257 m`,
heading `-0.280151 deg`; EstimatedPose was X `1.078895 m`, Y `-0.000257 m`,
heading `-0.277405 deg`. Final documentation review passed with
`PASS_V00_L09_FINAL_DOCUMENTATION_REVIEW_READY_FOR_FREEZE_AUTHORIZATION`, and
the Architect freeze authorization passed with
`PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`.

Vision was intentionally invalid/suppressed during Gate 9 because the physical
Tag 32 placement was not asserted to match the official field coordinate. Gate
9 proves bounded autonomous lifecycle and estimator compatibility, not exact
1.000 m endpoint accuracy or physical absolute-pose calibration accuracy.

One historical Driver Station overrun warning was observed. Independent
read-only review found no proven structural runtime defect. If it recurs,
capture WPILib/Driver Station timing epochs before any performance repair; no
specific cause is claimed.

**Expected Result:** Maintain the following final lifecycle state. User-owned
Git publication remains pending:

```text
THEORY VERIFIED
SIMULATION VERIFIED
REAL HARDWARE VERIFIED
LESSON STATUS: COMPLETE / FROZEN / READ-ONLY
GUIDE STATUS: FINAL / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
GIT COMMIT: PENDING USER COMMIT
GIT PUSH: PENDING USER PUSH
PUBLICATION: PENDING USER PUBLICATION
```
