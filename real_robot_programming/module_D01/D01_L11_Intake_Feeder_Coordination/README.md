# D01_L11 Intake Feeder Coordination

## Lesson

`D01_L11_Intake_Feeder_Coordination` inherits the completed and frozen
`D01_L10_Basic_Integrated_Robot` project.

Status: `COMPLETE`

Freeze status: `FROZEN`

## Objective

Improve manual intake behavior by coordinating the existing Intake and Feeder subsystems in
`ManualIntakeCommand`.

| Driver request | Intake output | Feeder output |
| --- | --- | --- |
| Right Trigger - Intake | Inward | Inward, opposite shooting direction |
| Left Trigger - Outtake | Outward | Outward, shooting direction |
| Release / interruption / disable | Stopped | Stopped |

The lesson preserves the existing controller bindings, subsystem APIs, IO contracts, telemetry,
observations, vendor adapters, and timed `ManualShootCommand` behavior.

## Architecture

```text
CONTROL
Driver
-> Xbox Controller
-> ManualIntakeCommand
-> IntakeSubsystem and FeederSubsystem
-> existing IO contracts
-> hardware or simulation

OBSERVATION
hardware
-> IOInputs
-> subsystem
-> immutable Observation
-> telemetry
-> NT4 / Glass / log
```

`RobotContainer` remains the composition root. Coordination belongs in the command, and each
subsystem continues to own its mechanism behavior and state.

## Tuning Baseline

The current feeder outputs and intake-to-feeder delay are baseline robot-tuning parameters, not
final competition values. Future tuning must modify the corresponding values in `Constants.java`
only; the architecture and command coordination contract remain unchanged.
