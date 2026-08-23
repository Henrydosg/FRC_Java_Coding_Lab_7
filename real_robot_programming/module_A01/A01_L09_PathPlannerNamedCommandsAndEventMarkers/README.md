# A01_L09 - PathPlanner NamedCommands and Event Markers

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`
- Previous lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Architecture Review: `PASS`
- Production implementation: `PASS`
- Build: `PASS` - user-verified Gradle/WPILib build; repository evidence also
  records compileJava, compileTestJava, focused tests, full tests, and an
  isolated clean build as PASS.
- Full test suite: `446/446 PASS` - 0 failures, 0 errors, 0 skips.
- Simulation: `PASS / USER-VERIFIED`
- Driver Station / event telemetry: `PASS / USER-VERIFIED`
- Real Robot: `PASS / USER-VERIFIED`
- Transition Guide: `FINAL / PASS`
- Git Commit / Push: `NOT TESTED` - user-owned; Codex ran no Git operations.

## Authoritative Objective

Teach one concept: controlled PathPlanner event-marker dispatch through
NamedCommands, a validated typed binding, and a fresh WPILib Command, without
adding mechanism architecture or changing the Frozen Backbone.

The approved L09 binding is a safe, observable, deterministic non-mechanism
demonstration. `LEARNING_EVENT` is not Intake, Feeder, Flywheel, scoring, or
fake mechanism integration.

## Event Architecture

```text
PathPlanner path
    -> event marker
    -> NamedCommands
    -> AutonomousEventBinding
    -> Commands.defer(...)
    -> fresh AutonomousEventDemonstrationCommand
    -> immutable AutonomousEventObservation
    -> AutonomousEventTelemetryFacade
    -> NetworkTables / Glass
```

`AutonomousEventBinding` owns the stable event ID, a `Supplier<Command>`, and
an immutable explicit requirement set. `AutonomousEventRegistration` rejects
duplicate names, invalid suppliers, null commands, requirement mismatches, and
any event command that requires `SwerveSubsystem`. Every marker dispatch
therefore resolves a fresh command instance.

The demonstration command is bounded to `0.50 s`, has no subsystem requirement,
does not access hardware or NetworkTables directly, and publishes deterministic
`STARTED`, `ACTIVE`, `COMPLETED`, `CANCELLED`, or `FACTORY_FAILURE` lifecycle
observations.

## Routine and Path Contract

- `SAFE_STOP` remains the chooser default.
- `ONE_METER_PATH` remains the inherited event-free routine.
- `ONE_METER_WITH_EVENT` is explicit and non-default.
- `A01_L09_OneMeter_With_Learning_Event.path` contains exactly one
  `LEARNING_EVENT` named-command marker at relative position `0.5`.
- The inherited `A01_L06_OneMeter_Forward.path` remains unchanged.
- The path follower owns `SwerveSubsystem`; the learning event does not.
- Path execution continues while the no-requirement event executes.

## Preserved Architecture

- Frozen Backbone and Frozen Interface Contract: preserved.
- `RobotContainer`: composition root only; it creates, registers, and injects
  the approved event components.
- Alliance transform owner: A01/L04 `FieldAllianceTransform` only.
- AutoBuilder vendor flipping: disabled with `shouldFlipPath = false`.
- Execution paths: `preventFlipping = true`.
- Drivetrain requirement and localization owner: `SwerveSubsystem`.
- Centralized stop: `SwerveSubsystem.stop()`.
- Failure behavior: fail closed for invalid readiness, alliance, bindings,
  asset data, requirements, command construction, mode loss, and cancellation.
- Re-enabling Autonomous without a new accepted readiness/reset sequence does
  not restart the previous routine.
- No D01 dependency, mechanism subsystem, mechanism IO, vendor mechanism API,
  mechanism contract, or fake mechanism implementation was introduced.

## Final Verification Evidence

### Automated evidence

- compileJava: `PASS`.
- compileTestJava: `PASS`.
- Focused L09 event, PathPlanner, routine-selection, and integration tests:
  `PASS`.
- Unchanged inherited regression tests: `384 PASS` across 41 test classes.
- Full suite: `446/446 PASS`, with zero failures, errors, or skips.
- Isolated clean build: `PASS`. The historical default-output clean lock on a
  stale Gradle problems report did not identify a production or configuration
  defect.

### User-owned runtime evidence

- Simulation: `PASS`.
- Blue Simulation: `PASS`.
- Red Simulation: `PASS`.
- `ONE_METER_WITH_EVENT`: `PASS`.
- Event dispatch: `PASS`; exactly one dispatch was observed.
- Event telemetry: `Active=false`, `DispatchCount=1`,
  `LastEvent="LEARNING_EVENT"`, `State="COMPLETED"`.
- Path/event concurrency: `PASS`; the path continued while the event ran.
- Disable/mode-loss stop: `PASS`.
- No automatic restart after re-enable without fresh readiness/reset: `PASS`.
- Real Robot: `PASS / USER-VERIFIED` on the real Swerve robot.

The ADR's historical `Real robot: HOLD` was a pre-verification gate. The ADR
states that this HOLD remains until the user supplies applicable hardware
evidence; the supplied real-robot PASS therefore satisfies and supersedes that
gate without changing verification ownership or rewriting ADR history.

## Explicitly Unclaimed and Deferred

This closure does not claim exact endpoint accuracy, final PID/feedforward
tuning, final physical characterization, competition readiness, mechanism
behavior, or D01 integration. Vision, AprilTags, dynamic replanning, obstacle
avoidance, game-specific autonomous strategy, and new mechanism architecture
remain outside A01.

## Final State

A01_L09 is `COMPLETE / FROZEN / READ-ONLY` and is the frozen completion point
for the approved A01 roadmap. No A01_L10 or next lesson/module has been created
or started.
