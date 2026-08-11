# Lesson Status

## Identity

- Lesson: `S00_L22_FieldRelativeDrive`
- Previous Lesson: `S00_L21_FirstFloorDriveValidation`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `IN_PROGRESS` — READY FOR USER GIT CLOSURE
- Freeze State: `EDITABLE`
- Lesson Goal: Add and validate field-relative teleop conversion while preserving the existing robot-relative drivetrain/output pipeline.
- Architecture Decision: `APPROVED` - the user-supplied Disabled Pigeon2 hardware gate cleared the audit condition.
- Architecture Delta: raw gyro yaw -> SwerveSubsystem-owned captured field reference -> wrapped field heading -> robot-relative `ChassisSpeeds` -> existing frozen output pipeline.
- Next Roadmap Lesson: `S00_L23_OdometryAndPoseVisualization` - `OUT OF SCOPE`

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| Architecture Review | PASS | Audit was conditionally approved; the user-supplied Disabled Pigeon2 hardware gate cleared the condition. |
| Disabled Pigeon2 Hardware Gate | PASS | User supplied: CAN 20 healthy; initial yaw near zero; CCW positive; CW negative; approximately 90-degree physical rotation produced approximately 88-93 degrees; pitch/roll near zero; no active fault. |
| Real-Robot Heading-Origin Defect | PASS | User supplied: with HEAD aligned to field +X, raw Pigeon yaw was `+129.207458` degrees; this is a runtime reference sample, not a constant. |
| Baseline Build | PASS | User supplied: `BUILD SUCCESSFUL in 38s`; 7/7 tasks executed. |
| Build | PASS | User supplied final Java 17 clean build: `BUILD SUCCESSFUL`. Prior L22 Gradle build also passed. |
| Inherited Regression | PASS | User supplied inherited regression PASS. |
| Field-Heading Reference Tests | PASS | Codex ran 44 focused reference/field-relative tests; 44 passed. |
| Full Regression | PASS | 189 tests, 0 failures, 0 errors, 0 skipped. |
| Simulation / HALSIM | PASS | User supplied PASS. No `GyroIOSim` was required. |
| Driver Station / Glass | PASS | User supplied PASS. |
| Real Robot | PASS (user supplied, scoped) | User supplied the final field-relative floor matrix PASS and the correct Disabled capture workflow: align HEAD to field +X, press/release Xbox Back/Button 7 once, enable, then drive. Disable/Enable reference persistence also passed. BL steer drift remains `INTERMITTENT / NOT REPRODUCED`; no root cause is claimed. |
| Transition Guide | FINAL / PASS | All required implementation, automated, simulation, driver-station, and real-robot evidence is recorded. |
| Git Commit | NOT TESTED | L22 Git remains user-owned. |
| Git Push | NOT TESTED | L22 Git remains user-owned. |

## Architecture Delta

```text
raw Pigeon yaw
-> captured field-heading reference
-> wrap(raw yaw - reference)
-> field heading
-> robot-relative ChassisSpeeds
-> existing frozen output pipeline
```

The existing robot-relative Swerve output pipeline, module IO contracts, hardware configuration, and tuning remain unchanged.

## Hardware Gate

The user supplied a passing Disabled verification for Pigeon2 CAN 20: heading was near the initial zero reference, counterclockwise chassis rotation increased yaw, clockwise rotation decreased yaw, approximately 90 degrees of physical rotation produced approximately 88-93 degrees of yaw change, pitch/roll remained near zero, and no active gyro/hardware fault was present.

The user then supplied a heading-origin defect: with HEAD physically aligned to field +X, raw Pigeon yaw was `+129.207458` degrees. L22 now captures a software reference while Disabled; this observed value is never hardcoded.

No gyro configuration, mount pose, inversion, offset, or heading-reset behavior was changed.

## L23 Boundary

L22 does not include odometry, pose estimation, pose visualization, autonomous behavior, PathPlanner, alliance pose transforms, or pose-reset behavior. `S00_L23_OdometryAndPoseVisualization` remains out of scope and has not been started.

## Known Issues

- BL steer drift is an intermittent watch item and was not reproduced; no corrective production change is authorized by this audit.

## Current State

The software field-heading reference implementation, focused tests, full regression, Java 17 clean build, Simulation/HALSIM, Driver Station/Glass, and final real-robot floor verification are complete. L22 remains `IN_PROGRESS / EDITABLE` and is READY FOR USER GIT CLOSURE.
