# M01_L03 — GOV3 Group 1 Activation Recording — 2026-10-17

## Authority and scope

- **Authorization:** Architect brief "M01_L03 — GROUP 1 LIFECYCLE ACTIVATION" (AUTH_M01_L03_GROUP1_ACTIVATION_RECORDING), supplied by the User in chat on 2026-10-17. Primary Engineer: Claude Code. Bounded documentation and lifecycle recording only.
- **Decisions recorded:** M01_L03 targeted Design Lock CONDITIONAL PASS, subject to: D1 independent mechanism stop fan-out at the existing RobotContainer scheduler-fatal runtime boundary with existing Swerve safety behavior preserved; D2 backward-compatible additive IntakeIO.setVoltage(double), no removal or replacement of existing IO methods without explicit contract authority; Xbox reverse on the B button; fatal latch persistent until a controlled robot-code restart, never cleared automatically by Disable, mode change or signal recovery; commissioning ±1.5 V for 1.0 s in Test mode only; provisional forward +4.0 V, reverse −3.0 V and hardware peak ±6.0 V; motor inversion TBD pending authorized real-hardware direction testing. Safety: no nonzero Autonomous Intake output; independent all-actuator stop attempts; no throwing cleanup paths; initialization failures fail closed; CTRE command/stop failures checked; no automatic restart after fatal faults. These are design decisions and do not authorize implementation.
- **Gate recorded:** AUD-P1-02 MANDATORY / UNRESOLVED implementation safety gate before any nonzero Intake output ([Fast-Track ADR](../../../architecture_decisions/ADR_M01_Fast_Track_10_Lesson_Roadmap_Revision.md) §4–§5).
- **Governance model:** [ADR_GOV3](../../../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md) §4 Group 1 — ACTIVATE / BASELINE. Root AGENTS §6 and [ADR_GOV2](../../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md) §8 state-update contract: prior cursor archived below.
- **User evidence:** copy/rename/cleanup complete; inherited baseline BUILD SUCCESSFUL in 1m 5s, 7 actionable tasks (6 executed, 1 up-to-date); target untracked; no commit.
- **Engineer evidence (read-only, 2026-10-17):** branch main; HEAD == origin/main == ed6653e837b6e5f28e704b03f15bc2a8ace6e375 (local tracking ref); 349/349 M01_L02 tracked files byte-identical in the target before recording; no other non-generated difference (ignored .Glass/ identical); no nested duplicate project; governance mirror validator PASS (12/12, 0 findings).
- **Status:** working-tree recording only; self-review; Architect review PENDING; User-owned Group 1 publication PENDING (must include the full inherited lesson snapshot). No commit or push identity is claimed.

## Exact recording scope

- real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl/README.md
- real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl/LESSON_STATUS.md
- real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl/LESSON_PLAN.md
- real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl/LESSON_CHECKLIST.md
- real_robot_programming/module_M01/M01_L03_IntakeRealMotionAndXboxControl/docs/M01_L02_to_M01_L03_Step_by_Step.md (new)
- docs/governance/CURRENT_STATE.md
- docs/governance/history/events/M01_L03_GROUP1_ACTIVATION_RECORDING_2026-10-17.md (this event, new)

Each lesson lifecycle document prepends the M01_L03 operative record and preserves the inherited M01_L02 working-tree body byte-for-byte with its size and SHA-256 (README.md and LESSON_PLAN.md retain 86 and 84 CRLF line endings in the preserved donor region). Inherited Java, tests, build files, vendordeps, deploy assets and historical documentation are unchanged. M01_L01, M01_L02, the Fast-Track roadmap, root AGENTS, root README, delegated rules and history index are unchanged. No Gradle, Simulation, hardware or Git write was performed by the engineer. Protected T00, CAN registry and secrets were not accessed.

## Verbatim prior CURRENT_STATE — historical / superseded cursor

Captured immediately before this recording: **16510 bytes**, SHA-256 **43f3bb93a353577c0bd98e21888585c55d3c0e99e0325a0ae92be3baad40403f**. Its statements describe the M01_L02 Group 3 closure stage. Relative links inside the preserved body retain their source context (docs/governance/CURRENT_STATE.md). [CURRENT_STATE](../../CURRENT_STATE.md) remains the sole operational cursor.

<!-- VERBATIM PRIOR CURRENT_STATE BEGIN -->
# Governance 2.0 Current State

> ACTIVE GOVERNANCE 2.0 CURRENT-STATE CURSOR
>
> CURRENT_STATE does NOT self-authorize. Its authority derives from [root AGENTS](../../AGENTS.md), applicable registered decisions and explicitly authorized state recording.

