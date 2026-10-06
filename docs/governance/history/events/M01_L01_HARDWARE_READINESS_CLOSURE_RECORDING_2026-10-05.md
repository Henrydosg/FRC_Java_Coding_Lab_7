# M01_L01 — Hardware Readiness Closure Recording — 2026-10-05

## Accepted decision and recording scope

- **Accepted source:** User-supplied Architect brief f96e63cd-cca0-4a1f-8eb5-e95a93f94772, M01_L01 HARDWARE READINESS CLOSURE RECORDING; the previous Intake prompt is SUPERSEDED and is not executed.
- **Decision:** HARDWARE READINESS = PASS on the specifically supplied User evidence. M01_L01 remains IN_PROGRESS, neither COMPLETE nor FROZEN, ready for Architect closure review.
- **Authorization:** Documentation/state recording only: four current lesson lifecycle files, their transition guide, CURRENT_STATE and this new event (seven paths). No prior event, frozen donor or technical file is changed.
- **Limits:** Runtime implementation and M01_L02 activation/implementation NOT AUTHORIZED. No powered Intake verification performed in L01/current recording; no project execution or Git writes. Architect closure, freeze, publication and independent review are not performed here.
- **Local documentation snapshot:** main / 5302c0b115849f8464a0d7d1d19a08820b19d099; read-only Git metadata corroborates the prior local Design Lock commit only. No remote publication or prior recording-review completion is inferred. This readiness recording has no commit/push identity.

## User hardware evidence and qualifications

| Hardware / disposition | Accepted current record |
| --- | --- |
| Motor | Kraken X44 |
| Motor count | 1 |
| Follower | NONE |
| Integrated controller | Talon FX / Kraken X44 integrated device |
| CAN ID | 40 |
| CAN bus | rio |
| Firmware | 26.3.0.0 |
| Mechanism ratio | 10 motor rotations : 1 intake roller rotation (10:1) |
| External sensors | NONE |
| Current-limit candidate | 35 A — USER PROVIDED / candidate; not final or applied |
| Safe zero | YES — When motor output = 0, the intake roller stops and this is physically safe. |
| Jam risk | Game piece can jam at the intake roller while the roller continues rotating. |
| Later recovery | Simple manual reverse/eject may be added in a later authorized lesson. |
| Automatic jam detection | Not inferred or authorized |
| NeutralMode | UNKNOWN — DEFER TO M01_L02 CONFIGURATION DESIGN; no Brake or Coast selection in L01 |

These are User-verified facts accepted by the Architect in supplied brief **f96e63cd-cca0-4a1f-8eb5-e95a93f94772**. HARDWARE READINESS = PASS is the scoped Architect readiness decision, not a commissioning or physical fault-test result. No physical fact is inferred from Noop behavior or another mechanism. No protected registry was read for this recording.

The recording date is 2026-10-05. The physical observation date/time, detailed hardware snapshot, method and conditions beyond the supplied zero-output statement were not supplied; they are not invented. Local HEAD 5302c0b115849f8464a0d7d1d19a08820b19d099 identifies the documentation snapshot only. Verification ownership remains with the User; the agent performed no hardware operation.

No powered Intake verification was performed in M01_L01 or by this recording. The safe-zero statement is accepted pre-existing User evidence. Powered direction/inversion, installed-library compatibility, application/readback of current limits, disable/E-stop execution, unavailable/device-failure response and competition readiness are not established by this evidence. Their applicable later qualification gates remain separate.

Current M01 ratio authority is 10:1. Historical 20:1 references remain historical only. The 35 A value is provisional, not final tuning or an applied setting. NeutralMode remains UNKNOWN and deferred; no Brake or Coast selection is made. Jam risk is recorded without automatic detection; manual reverse/eject requires later authorization.

## Architecture and lifecycle boundaries

Simple enough, safe enough, correct enough — do not over-engineer. Intended future control route: Xbox button → existing Command → IntakeSubsystem → IntakeIO → future IntakeIOReal → one Kraken X44. This is a future direction, not L01 implementation permission.

