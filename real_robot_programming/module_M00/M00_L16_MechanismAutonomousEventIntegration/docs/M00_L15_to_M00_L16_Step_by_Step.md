# M00_L15 to M00_L16 — Step by Step Transition Guide
<!-- ACM-01 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-01 formal domain closure — 2026-10-01

The Architect explicitly authorized formal domain closure of ACM-01 — Package / Lesson Architecture Boundaries — following Sol's independent domain rereview. Sol recommended closure with token:
PASS_ACM_01_DOMAIN_REREVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION
The Architect owns and made the formal closure decision; Sol supplied independent review evidence. Earlier F01-F04 lifecycle snapshots remain stage evidence; their former HOLD and next-gate wording predates this ACM-01 domain closure and does not control current status.

ACM-01: FORMALLY CLOSED / FORMALLY RECORDED. The rereview found no remaining current M00_L16 package or lesson architecture-boundary defect. All twelve domain closure dimensions are CLOSED. Static governance preflight PASS: 12 source PDFs, 12 matching source hashes, zero deterministic findings. ACM-01-F05: NOT ESTABLISHED; no F05 finding or placeholder record exists.

Package inventory: frc.robot lifecycle, composition-root and constants responsibilities
remain CORRECT at the ACM-01 boundary. Root commands correctly contain teleop and
mechanism actions, localization/reset and validation/commissioning commands, and
command-side provenance/coordination. commands.auto correctly owns autonomous
actions, compositions, factories, adapters and event registration. autonomous
correctly owns stable event identity. util contains generic/pure helpers.
Mechanism observation models follow mechanism subpackages. controls, subsystems,
io, telemetry and vision remain correct at package-inventory level for ACM-01;
their deeper contracts belong to later ACM domains.

AutonomousStartContext remains correctly in root commands as immutable command-side
provenance. AutonomousEventId remains correctly in autonomous as stable semantic
identity. FieldAllianceTransform remains a pure, explicitly parameterized field
geometry transformation. SwerveObservation and DriveThreeMeterValidationObservation
remain in observation.swerve.

F01 and F01-DOC-01 remain CLOSED; F02 and F03 remain CLOSED / FORMALLY RECORDED; F04 remains FORMALLY CLOSED / FORMALLY RECORDED. The 14-member F04 production family and its dedicated tests remain in commands.auto; the exact family is preserved in [the F04 repair record, Sections 1 and 4](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md). LearningTrajectoryFactory and its dedicated test remain in commands.auto.

Independent static inventory: 113 production Java files and 109 test Java files
scanned; zero package declaration/path mismatches. These are source-file counts,
not test counts. Repaired tests remain colocated. Old F01-F04 qualified identities
in placement assertions are intentional negative checks, not stale executable
imports. M00_L16 remains a continuation of M00_L15 with one teaching concept: dispatch the existing IntakeToFeederCommand through the inherited LEARNING_EVENT boundary. No ACM-01 repair introduced a teaching concept. Predecessor lesson copies remain historical; no repair was propagated backward. The narrow Frozen Backbone rereview found the composition root, concrete IO ownership, subsystem APIs, observation and telemetry hierarchies, scheduler-managed commands, explicit stop paths, Real/Simulation implementations, and Constants authority intact; it found no manual ownership flags or scheduler-polling arbitration. This is not a closure claim for later ACM domains.

CF-U and PF-U remain CAUSE NOT ESTABLISHED. The unrelated A01_L06_OneMeter_Forward.path state remains untouched. Prior protection evidence covers named-file hashes only; no aggregate protected-content digest PASS is claimed. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

Post-closure cursor: ACM-01 FORMALLY CLOSED / FORMALLY RECORDED. ACM-02 is the next prospective domain, NOT STARTED and NOT ACTIVATED. ACM-03 through ACM-12 remain NOT STARTED. Phase 3 is IN PROGRESS; Phase 4 is NOT STARTED / FORBIDDEN. No M00_L17. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 closure does not authorize re-freeze, publication, Constants.java cleanup/refactor, a later ACM activation, or Phase 4.

The repository has reached a domain-closure checkpoint suitable for a later User-owned Git checkpoint workflow. No commit or tag identity is claimed. This recording changes existing lifecycle documentation only: no Java/test, authoritative A/B/C, manifest, VERIFIED mirror, historical lesson source, dependency, deployment asset, or protected file was changed. No Git write or project execution occurred.

<!-- ACM-01 DOMAIN CLOSURE CURRENT END -->


<!-- ACM-01-F04 IMPLEMENTATION STAGE SNAPSHOT BEGIN -->
## Historical ACM-01-F04 implementation-stage snapshot — 2026-09-30

