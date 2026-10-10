# M01_L01 to M01_L02 — Step-by-Step Transition

Status: IN_PROGRESS / NOT FINAL
Donor: M01_L01_MechanismHardwareReadinessAndIOContract — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED
Donor path: real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract
Target: M01_L02_IntakeRealIOConfigurationAndSafeStop
Target path: real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop
Title: Intake Real IO Configuration and Safe Stop
Governance model: ADR_GOV3 three commit groups; current group: GROUP 1 — ACTIVATE / BASELINE
Design Lock: APPROVED
Group 2: NOT AUTHORIZED
Powered hardware: NOT AUTHORIZED

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

## Planned Group 2 steps — NOT AUTHORIZED

The following are planned from the Design Lock and grant no permission. Each will be recorded with all seven fields when authorized and performed.

1. Add IntakeConstants to Constants.java.
2. Create IntakeIOReal with NeutralOut-only output, configuration apply/readback/health and truthful inputs.
3. Add IntakeIORealConfigurationTest (S1–S3).
4. Select IntakeIOReal on the real robot in RobotContainer.
5. Extend IntakeArchitectureBoundaryTest (S4) and run inherited regression (S5).
6. User Simulation and, after separate Architect authorization, User hardware checks H1–H3.
