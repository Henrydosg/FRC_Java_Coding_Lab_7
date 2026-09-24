# M00_L13 — Elevator Travel-Limit Safety

## Current frozen lifecycle and verification — 2026-09-24

- M00_L12 predecessor: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED; primary SHA 5cc4c2c1bc6b4108f2c710c3ca0b0cdbdc26ba49; metadata SHA c1e90fad04469e5162b5c1814566dd534c6a0a6c; final gate PASS_M00_L12_FINAL_PUBLICATION_VERIFICATION.
- Preparation: PASS_M00_L13_UNTOUCHED_COPY_BASELINE_BUILD; User evidence BUILD SUCCESSFUL in 35s, 6 actionable tasks, 6 executed.
- Architecture / Inheritance Audit, Final Design Lock, Controlled Activation, activation documentation reconciliation, and Independent Activation Re-review: PASS.
- Implementation Authorization: PASS_M00_L13_IMPLEMENTATION_AUTHORIZATION.
- Implementation handoff: PASS_M00_L13_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW.
- Final Independent Static Re-review: PASS_M00_L13_FINAL_INDEPENDENT_STATIC_REREVIEW / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS.
- Focused tests: PASS_M00_L13_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 42s; 4 actionable tasks, 4 executed.
- Clean regression: PASS_M00_L13_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 25s; 5 actionable tasks, 5 executed; GRADLE_EXIT_CODE=0.
- Bounded Simulation: PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION; Disabled, Teleop Enabled, return-to-Disabled.
- Initial Closure Review HOLD_M00_L13_CLOSURE_REVIEW_STALE_AGENTS_IMPLEMENTATION_STATUS was repaired by PASS_M00_L13_CLOSURE_DOCUMENTATION_REPAIR and resolved by PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW / CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE.
- Freeze Reconciliation: COMPLETE. Independent Freeze Review: PENDING / NEXT GATE.
- Status: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED / NOT YET PUBLICATION-VERIFIED.
- Active Lesson Count: 0; Current Active M00 Lesson: NONE.
- Publication: PENDING / USER-OWNED; no M00_L13 publication SHA is established.

## Sole concept and implemented request-admission contract

> A vendor-neutral software operational travel envelope for Elevator closed-loop position requests, expressed in the logical position coordinate frame and enforced before an unsafe target can reach ElevatorIO.

This is request-admission travel-limit safety. ElevatorTravelLimits is an immutable, vendor-neutral logical-meter record with finite bounds and minPositionMeters < maxPositionMeters; invalid construction throws IllegalArgumentException. Finite negative and zero-crossing bounds are allowed. The configured interval is inclusive: exact minimum and maximum are accepted; outside targets are rejected. There is no epsilon, tolerance, clamp, or target rewrite. No robot-specific dimensions are embedded.

ElevatorSubsystem(ElevatorIO io) means no operational travel envelope is configured; no fake default limits exist. The bounded constructor requires non-null IO and limits. For requestPositionMeters, implemented validation order is: (1) finite target or IllegalArgumentException; (2) normalized positionValid && positionReferenced or IllegalStateException; (3) configured limits or IllegalStateException; (4) inclusive envelope membership or IllegalArgumentException. Only an accepted request records POSITION_REQUESTED and the exact target, rebuilds Observation, then calls io.requestPositionMeters(target) exactly once. Accepted IO exceptions propagate without retry or rollback; periodic does not reissue requests.

Every rejected request leaves requested state and target unchanged, does not rebuild Observation (the same Observation instance remains current), and makes no position, stop, or homing IO call. It does not automatically cancel previously recorded motion intent. An otherwise valid/referenced current position outside the envelope does not block an in-range requested target; recovery is caller-requested only, and periodic does not actuate it. This is not continuous or physical boundary protection.

requestHoming() does not require travel limits or valid/referenced position; it requires normalized available && connected, records HOMING with inherited 0.0 target placeholder, rebuilds Observation, and calls IO once. HomeElevatorCommand is unchanged. stop() is always available, records STOPPED and target 0.0, rebuilds Observation, and calls io.stop() once. periodic() performs one input update, normalization, and Observation rebuild without output.

## Preserved architecture and actual delta

ElevatorIO remains four methods; ElevatorIOInputs five fields; ElevatorRequestedState remains STOPPED, POSITION_REQUESTED, HOMING; ElevatorObservation and the read-only telemetry facade remain eight values. Constants.java and RobotContainer.java are unchanged. RobotContainer still composes new ElevatorSubsystem(new ElevatorIONoop()); there is no real travel envelope in runtime composition, real Elevator hardware, ElevatorIOSim, travel-limit binding, automatic homing, or autonomous mechanism integration.

Production comparison with frozen M00_L12: common 110, identical 109, changed 1, missing 0, added 1. ElevatorSubsystem.java is the one changed production file; ElevatorTravelLimits.java is the one added production file. ElevatorIO, ElevatorIONoop, ElevatorRequestedState, ElevatorObservation, HomeElevatorCommand, RobotContainer, Constants, telemetry, and concrete adapters remain unchanged.

Test comparison: common 103, identical 101, changed 2, missing 0, added 1. Modified tests are ElevatorSubsystemTest.java and ElevatorArchitectureBoundaryTest.java; ElevatorTravelLimitsTest.java was added. No aggregate JUnit test count is supplied.