This is the single operational repository state/action projection. Under [ADR_GOV3](../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md), the Architect brief "M01_L02 — GROUP 3 FINAL CLOSEOUT" (REISSUE_AUTH_M01_L02_GROUP3_EXACT_7_FILE_CLOSEOUT, 2026-10-16) authorizes this documentation-only Group 3 closure recording after the Architect accepted M01_L02 Group 2 hardware verification (PASS). **M01_L02 = COMPLETE / FROZEN / READ-ONLY** (Group 3 recording; publication PENDING). **Active editable lesson: NONE.** M01_L03 is not authorized. Nonzero Intake output is not authorized. This closure recording is in the working tree, self-reviewed only; User verification and User-owned publication are pending (ADR_GOV3 §4). No future commit or push identity is claimed.

The [M01_L02 Group 3 closure recording event and verbatim prior cursor](history/events/M01_L02_GROUP3_CLOSURE_RECORDING_2026-10-16.md) preserves the superseded roadmap-revision cursor. The [M01 Fast-Track roadmap revision event](history/events/M01_FAST_TRACK_ROADMAP_REVISION_2026-10-15.md), [GOV3-DOC-03 final audit closure event](history/events/GOV3_DOC_03_FINAL_AUDIT_CLOSURE_2026-10-15.md), [GOV3-DOC-02 reconciliation event](history/events/GOV3_DOC_02_AUDIT_STATE_RECONCILIATION_2026-10-15.md), [GOV3-DOC-01 reconciliation event](history/events/GOV3_DOC_01_AUDIT_STATE_RECONCILIATION_2026-10-13.md), [Group 2 verification recording event](history/events/M01_L02_GROUP2_USER_VERIFICATION_RECORDING_2026-10-10.md), [Group 1 activation event](history/events/M01_L02_GROUP1_ACTIVATION_RECORDING_2026-10-09.md), [GOV3 adoption event](history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md) and earlier events remain unchanged history. The [M01 Fast-Track 10-lesson roadmap revision](../architecture_decisions/ADR_M01_Fast_Track_10_Lesson_Roadmap_Revision.md) controls the M01 lesson sequence; the [original M01 roadmap](../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) is preserved and remains binding for everything except lesson numbering and count. The earlier M01 Roadmap Revision 2 proposal remains NOT APPROVED.

## Accepted operational cursor

