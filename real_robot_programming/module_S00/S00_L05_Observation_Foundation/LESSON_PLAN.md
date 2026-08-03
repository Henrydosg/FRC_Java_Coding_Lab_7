# Metadata

- Framework Version: 2.1
- Lesson: S00_L05_Observation_Foundation
- Previous Lesson: S00_L04_Swerve_Subsystem_Foundation
- Source: S00_L04_Swerve_Subsystem_Foundation
- Source Status: COMPLETE/FROZEN (commit 8aea88f)
- Owner: UNASSIGNED
- Date: UNSET
- Reviewer: USER-APPROVED

# Purpose

Implement one concept: one immutable aggregate SwerveObservation produced after a complete periodic IO refresh.

# Frozen Architecture

Follow AGENTS.md, Document A, ES-06, Document C, and FAR. Preserve the flow hardware -> IOInputs -> subsystem -> immutable Observation -> telemetry. Observation code is read-only meaning, never hardware access or control behavior.

# Frozen Interfaces

- Preserve SwerveModuleIO, SwerveModuleIOInputs, GyroIO, GyroIOInputs, and the S00_L04 subsystem lifecycle contracts.
- Do not expose or retain mutable IOInputs objects as the Observation contract.
- Observation types must be immutable, vendor-neutral, deterministic, and explicit about the inherited units and per-device validity fields.

# Dependencies

- Java standard library and approved vendor-neutral value types only.
- Existing SwerveSubsystem and IO interfaces.
- No new vendor dependency, NetworkTables dependency, telemetry publisher, or hardware API.

# Scope

- Create SwerveObservation.java in frc.robot.observation with nested immutable ModuleObservation and GyroObservation value types.
- Include every existing module and gyro IOInputs scalar field with identical names and units.
- Copy scalar values only; never retain IOInputs references.
- Have SwerveSubsystem replace latestObservation once, after all five updateInputs calls.
- Before the first periodic cycle, no observation exists; getObservation() returns Optional<SwerveObservation>.
- Keep observation construction separate from telemetry publication and control decisions.

# Out of Scope

IO interface or CTRE implementation changes, hardware configuration, commands, controls, RobotContainer wiring, telemetry publishers, NetworkTables, Glass, logging destinations, kinematics, odometry, pose estimation, motion/control behavior, PID, calibration, inversion, simulation/Noop implementations, and unrelated mechanism models or evaluators.

# Expected Files

- Create src/main/java/frc/robot/observation/SwerveObservation.java with nested immutable ModuleObservation and GyroObservation types.
- Modify src/main/java/frc/robot/subsystems/SwerveSubsystem.java to produce and expose Optional<SwerveObservation>.
- Update LESSON_STATUS.md and LESSON_CHECKLIST.md with evidence only.
- Do not create a transition guide in this initialization task.

# Forbidden Files

Only these Java files are allowed: observation/SwerveObservation.java and subsystems/SwerveSubsystem.java. Do not modify Robot.java, RobotContainer.java, Constants.java, IO interfaces, CTRE implementations, vendordeps, inherited transition guides, previous lessons, or packages outside the approved architecture.

# Implementation Order

1. Confirm resolved S00_L04 COMPLETE/FROZEN dependency (commit 8aea88f) and the user-verified PASS baseline build.
2. Audit the existing IOInputs scalar fields, identical names, units, and per-device validity fields.
3. Implement SwerveObservation.java with nested immutable ModuleObservation and GyroObservation types.
4. Update SwerveSubsystem to replace latestObservation once after all five refreshes and return Optional<SwerveObservation>.
5. Verify immutability, vendor neutrality, copied values, no IOInputs aliases, and no pre-cycle observation.
6. Build and record evidence; decide runtime applicability with explicit rationales.
7. Complete documentation, delivery, and freeze steps.

# Verification Plan

- Static: observation is immutable and vendor-neutral; every inherited scalar field is represented with identical names and units; no mutable IOInputs alias is retained; no timestampSeconds or aggregate validity is added; no hardware, vendor, NetworkTables, RobotContainer, command, control, or telemetry-publisher dependency exists.
- Lifecycle: latestObservation is replaced exactly once after all five updateInputs calls, and Optional.empty() is returned before the first complete cycle.
- Baseline Build: PASS (user-verified before Java changes).
- Build: run the clean Gradle build only after implementation and record the result in LESSON_STATUS.md.
- Simulation: remain NOT TESTED until applicability is reviewed; use NOT_APPLICABLE only with a documented rationale.
- Driver Station / Glass: remain NOT TESTED unless a reviewed telemetry path is added; use NOT_APPLICABLE only with a documented rationale.
- Real Robot: remain NOT TESTED unless the observation path is composed and scheduled; any verification is limited to secured readback and safe stop.

# Planned Technical Debt

- Any future evaluator, timestamp semantics, and aggregate validity policy remain deferred; this lesson intentionally adds neither timestampSeconds nor aggregate validity.
- Telemetry consumption and publication remain deferred to a later lesson.
- Swerve kinematics, odometry, pose estimation, simulation, and control behavior remain deferred.

# Current Known Issues

- S00_L05 implementation, architecture verification, build, runtime verification, transition guide, commit, push, and freeze remain pending.

# Architecture Decision

- Reason: Establish the permanent immutable read-model boundary required by Document C after the S00_L04 subsystem foundation.
- Scope: One aggregate SwerveObservation with nested immutable ModuleObservation and GyroObservation values, produced after complete IO refresh.
- Impact: Adds immutable read-model meaning while preserving IO ownership, subsystem lifecycle, telemetry separation, and control flow.
- Decision: APPROVED FOR IMPLEMENTATION
- Reviewer: USER-APPROVED
- Date: UNSET

Framework v2.1 remains the required documentation structure for this lesson.
