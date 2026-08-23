# A01_L08 to A01_L09 - Final Step-by-Step Transition Guide

## Activation Identity

- Source lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`.
- Source status: `COMPLETE / FROZEN / READ-ONLY`.
- Current lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`.
- Current status: `COMPLETE / FROZEN / READ-ONLY`.
- Authoritative title: `A01_L09 - PathPlanner NamedCommands and Event Markers`.
- Git: user-owned; Codex ran no Git commands.
- Implementation boundary: the approved software implementation and final
  user-owned Simulation and Real Robot evidence are recorded below. Git remains
  user-owned.

## Step 1 - Confirm Frozen L08 Predecessor

### Objective

Confirm that L08 is the immediate completed predecessor and protected
inheritance source.

### Why

The A01 ADR requires inheritance only from the immediately preceding
COMPLETE / FROZEN / READ-ONLY lesson.

### Action

Reviewed L08 identity, status, plan, checklist, transition record, source,
tests, learning guides, chooser, readiness, alliance, requirement, and stop
contracts.

### Files Changed

None.

### Verification

L08 records `COMPLETE / FROZEN / READ-ONLY` with user-confirmed build, test,
Simulation, and Real Robot evidence. L08 was not modified.

### Expected Result

L09 inherits only the frozen L08 snapshot.

## Step 2 - Record User-Owned Inheritance

### Objective

Record the user-owned copy into the L09 directory.

### Why

Repository governance assigns project copying, metadata preservation, runtime
verification, and Git operations to the user.

### Action

Audited `A01_L09_PathPlannerNamedCommandsAndEventMarkers` and its inherited
project structure.

### Files Changed

None during the copy; this guide records supplied history.

### Verification

`.wpilib`, `.vscode`, source, tests, vendordeps, Gradle files, documentation,
and PathPlanner assets were preserved. Generated artifacts were removed before
the inherited baseline.

### Expected Result

L09 begins as an inherited project, not a newly invented project.

## Step 3 - Accept the User-Owned Baseline

### Objective

Record the inherited WPILib VS Code baseline without claiming Codex executed it.

### Why

The user owns build verification and supplied the authoritative result.

### Action

Recorded compileJava, compileTestJava, tests, and clean build as PASS from the
user-confirmed inherited baseline.

### Files Changed

L09 documentation only during activation.

### Verification

User evidence reports the inherited baseline PASS; no L09 implementation delta
was present.

### Expected Result

L09 is eligible for documentation activation and a later implementation review.

## Step 4 - Preserve the Initial D01 Integration Audit HOLD

### Objective

Record why direct D01 mechanism integration is not available to A01_L09.

### Why

D01 is an independent Tank Drive WPILib project, while A01 is an independent
Swerve project. No approved shared command boundary exists.

### Action

Reviewed frozen D01 command evidence and the L09 dependency structure. Rejected
copying D01 code, direct A01-to-D01 dependency, fake mechanisms, and A01-owned
mechanism architecture.

### Files Changed

None during the audit.

### Verification

L09 has no D01 dependency or D01 mechanism command classes in its compile
boundary. The initial architecture audit is retained as `HOLD`.

### Expected Result

The lesson boundary remains honest and does not manufacture mechanism
integration.

## Step 5 - Record and Apply the Approved ADR Amendment

### Objective

Authorize demonstration-only event dispatch while preserving the A01 roadmap.

### Why

The event-dispatch concept remains teachable without violating independent
project boundaries.

### Action

Recorded the approved amendment: L09 may use safe, observable, deterministic
non-mechanism demonstration bindings; future approved robot integration may
replace those bindings without changing dispatch architecture; A01 must not
absorb mechanism architecture.

### Files Changed

- `docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`

### Verification

Architect approval and explicit User approval were supplied. The A01 lesson
number and nine-lesson roadmap order remain unchanged.

### Expected Result

The ADR explicitly resolves the original prerequisite blocker without adding a
lesson or authorizing production implementation.

## Step 6 - Lock the Minimum Event Contract

### Objective

Define the smallest understandable event binding that preserves future
substitution and scheduler safety.

### Why

NamedCommands accepts command instances, while the lesson requires fresh
construction and explicit requirements.

### Action

