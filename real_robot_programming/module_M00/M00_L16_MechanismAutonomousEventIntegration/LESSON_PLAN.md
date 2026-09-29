# M00_L16 — Mechanism Autonomous Event Integration Lesson Plan

## Current M00_L16 frozen repair and publication metadata — 2026-09-29

This record controls current M00_L16 lifecycle, repair closure and publication
metadata. The Architect/User explicitly authorized the prior re-freeze
documentation transition after the accepted final
independent closure review:
`PASS_M00_L16_EXCEPTIONAL_REPAIR_CLOSED_READY_FOR_REFREEZE_AUTHORIZATION`.
The Architect/User now authorizes only post-freeze publication metadata
reconciliation for the User-created repaired primary snapshot. This is the
established frozen-lesson metadata exception; M00_L16 is not reopened.
Earlier dated M00_L16 registration, reconciliation, activation, verification and
publication records below are preserved historical chronology. Their former
CURRENT, REOPENED, IN_PROGRESS, EDITABLE and PENDING wording describes those
earlier stages; it does not describe this repaired frozen candidate. Standing
governance and protected historical lesson scope remain in force.

- Status: COMPLETE.
- Active State: COMPLETE / FROZEN / READ-ONLY.
- Repository Active Lesson Count: 0; Active M00 Lesson Count: 0.
- Current Active M00 Lesson: NONE; editable lessons: NONE.
- Exceptional repair: CLOSED.
- Technical closure: CLOSED; verification closure: CLOSED.
- Documentation closure: CLOSED; architecture closure: CLOSED.
- Remaining repair requirements: NONE.
- P3-H01: IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED / PRESERVED / CLOSED.
- CFG-H01: IMPLEMENTED / VERIFIED / REMOVED / INDEPENDENTLY REVIEWED / CLOSED.
- Re-freeze authorization: APPROVED / CONSUMED by the prior documentation transition.
- Final repair Transition Guide: accepted through the final independent closure
  review; historical steps/appendices preserved and final re-freeze record appended.
- Independent re-freeze / frozen-candidate review: PASS —
  `PASS_M00_L16_FROZEN_CANDIDATE_READY_FOR_USER_PRIMARY_SNAPSHOT`.
- Repaired frozen PRIMARY SNAPSHOT / COMMIT 1: CREATED by the User —
  `015b8ca27d466a5a2fce2660a902bb58a4b62003`.
- Publication metadata reconciliation: COMPLETED / METADATA RECONCILED /
  READY FOR USER METADATA COMMIT.
- Repaired metadata commit / COMMIT 2: PENDING USER ACTION; DOES NOT EXIST YET.
- User push: PENDING / NOT PERFORMED YET.
- External final repaired-publication verification: PENDING / NOT PERFORMED YET.
- Repaired publication: NOT YET PUBLISHED; only the repaired primary snapshot
  has been created. No metadata commit identity is invented.
- Exact next gate: USER METADATA COMMIT — COMMIT 2.
- Governing repair record: [P3-H01 configuration-authority repair](../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md); original registration and reconciliation
  stages remain historical; final closure/re-freeze is recorded in Section 18
  and repaired-primary publication metadata in Section 19.

### Preserved implementation, verification and applicability

P3-H01 preserves Constants.VisionConstants ownership of
`kLowUncertaintyMaxDistanceMeters = 1.0`,
`kMediumUncertaintyMaxDistanceMeters = 2.0`,
`kMaximumAcceptedDistanceMeters = 3.0` and
`kMaximumFreshAgeSeconds = 0.250`. RobotContainer consumes/injects these defaults.
Inclusive comparisons and runtime semantics remain unchanged. Accepted fresh
User focused tests, full suite, clean build and bounded Simulation PASS remain
preserved with the independent architecture review.

Localization/heading initialization precedes expected accepted Vision estimator
fusion. Successful `LEARNING_EVENT` behavior is observed through
`/Intake/RequestedState` and `/Feeder/RequestedState`, rather than successful
AutonomousEvent lifecycle telemetry. The detailed accepted Simulation sequence
remains in the preserved evidence reconciliation below.

CFG-H01 preserves `public static final String kLimelightTableName = "limelight";`
in Constants.VisionConstants; VisionIOLimelight consumes that authority. The
effective endpoint remains `/limelight/json`. Both constructors and existing
adapter/IO/protocol/Observation behavior remain unchanged. The accepted User
VisionConfigurationAuthorityTest, unchanged VisionIOLimelightTest, Vision
regression suite, full suite and clean build all PASS; independent exact-delta
and static-removal review PASS remains preserved.

Final supplied CFG-H01 User output remains:

```text
BUILD SUCCESSFUL in 32s
7 actionable tasks: 7 executed
```

No numeric test count or exit code is inferred. These are accepted User results.

| Applicability | P3-H01 | CFG-H01 |
| --- | --- | --- |
| Simulation | COMPLETED — accepted fresh bounded PASS | NOT_REQUIRED_FOR_REPAIR_CLOSURE |
| Glass | NOT_REQUIRED_FOR_REPAIR_CLOSURE | NOT_APPLICABLE |
| Driver Station | COMPLETED | NOT_APPLICABLE |
| Real hardware | NOT_REQUIRED_FOR_REPAIR_CLOSURE | NOT_REQUIRED_FOR_REPAIR_CLOSURE |

These are separately accepted repair dispositions. THEORY VERIFIED / SIMULATION
VERIFIED / REAL HARDWARE DEFERRED distinctions remain preserved where applicable.
No fresh real Limelight/mechanism hardware validation, drivetrain calibration,
H1 convention promotion or BL quantitative maintenance completion is claimed.

### Publication, audit and protection boundary

Original historical publication remains primary
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
`3667290180fe1a9fd96265383e7412c142c18129`, original verification gate
`PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION`. Original identities and history
remain unchanged; they are not repaired-publication identities.

