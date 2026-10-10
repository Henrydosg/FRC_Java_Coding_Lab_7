# Governance 2.0 Current State

> ACTIVE GOVERNANCE 2.0 CURRENT-STATE CURSOR
>
> CURRENT_STATE does NOT self-authorize. Its authority derives from [root AGENTS](../../AGENTS.md), applicable registered decisions and explicitly authorized state recording.

This is the single operational repository state/action projection. Under [ADR_GOV3](../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md) Group 1, the Architect brief "M01_L02 — GROUP 1 DESIGN LOCK AND ACTIVATION RECORDING" (2026-10-09) accepts the inheritance audit PASS, APPROVES the M01_L02 Design Lock and authorizes Group 1 recording. **M01_L02 = IN_PROGRESS** and is the sole active editable lesson. **Group 2 implementation: NOT AUTHORIZED. Powered hardware: NOT AUTHORIZED.** This Group 1 recording is in the working tree, self-reviewed only; Architect review and User-owned Group 1 publication (full inherited lesson snapshot) are pending. No future commit or push identity is claimed.

The [M01_L02 Group 1 activation event and verbatim prior cursor](history/events/M01_L02_GROUP1_ACTIVATION_RECORDING_2026-10-09.md) preserves the superseded M01_L01 post-publication cursor. The [GOV3 adoption event](history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md), [M01_L01 final publication metadata event](history/events/M01_L01_FINAL_PUBLICATION_METADATA_RECONCILIATION_2026-10-06.md) and earlier events remain unchanged history. The [registered M01 roadmap](../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) controls curriculum scope.

## Accepted operational cursor

| Item | Accepted projection / applicability |
| --- | --- |
| Learning-flow audit | COMPLETE / CLOSED; accepted 62 main + 17 historical/parallel = 79 represented lessons; not recomputed by M01 work |
| Existing main progression | New WPILib Project → S00 → A00 → A01 → V00 → M00_L16 → M01_L01 → M01_L02 (IN_PROGRESS) |
| Historical/parallel lineage | D00 → D01 Tank lineage; NOT M00/M01 donor |
| Canonical completed endpoint | M01_L01_MechanismHardwareReadinessAndIOContract — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / REMOTE VERIFIED; readiness/contract scope, no powered actuation |
| Active editable lesson | M01_L02_IntakeRealIOConfigurationAndSafeStop — IN_PROGRESS |
| Governance | Governance 2.0 COMPLETE / FORMALLY CLOSED; GOV3 published and effective (commit 20167dd4929f8466ee20c1344287c8c8d5cee1aa; User-reported push; Architect statement "GOV3 is published and effective"; local refs corroborate HEAD = origin/main at recording) |
| Engineer roles | ChatGPT Architect; Claude Code and Codex Repository-aware Engineers; User Verification Engineer and sole Git / powered-hardware operator (ADR_GOV3 §2) |
| Constants/configuration authority review | CLOSED — CFG-A01 DEFER; CFG-A02 DO NOT CHANGE; CFG-A03 DEFER; M01_L02 Design Lock separately scopes a future IntakeConstants addition in Group 2 only |
| M01 roadmap | APPROVED / REGISTERED / PUBLISHED / REMOTE VERIFIED — supplied Architect/User evidence |
| M01_L02 identity / title | M01_L02_IntakeRealIOConfigurationAndSafeStop — Intake Real IO Configuration and Safe Stop |
| M01_L02 donor / target | real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/ → real_robot_programming/module_M01/M01_L02_IntakeRealIOConfigurationAndSafeStop/ |
| M01_L02 Primary Engineer | Claude Code; Codex optional independent reviewer before Group 3 |
| M01_L02 preparation and baseline | COMPLETE — User copy/rename/cleanup; BUILD SUCCESSFUL in 42s, 7 tasks (6 executed, 1 up-to-date); supplied User evidence, not engineer execution |
| M01_L02 inheritance | PASS — 344/344 donor tracked files byte-identical before recording; only git-ignored donor .Glass/ differs; no nested duplicate |
| M01_L02 Design Lock | APPROVED — D1–D9; zero-output Real IO configuration foundation |
| M01_L02 locked configuration | One Kraken X44 (integrated Talon FX), no follower; CAN 40 on rio; NeutralMode BRAKE; Stator 40 A enabled; Supply 35 A enabled (40 A breaker); inversion and ratio constant not configured in L02; initial values, not proof of jam force or motion performance |
| M01_L02 failure policy | Configuration failure fails closed for the session (available/connected false, error reported once); no runtime retry — F3 ACCEPTED |
| M01_L02 Group 2 scope (future) | NEW IntakeIOReal.java, IntakeIORealConfigurationTest.java; MODIFY Constants.java (IntakeConstants only), RobotContainer.java (Real/Noop selection only), IntakeArchitectureBoundaryTest.java; NeutralOut only — NOT AUTHORIZED |
| M01_L02 Group 2 implementation | NOT AUTHORIZED |
| M01_L02 powered hardware | NOT AUTHORIZED; no hardware verification recorded |
| M01_L02 transition guide | IN_PROGRESS / NOT FINAL |
| M01_L02 Group 1 publication | NOT PERFORMED — User-owned; must include the full inherited lesson snapshot |
| M01 hardware readiness (L01) | PASS — accepted scoped User evidence; powered commissioning REAL HARDWARE DEFERRED |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED; protected candidate contents excluded |
| Phase 2 | COMPLETE WITH RECORDED LIMITS |
| Phase 3 / ACM-01–12 | FORMALLY CLOSED; ACM-12 60 CLOSED / 0 BLOCKED; F01/F02/F03 CLOSED |
| Phase 4 / M00_L17 | NOT STARTED / FORBIDDEN; M00_L17 NOT AUTHORIZED |
| Next boundary | Await Architect review of this Group 1 recording, then User-owned GOV3 Group 1 publication of the full inherited M01_L02 snapshot. |

