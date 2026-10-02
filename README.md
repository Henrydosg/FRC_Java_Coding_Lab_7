<!-- ACM-09 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-09 formal domain closure — 2026-10-02

The Architect reviewed Sol's independent post-repair domain rereview and explicitly authorized **ACM-09 FORMAL DOMAIN CLOSURE**. Accepted rereview token: `PASS_ACM_09_POST_REPAIR_REREVIEW_READY_FOR_ARCHITECT_DOMAIN_CLOSURE`. **ACM-09 is FORMALLY CLOSED / FORMALLY RECORDED: 55 / 55 dimensions CLOSED; 0 BLOCKED. ACM-09-F01 is CLOSED / REPAIRED / USER VERIFIED / INDEPENDENTLY REVIEWED / ARCHITECT REPAIR CLOSURE AUTHORIZED. ACM-09-F02 is NOT ESTABLISHED. NO ADDITIONAL ACM-09 FINDING ESTABLISHED.** The detailed 55-dimension matrix is recorded in the current ACM-09 closure block in AGENTS.md. Earlier F01 repair-closure and ACM-08 cursor wording below remains stage evidence and is superseded where it describes ACM-09 as pending or ACM-09 as the next domain.

SwerveSubsystem is the single drivetrain localization owner: it owns the captured heading reference, odometry O, pose estimator E, updates and recovery, known-field-pose reset, heading synchronization, vision admission, and authoritative estimated pose. O remains the secondary raw localization state; E is the authoritative fused field pose and may differ from O. Successful heading capture preserves O and E independently, prepares both replacement trackers with the new adjusted heading and current module positions before committing the new reference, advances the vision history/reset barrier, refreshes the localization observation coherently, and preserves field-relative behavior. Invalid required input fails closed; capture before tracker initialization creates no placeholder trackers, and later initialization uses the captured reference. A later rejected autonomous known-pose reset no longer leaves a mixed localization frame.

VisionSubsystem owns acquisition and qualification; VisionFusionCoordinator coordinates qualified-measurement handoff; only SwerveSubsystem mutates the estimator. Robot orders scheduler, fusion, and telemetry; RobotContainer wires dependencies. AutoBuilder reads estimated pose E and delegates reset to SwerveSubsystem. Pose-targeted and path-following commands consume E; validation/commissioning uses its specified module state. Telemetry is read-only and publishes O and E separately; Field2d displays O observationally.

Architect F01 adjudication accepted the original finding and authorized its bounded SwerveSubsystem repair. Lifecycle evidence: initial finding `HOLD_ACM_09_NEW_FINDING_ACM_09_F01_READY_FOR_ARCHITECT_REVIEW`; bounded repair `PASS_ACM_09_F01_BOUNDED_REPAIR_IMPLEMENTED_READY_FOR_USER_VERIFICATION`; User focused suites `SwerveSubsystemKnownFieldPoseResetTest`, `SwerveSubsystemPoseEstimatorTest`, and `SwerveSubsystemFieldRelativeTest` PASS with `--rerun-tasks`; full M00_L16 build BUILD SUCCESSFUL in 12s, 6 actionable tasks (2 executed, 4 up-to-date), so this does not claim every task was freshly executed; independent repair review `PASS_ACM_09_F01_INDEPENDENT_REVIEW_READY_FOR_ARCHITECT_REPAIR_CLOSURE`; repair-closure reconciliation `PASS_ACM_09_F01_REPAIR_CLOSURE_RECORDED_READY_FOR_DOMAIN_REREVIEW`.

Static governance preflight PASS: 12 authoritative English PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was NOT PERFORMED. ACM-01 through ACM-08 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED; latest published checkpoint recorded by the User is `a932931ae674817b4fb994cba8cfe2ef2591db98` (`audit-acm-08-closed`). **ACM-10 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED**; ACM-11 and ACM-12 remain NOT STARTED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. This domain closure does not close the lesson or authorize re-freeze, publication, Constants.java cleanup/refactor, or Phase 4.

This recording changes exactly the eight established lifecycle documents. The two Java repair files and their existing diff remain byte-for-byte unchanged and uncommitted. No other tracked file was modified by this recording; protected and unrelated worktree state was left untouched. No Git write or project execution occurred; the index remains empty. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED. The exact next gate is the User-owned ACM-09 Git checkpoint; staging, commit, annotated tag, and push remain pending.
<!-- ACM-09 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-09-F01 REPAIR-CLOSURE CURRENT BEGIN -->
## Current ACM-09-F01 repair closure — 2026-10-02

The Architect explicitly authorized closure of ACM-09-F01 as a repair finding after User verification and Sol's independent post-repair review. **ACM-09-F01: CONFIRMED / REPAIRED / USER VERIFIED / INDEPENDENTLY REVIEWED / ARCHITECT REPAIR CLOSURE AUTHORIZED / CLOSED AS A REPAIR FINDING.** This records repair closure only; ACM-09 remains IN_PROGRESS / HOLD, with post-repair domain rereview pending and the domain NOT FORMALLY CLOSED. ACM-09-F02: NOT ESTABLISHED. No User token is asserted.

The original defect was that `SwerveSubsystem.captureFieldHeadingReference()` could change the raw-yaw reference after localization initialized without synchronizing odometry, pose estimation, exposed poses, continuity, and applicable vision-history state. Autonomous preparation captures heading before attempting `resetKnownFieldPose()`; a rejected reset could leave the changed reference active with trackers anchored in the previous frame.

The bounded repair validates required gyro, module, and localization state; preserves odometry pose O and estimated pose E independently; and prepares both replacement trackers from the new adjusted heading and current module positions before committing the heading reference. If reconstruction fails, the old reference and trackers remain authoritative. On success, poses, observation, continuity, estimator timestamp, and the vision reset/history barrier are synchronized. Measurements at or before the barrier are rejected; newer valid measurements are admitted after normal estimator progression. Capture before tracker initialization creates no placeholder trackers. Invalid required state fails closed. Field-relative capture behavior remains effective, and `resetKnownFieldPose()` semantics are unchanged.

Traceability: initial audit `HOLD_ACM_09_NEW_FINDING_ACM_09_F01_READY_FOR_ARCHITECT_REVIEW`; bounded implementation `PASS_ACM_09_F01_BOUNDED_REPAIR_IMPLEMENTED_READY_FOR_USER_VERIFICATION`; independent review `PASS_ACM_09_F01_INDEPENDENT_REVIEW_READY_FOR_ARCHITECT_REPAIR_CLOSURE`; Architect decision `ACM-09-F01 REPAIR CLOSURE AUTHORIZED`. All 30 repair-review dimensions were CLOSED.

User verification: `SwerveSubsystemKnownFieldPoseResetTest`, `SwerveSubsystemPoseEstimatorTest`, and `SwerveSubsystemFieldRelativeTest` PASS with `--rerun-tasks`. Full M00_L16 build: BUILD SUCCESSFUL in 12s, 6 actionable tasks (2 executed, 4 up-to-date); this does not mean every task/test was freshly executed. No Simulation, Glass, Driver Station, or hardware run was reported or required for this repair closure.

ACM-01 through ACM-08 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED. Latest checkpoint: `a932931ae674817b4fb994cba8cfe2ef2591db98`, tag `audit-acm-08-closed`. ACM-10 through ACM-12 remain NOT STARTED / NOT ACTIVATED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. No Constants.java cleanup/refactor is authorized.

This reconciliation updates established lifecycle documentation only. The two Java repair files remain byte-for-byte unchanged by this recording and uncommitted for the User-owned Git workflow. No Git write or project execution occurred; the index remains empty. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.
<!-- ACM-09-F01 REPAIR-CLOSURE CURRENT END -->

# FRC Java Coding Lab 7.0
<!-- ACM-08 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-08 formal domain closure — 2026-10-02

The Architect reviewed and accepted Sol's independent read-only ACM-08 audit and explicitly authorized **ACM-08 FORMAL DOMAIN CLOSURE**. Architect decision: **ACM-08 FORMAL DOMAIN CLOSURE AUTHORIZED**. Independent audit token: **PASS_ACM_08_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW**.

**ACM-08: FORMALLY CLOSED / FORMALLY RECORDED. All 46 ACM-08 closure dimensions are CLOSED. ACM-08-F01: NOT ESTABLISHED.** No telemetry-boundary defect, repair, or repair ADR was established. The Architect owns this closure decision; Sol supplied independent audit evidence.

Static governance preflight PASS: 12 authoritative English source PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was NOT PERFORMED. The applicable A/B/C authority establishes the one-way path: hardware / IO → IOInputs → subsystem → immutable Observation/read model → telemetry facade/publisher → NetworkTables / Field2d / dashboard diagnostic output. Telemetry remains observational/read-only; no reverse telemetry-control path was established.

Current production telemetry consists of RobotTelemetry; Swerve, Vision, Intake, Feeder, Flywheel, Elevator, DriverInput, AutonomousPreparation, and AutonomousEvent facades; and the DriveThreeMeterValidation telemetry facade/interface. RobotContainer constructs and wires the publishers. Robot invokes RobotTelemetry.periodic() after scheduler and vision-fusion work. The facades consume subsystem observations or immutable values and publish diagnostics. No mutable IOInputs reference, concrete IO adapter, vendor device, or mutable collection alias reaches telemetry. No telemetry facade owns subsystem mutation, IO output, vendor control, command scheduling/cancellation, pose reset, or mechanism stop/start authority.

