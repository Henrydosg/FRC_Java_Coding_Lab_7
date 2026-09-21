# M00_L07 to M00_L08 Step-by-Step Transition Guide

## Current scope

This guide records the controlled transition from the canonical published
M00_L07 Flywheel Foundation to the active M00_L08 Flywheel Closed-Loop
Velocity candidate. It stops before production implementation, test
implementation, and verification.

## Step 1 — Confirm the frozen predecessor

**Objective:** Establish the authorized inheritance source.

**Why:** Each lesson must inherit from the immediately previous completed and
published lesson.

**Action:** Confirmed M00_L07 as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED`.

**Files Changed:** None; predecessor inspection only.

**Verification:** Accepted predecessor publication evidence and lifecycle
record.

**Expected Result:** M00_L07 remains untouched and authoritative. **PASS**

## Step 2 — Copy the predecessor

**Objective:** Prepare M00_L08 through inheritance rather than recreation.

**Why:** The frozen development workflow requires copy-and-rename development.

**Action:** The User copied M00_L07 to
`M00_L08_FlywheelClosedLoopVelocity`.

**Files Changed:** New inherited candidate prepared by the User.

**Verification:** Candidate folder exists with the inherited source, tests,
configuration, deployment content, and lesson documentation.

**Expected Result:** One prepared M00_L08 candidate exists. **PASS**

## Step 3 — Remove copied generated artifacts

**Objective:** Keep generated build outputs outside governed lesson content.

**Why:** Build caches and outputs are derived artifacts, not inheritance
evidence.

**Action:** The User removed copied generated artifacts before the baseline.

**Files Changed:** Candidate generated-artifact state only.

**Verification:** The accepted preparation record identifies the candidate as an
untouched inherited copy.

**Expected Result:** Governed source and documentation remain inherited. **PASS**

## Step 4 — Run the untouched-copy baseline

**Objective:** Prove the inherited candidate is a usable baseline before design
activation.

**Why:** Implementation cannot begin from an unverified copy.

**Action:** The User ran the untouched-copy baseline build.

**Files Changed:** None by lesson design; generated build outputs are not
governed comparison content.

**Verification:** `PASS_M00_L08_PREPARATION_BASELINE`.

**Expected Result:** Baseline is accepted before activation. **PASS**

## Step 5 — Confirm inheritance integrity

**Objective:** Confirm production and test inheritance is exact.

**Why:** M00_L08 must begin as a faithful M00_L07 snapshot.

**Action:** Non-Git comparison established 103/103 production files and 96/96
test files byte-identical, with zero changed, missing, or added files; the
overall non-generated comparison was 711/711 identical.

**Files Changed:** None.

**Verification:** `PASS_M00_L08_ARCHITECTURE_INHERITANCE_AUDIT`.

**Expected Result:** Candidate is a valid untouched inheritance baseline. **PASS**

## Step 6 — Accept the Final Design Lock

**Objective:** Lock the single M00_L08 concept and exact future implementation
boundary before activation.

**Why:** Activation must not leave implementation semantics to guesswork.

**Action:** Accepted `PASS_M00_L08_FINAL_DESIGN_LOCK` for vendor-neutral
closed-loop velocity control through `void requestVelocity(double targetRpm)`.
`requestSpin()` is superseded; valid targets are finite and nonnegative RPM;
zero is canonical safe stop; invalid input fails closed; requested states are
`STOPPED` and `VELOCITY_REQUESTED`; Observation remains measurement-only; and
`FlywheelIONoop` remains the only runtime adapter.

**Files Changed:** None; design decision only.

**Verification:** Accepted Final Design Lock record.

**Expected Result:** One bounded design is ready for lifecycle activation. **PASS**

## Step 7 — Preserve the Architect Simulation clarification

**Objective:** Separate unit/test-double evidence from WPILib runtime Simulation
evidence.

**Why:** Test doubles must not be reported as runtime physical behavior.

**Action:** Recorded that bounded WPILib Simulation may later verify only Noop
composition, deterministic measurement/telemetry state, STOPPED idle behavior,
no automatic Teleop request, and Disabled → Teleop → Disabled persistence. It
must not claim requestVelocity runtime exercise, physical regulation,
convergence, tuning, sensor fidelity, RPM accuracy, CAN, or physical stop
behavior.

**Files Changed:** Activation documentation only.

**Verification:** Clarification is present in the active lesson records.

**Expected Result:** Evidence boundaries remain explicit. **PASS**

## Step 8 — Controlled Activation

**Objective:** Establish M00_L08 as the sole active editable lesson.

**Why:** The accepted design must be active before separately authorized
implementation.

**Action:** Updated the authorized root governance records, M00 roadmap ADR,
and M00_L08 lesson records; created this transition guide.

**Files Changed:**

- `AGENTS.md`
- `README.md`
- `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/README.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_STATUS.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_PLAN.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_CHECKLIST.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/docs/M00_L07_to_M00_L08_Step_by_Step.md`

**Verification:** Lifecycle records state `IN_PROGRESS / ACTIVE / EDITABLE
WITHIN FINAL DESIGN LOCK`, active lesson count `1`, and current active lesson
`M00_L08`.

**Expected Result:** M00_L08 is activated without production or test changes.
**PASS**

## Step 9 — Stop before implementation

**Objective:** Preserve the exact authorization boundary after activation.

**Why:** Controlled Activation does not authorize production or test coding.

**Action:** Stop after documentation/lifecycle activation. Future production
changes are limited to the four locked Flywheel files; future test changes are
limited to the six named inherited Flywheel-focused tests. No new class or test
file is authorized.

**Files Changed:** None beyond the activation documentation listed above.

**Verification:** Implementation, focused tests, regression, Simulation,
closure, freeze, publication, and Git evidence remain pending.

**Expected Result:** M00_L08 remains ready for Independent Activation Review.
**PASS**

## Current lifecycle state

```text
LESSON: M00_L08 - Flywheel Closed-Loop Velocity
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L08
IMPLEMENTATION: PENDING SEPARATE AUTHORIZATION
VERIFICATION: PENDING
REAL HARDWARE: DEFERRED
M00_L07: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L09: INACTIVE / NOT CREATED
```

## Step 10 — Repair activation documentation completeness

**Objective:** Resolve the Independent Activation Review documentation HOLD
without changing implementation authorization or lifecycle state.

**Why:** The locked design requires exact IO, ordering, exception, Noop,
composition, and test-boundary wording before independent rereview.

**Action:** Added explicit future `FlywheelIOInputs` fields and methods,
`requestSpin()` supersession, valid/invalid/zero request ordering and exception
semantics, unconditional safe stop, exact `FlywheelIONoop` values and no-op
behavior, exact `RobotContainer` composition/prohibitions, and all six
authorized test paths to the authorized activation records.

**Files Changed:** Authorized activation documentation only:

- `AGENTS.md`
- `README.md`
- `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/README.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_STATUS.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_PLAN.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_CHECKLIST.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/docs/M00_L07_to_M00_L08_Step_by_Step.md`

**Verification:** No production source, tests, configuration, or lifecycle
state were changed. Implementation and verification remain pending.

**Expected Result:** Documentation is complete and ready for independent
activation rereview. **PASS**

## Step 11 — Reconcile implementation, verification, and lifecycle evidence

**Objective:** Record the completed implementation and all accepted
verification evidence while stopping before Independent Closure Review,
freeze, publication, or M00_L09 activation.

**Why:** The transition guide must preserve the complete chronological
lineage from the frozen/published M00_L07 predecessor through the active
M00_L08 lesson without fabricating later lifecycle gates.

**Action:** The User-prepared M00_L07 copy was renamed to M00_L08, generated
artifacts were removed, and the untouched baseline passed. Architecture /
Inheritance Audit, Final Design Lock, Controlled Activation, activation
documentation repair, and activation rereview were recorded before the
separately authorized implementation. The implementation added exactly one
vendor-neutral semantic request, `void requestVelocity(double targetRpm)`,
with `requestSpin()` removed/superseded, exact `STOPPED` and
`VELOCITY_REQUESTED` states, measurement-only Observation, explicit safe stop,
and `FlywheelIONoop`-only runtime. The final production comparison is 103
compared, 99 byte-identical, 4 changed, 0 missing, 0 added; the changed files
are the four authorized Flywheel production files. The final test comparison
is 96 compared, 90 byte-identical, 6 changed, 0 missing, 0 added; the changed
files are the six authorized Flywheel-focused tests. No new production or test
file was added, and protected configuration plus M00_L07 remain unchanged.

The initial Independent Static Review was `HOLD` for stale stop target state,
weak Noop post-request assertions, missing negative-zero coverage, and brittle
regex comment stripping. The bounded static-review repair completed and the
Independent Static Re-review passed under
`PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.

