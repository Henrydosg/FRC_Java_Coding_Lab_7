<!-- ACM-11 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-11 formal domain closure — 2026-10-02

The Architect authorized ACM-11 FORMAL DOMAIN CLOSURE after Sol's independent Configuration Authority audit. Accepted token: `PASS_ACM_11_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. **ACM-11 is FORMALLY CLOSED / FORMALLY RECORDED: 55 / 55 dimensions CLOSED; 0 BLOCKED. ACM-11-F01: NOT ESTABLISHED. NO ADDITIONAL ACM-11 FINDING ESTABLISHED.** The detailed configuration record and complete matrix are in [AGENTS.md](../../../AGENTS.md).

`Constants.java` is the default owner for stable robot-specific and lesson-approved configuration; local implementation details, runtime state, and simulation/test fixtures retain their proper owners. Current module CANcoder offsets are FL +0.068603515625, FR +0.014404296875, BL +0.46240234375, BR -0.057373046875 rotations. The later tracked user-authoritative recalibration supersedes older activation-brief values. Drive gains and PathPlanner model inputs remain provisional; no project-specific estimator covariance is configured. No contradictory current production authority was established.

Static governance validator PASS (12 authoritative PDFs, hashes, trust checks; zero deterministic findings); semantic-fidelity certification was NOT PERFORMED. ACM-01–ACM-10 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED; latest checkpoint `d6bdc6f1fef24239d7b5c0802f29453d9ce1a24f` (`audit-acm-10-closed`). ACM-12 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN; M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. Documentation-only recording; exact next gate is User-owned ACM-11 Git checkpoint. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.
<!-- ACM-11 DOMAIN CLOSURE CURRENT END -->

<!-- ACM-10 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-10 formal domain closure — 2026-10-02

The Architect reviewed Sol's independent initial audit and explicitly authorized **ACM-10 FORMAL DOMAIN CLOSURE**. Accepted audit token: PASS_ACM_10_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW. **ACM-10 is FORMALLY CLOSED / FORMALLY RECORDED: 50 / 50 dimensions CLOSED; 0 BLOCKED. ACM-10-F01: NOT ESTABLISHED. NO ADDITIONAL ACM-10 FINDING ESTABLISHED.** The dimension-by-dimension record is included in the current ACM-10 closure block in [AGENTS.md](../../../AGENTS.md).

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

# M00_L16 — Mechanism Autonomous Event Integration Checklist
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

ACM-01 through ACM-05 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED, with their recorded checkpoint commits and tags preserved in [AGENTS.md](../../../AGENTS.md). No regression was established through ACM-05. ACM-07 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; ACM-08 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. No Constants.java cleanup/refactor is authorized. The User subsequently checkpointed, pushed, and annotated-tagged ACM-06 at `58549f11989d2198378a38479228663a6b6b7613` (`audit-acm-06-closed`); the User verified the remote main and annotated tag target at that commit. This is documentation-only; no project execution or Git write occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-06 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-05 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-05 formal domain closure — 2026-10-01

