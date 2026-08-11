# S00_L21 to S00_L22 Step-by-Step Transition Guide

## Status

`FINAL / PASS`

## Lesson Identity

- Source: `S00_L21_FirstFloorDriveValidation` - `COMPLETE / FROZEN / READ-ONLY`
- Current: `S00_L22_FieldRelativeDrive` - `IN_PROGRESS`
- Next: `S00_L23_OdometryAndPoseVisualization` - `OUT OF SCOPE`
- Objective: add and validate field-relative teleop conversion while preserving the existing robot-relative drivetrain/output pipeline.
- Architecture audit: `CONDITIONALLY APPROVED`; the user-supplied Disabled Pigeon2 hardware gate subsequently cleared the condition.

## Architecture Delta

```text
raw gyro yaw
-> captured software field reference
-> wrapped field heading
-> robot-relative ChassisSpeeds
-> existing frozen output pipeline
```

## Historical Evidence Reconciliation

- The initial L22 field-relative implementation converted raw Pigeon yaw directly through WPILib while preserving the inherited robot-relative output pipeline.
- Early forward-drive behavior could appear diagonal because raw yaw had no explicit field-zero reference. This was a field-origin definition defect, not permission to change module configuration or drivetrain tuning.
- The observed raw yaw of `+129.207458` degrees with HEAD aligned to field +X is runtime evidence only and is never a constant.
- The later initial no-drive observation occurred when no successful Disabled field-reference capture had armed a valid field-relative request; fail-closed behavior was intentional.
- The corrected procedure is Disabled -> align HEAD to field +X -> press/release Back/Button 7 once -> enable -> drive. The final user floor matrix passed; BL steer drift was `INTERMITTENT / NOT REPRODUCED`.

## Completed Inheritance and Governance Steps

### Step 1 - Confirm frozen L21 source

- Step: 1
- Objective: establish the authoritative source lesson.
- Why: inheritance must begin from a completed frozen snapshot.
- Action: confirmed `S00_L21_FirstFloorDriveValidation` as `COMPLETE / FROZEN / READ-ONLY`.
- Files Changed: none.
- Verification: PASS - repository status and user-supplied lesson identity agree.
- Expected Result: L21 remains unchanged and is the sole source for L22.

### Step 2 - Confirm L21 publication

- Step: 2
- Objective: record source-lesson publication before beginning L22.
- Why: the source lesson must be durably closed before the next lesson advances.
- Action: recorded the user-supplied result that L21 was pushed to `origin/main`.
- Files Changed: L22 documentation only.
- Verification: PASS - user supplied.
- Expected Result: L21 publication is recorded without Codex running Git.

### Step 3 - Copy L21 to L22

- Step: 3
- Objective: create the inherited L22 project.
- Why: repository lifecycle forbids recreating a lesson from scratch.
- Action: copied the frozen L21 project into `S00_L22_FieldRelativeDrive`.
- Files Changed: new L22 lesson directory only.
- Verification: PASS - user supplied; inherited source and tests are present.
- Expected Result: L22 begins with the complete L21 production and test baseline.

### Step 4 - Remove copied build artifacts

- Step: 4
- Objective: remove generated state inherited from L21.
- Why: L22 verification must use its own generated outputs.
- Action: removed copied `build/` and `.gradle/` artifacts.
- Files Changed: generated artifacts only; no Java or tests.
- Verification: PASS - user supplied.
- Expected Result: L22 starts from a clean build workspace.

### Step 5 - Baseline clean build

- Step: 5
- Objective: prove the copied project builds before L22 changes.
- Why: inheritance defects must be separated from field-relative implementation defects.
- Action: user ran the L22 baseline clean build.
- Files Changed: none claimed.
- Verification: PASS - `BUILD SUCCESSFUL in 38s`; 7/7 tasks executed.
- Expected Result: the inherited L21 source is a valid L22 baseline.

### Step 6 - Inherited regression

- Step: 6
- Objective: confirm existing behavior remains intact in the copied lesson.
- Why: L22 must preserve the verified robot-relative drivetrain/output pipeline.
- Action: user ran the inherited regression suite.
- Files Changed: none claimed.
- Verification: PASS - user supplied.
- Expected Result: the inherited production path remains regression-valid before the L22 delta.

### Step 7 - Architecture audit

