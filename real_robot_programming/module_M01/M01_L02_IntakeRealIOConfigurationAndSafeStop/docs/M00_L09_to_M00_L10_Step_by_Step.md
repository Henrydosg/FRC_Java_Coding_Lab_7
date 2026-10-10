# M00_L09 → M00_L10 Controlled Activation — Step by Step

Date: 2026-09-21  
Lesson: `M00_L10 - Elevator Foundation and Position-Reference Semantics`  
State: `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`

## 1. Authority and predecessor gate

The repository governance reading order, Frozen Backbone, development rules,
lesson workflow, documentation workflow, repository map, current lesson
contract/objectives, and approved inheritance source were read before this
activation. The governance mirror validator passed with 12 mirrors and 12
matching source hashes, three checklist structures, two headerless structures,
and zero deterministic findings.

M00_L09 is the approved immediate predecessor and remains protected:

- State: `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.
- Primary SHA: `3c822a1e3956850c9d0ba9954c5b163d83b801b9`.
- Metadata SHA: `249100db23262430ce2557eaa5e67d70b7b0a79c`.
- Final verdict: `PUBLICATION_VERIFIED`.

The M00_L10 untouched-copy baseline and architecture/inheritance audit were
accepted before documentation writes. The non-generated comparison reported
320 files on each side: production 103/103 byte-identical, tests 96/96,
lesson-local documentation 97/97, and deploy/configuration/support 24/24;
all changed, missing, and added counts were zero.

## 2. Lifecycle activation

The controlled activation changes documentation and lifecycle identity only.
After activation the authoritative state is:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L10
IMPLEMENTATION: NOT STARTED
INDEPENDENT ACTIVATION REVIEW: PENDING
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L11: INACTIVE / NOT CREATED
```

No Java source, test, deploy/configuration, dependency, build output,
verification evidence, frozen predecessor, Git state, or M00 roadmap entry is
changed by this activation. The candidate remains editable only inside the
locked design and the exact future implementation/test boundaries below.

## 3. One new concept and coordinate semantics

M00_L10 introduces exactly one concept:

> Vendor-neutral Elevator position-observation and reference contract: An
> independently owned Elevator reports linear mechanism position in a defined
> software coordinate frame, together with explicit measurement-validity and
> reference-trust semantics, without performing position control, homing, or
> travel-limit enforcement.

The canonical field is `positionMeters`, in meters. `0.0 m` is the logical
Elevator reference origin. Positive values mean increasing extension/upward
logical travel; negative finite software positions are permitted. Physical
sign, inversion, sensor conversion, and mounting remain real-hardware work and
are deferred. `positionMeters == 0.0` alone never proves physical zero, homed,
referenced, trusted, or valid state.

The five semantic inputs are exactly:
`available`, `connected`, `positionValid`, `positionReferenced`, and
`positionMeters`.

## 4. Required observation normalization

Every rebuild applies this deterministic normalization, with no travel-range
check and no rejection merely because a finite value is negative:

```text
available = inputs.available
connected = available && inputs.connected
finitePosition = Double.isFinite(inputs.positionMeters)
positionValid = connected && inputs.positionValid && finitePosition
positionReferenced = positionValid && inputs.positionReferenced
positionMeters = finitePosition ? inputs.positionMeters : 0.0
```

Therefore unavailable cannot be connected; disconnected cannot be valid;
invalid cannot be referenced; NaN and both infinities become invalid,
unreferenced, and `0.0`; finite valid-but-unreferenced observations are
allowed; and finite negative values are preserved.

## 5. Locked future production contract

The implementation phase, when separately authorized, may create exactly these
five production files:

1. `src/main/java/frc/robot/io/elevator/ElevatorIO.java`
2. `src/main/java/frc/robot/io/elevator/ElevatorIONoop.java`
3. `src/main/java/frc/robot/observation/elevator/ElevatorObservation.java`
4. `src/main/java/frc/robot/subsystems/ElevatorSubsystem.java`
5. `src/main/java/frc/robot/telemetry/elevator/ElevatorTelemetryFacade.java`

It may modify exactly `RobotContainer.java` and `RobotTelemetry.java` for
composition and read-only telemetry. `Constants.java` and every other
production file remain unchanged.

