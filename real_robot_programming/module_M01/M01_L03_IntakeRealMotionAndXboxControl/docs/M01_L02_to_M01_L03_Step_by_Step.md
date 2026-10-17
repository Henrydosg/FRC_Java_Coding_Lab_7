# M01_L02 to M01_L03 — Step-by-Step Transition

Status: IN_PROGRESS / NOT FINAL
Donor: M01_L02_IntakeRealIOConfigurationAndSafeStop — COMPLETE / FROZEN / READ-ONLY; User-owned publication commit ed6653e
Donor path: real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop
Target: M01_L03_IntakeRealMotionAndXboxControl
Target path: real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl
Title: Intake Real Motion and Xbox Control
Governance model: ADR_GOV3 three commit groups; current group: GROUP 1 — ACTIVATE / BASELINE
Design Lock: CONDITIONAL PASS
Group 2: NOT AUTHORIZED
Powered hardware / nonzero Intake output: NOT AUTHORIZED

[Current state](../../../../docs/governance/CURRENT_STATE.md), [M01 Fast-Track roadmap revision](../../../../docs/architecture_decisions/ADR_M01_Fast_Track_10_Lesson_Roadmap_Revision.md), [ADR_GOV3](../../../../docs/architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md) and [lesson plan](../LESSON_PLAN.md) govern applicability.

## Step 1 — Copy, rename and clean the frozen donor

- **Objective:** Create the independent M01_L03 WPILib project.
- **Why:** One lesson is one independent project inherited from the frozen predecessor.
- **Action:** User copied M01_L02 to the target path, renamed it and removed generated artifacts.
- **Files Changed:** New untracked target directory only; donor unchanged.
- **Verification:** Supplied User evidence.
- **Expected Result:** Target contains the complete inherited project.

## Step 2 — Run the inherited baseline build

- **Objective:** Prove the inherited snapshot builds before any change.
- **Why:** A baseline separates inherited failures from future lesson changes.
- **Action:** User ran the clean Gradle baseline in the target.
- **Files Changed:** Generated build outputs only (git-ignored).
- **Verification:** BUILD SUCCESSFUL in 1m 5s; 7 actionable tasks (6 executed, 1 up-to-date) — supplied User evidence.
- **Expected Result:** Baseline PASS.

## Step 3 — Audit inheritance and draft the targeted Design Lock

- **Objective:** Confirm exact inheritance and design the L03 safety and motion contract.
- **Why:** Design must start from verified inherited bytes and existing ownership.
- **Action:** Engineer read-only inheritance audit and "M01_L03 TARGETED DESIGN LOCK REPORT"; Codex design review (F01, F03, F04, F05 CONFIRMED; F02 PARTIAL).
- **Files Changed:** None.
- **Verification:** 349/349 donor tracked files byte-identical; no other non-generated difference; no nested duplicate. Static engineer audit.
- **Expected Result:** Inheritance PASS; Design Lock ready for Architect review.

## Step 4 — Architect Design Lock decision

- **Objective:** Fix the design decisions before implementation.
- **Why:** Architecture review precedes implementation (ES-01 Step 2).
- **Action:** Architect issued CONDITIONAL PASS with constraints D1, D2, provisional voltages, B-button reverse, persistent fatal latch and inversion TBD, and authorized Group 1 activation recording (AUTH_M01_L03_GROUP1_ACTIVATION_RECORDING).
- **Files Changed:** None.
- **Verification:** Architect brief supplied by the User on 2026-10-17.
- **Expected Result:** Group 1 recording permitted; Group 2 and powered hardware not authorized.

## Step 5 — Record activation

- **Objective:** Make the IN_PROGRESS state and Design Lock reviewable.
- **Why:** Copied M01_L02 lifecycle identity cannot serve as M01_L03 state.
- **Action:** Prepend the M01_L03 operative record to the four lesson lifecycle documents with preserved donor bodies; create this guide; update CURRENT_STATE and archive its prior text in the named history event.
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L02_to_M01_L03_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/M01_L03_GROUP1_ACTIVATION_RECORDING_2026-10-17.md.
- **Verification:** Static consistency, preserved donor bytes and unchanged technical inheritance; self-review is not Architect acceptance.
- **Expected Result:** M01_L03 sole IN_PROGRESS lesson; Group 1 ready for Architect review and User publication.

## Planned Group 2 gates — NOT AUTHORIZED

The following follow the Fast-Track L03 gate order and grant no permission. Each step will be recorded with all seven fields when authorized and performed.

1. Measurement validity — measured Intake inputs with units, freshness and validity.
2. AUD-P1-02 — all-actuator scheduler-fatal stop fan-out and exception-safe command cleanup.
3. Bounded Test-mode commissioning (±1.5 V, 1.0 s); direction/inversion and observed stopping.
4. Xbox Right Bumper held-forward (+4.0 V) and release-stop.
5. Xbox B held-reverse (−3.0 V) only if supported by accepted evidence.
6. User Simulation and, after separate Architect authorization, User hardware verification.
