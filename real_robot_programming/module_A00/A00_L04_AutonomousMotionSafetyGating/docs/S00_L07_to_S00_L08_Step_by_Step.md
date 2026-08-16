# S00_L07 to S00_L08: Swerve Module State Foundation

## Step 1 — Copy and rename

- Objective: Create S00_L08 from the completed S00_L07 project.
- Why: Preserve the frozen inheritance workflow and verified runtime foundation.
- Action: Copy S00_L07 and rename the project to S00_L08_Swerve_Module_State_Foundation.
- Files Changed: New S00_L08 project metadata and inherited source.
- Verification: Confirm the new lesson path and identity.
- Expected Result: S00_L08 is an independent working copy.

## Step 2 — Initialization

- Objective: Initialize the Framework v2.1 lesson documents.
- Why: Establish the authoritative evidence record before implementation.
- Action: Set lesson identity, inherited context, reset current evidence, and record the user-verified baseline build.
- Files Changed: README.md, LESSON_PLAN.md, LESSON_STATUS.md, LESSON_CHECKLIST.md.
- Verification: Confirm current lesson is IN_PROGRESS and Baseline Build is PASS.
- Expected Result: Documentation roles and evidence vocabulary are consistent.

## Step 3 — Baseline build

- Objective: Verify the inherited project before Java changes.
- Why: Separate inherited health from S00_L08 implementation results.
- Action: Use the user-verified BUILD SUCCESSFUL baseline.
- Files Changed: LESSON_STATUS.md, LESSON_CHECKLIST.md.
- Verification: Baseline Build = PASS.
- Expected Result: S00_L07 behavior remains the starting point.

## Step 4 — Audit

- Objective: Define the measured module-state boundary.
- Why: Prevent raw IO values from being confused with desired control state.
- Action: Review Document C, the observation contract, SwerveSubsystem, IO inputs, telemetry, and WPILib SwerveModuleState.
- Files Changed: LESSON_PLAN.md.
- Verification: Confirm direct WPILib type, measured-only semantics, FL/FR/BL/BR ordering, and deferred control scope.
- Expected Result: One approved architectural concept.

## Step 5 — Architecture decision

- Objective: Approve the implementation shape.
- Why: Lock units and safety boundaries before coding.
- Action: Approve direct `edu.wpi.first.math.kinematics.SwerveModuleState` usage without a project wrapper.
- Files Changed: LESSON_PLAN.md, LESSON_STATUS.md.
- Verification: Architecture Review = PASS.
- Expected Result: No desired state, optimization, kinematics, or motor output is introduced.

## Step 6 — Direct WPILib SwerveModuleState usage

- Objective: Expose measured states from SwerveSubsystem.
- Why: Provide a standard WPILib representation while retaining measured-only meaning.
- Action: Add `getMeasuredModuleStates()` returning new state objects in FL, FR, BL, BR order.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: Confirm no IOInputs are exposed and existing periodic/stop behavior is unchanged.
- Expected Result: A read-only measured-state API is available.

## Step 7 — Measured-state conversion

- Objective: Convert verified drive readback to wheel speed.
- Why: Establish physical units without guessing unresolved hardware calibration.
- Action: Use `driveVelocityRotationsPerSecond / 7.85 × (2π × wheelRadiusMeters)` with the verified 2.0-inch radius.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: Confirm no steer ratio, offset, inversion, or optimization is applied.
- Expected Result: `speedMetersPerSecond` is derived only from approved constants.

## Step 8 — Uncalibrated angle policy

- Objective: Preserve raw absolute encoder angle semantics.
- Why: Calibration data is unresolved and must not be guessed.
- Action: Construct the angle with `Rotation2d.fromRotations(encoderAbsolutePositionRotations)` and document it as uncalibrated.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: Confirm no CANcoder offset, inversion, or steer-ratio conversion exists.
- Expected Result: Angles are measured raw readback and explicitly UNCALIBRATED.

## Step 9 — User build verification

- Objective: Verify the completed implementation.
- Why: Confirm the project compiles after the architecture change.
- Action: Use the user-verified BUILD SUCCESSFUL result.
- Files Changed: LESSON_STATUS.md, LESSON_CHECKLIST.md.
- Verification: Build = PASS.
- Expected Result: S00_L08 implementation is build-verified.

## Step 10 — Deferred runtime verification

- Objective: Record why runtime checks are not performed.
- Why: Avoid implying behavior that is not composed or scheduled.
- Action: Mark Simulation, Driver Station / Glass, and Real Robot NOT APPLICABLE because no new runtime consumer or motor-control path exists.
- Files Changed: LESSON_STATUS.md, LESSON_CHECKLIST.md.
- Verification: Each NOT_APPLICABLE item has its rationale.
- Expected Result: Runtime scope remains honest and bounded.

## Step 11 — Git workflow

- Objective: Complete delivery after review.
- Why: Preserve repository commit, push, and freeze governance.
- Action: Review documentation and working tree, then commit, push, and freeze only after separate verification.
- Files Changed: Git metadata only when authorized.
- Verification: Commit, Push, and Freeze remain NOT TESTED for this update.
- Expected Result: Remaining delivery steps are explicit and unclaimed.
