# M00_L10 to M00_L11 — Controlled Activation and Final Design Lock

This guide is the M00_L11 activation record. It records the accepted
predecessor, preparation, inheritance audit, Final Design Lock, and controlled
activation. It does not record implementation, testing, Simulation, closure,
freeze, or publication results.

## Step 1 — Accept the frozen predecessor

**Objective:** Establish the authoritative M00_L10 starting point.

**Why:** A lesson is copied only from the frozen predecessor.

**Action:** Accepted M00_L10 as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED`, primary SHA `531bceabddf53f194b1edaabbd972ee6865e9ff0`, metadata SHA
`cb8c125da0bbc7e2bf0aeebe40163c1a72909445`, and final gate
`PASS_M00_L10_FINAL_PUBLICATION_VERIFICATION`.

**Files Changed:** Documentation records only.

**Verification:** M00_L10 remains frozen and untouched. **PASS**

**Expected Result:** M00_L11 has one authoritative predecessor. **PASS**

## Step 2 — Copy and clean the prepared lesson

**Objective:** Record the preparation lineage.

**Why:** The prepared lesson must inherit the predecessor before activation.

**Action:** Recorded the User copy/rename from M00_L10, generated-artifact
cleanup, and untouched-copy baseline build gate
`PASS_M00_L11_PREPARATION_BASELINE`.

**Files Changed:** Documentation records only.

**Verification:** The prepared M00_L11 tree was independently compared with
M00_L10 after excluding `build/`, `.gradle/`, and `bin/`. **PASS**

**Expected Result:** The prepared lesson is an untouched inheritance snapshot.
**PASS**

## Step 3 — Record inheritance evidence

**Objective:** Preserve exact inheritance counts.

**Action:** Recorded the accepted comparison:

- Production: `108 / 108 / 0 / 0 / 0`
- Tests: `102 / 102 / 0 / 0 / 0`
- Deploy/config/support: `24 / 24 / 0 / 0 / 0`
- Lesson-local documentation: `98 / 98 / 0 / 0 / 0`

The order is Compared / Byte-identical / Changed / Missing / Added. Unexpected
substantive drift is `NONE`.

**Files Changed:** Documentation records only.

**Verification:** No source, test, deploy/configuration, support, or lesson
documentation drift was found. **PASS**

**Expected Result:** M00_L11 begins with exact M00_L10 inheritance. **PASS**

## Step 4 — Accept the architecture and inheritance gate

**Objective:** Record the independent architecture review.

**Action:** Accepted `PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT` with verdict
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`.

**Files Changed:** Documentation records only.

**Verification:** The Frozen Backbone, package responsibilities, Elevator
contracts, Noop composition, and protected M00_L12/M00_L13 scope remain intact.
**PASS**

**Expected Result:** Final Design Lock may be recorded. **PASS**

## Step 5 — Record the one new concept

**Objective:** Define the exact M00_L11 concept.

**Action:** M00_L11 introduces vendor-neutral Elevator closed-loop position
request semantics in the logical position coordinate frame established by
M00_L10. It does not establish homing, trusted reference, physical zero,
travel limits, hardware integration, physical convergence, coordination, or
autonomous behavior.

**Files Changed:** Documentation records only.

**Verification:** The concept is one new mechanism contract. **PASS**

**Expected Result:** No scope is pulled forward from M00_L12, M00_L13, or later
coordination lessons. **PASS**

## Step 6 — Accept the Final Design Lock

**Objective:** Record the exact future implementation contract.

**Action:** Accepted `PASS_M00_L11_FINAL_DESIGN_LOCK` with the following locked
semantics.

### IO

`ElevatorIO` will contain exactly `updateInputs(ElevatorIOInputs)`,
`requestPositionMeters(double targetPositionMeters)`, and `stop()`. The request
is a vendor-neutral closed-loop position request in meters. Voltage, duty-cycle,
percent-output, PID/PIDF, feedforward, Motion Magic, homing, zeroing, and limit
APIs are excluded.

### Requested state

Create exactly one production file,
`src/main/java/frc/robot/observation/elevator/ElevatorRequestedState.java`,
with exactly `STOPPED` and `POSITION_REQUESTED`.

### Observation

`ElevatorObservation` will contain exactly the inherited five members plus
`requestedState`, `targetPositionMeters`, and `positionErrorMeters`.

