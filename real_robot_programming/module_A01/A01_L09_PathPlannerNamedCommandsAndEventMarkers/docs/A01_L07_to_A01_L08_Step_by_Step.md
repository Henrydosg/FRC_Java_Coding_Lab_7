# A01_L07 to A01_L08 - Step-by-Step Activation, Audit, and Design Lock

## Activation Identity

- Source lesson: `A01_L07_AutoBuilderContractIntegration`.
- Source status: `COMPLETE / FROZEN / READ-ONLY`.
- Active lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`.
- Active status: `COMPLETE / FROZEN / READ-ONLY`.
- Authoritative title: `A01_L08 - Autonomous Routine Selection and Safe Composition`.
- Git: user-owned; Codex ran no Git commands.
- Implementation boundary: the historical steps record the approved L08
  implementation and verification; final closure edits are documentation-only.

## Step 1 - Confirm Frozen L07 Predecessor

### Objective

Confirm that L07 is the immediate completed predecessor for L08.

### Why

The A01 ADR requires every later lesson to inherit only from its immediately
preceding COMPLETE / FROZEN / READ-ONLY lesson.

### Action

Reviewed frozen L07 identity, status, plan, checklist, transition record,
production source, tests, English/Vietnamese learning guides, AutoBuilder
settings, Swerve authority, and safety contracts.

### Files Changed

None.

### Verification

L07 records `COMPLETE / FROZEN / READ-ONLY` with user-confirmed implementation,
build/test, Simulation, and Real Robot gates. L07 was not modified.

### Expected Result

L08 may inherit only from the frozen L07 snapshot.

## Step 2 - Record User-Owned Inheritance

### Objective

Record that the user, not Codex, copied and renamed frozen L07 into the L08
directory.

### Why

Repository governance assigns project copying, build verification, real-robot
verification, and Git operations to the user.

### Action

Audited the user-created directory:
`A01_L08_AutonomousRoutineSelectionAndSafeComposition`.

### Files Changed

None during the copy. This transition record documents the supplied history.

### Verification

The directory exists, the source and test file counts match L07, and production
Java and tests are byte-identical to L07. The user supplied the final baseline
PASS evidence.

### Expected Result

L08 begins as an inherited baseline, not a newly invented project.

## Step 3 - Preserve Project Metadata and Structure

### Objective

Verify that required project metadata and inheritance content survived the copy.

### Why

`.wpilib` is required WPILib project metadata, not disposable build output.

### Action

Verified `.wpilib/wpilib_preferences.json`, `.vscode`, `src`, `vendordeps`,
`gradle`, build files, wrappers, documentation, tests, and PathPlanner assets.

### Files Changed

None.

### Verification

The metadata contains `teamNumber=10951`, `projectYear=2026`, and
`currentLanguage=java`. Required inherited paths are present.

### Expected Result

Future L08 work retains project identity and hardware-team metadata.

## Step 4 - Preserve the Settings Encoding Repair History

### Objective

Record the user-owned settings.gradle identity-rewrite failure and repair.

### Why

Activation records must not rewrite history to imply that a failed intermediate
state never occurred.

### Action

Recorded that the PowerShell identity rewrite introduced an encoding problem and
that the user repaired settings.gradle to valid UTF-8 without a BOM before the
accepted baseline.

### Files Changed

None during the repair; this is historical documentation.

### Verification

Current settings.gradle decodes as valid UTF-8 and has no UTF-8 BOM.

### Expected Result

The final inherited baseline is identified accurately without erasing the repair
event.

## Step 5 - Accept the User-Owned Baseline

### Objective

Record the final inherited baseline evidence without claiming Codex executed it.

### Why

The earlier failed build is superseded by the user's final confirmed result.

### Action

Recorded compileJava PASS, compileTestJava PASS, tests PASS, and clean build PASS
as user-owned evidence.

### Files Changed

L08 documentation only.

### Verification

The user supplied the final baseline PASS; Codex did not rerun or reinterpret it.

### Expected Result

L08 is eligible for activation and design review, not implementation by default.

## Step 6 - Define the L08 Concept Boundary

### Objective

Distinguish paths, trajectories, path-following commands, autonomous routines,
routine selection, and safe composition.

### Why

L08 must teach one architectural concept and must not absorb L09 marker/event
coordination or broader competition strategy.

### Action

Defined an autonomous routine as one or more safe scheduler-owned command steps
forming one autonomous behavior under inherited readiness and lifecycle rules.

### Files Changed

L08 README, status, plan, checklist, and this transition document.

### Verification

The vocabulary and L09 exclusions are recorded in the activated documentation.

### Expected Result

The L08 objective remains exactly the ADR objective: routine selection and safe
composition with explicit cancellation, requirements, and failure behavior.

## Step 7 - Lock the Minimum Routine Set

### Objective

Choose the smallest routine set that proves meaningful selection semantics.

### Why

L08 requires at least two Simulation routines, but does not authorize an
impressive chooser, scoring strategy, or mechanism program.

### Action

Locked exactly two routine identities:

1. `SAFE_STOP` - default bounded non-driving safety hold.
2. `ONE_METER_PATH` - existing known one-meter AutoBuilder path routine.

### Files Changed

L08 documentation only.

### Verification

No additional path, scoring behavior, mechanism action, or competition strategy
was added.

### Expected Result

The two routines are sufficient for selection, fallback, and lifecycle tests.

## Step 8 - Lock Selection Ownership and Snapshot Semantics

### Objective

Define where selection belongs and prevent dashboard changes from changing an
active autonomous run.

### Why

RobotContainer is the composition root, while scheduler lifecycle owns command
execution. Continuous chooser reads would violate snapshot and no-restart rules.

### Action

Design-locked one `SendableChooser<AutonomousRoutineId>` constructed and exposed
by RobotContainer. `SAFE_STOP` is the default; `ONE_METER_PATH` is the alternate.
`getAutonomousCommand()` samples the identity once during `Robot.autonomousInit()`
and delegates to fresh command construction.

### Files Changed

L08 documentation only.

### Verification

The design contains no persistent chooser Command instances and no chooser read
during active execution.

### Expected Result

Selection changes after autonomous starts cannot replace or restart the current
command; a later autonomous start receives a fresh snapshot.

## Step 9 - Lock Readiness, Transform, Requirement, and Stop Contracts

### Objective

Preserve all inherited safety and ownership boundaries while adding selection.

### Why

Routine selection must not bypass accepted pose, alliance validity, one-shot
readiness, scheduler requirements, or centralized stopping.

### Action

Locked shared accepted-start-context consumption, L04 as the exactly-one
transform owner, `shouldFlipPath=false`, `preventFlipping=true`,
SwerveSubsystem requirement ownership, centralized stop on every terminal/fault
path, and no automatic restart.

### Files Changed

L08 documentation only.

### Verification

Null/unknown selection, missing readiness, invalid alliance, missing path, factory
failure, and malformed routine all fail closed to non-driving SAFE_STOP.

### Expected Result

L08 selection cannot create unsafe motion or a second alliance transformation.

## Step 10 - Lock the Minimum Implementation and Test Delta

### Objective

Define independently verifiable implementation and test boundaries before coding.

### Why

The user requested audit/design activation only; implementation requires a later
explicit authorization.

### Action

Proposed one new `AutonomousRoutineFactory` class and a narrow RobotContainer
change. Proposed focused factory and RobotContainer selection tests cover default
and explicit selection, snapshot behavior, fresh instances, fallbacks,
requirements, stop behavior, Blue/Red transform ownership, and inherited L07
settings.

### Files Changed

L08 documentation only.

### Verification

No L08 Java or test file was modified. Inherited L08 source and tests remain
baseline-only and byte-identical to frozen L07.

### Expected Result

Implementation can begin later from a bounded, reviewable delta.

## Step 11 - Define Future User-Owned Verification

### Objective

Define Simulation and Real Robot evidence without claiming either result now.

### Why

The user owns runtime and hardware verification; L08 activation cannot invent
evidence.

### Action

Recorded future checks for chooser visibility/default, selected SAFE_STOP and
ONE_METER_PATH behavior, Blue/Red exactly-one transform, in-run chooser changes,
Disable stop, no restart without fresh readiness, and safe fallback. Real Robot
requires prior Simulation PASS, conservative speed, and physical stop/rollback.

### Files Changed

L08 documentation only.

### Verification

Simulation, Driver Station / Glass, and Real Robot remain `NOT TESTED`.

### Expected Result

Future evidence is collected in the required order and remains user-owned.

## Step 12 - Activate Documentation and Stop Before Implementation

### Objective

Make L08 the single active lesson while stopping at the approved design boundary.

### Why

Only the active lesson is editable; frozen L07 must remain immutable.

### Action

Activated L08 README, LESSON_STATUS, LESSON_PLAN, LESSON_CHECKLIST, root README
status, and this transition document. No production implementation was started.

### Files Changed

Documentation/status artifacts only; no L07 file changed.

### Verification

L08 is `IN_PROGRESS / EDITABLE`; L07 is `COMPLETE / FROZEN / READ-ONLY`; L09
does not exist; no L08 implementation, test delta, Simulation, or Real Robot
result is claimed.

### Expected Result

Activation and design lock are complete. The next step requires explicit
implementation authorization and a separate implementation review.

## Step 13 - Implement the Routine Factory

### Objective

Create the smallest immutable routine identity and fresh-command boundary.

### Why

Routine selection must not store reusable commands or allow invalid inputs to
silently select a driving routine.

### Action

Added `AutonomousRoutineFactory` with exactly `SAFE_STOP` and `ONE_METER_PATH`.
`SAFE_STOP` creates the existing bounded `AutonomousSafetyHoldCommand`.
`ONE_METER_PATH` delegates to the existing L07 `AutoBuilderContractAdapter`.
Null selection, missing readiness, null commands, and construction exceptions
fail closed to a fresh safety hold.

### Files Changed

- `src/main/java/frc/robot/commands/AutonomousRoutineFactory.java`

### Verification

`compileJava` passed using the repository's bundled WPILib JDK 17.

### Expected Result

Each autonomous request receives a fresh command while L07 path, alliance,
readiness, transform, flipping, and stop contracts remain owned by existing
components.

## Step 14 - Wire Chooser Snapshot at the Composition Root

### Objective

Publish one chooser and snapshot its identity exactly once per autonomous
command request.

### Why

`RobotContainer` owns composition, while later dashboard changes must not
replace, restart, or mutate a scheduled command.

### Action

Modified `RobotContainer` to publish `SendableChooser<AutonomousRoutineId>` with
`SAFE_STOP` as the default and `ONE_METER_PATH` as the alternate. The existing
`Robot.autonomousInit()` lifecycle remains unchanged; `getAutonomousCommand()`
reads the chooser once, consumes the accepted start context once, and delegates
fresh command creation.

### Files Changed

- `src/main/java/frc/robot/RobotContainer.java`

### Verification

No `Robot.java`, Constants, SwerveSubsystem, IO, RobotConfig, PathPlanner asset,
CTRE, or frozen L07 file changed. `compileJava` passed.

### Expected Result

Chooser selection is a pre-start snapshot and cannot dynamically switch or
restart autonomous execution.

## Step 15 - Add Focused L08 Tests

### Objective

Exercise factory fallback/freshness and composition-root chooser behavior.

### Why

The new boundary requires focused evidence without changing inherited L01-L07
regression source.

### Action

Added focused factory and chooser-selection tests covering default SAFE_STOP,
fresh instances, missing readiness, delegation, construction failure, and
post-snapshot chooser mutation.

### Files Changed

- `src/test/java/frc/robot/commands/AutonomousRoutineFactoryTest.java`
- `src/test/java/frc/robot/RobotContainerAutonomousRoutineSelectionTest.java`

### Verification

The local Gradle `compileTestJava` task is currently HOLD before test execution:
it reports that unchanged inherited project classes cannot be resolved from its
reported `build/classes/java/main` classpath. The failure reproduces with a
clean, no-daemon build and is recorded as environment state; no architecture
or frozen-source change was made to bypass it.

### Expected Result

Focused and inherited tests remain ready for the user's verified Java/Gradle
environment; no Simulation or real-robot evidence is claimed here.

## Step 16 - Record Implementation Boundary

### Objective

Finalize the L08 implementation record without closing or freezing the lesson.

### Why

L08 remains editable until user-owned build, Simulation, and real-robot gates
are complete.

### Action

Updated L08 README, status, plan, checklist, and this transition guide to record
the implemented boundary, compile result, test-classpath hold, exclusions, and
user-owned runtime verification.

### Files Changed

- repository `README.md`
- `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `docs/A01_L07_to_A01_L08_Step_by_Step.md`