## Evidence classification at Group 1

| Evidence scope | Classification / provenance |
| --- | --- |
| Inheritance / architecture audit | THEORY VERIFIED — static engineer audit accepted by the Architect |
| Inherited baseline build | Supplied User execution; not engineer execution |
| Group 2 build / tests, Simulation, Glass / Driver Station | NOT RUN / NOT TESTED — implementation not authorized |
| Real hardware | NOT AUTHORIZED / NOT TESTED; Design Lock is not hardware verification |
| Direction, inversion, stopping, current adequacy | REAL HARDWARE DEFERRED to later authorized M01 lessons |

## Authority, evidence and preservation limits

Known L01 hardware facts remain as recorded in the frozen [M01_L01 lesson plan evidence register](../../real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/LESSON_PLAN.md#hardware-readiness-evidence-register): Kraken X44; one motor; no follower; integrated Talon FX; CAN ID 40; bus rio; firmware 26.3.0.0; 10:1 ratio; no external sensors; safe zero YES on accepted pre-existing User evidence. The User clarified on 2026-10-09 that the earlier 35 A candidate was a supply limit; the Design Lock now records Supply 35 A and Stator 40 A.

All inherited Intake request paths (Right Bumper, RunIntakeCommand, IntakeToFeederCommand, PathPlanner LEARNING_EVENT, NamedCommands and direct semantic callers) retain Noop behavior until an authorized Group 2 change; the Design Lock requires IntakeIOReal to issue NeutralOut only. Swerve remains sole drivetrain/localization owner and autonomous safety boundaries are preserved.

Simple enough, safe enough, correct enough — do not over-engineer. Do not add automatic jam detection, reverse/eject, extra sensors, state machines, PID or velocity control, extra layers or speculative telemetry without a real requirement and later approval.

Root AGENTS is the constitution/entrypoint; seven delegated rules retain their scopes; ADR_GOV3 governs engineer roles and lesson publication mechanics from M01_L02 onward. English Documents A/B/C remain authoritative. Historical, candidate and consumed approvals grant no fresh permission. Unrelated dirty/untracked state and the prior hardware-registry search-scope incident remain preserved. Protected registry, T00 candidate and secrets were not accessed by this recording.

Later updates require accepted adjudication or an already-authorized gate, exact scope, prior-state preservation, semantic consistency review and User-owned publication. This cursor cannot authorize its own modification or future actions.