When stopped, the target is canonical inactive placeholder `0.0`. Error is
`targetPositionMeters - positionMeters` only when the requested state is
`POSITION_REQUESTED`, the position is valid, and the position is referenced;
otherwise error is canonical inactive/invalid placeholder `0.0`.

### Inherited normalization

The M00_L10 normalization remains unchanged:

```text
available = inputs.available
connected = available && inputs.connected
finitePosition = Double.isFinite(inputs.positionMeters)
positionValid = connected && inputs.positionValid && finitePosition
positionReferenced = positionValid && inputs.positionReferenced
positionMeters = finitePosition ? inputs.positionMeters : 0.0
```

Finite negative measured positions remain allowed. No travel-range clamp is
introduced.

### Request validation and ordering

The target must be finite. A non-finite target throws `IllegalArgumentException`
with no state mutation or IO request. The normalized observation must be both
valid and referenced; otherwise the request throws `IllegalStateException` with
no state mutation or IO request. Any finite target, including a finite negative
target, is software-contract valid once those prerequisites hold.

For a valid request, record `POSITION_REQUESTED`, record the target, rebuild the
immutable observation, and forward exactly one IO request. IO exceptions
propagate unchanged; recorded software intent is not rolled back and the
request is not retried.

### Periodic

`periodic()` calls `updateInputs` exactly once, normalizes inputs, and rebuilds
the observation. It does not request, reissue, stop, home, clamp, or calculate
controller output.

### Stop

`stop()` records `STOPPED`, records target `0.0`, rebuilds the observation, and
calls IO `stop()` exactly once. Exceptions propagate unchanged. There is no
retry, automatic restart, gravity-hold claim, reference modification, or
measured-position modification.

### Noop and telemetry

`ElevatorIONoop` retains `false, false, false, false, 0.0`; the new request is
a deterministic safe no-op and does not simulate movement, convergence, sensor
position, or reference trust. Telemetry contains exactly the eight topics:
`Available`, `Connected`, `PositionValid`, `PositionReferenced`,
`PositionMeters`, `RequestedState`, `TargetPositionMeters`, and
`PositionErrorMeters`.

### Composition and scope protection

Runtime remains `new ElevatorSubsystem(new ElevatorIONoop())`. RobotContainer,
RobotTelemetry, Constants, commands, bindings, autonomous integration, real
adapters, and ElevatorIOSim remain unchanged. M00_L12 exclusively owns homing
and trusted-reference establishment. M00_L13 exclusively owns travel limits,
travel safety, and target clamping.

**Files Changed:** Documentation records only.

**Verification:** The Design Lock is recorded before implementation and does
not authorize implementation by itself. **PASS**

**Expected Result:** The exact implementation boundary is reviewable. **PASS**

## Step 7 — Record the production and test write boundaries

**Objective:** Prevent scope expansion during later implementation.

**Action:** Later production work may create exactly one file,
`ElevatorRequestedState.java`, and modify exactly five existing Elevator files:
`ElevatorIO.java`, `ElevatorIONoop.java`, `ElevatorObservation.java`,
`ElevatorSubsystem.java`, and `ElevatorTelemetryFacade.java`.

Later tests may modify exactly five existing focused tests: the Elevator
architecture, IO Noop, observation, subsystem, and telemetry tests.
`RobotContainerElevatorCompositionTest.java` and all other tests remain
unchanged. No new test file is authorized.

**Files Changed:** Documentation records only.

**Verification:** RobotContainer, RobotTelemetry, Constants, commands, adapters,
and unrelated files are outside the boundary. **PASS**

**Expected Result:** Implementation authorization remains separate. **PASS**

## Step 8 — Record bounded future verification

**Objective:** Preserve the evidence limits.

**Action:** M00_L11 remains Noop-only at runtime. Expected idle state is
`false / false / false / false / 0.0 / STOPPED / 0.0 / 0.0`. Focused tests may
exercise request semantics with doubles. Simulation may verify construction,
telemetry, safe idle state, and Disabled → Teleoperated/Enabled → Disabled
persistence. It may not claim physical convergence, real position control,
sensor behavior, trusted reference, or hardware safety.

Target evidence classification is exactly `THEORY VERIFIED`, `SIMULATION
VERIFIED`, and `REAL HARDWARE DEFERRED`.

