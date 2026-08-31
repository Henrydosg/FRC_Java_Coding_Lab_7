# A01_L08 to A01_L09 - Step-by-Step Transition Guide

## Purpose and closure meaning

This guide explains how the final A01_L08 safety/robustness lesson became
A01_L09, including the reconstruction of the L09 baseline, the one new
learning concept, the test-fixture repair history, and the user-owned runtime
verification. It is written so a student can understand the lineage without
reconstructing the chat history.

The historical L09 baseline predates the final L08 safety repair. Therefore,
L09 was reconstructed from final L08 before the event-marker feature was added.
It is not accurate to describe L09 as merely inheriting an old L09
implementation.

Evidence used for this guide includes the L09 `README.md`, `LESSON_PLAN.md`,
`LESSON_CHECKLIST.md`, `LESSON_STATUS.md`, the Phase 2B implementation record,
the L09 learning guide, the unchanged event path asset, the final repository
README, the approved A01 roadmap ADR, the approved A01_L08 reopen ADR, and the
user-authoritative final verification report. Earlier Phase 2B records preserve
the historical verification-hold checkpoint; the current lesson records now
identify the final PASS evidence, documentation reconciliation, and final
closure decision. A01_L09 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at
`6b243bb`. Publication was performed by the User; Codex did not run Git.

The authoritative English governance documents reviewed were:

- `docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.pdf`
- `docs/Document_A/ES-06_Frozen_Interface_Contract_EN.pdf`
- `docs/Document_B/English/00_Engineering_Standard_Overview_EN.pdf`
- `docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf`
- `docs/Document_B/English/02_Java_Coding_Standard_EN.pdf`
- `docs/Document_B/English/03_Architecture_Review_Checklist_EN.pdf`
- `docs/Document_B/English/04_Lesson_Module_Checklist_EN.pdf`
- `docs/Document_C/English/00_Observation_Architecture_Overview_EN.pdf`
- `docs/Document_C/English/01_Observation_Model_Contract_EN.pdf`
- `docs/Document_C/English/02_Observation_Package_Standard_EN.pdf`
- `docs/Document_C/English/03_Observation_Architecture_Checklist_EN.pdf`

The governing flows remain:

```text
CONTROL:      Driver -> Xbox Controller -> controls -> commands -> subsystems -> io -> hardware
OBSERVATION:  hardware -> IOInputs -> subsystem/estimator -> immutable Observation -> telemetry -> NT4/Glass/log
```

The transition guide is a documentation closure artifact. It does not itself
perform the User-owned Git add, commit, or push, and it does not by itself
change the lesson lifecycle status.

## Step 1 - Confirm final A01_L08 and its authoritative parent

**Objective:** Establish the exact source boundary for the transition.

**Why:** A01_L08 is the last approved autonomous safety and safe-composition
foundation. Its final scheduler-native repair must be inherited before L09 can
add event dispatch.

**Action:** Confirm that A01_L08 is `COMPLETE / FROZEN / READ-ONLY` and identify
`A01_L08 @ 135272c`, message `Complete A01_L08 autonomous safety robustness repair`,
as the authoritative parent named by the repository and closure
evidence.

**Files Changed:** None in the parent lesson. The target project is
`real_robot_programming/module_A01/A01_L09_PathPlannerNamedCommandsAndEventMarkers/`.

**Verification:** The L09 project identifies `A01_L08 @ 135272c` as its
authoritative parent. The final L08 closure evidence records the scheduler
exception boundary, centralized stop, terminal ownership, SAFE_STOP, Teleop
gate, and no-restart behavior as PASS.

**Expected Result:** The student knows which L08 state is authoritative and
does not use the stale pre-repair L09 baseline as the parent.

## Step 2 - Create and reconstruct the L09 baseline from final L08

**Objective:** Produce one independent L09 project whose inherited baseline is
equivalent to final L08.

**Why:** The lesson workflow requires inheritance from the immediately previous
completed lesson, but the historical L09 material was created before the final
L08 repair. Reconstruction preserves lineage without creating a second L09
lesson or weakening the frozen predecessor.

