# S00_L04 to S00_L05 Step by Step

## Step 1 - Copy the completed lesson

- Objective: Create S00_L05 from the S00_L04 project.
- Why: Each lesson inherits a known architecture and working source baseline.
- Action: Copy the S00_L04 project into the S00_L05 lesson directory.
- Files Changed: New S00_L05 project copy.
- Verification: Confirm the independent S00_L05 project exists.
- Expected Result: S00_L05 inherits the frozen S00_L04 structure.

## Step 2 - Rename the lesson

- Objective: Apply the S00_L05_Observation_Foundation identity.
- Why: Lesson metadata and documentation must identify the active lesson consistently.
- Action: Rename the copied project and lesson metadata.
- Files Changed: S00_L05 project identity and metadata.
- Verification: Confirm Lesson, Previous Lesson, and Source fields are correct.
- Expected Result: The active project is identified as S00_L05.

## Step 3 - Initialize the documentation framework

- Objective: Initialize the four Framework v2.1 documents.
- Why: Overview, plan, evidence, and execution tracking have separate roles.
- Action: Initialize README.md, LESSON_PLAN.md, LESSON_STATUS.md, and LESSON_CHECKLIST.md.
- Files Changed: The four S00_L05 lesson documents.
- Verification: Confirm metadata, scope, evidence vocabulary, and checklist structure.
- Expected Result: S00_L05 has a consistent documentation foundation.

## Step 4 - Record the baseline build

- Objective: Establish inherited build evidence before Java changes.
- Why: The baseline distinguishes inherited project health from lesson implementation.
- Action: Build the copied project before implementation.
- Files Changed: None required.
- Verification: User verified BUILD SUCCESSFUL before Java changes.
- Expected Result: Baseline Build is PASS.

## Step 5 - Audit the observation architecture

- Objective: Review Document C, IOInputs, SwerveSubsystem, and lesson scope.
- Why: Observation must remain immutable, vendor-neutral, and downstream of IOInputs.
- Action: Audit the inherited contracts and identify required observation fields and lifecycle semantics.
- Files Changed: None during the audit.
- Verification: Confirm all existing scalar fields, units, health flags, and the absence of timestamp or aggregate-validity contracts.
- Expected Result: The implementation boundary is explicit.

## Step 6 - Lock the architecture

- Objective: Approve one aggregate SwerveObservation concept.
- Why: Formal approval prevents mutable aliases, vendor leakage, and scope expansion.
- Action: Lock nested immutable ModuleObservation and GyroObservation types, scalar copying, Optional lifecycle, and the two-file Java boundary.
- Files Changed: LESSON_PLAN.md records the approved decision.
- Verification: Confirm S00_L04 is COMPLETE/FROZEN at commit 8aea88f and the architecture decision is APPROVED FOR IMPLEMENTATION.
- Expected Result: Implementation has one approved concept.

## Step 7 - Implement the Observation foundation

- Objective: Produce one immutable observation after a complete IO refresh.
- Why: The subsystem is the interpretation boundary between mutable transport data and immutable meaning.
- Action: Create SwerveObservation.java and update SwerveSubsystem.java to replace Optional<SwerveObservation> once after all five refreshes.
- Files Changed: The two approved Java files only.
- Verification: Confirm no IOInputs references are retained, no vendor imports exist, and getObservation() is empty before the first cycle.
- Expected Result: Observation implementation is complete and architecture compliant.

## Step 8 - Record user build verification

- Objective: Verify the implementation builds.
- Why: Compilation confirms the observation model integrates with the inherited contracts.
- Action: Run the clean build after implementation.
- Files Changed: Build outputs only.
- Verification: User verified BUILD SUCCESSFUL after implementation.
- Expected Result: Build is PASS.

## Step 9 - Record deferred runtime verification

- Objective: Record runtime applicability without implying an unperformed test.
- Why: SwerveSubsystem is not composed or scheduled in this lesson.
- Action: Set Simulation, Driver Station / Glass, and Real Robot to NOT_APPLICABLE with the rationale that SwerveSubsystem is not composed or scheduled in this lesson, so no runtime path exists.
- Files Changed: LESSON_STATUS.md and LESSON_CHECKLIST.md.
- Verification: Confirm all three states and rationales are recorded.
- Expected Result: Deferred runtime verification is explicit.

## Step 10 - Complete documentation and Git steps

- Objective: Finish evidence and identify delivery actions.
- Why: Completion requires documentation, commit, push, and freeze verification.
- Action: Set Documentation and Transition Guide to PASS; later commit, push, and freeze the lesson.
- Files Changed: LESSON_STATUS.md, LESSON_CHECKLIST.md, and this guide.
- Verification: Commit, Push, and Freeze remain NOT TESTED; lesson Status remains IN_PROGRESS.
- Expected Result: Documentation is complete and remaining Git work is explicit.
