# ADR — M00_L16 P3-H01 Configuration Authority Exceptional Repair

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
- Governing repair record: the existing exceptional-repair ADR; original registration and reconciliation
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
- Governing amendment: the existing exceptional-repair ADR; preserved registration in Section 16 and
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

## Preserved original P3-H01 decision — 2026-09-28

- Decision status: APPROVED for governance registration / exceptional bounded repair workflow.
- Date: 2026-09-28.
- Decision owners: Architect and User.
- Change type: exceptional post-freeze architecture/configuration-authority repair.
- Production/test implementation: NOT AUTHORIZED.
- Status: IN_PROGRESS.
- Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Repository Active Lesson Count: 1.
- Active M00 Lesson Count: 1.
- Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- Reopen reason: P3-H01 post-freeze architecture/configuration-authority defect.
- Accepted design gate: `PASS_P3_H01_M00_L16_EXCEPTIONAL_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.

## 1. Authority and approval

The supplied Architect/Design Authority instruction approves this governance
package and exceptional registration; the User's explicit registration request
authorizes these documentation/lifecycle changes. Approval covers this ADR and
exactly the eight existing documents in Section 7, and makes M00_L16 the sole
reopened editable lesson. It authorizes no Java or test implementation.

This ADR supplements AGENTS Sections 8 and 14, Documents A/B/C, and
[the M00 roadmap](ADR_M00_Competition_Mechanism_Foundations_Roadmap.md).
The authority order and English-PDF authority remain unchanged. The
[A01_L08 exceptional-reopen ADR](ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md)
and [V00_L07 inherited-defect repair ADR](ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md)
establish the governed mechanism and preservation model; their earlier approvals
do not authorize P3-H01.

The existing rule permits a material architecture defect discovered after freeze.
P3-H01 invalidates the frozen assumption that operational defaults comply with
Constants authority and the no-magic-number rule. Ordinary frozen protection
would prohibit source edits; this narrow exception preserves that protection
for every other lesson and requires separate implementation authorization.

## 2. Trigger and exact defect

Repository-Wide Audit Phase 3 discovered P3-H01 after M00_L16 had already been
frozen, published, and externally verified. Operational Vision qualification
defaults are literal Policy arguments 1.0, 2.0, 3.0 and literal maximum freshness
0.250 in RobotContainer rather than configuration owned by Constants.

These values govern runtime admission. RobotContainer constructing/injecting the
existing Policy is valid composition. Numerical incorrectness, a fusion failure,
and a hardware safety failure are not established. This repair changes default
configuration ownership only.

Affected surviving source: V00_L09 and M00_L01-M00_L16, seventeen projects.
The earliest supported surviving boundary is V00_L08 -> V00_L09; the exact
historical first introduction is not independently established. This is an
inherited defect, not seventeen independently established introductions.

## 3. Historical preservation and selected correction role

V00_L09 and M00_L01-M00_L15 remain unchanged historical source, including their
P3-H01 finding and existing publication/evidence qualifications. No predecessor
source, test, document, configuration, dependency, or asset edit is authorized.

M00_L16 alone is the correction working lesson. Its original published snapshot
remains immutable historical evidence. A successfully verified, re-frozen and
published repaired M00_L16 may become the canonical curriculum/audit correction
snapshot for this inherited defect. This designation does not establish a current
hardware deployment. No predecessor mass edit, new module, M00_L17, or new
curriculum concept is authorized; M00's sixteen-lesson roadmap remains closed.

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

## 4. Exact future production boundary

All paths below are relative to the existing lesson:
`real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration`.

| Existing production file | Sole proposed future change |
| --- | --- |
| `src/main/java/frc/robot/Constants.java` | Add four named defaults in existing VisionConstants and narrowly clarify its JavaDoc. |
| `src/main/java/frc/robot/RobotContainer.java` | Replace the three Policy literals and one freshness literal with the corresponding named constants. |

Exactly two production files, zero new production files, and no other production
modification are selected. These are registered future boundaries, not present
implementation permission. Any need for a third production file triggers HOLD.

## 5. Locked defaults, ownership and unchanged semantics

All four fields belong in existing Constants.VisionConstants as public static
final primitive doubles, matching existing constructor and repository conventions.

| Name | Exact value | Units/meaning |
| --- | --- | --- |
| kLowUncertaintyMaxDistanceMeters | 1.0 | meters; inclusive LOW upper boundary |
| kMediumUncertaintyMaxDistanceMeters | 2.0 | meters; inclusive MEDIUM upper boundary |
| kMaximumAcceptedDistanceMeters | 3.0 | meters; inclusive maximum accepted distance |
| kMaximumFreshAgeSeconds | 0.250 | seconds; inclusive maximum fresh age |

Constants owns default values. RobotContainer continues constructing the existing
VisionMeasurementQualityEvaluator.Policy from the three distance values and
injecting the fourth value separately into the same VisionSubsystem constructor.
The argument order and Timer::getFPGATimestamp supplier remain unchanged.
Do not instantiate Policy in Constants or introduce a units abstraction migration.

VisionSubsystem consumes supplied configuration. Policy retains its existing
immutable record type/owner and validation. Quality/timing evaluators remain pure,
stateless, deterministic, and explicitly parameterized; they must not obtain
configuration from Constants, RobotContainer, clocks, or runtime globals.
The existing inclusive comparisons, branch priority, distance norm, timing basis,
latency subtraction, ordering, rejection, and qualification semantics remain intact.
No tuning, calibration or additional production validation is part of relocation.

## 6. Exact future test boundary

One new proposed file, not created by this registration:
`src/test/java/frc/robot/VisionConfigurationAuthorityTest.java`.

Zero existing test modifications are permitted. A later implementation approval
must name the new file explicitly. Its bounded obligations are:

1. Independent expected values for the four defaults, declaration shape,
   finite/nonnegative values, and distance ordering.
2. Production RobotContainer constructor references to those named defaults in
   the correct positions; comments or strings cannot satisfy source-origin proof.
3. Actual injected Policy/freshness from a real RobotContainer test fixture,
   using existing test-only reflection/cleanup conventions; no production getters
   or dependencies added for testing.
4. Unchanged behavior at and around 1.0/2.0/3.0 m and below/equal/above 0.250 s,
   with inclusive limits and deterministic time fixtures.
5. Preserved evaluator dependency direction and explicit-parameter interpretation.

Runtime value equality alone does not prove configuration origin. Combine the
source-origin guard, independent numeric oracles, and actual composition check.
Keep existing arbitrary explicit test policies and independent literal fixtures.

Rerun existing quality, timing, VisionSubsystem, VisionIOSimHarness,
VisionFusionCoordinator, mechanism, scheduler, autonomous, Swerve and event
regressions unchanged, including IntakeArchitectureBoundaryTest and the eight
RobotContainerMechanismAutonomousEventIntegrationTest cases. No guard weakening,
new test dependency, or general test refactor is authorized.

## 7. Exact governance-registration document boundary

Create only this ADR. Modify only:

1. AGENTS.md.
2. Repository README.md.
3. docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md.
4. real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/README.md.
5. real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/LESSON_STATUS.md.
6. real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/LESSON_PLAN.md.
7. real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/LESSON_CHECKLIST.md.
8. real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration/docs/M00_L15_to_M00_L16_Step_by_Step.md.

Add dated current repair records and a future repair appendix, preserving original
lesson history and transition steps. This supplement applies only to the exact
future Constants/RobotContainer boundary, subject to separate implementation
approval; it narrowly supplements the original L16 Constants/Vision exclusion.
It does not change the Frozen Backbone, interface contracts, global standards,
roadmap identity/order, or the original mechanism-event concept.

Future evidence reconciliation or publication metadata writing requires its own
explicit documentation scope; this registration is not unrestricted permission
to edit these documents throughout the repair.

## 8. Protected production and excluded scope

No production edit to VisionSubsystem.java, VisionMeasurementQualityEvaluator.java,
VisionTimingEvaluator.java, their algorithms or Policy type/ownership.
No estimator ownership or fusion change, telemetry redesign, PathPlanner/event
change, mechanism behavior change, controller mapping change, CAN/drivetrain/
hardware configuration change, dependency/vendordep/Gradle change, asset edit,
new abstraction, new module, or M00_L17. All other production/tests remain protected.

The existing LEARNING_EVENT dispatch, fresh IntakeToFeederCommand child,
Intake/Feeder requirements, teleop bindings, chooser and paths remain intact.
Normal cleanup stops Feeder then Intake. No guarantee after arbitrary uncaught
library exceptions, no timer/timeout/wrapper, and no telemetry expansion is added.

## 9. Current lifecycle and fresh pre-implementation gates

Original published snapshot -> explicitly registered IN_PROGRESS lesson with
Active State REOPENED / IN_PROGRESS / EDITABLE. REOPENED is provenance, not a
new generic status. Repository active count is exactly 1; all other lessons
remain read-only. No new copy, rename, cleanup, restore, or reconstruction is
authorized. The working repair derives from the original published L16.

The fresh User-owned Java 17 original-source/provenance baseline, inherited
focused/full-suite baseline, clean build, and changed-file isolation evidence
are PENDING. A fresh baseline/scope architecture review and exact implementation
authorization are PENDING. The accepted authorization design does not replace
these later gates. No original PASS is reused as fresh repair evidence.

Implementation authorization must separately name the exact two production files
and one new test. It may not be inferred from governance approval or editability.
P3-H01 remains present in unchanged source; implementation is NOT STARTED.

## 10. Fresh verification requirements — all PENDING

| Gate | Classification | Required future evidence |
| --- | --- | --- |
| Focused unit tests | REQUIRED | Independent defaults and inclusive distance/freshness proof |
| Architecture/configuration guard | REQUIRED | Source origin, actual injected configuration, pure evaluator dependencies |
| Full inherited suite | REQUIRED | Fresh complete repair-project regression, including mechanisms/autonomous |
| Clean Gradle build | REQUIRED | Fresh User-owned Java 17 project result |
| WPILib Simulation | REQUIRED | Bounded production-composition and integrated runtime re-verification |
| Driver Station | REQUIRED | Attachment/mode transitions and Disabled cleanup in Simulation |
| Glass | APPLICABILITY_DECISION_REQUIRED | Explicit Architect/User disposition and applicable observation evidence |
| Real robot | APPLICABILITY_DECISION_REQUIRED | Explicit Architect/User disposition and applicable bounded hardware evidence |

Simulation must cover startup, existing valid-frame qualification/fusion and
unavailable/stale handling, existing event-path and event-free controls,
Disabled cleanup, no observed fatal runtime exception, and normal exit.
Use existing fixtures and observation contracts only. Unit tests are not runtime
Simulation evidence. Noop output and Simulation do not prove physical performance.

Unchanged values do not automatically require recalibration. The ownership-only
repair does not establish a hardware defect. Nevertheless, neither historic
hardware evidence nor prior deferral silently satisfies fresh applicability.
Record an explicit applicable requirement or justified NOT APPLICABLE/approved
DEFERRED disposition with scope and reason. An unresolved applicability decision,
or an applicable failed/missing verification gate, blocks closure/re-freeze.

Changed-file review, unchanged protected/predecessor evidence, final architecture
review, documentation reconciliation, and independent closure review are REQUIRED
and PENDING. Every repair verification claim must cite new accepted evidence;
counts, exit codes, physical conclusions, and remote identities must not be invented.

## 11. Re-freeze requirements

After exact implementation authorization, re-freeze requires accepted scoped
implementation; static removal of P3-H01; fresh focused/guard, full-suite,
clean-build, Simulation and Driver Station PASS; explicit Glass/hardware decisions
and applicable evidence; changed-file/provenance review; final architecture and
independent closure PASS; finalized repair documentation/appendix; then explicit
Architect/User re-freeze approval and independent freeze review.

Verification completion is a milestone, not a new lifecycle status. Until
re-freeze, Status remains IN_PROGRESS. After explicit re-freeze: COMPLETE /
FROZEN / READ-ONLY; active count 0; repaired publication still pending.
Original publication remains historical throughout. No required failed/missing
gate may be overridden by the old lesson closure.

## 12. New repaired publication and supersession

Only after fresh verification and explicit re-freeze may the User create a new
primary repaired frozen-snapshot identity. Reconcile authorized repair metadata,
then the User creates a separate new metadata identity and pushes. Obtain external
final publication verification of the new identities and exact scope.

Use the established two-commit Historical Snapshot model; no third verification-only
commit is required and no commit embeds its own future hash. Preserve the original
primary/metadata identities, original evidence scope, this authorization, exact
repair delta, fresh verification and re-freeze gates, and new publication linkage.

Original published snapshot -> exceptional authorization -> bounded working repair
-> fresh verification -> new repaired frozen snapshot -> new repair metadata
-> User-owned push -> external final publication verification.

Supersession is effective only for the approved canonical curriculum/audit
correction role after repaired publication verification. It neither erases earlier
findings/publications nor certifies unchanged predecessor code as corrected.
New primary, metadata, push and external-verification gates are all PENDING.

## 13. Phase-3 resumption and prior-audit preservation

Phase 2 remains `PASS_REPOSITORY_WIDE_AUDIT_PHASE_2_PHYSICAL_LINEAGE_WITH_RECORDED_LIMITS`.
Phase 3 remains `HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
Phase 4 is NOT STARTED / FORBIDDEN.

