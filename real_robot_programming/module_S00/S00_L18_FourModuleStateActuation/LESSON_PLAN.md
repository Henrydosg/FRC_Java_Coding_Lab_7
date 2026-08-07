# S00_L18 Four Module State Actuation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: `S00_L18_FourModuleStateActuation`
- Previous Lesson: `S00_L17_SingleModuleClosedLoopControl`
- Source Lesson Status: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE / FROZEN / READ-ONLY

## Lesson Concept

Expand the verified single-module closed-loop foundation to bounded four-module state actuation by
reusing the existing robot-relative `ChassisSpeeds` pipeline and fixed FL/FR/BL/BR dispatch order.

## Locked Architecture

- `ChassisSpeeds -> SwerveOutputPipeline -> optimized/desaturated SwerveModuleState[4]` remains the
  only production state path.
- `SwerveSubsystem` dispatches one state to each matching module in Front Left, Front Right, Back
  Left, Back Right order.
- `acceptChassisSpeeds()` is the production intent entry point.
- Production dispatch requires Enabled, no Front Left commissioning ownership, and
  `productionIntentArmed`.
- Disabled and commissioning-owned periods compute observations and pipeline state but issue no
  production actuation.
- `SwerveModuleIO` remains vendor-neutral and `RobotContainer` remains composition root only.

## Fixed Test-mode Producer

- Translation verification speed: `0.30 m/s`.
- Robot-left verification speed: `0.30 m/s`.
- CCW rotation verification speed: `0.75 rad/s`.
- Command duration: `1.0 s`.
- Dashboard commands: `Four Module Forward`, `Four Module Robot Left`, `Four Module Rotate CCW`,
  and `Four Module Stop`.
- Commands are mutually exclusive, Test + Enabled only, fixed-value, bounded, and fail-safe.

## Final Verification Matrix

| Item | State | Evidence |
| --- | --- | --- |
| Architecture review | PASS | Frozen Backbone and ownership boundaries preserved. |
| Frozen Backbone | PASS | Driver-to-hardware and observation boundaries preserved. |
| Public IO contract | PASS | Existing vendor-neutral methods preserved. |
| All four closed-loop modules ready | PASS | Shared CTRE configuration/readback path verified. |
| FL/FR/BL/BR dispatch order | PASS | Exactly one state dispatched per matching module. |
| Production intent lifecycle | PASS | Armed by accepted intent; cleared by `stop()`. |
| Commissioning isolation | PASS | Front Left commissioning suppresses production dispatch. |
| Focused tests | PASS | Focused producer and regression tests passed. |
| Full test suite | PASS | `114/114 PASS`. |
| Clean build | PASS | `BUILD SUCCESSFUL`. |
| Simulation | PASS | Fixed command lifecycle verified. |
| Glass / SmartDashboard | PASS | Four command entries published and executable. |
| Driver Station | PASS | Test-mode command workflow verified. |
| Real robot Forward | PASS | Four-module actuation verified. |
| Real robot Robot Left | PASS | Four-module actuation verified. |
| Real robot Rotate CCW | PASS | Four-module actuation verified. |
| Automatic stop | PASS | All modules stopped after `1.0 s`. |
| Explicit Stop | PASS | All modules stopped on explicit command. |
| Abnormal vibration | PASS | None observed. |

## Technical Debt / Deferred Scope

PID and feedforward values remain commissioning baselines. `kS` and full SysId/characterization remain
future work. Test-mode commands must remain verification tools and must not become normal drive
controls. Joystick/teleop integration belongs to S00_L19. Field-relative control, odometry, pose
estimation, autonomous behavior, and fault aggregation remain outside S00_L18.

## Freeze Record

S00_L18 is COMPLETE / FROZEN / READ-ONLY. S00_L17 remains unchanged and frozen. S00_L19 was not
created or modified. Git commit, push, and final clean-working-tree verification remain user-owned.
