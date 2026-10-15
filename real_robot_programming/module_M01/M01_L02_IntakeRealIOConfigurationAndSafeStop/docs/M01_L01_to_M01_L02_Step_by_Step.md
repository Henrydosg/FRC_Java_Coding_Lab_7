# M01_L01 to M01_L02 — Step-by-Step Transition

Status: IN_PROGRESS / NOT FINAL
Donor: M01_L01_MechanismHardwareReadinessAndIOContract — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED
Donor path: real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract
Target: M01_L02_IntakeRealIOConfigurationAndSafeStop
Target path: real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop
Title: Intake Real IO Configuration and Safe Stop
Governance model: ADR_GOV3 three commit groups; current group: GROUP 2 — IMPLEMENT / VERIFY
Design Lock: APPROVED
Group 2: PUBLISHED / SOFTWARE VERIFIED (commit 58f8e3a); GOV3 audit repairs published through 23eed68
Powered hardware: NOT AUTHORIZED (H1.2 awaiting real robot access / NOT VERIFIED; H2/H3 NOT AUTHORIZED)
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

## Step 17 — Publish Group 2

- **Objective:** Publish the verified Group 2 implementation.
- **Why:** GOV3 Group 2 ends with one User-owned publication of the verified implementation.
- **Action:** After Architect review and Codex review PASS, the User committed and pushed Group 2.
- **Files Changed:** Group 2 files and recording (Steps 7–16).
- **Verification:** User-owned commit 58f8e3a548f61f19a96d3eb24a55db591cfad4a9 "Implement M01_L02 Intake real IO and safe stop"; accepted by the Architect.
- **Expected Result:** Group 2 PUBLISHED / SOFTWARE VERIFIED; lesson remains IN_PROGRESS.

## GOV3 audit repairs

The GOV3 repository-wide correctness audit inspected the inherited code of this lesson. Each repair below fixes one finding in inherited Swerve or autonomous code, was separately authorized by the Architect, verified by the User and published by the User. None changes the Intake scope or adds a nonzero output path. Software results are not real hardware verification.

## Step 18 — AUD-P2A-01: report drive configuration failures for every module

- **Objective:** Report a drive configuration failure for any of the four swerve modules.
- **Why:** The diagnostic previously covered only the front-left module.
- **Action:** Extended the failure report in the CTRE swerve module adapter and added regression tests.
- **Files Changed:** io/swerve/SwerveModuleIOCTRE.java; SwerveModuleIOCTREConfigurationTest.java.
- **Verification:** User clean build PASS; exact focused/full counts not established. Commit f0daab798bd010bf59abb2d7f1f8c44771c6a998.
- **Expected Result:** Every module's configuration failure is visible.

## Step 19 — AUD-P2A-02 / R1: latched fail-stop on module health faults

- **Objective:** Stop the drivetrain when a module health fault appears before production motion.
- **Why:** Teleop dispatch did not check runtime module health; a drive could be commanded while a steer module was not tracking.
- **Action:** SwerveSubsystem latches a module health fault and keeps outputs stopped; test fixtures updated to report healthy modules.
- **Files Changed:** subsystems/SwerveSubsystem.java; new SwerveSubsystemRuntimeHealthFailStopTest.java; six existing test fixtures.
- **Verification:** User full suite 909 PASS; focused PASS, exact count not established. Commit 2ef513afc2559a73735bd1f2a347215e10d9ebb1.
- **Expected Result:** A health fault fails closed instead of driving.

## Step 20 — AUD-P2A-06 / R2: CTRE configuration regression tests

- **Objective:** Cover the CTRE swerve configuration paths with tests.
- **Why:** The audit found configuration behavior without regression coverage.
- **Action:** Added test cases only; no production change.
- **Files Changed:** SwerveModuleIOCTREConfigurationTest.java.
- **Verification:** User focused 46/46 PASS; full 918/918 PASS. Commit b364f1336816d88bd0f0a5ddf7a9baa036e022b3.
- **Expected Result:** Configuration regressions are caught in software.

