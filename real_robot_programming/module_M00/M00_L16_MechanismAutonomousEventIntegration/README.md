# M00_L16 — Mechanism Autonomous Event Integration
<!-- ACM-06 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-06 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-06 — Command Semantics + CommandScheduler Requirements — after Sol's independent read-only audit token `PASS_ACM_06_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. The Architect owns the closure decision; Sol supplied independent evidence. **ACM-06: FORMALLY CLOSED / FORMALLY RECORDED. All forty audit dimensions are CLOSED. ACM-06-F01: NOT ESTABLISHED.** No repair or repair ADR is required.

Governance preflight PASS: 12 authoritative source PDFs, 12 matching hashes, zero deterministic findings; semantic fidelity certification was not performed. The audit inventoried all twenty current production `Command` subclasses: nineteen declare exact requirements for the subsystem semantic state they change; `AutonomousEventDemonstrationCommand` is the sole zero-requirement command and changes no subsystem state. No under-claim or materially incorrect over-claim was established. Commands use subsystem APIs and project observations, with no direct vendor API, concrete adapter, or mutable IOInputs access.

Production direct `CommandScheduler` calls remain in Robot lifecycle integration (`run`, autonomous schedule, Test-mode `cancelAll`); `teleopInit` cancels the autonomous command through command lifecycle semantics. No scheduler polling, manual ownership flag/lock, or dynamic ownership transfer is used for arbitration. The Swerve default and active bindings carry their subsystem requirements. `LEARNING_EVENT` supplies a fresh `IntakeToFeederCommand` with an exact Intake + Feeder deferred requirement set. Autonomous command compositions preserve Swerve ownership; stop/output safety beyond requirement ownership remains ACM-07. Existing requirement and scheduling tests were reviewed but not run.

ACM-01 through ACM-05 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED, with their recorded checkpoint commits and tags preserved in [AGENTS.md](../../../AGENTS.md). No regression was established through ACM-05. ACM-07 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; ACM-08 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. No Constants.java cleanup/refactor is authorized. ACM-06 is ready for the later User-owned Git checkpoint workflow; no ACM-06 commit or tag identity exists. This is documentation-only; no project execution or Git write occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-06 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-05 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-05 formal domain closure — 2026-10-01

The Architect authorized formal closure of ACM-05 — Observation / IOInputs Data Flow — after Sol's independent read-only audit: `PASS_ACM_05_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. The Architect owns the decision; Sol supplied evidence. **ACM-05: FORMALLY CLOSED / FORMALLY RECORDED; all thirty-one audit dimensions CLOSED. ACM-05-F01: NOT ESTABLISHED.** No repair is required.

The ACM-04 closure block below preserves its earlier stage record; its ACM-05 next-domain wording predates this closure and does not control the current cursor.

The governance preflight passed with 12 source PDFs, 12 matching hashes, and zero deterministic findings; semantic fidelity certification was not performed. Current M00_L16 preserves the one-way IO → subsystem-owned IOInputs → immutable observation → consumer path. All seven IO families were inventoried; every subsystem owns its Inputs and refreshes before its periodic observation. No production Inputs escape, alias, second owner, or bypass was established.

The fourteen top-level read models are SwerveObservation, VisionObservation, QualifiedVisionMeasurement, VisionTiming, VisionMeasurementQuality, VisionFusionObservation, IntakeObservation, FeederObservation, FlywheelObservation, ElevatorObservation, DriverInputObservation, DriveThreeMeterValidationObservation, AutonomousEventObservation, and AutonomousPreparationObservation. Vision copies its target list and target values. Telemetry and commands use observations or semantic project APIs. Noop and Swerve/Gyro/Vision simulation preserve the boundary. Feeder flow: FeederIO → FeederIOInputs → FeederSubsystem → FeederObservation; escape NONE ESTABLISHED; CORRECT. Detailed evidence is in [AGENTS.md](../../../AGENTS.md).

ACM-01 through ACM-04 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED. At the ACM-05 checkpoint, ACM-06 was NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; that cursor was superseded by the formal ACM-06 closure recorded above. ACM-07 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. The User checkpointed and pushed ACM-05 at `6c125c2490c50b9f2c0151379307ac72d10c0b22` with annotated tag `audit-acm-05-closed`; its remote target was verified by the User. Documentation-only recording; no Git write or project execution occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-05 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-04 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-04 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-04 — Subsystem → IO Contract Dependency — after Sol's independent read-only audit. Sol's accepted audit token is:
`PASS_ACM_04_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`
The Architect owns the closure decision; Sol supplied independent evidence.

**ACM-04: FORMALLY CLOSED / FORMALLY RECORDED.** All twenty-three closure dimensions are CLOSED. **ACM-04-F01: NOT ESTABLISHED**; no current subsystem-to-IO dependency defect, repair, or repair ADR exists. No implementation repair is required.

Static governance preflight PASS: 12 authoritative source PDFs, 12 matching source hashes, and zero deterministic findings. The validator does not certify semantic fidelity. The ACM-03, ACM-02, and ACM-01 closure blocks below preserve their stage records; their former next-domain cursor wording predates ACM-04 closure and does not control current status.

The complete current `frc.robot.subsystems` inventory is SwerveSubsystem, VisionSubsystem, IntakeSubsystem, FeederSubsystem, FlywheelSubsystem, and ElevatorSubsystem. Each receives and stores external IO through its project contract: SwerveSubsystem has four `SwerveModuleIO` dependencies and one `GyroIO`; VisionSubsystem uses `VisionIO`; each mechanism subsystem uses its matching `IntakeIO`, `FeederIO`, `FlywheelIO`, or `ElevatorIO`. ElevatorSubsystem may also use local `ElevatorTravelLimits`. Swerve kinematics, estimator, timer, output pipeline, and state/value objects are internal details. Vision field-layout, camera transform, quality policy, age limit, and time supplier are non-concrete-IO dependencies. This closure makes no ACM-05 IOInputs-flow or ACM-09 estimator-ownership claim.

