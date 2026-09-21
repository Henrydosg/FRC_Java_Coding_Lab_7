# V00_L06 to V00_L07 Step-by-Step Transition Guide

## Purpose and current lifecycle state

This guide explains how
`V00_L07_VisionTimestampAndLatencyContract` was prepared from the final
authoritative V00_L06 snapshot. It is a student-facing record of the
reasoning, boundaries, and preparation gates for the next lesson.

Historical pre-closure state:

```text
V00_L07: IN_PROGRESS / DESIGN LOCKED / EDITABLE
IMPLEMENTATION AUTHORIZATION: AUTHORIZED BY ARCHITECT
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
AUTOMATED VERIFICATION: PASS
BUILD: PASS / CLEAN BUILD
GIT PUBLICATION: PENDING USER GIT
```

That was the state during implementation reconciliation. The final
read-only architecture review and closure/freeze authorization have now
passed. The current state is:

```text
V00_L07: COMPLETE / FROZEN / READ-ONLY
FINAL ARCHITECTURE REVIEW: PASS
FINAL CLOSURE / FREEZE: PASS
PUBLICATION: PUBLISHED @ d58bef0 / USER VERIFIED
PUBLICATION COMMIT: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
PUBLICATION SUBJECT: Complete V00_L07 vision timestamp and latency contract
PUSH: PASS / origin/main / USER VERIFIED
HEAD == origin/main: PASS
```

The User-owned publication gate has been completed. Repository-level lifecycle
reconciliation remains a separate pending task.

Authoritative lineage:

```text
V00_L06 @ 1327bf4
-> lesson-local publication metadata reconciliation @ 49c4286
-> repository lifecycle reconciliation @ 9d7b04b
-> copied and cleaned V00_L07 inherited candidate
-> V00_L07 inheritance audit and baseline build
-> V00_L07 Design Lock
-> Architect-authorized timing-contract implementation
-> focused and inherited verification
-> full suite and clean build
-> final read-only architecture review
-> authorized closure and freeze metadata
-> User-owned Git publication @ d58bef0
-> successful push and HEAD/origin agreement
```

## Step 1 - Confirm the authoritative predecessor

**Objective:** Establish the only valid source snapshot for V00_L07.

**Why:** A lesson must inherit from the final frozen predecessor, not from an
older candidate or an unfinished historical copy.

**Action:** Confirm V00_L06 is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at
`1327bf41736c8fe79ba58ec5eea9e0120bd978fb` (`1327bf4`), with lesson-local
publication metadata reconciliation at `49c42862aba1659dc1730b3b7229288e8c10fea2`
(`49c4286`).

**Files Changed:** None.

**Verification:** V00_L06 lesson records, repository lifecycle metadata, and
User-confirmed publication history agree.

**Expected Result:** V00_L06 is accepted as the frozen predecessor and remains
read-only.

## Step 2 - Prepare the inherited V00_L07 candidate

**Objective:** Create the V00_L07 project boundary through normal inheritance.

**Why:** The repository uses continuous inheritance; a new lesson is not
recreated from scratch.

**Action:** Copy the final V00_L06 project to
`V00_L07_VisionTimestampAndLatencyContract` and remove copied generated
`build/` and `.gradle/` output before baseline verification.

**Files Changed:** The new V00_L07 candidate only; no frozen predecessor file.

**Verification:** The expected V00_L07 directory and independent WPILib project
structure exist.

**Expected Result:** V00_L07 has the same starting architecture and project
configuration as V00_L06.

## Step 3 - Verify inheritance before lesson work

**Objective:** Prove that the candidate contains no accidental feature delta.

**Why:** A clean inheritance comparison separates predecessor behavior from
the new lesson concept.

**Action:** Compare V00_L07 with V00_L06 while excluding generated build
artifacts.

**Files Changed:** None during the comparison.

**Verification:** 236 comparable files matched with zero content differences;
77 production Java files and 63 test Java files matched with zero differences.

**Expected Result:** V00_L07 begins as a faithful inherited candidate with no
timing implementation, vendor integration, estimator fusion, or runtime wiring.

## Step 4 - Confirm the inherited safety and architecture boundary

**Objective:** Preserve the Frozen Backbone and the V00 vision boundaries.