Phase 2 remains
`PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains HOLD / `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`:
independent frozen-candidate review and User primary snapshot creation are complete;
User metadata Commit 2, push and external final repaired-publication verification
remain pending before separately authorized audit resumption can be considered.
Phase 4 remains NOT STARTED / FORBIDDEN. D2A/H01, historical R1, A01_L07 and
historical-byte qualifications remain preserved. No M00_L17.

This post-freeze authorization covers publication metadata only in the existing
records requiring reconciliation. Source/tests, other lessons, dependencies and
assets remain protected. The unrelated A01_L06_OneMeter_Forward.path state remains
untouched. The canonical workflow remains User primary Commit 1, metadata
reconciliation, User metadata Commit 2, User push, then external final verification;
no third verification-only commit. No new files/ADRs, Git or project execution
occurs in this reconciliation; publication and audit resumption remain pending.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

### Current completed repair plan and next lifecycle gate

1. COMPLETE — P3-H01 bounded implementation, fresh verification and independent
   review preserved; correction CLOSED.
2. COMPLETE — CFG-H01 design/registration and separate exact-file implementation
   authorization, implementation, five User automated gates and independent
   exact-delta/static-removal review; finding REMOVED / CLOSED.
3. COMPLETE — Authorized documentation/evidence reconciliation and final repair
   Transition Guide accepted by independent final architecture/closure review.
4. COMPLETE — Technical, verification, documentation and architecture closure;
   remaining repair requirements NONE.
5. COMPLETE — Explicit Architect/User re-freeze authorization consumed;
   M00_L16 COMPLETE / FROZEN / READ-ONLY; active lesson counts 0.
6. PASS — INDEPENDENT RE-FREEZE / FROZEN-CANDIDATE REVIEW accepted:
   `PASS_M00_L16_FROZEN_CANDIDATE_READY_FOR_USER_PRIMARY_SNAPSHOT`.
7. COMPLETE / USER-OWNED — Repaired primary Commit 1 CREATED at
   `015b8ca27d466a5a2fce2660a902bb58a4b62003`.
8. COMPLETE — Authorized publication metadata reconciliation; ready for Commit 2.
9. NEXT / PENDING USER ACTION — USER METADATA COMMIT — COMMIT 2;
   metadata commit DOES NOT EXIST YET.
10. PENDING / USER-OWNED — Push, then external final repaired-publication verification.
11. HOLD — Phase-3 resumption consideration requires all preceding gates and
    separate audit authorization. Phase 4 remains NOT STARTED / FORBIDDEN.

No further repair implementation or verification requirement remains.
Applicability and physical-evidence limits are preserved in the current table.
The original sixteen-lesson M00 curriculum remains closed; no M00_L17.

## Historical P3-H01 + CFG-H01 documentation/evidence reconciliation — 2026-09-29 (before final closure/re-freeze)

This dated record controls current repair status and evidence. The original
2026-09-28 P3-H01 registration, the 2026-09-29 CFG-H01 registration-stage
planning in ADR Section 16, and earlier lifecycle/transition records remain
historical stage evidence. Their former NOT STARTED/PENDING entries are not
current implementation or verification results. Standing governance, approved
scope and separate closure requirements remain in force.

- Status: IN_PROGRESS.
- Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Repository Active Lesson Count: 1; Active M00 Lesson Count: 1.
- Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Stage 1 — P3-H01: IMPLEMENTED / FRESHLY VERIFIED / INDEPENDENTLY REVIEWED /
  PRESERVED / NOT REOPENED.
- Stage 2 — CFG-H01: GOVERNANCE REGISTERED / DESIGN APPROVED / IMPLEMENTED /
  USER AUTOMATED VERIFICATION PASS / INDEPENDENT REVIEW PASS / REMOVED / VERIFIED.
- CFG-H01 exact-file implementation authorization: APPROVED and executed in
  the prior bounded implementation stage; no source/test work is authorized now.
- Documentation/evidence: RECONCILED.
- Next gate: INDEPENDENT FINAL ARCHITECTURE / CLOSURE REVIEW — PENDING.
- Accepted CFG-H01 design:
  `PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.
