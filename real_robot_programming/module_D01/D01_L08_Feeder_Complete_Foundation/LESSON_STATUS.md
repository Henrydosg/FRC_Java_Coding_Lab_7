# D01_L08 Feeder Complete Foundation

## Lesson Information

| Item | Value |
| --- | --- |
| Lesson | D01_L08_Feeder_Complete_Foundation |
| Previous Lesson | D01_L07_Flywheel_Complete_Foundation |
| Previous Lesson Status | COMPLETE and FROZEN |
| Status | COMPLETE |
| Freeze Status | FROZEN |
| Development Model | Inheritance Development |

## Architecture

| Check | Result |
| --- | --- |
| Frozen Backbone | PASS |
| RobotContainer composition-root boundary | PASS |
| Vendor-independent FeederIO contract | PASS |
| REVLib isolation in FeederIOSparkMax | PASS |
| Feeder Inputs snapshot ownership | PASS |
| Read-only telemetry flow | PASS |
| Inherited Drivebase architecture | PASS |
| Inherited Intake and Flywheel architecture | PASS |
| Intake/Flywheel responsibility separation | PASS |

```text
CONTROL
Driver
-> Xbox Controller
-> FeederInputProcessor
-> ManualFeederCommand
-> FeederSubsystem
-> FeederIO
-> FeederIOSparkMax or FeederIOSim
-> Hardware or Simulation

OBSERVATION
FeederIO
-> FeederIOInputs
-> FeederSubsystem
-> FeederObservation
-> RobotTelemetry
-> FeederTelemetryFacade
-> NetworkTables / Glass
```

## Final Hardware and Configuration

| Item | Value |
| --- | --- |
| Device | Feeder |
| Motor | REV NEO Brushless |
| Controller | REV Spark MAX |
| Encoder | Integrated NEO Encoder |
| Motor count | 1 |
| CAN ID | 19 |
| CAN bus | rio |
| Inverted | `false` |
| Idle mode | Brake |
| Supply-current limit | `30 A` |
| Stator-current limit | NOT APPLICABLE |
| Open-loop ramp | `0.20 s` |
| Peak output range | `-0.40` to `+0.40` |
| Manual feed output | `+0.20` |
| Manual reverse output | `-0.20` |
| Safe stop | `0.0` |

Spark MAX stator current is unsupported and is reported deterministically as `0.0`.

## Driver Behavior

| Verification | Result |
| --- | --- |
| Hold right bumper only commands `+0.20` and `FEEDING` | PASS |
| Hold left bumper only commands `-0.20` and `REVERSING` | PASS |
| Hold both bumpers commands `0.0` and `STOPPED` | PASS |
| Release both bumpers commands safe stop `0.0` | PASS |
| Command interruption commands safe stop `0.0` | PASS |
| Motor direction | PASS |

## Telemetry

Table: `/Feeder`

Published fields:

- `AppliedOutput`
- `PositionRotations`
- `VelocityRpm`
- `SupplyCurrentAmps`
- `StatorCurrentAmps`
- `TemperatureCelsius`
- `Connected`
- `ConfigurationHealthy`
- `Mode`

Telemetry is read-only.

## Verification Status

| Required Field | Result |
| --- | --- |
| Architecture Review | PASS |
| Baseline Build | NOT TESTED |
| Build | PASS |
| Simulation | PASS |
| Driver Station / Glass | PASS |
| Real Robot | PASS |
| Approved-input behavior | PASS |
| Feeder telemetry | PASS |
| Transition Guide | PASS |
| Git Commit | PENDING |
| Git Push | PENDING |
| Known Issues | NONE |

## Verification Evidence

- Build PASS: verified by the user and rerun during lesson closure.
- Simulation PASS: verified by the user.
- Driver Station / Glass PASS: verified by the user.
- Real Robot PASS: verified by the user.
- Right-bumper feed, left-bumper reverse, simultaneous-request stop, release safe stop, and motor
  direction: verified by the user.

## Transition Guide

`docs/D01_L07_Flywheel_Complete_Foundation_to_D01_L08_Feeder_Complete_Foundation_Step_by_Step.md`

## Next Lesson

The next lesson is Shooter integration. It will coordinate the existing Flywheel and Feeder
subsystems through commands without merging their subsystem responsibilities, IO contracts,
state ownership, or telemetry boundaries. D01_L09 has not been created.

## Final Status

```text
Lesson: D01_L08_Feeder_Complete_Foundation
Next Lesson: D01_L09_Shooter_Integration
Status: COMPLETE
Freeze Status: FROZEN
```
