# M00_L05 Feeder Foundation Learning Guide

English is normative. This guide teaches the verified M00_L05 software architecture and does not claim physical Feeder hardware behavior.

## 1. Lesson identity

- Course: FRC Java Coding Lab 7.0
- Module: M00 - Competition Mechanism Foundations
- Lesson: M00_L05 - Feeder Foundation
- Lifecycle: `IN_PROGRESS / EDITABLE`
- One new concept: Feeder as one independently owned transport mechanism capability

## 2. Learning objectives

By the end of this lesson, a student should be able to explain Feeder ownership, trace its control and observation paths, distinguish mutable IO transport from immutable semantic meaning, describe Noop and safe-stop behavior, classify the accepted evidence, and identify what belongs to M00_L06 instead.

## 3. Prerequisites

Students should understand the Frozen Backbone, IO and IOInputs, subsystem ownership, immutable Observations, read-only telemetry, RobotContainer composition, and the M00_L03/M00_L04 Intake lessons.

## 4. Relationship to M00_L03 and M00_L04

M00_L03 established Intake as an independently owned mechanism. M00_L04 added scheduler-managed manual command ownership for that existing Intake capability. M00_L05 returns to the Foundation level and creates a separate Feeder capability. It does not copy Intake behavior or introduce Feeder command ownership.

## 5. Reuse architecture, not behavior

Feeder follows the same proven architecture pattern as Intake: IO contract, one-cycle inputs, subsystem ownership, immutable observation, read-only telemetry, and composition-root wiring. Reuse means preserving these responsibility boundaries. It does not mean that Intake and Feeder share state, methods, commands, hardware assumptions, or mechanism policy.

## 6. What a Feeder means in this lesson

Feeder is a transport mechanism capability with one semantic request: feed. M00_L05 models the software boundary only. It does not define a motor, direction, speed, game-piece path, sensor, jam policy, or successful physical transport.

## 7. Mechanism ownership

`FeederSubsystem` is the sole owner of Feeder software behavior and requested state. It owns one injected `FeederIO`, one mutable `FeederIOInputs`, the current requested state, and the latest immutable `FeederObservation`.

## 8. Control path

```text
future caller
-> FeederSubsystem
-> FeederIO
-> FeederIONoop in M00_L05
```

There is no current Feeder command or controller binding. “Future caller” marks the public subsystem boundary without implementing M00_L06 early.

## 9. Observation path

```text
FeederIONoop
-> mutable FeederIOInputs
-> FeederSubsystem
-> immutable FeederObservation
-> FeederTelemetryFacade
-> RobotTelemetry
```

RobotContainer selects and connects implementations. Telemetry remains observer-only.

## 10. FeederIO contract

`FeederIO` is vendor-neutral and declares exactly three semantic operations:

- `updateInputs(FeederIOInputs inputs)`
- `requestFeed()`
- `stop()`

The interface contains no vendor type, NetworkTables publisher, scheduler dependency, or physical hardware value.

## 11. FeederIOInputs

`FeederIOInputs` contains exactly two boolean instance fields: `available` and `connected`. It is a mutable one-cycle transport/input snapshot populated by the selected IO implementation. It is not the public semantic observation and must not be retained or exposed as one.

## 12. FeederIONoop

`FeederIONoop` is the only runtime Feeder implementation in M00_L05. Every update writes `available=false` and `connected=false`. `requestFeed()` and `stop()` are safe deterministic no-ops. Therefore the software path can run, but no physical output occurs.

## 13. FeederSubsystem initial state

A valid `FeederSubsystem` begins with requested state `STOPPED` and an immutable observation containing false availability, false connection, and `STOPPED`. Construction does not automatically request feed or stop output.

## 14. Requested-state vocabulary

The vocabulary is exactly:

- `STOPPED`: software is not requesting Feeder transport.
- `FEED_REQUESTED`: software requests Feeder transport.

These values describe software intent, not measured hardware motion.

## 15. requestFeed()

`FeederSubsystem.requestFeed()` first records `FEED_REQUESTED`, then refreshes the immutable observation, then calls `FeederIO.requestFeed()` exactly once per invocation. The IO callback therefore observes the already-updated software intent.

`FEED_REQUESTED` does not prove motor rotation, successful transport, game-piece motion, or hardware availability.

## 16. periodic()

`periodic()` asks IO to update the mutable inputs and then rebuilds a fresh immutable observation. It does not call `requestFeed()`, call `stop()`, publish NetworkTables directly, read a controller, or use a vendor API.

## 17. stop() and safe-stop ordering

The accepted ordering is:

```text
requestedState = STOPPED
-> rebuild FeederObservation
-> FeederIO.stop() exactly once
```

If `FeederIO.stop()` throws, the software intent and latest observation remain `STOPPED`. This is a software safe-stop invariant; it is not proof that unknown physical hardware stopped.

