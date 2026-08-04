# S00_L14 Swerve Hardware Configuration and Commissioning Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L14_SwerveHardwareConfiguration_Commissioning_Foundation
- Previous Lesson: S00_L13_SwerveModuleControl_Foundation
- Previous Lesson Status: COMPLETE / FROZEN / READ-ONLY
- Source: S00_L13_SwerveModuleControl_Foundation
- Status: COMPLETE
- Freeze: NOT TESTED
- Architecture Review: PASS
- Date: 2026-08-04
- Reviewer: Architecture Review

## Exact Lesson Goal

Audit the inherited Constants, IO adapters, observations, telemetry, tests, and documentation for
the four swerve modules and Pigeon2. Produce a hardware matrix that distinguishes VERIFIED,
PROVISIONAL, and UNRESOLVED values without commanding hardware.

## Architecture Decision

PASS. No new code abstraction is justified. `SwerveModuleIO` already owns the one-cycle raw module
snapshot, `GyroIO` already owns the raw gyro snapshot, `SwerveSubsystem` already creates immutable
`SwerveObservation` values, and `SwerveTelemetryFacade` already publishes connectivity and
configuration-health fields. The audit therefore adds documentation only and preserves the
Frozen Backbone, Interface Contract, vendor isolation, and read-only telemetry boundary.

## Final Lesson State

COMPLETE / FROZEN / READ-ONLY. The documented lesson state is complete; Commit, Push, and Freeze
remain NOT TESTED because all Git operations are user-owned.

## Audit Findings

- `Constants.java` and the inherited hardware map agree on the four module CAN ID triplets, 7.85:1 drive ratio, 4.0 inch wheel diameter, and 21.5 inch wheelbase/track width.
- Steer ratio, inversion, absolute offsets, neutral modes, current limits, ramp rates, Pigeon2 mounting, and yaw convention remain UNRESOLVED. CAN bus, device connectivity, configuration health, and commissioning checks are now supported by the supplied real-robot evidence.
- FL CANcoder sensor direction was verified on hardware; FR/BL/BR hardware checks passed.
- `SwerveModuleIOCTRE` refreshes vendor signals and records connectivity/configuration status but does not apply configuration values.
- `GyroIOPigeon2` refreshes raw Pigeon2 signals and records connectivity/configuration status but does not define bus, mounting, or yaw convention.
- `SwerveSubsystem.periodic()` refreshes all five IO snapshots and creates observations without calling module output methods.

## Focused Verification Plan

No new Java validation or read-only class was added. Existing inherited tests remain the focused
software boundary. The supplied hardware record completes the observational commissioning checks:
14/14 CTRE devices were detected on `rio`, all expected device families were online, no duplicate
CAN IDs or unexpected faults were reported, and the Disabled-only sensor and Pigeon2 checks passed.

## Verification Record

- Architecture Review: PASS.
- Implementation: PASS - audit foundation is documentation-only because the inherited read-only path is sufficient.
- Baseline build: PASS based on the user's inherited S00_L13 result.
- Focused tests: NOT TESTED; no focused-test result was supplied for this documentation finalization.
- Full build: PASS; result supplied by the user.
- Simulation: NOT APPLICABLE.
- Glass: NOT APPLICABLE.
- Driver Station: PASS - Disabled commissioning only.
- Real Robot: PASS - Disabled commissioning only.
- Phoenix Tuner X: PASS - hardware verification evidence supplied by the user.
- Documentation: PASS.
- Commit, Push, and Freeze: NOT TESTED by instruction.

## Forbidden Scope

Do not write device configurations, change unresolved values, add motor output, commands, Xbox
input, teleop, PID, closed-loop control, odometry, pose estimation, autonomous logic, or modify
prior frozen lessons.
