# ADR — M00_L16 ACM-01-F01 Swerve Observation Package Repair

<!-- ACM-01-F01 CURRENT BEGIN -->
## Current formal repair closure — version 1.2, 2026-09-30

The Architect explicitly authorized ACM-01-F01 repair closure after the
independent final review: PASS_ACM_01_F01_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION.
Section 10 records the closure. Sections 1-9 and the pre-closure snapshot
preserve earlier registration, failure, verification and reconciliation stages.

- ACM-01-F01: CLOSED. Technical, verification, documentation and architecture
  closure dimensions: CLOSED. Remaining ACM-01-F01 repair requirements: NONE.
- CF-U and PF-U: CAUSE NOT ESTABLISHED; preserved historical limitations,
  independently adjudicated nonblocking for this repair closure.
- M00_L16: Status IN_PROGRESS; Active State REOPENED / IN_PROGRESS / EDITABLE
  solely for later separately authorized lifecycle/governance work. The
  exceptional repair is CLOSED; source/test scope is not reopened. Repository
  and M00 active lesson counts: 1; Current Active M00 Lesson: M00_L16.
  M00_L16 is not re-frozen or republished.
- ACM-01: HOLD — READY FOR INDEPENDENT DOMAIN REREVIEW. ACM-02: NOT STARTED.
  Phase 3: HOLD. Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.
- P3-H01: CLOSED / PRESERVED. CFG-H01: CLOSED / REMOVED.
- Exact next gate: INDEPENDENT ACM-01 DOMAIN REREVIEW.

<!-- ACM-01-F01 CURRENT END -->

<!-- ACM-01-F01 PRE-CLOSURE RECONCILED SNAPSHOT BEGIN -->
## Historical pre-closure reconciliation record — version 1.1, 2026-09-29

This dedicated ADR records the complete ACM-01-F01 repair chronology under the
Architect-approved exception. Authoritative Documents A/B/C retain their authority.
The preserved version 1.0 metadata and Sections 1–8 below record registration,
implementation and initial failed verification; their former HOLD/PENDING or
UNVERIFIED entries are historical stage evidence. Section 9 records subsequent
User verification, independent review and the current reconciliation.

- Lifecycle: M00_L16 EXCEPTIONAL REPAIR IN PROGRESS.
- Status: IN_PROGRESS; Active State: REOPENED / IN_PROGRESS / EDITABLE.
- Repository Active Lesson Count: 1; Active M00 Lesson Count: 1; Current Active M00 Lesson: M00_L16.
- Sole editable lesson: M00_L16_MechanismAutonomousEventIntegration.
- ACM-01-F01: IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED / DOCUMENTATION RECONCILED; independent final closure review PENDING.
- P3-H01: CLOSED / PRESERVED; CFG-H01: CLOSED / REMOVED.
- ACM-01: HOLD pending repair closure + ACM-01 rereview; ACM-02: NOT STARTED.
- Phase 3: HOLD / HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE.
- Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.
- Exact next gate: INDEPENDENT FINAL ACM-01-F01 ARCHITECTURE / CLOSURE REVIEW.

<!-- ACM-01-F01 PRE-CLOSURE RECONCILED SNAPSHOT END -->

## Preserved registration and initial verification record — version 1.0


- Record version: 1.0.
- Date: 2026-09-29.
- Finding: ACM-01-F01.
- Lifecycle: EXCEPTIONAL REPAIR IN PROGRESS.
- Status: bounded implementation completed; automated verification HOLD / FAILED before test execution.
- Architect accepted design: `PASS_ACM_01_F01_REPAIR_DESIGN_READY_FOR_ARCHITECT_IMPLEMENTATION_AUTHORIZATION`.
- ACM-01: HOLD — REPAIR IN PROGRESS; ACM-02: NOT STARTED.
- Phase 4: NOT STARTED / FORBIDDEN.

## 1. Reason and authority

