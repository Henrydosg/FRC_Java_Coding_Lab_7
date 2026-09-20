# M00_L04 Intake Command Ownership Learning Guide

English is normative. This guide teaches the verified M00_L04 software boundary; it does not claim physical Intake hardware behavior.

## 1. Lesson identity

- Course: FRC Java Coding Lab 7.0
- Module: M00 - Competition Mechanism Foundations
- Lesson: M00_L04 - Intake Command Ownership
- Lifecycle: `IN_PROGRESS / EDITABLE`
- One new concept: scheduler-managed manual ownership of the existing Intake capability

## 2. Why this lesson exists

M00_L03 created a complete vendor-neutral Intake capability, but it did not decide how an operator temporarily owns that capability. M00_L04 adds the command and controller binding that let WPILib's scheduler manage that ownership safely.

## 3. Previous knowledge from M00_L03

M00_L03 answered, “What capability does Intake own?” `IntakeSubsystem` owns Intake behavior, requested software state, `IntakeIO`, safe stop, and immutable Observation creation. Its IO and telemetry boundaries remain unchanged here.

## 4. The one new concept

M00_L04 answers, “Who is allowed to own the Intake capability while an operator is requesting it?” The `CommandScheduler` is the ownership manager, and `RunIntakeCommand` is the scheduled owner while active.

## 5. Big-picture control flow

```text
Driver
-> Xbox Controller
-> Right Bumper Trigger
-> RunIntakeCommand
-> IntakeSubsystem
-> IntakeIO
```

The command coordinates an existing capability. It does not access hardware or manufacture a second Intake state model.

## 6. Observation flow remains unchanged

```text
IntakeIO
-> mutable IntakeIOInputs
-> IntakeSubsystem
-> immutable IntakeObservation
-> read-only telemetry
```

Command ownership changes the requested software state through the subsystem. It does not move Observation responsibility into the command.

## 7. What a WPILib Command is

A command represents one schedulable robot action. It declares the subsystem resources it needs and provides lifecycle callbacks for starting, running, finishing, and ending. A command is not a motor controller, an IO adapter, or a hardware lock.

## 8. What CommandScheduler owns

The scheduler starts commands, calls their lifecycle methods, enforces subsystem requirements, interrupts conflicting owners, and cancels commands when a Trigger binding requests cancellation. It manages software ownership, not threads or electrical hardware.

## 9. Subsystem requirements

`addRequirements(intakeSubsystem)` tells the scheduler that `RunIntakeCommand` needs exclusive use of `IntakeSubsystem`. Two commands requiring that subsystem cannot be scheduled as simultaneous owners.

## 10. Why IntakeSubsystem is the requirement

The subsystem is the public behavior boundary for Intake. Requiring IO, telemetry, RobotContainer, or a vendor device would bypass the Frozen Backbone. Requiring exactly `IntakeSubsystem` expresses the real coordination boundary.

## 11. RunIntakeCommand structure

`RunIntakeCommand` is a final command with one retained dependency: the supplied `IntakeSubsystem`. It contains no controller, IO, telemetry, NetworkTables, vendor API, timer, sensor policy, or physical output value.

## 12. Constructor meaning

The constructor rejects a null subsystem, stores the supplied instance, and calls `addRequirements(intakeSubsystem)`. RobotContainer injects the existing Intake owner; the command does not construct another subsystem.

## 13. initialize()

`initialize()` runs when the command first becomes scheduled. In this lesson it calls `intakeSubsystem.requestIntake()` exactly once, recording the semantic Intake request at command start.

## 14. execute()

`execute()` runs repeatedly while scheduled, but it intentionally does not repeat `requestIntake()`. This lesson teaches scheduler lifetime ownership, not a 20 ms loop that repeatedly writes a mechanism request.

## 15. isFinished()

`isFinished()` returns `false`. The command does not decide to finish automatically; the Right Bumper Trigger controls its lifetime through scheduling and cancellation.

## 16. end(...)

`end(boolean interrupted)` always calls `intakeSubsystem.stop()`. The stop call is unconditional, so every ending path converges on the subsystem-owned safe-stop boundary.

