# S00_L23 - Odometry and Pose Visualization

## Lesson State

- Status: `COMPLETE / FROZEN / READ-ONLY`
- Previous lesson: `S00_L22_FieldRelativeDrive` - `COMPLETE / FROZEN / READ-ONLY`
- Next lesson: `S00_L24_PoseEstimationAndAutonomousReadiness` - `NOT STARTED / OUT OF SCOPE`
- Architecture review: `PASS`
- Transition guide: `FINAL / PASS`
- Git commit and push: user-owned; evidence not yet supplied

## Objective and Result

L23 adds the smallest governed path from existing Swerve measurements to subsystem-owned odometry and read-only pose visualization while preserving the L22 field-relative control path.

```text
raw TalonFX RotorPosition + calibrated module angle + L22 field heading
-> vendor-neutral IOInputs
-> SwerveSubsystem
-> SwerveModulePosition (FL, FR, BL, BR)
-> SwerveDriveOdometry
-> subsystem-owned Pose2d
-> immutable SwerveObservation
-> RobotTelemetry
-> SwerveTelemetryFacade
-> NT4 / Glass / Field2d
```

The implementation, final WPILib Java 17 clean build, focused odometry tests, full regression, Glass inspection, and real-robot validation are complete and PASS.

The authoritative final hardware configuration, superseded L14 values, conversion equations, bounded commissioning procedures, Phoenix readback rules, three-meter validation workflow, and field/pose verification procedure are consolidated in the [S00_L23 Final Swerve Calibration and Commissioning Guide](docs/S00_L23_Final_Swerve_Calibration_and_Commissioning_Guide.md). Use that guide for final L23 calibration facts; preserve older commissioning documents as historical evidence.

## Verified Measurement Contract

The drive position source is raw TalonFX `RotorPosition`, transported as vendor-neutral `drivePositionRotations`.

The inherited configured ratio `7.846153846153847` was a disproven assumption. Hardware documentation and repeated FR raw `RotorPosition` measurements over 20 wheel rotations verified the L23 drive ratio as `6.75:1`. L22 and older commissioning documents remain unchanged historical records.

Distance conversion uses the single Constants authority:

```text
wheelRotations = physicalForwardSign * rawRotorRotations / 6.75
distanceMeters = wheelRotations * (2 * pi * wheelRadiusMeters)
```

Physical-forward measurement signs are normalized at the subsystem measurement boundary. Module order is always `FL, FR, BL, BR`. The real-floor three-meter diagnostic and real odometry validation confirmed the resulting distance scale.

## Odometry Contract

- `SwerveSubsystem` owns `SwerveDriveOdometry` and the current `Pose2d`.
- Odometry initializes only after a valid L22 software field heading and one complete healthy finite four-module sample exist.
- Odometry updates exactly once per subsystem periodic cycle.
- Invalid gyro or module samples hold the last valid pose and publish `MeasurementSampleValid=false`.
- Recovery re-establishes continuity at the held pose without integrating invalid-interval movement.
- `getCurrentPose()` returns a defensive pose copy.
- No pose reset is required or implemented in L23.

## Observation and Visualization

`SwerveObservation.currentPose()` explicitly represents unavailable pose with `Optional.empty()`. Available pose contains X meters, Y meters, heading radians, and current-sample validity.

The telemetry layer publishes:

- `/Swerve/Pose/Available`
- `/Swerve/Pose/XMeters`
- `/Swerve/Pose/YMeters`
- `/Swerve/Pose/HeadingDegrees`
- `/Swerve/Pose/MeasurementSampleValid`
- `/SmartDashboard/Swerve/Field` as WPILib `Field2d`

Field2d is registered only after pose becomes available. A held valid pose remains visible during an invalid measurement sample; no pose is fabricated before initialization.

## Verification Record

- Java 17 baseline clean build: PASS - user supplied.
- Final WPILib Java 17 clean build: PASS - user supplied.
- Focused `SwerveSubsystemOdometryTest`: PASS - user supplied.
- Current automated regression: PASS - 29 suites, 262 tests, 0 failures, 0 errors, 0 skipped.
- Deterministic odometry coverage: initialization, zero, +X, pure +Y, heading, pure rotation, combined translation/rotation, fixed ordering, units, exactly-once updates, invalid hold, and recovery - PASS.
- Deterministic Swerve module/gyro simulation tests: PASS.
- Module and gyro connection/configuration health: PASS - real robot.
- Field-heading capture: PASS - real robot.
- Pose unavailable-to-available transition and healthy sample validity: PASS - real robot.
- Initial pose approximately `(0 m, 0 m, 0 deg)`: PASS - real robot.
- Real forward movement: approximately `0.45 m` physical and `XMeters=0.458470 m`, heading near zero - PASS.
- Three-meter real-floor distance validation: PASS.
- NT4 pose telemetry: PASS.
- Glass recognized `.type="Field2d"` and the `Robot` double array: PASS.
- Field2d widget opened and its pose marker moved with the real robot: PASS.

## Three-Meter Validation Diagnostic

`Drive 3m Validation` is a bounded enabled-Test-mode measurement diagnostic. It commands robot-relative `0.30 m/s`, tracks projected robot-forward progress independently for FL/FR/BL/BR, uses median consensus with the named disagreement tolerance, stops at measured travel `>= 3.000 m`, preserves overshoot, and stops on every completion, interruption, timeout, mode-exit, health, nonfinite, disagreement, submission, or telemetry failure path.

It is not autonomous functionality and does not use odometry to determine completion.

## Provisional Drive Velocity Baseline

The drive Slot 0 values remain the verified functional commissioning baseline: `kP=0.675`, `kI=0.0`, `kD=0.0`, `kS=0.15 V`, `kV=0.837 V/(rotation/s)`, and `kA=0.0`, with `SensorToMechanismRatio=6.75`.

The final clean build and real-floor validations passed with this configuration. Further PID/feedforward optimization is deferred technical debt and is not an L23 odometry or visualization completion requirement.

## L24 Boundary

L23 does not implement pose reset architecture, `SwerveDrivePoseEstimator`, vision or AprilTags, PathPlanner, trajectories, autonomous behavior, alliance transforms, or autonomous readiness. Those subjects belong to L24 or later.

## Frozen Integrity

L22 remains historical, complete, frozen, and unchanged. L23 is now complete and shall remain read-only after the User records the final Git commit and push.

See [`docs/S00_L22_to_S00_L23_Step_by_Step.md`](docs/S00_L22_to_S00_L23_Step_by_Step.md) for the finalized transition record.