`ElevatorIO` contains nested `ElevatorIOInputs` with only the five fields
above, `void updateInputs(ElevatorIOInputs inputs)`, and `void stop()`. It has
no request, target, voltage, percent-output, PID/PIDF, Motion Magic, homing,
zeroing, or limit method. `ElevatorIONoop` writes
`false, false, false, false, 0.0` and has a safe no-op `stop()`; no IOSim,
CTRE, REV, or vendor adapter is authorized.

`ElevatorObservation` is immutable and contains only the five semantic
components. `ElevatorSubsystem(ElevatorIO)` owns the mutable input snapshot and
current immutable Observation. `periodic()` updates inputs once and rebuilds
the normalized Observation once, with no output. `stop()` calls exactly one
`io.stop()` and does not clear, home, zero, reference, retry, or hold state;
exceptions propagate unchanged. No mutable fake reference state exists.

There is no `ElevatorRequestedState` or STOPPED enum in M00_L10. M00_L11 owns
requested-position and closed-loop control semantics. Telemetry is exactly five
read-only fields/topics: `Available`, `Connected`, `PositionValid`,
`PositionReferenced`, and `PositionMeters`.

`RobotContainer` composes `new ElevatorSubsystem(new ElevatorIONoop())` while
leaving existing Swerve, Intake, Feeder, and Flywheel composition unchanged.
No Elevator command, default command, binding, autonomous registration,
NamedCommands, event marker, or real-hardware selection is included.

“Safe stop” means only that actuator demand is removed at the IO boundary. It
does not claim load holding, braking, or no-fall behavior.

## 6. Locked future tests and evidence boundary

The future activation may create exactly these six tests, without modifying
inherited tests:

- `ElevatorArchitectureBoundaryTest.java`
- `io/elevator/ElevatorIONoopTest.java`
- `observation/elevator/ElevatorObservationTest.java`
- `subsystems/ElevatorSubsystemTest.java`
- `telemetry/elevator/ElevatorTelemetryFacadeTest.java`
- `RobotContainerElevatorCompositionTest.java`

The matrix covers no vendor dependency; exact Noop values; zero invalid and
unreferenced; positive/negative finite preservation; valid-but-unreferenced;
reference prerequisites; normalization of unavailable/disconnected/invalid
and non-finite input; one input refresh and one rebuild per periodic call; no
periodic output; exactly one stop with no reference semantics; five read-only
telemetry fields; composition; and absence of requests, control, homing, or
limits.

