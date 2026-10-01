# ADR — M00_L16 ACM-01-F03 Learning Trajectory Factory Package Repair

## 1. Architect-authorized registration and decision — 2026-09-30

**Finding:** ACM-01-F03, CURRENT / CANONICAL, ACCEPTED / ACTIVATED /
IMPLEMENTATION AUTHORIZED. This section is the pre-implementation
registration. It does not claim implementation, User verification,
independent review, evidence reconciliation or closure.

### Reason and governing authority

The fixed LearningTrajectoryFactory originated in A01_L03 and remains at
`frc.robot.util.LearningTrajectoryFactory` in current M00_L16.
It creates a learning/autonomous trajectory from named field poses,
waypoints and constraints. It is not a generic shared helper. AGENTS.md
Section 4 and Document A, Final Frozen Package Backbone, Section 2
reserve util for genuinely generic shared helpers. Document A Section 8
assigns autonomous factories to `commands/auto`. The Architect
approves exactly `frc.robot.commands.auto.LearningTrajectoryFactory`.

This decision addresses package ownership only. Existing
`frc.robot.autonomous.AutonomousEventId` owns a vendor-neutral event
identity; it does not override Document A's autonomous-factory rule.
F04's broader autonomous command-family placement remains ACCEPTED /
PARKED and is not designed or implemented here. The prospective
correction target is M00_L16 only. All A01, V00 and M00_L01-L15
historical copies remain protected.

### Scope, impact and migration boundary

All paths below are relative to M00_L16. Exactly four existing Java
identities may change; zero new Java identities.

| Identity | Authorized F03 delta |
| --- | --- |
| `src/main/java/frc/robot/util/LearningTrajectoryFactory.java` | Relocate to `src/main/java/frc/robot/commands/auto/LearningTrajectoryFactory.java`; change package declaration only. Remove old path; no alias. |
| `src/test/java/frc/robot/util/LearningTrajectoryFactoryTest.java` | Relocate to `src/test/java/frc/robot/commands/auto/LearningTrajectoryFactoryTest.java`; change package, add only necessary imports and exactly one narrow placement guard. Preserve ten existing behavioral tests and helpers. |
| `src/test/java/frc/robot/commands/HolonomicTrajectoryFollowingCommandTest.java` | Replace only the factory import. |
| `src/test/java/frc/robot/subsystems/SwerveSimulationIntegrationTest.java` | Replace only the factory import. Preserve the accepted F01 SwerveObservation import. |

The current factory has zero production callers. If another Java identity
or a production consumer must change, stop for Architect review without
expanding scope. Creating only the two `commands/auto` source/test
directories needed for the relocated identities is within this boundary.
No `package-info.java`, compatibility alias, wrapper or new
abstraction is authorized.

The placement guard checks only the new runtime qualified name, new
lesson-local production source path, absent old production source path
and absence of the old qualified class through negative
`Class.forName(...)` on a clean test classpath. Use lesson-relative
`Path.of(...)` values. No repository-wide scanner, absolute machine
path, cross-lesson dependency or unrelated assertion.

### Semantic preservation

Preserve the non-instantiable stateless structure,
`createLearningTrajectory()`, fresh WPILib trajectory generation,
validation and exception behavior, start/interior/goal geometry,
constraints, time parameterization, finite-state and monotonic-time
checks, units, comments, path-rotation versus holonomic-heading
distinction, alliance assumptions and all test behavior. No
mathematical redesign, PathPlanner conversion, numerical tuning,
field-coordinate change, Constants change or new autonomous capability.
Preserve RobotContainer composition, IO/subsystem boundaries,
Observation and telemetry flows, scheduler ownership, safe stop,
Real/Simulation selection and Constants authority.

The old production source and dedicated test paths must be absent.
Reverse-normalized post-edit comparison uses the pre-F03 working state,
preserving accepted F01/F02 changes. No historical lesson or inherited
transition guide is migrated.

### Verification plan and fail-stop order

The User owns future project execution. Before Gradle, set
`JAVA_HOME=C:\Users\Public\wpilib\2026\jdk`, prepend that JDK's
`bin` to PATH and confirm OpenJDK/Temurin 17. Stop on mismatch.

After static scope and reverse-normalized checks, run in order and stop
at the first failure:

1. REQUIRED — clean, then focused
   `frc.robot.commands.auto.LearningTrajectoryFactoryTest`.
2. REQUIRED — `frc.robot.commands.HolonomicTrajectoryFollowingCommandTest`
   and `frc.robot.subsystems.SwerveSimulationIntegrationTest`.