ACM-05 — Observation / IOInputs Data Flow — is FORMALLY CLOSED / FORMALLY RECORDED by the Architect after independent Sol audit `PASS_ACM_05_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. All thirty-one audit dimensions are CLOSED; ACM-05-F01 is NOT ESTABLISHED. Static governance preflight passed: 12 source PDFs, 12 matching hashes, zero deterministic findings. Semantic fidelity certification was not performed.

The ACM-04 closure block below preserves its earlier stage record; its ACM-05 next-domain wording predates this closure and does not control the current cursor.

Current lesson preserves IO / hardware / simulation → subsystem-owned IOInputs → immutable observation/read model → project consumers. Seven IO families were inventoried; no mutable Inputs escape, alias, shared ownership, or alternate data bypass was found. The fourteen observation types and their nested Swerve/Vision values remain immutable project read models. Vision snapshots its target list and values. Telemetry and commands consume observations or semantic values. Noop and Swerve/Gyro/Vision simulation use the same boundary. FeederIO → FeederIOInputs → FeederSubsystem → FeederObservation is CORRECT; no transport escape. Full record: [AGENTS.md](../../../AGENTS.md).

ACM-01 through ACM-04 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED. At the ACM-05 checkpoint, ACM-06 was NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; that cursor was superseded by the formal ACM-06 closure recorded above. ACM-07 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; ACM-08 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. The User checkpointed and pushed ACM-05 at `6c125c2490c50b9f2c0151379307ac72d10c0b22` with annotated tag `audit-acm-05-closed`; its remote target was verified by the User. Documentation-only; no Git write or project execution. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

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

Current F03 closure gates:
- [x] Architect-approved destination and registration before Java edits.
- [x] Four-identity implementation and required User verification.
- [x] Independent post-implementation review.
- [x] Documentation/evidence reconciliation.
- [x] Independent final architecture/closure review.
- [x] Explicit Architect F03 repair-closure authorization recorded.
- [ ] Separate Architect activation/design authorization for F04.

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

Current F03 evidence gates:
- [x] Architect target authorization and registration before code.
- [x] Four-identity implementation and static self-audit.
- [x] User Java-17 environment and ordered Gates 1-4 PASS.
- [x] Independent post-implementation review PASS.
- [x] Documentation/evidence reconciliation.
- [ ] Independent final architecture/closure review.
- [ ] Formal F03 repair closure.

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

- [x] F03 design and explicit Architect target authorization.
- [x] F03 governance registration before Java changes.
- [ ] F03 bounded implementation and static self-audit.
- [ ] User verification and independent review.
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

- [x] Independent final F02 architecture/closure review PASS.
- [x] Explicit Architect F02 repair-closure authorization issued.
- [x] Formal F02 closure recorded; all four dimensions CLOSED; requirements NONE.

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

- [x] Exact six-Java-identity implementation and static self-audit.
- [x] Reverse-normalized comparison PASS; no alias or old production identity.
- [x] User clean and focused placement test PASS.
- [x] User direct consumer test, full suite and clean build PASS.
- [x] Independent implementation review PASS.
- [x] Documentation/evidence reconciliation COMPLETE.
- [ ] Independent final architecture/closure review.
- [ ] Architect formal F02 closure authorization and recording.

<!-- ACM-01-F02 PRE-CLOSURE RECONCILED SNAPSHOT END -->

<!-- ACM-01-F02 REGISTRATION BEGIN -->
## ACM-01-F02 exceptional repair registration — 2026-09-30

This is the registration-stage checklist, not an execution result.
Architect-accepted F02 corrects only the inherited S00_L23
DriveThreeMeterValidationObservation package placement in M00_L16 under
Document C OC-02 Section 1. Authorized: one move to
`frc.robot.observation.swerve`, three production import changes, one
existing test import change, and one new focused placement test. See
[the F02 repair ADR](../../../docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md).

- [ ] Exact six-Java-identity static self-audit and reverse-normalized comparison.
- [ ] User clean and new focused placement test; fail-stop on failure.
- [ ] User existing validation command test; fail-stop on failure.
- [ ] User full test suite; fail-stop on failure.
- [ ] User clean build; fail-stop on failure.
- [ ] Independent post-implementation review.

At registration, F02 remains open; F01/DOC-01 are CLOSED; F03/F04 are
ACCEPTED / PARKED. M00_L16 is IN_PROGRESS / NOT RE-FROZEN /
NOT REPUBLISHED; ACM-01 HOLD, ACM-02 NOT STARTED, Phase 4 NOT STARTED /
FORBIDDEN. No M00_L17 or historical-lesson edits.

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

### Current ACM-01-F01 checklist

- [x] Architect-adjudicated design and exceptional repair registration.
- [x] Exact 15-identity package/import/placement-guard correction.
- [x] Initial compile failure, forensic CF-U/PF-U and evidence limits preserved.
- [x] User controlled clean reproduction and all six moved model tests PASS.
- [x] Remaining focused gates, full suite and clean build PASS.
- [x] Independent Sol post-implementation review PASS.
- [x] Authorized nine-document documentation/evidence reconciliation.
- [ ] Independent final ACM-01-F01 architecture / closure review.
- [ ] Repair closure and explicit re-freeze authorization/transition.
- [ ] Independent freeze review and User-owned publication workflow.
- [ ] ACM-01 rereview and separate audit resumption authorization.

ACM-02: NOT STARTED; Phase 4: NOT STARTED / FORBIDDEN. No M00_L17.

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

### Current refrozen repair checklist

- [x] P3-H01 IMPLEMENTED / VERIFIED / INDEPENDENTLY REVIEWED / PRESERVED / CLOSED.
- [x] CFG-H01 IMPLEMENTED / VERIFIED / REMOVED / INDEPENDENTLY REVIEWED / CLOSED.
- [x] Accepted User automated and fresh P3-H01 Simulation evidence preserved.
- [x] Localization-before-fusion and Intake/Feeder semantic corrections preserved.
- [x] Separate P3-H01 / CFG-H01 applicability dispositions resolved and recorded.
- [x] Independent final architecture/closure review PASS accepted.
- [x] Technical / verification / documentation / architecture closure CLOSED.
- [x] Final repair Transition Guide accepted; earlier steps/appendices preserved.
- [x] Remaining repair requirements NONE.
- [x] Explicit Architect/User re-freeze authorization consumed.
- [x] M00_L16 COMPLETE / FROZEN / READ-ONLY; active counts 0; current lesson NONE.
- [x] Original historical publication identities and physical-evidence limits preserved.
- [x] Independent frozen-candidate review PASS accepted.
- [x] User repaired primary Commit 1 CREATED at
  `015b8ca27d466a5a2fce2660a902bb58a4b62003`.
- [x] Authorized publication metadata reconciliation COMPLETED; ready for Commit 2.
- [ ] USER METADATA COMMIT — COMMIT 2 — exact next gate; does not exist yet.
- [ ] User push — pending / not performed yet.
- [ ] External repaired-publication verification.
- [ ] Separately authorized Phase-3 resumption consideration.

The unchecked items are subsequent lifecycle/audit gates, not remaining repair
requirements. Earlier checklists below retain historical stage meaning.

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

### Historical stage-specific checklist — before final closure/re-freeze

- [x] P3-H01 IMPLEMENTED / FRESHLY VERIFIED / INDEPENDENTLY REVIEWED / PRESERVED.
- [x] P3-H01 focused/full/clean-build and bounded Simulation PASS preserved.
- [x] Localization-before-fusion and Intake/Feeder semantic evidence corrections preserved.
- [x] CFG-H01 bounded design approved and governance amendment registered.
- [x] Separate exact-file CFG-H01 implementation authorization executed.
- [x] Exactly two existing production files changed; zero new source/test files.
- [x] Existing VisionConfigurationAuthorityTest extended; prior P3-H01 assertions preserved.
- [x] Constants owns the table identity; adapter consumes it; private default removed.
- [x] Endpoint, both constructors, IO/protocol/behavior and protected source preserved.
- [x] Fresh User authority guard, unchanged Limelight tests and Vision regressions PASS.
- [x] Fresh User full suite and clean build PASS after CFG-H01.
- [x] Independent exact-delta/static-removal/protected-file review PASS.
- [x] CFG-H01 IMPLEMENTED / VERIFIED / REMOVED.
- [x] Authorized documentation/evidence and appended repair chronology RECONCILED.
- [x] CFG-H01 applicability recorded separately from P3-H01 Simulation evidence.
- [ ] Any separate outstanding earlier applicability/closure gates explicitly disposed.
- [ ] INDEPENDENT FINAL ARCHITECTURE / CLOSURE REVIEW PASS — NEXT.
- [ ] Final repair Transition Guide acceptance recorded after final review.
- [ ] Explicit Architect/User re-freeze and independent freeze review accepted.
- [ ] New repaired primary/metadata identities and User push evidence recorded.
- [ ] External repaired-publication verification accepted.
- [ ] Separately authorized Phase-3 resumption.

Older unchecked registration checklists retain historical planning meaning.
This dated checklist controls current stage status. Runtime applicability
dispositions are decisions, not fresh CFG-H01 runtime PASS claims.


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

### Repair-specific gates — PENDING

The following repair gates are initially unchecked. Original checked items below
belong to the historical lesson, not to this repair.

- [ ] Fresh Java 17 baseline and original-source provenance accepted.
- [ ] Fresh inherited focused/full suite and baseline clean build accepted.
- [ ] Fresh baseline/scope architecture review accepted.
- [ ] Separate exact-file Architect/User implementation authorization recorded.
- [ ] Constants-only default ownership repair implemented within scope.
- [ ] RobotContainer-only named-default injection implemented within scope.
- [ ] One new configuration guard created; zero existing test edits.
- [ ] Independent static-removal and exact changed-file review PASS.
- [ ] Fresh focused unit tests and architecture/configuration guard PASS.
- [ ] Fresh inherited/full suite and clean Gradle build PASS.
- [ ] Fresh WPILib Simulation and Driver Station verification PASS.
- [ ] Glass applicability decision recorded and applicable evidence accepted.
- [ ] Real-hardware applicability decision recorded and applicable evidence accepted.
- [ ] Repair documentation and transition appendix finalized with accepted evidence.
- [ ] Final architecture and independent closure reviews PASS.
- [ ] Explicit Architect/User re-freeze recorded; active count returned to 0.
- [ ] Independent freeze review accepted.
- [ ] User-created new repaired primary snapshot identity recorded.
- [ ] New repair metadata identity and User push evidence recorded.
- [ ] External final repaired-publication verification accepted.
- [ ] Separate Phase-3 resumption authorization after reviewed static removal.

No unchecked gate is satisfied by an original PASS. Phase 4 remains forbidden.

## Historical M00_L16 pre-Commit-2 publication metadata reconciliation — 2026-09-27

- [x] M00_L16 remains COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED; Active M00 Lesson Count is 0 and Current Active M00 Lesson is NONE.
- [x] Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; real hardware remains deferred.
- [x] The initial closure HOLD, bounded README repair, fresh independent closure rereview PASS, and Freeze Reconciliation PASS remain recorded below.
- [x] Independent Freeze Review passed: `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW` / `M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
- [x] User-owned Primary Frozen Snapshot Commit 1 was created at `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`.
- [x] Publication metadata reconciliation is COMPLETE / `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`; prepared for Metadata Publication Commit 2.
- [x] M00_L16 is the final M00 lesson; no M00_L17 is authorized.
- [ ] User-owned Metadata Publication Commit 2 and its hash remain PENDING.
- [ ] Publication push remains PENDING / USER-OWNED.
- [ ] Final external publication verification remains PENDING; M00_L16 is NOT PUBLISHED.

