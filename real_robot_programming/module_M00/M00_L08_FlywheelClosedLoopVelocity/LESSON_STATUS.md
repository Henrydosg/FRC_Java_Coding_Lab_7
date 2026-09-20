# LESSON_STATUS

## Identity and lifecycle

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson:** `M00_L08 - Flywheel Closed-Loop Velocity`
- **Directory:** `M00_L08_FlywheelClosedLoopVelocity`
- **Previous Lesson:** `M00_L07 - Flywheel Foundation`
- **Previous Lesson State:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `IN_PROGRESS`
- **Active State:** `ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`
- **Freeze State:** `NOT FROZEN`
- **Editable Boundary:** `WITHIN ACCEPTED FINAL DESIGN LOCK ONLY`
- **Active Lesson:** `YES`
- **Current Active M00 Lesson:** `M00_L08`
- **Active Lesson Count:** `1`

## Required status fields

- **Architecture Review:** `PASS / PASS_M00_L08_ARCHITECTURE_INHERITANCE_AUDIT`
- **Baseline Build:** `PASS / UNTOUCHED INHERITED COPY / PASS_M00_L08_PREPARATION_BASELINE`
- **Build:** `PENDING SEPARATE USER VERIFICATION`
- **Simulation:** `PENDING`
- **Driver Station / Glass:** `PENDING`
- **Real Robot:** `REAL HARDWARE DEFERRED`
- **Transition Guide:** `CREATED / FINALIZATION PENDING IMPLEMENTATION AND VERIFICATION`
- **Git Commit:** `PENDING / USER-OWNED`
- **Git Push:** `PENDING / USER-OWNED`
- **Known Issues:** `PHYSICAL FLYWHEEL HARDWARE AND CONFIGURATION UNKNOWN; CAN 50-54 IS PLANNING-ONLY`

## Accepted gates and current phase

- **Preparation / Baseline:** `PASS_M00_L08_PREPARATION_BASELINE`
- **Inheritance:** `PASS / 103 OF 103 PRODUCTION AND 96 OF 96 TEST FILES BYTE-IDENTICAL`
- **Architecture / Inheritance Audit:** `PASS_M00_L08_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L08_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `COMPLETE / ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`
- **Implementation Authorization:** `PENDING SEPARATE AUTHORIZATION`
- **Implementation:** `NOT STARTED`
- **Focused Tests:** `PENDING`
- **Full Regression:** `PENDING`
- **Bounded Simulation:** `PENDING`
- **Evidence:** `PENDING / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `PENDING`
- **Independent Activation Review:** `PENDING`
- **Independent Closure Review:** `PENDING`
- **Freeze:** `PENDING`
- **Publication:** `PENDING / USER-OWNED`

## Locked Final Design

- One new concept: vendor-neutral Flywheel closed-loop velocity control through one validated semantic RPM request.
- `requestSpin()` is `REMOVED / SUPERSEDED`.
- The exact request is `void requestVelocity(double targetRpm)` using Flywheel mechanism RPM.
- Valid targets are finite and nonnegative; zero is the canonical safe-stop request.
- Invalid requests fail closed to zero / `STOPPED`, attempt IO stop, then throw `IllegalArgumentException` with any stop failure suppressed.
- Requested states are exactly `STOPPED` and `VELOCITY_REQUESTED`.
- Observation remains measurement-only with `available`, `connected`, `velocityValid`, `velocityRpm`, and `requestedState`.
- Runtime remains `FlywheelIONoop` only; no physical adapter or hardware configuration is authorized.
- `FlywheelTelemetryFacade`, `RobotTelemetry`, `RobotContainer`, and `Constants.java` remain unchanged.
- M00_L09 readiness and all later command/coordination/autonomous scope remain protected.

## Future implementation boundary

Only these production files may later be modified:

- `src/main/java/frc/robot/io/flywheel/FlywheelIO.java`
- `src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`
- `src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`
- `src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`

Only the six inherited Flywheel-focused tests may later be modified. No new
test file and no unrelated inherited test change is authorized.

## Current gate

```text
LESSON: M00_L08 - Flywheel Closed-Loop Velocity
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L08
IMPLEMENTATION: PENDING SEPARATE AUTHORIZATION
FOCUSED TESTS: PENDING
CLEAN REGRESSION: PENDING
BOUNDED SIMULATION: PENDING
REAL HARDWARE: DEFERRED
CONTROLLED ACTIVATION: COMPLETE
M00_L09: INACTIVE / NOT CREATED
```

## Documentation repair clarification

The future FlywheelIO contract is exact: `FlywheelIOInputs` contains only
`boolean available`, `boolean connected`, `boolean velocityValid`, and
`double velocityRpm`; the public methods are exactly
`void updateInputs(FlywheelIOInputs inputs)`,
`void requestVelocity(double targetRpm)`, and `void stop()`. No vendor type,
vendor control object, gain parameter, or hardware-configuration parameter is
allowed. `requestSpin()` is `REMOVED / SUPERSEDED`.

Positive valid request ordering is: validate; record
`requestedVelocityRpm`; set `requestedState = VELOCITY_REQUESTED`; replace the
immutable Observation; forward exactly one `flywheelIO.requestVelocity(targetRpm)`.
If forwarding throws, target/state/Observation remain recorded, the exception
propagates, no rollback occurs, and `periodic()` does not retry. For `0.0`,
record zero, set `STOPPED`, replace Observation, and forward exactly one stop.

Invalid values are NaN, positive infinity, negative infinity, and negative
finite values. Invalid handling records zero/`STOPPED`, replaces Observation,
attempts stop, then throws `IllegalArgumentException`; no invalid target is
forwarded. A stop failure is suppressed on that primary exception, while
zero/STOPPED software intent remains recorded. Explicit stop is unconditional;
if it throws, zero/STOPPED/Observation remain and the exception propagates.
There is no rollback, automatic restart, or periodic output reissue.