Accepted User focused-test evidence is `BUILD SUCCESSFUL in 16s`, `4
actionable tasks: 3 executed, 1 up-to-date`, `FOCUSED TESTS: PASS` under
`PASS_M00_L08_USER_FOCUSED_TESTS`. Accepted clean full-regression evidence is
`BUILD SUCCESSFUL in 26s`, `7 actionable tasks: 7 executed`, `CLEAN
REGRESSION: PASS` under `PASS_M00_L08_CLEAN_FULL_REGRESSION`.

Accepted bounded WPILib Simulation is recorded chronologically:

1. **Checkpoint 1 — Disabled:** Available=false, Connected=false,
   RequestedState=STOPPED, VelocityRpm=0.0, VelocityValid=false;
   `PASS_M00_L08_SIMULATION_CHECKPOINT_1_DISABLED`.
2. **Checkpoint 2 — Teleoperated enabled, no driver action:** Robot Enabled=Yes;
   Available=false, Connected=false, RequestedState=STOPPED,
   VelocityRpm=0.0, VelocityValid=false;
   `PASS_M00_L08_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`.
3. **Checkpoint 3 — Return Disabled:** FMS Robot Enabled=No;
   Available=false, Connected=false, RequestedState=STOPPED,
   VelocityRpm=0.0, VelocityValid=false;
   `PASS_M00_L08_SIMULATION_CHECKPOINT_3_DISABLED`.

