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
