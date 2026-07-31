# D01_L10 Basic Integrated Robot

## Lesson

`D01_L10_Basic_Integrated_Robot` strictly inherits the tested
`D01_L09_Shooter_Complete_Foundation` working state and integrates the existing drivetrain,
intake, flywheel, feeder, and coordinated shooter into one driver layout.

Status: `COMPLETE`

Freeze status: `FROZEN`

## Objective

Deliver a basic integrated robot with a standardized Xbox controller layout, correct physical
drivetrain identity, and a timed full-shooter sequence while preserving the frozen architecture.

## Final Architecture

```text
CONTROL
Driver
-> Xbox Controller
-> controls
-> commands
-> subsystems
-> io
-> hardware

OBSERVATION
Subsystems
-> immutable observations
-> RobotTelemetry
-> telemetry facades
-> NetworkTables / Glass
```

`RobotContainer` remains the composition root. No subsystem, IO contract, telemetry control path,
or dependency direction was added or changed.

## Xbox Controller Mapping

| Input | Final behavior |
| --- | --- |
| Left Stick Y | Physical left tank drive |
| Right Stick Y | Physical right tank drive |
| RT | Intake |
| LT | Outtake |
| RB | Feeder forward |
| LB | Feeder reverse |
| X | Flywheel only at `0.60` |
| Y | Full shooter: flywheel `0.60`, wait `1.0 s`, feeder `0.40` |
| A, B, Start, Back | Reserved |

## Final Drivetrain Hardware Mapping

| Software side | Leader CAN ID | Follower CAN ID | Leader inverted |
| --- | --- | --- | --- |
| Left | `10` | `7` | `true` |
| Right | `11` | `8` | `false` |

Previous lessons used an incorrect physical Left/Right drivetrain identity.
D01_L10 corrects the hardware mapping so software Left controls the physical
left drivetrain and software Right controls the physical right drivetrain.
No command, subsystem, IO contract, or telemetry architecture was changed.

## Full Shooter Sequence

While Y is held:

1. The flywheel starts immediately at normalized output `0.60`.
2. The command waits `1.0` second for spin-up.
3. The feeder starts at normalized output `0.40`.
4. Both motors remain commanded while Y remains held.

When Y is released, disabled, or interrupted, the command immediately stops both motors and resets
the timer so the next Y press waits the complete `1.0` second again.

## Verification

| Verification | Result | Evidence |
| --- | --- | --- |
| Architecture review | PASS | Frozen package responsibilities and dependency direction preserved |
| Baseline clean build | PASS | Inherited L09 working state built before L10 implementation |
| Final clean build | PASS | `BUILD SUCCESSFUL in 24s` |
| WPILib Simulation | PASS | User confirmed the current tested source as the final lesson state |
| Driver Station / Glass | PASS | User confirmed the current tested source as the final lesson state |
| Real robot | PASS | User confirmed the current tested source as the final lesson state |
| Documentation render | PASS | Final DOCX/PDF pages visually inspected |

## Transition Guide

`docs/D01_L09_to_D01_L10_Basic_Integrated_Robot_Guide.md`

## Final State

`D01_L10_Basic_Integrated_Robot` is `COMPLETE` and `FROZEN`.
