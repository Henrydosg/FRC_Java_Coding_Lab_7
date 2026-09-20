# S00_L08 to S00_L09: ChassisSpeeds Foundation

## Step 1 - Inherit the frozen predecessor

- Objective: Start S00_L09 from the frozen S00_L08 foundation.
- Why: Preserve inheritance and the approved Swerve module-state boundary.
- Action: Use S00_L08 COMPLETE/FROZEN as the source lesson.
- Files Changed: S00_L09 lesson project metadata.
- Verification: S00_L08 completion commit `573c814` and documentation correction commit `3efb143` are recorded.
- Expected Result: S00_L09 inherits a frozen predecessor without modifying it.

## Step 2 - Lock the chassis-intent boundary

- Objective: Define one architectural concept for chassis velocity intent.
- Why: Prevent `ChassisSpeeds` from becoming accidental kinematics or hardware control.
- Action: Approve subsystem acceptance of robot-relative intent only.
- Files Changed: `LESSON_PLAN.md`, `LESSON_STATUS.md`.
- Verification: Architecture Review = PASS; forbidden scope is documented.
- Expected Result: Ownership and dependency direction are explicit.

## Step 3 - Snapshot mutable WPILib input

- Objective: Prevent caller mutation from changing retained subsystem intent.
- Why: WPILib `ChassisSpeeds` is mutable.
- Action: Copy vx, vy, and omega into a private immutable nested record.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: 5/5 focused SwerveSubsystem tests passed.
- Expected Result: No caller-owned `ChassisSpeeds` reference is retained.

## Step 4 - Preserve inert runtime behavior

- Objective: Keep the lesson non-actuating.
- Why: Kinematics, IO output, and drivetrain behavior are deferred.
- Action: Keep periodic observation-only and reset intent before existing four-module stop delegation.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: Focused tests passed; Simulation, Glass, Driver Station, and Real Robot are NOT APPLICABLE for this scope.
- Expected Result: Chassis intent has no physical runtime effect.

## Step 5 - Build verification and documentation

- Objective: Verify the implementation and record evidence.
- Why: Separate implementation correctness from later delivery steps.
- Action: Run `:test` and `gradlew build`; finalize Framework v2.1 records.
- Files Changed: `LESSON_STATUS.md`, `LESSON_CHECKLIST.md`, `README.md`, `LESSON_PLAN.md`, and this guide.
- Verification: 5/5 tests passed; `BUILD SUCCESSFUL in 1m 11s`.
- Expected Result: Documentation is complete and S00_L09 is frozen read-only after commit and push.
