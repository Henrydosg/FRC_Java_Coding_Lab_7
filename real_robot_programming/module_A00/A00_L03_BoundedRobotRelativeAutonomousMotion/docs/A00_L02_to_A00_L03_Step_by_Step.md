# A00_L02 to A00_L03 - Step-by-Step Transition Guide

## Transition State

- Source lesson: `A00_L02_AutonomousModeScheduling`
- Active lesson: `A00_L03_BoundedRobotRelativeAutonomousMotion`
- Source state: `COMPLETE / FROZEN / READ-ONLY`
- Active state: `COMPLETE / FROZEN / READ-ONLY`
- Guide state: `FINAL / PASS`
- Final architecture review: `PASS`
- Real robot: `PASS` for the user-supplied A00_L03 bounded-motion and
  transition evidence only

This guide records the L02-to-L03 inheritance and the single approved learning
concept. It is not final until implementation, verification, and architecture
review are complete.

## Step 1 - Governance Authorization

**Objective:** Establish A00_L03 as the next authorized lesson.  
**Why:** A00_L03 is the first A00 lesson permitted to generate nonzero
autonomous motion.  
**Action:** Follow the approved A00 roadmap and preserve A00_L02 as frozen.  
**Files Changed:** No frozen lesson files.  
**Verification:** A00 roadmap ADR reviewed.  
**Expected Result:** A00_L03 scope is bounded robot-relative autonomous motion.

## Step 2 - Inherit the Previous Frozen Lesson

**Objective:** Start from the complete A00_L02 project.  
**Why:** Every lesson is an independent inherited WPILib project.  
**Action:** Copy A00_L02 into `A00_L03_BoundedRobotRelativeAutonomousMotion`.  
**Files Changed:** New A00_L03 project created from A00_L02.  
**Verification:** User-supplied direct-inheritance evidence.  
**Expected Result:** A00_L03 initially matches frozen A00_L02.

## Step 3 - Clean Generated Artifacts and Establish Baseline

**Objective:** Separate source inheritance from generated output.  
**Why:** Build artifacts are not lesson source and must not define the baseline.  
**Action:** Remove inherited `build/` and `.gradle/` artifacts, then run the
Java 17 baseline build.  
**Files Changed:** No production source concept.  
**Verification:** User-supplied baseline cleanup and Java 17 evidence.  
**Expected Result:** Clean inherited baseline builds before L03 implementation.

## Step 4 - Architecture Audit and Design Lock

**Objective:** Lock one new concept before implementation.  
**Why:** A00_L01 and A00_L02 established lifecycle and ownership without motion.  
**Action:** Define one bounded robot-relative request followed by the repeating
zero-motion hold.  
**Files Changed:** Design records only; no frozen source changes.  
**Verification:** Architecture audit and design lock completed.  
**Expected Result:** No pose targeting, trajectory, planner, or vision scope.

## Step 5 - Implement the Bounded Motion Command

**Objective:** Add one finite robot-relative command lifecycle.  
**Why:** A00_L03 is the first lesson allowed to generate nonzero autonomous motion.  
**Action:** Add `BoundedRobotRelativeAutonomousDriveCommand` with validated
`ChassisSpeeds`, bounded duration, injected clock, one request, and stop-on-exit.  
**Files Changed:** New command source and focused command tests.  
**Verification:** Deterministic command tests supplied as part of Java 17 verification.  
**Expected Result:** Invalid configuration/time fails closed; no direct IO or pose logic.

## Step 6 - Compose Autonomous Ownership

**Objective:** Keep Swerve ownership after motion completes.  
**Why:** A finite motion command alone would release the subsystem during Autonomous.  
**Action:** In RobotContainer, compose bounded motion with
`AutonomousSafetyHoldCommand.repeatedly()`.  
**Files Changed:** `Constants.java`, `RobotContainer.java`, and composition tests.  
**Verification:** Scheduler/ownership tests supplied as part of Java 17 verification.  
**Expected Result:** The hold remains scheduled until external mode cancellation.

## Step 7 - Java Verification

**Objective:** Verify the implementation and inherited regressions.  
**Why:** Source review alone cannot establish executable correctness.  
**Action:** Run the required Java 17 command, composition, regression, full-test,
and clean-build gates.  
**Files Changed:** No additional source files.  
**Verification:** User supplied `Java 17 verification: PASS`.  
**Expected Result:** A00_L03 implementation and inherited safety behavior pass.

## Step 8 - Simulation Case 1: Disabled Baseline

**Objective:** Prove no motion occurs before Autonomous.  
**Why:** A bounded autonomous request must not run while Disabled.  
**Action:** Start Simulation Disabled with neutral input and observe drive output.  
**Files Changed:** None.  
**Verification:** Simulation Case 1: `PASS`.  
**Expected Result:** Robot remains stationary.

## Step 9 - Simulation Case 2: Bounded Robot-Relative Motion