**Why:** V00_L07 teaches timing semantics; it must not silently absorb later
camera or estimator responsibilities.

**Action:** Preserve RobotContainer as the Composition Root, keep observations
immutable and vendor-neutral, keep telemetry read-only, preserve canonical
WPILib field-frame ownership, and leave Swerve and autonomous architecture
unchanged.

**Files Changed:** None.

**Verification:** The inherited source and tests contain no V00_L07 timing
type, vendor-specific timing code, camera implementation, fusion call, or
additional wiring.

**Expected Result:** V00_L07 extends the roadmap without changing inherited
architecture.

## Step 5 - Record the one-concept V00_L07 learning objective

**Objective:** Define what the student is learning in this lesson.

**Why:** One lesson must introduce one primary architectural concept.

**Action:** Define V00_L07 as a vendor-neutral deterministic measurement
timestamp and latency contract, including freshness, ordering, and duplicate
semantics. The lesson answers when a vision measurement occurred, not merely
when the robot received it.

**Files Changed:** V00_L07 lesson metadata and transition documentation only.

**Verification:** The objective does not select a camera vendor or add pose
fusion.

**Expected Result:** The L07 concept is distinct from L06 quality and from
L08/L09 runtime work.

## Step 6 - Lock the canonical timing relationship

**Objective:** State the deterministic relationship that the future
implementation must preserve.

**Why:** Estimator fusion later needs a measurement timestamp in the robot
timebase, rather than an arrival time with unknown meaning.

**Action:** Use seconds for all temporal values and preserve:

```text
measurementTimestampSeconds
    = receiveTimestampSeconds - totalLatencySeconds
```

The measurement timestamp must not be later than the receive timestamp and
must eventually use the same compatible epoch as the estimator timebase.

**Files Changed:** None during this conceptual lock.

**Verification:** The relationship is pure, deterministic, and does not read a
global clock.

**Expected Result:** The timing contract has one unambiguous temporal basis for
future adapter and fusion lessons.

## Step 7 - Lock freshness, ordering, and duplicate semantics

**Objective:** Define how valid timestamps are compared without hidden state or
runtime clock access.

**Why:** Vision samples can arrive late, repeat, or arrive out of order.

**Action:** Evaluate freshness against an explicitly supplied reference
timestamp and explicit policy. Compare a new measurement timestamp with the
previous timestamp:

```text
new > previous  -> newer / ordered
new == previous -> duplicate
new < previous  -> out-of-order
```

Stale classification is based on deterministic measurement age versus the
explicit freshness policy.

**Files Changed:** None during this conceptual lock.

**Verification:** No evaluator is allowed to call `Timer`, read a global clock,
or depend on vendor runtime state.

**Expected Result:** Timing decisions are repeatable from explicit inputs.

## Step 8 - Lock the programming-contract error boundary

**Objective:** Separate malformed inputs from ordinary measurement rejection.

**Why:** Invalid timing data must not be silently normalized into apparently
valid robot state.

**Action:** Treat null required inputs, NaN, infinity, negative latency,
invalid negative freshness policy, and a measurement timestamp later than the
receive timestamp as deterministic programming-contract errors. Zero latency
is valid.

**Files Changed:** None during the historical activation step.

**Verification:** At this historical activation point, no production
implementation or final Java API had been authorized by the activation task.

**Expected Result:** The future implementation has a clear fail-fast boundary
without inventing a vendor-specific invalid-value convention.

## Step 9 - Preserve the L08 and L09 boundaries

**Objective:** Keep later responsibilities in their authorized lessons.

**Why:** Timing semantics must remain understandable and testable before real
camera integration or estimator fusion is introduced.

**Action:** Defer real camera adapters, Limelight/PhotonVision compatibility,
vendor timestamp conversion, camera synchronization, network transport, and
physical-camera verification to V00_L08. Defer
`SwerveDrivePoseEstimator.addVisionMeasurement(...)`, fusion, covariance or
standard-deviation selection, and estimator wiring to V00_L09.

**Files Changed:** None.

**Verification:** The V00 roadmap remains L07 → L08 → L09 with no merge,
reorder, or skipped concept.

**Expected Result:** V00_L07 remains implementation-neutral and vendor-neutral.