- Accepted independent review:
  `PASS_CFG_H01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
- Architect/User authorization for this reconciliation: only the nine existing
  governance/lesson documents; no Java, tests, Git, project execution, re-freeze,
  publication or Phase-3 resumption.
- Governing amendment: [existing exceptional-repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md); preserved registration in Section 16 and
  current reconciliation/chronology in Section 17.

### Preserved P3-H01 correction and fresh evidence

Constants.VisionConstants owns the exact public static final double defaults:
`kLowUncertaintyMaxDistanceMeters = 1.0`,
`kMediumUncertaintyMaxDistanceMeters = 2.0`,
`kMaximumAcceptedDistanceMeters = 3.0`, and
`kMaximumFreshAgeSeconds = 0.250`.
RobotContainer constructs/injects the existing Policy and freshness using those
named defaults. Inclusive comparisons and runtime semantics remain unchanged.

Accepted fresh User evidence: focused VisionConfigurationAuthorityTest PASS,
full test suite PASS, clean build PASS, and fresh bounded Simulation PASS.
The accepted Simulation observations include clean startup, Disabled baseline,
gyro health, autonomous preparation, heading-reference initialization,
Pose / EstimatedPose initialization, Vision UNAVAILABLE baseline,
VALID_FRAME_A qualification and accepted fusion, unchanged-frame duplicate/stale
hold, VALID_FRAME_B fresh recovery, return to UNAVAILABLE,
ONE_METER_WITH_EVENT preparation and event-enabled autonomous execution,
LEARNING_EVENT Intake/Feeder semantic behavior, mechanism cleanup,
autonomous completion, consumed-readiness fail-closed behavior and normal exit.

Localization/heading initialization must precede expected accepted Vision
estimator fusion. Successful LEARNING_EVENT behavior is verified through
`/Intake/RequestedState` and `/Feeder/RequestedState`, not successful
AutonomousEvent lifecycle telemetry. This is preserved P3-H01 evidence, not
post-CFG-H01 Simulation or physical-hardware evidence. P3-H01 is not reopened.

### CFG-H01 completed exact boundary and independent review

The original finding was ONE inherited MISPLACED_CONFIGURATION:
VisionIOLimelight privately owned `kLimelightTableName = "limelight"`.
The earliest surviving occurrence is V00_L08, propagated through V00_L09 and
M00_L01-M00_L16 (18 lessons, one finding). Historical source remains unchanged.

All implementation paths below are relative to existing M00_L16:

| Existing file | Completed CFG-H01 change |
| --- | --- |
| `src/main/java/frc/robot/Constants.java` | Existing VisionConstants now owns `public static final String kLimelightTableName = "limelight";` before calibration fields; narrow JavaDoc clarification; prior declarations/values preserved. |
| `src/main/java/frc/robot/io/vision/VisionIOLimelight.java` | Imports Constants, removes the private endpoint default, and uses `Constants.VisionConstants.kLimelightTableName` in the public default constructor. |
| `src/test/java/frc/robot/VisionConfigurationAuthorityTest.java` | Existing guard extended with independent declaration/value, executable AST origin/removal, and actual default `/limelight/json` endpoint assertions. |

Exactly two existing production files and one existing test were changed for
CFG-H01; zero new source/test files. RobotContainer, VisionIO and
VisionIOLimelightTest remain unchanged for CFG-H01. Both original constructors,
adapter protocol/parsing/validation/timing/session/frame/geometry/Observation
behavior, and prior P3-H01 test bodies/helpers are preserved. No getter,
factory, configuration object, test seam or dependency was added.
NetworkTables/vendor/protocol ownership stays inside the concrete IO adapter.

The independent review found no implementation defect or scope violation.
Constants uniquely owns the configured identity; endpoint remains exactly
`"limelight"` and `/limelight/json`. Tests use independent literal oracles;
comments cannot satisfy the executable AST source-origin guard.
No behavior change was intended or established. CFG-H01 is REMOVED / VERIFIED
in current M00_L16; historical predecessor findings are not erased.

### Accepted CFG-H01 User verification and applicability

| Gate | Accepted evidence / approved disposition |
| --- | --- |
| Focused VisionConfigurationAuthorityTest | PASS — fresh User execution |
| Unchanged VisionIOLimelightTest | PASS — fresh User execution |
| Relevant inherited Vision regression suite | PASS — fresh User execution |
| Full test suite | PASS — fresh User execution |
| Clean build | PASS — fresh User execution |
| Independent exact-delta/static review | PASS — accepted independent Sol review |
| Fresh Simulation | NOT REQUIRED FOR CFG-H01: Simulation selects VisionIOSim rather than the real Limelight adapter. |
| Glass | NOT APPLICABLE |
| Driver Station | NOT APPLICABLE |
| Real hardware | NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE: endpoint remains exactly `"limelight"`; configuration authority changed without deployed endpoint behavior change. |

Final supplied User output:

```text
BUILD SUCCESSFUL in 32s
7 actionable tasks: 7 executed
CFG-H01 AUTOMATED USER VERIFICATION COMPLETE
```

These are User-supplied execution results, not Codex execution. Seven actionable
tasks are not a numeric test count. No test count or exit code is invented.
CFG-H01 applicability applies only to CFG-H01; it does not substitute for
P3-H01 Simulation evidence or silently resolve separate earlier
applicability/closure requirements. Original REAL HARDWARE DEFERRED and
physical-evidence limits remain preserved.

### Lifecycle, publication and remaining gates

Documentation/evidence and the bounded repair chronology are RECONCILED.
The repair evidence appendix is ready for independent final review; no final
closure or final repair Transition Guide acceptance is claimed by this turn.
Next: INDEPENDENT FINAL ARCHITECTURE / CLOSURE REVIEW, including explicit
disposition of any separate outstanding earlier applicability/closure gate.
Explicit Architect/User re-freeze, independent freeze review, User-owned new
repaired primary/metadata commits and push, and external repaired-publication
verification remain PENDING. M00_L16 remains IN_PROGRESS, not COMPLETE/FROZEN.

Original publication remains historical truth:
primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`,
metadata `3667290180fe1a9fd96265383e7412c142c18129`,
gate `PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION`.
No original identity/history is rewritten, no old PASS becomes fresh repair
evidence, and the repaired working state has not been republished.

Phase 2 remains
`PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
Phase 4 remains NOT STARTED / FORBIDDEN. Audit resumption requires separate
authorization; reviewed removal and documentation reconciliation do not resume it.
D2A/H01, historical R1, A01_L07 and historical-byte limits remain qualified.
M00's original sixteen-lesson curriculum remains closed; no M00_L17.

All other lessons/source/tests/assets/dependencies remain protected.
The pre-existing A01_L06_OneMeter_Forward.path difference is unrelated and
untouched. No normalization, restoration, cleanup or staging is authorized.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

STOP/HOLD on unauthorized drift, failed/missing evidence required for a claimed
gate, changed endpoint/numerical behavior, architecture expansion or another
lesson. No automatic rollback or scope expansion.

### Historical bounded repair plan and completed chronology — before final closure/re-freeze

1. COMPLETE — Govern P3-H01, authorize bounded implementation, implement,
   freshly verify and independently review; preserve its correction and evidence.
2. COMPLETE — Discover CFG-H01, review the bounded design and register the amendment.
3. COMPLETE — Obtain separate exact-file implementation authorization and relocate
   the endpoint identity within Constants.java, VisionIOLimelight.java and the
   existing VisionConfigurationAuthorityTest.java only.
4. PASS — User runs the five required CFG-H01 automated gates; accept the supplied
   focused/adapter/Vision/full-suite/clean-build evidence.
5. PASS — Independent post-implementation exact-delta/static review confirms
   removed finding, endpoint/API/behavior preservation and protected-file isolation.
6. RECONCILED — Authorized documentation/evidence and appended repair chronology
   updated in the nine existing documents; historical records remain preserved.
7. NEXT / PENDING — Independent final architecture/closure review; explicitly
   resolve any separate earlier outstanding applicability/closure requirement.
8. PENDING — Explicit Architect/User re-freeze approval and independent freeze review.
9. PENDING / USER-OWNED — New repaired primary/metadata commits, push and external
   repaired-publication verification under separately authorized scope.

Approved CFG-H01 applicability remains Simulation NOT REQUIRED, Glass/Driver
Station NOT APPLICABLE, and hardware NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE.
M00_L16 remains REOPENED / IN_PROGRESS / EDITABLE; no repeat P3-H01 implementation,
new simulation fixture, new lesson or Phase-3 resumption is planned by this turn.


## Historical P3-H01 exceptional-repair governance registration — 2026-09-28

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
- Governing repair ADR: [P3-H01 configuration-authority repair](../../../docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md).

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

### Bounded future repair plan

1. User supplies fresh Java 17 baseline, original-source provenance, inherited
   focused/full-suite and clean-build evidence. Review scope and protected files.
2. Confirm the accepted ownership design and receive separate Architect/User
   implementation authorization naming exactly the two production files and
   one new test file. Baseline/review failure blocks implementation.
3. In Constants only, add the four public static final double defaults to existing
   VisionConstants and narrowly clarify its JavaDoc; preserve all calibration.
4. In RobotContainer only, replace the existing three Policy literals and one
   freshness literal with the named constants. Keep Policy composition here and
   retain the constructor, argument order, timestamp supplier, and event bindings.
5. Add only VisionConfigurationAuthorityTest.java. Prove independent exact values,
   production constructor references, actual injected configuration using existing
   test-only fixture/reflection conventions, inclusive boundary behavior, and pure
   evaluator dependencies. Source-origin checks complement runtime value equality.
6. Independently review the exact delta and static removal of P3-H01. User then
   supplies fresh focused/guard, inherited/full-suite, clean-build, Simulation,
   and Driver Station evidence. Existing tests remain unchanged.
7. Resolve Glass and real-hardware applicability explicitly. Reconcile evidence,
   complete final architecture/closure review and repair appendix, then seek
   explicit Architect/User re-freeze and independent freeze review.
8. User publishes a new repaired primary snapshot and separate metadata identity;
   preserve both original identities and obtain external publication verification.
   Phase-3 resumption remains a separate authorization after reviewed static
   removal, with accurate lifecycle/publication qualifications.

Boundary proof covers 1.0/2.0/3.0 m and below/equal/above 0.250 s, including
inclusive limits. Keep independent literal test oracles and arbitrary test policies.
Rerun existing quality, timing, VisionSubsystem, VisionIOSimHarness, fusion,
mechanism, scheduler, autonomous, and eight-event integration tests unchanged.
Simulation will exercise existing valid-frame qualification/fusion and
unavailable/stale handling, event and event-free controls, Disabled cleanup,
no observed fatal runtime exception, and normal exit. Tests cannot substitute
for User runtime Simulation. No physical calibration is required by relocation
alone; applicable hardware verification still needs an explicit decision.
Every future implementation/verification/publication step remains PENDING.

## Historical pre-Commit-2 publication metadata reconciliation — 2026-09-27

M00_L16 remains COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED; Active M00
Lesson Count is 0 and Current Active M00 Lesson is NONE. Independent Freeze
Review passed as `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW` with verdict
`M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
The User created Primary Frozen Snapshot Commit 1 at
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata
reconciliation is COMPLETE / `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`,
prepared for User-owned Metadata Publication Commit 2. Commit 2 and its hash,
publication push, and final external publication verification remain PENDING.
Evidence stays THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.
The accepted closure HOLD, bounded README repair, closure rereview PASS, and
Freeze Reconciliation PASS remain in the historical record below. The
two-commit Historical Snapshot model has no third verification-only commit.
M00_L16 remains the final M00 lesson; no M00_L17 is authorized.