**Action:** Reconstruct the L09 project from final L08, clean generated lesson
artifacts as required by the normal inheritance workflow, and preserve the
Frozen Backbone, Frozen Interface Contract, and Documents A/B/C observation
boundary. Keep L08 read-only and keep V00_L02 suspended and untouched.

**Files Changed:** The L09 project baseline: inherited production source,
inherited tests, lesson-local documentation, and the L09 project identity.
No parent lesson or V00_L02 file is changed.

**Verification:** Phase 2A user-authoritative evidence records the reconstructed
final-L08 baseline as passing `compileJava`, `compileTestJava`, the inherited
test suite, and clean build. The Phase 2A record also states that stale event
runtime code was removed from the baseline while the event asset remained
unwired.

**Expected Result:** L09 starts from final-L08-equivalent safety architecture,
not from the older historical L09 implementation.

## Step 3 - Restore and verify inherited L08 safety architecture

**Objective:** Verify the inherited safety behavior before adding the L09
feature.

**Why:** A feature test must not hide an inheritance defect. The event path is
allowed to add observation and coordination, but it must not replace L08
autonomous ownership or safety behavior.

**Action:** Verify the final-L08-equivalent baseline, including:

- `PrepareAutonomousCommand.java` remains inherited exactly;
- the old `SafeAutoBuilderCommand` wrapper is absent;
- manual child `initialize`, `execute`, `isFinished`, and `end` delegation is
  absent;
- the Robot-level scheduler exception boundary and coordinator first-fault
  behavior are present;
- centralized `SwerveSubsystem.stop()` remains the stop authority;
- the scheduler-managed autonomous composition owns Swerve through terminal
  `HOLDING`; and
- SAFE_STOP, the defensive Teleop-enabled output gate, mode-loss handling, and
  no automatic autonomous restart remain in force.

**Files Changed:** None for the inherited contract. L09 event work is kept
outside Swerve, IO, CTRE, CANcoder, tuning, calibration, Gradle, vendordeps,
and the inherited preparation command.

**Verification:** The L09 implementation record and checklist record these
inheritance checks as PASS, including final-L08 safety files remaining
hash-identical after the event implementation.

**Expected Result:** A01_L09 has a verified L08 safety floor before the new
NamedCommands/event-marker behavior is introduced.

## Step 4 - Preserve and verify the L09 PathPlanner event asset

**Objective:** Keep the approved L09 event path available for the feature
without changing its event contract.

**Why:** The path asset is the concrete lesson input that distinguishes an
event-enabled path from the inherited baseline path. It belongs to L09 because
the event marker is the new architectural input being taught.

**Action:** Preserve
`src/main/deploy/pathplanner/paths/A01_L09_OneMeter_With_Learning_Event.path`
unchanged. Verify that it is a one-meter path with exactly one named
`LEARNING_EVENT` marker at `waypointRelativePos: 0.5`.

**Files Changed:** No change to the asset. The asset is retained as L09 scope
and is not used to redesign the L08 path or the drivetrain.

**Verification:** The repository asset contains the named marker, a relative
position of `0.5`, and the expected one-meter waypoints. Blue and Red path
construction tests preserve the marker. `shouldFlipPath = false` and
`preventFlipping = true` remain the active transform contract.

**Expected Result:** The path contains one deliberate event input, and the
student can trace that input to the L09 feature rather than to a hidden
drivetrain change.

## Step 5 - Introduce the L09 learning objective

**Objective:** Add exactly one new architectural concept: PathPlanner
NamedCommands and event markers.

**Why:** Path following can coordinate other robot actions at defined points,
but the coordination boundary must be explicit, scheduler-owned, observable,
and safe. L09 teaches that boundary without adding new mechanism architecture.

**Action:** Add the neutral `AutonomousEventId.LEARNING_EVENT` identity and the
additive `ONE_METER_WITH_EVENT` autonomous routine. Keep `SAFE_STOP` as the
chooser default and keep `ONE_METER_PATH` as the event-free baseline.

**Files Changed:** L09 event identity, event binding/registration/command,
immutable event observation, event telemetry, the routine factory/chooser
boundary, and the corresponding focused tests. The exact implementation
inventory is preserved in `LESSON_CHECKLIST.md` and the Phase 2B record.

**Verification:** The source contains one stable NamedCommands name,
`AutonomousEventId`, and one additional routine identity. No mechanism command
or D01 subsystem is imported into A01.

