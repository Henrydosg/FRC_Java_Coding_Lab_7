# A01_L05 to A01_L06 - Step-by-Step Transition Guide

## Guide State

- Previous lesson: `A01_L05_HolonomicTrajectoryFollowing - COMPLETE / FROZEN / READ-ONLY`
- Current lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration - COMPLETE / FROZEN / READ-ONLY`
- Guide state: `FINAL / PASS`
- This guide records inheritance, preparation, compatibility, design-lock,
  activation, implementation, verification evidence, evidence reconciliation,
  and final closure.
- Git commit and push: user-owned; Codex does not run Git.

Steps 1-10 preserve the historical activation-era record. Their statements
that implementation and the path asset were still pending were accurate at
activation time. Steps 11 onward record the later separately authorized
implementation and verification evidence.

## Step 1 - Copy the Frozen L05 Baseline

- Objective: start L06 from the exact frozen L05 project.
- Why: A01 requires inheritance from the immediately preceding completed lesson
  and protection of all frozen predecessor contracts.
- Action: copy frozen `A01_L05_HolonomicTrajectoryFollowing` into
  `A01_L06_PathPlannerPathAndRuntimeIntegration` as the prepared L06 project.
- Files Changed: prepared L06 project tree; no frozen L05 file was changed.
- Verification: user supplied that the inherited L06 root `src` was byte-identical
  to frozen L05 and the inherited baseline build was `BUILD SUCCESSFUL`.
- Expected Result: L06 begins with the L05 follower, readiness, transform,
  safety, localization, IO, observation, and telemetry contracts intact.

## Step 2 - Create the Authorized L06 Directory

- Objective: establish the exact A01_L06 directory identity.
- Why: lesson identity and directory naming are governed by the approved A01
  ADR and one lesson must remain one independent WPILib project.
- Action: create/use
  `real_robot_programming/module_A01/A01_L06_PathPlannerPathAndRuntimeIntegration/`
  as the copied project boundary.
- Files Changed: prepared L06 project metadata and inherited tree.
- Verification: directory name matches the approved lesson identity; L01-L05
  remain frozen.
- Expected Result: L06 is a separate project boundary suitable for its one new
  PathPlanner concept.

## Step 3 - Discover the Accidental Nested Duplicate

- Objective: identify and isolate the reported nested L05 duplicate before
  activation.
- Why: a nested project could create a second lesson/build boundary and violate
  the fixed one-lesson/one-project structure.
- Action: inspect the prepared L06 tree and locate the accidental nested
  `A01_L05_HolonomicTrajectoryFollowing` directory.
- Files Changed: none during discovery.
- Verification: the nested duplicate was found inside the prepared L06 copy and
  was identified as preparation residue, not an authorized L06 source boundary.
- Expected Result: only the intended L06 project remains as the lesson root.

## Step 4 - Perform Targeted Preparation Cleanup

- Objective: remove only preparation residue from L06.
- Why: generated build state and the accidental nested project must not be
  treated as lesson source, governance, or a second project.
- Action: remove the accidental nested duplicate and generated preparation
  artifacts (`build/`, `.gradle/`, and generated `bin` state) from L06 during
  the prior preparation phase.
- Files Changed: prepared L06 cleanup only; no frozen L05 files were changed.
- Verification: user supplied preparation cleanup PASS; the nested duplicate and
  generated preparation artifacts were absent before activation.
- Expected Result: L06 contains one project and only intentional inherited
  source, test, deploy, vendordep, and documentation content.

## Step 5 - Verify the Inherited Baseline

- Objective: prove the prepared L06 project builds before adding the new concept.
- Why: the frozen workflow requires a successful inherited baseline before
  implementation work.
- Action: run the inherited L06 baseline build in the user's environment.
- Files Changed: none.
- Verification: user supplied `BUILD SUCCESSFUL`.
- Expected Result: any later L06 failure is distinguishable from inheritance
  residue.

## Step 6 - Correct the Java Compatibility Environment

- Objective: resolve the compatibility environment identified during the
  PathPlanner gate.
- Why: the project and PathPlannerLib target Java 17; the earlier Java-8
  environment was not an acceptable compatibility proof.
- Action: rerun the compatibility gate under OpenJDK `17.0.16`; no production
  Java source change was made for this correction.
- Files Changed: user build environment only; no repository source change.
- Verification: user supplied OpenJDK `17.0.16` and successful dependency/build
  results.
- Expected Result: compatibility evidence reflects the repository's Java 17
  contract rather than the obsolete Java-8 environment.

## Step 7 - Preserve and Prove PathPlannerLib 2026.1.2 Compatibility

- Objective: establish the dependency/API entry gate without implementing L06.
- Why: the A01 ADR requires exact dependency and build compatibility before
  PathPlanner implementation.
- Action: preserve the official L06 `vendordeps/PathplannerLib.json` at version
  `2026.1.2` and resolve the dependency in the prepared project.
- Files Changed: the vendordep was established during the prior authorized
  compatibility gate; activation does not replace or upgrade it.
- Verification: user supplied vendordep detection PASS, clean PASS,
  `compileJava` PASS, `compileTestJava` PASS, tests PASS, and `BUILD SUCCESSFUL`.
- Expected Result: PathPlanner classes are available for a later separately
  authorized implementation; no PathPlanner behavior is present yet.

## Step 8 - Pass the Final Design-Lock

- Objective: make the L06 architecture deterministic before activation.
- Why: implementation requires a locked path, adapter boundary, transform
  owner, RobotConfig classification, safety behavior, and exclusions.
- Action: approve the Final Design-Lock for one canonical Blue-frame one-meter
  path, A01/L04 exactly-once transform ownership, a narrow adapter into the
  unchanged L05 follower, and fail-closed runtime behavior.
- Files Changed: none during the design-lock audit.
- Verification: Final Design-Lock `PASS`; activation readiness `YES`; readiness
  for implementation remains `NO` until separate authorization.
- Expected Result: activation can establish governance without silently
  authorizing implementation or real-robot action.

## Step 9 - Activate L06

- Objective: establish L06 as the single active editable A01 lesson.
- Why: only the lesson with `Status = IN_PROGRESS` may receive the next
  authorized implementation change; L01-L05 remain frozen.
- Action: update L06 identity, status, plan, checklist, README, this guide, and
  the repository A01 roadmap state.
- Files Changed: repository `README.md`; L06 `README.md`, `LESSON_STATUS.md`,
  `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide.