## Step 10 - Record the preparation baseline

**Objective:** Confirm that the inherited candidate is buildable before
implementation authorization.

**Why:** A baseline build distinguishes inherited environment/project defects
from future L07 defects.

**Action:** Record the User-supplied Java 17 baseline clean-build result:
`BUILD SUCCESSFUL in 55s`, `7 actionable tasks: 6 executed, 1 up-to-date`,
exit code `0`.

**Files Changed:** Lesson metadata only.

**Verification:** Baseline build PASS was supplied by the User.

**Expected Result:** The candidate is ready for separately authorized
implementation work.

## Step 11 - Record the historical activation boundary

**Objective:** Move only the lesson lifecycle metadata into the authorized
active state.

**Why:** Design Lock and implementation authorization are separate governance
decisions.

**Action:** At the activation stage, update the candidate’s lesson metadata to
`IN_PROGRESS / DESIGN LOCKED / EDITABLE`. At that historical point,
implementation authorization had not yet been granted and implementation had
not started. The later Architect authorization and implementation are recorded
in the following steps.

**Files Changed:**

- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `docs/V00_L06_to_V00_L07_Step_by_Step.md`

**Verification:** No production Java, test Java, configuration, vendordep,
asset, predecessor, governance, or roadmap file is changed.

**Expected Result:** V00_L07 is the sole active lesson and is ready for the
separately authorized implementation decision. This step does not claim that
the later implementation was part of activation.

## Step 12 - Execute the locked verification gate

**Objective:** Establish how the later implementation will be proven.

**Why:** This pure contract lesson does not require hardware or a runtime
demonstration.

**Action:** After implementation authorization, run deterministic focused unit
tests, inherited vision regression tests, the complete test suite, and a clean
build. Include zero and positive finite latency, malformed numeric values,
impossible timestamp relationships, freshness, duplicates, out-of-order
samples, repeated evaluation, and seconds-unit consistency.

**Files Changed:** No source files are changed by running the verification.

**Verification:** The final focused tests, inherited vision regressions, full
suite, and clean build passed. The full suite reported 593 tests with 0
failures, 0 errors, and 0 skipped tests. HALSIM, Glass, Driver Station, real
robot, and physical-Limelight verification are not completion requirements for
this pure contract lesson.

**Expected Result:** The timing contract is verified without claiming camera
integration, runtime fusion, or hardware behavior.

## Step 13 - Implement the immutable timing value

**Objective:** Represent the two independent timing facts with one immutable
vendor-neutral production value.

**Why:** L07 needs a stable contract that L08 can populate and L09 can later
consume without storing a duplicated derived timestamp.

**Action:** Add `VisionTiming` in
`frc.robot.observation.vision` with
`receiveTimestampSeconds` and `totalLatencySeconds`. Derive
`measurementTimestampSeconds()` as receive time minus total latency. Reject
nonfinite values, negative latency, and a nonfinite derived timestamp; allow
zero latency.

**Files Changed:**

- `src/main/java/frc/robot/observation/vision/VisionTiming.java`

**Verification:** The focused timing tests cover the formula, seconds units,
finite values, malformed values, negative latency, zero latency, and repeated
deterministic derivation.

**Expected Result:** Timing data is immutable, finite, deterministic, and
independent of vendor APIs or a global clock.

## Step 14 - Implement pure freshness and ordering evaluation

**Objective:** Add the deterministic policy evaluation required by the locked
L07 concept.

**Why:** A valid timestamp still needs explicit freshness and ordering
semantics for stale, duplicate, and out-of-order samples.

**Action:** Add `VisionTimingEvaluator` with `Freshness.FRESH` or
`Freshness.STALE`, and `Ordering.NEWER`, `Ordering.DUPLICATE`, or
`Ordering.OUT_OF_ORDER`. Require explicit reference time and maximum age for
freshness; compare canonical measurement timestamps for ordering. Do not read
`Timer`, hardware, vendor APIs, NetworkTables, or scheduler state.

**Files Changed:**

- `src/main/java/frc/robot/observation/vision/VisionTimingEvaluator.java`

**Verification:** The evaluator tests cover below/equal/above freshness
boundaries, zero age, zero maximum age, invalid policies, invalid reference
ordering, all three ordering outcomes, null arguments, and deterministic
repetition.

