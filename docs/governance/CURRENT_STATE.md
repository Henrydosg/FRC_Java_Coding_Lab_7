# Governance 2.0 Current State

> ACTIVE GOVERNANCE 2.0 CURRENT-STATE CURSOR
>
> CURRENT_STATE does NOT self-authorize. Its authority derives from [root AGENTS](../../AGENTS.md), applicable registered decisions and explicitly authorized state recording.

This is the single operational repository state/action projection. Under [ADR_GOV3](../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md), the Architect brief "GOV3-DOC-01 — REVISED ARCHITECT AUTHORIZATION — BOUNDED DOCUMENTATION RECONCILIATION" (2026-10-13) authorizes this documentation-only recording of the accepted published baseline and GOV3 audit position. **M01_L02 = IN_PROGRESS** and is the sole active editable lesson. Group 1 and Group 2 are published. **Powered hardware: NOT AUTHORIZED. Group 3: NOT AUTHORIZED. GOV3 Phase 2d: PLANNED / NOT AUTHORIZED.** This recording is in the working tree, self-reviewed only; Architect evaluation, Codex independent review, User verification and User-owned publication are pending. No future commit or push identity is claimed.

The [GOV3-DOC-01 reconciliation event and verbatim prior cursor](history/events/GOV3_DOC_01_AUDIT_STATE_RECONCILIATION_2026-10-13.md) preserves the superseded Group 2 recording cursor. The [Group 2 verification recording event](history/events/M01_L02_GROUP2_USER_VERIFICATION_RECORDING_2026-10-10.md), [Group 1 activation event](history/events/M01_L02_GROUP1_ACTIVATION_RECORDING_2026-10-09.md), [GOV3 adoption event](history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md) and earlier events remain unchanged history. The [registered M01 roadmap](../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) controls curriculum scope; M01 Roadmap Revision 2 was NOT APPROVED and the registered 18-lesson roadmap remains controlling.

## Accepted operational cursor

| Item | Accepted projection / applicability |
| --- | --- |
| Accepted published baseline | fc10df4364b1933b14d615c5f8fab6f4f1f55707 "fix(M01_L02): terminate autonomous path on adapter fault" — User-verified HEAD == origin/main, tracked tree clean at the accepted publication gate; accepted by the Architect |
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
| GOV3 repository-wide correctness audit | Phase 2c TECHNICALLY CLOSED; repairs published inside M01_L02 (see GOV3 audit position below); GOV3-DOC-01 documentation reconciliation IN PROGRESS until independently reviewed, User-verified and published |
| M01_L02 powered hardware | NOT AUTHORIZED. H1.2: AWAITING REAL ROBOT ACCESS / NOT VERIFIED. H2/H3: NOT AUTHORIZED |
| E2-F01 hardware pre-check | Disabled-only all-four-CANcoder MagnetHealth pre-check PENDING before any separately authorized powered Swerve motion |
| M01_L02 transition guide | IN_PROGRESS / NOT FINAL |
| M01_L02 Group 3 | NOT AUTHORIZED |
| M01 hardware readiness (L01) | PASS — accepted scoped User evidence; powered commissioning REAL HARDWARE DEFERRED |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED; protected candidate contents excluded |
| Earlier ACM repository-wide audit (pre-M01) | Its Phase 2 COMPLETE WITH RECORDED LIMITS; its Phase 3 / ACM-01–12 FORMALLY CLOSED; ACM-12 60 CLOSED / 0 BLOCKED; F01/F02/F03 CLOSED. These ACM phases are distinct from the GOV3 audit phases below |
| Phase 4 / M00_L17 (ACM audit lineage) | NOT STARTED / FORBIDDEN; M00_L17 NOT AUTHORIZED |
| Next boundary | Architect evaluation of this GOV3-DOC-01 recording, then Codex independent review, User verification and User-owned publication. GOV3 Phase 2d, powered verification and Group 3 each require separate Architect authorization. |

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
| Phase 2c | TECHNICALLY CLOSED |
| AUD-P2C-01 | CLOSED / PUBLISHED / SOFTWARE VERIFIED — fc10df4364b1933b14d615c5f8fab6f4f1f55707 |
| AUD-P2C-02 | NOTE / RECORD / NO CHANGE |
| AUD-P2C-03 | NOTE / ACCEPTED FAIL-SAFE |
| AUD-P2C-04 | NOTE / RECORD / NO CHANGE |
| Phase 2d | PLANNED / NOT AUTHORIZED — provisional scope: Vision, mechanism and telemetry correctness |

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
