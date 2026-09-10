# S00_L06 to S00_L07 Step by Step

## Step 1 - Copy the previous lesson

- Objective: Create S00_L07 from S00_L06.
- Why: The runtime lesson must inherit the approved telemetry facade and Observation contracts.
- Action: Copy the S00_L06 project into the S00_L07 lesson directory.
- Files Changed: New S00_L07 project copy.
- Verification: Confirm the independent S00_L07 project exists.
- Expected Result: S00_L07 inherits the S00_L06 telemetry foundation.

## Step 2 - Rename the lesson

- Objective: Apply the S00_L07_Runtime_Telemetry_Integration identity.
- Why: Project metadata and documentation must identify the active lesson.
- Action: Rename the copied project and lesson metadata.
- Files Changed: S00_L07 project identity and metadata.
- Verification: Confirm Lesson, Previous Lesson, and Source fields.
- Expected Result: The active project is identified as S00_L07.

## Step 3 - Initialize the documentation framework

- Objective: Initialize the four Framework v2.1 documents.
- Why: Overview, plan, evidence, and execution tracking have separate roles.
- Action: Initialize README.md, LESSON_PLAN.md, LESSON_STATUS.md, and LESSON_CHECKLIST.md.
- Files Changed: The four S00_L07 lesson documents.
- Verification: Confirm metadata, fixed vocabulary, scope, evidence, and checklist structure.
- Expected Result: S00_L07 has a consistent documentation foundation.

## Step 4 - Record the baseline build

- Objective: Establish inherited build evidence before runtime changes.
- Why: The baseline distinguishes inherited project health from integration work.
- Action: Build the copied S00_L07 project before Java changes.
- Files Changed: None required.
- Verification: User verified BUILD SUCCESSFUL before S00_L07 Java changes.
- Expected Result: Baseline Build is PASS.

## Step 5 - Audit the runtime architecture

- Objective: Review the inherited facade, subsystem Observation accessor, Robot lifecycle, RobotContainer, and D01 runtime pattern.
- Why: Runtime telemetry must publish after subsystem refresh without becoming a control path.
- Action: Audit ownership, dependency injection, lifecycle order, Optional behavior, IO selection, and shutdown boundaries.
- Files Changed: None during the audit.
- Verification: Confirm the scheduler-before-telemetry order and composition-root responsibilities.
- Expected Result: The runtime integration boundary is explicit.

## Step 6 - Approve the architecture

- Objective: Lock RobotTelemetry, deterministic Noop IOs, composition, and lifecycle ordering.
- Why: Formal approval prevents runtime wiring from changing frozen interfaces or control behavior.
- Action: Approve the exact files, real/simulation selection, Optional.empty handling, and one publish attempt per cycle.
- Files Changed: LESSON_PLAN.md records the approved decision.
- Verification: Confirm Architecture Review is PASS.
- Expected Result: Implementation has one approved runtime concept.

## Step 7 - Integrate runtime telemetry

- Objective: Compose the Swerve subsystem and telemetry facade into the robot runtime.
- Why: The facade requires a lifecycle owner to publish completed immutable observations.
- Action: Add RobotTelemetry and deterministic Noop IOs; update RobotContainer for real/simulation selection and update Robot.robotPeriodic() to run the scheduler before telemetry.
- Files Changed: RobotTelemetry.java, SwerveModuleIONoop.java, GyroIONoop.java, RobotContainer.java, and Robot.java.
- Verification: Confirm no close/shutdown, control, physics, kinematics, odometry, or estimation behavior was added.
- Expected Result: Implementation and Architecture Review are PASS.

## Step 8 - Record user build verification

- Objective: Verify the integrated runtime project builds.
- Why: Compilation confirms the coordinator, IO selection, and lifecycle wiring agree with inherited contracts.
- Action: Run the clean build after implementation.
- Files Changed: Build outputs only.
- Verification: User verified BUILD SUCCESSFUL after implementation.
- Expected Result: Build is PASS.

## Step 9 - Verify simulation telemetry

- Objective: Verify runtime telemetry in simulation and the Driver Station / Glass path.
- Why: Deterministic Noop IOs provide a safe startup path without simulation physics or hardware behavior.
- Action: Start simulation and inspect the Swerve telemetry topics through Driver Station / Glass.
- Files Changed: None beyond implementation and evidence documents.
- Verification: User verified Simulation PASS and Driver Station / Glass PASS.
- Expected Result: Runtime telemetry is verified in simulation; Real Robot is NOT_APPLICABLE because no real-hardware verification is required in this lesson.

## Step 10 - Complete documentation and Git workflow

- Objective: Finish lesson evidence and identify delivery actions.
- Why: Completion requires documentation, commit, push, and freeze verification.
- Action: Set Documentation and Transition Guide to PASS; later commit, push, and freeze the lesson.
- Files Changed: LESSON_STATUS.md, LESSON_CHECKLIST.md, and this guide.
- Verification: Commit, Push, and Freeze remain NOT_TESTED; Lesson Status remains IN_PROGRESS.
- Expected Result: Documentation is complete and remaining Git steps are explicit.
