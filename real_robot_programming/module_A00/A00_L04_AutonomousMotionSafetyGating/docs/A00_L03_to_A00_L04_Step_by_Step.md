# A00_L03 to A00_L04 - Step-by-Step Transition Guide

## Transition State

- Source lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Source state: `COMPLETE / FROZEN / READ-ONLY`
- Publication state: user-supplied published to `origin/main`
- Active lesson: `A00_L04_AutonomousMotionSafetyGating`
- Active state: `COMPLETE / FROZEN / READ-ONLY`
- Architecture Review: `PASS`
- Implementation: `COMPLETE`
- Guide state: `FINAL / PASS`
- Real robot: `HOLD`

This guide records inheritance, implementation, verification, final review,
and the documentation freeze. It is the final transition guide for A00_L04.

## Step 1 - Preserve the Frozen Predecessor

**Objective:** Establish A00_L03 as the immutable source lesson.  
**Why:** A00 lessons must inherit only from a complete, frozen predecessor.  
**Action:** Use the published `A00_L03_BoundedRobotRelativeAutonomousMotion`
project as the source.  
**Files Changed:** No A00_L03 files.  
**Verification:** User-supplied A00_L03 `COMPLETE / FROZEN / READ-ONLY` and
published-to-`origin/main` evidence.  
**Expected Result:** A00_L03 remains frozen and untouched.

## Step 2 - Copy to A00_L04

**Objective:** Create the independent A00_L04 project.  
**Why:** Each lesson is an independent inherited WPILib project.  
**Action:** Copy A00_L03 into
`A00_L04_AutonomousMotionSafetyGating`.  
**Files Changed:** New A00_L04 project created from the predecessor.  
**Verification:** User-supplied direct-copy evidence.  
**Expected Result:** A00_L04 starts as the inherited L03 baseline.

## Step 3 - Clean Generated Artifacts

**Objective:** Remove generated build artifacts from the copied project.  
**Why:** Generated output must not define lesson source or verification.  
**Action:** Clean inherited build artifacts.  
**Files Changed:** Generated artifacts only; no production source concept.  
**Verification:** User-supplied cleanup evidence.  
**Expected Result:** A clean inherited baseline is available for rebuilding.

## Step 4 - Record the `.wpilib` Configuration Failure

**Objective:** Preserve the actual baseline failure and its cause.  
**Why:** `.wpilib` is required by the current Gradle team-number configuration.  
**Action:** Record that `.wpilib` was initially removed accidentally. The
baseline then failed because the WPILib team number became unavailable.  
**Files Changed:** No production source files.  
**Verification:** User-supplied inheritance evidence.  
**Expected Result:** The repository rule is explicit: `.wpilib` must not be
treated as disposable build output because
`.wpilib/wpilib_preferences.json` is required by the current Gradle
configuration.

## Step 5 - Restore `.wpilib` and Establish the Baseline

**Objective:** Restore the required WPILib project configuration.  
**Why:** Gradle must obtain the configured team number before the project can
build.  
**Action:** Restore `.wpilib` from frozen A00_L03 and rerun the Java 17
baseline.  
**Files Changed:** `.wpilib` restoration only; no Java, tests, or Gradle changes.  
**Verification:** User-supplied result: `BUILD SUCCESSFUL`.  
**Expected Result:** The inherited A00_L03 baseline builds successfully in
the A00_L04 project.

## Step 6 - Record Repository Ownership Evidence

**Objective:** Record the user-supplied working-tree boundary.  
**Why:** Git is user-owned and must not be run by Codex.  
**Action:** Record the supplied evidence that only A00_L04 is untracked.  
**Files Changed:** Documentation only.  
**Verification:** User-supplied evidence; Git was not run by Codex.  
**Expected Result:** A00_L04 is the user-owned new lesson boundary.

## Step 7 - Complete the Architecture Audit

**Objective:** Establish the pre-implementation architecture result.  
**Why:** A00_L04 must have an architecture decision before implementation.  
**Action:** Apply the completed A00_L04 architecture audit.  
**Files Changed:** No production source files.  
**Verification:** Architecture audit result: `PASS / READY FOR DESIGN LOCK`.  
**Expected Result:** The architecture is compatible, and the only prior hold
was copied-lesson identity and missing transition documentation.

## Step 8 - Activate and Normalize A00_L04

**Objective:** Make A00_L04 the single editable lesson.  
**Why:** Only the active `IN_PROGRESS / EDITABLE` lesson may receive future
implementation changes.  
**Action:** Normalize the A00_L04 README, plan, checklist, and status files;
create this transition guide; preserve all inherited source and verification
boundaries.  
**Files Changed:**