The current production subsystem-source sweep found zero references to each concrete external IO class: `SwerveModuleIOCTRE`, `SwerveModuleIOSim`, `SwerveModuleIONoop`, `GyroIOPigeon2`, `GyroIOSim`, `GyroIONoop`, `VisionIOLimelight`, `VisionIOSim`, `IntakeIONoop`, `FeederIONoop`, `FlywheelIONoop`, and `ElevatorIONoop`. No concrete-IO imports, fully qualified names, construction, fields, generic dependencies, casts, `instanceof` checks, reflection/class loading, or adapter-specific calls were found. There is no static/global IO lookup, service locator, registry, singleton adapter access, RobotContainer lookup, subsystem-owned composition of another subsystem, or concrete-IO type in public/protected subsystem APIs.

`RobotContainer` remains the composition root and selects implementations before injection: CTRE or simulation swerve modules, Pigeon2 or simulation gyro, Limelight or simulation vision, and current mechanism Noop IOs. The selected classes implement their expected project interfaces; additional swerve and gyro Noop classes also implement their contracts, though RobotContainer does not select them. Current Noop mechanism selection and the absence of real mechanism adapters do not constitute ACM-04 defects. Test-only fake, recording, simulation, and Noop construction is not production coupling. Historical predecessor lessons remain unchanged.

ACM-01 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `88b36ad22560e5bf08f1dc1365bed86efaaa68b8` (`audit-acm-01-closed`). ACM-02 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `25b01017f2a854b1370c192729cc3c63beaab930` (`audit-acm-02-closed`); ACM-02-F01 remains NOT ESTABLISHED. ACM-03 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `849dc94061e29b80cf75e199bfc231659e2551c5` (`audit-acm-03-closed`); ACM-03-F01 remains NOT ESTABLISHED, and the User verified the remote main and annotated tag target at that commit. No regression was established in ACM-01 through ACM-03.

ACM-05 is the next prospective domain: NOT STARTED / NOT ACTIVATED. ACM-06 through ACM-12 remain NOT STARTED; this closure makes no later-domain closure claim. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. No M00_L17. No Constants.java cleanup/refactor is authorized. This documentation-only closure reached a checkpoint suitable for a later User-owned Git workflow; no ACM-04 commit or tag identity exists. No Java, tests, authoritative A/B/C sources, governance manifest/mirrors, historical lesson source, or protected/unrelated files were changed. No Gradle, tests, build, Simulation, Glass, Driver Station, or hardware execution occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-04 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-03 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-03 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-03 — Vendor API → Concrete IO Adapter Boundary — after Sol's independent read-only audit. Sol's accepted audit token is:
PASS_ACM_03_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW

ACM-03: FORMALLY CLOSED / FORMALLY RECORDED. All twenty-one closure dimensions are CLOSED. ACM-03-F01: NOT ESTABLISHED; no vendor-boundary defect, repair, or repair ADR exists. The audit covered all 113 current production Java files; governance mirror preflight PASS: 12 source PDFs, 12 matching source hashes, zero deterministic findings.

CTRE Phoenix 6 production use remains inside frc.robot.io.swerve.SwerveModuleIOCTRE and frc.robot.io.gyro.GyroIOPigeon2. The former owns the current TalonFX, CANcoder, Phoenix status signals, configuration objects, and control requests; the latter owns Pigeon2, gyro configuration/readback, and orientation/status signals. NeutralModeValue is not present in current production imports. Limelight JSON acquisition through NetworkTables remains inside frc.robot.io.vision.VisionIOLimelight, which converts camera data to project VisionIOInputs. Telemetry NetworkTables publishers remain project telemetry infrastructure. No Limelight protocol access or CTRE hardware API was found in current subsystems, commands, observations, or telemetry facades.

Current IO contracts expose vendor-neutral primitives, project/domain values, and WPILib geometry. No vendor-type escape was found. RobotContainer selects project adapter classes without directly constructing or manipulating CTRE devices/signals/control requests. Utility, controls, autonomous factories/coordinators and related consumers have no hardware-vendor ownership. The sweep found no current production REV, Kauai, PhotonVision, or other additional hardware-vendor package, and no hidden/FQCN/reflection vendor leakage. PathPlanner remains autonomous integration, not hardware-device vendor ownership. Test-only Phoenix/NetworkTables use in SwerveModuleIOCTREConfigurationTest and VisionIOLimelightTest remains bounded adapter verification. Historical predecessor lessons remain historical; no backward repair is authorized or required.

The observed current paths are SwerveSubsystem → SwerveModuleIO / GyroIO → SwerveModuleIOCTRE / GyroIOPigeon2 → CTRE Phoenix; VisionSubsystem → VisionIO → VisionIOLimelight → Limelight NetworkTables JSON; and mechanism subsystems → project mechanism IO → current Noop implementations. No real mechanism vendor adapter exists in current M00_L16.

ACM-01 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED. F01 is CLOSED; F01-DOC-01 is CLOSED; F02 and F03 are CLOSED / FORMALLY RECORDED; F04 is FORMALLY CLOSED / FORMALLY RECORDED; F05 is NOT ESTABLISHED. ACM-02 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED; ACM-02-F01 remains NOT ESTABLISHED. Their checkpoint commits are 88b36ad22560e5bf08f1dc1365bed86efaaa68b8 (audit-acm-01-closed) and 25b01017f2a854b1370c192729cc3c63beaab930 (audit-acm-02-closed).

ACM-04 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED. ACM-05 through ACM-12 remain NOT STARTED; this closure makes no later-domain closure claim. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. No M00_L17. This record does not authorize re-freeze, publication, Constants.java cleanup/refactor, ACM-04 activation, or Phase 4.