**Expected Result:** L09 adds one controlled event-dispatch lesson and no
unapproved mechanism or drivetrain feature.

## Step 6 - Understand the L09 event architecture

**Objective:** Trace one event marker from the path file to operator-visible
telemetry.

**Why:** Each layer has a single responsibility. PathPlanner identifies when
the event is requested; the registry resolves its stable name; the scheduler
owns command lifecycle; the command emits meaning; and telemetry publishes
that meaning without controlling the robot.

**Action:** Follow this runtime sequence:

```text
PathPlanner event marker
    -> NamedCommands registry
    -> Commands.defer(...)
    -> fresh scheduler-managed event command
    -> immutable AutonomousEventObservation
    -> read-only AutonomousEventTelemetryFacade
    -> /AutonomousEvent
```

`RobotContainer` performs composition and registration only. The deferred
supplier creates a fresh `AutonomousEventDemonstrationCommand` for each
dispatch. The command publishes `STARTED`, `ACTIVE`, and terminal
`COMPLETED`/`CANCELLED` observations. Registration failures publish
`FACTORY_FAILURE` and return a scheduler-owned safe no-op.

In parallel, the path follower remains on the existing control and hardware
flow:

```text
Path-following command -> SwerveSubsystem -> IO contracts -> hardware
```

**Files Changed:** `AutonomousEventId.java`,
`AutonomousEventObservation.java`, `AutonomousEventBinding.java`,
`AutonomousEventRegistration.java`,
`AutonomousEventDemonstrationCommand.java`,
`AutonomousEventTelemetryFacade.java`, and the composition/factory wiring.

**Verification:** Focused tests cover stable identity, immutable observation,
binding validation, deferred construction, fresh command instances, lifecycle
states, factory failure, duplicate registration, and telemetry publication.

**Expected Result:** A student can explain both flows and can identify exactly
where event timing, command lifecycle, observation meaning, and telemetry
publication belong.

## Step 7 - Understand why LEARNING_EVENT has no Swerve requirement

**Objective:** Allow the demonstration event and the path follower to coexist.

**Why:** A command requirement is an ownership claim. If the learning event
claimed Swerve, the scheduler would interrupt or prevent the path-following
command. That would turn a marker demonstration into an accidental drivetrain
coordination feature.

**Action:** Register `LEARNING_EVENT` with an empty immutable requirement set.
Reject a Swerve requirement at the binding boundary. Do not allow the event to
submit chassis speeds, stop modules, read IO, access vendor APIs, or represent
an Intake, Feeder, Flywheel, or other mechanism action.

**Files Changed:** `AutonomousEventBinding.java`, event registration, and the
focused requirement tests. No Swerve or IO file is changed.

**Verification:** The binding test rejects `SwerveSubsystem`; event tests
assert an empty requirement set; runtime evidence shows path following
continues while the learning event executes.

**Expected Result:** The scheduler can run the Swerve-owning path command and
the non-mechanism learning event at the same time without an ownership
conflict.

## Step 8 - Compare ONE_METER_PATH and ONE_METER_WITH_EVENT

**Objective:** Distinguish the baseline routine from the event-enabled routine.

**Why:** The student must be able to tell whether a path behavior comes from
normal path following or from the new marker/event boundary.

**Action:** Use the chooser as follows:

| Routine | Path behavior | Event behavior | Safety behavior |
| --- | --- | --- | --- |
| `ONE_METER_PATH` | Follows the inherited one-meter path. | Does not intentionally dispatch `LEARNING_EVENT`. | Uses final-L08 preparation, Swerve ownership, stop, terminal `HOLDING`, and no-restart behavior. |
| `ONE_METER_WITH_EVENT` | Follows the one-meter event path. | Dispatches `LEARNING_EVENT` at relative position `0.5`; lifecycle is observable. | Uses the same final-L08 preparation, Swerve ownership, stop, terminal `HOLDING`, and no-restart behavior. |

The event path's `LEARNING_EVENT` marker is at relative position `0.5`, the
middle of the path's waypoint-relative event coordinate. The marker is not a
second path, a second transform, or a request to stop the drivetrain.

