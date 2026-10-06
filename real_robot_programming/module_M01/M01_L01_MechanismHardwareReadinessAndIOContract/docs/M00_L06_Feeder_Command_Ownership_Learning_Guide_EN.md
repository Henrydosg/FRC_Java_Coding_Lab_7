# M00_L06 — Feeder Command Ownership Learning Guide

English is normative. The Vietnamese guide is an explanatory translation with the same structure and technical meaning.

## 1. Lesson identity and current state

M00_L06 adds Feeder command ownership to the frozen M00_L05 Feeder foundation. It is the sole `IN_PROGRESS / EDITABLE` lesson. Implementation and accepted verification are complete; independent closure review, freeze, and publication remain pending.

Evidence classification:

- `THEORY VERIFIED`
- `SIMULATION VERIFIED`
- `REAL HARDWARE DEFERRED`

## 2. Learning goals

After this lesson, a student should be able to:

- explain why a command owns a driver request but not mechanism behavior;
- describe `whileTrue(...)` scheduling, release, interruption, and disable behavior;
- explain why subsystem requirements prevent conflicting command ownership;
- distinguish command verification from real-hardware verification; and
- identify which later mechanism-integration topics remain outside M00_L06.

## 3. Foundation inherited from M00_L05

M00_L05 already established the Feeder IO interface, inputs snapshot, Noop implementation, subsystem, immutable observation, telemetry consumer, constants, and safe stop path. M00_L06 does not redesign those layers. The inherited mechanism flow remains:

`hardware -> FeederIO -> FeederIOInputs -> FeederSubsystem -> FeederObservation -> FeederTelemetry`

Telemetry remains read-only. The subsystem remains the owner of mechanism state and requests.

## 4. The one new concept

The only new architectural concept is command-based ownership of a Feeder request. `RunFeederCommand` translates the driver's held left bumper into a request to the existing subsystem. It does not access IO, mutable inputs, vendor APIs, NetworkTables, or telemetry.

## 5. Control architecture

The control path is:

`Driver left bumper -> Trigger binding -> RunFeederCommand -> FeederSubsystem`

The observation path remains separate and read-only. A command requests behavior; the subsystem decides and reports mechanism state through its existing contract.

## 6. Held-trigger scheduling

`whileTrue(...)` schedules the command when the left-bumper condition becomes true and keeps the command scheduled while the condition stays true. When the condition becomes false, WPILib cancels the command. Cancellation leads to `end(true)`, where safe stop is requested.

This means the binding expresses ownership duration. It does not repeatedly create control logic in `RobotContainer`.

## 7. Constructor and subsystem requirement

The command constructor receives `FeederSubsystem` through dependency injection and calls `addRequirements(feederSubsystem)`. The requirement tells the scheduler that this command owns the Feeder while scheduled. Another command requiring the same subsystem cannot own it simultaneously.

## 8. Initialize once

`initialize()` calls the subsystem's feed-request method once at schedule start. This is sufficient because the Feeder subsystem holds its requested state. Repeating the same request every scheduler loop would add noise without changing the lesson's behavior.

## 9. Execute and completion semantics

`execute()` is intentionally empty. `isFinished()` returns `false`, so the command does not end by itself. Its lifetime is controlled by the trigger, interruption, robot disable, or scheduler cancellation.

Empty `execute()` is deliberate design, not missing implementation.

## 10. End means safe stop

`end(boolean interrupted)` always requests Feeder stop. The same safe terminal action applies whether the left bumper is released, another command interrupts ownership, the scheduler cancels the command, or the robot becomes disabled.

The command does not need separate stop behavior for normal and interrupted endings.

## 11. Disabled behavior

The command does not opt into disabled execution. WPILib therefore prevents it from continuing when the robot is disabled. Its `end(...)` path requests stop, and Simulation confirmed that disabling while the bumper remained held left both Feeder and Intake stopped.

## 12. Why there is no default Feeder command

A default command would automatically reacquire the subsystem whenever it was idle. M00_L06 needs no background Feeder behavior, so no default is installed. With no command scheduled, the existing stopped state remains authoritative.

## 13. RobotContainer remains the composition root