No third verification-only commit is part of the two-commit Historical
Snapshot model.

## Historical M00_L16 freeze reconciliation — 2026-09-27

- [x] M00_L15 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.
- [x] Baseline, inheritance audit, Final Design Lock, controlled activation, independent activation review, and implementation authorization passed.
- [x] Implemented the one locked concept in `RobotContainer.java`: fresh `IntakeToFeederCommand` per `LEARNING_EVENT` dispatch, exact Intake and Feeder requirements.
- [x] Added one eight-test integration file and updated only the inherited architecture guard; 221 other source files remain unchanged.
- [x] Initial independent static review HOLD was cleared by bounded guard repair and fresh independent static rereview.
- [x] User focused tests passed: architecture guard methods and 8/8 integration tests; BUILD SUCCESSFUL, exit 0.
- [x] User clean regression passed: BUILD SUCCESSFUL, all tests shown passed, no regression blocker. No numeric count or exit code supplied.
- [x] Accepted `PASS_M00_L16_BOUNDED_SIMULATION`: Driver Station attached, positive `ONE_METER_WITH_EVENT` semantic requests and Disabled/interruption cleanup, event-free `ONE_METER_PATH` control, no observed fatal scheduler/runtime exception, normal return to PowerShell prompt. No Simulation exit code supplied.
- [x] Blank AutonomousEvent NT fields classified as expected under observation-only publication; no `LastEvent` or dispatch-count success claim.
- [x] Evidence classified THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; no physical performance claim.
- [x] Documentation Reconciliation passed: `PASS_M00_L16_DOCUMENTATION_RECONCILIATION`.
- [x] Initial Independent Closure Review HOLD for the README hierarchy was preserved; bounded repair passed as `PASS_M00_L16_CLOSURE_DOCUMENTATION_REPAIR`.
- [x] Fresh Independent Closure Review passed: `PASS_M00_L16_INDEPENDENT_CLOSURE_REVIEW`; no remaining findings.
- [x] Freeze Reconciliation passed: `PASS_M00_L16_FREEZE_RECONCILIATION`; M00_L16 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED.
- [x] Active M00 Lesson Count is 0; Current Active M00 Lesson is NONE.
- [ ] Independent Freeze Review: PENDING / NEXT GATE.
- [ ] User-owned Git commit, push, and final publication verification: PENDING.

