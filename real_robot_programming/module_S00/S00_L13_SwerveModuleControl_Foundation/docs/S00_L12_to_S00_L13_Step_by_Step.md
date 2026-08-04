# S00_L12 to S00_L13 Step by Step

## Step 1

- Objective: Audit the inherited S00_L12 boundary.
- Why: The lesson must add one concept while preserving the Frozen Backbone and the existing IO contract.
- Action: Confirm that `SwerveOutputPipeline` is stateless, ordered FL/FR/BL/BR, vendor-neutral, and independent of hardware output.
- Files Changed: None.
- Verification: The inherited pipeline and `SwerveSubsystem` were inspected before implementation.
- Expected Result: The pipeline can be reused directly by the subsystem without a controller or dispatcher class.

## Step 2

- Objective: Make `SwerveSubsystem` the owner of final module states.
- Why: The subsystem owns mechanism state, while the pipeline remains a pure calculation helper.
- Action: Add one subsystem-owned pipeline, four final-state slots, current-angle conversion from IOInputs, and pipeline refresh after intent or periodic input refresh.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: The implementation contains no vendor imports, IO output calls, or changes to IO contracts.
- Expected Result: Final states are always stored in FL/FR/BL/BR order and are calculated through the existing pipeline.

## Step 3

- Objective: Provide safe read-only access.
- Why: Later lessons need the final state data without receiving mutable aliases to subsystem state.
- Action: Add `getFinalModuleStates()` that returns a new array and a new `SwerveModuleState` for each slot.
- Files Changed: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Verification: The getter copies both the array and each state object.
- Expected Result: Mutating returned data cannot change the next getter result.

## Step 4

- Objective: Verify ownership, ordering, integration, and isolation.
- Why: The new boundary must be proven without commanding hardware.
- Action: Add focused `SwerveSubsystemTest` cases comparing each state slot with a direct pipeline result, checking repeatability, and mutating returned data.
- Files Changed: `src/test/java/frc/robot/subsystems/SwerveSubsystemTest.java`.
- Verification: `SwerveSubsystemTest` passed 8/8; observation refresh performed no IO output calls.
- Expected Result: Focused subsystem tests pass and no IO output method is called.

## Step 5

- Objective: Record the lesson handoff state.
- Why: S00_L13 remains active until user-owned verification and Git lifecycle steps are complete.
- Action: Update only S00_L13 README, plan, checklist, status, and this transition guide.
- Files Changed: S00_L13 documentation files only.
- Verification: Documentation records Architecture Review PASS, Implementation PASS, Focused Tests PASS - 8/8, Full Build PASS, runtime checks NOT APPLICABLE, Documentation PASS, and Commit/Push/Freeze NOT TESTED.
- Expected Result: The lesson remains `IN_PROGRESS` with no unsupported completion claims.
