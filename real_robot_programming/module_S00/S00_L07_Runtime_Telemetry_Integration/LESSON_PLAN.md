# Metadata

- Framework Version: 2.1
- Lesson: S00_L07_Runtime_Telemetry_Integration
- Previous Lesson: S00_L06_Telemetry_Foundation
- Source: S00_L06_Telemetry_Foundation
- Source Status: IN_PROGRESS; inherited facade, architecture, implementation, and build evidence are recorded in S00_L06.
- Owner: UNASSIGNED
- Date: UNSET
- Reviewer: USER-APPROVED

# Purpose

Implement one concept: compose Swerve Observation telemetry into the robot runtime.

# Frozen Architecture

Follow AGENTS.md, Document A, ES-06, Document C, and FAR. Preserve Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware and hardware -> IOInputs -> subsystem -> immutable Observation -> telemetry. Runtime telemetry must not control behavior or access hardware.

# Frozen Interfaces

- Preserve SwerveObservation immutability and SwerveTelemetryFacade.publish(SwerveObservation)/close() semantics.
- Preserve RobotContainer as the composition root.
- Preserve scheduler and subsystem lifecycle contracts. Robot.robotPeriodic() must run CommandScheduler.run() before RobotTelemetry.periodic().

# Dependencies

- Existing SwerveSubsystem observation accessor.
- Existing SwerveTelemetryFacade and WPILib NetworkTables publishers.
- WPILib robot lifecycle and CommandScheduler contracts.
- RobotTelemetry injects SwerveSubsystem and SwerveTelemetryFacade.
- Real selection uses four CTRE module IOs plus Pigeon2 IO; simulation selection uses deterministic Noop IOs.

# Scope

- Create RobotTelemetry as the runtime coordinator.
- RobotTelemetry.periodic() publishes only when SwerveSubsystem.getObservation() is present; Optional.empty() skips publication and never fabricates zeros.
- RobotContainer remains composition-only: select real or deterministic Noop IOs, create SwerveSubsystem, create NetworkTable "Swerve", create SwerveTelemetryFacade, and inject RobotTelemetry.
- Robot.robotPeriodic() performs one publish attempt per cycle after CommandScheduler.run().
- Noop IOs expose zero measurements, false health/connectivity, and no hardware behavior; no simulation physics are added.

# Out of Scope

IO or Observation changes, topic changes, logging, Glass layout changes, hardware behavior beyond inherited CTRE IO, simulation physics, kinematics, odometry, estimation, commands, controls, and motor behavior.

# Expected Files

- Create src/main/java/frc/robot/telemetry/RobotTelemetry.java.
- Create src/main/java/frc/robot/io/swerve/SwerveModuleIONoop.java.
- Create src/main/java/frc/robot/io/gyro/GyroIONoop.java.
- Modify only src/main/java/frc/robot/RobotContainer.java and src/main/java/frc/robot/Robot.java.
- Update LESSON_STATUS.md and LESSON_CHECKLIST.md with evidence only.
- A transition guide is deferred until implementation and verification are complete.

# Forbidden Files

Do not modify SwerveObservation, SwerveTelemetryFacade, IO interfaces, CTRE implementations, Constants.java, vendordeps, inherited transition guides, previous lessons, or governance documents. No Java files beyond the three created files and two modified files are allowed.

# Implementation Order

1. Confirm S00_L06 source context and user-verified S00_L07 baseline build.
2. Verify inherited facade, SwerveSubsystem Optional observation, Robot lifecycle, and D01 runtime pattern.
3. Implement deterministic SwerveModuleIONoop and GyroIONoop with zero measurements, false health/connectivity, and no hardware behavior.
4. Implement RobotTelemetry with injected SwerveSubsystem and SwerveTelemetryFacade.
5. Update RobotContainer for real CTRE/Pigeon2 or simulation Noop composition.
6. Update Robot.robotPeriodic() to run scheduler then one telemetry publish attempt.
7. Build and verify simulation startup, NT4/Glass topics, and secured real-robot read-only telemetry.
8. Complete documentation, delivery, and freeze steps.

# Verification Plan

- Static: RobotTelemetry consumes immutable Observations through the facade only; no IOInputs, hardware, vendor, command, control, or feedback dependency is introduced.
- Lifecycle: Robot.robotPeriodic() order is exactly CommandScheduler.run(), then RobotTelemetry.periodic(); one publish attempt occurs per cycle.
- Optional: Optional.empty() skips publication; no zero observation is fabricated.
- Composition: real uses four CTRE module IOs and Pigeon2 IO; simulation uses deterministic Noop IOs with zero measurements, false health/connectivity, and no hardware behavior.
- Safety: fail-fast exception behavior is preserved; explicit shutdown/close lifecycle remains deferred.
- Baseline Build: PASS (user-verified before S00_L07 Java changes).
- Build: run the clean Gradle build only after approved implementation and record the result in LESSON_STATUS.md.
- Simulation: verify startup with deterministic Noop IOs and no physics.
- Driver Station / Glass: verify NT4/Glass Swerve topics after runtime composition.
- Real Robot: verify secured read-only telemetry with inherited CTRE/Pigeon2 IO selection.

# Planned Technical Debt

- Explicit shutdown/close lifecycle remains deferred.
- NetworkTables, Glass, and secured real-robot integration require verification.
- Kinematics, odometry, pose estimation, simulation, commands, controls, and motor behavior remain deferred.

# Current Known Issues

- S00_L06 remains IN_PROGRESS because commit, push, and freeze evidence are not verified; inherited facade evidence is usable but not promoted to current-lesson completion.
- Noop IOs intentionally provide no simulation physics or motor behavior.

# Architecture Decision

- Reason: Compose inherited Swerve Observation telemetry into the robot runtime without changing control behavior.
- Scope: RobotTelemetry coordinator, deterministic Noop IOs, RobotContainer composition, Robot lifecycle ordering, Optional handling, and one publish attempt per cycle.
- Impact: Connects immutable Swerve observations to runtime telemetry while preserving RobotContainer composition, scheduler safety, IO abstraction, and control flow.
- Decision: APPROVED FOR IMPLEMENTATION
- Reviewer: USER-APPROVED
- Date: UNSET

Framework v2.1 remains the required documentation structure for this lesson.
