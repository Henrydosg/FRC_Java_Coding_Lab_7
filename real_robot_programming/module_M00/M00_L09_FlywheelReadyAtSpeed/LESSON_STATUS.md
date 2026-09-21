# LESSON_STATUS

## Identity and lifecycle

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson:** `M00_L09 - Flywheel Ready-at-Speed`
- **Directory:** `M00_L09_FlywheelReadyAtSpeed`
- **Previous Lesson:** `M00_L08 - Flywheel Closed-Loop Velocity`
- **Previous Lesson State:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE`
- **Active State:** `FROZEN / READ-ONLY`
- **Freeze State:** `COMPLETE`
- **Editable Boundary:** `READ-ONLY AFTER CONTROLLED FREEZE`
- **Active Lesson:** `NO`
- **Current Active M00 Lesson:** `NONE`
- **Active Lesson Count:** `0`

## Required status fields

- **Architecture Review:** `PASS / PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT`
- **Baseline Build:** `PASS / UNTOUCHED INHERITED COPY / PASS_M00_L09_PREPARATION_BASELINE`
- **Build:** `PASS / CLEAN FULL REGRESSION / BUILD SUCCESSFUL / 7 ACTIONABLE TASKS EXECUTED`
- **Simulation:** `PASS / PASS_M00_L09_BOUNDED_SIMULATION`
- **Driver Station / Glass:** `PASS / BOUNDED SIMULATION TELEMETRY OBSERVED`
- **Real Robot:** `REAL HARDWARE DEFERRED`
- **Transition Guide:** `PASS / FINALIZED THROUGH CONTROLLED FREEZE; INDEPENDENT FREEZE REVIEW PENDING`
- **Git Commit:** `PENDING / USER-OWNED`
- **Git Push:** `PENDING / USER-OWNED`
- **Known Issues:** `PHYSICAL FLYWHEEL HARDWARE AND CONFIGURATION UNKNOWN; CAN 50-54 IS PLANNING-ONLY`

## Accepted gates and current phase

- **Preparation / Baseline:** `PASS_M00_L09_PREPARATION_BASELINE`
- **Inheritance:** `PASS / 103 OF 103 PRODUCTION AND 96 OF 96 TEST FILES BYTE-IDENTICAL`
- **Architecture / Inheritance Audit:** `PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT`
- **Final Design Lock:** `PASS_M00_L09_FINAL_DESIGN_LOCK / READY_FOR_CONTROLLED_ACTIVATION`
- **Controlled Activation:** `COMPLETE / CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`
- **Implementation Authorization:** `GRANTED / EXACT BOUNDED PRODUCTION AND FOCUSED-TEST SCOPE`
- **Implementation:** `COMPLETE / PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED`
- **Initial Independent Static Review:** `HOLD / CACHED-INPUT, TELEMETRY, OBSERVATION, ARCHITECTURE, AND POLICY-DOCUMENTATION FINDINGS`
- **Bounded Static-Review Repair:** `PASS / FIVE AUTHORIZED REPAIR FILES ONLY`
- **Final Independent Static Re-review:** `PASS / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`
- **Focused Tests:** `PASS_M00_L09_USER_FOCUSED_TESTS / BUILD SUCCESSFUL in 22s / 4 ACTIONABLE TASKS (3 EXECUTED, 1 UP-TO-DATE)`
- **Full Regression:** `PASS_M00_L09_CLEAN_FULL_REGRESSION / BUILD SUCCESSFUL / 7 ACTIONABLE TASKS EXECUTED`
- **Bounded Simulation:** `PASS_M00_L09_BOUNDED_SIMULATION`
- **Evidence:** `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`
- **Documentation Reconciliation:** `COMPLETE`
- **Independent Activation Review:** `PASS_M00_L09_FINAL_INDEPENDENT_ACTIVATION_REREVIEW`
- **Independent Closure Review:** `PASS / PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`
- **Freeze:** `COMPLETE`
- **Independent Freeze Review:** `PENDING`
- **Publication:** `PENDING / NOT YET PUBLISHED / USER-OWNED`

## Locked Final Design

- One new concept: vendor-neutral instantaneous Flywheel Ready-at-Speed classification.
- Semantic question: whether the latest valid Flywheel velocity is within the approved tolerance of the current positive velocity intent.
- `FlywheelSubsystem` owns readiness through one private deterministic side-effect-free helper; no separate evaluator or mutable readiness state.
- `readyAtSpeed` is recomputed on every immutable Observation rebuild.
- `readyAtSpeed=true` iff requested state is `VELOCITY_REQUESTED`, target is finite and greater than zero, inputs are available/connected/velocity-valid, measured velocity and target are finite, and `Math.abs(measured-target) <= Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm`.
- The tolerance is exactly `50.0` mechanism RPM, a provisional software-policy acceptance tolerance; it is not hardware-tuned and is not real-robot validated.
- The Observation adds exactly `readyAtSpeed` to `available`, `connected`, `velocityValid`, `velocityRpm`, and `requestedState`; its constructor rejects readiness without valid connected input and velocity intent.
- STOPPED, zero or negative-zero intent, stale matching measurements, unavailable/disconnected/invalid/nonfinite data, and outside-tolerance values are false; no dwell, debounce, hysteresis, history, state machine, command, or automatic action exists.
- Runtime remains `FlywheelIONoop` only; IO, Noop, RobotContainer, commands, coordination, autonomous, and hardware configuration remain unchanged.

## Future implementation boundary

Only these production files may later be modified:

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`
- `src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`
- `src/main/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacade.java`