## 18. FeederObservation

`FeederObservation` is an immutable vendor-neutral record with exactly three components:

- `available`
- `connected`
- `requestedState`

It copies semantic values and never exposes the mutable `FeederIOInputs` object.

## 19. Available, connected, and requestedState

`available` answers whether the selected IO implementation can provide Feeder service. `connected` answers whether that available implementation reports a connected source. `requestedState` answers what software currently requests. The three facts must not be collapsed into one meaning.

## 20. FeederTelemetryFacade

`FeederTelemetryFacade` consumes one immutable observation and publishes only typed topics `Available`, `Connected`, and `RequestedState`. It has no Feeder control method, IO dependency, vendor dependency, or scheduler dependency.

## 21. RobotTelemetry

`RobotTelemetry` preserves existing Swerve, Vision, Intake, autonomous, and driver telemetry. Its Feeder addition obtains the subsystem's immutable observation and delegates publication to `FeederTelemetryFacade`. It does not request, stop, schedule, or configure Feeder behavior.

## 22. RobotContainer composition

RobotContainer performs exactly the composition work needed for this lesson:

```text
new FeederIONoop()
-> new FeederSubsystem(...)
-> new FeederTelemetryFacade(Feeder table)
-> RobotTelemetry
```

It does not add `FeederIOSim`, a real adapter, a CAN ID, a Feeder command, a controller binding, or a default Feeder command.

## 23. Why no command or binding exists yet

M00_L05 teaches capability ownership, not scheduler ownership. A Feeder command, subsystem requirement, hold behavior, and controller binding belong to M00_L06. Adding them now would violate the one-new-concept rule.

## 24. Why no real adapter exists yet

No verified Feeder motor/controller, device count, bus, ID, ratio, inversion, sensor, current limit, or output limit is available. A real adapter would require invented hardware facts. The safe architecture choice is `FeederIONoop` and `REAL HARDWARE DEFERRED`.

## 25. CAN reservation versus assignment

The canonical planning registry reserves CAN 45-49 for future Feeder work. A planning reservation protects namespace capacity; it does not identify an installed device. M00_L05 assigns no physical Feeder CAN ID, and CAN 45 must not be described as an actual Feeder device.

## 26. What bounded Simulation proves

WPILib Simulation showed a stable runtime and these topics:

```text
Feeder/Available      = false
Feeder/Connected      = false
Feeder/RequestedState = STOPPED
```

This verifies composition, Noop facts, observation flow, telemetry publication, and software state. Classification: `SIMULATION VERIFIED / BOUNDED`.

## 27. What bounded Simulation does not prove

Simulation does not prove real motor behavior, game-piece transport, current draw, gear ratio, inversion, physical speed, sensor behavior, or CAN configuration. No dynamic Feeder physics model exists in this lesson.

## 28. HALSIM Driver Station evidence

Verification used HALSIM Robot State inside Robot Simulation, not a physical Driver Station/roboRIO connection. In Disabled, Robot Enabled was No and DS Attached was Yes. In Teleoperated Enabled, Robot Enabled was Yes and DS Attached was Yes. Feeder remained false/false/`STOPPED` in both states. Classification: `SIMULATED DRIVER-STATION VERIFIED / BOUNDED`.

## 29. Focused-test evidence and the architecture-test lesson

The initial focused run completed 19 tests: 18 passed and one architecture test failed. The old assertion searched source text for `current` and accidentally matched the valid Javadoc phrase `current cycle`. Production `FeederIOInputs` was already correct.

The repair uses reflection over non-static, non-synthetic fields and requires exactly two boolean fields named `available` and `connected`. All six authorized focused classes then passed with `BUILD SUCCESSFUL in 26s`, exit code 0. Classification: `FOCUSED TESTS VERIFIED`.

Engineering lesson: architecture tests should inspect Java structure and semantics, not unrelated words in comments.

## 30. Full-regression evidence and scheduler isolation

The initial full clean regression completed 682 tests: 681 passed and `SwerveSubsystemKnownFieldPoseResetTest.scheduledPersistentResetPreservesBaselineAndCanResetAgainAfterMotion()` failed because `FeederSubsystem.periodic()` was invoked on a partial object with null IO. This was a `TEST ISOLATION / SHARED GLOBAL STATE DEFECT`; the production Feeder code and the inherited Swerve test were both correct. The null-constructor test had intentionally called `new FeederSubsystem(null)`. Java ran the `SubsystemBase` constructor first, which registered the object with the singleton `CommandScheduler`; subclass null rejection then threw, leaving global registration behind.

`FeederSubsystemTest` added `@AfterEach` cleanup using `CommandScheduler.getInstance().unregisterAllSubsystems()` while preserving null rejection. `cancelAll()` cancels scheduled commands but is not equivalent to clearing subsystem registration. The final full clean regression passed 682/682 tests with `BUILD SUCCESSFUL in 51s`, seven tasks executed, and exit code 0. Classification: `FULL CLEAN BUILD REGRESSION VERIFIED`.

