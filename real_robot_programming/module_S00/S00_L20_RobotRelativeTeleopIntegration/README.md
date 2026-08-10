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

For an exact zero chassis request, the output pipeline commands zero drive speed and copies each
module's current measured steer angle into the corresponding FL/FR/BL/BR state. The measured angle
is refreshed each update; this is not a persistent last-commanded-angle latch. Every nonzero request
continues through kinematics, optimization, and desaturation unchanged.

At the CTRE IO boundary, zero drive velocity now stops only the drive motor and leaves steer
position control uninterrupted. Explicit module/subsystem stop and unhealthy or nonfinite fail-closed
paths still stop both drive and steer.

Approved L20 baseline limits:

- Translation: `1.0 m/s`
- Rotation: `1.0 rad/s`

## Verification Summary

| Verification | State | Evidence |
| --- | --- | --- |
| Focused command tests | PASS | User supplied 11/11 PASS. |
| End-to-end production-path tests | PASS | User explicitly reran and supplied 10/10 PASS. |
| Current post-fix tests | PASS | Repository artifacts record 166/166 PASS with zero failures, errors, or skips. |
| Historical clean build | PASS | User-supplied clean build passed before the final two production corrections. |
| Final post-fix clean build | PASS | `BUILD SUCCESSFUL in 35s`; 7 actionable tasks executed; all clean-build tests passed. |
| Simulation | PASS | User-supplied evidence. |
| HALSIM joystick | PASS | User-supplied evidence. |
| Glass / DriverInput | PASS | User-supplied evidence. |
| Robot on stands | PASS | Enable/Disable 10/10, all requested motion checks, and transition stress 3/3 passed. |
| Floor verification | PASS | Robot-relative driving behaved correctly; no unintended module actuation was observed. |

The test-only `RobotRelativeTeleopProductionPathTest` traverses the real command, subsystem,
kinematics, optimization, desaturation, and FL/FR/BL/BR dispatch path using recording test IO. It
does not introduce a production fake or simulation IO implementation.

## Locked Decisions

- Robot-relative control only.
- Production-request Observation and requested chassis/module telemetry were rejected.
- Production architecture is locked.
- No `SwerveModuleIOSim`, field-relative control, odometry, pose, L21, or L22 behavior is included.

## Diagnostic Closure and Administrative Gate

An intermittent BL steer drift/jitter was investigated. The user mechanically reseated/tightened
the encoder assembly, then did not reproduce BL drift/jitter or FL jitter during the supplied
post-fix stands, transition-stress, and floor verification. The bounded conclusion is: probable
mechanical encoder/mounting issue; symptom not reproduced after mechanical correction and post-fix
verification. No absolute hardware root cause is claimed, and no speculative tuning change was made.

All L20 technical verification is complete, including the final clean build performed after both
production corrections. The transition guide is `FINAL / PASS`. L20 remains `IN_PROGRESS` solely
because Document B requires a clear Git commit before lesson/module closure. Git is user-owned and
has not run. The lesson is ready for user Git closure. Do not start L21.

See `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and
`docs/S00_L19_to_S00_L20_Step_by_Step.md` for the reconciled lesson record.
