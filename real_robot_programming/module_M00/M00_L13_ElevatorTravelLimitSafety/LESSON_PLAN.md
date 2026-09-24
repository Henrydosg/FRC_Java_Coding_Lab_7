# M00_L13 — Elevator Travel-Limit Safety Lesson Plan

## Current frozen lifecycle, verification, and next gates — 2026-09-24

- M00_L12 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED; publication gate PASS_M00_L12_FINAL_PUBLICATION_VERIFICATION.
- Preparation, Architecture / Inheritance Audit, Final Design Lock, Controlled Activation, activation documentation reconciliation, and Independent Activation Re-review are accepted.
- Implementation Authorization: PASS_M00_L13_IMPLEMENTATION_AUTHORIZATION.
- Implementation handoff: PASS_M00_L13_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW.
- Final Independent Static Re-review: PASS_M00_L13_FINAL_INDEPENDENT_STATIC_REREVIEW / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS.
- Focused tests: PASS_M00_L13_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 42s; 4 actionable tasks, 4 executed.
- Clean regression: PASS_M00_L13_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 25s; 5 actionable tasks, 5 executed; GRADLE_EXIT_CODE=0.
- Bounded Simulation: PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION; Disabled -> Teleop Enabled -> Disabled, truthful Noop runtime only.
- Initial Closure Review HOLD_M00_L13_CLOSURE_REVIEW_STALE_AGENTS_IMPLEMENTATION_STATUS was resolved by PASS_M00_L13_CLOSURE_DOCUMENTATION_REPAIR and PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW / CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE.
- Current lifecycle: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED / NOT YET PUBLICATION-VERIFIED; Active Lesson Count 0; Current Active M00 Lesson NONE.
- Freeze Reconciliation is COMPLETE. Independent Freeze Review is NEXT / PENDING. Primary frozen snapshot publication, metadata publication, User push, and Final Publication Verification remain PENDING / USER-OWNED; no publication SHA is established.

## Sole concept — implemented request-admission envelope

A vendor-neutral software operational travel envelope for Elevator closed-loop position requests, expressed in the inherited logical position coordinate frame and enforced before an unsafe target reaches ElevatorIO. This is request-admission safety only; it does not implement physical hard limits, continuous overtravel prevention, runtime overshoot protection, controller soft-limit commissioning, homing redesign, or mechanism coordination.

ElevatorTravelLimits is an immutable vendor-neutral record of finite logical-meter minPositionMeters and maxPositionMeters with min < max. Invalid construction throws IllegalArgumentException. Finite negative and zero-crossing bounds are valid. The interval is inclusive, with no epsilon, tolerance, clamp, or target rewrite. Exact endpoints pass; targets below/above reject.

ElevatorSubsystem(ElevatorIO io) means no operational envelope is configured; no fake default exists. The bounded constructor requires non-null IO and limits. requestPositionMeters checks in this exact order: finite target or IllegalArgumentException; normalized positionValid && positionReferenced or IllegalStateException; configured limits or IllegalStateException; inclusive target membership or IllegalArgumentException. Acceptance records POSITION_REQUESTED and the exact target, rebuilds Observation, and calls io.requestPositionMeters(target) exactly once. Accepted IO exceptions propagate without retry or rollback; no periodic reissue occurs.

Rejection preserves requested state and target and the same Observation instance; it causes no rebuild and no position, stop, or homing IO. It does not cancel previously accepted intent. A valid/referenced current position outside the envelope does not reject an in-range target; recovery is caller-requested and periodic() does not actuate it.

requestHoming() is independent of limits, requires normalized available && connected but not positionValid or positionReferenced, records HOMING and inherited 0.0 target placeholder, rebuilds Observation, then requests IO once. HomeElevatorCommand is unchanged. stop() is always available and limit-independent; it records STOPPED/0.0, rebuilds Observation, and calls io.stop() once. periodic() performs one input update, normalization, and Observation rebuild without output.

## Inheritance and actual implementation delta

ElevatorIO remains four methods; ElevatorIOInputs five fields; ElevatorRequestedState exactly STOPPED, POSITION_REQUESTED, HOMING; ElevatorObservation and telemetry eight values. Constants.java and RobotContainer.java are unchanged. Runtime composition remains new ElevatorSubsystem(new ElevatorIONoop()) with no configured envelope, real Elevator hardware, ElevatorIOSim, limit binding, automatic homing, or autonomous mechanism integration.

Production comparison against frozen M00_L12: common 110, identical 109, changed 1, missing 0, added 1. ElevatorSubsystem.java was modified and ElevatorTravelLimits.java added. Test comparison: common 103, identical 101, changed 2, missing 0, added 1; ElevatorSubsystemTest.java and ElevatorArchitectureBoundaryTest.java were modified, and ElevatorTravelLimitsTest.java was added.