[OC-02, Observation Package Standard](../Document_C/English/02_Observation_Package_Standard_EN.pdf),
version 1.0, Section 1 requires mechanism-specific observations in
`frc.robot.observation.<mechanism>`. Section 9 requires Reason, Scope, Impact,
Decision, version update and migration evidence for responsibility moves or
breaking model changes. The authoritative English PDF governs; its VERIFIED
registered TEXTUAL mirror supports machine reading after integrity verification.

The inherited `frc.robot.observation.SwerveObservation` placement conflicts
with OC-02. Historical S00_L05 approval of the root-package placement remains
historical evidence of a conflicting lower-authority decision; it does not amend
OC-02. The Architect accepted the independent repair design and expressly
authorized this M00_L16-only frozen exception. No runtime malfunction was
established. This finding concerns package placement only.

## 2. Scope and historical preservation

The inherited placement spans the accepted 58-snapshot lineage beginning at
S00_L05 and continuing through M00_L16. Preserve S00_L05–L24, A00_L01–L04,
A01_L01–L09, V00_L01–L09 and M00_L01–L15 unchanged. Only the current
M00_L16_MechanismAutonomousEventIntegration project is corrected.

M00_L16 is REOPENED / IN_PROGRESS / EDITABLE for this exact exception.
Repository Active Lesson Count and Active M00 Lesson Count are both 1;
Current Active M00 Lesson is M00_L16. No M00_L17 is authorized.

## 3. Decision, impact and migration

Move the existing production model and existing semantic test into the swerve
observation package; replace thirteen consumer imports; extend the moved test
with one narrow placement guard. Leave no compatibility alias.

- Old qualified name: `frc.robot.observation.SwerveObservation`.
- New qualified name: `frc.robot.observation.swerve.SwerveObservation`.
- Old production path: `src/main/java/frc/robot/observation/SwerveObservation.java`.
- New production path: `src/main/java/frc/robot/observation/swerve/SwerveObservation.java`.
- Old test path: `src/test/java/frc/robot/observation/SwerveObservationTest.java`.
- New test path: `src/test/java/frc/robot/observation/swerve/SwerveObservationTest.java`.

All paths in the Java boundary below are relative to M00_L16.
The qualified/binary identity of the outer type and its nested types changes.
Previously compiled consumers require rebuilding against the new name.
A clean build is required; stale old classes must not remain on the test
classpath. Pre-implementation inspection found no affected package-private
access or production old-name reflection/serialization dependency.

Preserve outer record components, nested records, constructors, validation,
methods, units, signs, comments and JavaDoc. Production consumer bodies and all
existing semantic tests/helpers remain byte-identical after normalization of
the authorized package/import replacements. Runtime algorithms, behavior and
telemetry keys do not change. This ADR version records the migration; it does
not revise the frozen OC-02 document or model schema.

## 4. Exact authorized Java boundary

### Production — six existing identities

1. `src/main/java/frc/robot/commands/DriveThreeMeterValidationCommand.java` — import only.
2. `src/main/java/frc/robot/commands/HolonomicTrajectoryFollowingCommand.java` — import only.
3. `src/main/java/frc/robot/commands/PoseTargetedAutonomousMotionCommand.java` — import only.
4. `src/main/java/frc/robot/observation/SwerveObservation.java` — move and package declaration only.
5. `src/main/java/frc/robot/subsystems/SwerveSubsystem.java` — import only.
6. `src/main/java/frc/robot/telemetry/swerve/SwerveTelemetryFacade.java` — import only.

### Tests — nine existing identities

1. `src/test/java/frc/robot/commands/PoseTargetedAutonomousMotionCommandTest.java` — import only.
2. `src/test/java/frc/robot/observation/SwerveObservationTest.java` — move/package and narrow guard only.
3. `src/test/java/frc/robot/subsystems/SwerveSimulationIntegrationTest.java` — import only.
4. `src/test/java/frc/robot/subsystems/SwerveSubsystemKnownFieldPoseResetTest.java` — import only.
5. `src/test/java/frc/robot/subsystems/SwerveSubsystemMeasuredSpeedTest.java` — import only.
6. `src/test/java/frc/robot/subsystems/SwerveSubsystemModulePositionTest.java` — import only.
7. `src/test/java/frc/robot/subsystems/SwerveSubsystemOdometryTest.java` — import only.
8. `src/test/java/frc/robot/subsystems/SwerveSubsystemPoseEstimatorTest.java` — import only.
9. `src/test/java/frc/robot/telemetry/swerve/SwerveTelemetryFacadeTest.java` — import only.

