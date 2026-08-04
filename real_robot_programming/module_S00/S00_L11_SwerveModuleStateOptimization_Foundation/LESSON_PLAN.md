# S00_L11 SwerveModuleState Optimization Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L11_SwerveModuleStateOptimization_Foundation
- Previous Lesson: S00_L10_SwerveDriveKinematics_Foundation
- Previous Lesson Status: COMPLETE / FROZEN
- Source: S00_L10_SwerveDriveKinematics_Foundation
- Status: COMPLETE
- Freeze: FROZEN / READ-ONLY
- Architecture Review: PASS
- Date: 2026-08-04
- Reviewer: Architecture Review

## Exact Lesson Goal

Optimize one desired `SwerveModuleState` against one current `Rotation2d` using WPILib's supported optimization API, without hardware, subsystem state, or side effects.

## Architecture Decision

The smallest compliant location is one new stateless class, `frc.robot.subsystems.SwerveModuleStateOptimizer`. Its API is `optimize(SwerveModuleState desiredState, Rotation2d currentAngle)`. It copies the desired state, calls the instance method `SwerveModuleState.optimize(currentAngle)`, and returns the optimized copy.

The deprecated static WPILib overload is not used. No change to `SwerveSubsystem`, `SwerveKinematics`, IO, observations, telemetry, RobotContainer, controls, commands, or hardware is required or authorized.

## Optimization Semantics

- A target within 90 degrees keeps the requested speed direction.
- A target beyond 90 degrees reverses speed and rotates the target angle by 180 degrees through WPILib.
- Positive and negative wraparound near +/-180 degrees follows WPILib's `Rotation2d` behavior.
- Exactly 90 degrees follows WPILib's current boundary behavior and keeps speed direction.
- The desired input object is not mutated; the returned state is a separate object.

## Dependency and Ownership Rules

- The helper is vendor-neutral and stateless.
- It depends only on WPILib `SwerveModuleState` and `Rotation2d`.
- It does not access hardware, IO, subsystem state, telemetry, scheduler, or control behavior.
- Existing FL/FR/BL/BR ordering remains unchanged because this lesson optimizes one state only.

## Verification Plan

Focused tests passed 8/8: null desired state, null current angle, within-90-degree behavior, beyond-90-degree reversal, positive and negative wraparound, exactly-90-degree behavior, and input immutability. The full `gradlew build` also passed.

Simulation, Glass, Driver Station, and Real Robot are NOT APPLICABLE because the lesson adds no runtime wiring, actuator path, or telemetry consumer.

## Forbidden Scope

Kinematics, FL/FR/BL/BR ordering changes, field-relative driving, desaturation, cosine compensation, PID, motor control, IO, telemetry, commands, controls, autonomous behavior, odometry, pose estimation, vendor APIs, and hardware configuration.

## Technical Debt and Unresolved Decisions

Runtime integration, desired-state scheduling, closed-loop output, cosine compensation, desaturation, and downstream drivetrain behavior remain deferred. Commit `18d308b`, push, and freeze are verified PASS.
