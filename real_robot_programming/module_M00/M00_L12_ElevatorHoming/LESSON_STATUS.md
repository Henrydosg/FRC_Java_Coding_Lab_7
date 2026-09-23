# LESSON_STATUS — M00_L12 Elevator Homing

## Current identity and lifecycle

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson:** `M00_L12 - Elevator Homing`
- **Directory:** `M00_L12_ElevatorHoming`
- **Previous Lesson:** `M00_L11 - Elevator Closed-Loop Position`
- **Previous Lesson State:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE`
- **Lifecycle:** `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`
- **Active State:** `FROZEN / READ-ONLY / NOT PUBLISHED`
- **Freeze State:** `COMPLETE / INDEPENDENT FREEZE REVIEW PENDING`
- **Editable Boundary:** `READ-ONLY AFTER CONTROLLED FREEZE`
- **Active Lesson:** `NO`
- **Current Active M00 Lesson:** `NONE`
- **Active Lesson Count:** `0`

## Required status fields

- **Architecture Review:** `PASS_M00_L12_ARCHITECTURE_INHERITANCE_AUDIT`
- **Baseline Build:** `PASS_M00_L12_UNTOUCHED_COPY_BASELINE_BUILD / PREPARATION EVIDENCE ONLY / BUILD SUCCESSFUL in 27s / 7 ACTIONABLE TASKS (6 EXECUTED, 1 UP-TO-DATE)`
- **Build:** `PASS_M00_L12_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 24s / 5 ACTIONABLE TASKS (5 EXECUTED)`
- **Simulation:** `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION / THREE CHECKPOINTS / SOFTWARE-NOOP EVIDENCE`
- **Driver Station / Glass:** `USER INSPECTED SIMULATION TELEMETRY AT THE THREE ACCEPTED CHECKPOINTS; NO SEPARATE HARDWARE VERIFICATION CLAIMED`
- **Real Robot:** `DEFERRED`
- **Transition Guide:** `RECONCILED THROUGH FREEZE RECONCILIATION / M00_L11_TO_M00_L12_STEP_BY_STEP`
- **Git Commit:** `PENDING / USER-OWNED`
- **Git Push:** `PENDING / USER-OWNED`
- **Known Issues:** `HOMING HARDWARE MECHANISM, SENSOR, DIRECTION, SPEED, TIMEOUT, CALIBRATION, AND CAN DETAILS UNKNOWN / DEFERRED`

## Accepted gates and current evidence

- **Final Design Lock:** `PASS_M00_L12_FINAL_DESIGN_LOCK`
- **Controlled Activation:** `CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`
- **Activation duplicate-documentation repair:** `PASS_M00_L12_ACTIVATION_DUPLICATE_DOCUMENTATION_RECONCILIATION`
- **Activation re-review:** `ACTIVATION_REREVIEW_PASS_READY_FOR_IMPLEMENTATION_AUTHORIZATION`
- **Implementation Authorization:** `PASS_M00_L12_IMPLEMENTATION_AUTHORIZATION`
- **Implementation:** `COMPLETE / IMPLEMENTATION_COMPLETE_READY_FOR_INDEPENDENT_STATIC_REVIEW`
- **Final Independent Static Re-review:** `PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS`
- **Focused Tests:** `PASS_M00_L12_USER_FOCUSED_TESTS / FRESH --rerun-tasks / BUILD SUCCESSFUL in 35s / 4 ACTIONABLE TASKS (4 EXECUTED)`
- **Clean Regression:** `PASS_M00_L12_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 24s / 5 ACTIONABLE TASKS (5 EXECUTED)`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Independent Closure Review:** `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE / NO REMAINING LEGITIMATE FINDINGS`
- **Freeze Reconciliation:** `COMPLETE / FREEZE_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_FREEZE_REVIEW`
- **Independent Freeze Review:** `PENDING / NEXT GATE`
- **Freeze:** `COMPLETE / FROZEN / READ-ONLY`
- **Publication:** `NOT PUBLISHED / PENDING / USER-OWNED`
- **Publication SHA:** `NONE / NOT YET ESTABLISHED`

## Locked design boundary

M00_L12 introduces exactly one concept: a bounded, scheduler-managed Elevator homing lifecycle using vendor-neutral `requestHoming()` and recognizing reference only from normalized `positionReferenced`. Zero position is not proof of home. The five-field IO input contract, eight-field observation, normal position-reference guard, read-only telemetry, unchanged Constants and RobotContainer, and M00_L13 travel-limit firewall remain protected.

Production delta versus frozen M00_L11 is `109 / 105 / 4 / 0 / 1`; changed files are `ElevatorIO.java`, `ElevatorIONoop.java`, `ElevatorRequestedState.java`, and `ElevatorSubsystem.java`, with new `HomeElevatorCommand.java`. Test delta is `102 / 98 / 4 / 0 / 1`; changed files are `ElevatorArchitectureBoundaryTest.java`, `ElevatorIONoopTest.java`, `ElevatorObservationTest.java`, and `ElevatorSubsystemTest.java`, with new `HomeElevatorCommandTest.java`. Deploy/config/support delta is `24 / 24 / 0 / 0 / 0`. No real Elevator adapter or `ElevatorIOSim` was added.

## Evidence classification

M00_L12 evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Theory evidence includes the accepted Design Lock, implementation, final static review, focused tests, and clean regression. Simulation evidence is limited to bounded runtime safety, truthful `ElevatorIONoop` observations, no automatic homing, the Disabled → Teleop Enabled → Disabled lifecycle, and the software meaning that zero position is not home. It does not establish physical Elevator homing, motion, sensor activation, calibration, or reference accuracy. Unknown hardware mechanism, sensor, direction, speed, timeout, calibration, and CAN details remain deferred. The accepted Independent Closure Review passed; M00_L12 is now frozen and read-only. Independent Freeze Review is pending, and publication has not occurred.

## Historical copied predecessor documentation

The inherited documentation snapshot follows below for provenance only. Its old lesson identity and lifecycle are historical and are not the current M00_L12 status above.
# LESSON_STATUS

## Identity and lifecycle

- **Module:** `M00 - Competition Mechanism Foundations`
- **Lesson:** `M00_L10 - Elevator Foundation and Position-Reference Semantics`
- **Directory:** `M00_L10_ElevatorFoundationAndPositionReferenceSemantics`
- **Previous Lesson:** `M00_L09 - Flywheel Ready-at-Speed`
- **Previous Lesson State:** `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
- **Status:** `COMPLETE`
- **Active State:** `FROZEN / READ-ONLY`
- **Freeze State:** `COMPLETE`
- **Editable Boundary:** `READ-ONLY AFTER CONTROLLED FREEZE`
- **Active Lesson:** `NO`
- **Current Active M00 Lesson:** `NONE`
- **Active Lesson Count:** `0`

