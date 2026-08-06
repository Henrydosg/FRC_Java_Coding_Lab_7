# Lesson Status

## Identity

- Lesson: `S00_L17_SingleModuleClosedLoopControl`
- Previous Lesson: `S00_L16_ModuleHardwareConfigurationContract_Foundation`
- Source Status: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE / FROZEN / READ-ONLY

## Final Verification Record

| Item | State | Evidence |
| --- | --- | --- |
| Architecture Review | PASS | Frozen Backbone, vendor-neutral IO, composition root, and read-only telemetry preserved. |
| Frozen Backbone | PASS | Control and observation boundaries preserved. |
| Interface Contract | PASS | Closed-loop methods remain vendor-neutral; open-loop methods and `stop()` remain. |
| Baseline Build | PASS | Inherited S00_L16 baseline passed. |
| Simulation | PASS | User verified. |
| Driver Station / Glass | PASS | User verified. |
| Front Left Drive Positive at `+0.30 m/s` | PASS | Correct direction and automatic stop. |
| Front Left Drive Negative at `-0.30 m/s` | PASS | Correct direction and automatic stop. |
| Oscillation / hunting check | PASS | None observed. |
| Static-friction result line | PASS | User verified. |
| Repeated positive unloaded breakaway | PASS | User verified. |
| Focused Tests | PASS | User verified. |
| Full Test Suite | PASS | User verified. |
| Clean Build | PASS | User verified. |
| Documentation | PASS | All active lesson records finalized. |
| Lesson Status | COMPLETE / FROZEN | Read-only snapshot. |
| Git Commit | USER-OWNED | Codex did not run Git. |
| Git Push | USER-OWNED | Codex did not run Git. |

## Final Engineering Record

- Drive uses `VelocityVoltage`, Slot 0, FOC false, and the approved commissioning `kV=0.124` baseline.
- Steer uses `PositionVoltage`, Slot 0, FOC false, RemoteCANcoder feedback, and continuous wrap.
- Closed-loop health and fail-closed behavior remain enforced.
- Static-friction characterization is Front Left-only, Test-mode-only, manual, positive-only, and
  bounded from `+0.10` through `+1.00 V`.
- Each pulse reports requested voltage, setControl status, peak velocity/current values, breakaway
  classification, and typed stop reason.

## Technical Debt / Deferred Scope

- PID and feedforward values are commissioning baselines, not production-final values.
- `kS` remains deferred.
- Full SysId and static-friction characterization remain future work.
- Commissioning commands must not become normal drive controls.
- FusedCANcoder, Motion Magic, closed-loop teleop, four-module state actuation, kinematics, odometry,
  and pose estimation remain deferred.

## Roadmap Lock

`S00_L18_SingleModuleClosedLoopControl` is the next locked lesson. It was not created or modified.
It may inherit only from this frozen S00_L17 snapshot.
