# S00_L16 Module Hardware Configuration Contract Foundation - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: S00_L16_ModuleHardwareConfigurationContract_Foundation
- Previous Lesson: S00_L15_SingleModuleOpenLoopCommissioning_Foundation
- Source Lesson: S00_L15_SingleModuleOpenLoopCommissioning_Foundation
- Previous Lesson Status: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Architecture Audit: PASS
- Baseline Build: PASS - user-reported inherited baseline
- Implementation: COMPLETE
- Build: PASS - clean full build successful
- Simulation: PASS
- Driver Station / Glass: PASS
- Real Robot: PASS
- Documentation Finalization: COMPLETE
- Git Commit: PASS - `eb65523 Complete S00_L16 module hardware configuration contract`
- Git Push: PASS - pushed to `origin/main`
- Freeze: NOT TESTED - user final freeze commit and clean-working-tree validation remain

## Lesson Concept

Define a deterministic CTRE module hardware configuration contract for one module's drive TalonFX,
steer TalonFX, and CANcoder. Configuration remains owned by the CTRE IO implementation, desired
values remain under the Constants configuration authority, and configuration results flow through
the existing IOInputs and immutable observation boundary.

## Exact Lesson Goal

Establish the smallest architecture-preserving contract for deterministic CTRE hardware
configuration, including configuration ownership, result handling, health semantics, and safe
behavior when configuration is not healthy.

Implementation is complete. Constants remain the configuration authority, while CTRE apply,
readback, comparison, diagnostics, and fail-closed output behavior remain inside the existing IO
implementation.

## Architecture Decision

APPROVED. The completed lesson preserves the Frozen Backbone, keeps
RobotContainer as composition root only, keeps vendor APIs inside IO implementations, and keeps
telemetry read-only. Any breaking IO contract change requires formal architecture review before
implementation.

## Locked Roadmap

Previous completed lesson -> copied baseline -> baseline build -> one new concept -> build ->
simulation -> Driver Station / Glass -> real robot -> documentation -> Git commit -> Git push.

The lesson is COMPLETE / FROZEN / READ-ONLY. Only the deterministic CTRE module hardware
configuration contract was in scope.

## Explicit Exclusions

- No closed-loop control.
- No PID, feedforward, or Motion Magic.
- No kinematics or odometry.
- No driver input or Xbox changes.
- No four-module actuation.
- No changes to previous frozen lessons.
- No commissioning commands for other modules.

## Planned Verification Record

| Item | State | Evidence / Owner |
| --- | --- | --- |
| Architecture Audit | PASS | Repository audit completed before implementation. |
| Baseline Build | PASS | User-reported inherited baseline. |
| Implementation | PASS - COMPLETE | Deterministic TalonFX/CANcoder configuration contract implemented inside the existing IO boundary. |
| Build | PASS | Clean full build: `BUILD SUCCESSFUL`. |
| Focused Tests | PASS | `10/10 PASS`, including offset wrap, quantization, mismatch, and direction checks. |
| Full Tests | PASS | `58/58 PASS`. |
| Simulation | PASS | User-verified. |
| Driver Station / Glass | PASS | User-verified. |
| Real Robot | PASS | Apply/readback, connectivity, health, direction, and safe-stop verification completed. |
| Documentation Finalization | PASS - COMPLETE | Final S00_L16 record written. |
| Transition Guide | PASS | `docs/S00_L15_to_S00_L16_Step_by_Step.md`. |
| Git Commit | PASS | Completion commit recorded: `eb65523 Complete S00_L16 module hardware configuration contract`. |
| Git Push | PASS | Completion commit pushed to `origin/main`. |
| Freeze | NOT TESTED | User-owned freeze commit, push, and final clean-working-tree validation remain. |

## Final Configuration Contract

The contract configures one CTRE module at a time:

- Drive TalonFX: approved per-module inversion and 70 A supply current limit.
- Steer TalonFX: approved per-module inversion and 60 A stator current limit.
- CANcoder: approved direction and module-specific magnet offset.
- Apply and refresh results are mandatory.
- Sensor direction must match exactly.
- Magnet offsets are compared modulo one rotation and within one observed CANcoder quantization
  step, `1 / 4096 = 0.000244140625` rotations.
- Any unhealthy module rejects nonzero output while `stop()` remains functional.

Approved CANcoder offsets:

| Module | Offset (rotations) |
| --- | ---: |
| Front Left | `0.067138671875` |
| Front Right | `0.02099609375` |
| Back Left | `0.464599609375` |
| Back Right | `-0.052978515625` |

## Final Regression Evidence

All four modules reported `DriveConnected`, `SteerConnected`, `EncoderConnected`,
`DriveConfigurationHealthy`, `SteerConfigurationHealthy`, and `EncoderConfigurationHealthy` as
true. Front Left drive-positive, drive-negative, steer-positive, steer-negative, and safe-stop
checks all passed.

## Deferred Items

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

S00_L17 is locked as the next lesson. Closed-loop control scope remains excluded from S00_L16.

## Final Lifecycle Boundary

S00_L16 documentation records the lesson as `COMPLETE / FROZEN / READ-ONLY`. The user remains
responsible for the final build confirmation, freeze commit, push of that freeze commit, and
working-tree status validation.
