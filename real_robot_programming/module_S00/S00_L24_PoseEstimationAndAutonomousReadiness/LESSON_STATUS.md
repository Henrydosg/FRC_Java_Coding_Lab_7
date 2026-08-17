# Lesson Status

## Identity

- Lesson: `S00_L24_PoseEstimationAndAutonomousReadiness`
- Previous Lesson: `S00_L23_OdometryAndPoseVisualization`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: extend frozen L23 odometry with subsystem-owned pose estimation, safe known-field-pose reset, and the minimum drivetrain contracts required by a future autonomous layer without changing L23 meaning or L22 field-relative control.
- Architecture Review: `PASS`
- Transition Guide: `FINAL / PASS`
- Next Roadmap Lesson: `TBD`
- Git: user-owned; not run by Codex

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| Inheritance: copy and rename L23 -> L24 | PASS | Independent L24 project inherited from frozen L23. |
| Generated artifacts cleaned | PASS | Inherited L24 generated artifacts were cleaned. |
| Baseline Java 17 build | PASS | User-supplied baseline evidence. |
| Java focused regression | PASS | User-supplied Java 17 focused verification after L24 implementation. |
| Java full regression | PASS | User-supplied Java 17 full regression. |
| Clean build | PASS | User-supplied Java 17 clean build; all tasks executed from clean state. |
| Simulation | PASS | Pose/EstimatedPose, reset, continuity, and Disabled-transition behavior verified by user. |
| Driver Station / Glass | PASS | Dashboard reset reuse and localization behavior verified by user. |
| Real Robot | PASS | User-supplied ten-case L24 localization/reset hardware verification. |
| Transition Guide | PASS | Complete chronological L23 -> L24 evolution is recorded. |
| Git Status | NOT RUN | Git is user-owned and was not run by Codex. |
| Git Commit | NOT TESTED | User-owned. |
| Git Push | NOT TESTED | User-owned. |

## Implemented L24 Capability

- Subsystem-owned `SwerveDrivePoseEstimator` coexists with L23 odometry.
- `currentPose()` remains the L23 odometry pose; estimated pose is separate.
- Immutable estimated-pose observation and `/Swerve/EstimatedPose/...` telemetry are additive.
- Known-field-pose reset updates odometry and estimator together without resetting physical sensors.
- Disabled-only `ResetKnownFieldPoseCommand` uses the subsystem reset API.
- `Reset Known Starting Pose` uses provisional `Pose2d.kZero` and is reusable per schedule.
- `getMeasuredRobotRelativeSpeeds()` reports validated measured module-derived speeds.
- Null/nonfinite robot-relative and field-relative requests fail closed.
- Disabled disarms stale production chassis intent and requires a new valid request after re-enable.
- Centralized `stop()` remains the drivetrain stop authority.
- Autonomous-readiness command lifecycle requirements are defined; no autonomous command is implemented.

## Runtime Defect Closure

### Stale actuation across Disabled

Resolved in Increment 5E. At the Disabled subsystem periodic/output boundary,
stored production intent is cleared, final module states are zeroed, and all
four modules are stopped. A previous request cannot resume after re-enable;
only a new valid request can arm motion.

### Persistent dashboard reset command

Resolved after runtime audit. `ResetKnownFieldPoseCommand` is one-shot per
schedule rather than one-shot per object lifetime. The same dashboard command
instance can perform reset #1 and reset #2 in one Simulation/Glass session.

## Simulation / Glass Evidence

User verification is PASS for the implemented L24 scope:

- Pose and EstimatedPose availability/validity;
- agreement without vision;
- Reset Known Starting Pose #1;
- reuse of the same dashboard command for reset #2;
- accumulated raw sensor positions after reset;
- post-reset continuity;
- neutral Disabled -> Teleoperated transition remains stopped;
- fresh valid joystick request resumes motion normally.

## Real-Robot Evidence Amendment

User-supplied L24 real-robot verification is `PASS` for the existing
localization and reset foundation only. Odometry Pose uses measured module
positions plus gyro heading. EstimatedPose remains very close to or equal to
Pose because no vision measurement is fused yet.

1. Hardware / Disabled baseline: four modules and Pigeon were connected and
   configuration-healthy; drive/steer outputs and velocities were zero while
   stationary; EstimatedPose was available and measurement-valid.
2. Stationary pose/estimator: Pose and EstimatedPose remained available and
   valid; X/Y stayed effectively stationary; heading drift was very small; both
   poses remained mutually consistent.
3. Translation tracking: approximately 0.67 m forward; X was approximately
   `+0.671 m`; Y and heading remained near zero in both poses.
4. Rotation tracking: approximately 46 degrees was tracked; X/Y changed only
   by a few millimeters.
5. Combined translation and rotation: both poses remained available and valid,
   changed consistently, and remained mutually consistent.
6. Disabled known-field-pose reset: accepted; Pose and EstimatedPose reset
   atomically to approximately `(0 m, 0 m, 0 deg)`; physical gyro state was not
   reset; no drivetrain motion occurred.
7. Enabled reset rejection: after motion away from zero, Enabled reset was
   rejected; neither pose reset to zero and localization remained valid.
8. Reset followed by translation: approximately 0.53 m forward was tracked as
   X approximately `+0.526 m`; Y and heading remained near zero.
9. Reset followed by rotation: heading tracked approximately `-88.1 deg`; X/Y
   remained within a few millimeters of zero.
10. Combined motion after reset: user explicitly confirmed `PASS`; no additional
    numerical measurements are added.

The reset evidence confirms Disabled-only acceptance, Enabled rejection,
atomic odometry/estimator localization update, preservation of physical
gyro/module sensor state, and no drivetrain motion caused by reset.

Vision/AprilTag fusion was not tested. This evidence does not claim final
competition localization accuracy, autonomous path following, PathPlanner,
AutoBuilder, or A01 starting-pose readiness.

## Architecture Map

```text
hardware / simulation
-> vendor-neutral IOInputs
-> SwerveSubsystem
-> L23 SwerveDriveOdometry / currentPose()
-> L24 SwerveDrivePoseEstimator / estimatedPose
-> immutable SwerveObservation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

The control path remains:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

## Preserved L23/L22 Contract

L23 remains `COMPLETE / FROZEN / READ-ONLY`. L22 remains historical,
complete, frozen, and untouched. L23 module ordering, measurement semantics,
field-heading semantics, hardware calibration, verified drive ratio `6.75`,
provisional gains, odometry pose meaning, observation boundary, telemetry, and
Field2d path remain unchanged.

## Deferred Technical Debt and Remaining Items

- Final competition localization accuracy and tuning remain outside L24.
- Final drive PID/feedforward optimization remains deferred; provisional L23 gains are unchanged.
- Normal-drive CTRE `setControl` status handling remains deferred robustness debt.
- Retained CTRE diagnostic signals, connection-history fields, and dashboard ownership fields may remain IDE unused-field diagnostics.
- Vision/AprilTag integration, timestamps/latency, uncertainty tuning, PathPlanner/AutoBuilder, trajectories, autonomous routines, alliance transforms, and competition-specific starting poses are outside this lesson.

These remain separate from the software and Simulation/Glass verification
record and do not change the frozen L24 scope.

## Documentation Result

The following documentation-only finalization changes are complete:

- `README.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `LESSON_STATUS.md`
- `docs/S00_L23_to_S00_L24_Step_by_Step.md`

All five now describe the implemented increments, supplied verification, the
two runtime defect fixes, deferred work, and the L23/L24 boundary.

## Current State

L24 is `COMPLETE / FROZEN / READ-ONLY`.
