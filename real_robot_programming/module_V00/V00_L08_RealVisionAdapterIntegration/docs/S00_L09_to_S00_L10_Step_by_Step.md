# S00_L09 to S00_L10: SwerveDriveKinematics Foundation

## Step 1 - Inherit the frozen predecessor

- Objective: Start S00_L10 from S00_L09.
- Why: Preserve the frozen chassis-intent foundation and repository inheritance workflow.
- Action: Use the S00_L09 COMPLETE/FROZEN project as the source.
- Files Changed: S00_L10 project metadata.
- Verification: Confirm S00_L09 is the source lesson and remains unchanged.
- Expected Result: S00_L10 is an independent working lesson.

## Step 2 - Define the kinematics boundary

- Objective: Introduce one pure chassis-to-module conversion concept.
- Why: Establish geometry and ordering before any runtime drivetrain behavior.
- Action: Place one `SwerveKinematics` helper in `frc.robot.subsystems`.
- Files Changed: `LESSON_PLAN.md`, `LESSON_STATUS.md`.
- Verification: Architecture Review = PASS; forbidden scope is documented.
- Expected Result: Ownership and dependency direction are explicit.

## Step 3 - Define physical module locations

- Objective: Create the four module translations.
- Why: Kinematics requires physical locations and Constants is the configuration authority.
- Action: Use half wheelbase and half track width with WPILib +X forward/+Y left coordinates.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveKinematics.java`.
- Verification: Locations are FL, FR, BL, BR and use only `Constants.java` geometry.
- Expected Result: No dimensions are invented or duplicated.

## Step 4 - Convert chassis speeds

- Objective: Convert robot-relative `ChassisSpeeds` into module states.
- Why: Provide the standard WPILib kinematics foundation without actuation.
- Action: Delegate to one `SwerveDriveKinematics` instance and return its ordered states.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveKinematics.java`.
- Verification: 7/7 focused tests passed, covering zero, forward translation, left translation, counterclockwise rotation, combined motion, deterministic ordering, and null rejection.
- Expected Result: Four deterministic FL/FR/BL/BR module states are produced.

## Step 5 - Verify and record

- Objective: Verify the pure conversion and preserve honest lesson status.
- Why: Separate mathematical correctness from future runtime integration.
- Action: User runs focused tests and `gradlew build`.
- Files Changed: `LESSON_STATUS.md`, `LESSON_CHECKLIST.md`, and this guide.
- Verification: User confirmed focused tests PASS (7/7) and full `gradlew build` PASS.
- Expected Result: S00_L10 is COMPLETE, FROZEN, and READ-ONLY after verified user-managed Git workflow completion.