`RobotContainer` creates `RunFeederCommand`, injects the Feeder subsystem, and binds the driver's left bumper. It does not calculate Feeder outputs, call IO, interpret mutable inputs, publish telemetry, or own mechanism state. This preserves its composition-root role.

## 14. Observation, telemetry, and Noop behavior

The command does not publish telemetry. It asks the subsystem for behavior; the subsystem produces the immutable Feeder observation; telemetry consumes that observation.

This lesson still uses `FeederIONoop`. Therefore `available = false` and `connected = false` are expected. A simulated request-state change proves command/subsystem logic, not physical motor response.

## 15. Simultaneous bumper input is not coordination

The bounded Simulation observed left-bumper Feeder requests while Intake remained stopped. Manually pressing multiple controls at once is not an approved Intake/Feeder coordination design. It does not establish sequencing, arbitration, interlocks, automatic transfer, or combined-command ownership.

## 16. Focused test design and scheduler isolation

The focused verification set contains:

- `RunFeederCommandTest`
- `RobotContainerFeederCommandBindingTest`
- `FeederCommandArchitectureBoundaryTest`
- `FeederArchitectureBoundaryTest`

The tests cover command lifecycle, requirements, binding behavior, disabled safety, and prohibited dependencies. Scheduler state must be cleaned between tests so one test's scheduled command or enable state cannot contaminate another test.

## 17. Expected inherited test contract evolution

The inherited `FeederArchitectureBoundaryTest` encoded the correct M00_L05 assumption that no Feeder command existed yet. M00_L06 intentionally introduces the roadmap-authorized command, so that narrow assumption had to evolve while all still-valid Feeder foundation boundaries remained protected.

This is classified as `EXPECTED INHERITED TEST CONTRACT EVOLUTION`. It is not a production defect and does not authorize unrelated test weakening.

## 18. Accepted build and regression evidence

The four focused test classes passed with `BUILD SUCCESSFUL in 7s`, four tasks up-to-date, and exit code 0.

The full clean regression passed with `gradlew clean build`, `BUILD SUCCESSFUL in 37s`, seven tasks executed, and exit code 0.

These results are accepted supplied evidence. This documentation phase did not rerun builds or tests.

## 19. Bounded Simulation evidence

The accepted checkpoints were:

| Checkpoint | Condition | Feeder | Intake |
|---|---|---|---|
| A | Robot disabled | `STOPPED` | — |
| B | Driver Station and Xbox connected while disabled | `STOPPED` | — |
| C | Teleop enabled, controls idle | `STOPPED` | — |
| D | Left bumper held | `FEED_REQUESTED` | `STOPPED` |
| E | Left bumper released | `STOPPED` | — |
| F | Left bumper held again | `FEED_REQUESTED` | `STOPPED` |
| G | Robot disabled while bumper remained held | `STOPPED` | `STOPPED` |

Accepted gates:

- `PASS_M00_L06_SIMULATION_LEFT_BUMPER_RELEASE_STOPPED`
- `PASS_M00_L06_SIMULATION_DISABLE_WHILE_HELD_STOPPED`
- `PASS_M00_L06_BOUNDED_SIMULATION_COMPLETE`

## 20. Evidence limits and protected future scope

No real Feeder hardware was present or verified. CAN IDs 45–49 are planning reservations only. This lesson makes no claim about motor-controller selection, device identity, wiring, direction, gearing, sensor behavior, current limits, PID, feedforward, calibration, tuning, physical safety, or real-robot operation.

M00_L14, M00_L15, and M00_L16 remain protected future scope. Nothing in this lesson authorizes real adapter integration, coordinated mechanism behavior, or curriculum reordering.

## 21. Student review

Confirm that you can answer these questions:

1. Why does the command require `FeederSubsystem` rather than `FeederIO`?
2. Why is an empty `execute()` correct here?
3. Which callback guarantees a stop request after release or interruption?
4. Why does Simulation with `FeederIONoop` not prove real hardware behavior?
5. Why is the inherited test change expected contract evolution rather than a defect?

Correct summary: the driver binding controls how long the command owns the request; the command requests behavior; the subsystem owns mechanism state; the IO boundary owns hardware access; and telemetry only observes immutable state.