## 17. Interrupted versus normal ending

`end(false)` represents normal completion and still stops defensively. `end(true)` represents cancellation or interruption and also stops. Although this command normally stays active until cancelled, both lifecycle paths are intentionally safe.

## 18. Safe-stop delegation

The command never calls `IntakeIO.stop()` directly. It delegates to `IntakeSubsystem.stop()`, which records `STOPPED`, refreshes the immutable Observation, and forwards the semantic stop request to IO. This is software safe-stop architecture, not proof of physical stopping.

## 19. Competing-command ownership transfer

When another command requiring `IntakeSubsystem` is accepted, WPILib interrupts the current owner. `RunIntakeCommand.end(true)` stops Intake before the new command initializes. This is scheduler mutual exclusion, not thread locking or hardware locking.

## 20. Right Bumper mapping

RobotContainer uses the semantic `rightBumper()` accessor on the existing controller at the inherited port. No raw button number is introduced, making the operator intent reviewable from the source.

## 21. whileTrue lifecycle

```java
driverController.rightBumper().whileTrue(runIntakeCommand);
```

When the button becomes true, the command is scheduled. While it remains true, the command remains scheduled. When it becomes false, the command is cancelled, `end(true)` runs, and the subsystem stop boundary is invoked.

## 22. Why no default Intake command

An Intake default command would claim the subsystem whenever it was otherwise free, which is not the concept being taught. `toggleOnTrue` could leave ownership active after release, and split `onTrue`/`onFalse` subsystem calls would bypass command ownership. `whileTrue` directly represents hold-to-own behavior.

## 23. RobotContainer composition responsibility

RobotContainer reuses the existing `IntakeSubsystem`, constructs one `RunIntakeCommand`, and creates the Right Bumper binding. It also preserves the inherited Back/View Prepare Autonomous binding.

## 24. What must not live in RobotContainer

RobotContainer must not call `requestIntake()` or `stop()` from the Trigger, own Intake state, access Intake hardware behavior, interpret sensors, or contain motor values. It remains the composition root, not a mechanism owner.

## 25. IntakeIOInputs versus IntakeObservation

`IntakeIOInputs` is a mutable one-cycle transport/input snapshot populated by IO. `IntakeObservation` is an immutable vendor-neutral observation/value snapshot created by the subsystem. Mutable transport data must not be exposed as the public Observation contract.

## 26. Available, Connected, and RequestedState

`Available` and `Connected` describe IO or hardware availability facts. `RequestedState` describes software intent owned by `IntakeSubsystem`. These values answer different questions and must not be treated as interchangeable.

## 27. IntakeIONoop meaning

The selected implementation is `IntakeIONoop`, a deterministic, vendor-neutral implementation for this bounded software lesson. It intentionally reports `Available=false` and `Connected=false`, accepts semantic method calls without producing physical output, and allows software architecture and scheduler behavior to be verified safely.

## 28. Focused-test evidence

Fourteen focused tests passed with `BUILD SUCCESSFUL in 28s`, four tasks executed, and exit code 0. They verify null rejection, exact requirements, one start request, no repeated execute request, command persistence, normal stop, cancellation stop, competing-owner order, disabled behavior, binding meaning, and architecture boundaries.

Evidence classification: `FOCUSED TESTS: VERIFIED`.

## 29. Full-regression evidence

The full clean regression passed with `BUILD SUCCESSFUL in 47s`, seven tasks executed, and exit code 0. This demonstrates that the new command boundary did not break the inherited automated suite.

Evidence classification: `FULL REGRESSION: VERIFIED`.

## 30. Simulation evidence

WPILib Simulation launched, stayed alive for more than two minutes, exposed the controller at `Joystick[0]`, provided NetworkTables connectivity, and showed the Intake node without an Intake command crash. Intake remained Noop, so no movement was expected.

Evidence classification: `SIMULATION VERIFIED / BOUNDED`.

## 31. Driver Station evidence

With Teleop enabled, the observed software sequence was:

```text
STOPPED
-> hold Right Bumper
-> INTAKE_REQUESTED
-> release Right Bumper
-> STOPPED
```

Throughout that sequence, `Available=false` and `Connected=false` remained expected because the implementation was `IntakeIONoop`. Evidence classification: `DRIVER STATION: VERIFIED / BOUNDED`.

## 32. What is not proven

No evidence proves a motor/controller type, CAN ID or bus, wiring, inversion, gearing, current limit, speed, direction, torque, sensor, game-piece acquisition, physical motion, or physical stopping. Glass was supporting read-only visualization and is `NOT APPLICABLE` as a distinct completion gate. Real hardware is `REAL HARDWARE DEFERRED`.

## 33. Common mistakes and debugging

- Repeating `requestIntake()` in `execute()` confuses scheduler ownership with repeated output control.
- Calling the subsystem directly from a Trigger bypasses command ownership.
- Omitting `addRequirements(...)` allows conflicting software owners.
- Conditional stop logic risks leaving requested state active.
- Treating `RequestedState` as proof of motor motion promotes software intent into a false hardware claim.
- Treating `Available=false` as a Driver Station failure ignores the selected Noop implementation.

## 34. Connection to M00_L05

M00_L04 remains about Intake ownership only. M00_L05 will introduce Feeder as a separate mechanism foundation after M00_L04 is fully reviewed, closed, frozen, and published. This lesson does not create Feeder code or coordinate Intake with Feeder.

## Student Questions

1. What single new concept does M00_L04 introduce?
2. Which component manages command ownership?
3. Why does `RunIntakeCommand` require `IntakeSubsystem`?
4. When is `initialize()` called, and what does it do here?
5. Why is `requestIntake()` absent from `execute()`?
6. Why does `isFinished()` return `false`?
7. What must `end(...)` do for both normal and interrupted endings?
8. What happens when a competing Intake command is scheduled?
9. What does `whileTrue` mean for press, hold, and release?
10. Why is no Intake default command configured?
11. What is RobotContainer allowed to do for this lesson?
12. How do `IntakeIOInputs` and `IntakeObservation` differ?
13. Why can `RequestedState` be `INTAKE_REQUESTED` while `Available` and `Connected` are false?
14. What does the bounded Driver Station evidence prove?
15. What physical Intake facts remain unverified?

## Answers

1. Scheduler-managed manual ownership of the existing Intake capability.
2. WPILib's `CommandScheduler`.
3. The subsystem is the public Intake behavior and ownership boundary, so it is the mutually exclusive scheduler resource.
4. It is called when scheduling begins and requests Intake once through `IntakeSubsystem`.
5. The subsystem already owns the requested state; the command teaches lifetime ownership rather than repeated output writes.
6. Button hold and cancellation determine command lifetime, not automatic completion.
7. It must unconditionally call `IntakeSubsystem.stop()`.
8. The current command is interrupted, stops through `end(true)`, and then ownership transfers.
9. Press schedules, hold keeps the command scheduled, and release cancels it and invokes the stop path.
10. Intake should be owned only by the explicit hold command in this lesson.
11. It may reuse and inject the subsystem, construct the command, and configure the binding.
12. IOInputs is mutable one-cycle transport; Observation is immutable vendor-neutral meaning.
13. `RequestedState` is software intent, while the other fields report IO/hardware availability; `IntakeIONoop` has no real hardware.
14. It proves the bounded controller-to-scheduler-to-software-state sequence, not physical motion.
15. All motor, CAN, wiring, direction, speed, current, sensing, acquisition, and physical-stop facts remain unverified and deferred.

## Evidence summary

- Theory: `VERIFIED`
- Focused tests: `VERIFIED`
- Full regression: `VERIFIED`
- Simulation: `SIMULATION VERIFIED / BOUNDED`
- Driver Station: `VERIFIED / BOUNDED`
- Glass: `NOT APPLICABLE` as a distinct completion gate
- Real hardware: `REAL HARDWARE DEFERRED`
- Lesson lifecycle: `IN_PROGRESS / EDITABLE`