**Objective:** Prove the first authorized nonzero autonomous request.  
**Why:** This is the single new L03 behavior.  
**Action:** Enter Autonomous with the named baseline `+0.30 m/s` forward,
`0.00 m/s` lateral, `0.00 rad/s`, and approximately `1.0 s`.  
**Files Changed:** None.  
**Verification:** Simulation Case 2: `PASS`; position changed, then output and
velocity returned to zero.  
**Expected Result:** Robot moves briefly, stops automatically, and the repeating
hold does not restart motion.

## Step 10 - Simulation Case 3: Joystick Isolation

**Objective:** Prove Teleop input does not own Swerve during Autonomous.  
**Why:** Autonomous requirement ownership must exclude the default Teleop command.  
**Action:** Apply nonzero joystick input while Autonomous remains active.  
**Files Changed:** None.  
**Verification:** Simulation Case 3: `PASS`.  
**Expected Result:** Autonomous motion behavior is unaffected by joystick input.

## Step 11 - Simulation Case 4: Autonomous to Disabled

**Objective:** Prove mode transition stop behavior.  
**Why:** No autonomous request may survive Disabled.  
**Action:** Transition from Autonomous to Disabled and observe all drive outputs.  
**Files Changed:** None.  
**Verification:** Simulation Case 4: `PASS`.  
**Expected Result:** Drivetrain stops and stale motion does not resume.

## Step 12 - Simulation Case 5: Teleop Recovery

**Objective:** Prove fresh Teleop input can recover after Autonomous cancellation.  
**Why:** Safe autonomous termination must not permanently disarm normal Teleop.  
**Action:** Enter Teleop, keep input neutral, then provide fresh valid input.  
**Files Changed:** None.  
**Verification:** Simulation Case 5: `PASS`.  
**Expected Result:** Neutral input remains stopped; fresh input restores Teleop motion.

## Step 13 - Documentation Increment

**Objective:** Reconcile active lesson documentation with the implemented L03 scope.  
**Why:** The inherited documents still identified the active lesson as A00_L02.  
**Action:** Normalize README, plan, checklist, status, and this transition guide.  
**Files Changed:** Those five documentation files only.  
**Verification:** Final architecture review: `PASS`; user approved finalization.  
**Expected Result:** Documentation records the completed and frozen L03 lesson accurately.

## Real-Robot Verification Amendment

**Objective:** Record the user-supplied A00_L03 hardware evidence without
changing the frozen implementation, architecture, or lesson scope.

**Evidence:**

1. **Disabled baseline:** `PASS`.
2. **Autonomous bounded real drivetrain motion on the floor:** `PASS`; the
   command completed, the drivetrain stopped, and motion did not restart
   while Autonomous remained enabled.
3. **Autonomous -> Disabled interruption:** `PASS`; the drivetrain stopped
   with no stale output.
4. **Autonomous -> Teleop transition:** `PASS`; autonomous ownership cleared
   and fresh Teleop control recovered normally.
5. **Autonomous -> Test transition:** `PASS`; no stale or restarted
   autonomous output appeared.

A temporary E-Stop occurred during testing. The robot was rebooted and Case 3
was rerun successfully. This event is test context and is not classified as
an A00_L03 defect.

**Scope boundary:** This PASS is limited to A00_L03 bounded robot-relative
motion and lifecycle transitions. It does not claim PathPlanner, AutoBuilder,
localization, autonomous path following, or competition readiness.

**Files Changed:** Documentation only: the A00_L03 README, lesson plan,
checklist, status, and this transition guide.

**Verification:** Documentation consistency audit completed. A00_L03 remains
`COMPLETE / FROZEN / READ-ONLY`; Java, tests, Gradle, vendordeps, hardware
configuration, and other lessons remain unchanged.

**Expected Result:** The supplied A00_L03 real-robot bounded-motion and
lifecycle evidence is recorded as `PASS` without expanding the lesson.

## Final State

A00_L03 is `COMPLETE / FROZEN / READ-ONLY`.

- Architecture Review: `PASS`;
- this guide: `FINAL / PASS`;
- Java verification: `PASS`;
- Simulation verification: `PASS`; and
- real-robot bounded-motion/lifecycle verification: `PASS`.

No new Glass-specific behavior or evidence was introduced; separate Glass
evidence remains `NOT TESTED`.

A00_L03 remains the first authorized nonzero autonomous-motion lesson. A00_L04
is the next authorized roadmap lesson. The bounded robot-relative motion is
followed by the repeating zero-motion safety hold, and the composition retains
Swerve ownership until mode-transition cancellation.

PathPlanner, AutoBuilder, trajectories, pose targeting, field/alliance
transforms, vision, AprilTags, multi-step routines, Test-mode/global gating,
hardware calibration, tuning, and changes to A00_L02 or S00 remain out of scope.