## Required status fields

- **Architecture Review:** `PASS / PASS_M00_L10_ARCHITECTURE_INHERITANCE_AUDIT`
- **Baseline Build:** `PASS / INHERITED BASELINE / PASS_M00_L10_PREPARATION_BASELINE`
- **Build:** `PASS / USER CLEAN FULL REGRESSION / BUILD SUCCESSFUL in 27s / 7 ACTIONABLE TASKS (7 EXECUTED)`
- **Simulation:** `PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION / USER VERIFIED`
- **Driver Station / Glass:** `NOT TESTED / NO SEPARATE GLASS EVIDENCE CLAIMED`
- **Real Robot:** `REAL HARDWARE DEFERRED`
- **Transition Guide:** `PASS / M00_L09_TO_M00_L10_STEP_BY_STEP / FINALIZED THROUGH CONTROLLED FREEZE; INDEPENDENT FREEZE REVIEW PENDING`
- **Git Commit:** `PENDING / USER-OWNED`
- **Git Push:** `PENDING / USER-OWNED`
- **Known Issues:** `ELEVATOR HARDWARE, SENSOR, CONVERSION, DIRECTION, LIMITS, AND CAN ASSIGNMENTS UNKNOWN / DEFERRED`

## Historical inherited M00_L09 gates and phase

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

## Historical inherited M00_L09 final design

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
LESSON: M00_L10 - Elevator Foundation and Position-Reference Semantics
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
CONTROLLED ACTIVATION: COMPLETE / PASS_M00_L10_CONTROLLED_ACTIVATION
INDEPENDENT ACTIVATION REVIEW: PASS / PASS_M00_L10_FINAL_INDEPENDENT_ACTIVATION_REREVIEW
DOCUMENTATION RECONCILIATION: COMPLETE
INDEPENDENT CLOSURE REVIEW: PASS / PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW
FREEZE: COMPLETE
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: PENDING / NOT YET PUBLISHED
M00_L11: INACTIVE / NOT CREATED
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

