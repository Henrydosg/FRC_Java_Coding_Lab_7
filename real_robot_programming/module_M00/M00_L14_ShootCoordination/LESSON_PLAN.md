# M00_L14 — Shoot Coordination Lesson Plan

## Current frozen lifecycle — 2026-09-26

M00_L14 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED. Active Lesson Count
is 0 and Current Active M00 Lesson is NONE. M00_L13 remains COMPLETE /
FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED.
Independent Activation Review returned
HOLD_M00_L14_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_RECONCILIATION_REQUIRED;
the bounded documentation repair and Independent Activation Re-review passed.
Implementation Authorization and Implementation Handoff also passed. Final
Independent Static Review, User focused tests, clean regression, and bounded
Simulation passed. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED. The initial Independent Closure Review HOLD was repaired by
PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION. Independent Closure Rereview
passed as PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW; Freeze Reconciliation is
complete. Independent Freeze Review is pending. Publication and Final
Publication Verification remain pending.

Accepted gates:
- PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD — 6 actionable tasks, 6 executed, BASELINE_BUILD_EXIT_CODE=0.
- PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT — 339/339 authored files inherited unchanged.
- PASS_M00_L14_FINAL_DESIGN_LOCK — FINAL_DESIGN_LOCK_PASS_READY_FOR_CONTROLLED_ACTIVATION.
- HOLD_M00_L14_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_RECONCILIATION_REQUIRED — preserved and resolved by bounded documentation repair.
- PASS_M00_L14_INDEPENDENT_ACTIVATION_REREVIEW.
- PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION and PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW.
- PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW.
- PASS_M00_L14_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 10s; 4 actionable tasks (3 executed, 1 up-to-date); FOCUSED_TEST_EXIT_CODE=0.
- PASS_M00_L14_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); CLEAN_REGRESSION_EXIT_CODE=0.
- PASS_M00_L14_USER_BOUNDED_SIMULATION; Disabled -> Teleoperated enabled -> Disabled; LAST_NATIVE_EXIT_CODE=0.
- HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED — documentation/evidence wording only; no production, architecture, runtime, test, or Simulation defect.
- PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION — bounded repair of LESSON_STATUS.md, LESSON_PLAN.md, and LESSON_CHECKLIST.md.
- PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW — INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION.
- Freeze Reconciliation — COMPLETE; Independent Freeze Review PENDING.

## Locked design — coordination logic only

Implement exactly frc.robot.commands.ShootCommand in frc.robot.commands.
It requires exactly FlywheelSubsystem and FeederSubsystem; it controls through
subsystem semantic APIs only and owns both requirements throughout its
scheduled lifetime.

Exact constructor signature and parameter order:

```java
ShootCommand(
    FlywheelSubsystem flywheel,
    FeederSubsystem feeder,
    double targetVelocityRpm
)
```

The constructor requires non-null Flywheel and Feeder subsystems and finite
targetVelocityRpm > 0.0. Invalid numeric input throws
IllegalArgumentException. Construction makes no mechanism output and mutates no
subsystem. targetVelocityRpm is caller-supplied semantic input, not a verified
real shooting RPM. No shooting or timing constant is added.

Readiness: consume only FlywheelObservation.readyAtSpeed(). Do not calculate
error/tolerance or add another readiness, validity, hysteresis, or timer
authority. Feeder admission requires available() && connected().
FeederObservation.requestedState() is software request state only and is not
physical transport proof. Do not create a command-local feedingRequested flag.

Successful initialize order: feeder.stop() once, then
flywheel.requestVelocity(targetVelocityRpm) once; do not feed or reissue that
Flywheel request from execute(). Execute:
when admission is true and state is not FEED_REQUESTED, request feed once;
when admission is false and state is FEED_REQUESTED, stop once; otherwise
issue no Feeder output. On normal readiness loss, Feeder stops and Flywheel
velocity intent remains active; later recovery may request feed.

The command never self-finishes. No Timer, timeout, feed duration, shot count,
or completion sensor is permitted. Every end(false) and end(true) attempts
Feeder stop then Flywheel stop, even if the first throws; if both throw,
Feeder remains primary and Flywheel is suppressed. If initial Feeder stop
throws in initialize(), do not request Flywheel velocity; attempt Flywheel
stop once. If Flywheel velocity request throws, do not repeat the completed
Feeder baseline stop; attempt Flywheel stop once. If Feeder requestFeed throws
in execute(), attempt Feeder stop once and Flywheel stop once even if Feeder
cleanup throws. If Feeder stop throws during readiness/admission loss in
execute(), do not retry Feeder stop in that call; attempt Flywheel stop once.
In each case, cleanup RuntimeExceptions are suppressed on the original
RuntimeException, which remains primary and is rethrown. No retries, silent
recovery, clamp, rewrite, fallback RPM, or routine Error catch.

## Scope and expected delta

Actual production delta: add only src/main/java/frc/robot/commands/ShootCommand.java. Constants.java,
RobotContainer.java, subsystems, IO, Observations, telemetry, vendor adapters,
and deploy files stay unchanged. There is no L14 binding. Elevator, Intake,
Swerve, vision, and autonomous mechanisms are excluded. There is no L14 driver
binding; the inherited Left Bumper manual Feeder command remains, with
scheduler requirements providing Feeder mutual exclusion. M00_L15 owns
Intake-to-Feeder Coordination; M00_L16 owns Mechanism Autonomous Event
Integration.

## Remaining workflow gates

The implementation, accepted verification, closure rereview, and Freeze
Reconciliation are complete. Independent Freeze Review is next. User-owned
publication and Final Publication Verification remain pending. Real hardware
remains deferred.

