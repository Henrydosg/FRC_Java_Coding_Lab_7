# Governance 2.0 Current State

> ACTIVE GOVERNANCE 2.0 CURRENT-STATE CURSOR
>
> CURRENT_STATE does NOT self-authorize. Its authority derives from [root AGENTS](../../AGENTS.md), applicable registered decisions and explicitly authorized state recording.

This is the single operational repository state/action projection. Under [ADR_GOV3](../architecture_decisions/ADR_GOV3_Engineer_Roles_and_Lightweight_Lesson_Publication.md) Group 2, the Architect brief "M01_L02 — GROUP 2 USER VERIFICATION RECORDING" (2026-10-10) ACCEPTS the User software verification and the User Simulation (Noop scope) and authorizes this recording. **M01_L02 = IN_PROGRESS** and is the sole active editable lesson. Group 1 is published (commit 9bdc4f8). **Group 2 implementation is recorded; Group 2 publication is AUTHORIZED TO PREPARE, NOT YET EXECUTED. Powered hardware: NOT AUTHORIZED. Group 3: NOT AUTHORIZED.** This recording is in the working tree, self-reviewed only; Architect review and User-owned Group 2 publication are pending. No future commit or push identity is claimed.

The [M01_L02 Group 2 verification recording event and verbatim prior cursor](history/events/M01_L02_GROUP2_USER_VERIFICATION_RECORDING_2026-10-10.md) preserves the superseded Group 1 cursor. The [Group 1 activation event](history/events/M01_L02_GROUP1_ACTIVATION_RECORDING_2026-10-09.md), [GOV3 adoption event](history/events/GOV3_ADOPTION_RECORDING_2026-10-09.md) and earlier events remain unchanged history. The [registered M01 roadmap](../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) controls curriculum scope; M01 Roadmap Revision 2 was NOT APPROVED and the registered 18-lesson roadmap remains controlling.

## Accepted operational cursor

| Item | Accepted projection / applicability |
| --- | --- |
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
| M01_L02 Primary Engineer | Claude Code; Codex optional independent reviewer before Group 3 |
| M01_L02 Design Lock | APPROVED — D1–D9; Intake Real IO configuration and safe zero output ONLY |
| M01_L02 locked configuration | One Kraken X44 (integrated Talon FX), no follower; CAN 40 on rio; NeutralMode BRAKE; Stator 40 A enabled; Supply 35 A enabled (40 A breaker); inversion and ratio constant not configured in L02; initial values, not proof of jam force or motion performance |
| M01_L02 failure policy | Configuration failure fails closed for the session (available/connected false, error reported once); no runtime retry — F3 ACCEPTED |
| M01_L02 Group 1 publication | PUBLISHED — User-owned commit 9bdc4f80d3321b67d4054c46bed31f18413f185e "Activate M01_L02 and record inherited baseline"; User-reported, accepted by the Architect; local read-only HEAD = origin/main observation (local tracking ref) |
| M01_L02 Group 2 implementation | AUTHORIZED and implemented in the working tree: NEW IntakeIOReal.java (NeutralOut-only), IntakeIORealConfigurationTest.java; MODIFY Constants.java (IntakeConstants only), RobotContainer.java (RobotBase.isReal() Real/Noop selection only), IntakeArchitectureBoundaryTest.java; Architect-ACCEPTED bounded inherited repair of one RobotContainerIntakeCommandBindingTest assertion |
| M01_L02 Group 2 verification | User build/tests ACCEPTED; User Simulation (Noop scope) ACCEPTED; Group 2 NOT COMPLETE |
| M01_L02 Group 2 publication | AUTHORIZED TO PREPARE, NOT YET EXECUTED — User-owned; after Architect review of this recording |
| M01_L02 powered hardware | NOT AUTHORIZED; H1–H3 NOT TESTED |
| M01_L02 transition guide | IN_PROGRESS / NOT FINAL |
| M01_L02 Group 3 | NOT AUTHORIZED |
| M01 hardware readiness (L01) | PASS — accepted scoped User evidence; powered commissioning REAL HARDWARE DEFERRED |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED; protected candidate contents excluded |
| Phase 2 | COMPLETE WITH RECORDED LIMITS |
| Phase 3 / ACM-01–12 | FORMALLY CLOSED; ACM-12 60 CLOSED / 0 BLOCKED; F01/F02/F03 CLOSED |
| Phase 4 / M00_L17 | NOT STARTED / FORBIDDEN; M00_L17 NOT AUTHORIZED |
| Next boundary | Await Architect review of this Group 2 recording, then User-owned Group 2 implementation publication. Powered verification and Group 3 each require separate authorization. |

## Evidence classification at Group 2

| Evidence scope | Classification / provenance |
| --- | --- |
| Inheritance / architecture audit | THEORY VERIFIED — static engineer audit accepted by the Architect |
| Inherited baseline build | Supplied User execution (Group 1) |
| Group 2 build | BUILD VERIFIED — User, 2026-10-10: `.\gradlew clean build`, BUILD SUCCESSFUL in 34s, 7 actionable tasks: 7 executed, WPILib JDK 17; accepted by the Architect |
| Group 2 tests | SOFTWARE TEST VERIFIED — User, 2026-10-10: 895 tests, 0 failures, 0 errors, 0 skipped; accepted by the Architect |
| Simulation | SIMULATION VERIFIED — NOOP SCOPE ONLY — User, 2026-10-10: Right Bumper INTAKE_REQUESTED, release STOPPED, Available/Connected false, no console errors reported; IntakeIOReal was not simulated |
| Glass / Driver Station | NOT TESTED |
| Real hardware | NOT AUTHORIZED / NOT TESTED |
| Direction, inversion, stopping, current adequacy | REAL HARDWARE DEFERRED to later authorized M01 lessons |

Earlier engineer-run results (14/14 configuration tests, 3/3 focused binding tests, 895/895 full suite, BUILD SUCCESSFUL) remain preliminary convenience evidence and are superseded for gate purposes by the accepted User results.

## Authority, evidence and preservation limits

Known L01 hardware facts remain as recorded in the frozen [M01_L01 lesson plan evidence register](../../real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/LESSON_PLAN.md#hardware-readiness-evidence-register): Kraken X44; one motor; no follower; integrated Talon FX; CAN ID 40; bus rio; firmware 26.3.0.0; 10:1 ratio; no external sensors; safe zero YES on accepted pre-existing User evidence.

All Intake request paths (Right Bumper, RunIntakeCommand, IntakeToFeederCommand, PathPlanner LEARNING_EVENT, NamedCommands and direct semantic callers) reach hardware only through IntakeIO.requestIntake(). On the real robot IntakeIOReal issues NeutralOut only; off-robot builds, tests and Simulation select IntakeIONoop. Swerve remains sole drivetrain/localization owner and autonomous safety boundaries are preserved.

Simple enough, safe enough, correct enough — do not over-engineer. Do not add automatic jam detection, reverse/eject, extra sensors, state machines, PID or velocity control, extra layers or speculative telemetry without a real requirement and later approval.

Root AGENTS is the constitution/entrypoint; seven delegated rules retain their scopes; ADR_GOV3 governs engineer roles and lesson publication mechanics from M01_L02 onward. English Documents A/B/C remain authoritative. Historical, candidate and consumed approvals grant no fresh permission. Unrelated dirty/untracked state and the prior hardware-registry search-scope incident remain preserved. Protected registry, T00 candidate and secrets were not accessed by this recording.

Later updates require accepted adjudication or an already-authorized gate, exact scope, prior-state preservation, semantic consistency review and User-owned publication. This cursor cannot authorize its own modification or future actions.
