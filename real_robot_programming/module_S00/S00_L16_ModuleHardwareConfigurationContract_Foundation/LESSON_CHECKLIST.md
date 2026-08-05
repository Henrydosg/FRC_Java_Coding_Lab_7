# S00_L16 Lesson Checklist

Status: COMPLETE / FROZEN / READ-ONLY

Source lesson: S00_L15_SingleModuleOpenLoopCommissioning_Foundation - COMPLETE / FROZEN / READ-ONLY

| Step | State |
| --- | --- |
| Inherit frozen S00_L15 | PASS - source lesson is frozen and unchanged |
| Baseline Build | PASS - user-reported inherited baseline |
| Architecture Audit | PASS - Frozen Backbone and package boundaries preserved |
| Deterministic CTRE configuration contract | PASS - COMPLETE |
| Drive TalonFX configuration | PASS - apply/readback and 70 A supply limit verified |
| Steer TalonFX configuration | PASS - apply/readback and 60 A stator limit verified |
| CANcoder configuration | PASS - direction, offset, apply/readback verified |
| Configuration-result handling | PASS - apply and refresh status results required |
| Configuration-health behavior | PASS - all four modules healthy; fail-closed behavior preserved |
| Java implementation | PASS - COMPLETE |
| Focused tests | PASS - 10/10 |
| Full test suite | PASS - 58/58 |
| Clean full build | PASS - BUILD SUCCESSFUL |
| Simulation | PASS |
| Driver Station / Glass | PASS |
| Real Robot | PASS |
| Documentation finalization | PASS - COMPLETE |
| Transition guide | PASS - `docs/S00_L15_to_S00_L16_Step_by_Step.md` |
| Git Commit | PASS - `eb65523 Complete S00_L16 module hardware configuration contract` recorded |
| Git Push | PASS - pushed to `origin/main` |
| Freeze | NOT TESTED - user-owned freeze commit and final working-tree validation |

## Safety and Architecture Constraints

- Preserve Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware.
- Preserve hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry.
- Keep RobotContainer as composition root only.
- Keep vendor APIs inside IO implementations only.
- Keep telemetry read-only.
- Do not add closed-loop control, PID, feedforward, Motion Magic, kinematics, odometry, driver input,
  or four-module actuation.
- Do not modify previous frozen lessons.
- Do not add commissioning commands for other modules.

## Final Hardware Verification

All four modules passed the following Glass health and connectivity checks:

- `DriveConnected = true`.
- `SteerConnected = true`.
- `EncoderConnected = true`.
- `DriveConfigurationHealthy = true`.
- `SteerConfigurationHealthy = true`.
- `EncoderConfigurationHealthy = true`.

Front Left open-loop regression passed for drive-positive, drive-negative, steer-positive,
steer-negative, and safe stop/output return to zero.

The CANcoder readback comparison accepts offsets modulo one rotation and no more than one observed
quantization step: `1 / 4096 = 0.000244140625` rotations. Apply status, refresh status, and exact
sensor-direction equality remain mandatory.

## Deferred Scope

- PID.
- Feedforward.
- FusedCANcoder closed-loop feedback.
- Motion Magic.
- Closed-loop drive and steer requests.
- Four-module state actuation.
- Kinematics.
- Odometry.

## Next Locked Lesson

`S00_L17_SingleModuleClosedLoopControl`

S00_L17 is the next locked lesson. Its closed-loop scope remains outside this frozen S00_L16
lesson.

## Remaining User Lifecycle Steps

- Confirm the final build state.
- Create or verify the freeze commit.
- Push the freeze commit.
- Verify the final working tree with Git status.