## Historical freeze reconciliation — 2026-09-27

The locked one concept is implemented: `LEARNING_EVENT` dispatches a fresh
`IntakeToFeederCommand` through the inherited deferred registration with
exact Intake and Feeder requirements. The source delta is one production
modification (`RobotContainer.java`), one bounded inherited architecture-test
modification, and one new integration test file with exactly eight tests;
221 other source files remain unchanged from M00_L15. The inherited marker,
event-free path, chooser, teleop bindings, and mechanism code are unchanged.

The initial independent static review HOLD required only an architecture-guard
repair; the bounded repair and fresh independent static rereview passed.
User focused tests passed (architecture guard and 8/8 integration tests;
BUILD SUCCESSFUL, exit 0). User clean regression passed with BUILD SUCCESSFUL
and no regression blocker; no numeric count or exit code was supplied.
Accepted `PASS_M00_L16_BOUNDED_SIMULATION` verified the event-path semantic
requests, event-free control, Driver Station attachment, Disabled cleanup,
no observed fatal scheduler/runtime exception, and normal return to the
PowerShell prompt. The Simulation exit code was not supplied. Blank
AutonomousEvent NT fields are expected because no success lifecycle
observation is emitted. Evidence is THEORY VERIFIED / SIMULATION VERIFIED /
REAL HARDWARE DEFERRED. Physical performance is unverified.

The first Independent Closure Review returned
`HOLD_M00_L16_INDEPENDENT_CLOSURE_REVIEW` for the README hierarchy. The bounded
README repair passed as `PASS_M00_L16_CLOSURE_DOCUMENTATION_REPAIR`, and the
fresh Independent Closure Review passed as
`PASS_M00_L16_INDEPENDENT_CLOSURE_REVIEW` with no remaining findings.
M00_L16 is now `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`; Active M00
Lesson Count is 0 and Current Active M00 Lesson is NONE. Freeze Reconciliation
is COMPLETE / `PASS_M00_L16_FREEZE_RECONCILIATION`. Independent Freeze Review
is PENDING, and User-owned publication is PENDING / NOT PUBLISHED. Evidence
remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.
M00_L15 remains frozen and verified; M00_L16 is the final M00 lesson.

## Historical controlled activation plan — 2026-09-27

M00_L15 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED by accepted
User evidence: primary snapshot `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`,
metadata publication `0d3685ce67a0b985459392621e003611eaa6dc35`,
and `PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`. M00_L16 was copied from
that frozen predecessor. Its untouched-copy baseline, 345/345 authored-file
inheritance audit, and `PASS_M00_L16_FINAL_DESIGN_LOCK` are accepted.
Controlled activation makes M00_L16 the sole `IN_PROGRESS / ACTIVE /
EDITABLE WITHIN FINAL DESIGN LOCK` M00 lesson. Independent activation
review and implementation authorization are PENDING; this plan does not
authorize implementation.

### One new concept

Scheduler-managed dispatch of one already-verified mechanism command through
the existing PathPlanner named-event boundary. Select
`IntakeToFeederCommand` and reuse `LEARNING_EVENT`. The M00_L15 command
already requires IntakeSubsystem and FeederSubsystem, implements startup and
cleanup, and needs no new numeric setpoint. Do not select `ShootCommand`:
RPM configuration would add another lesson question. Do not add a second
event, event ID, mechanism algorithm, subsystem, IO contract, timing policy,
or autonomous framework.

### Future production delta

Modify exactly `src/main/java/frc/robot/RobotContainer.java` to replace
the demonstration `LEARNING_EVENT` binding with a supplier that constructs
a fresh `IntakeToFeederCommand(intakeSubsystem, feederSubsystem)` each
dispatch. The binding's requirements are exactly IntakeSubsystem and
FeederSubsystem. Use existing subsystem instances and inherited
`Supplier<Command>` plus `Commands.defer(...)` registration. WPILib
CommandScheduler requirements arbitrate external mechanism contention;
PathPlanner's inherited EventScheduler handles event-child execution.
No manual busy flags or manual conflict arbitration.