Runtime remains Noop-only. A later bounded simulation may prove deterministic
false/false/false/false/0.0 telemetry and disabled→teleop→disabled persistence,
but cannot claim physical zero/reference, direction, conversion, sensor,
gear/gravity/load, CAN, homing, limits, or closed-loop behavior. Evidence is
planned as `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
Hardware remains unknown/deferred; CAN 50–54 is a planning reservation only.

## 7. Protected successor scope and completion gates

M00_L11 owns closed-loop position control, M00_L12 owns homing/trusted
reference acquisition, and M00_L13 owns travel limits. M00_L14–M00_L16 and the
M00_L01–M00_L16 roadmap remain unchanged. This activation does not start
implementation, testing, build, Simulation, Independent Activation Review,
closure, freeze, publication, or M00_L11.

## 8. Bounded Activation Documentation Repair — 2026-09-21

The accepted M00_L09 canonical predecessor is:

```text
COMPLETE
FROZEN
READ-ONLY
PUBLISHED
VERIFIED
PRIMARY SHA: 3c822a1e3956850c9d0ba9954c5b163d83b801b9
METADATA SHA: 249100db23262430ce2557eaa5e67d70b7b0a79c
FINAL PUBLICATION VERDICT: PUBLICATION_VERIFIED
```

User copied `M00_L09_FlywheelReadyAtSpeed` to
`M00_L10_ElevatorFoundationAndPositionReferenceSemantics`. Copied build
artifacts were removed, including `build` and `.gradle`.

The untouched-copy preparation baseline was:

```text
BUILD SUCCESSFUL in 53s
7 actionable tasks:
6 executed
1 up-to-date
GATE: PASS_M00_L10_PREPARATION_BASELINE
```

The Architecture / Inheritance Audit was
`PASS_M00_L10_ARCHITECTURE_INHERITANCE_AUDIT` with no unexpected drift:

```text
Production: 103 / 103 / 0 / 0 / 0
Tests: 96 / 96 / 0 / 0 / 0
Deploy/config/support: 24 / 24 / 0 / 0 / 0
Lesson-local documentation: 97 / 97 / 0 / 0 / 0
Unexpected drift: NONE
```

The accepted Final Design Lock is `PASS_M00_L10_FINAL_DESIGN_LOCK`. Controlled
Activation is `PASS_M00_L10_CONTROLLED_ACTIVATION`. Independent Activation
Review returned `HOLD`, under architect gate
`HOLD_M00_L10_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_INCOMPLETENESS`.
This bounded documentation repair addresses that HOLD only.

### Explicit semantic definitions

- `available`: the Elevator IO source is currently capable of providing
  Elevator mechanism information.
- `connected`: the relevant Elevator device/data path is currently connected.
- `positionValid`: `positionMeters` is currently a finite
  mechanism-position measurement valid under the declared meters/sign software
  contract.
- `positionReferenced`: the measurement origin is currently trusted against
  the logical Elevator reference origin.
- `positionMeters`: the vendor-neutral linear Elevator mechanism position in
  meters in the declared logical software coordinate frame.

`positionValid == true` does not require `positionReferenced == true`; a
finite valid measurement may remain unreferenced. `positionReferenced == true`
requires all of `available == true`, `connected == true`,
`positionValid == true`, and `Double.isFinite(positionMeters)`. M00_L10 does
not establish reference; M00_L12 owns reference establishment through homing.

The locked normalization remains exactly:

```text
available = inputs.available
connected = available && inputs.connected
finitePosition = Double.isFinite(inputs.positionMeters)
positionValid = connected && inputs.positionValid && finitePosition
positionReferenced = positionValid && inputs.positionReferenced
positionMeters = finitePosition ? inputs.positionMeters : 0.0
```

Unavailable cannot expose connected; disconnected cannot expose valid;
invalid cannot expose referenced; NaN and either infinity canonicalize to
invalid/unreferenced; finite valid-but-unreferenced and finite negative
positions are allowed; and M00_L10 performs no travel-range enforcement.

### Exact IO and Noop repair

The only ElevatorIO methods are
`void updateInputs(ElevatorIOInputs inputs)` and `void stop()`. M00_L10
ElevatorIO must not contain `requestPosition`, `setPosition`, `setVoltage`,
`setPercent`, `setDutyCycle`, PID, PIDF, feedforward, Motion Magic, homing,
zeroing, or limit methods. It also has no open-loop motor command API,
closed-loop position request API, or target-position API.

`ElevatorIONoop.updateInputs()` writes exactly:

```text
available = false
connected = false
positionValid = false
positionReferenced = false
positionMeters = 0.0
```

`positionMeters = 0.0` is the **CANONICAL INVALID PLACEHOLDER**. It does not
prove physical Elevator zero, valid measured zero, that homing occurred, that
the reference is trusted, that a real sensor measured zero, or that the
Elevator is physically located at the logical origin. `ElevatorIONoop.stop()`
is a safe no-op. No `ElevatorIOSim`, `ElevatorIOCTRE`, `ElevatorIOREV`, or real
hardware adapter is authorized.

### Explicit valid-zero/unreferenced test case

The future test matrix includes this case, distinct from Noop:

```text
available = true
connected = true
positionValid = true
positionReferenced = false
positionMeters = 0.0
```

Expected Observation values are `positionMeters == 0.0`,
`positionValid == true`, and `positionReferenced == false`. The purpose is to
prove that numerical zero may be a valid measurement while the logical origin
remains untrusted. The matrix also retains Noop false/false/false/false/0.0,
positive and negative finite preservation, prerequisite enforcement,
unavailable/disconnected/invalid forcing rules, all NaN/infinity cases,
single periodic refresh/no output/rebuild, exactly one stop, five read-only
telemetry fields, Noop composition, and absence of command/control/homing/
limit APIs.

### Future-gate protection and current lifecycle

The following have **NOT** occurred: Final Independent Activation Re-review,
Implementation Authorization, Implementation, Independent Static Review,
Focused Tests, Clean Full Regression, Bounded Simulation, Documentation
Reconciliation, Independent Closure Review, Freeze Authorization, Controlled
Freeze, Independent Freeze Review, Publication, and M00_L11 creation or
activation. No future PASS result is fabricated.

M00_L10 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`;
implementation is `NOT STARTED`; Independent Activation Review is
`HOLD — RE-REVIEW REQUIRED`; Freeze and Publication are `NOT AUTHORIZED`;
Active Lesson Count is `1`; Current Active M00 Lesson is `M00_L10`; and M00_L11
is `INACTIVE / NOT CREATED`.

