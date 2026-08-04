# Framework Version

2.1

# Lesson

S00_L14_SwerveHardwareConfiguration_Commissioning_Foundation

# Previous Lesson

S00_L13_SwerveModuleControl_Foundation

# Status

COMPLETE

# Final Lesson State

FROZEN / READ-ONLY

# Freeze Verification

PASS - lesson commit and push completed; lesson is now FROZEN / READ-ONLY.

# Goal

Audit the inherited S00_L13 swerve hardware boundary and establish a read-only commissioning
foundation for the four modules and Pigeon2. This lesson does not configure hardware, command
motors, or invent unresolved values.

# Architecture Decision

PASS. The inherited architecture already provides the smallest compliant read-only path:

`hardware -> IOInputs -> SwerveSubsystem -> immutable SwerveObservation -> telemetry`.

`SwerveModuleIOCTRE` and `GyroIOPigeon2` keep vendor APIs inside real IO adapters. Their refresh
status and configuration-health results are copied into IOInputs, then into immutable observations
and existing read-only telemetry. `SwerveSubsystem.periodic()` refreshes observations without
calling drive or steer output methods. No new class, IO method, telemetry topic, or abstraction is
needed for this audit phase.

# Hardware Audit

PASS. The repository matrix was reconciled with the supplied disabled real-robot evidence. The
four module CAN ID triplets, Pigeon2 ID, drive ratio, and wheel diameter remain verified. Live
verification detected 14/14 CTRE devices on the `rio` CAN bus, with all TalonFX, CANcoder, and
Pigeon2 devices online, no duplicate CAN IDs, and no unexpected faults.

# Hardware Evidence Classification

- VERIFIED values are supported by the inherited `Swerve_Robot_Hardware_Map_v2.0.pdf`, matching `Constants.java` values, or the supplied live verification record. Live evidence is recorded separately from repository evidence in the matrix.
- PROVISIONAL values are software baselines that are not hardware commissioning evidence.
- UNRESOLVED values are intentionally not supplied because the repository contains no authoritative value.

The complete matrix is in `docs/S00_L14_Swerve_Hardware_Commissioning_Matrix.md`.

# Hardware Commissioning

PASS. All commissioning checks were performed while the robot remained Disabled. No unintended
motor actuation occurred. CANcoder signals updated correctly; FL CANcoder sensor direction was
verified on hardware; FR, BL, and BR hardware checks passed; and Pigeon2 communication passed.

# Verification Scope

- Architecture Review: PASS.
- Implementation: PASS - existing read-only observation and telemetry paths are reused; no source change is required for the audit phase.
- Baseline build: PASS; inherited baseline result was supplied by the user.
- Focused tests: NOT TESTED; no focused-test result was supplied for this documentation finalization.
- Full build: PASS; result supplied by the user.
- Simulation: NOT APPLICABLE; no simulation commissioning path was added.
- Glass: NOT APPLICABLE.
- Driver Station: PASS - Disabled commissioning only.
- Real Robot: PASS - Disabled commissioning only.
- Phoenix Tuner X: PASS - hardware verification evidence supplied by the user.
- Documentation: PASS for the S00_L14 audit records and matrix.
- Commit: PASS - `97af186`.
- Push: PASS - `origin/main`.
- Freeze: PASS - working tree clean and local `main` matches `origin/main`.

# Safety Boundary

No commissioning code calls `setDriveOutput()` or `setSteerOutput()`. No commands, Xbox input,
teleop, PID, closed-loop control, odometry, pose estimation, autonomous logic, or motor output was
added. The inherited CTRE adapter constructor retains its existing safe-stop behavior; this lesson
adds no new hardware actuation path.

# Out of Scope

Writing CTRE configurations, selecting neutral modes, setting current limits or ramp rates,
choosing inversion or absolute offsets, assigning a CAN bus, determining Pigeon2 mounting or yaw
convention, and changes to prior frozen lessons remain out of scope. The supplied live verification
was observational commissioning only.