The initial independent static review held on test/architecture-test defects, not production: precondition-order tests did not distinguish two IllegalStateException branches; an identifier guard could miss compound hardware-limit names; and an ElevatorIO method-name Set could miss overloads. PASS_M00_L13_TEST_GUARD_REPAIR changed only ElevatorSubsystemTest.java and ElevatorArchitectureBoundaryTest.java, adding distinct exception-message checks, compound-name detection, exact declared method count and full-signature/overload protection. No production defect or Final Design Lock change was found. Final Independent Static Re-review passed. Six ignored bin/*.class differences are GENERATED_ARTIFACT_ONLY_NON_BLOCKING, not authored changes or build/test evidence.

## Verification evidence and limits

The focused suite covered ElevatorTravelLimitsTest, ElevatorSubsystemTest, ElevatorArchitectureBoundaryTest, ElevatorIONoopTest, ElevatorObservationTest, HomeElevatorCommandTest, ElevatorTelemetryFacadeTest, and RobotContainerElevatorCompositionTest. User evidence is PASS_M00_L13_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 42s; 4 actionable tasks, 4 executed. No aggregate test count is claimed.

Clean regression evidence is PASS_M00_L13_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 25s; 5 actionable tasks, 5 executed; GRADLE_EXIT_CODE=0. An earlier pasted output lacked the Gradle verdict, so it did not establish PASS. A PowerShell evidence-capture attempt surfaced NativeCommandError when WPILib stderr warning said “Joystick Button 6 on port 0 not available, check if controller is plugged in”. This was an evidence-capture issue, not a Java/test failure. The final cmd.exe capture preserved stdout, stderr, and exit code.

Simulation uses the unchanged truthful Noop composition and no configured travel envelope. Checkpoint 1, PASS_M00_L13_SIMULATION_DISABLED_BASELINE: Available=false, Connected=false, PositionValid=false, PositionReferenced=false, PositionMeters=0.0, PositionErrorMeters=0.0, RequestedState=STOPPED, TargetPositionMeters=0.0, Robot Enabled=No, DS Attached=Yes. Thus 0.0 m while unreferenced is not proof of physical home. Checkpoint 2, PASS_M00_L13_SIMULATION_TELEOP_ENABLED_NO_UNCOMMANDED_ELEVATOR_ACTION: Robot Enabled=Yes and DS Attached=Yes; the same unavailable/unconnected/invalid/unreferenced Noop values, STOPPED, target 0.0; no uncommanded request, homing, or travel-limit action. Checkpoint 3, PASS_M00_L13_SIMULATION_RETURN_TO_DISABLED: Robot Enabled=No and DS Attached=Yes; the same Noop values, STOPPED, target 0.0. Overall gate: PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION for Disabled -> Teleop Enabled -> Disabled.

Evidence classification: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Simulation verifies bounded startup, truthful Noop telemetry, safe enable/disable, and no uncommanded action. It does not verify UI rejection through a configured envelope, physical travel bounds/overtravel, limit switches, vendor soft limits, real motor behavior, or homing. Real minimum/maximum, stroke, switches, hard stop, spool, ratio, motor/controller, CAN ID, vendor limits, homing parameters, current threshold, and calibration remain UNKNOWN / DEFERRED. Software test values are not robot dimensions.

## Lifecycle and roadmap boundary

M00_L13 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED / NOT YET PUBLICATION-VERIFIED. Active Lesson Count is 0 and Current Active M00 Lesson is NONE. M00_L14 Shoot Coordination is INACTIVE / NOT CREATED. M00_L15 Intake-to-Feeder Coordination and M00_L16 Mechanism Autonomous Event Integration remain future scope; no mechanism NamedCommands or PathPlanner events are introduced. Independent Freeze Review is PENDING. Primary frozen snapshot publication, metadata publication, User push, and Final Publication Verification remain pending / User-owned; no M00_L13 publication SHA is established.

## Historical controlled activation snapshot — 2026-09-23

- Predecessor M00_L12: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Predecessor primary SHA: 5cc4c2c1bc6b4108f2c710c3ca0b0cdbdc26ba49
- Predecessor metadata SHA: c1e90fad04469e5162b5c1814566dd534c6a0a6c
- Predecessor gate: PASS_M00_L12_FINAL_PUBLICATION_VERIFICATION
- Preparation: PASS_M00_L13_UNTOUCHED_COPY_BASELINE_BUILD; BUILD SUCCESSFUL in 35s; 6 tasks, 6 executed (User-supplied)
- Architecture / Inheritance Audit: PASS_M00_L13_ARCHITECTURE_INHERITANCE_AUDIT
- Final Design Lock: PASS_M00_L13_FINAL_DESIGN_LOCK
- Controlled Activation: COMPLETE / CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW
- Status: IN_PROGRESS; ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
- Active Lesson Count: 1; Current Active M00 Lesson: M00_L13 — Elevator Travel-Limit Safety
- Lifecycle: NOT COMPLETE / NOT FROZEN / NOT PUBLISHED
- Implementation Authorization: NOT YET AUTHORIZED; Implementation NOT STARTED
- M00_L13 tests NOT RUN; Simulation NOT VERIFIED; real hardware DEFERRED
- Next gate: Independent Activation Review

## Locked one-new-concept boundary

> A vendor-neutral software operational travel envelope for Elevator closed-loop position requests, expressed in the logical position coordinate frame and enforced before an unsafe target can reach ElevatorIO.

This is request-admission travel-limit safety. It rejects an out-of-range requested target before IO. It does not claim physical hard-limit, continuous overtravel, overshoot, or controller protection; it adds no limit switches, homing redesign, commissioning, or mechanism coordination.

## Logical coordinate and hardware boundary

Use the inherited logical Elevator coordinate in meters. Zero does not prove physical home; finite negative positions remain valid. The envelope neither redefines zero nor establishes calibration or physical stroke. No real minimum/maximum is established and no real travel values belong in Constants or RobotContainer. Switches, hard stops, stroke, spool, ratio, controller, CAN ID, vendor limits, homing parameters, current threshold, and calibration remain UNKNOWN / DEFERRED.

### Historical activation design snapshot — planned request policy

PLANNED / NOT YET IMPLEMENTED. The planned immutable, vendor-neutral, hardware-agnostic value object uses logical-meter software configuration:

```java
public record ElevatorTravelLimits(
    double minPositionMeters,
    double maxPositionMeters
)
```

Both values must be finite and minPositionMeters < maxPositionMeters; invalid construction throws IllegalArgumentException. Finite negative bounds, including bounds crossing zero, are valid. These values establish no physical robot dimensions. The inclusive interval accepts endpoints; it has no epsilon, clamping, or target rewrite.

ElevatorSubsystem(ElevatorIO io) means no envelope is configured. A planned bounded constructor requires non-null limits. For requestPositionMeters, check in order: (1) finite target, else IllegalArgumentException; (2) normalized positionValid && positionReferenced, else IllegalStateException; (3) configured limits, else IllegalStateException; (4) target inside inclusive range, else IllegalArgumentException. Rejection changes no state/target, preserves the same Observation instance, and calls no position, stop, or homing IO method. Accepted requests update intent/target, rebuild Observation, then call position IO once. Accepted IO exceptions propagate without retry or rollback. No periodic output/reissue; rejected requests do not cancel prior accepted motion.

A valid/referenced current position outside the envelope does not reject an in-range target; recovery remains caller-requested. Homing needs no operational limits; stop is always available; periodic remains output-free.

### Historical activation design snapshot — inherited contracts and planned change boundary

ElevatorIO remains four methods; ElevatorIOInputs five fields; ElevatorRequestedState three values; ElevatorObservation and telemetry eight values. Constants.java, RobotContainer.java, HomeElevatorCommand, command architecture, and concrete adapters remain unchanged. Composition stays new ElevatorSubsystem(new ElevatorIONoop()). ElevatorSubsystem is the sole planned enforcement owner.

Planned production delta, NOT AUTHORIZED: add ElevatorTravelLimits.java and modify ElevatorSubsystem.java only. Planned tests: ElevatorTravelLimitsTest.java; modify ElevatorSubsystemTest.java and, only as needed, ElevatorArchitectureBoundaryTest.java. Preserve inherited assertions.

Required tests include value validity; inclusive endpoints; in/out-of-range; negative targets; unconfigured fail-closed and validation order; one accepted IO request and accepted IO exception behavior. Required outside-current test accepts an in-range target with valid/referenced current position outside, proves state/target update, Observation rebuild, one position call, no stop/homing. Required rejection identity tests capture Observation and prove assertSame after below-min, above-max, and unconfigured rejections; check exception, unchanged state/target and zero position/stop/homing calls. Accepted tests prove Observation rebuild. Also preserve homing without limits, stop with/without limits, output-free periodic and no fabricated reference.

### Historical activation design snapshot — verification and roadmap boundary

Accepted baseline is preparation evidence only. Future bounded Noop Simulation may show safe startup, truthful Noop, no automatic output/homing, safe enable/disable, and truthful inherited telemetry; it cannot prove physical bounds, switches, soft limits, or stopping. Maximum planned evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; activation claims none achieved.

M00_L14 Shoot Coordination remains INACTIVE / NOT CREATED. M00_L15 Intake-to-Feeder Coordination and M00_L16 Mechanism Autonomous Event Integration remain future scope.

## Historical copied M00_L12 activation snapshot

The inherited current README block follows verbatim as historical provenance, not current M00_L13 authority.


# M00_L12 — Elevator Homing

## Current frozen lesson record — 2026-09-23

- **Previous lesson:** `M00_L11 - Elevator Closed-Loop Position`
- **Previous lesson state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Preparation:** `PASS_M00_L12_UNTOUCHED_COPY_BASELINE_BUILD`
- **Architecture / Inheritance Audit:** `PASS_M00_L12_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L12_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `COMPLETE`
- **Status:** `COMPLETE`
- **Lifecycle:** `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`
- **Active lesson count:** `0`
- **Current active M00 lesson:** `NONE`
- **Implementation Authorization:** `PASS_M00_L12_IMPLEMENTATION_AUTHORIZATION`
- **Implementation:** `COMPLETE / IMPLEMENTATION_COMPLETE_READY_FOR_INDEPENDENT_STATIC_REVIEW`
- **Final Independent Static Review:** `PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`
- **Focused Tests:** `PASS_M00_L12_USER_FOCUSED_TESTS / FRESH --rerun-tasks / BUILD SUCCESSFUL in 35s / 4 ACTIONABLE TASKS EXECUTED`
- **Clean Regression:** `PASS_M00_L12_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 24s / 5 ACTIONABLE TASKS EXECUTED`
- **Simulation:** `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION / THREE CHECKPOINTS / SOFTWARE-NOOP EVIDENCE`
- **Evidence:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Independent Closure Review:** `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`
- **Freeze Reconciliation:** `COMPLETE / FREEZE_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_FREEZE_REVIEW`
- **Independent Freeze Review:** `PENDING / NEXT GATE`
- **Publication:** `NOT PUBLISHED / PENDING / USER-OWNED`
- **Publication SHA:** `NONE / NOT YET ESTABLISHED`
- **Real hardware:** `DEFERRED`
- **M00_L13:** `INACTIVE / NOT CREATED`

The accepted implementation delta versus frozen M00_L11 is production `109 / 105 / 4 / 0 / 1`, tests `102 / 98 / 4 / 0 / 1`, and deploy/config/support `24 / 24 / 0 / 0 / 0` (compared / identical / changed / missing / added). Implementation changes only the locked Elevator homing concept. The exact changed and added files are recorded in LESSON_STATUS.md and the transition guide.

The accepted closure review found no remaining legitimate closure findings. Freeze reconciliation changed documentation and lifecycle records only. Publication has not occurred; no primary or metadata publication SHA, push, or remote verification is claimed.

## Locked one-new-concept boundary

> A bounded, scheduler-managed Elevator homing lifecycle that requests vendor-neutral reference acquisition and recognizes success only from the normalized IO-reported `positionReferenced` semantic.

`positionReferenced` is the sole reference authority. Assigning `positionMeters = 0.0` never establishes physical home, sensor activation, calibration, or reference acquisition. `ElevatorIOInputs` remains the exact five-field contract; `ElevatorIO` exposes `updateInputs(...)`, `requestPositionMeters(...)`, `requestHoming()`, and `stop()`; `ElevatorRequestedState` is exactly `STOPPED`, `POSITION_REQUESTED`, and `HOMING`; and `ElevatorObservation` remains exactly eight components. Normal position requests retain the valid-and-referenced guard. M00_L13 owns general travel-limit safety.

The accepted User Simulation checkpoints were Disabled baseline (`FMSControlData = 32`), Teleop enabled without uncommanded homing (`FMSControlData = 33`), and return to Disabled (`FMSControlData = 32`). Elevator values remained the truthful unavailable Noop state at all checkpoints. This verifies bounded software/runtime and Noop composition only; no physical Elevator homing was simulated or proven. No hardware sensor, direction, speed, vendor control mode, physical timeout, real Elevator adapter, or `ElevatorIOSim` is defined. Constants and RobotContainer remain unchanged.

## Implemented software contract

`ElevatorIOInputs` has exactly `available`, `connected`, `positionValid`, `positionReferenced`, and `positionMeters`. `ElevatorIO` exposes exactly `updateInputs(ElevatorIOInputs)`, `requestPositionMeters(double)`, `requestHoming()`, and `stop()`. `ElevatorIONoop` reports all availability/validity/reference flags false and `positionMeters = 0.0`; its request and stop methods are no-ops and do not fabricate movement or reference.

`ElevatorRequestedState` contains exactly `STOPPED`, `POSITION_REQUESTED`, and `HOMING`. `ElevatorObservation` remains an immutable eight-component record: the five input meanings plus `requestedState`, `targetPositionMeters`, and `positionErrorMeters`. During `HOMING`, target and error are both `0.0`. Signed position error is active only for `POSITION_REQUESTED` with valid and referenced position.

`ElevatorSubsystem.requestHoming()` requires normalized `available && connected`, but does not require valid or already referenced position. A rejected call throws `IllegalStateException` without changing intent, target, or the current Observation and without calling IO. An accepted call records `HOMING`, sets target `0.0`, rebuilds the Observation, then calls `io.requestHoming()` once. An IO exception propagates without retry or rollback; recorded intent remains and no reference or position is fabricated. `periodic()` updates inputs once, normalizes, and rebuilds without reissuing outputs. Position requests still require valid and referenced measurements. `stop()` records `STOPPED` and target `0.0`, rebuilds, then calls `io.stop()` once.

`HomeElevatorCommand(ElevatorSubsystem elevator, double timeoutSeconds)` requires the subsystem and rejects non-finite or non-positive timeouts. The deterministic clock seam is package-private. Initialize finishes safely without a homing request when already referenced or unavailable/disconnected; otherwise it requests homing once and starts timeout tracking. `execute()` issues no Elevator output. It finishes on an existing or newly observed `positionReferenced`, inability to start, or timeout; position zero alone never finishes it. Each `end(...)` call invokes `elevator.stop()` once, and reinitialization resets command-local timing and lifecycle state.

RobotContainer remains unchanged and composes `new ElevatorSubsystem(new ElevatorIONoop())`. No homing binding, default homing command, startup homing, real Elevator adapter, or autonomous/NamedCommands/PathPlanner homing integration exists. Constants remain unchanged.

The architecture test preserves the approved HOMING/request names, detects compound home/homing identifiers in the protected semantic layers, guards `com.ctre.` and `com.revrobotics.` imports and fully qualified references outside concrete ElevatorIO implementations, and checks command, composition, and exact contract boundaries. These are prohibited-dependency guards; they do not claim production violations occurred.

## Current records

- [M00_L11 to M00_L12 transition guide](docs/M00_L11_to_M00_L12_Step_by_Step.md)
- [Lesson status](LESSON_STATUS.md)
- [Lesson plan](LESSON_PLAN.md)
- [Lesson checklist](LESSON_CHECKLIST.md)

## Historical inherited earlier lesson records

The inherited documentation snapshot follows below for provenance only. Its old lesson identity and lifecycle are historical and are not the current M00_L12 state above.
# M00_L10 — Elevator Foundation and Position-Reference Semantics

M00_L10 is `COMPLETE / FROZEN / READ-ONLY`.
It introduces exactly one concept: a vendor-neutral Elevator position
observation and reference contract.

## Current lifecycle

- **Previous lesson:** `M00_L09 - Flywheel Ready-at-Speed`
- **Previous lesson state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Preparation / inherited baseline:** `PASS_M00_L10_PREPARATION_BASELINE`
- **Architecture / Inheritance Audit:** `PASS_M00_L10_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L10_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `PASS_M00_L10_CONTROLLED_ACTIVATION`
- **Status:** `COMPLETE`
- **Active state:** `FROZEN / READ-ONLY`
- **Freeze state:** `COMPLETE`
- **Active lesson count:** `0`
- **Current active M00 lesson:** `NONE`
- **Implementation:** `COMPLETE / PASS_M00_L10_IMPLEMENTATION_REPORT_ACCEPTED`
- **Independent Static Review:** `PASS / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`
- **Focused Tests:** `PASS_M00_L10_USER_FOCUSED_TESTS / USER VERIFIED`
- **Clean Full Regression:** `PASS_M00_L10_CLEAN_FULL_REGRESSION / BUILD SUCCESSFUL in 27s / 7 ACTIONABLE TASKS (7 EXECUTED)`
- **Verification:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Bounded Simulation:** `PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Real hardware:** `DEFERRED`
- **Independent Activation Review:** `PASS_M00_L10_FINAL_INDEPENDENT_ACTIVATION_REREVIEW`
- **Independent Closure Review:** `PASS / PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW`
- **Independent Freeze Review:** `PENDING`
- **Publication:** `PENDING / NOT YET PUBLISHED`
- **M00_L11:** `INACTIVE / NOT CREATED`

Controlled Activation changed lifecycle and documentation identity only. Later
implementation and User-owned verification are recorded in the append-only
chronology below; Independent Freeze Review and publication remain separate
pending gates.

## Historical inherited M00_L09 Ready-at-Speed design boundary

The one new concept is vendor-neutral instantaneous Flywheel Ready-at-Speed
classification: given current requested velocity intent and the latest valid
Flywheel velocity measurement, determine whether the mechanism is within the
approved velocity tolerance.

`FlywheelSubsystem` owns the semantic. No separate evaluator class is created;
one private deterministic side-effect-free helper combines the requested intent,
cached `FlywheelIOInputs`, and the locked tolerance. Ready-at-Speed is not owned
by IO, Noop, telemetry, RobotTelemetry, RobotContainer, or a command.

The exact software-policy tolerance is
`Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm`, a `double` value of
`50.0` Flywheel mechanism RPM. It is provisional, not hardware-tuned, and not
real-robot validated; it is not a target RPM, maximum RPM, percentage tolerance,
or physical performance threshold.

`readyAtSpeed` is true only when requested state is `VELOCITY_REQUESTED`, the
requested target is finite and positive, availability/connection/velocity
validity are true, the measured velocity is finite, and
`Math.abs(measured - target) <= tolerance`. The comparison is symmetric and
inclusive. STOPPED, zero, negative-zero, invalid, unavailable, disconnected,
non-finite, and non-positive-target cases are always false.

`FlywheelObservation` adds exactly one immutable component, `readyAtSpeed`,
after `available`, `connected`, `velocityValid`, `velocityRpm`, and
`requestedState`. Its constructor rejects `readyAtSpeed=true` unless the
availability, connection, validity, and requested-state invariants hold.
There is no target, error, tolerance, dwell, debounce, hysteresis, timestamp,
or readiness-history component.

`FlywheelTelemetryFacade` adds exactly `ReadyAtSpeed`; RobotTelemetry and
RobotContainer remain unchanged. No command, binding, automatic action,
Feeder/Flywheel coordination, shooting, or autonomous integration is included.

## Architect Simulation clarification

Focused unit tests and test doubles may verify request semantics, validation,
ordering, and safe stop. They are not WPILib runtime Simulation evidence.

While runtime remains Noop-only, bounded WPILib Simulation may later verify
Noop composition, deterministic unavailable/disconnected/invalid measurements,
`velocityRpm = 0.0` with `velocityValid = false`, STOPPED idle behavior, no
automatic request on Teleop enable, and Disabled → Teleop → Disabled
persistence. It may not claim requestVelocity runtime exercise, physical
regulation, convergence, gain correctness, sensor fidelity, RPM accuracy, CAN
behavior, or physical stop behavior.

## Protected future scope

- M00_L10 through M00_L16 remain outside this lesson.
- No Flywheel command, controller binding, default command, shooting, feeder coordination, automatic firing, NamedCommands, event markers, or autonomous mechanism integration is included.
- Physical hardware, tuning, calibration, and real-robot verification remain deferred.

## Lesson records

- [M00_L09 to M00_L10 transition guide](docs/M00_L09_to_M00_L10_Step_by_Step.md)
- [Lesson status](LESSON_STATUS.md)
- [Lesson plan](LESSON_PLAN.md)
- [Lesson checklist](LESSON_CHECKLIST.md)

## Documentation repair clarification

The future `FlywheelIO` contract is exactly:

- `FlywheelIOInputs`: `boolean available`, `boolean connected`,
  `boolean velocityValid`, `double velocityRpm`;
- `void updateInputs(FlywheelIOInputs inputs)`;
- `void requestVelocity(double targetRpm)`; and
- `void stop()`.

`requestSpin()` is `REMOVED / SUPERSEDED`. No vendor type, vendor control
object, gain parameter, or hardware-configuration parameter is permitted.

For a positive valid request, future ordering is validate, record target, set
`VELOCITY_REQUESTED`, replace immutable Observation, and forward exactly one IO
request. Forwarding exceptions leave target/state/Observation recorded,
propagate, do not roll back, and are not retried by `periodic()`. Exactly
`0.0` records zero/`STOPPED`, replaces Observation, and forwards one stop.
Invalid NaN, positive infinity, negative infinity, and negative finite values
record zero/`STOPPED`, replace Observation, attempt stop, and throw
`IllegalArgumentException`; stop failure is suppressed and no invalid target
is forwarded. Explicit stop is unconditional, preserves zero/STOPPED/
Observation if IO throws, and never automatically restarts or reissues output.

`FlywheelIONoop.updateInputs()` sets exactly `available=false`,
`connected=false`, `velocityValid=false`, and `velocityRpm=0.0`. Its request and
stop are safe deterministic no-ops without convergence or physical modeling;
invalid zero RPM is not measured physical zero RPM.

`RobotContainer` remains unchanged and composes exactly
`new FlywheelSubsystem(new FlywheelIONoop())`. It adds no Flywheel command,
binding, default command, direct request/stop, autonomous registration,
NamedCommands, event markers, Feeder/Flywheel coordination, physical hardware
selection, or Flywheel-specific `RobotBase.isReal()` branch.

Future test reconciliation is limited to the six exact existing paths named
in the lesson records; no new or unrelated test file is authorized.

## Post-verification reconciliation — 2026-09-20

Implementation is complete within the locked four-file production boundary.
The one new concept is vendor-neutral Flywheel closed-loop velocity control
through `void requestVelocity(double targetRpm)`, with
`requestSpin()` `REMOVED / SUPERSEDED`, exact states `STOPPED` and
`VELOCITY_REQUESTED`, measurement-only Observation, and explicit safe stop.
`FlywheelIOInputs` remains exactly `available`, `connected`, `velocityValid`,
and `velocityRpm`; the exact IO methods are `updateInputs(...)`,
`requestVelocity(...)`, and `stop()`. Valid targets are finite and
nonnegative; positive requests forward once, zero (`+0.0` or `-0.0`) is safe
stop, and NaN/infinite/negative requests fail closed to zero/`STOPPED`, update
Observation, attempt stop, throw `IllegalArgumentException`, and suppress a
stop failure. Explicit stop records zero/`STOPPED` before unconditional IO
stop. `periodic()` only updates inputs and Observation. `FlywheelIONoop`
reports false/false/false/0.0 and is a deterministic no-op; CAN 50–54 and real
hardware remain deferred.

The accepted final comparison against M00_L07 is production
`103 compared / 99 byte-identical / 4 changed / 0 missing / 0 added`, exactly
the four authorized Flywheel production files, and tests `96 compared / 90
byte-identical / 6 changed / 0 missing / 0 added`, exactly the six authorized
Flywheel-focused tests. No new file was added; Constants, RobotContainer,
telemetry, Gradle, vendordeps, deploy, and M00_L07 are unchanged.

The initial static review `HOLD` (stale stop target, weak Noop assertions,
missing negative-zero coverage, brittle comment stripping) was repaired in a
bounded change. Final Independent Static Re-review is `PASS` under
`PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.

Accepted User evidence: focused tests `BUILD SUCCESSFUL in 16s`; `4
actionable tasks: 3 executed, 1 up-to-date`; `FOCUSED TESTS: PASS` under
`PASS_M00_L08_USER_FOCUSED_TESTS`. Clean regression `BUILD SUCCESSFUL in
26s`; `7 actionable tasks: 7 executed`; `CLEAN REGRESSION: PASS` under
`PASS_M00_L08_CLEAN_FULL_REGRESSION`.

Accepted bounded Simulation (`PASS_M00_L08_BOUNDED_SIMULATION`) records:

1. Disabled — Available=false, Connected=false, RequestedState=STOPPED,
   VelocityRpm=0.0, VelocityValid=false
   (`PASS_M00_L08_SIMULATION_CHECKPOINT_1_DISABLED`).
2. Teleoperated enabled with no driver action — Robot Enabled=Yes and the
   same values (`PASS_M00_L08_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`).
3. Return Disabled — FMS Robot Enabled=No and the same values
   (`PASS_M00_L08_SIMULATION_CHECKPOINT_3_DISABLED`).

`VelocityRpm=0.0` with `VelocityValid=false` is canonical invalid-Noop state,
not measured physical zero RPM. Simulation verifies only Noop composition,
telemetry/state presence, deterministic invalid measurement, STOPPED idle,
no automatic Teleop request, and mode persistence. It does not verify
physical regulation, convergence, gains, feedforward, sensor fidelity, RPM
accuracy, direction, CAN, physical stop, or **requestVelocity runtime
exercise was NOT claimed**; request semantics came from focused/unit
tests. Evidence classification is exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, `REAL HARDWARE DEFERRED`.
The exact documentation statement is: `requestVelocity runtime exercise was NOT claimed`.

Lifecycle remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`,
active lesson count `1`, current active lesson `M00_L08`; implementation,
static rereview, focused tests, clean regression, and bounded Simulation are
complete/pass. Independent Closure Review is `PENDING`; Freeze and Publication
are `NOT AUTHORIZED`. M00_L09 Ready-at-Speed remains `INACTIVE / NOT CREATED`
and all later command, readiness, shooting, coordination, and autonomous
mechanism scope remains protected.

## Controlled freeze transition — 2026-09-20

The accepted Independent Closure Review gate
`PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW` returned
`READY_FOR_FREEZE_AUTHORIZATION`, and the Architect authorized freeze.
M00_L08 is now `COMPLETE / FROZEN / READ-ONLY`; active lesson count is `0`,
and the current active M00 lesson is `NONE`.

Publication remains `PENDING / NOT YET PUBLISHED`, and Final Publication
Verification is `NOT YET PERFORMED`. M00_L09 remains `INACTIVE / NOT CREATED`.
No source, test, configuration, dependency, deployment, or verification
evidence changed during this transition.

The exact Flywheel contract, one-concept boundary, safe-stop and validation
semantics, immutable Observation, output-free `periodic()`, deterministic
Noop runtime, read-only telemetry, and deferred real hardware remain
unchanged. Evidence remains `THEORY VERIFIED`, `SIMULATION VERIFIED`, and
`REAL HARDWARE DEFERRED`; Simulation did not claim runtime `requestVelocity`
exercise or physical Flywheel behavior.

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
M00_L09: INACTIVE / NOT CREATED
```

## M00_L09 controlled activation — 2026-09-21

The historical M00_L08 material above is retained as inherited snapshot
history. The current lesson state is authoritative here: M00_L09 is
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, the sole active M00
lesson, with implementation `NOT STARTED` and Independent Activation Review
`PENDING`. The accepted gates are
`PASS_M00_L09_PREPARATION_BASELINE`,
`PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT`, and
`PASS_M00_L09_FINAL_DESIGN_LOCK`; Controlled Activation is
`COMPLETE / CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.

The exact one-concept design, 50.0 RPM provisional software-policy tolerance,
Observation invariant, Noop false behavior, read-only telemetry, unchanged IO
and RobotContainer boundaries, and prohibition on commands, coordination,
automatic action, and M00_L10–M00_L16 changes are recorded in the current
sections above. No implementation, test, build, Simulation, closure, freeze,
publication, or Git evidence is claimed. M00_L08 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; M00_L10 is
`INACTIVE / NOT CREATED`.

## Independent activation documentation repair reconciliation — 2026-09-21

### Predecessor and gate provenance

The canonical external M00_L08 state is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. Primary publication:
`5daecd970ff95fb906d6de9d5bc22a5cb094877d`; metadata publication:
`a76dc33c2b485b4988e7058cbfed0fa3362cc560`; metadata parent:
`5daecd970ff95fb906d6de9d5bc22a5cb094877d`;
final remote-aligned published HEAD: the metadata SHA; final verdict:
`PUBLICATION_VERIFIED`; third publication commit: not required. The passing
two-commit Historical Snapshot Model permits frozen M00_L08 local records to
retain historical pending wording. That wording is not a contradiction and
the frozen lesson-local files remain unmodified.

The Architect acceptance gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`. The
engineer task verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
Both are recorded distinctly. The current independent review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW` and must be re-reviewed.

### Locked semantic clarifications

The exact Final Design Lock formula remains the only truth rule: positive,
finite requested velocity intent; available, connected, and valid input; finite
measurement; and `Math.abs(measured - target) <= tolerance`, where tolerance is
`Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm = 50.0` mechanism RPM,
an explicitly provisional software-policy acceptance tolerance. A measured
velocity of `0.0` RPM does not, by itself, establish Ready-at-Speed. This is a
semantic clarification only; it does not add a new formula condition, a
minimum target, or a measured-velocity positivity requirement.

Exact clarification: "A measured velocity of 0.0 RPM does not, by itself, establish Ready-at-Speed."

Exact cached-state rule: "Ready-at-Speed classifies cached requested intent plus cached measurement." Exact delivery limitation: "Ready-at-Speed does NOT certify successful delivery of the newest IO request."

When a positive `requestVelocity(target)` updates intent and Observation and
the subsequent IO forwarding throws, the requested target remains the new
target, requested state remains `VELOCITY_REQUESTED`, and the already-derived
Observation remains. The original IO exception propagates unchanged; no
rollback, retry, or readiness-specific exception masking occurs. Ready-at-Speed
classifies cached requested intent plus cached measurement. Ready-at-Speed does
NOT certify successful delivery of the newest IO request.

The final Flywheel telemetry fields are exactly `Available`, `Connected`,
`VelocityValid`, `VelocityRpm`, `RequestedState`, and `ReadyAtSpeed`. Only
`ReadyAtSpeed` is new in M00_L09, sourced from
`FlywheelObservation.readyAtSpeed()`. `TargetRpm`, `VelocityErrorRpm`,
`ToleranceRpm`, `Dwell`, `Debounce`, and `ReadinessDuration` are excluded.
`RobotTelemetry.java` remains unchanged and telemetry remains read-only.

`readyAtSpeed=true` must never automatically feed a game piece, run Feeder,
fire, stage, stop Flywheel, schedule a command, advance another subsystem, or
trigger autonomous behavior. M00_L09 introduces no Flywheel command ownership,
default command, controller binding, `ShootCommand`, Feeder/Flywheel
coordination, NamedCommands, event markers, or autonomous mechanism
integration; it only computes and exposes readiness.

### Locked boundary truth table

For positive target `T` and tolerance `E`, exact target, measurements below or
above target by less than `E`, and measurements exactly `T - E` or `T + E` are
true. Measurements `< T - E` or `> T + E` are false. Unavailable,
disconnected, invalid, or non-finite measurements are false (existing
Observation validation remains authoritative). `STOPPED` with a stale matching
measurement is false. Targets `+0.0` and `-0.0` are false. If the target
changes while the cached measurement equals the old target, evaluation uses the
new target. A new valid matching periodic sample is true when all other
conditions hold; an outside-tolerance sample is false; repeated identical
periodic samples are deterministic. A `0.0` RPM measurement alone does not
independently establish readiness; the exact locked formula still governs.

The comparison is exactly `Math.abs(measured - target) <= tolerance`. Focused
tests should use exactly representable values where practical, especially
integer RPM values around the 50.0 RPM boundary. No second epsilon or
“tolerance for the tolerance” is permitted.

### Required later implementation-test matrix

The later implementation must cover, at minimum: the exact new Observation
component; the Observation readiness consistency invariant; exact target;
inside-tolerance lower and upper values; exact inclusive lower and upper
boundaries; just-outside lower and upper values; unavailable, disconnected,
invalid, and non-finite fail-safe cases; STOPPED; `+0.0` and `-0.0` targets;
stale matching STOPPED measurement; target change against the new target;
matching and outside-tolerance periodic samples; repeated periodic
determinism; no output actuation from evaluation; positive-request ordering;
forwarding-exception preservation; zero-request semantics; invalid fail-closed
semantics; invalid stop-failure suppression; explicit stop; FlywheelIONoop
false implication; ReadyAtSpeed telemetry; absence of TargetRpm,
VelocityErrorRpm, and ToleranceRpm telemetry; unchanged FlywheelIO,
FlywheelIONoop, and RobotContainer; no command/binding, automatic action, or
coordination; and M00_L10–M00_L16 protection. This repair adds no tests.

### Evidence and hardware boundary

If all later gates pass, the eventual evidence classification is exactly
`THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.
Current M00_L09 verification remains `PENDING` because implementation, tests,
and Simulation have not occurred. Any later bounded Simulation is limited to
Noop composition, `ReadyAtSpeed=false`, telemetry, STOPPED safety, no automatic
request, no readiness-triggered action, and Disabled → Teleop → Disabled
lifecycle persistence.

The following remain UNKNOWN / DEFERRED: physical motor/controller, vendor, CAN
ID and bus, motor count and topology, sensor, gear ratio, inversion and
direction, current and voltage limits, neutral mode and ramping, physical
target RPM and maximum RPM, hardware-tuned readiness tolerance, PID/PIDF and
feedforward gains, physical convergence behavior, and real-hardware readiness
behavior. CAN 50–54 is a planning reservation only; 50.0 RPM is provisional
software policy, not a verified physical threshold.

### Unchanged design and lifecycle boundaries

Future production modification remains limited to `Constants.java`,
`FlywheelObservation.java`, `FlywheelSubsystem.java`, and
`FlywheelTelemetryFacade.java`; `FlywheelIO.java`, `FlywheelIONoop.java`,
`RobotTelemetry.java`, and `RobotContainer.java` remain unchanged and no
production file is created. In exact path form, production modifications are
limited to `src/main/java/frc/robot/Constants.java`,
`src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`,
`src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`, and
`src/main/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacade.java`;
unchanged production paths are
`src/main/java/frc/robot/io/flywheel/FlywheelIO.java`,
`src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`,
`src/main/java/frc/robot/telemetry/RobotTelemetry.java`, and
`src/main/java/frc/robot/RobotContainer.java`. No production file is created.
Future test modifications are limited exactly to
`src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`,
`src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`,
`src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`, and
`src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`.
`src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java` and
`src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java` remain
unchanged; no test file is created.

M00_L09 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`,
implementation `NOT STARTED`, Active Lesson Count `1`, and Current Active M00
Lesson `M00_L09`. Independent Activation Review is
`HOLD — RE-REVIEW REQUIRED`. M00_L10 remains `INACTIVE / NOT CREATED`, and the
M00_L01–M00_L16 roadmap is unchanged.

## Implementation, verification, and documentation reconciliation — 2026-09-21

The accepted implementation gate is
`PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED`. The implementation adds exactly
one concept: vendor-neutral instantaneous Flywheel Ready-at-Speed
classification. `FlywheelSubsystem` owns the semantic through one private
deterministic side-effect-free helper; `FlywheelObservation` has exactly six
components, and telemetry has exactly six fields with only `ReadyAtSpeed` new.
IO, `FlywheelIONoop`, `RobotTelemetry`, `RobotContainer`, commands,
coordination, autonomous integration, and hardware remain unchanged.

The final production comparison against frozen M00_L08 is `103 compared / 99
byte-identical / 4 changed / 0 missing / 0 added`; the changed paths are
exactly the four authorized production files. The final test comparison is
`96 compared / 92 byte-identical / 4 changed / 0 missing / 0 added`; the changed
paths are exactly the four authorized focused tests. No new production or test
file was added, and deploy/configuration remains unchanged.

The static-review chronology is preserved: initial Independent Static Review
`HOLD`; bounded Static-Review Repair `PASS`; Final Independent Static
Re-review `PASS` with verdict
`STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`.

User-focused evidence is `BUILD SUCCESSFUL in 22s`; four actionable tasks,
three executed and one up-to-date; gate
`PASS_M00_L09_USER_FOCUSED_TESTS`. Clean full regression is `BUILD SUCCESSFUL`
with seven actionable tasks, all seven executed; gate
`PASS_M00_L09_CLEAN_FULL_REGRESSION`.

Bounded Simulation passed the intended Noop/lifecycle scope:

1. Disabled: Available=false, Connected=false, ReadyAtSpeed=false,
   RequestedState=STOPPED, VelocityRpm=0.0, VelocityValid=false;
   `PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`.
2. Teleoperated enabled with no controller action: the same Flywheel values,
   with Robot Enabled=Yes; `PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`.
3. Return to Disabled: the same Flywheel values, with Robot Enabled=No;
   `PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`.

Overall gate: `PASS_M00_L09_BOUNDED_SIMULATION`. The invalid Noop
`VelocityRpm=0.0` value is not a physical zero measurement. Simulation verifies
Noop composition, telemetry presence, fail-safe false readiness, STOPPED idle
behavior, no automatic Teleop request, no readiness-triggered actuation, and
Disabled → Teleop → Disabled persistence. It does not verify ReadyAtSpeed=true,
50-RPM runtime boundaries, physical convergence, sensor fidelity, CAN behavior,
or real-hardware readiness.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`. The physical motor/controller, vendor, CAN 50–54,
sensor, topology, gearing, inversion, limits, gains, feedforward, convergence,
and real-hardware readiness remain unknown/deferred; CAN 50–54 is a planning
reservation only and 50.0 RPM remains provisional software policy.

Documentation reconciliation is complete. M00_L09 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, with Active Lesson
Count `1` and M00_L10 `INACTIVE / NOT CREATED`. Independent Closure Review is
`PENDING`; Freeze and Publication are `NOT AUTHORIZED`. No future closure,
freeze, publication, or M00_L10 event is claimed.

## Final implementation, verification, and documentation reconciliation — 2026-09-22

This current record supersedes earlier M00_L10 pending wording while
preserving that wording as historical chronology. The locked concept remains
exactly one vendor-neutral Elevator position-observation and reference
contract. M00_L10 does not implement position control, homing, reference
establishment, or travel-limit enforcement.

The accepted technical gates are:

- `PASS_M00_L10_IMPLEMENTATION_REPORT_ACCEPTED`
- `PASS_M00_L10_FINAL_INDEPENDENT_STATIC_REREVIEW`
- `PASS_M00_L10_FOCUSED_TEST_FAILURE_FORENSICS`
- `PASS_M00_L10_FOCUSED_TEST_DEFECT_REPAIR`
- `PASS_M00_L10_INDEPENDENT_FOCUSED_TEST_REPAIR_REREVIEW`
- `PASS_M00_L10_USER_FOCUSED_TESTS`
- `PASS_M00_L10_CLEAN_FULL_REGRESSION`
- `PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION`

The initial focused invocation is retained accurately: 23 tests completed,
22 passed, and one failed in
`ElevatorArchitectureBoundaryTest.elevatorProductionUsesNoRequestedMotionOrControlState()`.
Forensics classified the failure as `TEST_IMPLEMENTATION_DEFECT`: the test
rejected the required `edu.wpi.first.wpilibj2.command.SubsystemBase` import by
using a package-wide command substring. Production had no M00_L11, M00_L12, or
M00_L13 violation. The bounded test-only repair changed only
`ElevatorArchitectureBoundaryTest.java`, and the independent repair re-review
passed.

The User reran all six focused M00_L10 test classes successfully under
`PASS_M00_L10_USER_FOCUSED_TESTS`. The User then supplied clean regression
evidence: `BUILD SUCCESSFUL in 27s`, seven actionable tasks, all seven
executed. No production repair was made for the focused-test defect.

Integrity against frozen M00_L09 remains production `103 / 101 / 2 / 0 / 5`,
tests `96 / 96 / 0 / 0 / 6`, and deploy/config/support `24 / 24 / 0 / 0 / 0`
for compared, byte-identical, changed, missing, and added files.

The User also supplied bounded WPILib Simulation evidence under
`PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION`:

1. Disabled: `Available=false`, `Connected=false`, `PositionValid=false`,
   `PositionReferenced=false`, and `PositionMeters=0.0`.
2. Teleoperated/enabled: Robot Enabled=Yes and the same Elevator values.
3. Final Disabled: Robot Enabled=No and the same Elevator values.

Elevator NetworkTables telemetry exists with exactly `Available`, `Connected`,
`PositionValid`, `PositionReferenced`, and `PositionMeters`. No Elevator
`RequestedState` field exists. The Noop `PositionMeters=0.0` value is the
canonical invalid placeholder; it does not establish physical zero, homing,
trusted reference, sensor calibration, or real mechanism position.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
and `REAL HARDWARE DEFERRED`. Elevator hardware, sensors, conversion,
direction, limits, and CAN 50–54 remain unknown or deferred. M00_L11 remains
responsible for position control, M00_L12 for homing/reference establishment,
and M00_L13 for travel-limit enforcement.

M00_L10 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`.
Documentation Reconciliation is complete and the lesson is ready for
Independent Closure Review. Closure, freeze, publication, and M00_L11
activation remain future gates and are not claimed.

### Focused-test execution clarification — 2026-09-21

The User ran one focused Gradle test invocation selecting all six Flywheel-
focused test classes:

1. `frc.robot.FlywheelArchitectureBoundaryTest`
2. `frc.robot.observation.flywheel.FlywheelObservationTest`
3. `frc.robot.subsystems.FlywheelSubsystemTest`
4. `frc.robot.telemetry.flywheel.FlywheelTelemetryFacadeTest`
5. `frc.robot.io.flywheel.FlywheelIONoopTest`
6. `frc.robot.RobotContainerFlywheelCompositionTest`

Result: `BUILD SUCCESSFUL in 22s`; 4 actionable tasks: 3 executed and 1
up-to-date. Gate: `PASS_M00_L09_USER_FOCUSED_TESTS`. The four M00_L09 test
files modified are distinct from the two inherited protected tests, which were
intentionally included as regression checks despite remaining byte-identical
to frozen M00_L08. The four Gradle task counts do not represent test-class
counts, and no six-task claim is made.
Independent Closure Review remains `HOLD — RE-REVIEW REQUIRED`; Freeze and
Publication remain `NOT AUTHORIZED`.

## Controlled freeze transition — 2026-09-21

The final independent closure re-review passed with
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`, and Architect freeze
authorization was accepted. M00_L09 is now `COMPLETE / FROZEN / READ-ONLY`.
Independent Freeze Review is `PENDING`; Publication is `PENDING / NOT YET
PUBLISHED`. Active Lesson Count is `0`, Current Active M00 Lesson is `NONE`,
and M00_L10 remains `INACTIVE / NOT CREATED`.

The technical contract, architecture, production/test/support integrity, and
evidence classification remain unchanged. No publication SHA or Git event is
claimed. The chronology stops before Independent Freeze Review, publication,
and successor activation.

## M00_L10 controlled activation snapshot — 2026-09-21

All M00_L09 implementation, verification, freeze, and publication paragraphs
above are inherited historical snapshot material. They are not the current
M00_L10 lifecycle state.

M00_L10 is the sole active lesson: `IN_PROGRESS / ACTIVE / EDITABLE WITHIN
FINAL DESIGN LOCK`. The accepted predecessor is M00_L09
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, with primary SHA
`3c822a1e3956850c9d0ba9954c5b163d83b801b9`, metadata SHA
`249100db23262430ce2557eaa5e67d70b7b0a79c`, and final verdict
`PUBLICATION_VERIFIED`.

The one locked concept is the vendor-neutral Elevator position-observation and
reference contract. The software coordinate frame uses `positionMeters`, with
`0.0 m` as the logical Elevator reference origin, positive values increasing
logical extension/upward travel, and negative finite values permitted. A zero
value does not imply physical zero, homed, referenced, trusted, or valid.

The semantic observation inputs are exactly `available`, `connected`,
`positionValid`, `positionReferenced`, and `positionMeters`. Normalization is:

```text
available = inputs.available
connected = available && inputs.connected
finitePosition = Double.isFinite(inputs.positionMeters)
positionValid = connected && inputs.positionValid && finitePosition
positionReferenced = positionValid && inputs.positionReferenced
positionMeters = finitePosition ? inputs.positionMeters : 0.0
```

No position control, homing, zeroing, travel-limit enforcement, target/error,
command, binding, autonomous action, vendor API, IOSim, or real hardware is in
scope. Runtime composition is `new ElevatorSubsystem(new ElevatorIONoop())`.
At that activation point, implementation, tests, build, Simulation, closure,
freeze, and publication remained pending or unauthorized. The later final
reconciliation below supersedes that pending state.

## Bounded activation documentation repair — 2026-09-21

This section is historical activation-repair evidence. Its pending review
wording is superseded by the final reconciliation below.

The five semantic members are explicitly defined as follows:

- `available`: the Elevator IO source is currently capable of providing
  Elevator mechanism information.
- `connected`: the relevant Elevator device/data path is currently connected.
- `positionValid`: `positionMeters` is currently a finite mechanism-position
  measurement valid under the declared meters/sign software contract.
- `positionReferenced`: the measurement origin is currently trusted against
  the logical Elevator reference origin.
- `positionMeters`: the vendor-neutral linear Elevator mechanism position in
  meters in the declared logical software coordinate frame.

`positionValid == true` does not require `positionReferenced == true`; a finite
valid measurement may remain unreferenced. `positionReferenced == true`
requires `available == true`, `connected == true`, `positionValid == true`,
and `Double.isFinite(positionMeters)`. M00_L10 does not establish reference;
M00_L12 owns reference establishment through homing.

The exact ElevatorIO methods remain only
`void updateInputs(ElevatorIOInputs inputs)` and `void stop()`. M00_L10
prohibits `requestPosition`, `setPosition`, `setVoltage`, `setPercent`,
`setDutyCycle`, PID, PIDF, feedforward, Motion Magic, homing, zeroing, limit
methods, open-loop motor commands, closed-loop position requests, and target
position APIs.

`ElevatorIONoop.updateInputs()` writes exactly
`false, false, false, false, 0.0`; `positionMeters = 0.0` is the
**CANONICAL INVALID PLACEHOLDER**. It does not prove physical Elevator zero,
valid measured zero, homing, a trusted reference, a real sensor measuring zero,
or physical location at the logical origin. `stop()` is a safe no-op; no
ElevatorIOSim, ElevatorIOCTRE, ElevatorIOREV, or real hardware adapter exists.

The future test matrix now explicitly includes a finite valid zero case:
`available=true`, `connected=true`, `positionValid=true`,
`positionReferenced=false`, `positionMeters=0.0`, preserving valid zero while
the logical origin remains untrusted. This is distinct from the Noop invalid
placeholder state.

The repaired chronology and future-gate protections are recorded in
`docs/M00_L09_to_M00_L10_Step_by_Step.md`. Documentation Reconciliation remains
pending until Independent Activation Review re-review accepts this repair.

## Controlled freeze transition — 2026-09-22

The accepted closure gate is `PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW` with
verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_AUTHORIZATION`. Architect
authorization is recorded as `PASS_M00_L10_FREEZE_AUTHORIZATION`.

M00_L10 is now `COMPLETE / FROZEN / READ-ONLY`; Active Lesson Count is `0` and
Current Active M00 Lesson is `NONE`. The evidence classification remains
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Independent
Freeze Review is `PENDING`, Publication is `PENDING / NOT YET PUBLISHED`, and
M00_L11 remains `INACTIVE / NOT CREATED`.

No production, test, deploy/configuration, support, root governance, Git, or
publication change was made by this freeze transition. No publication SHA or
future Independent Freeze Review result is recorded.

## M00_L11 controlled activation — 2026-09-22

The copied M00_L10 material above is historical snapshot chronology. The
current semantic state is:

```text
LESSON: M00_L11 - Elevator Closed-Loop Position
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L11
IMPLEMENTATION: NOT STARTED
INDEPENDENT ACTIVATION REVIEW: PENDING
BUILD: NOT RUN / DOCUMENTATION-ONLY ACTIVATION
SIMULATION: NOT RUN / NO RUNTIME EVIDENCE CLAIMED
CURRENT EVIDENCE: NOT YET ESTABLISHED FOR M00_L11
BUILD / TEST EVIDENCE: NOT YET ESTABLISHED FOR M00_L11
SIMULATION EVIDENCE: NOT YET ESTABLISHED FOR M00_L11
REAL HARDWARE: DEFERRED
M00_L10: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L12: INACTIVE / NOT CREATED
```

Accepted gates are `PASS_M00_L11_PREPARATION_BASELINE`,
`PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT`,
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`, and
`PASS_M00_L11_FINAL_DESIGN_LOCK`.

Inheritance is exact: production `108 / 108 / 0 / 0 / 0`, tests
`102 / 102 / 0 / 0 / 0`, deploy/config/support `24 / 24 / 0 / 0 / 0`, and
lesson-local documentation `98 / 98 / 0 / 0 / 0` for Compared / Byte-identical /
Changed / Missing / Added. Unexpected substantive drift is `NONE`.

M00_L11 introduces exactly one concept: vendor-neutral Elevator closed-loop
position request semantics in the M00_L10 logical position frame. The future IO
surface is `updateInputs(...)`, `requestPositionMeters(double)`, and `stop()`;
the exact requested states are `STOPPED` and `POSITION_REQUESTED`. The future
observation adds `requestedState`, `targetPositionMeters`, and
`positionErrorMeters` to the inherited five members. Requests require a finite
target plus valid and trusted position; finite negative targets remain valid and
no physical range clamp exists. Rejected requests mutate nothing and call no
IO. Valid requests record intent, rebuild the immutable observation, and
forward once. `periodic()` never reissues output. `stop()` records stopped and
target `0.0`, rebuilds, and forwards once.

Noop remains deterministic and output-free. RobotContainer, RobotTelemetry,
Constants, commands, bindings, autonomous integration, real adapters, and
ElevatorIOSim remain unchanged. M00_L12 owns homing/reference establishment;
M00_L13 owns travel-limit safety and target clamping. Telemetry may contain
exactly the eight locked read-only topics, including requested state, target,
and position error. The target evidence classification for eventual closure is
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; it is planned
and is not current achieved evidence.

The complete activation chronology and write boundaries are recorded in
`docs/M00_L10_to_M00_L11_Step_by_Step.md`. Independent Activation Review is
pending; no implementation, verification, closure, freeze, publication, or
Git result is claimed.

## M00_L11 implementation, verification, and documentation reconciliation — 2026-09-22

This is the authoritative current-state record. The earlier activation-only
snapshot above remains historical chronology and is superseded here.

The accepted gates are `PASS_M00_L11_IMPLEMENTATION_REPORT_ACCEPTED`,
`PASS_M00_L11_INDEPENDENT_STATIC_REVIEW`,
`PASS_M00_L11_FOCUSED_TEST_COMPILE_FAILURE_FORENSICS`,
`PASS_M00_L11_FOCUSED_TEST_COMPILE_DEFECT_REPAIR`,
`PASS_M00_L11_INDEPENDENT_FOCUSED_TEST_COMPILE_REPAIR_REREVIEW`,
`PASS_M00_L11_FOCUSED_TEST_OBSERVATION_FAILURE_FORENSICS`,
`PASS_M00_L11_OBSERVATION_EQUALITY_FIXTURE_REPAIR`, and
`PASS_M00_L11_INDEPENDENT_OBSERVATION_FIXTURE_REPAIR_REREVIEW`.

Implementation is complete within the authorized boundary. Production
integrity remains 108 compared, 103 byte-identical, 5 changed, 0 missing, and
1 new relative to frozen M00_L10. Test integrity remains 102 compared, 97
byte-identical, 5 changed, 0 missing, and 0 new. The protected
`RobotContainerElevatorCompositionTest.java` remains byte-identical to M00_L10.

The first focused-test attempt failed during `compileTestJava` because the
architecture-test helper had private visibility. This was a
`TEST_IMPLEMENTATION_DEFECT` with `NO_PRODUCTION_CAUSE`. The bounded repair
restored package-private visibility only. The next focused run executed 26
tests and recorded 25 passes and one failure in
`ElevatorObservationTest.equalValuesProduceEqualObservations()`. Its third
fixture supplied `positionMeters = 1.25`, `targetPositionMeters = 2.0`, and
`positionErrorMeters = 0.5`, violating the locked `target - position` rule.
The test-only repair changed that fixture's position to `1.50`; the invariant
is now `2.0 - 1.50 = 0.50`. This second issue was also a
`TEST_IMPLEMENTATION_DEFECT`; production validation was
`NO_PRODUCTION_IMPLEMENTATION_DEFECT` and `AUTHORIZED_AND_CORRECT`.

The User then forced a fresh focused-test run with `--rerun-tasks`:
`BUILD SUCCESSFUL in 18s`, four actionable tasks, all four executed, under
`PASS_M00_L11_USER_FOCUSED_TESTS`. User clean regression with
`gradlew clean build --rerun-tasks` passed:
`BUILD SUCCESSFUL in 37s`, seven actionable tasks, all seven executed, under
`PASS_M00_L11_USER_CLEAN_REGRESSION`.

User bounded Simulation passed the three required checkpoints under
`PASS_M00_L11_SIMULATION_DISABLED_IDLE_TELEMETRY`,
`PASS_M00_L11_SIMULATION_TELEOP_ENABLED_PERSISTENCE`, and
`PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`. Runtime remains
`new ElevatorSubsystem(new ElevatorIONoop())`; no Elevator request or physical
movement was claimed. Disabled idle, Teleoperated enabled, and return to
Disabled preserved all eight exact telemetry values:
`false / false / false / false / 0.0 / STOPPED / 0.0 / 0.0`.

The current evidence classification is exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`. Simulation evidence is
limited to Noop composition, safe idle, telemetry, and Disabled → Teleop
Enabled → Disabled persistence. It does not establish motor actuation,
convergence, encoder correctness, physical position, homing, gravity behavior,
travel limits, hardware communication, or mechanism safety.

M00_L11 remains `IN_PROGRESS / ACTIVE / IMPLEMENTATION COMPLETE` with focused
tests, clean regression, Simulation, and documentation reconciliation passed.
Independent Closure Review is `PENDING`; Freeze and Publication are not
authorized. M00_L12 retains homing/reference establishment, M00_L13 retains
travel-limit safety, and real hardware remains deferred.

## M00_L11 controlled freeze — 2026-09-22

The preceding implementation, verification, and reconciliation section is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`.

The authoritative lesson lifecycle is:

```text
STATUS: COMPLETE
LIFECYCLE: COMPLETE / FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
STATIC REVIEW: PASS
FOCUSED TESTS: PASS_M00_L11_USER_FOCUSED_TESTS
CLEAN REGRESSION: PASS_M00_L11_USER_CLEAN_REGRESSION
SIMULATION: PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION
DOCUMENTATION RECONCILIATION: COMPLETE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: NOT YET PUBLISHED / USER-OWNED
M00_L12: INACTIVE / NOT CREATED
M00_L13: FUTURE SCOPE / NOT ACTIVATED
```

The locked one-concept implementation, exact contracts, protected predecessor, both test-defect histories and bounded repairs, focused-test PASS, clean regression PASS, and bounded Simulation limits remain unchanged. Real hardware remains deferred. No publication, remote verification, hardware verification, or downstream activation is claimed. Earlier active-state wording remains historical chronology.

The inherited M00_L11 documentation snapshot follows below for provenance only.
Its old lesson identity and lifecycle are historical and are not the current
M00_L12 state above.


The inherited documentation snapshot follows below for provenance only. Its old lesson identity and lifecycle are historical and are not the current M00_L12 state above.