- `README.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `LESSON_STATUS.md`
- `docs/A00_L03_to_A00_L04_Step_by_Step.md`

**Verification:** Documentation consistency audit after editing.  
**Expected Result:** A00_L04 is `IN_PROGRESS / EDITABLE`, with no Java
implementation at that historical activation point.

## Step 9 - Implement the Locked L04 Composition

**Objective:** Add only Test/global autonomous-motion mode gating.  
**Why:** The L04 invariant requires nonzero autonomous motion only while
`DriverStation.isAutonomousEnabled()` is true.  
**Action:** Compose the inherited bounded robot-relative motion with the
repeating zero-motion safety hold and apply the scheduler-managed autonomous
enabled gate. Preserve the frozen A00_L03 command.  
**Files Changed:** Active L04 `RobotContainer.java` only for production
composition.  
**Verification:** User-supplied focused/full Java 17 regression and clean
build: `PASS`.  
**Expected Result:** Invalid mode terminates autonomous motion fail-closed,
and valid completion enters the repeating zero-motion hold without automatic
restart.

## Step 10 - Correct the Disabled Scheduler Expectation

**Objective:** Match the focused test to WPILib scheduling semantics while
preserving safety assertions.  
**Why:** A command with `runsWhenDisabled() == false` is rejected before
initialization when scheduled while already Disabled.  
**Action:** In `disabledInitialSchedulingCannotProduceAutonomousMotion()`, use
`assertEquals(0, subsystem.stopCount)`. Keep the assertions for unscheduled
state, `acceptCount == 0`, no motion, and final zero module states.  
**Files Changed:** Active L04 `RobotContainerAutonomousModeSchedulingTest.java`
only.  
**Verification:** User-supplied focused/full Java 17 regression: `PASS`.  
**Expected Result:** `stopCount == 0` correctly records that no command
lifecycle or stop branch runs during rejected Disabled scheduling; it does not
weaken the safety invariant.

## Step 11 - Verify in Simulation

**Objective:** Verify the locked invariant across the supplied mode scenarios.  
**Why:** Simulation must establish fail-closed behavior before real-robot
verification.  
**Action:** Run the user-owned Simulation verification.  
**Files Changed:** None.  
**Verification:** `PASS` for all supplied scenarios:

1. Disabled baseline has zero drive/steer output and velocity.
2. Autonomous + Enabled produces inherited bounded `+0.30 m/s`
   robot-relative motion, stops after approximately `1.0 s`, and does not
   restart during the repeating safety hold.
3. Autonomous -> Teleop terminates motion immediately and neutral Teleop stays
   stopped.
4. Autonomous -> Disabled stops/disarms motion with no stale request.
5. Test permits no autonomous motion, and Autonomous -> Test terminates motion
   without restart.

**Expected Result:** The mode gate permits motion only while
`DriverStation.isAutonomousEnabled()` is true and remains disarmed after an
invalid-mode termination.

## Step 12 - Normalize Post-Verification Documentation

**Objective:** Record the current implementation, build, Java, and Simulation
results without claiming final review or real-robot verification.  
**Why:** Lesson metadata must distinguish completed current-scope work from
remaining governance and hardware gates.  
**Action:** Normalize the active README, lesson plan, checklist, status, and
this transition guide.  
**Files Changed:**

- `README.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `LESSON_STATUS.md`
- `docs/A00_L03_to_A00_L04_Step_by_Step.md`

**Verification:** Read-only documentation consistency audit.  
**Expected Result at this historical step:** A00_L04 was `IN_PROGRESS /
EDITABLE`; Architecture Review was ready for final review; Simulation was
`PASS`; and real robot remained `HOLD`.

## Step 13 - Final Documentation Freeze

**Objective:** Transition A00_L04 to its authorized frozen lesson state.  
**Why:** Final Architecture Review passed and all required current-scope
implementation and verification evidence is recorded.  
**Action:** Finalize the active README, lesson plan, checklist, status, and
this transition guide. Set the lesson to `COMPLETE / FROZEN / READ-ONLY`.  
**Files Changed:** Documentation only:

- `README.md`
- `LESSON_PLAN.md`
- `LESSON_CHECKLIST.md`
- `LESSON_STATUS.md`
- `docs/A00_L03_to_A00_L04_Step_by_Step.md`

**Verification:** Final Architecture Review: `PASS`; focused/full Java 17
regression: `PASS`; clean build: `PASS`; five supplied Simulation cases:
`PASS`; Real robot: `HOLD`; Driver Station / Glass: not separately tested.  
**Expected Result:** A00_L04 is `COMPLETE / FROZEN / READ-ONLY` and this guide
is `FINAL / PASS`.

## Authorized L04 Concept

Test/global autonomous-motion mode gating.

Safety invariant:

> Nonzero autonomous drivetrain motion is permitted only while
> `DriverStation.isAutonomousEnabled() == true`. Otherwise autonomous motion
> must fail closed through centralized drivetrain stop.

The concept is implemented for the current L04 scope using standard WPILib
command composition. The frozen A00_L03 command remains unchanged.

## Explicitly Out of Scope

- Subsystem health-policy expansion.
- CAN/configuration fault gating.
- Pose gating.
- Odometry/estimator gating.
- Observation freshness contracts.
- PathPlanner.
- AutoBuilder.
- Trajectories.
- Vision / AprilTags.
- Alliance transforms.
- Multi-step autonomous routines.
- Drivetrain tuning.
- Hardware changes.
- Frozen Interface Contract changes.

The following remain frozen and unchanged: A00_L03, A00_L02, A00_L01, and S00.
`Robot.java`, `SwerveSubsystem`, IO, observation, telemetry, hardware
configuration, and Gradle remain unchanged. Only the active L04 composition
and its focused test expectation were changed during implementation; this
normalization changes documentation only.

A00_L04 is the final lesson currently authorized by the existing A00 roadmap
ADR. No A00_L05 is authorized by that ADR.

## Current State

A00_L04 is the final currently authorized A00 lesson and is
`COMPLETE / FROZEN / READ-ONLY`.

- Architecture Review: `PASS`;
- Implementation: `COMPLETE`;
- Java verification: `PASS` for focused and full Java 17 regression;
- Build: `PASS`, including clean build;
- L04 Simulation: `PASS` for all five supplied scenarios;
- L04 Driver Station / Glass: `NOT SEPARATELY TESTED`;
- Real robot: `HOLD`;
- this guide: `FINAL / PASS`.

The remaining non-blocking debt is real-robot verification `HOLD`, no separate
Glass evidence, and inherited sleep-based commissioning tests. No A00_L05 is
authorized by the current roadmap ADR.