## Step 21 — E2-F02: validate the encoder sample before steer commissioning

- **Objective:** Use only a valid front-left encoder sample during steer commissioning.
- **Why:** Commissioning used an unchecked encoder sample.
- **Action:** SwerveSubsystem rejects an invalid sample and stops commissioning safely; tests added.
- **Files Changed:** subsystems/SwerveSubsystem.java; SwerveSubsystemClosedLoopCommissioningTest.java; SwerveFrontLeftClosedLoopCommissioningCommandTest.java fixture.
- **Verification:** User focused 18/18 PASS; full 924/924 PASS. Commit d568803996e28333ee21a3213b9a1ab0113df890.
- **Expected Result:** Commissioning never acts on an invalid encoder value.

## Step 22 — E2-F01: CANcoder magnet health in Swerve safety paths

- **Objective:** Treat an encoder with bad magnet health as an invalid measurement.
- **Why:** CANcoder magnet health was not read, so a bad magnet could look like a valid angle.
- **Action:** Added the vendor-neutral `encoderMeasurementValid` input, filled by the CTRE adapter from magnet health, and used it in Swerve module health and commissioning; Sim/Noop adapters and test fixtures updated.
- **Files Changed:** SwerveModuleIO, SwerveModuleIOCTRE, SwerveModuleIOSim, SwerveModuleIONoop, SwerveObservation, SwerveSubsystem, DriveThreeMeterValidationCommand; new SwerveModuleMeasurementValidityTest; existing test fixtures.
- **Verification:** User focused 77/77 PASS; full 934/934 PASS. Commit fc0fb0f181313a4f981fe9f55375a6f3ed904729. Hardware: Disabled-only all-four-CANcoder MagnetHealth pre-check PENDING.
- **Expected Result:** A bad-magnet reading fails closed in software; physical magnet health still to be checked on the robot.

## Step 23 — AUD-P2C-01: end the autonomous path on adapter fault

- **Objective:** Stop a running PathPlanner path and its inline events when the autonomous adapter faults.
- **Why:** The adapter fault was not part of the path race, so inline event commands could keep running after a fault.
- **Action:** Added `Commands.waitUntil(() -> faulted)` to the existing path race in AutoBuilderContractAdapter; added two regression tests.
- **Files Changed:** commands/auto/AutoBuilderContractAdapter.java; AutoBuilderContractAdapterRecoveryTest.java.
- **Verification:** User focused 14/14 PASS; full 936/936 PASS; clean BUILD SUCCESSFUL; Codex independent review PASS; GitHub publication verified by the User. Commit fc10df4364b1933b14d615c5f8fab6f4f1f55707.
- **Expected Result:** The path ends and the final safe stop runs. Accepted limitation: an inline event may execute once more in the cycle that detects FAULTED; this is not a synchronous emergency stop.

## Step 24 — Reconcile audit state documentation (GOV3-DOC-01)

- **Objective:** Make the current state and lesson records match the published baseline.
- **Why:** The records still showed Group 2 publication as pending and did not mention the audit repairs.
- **Action:** Updated the four lesson documents, this guide and CURRENT_STATE; archived the prior CURRENT_STATE verbatim in a new history event.
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L01_to_M01_L02_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/GOV3_DOC_01_AUDIT_STATE_RECONCILIATION_2026-10-13.md.
- **Verification:** Engineer self-review; governance mirror validator; Architect evaluation, Codex review and User verification PENDING.
- **Expected Result:** One truthful cursor; lesson remains IN_PROGRESS; hardware gates unchanged.

## Step 25 — AUD-P2D-01: publish encoder measurement validity

