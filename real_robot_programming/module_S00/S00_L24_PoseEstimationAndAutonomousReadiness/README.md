# S00_L24 - Pose Estimation and Autonomous Readiness

## Lesson State

- Status: `COMPLETE / FROZEN / READ-ONLY`
- Finalization state: `COMPLETE / FROZEN / READ-ONLY`
- Previous lesson: `S00_L23_OdometryAndPoseVisualization` - `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `PASS - COMPLETE L23 -> L24 EVOLUTION RECORDED`
- Git commit and push: user-owned; Git was not run by Codex

L24 is `COMPLETE / FROZEN / READ-ONLY`. The implementation and supplied
verification evidence are complete for the governed L24 scope; real-robot L24
verification remains explicitly HOLD pending hardware testing.

## Objective and Result

L24 extends the frozen L23 odometry architecture with the smallest
architecture-safe path toward pose estimation and autonomous readiness:

```text
hardware or simulation IOInputs
-> SwerveSubsystem
-> L23 SwerveDriveOdometry / currentPose()
-> L24 SwerveDrivePoseEstimator / getEstimatedPose()
-> immutable SwerveObservation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

The control path remains unchanged:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

The L24 implementation preserves the Frozen Backbone, the IO contracts, the
immutable observation boundary, and RobotContainer's composition-root role.

## Completed Learning Increments

### Increment 0 - L24 inheritance and architecture lock

The frozen L23 project was inherited as an independent L24 project. Generated
artifacts were cleaned, L23/L22 preservation was recorded, and the L24 boundary
was locked before production implementation.

### Increment 1 - Estimator contract design

The contract established subsystem-owned estimator state, coexistence with L23
odometry, the shared validated FL/FR/BL/BR measurement sample, invalid-sample
hold/recovery semantics, defensive pose access, WPILib default estimator
parameters, and no timestamp before vision exists.

### Increment 2 - Pose estimator foundation

`SwerveSubsystem` privately owns `SwerveDrivePoseEstimator`. It initializes and
updates it exactly once per valid subsystem periodic sample, holds state for
invalid measurements, and re-establishes a sensor baseline after an invalid
gap so unverified gap motion is not integrated. `getEstimatedPose()` returns a
defensive optional snapshot. L23 `currentPose()` remains odometry.

### Increment 3A - Estimated pose observation

`SwerveObservation` now carries an additive primitive-only
`EstimatedPoseObservation`. It is unavailable before estimator initialization,
retains the held pose with `measurementSampleValid=false` during an invalid
sample, and reports valid recovery without changing the meaning of
`currentPose()`.

### Increment 3B - Estimated pose telemetry

The read-only telemetry facade publishes estimated pose under its own namespace:

```text
/Swerve/EstimatedPose/Available
/Swerve/EstimatedPose/XMeters
/Swerve/EstimatedPose/YMeters
/Swerve/EstimatedPose/HeadingDegrees
/Swerve/EstimatedPose/MeasurementSampleValid
```

Existing `/Swerve/Pose/...` keys and the L23 odometry Field2d source retain
their original meaning.

### Increment 4A - Known-field-pose reset contract

The reset design was approved as a localization-frame reset of both odometry
and estimator. It requires a finite request, Disabled mode, a healthy gyro,
and one complete finite healthy FL/FR/BL/BR position sample. Physical sensors
are not reset.

### Increment 4B - Subsystem reset foundation

`SwerveSubsystem.resetKnownFieldPose(Pose2d)` validates one local snapshot and
resets both WPILib pose states with `resetPosition(...)`. Failure preserves
the previous localization state. A successful reset immediately updates both
direct pose APIs; the immutable observation reflects it on the next periodic
cycle. Unchanged encoder values produce no artificial movement.

### Increment 4C - One-shot reset command

`ResetKnownFieldPoseCommand` owns operator invocation semantics, requires the
swerve subsystem, runs while Disabled, rejects enabled execution, invokes the
subsystem reset once per schedule, and finishes after that attempt. The
subsystem remains the sole owner of reset validation and state mutation.

### Increment 4D - Learning starting pose and Glass trigger

The provisional learning pose is `Constants.FieldConstants.kLearningStartingPose`
(`Pose2d.kZero`), explicitly not an official competition coordinate. The
composition root registers the exact SmartDashboard/Glass command label:

```text
Reset Known Starting Pose
```

The command remains Disabled-only, the Back/View field-heading capture path is
unchanged, and the existing odometry Field2d remains the visualization source.

### Increment 5A - Autonomous-readiness contract audit

The minimum drivetrain boundary was defined without adding autonomous behavior:
estimated pose, measured robot-relative speeds, finite robot-relative
actuation, subsystem requirements, and centralized stop behavior. PathPlanner,
AutoBuilder, trajectories, path following, and autonomous routines remain out
of scope.

