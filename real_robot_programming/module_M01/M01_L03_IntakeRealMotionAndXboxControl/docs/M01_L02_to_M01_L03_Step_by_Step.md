# M01_L02 to M01_L03 — Step-by-Step Transition

Status: FINAL / PASS
Donor: M01_L02_IntakeRealIOConfigurationAndSafeStop — COMPLETE / FROZEN / READ-ONLY; User-owned publication commit ed6653e
Donor path: real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop
Target: M01_L03_IntakeRealMotionAndXboxControl
Target path: real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl
Title: Intake Real Motion and Xbox Control
Governance model: ADR_GOV3 three commit groups; current group: GROUP 3 — COMPLETE / FREEZE
Design Lock: CONDITIONAL PASS — constraints implemented
Group 2: Checkpoints A–E implemented and accepted; User-owned publication PENDING
Lesson: COMPLETE / FROZEN / READ-ONLY (Group 3 recording 2026-10-26); User-owned publication PENDING

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

## Step 6 — Checkpoint A: measured Intake feedback

- **Objective:** Read truthful Kraken X44 feedback without enabling motion.
- **Why:** Measurement trust precedes motion (Fast-Track L03 gate 1).
- **Action:** Added position, velocity, stator/supply current and motor voltage (rotor units) with measurementValid and NaN-when-invalid through IntakeIOInputs, IntakeObservation and Intake telemetry; 50 Hz signal rate (AUTH_M01_L03_G2_A_MEASURED_FEEDBACK_IMPLEMENTATION).
- **Files Changed:** IntakeIO, IntakeIONoop, IntakeIOReal, IntakeObservation, IntakeSubsystem, IntakeTelemetryFacade and their focused tests.
- **Verification:** User BUILD SUCCESSFUL in 49s; 953/953 PASS. Codex review HOLD on F-A4.
- **Expected Result:** Valid measurements published; outputs still NeutralOut only.

## Step 7 — F-A4: temporal freshness repair

- **Objective:** Bound sample age for every measured signal.
- **Why:** A non-blocking refresh status does not prove sample age (OC-01 §6).
- **Action:** Phoenix System timestamps checked against the status-signal clock with a 100 ms maximum age; timestampSeconds of the oldest accepted sample in robot FPGA time (AUTH_M01_L03_G2_A_F_A4_BOUNDED_REPAIR).
- **Files Changed:** IntakeIO, IntakeIONoop, IntakeIOReal, IntakeObservation, IntakeSubsystem and focused tests.
- **Verification:** User BUILD SUCCESSFUL in 33s; 967/967 PASS; Codex PASS.
- **Expected Result:** Stale, future or invalid samples make the whole measurement invalid.

## Step 8 — Checkpoint B: AUD-P1-02 safety implementation

- **Objective:** Satisfy AUD-P1-02 before any nonzero output.
- **Why:** Fast-Track L03 gate 2.
- **Action:** Scheduler-fatal fan-out to every mechanism after the Swerve path; Intake session fault latch; non-throwing Intake stop and IntakeToFeederCommand cleanup; CTRE neutral StatusCode checks (AUTH_M01_L03_G2_B_AUD_P1_02_SAFETY_IMPLEMENTATION).
- **Files Changed:** RobotContainer, IntakeToFeederCommand, IntakeSubsystem, IntakeIOReal, IntakeObservation, IntakeTelemetryFacade and focused tests.
- **Verification:** User BUILD SUCCESSFUL in 39s; 980/980 PASS. Codex HOLD_AUD_P1_02 (H1, H2).
- **Expected Result:** Fatal faults stop all actuators and inhibit Intake output until restart.

## Step 9 — H1/H2 bounded safety repair

- **Objective:** Close the two Codex P1 findings.
- **Why:** H1 — teleop drive cleanup could throw; H2 — positive CTRE warnings were accepted.
- **Action:** FieldRelativeTeleopDriveCommand.end() no longer throws and inhibits later drive intent after a cleanup failure; NeutralOut accepted only on StatusCode.OK; construction rejection fails closed (AUTH_M01_L03_G2_B_H1_H2_BOUNDED_REPAIR).
- **Files Changed:** FieldRelativeTeleopDriveCommand, IntakeIOReal and focused tests.
- **Verification:** User BUILD SUCCESSFUL in 39s; 990/990 PASS; Codex PASS_H1_H2_REPAIR.
- **Expected Result:** AUD-P1-02 SOFTWARE CLOSED.