## M00_L10 controlled activation snapshot — 2026-09-21

The copied M00_L09 implementation and verification records above are retained
as historical snapshot chronology only. The activation-point state was:

```text
LESSON: M00_L10 - Elevator Foundation and Position-Reference Semantics
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L10
IMPLEMENTATION: NOT STARTED
INDEPENDENT ACTIVATION REVIEW: HOLD — RE-REVIEW REQUIRED
BUILD: NOT RUN / DOCUMENTATION-ONLY ACTIVATION
SIMULATION: NOT RUN / NO RUNTIME EVIDENCE CLAIMED
DOCUMENTATION RECONCILIATION: PENDING / NOT YET PERFORMED
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L11: INACTIVE / NOT CREATED
```

The accepted predecessor gates are `PASS_M00_L09_PREPARATION_BASELINE`,
`PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT`, and final
`PUBLICATION_VERIFIED` under primary SHA
`3c822a1e3956850c9d0ba9954c5b163d83b801b9` and metadata SHA
`249100db23262430ce2557eaa5e67d70b7b0a79c`.

The exact locked M00_L10 contract is maintained in
`docs/M00_L09_to_M00_L10_Step_by_Step.md`: one immutable, vendor-neutral
position observation with explicit validity and reference trust; meters as the
canonical unit; finite negative values permitted; invalid non-finite values
normalized to `0.0`; no control, homing, limits, or vendor semantics; Noop-only
runtime composition; exactly five read-only telemetry fields; and the bounded
future production/test file lists. No implementation, test, build, simulation,
freeze, publication, or Git operation is claimed by this activation.

## Bounded activation documentation repair snapshot — 2026-09-21

This section is historical activation-repair evidence. Its pending review and
documentation wording is superseded by the final reconciliation below.

The semantic definitions, validity/reference distinction, exact IO prohibition
terms, Noop canonical invalid placeholder wording, and valid-zero/unreferenced
test case are repaired in the current M00_L10 records and transition guide.
The review gate at that earlier point was `HOLD — RE-REVIEW REQUIRED` under
`HOLD_M00_L10_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_INCOMPLETENESS`.

## Final implementation, verification, and documentation reconciliation — 2026-09-22

The preceding pending wording is preserved as historical activation evidence.
The current M00_L10 state is:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L10
IMPLEMENTATION: COMPLETE / PASS_M00_L10_IMPLEMENTATION_REPORT_ACCEPTED
INDEPENDENT STATIC REVIEW: PASS / STATIC_REREVIEW_PASS_READY_FOR_USER_FOCUSED_TESTS
FOCUSED TESTS: PASS_M00_L10_USER_FOCUSED_TESTS / USER VERIFIED
CLEAN FULL REGRESSION: PASS_M00_L10_CLEAN_FULL_REGRESSION / BUILD SUCCESSFUL in 27s
BOUNDED SIMULATION: PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION / USER VERIFIED
DOCUMENTATION RECONCILIATION: COMPLETE / READY FOR INDEPENDENT CLOSURE REVIEW
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PENDING
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L11: INACTIVE / NOT CREATED
```

The focused-test chronology is reconciled without hiding the temporary
failure: 23 tests completed, 22 passed, and one architecture-boundary method
failed because a package-wide command substring rejected the legitimate
`SubsystemBase` import. Forensics classified this as
`TEST_IMPLEMENTATION_DEFECT`; production had no L11/L12/L13 violation. The
test-only repair changed only `ElevatorArchitectureBoundaryTest.java`, the
independent repair re-review passed, and the User reran all six focused test
classes successfully.

The User supplied clean regression evidence of `BUILD SUCCESSFUL in 27s` with
seven actionable tasks, all seven executed. The User supplied bounded
Simulation evidence for Disabled → Teleoperated/Enabled → Disabled, with
`Available=false`, `Connected=false`, `PositionValid=false`,
`PositionReferenced=false`, and `PositionMeters=0.0` throughout; Robot Enabled
was Yes during Teleoperated and No at final Disabled. The value `0.0` is the
canonical invalid placeholder, not physical zero or a trusted reference.

Integrity against frozen M00_L09 remains production `103 / 101 / 2 / 0 / 5`,
tests `96 / 96 / 0 / 0 / 6`, and deploy/config/support `24 / 24 / 0 / 0 / 0`
for compared, byte-identical, changed, missing, and added files.

The evidence classification remains exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`. Closure, freeze,
publication, and M00_L11 activation remain unstarted.