Governance registration alone does not remove the source conflict or resume
Phase 3. Separate audit authorization requires accepted target/preservation
governance and reviewed implementation/static removal in the correction target.
A read-only static audit may later inspect a reviewed working candidate before
publication, reporting its actual lifecycle. Static removal cannot claim fresh
verification, re-freeze, or repaired publication. COMPLETE/FROZEN claims require
closure/re-freeze; PUBLISHED/VERIFIED supersession claims require new publication.

P3-H01 correction does not complete the remaining Architecture Consistency Matrix.
Final Phase-3 PASS requires its remaining scope and absence of uncontained
blocking architecture findings, with all claimed states sufficiently evidenced.
No Phase-4 permission follows from this registration.

D2A/H01, historical R1, A01_L07 and their accepted correction/containment boundaries,
and historical-byte limitations remain unchanged. Phase-2 counts remain
65 PASS / 8 WARNING / 2 HOLD / 2 FAIL / 0 UNRESOLVED.

## 14. HOLD, rollback and protection

STOP/HOLD and return for governance review if the repair requires a third
production file, an existing test edit, numerical behavior change, architecture
expansion, or another lesson. Also HOLD on baseline/build/verification failure,
unauthorized file drift, a second editable lesson, missing applicability decision,
or missing closure/re-freeze/publication evidence needed for the claimed gate.

