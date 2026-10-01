# ADR — M00_L16 ACM-01-F04 Autonomous Command Family Package Repair

## 1. Architect-authorized registration — 2026-09-30

**Finding:** ACM-01-F04 — ACCEPTED / ACTIVATED / TARGET AND FAMILY APPROVED /
IMPLEMENTATION AUTHORIZED. This section records the decision **before** any F04
Java edit. It makes no implementation, verification, independent-review,
documentation-reconciliation or closure claim.

### Authority and reason

Document A, Final Frozen Package Backbone, Section 8 assigns autonomous
actions, command compositions, factories and named-command registration to
`frc.robot.commands.auto`. The fourteen types below together own the M00_L16
autonomous command family. Their current `frc.robot.commands` placement is
the single ACM-01-F04 package-ownership finding. The approved canonical target
is `frc.robot.commands.auto`. This is a prospective correction in **M00_L16
only**; A00/A01/V00 and M00_L01–L15 copies remain historical truth. No general
command-package cleanup or behavior change is authorized.

### Exact production family and historical origins

Each listed identity moves from
`src/main/java/frc/robot/commands/<Type>.java` to
`src/main/java/frc/robot/commands/auto/<Type>.java`, with the corresponding
package identity change and only required imports/references. Old current
paths disappear; no compatibility alias is created.

| Type | Earliest surviving origin |
| --- | --- |
| `AutonomousSafetyHoldCommand` | A00_L01 |
| `BoundedRobotRelativeAutonomousDriveCommand` | A00_L03 |
| `PoseTargetedAutonomousMotionCommand` | A01_L02 |
| `AllianceAwareAutonomousStartPoseResetCommand` | A01_L05 |
| `HolonomicTrajectoryFollowingCommand` | A01_L05 |
| `PathPlannerTrajectoryAdapter` | A01_L06 |
| `AutoBuilderContractAdapter` | A01_L07 |
| `PathPlannerExecutionPathFactory` | A01_L07 |
| `AutonomousPreparationCoordinator` | A01_L08 |
| `AutonomousRoutineFactory` | A01_L08 |
| `PrepareAutonomousCommand` | A01_L08 |
| `AutonomousEventBinding` | A01_L09 |
| `AutonomousEventRegistration` | A01_L09 |
| `AutonomousEventDemonstrationCommand` | A01_L09 |

### Hard implementation boundary

All paths here are relative to current M00_L16. The only other production
identity is `src/main/java/frc/robot/RobotContainer.java`: update its seven
affected imports only, with no method-body or composition change. Relocate
the fourteen matching dedicated tests from `src/test/java/frc/robot/commands/`
to `src/test/java/frc/robot/commands/auto/`, preserving their existing tests:
`AutonomousSafetyHoldCommandTest`, `BoundedRobotRelativeAutonomousDriveCommandTest`,
`PoseTargetedAutonomousMotionCommandTest`,
`AllianceAwareAutonomousStartPoseResetCommandTest`,
`HolonomicTrajectoryFollowingCommandTest`, `PathPlannerTrajectoryAdapterTest`,
`AutoBuilderContractAdapterRecoveryTest`, `PathPlannerExecutionPathFactoryTest`,
`AutonomousPreparationCoordinatorTest`, `AutonomousRoutineFactoryTest`,
`PrepareAutonomousCommandTest`, `AutonomousEventBindingTest`,
`AutonomousEventRegistrationTest`, and
`AutonomousEventDemonstrationCommandTest`.

Seven tests remain at their current paths and may receive only necessary
import/reference changes: `commands/KnownFieldPoseResetDashboardTest`,
`RobotContainerAutonomousModeSchedulingTest`,
`RobotContainerAutonomousRoutineSelectionTest`,
`RobotContainerMechanismAutonomousEventIntegrationTest`,
`RobotContainerPathPlannerIntegrationTest`,
`RobotSchedulerExceptionBoundaryTest`, and
`subsystems/SwerveSimulationIntegrationTest`.

The approved boundary is **14 relocated production + 1 production import-only
+ 14 relocated tests + 7 nonrelocating test consumers = 36 existing Java
identities, zero new Java identities**. A need to edit a 37th identity stops
implementation for Architect scope review. `IntakeArchitectureBoundaryTest`,
`RobotContainerElevatorCompositionTest`, and
`RobotContainerFlywheelCompositionTest` receive no name-only edit.