## Controlled freeze transition — 2026-09-22

The accepted closure gate is `PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW` with
verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_AUTHORIZATION`. Architect freeze
authorization is `PASS_M00_L10_FREEZE_AUTHORIZATION`.

The current M00_L10 state is:

```text
STATUS: COMPLETE
ACTIVE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
INDEPENDENT STATIC REVIEW: PASS
FOCUSED TESTS: PASS
CLEAN FULL REGRESSION: PASS
BOUNDED SIMULATION: PASS
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
DOCUMENTATION RECONCILIATION: COMPLETE
INDEPENDENT CLOSURE REVIEW: PASS
FREEZE AUTHORIZATION: PASS_M00_L10_FREEZE_AUTHORIZATION
FREEZE: COMPLETE
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: PENDING / NOT YET PUBLISHED
M00_L11: INACTIVE / NOT CREATED
```

No production, test, deploy/configuration, support, root governance, Git, or
publication operation occurred. No publication SHA or future Independent
Freeze Review result is recorded.

## Controlled Activation — 2026-09-22

The copied M00_L10 records above are historical snapshot material. The current
M00_L11 lifecycle is:

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

Inheritance is recorded as production `108 / 108 / 0 / 0 / 0`, tests
`102 / 102 / 0 / 0 / 0`, deploy/config/support `24 / 24 / 0 / 0 / 0`, and
lesson-local documentation `98 / 98 / 0 / 0 / 0` for Compared / Byte-identical /
Changed / Missing / Added. Unexpected substantive drift is `NONE`.

The locked concept is vendor-neutral Elevator closed-loop position request
semantics in meters. Future IO methods are exactly `updateInputs(...)`,
`requestPositionMeters(double)`, and `stop()`. Future requested states are
exactly `STOPPED` and `POSITION_REQUESTED`. Requests require finite target,
valid position, and trusted reference; finite negative targets remain valid and
no travel clamp exists. Valid requests record intent, rebuild the immutable
observation, and forward once. Invalid requests make no mutation and issue no
IO call. `periodic()` never reissues. Stop records stopped/target `0.0`,
rebuilds, and forwards once.

Noop remains deterministic and output-free. RobotContainer, RobotTelemetry,
Constants, commands, bindings, autonomous integration, real adapters, and
ElevatorIOSim remain unchanged. M00_L12 owns homing/reference establishment;
M00_L13 owns travel-limit safety. The target evidence classification for
eventual closure is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`; it is planned and is not current achieved evidence. No
implementation, tests, build, Simulation, closure, freeze, publication, or Git
result is claimed by this activation.

## Bounded activation evidence-classification repair — 2026-09-22

The earlier activation record was held under
`HOLD_M00_L11_CONTROLLED_ACTIVATION_PREMATURE_EVIDENCE_CLASSIFICATION`
because it presented the eventual closure classification as current achieved
evidence. The repaired current state is `NOT YET ESTABLISHED FOR M00_L11` for
implementation, build/test, and Simulation evidence, with real hardware
`DEFERRED`. The eventual `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED` classification is retained as a planned closure target. Independent
Activation Review remains `PENDING`.

## Current M00_L11 status after implementation and verification — 2026-09-22

This append-only section is authoritative for the current M00_L11 lifecycle;
earlier copied M00_L10 and activation snapshots remain historical.

