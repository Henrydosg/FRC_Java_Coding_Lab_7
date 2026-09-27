# A00_L01 to A00_L02 - Step-by-Step Transition Guide

## Transition Scope

This guide records the controlled inheritance from frozen
`A00_L01_AutonomousCommandLifecycleFoundation` to active
`A00_L02_AutonomousModeScheduling`.

The single new concept is autonomous mode composition and scheduler
requirement ownership. A00_L02 remains zero-motion. A00_L03 remains the first
A00 lesson permitted to issue a nonzero autonomous drivetrain request.

Historical S00 transition guides and frozen A00_L01 files were preserved.

## Step 1 - Governance Authorization

**Objective:** Confirm that A00_L02 is the authorized next lesson.

**Why:** A later lesson may inherit only from the previous completed and
frozen lesson.

**Action:** Review AGENTS.md, Documents A/B/C, the Frozen Backbone and
Interface Contract, the A00 roadmap ADR, and frozen A00_L01.

**Files Changed:** None.

**Verification:** A00 roadmap order authorizes A00_L02 after frozen A00_L01.

**Expected Result:** A00_L02 has an approved inheritance source and a locked
zero-motion boundary.

## Step 2 - Copy Frozen A00_L01 and Rename

**Objective:** Create the independent A00_L02 project from frozen A00_L01.

**Why:** The repository uses one independent inherited WPILib project per
lesson and does not recreate lessons from scratch.

**Action:** Copy the frozen A00_L01 project into
`A00_L02_AutonomousModeScheduling` and update the project identity.

**Files Changed:** New A00_L02 project copy; frozen A00_L01 was not modified.

**Verification:** Direct-inheritance baseline supplied as `PASS`.

**Expected Result:** A00_L02 starts as a source-equivalent copy of A00_L01.

## Step 3 - Remove Generated Artifacts

**Objective:** Establish a clean source baseline.

**Why:** Build outputs and local generated state are not lesson concepts.

**Action:** Remove inherited generated build artifacts before baseline
verification.

**Files Changed:** Generated artifacts only.

**Verification:** Supplied artifact-cleanup evidence: `PASS`.

**Expected Result:** A00_L02 has a clean inherited project baseline.

## Step 4 - Baseline Java 17 Build

**Objective:** Prove the inherited project builds before the A00_L02 change.

**Why:** This separates inheritance problems from the new scheduling concept.

**Action:** Run the baseline Java 17 build.

**Files Changed:** None.

**Verification:** User-supplied Java 17 baseline build: `PASS`.

**Expected Result:** The inherited A00_L02 project is buildable before edits.

## Step 5 - Architecture Audit

**Objective:** Identify the autonomous ownership gap without adding motion.

**Why:** `Commands.none()` completes immediately and requires no subsystem.
The teleop default command could therefore remain eligible during Autonomous.
A finite hold alone would release ownership when it finished.

**Action:** Audit Robot mode hooks, scheduler behavior, the default command,
`RobotContainer.getAutonomousCommand()`, the frozen hold command, and
`SwerveSubsystem.stop()`.

**Files Changed:** None.

**Verification:** Architecture audit approved a repeating composition and no
Robot.java change.

**Expected Result:** The design retains Swerve ownership for the whole active
autonomous command lifecycle.

## Step 6 - Design Lock

**Objective:** Lock the smallest production change for A00_L02.

**Why:** One lesson introduces one independently verifiable concept.

**Action:** Preserve `AutonomousSafetyHoldCommand`; add one named lifecycle
interval; construct the hold with `SwerveSubsystem` and `Timer::getFPGATimestamp`;
wrap it with `.repeatedly()`; and return it from
`getAutonomousCommand()`.

**Files Changed:** None.

**Verification:** Design lock forbids nonzero requests, Robot changes, IO
changes, telemetry logic, and autonomous frameworks.

**Expected Result:** A00_L02 remains zero-motion while autonomous ownership is
continuous until external cancellation/interruption.

## Step 7 - Implementation

**Objective:** Implement the locked composition at the composition root.

**Why:** RobotContainer is the approved location for construction, injection,
and command composition.

**Action:** Add the named lifecycle interval and return the repeating frozen
safety-hold composition.

**Files Changed:**

- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`

**Verification:** Source review confirmed `Timer::getFPGATimestamp`,
`.repeatedly()`, and no nonzero autonomous request. `Robot.java` remained
unchanged.

**Expected Result:** The returned autonomous command owns Swerve and performs
no motion.

## Step 8 - Deterministic Scheduler Tests

**Objective:** Prove command selection, ownership, repetition, and transitions.

**Why:** Requirement ownership must be verified beyond one hold interval and
across mode transitions.

**Action:** Add focused scheduler/composition coverage for autonomous
selection, default-command exclusion, repeated ownership, zero output,
Autonomous-to-Teleop recovery, Disabled cleanup, and Test cancellation.

**Files Changed:**

- `src/test/java/frc/robot/RobotContainerAutonomousModeSchedulingTest.java`

**Verification:** User-supplied Java 17 focused test: `PASS`.

**Expected Result:** No default-command resumption or stale autonomous intent
occurs while the composition is active or after cancellation.

## Step 9 - Test Compile Correction

**Objective:** Align the new test fixture with the frozen IO input contract.

**Why:** The current `SwerveModuleIOInputs` type has separate drive, steer,
and encoder health fields rather than generic health fields.

**Action:** Remove invalid `inputs.connected` and
`inputs.configurationHealthy` assignments. Retain the established six-field
healthy fixture pattern.

**Files Changed:**

- `src/test/java/frc/robot/RobotContainerAutonomousModeSchedulingTest.java`

**Verification:** The fixture now uses
`driveConnected`, `steerConnected`, `encoderConnected`,
`driveConfigurationHealthy`, `steerConfigurationHealthy`, and
`encoderConfigurationHealthy`. User-supplied focused Java 17 result: `PASS`.

**Expected Result:** Test compilation uses the actual frozen IO contract without
inventing compatibility fields or changing production code.

## Step 10 - Java Verification

**Objective:** Verify the composition and inherited regression.

**Why:** A00_L02 must not regress the frozen lifecycle, drivetrain, or safety
behavior.

**Action:** Run the focused scheduler test, the inherited
`AutonomousSafetyHoldCommandTest`, the full Java 17 regression, and a clean
Java 17 build.

**Files Changed:** None.

**Verification:** User-supplied evidence:

- focused `RobotContainerAutonomousModeSchedulingTest`: `PASS`;
- `AutonomousSafetyHoldCommandTest` regression: `PASS`;
- full Java 17 regression: `PASS`;
- clean Java 17 build: `PASS`.

**Expected Result:** Composition and lifecycle ownership pass without adding
autonomous motion.

## Step 11 - Simulation Verification

**Objective:** Verify runtime ownership and zero-motion behavior.

**Why:** A00_L02 is not permitted to move the drivetrain.

**Action:** Verify Disabled baseline, Autonomous Enabled zero-motion,
multiple repeat intervals, nonzero joystick input during Autonomous,
Autonomous-to-Disabled safe stop, and Autonomous-to-Teleop fresh-input
recovery.

**Files Changed:** None.

**Verification:** User-supplied Simulation evidence: all listed cases `PASS`.

**Expected Result:** Autonomous scheduling owns Swerve without motion; mode
transitions stop safely and fresh Teleop input can recover afterward.

## Step 12 - Documentation Normalization

**Objective:** Replace inherited A00_L01 identity/status with the active A00_L02
lesson record.

**Why:** The active lesson documentation must describe the implemented concept
and must not rewrite frozen A00_L01 history.

**Action:** Update the active README, lesson plan, checklist, and status; create
this transition guide.

**Files Changed:**

- `README.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `LESSON_STATUS.md`
- `docs/A00_L01_to_A00_L02_Step_by_Step.md`

**Verification:** Final architecture and documentation review: `PASS`.
The supplied Java/Simulation evidence is consistent with the locked scope.

**Expected Result:** The active lesson identity, concept, evidence, boundary,
and exclusions are accurate and consistent.

## Step 13 - Finalization and Freeze

**Objective:** Freeze A00_L02 after the approved final architecture review.

**Why:** A completed lesson must preserve its verified implementation and
documentation as a read-only inheritance source.

**Action:** Set the active documentation to `COMPLETE / FROZEN / READ-ONLY`,
set Architecture Review to `PASS`, set Freeze State to `FROZEN`, and mark this
transition guide `FINAL / PASS`.

**Files Changed:**

- `README.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `LESSON_STATUS.md`
- `docs/A00_L01_to_A00_L02_Step_by_Step.md`

**Verification:** Final review confirmed the Frozen Backbone, IO contracts,
RobotContainer composition-root role, zero-motion boundary, and supplied
verification evidence. Real-robot verification remains `HOLD`.

**Expected Result:** A00_L02 is a frozen inheritance source and A00_L03 is the
next authorized lesson.

## Final Transition State

A00_L02 is `COMPLETE / FROZEN / READ-ONLY`. The implementation adds only
autonomous mode composition and scheduler requirement ownership. The repeating
safety hold remains zero-motion, and `kSafetyHoldLifecycleDurationSeconds = 1.0`
is only a lifecycle repeat interval. A00_L03 remains the first lesson
permitted to generate nonzero autonomous drivetrain motion.

Real-robot verification remains `HOLD`; no hardware PASS is claimed.

Non-blocking technical debt retained at freeze:

- inherited commissioning tests using `Thread.sleep`;
- optional stronger default-command precondition assertion;
- real-robot verification HOLD; and
- deferred Test-mode global motion gating.
