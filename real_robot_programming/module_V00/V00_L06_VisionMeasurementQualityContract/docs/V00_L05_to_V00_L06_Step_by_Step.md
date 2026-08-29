# V00_L05 to V00_L06 Step-by-Step Transition Guide

## Purpose and current lifecycle state

This guide explains how V00_L06_VisionMeasurementQualityContract is derived
from the final authoritative V00_L05_AprilTagRobotPoseEstimation snapshot.
This guide is finalized as part of the V00_L06 closure record. V00_L06 is now
COMPLETE / FROZEN / READ-ONLY; User Git publication remains pending.

Authoritative lineage:

~~~text
V00_L05 @ 6482160
-> metadata reconciliation @ 3161dfb
-> copied and cleaned V00_L06 inherited baseline
-> distance-only measurement-quality Design Lock
-> authorized V00_L06 implementation
~~~

## Step 1 - Confirm the authoritative predecessor

**Objective:** Establish the only valid source snapshot for V00_L06.

**Why:** A lesson must inherit the final frozen predecessor rather than an
older or reconstructed approximation.

**Action:** Confirm V00_L05 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED at
6482160 and record its later metadata reconciliation at 3161dfb.

**Files Changed:** None.

**Verification:** Repository authority and lesson lifecycle records were
audited.

**Expected Result:** The V00_L06 lineage begins from the published V00_L05
implementation and its reconciled metadata.

## Step 2 - Prepare the V00_L06 inherited baseline

**Objective:** Create the next independent WPILib lesson without altering the
predecessor.

**Why:** The repository lifecycle requires copy, rename, generated-artifact
cleanup, and baseline verification rather than reconstruction from scratch.

**Action:** The User prepared
V00_L06_VisionMeasurementQualityContract from final V00_L05 and removed copied
generated artifacts before baseline verification.

**Files Changed:** The new V00_L06 lesson candidate only.

**Verification:** User-supplied inherited WPILib Java 17 baseline build PASS.

**Expected Result:** A buildable V00_L06 candidate whose inherited source still
matches V00_L05.

## Step 3 - Prove inheritance and protect frozen lessons

**Objective:** Distinguish inherited behavior from new V00_L06 work.

**Why:** Any unexplained difference would invalidate the controlled lesson
boundary.

**Action:** Compare predecessor and candidate while excluding generated
artifacts and lesson-identity documentation.

**Files Changed:** None.

**Verification:** 232 comparable non-generated files had zero differences.
All 75 inherited production Java files and 62 inherited test Java files were
identical. Build/configuration, wrapper, vendordeps, deploy/resources, and
PathPlanner assets were unchanged.

**Expected Result:** Frozen V00_L01-L05 behavior, the Frozen Backbone, Frozen
Interface Contract, and Document C architecture remain inherited.

## Step 4 - Identify the one new learning objective

**Objective:** Add measurement-quality classification without adding later
vision-pipeline responsibilities.

**Why:** V00_L05 creates a geometrically valid pose candidate but deliberately
does not decide whether a measurement should be accepted for future fusion.

**Action:** Define V00_L06 as a pure deterministic evaluator of one immutable
TargetObservation using only camera-to-target distance.

**Files Changed:** Lesson-local planning documentation.

**Verification:** Architect-approved Design Lock.

**Expected Result:** Students can separate structural geometry from explicit
measurement-quality policy.

## Step 5 - Lock the quality-result state space

**Objective:** Make accepted and rejected states impossible to represent
inconsistently.

**Why:** A result such as REJECTED / LOW / NONE would be ambiguous and unsafe
for later consumers.

**Action:** Authorize VisionMeasurementQuality as an immutable record with
Acceptance, UncertaintyClass, and RejectionReason enums. Permit only:

- ACCEPTED / LOW / NONE
- ACCEPTED / MEDIUM / NONE
- ACCEPTED / HIGH / NONE
- REJECTED / UNUSABLE / TARGET_TOO_FAR

**Files Changed:** Planned new
src/main/java/frc/robot/observation/vision/VisionMeasurementQuality.java.

**Verification:** Focused tests constructed all four valid tuples and proved
every other non-null enum tuple plus each null enum position throws.

**Expected Result:** Every constructed result has coherent acceptance,
uncertainty, and reason semantics.

## Step 6 - Lock the distance policy

**Objective:** Express the lesson policy as immutable ordered thresholds.

**Why:** Explicit thresholds make the behavior deterministic, testable, and
independent of vendor confidence values.

**Action:** Authorize nested Policy values lowMaxMeters, mediumMaxMeters, and
maximumAcceptedMeters with:

~~~text
0 <= lowMaxMeters <= mediumMaxMeters <= maximumAcceptedMeters
~~~

Equality remains valid and may create empty bands.

**Files Changed:** Planned new
src/main/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluator.java.

**Verification:** Focused tests passed for negative values, NaN, positive and
negative infinity, decreasing thresholds, and all three equality-policy forms.

**Expected Result:** Malformed configuration throws an exception; valid
boundary-equality policies remain usable.

## Step 7 - Lock distance extraction and classification

**Objective:** Ensure the evaluator has one unambiguous mathematical input.

**Why:** Scoring a Pose3d candidate, a whole observation, or multiple targets
would expand the lesson and hide the policy being taught.

