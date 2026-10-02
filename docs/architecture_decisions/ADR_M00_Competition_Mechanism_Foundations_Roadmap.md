<!-- ACM-10 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-10 formal domain closure — 2026-10-02

The Architect reviewed Sol's independent initial audit and explicitly authorized **ACM-10 FORMAL DOMAIN CLOSURE**. Accepted audit token: PASS_ACM_10_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW. **ACM-10 is FORMALLY CLOSED / FORMALLY RECORDED: 50 / 50 dimensions CLOSED; 0 BLOCKED. ACM-10-F01: NOT ESTABLISHED. NO ADDITIONAL ACM-10 FINDING ESTABLISHED.** The dimension-by-dimension record is included in the current ACM-10 closure block in [AGENTS.md](../../AGENTS.md).

The static governance validator passed: 12 authoritative English source PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was NOT PERFORMED.

RobotContainer is the production composition root and Real/Simulation selection authority; WPILib RobotBase is the runtime environment signal. RobotBase.isReal() governs RobotContainer's Vision and Swerve construction: Real selects four SwerveModuleIOCTRE adapters, GyroIOPigeon2, and VisionIOLimelight; otherwise it selects four SwerveModuleIOSim adapters with shared SwerveSimulationState and GyroIOSim, plus VisionIOSim and its simulation-only VisionIOSimHarness/fixture chooser. Both modes inject the same project IO contracts into the same subsystems. The fixture changes simulated Vision input only; it neither swaps adapters nor writes estimator state. No hidden production mode-selection branch or hardware adapter reachable from the selected Simulation graph was established.

IntakeIONoop, FeederIONoop, FlywheelIONoop, and ElevatorIONoop are used in both modes. These are unavailable, non-actuating implementations; no current mechanism hardware or mechanism physics simulation is claimed. Real and Simulation paths retain the same command, observation, telemetry, localization, driver-input, autonomous, and named-event architecture. SwerveSubsystem remains the localization owner. No numerical or physical Simulation fidelity is claimed for battery, current, thermal, traction, or controller dynamics.

Static test review identified RobotSimulationHarnessCompositionTest, VisionIOSimHarnessTest, Swerve/Gyro simulation tests, and mechanism Noop tests. No tests were executed during the ACM-10 audit. There is no paired Real-mode RobotContainer test; its absence was not established as a defect.

RobotContainer selects concrete adapters once at construction, creates one subsystem graph, and keeps adapter identity fixed for that graph's lifetime. Robot owns lifecycle and does not select IO. Subsystems consume the shared project IO contracts, while commands use subsystem semantic APIs; both are mode-agnostic. Hardware adapters are confined to the Real-selected graph, and simulation adapters and the harness to the Simulation-selected graph.

RobotContainer.runSimulationHarness() applies the selected Vision fixture only when RobotBase.isSimulation(); that gate does not choose adapters. VisionIOSim flows through VisionSubsystem qualification and the existing fusion handoff to SwerveSubsystem's estimator, while simulated modules and gyro feed that same localization owner. Telemetry reads shared project observations, and driver input, autonomous, and named events retain their existing shared controller, scheduler, and command paths. Intake and Feeder Noop IOs do not model physical mechanism movement.

ACM-01 through ACM-09 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED. The latest published checkpoint is c4824c255eae5835ebf6c51505a95899f95d0b35, parent a932931ae674817b4fb994cba8cfe2ef2591db98, annotated tag audit-acm-09-closed. The User reports origin/main and the remote peeled tag target verified at that commit; the local HEAD and origin/main refs also resolve there. **ACM-11 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED**; ACM-12 remains NOT STARTED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. This closure does not authorize an ACM-10 checkpoint, tag, push, ACM-11 activation, Constants.java cleanup/refactor, re-freeze, publication, or Phase 4.

This formal closure recording changes only the eight authorized lifecycle documents. No Java source, tests, or other protected content was changed. No Git write or project execution occurred; the index remains empty. Existing protected/unrelated worktree state was left untouched. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED. The next gate is the User-owned ACM-10 Git checkpoint.
<!-- ACM-10 DOMAIN CLOSURE CURRENT END -->
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

# ADR: M00 Competition Mechanism Foundations Roadmap
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

The Architect explicitly authorized formal closure of ACM-05 — Observation / IOInputs Data Flow — following Sol's independent read-only audit token `PASS_ACM_05_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. The Architect owns the decision; Sol supplied evidence. **ACM-05: FORMALLY CLOSED / FORMALLY RECORDED.** All thirty-one audit dimensions are CLOSED. **ACM-05-F01: NOT ESTABLISHED;** no current defect or repair exists.

Earlier ACM-01–ACM-04 blocks below preserve their closure-stage evidence; their former next-domain cursor wording predates this ACM-05 closure and does not control the current cursor.

Governance preflight PASS: 12 source PDFs, 12 matching hashes, zero deterministic findings; semantic fidelity certification was not performed. Current M00_L16 preserves IO / hardware / simulation → subsystem-owned mutable IOInputs → subsystem interpretation → immutable project read models → consumers. The seven IO families are SwerveModuleIO, GyroIO, VisionIO, IntakeIO, FeederIO, FlywheelIO, and ElevatorIO. Their subsystem-owned Inputs refresh before periodic observation creation. No mutable transport escape, alias, shared owner, or alternate hardware-derived public read path was established.

The fourteen top-level read models are SwerveObservation, VisionObservation, QualifiedVisionMeasurement, VisionTiming, VisionMeasurementQuality, VisionFusionObservation, IntakeObservation, FeederObservation, FlywheelObservation, ElevatorObservation, DriverInputObservation, DriveThreeMeterValidationObservation, AutonomousEventObservation, and AutonomousPreparationObservation. Vision defensively copies the target list and values. Telemetry uses subsystem observations; reviewed commands use observations or semantic APIs. Noop and Swerve/Gyro/Vision simulation implementations preserve the same data boundary. FeederIO → FeederIOInputs → FeederSubsystem → FeederObservation is CORRECT; mutable transport escape NONE ESTABLISHED. Full inventory and evidence are recorded in [AGENTS.md](../../AGENTS.md).

ACM-01 through ACM-04 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED; no regression was established. At the ACM-05 checkpoint, ACM-06 was NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; that cursor was superseded by the formal ACM-06 closure recorded above. ACM-07 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. The User checkpointed and pushed ACM-05 at `6c125c2490c50b9f2c0151379307ac72d10c0b22` with annotated tag `audit-acm-05-closed`; its remote target was verified by the User. Documentation-only recording; no Git write or project execution occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

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

