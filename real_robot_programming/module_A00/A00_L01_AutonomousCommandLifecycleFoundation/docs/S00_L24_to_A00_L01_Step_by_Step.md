# S00_L24 to A00_L01 Step-by-Step Transition Guide

## Transition Identity

- Source lesson: `S00_L24_PoseEstimationAndAutonomousReadiness`
  (`COMPLETE / FROZEN / READ-ONLY`)
- Current lesson: `A00_L01_AutonomousCommandLifecycleFoundation`
  (`COMPLETE / FROZEN / READ-ONLY`)
- Module: `A00 - Autonomous Command Foundation`
- New concept: autonomous command lifecycle and stop ownership
- Current transition status: `FINAL / PASS`
- Real robot: `PASS` for the user-supplied A00_L01 lifecycle/zero-motion
  evidence only
- Git: user-owned; not run by Codex

This guide records the authorized inheritance and the one-concept A00_L01
implementation. Historical S00 transition guides remain unchanged.

## Step 1 - Governance Authorization

**Objective:** Authorize the post-S00 module and lesson boundary.

**Why:** S00 officially ends at L24; A00 must inherit from the frozen L24
snapshot without modifying it.

**Action:** Confirm the A00 roadmap authorization and locked sequence:
`A00_L01`, `A00_L02`, `A00_L03`, and `A00_L04`. Confirm the zero-motion
rule for L01/L02 and the first nonzero-motion boundary at L03.

**Files Changed:** None in S00; the A00 authorization is recorded in the
repository ADR outside this lesson.

**Verification:** A00 roadmap authorization and Frozen Backbone/interface
constraints reviewed.

**Expected Result:** A00_L01 may be created as the first post-S00 lesson.

## Step 2 - Copy the Frozen S00_L24 Project

**Objective:** Create an independent lesson project from the previous frozen
lesson.

**Why:** Governance requires every lesson to inherit from the previous
completed lesson rather than being recreated from scratch.

**Action:** Copy the frozen S00_L24 project into the authorized `module_A00`
location.

**Files Changed:** New A00_L01 project copy; S00_L24 remains unchanged.

**Verification:** Source, tests, Gradle configuration, and inherited
architecture matched the frozen S00_L24 baseline before A00 edits.

**Expected Result:** A00_L01 starts as an independent L24 snapshot.

## Step 3 - Rename the Lesson

**Objective:** Give the copied project its authorized A00 identity.

**Why:** Active metadata must identify the current lesson and must not retain
stale L24 status as its active identity.

**Action:** Rename the project identity to
`A00_L01_AutonomousCommandLifecycleFoundation` and retain S00_L24 as the
previous lesson.

**Files Changed:** A00_L01 project metadata and source-tree identity as
inherited before this documentation normalization.

**Verification:** A00 roadmap naming and repository layout checked.

**Expected Result:** A00_L01 is the active lesson with an independent identity.

## Step 4 - Remove Generated Artifacts

**Objective:** Establish a clean inherited project baseline.

**Why:** Build outputs and Gradle caches are not lesson source and must not
become inherited implementation changes.

**Action:** Remove inherited generated build artifacts before baseline
verification.

**Files Changed:** Generated artifacts only; no S00 source was changed.

**Verification:** Artifact cleanup was supplied as PASS evidence.

**Expected Result:** A00_L01 has a clean source baseline.

## Step 5 - Baseline Java 17 Build

**Objective:** Prove the inherited project builds before adding the A00
concept.

**Why:** A failing inherited baseline would be a migration blocker rather than
an A00 implementation result.

**Action:** Run the baseline Java 17 build on the copied project.

**Files Changed:** None.

**Verification:** User-supplied baseline Java 17 build: `PASS`.

**Expected Result:** The inherited A00_L01 project is buildable before edits.

## Step 6 - Architecture Audit

**Objective:** Confirm the smallest production-useful zero-motion design.

**Why:** A00_L01 must establish lifecycle and stop ownership without
introducing motion, mode composition, or future autonomous frameworks.

**Action:** Audit existing Robot hooks, CommandScheduler behavior,
`SwerveSubsystem.stop()`, command requirements, and the A00 boundaries.

**Files Changed:** None.

**Verification:** Architecture audit approved a bounded
`AutonomousSafetyHoldCommand`; no Robot or RobotContainer change was
authorized.

**Expected Result:** The design preserves the Frozen Backbone and is not a
throwaway demo.

## Step 7 - Design Lock

**Objective:** Lock the exact A00_L01 production and test scope.

**Why:** One lesson introduces one independently verifiable concept.

**Action:** Lock a command with a required `SwerveSubsystem`, finite positive
duration, injected monotonic clock, `initialize() -> stop()`, empty
actuation-free `execute()`, `runsWhenDisabled() == false`, stop on both
termination paths, and fail-closed invalid-clock behavior.

