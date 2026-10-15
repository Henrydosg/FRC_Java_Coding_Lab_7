# M01 — Fast-Track 10-Lesson Roadmap Revision — 2026-10-15

## Authority and scope

- **Decision:** APPROVE_M01_FAST_TRACK_10_LESSON_ROADMAP — the Architect approved the User-approved Fast-Track 10-lesson M01 curriculum (brief "M01 — FAST-TRACK 10-LESSON ROADMAP REVISION", supplied by the User in chat on 2026-10-15).
- **Recording authorization:** AUTH_M01_FAST_TRACK_ROADMAP_REVISION_EXACT_8_FILES (brief "M01 FAST-TRACK 10-LESSON ROADMAP — EXACT-PATH IMPLEMENTATION AUTHORIZATION", 2026-10-15). It accepted the engineer HOLD on the first brief, which named no file paths (root AGENTS §12). Primary Documentation Engineer: Claude Code.
- **Governance model:** Root AGENTS §6, §8 and §15; [ADR_GOV2](../../../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md) §8 state-update contract (prior cursor archived below); [ADR_GOV3](../../../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md).
- **Successor decision:** [ADR_M01_Fast_Track_10_Lesson_Roadmap_Revision](../../../architecture_decisions/ADR_M01_Fast_Track_10_Lesson_Roadmap_Revision.md). It supersedes only the lesson lineage, sequence and lesson-number boundaries of the [original M01 roadmap](../../../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) (§7, §8, §9 numbering). The original ADR is preserved unchanged and remains binding for all other content.
- **Mapping:** New L01 = old L01 (COMPLETE/FROZEN, unchanged); L02 = old L02 (IN_PROGRESS, unchanged); L03 = old L03–L05; L04 = old L06–L07; L05 = old L08–L09; L06 = old L10–L11; L07 = old L12–L13; L08 = old L14–L15; L09 = old L16; L10 = old L17–L18.
- **Preserved safety prerequisites:** AUD-P1-02 must be satisfied inside new L03 after measurement validity and before any nonzero mechanism output; L03 order measurement validity → AUD-P1-02 → bounded first motion → Xbox held-forward / release-stop → manual reverse only if safely supported → User hardware verification. Old dependent gates remain separate ordered gates inside merged lessons. Four-mechanism module exit unchanged. M01_L02 gates are not changed by this revision; as projected in CURRENT_STATE at registration: powered hardware NOT AUTHORIZED; H1.2 AWAITING REAL ROBOT ACCESS / NOT VERIFIED; H2/H3 NOT AUTHORIZED; E2-F01 MagnetHealth pre-check PENDING. These historical records do not override later accepted User hardware evidence. This roadmap revision neither adjudicates hardware verification nor authorizes powered motion.
- **Not authorized by this recording:** M01_L03 creation or preparation; M01_L02 closure; code, test or configuration changes; Gradle, Simulation, hardware or Git writes by the engineer.
- **Status:** working-tree recording only; self-review; Architect review, independent review if authorized, User verification and User-owned publication PENDING. No commit or push identity is claimed for this recording.
- **Brief completeness note:** the supplied authorization text ended after the L09 entry; L10 (Elevator Position & Manual Control, old L17–L18) is taken from the approved decision brief.

## Exact recording scope

- docs/architecture_decisions/ADR_M01_Fast_Track_10_Lesson_Roadmap_Revision.md (new)
- docs/governance/history/events/M01_FAST_TRACK_ROADMAP_REVISION_2026-10-15.md (this event, new)
- docs/governance/CURRENT_STATE.md (roadmap authority, archive link and next boundary)
- AGENTS.md (§15 roadmap navigation only)
- README.md (root roadmap navigation only)
- real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop/LESSON_STATUS.md (operative roadmap reference only)
- real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop/LESSON_CHECKLIST.md (operative roadmap reference only)
- real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop/docs/M01_L01_to_M01_L02_Step_by_Step.md (roadmap statement and Step 29)

No other file, existing event, frozen lesson, ADR_M01, rule or history index is changed. Protected T00, CAN registry and secrets were not accessed.

## Verbatim prior CURRENT_STATE — historical / superseded cursor

Captured immediately before this recording from the working tree, identical to its blob in HEAD 5ebfeca (last changed in 5ebfeca): **13808 bytes**, SHA-256 **42f6c03436255db4645205be3b4f0680b33e0c57b9285794793e451f0ade97b2**. Its statements describe the GOV3-DOC-03 recording stage; its authorizing paragraph, "registered 18-lesson roadmap remains controlling", "M01 roadmap ... 18 lessons" and "Next boundary" fields are superseded. Relative links inside the preserved body retain their source context (docs/governance/CURRENT_STATE.md). [CURRENT_STATE](../../CURRENT_STATE.md) remains the sole operational cursor.

<!-- VERBATIM PRIOR CURRENT_STATE BEGIN -->
# Governance 2.0 Current State