**Expected Result:** Timing decisions depend only on explicit inputs and use the
locked inclusive freshness boundary.

## Step 15 - Add focused contract tests

**Objective:** Prove the new production boundary without broadening the
architecture.

**Why:** The new public types need direct tests for both behavior and shape.

**Action:** Add focused JUnit tests for `VisionTiming` and
`VisionTimingEvaluator`, using explicit numeric seconds and no sleeps or wall
clock.

**Files Changed:**

- `src/test/java/frc/robot/observation/vision/VisionTimingTest.java`
- `src/test/java/frc/robot/observation/vision/VisionTimingEvaluatorTest.java`

**Verification:** Focused command:
`.\gradlew.bat --no-daemon test --tests frc.robot.observation.vision.VisionTimingTest --tests frc.robot.observation.vision.VisionTimingEvaluatorTest`
passed under the WPILib Java 17 toolchain with exit code `0`.

**Expected Result:** The new contract is directly executable and its malformed
input boundary is explicit.

## Step 16 - Prove inherited vision behavior remains unchanged

**Objective:** Confirm that adding L07 timing semantics did not modify the
existing L03-L06 vision contracts.

**Why:** L07 is a new contract layer, not a reason to rewrite VisionIO,
VisionObservation, VisionIOSim, pose geometry, or L06 quality evaluation.

**Action:** Run the existing vision regression classes for VisionIO,
VisionIOSim, VisionObservation, L06 quality, L05 pose estimation, and L03 frame
transforms.

**Files Changed:** None.

**Verification:** All six requested regression classes passed under Java 17;
the following command exited `0`:

`.\gradlew.bat --no-daemon test --tests frc.robot.io.vision.VisionIOTest --tests frc.robot.io.vision.VisionIOSimTest --tests frc.robot.observation.vision.VisionObservationTest --tests frc.robot.observation.vision.VisionMeasurementQualityEvaluatorTest --tests frc.robot.vision.AprilTagRobotPoseEstimatorTest --tests frc.robot.vision.VisionFrameTransformTest`

**Expected Result:** Inherited vision behavior remains compatible and the new
lesson has no vendor, runtime, telemetry, estimator, or actuation integration.

## Step 17 - Complete the full suite and clean build

**Objective:** Check the complete inherited project after the focused boundary
passes.

**Why:** A focused pass is not sufficient evidence that copied lesson behavior
and the new types coexist across the project.

**Action:** Run the complete `test` task, then run `clean build` under the
WPILib Java 17 toolchain:

- `.\gradlew.bat --no-daemon test`
- `.\gradlew.bat --no-daemon clean build`

**Files Changed:** Generated build output only; no tracked source, test, or
lesson configuration file was changed.

**Verification:** The full test suite passed with 593 tests, 0 failures, 0
errors, and 0 skipped. `clean build` passed with 7 actionable tasks executed
and exit code `0`.

**Expected Result:** The implementation is technically verified and ready for
the separate final architecture and lifecycle review.

## Step 18 - Reconcile implementation documentation without closing the lesson

**Objective:** Make the lesson records agree with the actual implementation
and evidence.

**Why:** Documentation must distinguish the completed implementation from the
still-pending closure and User-owned Git gates.

**Action:** Update lesson-local README, status, plan, checklist, and this guide
to record the two production files, two focused test files, test results, clean
build, deferred L08/L09 responsibilities, and the remaining lifecycle state.

**Files Changed:**

- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `docs/V00_L06_to_V00_L07_Step_by_Step.md`

**Verification:** The records state `IN_PROGRESS / DESIGN LOCKED / EDITABLE`,
`IMPLEMENTATION COMPLETE / AUTHORIZED BOUNDARY`, automated verification PASS,
and Git publication pending User commit/push. They do not claim COMPLETE,
FROZEN, READ-ONLY, or PUBLISHED.

**Expected Result:** A student can distinguish activation history,
implementation evidence, inherited behavior, and remaining closure authority.

## Step 19 - Complete the final architecture review

**Objective:** Decide whether the verified V00_L07 lesson is architecturally
ready to close and freeze.