## Historical M00_L16 controlled activation — 2026-09-27

- [x] M00_L15 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED; accepted primary SHA `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`, metadata SHA `0d3685ce67a0b985459392621e003611eaa6dc35`, final gate `PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`.
- [x] M00_L16 untouched-copy baseline passed: `PASS_M00_L16_UNTOUCHED_COPY_BASELINE_BUILD`; User-reported BUILD SUCCESSFUL, exit code 0.
- [x] Architecture / Inheritance Audit passed: `PASS_M00_L16_ARCHITECTURE_INHERITANCE_AUDIT`; 345/345 authored files, 113/113 production Java, and 106/106 test Java identical.
- [x] Final Design Lock passed: `PASS_M00_L16_FINAL_DESIGN_LOCK`.
- [x] Controlled activation is documentation-only; M00_L16 is the sole IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK M00 lesson.
- [x] One concept locked: scheduler-managed dispatch of one verified `IntakeToFeederCommand` through inherited `LEARNING_EVENT`.
- [x] `ShootCommand` excluded to avoid a new RPM caller/configuration question.
- [x] Later production budget is exactly one modified `RobotContainer.java`, zero added production files, with fresh command per dispatch and exact IntakeSubsystem + FeederSubsystem requirements.
- [x] Existing `Supplier<Command>` and `Commands.defer(...)` remain; WPILib requirements own external command contention, and inherited PathPlanner EventScheduler owns event-child lifecycle.
- [x] Event helpers, selected command, mechanism subsystems/IO/Observations/telemetry, AutoBuilder/path factories, Swerve, Vision, pose estimation, Constants, deploy, vendordeps, and Gradle/config remain locked unchanged.
- [x] `LEARNING_EVENT` identity and existing marker/path remain; no new event ID, marker, timing policy, timer, timeout, wrapper, or state machine.
- [x] Right Bumper `RunIntakeCommand whileTrue`, Left Bumper `RunFeederCommand whileTrue`, and chooser `SAFE_STOP` / `ONE_METER_PATH` / `ONE_METER_WITH_EVENT` remain unchanged.
- [x] Normal path completion/interruption chain reaches hold child `end(...)`, stopping Feeder then Intake; arbitrary uncaught library-exception cleanup is not claimed.
- [x] Supplier `RuntimeException` retains `FACTORY_FAILURE` observation and safe no-op without failed-child mechanism actuation.
- [x] Future test scope is exactly one new `src/test/java/frc/robot/RobotContainerMechanismAutonomousEventIntegrationTest.java` with eight `@Test` methods, plus one narrow `src/test/java/frc/robot/IntakeArchitectureBoundaryTest.java` expectation update preserving teleop/default/direct-access guards.
- [x] Future bounded Simulation plan includes `ONE_METER_WITH_EVENT` semantic dispatch/cleanup and event-free `ONE_METER_PATH` control; Noop adapters make physical behavior unverified.
- [x] M00_L16 transition guide is created and in progress; later gates are recorded as pending.
- [ ] Independent Activation Review: PENDING.
- [ ] Implementation Authorization: PENDING; no production implementation is yet authorized.
- [ ] Implementation and Independent Static Review: PENDING.
- [ ] Eight focused tests and clean regression: PENDING / not run for L16.
- [ ] Bounded Simulation / Driver Station: PENDING / not run for L16.
- [ ] Documentation Reconciliation and Independent Closure Review: PENDING.
- [ ] Freeze Reconciliation and Independent Freeze Review: PENDING.
- [ ] User-owned publication: PENDING.