- **Objective:** Show each module's encoder measurement validity on the dashboard.
- **Why:** A bad CANcoder magnet makes the R1 latch stop the drivetrain while every published health flag still reads true, so the cause was invisible.
- **Action:** SwerveTelemetryFacade publishes one boolean topic EncoderMeasurementValid per module from the existing observation field; the regression test checks all four modules with TRUE and FALSE values and closes its NetworkTables resources with try-with-resources (Codex correction P2D01-R01). No R1 DriverStation warning was added.
- **Files Changed:** telemetry/swerve/SwerveTelemetryFacade.java; SwerveTelemetryFacadeTest.java.
- **Verification:** User focused 4/4 PASS; full 937/937 PASS; clean BUILD SUCCESSFUL; Codex independent review PASS; GitHub publication verified by the User. Commit d6f7615209a3e51e11a6b90ce1ddd0c6a8748608.
- **Expected Result:** A magnet-health fault is visible as EncoderMeasurementValid = false while EncoderConnected and EncoderConfigurationHealthy stay true.

## Step 26 — AUD-P3-01: isolate the vision timing-validity regression

- **Objective:** Make the invalid-timing test fail if the timingValid guard is removed.
- **Why:** The test fake did not copy timing fields, so the sample was rejected as stale whatever the guard did.
- **Action:** The fake now copies timingValid, receive timestamp and latency; the negative test uses a fresh, in-range, known-tag sample with timingValid = false, and a new positive control shows the same sample qualifies with timingValid = true. Test-only; no production change.
- **Files Changed:** subsystems/VisionSubsystemTest.java.
- **Verification:** User focused VisionSubsystemTest 5/5 PASS; full 938/938 PASS; clean BUILD SUCCESSFUL, 7 actionable tasks: 7 executed; Codex independent review PASS; GitHub publication verified by the User. Commit 23eed68ac5bb226d8ba9a70585812b206bb1206e.
- **Expected Result:** The regression protects the timingValid guard itself.

## Step 27 — Reconcile audit state documentation (GOV3-DOC-02)

- **Objective:** Make the current state and lesson records match the published baseline 23eed68.
- **Why:** The records still showed GOV3-DOC-01 and Phase 2d as pending and did not include the Phase 2d and Phase 3 results.
- **Action:** Updated the four lesson documents, this guide and CURRENT_STATE; archived the prior CURRENT_STATE verbatim in a new history event.
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L01_to_M01_L02_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/GOV3_DOC_02_AUDIT_STATE_RECONCILIATION_2026-10-15.md.
- **Verification:** Engineer self-review; governance mirror validator; Architect evaluation, Codex review and User verification PENDING.
- **Expected Result:** One truthful cursor; lesson remains IN_PROGRESS; hardware gates unchanged.

## Step 28 — Record the GOV3 final audit result (GOV3-DOC-03)

- **Objective:** Record that the GOV3 technical audit is complete and its Phase 4 final report is accepted.
- **Why:** The records still showed GOV3-DOC-02 and GOV3 Phase 4 as pending; formal closure needs one recorded state with its evidence limits.
- **Action:** Updated CURRENT_STATE and the operative next-boundary and checklist statements; archived the prior CURRENT_STATE verbatim in a new history event that lists final dispositions, residual risks and evidence limitations K1–K6. No code or test changed.
- **Files Changed:** README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, docs/M01_L01_to_M01_L02_Step_by_Step.md; repository docs/governance/CURRENT_STATE.md and docs/governance/history/events/GOV3_DOC_03_FINAL_AUDIT_CLOSURE_2026-10-15.md.
- **Verification:** Engineer self-review; governance mirror validator; Codex review and User verification PENDING.
- **Expected Result:** Formal GOV3 closure can be decided by the Architect after publication; lesson remains IN_PROGRESS; hardware gates unchanged.

## Remaining steps — PENDING

The following are planned and grant no permission. Each will be recorded with all seven fields when performed.

1. Codex review, User verification and User-owned publication of the GOV3-DOC-03 recording, then Architect formal GOV3 closure.
2. After separate Architect authorization only: User hardware checks (H1.2 awaiting real robot access; H2/H3 NOT AUTHORIZED) and the E2-F01 MagnetHealth pre-check.
3. Group 3 closure — NOT AUTHORIZED.
