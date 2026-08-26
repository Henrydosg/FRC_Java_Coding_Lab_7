# A01_L09 - PathPlanner NamedCommands and Event Markers

## Lesson State

- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Previous lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition - COMPLETE / FROZEN / READ-ONLY`
- Authoritative parent: `A01_L08 @ 135272c`
- Phase 2A baseline: `PASS` by user-authoritative verification.
- Phase 2B event implementation: `IMPLEMENTED / VERIFIED`.
- Technical verification: `PASS` - automated gates, Simulation, Driver Station /
  Glass, and real-robot verification are recorded below.
- Documentation reconciliation: `PASS`.
- Final Architecture Review: `PASS`.
- Final Closure Review: `PASS`.
- Git publication: `PENDING USER COMMIT/PUSH`.
- Lesson Content/State: `COMPLETE / FROZEN / READ-ONLY`.

## Scope

This lesson adds exactly one controlled learning concept: PathPlanner
NamedCommands and event markers. The event is the neutral, non-mechanism
`LEARNING_EVENT` event. It is registered with scheduler-native
`Commands.defer(...)`, creates a fresh command per dispatch, declares an
immutable requirement set, rejects Swerve requirements, and publishes an
immutable event observation through read-only telemetry.

The lesson adds one explicit `ONE_METER_WITH_EVENT` routine. It reuses the
final-L08 preparation, readiness claim, AutoBuilder execution, terminal
HOLDING, SAFE_STOP, Teleop gate, and no-automatic-restart contracts. The
existing `ONE_METER_PATH` routine remains unchanged.

## Architecture

- Event ID ownership is `frc.robot.autonomous.AutonomousEventId`.
- Observation remains vendor-neutral and does not depend on commands.
- Telemetry consumes observations only and does not schedule or cancel commands.
- The event command has no Swerve, IO, vendor, or mechanism dependency.
- `PrepareAutonomousCommand.java` is inherited exactly.
- `SafeAutoBuilderCommand` and manual child lifecycle delegation are absent.
- The final-L08 Robot scheduler exception boundary, coordinator fatal latch,
  first-fault preservation, centralized Swerve stop, terminal HOLDING,
  SAFE_STOP, Teleop gate, and no-restart behavior remain inherited.

## Asset Boundary

`src/main/deploy/pathplanner/paths/A01_L09_OneMeter_With_Learning_Event.path`
remains unchanged. It contains exactly one `LEARNING_EVENT` marker at relative
position `0.5`. The canonical Blue asset remains the source of the transformed
Red execution path; `shouldFlipPath` remains `false` and execution paths set
`preventFlipping` to `true`.

No Swerve, IO, CTRE, CANcoder, PID/feedforward, tuning, Gradle, vendordep, or
V00_L02 change is part of this lesson. `.Glass` is an operator-view
configuration artifact outside the production/test architecture boundary.

## Verification and Closure-Ready State

The historical Phase 2B environment/classpath hold is retained in the Phase 2B
record as an intermediate result. It was resolved by the later authoritative
verification evidence; no production repair or build-configuration workaround
was required for the L09 event feature.

Current technical verification is PASS:

- `compileJava`: PASS.
- `compileTestJava`: PASS.
- Focused L09 event, path, routine, integration, observation, and telemetry
  tests: PASS.
- 384 unchanged inherited regression tests: PASS.
- Full suite: `446/446 PASS`.
- Isolated clean build: PASS.
- Simulation: PASS for Blue and Red event/path, telemetry, coexistence,
  mode-loss stop, no automatic restart, and Teleop recovery.
- Driver Station / Glass: PASS for `/AutonomousEvent` and
  `/AutonomousPreparation` inspection.
- Real Robot: PASS for SAFE_STOP, the event-free baseline,
  `ONE_METER_WITH_EVENT`, LEARNING_EVENT, terminal HOLDING, no automatic
  restart, Disabled-to-Teleop recovery, marker preservation, and event/path
  coexistence on Blue and Red.

Phase 2A's user-authoritative evidence remains separate and valid:
`compileJava PASS`, `compileTestJava PASS`, full inherited test suite `PASS`,
and clean build `PASS` for the reconstructed final-L08 baseline.

## Governance

- A01_L08 remains `COMPLETE / FROZEN / READ-ONLY`.
- A01_L09 is `COMPLETE / FROZEN / READ-ONLY` after final closure review PASS.
- V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`.
- Git/GitHub remains user-owned; publication is pending User commit/push and
  Codex ran no Git operations.