Locked the chain Path -> marker -> NamedCommands -> typed binding ->
`Commands.defer(...)` -> fresh Command. The typed binding carries a stable name,
`Supplier<Command>`, and `Set<Subsystem>` requirements. No provider interface is
authorized.

### Files Changed

L09 documentation only.

### Verification

PathPlannerLib 2026.1.2 API behavior was reverified. Duplicate names are
preflighted; silent overwrite is not accepted; no event may require Swerve.

### Expected Result

Implementation has one narrow, reviewable registration boundary.

## Step 7 - Lock the Demonstration and Observation Contract

### Objective

Define a safe learning binding without fake mechanism behavior.

### Why

The demonstration must prove lifecycle and observability while remaining
replaceable by a future approved real command.

### Action

Locked `LEARNING_EVENT` as the mechanism-independent name. The future
demonstration Command will have no hardware or drivetrain output, deterministic
termination, fresh construction, cancellation support, and immutable lifecycle
Observation published through the existing telemetry pattern.

### Files Changed

L09 documentation only.

### Verification

No direct NetworkTables access, fake mechanism, D01 dependency, or new
mechanism contract is authorized.

### Expected Result

Simulation can visibly prove dispatch and lifecycle without pretending to be
competition mechanism integration.

## Step 8 - Preserve L08 Path, Routine, Alliance, and Failure Contracts

### Objective

Carry all frozen safety contracts into L09.

### Why

Event dispatch must not alter routine selection, transform ownership, or stop
authority.

### Action

Preserved `SAFE_STOP` as chooser default, `ONE_METER_PATH`, selection snapshot,
one-shot readiness, no automatic restart, centralized Swerve stop, L04 as the
sole transform owner, `shouldFlipPath=false`, and `preventFlipping=true`.
Locked a future separate non-default event routine and new L09 event path; the
inherited L08 path remains event-free and unchanged.

### Files Changed

L09 documentation only.

### Verification

No L08 source, tests, configuration, or PathPlanner asset was modified.

### Expected Result

Future event execution remains one-transform, scheduler-owned, and fail-closed.

## Step 9 - Activate Documentation and Stop Before Implementation

### Objective

Make L09 the single active lesson while stopping at the approved documentation
boundary.

### Why

Only the active lesson is editable; implementation requires a separate design
review and authorization.

### Action

Updated the repository README, L09 README, LESSON_STATUS, LESSON_PLAN,
LESSON_CHECKLIST, the A01 ADR, and this transition record. Recorded L08 frozen
status, the copy history, baseline PASS, initial D01 HOLD, approved amendment,
and the absence of implementation.

### Files Changed

- repository `README.md`
- `docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`
- L09 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, and
  `LESSON_CHECKLIST.md`
- `docs/A01_L08_to_A01_L09_Step_by_Step.md`

### Verification

L09 is `IN_PROGRESS / EDITABLE`; L08 remains `COMPLETE / FROZEN / READ-ONLY`.
No production Java, tests, PathPlanner assets, D01 files, or Git operations
were changed. The transition guide is an activation record, not a final lesson
closure guide; it remains `IN_PROGRESS` until implementation and verification
are complete.

### Expected Result

Documentation activation and ADR amendment are complete. The next step requires
separate ChatGPT implementation design review and authorization.

## Pre-Closure Boundary

- L09 implementation: `PASS / SOFTWARE SCOPE`.
- Software verification: compileJava, compileTestJava, focused tests, full
  `446/446` suite, and isolated clean build `PASS`.
- Simulation: `NOT TESTED - USER OWNED`.
- Driver Station / Glass: `NOT TESTED - USER OWNED`.
- Real Robot: `HOLD`.
- Exact endpoint accuracy, final PID/feedforward tuning, and final physical
  characterization: explicitly unclaimed.
- Git commit and push: user-owned and not run by Codex.

## Step 10 - Audit and Preserve PathPlanner Event Features

### Objective

Extend the existing path boundary only for the one approved learning marker.

### Why

The inherited L08 execution-path factory rejected event markers rather than
silently discarding them. L09 must preserve exactly one approved event while
keeping the event-free L08 behavior unchanged.

### Action

Added a dedicated event-path loader and an event-preserving execution-path
factory method. The existing event-free loader and factory continue to reject
event markers. Validation accepts only one `LEARNING_EVENT` marker at relative
position `0.5`, with a non-null registered named command.

