# S00_L09 ChassisSpeeds Foundation - Architecture Audit Round 2

## Metadata

- Framework Version: 2.1
- Lesson: S00_L09_ChassisSpeeds_Foundation
- Previous Lesson: S00_L08_Swerve_Module_State_Foundation
- Previous Lesson Status: COMPLETE / FROZEN
- Previous Lesson Completion Commit: `573c814`
- Documentation Correction Commit: `3efb143`
- Source: S00_L08_Swerve_Module_State_Foundation
- Status: COMPLETE
- Freeze: FROZEN / READ-ONLY
- Architecture Review: PASS - implementation follows the approved architecture lock
- Date: 2026-08-04
- Reviewer: UNASSIGNED

## Repository Evidence

- S00_L08 is COMPLETE and FROZEN, with measured `SwerveModuleState` interpretation only.
- `SwerveSubsystem` owns mechanism state and already sits between IOInputs and immutable observations.
- `SwerveObservation` is immutable measured state; it must not carry command intent.
- `RobotTelemetry` consumes observations only.
- `RobotContainer` is the composition root and currently contains no chassis-velocity behavior.
- `build.gradle` already provides WPILib; no dependency change is needed.

## Single-Concept Decision

The candidate goal is valid as one concept when limited to accepting and retaining robot-relative chassis velocity intent. The implementation does not turn `ChassisSpeeds` into a control pipeline, kinematics boundary, observation, telemetry value, or hardware command.

## Ownership and API Boundary

`SwerveSubsystem` is the correct owner for the current chassis intent because subsystems own mechanism state. The implemented boundary is `acceptChassisSpeeds(ChassisSpeeds speeds)`. It accepts intent only; it does not calculate module states, command IO, publish telemetry, or process driver input. Commands and controls remain future callers and are not added in this lesson.

## Mutability and Internal Storage

The subsystem must never retain the caller's `ChassisSpeeds` reference. The method must reject null and copy all three scalar fields immediately: `vxMetersPerSecond`, `vyMetersPerSecond`, and `omegaRadiansPerSecond`. Internal storage should be three private primitive `double` fields, or one private immutable record containing those three doubles. Do not store a WPILib `ChassisSpeeds` instance. Any future read API must return copied values or an immutable project-owned view, not the mutable WPILib object.

## Exact Semantics

- Frame: robot-relative; +X is robot forward, +Y is robot left, and positive omega is counterclockwise, following WPILib chassis convention.
- Units: `vx` and `vy` are meters per second; `omega` is radians per second.
- Zero intent: `(0.0, 0.0, 0.0)` means no requested chassis motion and is the initialized state.
- `stop()`: clear stored intent to zero, then execute the existing safe module stop behavior. It must not introduce a new output path.
- `periodic()`: refresh existing IOInputs and publish the existing immutable observation exactly as before; do not consume intent, convert it, schedule commands, or write hardware.

## Exact Java Scope

- Modify: `src/main/java/frc/robot/subsystems/SwerveSubsystem.java`.
- Create: no new production Java file is required.
- Minimum verification artifact: `src/test/java/frc/robot/subsystems/SwerveSubsystemTest.java`, if the implementation is approved. The focused test must prove scalar copy isolation, robot-relative field mapping, zero initialization, `stop()` zeroing, and periodic non-actuation.
- Do not modify `Constants.java`, `RobotContainer.java`, IO, observations, telemetry, controls, commands, Gradle, vendordeps, or hardware configuration.

## Approved Architecture Lock

The subsystem acceptance method copies three scalar values into a private immutable nested record; no `ChassisSpeeds` reference crosses the method boundary into retained state; no consumer or actuator path is added.

## Verification Plan

- Static: no retained `ChassisSpeeds` field, no vendor/hardware/telemetry/control dependency, and no change to observation flow.
- Copy isolation: mutate the caller object after acceptance and prove stored values do not change.
- Semantics: verify field-to-scalar mapping, signs, units, and zero initialization.
- Lifecycle: verify `stop()` zeros intent and `periodic()` does not convert or output it.
- Regression: user verified 5/5 `SwerveSubsystem` tests passed and `gradlew build` returned `BUILD SUCCESSFUL in 1m 11s`.

## Forbidden Scope

Field-relative conversion; discretization; kinematics; module-state conversion; odometry; pose estimation; controls; commands; bindings; autonomous; telemetry; IO; motor output; vendor APIs; hardware configuration; and changes to frozen lessons.

## Risks, Dependencies, and Technical Debt

- `ChassisSpeeds` is mutable, so retaining it would create an aliasing defect.
- A public getter returning `ChassisSpeeds` could reintroduce mutability; it is not part of the minimum scope.
- Coordinate-frame and sign semantics must remain explicitly robot-relative and WPILib-compatible.
- Future kinematics ownership, field-relative conversion, discretization policy, and command scheduling remain unresolved.
- The stored intent has no runtime consumer in S00_L09 and therefore has no physical effect.

## Architecture Decision

- Reason: establish a safe chassis-intent boundary before later drivetrain behavior.
- Scope: accept and copy robot-relative chassis velocity intent only.
- Impact: one subsystem API and one focused verification artifact; no control, observation, telemetry, IO, or hardware impact.
- Decision: APPROVED, IMPLEMENTED, VERIFIED, and FROZEN for this lesson scope.