Do not silently broaden or revert scope. The Architect/User must explicitly
decide any revision, rollback, or abandonment. Preserve original history and
all repair evidence. No cleanup, restore, move, delete, branch or history operation
is authorized.

Git, Gradle, tests, build, Simulation, Glass, Driver Station and real-hardware
execution remain User-owned. This governance registration performs none of them.
Authoritative PDFs/mirrors/manifest and unrelated protected files remain unchanged.

github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED

## 15. Registration disposition

APPROVED for governance registration and the bounded exceptional repair workflow
only. M00_L16 is the sole REOPENED / IN_PROGRESS / EDITABLE lesson.
Implementation Authorization is PENDING / NOT AUTHORIZED; implementation,
fresh execution verification, re-freeze, repaired publication and audit resumption
are not claimed. The next permitted decision is separate implementation
authorization after the required fresh baseline/review gates.


## 16. CFG-H01 governance amendment — 2026-09-29

Historical registration/design-stage record: the then-pending implementation
and verification entries below retain their original planning meaning.
Current accepted implementation, User verification, independent review and
documentation reconciliation are controlled by the current summary and Section 17.

### 16.1 Reason, authority and decision

A repository-wide configuration-authority audit discovered CFG-H01 after the
P3-H01 correction and fresh User verification. The real camera's NetworkTables
table identity is robot/device configuration, currently privately owned by
VisionIOLimelight. Correct ownership is Constants.VisionConstants; the adapter
continues owning NetworkTables acquisition, schema/protocol, parsing and session
implementation. The `"json"` schema field and other protocol constants remain local.