### Verification

L08 is `IN_PROGRESS / EDITABLE`; L07 remains `COMPLETE / FROZEN / READ-ONLY`.
No AutoBuilder implementation beyond the frozen L07 adapter was added, and no
Git, Simulation, Driver Station / Glass, or Real Robot operation was performed.

### Expected Result

The implementation is ready for ChatGPT implementation review after the local
test-classpath environment issue is resolved and the required verification
sequence is rerun.

## Step 17 - Audit the Eleven Initial Failures

### Objective

Classify every failure from the user-supplied 430-test / 11-failure result
before changing any test or production code.

### Why

L08 changes autonomous selection and command snapshot semantics. Inherited L07
tests may encode assumptions that are intentionally replaced by the L08
contract, while a focused failure may still expose a real defect.

### Action

Ran `RobotContainerAutonomousModeSchedulingTest`,
`RobotContainerPathPlannerIntegrationTest`,
`AutonomousRoutineFactoryTest`, and
`RobotContainerAutonomousRoutineSelectionTest` independently. The first three
reproduced 6, 4, and 1 failures respectively; the chooser class passed 2/2.
This established that the 11 failures were not full-suite-only state leakage.

### Files Changed

None during classification.

### Verification

Ten failures were Category B inherited-test migrations: implicit default
selection, pre-readiness command snapshots, or obsolete one-shot stop
lifecycle. One was Category F: a focused fixture contradicted the locked
Swerve requirement owner. No Category A production regression was found.