Real hardware remains DEFERRED. L16 has not earned THEORY VERIFIED or
SIMULATION VERIFIED. M00_L15 files remain untouched, and M00_L16 is the
final M00 lesson.

### Exact eight future focused-test obligations

1. `LEARNING_EVENT` exposes exactly Intake and Feeder requirements.
2. Every dispatch creates a fresh `IntakeToFeederCommand` child.
3. Real `CommandScheduler` scheduling starts Intake and Feeder requests.
4. Repeated scheduler runs do not reinitialize one active child.
5. Cancellation/interruption stops Feeder and Intake through child cleanup.
6. Real WPILib scheduler resolves Intake requirement contention.
7. Real WPILib scheduler resolves Feeder requirement contention.
8. Supplier `RuntimeException` with nonempty Intake and Feeder requirements
   takes the `FACTORY_FAILURE` safe-no-op path without mechanism actuation.

## Inherited M00_L15 checklist (historical copy)

The following checked items describe the copied M00_L15 lesson and do not
mark M00_L16 implementation or verification complete.

### Historical M00_L15 freeze and verification checklist — 2026-09-27

- [x] M00_L14 predecessor is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.
- [x] Untouched-copy baseline, Architecture / Inheritance Audit, and Final Design Lock passed.
- [x] Controlled Activation and Independent Activation Review / Rereview passed.
- [x] Implementation Authorization, Implementation Handoff, implementation, and Final Independent Static Review passed.
- [x] The sole production addition is IntakeToFeederCommand.java; it requires exactly IntakeSubsystem and FeederSubsystem.
- [x] RobotContainer retains Right Bumper to RunIntakeCommand and Left Bumper to RunFeederCommand; no IntakeToFeederCommand binding was added.
- [x] Focused tests passed: 22 tests, 22 PASS, 0 failures, 0 errors, 0 skipped.
- [x] Initial clean regression: 830 tests, 2 failures; diagnosis classified both as EXPECTED_INHERITED_TEST_CONTRACT_EVOLUTION, not production defects.
- [x] Inherited architecture test reconciliation changed only current M00_L15 test copies; frozen predecessors remain unchanged.
- [x] Intake / Feeder path scans use .java-only filtering, normalized relative paths, and exact closed sets.
- [x] Feeder owner detection uses the JDK Java parser; architecture scan robustness repair, parser repair, and independent parser review passed.
- [x] Final User clean regression passed: 830 tests, 830 PASS, 0 failures, 0 errors, 0 skipped; BUILD SUCCESSFUL; BUILD_EXIT_CODE=0.
- [x] Bounded Simulation / Driver Station verification passed: Disabled Robot Enabled=No and Intake/Feeder/Flywheel STOPPED; Teleoperated Robot Enabled=Yes, DS Attached=Yes, Intake/Feeder STOPPED; return to Disabled Robot Enabled=No and Intake/Feeder STOPPED; no unintended L15 activation or fatal runtime/scheduler error; BUILD SUCCESSFUL; SIMULATION_EXIT_CODE=0.
- [x] Port 0 joystick button warning was EXPECTED / NON-BLOCKING because the controller was unassigned or unplugged.
- [x] Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; Simulation did not execute IntakeToFeederCommand because RobotContainer has no binding.
- [x] Documentation reconciliation is complete and the transition guide records the accepted chronology.
- [x] Independent Closure Rereview PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW returned M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION with no findings.
- [x] Freeze Reconciliation is COMPLETE; M00_L15 is COMPLETE / FROZEN / READ-ONLY.
- [x] Independent Freeze Rereview passed: PASS_M00_L15_INDEPENDENT_FREEZE_REREVIEW / M00_L15_INDEPENDENT_FREEZE_REREVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW.
- [x] Primary Snapshot Commit 1 is committed under PASS_M00_L15_PRIMARY_SNAPSHOT_COMMIT at 15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa.
- [x] Publication Metadata Reconciliation is COMPLETE and prepared for User-owned Metadata Publication Commit 2.
- [x] Active M00 Lesson Count is 0; Current Active M00 Lesson is NONE; M00_L16 remains FUTURE / INACTIVE / NOT CREATED.
- [ ] Metadata Publication Commit 2 remains PENDING / USER-OWNED; M00_L15 is NOT PUBLISHED.
- [ ] Publication push remains PENDING / USER-OWNED.
- [ ] External Final Publication Verification remains PENDING.

