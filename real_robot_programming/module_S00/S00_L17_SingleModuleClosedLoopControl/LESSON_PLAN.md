# S00_L17 Single Module Closed-Loop Control - Lesson Plan

## Metadata

- Framework Version: 2.1
- Lesson: `S00_L17_SingleModuleClosedLoopControl`
- Previous Lesson: `S00_L16_ModuleHardwareConfigurationContract_Foundation`
- Source Lesson Status: COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE / FROZEN / READ-ONLY

## Lesson Concept

Define and verify one vendor-neutral Front Left closed-loop contract while retaining the frozen
S00_L16 open-loop commissioning path and deterministic CTRE configuration-health boundary.

## Locked Design

- `setDriveVelocityMetersPerSecond(double)` and `setSteerAngle(Rotation2d)` remain vendor-neutral.
- CTRE maps drive to `VelocityVoltage` Slot 0 and steer to `PositionVoltage` Slot 0.
- FOC is explicitly false for both requests.
- Front Left is the only actuated module.
- Base/open-loop health is distinct from closed-loop health.
- Unhealthy required configuration rejects nonzero requests; zero output and `stop()` remain safe.

## Completed Work

1. Preserved S00_L16 constants, configuration apply/readback, CANcoder quantization handling, and
   open-loop behavior.
2. Added the vendor-neutral closed-loop IO methods and Noop implementations.
3. Added CTRE drive and steer request mapping and closed-loop-critical readback checks.
4. Added Front Left Test-mode commissioning at `+0.30/-0.30 m/s`, with `±0.50 m/s` clamp,
   `1.0 s` timeout, ownership, mode guards, and safe stop paths.
5. Added manual positive static-friction characterization from `+0.10` through `+1.00 V`, with
   `0.25 s` pulses, peak sampling, `0.10 rps` breakaway threshold, and one result per click.

## Final Verification Matrix

| Item | State | Evidence |
| --- | --- | --- |
| Architecture Audit | PASS | Frozen Backbone and package boundaries preserved. |
| Frozen Backbone | PASS | Control and observation flows unchanged. |
| Interface Contract | PASS | Vendor-neutral IO methods, open-loop methods, and `stop()` preserved. |
| Simulation | PASS | User verified. |
| Glass / Driver Station | PASS | User verified. |
| Front Left Drive Positive at `+0.30 m/s` | PASS | User verified. |
| Front Left Drive Negative at `-0.30 m/s` | PASS | User verified. |
| Direction checks | PASS | User verified. |
| Automatic stop at `1.0 s` | PASS | User verified. |
| Oscillation / hunting check | PASS | None observed. |
| Static-friction result line | PASS | User verified. |
| Repeated positive unloaded breakaway | PASS | User verified. |
| Focused tests | PASS | User verified. |
| Full test suite | PASS | User verified. |
| Clean build | PASS | User verified. |
| Documentation | PASS | Active lesson records finalized. |
| Lesson state | COMPLETE / FROZEN | This snapshot is read-only. |

## Technical Debt

- PID and feedforward values remain commissioning baselines, not production-final values.
- `kS` remains deferred.
- Full SysId and static-friction characterization remain future work.
- Commissioning commands remain Test-mode tools only.

## Roadmap Lock

S00_L18 is the next locked lesson. It must be created only by copying this frozen S00_L17 snapshot;
S00_L17 source and documentation must not be modified from the S00_L18 workflow.
