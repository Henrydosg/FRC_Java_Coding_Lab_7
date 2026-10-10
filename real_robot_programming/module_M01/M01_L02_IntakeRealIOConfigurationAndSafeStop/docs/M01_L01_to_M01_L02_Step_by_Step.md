# M01_L01 to M01_L02 — Step-by-Step Transition

Status: IN_PROGRESS / NOT FINAL
Donor: M01_L01_MechanismHardwareReadinessAndIOContract — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED
Donor path: real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract
Target: M01_L02_IntakeRealIOConfigurationAndSafeStop
Target path: real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop
Title: Intake Real IO Configuration and Safe Stop
Governance model: ADR_GOV3 three commit groups; current group: GROUP 2 — IMPLEMENT / VERIFY
Design Lock: APPROVED
Group 2: AUTHORIZED — implementation recorded; User build/tests and Noop Simulation ACCEPTED; publication PENDING; NOT COMPLETE
Powered hardware: NOT AUTHORIZED
Group 3: NOT AUTHORIZED

[Current state](../../../../docs/governance/CURRENT_STATE.md), [registered M01 roadmap](../../../../docs/architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md), [ADR_GOV3](../../../../docs/architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md) and [lesson plan](../LESSON_PLAN.md) govern applicability.

## Step 1 — Authorize Group 1

- **Objective:** Establish exact Group 1 permission.
- **Why:** The roadmap and GOV3 do not activate a lesson by themselves.
- **Action:** Architect authorized M01_L02 Group 1 with exact donor, target and Primary Engineer Claude Code.
- **Files Changed:** None.
- **Verification:** Architect authorization supplied by the User on 2026-10-09.
- **Expected Result:** User preparation and engineer audit permitted; Group 2 and powered hardware not authorized.

## Step 2 — Copy, rename and clean the frozen donor

- **Objective:** Create the independent M01_L02 WPILib project.
- **Why:** One lesson is one independent project inherited from the frozen predecessor.
- **Action:** User copied M01_L01 to the target path, renamed it and removed generated artifacts.
- **Files Changed:** New untracked target directory only; donor unchanged.
- **Verification:** Supplied User evidence.
- **Expected Result:** Target contains the complete inherited project.

## Step 3 — Run the inherited baseline build

- **Objective:** Prove the inherited snapshot builds before any change.
- **Why:** A baseline separates inherited failures from future lesson changes.
- **Action:** User ran the clean Gradle baseline in the target.
- **Files Changed:** Generated build outputs only (git-ignored).
- **Verification:** BUILD SUCCESSFUL in 42s; 7 tasks (6 executed, 1 up-to-date) — supplied User evidence.
- **Expected Result:** Baseline PASS.

## Step 4 — Audit inheritance and architecture

- **Objective:** Confirm exact inheritance and identify reusable Intake contracts.
- **Why:** Design must start from verified inherited bytes and existing ownership.
- **Action:** Engineer compared all donor tracked files with the target and inspected IntakeIO, IntakeIONoop, IntakeSubsystem, IntakeObservation, Intake commands, RobotContainer, Constants and Intake tests.
- **Files Changed:** None.
- **Verification:** 344/344 donor tracked files byte-identical; only difference is git-ignored donor .Glass/ GUI state; no nested duplicate. Static engineer audit.
- **Expected Result:** Inheritance PASS; existing IntakeIO contract sufficient; single composition edit point identified.

## Step 5 — Record the Architect-approved Design Lock and activate the lesson

- **Objective:** Make the approved design and IN_PROGRESS state reviewable.
- **Why:** Copied M01_L01 lifecycle identity cannot serve as M01_L02 state.
- **Action:** Initialize the four lesson lifecycle documents and this guide with preserved donor bodies; update CURRENT_STATE and archive its prior text in the named history event.
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L01_to_M01_L02_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/M01_L02_GROUP1_ACTIVATION_RECORDING_2026-10-09.md.
- **Verification:** Static consistency, preserved donor bytes and unchanged technical inheritance; self-review is not Architect acceptance.
- **Expected Result:** M01_L02 sole IN_PROGRESS lesson; Group 1 ready for Architect review and User publication.

## Group 2 — IMPLEMENT / VERIFY

Group 2 steps below were performed by the Primary Engineer within the Architect-authorized Design Lock scope. Each step diff was self-reviewed by the engineer under User delegation; self-review is not independent review. Engineer-run test and build results are preliminary convenience evidence (ADR_GOV3 §8), not User-owned verification. The registered 18-lesson M01 roadmap remains controlling; L02 scope is Intake Real IO configuration and safe zero output only.

## Step 6 — Authorize Group 2