No production file is added. `AutonomousEventRegistration`,
`AutonomousEventBinding`, `AutonomousEventId`,
`IntakeToFeederCommand`, Intake/Feeder/Flywheel/Elevator subsystems,
mechanism IO, Observations, telemetry, AutoBuilder, trajectory and path
factories, Swerve, Vision, pose estimation, and Constants remain unchanged.
Preserve Right Bumper `RunIntakeCommand whileTrue` and Left Bumper
`RunFeederCommand whileTrue`; add no driver/default binding.
Preserve `SAFE_STOP`, `ONE_METER_PATH`, and
`ONE_METER_WITH_EVENT` chooser options. Do not edit the existing
`A01_L09_OneMeter_With_Learning_Event.path` marker, position, geometry,
or constraints; `ONE_METER_PATH` remains event-free. No deploy,
vendordep, Gradle, or configuration delta is planned.

### Hold-event safety and factory failure

`IntakeToFeederCommand.isFinished()` is false. Verified normal lifecycle:
`FollowPathCommand.end(...)` → `EventScheduler.end()` → active event
`end(true)` → wrapped registered event → WPILib
`DeferredCommand.end(true)` → `IntakeToFeederCommand.end(...)` →
`feeder.stop()` → `intake.stop()`. This supports normal path completion
and interruption. It does not guarantee cleanup after an arbitrary
uncaught library exception. Do not add a WaitCommand, timer, timeout,
race/deadline group, wrapper, or state machine. An event child supplier
`RuntimeException` follows the inherited `FACTORY_FAILURE` observation
and safe no-op path; no mechanism output is requested by the failed child.

### Exact future test delta — eight focused obligations

Add one file:
`src/test/java/frc/robot/RobotContainerMechanismAutonomousEventIntegrationTest.java`
with exactly eight `@Test` methods:

1. `LEARNING_EVENT` exposes Intake and Feeder requirements only.
2. Each dispatch constructs a fresh `IntakeToFeederCommand` child.
3. Real `CommandScheduler` dispatch starts both semantic requests.
4. Repeated `scheduler.run()` within one lifecycle does not recreate or
   reinitialize the child.
5. Cancellation/interruption reaches the child and stops Feeder then Intake.
6. Real scheduler resolves Intake requirement contention.
7. Real scheduler resolves Feeder requirement contention.
8. Supplier `RuntimeException` under nonempty Intake and Feeder
   requirements publishes `FACTORY_FAILURE`, returns safe no-op, and
   causes no mechanism actuation.

Update only `src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java`:
replace the stale L15 prohibition on any RobotContainer command reference
with an L16-aware autonomous-event expectation. Preserve its teleop,
default-command, and direct-subsystem-access guards. Preserve inherited
duplicate-registration, factory-failure, event-observation, marker,
chooser, AutoBuilder, Swerve-safety, and mechanism command tests. No
tests are edited at activation.

### Future verification and evidence boundary

After separate implementation authorization and independent static review,
the User runs focused tests and clean regression. Bounded Simulation
must start normally, attach Driver Station, show both mechanisms STOPPED
before Autonomous, select `ONE_METER_WITH_EVENT`, observe Intake and
Feeder semantic requests at `LEARNING_EVENT`, confirm scheduler validity,
observe Feeder then Intake STOPPED at path completion/interruption and
after Disabled, show no fatal scheduler exception, and exit successfully.
The `ONE_METER_PATH` control must not dispatch the mechanism event.
Noop adapters and RequestedState cannot establish physical acquisition,
transfer, motor performance, timing, sensor correctness, or hardware
behavior. Real hardware is DEFERRED. Intended eventual classification
is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, but
L16 has not earned it.

Independent activation review, implementation authorization, implementation,
static review, focused tests, clean regression, bounded Simulation,
documentation reconciliation, closure review, freeze reconciliation,
independent freeze review, and publication remain PENDING. M00_L16 is
the final M00 lesson; the roadmap is unchanged.

## Inherited M00_L15 lesson plan (historical copy)

The copied plan below describes M00_L15's prior design and publication
stage. Its then-current status and no-autonomous-binding rule are
historical, not M00_L16 instructions.

### Historical M00_L15 publication state and accepted evidence — 2026-09-27