3. REQUIRED — full test suite.
4. REQUIRED — clean build.

Interactive Simulation is NOT REQUIRED; Glass and Driver Station are
NOT APPLICABLE; real hardware is NOT REQUIRED FOR F03 REPAIR CLOSURE.
No verification result is claimed by this registration. The earlier
F02 Java-8 environment failure remains historical evidence, separate
from F03; no cause for that Java selection is inferred.

### Lifecycle and separation

F01 and F01-DOC-01 remain CLOSED; F02 remains CLOSED / FORMALLY
RECORDED. P3-H01 is CLOSED / PRESERVED; CFG-H01 is CLOSED / REMOVED.
CF-U and PF-U remain CAUSE NOT ESTABLISHED. F04 is ACCEPTED / PARKED;
repair order is F03 -> F04.

M00_L16 is IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. ACM-01
remains HOLD; Phase 3 remains
`HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`.
ACM-02 is NOT STARTED; Phase 4 is NOT STARTED / FORBIDDEN.
No M00_L17, F04 work, Constants.java cleanup/refactor,
re-freeze or publication is authorized here. The unrelated
A01_L06_OneMeter_Forward.path state remains untouched.
`github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

**Next gate at registration:** implement only the four Java identities,
perform the bounded static self-audit, then await User-owned
verification. Formal repair closure and audit resumption are later
separate gates.

## 2. Bounded implementation and static self-audit — 2026-09-30

The Section 1 registration and eight lifecycle-ledger entries were written
before any F03 Java edit. The authorized package repair is implemented
only in the four existing Java identities listed in Section 1. No new Java
identity, production consumer or compatibility alias was introduced.

The production factory moved to
`src/main/java/frc/robot/commands/auto/LearningTrajectoryFactory.java`.
The old production path is absent. Its package declaration is the only
textual change: reversing it reproduces the pre-F03 working-state SHA-256
of the complete file. No production method body, geometry, trajectory
constraint, validation, exception, unit or comment changed.

The dedicated test moved to
`src/test/java/frc/robot/commands/auto/LearningTrajectoryFactoryTest.java`.
Its old path is absent. Exactly one placement test checks the runtime
qualified name, new lesson-local production source presence, old source
absence and old class absence with negative `Class.forName(...)`.
Removing that guard and its necessary imports and reversing the package
declaration reproduces the pre-F03 test SHA-256. All ten existing
behavioral test methods and helpers are byte-preserved by this check.

`HolonomicTrajectoryFollowingCommandTest` and
`SwerveSimulationIntegrationTest` changed only their factory
import. Reversing each import reproduces its pre-F03 working-state
SHA-256; the accepted F01 SwerveObservation import remains present.
The lesson-local Java source sweep found no production caller of the
factory, and the old qualified name remains only as the intentional
negative guard string. F04 and all historical lesson identities remain
outside this repair boundary.

These are static source checks, not executed JUnit, Gradle, build or
Simulation evidence. F03 is REGISTERED / IMPLEMENTED / UNVERIFIED /
PENDING USER VERIFICATION; no independent review or closure is claimed.
F04 remains ACCEPTED / PARKED. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 and Phase 3 remain HOLD,
ACM-02 NOT STARTED and Phase 4 NOT STARTED / FORBIDDEN.
The next gate is the User-owned Java-17 environment check and ordered
automated gates in Section 1, followed by independent review.

## 3. User verification, independent review and documentation reconciliation — 2026-09-30

### Authority and repair chronology

The ACM-01 domain rereview found the A01_L03-inherited
`frc.robot.util.LearningTrajectoryFactory` placement in current
M00_L16. F03 was accepted but initially parked behind F02. After F02
formal closure, the Architect activated F03 for read-only design. Sol
identified one governance-supported target:
`frc.robot.commands.auto.LearningTrajectoryFactory`. AGENTS.md
Section 4 reserves util for generic shared helpers; the authoritative
English Document A, Final Frozen Package Backbone, Sections 2 and 8
assign autonomous factories to `commands/auto`. The Architect
explicitly approved this target and the bounded implementation.

Section 1 of this ADR and the eight lifecycle-ledger registration
entries were created before any F03 Java edit. Section 2 records the
subsequent M00_L16-only implementation. F03 is independently resolvable
without moving the broader F04 autonomous command family; F04 remains
ACCEPTED / PARKED.

### Exact implementation and static preservation

The four existing Java identities changed for F03 are exactly:

| Existing identity | Accepted change |
| --- | --- |
| `LearningTrajectoryFactory.java` | Move from `src/main/java/frc/robot/util` to `src/main/java/frc/robot/commands/auto`; package declaration only. |
| `LearningTrajectoryFactoryTest.java` | Matching test move and package change; only necessary imports and one placement guard added. Ten prior behavioral tests and helpers preserved. |
| `HolonomicTrajectoryFollowingCommandTest.java` | Factory import replacement only. |
| `SwerveSimulationIntegrationTest.java` | Factory import replacement only; accepted F01 SwerveObservation import preserved. |

Zero new Java identities, zero fifth F03 Java identity, zero
compatibility aliases and zero other production consumers. Old M00_L16
factory and dedicated-test paths are absent. The new guard checks only
the runtime qualified name, new lesson-local production source path,
absent old source path and old qualified class absence via negative
`Class.forName(...)` on the clean test classpath. It uses
lesson-relative `Path.of(...)` values and adds no repository-wide
scanner, absolute machine path, cross-lesson dependency, unrelated
architecture assertion or trajectory behavior assertion.

The factory remains stateless and non-instantiable.
`createLearningTrajectory()` retains
`Constants.FieldConstants.kLearningStartingPose`, its interior
waypoint, goal pose, maximum velocity and acceleration, native WPILib
`TrajectoryGenerator`, geometry, path rotation versus holonomic
heading distinction, time parameterization, finite-state and
monotonic-time validation, exceptions, units, field coordinates,
comments and fresh-generation behavior. Production method bodies,
Constants.java, numerical behavior and autonomous capability did not
change. F04's `AutonomousRoutineFactory` and
`AutonomousEventRegistration` remain untouched.

Sol independently reverse-normalized all four identities against the
saved pre-F03 working state and confirmed matching SHA-256 hashes.
Production package reversal reproduces its prior complete text;
removing the one guard and its necessary imports and reversing the
test package reproduces the prior dedicated test; reversing the two
external imports reproduces their pre-F03 text, including accepted
F01/F02 working-state differences. This supports preservation of all
ten pre-existing dedicated behavioral tests and helpers.

### Accepted User-owned automated verification

The approved environment gate used
`C:\Users\Public\wpilib\2026\jdk` and reported OpenJDK
Temurin 17.0.16+8 before Gradle. The prior F02 Java-8 selection remains
a separate historical fact; no cause is inferred for it.

| Gate | Requirement and accepted User evidence | Disposition |
| --- | --- | --- |
| Environment | WPILib JDK, Temurin 17.0.16+8 confirmed. | REQUIRED — COMPLETE / PASS |
| 1 | Clean, then focused `frc.robot.commands.auto.LearningTrajectoryFactoryTest`. Supplied output includes `trajectoryUsesPositiveInteriorWaypointExcursionWithoutFixingSplineDiscretization() PASSED`; `BUILD SUCCESSFUL in 30s`; `4 actionable tasks: 4 executed`; User marker `ACM-01-F03 GATE 1 PASS / CLEAN + FOCUSED FACTORY TEST PASS`. | REQUIRED — COMPLETE / PASS |
| 2 | `frc.robot.commands.HolonomicTrajectoryFollowingCommandTest` and `frc.robot.subsystems.SwerveSimulationIntegrationTest`. Supplied named PASS results: `rejectsNullTrajectoryHeadingAndClock()`, `rejectsInvalidConfigurationAndDoesNotRunWhenDisabled()`, `holonomicFollowerConvergesForBlueAndStopsImmediatelyWhenDisabled()`, `poseTargetedAutonomousMotionConvergesToTheKnownLearningTarget()`, `rotationalChassisIntentAdvancesCounterclockwiseYawFromActualModuleStates()`, `redExecutionTrajectoryUsesTheL04TransformAndASeparateHolonomicHeading()`. `BUILD SUCCESSFUL in 12s`; `4 actionable tasks: 1 executed, 3 up-to-date`; User marker `ACM-01-F03 GATE 2 PASS / AFFECTED CONSUMER TESTS PASS`. | REQUIRED — COMPLETE / PASS |
| 3 | Full-suite Gradle gate, runtime Temurin 17.0.16+8: `BUILD SUCCESSFUL in 9s`; `4 actionable tasks: 4 up-to-date`; User marker `ACM-01-F03 GATE 3 PASS / FULL TEST SUITE PASS`. These tasks were UP-TO-DATE in this invocation; this is not a claim that every test freshly executed during Gate 3. | REQUIRED — COMPLETE / PASS |
| 4 | Clean build: supplied output includes VisionFrameTransform tests passing; `BUILD SUCCESSFUL in 37s`; `7 actionable tasks: 7 executed`; User marker `ACM-01-F03 GATE 4 PASS / CLEAN BUILD PASS / ALL REQUIRED F03 USER AUTOMATED VERIFICATION GATES PASS`. This provides clean-state complete-project execution evidence. | REQUIRED — COMPLETE / PASS |

These are accepted User-owned results, not Codex execution. Actionable
tasks are not a numeric test count. No unseen test, total test count or
exit code is inferred. Interactive Simulation is NOT REQUIRED; Glass
and Driver Station are NOT APPLICABLE; real hardware is NOT REQUIRED
FOR F03 REPAIR CLOSURE. No fresh interactive runtime evidence is
claimed, and no additional gate was imposed.

### Independent review, architecture and protected limits

Sol's independent post-implementation review confirmed governance
integrity, the `commands/auto` destination, F03 independence
from F04, registration-before-code chronology, the exact four-identity
boundary and zero new Java identities, correct relocations and guard,
ten preserved behavioral tests, both import-only changes, zero other
production callers, no executable old-qualified-name dependency
except the negative guard string, no alias, no production method-body
or Constants change, reverse-normalized preservation, completion of
required User gates, the Gate-3 UP-TO-DATE nuance and Gate-4 clean
build. It found Frozen Backbone and historical copies preserved and
returned exactly:

`PASS_ACM_01_F03_IMPLEMENTATION_VERIFIED_READY_FOR_DOCUMENTATION_RECONCILIATION`

RobotContainer remains the composition root; vendor APIs remain in
concrete IO adapters; subsystems consume IO contracts; Observation
and telemetry hierarchies, scheduler-managed commands, Constants
authority, explicit safe stop, Real/Simulation selection and
CommandScheduler requirements remain intact. No manual ownership
flags or scheduler-polling arbitration were added. This is a
package-responsibility correction only.

Historical copies beginning at A01_L03 and continuing through A01,
V00 and M00_L01-L15 remain unchanged. Their historical util placement
is not rewritten; the M00_L16 correction is prospective only. No
inherited historical transition guide is changed.

The unrelated A01_L06_OneMeter_Forward.path remained in its pre-existing
modified state, and no protected-file write was reported. An aggregate
protected-content digest match was NOT established. This absence is a
recorded evidence limit, not an established defect; no aggregate
protected-content PASS is claimed. `github-recovery-codes.txt`:
NOT ACCESSED / NOT MODIFIED.

### Reconciled lifecycle and next gate

This Section 3 reconciles accepted F03 implementation, User
verification and independent-review evidence. ACM-01-F03 is
IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
It is NOT CLOSED. F04 remains ACCEPTED / PARKED in order F03 -> F04.
F01 and F01-DOC-01 remain CLOSED; F02 CLOSED / FORMALLY RECORDED;
P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED; CF-U and PF-U
CAUSE NOT ESTABLISHED.

M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED.
ACM-01 and Phase 3 remain HOLD /
`HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`;
ACM-02 is NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN.
No M00_L17, re-freeze, publication or F04 implementation is authorized
by this reconciliation.

**Exact next gate:** INDEPENDENT FINAL ACM-01-F03 ARCHITECTURE /
CLOSURE REVIEW. Formal repair closure and audit resumption require
later separate authorization.

## 4. Formal ACM-01-F03 repair closure — 2026-09-30

### Independent final review and subsequent Architect authorization

Sol's independent final architecture/closure review returned exactly:

`PASS_ACM_01_F03_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`

The review confirmed that the original
`frc.robot.util.LearningTrajectoryFactory` finding was valid, the
canonical `frc.robot.commands.auto.LearningTrajectoryFactory`
destination is correct under AGENTS Section 4 and authoritative
Document A Sections 2 and 8, and the F03 package repair is independent
of the parked F04 command-family finding. It found no substantive
technical, verification, documentation or architecture requirement
remaining. After that accepted review, the Architect explicitly
authorized ACM-01-F03 REPAIR CLOSURE. That authorization is now
formally recorded: **ACM-01-F03: CLOSED**.

| F03 closure dimension | Final classification | Remaining requirement |
| --- | --- | --- |
| TECHNICAL | CLOSED | NONE |
| VERIFICATION | CLOSED | NONE |
| DOCUMENTATION | CLOSED | NONE |
| ARCHITECTURE | CLOSED | NONE |

The four CLOSED dimension classifications are the basis of this
formal F03 closure. Earlier Sections 1-3 retain their registration,
implementation, User verification and pre-closure reconciliation
stage meanings; their former PENDING and NOT CLOSED wording remains
historical truth for those stages.

### Preserved technical and verification basis

Registration in this ADR and eight lifecycle ledgers preceded Java
changes. M00_L16 alone received the prospective repair. Exactly four
existing Java identities changed and zero new Java identities were
introduced: `LearningTrajectoryFactory.java` moved to
`commands/auto` with only its package declaration changed;
`LearningTrajectoryFactoryTest.java` moved with its package,
necessary imports and one narrow placement guard; the Holonomic
follower and Swerve Simulation integration tests changed only their
factory imports. Ten pre-existing dedicated behavioral tests and
helpers remained intact. The guard checks the runtime qualified name,
new lesson-local source path, absent old source path and old class
absence through negative `Class.forName(...)`. No fifth F03
Java identity, production caller, compatibility alias, production
method-body change, Constants change, trajectory tuning, PathPlanner
conversion or new autonomous capability was introduced.
Reverse-normalized static comparisons matched all four pre-F03
working-state identities while preserving accepted F01/F02 differences.

User-owned verification used
`C:\Users\Public\wpilib\2026\jdk` and OpenJDK Temurin
17.0.16+8. Gate 1 clean plus focused factory test PASSed;
Gate 2 affected consumer tests PASSed; Gate 3 full-suite Gradle gate
PASSed with `4 actionable tasks: 4 up-to-date`; Gate 4 clean
build PASSed with `7 actionable tasks: 7 executed` and the User
marker `ALL REQUIRED F03 USER AUTOMATED VERIFICATION GATES PASS`.
Gate 3 is not represented as fresh execution of every test in that
invocation. Gate 4 supplies clean-state project execution evidence.
Actionable task counts are not test counts; no unseen exit code is
inferred. Sol's independent post-implementation review and the
Section 3 documentation reconciliation PASSed before the final
closure review. Interactive Simulation and real hardware were not
required for F03 closure; Glass and Driver Station were not applicable.

### Architecture, historical and protected boundaries

The Frozen Backbone remains intact: RobotContainer composition root;
vendor APIs in concrete IO adapters; subsystems through IO contracts;
Observation and telemetry hierarchies; scheduler-managed commands;
Constants authority; explicit safe stop; Real/Simulation selection;
CommandScheduler requirements; no manual ownership flags or
scheduler-polling arbitration. Historical A01, V00 and M00 predecessor
copies beginning at A01_L03 remain unchanged. The current M00_L16
correction does not rewrite historical lessons or inherited guides.

F04 remains ACCEPTED / PARKED. `AutonomousRoutineFactory`,
`AutonomousEventRegistration` and the broader F04 family were
not changed or designed here. The unrelated
A01_L06_OneMeter_Forward.path retains its pre-existing modified
state; no protected-file write was reported. An aggregate
protected-content digest match was NOT established, and no aggregate
digest PASS or defect is inferred from that evidence limit.
`github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

