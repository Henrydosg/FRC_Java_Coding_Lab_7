# D01_L07 Flywheel Complete Foundation

## Lesson Information

| Item | Value |
| --- | --- |
| Lesson | D01_L07_Flywheel_Complete_Foundation |
| Previous Lesson | D01_L06_Intake_Complete_Foundation |
| Status | COMPLETE |
| Freeze Status | FROZEN |
| Development Model | Inheritance Development |

## Architecture

| Check | Result |
| --- | --- |
| Frozen Backbone | PASS |
| RobotContainer composition-root boundary | PASS |
| Vendor-independent FlywheelIO contract | PASS |
| Phoenix 6 isolation in FlywheelIOTalonFX | PASS |
| Read-only telemetry flow | PASS |
| Inherited Drivebase architecture | PASS |
| Inherited Intake architecture | PASS |

```text
CONTROL
Driver
-> Xbox Controller
-> FlywheelInputProcessor
-> ManualFlywheelCommand
-> FlywheelSubsystem
-> FlywheelIO
-> FlywheelIOTalonFX or FlywheelIOSim
-> Hardware or Simulation

OBSERVATION
FlywheelIO
-> FlywheelIOInputs
-> FlywheelSubsystem
-> FlywheelObservation
-> RobotTelemetry
-> FlywheelTelemetryFacade
-> NetworkTables / Glass
```

## Final Hardware

| Item | Value |
| --- | --- |
| Device | Flywheel |
| Motor | Kraken X60 |
| Controller | Talon FX |
| Motor count | 1 |
| CAN ID | 9 |
| CAN bus | rio |

## Driver Behavior

| Verification | Result |
| --- | --- |
| Hold Xbox Y commands `+0.10` | PASS |
| Release Xbox Y commands `0.0` | PASS |
| Command interruption commands `0.0` | PASS |
| Robot disable commands `0.0` | PASS |

## Telemetry

Table: `/Flywheel`

Published fields:

- `AppliedOutput`
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
| Baseline Build | PASS |
| Build | PASS |
| Simulation | PASS |
| Driver Station / Glass | PASS |
| Real Robot | PASS |
| Y-button behavior | PASS |
| Flywheel telemetry | PASS |
| Drivebase regression | PASS |
| Intake regression | PASS |
| Transition Guide | PASS |
| Git Commit | NOT APPLICABLE |
| Git Push | NOT APPLICABLE |
| Known Issues | NONE |

## Transition Guide

`docs/D01_L06_Intake_Complete_Foundation_to_D01_L07_Flywheel_Complete_Foundation_Step_by_Step.md`

## Final Status

```text
Lesson: D01_L07_Flywheel_Complete_Foundation
Status: COMPLETE
Freeze Status: FROZEN
```