### Preservation and guard

Only package/import/reference placement may change. Preserve constructors,
method bodies, suppliers, CommandScheduler requirements, deferred requirement
sets, interruption and safe-stop behavior, autonomous preparation and access,
PathPlanner configuration, path markers, routine names and ordering, timing,
telemetry, controller/default/trigger bindings, and the Frozen Backbone.
`RobotContainer` retains the `LEARNING_EVENT` supplier
`() -> new IntakeToFeederCommand(intakeSubsystem, feederSubsystem)` and
`Set.of(intakeSubsystem, feederSubsystem)`, preserving fresh command creation,
Intake/Feeder scheduler ownership and the autonomous-only boundary.

Add exactly one placement-guard method to the relocated
`AutonomousEventRegistrationTest`. A fixed explicit list of the fourteen F04
production types checks each new runtime qualified name, lesson-relative new
source path, absence of its old source path, and absence of its old qualified
class through `Class.forName(oldName, false, classLoader)` on a clean test
classpath. No dynamic scanner, cross-lesson path or F03 type is included.

`AutonomousStartContext` stays in root commands as an immutable provenance
value; moved owners/tests import it as needed.
`frc.robot.autonomous.AutonomousEventId` stays the semantic event identity.
F03 `LearningTrajectoryFactory` and its test stay unchanged in
`commands.auto`. F01/F02/F03 accepted edits, `Constants.java`, mechanism
commands, other owners, package-info, deployed resources and historical
lessons remain protected. The unrelated A01_L06 path state remains untouched.

### Future User verification and applicability — record only

The User first sets `JAVA_HOME=C:\Users\Public\wpilib\2026\jdk`, puts its
`bin` first on `PATH`, and confirms OpenJDK/Temurin 17; stop on mismatch.
Ordered fail-stop gates: (1) `clean`, then focused
`frc.robot.commands.auto.*` tests covering the fourteen moved tests, guard,
and unchanged F03 `LearningTrajectoryFactoryTest`; (2) the seven nonrelocating
tests above plus unchanged `IntakeArchitectureBoundaryTest`, using their real
FQCNs; (3) full test suite; (4) `clean build`. These are future User actions,
not results of this registration. Interactive Simulation is not required;
Glass and Driver Station are not applicable; real hardware is not required
for F04 repair closure, provided the package-only change preserves behavior.
Any behavior change invalidates that applicability and stops the repair.

### Lifecycle at registration

F01, F01-DOC-01, F02 and F03 are **CLOSED**. P3-H01 is CLOSED / PRESERVED;
CFG-H01 is CLOSED / REMOVED; CF-U and PF-U remain CAUSE NOT ESTABLISHED.
F04 is REGISTERED / IMPLEMENTATION AUTHORIZED; implementation and User
verification remain pending at this stage. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 and Phase 3 remain HOLD; ACM-02
NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN. No M00_L17. Next gate:
bounded 36-identity implementation and static self-audit, then User-owned
Java-17 Gates 1–4 and independent post-implementation review. No Git write,
project execution, closure, re-freeze or publication is authorized here.

## 2. Bounded implementation and static self-audit — 2026-09-30

This section records the authorized implementation **after** Section 1 and
the eight lifecycle registrations were saved. F04 is REGISTERED /
IMPLEMENTED / UNVERIFIED / PENDING USER VERIFICATION. No automated or
interactive verification, independent review, documentation reconciliation
or closure is claimed.

All fourteen approved production identities and fourteen matching dedicated
tests now occupy their `commands/auto` paths and packages. All twenty-eight
old current paths are absent. `RobotContainer` has only its seven affected
imports changed. The seven approved nonrelocating test consumers have only
the needed imports changed. No 37th Java identity or new Java identity was
needed. The one added method in `AutonomousEventRegistrationTest` checks a
fixed list of exactly the fourteen F04 production types for new qualified
names and source paths, absent old paths and absent old runtime classes.
The F03 factory is excluded.

Static inspection found no remaining executable old F04 qualified import
in current lesson Java, no compatibility alias and no unexpected production
consumer. Reverse-normalizing package/import changes in each of the
**36 existing Java identities** to the pre-F04 working-state text matched
all 36; removing the new guard and its three imports restores the prior
registration test. This supports preservation of production method bodies,
existing behavioral assertions, `RobotContainer` composition, scheduler
requirements, safe stop, PathPlanner and `LEARNING_EVENT` supplier semantics.
It is static evidence, not compilation or runtime verification.