Decision: APPROVED for this documentation-only governance registration under
AGENTS Sections 8 and 14, based on the already reviewed bounded design
`PASS_CFG_H01_BOUNDED_REPAIR_DESIGN_READY_FOR_AUTHORIZATION`.
The Architect/User request authorizes amendment of this existing ADR and the
eight other existing documents below. It does not authorize Java or tests.
No new ADR is created. English PDFs retain authority; standards, interfaces and
Frozen Backbone are unchanged.

### 16.2 Stage 1 — original P3-H01 decision and accepted evidence

The original 2026-09-28 decision and Sections 1-15 remain byte-preserved as
historical approval/planning evidence. Its original authorization boundary was
Constants.java, RobotContainer.java and one then-new
VisionConfigurationAuthorityTest.java. Subsequent implementation is complete;
the four default values 1.0/2.0/3.0/0.250 and inclusive semantics remain correct.

Current supplied User evidence: focused test PASS, full suite PASS, clean build
PASS, fresh Simulation PASS, Vision qualification/fusion PASS, event-path
runtime PASS, LEARNING_EVENT Intake/Feeder semantic behavior PASS, cleanup PASS,
consumed-readiness fail-closed PASS and normal Simulation exit PASS.
This evidence is preserved as fresh P3-H01 evidence, with no invented counts,
exit codes or physical conclusions.

