# M00_L15 — Intake-to-Feeder Coordination

## Current lifecycle — Freeze reconciliation, 2026-09-27

- Lifecycle: COMPLETE / FROZEN / READ-ONLY
- Active M00 lesson count: 0; current active lesson: NONE
- Independent Closure Rereview: PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW; M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION
- Freeze Reconciliation: COMPLETE / PASS_M00_L15_FREEZE_RECONCILIATION
- Independent Freeze Review: PENDING
- Publication: PENDING / NOT PUBLISHED
- Final Publication Verification: PENDING
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- M00_L16: FUTURE / INACTIVE / NOT CREATED

The implementation and verification record below remains unchanged. Freeze
reconciliation did not modify source or tests. It preserves the focused 22 / 22
PASS result, the 830 / 830 PASS clean regression, and bounded Simulation / DS
PASS. Simulation did not schedule `IntakeToFeederCommand`; scheduler behavior is
verified by the focused tests. Real hardware remains deferred.

## Historical lifecycle — Implementation and verification reconciliation, 2026-09-27

- Lesson: M00_L15 — Intake-to-Feeder Coordination
- Previous lesson: M00_L14 — Shoot Coordination
- Previous lesson state: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Previous primary SHA: `fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`
- Previous metadata SHA: `1a85c0827ee91ba7f70a595d94c49ad16a6df9cb`
- Lifecycle: IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
- Active M00 lesson count: 1; current active lesson: M00_L15
- Untouched-copy baseline: PASS_M00_L15_UNTOUCHED_COPY_BASELINE_BUILD; BUILD SUCCESSFUL in 53s; 6 actionable tasks, all executed; exit code 0
- Architecture / Inheritance Audit: PASS_M00_L15_ARCHITECTURE_INHERITANCE_AUDIT; 341/341 authored files identical to M00_L14
- Final Design Lock: PASS_M00_L15_FINAL_DESIGN_LOCK
- Controlled Activation: PASS_M00_L15_CONTROLLED_ACTIVATION
- Independent Activation Review / Rereview: PASS_M00_L15_INDEPENDENT_ACTIVATION_REREVIEW
- Implementation Authorization / Handoff: PASS_M00_L15_IMPLEMENTATION_AUTHORIZATION / PASS_M00_L15_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW
- Implementation: COMPLETE; production addition is only IntakeToFeederCommand.java
- Final Independent Static Review: PASS_M00_L15_FINAL_INDEPENDENT_STATIC_REVIEW
- Focused tests: PASS_M00_L15_USER_FOCUSED_TESTS; 22 / 22 PASS; 0 failures, 0 errors, 0 skipped
- Clean regression: PASS_M00_L15_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL; 830 tests, 0 failures, 0 errors, 0 skipped; BUILD_EXIT_CODE=0
- Bounded Simulation / Driver Station: PASS_M00_L15_BOUNDED_SIMULATION_DS_VERIFICATION; startup and DS attachment; Disabled Robot Enabled=No with Intake, Feeder, and Flywheel STOPPED; Teleoperated Robot Enabled=Yes and DS Attached=Yes with Intake and Feeder STOPPED; return to Disabled with Robot Enabled=No and Intake and Feeder STOPPED; no unintended L15 activation or fatal runtime/scheduler error; BUILD SUCCESSFUL; SIMULATION_EXIT_CODE=0
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- Documentation Reconciliation: COMPLETE; Independent Closure Review is the next gate
- Freeze / Publication: PENDING; M00_L15 remains active and is not frozen or published
- Real hardware: DEFERRED
- M00_L16: FUTURE / INACTIVE / NOT CREATED

## One new concept

Scheduler-managed coordination of the existing Intake and Feeder semantic
behaviors under one command lifecycle. This is software coordination only; it
does not establish physical mechanism movement, game-piece presence, transfer
completion, or hardware timing.

## Locked command contract

Add only `frc.robot.commands.IntakeToFeederCommand` with constructor
`IntakeToFeederCommand(IntakeSubsystem intake, FeederSubsystem feeder)`.
Reject either null dependency, retain the exact supplied subsystem references,
and require exactly IntakeSubsystem and FeederSubsystem. Construction performs
no mechanism output or subsystem mutation. Flywheel, Elevator, and Swerve are
excluded.

Each initialize lifecycle calls `intake.requestIntake()` once, then
`feeder.requestFeed()` once. `execute()` issues no repeated request;
`isFinished()` returns false. Both `end(false)` and `end(true)` stop Feeder
first and Intake second as deterministic software cleanup, with best-effort
attempts of both stops.