## Step 10 — Checkpoint C: bounded Test-mode commissioning software

- **Objective:** Provide the first nonzero Intake output path, Test mode only.
- **Why:** Fast-Track L03 gate 3.
- **Action:** Additive IntakeIO.setVoltage; VoltageOut with FOC off; ±6.0 V peak configuration and readback; subsystem-owned admission; IntakeCommissioningCommand ±1.5 V for at most 1.0 s; dashboard entry points (AUTH_M01_L03_G2_C_BOUNDED_COMMISSIONING_SOFTWARE).
- **Files Changed:** Constants, IntakeIO, IntakeIOReal, IntakeSubsystem, IntakeObservation, IntakeCommissioningCommand (new), RobotContainer and focused tests.
- **Verification:** User 1013/1013 PASS; Codex PASS.
- **Expected Result:** Only explicit Test-mode commissioning can produce output.

## Step 11 — Checkpoint C hardware commissioning

- **Objective:** Confirm direction and bounded stop on the real Intake.
- **Why:** Simulation and tests cannot prove physical direction.
- **Action:** User ran forward and reverse commissioning on the robot.
- **Files Changed:** None.
- **Verification:** REAL HARDWARE FUNCTIONAL VERIFIED — Kraken X44 CAN 40 connected; feedback valid; +1.5 V and −1.5 V rotate in opposite correct directions; both stop after approximately 1 s (operator observation); repeat works; Disabled prevents commissioning. Accepted by the Architect.
- **Expected Result:** Default inversion accepted; manual control may be implemented.

## Step 12 — Checkpoint D/E: Xbox forward / stop / reverse software

- **Objective:** Qualify held-button manual Intake control.
- **Why:** Fast-Track L03 gates 4 and 5.
- **Action:** IntakeSubsystem.startManual (Teleop-only, valid feedback, no fault, approved voltages); Right Bumper +4.0 V and Xbox B −3.0 V through RunIntakeCommand; later press wins; legacy requestIntake() stays NeutralOut for autonomous (AUTH_M01_L03_G2_D_E_XBOX_MANUAL_CONTROL_SOFTWARE).
- **Files Changed:** Constants, IntakeSubsystem, RunIntakeCommand, RobotContainer, IntakeObservation and focused tests.
- **Verification:** User final regression 1031/1031 PASS (0 failures, 0 errors, 0 skipped); Codex PASS_M01_L03_D_E.
- **Expected Result:** Manual output only while a button is held in Teleop.

## Step 13 — Checkpoint D/E hardware functional check

- **Objective:** Confirm manual forward and reverse on the real robot.
- **Why:** Gate 6 User hardware verification.
- **Action:** User operated the Intake with a Microsoft Xbox Controller; an earlier Logitech mapping discrepancy was resolved operationally without code change.
- **Files Changed:** None.
- **Verification:** REAL HARDWARE FUNCTIONAL VERIFIED — Right Bumper forward, release stops; B reverse, release stops. Accepted by the Architect.
- **Expected Result:** Lesson ready for closure; fault scenarios remain NOT TESTED on hardware.

## Step 14 — Group 3 closure recording

- **Objective:** Record COMPLETE / FROZEN / READ-ONLY and the evidence limits.
- **Why:** ADR_GOV3 §4 Group 3.
- **Action:** Updated the four lesson lifecycle documents, finalized this guide, updated CURRENT_STATE and added the closure event (AUTH_M01_L03_GROUP3_FINAL_LIFECYCLE_CLOSURE).
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L02_to_M01_L03_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/M01_L03_GROUP3_CLOSURE_RECORDING_2026-10-26.md.
- **Verification:** Static consistency; preserved donor bytes; no source or test change in this step; self-review is not Architect acceptance.
- **Expected Result:** No active editable lesson; User-owned Group 2 and Group 3 publication pending.