### Expected Result

Only contract-aligned test expectations are changed; production behavior and
frozen L07 remain protected.

## Step 18 - Apply Minimal Repair and Verify

### Objective

Repair only the proven L08 test-contract mismatches and verify the complete
suite without weakening safety assertions.

### Why

Driving tests must select `ONE_METER_PATH` explicitly and request a fresh
command after accepted readiness. Invalid readiness, alliance, and adapter
cases must verify bounded `SAFE_STOP`, which is the L08 fail-closed contract.

### Action

Updated only the active L08 inherited test fixtures to select the routine
explicitly, request readiness-timed snapshots, and assert the safety-hold
fallback. Corrected the focused factory fixture to assert that delegated path
commands retain the Swerve requirement. No production Java change was needed.

### Files Changed

- `src/test/java/frc/robot/RobotContainerAutonomousModeSchedulingTest.java`
- `src/test/java/frc/robot/RobotContainerPathPlannerIntegrationTest.java`
- `src/test/java/frc/robot/commands/AutonomousRoutineFactoryTest.java`
- L08 README, status, plan, checklist, and this transition record

### Verification

Factory 4/4, chooser 2/2, autonomous mode 17/17, PathPlanner integration 9/9,
frozen-L07 PathPlanner regression 14/14, and the full source-complete JUnit
suite 430/430 passed with zero failures, errors, or skips. Assertions were not
weakened; tests were not deleted, disabled, ignored, or skipped. A fresh direct
Gradle `clean compileTestJava` remains an environment classpath-resolution
HOLD, while the user-owned baseline compileTestJava/tests/clean-build PASS is
preserved as supplied evidence. At the time of this repair step, Simulation and
Real Robot remained unrun; the subsequent user-owned results are reconciled in
Step 19.