### Activation chronology gate reconciliation — 2026-09-21

Bounded Activation Documentation Repair: `COMPLETE`

Accepted gate: `PASS_M00_L10_ACTIVATION_DOCUMENTATION_REPAIR`

Repair verdict:
`ACTIVATION_DOCUMENTATION_REPAIR_COMPLETE_READY_FOR_INDEPENDENT_REREVIEW`

Final Independent Activation Re-review: `HOLD`

Architect gate:
`HOLD_M00_L10_FINAL_ACTIVATION_REREVIEW_TRANSITION_GATE_RECORD`

Reason: the transition guide itself was missing the explicit canonical accepted
repair-gate identity. This minimal chronology repair records that identity;
the final re-review has not yet passed.

Minimal Activation Chronology Repair: this task.

### Final transition chronology reconciliation — 2026-09-21

Minimal Activation Chronology Repair: `COMPLETE`

Accepted gate:
`PASS_M00_L10_MINIMAL_ACTIVATION_CHRONOLOGY_REPAIR`

Repair verdict:
`MINIMAL_ACTIVATION_CHRONOLOGY_REPAIR_COMPLETE_READY_FOR_FINAL_REREVIEW`

Latest Final Independent Activation Re-review: `HOLD`

Architect gate:
`HOLD_M00_L10_FINAL_ACTIVATION_REREVIEW_MINIMAL_REPAIR_GATE_NOT_RECORDED`

Reason: the transition guide had not yet explicitly recorded the already
accepted minimal chronology repair gate.

Final Transition Chronology Reconciliation: `CURRENT TASK`

This task records the accepted prior gate only. It does not record a PASS
result for the current reconciliation or for the final activation re-review.

## Final implementation, verification, and documentation reconciliation — 2026-09-22

The previous activation and chronology-repair sections remain preserved as
historical records. The current M00_L10 reconciliation consumes the accepted
implementation, final static review, focused-test repair and re-review, User
focused-test rerun, clean regression, and bounded Simulation evidence.

### Accepted technical gates

```text
PASS_M00_L10_IMPLEMENTATION_REPORT_ACCEPTED
PASS_M00_L10_FINAL_INDEPENDENT_STATIC_REREVIEW
PASS_M00_L10_FOCUSED_TEST_FAILURE_FORENSICS
PASS_M00_L10_FOCUSED_TEST_DEFECT_REPAIR
PASS_M00_L10_INDEPENDENT_FOCUSED_TEST_REPAIR_REREVIEW
PASS_M00_L10_USER_FOCUSED_TESTS
PASS_M00_L10_CLEAN_FULL_REGRESSION
PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION
```

### Focused-test failure and repair chronology

The first User focused invocation completed 23 tests with 22 passing and one
failure in
`ElevatorArchitectureBoundaryTest.elevatorProductionUsesNoRequestedMotionOrControlState()`.
Forensics identified `TEST_IMPLEMENTATION_DEFECT`: the architecture test
rejected the legitimate
`import edu.wpi.first.wpilibj2.command.SubsystemBase;` by treating the entire
`edu.wpi.first.wpilibj2.command` package as forbidden command behavior.
Production contained no M00_L11, M00_L12, or M00_L13 implementation.

The bounded repair changed only
`src/test/java/frc/robot/ElevatorArchitectureBoundaryTest.java`. Its import
rule permits exactly `SubsystemBase`, rejects other direct command imports and
wildcards, and rejects fully qualified command behavior while preserving the
existing binding, autonomous, control, limit, vendor, and dependency guards.
The independent repair re-review passed. The User then reran all six focused
M00_L10 test classes successfully under `PASS_M00_L10_USER_FOCUSED_TESTS`.

### Clean regression and bounded Simulation evidence

The User supplied `PASS_M00_L10_CLEAN_FULL_REGRESSION` with
`BUILD SUCCESSFUL in 27s`; seven actionable tasks were reported and all seven
executed.