| Item | Accepted projection / applicability |
| --- | --- |
| Accepted published baseline | c508f6ff44792ac8cb67aa42e896e4a2759adef4 "docs(M01): register 10-lesson Fast-Track roadmap" (published; HEAD == origin/main); latest software baseline 23eed68ac5bb226d8ba9a70585812b206bb1206e — the M01_L02 source tree is identical at every commit from 23eed68 to c508f6f |
| Learning-flow audit | COMPLETE / CLOSED; accepted 62 main + 17 historical/parallel = 79 represented lessons; not recomputed by M01 work |
| Existing main progression | New WPILib Project → S00 → A00 → A01 → V00 → M00_L16 → M01_L01 → M01_L02 (COMPLETE / FROZEN / READ-ONLY; publication PENDING) |
| Historical/parallel lineage | D00 → D01 Tank lineage; NOT M00/M01 donor |
| Canonical completed endpoint | M01_L01_MechanismHardwareReadinessAndIOContract — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED; readiness/contract scope, no powered actuation |
| Active editable lesson | NONE — M01_L02 closed by this Group 3 recording; M01_L03 requires separate preparation authorization |
| Governance | Governance 2.0 COMPLETE / FORMALLY CLOSED; GOV3 published and effective (commit 20167dd4929f8466ee20c1344287c8c8d5cee1aa) |
| Engineer roles | ChatGPT Architect; Claude Code and Codex Repository-aware Engineers; User Verification Engineer and sole Git / powered-hardware operator (ADR_GOV3 §2) |
| Constants/configuration authority review | CLOSED — CFG-A01 DEFER; CFG-A02 DO NOT CHANGE; CFG-A03 DEFER; M01_L02 Design Lock scopes the IntakeConstants addition only |
| M01 roadmap | Fast-Track 10-lesson revision APPROVED (APPROVE_M01_FAST_TRACK_10_LESSON_ROADMAP) and PUBLISHED (c508f6ff44792ac8cb67aa42e896e4a2759adef4). L01 COMPLETE/FROZEN; L02 COMPLETE/FROZEN by this recording; L03 Intake Real Motion & Xbox Control (old L03–L05, AUD-P1-02 before any nonzero output); L04 Feeder (old L06–L07); L05–L06 Flywheel (old L08–L11); L07–L10 Elevator (old L12–L18). Original 18-lesson ADR preserved as history; earlier Revision 2 proposal NOT APPROVED |
| M01_L02 identity / title | M01_L02_IntakeRealIOConfigurationAndSafeStop — Intake Real IO Configuration and Safe Stop |
| M01_L02 donor / target | real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/ → real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop/ |
| M01_L02 Primary Engineer | Claude Code; Codex independent reviewer |
| M01_L02 Design Lock | APPROVED — D1–D9; Intake Real IO configuration and safe zero output ONLY |
| M01_L02 locked configuration | One Kraken X44 (integrated Talon FX), no follower; CAN 40 on rio; NeutralMode BRAKE; Stator 40 A enabled; Supply 35 A enabled (40 A breaker); inversion and ratio constant not configured in L02; initial values, not proof of jam force or motion performance |
| M01_L02 failure policy | Configuration failure fails closed for the session (available/connected false, error reported once); no runtime retry — F3 ACCEPTED |
| M01_L02 Group 1 publication | PUBLISHED — User-owned commit 9bdc4f80d3321b67d4054c46bed31f18413f185e "Activate M01_L02 and record inherited baseline" |
| M01_L02 Group 2 | PUBLISHED / SOFTWARE VERIFIED — User-owned commit 58f8e3a548f61f19a96d3eb24a55db591cfad4a9 "Implement M01_L02 Intake real IO and safe stop"; Codex review PASS; Group 2 hardware acceptance PASS (Architect, H1/H2/H3 stationary-output scope) |
| GOV3 repository-wide correctness audit | FORMALLY CLOSED (Architect). Technical audit COMPLETE; Phases 2c, 2d and 3 CLOSED; Phase 4 final report ACCEPTED (PASS_GOV3_PHASE_4_FINAL_REPORT_WITH_RECORDING_GAP); GOV3-DOC-01 (0f3d6406498e9580a3a55d17d13f05d32feb6177), GOV3-DOC-02 (dc4e6cccc865ae949922b392b79ad63c16c4be65) and GOV3-DOC-03 (5ebfecadf463af294a463874fb7373d29e88b842) CLOSED / PUBLISHED; no Phase 5 authorized |
| M01_L02 hardware verification | ACCEPTED — Architect authorized the bounded H1 Disabled configuration/deployment checks, H2 Teleop zero-output test and H3 controlled-Disable zero-output test; User operated the robot on 2026-10-15 and 2026-10-16. REAL HARDWARE VERIFIED in configuration/readback scope (H1) and stationary-output scope (H2, H3) only. Nonzero Intake output NOT AUTHORIZED |
| E2-F01 hardware pre-check | DONE — all four CANcoder MagnetHealth readings GREEN in the Disabled preflight before the Teleop test (User) |
| M01_L02 transition guide | FINAL / PASS |
| M01_L02 Group 3 | AUTHORIZED (REISSUE_AUTH_M01_L02_GROUP3_EXACT_7_FILE_CLOSEOUT); COMPLETE / FROZEN / READ-ONLY recorded; User verification and publication PENDING |
| M01 hardware readiness (L01) | PASS — accepted scoped User evidence; powered commissioning REAL HARDWARE DEFERRED |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED; protected candidate contents excluded |
| Earlier ACM repository-wide audit (pre-M01) | Its Phase 2 COMPLETE WITH RECORDED LIMITS; its Phase 3 / ACM-01–12 FORMALLY CLOSED; ACM-12 60 CLOSED / 0 BLOCKED; F01/F02/F03 CLOSED. These ACM phases are distinct from the GOV3 audit phases below |
| Phase 4 / M00_L17 (ACM audit lineage) | NOT STARTED / FORBIDDEN; M00_L17 NOT AUTHORIZED (distinct from GOV3 Phase 4) |
| Next boundary | User verification and User-owned publication of the M01_L02 Group 3 closure recording (then COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED by Git history, ADR_GOV3 §4). M01_L03 Intake Real Motion & Xbox Control requires separate preparation authorization; AUD-P1-02 must be satisfied before any nonzero Intake output. |

## GOV3 audit position

The GOV3 repository-wide code-correctness audit (Architect brief "GOV3 — REPOSITORY-WIDE INDEPENDENT CODE-CORRECTNESS AUDIT", Primary Audit Engineer Claude Code) is separate from the earlier ACM audit. Its phases and findings were adjudicated by the Architect in chat; only the dispositions below are projected here.