## Accepted focused-test proof plan — 18 @Test methods

The 18-method focused suite passed under PASS_M00_L14_USER_FOCUSED_TESTS. The
cases below record its intended proof coverage; arbitrary RPM values remain
TEST DATA ONLY and do not establish a physical shooting RPM.

### Constructor and ownership

- Prove the exact production class identity is
  `frc.robot.commands.ShootCommand` and the constructor signature and parameter
  order are exactly the signature recorded above.
- Prove null Flywheel and null Feeder inputs are rejected.
- Prove non-finite RPM, zero RPM, and negative RPM each throw
  `IllegalArgumentException`.
- Prove invalid construction produces no mechanism output and no subsystem
  mutation.
- Prove the command requires FlywheelSubsystem and FeederSubsystem, and does
  not require ElevatorSubsystem or IntakeSubsystem.

### Successful initialization and normal coordination

- Prove successful `initialize()` calls `feeder.stop()` exactly once and
  `flywheel.requestVelocity(targetVelocityRpm)` exactly once.
- Prove `targetVelocityRpm` is passed through unchanged, `initialize()` never
  calls `feeder.requestFeed()`, and `execute()` never reissues
  `flywheel.requestVelocity()`.
- Prove an unready Flywheel, unavailable Feeder, or disconnected Feeder each
  prevents a feed request; ready Flywheel plus available and connected Feeder
  requests feed.
- Prove unchanged ready state does not reissue `requestFeed()`.
- Prove readiness loss while feeding stops Feeder, and unchanged stopped/unready
  state does not reissue `stop()` while normal Flywheel velocity intent remains
  active.
- Prove readiness recovery may request feed again on the semantic transition.
- Prove Feeder availability loss and Feeder connection loss during feeding each
  stop Feeder.
- Prove `FeederObservation.requestedState()` is used only as software request
  state and `FEED_REQUESTED` is never treated as physical game-piece movement.
- Prove the command does not self-finish (`isFinished() == false`).

### `initialize()` failure cases

- If initial `feeder.stop()` throws, prove the original Feeder failure
  propagates, Flywheel velocity is not requested, Flywheel stop cleanup is
  attempted once, cleanup failure is suppressed on the original, and Feeder
  stop is not retried in that failing call.
- If `flywheel.requestVelocity()` throws after the baseline Feeder stop,
  prove the original request failure propagates, the baseline stop already
  occurred, Flywheel stop cleanup is attempted once, cleanup failure is
  suppressed on the original, velocity is not retried, and initialization does
  not continue normally.

### `requestFeed()` failure case

- Prove the original `requestFeed()` RuntimeException remains primary; Feeder
  stop and Flywheel stop cleanup are each attempted once; Flywheel cleanup is
  still attempted if Feeder cleanup throws; cleanup RuntimeExceptions are
  suppressed on the original; and `requestFeed()` is not retried.

### Readiness/admission transition `feeder.stop()` failure case

- Prove the original Feeder stop failure remains primary, Feeder stop is not
  retried in that same failing `execute()`, Flywheel stop cleanup is attempted
  once, cleanup failure is suppressed on the original, and cleanup does not
  depend on a later scheduler cycle.

### Terminal `end()` failure matrix

For both `end(false)` and `end(true)` where repository test architecture
supports direct lifecycle proof, prove both Feeder and Flywheel stops are
attempted in every case:

1. Feeder stop succeeds and Flywheel stop succeeds: `end()` returns normally.
2. Feeder stop throws and Flywheel stop succeeds: Feeder failure is rethrown.
3. Feeder stop succeeds and Flywheel stop throws: Flywheel failure is rethrown.
4. Both stops throw: Feeder failure remains primary, Flywheel failure is
   suppressed, and Feeder failure is rethrown.

The focused tests invoke `end(true)` directly and unit-test interrupted-end
cleanup semantics, including the applicable stop-failure behavior. They do
not schedule ShootCommand through CommandScheduler or cancel/interruption-drive
it through the scheduler; scheduler-driven integration is not proven by these
tests. Prove no retry loop and primary RuntimeException preservation with
suppressed cleanup failures for all locked exception paths.

### Named architecture and scope exclusion guards

Require test or architecture-guard proof that the implementation has:

- no alternate `ShootCoordinationCommand` production class;
- no duplicate Flywheel readiness calculation, command-local Flywheel
  tolerance, or command-local readiness timer;
- no command-local `feedingRequested` authority;
- no direct FlywheelIO or FeederIO access and no vendor API access;
- no telemetry ownership and no hardware construction;
- no Elevator, Intake, or Swerve dependency;
- no new Constants shooting RPM, spin-up timeout, feed-duration, or shot-duration
  authority; no Elevator shooting position, Feeder speed, or hardware calibration
  authority;
- no clamp, rewrite, or fallback of the caller-supplied target;
- no RobotContainer L14 binding;
- no NamedCommands integration or PathPlanner mechanism events;
- no M00_L15 Intake-to-Feeder coordination leakage; and
- no M00_L16 autonomous event integration leakage.

The bounded Simulation verified Disabled startup (Robot Disabled, not enabled,
DS attached, not E-stopped), Teleoperated enable (Robot Teleoperated, enabled,
DS attached, not E-stopped), and return to Disabled (not enabled, DS attached,
not E-stopped) with mechanism requested states STOPPED. No application crash
or scheduler/runtime exception was observed; normal shutdown completed with
LAST_NATIVE_EXIT_CODE=0. Since RobotContainer has no ShootCommand binding,
Simulation did not directly execute ShootCommand. It verifies application
startup, mode transitions, scheduler and integration stability, safe semantic
state, and clean exit only. No physical shot is claimed. Real hardware remains
deferred.
