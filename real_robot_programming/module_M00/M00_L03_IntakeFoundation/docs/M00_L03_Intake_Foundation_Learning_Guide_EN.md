# M00_L03 - Intake Foundation Learning Guide

English is normative. This guide describes only the verified M00_L03 implementation and evidence.

## 1. Lesson Identity

- Course: FRC Java Coding Lab 7.0
- Module: M00 - Competition Mechanism Foundations
- Lesson: M00_L03 - Intake Foundation
- Filesystem: `M00_L03_IntakeFoundation`
- Predecessor: M00_L02 - Mechanism Hardware Evidence Audit
- Lifecycle: `IN_PROGRESS / EDITABLE`

## 2. Why This Lesson Exists

The robot needs a clean Intake software boundary before commands or controller bindings can own it. This lesson builds that reusable foundation while honestly leaving unknown physical hardware facts unresolved.

## 3. Learning Objectives

By the end of this lesson, a student can explain the Intake IO boundary, subsystem ownership, requested software state, immutable Observation flow, read-only telemetry, safe Noop composition, focused verification, and the boundary between M00_L03 and M00_L04.

## 4. What We Already Know from M00_L01 and M00_L02

M00_L01 taught reuse of the Frozen Backbone. M00_L02 taught that facts are not verified merely because they seem likely. M00_L03 applies both lessons: reuse the architecture and keep unsupported motor, wiring, direction, gearing, and sensor facts `UNKNOWN`.

## 5. The One New Concept

```text
AN INDEPENDENTLY OWNED, VENDOR-NEUTRAL INTAKE MECHANISM FOUNDATION
```

Everything added in M00_L03 supports this one capability. Scheduler-managed command ownership belongs to M00_L04.

## 6. Intake Foundation Architecture Overview

```text
RobotContainer
    -> IntakeIONoop
    -> IntakeSubsystem
    -> immutable IntakeObservation
    -> IntakeTelemetryFacade
    -> RobotTelemetry
```

The control and observation responsibilities remain separate. `IntakeSubsystem + IntakeIO` is the Intake ownership boundary.

## 7. IntakeIO — Why an IO Boundary Exists

`IntakeIO` separates mechanism meaning from hardware implementation. It defines `updateInputs(...)`, semantic `requestIntake()`, and `stop()`. It intentionally exposes no raw motor-percent API and no vendor type, CAN identity, controller model, inversion, gearing, current, velocity, position, PID, or sensor state.

## 8. IntakeIOInputs

The owned mutable one-cycle snapshot has exactly two fields:

- `available`: the selected implementation can provide Intake service.
- `connected`: the available implementation reports a connected source.

It is transport data, not the public immutable domain model.

## 9. IntakeIONoop

`IntakeIONoop` is a safe no-hardware implementation. It deterministically reports unavailable and disconnected, and its request and stop methods perform no physical action. It enables composition and bounded Simulation without pretending to simulate a motor.

## 10. IntakeSubsystem Ownership

`IntakeSubsystem` owns exactly one `IntakeIO`, its mutable inputs, the requested software state, and the current immutable observation. `periodic()` refreshes inputs and creates a fresh observation. The subsystem contains no controller binding, scheduling, automatic intake, or cross-mechanism coordination.

## 11. Requested Software State

The only states are:

- `STOPPED`
- `INTAKE_REQUESTED`

They express software intent only. They do not prove motor motion, roller motion, game-piece movement, or the real physical mechanism state.

## 12. requestIntake() Flow

```text
IntakeSubsystem.requestIntake()
    -> requested state = INTAKE_REQUESTED
    -> refresh immutable software observation
    -> IntakeIO.requestIntake()
```

The semantic request avoids inventing an arbitrary motor output value.

## 13. stop() / Safe-Stop Flow

```text
IntakeSubsystem.stop()
    -> requested state = STOPPED
    -> refresh immutable software observation
    -> IntakeIO.stop()
```

Focused tests verify this software call path. They do not prove that a real motor physically stopped. Physical safe-stop verification remains deferred with real hardware.