The locked exception policy handles RuntimeException only. If either initialize
request fails, preserve it as primary, do not continue to the next request, and
attempt `feeder.stop()` followed by `intake.stop()`. Suppress cleanup failures
on the primary in occurrence order. End always attempts both stops in the same
order; a Feeder stop failure remains primary and an Intake stop failure is
suppressed. An Intake-only end failure propagates. There are no retries,
fallback requests, swallowed primary failures, or Error catches.

## Focused test and evidence record

The focused test file is
`src/test/java/frc/robot/commands/IntakeToFeederCommandTest.java`. Its 22 test
methods comprise 18 direct-contract cases and four real CommandScheduler cases:
startup, cancellation, Intake requirement contention, and Feeder requirement
contention. The accepted result is 22 / 22 PASS, with no failures, errors, or
skips. Direct `end(true)` coverage proves method semantics only; the scheduler
cancellation case proves scheduler-driven interrupted cleanup.

The current M00_L15 copies of Feeder and Intake architecture boundary tests
were reconciled within this active lesson. The Feeder owner scan now uses the
JDK Java parser and syntax tree. The exact Java source sets are Intake-named
`{RunIntakeCommand.java, IntakeToFeederCommand.java}`, Feeder-named
`{RunFeederCommand.java, IntakeToFeederCommand.java}`, and FeederSubsystem
command owners `{RunFeederCommand.java, IntakeToFeederCommand.java,
ShootCommand.java}`. Frozen predecessor test files were not changed.

RobotContainer receives no M00_L15 binding. Preserve Right Bumper to
RunIntakeCommand and Left Bumper to RunFeederCommand. Do not add autonomous
bindings, NamedCommands, or PathPlanner event wiring. The scheduler's subsystem
requirements remain the only command-contention authority.

IntakeObservation and FeederObservation expose availability, connection, and
software requested state. Requested state is not proof of motor movement,
game-piece presence, transfer, or completion. The command does not gate its
semantic requests on Observations and adds no sensor assumptions, timer, delay,
debounce, current threshold, velocity threshold, or physical sequencing logic.

Because runtime Intake and Feeder implementations are Noop and RobotContainer
has no L15 binding, bounded Simulation did not execute this command. The
accepted Simulation verified startup and Driver Station attachment. In Disabled,
Robot Enabled was No and Intake, Feeder, and Flywheel RequestedState were
STOPPED. In Teleoperated, Robot Enabled and DS Attached were Yes and Intake and
Feeder remained STOPPED; no L15 command started automatically. On return to
Disabled, Robot Enabled was No and Intake and Feeder remained STOPPED, with no
unexpected mechanism semantic state active. No fatal runtime or scheduler error
occurred; the process ended with BUILD SUCCESSFUL and
SIMULATION_EXIT_CODE=0. The unassigned or unplugged controller on port 0
produced an EXPECTED / NON-BLOCKING button warning. Scheduler behavior is
covered by the focused tests. Real hardware remains DEFERRED.

## Historical next gate before closure rereview

At this earlier point, Independent Closure Review was PENDING and M00_L15 was
IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK. The accepted closure
rereview and current freeze state are recorded above and in the transition
guide. M00_L16 remains FUTURE / INACTIVE / NOT CREATED; no L17 is authorized.

---

## Inherited M00_L14 predecessor README (historical copy)

The following copied M00_L14 material is retained as predecessor context. Its
lesson identity and lifecycle statements are historical and do not describe
the current M00_L15 state.

### Historical M00_L14 frozen lifecycle and publication state — 2026-09-26