Do not add automatic jam detection, extra sensors, complex state machines, PID, closed-loop velocity control, extra subsystem layers, unnecessary abstractions, speculative telemetry or an additional safety framework without a real requirement and later approval. Simple manual reverse/eject may be added only in a later authorized lesson. Applicable registered roadmap and qualification gates remain intact; a new requirement needs scoped Architect authorization.

Frozen Backbone, vendor-neutral IO, subsystem-owned behavior/stop, immutable observations, read-only telemetry, scheduler-managed commands and RobotContainer composition remain unchanged. Existing non-actuating Noop paths remain intact. No Java, tests, Constants, IntakeIO/Noop/subsystem, RobotContainer, vendor dependencies, Gradle or deploy assets are modified; IntakeIOReal is not created. The User owns project verification and all Git writes.

Preparation, inherited baseline and Design Lock provenance remain historical accepted facts. This decision supersedes the prior pending hardware-readiness/collection boundary and replaces previously unknown inventory entries only with explicitly supplied evidence. It does not silently complete prior recording review, transition-guide finalization, closure, freeze or publication. The guide remains IN_PROGRESS / NOT FINAL / NOT PASS. T00 remains PARKED / NOT ACTIVATED; M00_L17 NOT AUTHORIZED.

## Prior-state preservation and supersession

The prior [CURRENT_STATE](../../CURRENT_STATE.md) is preserved byte-for-byte below: **9442 bytes**, SHA-256 **2ae47fd3b6dacbc39a81c5f4ec3559ad9e0c4dce41f29a6adcd5d93ff632dcf3**. Its pending-readiness wording is historical after the accepted decision above. Its original uncommitted-recording wording and older HEAD remain original stage statements; this annotation records the currently observed local commit without altering that body. The [prior Design Lock event](M01_L01_DESIGN_LOCK_AND_LIFECYCLE_INITIALIZATION_2026-10-05.md) and all prior events remain unchanged.

Relative links inside that body retain their original source context, docs/governance/CURRENT_STATE.md, and are resolved against docs/governance/; annotation links use this event file's directory.

The annotations above are outside the verbatim body. This archive is evidence, not fresh permission. Self-review is not independent review or Architect closure.

**Next boundary:** Await Architect closure review of M01_L01 before M01_L02 activation.

<!-- VERBATIM PRIOR CURRENT_STATE BEGIN -->
# Governance 2.0 Current State

> ACTIVE GOVERNANCE 2.0 CURRENT-STATE CURSOR
>
> CURRENT_STATE does NOT self-authorize. Its authority derives from [root AGENTS](../../AGENTS.md), applicable registered decisions and explicitly authorized state recording.

This is the single operational repository state/action projection. Architect brief **b4406659-f136-462b-84e7-d19a6f603de4** approves the M01_L01 Design Lock and authorizes bounded lesson lifecycle/documentation and state/history initialization. Preparation is COMPLETE, inheritance PASS, and M01_L01 is the sole **IN_PROGRESS** lesson for readiness/documentation only. Runtime implementation is **NOT AUTHORIZED**. Physical readiness remains **IN_PROGRESS / USER EVIDENCE REQUIRED**.