F01 and F01-DOC-01 remain CLOSED; F02 CLOSED / FORMALLY RECORDED;
P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED; CF-U and PF-U
CAUSE NOT ESTABLISHED. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED. ACM-01 and Phase 3 remain HOLD /
`HOLD_REPOSITORY_WIDE_AUDIT_PHASE_3_ARCHITECTURE`;
ACM-02 NOT STARTED; Phase 4 NOT STARTED / FORBIDDEN. No M00_L17.
Formal F03 closure does not re-freeze or republish M00_L16, close
ACM-01, activate F04, start ACM-02 or Phase 4, or authorize
Constants.java cleanup/refactor.

**Remaining ACM-01-F03 repair requirements:** NONE.

**Exact next gate:** ARCHITECT ACTIVATION / DESIGN AUTHORIZATION FOR
ACM-01-F04. This record does not perform that activation or design.

## Version history

| Version | Date | Decision |
| --- | --- | --- |
| 1.0 | 2026-09-30 | Architect-approved F03 target, exact four-identity registration and future User verification plan; implementation and verification pending. |
| 1.1 | 2026-09-30 | Bounded four-identity implementation and static self-audit recorded; User verification and independent review pending. |
| 1.2 | 2026-09-30 | Accepted User Java-17 gates, Sol independent review and bounded documentation/evidence reconciliation recorded; F03 pending independent final closure review, NOT CLOSED. |
| 1.3 | 2026-09-30 | Independent final closure review PASS, subsequent explicit Architect ACM-01-F03 repair-closure authorization, and bounded formal recording; F03 CLOSED with all four dimensions CLOSED and no remaining repair requirements. |