NetworkTables direction is preserved by category: Swerve/*, Vision/*, Intake/*, Feeder/*, Flywheel/*, Elevator/*, DriverInput/*, AutonomousPreparation/*, AutonomousEvent/*, DriveThreeMeterValidation/*, and Swerve/Field are TELEMETRY OUTPUT. Limelight json is SENSOR INPUT through VisionIOLimelight → Vision IOInputs → VisionSubsystem; it is not telemetry-control input. The Autonomous Routine chooser is AUTHORIZED CONFIGURATION / SELECTION through RobotContainer/autonomous preparation. The simulation fixture chooser is AUTHORIZED SIMULATION SELECTION. Dashboard command widgets, including commissioning, validation, and Prepare Autonomous, are EXPLICIT COMMAND CONTROL SURFACES; they are not reads of telemetry output topics used to derive hidden actuator commands.

Swerve Field2d publication is output-only: Swerve observation pose → Field2d visualization. No Field2d value is read back to drive, reset pose, modify estimator state, schedule a command, or alter mechanism behavior. Estimator ownership remains outside this closure and belongs to ACM-09.

Autonomous preparation telemetry reads the coordinator observation. Named-event telemetry reads its event observation state and does not dispatch LEARNING_EVENT. The wired LEARNING_EVENT creates IntakeToFeederCommand through the command registration path. The currently wired event path does not establish complete STARTED / ACTIVE / terminal lifecycle reporting; this is a reporting-completeness limitation and did not satisfy the ACM-08 finding standard.

Robot.robotPeriodic() invokes telemetry in a finally path without a telemetry-specific catch, so a telemetry publication exception may propagate from that periodic call. Command-owned teleop and three-meter validation publication paths have their own command-owned failure/stop handling. This is a runtime exception-containment limitation; it does not establish telemetry authority to mutate subsystem state, schedule commands, control IO, stop mechanisms, reset pose, or control vendor hardware. No blanket telemetry-exception containment guarantee is made.

ACM-01 through ACM-07 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED; no current regression was established. ACM-07 checkpoint: 6d0029b8bf54f4e816ef6eda237bb7b5f5529e8b, annotated tag audit-acm-07-closed. **ACM-09 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED** and is the Estimator / Localization Ownership domain. ACM-10 through ACM-12 remain NOT STARTED. This record does not audit ACM-09.

Phase 3 remains IN_PROGRESS. Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. No M00_L17. ACM-08 closure does not authorize re-freeze, publication, Constants.java cleanup/refactor, or Phase 4.

This documentation-only lifecycle record changes no Java, tests, Gradle files, deployment assets, PathPlanner assets, governance sources/mirrors, or protected/unrelated content. No ACM-08 checkpoint commit, tag, or push exists yet; those remain User-owned gates. No Git write or project execution occurred. The index remains empty. Existing protected/unrelated worktree state was left untouched. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.
<!-- ACM-08 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-07 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-07 formal domain closure — 2026-10-01

The Architect reviewed and accepted Sol's independent post-repair domain rereview and explicitly authorized **ACM-07 FORMAL DOMAIN CLOSURE**. Accepted rereview token: PASS_ACM_07_POST_REPAIR_REREVIEW_READY_FOR_ARCHITECT_DOMAIN_CLOSURE. The Architect owns this closure decision.

**ACM-07: FORMALLY CLOSED / FORMALLY RECORDED. All 45 post-repair ACM-07 closure dimensions are CLOSED. ACM-07-F01: CLOSED / REPAIRED / USER VERIFIED / INDEPENDENTLY REVIEWED / ARCHITECT REPAIR CLOSURE AUTHORIZED. ACM-07-F02: NOT ESTABLISHED.**

The initial audit established ACM-07-F01 only: a drive-stop exception could skip the CTRE module's steer-stop attempt. Initial audit token: HOLD_ACM_07_NEW_FINDING_ACM_07_F01_READY_FOR_ARCHITECT_REVIEW. The bounded repair token is PASS_ACM_07_F01_BOUNDED_REPAIR_IMPLEMENTED_READY_FOR_USER_VERIFICATION; the independent repair-review token is PASS_ACM_07_F01_INDEPENDENT_REVIEW_READY_FOR_ARCHITECT_REPAIR_CLOSURE; the repair-closure reconciliation token is PASS_ACM_07_F01_REPAIR_CLOSURE_RECORDED_READY_FOR_DOMAIN_REREVIEW.

The accepted repair changes only current M00_L16 SwerveModuleIOCTRE.java and SwerveModuleIOCTREStopSeparationTest.java. The CTRE full-module stop now attempts drive once and steer once even if drive throws. A lone failure propagates; on distinct dual failures drive remains primary and steer is suppressed; RuntimeException and Error semantics are preserved; same-instance self-suppression is guarded. The independent rereview found all previously F01-blocked areas CLOSED: subsystem end-to-end safe-stop API, CTRE adapter stop, path-following end-to-end stop, persistent-output sweep, repeated-stop software guarantee, and remaining ACM-07 technical work. This is an attempt guarantee, not a claim of physical hardware response after a vendor call throws.

User verification: focused SwerveModuleIOCTREStopSeparationTest PASS with --rerun-tasks; full M00_L16 build BUILD SUCCESSFUL in 20s, 6 actionable tasks (3 executed, 3 up-to-date). This does not claim every test/task was freshly executed. The accepted rereview requires no additional Simulation, Glass, Driver Station, or real-hardware execution for ACM-07 closure.

All 45 closure dimensions are CLOSED; the dimension-by-dimension record is in the current ACM-07 closure block in AGENTS.md. No current ACM-07 technical repair work remains.

Static governance preflight PASS: 12 authoritative source PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was not performed. No dedicated repair ADR was created; existing lifecycle governance does not require one for this closure.

ACM-01 through ACM-06 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED, with no prior-domain regression established. ACM-06 checkpoint: 58549f11989d2198378a38479228663a6b6b7613 (audit-acm-06-closed). **ACM-08 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED**; ACM-09 through ACM-12 remain NOT STARTED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. This closure does not authorize re-freeze, publication, Constants.java cleanup/refactor, or Phase 4.

This formal closure recording changes lifecycle documentation only. The two Java repair files remain byte-for-byte unchanged and uncommitted as evidence for the later User-owned Git workflow. No ACM-07 checkpoint commit, tag, or push exists yet. The index remains empty. No Git write or project execution verification occurred during this recording. Earlier ACM-01–ACM-06 blocks below preserve their stage evidence; their former ACM-07 cursor wording predates this closure and does not control the current cursor. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED.
<!-- ACM-07 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-06 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-06 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-06 — Command Semantics + CommandScheduler Requirements — after Sol's independent read-only audit token `PASS_ACM_06_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. The Architect owns the closure decision; Sol supplied independent evidence. **ACM-06: FORMALLY CLOSED / FORMALLY RECORDED. All forty audit dimensions are CLOSED. ACM-06-F01: NOT ESTABLISHED.** No repair or repair ADR is required.

Governance preflight PASS: 12 authoritative source PDFs, 12 matching hashes, zero deterministic findings; semantic fidelity certification was not performed. The audit inventoried all twenty current production `Command` subclasses: nineteen declare exact requirements for the subsystem semantic state they change; `AutonomousEventDemonstrationCommand` is the sole zero-requirement command and changes no subsystem state. No under-claim or materially incorrect over-claim was established. Commands use subsystem APIs and project observations, with no direct vendor API, concrete adapter, or mutable IOInputs access.

Production direct `CommandScheduler` calls remain in Robot lifecycle integration (`run`, autonomous schedule, Test-mode `cancelAll`); `teleopInit` cancels the autonomous command through command lifecycle semantics. No scheduler polling, manual ownership flag/lock, or dynamic ownership transfer is used for arbitration. The Swerve default and active bindings carry their subsystem requirements. `LEARNING_EVENT` supplies a fresh `IntakeToFeederCommand` with an exact Intake + Feeder deferred requirement set. Autonomous command compositions preserve Swerve ownership; stop/output safety beyond requirement ownership remains ACM-07. Existing requirement and scheduling tests were reviewed but not run.

ACM-01 through ACM-05 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED, with their recorded checkpoint commits and tags preserved below. No regression was established through ACM-05. ACM-07 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; ACM-08 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. No Constants.java cleanup/refactor is authorized. The User subsequently checkpointed, pushed, and annotated-tagged ACM-06 at `58549f11989d2198378a38479228663a6b6b7613` (`audit-acm-06-closed`); the User verified the remote main and annotated tag target at that commit. This is documentation-only; no project execution or Git write occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-06 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-05 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-05 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-05 — Observation / IOInputs Data Flow — after Sol's independent read-only audit. Sol's accepted token is `PASS_ACM_05_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`; the Architect owns the closure decision. **ACM-05: FORMALLY CLOSED / FORMALLY RECORDED.** All thirty-one audit dimensions are CLOSED. **ACM-05-F01: NOT ESTABLISHED.** No current data-flow defect or repair exists.

Earlier ACM-01–ACM-04 blocks below preserve their closure-stage evidence; their former next-domain cursor wording predates this ACM-05 closure and does not control the current cursor.

Static governance preflight PASS: 12 authoritative source PDFs, 12 matching source hashes, zero deterministic findings. This does not certify semantic fidelity. Current M00_L16 preserves IO / hardware / simulation → subsystem-owned mutable IOInputs → subsystem interpretation → immutable project observations → project consumers. The seven IO families are SwerveModuleIO, GyroIO, VisionIO, IntakeIO, FeederIO, FlywheelIO, and ElevatorIO. Each subsystem owns its transport instance(s); refresh precedes periodic observation construction. No mutable Inputs escape, alias, shared owner, alternate hardware read route, or static/global mutable transport was established.

Fourteen current top-level records comprise the observation/read-model inventory: SwerveObservation, VisionObservation, QualifiedVisionMeasurement, VisionTiming, VisionMeasurementQuality, VisionFusionObservation, IntakeObservation, FeederObservation, FlywheelObservation, ElevatorObservation, DriverInputObservation, DriveThreeMeterValidationObservation, AutonomousEventObservation, and AutonomousPreparationObservation. VisionObservation copies its target list and independently owns target values. Commands and telemetry receive observations or semantic project values. Noop and Swerve/Gyro/Vision simulation paths preserve the same transport boundary. The corrected Feeder path is FeederIO → FeederIOInputs → FeederSubsystem → FeederObservation; escape NONE ESTABLISHED; status CORRECT. Full closure evidence and the IO inventory are recorded in [AGENTS.md](AGENTS.md).

ACM-01 through ACM-04 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED; no regression was established. At the ACM-05 checkpoint, ACM-06 was NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; that cursor was superseded by the formal ACM-06 closure recorded above. ACM-07 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. The User checkpointed and pushed ACM-05 at `6c125c2490c50b9f2c0151379307ac72d10c0b22` with annotated tag `audit-acm-05-closed`; its remote target was verified by the User. No Git write or project execution occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

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

F01 and F01-DOC-01 remain CLOSED; F02 and F03 remain CLOSED / FORMALLY RECORDED; F04 remains FORMALLY CLOSED / FORMALLY RECORDED. The 14-member F04 production family and its dedicated tests remain in commands.auto; the exact family is preserved in [the F04 repair record, Sections 1 and 4](docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md). LearningTrajectoryFactory and its dedicated test remain in commands.auto.

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
[The F04 ADR, Section 2](docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md#2-bounded-implementation-and-static-self-audit--2026-09-30) records this static self-audit.
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
family. [The F04 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md) records each type and historical
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
[The F03 ADR, Section 4](docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md#4-formal-acm-01-f03-repair-closure--2026-09-30) governs this decision. The
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
[The F03 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md) now reconciles the accepted evidence.

F03 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW;
F03 is NOT CLOSED. F04 remains ACCEPTED / PARKED in order F03 -> F04.
F01, F01-DOC-01 and F02 remain CLOSED; P3-H01 CLOSED / PRESERVED;
CFG-H01 CLOSED / REMOVED; CF-U and PF-U CAUSE NOT ESTABLISHED.
M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED;
ACM-01 and Phase 3 remain HOLD; ACM-02 NOT STARTED; Phase 4
NOT STARTED / FORBIDDEN. No M00_L17.
Exact next gate: INDEPENDENT FINAL ACM-01-F03 ARCHITECTURE / CLOSURE REVIEW.

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
Document A Sections 2 and 8. [The dedicated F03 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md)
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
The governing closure decision is [the F02 ADR, Section 3](docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#3-formal-acm-01-f02-repair-closure--2026-09-30).

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
[the F02 ADR, Section 2](docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#2-accepted-implementation-and-evidence-reconciliation--2026-09-30).
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

This is the registration-stage record, before F02 Java implementation or
verification. The Architect accepted ACM-01-F02 under Document C OC-02
Section 1 and authorized a package-only correction of
`DriveThreeMeterValidationObservation` in M00_L16: one relocation to
`frc.robot.observation.swerve`, three production import updates, one
existing test import update, and one new focused placement test. S00_L23 is
the earliest surviving origin; historical copies remain untouched. The
exact six-Java-identity boundary and verification design are in
[the F02 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md).

At registration, verification, independent review, and closure are PENDING.
F01 and DOC-01 remain CLOSED; F03 and F04 are ACCEPTED / PARKED. M00_L16
remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; ACM-01 HOLD,
ACM-02 NOT STARTED, Phase 4 NOT STARTED / FORBIDDEN. Next: bounded F02
implementation and User automated verification, then independent review.

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
- Governing chronology, evidence and closure: [dedicated ACM-01-F01 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

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
- Governing chronology/evidence: [dedicated ACM-01-F01 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

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
- Governing record: [ACM-01-F01 package repair](docs/architecture_decisions/ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).
- Next gate: Architect review of the compile failure and protected-state fingerprint discrepancy; no further execution or scope expansion.
- Full documentation reconciliation, closure, re-freeze, new publication and audit resumption remain later gates. No M00_L17.

<!-- ACM-01-F01 FIRST-STAGE SNAPSHOT END -->

## Real Robot Programming

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
- Governing repair record: [P3-H01 configuration-authority repair](docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md); original registration and reconciliation
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
- Governing amendment: [existing exceptional-repair ADR](docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md); preserved registration in Section 16 and
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
- Governing repair ADR: [P3-H01 configuration-authority repair](docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md).

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

## Historical M00 lifecycle — pre-Commit-2 publication metadata reconciliation, 2026-09-27

M00_L15 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED under accepted
User-owned primary snapshot `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`,
metadata publication `0d3685ce67a0b985459392621e003611eaa6dc35`,
and `PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`. M00_L16 is
`COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`; active lesson count is 0 and
the current active M00 lesson is NONE. Baseline, inheritance, Final Design Lock, controlled activation,
independent activation review, implementation authorization, implementation,
bounded architecture-test repair, and independent static rereview passed.

M00_L16's one concept is scheduler-managed dispatch of the existing
`IntakeToFeederCommand` through the inherited `LEARNING_EVENT` named-event
boundary. Implementation modified only `RobotContainer.java` in production,
with exact IntakeSubsystem and FeederSubsystem requirements, modified the
inherited architecture guard, and added the eight-test integration file.
The path, chooser, teleop controls, event helpers, and mechanism code remain
unchanged. User focused tests passed (8/8 integration tests), clean regression
passed with BUILD SUCCESSFUL, and accepted bounded Simulation passed for the
event path and event-free control. No clean-regression count or exit code was
supplied. AutonomousEvent NT fields may remain blank under the existing
publication contract. Evidence is THEORY VERIFIED / SIMULATION VERIFIED /
REAL HARDWARE DEFERRED. Focused Tests, Clean Regression, Bounded Simulation,
Documentation Reconciliation, and Independent Closure Review are COMPLETE /
PASS. The initial closure HOLD and bounded README repair are preserved in the
lesson chronology. Freeze Reconciliation is COMPLETE / PASS_M00_L16_FREEZE_RECONCILIATION;
Independent Freeze Review passed as `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW`
with verdict `M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
The User created Primary Frozen Snapshot Commit 1 at
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata
reconciliation is COMPLETE / `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`,
awaiting User-owned Metadata Commit 2. The metadata commit, push, and final
external publication verification remain PENDING; M00_L16 is NOT PUBLISHED.
M00_L16 is the final M00 lesson; no M00_L17 is authorized.

Older dated lifecycle records in this README are historical snapshots.

> Learn professional FRC robot software engineering through incremental inheritance development.

> Học lập trình robot FRC theo phương pháp phát triển kế thừa (Inheritance Development) với các dự án robot thật.

---

# Repository Purpose
# Mục tiêu Repository

This repository is a complete learning roadmap for developing FRC robots using Java and WPILib.

The goal is not only to build a working robot, but also to learn software architecture, engineering workflow, testing, documentation, and long-term project maintenance.

Repository này là lộ trình học hoàn chỉnh để phát triển robot FRC bằng Java và WPILib.

Mục tiêu không chỉ là làm robot chạy được, mà còn học kiến trúc phần mềm, quy trình phát triển, kiểm thử, tài liệu hóa và bảo trì dự án lâu dài.

---

# Core Principles
# Nguyên tắc cốt lõi

- Frozen Architecture
- Inheritance Development
- One Lesson = One Project
- One Lesson = One New Concept
- Step-by-Step Learning
- Build Before Continue
- Simulation Before Real Robot
- Complete Documentation

---

# Repository Structure
# Cấu trúc Repository

```text
FRC_Java_Coding_Lab_7/
│
├── AGENTS.md
├── README.md
├── .gitignore
│
├── docs/
│   ├── Document_A/
│   ├── Document_B/
│   └── Document_C/
│
└── real_robot_programming/
    ├── D00_L01_Competition_Robot_Foundation/
    ├── D00_L02_Drivebase_Safety_Configuration/
    ├── D00_L03_Tank_Drive_With_Joystick/
    └── ...
```

---

# Directory Description
# Giải thích thư mục

## AGENTS.md

Defines the operating rules for Codex.

Định nghĩa quy tắc vận hành của Codex.

---

## docs/

Contains the repository engineering standards.

Chứa các tiêu chuẩn kỹ thuật của toàn bộ repository.

Typical contents:

- Frozen Backbone
- Frozen Interface Contract
- Engineering Standard
- Architecture Documents
- Observation Architecture and Model Contract

`Document_C` formally defines `frc.robot.observation` as the permanent
top-level package for immutable, vendor-neutral read models.

`Document_C` chính thức định nghĩa `frc.robot.observation` là package
top-level cố định cho các read model bất biến và độc lập với vendor.

---

## Governance PDFs and VERIFIED Markdown Mirrors

Authoritative English governance PDFs remain authoritative. Their co-located
VERIFIED Markdown mirrors are machine-readable representations and do not have
independent or equal authority. The normative operating rules are in
`AGENTS.md`; source/mirror paths, trust metadata, and final mirror hashes are
indexed in `docs/GOVERNANCE_DOCUMENT_MANIFEST.md`.

At first governance use in a task or at formal audit start, run:

```powershell
py -3 docs/tools/governance/validate_governance_mirrors.py
```

Then consult the manifest, confirm the applicable mirror is VERIFIED and
registered, compare its current Markdown SHA-256 with the manifest, and read
all applicable sections sufficiently for the decision. Targeted section
retrieval is allowed for follow-up work in the same unchanged scope after these
checks. Use the authoritative PDF when a mirror is missing, not VERIFIED,
hash-mismatched, disputed, ambiguous, under fidelity review, needed for
forensics, or otherwise subject to the fallback rules in `AGENTS.md`.

The Architecture Poster mirror may represent its explicit text and
relationships. Consult the authoritative poster PDF whenever spatial layout,
adjacency, shared boxes, color emphasis, grouping, relative prominence, or
visual hierarchy matters.

---

## real_robot_programming/

Contains every lesson project.

Mỗi lesson là một project WPILib độc lập.

Each lesson inherits from the previous completed lesson.

Mỗi lesson kế thừa lesson hoàn thành trước đó.

Example:

```text
D00_L01
    ↓ Copy
D00_L02
    ↓ Copy
D00_L03
```

## Approved Autonomous Roadmap

### A01 - Autonomous Navigation and Path Following

A01 is the approved successor roadmap after frozen
`A00_L04_AutonomousMotionSafetyGating`. Its purpose is to progress from the
autonomous command foundation to deterministic field-based trajectory
execution and PathPlanner/AutoBuilder integration.

Authority:
`docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`

Current implementation state: A01 is authorized and `module_A01` exists.
`A01_L01` through `A01_L07_AutoBuilderContractIntegration` are `COMPLETE /
FROZEN / READ-ONLY`. After the latest Swerve zero-offset recalibration, the user
physically executed the L06 one-meter autonomous on both Blue and Red. A slight
Blue endpoint overshoot followed by a small reverse correction was observed;
exact endpoint accuracy is not formally measured or claimed, and final
PID/feedforward and physical-model tuning remain deferred. `A01_L07` is
`COMPLETE / FROZEN / READ-ONLY` after the user-confirmed implementation,
Simulation, and real-robot verification gates passed. `A01_L08` was
`COMPLETE / FROZEN / READ-ONLY` at its original closure after the user-verified
post-repair WPILib VS Code build (`BUILD SUCCESSFUL in 1s`; 6 actionable tasks:
1 executed, 5 up-to-date), the accepted 430/430 test result, Simulation PASS,
and Real Robot PASS. The 11 initial failures were independently classified and
repaired as minimal L08 test-contract migrations, with no production defect
found. L08 preserves the locked routine-selection, readiness, alliance,
requirement, and centralized-stop contracts and was the frozen inheritance
source for L09. Exact endpoint accuracy, final PID/feedforward tuning, and final
physical characterization remain explicitly unclaimed.
`A01_L09_PathPlannerNamedCommandsAndEventMarkers` is now `COMPLETE / FROZEN /
READ-ONLY` after the final Architect/Reviewer closure decision PASS. Its
approved ADR amendment
permits one safe, observable, deterministic non-mechanism `LEARNING_EVENT`
binding because D01 remains an independent Tank Drive project with no approved
shared command boundary. The implemented `ONE_METER_WITH_EVENT` routine
preserves `SAFE_STOP` as the chooser default and the inherited `ONE_METER_PATH`
routine. Repository evidence records compileJava and compileTestJava PASS,
focused L09 tests PASS, 384 unchanged inherited regression tests PASS, the full
446/446 suite PASS, and isolated clean-build PASS. The user supplied Simulation,
Driver Station / Glass, and Real Robot PASS evidence for Blue and Red, event
dispatch and telemetry, concurrent path/event execution, Disable/mode-loss
stop, no automatic restart, and recovery to Teleop. Documentation reconciliation,
final architecture review, and final closure review are PASS. User-owned Git
publication is complete at `6b243bb`; the event remains a
non-mechanism demonstration, and no Intake, Feeder, Flywheel, or other D01
mechanism integration is claimed. Exact endpoint accuracy, final
PID/feedforward tuning, and final physical characterization remain explicitly
unclaimed.

The original A01_L08 closure evidence remains preserved as historical evidence,
but post-freeze real-robot evidence identified material preparation/readiness and
terminal mode-ownership defects. Under the approved supplemental ADR, A01_L08
was reopened narrowly for safety/robustness repair; at that historical stage,
V00_L02 was `SUSPENDED / READ-ONLY`. The preparation/readiness repair and the separately
authorized Option D terminal repair are implemented and locally verified. The
final user-owned Simulation and Real Robot evidence confirms recoverable
preparation without Robot Code restart, deterministic Blue/Red execution,
session-long terminal Swerve ownership, no controller leakage while Autonomous
remains enabled, safe mode-loss handling, and normal Teleop recovery. The final
pre-implementation architecture audit was `HOLD`: the active
`AutoBuilderContractAdapter.SafeAutoBuilderCommand` manually delegated
child lifecycle callbacks, conflicting with the scheduler-native re-freeze gate.
At that pre-implementation audit stage, A01_L08 remained `REOPENED /
IN_PROGRESS / EDITABLE`. The repair changed only the approved L08
production/test boundary; no SwerveSubsystem, CTRE/IO, tuning, calibration,
PathPlanner asset, Gradle, vendordep, frozen predecessor, successor, or V00 file
was changed. Exact endpoint accuracy, final PID/feedforward tuning, and final
physical characterization remain explicitly unclaimed. A01_L09 is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `6b243bb`, and the A01 lesson
order remains unchanged.
A01 ends at L09; A01_L10 is not authorized. A00 is closed at A00_L04; A00_L05
is not authorized.

On 2026-08-25, the A01_L08 governance amendment expanded the future repair
boundary for the scheduler exception design to exactly four production files:
`AutoBuilderContractAdapter.java`, `AutonomousPreparationCoordinator.java`,
`RobotContainer.java`, and `Robot.java`, with four named focused test files. The
amendment was governance-only; a separate final implementation action is
recorded below. At that historical stage, the final architecture re-freeze gate
remained `HOLD` pending the required test, runtime, and user-owned
re-verification gates.

Temporary change-control authority:
`docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`

The final 2026-08-25 A01_L08 implementation action separately authorized and
implemented that exact four-file scheduler-native exception-boundary repair.
`SafeAutoBuilderCommand` manual child lifecycle delegation is removed; the
Robot-level scheduler exception boundary, RobotContainer safety bridge, and
coordinator fatal entry are present. That implementation record did not itself
re-freeze A01_L08: local `compileJava` passed under WPILib Java 17 while
`compileTestJava` was still held by the then-existing Windows Gradle/Javac
classpath-resolution failure, and Simulation and real-robot re-verification
remained user gates.

On 2026-08-26, later verification closed those gates: `compileJava` PASS,
`compileTestJava` PASS, `RobotSchedulerExceptionBoundaryTest` PASS, full 449/449
test suite PASS, and clean build PASS. User-verified Simulation and real-robot
retests passed Blue/Red execution, terminal ownership, SAFE_STOP, the Teleop
mode gate, recovery, and no automatic restart. A brief terminal steering event
is recorded as `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`, `ACCEPTED FOR CURRENT
LESSON`, and `DEFERRED FOR FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`; its exact
physical root cause is not fully proven and no drivetrain, tuning, encoder,
CTRE, PathPlanner, or asset change is justified. A01_L08 is now `COMPLETE /
FROZEN / READ-ONLY` after its authorized reopen. At that closure point V00_L02
remained `SUSPENDED / READ-ONLY`; the later controlled reconstruction and
activation described in the V00 section was a separate governance decision.

Approved lesson sequence:

1. `A01_L01 - Autonomous Starting-Pose and Field-Frame Contract`
2. `A01_L02 - Pose-Targeted Autonomous Motion`
3. `A01_L03 - Trajectory Generation and Sampling Fundamentals`
4. `A01_L04 - Field and Alliance Transform Contract`
5. `A01_L05 - Holonomic Trajectory Following`
6. `A01_L06 - PathPlanner Path and Runtime Integration`
7. `A01_L07 - AutoBuilder Contract Integration`
8. `A01_L08 - Autonomous Routine Selection and Safe Composition`
9. `A01_L09 - PathPlanner NamedCommands and Event Markers`

### V00 - AprilTag Vision Observation and Pose Fusion

V00 is the approved successor roadmap after final closure and freezing of
`A01_L09_PathPlannerNamedCommandsAndEventMarkers`.

Authority:
`docs/architecture_decisions/ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`

Current state: roadmap `APPROVED / FROZEN`; `module_V00` exists. The historical
V00_L01 lineage became stale when final A01_L09 was reconstructed and published
at `6b243bb`. The current canonical V00_L01 was reconstructed from final
A01_L09, passed final architecture and closure review, and is `COMPLETE /
FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf`.

The V00_L02 that had been activated and implemented from the stale historical
lineage was suspended and later preserved outside the active lesson lineage.
The current canonical V00_L02 was reconstructed from published V00_L01. Its
inheritance audit, Java 17 baseline build, full inherited test suite,
architecture audit, pre-implementation design lock, implementation,
verification, documentation, final architecture review, and final closure
review are PASS. It is now `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at
`53e9b9f`. The exact V00_L02 Java delta is
`AprilTagFieldLayoutContract.java` plus
`AprilTagFieldLayoutContractTest.java`; no other production or test file
changed. Authoritative User verification under WPILib Java 17 records the
focused AprilTag test PASS, inherited `VisionFrameTransformTest` PASS, full test
suite PASS, and clean full build PASS (`BUILD SUCCESSFUL in 24s`; 7 actionable
tasks, 7 executed).

V00_L02 now has state `COMPLETE / FROZEN / READ-ONLY`. The contract explicitly
selects the welded or AndyMark official 2026
resource, returns canonical Blue-origin `fieldToTag` poses, and retains no raw
mutable layout/tag state. It does not use `kDefaultField`, alliance flipping,
pose inversion, `fromLayout(...)`, runtime wiring, camera/vendor integration,
VisionIO, Observation, telemetry, autonomous, PathPlanner, or Swerve behavior.
Simulation, Driver Station / Glass, real robot, and physical camera are `NOT
APPLICABLE`. User-owned Git publication is complete at `53e9b9f`.

The User prepared `V00_L03_VisionIOAndImmutableObservationContract` directly
from published V00_L02. Its WPILib Java 17 baseline build is User-verified
PASS, its no-Git inheritance audit found zero differences across 219 comparable
non-generated files, its architecture audit and Design Lock are PASS/APPROVED,
and the separately authorized implementation is complete. V00_L03 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `cc20d62` after the final
architecture and closure reviews passed and the User confirmed publication.
Its final state is `IMPLEMENTATION COMPLETE / USER-VERIFIED / DOCUMENTATION
COMPLETE / FINAL ARCHITECTURE AUDIT PASS / PREDECESSOR PROVENANCE PASS / FINAL
CLOSURE REVIEW PASS`.

The completed L03 concept is vendor-neutral one-cycle VisionIO transport plus
an immutable Vision Observation contract. The exact production boundary is
`VisionIO.java` and `VisionObservation.java`; the exact focused-test boundary
is `VisionIOTest.java` and `VisionObservationTest.java`. No runtime producer,
vendor adapter, NetworkTables acquisition, telemetry, simulation
implementation, Limelight, PhotonVision, physical-camera work, pose
estimation, quality/ambiguity, timestamps/latency, fusion, Swerve,
autonomous, PathPlanner, Robot, or RobotContainer change was added.

The earlier effectively-zero-quaternion test failure was a false oracle at the
locked `Transform3d` boundary because WPILib `Rotation3d` canonicalization had
already produced a valid identity rotation. The authorized repair changed the
test oracle to verify valid identity rotation; no raw quaternion API or
production contract expansion was introduced. Focused tests, inherited L01/L02
regressions, the full suite, clean build, final documentation, and the
read-only architecture audit are PASS. Simulation, Driver Station / Glass,
physical camera, and real robot remain not applicable to this contract-only
lesson and are deferred to the governed V00 lessons. V00_L01 remains published
at `7d52ebf`, V00_L02 remains published at `53e9b9f`, and A01_L10 remains
prohibited. User-owned Git publication of V00_L03 is complete at `cc20d62`.

The User subsequently prepared
`V00_L04_DeterministicVisionSimulation` from authoritative V00_L03 by the
approved copy/rename and generated-artifact cleanup workflow and supplied
WPILib Java 17 inherited baseline-build PASS evidence. Its read-only
inheritance, roadmap-scope, Frozen Backbone, Frozen Interface Contract, and
Document C audits passed, and the Architect approved the refined
deterministic-vision-simulation Design Lock. At the historical activation
stage, V00_L04 became the sole `IN_PROGRESS / EDITABLE` lesson. Separate
authorization later permitted exactly `VisionIOSim.java` and `VisionIOSimTest.java`; implementation is now
`IMPLEMENTED / VERIFIED`.

The locked L04 scope is one deterministic, vendor-neutral `VisionIOSim`
implementation of the frozen `VisionIO` contract. It uses official AprilTag
field geometry, a fixed `robotToCamera`, an immutable caller-selected frame,
explicit ground truth only for target-present frames, direct WPILib forward
geometry, and complete per-cycle `VisionIOInputs` overwrite. L05 pose
estimation, L06 quality, L07 timing, L08 real-camera integration, and L09
fusion remain deferred.

Authoritative User verification under WPILib Java 17 records
`compileTestJava` PASS, `VisionIOSimTest` PASS, inherited vision regressions
PASS, full test suite PASS, and clean build PASS. The earlier Codex-local
classpath failure is `RESOLVED / SUPERSEDED / NON-REPRODUCIBLE`. The
post-implementation architecture review and User-owned artifact cleanup are
PASS, and the required L03-to-L04 transition guide and lesson documentation are
reconciled. The final read-only architecture/documentation review and Architect
closure authorization are PASS. V00_L04 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 5461555 / USER VERIFIED`. At the
V00_L04 publication point, no V00 lesson was active and the active V00 lesson
count was `0`; the prepared V00_L05 successor was not yet activated.

The User subsequently prepared the ADR-locked
`V00_L05_AprilTagRobotPoseEstimation` directory from the final V00_L04
snapshot. The inheritance and architecture audits recorded 229 comparable
non-generated files, zero differences, identical production/test Java and
documentation, unchanged build/configuration/dependencies/deploy resources,
and preserved V00_L01-L04 protection. The prior candidate naming HOLD was
resolved by using the official ADR identity, with no ADR amendment.

The Architect approved the V00_L05 Design Lock. The authorized implementation
is complete. The narrow Java 17 compatibility repair (`getFirst()` to
`get(0)`) and noncommutativity test-fixture repair were test-only, and the API
reflection hardening is complete. The implementation architecture review and
authoritative post-hardening User verification are PASS, including clean
`compileTestJava`, focused L05 tests, inherited vision regressions, the full
suite, and clean build. Final documentation reconciliation and Architect
closure authorization are also PASS. V00_L05 is now
`COMPLETE / FROZEN / READ-ONLY`; its pure deterministic canonical AprilTag
robot-pose candidate estimation remains the locked concept:

```text
fieldToTag + cameraToTarget + robotToCamera
    -> canonical Blue-origin fieldToRobot pose candidate
```

Simulation, Driver Station, Glass, and real-robot verification remain
`NOT APPLICABLE` because V00_L05 adds no runtime camera or hardware integration.

The approved package is `frc.robot.vision`; the approved class is the final,
stateless, non-instantiable `AprilTagRobotPoseEstimator`; and the only approved
public method is
`estimateFieldToRobotCandidate(Pose3d, Transform3d, Transform3d)`. The exact
mathematics, structural validation, output semantics, dependency ownership,
test matrix, and V00_L06 quality boundary are recorded in the lesson
documentation. No runtime wiring, camera vendor, telemetry, Swerve, alliance
transform, or pose fusion is authorized.

V00_L01-L06 remain complete, frozen, protected, and published. V00_L06 is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 1327bf4 / USER VERIFIED` after
the User-confirmed publication commit
`1327bf41736c8fe79ba58ec5eea9e0120bd978fb` (`Complete V00_L06 vision
measurement quality contract`) and lesson-local metadata reconciliation
`49c4286` (`Reconcile V00_L06 publication metadata`). The original V00_L07
lesson publication remains historical pre-repair evidence:
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ d58bef0 / USER VERIFIED`.
Its User-confirmed publication commit is
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f` (`Complete V00_L07 vision
timestamp and latency contract`) and its lesson-local metadata reconciliation
is `618dd09` (`Reconcile V00_L07 publication metadata`).

At exceptional-reopen activation, V00_L07 became the sole current editable
lesson under an Architect/User-approved reopen:
`IN_PROGRESS / REOPENED / EDITABLE`. The reopen was limited to inherited
Swerve integrity repairs R1/R2/R3; implementation, fresh verification,
re-freeze, and repair publication were then pending. The original d58bef0
publication remains historical pre-repair evidence.

The authorized V00_L07 repair is now implemented and documented. The fresh
pre-repair baseline passed with 593/593 tests and a clean build; the post-repair
full suite passed with 600/600 tests and a clean build. Runtime WPILib
Simulation and the post-implementation read-only architecture/Frozen Backbone
review passed.
At an earlier stage, real-robot verification was deferred because the robot was
unavailable. Later User-supplied evidence verifies Teleop and Autonomous
usability. No stronger bounded stop, Disable, or no-unintended-restart claim is
added beyond that supplied evidence. The separately observed BL quantitative
drivetrain anomaly remains `KNOWN / DEFERRED HARDWARE MAINTENANCE`; it is not BL
PASS, quantitative drivetrain PASS, completed tuning/calibration, or a resolved
issue, and the approved disposition does not make it a Vision-curriculum
closure blocker.

The final read-only closure review passed and the Architect/User explicitly
authorized re-freeze. V00_L07 is now `COMPLETE / FROZEN / READ-ONLY / PUBLISHED
@ 4704cfc / USER VERIFIED`; no V00 lesson is active. The corrected repair
publication commit is
`4704cfc0801910e30c8abb7cffcc467e4f4df016` with subject `Complete corrected
V00_L07 Swerve integrity repair`. Historical `d58bef0` is not that corrected
repair publication.
The preceding V00_L08 lifecycle statement is historical and is superseded for
the existing candidate by the controlled repair activation below. V00_L09 is
not started. A01_L10 remains prohibited.

## Current V00_L08 final closure and freeze state - 2026-09-10

The User authorized the bounded V00_L08 repair based on the final Astra closure
audit. V00_L08 is now `COMPLETE / FROZEN / READ-ONLY`, and no V00 lesson is
active. The repair was
limited to real-adapter freshness/coherence,
the periodic observation-only runtime owner, read-only diagnostics, the private
Limelight schema boundary, directly related tests, and L08/root documentation.
V00_L09 fusion, Swerve/drivetrain/IO/tuning/calibration, autonomous behavior,
PathPlanner, vendor dependency/configuration changes, and H1 promotion remain
excluded.

The adapter requires heartbeat progression and fresh target-pose evidence after
both the prior target-refresh boundary and the prior heartbeat-change boundary
before accepting a target. Stable current `tv` and `tid` values remain
required structural fields; a changed heartbeat, tag id, or visibility field
alone is insufficient. Target changes observed while the heartbeat is stalled
are not recovery evidence. Missing, reset, reconnect, stale, partial,
malformed, or unstable data fails closed.
Independent NetworkTables topics are not atomic; two equal target snapshots
plus heartbeat rechecks provide only a read-stability and bounded-coherence
guard, not an atomic frame guarantee. `tv == 0` is an invalid acquisition
sample. Both real and simulation VisionIO use the same path:

```text
VisionSubsystem / immutable VisionObservation
        |
        +----> read-only Vision telemetry
        |
        +----> future V00_L09 estimator-fusion boundary
```

RobotContainer remains the composition root.

The bounded repair and its verification gates are PASS by User evidence:
the focused adapter tests are 37/37 PASS, the full regression is 642/642 PASS,
and the clean build is PASS under Java 17. The earlier 642-test result of
637 passed and five isolated failures remains historical pre-repair evidence;
those five defects are no longer a current automated hold.

User-owned runtime evidence is also PASS. WPILib Simulation and Driver
Station/Glass confirm the Java observation path and telemetry; the inherited
default `VisionIOSim` state is `UNAVAILABLE` with no target, which is an
acceptable deterministic simulation result. This does not claim that the real
Limelight adapter or LimelightOS was simulated. On the real robot, Limelight 4
target acquisition for AprilTag 32, target loss, and reacquisition were
observed through Java telemetry with the expected `TARGETS_PRESENT` and
`INVALID_SAMPLE` states. H1 remains the **PROVISIONAL COMMISSIONING LOCK** and
is not promoted to official or proven vendor semantics. The final architecture
and closure review returned `PASS_V00_L08_FINAL_CLOSURE_REVIEW`; transition
documentation and the final documentation reconciliation are PASS. Codex did
not run Git.

At the V00_L08 closure point, the final lesson content/state was
`COMPLETE / FROZEN / READ-ONLY`. V00_L09 was still future work at that
historical point and no fusion implementation was added. The later controlled
V00_L09 activation is recorded below. The User still owns Git publication.

Publication: `PUBLISHED @ f34b210 / USER VERIFIED`; message `Complete V00_L08
real vision adapter integration`; push PASS to `origin/main`.

## Current V00_L09 implementation, evidence, and documentation state - 2026-09-15

V00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ f34b210`. Its
implementation publication is `f34b210` with message `Complete V00_L08 real vision adapter
integration`. The User separately identifies `6415b17` as the later V00_L08
publication-metadata reconciliation; that history remains distinct from the
predecessor implementation publication, and no V00_L08 file is modified by
this activation.

The prepared ADR-locked
`V00_L09_SwervePoseEstimatorVisionFusion` candidate became the sole active V00
lesson during implementation and is now closed by explicit Architect freeze
authorization:

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
GIT COMMIT: 6548c98
COMMIT MESSAGE: Complete V00_L09 Swerve pose estimator vision fusion
GIT PUSH: COMPLETE / VERIFIED
REMOTE: origin/main = 5d36529
ORIGIN/HEAD: origin/HEAD = 5d36529
PUBLICATION: PUBLISHED / VERIFIED
FINAL PUBLICATION GATE: PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED
METADATA RECONCILIATION COMMIT: 5d36529
METADATA COMMIT MESSAGE: Record V00_L09 publication metadata
FINAL REPOSITORY STATE: HEAD = origin/main = origin/HEAD = 5d36529
SYNC STATE: ahead = 0 / behind = 0
V00 FINAL CLOSURE GATE: PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED
```

The User-supplied evidence records candidate preparation, generated-artifact
cleanup, inherited baseline clean-build PASS, roadmap identity reconciliation,
architecture audit PASS, timing-source investigation PASS, runtime
orchestration micro-audit PASS, coordinator decision PASS, and the Architect
gate `PASS_V00_L09_FINAL_DESIGN_LOCK`.

The one locked L09 concept is:

```text
qualified timestamped AprilTag measurement
    -> post-scheduler VisionFusionCoordinator requirement
    -> guarded Swerve-owned admission
    -> SwerveDrivePoseEstimator.addVisionMeasurement(...)
```

Swerve remains the sole owner of estimator state. Vision remains vendor-neutral
above IO. RobotContainer remains the composition root. No MegaTag migration,
dynamic quality covariance, Constants tuning, or unrelated architecture
change is included.

The locked implementation is complete. User-verified focused and inherited
tests, direct A-G failure/admission evidence, `compileTestJava`, the full
637/637 suite, and clean build are PASS; the preserved generated report records
637 tests with zero failures, errors, and skipped tests. The narrow
failure-boundary test repair changed only invalid test fixtures and reused the
existing deterministic quality-valid VisionIOSimHarness Frame A/B path;
production policy and behavior were not changed. Simulation Gate 1, estimator
initialization, Simulation Gate 2, and Driver Station / Glass are PASS.

Deployed V00_L09 Limelight flat-root `/limelight/json` parser, timing/result,
Available/Connected/SampleValid, and AprilTag 32 acquisition verification are
PASS. Real estimator initialization, stationary accepted fusion, positive
accepted-fusion count, geometry consistency, controlled translation/rotation,
target loss/reacquisition, and camera disconnect/recovery are PASS.

The evidence classifications are `THEORY VERIFIED`, `SIMULATION VERIFIED`, and
`REAL HARDWARE VERIFIED`. For bounded autonomous Gate 9, BLUE `ONE_METER_PATH`
preparation and completion passed. Vision was intentionally invalid/suppressed
because the physical Tag 32 placement was not asserted to match the official
field coordinate. Gate 9 proves bounded autonomous lifecycle and estimator
compatibility; it does not claim exact 1.000 m endpoint accuracy or physical
absolute-pose calibration accuracy.

The final independent architecture review passed with
`PASS_V00_L09_FINAL_ARCHITECTURE_REVIEW_READY_FOR_DOCUMENTATION_CLOSURE`. Final
documentation review passed with
`PASS_V00_L09_FINAL_DOCUMENTATION_REVIEW_READY_FOR_FREEZE_AUTHORIZATION`, and
the Architect issued `PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`. The
documentation-only lifecycle transition is now recorded as
`COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.

One historical Driver Station overrun warning remains non-proven; if it recurs,
capture WPILib/Driver Station timing epochs before any performance repair. User
Git commit `6548c98` and remote publication are verified. The distinct
metadata-reconciliation commit is User-published at `5d36529` with subject
`Record V00_L09 publication metadata`.

The User supplied the final repository evidence: `HEAD = origin/main =
origin/HEAD = 5d36529`, ahead `0`, and behind `0`. The implementation/freeze
publication remains `6548c98`; the later metadata reconciliation is `5d36529`.
The final gates are `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED` and
`PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`. This reconciliation changes
documentation metadata only and does not alter the accepted technical evidence,
Gate 9 qualification, or historical overrun wording.

Approved lesson sequence:

1. `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
2. `V00_L02 - AprilTag Field Layout Contract`
3. `V00_L03 - Vision IO and Immutable Observation Contract`
4. `V00_L04 - Deterministic Vision Simulation`
5. `V00_L05 - AprilTag Robot Pose Estimation`
6. `V00_L06 - Vision Measurement Quality Contract`
7. `V00_L07 - Vision Timestamp and Latency Contract`
8. `V00_L08 - Real Vision Adapter Integration`
9. `V00_L09 - Swerve Pose Estimator Vision Fusion`

The roadmap preserves frozen S00 and A01, RobotContainer's composition-root
role, vendor-neutral VisionIO and Observation contracts, read-only telemetry,
SwerveSubsystem ownership of `SwerveDrivePoseEstimator`, autonomous consumption
of `getEstimatedPose()`, and A01_L04 ownership of the sole alliance transform.
Vision measurements use canonical WPILib field coordinates and are not
alliance-flipped. Simulation must use independent ground truth, must not use
EstimatedPose as camera truth, and must pass before real-robot fusion
verification.

No camera/vendor is selected in V00_L01 through V00_L07. V00_L08 may select one
real implementation only after the ADR's hardware, WPILib 2026, vendor-version,
timestamp, dependency, and applicable simulation compatibility gate passes.

## M00_L01 final publication state — 2026-09-15

The controlling successor record is
`docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`.
The M00 roadmap is `APPROVED / ROADMAP AUTHORIZED`. Preparation was authorized
by `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, published, completed by the
User, and accepted through the inherited Java 17 baseline build and independent
Architecture / Inheritance Audit. The Architect Design Lock is
`PASS_M00_L01_FINAL_DESIGN_LOCK`.

```text
Active Lesson Count: 0
```

The locked roadmap contains exactly 16 lessons, `M00_L01` through `M00_L16`, in
the ADR-defined order. The first lesson is exactly
`M00_L01 - Mechanism Architecture Reuse`, with locked directory identity
`M00_L01_MechanismArchitectureReuse`. It is now `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`, and no M00 lesson is active. Implementation
authorization and production-code authorization remain `NONE`.

Its exact predecessor is the complete frozen V00_L09 snapshot at repository
state `5d36529`, with implementation/freeze commit `6548c98`:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_V00\V00_L09_SwervePoseEstimatorVisionFusion
```

D01 is not the predecessor. The exact future destination is:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_M00\M00_L01_MechanismArchitectureReuse
```

The User completed the authorized preparation sequence: copy the complete
frozen V00_L09 directory, create `module_M00` as part of that workflow, rename
only the destination copy, remove only its copied `build\` and `.gradle\`, and
run the inherited baseline clean build under WPILib 2026 Java 17. The accepted
result is `BUILD SUCCESSFUL in 20s` with 7 actionable tasks executed. The
recorded command was:

```powershell
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
.\gradlew.bat clean build "-Dorg.gradle.java.home=C:\Users\Public\wpilib\2026\jdk"
```

The Architecture / Inheritance Audit passed with 601 comparable non-generated
files on each side, zero missing or candidate-only files, and zero SHA-256
differences. The Architect Design Lock then passed, and this documentation-only
controlled activation reconciled M00_L01 to `IN_PROGRESS / EDITABLE` with
active lesson count `1`. M00 preserves the Frozen Backbone,
Frozen Interface Contract, Constants authority, frozen lesson protection, and
one-lesson/one-new-concept rule. RobotContainer remains composition root only;
vendor APIs remain inside concrete IO adapters; mechanism data follows
`hardware -> IOInputs -> subsystem/processing -> immutable Observation ->
read-only telemetry`. Intake, Feeder, Flywheel, and Elevator retain independent
ownership. Shooting remains `FlywheelSubsystem + FeederSubsystem +
ShootCommand`; no `ShooterSubsystem` or `ShooterIO` is authorized absent a
later formal change.

M00_L01's sole concept is Mechanism Architecture Reuse: how mastered
drivetrain, vision, and autonomous architecture applies to non-drivetrain
mechanisms. It may teach ownership, IO, immutable Observations, read-only
telemetry, composition-root assembly, safe stop, and architecture mapping. It
must not implement mechanisms, control/readiness behavior, elevator behavior,
homing, travel limits, coordination, autonomous events, or a new hardware API.

Future student-facing M00 Markdown must use separate English and Vietnamese
files with identical structure, course/chapter identity, meaning, evidence, and
architecture rules. English is normative; Vietnamese is student-friendly but
semantically equivalent. Evidence labels are limited to `THEORY VERIFIED`,
`SIMULATION VERIFIED`, `REAL HARDWARE VERIFIED`, `REAL HARDWARE DEFERRED`, and
`NOT APPLICABLE`. Under the approved Design Lock, M00_L01 is architecture-only:
Simulation, Driver Station / Glass, and real hardware are `NOT APPLICABLE`.
Focused new tests and test implementation are also `NOT APPLICABLE` / `NONE`.
The paired English and Vietnamese student learning guides are complete and
reviewed with identical 21-section structure and equivalent technical meaning.
The initial independent review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`.
The minimal repair added the missing `Constants.java` default-configuration-
authority statement without defining mechanism configuration values and passed
at `PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`; the
independent rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`.
The Architect documentation review then passed at
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

The User-supplied final inherited clean build/regression is distinct from the
20-second preparation baseline and passed with `BUILD SUCCESSFUL in 23s`; 7
actionable tasks were executed. Its gate is
`PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`. The final read-only
closure review passed at
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted through
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical/content closure readiness is `PASS`.

The controlled documentation reconciliation then passed at
`PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`, was
accepted for independent review at
`PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`,
and passed independent review at
`PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect issued `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

At the freeze-recording point, M00_L01 was `COMPLETE / FROZEN / READ-ONLY`
with active lesson count `0`, freeze state `FROZEN / READ-ONLY`, Design Lock
`PASS_M00_L01_FINAL_DESIGN_LOCK`, and production-code authorization `NONE`.
Technical/content work is complete; evidence remains `THEORY VERIFIED`, and
runtime verification remains `NOT APPLICABLE`. Publication, Git commit, Git
push, and remote verification were then pending.

The User subsequently published the frozen lesson at commit `83907ab` with
subject `Complete M00_L01 mechanism architecture reuse`. User-supplied evidence
records `HEAD = origin/main = origin/HEAD = 83907ab` on branch `main`; remote
verification passed at `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`.

M00_L01 is therefore `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`,
with active lesson count `0`. M00_L02 is `NOT ACTIVE / NOT CREATED`, and the M00
module is not declared complete. The distinct publication-metadata
reconciliation remains `PENDING USER COMMIT`, its push remains `PENDING USER
PUSH`, and final remote verification of that later metadata commit remains
`PENDING`. No V00 runtime evidence is reused as M00_L01 verification.

## M00_L02 final freeze and lifecycle closure — 2026-09-16

M00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` at
primary publication `83907ab` and metadata publication `f523118`. The Architect
issued `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`, and M00_L02 is now frozen:

```text
Lesson: M00_L02 - Mechanism Hardware Evidence Audit
Filesystem: M00_L02_MechanismHardwareEvidenceAudit
Status: COMPLETE
Active State: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Design Lock: PASS_M00_L02_FINAL_DESIGN_LOCK
Technical Implementation: NONE AUTHORIZED
Documentation Implementation: COMPLETE
Independent Documentation Rereview: PASS
Final User Build: PASS
Final Closure Review: PASS
Documentation Reconciliation: COMPLETE / RECORDED
Independent Reconciliation Review: PASS
Freeze Authorization: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
Theory: VERIFIED
Simulation: NOT APPLICABLE
Driver Station / Glass: NOT APPLICABLE
Real Hardware: DEFERRED
Primary Publication Commit: 65a92a4a5806fd5134e0114e851c4e4cc093c58e
Primary Publication Push: PASS
Publication Metadata Reconciliation: COMPLETE / RECORDED
Final Publication: PENDING METADATA COMMIT/PUSH/REMOTE VERIFICATION
M00_L03: NOT ACTIVE / NOT CREATED
```

The accepted inherited baseline is `BUILD SUCCESSFUL in 58s` with 637 tests,
zero failures, zero errors, and zero skipped. Post-repair inheritance is
604/604 comparable files and 173/173 protected files with zero missing, extra,
or SHA-256-different files. The accidental nested M00_L01 project was removed
through the controlled repair and verified absent.

The sole concept is `MECHANISM HARDWARE EVIDENCE AUDIT`: distinguish facts that
are `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or `NOT APPLICABLE`. Unsupported
physical facts may not be promoted to `VERIFIED`, and no hardware value may be
invented. Evidence is `THEORY VERIFIED` as a required lesson gate; focused new
tests, Simulation, and Driver Station / Glass are `NOT APPLICABLE`; real
hardware is `REAL HARDWARE DEFERRED`.

Production Java, tests, configuration, vendordeps, deploy assets, runtime
behavior, and mechanism APIs remain unauthorized. The paired English and
Vietnamese learning guides were separately authorized and implemented with
matching 25-section structure, 15 questions, 15 answers, and 80-row evidence
matrices containing 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN` rows.

The initial independent documentation review found the bounded missing
Constants authorization boundary, ownership/shooting lock, and knowledge-check
coverage. The authorized minimal two-guide repair resolved those findings, and
the independent rereview passed. The User-supplied final build/regression passed
with `BUILD SUCCESSFUL in 33s` and all 7 actionable tasks executed. The final
closure review passed, and documentation reconciliation is complete and
recorded at `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.
The independent reconciliation review passed, and the Architect-authorized
transition to `COMPLETE / FROZEN / READ-ONLY` is recorded with active lesson
count `0`. Theory is `VERIFIED`; Simulation and Driver Station / Glass are `NOT
APPLICABLE`; real hardware remains `DEFERRED`. The User completed the primary
publication at full commit `65a92a4a5806fd5134e0114e851c4e4cc093c58e` with
push `PASS`. Publication metadata reconciliation is now `COMPLETE / RECORDED`.
The distinct metadata commit, metadata push, final remote verification, and
final publication completion remain pending. M00_L03 exists in the roadmap but
remains `NOT ACTIVE / NOT CREATED`.

## M00_L03 controlled lifecycle activation — 2026-09-16

M00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` at
primary publication `65a92a4a5806fd5134e0114e851c4e4cc093c58e` and metadata
publication `84010ff5022a33a946888fafedbbca0d67439e0c`. M00_L03 is now the sole
active lesson:

```text
Lesson: M00_L03 - Intake Foundation
Filesystem: M00_L03_IntakeFoundation
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Sole Concept: AN INDEPENDENTLY OWNED, VENDOR-NEUTRAL INTAKE MECHANISM FOUNDATION
Final Design Lock: PASS_M00_L03_FINAL_DESIGN_LOCK
Implementation: PENDING SEPARATE AUTHORIZATION
M00_L04: NOT ACTIVE / NOT CREATED
```

This documentation-only activation introduces no Intake Java, tests,
configuration, vendor adapter, Constants change, command, binding, or runtime
behavior. Focused tests and bounded Fake/Noop software simulation are required
later; Driver Station / Glass are not completion gates, and real hardware is
deferred.

## M00_L03 implementation and student-documentation reconciliation — 2026-09-16

The activation record above is historical. Separate production/test
authorization was issued, and the bounded vendor-neutral Intake foundation is
now implemented. The initial focused run recorded 16 tests with 15 passing and
one test-defect failure caused by an optional callback dereference in
`IntakeSubsystemTest`. A minimal guard-only repair removed no assertions. The
focused retest passed (`BUILD SUCCESSFUL in 4s`, exit code `0`), and the full
clean regression passed (`BUILD SUCCESSFUL in 20s`, exit code `0`, 7 of 7
actionable tasks executed).

Bounded Simulation passed for startup, Noop composition, subsystem integration,
and Intake telemetry presence while Disabled. It does not prove physical
hardware behavior. Real-hardware verification remains deferred, no Intake
vendor adapter was added, and `Constants.java` is unchanged.

The independent implementation review passed. The authorized English and
Vietnamese learning guides are implemented and await independent documentation
review. M00_L03 remains the sole `IN_PROGRESS / EDITABLE` lesson with active
lesson count `1`; final closure build, closure review, freeze authorization,
`COMPLETE / FROZEN / READ-ONLY`, and User publication remain pending. M00_L04
remains `NOT ACTIVE / NOT CREATED`.

## M00_L03 final closure review and lifecycle reconciliation — 2026-09-17

The initial independent documentation review returned `HOLD` because Step 16
of the transition guide described the mutable one-cycle `IntakeIOInputs`
transport snapshot as immutable. The bounded one-line repair corrected the
phrase to `mutable one-cycle input snapshot`. Independent documentation
rereview passed and was accepted by the Architect. The English and Vietnamese
student guides remain unchanged and verified with matching 34-section,
15-question, and 15-answer structures.

The User-supplied final Java 17 closure build passed with `BUILD SUCCESSFUL in
42s`, 7 actionable tasks executed, and exit code `0`. The final read-only
closure review passed at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
This documentation-only lifecycle reconciliation is complete.

```text
M00_L03: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Technical Implementation: COMPLETE
Focused Tests: VERIFIED
Full Regression: VERIFIED
Final Closure Build: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION ONLY
Driver Station / Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
Student Documentation: VERIFIED AFTER BOUNDED REPAIR AND REREVIEW
Final Closure Review: PASS
Lifecycle Reconciliation: COMPLETE
Freeze Authorization: PENDING
Publication: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

M00_L03 is not yet `COMPLETE / FROZEN / READ-ONLY`. Independent reconciliation
review, explicit Architect freeze authorization, freeze recording, and
User-owned publication remain pending. No new technical feature work remains.

## M00_L03 final lifecycle freeze — 2026-09-17

The preceding reconciliation section is preserved as the historical
pre-freeze state. The independent reconciliation review passed at
`PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`,
and the Architect issued `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.

```text
M00_L03: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Technical Implementation: COMPLETE
Focused Tests: VERIFIED
Full Regression: VERIFIED
Final Closure Build: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION ONLY
Driver Station / Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
Student Documentation: VERIFIED
Final Closure Review: PASS
Lifecycle Reconciliation: COMPLETE
Independent Reconciliation Review: PASS
Freeze: COMPLETE / AUTHORIZED
Architect Freeze Authorization: PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION
Git Publication: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

M00_L03 is no longer editable. Remaining work is publication only. This
freeze does not claim a Git commit, push, remote verification, or completed
publication, and it does not create or activate M00_L04.

## M00_L03 primary publication and metadata reconciliation — 2026-09-17

M00_L03 remains `COMPLETE / FROZEN / READ-ONLY`, with freeze state `FROZEN`
and active lesson count `0`. The User completed the primary Git publication at
full commit `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`. The primary push passed,
and post-push local `HEAD` and `origin/main` both matched that commit; primary
remote alignment is `PASS`.

```text
Primary Git Publication: COMPLETE
Primary Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
Primary Push: PASS
Primary Remote Alignment: PASS
Publication Metadata: RECONCILED
Metadata Git Publication: PENDING USER GIT
Final Publication Verification: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

This metadata reconciliation is complete, but final publication is not. The
remaining sequence is independent metadata review, User-owned metadata Git
publication, metadata remote-alignment verification, and final publication
verification. M00_L04 remains inactive and uncreated.

## M00_L03 final publication and M00_L04 controlled activation — 2026-09-18

M00_L03 completed its two-commit publication sequence at primary commit
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3` and metadata commit
`b2464f66da42a6281acb7bfc709f2a3b83296505`. Metadata remote alignment and
final publication verification passed. M00_L03 is now accepted as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

M00_L04 is the sole active lesson:

```text
Lesson: M00_L04 - Intake Command Ownership
State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Current Phase: CONTROLLED ACTIVATION COMPLETE
Final Design Lock: PASS_M00_L04_FINAL_DESIGN_LOCK
Implementation: NOT STARTED
Implementation Authorization: PENDING
One New Concept: SCHEDULER-MANAGED MANUAL INTAKE OWNERSHIP
Controller Binding: RIGHT BUMPER / whileTrue / RunIntakeCommand
Simulation: NOT RUN
Driver Station: NOT RUN
Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
M00_L05: NOT ACTIVE / NOT CREATED
```

The locked future command will require the existing `IntakeSubsystem`, request
Intake once during initialization, remain scheduled while held, and call the
existing subsystem `stop()` unconditionally when ending. Observation,
telemetry, IO, Constants, and hardware selection remain unchanged.

## M00_L04 post-verification documentation state — 2026-09-18

The preceding activation record is historical. The separately authorized
implementation is complete within the exact production/test boundary. The
implemented path is:

```text
Right Bumper
-> whileTrue
-> RunIntakeCommand
-> IntakeSubsystem
-> existing requestIntake() / stop()
```

`RunIntakeCommand` requires exactly `IntakeSubsystem`, requests Intake once in
`initialize()`, performs no repeated request in `execute()`, returns `false`
from `isFinished()`, and unconditionally calls `IntakeSubsystem.stop()` from
`end(...)`. There is no Intake default command. The inherited Back/View
Prepare Autonomous binding remains present.

User evidence records 14/14 focused tests PASS (`BUILD SUCCESSFUL in 28s`,
four tasks executed, exit code 0) and full clean regression PASS (`BUILD
SUCCESSFUL in 47s`, seven tasks executed, exit code 0). Simulation is
`SIMULATION VERIFIED / BOUNDED`. Driver Station verification is `VERIFIED /
BOUNDED` and observed `STOPPED -> INTAKE_REQUESTED -> STOPPED` for Right Bumper
hold and release. Glass is `NOT APPLICABLE` as a distinct completion gate.

The selected implementation remains `IntakeIONoop`; therefore
`Available=false` and `Connected=false` are expected while `RequestedState`
records software intent. This evidence does not prove physical Intake motion
or stopping. Real hardware remains `REAL HARDWARE DEFERRED`.

The independent implementation review is PASS. The paired
[English](real_robot_programming/module_M00/M00_L04_IntakeCommandOwnership/docs/M00_L04_Intake_Command_Ownership_Learning_Guide_EN.md)
and
[Vietnamese](real_robot_programming/module_M00/M00_L04_IntakeCommandOwnership/docs/M00_L04_Intake_Command_Ownership_Learning_Guide_VI.md)
learning guides are created and await independent documentation review.
M00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson with active lesson
count `1`; freeze is not authorized, publication is not started, and M00_L05
is `NOT ACTIVE / NOT CREATED`.

## M00_L04 final lifecycle reconciliation and freeze — 2026-09-18

The preceding section is preserved as historical pre-closure state. The
documentation review/repair/rereview sequence, transition-history
reconciliation and independent confirmation, final User closure build, and
resumed independent final closure review are complete and accepted. The final
closure build passed with `BUILD SUCCESSFUL in 23s`, 7/7 actionable tasks
executed, and exit code `0`.

M00_L04 established scheduler-managed manual Intake ownership through the
semantic Right Bumper `whileTrue` binding. `RunIntakeCommand` requests Intake
once during `initialize()`, retains scheduler ownership while held, and
unconditionally delegates to `IntakeSubsystem.stop()` when ending. Focused
tests passed 14/14, full regression passed, Simulation is `SIMULATION VERIFIED
/ BOUNDED`, and Driver Station verification is `VERIFIED / BOUNDED` for
`STOPPED -> INTAKE_REQUESTED -> STOPPED`.

`IntakeIONoop` remains deterministic and vendor-neutral. It intentionally
reports `Available=false` and `Connected=false`, produces no physical output,
and supports bounded software verification only. Glass is `NOT APPLICABLE` as
a distinct completion gate, and real hardware remains `REAL HARDWARE DEFERRED`.

```text
M00_L03: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L04: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Editable Boundary: NONE
Active Lesson: NO
Active Lesson Count: 0
Documentation: COMPLETE / VERIFIED
Final Closure Review: PASS
Git Publication: PENDING USER ACTION
Publication State: NOT YET PUBLISHED
M00_L05: NOT ACTIVE / NOT CREATED
```

No M00 lesson is active. This lifecycle reconciliation does not publish
M00_L04, claim a commit or push, or activate M00_L05.

## M00_L04 primary publication and metadata reconciliation — 2026-09-18

The preceding lifecycle section is preserved as historical pre-publication
state. Primary M00_L04 publication is complete at commit `5c86be3` with
subject `Complete M00_L04 intake command ownership`. Accepted post-push
evidence records `HEAD`, `origin/main`, and `origin/HEAD` at `5c86be3`, so
primary remote alignment is `PASS`.

```text
M00_L04: COMPLETE / FROZEN / READ-ONLY
Implementation: COMPLETE / VERIFIED
Documentation: COMPLETE / VERIFIED
Final Closure Review: PASS
Primary Publication: COMPLETE
Primary Publication Commit: 5c86be3
Primary Remote Alignment: PASS
Publication Metadata Reconciliation: COMPLETE IN WORKING TREE
Metadata Git Publication: PENDING USER ACTION
Final Publication Verification: PENDING
Real Hardware: REAL HARDWARE DEFERRED
Active Lesson Count: 0
Current Active M00 Lesson: NONE
M00_L05: NOT ACTIVE / NOT CREATED
```

The metadata reconciliation is prepared but has not been committed. Its
User-owned metadata commit/push, remote verification, and final publication
verification remain pending. No future metadata SHA is claimed.

## M00_L04 final publication and M00_L05 controlled activation — 2026-09-18

The preceding section is historical pre-metadata state. M00_L04 completed its
two-commit publication at primary commit `5c86be3` and metadata commit
`24738e6`; accepted final alignment is `HEAD = origin/main = origin/HEAD =
24738e6`, and final publication verification is `PASS`. M00_L04 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

The User prepared M00_L05 from final M00_L04, removed candidate-only generated
artifacts, and supplied a Java 17 baseline-build PASS: `BUILD SUCCESSFUL in
41s`, 7 actionable tasks, 6 executed and 1 up-to-date. The inheritance audit
passed with 285/285 governed files byte-identical and zero unexpected delta.
The Architect issued
`PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

```text
Lesson: M00_L05 - Feeder Foundation
State: IN_PROGRESS / EDITABLE WITHIN LOCKED DESIGN BOUNDARY
Freeze State: NOT FROZEN / EDITABLE WITHIN LOCKED DESIGN BOUNDARY
Active Lesson Count: 1
Current Active M00 Lesson: M00_L05
One New Concept: FEEDER AS ONE INDEPENDENTLY OWNED TRANSPORT MECHANISM CAPABILITY
Semantic API: FeederSubsystem.requestFeed()
Requested States: STOPPED / FEED_REQUESTED
Runtime Strategy: FeederIONoop ONLY
Dynamic Feeder Simulation: NOT AUTHORIZED
Real Adapter: NOT AUTHORIZED
Real Hardware: REAL HARDWARE DEFERRED
Implementation Authorization: PENDING
Constants.java Changes: NOT AUTHORIZED
M00_L06: NOT ACTIVE / NOT CREATED
```

No Feeder production or test implementation is part of this activation. CAN
45-49 remains a planning reservation only, and no physical Feeder assignment
is claimed.

## M00_L05 post-verification documentation reconciliation — 2026-09-19

The preceding activation record is historical. Separate implementation
authorization was consumed. M00_L05 now has a complete vendor-neutral Feeder
foundation using `FeederIONoop` only: `FeederIO`, `FeederIONoop`,
`FeederSubsystem`, immutable `FeederObservation`, read-only
`FeederTelemetryFacade`, and bounded `RobotContainer` / `RobotTelemetry`
composition.

The initial focused run completed 19 tests with 18 PASS and one test-only
failure. A brittle architecture assertion matched the valid Javadoc phrase
`current cycle`; the test was repaired to reflect over non-static,
non-synthetic `FeederIOInputs` fields. The six focused classes then passed with
`BUILD SUCCESSFUL in 26s`, exit code `0`.

The initial full clean regression completed 682 tests with 681 PASS. The one
failure was shared `CommandScheduler` state leaked by the intentional
`new FeederSubsystem(null)` constructor test. `FeederSubsystemTest` received
an `@AfterEach` `unregisterAllSubsystems()` cleanup without weakening null
rejection. The final full clean regression passed all 682 tests with
`BUILD SUCCESSFUL in 51s`, seven tasks executed, and exit code `0`.

Bounded WPILib Simulation and HALSIM Robot State verification passed. Feeder
telemetry remained `Available=false`, `Connected=false`, and
`RequestedState=STOPPED` in both Disabled and Teleoperated Enabled states,
which is correct for `FeederIONoop`. This is software evidence only; real
hardware remains deferred and CAN 45-49 remains a planning reservation rather
than a physical assignment.

The independent implementation review passed and was accepted for
documentation implementation. Matching English and Vietnamese learning guides
are now implemented. M00_L05 remains `IN_PROGRESS / EDITABLE`, active lesson
count `1`, documentation is `IMPLEMENTED / READY FOR INDEPENDENT DOCUMENTATION
REVIEW`, freeze and publication remain pending, and M00_L06 remains `NOT
ACTIVE / NOT CREATED`.

## M00_L05 final lifecycle freeze — 2026-09-19

The preceding post-verification state is historical. The initial independent
documentation review recorded two bounded history defects; the authorized
three-file repair passed, and the independent documentation rereview passed.
The User then supplied the accepted final closure build result: `BUILD
SUCCESSFUL in 41s`, 7/7 tasks executed, exit code `0`. The independent final
closure review passed at
`PASS_M00_L05_FINAL_CLOSURE_REVIEW_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`,
and the Architect accepted that gate for lifecycle reconciliation and freeze.

M00_L05 is now `COMPLETE / FROZEN / READ-ONLY`. Implementation, focused tests,
the 682/682 full regression, bounded Simulation, bounded simulated Driver
Station verification, independent implementation review, documentation,
independent documentation rereview, final closure build, and final closure
review are complete. Real hardware remains `REAL HARDWARE DEFERRED`. Active
lesson count is `0`, no M00 lesson is active, and M00_L06 remains `NOT ACTIVE /
NOT CREATED`. Primary Git publication, publication metadata reconciliation,
metadata publication, and final publication verification remain pending; no
publication commit is claimed.

## M00_L05 primary publication and metadata reconciliation — 2026-09-19

The User completed primary publication at full commit
`5709f1d74b3318303bcc56779315b243dd81770b` with subject `Complete M00_L05 feeder foundation`.
Accepted evidence records push `24738e6..5709f1d main -> main` and primary remote alignment `HEAD == origin/main ==
5709f1d74b3318303bcc56779315b243dd81770b`.

M00_L05 remains `COMPLETE / FROZEN / READ-ONLY`, with active lesson count `0`
and no active M00 lesson. Primary publication is complete and publication
metadata reconciliation is complete. The separate metadata publication commit
and push, post-metadata remote alignment, and final publication verification
remain pending; no metadata commit hash is claimed. M00_L06 remains `NOT ACTIVE
/ NOT CREATED`.

## M00_L05 final publication and M00_L06 controlled activation — 2026-09-19 (historical activation record)

The preceding publication state is historical. M00_L05 completed metadata
publication at `1d6fadeec57fbfd3be245746b21d06e58b79518f`, subject `Record
M00_L05 publication metadata`, and passed final remote publication verification
at `PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`. M00_L05 remains `COMPLETE /
FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

The User prepared M00_L06 from that final predecessor and supplied the accepted
baseline: `BUILD SUCCESSFUL in 40s`, 7 actionable tasks, 6 executed, 1
up-to-date, exit code `0`. The independent inheritance audit found 299 of 299
governed files byte-identical with no unexpected delta. After Architect
acceptance, the Final Design Lock passed at
`PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

M00_L06 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; active lesson count is `1`. The locked concept is scheduler-
managed ownership of the existing Feeder semantic API through
`RunFeederCommand` and Left Bumper `whileTrue`. Implementation is `NOT STARTED`.
No Java or test work is claimed. `FeederIONoop` remains the only runtime,
real hardware remains deferred, and CAN 45-49 remains a planning reservation.

## M00_L06 implementation, verification, and bounded documentation repair — 2026-09-19

The preceding activation section records the historical state before separate
implementation authorization. That authorization was subsequently accepted and
consumed. M00_L06 implementation is complete within the exact two-file
production boundary: `RunFeederCommand.java` was created and
`RobotContainer.java` was modified for Left Bumper `whileTrue` composition.
Three focused tests were created, and the inherited
`FeederArchitectureBoundaryTest.java` reconciliation is classified as
`EXPECTED INHERITED TEST CONTRACT EVOLUTION`, not a production defect.

The Independent Static Rereview passed. The four authorized focused test
classes passed with `BUILD SUCCESSFUL in 7s`, four actionable tasks up-to-date,
and exit code `0`. The full `gradlew clean build` regression passed with
`BUILD SUCCESSFUL in 37s`, seven actionable tasks executed, and exit code `0`.
Bounded Simulation checkpoints A-G passed, including Left Bumper release stop
and disable-while-held stop. Evidence remains `THEORY VERIFIED`, `SIMULATION
VERIFIED`, and `REAL HARDWARE DEFERRED`.

The documentation phase completed. The first Independent Closure Review
returned `HOLD` for three documentation/lifecycle inconsistencies, and the
authorized bounded repair is now complete. Independent closure rereview remains
pending. M00_L06 remains the sole `IN_PROGRESS / ACTIVE / EDITABLE` lesson with
active lesson count `1`; it is not `COMPLETE`, `FROZEN`, or `PUBLISHED`.
`FeederIONoop` remains the only runtime implementation, CAN 45-49 remains a
planning reservation only, and M00_L14 through M00_L16 remain protected.

## M00_L06 final lifecycle freeze — 2026-09-20

The preceding implementation and repair section is preserved as historical.
After the bounded transition-history repair, the final Independent Closure
Rereview passed with verdict `READY_FOR_FREEZE` and no remaining findings. The
Architect accepted the result through
`PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and authorized
`AUTHORIZED_FOR_FREEZE`.

M00_L06 is now `COMPLETE / FROZEN / READ-ONLY`. Active lesson count is `0`, no
M00 lesson is active, and M00_L07 remains `NOT ACTIVE / NOT CREATED`. Accepted
evidence remains `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE
DEFERRED`; `FeederIONoop` remains the only runtime implementation and CAN 45-49
remains planning-only. User-owned publication is pending, and no Git commit,
push, or publication is claimed.

## M00_L06 primary publication and metadata reconciliation — 2026-09-20

The preceding freeze section remains the historical pre-publication state.
Accepted gate `PASS_M00_L06_PRIMARY_PUBLICATION` records primary commit
`f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject `Complete M00_L06 Feeder command ownership`.
The User-owned primary push passed, and accepted
remote evidence records `HEAD = origin/main =
f102a5e662877f8cb49eb63f2cfd888ac356bea4`; primary remote alignment is
`PASS`.

M00_L06 remains `COMPLETE / FROZEN / READ-ONLY`. Active lesson count remains
`0`, no M00 lesson is active, and M00_L07 remains `NOT ACTIVE / NOT CREATED`.
Publication metadata reconciliation is `COMPLETE / PREPARED FOR USER COMMIT`.
The separate metadata Git publication and final publication verification remain
`PENDING`, so final `PUBLISHED / VERIFIED` status is not yet claimed.

## M00_L07 Controlled Activation — 2026-09-20

The preceding M00_L06 publication state is preserved as historical. The
accepted activation prerequisite records M00_L06 as `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`.

The User prepared `M00_L07_FlywheelFoundation` from that final predecessor and
supplied the accepted untouched-inheritance baseline: `BUILD SUCCESSFUL in
38s`, 6 actionable tasks, all 6 executed. The Architecture / Inheritance Audit
passed with 306 of 306 governed files byte-identical and no missing, added, or
changed governed files. The Architect accepted
`PASS_M00_L07_FINAL_DESIGN_LOCK`.

M00_L07 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson. Active lesson count is `1`, and the current active M00 lesson is
`M00_L07`. The one new concept is: Flywheel is one independently owned
rotational-speed mechanism.

Controlled Activation is documentation and lifecycle work only. Implementation
is `PENDING SEPARATE AUTHORIZATION`; no Flywheel production Java, test Java,
command, controller binding, Constants entry, hardware adapter, or autonomous
integration is added. The locked future runtime is `FlywheelIONoop` only. CAN
50-54 remains a planning reservation, physical Flywheel hardware remains
unknown, and real hardware remains deferred. M00_L08 is `INACTIVE / NOT
CREATED`.

## M00_L07 implementation and verification reconciliation — 2026-09-20

The preceding Controlled Activation record is preserved as historical.
Governance adjudication and the independent activation rereview passed. The
separately authorized Flywheel Foundation implementation was accepted at
`PASS_M00_L07_IMPLEMENTATION_REPORT_ACCEPTED`: vendor-neutral Flywheel
IO/IOInputs, `FlywheelIONoop`, immutable Observation, subsystem ownership,
read-only telemetry, and bounded `RobotContainer`/`RobotTelemetry`
composition are implemented. No inherited test was modified.

The initial Independent Static Review accepted production but returned `HOLD`
for four focused-test quality findings. A bounded four-test repair closed three;
the first rereview retained one architecture-test source-parsing `HOLD`. A
final repair changed only `FlywheelArchitectureBoundaryTest.java`, replacing
comment-sensitive raw-source checks with semantic type inspection and
comment-free import parsing. Final static rereview passed at
`PASS_M00_L07_FINAL_INDEPENDENT_STATIC_REREVIEW`.

User evidence records the six focused classes as `BUILD SUCCESSFUL` and
`FOCUSED TESTS: PASS`. The clean full regression passed with `BUILD
SUCCESSFUL in 36s`; all 7 actionable tasks executed. Bounded Simulation passed
for Disabled initial, Teleop Enabled idle without Flywheel controller action,
and return to Disabled. All three checkpoints reported unavailable,
disconnected, invalid velocity, `velocityRpm = 0.0`, and `STOPPED`. The
invalid `0.0` RPM is the canonical `FlywheelIONoop` representation and is
not a verified physical zero-speed measurement.

Evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
Documentation reconciliation is complete and the next gate is Independent
Closure Review. M00_L07 remains the sole `IN_PROGRESS / ACTIVE / EDITABLE
WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`; completion,
freeze, read-only state, and publication are not claimed. M00_L08 remains
inactive/uncreated, and M00_L08 closed-loop velocity, M00_L09 ready-at-speed,
Flywheel command ownership, shooting coordination, Feeder/Flywheel
orchestration, NamedCommands, and autonomous mechanism integration remain
protected future scope.

## M00_L07 controlled freeze transition — 2026-09-20

The preceding implementation and verification record is preserved as
historical pre-freeze state. The Independent Closure Review passed at
`PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` with `CLOSURE_REVIEW_PASS`,
`READY_FOR_FREEZE_AUTHORIZATION`, and no remaining findings. The Architect
authorized the controlled freeze transition.

M00_L07 is now `COMPLETE / FROZEN / READ-ONLY`. Active lesson count is `0`,
and the current active M00 lesson is `NONE`. Evidence remains `THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. The accepted
implementation, final static rereview, focused tests, clean regression, bounded
Simulation, and documentation reconciliation remain unchanged.

Publication is `PENDING / NOT YET PUBLISHED`; no Git commit, push, remote
alignment, or publication verification is claimed. M00_L08 remains `INACTIVE /
NOT CREATED`. The locked M00_L08 closed-loop velocity, M00_L09 ready-at-speed,
and later command, coordination, and autonomous-integration scopes remain
protected.

## M00_L07 primary publication and metadata reconciliation — 2026-09-20

The preceding controlled-freeze state is preserved as historical. Accepted
gate `PASS_M00_L07_PRIMARY_PUBLICATION_EVIDENCE` records the User-owned primary
publication commit `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` with subject
`Complete M00_L07 Flywheel foundation`. Primary push is `PASS`, and accepted
remote evidence records `HEAD = origin/main =
50e5f440bb0c9d96bdcd57eed533651d8d59ca93`; primary remote alignment is
`PASS`.

M00_L07 remains `COMPLETE / FROZEN / READ-ONLY`, active lesson count remains
`0`, and the current active M00 lesson remains `NONE`. Publication metadata
reconciliation is complete and prepared for User commit. The metadata commit
and metadata push remain `PENDING USER ACTION`, and final publication
verification remains `PENDING`; final `PUBLISHED / VERIFIED` status is not yet
claimed. M00_L08 remains `INACTIVE / NOT CREATED`.

---

# Lesson Structure
# Cấu trúc một Lesson

```text
D00_L02_Drivebase_Safety_Configuration/
│
├── docs/
├── src/
├── vendordeps/
├── gradle/
├── build.gradle
├── settings.gradle
├── WPILib-License.md
└── LESSON_STATUS.md
```

---

## docs/

Contains the learning documentation for this lesson.

Chứa tài liệu học của lesson.

Example:

```
D00_L01_to_D00_L02_Step_by_Step.md
```

---

## src/

Robot source code.

Mã nguồn robot.

---

## LESSON_STATUS.md

Records the lesson status.

Ghi lại trạng thái lesson.

Typical information:

- Current lesson
- Previous lesson
- Build result
- Simulation result
- Real robot result
- Git status

---

# Learning Workflow
# Quy trình học

```text
Previous Lesson Completed
            │
            ▼
Copy Previous Project
            │
            ▼
Rename New Lesson
            │
            ▼
Remove Generated Folders
            │
            ▼
Baseline Build
            │
            ▼
Learn One New Concept
            │
            ▼
Build
            │
            ▼
Simulation
            │
            ▼
Real Robot
            │
            ▼
Create Step-by-Step Guide
            │
            ▼
Git Commit
            │
            ▼
Git Push
```

---

# Required Documents
# Tài liệu bắt buộc

Every completed lesson contains:

- source code
- lesson status
- transition guide

Mỗi lesson hoàn thành bao gồm:

- mã nguồn
- trạng thái lesson
- tài liệu chuyển đổi

---

# Repository Rules
# Quy tắc Repository

Always read:

1. AGENTS.md
2. Engineering Documents
3. Current Lesson

before modifying code.

Luôn đọc:

1. AGENTS.md
2. Tài liệu kỹ thuật
3. Lesson hiện tại

trước khi sửa mã nguồn.

---

# Goal
# Mục tiêu

Learn professional robot software engineering through continuous inheritance development.

Học phát triển phần mềm robot chuyên nghiệp thông qua phát triển kế thừa liên tục.
---

## M00_L08 controlled lifecycle activation — 2026-09-20

The accepted M00_L08 preparation baseline, Architecture / Inheritance Audit,
and Final Design Lock are complete. The User copied the frozen M00_L07
snapshot, removed generated artifacts, and supplied the passing untouched-copy
baseline. Production inheritance is 103/103 byte-identical and test
inheritance is 96/96 byte-identical; the accepted overall non-generated
comparison is 711/711 identical.

M00_L08 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson, with active lesson count `1` and current active lesson `M00_L08`.
Its one new concept is vendor-neutral Flywheel closed-loop velocity control
through one validated semantic RPM request while preserving measurement-only
Observation and explicit safe stop.

The locked request is `void requestVelocity(double targetRpm)` in finite,
nonnegative Flywheel mechanism RPM. Zero is canonical safe stop;
`requestSpin()` is `REMOVED / SUPERSEDED`; requested states are `STOPPED` and
`VELOCITY_REQUESTED`; Observation remains measurement-only; and runtime remains
`FlywheelIONoop` only. Hardware facts, CAN assignments, gains, target RPM, and
physical tuning remain unknown or deferred.

Focused tests and test doubles are not runtime Simulation evidence. Future
bounded runtime Simulation is limited to deterministic Noop composition,
measurement/telemetry state, STOPPED idle behavior, no automatic Teleop
request, and mode-transition persistence. Implementation and verification are
pending separate authorization and User evidence. M00_L07 remains frozen,
published, and verified; M00_L09 remains inactive and uncreated.

### M00_L08 activation documentation repair — 2026-09-20

The future FlywheelIO contract is explicitly limited to
`FlywheelIOInputs.available`, `connected`, `velocityValid`, and `velocityRpm`,
plus exactly `updateInputs(FlywheelIOInputs)`,
`requestVelocity(double targetRpm)`, and `stop()`. `requestSpin()` is
`REMOVED / SUPERSEDED`; vendor types, vendor control objects, gains, and
hardware-configuration parameters are prohibited.

Future positive-request ordering is validate, record target, set
`VELOCITY_REQUESTED`, replace immutable Observation, then forward exactly one
IO request. A forwarding exception leaves target/state/Observation recorded,
propagates, does not roll back, and is not retried by `periodic()`. Zero sets
zero/`STOPPED`, replaces Observation, and forwards exactly one stop. Invalid
NaN, infinities, or negative values set zero/`STOPPED`, replace Observation,
attempt stop, and throw `IllegalArgumentException`; stop failure is suppressed
on that exception and no invalid value is forwarded. Explicit stop is
unconditional and preserves zero/`STOPPED`/Observation if IO throws; no
automatic restart or periodic reissue exists.

`FlywheelIONoop` always reports `available=false`, `connected=false`,
`velocityValid=false`, and `velocityRpm=0.0`; requests and stop are safe
deterministic no-ops without convergence or physical modeling. The zero RPM
value is an invalid-Noop representation, not measured physical zero RPM.

`RobotContainer` remains unchanged and composes exactly
`new FlywheelSubsystem(new FlywheelIONoop())`; it has no Flywheel command,
binding, default command, direct request/stop, autonomous registration,
NamedCommands, event markers, Feeder/Flywheel coordination, hardware
selection, or Flywheel-specific real/simulation branch.

Future test changes are limited to the six existing Flywheel tests named in
the active lesson records; no new or unrelated test file is authorized.

## M00_L08 post-verification documentation reconciliation — 2026-09-20

The final M00_L08 implementation and accepted User-owned evidence are
recorded here without advancing lifecycle. The lesson remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`; Independent
Closure Review is `PENDING`, Freeze is `NOT AUTHORIZED`, Publication is
`NOT AUTHORIZED`, and M00_L09 remains `INACTIVE / NOT CREATED`.

M00_L08 introduces exactly one concept: vendor-neutral Flywheel closed-loop
velocity control through one validated semantic mechanism-RPM request,
measurement-only Observation, and explicit safe stop. The API is
`void requestVelocity(double targetRpm)`; `requestSpin()` is
`REMOVED / SUPERSEDED`; requested states are `STOPPED` and
`VELOCITY_REQUESTED`. `FlywheelIOInputs` is exactly
`available`, `connected`, `velocityValid`, and `velocityRpm`, with only
`updateInputs(...)`, `requestVelocity(...)`, and `stop()`. Valid targets are
finite and nonnegative; positive requests forward once, zero (`+0.0` or
`-0.0`) is canonical stop, and NaN/infinite/negative values fail closed to
zero/`STOPPED`, update Observation, attempt stop, throw
`IllegalArgumentException`, and suppress stop failure. Stop records zero and
`STOPPED` before unconditional IO stop; IO failure does not roll back or
restart. `periodic()` only refreshes inputs and Observation. Noop reports
false/false/false/0.0, and CAN 50–54 plus real hardware remain deferred.

Final inheritance reconciliation is production `103 compared / 99
byte-identical / 4 changed / 0 missing / 0 added`, with changed files
`FlywheelIO.java`, `FlywheelIONoop.java`, `FlywheelObservation.java`, and
`FlywheelSubsystem.java`. Tests are `96 compared / 90 byte-identical / 6
changed / 0 missing / 0 added`, limited to the six named Flywheel-focused
tests. Constants, RobotContainer, telemetry, Gradle, vendordeps, deploy, and
M00_L07 remain unchanged.

The initial static review `HOLD` (stale stop intent, weak Noop assertions,
missing negative-zero coverage, brittle comment stripping) was repaired in a
bounded change; the independent static re-review is `PASS` under
`PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`. Accepted focused evidence is
`BUILD SUCCESSFUL in 16s`, `4 actionable tasks: 3 executed, 1 up-to-date`,
`FOCUSED TESTS: PASS` under `PASS_M00_L08_USER_FOCUSED_TESTS`. Accepted clean
regression evidence is `BUILD SUCCESSFUL in 26s`, `7 actionable tasks: 7
executed`, `CLEAN REGRESSION: PASS` under
`PASS_M00_L08_CLEAN_FULL_REGRESSION`.

Accepted bounded Simulation is `PASS_M00_L08_BOUNDED_SIMULATION`:

- Disabled: Available=false, Connected=false, RequestedState=STOPPED,
  VelocityRpm=0.0, VelocityValid=false.
- Teleoperated enabled with no driver action: Robot Enabled=Yes and the same
  Flywheel values; no automatic velocity request.
- Return Disabled: FMS Robot Enabled=No and the same Flywheel values.

These checkpoints verify only runtime Noop composition, telemetry/state
presence, deterministic invalid measurement, STOPPED idle behavior, and mode
persistence. `VelocityRpm=0.0` with `VelocityValid=false` is not measured
physical zero RPM. Physical regulation, convergence, gains, feedforward,
sensor fidelity, RPM accuracy, direction, CAN, physical stop behavior, and
**requestVelocity runtime exercise was NOT claimed**; request semantics were
verified by focused/unit tests. Evidence classification is exactly
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

The M00_L09 Ready-at-Speed lesson and all later readiness, command, shooting,
feeder-coordination, automatic firing, event-marker, and autonomous
mechanism scope remain protected.

## M00_L08 controlled freeze transition — 2026-09-20

The accepted Independent Closure Review gate
`PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW` returned
`READY_FOR_FREEZE_AUTHORIZATION`, and the Architect authorized the controlled
freeze transition. No production, test, configuration, dependency,
deployment, or verification evidence changed.

M00_L08 is now `COMPLETE / FROZEN / READ-ONLY`. Active lesson count is `0`,
and the current active M00 lesson is `NONE`. Independent Closure Review is
`PASS`; Freeze is `COMPLETE / FROZEN / READ-ONLY`; Publication is
`PENDING / NOT YET PUBLISHED`; Final Publication Verification is
`NOT YET PERFORMED`; and M00_L09 remains `INACTIVE / NOT CREATED`.

The locked one-concept Flywheel contract, exact IO methods and inputs,
finite-nonnegative mechanism RPM validation, canonical zero and negative-zero
safe stop, invalid fail-closed behavior, immutable Observation, output-free
`periodic()`, deterministic `FlywheelIONoop`, read-only telemetry, and
unchanged RobotContainer boundary remain preserved. Evidence classification
remains exactly `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`. The bounded Simulation did not claim runtime `requestVelocity`
exercise or physical Flywheel behavior.

Final integrity remains production `103 / 99 / 4 / 0 / 0` and tests
`96 / 90 / 6 / 0 / 0` for Compared / Byte-identical / Changed / Missing /
Added. M00_L07 remains the accepted published and verified predecessor at
primary `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` and metadata
`62199c3ecd1ac7940e188dbef3d28de784c8da2c`; no publication claim is made for
M00_L08.

## M00_L08 primary publication and metadata reconciliation — 2026-09-20

The accepted gate `PASS_M00_L08_PRIMARY_PUBLICATION` records the User-owned
primary publication of the frozen M00_L08 snapshot. M00_L08 remains
`COMPLETE / FROZEN / READ-ONLY`. Primary publication is complete at
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` with subject
`Complete M00_L08 Flywheel closed-loop velocity`. Primary push is `PASS`, and
local HEAD and `origin/main` both equal that SHA; primary remote alignment is
`PASS`.

Publication metadata is `PENDING METADATA COMMIT`; no metadata commit SHA,
metadata push, or final `PUBLICATION_VERIFIED` state is claimed. Final
independent publication verification remains `PENDING / NOT YET PERFORMED`.
Active lesson count remains `0`, the current active M00 lesson remains `NONE`,
and M00_L09 remains `INACTIVE / NOT CREATED`.

The frozen lesson-local snapshot is intentionally unchanged under the
Historical Snapshot Model; its historical pending-publication wording is not a
defect and does not require a third publication commit. Evidence remains
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`, and
`requestVelocity` runtime Simulation exercise was NOT claimed.

## M00_L09 Controlled Activation — 2026-09-21

M00_L09 — Flywheel Ready-at-Speed is now the sole active M00 lesson after the
accepted preparation baseline, architecture/inheritance audit, and Final
Design Lock (`READY_FOR_CONTROLLED_ACTIVATION`). Controlled Activation is
documentation-only: M00_L09 is `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL
DESIGN LOCK`, Active Lesson Count `1`, Current Active M00 Lesson `M00_L09`,
implementation `NOT STARTED`, and Independent Activation Review `PENDING`.

The one locked concept is instantaneous vendor-neutral readiness owned by
`FlywheelSubsystem`, with immutable Observation field `readyAtSpeed` and
telemetry field `ReadyAtSpeed`. The exact inclusive symmetric tolerance is
`Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm = 50.0` mechanism RPM,
classified as provisional software policy only, not hardware-tuned or
real-robot validated. IO, Noop, RobotTelemetry, RobotContainer, commands,
coordination, autonomous, and hardware remain unchanged; no dwell, debounce,
hysteresis, history, state machine, or automatic action is authorized.

M00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` and
M00_L10 is `INACTIVE / NOT CREATED`. No source/test implementation, build,
test, Simulation, closure, freeze, publication, or Git evidence is claimed.

## M00_L09 activation documentation repair — 2026-09-21

The Architect acceptance gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`. The
bounded engineer task verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`;
these are compatible but distinct. Independent Activation Review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW` pending re-review.

Canonical external predecessor provenance is
`M00_L08 = COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, primary
`5daecd970ff95fb906d6de9d5bc22a5cb094877d`, metadata
`a76dc33c2b485b4988e7058cbfed0fa3362cc560` (parent = primary), final
remote-aligned HEAD = metadata SHA, and final verdict `PUBLICATION_VERIFIED`.
No third publication commit was required. Historical frozen snapshots may
retain pending wording under the passing two-commit Historical Snapshot Model;
that wording is historical, not contradictory, and frozen M00_L08 was not
modified.

The locked readiness formula, 50.0 RPM provisional software-policy tolerance,
observation-only boundary, telemetry read-only rule, forwarding-exception
semantics, automatic-action prohibition, truth-table boundaries, floating-point
comparison policy, focused-test matrix, evidence plan, and complete deferred
hardware list are maintained in the current M00_L09 records. No source, test,
configuration, build, Simulation, freeze, publication, Git, or M00_L10 change
is claimed.

## M00_L09 implementation, verification, and documentation reconciliation — 2026-09-21

M00_L09 remains the sole active lesson and now records the completed locked
implementation of exactly one concept: instantaneous vendor-neutral Flywheel
Ready-at-Speed classification. The accepted implementation gate is
`PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED`. Production integrity is
`103 / 99 / 4 / 0 / 0` (compared, byte-identical, changed, missing, added),
test integrity is `96 / 92 / 4 / 0 / 0`, and deploy/configuration integrity is
`4 / 4 / 0 / 0 / 0`; no new files or unrelated boundaries changed.

The static-review chronology is preserved as initial `HOLD`, bounded repair
`PASS`, and final independent static re-review `PASS`. User evidence accepts
`PASS_M00_L09_USER_FOCUSED_TESTS` (`BUILD SUCCESSFUL in 22s`; four actionable
tasks, three executed and one up-to-date) and
`PASS_M00_L09_CLEAN_FULL_REGRESSION` (`BUILD SUCCESSFUL`; seven actionable
tasks, all seven executed).

The accepted Simulation gates are
`PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`, and
`PASS_M00_L09_BOUNDED_SIMULATION`. Disabled, Teleop idle, and Disabled again
all retained `Available=false`, `Connected=false`, `ReadyAtSpeed=false`,
`RequestedState=STOPPED`, `VelocityRpm=0.0`, and `VelocityValid=false`.
This proves only bounded Noop/lifecycle and read-only telemetry behavior; it
does not claim runtime 50-RPM boundaries, physical convergence, sensor
fidelity, CAN, or hardware readiness. Evidence classification is exactly
`THEORY VERIFIED`, `SIMULATION VERIFIED`, `REAL HARDWARE DEFERRED`.

Documentation Reconciliation is `COMPLETE`. Independent Closure Review is
`PENDING`; Freeze and Publication are `NOT AUTHORIZED`. Active Lesson Count is
`1`, current active M00 lesson is M00_L09, and M00_L10 remains
`INACTIVE / NOT CREATED`. M00_L08 remains frozen and unchanged.

## M00_L09 controlled freeze transition — 2026-09-21

The final independent closure re-review passed with
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`, and Architect freeze
authorization was accepted. M00_L09 is now `COMPLETE / FROZEN / READ-ONLY`.
No source, test, deploy/configuration, support, or frozen M00_L08 content was
changed by the freeze transition.

The final technical contract remains frozen: one vendor-neutral instantaneous
Ready-at-Speed concept, the exact immutable Observation and read-only telemetry
boundary, inclusive symmetric 50.0 RPM provisional software-policy tolerance,
and no automatic action. Production, test, and full deploy/config/support
integrity remain `103 / 99 / 4 / 0 / 0`, `96 / 92 / 4 / 0 / 0`, and
`24 / 24 / 0 / 0 / 0` respectively.

Evidence remains exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL
HARDWARE DEFERRED`. Independent Freeze Review is `PENDING`; Publication is
`PENDING / NOT YET PUBLISHED`; no publication SHA or Git event is claimed.
Active Lesson Count is `0`, Current Active M00 Lesson is `NONE`, and M00_L10
remains `INACTIVE / NOT CREATED`.

## M00_L09 primary publication and metadata reconciliation — 2026-09-21

The accepted Independent Freeze Review gate is
`PASS_M00_L09_INDEPENDENT_FREEZE_REVIEW`. M00_L09 remains
`COMPLETE / FROZEN / READ-ONLY`, and its frozen lesson-local tree was not
modified. The accepted primary snapshot gate is
`PASS_M00_L09_PRIMARY_FROZEN_SNAPSHOT_COMMIT`.

Primary publication identity:

- SHA: `3c822a1e3956850c9d0ba9954c5b163d83b801b9`
- Subject: `Complete M00_L09 Flywheel ready-at-speed`

The primary snapshot exists locally but has not been pushed. No primary remote
alignment, origin state, or remote publication verification is claimed. The
canonical publication phase remains `PENDING METADATA COMMIT`. No metadata
commit SHA is available, no metadata push has occurred, and no
`PUBLICATION_VERIFIED` verdict is claimed.

This preserves the M00_L08 two-commit Historical Snapshot model: primary frozen
lesson snapshot, repository-level metadata commit, and later external final
publication verification. No third commit is required merely to record final
verification. Historical pending wording in frozen lesson-local records is
preserved.

Evidence remains exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL
HARDWARE DEFERRED`. Integrity remains production `103 / 99 / 4 / 0 / 0`, tests
`96 / 92 / 4 / 0 / 0`, and full deploy/config/support `24 / 24 / 0 / 0 / 0`.
Active Lesson Count remains `0`, the current active M00 lesson remains `NONE`,
and M00_L10 remains `INACTIVE / NOT CREATED`. The M00 roadmap is unchanged.

## M00_L10 controlled activation — 2026-09-21

M00_L09 is the accepted frozen predecessor and its publication is reconciled
as primary SHA `3c822a1e3956850c9d0ba9954c5b163d83b801b9`, metadata SHA
`249100db23262430ce2557eaa5e67d70b7b0a79c`, final verdict
`PUBLICATION_VERIFIED`. M00_L10 is the sole active lesson with state
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`; implementation is
`NOT STARTED`, Independent Activation Review is `PENDING`, Freeze and
Publication are `NOT AUTHORIZED`, and M00_L11 is inactive/not created.

This record is documentation/lifecycle activation only. The exact one-concept
Elevator position/reference contract, normalization, future file boundaries,
test matrix, and evidence limits are in the M00_L09→M00_L10 transition guide.
No source, test, build, Simulation, Git, frozen predecessor, or roadmap change
is claimed.

## M00_L10 primary publication and metadata reconciliation — 2026-09-22

The accepted Independent Freeze Review gate is
`PASS_M00_L10_INDEPENDENT_FREEZE_REVIEW`. M00_L10 remains
`COMPLETE / FROZEN / READ-ONLY`, and its frozen lesson-local tree was not
modified. The accepted primary snapshot gate is
`PASS_M00_L10_PRIMARY_PUBLICATION_SNAPSHOT`.

Primary publication identity:

- SHA: `531bceabddf53f194b1edaabbd972ee6865e9ff0`

The User independently verified that the primary snapshot contains only files
inside the frozen M00_L10 lesson folder. The canonical publication phase is
`PENDING METADATA COMMIT`. No metadata commit SHA is available, no metadata
push has occurred, and no `PUBLICATION_VERIFIED` verdict is claimed.

This preserves the two-commit Historical Snapshot model: frozen lesson
snapshot primary commit, repository-level metadata commit, and later external
final publication verification. No third commit is required merely to record
final verification. Frozen M00_L10 lesson-local pending wording remains
unchanged.

M00_L10 introduced exactly one concept: vendor-neutral Elevator position
observation and reference semantics. Position control remains M00_L11 scope,
homing and trusted-reference establishment remain M00_L12 scope, and
travel-limit enforcement remains M00_L13 scope. Evidence remains exactly
`THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.
Active Lesson Count remains `0`, Current Active M00 Lesson remains `NONE`,
M00_L11 remains `INACTIVE / NOT CREATED`, and the locked M00_L01–M00_L16
roadmap remains unchanged with no M00_L17.

## M00_L11 controlled activation — 2026-09-22

M00_L10 is accepted as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
with primary SHA `531bceabddf53f194b1edaabbd972ee6865e9ff0`, metadata SHA
`cb8c125da0bbc7e2bf0aeebe40163c1a72909445`, and final gate
`PASS_M00_L10_FINAL_PUBLICATION_VERIFICATION`.

M00_L11 consumed `PASS_M00_L11_PREPARATION_BASELINE`,
`PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT`,
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`, and
`PASS_M00_L11_FINAL_DESIGN_LOCK`. Inheritance is exact: production
`108 / 108 / 0 / 0 / 0`, tests `102 / 102 / 0 / 0 / 0`, deploy/config/support
`24 / 24 / 0 / 0 / 0`, and lesson-local documentation `98 / 98 / 0 / 0 / 0`;
unexpected substantive drift is `NONE`.

M00_L11 is the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`
lesson, with Active Lesson Count `1`, Current Active M00 Lesson `M00_L11`,
implementation `NOT STARTED`, and Independent Activation Review `PENDING`.
M00_L12 remains `INACTIVE / NOT CREATED`.

The one locked concept is vendor-neutral Elevator closed-loop position request
semantics in meters. The future IO request is `requestPositionMeters(double)`;
requested states are exactly `STOPPED` and `POSITION_REQUESTED`; the future
observation adds requested state, target, and derived error to the inherited
five fields. Requests require finite target, valid position, and trusted
reference; finite negative targets remain valid and no physical travel clamp is
introduced. Noop remains safe and deterministic. RobotContainer,
RobotTelemetry, Constants, commands, bindings, autonomous integration, real
adapters, and ElevatorIOSim remain unchanged. M00_L12 owns homing/reference
establishment and M00_L13 owns travel-limit safety.

The activation chronology is recorded in
`real_robot_programming/module_M00/M00_L11_ElevatorClosedLoopPosition/docs/M00_L10_to_M00_L11_Step_by_Step.md`.
No implementation, verification, closure, freeze, publication, or Git result
is claimed.

## M00_L11 implementation, verification, and documentation reconciliation — 2026-09-22

The preceding M00_L11 activation record is historical. The current accepted
state is `IN_PROGRESS / ACTIVE / IMPLEMENTATION COMPLETE`, with focused tests,
clean regression, bounded Simulation, and documentation reconciliation passed.
Independent Closure Review remains `PENDING`; Freeze and Publication are not
authorized. M00_L12 homing/reference establishment, M00_L13 travel-limit
safety, and real hardware remain protected/deferred.

The accepted focused-test gates include the compile-helper failure forensics,
package-private visibility repair and re-review, the later observation-fixture
failure forensics, the `positionMeters 1.25 -> 1.50` fixture repair and
re-review, and `PASS_M00_L11_USER_FOCUSED_TESTS`. The forced fresh run passed
with `BUILD SUCCESSFUL in 18s` and four executed actionable tasks. Clean
regression passed with `BUILD SUCCESSFUL in 37s` and seven executed actionable
tasks. Both earlier failures remain preserved as test defects; neither was a
production failure.

User Simulation evidence is limited to Noop runtime composition, exact eight
telemetry values, disabled idle, Teleop enabled persistence, and return to
Disabled persistence. The final evidence classification is
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

## M00_L11 controlled freeze — 2026-09-22

The preceding implementation and documentation reconciliation section is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`. The reconciled implementation, forced focused tests, clean regression, and bounded Simulation evidence remain unchanged.

The authoritative M00_L11 state is:

```text
STATUS: COMPLETE
LIFECYCLE: COMPLETE / FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
IMPLEMENTATION: COMPLETE
STATIC REVIEW: PASS
FOCUSED TESTS: PASS_M00_L11_USER_FOCUSED_TESTS
CLEAN REGRESSION: PASS_M00_L11_USER_CLEAN_REGRESSION
SIMULATION: PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION
DOCUMENTATION RECONCILIATION: COMPLETE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT FREEZE REVIEW: PENDING
PUBLICATION: NOT YET PUBLISHED / USER-OWNED
M00_L12: INACTIVE / NOT CREATED
M00_L13: FUTURE SCOPE / NOT ACTIVATED
```

The exact one-concept Design Lock, M00_L10 predecessor integrity, focused-test history, clean-regression evidence, bounded Simulation boundaries, and real-hardware deferral are preserved. No M00_L12 creation or activation, roadmap movement, publication, remote verification, or hardware verification is claimed. Earlier active-state text remains historical chronology.

## M00_L11 Publication Phase 1 Primary Frozen Snapshot — 2026-09-22

The User-accepted gate is `PASS_M00_L11_PUBLICATION_PHASE_1_PRIMARY_FROZEN_SNAPSHOT`.
The authoritative primary frozen lesson snapshot is:

```text
SHA: 385bf1d4ff6550dafb3e15de06ceaa9f1e9edc03
COMMIT SUBJECT: Complete M00_L11 Elevator closed-loop position
BRANCH: main
```

User evidence confirms that this primary commit contains only M00_L11 lesson files and that the Git index was empty after the primary commit. The primary frozen snapshot is committed locally; it has not yet been pushed as part of the final publication workflow. No metadata publication commit SHA exists yet.

The current publication metadata state is:

```text
M00_L11: COMPLETE / FROZEN / READ-ONLY
PRIMARY FROZEN SNAPSHOT: 385bf1d4ff6550dafb3e15de06ceaa9f1e9edc03 / COMMITTED LOCALLY
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
NEXT ROADMAP LESSON: M00_L12 - Elevator Homing
M00_L12: INACTIVE / NOT CREATED
M00_L13: FUTURE ELEVATOR TRAVEL-LIMIT SAFETY
METADATA PUBLICATION COMMIT: NOT YET CREATED / USER-OWNED
FINAL REMOTE PUBLICATION VERIFICATION: PENDING / EXTERNAL FUTURE GATE
```

This record follows the Historical Snapshot publication model. It does not claim `REMOTE VERIFIED`, `FINAL PUBLICATION VERIFIED`, `origin/main` alignment, remote-main alignment, or a metadata commit SHA. The M00_L11 frozen lesson and its technical Design Lock remain unchanged.

## M00_L12 Controlled Freeze — 2026-09-23

The current authoritative M00_L12 lifecycle follows accepted closure gate `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW`, verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`, with no remaining legitimate closure findings. Earlier preparation, activation, implementation, verification, and documentation-reconciliation records remain historical chronology.

The lesson is now `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`. Its single concept remains a bounded, scheduler-managed Elevator homing lifecycle using vendor-neutral `requestHoming()` and normalized IO-reported `positionReferenced` as the sole trusted-reference authority. Zero position alone does not establish home. The accepted evidence remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; Simulation proves bounded software/runtime and truthful Noop behavior only. Physical homing, movement, sensor activation, calibration, reference accuracy, and hardware convergence remain unverified.

The accepted implementation deltas remain production `109 / 105 / 4 / 0 / 1`, tests `102 / 98 / 4 / 0 / 1`, and deploy/config/support `24 / 24 / 0 / 0 / 0` (compared / identical / changed / missing / added). Focused tests remain `PASS_M00_L12_USER_FOCUSED_TESTS` (`--rerun-tasks`, `BUILD SUCCESSFUL in 35s`, four tasks executed); clean regression remains `PASS_M00_L12_USER_CLEAN_REGRESSION` (`BUILD SUCCESSFUL in 24s`, five tasks executed); bounded Simulation remains `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION`.

```text
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
M00_L12: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE
FREEZE RECONCILIATION: COMPLETE
INDEPENDENT FREEZE REVIEW: PENDING / NEXT GATE
PUBLICATION: NOT PUBLISHED / PENDING / USER-OWNED
PUBLICATION SHA: NONE / NOT YET ESTABLISHED
REAL HARDWARE: DEFERRED
M00_L13: INACTIVE / NOT CREATED
```

M00_L11 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. M00_L13 remains inactive and not created. Publication, publication SHAs, and remote verification remain pending User-owned Git gates. See the [M00_L11 to M00_L12 transition guide](real_robot_programming/module_M00/M00_L12_ElevatorHoming/docs/M00_L11_to_M00_L12_Step_by_Step.md) for the complete chronology.

## M00_L12 Metadata Publication Reconciliation — 2026-09-23

The accepted independent freeze-review gate is `PASS_M00_L12_FINAL_INDEPENDENT_FREEZE_REVIEW`, with verdict `FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION`. M00_L12 remains `COMPLETE / FROZEN / READ-ONLY`. The User-verified primary frozen snapshot gate is `PASS_M00_L12_PRIMARY_FROZEN_SNAPSHOT_COMMIT`:

```text
PRIMARY SHA: 5cc4c2c1bc6b4108f2c710c3ca0b0cdbdc26ba49
PRIMARY COMMIT SUBJECT: Complete M00_L12 Elevator homing
PRIMARY FROZEN SNAPSHOT: COMMITTED AND VERIFIED
USER EVIDENCE: HEAD contains only M00_L12 lesson files
```

The publication progression remains `PRIMARY FROZEN SNAPSHOT COMMITTED` → `METADATA PUBLICATION COMMIT PENDING` → `REMOTE PUSH PENDING` → `FINAL PUBLICATION VERIFICATION PENDING`.

```text
METADATA PUBLICATION COMMIT: PENDING
METADATA PUBLICATION SHA: NONE / NOT YET ESTABLISHED
REMOTE PUSH: PENDING
REMOTE PUBLICATION SHA: NONE / NOT YET VERIFIED
FINAL PUBLICATION VERIFICATION: PENDING
FULLY PUBLISHED: NO
```

M00_L12 is not marked fully published or remote verified.

The evidence classification remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Simulation evidence is bounded runtime and `ElevatorIONoop` verification only; physical homing is not established. Active Lesson Count remains `0`, Current Active M00 Lesson remains `NONE`, and M00_L13 Elevator Travel-Limit Safety remains `INACTIVE / NOT CREATED`. The frozen M00_L12 lesson files remain unchanged.

## Historical M00_L13 Controlled Activation Snapshot — 2026-09-23

The following records the lifecycle and evidence as they stood at activation. Pending implementation and verification statements in that snapshot are historical and are superseded by the current reconciliation below.

The accepted predecessor M00_L12 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Accepted primary SHA: 5cc4c2c1bc6b4108f2c710c3ca0b0cdbdc26ba49. Accepted metadata SHA: c1e90fad04469e5162b5c1814566dd534c6a0a6c. Final gate: PASS_M00_L12_FINAL_PUBLICATION_VERIFICATION. Its evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Earlier repository M00_L12 pending-publication blocks remain historical snapshots; accepted later User evidence establishes publication. Frozen M00_L12 lesson files are unchanged.

M00_L13 accepted preparation: PASS_M00_L13_UNTOUCHED_COPY_BASELINE_BUILD; User-supplied BUILD SUCCESSFUL in 35s, 6 actionable tasks, 6 executed. Accepted gates: PASS_M00_L13_ARCHITECTURE_INHERITANCE_AUDIT and PASS_M00_L13_FINAL_DESIGN_LOCK. The initial lifecycle and test-plan HOLDs were resolved by targeted re-reviews.

Current M00 lifecycle:

M00_L12: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L13: ACTIVE / IN_PROGRESS / EDITABLE WITHIN FINAL DESIGN LOCK
         NOT COMPLETE / NOT FROZEN / NOT PUBLISHED
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L13 — Elevator Travel-Limit Safety
IMPLEMENTATION AUTHORIZATION: NOT YET AUTHORIZED
NEXT GATE: INDEPENDENT ACTIVATION REVIEW
M00_L14: INACTIVE / NOT CREATED

The one locked concept is a vendor-neutral software operational travel envelope for Elevator closed-loop position requests in inherited logical meters, enforced before ElevatorIO. It is request-admission safety only; it does not claim physical hard-limit, continuous overtravel, overshoot, switch, motor-controller soft-limit, homing-redesign, commissioning, or coordination behavior. No physical travel minimum or maximum is established; hardware remains UNKNOWN / DEFERRED and real hardware remains deferred.

Implementation and test changes remain planned, not implemented or authorized. Constants.java, RobotContainer.java, IO contracts, observation, state, telemetry, homing command, and adapters remain unchanged. The subsystem is the planned sole enforcement owner. The accepted test plan includes outside-current/in-range-target admission and same-Observation identity on rejected below-min, above-max, and unconfigured requests.

M00_L13 tests and Simulation were NOT RUN / NOT VERIFIED at activation. Activation did not claim theory, Simulation, hardware, freeze, or publication gates as achieved. M00_L14 remained inactive/not created; M00_L15/L16 remained future scope. Earlier M00_L12 status snapshots and copied M00_L13 records are historical.

## Historical M00_L13 Freeze Reconciliation — 2026-09-24

Accepted current gates include implementation authorization and handoff, final independent static re-review, User focused tests, clean regression, bounded Simulation, and completed Documentation Reconciliation. The initial Closure Review returned HOLD_M00_L13_CLOSURE_REVIEW_STALE_AGENTS_IMPLEMENTATION_STATUS; PASS_M00_L13_CLOSURE_DOCUMENTATION_REPAIR reconciled the stale current AGENTS.md lifecycle sentence, and PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW returned CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE. Freeze Reconciliation is complete; Independent Freeze Review is next.

M00_L13 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED / NOT YET PUBLICATION-VERIFIED. Active Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L12 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14 remains INACTIVE / NOT CREATED.

The implemented single concept is a vendor-neutral software request-admission travel envelope in inherited logical meters, enforced before ElevatorIO. Production delta versus M00_L12: common 110, identical 109, changed 1, missing 0, added 1; ElevatorSubsystem.java changed and ElevatorTravelLimits.java added. Test delta: common 103, identical 101, changed 2, missing 0, added 1; ElevatorSubsystemTest.java and ElevatorArchitectureBoundaryTest.java changed, and ElevatorTravelLimitsTest.java added. Constants.java, RobotContainer.java, IO contracts, Observation/state, telemetry, homing command, and adapters remain unchanged.

The initial static-review HOLD was limited to test/architecture-guard defects; no production defect or Final Design Lock change occurred. The bounded repair changed only the two named test files, and final independent static re-review passed. Focused tests passed: PASS_M00_L13_USER_FOCUSED_TESTS, BUILD SUCCESSFUL in 42s, 4 actionable tasks executed. Clean regression passed: PASS_M00_L13_USER_CLEAN_REGRESSION, BUILD SUCCESSFUL in 25s, 5 actionable tasks executed, GRADLE_EXIT_CODE=0. The PowerShell NativeCommandError caused by a WPILib joystick stderr warning was an evidence-capture issue, not a Java/test failure.

Bounded Simulation passed as PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION for Disabled -> Teleop Enabled -> Disabled using the unchanged truthful Noop composition. This does not verify a configured envelope or physical travel-limit behavior. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Physical travel and hardware facts remain UNKNOWN / DEFERRED. Independent Freeze Review is PENDING. Primary frozen snapshot publication, metadata publication, User push, and Final Publication Verification remain pending / User-owned. No M00_L13 publication SHA is established; M00_L15/L16 remain future scope.

## Historical M00_L13 Metadata Publication Reconciliation before Commit 2 — 2026-09-24

Accepted gates: PASS_M00_L13_INDEPENDENT_FREEZE_REVIEW / FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION; PASS_M00_L13_PRIMARY_FROZEN_SNAPSHOT_PUBLICATION_COMMIT; and PASS_M00_L13_METADATA_PUBLICATION_RECONCILIATION. The User-owned primary frozen snapshot commit completed with SHA `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704`, subject `Complete M00_L13 Elevator travel-limit safety`.

Two safe primary-publication staging attempts were held: HOLD_M00_L13_PRIMARY_PUBLICATION_SCRIPT_BOUNDARY_MISMATCH and HOLD_M00_L13_PRIMARY_PUBLICATION_GIT_ADD_PATHSPEC_DEFECT. Both are classified as publication-script defects, not lesson, source, test, or freeze defects. The corrected User-owned staging flow created the primary commit. Accepted User evidence says no M00_L13 authored path remained dirty afterward; previously identified unrelated working-tree paths remain protected.

At the time this record was written, M00_L13 was COMPLETE / FROZEN / READ-ONLY, its primary snapshot was committed, and the metadata publication commit, push, and external Final Publication Verification had not yet occurred. That pending state is historical and is superseded below.

Active Lesson Count was 0; Current Active M00 Lesson was NONE. M00_L14 was INACTIVE / NOT CREATED; M00_L15/L16 remained future scope. Technical contracts and evidence were unchanged: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. Physical travel and hardware facts remained UNKNOWN / DEFERRED.

## Historical M00_L13 Publication State before M00_L14 Activation — 2026-09-25

This is an earlier publication-state snapshot. Its then-pending final verification statements are historical and are superseded by the accepted external M00_L13 verification recorded in the current M00_L14 activation state below.


M00_L13 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED. The Primary Frozen Snapshot Commit completed at `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704`, subject `Complete M00_L13 Elevator travel-limit safety`. The canonical metadata publication is established by Commit 2 of the two-commit Historical Snapshot model. The Metadata Commit identity and matching remote-main identity are external publication evidence and are not self-embedded. Final Publication Verification remains PENDING / EXTERNAL.

Active Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L14 is INACTIVE / NOT CREATED; M00_L15/L16 remain future scope. The publication model remains exactly two commits; no third verification-only commit is required.

The independent final publication reviews recorded `HOLD_M00_L13_FINAL_PUBLICATION_VERIFICATION_STALE_CURRENT_PUBLICATION_STATE` and `HOLD_M00_L13_FINAL_PUBLICATION_REREVIEW_STALE_POST_AMEND_CHRONOLOGY`. Both are historical publication metadata/documentation findings; the latter is addressed by this chronology repair. Classification: `PUBLICATION_METADATA_CHRONOLOGY_DEFECT`; `NO_LESSON_DEFECT`; `NO_PRODUCTION_DEFECT`; `NO_TEST_DEFECT`; `NO_ARCHITECTURE_DEFECT`; `NO_SIMULATION_DEFECT`; `NO_FREEZE_DEFECT`; `NO_PUBLICATION_IDENTITY_DEFECT`; `NO_TWO_COMMIT_MODEL_CHANGE`. Technical, test, Simulation, and hardware-evidence classifications remain unchanged.


## Historical M00_L14 Controlled Activation Snapshot — 2026-09-25

This records the activation-time state. Its pending implementation and
verification statements are superseded by the accepted post-verification
reconciliation below.

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Its
primary snapshot SHA is 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704; canonical
metadata SHA is 658d1e44c417763df3689b9b52e409161446c593. Its evidence is
THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

Accepted M00_L14 gates are PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD (6
actionable tasks, 6 executed, BASELINE_BUILD_EXIT_CODE=0),
PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT, and
PASS_M00_L14_FINAL_DESIGN_LOCK. Controlled Activation updates documentation
only; 339/339 authored files were identical to M00_L13 at audit.

Current lifecycle:

M00_L13: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L14: ACTIVE / IN_PROGRESS / NOT COMPLETE / NOT FROZEN / NOT PUBLISHED
Active Lesson Count: 1
Current Active M00 Lesson: M00_L14 — Shoot Coordination
M00_L15: FUTURE / INACTIVE / NOT CREATED
M00_L16: FUTURE / INACTIVE / NOT CREATED
Independent Activation Review: PENDING
Implementation Authorization: NOT AUTHORIZED / PENDING

The locked concept is scheduler-managed frc.robot.commands.ShootCommand
coordination of existing Flywheel ready-at-speed semantics and Feeder action.
It requires exactly FlywheelSubsystem and FeederSubsystem. Feed admission
requires readyAtSpeed() plus Feeder availability and connection.
FeederObservation.requestedState() tracks software intent only. Constructor RPM
is caller-supplied semantic configuration, not a hardware shooting value. The
command has no local readiness or feed-request authority and uses
transition-based Feeder requests and never reissues the Flywheel velocity
request from execute(). If the initial Feeder stop throws during
initialize(), Flywheel velocity is not requested; Flywheel stop is attempted
once and any cleanup failure is suppressed on the original Feeder exception.
If the Flywheel velocity request throws, the completed Feeder baseline stop is
not repeated; Flywheel stop is attempted once and any cleanup failure is
suppressed on the original request exception. If requestFeed() throws during
execute(), Feeder stop and Flywheel stop are both attempted once, even if
Feeder cleanup throws, and cleanup failures are suppressed on the original
request exception. If Feeder stop throws during readiness/admission loss in
execute(), it is not retried in that failing call and Flywheel stop is
attempted once. Every terminal end attempts Feeder stop then Flywheel stop;
when both throw, Feeder remains primary and Flywheel is suppressed. Original
RuntimeExceptions are rethrown; there is no retry loop, silent recovery,
clamp, rewrite, fallback, or routine java.lang.Error recovery.

Constants.java, RobotContainer.java, existing subsystems, IO, Observations,
telemetry, deploy files, and vendor adapters remain outside the future
implementation delta. The expected production change is one added
ShootCommand.java. No L14 source or tests have been implemented or authorized.
There is no L14 driver binding; the inherited Left Bumper manual Feeder command
remains, with scheduler requirements providing Feeder mutual exclusion. There
is no new shooting numerical authority.

Evidence at activation was THEORY VERIFICATION IN PROGRESS /
SIMULATION NOT TESTED / REAL HARDWARE DEFERRED. Flywheel and Feeder runtime
adapters are Noop, so runtime Simulation cannot establish a physical shot.
M00_L15 owns Intake-to-Feeder Coordination; M00_L16 owns Mechanism Autonomous
Event Integration.

## Historical M00_L14 Post-Verification Reconciliation — 2026-09-25

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14
is the sole ACTIVE / IN_PROGRESS lesson, NOT COMPLETE / NOT FROZEN / NOT
PUBLISHED; Active Lesson Count is 1. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED. Independent Closure Review is the next gate.

Accepted gates: PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION,
PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW,
PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW,
PASS_M00_L14_USER_FOCUSED_TESTS,
PASS_M00_L14_USER_CLEAN_REGRESSION, and
PASS_M00_L14_USER_BOUNDED_SIMULATION. The focused test command completed with
BUILD SUCCESSFUL in 10s (4 actionable tasks: 3 executed, 1 up-to-date;
FOCUSED_TEST_EXIT_CODE=0). The clean regression completed with BUILD SUCCESSFUL
in 30s (5 actionable tasks, 5 executed; CLEAN_REGRESSION_EXIT_CODE=0).

The bounded Simulation verified Disabled startup, Teleoperated enable, return
to Disabled with mechanism requested states STOPPED, and clean shutdown
(LAST_NATIVE_EXIT_CODE=0). RobotContainer remains unchanged and has no
ShootCommand binding. Simulation therefore verifies startup, mode transitions,
scheduler/integration stability, safe semantic mechanism state, and clean exit;
it did not execute ShootCommand through a RobotContainer binding or demonstrate
physical shooting. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED.

The five independent static-review HOLD/repair rounds concerned test-only
architecture guards. No production defect was found, and the final independent
static review passed. Runtime Flywheel and Feeder adapters remain Noop; physical
shooting values and behavior remain unverified. Documentation reconciliation
is current. Independent Closure Review is pending; freeze and publication are
not claimed.

## Historical M00_L14 Freeze Reconciliation — 2026-09-26

The initial Independent Closure Review returned
`HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED`
for documentation and evidence wording only. The bounded repair passed as
`PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION`. Independent Closure Rereview
passed as `PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW` with verdict
`INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION`.
Freeze Reconciliation is complete.

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14
is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED. Active Lesson Count is 0;
Current Active M00 Lesson is NONE. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED. Evidence remains THEORY VERIFIED / SIMULATION VERIFIED
/ REAL HARDWARE DEFERRED. Accepted focused tests, clean regression, and bounded
Simulation remain PASS; no new execution is claimed.

RobotContainer still has no ShootCommand binding. Simulation covered startup,
Disabled -> Teleoperated enabled -> Disabled, safe semantic mechanism state,
and clean shutdown; it did not schedule ShootCommand or prove physical shooting.
Direct unit-test invocation of `end(true)` covers interrupted-end cleanup, but
actual scheduler-driven cancellation was not tested. Independent Freeze Review
is PENDING. Publication is NOT PUBLISHED / PENDING, and Final Publication
Verification is PENDING.

## Historical M00_L14 Metadata Publication Preparation — 2026-09-26

The Independent Freeze Review passed as
`PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW` /
`INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION`. The User completed
`PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT` at primary SHA
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`. This is the Primary Frozen
Snapshot, Commit 1 of the two-commit Historical Snapshot model.

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14
remains COMPLETE / FROZEN / READ-ONLY and NOT ACTIVE. Active Lesson Count is
0; Current Active M00 Lesson is NONE. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED. Evidence remains THEORY VERIFIED / SIMULATION VERIFIED
/ REAL HARDWARE DEFERRED.

This earlier record prepared metadata for Commit 2. It is superseded by the
current reconciliation below.

The accepted tests and bounded Simulation evidence are unchanged.
RobotContainer has no ShootCommand binding; Simulation did not schedule the
command or establish physical shooting. Direct unit invocation of `end(true)`
proves cleanup method semantics, not scheduler-driven cancellation.

## Historical M00_L14 Metadata Publication Reconciliation Repair — pre-Commit-2 state

This is the accepted preparation snapshot before the User created Metadata
Publication Commit 2. Its pending-commit statements are historical and are
superseded by the current post-amend publication record below.

The accepted HOLD `HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA`
identified the absence of a real publication-metadata delta after Commit 1.
This repair supplied that delta and prepared the M00_L13 Commit-2 publication
semantics: Commit 2 records M00_L14 as `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED`. At this earlier preparation point, the pre-commit worktree did
not claim that Commit 2 or its push had occurred.

The Primary Frozen Snapshot was Commit 1 at
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`. The metadata commit's own SHA and
remote identity were external and were not self-embedded. Final Publication
Verification was PENDING / EXTERNAL, with no third verification-only
commit. M00_L14 was not active; Active Lesson Count was 0 and Current Active
M00 Lesson was NONE. M00_L15/L16 remain FUTURE / INACTIVE / NOT CREATED.
Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

RobotContainer had no ShootCommand binding, so bounded Simulation did not
schedule the command or prove physical shooting. Direct unit invocation of
`end(true)` established cleanup method semantics, not scheduler-driven cancellation.
No production or test content changed in this metadata repair.

## Historical M00_L14 Post-Amend Publication State — 2026-09-26 (before M00_L15 activation)

Accepted User evidence establishes that Primary Frozen Snapshot Commit 1
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3` is followed by a completed User-owned
Metadata Publication Commit 2. M00_L14 is COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / NOT ACTIVE. Active Lesson Count is 0 and Current Active M00 Lesson
is NONE. M00_L15 and M00_L16 remain FUTURE / INACTIVE / NOT CREATED. The
Metadata Publication Commit amendment is COMPLETED by accepted User evidence;
its canonical identity remains external and is not embedded in these records.

The exact accepted chronology includes
`PASS_M00_L14_DOCUMENTATION_RECONCILIATION`,
`PASS_M00_L14_FREEZE_RECONCILIATION`,
`PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION`,
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT`,
`PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION`, and
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND`. The amendment preserves
exactly two publication commits; no canonical Metadata Commit hash is
self-embedded.

Remote push remains PENDING / USER-OWNED because no push evidence is supplied.
The prior `HOLD_M00_L14_FINAL_PUBLICATION_VERIFICATION_METADATA_COMMIT_STATE_RECONCILIATION_REQUIRED`
was resolved by the accepted User amendment. The subsequent
`HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED`
identified stale current-state chronology and is addressed by this
documentation reconciliation. Final Publication Verification remains PENDING / EXTERNAL.
No final-verification PASS or third verification-only commit was claimed in
this historical state. Evidence remains THEORY VERIFIED / SIMULATION VERIFIED
/ REAL HARDWARE DEFERRED. The bounded Simulation limitation and the distinction
between direct `end(true)` unit invocation and scheduler-driven cancellation
remain as recorded in the lesson documents. This was a documentation-only
publication metadata update.

## Historical M00_L15 Controlled Activation — 2026-09-26

M00_L14 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED and NOT ACTIVE.
Its Primary Frozen Snapshot is `fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`;
its canonical Metadata Publication Commit is
`1a85c0827ee91ba7f70a595d94c49ad16a6df9cb`. Accepted gates are
`PASS_M00_L14_FINAL_PUBLICATION_VERIFICATION` and
`PASS_M00_L14_PUBLICATION_PUSH`.

M00_L15 preparation passed the untouched-copy baseline, Architecture /
Inheritance Audit, and Final Design Lock. The candidate inherited all 341
authored files unchanged from M00_L14. The initial design-lock HOLD requiring
scheduler coverage was resolved by adding test-plan cases for real scheduler
startup, cancellation, Intake requirement contention, and Feeder requirement
contention, alongside 18 direct-contract cases.

M00_L15 — Intake-to-Feeder Coordination is now the sole
IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK lesson. Active M00
lesson count is 1. Its single concept is scheduler-managed coordination of
existing Intake and Feeder semantic behavior under one command lifecycle. The
locked command is `frc.robot.commands.IntakeToFeederCommand`, with exactly
`IntakeSubsystem` and `FeederSubsystem` requirements. No new RobotContainer
binding is authorized. M00_L16 remains FUTURE / INACTIVE / NOT CREATED.

Controlled Activation changed documentation only. M00_L15 production and
tests were NOT STARTED and NOT AUTHORIZED at that time. Independent Activation
Review was PENDING; freeze and publication were NOT AUTHORIZED. M00_L15
Simulation was NOT TESTED and real hardware remained DEFERRED. No physical
movement, game-piece presence, transfer completion, or hardware timing was
claimed. No L17 was authorized.

## Historical M00_L15 Implementation and Verification Reconciliation — 2026-09-27 (pre-freeze state)

M00_L15 remains IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK;
M00_L14 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Active
M00 Lesson Count is 1. Independent Closure Review is pending. M00_L15 is not
COMPLETE, FROZEN, or PUBLISHED. M00_L16 remains FUTURE / INACTIVE / NOT
CREATED.

Accepted gates include the untouched-copy baseline, Architecture / Inheritance
Audit, Final Design Lock, Controlled Activation, Independent Activation
Rereview, Implementation Authorization and Handoff, and Final Independent
Static Review. The single concept remains scheduler-managed coordination of
existing Intake and Feeder semantic behavior under one command lifecycle.

Relative to frozen M00_L14, 112 shared production Java files remain identical;
the sole L15 production addition is
`src/main/java/frc/robot/commands/IntakeToFeederCommand.java`. RobotContainer,
RunIntakeCommand, RunFeederCommand, ShootCommand, IntakeSubsystem, and
FeederSubsystem are unchanged. The command requires only IntakeSubsystem and
FeederSubsystem. Right Bumper remains bound to RunIntakeCommand whileTrue and
Left Bumper to RunFeederCommand whileTrue. No IntakeToFeederCommand binding or
default command was added.

The focused suite passed 22 / 22 tests with no failures, errors, or skips. The
initial clean regression had 830 tests and two failures, diagnosed as
`EXPECTED_INHERITED_TEST_CONTRACT_EVOLUTION` rather than production defects.
Inherited architecture reconciliation, scan robustness repair, the JDK Java
parser repair, and independent parser review passed within current M00_L15
test copies; frozen predecessor files remained unchanged. The final User clean
regression passed with BUILD SUCCESSFUL, 830 tests, 830 PASS, zero failures,
errors, or skips, and `BUILD_EXIT_CODE=0`.

Bounded Simulation / Driver Station verification passed for startup and DS
attachment. Disabled had Robot Enabled=No and Intake, Feeder, and Flywheel
RequestedState=STOPPED. Teleoperated had Robot Enabled=Yes, DS Attached=Yes,
and Intake and Feeder STOPPED. On return to Disabled, Robot Enabled=No and
Intake and Feeder remained STOPPED, with no unexpected mechanism state. No
unintended L15 activation or fatal runtime/scheduler error occurred. Termination
was BUILD SUCCESSFUL with `SIMULATION_EXIT_CODE=0`. The unavailable Joystick
Button 6 warning on port 0 was EXPECTED / NON-BLOCKING because the controller
was unassigned or unplugged.
Simulation did not schedule IntakeToFeederCommand because RobotContainer has no
binding; scheduler behavior is established by the focused tests.

Evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
No physical transfer, game-piece presence, mechanism timing, sensor behavior,
or real-hardware operation is claimed. Documentation reconciliation was
complete at this pre-freeze point; Independent Closure Review was then the next
gate. This section records the state at that time.

## Historical M00_L15 Freeze Reconciliation — 2026-09-27

The accepted independent closure rereview gate is
`PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW`, with verdict
`M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION`;
the rereview reported no findings. Freeze reconciliation is complete, and
M00_L15 is `COMPLETE / FROZEN / READ-ONLY`.

The evidence remains exactly `THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED`. Focused tests remain 22 / 22 PASS. The final clean
regression remains 830 / 830 PASS with zero failures, errors, or skips and
`BUILD_EXIT_CODE=0`. Bounded Simulation / Driver Station remains PASS with
`SIMULATION_EXIT_CODE=0`; it did not schedule `IntakeToFeederCommand`, whose
scheduler behavior is verified by the focused tests. The only production
addition remains `IntakeToFeederCommand.java`; 112 shared production Java
files remain identical to M00_L14. No source or test file changed during
freeze reconciliation.

At this freeze-reconciliation point, M00_L14 remained
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; M00_L15 Independent
Freeze Review, publication, and final publication verification were pending.
That state is superseded by the current metadata reconciliation below. Active
Lesson Count was `0`, Current Active M00 Lesson was `NONE`, and M00_L16 was
`FUTURE / INACTIVE / NOT CREATED`. The L01–L16 roadmap and lesson order are
unchanged; no M00_L17 scope is introduced.

## Historical M00_L15 Publication Metadata Reconciliation — 2026-09-27

The accepted independent freeze rereview gate is
`PASS_M00_L15_INDEPENDENT_FREEZE_REREVIEW`, with verdict
`M00_L15_INDEPENDENT_FREEZE_REREVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
The primary frozen snapshot is committed under
`PASS_M00_L15_PRIMARY_SNAPSHOT_COMMIT` at SHA
`15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`. Publication metadata
reconciliation is COMPLETE and prepared for the User-owned Metadata Publication
Commit 2. Metadata Publication Commit: PENDING; publication push: PENDING;
external Final Publication Verification: PENDING. M00_L15 remains
`COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`; evidence remains
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. Active Lesson
Count is `0`, Current Active M00 Lesson is `NONE`, and M00_L16 remains
`FUTURE / INACTIVE / NOT CREATED`.