| Finding / phase | Accepted disposition |
| --- | --- |
| AUD-P1-01 | S0 — no production change |
| AUD-P1-02 | Future M01 safety prerequisite (all-actuator stop coverage before nonzero mechanism output) |
| AUD-P2A-01 | CLOSED — f0daab798bd010bf59abb2d7f1f8c44771c6a998 |
| AUD-P2A-02 / R1 latched fail-stop | CLOSED — 2ef513afc2559a73735bd1f2a347215e10d9ebb1 |
| AUD-P2A-06 / R2 | CLOSED — b364f1336816d88bd0f0a5ddf7a9baa036e022b3 |
| E2-F02 | CLOSED — d568803996e28333ee21a3213b9a1ab0113df890 |
| E2-F01 | CLOSED (software) — fc0fb0f181313a4f981fe9f55375a6f3ed904729; hardware MagnetHealth pre-check DONE (all four GREEN, Disabled preflight) |
| Phase 2c | CLOSED |
| AUD-P2C-01 | CLOSED / PUBLISHED / SOFTWARE VERIFIED — fc10df4364b1933b14d615c5f8fab6f4f1f55707 |
| AUD-P2C-02 | NOTE / RECORD / NO CHANGE |
| AUD-P2C-03 | NOTE / ACCEPTED FAIL-SAFE |
| AUD-P2C-04 | NOTE / RECORD / NO CHANGE |
| Phase 2d (Vision, mechanisms, telemetry) | FORMALLY CLOSED |
| AUD-P2D-01 | CLOSED / REPAIRED / SOFTWARE VERIFIED / PUBLISHED — d6f7615209a3e51e11a6b90ce1ddd0c6a8748608 (EncoderMeasurementValid telemetry only; no R1 DriverStation warning); P2D01-R01 resolved |
| AUD-P2D-02 | NOTE / NO CHANGE |
| AUD-P2D-03 | NOTE / NO CHANGE |
| Phase 3 (lineage and regression integrity) | FORMALLY CLOSED |
| AUD-P3-01 | CLOSED / REPAIRED / SOFTWARE VERIFIED / PUBLISHED — 23eed68ac5bb226d8ba9a70585812b206bb1206e (test-only) |
| AUD-P3-02 | RESIDUAL RISK / RECORD / NO CHANGE |
| AUD-P3-03 | NOTE / RECORD / NO CHANGE |
| Phase 4 (final consolidated audit report) | Defined and authorized by the Architect brief "GOV3 — PHASE 4 — FINAL CONSOLIDATED AUDIT REPORT"; report ACCEPTED — PASS_GOV3_PHASE_4_FINAL_REPORT_WITH_RECORDING_GAP |
| Technical audit | COMPLETE — no unresolved confirmed production correctness defect in the recorded findings |
| Formal GOV3 closure | FORMALLY CLOSED by the Architect after GOV3-DOC-03 publication (5ebfeca) |

Evidence limitations (accepted with the Phase 4 report; see the GOV3-DOC-03 event): closure records for GOV3 Phases 0, 1, 2a and 2b are not established in repository evidence; dispositions known only from chat or engineer recollection are NOT ESTABLISHED; Codex review evidence for AUD-P2A-01, R1, R2, E2-F02 and E2-F01 is not recorded; f0daab7 through fc0fb0f are ancestors of the verified published HEAD, without recorded per-commit publication checks; missing historical test counts remain NOT ESTABLISHED.

AUD-P2C-01 limitation (ACCEPTED): the adapter fault ends a running path at the next race check, not synchronously. An inline PathPlanner event may execute once more in the scheduler cycle that detects FAULTED. It is not a synchronous emergency stop; AUD-P1-02 remains a separate future requirement.

Baseline provenance: 5b4fb3522e0c4c2dfcbba2568bb583d75acbaa94 (Claude Code working notes) and 06529d79d0495a7eb4f7935b0dbcb7ae90b726cf (M01_L02 software CI workflow) are also part of the published baseline.

## Evidence classification

