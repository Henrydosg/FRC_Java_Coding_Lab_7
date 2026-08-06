# S00_L17 Lesson Checklist

Status: COMPLETE / FROZEN / READ-ONLY

Source lesson: `S00_L16_ModuleHardwareConfigurationContract_Foundation` - COMPLETE / FROZEN / READ-ONLY

| Step | State |
| --- | --- |
| Inherit frozen S00_L16 | PASS |
| Baseline build | PASS |
| Architecture Audit | PASS |
| Frozen Backbone and vendor isolation | PASS |
| Vendor-neutral closed-loop IO contract | PASS |
| CTRE VelocityVoltage / PositionVoltage mapping | PASS |
| RemoteCANcoder / continuous wrap | PASS |
| Base versus closed-loop health separation | PASS |
| Front Left closed-loop commissioning | PASS |
| Static-friction manual voltage characterization | PASS |
| Static-friction peak sampling and result finalization | PASS |
| S00_L16 open-loop behavior preserved | PASS |
| Simulation | PASS |
| Glass / Driver Station | PASS |
| Front Left Drive Positive at `+0.30 m/s` | PASS |
| Front Left Drive Negative at `-0.30 m/s` | PASS |
| Correct directions | PASS |
| Automatic `1.0 s` stop | PASS |
| No visible oscillation or hunting | PASS |
| Static-friction result line | PASS |
| Repeated positive unloaded breakaway verification | PASS |
| Focused tests | PASS |
| Full test suite | PASS |
| Clean build | PASS |
| Documentation finalization | PASS |
| Lesson freeze | COMPLETE / FROZEN |

## Required Safety

- Use Test mode and Enabled only.
- Raise and securely support the wheel before static-friction characterization.
- Trigger one positive voltage step at a time; never run an automatic sweep.
- Use only `+0.10` through `+1.00 V`, with `0.25 s` pulses.
- Stop on timeout, disable, mode exit, interruption, exception, rejection, or failed request.
- Do not infer `kS` from a single breakaway observation.

## Frozen Architecture

Driver -> controls -> commands -> subsystems -> IO -> hardware

hardware -> IOInputs -> subsystem / estimator -> immutable Observation -> telemetry

RobotContainer remains composition root only; telemetry remains read-only; Phoenix APIs remain in
`SwerveModuleIOCTRE`; Front Right, Back Left, and Back Right remain unactuated.