**Files Changed:** `AutonomousRoutineFactory.java`, chooser wiring, and the
event path asset retained in Step 4. The inherited `ONE_METER_PATH` asset and
its safety composition remain unchanged.

**Verification:** Routine-selection and PathPlanner integration tests verify
the two identities, the event path marker, and the inherited requirements and
transform contract.

**Expected Result:** Selecting `ONE_METER_PATH` isolates the baseline; selecting
`ONE_METER_WITH_EVENT` demonstrates marker dispatch without changing terminal
autonomous safety.

## Step 9 - Run automated verification and record the initial nine failures

**Objective:** Use automated tests to verify both the new event boundary and
the inherited L08 regression surface.

**Why:** The event feature crosses PathPlanner, WPILib scheduling,
DriverStation simulation, observation, telemetry, and routine composition.
The first test result is evidence to diagnose, not a reason to change
production code immediately.

**Action:** Run the Java compile gates, focused event/integration tests, the
inherited regression suite, the full test suite, and the clean Gradle build.
Record the initial result as nine failures before making repairs.

**Files Changed:** None as part of diagnosis. Do not change production code,
Gradle, vendordeps, or assets merely to make the first failing run green.

**Verification:** The authoritative verification report records nine initial
failures. The repository's earlier Phase 2B hold is historical evidence from
before the environment recovered; it is superseded for closure by the later
PASS record.

**Expected Result:** The failure set is preserved as a reproducible forensic
starting point, separate from the later repair and PASS evidence.

## Step 10 - Diagnose the failures as test-fixture defects

**Objective:** Prove whether the nine failures are production defects or
incorrect test setup.

**Why:** Changing production safety or event architecture without proving a
production defect would expand lesson scope and could weaken the frozen
contracts.

**Action:** Isolate the failures and compare each fixture's assumptions with
WPILib/PathPlanner runtime requirements:

1. Four event-command failures used a `DriverStationSim` state that was not
   enabled for Autonomous. The scheduler therefore did not execute the event
   under the mode the test intended.
2. Five failures were caused by `NamedCommands` static registration state
   leaking between tests. Earlier registrations contaminated later tests and
   made duplicate or lookup behavior depend on test order.

The source architecture remained consistent: the event is scheduler-native,
the event has no Swerve requirement, and the production duplicate-registration
guard remains necessary.

**Files Changed:** None during the forensic diagnosis.

**Verification:** The failures reproduce as individual fixture problems, and
the failure classification in the authoritative report identifies no
production defect requiring a Java production repair.

**Expected Result:** The nine failures are classified as test-fixture defects,
not as evidence against the L09 production architecture.

## Step 11 - Apply only the authorized test-only repairs

**Objective:** Make the tests represent the intended runtime contract without
changing production behavior.

**Why:** Test isolation and correct simulated mode are necessary for meaningful
verification. They are test harness concerns, not new robot behavior.

**Action:** Repair only the tests:

- initialize the event-command fixtures with enabled Autonomous
  `DriverStationSim` data and notify the simulated Driver Station of the new
  data; and
- clear the static `NamedCommands` registry before/after each relevant test so
  each test owns its registration state.

Do not remove the production duplicate-registration guard, loosen requirement
validation, add a Swerve requirement, or change `Commands.defer(...)`.

**Files Changed:** Authorized L09 test fixtures only. No production file was
repaired for these nine failures.

**Verification:** The authoritative report classifies the repair as TEST ONLY
and records production repair as NONE. The current event tests show explicit
Autonomous simulation setup and `NamedCommands.clearAll()` isolation.

**Expected Result:** The tests exercise the intended mode and registry state,
while production behavior and safety guards remain unchanged.

## Step 12 - Record automated PASS gates

**Objective:** Close the automated verification portion of the L09 feature.

**Why:** The workflow requires compile, focused tests, inherited regression,
full tests, and a clean build before Simulation and hardware verification.

**Action:** Record the later authoritative PASS evidence:

- `compileJava`: PASS.
- `compileTestJava`: PASS.
- focused L09 event, path, routine, integration, observation, and telemetry
  tests: PASS.
- historical full-suite statement: the repository closure summary recorded
  `446/446 PASS`, including 384 unchanged inherited regression tests.
- clean Gradle build: PASS; `BUILD SUCCESSFUL`.