The initial static review HOLD concerned test and architecture-guard defects only: insufficient distinction between two IllegalStateException branches, compound hardware-limit identifier coverage, and same-name ElevatorIO overload coverage. PASS_M00_L13_TEST_GUARD_REPAIR changed only the two named test files; no production defect or design-lock change occurred. Final Independent Static Re-review passed. Six ignored bin/*.class differences are GENERATED_ARTIFACT_ONLY_NON_BLOCKING.

## User verification and evidence boundary

Focused suite PASS_M00_L13_USER_FOCUSED_TESTS: BUILD SUCCESSFUL in 42s; 4 actionable tasks, 4 executed. Classes were ElevatorTravelLimitsTest, ElevatorSubsystemTest, ElevatorArchitectureBoundaryTest, ElevatorIONoopTest, ElevatorObservationTest, HomeElevatorCommandTest, ElevatorTelemetryFacadeTest, and RobotContainerElevatorCompositionTest. No aggregate JUnit count was supplied.

Clean regression PASS_M00_L13_USER_CLEAN_REGRESSION: BUILD SUCCESSFUL in 25s; 5 actionable tasks, 5 executed; GRADLE_EXIT_CODE=0. The initial output had no Gradle verdict. A PowerShell evidence-capture attempt surfaced NativeCommandError due to a WPILib joystick stderr warning (“Joystick Button 6 on port 0 not available, check if controller is plugged in”); this was an evidence-capture issue, not test failure. Final cmd.exe capture preserved output and process exit code.

Simulation checkpoints: (1) PASS_M00_L13_SIMULATION_DISABLED_BASELINE, Robot disabled and DS attached; all Noop availability, connection, validity, and reference flags false; position/error/target 0.0 and STOPPED; zero is not proof of home. (2) PASS_M00_L13_SIMULATION_TELEOP_ENABLED_NO_UNCOMMANDED_ELEVATOR_ACTION, Robot enabled and DS attached; same truthful Noop snapshot and no uncommanded action. (3) PASS_M00_L13_SIMULATION_RETURN_TO_DISABLED, Robot disabled and DS attached; same Noop snapshot. Overall PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION covers Disabled -> Teleop Enabled -> Disabled only.

Evidence classification is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Simulation does not test a configured envelope through UI, physical bounds/overtravel, switches, vendor soft limits, real motor behavior, or real homing. Physical travel dimensions and hardware facts remain UNKNOWN / DEFERRED; test fixtures are not robot dimensions.

M00_L12 remains protected. M00_L14 remains INACTIVE / NOT CREATED; M00_L15 Intake-to-Feeder Coordination and M00_L16 Mechanism Autonomous Event Integration remain future scope. No mechanism NamedCommands or PathPlanner mechanism events are added.

## Historical controlled activation snapshot — 2026-09-23

- Predecessor M00_L12: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Predecessor primary / metadata SHA: 5cc4c2c1bc6b4108f2c710c3ca0b0cdbdc26ba49 / c1e90fad04469e5162b5c1814566dd534c6a0a6c
- Preparation: PASS_M00_L13_UNTOUCHED_COPY_BASELINE_BUILD; User-supplied BUILD SUCCESSFUL in 35s; 6 actionable tasks, 6 executed
- Architecture / Inheritance Audit: PASS_M00_L13_ARCHITECTURE_INHERITANCE_AUDIT
- Final Design Lock: PASS_M00_L13_FINAL_DESIGN_LOCK
- Controlled Activation: COMPLETE / READY FOR INDEPENDENT ACTIVATION REVIEW
- Status: ACTIVE / IN_PROGRESS / EDITABLE WITHIN FINAL DESIGN LOCK
- Active Lesson Count: 1; Current Active M00 Lesson: M00_L13 — Elevator Travel-Limit Safety
- Lifecycle: NOT COMPLETE / NOT FROZEN / NOT PUBLISHED
- Implementation Authorization: NOT YET AUTHORIZED; implementation NOT STARTED
- Tests NOT RUN; M00_L13 Simulation NOT VERIFIED; real hardware DEFERRED

## Sole new concept — LOCKED, NOT IMPLEMENTED

A vendor-neutral software operational travel envelope for Elevator closed-loop position requests, expressed in logical meters and enforced before an unsafe target reaches ElevatorIO. This is request-admission safety only; it does not establish physical limits or prevent physical overtravel.

### Historical activation snapshot — locked behavior and scope

ElevatorTravelLimits is planned as an immutable vendor-neutral value with finite min/max meter bounds and min < max; finite negative bounds are valid. Its interval is inclusive; no tolerance, clamp, or rewrite. The existing ElevatorSubsystem(ElevatorIO io) means no envelope is configured; a bounded constructor requires non-null limits. An otherwise valid/referenced position request without limits fails closed.

requestPositionMeters validates in order: finite target; normalized positionValid && positionReferenced; configured limits; inclusive target membership; accepted-only state/target update, Observation rebuild, and one IO call. Rejections preserve intent, target, and the same Observation instance, with no position, stop, or homing IO call. Accepted IO exceptions propagate without retry or rollback. An out-of-envelope current position does not reject an in-envelope target; recovery is caller-requested. Homing needs no limits, stop is universal, periodic is output-free.

ElevatorIO stays four methods; Inputs five fields; RequestedState three values; Observation and telemetry eight values. Constants, RobotContainer, HomeElevatorCommand, commands, and concrete adapters remain unchanged. The subsystem is the sole enforcement owner. No hardware travel values or limits are established. M00_L14 remains inactive/not created; M00_L15/L16 remain future scope.

### Historical activation snapshot — planned implementation delta

PLANNED / NOT YET IMPLEMENTED. NEW production type: ElevatorTravelLimits.java:

```java
public record ElevatorTravelLimits(
    double minPositionMeters,
    double maxPositionMeters
)
```

Validation requires finite min and max values and minPositionMeters < maxPositionMeters; invalid construction throws IllegalArgumentException. Finite negative bounds are allowed. Production scope remains limited to adding ElevatorTravelLimits.java and modifying ElevatorSubsystem.java only. Tests: add ElevatorTravelLimitsTest.java; modify ElevatorSubsystemTest.java and ElevatorArchitectureBoundaryTest.java only as needed. Minimal inherited fixture updates only where necessary; preserve inherited assertions.

### Historical activation snapshot — required test plan

Value-object tests: positive, negative, zero-crossing, NaN/infinite, equal/reversed, deterministic immutable values. Subsystem tests: interior and inclusive endpoints, below/above rejection, negative target, unconfigured fail-closed and precondition order, one accepted IO call, accepted IO exception no-retry/no-rollback.

Required outside-current admission test: valid/referenced current position outside configured envelope and target inside; prove acceptance, recorded intent/target, Observation rebuild, one position IO call, no stop/homing. At least one side; values are test fixtures only.

Required rejection identity tests: below min, above max, and unconfigured limits. Capture Observation and assertSame after rejection; check exception, unchanged state/target, and zero position/stop/homing calls. Accepted tests prove Observation rebuild. Preserve inherited invalid-target and invalid-reference tests.

Also test homing without limits, unchanged HomeElevatorCommand, stop with and without limits, output-free periodic, no outside-current automatic output, and no fabricated reference. Architecture tests preserve repaired inherited guards and exact contracts without brittle false positives.

### Historical activation snapshot — verification boundary and next gates

Baseline is preparation evidence only. Activation claims no implementation, tests, Simulation, freeze, or publication. Bounded Noop Simulation later cannot establish physical bounds, switches, soft limits, or stopping. Maximum planned evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Independent Activation Review is next; implementation authorization and all later gates remain pending.

## Historical copied M00_L12 lesson-plan snapshot

The inherited current M00_L12 lesson plan follows verbatim as historical provenance, not current M00_L13 authority.

# M00_L12 — Elevator Homing Lesson Plan

## Current state

- **Lesson:** `M00_L12 - Elevator Homing`
- **Predecessor:** `M00_L11 - Elevator Closed-Loop Position`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`
- **Active Lesson Count:** `0`
- **Current Active M00 Lesson:** `NONE`
- **Final Design Lock:** `PASS_M00_L12_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `COMPLETE`
- **Activation duplicate-documentation repair:** `PASS_M00_L12_ACTIVATION_DUPLICATE_DOCUMENTATION_RECONCILIATION`
- **Activation re-review:** `ACTIVATION_REREVIEW_PASS_READY_FOR_IMPLEMENTATION_AUTHORIZATION`
- **Implementation Authorization:** `PASS_M00_L12_IMPLEMENTATION_AUTHORIZATION`
- **Implementation:** `COMPLETE / IMPLEMENTATION_COMPLETE_READY_FOR_INDEPENDENT_STATIC_REVIEW`
- **Final Independent Static Re-review:** `PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW`
- **Focused Tests:** `PASS_M00_L12_USER_FOCUSED_TESTS / FRESH --rerun-tasks / BUILD SUCCESSFUL in 35s / 4 TASKS EXECUTED`
- **Clean Regression:** `PASS_M00_L12_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 24s / 5 TASKS EXECUTED`
- **Simulation:** `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION / THREE BOUNDED CHECKPOINTS`
- **Evidence:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Independent Closure Review:** `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`
- **Freeze Reconciliation:** `COMPLETE / FREEZE_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_FREEZE_REVIEW`
- **Independent Freeze Review:** `PENDING / NEXT GATE`
- **Publication:** `NOT PUBLISHED / PENDING / USER-OWNED`
- **Publication SHA:** `NONE / NOT YET ESTABLISHED`
- **Real hardware:** `DEFERRED`
- **M00_L13:** `INACTIVE / NOT CREATED`

## Sole new concept

M00_L12 introduces exactly one concept:

> A bounded, scheduler-managed Elevator homing lifecycle that requests vendor-neutral reference acquisition and recognizes success only from the normalized IO-reported `positionReferenced` semantic.

`positionReferenced` is the sole reference authority. `positionMeters = 0.0` is only a logical coordinate value and never proves physical home. Homing does not add a hardware-specific input, a second homed authority, travel-limit policy, or an implementation-specific timeout.

## Implemented boundary

The actual production delta versus frozen M00_L11 is `109 / 105 / 4 / 0 / 1` (compared / identical / changed / missing / added). Changed production files are `ElevatorIO.java`, `ElevatorIONoop.java`, `ElevatorRequestedState.java`, and `ElevatorSubsystem.java`; new production file is `HomeElevatorCommand.java`. Observation and telemetry contracts remain unchanged. `RobotContainer.java` and `Constants.java` remain unchanged. No real adapter or `ElevatorIOSim` exists.

The test delta is `102 / 98 / 4 / 0 / 1` (compared / identical / changed / missing / added). Changed tests are `ElevatorArchitectureBoundaryTest.java`, `ElevatorIONoopTest.java`, `ElevatorObservationTest.java`, and `ElevatorSubsystemTest.java`; `HomeElevatorCommandTest.java` is new. Deploy/config/support remains `24 / 24 / 0 / 0 / 0`.

## Scope firewall

M00_L12 does not own upper/lower travel limits, clamps, workspace envelopes, general motion inhibition, post-home bounds, PID/feedforward/Motion Magic, autonomous integration, coordination, physical homing performance, calibration, or hardware configuration. M00_L13 owns general travel-limit safety.

## Current gate sequence

1. Untouched-copy preparation and baseline build — accepted.
2. Architecture / Inheritance Audit and Final Design Lock — accepted.
3. Controlled Activation, duplicate-current-block repair, and activation re-review — accepted.
4. Implementation Authorization and implementation — accepted.
5. Three bounded architecture-test repair episodes — accepted; all were test implementation defects, with no production defect.
6. Final Independent Static Re-review — passed.
7. User focused tests — passed with fresh `--rerun-tasks`: `BUILD SUCCESSFUL in 35s`, four actionable tasks executed. The seven focused classes are listed in the transition guide; no aggregate test count was supplied.
8. User clean full regression — passed: `BUILD SUCCESSFUL in 24s`, five actionable tasks executed.
9. User bounded Simulation — passed with Disabled baseline, Teleop enabled without uncommanded homing, and return-to-Disabled checkpoints. No physical homing was simulated or proven.
10. Documentation reconciliation — complete.
11. Independent Closure Review — passed as `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`; no remaining legitimate findings.
12. Freeze Reconciliation — complete; M00_L12 is `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`, Active Lesson Count is `0`, and no M00 lesson is active.
13. Independent Freeze Review — pending and is the current next gate.
14. Publication and final publication verification — pending User-owned Git workflow; no publication SHA is established. Real hardware remains deferred.

## Historical inherited earlier lesson records

The inherited documentation snapshot follows below for provenance only. Its old lesson identity and lifecycle are historical and are not the current M00_L12 plan above.
# M00_L10 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `M00_L10 - Elevator Foundation and Position-Reference Semantics`
- **Predecessor:** `M00_L09 - Flywheel Ready-at-Speed`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Current active M00 lesson:** `NONE`
- **Preparation / inherited baseline:** `PASS_M00_L10_PREPARATION_BASELINE`
- **Architecture / Inheritance Audit:** `PASS_M00_L10_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L10_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `PASS_M00_L10_CONTROLLED_ACTIVATION`
- **Implementation:** `COMPLETE / PASS_M00_L10_IMPLEMENTATION_REPORT_ACCEPTED`
- **Independent Static Review:** `PASS / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`
- **Focused Tests:** `PASS_M00_L10_USER_FOCUSED_TESTS / USER VERIFIED`
- **Clean Full Regression:** `PASS_M00_L10_CLEAN_FULL_REGRESSION / BUILD SUCCESSFUL in 27s / 7 ACTIONABLE TASKS (7 EXECUTED)`
- **Bounded Simulation:** `PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION`
- **Verification:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Real hardware:** `DEFERRED`
- **Independent Closure Review:** `PASS / PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW`
- **Freeze Authorization:** `PASS_M00_L10_FREEZE_AUTHORIZATION`
- **Freeze:** `COMPLETE`
- **Independent Freeze Review:** `PENDING`
- **Publication:** `PENDING / NOT YET PUBLISHED`

## Sole new concept

```text
M00_L10 introduces exactly one concept: vendor-neutral Elevator
position-observation and reference semantics.

An independently owned Elevator reports linear mechanism position in a defined
software coordinate frame, together with explicit measurement-validity and
reference-trust semantics, without performing position control, homing, or
travel-limit enforcement.
```

## Historical inherited M00_L09 semantic contract

`FlywheelSubsystem` owns readiness through one private deterministic,
side-effect-free helper. It recomputes `readyAtSpeed` on every Observation
rebuild. The exact predicate is true only when requested state is
`VELOCITY_REQUESTED`, target is finite and greater than zero, inputs are
available/connected/velocity-valid, measured velocity and target are finite,
and `Math.abs(measured-target) <= Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm`.
The comparison is
symmetric and inclusive with no second epsilon.

The exact policy tolerance is `Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm`,
type `double`, value `50.0`, unit mechanism RPM.
It is a `PROVISIONAL SOFTWARE-POLICY ACCEPTANCE TOLERANCE`, not hardware-tuned
and not real-robot validated. It is not a physical target, maximum, percentage,
or tuned threshold.

Observation adds exactly `readyAtSpeed` to the existing immutable fields
`available`, `connected`, `velocityValid`, `velocityRpm`, and `requestedState`.
Its constructor invariant requires readiness to imply available, connected,
velocity-valid input and `VELOCITY_REQUESTED`; target positivity and tolerance
remain subsystem-owned. STOPPED, zero/negative-zero intent, stale matching
measurements, invalid/unavailable input, and outside-tolerance values are
false. There is no dwell, debounce, hysteresis, history, state machine,
command, or automatic action.

## Historical inherited M00_L09 runtime and evidence boundary

`FlywheelIONoop` remains the only runtime adapter. It reports unavailable,
disconnected, invalid, and `velocityRpm = 0.0` deterministically, so runtime
readiness is false. No physical adapter, CAN assignment, gain, target RPM, or
hardware configuration is authorized.

Focused tests may verify request semantics using test doubles, but those tests
are not WPILib runtime Simulation evidence. Bounded Simulation may later verify
only Noop composition, deterministic telemetry/measurement state, STOPPED idle
behavior, no automatic Teleop request, and mode-transition persistence.
Physical velocity regulation, convergence, tuning, sensor fidelity, CAN, and
physical stop behavior remain unverified and real hardware remains deferred.

## Historical inherited M00_L09 implementation boundary

Future implementation is limited to exactly four production files:
`Constants.java`, `FlywheelObservation.java`, `FlywheelSubsystem.java`, and
`FlywheelTelemetryFacade.java`. IO, Noop, `RobotTelemetry`, and
`RobotContainer` remain unchanged. Exactly four existing focused test files
may be modified; the inherited IO and RobotContainer composition tests remain
unchanged. No new production or test file is authorized.

## Historical inherited M00_L09 protected future scope

- M00_L09 exclusively owns Flywheel Ready-at-Speed and its provisional
  acceptance tolerance; dwell, debounce, history, and readiness state-machine
  policy are explicitly excluded.
- Commands, controller bindings, shooting, Feeder/Flywheel coordination,
  automatic firing, NamedCommands, event markers, and autonomous mechanism
  integration remain outside M00_L09.
- M00_L10 remains `INACTIVE / NOT CREATED`; M00_L09 is the sole active lesson.

## Activation interpretation

Controlled Activation changed lifecycle and documentation identity only. Later
implementation and User-owned verification are recorded in the append-only
reconciliation below; closure, freeze, publication, and successor activation
remain separate gates.

## Current controlled activation record — 2026-09-21

Historical M00_L08 reconciliation text below this point is retained only as
inherited snapshot history. The current record is M00_L09
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, sole active M00
lesson, implementation `NOT STARTED`, and Independent Activation Review
`PENDING`. No implementation, tests, build, Simulation, closure, freeze,
publication, or Git evidence is claimed. M00_L08 remains frozen, published,
and verified; M00_L10 is inactive/not created.

## Documentation repair clarification

### Exact future FlywheelIO contract

`FlywheelIOInputs` contains exactly `boolean available`, `boolean connected`,
`boolean velocityValid`, and `double velocityRpm`. Its exact public methods are
`void updateInputs(FlywheelIOInputs inputs)`,
`void requestVelocity(double targetRpm)`, and `void stop()`.
`requestSpin()` is `REMOVED / SUPERSEDED`. Vendor types, vendor control
objects, gain parameters, and hardware-configuration parameters are excluded.

### Request ordering and exception behavior

For a valid positive request: validate `targetRpm`; record
`requestedVelocityRpm`; set `requestedState = VELOCITY_REQUESTED`; replace the
immutable Observation; and forward exactly one
`flywheelIO.requestVelocity(targetRpm)`. If forwarding throws, target/state/
Observation remain recorded, the exception propagates, no rollback occurs, and
`periodic()` does not retry. Exactly `0.0` records zero, sets `STOPPED`,
replaces Observation, and forwards exactly one `flywheelIO.stop()`.

Invalid values are NaN, positive infinity, negative infinity, and negative
finite values. Invalid handling records zero/`STOPPED`, replaces Observation,
attempts `flywheelIO.stop()`, and throws `IllegalArgumentException`; no invalid
target is forwarded. If stop throws, the IllegalArgumentException remains
primary with stop failure suppressed, and zero/STOPPED intent remains recorded.
Explicit stop is unconditional; if it throws, zero/STOPPED/Observation remain
and the exception propagates. There is no rollback, automatic restart, or
periodic output reissue.

### Exact Noop and composition boundaries

Every future `FlywheelIONoop.updateInputs()` sets
`available=false`, `connected=false`, `velocityValid=false`, and
`velocityRpm=0.0`. `requestVelocity(double)` and `stop()` are safe deterministic
no-ops without convergence or physical modeling; invalid zero RPM is not a
measured physical zero RPM.

`RobotContainer` and all existing telemetry/configuration remain unchanged.
The exact runtime composition is `new FlywheelSubsystem(new FlywheelIONoop())`.
No Flywheel command, binding, default command, direct request/stop,
autonomous registration, NamedCommands, event markers, Feeder/Flywheel
coordination, physical hardware selection, or Flywheel-specific
`RobotBase.isReal()` branch is permitted.

Future M00_L09 test implementation may modify exactly these four files:

1. `src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`
2. `src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`
3. `src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`
4. `src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`

These two inherited tests remain unchanged:

1. `src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`
2. `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`

Create no test file. No unrelated inherited test change is authorized.

## Post-verification reconciliation — 2026-09-20

The implementation phase is complete within the authorized four production
files and six inherited focused-test files. The final concept is one
vendor-neutral semantic Flywheel RPM request:
`void requestVelocity(double targetRpm)`; `requestSpin()` is
`REMOVED / SUPERSEDED`. Requested states are `STOPPED` and
`VELOCITY_REQUESTED`. `FlywheelIOInputs` contains only `available`,
`connected`, `velocityValid`, and `velocityRpm`; IO exposes only
`updateInputs(...)`, `requestVelocity(...)`, and `stop()`. Finite nonnegative
RPM is valid, zero (`+0.0` or `-0.0`) is canonical stop, invalid NaN,
infinities, and negatives fail closed to zero/`STOPPED`, and stop is recorded
before unconditional IO stop. Observation remains measurement-only and
`periodic()` is update/rebuild only. Noop remains false/false/false/0.0 with
no physical model; CAN 50–54 and real hardware are deferred.

The final M00_L07 comparison is production `103 / 99 / 4 / 0 / 0` and tests
`96 / 90 / 6 / 0 / 0` for Compared / Byte-identical / Changed / Missing /
Added. Only the four named production and six named test paths changed; no
new file was added, and protected configuration plus M00_L07 remain
unchanged.

Static chronology remains visible: initial Independent Static Review `HOLD`
for stale stop target, weak Noop assertions, missing negative-zero coverage,
and brittle comment stripping; bounded repair `COMPLETE`; final independent
static rereview `PASS` under `PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.
User focused tests are accepted as `BUILD SUCCESSFUL in 16s`, `4 actionable
tasks: 3 executed, 1 up-to-date`, `FOCUSED TESTS: PASS` under
`PASS_M00_L08_USER_FOCUSED_TESTS`. Clean regression is accepted as
`BUILD SUCCESSFUL in 26s`, `7 actionable tasks: 7 executed`, `CLEAN
REGRESSION: PASS` under `PASS_M00_L08_CLEAN_FULL_REGRESSION`.

Bounded Simulation is accepted under `PASS_M00_L08_BOUNDED_SIMULATION`:
Disabled reports Available=false, Connected=false, RequestedState=STOPPED,
VelocityRpm=0.0, VelocityValid=false; Teleoperated enabled with no driver
action reports Robot Enabled=Yes and the same values; returning Disabled
reports FMS Robot Enabled=No and the same values. These are Noop/lifecycle
claims only. `VelocityRpm=0.0` with `VelocityValid=false` is not physical
measured zero, and **requestVelocity runtime exercise was NOT claimed**.
Physical regulation, convergence, gains, feedforward, sensor fidelity, RPM
accuracy, direction, CAN, and physical stop behavior remain unverified;
requestVelocity runtime exercise was NOT claimed.
Evidence classification is `THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED`. The exact status is REAL HARDWARE DEFERRED.

The current lifecycle is `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK`, active lesson count `1`, current lesson `M00_L08`, implementation
complete, static/focused/regression/Simulation PASS, Independent Closure Review
`PENDING`, Freeze `NOT AUTHORIZED`, and Publication `NOT AUTHORIZED`.
REAL HARDWARE: DEFERRED.
M00_L09 remains `INACTIVE / NOT CREATED`; Ready-at-Speed and all later command,
shooting, coordination, and autonomous mechanism scope remain excluded.

## Controlled freeze transition — 2026-09-20

The accepted Independent Closure Review returned
`READY_FOR_FREEZE_AUTHORIZATION` under
`PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW`; Architect freeze authorization is
consumed. M00_L08 is now `COMPLETE / FROZEN / READ-ONLY`, with active lesson
count `0` and current active M00 lesson `NONE`.

The implementation, static re-review, focused tests, clean regression, and
bounded Simulation remain PASS. Evidence remains exactly
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Publication
is `PENDING / NOT YET PUBLISHED`; Final Publication Verification is
`NOT YET PERFORMED`; M00_L09 remains `INACTIVE / NOT CREATED`.

This transition changes lifecycle documentation only. No source, test,
configuration, dependency, deployment, or verification evidence changed, and
no publication, metadata reconciliation, or successor activation occurred.

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
PUBLICATION: PENDING / NOT YET PUBLISHED
FINAL PUBLICATION VERIFICATION: NOT YET PERFORMED
M00_L09: INACTIVE / NOT CREATED
```

## Historical activation documentation repair reconciliation — 2026-09-21

The Architect gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`; the bounded
engineer verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
Independent Activation Review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW` pending re-review.

The canonical external predecessor is M00_L08
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, primary
`5daecd970ff95fb906d6de9d5bc22a5cb094877d`, metadata
`a76dc33c2b485b4988e7058cbfed0fa3362cc560` with parent = primary, final
remote-aligned HEAD = metadata SHA, and verdict `PUBLICATION_VERIFIED`.
No third publication commit was required. Historical frozen snapshot pending
wording remains valid historical evidence under the passing two-commit model;
the frozen lesson was not changed.

The exact readiness formula and inclusive 50.0 RPM provisional software policy
remain locked. The repair records the measured-zero clarification without a new
condition, cached-intent forwarding exception semantics, six telemetry fields
and exclusions, automatic-action prohibition, full boundary truth table,
exact floating comparison policy, focused-test coverage matrix, eventual
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED` plan, and the
complete unknown/deferred hardware list. Current verification is still PENDING.

The later implementation and verification reconciliation supersedes the pending
wording above. M00_L09 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL
DESIGN LOCK`, implementation `COMPLETE`, Active Lesson Count `1`, and M00_L10
remains `INACTIVE / NOT CREATED`; no roadmap or downstream scope changed.

## Final implementation and verification reconciliation — 2026-09-21

Implementation is complete under `PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED`.
The initial Independent Static Review was `HOLD`; the bounded repair was
`PASS`; the Final Independent Static Re-review passed with
`STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`.

Production inheritance against frozen M00_L08 is `103 / 99 / 4 / 0 / 0` and
test inheritance is `96 / 92 / 4 / 0 / 0` for Compared / Byte-identical /
Changed / Missing / Added. The four authorized production files and four
authorized focused tests are the only changed paths; no new file or
configuration change exists.

User-focused tests passed with `BUILD SUCCESSFUL in 22s`; four actionable tasks
were reported, three executed and one up-to-date. Clean full regression passed
with `BUILD SUCCESSFUL`; seven actionable tasks were reported, all seven
executed. The accepted gates are `PASS_M00_L09_USER_FOCUSED_TESTS` and
`PASS_M00_L09_CLEAN_FULL_REGRESSION`.

Bounded Simulation passed all three checkpoints under
`PASS_M00_L09_BOUNDED_SIMULATION`: Disabled, Teleoperated enabled with no
controller action, and return to Disabled. The Noop state remained unavailable,
disconnected, invalid, `VelocityRpm=0.0`, `RequestedState=STOPPED`, and
`ReadyAtSpeed=false`. This is Noop/lifecycle evidence only; it does not prove
ReadyAt-Speed true, tolerance boundaries at runtime, physical convergence, or
real hardware behavior.

Evidence is exactly `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`. Documentation reconciliation is complete. The lesson remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, Active Lesson Count
`1`, and M00_L10 remains `INACTIVE / NOT CREATED`. Independent Closure Review
is `PENDING`; Freeze and Publication are `NOT AUTHORIZED`.

## Controlled freeze transition — 2026-09-21

The final independent closure re-review passed with
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`, and Architect freeze
authorization was accepted. M00_L09 is now `COMPLETE / FROZEN / READ-ONLY`.
Independent Freeze Review is `PENDING`; Publication is `PENDING / NOT YET
PUBLISHED`. Active Lesson Count is `0`, Current Active M00 Lesson is `NONE`,
and M00_L10 remains `INACTIVE / NOT CREATED`.

The technical contract, architecture, integrity counts, and evidence
classification remain unchanged. No publication SHA or Git event is claimed.

## M00_L10 controlled activation snapshot — 2026-09-21

Everything above that describes M00_L09 implementation, verification, freeze,
or publication is historical inherited snapshot material. It is superseded for
current-state purposes by this M00_L10 record.

At the activation point, M00_L10 was `IN_PROGRESS / ACTIVE / EDITABLE WITHIN
FINAL DESIGN LOCK`, the sole active M00 lesson. Implementation was `NOT
STARTED`; Independent Activation Review was `HOLD — RE-REVIEW REQUIRED`; and
Freeze and Publication were `NOT AUTHORIZED`.
M00_L11 is `INACTIVE / NOT CREATED`. No build, tests, runtime Simulation, or
Git operation is authorized in this documentation-only activation.

The exact transition, normalization, IO, Observation, subsystem, telemetry,
composition, test, simulation, and evidence contracts are in
`docs/M00_L09_to_M00_L10_Step_by_Step.md`. That guide is the current lesson
record and does not alter the Frozen Backbone, M00 roadmap, M00_L09 snapshot,
or hardware-unknown boundary.

## Final implementation, verification, and documentation reconciliation — 2026-09-22

M00_L10 implementation is complete under
`PASS_M00_L10_IMPLEMENTATION_REPORT_ACCEPTED`. Final Independent Static
Re-review passed. The focused-test chronology records the temporary
architecture-test false positive, its `TEST_IMPLEMENTATION_DEFECT`
classification, the bounded test-only repair, and the independent repair
re-review. The User then reran all six focused test classes successfully under
`PASS_M00_L10_USER_FOCUSED_TESTS`.

The User supplied clean full regression evidence:
`PASS_M00_L10_CLEAN_FULL_REGRESSION`, `BUILD SUCCESSFUL in 27s`, seven
actionable tasks, all seven executed. The User supplied bounded Simulation
evidence under `PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION` for Disabled →
Teleoperated/Enabled → Disabled. Elevator telemetry remained
`false / false / false / false / 0.0`; no `RequestedState` exists. The Noop
zero is the canonical invalid placeholder, not physical zero or homing
evidence.

The current evidence classification is `THEORY VERIFIED`,
`SIMULATION VERIFIED`, `REAL HARDWARE DEFERRED`. M00_L11, M00_L12, and M00_L13
remain protected future scopes for position control, homing/reference
establishment, and travel limits. The lesson is ready for Independent Closure
Review; freeze, publication, and successor activation remain unauthorized.

## Bounded activation documentation repair snapshot — 2026-09-21

This section is historical activation-repair evidence. Its pending wording is
superseded by the final reconciliation below.

The five semantic definitions are explicit: `available` means the IO source
can provide Elevator information; `connected` means the relevant device/data
path is connected; `positionValid` means finite `positionMeters` is valid under
the meters/sign contract; `positionReferenced` means the origin is trusted
against the logical reference origin; and `positionMeters` is the vendor-neutral
linear mechanism position in meters in the logical software frame.

Validity does not imply reference. Reference requires availability, connection,
validity, and `Double.isFinite(positionMeters)`. M00_L10 does not establish
reference; M00_L12 owns homing/reference establishment.

The IO contract prohibits `requestPosition`, `setPosition`, `setVoltage`,
`setPercent`, `setDutyCycle`, PID, PIDF, feedforward, Motion Magic, homing,
zeroing, and limit methods, plus open-loop, closed-loop request, and target
position APIs. Noop `0.0` is the **CANONICAL INVALID PLACEHOLDER**, never
physical or valid zero. The required valid-zero/unreferenced test case is
recorded in the transition guide.

## Controlled freeze transition — 2026-09-22

`PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW` was accepted with verdict
`CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_AUTHORIZATION`, followed by
`PASS_M00_L10_FREEZE_AUTHORIZATION`.

M00_L10 is now `COMPLETE / FROZEN / READ-ONLY`, with Active Lesson Count `0`
and Current Active M00 Lesson `NONE`. The evidence classification remains
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Independent
Freeze Review is `PENDING`, Publication is `PENDING / NOT YET PUBLISHED`, and
M00_L11 remains `INACTIVE / NOT CREATED`.

This transition changed lesson-local lifecycle documentation only. No source,
tests, deploy/configuration, support files, Git state, publication metadata, or
future review result was changed or recorded.

## Controlled Activation and Final Design Lock — 2026-09-22

M00_L11 is now the sole active M00 lesson:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L11
IMPLEMENTATION: NOT STARTED
INDEPENDENT ACTIVATION REVIEW: PENDING
M00_L10: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L12: INACTIVE / NOT CREATED
```

Accepted gates are `PASS_M00_L11_PREPARATION_BASELINE`,
`PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT`,
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`, and
`PASS_M00_L11_FINAL_DESIGN_LOCK`.

The one new concept is vendor-neutral Elevator closed-loop position request
semantics in meters. The exact future IO request is
`requestPositionMeters(double)` beside `updateInputs(...)` and `stop()`; the
exact requested states are `STOPPED` and `POSITION_REQUESTED`. The observation
adds requested state, target position, and derived position error to the five
inherited members. Requests require finite target, valid position, and trusted
reference; finite negative targets are allowed and no physical range clamp
exists. Invalid requests make no mutation or IO call. Valid requests record
intent, rebuild the immutable observation, and forward once. `periodic()` does
not reissue output. Stop records stopped/target `0.0`, rebuilds, and forwards
one stop.

The future write boundary is one new requested-state production file and five
existing Elevator production files, with five existing focused Elevator tests
modifiable. RobotContainer, RobotTelemetry, Constants, commands, bindings,
autonomous integration, real adapters, and ElevatorIOSim remain unchanged.
M00_L12 owns homing/reference establishment; M00_L13 owns travel limits and
target clamping. Runtime remains Noop-only with evidence target
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

No implementation authorization, implementation, test, build, Simulation,
closure, freeze, publication, or Git result is recorded. See
`docs/M00_L10_to_M00_L11_Step_by_Step.md` for the complete record.

## M00_L11 implementation and verification reconciliation — 2026-09-22

The preceding activation material is historical. The current lesson state is:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L11
IMPLEMENTATION: COMPLETE
INDEPENDENT STATIC REVIEW: PASS
FOCUSED TESTS: PASS_M00_L11_USER_FOCUSED_TESTS / BUILD SUCCESSFUL in 18s
CLEAN REGRESSION: PASS_M00_L11_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 37s
SIMULATION: PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION
DOCUMENTATION RECONCILIATION: COMPLETE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PENDING
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L12: INACTIVE / NOT CREATED
```

The implementation preserves the locked one-concept boundary: finite
vendor-neutral position requests in meters, `STOPPED` and
`POSITION_REQUESTED`, the exact eight-field immutable Observation, valid and
referenced-position gating, signed target-minus-position error, no travel
clamp, no periodic reissue, and explicit stop semantics. M00_L12 owns
homing/reference establishment; M00_L13 owns travel-limit safety and target
clamping.

The focused-test chronology is preserved. The compile helper visibility defect
and the later inconsistent equality fixture were both test defects. The
forced fresh focused run and clean regression passed under the gates recorded
above. Simulation evidence is limited to Noop runtime composition, exact
telemetry, safe idle, and Disabled → Teleop Enabled → Disabled persistence.
Real hardware remains deferred.

## M00_L11 controlled freeze — 2026-09-22

The preceding implementation and verification reconciliation is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`.

```text
STATUS: COMPLETE
LIFECYCLE: COMPLETE / FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
INDEPENDENT STATIC REVIEW: PASS
FOCUSED TESTS: PASS_M00_L11_USER_FOCUSED_TESTS
CLEAN REGRESSION: PASS_M00_L11_USER_CLEAN_REGRESSION
SIMULATION: PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION
DOCUMENTATION RECONCILIATION: COMPLETE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: NOT YET PUBLISHED / USER-OWNED
M00_L12: INACTIVE / NOT CREATED
M00_L13: FUTURE SCOPE / NOT ACTIVATED
```

The locked one-concept boundary, exact contracts, predecessor integrity, focused-test history, clean regression, bounded Simulation limits, and real-hardware deferral remain preserved. No downstream activation or publication is claimed. Earlier active-state wording remains historical chronology.

The inherited M00_L11 documentation snapshot follows below for provenance only.
Its old lesson identity and lifecycle are historical and are not the current
M00_L12 plan above.


The inherited documentation snapshot follows below for provenance only. Its old lesson identity and lifecycle are historical and are not the current M00_L12 plan above.