This is a documentation-only lifecycle record. No Java/tests, authoritative A/B/C documents, governance manifest/mirrors, historical lesson source, dependencies, deployment assets, or protected/unrelated files were changed. No Gradle, tests, build, Simulation, Glass, Driver Station, or hardware execution occurred. ACM-03 has reached a domain-closure checkpoint suitable for the later User-owned Git workflow; no ACM-03 commit or tag identity exists. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-03 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-02 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-02 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-02 — Composition Root / RobotContainer Ownership — after Sol's independent read-only audit. Sol's accepted audit token is:
PASS_ACM_02_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW

ACM-02: FORMALLY CLOSED / FORMALLY RECORDED. All seventeen closure dimensions are CLOSED. ACM-02-F01: NOT ESTABLISHED; no ownership defect, repair, or repair ADR exists.

The audit confirmed that Robot.java constructs one RobotContainer and retains lifecycle/scheduler responsibilities. RobotContainer owns current production subsystem construction, real/simulation IO selection, command and autonomous dependency wiring, the inspected default command and controller bindings, event registration, and telemetry collaborators. Subsystems receive external IO; internal helpers remain mechanism details. Commands and autonomous factories use supplied dependencies. No alternate production graph, static mutable subsystem/IO owner, RobotContainer lookup, or duplicate major subsystem construction was found. Independent test fixtures remain test-only.

ACM-01 and its F01–F04 closures remain preserved; F05 remains NOT ESTABLISHED. ACM-03 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; ACM-04 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. No M00_L17. ACM-02 closure makes no later-domain closure claim and does not authorize re-freeze, publication, Constants.java cleanup/refactor, ACM-03 activation, or Phase 4.

Static governance preflight PASS: 12 source PDFs, 12 matching source hashes, zero deterministic findings. This closure is a documentation-only lifecycle record. No ACM-02 Git commit or tag identity exists; the checkpoint is ready for the later User-owned Git workflow.

<!-- ACM-02 DOMAIN CLOSURE CURRENT END -->

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

F01 and F01-DOC-01 remain CLOSED; F02 and F03 remain CLOSED / FORMALLY RECORDED; F04 remains FORMALLY CLOSED / FORMALLY RECORDED. The 14-member F04 production family and its dedicated tests remain in commands.auto; the exact family is preserved in [the F04 repair record, Sections 1 and 4](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md). LearningTrajectoryFactory and its dedicated test remain in commands.auto.

Independent static inventory: 113 production Java files and 109 test Java files
scanned; zero package declaration/path mismatches. These are source-file counts,
not test counts. Repaired tests remain colocated. Old F01-F04 qualified identities
in placement assertions are intentional negative checks, not stale executable
imports. M00_L16 remains a continuation of M00_L15 with one teaching concept: dispatch the existing IntakeToFeederCommand through the inherited LEARNING_EVENT boundary. No ACM-01 repair introduced a teaching concept. Predecessor lesson copies remain historical; no repair was propagated backward. The narrow Frozen Backbone rereview found the composition root, concrete IO ownership, subsystem APIs, observation and telemetry hierarchies, scheduler-managed commands, explicit stop paths, Real/Simulation implementations, and Constants authority intact; it found no manual ownership flags or scheduler-polling arbitration. This is not a closure claim for later ACM domains.

CF-U and PF-U remain CAUSE NOT ESTABLISHED. The unrelated A01_L06_OneMeter_Forward.path state remains untouched. Prior protection evidence covers named-file hashes only; no aggregate protected-content digest PASS is claimed. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.

Post-ACM-01-closure cursor at that checkpoint: ACM-01 FORMALLY CLOSED / FORMALLY RECORDED; ACM-02 was prospective, NOT STARTED and NOT ACTIVATED; ACM-03 through ACM-12 were NOT STARTED. The current post-ACM-02 cursor is recorded above. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. No M00_L17. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. ACM-02 closure does not authorize re-freeze, publication, Constants.java cleanup/refactor, ACM-03 activation, or Phase 4.

