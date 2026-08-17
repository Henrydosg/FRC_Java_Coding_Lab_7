# S00_L23 to S00_L24 Step-by-Step Transition Guide

## Status

`FINAL / PASS`

L24 is `COMPLETE / FROZEN / READ-ONLY`.

## Lesson Identity and Authority

- Source: `S00_L23_OdometryAndPoseVisualization` - `COMPLETE / FROZEN / READ-ONLY`
- Current: `S00_L24_PoseEstimationAndAutonomousReadiness` - `COMPLETE / FROZEN / READ-ONLY`
- L22: historical, complete, frozen, and untouched
- L23 reference: [Final Swerve Calibration and Commissioning Guide](../../S00_L23_OdometryAndPoseVisualization/docs/S00_L23_Final_Swerve_Calibration_and_Commissioning_Guide.md)
- Git: user-owned; not run by Codex

The L23 source, tests, configuration, calibration values, and documentation
remain frozen history. L24 adds only the approved estimator/readiness layers
and their safety contracts.

## Frozen Architecture Preserved

Control:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

Observation:

```text
hardware / simulation
-> IOInputs
-> SwerveSubsystem
-> odometry / estimator
-> immutable SwerveObservation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

`SwerveSubsystem` owns mechanism and localization state. Observation models
remain immutable, primitive-only, vendor-neutral read models. Telemetry is
read-only. RobotContainer remains the composition root.

## Step 0 - Inherit and normalize the L24 baseline

### Objective

Create the independent L24 lesson from frozen L23 and establish its identity.

### Why

Each lesson is an independent inherited project, and frozen lessons must not
be rewritten.

### Action

Inherited L23 into `S00_L24_PoseEstimationAndAutonomousReadiness`, cleaned the
inherited generated artifacts, recorded the architecture audit, and preserved
L23/L22 history.

### Files Changed

L24 lesson metadata and transition documentation only; no L23 files, Java, IO
contracts, Gradle files, hardware configuration, or calibration values.

### Verification

User supplied Java 17 baseline clean build: `PASS`. L23 was confirmed
`COMPLETE / FROZEN / READ-ONLY`; the L24 architecture audit was `PASS`.

### Expected Result

L24 begins as an editable independent lesson with no change to L23 or L22.

## Increment 1 - Estimator contract design

### Objective

Define the minimum estimator contract before implementation.

### Why

Estimator ownership, continuity, validity, and coexistence must be explicit
before adding a second localization state.

### Action

Approved subsystem-owned `SwerveDrivePoseEstimator`, shared accepted field
heading and FL/FR/BL/BR module positions, exact-once periodic updates,
invalid hold/recovery, defensive pose access, WPILib default estimator
parameters, and no speculative timestamp before vision.

### Files Changed

Architecture/lesson design records only; no production or test Java change.

### Verification

Estimator architecture audit approved the design.

### Expected Result

Implementation can add estimated pose without changing L23 `currentPose()`.

## Increment 2 - Subsystem-owned pose estimator

### Objective

Add and verify the estimator foundation.

### Why

L24 needs an independent estimated pose while preserving the validated L23
odometry pose for direct comparison.

### Action

`SwerveSubsystem` privately owns `SwerveDrivePoseEstimator`. It initializes from
one complete valid measurement sample, updates once per valid periodic sample,
holds on invalid data, and re-baselines after an invalid gap so unverified gap
motion is not integrated. `getEstimatedPose()` returns a defensive optional
snapshot.

### Files Changed

`SwerveSubsystem.java` and focused subsystem estimator tests.

### Verification

Estimator-focused tests, L23 odometry regression, and the Java 17 clean build
were user-verified `PASS`.

### Expected Result

Odometry and estimator coexist; without vision they track identical valid
inputs consistently.

## Increment 3A - Estimated pose observation

### Objective

Expose estimated pose through the immutable observation boundary.

### Why

Telemetry must not read mutable subsystem state directly.

### Action

Added primitive-only `EstimatedPoseObservation` to `SwerveObservation` and
populated it atomically from subsystem state. It is empty before initialization,
valid when the sample is valid, held with `measurementSampleValid=false` during
an invalid gap, and valid again on recovery.

### Files Changed

`SwerveObservation.java`, subsystem observation construction, and observation /
subsystem tests.

### Verification

Observation, estimator, odometry, and Java 17 regression evidence: `PASS`.

### Expected Result

`currentPose()` retains exact L23 odometry semantics while estimated pose is
additive and distinguishable.

## Increment 3B - Estimated pose telemetry

### Objective

Publish estimated pose for direct Glass comparison.

### Why

Operators need to compare odometry and estimator state without changing the
observation architecture.

### Action

Extended `SwerveTelemetryFacade` with typed estimated-pose publishers:

```text
/Swerve/EstimatedPose/Available
/Swerve/EstimatedPose/XMeters
/Swerve/EstimatedPose/YMeters
/Swerve/EstimatedPose/HeadingDegrees
/Swerve/EstimatedPose/MeasurementSampleValid
```

Unavailable values do not fabricate numeric pose data. Held values remain
available while validity is false. The existing L23 odometry topics and
odometry Field2d source were preserved.

### Files Changed

`SwerveTelemetryFacade.java` and focused telemetry tests.

### Verification

Focused telemetry/observation tests and Java 17 clean regression: `PASS`.

### Expected Result

Glass can inspect independent odometry and estimated-pose namespaces.

## Increment 4A - Known-field-pose reset contract

### Objective

Design the smallest safe localization reset.

### Why

Known-pose reset must change localization frames without falsifying physical
sensor history.

### Action

Defined Disabled-only reset prerequisites, one validated heading and module
snapshot, identical reset inputs for odometry and estimator, failure
preservation, and post-reset baseline semantics. Physical gyro, drive rotor,
and CANcoder sensors remain untouched.

### Files Changed

Design/audit record only; no implementation change.

### Verification

Reset architecture audit approved the contract.

### Expected Result

Both localization states can move to one known field pose while sensor history
continues unchanged.

## Increment 4B - Subsystem reset foundation

### Objective

Implement atomic subsystem-owned known-pose reset.

### Why

The subsystem must own validation and mutate odometry/estimator together.

### Action

Added `resetKnownFieldPose(Pose2d)`. It rejects invalid or unhealthy requests,
uses one validated snapshot with `resetPosition(...)` for both pose owners,
returns failure without changing prior state, and exposes the requested pose
immediately through direct APIs. The next periodic observation reflects the
reset; unchanged sensors cause no jump.

### Files Changed

`SwerveSubsystem.java` and `SwerveSubsystemKnownFieldPoseResetTest.java`.

### Verification

Focused reset, estimator, odometry, observation, telemetry, and Java 17
regression evidence: `PASS`.

### Expected Result

Odometry Pose and Estimated Pose reset together without physical sensor reset.

## Increment 4C - Disabled-only reset command

### Objective

Add the command-layer invocation wrapper.

### Why

Command lifecycle and mode policy belong at the command boundary; validation
and state mutation remain in the subsystem.

### Action

Added `ResetKnownFieldPoseCommand` with a subsystem requirement,
`runsWhenDisabled() == true`, Disabled-only execution, one reset attempt per
schedule, and immediate completion.

### Files Changed

`ResetKnownFieldPoseCommand.java` and focused command tests.

### Verification

Focused command/reset tests and Java 17 clean regression: `PASS`.

### Expected Result

An operator can request a safe Disabled-only reset without command-side
duplication of subsystem validation.

## Increment 4D - Learning starting pose and Glass trigger

### Objective

Make the reset demonstrable in Simulation/Glass with one explicit learning
pose.

### Why

The lesson needs a reproducible learning pose without inventing competition
field coordinates or autonomous architecture.

### Action

Defined `Constants.FieldConstants.kLearningStartingPose = Pose2d.kZero` as a
provisional learning pose and registered the exact dashboard command label:

```text
Reset Known Starting Pose
```

RobotContainer only constructs and registers the dashboard object. Existing
Back/View heading capture, odometry Field2d meaning, and Disabled-only reset
policy remain unchanged.

### Files Changed

`Constants.java`, `RobotContainer.java`, dashboard wiring, and focused reset /
dashboard tests.

### Verification

Simulation/Glass reset and post-reset continuity were user-verified `PASS`.

### Expected Result

The learning pose can be selected from Glass while Disabled, without a pose
database or autonomous binding.

## Increment 5A - Autonomous-readiness contract audit

### Objective

Define the minimum drivetrain boundary for a future autonomous layer.

### Why

The word “readiness” does not authorize PathPlanner, trajectories, or an
autonomous routine.

### Action

Separated requested, final-commanded, and measured speeds; confirmed existing
robot-relative actuation and centralized stop authority; and defined future
command requirements and mode-transition behavior.

### Files Changed

Audit/design documentation only.

### Verification

Autonomous-readiness safety contract audit: `PASS`.

### Expected Result

Future autonomous code has a clear boundary without adding autonomous behavior.

## Increment 5B - Measured robot-relative speeds

### Objective

Expose actual measured robot-relative chassis speeds.

### Why

Future autonomous consumers must not mistake requested or final-commanded state
for measured motion.

### Action

Added `Optional<ChassisSpeeds> getMeasuredRobotRelativeSpeeds()`, deriving
speeds from measured module velocity and calibrated angle in fixed FL/FR/BL/BR
order through existing inverse kinematics. Invalid, unhealthy, disconnected,
incomplete, and nonfinite samples return empty.

### Files Changed

`SwerveSubsystem.java` and focused measured-speed/kinematics tests.

### Verification

Measured-speed, kinematics, and Java 17 regression evidence: `PASS`.

### Expected Result

Measured speed is available defensively and remains independent of command
request state and gyro/field-heading capture.

## Increment 5C - Nonfinite request boundary hardening

### Objective

Prevent invalid chassis-speed requests from reaching the output pipeline.

### Why

Autonomous-readiness requires invalid requests to fail closed without throwing
through robot periodic or preserving stale nonzero intent.

### Action

Hardened `acceptChassisSpeeds(...)` and
`acceptFieldRelativeChassisSpeeds(...)` for null and nonfinite components.
Invalid requests invoke the existing `stop()` path, clear/disarm intent, stop
all four modules, and return. The next finite request recovers normally.

### Files Changed

`SwerveSubsystem.java` and focused robot-relative/field-relative tests. A
test-only DriverStation simulation helper correction reused the repository's
canonical mode setup.

### Verification

Focused 5C tests, measured-speed regression, full regression, and Java 17 clean
build evidence: `PASS`.

### Expected Result

Finite teleop and field-relative behavior is preserved; invalid input cannot
leave stale output armed.

## Increment 5D - Autonomous command safety contract audit

### Objective

Audit the future command lifecycle without implementing autonomous behavior.

### Why

Completion, interruption, cancellation, timeout, invalid-state, and mode
transition safety must be explicit before future autonomous work.

### Action

Defined the requirement, stop, and fail-closed contracts for future drivetrain
commands. No dummy command, PathPlanner, AutoBuilder, trajectory, or routine
was added.

### Files Changed

Audit/design documentation only.

### Verification

Command safety audit: `PASS`.

### Expected Result

The current L24 boundary is sufficient for readiness contracts without scope
creep.

## Increment 5E - Disabled-transition actuation disarm

### Objective

Prevent production chassis intent from surviving Disabled.

### Why

Re-enable must never dispatch an old nonzero request before a fresh operator or
future command request.

### Action

At the subsystem periodic/output boundary, Disabled clears stored production
intent, zeros final module states, and stops all four modules through the
existing stop authority. Re-enable does not resume the old request; only a new
finite valid request arms motion.

### Files Changed

`SwerveSubsystem.java` and deterministic robot-relative/field-relative safety
tests, including stale legacy fixtures corrected to establish the required
DriverStation mode.

### Verification

User-supplied Java 17 focused/full/build evidence and runtime Simulation/Glass
evidence: `PASS`.

### Expected Result

Disabled -> Teleoperated with neutral input remains stopped, and a fresh valid
request recovers normally.

## Final runtime correction - reusable dashboard reset lifecycle

### Objective

Make the persistent dashboard reset command one-shot per schedule.

### Why

The dashboard retains one command object; object-lifetime `attempted` state
made later schedules no-ops.

### Action

Removed the lifetime behavior so each Disabled schedule attempts one reset and
finishes. The subsystem remains the sole reset authority.

### Files Changed

`ResetKnownFieldPoseCommand.java`, dashboard reset tests, and reset subsystem
regression coverage.

### Verification

Reset #1, motion, Disable, reset #2 with the same dashboard command, and
post-reset continuity were user-verified `PASS`.

### Expected Result

The persistent Glass command is reusable without retrying within one schedule.

## Final warning cleanup - test-only deprecated API removal

### Objective

Remove the nine proven Java compiler warnings without changing production
behavior.

### Why

AGENTS requires non-deprecated APIs, and the calls were in L24 tests only.

### Action

Replaced `command.schedule()` with
`CommandScheduler.getInstance().schedule(command)` or the existing scheduler
variable in:

- `KnownFieldPoseResetDashboardTest.java`;
- `ResetKnownFieldPoseCommandTest.java`;
- `SwerveSubsystemKnownFieldPoseResetTest.java`.

### Files Changed

The three test files above only.

### Verification

User-supplied focused tests, full regression, and clean Java 17 build: `PASS`.
The nine deprecated compiler warnings are gone. Remaining IDE unused-field
diagnostics and ignored normal-drive CTRE status handling remain documented
technical debt; production warning cleanup was not performed.

### Expected Result

L24 uses the supported scheduler API without unrelated refactoring.

## Final documentation reconciliation

### Objective

Bring L24 documentation into agreement with the implemented source and
verified evidence before final architecture review.

### Why

The inherited Increment-0 documents incorrectly said estimator/reset/measured
speed implementation and verification were still pending.

### Action

Updated only:

- `README.md`;
- `LESSON_PLAN.md`;
- `LESSON_CHECKLIST.md`;
- `LESSON_STATUS.md`;
- this transition guide.

The documents now record completed increments, both runtime defect fixes,
Java/Simulation/Glass PASS evidence, the real-robot evidence status, deferred
debt, and the explicit L24 boundary.

### Files Changed

The five L24 documentation files listed above.

### Verification

Final documentation consistency review performed against current L24 source,
tests, supplied Java 17 verification, Simulation/Glass evidence, frozen L23
documentation, and governance constraints.

### Expected Result

L24 is `COMPLETE / FROZEN / READ-ONLY`.

## Documentation Amendment - L24 Real-Robot Verification

### Objective

Record the user-supplied real-robot verification of the existing L24
localization and known-field-pose reset foundation without reopening the frozen
lesson or expanding its architecture.

### Evidence

1. Hardware / Disabled baseline: four swerve modules and Pigeon connected and
   configuration-healthy; drive/steer applied outputs and velocities were zero
   while stationary; EstimatedPose was available and measurement-valid.
2. Stationary Pose/Estimator: Pose and EstimatedPose remained available and
   valid; X/Y stayed effectively stationary; heading drift was very small; both
   poses remained mutually consistent.
3. Translation tracking: approximately 0.67 m forward; Pose/EstimatedPose X was
   approximately `+0.671 m`; Y and heading remained near zero.
4. Rotation tracking: approximately 46 degrees was tracked; X/Y changed only
   by a few millimeters.
5. Combined translation and rotation: Pose/EstimatedPose remained available and
   valid, changed consistently, and remained mutually consistent.
6. Disabled known-field-pose reset: reset was accepted; Pose and EstimatedPose
   reset atomically to approximately `(0 m, 0 m, 0 deg)`; raw physical gyro
   measurement was not reset; no drivetrain motion was caused by reset.
7. Enabled reset rejection: after the robot moved away from zero, an Enabled
   reset was rejected; neither pose reset to zero and localization remained
   available and valid.
8. Reset followed by translation: after a Disabled reset, approximately 0.53 m
   forward was tracked as X approximately `+0.526 m`; Y and heading remained
   near zero.
9. Reset followed by rotation: after a Disabled reset, heading tracked
   approximately `-88.1 deg`; X/Y remained within a few millimeters of zero.
10. Combined motion after reset: user explicitly confirmed `PASS`; no additional
    numerical measurements are invented.

Odometry Pose currently uses measured module positions plus gyro heading.
EstimatedPose remains very close to or equal to Pose because no vision
measurement is fused yet. Vision/AprilTag fusion was not tested here and
remains deferred.

### Safety and Scope Result

The evidence confirms Disabled-only reset acceptance, Enabled reset rejection,
atomic odometry/estimator localization update, preservation of physical
gyro/module sensor state, and no drivetrain motion caused by reset.

This record validates only the existing L24 localization/reset foundation. It
does not claim final competition localization accuracy, autonomous path
following, PathPlanner, AutoBuilder, or A01 starting-pose readiness.

### Files Changed

Only the five existing S00_L24 documentation files were amended:

- `README.md`;
- `LESSON_PLAN.md`;
- `LESSON_CHECKLIST.md`;
- `LESSON_STATUS.md`; and
- this transition guide.

### Verification

Documentation consistency was checked against the frozen L24 source/tests,
existing software and Simulation/Glass evidence, the supplied ten-case
real-robot evidence, and the deferred vision/autonomous boundaries.

### Expected Result

S00_L24 remains `COMPLETE / FROZEN / READ-ONLY`, with Real Robot verification
recorded as `PASS` for the existing L24 localization/reset foundation.

## Verification Summary

| Gate | Result | Scope |
|---|---|---|
| Focused Java regression | PASS | User-supplied Java 17 evidence |
| Full Java regression | PASS | User-supplied Java 17 evidence |
| Clean build | PASS | User-supplied Java 17 clean build, all tasks executed |
| Simulation / Glass | PASS | Pose, estimated pose, reset reuse, continuity, and transition safety |
| Real robot L24 | PASS | User-supplied ten-case localization/reset hardware evidence |
| L22/L23 integrity | PASS | Frozen source/history preserved |
| Architecture | PASS | Frozen Backbone, interfaces, observation, telemetry, and RobotContainer preserved |

## Explicit L24 Boundary

Not implemented and not required for this lesson closure:

- vision or AprilTag integration;
- pose-estimator vision fusion, timestamp/latency work, or tuned uncertainty;
- PathPlanner or AutoBuilder;
- trajectory generation, path following, or autonomous routines;
- alliance transforms or field mirroring;
- official competition starting-pose selection;
- estimator-specific Field2d replacement;
- hardware sensor reset, calibration changes, or final drive-gain tuning.

The lesson establishes drivetrain contracts for a future autonomous layer; it
does not implement autonomous behavior.

## Deferred Technical Debt

- Final drive PID/feedforward optimization beyond the provisional L23 baseline.
- Separate design review for normal-drive CTRE `setControl` status handling.
- IDE unused-field diagnostics for retained CTRE diagnostic and ownership state.
- Final competition localization accuracy and tuning.

## Final State

L23 remains `COMPLETE / FROZEN / READ-ONLY`. L24 is
`COMPLETE / FROZEN / READ-ONLY`.

Git was not run.