- **Objective:** Establish exact Group 2 implementation permission.
- **Why:** The Design Lock recorded the future Group 2 scope but did not authorize it.
- **Action:** Architect authorized Group 2 software implementation within the locked scope, Primary Engineer Claude Code; powered hardware remained NOT AUTHORIZED.
- **Files Changed:** None.
- **Verification:** Architect authorization supplied by the User in chat.
- **Expected Result:** Engineer may implement the locked Group 2 files and run software-only builds/tests.

## Step 7 — Add IntakeConstants

- **Objective:** Give the Intake adapter one vendor-neutral configuration source.
- **Why:** Constants.java is the default configuration authority; the adapter must not contain magic numbers.
- **Action:** Added `Constants.IntakeConstants`: `kMotorCanId = 40`, `kStatorCurrentLimitAmps = 40.0` and enabled, `kSupplyCurrentLimitAmps = 35.0` and enabled, `kBrakeWhenNeutral = true`.
- **Files Changed:** src/main/java/frc/robot/Constants.java (IntakeConstants only).
- **Verification:** Engineer-run compile; values match the Design Lock.
- **Expected Result:** Approved values exist in one place; no other constant changed.

## Step 8 — Create IntakeIOReal

- **Objective:** Provide the concrete Real Intake adapter with configuration, readback and safe zero output.
- **Why:** Roadmap L02 requires a qualified Real IO configuration while the Intake must still never move.
- **Action:** Created IntakeIOReal implementing the unchanged IntakeIO contract: constructs one TalonFX on CAN 40; issues NeutralOut, applies `createConfiguration()` (Brake, Stator 40 A, Supply 35 A), refreshes the configuration and checks it with `isConfigurationHealthy()`, then issues NeutralOut again; reports a configuration failure once and fails closed for the session without retry; `updateInputs()` maps health and a fresh supply-voltage signal refresh through `fillInputs()` so connected implies available; `requestIntake()` and `stop()` both issue NeutralOut only.
- **Files Changed:** src/main/java/frc/robot/io/intake/IntakeIOReal.java (new).
- **Verification:** Engineer-run compile; static review: the only control request imported is NeutralOut.
- **Expected Result:** Vendor API confined to the concrete adapter; no nonzero output path exists.

## Step 9 — Add IntakeIORealConfigurationTest

- **Objective:** Verify configuration content, health decision and input mapping without hardware (S1–S3).
- **Why:** The adapter's decision logic must be proven before any powered check.
- **Action:** Added 14 tests: approved configuration only; inversion and feedback at Phoenix defaults; healthy only when apply, refresh and readback all succeed; unhealthy for apply failure, readback failure, neutral-mode mismatch, stator limit mismatch or disabled, supply limit mismatch or disabled; healthy+connected, healthy+disconnected and unhealthy input mappings; every mapping satisfies the Observation invariant.
- **Files Changed:** src/test/java/frc/robot/io/intake/IntakeIORealConfigurationTest.java (new).
- **Verification:** 14/14 PASS — engineer-run, preliminary.
- **Expected Result:** S1–S3 covered in software.

## Step 10 — Select IntakeIOReal on the real robot

- **Objective:** Use the Real adapter only on the robot.
- **Why:** RobotContainer is the composition root; off-robot builds, tests and Simulation must keep IntakeIONoop.
- **Action:** Changed the Intake construction to `new IntakeSubsystem(RobotBase.isReal() ? new IntakeIOReal() : new IntakeIONoop())`; the formatter wraps it after `intakeSubsystem =` (RobotContainer.java lines 152–153).
- **Files Changed:** src/main/java/frc/robot/RobotContainer.java (Intake selection and its import only).
- **Verification:** Engineer-run compile; the full suite then exposed one inherited assertion failure (Step 11).
- **Expected Result:** Real robot selects IntakeIOReal; every other environment keeps IntakeIONoop.

## Step 11 — Bounded inherited test contract repair

- **Objective:** Align one inherited source-text assertion with the authorized composition change.
- **Why:** RobotContainerIntakeCommandBindingTest pinned the former Noop-only line `intakeSubsystem = new IntakeSubsystem(new IntakeIONoop())`, which the authorized Step 10 change necessarily replaces.
- **Action:** Changed only the assertion at test line 26 to `new IntakeSubsystem(RobotBase.isReal() ? new IntakeIOReal() : new IntakeIONoop())`. Disclosed wording deviation: the assertion matches the actual wrapped source text at RobotContainer.java line 153 and therefore omits the `intakeSubsystem = ` prefix. The other two tests of the class are unchanged.
- **Files Changed:** src/test/java/frc/robot/RobotContainerIntakeCommandBindingTest.java (one assertion).
- **Verification:** Focused 3/3 PASS — engineer-run, preliminary. Architect ACCEPTED the bounded repair and the assertion wording deviation.
- **Expected Result:** Inherited binding contract still guards a single RunIntakeCommand on the existing subsystem.