The [registered M01 roadmap](../architecture_decisions/ADR_M01_Real_Mechanism_Hardware_Integration_Roadmap.md) controls curriculum scope. The [Design Lock event and verbatim prior cursor](history/events/M01_L01_DESIGN_LOCK_AND_LIFECYCLE_INITIALIZATION_2026-10-05.md) preserve preparation-stage wording with provenance and supersession under root §6 and [GOV2 ADR §8](../architecture_decisions/ADR_GOV2_Governance_2_0_Agent_Instructions_State_and_History_Migration.md#8-current_state-authority-and-update-contract). The [preparation authorization event](history/events/M01_L01_PREPARATION_AUTHORIZATION_2026-10-04.md) and [roadmap registration event](history/events/M01_ROADMAP_REGISTRATION_2026-10-04.md) retain historical applicability. This recording is uncommitted; Architect review of the recording is PENDING and User-owned publication is NOT PERFORMED by this task. Design Lock approval is distinct from acceptance of its recording.

## Accepted operational cursor

| Item | Accepted projection / applicability |
| --- | --- |
| Learning-flow audit | COMPLETE / CLOSED; accepted 62 main + 17 historical/parallel = 79 represented lessons; this historical audit count is not recomputed by preparing M01 |
| Existing main progression | New WPILib Project → S00 → A00 → A01 → V00 → M00_L16; M01_L01 is the prepared IN_PROGRESS successor, not a completed endpoint |
| Historical/parallel lineage | D00 → D01 Tank lineage; NOT M00/M01 donor |
| Canonical completed endpoint / M00_L16 | M00_L16_MechanismAutonomousEventIntegration — COMPLETE / FROZEN / READ-ONLY |
| M00 | COMPLETE through L16; accepted SOFTWARE / ARCHITECTURE mechanism foundation; physical mechanisms deferred |
| Active editable lesson | M01_L01_MechanismHardwareReadinessAndIOContract — sole active lesson; readiness/documentation scope only |
| Governance 2.0 | COMPLETE / FORMALLY CLOSED / REMOTE VERIFIED; no open migration gate, G10 finding or active repair; no GOV2-G11 |
| Governance 2.0 final closure commit | d2b241aba86307a9909bf64f6bc74ce781373e06 — accepted historical closure/publication identity |
| Constants/configuration authority review | CLOSED — CFG-A01 DEFER; CFG-A02 DO NOT CHANGE; CFG-A03 DEFER; proposed cleanup change set NONE |
| M01 module identity | M01 — Real Mechanism Hardware Integration |
| M01 roadmap | APPROVED / REGISTERED / PUBLISHED / REMOTE VERIFIED — supplied Architect/User evidence |
| M01 roadmap publication identity | b551fb4b1335acdbc553ab0861c5873a7a107893 — accepted historical roadmap publication identity; no new live remote verification |
| M01_L01 preparation authorization | APPROVED / PUBLISHED / REMOTE VERIFIED — supplied Architect/User evidence; permission consumed by User preparation |
| Repository HEAD / branch at recording | 1a391e978e8595b636103b168558566c37d54aa1 / main; local read-only corroboration, not the future recording commit |
| M01_L01 identity / title | M01_L01_MechanismHardwareReadinessAndIOContract — Mechanism Hardware Readiness and IO Contract |
| M01_L01 donor | real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/ — COMPLETE / FROZEN / READ-ONLY |
| M01_L01 target | real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/ |
| M01_L01 preparation | COMPLETE — User copy/rename, generated-artifact cleanup and bounded nested-copy repair |
| M01_L01 inherited baseline | Successful initial and post-repair inherited clean Gradle baselines, supplied User execution; NOT RUN by agent |
| M01_L01 inheritance | PASS before lifecycle initialization; 349/349 byte-equivalent project files; changed/missing/unexpected 0; nested duplicate ABSENT |
| M01_L01 Design Lock | APPROVED by Architect; HARDWARE READINESS / CONTRACT FOUNDATION |
| M01_L01 lifecycle | IN_PROGRESS; not COMPLETE or FROZEN |
| M01_L01 runtime implementation | NOT AUTHORIZED; Java / Constants / Real IO / powered actuation required in L01 = NO |
| M01_L01 Intake IO verdict | SUFFICIENT FOR L01 BUT LIKELY EXTENSION REQUIRED LATER; no L01 interface extension |
| M01 hardware readiness | IN_PROGRESS / USER EVIDENCE REQUIRED; actual physical facts UNKNOWN; powered operation REAL HARDWARE DEFERRED |
| M01_L01 transition guide | IN_PROGRESS / NOT FINAL / NOT PASS |
| M01_L01 Design Lock recording | Documentation/state initialized in working tree; Architect recording review PENDING |
| Git publication of this recording | NOT PERFORMED; no future commit/push SHA claimed; User owns ALL Git writes |
| M01_L02 | NOT AUTHORIZED |
| T00 | PARKED / NOT REGISTERED / NOT ACTIVATED; protected candidate contents excluded |
| Phase 2 | COMPLETE WITH RECORDED LIMITS |
| Phase 3 / ACM-01–12 | FORMALLY CLOSED; ACM-12 60 CLOSED / 0 BLOCKED; F01/F02/F03 CLOSED |
| Phase 4 / M00_L17 | NOT STARTED / FORBIDDEN; M00_L17 NOT AUTHORIZED |
| Next boundary | Await Architect review of Design Lock recording before collecting User hardware-readiness evidence. |

## Authority, evidence and preservation limits

The original nested-copy preparation HOLD and bounded User repair remain historical evidence. The current Architect brief accepts post-repair reinspection PASS; a fresh recording preflight corroborated nested absence and 349/349 byte equality before document initialization. After this recording, only four target lifecycle files differ and the required guide is added; technical bytes remain unchanged. The donor and its lifecycle documents are protected and unchanged.

Known software facts: IntakeIO updateInputs/requestIntake/stop; available/connected Inputs; non-actuating IntakeIONoop; subsystem-owned semantic forwarding/refresh and immutable observations; scheduler-managed RunIntakeCommand and IntakeToFeederCommand; read-only telemetry; RobotContainer composition root. All Intake request paths (Right Bumper, direct RunIntakeCommand, IntakeToFeederCommand, PathPlanner LEARNING_EVENT, NamedCommands and direct semantic callers) retain Noop behavior. Swerve remains sole drivetrain/localization owner and autonomous safety boundaries are preserved.

The [lesson plan](../../real_robot_programming/module_M01/M01_L01_MechanismHardwareReadinessAndIOContract/LESSON_PLAN.md) records required physical facts as UNKNOWN / USER VERIFICATION REQUIRED, future configuration categories without values and future qualified Real/non-real composition without implementation. Actual inventory, compatibility, safety, safe zero, disable/E-stop and unavailable/device-failure expectations require User evidence after recording review. No protected registry is consumed. Measurements, powered direction/inversion, commissioning bounds, gains/feedforward/final tuning and SysId are deferred to later lessons unless evidence establishes a basic safety need requiring Architect reconciliation.

Root AGENTS is the constitution/entrypoint; seven delegated rules retain their scopes. English Documents A/B/C remain authoritative; the [manifest](../GOVERNANCE_DOCUMENT_MANIFEST.md) indexes PDF/mirror integrity, not semantic authority or ADR activation. [LEARNING_FLOW_MAP](LEARNING_FLOW_MAP.md) remains derived/non-authorizing and unchanged. Historical/candidate/consumed approvals grant no fresh permission. Design Lock recording does not authorize runtime changes, Real IO, powered operation, completion/freeze, publication or successor work.

Phase-2 physical-lineage qualifications, M00 Noop/physical deferral, provisional configuration and historical Simulation/hardware applicability remain preserved. D01_L11 final clean build remains NOT ESTABLISHED IN RETAINED EVIDENCE. Camera/localization/tuning qualifications retain accepted scope. No new test/build, Simulation, Glass/AdvantageScope, Driver Station, real hardware, SysId or tuning operation was performed; supplied baseline success is not new agent execution. Physical transfer, homing, travel protection, regulation and competition readiness are not established.

Published/remote-verified roadmap and preparation statuses come from accepted Architect/User evidence; no live remote verification repeated here. Primary/metadata/checkpoint/publication identities and this uncommitted record remain separate. Unrelated dirty/untracked state and the prior hardware-registry search-scope incident remain preserved. Protected registry, T00 candidate and recovery-code contents were not accessed by this recording.

Later updates require accepted adjudication or an already-authorized gate, exact scope, prior-state preservation, semantic consistency review and User-owned publication. This cursor cannot authorize its own modification or future actions. Initialization self-review is not independent review or Architect acceptance of the recording.

<!-- VERBATIM PRIOR CURRENT_STATE END -->