Expected delta: two moves, two package changes, thirteen imports, one guard,
zero new Java identities and zero aliases. The guard independently checks the
new runtime name, new source presence, old source absence and old class absence.

## 5. Verification plan and stop conditions

Use the established WPILib 2026 Java 17 environment. Execute sequentially and
stop on failure:

1. Moved `frc.robot.observation.swerve.SwerveObservationTest`.
2. Unchanged `frc.robot.subsystems.SwerveSubsystemTest`.
3. `frc.robot.subsystems.SwerveSubsystemKnownFieldPoseResetTest`.
4. `frc.robot.subsystems.SwerveSubsystemPoseEstimatorTest`.
5. `frc.robot.telemetry.swerve.SwerveTelemetryFacadeTest`.
6. Full test suite.
7. Clean build.

After success, compare normalized Java bytes with the captured pre-edit SHA-256
baseline; verify both old paths absent, both new paths present, imports only,
existing semantic tests unchanged and all protected authored files unchanged.
Simulation and physical execution are not required or authorized here.

STOP on any extra Java identity, unexpected target, package-private failure,
old-name runtime coupling, model/body/unit/key change, architecture redesign,
cross-lesson edit or another defect. Do not widen this exception.

## 6. Frozen Backbone and prior repairs

Preserve RobotContainer composition, concrete-IO vendor ownership, subsystem
IO contracts and immutable observation flow:
hardware/IO -> IOInputs -> subsystem -> immutable Observation -> read-only telemetry.
Commands remain scheduler-managed and use subsystem-semantic APIs.
Constants remains configuration authority; explicit safe stop, Real/Simulation
selection and scheduler requirements remain unchanged. No command ownership
flags or scheduler polling for arbitration are introduced.

P3-H01 remains CLOSED / PRESERVED; CFG-H01 remains CLOSED / REMOVED.
Their separate [configuration-authority ADR](ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md)
and accepted evidence remain unchanged. This package repair does not reopen them.

## 7. Publication and protected state