The ACM-01 checkpoint was later created and pushed by the User as commit `88b36ad22560e5bf08f1dc1365bed86efaaa68b8`, with annotated tag `audit-acm-01-closed`; the local tag resolves to that commit, and the User-provided checkpoint record states that the remote tag target was verified. ACM-02 has reached a separate domain-closure checkpoint suitable for the later User-owned Git workflow; no ACM-02 commit or tag identity exists. The ACM-01 recording changed existing lifecycle documentation only. No Git write or project execution occurred during that recording.

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
[The F04 ADR, Section 2](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md#2-bounded-implementation-and-static-self-audit--2026-09-30) records this static self-audit.
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
family. [The F04 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md) records each type and historical
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
[The F03 ADR, Section 4](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md#4-formal-acm-01-f03-repair-closure--2026-09-30) governs this decision. The
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
[The F03 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md) now reconciles the accepted evidence.

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
Document A Sections 2 and 8. [The dedicated F03 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md)
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

<!-- ACM-01-F03 REGISTRATION END -->

<!-- ACM-01-F02 CURRENT BEGIN -->
## Current ACM-01-F02 formal repair closure — 2026-09-30

The Architect explicitly authorized ACM-01-F02 REPAIR CLOSURE after the
accepted independent final architecture/closure review:
`PASS_ACM_01_F02_FINAL_CLOSURE_REVIEW_READY_FOR_ARCHITECT_CLOSURE_AUTHORIZATION`.
The subsequent authorization is now formally recorded. **ACM-01-F02: CLOSED**.
Technical, verification, documentation and architecture dimensions are
**CLOSED**. Remaining F02 repair requirements: **NONE**.
The governing closure decision is [the F02 ADR, Section 3](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#3-formal-acm-01-f02-repair-closure--2026-09-30).

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

<!-- ACM-01-F02 CURRENT END -->

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT BEGIN -->
## Historical ACM-01-F02 documentation/evidence reconciliation — 2026-09-30

The Architect-authorized F02 documentation/evidence reconciliation is
complete. The accepted package-only implementation is five changed existing
Java identities plus one focused placement test; all required User automated
gates and independent implementation review PASS. The initial Java 8
configuration failure and corrected Java 17 sequence are preserved in
[the F02 ADR, Section 2](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#2-accepted-implementation-and-evidence-reconciliation--2026-09-30).
The registration-stage record below remains historical stage evidence.

F02 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
F02 is NOT CLOSED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED. F01 and DOC-01 remain
CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED.
Exact next gate: INDEPENDENT FINAL ACM-01-F02 ARCHITECTURE / CLOSURE REVIEW.

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT END -->

<!-- ACM-01-F02 REGISTRATION BEGIN -->
## ACM-01-F02 exceptional repair registration — 2026-09-30

This is the pre-implementation registration snapshot. The Architect accepted
the inherited S00_L23 drive validation observation placement finding under
Document C OC-02 Section 1 and authorized only the M00_L16 package move to
`frc.robot.observation.swerve`, three production import changes, one
existing test import change, and one new focused placement test. The exact
six-Java-identity boundary and fail-stop verification sequence are in
[the F02 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md).

At registration, F02 implementation, verification, independent review, and
closure are PENDING. F01 and DOC-01 remain CLOSED; F03/F04 are ACCEPTED /
PARKED. M00_L16 is IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; ACM-01
HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED / FORBIDDEN. Historical
lessons remain untouched. Next: bounded F02 implementation and User
automated verification, then independent review.

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
- Governing chronology, evidence and closure: [dedicated ACM-01-F01 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

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
- Governing chronology/evidence: [dedicated ACM-01-F01 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

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
- Governing record: [ACM-01-F01 package repair](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).
- Next gate: Architect review of the compile failure and protected-state fingerprint discrepancy; no further execution or scope expansion.
- Full documentation reconciliation, closure, re-freeze, new publication and audit resumption remain later gates. No M00_L17.

<!-- ACM-01-F01 FIRST-STAGE SNAPSHOT END -->


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

## Historical lifecycle — pre-Commit-2 publication metadata reconciliation, 2026-09-27

M00_L16 remains COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED. Active M00
lesson count is 0; Current Active M00 Lesson is NONE. Independent Freeze Review
passed as `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW` with verdict
`M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
The User created Primary Frozen Snapshot Commit 1 at
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata
reconciliation is COMPLETE / `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`,
awaiting the separate User-owned Metadata Publication Commit 2. Commit 2, its
hash, publication push, and final external publication verification remain
PENDING; no remote publication is claimed. Evidence remains THEORY VERIFIED /
SIMULATION VERIFIED / REAL HARDWARE DEFERRED. The initial closure HOLD,
bounded README repair, fresh closure rereview PASS, and Freeze Reconciliation
PASS remain recorded below. The two-commit Historical Snapshot model requires
no third verification-only commit. M00_L16 is the final M00 lesson; no
M00_L17 is authorized.

## Historical lifecycle — Freeze Reconciliation, 2026-09-27

M00_L16 is `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`. Active M00 lesson
count is 0; Current Active M00 Lesson is NONE. M00_L15 remains COMPLETE /
FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Baseline, inheritance, Final
Design Lock, controlled activation, independent activation review,
implementation authorization, implementation, bounded architecture-test
repair, independent static rereview, and independent closure review passed.

The one implemented concept binds the inherited `LEARNING_EVENT` to a fresh
`IntakeToFeederCommand(intakeSubsystem, feederSubsystem)` per dispatch through
the existing deferred registration. The exact event requirements are Intake
and Feeder. Source comparison with L15 found 221 unchanged source files:
modified `RobotContainer.java`, modified `IntakeArchitectureBoundaryTest.java`,
and added `RobotContainerMechanismAutonomousEventIntegrationTest.java` with
exactly eight tests. The marker path, event-free control path, chooser, teleop
bindings, event helpers, and mechanism code remain unchanged.
The repaired architecture guard permits only the autonomous event reference;
its teleop, default-command, and direct-subsystem-access guards remain.
The eight tests cover exact requirements, a fresh child per dispatch,
scheduler-started requests, no reinitialization in one lifecycle,
cancellation cleanup (Feeder STOP then Intake STOP), Intake contention,
Feeder contention, and supplier `RuntimeException` leading to inherited
`FACTORY_FAILURE` safe no-op without mechanism actuation.

- Focused Tests: COMPLETE / PASS; architecture guard focused methods and all
  8 integration tests passed; BUILD SUCCESSFUL, exit code 0.
- Clean Regression: COMPLETE / PASS; BUILD SUCCESSFUL, no regression blocker.
  No numeric test count or exit code was supplied.
- Bounded Simulation: COMPLETE / `PASS_M00_L16_BOUNDED_SIMULATION`. Driver
  Station was attached; `ONE_METER_WITH_EVENT` ran Autonomous, with Intake
  `INTAKE_REQUESTED` and Feeder `FEED_REQUESTED` at `LEARNING_EVENT`, and
  Flywheel/Elevator STOPPED. Disable/interruption stopped Intake/Feeder. The
  `ONE_METER_PATH` control ran without an event and kept Intake/Feeder STOPPED.
  No fatal scheduler/runtime exception was observed; Simulation returned
  normally to the PowerShell prompt. Final Gradle output showed five actionable
  tasks, three executed and two up-to-date. No Simulation exit code was supplied.

Blank AutonomousEvent NT fields are expected under the inherited contract:
the facade publishes only when an observation exists; RobotContainer starts
with `Optional.empty`; the new command emits no lifecycle observation; and
registration emits only `FACTORY_FAILURE`. Neither `LastEvent=LEARNING_EVENT`
nor a dispatch count is claimed. No telemetry expansion is authorized.
Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.
No game-piece transfer, motor performance, sensor behavior, timing, or
electrical behavior is claimed. Documentation Reconciliation is COMPLETE /
`PASS_M00_L16_DOCUMENTATION_RECONCILIATION`. The first Independent Closure
Review returned `HOLD_M00_L16_INDEPENDENT_CLOSURE_REVIEW` for the README
historical/current hierarchy; the bounded README repair passed, and the fresh
Independent Closure Review returned
`PASS_M00_L16_INDEPENDENT_CLOSURE_REVIEW` with no remaining findings.
Freeze Reconciliation is COMPLETE /
`PASS_M00_L16_FREEZE_RECONCILIATION`. Independent Freeze Review is PENDING.
User-owned publication is PENDING / NOT PUBLISHED. Evidence remains THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. M00_L16 is the final
M00 lesson; no M00_L17 is authorized.

## Historical lifecycle — Controlled Activation, 2026-09-27

M00_L16 is the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`
M00 lesson. M00_L15 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED` at accepted primary SHA
`15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa` and metadata SHA
`0d3685ce67a0b985459392621e003611eaa6dc35`, with final gate
`PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`.

The L16 untouched-copy baseline passed (User-reported BUILD SUCCESSFUL,
exit code 0); the inheritance audit passed with 345/345 authored files,
113/113 production Java files, and 106/106 test Java files identical.
The Final Design Lock passed under `PASS_M00_L16_FINAL_DESIGN_LOCK`.
Controlled activation changes documentation only. Independent activation
review and implementation authorization remain PENDING. Implementation,
focused tests, clean regression, bounded Simulation, closure, freeze, and
publication remain PENDING. Real hardware is DEFERRED.

### Historical design lock and implementation plan — superseded by the current results above

The one concept is scheduler-managed dispatch of one already-verified
mechanism command through the existing PathPlanner named-event boundary.
The selected M00_L15 command is `IntakeToFeederCommand`; it already owns
Intake and Feeder requirements, verified startup and cleanup semantics,
and needs no new numeric caller setpoint. `ShootCommand` is excluded
because it introduces an RPM configuration question.

The existing `LEARNING_EVENT` identity and marker remain unchanged. The
later production delta is exactly one modified file:
`src/main/java/frc/robot/RobotContainer.java`. Its binding must supply a
fresh `IntakeToFeederCommand` for each dispatch and expose exactly
IntakeSubsystem and FeederSubsystem requirements through the inherited
`Supplier<Command>` and `Commands.defer(...)` registration. WPILib
requirements arbitrate contention with other robot commands; PathPlanner's
inherited EventScheduler manages the event child. No new event ID, second
event, manual busy flag, timer, timeout, wrapper, or path change is approved.

`AutonomousEventRegistration`, `AutonomousEventBinding`,
`AutonomousEventId`, `IntakeToFeederCommand`, all mechanism subsystems
and IO, Observations, telemetry, AutoBuilder, path factories, Swerve,
Vision, and pose estimation remain unchanged. RobotContainer retains
Right Bumper `RunIntakeCommand whileTrue` and Left Bumper
`RunFeederCommand whileTrue`; no driver or default coordination binding
is added. Chooser options remain `SAFE_STOP`, `ONE_METER_PATH`, and
`ONE_METER_WITH_EVENT`. The latter uses the existing
`A01_L09_OneMeter_With_Learning_Event.path`; the former is event-free.

`IntakeToFeederCommand` is a hold command with `isFinished() == false`.
For normal path completion or interruption, the inherited chain is
`FollowPathCommand.end(...)` → `EventScheduler.end()` → active event
`end(true)` → wrapped registered event → `DeferredCommand.end(true)`
→ `IntakeToFeederCommand.end(...)` → `feeder.stop()` then
`intake.stop()`. No guaranteed cleanup is claimed after an arbitrary
uncaught library exception. If the child supplier throws
`RuntimeException`, the existing registration publishes
`FACTORY_FAILURE` and supplies a safe no-op without mechanism actuation.

### Historical pre-verification test and Simulation plan — superseded by the current results above

Add only `src/test/java/frc/robot/RobotContainerMechanismAutonomousEventIntegrationTest.java`
with exactly eight `@Test` methods: exact event requirements, fresh child
per dispatch, real scheduler startup requests, no repeated initialization,
cancellation cleanup, Intake contention, Feeder contention, and nonempty
requirement supplier failure safe no-op. Update only the stale L15
`src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java` expectation
that forbids any RobotContainer reference to `IntakeToFeederCommand`;
preserve its teleop, default-command, and direct-subsystem-access guards.
No tests are changed by activation.

Later bounded Simulation must start and attach Driver Station, show Intake
and Feeder STOPPED before Autonomous, select `ONE_METER_WITH_EVENT`,
observe both semantic requests at `LEARNING_EVENT`, observe Feeder then
Intake STOPPED after path completion/interruption and after Disabled, show
no fatal scheduler exception, and exit successfully. `ONE_METER_PATH`
must not dispatch the mechanism event. These are future observations,
not current L16 results. Noop adapters and RequestedState telemetry do
not prove game-piece acquisition, transfer, motor performance, timing,
sensor correctness, or real hardware behavior.

## Inherited M00_L15 README (historical copy)

The copied material below records M00_L15's earlier lifecycle. Its
then-current publication and active-lesson statements are historical and
do not supersede the current M00_L16 record above.

### Historical lifecycle — M00_L15 publication metadata reconciliation, 2026-09-27

- Lifecycle: COMPLETE / FROZEN / READ-ONLY
- Active M00 lesson count: 0; current active lesson: NONE
- Independent Closure Rereview: PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW; M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION
- Freeze Reconciliation: COMPLETE / PASS_M00_L15_FREEZE_RECONCILIATION
- Independent Freeze Rereview: PASS_M00_L15_INDEPENDENT_FREEZE_REREVIEW; M00_L15_INDEPENDENT_FREEZE_REREVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW
- Primary Snapshot Commit: `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`
- Primary Snapshot Gate: PASS_M00_L15_PRIMARY_SNAPSHOT_COMMIT
- Publication Metadata Reconciliation: COMPLETE / PREPARED FOR USER METADATA COMMIT 2
- Metadata Publication Commit: PENDING / USER-OWNED
- Publication Push: PENDING / USER-OWNED
- Publication: PENDING / NOT PUBLISHED
- Final Publication Verification: PENDING
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- M00_L16: FUTURE / INACTIVE / NOT CREATED

The implementation and verification record below remains unchanged. Freeze
reconciliation did not modify source or tests, and the current metadata
reconciliation is documentation-only. It preserves the focused 22 / 22 PASS
result, the 830 / 830 PASS clean regression, and bounded Simulation / DS PASS.
Simulation did not schedule `IntakeToFeederCommand`; scheduler behavior is
verified by the focused tests. Real hardware remains deferred.

## Historical lifecycle — Implementation and verification reconciliation, 2026-09-27

- Lesson: M00_L15 — Intake-to-Feeder Coordination
- Previous lesson: M00_L14 — Shoot Coordination
- Previous lesson state: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Previous primary SHA: `fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`
- Previous metadata SHA: `1a85c0827ee91ba7f70a595d94c49ad16a6df9cb`
- Lifecycle: IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
- Active M00 lesson count: 1; current active lesson: M00_L15
- Untouched-copy baseline: PASS_M00_L15_UNTOUCHED_COPY_BASELINE_BUILD; BUILD SUCCESSFUL in 53s; 6 actionable tasks, all executed; exit code 0
- Architecture / Inheritance Audit: PASS_M00_L15_ARCHITECTURE_INHERITANCE_AUDIT; 341/341 authored files identical to M00_L14
- Final Design Lock: PASS_M00_L15_FINAL_DESIGN_LOCK
- Controlled Activation: PASS_M00_L15_CONTROLLED_ACTIVATION
- Independent Activation Review / Rereview: PASS_M00_L15_INDEPENDENT_ACTIVATION_REREVIEW
- Implementation Authorization / Handoff: PASS_M00_L15_IMPLEMENTATION_AUTHORIZATION / PASS_M00_L15_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW
- Implementation: COMPLETE; production addition is only IntakeToFeederCommand.java
- Final Independent Static Review: PASS_M00_L15_FINAL_INDEPENDENT_STATIC_REVIEW
- Focused tests: PASS_M00_L15_USER_FOCUSED_TESTS; 22 / 22 PASS; 0 failures, 0 errors, 0 skipped
- Clean regression: PASS_M00_L15_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL; 830 tests, 0 failures, 0 errors, 0 skipped; BUILD_EXIT_CODE=0
- Bounded Simulation / Driver Station: PASS_M00_L15_BOUNDED_SIMULATION_DS_VERIFICATION; startup and DS attachment; Disabled Robot Enabled=No with Intake, Feeder, and Flywheel STOPPED; Teleoperated Robot Enabled=Yes and DS Attached=Yes with Intake and Feeder STOPPED; return to Disabled with Robot Enabled=No and Intake and Feeder STOPPED; no unintended L15 activation or fatal runtime/scheduler error; BUILD SUCCESSFUL; SIMULATION_EXIT_CODE=0
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- Documentation Reconciliation: COMPLETE; Independent Closure Review is the next gate
- Freeze / Publication: PENDING; M00_L15 remains active and is not frozen or published
- Real hardware: DEFERRED
- M00_L16: FUTURE / INACTIVE / NOT CREATED

## One new concept

Scheduler-managed coordination of the existing Intake and Feeder semantic
behaviors under one command lifecycle. This is software coordination only; it
does not establish physical mechanism movement, game-piece presence, transfer
completion, or hardware timing.

## Locked command contract

Add only `frc.robot.commands.IntakeToFeederCommand` with constructor
`IntakeToFeederCommand(IntakeSubsystem intake, FeederSubsystem feeder)`.
Reject either null dependency, retain the exact supplied subsystem references,
and require exactly IntakeSubsystem and FeederSubsystem. Construction performs
no mechanism output or subsystem mutation. Flywheel, Elevator, and Swerve are
excluded.

Each initialize lifecycle calls `intake.requestIntake()` once, then
`feeder.requestFeed()` once. `execute()` issues no repeated request;
`isFinished()` returns false. Both `end(false)` and `end(true)` stop Feeder
first and Intake second as deterministic software cleanup, with best-effort
attempts of both stops.

The locked exception policy handles RuntimeException only. If either initialize
request fails, preserve it as primary, do not continue to the next request, and
attempt `feeder.stop()` followed by `intake.stop()`. Suppress cleanup failures
on the primary in occurrence order. End always attempts both stops in the same
order; a Feeder stop failure remains primary and an Intake stop failure is
suppressed. An Intake-only end failure propagates. There are no retries,
fallback requests, swallowed primary failures, or Error catches.

## Focused test and evidence record

The focused test file is
`src/test/java/frc/robot/commands/IntakeToFeederCommandTest.java`. Its 22 test
methods comprise 18 direct-contract cases and four real CommandScheduler cases:
startup, cancellation, Intake requirement contention, and Feeder requirement
contention. The accepted result is 22 / 22 PASS, with no failures, errors, or
skips. Direct `end(true)` coverage proves method semantics only; the scheduler
cancellation case proves scheduler-driven interrupted cleanup.

The current M00_L15 copies of Feeder and Intake architecture boundary tests
were reconciled within this active lesson. The Feeder owner scan now uses the
JDK Java parser and syntax tree. The exact Java source sets are Intake-named
`{RunIntakeCommand.java, IntakeToFeederCommand.java}`, Feeder-named
`{RunFeederCommand.java, IntakeToFeederCommand.java}`, and FeederSubsystem
command owners `{RunFeederCommand.java, IntakeToFeederCommand.java,
ShootCommand.java}`. Frozen predecessor test files were not changed.

RobotContainer receives no M00_L15 binding. Preserve Right Bumper to
RunIntakeCommand and Left Bumper to RunFeederCommand. Do not add autonomous
bindings, NamedCommands, or PathPlanner event wiring. The scheduler's subsystem
requirements remain the only command-contention authority.

IntakeObservation and FeederObservation expose availability, connection, and
software requested state. Requested state is not proof of motor movement,
game-piece presence, transfer, or completion. The command does not gate its
semantic requests on Observations and adds no sensor assumptions, timer, delay,
debounce, current threshold, velocity threshold, or physical sequencing logic.

Because runtime Intake and Feeder implementations are Noop and RobotContainer
has no L15 binding, bounded Simulation did not execute this command. The
accepted Simulation verified startup and Driver Station attachment. In Disabled,
Robot Enabled was No and Intake, Feeder, and Flywheel RequestedState were
STOPPED. In Teleoperated, Robot Enabled and DS Attached were Yes and Intake and
Feeder remained STOPPED; no L15 command started automatically. On return to
Disabled, Robot Enabled was No and Intake and Feeder remained STOPPED, with no
unexpected mechanism semantic state active. No fatal runtime or scheduler error
occurred; the process ended with BUILD SUCCESSFUL and
SIMULATION_EXIT_CODE=0. The unassigned or unplugged controller on port 0
produced an EXPECTED / NON-BLOCKING button warning. Scheduler behavior is
covered by the focused tests. Real hardware remains DEFERRED.

## Historical next gate before closure rereview

At this earlier point, Independent Closure Review was PENDING and M00_L15 was
IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK. The accepted closure
rereview and current freeze state are recorded above and in the transition
guide. M00_L16 remains FUTURE / INACTIVE / NOT CREATED; no L17 is authorized.

---

## Inherited M00_L14 predecessor README (historical copy)

The following copied M00_L14 material is retained as predecessor context. Its
lesson identity and lifecycle statements are historical and do not describe
the current M00_L15 state.

### Historical M00_L14 frozen lifecycle and publication state — 2026-09-26

- Lesson: M00_L14 — Shoot Coordination
- Previous Lesson: M00_L13 — Elevator Travel-Limit Safety
- Previous Lesson State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- Previous primary SHA: 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704
- Previous metadata SHA: 658d1e44c417763df3689b9b52e409161446c593
- Status: COMPLETE / FROZEN / READ-ONLY
- Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / NOT ACTIVE
- Active Lesson Count: 0
- Current Active M00 Lesson: NONE
- Accepted preparation: PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD; 6 actionable tasks, 6 executed; BASELINE_BUILD_EXIT_CODE=0
- Architecture / Inheritance Audit: PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT
- Final Design Lock: PASS_M00_L14_FINAL_DESIGN_LOCK
- Controlled Activation: PASS_M00_L14_CONTROLLED_ACTIVATION
- Independent Activation Review: PASS_M00_L14_INDEPENDENT_ACTIVATION_REREVIEW
- M00_L15: FUTURE / INACTIVE / NOT CREATED
- M00_L16: FUTURE / INACTIVE / NOT CREATED
- Implementation Authorization: PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION
- Implementation Handoff: PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW
- Final Independent Static Review: PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW
- Focused Tests: PASS_M00_L14_USER_FOCUSED_TESTS; BUILD SUCCESSFUL in 10s; 4 actionable tasks (3 executed, 1 up-to-date); exit code 0
- Clean Regression: PASS_M00_L14_USER_CLEAN_REGRESSION; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); exit code 0
- Bounded Simulation: PASS_M00_L14_USER_BOUNDED_SIMULATION; Disabled -> Teleoperated enabled -> Disabled; clean shutdown
- Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- Initial Independent Closure Review: HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED
- Documentation Proof Reconciliation: PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION
- Independent Closure Rereview: PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW / INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION
- Documentation Reconciliation: PASS_M00_L14_DOCUMENTATION_RECONCILIATION
- Freeze Reconciliation: PASS_M00_L14_FREEZE_RECONCILIATION
- Independent Freeze Review: PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW / INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION
- Primary Frozen Snapshot: PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT; SHA fa34556a3f1b7ef52b2c678a39c1083392d7c8d3
- Metadata Publication Reconciliation: PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION
- Metadata Publication Commit: COMPLETED / PASS_M00_L14_METADATA_PUBLICATION_COMMIT; identity external and not self-embedded
- Metadata Publication Commit Amendment: COMPLETED / PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION / PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND; canonical identity external and not self-embedded
- Remote Push: PENDING / USER-OWNED
- Final Publication Verification: PENDING / EXTERNAL

## Historical pre-Commit-2 metadata publication preparation — bounded repair

This section records the accepted state before the User created Metadata
Publication Commit 2. Its pending-commit language is historical and is
superseded by the current lifecycle at the top of this README.

The accepted `HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA`
found that the prior preparation had no real metadata delta after Commit 1.
This bounded documentation repair supplied that delta. Following M00_L13, the
metadata state intended for User-owned Commit 2 recorded M00_L14 as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED`. At that historical point, the
pre-commit worktree awaited the User's Commit 2; no Commit 2 identity was
claimed. Its own final SHA was external publication evidence and could not be
self-embedded. Final Publication Verification was PENDING / EXTERNAL, and no
third verification-only commit was part of the model. M00_L14 was NOT ACTIVE;
Active Lesson Count was 0 and Current Active M00 Lesson was NONE. M00_L15
and M00_L16 remain FUTURE / INACTIVE / NOT CREATED.

## Closure and freeze chronology

The initial Closure Review HOLD concerned documentation and evidence wording
only; it identified no production, architecture, runtime, test, or Simulation
defect. The bounded repair changed only LESSON_STATUS.md, LESSON_PLAN.md, and
LESSON_CHECKLIST.md. Independent Closure Rereview passed, and documentation-only
Freeze Reconciliation recorded the complete, frozen, read-only state. M00_L15
and M00_L16 remain future, inactive, and uncreated.

## Locked concept and command

The sole concept is coordination logic: scheduler-managed
frc.robot.commands.ShootCommand coordinates existing Flywheel ready-at-speed
semantics with Feeder request semantics. It requires exactly
FlywheelSubsystem and FeederSubsystem for the full scheduled lifetime.

The constructor accepts non-null Flywheel and Feeder subsystems and finite,
strictly positive caller-supplied targetVelocityRpm. Invalid numeric targets
throw IllegalArgumentException. The target is semantic configuration, not an
authoritative real-hardware shooting RPM. Construction performs no output or
subsystem mutation.

FlywheelObservation.readyAtSpeed() is the only Flywheel readiness authority.
The command does not recalculate RPM error or tolerance, inspect raw readiness
inputs, add hysteresis or a readiness timer, or keep a second readiness flag.

Feed admission requires all three conditions: Flywheel readyAtSpeed(),
Feeder available(), and Feeder connected(). Transition deduplication uses
FeederObservation.requestedState() as software request state only;
FEED_REQUESTED does not prove physical game-piece transport. The command
keeps no command-local feedingRequested state.

## Locked lifecycle and exception behavior

On successful initialize(), the command calls feeder.stop() once and then
flywheel.requestVelocity(targetVelocityRpm) once. It does not feed during
initialization or reissue the Flywheel request from execute(). On normal
execute(), it requests feed only when admission is
true and Feeder state is not FEED_REQUESTED; it stops only when admission is
false and Feeder state is FEED_REQUESTED. No unchanged state causes output
reissue. Readiness loss stops Feeder while leaving Flywheel velocity intent
active on the normal path. Admission recovery may request feed again.

isFinished() is always false. There is no timer, timeout, feed duration, shot
counter, or completion detector.

Every end(false) and end(true) attempts Feeder stop followed by Flywheel stop,
even if Feeder stop throws. A sole stop failure is rethrown; when both throw,
Feeder failure remains primary and Flywheel failure is suppressed.

For an initial feeder.stop() RuntimeException, initialize() does not request
Flywheel velocity; it attempts flywheel.stop() once, suppresses any cleanup
failure on the original Feeder exception, and rethrows the original. If
flywheel.requestVelocity() throws, the Feeder baseline stop is already
complete; initialize() attempts flywheel.stop() once, suppresses any cleanup
failure on the original request exception, and rethrows it. If
feeder.requestFeed() throws in execute(), the Feeder intent may already be
FEED_REQUESTED; execute() attempts feeder.stop() once and flywheel.stop() once,
still attempting Flywheel cleanup if Feeder cleanup throws, suppresses cleanup
failures on the original request exception, and rethrows it. If feeder.stop()
throws in execute() during readiness/admission loss, execute() does not retry
that Feeder stop and attempts flywheel.stop() once; any cleanup failure is
suppressed on the original Feeder exception. Cleanup occurs in the failing
lifecycle method. RuntimeExceptions are not retried, silently recovered,
clamped, rewritten, or replaced by fallback behavior. java.lang.Error is not
caught as routine mechanism recovery.

## Protected boundaries and implementation delta

- Constants.java and RobotContainer.java remain unchanged; there is no L14 driver binding or provisional shooting RPM.
- The inherited Left Bumper manual Feeder command remains; scheduler requirement ownership provides Feeder mutual exclusion.
- Flywheel + Feeder are the only required subsystems. Elevator, Intake, and Swerve are excluded.
- Existing IO, Inputs, Observations, requested-state enums, telemetry, vendor adapters, and deploy files remain unchanged.
- M00_L15 owns Intake-to-Feeder Coordination.
- M00_L16 owns Mechanism Autonomous Event Integration; no NamedCommands or PathPlanner mechanism events enter L14.
- Vision aiming, physical shot detection, shot completion, new IO, ShooterSubsystem, and hardware calibration are excluded.
- Actual production delta: added only src/main/java/frc/robot/commands/ShootCommand.java; existing production files remain unchanged.
- The 18 focused tests use controlled IO fakes; arbitrary test RPM values are TEST DATA ONLY.
- Runtime Flywheel and Feeder adapters are Noop, so runtime Simulation cannot prove a physical shot.

## Accepted verification evidence and limits

The bounded Simulation verified Disabled startup (Robot Disabled, not enabled,
DS attached, not E-stopped), Teleoperated enable (Robot Teleoperated, enabled,
DS attached, not E-stopped), and return to Disabled (not enabled, DS attached,
not E-stopped) with Feeder, Flywheel, and Intake requested states STOPPED. No
application crash or scheduler/runtime exception was observed; normal process
shutdown completed with LAST_NATIVE_EXIT_CODE=0. It supports application
startup, mode transitions, scheduler/integration stability, safe semantic
mechanism state, and clean shutdown. RobotContainer is unchanged and has no
ShootCommand binding, so the Simulation did not directly execute ShootCommand.
No physical shot or hardware performance is established.

The focused unit test invokes `end(true)` through a direct lifecycle call and
checks interrupted-end cleanup. It does not test CommandScheduler scheduling
or cancellation of ShootCommand.

Five independent static-review HOLD/repair rounds addressed test-only
architecture-guard defects. No production defect was found; the final
independent static review passed. The HOLD/repair chronology and classifications
are recorded in LESSON_STATUS.md and the transition guide.

The transition guide records the accepted Independent Freeze Review and
primary snapshot, followed by metadata reconciliation, Commit 2 completion,
and its completed amendment. The canonical identity remains external rather
than self-embedded.
The earlier HOLD_M00_L14_FINAL_PUBLICATION_VERIFICATION_METADATA_COMMIT_STATE_RECONCILIATION_REQUIRED
was resolved by the accepted User amendment. The subsequent
HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED
identified stale current-state chronology and is addressed by this documentation
reconciliation. Final Publication Verification remains pending, remote push is
pending User-owned verification, and real hardware remains deferred.
