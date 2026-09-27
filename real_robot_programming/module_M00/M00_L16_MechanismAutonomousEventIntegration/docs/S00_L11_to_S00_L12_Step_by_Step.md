# S00_L11 to S00_L12 Step by Step

## Step 1 - Confirm the frozen inheritance boundary

- Objective: Confirm S00_L12 inherits the frozen S00_L11 project and only the active lesson is editable.
- Why: The lesson lifecycle requires inheritance from the previous completed lesson and protects frozen source lessons.
- Action: Compare the non-generated S00_L11 and S00_L12 files, confirm the active lesson status, and preserve the existing backbone packages.
- Files Changed: None.
- Verification: PASS; the inherited non-generated S00_L12 files matched S00_L11 before implementation, and the repository reported S00_L12 as untracked.
- Expected Result: S00_L11 remains unchanged and S00_L12 remains the only implementation scope.

## Step 2 - Add the configured wheel-speed limit

- Objective: Provide one named maximum wheel-speed configuration value for desaturation.
- Why: The inherited Constants.java had no legitimate maximum wheel-speed constant, while the output pipeline must use a configured limit.
- Action: Add `kMaxWheelSpeedMetersPerSecond` to `Constants.SwerveConstants` with a value of `4.0`.
- Files Changed: `src/main/java/frc/robot/Constants.java`.
- Verification: PASS by source inspection; the value is positive, named by unit, and remains in Constants.java as the configuration authority. The 4.0 m/s value is provisional software baseline data, not verified hardware capability.
- Expected Result: The default pipeline can use one explicit maximum wheel-speed limit without a magic number in pipeline logic.

## Step 3 - Implement the pure output pipeline

- Objective: Convert chassis speeds and current module angles into final ordered module states.
- Why: S00_L12 combines the three already-approved transformations while preserving ownership boundaries.
- Action: Copy chassis-speed scalars, validate exactly four current angles, perform kinematics through `SwerveKinematics`, optimize each matching module through `SwerveModuleStateOptimizer`, and perform wheel-speed desaturation through WPILib's `SwerveDriveKinematics.desaturateWheelSpeeds`.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveOutputPipeline.java`.
- Verification: PASS by source inspection; no hardware, IO, telemetry, commands, RobotContainer, vendor APIs, or retained mutable input are used.
- Expected Result: The method returns newly allocated FL/FR/BL/BR states with optimized direction and bounded absolute speed.

## Step 4 - Add focused pipeline tests

- Objective: Define the minimum behavioral and validation contract for the new pure pipeline.
- Why: The pipeline must be deterministic and safe at zero, normal, excessive, reversed, combined, invalid, and input-ownership boundaries.
- Action: Add tests for zero speeds, normal motion, proportional desaturation, the final speed limit, optimized reversal, combined translation and rotation, ordering, null inputs, wrong array length, invalid explicit limits, and caller-input immutability.
- Files Changed: `src/test/java/frc/robot/subsystems/SwerveOutputPipelineTest.java`.
- Verification: PASS; focused tests passed 13/13.
- Expected Result: All required pipeline behavior and validation cases pass.

## Step 5 - Record lesson documentation and verification state

- Objective: Document the S00_L12 architecture, constant decision, test contract, and current verification evidence.
- Why: Lesson status must report only verified facts and keep user-owned runtime and Git work visible as pending.
- Action: Update the lesson README, plan, status, checklist, and this transition guide. Record the verified 13/13 focused tests and full build, keep runtime checks NOT APPLICABLE, record commit `0295ac0` and push to `origin/main`, and freeze the lesson as COMPLETE / FROZEN / READ-ONLY.
- Files Changed: `README.md`, `LESSON_PLAN.md`, `LESSON_STATUS.md`, `LESSON_CHECKLIST.md`, `docs/S00_L11_to_S00_L12_Step_by_Step.md`.
- Verification: PASS; documentation matches the implemented source and verified results.
- Expected Result: S00_L12 is documented as COMPLETE / FROZEN / READ-ONLY. The provisional 4.0 m/s baseline remains a later hardware-validation follow-up.