| Evidence scope | Classification / provenance |
| --- | --- |
| Inheritance / architecture audit | THEORY VERIFIED — static engineer audit accepted by the Architect |
| Inherited baseline build | Supplied User execution (Group 1) |
| Group 2 (58f8e3a) | BUILD VERIFIED — User clean build PASS; SOFTWARE TEST VERIFIED — 895/895 PASS; SIMULATION VERIFIED — NOOP SCOPE ONLY; Codex review PASS |
| AUD-P2A-01 (f0daab7) | User clean build PASS; exact focused/full counts not established |
| R1 (2ef513a) | User full suite 909 PASS; focused PASS, exact count not established |
| R2 (b364f13) | User focused 46/46 PASS; User full 918/918 PASS |
| E2-F02 (d568803) | User focused 18/18 PASS; User full 924/924 PASS |
| E2-F01 (fc0fb0f) | User focused 77/77 PASS; User full 934/934 PASS |
| AUD-P2C-01 (fc10df4) | User focused 14/14 PASS; User full 936/936 PASS; full clean BUILD SUCCESSFUL; Codex independent review PASS; GitHub publication VERIFIED by the User |
| AUD-P2D-01 (d6f7615) | User focused 4/4 PASS; User full 937/937 PASS (0 failures, 0 errors, 0 skipped); full clean BUILD SUCCESSFUL; Codex independent review PASS; GitHub publication VERIFIED by the User |
| AUD-P3-01 (23eed68) | User focused VisionSubsystemTest 5/5 PASS; User full 938/938 PASS (0 failures, 0 errors, 0 skipped); full clean BUILD SUCCESSFUL, 7 actionable tasks: 7 executed; Codex independent review PASS; GitHub publication VERIFIED by the User |
| Engineer-run results | Claude Code preliminary convenience evidence only (ADR_GOV3 §8); superseded for gate purposes by the User results above |
| M01_L02 H1 (User, 2026-10-15/16) | REAL HARDWARE VERIFIED — configuration/readback scope: Kraken X44 on CAN 40, bus rio; Brake; Stator 40 A and Supply 35 A enabled; build and Disabled deployment PASS; post-deployment readback PASS (Phoenix Tuner X prescribed; no independent tool log supplied); no "Intake TalonFX configuration unhealthy" error observed |
| M01_L02 H2 (User) | REAL HARDWARE VERIFIED — stationary-output scope: Teleop Enabled; Right Bumper released / held / released gave RequestedState STOPPED / INTAKE_REQUESTED / STOPPED; all motors stationary, no abnormal movement |
| M01_L02 H3 (User) | REAL HARDWARE VERIFIED — stationary-output scope: controlled Disable while holding Right Bumper; all motors stationary; final Driver Station state Disabled |
| Glass / Driver Station | GLASS VERIFIED — Intake Available = true, Connected = true and RequestedState transitions (User screenshots, 2026-10-16); DRIVER STATION VERIFIED — Teleop Enable and controlled Disable within the H2/H3 scope |
| Hardware evidence limits | Zero motion does not independently prove the internal Talon FX NeutralOut control mode; no rotating-motor stopping test; no nonzero Intake output tested; the operator was instructed to keep the roller clear but no separate roller-clear attestation is recorded; the exact deployed commit was not recorded (the M01_L02 source tree is identical from 23eed68 to c508f6f); software suite 938/938 was not rerun at the documentation HEAD |
| Real hardware (other) | No software, Codex or Simulation result implies REAL HARDWARE VERIFIED beyond the H1–H3 scope above |
| Direction, inversion, stopping, current adequacy | REAL HARDWARE DEFERRED to M01_L03 and later authorized M01 lessons |

## Authority, evidence and preservation limits

Known L01 hardware facts remain as recorded in the frozen [M01_L01 lesson plan evidence register](../../real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/LESSON_PLAN.md#hardware-readiness-evidence-register): Kraken X44; one motor; no follower; integrated Talon FX; CAN ID 40; bus rio; firmware 26.3.0.0; 10:1 ratio; no external sensors; safe zero YES on accepted pre-existing User evidence.

All Intake request paths (Right Bumper, RunIntakeCommand, IntakeToFeederCommand, PathPlanner LEARNING_EVENT, NamedCommands and direct semantic callers) reach hardware only through IntakeIO.requestIntake(). On the real robot IntakeIOReal issues NeutralOut only; off-robot builds, tests and Simulation select IntakeIONoop. Swerve remains sole drivetrain/localization owner and autonomous safety boundaries are preserved.

Simple enough, safe enough, correct enough — do not over-engineer. Do not add automatic jam detection, reverse/eject, extra sensors, state machines, PID or velocity control, extra layers or speculative telemetry without a real requirement and later approval.

Root AGENTS is the constitution/entrypoint; seven delegated rules retain their scopes; ADR_GOV3 governs engineer roles and lesson publication mechanics from M01_L02 onward. English Documents A/B/C remain authoritative. Historical, candidate and consumed approvals grant no fresh permission. Unrelated dirty/untracked state and the prior hardware-registry search-scope incident remain preserved. Protected registry, T00 candidate and secrets were not accessed by this recording.

Later updates require accepted adjudication or an already-authorized gate, exact scope, prior-state preservation, semantic consistency review and User-owned publication. This cursor cannot authorize its own modification or future actions.
<!-- VERBATIM PRIOR CURRENT_STATE END -->