## Inherited M00_L14 checklist (historical copy)

The checklist below is retained from the frozen predecessor copy. Its checked
items document M00_L14 and do not mark M00_L15 work complete.

### Historical M00_L14 frozen lifecycle and publication state — 2026-09-26

- [x] M00_L13 predecessor is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED.
- [x] M00_L14 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED / NOT ACTIVE; accepted User evidence confirms Metadata Publication Commit 2 is complete.
- [x] Active Lesson Count is 0; Current Active M00 Lesson is NONE.
- [x] M00_L15 and M00_L16 remain FUTURE / INACTIVE / NOT CREATED.
- [x] Untouched-copy baseline, Architecture / Inheritance Audit, and Final Design Lock passed.
- [x] Controlled Activation, Independent Activation Re-review, and Implementation Authorization passed.
- [x] Implementation Handoff and final independent static review passed.
- [x] Governance mirror validation PASS (accepted review evidence): 12 VERIFIED mirrors, 12 matching PDF hashes, 12 matching Markdown hashes, and zero deterministic findings.
- [x] User focused tests, clean regression, and bounded Simulation passed.
- [x] Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.
- [x] Initial Independent Closure Review HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED is preserved as a documentation/evidence-language finding only.
- [x] Bounded repair PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION is accepted.
- [x] Independent Closure Rereview PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW returned INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION.
- [x] PASS_M00_L14_DOCUMENTATION_RECONCILIATION and PASS_M00_L14_FREEZE_RECONCILIATION are accepted; the transition guide is finalized.
- [x] Independent Freeze Review passed: PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW / INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION.
- [x] User-owned Primary Frozen Snapshot committed: PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT at fa34556a3f1b7ef52b2c678a39c1083392d7c8d3.
- [x] PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION prepared Commit 2 metadata under the two-commit Historical Snapshot model.
- [x] Resolved HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA by adding the required publication-metadata documentation delta.
- [x] PASS_M00_L14_METADATA_PUBLICATION_COMMIT; Metadata Publication Commit 2 is complete by accepted User evidence, with its current identity external and not self-embedded.
- [x] PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION and PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND; the amendment is complete by accepted User evidence, with canonical identity external and not self-embedded.
- [x] HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED is preserved as a documentation chronology finding and addressed by this reconciliation; external Final Publication Verification remains pending.
- [ ] Remote push remains PENDING / USER-OWNED; no push evidence is supplied.
- [ ] Final Publication Verification remains PENDING. The metadata-state HOLD was resolved by the accepted amendment; the post-amend chronology HOLD is addressed by this documentation reconciliation.