Read-only SHA-256 comparisons show unchanged F03 factory/test, root
`AutonomousStartContext`, `AutonomousEventId`, `Constants.java`, and the
pre-existing modified A01_L06 path. Historical lessons and other protected
files were not edited for F04. No aggregate protected-content digest result
is claimed. The sensitive recovery-codes file was NOT ACCESSED / NOT MODIFIED.

The exact next gate is **User-owned Java-17 environment confirmation and
ordered automated Gates 1–4 in Section 1**, followed by independent
post-implementation review. F01, F01-DOC-01, F02 and F03 remain CLOSED;
P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED; CF-U and PF-U
CAUSE NOT ESTABLISHED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 and Phase 3 HOLD; ACM-02 NOT STARTED;
Phase 4 NOT STARTED / FORBIDDEN. No M00_L17. No Git write, project
execution, re-freeze or publication occurred in this implementation turn.

For Gate 2, the actual existing test identities are
`frc.robot.commands.KnownFieldPoseResetDashboardTest`,
`frc.robot.RobotContainerAutonomousModeSchedulingTest`,
`frc.robot.RobotContainerAutonomousRoutineSelectionTest`,
`frc.robot.RobotContainerMechanismAutonomousEventIntegrationTest`,
`frc.robot.RobotContainerPathPlannerIntegrationTest`,
`frc.robot.RobotSchedulerExceptionBoundaryTest`, and
`frc.robot.subsystems.SwerveSimulationIntegrationTest`, plus unchanged
`frc.robot.IntakeArchitectureBoundaryTest`. The User runs these after
Gate 1, before the full-suite and clean-build gates; none ran here.

## 3. User verification, independent review, and documentation/evidence reconciliation — 2026-09-30

This section reconciles the accepted User verification and Sol post-implementation
review after Sections 1 and 2. Section 1 remains the pre-edit Architect
registration; Section 2 remains the implementation-stage record. Their stage
wording is historical. The sequence below preserves the full F04 chronology:

1. ACM-01 domain rereview identified the autonomous command-family placement
   finding ACM-01-F04 in M00_L16.
2. F04 was accepted and parked behind F02 and F03.
3. F02 and F03 were formally closed before F04 activation.
4. The Architect activated F04 for read-only design.
5. Sol identified a responsibility-based autonomous command family, rather
   than a two-file or class-name-based repair.
6. Sol identified frc.robot.commands.auto as the canonical target under
   Document A, Section 8.
7. The Architect approved the responsibility-based family boundary and exact
   implementation count.
8. This dedicated ADR and the eight lifecycle registrations recorded the
   decision before Java changes.
9. The M00_L16-only package implementation followed that registration.
10. The User supplied the required Java-17 environment evidence and executed
    the approved ordered verification sequence.
11. User Gates 1 through 4 all passed.
12. Sol independently reviewed the implementation and accepted User evidence.
13. This bounded documentation/evidence reconciliation records that result.
    F04 remains pending independent final closure review and is not closed.

### Architecture authority and bounded family

Document A, Final Frozen Package Backbone, Section 8 assigns autonomous
actions, command compositions, factories, and named-command registration to
frc.robot.commands.auto. The F04 family is identified by those responsibilities,
not by class name. The fourteen production identities and fourteen dedicated
test identities are the exact rosters recorded in Section 1. All fourteen
production identities moved from frc.robot.commands to frc.robot.commands.auto;
for each, the new lesson-local path exists, the old path is absent, and no
compatibility alias was created. There is no thirty-seventh F04 Java identity.

The exact implementation count is 14 relocated production identities, plus
1 production import-only identity (RobotContainer), plus 14 relocated
dedicated test identities, plus 7 nonrelocating test import/reference
identities: 36 existing Java identities changed and zero new Java identities.
RobotContainer remained in place and changed only its seven required imports;
no method body changed. Its composition-root ownership, bindings, autonomous
lifecycle, supplier behavior, and CommandScheduler ownership remain preserved.

