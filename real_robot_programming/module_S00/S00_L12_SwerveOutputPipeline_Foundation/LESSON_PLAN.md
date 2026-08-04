# S00_L12 Swerve Output Pipeline Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L12_SwerveOutputPipeline_Foundation
- Previous Lesson: S00_L11_SwerveModuleStateOptimization_Foundation
- Previous Lesson Status: COMPLETE / FROZEN
- Source: S00_L11_SwerveModuleStateOptimization_Foundation
- Status: IN_PROGRESS
- Freeze: NOT TESTED
- Architecture Review: PASS
- Date: 2026-08-04
- Reviewer: Architecture Review

## Exact Lesson Goal

Convert robot-relative `ChassisSpeeds` and four current module angles into four final `SwerveModuleState` values by applying existing kinematics, existing per-module optimization, and WPILib wheel-speed desaturation in that order.

## Architecture Decision

The smallest compliant addition is one stateless class, `frc.robot.subsystems.SwerveOutputPipeline`. It owns no mechanism state and accepts every input explicitly. Its `toModuleStates(ChassisSpeeds, Rotation2d[])` method performs the pipeline in this fixed order: robot-relative chassis speeds through `SwerveKinematics`, per-module optimization through `SwerveModuleStateOptimizer`, then WPILib's supported `SwerveDriveKinematics.desaturateWheelSpeeds` API. The returned state order remains FL, FR, BL, BR.

The pipeline returns newly allocated states and does not retain or mutate caller-owned inputs. No changes to `SwerveSubsystem`, RobotContainer, commands, IO, hardware, telemetry, observations, odometry, pose estimation, gyro logic, or motor output are part of this lesson.

## Constant Decision

The inherited S00_L11 `Constants.java` contains wheel geometry but no maximum wheel-speed limit. Add only `Constants.SwerveConstants.kMaxWheelSpeedMetersPerSecond`, configured to `4.0` meters per second. The 4.0 m/s value is a provisional software baseline, not a verified hardware capability, and requires later hardware validation.

## Validation Contract

- Null chassis speeds, null angle arrays, and null angle elements are rejected.
- The current-angle array must contain exactly four entries.
- An explicit maximum wheel speed must be positive and finite.
- Output order is always Front Left, Front Right, Back Left, Back Right.
- Optimization occurs before desaturation so negative optimized speed direction is preserved while all absolute wheel speeds are scaled to the configured limit.

## Focused Test Plan

`SwerveOutputPipelineTest` covers zero chassis speeds, normal motion below the limit, proportional desaturation, the final absolute speed limit, optimized direction reversal through desaturation, combined translation and rotation, deterministic ordering, null chassis speeds, null current-angle inputs, wrong array length, invalid explicit maximum speeds, and caller-owned input immutability. Focused verification passed 13/13.

## Verification Record

- Architecture Review: PASS.
- Implementation: PASS.
- Baseline build: PASS based on the user's current S00_L11/S00_L12 state report.
- Focused tests: PASS - 13/13.
- Full build: PASS.
- Simulation, Glass, Driver Station, and Real Robot: NOT APPLICABLE; no runtime wiring or hardware-output path was added.
- Documentation: PASS.
- Commit, Push, and Freeze: NOT TESTED; no Git operations performed.

## Remaining Completion Steps

User-owned Git commit, push, and lesson freeze remain NOT TESTED. The provisional 4.0 m/s software baseline also requires later hardware validation.

## Forbidden Scope

Do not add field-relative conversion, discretization, cosine compensation, PID, motor output, runtime wiring, telemetry, command scheduling, IO behavior, hardware configuration, or changes to prior frozen lessons.
