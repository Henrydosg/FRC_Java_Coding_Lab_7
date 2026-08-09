# S00_L20 Robot-Relative Teleop Integration

## Lesson State

`IN_PROGRESS`

Previous lesson: `S00_L19_DriverInputProcessing` (`COMPLETE / FROZEN / READ-ONLY`)

Next roadmap lesson: `S00_L21_FirstFloorDriveValidation` (`OUT OF SCOPE`)

## Final Implemented Architecture

```text
XboxController
-> XboxDriverInputSource
-> DriverInputProcessor
-> immutable DriverInputObservation
-> RobotRelativeTeleopDriveCommand
-> robot-relative ChassisSpeeds
-> SwerveSubsystem
-> SwerveOutputPipeline
-> SwerveModuleIO
```

`RobotRelativeTeleopDriveCommand` acquires exactly one authoritative driver-input sample per
execution. That same immutable `DriverInputObservation` supplies both control scaling and
driver-input telemetry. `RobotTelemetry` does not poll Xbox independently.

Approved L20 baseline limits:

- Translation: `1.0 m/s`
- Rotation: `1.0 rad/s`

## Verification Summary

| Verification | State | Evidence |
| --- | --- | --- |
| Focused command tests | PASS | User supplied 11/11 PASS. |
| End-to-end production-path tests | PASS | User explicitly reran and supplied 10/10 PASS. |
| Full regression | PASS | User explicitly reran and supplied PASS. |
| Final clean build | PASS | `gradlew clean build`; `BUILD SUCCESSFUL`, 7 actionable tasks executed. |
| Simulation | PASS | User-supplied evidence. |
| HALSIM joystick | PASS | User-supplied evidence. |
| Glass / DriverInput | PASS | User-supplied evidence. |
| Real robot | NOT TESTED | Hardware unavailable; required later. |

The test-only `RobotRelativeTeleopProductionPathTest` traverses the real command, subsystem,
kinematics, optimization, desaturation, and FL/FR/BL/BR dispatch path using recording test IO. It
does not introduce a production fake or simulation IO implementation.

## Locked Decisions

- Robot-relative control only.
- Production-request Observation and requested chassis/module telemetry were rejected.
- Production architecture is locked.
- No `SwerveModuleIOSim`, field-relative control, odometry, pose, L21, or L22 behavior is included.

## Outstanding Verification Debt

Real Robot Verification is `NOT TESTED - hardware unavailable`. Simulation does not substitute for
real-hardware actuation and safety verification. The requirement remains open for execution when
hardware becomes available.

Accordingly, the lesson remains `IN_PROGRESS`, the transition guide remains `NOT FINAL`, and
Architect closure is still required. Do not start L21.

See `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and
`docs/S00_L19_to_S00_L20_Step_by_Step.md` for the reconciled lesson record.