Localization initialization must precede expected accepted Vision fusion.
Successful event evidence is `/Intake/RequestedState` and
`/Feeder/RequestedState`, not successful AutonomousEvent lifecycle telemetry.
Do not repeat or reopen P3-H01, downgrade this evidence, or recast it as fresh
post-CFG-H01 execution. Separate earlier applicability and closure decisions
are not silently satisfied by CFG-H01's dispositions.

### 16.3 Stage 2 — inherited finding and exact future scope

CFG-H01 is ONE inherited MISPLACED_CONFIGURATION finding across V00_L08,
V00_L09 and M00_L01-M00_L16, eighteen surviving lessons. V00_L08 is the earliest
surviving occurrence; this does not invent a stronger historical first-introduction
claim. No endpoint error, runtime failure or hardware safety failure is established.

All future code paths below are relative to existing
`real_robot_programming/module_M00/M00_L16_MechanismAutonomousEventIntegration`.

1. Existing production `src/main/java/frc/robot/Constants.java`:
   add exactly `public static final String kLimelightTableName = "limelight";`
   at the beginning of existing VisionConstants, before calibration fields;
   narrow class JavaDoc if needed. Preserve all current declarations and values,
   including the four completed P3-H01 defaults.
2. Existing production `src/main/java/frc/robot/io/vision/VisionIOLimelight.java`:
   import Constants; remove private adapter-owned kLimelightTableName;
   public default constructor uses
   `this(NetworkTableInstance.getDefault().getTable(Constants.VisionConstants.kLimelightTableName));`.
   Preserve the public default and package-private injected NetworkTable constructors.
   Preserve every protocol constant, parser, validation, timing, frame/session,
   source boundary and Observation behavior.
3. Existing test `src/test/java/frc/robot/VisionConfigurationAuthorityTest.java`:
   extend only this existing file. No new production or test file.

RobotContainer.java and VisionIOLimelightTest.java are protected in this stage.
The original P3-H01 authorization did NOT silently cover VisionIOLimelight or
later extension of an existing test. This amendment explicitly registers the
future incremental exception to the original adapter protection/test boundary.
Separate implementation authorization is still required.

Cumulatively, the two stages involve Constants.java, RobotContainer.java and
VisionIOLimelight.java; the incremental CFG-H01 scope is only Constants plus
the adapter. The original P3-H01 two-production-file limit remains the original
stage limit. It is not blanket permission for a third CFG-H01 production file.

### 16.4 Exact future test obligations

- Reflection proves public/static/final/String declaration and independent
  expected `"limelight"`, rather than a self-derived expected value.
- Reuse the existing JDK AST source helper to prove executable public default
  constructor consumption of the named Constants field and removal of the
  private endpoint default. Comments or strings cannot satisfy source-origin proof.
