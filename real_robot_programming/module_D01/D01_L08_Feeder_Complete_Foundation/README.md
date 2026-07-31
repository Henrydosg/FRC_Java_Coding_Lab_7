# D01_L08 Feeder Complete Foundation

## Lesson

`D01_L08_Feeder_Complete_Foundation` inherits the completed and frozen
`D01_L07_Flywheel_Complete_Foundation` project. It adds one complete Feeder foundation while
preserving the inherited Drivebase, Intake, and Flywheel behavior and responsibilities.

Status: `COMPLETE`

Freeze status: `FROZEN`

## Objective

Provide safe manual forward and reverse control, real and simulation IO, integrated encoder
observations, immutable mechanism observations, and read-only telemetry for one Feeder driven by
a REV NEO Brushless motor and REV Spark MAX.

## Frozen Architecture

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

`RobotContainer` remains the composition root. It creates objects, selects real or simulation
implementations, injects dependencies, and declares controller bindings. It contains no Feeder
hardware logic, input processing, mechanism logic, or telemetry calculations.

REVLib types remain inside `FeederIOSparkMax`. `FeederIO`, `FeederSubsystem`, commands, controls,
observations, and telemetry remain vendor-independent.

## Final Hardware

| Item | Value |
| --- | --- |
| Device name | Feeder |
| Motor | REV NEO Brushless |
| Motor controller | REV Spark MAX |
| Encoder | Integrated NEO Encoder |
| Motor count | 1 |
| CAN ID | 19 |
| CAN bus | rio |
| Control mode | Open-loop duty cycle |
| Real IO | FeederIOSparkMax |
| Simulation IO | FeederIOSim |
| Safe fallback IO | FeederIONoop |

## Final Configuration

| Setting | Value |
| --- | --- |
| Motor inversion | `false` |
| Idle mode | Brake |
| Supply-current limit | `30 A` |
| Stator-current limit | Not applicable |
| Open-loop ramp | `0.20 s` |
| Peak reverse output | `-0.40` |
| Peak forward output | `+0.40` |
| Manual feed output | `+0.20` |
| Manual reverse output | `-0.20` |
| Safe stopped output | `0.0` |

The Spark MAX configuration is applied and checked inside `FeederIOSparkMax`. Output is clamped
to the configured peak range in the subsystem and IO implementations. The mechanism starts
stopped and returns to `0.0` when the command ends or is interrupted.

REVLib does not provide a separate Spark MAX stator-current observation. The Feeder contract
therefore reports `StatorCurrentAmps` deterministically as `0.0`, documented as not applicable.

## Driver Controls

| Input | Output | Mode |
| --- | --- | --- |
| Hold Xbox right bumper only | `+0.20` | `FEEDING` |
| Hold Xbox left bumper only | `-0.20` | `REVERSING` |
| Hold both bumpers | `0.0` | `STOPPED` |
| Release both bumpers | `0.0` through safe stop | `STOPPED` |
| Command interruption | `0.0` through safe stop | `STOPPED` |
| Robot disabled | `0.0` | `STOPPED` |

Both bumper controls are hold-to-run. They do not toggle. Simultaneous requests resolve to a
stopped output using the inherited Intake conflict-handling pattern.

## IO Implementations

- `FeederIOSparkMax` owns Spark MAX creation, REVLib configuration, open-loop output, integrated
  encoder reads, status values, connection state, configuration health, output clamping, and safe
  stop.
- `FeederIOSim` stores the commanded output and reports deterministic, contract-compatible
  observation values.
- `FeederIONoop` reports deterministic safe stopped and disconnected values.

## Read-Only Telemetry

NetworkTables table: `/Feeder`

Published fields:

- `/Feeder/AppliedOutput`
- `/Feeder/PositionRotations`
- `/Feeder/VelocityRpm`
- `/Feeder/SupplyCurrentAmps`
- `/Feeder/StatorCurrentAmps`
- `/Feeder/TemperatureCelsius`
- `/Feeder/Connected`
- `/Feeder/ConfigurationHealthy`
- `/Feeder/Mode`

Telemetry observes immutable `FeederObservation` values. It does not command hardware, schedule
commands, modify subsystem state, or calculate control outputs.

## Verification

| Verification | Result |
| --- | --- |
| Architecture review | PASS |
| Build | PASS |
| WPILib Simulation | PASS |
| Driver Station / Glass | PASS |
| Real robot | PASS |
| Right bumper feed at `+0.20` | PASS |
| Left bumper reverse at `-0.20` | PASS |
| Simultaneous bumper stop | PASS |
| Release safe stop | PASS |
| Motor direction | PASS |
| Telemetry read-only behavior | PASS |

The user supplied the simulation, Glass, real-robot, control-behavior, safe-stop, and motor
direction verification results. The final repository build was also run during lesson closure.

## Scope Exclusions

D01_L08 does not add Shooter integration, closed-loop velocity control, PID, feedforward,
characterization, automatic Feeder logic, mechanism coordination, or autonomous Feeder commands.

## Next Lesson

The next lesson is Shooter integration. It will coordinate the existing Flywheel and Feeder
subsystems through commands while preserving their separate subsystem responsibilities, IO
contracts, state ownership, and telemetry boundaries. D01_L09 is not created by this lesson.

## Transition Guide

`docs/D01_L07_Flywheel_Complete_Foundation_to_D01_L08_Feeder_Complete_Foundation_Step_by_Step.md`

## Final State

`D01_L08_Feeder_Complete_Foundation` is `COMPLETE` and `FROZEN`.