`FlywheelIONoop.updateInputs()` sets exactly
`available=false`, `connected=false`, `velocityValid=false`, and
`velocityRpm=0.0`; request and stop are safe deterministic no-ops with no
convergence or physical model. Zero RPM while invalid is not measured physical
zero RPM.

`RobotContainer` remains unchanged and composes exactly
`new FlywheelSubsystem(new FlywheelIONoop())`. It must not add a Flywheel
command, controller binding, default command, direct request/stop, autonomous
registration, NamedCommands, event markers, Feeder/Flywheel coordination,
physical hardware selection, or a Flywheel-specific `RobotBase.isReal()`
branch.

Future test reconciliation may modify only these six existing files:

1. `src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`
2. `src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`
3. `src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`
4. `src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`
5. `src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`
6. `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`

No new test file or unrelated inherited test change is authorized.

## Post-verification reconciliation — 2026-09-20

This append-only record consumes accepted implementation and User evidence; it
does not advance the lifecycle beyond the current active lesson.

```text
LESSON: M00_L08 - Flywheel Closed-Loop Velocity
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L08
IMPLEMENTATION: COMPLETE
INDEPENDENT STATIC RE-REVIEW: PASS
FOCUSED TESTS: PASS
CLEAN REGRESSION: PASS
BOUNDED SIMULATION: PASS
REAL HARDWARE: DEFERRED
INDEPENDENT CLOSURE REVIEW: PENDING
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L09: INACTIVE / NOT CREATED
```

Final contract reconciliation: `void requestVelocity(double targetRpm)` is
the sole semantic request and `requestSpin()` is `REMOVED / SUPERSEDED`.
`FlywheelIOInputs` is exactly `available`, `connected`, `velocityValid`, and
`velocityRpm`; the exact methods are `updateInputs(...)`,
`requestVelocity(...)`, and `stop()`. Finite nonnegative RPM is valid; positive
finite requests forward once; `+0.0`/`-0.0` stop safely; NaN, infinities, and
negative finite values fail closed to zero/`STOPPED`, update Observation,
attempt stop, throw `IllegalArgumentException`, and suppress stop failure.
Stop records zero/`STOPPED` before unconditional IO stop and never rolls back
or restarts. Observation remains measurement-only and `periodic()` only
refreshes it. Noop values are false/false/false/0.0 with deterministic no-op
request/stop behavior. CAN 50–54 and real hardware remain deferred.

The initial static review was `HOLD` for stale stop target state, weak Noop
assertions, missing negative-zero coverage, and brittle comment stripping; the
bounded repair completed and final Independent Static Re-review is `PASS`
under `PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.

Accepted User evidence is `BUILD SUCCESSFUL in 16s`; `4 actionable tasks: 3
executed, 1 up-to-date`; `FOCUSED TESTS: PASS` under
`PASS_M00_L08_USER_FOCUSED_TESTS`, and `BUILD SUCCESSFUL in 26s`; `7
actionable tasks: 7 executed`; `CLEAN REGRESSION: PASS` under
`PASS_M00_L08_CLEAN_FULL_REGRESSION`.

Accepted bounded Simulation is `PASS_M00_L08_BOUNDED_SIMULATION`:

- Disabled: Available=false, Connected=false, RequestedState=STOPPED,
  VelocityRpm=0.0, VelocityValid=false.
- Teleoperated enabled/no driver action: Robot Enabled=Yes and the same values.
- Return Disabled: FMS Robot Enabled=No and the same values.

`VelocityRpm=0.0` while invalid is canonical Noop representation, not
physical measured zero. Simulation claims only Noop composition,
telemetry/state presence, deterministic invalid measurement, STOPPED idle,
no automatic Teleop request, and mode persistence. It does not claim physical
regulation, convergence, gains, sensor fidelity, RPM accuracy, CAN, physical
stop behavior, or **requestVelocity runtime exercise was NOT claimed**. Evidence is exactly
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
Lifecycle state: IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK.

Production comparison remains `103 / 99 / 4 / 0 / 0` and test comparison
remains `96 / 90 / 6 / 0 / 0` for Compared / Byte-identical / Changed /
Missing / Added. Protected configuration and M00_L07 predecessor integrity
remain PASS/unchanged. M00_L09 and later readiness, command, shooting,
coordination, and autonomous mechanism scope remain protected.

## Controlled freeze transition — 2026-09-20

The accepted `PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW` verdict is
`READY_FOR_FREEZE_AUTHORIZATION`, and Architect freeze authorization is
recorded. This lifecycle transition changes no source, tests, configuration,
deployment content, or verification evidence.

```text
LESSON: M00_L08 - Flywheel Closed-Loop Velocity
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
INDEPENDENT STATIC RE-REVIEW: PASS
FOCUSED TESTS: PASS
CLEAN REGRESSION: PASS
BOUNDED SIMULATION: PASS
INDEPENDENT CLOSURE REVIEW: PASS
FREEZE: COMPLETE / FROZEN / READ-ONLY
PUBLICATION: PENDING / NOT YET PUBLISHED
FINAL PUBLICATION VERIFICATION: NOT YET PERFORMED
REAL HARDWARE: DEFERRED
M00_L09: INACTIVE / NOT CREATED
```

The exact one-concept Flywheel contract, IO and validation semantics,
measurement-only Observation, output-free `periodic()`, deterministic Noop,
read-only telemetry, and protected later scope remain unchanged. Evidence
classification remains exactly `THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED`; no physical Flywheel or runtime `requestVelocity`
exercise is claimed.
