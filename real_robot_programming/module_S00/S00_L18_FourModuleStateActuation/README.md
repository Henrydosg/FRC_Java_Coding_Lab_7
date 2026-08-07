# S00_L18 Four Module State Actuation

## Lesson State

`COMPLETE / FROZEN / READ-ONLY`

Source lesson: `S00_L17_SingleModuleClosedLoopControl` (`COMPLETE / FROZEN / READ-ONLY`)

## Objective

Submit bounded robot-relative `ChassisSpeeds` through the existing four-module closed-loop
pipeline and verify simultaneous Front Left, Front Right, Back Left, and Back Right state actuation.

## Final Architecture

```text
ChassisSpeeds
  -> SwerveOutputPipeline
  -> optimized/desaturated SwerveModuleState[4]
  -> SwerveSubsystem dispatch
  -> FL / FR / BL / BR vendor-neutral IO
  -> CTRE hardware
```

- `SwerveOutputPipeline` owns kinematics, optimization, and desaturation.
- `SwerveSubsystem` dispatches states exactly in FL, FR, BL, BR order.
- Production dispatch requires `enabled`, no active Front Left commissioning ownership, and
  `productionIntentArmed`.
- `productionIntentArmed` is armed only by an accepted chassis intent and cleared by `stop()`.
- Disabled mode and Front Left commissioning suppress production dispatch without repeated periodic
  stop calls.
- `RobotContainer` remains the composition root; Phoenix APIs remain inside IO implementations.

## Implemented Scope

- All four modules are closed-loop ready through the shared CTRE configuration/readback path.
- The inherited Front Left-only limit was replaced for production dispatch by the global wheel-speed
  clamp already owned by the output pipeline.
- Fixed Test-mode commands publish:
  - `Four Module Forward`
  - `Four Module Robot Left`
  - `Four Module Rotate CCW`
  - `Four Module Stop`
- Verification constants are translation `0.30 m/s`, rotation `0.75 rad/s`, and timeout `1.0 s`.
- Commands require Test + Enabled, accept fixed speeds once, use the existing pipeline, and stop
  safely on timeout, interruption, disable, mode exit, exception, or explicit Stop.

## Final Verification

- Architecture review: PASS.
- Frozen Backbone: PASS.
- Public IO contract: PASS.
- RobotContainer composition-root boundary: PASS.
- All four modules closed-loop ready: PASS.
- Fixed FL/FR/BL/BR dispatch order: PASS.
- `productionIntentArmed` lifecycle and commissioning isolation: PASS.
- Focused tests: PASS.
- Full suite: `114/114 PASS`.
- Clean build: `BUILD SUCCESSFUL`.
- Simulation command lifecycle: PASS.
- Glass / SmartDashboard command publication: PASS.
- Driver Station: PASS.
- Real robot Forward, Robot Left, Rotate CCW, automatic `1.0 s` stop, and explicit Stop: PASS.
- No abnormal vibration observed.

## Technical Debt and Deferred Scope

- PID and feedforward values remain commissioning baselines, not production-final tuning.
- `kS` and full SysId/characterization remain deferred.
- Four-module Test-mode commands are verification tools only, not normal drive controls.
- Joystick and teleop integration belong to S00_L19.
- Field-relative control, odometry, pose estimation, autonomous behavior, and fault aggregation are
  outside this lesson.

S00_L17 is unchanged and frozen. S00_L19 was not created or modified during this lesson.
