# M00_L08 Lesson Plan and Lifecycle Record

## Current state

- **Lesson:** `M00_L08 - Flywheel Closed-Loop Velocity`
- **Predecessor:** `M00_L07 - Flywheel Foundation`
- **Predecessor state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`
- **Active lesson count:** `1`
- **Current active M00 lesson:** `M00_L08`
- **Preparation / inherited baseline:** `PASS_M00_L08_PREPARATION_BASELINE`
- **Architecture / Inheritance Audit:** `PASS_M00_L08_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L08_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `COMPLETE / ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`
- **Implementation:** `PENDING SEPARATE AUTHORIZATION`
- **Verification:** `PENDING`
- **Real hardware:** `DEFERRED`

## Sole new concept

```text
M00_L08 introduces vendor-neutral Flywheel closed-loop velocity control
through one validated semantic RPM request while preserving
measurement-only Observation and explicit safe stop.
```

## Locked semantic contract

The only velocity request is:

```java
void requestVelocity(double targetRpm);
```

The value is finite, nonnegative Flywheel mechanism RPM. Positive values
request velocity control. Exactly `0.0` is the canonical safe-stop request.
NaN, infinities, and negative values fail closed by recording zero and
`STOPPED`, updating Observation, attempting IO stop, and throwing
`IllegalArgumentException`; any stop failure is suppressed onto that
exception. `requestSpin()` is removed/superseded.

The subsystem owns `requestedVelocityRpm` and `requestedState`. The exact
states are `STOPPED` and `VELOCITY_REQUESTED`. Positive-request ordering is
validate, store target, set state, update Observation, and forward one IO
request. Explicit stop ordering is reset target, set STOPPED, update
Observation, and forward IO stop. `periodic()` remains observation/update only.

Observation remains unchanged as a five-component immutable measurement/read
model: `available`, `connected`, `velocityValid`, `velocityRpm`, and
`requestedState`. Target, error, tolerance, convergence, and readiness are
not Observation data in this lesson.

## Runtime and evidence boundary

`FlywheelIONoop` remains the only runtime adapter. It reports unavailable,
disconnected, invalid, and `velocityRpm = 0.0` deterministically and performs
safe no-op velocity requests and stops. No physical adapter, CAN assignment,
gain, target RPM, or hardware configuration is authorized.

Focused tests may verify request semantics using test doubles, but those tests
are not WPILib runtime Simulation evidence. Bounded Simulation may later verify
only Noop composition, deterministic telemetry/measurement state, STOPPED idle
behavior, no automatic Teleop request, and mode-transition persistence.
Physical velocity regulation, convergence, tuning, sensor fidelity, CAN, and
physical stop behavior remain unverified and real hardware remains deferred.

## Implementation boundary

Future implementation is limited to the four authorized Flywheel production
files and the six named inherited Flywheel-focused tests. No new production
class, no new test file, and no unrelated inherited test change is authorized.
`FlywheelTelemetryFacade`, `RobotTelemetry`, `RobotContainer`, and
`Constants.java` remain unchanged.

## Protected future scope

- M00_L09 exclusively owns Flywheel Ready-at-Speed and its tolerance, dwell, debounce, and readiness policy.
- Commands, controller bindings, shooting, Feeder/Flywheel coordination, automatic firing, NamedCommands, event markers, and autonomous mechanism integration remain outside M00_L08.
- M00_L09 remains `INACTIVE / NOT CREATED`.

## Activation interpretation

Controlled Activation changed lifecycle and documentation identity only. It did
not implement production code or tests and did not produce build, focused-test,
regression, Simulation, closure, freeze, publication, or Git evidence.

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

Future test reconciliation is limited to exactly these six existing files:

1. `src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`
2. `src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`
3. `src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`
4. `src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`
5. `src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`
6. `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`

No new test file or unrelated inherited test change is authorized.

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