The seven nonrelocating test consumers remain at their existing paths:
KnownFieldPoseResetDashboardTest,
RobotContainerAutonomousModeSchedulingTest,
RobotContainerAutonomousRoutineSelectionTest,
RobotContainerMechanismAutonomousEventIntegrationTest,
RobotContainerPathPlannerIntegrationTest,
RobotSchedulerExceptionBoundaryTest, and SwerveSimulationIntegrationTest.
Their F04 changes were limited to required imports/references. The three
non-edited static-name boundary tests remain
IntakeArchitectureBoundaryTest,
RobotContainerElevatorCompositionTest, and
RobotContainerFlywheelCompositionTest. IntakeArchitectureBoundaryTest
participated in User verification only.

Exactly one bounded family-placement guard method was added to
AutonomousEventRegistrationTest. Its fixed list contains exactly the fourteen
F04 production types. For each type it checks the expected runtime qualified
name under frc.robot.commands.auto, the new lesson-local source path, absence
of the old lesson-local source path, and absence of the old qualified runtime
identity through Class.forName. It adds no repository-wide scanner, absolute
machine path, cross-lesson dependency, unrelated assertion, or F03 type.
LearningTrajectoryFactory belongs to closed F03 and is excluded from the F04
guard.

The excluded or retained identities remain outside the relocated family:
frc.robot.autonomous.AutonomousEventId remains the event identity;
AutonomousStartContext remains in root commands as a provenance value;
the closed-F03 LearningTrajectoryFactory remains under commands.auto;
ordinary teleop, mechanism, and root commands, observations, telemetry, IO,
subsystems, and Constants are outside F04.

### RobotContainer, event, and behavior preservation

RobotContainer remains the composition root and sole external production
consumer identified by the independent review. It retains the LEARNING_EVENT
supplier () -> new IntakeToFeederCommand(intakeSubsystem, feederSubsystem)
and requirement set Set.of(intakeSubsystem, feederSubsystem). Fresh supplier
semantics, Intake and Feeder requirements, deferred requirements,
autonomous-only access, event identity, and NamedCommands registration remain
preserved. F04 introduced zero teleop access expansion, trigger changes,
default-command changes, or scheduler-ownership changes.

Sol independently confirmed no behavioral redesign. Existing constructors,
command behavior, suppliers, subsystem and deferred requirements, scheduler
ownership, event IDs, LEARNING_EVENT, NamedCommands registration, failure
handling, routine selection and ordering, preparation and readiness,
PathPlanner and trajectory behavior, event markers, alliance transforms,
timing and waits, safe stop, telemetry, controller bindings, default commands,
and trigger bindings remain preserved. There are zero Constants.java changes,
zero deployed PathPlanner resource changes, and zero compatibility aliases.

The fourteen relocated dedicated test bodies and helpers were preserved.
F03 remains unchanged: hashes for
frc.robot.commands.auto.LearningTrajectoryFactory and
LearningTrajectoryFactoryTest match their pre-F04 values. They are not among
the 36 F04 identities. AutonomousEventId is unchanged; AutonomousStartContext
is retained at its original package identity.

### Reverse-normalized comparison and protected-state limits

Sol independently confirmed the reverse-normalized comparison for all
36 existing Java identities against the captured post-F01/F02/F03,
pre-F04 working-state baseline: 36 of 36 matched. The baseline is a working
tree capture, not a separate Git commit, and is not described as committed
historical state. Protected hash comparisons confirmed the unchanged F03
factory and test, AutonomousStartContext, AutonomousEventId, Constants.java,
and the pre-existing modified
A01_L06_OneMeter_Forward.path. No aggregate protected-content digest PASS
was claimed. Constants.java cleanup/refactor remains unstarted.

### Accepted User Java-17 environment and automated gates

The accepted User environment evidence identifies the WPILib JDK at
C:\Users\Public\wpilib\2026\jdk and the runtime family as OpenJDK / Temurin
17. Sol accepted this User evidence and did not independently rerun Java.
No separate raw java-version transcript is asserted.

