# Metadata

- Framework Version: 2.1
- Lesson: S00_L04_Swerve_Subsystem_Foundation
- Previous Lesson: S00_L03_CTRE_IO_Foundation
- Source: S00_L03_CTRE_IO_Foundation
- Owner: UNASSIGNED
- Date: UNSET
- Reviewer: USER-APPROVED

# Purpose

Implement one concept: SwerveSubsystem ownership and periodic IO refresh.

# Frozen Architecture

Follow AGENTS.md, Document A, ES-06, Document C, and FAR. The subsystem owns mechanism state and depends on vendor-neutral IO interfaces only.

# Frozen Interfaces

- Preserve `SwerveModuleIO`, `SwerveModuleIOInputs`, `GyroIO`, and `GyroIOInputs` unchanged.
- Store interface types only; never store CTRE implementation types.

# Dependencies

- WPILib `SubsystemBase`.
- Four `SwerveModuleIO` dependencies.
- One `GyroIO` dependency.
- No new vendor dependency.

# Scope

- Constructor:

  ```java
  SwerveSubsystem(
      SwerveModuleIO frontLeft,
      SwerveModuleIO frontRight,
      SwerveModuleIO backLeft,
      SwerveModuleIO backRight,
      GyroIO gyro)
  ```

- Own four `SwerveModuleIOInputs` snapshots and one `GyroIOInputs` snapshot.
- Call each `updateInputs(...)` exactly once from `periodic()`.
- Delegate `stop()` to all four module IO objects.

# Out of Scope

Observation, telemetry, commands, controls, RobotContainer wiring, simulation/Noop IO, kinematics, odometry, pose estimation, PID, offsets, inversions, steer-ratio conversion, neutral modes, current limits, and other hardware configuration.

# Expected Files

- Create `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Update `LESSON_STATUS.md` and `LESSON_CHECKLIST.md` with evidence only.
- Create the required transition guide during final documentation.

# Forbidden Files

Do not modify `Robot.java`, `RobotContainer.java`, `Constants.java`, IO interfaces, CTRE implementations, vendordeps, previous lessons, or the inherited transition guide.

# Implementation Order

1. Confirm baseline and audit evidence.
2. Confirm this decision is approved.
3. Implement the single subsystem file.
4. Verify source boundaries and safe stop delegation.
5. Build and record evidence in `LESSON_STATUS.md`.
6. Complete runtime verification, transition documentation, delivery, and freeze steps.

# Verification Plan

- Static: interface-only dependencies, five owned snapshots, one update call per dependency per cycle, and four delegated stops.
- Build: run the clean Gradle build and record the result only in `LESSON_STATUS.md`.
- Simulation: `NOT_APPLICABLE` unless a separately reviewed concrete implementation exists.
- Driver Station / Glass: `NOT_APPLICABLE` when no dashboard or telemetry is in scope, with rationale in status.
- Real Robot: limit verification to readback and safe stop with the robot secured.

# Planned Technical Debt

- Observation and telemetry contracts remain for a later lesson.
- Simulation/Noop strategy remains for a separately reviewed lesson.
- Kinematics, odometry, pose estimation, calibration, inversion, neutral modes, and current limits remain deferred.

# Architecture Decision

- Reason: Establish the next frozen responsibility boundary after S00_L03 IO.
- Scope: SwerveSubsystem ownership, periodic IOInputs refresh, and safe stop delegation.
- Impact: Adds one subsystem class; preserves all inherited interfaces, vendor isolation, and lifecycle files.
- Decision: APPROVED
- Reviewer: USER-APPROVED
- Date: UNSET

Framework v2.1 is ready to freeze for future lessons.