**Files Changed:** Test fixtures only, as described in Step 11. No production
repair was required.

**Verification:** The `446/446` statement is retained as the historical closure
record. A later reverse audit proved that it did not describe the present
source/test snapshot. It is superseded for current-snapshot verification by
the User-owned 2026-08-31 `460/460 PASS` rerun recorded below. The A01_L08
re-freeze count of 449/449 remains a separate L08 evidence record.

**Expected Result:** Automated verification proves the L09 event boundary and
the inherited L08 safety regression without a production safety change.

## Step 13 - Verify Simulation step by step

**Objective:** Confirm the event and inherited safety contracts in Simulation
before physical testing.

**Why:** Simulation provides a controlled check of scheduler ownership, event
dispatch, telemetry, path continuation, mode loss, and no-restart behavior.
The frozen workflow requires Simulation before real-robot verification.

**Action:** With the L09 code deployed to the simulation project, verify:

1. `SAFE_STOP` remains the safe default and produces no autonomous drive.
2. `ONE_METER_PATH` completes as the event-free baseline.
3. `ONE_METER_WITH_EVENT` runs on Blue and dispatches `LEARNING_EVENT`.
4. The event lifecycle becomes observable and reaches its terminal state.
5. The path follower continues while the no-requirement event runs.
6. Disable or mode loss stops autonomous motion safely.
7. Autonomous does not restart automatically after completion or mode loss.
8. The corresponding Red path preserves the event marker and the alliance
   transform contract.

**Files Changed:** None. Simulation is verification evidence, not a source
change.

**Verification:** Simulation Gate: `PASS`, user-verified. The final runtime
evidence covers Blue/Red path behavior, event dispatch and telemetry,
concurrent path/event execution, Disable/mode-loss stop, and no automatic
restart.

**Expected Result:** Simulation demonstrates the intended L09 event behavior
without reopening implementation or changing the inherited safety architecture.

## Step 14 - Verify Driver Station / Glass and inspect telemetry

**Objective:** Learn how to distinguish the selected routine, preparation
state, and event lifecycle in NT4/Glass.

**Why:** The observation and telemetry boundaries make runtime behavior
inspectable without allowing telemetry to control the robot.

**Action:** Open the `/AutonomousEvent` table and inspect:

- `Active`: whether the current event command is active;
- `DispatchCount`: number of observed `STARTED` event dispatches;
- `LastEvent`: the stable event name, expected as `LEARNING_EVENT` after the
  event path runs; and
- `State`: `STARTED`, `ACTIVE`, `COMPLETED`, `CANCELLED`, or
  `FACTORY_FAILURE`.

Open `/AutonomousPreparation` and inspect:

- `Alliance`;
- `Routine`;
- `ReturnedCommand`;
- `Reason`;
- `Running`;
- `State`;
- `PathValid`; and
- `PoseAvailable`.

For a successful event-path completion, the supplied real-robot evidence
showed `LastEvent = "LEARNING_EVENT"`, `State = "COMPLETED"`, and
`Active = false`. The preparation evidence included
`Reason = "COMMAND_COMPLETED"`, `ReturnedCommand = "ONE_METER_WITH_EVENT"`,
`Routine = "ONE_METER_WITH_EVENT"`, `Running = false`, and `State =
"HOLDING"`.

When comparing the two routines, `ONE_METER_PATH` should complete without a
new intentional `LEARNING_EVENT` dispatch, while `ONE_METER_WITH_EVENT` should
increment `DispatchCount` and publish the event lifecycle. Retained telemetry
such as `LastEvent` and `State` may remain visible after an earlier run. Read
those values together with `DispatchCount`, `Routine`, `Running`, and the
current mode; do not assume every retained value represents a new occurrence.

**Files Changed:** None. Glass/NT4 inspection is user-owned verification.

**Verification:** Driver Station / Glass Gate: `PASS`, user-verified. The
reported event and preparation fields were observed during the event-path run.

**Expected Result:** A student can distinguish routine selection, preparation
readiness, event dispatch, and terminal safety state from read-only telemetry.

## Step 15 - Verify the real robot step by step

**Objective:** Confirm the intended L09 behavior on the physical robot after
Simulation and Glass verification pass.