- Verification: activation audit must find exactly one active A01 lesson, L06;
  frozen L05 source/docs remain unchanged; no Java, test, path asset, or runtime
  implementation is introduced.
- Expected Result: `A01_L06` is `IN_PROGRESS`; L07-L09 remain authorized but
  not started; Real Robot remains `HOLD`.

## Step 10 - Next Authorized Boundary

- Objective: define what may happen after activation.
- Why: activation and implementation are separate governance steps.
- Action: wait for separate Architect authorization before creating the path
  asset or modifying Java/tests/runtime wiring.
- Files Changed: none.
- Verification: activation-only audit passes with no PathPlanner implementation
  references in L06 source/test files and no `.path` asset.
- Expected Result: L06 is ready for a separately authorized implementation task,
  but implementation has not begun.

## Step 11 - Authorize the Locked Minimum Implementation

- Objective: move from activation into the already approved implementation
  boundary without expanding L06 scope.
- Why: activation and implementation are separate governance gates.
- Action: receive separate authorization for the locked PathPlanner adapter,
  RobotContainer composition, Constants inputs, focused tests, and one path
  asset.
- Files Changed: authorized L06 production/test/deploy delta only; no frozen
  L01-L05 files were changed.
- Verification: user supplied the current implementation state and retained the
  approved Final Design-Lock, exactly-once transform ownership, and provisional
  RobotConfig classifications.
- Expected Result: L06 implementation proceeds only within the approved narrow
  PathPlanner-to-L05 boundary.

## Step 12 - Implement and Compile the L06 Boundary

- Objective: load the known PathPlanner asset and adapt its validated trajectory
  data into the unchanged L05 follower.
- Why: this is the single new architectural concept authorized for L06.
- Action: add the narrow adapter and composition-root wiring, preserve the
  centralized stop/readiness contracts, and add the locked path asset.
- Files Changed: L06 `Constants.java`, `RobotContainer.java`,
  `commands/PathPlannerTrajectoryAdapter.java`, the two focused L06 test files,
  and `src/main/deploy/pathplanner/paths/A01_L06_OneMeter_Forward.path`.
- Verification: user supplied `compileJava: PASS` and `compileTestJava: PASS`
  under Java 17.
- Expected Result: the current L06 source compiles without modifying frozen
  predecessor source, the L05 follower, SwerveSubsystem, IO, or telemetry.

## Step 13 - Complete Focused and Regression Verification

- Objective: verify the adapter, integration lifecycle, inherited contracts, and
  clean build.
- Why: implementation cannot be treated as verified from compilation alone.
- Action: execute the focused L06 tests, inherited direct regression set, full
  test suite, and clean build.
- Files Changed: test-only sequencing correction was limited to
  `RobotContainerPathPlannerIntegrationTest.java`; no production correction was
  required by the supplied failures.
- Verification: focused L06 tests PASS `18/18`; the user supplied PASS for
  `RobotContainerAutonomousModeSchedulingTest`,
  `AllianceAwareAutonomousStartPoseResetCommandTest`,
  `HolonomicTrajectoryFollowingCommandTest`,
  `SwerveSimulationIntegrationTest`, `FieldAllianceTransformTest`, the full
  test suite, and the clean build.
- Expected Result: no production lifecycle regression and no frozen L01-L05
  regression are established.

## Step 14 - Verify Blue and Alliance-Transformed Simulation

- Objective: verify the deterministic path and exactly-once alliance transform
  in Simulation with Glass telemetry.
- Why: Simulation-before-real-robot verification is mandatory and the real
  robot gate remains separate.