**Action:** Compute exactly:

~~~java
target.cameraToTarget().getTranslation().getNorm()
~~~

Then apply ordered inclusive bounds for LOW, MEDIUM, HIGH, and finally
UNUSABLE / TARGET_TOO_FAR.

**Files Changed:** Planned VisionMeasurementQualityEvaluator.java.

**Verification:** Focused tests passed at zero, below and exactly on each
boundary, between boundaries, above maximum, all equality-policy forms, and a
3-4-12 translation proving use of the full 3D norm.

**Expected Result:** A deterministic classification whose branch ownership is
clear at every threshold.

## Step 8 - Separate malformed input from measurement rejection

**Objective:** Preserve the meaning of TARGET_TOO_FAR as a valid measured
distance outside policy.

**Why:** Null inputs, invalid thresholds, or nonfinite arithmetic are
programming/configuration defects, not normal rejected measurements.

**Action:** Require null arguments to fail with NullPointerException and
invalid policies, quality tuples, or nonfinite computed norms to fail with
IllegalArgumentException. Explicitly check Double.isFinite after getNorm
because finite translation components can overflow during norm calculation.

**Files Changed:** Planned production and focused test files.

**Verification:** Focused tests passed for null arguments, NaN/infinite target
geometry at the inherited TargetObservation boundary, malformed policy, and a
finite-component norm that overflows to infinity in the evaluator.

**Expected Result:** Consumers can trust every returned rejection as a genuine
finite-distance policy outcome.

## Step 9 - Activate only the authorized lesson boundary

**Objective:** Make V00_L06 editable without changing frozen or future lessons.

**Why:** Repository governance permits exactly one active editable lesson.

**Action:** Reconcile README, LESSON_STATUS, LESSON_PLAN, LESSON_CHECKLIST, and
this transition guide. At this historical activation point, record V00_L06 as
IN_PROGRESS / EDITABLE with active lesson count 1.

**Files Changed:**

- README.md
- LESSON_STATUS.md
- LESSON_PLAN.md
- LESSON_CHECKLIST.md
- docs/V00_L05_to_V00_L06_Step_by_Step.md

**Verification:** Documentation consistency review before Java implementation.

**Expected Result:** Controlled activation PASS; V00_L01-L05 remain frozen and
V00_L07-L09 remain not started.

## Step 10 - Implement the exact authorized files

**Objective:** Add the locked contract without architectural drift.

**Why:** New-only files preserve the inherited production architecture and
make the lesson delta easy to audit.

**Action:** Create exactly two production files and one focused test file:

~~~text
src/main/java/frc/robot/observation/vision/VisionMeasurementQuality.java
src/main/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluator.java
src/test/java/frc/robot/observation/vision/VisionMeasurementQualityEvaluatorTest.java
~~~

**Files Changed:** The three files above only.

**Verification:** Implementation completed within the exact three-file
boundary. Clean compileJava passed under WPILib Java 17.

**Expected Result:** No existing production file changes.

## Step 11 - Run focused and inherited verification

**Objective:** Prove the new semantics and protect all inherited vision
contracts.

**Why:** Focused tests catch policy defects; inherited tests catch regression
outside the lesson concept.

**Action:** Compile production/test Java, run the focused evaluator test, run
the six inherited vision test classes, run the full suite, and run a clean
build.

**Files Changed:** None, except generated build output.

**Verification:** The initial TERRA/Codex-local compileTestJava classpath
failure and bounded-javac workaround are preserved as historical evidence.
They are classified as `SUPERSEDED / ENVIRONMENT-PROCESS-ONLY` and are not a
current lesson blocker. User-controlled standard WPILib Java 17 verification
then passed compileJava, compileTestJava, the focused test, inherited vision
regressions, the full test suite, and the clean build with `BUILD SUCCESSFUL`.

**Expected Result:** Focused, inherited, full-suite, and standard clean-build
evidence all pass. No Gradle or classpath repair is required.

## Step 12 - Reconcile evidence and close later

**Objective:** Preserve an accurate long-term lifecycle record.

**Why:** Local implementation success is not the same as User verification,
final architecture approval, lesson freeze, or Git publication.

**Action:** Reconcile the five lesson-local documents with the current User
verification and the independent final architecture review. Following the
Architect's closure authorization, record V00_L06 as COMPLETE / FROZEN /
READ-ONLY. The User then owns Git add, commit, and push.

**Files Changed:** The five lesson-local documentation files only.

**Verification:** Documentation reconciliation PASS. User standard WPILib Java
17 verification is PASS, and the independent architecture review was PASS WITH
DOCUMENTATION REPAIR REQUIRED; this reconciliation completed that requirement.
Final closure and freeze metadata are approved and complete. User Git
publication remains pending.

**Expected Result:** A final transition guide that clearly distinguishes
inheritance, new V00_L06 behavior, automated evidence, User-owned evidence,
closure state, and Git publication state.

## Scope intentionally deferred

V00_L06 does not add vendor APIs, a real camera, timestamp or latency,
covariance or standard deviation, pose fusion, addVisionMeasurement, Swerve or
RobotContainer wiring, telemetry, NetworkTables, alliance transforms,
PathPlanner changes, whole-observation scoring, target ranking, or multi-target
aggregation.
