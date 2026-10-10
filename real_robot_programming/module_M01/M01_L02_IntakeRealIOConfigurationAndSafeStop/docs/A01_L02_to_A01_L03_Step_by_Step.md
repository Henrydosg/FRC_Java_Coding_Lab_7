# A01_L02 to A01_L03 - Step-by-Step Transition Guide

## Guide Status

- Previous lesson: A01_L02_PoseTargetedAutonomousMotion - COMPLETE / FROZEN / READ-ONLY
- Current lesson: A01_L03_TrajectoryGenerationAndSamplingFundamentals - COMPLETE / FROZEN / READ-ONLY
- Guide state: FINAL / PASS
- Git commit and push: user-owned; NOT TESTED

## Step 1 - Inherit the Frozen L02 Baseline

- Objective: begin L03 from the immediate completed predecessor.
- Why: retain validated localization, safety, readiness, and autonomous
  lifecycle contracts.
- Action: preserve L02 read-only and use its source/test/deploy tree as L03's
  baseline.
- Files Changed: no L02 files.
- Verification: pre-activation comparison found L03 source/test/deploy
  byte-identical to L02.
- Expected Result: L03 begins with no accidental architecture delta.

## Step 2 - Correct the Directory Identity

- Objective: align the project directory with the ADR lesson identity.
- Why: the authorized lesson name must be exact before activation.
- Action: verify A01_L03_TrajectoryGenerationAndSamplingFundamentals exists
  and the previous directory spelling does not.
- Files Changed: identity correction completed before activation.
- Verification: user-supplied filesystem verification PASS.
- Expected Result: the directory matches A01_L03 - Trajectory Generation and
  Sampling Fundamentals.

## Step 3 - Establish the Inherited Baseline Build

- Objective: verify the inherited project before adding L03's concept.
- Why: later behavior must be attributable to the L03 delta.
- Action: build the renamed inherited project.
- Files Changed: none.
- Verification: user-supplied result: BUILD SUCCESSFUL.
- Expected Result: a known-good L02 baseline.

## Step 4 - Complete Architecture Audit and Design Lock

- Objective: isolate generation and sampling from future following.
- Why: a time-parameterized sample must be understandable before it controls a
  drivetrain.
- Action: lock only Start/Goal Pose plus interior Translation2d waypoint,
  TrajectoryGenerator, Trajectory, trajectory.sample(t), and Trajectory.State.
- Files Changed: documentation only.
- Verification: Architecture Audit PASS; Design Lock PASS.
- Expected Result: no controller, ChassisSpeeds, SwerveSubsystem output, or
  robot motion is introduced.

## Step 5 - Activate L03

- Objective: make L03 the controlled editable lesson.
- Why: governance permits changes only inside the active lesson.
- Action: normalize L03 documentation and record the active identity.
- Files Changed: L03 governance documentation and repository README.
- Verification: activation PASS and L03 was the sole IN_PROGRESS lesson.
- Expected Result: L02 remained frozen while L03 received one authorized
  concept.

## Step 6 - Add the Trajectory Generation and Sampling Delta

- Objective: implement one native WPILib trajectory data factory.
- Why: expose constraints, time ordering, endpoint states, and deterministic
  sampling without beginning trajectory following.
- Action: add TrajectoryGenerationConstants to Constants; add
  LearningTrajectoryFactory; add LearningTrajectoryFactoryTest.
- Files Changed: Constants.java; util/LearningTrajectoryFactory.java;
  util/LearningTrajectoryFactoryTest.java.
- Verification: final source comparison found exactly those three changes from
  frozen L02.
- Expected Result: a fresh validated Trajectory is returned directly and owns
  trajectory.sample(t).

## Step 7 - Verify Locally Without Actuation

- Objective: prove the pure data contract without commanding a robot.
- Why: L03 deliberately has no autonomous dispatch, drivetrain output, or
  physical motion.
- Action: run compileTestJava, LearningTrajectoryFactoryTest, full regression,
  and clean build in the user's local WPILib Java 17 environment.
- Files Changed: none.
- Verification: user supplied PASS for each gate and the final terminal result
  PASS - A01_L03 FULL LOCAL VERIFICATION COMPLETE. The clean build reported
  BUILD SUCCESSFUL in 44s with 7 actionable tasks executed.
- Expected Result: finite positive-duration trajectory data, endpoint samples,
  finite/monotonic states, constraint compliance, native sample clamping, and
  repeatability are verified. No test count is claimed.

## Step 8 - Finalize and Freeze L03

- Objective: record evidence and preserve the completed lesson snapshot.
- Why: the next authorized lesson must inherit an exact frozen predecessor.
- Action: finalize L03 README, status, plan, checklist, transition guide, and
  repository active-lesson state; preserve all Java/test files.
- Files Changed: L03 documentation and repository README only.
- Verification: L03 is COMPLETE / FROZEN / READ-ONLY; no lesson remains
  IN_PROGRESS; A01_L04 was not created or started.
- Expected Result: L03 is frozen with Real Robot HOLD by A01 ADR, while Git
  commit and push remain user-owned and NOT TESTED.

## Scope Preserved for Later Lessons

L03 does not add trajectory following, Ramsete, holonomic control,
ChassisSpeeds, drivetrain motion, PathPlanner, AutoBuilder, alliance
transforms, vision, replanning, NamedCommands, event markers, mechanism
events, telemetry, IO, hardware, drivetrain tuning, or Frozen
Backbone/interface changes. L04 and later remain separate authorized lessons.