## 14. Immutable IntakeObservation

`IntakeObservation` is a Java record containing `available`, `connected`, and requested software state. It enforces that a connected source must also be available. Each periodic update creates a new value, so later mutations of `IntakeIOInputs` cannot alter a previously returned Observation.

## 15. Observation vs Control

An Observation describes what software currently knows. It does not request behavior. `IntakeObservation` contains no IO reference, vendor API, NetworkTables publisher, Command, scheduler call, or mutable mechanism state.

## 16. Intake Telemetry

`IntakeTelemetryFacade` publishes only `Available`, `Connected`, and `RequestedState` from an immutable `IntakeObservation`. `RobotTelemetry` connects the read-only source and facade. Telemetry never commands Intake, mutates Intake, schedules Commands, or calls vendor APIs.

## 17. RobotContainer Composition

`RobotContainer` constructs `IntakeIONoop`, injects it into `IntakeSubsystem`, creates the telemetry facade, and connects the Intake observation path to `RobotTelemetry`. It contains no Intake mechanism logic, command, button binding, default command, or scheduling behavior.

## 18. M00_L03 vs M00_L04

| M00_L03 - Intake Foundation | M00_L04 - Intake Command Ownership |
| --- | --- |
| IO contract and Inputs | Scheduler-managed Commands |
| Subsystem and requested state | Command requirements |
| Architecture-level stop | Controller and Trigger bindings |
| Immutable Observation | Default/manual ownership |
| Read-only telemetry | Interruption behavior |
| Noop composition | Scheduler ownership semantics |

The M00_L04 column is future work and is not implemented here.

## 19. Why There Is No Real Hardware Adapter Yet

No acceptable evidence currently selects a motor, controller, CAN bus, CAN ID, wiring, inversion, direction, gearing, current limit, sensor, or other physical configuration. A CTRE, REV, or other real adapter would therefore invent facts and is not authorized.

## 20. Why Constants.java Does Not Change

`Constants.java` remains the default configuration authority, but M00_L03 has no verified Intake hardware value to place there. Its accepted SHA-256 remained unchanged, and no Intake constant was added.

## 21. Focused Test Strategy

Six focused test classes cover the Noop, Observation value semantics, subsystem state and forwarding, fresh snapshot immutability, telemetry publication, RobotContainer composition, vendor isolation, safe-stop ordering, and M00_L04 leakage protection. Test-local fake IO models semantic calls, not physical motor output.

## 22. The Initial Test Failure and What We Learned

The first focused run completed 16 tests: 15 passed and 1 failed. `periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent()` threw `NullPointerException` because the test-local fake unconditionally dereferenced an optional callback. This was a `TEST DEFECT`, not a production defect. Only `IntakeSubsystemTest.java` was repaired by guarding the callback when configured; no assertion was removed and the snapshot intent remained intact.

## 23. Full Regression Verification

After the repaired focused suite passed, the User ran a clean Java 17.0.16 Temurin build. Result: `BUILD SUCCESSFUL in 20s`, 7 actionable tasks executed, exit code 0. Classification: `FULL REGRESSION VERIFIED`.

## 24. Bounded Simulation Verification

WPILib Simulation launched, the application stayed alive, the robot remained Disabled, an Intake NetworkTables node was visible, and safe Noop composition was active. Driver Station and Glass were not required. Classification: `SIMULATION VERIFIED` for bounded software architecture behavior.

## 25. What Simulation Proves

Simulation evidence supports application startup, Noop composition, Intake subsystem integration, Intake telemetry presence, and the bounded software data path.

## 26. What Simulation Does NOT Prove

Simulation does not prove CAN identity, wiring, motor/controller identity, physical direction, physical movement, current or load behavior, mechanism performance, game-piece handling, or physical safe stop.

## 27. Real Hardware Deferred

Real hardware classification is `REAL HARDWARE DEFERRED`. This is an honest evidence state, not a failure. A future separately reviewed real adapter must preserve the existing IO, subsystem, Observation, telemetry, and command boundaries.