Preserve the original publication (primary
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`, metadata
`3667290180fe1a9fd96265383e7412c142c18129`) and accepted repaired publication
(primary `015b8ca27d466a5a2fce2660a902bb58a4b62003`, metadata
`8f78de936c76bbf3888430de80b984851202457d`).
The accepted repaired external gate is
`PASS_M00_L16_REPAIRED_FINAL_PUBLICATION_VERIFIED_READY_FOR_SEPARATE_PHASE3_RESUMPTION_AUTHORIZATION`.
These are prior publication identities, not identities for ACM-01-F01.
No new commit, push, publication, re-freeze or automatic audit resumption is claimed.

Preserve the unrelated A01_L06_OneMeter_Forward.path state; all historical
lessons; PathPlanner_Practice_2026/; curriculum/FRC_Robot_Programming_For_Dummies/;
the CAN allocation registry; both governance __pycache__ directories; org/;
dependencies/assets and all Java outside the exact boundary.
build.gradle, settings.gradle, observation/package-info.java,
RobotSchedulerExceptionBoundaryTest.java, SwerveSubsystemTest.java,
RobotContainer, Constants, IO/vendor adapters and control algorithms are unchanged.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

## 8. Chronology and implementation evidence

1. Independent repair design accepted by the Architect.
2. Explicit bounded implementation authorization supplied on 2026-09-29.
3. Governance validator PASS and all 12 registered VERIFIED mirror hashes
   matched before edits; required governance/source reading completed.
4. This ADR and minimal current lifecycle registration created before Java changes.
5. Implementation completed: two moves/package changes, thirteen import replacements,
   one guard, zero new Java identities and zero aliases.
6. Gate 1 attempted with WPILib JDK 17.0.16:
   `gradlew.bat test --tests frc.robot.observation.swerve.SwerveObservationTest`.
   Result: exit 1; `compileJava` FAILED; BUILD FAILED in 59s;
   1 actionable task executed. Compilation reported 100 errors, including
   unresolved existing Constants, GyroIO, SwerveModuleIO and Vision types.
   No tests ran. Gates 2–7 were NOT RUN under the required fail-stop policy.
   No clean/cache recovery, retry, additional repair or build-file edit occurred.
7. Read-only preservation checks after the failure confirmed both relocations
   and all 15 normalized Java identities match their pre-edit SHA-256 values;
   removing the exact inserted lifecycle blocks reproduces all eight original
   document hashes. Runtime/classpath placement remains UNVERIFIED.
   The broader protected fingerprint check did NOT PASS:
   pre-edit count 12119 / SHA-256
   `ac668aa35e173e0751813d8144980a7348d528cc48223068aa947d80f5784df0`;
   observed count 12118 / SHA-256
   `d69c0e5ae05d6e1f3706c0bc5af9d389cfb9c542599540b12a53c348a0e510e8`.
   The cause is not established. Read-only Git scope inspection showed no
   additional tracked change beyond this authorized repair and the preserved
   pre-existing A01_L06 path difference. Complete protected-state hash
   preservation is not claimed; no protected/unrelated repair was attempted.
8. Next gate: Architect review of failed compilation and the protected-state
   fingerprint discrepancy, with explicit disposition before verification resumes.
   Independent post-implementation review remains pending.

This registration is not final documentation reconciliation, repair closure,
re-freeze or publication authorization.

## 9. Authorized documentation/evidence reconciliation — 2026-09-29

### 9.1 Authority and actual chronology

The Architect/User accepted the independent implementation review and expressly
authorized documentation/evidence reconciliation only in this existing dedicated
ADR and eight existing documents: AGENTS.md, root README.md, the M00 roadmap ADR,
M00_L16 README.md, LESSON_STATUS.md, LESSON_PLAN.md, LESSON_CHECKLIST.md and
docs/M00_L15_to_M00_L16_Step_by_Step.md. No other documentation, Java, project
execution, Git write, final closure review, re-freeze, publication or audit
resumption is authorized by this stage.

Sections 1–4 retain the authority basis, historical conflict, Architect decision,
exact 15-identity boundary and qualified/binary-name migration consequences.
Version 1.1 updates this repair record with later evidence; it does not alter
the frozen governance documents, model schema or approved scope.

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

### 9.2 Initial compile failure and CF-U

CF-U — CAUSE NOT ESTABLISHED.

The first attempt failed during compileJava with 100 diagnostics involving broad
unresolved existing symbols. No compiler diagnostic identified SwerveObservation
as the failure. Source inspection found the referenced existing types present;
no package/path mismatch or new production-body defect was established.
The preserved failed result in Section 8 remains a real historical failure.
No tests ran during that attempt; later PASS does not rewrite it.

The accepted User controlled reproduction used WPILib Temurin Java 17.0.16 in the
correct M00_L16 directory. User clean with --no-daemon PASS preceded the moved
model test with --no-daemon --stacktrace. All six tests and the placement guard PASS.
THE EARLIER FAILURE DID NOT REPRODUCE AFTER CONTROLLED CLEAN REPRODUCTION.
Cache corruption, daemon failure, Gradle bug, source-set bug, environment problem
and implementation defect were not established as causes. CF-U remains CF-U.

### 9.3 Historical protected fingerprint and PF-U

PF-U — CAUSE NOT ESTABLISHED.

The Section 8 historical fingerprint count was 12,119 -> 12,118, with the exact
recorded hashes preserved there. The original per-path/hash manifest was not
retained, so the exact historical pathname difference cannot be reconstructed.
No pathname is invented, no unauthorized modification is inferred, and the
discrepancy is not claimed resolved.

Accepted independent-review disposition: PF-U remains a historical verification
limitation but does NOT block ACM-01-F01 technical acceptance. Current read-only
scope/protected-state checks found no unauthorized tracked repair expansion.
The independently reviewed current protected fingerprint matches the post-failure
forensic snapshot; this does not reconstruct or equal the original pre-edit
aggregate. Current technical acceptance and historical fingerprint limits
must remain separately stated.

### 9.4 Accepted implementation, User verification and applicability

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

Accepted independent Sol implementation review:
`PASS_ACM_01_F01_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.

The review confirmed the exact 15 existing Java identities, thirteen import-only
consumers, unchanged normalized production/model bodies and inherited semantic
test bodies/helpers, the narrow placement guard, and no backward propagation.
Fresh static report inspection corroborates the five required focused classes
with no errors/failures, including all six moved model tests.
Old compiled root-package model/test classes are absent; new package classes
are present in the clean User build output. These are read-only artifact
observations, not Codex test execution.

The outer and nested qualified/binary identities changed as planned; source
meaning did not. No compatibility alias exists. Old executable qualified-name
usage survives only as the intentional negative class-loading assertion in
the placement guard. No old-name production coupling is established.

### 9.5 Backbone, history and prior repairs

The accepted independent review preserves the Frozen Backbone:
Driver -> Xbox Controller -> controls -> commands -> subsystems -> IO -> hardware;
hardware -> IOInputs -> subsystem/estimator -> immutable Observation ->
read-only telemetry -> NT4/Glass/log. IO/vendor ownership, scheduler-managed
commands, RobotContainer composition, Constants authority, safe stop and
Real/Simulation selection are unchanged.

Historical affected predecessors remain S00_L05–L24, A00_L01–L04, A01_L01–L09,
V00_L01–L09 and M00_L01–L15. The inherited placement remains historical truth
in those snapshots; M00_L16 only is the prospective/canonical correction target.
The accepted 58-snapshot lineage and 57 protected predecessors are preserved.

P3-H01: CLOSED / PRESERVED. CFG-H01: CLOSED / REMOVED.
Their separate ADR and histories remain unchanged; their verification is not
fresh ACM-01-F01 evidence. Section 7 preserves original and accepted prior
repaired publication identities and the repaired external verification gate.
No new ACM-01-F01 publication identity is claimed.
Phase-2 PASS with recorded limits remains preserved, as do D2A/H01, historical
R1, A01_L07 and historical-byte qualifications.

### 9.6 Current lifecycle and remaining requirements

M00_L16: EXCEPTIONAL REPAIR IN PROGRESS, Status IN_PROGRESS,
Active State REOPENED / IN_PROGRESS / EDITABLE; both active counts 1.
ACM-01-F01: IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED, pending independent final closure review.
ACM-01: HOLD pending repair closure + ACM-01 rereview.
ACM-02: NOT STARTED. Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.

Exact next gate: INDEPENDENT FINAL ACM-01-F01 ARCHITECTURE / CLOSURE REVIEW.
The repair evidence and transition appendix are ready for that independent
review; final repair closure and final exceptional-repair Transition Guide
acceptance are not claimed. Explicit re-freeze, freeze review, User-owned
publication, external verification and separately authorized ACM-01 rereview/
audit resumption remain later gates. Constants.java cleanup/refactor is excluded.

Only the authorized nine documents changed in this reconciliation.
Java/source/tests, Gradle/settings/dependencies, assets, historical lessons,
Documents A/B/C, manifest, VERIFIED mirrors and generated files are unchanged.
Protected/unrelated state, including A01_L06_OneMeter_Forward.path and the
established paths in Section 7, remains untouched.
No Git write, tests/build, Simulation, Glass, Driver Station or hardware action.
github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

### 9.7 Record version history

| Record version | Stage |
| --- | --- |
| 1.0 | Exceptional registration, exact implementation and initial failed verification; preserved in Sections 1–8. |
| 1.1 | Later User clean reproduction and required automated PASS, independent review, CF-U/PF-U dispositions and authorized nine-document reconciliation; final closure pending. |

## 10. Formal ACM-01-F01 repair closure — 2026-09-30

### 10.1 Authority and decision

The Architect explicitly authorized ACM-01-F01 REPAIR CLOSURE after the
independent final architecture/closure review returned
PASS_ACM_01_F01_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION.
This decision closes ACM-01-F01 only. The architecture finding was valid;
the canonical observation-package correction was implemented, the exact
bounded Java delta independently verified, behavior preserved, required User
automated gates passed, documentation reconciled, and final independent
closure review passed. Frozen Backbone and historical lessons are preserved.
Final exceptional-repair Transition Guide evidence is accepted for this
repair closure.

| Closure dimension | Final disposition |
| --- | --- |
| TECHNICAL | CLOSED |
| VERIFICATION | CLOSED |
| DOCUMENTATION | CLOSED |
| ARCHITECTURE | CLOSED |

ACM-01-F01: CLOSED. Remaining ACM-01-F01 technical, verification,
documentation and architecture repair requirements: NONE.

### 10.2 Preserved evidence and limitations

Section 8 retains the initial failed compileJava attempt, which stopped
before tests. CF-U — CAUSE NOT ESTABLISHED. THE EARLIER FAILURE DID NOT
REPRODUCE AFTER CONTROLLED CLEAN REPRODUCTION. No particular cache,
daemon, Gradle, environment, source-set or implementation cause is inferred.

PF-U — CAUSE NOT ESTABLISHED. The historical protected aggregate discrepancy
12,119 -> 12,118 remains unresolved because the original path/hash manifest
was not retained; its exact pathname/cause is unknown. The independent final
closure review confirmed PF-U does not block this repair closure. No
unauthorized modification is inferred.

The later User-controlled clean reproduction, moved SwerveObservationTest
(all six tests including placement guard), SwerveSubsystemTest,
SwerveSubsystemKnownFieldPoseResetTest, SwerveSubsystemPoseEstimatorTest,
SwerveTelemetryFacadeTest, full suite and clean build all PASS. Accepted
final User output:

    BUILD SUCCESSFUL in 45s
    7 actionable tasks: 7 executed

    ACM-01-F01 AUTOMATED USER VERIFICATION COMPLETE
    ALL REQUIRED AUTOMATED GATES PASS

No additional count or exit code is inferred. Focused tests, full suite and
clean build were REQUIRED / COMPLETE / PASS. Simulation was NOT REQUIRED FOR
REPAIR CLOSURE; Glass and Driver Station were NOT APPLICABLE; real hardware
was NOT REQUIRED FOR REPAIR CLOSURE. This closure adds no fresh project or
physical execution claim.

### 10.3 Lifecycle and scope after closure

M00_L16 remains Status IN_PROGRESS and Active State REOPENED / IN_PROGRESS /
EDITABLE under the exceptional lifecycle. The repair itself is CLOSED;
the lesson awaits separately authorized audit-domain and re-freeze gates.
Repository Active Lesson Count: 1; Active M00 Lesson Count: 1; Current Active
M00 Lesson: M00_L16. No Java/test edit, re-freeze or publication is authorized.

ACM-01: HOLD — READY FOR INDEPENDENT DOMAIN REREVIEW; it is not closed.
ACM-02: NOT STARTED. Phase 3: HOLD /
HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE.
Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.
P3-H01: CLOSED / PRESERVED. CFG-H01: CLOSED / REMOVED.
The original and prior repaired publication identities remain historical;
no new ACM-01-F01 publication identity exists.

The exact next gate is INDEPENDENT ACM-01 DOMAIN REREVIEW. That review,
re-freeze, publication, Constants.java cleanup/refactor and later audit work
are outside this closure recording. The unrelated
A01_L06_OneMeter_Forward.path and all protected/unrelated paths remain
untouched. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.
No Git write or project execution occurred in this recording.

### 10.4 Record version history

| Record version | Stage |
| --- | --- |
| 1.2 | Explicit Architect-authorized ACM-01-F01 formal repair closure after independent final closure review; ACM-01 domain rereview pending. |