**Why:** Implementation and automated tests alone do not authorize a lesson to
become a frozen curriculum snapshot.

**Action:** Perform the final read-only architecture and closure-readiness
review against the Design Lock, inherited V00_L06 boundary, Frozen Backbone,
observation purity, deferred V00_L08/L09 responsibilities, and User-supplied
verification evidence.

**Files Changed:** None.

**Verification:** The review passed. The focused timing tests, vision
regression, full suite, clean build, inheritance, architecture, and scope
gates were accepted as PASS. The User supplied Java 17 evidence with exit code
`0`, including `BUILD SUCCESSFUL in 22s` and 7 actionable tasks executed.

**Expected Result:** V00_L07 is authorized for closure and freeze, with no
production, test, predecessor, governance, or future-lesson scope expansion.

## Step 20 - Apply historical pre-publication closure and freeze metadata

**Objective:** Record the authorized final lesson state before Git publication.

**Why:** The lesson content can be complete and frozen before the User performs
the separate commit and push operation.

**Action:** Update only the V00_L07 lesson-local README, status, plan,
checklist, and this transition guide from the historical active state to
`COMPLETE / FROZEN / READ-ONLY`. Record Final Architecture Review PASS and
retain `PENDING USER GIT PUBLICATION` as the publication state at that
historical point.

**Files Changed:**

- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `docs/V00_L06_to_V00_L07_Step_by_Step.md`

**Verification:** All five lesson-local documents agree on the final frozen
lesson state. No production Java, test Java, V00_L06, governance, A01, or
other module file is changed. No commit hash or publication claim is added.

**Expected Result:** V00_L07 is a complete frozen read-only lesson awaiting
User-owned Git publication. The later publication is recorded in Step 21.

## Step 21 - Reconcile lesson-local publication metadata

**Objective:** Record the actual User-owned publication without rewriting the
historical pre-publication chronology.

**Why:** Lesson content was frozen before the publication commit existed; the
current lesson documents must later identify the real published snapshot.

**Action:** Record the User-confirmed publication commit
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f` (`d58bef0`) with subject
`Complete V00_L07 vision timestamp and latency contract`. Record commit PASS,
push PASS to `origin/main`, and HEAD equal to `origin/main`.

**Files Changed:**

- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `docs/V00_L06_to_V00_L07_Step_by_Step.md`

**Verification:** User supplied zero unexpected staged files, zero unexpected
committed files, commit exit code `0`, push exit code `0`, and matching full
HEAD/origin commit identities. The separate reconciliation commit for these
metadata edits does not yet exist; no hash is invented for it.

**Expected Result:** The lesson-local records identify V00_L07 as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ d58bef0 / USER VERIFIED` while
repository-level lifecycle reconciliation remains pending.

## Final closure result

V00_L07 is now a complete, frozen, read-only lesson:

```text
COMPLETE / FROZEN / READ-ONLY
IMPLEMENTATION AUTHORIZATION: AUTHORIZED BY ARCHITECT
IMPLEMENTATION: COMPLETE / AUTHORIZED BOUNDARY
AUTOMATED VERIFICATION: PASS
FULL TEST SUITE: PASS / 593 TESTS / 0 FAILURES / 0 ERRORS / 0 SKIPPED
CLEAN BUILD: PASS / BUILD SUCCESSFUL in 22s / 7 ACTIONABLE TASKS EXECUTED / EXIT CODE 0
FINAL ARCHITECTURE REVIEW: PASS
FINAL CLOSURE / FREEZE: PASS
PUBLICATION: PUBLISHED @ d58bef0 / USER VERIFIED
PUBLICATION COMMIT: d58bef0d17d202ce1dd0b8645635a8c35095dd3f
PUBLICATION SUBJECT: Complete V00_L07 vision timestamp and latency contract
PUSH: PASS / origin/main / USER VERIFIED
HEAD == origin/main: PASS
```

The frozen V00_L06 predecessor remains unchanged. The implementation adds no
vendor dependency, runtime wiring, telemetry, estimator fusion, or hardware
behavior. The historical `IN_PROGRESS / DESIGN LOCKED / EDITABLE` state is
preserved above as lifecycle provenance. The User-owned V00_L07 publication is
complete at `d58bef0`; repository-level reconciliation remains pending.