**Files Changed:** Documentation records only.

**Verification:** No build, tests, Simulation, or hardware verification was
performed by this activation task. **PASS**

**Expected Result:** Future evidence remains bounded. **PASS**

## Step 9 — Controlled Activation

**Objective:** Make M00_L11 the single active lesson.

**Action:** Recorded the controlled activation lifecycle:

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

**Files Changed:** The authorized documentation/lifecycle paths only.

**Verification:** No future review result, implementation authorization,
implementation, test, Simulation, closure, freeze, or publication result is
fabricated. **PASS**

**Expected Result:** M00_L11 is ready for Independent Activation Review with
current evidence still unestablished and the eventual closure classification
explicitly retained as a target. **PASS**

## Step 10 — Stop at the activation boundary

**Objective:** Preserve the next human-owned gate.

**Action:** Left Independent Activation Review as `PENDING`. No Git, Gradle,
test, build, Simulation, hardware, closure, freeze, or publication operation
was performed.

**Files Changed:** Documentation records only.

**Verification:** Production, tests, deploy/config/support, M00_L10, M00_L12,
and unrelated working-tree material remain protected. **PASS**

**Expected Result:** The next action is Independent Activation Review. **PASS**

## Step 11 — Repair current evidence classification

**Objective:** Separate current M00_L11 evidence from the eventual closure
target.

