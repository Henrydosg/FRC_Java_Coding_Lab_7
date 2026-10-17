# S00_L05 to S00_L06 Step by Step

## Step 1 - Copy the previous lesson

- Objective: Create S00_L06 from S00_L05.
- Why: Each lesson inherits the verified Observation foundation and frozen project structure.
- Action: Copy the S00_L05 project into the S00_L06 lesson directory.
- Files Changed: New S00_L06 project copy.
- Verification: Confirm the independent S00_L06 project exists.
- Expected Result: S00_L06 inherits the immutable SwerveObservation contract.

## Step 2 - Rename the lesson

- Objective: Apply the S00_L06_Telemetry_Foundation identity.
- Why: All lesson metadata and documentation must identify the active lesson.
- Action: Rename the copied project and lesson metadata.
- Files Changed: S00_L06 project identity and metadata.
- Verification: Confirm Lesson, Previous Lesson, and Source fields.
- Expected Result: The active project is identified as S00_L06.

## Step 3 - Initialize the documentation framework

- Objective: Initialize the four Framework v2.1 documents.
- Why: README, plan, status, and checklist have distinct roles.
- Action: Initialize README.md, LESSON_PLAN.md, LESSON_STATUS.md, and LESSON_CHECKLIST.md.
- Files Changed: The four S00_L06 lesson documents.
- Verification: Confirm metadata, fixed vocabulary, scope, evidence, and execution tracking.
- Expected Result: S00_L06 has a consistent documentation foundation.

## Step 4 - Record the baseline build

- Objective: Establish inherited build evidence before telemetry changes.
- Why: The baseline separates inherited project health from this lesson's implementation.
- Action: Build the copied S00_L06 project before Java changes.
- Files Changed: None required.
- Verification: User verified BUILD SUCCESSFUL before S00_L06 Java changes.
- Expected Result: Baseline Build is PASS.

## Step 5 - Audit the telemetry architecture

- Objective: Review Document C, SwerveObservation, and completed D01 telemetry patterns.
- Why: Telemetry must consume immutable Observations without becoming a control path.
- Action: Audit facade ownership, typed publishers, topic conventions, and dependency direction.
- Files Changed: None during the audit.
- Verification: Confirm no IOInputs, hardware, vendor, scheduler, command, or RobotContainer dependency enters telemetry.
- Expected Result: The single facade concept is bounded.

## Step 6 - Approve the architecture

- Objective: Lock the SwerveTelemetryFacade contract before implementation.
- Why: Formal approval prevents runtime wiring and topic expansion from entering the foundation lesson.
- Action: Approve NetworkTable injection, typed publisher ownership, exact subtables/topics, publish(SwerveObservation), and close().
- Files Changed: LESSON_PLAN.md records the approved decision.
- Verification: Confirm deferred Optional handling, cadence, RobotTelemetry, RobotContainer, and Robot lifecycle integration.
- Expected Result: Architecture Review is PASS.

## Step 7 - Implement telemetry

- Objective: Implement the read-only SwerveTelemetryFacade.
- Why: A dedicated facade isolates NetworkTables publication from robot behavior.
- Action: Create SwerveTelemetryFacade.java with the approved diagnostic subset and complete publisher cleanup.
- Files Changed: src/main/java/frc/robot/telemetry/swerve/SwerveTelemetryFacade.java.
- Verification: Confirm exact topic names, DoublePublisher/BooleanPublisher types, non-null publish contract, and AutoCloseable cleanup.
- Expected Result: Implementation is complete without runtime composition.

## Step 8 - Record user build verification

- Objective: Verify the telemetry facade compiles in the lesson project.
- Why: Compilation confirms the facade matches the inherited Observation contract and WPILib APIs.
- Action: Run the clean build after implementation.
- Files Changed: Build outputs only.
- Verification: User verified BUILD SUCCESSFUL after implementation.
- Expected Result: Build is PASS.

## Step 9 - Record deferred runtime verification

- Objective: Record runtime applicability without claiming unperformed testing.
- Why: The telemetry facade is not composed into a runtime coordinator in this lesson.
- Action: Set Simulation, Driver Station / Glass, and Real Robot to NOT_APPLICABLE with that rationale.
- Files Changed: LESSON_STATUS.md and LESSON_CHECKLIST.md.
- Verification: Confirm all three applicability states and rationales.
- Expected Result: Runtime integration remains explicitly deferred.

## Step 10 - Complete documentation and Git workflow

- Objective: Finish lesson evidence and identify delivery actions.
- Why: Completion requires documentation, commit, push, and freeze verification.
- Action: Set Documentation and Transition Guide to PASS; later commit, push, and freeze the lesson.
- Files Changed: LESSON_STATUS.md, LESSON_CHECKLIST.md, and this guide.
- Verification: Commit, Push, and Freeze remain NOT_TESTED; Lesson Status remains IN_PROGRESS.
- Expected Result: Documentation is complete and remaining Git steps are explicit.