- Construct the real public default adapter and use existing test-only reflection
  conventions to inspect private jsonEntry and assert independent
  `/limelight/json`; no production test accessors or new dependencies.
- Unchanged VisionIOLimelight tests retain injected-constructor and parser/session
  coverage. Preserve all existing P3-H01 guards, arbitrary policy fixtures,
  numeric independent oracles and evaluator dependency checks.

### 16.5 Impact and protected boundaries

Only configuration ownership changes; endpoint stays exactly `"limelight"`.
No Constants reorganization or decorative headings, CAN IDs, mechanism CAN IDs,
Vision thresholds/calibration, RobotContainer composition, VisionIO contract,
VisionSubsystem, evaluators, estimator/fusion, telemetry, controller mappings,
PathPlanner/events, mechanism behavior, simulation fixtures, dependencies,
Gradle/vendordeps or deploy assets are changed by CFG-H01.

V00_L08/V00_L09/M00_L01-M00_L15 and all other lessons remain untouched historical
snapshots. No mass correction, new curriculum concept, roadmap change or
M00_L17 is authorized. Limelight IO responsibilities stay within the existing
approved vendor boundary; no new global IO/NetworkTables exception is created.

### 16.6 Approved post-CFG-H01 verification applicability

Required, all PENDING until fresh post-implementation evidence:

- focused VisionConfigurationAuthorityTest;
- unchanged VisionIOLimelight tests;
- relevant inherited Vision regressions;
- full test suite;
- clean build;
- independent exact-delta/static review, including source-origin removal,
  constructor preservation and protected/historical-file isolation.

Fresh Simulation: NOT REQUIRED. The Simulation branch selects VisionIOSim and
does not exercise real Limelight adapter endpoint ownership.
Glass: NOT APPLICABLE. Driver Station: NOT APPLICABLE.
Real hardware: NOT REQUIRED FOR CFG-H01 REPAIR CLOSURE, because the endpoint
value remains `"limelight"` and deployed endpoint behavior is unchanged.

These are explicit Architect/User approved design dispositions, not inferred
waivers. They apply to CFG-H01 alone. Preserve P3-H01 fresh Simulation PASS,
original REAL HARDWARE DEFERRED and any separate earlier applicability/closure
requirements. Do not claim post-CFG-H01 tests/builds or runtime verification now.

### 16.7 Exactly nine existing registration documents

1. `AGENTS.md`.
2. Root `README.md`.
3. This existing P3-H01 ADR.
4. `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`.
5. L16 `README.md`.
6. L16 `LESSON_STATUS.md`.
7. L16 `LESSON_PLAN.md`.
8. L16 `LESSON_CHECKLIST.md`.
9. L16 `docs/M00_L15_to_M00_L16_Step_by_Step.md`.

The guide's original steps and existing material are byte-preserved; a dated
CFG-H01 appendix follows all existing material. Original P3-H01 decision text
is retained, with this dated amendment distinguishing the stages.
PDFs, mirrors, manifest and unrelated documentation remain protected.
Future evidence/finalization/metadata updates require their own explicit scope.

### 16.8 Lifecycle, closure and publication

M00_L16 remains the sole REOPENED / IN_PROGRESS / EDITABLE lesson; repository
active count and active M00 count remain 1. P3-H01 is IMPLEMENTED / FRESHLY
VERIFIED / NOT REOPENED. CFG-H01 is GOVERNANCE REGISTERED / DESIGN APPROVED /
IMPLEMENTATION AUTHORIZATION PENDING / NOT AUTHORIZED; implementation NOT STARTED.

Independent registration review and separate exact-file implementation
authorization precede implementation. Future required automated evidence and
independent static removal review precede authorized documentation finalization,
final architecture and independent closure review. Resolve any separate earlier
outstanding applicability/closure requirement explicitly. Then obtain explicit
Architect/User re-freeze approval and independent freeze review. No current
COMPLETE/FROZEN, final transition-guide PASS, repaired publication or closure
PASS is recorded.