## Step 12 — Extend architecture boundary guards and run regression

- **Objective:** Enforce the Real IO boundaries permanently (S4) and confirm no inherited regression (S5).
- **Why:** Zero output and vendor confinement must stay true in later edits, not only at review time.
- **Action:** Added three IntakeArchitectureBoundaryTest guards: `vendorImportsAreConfinedToIntakeIOReal`, `intakeIORealIssuesNeutralOutputOnly`, `robotContainerSelectsRealIntakeOnlyOnTheRealRobot`. Ran the full suite and Gradle build with JAVA_HOME set to the WPILib 2026 JDK.
- **Files Changed:** src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java.
- **Verification:** Full suite 895/895 PASS; BUILD SUCCESSFUL — engineer-run, preliminary.
- **Expected Result:** S4 and S5 covered in software; User authoritative verification still required.

## Step 13 — Record Group 2 implementation documentation

- **Objective:** Make the implementation and its evidence reviewable without overstating it.
- **Why:** The Architect requested Group 2 Step 6 documentation; lifecycle state must not advance prematurely.
- **Action:** Recorded Steps 6–13 in this guide and the Group 2 record in LESSON_STATUS.md and LESSON_CHECKLIST.md.
- **Files Changed:** docs/M01_L01_to_M01_L02_Step_by_Step.md, LESSON_STATUS.md, LESSON_CHECKLIST.md.
- **Verification:** Engineer self-review; governance mirror validator run; Architect review PENDING.
- **Expected Result:** Documentation truthfully shows implementation done, verification NOT COMPLETE.

## Step 14 — User authoritative build and tests

- **Objective:** Replace preliminary engineer-run results with User-owned software verification.
- **Why:** Engineer-run builds/tests are convenience evidence only (ADR_GOV3 §8); gates require User verification.
- **Action:** User ran `.\gradlew clean build` in the M01_L02 lesson directory with JAVA_HOME set to the WPILib 2026 JDK (JDK 17).
- **Files Changed:** None (generated build outputs only, git-ignored).
- **Verification:** BUILD SUCCESSFUL in 34s; 7 actionable tasks: 7 executed; 895 tests, 0 failures, 0 errors, 0 skipped (counts read read-only from the User-generated Gradle test report). Architect ACCEPTED, 2026-10-10.
- **Expected Result:** BUILD VERIFIED and SOFTWARE TEST VERIFIED (S1–S5).

## Step 15 — User Simulation (Noop scope)

- **Objective:** Confirm the off-robot composition still selects IntakeIONoop and the Intake command path works.
- **Why:** Simulation precedes any powered check; it exercises Command → Subsystem → IO without hardware.
- **Action:** User ran WPILib Simulation in Teleoperated with Keyboard 2 on Joystick[0] (no physical controller); PageDown = button 6 = Right Bumper.
- **Files Changed:** None (Sim GUI state files are git-ignored).
- **Verification:** Hold: Intake RequestedState INTAKE_REQUESTED; release: STOPPED; Available=false, Connected=false (truthful IntakeIONoop values); no console errors reported. Architect ACCEPTED, 2026-10-10.
- **Expected Result:** SIMULATION VERIFIED — NOOP SCOPE ONLY. IntakeIOReal was not simulated; its logic is covered by S2/S3.

## Step 16 — Record accepted User verification and prepare Group 2 publication

- **Objective:** Record the accepted User evidence and current state for one User-owned Group 2 publication.
- **Why:** Obsolete "Group 2 NOT AUTHORIZED" claims must not remain operative, and the prior cursor must be preserved.
- **Action:** Updated the M01_L02 operative records and CURRENT_STATE; archived the prior CURRENT_STATE verbatim in a new history event.
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L01_to_M01_L02_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/M01_L02_GROUP2_USER_VERIFICATION_RECORDING_2026-10-10.md.
- **Verification:** Engineer self-review; governance mirror validator; Architect review PENDING.
- **Expected Result:** Group 2 ready for User-owned publication; Group 2 NOT COMPLETE; lesson IN_PROGRESS.

## Remaining steps — PENDING

The following are planned and grant no permission. Each will be recorded with all seven fields when performed.

1. Architect review of the Group 2 recording, then User-owned Group 2 implementation publication.
2. After separate Architect authorization only: User powered checks H1–H3 — NOT AUTHORIZED.
3. Group 3 closure — NOT AUTHORIZED.