> ACTIVE GOVERNANCE 2.0 CURRENT-STATE CURSOR
>
> CURRENT_STATE does NOT self-authorize. Its authority derives from [root AGENTS](../../AGENTS.md), applicable registered decisions and explicitly authorized state recording.

This is the single operational repository state/action projection. Under [ADR_GOV3](../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md), the Architect brief "GOV3-DOC-03 — FINAL AUDIT CLOSURE RECORDING" (2026-10-15) authorizes this documentation-only recording of the accepted GOV3 Phase 4 final report (PASS_GOV3_PHASE_4_FINAL_REPORT_WITH_RECORDING_GAP), the completed technical audit and its evidence limitations. **M01_L02 = IN_PROGRESS** and is the sole active editable lesson. Group 1 and Group 2 are published. **Powered hardware: NOT AUTHORIZED. Group 3: NOT AUTHORIZED. GOV3 formal closure: PENDING.** GOV3-DOC-03 is a documentation recording IN_PROGRESS in the working tree, self-reviewed only; Codex independent review, User verification and User-owned publication are pending. No future commit or push identity is claimed.

The [GOV3-DOC-03 final audit closure event and verbatim prior cursor](history/events/GOV3_DOC_03_FINAL_AUDIT_CLOSURE_2026-10-15.md) preserves the superseded GOV3-DOC-02 cursor. The [GOV3-DOC-02 reconciliation event](history/events/GOV3_DOC_02_AUDIT_STATE_RECONCILIATION_2026-10-15.md), [GOV3-DOC-01 reconciliation event](history/events/GOV3_DOC_01_AUDIT_STATE_RECONCILIATION_2026-10-13.md), [Group 2 verification recording event](history/events/M01_L02_GROUP2_USER_VERIFICATION_RECORDING_2026-10-10.md), [Group 1 activation event](history/events/M01_L02_GROUP1_ACTIVATION_RECORDING_2026-10-09.md), [GOV3 adoption event](history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md) and earlier events remain unchanged history. The [registered M01 roadmap](../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) controls curriculum scope; M01 Roadmap Revision 2 was NOT APPROVED and the registered 18-lesson roadmap remains controlling.

## Accepted operational cursor

