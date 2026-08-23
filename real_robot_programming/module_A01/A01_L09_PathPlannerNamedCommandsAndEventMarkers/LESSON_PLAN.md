# A01_L09 - PathPlanner NamedCommands and Event Markers - Final Plan

## Final State

- Lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`.
- Previous lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition - COMPLETE / FROZEN / READ-ONLY`.
- Status: `COMPLETE / FROZEN / READ-ONLY`.
- Architecture Review: `PASS`.
- Implementation: `COMPLETE`.
- Build and tests: `PASS`.
- Simulation: `PASS / USER-VERIFIED`.
- Real Robot: `PASS / USER-VERIFIED`.
- Git/GitHub: user-owned; not run by Codex.

## Completed Learning Objective

L09 teaches one concept: controlled PathPlanner event-marker dispatch using
NamedCommands, a validated typed event binding, and a fresh WPILib Command.
The implementation proves dispatch, lifecycle, requirements, observability,
concurrency, and failure behavior without adding mechanism architecture.

## Final Runtime Design

```text
PathPlanner path
    -> LEARNING_EVENT marker
    -> NamedCommands
    -> AutonomousEventBinding
    -> Commands.defer(...)
    -> fresh AutonomousEventDemonstrationCommand
    -> immutable AutonomousEventObservation
    -> AutonomousEventTelemetryFacade
```

The typed binding contains one stable event ID, a `Supplier<Command>`, and an
explicit immutable `Set<Subsystem>`. Duplicate names, invalid suppliers, null
commands, mismatched requirements, and any Swerve event requirement fail
closed. No `AutonomousEventCommandProvider` interface was introduced.

## Final Routine Design

- `SAFE_STOP`: safe chooser default.
- `ONE_METER_PATH`: preserved event-free inherited routine.
- `ONE_METER_WITH_EVENT`: explicit non-default L09 routine.
- Event path: one marker named `LEARNING_EVENT` at relative position `0.5`.
- Event duration: `0.50 s`.
- Path owns Swerve; the event owns no subsystem and may run concurrently.

## Preserved Contracts

- Frozen Backbone and Frozen Interface Contract.
- RobotContainer as composition root only.
- L04 as the sole alliance-transform owner.
- `shouldFlipPath = false`.
- `preventFlipping = true`.
- SwerveSubsystem as localization, drivetrain requirement, and centralized
  stop owner.
- One-shot accepted readiness and selection snapshot.
- Fail-closed invalid state and construction behavior.
- Disable/mode-loss stop, interruption/cancellation stop, and no automatic
  restart.
- No D01 dependency, mechanism IO, mechanism subsystem, mechanism business
  logic, vendor mechanism API, or fake mechanism.

## Completed Verification Plan

1. Governance and ADR review: `PASS`.
2. Frozen predecessor and inheritance review: `PASS`.
3. compileJava: `PASS`.
4. compileTestJava: `PASS`.
5. Focused L09 tests: `PASS`.
6. Unchanged inherited regression: `384 PASS`.
7. Full suite: `446/446 PASS`.
8. Isolated clean build: `PASS`.
9. Simulation Blue and Red: `PASS / USER-VERIFIED`.
10. Event dispatch, telemetry, and path/event concurrency: `PASS / USER-VERIFIED`.
11. Disable/mode-loss stop and no automatic restart: `PASS / USER-VERIFIED`.
12. Real Robot: `PASS / USER-VERIFIED`.
13. Documentation and transition guide: `FINAL / PASS`.

## ADR HOLD Reconciliation

The historical lesson entry `Real robot: HOLD` was the required user-owned
pre-verification state, not a permanent ban. The ADR says the HOLD remains
visible until the user supplies applicable hardware evidence. The supplied L09
real-robot PASS satisfies that gate, while the ADR's ownership and safety policy
remain unchanged.

## Closure Boundary

The lesson claims only successful event-marker dispatch and the supplied
runtime evidence. It does not claim exact endpoint accuracy, final
PID/feedforward tuning, final physical characterization, competition readiness,
or real mechanism behavior. A01 ends at L09. No A01_L10 or successor
lesson/module is created or started by this closure.
