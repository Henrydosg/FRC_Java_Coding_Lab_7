# ADR — M00_L16 ACM-01-F02 Drive Validation Observation Package Repair
<!-- ACM-01-F02 CURRENT BEGIN -->
## Current ACM-01-F02 formal repair closure — 2026-09-30

The Architect explicitly authorized ACM-01-F02 REPAIR CLOSURE after the
accepted independent final architecture/closure review:
`PASS_ACM_01_F02_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`.
The subsequent authorization is now formally recorded. **ACM-01-F02: CLOSED**.
Technical, verification, documentation and architecture dimensions are
**CLOSED**. Remaining F02 repair requirements: **NONE**.
The governing closure decision is [the F02 ADR, Section 3](#3-formal-acm-01-f02-repair-closure--2026-09-30).

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

Record version 1.2 adds Section 3; Sections 1 and 2 and their original
version histories remain preserved.

<!-- ACM-01-F02 CURRENT END -->

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT BEGIN -->
## Historical ACM-01-F02 documentation/evidence reconciliation — 2026-09-30

Version 1.1 records accepted implementation, User verification, independent
implementation review and this authorized documentation reconciliation.
Section 2 governs the current F02 stage. Section 1 and the original version
history remain the unchanged registration-stage record.

F02 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
F02 is NOT CLOSED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED. F01 and DOC-01 remain
CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED.
Exact next gate: INDEPENDENT FINAL ACM-01-F02 ARCHITECTURE / CLOSURE REVIEW.

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT END -->


## 1. Registration and decision — 2026-09-30

**Status at registration:** ACM-01-F02 ACCEPTED / IMPLEMENTATION AUTHORIZED.
This section records authorization before Java changes; it does not record
implementation, verification, independent review, or closure.

### Reason and governing authority

Document C, OC-02 Section 1 requires mechanism-specific observation types in
`frc.robot.observation.<mechanism>`. The immutable
`DriveThreeMeterValidationObservation` represents Swerve-drive validation
distance and four Swerve module deltas, but the current M00_L16 source declares
`frc.robot.observation.DriveThreeMeterValidationObservation`. ACM-01-F02 is an
accepted current/canonical package-placement finding. The root-package
identity first survives in S00_L23.

### Architect decision, scope, and impact

The Architect approves the target
`frc.robot.observation.swerve.DriveThreeMeterValidationObservation` in
**M00_L16 only**. Historical S00_L23–L24, A00, A01, V00, and M00_L01–L15
copies remain unchanged. This is a package-identity correction, with no
behavioral, numerical, telemetry-topic, scheduler, IO, configuration, or
runtime-selection change. No compatibility alias is approved. The old
production source path must be absent after relocation.

Exactly five existing Java identities may change:

1. Move `src/main/java/frc/robot/observation/DriveThreeMeterValidationObservation.java`
   to `src/main/java/frc/robot/observation/swerve/DriveThreeMeterValidationObservation.java`;
   change only its package declaration.
2. Update only the observation import in
   `src/main/java/frc/robot/commands/DriveThreeMeterValidationCommand.java`.
   Preserve its existing F01 `SwerveObservation` import.
3. Update only the observation import in
   `src/main/java/frc/robot/telemetry/validation/DriveThreeMeterValidationTelemetry.java`.
4. Update only the observation import in
   `src/main/java/frc/robot/telemetry/validation/DriveThreeMeterValidationTelemetryFacade.java`.
5. Update only the observation import in
   `src/test/java/frc/robot/commands/DriveThreeMeterValidationCommandTest.java`.

Exactly one new focused test is authorized:
`src/test/java/frc/robot/observation/swerve/DriveThreeMeterValidationObservationTest.java`.
It checks the new qualified name and local source path, absence of the old
source path, and unavailability of the old class on a clean test classpath.
No seventh Java identity, repository-wide scanner, cross-lesson dependency,
or additional abstraction is authorized.

The record components, constructors, idle factory, units, signs, module
ordering, distance interpretation, comments, public behavior, command bodies,
telemetry contract, facade behavior, topic names, and existing behavioral
assertions must be preserved. The four existing consumers change imports only.

### Verification and fail-stop order

Before project execution, statically confirm the exact six-identity boundary,
old-path removal, new declaration/imports, no alias or executable old import,
and reverse-normalized equivalence. Then the User runs these required gates in
order, stopping on the first failure:

1. Clean, then the new focused observation placement test.
2. Existing `frc.robot.commands.DriveThreeMeterValidationCommandTest`.
3. Full test suite.
4. Clean build.

Simulation and real hardware are not required for this package-only repair;
Glass and Driver Station are not applicable. No gate is claimed PASS by this
registration.

### Lifecycle and separation

M00_L16 remains IN_PROGRESS, not re-frozen or republished. ACM-01 remains
HOLD; ACM-02 is NOT STARTED; Phase 4 is NOT STARTED / FORBIDDEN. No M00_L17.
ACM-01-F01 and ACM-01-F01-DOC-01 remain CLOSED. P3-H01 is CLOSED /
PRESERVED; CFG-H01 is CLOSED / REMOVED. CF-U and PF-U remain historical
cause-not-established limitations. ACM-01-F03 and ACM-01-F04 are ACCEPTED /
PARKED; their repairs are separate and follow F02 in that order.

The unrelated A01_L06_OneMeter_Forward.path and all protected state remain
untouched. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.
The exact next gate at registration is bounded F02 implementation followed by
User automated verification and independent post-implementation review.

## Version history

| Version | Date | Decision |
| --- | --- | --- |
| 1.0 | 2026-09-30 | Architect-adjudicated F02 registration and six-identity implementation authorization; verification and closure pending. |

<!-- ACM-01-F02 RECONCILIATION EVIDENCE BEGIN -->
## 2. Accepted implementation and evidence reconciliation — 2026-09-30

### 2.1 Authority and stage chronology

Document C OC-02 Section 1 requires mechanism-specific observations under
`frc.robot.observation.<mechanism>`. ACM-01 domain rereview found the
S00_L23-inherited root-package DriveThreeMeterValidationObservation in
canonical M00_L16. The Architect accepted F02, accepted but parked F03 and
F04, and locked the separate repair order F02 -> F03 -> F04.

Sol bounded repair design passed:
`PASS_ACM_01_F02_REPAIR_DESIGN_READY_FOR_ARCHITECT_IMPLEMENTATION_AUTHORIZATION`.
The Architect then authorized registration and exactly six Java identities.
The dedicated F02 ADR and eight lifecycle registration records were created
before Java changes. Section 1 preserves that decision and stage. The
M00_L16-only implementation followed; historical lessons were not migrated.

The first Gate 1 invocation failed during Gradle configuration, before tasks
ran. It used Java 8 while GradleRIO 2026.2.1 requires Java 17 or newer.
Neither clean nor the focused placement test completed. This is a
VERIFICATION ENVIRONMENT / JVM VERSION FAILURE for that invocation, not
evidence of an F02 code defect. Why Java 8 was selected is not established;
no cache, daemon, GradleRIO or WPILib defect is inferred.

The User explicitly set `JAVA_HOME=C:\Users\Public\wpilib\2026\jdk`,
confirmed OpenJDK Temurin 17.0.16+8, and restarted the authorized fail-stop
sequence. Clean, focused placement test, direct consumer test, full suite,
and clean build passed. Sol independent post-implementation review then
passed. This authorized reconciliation records those later stages without
closing F02 or replacing the registration history.

### 2.2 Exact technical result and migration evidence

All paths below are relative to M00_L16.

| Existing identity | Accepted change |
| --- | --- |
| `src/main/java/frc/robot/observation/DriveThreeMeterValidationObservation.java` | Moved to `src/main/java/frc/robot/observation/swerve/DriveThreeMeterValidationObservation.java`; only package declaration changes to `frc.robot.observation.swerve`. |
| `src/main/java/frc/robot/commands/DriveThreeMeterValidationCommand.java` | F02 import replacement only; prior F01 `SwerveObservation` import preserved. |
| `src/main/java/frc/robot/telemetry/validation/DriveThreeMeterValidationTelemetry.java` | Import replacement only. |
| `src/main/java/frc/robot/telemetry/validation/DriveThreeMeterValidationTelemetryFacade.java` | Import replacement only. |
| `src/test/java/frc/robot/commands/DriveThreeMeterValidationCommandTest.java` | Import replacement only; existing behavioral assertions preserved. |

Exactly five existing Java identities changed, plus one new focused test:
`src/test/java/frc/robot/observation/swerve/DriveThreeMeterValidationObservationTest.java`.
There was no seventh F02 Java identity.

The new guard verifies the runtime qualified name
`frc.robot.observation.swerve.DriveThreeMeterValidationObservation`, the
new local production source path, absence of the old local source path,
and unavailability of `frc.robot.observation.DriveThreeMeterValidationObservation`
through the negative `Class.forName(...)` assertion on a clean test
classpath. It uses lesson-relative paths; no repository-wide scanner,
absolute machine path, cross-lesson dependency or behavior redesign was added.

Record components, constructors, idle factory, units, signs, distance
interpretation, comments and front-left/front-right/back-left/back-right
module ordering are preserved. Command behavior, telemetry topics, published
fields and behavioral assertions are preserved. Zero production method-body
changes, zero compatibility aliases and zero historical lesson migrations
were made. The old production identity is absent; the intentional negative
test string is the only remaining old qualified-name occurrence in Java.
Reverse-normalized comparisons of the record and four consumers PASS.

### 2.3 Corrected User-owned automated verification

Runtime supplied by the User:

```text
JAVA_HOME=C:\Users\Public\wpilib\2026\jdk
openjdk version "17.0.16"
Temurin-17.0.16+8
```

| Required gate | Accepted User evidence |
| --- | --- |
| Gate 1 — clean | PASS. BUILD SUCCESSFUL in 12s. 1 actionable task: 1 executed. |
| Gate 1 — focused placement test | PASS. `usesSwervePackageWithoutLegacyClass() PASSED` in `frc.robot.observation.swerve.DriveThreeMeterValidationObservationTest`. BUILD SUCCESSFUL in 31s. 4 actionable tasks: 4 executed. |
| Gate 2 — direct consumer test | PASS. `frc.robot.commands.DriveThreeMeterValidationCommandTest`; supplied result includes `abortsAtDeterministicTimeout() PASSED`. BUILD SUCCESSFUL in 13s. 4 actionable tasks: 1 executed, 3 up-to-date. |
| Gate 3 — full test suite | PASS. BUILD SUCCESSFUL in 22s. 4 actionable tasks: 1 executed, 3 up-to-date. |
| Gate 4 — clean build | PASS. BUILD SUCCESSFUL in 46s. 7 actionable tasks: 7 executed. |

Final User marker:

```text
ACM-01-F02 GATE 4 PASS
CLEAN BUILD PASS
ALL REQUIRED F02 USER AUTOMATED VERIFICATION GATES PASS
```

These are accepted User-owned results. Actionable task counts are not test
counts; no total test count, unseen test name or unseen exit code is inferred.
This reconciliation reruns no project verification.

### 2.4 Independent review and applicability

Accepted Sol post-implementation review:
`PASS_ACM_01_F02_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.

The independent review confirmed governance integrity PASS, registration
before code, the exact six-Java-identity boundary with no seventh identity,
correct relocation and imports, a bounded guard, absence of the old production
identity and aliases, semantic preservation and reverse-normalized comparison
PASS. It accepted the complete corrected User sequence, found no additional
automated or runtime gate required, and confirmed Frozen Backbone and
historical preservation with F03/F04 parked.

| Verification category | Accepted disposition |
| --- | --- |
| Focused placement test | REQUIRED — COMPLETE / PASS |
| Direct consumer test | REQUIRED — COMPLETE / PASS |
| Full test suite | REQUIRED — COMPLETE / PASS |
| Clean build | REQUIRED — COMPLETE / PASS |
| Simulation | NOT REQUIRED |
| Glass | NOT APPLICABLE |
| Driver Station | NOT APPLICABLE |
| Real hardware | NOT REQUIRED FOR F02 CLOSURE |

No fresh runtime verification is claimed. Existing physical-evidence limits
persist; accepted automated verification does not become hardware evidence.

### 2.5 Architecture and historical preservation

F02 preserves RobotContainer composition-root placement, concrete IO adapter
vendor ownership, subsystem IO contracts, observation/telemetry relationships,
scheduler-managed commands, Constants authority, explicit safe stop,
real/simulation selection and CommandScheduler requirements. It adds no
manual ownership flags or scheduler polling arbitration. These are accepted
package-repair preservation findings, not later ACM-domain audits.

No F02 correction was propagated into S00_L23–L24, A00, A01, V00 or
M00_L01–L15. Their historical root-package placement and step-by-step records
remain historical truth. F01 and DOC-01 remain CLOSED; P3-H01 remains
CLOSED / PRESERVED; CFG-H01 remains CLOSED / REMOVED. CF-U and PF-U remain
CAUSE NOT ESTABLISHED, separate historical limitations; their histories are
not merged with the established F02 JVM-version mismatch.

### 2.6 Current lifecycle and next gate

F02 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
F02 is NOT CLOSED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED. F01 and DOC-01 remain
CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED.
Exact next gate: INDEPENDENT FINAL ACM-01-F02 ARCHITECTURE / CLOSURE REVIEW.

Repository Active Lesson Count: 1; Active M00 Lesson Count: 1; Current Active
M00 Lesson: M00_L16. No M00_L17, Constants cleanup/refactor, F03/F04 work,
re-freeze, publication, ACM-02 or Phase-4 resumption is authorized by this
reconciliation. F02 documentation/evidence reconciliation is complete;
final closure review and Architect closure authorization remain later gates.

The unrelated A01_L06_OneMeter_Forward.path and all protected state remain
untouched. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.
Only the nine authorized F02 documents are reconciled; no Java/test,
dependency, configuration, asset or generated-file edit, Git write, or
project execution occurs in this stage.

### 2.7 Reconciliation version record

| Version | Date | Decision |
| --- | --- | --- |
| 1.1 | 2026-09-30 | Accepted implementation, corrected User verification, independent review and documentation/evidence reconciliation; F02 pending independent final closure review, NOT CLOSED. |
<!-- ACM-01-F02 RECONCILIATION EVIDENCE END -->

<!-- ACM-01-F02 FORMAL CLOSURE BEGIN -->
## 3. Formal ACM-01-F02 repair closure — 2026-09-30

### 3.1 Explicit Architect authorization and decision

After Sol's independent final architecture/closure review returned
`PASS_ACM_01_F02_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`,
the Architect explicitly authorized ACM-01-F02 REPAIR CLOSURE.
That authorization is now formally recorded: **ACM-01-F02: CLOSED**.

The review established all four closure dimensions CLOSED and zero
substantive repair requirements before authorization. The final review
established readiness; the subsequent explicit Architect decision authorized
closure. This record consumes that closure-recording authorization for F02
only. Section 3 governs the formal closure; Sections 1 and 2 retain their
registration and pre-closure reconciliation stage meanings, including former
current/pending wording.

### 3.2 Closure basis and four dimensions

- The original package finding was valid under Document C OC-02 Section 1.
  S00_L23 is the historical origin; the prospective correction is M00_L16 only.
- The correct canonical identity is
  `frc.robot.observation.swerve.DriveThreeMeterValidationObservation`.
- The dedicated F02 ADR and eight lifecycle ledger registrations preceded
  Java modification.
- Exactly five existing Java identities changed: observation relocation and
  package declaration only, three production consumer import replacements,
  and one existing test import replacement. Exactly one new focused
  DriveThreeMeterValidationObservationTest supplies the approved placement
  guard. There is no seventh F02 Java identity.
- Record components, constructors, idle factory, units, signs, module order,
  distance interpretation, comments, command behavior, telemetry contract and
  existing behavioral assertions remain preserved. Reverse-normalized
  comparisons PASS; no production method body changed.
- The old production source/qualified identity is removed. No compatibility
  alias, wrapper, forwarder or shim exists. The intentional old-name string
  in the negative Class.forName assertion remains expected.
- Required User automated verification is COMPLETE / PASS. Independent
  post-implementation review, documentation reconciliation and independent
  final closure review PASS.
- Frozen Backbone, historical lessons and protected/unrelated state are
  preserved. F03 and F04 remained ACCEPTED / PARKED throughout F02.

Accepted independent post-implementation review:
`PASS_ACM_01_F02_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`.

Accepted documentation reconciliation:
`PASS_ACM_01_F02_DOCUMENTATION_RECONCILED_READY_FOR_FINAL_CLOSURE_REVIEW`.

| Closure dimension | Formal disposition | Remaining requirement |
| --- | --- | --- |
| TECHNICAL | CLOSED | NONE |
| VERIFICATION | CLOSED | NONE |
| DOCUMENTATION | CLOSED | NONE |
| ARCHITECTURE | CLOSED | NONE |

Remaining substantive ACM-01-F02 repair requirements: **NONE**.

### 3.3 Preserved verification history and applicability

Section 2.1 preserves the initial Gate 1 Java-8 invocation. GradleRIO
2026.2.1 requires Java 17+; Gradle configuration failed before tasks ran.
Neither clean nor DriveThreeMeterValidationObservationTest completed.
Classification: VERIFICATION ENVIRONMENT / JVM VERSION FAILURE.
This did not establish an F02 code defect. Why Java 8 was selected remains
not established; no explanation is invented.

Section 2.3 retains the accepted User restart with
`JAVA_HOME=C:\Users\Public\wpilib\2026\jdk` and OpenJDK Temurin
17.0.16+8, with these results:

| Accepted User gate | Preserved evidence |
| --- | --- |
| Gate 1 clean | PASS — BUILD SUCCESSFUL in 12s. |
| Gate 1 focused placement test | PASS — usesSwervePackageWithoutLegacyClass() PASSED; BUILD SUCCESSFUL in 31s. |
| Gate 2 direct consumer test | PASS — supplied output includes abortsAtDeterministicTimeout() PASSED; BUILD SUCCESSFUL in 13s. |
| Gate 3 full test suite | PASS — BUILD SUCCESSFUL in 22s. |
| Gate 4 clean build | PASS — BUILD SUCCESSFUL in 46s. |

Final accepted User marker remains:

```text
ACM-01-F02 GATE 4 PASS
CLEAN BUILD PASS
ALL REQUIRED F02 USER AUTOMATED VERIFICATION GATES PASS
```

Focused placement test, direct consumer test, full suite and clean build are
REQUIRED — COMPLETE / PASS. Simulation is NOT REQUIRED; Glass and Driver
Station are NOT APPLICABLE; real hardware is NOT REQUIRED FOR F02 CLOSURE.
No total test count or unseen exit code is inferred. This recording adds no
project execution or fresh physical evidence; existing physical-evidence
limits remain preserved.

### 3.4 Post-closure lifecycle, preservation and next gate

M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED, with Active
State REOPENED / IN_PROGRESS / EDITABLE for separately authorized scope only.
Repository Active Lesson Count: 1; Active M00 Lesson Count: 1; Current Active
M00 Lesson: M00_L16. No M00_L17.

ACM-01 remains HOLD; Phase 3 remains
HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE. ACM-02 is NOT STARTED.
Phase 4 is NOT STARTED / FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED;
the locked separate repair order F02 -> F03 -> F04 is preserved.

F01 and F01-DOC-01 remain CLOSED. P3-H01 remains CLOSED / PRESERVED;
CFG-H01 remains CLOSED / REMOVED. CF-U and PF-U remain CAUSE NOT
ESTABLISHED, separate historical limitations. No F02 correction was
propagated into S00_L23–L24, A00, A01, V00 or M00_L01–L15; their
root-package placement remains historical truth.

Exact next gate:
**ARCHITECT ACTIVATION / DESIGN AUTHORIZATION FOR ACM-01-F03**.

This recording performs no F03/F04 design or implementation, re-freeze,
publication, ACM-01 closure, ACM-02 activation, Phase-4 work or Constants.java
cleanup/refactor. It changes only the nine authorized F02 documents; Java,
tests, configuration, dependencies, deployment assets and generated files
remain untouched. The unrelated A01_L06_OneMeter_Forward.path and all
protected/unrelated state remain untouched. No Git write occurs.
`github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

### 3.5 Closure record version history

| Version | Date | Decision |
| --- | --- | --- |
| 1.2 | 2026-09-30 | Independent final closure review PASS, subsequent explicit Architect F02 repair-closure authorization, and bounded formal recording; F02 CLOSED with four CLOSED dimensions and no remaining repair requirements. F03 Architect activation/design authorization is the next gate. |
<!-- ACM-01-F02 FORMAL CLOSURE END -->