These remain unchanged: `FlywheelIO.java`, `FlywheelIONoop.java`,
`RobotTelemetry.java`, and `RobotContainer.java`. Only the four named existing
Flywheel-focused test files may later be modified; the two inherited IO and
composition tests remain unchanged. No new production or test file is authorized.

The four authorized focused test files are
`FlywheelArchitectureBoundaryTest.java`, `FlywheelObservationTest.java`,
`FlywheelSubsystemTest.java`, and `FlywheelTelemetryFacadeTest.java`.
`FlywheelIONoopTest.java` and `RobotContainerFlywheelCompositionTest.java`
remain unchanged.

## Current gate

```text
LESSON: M00_L09 - Flywheel Ready-at-Speed
STATUS: COMPLETE
ACTIVE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
INDEPENDENT STATIC REVIEW: PASS
FOCUSED TESTS: PASS
CLEAN REGRESSION: PASS
BOUNDED SIMULATION: PASS
REAL HARDWARE: DEFERRED
CONTROLLED ACTIVATION: COMPLETE / CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW
INDEPENDENT ACTIVATION REVIEW: PASS
DOCUMENTATION RECONCILIATION: COMPLETE
INDEPENDENT CLOSURE REVIEW: PASS
FREEZE: COMPLETE
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: PENDING / NOT YET PUBLISHED
M00_L10: INACTIVE / NOT CREATED
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

## Historical controlled activation record — 2026-09-21

The inherited M00_L08 reconciliation and freeze text below is historical
snapshot material. Later implementation and verification reconciliation
supersedes its pending wording. The current authoritative state is M00_L09
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, Active Lesson Count
`1`, Current Active M00 Lesson `M00_L09`, implementation `COMPLETE`, and
Independent Activation Review `PASS`. M00_L08 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; M00_L10 is
`INACTIVE / NOT CREATED`. Independent Closure Review remains `PENDING`, and
freeze/publication remain unauthorized.

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

## Activation documentation repair reconciliation — 2026-09-21

- Accepted Architect gate: `PASS_M00_L09_CONTROLLED_ACTIVATION`.
- Engineer verdict: `CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
- Independent Activation Review: `HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW`;
  repair complete, re-review required.
