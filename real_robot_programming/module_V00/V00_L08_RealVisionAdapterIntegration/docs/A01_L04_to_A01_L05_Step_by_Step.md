# A01_L04 to A01_L05 - Step-by-Step Transition Guide

## Guide State

- Previous lesson: A01_L04_FieldAndAllianceTransformContract - COMPLETE / FROZEN / READ-ONLY
- Current lesson: A01_L05_HolonomicTrajectoryFollowing - COMPLETE / FROZEN / READ-ONLY
- Guide state: FINAL / PASS.
- Git commit and push: user-owned; NOT TESTED.

## Step 1 - Inherit Frozen L04

- Objective: begin L05 from the exact frozen field/alliance-transform predecessor.
- Why: L05 must use L04's canonical Blue-origin frame, explicit field variant, and exactly-one-transform rule without changing them.
- Action: copy L04 into the authorized L05 directory and remove generated artifacts.
- Files Changed: inherited L05 project only.
- Verification: user supplied that L05 `src` is byte-identical to frozen L04 and the inherited baseline build is BUILD SUCCESSFUL.
- Expected Result: L05 starts with the established localization, readiness, trajectory, and transform contracts intact.

## Step 2 - Audit and Lock the Follower Boundary

- Objective: isolate the first trajectory-motion concept.
- Why: L05 follows a sampled trajectory; it does not introduce vendor path assets, AutoBuilder, replanning, or vision.
- Action: complete the Architecture Audit and Design-Lock Blocker Resolution.
- Files Changed: none.
- Verification: Architecture Audit PASS; Design-Lock Blocker Resolution PASS.
- Expected Result: WPILib `HolonomicDriveController` produces bounded robot-relative `ChassisSpeeds` for the existing Swerve subsystem.

## Step 3 - Lock Start-Pose Provenance

- Objective: ensure a transformed Red or Blue trajectory begins from the exact estimator pose accepted while Disabled.
- Why: a boolean-only readiness token cannot prove which alliance frame or field dimensions produced the reset pose.
- Action: lock a minimal `AutonomousStartContext` containing `FieldVariant`, definite `Alliance`, and execution-start `Pose2d`, plus a Disabled-only alliance-aware reset/readiness command.
- Files Changed: none during activation; implementation is deferred.
- Verification: unknown alliance is defined to yield no context and no autonomous motion; consumed context must match the execution trajectory initial pose before following begins.
- Expected Result: exactly-one L04 transform ownership is preserved and start-pose mismatch fails closed.

## Step 4 - Activate L05

- Objective: establish L05 as the sole editable lesson before implementation.
- Why: governance permits changes only in one `IN_PROGRESS / NOT FROZEN` lesson while L01-L04 remain frozen.
- Action: normalize repository and L05 governance documentation; create this guide.
- Files Changed: repository README; L05 README, LESSON_STATUS, LESSON_PLAN, LESSON_CHECKLIST, and this guide.
- Verification: repository-wide lesson-status scan identifies only L05 as active; L04 remains COMPLETE / FROZEN / READ-ONLY; L05 source remains byte-identical to L04.
- Expected Result: implementation may proceed only after a separate authorized implementation request.

## Step 5 - Implement Alliance-Aware Start Provenance

- Objective: bind every accepted autonomous reset to one explicit execution frame.
- Why: the follower must not combine an alliance-transformed trajectory with stale or ambiguous starting-pose readiness.
- Action: add immutable `AutonomousStartContext` and the Disabled-only `AllianceAwareAutonomousStartPoseResetCommand`; update the dashboard composition without changing its operator label.
- Files Changed: `src/main/java/frc/robot/commands/AutonomousStartContext.java`, `AllianceAwareAutonomousStartPoseResetCommand.java`, `KnownFieldPoseResetDashboard.java`, `RobotContainer.java`, and focused tests.
- Verification: unknown alliance, Enabled mode, rejected reset, and consumed context produce no readiness; accepted Blue and Red contexts preserve their exact execution start poses once.
- Expected Result: reset provenance is explicit, one-shot, alliance-aware, and fail-closed.

## Step 6 - Implement the Bounded Holonomic Follower

- Objective: follow the existing L03 learning trajectory after exactly one L04 transform.
- Why: L05 introduces trajectory following without changing subsystem, IO, telemetry, hardware, or architecture ownership.
- Action: add `HolonomicTrajectoryFollowingCommand`, L05 constants, and RobotContainer composition using WPILib `HolonomicDriveController`, fixed holonomic heading, bounded robot-relative output, tolerance completion, timeout, and centralized stop.
- Files Changed: `src/main/java/frc/robot/commands/HolonomicTrajectoryFollowingCommand.java`, `Constants.java`, `RobotContainer.java`, and focused tests.
- Verification: command validation, scheduler safety, both-alliance transform composition, Simulation convergence, immediate Disable stop, and no-restart behavior are covered by deterministic tests and user runtime evidence.
- Expected Result: one safe, finite, alliance-correct learning trajectory executes through the inherited swerve command boundary.

## Step 7 - Verify Java and Regression Gates

- Objective: prove L05 and inherited contracts remain correct before real-robot closure.
- Why: governance requires focused verification, inherited regression, full regression, and a clean build.
- Action: run `compileJava`, `compileTestJava`, focused L05 tests, focused inherited L01-L04 tests, the full test suite, and `clean build` under the installed WPILib Java 17 runtime.
- Files Changed: none.
- Verification: focused L05 32/32 PASS; focused inherited L01-L04 57/57 PASS; full regression 401/401 PASS with zero failures, errors, or skips; final clean build PASS with all seven reported tasks executed.
- Expected Result: no production or inherited regression blocks runtime closure.

## Step 8 - Verify Simulation and Real Robot

- Objective: validate the complete learning trajectory and safety lifecycle in both alliance frames.
- Why: the A01 ADR retains Real Robot HOLD until the user supplies applicable hardware evidence after Simulation.
- Action: the user verified Blue and Red Simulation; USB and radio communication/health; Blue and Red resets; robot-on-blocks actuation; Disable-stop; and expected Blue and Red real-field autonomous trajectories.
- Files Changed: none.
- Verification: every listed runtime case is user-supplied PASS evidence. Earlier interactive evidence also confirms field-heading lifecycle and one-shot/no-automatic-restart behavior. Repeated physical 20-motor-rotation / wheel-rotation tests establish the installed drive ratio as `6.75:1`.
- Expected Result: the evidence-dependent L05 Real Robot HOLD is cleared without bypassing architecture or safety governance.

## Step 9 - Finalize and Freeze L05

- Objective: reconcile the repository record with completed implementation and verification.
- Why: a lesson may become COMPLETE only after implementation, required verification, and the transition guide are final.
- Action: update the repository README and L05 README, status, plan, checklist, and this guide to COMPLETE / FROZEN / READ-ONLY.
- Files Changed: repository `README.md`; L05 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide.
- Verification: L01-L04 remain frozen; L05 is no longer active; no L06 project is started; production Java, tests, Gradle, Frozen Backbone, and Frozen Interface Contract are unchanged during closure.
- Expected Result: A01_L05 is final and read-only. Git commit and push remain user-owned and NOT TESTED.
