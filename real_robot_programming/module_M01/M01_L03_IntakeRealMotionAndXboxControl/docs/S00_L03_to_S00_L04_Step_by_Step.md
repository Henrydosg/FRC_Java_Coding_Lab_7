# S00_L03 to S00_L04 Step by Step

## Step 1 - Copy the completed S00_L03 lesson

- Objective: Create the new lesson from the completed S00_L03 project.
- Why: The frozen workflow requires each lesson to inherit a known-good project.
- Action: Copy the S00_L03 project into the S00_L04 lesson directory.
- Files Changed: New S00_L04 project files are created by the copy.
- Verification: Confirm the copied project contains its source, build files, vendordeps, and lesson status.
- Expected Result: S00_L04 starts as an independent copy of S00_L03.

## Step 2 - Rename the lesson

- Objective: Apply the S00_L04 lesson name consistently.
- Why: The lesson name identifies the active module and its documentation.
- Action: Rename the copied project and lesson metadata to S00_L04_Swerve_Subsystem_Foundation.
- Files Changed: S00_L04 project paths and lesson metadata.
- Verification: Confirm the project directory and lesson fields use the S00_L04 name.
- Expected Result: No S00_L03 lesson identity remains in the active lesson metadata.

## Step 3 - Run the baseline build

- Objective: Establish inherited build evidence before lesson changes.
- Why: A baseline separates inherited issues from implementation issues.
- Action: Build the copied project before implementing S00_L04.
- Files Changed: None requested by this step.
- Verification: User verified that the inherited S00_L04 project built successfully before Java changes.
- Expected Result: Baseline Build is recorded as PASS.

## Step 4 - Initialize the documentation framework

- Objective: Initialize the four v2.1 lesson documents.
- Why: The framework keeps overview, plan, evidence, and execution tracking separate.
- Action: Prepare README.md, LESSON_PLAN.md, LESSON_STATUS.md, and LESSON_CHECKLIST.md for S00_L04.
- Files Changed: The four S00_L04 lesson documents.
- Verification: Confirm metadata, fixed status vocabulary, evidence sections, and checklist workflow are present.
- Expected Result: The lesson has a consistent v2.1 documentation foundation.

## Step 5 - Audit the inherited architecture

- Objective: Inspect the frozen backbone, IO contracts, and current source before implementation.
- Why: The audit prevents scope expansion and architecture drift.
- Action: Review governance documents, S00_L03, current Java source, and the S00_L04 plan.
- Files Changed: None during the audit.
- Verification: Confirm the single concept is SwerveSubsystem ownership and periodic IO refresh.
- Expected Result: The implementation boundary and deferred concerns are explicit.

## Step 6 - Lock the architecture

- Objective: Approve the exact S00_L04 architecture before coding.
- Why: Formal approval protects the frozen interfaces and dependency direction.
- Action: Lock the constructor dependency-injection contract, owned input snapshots, periodic refresh, and stop delegation in LESSON_PLAN.md.
- Files Changed: LESSON_PLAN.md records the approved decision.
- Verification: Confirm RobotContainer wiring, Observation, telemetry, simulation/noop, kinematics, odometry, commands, controls, PID, offsets, inversions, and configuration remain deferred.
- Expected Result: Architecture Review is PASS and implementation has one bounded concept.

## Step 7 - Implement the subsystem foundation

- Objective: Implement only the approved SwerveSubsystem foundation.
- Why: The subsystem must own IO refresh without adding control or hardware policy.
- Action: Add SwerveSubsystem with four module IO dependencies, one gyro dependency, five input snapshots, periodic refresh, and safe stop delegation.
- Files Changed: src/main/java/frc/robot/subsystems/SwerveSubsystem.java.
- Verification: Inspect the constructor, fields, periodic() calls, stop() delegation, and absence of vendor imports or extra behavior.
- Expected Result: Implementation is complete and architecture compliant.

## Step 8 - Verify the implementation build

- Objective: Verify the lesson builds after implementation.
- Why: Compilation confirms the injected contracts and subsystem lifecycle integrate correctly.
- Action: Run the clean Gradle build for S00_L04.
- Files Changed: Build outputs only; no source changes are authorized by this guide.
- Verification: User verified Build PASS.
- Expected Result: Build is recorded as PASS.

## Step 9 - Record deferred runtime verification

- Objective: Record runtime checks according to the approved lesson scope.
- Why: S00_L04 does not compose or schedule the subsystem and has no separately reviewed simulation implementation.
- Action: Keep Simulation and Driver Station / Glass as NOT_APPLICABLE with their existing rationales. Set Real Robot to NOT_APPLICABLE because SwerveSubsystem is not composed or scheduled in this lesson, so no runtime robot path exists.
- Files Changed: LESSON_STATUS.md and LESSON_CHECKLIST.md evidence entries.
- Verification: Confirm all three applicability decisions and rationales are present.
- Expected Result: Deferred runtime verification is explicit and does not imply unperformed testing.

## Step 10 - Complete documentation and perform final Git steps

- Objective: Finish lesson documentation and identify the remaining release actions.
- Why: A lesson is not complete until its evidence is committed, pushed, and frozen.
- Action: Create this transition guide, set Documentation and Transition Guide to PASS, then later commit, push, and freeze the lesson.
- Files Changed: This guide, LESSON_STATUS.md, and LESSON_CHECKLIST.md.
- Verification: Documentation and Transition Guide are PASS. Commit, Push, and Freeze remain NOT_TESTED because this task does not authorize Git actions; lesson Status remains IN_PROGRESS.
- Expected Result: The documentation is complete, with Git commit, push, and freeze explicitly remaining.