### Files Changed

- `src/main/java/frc/robot/commands/PathPlannerTrajectoryAdapter.java`
- `src/main/java/frc/robot/commands/PathPlannerExecutionPathFactory.java`
- `src/main/java/frc/robot/commands/AutoBuilderContractAdapter.java`

### Verification

PathPlanner event and transform tests pass, including Blue identity, Red one-
transform behavior, marker preservation, and event-free regression rejection.

### Expected Result

The event marker remains attached to the transformed path without enabling
unsupported future PathPlanner features or a second alliance transform.

## Step 11 - Implement the Typed Binding and Demonstration Lifecycle

### Objective

Implement one immutable, fresh, observable, mechanism-independent event path.

### Why

The lesson teaches the dispatch contract, not a mechanism or a registry
framework.

### Action

Added `AutonomousEventId`, `AutonomousEventBinding`,
`AutonomousEventRegistration`, `AutonomousEventDemonstrationCommand`,
`AutonomousEventObservation`, and `AutonomousEventTelemetryFacade`. The one
stable identifier is `LEARNING_EVENT`; registration uses `Commands.defer` and
rejects duplicate names, invalid suppliers, null results, requirement mismatch,
and Swerve requirements. The demonstration command uses the explicit `0.50 s`
duration and an injectable monotonic clock.

### Files Changed

- `src/main/java/frc/robot/commands/AutonomousEventId.java`
- `src/main/java/frc/robot/commands/AutonomousEventBinding.java`
- `src/main/java/frc/robot/commands/AutonomousEventRegistration.java`
- `src/main/java/frc/robot/commands/AutonomousEventDemonstrationCommand.java`
- `src/main/java/frc/robot/observation/AutonomousEventObservation.java`
- `src/main/java/frc/robot/telemetry/autonomous/AutonomousEventTelemetryFacade.java`

### Verification

Binding, lifecycle, registration, factory-failure, cancellation, fresh-command,
and telemetry tests pass. The event command has no hardware access, no
mechanism dependency, and no Swerve requirement.

### Expected Result

Every marker dispatch gets a fresh command and emits an immutable lifecycle
Observation without allowing telemetry to control robot behavior.

## Step 12 - Wire the Composition Root and Event Routine

### Objective

Expose one explicit non-default event routine while preserving the frozen L08
chooser and autonomous lifecycle.

### Why

RobotContainer is the composition root; it must register the event before the
event path is loaded and must not own event timing or telemetry aggregation.

### Action

Added the L09 duration and asset constants, registered the one event in
`RobotContainer`, added `ONE_METER_WITH_EVENT`, and reused the existing
AutoBuilder/Swerve path command boundary. Added exactly one new event asset with
the explicit PathPlanner named-command JSON node.