- M00_L08 canonical external state:
  `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.
- Primary: `5daecd970ff95fb906d6de9d5bc22a5cb094877d`; metadata:
  `a76dc33c2b485b4988e7058cbfed0fa3362cc560`, parent = primary; final
  remote-aligned HEAD = metadata SHA; verdict `PUBLICATION_VERIFIED`; no third
  publication commit required.
- Historical pending wording in frozen M00_L08 snapshots is retained under the
  passing two-commit Historical Snapshot Model and is not contradictory.
- Locked formula, forwarding-exception semantics, six-field telemetry
  contract, truth table, floating policy, automatic-action prohibition, test
  matrix, evidence plan, and full deferred hardware boundary are recorded in
  the M00_L09 README; no implementation or test was added.
- Current lifecycle remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL
  DESIGN LOCK`, implementation `NOT STARTED`, Active Lesson Count `1`, Current
  Active M00 Lesson `M00_L09`; M00_L10 is `INACTIVE / NOT CREATED`.

## Final implementation, verification, and documentation reconciliation — 2026-09-21

This is the latest current-state record; earlier pending entries remain
historical chronology. M00_L09 remains the sole active editable lesson.

```text
LESSON: M00_L09 - Flywheel Ready-at-Speed
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L09
IMPLEMENTATION: COMPLETE
INITIAL INDEPENDENT STATIC REVIEW: HOLD
BOUNDED STATIC-REVIEW REPAIR: PASS
FINAL INDEPENDENT STATIC RE-REVIEW: PASS
FOCUSED TESTS: PASS
CLEAN REGRESSION: PASS
BOUNDED SIMULATION: PASS
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
DOCUMENTATION RECONCILIATION: COMPLETE
INDEPENDENT CLOSURE REVIEW: PENDING
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L10: INACTIVE / NOT CREATED
```

Accepted gates are `PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED`,
`STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`,
`PASS_M00_L09_USER_FOCUSED_TESTS`,
`PASS_M00_L09_CLEAN_FULL_REGRESSION`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`, and
`PASS_M00_L09_BOUNDED_SIMULATION`.

User-focused evidence is `BUILD SUCCESSFUL in 22s` with four actionable tasks,
three executed and one up-to-date. Clean regression is `BUILD SUCCESSFUL` with
seven actionable tasks, all seven executed. The three bounded Simulation
checkpoints observed the canonical Noop state: unavailable, disconnected,
invalid velocity, `VelocityRpm=0.0`, `RequestedState=STOPPED`, and
`ReadyAtSpeed=false`. The Simulation claim is limited to Noop composition,
telemetry presence, fail-safe readiness, STOPPED idle behavior, no automatic
Teleop request, no readiness-triggered actuation, and Disabled → Teleop →
Disabled persistence. ReadyAtSpeed=true, runtime tolerance boundaries,
physical convergence, and real hardware are not claimed.

The exact production integrity is `103 / 99 / 4 / 0 / 0`, test integrity is
`96 / 92 / 4 / 0 / 0`, and deploy/configuration remains unchanged. The final
readiness formula, six-field Observation/telemetry contracts, IO/Noop/
RobotTelemetry/RobotContainer boundaries, automatic-action prohibition, and
M00_L10–M00_L16 roadmap remain unchanged. Documentation reconciliation is
complete; closure, freeze, publication, and successor activation remain
separate pending gates.

## Focused-test execution clarification — 2026-09-21

The User ran one focused Gradle test invocation selecting all six Flywheel-
focused classes:

1. `frc.robot.FlywheelArchitectureBoundaryTest`
2. `frc.robot.observation.flywheel.FlywheelObservationTest`
3. `frc.robot.subsystems.FlywheelSubsystemTest`
4. `frc.robot.telemetry.flywheel.FlywheelTelemetryFacadeTest`
5. `frc.robot.io.flywheel.FlywheelIONoopTest`
6. `frc.robot.RobotContainerFlywheelCompositionTest`

Result: `BUILD SUCCESSFUL in 22s`; 4 actionable tasks: 3 executed and 1
up-to-date. Gate: `PASS_M00_L09_USER_FOCUSED_TESTS`. Four test files were
modified; the two inherited protected tests were intentionally included as
regression checks while remaining byte-identical to frozen M00_L08. Gradle
task counts are not test-class counts, and six Gradle tasks are not claimed.
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