Integrity against frozen M00_L09 remains production `103 / 101 / 2 / 0 / 5`,
tests `96 / 96 / 0 / 0 / 6`, and deploy/config/support `24 / 24 / 0 / 0 / 0`
for compared, byte-identical, changed, missing, and added files.

The User supplied `PASS_M00_L10_BOUNDED_SIMULATION_VERIFICATION` for:

1. Disabled: `Available=false`, `Connected=false`, `PositionValid=false`,
   `PositionReferenced=false`, `PositionMeters=0.0`.
2. Teleoperated/Enabled: Robot Enabled=Yes and the same values.
3. Final Disabled: Robot Enabled=No and the same values.

Elevator NetworkTables contains exactly `Available`, `Connected`,
`PositionValid`, `PositionReferenced`, and `PositionMeters`. No Elevator
`RequestedState` exists. `PositionMeters=0.0` is the canonical invalid
placeholder and does not establish physical zero, homing, trusted reference,
sensor calibration, or real mechanism position. No physical Elevator
simulation was introduced.

### Current lifecycle and future protection

M00_L10 remains:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L10
IMPLEMENTATION: COMPLETE
FOCUSED TESTS: PASS
CLEAN FULL REGRESSION: PASS
BOUNDED SIMULATION: PASS
DOCUMENTATION RECONCILIATION: COMPLETE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
NEXT GATE: INDEPENDENT CLOSURE REVIEW
FREEZE: NOT AUTHORIZED
PUBLICATION: NOT AUTHORIZED
M00_L11: INACTIVE / NOT CREATED
```

M00_L11 retains position requests, target/error semantics, and closed-loop
control. M00_L12 retains homing and trusted reference establishment. M00_L13
retains top/bottom and soft/hard travel-limit enforcement. Elevator hardware,
sensor, conversion, direction, limits, and CAN 50–54 remain unknown or
deferred. Evidence classification is exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, `REAL HARDWARE DEFERRED`.

## Controlled freeze transition — 2026-09-22

### Step 21 — Record Independent Closure Review

**Objective:** Record the accepted closure gate before freezing M00_L10.

**Why:** Controlled freeze requires an Independent Closure Review PASS.

**Action:** Recorded `PASS_M00_L10_INDEPENDENT_CLOSURE_REVIEW` with verdict
`CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_AUTHORIZATION`.

**Files Changed:** Authorized lesson-local lifecycle documentation only.

**Verification:** Closure review passed; no production, test, build, Simulation,
Git, or publication operation occurred. **PASS**

**Expected Result:** Architect freeze authorization may be consumed. **PASS**

### Step 22 — Record Architect Freeze Authorization

**Objective:** Record the authorization permitting the controlled freeze.

**Why:** Freeze authorization is distinct from the applied frozen state.

**Action:** Recorded `PASS_M00_L10_FREEZE_AUTHORIZATION`.

**Files Changed:** Authorized lesson-local lifecycle documentation only.

**Verification:** Freeze was authorized; publication was not authorized. **PASS**

**Expected Result:** Controlled freeze may proceed. **PASS**

### Step 23 — Apply controlled freeze lifecycle transition

**Objective:** Transition M00_L10 to `COMPLETE / FROZEN / READ-ONLY`.

**Why:** All pre-freeze gates passed and the lesson must no longer be editable.

**Action:** Recorded M00_L10 as `COMPLETE / FROZEN / READ-ONLY`, set Active
Lesson Count to `0`, and set Current Active M00 Lesson to `NONE`.

**Files Changed:** The five authorized lesson-local documentation paths only.

**Verification:** Evidence remains `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`; M00_L11 remains `INACTIVE / NOT CREATED`. **PASS**

**Expected Result:** M00_L10 is frozen without runtime or source changes. **PASS**

### Step 24 — Preserve post-freeze pending gates

**Objective:** Keep Independent Freeze Review and publication separate from freeze.

**Why:** Controlled freeze does not complete the later review or publication.

**Action:** Recorded Independent Freeze Review as `PENDING` and Publication as
`PENDING / NOT YET PUBLISHED`; no publication SHA or Git event was recorded.

**Files Changed:** Authorized lesson-local lifecycle documentation only.

**Verification:** Chronology stops before Independent Freeze Review result,
publication authorization, Git publication, metadata verification, and M00_L11
creation. **PASS**

**Expected Result:** Controlled Freeze is complete and ready for Independent
Freeze Review. **PASS**