- Action: run the Blue canonical path and the alliance-transformed opposite-
  direction path; verify Known Starting Pose reset in Glass.
- Files Changed: none.
- Verification: user supplied approximately `+1.0 m` Blue motion,
  approximately `1.0 m` alliance-transformed opposite-direction motion, valid
  EstimatedPose, and successful Glass reset verification.
- Expected Result: both Simulation paths satisfy the locked L06 learning
  behavior without claiming real-robot autonomous success.

## Step 15 - Close the CAN Hardware Blocker

- Objective: record the user-supplied closure of the previously identified CAN
  hardware blocker.
- Why: the blocker was external to the L06 software boundary and required
  physical repair/replacement.
- Action: user completed the physical repair/replacement and supplied closure.
- Files Changed: none.
- Verification: CAN hardware blocker is recorded as `CLOSED` based only on the
  user-supplied evidence.
- Expected Result: CAN hardware readiness is no longer the documented blocker;
  this does not constitute L06 real-robot autonomous PASS.

## Step 16 - Reconcile Real-Robot Execution Evidence (Earlier Closure State)

- Objective: distinguish completed physical execution from later calibration
  re-verification.
- Why: reporting L06 as never physically tested would discard user-owned
  evidence, while calling the old run post-recalibration verification would
  overstate it.
- Action: record that the one-meter PathPlanner autonomous was physically run
  on both Blue and Red before the latest Swerve zero-offset recalibration, and
  separately record the new hardware rerun as deferred.
- Files Changed: none.
- Verification: user confirms both physical runs and reported one-meter
  behavior working; no centimeter-level physical accuracy is inferred.
- Expected Result at that earlier closure point: pre-recalibration execution was
  preserved as evidence and post-recalibration real-robot reverification was
  `DEFERRED / NOT YET PERFORMED`, not failed. Step 19 supersedes that temporary
  evidence boundary with the later user-supplied post-recalibration executions.

## Step 17 - Verify the Current Swerve Calibration Boundary

- Objective: confirm that the final L06 software consistently represents the
  latest physical calibration.
- Why: offsets are hardware configuration authority even though they do not
  change the PathPlanner or frozen architecture.
- Action: inspect `Constants.java`, the hardware configuration contract test,
  the CTRE configuration test, and the CTRE implementation's use of the shared
  constants.
- Files Changed: none during this closure audit.
- Verification: FL `+0.068603515625`, FR `+0.014404296875`,
  BL `+0.46240234375`, and BR `-0.057373046875` rotations agree across current
  production and direct fixtures; drive ratio remains exactly `6.75:1`.
  Existing post-recalibration Swerve configuration results record `41/41`
  tests PASS with zero failures, and the user supplied post-recalibration
  Simulation PASS.
- Expected Result: the current software configuration is internally
  consistent without changing any other Swerve value.

## Step 18 - Finalize Documentation and Freeze L06

- Objective: close the lesson without erasing evidence boundaries or creating
  L07.
- Why: implementation, build/test evidence, Simulation, explicit real-robot
  status, known issues, and this final transition guide satisfy the governed
  closure record.
- Action: reconcile L06 status, plan, checklist, README, learning-guide status
  notes, and repository roadmap state; preserve the L07 pre-activation record
  as planning information only.
- Files Changed: documentation only; no production Java, tests, PathPlanner
  assets, frozen L01-L05 lesson, or governance document.
- Verification: architecture review, focused L06 tests, inherited regression,
  full tests, clean build, Simulation, current Swerve configuration evidence,
  evidence reconciliation, and documentation audit are PASS. Git remains
  user-owned and was not run.
- Expected Result: A01_L06 is `COMPLETE / FROZEN / READ-ONLY`; the historical
  closure record remains intact, and no L07 implementation is introduced by
  this transition guide.

## Step 19 - Reconcile Final Post-Recalibration Evidence

- Objective: record the user's final Blue and Red real-robot evidence without
  converting an observed endpoint behavior into an unsupported precision claim.
- Why: the latest zero-offset recalibration has now been exercised physically,
  while endpoint measurement and dynamics characterization remain separate
  future work.
- Action: update only the L06 documentation records with the user-supplied
  post-recalibration Blue and Red one-meter autonomous PASS, the observed Blue
  overshoot followed by a small reverse correction and settling, and the
  deferred tuning boundary.
- Files Changed: L06 Markdown documentation only; no production Java, tests,
  PathPlanner assets, SwerveSubsystem, IO, CTRE configuration, or frozen lesson
  source.
- Verification: Blue and Red post-recalibration physical execution are PASS;
  exact endpoint accuracy is not formally measured or claimed; RobotConfig mass
  `45.0 kg`, MOI `5.0 kg*m^2`, maximum drive velocity `4.0 m/s`, and wheel COF
  `1.0` remain provisional; no single provisional value is proven to cause the
  observed behavior; final PID/feedforward and physical-model tuning are
  deferred.
- Expected Result: L06 remains `COMPLETE / FROZEN / READ-ONLY` with a complete
  evidence boundary, and the separately active L07 remains implementation-free.