## 31. Common student mistakes

- Treating `FEED_REQUESTED` as proof of motion or transport.
- Exposing mutable `FeederIOInputs` as the Observation.
- Publishing directly from IO or controlling Feeder from telemetry.
- Adding a Feeder command or controller binding in M00_L05.
- Inventing a motor, CAN ID, inversion, ratio, or current limit.
- Confusing CAN reservation 45-49 with verified hardware.
- Writing architecture tests that search arbitrary comment words.
- Cancelling commands but forgetting registered subsystem state in tests.

## 32. Debugging checklist

1. Confirm `FeederIONoop` is the selected runtime implementation.
2. Confirm inputs become false/false after every update.
3. Confirm the subsystem owns the requested state and latest observation.
4. Confirm `requestFeed()` updates intent before forwarding once.
5. Confirm `periodic()` issues no output calls.
6. Confirm `stop()` records and publishes `STOPPED` before IO stop.
7. Confirm telemetry publishes only immutable observation fields.
8. Confirm no Feeder command, binding, Constants entry, or physical CAN assignment exists.
9. If full-suite behavior differs from focused behavior, inspect singleton scheduler cleanup.

## 33. Architecture summary

M00_L05 preserves the Frozen Backbone: Noop IO updates mutable inputs; the subsystem owns behavior and creates immutable meaning; telemetry only publishes that meaning; RobotContainer composes the graph. Vendor APIs and unknown hardware remain outside the lesson.

## 34. Handoff to M00_L06

M00_L06 is `NOT ACTIVE / NOT CREATED`. After M00_L05 completes independent documentation review, closure, freeze, and publication, the next lesson may teach scheduler-managed manual Feeder command ownership. M00_L05 neither implements nor claims that future work.

## Student Questions

1. What single new concept does M00_L05 introduce?
2. What does “reuse architecture, not behavior” mean?
3. Which class owns Feeder requested state?
4. What fields are in `FeederIOInputs`?
5. How does `FeederIOInputs` differ from `FeederObservation`?
6. What does `FEED_REQUESTED` mean, and what does it not prove?
7. What is the exact ordering inside `stop()`?
8. What does `periodic()` do, and which output calls must it avoid?
9. Why is `FeederIONoop` the only runtime implementation?
10. Why is there no Feeder command or controller binding?
11. What does CAN 45-49 mean in this lesson?
12. What did bounded Simulation verify?
13. What did HALSIM Driver Station evidence verify?
14. Why was the architecture-test failure a test defect?
15. Why was `unregisterAllSubsystems()` needed after the null-constructor test?

## Answers

1. Feeder as one independently owned transport mechanism capability.
2. Reuse the proven IO/subsystem/Observation/telemetry boundaries without sharing Intake behavior or hardware assumptions.
3. `FeederSubsystem`.
4. Exactly `available` and `connected`, both boolean.
5. IOInputs is mutable one-cycle transport; Observation is immutable vendor-neutral semantic meaning.
6. It means software requests feed; it does not prove availability, motor rotation, transport, or game-piece motion.
7. Record `STOPPED`, rebuild the observation, then call `FeederIO.stop()` exactly once.
8. It updates inputs and rebuilds observation; it must not request feed or stop output.
9. Physical Feeder hardware facts are unknown, so Noop safely supports architecture learning without invented configuration.
10. Scheduler-managed Feeder ownership is the next lesson's concept, M00_L06.
11. It is reserved planning capacity, not a verified physical device assignment.
12. Composition, runtime stability, Noop facts, observation flow, telemetry, and software state.
13. Stable bounded software behavior in simulated Disabled and Teleoperated Enabled robot states, not physical DS/roboRIO behavior.
14. It matched a word in valid Javadoc rather than inspecting the actual Java field structure.
15. `SubsystemBase` registered the partial object before subclass null rejection; cancelling commands alone would not clear subsystem registration.

## Evidence summary

- Implementation: `COMPLETE / VERIFIED`
- Theory / architecture: `THEORY / ARCHITECTURE VERIFIED`
- Focused tests: `FOCUSED TESTS VERIFIED`
- Full regression: `FULL CLEAN BUILD REGRESSION VERIFIED`
- Simulation: `SIMULATION VERIFIED / BOUNDED`
- Simulated Driver Station: `SIMULATED DRIVER-STATION VERIFIED / BOUNDED`
- Real hardware: `REAL HARDWARE DEFERRED`
- Independent implementation review: `PASS`
- Documentation: `IMPLEMENTED / READY FOR INDEPENDENT DOCUMENTATION REVIEW`
- Lesson lifecycle: `IN_PROGRESS / EDITABLE / NOT FROZEN / NOT PUBLISHED`
