# D01_L10 Basic Integrated Robot

## Lesson Information

| Item | Value |
| --- | --- |
| Lesson | D01_L10_Basic_Integrated_Robot |
| Previous Lesson | D01_L09_Shooter_Complete_Foundation |
| Previous Lesson Status | COMPLETE and FROZEN |
| Status | COMPLETE |
| Freeze Status | FROZEN |
| Development Model | Inheritance Development |

## Lesson Concept

Integrate the existing drivetrain, intake, flywheel, feeder, and coordinated shooter controls into
one tested Xbox controller layout.

## Architecture

| Check | Result |
| --- | --- |
| Architecture Review | PASS |
| Frozen Backbone | PASS |
| RobotContainer composition-root boundary | PASS |
| Existing subsystem responsibilities | PASS |
| Existing IO contracts and hardware ownership | PASS |
| Read-only telemetry flow | PASS |
| New subsystems | NONE |
| New IO contracts | NONE |

## Verification Status

| Required Field | Result |
| --- | --- |
| Baseline Build | PASS |
| Build | PASS |
| Simulation | PASS |
| Driver Station / Glass | PASS |
| Real Robot | PASS |
| Transition Guide | PASS |
| Git Commit | NOT TESTED |
| Git Push | NOT TESTED |
| Known Issues | NONE |

## Verification Evidence

- The inherited L09 working state completed a clean baseline build.
- The final L10 source completed a clean build: `BUILD SUCCESSFUL in 24s`.
- The user identified the current tested source as the final lesson state, providing simulation,
  Driver Station / Glass, and real-robot verification evidence.
- Source inspection confirmed the final Xbox mappings, physical drivetrain identity, shooter
  outputs, spin-up delay, safe stop, and timer reset.
- Final DOCX and PDF documentation was rendered and visually inspected.

## Final Xbox Controller Mapping

| Input | Behavior |
| --- | --- |
| Left Stick Y | Physical left tank drive |
| Right Stick Y | Physical right tank drive |
| RT | Intake |
| LT | Outtake |
| RB | Feeder forward |
| LB | Feeder reverse |
| X | Flywheel only at `0.60` |
| Y | Flywheel `0.60` immediately, wait `1.0 s`, feeder `0.40` |
| A, B, Start, Back | Reserved |

## Final Drivetrain Hardware Identity

| Software side | Leader CAN ID | Follower CAN ID | Leader inverted |
| --- | --- | --- | --- |
| Left | `10` | `7` | `true` |
| Right | `11` | `8` | `false` |

Previous lessons used an incorrect physical Left/Right drivetrain identity.
D01_L10 corrects the hardware mapping so software Left controls the physical
left drivetrain and software Right controls the physical right drivetrain.
No command, subsystem, IO contract, or telemetry architecture was changed.

## Final Shooter Behavior

- Hold Y: flywheel starts immediately at `0.60`.
- After `1.0 s`: feeder starts at `0.40`.
- Continue holding Y: both remain running.
- Release, disable, or interruption: both stop immediately and the timer resets.

## Transition Guide

`docs/D01_L09_to_D01_L10_Basic_Integrated_Robot_Guide.md`

## Final Status

```text
Lesson: D01_L10_Basic_Integrated_Robot
Previous Lesson: D01_L09_Shooter_Complete_Foundation
Status: COMPLETE
Freeze Status: FROZEN
```