- Step: 7
- Objective: identify the smallest correct L22 architecture delta.
- Why: implementation must preserve the Frozen Backbone and avoid L23 scope.
- Action: audited driver input, command, subsystem, gyro IO, Observation, output pipeline, module IO, focused tests, coordinate conventions, safety, simulation, and real-robot verification requirements.
- Files Changed: none.
- Verification: `CONDITIONALLY APPROVED`.
- Expected Result: the approved delta remains field-relative request -> validated heading -> robot-relative `ChassisSpeeds` -> existing output pipeline.

### Step 8 - Initialize L22 governance

- Step: 8
- Objective: make the copied lesson the active editable L22 project.
- Why: implementation is forbidden while copied metadata still identifies frozen L21.
- Action: updated L22 status, plan, checklist, README, and created this transition guide.
- Files Changed: `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, `README.md`, and `docs/S00_L21_to_S00_L22_Step_by_Step.md` in L22 only.
- Verification: PASS - L22 is recorded as `IN_PROGRESS`; source, next lesson, objective, architecture delta, hardware gate, and L23 boundary are consistent.
- Expected Result: L22 governance is active while implementation remains blocked by the hardware gate.

## Completed Implementation Steps

### Step 9 - Clear the Disabled Pigeon2 hardware gate

- Step: 9
- Objective: confirm the existing yaw signal is safe for WPILib field-relative conversion.
- Why: field-relative control must not actuate from an unverified heading sign or mounting orientation.
- Action: user verified Pigeon2 CAN 20 while Disabled: initial yaw near zero, CCW positive, CW negative, approximately 90 degrees physical rotation produced approximately 88-93 degrees yaw change, pitch/roll near zero, and no active fault.
- Files Changed: none.
- Verification: PASS - user supplied.
- Expected Result: the architecture audit condition is cleared without changing gyro configuration.

### Step 10 - Replace the default teleop command

- Step: 10
- Objective: express teleop requests in the field frame from one coherent controller sample.
- Why: L22 adds field-relative driver intent without changing input processing or telemetry ownership.
- Action: replaced `RobotRelativeTeleopDriveCommand` with `FieldRelativeTeleopDriveCommand`; preserved 1.0 m/s translation and 1.0 rad/s rotation scaling, published the same immutable observation used for control, and retained fail-safe lifecycle stops.
- Files Changed: `src/main/java/frc/robot/commands/RobotRelativeTeleopDriveCommand.java` removed; `src/main/java/frc/robot/commands/FieldRelativeTeleopDriveCommand.java` added.
- Verification: PASS - focused command tests.
- Expected Result: each execute submits one field-relative request and publishes its exact input observation.

### Step 11 - Add subsystem-owned field-relative conversion

- Step: 11
- Objective: convert validated field-relative requests before the inherited output pipeline.
- Why: the subsystem owns both the gyro snapshot and drivetrain behavior boundary.
- Action: added `acceptFieldRelativeChassisSpeeds()`, retained robot-relative `acceptChassisSpeeds()`, validated connected/configuration-healthy/finite yaw, converted with WPILib `ChassisSpeeds.fromFieldRelativeSpeeds(...)`, and disarmed/stopped on invalid heading without fallback or stale dispatch.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: PASS - focused subsystem and production-path tests.
- Expected Result: only robot-relative speeds enter the existing kinematics/optimization/desaturation/output pipeline.

### Step 12 - Wire the default command at the composition root

- Step: 12
- Objective: install field-relative teleop without adding behavior to `RobotContainer`.
- Why: `RobotContainer` is limited to construction, dependency injection, and default-command selection.
- Action: constructed `FieldRelativeTeleopDriveCommand` with existing dependencies and installed it as the default Swerve command.
- Files Changed: `src/main/java/frc/robot/RobotContainer.java`.
- Verification: PASS - production compilation and command requirement tests.
- Expected Result: normal teleop uses the new command while Test/commissioning commands remain robot-relative.

### Step 13 - Clarify gyro yaw semantics

- Step: 13
- Objective: document the heading sign and validity contract used by conversion.
- Why: the existing public IO fields are sufficient when their semantics are explicit.
- Action: documented continuous yaw, adapter-established zero, CCW-positive sign, and finite/connected/configuration-healthy validity; no public field or adapter behavior changed.
- Files Changed: `src/main/java/frc/robot/io/gyro/GyroIO.java`.
- Verification: PASS - production compilation.
- Expected Result: conversion uses the existing vendor-neutral gyro contract without configuration changes.

### Step 14 - Add focused field-relative verification

- Step: 14
- Objective: verify the new command, conversion, fail-safe, recovery, and final production path.
- Why: the architecture delta requires focused evidence while preserving inherited behavior.
- Action: replaced robot-relative teleop-specific tests with field-relative equivalents and added subsystem conversion/invalid-heading tests for 0, +90, -90, 180 degrees, combined translation/positive omega, module ordering, five invalid-heading cases, stale-output prevention, recovery, and robot-relative independence.
- Files Changed: removed `RobotRelativeTeleopDriveCommandTest.java` and `RobotRelativeTeleopProductionPathTest.java`; added `FieldRelativeTeleopDriveCommandTest.java`, `FieldRelativeTeleopProductionPathTest.java`, and `SwerveSubsystemFieldRelativeTest.java`.
- Verification: PASS - 44/44 focused tests, including field-reference capture, wraparound, Disabled-only capture, recapture disarm, disable/enable persistence, and fresh-request recovery.
- Expected Result: the approved delta and safety contract are directly exercised without `GyroIOSim`.

### Step 15 - Run affected and full regression tests

- Step: 15
- Objective: prove the L22 delta preserves inherited command, commissioning, subsystem, IO, and telemetry behavior.
- Why: focused correctness is insufficient if the frozen robot-relative paths regress.
- Action: Codex ran the affected command/subsystem suite and then the complete L22 Gradle test task with the WPILib Java 17 runtime.
- Files Changed: generated build/test artifacts only.
- Verification: PASS - full regression: 189 tests, 0 failures, 0 errors, 0 skipped; `BUILD SUCCESSFUL in 15s`.
- Expected Result: L22 Java and tests compile and all automated regression tests pass.

### Step 16 - Record the field-heading origin defect

- Step: 16
- Objective: distinguish raw gyro yaw validity from the field's chosen zero heading.
- Why: a valid Pigeon2 yaw can still be nonzero when the robot is physically aligned with field +X.
- Action: recorded the user-supplied Disabled evidence: robot HEAD aligned to field +X while raw yaw was approximately `+129.207458` degrees; the value is runtime evidence only and is not a constant.
- Files Changed: L22 documentation only.
- Verification: PASS - R1 centered-enable behavior passed; R2 field-relative actuation was blocked pending an explicit field reference.
- Expected Result: L22 establishes field zero explicitly instead of assuming raw Pigeon yaw is field heading.

### Step 17 - Implement the software field-heading reference

- Step: 17
- Objective: establish field heading without changing Pigeon2 hardware state or the GyroIO contract.
- Why: the subsystem owns the gyro snapshot and field-relative drivetrain boundary.
- Action: `SwerveSubsystem` now captures the latest connected, configuration-healthy, finite raw yaw only while Disabled; it computes `wrap(rawYaw - capturedRawYawReference)` in `[-180, +180)` and converts through `ChassisSpeeds.fromFieldRelativeSpeeds(...)` before the existing robot-relative pipeline.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: PASS - reference initialization, arbitrary capture, ±90 degree motion, wrap boundaries, invalid heading, stale-output prevention, fresh-request recovery, and disable/enable persistence tests.
- Expected Result: field-relative requests fail closed before capture and after invalid gyro data, while the reference survives normal Disable -> Enable in the same runtime.

### Step 18 - Add the explicit Disabled capture binding

- Step: 18
- Objective: expose the smallest operator action needed to capture field zero.
- Why: capture must be deliberate and must not put field-reference business logic in `RobotContainer`.
- Action: added `CaptureFieldHeadingReferenceCommand`, which requires `SwerveSubsystem`, runs while Disabled, finishes after one capture attempt, and is bound to the unused Xbox Back/View button. The operator aligns robot HEAD to field +X, keeps the robot Disabled, and presses Back/View; capture stops/disarms the drivetrain and the next field-relative command must be a fresh request.
- Files Changed: `src/main/java/frc/robot/commands/CaptureFieldHeadingReferenceCommand.java`; `src/main/java/frc/robot/RobotContainer.java`.
- Verification: PASS - command requirement/lifecycle and accepted/rejected capture tests; binding inspection found no inherited use of Xbox Back/View.
- Expected Result: the composition root only wires the command and button; all reference ownership and safety behavior remain in the subsystem.

### Step 19 - Verify the reference implementation

- Step: 19
- Objective: compile and regress the complete L22 implementation before user-owned field verification.
- Why: automated evidence must be complete before Simulation or real-robot verification is attempted.
- Action: ran focused field-relative/reference tests, affected subsystem/command/commissioning tests, the complete regression, and the Gradle build.
- Files Changed: generated build/test artifacts only.
- Verification: PASS - focused 44/44; full regression 189 tests with 0 failures, 0 errors, 0 skipped; `gradlew.bat ... build` `BUILD SUCCESSFUL in 12s`.
- Expected Result: implementation is automated-test complete; Simulation, Driver Station/Glass, and real-robot R2+ remain user-owned gates.

### Step 20 - Record the field-reference runtime procedure and final floor evidence

- Step: 20
- Objective: document the explicit field-zero workflow and the user-owned final field-relative result.
- Why: raw Pigeon yaw is a valid robot heading signal but is not inherently the field's +X zero.
- Action: while Disabled, align robot HEAD to field +X, press and release Xbox Back / Button 7 once, enable teleop, and drive. The button captures the current raw yaw in software; it does not calibrate or reset Pigeon hardware. Do not recapture during ordinary chassis rotation. Recapture only for intentional field-zero redefinition or reference/gyro validity loss.
- Files Changed: L22 documentation only.
- Verification: PASS - user supplied final field-relative floor matrix across multiple headings, translation, strafe, rotation, combined motion, centered stop, and Disable -> Enable reference persistence.
- Expected Result: the captured software reference establishes field heading zero for the current runtime and survives normal Disable -> Enable.

### Step 21 - Record intermittent BL steer status without changing production behavior

- Step: 21
- Objective: preserve the truthful hardware observation without converting it into an unsupported root-cause claim.
- Why: the BL steer symptom was intermittent and was not reproduced during the final floor matrix.
- Action: record BL drift as `INTERMITTENT / NOT REPRODUCED` and retain it as a watch item only. No production correction, tuning, inversion, offset, module configuration, or telemetry was added.
- Files Changed: L22 documentation only.
- Verification: PASS - user supplied final floor observation; no root cause was established.
- Expected Result: the symptom remains visible as technical debt without altering the approved L22 architecture.

### Step 22 - Complete final clean Java 17 closure build

- Step: 22
- Objective: perform the final clean build and regression required before user Git closure.
- Why: closure evidence must be reproducible in the required WPILib Java 17 environment.
- Action: ran `gradlew.bat clean build` in the L22 project.
- Files Changed: generated build artifacts only.
- Verification: PASS - user supplied final Java 17 clean build `BUILD SUCCESSFUL`; focused tests 44/44; full regression 189 tests, 0 failures, 0 errors, 0 skipped.
- Expected Result: all automated closure gates are reproducibly recorded.

### Step 23 - Record final user verification and close the guide

- Step: 23
- Objective: reconcile all remaining user-owned L22 verification before Git closure.
- Why: the transition guide may be final only after the complete evidence matrix is recorded.
- Action: recorded user-supplied PASS for Simulation/HALSIM, Driver Station/Glass, Disabled Pigeon hardware direction/magnitude, software field-reference capture, final real-robot floor matrix, and Disable -> Enable reference persistence.
- Files Changed: L22 documentation only.
- Verification: PASS - BL steer drift remains `INTERMITTENT / NOT REPRODUCED`; no fix, resolution, or root cause is claimed.
- Expected Result: the guide is `FINAL / PASS`, while L22 remains `IN_PROGRESS / EDITABLE` for user Git closure.

## Closure Status

User supplied Simulation/HALSIM PASS, Driver Station/Glass PASS, final Java 17 clean build PASS, field-reference capture PASS, final floor matrix PASS, and Disable -> Enable persistence PASS. Focused tests are 44/44 PASS and full regression is 189 tests with 0 failures, 0 errors, and 0 skipped. BL steer drift remains `INTERMITTENT / NOT REPRODUCED`; no root cause is claimed. The guide is `FINAL / PASS`. L22 commit and push remain user-owned; Codex did not run Git.

## L23 Boundary

L22 excludes odometry, pose estimation, pose visualization, autonomous behavior, PathPlanner, alliance pose transforms, pose reset, and pose ownership. `S00_L23_OdometryAndPoseVisualization` remains out of scope and has not been started.

## Finalization Boundary

This guide is `FINAL / PASS`. L22 remains `IN_PROGRESS / EDITABLE` until the user performs Git closure.

## Git Boundary

L21 push to `origin/main` is recorded from user-supplied evidence. Codex did not run Git. L22 commit and push remain `NOT TESTED` and user-owned.
