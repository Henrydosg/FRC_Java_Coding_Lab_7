# M00_L08 — Flywheel Closed-Loop Velocity

M00_L08 is `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`.
It introduces exactly one concept: vendor-neutral Flywheel closed-loop
velocity control through one validated semantic RPM request while preserving
measurement-only Observation and explicit safe stop.

## Current lifecycle

- **Previous lesson:** `M00_L07 - Flywheel Foundation`
- **Previous lesson state:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Preparation / inherited baseline:** `PASS_M00_L08_PREPARATION_BASELINE`
- **Architecture / Inheritance Audit:** `PASS_M00_L08_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L08_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `COMPLETE / ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`
- **Status:** `IN_PROGRESS`
- **Active state:** `ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`
- **Active lesson count:** `1`
- **Current active M00 lesson:** `M00_L08`
- **Implementation:** `PENDING SEPARATE AUTHORIZATION`
- **Verification:** `PENDING` except inherited untouched-copy baseline evidence
- **Bounded Simulation:** `PENDING`
- **Real hardware:** `DEFERRED`
- **M00_L09:** `INACTIVE / NOT CREATED`

Controlled Activation changes lifecycle and documentation identity only. No
production or test implementation has been performed.

## Locked design boundary

The authoritative velocity semantic is:

```java
void requestVelocity(double targetRpm);
```

`targetRpm` is finite, nonnegative Flywheel mechanism RPM. Positive values
request velocity control. Exactly `0.0` is the canonical safe-stop request.
NaN, infinities, and negative values fail closed: the subsystem records zero
and `STOPPED`, updates Observation, attempts `stop()`, and throws
`IllegalArgumentException`. `requestSpin()` is `REMOVED / SUPERSEDED`.

The post-L08 requested states are exactly `STOPPED` and
`VELOCITY_REQUESTED`. Target velocity remains subsystem control intent and is
not added to `FlywheelObservation` or telemetry. Observation remains the
immutable measurement/read-model boundary with `available`, `connected`,
`velocityValid`, `velocityRpm`, and `requestedState`.

The runtime remains `FlywheelIONoop` only. No physical adapter, hardware
configuration, CAN assignment, gain, or target RPM is authorized. CAN 50–54
remains a planning reservation only.

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

- M00_L09 owns Flywheel Ready-at-Speed, tolerance, dwell, debounce, and readiness policy.
- No Flywheel command, controller binding, default command, shooting, feeder coordination, automatic firing, NamedCommands, event markers, or autonomous mechanism integration is included.
- Physical hardware, tuning, calibration, and real-robot verification remain deferred.

## Lesson records

- [M00_L07 to M00_L08 transition guide](docs/M00_L07_to_M00_L08_Step_by_Step.md)
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