| Gate | Required sequence and accepted result |
| --- | --- |
| Gate 1 | REQUIRED — PASS. clean, then frc.robot.commands.auto.* focused tests, covering the relocated F04 dedicated tests, the family-placement guard, and the unchanged F03 LearningTrajectoryFactoryTest. BUILD SUCCESSFUL in 55s; 4 actionable tasks: 4 executed. Final marker: ACM-01-F04 GATE 1 PASS / CLEAN + AUTO PACKAGE FOCUSED TESTS PASS. No unseen individual test names are inferred. |
| Gate 2 | REQUIRED — PASS. KnownFieldPoseResetDashboardTest; RobotContainerAutonomousModeSchedulingTest; RobotContainerAutonomousRoutineSelectionTest; RobotContainerMechanismAutonomousEventIntegrationTest; RobotContainerPathPlannerIntegrationTest; RobotSchedulerExceptionBoundaryTest; SwerveSimulationIntegrationTest; and IntakeArchitectureBoundaryTest. BUILD SUCCESSFUL in 15s; 4 actionable tasks: 1 executed, 3 up-to-date. Final marker: ACM-01-F04 GATE 2 PASS / CONSUMER + EVENT-BOUNDARY TESTS PASS. The task-cache result is preserved; this is not described as every task freshly executed. |
| Gate 3 | REQUIRED — PASS. Full test-suite Gradle gate. BUILD SUCCESSFUL in 25s; 4 actionable tasks: 1 executed, 3 up-to-date. Final marker: ACM-01-F04 GATE 3 PASS / FULL TEST SUITE PASS. The invocation does not establish that every suite test was freshly executed. |
| Gate 4 | REQUIRED — PASS. Clean build. BUILD SUCCESSFUL in 1m 3s; 7 actionable tasks: 7 executed. Final markers: ACM-01-F04 GATE 4 PASS / CLEAN BUILD PASS / ALL REQUIRED F04 USER AUTOMATED VERIFICATION GATES PASS. Seven actionable tasks are not a test count. |

No total test count or unseen exit code is inferred. These are accepted User
execution results, not execution performed during this documentation turn.

### Independent review and applicability

Sol's independent post-implementation review passed governance integrity and
confirmed the canonical package and responsibility-based family, registration
before code, exact 36-existing/zero-new boundary, all fourteen production
relocations, RobotContainer's import-only change, LEARNING_EVENT preservation,
all fourteen dedicated test relocations, the single fixed-list placement
guard, all seven nonrelocating consumer updates, and the three unchanged
static-name boundary tests. Sol confirmed there is no executable old qualified
dependency or compatibility alias, RobotContainer is the sole external
production consumer, and scheduler requirements and semantics are preserved.
The review confirmed the 36/36 reverse-normalized comparison, F03 preservation,
AutonomousEventId preservation, AutonomousStartContext retention, all User
Gates 1–4, and the Gate 2/Gate 3 up-to-date nuances. It confirmed no additional
runtime verification is required for this package-only repair and that
historical lessons and the Frozen Backbone remain preserved.

Accepted independent review token:
PASS_ACM_01_F04_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION

Applicability remains: environment REQUIRED / COMPLETE / PASS; Gates 1–4
REQUIRED / COMPLETE / PASS; interactive Simulation NOT REQUIRED; Glass NOT
APPLICABLE; Driver Station NOT APPLICABLE; real hardware NOT REQUIRED FOR
F04 PACKAGE-ONLY REPAIR CLOSURE. No fresh interactive runtime verification
or real-hardware verification is claimed.

### Frozen Backbone, history, and current lifecycle

The repair preserves the Frozen Backbone: RobotContainer composition root;
vendor APIs only in concrete IO adapters; subsystems accessed through IO
contracts; observation and telemetry hierarchies; scheduler-managed commands;
Constants authority; explicit safe stop; real/Simulation selection;
CommandScheduler requirements; no manual ownership flags; and no scheduler
polling arbitration. F04 changes only current M00_L16. Historical lineage
beginning at A00_L01 and inherited A00/A01/V00/M00 lesson copies remains
unchanged; no correction was propagated backward.

ACM-01-F01 and ACM-01-F01-DOC-01 remain CLOSED. F02 and F03 remain CLOSED /
FORMALLY RECORDED. P3-H01 remains CLOSED / PRESERVED; CFG-H01 remains CLOSED /
REMOVED. CF-U and PF-U remain CAUSE NOT ESTABLISHED. F04 is IMPLEMENTED /
USER VERIFIED / INDEPENDENTLY REVIEWED / DOCUMENTATION RECONCILED / PENDING
INDEPENDENT FINAL CLOSURE REVIEW; F04 is NOT CLOSED. M00_L16 remains
IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 remains HOLD; Phase 3 remains HOLD /
HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE; ACM-02 remains NOT STARTED; Phase 4 remains NOT STARTED / FORBIDDEN.
No M00_L17.

