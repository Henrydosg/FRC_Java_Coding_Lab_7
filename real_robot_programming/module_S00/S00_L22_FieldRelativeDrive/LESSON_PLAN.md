# S00_L22 Field-Relative Drive - Lesson Plan

## Lesson Metadata

- Lesson: `S00_L22_FieldRelativeDrive`
- Previous: `S00_L21_FirstFloorDriveValidation` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `IN_PROGRESS` — READY FOR USER GIT CLOSURE
- Next: `S00_L23_OdometryAndPoseVisualization` - `OUT OF SCOPE`
- Architecture review: `APPROVED` after the user-supplied Disabled Pigeon2 hardware gate passed.

## Objective

Add and validate field-relative teleop conversion while preserving the existing robot-relative drivetrain/output pipeline.

## Inherited Production Path

`XboxController` -> `XboxDriverInputSource` -> `DriverInputProcessor` -> immutable `DriverInputObservation` -> `FieldRelativeTeleopDriveCommand` -> field-relative requested `ChassisSpeeds` -> `SwerveSubsystem` validated gyro conversion -> robot-relative `ChassisSpeeds` -> `SwerveOutputPipeline` -> `SwerveModuleIO` -> `SwerveModuleIOCTRE` -> hardware.

The inherited translation limit remains `1.0 m/s`, the rotation limit remains `1.0 rad/s`, and the existing zero-demand hold, kinematics, optimization, desaturation, module ordering, IO contracts, hardware configuration, and tuning remain unchanged.

## Approved Architecture Delta

```text
raw Pigeon yaw
-> captured field-heading reference
-> wrap(raw yaw - reference)
-> field heading
-> robot-relative ChassisSpeeds
-> existing frozen output pipeline
```

The delta is limited to field-relative teleop conversion. The output pipeline continues to receive robot-relative `ChassisSpeeds`.

## Hardware Gate Evidence

The user verified Pigeon2 CAN 20 while Disabled:

- initial heading was near zero;
- counterclockwise chassis rotation increased yaw;
- clockwise chassis rotation decreased yaw;
- approximately 90 degrees of physical rotation produced approximately 88-93 degrees of yaw change;
- pitch/roll remained near zero;
- no active gyro/hardware fault was present.

No mount pose, inversion, gyro configuration, offset, or heading reset was changed.

## Completed Initialization Evidence

| Gate | Result |
|---|---|
| L21 source lesson | PASS - `COMPLETE / FROZEN / READ-ONLY` |
| L21 push | PASS - user supplied: pushed to `origin/main` |
| L21 copied to L22 | PASS - user supplied |
| Copied build artifacts removed | PASS - user supplied |
| Baseline clean build | PASS - `BUILD SUCCESSFUL in 38s`; 7/7 tasks |
| Inherited regression | PASS - user supplied |
| Architecture audit | `CONDITIONALLY APPROVED` |
| L22 governance initialization | PASS - documentation-only initialization |
| Disabled Pigeon2 hardware gate | PASS - user supplied |
| Real-robot heading-origin defect | PASS - user supplied raw yaw `+129.207458` at field +X; not hardcoded |
| Minimal production delta | IMPLEMENTED |
| Software field-heading reference | IMPLEMENTED - subsystem-owned dynamic capture and wrap |
| Operator binding | IMPLEMENTED - Xbox Back/View, Disabled-only capture command |
| Focused tests | PASS - 44/44 |
| Full regression | PASS - 189 tests, 0 failures/errors/skips |
| Gradle build | PASS - `BUILD SUCCESSFUL in 12s` |

## Implementation Boundary

The implementation is limited to the field-relative default teleop command, a SwerveSubsystem-owned dynamic raw-yaw reference captured while Disabled, deterministic heading wrapping, a Disabled-only capture command, composition-root wiring, gyro yaw validity documentation, and focused tests. Robot-relative Test/commissioning entry points remain unchanged. No simulation adapter or L23 behavior was added.

## Final Field-Reference Operational Contract

1. Keep the robot Disabled.
2. Align robot HEAD to field +X.
3. Press and release Xbox Back / Button 7 once.
4. The subsystem captures the current raw yaw as the software field reference.
5. Enable teleop and drive.
6. Do not recapture merely because the chassis rotates.
7. Recapture only for an intentional field-zero redefinition or after reference/gyro validity is lost.

Back / Button 7 does not calibrate, reset, or zero Pigeon hardware.

## Final User Evidence

- User supplied final field-relative floor matrix PASS across multiple headings, translation, strafe, rotation, combined motion, centered stop, and Disable -> Enable persistence.
- User supplied the correct Disabled capture workflow above.
- BL steer drift status: `INTERMITTENT / NOT REPRODUCED`; no root cause or corrective production change is claimed.

## Closure Gates

- Simulation / HALSIM: PASS - user supplied.
- Driver Station / Glass: PASS - user supplied.
- Prior focused tests: PASS - 44/44.
- Prior full regression: PASS - 189 tests, 0 failures/errors/skips.
- Final clean Java 17 build: PASS - user supplied `BUILD SUCCESSFUL`.
- Full regression: PASS - 189 tests, 0 failures, 0 errors, 0 skipped.
- BL steer drift: `INTERMITTENT / NOT REPRODUCED`; no root cause claimed.
- Git commit and push remain user-owned.

## L23 Boundary

The following remain outside L22:

- odometry;
- pose estimation;
- pose visualization;
- autonomous behavior;
- PathPlanner;
- alliance pose transforms;
- pose reset or pose ownership.

`S00_L23_OdometryAndPoseVisualization` is not started by this lesson initialization.

## Transition Guide

See [`docs/S00_L21_to_S00_L22_Step_by_Step.md`](docs/S00_L21_to_S00_L22_Step_by_Step.md). Its status is `FINAL / PASS`.