**Why:** The prior activation record incorrectly presented
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED` as achieved
M00_L11 evidence while implementation, build/test, and Simulation evidence were
not yet established.

**Action:** Recorded
`HOLD_M00_L11_CONTROLLED_ACTIVATION_PREMATURE_EVIDENCE_CLASSIFICATION` and
repaired the activation records. Current implementation, build/test, and
Simulation evidence are now `NOT YET ESTABLISHED FOR M00_L11`; real hardware is
`DEFERRED`. The three-part classification remains explicitly labeled as the
planned target for eventual closure.

**Files Changed:** The bounded activation documentation records containing the
premature current-evidence claim.

**Verification:** No Design Lock decision, lifecycle state, source, test,
configuration, or verification result changed. Independent Activation Review
remains `PENDING`. **PASS**

**Expected Result:** Current and target evidence are unambiguous and ready for
Independent Activation Review re-review. **PASS**

## Post-activation implementation and verification reconciliation — 2026-09-22

This append-only section records the completed M00_L11 chronology. Earlier
activation-only steps remain historical and are not deleted or rewritten.

### Step 12 — Authorize and complete the bounded implementation

The accepted implementation authorization covered exactly the five existing
Elevator production files plus the new `ElevatorRequestedState.java`, and the
five authorized focused tests. The implementation added vendor-neutral
position-request semantics in meters, `STOPPED` and `POSITION_REQUESTED`, the
eight-field immutable Observation, finite-target validation, valid/reference
gating, signed target-minus-position error, no periodic reissue, and explicit
stop behavior. M00_L12 homing/reference establishment and M00_L13 travel-limit
safety remained outside scope. **PASS**

### Step 13 — Record the independent static review

The post-implementation static review passed. Frozen Backbone ownership,
RobotContainer composition, Noop-only runtime, Constants protection,
telemetry read-only behavior, real-adapter exclusion, ElevatorIOSim exclusion,
and the M00_L12/M00_L13 boundaries remained intact. **PASS**

### Step 14 — Preserve the first focused-test compile failure

The first User focused-test invocation stopped at `compileTestJava` because
`ElevatorArchitectureBoundaryTest.withoutCommentsAndLiterals(String)` had
private visibility while the same-package composition test invoked it. The
failure was classified as `TEST_IMPLEMENTATION_DEFECT` with
`NO_PRODUCTION_CAUSE`. The bounded repair removed only `private`, and the
independent compile-repair re-review passed. The failed gate remains historical
evidence. **PASS**

### Step 15 — Preserve and repair the runtime observation failure

The next executed focused run recorded 26 tests with 25 passing and one
failure in `ElevatorObservationTest.equalValuesProduceEqualObservations()`.
The accepted category breakdown for that same historical executed run was:

```text
Architecture: 6/6 PASS
Noop: 3/3 PASS
Observation: 3/4 PASS
Subsystem: 10/10 PASS
Telemetry: 3/3 PASS
Overall: 25/26 PASS
```
The third inequality fixture used `positionMeters = 1.25`,
`targetPositionMeters = 2.0`, and `positionErrorMeters = 0.5`, while the locked
invariant required `2.0 - 1.25 = 0.75`. Forensics classified this as a
`TEST_IMPLEMENTATION_DEFECT`; `ElevatorObservation` validation was
`AUTHORIZED_AND_CORRECT`. The bounded test-only repair changed only the third
fixture position to `1.50`, preserving target `2.0` and error `0.5`; therefore
`2.0 - 1.50 = 0.50`. The independent fixture repair re-review passed. **PASS**

### Step 16 — Preserve the normal post-repair focused-test invocation

After the observation equality-fixture repair and its independent fixture-repair
re-review passed, the User performed a normal focused-test invocation. The
observed result was:

```text
BUILD SUCCESSFUL
4 actionable tasks: 4 up-to-date
```

This normal invocation was explicitly **NOT ACCEPTED AS FRESH FOCUSED-TEST
EVIDENCE** because Gradle did not freshly execute the tasks after the repair.
The workflow therefore required the forced focused-test rerun with
`--rerun-tasks`. **PASS**
### Step 17 — Record the forced fresh focused-test PASS

The User reran the exact focused tests with `--rerun-tasks`.
`PASS_M00_L11_USER_FOCUSED_TESTS` is accepted with
`BUILD SUCCESSFUL in 18s`, four actionable tasks, all four executed. The
earlier 25/26 result remains preserved and is not rewritten. **PASS**

### Step 18 — Record the clean regression PASS

The User ran `gradlew clean build --rerun-tasks`.
`PASS_M00_L11_USER_CLEAN_REGRESSION` is accepted with
`BUILD SUCCESSFUL in 37s`, seven actionable tasks, all seven executed. **PASS**

### Step 19 — Record bounded Simulation evidence

Runtime remained `new ElevatorSubsystem(new ElevatorIONoop())`, and no
Elevator request or physical movement was claimed. The User verified:

1. Disabled idle: `false / false / false / false / 0.0 / STOPPED / 0.0 / 0.0`;
2. Teleoperated enabled with DS attached and all eight values unchanged;
3. Return to Disabled with all eight values unchanged.

The accepted gates are `PASS_M00_L11_SIMULATION_DISABLED_IDLE_TELEMETRY`,
`PASS_M00_L11_SIMULATION_TELEOP_ENABLED_PERSISTENCE`, and
`PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`. This proves only Noop
composition, telemetry, safe idle, and lifecycle persistence. It does not prove
motor actuation, convergence, encoder correctness, homing, gravity behavior,
travel limits, hardware communication, or mechanism safety. **PASS**

### Step 20 — Reconcile current lifecycle and evidence

M00_L11 is now `IN_PROGRESS / ACTIVE / IMPLEMENTATION COMPLETE` with focused
tests, clean regression, Simulation, and Documentation Reconciliation passed.
The evidence classification is exactly `THEORY VERIFIED / SIMULATION VERIFIED /
REAL HARDWARE DEFERRED`. Independent Closure Review remains `PENDING`; Freeze
and Publication are not authorized. **PASS**

## Step 21 — Reconcile closure review and controlled freeze

Documentation Reconciliation completed with all eight authorized documentation surfaces aligned. The independent closure review accepted gate `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` and verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`. The reconciled implementation, forced focused tests, clean regression, and bounded Simulation evidence remain unchanged. **PASS**

M00_L11 transitions to `COMPLETE / FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED`. Active Lesson Count is `0` and Current Active M00 Lesson is `NONE`. The exact evidence classification remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; real hardware remains deferred. **PASS**

Independent Freeze Review remains `PENDING`. Publication remains `NOT YET PUBLISHED / USER-OWNED`; no Git commit, push, remote verification, or publication claim is made. M00_L12 remains `INACTIVE / NOT CREATED`, M00_L13 remains future scope, and the M00 roadmap does not advance. **PASS**

The locked one-concept boundary, M00_L10 predecessor integrity, both historical test defects and bounded repairs, focused-test history, clean regression evidence, Simulation limits, and Final Design Lock remain preserved. Earlier active-state wording in this guide is historical chronology; this freeze section is the authoritative current lifecycle record. **PASS**