### Expected Result

The L08 repair is functionally PASS and remains `IN_PROGRESS / EDITABLE` until
the user-owned runtime gates are completed; no lesson closure or Git operation
is performed.

## Step 19 - Reconcile User-Owned Simulation and Real-Robot Evidence

### Objective

Record the final user-owned runtime evidence without inventing an unavailable
manual chooser-change result or precision claim.

### Why

Governance assigns Simulation, Driver Station / Glass, and Real Robot
verification to the user. Codex records supplied evidence but does not replace
it with inference.

### Action

Recorded user confirmation that the chooser was visible, SAFE_STOP was the
safe/default routine, ONE_METER_PATH was explicitly selectable and executed,
Blue and Red autonomous execution passed, Disable/mode-loss/cancellation
stopped the robot, and re-enable without fresh readiness did not restart
autonomous motion. The UI did not permit switching to ONE_METER_PATH while
Autonomous was already enabled, so no runtime chooser-change result is claimed.
Recorded A01_L08 Real Robot verification PASS.

### Files Changed

- repository `README.md`
- L08 `README.md`
- `LESSON_STATUS.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- this transition document
- English and Vietnamese L08 learning guides

### Verification

Simulation and Real Robot are `PASS / USER-CONFIRMED`. Exact endpoint accuracy,
one-meter precision, PID/feedforward tuning, RobotConfig physical
characterization, mass/MOI/COF characterization, and competition readiness are
not claimed.

### Expected Result

Runtime evidence is reconciled accurately while preserving the L08 safety,
alliance, readiness, and ownership contracts.

## Step 20 - Closure Gate Decision

### Objective

Determine whether L08 may become COMPLETE / FROZEN / READ-ONLY.

### Why

The user supplied the authoritative post-repair build result required to close
the final verification gate.

### Action

Recorded the user-verified post-repair WPILib VS Code build: `BUILD SUCCESSFUL
in 1s` with `6 actionable tasks: 1 executed, 5 up-to-date`. This supersedes the
prior direct-Gradle classpath-resolution environment hold. The accepted full
test result is 430/430 PASS, and the user also supplied Simulation PASS,
including Blue/Red, SAFE_STOP, ONE_METER_PATH, Disable/mode-loss stop, and no
automatic restart, plus Real Robot PASS.

### Files Changed

Documentation only.

### Verification

All required verification gates are PASS from user-supplied evidence. No
endpoint precision, final PID/feedforward tuning, or final physical
characterization claim is made. L07 remains frozen; L09 is not created or
started.

### Expected Result

The final build gate is reconciled and the lesson is eligible for documentation
closure.

## Step 21 - Freeze A01_L08

### Objective

Record the final documentation-only state as COMPLETE / FROZEN / READ-ONLY.

### Why

Governance requires a completed lesson to be a protected snapshot before the
next lesson inherits it.

### Action

Updated the root README, L08 README, status, plan, checklist, this transition
guide, and the English/Vietnamese learning guides. No production Java, tests,
configuration, assets, or frozen L01-L07 files were modified.

### Files Changed

- repository `README.md`
- L08 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, and
  `LESSON_CHECKLIST.md`
- this transition document
- L08 English and Vietnamese learning guides

### Verification

A01_L08 is `COMPLETE / FROZEN / READ-ONLY`; the transition guide is final and
PASS. A01_L08 is the frozen inheritance source for A01_L09. A01_L09 remains
`NOT CREATED / NOT STARTED`. Git commit and push remain user-owned and were not
run by Codex.

### Expected Result

Documentation closure is PASS, the L08 snapshot is protected for future
inheritance, and no implementation work is started for L09.