Preserve original primary `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
`3667290180fe1a9fd96265383e7412c142c18129` and accepted original
`PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION` as historical fact.
After authorized re-freeze, use the existing User-owned new-primary/new-metadata
two-commit model, push, and independent external verification; no third
verification-only commit, rewritten original identity, or repaired publication
claim now. No old PASS becomes fresh CFG-H01 evidence.

Phase 2 remains PASS with recorded limits; Phase 3 remains
`HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`; Phase 4 remains
NOT STARTED / FORBIDDEN. Reviewed static removal and separate authorization
remain necessary for Phase-3 resumption. Prior D2A/H01/R1/A01_L07 qualifications
and historical-byte limits remain unchanged.

### 16.9 HOLD and next authorization gate

STOP/HOLD for a third CFG-H01 production file, second test file, new production
or test file, changed endpoint or numerical behavior, architecture expansion,
another lesson, unauthorized drift, failed required verification or missing
evidence for a claimed closure/freeze/publication gate. Return for explicit
governance review; no automatic rollback or scope expansion.

The next gate is independent registration review followed by separate
Architect/User implementation authorization naming the exact two existing
production files and one existing test. This registration executes no Git,
Gradle, project tests/build, Simulation, Glass, Driver Station or hardware.

github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## 17. Post-implementation documentation/evidence reconciliation — 2026-09-29

Historical pre-final-review reconciliation record: Section 18 and the current
re-freeze record above control the accepted closure and frozen candidate. All
then-current/pending/editable statements in this section retain historical meaning.

### 17.1 Authority and completed chronology

The Architect/User explicitly authorized this documentation-only reconciliation
in the same nine existing records listed in Section 16.7. No new ADR, Java/test
change, project execution, Git, re-freeze, publication or audit resumption is
authorized. Section 16 preserves the original registration/design planning;
the current record above and this section supersede its then-pending stage status.

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

### 17.2 Accepted evidence and exact preservation

P3-H01 is IMPLEMENTED / FRESHLY VERIFIED / INDEPENDENTLY REVIEWED / PRESERVED /
NOT REOPENED. Constants owns 1.0/2.0/3.0/0.250 and RobotContainer injects them.
The supplied focused/full/clean-build and complete bounded Simulation evidence,
including localization before accepted fusion and Intake/Feeder requested-state
event semantics, is recorded in the current evidence section above.

CFG-H01 is IMPLEMENTED / VERIFIED / REMOVED. Constants.VisionConstants uniquely
owns the public static final String kLimelightTableName = "limelight";
VisionIOLimelight's existing default constructor consumes that authority.
The private endpoint default is removed; /limelight/json, both constructors,
protocol/parsing/validation/timing/session/frame/geometry/observations are preserved.
The independent review confirmed exactly the two authorized existing production
files and the existing authority-test extension, with prior P3-H01 tests/helpers,
RobotContainer, VisionIO, unchanged adapter tests and historical lessons preserved.

All five required fresh User automated gates PASS: VisionConfigurationAuthorityTest,
VisionIOLimelightTest, Vision regression suite, full suite and clean build.
The final User output was BUILD SUCCESSFUL in 32s, 7 actionable tasks: 7 executed,
CFG-H01 AUTOMATED USER VERIFICATION COMPLETE. No numeric test count or exit code
is inferred. Accepted independent Sol review:
`PASS_CFG_H01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.

### 17.3 Applicability and closure boundary

CFG-H01 fresh Simulation is NOT REQUIRED because Simulation uses VisionIOSim;
Glass and Driver Station are NOT APPLICABLE; real hardware is NOT REQUIRED FOR
CFG-H01 REPAIR CLOSURE because the same limelight endpoint behavior is preserved.
These decisions do not replace the separate P3-H01 Simulation PASS or silently
satisfy any separate earlier applicability/closure requirement.

Documentation/evidence is RECONCILED. Next gate:
INDEPENDENT FINAL ARCHITECTURE / CLOSURE REVIEW. M00_L16 remains the sole
REOPENED / IN_PROGRESS / EDITABLE lesson, active count 1. No final closure,
re-freeze, freeze-review, final repair Transition Guide acceptance or repaired
publication PASS is claimed. Original primary ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11,
metadata 3667290180fe1a9fd96265383e7412c142c18129 and original final publication
gate remain historical truth. Phase 3 remains HOLD; Phase 4 remains FORBIDDEN.
No M00_L17. Protected unrelated A01_L06 path state remains untouched.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## 18. Final independent closure and authorized re-freeze — 2026-09-29