| Item | Accepted projection / applicability |
| --- | --- |
| Accepted published baseline | dc4e6cccc865ae949922b392b79ad63c16c4be65 "docs(governance): reconcile GOV3 audit closures" (GOV3-DOC-02, CLOSED / PUBLISHED / USER VERIFIED); latest software baseline 23eed68ac5bb226d8ba9a70585812b206bb1206e (User-verified HEAD == origin/main == GitHub remote); accepted by the Architect |
| Learning-flow audit | COMPLETE / CLOSED; accepted 62 main + 17 historical/parallel = 79 represented lessons; not recomputed by M01 work |
| Existing main progression | New WPILib Project → S00 → A00 → A01 → V00 → M00_L16 → M01_L01 → M01_L02 (IN_PROGRESS) |
| Historical/parallel lineage | D00 → D01 Tank lineage; NOT M00/M01 donor |
| Canonical completed endpoint | M01_L01_MechanismHardwareReadinessAndIOContract — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED; readiness/contract scope, no powered actuation |
| Active editable lesson | M01_L02_IntakeRealIOConfigurationAndSafeStop — IN_PROGRESS |
| Governance | Governance 2.0 COMPLETE / FORMALLY CLOSED; GOV3 published and effective (commit 20167dd4929f8466ee20c1344287c8c8d5cee1aa) |
| Engineer roles | ChatGPT Architect; Claude Code and Codex Repository-aware Engineers; User Verification Engineer and sole Git / powered-hardware operator (ADR_GOV3 §2) |
| Constants/configuration authority review | CLOSED — CFG-A01 DEFER; CFG-A02 DO NOT CHANGE; CFG-A03 DEFER; M01_L02 Design Lock scopes the IntakeConstants addition only |
| M01 roadmap | APPROVED / REGISTERED / PUBLISHED / REMOTE VERIFIED — 18 lessons; Revision 2 NOT APPROVED |
| M01_L02 identity / title | M01_L02_IntakeRealIOConfigurationAndSafeStop — Intake Real IO Configuration and Safe Stop |
| M01_L02 donor / target | real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/ → real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop/ |
| M01_L02 Primary Engineer | Claude Code; Codex independent reviewer |
| M01_L02 Design Lock | APPROVED — D1–D9; Intake Real IO configuration and safe zero output ONLY |
| M01_L02 locked configuration | One Kraken X44 (integrated Talon FX), no follower; CAN 40 on rio; NeutralMode BRAKE; Stator 40 A enabled; Supply 35 A enabled (40 A breaker); inversion and ratio constant not configured in L02; initial values, not proof of jam force or motion performance |
| M01_L02 failure policy | Configuration failure fails closed for the session (available/connected false, error reported once); no runtime retry — F3 ACCEPTED |
| M01_L02 Group 1 publication | PUBLISHED — User-owned commit 9bdc4f80d3321b67d4054c46bed31f18413f185e "Activate M01_L02 and record inherited baseline" |
| M01_L02 Group 2 | PUBLISHED / SOFTWARE VERIFIED — User-owned commit 58f8e3a548f61f19a96d3eb24a55db591cfad4a9 "Implement M01_L02 Intake real IO and safe stop"; Codex review PASS; accepted by the Architect |
| GOV3 repository-wide correctness audit | Technical audit COMPLETE; Phases 2c, 2d and 3 CLOSED; Phase 4 final report ACCEPTED (PASS_GOV3_PHASE_4_FINAL_REPORT_WITH_RECORDING_GAP); repairs published inside M01_L02 (see GOV3 audit position below); GOV3-DOC-01 CLOSED / PUBLISHED (0f3d6406498e9580a3a55d17d13f05d32feb6177); GOV3-DOC-02 CLOSED / PUBLISHED (dc4e6cccc865ae949922b392b79ad63c16c4be65); GOV3-DOC-03 recording IN_PROGRESS, publication PENDING; formal GOV3 closure PENDING; no Phase 5 authorized |
| M01_L02 powered hardware | NOT AUTHORIZED. H1.2: AWAITING REAL ROBOT ACCESS / NOT VERIFIED. H2/H3: NOT AUTHORIZED |
| E2-F01 hardware pre-check | Disabled-only all-four-CANcoder MagnetHealth pre-check PENDING before any separately authorized powered Swerve motion |
| M01_L02 transition guide | IN_PROGRESS / NOT FINAL |
| M01_L02 Group 3 | NOT AUTHORIZED |
| M01 hardware readiness (L01) | PASS — accepted scoped User evidence; powered commissioning REAL HARDWARE DEFERRED |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED; protected candidate contents excluded |
| Earlier ACM repository-wide audit (pre-M01) | Its Phase 2 COMPLETE WITH RECORDED LIMITS; its Phase 3 / ACM-01–12 FORMALLY CLOSED; ACM-12 60 CLOSED / 0 BLOCKED; F01/F02/F03 CLOSED. These ACM phases are distinct from the GOV3 audit phases below |
| Phase 4 / M00_L17 (ACM audit lineage) | NOT STARTED / FORBIDDEN; M00_L17 NOT AUTHORIZED (distinct from GOV3 Phase 4) |
| Next boundary | Codex independent review, User verification and User-owned publication of GOV3-DOC-03, followed by Architect formal GOV3 closure. Powered verification and Group 3 each require separate Architect authorization; no GOV3 Phase 5 is authorized. |

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
| E2-F01 | CLOSED (software) — fc0fb0f181313a4f981fe9f55375a6f3ed904729; hardware MagnetHealth pre-check PENDING |
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
| Formal GOV3 closure | PENDING User verification and publication of GOV3-DOC-03, then Architect formal closure |

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
| Glass / Driver Station | NOT TESTED |
| Real hardware | NOT AUTHORIZED / NOT VERIFIED — no software, Codex or Simulation result implies REAL HARDWARE VERIFIED |
| Direction, inversion, stopping, current adequacy | REAL HARDWARE DEFERRED to later authorized M01 lessons |

## Authority, evidence and preservation limits

Known L01 hardware facts remain as recorded in the frozen [M01_L01 lesson plan evidence register](../../real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/LESSON_PLAN.md#hardware-readiness-evidence-register): Kraken X44; one motor; no follower; integrated Talon FX; CAN ID 40; bus rio; firmware 26.3.0.0; 10:1 ratio; no external sensors; safe zero YES on accepted pre-existing User evidence.

All Intake request paths (Right Bumper, RunIntakeCommand, IntakeToFeederCommand, PathPlanner LEARNING_EVENT, NamedCommands and direct semantic callers) reach hardware only through IntakeIO.requestIntake(). On the real robot IntakeIOReal issues NeutralOut only; off-robot builds, tests and Simulation select IntakeIONoop. Swerve remains sole drivetrain/localization owner and autonomous safety boundaries are preserved.

Simple enough, safe enough, correct enough — do not over-engineer. Do not add automatic jam detection, reverse/eject, extra sensors, state machines, PID or velocity control, extra layers or speculative telemetry without a real requirement and later approval.

Root AGENTS is the constitution/entrypoint; seven delegated rules retain their scopes; ADR_GOV3 governs engineer roles and lesson publication mechanics from M01_L02 onward. English Documents A/B/C remain authoritative. Historical, candidate and consumed approvals grant no fresh permission. Unrelated dirty/untracked state and the prior hardware-registry search-scope incident remain preserved. Protected registry, T00 candidate and secrets were not accessed by this recording.

Later updates require accepted adjudication or an already-authorized gate, exact scope, prior-state preservation, semantic consistency review and User-owned publication. This cursor cannot authorize its own modification or future actions.
<!-- VERBATIM PRIOR CURRENT_STATE END -->