- Lesson: M00_L14 — Shoot Coordination
- Previous Lesson: M00_L13 — Elevator Travel-Limit Safety
- Previous Lesson State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Previous primary SHA: 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704
- Previous metadata SHA: 658d1e44c417763df3689b9b52e409161446c593
- Status: COMPLETE / FROZEN / READ-ONLY
- Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / NOT ACTIVE
- Active Lesson Count: 0
- Current Active M00 Lesson: NONE
- Accepted preparation: PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD; 6 actionable tasks, 6 executed; BASELINE_BUILD_EXIT_CODE=0
- Architecture / Inheritance Audit: PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT
- Final Design Lock: PASS_M00_L14_FINAL_DESIGN_LOCK
- Controlled Activation: PASS_M00_L14_CONTROLLED_ACTIVATION
- Independent Activation Review: PASS_M00_L14_INDEPENDENT_ACTIVATION_REREVIEW
- M00_L15: FUTURE / INACTIVE / NOT CREATED
- M00_L16: FUTURE / INACTIVE / NOT CREATED
- Implementation Authorization: PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION
- Implementation Handoff: PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW
- Final Independent Static Review: PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW
- Focused Tests: PASS_M00_L14_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 10s; 4 actionable tasks (3 executed, 1 up-to-date); exit code 0
- Clean Regression: PASS_M00_L14_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); exit code 0
- Bounded Simulation: PASS_M00_L14_USER_BOUNDED_SIMULATION; Disabled -> Teleoperated enabled -> Disabled; clean shutdown
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- Initial Independent Closure Review: HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED
- Documentation Proof Reconciliation: PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION
- Independent Closure Rereview: PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW / INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION
- Documentation Reconciliation: PASS_M00_L14_DOCUMENTATION_RECONCILIATION
- Freeze Reconciliation: PASS_M00_L14_FREEZE_RECONCILIATION
- Independent Freeze Review: PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW / INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION
- Primary Frozen Snapshot: PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT; SHA fa34556a3f1b7ef52b2c678a39c1083392d7c8d3
- Metadata Publication Reconciliation: PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION
- Metadata Publication Commit: COMPLETED / PASS_M00_L14_METADATA_PUBLICATION_COMMIT; identity external and not self-embedded
- Metadata Publication Commit Amendment: COMPLETED / PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION / PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND; canonical identity external and not self-embedded
- Remote Push: PENDING / USER-OWNED
- Final Publication Verification: PENDING / EXTERNAL

## Historical pre-Commit-2 metadata publication preparation — bounded repair

This section records the accepted state before the User created Metadata
Publication Commit 2. Its pending-commit language is historical and is
superseded by the current lifecycle at the top of this README.

