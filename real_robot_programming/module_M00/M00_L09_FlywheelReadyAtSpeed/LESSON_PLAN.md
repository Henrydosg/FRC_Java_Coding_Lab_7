# M00_L09 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `M00_L09 - Flywheel Ready-at-Speed`
- **Predecessor:** `M00_L08 - Flywheel Closed-Loop Velocity`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE / FROZEN / READ-ONLY`
- **Active lesson count:** `0`
- **Current active M00 lesson:** `NONE`
- **Preparation / inherited baseline:** `PASS_M00_L09_PREPARATION_BASELINE`
- **Architecture / Inheritance Audit:** `PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L09_FINAL_DESIGN_LOCK / READY_FOR_CONTROLLED_ACTIVATION`
- **Controlled Activation:** `COMPLETE / CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`
- **Implementation:** `COMPLETE / PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED`
- **Independent Static Review:** `PASS / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`
- **Focused Tests:** `PASS / PASS_M00_L09_USER_FOCUSED_TESTS`
- **Clean Full Regression:** `PASS / PASS_M00_L09_CLEAN_FULL_REGRESSION`
- **Bounded Simulation:** `PASS / PASS_M00_L09_BOUNDED_SIMULATION`
- **Verification:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `COMPLETE`
- **Real hardware:** `DEFERRED`
- **Independent Closure Review:** `PASS / PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`
- **Freeze:** `COMPLETE`
- **Independent Freeze Review:** `PENDING`
- **Publication:** `PENDING / NOT YET PUBLISHED`

## Sole new concept

```text
M00_L09 introduces exactly one concept: vendor-neutral instantaneous
Flywheel Ready-at-Speed classification from the current positive velocity
intent and latest valid velocity measurement.
```

## Locked semantic contract

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

## Runtime and evidence boundary

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

## Implementation boundary

Future implementation is limited to exactly four production files:
`Constants.java`, `FlywheelObservation.java`, `FlywheelSubsystem.java`, and
`FlywheelTelemetryFacade.java`. IO, Noop, `RobotTelemetry`, and
`RobotContainer` remain unchanged. Exactly four existing focused test files
may be modified; the inherited IO and RobotContainer composition tests remain
unchanged. No new production or test file is authorized.

## Protected future scope

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