**Why:** Automated tests and Simulation cannot prove physical path execution,
real Driver Station mode transitions, or real event/path coexistence.

**Action:** With the normal safety procedure and Driver Station controls:

1. Select and verify `SAFE_STOP`.
2. Run `ONE_METER_PATH` as the event-free one-meter baseline.
3. Run `ONE_METER_WITH_EVENT` on Blue.
4. Inspect the `LEARNING_EVENT` lifecycle and `/AutonomousEvent` telemetry.
5. Confirm terminal `HOLDING` after path motion completes.
6. Confirm no automatic autonomous restart while Autonomous remains active.
7. Disable, then enter Teleop, and confirm normal Teleop recovery.
8. Run `ONE_METER_WITH_EVENT` on Red.
9. Confirm the marker remains present on Red and the event/path coexistence is
   preserved without interrupting drivetrain path following.

**Files Changed:** None. Real-robot verification is User-owned evidence.

**Verification:** Real Robot Gate: `PASS`, user-verified. All intended cases
passed, including SAFE_STOP, the baseline path, Blue and Red event paths,
event lifecycle/telemetry, terminal HOLDING, no automatic restart,
Disabled-to-Teleop recovery, marker preservation on both alliances, and
event/path coexistence. No new real-robot defect requiring production
modification was found.

**Expected Result:** The physical robot confirms the new event behavior while
retaining the inherited L08 safety and ownership behavior.

## Step 16 - Record what remained inherited and unchanged

**Objective:** Make the boundary between L08 inheritance and L09 addition
explicit.

**Why:** Future students must know which behavior belongs to the safety
foundation and which behavior belongs to event markers.

**Action:** Treat the following as inherited from final L08 and unchanged in
meaning:

- scheduler-native command lifecycle and Robot scheduler exception boundary;
- coordinator first-fault/fatal behavior and fail-closed preparation;
- one-shot readiness consumption and safe routine selection;
- centralized `SwerveSubsystem.stop()` authority;
- Swerve requirement ownership during path execution and terminal `HOLDING`;
- SAFE_STOP ownership;
- the Teleop-enabled output gate;
- Disable/mode-loss stop and normal Teleop recovery; and
- no manual child lifecycle delegation and no automatic restart.

Treat the following as new L09 behavior:

- `LEARNING_EVENT` identity and NamedCommands registration;
- `Commands.defer(...)` fresh construction per dispatch;
- the no-Swerve demonstration command;
- immutable `AutonomousEventObservation` lifecycle values;
- read-only `/AutonomousEvent` telemetry; and
- the additive `ONE_METER_WITH_EVENT` path/routine boundary.

Treat the following as test-only repairs, not production behavior:

- enabling Autonomous in the DriverStationSim event fixtures; and
- clearing static NamedCommands state between tests.

**Files Changed:** None. This step is an architecture and evidence boundary.

**Verification:** The changed-file audit records no Gradle, vendordep, Swerve,
IO, CTRE, CANcoder, PID/feedforward, gyro, PathPlanner asset, frozen-lesson,
or V00_L02 change as part of the L09 event implementation. `.Glass` remains an
operator-view configuration artifact outside the production/test architecture
boundary.

**Expected Result:** A future review can separate inherited safety, new event
behavior, fixture repairs, Simulation evidence, Glass evidence, and physical
evidence without mixing them.

## Step 17 - Follow the final lesson closure sequence

**Objective:** Move from verified implementation to an authorized frozen
lesson closure.

**Why:** Documentation and verification are prerequisites, but only the
approved review and User-owned publication may complete the lifecycle.

**Action:** Follow this order:

```text
documentation
    -> final architecture review
    -> closure approval
    -> User-owned Git add/commit/push
    -> COMPLETE / FROZEN / READ-ONLY
```

Finalize this guide only after the implementation, automated gates, Simulation,
Driver Station / Glass, and real-robot evidence are present. Reconcile the
current L09 README, plan, status, checklist, Phase 2B record, English and
Vietnamese learning guides, and clearly stale repository-level lesson metadata
with that evidence. Preserve earlier HOLD checkpoints as historical records.
Then perform the final architecture review and closure decision. The User
performs Git add, commit, and push. Codex does not run Git or claim publication.

