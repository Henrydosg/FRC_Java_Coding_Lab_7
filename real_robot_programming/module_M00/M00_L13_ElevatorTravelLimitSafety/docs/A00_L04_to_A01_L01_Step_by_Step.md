# A00_L04 to A01_L01 - Step-by-Step Transition Guide

## Transition Identity

- Source lesson: `A00_L04_AutonomousMotionSafetyGating`
  (`COMPLETE / FROZEN / READ-ONLY`)
- Current lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract`
- Module: `A01 - Autonomous Navigation and Path Following`
- New concept: autonomous starting-pose and field-frame contract
- Active state: `COMPLETE / FROZEN / READ-ONLY`
- Guide state: `FINAL / PASS`
- Real Robot: `PASS` for the seven supplied A01_L01 cases only
- Git: user-owned; not run by Codex

This guide records the controlled inheritance transition from frozen A00_L04
to the completed, frozen A01_L01 lesson and its final verification evidence.

## Step 1 - Governance Authorization

**Objective:** Establish the authoritative A01_L01 identity, predecessor, and
single concept.

**Why:** A01_L01 must inherit the frozen A00_L04 boundary and must not be
represented as a copied A00 lesson.

**Action:** Apply the approved A01 roadmap ADR. Use the exact lesson identity
`A01_L01_AutonomousStartingPoseAndFieldFrameContract`, predecessor
`A00_L04_AutonomousMotionSafetyGating`, and concept of authoritative
reference-frame initialization from a validated starting pose and heading.

**Files Changed:** A01_L01 documentation identity fields.

**Verification:** The approved ADR establishes the title, predecessor,
prerequisite, required verification gates, and exclusions.

**Expected Result:** A01_L01 is recognized as the first active A01 lesson.

## Step 2 - Inheritance Preparation

**Objective:** Prepare A01_L01 from the frozen A00_L04 project.

**Why:** Inheritance preserves the tested autonomous lifecycle, centralized
stop authority, and frozen package/interface contracts.

**Action:** Copy the completed A00_L04 project into the A01_L01 location,
remove generated build artifacts as required by the lesson workflow, preserve
the inherited project configuration, and keep A00_L04 read-only.

**Files Changed:** A01_L01 project copy only; no A00_L04 files.

**Verification:** A01_L01 identifies A00_L04 as its immediate frozen predecessor
and preserves the Frozen Backbone, Frozen Interface Contract, and
`RobotContainer` composition-root rule.

**Expected Result:** A01_L01 has a valid frozen inheritance baseline.

## Step 3 - Baseline Build

**Objective:** Verify that the inherited A01_L01 project is buildable before
the new concept is evaluated.

**Why:** A lesson cannot attribute later verification to an unverified
predecessor baseline.

**Action:** Run the inherited Java 17 baseline and the required clean/full
build workflow during implementation.

**Files Changed:** None by verification.

**Verification:** Java 17 is verified; clean build, full tests, and full build
are recorded as `PASS`.

**Expected Result:** The inherited project is a verified foundation for A01_L01
work.

## Step 4 - Architecture Audit

**Objective:** Confirm that the starting-pose concept fits the approved A01
roadmap without expanding scope.

**Why:** Starting-pose and field-frame initialization is a prerequisite for
later navigation lessons and must remain independent of trajectories,
PathPlanner, vision, and mechanism integration.

**Action:** Audit the source, tests, frozen predecessor, and A01 ADR for
ownership, scheduler lifecycle, safety gating, localization reset behavior,
and exclusions.

**Files Changed:** None by the audit.

**Verification:** Architecture Audit is `PASS`; A00_L04's
Autonomous+Enabled invariant, centralized `SwerveSubsystem.stop()`, and
frozen boundaries remain authoritative.

**Expected Result:** The lesson has one independently verifiable architectural
concept and no A01_L02 or later capability claim.

## Step 5 - Design Lock

**Objective:** Lock the smallest scheduler-correct starting-pose authorization
design before implementation.

**Why:** Autonomous motion must fail closed without a fresh accepted reset and
must not restart from stale readiness.

**Action:** Preserve the Disabled-only `ResetKnownFieldPoseCommand`, its
one-shot accepted-start-pose consumption, and composition-root integration with
the inherited A00_L04 autonomous gate.

**Files Changed:** Design decision and implementation planning only.

**Verification:** Design Lock is recorded as complete for the approved L01
scope; no manual child-command lifecycle delegation or new architecture
boundary is introduced.

**Expected Result:** Implementation can prove valid reset, fail-closed startup,
one-shot consumption, and safe mode transitions.

## Step 6 - A01_L01 Implementation

**Objective:** Implement only authoritative starting-pose and field-frame
initialization.

**Why:** The new lesson must add one concept while retaining inherited
autonomous safety behavior.

**Action:** Implement the approved reset/readiness behavior, expose the reset
command for operator use, preserve subsystem-owned localization and stop
authority, and add deterministic tests.

**Files Changed:** A01_L01 production source and tests during the implementation
phase; unchanged by this documentation normalization.

**Verification:** Source and tests implement the A01_L01 contract. No
PathPlanner, AutoBuilder, trajectory-following, vision, or new mechanism
architecture is introduced.

**Expected Result:** A01_L01 provides validated starting-pose initialization
without claiming later A01 capabilities.

## Step 7 - Java and Deterministic Verification

**Objective:** Verify implementation behavior under Java 17 and the scheduler
test fixtures.

**Why:** The lesson must distinguish source/test verification from hardware
evidence.

**Action:** Run focused deterministic tests, full regression, and the clean
build workflow.

**Files Changed:** None by verification.

**Verification:** Java 17 is `VERIFIED`; focused tests, full tests, clean build,
and full build are `PASS`.

**Expected Result:** The A01_L01 implementation passes its software
verification gates.

## Step 8 - Simulation and Real-Robot Verification

**Objective:** Verify the starting-pose contract in Simulation and on the
real robot within the authorized scope.

**Why:** Simulation precedes hardware, and real-robot evidence must remain
limited to the lesson concept.

**Action:** Verify valid and invalid starting-pose availability, Disabled-only
reset, autonomous fail-closed behavior, one-shot authorization, mode
transitions, and enabled reset rejection.

**Files Changed:** None by verification.

**Verification:** Simulation is `PASS`. Driver Station / Glass is `PASS` for
disabled runtime telemetry, valid observable Pose and EstimatedPose, runtime
pose update after movement, visible Disabled starting-pose reset, and safe
zero drivetrain output when expected. Real Robot is `PASS` for Cases 1-7:
Disabled baseline; autonomous without a fresh reset; Disabled reset;
fresh reset followed by one Autonomous run; second Autonomous enable without a
reset; a new Disabled reset followed by a new Autonomous session; and an
Enabled Teleop reset attempt that leaves localization unchanged.

**Expected Result:** The starting-pose and field/reference-frame contract is
verified without claiming trajectory following or competition readiness.

## Step 9 - Final Documentation Freeze

**Objective:** Finalize the documentation after the final A01_L01 architecture
review and all verification gates passed.

**Why:** A completed lesson must preserve its evidence and become an immutable
read-only snapshot.

**Action:** Update the lesson status, plan, checklist, README, transition guide,
and root README implementation-state registration to record the final review,
Driver Station / Glass evidence, and frozen state.

**Files Changed:**

- Root `README.md`
- A01_L01 `README.md`
- A01_L01 `LESSON_PLAN.md`
- A01_L01 `LESSON_STATUS.md`
- A01_L01 `LESSON_CHECKLIST.md`
- `docs/A00_L04_to_A01_L01_Step_by_Step.md`

**Verification:** Documentation records `COMPLETE / FROZEN / READ-ONLY`,
Architecture Review `PASS`, Driver Station / Glass `PASS`, Simulation `PASS`,
Real Robot `PASS`, and Transition Guide `FINAL / PASS`. The E-Stop and
CommandScheduler loop-overrun observations remain non-blocking follow-up items
only.

**Expected Result:** A01_L01 is a complete frozen read-only lesson with no
expanded scope claims.

## Scope and Known Issues

A01_L01 proves only validated autonomous starting-pose and
field/reference-frame initialization. It does not prove pose-target control,
trajectory generation or following, alliance transforms, PathPlanner,
AutoBuilder, vision, mechanism events, multi-step routines, or competition
readiness.

A temporary Driver Station Spacebar E-Stop and CommandScheduler loop-overrun
observations remain documented as non-blocking follow-up items. No A01_L01
defect is claimed from those observations without further evidence.

## Current State

- Lesson: `COMPLETE / FROZEN / READ-ONLY`
- Architecture Review: `PASS`
- Driver Station / Glass: `PASS`
- Transition Guide: `FINAL / PASS`
- Simulation: `PASS`
- Real Robot: `PASS` for Cases 1-7 only
- Freeze State: `FROZEN`

A01_L01 is `COMPLETE / FROZEN / READ-ONLY`.
