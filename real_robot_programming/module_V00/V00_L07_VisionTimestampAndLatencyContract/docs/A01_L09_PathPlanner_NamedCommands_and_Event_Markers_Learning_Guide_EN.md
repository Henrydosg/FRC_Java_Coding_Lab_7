# A01_L09 - PathPlanner NamedCommands and Event Markers - Learning Guide

> Historical Phase 2A note: the working L09 project was reconstructed from
> final A01_L08 before the event feature was reapplied. The current L09 event
> implementation is complete and technically verified.

Current lesson content/state: `COMPLETE / FROZEN / READ-ONLY` after final
architecture and closure review PASS. Git publication remains pending User
commit/push and is not claimed complete here.

## 1. Learning Objective

A01_L09 teaches how a PathPlanner event marker dispatches one fresh WPILib
Command through NamedCommands while the path follower continues to own and
drive the Swerve drivetrain.

The lesson proves an event-dispatch boundary, not mechanism integration. The
only event is `LEARNING_EVENT`, a deterministic non-mechanism demonstration.

## 2. Inherited Foundation

L09 inherits these frozen contracts from L08 and earlier lessons:

- `SAFE_STOP` is the chooser default.
- `ONE_METER_PATH` is the known event-free routine.
- a routine selection is sampled once at autonomous start;
- accepted starting-pose readiness is one-shot;
- L04 owns the single Blue/Red alliance transform;
- AutoBuilder vendor flipping is disabled;
- every execution path uses `preventFlipping = true`;
- `SwerveSubsystem` owns localization, drivetrain requirements, and stop;
- Disable/mode loss stops motion; and
- re-enable without fresh readiness does not restart the old routine.

## 3. Dispatch Flow

```text
A01_L09 event path
    -> marker reaches relative position 0.5
    -> PathPlanner requests LEARNING_EVENT
    -> NamedCommands resolves the registered deferred command
    -> AutonomousEventBinding validates identity and requirements
    -> Commands.defer(...) invokes Supplier<Command>
    -> a fresh AutonomousEventDemonstrationCommand runs
```

Registration happens in `RobotContainer` before the event path is loaded.
`RobotContainer` creates and injects components; it does not implement event
timing, drivetrain behavior, or telemetry calculations.

## 4. Why Fresh Command Construction Matters

A WPILib Command has lifecycle state. Reusing one command instance across
markers or autonomous sessions can retain stale state or violate scheduler
expectations. L09 registers a deferred wrapper and calls the binding's
`Supplier<Command>` for each dispatch.

The registration boundary also checks that the returned command's requirements
exactly equal the binding's declared requirements. A null command, supplier
exception, mismatch, duplicate name, or Swerve requirement fails closed.

## 5. Requirement Ownership and Concurrency

The path-following command requires `SwerveSubsystem`. `LEARNING_EVENT` has an
empty requirement set. Therefore the scheduler can run both commands at the
same time without a drivetrain ownership conflict:

```text
Path follower: requires SwerveSubsystem -> drivetrain motion
Learning event: requires nothing       -> bounded lifecycle demonstration
```

The event cannot submit chassis speeds, stop modules, access IO, or imitate an
Intake, Feeder, Flywheel, or other mechanism.

## 6. Observation and Telemetry

The demonstration command publishes immutable
`AutonomousEventObservation` values. The telemetry facade consumes those
observations and owns NetworkTables publication.

```text
Command
    -> AutonomousEventObservation
    -> AutonomousEventTelemetryFacade
    -> /Autonomous/Event/*
```

The published topics are:

- `LastEvent`: stable event name;
- `State`: `STARTED`, `ACTIVE`, `COMPLETED`, `CANCELLED`, or
  `FACTORY_FAILURE`;
- `Active`: whether the event is currently active; and
- `DispatchCount`: number of observed `STARTED` transitions.

The command never calls NetworkTables directly, and telemetry never controls
robot behavior.

## 7. Path and Alliance Contract

`A01_L09_OneMeter_With_Learning_Event.path` is canonical Blue-frame data with
one named-command marker at relative position `0.5`. The L04 transform creates
a fresh Blue or Red execution path exactly once. AutoBuilder's
`shouldFlipPath` callback returns `false`, and the execution path sets
`preventFlipping = true`.

This prevents the event-enabled path from introducing a second transform. The
marker identity and relative path position remain valid for both alliances.

## 8. Failure and Safety Behavior

L09 fails closed for:

- missing or invalid readiness;
- unknown alliance;
- missing, malformed, or unsupported path data;
- duplicate or invalid event registration;
- supplier failure or invalid returned Command;
- event requirement conflict;
- nonfinite or backward event time;
- cancellation, Disable, or mode loss; and
- AutoBuilder/path execution fault.

Drivetrain terminal behavior remains centralized in
`SwerveSubsystem.stop()`. The event does not replace or bypass that authority.

## 9. Final Verification Evidence

Automated evidence:

- compileJava PASS;
- compileTestJava PASS;
- focused event, path, routine, and integration tests PASS;
- 384 unchanged inherited regression tests PASS; and
- full suite 446/446 PASS with an isolated clean build PASS.

User-owned runtime evidence:

- Blue Simulation PASS;
- Red Simulation PASS;
- `ONE_METER_WITH_EVENT` PASS;
- path continued while `LEARNING_EVENT` executed;
- event telemetry ended at `Active=false`, `DispatchCount=1`,
  `LastEvent="LEARNING_EVENT"`, and `State="COMPLETED"`;
- Disable/mode-loss stop PASS;
- no automatic restart PASS; and
- Real Robot PASS on the real Swerve robot.

## 10. Scope Boundary and Closure

The final evidence does not claim exact endpoint accuracy, final
PID/feedforward tuning, final physical characterization, competition readiness,
or real mechanism behavior. D01 remains an independent Tank Drive project and
no D01 mechanism contract is imported into A01.

A01_L09 is `COMPLETE / FROZEN / READ-ONLY` after final closure approval. Git
publication remains pending User commit/push. It is the approved final lesson
boundary of A01; no A01_L10 or next lesson/module has been created or started.