F01 and F01-DOC-01 remain CLOSED; F02 and F03 remain CLOSED / FORMALLY RECORDED; F04 remains FORMALLY CLOSED / FORMALLY RECORDED. The 14-member F04 production family and its dedicated tests remain in commands.auto; the exact family is preserved in [the F04 repair record, Sections 1 and 4](ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md). LearningTrajectoryFactory and its dedicated test remain in commands.auto.

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
[The F04 ADR, Section 2](ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md#2-bounded-implementation-and-static-self-audit--2026-09-30) records this static self-audit.
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
family. [The F04 repair ADR](ADR_M00_L16_ACM_01_F04_Autonomous_Command_Family_Package_Repair.md) records each type and historical
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
[The F03 ADR, Section 4](ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md#4-formal-acm-01-f03-repair-closure--2026-09-30) governs this decision. The
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

The ACM-01 architecture cursor remains HOLD after F03 closure.
The F03 -> F04 order is preserved. F04 has not been designed,
implemented or verified by this action.

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
[The F03 repair ADR](ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md) now reconciles the accepted evidence.

F03 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW;
F03 is NOT CLOSED. F04 remains ACCEPTED / PARKED in order F03 -> F04.
F01, F01-DOC-01 and F02 remain CLOSED; P3-H01 CLOSED / PRESERVED;
CFG-H01 CLOSED / REMOVED; CF-U and PF-U CAUSE NOT ESTABLISHED.
M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED;
ACM-01 and Phase 3 remain HOLD; ACM-02 NOT STARTED; Phase 4
NOT STARTED / FORBIDDEN. No M00_L17.
Exact next gate: INDEPENDENT FINAL ACM-01-F03 ARCHITECTURE / CLOSURE REVIEW.

This records the ACM-01 HOLD cursor after F03 reconciliation.
F04 has no design, implementation or verification from this F03 work;
ACM-02 and Phase 4 remain outside the current audit gate.

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
Document A Sections 2 and 8. [The dedicated F03 repair ADR](ADR_M00_L16_ACM_01_F03_Learning_Trajectory_Factory_Package_Repair.md)
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
The governing closure decision is [the F02 ADR, Section 3](ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#3-formal-acm-01-f02-repair-closure--2026-09-30).

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

The prospective ACM sequence, domain scopes and locked separate repair
order F02 -> F03 -> F04 remain unchanged. F03 activation/design authorization
is pending; F03/F04 work has not begun.

<!-- ACM-01-F02 CURRENT END -->

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT BEGIN -->
## Historical ACM-01-F02 documentation/evidence reconciliation — 2026-09-30

The Architect-authorized F02 documentation/evidence reconciliation is
complete. The accepted package-only implementation is five changed existing
Java identities plus one focused placement test; all required User automated
gates and independent implementation review PASS. The initial Java 8
configuration failure and corrected Java 17 sequence are preserved in
[the F02 ADR, Section 2](ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md#2-accepted-implementation-and-evidence-reconciliation--2026-09-30).
The registration-stage record below remains historical stage evidence.

F02 is IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED /
DOCUMENTATION RECONCILED / PENDING INDEPENDENT FINAL CLOSURE REVIEW.
F02 is NOT CLOSED. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. F03 and F04 remain ACCEPTED / PARKED. F01 and DOC-01 remain
CLOSED; P3-H01 CLOSED / PRESERVED; CFG-H01 CLOSED / REMOVED.
Exact next gate: INDEPENDENT FINAL ACM-01-F02 ARCHITECTURE / CLOSURE REVIEW.

The prospective ACM sequence, domain scopes and locked repair order
F02 -> F03 -> F04 are unchanged. This records the ACM-01 HOLD cursor only.

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT END -->

<!-- ACM-01-F02 REGISTRATION BEGIN -->
## ACM-01-F02 exceptional repair registration — 2026-09-30

This registration-stage record preserves the M00 roadmap while the Architect
authorizes the M00_L16-only, six-Java-identity F02 package correction under
Document C OC-02 Section 1. The drive validation observation moves to
`frc.robot.observation.swerve`; three production imports, one existing test
import, and one new focused placement test are the complete boundary.
The inherited root placement began in S00_L23; historical copies remain
unchanged. The separate decision is
[the F02 repair ADR](ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md).

At registration, F02 verification and closure are PENDING. F01 and DOC-01
remain CLOSED; F03/F04 are ACCEPTED / PARKED. M00_L16 remains IN_PROGRESS /
NOT RE-FROZEN / NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4
NOT STARTED / FORBIDDEN. No M00_L17. Next: bounded F02 implementation and
User automated verification, then independent review.

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
- Governing chronology, evidence and closure: [dedicated ACM-01-F01 repair ADR](ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

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
- Governing chronology/evidence: [dedicated ACM-01-F01 repair ADR](ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).

The Phase-3 cursor remains ACM-01 HOLD pending repair closure and ACM-01 rereview.
The locked M00_L01–L16 roadmap, scope and order are unchanged.

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
- Governing record: [ACM-01-F01 package repair](ADR_M00_L16_ACM_01_F01_Swerve_Observation_Package_Repair.md).
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
- Governing repair record: [P3-H01 configuration-authority repair](ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md); original registration and reconciliation
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
- Governing amendment: [existing exceptional-repair ADR](ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md); preserved registration in Section 16 and
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

### CFG-H01 roadmap exception

For this repair stage only, the protected Limelight adapter boundary is
supplemented by the exact endpoint-ownership change above. Constants authority
is restored using the existing adapter and existing default constructor.
This is an exceptional correction in final M00_L16, not a new lesson/concept,
vendor selection, hardware reconfiguration, or Vision redesign. The sixteen
lesson identities, order, inheritance, and original event-integration objective
remain unchanged. RobotContainer is excluded from CFG-H01 edits.

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
- Governing repair ADR: [P3-H01 configuration-authority repair](ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md).

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

### Narrow post-publication roadmap supplement

This approved supplement registers the exceptional inherited configuration-authority
repair in final M00_L16 only. The locked sixteen-lesson order and L16's original
mechanism-event concept are unchanged. For this repair workflow only, the original
Constants/Vision modification exclusion is supplemented by the exact two-file
future boundary in the dedicated ADR, subject to separate implementation approval.
No Vision algorithm, owner, interface, hardware value, or protected system is
redesigned. The ordinary frozen-lesson and one-concept rules remain in force.

## Historical roadmap authorization and lifecycle records — through 2026-09-27

- Status: APPROVED
- Date: 2026-09-13
- Roadmap State: APPROVED / ROADMAP AUTHORIZED
- Current Active M00 Lesson: NONE
- Active Lesson Count: 0
- M00_L12: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L13 Implementation Authorization: PASS_M00_L13_IMPLEMENTATION_AUTHORIZATION
- M00_L13 Implementation: COMPLETE
- M00_L13 Implementation Handoff: PASS_M00_L13_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW
- M00_L13 Final Independent Static Re-review: PASS_M00_L13_FINAL_INDEPENDENT_STATIC_REREVIEW
- M00_L13 Focused Tests / Clean Regression: PASS / USER EVIDENCE ACCEPTED
- M00_L13 Bounded Simulation: PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION
- M00_L13 Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- M00_L13 Closure Re-review: PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW / CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE
- M00_L13 Freeze Reconciliation: COMPLETE
- M00_L13 Independent Freeze Review: PASS_M00_L13_INDEPENDENT_FREEZE_REVIEW / FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION
- M00_L13 Primary Frozen Snapshot: COMPLETED / PASS_M00_L13_PRIMARY_FROZEN_SNAPSHOT_PUBLICATION_COMMIT
- M00_L13 Primary SHA: 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704
- M00_L13 Metadata Publication: ESTABLISHED BY COMMIT 2 OF THE TWO-COMMIT HISTORICAL SNAPSHOT MODEL; metadata and matching remote-main identities are external publication evidence and are not self-embedded
- M00_L13 Publication Push / Remote Identity: ESTABLISHED BY ACCEPTED EXTERNAL PUBLICATION EVIDENCE
- M00_L13 Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L13 Final Publication Verification: PASS / ACCEPTED EXTERNAL EVIDENCE
- HISTORICAL M00_L13 closure state: Active Lesson Count: 0; subsequent current lesson: M00_L14 — Shoot Coordination
- M00_L14 Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / NOT ACTIVE
- M00_L14 Independent Closure Rereview: PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW
- M00_L14 Documentation Reconciliation: PASS_M00_L14_DOCUMENTATION_RECONCILIATION
- M00_L14 Freeze Reconciliation: PASS_M00_L14_FREEZE_RECONCILIATION
- M00_L14 Independent Freeze Review: PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW / INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION
- M00_L14 Primary Frozen Snapshot: PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT / SHA fa34556a3f1b7ef52b2c678a39c1083392d7c8d3
- M00_L14 Metadata Publication Reconciliation: PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION
- M00_L14 Metadata Publication Commit: COMPLETED / PASS_M00_L14_METADATA_PUBLICATION_COMMIT; its identity is external and not self-embedded
- M00_L14 Metadata Publication Commit Amendment: COMPLETED / PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND; canonical identity remains external
- M00_L14 Metadata SHA: 1a85c0827ee91ba7f70a595d94c49ad16a6df9cb
- M00_L14 Remote Push: PASS_M00_L14_PUBLICATION_PUSH
- M00_L14 Final Publication Verification: PASS_M00_L14_FINAL_PUBLICATION_VERIFICATION
- M00_L15 Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L15 Independent Activation Review: PASS_M00_L15_INDEPENDENT_ACTIVATION_REREVIEW
- M00_L15 Implementation / Final Static Review: COMPLETE / PASS_M00_L15_FINAL_INDEPENDENT_STATIC_REVIEW
- M00_L15 Focused Tests: PASS_M00_L15_USER_FOCUSED_TESTS / 22 OF 22
- M00_L15 Clean Regression: PASS_M00_L15_USER_CLEAN_REGRESSION / 830 OF 830 / BUILD_EXIT_CODE=0
- M00_L15 Bounded Simulation / Driver Station: PASS_M00_L15_BOUNDED_SIMULATION_DS_VERIFICATION / SIMULATION_EXIT_CODE=0
- M00_L15 Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- M00_L15 Independent Closure Rereview: PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW / M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION
- M00_L15 Freeze Reconciliation: COMPLETE / PASS_M00_L15_FREEZE_RECONCILIATION
- M00_L15 Independent Freeze Rereview: PASS_M00_L15_INDEPENDENT_FREEZE_REREVIEW / M00_L15_INDEPENDENT_FREEZE_REREVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW
- M00_L15 Primary Frozen Snapshot: PASS_M00_L15_PRIMARY_SNAPSHOT_COMMIT / SHA 15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa
- M00_L15 Publication Metadata Reconciliation: COMPLETE / CONSUMED BY USER METADATA COMMIT 2
- M00_L15 Metadata Publication Commit: COMPLETE / USER-OWNED / SHA 0d3685ce67a0b985459392621e003611eaa6dc35
- M00_L15 Publication Push: COMPLETE / ACCEPTED EXTERNAL EVIDENCE
- M00_L15 Publication: COMPLETE / PUBLISHED / VERIFIED
- M00_L15 Final Publication Verification: PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION
- M00_L16: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED; implementation, focused tests, clean regression, bounded Simulation, documentation reconciliation, independent closure review, and Freeze Reconciliation COMPLETE; Independent Freeze Review PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW
- M00_L16 Primary Frozen Snapshot Commit 1: CREATED / USER-OWNED / SHA ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11
- M00_L16 Publication Metadata Reconciliation: COMPLETE / PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION / AWAITING USER METADATA COMMIT 2
- M00_L16 Metadata Commit 2 / Push / Final Publication Verification: PENDING / USER-OWNED OR EXTERNAL AS APPLICABLE
- Preparation State: COMPLETE / ACCEPTED
- Preparation Authorization: PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED
- Runtime / Lesson Lifecycle: M00_L13 COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED; M00_L14 COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED; M00_L15 COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED; M00_L16 COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED; ACTIVE LESSON COUNT 0; CURRENT ACTIVE M00 LESSON NONE
- M00_L07 Controlled Activation: PASS_M00_L07_CONTROLLED_ACTIVATION
- M00_L07 Freeze State: COMPLETE / FROZEN / READ-ONLY
- M00_L07 Design Lock: PASS_M00_L07_FINAL_DESIGN_LOCK
- M00_L07 Implementation Authorization: AUTHORIZED / CONSUMED
- M00_L07 Implementation: COMPLETE / PASS_M00_L07_IMPLEMENTATION_REPORT_ACCEPTED
- M00_L07 Final Static Rereview: PASS_M00_L07_FINAL_INDEPENDENT_STATIC_REREVIEW
- M00_L07 Focused Tests: PASS / SIX CLASSES / BUILD SUCCESSFUL
- M00_L07 Full Regression: PASS / BUILD SUCCESSFUL IN 36s / 7 OF 7 TASKS EXECUTED
- M00_L07 Simulation: PASS_M00_L07_BOUNDED_SIMULATION
- M00_L07 Evidence: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
- M00_L07 Documentation: COMPLETE / READY FOR INDEPENDENT CLOSURE REVIEW
- M00_L07 Independent Closure Review: PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS
- M00_L07 Freeze: COMPLETE / FROZEN / READ-ONLY
- M00_L07 Primary Publication: COMPLETE / PUSHED / REMOTE-ALIGNED
- M00_L07 Primary Publication Commit: 50e5f440bb0c9d96bdcd57eed533651d8d59ca93
- M00_L07 Primary Commit Subject: Complete M00_L07 Flywheel foundation
- M00_L07 Publication Metadata Reconciliation: COMPLETE / PREPARED FOR USER COMMIT
- M00_L07 Metadata Commit / Push: PENDING USER ACTION
- M00_L07 Final Publication Verification: PENDING
- M00_L07 Final Publication State: NOT YET PUBLISHED / VERIFIED
- M00_L05 Implementation: COMPLETE / VERIFIED
- M00_L05 Focused Tests: PASS / VERIFIED
- M00_L05 Full Regression: PASS / 682 TESTS
- M00_L05 Simulation: SIMULATION VERIFIED / BOUNDED
- M00_L05 Simulated Driver Station: VERIFIED / BOUNDED
- M00_L05 Independent Implementation Review: PASS
- M00_L05 Documentation: IMPLEMENTED / READY FOR INDEPENDENT DOCUMENTATION REVIEW
- HISTORICAL pre-M00_L06 activation status: Active Lesson Count: 0
- M00_L03 Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L03 Primary Git Publication: PASS
- M00_L03 Primary Publication Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
- M00_L03 Metadata Publication Commit: b2464f66da42a6281acb7bfc709f2a3b83296505
- M00_L03 Final Publication Verification: PASS
- M00_L01 Publication: PUBLISHED / VERIFIED @ 83907ab
- M00_L01 Metadata Publication: PUBLISHED / VERIFIED @ f523118
- M00_L02 Primary Publication Commit: 65a92a4a5806fd5134e0114e851c4e4cc093c58e
- M00_L02 Primary Push: PASS
- M00_L02 Publication Metadata: PUBLISHED / VERIFIED @ 84010ff5022a33a946888fafedbbca0d67439e0c
- M00_L02 Final Publication Completion: PASS_M00_L02_FINAL_PUBLICATION_COMPLETE
- M00_L03 Preparation: PASS
- M00_L03 Baseline Build: PASS / EXIT CODE 0
- M00_L03 Architecture / Inheritance Audit: PASS
- M00_L03 Implementation: COMPLETE FOR AUTHORIZED M00_L03 SCOPE
- M00_L04 Preparation: PASS
- M00_L04 Baseline Build: PASS / BUILD SUCCESSFUL IN 48s / EXIT CODE 0
- M00_L04 Architecture / Inheritance Audit: PASS
- M00_L04 Lifecycle: COMPLETE / FROZEN / READ-ONLY
- M00_L04 One New Concept: SCHEDULER-MANAGED MANUAL OWNERSHIP OF EXISTING INTAKE
- M00_L04 Locked Scope: RunIntakeCommand + RIGHT BUMPER whileTrue + FOCUSED LIFECYCLE/OWNERSHIP TESTS
- M00_L04 Implementation: COMPLETE / VERIFIED
- M00_L04 Focused Tests: VERIFIED / 14 OF 14 PASS
- M00_L04 Full Regression: VERIFIED
- M00_L04 Simulation: SIMULATION VERIFIED / BOUNDED
- M00_L04 Driver Station: VERIFIED / BOUNDED
- M00_L04 Glass: NOT APPLICABLE AS A DISTINCT COMPLETION GATE
- M00_L04 Real Hardware: REAL HARDWARE DEFERRED
- M00_L04 Independent Implementation Review: PASS
- M00_L04 Documentation: COMPLETE / VERIFIED
- M00_L04 Final Closure Build: PASS / BUILD SUCCESSFUL IN 23s / 7 OF 7 TASKS EXECUTED / EXIT CODE 0
- M00_L04 Final Closure Review: PASS
- M00_L04 Freeze: COMPLETE / FROZEN / READ-ONLY
- M00_L04 Primary Publication: COMPLETE
- M00_L04 Primary Publication Commit: 5c86be3
- M00_L04 Primary Remote Alignment: PASS
- M00_L04 Publication Metadata Reconciliation: COMPLETE IN WORKING TREE
- M00_L04 Metadata Publication: COMPLETE @ 24738e6
- M00_L04 Metadata Remote Alignment: PASS
- M00_L04 Final Publication Verification: PASS
- M00_L04 Final State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L05 Preparation: PASS / BUILD SUCCESSFUL IN 41s / JAVA 17.0.16
- M00_L05 Inheritance: PASS / 285 OF 285 GOVERNED FILES BYTE-IDENTICAL
- M00_L05 Architecture Audit: PASS_M00_L05_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK
- M00_L05 Design Lock: PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION
- M00_L05 Lifecycle: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L05 Runtime Strategy: FeederIONoop ONLY
- M00_L05 Real Hardware: REAL HARDWARE DEFERRED
- M00_L05 Primary Publication: 5709f1d74b3318303bcc56779315b243dd81770b
- M00_L05 Metadata Publication: 1d6fadeec57fbfd3be245746b21d06e58b79518f
- M00_L05 Final Publication: PASS_M00_L05_FINAL_PUBLICATION_COMPLETE
- HISTORICAL pre-M00_L06 activation status: Current Active M00 Lesson: NONE
- M00_L06: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
- M00_L06 Preparation: PASS / BUILD SUCCESSFUL IN 40s / EXIT CODE 0
- M00_L06 Inheritance: PASS / 299 OF 299 GOVERNED FILES BYTE-IDENTICAL
- M00_L06 Architecture Audit: PASS_M00_L06_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK
- M00_L06 One New Concept: SCHEDULER-MANAGED COMMAND OWNERSHIP OF EXISTING FEEDER SEMANTIC API
- M00_L06 Locked Scope: RunFeederCommand + LEFT BUMPER whileTrue + FOCUSED LIFECYCLE/OWNERSHIP TESTS
- M00_L06 Implementation: COMPLETE / EXACT TWO-FILE PRODUCTION BOUNDARY
- M00_L06 Focused Tests: PASS / FOUR AUTHORIZED CLASSES / BUILD SUCCESSFUL IN 7s / EXIT CODE 0
- M00_L06 Full Regression: PASS / CLEAN BUILD / BUILD SUCCESSFUL IN 37s / EXIT CODE 0
- M00_L06 Simulation: SIMULATION VERIFIED / BOUNDED / CHECKPOINTS A-G
- M00_L06 Documentation: COMPLETE / VERIFIED
- M00_L06 Independent Closure Review: PASS / PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED
- M00_L06 Freeze: COMPLETE / AUTHORIZED_FOR_FREEZE
- M00_L06 Primary Publication: COMPLETE / PUSHED / REMOTE-ALIGNED
- M00_L06 Primary Publication Commit: f102a5e662877f8cb49eb63f2cfd888ac356bea4
- M00_L06 Primary Commit Subject: Complete M00_L06 Feeder command ownership
- M00_L06 Publication Metadata Reconciliation: COMPLETE / HISTORICAL
- M00_L06 Final Publication: PUBLISHED / VERIFIED
- M00_L07: COMPLETE / FROZEN / READ-ONLY
- M00_L07 Preparation: PASS
- M00_L07 Baseline Build: PASS / BUILD SUCCESSFUL IN 38s / 6 OF 6 ACTIONABLE TASKS EXECUTED
- M00_L07 Inheritance: PASS / 306 OF 306 GOVERNED FILES BYTE-IDENTICAL
- M00_L07 Architecture Audit: PASS_M00_L07_ARCHITECTURE_INHERITANCE_AUDIT
- M00_L07 Design Lock: PASS_M00_L07_FINAL_DESIGN_LOCK
- M00_L07 Governance Adjudication: PASS_M00_L07_GOVERNANCE_ADJUDICATION
- M00_L07 Independent Activation Review: PASS_M00_L07_INDEPENDENT_ACTIVATION_REVIEW_AFTER_ADJUDICATION
- M00_L07 Implementation: COMPLETE / ACCEPTED
- M00_L07 Focused Tests: PASS
- M00_L07 Clean Regression: PASS
- M00_L07 Bounded Simulation: PASS
- M00_L07 Independent Closure Review: PASS / READY_FOR_FREEZE_AUTHORIZATION
- M00_L07 Freeze Transition: COMPLETE
- M00_L07 Primary Publication: COMPLETE / PUSHED / REMOTE-ALIGNED
- M00_L07 Primary Publication Commit: 50e5f440bb0c9d96bdcd57eed533651d8d59ca93
- M00_L07 Primary Commit Subject: Complete M00_L07 Flywheel foundation
- M00_L07 Publication Metadata Reconciliation: COMPLETE / PREPARED FOR USER COMMIT
- M00_L07 Metadata Commit / Push: PENDING USER ACTION
- M00_L07 Final Publication Verification: PENDING
- M00_L07 Final Publication State: NOT YET PUBLISHED / VERIFIED
- M00_L07 Real Hardware: REAL HARDWARE DEFERRED
- M00_L08: INACTIVE / NOT CREATED
- M00_L06 Real Hardware: REAL HARDWARE DEFERRED
- Scope: Future post-V00 mechanism curriculum roadmap
- Authority: Approved successor ADR to
  `ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`. The
  repository authority order remains unchanged.

## Context

`V00_L09_SwervePoseEstimatorVisionFusion` is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED`. Its implementation/freeze publication is `6548c98` with
subject `Complete V00_L09 Swerve pose estimator vision fusion`. Its later
metadata-reconciliation publication is `5d36529` with subject `Record V00_L09
publication metadata`. The accepted repository state is `HEAD = origin/main =
origin/HEAD = 5d36529`, ahead `0`, behind `0`, and the final V00 closure gate is
`PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`. No V00 lesson is active or reopened.

The repository already teaches the Frozen Backbone, IO and IOInputs, real versus
simulation replacement, subsystem ownership, safe stop, immutable Observations,
read-only telemetry, RobotContainer composition, vendor isolation, command
requirements, and dependency direction through the S00/A00/A01/V00 lineage.

The D01 mechanism lessons are a separate historical/parallel Tank Drive
practice line. They provide reference examples, but they are not the
predecessor of the post-V00 main line.

The accepted Architect roadmap gate is
`PASS_M00_COMPACT_REUSE_ROADMAP_ARCHITECT_ACCEPTED`, following the independent
review gate `PASS_M00_COMPACT_REUSE_ROADMAP_READY_FOR_ARCHITECT_LOCK`.

## Decision

Document the future successor module:

`M00 - Competition Mechanism Foundations`

Document its future repository location:

`real_robot_programming/module_M00/`

M00 shall inherit from the final frozen and User-published
`V00_L09_SwervePoseEstimatorVisionFusion` main-line snapshot. M00 shall not
inherit from D01.

The original ADR approval authorized the future roadmap only. The separate
2026-09-15 governance decision now authorizes preparation after this record is
reviewed and User-published. It does not activate M00, make M00_L01
`IN_PROGRESS`, create anything during this recording task, or authorize
production Java, tests, configuration, dependencies, simulation, real-robot
work, or implementation.

## Rationale

The new module provides a practical mechanism progression without redesigning
the existing Swerve, autonomous, PathPlanner, Vision, estimator, or fusion
architecture. The sequence reuses mastered architecture in complete mechanism
Foundation lessons and isolates genuinely new control, readiness, safety, and
coordination concepts.

The module is a curriculum boundary, not a version-control boundary. Git branch
selection remains User-owned and cannot substitute for an ADR or lesson
inheritance boundary.

## One-New-Concept Rule

Every lesson introduces exactly one new architectural, control, safety, or
coordination concept. One lesson is not required to equal one Java file, class,
package, or architecture layer.

Previously mastered architecture may be bundled as supporting implementation in
one lesson when all changed files express that lesson's single new concept.
New behavior such as feedback control, readiness, homing, travel-limit
enforcement, coordination, or autonomous event integration remains isolated.

## Prior-Knowledge Reuse Rule

The following are prior knowledge and may be applied together in a Foundation
lesson:

- vendor-neutral IO and IOInputs;
- deterministic Simulation/Noop and Real adapter replacement;
- subsystem ownership and safe stop;
- immutable mechanism Observations;
- read-only telemetry facades and RobotTelemetry integration;
- RobotContainer composition and implementation selection;
- vendor isolation, Constants authority, command requirements, and testing.

Reusing these patterns does not constitute multiple new concepts. A Foundation
lesson must not add closed-loop velocity, ready-at-speed, active homing,
travel-limit enforcement, cross-mechanism coordination, or autonomous event
integration at the same time.

## Foundation Reuse Policy

Each mechanism Foundation may contain the complete known architecture slice:

```text
hardware candidate
-> mechanism IO / IOInputs
-> Simulation/Noop and, when justified, selected Real IO
-> mechanism Subsystem and safe stop
-> immutable Observation
-> read-only TelemetryFacade
-> RobotTelemetry / RobotContainer composition
```

The Foundation's sole new concept is the independently owned mechanism
capability. A missing hardware adapter may remain `REAL HARDWARE DEFERRED`;
Simulation/Noop is valid evidence for architecture learning.

## Locked 16-Lesson Roadmap

The following order is locked for this approved ADR. Each later lesson inherits
only from the immediately preceding lesson after that predecessor is
`COMPLETE / FROZEN / READ-ONLY`.

### M00_L01 - Mechanism Architecture Reuse

- One concept: apply the mastered Frozen mechanism architecture to future
  non-drivetrain mechanism capabilities.
- Student question: how does the mastered drivetrain, vision, and autonomous
  architecture apply to non-drivetrain mechanisms?
- Locked directory identity: `M00_L01_MechanismArchitectureReuse`.
- Prerequisite: final frozen/published V00_L09.
- Excludes mechanism implementation, hardware selection, and behavior.

### M00_L02 - Mechanism Hardware Evidence Audit

- One concept: classify mechanism facts as `VERIFIED`, `PROVISIONAL`,
  `UNKNOWN`, or `NOT APPLICABLE`.
- Prerequisite: M00_L01 ownership map.
- Excludes guessed device values and mandatory final hardware selection.

### M00_L03 - Intake Foundation

- One concept: Intake is one independently owned robot mechanism capability.
- Prerequisite: M00_L02 evidence method.
- Excludes manual command ownership, automatic intake, coordination, and
  autonomous events.

### M00_L04 - Intake Command Ownership

- One concept: scheduler-managed manual ownership of Intake.
- Prerequisite: verified M00_L03 Intake capability.
- Excludes automatic sensing, Feeder coordination, and autonomous use.

### M00_L05 - Feeder Foundation

- One concept: Feeder is one independently owned transport mechanism.
- Prerequisite: M00_L04 and reused mechanism architecture.
- Excludes manual transport ownership, shooting, transfer, and autonomous use.

### M00_L06 - Feeder Command Ownership

- One concept: scheduler-managed manual transport ownership of Feeder.
- Prerequisite: verified M00_L05 Feeder capability.
- Excludes shooting, automatic staging, Intake transfer, and autonomous use.

### M00_L07 - Flywheel Foundation

- One concept: Flywheel is one independently owned rotational-speed mechanism.
- Prerequisite: M00_L06 and reused mechanism architecture.
- Excludes velocity feedback, readiness, shooting, and autonomous behavior.

### M00_L08 - Flywheel Closed-Loop Velocity

- One concept: velocity-setpoint feedback control.
- Prerequisite: verified M00_L07 Flywheel measurement and ownership.
- Excludes ready-at-speed policy, Feeder action, and shooting coordination.

### M00_L09 - Flywheel Ready-at-Speed

- One concept: deterministic readiness classification separate from velocity
  control.
- Prerequisite: verified M00_L08 velocity control.
- Excludes Feeder sequencing and autonomous behavior.

### M00_L10 - Elevator Foundation and Position-Reference Semantics

- One concept: Elevator is an independently owned position mechanism whose
  reported position has explicit reference meaning.
- Prerequisite: M00_L09 and the reused mechanism architecture.
- May define units, positive direction, zero meaning, relative/absolute
  semantics, and valid/unknown/unreferenced/disconnected states.
- Excludes active homing, position feedback, and travel-limit enforcement.

### M00_L11 - Elevator Closed-Loop Position

- One concept: position-setpoint feedback control.
- Prerequisite: verified M00_L10 position-reference semantics.
- Excludes active homing, travel-limit policy, and coordination.

### M00_L12 - Elevator Homing

- One concept: safely establish a trusted Elevator position reference.
- Prerequisite: verified M00_L11 position control and reference contract.
- Excludes general travel-limit enforcement and scoring behavior.

### M00_L13 - Elevator Travel-Limit Safety

- One concept: admit or reject Elevator closed-loop position targets against a vendor-neutral software operational travel envelope in logical meters before ElevatorIO.
- Prerequisite: verified M00_L12 homing/reference behavior.
- Excludes physical hard-limit protection, continuous overtravel protection, homing redesign, and cross-mechanism coordination.

### M00_L14 - Shoot Coordination

- One concept: one scheduler-managed command coordinates Flywheel readiness and
  Feeder action.
- Prerequisite: verified M00_L06 Feeder ownership and M00_L09 readiness.
- Requires both `FlywheelSubsystem` and `FeederSubsystem`.
- Excludes a ShooterSubsystem, ShooterIO, vision aiming, and autonomous events.

### M00_L15 - Intake-to-Feeder Coordination

- One concept: one scheduler-managed command coordinates Intake and Feeder
  transfer.
- Prerequisite: verified M00_L04 Intake and M00_L06 Feeder ownership.
- Excludes shooting, new sensing, and autonomous integration.

### M00_L16 - Mechanism Autonomous Event Integration

- One concept: schedule exactly one already-verified mechanism command through
  the existing autonomous event infrastructure.
- Prerequisite: verified M00_L14 or M00_L15 command and the frozen A01 event
  boundary.
- Excludes new mechanism behavior, path redesign, PathPlanner redesign, Swerve,
  Vision, pose-fusion, and multiple mechanism commands.

## Hardware-Evidence Policy

M00_L02 establishes an evidence method, not a requirement that every final
mechanism be selected at module start.

The following facts require explicit evidence and must use exactly one of the
four classifications `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or
`NOT APPLICABLE`:

- mechanical purpose and operating direction;
- motor/controller family, CAN bus, CAN ID, motor count, and follower layout;
- sensor type and mounting;
- gear ratio and mechanism conversion;
- inversion and sensor phase;
- neutral behavior;
- voltage, supply-current, stator-current, ramp, and peak-output limits;
- physical travel limits;
- firmware and vendor-library compatibility;
- configuration apply/readback behavior;
- Simulation support and commissioning/emergency-stop procedure.

Kraken X60 with Talon FX, Kraken X44 with Talon FX, and Minion with Talon FXS
remain candidates until evidence selects them. No CAN ID, bus, ratio, sensor,
limit, gain, speed, current, voltage, geometry, or calibration value may be
invented.

A Foundation may close with `REAL HARDWARE DEFERRED` when hardware evidence is
unavailable. A later real adapter must preserve the subsystem, command,
Observation, and telemetry contracts; a motor/controller swap should remain
primarily an IO/hardware-layer concern.

## Mechanism Ownership

The independent mechanism owners are:

- `IntakeSubsystem` with `IntakeIO`;
- `FeederSubsystem` with `FeederIO`;
- `FlywheelSubsystem` with `FlywheelIO`;
- `ElevatorSubsystem` with `ElevatorIO`.

Each IO interface owns its mechanism-specific Inputs snapshot. Each subsystem
owns behavior, state, and safe stop. Vendor APIs remain inside concrete IO
adapters. No mechanism is added merely because it appears in a command name.

## Shooter Ownership Decision

The initial shooting architecture is locked as:

```text
FlywheelSubsystem
+
FeederSubsystem
+
ShootCommand requiring both
```

No `ShooterSubsystem` or `ShooterIO` is authorized by this ADR. Shooting is
initially command-level coordination of independently owned Flywheel and Feeder
capabilities. A future hood, turret, pivot, or other independently owned
actuator receives its own reviewed subsystem/IO boundary. An umbrella Shooter
owner requires new ownership evidence and formal architecture review.

## RobotContainer Policy

`RobotContainer` remains the Composition Root only. It may construct objects,
select Real/Simulation/Noop implementations, inject dependencies, configure
bindings, construct telemetry facades, and connect dependencies.

It shall not contain PID or control math, velocity or position control,
readiness decisions, homing policy, travel-limit policy, sensor interpretation,
motor safety logic, telemetry calculations, or vendor behavior.

Simple private helpers such as `createIntakeIO()`, `createFeederIO()`,
`createFlywheelIO()`, and `createElevatorIO()` are allowed only for direct
implementation selection. Dependency-injection frameworks, service locators,
reflection, arbitrary registries, and generic mechanism factories are excluded.

## Observation and Telemetry Policy

Every mechanism follows:

```text
hardware
-> IOInputs
-> subsystem / estimator
-> immutable Observation
-> read-only telemetry
-> NT4 / Glass / log
```

IOInputs is mutable one-cycle transport only and must not be retained as public
mechanism state. Subsystems create immutable, vendor-neutral Observations with
explicit units, timing, and validity where applicable. Telemetry publishes
Observations only and cannot control behavior, schedule commands, interpret
hardware, or mutate state.

Readiness, homing, travel-limit, and other policies remain in their approved
subsystem/evaluator/command boundaries, not in telemetry.

## Protected Existing Systems

M00 shall preserve without redesign:

- the Frozen Backbone and Frozen Interface Contract;
- Swerve module architecture and `SwerveSubsystem` ownership;
- odometry and estimated-pose semantics;
- autonomous safety and centralized stop authority;
- PathPlanner, AutoBuilder, and NamedCommands/event ownership;
- VisionIO, Limelight adapter, VisionSubsystem, and VisionFusionCoordinator;
- `SwerveDrivePoseEstimator` ownership and vision-fusion boundaries;
- existing alliance-transform ownership and canonical field-frame rules.

M00_L16 may consume the existing autonomous event boundary but may not redesign
it.

## Evidence and Verification Policy

The course evidence vocabulary remains:

- `THEORY VERIFIED`;
- `SIMULATION VERIFIED`;
- `REAL HARDWARE VERIFIED`;
- `REAL HARDWARE DEFERRED`;
- `NOT APPLICABLE`.

Simulation cannot prove CAN identity, wiring, physical direction, gearing,
sensor phase, current or thermal behavior, travel limits, loading, friction,
shooter performance, or physical safety margins. Real-robot claims remain
bounded to the exact tested scope.

Each M00 lesson must pass the applicable architecture review, inherited
baseline build, focused tests, inherited regression, clean build, Simulation,
Driver Station/Glass, real-robot, documentation, and freeze gates. No lesson
may be marked complete from a build result alone.

## Activation Prerequisites

M00 activation requires all of the following:

1. V00_L09 is `COMPLETE`.
2. V00_L09 is `FROZEN / READ-ONLY`.
3. V00_L09 is User-published.
4. Final predecessor and publication evidence is recorded.
5. The normal copy/rename/generated-artifact-cleanup workflow is authorized.
6. `module_M00` and its first lesson are separately created through the
   approved lifecycle.
7. The active lesson count is reconciled so only the intended lesson is
   editable.

ADR approval does not activate M00. No active lesson state changes because of
this ADR.

## Governance Preparation Authorization

The Architect authorization
`PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED` is recorded with these exact state
distinctions:

```text
M00 Roadmap: APPROVED / ROADMAP AUTHORIZED
M00 Preparation: AUTHORIZED
M00 Runtime/Lesson Activation: NOT ACTIVE
Active Lesson Count: 0
M00_L01: NOT ACTIVE / NOT YET CREATED
Implementation Authorization: NONE
```

Preparation may occur only after this governance record is reviewed and
User-published. M00_L01 is not `IN_PROGRESS`, and neither `module_M00` nor its
first lesson is created by this authorization-recording task.

The locked first lesson identity is:

```text
Lesson: M00_L01 - Mechanism Architecture Reuse
Directory: M00_L01_MechanismArchitectureReuse
```

The exact predecessor/source is the complete frozen V00_L09 snapshot at
repository state `5d36529`, with implementation/freeze commit `6548c98`:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_V00\V00_L09_SwervePoseEstimatorVisionFusion
```

D01 is not the predecessor. The exact future destination is:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_M00\M00_L01_MechanismArchitectureReuse
```

The future preparation sequence is User-owned:

1. Start at the repository root.
2. Copy the complete frozen V00_L09 directory.
3. Create `module_M00` only as part of the authorized copy workflow.
4. Rename only the destination copy to `M00_L01_MechanismArchitectureReuse`.
5. Remove only the destination `build\` and `.gradle\`.
6. Select WPILib 2026 Java 17.
7. Run the inherited baseline clean build.
8. Report `BUILD SUCCESSFUL` and Git status.
9. Only then proceed to Architecture Audit, Design Lock, lifecycle activation,
   and separately granted implementation authorization.

The following command is recorded but is not run by this governance task. The
User runs it inside the future destination:

```powershell
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
.\gradlew.bat clean build "-Dorg.gradle.java.home=C:\Users\Public\wpilib\2026\jdk"
```

M00_L01's sole concept is Mechanism Architecture Reuse. It may teach reuse of
subsystem ownership, IO, immutable Observations, read-only telemetry,
composition-root assembly, safe stop, and architecture mapping. It must not
implement Intake, Feeder, Flywheel, Elevator, closed-loop control, readiness,
elevator position control, homing, travel-limit safety, coordination,
autonomous events, or any new hardware API.

Any future student-facing M00 Markdown must be two separate files, one English
and one Vietnamese, with identical structure, course/chapter identity, meaning,
evidence, and architecture rules. English is normative. Vietnamese must remain
student-friendly while preserving the same meaning. No student-facing M00
Markdown is created by this task.

Evidence classifications are limited to `THEORY VERIFIED`, `SIMULATION
VERIFIED`, `REAL HARDWARE VERIFIED`, `REAL HARDWARE DEFERRED`, and `NOT
APPLICABLE`. Runtime applicability is not claimed for M00_L01; its future
Design Lock decides which runtime evidence applies. No V00 rerun is required.

## M00_L01 Controlled Activation — 2026-09-15

The User completed the authorized copy, rename, destination-only generated
artifact cleanup, and WPILib Java 17 inherited baseline build. The accepted
baseline result is `BUILD SUCCESSFUL in 20s` with seven actionable tasks
executed. The independent read-only Architecture / Inheritance Audit passed
with 601 comparable non-generated files in both the predecessor and candidate,
zero missing files, zero candidate-only files, and zero SHA-256 differences.

The Architect then issued `PASS_M00_L01_FINAL_DESIGN_LOCK`. The locked lesson
is documentation-only and teaches Mechanism Architecture Reuse. Production
Java, test code, configuration, vendordeps, deploy assets, new runtime behavior,
and mechanism hardware APIs all have authorization `NONE`.

This controlled activation records:

```text
M00 Roadmap: APPROVED / ROADMAP AUTHORIZED
M00 Preparation: COMPLETE / ACCEPTED
M00_L01 Status: IN_PROGRESS
M00_L01 Active State: IN_PROGRESS / EDITABLE
M00_L01 Freeze State: EDITABLE
Active Lesson Count: 1
Implementation Authorization: NONE
Production Code Authorization: NONE
```

V00_L09 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.
At this controlled-activation point, M00_L01 evidence was `THEORY VERIFIED`.
Focused new tests were `NOT APPLICABLE`; Simulation, Driver Station / Glass,
and real hardware were `NOT APPLICABLE` because no executable or hardware
behavior was introduced. The User-owned final inherited clean build/regression
was still required before final lesson closure; its later result is recorded
below.

Any new student-facing M00 Markdown remains subject to the separate English and
Vietnamese file rule with identical structure, identity, meaning, evidence, and
architecture rules. No student learning guide was created by the activation
itself.

## M00_L01 Pre-Freeze Documentation and Closure Readiness — 2026-09-15

The separately authorized paired English and Vietnamese learning guides were
created with identical 21-section structure and equivalent technical meaning.
The first independent documentation review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`
because both guides omitted the rule that `Constants.java` remains the default
configuration authority. The minimal two-guide repair added equivalent
statements and defined no mechanism configuration value, producing
`PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`. The independent
rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`,
and the Architect recorded
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

The User then supplied the distinct final inherited clean build/regression:
`BUILD SUCCESSFUL in 23s`, with 7 actionable tasks executed. The accepted gate
is `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`. The final read-only
architecture/documentation closure review passed at
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`, and
the Architect accepted that review through
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical/content closure readiness is `PASS`.

At that pre-freeze reconciliation point, M00_L01 remained the sole `IN_PROGRESS
/ EDITABLE` lesson, freeze state `EDITABLE`, with active lesson count `1`, Design Lock
`PASS_M00_L01_FINAL_DESIGN_LOCK`, and production-code authorization `NONE`.
Evidence remains `THEORY VERIFIED`; focused new tests, Simulation, Driver
Station / Glass, and real hardware are `NOT APPLICABLE`. No production Java,
test code, configuration, vendordep, deploy asset, runtime behavior, mechanism
hardware API, or mechanism implementation changed. Explicit Architect freeze
authorization and User-owned Git publication remained pending.

## M00_L01 Final Documentation-Only Freeze Closure — 2026-09-15

The pre-freeze documentation reconciliation passed through
`PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`,
`PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`,
and
`PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then issued `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

At the freeze-recording point, M00_L01 was `COMPLETE / FROZEN / READ-ONLY`, its freeze state was `FROZEN /
READ-ONLY`, and active lesson count is `0`. Technical/content closure remains
`PASS`, the Design Lock remains `PASS_M00_L01_FINAL_DESIGN_LOCK`, and
production-code authorization remains `NONE`. Evidence remains `THEORY
VERIFIED`; focused new tests, Simulation, Driver Station / Glass, and real
hardware remain `NOT APPLICABLE`.

Publication was `NOT YET PUBLISHED / PENDING USER GIT`; Git commit was
`PENDING USER COMMIT`, Git push was `PENDING USER PUSH`, and remote verification
was `PENDING`. M00_L02 was not active, was not created, and received no lifecycle
change from this closure. The M00 module is not declared complete. The locked
16-lesson roadmap is unchanged.

## M00_L01 Lesson Publication Metadata — 2026-09-15

The User published the frozen lesson at commit `83907ab` with subject `Complete
M00_L01 mechanism architecture reuse`. User-supplied evidence records branch
`main` with `HEAD = origin/main = origin/HEAD = 83907ab`; remote publication
verification passed at `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`.

M00_L01 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, active
lesson count remains `0`, and M00_L02 remains `NOT ACTIVE / NOT CREATED`.
Publication-metadata reconciliation is a distinct later record and remains
`PENDING USER COMMIT`; metadata push is `PENDING USER PUSH`, and final remote
verification of that later commit is `PENDING`. This publication record does
not complete the M00 module or alter the locked roadmap.

## M00_L02 Final Freeze and Lifecycle Closure — 2026-09-16

The accepted M00_L01 final state is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED
/ VERIFIED`, with primary publication `83907ab`, metadata publication
`f523118`, and `PASS_M00_L01_FINAL_PUBLICATION_COMPLETE`. It remains the frozen
predecessor.

M00_L02 preparation and inheritance are accepted: the outer candidate baseline
passed under WPILib Java 17 with `BUILD SUCCESSFUL in 58s`; generated reports
record 637 tests with no failures, errors, or skips. The accidental nested
M00_L01 project was forensically isolated, removed through the controlled
repair, and verified absent. Current inheritance evidence is 604/604 comparable
files and 173/173 protected files with zero missing, extra, or SHA-256-different
files. The Architecture / Inheritance Audit passed at
`PASS_M00_L02_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`.

The Architect explicitly issued `PASS_M00_L02_FINAL_DESIGN_LOCK`. The
controlled activation passed through
`PASS_M00_L02_CONTROLLED_ACTIVATION_RECORDED_READY_FOR_DOCUMENTATION_IMPLEMENTATION_AUTHORIZATION`
and `PASS_M00_L02_CONTROLLED_ACTIVATION_ACCEPTED`.

Documentation implementation was separately authorized at
`PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`. The paired English and
Vietnamese guides are complete with matching 25-section structure, 15
knowledge-check questions, 15 answers, and 80-row evidence matrices. Each
matrix contains 8 `VERIFIED`, 0 `PROVISIONAL`, and 72 `UNKNOWN` rows.

The initial independent review preserved the exact documentation-quality gate
`HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`.
It identified the missing explicit `Constants.java` authorization boundary,
the incomplete full M00 ownership/shooting lock, and incomplete required
knowledge-check coverage. The bounded two-guide repair passed through
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_AUTHORIZED`,
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`, and
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_REREVIEW`.
Independent rereview then passed at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD` and
was accepted at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_BUILD`.

The User-supplied final build/regression passed with `BUILD SUCCESSFUL in 33s`
and 7 actionable tasks executed. The accepted gates are
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION` and
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
The final read-only closure review passed through
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Reconciliation was authorized by
`PASS_M00_L02_DOCUMENTATION_RECONCILIATION_AUTHORIZED` and is now complete and
recorded at
`PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.

The independent reconciliation review passed at
`PASS_M00_L02_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then issued `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`. The final
pre-publication lifecycle is:

```text
M00_L02 Status: COMPLETE
M00_L02 Active State: COMPLETE / FROZEN / READ-ONLY
M00_L02 Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Design Lock: PASS_M00_L02_FINAL_DESIGN_LOCK
Production Code Authorization: NONE
Test Implementation Authorization: NONE
Configuration Authorization: NONE
Runtime Behavior Authorization: NONE
Documentation Implementation Authorization: PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED
Documentation Implementation: COMPLETE
Independent Documentation Rereview: PASS
Final User Build: PASS
Final Closure Review: PASS
Documentation Reconciliation: PASS
Independent Reconciliation Review: PASS
Freeze Authorization: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
Git Commit: PENDING USER COMMIT
Git Push: PENDING USER PUSH
Remote Verification: PENDING
Publication Metadata Reconciliation: PENDING
Publication: PENDING USER GIT PUBLICATION
M00_L03: NOT ACTIVE / NOT CREATED
```

The documentation implementation authorization is
`PASS_M00_L02_DOCUMENTATION_IMPLEMENTATION_AUTHORIZED`; the historical pending
value in the activation record is superseded by that later authorization. The
sole concept remains Mechanism Hardware Evidence Audit. Each audited fact
must use exactly one state: `VERIFIED`, `PROVISIONAL`, `UNKNOWN`, or `NOT
APPLICABLE`. No guessed hardware fact or typical FRC value may be promoted to
`VERIFIED`. Evidence is `THEORY VERIFIED` as a required lesson gate; focused
new tests and Simulation are `NOT APPLICABLE`; Driver Station / Glass is `NOT
APPLICABLE`; real hardware is `REAL HARDWARE DEFERRED`.

No mechanism implementation, command, Observation, adapter, sensor boundary,
constant, CAN/PID configuration, runtime telemetry, or technical refactor was
introduced. M00_L02 is `COMPLETE / FROZEN / READ-ONLY` but not yet published,
and M00_L03 is `NOT ACTIVE / NOT CREATED`. The locked 16-lesson order and every
lesson scope remain unchanged.

## M00_L02 Primary Publication and Metadata Reconciliation — 2026-09-16

The User completed the primary publication at full commit
`65a92a4a5806fd5134e0114e851c4e4cc093c58e` with subject `Complete M00_L02
mechanism hardware evidence audit`. The primary push is `PASS`; `HEAD`,
`origin/main`, and `origin/HEAD` were observed aligned at short commit
`65a92a4`. The primary publication gates are
`PASS_M00_L02_PRIMARY_GIT_PUBLICATION` and
`PASS_M00_L02_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.

M00_L02 remains `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.
Publication metadata reconciliation is `COMPLETE`, while metadata Git
publication and final publication completion remain `PENDING`. M00_L03 remains
`NOT ACTIVE / NOT CREATED`; all 16 lesson headings and scopes remain unchanged.

## M00_L03 Controlled Documentation-Only Lifecycle Activation — 2026-09-16

The prepared candidate identity is `M00_L03 - Intake Foundation`, filesystem
`M00_L03_IntakeFoundation`, with M00_L02 as its final frozen and published
predecessor. The candidate baseline build passed under Java 17.0.16 Temurin
with exit code `0`. The earlier preparation-script requirement for a
lesson-local `AGENTS.md` was defective; repository-root `AGENTS.md` is the
authority, the candidate was unaffected, and no recopy was required.

Inheritance passed at 607/607 comparable files and 173/173 protected files,
with zero missing, extra, or SHA-256-different files. The audit gate is
`PASS_M00_L03_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`, and
the Architect Design Lock is `PASS_M00_L03_FINAL_DESIGN_LOCK`.

M00_L03 is now the sole `IN_PROGRESS / EDITABLE` lesson with active lesson
count `1` and freeze state `EDITABLE`. Its sole concept is an independently
owned, vendor-neutral Intake mechanism foundation. Production, test, and
documentation implementation remain pending separate Architect authorization.
No vendor adapter, Constants change, command, binding, default/manual command
ownership, or physical hardware claim is authorized. M00_L04 remains `NOT
ACTIVE / NOT CREATED`, and the locked 16-lesson roadmap is unchanged.

## M00_L03 Implementation, Verification, and Documentation Reconciliation — 2026-09-16

The activation paragraph above is preserved as historical state. Separate
authorization later issued
`PASS_M00_L03_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED`, and the bounded
M00_L03 Intake foundation was implemented. The authorized architecture contains
vendor-neutral `IntakeIO` and its owned input snapshot, `IntakeIONoop`,
`IntakeSubsystem`, semantic `requestIntake()`, centralized `stop()`, requested
states `STOPPED` and `INTAKE_REQUESTED`, immutable `IntakeObservation`,
read-only telemetry, RobotContainer composition, and focused tests.

The initial focused run executed 16 tests: 15 passed and one failed in
`IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`
because an optional callback was dereferenced at line 69. This was classified
as a test defect. The minimal repair guarded the optional callback in
`IntakeSubsystemTest.java` and removed no assertions. The focused retest passed
with `BUILD SUCCESSFUL in 4s` and exit code `0`. The full clean regression
passed with `BUILD SUCCESSFUL in 20s`, exit code `0`, and 7 of 7 actionable
tasks executed. The independent implementation review passed and was accepted.

User-owned Simulation passed for startup, Noop composition, subsystem
integration, and Intake NetworkTables/telemetry presence while Disabled. This
is bounded software evidence only. It does not verify wiring, CAN identity,
controller configuration, physical direction, motion, current, load, force, or
physical stopping. Real-hardware verification remains deferred.

Documentation authorization is recorded at
`PASS_M00_L03_DOCUMENTATION_IMPLEMENTATION_AND_LIFECYCLE_RECONCILIATION_AUTHORIZED`.
The matching English and Vietnamese 34-section learning guides, each with 15
review questions and 15 answers, are implemented. Independent documentation
review, the final User closure build, final closure review, final lifecycle
reconciliation, freeze authorization, `COMPLETE / FROZEN / READ-ONLY`, and User
publication remain pending.

No Intake vendor adapter or `Constants.java` change was introduced. Unsupported
physical hardware facts remain `UNKNOWN`. M00_L04 retains exclusive ownership
of scheduler-managed Intake Commands, requirements, bindings, interruption
behavior, and default/manual ownership. It remains inactive and uncreated.

## M00_L03 Final Closure Review and Lifecycle Reconciliation — 2026-09-17

The initial independent documentation review returned `HOLD` because Step 16
of the transition guide incorrectly described `IntakeIOInputs` as an immutable
input snapshot. The correct architecture is a mutable one-cycle IO transport
snapshot feeding subsystem processing, followed by an immutable
`IntakeObservation`. The authorized bounded repair changed only the Step 16
phrase to `mutable one-cycle input snapshot` and passed at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_ONE_LINE_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`.
The Architect accepted the repair at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_DOCUMENTATION_REREVIEW`.

The independent documentation rereview passed at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_CLOSURE_BUILD`
and was accepted at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_CLOSURE_BUILD`.
The User then supplied final closure-build evidence: `BUILD SUCCESSFUL in 42s`,
7 actionable tasks executed, and exit code `0`. The accepted build gates are
`PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION` and
`PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.

The final read-only closure review passed at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical defects remaining are `NONE`; substantive documentation defects
remaining are `NONE`. This documentation/lifecycle reconciliation is complete.

M00_L03 remains the sole `IN_PROGRESS / EDITABLE` lesson, freeze state
`EDITABLE`, with active lesson count `1`. It is not yet `COMPLETE / FROZEN /
READ-ONLY`. Focused tests, full regression, final closure build, bounded
software/Noop/composition Simulation, student documentation, independent
documentation rereview, and final closure review are verified or PASS as
applicable. Driver Station / Glass remain `NOT APPLICABLE`; real hardware
remains `REAL HARDWARE DEFERRED`. Independent reconciliation review, explicit
Architect freeze authorization, freeze recording, and User-owned publication
remain pending. M00_L04 remains `NOT ACTIVE / NOT CREATED`.

## M00_L03 Final Lifecycle Freeze — 2026-09-17

The preceding final-closure section is preserved as the historical pre-freeze
state. Independent reconciliation review passed at
`PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then explicitly authorized the documentation-only lifecycle
transition through `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.

M00_L03 is now `COMPLETE / FROZEN / READ-ONLY`, with freeze state `FROZEN` and
active lesson count `0`. Preparation, Architecture / Inheritance Audit, Final
Design Lock, controlled activation, implementation, minimal test repair,
focused retest, full regression, bounded Simulation, independent
implementation review, student documentation, bounded documentation repair,
independent documentation rereview, final closure build, final closure review,
lifecycle reconciliation, independent reconciliation review, and Architect
freeze authorization are PASS.

```text
Preparation: PASS
Architecture / Inheritance Audit: PASS
Final Design Lock: PASS
Controlled Activation: PASS
Implementation: PASS
Focused Test Initial Run: HOLD — TEST DEFECT
Minimal Test Repair: PASS
Focused Retest: PASS / VERIFIED
Full Regression: PASS / VERIFIED
Bounded Simulation: PASS / VERIFIED — SOFTWARE/NOOP/COMPOSITION ONLY
Independent Implementation Review: PASS
Student Documentation: PASS / VERIFIED
Independent Documentation Review: HOLD — STEP 16 TERMINOLOGY DEFECT
Bounded Documentation Repair: PASS
Independent Documentation Rereview: PASS
Final Closure Build: PASS / VERIFIED
Final Closure Review: PASS
Lifecycle Reconciliation: PASS / COMPLETE
Independent Reconciliation Review: PASS
Architect Freeze Authorization: PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION
Lifecycle: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Publication: PENDING USER GIT
M00_L04: NOT ACTIVE / NOT CREATED
```

The initial focused-test HOLD remains preserved as a test defect, and the
initial independent documentation HOLD remains preserved as the Step 16
mutable/immutable terminology defect. `IntakeIOInputs` remains a mutable
one-cycle transport snapshot; `IntakeObservation` remains immutable. Real
hardware remains `REAL HARDWARE DEFERRED`, and unsupported physical hardware
facts remain `UNKNOWN`.

Publication remains `PENDING USER GIT`. This freeze does not claim a commit,
push, remote verification, or publication completion. M00_L04 remains `NOT
ACTIVE / NOT CREATED`, and the locked M00_L01 through M00_L16 roadmap remains
unchanged.

## M00_L03 Primary Publication and Metadata Reconciliation — 2026-09-17

The accepted primary publication gate is
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION`, with Architect acceptance at
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.
The User-owned primary publication commit is
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`; the primary push passed as
`84010ff..3d94dc6  main -> main`. Post-push local `HEAD` and `origin/main`
both matched the full primary commit, so remote alignment is `PASS`.

```text
Lifecycle: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Primary Git Publication: PASS
Primary Publication Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
Remote Alignment: PASS
Publication Metadata Reconciliation: COMPLETE
Metadata Git Publication: PENDING USER GIT
Final Publication Verification: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

The primary lesson snapshot is published, but the two-commit publication model
is not yet complete. Independent metadata review, User-owned metadata Git
publication, metadata remote verification, and final publication verification
remain pending. The locked M00_L01 through M00_L16 roadmap is unchanged.

## M00_L03 Final Publication and M00_L04 Controlled Activation — 2026-09-18

M00_L03 completed final publication at primary commit
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3` and metadata commit
`b2464f66da42a6281acb7bfc709f2a3b83296505`; metadata remote alignment and
final publication verification are `PASS`. M00_L03 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`.

The prepared M00_L04 candidate passed its accepted Java baseline, exact
inheritance audit, Frozen Backbone review, and one-new-concept review. The
Architect issued `PASS_M00_L04_FINAL_DESIGN_LOCK`. M00_L04 is now the sole
`IN_PROGRESS / EDITABLE` lesson with active lesson count `1`.

The locked scope is exactly one dedicated `RunIntakeCommand`, a semantic Xbox
Right Bumper `whileTrue` binding in `RobotContainer`, and focused command
lifecycle, requirement ownership, safe-stop, binding, and architecture tests.
Implementation remains `NOT STARTED / PENDING AUTHORIZATION`. M00_L05 remains
inactive and uncreated; the M00_L01 through M00_L16 sequence is unchanged.

## M00_L04 Post-Verification Documentation and Lifecycle Reconciliation — 2026-09-18

The preceding activation paragraph is preserved as historical pre-
implementation state. Separate authorization was consumed, and the exact
locked implementation is complete. Production created
`RunIntakeCommand.java` and modified only the bounded `RobotContainer` wiring.
Tests created `RunIntakeCommandTest.java` and
`RobotContainerIntakeCommandBindingTest.java` and narrowly updated
`IntakeArchitectureBoundaryTest.java`. No other production or test Java
changed.

The command requires exactly `IntakeSubsystem`, requests once during
`initialize()`, performs no repeated request during `execute()`, does not
self-finish, and unconditionally delegates `end(...)` to
`IntakeSubsystem.stop()`. The existing semantic Right Bumper uses `whileTrue`;
the inherited Back/View binding remains intact; no Intake default command was
added.

The User-verified evidence is 14/14 focused tests PASS with `BUILD SUCCESSFUL
in 28s`, four tasks executed, exit code 0; full clean regression PASS with
`BUILD SUCCESSFUL in 47s`, seven tasks executed, exit code 0; `SIMULATION
VERIFIED / BOUNDED`; and Driver Station `VERIFIED / BOUNDED` with observed
software state `STOPPED -> INTAKE_REQUESTED -> STOPPED`. Glass remains `NOT
APPLICABLE` as a distinct completion gate. Real hardware remains `REAL
HARDWARE DEFERRED`.

The independent implementation review passed and was accepted for
documentation implementation. Matching English and Vietnamese student guides
are now created. `IntakeIOInputs` remains mutable one-cycle transport;
`IntakeObservation` remains immutable vendor-neutral meaning. The Noop's
`Available=false` and `Connected=false` are expected and do not contradict an
`INTAKE_REQUESTED` software intent.

M00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson with active lesson
count `1`. Independent documentation review, final closure build/review,
lifecycle reconciliation, freeze authorization, freeze, and the two-commit
publication workflow remain pending. M00_L04 is not complete, frozen, or
published. M00_L05 remains inactive and uncreated.

## M00_L04 Primary Publication and Metadata Reconciliation — 2026-09-18

The preceding lifecycle history remains preserved. After final closure and
freeze, the User completed the primary M00_L04 publication at commit
`5c86be3`, subject `Complete M00_L04 intake command ownership`. Accepted
post-push evidence records `HEAD = origin/main = origin/HEAD = 5c86be3`, so
primary remote alignment is `PASS`.

M00_L04 remains `COMPLETE / FROZEN / READ-ONLY`; implementation and
documentation are `COMPLETE / VERIFIED`; final closure review is `PASS`; real
hardware remains `REAL HARDWARE DEFERRED`; active lesson count is `0`; no M00
lesson is active; and M00_L05 remains `NOT ACTIVE / NOT CREATED`. Publication
metadata reconciliation is complete in the working tree, while the separate
User-owned metadata commit and push remain pending. No metadata SHA or final
publication completion is claimed.

## M00_L04 Final Publication and M00_L05 Controlled Activation — 2026-09-18

The preceding M00_L04 metadata section is preserved as historical pre-metadata
state. M00_L04 completed metadata publication at `24738e6`; accepted final
alignment is `HEAD = origin/main = origin/HEAD = 24738e6`, and final
publication verification is `PASS`. Its final state is `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`.

The prepared M00_L05 candidate passed its accepted Java 17 baseline build and
the independent architecture/inheritance audit with 285/285 governed files
byte-identical. The Architect issued
`PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`. M00_L05 is
now the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`.

The locked concept is one independently owned Feeder transport capability.
The locked semantic API is `FeederSubsystem.requestFeed()` with requested
states `STOPPED` and `FEED_REQUESTED`. The locked runtime is `FeederIONoop`
only; dynamic Feeder simulation, a real adapter, vendor APIs, physical CAN
configuration, and `Constants.java` changes are not authorized. The future
observation fields are exactly `available`, `connected`, and
`requestedState`; Feeder telemetry and RobotTelemetry integration are in
scope and remain read-only. Real hardware is `REAL HARDWARE DEFERRED`.

This activation records no implementation. M00_L06 remains `NOT ACTIVE / NOT
CREATED`, and the locked M00_L01 through M00_L16 roadmap is unchanged.

## M00_L05 Post-Verification Documentation and Lifecycle Reconciliation — 2026-09-19

The preceding activation paragraph is historical. Separate implementation
authorization was consumed, and the exact locked implementation is complete.
Production created the five Feeder foundation types and modified only
`RobotContainer.java` and `RobotTelemetry.java`. Tests created exactly the six
authorized Feeder test files.

The initial focused run completed 19 tests with 18 PASS and one test-defect
failure. `FeederArchitectureBoundaryTest` falsely matched the valid phrase
`current cycle`; its one-file repair replaced brittle text matching with
reflection over non-static, non-synthetic `FeederIOInputs` fields. All six
focused test classes then passed with `BUILD SUCCESSFUL in 26s`, exit code `0`.

The initial full clean regression completed 682 tests with 681 PASS. The one
failure came from shared singleton scheduler state: the intentional
`new FeederSubsystem(null)` test allowed `SubsystemBase` registration before
subclass null rejection. The one-file repair added `@AfterEach`
`CommandScheduler.getInstance().unregisterAllSubsystems()` cleanup while
preserving null rejection. The final full clean regression passed 682 of 682
tests with `BUILD SUCCESSFUL in 51s`, seven tasks executed, and exit code `0`.

Bounded WPILib Simulation and HALSIM Robot State verification passed with the
Noop facts `Available=false`, `Connected=false`, and
`RequestedState=STOPPED` in Disabled and Teleoperated Enabled states. Real
hardware remains `REAL HARDWARE DEFERRED`; physical Feeder facts remain
unknown, and CAN 45-49 remains a planning reservation only.

The independent implementation review passed and was accepted for
documentation implementation. Paired English and Vietnamese student guides
and the lifecycle records are implemented and ready for independent
documentation review. M00_L05 remains the sole `IN_PROGRESS / EDITABLE`
lesson with active lesson count `1`; it is not complete, frozen, or published.
M00_L06 remains `NOT ACTIVE / NOT CREATED`.

## M00_L05 Final Lifecycle Freeze — 2026-09-19

The preceding post-verification section is preserved as historical pre-freeze
state. The bounded documentation-history repair and independent documentation
rereview passed. The User-supplied final closure build passed with `BUILD
SUCCESSFUL in 41s`, 7/7 tasks executed, and exit code `0`. The independent final
closure review returned
`PASS_M00_L05_FINAL_CLOSURE_REVIEW_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`,
and the Architect accepted it through
`PASS_M00_L05_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`.

M00_L05 is therefore `COMPLETE / FROZEN / READ-ONLY`. Active lesson count is
`0`, no M00 lesson is active, and M00_L06 remains `NOT ACTIVE / NOT CREATED`.
Implementation and accepted verification evidence remain unchanged, real
hardware remains `REAL HARDWARE DEFERRED`, and CAN 45-49 remains a planning
reservation only. Primary Git publication, publication metadata reconciliation,
metadata publication, and final publication verification remain pending and
User-owned. This closure does not change the locked M00_L01 through M00_L16
roadmap or activate M00_L06.

## M00_L05 Primary Publication and Metadata Reconciliation — 2026-09-19

The User completed the primary M00_L05 publication at full commit
`5709f1d74b3318303bcc56779315b243dd81770b`, subject `Complete M00_L05 feeder foundation`.
Accepted evidence records push `24738e6..5709f1d main -> main`
and `HEAD == origin/main == 5709f1d74b3318303bcc56779315b243dd81770b`.

M00_L05 remains `COMPLETE / FROZEN / READ-ONLY`; primary publication and
primary remote alignment are complete, and publication metadata reconciliation
is complete. Metadata publication and final publication verification remain
pending, with no metadata commit hash claimed. Active lesson count remains `0`,
no M00 lesson is active, and M00_L06 remains `NOT ACTIVE / NOT CREATED`. This
metadata-only reconciliation preserves the locked M00_L01 through M00_L16
roadmap and does not authorize M00_L06 preparation or activation.

## M00_L05 Final Publication and M00_L06 Controlled Activation — 2026-09-19

The preceding primary-publication state is historical. User evidence records
M00_L05 metadata publication at
`1d6fadeec57fbfd3be245746b21d06e58b79518f`, subject `Record M00_L05
publication metadata`, remote alignment PASS, and final publication gate
`PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`. M00_L05 is `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`.

The prepared M00_L06 candidate passed its accepted baseline build (`BUILD
SUCCESSFUL in 40s`, 7 actionable tasks, 6 executed, 1 up-to-date, exit code
`0`) and independent architecture/inheritance audit (299 governed files, 299
byte-identical, no missing, added, or changed files). The Architect accepted
`PASS_M00_L06_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK` and
issued `PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

M00_L06 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson, with active lesson count `1`. Its one new concept is scheduler-
managed command ownership of the existing Feeder semantic API. The future
production boundary is exactly creation of `RunFeederCommand.java` and
modification of `RobotContainer.java`. The focused-test boundary is exactly
creation of `RunFeederCommandTest.java`,
`RobotContainerFeederCommandBindingTest.java`, and
`FeederCommandArchitectureBoundaryTest.java`.

The locked command requires exactly `FeederSubsystem`, requests feed exactly
once from `initialize()`, has intentionally empty `execute()`, never finishes
on its own, and unconditionally stops through `FeederSubsystem.stop()` on
normal or interrupted end. RobotContainer will bind it to
`driverController.leftBumper().whileTrue(runFeederCommand)`. No default Feeder
command is authorized. Implementation remains `NOT STARTED` and requires a
separate authorization.

This activation changes no Java, tests, Constants, CAN registry, vendordeps,
Gradle, deploy assets, hardware maps, or frozen lessons. `FeederIONoop` remains
the only runtime selection, real hardware remains deferred, CAN 45-49 remains
a planning reservation, and the M00_L01 through M00_L16 roadmap is unchanged.

## M00_L06 Implementation, Verification, and Bounded Documentation Repair — 2026-09-19

The preceding controlled-activation section is retained as the historical
activation-time record. Separate implementation authorization was subsequently
accepted and consumed. M00_L06 implementation is complete within the exact
production boundary: creation of `RunFeederCommand.java` and modification of
`RobotContainer.java`. The three authorized focused tests were created, and
the inherited `FeederArchitectureBoundaryTest.java` was reconciled as
`EXPECTED INHERITED TEST CONTRACT EVOLUTION`, not a production defect.

The Independent Static Rereview passed. The four authorized focused test
classes passed with `BUILD SUCCESSFUL in 7s`, four actionable tasks up-to-date,
and exit code `0`. The full `gradlew clean build` regression passed with
`BUILD SUCCESSFUL in 37s`, seven actionable tasks executed, and exit code `0`.
Bounded Simulation checkpoints A-G passed, including Left Bumper release stop
and disable-while-held stop. The accepted evidence remains `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.

The documentation phase completed, after which the Independent Closure Review
returned `HOLD` for exactly three documentation/lifecycle inconsistencies. The
authorized bounded repair reconciled the current repository lifecycle record,
removed unsupported Intake observations at Simulation checkpoints A, B, C,
and E from both student guides, and corrected the inherited-test path in the
transition guide. Independent closure rereview remains pending.

M00_L06 remains the sole `IN_PROGRESS / ACTIVE / EDITABLE` lesson with active
lesson count `1`. It is not `COMPLETE`, `FROZEN`, or `PUBLISHED`. `FeederIONoop`
remains the only Feeder runtime implementation, CAN 45-49 remains a planning
reservation only, real hardware remains deferred, and the locked M00_L01
through M00_L16 roadmap is unchanged. M00_L14, M00_L15, and M00_L16 remain
protected future scope.

## M00_L06 Final Lifecycle Freeze — 2026-09-20

The preceding implementation and bounded-repair section is retained as the
historical pre-freeze state. The later Independent Closure Rereview returned
`HOLD` solely because the transition guide had not yet recorded the complete
closure history. The authorized bounded transition-history repair resolved that
last documentation gap. The final Independent Closure Rereview then passed with
verdict `READY_FOR_FREEZE` and exact remaining findings `NONE`.

The Architect accepted the final result through
`PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and authorized
`AUTHORIZED_FOR_FREEZE`. M00_L06 is therefore `COMPLETE / FROZEN / READ-ONLY`.
Active lesson count is `0`, no M00 lesson is active, and M00_L07 remains `NOT
ACTIVE / NOT CREATED`. Implementation and accepted verification evidence remain
unchanged. Evidence remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED`; `FeederIONoop` remains the only runtime implementation and
CAN 45-49 remains a planning reservation only.

The locked M00_L01 through M00_L16 roadmap and protected M00_L14 through M00_L16
scope remain unchanged. User-owned publication is pending; no Git commit, push,
publication, or M00_L07 activation is claimed.

## M00_L06 Primary Publication and Metadata Reconciliation — 2026-09-20

The preceding freeze section is preserved as the historical pre-publication
state. Accepted gate `PASS_M00_L06_PRIMARY_PUBLICATION` records the User-owned
primary publication commit `f102a5e662877f8cb49eb63f2cfd888ac356bea4` with
subject `Complete M00_L06 Feeder command ownership`. Primary push is `PASS`.
Accepted remote evidence records `HEAD = origin/main =
f102a5e662877f8cb49eb63f2cfd888ac356bea4`, so primary remote alignment is
`PASS`.

M00_L06 remains `COMPLETE / FROZEN / READ-ONLY`; active lesson count remains
`0`, and no M00 lesson is active. Publication metadata reconciliation is
`COMPLETE / PREPARED FOR USER COMMIT`. The separate metadata Git publication
and final publication verification remain `PENDING`; final `PUBLISHED /
VERIFIED` status is not claimed. M00_L07 remains `NOT ACTIVE / NOT CREATED`.

## M00_L07 Controlled Activation — 2026-09-20

The preceding M00_L06 publication section is retained as historical. The
accepted activation prerequisite records M00_L06 as `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`. The User prepared
`M00_L07_FlywheelFoundation` from that final predecessor, removed generated
candidate artifacts, and supplied an untouched-inheritance baseline of `BUILD
SUCCESSFUL in 38s`, 6 actionable tasks, all 6 executed.

The accepted Architecture / Inheritance Audit compared 306 governed files:
306 were byte-identical, with zero missing, added, or changed files. The
Architect accepted `PASS_M00_L07_FINAL_DESIGN_LOCK` for the one new concept:
Flywheel is one independently owned rotational-speed mechanism.

M00_L07 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson. Active lesson count is `1`, and the current active M00 lesson is
`M00_L07`. Implementation remains `PENDING SEPARATE AUTHORIZATION`.
Activation adds no production or test Java, command, binding, Constants entry,
hardware adapter, or autonomous integration. The locked future runtime is
`FlywheelIONoop` only; CAN 50-54 remains planning-only; physical hardware and
real-hardware verification remain deferred. M00_L08 is `INACTIVE / NOT
CREATED`, and M00_L08, M00_L09, M00_L14, and M00_L16 scopes remain protected.

## Non-Goals and Exclusions

This ADR does not authorize:

- any M00 runtime/lifecycle activation beyond the recorded M00_L07 activation;
- source, test, Gradle, vendordep, PathPlanner, or configuration changes;
- changes to V00_L09 or any frozen predecessor;
- redesign of Swerve, Vision, autonomous, estimator, or fusion ownership;
- guessed hardware values or unsupported tuning claims;
- a ShooterSubsystem or ShooterIO without new ownership evidence;
- additional M00 lessons or a reordered/renamed/merged/split sequence;
- competition strategy, final characterization, or readiness claims.

## Consequences

- M00 has a documented future boundary after final published V00_L09.
- D01 remains useful as historical mechanism reference without becoming a
  predecessor.
- Mechanism architecture can be reused without repetitive layer-only lessons.
- New feedback, readiness, homing, limit, coordination, and event concepts
  remain independently teachable and verifiable.
- Hardware uncertainty can be recorded honestly without inventing constants or
  blocking architecture learning.
- Existing Swerve, autonomous, Vision, and fusion capabilities remain protected.

## Future-Change Policy

Formal architecture review and an ADR amendment or successor ADR are required
before changing the M00 module boundary, lesson identity/order, predecessor
rule, mechanism ownership, shooter ownership, RobotContainer role, Observation
flow, hardware-evidence policy, protected-system boundary, or activation gates.

Any future real hardware selection must document the evidence, exact adapter,
vendor/library compatibility, and bounded verification scope. It must not be
introduced by familiarity or copied historical constants.

## Review Result

This ADR is the M00 roadmap authority. Its roadmap remains `APPROVED / ROADMAP
AUTHORIZED`. M00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED` at primary publication `83907ab` and metadata publication `f523118`.
M00_L02 is `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, Design
Lock `PASS_M00_L02_FINAL_DESIGN_LOCK`, documentation implementation complete,
independent rereview PASS, final User build PASS, final closure review PASS,
documentation reconciliation PASS, independent reconciliation PASS, and freeze
authorization `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`. Production-code,
test, configuration, vendordep, deploy, runtime-behavior, and mechanism-API
authorization remain `NONE`. The primary publication is complete at
`65a92a4a5806fd5134e0114e851c4e4cc093c58e`, publication metadata reconciliation
is complete, and the separate metadata Git publication and final publication
completion remain pending. M00_L03 is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED` with freeze state `FROZEN`. Its bounded implementation,
focused retest, full clean regression, bounded Simulation, independent
implementation review, bilingual student documentation, bounded Step 16
terminology repair, independent documentation rereview, final User closure
build, final closure review, documentation/lifecycle reconciliation, and
independent reconciliation review are accepted. Architect freeze authorization
is recorded at `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`. Primary Git
publication is complete at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`,
metadata publication is complete at
`b2464f66da42a6281acb7bfc709f2a3b83296505`, and final publication
verification is PASS. M00_L04 completed its authorized `RunIntakeCommand`,
Xbox Right Bumper `whileTrue` binding, and focused lifecycle/ownership test
scope. Focused tests, full regression, bounded Simulation, bounded Driver
Station verification, independent implementation review, bilingual
documentation review and repair, final closure build, transition
reconciliation, independent confirmation, and resumed final closure review
are accepted. M00_L04 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED`; its primary publication is `5c86be3`, metadata publication is
`24738e6`, and final remote alignment and publication verification are `PASS`.
M00_L05 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` with implementation, verification,
documentation, independent documentation rereview, final closure build, and
final closure review complete. Primary publication is complete at `5709f1d`,
metadata publication is complete at `1d6fade`, and final publication
verification is PASS. M00_L06 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED`; its primary publication remains
`f102a5e662877f8cb49eb63f2cfd888ac356bea4`. M00_L07 is `COMPLETE / FROZEN /
READ-ONLY`, active lesson count is `0`, and no M00 lesson is active.
Preparation, inherited baseline,
Architecture / Inheritance Audit, Final Design Lock, Controlled Activation,
governance adjudication, independent activation review, bounded implementation,
final static rereview, six-class focused tests, clean full regression, bounded
Simulation, and documentation reconciliation are accepted. Evidence is `THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`. The invalid Noop
`velocityRpm = 0.0` observation is not physical zero-speed evidence. Independent
The Independent Closure Review passed at
`PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` with no remaining findings, and the
authorized controlled freeze transition is complete. Accepted primary
publication is complete at `50e5f440bb0c9d96bdcd57eed533651d8d59ca93`,
primary push and remote alignment are `PASS`, and publication metadata
reconciliation is complete/prepared for User commit. The metadata commit and
push remain pending User action, and final publication verification remains
pending; final `PUBLISHED / VERIFIED` status is not claimed. M00_L08 remains
inactive/uncreated. Roadmap approval remains recorded by
`PASS_M00_ROADMAP_ADR_ARCHITECT_APPROVED`; preparation authorization remains
recorded by `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`. This reconciliation
does not change the roadmap or alter any frozen predecessor.

## M00_L07 Primary Publication and Metadata Reconciliation — 2026-09-20

The frozen M00_L07 lesson-local snapshot remains unchanged under the resolved
historical-snapshot publication model. Accepted gate
`PASS_M00_L07_PRIMARY_PUBLICATION_EVIDENCE` records the User-owned primary
publication commit `50e5f440bb0c9d96bdcd57eed533651d8d59ca93`, subject
`Complete M00_L07 Flywheel foundation`, primary push `PASS`, and primary remote
alignment `PASS` with `HEAD = origin/main =
50e5f440bb0c9d96bdcd57eed533651d8d59ca93`.

M00_L07 remains `COMPLETE / FROZEN / READ-ONLY`. Publication metadata
reconciliation is complete/prepared for User commit. The metadata commit and
push remain `PENDING USER ACTION`; final publication verification remains
`PENDING`; final `PUBLISHED / VERIFIED` status is not claimed. Active lesson
count remains `0`, the current active M00 lesson remains `NONE`, and M00_L08
remains `INACTIVE / NOT CREATED`.

## Revision History

| Version | Date | Status | Notes |
| --- | --- | --- | --- |
| 1.0 | 2026-09-13 | PROPOSED | Created from the Architect-accepted compact reuse roadmap; future authorization only. |
| 1.1 | 2026-09-13 | APPROVED | Architect approval recorded at `PASS_M00_ROADMAP_ADR_ARCHITECT_APPROVED`; roadmap authorized but not active. |
| 1.2 | 2026-09-15 | APPROVED | Recorded V00 final closure at implementation `6548c98` and metadata publication `5d36529`; recorded `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, exact M00_L01 identity, predecessor, future destination, User-owned baseline sequence, bilingual/evidence rules, and active lesson count `0`; M00 runtime remains `NOT ACTIVE`, M00_L01 remains `NOT YET CREATED`, and implementation is not authorized. |
| 1.3 | 2026-09-15 | APPROVED | Consumed `PASS_M00_L01_FINAL_DESIGN_LOCK` and recorded the documentation-only controlled activation of M00_L01 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; implementation and production-code authorization remain `NONE`. |
| 1.4 | 2026-09-15 | APPROVED | Reconciled paired learning documentation, the preserved Constants-authority HOLD and repair, independent rereview PASS, User final inherited clean build/regression PASS, final closure review and Architect acceptance, and technical/content readiness PASS while retaining `IN_PROGRESS / EDITABLE`, active lesson count `1`, pending freeze authorization, and pending User Git publication. |
| 1.5 | 2026-09-15 | APPROVED | Consumed `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`; recorded M00_L01 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserved the final technical boundary and locked 16-lesson roadmap, left M00_L02 inactive and uncreated, and retained User Git publication and remote verification as pending. |
| 1.6 | 2026-09-15 | APPROVED | Reconciled User-verified M00_L01 lesson publication at `83907ab` and `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`; recorded `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, preserved active lesson count `0` and inactive/uncreated M00_L02, and left the distinct publication-metadata commit, push, and final remote verification pending. |
| 1.7 | 2026-09-16 | APPROVED | Consumed `PASS_M00_L02_FINAL_DESIGN_LOCK`; recorded M00_L02 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserved M00_L01 frozen publication and the locked 16-lesson roadmap; retained all technical implementation authorization as `NONE`; and left student documentation pending separate Architect authorization. |
| 1.8 | 2026-09-16 | APPROVED | Reconciled documentation authorization and implementation, preserved the initial independent-review HOLD and bounded repair, recorded independent rereview PASS, User final build/regression PASS, final closure review PASS, and `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`; retained M00_L02 as `IN_PROGRESS / EDITABLE` with active lesson count `1`, pending freeze authorization and publication, and left M00_L03 inactive/uncreated. |
| 1.9 | 2026-09-16 | APPROVED | Consumed `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION` after independent reconciliation PASS; recorded M00_L02 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserved all technical authorization as `NONE`, kept M00_L03 inactive/uncreated, and retained User Git publication and publication metadata reconciliation as pending. |
| 1.10 | 2026-09-16 | APPROVED | Reconciled User-confirmed M00_L02 primary publication at `65a92a4a5806fd5134e0114e851c4e4cc093c58e` with primary push PASS; recorded publication metadata reconciliation complete while leaving the separate metadata Git publication and final publication completion pending; preserved the 16-lesson roadmap and inactive/uncreated M00_L03. |
| 1.11 | 2026-09-16 | APPROVED | Consumed `PASS_M00_L03_FINAL_DESIGN_LOCK`; recorded the prepared M00_L03 candidate as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, preserved the script-check-defect history and exact inheritance evidence, retained all implementation authorization as separately pending, and left M00_L04 inactive/uncreated. |
| 1.12 | 2026-09-16 | APPROVED | Reconciled M00_L03 implementation authorization and completion, preserved the initial focused-test HOLD and minimal test-only repair, recorded focused retest PASS, full clean regression PASS, bounded Simulation PASS, independent implementation review PASS, and authorized bilingual student-documentation implementation; retained M00_L03 as `IN_PROGRESS / EDITABLE` with active lesson count `1`, independent documentation review and closure/freeze/publication pending, and M00_L04 inactive/uncreated. |
| 1.13 | 2026-09-17 | APPROVED | Recorded the Step 16 mutable-IOInputs documentation HOLD and bounded one-line repair, independent documentation rereview PASS, final User closure build PASS, final closure review PASS, and completed lifecycle reconciliation; retained M00_L03 as `IN_PROGRESS / EDITABLE`, active lesson count `1`, freeze authorization and publication pending, and M00_L04 inactive/uncreated. |
| 1.14 | 2026-09-17 | APPROVED | Consumed `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION` after independent reconciliation review PASS; recorded M00_L03 as `COMPLETE / FROZEN / READ-ONLY` with freeze state `FROZEN` and active lesson count `0`; preserved accepted technical/documentation evidence, M00_L02 protection, the 16-lesson roadmap, and inactive/uncreated M00_L04; left User-owned publication pending. |
| 1.15 | 2026-09-17 | APPROVED | Reconciled accepted M00_L03 primary publication at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, primary push PASS, and remote alignment PASS; recorded publication metadata reconciliation complete while leaving metadata Git publication and final publication verification pending; preserved frozen M00_L03, inactive/uncreated M00_L04, and the locked 16-lesson roadmap. |
| 1.16 | 2026-09-18 | APPROVED | Recorded M00_L03 final publication at primary commit `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3` and metadata commit `b2464f66da42a6281acb7bfc709f2a3b83296505`; consumed `PASS_M00_L04_FINAL_DESIGN_LOCK`; activated M00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, locked `RunIntakeCommand` plus Right Bumper `whileTrue` scope, implementation pending authorization, and M00_L05 inactive/uncreated. |
| 1.17 | 2026-09-18 | APPROVED | Reconciled completed M00_L04 implementation, 14/14 focused tests, full clean regression, bounded Simulation and Driver Station verification, independent implementation review PASS, and paired student-guide creation; retained `IN_PROGRESS / EDITABLE`, active lesson count `1`, pending independent documentation review and all closure/freeze/publication gates, and inactive/uncreated M00_L05. |
| 1.18 | 2026-09-18 | APPROVED | Consumed `PASS_M00_L04_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_FINAL_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`; preserved the resolved documentation and transition-history HOLDs, recorded M00_L04 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, retained bounded evidence and deferred real hardware, kept M00_L05 inactive/uncreated, and left User-owned Git publication pending. |
| 1.19 | 2026-09-18 | APPROVED | Reconciled accepted M00_L04 primary publication at `5c86be3` and primary remote alignment PASS; recorded publication metadata reconciliation complete in the working tree while leaving metadata Git publication and final publication verification pending; preserved the locked 16-lesson roadmap and inactive/uncreated M00_L05. |
| 1.20 | 2026-09-18 | APPROVED | Recorded M00_L04 final publication at primary `5c86be3` and metadata `24738e6`; consumed `PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`; activated M00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, FeederIONoop-only locked foundation scope, implementation authorization pending, real hardware deferred, and M00_L06 inactive/uncreated. |
| 1.21 | 2026-09-19 | APPROVED | Reconciled the authorized M00_L05 implementation, both test-only repairs, focused tests PASS, 682-test full regression PASS, bounded Simulation and HALSIM Driver Station evidence, independent implementation review PASS, and paired student-guide implementation; retained M00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson pending independent documentation review and later closure/freeze/publication, with M00_L06 inactive/uncreated. |
| 1.22 | 2026-09-19 | APPROVED | Consumed the accepted M00_L05 final closure gate after bounded documentation repair, independent documentation rereview PASS, and final closure build PASS; recorded M00_L05 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, kept M00_L06 inactive/uncreated, preserved the locked M00_L01-L16 roadmap and deferred real hardware, and left every User-owned publication stage pending. |
| 1.23 | 2026-09-19 | APPROVED | Reconciled User-owned M00_L05 primary publication at `5709f1d74b3318303bcc56779315b243dd81770b` with primary remote alignment PASS; recorded publication metadata reconciliation complete while leaving metadata publication and final publication verification pending; preserved the M00_L01-L16 roadmap, active lesson count `0`, and inactive/uncreated M00_L06. |
| 1.24 | 2026-09-19 | APPROVED | Recorded final M00_L05 metadata publication at `1d6fadeec57fbfd3be245746b21d06e58b79518f` and `PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`; consumed `PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`; activated M00_L06 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, locked `RunFeederCommand` plus Left Bumper `whileTrue` scope, implementation pending separate authorization, `FeederIONoop` retained, real hardware deferred, and M00_L14-L16 protected. |
| 1.25 | 2026-09-19 | APPROVED | Reconciled the separately authorized M00_L06 implementation, Independent Static Rereview PASS, focused tests PASS, full clean regression PASS, bounded Simulation PASS, completed documentation phase, Independent Closure Review HOLD, and the exact three-item bounded documentation/lifecycle repair; retained M00_L06 as the sole `IN_PROGRESS / ACTIVE / EDITABLE` lesson pending independent closure rereview, with freeze and publication unclaimed. |
| 1.26 | 2026-09-20 | APPROVED | Preserved both M00_L06 closure-HOLD and bounded-repair histories; consumed `PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and `AUTHORIZED_FOR_FREEZE` after final verdict `READY_FOR_FREEZE` with no remaining findings; recorded M00_L06 as `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, no active M00 lesson, publication pending, and M00_L07 inactive/uncreated. |
| 1.27 | 2026-09-20 | APPROVED | Reconciled accepted M00_L06 primary publication at `f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject `Complete M00_L06 Feeder command ownership`, primary push PASS, and remote alignment PASS; recorded publication metadata reconciliation complete/prepared for User commit while leaving metadata Git publication and final publication verification pending; preserved the locked roadmap and inactive/uncreated M00_L07. |
| 1.28 | 2026-09-20 | APPROVED | Consumed `PASS_M00_L07_FINAL_DESIGN_LOCK` after accepted M00_L06 final publication, M00_L07 preparation, 306-of-306 byte-identical inheritance PASS, and inherited baseline build PASS; activated M00_L07 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, implementation pending separate authorization, `FlywheelIONoop`-only future runtime, CAN 50-54 planning-only, deferred real hardware, and inactive/uncreated M00_L08. |
| 1.29 | 2026-09-20 | APPROVED | Reconciled accepted M00_L07 governance adjudication and activation review, completed bounded implementation, preserved the initial static HOLD and two-stage test-only repair history, final static rereview PASS, User focused-test PASS, clean full-regression PASS, bounded Simulation PASS, and documentation completion; retained M00_L07 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson, classified evidence as `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`, set Independent Closure Review as the next gate, and left freeze, publication, and M00_L08 activation unclaimed. |
| 1.30 | 2026-09-20 | APPROVED | Consumed `PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` after `CLOSURE_REVIEW_PASS`, `READY_FOR_FREEZE_AUTHORIZATION`, and no remaining findings; completed the controlled transition to `COMPLETE / FROZEN / READ-ONLY`, set active lesson count to `0` with no active M00 lesson, preserved `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`, left publication pending/not yet published, and kept M00_L08 inactive/uncreated. |
| 1.31 | 2026-09-20 | APPROVED | Reconciled accepted M00_L07 primary publication at `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` with subject `Complete M00_L07 Flywheel foundation`, primary push PASS, and remote alignment PASS; recorded publication metadata reconciliation complete/prepared for User commit while leaving the metadata commit/push and final publication verification pending; preserved the frozen lesson-local snapshot, active lesson count `0`, no active M00 lesson, the locked M00_L01-L16 roadmap, and inactive/uncreated M00_L08. |
| 1.73 | 2026-09-23 | APPROVED | Record documentation-only M00_L13 Controlled Activation after accepted Architecture / Inheritance Audit and Final Design Lock; preserve M00_L12 publication and freeze, make M00_L13 the sole active lesson, keep implementation unauthorized, and leave M00_L14 inactive/uncreated. |
| 1.74 | 2026-09-24 | APPROVED | Reconciled M00_L13 implementation and accepted static review, focused/clean-regression tests, bounded Noop Simulation, and documentation; kept M00_L13 active with Independent Closure Review next, freeze/publication pending, and M00_L14 inactive/uncreated. |
| 1.75 | 2026-09-24 | APPROVED | Reconciled PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW and Freeze Reconciliation; preserved the closure HOLD and documentation repair, recorded M00_L13 as COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED with active lesson count 0 and no active M00 lesson, and left Independent Freeze Review and User-owned publication pending. |
| 1.76 | 2026-09-24 | APPROVED | Reconciled PASS_M00_L13_INDEPENDENT_FREEZE_REVIEW and User-owned primary snapshot commit `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704`; retained both safe publication-script HOLDs as script defects; prepared metadata publication for User commit while leaving metadata SHA, push, and final verification pending; preserved frozen M00_L13, active lesson count 0, and inactive/uncreated M00_L14. |
| 1.77 | 2026-09-25 | APPROVED | Reconciled the bounded M00_L13 final-publication metadata documentation repair after `HOLD_M00_L13_FINAL_PUBLICATION_VERIFICATION_STALE_CURRENT_PUBLICATION_STATE`; record COMPLETE / FROZEN / READ-ONLY / PUBLISHED with external final verification pending, keep the metadata SHA external and the two-commit model unchanged, and preserve active lesson count 0 and inactive/uncreated M00_L14. |
---

## M00_L08 Controlled Activation — 2026-09-20

The accepted M00_L08 preparation baseline, Architecture / Inheritance Audit,
and Final Design Lock are consumed. The User prepared the candidate from
canonical M00_L07 through copy/rename and generated-artifact cleanup. The
untouched-copy baseline passed; inheritance remains 103/103 production and
96/96 test files byte-identical, with 711/711 overall non-generated files
identical.

M00_L08 is activated as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL
DESIGN LOCK` lesson with active lesson count `1` and current active lesson
`M00_L08`. The one new concept is vendor-neutral Flywheel closed-loop velocity
control through one validated semantic RPM request while preserving
measurement-only Observation and explicit safe stop.

The accepted design supersedes `requestSpin()` with
`void requestVelocity(double targetRpm)` in finite, nonnegative Flywheel
mechanism RPM. Exactly zero is canonical safe stop; invalid values fail closed;
requested states are `STOPPED` and `VELOCITY_REQUESTED`; Observation remains
measurement-only; and runtime remains `FlywheelIONoop` only. No physical
adapter, hardware configuration, gain, target RPM, or CAN assignment is
authorized. M00_L09 Ready-at-Speed and later command, coordination, shooting,
and autonomous mechanism scope remain protected.

The Architect Simulation clarification is part of this activation: focused
unit tests and test doubles are not runtime Simulation evidence. Future bounded
runtime Simulation may claim only deterministic Noop composition,
measurement/telemetry state, STOPPED idle behavior, no automatic Teleop
request, and Disabled → Teleop → Disabled persistence. Physical regulation,
convergence, tuning, sensor fidelity, CAN, RPM accuracy, and physical stop
behavior remain unverified; real hardware remains deferred.

Implementation is `PENDING SEPARATE AUTHORIZATION`; verification, closure,
freeze, and publication are pending. This activation changes lifecycle and
documentation only and claims no production code, tests, Simulation results,
Git hashes, closure, freeze, or publication. M00_L07 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; M00_L09 remains
`INACTIVE / NOT CREATED`.

## M00_L08 Controlled Activation Documentation Repair — 2026-09-20

The Independent Activation Review HOLD identified documentation completeness
only. This repair does not authorize implementation or verification.

The future FlywheelIO contract is exactly:

- `FlywheelIOInputs`: `boolean available`, `boolean connected`,
  `boolean velocityValid`, `double velocityRpm`;
- `void updateInputs(FlywheelIOInputs inputs)`;
- `void requestVelocity(double targetRpm)`; and
- `void stop()`.

`requestSpin()` is `REMOVED / SUPERSEDED`. Vendor types, vendor control
objects, gain parameters, and hardware-configuration parameters are excluded.

Positive valid-request ordering is validate, record target, set
`VELOCITY_REQUESTED`, replace immutable Observation, and forward exactly one
IO request. Forwarding exceptions preserve target/state/Observation, propagate,
do not roll back, and are not retried by `periodic()`. Zero records zero and
`STOPPED`, replaces Observation, and forwards exactly one stop. Invalid NaN,
positive infinity, negative infinity, and negative finite values record zero/
`STOPPED`, replace Observation, attempt stop, and throw
`IllegalArgumentException`; stop failure is suppressed and no invalid value is
forwarded. Explicit stop is unconditional and preserves zero/STOPPED/Observation
if IO throws, with no rollback, automatic restart, or periodic reissue.

`FlywheelIONoop` deterministically writes `available=false`, `connected=false`,
`velocityValid=false`, and `velocityRpm=0.0`; request and stop are safe no-ops,
with no convergence or physical model. Zero RPM while invalid is not measured
physical zero RPM. `RobotContainer` remains unchanged and composes exactly
`new FlywheelSubsystem(new FlywheelIONoop())`; no Flywheel command, binding,
default command, direct request/stop, autonomous registration, NamedCommands,
event markers, Feeder/Flywheel coordination, hardware selection, or
Flywheel-specific `RobotBase.isReal()` branch is permitted.

Future test reconciliation is limited to the six existing files named in the
M00_L08 lesson records; no new or unrelated test file is authorized.

## M00_L08 Post-Verification Documentation Reconciliation — 2026-09-20

This reconciliation consumes the accepted implementation, final static
review, focused-test, clean-regression, and bounded-Simulation gates. It is a
documentation record only. It does not consume Independent Closure Review,
freeze, publication, or M00_L09 activation authority.

### Final contract and implementation boundary

M00_L08 implements exactly one new concept: vendor-neutral Flywheel
closed-loop velocity control through one validated semantic mechanism-RPM
request while preserving measurement-only Observation and explicit safe stop.
The final method is `void requestVelocity(double targetRpm)`;
`requestSpin()` is `REMOVED / SUPERSEDED`; requested states are exactly
`STOPPED` and `VELOCITY_REQUESTED`.

`FlywheelIOInputs` contains exactly `boolean available`, `boolean connected`,
`boolean velocityValid`, and `double velocityRpm`. The exact methods are
`updateInputs(FlywheelIOInputs)`, `requestVelocity(double)`, and `stop()`.
There are no vendor types, gains, vendor control object, or hardware-specific
configuration parameters. Finite nonnegative mechanism RPM is valid. A
positive finite request records target, enters `VELOCITY_REQUESTED`, updates
immutable Observation, and forwards one IO request. `+0.0` and `-0.0` are
canonical safe stop. NaN, positive infinity, negative infinity, and negative
finite values record zero/`STOPPED`, update Observation, attempt stop, throw
`IllegalArgumentException`, suppress stop failure on that primary exception,
and never forward an invalid target. Explicit stop records zero/`STOPPED` and
updated Observation before unconditional IO stop; IO failure preserves those
software facts and propagates without rollback or restart. `periodic()` only
updates inputs and rebuilds Observation. Target RPM remains subsystem intent,
not Observation or telemetry. `FlywheelIONoop` reports
false/false/false/0.0 and has deterministic no-op request/stop behavior.
CAN 50–54 is planning reservation only; real hardware is deferred.

### Provenance and verification evidence

Compared with frozen M00_L07, production is `103 / 99 / 4 / 0 / 0` for
Compared / Byte-identical / Changed / Missing / Added. The four changed files
are `src/main/java/frc/robot/io/flywheel/FlywheelIO.java`,
`src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`,
`src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`, and
`src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`. Tests are
`96 / 90 / 6 / 0 / 0` with changed files exactly
`src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`,
`src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`,
`src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`,
`src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`,
`src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`,
and `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`.
No production or test file was added. Constants, RobotContainer, telemetry,
Gradle, vendordeps, deploy, and frozen M00_L07 remain unchanged.

The initial Independent Static Review was `HOLD` for stale stop target state,
insufficient Noop post-request assertions, missing explicit negative-zero
coverage, and brittle regex comment stripping. The bounded repair is
`COMPLETE`; the final Independent Static Re-review is `PASS` under
`PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.

User focused-test evidence is `BUILD SUCCESSFUL in 16s`, `4 actionable tasks:
3 executed, 1 up-to-date`, `FOCUSED TESTS: PASS` under
`PASS_M00_L08_USER_FOCUSED_TESTS`. Clean regression evidence is
`BUILD SUCCESSFUL in 26s`, `7 actionable tasks: 7 executed`,
`CLEAN REGRESSION: PASS` under `PASS_M00_L08_CLEAN_FULL_REGRESSION`.

Bounded WPILib Simulation is accepted under `PASS_M00_L08_BOUNDED_SIMULATION`:

1. Disabled: Available=false, Connected=false, RequestedState=STOPPED,
   VelocityRpm=0.0, VelocityValid=false
   (`PASS_M00_L08_SIMULATION_CHECKPOINT_1_DISABLED`).
2. Teleoperated enabled with no driver action: Robot Enabled=Yes and the same
   Flywheel values (`PASS_M00_L08_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`).
3. Return Disabled: FMS Robot Enabled=No and the same Flywheel values
   (`PASS_M00_L08_SIMULATION_CHECKPOINT_3_DISABLED`).

`VelocityRpm=0.0` while `VelocityValid=false` is canonical invalid-Noop state,
not measured physical zero RPM. Simulation verifies only Noop composition,
telemetry/state presence, deterministic unavailable/disconnected/invalid
measurement, STOPPED idle behavior, no automatic Teleop request, and mode
persistence. It does not verify physical control, convergence, PID/PIDF,
feedforward, sensor fidelity, RPM accuracy, direction, CAN, physical stop, or
runtime exercise of `requestVelocity()`; **requestVelocity runtime exercise
was NOT claimed**. Request semantics were verified by focused/unit tests.
The exact documentation statement is: `requestVelocity runtime exercise was NOT claimed`.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
and `REAL HARDWARE DEFERRED`.

### Lifecycle decision after reconciliation

M00_L08 remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`,
with active lesson count `1` and current active M00 lesson `M00_L08`.
Implementation, final static re-review, focused tests, clean regression, and
bounded Simulation are `COMPLETE/PASS` as recorded above. Independent Closure
Review is `PENDING`; Freeze and Publication are `NOT AUTHORIZED`.
M00_L09 Flywheel Ready-at-Speed remains `INACTIVE / NOT CREATED`, and no
readiness policy, command ownership, shooting, feeder coordination, automatic
firing, event-marker, or autonomous mechanism integration is included.

## M00_L08 Controlled Freeze Transition — 2026-09-20

The accepted documentation reconciliation, `PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW`
with verdict `READY_FOR_FREEZE_AUTHORIZATION`, and Architect decision
`FREEZE AUTHORIZED` are consumed. This is a controlled lifecycle transition
only. No production source, tests, configuration, dependencies, deployment
content, or verification evidence changed.

M00_L08 is transitioned to:

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
INDEPENDENT CLOSURE REVIEW: PASS
FREEZE: COMPLETE / FROZEN / READ-ONLY
PUBLICATION: PENDING / NOT YET PUBLISHED
FINAL PUBLICATION VERIFICATION: NOT YET PERFORMED
M00_L09: INACTIVE / NOT CREATED
```

All accepted technical evidence remains preserved: the final single Flywheel
closed-loop velocity concept, exact vendor-neutral IO contract, validation and
safe-stop semantics, immutable Observation, output-free `periodic()`,
`FlywheelIONoop`-only runtime, read-only telemetry, unchanged RobotContainer,
production integrity `103 / 99 / 4 / 0 / 0`, test integrity `96 / 90 / 6 / 0 /
0`, final static re-review PASS, focused tests PASS, clean regression PASS,
and bounded Simulation PASS. Evidence classification remains exactly
`THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.
Simulation remains bounded Noop/lifecycle evidence and does not claim runtime
`requestVelocity` exercise or physical Flywheel behavior.

M00_L07 remains the frozen published predecessor with accepted primary
publication `50e5f440bb0c9d96bdcd57eed533651d8d59ca93`, metadata publication
`62199c3ecd1ac7940e188dbef3d28de784c8da2c`, and final
`PUBLICATION_VERIFIED`. This transition does not modify M00_L07, publish
M00_L08, perform final publication verification, or activate M00_L09. The
locked M00_L01–M00_L16 roadmap remains unchanged and contains no M00_L17.

## M00_L08 Primary Publication and Metadata Reconciliation — 2026-09-20

The accepted gate `PASS_M00_L08_PRIMARY_PUBLICATION` records the User-owned
primary publication after the completed freeze transition. M00_L08 remains
`COMPLETE / FROZEN / READ-ONLY`. The primary publication commit is
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` with subject
`Complete M00_L08 Flywheel closed-loop velocity`; primary push is `PASS`, and
local HEAD and `origin/main` both equal that SHA, giving primary remote
alignment `PASS`.

Publication metadata reconciliation is the current bounded stage and remains
`PENDING METADATA COMMIT`. No metadata commit SHA, metadata push, or final
`PUBLICATION_VERIFIED` verdict is recorded. Final independent publication
verification remains `PENDING / NOT YET PERFORMED`. Active lesson count remains
`0`, current active M00 lesson remains `NONE`, and M00_L09 remains
`INACTIVE / NOT CREATED`.

The frozen lesson-local snapshot and technical contracts remain unchanged. This
follows the Historical Snapshot Model used by M00_L05, M00_L06, and M00_L07:
the frozen snapshot, metadata reconciliation commit, and later independent
verification are distinct stages, with no third commit required merely to
record final verification. Evidence remains exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`; runtime
`requestVelocity` Simulation exercise was NOT claimed.

## M00_L09 Controlled Activation — 2026-09-21

The canonical M00_L08 predecessor is consumed as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. M00_L09 preparation
passed `PASS_M00_L09_PREPARATION_BASELINE` with `BUILD SUCCESSFUL in 34s` and
six actionable tasks, all six executed. The architecture/inheritance gate
`PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT` and Final Design Lock
`PASS_M00_L09_FINAL_DESIGN_LOCK` are accepted with verdict
`READY_FOR_CONTROLLED_ACTIVATION`.

This decision authorizes documentation-only Controlled Activation of M00_L09
as the sole active editable lesson. Its exact one concept is vendor-neutral
instantaneous Flywheel Ready-at-Speed classification, owned by
`FlywheelSubsystem` through one private deterministic side-effect-free helper.
The immutable Observation adds exactly `readyAtSpeed`; telemetry adds exactly
`ReadyAtSpeed`. The predicate requires positive finite velocity intent,
available/connected/velocity-valid input, finite measured velocity, and
inclusive symmetric error within
`Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm = 50.0` mechanism RPM.
That value is a provisional software-policy acceptance tolerance, not
hardware-tuned and not real-robot validated. Dwell, debounce, hysteresis,
history, readiness state machines, commands, coordination, autonomous action,
and physical implementation are excluded.

The activation write boundary is limited to the root governance records, the
four M00_L09 lesson records, and the new
`M00_L08_to_M00_L09_Step_by_Step.md` transition guide. No production source,
test source, Constants implementation, IO/Noop, RobotTelemetry,
RobotContainer, configuration, deployment, dependency, or frozen M00_L08 file
may change. M00_L09 is `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK`, Active Lesson Count is `1`, Current Active M00 Lesson is `M00_L09`,
implementation is `NOT STARTED`, and Independent Activation Review is
`PENDING`. M00_L10 is `INACTIVE / NOT CREATED`. Freeze and publication are not
claimed.

## M00_L09 activation documentation repair — 2026-09-21

This bounded repair reconciles lifecycle documentation only. The Architect
acceptance gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`; the engineer-owned
Controlled Activation verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
The current independent review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW`.

The canonical external M00_L08 state is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, primary
`5daecd970ff95fb906d6de9d5bc22a5cb094877d`, metadata
`a76dc33c2b485b4988e7058cbfed0fa3362cc560` with parent equal to the primary,
final remote-aligned HEAD equal to the metadata SHA, and verdict
`PUBLICATION_VERIFIED`. No third publication commit was required. Historical
M00_L08 snapshots may retain publication-pending wording under the passing
two-commit Historical Snapshot Model; no frozen M00_L08 record is changed.

The M00_L09 Final Design Lock is unchanged: one deterministic instantaneous
Ready-at-Speed concept, the inclusive symmetric comparison and provisional
50.0 RPM policy tolerance, immutable Observation and read-only telemetry only,
no automatic action, and no expansion of the production/test boundaries or
roadmap. The future evidence plan is `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`; current verification is PENDING. M00_L09 remains the
sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with
implementation `NOT STARTED`, and M00_L10 remains inactive/not created.

## M00_L09 Implementation, Verification, and Documentation Reconciliation — 2026-09-21

This governance record reconciles the later implementation and user-supplied
verification evidence without changing the M00 roadmap or the M00_L09 Design
Lock. Exactly one concept was implemented: instantaneous vendor-neutral
Flywheel Ready-at-Speed classification using the positive finite requested
target, valid connected finite measurement, and inclusive symmetric tolerance
comparison. The 50 RPM tolerance remains provisional software policy only.

`PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED` is accepted. The initial
Independent Static Review `HOLD` is retained as chronology; its bounded repair
passed and the final independent static re-review passed. Production integrity
is `103 / 99 / 4 / 0 / 0`; test integrity is `96 / 92 / 4 / 0 / 0`; and
deploy/configuration integrity is `4 / 4 / 0 / 0 / 0`. The exact authorized
production/test boundaries were respected, with no new files, unrelated
runtime changes, or frozen M00_L08 modifications.

User evidence accepts `PASS_M00_L09_USER_FOCUSED_TESTS` (`BUILD SUCCESSFUL in
22s`; four actionable tasks, three executed and one up-to-date) and
`PASS_M00_L09_CLEAN_FULL_REGRESSION` (`BUILD SUCCESSFUL`; seven actionable
tasks, all seven executed). User Simulation evidence accepts
`PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`, and
`PASS_M00_L09_BOUNDED_SIMULATION`. Each checkpoint retained
`Available=false`, `Connected=false`, `ReadyAtSpeed=false`,
`RequestedState=STOPPED`, `VelocityRpm=0.0`, and `VelocityValid=false`.
The bounded interpretation covers Noop composition, read-only telemetry,
fail-safe idle, no automatic request, no readiness-triggered actuation, and
Disabled→Teleop→Disabled persistence only. It does not claim runtime
50-RPM boundaries, physical convergence, sensor fidelity, CAN, or hardware.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`. Hardware readiness and CAN 50–54 remain unknown or
deferred; CAN 50–54 is planning only. M00_L09 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`; implementation,
static review, focused tests, clean regression, bounded Simulation, and
Documentation Reconciliation are complete. Independent Closure Review remains
pending, Freeze and Publication are not authorized, Active Lesson Count is
`1`, current active lesson is M00_L09, and M00_L10 remains inactive/not
created.

## M00_L09 Controlled Freeze Transition — 2026-09-21

The final independent closure re-review returned
`READY_FOR_FREEZE_AUTHORIZATION` under
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`; Architect freeze authorization
is recorded as accepted. M00_L09 transitions to `COMPLETE / FROZEN /
READ-ONLY` without changing the Final Design Lock, Frozen Backbone, or roadmap.

The exact technical contract remains frozen: vendor-neutral instantaneous
Ready-at-Speed classification, subsystem-owned immutable Observation semantics,
inclusive symmetric 50.0 RPM provisional software-policy tolerance, unchanged
IO/Noop/RobotTelemetry/RobotContainer boundaries, and no automatic mechanism
action. Integrity remains production `103 / 99 / 4 / 0 / 0`, tests
`96 / 92 / 4 / 0 / 0`, and full deploy/config/support `24 / 24 / 0 / 0 / 0`.

Evidence remains `THEORY VERIFIED`, `SIMULATION VERIFIED`, and
`REAL HARDWARE DEFERRED`. Independent Freeze Review is `PENDING`; Publication
is `PENDING / NOT YET PUBLISHED`; no publication SHA or Git event is claimed.
Active Lesson Count is `0`, Current Active M00 Lesson is `NONE`, and M00_L10
remains `INACTIVE / NOT CREATED`. The controlled freeze record stops before
Independent Freeze Review, publication, and successor activation.

## M00_L09 Primary Publication and Metadata Reconciliation — 2026-09-21

The accepted Independent Freeze Review gate is
`PASS_M00_L09_INDEPENDENT_FREEZE_REVIEW`. M00_L09 remains
`COMPLETE / FROZEN / READ-ONLY`, and the frozen lesson-local snapshot is
unchanged. The accepted primary snapshot gate is
`PASS_M00_L09_PRIMARY_FROZEN_SNAPSHOT_COMMIT`.

Primary publication identity:

- SHA: `3c822a1e3956850c9d0ba9954c5b163d83b801b9`
- Subject: `Complete M00_L09 Flywheel ready-at-speed`

The primary snapshot commit exists locally and has not been pushed. Therefore
primary remote alignment, origin state, and remote publication verification are
not claimed. The canonical publication phase remains
`PENDING METADATA COMMIT`. No metadata commit SHA is available, no metadata
push has occurred, and no `PUBLICATION_VERIFIED` verdict is recorded.

This preserves the M00_L08 two-commit Historical Snapshot model: frozen lesson
snapshot primary commit, repository-level metadata commit, and later external
final publication verification. No third commit is required merely to record
final verification. Frozen lesson-local historical pending wording remains
unchanged.

The final lifecycle is `COMPLETE / FROZEN / READ-ONLY`. Evidence remains
exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.
Integrity remains production `103 / 99 / 4 / 0 / 0`, tests `96 / 92 / 4 / 0 / 0`,
and full deploy/config/support `24 / 24 / 0 / 0 / 0`. Active Lesson Count is
`0`, Current Active M00 Lesson is `NONE`, and M00_L10 is
`INACTIVE / NOT CREATED`. The locked M00_L01–M00_L16 roadmap is unchanged.

## M00_L10 Controlled Activation — 2026-09-21

M00_L09 is the accepted predecessor in state
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. Its accepted primary
publication SHA is `3c822a1e3956850c9d0ba9954c5b163d83b801b9`, its metadata SHA
is `249100db23262430ce2557eaa5e67d70b7b0a79c`, and its final verdict is
`PUBLICATION_VERIFIED`.

M00_L10 is activated as the sole active lesson:
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, implementation
`NOT STARTED`, Independent Activation Review `PENDING`, Active Lesson Count
`1`, Current Active M00 Lesson `M00_L10`, Freeze and Publication not authorized,
and M00_L11 inactive/not created. The accepted preparation baseline,
architecture/inheritance audit, and final design lock report zero non-generated
drift from M00_L09.

The locked concept is an independently owned, vendor-neutral Elevator position
observation with explicit validity and reference-trust semantics in meters. It
does not own position control, homing, or travel-limit enforcement. The exact
normalization, IO/Observation/subsystem/telemetry contracts, composition,
future tests, simulation boundary, and evidence plan are recorded in the
lesson-local `M00_L09_to_M00_L10_Step_by_Step.md` guide. This activation changes
documentation/lifecycle identity only and leaves the Frozen Backbone, M00
roadmap, M00_L09 snapshot, source, tests, build, Simulation, and Git state
unchanged.

## M00_L10 Primary Publication and Metadata Reconciliation — 2026-09-22

The accepted Independent Freeze Review gate is
`PASS_M00_L10_INDEPENDENT_FREEZE_REVIEW`. M00_L10 remains
`COMPLETE / FROZEN / READ-ONLY`, and the frozen lesson-local snapshot is
unchanged. The accepted primary snapshot gate is
`PASS_M00_L10_PRIMARY_PUBLICATION_SNAPSHOT`.

Primary publication identity:

- SHA: `531bceabddf53f194b1edaabbd972ee6865e9ff0`

The User independently verified that the primary snapshot contains only files
inside the frozen M00_L10 lesson folder. The canonical publication phase remains
`PENDING METADATA COMMIT`. No metadata commit SHA is available, no metadata
push has occurred, and no `PUBLICATION_VERIFIED` verdict is recorded.

This preserves the two-commit Historical Snapshot model: frozen lesson snapshot
primary commit, repository-level metadata commit, and later external final
publication verification. No third commit is required merely to record final
verification. Frozen lesson-local historical pending wording remains unchanged.

The final lifecycle is `COMPLETE / FROZEN / READ-ONLY`. M00_L10 contains only
the vendor-neutral Elevator position-observation and reference contract.
Position control remains M00_L11 scope, homing and trusted-reference
establishment remain M00_L12 scope, and travel-limit enforcement remains
M00_L13 scope. Evidence remains exactly `THEORY VERIFIED`, `SIMULATION
VERIFIED`, and `REAL HARDWARE DEFERRED`. Active Lesson Count is `0`, Current
Active M00 Lesson is `NONE`, M00_L11 is `INACTIVE / NOT CREATED`, and the
locked M00_L01–M00_L16 roadmap is unchanged with no M00_L17.

## M00_L11 Controlled Activation — 2026-09-22

The accepted M00_L10 predecessor is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED`, primary SHA `531bceabddf53f194b1edaabbd972ee6865e9ff0`, metadata SHA
`cb8c125da0bbc7e2bf0aeebe40163c1a72909445`, with final gate
`PASS_M00_L10_FINAL_PUBLICATION_VERIFICATION`.

M00_L11 consumed `PASS_M00_L11_PREPARATION_BASELINE`,
`PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT`,
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`, and
`PASS_M00_L11_FINAL_DESIGN_LOCK`. Inheritance is exact: production
`108 / 108 / 0 / 0 / 0`, tests `102 / 102 / 0 / 0 / 0`, deploy/config/support
`24 / 24 / 0 / 0 / 0`, and lesson-local documentation `98 / 98 / 0 / 0 / 0`;
unexpected substantive drift is `NONE`.

M00_L11 is activated as the sole
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active
lesson count `1`, current active lesson `M00_L11`, implementation `NOT
STARTED`, and Independent Activation Review `PENDING`. M00_L10 remains frozen,
published, and verified; M00_L12 remains `INACTIVE / NOT CREATED`.

The one new concept is vendor-neutral Elevator closed-loop position request
semantics in meters. The locked future IO surface is `updateInputs(...)`,
`requestPositionMeters(double)`, and `stop()`. Requested states are exactly
`STOPPED` and `POSITION_REQUESTED`; the future observation adds requested state,
target position, and derived position error to the five inherited members.
Requests require a finite target plus valid and trusted position; finite
negative targets remain valid and no travel-range clamp exists. Valid requests
record intent, rebuild the immutable observation, and forward once. Invalid
requests make no state mutation or IO request. `periodic()` does not reissue;
stop records stopped/target `0.0`, rebuilds, and forwards once.

Noop remains the only runtime Elevator implementation. RobotContainer,
RobotTelemetry, Constants, commands, bindings, autonomous integration, real
adapters, and ElevatorIOSim remain unchanged. M00_L12 owns homing and trusted
reference establishment. M00_L13 owns travel-limit safety and target clamping.
Current M00_L11 implementation, build/test, and Simulation evidence are `NOT
YET ESTABLISHED FOR M00_L11`; real hardware is `DEFERRED`. The eventual
closure target is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`, explicitly planned rather than achieved. The complete activation
record is the M00_L10→M00_L11 transition guide. No implementation, test, build,
Simulation, closure, freeze, publication, or Git result is claimed by this
activation.

## M00_L11 Implementation, Verification, and Documentation Reconciliation — 2026-09-22

The preceding controlled-activation section is historical. M00_L11 is now
`IN_PROGRESS / ACTIVE / IMPLEMENTATION COMPLETE` with accepted gates
`PASS_M00_L11_USER_FOCUSED_TESTS`, `PASS_M00_L11_USER_CLEAN_REGRESSION`, and
`PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`. Documentation Reconciliation is
complete. Independent Closure Review remains `PENDING`; Freeze and Publication
remain unauthorized.

The accepted chronology preserves both test defects: the compile-helper
visibility defect and the inconsistent equality fixture. Both were classified
as `TEST_IMPLEMENTATION_DEFECT`; no production implementation defect was
found. The final production/test integrity remains the authorized M00_L11
delta, and the protected RobotContainer composition test remains inherited.

The User's forced focused run passed with `BUILD SUCCESSFUL in 18s` and four
executed actionable tasks. Clean regression passed with `BUILD SUCCESSFUL in
37s` and seven executed actionable tasks. Bounded Simulation passed Disabled
idle, Teleop enabled persistence, and return-to-Disabled persistence using
Noop-only runtime composition and all eight exact telemetry values.

The current evidence classification is exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`. Simulation does not claim
actuation, convergence, encoder correctness, homing, gravity behavior, travel
limits, hardware communication, or mechanism safety. M00_L12 and M00_L13 scope
remain unchanged.

## M00_L11 Controlled Freeze — 2026-09-22

The preceding implementation and verification reconciliation is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`. The authorized implementation delta, focused-test history, clean regression, and bounded Simulation evidence remain unchanged.

The authoritative roadmap state is:

```text
M00_L11: COMPLETE / FROZEN / READ-ONLY / READY FOR INDEPENDENT FREEZE REVIEW / NOT YET PUBLISHED
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

The one-concept Design Lock, Frozen Backbone, M00_L10 predecessor, exact roadmap order, test-defect chronology, and verification boundaries remain unchanged. This freeze does not activate M00_L12, advance the roadmap, publish M00_L11, or establish remote or hardware verification. Earlier active-state text remains historical chronology.

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

This is the current authoritative M00_L12 lifecycle record. The accepted Independent Closure Review gate is `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW`, verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`, with remaining legitimate closure findings `NONE`. Earlier activation, implementation, verification, and documentation-reconciliation records remain historical chronology.

M00_L12 is now `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`. Active Lesson Count is `0`, Current Active M00 Lesson is `NONE`, and M00_L13 remains `INACTIVE / NOT CREATED`. The next gate is Independent Freeze Review. Publication and final publication verification remain pending User-owned Git workflow; no M00_L12 publication SHA or remote status is established.

The single concept remains a bounded, scheduler-managed Elevator homing lifecycle that requests vendor-neutral reference acquisition and recognizes success only from normalized IO-reported `positionReferenced`. The five-field Inputs contract, four-method IO surface, three requested states, eight-field Observation, command lifecycle, truthful Noop behavior, and M00_L13 travel-limit firewall remain as accepted. `positionMeters == 0.0` alone does not establish home. No physical adapter, `ElevatorIOSim`, homing binding, or physical homing behavior is claimed.

The accepted test and Simulation evidence remains unchanged: fresh focused tests passed under `PASS_M00_L12_USER_FOCUSED_TESTS` with `--rerun-tasks`, `BUILD SUCCESSFUL in 35s`, four tasks executed, and seven named test classes; clean regression passed under `PASS_M00_L12_USER_CLEAN_REGRESSION`, `BUILD SUCCESSFUL in 24s`, five tasks executed; bounded Simulation passed under `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION`. Simulation shows bounded software/runtime and Noop behavior only. The classification remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; physical motion, sensor activation, calibration, reference accuracy, and hardware convergence remain unverified.

The accepted byte comparison remains production `109 / 105 / 4 / 0 / 1`, tests `102 / 98 / 4 / 0 / 1`, and deploy/config/support `24 / 24 / 0 / 0 / 0` (compared / identical / changed / missing / added). The three architecture-test HOLD/repair episodes remain test implementation defects; no production defect was found. M00_L11 remains frozen and published.

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

The following records the lifecycle and evidence at activation. Pending implementation and verification statements in that snapshot are historical and are superseded by the current reconciliation below.

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

Accepted gates include PASS_M00_L13_IMPLEMENTATION_AUTHORIZATION, PASS_M00_L13_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW, PASS_M00_L13_FINAL_INDEPENDENT_STATIC_REREVIEW, PASS_M00_L13_USER_FOCUSED_TESTS, PASS_M00_L13_USER_CLEAN_REGRESSION, PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION, PASS_M00_L13_DOCUMENTATION_RECONCILIATION, PASS_M00_L13_CLOSURE_DOCUMENTATION_REPAIR, and PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW with verdict CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE. The initial Closure Review HOLD_M00_L13_CLOSURE_REVIEW_STALE_AGENTS_IMPLEMENTATION_STATUS was limited to the stale current lifecycle statement and is preserved as resolved history. Freeze Reconciliation is complete; Independent Freeze Review is next.

Production delta versus frozen M00_L12 is common 110 / identical 109 / changed 1 / missing 0 / added 1. ElevatorSubsystem.java changed and ElevatorTravelLimits.java was added. Test delta is common 103 / identical 101 / changed 2 / missing 0 / added 1. The initial static-review HOLD concerned test and architecture guards only; repair changed the two test files, with no production defect or design-lock change. Final static re-review passed.

Focused tests passed with BUILD SUCCESSFUL in 42s and 4 actionable tasks executed. Clean regression passed with BUILD SUCCESSFUL in 25s, 5 actionable tasks executed, and GRADLE_EXIT_CODE=0. The earlier PowerShell NativeCommandError caused by the WPILib joystick stderr warning was an evidence-capture issue, not a test failure. Bounded Simulation passed for Disabled -> Teleop Enabled -> Disabled using truthful Noop composition; it does not establish configured or physical travel-limit behavior. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

M00_L13 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED / NOT YET PUBLICATION-VERIFIED. Active Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L12 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14 remains INACTIVE / NOT CREATED; M00_L15/L16 remain future scope. Independent Freeze Review is PENDING. Primary frozen snapshot publication, metadata publication, User push, and Final Publication Verification remain PENDING / USER-OWNED; no M00_L13 publication SHA is established. Physical travel and hardware facts remain UNKNOWN / DEFERRED.

## Historical M00_L13 Metadata Publication Reconciliation before Commit 2 — 2026-09-24

Accepted gates: PASS_M00_L13_INDEPENDENT_FREEZE_REVIEW / FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION; PASS_M00_L13_PRIMARY_FROZEN_SNAPSHOT_PUBLICATION_COMMIT; and PASS_M00_L13_METADATA_PUBLICATION_RECONCILIATION. The User-owned primary frozen snapshot commit completed with SHA `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704` and subject `Complete M00_L13 Elevator travel-limit safety`.

HOLD_M00_L13_PRIMARY_PUBLICATION_SCRIPT_BOUNDARY_MISMATCH and HOLD_M00_L13_PRIMARY_PUBLICATION_GIT_ADD_PATHSPEC_DEFECT were safe staging failures caused by the publication script. Both are classified as PUBLICATION_SCRIPT_DEFECT with NO_LESSON_DEFECT, NO_SOURCE_DEFECT, NO_TEST_DEFECT, and NO_FREEZE_DEFECT. The corrected User-owned staging flow created the accepted primary commit. User evidence states no M00_L13 authored path remained dirty after that commit; the known unrelated working-tree paths remain protected.

At the time this record was written, M00_L13 was COMPLETE / FROZEN / READ-ONLY and its primary snapshot was committed. The metadata publication commit, push, and external Final Publication Verification had not yet occurred. This historical state is superseded below.

Active Lesson Count was 0; Current Active M00 Lesson was NONE. M00_L14 was INACTIVE / NOT CREATED; M00_L15/L16 remained future scope. Technical contracts, source/test deltas, executable and Simulation evidence, and THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED classification were unchanged. Frozen M00_L12 and generated/runtime classifications remained protected.

## Historical M00_L13 Publication State before M00_L14 Activation — 2026-09-25

This is an earlier publication-state snapshot. Its then-pending final verification statements are historical and are superseded by the accepted external M00_L13 verification recorded in the current M00_L14 activation state below.


M00_L13 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED. The Primary Frozen Snapshot Commit completed at `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704`, subject `Complete M00_L13 Elevator travel-limit safety`. The canonical metadata publication is established by Commit 2 of the two-commit Historical Snapshot model. The Metadata Commit identity and matching remote-main identity are external publication evidence and are not self-embedded. Final Publication Verification remains PENDING / EXTERNAL.

Active Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L14 is INACTIVE / NOT CREATED; M00_L15/L16 remain future scope. The canonical publication remains exactly two commits; amending the existing metadata commit does not add a third verification-only commit.

The independent final publication reviews recorded `HOLD_M00_L13_FINAL_PUBLICATION_VERIFICATION_STALE_CURRENT_PUBLICATION_STATE` and `HOLD_M00_L13_FINAL_PUBLICATION_REREVIEW_STALE_POST_AMEND_CHRONOLOGY`. Both are historical publication metadata/documentation findings; the latter is addressed by this chronology repair. Classification: `PUBLICATION_METADATA_CHRONOLOGY_DEFECT`; `NO_LESSON_DEFECT`; `NO_PRODUCTION_DEFECT`; `NO_TEST_DEFECT`; `NO_ARCHITECTURE_DEFECT`; `NO_SIMULATION_DEFECT`; `NO_FREEZE_DEFECT`; `NO_PUBLICATION_IDENTITY_DEFECT`; `NO_TWO_COMMIT_MODEL_CHANGE`. Technical contracts and verification evidence remain unchanged.


## Historical M00_L14 Controlled Activation Snapshot — 2026-09-25

This records the activation-time state. Its pending implementation and
verification statements are superseded by the accepted post-verification
reconciliation below.

M00_L13 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Primary
snapshot SHA: 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704; canonical metadata SHA:
658d1e44c417763df3689b9b52e409161446c593. Evidence remains THEORY VERIFIED /
SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

Accepted M00_L14 gates: PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD (6 actionable
tasks, 6 executed, BASELINE_BUILD_EXIT_CODE=0);
PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT; and
PASS_M00_L14_FINAL_DESIGN_LOCK. The candidate was an untouched authored copy
of M00_L13 with 339/339 files identical at audit. Controlled Activation changes
lifecycle and documentation only.

Current M00 lifecycle:

M00_L13: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L14: ACTIVE / IN_PROGRESS / NOT COMPLETE / NOT FROZEN / NOT PUBLISHED
Active Lesson Count: 1
Current Active M00 Lesson: M00_L14 — Shoot Coordination
M00_L15: FUTURE / INACTIVE / NOT CREATED
M00_L16: FUTURE / INACTIVE / NOT CREATED
Independent Activation Review: HOLD_M00_L14_INDEPENDENT_ACTIVATION_REVIEW_DOCUMENTATION_RECONCILIATION_REQUIRED (preserved)
Activation Documentation Repair: COMPLETE / READY FOR INDEPENDENT ACTIVATION RE-REVIEW
Independent Activation Re-review: PENDING
Implementation Authorization: NOT AUTHORIZED / PENDING

M00_L14 adds coordination logic only: one scheduler-managed
frc.robot.commands.ShootCommand requires exactly FlywheelSubsystem and
FeederSubsystem. It consumes FlywheelObservation.readyAtSpeed() as the sole
Flywheel readiness authority. Feeding is admitted only when ready-at-speed and
the Feeder is available and connected. Feeder requestedState() is software
request state only, not physical transport evidence.

The constructor accepts non-null Flywheel and Feeder subsystems and finite,
strictly positive caller-supplied targetVelocityRpm; it is not a verified
hardware shooting RPM. Constants, RobotContainer, subsystem, IO, Observation,
telemetry, vendor adapter, and deploy contracts remain unchanged. No local
readiness/feed authority, timeout, completion detector, Elevator/Intake
coordination, vision aiming, or autonomous event integration is authorized.
The expected future production delta is one added ShootCommand.java.

Normal initialization stops Feeder once then requests Flywheel velocity once;
execute does not reissue that Flywheel request and makes Feeder requests only
on semantic state transitions. The command does not self-finish. Every
terminal end attempts both stops in Feeder-then-
Flywheel order; if both throw, Feeder remains primary and Flywheel is
suppressed. If the initial Feeder stop throws during initialize(), Flywheel
velocity is not requested and Flywheel stop is attempted once. If the Flywheel
velocity request throws, the completed Feeder baseline stop is not repeated
and Flywheel stop is attempted once. If requestFeed() throws during execute(),
Feeder stop and Flywheel stop are both attempted once, including Flywheel stop
if Feeder cleanup throws. If Feeder stop throws during readiness/admission
loss, that stop is not retried in the failing execute() and Flywheel stop is
attempted once. Cleanup RuntimeExceptions are suppressed on the original
RuntimeException, which remains primary and is rethrown. No retry loop, silent
recovery, clamp, rewrite, fallback, or routine Error recovery is introduced.

There is no L14 driver binding. The inherited Left Bumper manual Feeder
command remains; scheduler requirement ownership provides Feeder mutual
exclusion.

M00_L15 remains Intake-to-Feeder Coordination. M00_L16 remains Mechanism
Autonomous Event Integration. Evidence at activation was THEORY VERIFICATION
IN PROGRESS / SIMULATION NOT TESTED / REAL HARDWARE DEFERRED. Runtime Flywheel
and Feeder adapters remain Noop; no physical shot is claimed.

## Historical M00_L14 Post-Verification Reconciliation — 2026-09-25

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14
is the sole ACTIVE / IN_PROGRESS lesson, NOT COMPLETE / NOT FROZEN / NOT
PUBLISHED; Active Lesson Count is 1. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED. Independent Closure Review is the next gate.

Accepted gates are PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION,
PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW,
PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW,
PASS_M00_L14_USER_FOCUSED_TESTS,
PASS_M00_L14_USER_CLEAN_REGRESSION, and
PASS_M00_L14_USER_BOUNDED_SIMULATION. Focused tests completed with BUILD
SUCCESSFUL in 10s (4 actionable tasks: 3 executed, 1 up-to-date;
FOCUSED_TEST_EXIT_CODE=0). Clean regression completed with BUILD SUCCESSFUL in
30s (5 actionable tasks, 5 executed; CLEAN_REGRESSION_EXIT_CODE=0).

Bounded Simulation verified Disabled startup, Teleoperated enable, return to
Disabled with semantic mechanism request states STOPPED, and clean shutdown
(LAST_NATIVE_EXIT_CODE=0). RobotContainer remains unchanged, with no
ShootCommand binding. The Simulation evidence covers application startup, mode
transitions, scheduler/integration stability, safe semantic state, and clean
exit; it does not demonstrate a ShootCommand binding or physical shooting.
Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

The five independent static-review HOLD/repair rounds were test-only
architecture-guard findings; no production defect was found. The final
independent static review passed. Flywheel and Feeder runtime adapters remain
Noop, so physical shooting values and behavior are unverified. Documentation
reconciliation is current. Independent Closure Review remains pending; M00_L14
is not complete, frozen, or published.

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
INACTIVE / NOT CREATED. The locked M00_L01-L16 roadmap and L15/L16 ownership
remain unchanged.

Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED. User focused tests, clean regression, and bounded Simulation retain
their accepted PASS results. RobotContainer has no ShootCommand binding, so
Simulation did not schedule ShootCommand or verify physical shooting. Direct
`end(true)` unit invocation covers interrupted-end cleanup semantics; actual
scheduler-driven cancellation was not tested. Independent Freeze Review is
PENDING; publication is NOT PUBLISHED / PENDING and Final Publication
Verification is PENDING.

## Historical M00_L14 Metadata Publication Preparation — 2026-09-26

The accepted Independent Freeze Review is
`PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW` with verdict
`INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION`. The User completed
`PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT` at SHA
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`. That Primary Frozen
Snapshot is Commit 1 of the two-commit Historical Snapshot model.

M00_L14 remains COMPLETE / FROZEN / READ-ONLY and NOT ACTIVE. At that earlier
preparation point, the publication target had not yet been reconciled. The
record is superseded by the repair below.

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Active
Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L15 and M00_L16
remain FUTURE / INACTIVE / NOT CREATED. The authorized M00_L01-L16 roadmap
and lesson ownership are unchanged. Evidence remains THEORY VERIFIED /
SIMULATION VERIFIED / REAL HARDWARE DEFERRED. RobotContainer has no
ShootCommand binding; bounded Simulation did not schedule it or prove a
physical shot. Direct `end(true)` unit invocation does not prove scheduler
cancellation. No technical lesson content is changed by this metadata step.

## Historical M00_L14 Metadata Publication Reconciliation Repair — pre-Commit-2 state

This is the accepted preparation snapshot before Metadata Publication Commit 2
was created. Its pending-commit wording is historical and superseded by the
current post-amend publication record below.

The accepted `HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA`
identified that the earlier preparation supplied no actual post-Commit-1
metadata delta. This repair added the required documentation delta and applied
the M00_L13 precedent: User-owned Metadata Publication Commit 2 was the
publication point, and its intended committed lesson state was
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.

Primary Frozen Snapshot Commit 1 remains
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`. At the time of this preparation,
Commit 2 and its push had not occurred; its own identity was to remain external
and not self-embedded. Final Publication Verification was pending. M00_L14 was
NOT ACTIVE; Active Lesson Count was 0 and Current Active M00 Lesson was NONE.
M00_L15/L16 remained FUTURE / INACTIVE / NOT CREATED. Evidence remained THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. The L01-L16 roadmap
and lesson ownership were unchanged.

## Historical M00_L14 Post-Amend Publication State — 2026-09-26 (before M00_L15 activation)

Accepted User evidence establishes Primary Frozen Snapshot Commit 1 at
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`, followed by completed User-owned
Metadata Publication Commit 2. Its amendment is COMPLETED under
`PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION` and
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND`; the canonical identity
remains external and is not self-embedded. M00_L14 is COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / NOT ACTIVE; Active Lesson Count is 0 and Current Active M00 Lesson
is NONE. M00_L15 and M00_L16 remain FUTURE / INACTIVE / NOT CREATED.

Accepted gates include `PASS_M00_L14_DOCUMENTATION_RECONCILIATION`,
`PASS_M00_L14_FREEZE_RECONCILIATION`,
`PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION`,
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT`,
`PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION`, and
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND`. The publication remains
exactly two commits; no third verification-only commit is introduced.

Remote push is PENDING / USER-OWNED because no push evidence is supplied. The
prior `HOLD_M00_L14_FINAL_PUBLICATION_VERIFICATION_METADATA_COMMIT_STATE_RECONCILIATION_REQUIRED`
was resolved by the accepted User amendment. The subsequent
`HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED`
identified stale current-state chronology and is addressed by this
documentation reconciliation. Final Publication Verification remains PENDING / EXTERNAL.
No final-verification PASS was claimed in this historical state. Evidence
remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; lesson
scope, Simulation limitations, and the direct `end(true)` versus
scheduler-driven cancellation distinction remain unchanged.

## Historical M00_L15 Controlled Activation — 2026-09-26

M00_L14 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED and NOT ACTIVE.
Its primary SHA is `fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`; canonical metadata
SHA is `1a85c0827ee91ba7f70a595d94c49ad16a6df9cb`. Its accepted final
publication and push gates are `PASS_M00_L14_FINAL_PUBLICATION_VERIFICATION`
and `PASS_M00_L14_PUBLICATION_PUSH`.

M00_L15 is the sole IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
lesson. Accepted gates are `PASS_M00_L15_UNTOUCHED_COPY_BASELINE_BUILD`,
`PASS_M00_L15_ARCHITECTURE_INHERITANCE_AUDIT`, and
`PASS_M00_L15_FINAL_DESIGN_LOCK`. The candidate inherited all 341 authored
files from M00_L14 unchanged. Its one concept is scheduler-managed
coordination of existing Intake and Feeder semantic behaviors under one
command lifecycle. The locked command requires exactly IntakeSubsystem and
FeederSubsystem.

The Final Design Lock preserves 18 direct-contract test cases and requires
four real CommandScheduler test cases for startup, scheduler cancellation,
Intake requirement contention, and Feeder requirement contention. No
RobotContainer binding is added. M00_L16 retains Mechanism Autonomous Event
Integration and remains FUTURE / INACTIVE / NOT CREATED.

This Controlled Activation was documentation-only. M00_L15 production and test
implementation were NOT STARTED and NOT AUTHORIZED at that time. Independent
Activation Review was PENDING; freeze and publication were NOT AUTHORIZED.
Simulation was NOT TESTED for M00_L15 and real hardware remained DEFERRED. No
physical transfer or timing claim was made. No M00_L17 was authorized.

## Historical M00_L15 Implementation and Verification Reconciliation — 2026-09-27 (pre-freeze state)

M00_L15 remains the sole IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK lesson. M00_L14 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED. Independent Closure Review is pending; M00_L15 is not COMPLETE,
FROZEN, or PUBLISHED. M00_L16 remains FUTURE / INACTIVE / NOT CREATED. The
L01–L16 roadmap, lesson order, and one-concept ownership remain unchanged.

The accepted M00_L15 implementation and verification gates are
`PASS_M00_L15_IMPLEMENTATION_AUTHORIZATION`,
`PASS_M00_L15_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW`,
`PASS_M00_L15_FINAL_INDEPENDENT_STATIC_REVIEW`,
`PASS_M00_L15_USER_FOCUSED_TESTS`,
`PASS_M00_L15_CLEAN_REGRESSION_FAILURE_DIAGNOSIS`,
`PASS_M00_L15_INHERITED_ARCHITECTURE_TEST_RECONCILIATION`,
`PASS_M00_L15_ARCHITECTURE_SCAN_ROBUSTNESS_REPAIR`,
`PASS_M00_L15_FEEDER_OWNER_JAVA_PARSER_REPAIR`,
`PASS_M00_L15_FEEDER_OWNER_JAVA_PARSER_REVIEW`,
`PASS_M00_L15_USER_CLEAN_REGRESSION`, and
`PASS_M00_L15_BOUNDED_SIMULATION_DS_VERIFICATION`.

Relative to M00_L14, 112 shared production Java files remain identical. The
sole L15 production addition is
`src/main/java/frc/robot/commands/IntakeToFeederCommand.java`; the command
requires exactly IntakeSubsystem and FeederSubsystem. RobotContainer and the
inherited Intake, Feeder, Shoot, and subsystem sources remain unchanged. No
IntakeToFeederCommand binding was added. The focused suite passed 22 / 22.

The initial 830-test clean regression had two failures. The accepted diagnosis
classified them as `EXPECTED_INHERITED_TEST_CONTRACT_EVOLUTION`, not production
defects. Active-lesson test reconciliation preserved exact normalized-path
allowlists and moved Feeder ownership detection to the JDK Java parser. The
final User clean regression passed all 830 tests: BUILD SUCCESSFUL, 830 PASS,
zero failures, errors, or skips, `BUILD_EXIT_CODE=0`.

Bounded Simulation verified application startup and Driver Station attachment.
Disabled had Robot Enabled=No and Intake, Feeder, and Flywheel RequestedState=
STOPPED. Teleoperated had Robot Enabled=Yes, DS Attached=Yes, and Intake and
Feeder STOPPED. On return to Disabled, Robot Enabled=No and Intake and Feeder
remained STOPPED, with no unexpected mechanism state. No unintended L15
activation or fatal runtime/scheduler error occurred. Termination was BUILD
SUCCESSFUL with `SIMULATION_EXIT_CODE=0`. The unavailable Joystick Button 6
warning on unassigned or unplugged port 0 was EXPECTED / NON-BLOCKING. Because
RobotContainer has no L15 binding, Simulation did not
schedule IntakeToFeederCommand; direct scheduler evidence comes from focused
tests. Evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`. Physical transfer, game-piece presence, mechanism timing, sensors,
and hardware behavior are not established. Documentation reconciliation was
complete at this pre-freeze point; Independent Closure Review was then the
next gate. This section records the state at that time.

## Historical M00_L15 Freeze Reconciliation — 2026-09-27

The accepted independent closure rereview gate is
`PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW`, with verdict
`M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION`;
the rereview reported no findings. Freeze reconciliation is complete, and
M00_L15 is `COMPLETE / FROZEN / READ-ONLY`.

The evidence remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`. Focused tests remain 22 / 22 PASS. The clean regression remains
830 / 830 PASS with zero failures, errors, or skips and
`BUILD_EXIT_CODE=0`. Bounded Simulation / Driver Station remains PASS with
`SIMULATION_EXIT_CODE=0`; Simulation did not schedule
`IntakeToFeederCommand`. The only production addition remains
`IntakeToFeederCommand.java`, and 112 shared production Java files remain
identical to M00_L14. No source or test file changed during freeze
reconciliation.

At this freeze-reconciliation point, M00_L14 remained COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED; M00_L15 Independent Freeze Review,
publication, and final publication verification were pending. That state is
superseded by the current metadata reconciliation below. Active Lesson Count
was 0; Current Active M00 Lesson was NONE; M00_L16 was FUTURE / INACTIVE / NOT
CREATED. No M00_L17 is authorized.

### Historical M00_L15 Publication Metadata Reconciliation — 2026-09-27

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

### Historical M00_L16 Controlled Activation — 2026-09-27

Accepted User-owned M00_L15 publication evidence establishes primary snapshot
`15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`, metadata publication
`0d3685ce67a0b985459392621e003611eaa6dc35`, and
`PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`. M00_L15 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` and is not modified.
The copied M00_L16 candidate passed
`PASS_M00_L16_UNTOUCHED_COPY_BASELINE_BUILD` (User-reported BUILD SUCCESSFUL,
exit code 0), `PASS_M00_L16_ARCHITECTURE_INHERITANCE_AUDIT` (345/345 authored
files, including 113/113 production Java and 106/106 test Java, inherited
identically), and `PASS_M00_L16_FINAL_DESIGN_LOCK`. The accepted final verdict
is `M00_L16_FINAL_DESIGN_LOCK_PASS_READY_FOR_CONTROLLED_ACTIVATION`.

Controlled activation is APPROVED and documentation-only. M00_L16 is now the
sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` M00 lesson;
Active Lesson Count is 1. Independent Activation Review and separate
Implementation Authorization are PENDING. No production implementation,
focused test, clean regression, or bounded Simulation result is claimed for
L16. Real hardware remains DEFERRED. M00_L16 is the final M00 lesson; the
approved roadmap and lesson order do not change.

The one new concept is scheduler-managed dispatch of one already-verified
mechanism command through the existing PathPlanner named-event boundary.
The selected command is `IntakeToFeederCommand` from M00_L15, which already
owns Intake and Feeder requirements and verified startup/interruption/cleanup
semantics without a new caller numeric setpoint. `ShootCommand` is excluded
because RPM configuration would add another design question. Reuse
`LEARNING_EVENT`, its existing marker and path geometry, and the existing
`ONE_METER_WITH_EVENT` chooser option. `ONE_METER_PATH` remains the event-free
control; `SAFE_STOP` remains. No new event ID, path, timing policy, mechanism
algorithm, subsystem, IO contract, or autonomous framework is approved.

The future production change budget is exactly one modified file:
`src/main/java/frc/robot/RobotContainer.java`. Its eventual binding must
construct a fresh `IntakeToFeederCommand` per dispatch with exact
`IntakeSubsystem` and `FeederSubsystem` requirements. Existing
`Supplier<Command>` and `Commands.defer(...)` registration remain. WPILib
scheduler requirements arbitrate external mechanism contention; the inherited
PathPlanner EventScheduler manages event-child lifecycle. Teleop Right Bumper
`RunIntakeCommand` and Left Bumper `RunFeederCommand` remain unchanged.
The event helpers, selected command, mechanism subsystems, IO, Observations,
telemetry, AutoBuilder, path factories, Swerve, Vision, pose estimation,
deploy assets, vendordeps, and Gradle/config remain unchanged.

`IntakeToFeederCommand.isFinished()` is false. The verified normal
termination chain is PathPlanner `FollowPathCommand.end(...)` to inherited
`EventScheduler.end()`, active event `end(true)`, wrapped registered event,
WPILib `DeferredCommand.end(true)`, then `IntakeToFeederCommand.end(...)`,
which calls `feeder.stop()` and then `intake.stop()`. This supports normal
path completion and interruption; it does not guarantee cleanup after an
arbitrary uncaught library exception. No added timer, timeout, WaitCommand,
race/deadline group, wrapper, or state machine is authorized. A supplier
`RuntimeException` retains the inherited `FACTORY_FAILURE` observation and
safe no-op path without mechanism actuation.

Later test scope adds only
`src/test/java/frc/robot/RobotContainerMechanismAutonomousEventIntegrationTest.java`
with exactly eight `@Test` methods, and updates only the now-stale
`src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java` expectation
that forbids any RobotContainer mention of the command. Teleop, default
command, and direct subsystem access guards remain. The eight obligations
are exact requirements, fresh child per dispatch, real scheduler startup,
no repeated initialization during one lifecycle, interruption cleanup,
Intake contention, Feeder contention, and nonempty-requirement supplier
failure safe no-op.

Future bounded Simulation must exercise `ONE_METER_WITH_EVENT`: startup and
Driver Station attachment; both mechanisms STOPPED before Autonomous;
semantic Intake and Feeder requests at the marker; scheduler-safe completion
or interruption returning Feeder and Intake to STOPPED; Disabled remaining
STOPPED; no fatal scheduler error; successful exit. `ONE_METER_PATH` must not
dispatch the mechanism event. Noop adapters and RequestedState observations
cannot establish physical acquisition, transfer, motor performance, timing,
sensor correctness, or real-hardware behavior. Intended eventual evidence
is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, but L16
has not yet earned that classification.

### Historical M00_L16 Documentation Reconciliation — pre-closure state, 2026-09-27

The preceding controlled-activation section is a historical gate record.
M00_L16 remains the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` M00 lesson. M00_L15 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED. Independent activation review and separate implementation
authorization passed. Implementation completed the locked one concept:
`RobotContainer.java` binds `LEARNING_EVENT` to a fresh
`IntakeToFeederCommand` with exact Intake and Feeder requirements. The source
delta comprises that one production modification, one bounded modification
to `IntakeArchitectureBoundaryTest.java`, and one new eight-test integration
file; 221 other source files remain unchanged. The marker, control path,
chooser, teleop bindings, event helpers, and mechanism code remain unchanged.

The initial independent static review HOLD concerned the architecture guard.
The bounded repair and fresh independent static rereview passed. User focused
tests passed for the architecture guard and all 8 integration tests (BUILD
SUCCESSFUL, exit 0). User clean regression passed with BUILD SUCCESSFUL and
no regression blocker; no numeric test count or exit code was supplied.
Accepted `PASS_M00_L16_BOUNDED_SIMULATION` verified Driver Station
attachment, `ONE_METER_WITH_EVENT` Autonomous RUNNING with Intake
`INTAKE_REQUESTED` and Feeder `FEED_REQUESTED` at `LEARNING_EVENT`,
Flywheel/Elevator STOPPED, and Intake/Feeder STOPPED on Disable or
interruption. `ONE_METER_PATH` ran without an event and kept Intake/Feeder
STOPPED. No fatal scheduler/runtime exception was observed in the supplied
run; Simulation returned normally to the PowerShell prompt. The final Gradle
output showed five actionable tasks, three executed and two up-to-date.
No Simulation exit code was supplied.

Blank AutonomousEvent NT fields are expected: the facade publishes only
when an observation exists, RobotContainer begins with `Optional.empty`,
the new command emits no lifecycle observation, and registration emits only
`FACTORY_FAILURE`. No `LastEvent=LEARNING_EVENT` or dispatch-count claim is
made. No telemetry expansion or production repair is authorized. Evidence
is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; physical
transfer, motor performance, sensors, timing, and electrical behavior remain
unverified. Documentation reconciliation is complete. Independent closure
review, freeze reconciliation, independent freeze review, and User-owned
publication were pending at that historical gate. M00_L16 is the final M00
lesson; no M00_L17 is authorized.

## Historical M00_L16 Freeze Reconciliation — 2026-09-27

The accepted Independent Closure Review is
`PASS_M00_L16_INDEPENDENT_CLOSURE_REVIEW`, with final verdict
`M00_L16_INDEPENDENT_CLOSURE_REVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION` and
no remaining findings. The initial closure HOLD identified the README's
historical/current hierarchy contradiction; the bounded repair changed only
the M00_L16 README and labeled the old plan headings historical and
superseded. The fresh independent rereview closed that finding.

M00_L16 is `COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED`. Active Lesson Count
is `0`; Current Active M00 Lesson is `NONE`. The one concept remains
scheduler-managed dispatch of the already-verified `IntakeToFeederCommand`
through the existing PathPlanner `LEARNING_EVENT` boundary. The accepted
production delta remains the single `RobotContainer.java` modification; the
test delta remains the one bounded architecture-guard modification and one
eight-test integration file. No implementation scope changed during freeze
reconciliation.

Focused Tests, Clean Regression, Bounded Simulation, Documentation
Reconciliation, and Independent Closure Review are COMPLETE / PASS. Evidence
remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
Simulation establishes the bounded software/runtime integration only; it
does not verify physical game-piece transfer, mechanism motor performance,
physical timing, real sensor behavior, electrical behavior, or real-robot
mechanism behavior. Blank AutonomousEvent telemetry remains expected under the
existing observation-publication contract.

Freeze Reconciliation is COMPLETE / `PASS_M00_L16_FREEZE_RECONCILIATION`.
Independent Freeze Review is PENDING. Publication is PENDING / NOT PUBLISHED
and remains subject to the independent freeze review and User-owned publication
workflow. M00_L16 remains the final M00 lesson; no M00_L17 is introduced or
authorized.

## Historical M00_L16 pre-Commit-2 Publication Metadata Reconciliation — 2026-09-27

The preceding freeze-reconciliation section records its historical gate.
Independent Freeze Review passed as `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW`
with verdict `M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
The User created the Primary Frozen Snapshot Commit 1 at
`ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata
reconciliation is COMPLETE / `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`
and prepared for the separate User-owned Metadata Publication Commit 2.
Commit 2 has no established hash and remains PENDING; push and final external
publication verification also remain PENDING. M00_L16 remains COMPLETE /
FROZEN / READ-ONLY / NOT PUBLISHED, with Active Lesson Count 0 and Current
Active M00 Lesson NONE. Evidence remains THEORY VERIFIED / SIMULATION VERIFIED /
REAL HARDWARE DEFERRED. The initial closure HOLD, bounded README repair, and
fresh closure rereview PASS remain part of the accepted chronology. The
two-commit Historical Snapshot model requires no third verification-only
commit. M00_L16 remains the final M00 lesson; no M00_L17 is authorized.
