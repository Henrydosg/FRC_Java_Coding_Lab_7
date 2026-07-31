# D01_L07 Flywheel Complete Foundation

## Lesson

`D01_L07_Flywheel_Complete_Foundation` inherits the completed and frozen
`D01_L06_Intake_Complete_Foundation` project. It adds one complete Flywheel foundation while
preserving the inherited Drivebase and Intake behavior.

Status: `COMPLETE`

Freeze status: `FROZEN`

## Objective

Provide safe manual control, real and simulation IO, immutable observation, and read-only
telemetry for one Flywheel driven by a Kraken X60 and Talon FX.

## Frozen Architecture

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

`RobotContainer` remains the composition root. It creates objects, selects real or simulation
implementations, injects dependencies, and declares the Y-button binding. It contains no
Flywheel hardware logic, input processing, mechanism logic, or telemetry calculations.

Phoenix 6 types remain inside `FlywheelIOTalonFX`. `FlywheelIO`, `FlywheelSubsystem`, commands,
controls, observations, and telemetry remain vendor-independent.

## Final Hardware

| Item | Value |
| --- | --- |
| Device name | Flywheel |
| Motor | Kraken X60 |
| Motor controller | Talon FX |
| Motor count | 1 |
| CAN ID | 9 |
| CAN bus | rio |
| Control mode | Open-loop duty cycle |
| Real IO | FlywheelIOTalonFX |
| Simulation IO | FlywheelIOSim |

## Safety Configuration

| Setting | Value |
| --- | --- |
| Inversion | CounterClockwise_Positive |
| Neutral mode | Coast |
| Supply-current limit | 25 A |
| Stator-current limit | 40 A |
| Open-loop ramp | 1.0 s |
| Peak reverse output | 0.0 |
| Peak forward output | +0.20 |
| Manual test output | +0.10 |

The Talon FX configuration is applied and checked inside `FlywheelIOTalonFX`. The mechanism starts
stopped and stops when its command ends or is interrupted.

## Driver Control

| Input | Behavior |
| --- | --- |
| Hold Xbox Y | Command Flywheel output `+0.10` |
| Release Xbox Y | Stop Flywheel at `0.0` |
| Command interruption | Stop Flywheel at `0.0` |
| Robot disabled | Stop Flywheel at `0.0` |

The binding uses hold-to-run behavior. It does not use a toggle.

## IO Implementations

- `FlywheelIOTalonFX` owns Talon FX creation, Phoenix 6 configuration, `DutyCycleOut`, status
  signals, connection state, configuration health, and safe stop.
- `FlywheelIOSim` stores the commanded output and reports deterministic, contract-compatible
  observation values.
- `FlywheelIONoop` reports deterministic safe stopped and disconnected values.

## Read-Only Telemetry

The NetworkTables table is `/Flywheel`.

Published fields:

- `/Flywheel/AppliedOutput`
- `/Flywheel/VelocityRpm`
- `/Flywheel/SupplyCurrentAmps`
- `/Flywheel/StatorCurrentAmps`
- `/Flywheel/TemperatureCelsius`
- `/Flywheel/Connected`
- `/Flywheel/ConfigurationHealthy`
- `/Flywheel/Mode`

Telemetry observes immutable `FlywheelObservation` values. It does not command hardware, schedule
commands, modify subsystem state, or calculate control outputs.

## Verification

| Verification | Result |
| --- | --- |
| Architecture review | PASS |
| Clean build | PASS |
| WPILib Simulation | PASS |
| Y-button hold and release | PASS |
| Command interruption stop | PASS |
| Driver Station / Glass | PASS |
| Real robot | PASS |
| Drivebase regression | PASS |
| Intake regression | PASS |
| Telemetry read-only behavior | PASS |

Verified behavior:

- Holding Y runs the Flywheel at the configured `+0.10` output.
- Releasing Y returns the Flywheel output to zero.
- Interrupting the command returns the Flywheel output to zero.
- Disabling the robot returns the Flywheel output to zero.
- The `/Flywheel` telemetry fields publish through the existing telemetry layer.
- Existing Drivebase and Intake control and telemetry behavior remain functional.

## Scope Exclusions

D01_L07 does not add a Feeder, closed-loop velocity control, PID, feedforward, characterization,
automatic Flywheel logic, Intake coordination, or autonomous Flywheel commands.

## Transition Guide

The completed transition is documented in:

`docs/D01_L06_Intake_Complete_Foundation_to_D01_L07_Flywheel_Complete_Foundation_Step_by_Step.md`

## Final State

`D01_L07_Flywheel_Complete_Foundation` is `COMPLETE` and `FROZEN`.
