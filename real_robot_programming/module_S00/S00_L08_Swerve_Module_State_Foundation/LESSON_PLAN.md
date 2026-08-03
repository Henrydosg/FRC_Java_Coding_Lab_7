# Metadata

- Framework Version: 2.1
- Lesson: S00_L08_Swerve_Module_State_Foundation
- Previous Lesson: S00_L07_Runtime_Telemetry_Integration
- Source: S00_L07_Runtime_Telemetry_Integration
- Source Status: IN_PROGRESS; inherited runtime telemetry, architecture, implementation, build, simulation, and Driver Station / Glass evidence are recorded in S00_L07.
- Owner: UNASSIGNED
- Date: UNSET
- Reviewer: UNASSIGNED

# Purpose

Implement one measured-state concept: interpret inherited immutable Swerve observations as measured `edu.wpi.first.math.kinematics.SwerveModuleState` values. The state is read-only measured data, never a desired state or control command.

# Frozen Architecture

Follow AGENTS.md, Document A, ES-06, Document C, and FAR. Preserve hardware -> IOInputs -> subsystem -> immutable Observation -> telemetry and the existing control flow. Module state must not access hardware or control behavior.

# Frozen Interfaces

- Preserve SwerveModuleIO and SwerveModuleIOInputs as hardware contracts.
- Preserve SwerveObservation immutability and SwerveSubsystem lifecycle semantics.
- Do not publish mutable IOInputs or introduce vendor types into module-state meaning.
- Use WPILib `SwerveModuleState` directly; do not create a project-specific wrapper.
- Preserve module order: FrontLeft, FrontRight, BackLeft, BackRight (FL, FR, BL, BR).
- State speed is measured wheel speed in meters per second, derived only from the verified wheel radius and drive ratio.
- State angle is `Rotation2d.fromRotations(absoluteEncoderRotations)` and is explicitly `UNCALIBRATED`.
- Do not apply steer ratio, CANcoder offsets, or inversion conventions; those values remain unresolved.

# Dependencies

- Existing SwerveModuleIOInputs and immutable SwerveObservation values.
- Existing SwerveSubsystem and runtime telemetry boundaries.
- Java standard library and approved vendor-neutral value types only.
- No new vendor dependency is authorized by initialization.

# Scope

- Add the smallest measured module-state interpretation using WPILib `SwerveModuleState` directly.
- Define the measured-state ownership, verified conversion, uncalibrated-angle semantics, lifecycle, and dependency direction.
- Preserve runtime telemetry and defer drivetrain behavior.

# Out of Scope

Desired state, optimization, kinematics, ChassisSpeeds, odometry, pose estimation, commands, controls, motor behavior, PID, calibration, offsets, inversions, steer-ratio conversion, hardware configuration, telemetry topic changes, RobotContainer wiring, Robot lifecycle changes, simulation physics, and vendor APIs.

# Expected Files

- Create only the approved measured-state implementation file(s) and the required SwerveSubsystem accessor/production changes.
- Update LESSON_STATUS.md and LESSON_CHECKLIST.md with evidence only.
- A transition guide is deferred until implementation and verification are complete.

# Forbidden Files

Do not modify SwerveModuleIO, SwerveModuleIOCTRE, SwerveModuleIONoop, GyroIO, SwerveObservation, SwerveSubsystem, SwerveTelemetryFacade, RobotTelemetry, RobotContainer, Robot, Constants.java, vendordeps, inherited transition guides, previous lessons, or governance documents before approval.

# Implementation Order

1. Confirm S00_L07 source context and user-verified S00_L08 baseline build.
2. Audit inherited module IOInputs, SwerveObservation, subsystem, and telemetry contracts.
3. Implement measured `SwerveModuleState` values in FL/FR/BL/BR order using verified drive conversion and uncalibrated encoder angle.
4. Complete formal implementation review.
5. Implement only the approved module-state files and behavior.
6. Build and record evidence; decide runtime applicability with explicit rationales.
7. Complete documentation, delivery, and freeze steps.

# Verification Plan

- Static: module state is vendor-neutral, immutable where applicable, derived only from approved Observation data, and contains no IOInputs alias, hardware, vendor, control, or telemetry-destination dependency.
- Conversion: `wheelSpeed_mps = driveVelocity_rotations_per_second / 7.85 * 2π * wheelRadius_m`; angle uses `Rotation2d.fromRotations(absoluteEncoderRotations)` and is marked `UNCALIBRATED`.
- Ordering: measured states are exposed only as FL, FR, BL, BR.
- Baseline Build: PASS (user-verified before S00_L08 Java changes).
- Build: run the clean Gradle build only after approved implementation and record the result in LESSON_STATUS.md.
- Simulation: remain NOT TESTED until module-state architecture is approved; use NOT_APPLICABLE only with a documented rationale.
- Driver Station / Glass: remain NOT TESTED unless a reviewed state-consumption path is added; use NOT_APPLICABLE only with a documented rationale.
- Real Robot: remain NOT TESTED unless the approved module-state path is composed; verification must be secured and read-only.

# Planned Technical Debt

- Angle calibration, steer ratio, CANcoder offsets, and inversion conventions remain unresolved and intentionally unapplied.
- Kinematics, odometry, pose estimation, simulation physics, commands, controls, and motor behavior remain deferred.
- Desired-state semantics, optimization, ChassisSpeeds, and runtime wiring remain deferred.

# Current Known Issues

- S00_L07 remains IN_PROGRESS because commit, push, and freeze evidence are not verified; inherited runtime telemetry context is usable but not promoted to current-lesson completion.
- Runtime applicability remains deferred because no runtime wiring is changed in this lesson.

# Architecture Decision

- Reason: Establish a governed module-state meaning boundary before later Swerve behavior and estimation work.
- Scope: Measured module-state interpretation only: direct WPILib `SwerveModuleState`, verified drive conversion, `Rotation2d.fromRotations` encoder angle, explicit `UNCALIBRATED` angle status, and FL/FR/BL/BR ordering.
- Impact: Adds no control semantics and preserves IO ownership, immutable Observations, telemetry, runtime composition, and control flow.
- Decision: APPROVED FOR IMPLEMENTATION
- Reviewer: Architecture Review
- Date: 2026-08-03

Framework v2.1 remains the required documentation structure for this lesson.