The exact next gate is INDEPENDENT FINAL ACM-01-F04 ARCHITECTURE / CLOSURE
REVIEW. This reconciliation does not perform that review, close F04 or ACM-01,
re-freeze M00_L16, publish, start ACM-02, or start Phase 4.

## 4. Formal ACM-01-F04 repair closure - 2026-10-01

### Closure authority and accepted review

The Architect reviewed Sol's independent final architecture/closure review
and accepted its recommendation. Sol's exact final-review token is:
`PASS_ACM_01_F04_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_AUTHORIZATION`.
Sol classified all ten closure dimensions as CLOSED: governance, architecture,
implementation, behavioral preservation, verification, documentation,
historical preservation, Frozen Backbone, protected state, and remaining F04
work. The review found no substantive F04 repair work remaining.

The Architect explicitly authorized `ACM-01-F04 = FORMALLY CLOSED`. This is
Architect-owned closure authority; Sol's review token is independent evidence
and recommendation, not a closure action. This section formally records F04
closure only.

### Completed chronology

Section 3 preserves F04 chronology through documentation/evidence
reconciliation. Its final sequence is now:

14. Sol completed the independent final architecture/closure review and
    returned the token above.
15. The Architect accepted Sol's recommendation and explicitly authorized
    formal closure of ACM-01-F04.
16. This bounded documentation action records **ACM-01-F04: FORMALLY CLOSED /
    FORMALLY RECORDED** in this ADR and the eight existing lifecycle ledgers.

The earlier registration, bounded implementation, User verification,
independent implementation review, and documentation/evidence reconciliation
remain preserved in Sections 1-3. The exact implementation boundary remains
14 production relocations, 1 production import-only `RobotContainer`, 14
dedicated-test relocations, and 7 nonrelocating test updates: 36 existing
Java identities and zero new Java identities. No thirty-seventh identity was
changed. The responsibility family and its details remain governed by Sections
1-3.

### Closure evidence and preserved limits

The accepted User Java-17 environment was
`C:\Users\Public\wpilib\2026\jdk` (OpenJDK / Temurin 17). User
Gates 1-4 passed with their exact outputs and Gate 2/Gate 3 up-to-date task
nuance preserved in Section 3. Interactive Simulation was NOT REQUIRED;
Glass and Driver Station were NOT APPLICABLE; real hardware was NOT REQUIRED
for this package-only repair. No fresh project execution is claimed by this
closure-recording turn.

`LEARNING_EVENT` remains registered by `RobotContainer` with the fresh
`() -> new IntakeToFeederCommand(intakeSubsystem, feederSubsystem)` supplier
and `Set.of(intakeSubsystem, feederSubsystem)`. Event identity, supplier
semantics, Intake and Feeder requirements, deferred requirements,
autonomous-only access, `NamedCommands` behavior, and scheduler ownership
remain preserved. F03's `LearningTrajectoryFactory` and its test remain
outside F04 and unchanged.

The 36/36 reverse-normalized match is against the captured post-F01/F02/F03,
pre-F04 working-tree baseline, not a separate Git commit. Named-file hashes
remained unchanged; no aggregate protected-content digest PASS was established.
The unrelated pre-existing A01_L06 path state remains untouched. Historical
predecessor lessons and the Frozen Backbone remain preserved. Constants.java,
deployed PathPlanner resources, and historical lessons were not modified.
`github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

### Post-closure lifecycle and next gate

- ACM-01-F01: CLOSED.
- ACM-01-F01-DOC-01: CLOSED.
- ACM-01-F02: CLOSED / FORMALLY RECORDED.
- ACM-01-F03: CLOSED / FORMALLY RECORDED.
- ACM-01-F04: FORMALLY CLOSED / FORMALLY RECORDED.
- M00_L16: IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED.
- ACM-01: HOLD. F04 closure does not close ACM-01.
- ACM-02: NOT STARTED.
- Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.

The exact next gate is **INDEPENDENT ACM-01 DOMAIN REREVIEW**, whose purpose is
to confirm no package or lesson architecture-boundary finding remains after
F01-F04 closure. This record does not perform that review or close ACM-01.
Re-freeze, publication, audit resumption, ACM-02, and Phase 4 remain outside
this authorization. No Constants.java cleanup/refactor, Java/test change, Git
write, or Gradle/test/build/Simulation/Glass/Driver Station/hardware execution
occurred in this closure-recording step. Remaining F04 repair work: NONE.