### Files Changed

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/commands/AutonomousRoutineFactory.java`
- `src/main/deploy/pathplanner/paths/A01_L09_OneMeter_With_Learning_Event.path`

### Verification

Routine-selection and integration tests pass. `SAFE_STOP` remains default,
`ONE_METER_PATH` remains unchanged, the event routine requires Swerve only for
the path follower, and `shouldFlipPath=false` / `preventFlipping=true` remain
verified.

### Expected Result

The path follower continues while the no-requirement learning event runs, and
only the existing autonomous lifecycle owns drivetrain termination.

## Step 13 - Run Software Verification

### Objective

Verify the implementation and inherited regressions in the mandated order.

### Why

The active lesson must prove its new contract without weakening frozen tests or
architecture.

### Action

Ran compileJava, compileTestJava, focused event/PathPlanner/routine tests, the
full suite, and a clean build in an isolated clean output directory. The
default clean task could not remove a pre-existing locked Gradle problems report;
the project configuration was not changed.

### Files Changed

No source files were changed by verification.

### Verification

`compileJava PASS`, `compileTestJava PASS`, focused tests `PASS`, full suite
`446/446 PASS`, and isolated clean build `PASS`. No Simulation, deployment, or
real-robot test was run.

### Expected Result

The software implementation is verified while user-owned Simulation, Driver
Station / Glass, Real Robot, and Git gates remain open.

## Step 14 - Record the Active-Lesson Handoff

### Objective

Document the implementation without closing or freezing L09.

### Why

Simulation and hardware evidence are outside Codex authority, and the user
explicitly requires L09 to remain editable.

### Action

Updated the active L09 README, status, plan, checklist, and this transition
record. Preserved exact endpoint, final PID/feedforward tuning, and final
physical characterization as unclaimed.

### Files Changed

- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `docs/A01_L08_to_A01_L09_Step_by_Step.md`

### Verification

L09 remains `IN_PROGRESS / EDITABLE`; L08 remains `COMPLETE / FROZEN /
READ-ONLY`. No Git/GitHub command, deployment, D01 modification, or frozen L08
modification was made.

### Expected Result

The implementation is ready for ChatGPT implementation review and later
user-owned Simulation verification, but L09 is not COMPLETE/FROZEN.

## Step 15 - Reconcile Final User-Owned Runtime Evidence

### Objective

Record the supplied Simulation, event, safety, and real-robot results without
claiming measurements that were not supplied.

### Why

The A01 ADR requires Simulation before real-robot verification and keeps the
hardware gate visible until the user supplies applicable evidence.

### Action

Reconciled user-confirmed Simulation PASS for Blue and Red,
`ONE_METER_WITH_EVENT`, one `LEARNING_EVENT` dispatch, terminal telemetry,
path/event concurrency, Disable/mode-loss stop, no automatic restart, and Real
Robot PASS on the real Swerve robot. Interpreted the ADR's historical `Real
robot: HOLD` as the now-satisfied pre-verification gate, consistent with the
ADR verification and ownership policy.

### Files Changed

Documentation only.

### Verification

The supplied telemetry was `Active=false`, `DispatchCount=1`,
`LastEvent="LEARNING_EVENT"`, and `State="COMPLETED"`. No endpoint accuracy,
PID/feedforward tuning result, physical characterization, mechanism behavior,
or competition-readiness result was inferred.

### Expected Result

All user-owned L09 runtime gates are accurately recorded as PASS without an ADR
amendment or unsupported claim.

## Step 16 - Finalize Documentation and Freeze L09

### Objective

Complete the required documentation and close the final approved A01 lesson.

### Why

Repository governance requires a final transition guide and reconciled lesson
documentation before a lesson becomes COMPLETE / FROZEN / READ-ONLY.

### Action

Finalized the root README, L09 README, LESSON_STATUS, LESSON_PLAN,
LESSON_CHECKLIST, this transition guide, and the English and Vietnamese L09
learning guides. Recorded compileJava and compileTestJava PASS, focused L09
tests PASS, 384 unchanged inherited regression tests PASS, full suite 446/446
PASS, isolated clean-build PASS, and the supplied runtime evidence.

### Files Changed

- repository `README.md`
- L09 `README.md`
- L09 `LESSON_STATUS.md`
- L09 `LESSON_PLAN.md`
- L09 `LESSON_CHECKLIST.md`
- `docs/A01_L08_to_A01_L09_Step_by_Step.md`
- `docs/A01_L09_PathPlanner_NamedCommands_and_Event_Markers_Learning_Guide_EN.md`
- `docs/A01_L09_PathPlanner_NamedCommands_and_Event_Markers_Learning_Guide_VI.md`

### Verification

Frozen L01-L08, production Java, tests, PathPlanner assets, configuration, and
repository governance documents were unchanged during closure. The Frozen
Backbone, Frozen Interface Contract, exactly-one alliance transform,
requirement ownership, centralized stop, fail-closed behavior, and no-restart
contract remain preserved. Git/GitHub operations were not performed.

### Expected Result

A01_L09 becomes `COMPLETE / FROZEN / READ-ONLY`, completing the approved A01
roadmap. No A01_L10 or successor lesson/module is created or started.

## Final Transition State

- Source lesson: A01_L08 - `COMPLETE / FROZEN / READ-ONLY`.
- Current lesson: A01_L09 - `COMPLETE / FROZEN / READ-ONLY`.
- Architecture Review: `PASS`.
- Build and full tests: `PASS`.
- Simulation and Real Robot: `PASS / USER-VERIFIED`.
- Transition Guide: `FINAL / PASS`.
- Exact endpoint accuracy, final PID/feedforward tuning, and final physical
  characterization: explicitly unclaimed.
- Git/GitHub: user-owned; no operations performed.
- Next lesson/module: `NOT CREATED / NOT STARTED`.
