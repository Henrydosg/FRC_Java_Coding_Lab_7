# S00_L24 Pose Estimation and Autonomous Readiness - Lesson Plan

## Lesson Metadata

- Lesson: `S00_L24_PoseEstimationAndAutonomousReadiness`
- Previous: `S00_L23_OdometryAndPoseVisualization` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Finalization state: `COMPLETE / FROZEN / READ-ONLY`
- Architecture review: `PASS`
- Transition guide: `PASS - COMPLETE L23 -> L24 EVOLUTION RECORDED`
- Git: user-owned; not run by Codex

## Learning Objective

Extend the frozen L23 odometry architecture with a subsystem-owned pose
estimator, an additive estimated-pose observation/telemetry path, a safe
known-field-pose reset, and the minimum drivetrain contracts required by a
future autonomous layer. Preserve L23 odometry meaning, L22 field-relative
semantics, the Frozen Backbone, and all IO contracts.

L24 establishes autonomous readiness contracts; it does not implement an
autonomous routine.

## Locked Architecture

Control:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

Observation:

```text
hardware / simulation
-> IOInputs
-> SwerveSubsystem
-> L23 SwerveDriveOdometry / currentPose()
-> L24 SwerveDrivePoseEstimator / estimatedPose
-> immutable SwerveObservation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

RobotContainer remains composition, dependency injection, implementation
selection, default-command, binding, and dashboard registration only.

## Completed Learning Sequence

### Increment 0 - Inheritance and baseline normalization

Inherited L23 into the independent L24 project, cleaned generated artifacts,
confirmed L23/L22 frozen integrity, and recorded the L24 architecture boundary.

### Increment 1 - Estimator contract design

Approved estimator ownership by `SwerveSubsystem`, coexistence with odometry,
shared validated field-heading and FL/FR/BL/BR measurements, exact-once
periodic updates, invalid hold/recovery, defensive API semantics, WPILib
default parameters, and no timestamp before vision.

### Increment 2 - Estimator foundation

Implemented and tested private `SwerveDrivePoseEstimator` ownership. The
estimator initializes only from a valid complete sample, updates once per valid
periodic sample, holds on invalid samples, and re-baselines on recovery so
unverified invalid-gap motion is not integrated. `currentPose()` remains L23
odometry and `getEstimatedPose()` is additive.

### Increment 3A - Estimated-pose observation

Added immutable primitive-only `EstimatedPoseObservation` to `SwerveObservation`.
Unavailable, valid, held-invalid, and valid-recovery semantics were tested
without adding timestamps or changing `currentPose()`.

### Increment 3B - Estimated-pose telemetry

Published estimated pose through the existing read-only telemetry facade under
`/Swerve/EstimatedPose/...`. Existing odometry pose keys and the L23 Field2d
meaning were preserved.

### Increment 4A - Reset contract design

Defined a Disabled-only localization-frame reset of odometry and estimator
using one validated heading and one fixed-order FL/FR/BL/BR module-position
snapshot. Physical drive, steer, CANcoder, and gyro sensors are not reset.

### Increment 4B - Reset foundation

Implemented `resetKnownFieldPose(Pose2d)` with finite/health/initialization
validation, identical `resetPosition(...)` inputs for both pose owners,
failure preservation, immediate direct-pose semantics, and next-periodic
observation semantics.

### Increment 4C - Reset command layer

Implemented the one-shot Disabled-only command wrapper. It requires the
subsystem, calls the subsystem reset exactly once per schedule, finishes after
the attempt, and contains no duplicated validation or drive behavior.

### Increment 4D - Learning starting pose and dashboard trigger

Defined the provisional `Pose2d.kZero` learning pose in `Constants.FieldConstants`
and registered `Reset Known Starting Pose` through the existing SmartDashboard/
Glass command path. No competition field database or autonomous architecture
was introduced.

### Increment 5A - Autonomous-readiness contract audit

Audited the boundary between requested, final-commanded, and measured speeds;
confirmed existing robot-relative actuation and centralized stop semantics;
and defined the future command lifecycle contract without adding autonomous
behavior.

### Increment 5B - Measured robot-relative speeds

Added validated, measurement-derived `Optional<ChassisSpeeds>
getMeasuredRobotRelativeSpeeds()` using existing inverse kinematics and exact
FL/FR/BL/BR order. Incomplete, unhealthy, disconnected, or nonfinite samples
fail closed.

### Increment 5C - Nonfinite request boundary

Hardened both public chassis-speed request paths. Null/nonfinite input stops all
modules, clears/disarms stale intent, returns without throwing, and permits
recovery only through a later finite request. Normal finite behavior is
unchanged.

### Increment 5D - Autonomous command safety audit

Defined completion, interruption, cancellation, timeout, invalid-localization,
invalid-measured-speed, invalid-output, and mode-transition stop requirements.
No dummy autonomous command was added.

### Increment 5E - Disabled-transition disarm

Added the subsystem boundary invariant that Disabled cannot retain armed
production chassis intent. The periodic/output boundary clears intent, zeros
final states, and uses the existing stop authority. Re-enable requires a new
valid request.

The final runtime pass also corrected the persistent reset command lifecycle:
the same dashboard command instance is now one-shot per schedule rather than
one-shot per object lifetime. Both defects have deterministic regression and
user Simulation/Glass evidence.

### Final warning cleanup

Replaced the nine deprecated test-only `Command.schedule()` calls with
`CommandScheduler` scheduling. Remaining VS Code unused-field diagnostics and
the ignored normal-drive CTRE `StatusCode` are documented deferred technical
debt; production code was not changed for warning count reduction.

## Acceptance and Verification Plan

The following supplied evidence is complete:

| Gate | Result | Evidence |
|---|---|---|
| Focused Java regression | PASS | User-supplied Java 17 focused verification |
| Full Java regression | PASS | User-supplied Java 17 full verification |
| Clean build | PASS | User-supplied Java 17 clean build; all tasks executed |
| Pose / EstimatedPose availability and validity | PASS | Simulation/Glass |
| Pose / EstimatedPose agreement without vision | PASS | Simulation/Glass |
| Reset #1 and reset #2 using same dashboard command | PASS | Simulation/Glass |
| Sensor baseline preserved across reset | PASS | Simulation/Glass |
| Post-reset continuity | PASS | Simulation/Glass |
| Disabled -> Teleop neutral does not resume stale motion | PASS | Simulation/Glass |
| Fresh valid request recovers after re-enable | PASS | Simulation/Glass |
| L24 real-robot verification | PASS | User-supplied ten-case localization/reset hardware evidence |

### Real-Robot Verification Amendment

The user supplied the following L24 hardware evidence:

1. Hardware / Disabled baseline: four swerve modules and Pigeon connected and
   configuration-healthy; drive/steer outputs and velocities zero while
   stationary; EstimatedPose available and measurement-valid.
2. Stationary Pose/Estimator: Pose and EstimatedPose available/valid, X/Y
   effectively stationary, heading drift very small, and mutual consistency.
3. Translation tracking: approximately 0.67 m forward; X approximately
   `+0.671 m`, Y near zero, heading near zero.
4. Rotation tracking: approximately 46 degrees tracked; X/Y changed only by a
   few millimeters.
5. Combined translation and rotation: both poses remained available/valid,
   changed consistently, and remained mutually consistent.
6. Disabled known-field-pose reset: accepted; both poses reset atomically to
   approximately `(0 m, 0 m, 0 deg)`; physical gyro state was not reset; no
   drivetrain motion occurred.
7. Enabled reset rejection: after moving away from zero, Enabled reset was
   rejected; neither pose reset to zero and localization remained valid.
8. Reset followed by translation: approximately 0.53 m forward; X approximately
   `+0.526 m`, Y near zero, heading near zero.
9. Reset followed by rotation: heading approximately `-88.1 deg`; X/Y within
   a few millimeters of zero.
10. Combined motion after reset: user explicitly confirmed `PASS`; no additional
    numerical measurements are inferred.

Odometry Pose currently uses module measurements plus gyro heading. EstimatedPose
remains very close to or equal to Pose because no vision measurement is fused
yet. Vision/AprilTag fusion was not tested here and remains deferred.

This amendment validates only the existing L24 localization and reset
foundation. It does not claim final competition localization accuracy,
autonomous path following, PathPlanner, AutoBuilder, or A01 starting-pose
readiness.

The final architecture review confirmed this record. No new production feature
is required by this documentation finalization step.

## Explicitly Out of Scope

The following remain outside L24 and must not be added merely to satisfy the
word “autonomous”:

- vision or AprilTag camera integration;
- pose-estimator vision fusion and uncertainty tuning;
- timestamp/latency support before a proven need;
- PathPlanner or AutoBuilder;
- trajectory generation, path following, or autonomous routines;
- alliance transforms or field mirroring;
- official competition starting coordinates or pose databases;
- estimator-specific Field2d replacement;
- hardware sensor reset, drivetrain calibration, or final drive-gain tuning.

## Deferred Technical Debt

- Final PID/feedforward optimization remains deferred from L23's provisional
  drive baseline.
- Normal-drive Phoenix `setControl` status handling needs a separate
  fail-closed design review.
- Retained CTRE diagnostic signals, connection-history fields, and dashboard
  ownership fields may continue to produce IDE unused-field diagnostics.
- Final competition localization accuracy and tuning remain outside L24.

These items do not change the verified software/Simulation scope. Real-robot
evidence remains visibly separate from software PASS evidence.

## Finalization Decision

Current status is `COMPLETE / FROZEN / READ-ONLY`.
L23 remains `COMPLETE / FROZEN / READ-ONLY`; L22 remains untouched.
