# A01_L09 to V00_L01 Step-by-Step Transition Guide

Status: `FINAL / PASS`  
Source: `A01_L09_PathPlannerNamedCommandsAndEventMarkers - COMPLETE / FROZEN / READ-ONLY`  
Target: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics - COMPLETE / FROZEN / READ-ONLY`

This final guide records the approved transition through activation, design
lock, implementation, post-change verification, exact changed-file audit, and
lesson closure.

## Step 1 - Copy the Frozen Source

- Step: 1
- Objective: create V00_L01 only from the approved frozen predecessor.
- Why: the lesson lifecycle prohibits recreation from scratch and protects the
  verified A01 baseline.
- Action: copy the complete frozen A01_L09 WPILib project into
  `real_robot_programming/module_V00/` without modifying the source project.
- Files Changed: new copied V00_L01 project only; A01_L09 unchanged.
- Verification: 125 inherited source, test, deploy, Gradle-wrapper, and
  vendor-dependency files compare byte-for-byte equal.
- Expected Result: one independent inherited V00 project with the full frozen
  robot baseline.

## Step 2 - Rename to the Authorized Identity

- Step: 2
- Objective: use the exact directory identity locked by the V00 ADR.
- Why: roadmap IDs and lesson order are governance-controlled.
- Action: name the copied project
  `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`.
- Files Changed: copied directory identity only.
- Verification: the directory exists at the approved module path; no V00_L02
  or other V00 lesson exists.
- Expected Result: exact authoritative V00_L01 identity.

## Step 3 - Remove Copied Generated Artifacts

- Step: 3
- Objective: prevent inherited build caches and outputs from becoming baseline
  evidence.
- Why: the approved workflow requires the copy's `build/` and `.gradle/`
  directories to be removed before the first build.
- Action: remove `build/` and `.gradle/` from the copied V00_L01 project only.
- Files Changed: generated artifacts in the copied project only.
- Verification: the V00_L01 directory creation predates the current `build/`
  and `.gradle/` creation times; the baseline later recreated both directories,
  and all six baseline tasks executed.
- Expected Result: the baseline is generated from source rather than copied
  output.

## Step 4 - Run the Inherited Baseline

- Step: 4
- Objective: prove the untouched inherited project builds and tests before V00
  implementation.
- Why: later failures must be distinguishable from the frozen baseline.
- Action: the User ran the required baseline build in WPILib VS Code.
- Files Changed: baseline-generated `build/` and `.gradle/` only.
- Verification: User evidence: `BUILD SUCCESSFUL in 1m 4s`; `6 actionable
  tasks: 6 executed`; inherited tests PASS. Current XML reports show 446 tests,
  zero failures, errors, or skips.
- Expected Result: `PASS / USER-VERIFIED` inherited baseline.

## Step 5 - Perform the Architecture Audit

- Step: 5
- Objective: identify the legal future Vision connection points without adding
  code.
- Why: V00 must extend, not redesign, the Frozen Backbone.
- Action: review governance, the V00 ADR, frozen A01_L09 architecture/source/
  tests, pose-estimator ownership, AutoBuilder pose consumption, observation
  flow, and all deferred V00 lesson boundaries.
- Files Changed: V00_L01 lesson documentation only.
- Verification: RobotContainer remains composition root; SwerveSubsystem remains
  sole estimator/localization/future-fusion owner; autonomous remains a
  `getEstimatedPose()` consumer; no camera/vendor dependency is present.
- Expected Result: architecture review PASS with no regression.

## Step 6 - Lock the L01 Frame and Extrinsic Design

- Step: 6
- Objective: remove frame-direction, axis, sign, and unit ambiguity before
  implementation.
- Why: reversing or double-applying a 3D transform would corrupt every later
  field-layout, pose-estimation, and fusion lesson.
- Action: document WPILib NWU field, robot, camera, and AprilTag frames;
  `robotToCamera` direction; inverse/composition semantics; immutable future
  configuration authority; and all deferred lesson boundaries.
- Files Changed: `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`,
  `LESSON_CHECKLIST.md`, and this transition guide.
- Verification: all six physical camera mounting inputs remain explicitly TBD;
  no fake values, vendor, production Java, tests, configuration, simulation,
  real adapter, or fusion were added.
- Expected Result: V00_L01 is `IN_PROGRESS / EDITABLE`, design lock PASS, and
  ready for ChatGPT implementation review.

## Step 7 - Add the Pure Frame Helper

- Step: 7
- Objective: encode only the locked frame composition and mounting-inverse
  operations.
- Why: later vision lessons need one deterministic, vendor-neutral authority
  for transform direction without introducing IO or runtime behavior.
- Action: add non-instantiable `VisionFrameTransform` with
  `fieldToCamera(...)`, `cameraToRobot(...)`, and
  `fieldToRobotFromCamera(...)`; validate null and nonfinite inputs/results.
- Files Changed:
  `src/main/java/frc/robot/observation/vision/VisionFrameTransform.java`.
- Verification: `compileJava` PASS using the resolved WPILib 2026 Java 17 API;
  source review confirms no state, hardware, vendor, telemetry, subsystem,
  Driver Station, NetworkTables, or configuration dependency.
- Expected Result: one pure L01 geometry helper implementing exactly the
  approved API surface.

## Step 8 - Add Focused Contract Tests

- Step: 8
- Objective: prove direction, inversion, composition order, axes, units,
  validation, determinism, and immutability.
- Why: a reversed or silently nonfinite transform could contaminate every
  later pose-estimation and fusion lesson.
- Action: add 18 synthetic-geometry JUnit tests with no camera or vendor
  dependency.
- Files Changed:
  `src/test/java/frc/robot/observation/vision/VisionFrameTransformTest.java`.
- Verification: `compileTestJava` PASS and focused tests `18/18 PASS` under
  normal filesystem access.
- Expected Result: the locked L01 contract is independently executable and
  deterministic.

## Step 9 - Reconcile Learning Documentation and Regression Evidence

- Step: 9
- Objective: explain the contract bilingually and prove the frozen inheritance
  remains intact.
- Why: the lesson must teach the new concept while preserving the verified A01
  baseline.
- Action: add English normative and Vietnamese explanatory learning guides;
  maintain README, status, plan, checklist, and this transition guide; run the
  focused and inherited suites separately.
- Files Changed: V00_L01 documentation only in addition to Steps 7 and 8.
- Verification: inherited regression `446/446 PASS`; full suite `464/464
  PASS`; clean build PASS (`BUILD SUCCESSFUL in 29s`, seven actionable tasks
  executed); zero test failures, errors, or skips.
- Expected Result: an evidence-backed V00_L01 ready for final architecture and
  closure review.

## Step 10 - Reconcile the Exact Changed-File Report

- Step: 10
- Objective: explain every entry behind the implementation tool's 20-file
  report and exclude generated or scratch content from publication.
- Why: a frozen lesson must have a precise, reviewable delta with no accidental
  inherited, configuration, vendor, asset, IDE, or governance change.
- Action: classify one intended production file, one intended test file, seven
  intended V00_L01 documentation files, and eleven temporary PDF text-
  extraction files that were removed after the mandatory governance read.
  Audit `build/`, `.gradle/`, `bin/`, `.wpilib`, `.vscode`, vendordeps,
  PathPlanner deploy assets, Gradle files, and every inherited source/test file.
- Files Changed: documentation reconciliation only; no source, test,
  configuration, vendor, deploy, or frozen file changed during closure.
- Verification: the nine-file publishable delta is exact; all eleven scratch
  files and `tmp/` are absent; generated/IDE folders are ignored; all inherited
  source/test/configuration/vendor/deploy files are byte-identical to frozen
  A01_L09; no unexpected file exists.
- Expected Result: changed-file audit PASS with no closure blocker.

## Step 11 - Close and Freeze V00_L01

- Step: 11
- Objective: record the final verified lesson state and inheritance boundary.
- Why: only a fully implemented, verified, documented, architecture-reviewed
  lesson may become the frozen source for its successor.
- Action: record Codex compile/build/test evidence, user-verified WPILib VS
  Code build PASS, Simulation/Driver Station/Real Robot as NOT APPLICABLE for
  pure L01 scope, final architecture PASS, and transition documentation PASS.
- Files Changed: V00_L01 README, status, plan, checklist, transition guide, and
  English/Vietnamese learning guides only.
- Verification: focused tests 18/18 PASS; inherited regression 446/446 PASS;
  full suite 464/464 PASS; clean build PASS (`BUILD SUCCESSFUL in 29s`); user
  WPILib VS Code Build Robot Code PASS (`BUILD SUCCESSFUL`).
- Expected Result: V00_L01 is `COMPLETE / FROZEN / READ-ONLY`.

## Frozen Inheritance Boundary

V00_L01 is the frozen inheritance source for
`V00_L02_AprilTagFieldLayoutContract`. V00_L02 remains
`NOT CREATED / NOT STARTED` and requires a separate activation workflow. Do not
add physical extrinsic values, camera/vendor code, target observations, field
layout, estimation, quality, latency, simulation, real integration, or fusion
to frozen V00_L01.