**Files Changed:** None.

**Verification:** The design lock forbids nonzero
`acceptChassisSpeeds(...)`, direct IO, telemetry logic, Robot changes,
RobotContainer changes, and autonomous frameworks.

**Expected Result:** A00_L01 remains strictly zero-motion.

## Step 8 - Implementation

**Objective:** Implement the locked lifecycle command and deterministic tests.

**Why:** The command provides a reusable stop-safe lifecycle boundary for
future autonomous commands.

**Action:** Add `AutonomousSafetyHoldCommand` and its focused test only.

**Files Changed:**

- `src/main/java/frc/robot/commands/AutonomousSafetyHoldCommand.java`
- `src/test/java/frc/robot/commands/AutonomousSafetyHoldCommandTest.java`

**Verification:** Focused command tests passed in the supplied Java 17
verification.

**Expected Result:** Lifecycle, interruption, cancellation, timing failure,
and zero-motion invariants are deterministic and testable.

## Step 9 - Java Verification

**Objective:** Verify the implementation and inherited regression.

**Why:** A00_L01 must not regress frozen drivetrain, localization, or safety
behavior.

**Action:** Run focused tests, full regression, and a clean Java 17 build.

**Files Changed:** None.

**Verification:** User supplied:

- focused `AutonomousSafetyHoldCommandTest`: `PASS`;
- full regression: `PASS`;
- final clean Java 17 build: `PASS`.

**Expected Result:** The zero-motion lifecycle concept passes without
production architecture regressions.

## Step 10 - Simulation Verification

**Objective:** Confirm zero-motion behavior and mode-transition non-regression.

**Why:** A00_L01 is not permitted to generate autonomous motion, so the
simulation gate must prove the absence of motion.

**Action:** Verify the Simulation Disabled baseline, Autonomous Enabled
zero-motion/non-regression behavior, and Teleop fresh-input recovery after
Autonomous/Disabled transition.

**Files Changed:** None.

**Verification:** User-supplied Simulation/Driver Station evidence: all three
cases `PASS`. The command is intentionally not wired into RobotContainer or
autonomous selection in A00_L01.

**Expected Result:** No nonzero autonomous request is generated, existing
teleop behavior remains recoverable, and A00_L03 remains the first permitted
nonzero-motion lesson.

## Documentation Amendment - Real-Robot Verification

**Objective:** Record the user-supplied A00_L01 hardware evidence without
changing the frozen implementation, architecture, or verification scope.

**Evidence:**

1. **Disabled baseline:** The robot was Disabled and the drivetrain remained
   stationary. Drive and steer applied outputs and velocities were zero, and
   module/gyro connectivity and configuration were healthy.
2. **Autonomous + Enabled zero-motion:** Driver Station Autonomous + Enabled
   was held for approximately 51 seconds. Drive and steer applied outputs and
   velocities remained zero, with no autonomous drivetrain motion observed.
3. **Autonomous -> Disabled:** The drivetrain remained at zero and no stale
   output reappeared.
4. **Autonomous -> Teleoperated:** After transitioning through Disabled into
   Teleop Enabled, neutral driver input produced zero drive/steer output and
   no autonomous output persisted.
5. **Autonomous -> Test:** After transitioning through Disabled into Test
   Enabled, no test or commissioning command was intentionally activated; no
   autonomous drivetrain motion persisted and zero-motion safety was
   preserved.

**Scope boundary:** This evidence is limited to A00_L01 lifecycle and
zero-motion hardware behavior. It does not verify A00_L02 scheduler ownership
or repeating autonomous ownership, A00_L03 bounded motion, A00_L04 mode
gating, pose/odometry/estimator behavior, PathPlanner, AutoBuilder, or
autonomous competition readiness.

**Files Changed:** Documentation only: the A00_L01 README, lesson plan,
checklist, status, and this transition guide.

**Verification:** Documentation consistency audit completed. A00_L01 remains
`COMPLETE / FROZEN / READ-ONLY`; source, tests, Gradle, hardware
configuration, and frozen predecessor lessons remain unchanged.

**Expected Result:** The supplied A00_L01 real-robot lifecycle/zero-motion
evidence is recorded as `PASS` without expanding the lesson's concept or
claiming later autonomous capabilities.

## Current Closure State

A00_L01 implementation, supplied verification, architecture review, and
documentation are complete for the locked zero-motion scope. The lesson is
`COMPLETE / FROZEN / READ-ONLY`. The recorded real-robot PASS is limited to
the supplied A00_L01 lifecycle/zero-motion evidence. A00_L02 remains
zero-motion and A00_L03 remains the first lesson permitted to issue nonzero
autonomous drivetrain motion.
