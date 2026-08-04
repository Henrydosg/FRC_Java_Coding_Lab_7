# S00_L13 Swerve Module Control Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L13_SwerveModuleControl_Foundation
- Previous Lesson: S00_L12_SwerveOutputPipeline_Foundation
- Previous Lesson Status: COMPLETE / FROZEN
- Source: S00_L12_SwerveOutputPipeline_Foundation
- Status: IN_PROGRESS
- Freeze: NOT TESTED
- Architecture Review: PASS
- Date: 2026-08-04
- Reviewer: Architecture Review

## Exact Lesson Goal

Integrate the existing `SwerveOutputPipeline` into `SwerveSubsystem`. The subsystem owns four
final module states in deterministic FL/FR/BL/BR order and provides read-only access for later
lessons. This lesson does not command hardware.

## Architecture Decision

Use the existing stateless `frc.robot.subsystems.SwerveOutputPipeline` directly from
`SwerveSubsystem`; do not add a ModuleController or dispatcher. `periodic()` refreshes all four
module IOInputs, then invokes the pipeline with a copied robot-relative chassis intent and current
encoder angles in FL/FR/BL/BR order. `acceptChassisSpeeds()` also refreshes the owned final-state
snapshot using the latest known angles so the subsystem remains the sole state owner between
periodic cycles.

The subsystem stores four internally owned `SwerveModuleState` objects and
`getFinalModuleStates()` returns a new array with new state objects. No final state is sent to IO,
and no vendor API, motor output, telemetry expansion, command, or RobotContainer change is needed.

## Focused Test Plan

`SwerveSubsystemTest` covers:

- correct FL/FR/BL/BR ownership by comparing each slot with the existing pipeline;
- deterministic output data across repeated reads; and
- array and state defensive copies that prevent caller mutation from changing subsystem state.

## Verification Record

- Architecture Review: PASS.
- Implementation: PASS.
- Baseline build: PASS based on the user's inherited S00_L12 baseline result.
- Focused tests: PASS - 8/8.
- Full build: PASS.
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; no runtime hardware actuation path was added.
- Documentation: PASS.
- Commit, push, and freeze: NOT TESTED by instruction.

## Forbidden Scope

Do not add field-relative conversion, cosine compensation, PID, motor output, IO behavior, runtime
wiring, telemetry expansion, command scheduling, hardware configuration, odometry, pose
estimation, gyro integration, or changes to prior frozen lessons.
