# D01_L08 Feeder Complete Foundation

## Lesson

`D01_L08_Feeder_Complete_Foundation` inherits the completed and frozen
`D01_L07_Flywheel_Complete_Foundation` project. It adds one complete Feeder foundation while
preserving the inherited Drivebase, Intake, and Flywheel behavior.

Status: `IN_PROGRESS`

Freeze status: `NOT FROZEN`

## Objective

Provide safe manual control, real and simulation IO, immutable observation, and read-only
telemetry for one Feeder driven by a REV NEO Brushless motor and REV Spark MAX.

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
implementations, injects dependencies, and declares the approved controller binding. It contains
no Feeder hardware logic, input processing, mechanism logic, or telemetry calculations.

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

## Safety Configuration

| Setting | Value |
| --- | --- |
| Inversion | PENDING APPROVAL |
| Neutral mode | PENDING APPROVAL |
| Supply-current limit | PENDING APPROVAL |
| Stator-current limit | NOT APPLICABLE |
| Open-loop ramp | PENDING APPROVAL |
| Peak reverse output | PENDING APPROVAL |
| Peak forward output | PENDING APPROVAL |
| Manual test output | PENDING APPROVAL |

The Spark MAX configuration will be applied and checked inside `FeederIOSparkMax`. The mechanism
must start stopped and stop when its command ends or is interrupted.

## Driver Control

| Input | Behavior |
| --- | --- |
| Hold approved Xbox input | Command the approved Feeder output |
| Release approved Xbox input | Stop Feeder at `0.0` |
| Command interruption | Stop Feeder at `0.0` |
| Robot disabled | Stop Feeder at `0.0` |

The binding will use hold-to-run behavior. It will not use a toggle.

## IO Implementations

- `FeederIOSparkMax` will own Spark MAX creation, REVLib configuration, open-loop output, status
  values, connection state, configuration health, and safe stop.
- `FeederIOSim` will store the commanded output and report deterministic, contract-compatible
  observation values.
- `FeederIONoop` will report deterministic safe stopped and disconnected values.

## Read-Only Telemetry

The planned NetworkTables table is `/Feeder`.

Planned published fields:

- `/Feeder/AppliedOutput`
- `/Feeder/VelocityRpm`
- `/Feeder/SupplyCurrentAmps`
- `/Feeder/StatorCurrentAmps`
- `/Feeder/TemperatureCelsius`
- `/Feeder/Connected`
- `/Feeder/ConfigurationHealthy`
- `/Feeder/Mode`

Telemetry will observe immutable `FeederObservation` values. It must not command hardware,
schedule commands, modify subsystem state, or calculate control outputs.

## Verification

| Verification | Result |
| --- | --- |
| Architecture review | PENDING |
| Clean build | PENDING |
| WPILib Simulation | PENDING |
| Approved-input hold and release | PENDING |
| Command interruption stop | PENDING |
| Driver Station / Glass | PENDING |
| Real robot | PENDING |
| Drivebase regression | PENDING |
| Intake and Flywheel regression | PENDING |
| Telemetry read-only behavior | PENDING |

Required behavior:

- Holding the approved input runs the Feeder at the approved output.
- Releasing the approved input returns the Feeder output to zero.
- Interrupting the command returns the Feeder output to zero.
- Disabling the robot returns the Feeder output to zero.
- The `/Feeder` telemetry fields publish through the existing telemetry layer.
- Existing Drivebase, Intake, and Flywheel control and telemetry behavior remain functional.

## Scope Exclusions

D01_L08 does not add Shooter integration, closed-loop velocity control, PID, feedforward,
characterization, automatic Feeder logic, mechanism coordination, or autonomous Feeder commands.

The next lesson is `D01_L09_Shooter_Integration`.

## Transition Guide

The transition will be documented in:

`docs/D01_L07_Flywheel_Complete_Foundation_to_D01_L08_Feeder_Complete_Foundation_Step_by_Step.md`

## Final State

`D01_L08_Feeder_Complete_Foundation` is `IN_PROGRESS` and `NOT FROZEN`.