**Files Changed:** The L09 documentation set and clearly stale repository-level
lesson metadata were reconciled. No production Java, tests, Gradle, vendordeps,
PathPlanner assets, V00_L02, or frozen predecessor file was changed by this
documentation task.

**Verification:** Transition Guide: `FINAL / PASS` based on the authoritative
PASS evidence recorded in Steps 12-15 and the later current-snapshot
re-verification below. Automated verification, Simulation, Driver Station /
Glass, Real Robot, and documentation reconciliation gates are all recorded as
PASS. The User later completed publication at `6b243bb`; Codex did not perform
Git operations.

**Expected Result:** A01_L09 is documentation-complete, technically verified,
and `COMPLETE / FROZEN / READ-ONLY` after the final Architect/Reviewer decision.
User-owned Git publication is complete at `6b243bb`.

## Post-closure current-snapshot re-verification - 2026-08-31

**Objective:** Reconcile the historical test-count statement with the present
frozen lesson snapshot without changing source or architecture.

**Why:** A reverse inheritance audit found that the historical `446/446`
statement could not describe the current source/test tree. Static analysis
predicted 460 JUnit invocations, so a fresh execution was required before the
documentation could make a current-snapshot claim.

**Action:** The User independently ran the current A01_L09 snapshot under Java
17:

- `gradlew clean` - exit `0`, `BUILD SUCCESSFUL`;
- `gradlew test --rerun-tasks` - exit `0`, `BUILD SUCCESSFUL`;
- JUnit XML - `460` tests, `0` failures, `0` errors, `0` skipped; and
- `gradlew clean build` - exit `0`, `BUILD SUCCESSFUL`.

**Files Changed:** Documentation/metadata only. Production Java, test Java,
Gradle, vendordeps, PathPlanner assets, V00, and frozen predecessor files were
not changed.

**Verification:** `A01_L09 RE-VERIFICATION: PASS`; current full snapshot suite
`460/460 PASS`. Repository governance and raw commit/push reflogs independently
identify User publication commit `6b243bb5995bd880ea5b2d245e575067d3b8152a`,
subject `Complete reconstructed A01_L09 named commands and event markers`.

**Expected Result:** The chronology preserves the historical `446/446`
statement while making `460/460 PASS` the sole current-snapshot verification
claim. A01_L09 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.

## Final evidence summary

| Evidence area | Result | Boundary |
| --- | --- | --- |
| Authoritative parent | PASS | Final A01_L08 at `135272c`; L08 remains frozen. |
| Frozen Backbone / Interface / Observation architecture | PASS | Control and observation flows preserved; telemetry remains read-only. |
| L09 feature | PASS | `LEARNING_EVENT`, `Commands.defer(...)`, fresh command construction, immutable observation, and no Swerve requirement. |
| Path asset | PASS | One unchanged L09 event path with `LEARNING_EVENT` at relative position `0.5`. |
| Initial failures | Diagnosed | Nine failures were test-fixture defects: four DriverStationSim mode fixtures and five static NamedCommands contamination cases. |
| Production repair for those failures | NONE | Authorized repair was test-only; the duplicate-registration guard remains. |
| Historical automated record | SUPERSEDED | The closure-era `446/446` statement is retained as history and is not a current-snapshot claim. |
| Current automated verification | PASS | User rerun on 2026-08-31: `460/460`, 0 failures/errors/skips; rerun-tasks and clean build PASS. |
| Simulation | PASS | User-verified Blue/Red event/path, telemetry, safety, mode-loss, and no-restart behavior. |
| Driver Station / Glass | PASS | User-verified `/AutonomousEvent` and `/AutonomousPreparation` inspection. |
| Real Robot | PASS | User-verified SAFE_STOP, baseline, Blue/Red event paths, HOLDING, recovery, marker preservation, and coexistence. |
| Documentation reconciliation | PASS | Current L09 records agree on technical PASS and the final `COMPLETE / FROZEN / READ-ONLY` lesson state. |
| Git | PUBLISHED | User-owned publication complete at `6b243bb`; Codex did not run Git. |

The accepted scope does not claim exact endpoint accuracy, final PID/feedforward
tuning, final physical characterization, competition readiness, or new D01
mechanism behavior.
