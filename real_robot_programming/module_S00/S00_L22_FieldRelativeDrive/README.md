# S00_L22 - Field-Relative Drive

## Lesson State

- Status: `COMPLETE / FROZEN / READ-ONLY`
- Previous lesson: `S00_L21_FirstFloorDriveValidation` - `COMPLETE / FROZEN / READ-ONLY`
- Next lesson: `S00_L23_OdometryAndPoseVisualization` - `OUT OF SCOPE`
- Architecture audit: `APPROVED`; software field-reference architecture implemented.

## Objective

Add and validate field-relative teleop conversion while preserving the existing robot-relative drivetrain/output pipeline.

## Architecture Delta

```text
raw Pigeon yaw
-> captured field-heading reference
-> wrap(raw yaw - reference)
-> field heading
-> robot-relative ChassisSpeeds
-> existing frozen output pipeline
```

The inherited Swerve kinematics, optimization, desaturation, module IO contracts, hardware configuration, tuning, safety behavior, and output dispatch remain unchanged.

## Implementation State

- L21 is `COMPLETE / FROZEN / READ-ONLY`.
- User supplied that L21 was pushed to `origin/main`.
- L21 was copied to L22 and copied build artifacts were removed.
- Baseline clean build: PASS - `BUILD SUCCESSFUL in 38s`; 7/7 tasks executed.
- Inherited regression: PASS.
- Architecture audit: `CONDITIONALLY APPROVED`, with its hardware condition now cleared.
- Real-robot heading-origin defect recorded: raw Pigeon yaw `+129.207458` at HEAD -> field +X; value is not hardcoded.
- Minimal field-relative teleop production delta: implemented.
- Software field-heading reference: implemented in `SwerveSubsystem`.
- Operator binding: Xbox Back/View, Disabled-only capture.
- Codex focused tests: PASS - 44/44.
- Codex full regression: PASS - 189 tests, 0 failures, 0 errors, 0 skipped.
- Codex prior Gradle build: PASS - `BUILD SUCCESSFUL in 12s`.
- Final clean Java 17 build: PASS - user supplied `BUILD SUCCESSFUL`.
- Full regression: PASS - 189 tests, 0 failures, 0 errors, 0 skipped.
- Simulation/HALSIM: PASS - user supplied.
- Driver Station/Glass: PASS - user supplied.

## Hardware Gate

The user verified Pigeon2 CAN 20 while Disabled: initial yaw near zero, CCW yaw positive, CW yaw negative, approximately 90 degrees of physical rotation produced approximately 88-93 degrees of yaw change, pitch/roll near zero, and no active fault. The later field placement exposed the separate heading-origin defect: raw yaw was `+129.207458` degrees at field +X.

No gyro mount pose, inversion, configuration, offset, hardware yaw reset, or scalar changed. No simulation adapter was added.

## L23 Boundary

Odometry, pose estimation, pose visualization, autonomous behavior, PathPlanner, alliance pose transforms, and pose-reset behavior remain outside L22. `S00_L23_OdometryAndPoseVisualization` has not been started.

## Transition Guide

See [`docs/S00_L21_to_S00_L22_Step_by_Step.md`](docs/S00_L21_to_S00_L22_Step_by_Step.md): `FINAL / PASS`.

## Field-Reference Operational Contract

1. Disabled: align robot HEAD to field +X.
2. Press and release Xbox Back / Button 7 once to capture the current raw yaw.
3. Enable teleop and drive.
4. Do not recapture during ordinary chassis rotation; recapture only for intentional redefinition or reference/gyro validity loss.

The binding captures a software reference only; it does not calibrate or reset Pigeon hardware.

## Final User Evidence and Current Boundary

The user supplied a final field-relative floor matrix PASS, including multiple headings, translation, strafe, rotation, combined motion, centered stop, and Disable -> Enable persistence. BL steer drift is `INTERMITTENT / NOT REPRODUCED`; no root cause is claimed.

Simulation/HALSIM, Driver Station/Glass, the Java 17 clean build, focused tests, full regression, and final real-robot floor matrix are PASS. BL steer drift remains `INTERMITTENT / NOT REPRODUCED`; no root cause is claimed. User supplied completion commit `79ac5cc Complete S00_L22 field-relative drive`. L22 is now `COMPLETE / FROZEN / READ-ONLY`.
