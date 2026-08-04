# S00_L10 SwerveDriveKinematics Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L10_SwerveDriveKinematics_Foundation
- Previous Lesson: S00_L09_ChassisSpeeds_Foundation
- Previous Lesson Status: COMPLETE / FROZEN
- Source: S00_L09_ChassisSpeeds_Foundation
- Status: COMPLETE
- Freeze: FROZEN / READ-ONLY
- Architecture Review: PASS
- Date: 2026-08-04
- Reviewer: Architecture Review

## Exact Lesson Goal

Convert robot-relative WPILib `ChassisSpeeds` into four ordered `SwerveModuleState` values using the verified robot geometry, without consuming the values in a subsystem or producing hardware output.

## Architecture Decision

The smallest compliant location is one new pure class, `frc.robot.subsystems.SwerveKinematics`. It owns four `Translation2d` module locations and one `SwerveDriveKinematics`. Its only behavior is conversion through `toModuleStates(ChassisSpeeds)`.

No change to `SwerveSubsystem`, IO, observations, telemetry, RobotContainer, controls, commands, or hardware is required or authorized.

## Geometry and Ordering

Use `Constants.SwerveConstants.kWheelbaseMeters` and `kTrackWidthMeters`, divided by two. WPILib coordinates are used: +X forward and +Y left.

1. Front Left: `(+wheelbase/2, +trackwidth/2)`
2. Front Right: `(+wheelbase/2, -trackwidth/2)`
3. Back Left: `(-wheelbase/2, +trackwidth/2)`
4. Back Right: `(-wheelbase/2, -trackwidth/2)`

The constructor order and returned state order are fixed to FL, FR, BL, BR.

## Dependency and Ownership Rules

- `Constants.java` remains the configuration authority.
- `SwerveKinematics` uses WPILib value and kinematics types only.
- No hardware, vendor API, NetworkTables, telemetry, scheduler, or control dependency is introduced.
- The helper does not retain or modify caller-owned chassis speeds.

## Verification Plan

Focused tests passed 7/7: zero speeds, positive robot-forward translation, positive robot-left translation, positive counterclockwise rotation, combined translation and rotation, deterministic module ordering, and null rejection. The full `gradlew build` also passed.

Simulation, Glass, Driver Station, and Real Robot are NOT APPLICABLE because the lesson adds no runtime wiring, actuator path, or telemetry consumer.

## Forbidden Scope

Field-relative driving, discretization, desaturation, optimization, odometry, pose estimation, closed-loop control, motor output, telemetry, IO changes, RobotContainer changes, commands, controls, autonomous behavior, vendor APIs, and hardware configuration.

## Technical Debt and Unresolved Decisions

Future field-relative conversion, discretization policy, desaturation, optimization, odometry, pose estimation, and runtime integration remain deferred. Commit, push, and freeze verification are PASS; the lesson is frozen read-only.