### Increment 5B - Measured robot-relative speeds

`getMeasuredRobotRelativeSpeeds()` derives an optional defensive
`ChassisSpeeds` from actual measured module velocity and angle in fixed
FL/FR/BL/BR order through the existing inverse kinematics. It rejects
incomplete, disconnected, unhealthy, nonfinite, or nonfinite-derived samples.
Requested, final-commanded, and measured speeds remain distinct.

### Increment 5C - Nonfinite request-boundary hardening

Robot-relative and field-relative public request APIs reject null or
nonfinite chassis-speed components without throwing. They use the existing
`stop()` authority, clear stale intent, disarm production output, stop all four
modules, and allow later finite requests to recover normally. Finite teleop
behavior and field-heading fail-closed behavior remain unchanged.

### Increment 5D - Autonomous command safety contract audit

The command-layer safety contract was audited and documented without creating a
dummy autonomous command. Future drivetrain commands must require the
subsystem, stop on completion/interruption/cancellation/timeout/invalid state,
and fail closed when localization or measured-speed prerequisites disappear.

### Increment 5E - Disabled-transition actuation disarm

The subsystem now disarms production chassis intent at the Disabled
periodic/output boundary. Disabled clears stale intent, zeros final module
states, and stops all four modules through the existing stop authority. After
Disabled -> Teleoperated/Autonomous, an old request cannot resume; motion
requires a new valid request.

Two deterministic/runtime defects were closed during the final L24 pass:

1. Stale actuation intent could survive Disabled and resume on re-enable. The
   Disabled transition disarm fixed this without changing teleop semantics.
2. The persistent dashboard reset command was permanently single-use because
   its attempted state survived scheduler lifecycles. The command is now
   one-shot per schedule, so the same dashboard instance can reset repeatedly.

The nine deprecated test calls to `Command.schedule()` were also replaced with
the repository-approved `CommandScheduler` form. No production warning cleanup
or behavior refactor was introduced.

## Verification Record

### Java

User-supplied Java 17 verification is complete:

- focused regression: `PASS`;
- full regression: `PASS`;
- clean build from a clean state, all tasks executed: `PASS`;
- deprecated `Command.schedule()` compiler warnings removed.

### Simulation / Glass

User verification is `PASS` for the implemented L24 scope:

- Pose and EstimatedPose availability and validity;
- agreement of Pose and EstimatedPose without vision;
- Reset Known Starting Pose #1;
- reuse of the same dashboard reset command for reset #2;
- accumulated raw sensor positions preserved after reset;
- post-reset localization continuity;
- Disabled -> Teleoperated with neutral input does not resume stale motion;
- fresh valid joystick input after re-enable resumes motion normally.

### Real Robot

L24-specific hardware verification remains `HOLD`. No real-robot estimator,
known-pose reset, or Disabled-transition safety PASS is claimed here.

L23 real-robot calibration, 3 m validation, odometry, NT4 pose telemetry, and
Field2d evidence remain documented in the frozen L23 guide and are not relabeled
as L24 evidence.

## Deferred Technical Debt

The following are intentionally deferred and are not L24 closure blockers:

- final drive PID/feedforward optimization beyond the provisional L23 baseline;
- ignored normal-drive CTRE `setControl` status handling, which needs a
  separately reviewed fail-closed policy;
- remaining VS Code unused-field diagnostics for retained CTRE diagnostic
  signals, connection-history caches, and dashboard ownership fields;
- real-robot L24 verification.

Vision uncertainty tuning, vision/AprilTag integration, timestamp/latency work,
known-field-pose workflow expansion, and competition-specific field handling
remain future work when separately authorized.

## Explicit L24 Boundary

L24 does not implement or require closure of:

- vision or AprilTag camera integration;
- `PathPlanner` or `AutoBuilder`;
- trajectory generation, path following, or autonomous routines;
- alliance transforms or field mirroring;
- an official competition starting-pose database;
- an estimator-specific Field2d replacement;
- hardware sensor reset, calibration changes, or final drive-gain tuning.

An actual autonomous command is not required for this lesson's readiness
contract objective. The lesson establishes the drivetrain contracts a future
autonomous layer will consume.

## Frozen History

L23 remains `COMPLETE / FROZEN / READ-ONLY`, and L22 remains historical,
complete, frozen, and untouched. The L23 final calibration guide remains the
authority for the verified `6.75` drive ratio, wheel geometry, signs, offsets,
hardware configuration, provisional gains, and L23 field validation.

## Finalization State

The five L24 documentation files were reconciled with the implemented source
and supplied verification evidence. L24 is now `COMPLETE / FROZEN / READ-ONLY`.

See [S00_L23 to S00_L24 Step-by-Step Transition Guide](docs/S00_L23_to_S00_L24_Step_by_Step.md)
for the complete chronological evolution.
