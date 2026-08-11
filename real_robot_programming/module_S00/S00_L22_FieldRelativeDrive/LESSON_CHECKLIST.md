# S00_L22 Field-Relative Drive - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Source: `S00_L21_FirstFloorDriveValidation` - `COMPLETE / FROZEN / READ-ONLY`  
Next: `S00_L23_OdometryAndPoseVisualization` - `OUT OF SCOPE`

## Inheritance and Governance

- [x] L21 is `COMPLETE / FROZEN / READ-ONLY`.
- [x] User supplied that L21 was pushed to `origin/main`.
- [x] L21 was copied to the L22 lesson directory.
- [x] Copied build artifacts were removed.
- [x] Baseline clean build passed: `BUILD SUCCESSFUL in 38s`; 7/7 tasks.
- [x] Inherited regression passed.
- [x] Architecture audit completed with result `CONDITIONALLY APPROVED`.
- [x] L22 governance metadata initialized as `IN_PROGRESS`.
- [x] Transition guide finalized as `FINAL / PASS`.

## Architecture Scope

- [x] Objective is limited to field-relative teleop conversion.
- [x] Architecture delta is documented.
- [x] Existing robot-relative output pipeline remains frozen.
- [x] RobotContainer remains the composition root only.
- [x] Vendor APIs remain inside IO adapters.
- [x] L23 boundaries are preserved.
- [x] Field-relative conversion is owned by `SwerveSubsystem` before the frozen robot-relative output pipeline.
- [x] Existing `acceptChassisSpeeds()` remains robot-relative.
- [x] Robot-relative Test/commissioning paths do not depend on gyro validity.
- [x] SwerveSubsystem owns the captured raw-yaw field reference and wrapping.
- [x] Field requests fail closed before reference initialization.
- [x] Capture/re-capture disarms previous field-relative intent.
- [x] Xbox Back/View is the explicit Disabled-only capture binding.
- [x] No gyro configuration changed.
- [x] No `GyroIOSim` added.

## Required Hardware Gate

- [x] Verify Pigeon2 mounting orientation while Disabled.
- [x] Verify initial yaw near zero.
- [x] Verify yaw increases for counterclockwise robot rotation.
- [x] Verify yaw decreases for clockwise robot rotation.
- [x] Record user-supplied hardware evidence before implementation.

## Future Implementation and Verification

- [x] Receive explicit authorization to implement after the hardware gate passes.
- [x] Implement the approved software field-reference architecture.
- [x] Add focused field-reference and field-relative tests.
- [x] Codex focused tests PASS: 44/44.
- [x] Codex full regression PASS: 189 tests, 0 failures, 0 errors, 0 skipped.
- [x] Codex Gradle build PASS: `BUILD SUCCESSFUL in 12s`.
- [x] User Simulation / HALSIM verification PASS.
- [x] User Driver Station / Glass verification PASS.
- [x] User R1 centered-enable PASS supplied.
- [x] User R2 Disabled field-reference capture at field +X and centered-enable workflow supplied.
- [x] User R3 field-relative floor matrix, sign/magnitude, combined motion, centered stop, and Disable -> Enable persistence supplied.
- [x] Invalid-gyro stop/disarm and fresh-request recovery covered by focused automated tests.
- [x] Finalize the transition guide as `FINAL / PASS`.
- [x] Record BL steer drift as `INTERMITTENT / NOT REPRODUCED`; no root cause claimed.
- [x] Final clean Java 17 build and full regression recorded PASS.
- [x] User completion commit recorded: `79ac5cc Complete S00_L22 field-relative drive`.
- [ ] User Git push.

## L23 Boundary

- [x] No odometry.
- [x] No pose estimation.
- [x] No pose visualization.
- [x] No autonomous or PathPlanner.
- [x] No alliance pose transform.
- [x] No pose reset or pose ownership.
- [x] No heading reset.
- [x] `S00_L23_OdometryAndPoseVisualization` has not been started.