Overall bounded Simulation passed under `PASS_M00_L08_BOUNDED_SIMULATION`.
`VelocityRpm=0.0` while `VelocityValid=false` is canonical invalid-Noop
representation, not measured physical zero RPM. Simulation claims only Noop
composition, telemetry/state presence, deterministic invalid measurement,
STOPPED idle behavior, no automatic Teleop request, and Disabled → Teleop →
Disabled persistence. Physical regulation, convergence, PID/PIDF,
feedforward, sensor fidelity, RPM accuracy, direction, CAN, physical stop,
and **requestVelocity runtime exercise was NOT claimed**; request semantics
were verified by focused/unit tests. Evidence classification is exactly
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

**Files Changed:** The eight authorized documentation/lifecycle files only:

- `AGENTS.md`
- `README.md`
- `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/README.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_STATUS.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_PLAN.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_CHECKLIST.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/docs/M00_L07_to_M00_L08_Step_by_Step.md`

**Verification:** Documentation reconciliation is complete. M00_L08 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, active lesson count
`1`, current active M00 lesson `M00_L08`, implementation complete, static
rereview/focused tests/clean regression/bounded Simulation PASS. Independent
Closure Review is `PENDING`; Freeze and Publication are `NOT AUTHORIZED`;
M00_L09 remains `INACTIVE / NOT CREATED`.

**Expected Result:** The chronology is ready for independent closure review,
with no closure, freeze, publication, or successor-lesson event claimed.
**PASS**

## Step 12 — Controlled freeze transition

**Objective:** Consume the accepted Independent Closure Review and Architect
freeze authorization without performing publication or successor activation.

**Why:** A lesson may become frozen only after the independent closure gate
passes and the Architect explicitly authorizes the controlled lifecycle
transition.

**Action:** Consumed `PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW` with verdict
`READY_FOR_FREEZE_AUTHORIZATION` and recorded `FREEZE AUTHORIZED`. Transitioned
M00_L08 from `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` to
`COMPLETE / FROZEN / READ-ONLY`. Set Active Lesson Count to `0` and Current
Active M00 Lesson to `NONE`. Preserved the implementation, static review,
focused-test, clean-regression, bounded-Simulation, and evidence-classification
records. Publication remains pending and M00_L09 remains inactive/uncreated.

**Files Changed:** The eight authorized documentation/lifecycle files only:

- `AGENTS.md`
- `README.md`
- `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/README.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_STATUS.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_PLAN.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/LESSON_CHECKLIST.md`
- `real_robot_programming/module_M00/M00_L08_FlywheelClosedLoopVelocity/docs/M00_L07_to_M00_L08_Step_by_Step.md`

**Verification:** M00_L08 is `COMPLETE / FROZEN / READ-ONLY`; Active Lesson
Count is `0`; Current Active M00 Lesson is `NONE`; Independent Closure Review
is `PASS`; Publication is `PENDING / NOT YET PUBLISHED`; Final Publication
Verification is `NOT YET PERFORMED`; and M00_L09 is `INACTIVE / NOT CREATED`.
No source, tests, configuration, deployment content, Git publication, metadata
reconciliation, or successor activation was performed.

**Expected Result:** M00_L08 is frozen and ready for a later independent
freeze review, with publication still pending. **PASS**