## 28. Common Mistakes

- Treating `INTAKE_REQUESTED` as proof of physical movement.
- Adding a motor-percent method to `IntakeIO` without a verified need.
- Publishing mutable `IntakeIOInputs` directly.
- Letting telemetry call subsystem behavior.
- Adding Intake commands or bindings in M00_L03.
- Inventing CAN IDs or constants to make the foundation look complete.
- Calling `IntakeIONoop` a motor simulation.

## 29. Architecture Walkthrough

Current M00_L03 flow:

```text
RobotContainer
    -> constructs IntakeIONoop
    -> injects it into IntakeSubsystem
    -> subsystem processes IntakeIOInputs
    -> subsystem creates IntakeObservation
    -> IntakeTelemetryFacade consumes the Observation
    -> RobotTelemetry coordinates publication
```

Future M00_L04 control flow, not implemented in M00_L03:

```text
Xbox / Trigger
    -> Intake Command
    -> IntakeSubsystem
```

## 30. Knowledge Check

1. Why does `IntakeIO` exist?
2. Why are vendor APIs absent from the Intake foundation?
3. What is the role of `IntakeIONoop`?
4. What does `STOPPED` mean?
5. What does `INTAKE_REQUESTED` mean?
6. Why is requested state not proof of physical behavior?
7. What software path does `stop()` verify?
8. Why is `IntakeObservation` immutable?
9. What is telemetry allowed to do?
10. What is `RobotContainer` allowed to do for Intake?
11. Which Intake responsibilities belong to M00_L04?
12. Why are no Intake values added to `Constants.java`?
13. What do the focused tests verify?
14. What does bounded Simulation verify?
15. Why is real hardware still deferred?

## 31. Knowledge Check Answers

1. It separates vendor-neutral mechanism capability from hardware implementation.
2. No vendor or physical hardware choice has acceptable evidence or authorization.
3. It provides deterministic safe composition with no physical output.
4. It means software currently requests the stopped state; it is not physical proof.
5. It means software currently requests the semantic Intake action.
6. Software intent does not observe a real motor, roller, or game piece.
7. State changes to `STOPPED`, then the subsystem forwards `IntakeIO.stop()`.
8. A stable snapshot must not change when mutable IO inputs change later.
9. It may publish Observation fields only and must never control behavior.
10. Construct, select, inject, and connect dependencies only.
11. Commands, requirements, bindings, default/manual ownership, interruption, and scheduler semantics.
12. No verified Intake hardware configuration value exists.
13. State, forwarding, ordering, snapshots, immutability, Noop, telemetry, composition, and architecture boundaries.
14. Startup, Noop integration, subsystem presence, telemetry presence, and bounded software behavior.
15. No real adapter or verified physical configuration exists, and no physical test was performed.

## 32. Lesson Evidence Summary

| Evidence | Classification |
| --- | --- |
| Theory | `VERIFIED` |
| Focused tests | `VERIFIED` — repaired suite, 16/16, exit code 0 |
| Full regression | `VERIFIED` — clean build, exit code 0 |
| Simulation | `VERIFIED` — bounded software / Noop / composition only |
| Driver Station | `NOT APPLICABLE` |
| Glass | `NOT APPLICABLE` |
| Real hardware | `REAL HARDWARE DEFERRED` |

## 33. Exit Criteria

The authorized production/test scope, focused retest, full regression, bounded Simulation, independent implementation review, and bilingual guide implementation are complete. Independent documentation review, any authorized documentation repair, final closure build/review, lifecycle reconciliation, freeze authorization, and User publication remain pending. The lesson is not yet `COMPLETE / FROZEN / READ-ONLY`.

## 34. What Comes Next

The immediate next gate is independent documentation review. M00_L04 remains `NOT ACTIVE / NOT CREATED`; it may begin only after M00_L03 completes the required closure, freeze, and publication lifecycle and receives separate authorization.