The accepted `HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA`
found that the prior preparation had no real metadata delta after Commit 1.
This bounded documentation repair supplied that delta. Following M00_L13, the
metadata state intended for User-owned Commit 2 recorded M00_L14 as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED`. At that historical point, the
pre-commit worktree awaited the User's Commit 2; no Commit 2 identity was
claimed. Its own final SHA was external publication evidence and could not be
self-embedded. Final Publication Verification was PENDING / EXTERNAL, and no
third verification-only commit was part of the model. M00_L14 was NOT ACTIVE;
Active Lesson Count was 0 and Current Active M00 Lesson was NONE. M00_L15
and M00_L16 remain FUTURE / INACTIVE / NOT CREATED.

## Closure and freeze chronology

The initial Closure Review HOLD concerned documentation and evidence wording
only; it identified no production, architecture, runtime, test, or Simulation
defect. The bounded repair changed only LESSON_STATUS.md, LESSON_PLAN.md, and
LESSON_CHECKLIST.md. Independent Closure Rereview passed, and documentation-only
Freeze Reconciliation recorded the complete, frozen, read-only state. M00_L15
and M00_L16 remain future, inactive, and uncreated.

## Locked concept and command

The sole concept is coordination logic: scheduler-managed
frc.robot.commands.ShootCommand coordinates existing Flywheel ready-at-speed
semantics with Feeder request semantics. It requires exactly
FlywheelSubsystem and FeederSubsystem for the full scheduled lifetime.

The constructor accepts non-null Flywheel and Feeder subsystems and finite,
strictly positive caller-supplied targetVelocityRpm. Invalid numeric targets
throw IllegalArgumentException. The target is semantic configuration, not an
authoritative real-hardware shooting RPM. Construction performs no output or
subsystem mutation.

FlywheelObservation.readyAtSpeed() is the only Flywheel readiness authority.
The command does not recalculate RPM error or tolerance, inspect raw readiness
inputs, add hysteresis or a readiness timer, or keep a second readiness flag.

Feed admission requires all three conditions: Flywheel readyAtSpeed(),
Feeder available(), and Feeder connected(). Transition deduplication uses
FeederObservation.requestedState() as software request state only;
FEED_REQUESTED does not prove physical game-piece transport. The command
keeps no command-local feedingRequested state.

## Locked lifecycle and exception behavior

On successful initialize(), the command calls feeder.stop() once and then
flywheel.requestVelocity(targetVelocityRpm) once. It does not feed during
initialization or reissue the Flywheel request from execute(). On normal
execute(), it requests feed only when admission is
true and Feeder state is not FEED_REQUESTED; it stops only when admission is
false and Feeder state is FEED_REQUESTED. No unchanged state causes output
reissue. Readiness loss stops Feeder while leaving Flywheel velocity intent
active on the normal path. Admission recovery may request feed again.

isFinished() is always false. There is no timer, timeout, feed duration, shot
counter, or completion detector.

Every end(false) and end(true) attempts Feeder stop followed by Flywheel stop,
even if Feeder stop throws. A sole stop failure is rethrown; when both throw,
Feeder failure remains primary and Flywheel failure is suppressed.

For an initial feeder.stop() RuntimeException, initialize() does not request
Flywheel velocity; it attempts flywheel.stop() once, suppresses any cleanup
failure on the original Feeder exception, and rethrows the original. If
flywheel.requestVelocity() throws, the Feeder baseline stop is already
complete; initialize() attempts flywheel.stop() once, suppresses any cleanup
failure on the original request exception, and rethrows it. If
feeder.requestFeed() throws in execute(), the Feeder intent may already be
FEED_REQUESTED; execute() attempts feeder.stop() once and flywheel.stop() once,
still attempting Flywheel cleanup if Feeder cleanup throws, suppresses cleanup
failures on the original request exception, and rethrows it. If feeder.stop()
throws in execute() during readiness/admission loss, execute() does not retry
that Feeder stop and attempts flywheel.stop() once; any cleanup failure is
suppressed on the original Feeder exception. Cleanup occurs in the failing
lifecycle method. RuntimeExceptions are not retried, silently recovered,
clamped, rewritten, or replaced by fallback behavior. java.lang.Error is not
caught as routine mechanism recovery.

## Protected boundaries and implementation delta

- Constants.java and RobotContainer.java remain unchanged; there is no L14 driver binding or provisional shooting RPM.
- The inherited Left Bumper manual Feeder command remains; scheduler requirement ownership provides Feeder mutual exclusion.
- Flywheel + Feeder are the only required subsystems. Elevator, Intake, and Swerve are excluded.
- Existing IO, Inputs, Observations, requested-state enums, telemetry, vendor adapters, and deploy files remain unchanged.
- M00_L15 owns Intake-to-Feeder Coordination.
- M00_L16 owns Mechanism Autonomous Event Integration; no NamedCommands or PathPlanner mechanism events enter L14.
- Vision aiming, physical shot detection, shot completion, new IO, ShooterSubsystem, and hardware calibration are excluded.
- Actual production delta: added only src/main/java/frc/robot/commands/ShootCommand.java; existing production files remain unchanged.
- The 18 focused tests use controlled IO fakes; arbitrary test RPM values are TEST DATA ONLY.
- Runtime Flywheel and Feeder adapters are Noop, so runtime Simulation cannot prove a physical shot.

## Accepted verification evidence and limits

The bounded Simulation verified Disabled startup (Robot Disabled, not enabled,
DS attached, not E-stopped), Teleoperated enable (Robot Teleoperated, enabled,
DS attached, not E-stopped), and return to Disabled (not enabled, DS attached,
not E-stopped) with Feeder, Flywheel, and Intake requested states STOPPED. No
application crash or scheduler/runtime exception was observed; normal process
shutdown completed with LAST_NATIVE_EXIT_CODE=0. It supports application
startup, mode transitions, scheduler/integration stability, safe semantic
mechanism state, and clean shutdown. RobotContainer is unchanged and has no
ShootCommand binding, so the Simulation did not directly execute ShootCommand.
No physical shot or hardware performance is established.

The focused unit test invokes `end(true)` through a direct lifecycle call and
checks interrupted-end cleanup. It does not test CommandScheduler scheduling
or cancellation of ShootCommand.

Five independent static-review HOLD/repair rounds addressed test-only
architecture-guard defects. No production defect was found; the final
independent static review passed. The HOLD/repair chronology and classifications
are recorded in LESSON_STATUS.md and the transition guide.

The transition guide records the accepted Independent Freeze Review and
primary snapshot, followed by metadata reconciliation, Commit 2 completion,
and its completed amendment. The canonical identity remains external rather
than self-embedded.
The earlier HOLD_M00_L14_FINAL_PUBLICATION_VERIFICATION_METADATA_COMMIT_STATE_RECONCILIATION_REQUIRED
was resolved by the accepted User amendment. The subsequent
HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED
identified stale current-state chronology and is addressed by this documentation
reconciliation. Final Publication Verification remains pending, remote push is
pending User-owned verification, and real hardware remains deferred.