Historical closure/re-freeze-stage record: its pending primary-snapshot and
freeze-review entries describe that earlier stage. Section 19 and the current
publication record above control repaired-primary metadata status.

The accepted final review is
`PASS_M00_L16_EXCEPTIONAL_REPAIR_CLOSED_READY_FOR_REFREEZE_AUTHORIZATION`.
Technical, verification, documentation and architecture closure are CLOSED;
remaining repair requirements NONE. P3-H01 is IMPLEMENTED / VERIFIED /
INDEPENDENTLY REVIEWED / PRESERVED / CLOSED. CFG-H01 is IMPLEMENTED / VERIFIED /
REMOVED / INDEPENDENTLY REVIEWED / CLOSED. Accepted evidence, separate applicability
dispositions and physical-evidence limits are preserved in the current record.

The Architect/User explicitly authorized the documentation re-freeze transition.
That authorization is consumed: M00_L16 is COMPLETE / FROZEN / READ-ONLY;
Repository Active Lesson Count 0; Active M00 Lesson Count 0; Current Active M00
Lesson NONE; exceptional repair CLOSED. Sections 1–17 preserve earlier stage
decisions and chronology; their former pending/editable/current wording is historical.

The final repair Transition Guide is accepted through the independent closure
review; its original steps and repair appendices remain preserved, with the
minimal authorized closure/re-freeze record appended. No Java/test changes or new
files/ADRs are authorized by this transition.

Repaired publication is PENDING. No repaired Git snapshot or new publication
identity exists. Original primary ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11,
metadata 3667290180fe1a9fd96265383e7412c142c18129 and
`PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION` remain historical truth.

Exact next gate: INDEPENDENT RE-FREEZE / FROZEN-CANDIDATE REVIEW.
Phase 3 remains HOLD until that review, separately authorized User Git snapshot/
publication workflow, push and external final publication verification precede
consideration of separately authorized audit resumption. Phase 4 remains
NOT STARTED / FORBIDDEN. No M00_L17. No Git, project execution or publication
occurs in this transition; the unrelated A01_L06 path state remains untouched.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## 19. Repaired primary snapshot publication metadata — 2026-09-29

Independent frozen-candidate review PASS:
`PASS_M00_L16_FROZEN_CANDIDATE_READY_FOR_USER_PRIMARY_SNAPSHOT`.

The User successfully created repaired frozen PRIMARY SNAPSHOT / COMMIT 1:
`015b8ca27d466a5a2fce2660a902bb58a4b62003`.
This exact identity is accepted User-supplied evidence. The Architect/User
authorized only the bounded post-freeze metadata reconciliation now completed.
M00_L16 remains COMPLETE / FROZEN / READ-ONLY; active counts 0; current active
M00 lesson NONE. P3-H01, CFG-H01 and exceptional repair remain CLOSED;
remaining repair requirements NONE. No reopening or source/test change.

Publication metadata: RECONCILED / READY FOR USER METADATA COMMIT.
Exact next gate: USER METADATA COMMIT — COMMIT 2.
Metadata Commit 2: PENDING USER ACTION / DOES NOT EXIST YET.
User push: PENDING / NOT PERFORMED YET.
External final repaired-publication verification: PENDING / NOT PERFORMED YET.
The repaired candidate is NOT YET PUBLISHED; no metadata commit hash is invented.
Original historical primary/metadata identities and original verification gate
remain preserved separately in the current record and original publication history.

Canonical sequence: repaired primary Commit 1 -> metadata reconciliation ->
User metadata Commit 2 -> User push -> external final publication verification.
No third verification-only commit and no implementation file in Commit 2.
Phase 3 remains HOLD pending publication gates and separate audit authorization;
Phase 4 remains NOT STARTED / FORBIDDEN. Protected unrelated state is untouched.
No Git or project execution by this reconciliation.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.