- **Lesson:** `M00_L11 - Elevator Closed-Loop Position`
- **Previous Lesson:** `M00_L10 - Elevator Foundation and Position-Reference Semantics`
- **Status:** `IN_PROGRESS`
- **Active State:** `ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`
- **Active Lesson Count:** `1`
- **Current Active M00 Lesson:** `M00_L11`
- **Architecture Review:** `PASS / PASS_M00_L11_INDEPENDENT_STATIC_REVIEW`
- **Baseline Build:** `PASS / INHERITED M00_L10 BASELINE`
- **Implementation:** `COMPLETE / PASS_M00_L11_IMPLEMENTATION_REPORT_ACCEPTED`
- **Focused Tests:** `PASS / PASS_M00_L11_USER_FOCUSED_TESTS / BUILD SUCCESSFUL in 18s / 4 ACTIONABLE TASKS EXECUTED`
- **Clean Regression:** `PASS / PASS_M00_L11_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 37s / 7 ACTIONABLE TASKS EXECUTED`
- **Simulation:** `PASS / PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`
- **Driver Station / Glass:** `NOT TESTED / NO SEPARATE GLASS EVIDENCE CLAIMED`
- **Real Robot:** `REAL HARDWARE DEFERRED`
- **Transition Guide:** `PASS / M00_L10_TO_M00_L11_STEP_BY_STEP / RECONCILED`
- **Documentation Reconciliation:** `COMPLETE / DOCUMENTATION_RECONCILIATION_COMPLETE_READY_FOR_INDEPENDENT_CLOSURE_REVIEW`
- **Independent Closure Review:** `PENDING`
- **Freeze:** `NOT AUTHORIZED`
- **Publication:** `NOT AUTHORIZED / USER-OWNED`
- **Git Commit:** `PENDING / USER-OWNED`
- **Git Push:** `PENDING / USER-OWNED`

The two focused-test defects remain preserved as historical evidence: the
private helper visibility defect and the inconsistent equality fixture. Both
were test defects; no production defect was found. The final evidence
classification is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`. M00_L12 and M00_L13 remain protected future scopes.

## Authoritative M00_L11 controlled freeze — 2026-09-22

The preceding current-status section is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`. The reconciled implementation and all accepted User evidence remain unchanged.

- **Lesson:** `M00_L11 - Elevator Closed-Loop Position`
- **Previous Lesson:** `M00_L10 - Elevator Foundation and Position-Reference Semantics`
- **Status:** `COMPLETE`
- **Active State:** `FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED`
- **Active Lesson Count:** `0`
- **Current Active M00 Lesson:** `NONE`
- **Architecture Review:** `PASS / PASS_M00_L11_INDEPENDENT_STATIC_REVIEW`
- **Baseline Build:** `PASS / INHERITED M00_L10 BASELINE`
- **Implementation:** `COMPLETE / PASS_M00_L11_IMPLEMENTATION_REPORT_ACCEPTED`
- **Focused Tests:** `PASS / PASS_M00_L11_USER_FOCUSED_TESTS / BUILD SUCCESSFUL in 18s / 4 ACTIONABLE TASKS EXECUTED`
- **Clean Regression:** `PASS / PASS_M00_L11_USER_CLEAN_REGRESSION / BUILD SUCCESSFUL in 37s / 7 ACTIONABLE TASKS EXECUTED`
- **Simulation:** `PASS / PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`
- **Driver Station / Glass:** `NOT TESTED / NO SEPARATE GLASS EVIDENCE CLAIMED`
- **Real Robot:** `REAL HARDWARE DEFERRED`
- **Transition Guide:** `PASS / M00_L10_TO_M00_L11_STEP_BY_STEP / RECONCILED AND FROZEN`
- **Documentation Reconciliation:** `COMPLETE`
- **Independent Closure Review:** `PASS / PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`
- **Freeze:** `COMPLETE / READY FOR INDEPENDENT FREEZE REVIEW`
- **Publication:** `NOT YET PUBLISHED / USER-OWNED`
- **Git Commit:** `PENDING / USER-OWNED`
- **Git Push:** `PENDING / USER-OWNED`
- **M00_L12:** `INACTIVE / NOT CREATED`
- **M00_L13:** `FUTURE SCOPE / NOT ACTIVATED`

The exact evidence classification is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. The two focused-test defects remain historical test defects with no production causality. No publication, remote verification, or hardware verification is claimed. Earlier active-state text remains historical chronology.

The inherited M00_L11 documentation snapshot follows below for provenance only.
Its old lesson identity and lifecycle are historical and are not the current
M00_L12 status above.


The inherited documentation snapshot follows below for provenance only. Its old lesson identity and lifecycle are historical and are not the current M00_L12 status above.

