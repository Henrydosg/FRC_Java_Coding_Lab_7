# D01_L09 Shooter Complete Foundation

## Lesson

`D01_L09_Shooter_Complete_Foundation` inherits the completed and frozen
`D01_L08_Feeder_Complete_Foundation` project. It adds one command that coordinates the existing
Flywheel and Feeder mechanisms without merging their responsibilities.

Status: `COMPLETE`

Freeze status: `FROZEN`

## Objective

Introduce one concept: a single command coordinates the existing `FlywheelSubsystem` and
`FeederSubsystem` for a hold-to-shoot action.

## Frozen Architecture

```text
CONTROL
Driver
-> Xbox Controller
-> ManualShootCommand
-> FlywheelSubsystem and FeederSubsystem
-> Existing FlywheelIO and FeederIO contracts
-> Existing hardware or simulation implementations

OBSERVATION
FlywheelSubsystem and FeederSubsystem
-> Existing immutable observations
-> RobotTelemetry
-> Existing telemetry facades
-> NetworkTables / Glass
```

`RobotContainer` remains the composition root. It creates `ManualShootCommand`, injects both
existing subsystem dependencies, supplies the existing configured outputs, and binds the command
to the Y button.

`ManualShootCommand` requires both subsystems. While scheduled, it commands Flywheel output
`+0.10` and Feeder output `+0.20`. When the Y button is released, the robot is disabled, or the
command is interrupted, it safely stops both mechanisms.

## Responsibility Boundaries

- `ManualShootCommand` coordinates the two existing mechanism actions.
- `FlywheelSubsystem` continues to own Flywheel behavior and state.
- `FeederSubsystem` continues to own Feeder behavior and state.
- The existing Flywheel and Feeder IO contracts retain hardware abstraction.
- Existing IO implementations retain vendor hardware ownership.
- Existing telemetry remains read-only.
- No `ShooterSubsystem` or `ShooterIO` exists.

## Driver Controls

| Input | Flywheel output | Feeder output | Behavior |
| --- | --- | --- | --- |
| Hold Y | `+0.10` | `+0.20` | Coordinated shooting |
| Release Y | `0.0` | `0.0` | Safe stop |
| Disable robot | `0.0` | `0.0` | Safe stop |
| Command interruption | `0.0` | `0.0` | Safe stop |
| Hold right bumper | Unchanged | `+0.20` | Individual forward feed |
| Hold left bumper | Unchanged | `-0.20` | Individual reverse feed |

## Verification

| Verification | Result |
| --- | --- |
| Architecture review | PASS |
| Baseline build | PASS |
| Final clean build | PASS |
| WPILib Simulation | PASS |
| Driver Station / Glass | PASS |
| Real robot | PASS |
| Hold Y outputs | PASS |
| Release safe stop | PASS |
| Disable safe stop | PASS |
| Interruption safe stop | PASS |
| Individual Feeder RB/LB controls | PASS |

The user supplied the simulation, Glass, real-robot, coordinated-output, safe-stop, and inherited
Feeder-control verification results. The final clean build was run during lesson closure.

## Scope Exclusions

D01_L09 does not add a Shooter subsystem, Shooter IO contract, new hardware ownership,
closed-loop velocity control, spin-up sequencing, readiness logic, autonomous shooting, or
telemetry changes.

## Transition Guide

`docs/D01_L08_Feeder_Complete_Foundation_to_D01_L09_Shooter_Complete_Foundation_Step_by_Step.md`

## Final State

`D01_L09_Shooter_Complete_Foundation` is `COMPLETE` and `FROZEN`.
