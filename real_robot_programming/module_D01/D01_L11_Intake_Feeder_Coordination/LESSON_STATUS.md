# D01_L11 Intake Feeder Coordination

## Lesson Information

| Item | Value |
| --- | --- |
| Lesson | D01_L11_Intake_Feeder_Coordination |
| Previous Lesson | D01_L10_Basic_Integrated_Robot |
| Previous Lesson Status | COMPLETE and FROZEN |
| Status | COMPLETE |
| Freeze Status | FROZEN |
| Development Model | Inheritance Development |

## Lesson Objective

Coordinate the existing Intake and Feeder subsystems inside `ManualIntakeCommand`.

Dedicated feeder outputs support independent tuning for acquire, outtake, and shoot.

The current outputs and intake feeder delay are baseline robot-tuning parameters, not final
competition values. Future tuning must modify `Constants.java` only.

- Intake request: Intake inward and Feeder inward, opposite the shooting direction.
- Outtake request: Intake outward and Feeder outward, the shooting direction.
- Release, interruption, or disable: stop both mechanisms.

## Architecture Review

| Check | Result |
| --- | --- |
| Frozen Backbone | PASS |
| RobotContainer composition-root boundary | PASS |
| Existing IntakeSubsystem and FeederSubsystem ownership | PASS |
| Existing IO contracts and vendor isolation | PASS |
| ManualIntakeCommand coordinates subsystem APIs only | PASS |
| ManualIntakeCommand requires IntakeSubsystem and FeederSubsystem | PASS |
| ManualShootCommand behavior preserved | PASS |
| Telemetry and Observation flows unchanged | PASS |

## Verification

| Required Field | Result |
| --- | --- |
| Architecture Review | PASS |
| Baseline Build | PASS |
| Build | PASS |
| Simulation | NOT TESTED |
| Driver Station / Glass | NOT TESTED |
| Real Robot | NOT TESTED |
| Intake inward + Feeder inward | NOT TESTED |
| Outtake outward + Feeder outward | NOT TESTED |
| Release stops both | NOT TESTED |
| ManualShootCommand regression | NOT TESTED |
| Scheduler conflict review | PASS |
| Transition Guide | NOT APPLICABLE |
| Git Commit | PENDING |
| Git Push | PENDING |
| Known Issues | NONE |

## Build Evidence

Baseline command:

```text
.\gradlew.bat clean build --no-daemon
```

Baseline result:

```text
BUILD SUCCESSFUL in 1m 41s
```

Final command:

```text
.\gradlew.bat clean build --no-daemon
```

Final result:

```text
PENDING FINAL CLEAN BUILD
```

## Scheduler Requirement Review

- `ManualIntakeCommand` requires `IntakeSubsystem` and `FeederSubsystem`.
- `ManualShootCommand` requires `FlywheelSubsystem` and `FeederSubsystem`.
- The shared `FeederSubsystem` requirement prevents both commands from controlling the feeder
  concurrently.
- `ManualFeederCommand` also requires `FeederSubsystem`, preserving mutual exclusion for the
  bumper controls.

## Current Status

```text
Lesson: D01_L11_Intake_Feeder_Coordination
Previous Lesson: D01_L10_Basic_Integrated_Robot
Status: COMPLETE
Freeze Status: FROZEN
```
