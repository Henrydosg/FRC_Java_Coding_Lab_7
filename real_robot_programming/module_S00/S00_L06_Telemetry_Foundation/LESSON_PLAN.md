# Metadata

- Framework Version: 2.1
- Lesson: S00_L06_Telemetry_Foundation
- Previous Lesson: S00_L05_Observation_Foundation
- Source: S00_L05_Observation_Foundation
- Source Status: COMPLETE / FROZEN / READ-ONLY; historical S00 closure is governed by ADR_A00 and the final S00_L24 status. S00_L05 Commit and Push remain NOT TESTED.
- Owner: UNASSIGNED
- Date: UNSET
- Reviewer: USER-APPROVED

# Purpose

Implement one concept: a read-only SwerveTelemetryFacade publishing selected immutable SwerveObservation fields.

# Frozen Architecture

Follow AGENTS.md, Document A, ES-06, Document C, and FAR. Preserve hardware -> IOInputs -> subsystem -> immutable Observation -> telemetry. Telemetry may consume and publish immutable Observations only; it must not control behavior or access hardware.

# Frozen Interfaces

- Preserve SwerveObservation immutability and SwerveSubsystem.getObservation() semantics.
- Do not publish mutable IOInputs directly.
- The facade may depend on NetworkTables publisher APIs only; it must not access hardware, IOInputs, vendor APIs, controls, commands, or RobotContainer.

# Dependencies

- Existing immutable SwerveObservation contract.
- Existing SwerveSubsystem observation accessor.
- WPILib NetworkTables typed publisher APIs.
- No vendor dependency is authorized.

# Scope

- Create one SwerveTelemetryFacade that owns typed DoublePublisher and BooleanPublisher handles.
- Constructor accepts NetworkTable.
- API is publish(SwerveObservation) and close(); implement AutoCloseable.
- publish() requires a non-null SwerveObservation.
- Publish only the approved selected fields and topic hierarchy below.

# Out of Scope

Optional.empty handling and publication cadence, RobotTelemetry, RobotContainer wiring, Robot lifecycle, runtime wiring, voltage/current/temperature topics, encoder velocity, gyro X/Y rates, kinematics, odometry, estimation, commands, controls, simulation/Noop, and motor behavior.

# Expected Files

- Create src/main/java/frc/robot/telemetry/swerve/SwerveTelemetryFacade.java only.
- Update LESSON_STATUS.md and LESSON_CHECKLIST.md with evidence only.
- A transition guide is deferred until implementation and verification are complete.

# Forbidden Files

Do not modify Robot.java, RobotContainer.java, Constants.java, IO interfaces, CTRE implementations, SwerveObservation, SwerveSubsystem, vendordeps, inherited transition guides, previous lessons, or governance documents. No Java file other than SwerveTelemetryFacade.java is allowed.

# Implementation Order

1. Confirm the S00_L05 source context and user-verified S00_L06 baseline build.
2. Verify the approved SwerveObservation fields and selected topic contract.
3. Implement SwerveTelemetryFacade with NetworkTable injection, typed publishers, publish(), and close().
4. Verify read-only Observation consumption, non-null publish contract, topic hierarchy, and publisher cleanup.
5. Build and record evidence; runtime checks remain NOT_APPLICABLE without composition.
6. Complete documentation, delivery, and freeze steps.

# Verification Plan

- Static: facade consumes immutable SwerveObservation values only; owns typed DoublePublisher and BooleanPublisher handles; publish() rejects null; no IOInputs alias, hardware access, vendor API, control behavior, or mutable publishing state leaks into the observation boundary.
- Topics: use FrontLeft, FrontRight, BackLeft, BackRight, and Gyro subtables. Per-module topics are DriveAppliedOutput, DrivePositionRotations, DriveVelocityRotationsPerSecond, SteerAppliedOutput, SteerPositionRotations, SteerVelocityRotationsPerSecond, EncoderAbsolutePositionRotations, DriveConnected, SteerConnected, EncoderConnected, DriveConfigurationHealthy, SteerConfigurationHealthy, and EncoderConfigurationHealthy. Gyro topics are YawDegrees, PitchDegrees, RollDegrees, AngularVelocityZDegreesPerSecond, Connected, and ConfigurationHealthy.
- Baseline Build: PASS (user-verified before S00_L06 Java changes).
- Build: run the clean Gradle build only after approved implementation and record the result in LESSON_STATUS.md.
- Simulation: NOT_APPLICABLE without runtime composition.
- Driver Station / Glass: NOT_APPLICABLE without runtime composition.
- Real Robot: NOT_APPLICABLE without runtime composition.

# Planned Technical Debt

- Optional.empty handling and publication cadence belong to a future coordinator.
- Runtime composition, NetworkTables integration, Glass, logging, and serialization remain deferred.
- Future diagnostics or evaluators require separate architecture review.
- Swerve kinematics, odometry, pose estimation, simulation, and control behavior remain deferred.

# Current Known Issues

- S00_L05 is historically COMPLETE / FROZEN under the S00 closure record. Its Commit and Push fields remain NOT TESTED and are not reclassified by this recovery.
- S00_L06 runtime composition and coordinator behavior remain deferred.

# Architecture Decision

- Reason: Establish a governed read-only telemetry facade over immutable SwerveObservation values.
- Scope: One SwerveTelemetryFacade with NetworkTable injection, typed publishers, selected topics, publish(SwerveObservation), and close().
- Impact: Adds one read-only telemetry consumer while preserving IO ownership, Observation immutability, subsystem behavior, control flow, and RobotContainer composition rules.
- Decision: APPROVED FOR IMPLEMENTATION
- Reviewer: USER-APPROVED
- Date: UNSET

Framework v2.1 remains the required documentation structure for this lesson.
