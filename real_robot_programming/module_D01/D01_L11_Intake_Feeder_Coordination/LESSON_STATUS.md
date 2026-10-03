# D01_L11 Intake Feeder Coordination

## Current historical-evidence qualification — LF-02 repair, 2026-10-03

Architect adjudication: `PASS_D01_LEARNING_RECORD_ADJUDICATION`.
The accepted lesson concept remains Intake Feeder Coordination, with predecessor `D01_L10_Basic_Integrated_Robot`.

**The historical COMPLETE / FROZEN declaration is preserved. Final clean-build verification is not established by the retained record.**

```text
FINAL CLEAN BUILD: NOT ESTABLISHED IN RETAINED EVIDENCE
```

| Retained evidence | Interpretation |
| --- | --- |
| Historical lesson declaration | COMPLETE / FROZEN; preserved, not a new lifecycle transition. |
| Earlier/baseline clean build | Recorded `BUILD SUCCESSFUL in 1m 41s`; baseline evidence only. |
| Historical generic Build row | `PASS` remains preserved below; it does not establish accepted final clean-build verification. |
| Later recorded final clean-build result | `PENDING FINAL CLEAN BUILD`; preserved without conversion to PASS. |
| Simulation, Driver Station / Glass, and real robot | NOT TESTED in the retained record; unchanged. |

No accepted final clean-build PASS is newly asserted. This qualification does not run a build, change the historical COMPLETE / FROZEN declaration, or assert new test/runtime/hardware verification.

## Preserved historical lesson record

All original content below remains unchanged, including the baseline build result, generic Build PASS row, later PENDING FINAL CLEAN BUILD result, and COMPLETE / FROZEN declarations. This qualification governs their interpretation without rewriting their chronology.

---

<!-- LF-02 PRESERVED HISTORICAL CONTENT BEGIN -->
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