## Locked design and architecture

- [x] Sole concept: scheduler-managed frc.robot.commands.ShootCommand coordinates existing Flywheel readiness and Feeder action.
- [x] Exact requirements: FlywheelSubsystem and FeederSubsystem only; no Elevator, Intake, or Swerve.
- [x] Exact constructor accepts non-null subsystems and finite, positive caller-supplied RPM; invalid numeric input throws IllegalArgumentException; construction causes no output or subsystem mutation.
- [x] Caller RPM is semantic configuration / test input, not a verified physical shooting RPM; Constants.java is unchanged.
- [x] FlywheelObservation.readyAtSpeed() is the sole readiness authority.
- [x] Feed admission requires ready-at-speed and Feeder available and connected.
- [x] FeederObservation.requestedState() is software request state only; there is no command-local feedingRequested authority or physical transport claim.
- [x] Normal initialize stops Feeder once, then requests Flywheel velocity once; execute does not reissue the Flywheel request.
- [x] Normal execute requests feed only on the admitted transition and stops on readiness/admission loss when a feed request is active.
- [x] The command does not self-finish; no timeout, feed duration, shot count, or completion authority is introduced.
- [x] Required exception cleanup preserves the primary RuntimeException, attempts the approved safe stops, suppresses cleanup failures, and does not retry.
- [x] RobotContainer has no ShootCommand binding; the inherited Left Bumper manual Feeder command remains.
- [x] Constants, existing subsystems, IO, Observations, telemetry, vendor adapters, and deploy files remain unchanged.
- [x] No NamedCommands, PathPlanner mechanism events, vision aiming, or L15/L16 behavior is included.