The Architect-authorized registration below preceded the F04 Java edits.
The fourteen autonomous production commands and their fourteen dedicated
tests now reside in `frc.robot.commands.auto`; their twenty-eight old paths
are absent. `RobotContainer` has only seven import changes, and the seven
approved nonrelocating tests have only necessary import changes. Exactly
**36 existing Java identities** changed, with zero new Java identities.
One fixed fourteen-type placement guard was added to the relocated
`AutonomousEventRegistrationTest`. Reverse-normalized comparison to the
pre-F04 F01/F02/F03 working state matched all 36 identities; protected
F03/value/Constants and the unrelated A01_L06 path hashes are unchanged.
[The F04 ADR, Section 2](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md#2-bounded-implementation-and-static-self-audit--2026-09-30) records this static self-audit.
No Gradle, tests, build, Simulation or hardware verification was executed.

**F04: REGISTERED / IMPLEMENTED / UNVERIFIED / PENDING USER VERIFICATION.**
F01, F01-DOC-01, F02 and F03 remain CLOSED; P3-H01 CLOSED / PRESERVED;
CFG-H01 CLOSED / REMOVED; CF-U and PF-U CAUSE NOT ESTABLISHED.
M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; ACM-01 and
Phase 3 remain HOLD; ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN.
No M00_L17. Exact next gate: User-owned Java-17 environment confirmation
and ordered F04 automated Gates 1–4, then independent post-implementation
review. No F04 closure, re-freeze, publication or Git write is claimed.

<!-- ACM-01-F04 IMPLEMENTATION STAGE SNAPSHOT END -->

<!-- ACM-01-F04 REGISTRATION SNAPSHOT BEGIN -->
## Historical ACM-01-F04 authorized registration — 2026-09-30

The Architect accepted and activated ACM-01-F04 for an M00_L16-only
package-ownership repair under Document A Section 8. The canonical target is
`frc.robot.commands.auto` for the fixed fourteen-member autonomous command
family. [The F04 repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md) records each type and historical
origin, the exact 14 production relocations + RobotContainer import-only
change + 14 dedicated-test relocations + seven test-consumer changes =
**36 existing Java identities, zero new Java identities**. Historical copies
and unrelated working state remain protected. This is the registration stage,
recorded before F04 Java edits; implementation, User verification,
independent review, documentation reconciliation and closure are not claimed
at registration.

The package-only contract preserves command behavior, CommandScheduler and
deferred requirements, safe stop, PathPlanner resources, and the existing
`LEARNING_EVENT` fresh `IntakeToFeederCommand` supplier with
`Set.of(intakeSubsystem, feederSubsystem)`. Exactly one fixed fourteen-type
placement guard is authorized in the relocated
`AutonomousEventRegistrationTest`. F03 `LearningTrajectoryFactory` and its
test, root `AutonomousStartContext`, and semantic `AutonomousEventId`
remain outside the F04 edit boundary. Future User verification requires
Java 17, then ordered clean/focused auto-package tests, seven consumer tests
plus `IntakeArchitectureBoundaryTest`, full suite and clean build.
Interactive Simulation is not required; Glass/Driver Station are not
applicable; real hardware is not required for this package-only repair if
behavior remains unchanged.

F04 is REGISTERED / IMPLEMENTATION AUTHORIZED. F01, F01-DOC-01, F02 and F03
remain CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED;
CF-U and PF-U CAUSE NOT ESTABLISHED. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED; ACM-01 and Phase 3 remain HOLD;
ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN. No M00_L17.
Exact next gate: bounded F04 implementation and static self-audit, followed
by User-owned Java-17 Gates 1–4 and independent implementation review.
No Git write, verification execution, re-freeze or publication is recorded.

<!-- ACM-01-F04 REGISTRATION SNAPSHOT END -->

<!-- ACM-01-F03 FORMAL CLOSURE SNAPSHOT BEGIN -->
## Historical ACM-01-F03 formal repair closure — 2026-09-30

After Sol's independent final closure review
`PASS_ACM_01_F03_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`,
the Architect explicitly authorized ACM-01-F03 REPAIR CLOSURE.
The authorization is now formally recorded: **ACM-01-F03: CLOSED**.
Technical, verification, documentation and architecture closure
dimensions are **CLOSED**; remaining F03 repair requirements: **NONE**.
[The F03 ADR, Section 4](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md#4-formal-acm-01-f03-repair-closure--2026-09-30) governs this decision. The
reconciliation, implementation and registration blocks below preserve
their earlier stages.

The approved M00_L16-only correction is
`frc.robot.commands.auto.LearningTrajectoryFactory`:
four changed existing Java identities, zero new Java identities, no
production method-body change or alias. Ten behavioral tests and one
bounded placement guard are preserved. Required User Java-17 automated
Gates 1-4 PASS; Gate 3 reported UP-TO-DATE tasks, while Gate 4's clean
build reported seven actionable tasks executed. Those tasks are not a
numeric test count. Independent implementation and final reviews PASS.

F04 remains ACCEPTED / PARKED. F01, F01-DOC-01 and F02 remain CLOSED;
P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED; CF-U and PF-U
CAUSE NOT ESTABLISHED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED. ACM-01 and Phase 3 remain HOLD;
ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN. No M00_L17.
No aggregate protected-content digest match was established; the
unrelated A01_L06 path remains in its pre-existing modified state.
Exact next gate: ARCHITECT ACTIVATION / DESIGN AUTHORIZATION FOR ACM-01-F04.

Active State: REOPENED / IN_PROGRESS / EDITABLE for separately
authorized scope only. Repository Active Lesson Count: 1;
Active M00 Lesson Count: 1; Current Active M00 Lesson: M00_L16.
F03 closure does not re-freeze, republish or complete this lesson.

<!-- ACM-01-F03 FORMAL CLOSURE SNAPSHOT END -->

<!-- ACM-01-F03 RECONCILED SNAPSHOT BEGIN -->
## Historical ACM-01-F03 documentation/evidence reconciliation — 2026-09-30

The F03 registration and bounded implementation snapshots below remain
historical stages. The Architect-approved M00_L16-only move to
`frc.robot.commands.auto.LearningTrajectoryFactory` changed exactly
four existing Java identities and introduced zero new Java identities.
The User completed the required WPILib Java-17 environment check and
ordered automated Gates 1-4; all PASS. Gate 3 reported its full-suite
tasks UP-TO-DATE, while Gate 4's clean build reported all seven actionable
tasks executed. These task counts are not test counts.
Independent post-implementation review PASS:
`PASS_ACM_01_F03_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
[The F03 repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md) now reconciles the accepted evidence.

F03 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW;
F03 is NOT CLOSED. F04 remains ACCEPTED / PARKED in order F03 -> F04.
F01, F01-DOC-01 and F02 remain CLOSED; P3-H01 CLOSED / PRESERVED;
CFG-H01 CLOSED / REMOVED; CF-U and PF-U CAUSE NOT ESTABLISHED.
M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED;
ACM-01 and Phase 3 remain HOLD; ACM-02 NOT STARTED; Phase 4
NOT STARTED / FORBIDDEN. No M00_L17.
Exact next gate: INDEPENDENT FINAL ACM-01-F03 ARCHITECTURE / CLOSURE REVIEW.

The relocated test preserves ten behavioral tests and adds one narrow
placement guard. Source/test behavior and prior F01/F02 working-state
changes were preserved by reverse-normalized SHA-256 comparisons.
No interactive Simulation, Glass, Driver Station or real-hardware
verification was required for this repair.

<!-- ACM-01-F03 RECONCILED SNAPSHOT END -->

<!-- ACM-01-F03 IMPLEMENTATION SNAPSHOT BEGIN -->
## Historical ACM-01-F03 bounded implementation — 2026-09-30

The F03 registration below was recorded before Java edits. The authorized
M00_L16-only four-existing-Java-identity relocation is IMPLEMENTED and
STATICALLY SELF-AUDITED. The production factory and its dedicated test now
reside in `frc.robot.commands.auto`. Exactly two external test imports
were updated; the dedicated test retains all ten existing behavioral tests
and adds one narrow placement guard. Reverse-normalized byte comparisons
against the pre-F03 working-state baseline PASS for all four identities.

F03 is REGISTERED / IMPLEMENTED / UNVERIFIED / PENDING USER VERIFICATION.
No Gradle, test, build, Simulation or hardware execution is claimed.
The next gate is User-owned Java-17 environment confirmation and the
ordered clean/focused, two consumer tests, full-suite and clean-build gates
in the dedicated F03 ADR. F04 remains ACCEPTED / PARKED.
F01, F01-DOC-01 and F02 remain CLOSED; P3-H01 CLOSED / PRESERVED;
CFG-H01 CLOSED / REMOVED. CF-U and PF-U remain CAUSE NOT ESTABLISHED.
M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED;
ACM-01 HOLD; Phase 3 HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE;
ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN. No M00_L17.
Earlier F03 registration and F02 next-gate wording is historical.

<!-- ACM-01-F03 IMPLEMENTATION SNAPSHOT END -->

<!-- ACM-01-F03 REGISTRATION BEGIN -->
## Historical ACM-01-F03 authorized registration — 2026-09-30

The Architect activated ACM-01-F03 and authorized its M00_L16-only
bounded implementation after the independent design token:
`PASS_ACM_01_F03_REPAIR_DESIGN_READY_FOR_ARCHITECT_IMPLEMENTATION_AUTHORIZATION`.
The approved canonical identity is
`frc.robot.commands.auto.LearningTrajectoryFactory`.
The A01_L03-inherited util placement conflicts with AGENTS Section 4 and
Document A Sections 2 and 8. [The dedicated F03 repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md)
records the decision, exact four-Java-identity boundary, preservation
requirements and future User verification plan.

This entry is the registration stage, recorded before any F03 Java edit.
F03 is ACCEPTED / ACTIVATED / IMPLEMENTATION AUTHORIZED; implementation,
User verification, independent review, reconciliation and closure are
PENDING at registration. Exactly four existing Java identities and
zero new Java identities are authorized. F04 remains ACCEPTED / PARKED
in the locked order F03 -> F04.

F01 and F01-DOC-01 remain CLOSED; F02 CLOSED / FORMALLY RECORDED;
P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED. CF-U and PF-U
remain CAUSE NOT ESTABLISHED. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED. Repository Active Lesson Count: 1;
Active M00 Lesson Count: 1; Current Active M00 Lesson: M00_L16.
ACM-01 remains HOLD; Phase 3 remains
`HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`;
ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN. No M00_L17.
Prior F02 references to F03 as parked are historical at F02 closure.
The next gate is bounded F03 implementation and static self-audit,
followed by User-owned verification. No re-freeze or publication.

This appendix records the new exceptional package repair stage while
preserving all original transition steps and earlier repair evidence.
<!-- ACM-01-F03 REGISTRATION END -->

<!-- ACM-01-F02 CURRENT BEGIN -->
## Current ACM-01-F02 formal repair closure — 2026-09-30

The Architect explicitly authorized ACM-01-F02 REPAIR CLOSURE after the
accepted independent final architecture/closure review:
`PASS_ACM_01_F02_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`.
The subsequent authorization is now formally recorded. **ACM-01-F02: CLOSED**.
Technical, verification, documentation and architecture dimensions are
**CLOSED**. Remaining F02 repair requirements: **NONE**.
The governing closure decision is [the F02 ADR, Section 3](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#3-formal-acm-01-f02-repair-closure--2026-09-30).

The original finding was valid; the canonical `observation.swerve` target is
correct. Registration preceded code. The five-existing-plus-one-new Java
boundary, semantic preservation, no compatibility alias and removal of the
old production identity were independently verified. Required User automated
gates, independent implementation review, documentation reconciliation and
independent final closure review PASS. The initial Java-8 configuration
failure and corrected Java-17 evidence remain historical truth in ADR
Section 2; no unsupported explanation for Java-8 selection is added.
Frozen Backbone and historical lessons remain preserved.

M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED.
Active State: REOPENED / IN_PROGRESS / EDITABLE for separately authorized
scope only. Repository Active Lesson Count: 1; Active M00 Lesson Count: 1;
Current Active M00 Lesson: M00_L16. No M00_L17.
F01 and F01-DOC-01 remain CLOSED; P3-H01 CLOSED / PRESERVED;
CFG-H01 CLOSED / REMOVED. CF-U and PF-U remain CAUSE NOT ESTABLISHED.
F03 and F04 remain ACCEPTED / PARKED. ACM-01 remains HOLD; Phase 3 remains
HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE; ACM-02 NOT STARTED;
Phase 4 NOT STARTED / FORBIDDEN.
Exact next gate: **ARCHITECT ACTIVATION / DESIGN AUTHORIZATION FOR ACM-01-F03**.

The earlier reconciliation, registration and lifecycle records below are
preserved historical stages. Their former current/pending wording applies
to those stages; this summary and ADR Section 3 govern the recorded F02
closure. This action records F02 closure only; source/tests, protected state
and Constants.java cleanup/refactor remain untouched.

The appended F02 formal-closure record supplements the preserved transition
steps and earlier repair evidence; it does not finalize a re-freeze.

<!-- ACM-01-F02 CURRENT END -->

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT BEGIN -->
## Historical ACM-01-F02 documentation/evidence reconciliation — 2026-09-30

The Architect-authorized F02 documentation/evidence reconciliation is
complete. The accepted package-only implementation is five changed existing
Java identities plus one focused placement test; all required User automated
gates and independent implementation review PASS. The initial Java 8
configuration failure and corrected Java 17 sequence are preserved in
[the F02 ADR, Section 2](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#2-accepted-implementation-and-evidence-reconciliation--2026-09-30).
The registration-stage record below remains historical stage evidence.

F02 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
F02 is NOT CLOSED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED. F01 and DOC-01 remain
CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED.
Exact next gate: INDEPENDENT FINAL ACM-01-F02 ARCHITECTURE / CLOSURE REVIEW.

The appended F02 evidence reconciliation below records the later exceptional
repair stages. Original transition steps and all F01 history are preserved.

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT END -->

<!-- ACM-01-F02 REGISTRATION BEGIN -->
## ACM-01-F02 exceptional repair registration — 2026-09-30

This is an added F02 pre-implementation stage; preserved historical
transition steps remain unchanged. The Architect accepted the S00_L23
inherited drive validation observation package finding under Document C
OC-02 Section 1. M00_L16 alone may move the observation into
`frc.robot.observation.swerve`, update three production imports and one
existing test import, and add one focused placement test. The exact
six-Java-identity boundary and verification plan are in
[the F02 repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md).

At registration, implementation, verification, independent review, and
closure are PENDING. F01/DOC-01 remain CLOSED; F03/F04 are ACCEPTED /
PARKED. M00_L16 is IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED;
ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED / FORBIDDEN.
Next: bounded F02 implementation and User automated verification, then
independent review. Historical lessons remain untouched.

<!-- ACM-01-F02 REGISTRATION END -->

<!-- ACM-01-F01 CURRENT BEGIN -->
## Current ACM-01-F01 formal repair closure — 2026-09-30

The Architect explicitly authorized ACM-01-F01 closure after independent
final review PASS_ACM_01_F01_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION. The prior
pre-closure snapshot below is historical; its PENDING wording applies to
2026-09-29, not the current state.

The later Architect-adjudicated ACM-01-F01-DOC-01 found eight stale current
Section 9 pointers. This bounded correction points them to ADR Section 10,
which retains the formal closure decision. Independent documentation rereview
is pending before ACM-01 domain rereview.

- ACM-01-F01: CLOSED / FORMALLY RECORDED. At formal closure, technical,
  verification, documentation and architecture dimensions were CLOSED and
  remaining repair requirements were NONE. ACM-01-F01-DOC-01 is a bounded
  post-closure documentation consistency finding. Technical, verification
  and architecture closure remain CLOSED. Documentation: REMEDIATED —
  PENDING INDEPENDENT DOCUMENTATION REREVIEW; the consistency gate remains
  OPEN until that rereview.
- M00_L16: Status IN_PROGRESS; Active State REOPENED / IN_PROGRESS / EDITABLE.
  The exceptional repair is CLOSED; subsequent audit-domain and re-freeze
  lifecycle gates remain. No source/test edit is authorized by this record.
  Repository Active Lesson Count: 1; Active M00 Lesson Count: 1;
  Current Active M00 Lesson: M00_L16. No re-freeze or new publication.
- The initial compileJava failure remains historical. CF-U — CAUSE NOT
  ESTABLISHED. THE EARLIER FAILURE DID NOT REPRODUCE AFTER CONTROLLED CLEAN
  REPRODUCTION. PF-U — CAUSE NOT ESTABLISHED: historical 12,119 -> 12,118;
  the original path/hash manifest is unavailable. Both remain unresolved
  historical limitations and do not block this formal repair closure.
- Accepted User controlled clean reproduction, focused tests including all six
  SwerveObservationTest tests, full suite and clean build: PASS. Independent
  implementation, documentation and final closure reviews: PASS. Focused/full
  tests and clean build: REQUIRED / COMPLETE / PASS. Simulation: NOT REQUIRED
  FOR REPAIR CLOSURE; Glass and Driver Station: NOT APPLICABLE; real hardware:
  NOT REQUIRED FOR REPAIR CLOSURE. Existing physical-evidence limits persist.
- Frozen Backbone and historical lessons preserved. P3-H01: CLOSED / PRESERVED;
  CFG-H01: CLOSED / REMOVED.
- ACM-01: HOLD; domain rereview deferred until independent DOC-01 rereview.
  ACM-02: NOT STARTED.
  Phase 3: HOLD / HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE.
  Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.
- Exact next gate: INDEPENDENT READ-ONLY ACM-01-F01-DOC-01 DOCUMENTATION REREVIEW.
- Governing chronology, evidence and closure: [dedicated ACM-01-F01 repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

<!-- ACM-01-F01 CURRENT END -->

<!-- ACM-01-F01 PRE-CLOSURE SNAPSHOT BEGIN -->
## Historical ACM-01-F01 pre-closure documentation/evidence reconciliation — 2026-09-29

This Architect/User-authorized summary controls current M00_L16 repair status.
The preserved registration, failed-verification, frozen and publication passages
describe their earlier stages, including any former CURRENT/PENDING wording.

- Status: IN_PROGRESS; Active State: REOPENED / IN_PROGRESS / EDITABLE.
- M00_L16: EXCEPTIONAL REPAIR IN PROGRESS; sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Repository Active Lesson Count: 1; Active M00 Lesson Count: 1; Current Active M00 Lesson: M00_L16.
- ACM-01-F01: IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED / DOCUMENTATION RECONCILED; independent final closure review PENDING.
- Accepted User automated verification: all required gates PASS.
- Accepted independent Sol review: `PASS_ACM_01_F01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
- Initial compileJava failure remains historical; CF-U — CAUSE NOT ESTABLISHED.
- PF-U — CAUSE NOT ESTABLISHED remains a historical verification limitation; accepted independent review finds it does NOT block ACM-01-F01 technical acceptance.
- P3-H01: CLOSED / PRESERVED; CFG-H01: CLOSED / REMOVED.
- ACM-01: HOLD pending repair closure + ACM-01 rereview; ACM-02: NOT STARTED.
- Phase 3: HOLD / `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`; Phase 4: NOT STARTED / FORBIDDEN.
- Exact next gate: INDEPENDENT FINAL ACM-01-F01 ARCHITECTURE / CLOSURE REVIEW.
- Re-freeze, new publication and audit resumption require later separate authorization. No M00_L17.
- Governing chronology/evidence: [dedicated ACM-01-F01 repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

The exceptional repair chronology/evidence is appended at the end of this guide.
Original transition steps and P3-H01/CFG-H01 appendices remain historical records.
Final exceptional-repair guide acceptance awaits independent final closure review.

<!-- ACM-01-F01 PRE-CLOSURE SNAPSHOT END -->


This preserved first-stage snapshot records registration and the initial failed
verification. Its next-gate and HOLD wording applies only to that earlier stage.
During the pre-closure reconciliation, the dedicated ADR's Section 9 governed.
The current formal-closure summary above and ADR Section 10 govern the recorded
ACM-01-F01 closure decision. DOC-01 independent documentation rereview is the
next gate before ACM-01 domain rereview.

<!-- ACM-01-F01 FIRST-STAGE SNAPSHOT BEGIN -->
## Historical ACM-01-F01 registration / first verification — 2026-09-29

This authorized record controls the current lifecycle. Earlier frozen, closed,
publication-pending and remaining-requirements records below retain their
historical stage meaning. Original and repaired publication evidence is preserved.

- Status: IN_PROGRESS; Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Lifecycle: EXCEPTIONAL REPAIR IN PROGRESS; sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Repository Active Lesson Count: 1; Active M00 Lesson Count: 1; Current Active M00 Lesson: M00_L16.
- ACM-01: HOLD — REPAIR IN PROGRESS; ACM-02: NOT STARTED; Phase 4: NOT STARTED / FORBIDDEN.
- ACM-01-F01 implementation: COMPLETED within the exact 15-Java-identity boundary; verification: HOLD — first gate failed in compileJava before tests ran.
- P3-H01: CLOSED / PRESERVED; CFG-H01: CLOSED / REMOVED; neither repair is reopened.
- Governing record: [ACM-01-F01 package repair](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).
- Next gate: Architect review of the compile failure and protected-state fingerprint discrepancy; no further execution or scope expansion.
- Full documentation reconciliation, closure, re-freeze, new publication and audit resumption remain later gates. No M00_L17.

<!-- ACM-01-F01 FIRST-STAGE SNAPSHOT END -->


## Current CFG-H01 governance registration — 2026-09-29

This dated record controls current repair status and future scope. The
2026-09-28 P3-H01 registration, initial pending gates, original decision,
and earlier lifecycle passages below are preserved historical stage records.
Their former NOT STARTED/PENDING statements do not describe today's P3-H01
implementation or supplied fresh verification. Standing governance rules and
unresolved separate closure gates remain in force.

- Status: IN_PROGRESS.
- Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Repository Active Lesson Count: 1; Active M00 Lesson Count: 1.
- Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Stage 1 — P3-H01: IMPLEMENTED / FRESHLY VERIFIED / NOT REOPENED.
- Stage 2 — CFG-H01: GOVERNANCE REGISTERED / DESIGN APPROVED.
- CFG-H01 Implementation Authorization: PENDING / NOT AUTHORIZED.
- CFG-H01 Implementation: NOT STARTED.
- Accepted CFG-H01 design: `PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.
- Architect/User approval for this turn: the nine existing documentation
  files only; no production or test implementation permission.
- Governing amendment: [existing exceptional-repair ADR](../../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md), dated CFG-H01 supplement.

### Preserved P3-H01 fresh evidence

The current User-supplied evidence establishes implementation complete,
focused test PASS, full suite PASS, clean build PASS, and fresh Simulation PASS.
Vision qualification/fusion, event-path runtime, LEARNING_EVENT Intake/Feeder
semantic behavior, cleanup, consumed-readiness fail-closed, and normal
Simulation exit are PASS within the supplied observation scope.
No test count, exit code, hardware result, or new publication identity is inferred.

Localization initialization precedes expected accepted Vision fusion.
Successful LEARNING_EVENT runtime evidence is
`/Intake/RequestedState` and `/Feeder/RequestedState`; successful
AutonomousEvent lifecycle telemetry is not the evidence.
Preserve these procedure corrections and P3-H01 runtime PASS without
relabeling them as post-CFG-H01 execution.

### CFG-H01 registered future boundary

CFG-H01 is ONE inherited MISPLACED_CONFIGURATION finding: the real adapter's
private `kLimelightTableName = "limelight"` owns robot/device table identity.
The earliest surviving occurrence is V00_L08, propagated through V00_L09 and
M00_L01-M00_L16 (18 lessons, one finding). No endpoint error, runtime failure,
or hardware safety failure is established. Historical lessons remain untouched.

All following paths are relative to existing M00_L16:

| Future file | Exact bounded change; separately authorized later |
| --- | --- |
| `src/main/java/frc/robot/Constants.java` | Add `public static final String kLimelightTableName = "limelight";` first in existing VisionConstants, before calibration fields; narrow class JavaDoc if needed; preserve every current declaration/value. |
| `src/main/java/frc/robot/io/vision/VisionIOLimelight.java` | Import Constants, remove the private endpoint default, and make the public default constructor consume `Constants.VisionConstants.kLimelightTableName`; preserve the injected NetworkTable constructor and all protocol/parsing/session behavior. |
| `src/test/java/frc/robot/VisionConfigurationAuthorityTest.java` | Extend this existing test only: declaration shape, independent value, executable source origin/removal, and actual default `/limelight/json` endpoint proof. |

Exactly two existing production files and one existing test file form the
future CFG-H01 boundary; zero new production/test files. RobotContainer and
VisionIOLimelightTest remain protected. P3-H01's original authorization did
not cover VisionIOLimelight or this subsequent existing-test extension.

### Approved CFG-H01 verification applicability

| Gate | Disposition / current result |
| --- | --- |
| Focused VisionConfigurationAuthorityTest | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Unchanged VisionIOLimelight tests | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Relevant inherited Vision regressions | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Full test suite and clean build | REQUIRED / PENDING / NOT TESTED after CFG-H01 |
| Independent exact-delta/static review | REQUIRED / PENDING |
| Fresh Simulation | NOT REQUIRED: Simulation selects VisionIOSim and does not exercise real adapter endpoint ownership. |
| Glass | NOT APPLICABLE |
| Driver Station | NOT APPLICABLE |
| Real hardware | NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE: endpoint remains exactly `"limelight"`; only configuration authority changes. |

These are approved CFG-H01 dispositions only. They do not erase P3-H01 evidence
or silently resolve separate earlier P3-H01 applicability/closure requirements.

### Lifecycle, publication and remaining gates

The existing AGENTS Sections 8/14 exception is amended for CFG-H01;
P3-H01 is not reopened and no new curriculum concept is added.
Separate implementation authorization, future verification, documentation
finalization, final architecture/independent closure review, explicit
Architect/User re-freeze, independent freeze review, User-owned repaired
primary/metadata commits and push, and external publication verification
remain PENDING. The transition repair appendix is IN PROGRESS / NOT FINAL.

Original publication remains historical fact:
primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`,
metadata `3667290180fe1a9fd96265383e7412c142c18129`,
gate `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION`.
No old PASS becomes fresh CFG-H01 evidence; no repaired publication is claimed.

Phase 2 remains
`PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
Phase 4: NOT STARTED / FORBIDDEN. Audit resumption requires its separate
reviewed-removal/authorization gate. No M00_L17.
STOP/HOLD on any CFG-H01 third production file, second test file, new
production/test file, changed endpoint/threshold/behavior, architecture
expansion, other lesson edit, or unauthorized drift.

## Current P3-H01 exceptional-repair governance registration — 2026-09-28

- Status: IN_PROGRESS.
- Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Reopen reason: P3-H01 post-freeze architecture/configuration-authority defect.
- Repository Active Lesson Count: 1.
- Active M00 Lesson Count: 1.
- Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Architect and User authorization: APPROVED for governance/documentation and
  exceptional bounded repair workflow only, by the supplied registration request.
- Accepted design: `PASS_P3_H01_M00_L16_EXCEPTIONAL_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.
- Implementation Authorization: PENDING / NOT AUTHORIZED.
- P3-H01 Implementation: NOT STARTED.
- Fresh repair baseline, static review, focused tests, full inherited suite,
  clean build, Simulation, Driver Station, closure, re-freeze, and repaired
  publication: PENDING; no fresh execution PASS is claimed.
- Glass and real-hardware applicability decisions: PENDING.
- Governing repair ADR: [P3-H01 configuration-authority repair](../../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md).

This is the narrow exception under AGENTS Sections 8 and 14. REOPENED is a
provenance qualifier for IN_PROGRESS, not a new generic status. Editability
does not authorize Java/test changes. All other lessons remain read-only.
M00's original sixteen-lesson curriculum is closed; this repair adds no concept
or lesson and does not authorize M00_L17.

### Original published snapshot — preserved historical evidence

- Original lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.
- Original primary snapshot: `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- Original publication metadata: `3667290180fe1a9fd96265383e7412c142c18129`.
- Original final gate: `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION`.
- Original evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

These identities and the accepted external publication verification are historical
truth. Earlier pending-publication passages below describe the pre-Commit-2
record; they do not revoke the accepted original publication. Original steps,
closure HOLD/repair/rereview, freeze evidence, and publication history remain
preserved. No original commit is rewritten and no old PASS becomes fresh repair
evidence. The repair working state has no repaired publication identity.

### Registered future boundary and audit state

The future production boundary is exactly L16 `src/main/java/frc/robot/Constants.java`
and `src/main/java/frc/robot/RobotContainer.java`. Only one new test is proposed:
`src/test/java/frc/robot/VisionConfigurationAuthorityTest.java`; zero existing
test edits. No Java/test implementation is authorized by this registration.

Constants.VisionConstants will own primitive double defaults:
`kLowUncertaintyMaxDistanceMeters = 1.0`,
`kMediumUncertaintyMaxDistanceMeters = 2.0`,
`kMaximumAcceptedDistanceMeters = 3.0`, and
`kMaximumFreshAgeSeconds = 0.250`. RobotContainer will continue constructing
and injecting the existing Policy. Inclusive comparisons and runtime semantics
remain unchanged. VisionSubsystem and both evaluator production files remain
protected. V00_L09 and M00_L01-L15 retain their historical P3-H01 finding and
unchanged files. Estimator/fusion, telemetry, PathPlanner/events, mechanisms,
controllers, CAN/hardware configuration, dependencies, and assets are excluded.

Phase 2 remains `PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
Phase 4 is NOT STARTED / FORBIDDEN. Governance registration cannot resume the
audit: a reviewed implementation/static-removal gate and separate authorization
are required. D2A, H01, historical R1, A01_L07, and historical-byte limits remain
qualified as previously accepted.

STOP/HOLD if a third production file, an existing test edit, numerical behavior
change, architecture expansion, or another lesson is required. Return for
governance review; do not silently expand scope. Required gates remain pending
until fresh evidence is accepted.

## Historical original transition record — through 2026-09-27

The original introductory fields and Steps 1-27 below are preserved verbatim as
the historical transition/pre-Commit-2 record. The accepted original external
publication identities are recorded above. The separately dated appendix at
the end plans the repair workflow; it records no repair implementation result.

Status: FINAL / PASS THROUGH PUBLICATION METADATA RECONCILIATION / READY FOR USER METADATA COMMIT 2  
Current lesson: M00_L16 — Mechanism Autonomous Event Integration  
Current lifecycle: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED  
Previous lesson: M00_L15 — COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED  
Active M00 lesson count: 0  
Current active M00 lesson: NONE  
Real hardware: DEFERRED

This guide records accepted evidence separately from planned gates. The
one new concept is scheduler-managed dispatch of the existing
`IntakeToFeederCommand` through the inherited `LEARNING_EVENT` named-event
boundary. Implementation, focused tests, clean regression, bounded
Simulation, documentation reconciliation, independent closure review, and
freeze reconciliation are complete under accepted evidence. Each numbered
step records one change or gate and its reviewable result. Independent Freeze
Review passed, and the User created Primary Frozen Snapshot Commit 1 at
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata
reconciliation is complete. Metadata Commit 2, publication push, and final
external publication verification remain pending; M00_L16 is NOT PUBLISHED.
Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

## Step 1 — Verify M00_L15 final publication

- **Objective:** Establish the frozen predecessor.
- **Why:** L16 must inherit only the final published M00_L15 snapshot.
- **Action:** Accept User-owned primary snapshot `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`, metadata publication `0d3685ce67a0b985459392621e003611eaa6dc35`, and `PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`.
- **Files Changed:** None by this L16 activation; M00_L15 remains frozen.
- **Verification:** Accepted predecessor publication evidence; COMPLETE.
- **Expected Result:** M00_L15 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.

## Step 2 — Copy frozen M00_L15 into M00_L16

- **Objective:** Prepare the independent L16 WPILib project.
- **Why:** The inheritance workflow forbids recreating the lesson from scratch.
- **Action:** User-prepared copy and rename of frozen M00_L15 to `M00_L16_MechanismAutonomousEventIntegration`.
- **Files Changed:** New M00_L16 lesson copy; no predecessor file modified.
- **Verification:** Accepted untouched-copy inventory; 345/345 authored files later confirmed identical.
- **Expected Result:** Prepared L16 candidate with inherited architecture and no L16 implementation.

## Step 3 — Remove copied generated artifacts

- **Objective:** Keep generated build state out of the authored baseline.
- **Why:** The standard lesson workflow requires cleanup after copying.
- **Action:** User removed copied `build/` and `.gradle/` artifacts before baseline verification.
- **Files Changed:** Generated artifacts in the new candidate only; no authored source, test, or deploy file changed.
- **Verification:** Accepted preparation and untouched-copy audit evidence; COMPLETE.
- **Expected Result:** Clean candidate ready for its own baseline build.

## Step 4 — Run untouched-copy baseline build

- **Objective:** Verify the inherited project before L16 changes.
- **Why:** The baseline separates inherited behavior from future implementation.
- **Action:** User ran the accepted baseline workflow.
- **Files Changed:** None in authored lesson files.
- **Verification:** `PASS_M00_L16_UNTOUCHED_COPY_BASELINE_BUILD`; User-reported BUILD SUCCESSFUL in 1m 1s; BUILD_EXIT_CODE=0.
- **Expected Result:** Untouched inherited candidate builds successfully.

## Step 5 — Complete architecture and inheritance audit

- **Objective:** Confirm frozen provenance and the event integration boundary.
- **Why:** The new concept must preserve the Frozen Backbone and M00 roadmap.
- **Action:** Independently audit governance, copied files, relevant source, tests, and event path.
- **Files Changed:** None.
- **Verification:** `PASS_M00_L16_ARCHITECTURE_INHERITANCE_AUDIT`; 345/345 authored, 113/113 production Java, and 106/106 test Java files identical.
- **Expected Result:** L16 remains an untouched, architecture-compatible copy.

## Step 6 — Approve the Final Design Lock

- **Objective:** Select exactly one command and event integration design.
- **Why:** Implementation needs a bounded concept, file budget, and safety contract.
- **Action:** Lock `IntakeToFeederCommand` on existing `LEARNING_EVENT`; plan a fresh child per dispatch with exact IntakeSubsystem and FeederSubsystem requirements through inherited `Supplier<Command>` and `Commands.defer(...)`. Exclude `ShootCommand`, new event IDs, paths, timing policy, and helper changes.
- **Files Changed:** None by the design review.
- **Verification:** `PASS_M00_L16_FINAL_DESIGN_LOCK`; `M00_L16_FINAL_DESIGN_LOCK_PASS_READY_FOR_CONTROLLED_ACTIVATION`.
- **Expected Result:** Future production delta limited to `RobotContainer.java`; no implementation authorized yet.

Normal hold-event cleanup was verified through `FollowPathCommand.end(...)`
→ `EventScheduler.end()` → active event `end(true)` → wrapped registered
event → `DeferredCommand.end(true)` → `IntakeToFeederCommand.end(...)`
→ `feeder.stop()` → `intake.stop()`. This covers normal path completion
and interruption. It does not guarantee cleanup after an arbitrary
uncaught library exception. The inherited supplier-`RuntimeException`
boundary publishes `FACTORY_FAILURE` and returns a safe no-op without
failed-child actuation.

## Step 7 — Perform controlled activation (historical gate)

- **Objective:** Make M00_L16 the sole active editable M00 lesson.
- **Why:** L16 must enter the normal lifecycle before separately authorized implementation.
- **Action:** Reconcile current lifecycle and locked design in AGENTS.md, root README.md, the M00 ADR, four L16 lesson-local documents, and this guide. Preserve M00_L15 and mark later gates pending.
- **Files Changed:** `AGENTS.md`; `README.md`; `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`; L16 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide.
- **Verification:** `PASS_M00_L16_CONTROLLED_ACTIVATION`; documentation self-review and governance mirror validation; production, tests, deploy, vendordeps, and Gradle/config untouched at this historical gate.
- **Expected Result:** M00_L16 IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK; Independent Activation Review PENDING; Implementation Authorization PENDING.

## Step 8 — Complete independent activation review

- **Objective:** Review the active lesson boundary.
- **Why:** Implementation requires an independent gate.
- **Action:** Reviewer examined activation scope and frozen protection.
- **Files Changed:** None by review.
- **Verification:** `PASS_M00_L16_INDEPENDENT_ACTIVATION_REVIEW`, accepted.
- **Expected Result:** L16 remained the sole editable M00 lesson.

## Step 9 — Obtain implementation authorization

- **Objective:** Release the exact locked implementation scope.
- **Why:** Activation did not itself authorize source changes.
- **Action:** Architect/User separately authorized the production and test boundary.
- **Files Changed:** None by authorization.
- **Verification:** `PASS_M00_L16_IMPLEMENTATION_AUTHORIZATION`, accepted.
- **Expected Result:** Only the bounded L16 delta was released.

## Step 10 — Bind the existing event in RobotContainer

- **Objective:** Dispatch the existing mechanism command through LEARNING_EVENT.
- **Why:** This is L16's one new concept.
- **Action:** Replace the demonstration supplier with a fresh IntakeToFeederCommand(intakeSubsystem, feederSubsystem) per dispatch and exact Intake + Feeder requirements.
- **Files Changed:** L16 src/main/java/frc/robot/RobotContainer.java only.
- **Verification:** `PASS_M00_L16_IMPLEMENTATION`; source comparison and later static rereview PASS.
- **Expected Result:** Existing deferred registration dispatches a fresh child; teleop and chooser remain unchanged.

## Step 11 — Add focused integration tests

- **Objective:** Check the event binding contract.
- **Why:** L16 must verify dispatch, requirements, lifecycle, contention, and failure behavior.
- **Action:** Add exactly eight integration test methods.
- **Files Changed:** Added L16 src/test/java/frc/robot/RobotContainerMechanismAutonomousEventIntegrationTest.java.
- **Verification:** File inspection confirms eight tests; User focused run later passed 8/8.
- **Expected Result:** The new concept has direct scheduler and factory-failure coverage.

## Step 12 — Update the inherited architecture guard

- **Objective:** Align the copied guard with the approved event reference.
- **Why:** The L15 prohibition on any RobotContainer reference is stale for L16.
- **Action:** Update the event-reference expectation while preserving teleop, default-command, and direct-access protections.
- **Files Changed:** L16 src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java only.
- **Verification:** Initial implementation review found a remaining guard issue; Step 14 records its bounded repair.
- **Expected Result:** The guard allows only the approved integration.

## Step 13 — Record initial independent static review

- **Objective:** Audit the implementation before executable verification.
- **Why:** The exact file boundary and architecture protections require review.
- **Action:** Reviewer returned HOLD for the architecture guard.
- **Files Changed:** None by review.
- **Verification:** Initial independent static review HOLD, preserved as historical finding.
- **Expected Result:** Only the identified guard issue proceeds to bounded repair.

## Step 14 — Repair the architecture guard

- **Objective:** Clear the bounded static finding.
- **Why:** The test must enforce the approved event boundary without weakening inherited protections.
- **Action:** Repair only the guard identified by the initial review.
- **Files Changed:** L16 src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java only.
- **Verification:** `PASS_M00_L16_STATIC_REVIEW_REPAIR`; no production change.
- **Expected Result:** Guard is ready for fresh independent review.

## Step 15 — Complete independent static rereview

- **Objective:** Confirm the repaired implementation boundary.
- **Why:** Verification proceeds after static findings are cleared.
- **Action:** Reviewer reinspected the source and repaired guard.
- **Files Changed:** None by review.
- **Verification:** Fresh independent static rereview PASS, accepted.
- **Expected Result:** L16 proceeds to User execution gates.

## Step 16 — Run focused tests

- **Objective:** Verify the L16 integration behavior.
- **Why:** Scheduler and architecture claims require executable evidence.
- **Action:** User ran the architecture guard focused methods and the eight integration tests.
- **Files Changed:** None in authored files by the run.
- **Verification:** PASS; all focused methods and 8/8 integration tests passed; BUILD SUCCESSFUL; exit code 0.
- **Expected Result:** Exact requirements, fresh child, semantic requests, lifecycle, contention, and safe factory failure pass.

## Step 17 — Run clean regression

- **Objective:** Check inherited contracts after the bounded delta.
- **Why:** Event integration can affect prior autonomous and mechanism behavior.
- **Action:** User ran the clean regression.
- **Files Changed:** None in authored files by the run.
- **Verification:** PASS; BUILD SUCCESSFUL, all tests shown passed, no regression blocker. Numeric test count and exit code were not supplied.
- **Expected Result:** Inherited regression remains green within supplied evidence.

## Step 18 — Perform bounded Simulation

- **Objective:** Verify event-path runtime semantics with an event-free control.
- **Why:** Focused tests alone do not show the path marker at runtime.
- **Action:** User ran ONE_METER_WITH_EVENT and ONE_METER_PATH with Driver Station attached.
- **Files Changed:** None in authored files by the run.
- **Verification:** PASS_M00_L16_BOUNDED_SIMULATION: Autonomous RUNNING at LEARNING_EVENT with Intake INTAKE_REQUESTED, Feeder FEED_REQUESTED, Flywheel/Elevator STOPPED; Intake/Feeder STOPPED on Disable/interruption. Control path ran without event and kept both STOPPED. No fatal scheduler/runtime exception observed; normal return to PowerShell prompt. Final Gradle output: five actionable tasks, three executed, two up-to-date. No Simulation exit code supplied.
- **Expected Result:** Semantic dispatch and cleanup verified in Simulation; physical behavior remains deferred.

## Step 19 — Reconcile documentation

- **Objective:** Record the exact accepted evidence and limitations.
- **Why:** The guide and lifecycle records must reflect completed implementation and verification.
- **Action:** Update L16 local records and current repository lifecycle sections; preserve earlier gate text as historical.
- **Files Changed:** AGENTS.md, root README.md, M00 ADR, L16 README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md, and this guide only.
- **Verification:** Documentation self-review; PASS_M00_L16_DOCUMENTATION_RECONCILIATION.
- **Expected Result:** THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, with closure and freeze pending.

Blank AutonomousEvent NT fields do not contradict the Simulation result. The
facade publishes only an existing observation, RobotContainer starts with
Optional.empty, the child emits no success lifecycle observation, and
registration emits only FACTORY_FAILURE. No LastEvent or DispatchCount claim
is made. No physical transfer, motor, sensor, timing, or electrical claim is
made from Noop adapters.

## Step 20 — Independent closure review

- **Objective:** Review implementation, verification, and documentation closure.
- **Why:** Documentation reconciliation does not close the lesson.
- **Action:** The first Independent Closure Review returned HOLD because the README placed completed focused-test and Simulation evidence beside same-level future-looking planning headings. The bounded repair changed only the L16 README, nesting those plan sections beneath Historical lifecycle and labeling them historical and superseded. A fresh independent closure rereview then passed.
- **Files Changed:** None by review.
- **Verification:** Initial `HOLD_M00_L16_INDEPENDENT_CLOSURE_REVIEW`; bounded `PASS_M00_L16_CLOSURE_DOCUMENTATION_REPAIR`; fresh `PASS_M00_L16_INDEPENDENT_CLOSURE_REVIEW` with final verdict `M00_L16_INDEPENDENT_CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION` and no remaining findings.
- **Expected Result:** Closure evidence and documentation are accepted for freeze reconciliation.

## Step 21 — Reconcile freeze

- **Objective:** Freeze the lesson after accepted closure.
- **Why:** COMPLETE lessons become read-only snapshots.
- **Action:** Reconcile current lifecycle records to the accepted closure state; set M00_L16 to COMPLETE / FROZEN / READ-ONLY, active lesson count 0, preserve THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, and leave Independent Freeze Review and publication pending. Preserve the closure HOLD, bounded repair, and fresh rereview chronology.
- **Files Changed:** `AGENTS.md`; repository `README.md`; M00 roadmap ADR; L16 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide. No production, test, deploy, build, or configuration file changed.
- **Verification:** `PASS_M00_L16_FREEZE_RECONCILIATION`; documentation self-review; governance mirror validation PASS (12 VERIFIED mirrors, 12 matching PDF hashes, 12 matching Markdown hashes, zero deterministic findings).
- **Expected Result:** M00_L16 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED and ready for Independent Freeze Review.

## Step 22 — Independent freeze review

- **Objective:** Verify the proposed frozen snapshot.
- **Why:** Publication follows reviewed freeze.
- **Action:** Independent reviewer examined the reconciled frozen snapshot.
- **Files Changed:** None by review.
- **Verification:** `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW`; verdict `M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
- **Expected Result:** Freeze review PASS; M00_L16 remains COMPLETE / FROZEN / READ-ONLY.

## Step 23 — Record User-owned primary frozen snapshot commit

- **Objective:** Record Commit 1 of the two-commit Historical Snapshot model.
- **Why:** The frozen lesson snapshot precedes the separate metadata commit.
- **Action:** Accept the User-provided canonical primary snapshot hash `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- **Files Changed:** None in this metadata reconciliation step.
- **Verification:** User-created Primary Frozen Snapshot Commit 1; canonical SHA `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- **Expected Result:** Local primary snapshot recorded without claiming metadata commit, push, or remote publication.

## Step 24 — Reconcile publication metadata

- **Objective:** Prepare the accurate current record for User-owned Metadata Publication Commit 2.
- **Why:** The successful freeze review and primary snapshot require publication metadata before Commit 2.
- **Action:** Reconcile only the current lifecycle and publication fields, preserving historical HOLD, repair, rereview, technical evidence, and frozen source.
- **Files Changed:** `AGENTS.md`; repository `README.md`; M00 roadmap ADR; L16 `README.md`, `LESSON_STATUS.md`, `LESSON_PLAN.md`, `LESSON_CHECKLIST.md`, and this guide. No source, test, deploy, build, or configuration file changed.
- **Verification:** `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`; governance validation PASS with 12 VERIFIED mirrors, 12 matching PDF hashes, 12 matching Markdown hashes, and zero deterministic findings.
- **Expected Result:** Metadata is prepared for Commit 2; M00_L16 remains COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED.

## Step 25 — User-owned metadata commit pending

- **Objective:** Preserve Metadata Publication Commit 2 as a separate User-owned gate.
- **Why:** Commit 2 follows the completed primary snapshot and metadata reconciliation.
- **Action:** PENDING; await the User-owned metadata-only commit.
- **Files Changed:** None by this reconciliation step.
- **Verification:** Metadata Commit 2 and its SHA PENDING.
- **Expected Result:** The two-commit Historical Snapshot model remains intact; no third verification-only commit.

## Step 26 — Publication push pending

- **Objective:** Preserve push as a separate User-owned gate.
- **Why:** Remote publication follows Metadata Commit 2.
- **Action:** PENDING; await the User-owned publication push.
- **Files Changed:** None by this reconciliation step.
- **Verification:** Push PENDING; no remote identity claimed.
- **Expected Result:** M00_L16 remains NOT PUBLISHED until accepted publication evidence.

## Step 27 — Final external publication verification pending

- **Objective:** Preserve final publication verification as its own external gate.
- **Why:** Verification follows Commit 2 and push.
- **Action:** PENDING; await external evidence.
- **Files Changed:** None by this reconciliation step.
- **Verification:** Final External Publication Verification PENDING.
- **Expected Result:** No PUBLISHED / VERIFIED claim before accepted external evidence; no third verification-only commit.

## P3-H01 exceptional-repair appendix — 2026-09-28

Status: IN PROGRESS / NOT FINAL. The following steps describe the future sequence
only. All step statuses, verification and evidence remain PENDING. Governance
registration is recorded at the top; no repair implementation or execution result
is claimed here. Paths in step scopes refer to the existing M00_L16 project.
Original Steps 1-27 above remain unchanged.

### Repair Step R1 — Establish fresh baseline and provenance

- **Objective:** Establish fresh baseline and provenance.
- **Why:** Separate inherited published code from future repair.
- **Action:** User will verify Java 17, original L16 source/provenance, inherited focused/full suite and clean build before implementation.
- **Files Changed / Files and Scope:** Unchanged original L16; baseline evidence only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Fresh User evidence and baseline/scope review required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Verified unchanged baseline; repair still not implemented.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R2 — Obtain exact implementation authorization

- **Objective:** Obtain exact implementation authorization.
- **Why:** Release only the accepted future file boundary.
- **Action:** Architect/User will confirm the design and separately authorize exactly two production files and one new test.
- **Files Changed / Files and Scope:** Constants.java, RobotContainer.java and proposed VisionConfigurationAuthorityTest.java only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Explicit exact-file authorization required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Bounded implementation may start; no wider editability implied.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R3 — Place defaults in Constants

- **Objective:** Place defaults in Constants.
- **Why:** Restore default configuration authority.
- **Action:** Later authorized engineer will add the four locked public static final doubles to VisionConstants and narrowly clarify JavaDoc.
- **Files Changed / Files and Scope:** src/main/java/frc/robot/Constants.java only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Independent source/value and changed-file review required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Configuration ownership restored without changed values.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R4 — Inject named defaults in RobotContainer

- **Objective:** Inject named defaults in RobotContainer.
- **Why:** Preserve valid composition using authoritative defaults.
- **Action:** Later authorized engineer will replace the three Policy arguments and one freshness argument with named constants.
- **Files Changed / Files and Scope:** src/main/java/frc/robot/RobotContainer.java only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Constructor-origin/injected-configuration review required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Same Policy, constructor, timestamp supplier and runtime semantics.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R5 — Add one configuration authority guard

- **Objective:** Add one configuration authority guard.
- **Why:** Prove origin, actual wiring and unchanged boundaries.
- **Action:** Later authorized engineer will add independent default, constructor-reference, actual fixture/injection, inclusive-boundary and evaluator-dependency proof.
- **Files Changed / Files and Scope:** src/test/java/frc/robot/VisionConfigurationAuthorityTest.java only; zero existing test edits. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Focused execution later required; no current result.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** One bounded new guard protects the accepted repair.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R6 — Review static removal and file isolation

- **Objective:** Review static removal and file isolation.
- **Why:** Confirm P3-H01 is removed at the selected target.
- **Action:** Independent reviewer will inspect exact production/test delta and historical preservation.
- **Files Changed / Files and Scope:** Read-only two-file repair/new-test review and protected-source comparisons. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Static-removal and scope PASS required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Correction reviewed; verification/closure/publication not implied.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R7 — Run fresh focused and full verification

- **Objective:** Run fresh focused and full verification.
- **Why:** Protect inherited architecture and behavior.
- **Action:** User will run focused/guard tests, inherited/full suite and clean Gradle build; retain all existing tests.
- **Files Changed / Files and Scope:** User execution in repaired L16 only; no additional authored edits. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. New results required; original PASS cannot satisfy repair gates.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Fresh focused/full/build evidence accepted.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R8 — Verify bounded Simulation and Driver Station

- **Objective:** Verify bounded Simulation and Driver Station.
- **Why:** Confirm integrated runtime composition after repair.
- **Action:** User will verify startup, valid-frame qualification/fusion, unavailable/stale handling, event/event-free controls, Disabled cleanup, no observed fatal exception and normal exit.
- **Files Changed / Files and Scope:** Existing Simulation fixtures and runtime contracts only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Fresh Simulation and Driver Station evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Bounded software/runtime verification; no physical-performance claim.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R9 — Resolve Glass and real-hardware applicability

- **Objective:** Resolve Glass and real-hardware applicability.
- **Why:** Avoid silent waiver or reuse of historical evidence.
- **Action:** Architect/User will explicitly decide applicability and User will provide any required bounded evidence.
- **Files Changed / Files and Scope:** Existing observation/hardware contracts; no calibration, tuning or configuration change. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Explicit decisions and applicable fresh evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Applicability resolved with scoped results or approved disposition.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R10 — Finalize repair documentation and closure

- **Objective:** Finalize repair documentation and closure.
- **Why:** Record supported evidence and preserved provenance.
- **Action:** A separately authorized documentation action will reconcile repair records; independent architecture/closure review will follow.
- **Files Changed / Files and Scope:** This ADR and only later explicitly authorized existing governance/lesson records. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Documentation and final architecture/closure PASS required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Repair appendix final only after applicable gates complete.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R11 — Authorize re-freeze

- **Objective:** Authorize re-freeze.
- **Why:** Make the verified repaired lesson immutable again.
- **Action:** Architect/User will explicitly approve COMPLETE / FROZEN / READ-ONLY; independent freeze review will follow.
- **Files Changed / Files and Scope:** Authorized lifecycle records only; source remains fixed. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Explicit re-freeze and independent freeze-review evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Active count 0; repaired publication still pending.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R12 — Publish a new repaired primary snapshot

- **Objective:** Publish a new repaired primary snapshot.
- **Why:** Establish an identity separate from the original.
- **Action:** User alone will create the new repaired frozen-snapshot commit after accepted re-freeze.
- **Files Changed / Files and Scope:** New repaired snapshot; original ff4de5ab... and 36672901... preserved. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. User-supplied new primary identity required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** New primary snapshot recorded without metadata/push claims.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R13 — Reconcile and publish repair metadata

- **Objective:** Reconcile and publish repair metadata.
- **Why:** Link original and repaired identities without rewriting history.
- **Action:** Separately authorized metadata action will record new-primary provenance; User will create the separate metadata commit and push.
- **Files Changed / Files and Scope:** Only explicitly authorized repair metadata; no original commit amendment. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. User new metadata identity and successful push evidence required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Two-commit repaired publication prepared for external verification.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R14 — Verify repaired publication externally

- **Objective:** Verify repaired publication externally.
- **Why:** Accept the new publication and its bounded supersession role.
- **Action:** Independent external review will check new identities, scope and push evidence.
- **Files Changed / Files and Scope:** Read-only publication evidence; no third verification-only commit. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. External final repaired-publication verification required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Repaired snapshot PUBLISHED / VERIFIED for approved correction role.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.

### Repair Step R15 — Seek separate Phase-3 resumption authorization

- **Objective:** Seek separate Phase-3 resumption authorization.
- **Why:** Keep audit resumption distinct from registration and publication.
- **Action:** After reviewed static removal, seek a separate audit instruction; report actual verification/freeze/publication state.
- **Files Changed / Files and Scope:** Remaining read-only Architecture Consistency Matrix only. Future permitted scope only; no change claimed by this step.
- **Verification:** PENDING. Reviewed static-removal gate and separate authorization required.
- **Evidence:** PENDING; no fresh repair result supplied or accepted.
- **Expected Result:** Phase 3 may resume only when separately authorized; Phase 4 stays forbidden.
- **Rollback/HOLD Condition:** STOP and return for governance review on failed/missing required evidence, third production file, existing test edit, numerical change, architecture expansion, another lesson, or unauthorized drift. No automatic rollback or scope expansion.
- **Status:** PENDING.


## CFG-H01 exceptional-repair appendix — 2026-09-29

Status: IN PROGRESS / NOT FINAL. This appendix follows all pre-existing material.
Original Steps 1-27 and the complete 2026-09-28 P3-H01 appendix remain byte-for-byte
preserved as historical records. Their initially pending P3-H01 gates are
superseded for current status by this dated record and the current registration
above; P3-H01 is IMPLEMENTED / FRESHLY VERIFIED / NOT REOPENED.

The User-supplied P3-H01 focused/full/clean-build/fresh-Simulation PASS and bounded
runtime results remain preserved. Localization initialization precedes expected
accepted Vision fusion. Successful LEARNING_EVENT evidence is
`/Intake/RequestedState` and `/Feeder/RequestedState`, not successful
AutonomousEvent lifecycle telemetry. No post-CFG-H01 execution is claimed.

All future code paths refer only to the existing M00_L16 project.
The current implementation gate remains PENDING / NOT AUTHORIZED.

### CFG-H01 Step C1 — Record the inherited configuration-authority finding

- **Objective:** Record the inherited configuration-authority finding.
- **Why:** Identify device configuration ownership without inventing failure.
- **Action:** Record ONE CFG-H01 MISPLACED_CONFIGURATION finding, earliest surviving V00_L08 and propagation through V00_L09/M00_L01-M00_L16.
- **Files Changed / Files and Scope:** None; read-only audit evidence.
- **Verification:** Established audit/source evidence; no endpoint/runtime/hardware safety failure.
- **Expected Result:** One inherited finding; historical source preserved.
- **Status:** COMPLETE.

### CFG-H01 Step C2 — Register the reviewed bounded design

- **Objective:** Register the reviewed bounded design.
- **Why:** Keep correction within the independently reviewed ownership boundary.
- **Action:** Preserve the accepted design token and exact two-existing-production/one-existing-test future scope.
- **Files Changed / Files and Scope:** None by design review; design registration recorded in C3.
- **Verification:** PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION; reviewed design.
- **Expected Result:** Design approved; implementation still unauthorized.
- **Status:** COMPLETE.

### CFG-H01 Step C3 — Register governance and stage status

- **Objective:** Register governance and stage status.
- **Why:** Extend the existing exception explicitly while preserving Stage 1 evidence.
- **Action:** Add the dated CFG-H01 record to the exact nine existing documents listed in ADR Section 16.7; preserve all original bytes and append this appendix after existing material.
- **Files Changed / Files and Scope:** Exactly the nine existing governance/lesson documents; zero source/test edits; zero new ADRs.
- **Verification:** Current governance integrity and documentation/scope checks; independent registration review remains pending.
- **Expected Result:** GOVERNANCE REGISTERED / DESIGN APPROVED / IMPLEMENTATION AUTHORIZATION PENDING.
- **Status:** COMPLETE.

### CFG-H01 Step C4 — Obtain separate implementation authorization

- **Objective:** Obtain separate implementation authorization.
- **Why:** Documentation registration does not authorize source changes.
- **Action:** After independent registration review, obtain exact-file Architect/User approval for Constants.java, io/vision/VisionIOLimelight.java and existing VisionConfigurationAuthorityTest.java.
- **Files Changed / Files and Scope:** None by this gate; future implementation scope only.
- **Verification:** PENDING; explicit exact-file authorization required.
- **Expected Result:** Bounded source/test work may begin only after approval.
- **Status:** PENDING.

### CFG-H01 Step C5 — Relocate endpoint configuration authority

- **Objective:** Relocate endpoint configuration authority.
- **Why:** Restore Constants ownership with the same endpoint behavior.
- **Action:** Later authorized engineer adds the String field first in VisionConstants, imports/consumes it in the default adapter constructor and removes the private endpoint default; extends only the existing guard as designed.
- **Files Changed / Files and Scope:** Future only: the exact two existing production files and one existing test; zero new files.
- **Verification:** PENDING; no implementation performed by this registration.
- **Expected Result:** Endpoint remains limelight; constructors/protocol/session and completed P3-H01 guards preserved.
- **Status:** PENDING.

### CFG-H01 Step C6 — Review exact delta and static removal

- **Objective:** Review exact delta and static removal.
- **Why:** Prove source origin and preserve protected boundaries.
- **Action:** Independent reviewer checks executable constant consumption/removal, independent declaration/endpoint assertions and unchanged RobotContainer, IO tests and historical files.
- **Files Changed / Files and Scope:** None by read-only review.
- **Verification:** PENDING; independent exact-delta/static review required.
- **Expected Result:** Bounded CFG-H01 source correction accepted without claiming runtime or closure.
- **Status:** PENDING.

### CFG-H01 Step C7 — Obtain fresh automated verification

- **Objective:** Obtain fresh automated verification.
- **Why:** Protect real adapter behavior and inherited project contracts.
- **Action:** User runs focused authority guard, unchanged Limelight tests, relevant inherited Vision regressions, full suite and clean build.
- **Files Changed / Files and Scope:** No authored edits; User-owned verification only.
- **Verification:** PENDING; all listed post-CFG-H01 gates required; no old PASS reused.
- **Expected Result:** Fresh automated evidence accepted. Simulation NOT REQUIRED; Glass/DS NOT APPLICABLE; hardware NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE under approved scope.
- **Status:** PENDING.

### CFG-H01 Step C8 — Complete documentation and independent closure review

- **Objective:** Complete documentation and independent closure review.
- **Why:** Close only with supported evidence and resolved applicable gates.
- **Action:** Obtain separately scoped documentation authorization, finalize repair appendices, and complete final architecture/independent closure review; explicitly resolve any separate earlier outstanding applicability gate.
- **Files Changed / Files and Scope:** Only later explicitly authorized existing documentation; no current finalization claim.
- **Verification:** PENDING; required evidence, final documentation and independent closure PASS.
- **Expected Result:** Repair ready to seek explicit re-freeze; still IN_PROGRESS until approved.
- **Status:** PENDING.

### CFG-H01 Step C9 — Obtain explicit re-freeze and freeze review

- **Objective:** Obtain explicit re-freeze and freeze review.
- **Why:** Return the repaired lesson to immutable status only by approval.
- **Action:** Architect/User explicitly authorizes re-freeze; independent freeze review confirms supported repaired state.
- **Files Changed / Files and Scope:** Later explicitly authorized lifecycle records only.
- **Verification:** PENDING; explicit approval and independent freeze review required.
- **Expected Result:** COMPLETE / FROZEN / READ-ONLY and active count 0 only after approval; repaired publication pending.
- **Status:** PENDING.

### CFG-H01 Step C10 — Publish and externally verify repaired identities

- **Objective:** Publish and externally verify repaired identities.
- **Why:** Preserve original publication and establish distinct repaired provenance.
- **Action:** User creates new repaired primary and separate metadata commits, pushes, and obtains independent external publication verification under separately authorized workflow.
- **Files Changed / Files and Scope:** Later scoped repaired snapshot/metadata only; no original history rewrite.
- **Verification:** PENDING; new identities, push and external final verification required.
- **Expected Result:** Repaired PUBLISHED / VERIFIED only after accepted evidence; no third verification-only commit; Phase-3 resumption still separately authorized and Phase 4 forbidden.
- **Status:** PENDING.

STOP/HOLD on unauthorized scope, altered endpoint/behavior, failed required
verification, or missing applicable closure evidence. No automatic rollback.
Original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11` and metadata
`3667290180fe1a9fd96265383e7412c142c18129` remain historical truth.
No M00_L17, audit resumption, repaired publication, source edit or User execution
is authorized by this documentation registration.

## P3-H01 + CFG-H01 post-implementation evidence reconciliation — 2026-09-29

Status: DOCUMENTATION / EVIDENCE RECONCILED / READY FOR FINAL CLOSURE REVIEW.
M00_L16 remains REOPENED / IN_PROGRESS / EDITABLE; active lesson count 1.
This appended record controls current repair stage status. All pre-existing
bytes, including original Steps 1-27, the P3-H01 repair appendix and CFG-H01
C1-C10 registration/planning appendix, remain preserved. Their then-pending
implementation/verification entries are historical planning, not current status.
Final architecture/closure review remains NEXT / PENDING.

### Accepted repair chronology

| Sequence | Stage / accepted current record |
| --- | --- |
| 1 | P3-H01 discovered: operational Vision defaults were misplaced in RobotContainer. |
| 2 | P3-H01 governed: exceptional reopen/design and exact implementation boundary registered. |
| 3 | P3-H01 implemented: four defaults moved to Constants and injected by RobotContainer; bounded authority guard added. |
| 4 | P3-H01 freshly verified and independently reviewed: supplied focused/full/clean-build and bounded Simulation PASS preserved. |
| 5 | Configuration-authority audit discovered CFG-H01: real-camera table identity privately owned by the adapter. |
| 6 | CFG-H01 bounded design reviewed: PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION. |
| 7 | CFG-H01 amendment registered in the nine existing documents; separate exact-file implementation authorization subsequently approved. |
| 8 | CFG-H01 implemented: exactly two existing production files and one existing test; endpoint/behavior preserved. |
| 9 | User automated verification PASS: authority guard, unchanged adapter tests, Vision regressions, full suite and clean build. |
| 10 | Independent post-implementation review PASS: PASS_CFG_H01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION. |
| 11 | Authorized documentation/evidence reconciliation completed on 2026-09-29. |
| 12 | INDEPENDENT FINAL ARCHITECTURE / CLOSURE REVIEW remains NEXT / PENDING; no closure, re-freeze, publication or Phase-3 resumption. |

### Evidence Step E1 — Preserve the completed P3-H01 correction

- **Objective:** Record completed P3-H01 ownership and fresh verification.
- **Why:** Preserve Stage 1 evidence without reopening or repeating it.
- **Action:** Record Constants defaults 1.0/2.0/3.0/0.250 and RobotContainer injection.
- **Files Changed:** Prior P3-H01 stage: Constants.java, RobotContainer.java and then-new VisionConfigurationAuthorityTest.java; zero source/test edits in this reconciliation.
- **Verification:** Accepted User focused test, full suite, clean build and fresh bounded Simulation PASS; independent review preserved.
- **Evidence:** Clean startup, Disabled baseline, gyro health, autonomous preparation, heading reference and Pose / EstimatedPose initialization; UNAVAILABLE baseline; VALID_FRAME_A qualification/accepted fusion; unchanged-frame duplicate/stale hold; VALID_FRAME_B fresh recovery; return UNAVAILABLE; ONE_METER_WITH_EVENT preparation/event-enabled autonomous execution; LEARNING_EVENT Intake/Feeder semantics; cleanup; autonomous completion; consumed-readiness fail-closed; normal exit.
- **Expected Result:** P3-H01 IMPLEMENTED / VERIFIED / PRESERVED / NOT REOPENED.
- **Status:** RECORDED / PASS EVIDENCE PRESERVED.

Localization/heading initialization precedes expected accepted Vision estimator
fusion. Successful LEARNING_EVENT behavior is observed through
`/Intake/RequestedState` and `/Feeder/RequestedState`, not successful
AutonomousEvent lifecycle telemetry. No physical transfer/performance or
post-CFG-H01 Simulation result is inferred.

### Evidence Step E2 — Record bounded CFG-H01 implementation

- **Objective:** Record the completed configuration-authority relocation.
- **Why:** Remove the inherited misplaced table identity without changing behavior.
- **Action:** Record `public static final String kLimelightTableName = "limelight";` in existing Constants.VisionConstants, existing default adapter consumption, private default removal and bounded existing authority-test extension.
- **Files Changed:** Prior CFG-H01 implementation only: Constants.java, io/vision/VisionIOLimelight.java and existing VisionConfigurationAuthorityTest.java; zero new source/test files.
- **Verification:** Independent declaration/value, executable AST origin/removal and actual default `/limelight/json` endpoint proof; prior P3-H01 tests/helpers preserved.
- **Expected Result:** Same limelight endpoint, both constructors, adapter behavior/protocol and protected source.
- **Status:** IMPLEMENTED / VERIFIED / REMOVED.

### Evidence Step E3 — Record fresh User automated verification

- **Objective:** Reconcile all five required post-CFG-H01 automated gates.
- **Why:** Use accepted fresh execution evidence for this repair stage.
- **Action:** Record User PASS for VisionConfigurationAuthorityTest, unchanged VisionIOLimelightTest, Vision regression suite, full test suite and clean build.
- **Files Changed:** None by verification; this turn only records supplied evidence.
- **Verification:** User supplied the authorized fail-stop block completion and the final output below.
- **Expected Result:** USER AUTOMATED VERIFICATION PASS, with no invented numeric test count or exit code.
- **Status:** PASS / USER EVIDENCE ACCEPTED.

```text
BUILD SUCCESSFUL in 32s
7 actionable tasks: 7 executed
CFG-H01 AUTOMATED USER VERIFICATION COMPLETE
```

Seven actionable tasks are not seven tests. Fresh CFG-H01 Simulation:
NOT REQUIRED (VisionIOSim selection). Glass / Driver Station: NOT APPLICABLE.
Real hardware: NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE (same endpoint behavior).
These approved CFG-H01 decisions remain separate from P3-H01 Simulation evidence
and any separate earlier applicability/closure requirement.

### Evidence Step E4 — Record independent exact-delta review

- **Objective:** Record independent acceptance of removal and scope.
- **Why:** Confirm implementation origin, constructor/behavior preservation and isolation.
- **Action:** Record the accepted Sol post-implementation review.
- **Files Changed:** None by read-only review.
- **Verification:** `PASS_CFG_H01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`; no implementation defect or scope violation found.
- **Expected Result:** REMOVED / VERIFIED / READY_FOR_DOCUMENTATION_RECONCILIATION.
- **Status:** PASS / INDEPENDENT REVIEW ACCEPTED.

### Evidence Step E5 — Reconcile authorized documentation

- **Objective:** Make current lifecycle/evidence records agree with completed repairs.
- **Why:** Remove stale current-state pending claims while preserving chronology.
- **Action:** Replace the current registration-stage summaries and status/plan/checklist; append this evidence record and ADR Section 17.
- **Files Changed:** Only existing AGENTS.md, root README.md, existing P3-H01 repair ADR, M00 roadmap ADR, L16 README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md and this transition guide.
- **Verification:** Governance integrity and bounded documentation/static preservation checks; zero Java/test changes, new ADRs, new lessons, Git or project execution.
- **Expected Result:** DOCUMENTATION / EVIDENCE RECONCILED; original transition/repair appendix bytes preserved.
- **Status:** RECONCILED / READY FOR FINAL CLOSURE REVIEW.

### Evidence Step E6 — Await independent final architecture/closure review

- **Objective:** Submit reconciled evidence to the remaining closure gate.
- **Why:** Documentation reconciliation does not grant closure or re-freeze.
- **Action:** Next independent final architecture/closure review must assess the reconciled repair and explicitly dispose of any separate outstanding earlier applicability/closure gate.
- **Files Changed:** None by this pending gate; no review executed or PASS claimed here.
- **Verification:** PENDING; final architecture/closure acceptance required.
- **Expected Result:** Seek explicit Architect/User re-freeze only after accepted closure; freeze review and User-owned repaired publication remain later gates.
- **Status:** NEXT / PENDING.

Original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
`3667290180fe1a9fd96265383e7412c142c18129` and
`PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION` remain historical truth.
No repaired publication is claimed. Phase 3 remains HOLD; Phase 4 remains
NOT STARTED / FORBIDDEN. No M00_L17. Unrelated A01_L06 path asset remains untouched.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## Final exceptional-repair closure and authorized re-freeze — 2026-09-29

- Independent final closure review: PASS —
  `PASS_M00_L16_EXCEPTIONAL_REPAIR_CLOSED_READY_FOR_REFREEZE_AUTHORIZATION`.
- Technical, verification, documentation and architecture closure: CLOSED.
- P3-H01: IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED / PRESERVED / CLOSED.
- CFG-H01: IMPLEMENTED / VERIFIED / REMOVED / INDEPENDENTLY REVIEWED / CLOSED.
- Remaining repair requirements: NONE.
- Explicit Architect/User re-freeze authorization: APPROVED / CONSUMED by this
  documentation transition.
- M00_L16: COMPLETE / FROZEN / READ-ONLY; exceptional repair CLOSED.
- Repository Active Lesson Count: 0; Active M00 Lesson Count: 0;
  Current Active M00 Lesson: NONE.
- Final repair Transition Guide: accepted through the independent closure review.
  All preceding steps and repair appendices are preserved historical chronology;
  their earlier current/pending/editable wording describes the recorded stage.
- Repaired publication: PENDING; no repaired Git snapshot or identity exists.
  Original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
  `3667290180fe1a9fd96265383e7412c142c18129` and original gate
  `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION` remain historical truth.
- Exact next gate: INDEPENDENT RE-FREEZE / FROZEN-CANDIDATE REVIEW.
- Phase 3: HOLD; separate review, User snapshot/publication workflow, push,
  external final publication verification and separate audit authorization remain
  prerequisites to resumption consideration. Phase 4: NOT STARTED / FORBIDDEN.
- Accepted repair evidence/applicability and deferred physical-evidence limits
  remain preserved; no new hardware/commissioning claim or M00_L17.
- Files changed in this step: the same nine existing governance/lesson documents.
  Verification: governance integrity and static documentation/protected-file checks.
  Expected result: closed, refrozen candidate ready for independent freeze review.
- No Git or project execution. Source/tests, historical lessons and unrelated
  A01_L06 path state remain unchanged. No new files/ADRs.
  github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## Repaired primary snapshot metadata reconciliation — 2026-09-29

- Independent frozen-candidate review: PASS —
  `PASS_M00_L16_FROZEN_CANDIDATE_READY_FOR_USER_PRIMARY_SNAPSHOT`.
- User-created repaired PRIMARY SNAPSHOT / COMMIT 1: CREATED —
  `015b8ca27d466a5a2fce2660a902bb58a4b62003`.
- Explicit Architect/User post-freeze metadata authorization consumed:
  METADATA RECONCILED / READY FOR USER METADATA COMMIT.
- Exact next gate: USER METADATA COMMIT — COMMIT 2.
- Metadata Commit 2: PENDING USER ACTION / DOES NOT EXIST YET.
- User push: PENDING / NOT PERFORMED YET.
- External final repaired-publication verification: PENDING / NOT PERFORMED YET.
- Repaired publication: NOT YET PUBLISHED; no metadata hash is invented.
- M00_L16 remains COMPLETE / FROZEN / READ-ONLY; active counts 0; current active
  M00 lesson NONE; P3-H01 / CFG-H01 / exceptional repair CLOSED.
- Earlier transition steps and closure/re-freeze appendices retain their historical
  stage meaning and remain byte-preserved. Original publication identities remain
  historical and distinct from the repaired primary.
- Canonical sequence: Commit 1 -> metadata reconciliation -> User Commit 2 ->
  User push -> external final verification; no third verification-only commit.
- Phase 3: HOLD pending publication gates and separate audit authorization.
  Phase 4: NOT STARTED / FORBIDDEN. No Git or project execution in this step.

<!-- ACM-01-F01 EVIDENCE APPENDIX BEGIN -->
## ACM-01-F01 exceptional package-repair chronology/evidence — 2026-09-29

This Architect/User-authorized appendix preserves the actual transition history.
The original transition steps and P3-H01/CFG-H01 appendices above are unchanged.
The [dedicated ACM-01-F01 ADR](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md)
contains the complete authority, exact Java boundary, binary-name impact and
verification record. Authoritative English OC-02 Section 1 governs placement;
historical S00_L05 approval did not amend it. Formal design/impact review and
the explicit M00_L16-only exception govern this correction.

### Accepted repair chronology

| Sequence | Stage / evidence |
| --- | --- |
| 1 | ACM-01 architecture-consistency audit discovered ACM-01-F01: inherited mechanism-specific SwerveObservation placement conflicts with OC-02 Section 1. |
| 2 | Architect adjudicated the authority conflict: preserve historical S00_L05 approval and predecessor snapshots; correct current M00_L16 only through a bounded exceptional repair. |
| 3 | Sol bounded design PASS accepted: PASS_ACM_01_F01_REPAIR_DESIGN_READY_FOR_ARCHITECT_IMPLEMENTATION_AUTHORIZATION. |
| 4 | Exceptional repair registered on 2026-09-29 in this dedicated ADR and eight existing records before Java edits; separate Architect/User implementation authorization supplied. |
| 5 | Java implementation completed: two relocations/package changes, thirteen consumer imports and one placement guard, with zero new Java identities or aliases and no production-body changes. |
| 6 | Initial automated attempt failed at compileJava before tests ran; fail-stop left gates 2–7 NOT RUN at that stage. Initial output and preservation checks remain in Section 8. |
| 7 | Sol read-only forensic diagnostic returned HOLD_ACM_01_F01_DIAGNOSTIC_EVIDENCE_INSUFFICIENT: CF-U and PF-U, both CAUSE NOT ESTABLISHED. No additional code correction was justified. |
| 8 | User controlled clean reproduction: clean PASS; moved SwerveObservationTest PASS, all six tests including placement guard. |
| 9 | THE EARLIER FAILURE DID NOT REPRODUCE AFTER CONTROLLED CLEAN REPRODUCTION. A specific cache, daemon, Gradle, source-set, environment or implementation cause was not established. |
| 10 | Remaining User automated gates PASS: SwerveSubsystemTest, SwerveSubsystemKnownFieldPoseResetTest, SwerveSubsystemPoseEstimatorTest, SwerveTelemetryFacadeTest, full suite and clean build. |
| 11 | Independent Sol review PASS: PASS_ACM_01_F01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION. |
| 12 | Architect/User-authorized reconciliation completed in exactly this ADR and the eight named existing documents. Independent final closure review is pending. |

### ACM-01-F01 Step A1 — Adjudicate and register the exceptional repair

- Objective: resolve the inherited placement conflict under the governing authority.
- Why: mechanism-specific types belong in frc.robot.observation.<mechanism>.
- Action: Architect adjudicated the conflict and accepted Sol bounded design PASS;
  explicit implementation authorization followed. The distinct ADR and lifecycle
  registration were recorded before Java changes.
- Files Changed: dedicated ACM-01-F01 ADR and the eight authorized existing records.
- Verification: governance integrity PASS and accepted design token preserved in ADR.
- Expected Result: M00_L16 alone is REOPENED / IN_PROGRESS / EDITABLE; historical
  predecessors remain read-only; no new concept or M00_L17.

### ACM-01-F01 Step A2 — Relocate the existing model and test

- Objective: use frc.robot.observation.swerve.SwerveObservation.
- Why: align the existing mechanism model with OC-02 without changing its meaning.
- Action: move the model and matching semantic test into observation/swerve;
  change only their package declarations before the narrow test extension.
- Files Changed: model old/new paths and test old/new paths named in ADR Section 3.
- Verification: independent normalized-byte review confirms unchanged model contract
  and inherited semantic tests/helpers; source old paths absent, new paths present.
- Expected Result: two relocations, zero new Java identities and no alias.
  Outer/nested qualified and binary identities change; compiled consumers must rebuild.

### ACM-01-F01 Step A3 — Migrate consumer imports

- Objective: make all current M00_L16 consumers reference the relocated model.
- Why: compilation must resolve the canonical qualified name.
- Action: replace exactly thirteen imports in five production and eight test consumers.
- Files Changed: exact consumer paths in ADR Section 4; no additional Java identity.
- Verification: independent review confirms each consumer body is unchanged after
  normalizing the authorized import replacement.
- Expected Result: no production method-body, algorithm, unit, sign, telemetry-key
  or scheduler/control change; Frozen Backbone preserved.

### ACM-01-F01 Step A4 — Add the narrow placement guard

- Objective: prevent the old root-package source/class from surviving the migration.
- Why: source and runtime classpath placement both matter after the binary-name change.
- Action: extend the moved existing SwerveObservationTest with one guard checking
  canonical name, new source presence, old source absence and old class absence.
- Files Changed: moved existing test only; two java.nio imports for the guard.
- Verification: User all six model tests PASS, including
  usesMechanismSpecificPackageWithoutLegacyClass(); old compiled model/test classes
  absent and new package classes present in clean User build artifacts.
- Expected Result: no legacy compatibility class; five inherited semantic tests retained.

### ACM-01-F01 Step A5 — Preserve the first failed verification and forensic HOLD

- Objective: retain the actual fail-stop chronology and its evidence limits.
- Why: later PASS must not erase an earlier failure or invent its cause.
- Action: the initial model-test invocation failed during compileJava, before tests
  ran. Gates 2–7 were NOT RUN then. Sol performed read-only forensic diagnosis.
- Files Changed: initial registration/ADR evidence only; no extra Java repair,
  retry, cache cleanup or build-file change by the initial implementation stage.
- Verification: initial BUILD FAILED in 59s, exit 1, one actionable task executed;
  100 compiler diagnostics involved broad unresolved existing symbols.
  No compiler diagnostic identified SwerveObservation as the failure.
  Sol classification: CF-U — CAUSE NOT ESTABLISHED;
  PF-U — CAUSE NOT ESTABLISHED.
- Expected Result: preserve the failure and the historical fingerprint count
  12,119 -> 12,118. The original per-path/hash manifest was not retained;
  the exact pathname difference cannot be reconstructed. Do not infer a pathname,
  unauthorized modification or resolution.

### ACM-01-F01 Step A6 — Record subsequent User verification and independent review

- Objective: record the authorized controlled reproduction and remaining gates.
- Why: fresh User verification and independent technical review establish the
  implemented correction's acceptance.
- Action: User WPILib Temurin Java 17.0.16 clean reproduction in M00_L16 PASS;
  moved model test PASS, all six tests/guard; remaining focused tests, full suite
  and clean build PASS. Sol independent post-implementation review PASS.
- Files Changed: none by this evidence-recording action beyond authorized documentation.
- Verification: accepted User output and review token are recorded below.
- Expected Result: THE EARLIER FAILURE DID NOT REPRODUCE AFTER CONTROLLED CLEAN
  REPRODUCTION. No cache, daemon, Gradle, source-set, environment or implementation
  cause is proven. CF-U remains CF-U. PF-U remains unresolved historical limitation,
  accepted as nonblocking for ACM-01-F01 technical acceptance; current read-only
  scope/protected-state checks found no unauthorized tracked repair expansion.

### Accepted ACM-01-F01 evidence and applicability

Canonical model: `frc.robot.observation.swerve.SwerveObservation`.
The existing model and corresponding test were relocated, thirteen consumer
imports changed, and one narrow placement regression guard was added.
Exactly 15 existing Java identities were affected: zero new identities,
zero compatibility aliases and no production method-body change.
SwerveObservation semantics, the Frozen Backbone and historical snapshots are
preserved. No repair was propagated backward.

The initial attempt failed during compileJava before tests ran; broad existing
symbols were unresolved and no compiler diagnostic identified SwerveObservation
as the failure. The forensic classifications remain CF-U and PF-U, both
CAUSE NOT ESTABLISHED. The User controlled clean reproduction passed clean and
all six SwerveObservationTest tests, including the placement guard.
THE EARLIER FAILURE DID NOT REPRODUCE AFTER CONTROLLED CLEAN REPRODUCTION.
No cache, daemon, Gradle, source-set, environment or implementation cause was proven.

| Automated gate | Accepted User result |
| --- | --- |
| Moved SwerveObservationTest | PASS — all six tests, including placement guard |
| SwerveSubsystemTest | PASS |
| SwerveSubsystemKnownFieldPoseResetTest | PASS |
| SwerveSubsystemPoseEstimatorTest | PASS |
| SwerveTelemetryFacadeTest | PASS |
| Full test suite | PASS |
| Clean build | PASS |

Final supplied User evidence:

```text
BUILD SUCCESSFUL in 45s
7 actionable tasks: 7 executed

ACM-01-F01 AUTOMATED USER VERIFICATION COMPLETE
ALL REQUIRED AUTOMATED GATES PASS
```

No additional test counts or exit codes are inferred. These are User results;
this reconciliation did not rerun project verification.

PF-U preserves the historical protected fingerprint count 12,119 -> 12,118.
The original path/hash manifest was not retained, so the exact historical
pathname difference cannot be reconstructed. The discrepancy is unresolved;
no pathname or unauthorized modification is inferred.
Accepted independent review: PF-U does NOT block ACM-01-F01 technical acceptance.
Current read-only scope/protected-state checks found no unauthorized tracked
repair expansion.

| Verification applicability | Approved disposition |
| --- | --- |
| Focused automated tests | REQUIRED — COMPLETE / PASS |
| Full test suite | REQUIRED — COMPLETE / PASS |
| Clean build | REQUIRED — COMPLETE / PASS |
| Simulation | NOT REQUIRED FOR REPAIR CLOSURE |
| Glass | NOT APPLICABLE |
| Driver Station | NOT APPLICABLE |
| Real hardware | NOT REQUIRED FOR REPAIR CLOSURE |

No fresh Simulation, Glass, Driver Station or real-hardware execution is claimed
for ACM-01-F01. Existing physical-evidence limits remain preserved.
This documentation reconciliation does not accept final closure or finalize
the exceptional-repair Transition Guide for freeze; independent review is pending.

Accepted independent Sol review:
`PASS_ACM_01_F01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.

### ACM-01-F01 Step A7 — Reconcile documentation and await independent closure

- Objective: make the authorized nine-document record agree with the accepted evidence.
- Why: current status must distinguish implementation/verification from final closure.
- Action: update bounded current summaries, preserve original failed-stage/history,
  and append this repair chronology and the dedicated ADR's later evidence.
- Files Changed: exactly the existing dedicated ACM-01-F01 ADR, AGENTS.md, root README,
  M00 roadmap ADR, M00_L16 README/STATUS/PLAN/CHECKLIST and this guide.
- Verification: static governance integrity and documentation preservation/scope
  self-audit PASS. No project verification rerun and no final closure review here.
- Expected Result: M00_L16 EXCEPTIONAL REPAIR IN PROGRESS; ACM-01-F01 IMPLEMENTED /
  VERIFIED / INDEPENDENTLY REVIEWED / DOCUMENTATION RECONCILED.
  Exact next gate: INDEPENDENT FINAL ACM-01-F01 ARCHITECTURE / CLOSURE REVIEW.

Final exceptional-repair Transition Guide acceptance remains pending that review.
P3-H01: CLOSED / PRESERVED; CFG-H01: CLOSED / REMOVED.
Historical S00_L05–L24, A00_L01–L04, A01_L01–L09, V00_L01–L09 and M00_L01–L15
remain unchanged. M00_L16 alone is the canonical correction target.
Original and prior repaired publication identities remain historical evidence;
no new ACM-01-F01 publication or re-freeze is claimed.
ACM-01: HOLD pending repair closure + ACM-01 rereview.
ACM-02: NOT STARTED. Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.
Phase-2 and historical D2A/H01/R1/A01_L07/byte qualifications remain preserved.
Protected/unrelated A01_L06_OneMeter_Forward.path and all established excluded
paths remain untouched. Constants.java cleanup/refactor is excluded.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.
<!-- ACM-01-F01 EVIDENCE APPENDIX END -->


### ACM-01-F01 Step A8 — Record formal repair closure

- Objective: record the Architect-authorized closure of ACM-01-F01 only.
- Why: independent final review accepted all four closure dimensions and
  found no remaining ACM-01-F01 repair requirement.
- Action: record the final review token
  PASS_ACM_01_F01_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION
  and the subsequent explicit Architect closure authorization. Mark
  ACM-01-F01 CLOSED while retaining CF-U and PF-U as unresolved historical
  limitations, the failed attempt and later User PASS evidence.
- Files Changed: the nine existing ACM-01-F01 lifecycle/closure documents
  within the authorized boundary; no Java or test changes.
- Verification: static governance integrity and bounded documentation
  preservation/scope self-audit. No project execution or Git write.
- Expected Result: ACM-01-F01 CLOSED; M00_L16 remains IN_PROGRESS under the
  exceptional lifecycle; ACM-01 remains HOLD — READY FOR INDEPENDENT DOMAIN
  REREVIEW; ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN.
  Exact next gate: INDEPENDENT ACM-01 DOMAIN REREVIEW.

This closes the ACM-01-F01 repair record and accepts this guide's repair
evidence for that closure. It does not re-freeze or republish M00_L16.

<!-- ACM-01-F02 RECONCILIATION EVIDENCE BEGIN -->
## ACM-01-F02 exceptional repair — accepted evidence reconciliation, 2026-09-30

### Chronology and implementation

ACM-01 domain rereview discovered the S00_L23-inherited Swerve-drive
validation observation placement defect. The Architect accepted F02 and
parked accepted F03/F04 in the locked order F02 -> F03 -> F04. Sol bounded
design PASS was followed by explicit registration and implementation
authorization. The dedicated F02 ADR and eight ledger registration records
were created before Java changes; those registration-stage passages are
preserved. The prospective correction affected M00_L16 only.

Five existing Java identities changed: the observation moved from root
`observation` to `observation.swerve` with only its package declaration
changed; DriveThreeMeterValidationCommand, DriveThreeMeterValidationTelemetry,
DriveThreeMeterValidationTelemetryFacade and DriveThreeMeterValidationCommandTest
changed only the F02 import. The earlier F01 SwerveObservation import remains
preserved. One new test, `observation/swerve/DriveThreeMeterValidationObservationTest.java`,
guards qualified name, new source path, absent old path and absent old class
via a negative Class.forName assertion. No scanner, absolute test path,
cross-lesson dependency or behavior redesign was added.

Static and independent review confirmed exact scope, reverse-normalized
equivalence, preserved record components/constructors/idle factory/units/signs/
module ordering/distance interpretation/comments, preserved command behavior/
telemetry topics/published fields/behavioral assertions, zero production
method-body changes, zero aliases and zero historical migrations. The old
production identity is absent; only its intentional negative test string remains.

### Initial verification failure and User restart

The first Gate 1 attempt failed during Gradle configuration before tasks ran:
that invocation used Java 8, while GradleRIO 2026.2.1 requires Java 17+.
Neither clean nor the focused test completed. This is a verification
environment/JVM-version failure, not evidence of an F02 code defect.
Why Java 8 was selected is not established; no unsupported cause is inferred.

The User explicitly set `JAVA_HOME=C:\Users\Public\wpilib\2026\jdk`,
confirmed `openjdk version "17.0.16"` / `Temurin-17.0.16+8`,
and restarted the authorized fail-stop sequence.

| Gate | Accepted User-owned evidence |
| --- | --- |
| Gate 1 clean | PASS — BUILD SUCCESSFUL in 12s; 1 actionable task: 1 executed. |
| Gate 1 focused placement test | PASS — `usesSwervePackageWithoutLegacyClass() PASSED`; BUILD SUCCESSFUL in 31s; 4 actionable tasks: 4 executed. |
| Gate 2 DriveThreeMeterValidationCommandTest | PASS — supplied output includes `abortsAtDeterministicTimeout() PASSED`; BUILD SUCCESSFUL in 13s; 4 actionable tasks: 1 executed, 3 up-to-date. |
| Gate 3 full suite | PASS — BUILD SUCCESSFUL in 22s; 4 actionable tasks: 1 executed, 3 up-to-date. |
| Gate 4 clean build | PASS — BUILD SUCCESSFUL in 46s; 7 actionable tasks: 7 executed. |

Final User marker:

```text
ACM-01-F02 GATE 4 PASS
CLEAN BUILD PASS
ALL REQUIRED F02 USER AUTOMATED VERIFICATION GATES PASS
```

No total test count, unseen test name or unseen exit code is inferred.
Sol independent post-implementation review returned:
`PASS_ACM_01_F02_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
It confirmed the accepted sequence complete, no further gate required,
Frozen Backbone preserved, historical copies preserved and F03/F04 parked.
The four automated gate categories are REQUIRED — COMPLETE / PASS.
Simulation is NOT REQUIRED; Glass/Driver Station are NOT APPLICABLE;
real hardware is NOT REQUIRED FOR F02 CLOSURE. No fresh runtime evidence
is claimed. Full details are in [the F02 ADR, Section 2](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#2-accepted-implementation-and-evidence-reconciliation--2026-09-30).

### Current stage

F02 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
F02 is NOT CLOSED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED. F01 and DOC-01 remain
CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED.
Exact next gate: INDEPENDENT FINAL ACM-01-F02 ARCHITECTURE / CLOSURE REVIEW.

F01/DOC-01 history and CF-U/PF-U cause-not-established limits are preserved
separately. No backward migration into S00_L23–L24, A00, A01, V00 or
M00_L01–L15 occurred. Frozen Backbone ownership, IO contracts, scheduler
requirements, safe stop, constants and real/simulation selection remain
preserved; no ownership flags or polling arbitration were added.
No re-freeze, publication, Constants cleanup/refactor or new lesson occurs.
The unrelated A01_L06 path remains untouched.
`github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.
<!-- ACM-01-F02 RECONCILIATION EVIDENCE END -->

<!-- ACM-01-F02 FORMAL CLOSURE BEGIN -->
## ACM-01-F02 exceptional repair — formal closure, 2026-09-30

### Objective, authority and recorded action

Record the explicitly Architect-authorized ACM-01-F02 repair closure after
Sol's independent final review:
`PASS_ACM_01_F02_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`.

The review established all four F02 dimensions CLOSED and no substantive
repair requirement. The Architect subsequently issued explicit F02
REPAIR CLOSURE authorization. This bounded documentation action records
**ACM-01-F02: CLOSED**. Technical, verification, documentation and
architecture dimensions are **CLOSED**; remaining F02 requirements are
**NONE**.

The original transition steps and all earlier appendices remain historical
evidence. The earlier F02 reconciliation appendix's current/pending wording
describes its pre-closure stage. The formal decision and version 1.2 are
recorded in [the F02 ADR, Section 3](../../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#3-formal-acm-01-f02-repair-closure--2026-09-30).

### Closure basis and preserved verification

The valid OC-02 Section 1 finding is resolved in M00_L16 at
`frc.robot.observation.swerve.DriveThreeMeterValidationObservation`.
Registration preceded Java modification. The accepted five-existing-plus-one-new
Java boundary, import-only consumers, focused placement guard, semantic
preservation, reverse-normalized PASS, absence of the old production identity
and absence of aliases remain preserved. Independent implementation review,
documentation reconciliation and independent final closure review PASS.
Frozen Backbone and all historical copies remain preserved; F03/F04 remained
parked.

The initial Gate 1 Java-8 invocation remains historical: GradleRIO 2026.2.1
requires Java 17+; configuration failed before tasks ran; neither clean nor
DriveThreeMeterValidationObservationTest completed. This was a
VERIFICATION ENVIRONMENT / JVM VERSION FAILURE and established no F02 code
defect. Why Java 8 was selected remains not established.

The preceding evidence appendix retains the User's corrected
`JAVA_HOME=C:\Users\Public\wpilib\2026\jdk`, OpenJDK Temurin
17.0.16+8, clean PASS in 12s, focused guard PASS in 31s, direct consumer
test PASS in 13s, full suite PASS in 22s and clean build PASS in 46s.
The final User marker remains
`ALL REQUIRED F02 USER AUTOMATED VERIFICATION GATES PASS`.
No test count or unseen exit code is inferred; no verification was rerun.

The four automated categories are REQUIRED — COMPLETE / PASS. Simulation
is NOT REQUIRED; Glass/Driver Station are NOT APPLICABLE; real hardware is
NOT REQUIRED FOR F02 CLOSURE. Existing physical-evidence limits persist.

### Files changed, verification and resulting state

This closure recording updates only the existing nine-document F02 repair
record. Java/source/tests, configuration, dependencies, assets and historical
lessons remain untouched. No Git write or project execution occurs.

F01 and F01-DOC-01 remain CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01
CLOSED / REMOVED. CF-U and PF-U remain CAUSE NOT ESTABLISHED.
F03 and F04 remain ACCEPTED / PARKED. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 remains HOLD; ACM-02 is
NOT STARTED; Phase 4 is NOT STARTED / FORBIDDEN. No M00_L17.
Constants.java cleanup/refactor and the unrelated A01_L06 path remain
untouched. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

Expected and recorded next gate:
**ARCHITECT ACTIVATION / DESIGN AUTHORIZATION FOR ACM-01-F03**.
This closure action includes no F03/F04 work.
<!-- ACM-01-F02 FORMAL CLOSURE END -->


## ACM-01-F03 exceptional repair reconciliation — 2026-09-30

This appendix records the prospective M00_L16 package repair after the
original transition and F01/F02 repair history; those earlier steps and
historical lesson copies are unchanged.

1. The ACM-01 domain rereview discovered the A01_L03-inherited
   `frc.robot.util.LearningTrajectoryFactory` placement. F03 was
   accepted and initially parked behind F02.
2. F02 was formally closed. The Architect then activated F03 for
   read-only design. Sol found the governance-supported
   `frc.robot.commands.auto.LearningTrajectoryFactory` destination;
   the Architect approved that target and four-identity boundary.
3. The dedicated F03 ADR and eight ledger entries registered the
   decision before any F03 Java edit.
4. The M00_L16-only repair moved the factory and dedicated test, changed
   exactly two external test imports, preserved ten behavioral tests and
   added one placement guard. Four existing Java identities changed;
   zero new Java identities or aliases were added.
5. User-owned WPILib Temurin 17 verification PASSed: Gate 1 clean plus
   focused factory test; Gate 2 affected consumer tests; Gate 3 full-suite
   Gradle gate with all four actionable tasks UP-TO-DATE; Gate 4 clean
   build with seven actionable tasks executed. Gate 3 is not represented
   as fresh execution of every test; Gate 4 supplies clean-state
   complete-project execution evidence.
6. Sol's independent post-implementation review PASSed with
   `PASS_ACM_01_F03_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
   The accepted verification and review evidence is reconciled in the
   dedicated F03 ADR.

F03 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED, pending independent final closure review.
F04 remains ACCEPTED / PARKED; M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED; ACM-01 and Phase 3 remain HOLD.
The unrelated A01_L06 path stayed in its pre-existing modified state;
no protected-file write was reported, and no aggregate protected-content
digest match was established. This limitation is preserved without an
invented defect. No F03 closure, re-freeze, publication, ACM-02 or
Phase-4 claim is made here.

Exact next gate: INDEPENDENT FINAL ACM-01-F03 ARCHITECTURE / CLOSURE REVIEW.


## ACM-01-F03 formal repair closure — 2026-09-30

The prior F03 registration, implementation, User verification,
independent post-implementation review and documentation reconciliation
remain preserved above and in the dedicated F03 ADR.

Sol's independent final architecture/closure review returned
`PASS_ACM_01_F03_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`.
It classified TECHNICAL, VERIFICATION, DOCUMENTATION and ARCHITECTURE
dimensions CLOSED, with no substantive F03 repair requirement remaining.
The Architect subsequently authorized ACM-01-F03 REPAIR CLOSURE.
This appendix records **ACM-01-F03: CLOSED**. The dedicated ADR's
Section 4 governs the full closure decision.

The M00_L16-only package correction remains four changed existing
Java identities and zero new identities. Required User Java-17 Gates
1-4 PASS; Gate 3's full-suite tasks were UP-TO-DATE in that invocation,
and Gate 4 supplied clean-state build execution. No total test count
or unseen exit code is inferred. Historical predecessor lessons and
the Frozen Backbone remain preserved; F04 remains ACCEPTED / PARKED.

The unrelated A01_L06 path remains in its pre-existing modified state.
No protected-file write was reported, and an aggregate protected-content
digest match was NOT established. This limitation is not an established
defect. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED;
ACM-01 and Phase 3 remain HOLD; ACM-02 NOT STARTED; Phase 4
NOT STARTED / FORBIDDEN. No M00_L17 or publication is claimed.

Exact next gate: ARCHITECT ACTIVATION / DESIGN AUTHORIZATION FOR ACM-01-F04.
This F03 closure record performs no F04 design or implementation.

## ACM-01-F04 bounded repair chronology and evidence reconciliation — 2026-09-30

This appendix records the later F04 repair stages. The original M00_L15 to
M00_L16 transition steps and the F01, F02, and F03 histories above remain
preserved. The dedicated F04 ADR is the authoritative detailed record.

### F04 chronology

1. ACM-01 domain rereview identified ACM-01-F04 in M00_L16.
2. F04 was ACCEPTED / PARKED behind F02 and F03.
3. F02 and F03 were formally closed.
4. The Architect activated F04 for read-only design.
5. Sol identified an autonomous command family by responsibility, not a
   two-file or class-name repair.
6. The approved canonical package is frc.robot.commands.auto under
   Document A, Section 8.
7. The Architect approved the family boundary and exact identity count.
8. The dedicated F04 ADR and eight lifecycle registrations were recorded
   before Java changes.
9. The M00_L16-only implementation followed.
10. The User supplied accepted Java-17 environment evidence and ran the
    ordered verification gates.
11. All required User automated Gates 1–4 passed.
12. Sol independently reviewed the implementation and accepted verification.
13. Documentation/evidence reconciliation is complete; independent final
    closure review remains pending.

### Bounded implementation and preserved behavior

The exact change was 14 relocated production identities, 1 production
import-only identity (RobotContainer), 14 relocated dedicated test
identities, and 7 nonrelocating test import/reference identities: 36 existing
Java identities and zero new Java identities. No thirty-seventh identity
changed. The exact production/test rosters, origins, guard checks, and
consumer test list are in the F04 ADR.

RobotContainer changed only seven imports and retained its composition-root
methods and ownership. LEARNING_EVENT still supplies
() -> new IntakeToFeederCommand(intakeSubsystem, feederSubsystem), with
Set.of(intakeSubsystem, feederSubsystem). One fixed-list family-placement
guard covers exactly fourteen F04 production types. The seven consumer tests
and three unchanged static-name boundary tests are recorded in the ADR.
Reverse-normalized comparison matched 36/36 against the captured
post-F01/F02/F03 pre-F04 working-tree baseline, not a separate Git commit.
Closed F03 remains unchanged; AutonomousEventId is unchanged and
AutonomousStartContext remains in root commands. Protected hash evidence is
limited to the named files; no aggregate protected-content digest PASS is
claimed.

### Accepted User verification and independent review

The accepted environment evidence is WPILib JDK
C:\Users\Public\wpilib\2026\jdk, OpenJDK / Temurin 17. Sol accepted that
User evidence and did not independently rerun Java. Gate 1 passed after
clean and focused frc.robot.commands.auto.* tests: BUILD SUCCESSFUL in 55s,
4 actionable tasks: 4 executed. Gate 2 passed: BUILD SUCCESSFUL in 15s,
4 actionable tasks: 1 executed, 3 up-to-date. Gate 3 full test suite passed:
BUILD SUCCESSFUL in 25s, 4 actionable tasks: 1 executed, 3 up-to-date.
Gate 4 clean build passed: BUILD SUCCESSFUL in 1m 3s, 7 actionable tasks:
7 executed. The Gate 2 and Gate 3 cache nuance is preserved; seven actionable
tasks are not a test count, and no total test count or unseen exit code is
inferred.

Sol's independent review confirmed the exact implementation boundary,
behavioral and Frozen Backbone preservation, all four User gates, and the
36/36 reverse-normalized result. Accepted token:
PASS_ACM_01_F04_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION.
Interactive Simulation is NOT REQUIRED; Glass and Driver Station are NOT
APPLICABLE; real hardware is NOT REQUIRED FOR F04 PACKAGE-ONLY REPAIR
CLOSURE. No fresh interactive runtime evidence is claimed.

### Lifecycle and next gate

F04 is IMPLEMENTED / USER VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW; it is
NOT CLOSED. F01 and F01-DOC-01 remain CLOSED; F02 and F03 remain CLOSED /
FORMALLY RECORDED; P3-H01 remains CLOSED / PRESERVED; CFG-H01 remains
CLOSED / REMOVED; CF-U and PF-U remain CAUSE NOT ESTABLISHED. M00_L16
remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 remains HOLD;
Phase 3 remains HOLD / HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE;
ACM-02 is NOT STARTED; Phase 4 is NOT STARTED / FORBIDDEN. No M00_L17.

The exact next gate is INDEPENDENT FINAL ACM-01-F04 ARCHITECTURE / CLOSURE
REVIEW. This appendix does not close F04 or ACM-01, re-freeze M00_L16,
publish, start ACM-02, or start Phase 4.


## ACM-01-F04 formal repair closure - 2026-10-01

This closure entry completes the F04 chronology preserved in the reconciliation
appendix above. The earlier entry records registration before implementation,
the exact 36-existing/zero-new Java boundary, User Gates 1-4, the independent
implementation review, behavior and Frozen Backbone preservation, and the
reverse-comparison/protected-state limits. That entry's pending-closure status
is historical; this section and the dedicated F04 ADR Section 4 record the
current closure.

Sol's independent final closure review returned
`PASS_ACM_01_F04_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_AUTHORIZATION` and
classified all ten closure dimensions CLOSED, with no substantive F04 repair
work remaining. The Architect accepted that recommendation and explicitly
authorized **ACM-01-F04: FORMALLY CLOSED**. The Architect owns the closure
authorization; Sol's token is review evidence. This entry records F04 only.

The completed repair remains the M00_L16-only move of fourteen production
identities and fourteen dedicated tests into `frc.robot.commands.auto`, one
`RobotContainer` import-only identity, and seven nonrelocating test updates:
36 existing Java identities and zero new Java identities. `LEARNING_EVENT`
continues to use the fresh `IntakeToFeederCommand` supplier with Intake and
Feeder requirements. Gate evidence, cache nuance, reverse-normalized 36/36
working-tree comparison, named-file hash evidence, and the absence of an
aggregate protected-content digest PASS remain as recorded in the F04 ADR.
Interactive Simulation was NOT REQUIRED; Glass and Driver Station were NOT
APPLICABLE; real hardware was NOT REQUIRED for this package-only repair.
No new project execution is claimed here.

F01 and F01-DOC-01 remain CLOSED; F02 and F03 remain CLOSED / FORMALLY
RECORDED; F04 is FORMALLY CLOSED / FORMALLY RECORDED. P3-H01 remains CLOSED /
PRESERVED; CFG-H01 remains CLOSED / REMOVED; CF-U and PF-U remain CAUSE NOT
ESTABLISHED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED.
ACM-01 remains HOLD; ACM-02 remains NOT STARTED; Phase 4 remains NOT STARTED /
FORBIDDEN. No M00_L17. F04 closure does not close ACM-01.

Exact next gate: **INDEPENDENT ACM-01 DOMAIN REREVIEW**, to confirm that no
package or lesson architecture-boundary finding remains after F01-F04 closure.
This entry does not perform that review, close ACM-01, re-freeze or publish
M00_L16, start ACM-02, or start Phase 4. Historical predecessor lessons and
the Frozen Backbone remain preserved. The unrelated pre-existing A01_L06 path
remains untouched; no aggregate protected-content digest PASS is claimed.
`github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED. No Java/test edit,
Git write, or project execution occurred during this documentation step.