M00_L15 is COMPLETE / FROZEN / READ-ONLY. Active M00 lesson count is 0; the
current active lesson is NONE. M00_L14 is COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED. M00_L16 is FUTURE / INACTIVE / NOT CREATED. The
untouched-copy baseline, Architecture / Inheritance Audit, Final
Design Lock, Controlled Activation, and Independent Activation Review are
accepted. Implementation Authorization and Implementation Handoff passed;
implementation and Final Independent Static Review are complete. The focused
test suite passed all 22 cases. The initial full clean regression's two
failures were diagnosed as inherited test-contract evolution and reconciled in
the active lesson's architecture tests. The final clean regression passed all
830 tests. Bounded Simulation / Driver Station verification passed within the
limits recorded below. Independent Closure Rereview passed as
`PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW`, with verdict
`M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION`
and no findings. Freeze Reconciliation is complete. Independent Freeze
Rereview passed as `PASS_M00_L15_INDEPENDENT_FREEZE_REREVIEW`, with verdict
`M00_L15_INDEPENDENT_FREEZE_REREVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
The primary snapshot is committed under `PASS_M00_L15_PRIMARY_SNAPSHOT_COMMIT`
at SHA `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`. Publication Metadata
Reconciliation is COMPLETE; Metadata Publication Commit 2, push, and final
publication verification remain pending. Evidence remains
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

## One new concept

Scheduler-managed coordination of the existing Intake and Feeder semantic
behaviors under one command lifecycle. Coordination is software intent only;
it introduces no sensor, physical-transfer, timing, or completion authority.

## Locked production design

The sole M00_L15 production addition is
`src/main/java/frc/robot/commands/IntakeToFeederCommand.java`.

```java
IntakeToFeederCommand(
    IntakeSubsystem intake,
    FeederSubsystem feeder
)
```

The constructor rejects null Intake and null Feeder, retains the exact supplied
subsystem references, requires exactly IntakeSubsystem and FeederSubsystem,
and performs no mechanism output or subsystem mutation. Flywheel, Elevator,
and Swerve are not requirements.

### Locked lifecycle

- `initialize()`: call `intake.requestIntake()` once, then
  `feeder.requestFeed()` once per scheduler lifecycle. The order is a
  deterministic software contract and makes no physical sequencing claim.
- `execute()`: issue no repeated mechanism requests; no timer or physical-state
  polling.
- `isFinished()`: return `false`.
- `end(false)` and `end(true)`: always attempt `feeder.stop()` then
  `intake.stop()`. Both paths use the same best-effort cleanup order.

### Locked exception policy

Catch RuntimeException only; do not catch Error. If the Intake request fails,
preserve it as primary, skip the Feeder request, then attempt Feeder stop and
Intake stop. If the Feeder request fails, preserve it as primary and attempt
both stops in that order. Suppress cleanup RuntimeExceptions on the primary in
occurrence order. End always attempts Feeder stop then Intake stop; if Feeder
stop fails it remains primary and an Intake cleanup failure is suppressed. If
Feeder stop succeeds and Intake stop fails, propagate the Intake exception.
Do not retry, fall back, or swallow the primary exception.

### Observation and contention boundaries

IntakeObservation and FeederObservation contain availability, connection, and
requested software state. Requested state does not prove motion, game-piece
presence, transfer, or completion. The command does not use observations as
authorization gates for its semantic requests. No beam break, sensor, timer,
delay, debounce, current or velocity threshold, or physical sequencing is
introduced.

WPILib CommandScheduler requirement ownership is the sole contention authority.
RunIntakeCommand owns Intake; RunFeederCommand owns Feeder; ShootCommand owns
Feeder and Flywheel. The new command owns Intake and Feeder. Do not add manual
conflict flags, scheduler polling, special ShootCommand detection, or
production cancellation logic.

RobotContainer receives no new M00_L15 binding. Preserve Right Bumper to
RunIntakeCommand and Left Bumper to RunFeederCommand. Add no autonomous
binding, NamedCommands registration, PathPlanner event, or event-marker
behavior.

## Locked focused-test matrix — 22 cases

The focused command tests are in
`src/test/java/frc/robot/commands/IntakeToFeederCommandTest.java`; the accepted
suite has 22 test methods and all 22 passed. The Feeder and Intake inherited
architecture-test copies were also reconciled within M00_L15. Frozen
predecessor tests remain unchanged.

### Direct-contract cases 1–18

1. Reject null Intake.
2. Reject null Feeder.
3. Prove exact Intake + Feeder requirement-set equality.
4. Prove construction produces no mechanism output.
5. Prove initialize requests Intake exactly once.
6. Prove initialize requests Feeder exactly once.
7. Prove execute does not repeat either request.
8. Prove isFinished returns false.
9. Prove direct end(false) stops Feeder then Intake.
10. Prove direct end(true) stops Feeder then Intake; this proves method
    semantics only, not scheduler cancellation.
11. Prove Intake request failure prevents Feeder request.
12. Prove Intake request failure still attempts both cleanup stops.
13. Prove Feeder request failure still attempts both cleanup stops.
14. Prove initialize failures preserve the primary exception and ordered
    cleanup suppression.
15. Prove Feeder-stop failure at end still attempts Intake stop.
16. Prove dual-stop failure preserves Feeder as primary and suppresses Intake
    failure.
17. Prove no Flywheel requirement.
18. Prove no Elevator requirement.

### Real CommandScheduler cases 19–22

19. Schedule the command through CommandScheduler; prove it starts, requests
    Intake and Feeder once, remains scheduled, and does not repeat requests
    across scheduler runs.
20. Cancel the scheduled command through CommandScheduler; prove it becomes
    unscheduled and scheduler-driven interrupted cleanup stops Feeder then
    Intake exactly once. Do not replace this with direct end(true).
21. Schedule an interruptible Intake-owning competitor; prove the scheduler
    interrupts the coordination command through shared Intake ownership,
    stops both coordinated semantics, and accepts the competitor.
22. Prove the symmetric scheduler arbitration and cleanup for a Feeder-owning
    competitor.

Scheduler tests must isolate CommandScheduler state and prevent scheduled
commands, subsystem/default-command registrations, or test ordering from
leaking between tests. This is test-fixture responsibility, not a production
change. The accepted passing cases establish command scheduling, cancellation
cleanup, and requirement contention. They do not establish physical mechanism
or game-piece behavior.

## Evidence limits and next gates

Runtime Intake and Feeder adapters are Noop and RobotContainer has no L15
binding. The accepted bounded Simulation did not execute
IntakeToFeederCommand. It verified application startup and Driver Station
attachment. In Disabled, Robot Enabled was No and Intake, Feeder, and Flywheel
RequestedState were STOPPED. In Teleoperated, Robot Enabled and DS Attached were
Yes and Intake and Feeder remained STOPPED; no L15 command started automatically.
On return to Disabled, Robot Enabled was No and Intake and Feeder remained
STOPPED, with no unexpected mechanism semantic state active. No runtime
exception or fatal scheduler error occurred; termination was BUILD SUCCESSFUL
with `SIMULATION_EXIT_CODE=0`. The unavailable joystick button warning on
unassigned or unplugged port 0 was EXPECTED / NON-BLOCKING. Focused scheduler
tests provide direct scheduler evidence. Evidence is THEORY VERIFIED /
SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Physical movement, game-piece
presence, transfer, completion, timing, sensor behavior, and hardware behavior
remain unverified.

Accepted clean-regression history: the initial run had 830 tests and two
failures. `PASS_M00_L15_CLEAN_REGRESSION_FAILURE_DIAGNOSIS` classified them as
EXPECTED_INHERITED_TEST_CONTRACT_EVOLUTION, not production defects.
`PASS_M00_L15_INHERITED_ARCHITECTURE_TEST_RECONCILIATION` updated only the
active lesson's inherited test copies. The Intake and Feeder filename guards
use normalized relative paths and closed sets. The Feeder owner guard uses the
JDK Java parser / syntax tree to apply Java lexical and Unicode semantics.
Architecture scan robustness repair, parser repair, and independent parser
review passed; no production change resulted. The exact closed Java source sets
are Intake-named `{RunIntakeCommand.java, IntakeToFeederCommand.java}`;
Feeder-named `{RunFeederCommand.java, IntakeToFeederCommand.java}`; and
FeederSubsystem command owners `{RunFeederCommand.java,
IntakeToFeederCommand.java, ShootCommand.java}`.

The final User clean regression passed: BUILD SUCCESSFUL, 830 tests, 830 PASS,
0 failures, 0 errors, 0 skipped, BUILD_EXIT_CODE=0. The accepted closure
rereview, Freeze Reconciliation, and Independent Freeze Rereview are complete.
The primary snapshot is committed at the accepted SHA recorded above, and
Publication Metadata Reconciliation is complete. M00_L15 is COMPLETE / FROZEN /
READ-ONLY / NOT PUBLISHED. Metadata Publication Commit 2, push, and final
publication verification remain pending. Active M00 lesson count is 0, the
current active lesson is NONE, M00_L16 remains FUTURE / INACTIVE / NOT CREATED,
and no M00_L17 is authorized.

---

## Inherited M00_L14 lesson plan (historical copy)

The following copied plan describes the frozen predecessor and is retained as
historical context only; it is not the M00_L15 design.

### Historical M00_L14 frozen lifecycle and publication state — 2026-09-26

M00_L14 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / NOT ACTIVE. The User
completed Metadata Publication Commit 2 and its amendment according to
accepted external evidence; its canonical identity is not embedded. Active
Lesson Count is 0 and Current Active M00 Lesson is NONE. Remote push remains
pending.
M00_L13 remains COMPLETE /
FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED.
Independent Activation Review returned
HOLD_M00_L14_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_RECONCILIATION_REQUIRED;
the bounded documentation repair and Independent Activation Re-review passed.
Implementation Authorization and Implementation Handoff also passed. Final
Independent Static Review, User focused tests, clean regression, and bounded
Simulation passed. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED. The initial Independent Closure Review HOLD was repaired by
PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION. Independent Closure Rereview
passed as PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW;
PASS_M00_L14_FREEZE_RECONCILIATION is complete. Independent Freeze Review passed as
PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW. The User completed the Primary Frozen
Snapshot as PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT at SHA
fa34556a3f1b7ef52b2c678a39c1083392d7c8d3. The accepted
HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA is resolved
by this actual documentation delta, which applies the M00_L13 Commit-2
publication semantics. The exact documentation reconciliation gate is
PASS_M00_L14_DOCUMENTATION_RECONCILIATION.

Accepted gates:
- PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD — 6 actionable tasks, 6 executed, BASELINE_BUILD_EXIT_CODE=0.
- PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT — 339/339 authored files inherited unchanged.
- PASS_M00_L14_FINAL_DESIGN_LOCK — FINAL_DESIGN_LOCK_PASS_READY_FOR_CONTROLLED_ACTIVATION.
- HOLD_M00_L14_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_RECONCILIATION_REQUIRED — preserved and resolved by bounded documentation repair.
- PASS_M00_L14_INDEPENDENT_ACTIVATION_REREVIEW.
- PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION and PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW.
- PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW.
- PASS_M00_L14_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 10s; 4 actionable tasks (3 executed, 1 up-to-date); FOCUSED_TEST_EXIT_CODE=0.
- PASS_M00_L14_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); CLEAN_REGRESSION_EXIT_CODE=0.
- PASS_M00_L14_USER_BOUNDED_SIMULATION; Disabled -> Teleoperated enabled -> Disabled; LAST_NATIVE_EXIT_CODE=0.
- HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED — documentation/evidence wording only; no production, architecture, runtime, test, or Simulation defect.
- PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION — bounded repair of LESSON_STATUS.md, LESSON_PLAN.md, and LESSON_CHECKLIST.md.
- PASS_M00_L14_DOCUMENTATION_RECONCILIATION — accepted documentation reconciliation.
- PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW — INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION.
- PASS_M00_L14_FREEZE_RECONCILIATION — completed Freeze Reconciliation.
- PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW — INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION.
- PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT — primary SHA fa34556a3f1b7ef52b2c678a39c1083392d7c8d3.
- PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION.
- PASS_M00_L14_METADATA_PUBLICATION_COMMIT — completed by accepted User evidence; metadata commit identity remains external.
- PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION and PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND — amendment completed by accepted User evidence; canonical identity remains external.

The two-commit Historical Snapshot model records the frozen lesson in a
Primary Frozen Snapshot commit and establishes publication with User-owned
metadata Commit 2.
Commit 2 is the publication point and records PUBLISHED. Accepted User
evidence establishes that Commit 2 and its amendment are completed; its
canonical identity remains external and is not embedded. Remote push remains
PENDING / USER-OWNED. The earlier
`HOLD_M00_L14_FINAL_PUBLICATION_VERIFICATION_METADATA_COMMIT_STATE_RECONCILIATION_REQUIRED`
was resolved by the accepted User amendment. The subsequent
`HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED`
identified stale current-state chronology and is addressed by this
documentation reconciliation. Final Publication Verification remains PENDING / EXTERNAL.
No third commit is created merely for that verification.

## Locked design — coordination logic only

Implement exactly frc.robot.commands.ShootCommand in frc.robot.commands.
It requires exactly FlywheelSubsystem and FeederSubsystem; it controls through
subsystem semantic APIs only and owns both requirements throughout its
scheduled lifetime.

Exact constructor signature and parameter order:

```java
ShootCommand(
    FlywheelSubsystem flywheel,
    FeederSubsystem feeder,
    double targetVelocityRpm
)
```

The constructor requires non-null Flywheel and Feeder subsystems and finite
targetVelocityRpm > 0.0. Invalid numeric input throws
IllegalArgumentException. Construction makes no mechanism output and mutates no
subsystem. targetVelocityRpm is caller-supplied semantic input, not a verified
real shooting RPM. No shooting or timing constant is added.

Readiness: consume only FlywheelObservation.readyAtSpeed(). Do not calculate
error/tolerance or add another readiness, validity, hysteresis, or timer
authority. Feeder admission requires available() && connected().
FeederObservation.requestedState() is software request state only and is not
physical transport proof. Do not create a command-local feedingRequested flag.

Successful initialize order: feeder.stop() once, then
flywheel.requestVelocity(targetVelocityRpm) once; do not feed or reissue that
Flywheel request from execute(). Execute:
when admission is true and state is not FEED_REQUESTED, request feed once;
when admission is false and state is FEED_REQUESTED, stop once; otherwise
issue no Feeder output. On normal readiness loss, Feeder stops and Flywheel
velocity intent remains active; later recovery may request feed.

The command never self-finishes. No Timer, timeout, feed duration, shot count,
or completion sensor is permitted. Every end(false) and end(true) attempts
Feeder stop then Flywheel stop, even if the first throws; if both throw,
Feeder remains primary and Flywheel is suppressed. If initial Feeder stop
throws in initialize(), do not request Flywheel velocity; attempt Flywheel
stop once. If Flywheel velocity request throws, do not repeat the completed
Feeder baseline stop; attempt Flywheel stop once. If Feeder requestFeed throws
in execute(), attempt Feeder stop once and Flywheel stop once even if Feeder
cleanup throws. If Feeder stop throws during readiness/admission loss in
execute(), do not retry Feeder stop in that call; attempt Flywheel stop once.
In each case, cleanup RuntimeExceptions are suppressed on the original
RuntimeException, which remains primary and is rethrown. No retries, silent
recovery, clamp, rewrite, fallback RPM, or routine Error catch.

## Scope and expected delta

Actual production delta: add only src/main/java/frc/robot/commands/ShootCommand.java. Constants.java,
RobotContainer.java, subsystems, IO, Observations, telemetry, vendor adapters,
and deploy files stay unchanged. There is no L14 binding. Elevator, Intake,
Swerve, vision, and autonomous mechanisms are excluded. There is no L14 driver
binding; the inherited Left Bumper manual Feeder command remains, with
scheduler requirements providing Feeder mutual exclusion. M00_L15 owns
Intake-to-Feeder Coordination; M00_L16 owns Mechanism Autonomous Event
Integration.

## Remaining workflow gates

The implementation, accepted verification, closure rereview, Freeze
Reconciliation, Independent Freeze Review, User-owned Primary Frozen Snapshot,
and User-owned Metadata Publication Commit 2 are complete. Remote push and
Final Publication Verification remain pending. Real hardware remains deferred.

## Accepted focused-test proof plan — 18 @Test methods

The 18-method focused suite passed under PASS_M00_L14_USER_FOCUSED_TESTS. The
cases below record its intended proof coverage; arbitrary RPM values remain
TEST DATA ONLY and do not establish a physical shooting RPM.

### Constructor and ownership

- Prove the exact production class identity is
  `frc.robot.commands.ShootCommand` and the constructor signature and parameter
  order are exactly the signature recorded above.
- Prove null Flywheel and null Feeder inputs are rejected.
- Prove non-finite RPM, zero RPM, and negative RPM each throw
  `IllegalArgumentException`.
- Prove invalid construction produces no mechanism output and no subsystem
  mutation.
- Prove the command requires FlywheelSubsystem and FeederSubsystem, and does
  not require ElevatorSubsystem or IntakeSubsystem.

### Successful initialization and normal coordination

- Prove successful `initialize()` calls `feeder.stop()` exactly once and
  `flywheel.requestVelocity(targetVelocityRpm)` exactly once.
- Prove `targetVelocityRpm` is passed through unchanged, `initialize()` never
  calls `feeder.requestFeed()`, and `execute()` never reissues
  `flywheel.requestVelocity()`.
- Prove an unready Flywheel, unavailable Feeder, or disconnected Feeder each
  prevents a feed request; ready Flywheel plus available and connected Feeder
  requests feed.
- Prove unchanged ready state does not reissue `requestFeed()`.
- Prove readiness loss while feeding stops Feeder, and unchanged stopped/unready
  state does not reissue `stop()` while normal Flywheel velocity intent remains
  active.
- Prove readiness recovery may request feed again on the semantic transition.
- Prove Feeder availability loss and Feeder connection loss during feeding each
  stop Feeder.
- Prove `FeederObservation.requestedState()` is used only as software request
  state and `FEED_REQUESTED` is never treated as physical game-piece movement.
- Prove the command does not self-finish (`isFinished() == false`).

### `initialize()` failure cases

- If initial `feeder.stop()` throws, prove the original Feeder failure
  propagates, Flywheel velocity is not requested, Flywheel stop cleanup is
  attempted once, cleanup failure is suppressed on the original, and Feeder
  stop is not retried in that failing call.
- If `flywheel.requestVelocity()` throws after the baseline Feeder stop,
  prove the original request failure propagates, the baseline stop already
  occurred, Flywheel stop cleanup is attempted once, cleanup failure is
  suppressed on the original, velocity is not retried, and initialization does
  not continue normally.

### `requestFeed()` failure case

- Prove the original `requestFeed()` RuntimeException remains primary; Feeder
  stop and Flywheel stop cleanup are each attempted once; Flywheel cleanup is
  still attempted if Feeder cleanup throws; cleanup RuntimeExceptions are
  suppressed on the original; and `requestFeed()` is not retried.

### Readiness/admission transition `feeder.stop()` failure case

- Prove the original Feeder stop failure remains primary, Feeder stop is not
  retried in that same failing `execute()`, Flywheel stop cleanup is attempted
  once, cleanup failure is suppressed on the original, and cleanup does not
  depend on a later scheduler cycle.

### Terminal `end()` failure matrix

For both `end(false)` and `end(true)` where repository test architecture
supports direct lifecycle proof, prove both Feeder and Flywheel stops are
attempted in every case:

1. Feeder stop succeeds and Flywheel stop succeeds: `end()` returns normally.
2. Feeder stop throws and Flywheel stop succeeds: Feeder failure is rethrown.
3. Feeder stop succeeds and Flywheel stop throws: Flywheel failure is rethrown.
4. Both stops throw: Feeder failure remains primary, Flywheel failure is
   suppressed, and Feeder failure is rethrown.

The focused tests invoke `end(true)` directly and unit-test interrupted-end
cleanup semantics, including the applicable stop-failure behavior. They do
not schedule ShootCommand through CommandScheduler or cancel/interruption-drive
it through the scheduler; scheduler-driven integration is not proven by these
tests. Prove no retry loop and primary RuntimeException preservation with
suppressed cleanup failures for all locked exception paths.

### Named architecture and scope exclusion guards

Require test or architecture-guard proof that the implementation has:

- no alternate `ShootCoordinationCommand` production class;
- no duplicate Flywheel readiness calculation, command-local Flywheel
  tolerance, or command-local readiness timer;
- no command-local `feedingRequested` authority;
- no direct FlywheelIO or FeederIO access and no vendor API access;
- no telemetry ownership and no hardware construction;
- no Elevator, Intake, or Swerve dependency;
- no new Constants shooting RPM, spin-up timeout, feed-duration, or shot-duration
  authority; no Elevator shooting position, Feeder speed, or hardware calibration
  authority;
- no clamp, rewrite, or fallback of the caller-supplied target;
- no RobotContainer L14 binding;
- no NamedCommands integration or PathPlanner mechanism events;
- no M00_L15 Intake-to-Feeder coordination leakage; and
- no M00_L16 autonomous event integration leakage.

The bounded Simulation verified Disabled startup (Robot Disabled, not enabled,
DS attached, not E-stopped), Teleoperated enable (Robot Teleoperated, enabled,
DS attached, not E-stopped), and return to Disabled (not enabled, DS attached,
not E-stopped) with mechanism requested states STOPPED. No application crash
or scheduler/runtime exception was observed; normal shutdown completed with
LAST_NATIVE_EXIT_CODE=0. Since RobotContainer has no ShootCommand binding,
Simulation did not directly execute ShootCommand. It verifies application
startup, mode transitions, scheduler and integration stability, safe semantic
state, and clean exit only. No physical shot is claimed. Real hardware remains
deferred.