## Implementation and independent static review

- [x] PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION.
- [x] PASS_M00_L14_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW.
- [x] Production addition is only src/main/java/frc/robot/commands/ShootCommand.java; existing production files remain unchanged.
- [x] PASS_M00_L14_FINAL_INDEPENDENT_STATIC_REVIEW.
- [x] Five test-only architecture-guard HOLD/repair rounds are preserved in the status record and transition guide; no production defect was found.
- [x] All 18 focused @Test methods remain.

## Focused tests and clean regression

- [x] PASS_M00_L14_USER_FOCUSED_TESTS: `gradlew test --tests "*ShootCommandTest"`; BUILD SUCCESSFUL in 10s; 4 actionable tasks (3 executed, 1 up-to-date); FOCUSED_TEST_EXIT_CODE=0.
- [x] Constructor validation, exact requirements, and unchanged target pass-through are covered.
- [x] Ready/unready behavior, Feeder availability/connection admission, and transition deduplication are covered.
- [x] Readiness loss/recovery and Feeder state transitions are covered.
- [x] Initialize Feeder-stop failure and Flywheel-request failure cleanup are covered.
- [x] requestFeed failure cleanup and transition-stop failure cleanup are covered.
- [x] Direct `end(true)` invocation unit-tests interrupted-end cleanup semantics; actual `CommandScheduler` scheduling/cancellation integration is not tested or proven.
- [x] Architecture and scope guards cover sole readiness, Observation access, direct semantic API use, no duplicate control state, and L15/L16 boundaries.
- [x] PASS_M00_L14_USER_CLEAN_REGRESSION: `gradlew clean test`; BUILD SUCCESSFUL in 30s; 5 actionable tasks (5 executed); CLEAN_REGRESSION_EXIT_CODE=0.

## Bounded Simulation evidence

- [x] PASS_M00_L14_USER_BOUNDED_SIMULATION.
- [x] Disabled startup observed: Robot Disabled, not enabled, DS attached, not E-stopped.
- [x] Teleoperated enable observed: Robot Teleoperated, enabled, DS attached, not E-stopped; no application crash or scheduler/runtime exception observed.
- [x] Return to Disabled observed with Feeder, Flywheel, and Intake RequestedState STOPPED.
- [x] Normal Simulation shutdown completed with LAST_NATIVE_EXIT_CODE=0.
- [x] Scope limitation recorded: RobotContainer is unchanged and has no ShootCommand binding, so Simulation did not directly execute ShootCommand.
- [x] Simulation supports startup, mode transitions, scheduler/integration stability, safe semantic mechanism state, and clean shutdown only; no physical shot or hardware performance is claimed.
- [x] Runtime Flywheel and Feeder adapters are Noop; real hardware remains DEFERRED.

## Lifecycle gates for Metadata Publication Commit 2

- [x] Independent Closure Rereview passed after the documented HOLD and repair.
- [x] M00_L14 became COMPLETE / FROZEN / READ-ONLY through PASS_M00_L14_FREEZE_RECONCILIATION.
- [x] Independent Freeze Review passed; Primary Frozen Snapshot Commit 1 completed at the accepted SHA.
- [x] User-owned Metadata Publication Commit 2 completed; it records M00_L14 as COMPLETE / FROZEN / READ-ONLY / PUBLISHED.
- [x] Metadata Publication Commit 2 amendment completed by accepted User evidence; canonical identity remains external and is not self-embedded.
- [ ] Remote push remains PENDING / USER-OWNED.
- [ ] Final Publication Verification remains PENDING / EXTERNAL; publication chronology reconciliation is complete, and independent final verification remains pending.

The metadata commit's own final SHA cannot be self-embedded; external
publication evidence must establish it. Commit 2 is the publication point,
and no third verification-only commit is part of the model. Evidence remains
THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.
