<!-- ACM-11 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-11 formal domain closure — 2026-10-02

The Architect reviewed Sol's independent ACM-11 Configuration Authority audit and explicitly authorized **ACM-11 FORMAL DOMAIN CLOSURE**. Accepted audit token: `PASS_ACM_11_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. **ACM-11 is FORMALLY CLOSED / FORMALLY RECORDED: 55 / 55 dimensions CLOSED; 0 BLOCKED. ACM-11-F01: NOT ESTABLISHED. NO ADDITIONAL ACM-11 FINDING ESTABLISHED.** No repair or ACM-11 repair ADR was established. This records domain closure only; it does not close M00_L16 or Phase 3.

Static governance validator PASS: 12 authoritative English source PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was NOT PERFORMED.

### Accepted configuration authority model

`Constants.java` is the default authority for stable robot-specific and lesson-approved configuration. It does not own every project constant. Robot configuration belongs there or to another explicit governed configuration owner; implementation details remain with their consumers; runtime state remains in subsystem, IOInputs, Observation, or other runtime owners; simulation and test fixtures remain with their fixture owners. Protocol strings, telemetry keys, internal numeric tolerances, vendor conversions, local algorithm details, and synthetic simulation values do not need to move into `Constants.java` solely because they are constants. No contradictory current production configuration authority was established.

### Current Swerve configuration

| Module | Drive CAN | Steer CAN | CANcoder | Drive inverted | Steer inverted | CANcoder offset (rotations) | Encoder direction |
| --- | ---: | ---: | ---: | --- | --- | ---: | --- |
| Front Left | 21 | 22 | 23 | false | true | +0.068603515625 | CounterClockwise_Positive |
| Front Right | 24 | 25 | 26 | true | true | +0.014404296875 | CounterClockwise_Positive |
| Back Left | 27 | 28 | 29 | false | true | +0.46240234375 | CounterClockwise_Positive |
| Back Right | 30 | 31 | 32 | true | true | -0.057373046875 | CounterClockwise_Positive |

Pigeon2 CAN ID is 20. All drive-position signs are +1. Wheel diameter is 0.1016 m and radius is 0.0508 m; drive ratio is 6.75:1 and steer ratio is 15.42857142857143:1. Wheelbase and track width are each 0.5461 m. Drive supply-current limit is 70 A enabled; steer stator-current limit is 60 A enabled. Drive Slot 0 is `kP=0.675`, `kI=0.0`, `kD=0.0`, `kS=0.15`, `kV=0.837`, `kA=0.0`; this remains a **PROVISIONAL COMMISSIONING BASELINE**, not final tuning. Steer Slot 0 is `kP=100`, `kI=0`, `kD=0.5`; no steer feedforward value is asserted.

The activation brief's earlier CANcoder offsets are superseded historical evidence. A later tracked, user-authoritative Phoenix Tuner X recalibration establishes the current offsets in the table. Current `Constants.java`, CTRE construction/configuration, and current tests use those later values. No ACM-11 defect exists from the difference, and no source value was changed.

### Other current configuration decisions

- Driver configuration: Xbox port 0, deadband 0.10; forward `-LeftY`, strafe `-LeftX`, rotation `-RightX`. Signed-square shaping and semantic axis mapping remain with driver-input behavior. No competing authority exists.
- Autonomous starting pose, lesson targets, motion constraints, tolerances, and timeouts are owned by the named autonomous/trajectory configuration groups or explicit command inputs. No competing global production default was established. The tracked hardware commissioning record identifies the CAN bus as `rio`; current CTRE constructors use the vendor default bus and no competing production bus authority was found.
- PathPlanner: the RobotConfig derives wheel radius, gearing, drive-current limit, and module geometry from Swerve configuration. Its mass 45 kg, MOI 5 kg·m², maximum drive speed 4 m/s, and COF 1.0 remain provisional. Current path-asset copies are validated against governed path configuration before use; assets are not the primary robot authority.
- Vision: Limelight table `limelight`; robot-to-camera x=-0.038 m, y=+0.050 m, z=+0.114 m, pitch=-20°, roll=0°, yaw=0°. Qualification uses 1/2/3 m bands and 0.250 s freshness. No project-specific estimator covariance/std-dev configuration is defined, so WPILib constructor defaults apply; qualification thresholds are not estimator covariance tuning.
- Mechanisms: Flywheel readiness tolerance is 50 RPM and PROVISIONAL. Intake and Feeder use semantic requests. Elevator limits and homing configuration are explicit constructor inputs where applicable. No absent physical mechanism value is invented.
- Wheel diameter and radius are separately declared but currently satisfy radius = diameter / 2; that is a future maintenance consideration, not a current defect. Geometry consumers derive from governed dimensions. Other repeated values serve different purposes and do not create competing current authority.
- Local implementation details include module count/indexes, localization history window, vendor/readback tolerances, CTRE Slot selector, circumference derivation, Limelight JSON field names, simulation latency/targets, PathPlanner comparison tolerance, telemetry keys/sentinels, `LEARNING_EVENT`, constructor-supplied semantic parameters, and UI chooser labels.
- No mutable public/static robot-configuration store, dashboard/NetworkTables hidden configuration authority, Preferences/property loader, or telemetry-derived configuration was established. Choosers remain explicit selections. Provisional/learning status remains in force for drive gains, PathPlanner model values, marked learning pose/field selections, temporary learning limits, Flywheel readiness, and other explicitly provisional values.

### ACM-11 post-audit closure matrix — 55/55 CLOSED

| # | Dimension | Status | # | Dimension | Status |
| ---: | --- | --- | ---: | --- | --- |
| 1 | Governance authority | CLOSED | 2 | Complete configuration inventory | CLOSED |
| 3 | Configuration authority map | CLOSED | 4 | Constants.java classification | CLOSED |
| 5 | Duplicate configuration sweep | CLOSED | 6 | Robot-specific magic-value sweep | CLOSED |
| 7 | Unit clarity | CLOSED | 8 | Swerve wheel-size authority | CLOSED |
| 9 | Swerve drive-ratio authority | CLOSED | 10 | Swerve steer-ratio authority | CLOSED |
| 11 | Swerve geometry authority | CLOSED | 12 | Module CAN-ID authority | CLOSED |
| 13 | Module inversion authority | CLOSED | 14 | CANcoder-offset authority | CLOSED |
| 15 | Encoder-direction authority | CLOSED | 16 | Gyro configuration authority | CLOSED |
| 17 | Drive PID authority | CLOSED | 18 | Drive feedforward authority | CLOSED |
| 19 | Steer control authority | CLOSED | 20 | Current-limit authority | CLOSED |
| 21 | Driver controller-port authority | CLOSED | 22 | Driver deadband authority | CLOSED |
| 23 | Driver shaping/scaling authority | CLOSED | 24 | Autonomous starting-pose authority | CLOSED |
| 25 | Autonomous target/constraint authority | CLOSED | 26 | PathPlanner configuration authority | CLOSED |
| 27 | PathPlanner/drivetrain consistency | CLOSED | 28 | Vision camera-transform authority | CLOSED |
| 29 | Vision qualification-threshold authority | CLOSED | 30 | Vision estimator-trust authority | CLOSED |
| 31 | Intake configuration authority | CLOSED | 32 | Feeder configuration authority | CLOSED |
| 33 | Flywheel configuration authority | CLOSED | 34 | Elevator configuration authority | CLOSED |
| 35 | Real/Simulation configuration distinction | CLOSED | 36 | Hardware-bus configuration | CLOSED |
| 37 | Protocol/telemetry-key classification | CLOSED | 38 | Event-name classification | CLOSED |
| 39 | Derived-value consistency | CLOSED | 40 | static-final sweep | CLOSED |
| 41 | Numeric-literal sweep | CLOSED | 42 | String-configuration sweep | CLOSED |
| 43 | Boolean/enum configuration sweep | CLOSED | 44 | IO-adapter configuration ownership | CLOSED |
| 45 | RobotContainer configuration ownership | CLOSED | 46 | Subsystem configuration ownership | CLOSED |
| 47 | Command configuration ownership | CLOSED | 48 | Test-copy distinction | CLOSED |
| 49 | Documentation-copy consistency | CLOSED | 50 | Provisional/final distinction | CLOSED |
| 51 | Mutable/tunable configuration | CLOSED | 52 | Current/historical distinction | CLOSED |
| 53 | ACM-01–ACM-10 preservation | CLOSED | 54 | ACM-12 deferral / Frozen Backbone sanity | CLOSED |
| 55 | Remaining ACM-11 work | CLOSED |  |  |  |

ACM-01 through ACM-10 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED. Latest published checkpoint: `d6bdc6f1fef24239d7b5c0802f29453d9ce1a24f`, annotated tag `audit-acm-10-closed`; the User reports `origin/main` and the remote peeled tag target verified at that commit, and local HEAD/origin/main/tag target were revalidated here. **ACM-12 is NEXT PROSPECTIVE DOMAIN / NOT STARTED / NOT ACTIVATED.** Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17.

This formal closure recording changes exactly the eight authorized lifecycle documents. No Java, tests, Gradle files, configuration values, protected/unrelated content, or other tracked files were modified. No Git write or project execution occurred; the index was empty before recording. The protected hardware registry was not opened. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED. The exact next gate is the User-owned ACM-11 Git checkpoint; ACM-12 remains unactivated.
<!-- ACM-11 DOMAIN CLOSURE CURRENT END -->

<!-- ACM-10 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-10 formal domain closure — 2026-10-02

The Architect reviewed Sol's independent initial audit and explicitly authorized **ACM-10 FORMAL DOMAIN CLOSURE**. Accepted audit token: PASS_ACM_10_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW. **ACM-10 is FORMALLY CLOSED / FORMALLY RECORDED: 50 / 50 dimensions CLOSED; 0 BLOCKED. ACM-10-F01: NOT ESTABLISHED. NO ADDITIONAL ACM-10 FINDING ESTABLISHED.** The dimension-by-dimension record is included in this ACM-10 closure block.

The static governance validator passed: 12 authoritative English source PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was NOT PERFORMED.

RobotContainer is the production composition root and Real/Simulation selection authority; WPILib RobotBase is the runtime environment signal. RobotBase.isReal() governs RobotContainer's Vision and Swerve construction: Real selects four SwerveModuleIOCTRE adapters, GyroIOPigeon2, and VisionIOLimelight; otherwise it selects four SwerveModuleIOSim adapters with shared SwerveSimulationState and GyroIOSim, plus VisionIOSim and its simulation-only VisionIOSimHarness/fixture chooser. Both modes inject the same project IO contracts into the same subsystems. The fixture changes simulated Vision input only; it neither swaps adapters nor writes estimator state. No hidden production mode-selection branch or hardware adapter reachable from the selected Simulation graph was established.

IntakeIONoop, FeederIONoop, FlywheelIONoop, and ElevatorIONoop are used in both modes. These are unavailable, non-actuating implementations; no current mechanism hardware or mechanism physics simulation is claimed. Real and Simulation paths retain the same command, observation, telemetry, localization, driver-input, autonomous, and named-event architecture. SwerveSubsystem remains the localization owner. No numerical or physical Simulation fidelity is claimed for battery, current, thermal, traction, or controller dynamics.

Static test review identified RobotSimulationHarnessCompositionTest, VisionIOSimHarnessTest, Swerve/Gyro simulation tests, and mechanism Noop tests. No tests were executed during the ACM-10 audit. There is no paired Real-mode RobotContainer test; its absence was not established as a defect.

RobotContainer selects concrete adapters once at construction, creates one subsystem graph, and keeps adapter identity fixed for that graph's lifetime. Robot owns lifecycle and does not select IO. Subsystems consume the shared project IO contracts, while commands use subsystem semantic APIs; both are mode-agnostic. Hardware adapters are confined to the Real-selected graph, and simulation adapters and the harness to the Simulation-selected graph.

RobotContainer.runSimulationHarness() applies the selected Vision fixture only when RobotBase.isSimulation(); that gate does not choose adapters. VisionIOSim flows through VisionSubsystem qualification and the existing fusion handoff to SwerveSubsystem's estimator, while simulated modules and gyro feed that same localization owner. Telemetry reads shared project observations, and driver input, autonomous, and named events retain their existing shared controller, scheduler, and command paths. Intake and Feeder Noop IOs do not model physical mechanism movement.

ACM-01 through ACM-09 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED. The latest published checkpoint is c4824c255eae5835ebf6c51505a95899f95d0b35, parent a932931ae674817b4fb994cba8cfe2ef2591db98, annotated tag audit-acm-09-closed. The User reports origin/main and the remote peeled tag target verified at that commit; the local HEAD and origin/main refs also resolve there. **ACM-11 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED**; ACM-12 remains NOT STARTED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. This closure does not authorize an ACM-10 checkpoint, tag, push, ACM-11 activation, Constants.java cleanup/refactor, re-freeze, publication, or Phase 4.

This formal closure recording changes only the eight authorized lifecycle documents. No Java source, tests, or other protected content was changed. No Git write or project execution occurred; the index remains empty. Existing protected/unrelated worktree state was left untouched. github-recovery-codes.txt: NOT ACCESSED / NOT MODIFIED. The next gate is the User-owned ACM-10 Git checkpoint.
### ACM-10 formal domain closure matrix — 50/50 CLOSED

| # | Dimension | Status |
| ---: | --- | --- |
| 1 | Governance authority | CLOSED |
| 2 | Real/Sim selection inventory | CLOSED |
| 3 | Environment-detection authority | CLOSED |
| 4 | Composition-root selection authority | CLOSED |
| 5 | RobotBase.isReal boundary | CLOSED |
| 6 | Swerve mode selection | CLOSED |
| 7 | Swerve hardware isolation | CLOSED |
| 8 | Swerve simulation isolation | CLOSED |
| 9 | Vision mode selection | CLOSED |
| 10 | Vision hardware isolation | CLOSED |
| 11 | Vision simulation isolation | CLOSED |
| 12 | Intake mode selection | CLOSED |
| 13 | Feeder mode selection | CLOSED |
| 14 | Flywheel mode selection | CLOSED |
| 15 | Elevator mode selection | CLOSED |
| 16 | Same IO contract across modes | CLOSED |
| 17 | Subsystem mode ignorance | CLOSED |
| 18 | Command mode ignorance | CLOSED |
| 19 | Robot role | CLOSED |
| 20 | RobotContainer role | CLOSED |
| 21 | simulationPeriodic ownership | CLOSED |
| 22 | Simulation state ownership | CLOSED |
| 23 | Simulation fixture chooser boundary | CLOSED |
| 24 | Dashboard simulation-input boundary | CLOSED |
| 25 | Real hardware construction | CLOSED |
| 26 | Simulation adapter construction | CLOSED |
| 27 | Noop/fallback adapter ownership | CLOSED |
| 28 | Mode-selection lifetime | CLOSED |
| 29 | Duplicate-composition avoidance | CLOSED |
| 30 | Real/Sim API parity | CLOSED |
| 31 | Safe simulation writes | CLOSED |
| 32 | Safe real writes | CLOSED |
| 33 | Sensor-input parity | CLOSED |
| 34 | Vision simulation path parity | CLOSED |
| 35 | Localization simulation path parity | CLOSED |
| 36 | Simulation telemetry boundary | CLOSED |
| 37 | Driver-input simulation path | CLOSED |
| 38 | Autonomous simulation path | CLOSED |
| 39 | Named-event simulation path | CLOSED |
| 40 | Hardware-only API sweep | CLOSED |
| 41 | Simulation-only API sweep | CLOSED |
| 42 | Static mode-branch sweep | CLOSED |
| 43 | Startup failure semantics | CLOSED |
| 44 | Test evidence | CLOSED |
| 45 | Current/historical distinction | CLOSED |
| 46 | ACM-01–ACM-09 preservation | CLOSED |
| 47 | ACM-11 deferral | CLOSED |
| 48 | ACM-12 deferral | CLOSED |
| 49 | Frozen Backbone sanity | CLOSED |
| 50 | Remaining ACM-10 work | CLOSED |
<!-- ACM-10 DOMAIN CLOSURE CURRENT END -->

<!-- ACM-09 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-09 formal domain closure — 2026-10-02

The Architect reviewed Sol's independent post-repair domain rereview and explicitly authorized **ACM-09 FORMAL DOMAIN CLOSURE**. Accepted rereview token: `PASS_ACM_09_POST_REPAIR_REREVIEW_READY_FOR_ARCHITECT_DOMAIN_CLOSURE`. **ACM-09 is FORMALLY CLOSED / FORMALLY RECORDED: 55 / 55 dimensions CLOSED; 0 BLOCKED. ACM-09-F01 is CLOSED / REPAIRED / USER VERIFIED / INDEPENDENTLY REVIEWED / ARCHITECT REPAIR CLOSURE AUTHORIZED. ACM-09-F02 is NOT ESTABLISHED. NO ADDITIONAL ACM-09 FINDING ESTABLISHED.** The detailed 55-dimension matrix is recorded in the current ACM-09 closure block in AGENTS.md. Earlier F01 repair-closure and ACM-08 cursor wording below remains stage evidence and is superseded where it describes ACM-09 as pending or ACM-09 as the next domain.

SwerveSubsystem is the single drivetrain localization owner: it owns the captured heading reference, odometry O, pose estimator E, updates and recovery, known-field-pose reset, heading synchronization, vision admission, and authoritative estimated pose. O remains the secondary raw localization state; E is the authoritative fused field pose and may differ from O. Successful heading capture preserves O and E independently, prepares both replacement trackers with the new adjusted heading and current module positions before committing the new reference, advances the vision history/reset barrier, refreshes the localization observation coherently, and preserves field-relative behavior. Invalid required input fails closed; capture before tracker initialization creates no placeholder trackers, and later initialization uses the captured reference. A later rejected autonomous known-pose reset no longer leaves a mixed localization frame.

VisionSubsystem owns acquisition and qualification; VisionFusionCoordinator coordinates qualified-measurement handoff; only SwerveSubsystem mutates the estimator. Robot orders scheduler, fusion, and telemetry; RobotContainer wires dependencies. AutoBuilder reads estimated pose E and delegates reset to SwerveSubsystem. Pose-targeted and path-following commands consume E; validation/commissioning uses its specified module state. Telemetry is read-only and publishes O and E separately; Field2d displays O observationally.

Architect F01 adjudication accepted the original finding and authorized its bounded SwerveSubsystem repair. Lifecycle evidence: initial finding `HOLD_ACM_09_NEW_FINDING_ACM_09_F01_READY_FOR_ARCHITECT_REVIEW`; bounded repair `PASS_ACM_09_F01_BOUNDED_REPAIR_IMPLEMENTED_READY_FOR_USER_VERIFICATION`; User focused suites `SwerveSubsystemKnownFieldPoseResetTest`, `SwerveSubsystemPoseEstimatorTest`, and `SwerveSubsystemFieldRelativeTest` PASS with `--rerun-tasks`; full M00_L16 build BUILD SUCCESSFUL in 12s, 6 actionable tasks (2 executed, 4 up-to-date), so this does not claim every task was freshly executed; independent repair review `PASS_ACM_09_F01_INDEPENDENT_REVIEW_READY_FOR_ARCHITECT_REPAIR_CLOSURE`; repair-closure reconciliation `PASS_ACM_09_F01_REPAIR_CLOSURE_RECORDED_READY_FOR_DOMAIN_REREVIEW`.

Static governance preflight PASS: 12 authoritative English PDFs, 12 matching hashes, 12 trust checks, zero deterministic findings. Semantic-fidelity certification was NOT PERFORMED. ACM-01 through ACM-08 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED; latest published checkpoint recorded by the User is `a932931ae674817b4fb994cba8cfe2ef2591db98` (`audit-acm-08-closed`). **ACM-10 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED**; ACM-11 and ACM-12 remain NOT STARTED. Phase 3 remains IN_PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. This domain closure does not close the lesson or authorize re-freeze, publication, Constants.java cleanup/refactor, or Phase 4.

This recording changes exactly the eight established lifecycle documents. The two Java repair files and their existing diff remain byte-for-byte unchanged and uncommitted. No other tracked file was modified by this recording; protected and unrelated worktree state was left untouched. No Git write or project execution occurred; the index remains empty. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED. The exact next gate is the User-owned ACM-09 Git checkpoint; staging, commit, annotated tag, and push remain pending.


### ACM-09 post-repair domain closure matrix — 55/55 CLOSED

| # | Dimension | Status |
| ---: | --- | --- |
| 1 | Governance authority | CLOSED |
| 2 | Localization inventory | CLOSED |
| 3 | Authoritative pose owner | CLOSED |
| 4 | Odometry ownership | CLOSED |
| 5 | Pose-estimator ownership | CLOSED |
| 6 | Single-writer localization model | CLOSED |
| 7 | Odometry/estimator consistency | CLOSED |
| 8 | Gyro contribution | CLOSED |
| 9 | Heading-reference ownership | CLOSED |
| 10 | Module-position source | CLOSED |
| 11 | Localization update cadence | CLOSED |
| 12 | Vision-fusion ownership | CLOSED |
| 13 | Robot-level fusion coordination | CLOSED |
| 14 | Vision validation ownership | CLOSED |
| 15 | Duplicate/stale vision protection | CLOSED |
| 16 | Localization reset inventory | CLOSED |
| 17 | Autonomous starting-pose reset | CLOSED |
| 18 | Alliance/field transform ownership | CLOSED |
| 19 | PathPlanner pose supplier | CLOSED |
| 20 | PathPlanner reset consumer | CLOSED |
| 21 | Pose-targeted command consumption | CLOSED |
| 22 | Path-following pose consumption | CLOSED |
| 23 | Validation/commissioning consumption | CLOSED |
| 24 | Telemetry pose source | CLOSED |
| 25 | Field2d pose source | CLOSED |
| 26 | Observation/read-model ownership | CLOSED |
| 27 | Pose/collection aliasing | CLOSED |
| 28 | Raw-vs-estimated pose distinction | CLOSED |
| 29 | Localization exception behavior | CLOSED |
| 30 | Invalid sensor handling | CLOSED |
| 31 | Reset mode/lifecycle gating | CLOSED |
| 32 | RobotContainer role | CLOSED |
| 33 | Robot role | CLOSED |
| 34 | VisionSubsystem role | CLOSED |
| 35 | SwerveSubsystem role | CLOSED |
| 36 | Command reset authority | CLOSED |
| 37 | Static estimator-mutator sweep | CLOSED |
| 38 | Static pose-field sweep | CLOSED |
| 39 | Vision transport distinction | CLOSED |
| 40 | Simulation localization ownership | CLOSED |
| 41 | Test evidence | CLOSED |
| 42 | Current/historical distinction | CLOSED |
| 43 | ACM-01 preservation | CLOSED |
| 44 | ACM-02 preservation | CLOSED |
| 45 | ACM-03 preservation | CLOSED |
| 46 | ACM-04 preservation | CLOSED |
| 47 | ACM-05 preservation | CLOSED |
| 48 | ACM-06 preservation | CLOSED |
| 49 | ACM-07 preservation | CLOSED |
| 50 | ACM-08 preservation | CLOSED |
| 51 | ACM-10 deferral | CLOSED |
| 52 | ACM-11 deferral | CLOSED |
| 53 | ACM-12 deferral | CLOSED |
| 54 | Frozen Backbone sanity | CLOSED |
| 55 | Remaining ACM-09 work | CLOSED |
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

# AGENTS.md
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

### ACM-07 post-repair closure matrix — 45/45 CLOSED

1. Governance authority — CLOSED
2. Actuator subsystem inventory — CLOSED
3. Subsystem safe-stop APIs — CLOSED
4. IO safe-stop contracts — CLOSED
5. Concrete adapter stop behavior — CLOSED
6. Noop stop behavior — CLOSED
7. Simulation stop behavior — CLOSED
8. Command end(false) behavior — CLOSED
9. Command end(true) behavior — CLOSED
10. Teleop drive termination — CLOSED
11. RunIntake stop behavior — CLOSED
12. RunFeeder stop behavior — CLOSED
13. IntakeToFeeder multi-subsystem stop — CLOSED
14. ShootCommand multi-subsystem stop — CLOSED
15. HomeElevator stop/failure behavior — CLOSED
16. Swerve autonomous command stop behavior — CLOSED
17. Autonomous abort paths — CLOSED
18. AutonomousSafetyHold behavior — CLOSED
19. Path-following completion/interruption — CLOSED
20. Commissioning command cancellation — CLOSED
21. DriveThreeMeterValidation termination — CLOSED
22. Pose/heading reset non-motion classification — CLOSED
23. No-op/fallback paths — CLOSED
24. Default-command handoff — CLOSED
25. Trigger-release behavior — CLOSED
26. Disabled/mode transition behavior — CLOSED
27. Robot-level failure boundary — CLOSED
28. Subsystem requested-state consistency after stop — CLOSED
29. Persistent-output sweep — CLOSED
30. Multi-subsystem partial-stop sweep — CLOSED
31. Invalid/unavailable sensor-state handling — CLOSED
32. Vision-loss applicability — CLOSED
33. Cleanup/decorator semantics — CLOSED
34. Repeating/hold semantics — CLOSED
35. Stop idempotence / repeated-stop software guarantee — CLOSED
36. Test-only distinction / evidence coverage — CLOSED
37. Historical/current distinction — CLOSED
38. ACM-01 preservation — CLOSED
39. ACM-02 preservation — CLOSED
40. ACM-03 preservation — CLOSED
41. ACM-04 preservation — CLOSED
42. ACM-05 preservation — CLOSED
43. ACM-06 preservation — CLOSED
44. Frozen Backbone regression check — CLOSED
45. Remaining ACM-07 work — CLOSED
<!-- ACM-07 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-06 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-06 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-06 — Command Semantics + CommandScheduler Requirements — after Sol's independent read-only audit token `PASS_ACM_06_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`. The Architect owns the closure decision; Sol supplied independent evidence. **ACM-06: FORMALLY CLOSED / FORMALLY RECORDED. All forty audit dimensions are CLOSED. ACM-06-F01: NOT ESTABLISHED.** No repair or repair ADR is required.

Governance preflight PASS: 12 authoritative source PDFs, 12 matching hashes, zero deterministic findings; semantic fidelity certification was not performed. The audit inventoried all twenty current production `Command` subclasses: nineteen declare exact requirements for the subsystem semantic state they change; `AutonomousEventDemonstrationCommand` is the sole zero-requirement command and changes no subsystem state. No under-claim or materially incorrect over-claim was established. Commands use subsystem APIs and project observations, with no direct vendor API, concrete adapter, or mutable IOInputs access.

Production direct `CommandScheduler` calls remain in Robot lifecycle integration (`run`, autonomous schedule, Test-mode `cancelAll`); `teleopInit` cancels the autonomous command through command lifecycle semantics. No scheduler polling, manual ownership flag/lock, or dynamic ownership transfer is used for arbitration. The Swerve default and active bindings carry their subsystem requirements. `LEARNING_EVENT` supplies a fresh `IntakeToFeederCommand` with an exact Intake + Feeder deferred requirement set. Autonomous command compositions preserve Swerve ownership; stop/output safety beyond requirement ownership remains ACM-07. Existing requirement and scheduling tests were reviewed but not run.

ACM-01 through ACM-05 remain FORMALLY CLOSED / CHECKPOINTED / PUSHED / ANNOTATED TAGGED. Their existing checkpoint identities remain: `88b36ad22560e5bf08f1dc1365bed86efaaa68b8` (`audit-acm-01-closed`), `25b01017f2a854b1370c192729cc3c63beaab930` (`audit-acm-02-closed`), `849dc94061e29b80cf75e199bfc231659e2551c5` (`audit-acm-03-closed`), `1a165797cfe7a094c6df2ec0bb4bc7bec37a2255` (`audit-acm-04-closed`), and `6c125c2490c50b9f2c0151379307ac72d10c0b22` (`audit-acm-05-closed`). No regression was established through ACM-05. ACM-07 is NEXT PROSPECTIVE / NOT STARTED / NOT ACTIVATED; ACM-08 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; no M00_L17. No Constants.java cleanup/refactor is authorized. The User subsequently checkpointed, pushed, and annotated-tagged ACM-06 at `58549f11989d2198378a38479228663a6b6b7613` (`audit-acm-06-closed`); the User verified the remote main and annotated tag target at that commit. This is documentation-only; no project execution or Git write occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

<!-- ACM-06 DOMAIN CLOSURE CURRENT END -->
<!-- ACM-05 DOMAIN CLOSURE CURRENT BEGIN -->
## Current ACM-05 formal domain closure — 2026-10-01

The Architect explicitly authorized formal closure of ACM-05 — Observation / IOInputs Data Flow — following Sol's independent read-only audit. Sol's accepted audit token is:
`PASS_ACM_05_INITIAL_AUDIT_READY_FOR_ARCHITECT_DOMAIN_REVIEW`
The Architect owns the closure decision; Sol supplied independent evidence.

**ACM-05: FORMALLY CLOSED / FORMALLY RECORDED.** All thirty-one audit dimensions are CLOSED. **ACM-05-F01: NOT ESTABLISHED.** No current IOInputs/observation data-flow defect was established, no repair is required, and no repair ADR was created.

Earlier ACM-01–ACM-04 blocks below preserve their closure-stage evidence; their former next-domain cursor wording predates this ACM-05 closure and does not control the current cursor.

Static governance preflight PASS: 12 authoritative source PDFs, 12 matching source hashes, and zero deterministic findings. This deterministic result does not certify semantic fidelity. Documents A/B/C and their applicable VERIFIED mirrors establish the intended one-way path: IO / hardware / simulation → mutable IOInputs transport → subsystem ownership and interpretation → immutable project observation/read model → project consumers.

The seven current production IO families and transport ownership are:

| IO contract | Transport payload | Subsystem-owned instance(s) and consumer | Contract writes | Result |
| --- | --- | --- | --- | --- |
| `SwerveModuleIO` | Drive/steer output, position, velocity, electrical/temperature measurements, encoder measurements, connection and configuration-health flags | Four `SwerveModuleIOInputs` in `SwerveSubsystem` | Drive/steer output and velocity, steer angle, characterization completion, stop | `SwerveObservation.ModuleObservation`; CORRECT |
| `GyroIO` | Yaw/pitch/roll, three angular rates, connection and configuration-health flags | One `GyroIOInputs` in `SwerveSubsystem` | None | `SwerveObservation.GyroObservation`; CORRECT |
| `VisionIO` | Availability, connection, sample/timing validity, receive timestamp, latency, ordered target values | One `VisionIOInputs` in `VisionSubsystem` | None | `VisionObservation` and optional `QualifiedVisionMeasurement`; CORRECT |
| `IntakeIO` | Availability and connection | One `IntakeIOInputs` in `IntakeSubsystem` | Intake request, stop | `IntakeObservation`; CORRECT |
| `FeederIO` | Availability and connection | One `FeederIOInputs` in `FeederSubsystem` | Feed request, stop | `FeederObservation`; CORRECT |
| `FlywheelIO` | Availability, connection, velocity validity and RPM | One `FlywheelIOInputs` in `FlywheelSubsystem` | Velocity request, stop | `FlywheelObservation`; CORRECT |
| `ElevatorIO` | Availability, connection, position validity/reference and position in meters | One `ElevatorIOInputs` in `ElevatorSubsystem` | Position request, homing request, stop | `ElevatorObservation`; CORRECT |

Each subsystem creates and retains its Inputs instance(s). Concrete IO implementations receive these mutable objects only as `updateInputs` targets. No adapter retention/re-export, second architectural owner, or cross-subsystem shared mutable Inputs was established. Each subsystem periodic path refreshes before constructing its current public observation. Mechanism request/stop methods may rebuild an observation from new software intent and the most recently sampled sensor state; this is expected retained state, not a periodic-order defect.

The fourteen current top-level observation/read-model records are `SwerveObservation`, `VisionObservation`, `QualifiedVisionMeasurement`, `VisionTiming`, `VisionMeasurementQuality`, `VisionFusionObservation`, `IntakeObservation`, `FeederObservation`, `FlywheelObservation`, `ElevatorObservation`, `DriverInputObservation`, `DriveThreeMeterValidationObservation`, `AutonomousEventObservation`, and `AutonomousPreparationObservation`. Their nested Swerve module/gyro/pose records and Vision `TargetObservation` remain with their parent. `ElevatorRequestedState` and `VisionMeasurementQualityEvaluator.Policy` are not observations.

SwerveModuleIOCTRE / SwerveModuleIOSim / SwerveModuleIONoop → private module Inputs → `SwerveSubsystem` → copied module values in `SwerveObservation`. GyroIOPigeon2 / GyroIOSim / GyroIONoop → private gyro Inputs → `SwerveSubsystem` → `SwerveObservation.GyroObservation`. A separate front-left refresh during Test-mode steer commissioning is consumed inside `SwerveSubsystem` to calculate a steer request; it does not expose Inputs, and the public Swerve observation remains the complete periodic snapshot.

VisionIOLimelight / VisionIOSim → private `VisionIOInputs` → `VisionSubsystem` → `VisionObservation` / optional `QualifiedVisionMeasurement` → project consumers. Vision targets are converted into new target observations; the observation owns an independent copied list and independently constructed target values. Timing and validity remain project-facing; Limelight frame-index behavior stays in the adapter. Raw vendor frame objects do not escape.

IntakeIO → IntakeIOInputs → IntakeSubsystem → IntakeObservation; FeederIO → FeederIOInputs → FeederSubsystem → FeederObservation; FlywheelIO → FlywheelIOInputs → FlywheelSubsystem → FlywheelObservation; ElevatorIO → ElevatorIOInputs → ElevatorSubsystem → ElevatorObservation. Current Noop adapters use the same transport boundary. Swerve, gyro, and vision simulation implementations use `updateInputs` and the same subsystem observation paths. The absence of real mechanism adapters is not an ACM-05 defect; simulation fidelity is outside this closure.

Production IOInputs references are confined to IO contracts, concrete IO update implementations, and owning subsystems. No public getter, observation field, command constructor, telemetry API, RobotContainer plumbing, callback, supplier, collection, generic helper, or static/global state exposes mutable IOInputs. Telemetry obtains subsystem observations, including immutable autonomous diagnostic observations. Reviewed commands use observations or semantic project/subsystem values. No alternate hardware-derived route around IOInputs, mutable transport alias, static/global transport registry, or mutable observation registry was established. RobotContainer's autonomous-event reference contains an immutable observation value. Tests cover subsystem refresh/order, observation behavior, Vision collection/transform copying, and Vision transport overwrite/timing; test-fixture IOInputs access is test-only and was not treated as production leakage. Tests were not run.

ACM-01 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `88b36ad22560e5bf08f1dc1365bed86efaaa68b8` (`audit-acm-01-closed`). ACM-02 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `25b01017f2a854b1370c192729cc3c63beaab930` (`audit-acm-02-closed`). ACM-03 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `849dc94061e29b80cf75e199bfc231659e2551c5`. ACM-04 remains FORMALLY CLOSED / CHECKPOINTED / PUSHED / TAGGED at `1a165797cfe7a094c6df2ec0bb4bc7bec37a2255`. No regression was established in ACM-01 through ACM-04; ACM-03-F01 and ACM-04-F01 remain NOT ESTABLISHED.

Historical S00/A01/V00/M00 predecessor lessons remain historical and untouched. At the ACM-05 checkpoint, ACM-06 was the next prospective domain; that cursor was superseded by the formal ACM-06 closure recorded above. ACM-07 through ACM-12 remain NOT STARTED. Phase 3 remains IN PROGRESS; Phase 4 remains NOT STARTED / FORBIDDEN. M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED. No M00_L17. ACM-05 closure did not authorize re-freeze, publication, Constants.java cleanup/refactor, or Phase 4.

The User checkpointed and pushed ACM-05 at `6c125c2490c50b9f2c0151379307ac72d10c0b22` with annotated tag `audit-acm-05-closed`; its remote target was verified by the User. This is a documentation-only lifecycle record: no Java, tests, authoritative A/B/C sources, governance manifest/mirrors, historical lesson source, dependencies, deployment assets, or protected/unrelated files were changed. No Git write, Gradle, tests, build, Simulation, Glass, Driver Station, or hardware execution occurred. `github-recovery-codes.txt`: NOT ACCESSED / NOT MODIFIED.

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
verification. The Architect accepted ACM-01-F02 and authorized only the
M00_L16 move of DriveThreeMeterValidationObservation from the root observation
package to `frc.robot.observation.swerve`, three production import updates,
one existing test import update, and one new focused placement test. The
governing rule is Document C OC-02 Section 1; the inherited origin is S00_L23.
Historical lessons remain untouched. The separate F02 decision and exact
six-Java-identity boundary are in
[the F02 repair ADR](docs/architecture_decisions/ADR_M00_L16_ACM_01_F02_Drive_Validation_Observation_Package_Repair.md).

At registration, F02 verification, independent review, and closure are
PENDING. F01 and DOC-01 remain CLOSED; F03 and F04 are ACCEPTED / PARKED.
M00_L16 remains IN_PROGRESS / NOT RE-FROZEN / NOT REPUBLISHED; ACM-01 HOLD,
ACM-02 NOT STARTED, Phase 4 NOT STARTED / FORBIDDEN. The next gate is bounded
F02 implementation and User automated verification, followed by independent
post-implementation review. No F03/F04 work or M00_L17 is authorized.

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


# FRC Java Coding Lab 7.0 — Repository Rules

English is normative. Vietnamese is explanatory.

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

- M00_L15: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Accepted User-owned primary snapshot: `15467d1ff3d7b3f65c8855d4a6c3a642d04a74fa`; metadata publication: `0d3685ce67a0b985459392621e003611eaa6dc35`; final gate: `PASS_M00_L15_FINAL_PUBLICATION_VERIFICATION`. Its files remain unchanged.
- M00_L16: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED. Active M00 lesson count: 0; current active M00 lesson: NONE. The untouched-copy baseline, inheritance audit, Final Design Lock, controlled activation, independent activation review, implementation authorization, implementation, bounded static repair, independent static rereview, User focused tests, User clean regression, bounded Simulation, documentation reconciliation, and independent closure review have passed. Freeze Reconciliation is COMPLETE / `PASS_M00_L16_FREEZE_RECONCILIATION`; Independent Freeze Review passed as `PASS_M00_L16_INDEPENDENT_FREEZE_REVIEW` with verdict `M00_L16_INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION_WORKFLOW`.
- The implemented concept is scheduler-managed dispatch of one existing `IntakeToFeederCommand` through `LEARNING_EVENT`. `RobotContainer.java` supplies a fresh child per dispatch with exact IntakeSubsystem and FeederSubsystem requirements. The L16 source delta is one production modification, one bounded architecture-test modification, and one new eight-test integration file. The inherited event path and teleop bindings remain unchanged.
- The hold event's normal path completion/interruption chain reaches `IntakeToFeederCommand.end(...)`, which stops Feeder then Intake. No guarantee is claimed after an arbitrary uncaught library exception. No timer, timeout, or new wrapper is approved.
- User focused tests passed (architecture guard and 8/8 integration tests; BUILD SUCCESSFUL, exit 0). User clean regression passed (BUILD SUCCESSFUL; no numeric test count or exit code supplied). Accepted `PASS_M00_L16_BOUNDED_SIMULATION` covers the event path, event-free control, Driver Station attachment, Disabled cleanup, normal exit, and no observed fatal scheduler/runtime exception. AutonomousEvent NT fields may be blank under the existing publication contract. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. The initial closure HOLD, bounded README repair, and fresh independent closure rereview PASS are preserved in lesson records. The User created Primary Frozen Snapshot Commit 1 at `ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11`. Publication metadata reconciliation is COMPLETE / `PASS_M00_L16_PUBLICATION_METADATA_RECONCILIATION`, awaiting User-owned Metadata Commit 2; that commit, push, and final external publication verification remain PENDING. M00_L16 is NOT PUBLISHED. It is the final M00 lesson; no M00_L17 is authorized.

Older dated lifecycle sections below are historical snapshots and are superseded by this current record.

---

## 1. Required Reading

Before any analysis, architecture review, implementation, testing analysis, documentation, or
repository audit, Codex MUST read:

1. AGENTS.md
2. README.md
3. docs/Document_A/FRC_Final_Frozen_Backbone_Guide_EN.pdf
4. docs/Document_A/ES-06_Frozen_Interface_Contract_EN.pdf
5. docs/Document_B/English/00_Engineering_Standard_Overview_EN.pdf
6. docs/Document_B/English/01_Frozen_Development_Workflow_EN.pdf
7. docs/Document_B/English/02_Java_Coding_Standard_EN.pdf
8. docs/Document_B/English/03_Architecture_Review_Checklist_EN.pdf
9. docs/Document_B/English/04_Lesson_Module_Checklist_EN.pdf
10. docs/Document_C/English/00_Observation_Architecture_Overview_EN.pdf
11. docs/Document_C/English/01_Observation_Model_Contract_EN.pdf
12. docs/Document_C/English/02_Observation_Package_Standard_EN.pdf
13. docs/Document_C/English/03_Observation_Architecture_Checklist_EN.pdf
14. Active lesson LESSON_STATUS.md
15. Active lesson source code

Only the English PDF documents are authoritative.
DOCX files are editable source documents.
Vietnamese documents are reference translations and are not required reading.

### Authority Order

1. AGENTS.md
2. Document A
3. Document B
4. Document C
5. README.md
6. Repository Source Code

If documents conflict, the higher priority document wins.

Stop immediately if repository code conflicts with Document A or Document B.

### Verified Markdown Mirror Reading Policy

Activated on 2026-08-29. Authoritative English PDFs remain authoritative, and
the authority order above is unchanged. A required English PDF reading item may
be satisfied for routine machine-readable reading by its co-located Markdown
mirror only when the mirror is `VERIFIED`, is registered in
`docs/GOVERNANCE_DOCUMENT_MANIFEST.md`, passes the required integrity checks,
and is used within its fidelity-class limits. A mirror has no independent or
equal authority, and the PDF controls every conflict.

At the first governance use in a task, and at the start of every formal
governance or architecture audit, Codex MUST:

1. run or confirm a current PASS from
   `py -3 docs/tools/governance/validate_governance_mirrors.py`;
2. consult `docs/GOVERNANCE_DOCUMENT_MANIFEST.md`;
3. confirm every applicable mirror is `VERIFIED` and registered;
4. compare each applicable current Markdown SHA-256 with its manifest hash; and
5. sufficiently read every applicable VERIFIED mirror to cover all potentially
   governing sections. Narrow snippets alone do not satisfy a formal audit.

After those checks pass, targeted retrieval of relevant mirror sections is
allowed for follow-up work within the same unchanged task and scope. Reading
must expand whenever another section could govern the decision. Targeted
reading never permits skipping relevant governance requirements.

Direct authoritative PDF consultation is mandatory when a mirror or manifest
record is missing; a mirror is `UNVERIFIED`, `STALE`, or `HOLD`; a source or
Markdown hash mismatches; PDF and Markdown conflict; wording is ambiguous;
fidelity is questioned or under review; forensic or historical reconstruction
is required; a formal review explicitly requires source confirmation; or the
Architecture Poster's spatial or visual meaning matters. Mirror consumption
stops for a conflicting or disputed area. Do not silently rewrite a PDF,
mirror, hash, or trust state; governed reconciliation is required.

The 11 `TEXTUAL` mirrors may support routine semantic reading after integrity
verification, but they are not authoritative. The VERIFIED Architecture Poster
mirror is `SEMANTIC_WITH_VISUAL_REFERENCE` and may represent explicit text,
relationships, and source-preserved flow wording. Its authoritative PDF remains
mandatory for landscape arrangement, adjacency, shared boxes, color emphasis,
spatial grouping, relative prominence, or visual hierarchy.

The manifest is an integrity and verification index only, not semantic
authority. It supports source/mirror mapping, provenance checks, and final
Markdown hash lookup. It does not automatically transition mirror trust state.

---

## 2. Repository Structure

Repository layout is fixed.

```
FRC_Java_Coding_Lab_7/
├── AGENTS.md
├── README.md
├── docs/
│   ├── Document_A/
│   ├── Document_B/
│   ├── Document_C/
│   └── architecture_decisions/
└── real_robot_programming/
    ├── module_A00/
    ├── module_A01/
    ├── module_D00/
    ├── module_D01/
    ├── module_S00/
    ├── module_V00/ (authorized; V00_L01-L09 complete/frozen/read-only)
    └── module_M00/ (authorized; M00_L01-L16 COMPLETE / FROZEN / READ-ONLY; exceptional M00_L16 P3-H01 + CFG-H01 repair CLOSED after accepted independent final closure and explicit Architect/User re-freeze authorization; Repository Active Lesson Count 0, Active M00 Lesson Count 0, Current Active M00 Lesson NONE; original M00_L16 publication preserved at primary ff4de5abf8b1ed3554c9fd68becdfedfbd6aed11 and metadata 3667290180fe1a9fd96265383e7412c142c18129, gate PASS_M00_L16_FINAL_PUBLICATION_VERIFICATION; User repaired primary Commit 1 CREATED at 015b8ca27d466a5a2fce2660a902bb58a4b62003; metadata reconciliation COMPLETE; metadata Commit 2 PENDING USER ACTION, push and external final verification PENDING; repaired publication NOT YET PUBLISHED; next gate USER METADATA COMMIT — COMMIT 2; Phase 3 HOLD, Phase 4 NOT STARTED / FORBIDDEN; M00_L16 remains the final lesson, no M00_L17)
         └── <LESSON_NAME>/
            ├── docs/
            ├── src/
            ├── build.gradle
            ├── settings.gradle
            └── LESSON_STATUS.md
```

Rules

- One lesson = One independent WPILib project.
- Every lesson has its own docs.
- Every lesson has LESSON_STATUS.md.
- Do not create folders outside the approved structure.

---

## 3. Frozen Backbone

Always preserve

Driver
→ Xbox Controller
→ controls
→ commands
→ subsystems
→ io
→ hardware

Observation flow

hardware
→ IOInputs
→ subsystem / estimator
→ immutable Observation
→ telemetry
→ NT4 / Glass / log

Telemetry is read-only.

This mechanism observation flow remains unchanged. The narrowly approved external human/operator
input exception is defined in Section 14 and does not apply to mechanism Observations.

---

## 4. Package Responsibilities

controls
- Driver input processing only.
- For external human/operator input only, controls may acquire one coherent controller sample and
  produce an immutable, vendor-neutral DriverInputObservation.
- This exception does not permit controls to produce mechanism Observations.

commands
- Coordinate subsystem actions.

subsystems
- Own mechanism behavior and state.

io
- Hardware abstraction only.

observation
- Immutable, vendor-neutral read models and pure evaluators only.
- Subsystems or dedicated estimators produce mechanism Observations.
- The Section 14 external human/operator input exception is the only approved controls-produced
  Observation exception.
- No hardware access, vendor APIs, NetworkTables, CommandScheduler, RobotContainer, mutable mechanism state, or control behavior.

telemetry
- Consume and publish immutable Observations only.
- No behavior control or hardware access.
- Lesson-specific approved exceptions are recorded in the architecture decision records referenced
  by Section 14 and do not establish general package dependencies.

util
- Generic shared reusable helpers only.

---

## 5. RobotContainer

RobotContainer is the Composition Root.

Allowed

- object creation
- dependency injection
- implementation selection
- default commands
- button bindings

Forbidden

- hardware logic
- mechanism logic
- input processing
- telemetry calculations
- business logic

---

## 6. IO Contract

Every mechanism must provide

- IO interface
- Inputs snapshot
- Real implementation
- Simulation or Noop implementation when required by the current lesson
- Safe stop()

Flow

Hardware
→ IO
→ IOInputs
→ Subsystem
→ immutable Observation
→ Telemetry

Subsystems never access vendor hardware directly.
Telemetry never publishes directly from mutable IOInputs when an Observation contract exists.

---

## 7. Java Rules

- Complete Java files only.
- No partial code.
- No omitted lines.
- No deprecated APIs.
- No magic numbers.
- English comments only.
- Preserve WPILib header.
- Add

/**
 * Author: SSIS
 * Mentor: SSIS
 */

before package.

Keep Constants.java as the default configuration authority.

---

## 8. Lesson Lifecycle

Copy previous completed lesson
→ Rename
→ Delete build/ and .gradle/
→ Baseline Build
→ Create and maintain Transition Guide
→ Add ONE concept
→ Build
→ Simulation
→ Real Robot
→ Finalize Documentation
→ User Commit
→ User Push

Rules

- Never recreate from scratch.
- Never modify the source code of completed lessons except under the narrowly
  approved exceptional frozen-reopen rule in Section 14.
- Documentation or metadata may be updated only with explicit user approval.
- Only the lesson with Status = IN_PROGRESS is editable.
- COMPLETE lessons are frozen snapshots.

### Exceptional Suspension and Reopen Lifecycle

- `SUSPENDED / READ-ONLY` preserves unfinished work exactly as-is. It is not
  COMPLETE, not FROZEN, not editable, and does not count as the active editable
  lesson. No production, test, documentation, configuration, dependency,
  asset, or feature change is permitted while suspended. Resume requires
  explicit governance approval and uses the exact preserved state unless a
  separately approved reconciliation is required.
- `SUSPENDED / READ-ONLY` is reserved for exceptional higher-priority safety or
  robustness work; it is not a normal lesson workflow state.
- `REOPENED` is a provenance qualifier for an `IN_PROGRESS` lesson, not an
  additional generic lifecycle status.
- The exceptional frozen-reopen requirements are defined in Section 14.

### Fixed Role Ownership

- ChatGPT is the Architect, Mentor, and Reviewer.
- Codex is the repository implementation and audit engineer.
- The User runs and verifies builds, Simulation, Glass / AdvantageScope, Driver Station, and
  real-robot testing.
- The User is the only Git commit and push operator.
- Codex shall not run Git, commit, push, or claim user-owned verification without supplied evidence.

---

## 9. LESSON_STATUS.md

Required fields

- Lesson
- Previous Lesson
- Status
- Architecture Review
- Baseline Build
- Build
- Simulation
- Driver Station / Glass
- Real Robot
- Transition Guide
- Git Commit
- Git Push
- Known Issues

Lesson status

- IN_PROGRESS
- COMPLETE
- SUSPENDED

Only `IN_PROGRESS` is editable. `SUSPENDED` is always read-only and is neither
COMPLETE nor FROZEN. `REOPENED` may qualify an IN_PROGRESS lesson's active state
but is not a separate lesson status.

Verification

- PASS
- FAIL
- NOT TESTED
- NOT APPLICABLE

Never report PASS without evidence.

---

## 10. Development Workflow

Before coding

- Read required documents.
- Confirm active lesson.
- Review Backbone.
- Review architecture.
- Confirm lesson objective.

During coding

- Each implementation step shall have one objective and one independently verifiable result.
- Preserve architecture.
- The User runs builds frequently and supplies the result as verification evidence.

After coding

- The User runs the required build and verification workflow.
- Codex records only supplied or directly authorized evidence.
- Record issues.
- Update LESSON_STATUS.md.

Stop when

- required documents missing
- architecture conflict
- Document A/B conflict
- build fails
- verification fails

### Repository Safety

Never

- rename repository folders
- move repository folders
- delete repository folders
- overwrite repository folders
- reorganize repository structure

unless explicitly approved by the user.

### Self Review

Before reporting success verify

- Documents read
- Document A reviewed
- Document B reviewed
- Backbone preserved
- Architecture preserved
- RobotContainer preserved
- Build reported
- Verification reported
- Documentation reported
- No unsupported claims

---

## 11. Documentation

Every completed lesson contains

real_robot_programming/<MODULE>/<LESSON>/docs/

Required guide

<PREVIOUS>_to_<CURRENT>_Step_by_Step.md

The transition guide is created and maintained during the lesson.
It is finalized only after implementation and all required verification are complete.
Transition Guide may be marked PASS only when the guide is final.
The final guide must exist before the lesson becomes COMPLETE / FROZEN.

Each step contains

- Step
- Objective
- Why
- Action
- Files Changed
- Verification
- Expected Result

One step = One change.

---

## 12. Built-in Commands

### Make step by step docs

Codex shall

- Read AGENTS
- Read Document A
- Read Document B
- Read README
- Read LESSON_STATUS
- Compare previous and current lesson
- Generate guide
- Save guide
- Update LESSON_STATUS
- Report results

Guide creation and maintenance are allowed while the lesson is IN_PROGRESS.

Stop guide finalization and do not mark Transition Guide PASS if

- implementation incomplete
- build failed
- verification missing

### Reserved Commands

Start next lesson

Finish lesson

Publish lesson

Reserved for future repository automation.

---

## 13. Git Rules

Git is User-owned. Codex shall not run Git commands.

Workflow

git status
→ git add
→ git commit
→ git push

Never claim GitHub was updated unless push succeeds.

---

## 14. Change Control

Formal review required before changing

- Frozen Backbone
- Package responsibilities
- Dependency direction
- IO contracts
- RobotContainer role
- Constants architecture
- Completed lessons

The permanent top-level package `frc.robot.observation` is part of the Frozen Backbone.

The review shall document:

- Reason
- Scope
- Impact
- Decision (APPROVED / REJECTED)

### Approved External Operator-Input Observation Exception

For external human/operator input only, controls may produce an immutable, vendor-neutral
DriverInputObservation from one coherent controller sample.

This exception:

- does not change the mechanism observation flow;
- does not permit controls to produce mechanism Observations;
- does not permit Observation to contain hardware access, vendor APIs, NetworkTables,
  CommandScheduler, RobotContainer, mutable state, or control behavior; and
- does not permit telemetry to control robot behavior.

### Approved Architecture Decision Records

Lesson-specific decisions shall be recorded outside global governance and referenced here.

- S00_L19 / S00_L20 driver-input ownership and migration:
  `docs/architecture_decisions/ADR_S00_L19_L20_Driver_Input_Ownership.md`
- Post-S00 A00 roadmap authorization:
  `docs/architecture_decisions/ADR_A00_Autonomous_Command_Foundation_Roadmap.md`
- Post-A00 A01 roadmap authorization:
  `docs/architecture_decisions/ADR_A01_Autonomous_Navigation_Path_Following_Roadmap.md`
- Post-A01 V00 roadmap authorization:
  `docs/architecture_decisions/ADR_V00_AprilTag_Vision_Observation_and_Pose_Fusion_Roadmap.md`
- Post-V00 M00 roadmap and preparation authorization:
  `docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`
- A01_L08 exceptional autonomous safety/robustness reopen:
  `docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`
- V00_L07 inherited Swerve architecture/robustness integrity reopen:
  `docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`
- M00_L16 P3-H01 exceptional configuration-authority repair:
  `docs/architecture_decisions/ADR_M00_L16_P3_H01_Configuration_Authority_Repair.md`

The S00_L19/S00_L20 decision does not change the Frozen Backbone, the authority order, or the
S00_L15-S00_L24 roadmap. The separately referenced A00 decision authorizes only the post-S00
module and `module_A00` location; it does not change the Frozen Backbone or authority order.

The approved A01 decision authorizes `A01 - Autonomous Navigation and Path Following` and
`module_A01` as the successor boundary after frozen A00_L04. A00 is closed at A00_L04; A00_L05
is prohibited. A01 inherits frozen A00_L04, and its order is governed by the approved A01 ADR.
Lessons shall not be reordered, renamed, merged, split, inserted, or skipped without the
architecture/governance approval required by that ADR. One lesson remains one new architectural
concept, and frozen predecessor protection remains mandatory.

The authorized A01 lesson order is:

1. `A01_L01 - Autonomous Starting-Pose and Field-Frame Contract`
2. `A01_L02 - Pose-Targeted Autonomous Motion`
3. `A01_L03 - Trajectory Generation and Sampling Fundamentals`
4. `A01_L04 - Field and Alliance Transform Contract`
5. `A01_L05 - Holonomic Trajectory Following`
6. `A01_L06 - PathPlanner Path and Runtime Integration`
7. `A01_L07 - AutoBuilder Contract Integration`
8. `A01_L08 - Autonomous Routine Selection and Safe Composition`
9. `A01_L09 - PathPlanner NamedCommands and Event Markers`

A00_L04's Autonomous+Enabled safety invariant and centralized
`SwerveSubsystem.stop()` authority remain authoritative. `RobotContainer` remains the composition
root only, and Simulation-before-real-robot verification remains mandatory. PathPlanner is
prohibited before A01_L06, AutoBuilder is prohibited before A01_L07, and the A01_L06 mandatory
compatibility entry gate remains authoritative. Vision/AprilTags are outside the A01 baseline,
and D01 retains mechanism architecture ownership.

The approved V00 decision authorizes `V00 - AprilTag Vision Observation and Pose Fusion` and
`module_V00` as the successor boundary after final closure of A01_L09. A01 closes at
A01_L09 after the required closure approval; A01_L10 is prohibited. V00_L01 shall inherit the
then-frozen A01_L09 through the standard copy,
rename, generated-artifact cleanup, baseline-build, and transition-guide workflow. This governance
registration does not select a camera vendor. The historical pre-reconstruction V00_L01 later
completed and froze, and V00_L02 was activated and implemented from that lineage before being
suspended read-only by the exceptional A01_L08 safety decision. After final A01_L09 was
reconstructed and published at `6b243bb`, the historical V00_L01 lineage was classified stale.
The current canonical V00_L01 was reconstructed from that final A01_L09, passed
its final architecture and closure reviews, and is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf`. The stale historical
V00_L02 was preserved outside the active lesson lineage. The current canonical
V00_L02 was reconstructed from published V00_L01, passed its inheritance,
baseline-build, architecture, design-lock, controlled-activation,
implementation, User-verification, documentation-completion, final architecture,
and final closure gates. It is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at
`53e9b9f`. User-owned Git publication was subsequently confirmed by the User.
The current canonical V00_L03 was then prepared by the User from published
V00_L02, passed its User-owned Java 17 baseline build, inheritance audit,
architecture audit, approved Design Lock, exact implementation boundary,
focused tests, inherited regressions, full suite, clean build, and
documentation-completion audit. It is now `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED` at `cc20d62`. Its final state is `IMPLEMENTATION COMPLETE /
USER-VERIFIED / DOCUMENTATION COMPLETE / FINAL ARCHITECTURE AUDIT PASS /
PREDECESSOR PROVENANCE PASS / FINAL CLOSURE REVIEW PASS`. The User then
prepared V00_L04 from that authoritative snapshot through copy/rename,
generated-artifact cleanup, and a User-verified WPILib Java 17 inherited
baseline build. Its inheritance, roadmap-scope, Frozen Backbone, Frozen
Interface Contract, and Document C audits passed, and the Architect approved
the refined deterministic-vision-simulation Design Lock. At the historical
activation stage, V00_L04 became the sole `IN_PROGRESS / EDITABLE` lesson.
Separate implementation authorization was later granted for exactly
`VisionIOSim.java` and `VisionIOSimTest.java`.
That implementation is complete and User-verified. `compileTestJava`, the
focused test, inherited vision regressions, the full suite, and the clean build
are `PASS`; the earlier Codex-local classpath result is `RESOLVED /
SUPERSEDED / NON-REPRODUCIBLE`. The post-implementation architecture review,
artifact cleanup, documentation reconciliation, final read-only review, and
closure authorization are `PASS`. V00_L04 is now
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 5461555 / USER VERIFIED`; no V00
lesson is active. User-owned Git publication is confirmed at `5461555`.

V00_L05 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 6482160 / USER
VERIFIED`, and V00_L06 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
1327bf4 / USER VERIFIED`. V00_L06 lesson publication is recorded as
`1327bf41736c8fe79ba58ec5eea9e0120bd978fb` with subject `Complete V00_L06
vision measurement quality contract`; its lesson-local publication metadata
reconciliation is recorded at `49c4286` as `Reconcile V00_L06 publication
metadata`.

The latest published vision snapshot remains the original
V00_L07_VisionTimestampAndLatencyContract publication:
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ d58bef0 / USER VERIFIED`.
Its lesson publication commit is
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f` with subject `Complete V00_L07
vision timestamp and latency contract`; its lesson-local publication metadata
reconciliation is recorded at `618dd09` as `Reconcile V00_L07 publication
metadata`. At the time of the separately approved exceptional inherited-Swerve
reopen, V00_L07 became the sole current editable lesson with state
`REOPENED / IN_PROGRESS / EDITABLE`; implementation, fresh verification,
re-freeze, and repair publication were then pending, and the original d58bef0
publication remains historical pre-repair evidence.

The authorized V00_L07 R1/R2/R3 repair is now implemented and documented. The
fresh pre-repair baseline passed with 593/593 tests and a clean build; the
post-repair full suite passed with 600/600 tests and a clean build. Runtime
WPILib Simulation and the post-implementation read-only architecture/Frozen
Backbone review passed. At the earlier repair stage, real-robot verification
was `DEFERRED — ROBOT UNAVAILABLE`. Later User evidence verifies Teleop and
Autonomous usability. The BL quantitative drivetrain anomaly remains `KNOWN /
DEFERRED HARDWARE MAINTENANCE`; no quantitative drivetrain PASS, completed
tuning/calibration, or issue resolution is claimed. Under the explicit
Architect/User disposition it does not block the Vision curriculum closure
sequence. The final read-only closure review passed and the Architect/User
authorized re-freeze. V00_L07 is now `COMPLETE / FROZEN / READ-ONLY`; the
corrected repair publication is now `PUBLISHED @ 4704cfc`. The following L08
state is the historical pre-activation record: V00_L08 was then the next
roadmap lesson and remained `NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED /
NOT PUBLISHED / NON-AUTHORITATIVE / READ-ONLY`; its candidate had to remain
preserved until separately authorized reconciliation. The current L08 state is
recorded in the controlled repair activation section below. V00_L09 remains
not started.

The authorized V00 lesson order is:

1. `V00_L01 - Vision Coordinate Frames and Camera Extrinsics`
2. `V00_L02 - AprilTag Field Layout Contract`
3. `V00_L03 - Vision IO and Immutable Observation Contract`
4. `V00_L04 - Deterministic Vision Simulation`
5. `V00_L05 - AprilTag Robot Pose Estimation`
6. `V00_L06 - Vision Measurement Quality Contract`
7. `V00_L07 - Vision Timestamp and Latency Contract`
8. `V00_L08 - Real Vision Adapter Integration`
9. `V00_L09 - Swerve Pose Estimator Vision Fusion`

V00 preserves the Frozen Backbone and Observation Architecture. Vision vendor APIs may exist only
inside the selected real VisionIO adapter; Vision models and evaluators remain immutable and
vendor-neutral; telemetry remains read-only; and `SwerveSubsystem` remains the sole owner of
`SwerveDrivePoseEstimator`. Vision supplies accepted timestamped measurements only, and the
approved fusion boundary uses `addVisionMeasurement(...)` rather than continuous pose reset.
Autonomous continues to consume `getEstimatedPose()` and shall not access camera, VisionIO, or
vendor APIs directly. A01_L04 remains the sole alliance-transform owner; vision measurements use
canonical WPILib field coordinates and are not alliance-flipped. Simulation shall not use
EstimatedPose as camera ground truth and shall pass before real-robot fusion verification.

No camera or vendor is selected in V00_L01 through V00_L07. V00_L08 may select exactly one real
vision implementation only after explicit review of actual camera hardware, WPILib 2026
compatibility, the exact vendor library/version, timestamp semantics, dependency resolution, and
simulation support where applicable. Lessons shall not be reordered, renamed, merged, split,
inserted, or skipped without the architecture/governance approval required by the V00 ADR.

### Exceptional A01_L08 Safety / Robustness Reopen

The approved A01_L08 reopen ADR temporarily supplements the ordinary A01/V00
lifecycle without changing either roadmap. New post-freeze and post-reopen
real-robot evidence identified material autonomous preparation/readiness and
terminal mode-ownership defects. Source review also identified manual
child-command lifecycle delegation that conflicts with the A01 scheduler-native
composition contract. With explicit Architect and User approval:

- A01_L01-L07 and A01_L09 are `COMPLETE / FROZEN / READ-ONLY`; A01_L09 final
  architecture and closure reviews are PASS, and User-owned Git publication is
  complete at `6b243bb`;
- the historical pre-reconstruction V00_L01 had reached
  `COMPLETE / FROZEN / READ-ONLY`, but that lineage is stale and non-authoritative;
- the current reconstructed V00_L01 passed final architecture and closure
  review and is `COMPLETE / FROZEN / READ-ONLY`;
- A01_L08 completed its authorized reopen and is now
  `COMPLETE / FROZEN / READ-ONLY`;
- V00_L02 has Status SUSPENDED and Active State
  `SUSPENDED / READ-ONLY`; its unfinished engineering is preserved;
- this closed A01_L08 exception does not itself make a lesson editable; current
  reconstructed V00_L01 is frozen after its separately authorized closure, and
  V00_L02 remains suspended until separately resumed;
- the future terminal repair may use a scheduler-native Swerve-owning hold,
  make SAFE_STOP retain safe ownership during active Autonomous, add a minimum
  defensive Teleop-enabled output gate, replace the affected manual lifecycle
  delegation with WPILib-native composition, and add exactly one `HOLDING`
  lifecycle state if required; and
- the original scope amendment authorized governance scope only; a later
  separately recorded implementation authorization permitted the exact repair
  boundary. The implementation has now removed the active adapter's manual
  child lifecycle delegation and added the approved scheduler-native Robot
  exception boundary. Later environment recovery and verification passed
  `compileTestJava`, the scheduler exception test, the full 449/449 suite, and
  the clean build. User-owned Simulation and real-robot re-verification passed,
  and the lesson was explicitly re-frozen on 2026-08-26.

The authorized target terminal lifecycle is
`CONSUMED -> RUNNING -> HOLDING -> COMPLETE`. While `HOLDING`, path motion is
complete, centralized Swerve stop has occurred, the Autonomous session remains
active, Swerve remains required, default Teleop drive cannot reacquire it, and
no autonomous motion restarts. Changes to SwerveSubsystem, CTRE or other IO,
CANcoder offsets, calibration, PID/feedforward, PathPlanner assets, Gradle,
vendordeps, RobotContainer without separate review, or downstream frozen or
suspended lessons are not authorized.

A frozen lesson may use this exception only for new post-freeze evidence of a
material safety, correctness, architecture, hardware-runtime, or verification
defect that invalidates a frozen assumption. It requires explicit Architect and
User approval, written evidence, exact scope, preserved historical evidence,
one editable lesson, focused and inherited regression gates, applicable
Simulation and real-robot verification, explicit re-freeze, and no unrelated
feature or refactor.

V00_L02 may resume only by explicit governance approval after A01_L08 is
repaired, fully re-verified, and explicitly re-frozen. Those A01_L08 gates are
now complete. V00_L02 nevertheless remains suspended until a separate
downstream reconciliation confirms V00 work remained unchanged and determines
whether the accepted L08 repair must be forward-ported through the inherited
lineage. Resume is never automatic.

That paragraph records the prerequisite state established by the A01_L08
exception. The later controlled reconstruction and activation decision dated
2026-08-27, recorded below, completed the required downstream reconciliation
and supersedes the suspension for the current canonical V00_L02 only.

### A01_L08 Scheduler Exception Boundary Governance Amendment — 2026-08-25

New source evidence confirms that the active `SafeAutoBuilderCommand` manually
delegates child `initialize()`, `execute()`, `isFinished()`, and `end()` callbacks.
This violates the A01 scheduler-native composition contract. A single adapter-only
replacement cannot preserve equivalent fail-closed exception safety because the
WPILib 2026.2.1 `CommandScheduler` and PathPlanner 2026.1.2 do not provide the
required project fault boundary around arbitrary child lifecycle exceptions, and
`finallyDo`/decorators do not catch those exceptions.

Architect and User approval is `APPROVED` for governance scope expansion only.
The approved future design is Option F: scheduler-native AutoBuilder composition,
existing narrow callback/output protections, a Robot-level scheduler
`RuntimeException` boundary, a coordinator/adapter fault bridge, centralized
Swerve stop, immutable `FAULTED` observation, and no automatic restart. This
amendment does not authorize implementation.

After separate implementation authorization, the exact production scope for this
scheduler exception-boundary repair is limited to:

- `AutoBuilderContractAdapter.java`;
- `AutonomousPreparationCoordinator.java`;
- `RobotContainer.java`; and
- `Robot.java`.

The directly authorized test scope is limited to
`RobotContainerPathPlannerIntegrationTest.java`, `AutonomousRoutineFactoryTest.java`,
`AutonomousPreparationCoordinatorTest.java`, and the new
`RobotSchedulerExceptionBoundaryTest.java`; unchanged related safety tests may be
rerun. No other production or test file is authorized by this amendment. The
SwerveSubsystem, IO, CTRE, tuning, calibration, Constants tuning, PathPlanner
assets, RobotConfig, Gradle, vendordeps, frozen lessons, and suspended V00_L02
remain excluded.

The required safety contract is fail-closed Swerve behavior, centralized stop,
latched first-fault preservation, immutable operator-visible `FAULTED`, no
automatic autonomous restart, and terminal `HOLDING` where applicable. Re-freeze
remains `HOLD` until the scheduler-native lifecycle, Robot-level exception
boundary, focused exception tests, inherited regression, clean build, Simulation,
real-robot verification, changed-file audit, documentation closure, and explicit
Architect/User re-freeze gates all pass.

### A01_L08 Final Scheduler-Native Implementation Authorization — 2026-08-25

The final Architect/User action separately authorized implementation of the
exact four-file production boundary and named test boundary described above.
The implementation removed `SafeAutoBuilderCommand` manual child lifecycle
delegation and added the approved scheduler-native composition and Robot-level
exception boundary. A01_L08 remains `REOPENED / IN_PROGRESS / EDITABLE` and
V00_L02 remains `SUSPENDED / READ-ONLY`.

The local production compile passed under WPILib Java 17. Test compilation is
currently held by the existing Windows Gradle/Javac classpath-resolution
failure; Simulation and real-robot verification remain User-owned gates and
were not rerun. Re-freeze remains `HOLD`.

### A01_L08 Final Re-Freeze Closure — 2026-08-26

The preceding implementation-authorization result is preserved as historical
evidence. Subsequent environment recovery established `compileJava` PASS,
`compileTestJava` PASS, `RobotSchedulerExceptionBoundaryTest` PASS, the full
449/449 test suite PASS, and clean build PASS. User-supplied Simulation and
real-robot re-verification also passed the Blue/Red path, terminal ownership,
SAFE_STOP, Teleop gate, recovery, and no-automatic-restart gates.

The observed one-time terminal steering event is classified as `KNOWN / BOUNDED
TERMINAL STEER TRANSIENT`, `ACCEPTED FOR CURRENT LESSON`, and `DEFERRED FOR
FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`. Its exact physical root cause is not
fully proven, and it does not justify a PID/feedforward, CANcoder, CTRE,
PathPlanner, Swerve, configuration, or asset change. A single approximately
5.9 ms desktop `SwerveSubsystem.periodic()` sample is not roboRIO performance
proof; no blocking CAN wait or production performance defect was found, and it
is not a closure blocker.

A01_L08 is therefore `COMPLETE / FROZEN / READ-ONLY` after its authorized
safety/robustness reopen. The Frozen Backbone and Frozen Interface Contract are
preserved. V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`; this closure
does not resume it.

### V00_L02 Controlled Reconstruction and Activation — 2026-08-27

The preceding V00_L02 suspension statements are retained as historical records
of the A01_L08 exception and the stale downstream lineage. After A01_L08 was
re-frozen, final A01_L09 was published at `6b243bb`, and reconstructed V00_L01
was published at `7d52ebf`, the stale historical V00_L02 was backed up outside
the active lesson lineage. The current canonical V00_L02 was reconstructed from
published V00_L01 through the required copy, rename, generated-artifact cleanup,
Java 17 baseline-build, and transition-document workflow.

Architect and User approval was `APPROVED` for controlled activation and
documentation/lifecycle reconciliation only. Repository inheritance review,
the reconstructed baseline, the full inherited test suite, the architecture
audit, and the pre-implementation design lock were PASS. At that historical
activation stage, V00_L02 became the sole `IN_PROGRESS / EDITABLE` lesson with
active state `RECONSTRUCTED BASELINE VERIFIED / DESIGN LOCK REVIEWED /
IMPLEMENTATION NOT YET AUTHORIZED`. V00_L01 remained `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED` at `7d52ebf`.

The approved future lesson boundary is package `frc.robot.vision`, future class
`AprilTagFieldLayoutContract`, future API
`loadOfficial2026(Constants.FieldTransformConstants.FieldVariant)` and
`getTagPose(int)`, and the existing field variants `REBUILT_WELDED` and
`REBUILT_ANDYMARK`. Returned poses use canonical WPILib Blue-origin
`fieldToTag` semantics. This lesson shall not alliance-flip or invert those
poses and shall not expose the mutable raw layout or mutable tag objects. A
future lookup of an unknown positive ID returns `Optional.empty()`; a
nonpositive ID throws `IllegalArgumentException`. The future production
dependency boundary is WPILib AprilTag/geometry plus the JDK and the existing
`FieldVariant` ownership only.

The package-private `fromLayout(AprilTagFieldLayout)` seam is explicitly not
approved. Adding that seam or any equivalent injection surface requires a
separate Architect decision. VisionIO, runtime camera access, Observation
production, NetworkTables, RobotContainer, Swerve, autonomous, PathPlanner,
pose fusion, Java implementation, and test implementation remain outside this
activation. Simulation, Driver Station / Glass, real-robot, and physical-camera
verification are `NOT APPLICABLE` to the current pure reference-data scope.
Future implementation requires separate explicit authorization.

### V00_L02 Implementation Verification and Documentation Completion — 2026-08-27

The preceding controlled-activation section records the historical state before
implementation authorization. A later explicit Architect/User action authorized
exactly one production file,
`frc/robot/vision/AprilTagFieldLayoutContract.java`, and exactly one focused test,
`frc/robot/vision/AprilTagFieldLayoutContractTest.java`. No other production or
test file changed. The implementation maps `REBUILT_WELDED` to
`AprilTagFields.k2026RebuiltWelded` and `REBUILT_ANDYMARK` to
`AprilTagFields.k2026RebuiltAndymark`, snapshots validated canonical Blue-origin
`fieldToTag` poses into immutable owned state, and exposes only the approved load
and lookup API. It does not use `kDefaultField`, alliance flipping, pose
inversion, `fromLayout(...)`, raw mutable layout/tag exposure, or runtime wiring.

Authoritative User verification under WPILib Java 17 records
`AprilTagFieldLayoutContractTest` PASS, inherited `VisionFrameTransformTest`
PASS, full test suite PASS, and clean full build PASS with `BUILD SUCCESSFUL in
24s` and `7 actionable tasks: 7 executed`. The earlier Codex-side incremental
classpath failure is an environment/process discrepancy and is not an accepted
implementation defect. Simulation, Driver Station / Glass, real robot, and
physical camera remain `NOT APPLICABLE` because V00_L02 adds immutable reference
geometry only.

Documentation completion and the pre-closure architecture audit were PASS. At
that pre-closure stage, V00_L02 remained the sole `IN_PROGRESS / EDITABLE`
lesson while final read-only architecture review, closure authorization, freeze
metadata, and User-owned Git publication remained pending. That historical
entry did not mark the lesson `COMPLETE` or `FROZEN` and did not start V00_L03.

### V00_L02 Final Closure and Freeze — 2026-08-27

The final read-only architecture and closure audit returned `PASS`, and the
Architect explicitly authorized final closure. V00_L02 is therefore
`COMPLETE / FROZEN / READ-ONLY`. Implementation verification, documentation,
the transition guide, the Frozen Backbone, Frozen Interface Contract, and
Document C boundaries are PASS. Simulation, Driver Station / Glass, real robot,
and physical camera remain `NOT APPLICABLE` because the lesson adds immutable
deterministic field-reference geometry only.

V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf`.
V00_L02 is `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `53e9b9f`.
V00_L03 was subsequently prepared from published V00_L02 and activated through
the separately approved controlled activation recorded below. The activation
state and its later implementation completion are preserved in chronological
order below; at that historical point V00_L03 was the sole `IN_PROGRESS /
EDITABLE` lesson pending closure review.

### V00_L02 User Git Publication Reconciliation — 2026-08-27

The User subsequently verified publication of the frozen V00_L02 snapshot at
`53e9b9f`. The User also verified that `HEAD` equals `origin/main` and that
the working tree is clean. This reconciliation updates current lifecycle
metadata only; the User remains the sole Git add/commit/push operator.

### V00_L03 Controlled Activation — 2026-08-27 (historical activation record)

After V00_L02 was published at `53e9b9f`, the User prepared
`V00_L03_VisionIOAndImmutableObservationContract` by copying that authoritative
predecessor, renaming the copy, handling generated artifacts, and running the
inherited baseline build with WPILib Java 17. A no-Git inheritance audit found
219 comparable non-generated files in each lesson and zero differences. Final
V00_L01 frame semantics, final V00_L02 field-layout semantics, inherited A01
safety/event architecture, Gradle, vendordeps, configuration, source resources,
and deploy/PathPlanner assets remain preserved.

Architect and User approval is `APPROVED` for controlled lifecycle activation
and documentation reconciliation only. V00_L03 is the sole `IN_PROGRESS /
EDITABLE` lesson. Preparation is complete, the User-supplied baseline build is
PASS, the read-only architecture audit is PASS, and the Design Lock is
APPROVED. Java implementation, focused tests, runtime wiring, Simulation,
Driver Station / Glass, physical-camera work, and Git publication have not been
authorized or completed.

The approved future L03 responsibility is a vendor-neutral one-cycle
`VisionIO` transport plus immutable `VisionObservation` contract. Package
`frc.robot.io.vision` will own `VisionIO`, `VisionIOInputs`, and
`VisionTargetInputs`; package `frc.robot.observation.vision` will own immutable
`VisionObservation`, its state, and immutable target values. The only approved
future IO method is `void updateInputs(VisionIOInputs inputs)`. The locked
transport fields are `available`, `connected`, `sampleValid`, and a multiple-
target collection containing positive `tagId` identity and WPILib
`Transform3d cameraToTarget`, meaning target relative to camera. The immutable
states are `UNAVAILABLE`, `DISCONNECTED`, `INVALID_SAMPLE`, `NO_TARGETS`, and
`TARGETS_PRESENT`. Runtime producer ownership, field-layout use, telemetry,
runtime wiring, simulation, vendor integration, pose estimation, quality,
timing, and fusion remain deferred to their separately governed roadmap gates.

No Limelight, PhotonVision, vendor result object, NetworkTables acquisition,
camera implementation, best-target policy, ambiguity/quality, timestamp,
latency, field-to-robot estimate, Swerve fusion, alliance transform,
autonomous, PathPlanner, Robot, RobotContainer, command, subsystem, or
scheduler change is authorized by this activation. Implementation requires a
separate explicit Architect/User authorization.

### V00_L03 Implementation Verification and Documentation Completion — 2026-08-27 (historical pre-closure record)

The preceding V00_L03 section records the historical activation state before
implementation authorization. A separate Architect/User action authorized the
exact two-file production and two-file focused-test boundary defined by the
V00_L03 Design Lock. The implementation added only
`frc.robot.io.vision.VisionIO`, including its mutable one-cycle
`VisionIOInputs` and `VisionTargetInputs` transport, and
`frc.robot.observation.vision.VisionObservation`, including immutable state and
target values. No runtime producer, vendor adapter, NetworkTables acquisition,
telemetry, simulation implementation, camera, pose estimation, quality,
timestamp, latency, fusion, Swerve, autonomous, or RobotContainer change was
added.

The earlier failing test expectation for an effectively zero quaternion norm
was a false oracle at the locked `Transform3d` boundary. WPILib
`Rotation3d` canonicalization had already converted that raw construction to a
valid identity rotation before the Observation contract could observe it. The
authorized repair changed the test oracle to verify a valid identity
`Rotation3d`; it did not add raw quaternion fields, a new API, or a production
contract expansion. No production repair was required.

Authoritative User verification is PASS for `VisionObservationTest`,
`VisionIOTest`, inherited `VisionFrameTransformTest`, inherited
`AprilTagFieldLayoutContractTest`, the full test suite, and the clean full
build. The final documentation reconciliation and read-only architecture audit
are PASS. Simulation, Driver Station / Glass, physical camera, and real-robot
verification remain `NOT APPLICABLE` to this contract-only lesson and are
deferred to their governed V00 lessons.

At that historical pre-closure stage, V00_L03 was the sole `IN_PROGRESS /
EDITABLE` lesson with implementation and documentation complete, pending
ChatGPT's final closure review and freeze decision. User-owned Git publication
remained pending; Codex performed no Git operations.

### V00_L03 Final Closure and Freeze — 2026-08-27

The Architect's final closure review returned `PASS`. The authoritative User
verification record remains PASS for the Java 17 baseline, focused
`VisionObservationTest` and `VisionIOTest`, inherited
`VisionFrameTransformTest` and `AprilTagFieldLayoutContractTest`, the full
512/512 test suite, and the clean full build. Inheritance, Frozen Backbone,
Frozen Interface Contract, Document A/B/C compliance, the exact implementation
boundary, documentation completion, and predecessor provenance are PASS.

V00_L03 is now `COMPLETE / FROZEN / READ-ONLY`. The lesson content/state is
complete and frozen. At this closure point, Git publication was still
`PENDING / USER OWNED`; the later User publication is recorded in the
reconciliation section below. No active lesson remained. Simulation, Driver
Station / Glass, physical camera, and real-robot vision are `NOT APPLICABLE` or
deferred by the contract-only scope; they are not claimed as runtime PASS
results.

V00_L01 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `7d52ebf` and
V00_L02 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED` at `53e9b9f`.
At this closure point V00_L04 had not yet been prepared or activated, and
A01_L10 remained prohibited.

### V00_L03 User Publication and V00_L04 Preparation Reconciliation — 2026-08-27

The User subsequently confirmed V00_L03 publication at `cc20d62`
(`cc20d62c5ce1c2d0411375eaccd9b98b0c53cf33`) with `HEAD == origin/main`.
V00_L03 is therefore `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`.

After confirming a clean working tree, the User copied authoritative V00_L03
to `V00_L04_DeterministicVisionSimulation`, renamed the copy, removed generated
build artifacts, selected WPILib Java 17, and supplied inherited baseline-build
PASS evidence. The V00_L04 directory therefore exists as a prepared inherited
copy, but the lesson is not activated, not `IN_PROGRESS`, and not editable.
Its architecture audit and Design Lock remain pending, and implementation is
not authorized. The repository has zero active lessons. V00_L01 and V00_L02
remain published and frozen, V00_L03 remains published and frozen, and
A01_L10 remains prohibited.

### V00_L04 Controlled Activation — 2026-08-28

The preceding preparation record is retained as historical evidence. A later
read-only audit confirmed V00_L04 faithfully inherits published V00_L03 at
`cc20d62`; the inheritance, roadmap-scope, Frozen Backbone, Frozen Interface
Contract, and Document C gates are `PASS`. The Architect approved the refined
V00_L04 Design Lock.

V00_L04 is therefore the repository's sole `IN_PROGRESS / EDITABLE` lesson.
Its one concept is a deterministic, vendor-neutral `VisionIOSim`
implementation of the frozen `VisionIO` contract. The locked design uses an
immutable caller-selected frame, official AprilTag field geometry, a fixed
`robotToCamera`, explicit simulation ground truth only when targets are
present, direct WPILib forward geometry, and a complete `VisionIOInputs`
overwrite on every update. Duplicate visible tag IDs are rejected; progression
is explicit; initial state is `UNAVAILABLE`.

This activation authorizes lifecycle/documentation metadata only. Production
and test implementation are `NOT STARTED`; implementation authorization is the
next gate. No clock, FPGA time, randomness, threads, NetworkTables, Driver
Station, alliance, scheduler, Observation producer, RobotContainer wiring,
Swerve change, telemetry, vendor integration, physical camera, pose
estimation, quality/ambiguity, timestamp/latency, or fusion is authorized.
V00_L01-L03 remain published and frozen, V00_L05-L09 remain deferred, and
A01_L10 remains prohibited.

### V00_L04 Implementation Verification and Documentation Reconciliation — 2026-08-28

The preceding activation section is preserved as the historical state before
separate implementation authorization. A later Architect/User action
authorized exactly one production file,
`src/main/java/frc/robot/io/vision/VisionIOSim.java`, and exactly one focused
test, `src/test/java/frc/robot/io/vision/VisionIOSimTest.java`. No other
production or test implementation was authorized.

The implementation provides deterministic, vendor-neutral forward measurement
synthesis. For target-present frames it combines known `fieldToRobot` ground
truth, the fixed `robotToCamera`, and official `fieldToTag` geometry to produce
`fieldToCamera` and then camera-relative `cameraToTarget` values. Progression
occurs only through `setFrame(...)`; complete-cycle overwrite prevents stale
targets; validation is fail-atomic; and no clock, randomness, vendor API,
NetworkTables, Driver Station, scheduler, runtime wiring, Observation producer,
or telemetry dependency was added. V00_L05 pose-candidate estimation and all
later V00 responsibilities remain deferred.

Authoritative User verification under WPILib Java 17 records
`compileTestJava` PASS, `VisionIOSimTest` PASS, required inherited vision
regressions PASS, full test suite PASS, and clean build PASS, each with exit
code 0 where supplied. The earlier Codex-local test-classpath failure is
`RESOLVED / SUPERSEDED / NON-REPRODUCIBLE` and is not a current blocker. The
post-implementation read-only architecture review returned `PASS`, including
the corrected independent `-2.5 m` geometry oracle, Frozen Backbone, Frozen
Interface Contract, Document C, and V00_L01-L03 protection.

The User also deleted the audited temporary compile-forensics log and
accidental untracked V00_L03 path copy. Documentation reconciliation and the
required `V00_L03_to_V00_L04_Step_by_Step.md` transition guide are complete.
V00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson with state
`IMPLEMENTED / VERIFIED / DOCUMENTATION RECONCILED / CLOSURE PENDING`.
Final closure review, freeze metadata, and User-owned Git publication remain
pending. V00_L05 has not been created.

### V00_L04 Controlled Closure and Freeze — 2026-08-28

The preceding activation and implementation sections are preserved as
historical lifecycle records. After the final read-only architecture and
documentation review returned `READY FOR ARCHITECT CLOSURE AUTHORIZATION /
PASS`, the Architect authorized controlled closure.

V00_L04 is now `COMPLETE / FROZEN / READ-ONLY`. Architecture, Design Lock,
implementation, `compileTestJava`, `VisionIOSimTest`, inherited vision
regressions, full test suite, clean build, post-implementation architecture
review, artifact cleanup, documentation reconciliation, transition guide,
Frozen Backbone, Frozen Interface Contract, Document C, V00_L01 protection,
V00_L02 protection, V00_L03 protection, and V00_L05-L09 scope isolation are
`PASS`.

The earlier Gradle/classpath failure remains only historical as `RESOLVED /
SUPERSEDED / NON-REPRODUCIBLE`. No active V00 lesson remains, V00_L05 has not
been created, and A01_L10 remains prohibited. Git publication is
`PENDING USER GIT`; no commit hash is claimed.

### V00_L04 Publication Metadata Reconciliation — 2026-08-28

The preceding activation, implementation, and closure sections are preserved
as chronological records. The User subsequently confirmed the completed
publication of the frozen V00_L04 snapshot:

`5461555 Complete V00_L04 deterministic vision simulation`

V00_L04 is now recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
5461555 / USER VERIFIED`. The User also confirmed `HEAD == origin/main`, a
clean working tree, and a successful push to `origin/main`. No production,
test, configuration, dependency, deploy, predecessor, or V00_L05 content was
changed by this metadata reconciliation.

### V00_L05 Controlled Activation and Design Lock — 2026-08-28

The User then prepared `V00_L05_AprilTagRobotPoseEstimation` from the final
published V00_L04 snapshot through the approved copy, rename, generated-artifact
cleanup, and inherited WPILib Java 17 baseline-build workflow. The read-only
inheritance and architecture audits recorded 229 comparable non-generated files,
zero differences, identical production and test Java, unchanged build/config,
vendordeps, deploy/resources/PathPlanner content, and preserved predecessor
protection. The earlier candidate naming HOLD was resolved by retaining the
ADR-locked V00_L05 identity; no ADR amendment was required.

The Architect approved the V00_L05 Design Lock. The lesson teaches one pure,
deterministic, vendor-neutral responsibility: derive a canonical Blue-origin
`fieldToRobot` robot-pose candidate from `fieldToTag`, `cameraToTarget`, and
`robotToCamera`. The locked estimator belongs in `frc.robot.vision` as the
stateless `AprilTagRobotPoseEstimator` utility with exactly one public method,
`estimateFieldToRobotCandidate(Pose3d, Transform3d, Transform3d)`. The approved
structural validation, output semantics, frozen dependency ownership, test
matrix, and L06 quality boundary are recorded in the active lesson documents.

This controlled activation changes lifecycle/documentation metadata only.
V00_L05 is now the repository's sole `IN_PROGRESS / EDITABLE` lesson with
active lesson count `1`. Production implementation and focused-test
implementation remain `NOT STARTED / NOT AUTHORIZED`. Focused tests, inherited
regressions, full-suite verification, clean build, Simulation, Driver Station /
Glass, and real-robot verification remain pending or not applicable according
to the pure-geometry Design Lock. V00_L01-L04 remain complete, frozen, and
protected; A01_L04 remains the sole alliance-transform owner; V00_L06 and later
lessons remain deferred; and A01_L10 remains prohibited.

The User-owned Git add, commit, and push operations remain separate future
gates. Codex performed no Git operation.

### V00_L05 Post-Implementation Verification and Documentation Reconciliation — 2026-08-28

The separately authorized V00_L05 implementation and focused test are complete
within the approved two-file boundary. The Java 17 compatibility adjustment
from `getFirst()` to `get(0)` was test-only. The original noncommutativity
fixture defect was also repaired in the focused test only: its compared inverse
transforms were pure translations and therefore commuted. The replacement
nondegenerate fixture proves the locked order by producing translations
`(3, 2, 1.5)` and `(0, 1, 1.5)` for locked and reversed composition. API
reflection hardening now protects the final class and exactly one public
declared method.

Authoritative User verification under WPILib Temurin Java 17.0.16 is PASS for
clean `compileTestJava`, focused L05 tests, inherited vision regressions, the
full test suite, and clean build (`BUILD SUCCESSFUL`). The post-implementation
architecture review, Frozen Backbone, Frozen Interface Contract, Documents
A/B/C, and frozen V00_L01-L04 predecessor protection are PASS. V00_L05 remains
the sole `IN_PROGRESS / EDITABLE` lesson; final closure authorization, freeze,
and User-owned Git publication remain pending. No Git publication hash is
claimed and Codex performed no Git operation.

### V00_L05 Final Closure and Freeze — 2026-08-28

The preceding activation and post-implementation sections are preserved as
historical lifecycle records. Architect closure authorization is now
`APPROVED`. V00_L05 is therefore `COMPLETE / FROZEN / READ-ONLY`.

Implementation, production architecture review, public API, locked transform
order, validation, conceptual test matrix, API reflection hardening,
noncommutativity repair, independent oracle, frozen predecessor protection,
build/configuration/dependency protection, Frozen Backbone, Frozen Interface
Contract, Documents A/B/C, User Java 17 verification, post-hardening
verification, and documentation reconciliation are `PASS`. Simulation, Driver
Station, Glass, and real-robot verification remain `NOT APPLICABLE` because
this lesson is pure deterministic geometry without runtime camera or hardware
integration.

V00_L05 is no longer an active lesson; no V00 lesson is active, and V00_L06
has not been activated or created by this closure. At this historical closure
point, Git publication remained `PENDING USER GIT PUBLICATION`; no commit hash,
push result, or published state was claimed. The User remains the sole Git
operator.

### V00_L05 Publication Metadata Reconciliation — 2026-08-28

The User subsequently confirmed the actual V00_L05 lesson publication:

`6482160 Complete V00_L05 AprilTag robot pose estimation`

The full publication commit is
`648216094fbea7eb5ebf26252f1ea457b93fcce8`, and the User confirmed that
`HEAD` and `origin/main` resolved to that commit at publication time. V00_L05
is therefore recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ 6482160`.

This entry reconciles publication metadata only. Any later commit that records
this metadata reconciliation is separate from the actual lesson publication
commit and remains User-owned, not yet created, and not yet published. No
production, test, configuration, dependency, deploy, asset, predecessor, or
V00_L06 content is changed by this reconciliation.

### V00_L06 Publication Metadata Reconciliation — 2026-08-30

The User subsequently confirmed the actual V00_L06 lesson publication and its
lesson-local publication metadata reconciliation:

`1327bf4 Complete V00_L06 vision measurement quality contract`

`49c4286 Reconcile V00_L06 publication metadata`

V00_L06 is therefore recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
1327bf4 / USER VERIFIED`. The current V00 lifecycle contains no active lesson.
The prepared V00_L07 candidate is an inherited, pre-activation candidate with
a User-verified baseline clean build PASS; it is not active, editable, Design
Locked, implemented, or published. This repository-level record changes
documentation metadata only and does not change V00_L01-L06 lesson content,
the V00_L07 candidate, the V00 roadmap, or any production/test/configuration
file.

---

### V00_L07 Repository Lifecycle Reconciliation — 2026-08-30

The User subsequently confirmed publication of the frozen V00_L07 snapshot and
its lesson-local publication metadata reconciliation:

`d58bef0 Complete V00_L07 vision timestamp and latency contract`

`618dd09 Reconcile V00_L07 publication metadata`

V00_L07 is therefore recorded as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @
d58bef0 / USER VERIFIED`. The current V00 lifecycle contains no active lesson.
V00_L08 remains the next roadmap lesson and is `NOT STARTED / NOT ACTIVATED /
NOT IMPLEMENTED / NOT PUBLISHED`. This repository-level reconciliation changes
documentation metadata only; it does not change lesson implementation,
production/test code, the V00 roadmap, or any frozen predecessor.

The documentation changes in this reconciliation remain subject to the User's
separate Git publication operation. No future repository-reconciliation commit
hash is recorded here.

### V00_L07 Exceptional Swerve Integrity Reopen — 2026-08-31 (Historical activation record)

The Architect and User approved a documentation-only exceptional reopen of
V00_L07 for exactly three inherited Swerve integrity repairs: R1 removal of a
command-layer dependency on the IO-owned
`SwerveModuleIO.StaticFrictionStopReason` type; R2 best-effort all-module stop
fanout when one module stop throws; and R3 coherent physical-forward measured
drive position and velocity when `physicalForwardSign = -1`. The authoritative
drive ratio remains `6.75:1`.

The decision is recorded in
`docs/architecture_decisions/ADR_V00_L07_Inherited_Swerve_Architecture_Robustness_Integrity_Reopen.md`.
At this activation point, V00_L07 was the sole current
`IN_PROGRESS / REOPENED / EDITABLE` lesson. The original publication at
`d58bef0` remains historical pre-repair evidence; at that time no Design Lock,
implementation authorization, implementation, fresh baseline, verification,
re-freeze, or repair publication had yet occurred. The exact repair boundary
and all exclusions were subject to the ADR and a later Design Lock; no
production or test file was changed by that documentation-only lifecycle
update.

At that historical stage, V00_L08 remained unactivated, non-authoritative,
read-only, and `NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / NOT PUBLISHED`,
with fresh reconstruction from corrected V00_L07 as the then-authorized next
procedure. That prospective procedure is now superseded for the existing
candidate by the one-time preservation-based reconciliation authorized by the
V00_L07 reopen ADR. The independent V00_L08 Limelight physical-evidence HOLD
is unchanged. The V00 roadmap and lesson identities are unchanged, and
A01_L10 remains prohibited.

This latest reconciliation changes governance and lesson documentation only.
Git publication remains User-owned and pending.

### V00_L07 Post-Repair Documentation Reconciliation — 2026-08-31 (Historical robot-unavailable stage)

The separately authorized R1/R2/R3 implementation is complete within the
approved boundary. The fresh pre-repair baseline passed with 593/593 tests and
a clean build. The post-repair focused tests, inherited regressions, and full
600/600 test suite passed, and the clean build passed. Runtime WPILib
Simulation and the final read-only architecture/Frozen Backbone review passed.
Real-robot verification is `DEFERRED — ROBOT UNAVAILABLE`, so V00_L07 remains
the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson; re-freeze and repair
publication remain pending. V00_L08 remains unactivated and read-only, must be
reconstructed only from corrected published V00_L07, and its independent
Limelight physical-evidence HOLD is unchanged. This reconciliation changes
documentation metadata only.

### V00_L07 Pre-Closure Hardware-Evidence Reconciliation — 2026-09-07

The preceding 2026-08-31 record preserves the historical stage at which the
robot was unavailable. Later User evidence verifies Teleop and Autonomous
usability. The BL quantitative drivetrain anomaly remains `KNOWN / DEFERRED
HARDWARE MAINTENANCE`; it is unresolved and is not BL PASS, quantitative
drivetrain PASS, matched-module evidence, completed tuning/calibration, or
issue resolution. The Architect/User disposition makes that separate
maintenance condition non-blocking for continued Vision curriculum closure.

The R1/R2/R3 implementation boundary, 593/593 reopened baseline, focused and
inherited regressions, 600/600 post-repair suite, clean build, runtime WPILib
Simulation, post-implementation architecture review, and Frozen Backbone
review remain PASS. Pre-closure documentation evidence is reconciled. V00_L07
remains the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson and is
`CLOSURE-READY / PENDING FINAL READ-ONLY CLOSURE REVIEW`. Explicit
Architect/User re-freeze approval and User-owned corrected repair publication
remain pending. Historical `d58bef0` remains the pre-repair publication.
V00_L08 remains unactivated, non-authoritative, read-only, and untouched.

### V00_L07 Final Re-Freeze Closure — 2026-09-07

The final read-only closure review returned
`READY_FOR_EXPLICIT_L07_REFREEZE_AUTHORIZATION`, and the Architect/User
explicitly authorized the documentation-only re-freeze. V00_L07 is therefore
`COMPLETE / FROZEN / READ-ONLY`. The R1/R2/R3 repair, verification evidence,
Frozen Backbone, Frozen Interface Contract, and documentation are PASS.

Teleop and Autonomous usability remain User-verified. The BL quantitative
drivetrain anomaly remains `KNOWN / DEFERRED HARDWARE MAINTENANCE`; no BL PASS,
quantitative drivetrain PASS, matched-module result, completed tuning or
calibration, or issue resolution is claimed. Historical `d58bef0` remains the
pre-repair V00_L07 publication only. The corrected repair publication is
`PUBLISHED @ 4704cfc0801910e30c8abb7cffcc467e4f4df016` with subject
`Complete corrected V00_L07 Swerve integrity repair`; User commit and push are
verified, and HEAD equals origin/main at that commit. No V00 lesson is active;
V00_L08 remains unactivated, non-authoritative, read-only, stale pre-repair
inheritance and untouched. V00_L09 remains not started.

### V00_L07 Corrected Publication-Metadata Reconciliation — 2026-09-07

The User performed the authorized exact-allowlist publication. The corrected
post-repair V00_L07 publication is `PUBLISHED / USER VERIFIED` at
`4704cfc0801910e30c8abb7cffcc467e4f4df016`, with subject
`Complete corrected V00_L07 Swerve integrity repair`. HEAD and origin/main both
resolved to that commit at the time of that publication. The earlier
`d58bef0d17d202ce1dd0b8645635a8c35095dd3f`
publication remains preserved as historical pre-repair provenance.

V00_L07 remains `COMPLETE / FROZEN / READ-ONLY`. V00_L08 remains untouched and
unactivated; its one-time preservation-based reconciliation is a separate
governed action. V00_L09 remains not started. This reconciliation changes
documentation metadata only.

### V00_L08 One-Time Preservation-Based Reconciliation Amendment — 2026-09-07

The previous reconstruction-only procedure is HISTORICAL and is SUPERSEDED
PROSPECTIVELY only for the existing V00_L08 candidate. The candidate remains
preserved, NOT STARTED, NOT ACTIVATED, and READ-ONLY. Before any authorized
reconciliation change, the User must create and verify a byte-preserving
filesystem checkpoint outside the repository, including hidden files, `.Glass`,
build, `bin`, `.gradle`, test artifacts, deploy assets, configuration, and
documentation; the checkpoint is not staged or committed here.

The only permitted forward-port boundary is the exact seven-file R1/R2/R3
boundary from corrected V00_L07 publication
`4704cfc0801910e30c8abb7cffcc467e4f4df016`, as enumerated in the ADR. Focused
tests, inherited Swerve/vision/autonomous regressions, the full suite, clean
build, changed-file verification, and a post-reconciliation inheritance /
architecture review are mandatory. Only after those gates may normal L08
Real Vision Adapter Design Lock and controlled activation proceed.

This amendment changes no Documents A/B/C, Frozen Backbone, Frozen Interface
Contract, roadmap, lesson scope, or User Git ownership. It authorizes no
Limelight implementation, NetworkTables acquisition, vendor dependency, pose
normalization, estimator fusion, PathPlanner change, drivetrain tuning, BL
investigation, or V00_L09 work. The Limelight evidence distinction remains
`Limelight -> roboRIO NetworkTables server -> Glass` as VERIFIED USER HARDWARE
EVIDENCE, while `Limelight -> Java VisionIO -> immutable VisionObservation`
remains NOT YET IMPLEMENTED. Detailed evidence must later be captured in a
dedicated L08 experiment/evidence document; unresolved physical measurements
are not governance authority.

### V00_L08 Controlled Repair Activation and Reconciliation — 2026-09-09

This is a historical pre-closure lifecycle record.

The User explicitly authorized the bounded V00_L08 repair based on the final
Astra closure audit. At that historical stage, the preserved candidate was the
sole current `IN_PROGRESS / EDITABLE` lesson. The authorized repair boundary is limited to
the real Limelight adapter freshness/coherence policy, the observation-only
periodic runtime owner, read-only diagnostic telemetry, the public Limelight
constant boundary, the directly related focused tests, and L08 lesson/root
documentation reconciliation. V00_L09, pose-estimator fusion,
Swerve/drivetrain/IO/tuning/calibration, autonomous behavior, PathPlanner,
vendor dependency/configuration changes, and H1 promotion remain excluded.

This entry records the User-authorized activation and bounded repair scope only;
it does not promote an implementation-selected freshness recipe to frozen
governance, declare runtime readiness, or alter the frozen VisionIO contract.
The repair remains subject to the normal implementation, official build, test,
Simulation, physical-camera, documentation, and closure gates.

Both `VisionIOLimelight` and `VisionIOSim` are wired through the same periodic
observation-only runtime path. RobotContainer remains the composition root and
does not acquire samples or perform business logic. Limelight topic names remain
private to the real adapter, and no vendor type is exposed through public IO,
Observation, or telemetry contracts.

At this recorded stage, `compileJava` and the direct focused execution were
historical evidence only; official `compileTestJava`, inherited regressions,
the full suite, clean build, Simulation, Driver Station/Glass, and real-camera
validation were not closure evidence. H1 remains provisional. The lesson
remains `IN_PROGRESS / EDITABLE`; User Git ownership is unchanged and Codex did
not run Git.

### V00_L08 Bounded Repair Clarification — 2026-09-10

The 2026-09-09 entry is a lifecycle and scope authorization only. It does not
freeze a particular NetworkTables freshness algorithm, heartbeat threshold,
recovery recipe, or read-coherence implementation as global governance policy.
At that historical pre-closure stage, the repair had to remain fail-closed and
was judged by the explicit L08 verification gates. Until official test/build,
Simulation, Driver Station/Glass, and real-camera evidence were complete,
V00_L08 remained `IN_PROGRESS / EDITABLE`; no runtime-ready,
production-convention, or `COMPLETE / FROZEN` claim was authorized by that
record.

### V00_L08 Final Closure and Freeze — 2026-09-10

The final read-only architecture and closure review returned
`PASS_V00_L08_FINAL_CLOSURE_REVIEW`, and the Architect authorized the final
documentation-only freeze. V00_L08 is now
`COMPLETE / FROZEN / READ-ONLY`.

The authoritative evidence remains PASS for governance, the Frozen Backbone,
the Frozen Interface Contract, focused verification (`37/37`), the full
regression (`642/642`), clean build, WPILib Simulation, Driver Station/Glass,
and real-robot Limelight acquisition, target loss, and reacquisition. The
transition guide and documentation reconciliation are final and PASS. No
current technical blocker or unexpected architectural drift remains.

H1 remains a **PROVISIONAL COMMISSIONING LOCK** and is not promoted to official
or proven vendor semantics. NetworkTables behavior remains documented as
bounded freshness/coherence/read-stability checking, not atomic multi-topic
reads. V00_L08 contains no estimator fusion; V00_L09 remains future work.

No V00 lesson is active and V00_L09 remains not started. At the closure point
before later publication, lesson content/state was complete and frozen while
Git publication remained `PENDING USER COMMIT/PUSH`; Codex performed no Git
operation.

### V00_L08 User Git Publication Reconciliation — 2026-09-10

The User subsequently confirmed the authorized publication of the frozen
V00_L08 lesson. The authoritative publication commit is `f34b210` with
message `Complete V00_L08 real vision adapter integration`. The User also
confirmed push `PASS` and that `origin/main` contains `f34b210`.

V00_L08 remains `COMPLETE / FROZEN / READ-ONLY`; no production or test source
was changed by this metadata reconciliation, and V00_L09 remains not started.
Codex performed no Git operation.

### V00_L09 Controlled Activation — 2026-09-11

The preceding V00_L08 closure and publication records are preserved as
historical evidence. The User then prepared the ADR-locked
`V00_L09_SwervePoseEstimatorVisionFusion` candidate from frozen V00_L08,
removed copied generated artifacts, supplied inherited baseline clean-build
PASS, reconciled the roadmap identity, and supplied PASS evidence for the
pre-implementation architecture audit, real timing-source investigation,
runtime orchestration micro-audit, coordinator decision, and the Architect
gate `PASS_V00_L09_FINAL_DESIGN_LOCK`.

The V00_L08 implementation publication remains `f34b210` with subject
`Complete V00_L08 real vision adapter integration`. The User separately
identifies `6415b17` as the later V00_L08 publication-metadata reconciliation;
that publication-metadata history is retained as distinct from the
predecessor implementation publication. This activation does not modify any
V00_L08 file.

With explicit Architect authorization for this documentation-only action,
V00_L09 is now the repository's sole `IN_PROGRESS / EDITABLE` lesson with
active lesson count `1`. V00_L08 and V00_L01-L07 remain
`COMPLETE / FROZEN / READ-ONLY`. The activation reconciles lifecycle metadata
only; it does not claim that Java implementation has completed or authorize
any source change by itself.

The locked V00_L09 concept is qualified timestamped AprilTag measurement
admission through the post-scheduler `VisionFusionCoordinator` requirement
and guarded Swerve-owned admission into
`SwerveDrivePoseEstimator.addVisionMeasurement(...)`. Swerve remains the sole
owner of estimator state, Vision remains vendor-neutral above IO,
RobotContainer remains the composition root, and the lesson does not add a
MegaTag migration, dynamic quality covariance, Constants tuning, or a second
concept.

At this activation point, L09 implementation, focused tests, inherited
regression, post-implementation clean build, Simulation, Driver Station /
Glass, deployed Limelight JSON verification, real-robot verification, final
architecture review, final documentation closure, and COMPLETE/FROZEN
transition remained pending. The prior implementation authorization was
blocked by the lifecycle mismatch and required later reconciliation. The
subsequent implementation-and-evidence section below supersedes this pending
activation-point state. User Git add, commit, and push remained separate and
pending. Codex performed no Git operation.

This activation does not amend the V00 roadmap ADR, change lesson order,
reopen V00_L08, or modify any unrelated module.

### V00_L09 Implementation and Evidence Reconciliation — 2026-09-12

The preceding controlled-activation record remains historical evidence.
Implementation subsequently completed under the Architect-controlled workflow
and preserves the locked L09 concept: qualified timestamped AprilTag
measurement, post-scheduler `VisionFusionCoordinator`, guarded Swerve-owned
admission, and `SwerveDrivePoseEstimator.addVisionMeasurement(...)`. Swerve
remains the sole estimator owner and mutator; `getCurrentPose()` remains
odometry-only, `getEstimatedPose()` retains fused-estimator semantics,
RobotContainer remains the composition root, and telemetry remains read-only.

The User verified focused tests, inherited regression, `compileTestJava`, the
full suite, and a clean build. Direct automated evidence now covers stale,
future, and out-of-order rejection; reset-barrier cached-pre-reset rejection
and fresh post-reset acceptance; scheduler-failure fusion suppression;
coordinator failure through the existing Robot fail-closed boundary; and
telemetry execution through the `finally` path. The initial Robot-boundary
tests failed before those boundaries because their `Pose3d.kZero` fixture was
outside the production quality policy. Under
`PASS_V00_L09_NARROW_TEST_EVIDENCE_AUTHORIZED`, the fixture-only repair reused
the existing deterministic quality-valid `VisionIOSimHarness` Frame A/B path,
preserved the assertions, and changed no production behavior. The Architect
accepted the repair and automated evidence through
`PASS_V00_L09_FAILURE_BOUNDARY_TEST_FIXTURE_REPAIR_ACCEPTED` and
`PASS_V00_L09_AUTOMATED_EVIDENCE_CLOSURE_VERIFIED`.

User-owned Simulation evidence is PASS for runtime startup, estimator
initialization, Frame A/B accepted one-shot fusion, vision loss, and
reacquisition. Driver Station / Glass verification is also PASS. Deployed
Limelight L09 timing/result verification and real-robot estimator
vision-fusion verification remain `PENDING / REAL HARDWARE DEFERRED`; V00_L08
real-camera evidence does not satisfy those L09 gates. Final
architecture/Frozen Backbone review, final documentation and transition-guide
closure, the `COMPLETE / FROZEN / READ-ONLY` transition, and User-owned Git
publication remain pending. V00_L09 remains the sole `IN_PROGRESS / EDITABLE`
lesson with active lesson count `1`; V00_L08 remains
`COMPLETE / FROZEN / READ-ONLY` and unmodified.

### V00_L09 Documentation Reconciliation — 2026-09-15

The preceding implementation-and-evidence record remains historical evidence.
The accepted L09 evidence is now documentation-reconciled without changing the
lesson lifecycle: L09 remains the sole `IN_PROGRESS / EDITABLE` lesson with
active lesson count `1`, and V00_L08 remains `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED @ f34b210` and unmodified.

The preserved generated automated-evidence report records 637 tests, zero
failures, zero errors, and zero skipped tests; the User-verified compile,
regression, and clean-build results remain PASS.

The evidence classifications are explicit. `THEORY VERIFIED` covers governance,
Design Lock, architecture, Frozen Backbone preservation, and the independent
final architecture review. `SIMULATION VERIFIED` covers startup, estimator
initialization, one-shot fusion, loss, reacquisition, and Glass/runtime
simulation evidence. `REAL HARDWARE VERIFIED` covers deployed V00_L09
Limelight flat-root `/limelight/json` acquisition/parser behavior, timing and
result handling, AprilTag 32 acquisition, stationary accepted fusion,
controlled translation and rotation, target loss/reacquisition, camera
disconnect/recovery, and the bounded autonomous lifecycle.

The bounded BLUE `ONE_METER_PATH` Gate 9 reached READY and completed with
`Reason = COMMAND_COMPLETED`, `State = COMPLETE`, and no adapter fatal fault.
Vision was intentionally invalid/suppressed during that run because the
physical Tag 32 placement was not asserted to match the official field
coordinate. Gate 9 therefore proves bounded autonomous lifecycle and estimator
compatibility; it does not prove exact 1.000 m endpoint accuracy or physical
absolute-pose calibration accuracy.

One historical Driver Station overrun warning was observed. Independent
read-only review found no proven structural runtime defect, busy-wait,
`Thread.sleep` production path, or unbounded retry. PathPlanner preparation and
full Limelight JSON parsing remain plausible but unproven timing contributors.
If the warning recurs, capture WPILib/Driver Station timing epochs before any
performance repair. No specific cause or resolution is claimed.

The final independent architecture review gate is
`PASS_V00_L09_FINAL_ARCHITECTURE_REVIEW_READY_FOR_DOCUMENTATION_CLOSURE`.
Documentation reconciliation is complete, but separate final documentation
review and explicit `COMPLETE / FROZEN / READ-ONLY` authorization remain
pending. User-owned Git add, commit, and push remain pending. This record does
not amend the V00 roadmap, create V00_L10, reopen V00_L08, or authorize source,
test, deploy, vendordep, configuration, or tuning changes.

### V00_L09 Final Freeze Recording — 2026-09-15

The independent final documentation review returned
`PASS_V00_L09_FINAL_DOCUMENTATION_REVIEW_READY_FOR_FREEZE_AUTHORIZATION`. The
Architect then explicitly authorized the documentation-only lifecycle
transition with `PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`.

V00_L09 is now recorded as the completed frozen lesson:

```text
STATUS: COMPLETE
ACTIVE STATE: COMPLETE / FROZEN / READ-ONLY
FREEZE STATE: FROZEN / READ-ONLY
ACTIVE LESSON COUNT: 0
GIT COMMIT: PENDING USER COMMIT
GIT PUSH: PENDING USER PUSH
PUBLICATION: PENDING USER PUBLICATION
```

The accepted `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE
VERIFIED` classifications remain unchanged. Gate 9 remains qualified as
bounded autonomous lifecycle and estimator compatibility evidence only;
vision was intentionally invalid/suppressed because the physical Tag 32
placement was not asserted to match the official field coordinate. Exact
1.000 m endpoint accuracy and physical absolute-pose calibration accuracy are
not claimed.

One historical Driver Station overrun warning remains documented as having no
proven structural runtime defect. PathPlanner preparation and full Limelight
JSON parsing remain plausible but unproven contributors; if the warning recurs,
capture WPILib/Driver Station timing epochs before any performance repair. No
specific cause or performance repair is claimed.

V00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ f34b210` and
unmodified. At the time of this freeze record, User-owned L09 Git commit,
push, and publication remained pending; no L09 publication metadata was
claimed at that point.

### V00_L09 Post-Publication Metadata Reconciliation — 2026-09-15

The User supplied and verified the final Git publication state for the frozen
L09 implementation. The implementation publication commit is `6548c98` with
subject `Complete V00_L09 Swerve pose estimator vision fusion`. The User
verified that `origin/main = 6548c98` and `origin/HEAD = 6548c98`; the recorded
push result was `Everything up-to-date`.

The accepted publication gate is
`PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`. The reconciled publication record
is:

```text
GIT COMMIT: 6548c98
COMMIT MESSAGE: Complete V00_L09 Swerve pose estimator vision fusion
GIT PUSH: COMPLETE / VERIFIED
REMOTE: origin/main = 6548c98
PUBLICATION: PUBLISHED / VERIFIED
FINAL PUBLICATION GATE: PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED
METADATA RECONCILIATION COMMIT: PENDING USER COMMIT
```

The metadata-reconciliation commit is distinct from the implementation
publication commit and is not claimed to exist. This documentation-only
reconciliation does not change the accepted THEORY VERIFIED, SIMULATION
VERIFIED, or REAL HARDWARE VERIFIED evidence; it does not change Gate 9's
bounded qualification or the historical overrun wording.

V00_L09 remains `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`.
V00_L08 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED @ f34b210` and
unmodified. Codex performed no Git operation, and no source, test, deploy,
vendordep, configuration, tuning, roadmap, or predecessor change is included.

### V00 Module Final Closure Confirmation — 2026-09-15

The User subsequently published the documentation-only metadata reconciliation
at `5d36529` with subject `Record V00_L09 publication metadata`. The accepted
final repository state is `HEAD = origin/main = origin/HEAD = 5d36529`, with
ahead `0` and behind `0`. The V00 final closure gate is
`PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`.

`V00_L09_SwervePoseEstimatorVisionFusion` remains `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED`. Its implementation/freeze publication is
`6548c98` with subject `Complete V00_L09 Swerve pose estimator vision fusion`;
`5d36529` is the later metadata-reconciliation publication. No V00 lesson is
reopened, and the active lesson count remains `0`.

### M00 Governance Preparation Authorization — 2026-09-15

The controlling successor record is
`docs/architecture_decisions/ADR_M00_Competition_Mechanism_Foundations_Roadmap.md`.
The M00 roadmap is `APPROVED / ROADMAP AUTHORIZED`; governance preparation is
`AUTHORIZED` by `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`; M00 runtime and
lesson activation remain `NOT ACTIVE`.

```text
Active Lesson Count: 0
```

The locked roadmap contains exactly 16 lessons, `M00_L01` through `M00_L16`,
in the ADR-defined order. The first lesson is locked as
`M00_L01 - Mechanism Architecture Reuse`, with directory identity
`M00_L01_MechanismArchitectureReuse`. It is `NOT ACTIVE / NOT YET CREATED`, is
not `IN_PROGRESS`, and has no implementation authorization.

After this governance record is reviewed and User-published, the User may
perform only the normal preparation workflow. The exact predecessor is the
published V00_L09 snapshot at repository state `5d36529`, with implementation
commit `6548c98`, at:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_V00\V00_L09_SwervePoseEstimatorVisionFusion
```

The exact future destination is:

```text
C:\Users\xps7350i7\Desktop\FRC_Java_Coding_Lab_7\real_robot_programming\module_M00\M00_L01_MechanismArchitectureReuse
```

The future User-owned preparation sequence is: start at the repository root;
copy the complete frozen V00_L09 directory; create `module_M00` only as part of
that authorized copy workflow; rename only the destination copy to the locked
directory identity; remove only the destination `build\` and `.gradle\`;
select WPILib 2026 Java 17; run the inherited baseline clean build; and report
`BUILD SUCCESSFUL` plus Git status. Only after that evidence may Architecture
Audit, Design Lock, lifecycle activation, and implementation authorization be
considered separately. The baseline command to be run inside the future
destination is:

```powershell
$env:JAVA_HOME = "C:\Users\Public\wpilib\2026\jdk"
.\gradlew.bat clean build "-Dorg.gradle.java.home=C:\Users\Public\wpilib\2026\jdk"
```

M00 preserves the Frozen Backbone, Frozen Interface Contract, Constants as the
default configuration authority, frozen predecessor protection, and one lesson
per new concept. RobotContainer remains composition root only. Vendor APIs
remain confined to concrete IO adapters. Mechanism data continues to flow
`hardware -> IOInputs -> subsystem/processing -> immutable Observation ->
read-only telemetry`. Intake, Feeder, Flywheel, and Elevator retain independent
ownership. Shooting composition remains `FlywheelSubsystem + FeederSubsystem +
ShootCommand`; no `ShooterSubsystem` or `ShooterIO` is authorized absent a
later formal architecture change.

M00_L01's sole concept is Mechanism Architecture Reuse: how the mastered
drivetrain, vision, and autonomous architecture applies to non-drivetrain
mechanisms. It may teach subsystem ownership, IO, immutable Observations,
read-only telemetry, composition-root assembly, safe stop, and architecture
mapping. It must not implement Intake, Feeder, Flywheel, Elevator, closed-loop
control, readiness, homing, travel limits, coordination, autonomous events, or
a new hardware API.

Any future student-facing M00 Markdown must be delivered as separate English
and Vietnamese files with identical structure, course/chapter identity,
meaning, evidence, and architecture rules. English is normative; Vietnamese is
student-friendly but semantically equivalent. Evidence must use only `THEORY
VERIFIED`, `SIMULATION VERIFIED`, `REAL HARDWARE VERIFIED`, `REAL HARDWARE
DEFERRED`, or `NOT APPLICABLE`. M00_L01 runtime applicability is not claimed;
the future Design Lock decides it. No V00 rerun is required by this preparation
authorization.

### M00_L01 Controlled Activation — 2026-09-15

The accepted preparation, inherited Java 17 baseline build, independent
Architecture / Inheritance Audit, Frozen Backbone audit, M00-specific ownership
audit, and one-new-concept audit are `PASS`. The Architect Design Lock is
`PASS_M00_L01_FINAL_DESIGN_LOCK`.

The canonical lesson is `M00_L01 - Mechanism Architecture Reuse`, with locked
directory identity `M00_L01_MechanismArchitectureReuse`. Its predecessor is
`V00_L09_SwervePoseEstimatorVisionFusion`, which remains `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED` and is not modified by this activation.

This documentation-only controlled activation makes M00_L01 the sole current
`IN_PROGRESS / EDITABLE` lesson and reconciles the active lesson count to `1`.
Implementation authorization and production-code authorization remain `NONE`.
No Java, test, configuration, vendordep, deploy, mechanism, Simulation, Driver
Station / Glass, or real-hardware change is authorized. M00_L01 evidence is
currently `THEORY VERIFIED`; Simulation, Driver Station / Glass, and real
hardware are `NOT APPLICABLE` because the lesson is architecture-only.

At this controlled-activation point, the transition guide and student-facing
learning documentation, their reviews, the final inherited clean
build/regression, final architecture/documentation review, freeze, and User Git
publication remained pending and separately controlled. The later current state
is recorded below.

### M00_L01 Pre-Freeze Documentation and Closure Readiness Reconciliation — 2026-09-15

The paired English and Vietnamese student learning guides are `COMPLETE /
REVIEWED` with identical 21-section structure and equivalent technical meaning.
The initial independent review returned
`HOLD_M00_L01_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_CONSTANTS_AUTHORITY`
because both guides omitted the rule that `Constants.java` remains the default
configuration authority. A minimal two-guide repair added that rule without
defining mechanism configuration values and passed at
`PASS_M00_L01_CONSTANTS_AUTHORITY_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`; the
independent rereview passed at
`PASS_M00_L01_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD`.
The Architect accepted documentation review completion through
`PASS_M00_L01_DOCUMENTATION_REVIEW_COMPLETE_READY_FOR_FINAL_BUILD`.

The User then supplied the distinct final inherited clean build/regression:
`BUILD SUCCESSFUL in 23s`, with 7 actionable tasks executed. Its accepted gate
is `PASS_M00_L01_FINAL_INHERITED_CLEAN_BUILD_REGRESSION`. The final read-only
architecture/documentation closure review passed at
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION`, and
the Architect accepted that result through
`PASS_M00_L01_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Technical/content closure readiness is `PASS`.

```text
M00_L01 Status: IN_PROGRESS
M00_L01 Active State: IN_PROGRESS / EDITABLE
M00_L01 Freeze State: EDITABLE
Active Lesson Count: 1
Design Lock: PASS_M00_L01_FINAL_DESIGN_LOCK
Student Documentation: COMPLETE / REVIEWED
Constants Repair: COMPLETE
Independent Rereview: PASS
Final Inherited Clean Build/Regression: PASS
Final Closure Review: PASS
Technical/Content Closure Readiness: PASS
Production Code Authorization: NONE
Freeze Authorization: PENDING
Git Publication: PENDING USER GIT
```

No Java, test, configuration, vendordep, deploy, runtime, hardware API, or
mechanism implementation changed. Evidence remains `THEORY VERIFIED`; focused
new tests, Simulation, Driver Station / Glass, and real hardware remain `NOT
APPLICABLE`. At that pre-freeze reconciliation point, M00_L01 was not yet
`COMPLETE`, `FROZEN`, `READ-ONLY`, or `PUBLISHED`. Independent reconciliation
review, explicit Architect freeze authorization, lifecycle transition, and
User-owned publication remained pending.

### M00_L01 Final Documentation-Only Freeze Closure — 2026-09-15

The completed pre-freeze documentation reconciliation passed at
`PASS_M00_L01_DOCUMENTATION_RECONCILED_READY_FOR_FREEZE_AUTHORIZATION`, the
Architect accepted it for independent review at
`PASS_M00_L01_DOCUMENTATION_RECONCILIATION_ACCEPTED_READY_FOR_INDEPENDENT_REVIEW`,
and the independent read-only reconciliation review passed at
`PASS_M00_L01_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then explicitly issued `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION`.

That authorization closes the documentation-only lesson lifecycle as follows:

```text
M00_L01 Status: COMPLETE
M00_L01 Active State: COMPLETE / FROZEN / READ-ONLY
M00_L01 Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Design Lock: PASS_M00_L01_FINAL_DESIGN_LOCK
Final Clean Build/Regression: PASS
Final Documentation Review: PASS
Final Reconciliation Review: PASS
Technical/Content Closure: PASS
Production Code Authorization: NONE
Freeze Authorization: PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION
Publication: NOT YET PUBLISHED / PENDING USER GIT
Git Commit: PENDING USER COMMIT
Git Push: PENDING USER PUSH
Remote Verification: PENDING
```

Evidence remains `THEORY VERIFIED`; focused new tests, Simulation, Driver
Station / Glass, and real hardware remain `NOT APPLICABLE`. No Java, test,
configuration, vendordep, deploy, runtime, hardware API, mechanism
implementation, EN/VI guide, or frozen V00_L09 content changed. M00_L02 is not
active and is not created. The M00 module is not declared complete. At this
freeze-recording point, User-owned Git staging, commit, push, remote
verification, and any required publication metadata reconciliation remained
pending.

### M00_L01 Lesson Publication Metadata Reconciliation — 2026-09-15

The User subsequently committed and pushed the frozen M00_L01 lesson. The
accepted publication evidence is commit `83907ab` with subject `Complete
M00_L01 mechanism architecture reuse`. The User verified `HEAD = origin/main =
origin/HEAD = 83907ab` on branch `main`, and the remote publication gate is
`PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`. The later isolated PowerShell
`else` entry error is an interactive syntax issue after the PASS gate and does
not invalidate publication evidence.

```text
M00_L01: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
Lesson Publication Commit: 83907ab
Lesson Publication Commit Message: Complete M00_L01 mechanism architecture reuse
Published Branch: main
Verified Remote: origin/main
Published Repository State: 83907ab
Remote Publication Verification: PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED
Active Lesson Count: 0
M00_L02: NOT ACTIVE / NOT CREATED
Publication Metadata Reconciliation: PENDING USER COMMIT
Publication Metadata Push: PENDING USER PUSH
Final Metadata Remote Verification: PENDING
```

This metadata reconciliation does not alter technical closure, evidence,
runtime applicability, the frozen roadmap, or the M00_L01 Design Lock. The M00
module is not declared complete, and M00_L02 remains inactive and uncreated.

### M00_L02 Final Freeze and Lifecycle Closure — 2026-09-16

The accepted M00_L01 predecessor state is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED`, with primary publication `83907ab`, metadata publication
`f523118`, and final gate `PASS_M00_L01_FINAL_PUBLICATION_COMPLETE`. M00_L01
remains frozen and was not modified.

M00_L02 preparation, the inherited Java 17 baseline, the accidental nested-copy
forensic review and controlled repair, post-repair verification, and the
Architecture / Inheritance Audit are accepted. The Architect issued
`PASS_M00_L02_FINAL_DESIGN_LOCK`, and the controlled activation passed through
`PASS_M00_L02_CONTROLLED_ACTIVATION_RECORDED_READY_FOR_DOCUMENTATION_IMPLEMENTATION_AUTHORIZATION`
and `PASS_M00_L02_CONTROLLED_ACTIVATION_ACCEPTED`.

The separately authorized English and Vietnamese learning guides are complete.
Each contains 25 sections, 15 knowledge-check questions, 15 answers, and an
80-row evidence matrix. The matrices contain 8 `VERIFIED`, 0 `PROVISIONAL`, and
72 `UNKNOWN` rows. The initial independent review accurately recorded
`HOLD_M00_L02_INDEPENDENT_DOCUMENTATION_REVIEW_MISSING_REQUIRED_OWNERSHIP_CONSTANTS_AND_KNOWLEDGE_CHECK_COVERAGE`.
The bounded two-guide repair then passed through
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_AUTHORIZED`,
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`, and
`PASS_M00_L02_MINIMAL_DOCUMENTATION_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_REREVIEW`.
The independent rereview passed at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_BUILD` and
was accepted at
`PASS_M00_L02_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_BUILD`.

The User-supplied final clean build/regression passed with `BUILD SUCCESSFUL in
33s` and 7 actionable tasks executed. Its gates are
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION` and
`PASS_M00_L02_FINAL_USER_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
The final read-only closure review passed at
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L02_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
Documentation reconciliation was authorized by
`PASS_M00_L02_DOCUMENTATION_RECONCILIATION_AUTHORIZED` and is now recorded at
`PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`.
The independent reconciliation review then passed at
`PASS_M00_L02_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`,
and the Architect issued `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION`.

```text
Lesson: M00_L02 - Mechanism Hardware Evidence Audit
Filesystem: M00_L02_MechanismHardwareEvidenceAudit
Status: COMPLETE
Active State: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN / READ-ONLY
Active Lesson Count: 0
Predecessor: M00_L01 - Mechanism Architecture Reuse
Design Lock: PASS_M00_L02_FINAL_DESIGN_LOCK
Documentation Implementation: COMPLETE
Independent Documentation Rereview: PASS
Final User Build: PASS
Final Closure Review: PASS
Documentation Reconciliation: PASS
Independent Reconciliation Review: PASS
Production Code Authorization: NONE
Test Implementation Authorization: NONE
Configuration Authorization: NONE
Runtime Behavior Authorization: NONE
Freeze Authorization: PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION
Git Commit: PENDING USER COMMIT
Git Push: PENDING USER PUSH
Remote Verification: PENDING
Publication Metadata Reconciliation: PENDING
Publication: PENDING USER GIT PUBLICATION
M00_L03: NOT ACTIVE / NOT CREATED
```

The sole new concept remains `MECHANISM HARDWARE EVIDENCE AUDIT`. Evidence is
`THEORY VERIFIED`; focused new tests, Simulation, and Driver Station / Glass are
`NOT APPLICABLE`; real hardware is `REAL HARDWARE DEFERRED`. No production Java,
test, configuration, vendordep, deploy, runtime behavior, mechanism API,
mechanism Observation, command, adapter, constant, CAN/PID configuration, or
telemetry behavior changed. The Frozen Backbone, complete M00 ownership lock,
Constants authority, and locked 16-lesson roadmap remain preserved. M00_L02 is
now `COMPLETE / FROZEN / READ-ONLY`, no lesson is active, and M00_L03 is `NOT
ACTIVE / NOT CREATED`. No further M00_L02 lesson edit is authorized except a
later bounded publication-metadata reconciliation after User Git publication.
Publication remains pending and is not claimed complete.

### M00_L02 Post-Publication Metadata Reconciliation — 2026-09-16

The User completed and pushed the primary frozen-lesson publication. Accepted
evidence records full commit
`65a92a4a5806fd5134e0114e851c4e4cc093c58e` with subject `Complete M00_L02
mechanism hardware evidence audit`. After the push, `HEAD`, `origin/main`, and
`origin/HEAD` were observed aligned at short commit `65a92a4`. The accepted
gates are `PASS_M00_L02_PRIMARY_GIT_PUBLICATION` and
`PASS_M00_L02_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.

```text
M00_L02: COMPLETE / FROZEN / READ-ONLY
Active Lesson Count: 0
Primary Publication Commit: 65a92a4a5806fd5134e0114e851c4e4cc093c58e
Primary Publication Push: PASS
Publication Metadata Reconciliation: COMPLETE / RECORDED
Metadata Commit: PENDING USER COMMIT
Metadata Push: PENDING USER PUSH
Final Remote Verification: PENDING
Final Publication Completion: PENDING
M00_L03: NOT ACTIVE / NOT CREATED
```

This bounded metadata exception does not reopen M00_L02 or authorize technical,
student-guide, or roadmap changes. The future metadata commit does not yet
exist and is not claimed.

### M00_L03 Controlled Documentation-Only Lifecycle Activation — 2026-09-16

M00_L02 is accepted as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
at primary publication `65a92a4a5806fd5134e0114e851c4e4cc093c58e`, metadata
publication `84010ff5022a33a946888fafedbbca0d67439e0c`, and final gate
`PASS_M00_L02_FINAL_PUBLICATION_COMPLETE`. It remains untouched.

The prepared `M00_L03_IntakeFoundation` candidate passed its accepted Java
17.0.16 Temurin baseline with exit code `0`. The original preparation script's
lesson-local `AGENTS.md` expectation was a script-check defect only;
repository-root `AGENTS.md` is authoritative, the candidate was unaffected,
and no recopy was required. Inheritance is 607/607 comparable files and 173/173
protected files with zero missing, extra, or SHA-256-different files.

The Architecture / Inheritance Audit passed at
`PASS_M00_L03_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`, and
the Architect issued `PASS_M00_L03_FINAL_DESIGN_LOCK`.

```text
Active Lesson: M00_L03 - Intake Foundation
Filesystem: M00_L03_IntakeFoundation
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Predecessor: M00_L02 - Mechanism Hardware Evidence Audit
Predecessor State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
Design Lock: PASS_M00_L03_FINAL_DESIGN_LOCK
Production Implementation: PENDING SEPARATE ARCHITECT AUTHORIZATION
Test Implementation: PENDING SEPARATE ARCHITECT AUTHORIZATION
Documentation Implementation: PENDING SEPARATE ARCHITECT AUTHORIZATION
Theory / Architecture: THEORY VERIFIED REQUIRED
Focused Tests: REQUIRED LATER
Simulation: APPLICABLE / REQUIRED LATER
Driver Station / Glass: NOT APPLICABLE COMPLETION GATES
Real Hardware: DEFERRED
M00_L04: NOT ACTIVE / NOT CREATED
```

The sole new concept is an independently owned, vendor-neutral Intake mechanism
foundation. No Intake implementation, test, command, binding, vendor adapter,
configuration, Constants change, runtime behavior, or student guide is
authorized by this activation.

### M00_L03 Implementation, Verification, and Student Documentation Reconciliation — 2026-09-16

The preceding activation record is preserved as the historical pre-
implementation state. Separate Architect/User authorization subsequently
issued `PASS_M00_L03_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED`. The authorized
vendor-neutral Intake foundation is implemented and the independent
implementation review passed and was accepted.

The first focused run executed 16 tests: 15 passed and
`IntakeSubsystemTest.periodicBuildsFreshImmutableSnapshotsFromCurrentInputsAndIntent`
failed because an optional callback was dereferenced at line 69. This was
classified as a test defect. The bounded repair changed only
`IntakeSubsystemTest.java`, guarded the optional callback, and removed no
assertions. The focused retest passed with `BUILD SUCCESSFUL in 4s` and exit
code `0`. The full clean regression passed with `BUILD SUCCESSFUL in 20s`,
exit code `0`, and 7 of 7 actionable tasks executed.

User-owned Simulation passed only for startup, Noop composition, subsystem
integration, and Intake NetworkTables/telemetry presence while Disabled. It is
bounded software-architecture evidence, not proof of physical hardware,
wiring, CAN identity, direction, motion, current, load, force, or stopping.
Driver Station / Glass are not applicable completion gates and real-hardware
verification remains deferred.

The documentation gate
`PASS_M00_L03_DOCUMENTATION_IMPLEMENTATION_AND_LIFECYCLE_RECONCILIATION_AUTHORIZED`
is consumed. The English and Vietnamese student guides are implemented with
matching 34-section structures and 15-question/15-answer review sets. No
production Java, test, configuration, vendordep, or deploy asset was changed by
this documentation task.

```text
Active Lesson: M00_L03 - Intake Foundation
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Implementation: COMPLETE FOR AUTHORIZED M00_L03 SCOPE
Focused Tests: VERIFIED
Full Clean Regression: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION EVIDENCE ONLY
Independent Implementation Review: PASS / ACCEPTED
Student Documentation: IMPLEMENTED
Independent Documentation Review: PENDING
Final User Closure Build: PENDING
Final Closure Review: PENDING
Freeze Authorization: PENDING
COMPLETE / FROZEN / READ-ONLY: NOT YET AUTHORIZED
Publication: NOT YET PUBLISHED
M00_L04: NOT ACTIVE / NOT CREATED
```

No Intake vendor adapter or `Constants.java` change was introduced. Unsupported
hardware facts remain `UNKNOWN`. M00_L04 retains exclusive ownership of
Commands, requirements, bindings, interruption behavior, and default/manual
mechanism ownership.

### M00_L03 Final Closure Review and Lifecycle Reconciliation — 2026-09-17

The preceding implementation and student-documentation record is preserved as
the historical state before independent documentation review and final closure
evidence. The initial independent documentation review returned `HOLD` because
Step 16 of the transition guide incorrectly described the mutable one-cycle
`IntakeIOInputs` transport snapshot as immutable. The authorized one-line
repair changed that wording to `mutable one-cycle input snapshot` without
changing production Java, tests, student guides, configuration, vendordeps, or
deploy assets. The repair passed at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_ONE_LINE_REPAIR_READY_FOR_INDEPENDENT_REREVIEW`,
was accepted at
`PASS_M00_L03_TRANSITION_GUIDE_MUTABLE_IOINPUTS_REPAIR_ACCEPTED_READY_FOR_INDEPENDENT_DOCUMENTATION_REREVIEW`,
and the independent documentation rereview passed at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_READY_FOR_FINAL_USER_CLOSURE_BUILD`.
The Architect accepted that rereview at
`PASS_M00_L03_INDEPENDENT_DOCUMENTATION_REREVIEW_ACCEPTED_READY_FOR_FINAL_USER_CLOSURE_BUILD`.

The User then supplied the final Java 17 closure build/regression:
`BUILD SUCCESSFUL in 42s`, 7 actionable tasks executed, exit code `0`. The
accepted gates are `PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION` and
`PASS_M00_L03_FINAL_USER_CLOSURE_BUILD_REGRESSION_ACCEPTED_READY_FOR_FINAL_CLOSURE_REVIEW`.
The final read-only closure review passed at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_READY_FOR_DOCUMENTATION_RECONCILIATION` and
was accepted at
`PASS_M00_L03_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_RECONCILIATION`.
No technical or substantive documentation defect remains.

This documentation-only reconciliation is complete. M00_L03 remains the sole
`IN_PROGRESS / EDITABLE` lesson with active lesson count `1` and freeze state
`EDITABLE`; it is not yet `COMPLETE / FROZEN / READ-ONLY`. Implementation is
complete for the authorized scope, focused tests and full regression are
verified, the final closure build is verified, bounded software/Noop/composition
Simulation is verified, Driver Station / Glass are not applicable completion
gates, real hardware remains deferred, student documentation and its rereview
are verified, and final closure review is PASS. Independent reconciliation
review, explicit Architect freeze authorization, freeze recording, and
User-owned publication remain pending. M00_L04 remains `NOT ACTIVE / NOT
CREATED`.

### M00_L03 Final Lifecycle Freeze — 2026-09-17

The independent reconciliation review passed at
`PASS_M00_L03_INDEPENDENT_RECONCILIATION_REVIEW_READY_FOR_ARCHITECT_FREEZE_AUTHORIZATION`.
The Architect then explicitly issued
`PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION`.

The authorized documentation-only lifecycle transition is now recorded:

```text
Lesson: M00_L03 - Intake Foundation
Status: COMPLETE
Active State: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Implementation: COMPLETE FOR AUTHORIZED M00_L03 SCOPE
Focused Tests: VERIFIED
Full Regression: VERIFIED
Final Closure Build: VERIFIED
Simulation: VERIFIED — BOUNDED SOFTWARE/NOOP/COMPOSITION ONLY
Driver Station / Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
Student Documentation: VERIFIED
Independent Documentation Rereview: PASS
Final Closure Review: PASS
Lifecycle Reconciliation: COMPLETE
Independent Reconciliation Review: PASS
Architect Freeze Authorization: PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION
Publication: PENDING USER GIT
M00_L04: NOT ACTIVE / NOT CREATED
```

M00_L03 is no longer editable. This freeze does not publish the lesson and
does not authorize M00_L04 preparation or activation. Production Java, tests,
student guides, Constants, configuration, vendordeps, deploy assets, and
frozen M00_L02 remain unchanged. The next lifecycle work is User-owned
publication and any separately authorized publication-metadata reconciliation.

### M00_L03 Primary Publication and Metadata Reconciliation — 2026-09-17

The User completed the Architect-authorized explicit-allowlist primary Git
publication. The accepted primary publication commit is
`3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`; the accepted push result is
`84010ff..3d94dc6  main -> main`. Post-push local `HEAD` and `origin/main`
both resolved to `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, so primary
remote alignment is `PASS`. The accepted gates are
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION` and
`PASS_M00_L03_PRIMARY_GIT_PUBLICATION_ACCEPTED_READY_FOR_PUBLICATION_METADATA_RECONCILIATION`.

```text
Lesson: M00_L03 - Intake Foundation
Lifecycle: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Active Lesson Count: 0
Primary Git Publication: PASS
Primary Publication Commit: 3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3
Primary Push: PASS
Remote Alignment: PASS
Publication Metadata Reconciliation: COMPLETE
Metadata Publication: PENDING USER GIT
Final Publication Verification: PENDING
M00_L04: NOT ACTIVE / NOT CREATED
```

This reconciliation does not claim the metadata commit, metadata push, final
remote verification, or final publication completion. M00_L03 remains frozen
and read-only; M00_L04 remains inactive and uncreated.

### M00_L03 Final Publication and M00_L04 Controlled Activation — 2026-09-18

M00_L03 completed the two-commit publication workflow. Its primary publication
is `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, its metadata publication is
`b2464f66da42a6281acb7bfc709f2a3b83296505`, metadata remote alignment is
`PASS`, and final publication verification passed at
`PASS_M00_L03_FINAL_PUBLICATION_VERIFICATION_READY_FOR_ARCHITECT_FINAL_PUBLICATION_COMPLETE`.
M00_L03 is therefore `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`
and remains untouched.

The User-prepared `M00_L04_IntakeCommandOwnership` candidate passed its
accepted baseline build (`BUILD SUCCESSFUL in 48s`, 7 actionable tasks: 6
executed and 1 up-to-date, exit code `0`). Its Architecture / Inheritance Audit
passed at `PASS_M00_L04_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`:
279 of 279 comparable inherited files matched M00_L03 with no governed delta.
The Architect then issued `PASS_M00_L04_FINAL_DESIGN_LOCK`.

```text
Active Lesson: M00_L04 - Intake Command Ownership
Filesystem: M00_L04_IntakeCommandOwnership
Status: IN_PROGRESS
Active State: IN_PROGRESS / EDITABLE
Freeze State: EDITABLE
Active Lesson Count: 1
Predecessor: M00_L03 - Intake Foundation
Predecessor State: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
Preparation: PASS
Baseline Build: PASS
Architecture / Inheritance Audit: PASS
Final Design Lock: PASS_M00_L04_FINAL_DESIGN_LOCK
One New Concept: SCHEDULER-MANAGED MANUAL OWNERSHIP OF THE EXISTING INTAKE CAPABILITY
Locked Command: RunIntakeCommand
Locked Binding: Xbox Right Bumper / whileTrue
Implementation: NOT STARTED
Implementation Authorization: PENDING
Focused Tests: NOT RUN
Simulation: NOT RUN
Driver Station: NOT RUN
Glass: NOT APPLICABLE
Real Hardware: REAL HARDWARE DEFERRED
M00_L05: NOT ACTIVE / NOT CREATED
```

This controlled activation authorizes lifecycle documentation only. It does
not authorize Java or test implementation. The locked future production scope
is one `RunIntakeCommand` plus the bounded `RobotContainer` Right Bumper
`whileTrue` binding; focused lifecycle and binding tests remain pending a
separate implementation authorization. No Intake subsystem, IO, Observation,
telemetry, Constants, vendor adapter, hardware configuration, or later-lesson
work is authorized.

### M00_L04 Post-Verification Documentation and Lifecycle Reconciliation — 2026-09-18

The preceding controlled-activation section is preserved as the historical
pre-implementation state. Separate Architect/User authorization subsequently
issued
`PASS_M00_L04_CONTROLLED_ACTIVATION_ACCEPTED_PRODUCTION_TEST_IMPLEMENTATION_AUTHORIZED`.
The exact implementation created `RunIntakeCommand.java`, modified only the
bounded `RobotContainer` controller composition, created the two authorized
focused-test files, and narrowly updated `IntakeArchitectureBoundaryTest.java`.
No other production or test Java changed.

`RunIntakeCommand` requires exactly the existing `IntakeSubsystem`, requests
Intake once in `initialize()`, performs no repeated request in `execute()`,
never self-finishes, and delegates unconditionally to `IntakeSubsystem.stop()`
from `end(...)`. RobotContainer binds the existing controller's semantic Right
Bumper through `whileTrue` and preserves the Back/View Prepare Autonomous
binding. No Intake default command, IO change, Observation change, telemetry
change, Constants change, vendor adapter, or hardware selection was added.

Authoritative User evidence records 14/14 focused tests PASS with `BUILD
SUCCESSFUL in 28s`, four tasks executed, and exit code `0`; the full clean
regression passed with `BUILD SUCCESSFUL in 47s`, seven tasks executed, and
exit code `0`. Bounded WPILib Simulation is `SIMULATION VERIFIED / BOUNDED`.
Bounded Driver Station verification is `VERIFIED / BOUNDED` and observed the
software sequence `STOPPED -> INTAKE_REQUESTED -> STOPPED` across Right Bumper
hold and release. `Available=false` and `Connected=false` are expected because
the selected implementation remains `IntakeIONoop`. Glass is supporting
read-only visualization only and `NOT APPLICABLE` as a distinct completion
gate. Real hardware remains `REAL HARDWARE DEFERRED`.

The independent implementation review passed at
`PASS_M00_L04_INDEPENDENT_IMPLEMENTATION_REVIEW_READY_FOR_DOCUMENTATION_AUTHORIZATION`
and was accepted at
`PASS_M00_L04_INDEPENDENT_IMPLEMENTATION_REVIEW_ACCEPTED_READY_FOR_DOCUMENTATION_IMPLEMENTATION`.
The paired English and Vietnamese student guides are now created with matching
34-section and 15-question/15-answer structures. Documentation reconciliation
is complete for this authorized task and remains pending independent
documentation review.

```text
M00_L04 Status: IN_PROGRESS
M00_L04 Active State: IN_PROGRESS / EDITABLE
M00_L04 Freeze State: EDITABLE
Active Lesson Count: 1
Implementation: COMPLETE
Focused Tests: VERIFIED / 14 OF 14 PASS
Full Regression: VERIFIED
Simulation: SIMULATION VERIFIED / BOUNDED
Driver Station: VERIFIED / BOUNDED
Glass: NOT APPLICABLE AS A DISTINCT COMPLETION GATE
Real Hardware: REAL HARDWARE DEFERRED
Independent Implementation Review: PASS
Student Documentation: CREATED / PENDING INDEPENDENT DOCUMENTATION REVIEW
Freeze Authorization: NOT YET AUTHORIZED
Publication: NOT STARTED
M00_L05: NOT ACTIVE / NOT CREATED
```

`IntakeIOInputs` remains a mutable one-cycle transport/input snapshot;
`IntakeObservation` remains an immutable vendor-neutral observation/value
snapshot. No physical motor, CAN, wiring, direction, speed, current, sensing,
game-piece, or physical-stop claim is made. M00_L04 is not `COMPLETE`,
`FROZEN`, `READ-ONLY`, or published.

### M00_L04 Final Lifecycle Reconciliation and Freeze — 2026-09-18

The preceding post-verification section is preserved as historical pre-closure
state. The initial documentation HOLD was resolved by the authorized bounded
guide repair and independent rereview. The initial final-closure HOLD was
resolved by the authorized transition-guide reconciliation and independent
confirmation. The resumed independent final closure review passed at
`PASS_M00_L04_FINAL_CLOSURE_REVIEW_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`,
and the Architect accepted it at
`PASS_M00_L04_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_FINAL_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`.

```text
M00_L03: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L04: COMPLETE / FROZEN / READ-ONLY
Freeze State: FROZEN
Editable Boundary: NONE
Active Lesson: NO
Active Lesson Count: 0
Implementation: COMPLETE / VERIFIED TO AUTHORIZED LESSON SCOPE
Documentation: COMPLETE / VERIFIED
Final Closure Build: PASS / BUILD SUCCESSFUL IN 23s / 7 OF 7 TASKS EXECUTED / EXIT CODE 0
Final Closure Review: PASS
Simulation: SIMULATION VERIFIED / BOUNDED
Driver Station: VERIFIED / BOUNDED
Glass: NOT APPLICABLE AS A DISTINCT COMPLETION GATE
Real Hardware: REAL HARDWARE DEFERRED
Git Publication: PENDING USER ACTION
Publication State: NOT YET PUBLISHED
M00_L05: NOT ACTIVE / NOT CREATED
```

The frozen lesson preserves scheduler-managed manual Intake ownership through
the semantic Right Bumper `whileTrue` binding, one-shot initialization request,
and unconditional subsystem-owned stop on command end. `IntakeIONoop` remains
deterministic, vendor-neutral, unavailable, disconnected, and incapable of
physical output. No M00 lesson is active. This reconciliation does not perform
Git, claim publication, invent a commit, or activate M00_L05.

### M00_L04 Primary Publication and Metadata Reconciliation — 2026-09-18

The preceding lifecycle-freeze section is preserved as the historical
pre-publication state. The User completed the primary M00_L04 publication at
commit `5c86be3` with subject `Complete M00_L04 intake command ownership`.
Accepted post-push evidence records `HEAD = origin/main = origin/HEAD =
5c86be3`; primary remote alignment is `PASS`.

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

Primary publication is complete; the separate metadata commit does not yet
exist. Independent metadata review, User-owned metadata commit/push, metadata
remote verification, and final publication verification remain pending. No
M00 lesson is active, and M00_L05 is not created or activated.

### M00_L04 Final Publication and M00_L05 Controlled Activation — 2026-09-18

The preceding section is preserved as historical pre-metadata state. The User
completed M00_L04 metadata publication at commit `24738e6` with subject
`Record M00_L04 publication metadata`. Accepted evidence records `HEAD =
origin/main = origin/HEAD = 24738e6`, metadata remote alignment `PASS`, and
final publication verification `PASS`. M00_L04 is `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / VERIFIED` and remains untouched.

The User then prepared `M00_L05_FeederFoundation` from final M00_L04 through
copy, rename, candidate-only artifact cleanup, and a Java 17 baseline build.
The accepted baseline is `BUILD SUCCESSFUL in 41s` with 7 actionable tasks, 6
executed and 1 up-to-date. The independent inheritance audit found 285/285
governed files byte-identical with zero unexpected delta and passed at
`PASS_M00_L05_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`.
The Architect issued
`PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

```text
M00_L04: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L04 Primary Commit: 5c86be3
M00_L04 Metadata Commit: 24738e6
M00_L05: IN_PROGRESS / EDITABLE WITHIN LOCKED DESIGN BOUNDARY
Freeze State: NOT FROZEN / EDITABLE WITHIN LOCKED DESIGN BOUNDARY
Active Lesson Count: 1
Current Active M00 Lesson: M00_L05
One New Concept: FEEDER AS ONE INDEPENDENTLY OWNED TRANSPORT MECHANISM CAPABILITY
Runtime Strategy: FeederIONoop ONLY
Dynamic Feeder Simulation: NOT AUTHORIZED
Real Adapter: NOT AUTHORIZED
Real Hardware: REAL HARDWARE DEFERRED
Implementation Authorization: PENDING
Constants.java Changes: NOT AUTHORIZED
M00_L06: NOT ACTIVE / NOT CREATED
```

The locked future production boundary is exactly five new Feeder foundation
types plus bounded modifications to `RobotContainer.java` and
`RobotTelemetry.java`. The locked focused-test boundary is exactly the six
named Feeder tests. This activation does not implement Feeder, authorize Java
or test changes, configure CAN 45-49, or activate M00_L06.

### M00_L05 Post-Verification Documentation Reconciliation — 2026-09-19

The preceding activation record is historical. Separate authorization was
consumed, and M00_L05 implementation is complete within the exact
five-created/two-modified production boundary and six-test boundary.

The initial focused run passed 18 of 19 tests. The single failure was a brittle
architecture-test text match against the valid Javadoc phrase `current cycle`.
`FeederArchitectureBoundaryTest` was repaired to inspect non-static,
non-synthetic `FeederIOInputs` fields reflectively. All six focused test
classes then passed with `BUILD SUCCESSFUL in 26s` and exit code `0`.

The initial full clean regression passed 681 of 682 tests. The remaining
failure was a test-isolation defect: `new FeederSubsystem(null)` registered a
partially constructed `SubsystemBase` with the singleton `CommandScheduler`
before null rejection. The one-file `FeederSubsystemTest` repair added
`@AfterEach` cleanup through `unregisterAllSubsystems()` and preserved the
null-rejection assertion. The final full clean regression passed all 682 tests
with `BUILD SUCCESSFUL in 51s`, seven tasks executed, and exit code `0`.

Bounded WPILib Simulation and HALSIM Robot State verification passed with
`Available=false`, `Connected=false`, and `RequestedState=STOPPED` in both
Disabled and Teleoperated Enabled states. These results verify composition,
Noop runtime stability, observation flow, telemetry publication, and software
state only. Real Feeder hardware remains `REAL HARDWARE DEFERRED`; physical
hardware facts remain unknown, and CAN 45-49 remains a planning reservation.

The independent implementation review, paired English and Vietnamese student
guides, bounded documentation repair, independent documentation rereview,
final closure build, and independent final closure review passed. The accepted
gate `PASS_M00_L05_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`
authorized this documentation-only lifecycle freeze. M00_L05 is now
`COMPLETE / FROZEN / READ-ONLY`, active lesson count is `0`, and no M00 lesson
is active. User evidence records primary publication commit
`5709f1d74b3318303bcc56779315b243dd81770b` (`Complete M00_L05 feeder
foundation`) with `HEAD == origin/main`. Publication metadata reconciliation is
complete; later User evidence records metadata publication commit
`1d6fadeec57fbfd3be245746b21d06e58b79518f` (`Record M00_L05 publication
metadata`), remote alignment PASS, and final publication gate
`PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`.

### M00_L06 Controlled Activation — 2026-09-19

The User prepared `M00_L06_FeederCommandOwnership` from final published
M00_L05, removed candidate build artifacts, and supplied a passing baseline:
`BUILD SUCCESSFUL in 40s`, 7 actionable tasks, 6 executed, 1 up-to-date, exit
code `0`. The independent architecture/inheritance audit compared 299 governed
files: 299 were byte-identical, with no missing, added, or changed files. The
accepted audit gate is
`PASS_M00_L06_ARCHITECTURE_INHERITANCE_AUDIT_READY_FOR_FINAL_DESIGN_LOCK`, and
the Architect accepted it before issuing
`PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`.

M00_L06 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; active lesson count is `1`. Its one new concept is
scheduler-managed command ownership of the existing Feeder semantic API. The
locked future scope is `RunFeederCommand`, exactly one `FeederSubsystem`
requirement, one `requestFeed()` call from `initialize()`, empty `execute()`,
non-terminating `isFinished()`, unconditional subsystem `stop()` on normal or
interrupted end, and Left Bumper `whileTrue` composition in RobotContainer. No
default Feeder command is authorized.

Implementation is `NOT STARTED` and requires separate authorization. No Java,
test, Constants, CAN-registry, vendordep, Gradle, deploy, hardware, or prior-
lesson change is authorized by activation. `FeederIONoop` remains the only
runtime implementation, real hardware remains deferred, CAN 45-49 remains a
planning reservation only, and M00_L14 through M00_L16 scopes remain protected.

### M00_L06 Final Lifecycle Freeze — 2026-09-20

The preceding controlled-activation section is preserved as historical. The
authorized `RunFeederCommand` and Left Bumper binding implementation completed
within the exact two-file production boundary. The four focused tests, full
clean regression, and bounded Simulation evidence passed. The initial closure
review `HOLD`, three-item bounded documentation/lifecycle repair, later
transition-history `HOLD`, and final bounded transition-history repair are
preserved in the lesson transition guide.

The final Independent Closure Rereview returned `READY_FOR_FREEZE` with no
remaining findings. The Architect accepted that result through
`PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and authorized
`AUTHORIZED_FOR_FREEZE`. M00_L06 is now `COMPLETE / FROZEN / READ-ONLY`, active
lesson count is `0`, and no M00 lesson is active. Evidence remains `THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; `FeederIONoop` remains
the only runtime implementation, and CAN 45-49 remains planning-only.

User-owned publication is pending and no Git commit or push is claimed.
M00_L07 remains `NOT ACTIVE / NOT CREATED`, and M00_L14 through M00_L16 remain
protected future scope.

### M00_L06 Primary Publication and Metadata Reconciliation — 2026-09-20

The preceding lifecycle-freeze section is preserved as the historical
pre-publication state. The accepted gate `PASS_M00_L06_PRIMARY_PUBLICATION`
records the User-owned primary publication commit
`f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject `Complete M00_L06
Feeder command ownership`. Primary push is `PASS`, and accepted remote evidence
records `HEAD = origin/main = f102a5e662877f8cb49eb63f2cfd888ac356bea4`;
primary remote alignment is `PASS`.

M00_L06 remains `COMPLETE / FROZEN / READ-ONLY`, active lesson count remains
`0`, and no M00 lesson is active. Publication metadata reconciliation is
`COMPLETE / PREPARED FOR USER COMMIT`. The separate metadata Git publication
and final publication verification remain `PENDING`; M00_L06 is not yet
recorded as final `PUBLISHED / VERIFIED`. M00_L07 remains `NOT ACTIVE / NOT
CREATED`, and M00_L14 through M00_L16 remain protected future scope.

### M00_L07 Controlled Activation — 2026-09-20

The preceding M00_L06 publication-metadata section is preserved as historical.
The accepted activation prerequisite now records M00_L06 as `COMPLETE / FROZEN
/ READ-ONLY / PUBLISHED / VERIFIED`. The User prepared
`M00_L07_FlywheelFoundation` from that predecessor, removed candidate generated
artifacts, and supplied a passing untouched-inheritance baseline: `BUILD
SUCCESSFUL in 38s`, 6 actionable tasks, all 6 executed. The accepted
architecture/inheritance audit found 306 of 306 governed files byte-identical,
with zero missing, added, or changed files. The Architect accepted
`PASS_M00_L07_FINAL_DESIGN_LOCK`.

M00_L07 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; active lesson count is `1`, and the current active M00 lesson is
`M00_L07`. Its one new concept is: Flywheel is one independently owned
rotational-speed mechanism. Controlled Activation changes lifecycle and
documentation identity only. Implementation remains `PENDING SEPARATE
AUTHORIZATION`; no production Java, test Java, command, controller binding,
Constants entry, hardware adapter, or autonomous integration is authorized or
implemented by activation.

The locked future runtime is `FlywheelIONoop` only. CAN 50-54 remains a planning
reservation only, physical Flywheel hardware remains unknown, and real hardware
remains deferred. M00_L08 is `INACTIVE / NOT CREATED`; M00_L08 closed-loop
velocity, M00_L09 ready-at-speed, M00_L14 Shoot Coordination, and M00_L16
autonomous event integration remain protected future scope.

### M00_L07 implementation, verification, and documentation reconciliation — 2026-09-20

The preceding Controlled Activation section is preserved as historical
pre-implementation state. After
`PASS_M00_L07_GOVERNANCE_ADJUDICATION` and
`PASS_M00_L07_INDEPENDENT_ACTIVATION_REVIEW_AFTER_ADJUDICATION`, the exact
bounded implementation was authorized and accepted at
`PASS_M00_L07_IMPLEMENTATION_REPORT_ACCEPTED`. It created the vendor-neutral
Flywheel IO/IOInputs, `FlywheelIONoop`, immutable Observation, subsystem, and
read-only telemetry foundation and integrated it through `RobotContainer` and
`RobotTelemetry`. Six focused test classes were created; no inherited test was
modified.

The initial Independent Static Review accepted production and returned `HOLD`
for four focused-test quality findings. The first bounded test-only repair
closed the request-ordering, negative-infinity, and RobotContainer absence
boundary findings, but static rereview retained one `HOLD` for
comment-sensitive raw-source checks in
`FlywheelArchitectureBoundaryTest.java`. A final single-file repair replaced
those checks with semantic type inspection and comment-free import parsing.
The final review passed at
`PASS_M00_L07_FINAL_INDEPENDENT_STATIC_REREVIEW`.

User evidence records all six focused test classes as `BUILD SUCCESSFUL` and
`FOCUSED TESTS: PASS`. The clean full regression passed at `BUILD SUCCESSFUL
in 36s`, with 7 actionable tasks and all 7 executed. Bounded WPILib Simulation
passed at `PASS_M00_L07_BOUNDED_SIMULATION` for Disabled initial, Teleop
Enabled idle with no Flywheel action, and return to Disabled. Each checkpoint
reported unavailable, disconnected, velocity invalid, `velocityRpm = 0.0`,
and `STOPPED`. Because velocity is invalid, `0.0` is the canonical Noop
invalid representation, not a verified physical zero-speed measurement.

Evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
M00_L07 remains the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson with active lesson count `1`; it is not complete, frozen,
read-only, or published. Independent Closure Review is the next gate. M00_L08
remains inactive/uncreated; closed-loop velocity, ready-at-speed, Flywheel
command ownership, shooting coordination, Feeder/Flywheel orchestration,
NamedCommands, and autonomous mechanism integration remain protected future
scope.

### M00_L07 controlled freeze transition — 2026-09-20

The preceding implementation and documentation-reconciliation section remains
the historical pre-freeze state. The Independent Closure Review passed at
`PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` with verdict
`CLOSURE_REVIEW_PASS`, recommendation `READY_FOR_FREEZE_AUTHORIZATION`, and
exact remaining findings `NONE`. The Architect accepted the result and
authorized the controlled freeze transition.

M00_L07 is now `COMPLETE / FROZEN / READ-ONLY`. Active lesson count is `0`,
and no M00 lesson is active. Implementation, final static review, focused
tests, clean full regression, bounded Simulation, documentation reconciliation,
and Independent Closure Review remain accepted. Evidence remains `THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; invalid Noop
`velocityRpm = 0.0` remains non-physical evidence. `FlywheelIONoop` remains
the only Flywheel runtime adapter, and CAN 50-54 remains planning-only.

Publication is `PENDING / NOT YET PUBLISHED`. No commit, push, remote
alignment, or publication verification is claimed. M00_L08 remains `INACTIVE /
NOT CREATED`, and all M00_L08, M00_L09, coordination, command-ownership, and
autonomous-integration boundaries remain protected.

### M00_L07 Primary Publication and Metadata Reconciliation — 2026-09-20

The preceding controlled-freeze section is preserved as the historical
pre-publication state. Accepted gate
`PASS_M00_L07_PRIMARY_PUBLICATION_EVIDENCE` records the User-owned primary
publication commit `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` with subject
`Complete M00_L07 Flywheel foundation`. Primary push is `PASS`, and accepted
remote evidence records `HEAD = origin/main =
50e5f440bb0c9d96bdcd57eed533651d8d59ca93`; primary remote alignment is
`PASS`.

M00_L07 remains `COMPLETE / FROZEN / READ-ONLY`. Active lesson count remains
`0`, and the current active M00 lesson remains `NONE`. Publication metadata
reconciliation is `COMPLETE / PREPARED FOR USER COMMIT`. The metadata commit
and push remain `PENDING USER ACTION`, and final publication verification
remains `PENDING`; final `PUBLISHED / VERIFIED` status is not yet claimed.
M00_L08 remains `INACTIVE / NOT CREATED`.

---

## 15. Final Report

Always report

- Required Documents
- Architecture Check
- Source Lesson
- Active Lesson
- Files Inspected
- Files Changed
- Baseline Build
- Build Result
- Simulation Result
- Driver Station / Glass Result
- Real Robot Result
- Documentation Result
- Git Commit Result
- Git Push Result
- Known Issues
- Lesson Status

Only report verified facts.

---

## 16. Governance Revision History

| Version | Date | Status | Decision |
| --- | --- | --- | --- |
| 1.0 | 2026-07-18 | FROZEN | Initial repository governance. |
| 1.1 | 2026-08-01 | FROZEN | APPROVED: recognize `frc.robot.observation` as the permanent immutable read-model boundary; control flow remains unchanged. |
| 1.2 | 2026-08-08 | FROZEN | APPROVED: fixed role ownership, transition-guide lifecycle, module structure, durable external operator-input Observation exception, and referenced lesson-specific architecture decision records. |
| 1.3 | 2026-08-16 | FROZEN | APPROVED: authorize the post-S00 A00 roadmap and `module_A00` location without changing S00 or the Frozen Backbone. |
| 1.4 | 2026-08-16 | FROZEN | APPROVED: register the post-A00 A01 roadmap and `module_A01` successor boundary without creating lessons or changing frozen S00/A00 architecture. |
| 1.5 | 2026-08-23 | FROZEN | APPROVED: register the post-A01 V00 roadmap and future `module_V00` successor boundary without creating or activating V00_L01, selecting a camera vendor, or changing frozen S00/A00/A01 architecture. |
| 1.6 | 2026-08-24 | FROZEN | APPROVED: add the exceptional SUSPENDED / READ-ONLY lifecycle and narrowly reopen A01_L08 for safety/robustness governance review while preserving V00_L02 unfinished work read-only; implementation is not authorized. |
| 1.7 | 2026-08-24 | FROZEN | APPROVED: expand the A01_L08 reopen scope for scheduler-native autonomous terminal ownership, SAFE_STOP ownership, a defensive Teleop-mode output gate, and removal of manual child lifecycle delegation; implementation remains unauthorized. |
| 1.8 | 2026-08-25 | FROZEN | APPROVED: expand the A01_L08 governance boundary for the scheduler-native AutoBuilder exception design and Robot-level scheduler `RuntimeException` boundary across the exact four-file production scope and named focused tests; implementation remains unauthorized. |
| 1.9 | 2026-08-26 | FROZEN | APPROVED: record final A01_L08 verification and re-freeze it as `COMPLETE / FROZEN / READ-ONLY`; V00_L02 remains `SUSPENDED / READ-ONLY` pending separate reconciliation and resume approval. |
| 1.10 | 2026-08-26 | FROZEN | APPROVED: reconcile stale historical V00_L01 lifecycle metadata; current reconstructed V00_L01 is the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson pending final closure, and V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`. |
| 1.11 | 2026-08-26 | FROZEN | APPROVED: record final V00_L01 architecture and closure review PASS and freeze the reconstructed lesson as `COMPLETE / FROZEN / READ-ONLY`; V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED`. |
| 1.12 | 2026-08-27 | FROZEN | APPROVED: complete the downstream reconstruction reconciliation and activate current canonical V00_L02 as the sole `IN_PROGRESS / EDITABLE` lesson with verified reconstructed baseline and reviewed design lock; implementation remains unauthorized, and V00_L01 remains published and frozen at `7d52ebf`. |
| 1.13 | 2026-08-27 | FROZEN | APPROVED: record the exact two-file V00_L02 implementation, authoritative User verification PASS, documentation completion, and pre-closure architecture preservation; V00_L02 remains `IN_PROGRESS / EDITABLE` pending final read-only architecture review and closure authorization. |
| 1.14 | 2026-08-27 | FROZEN | APPROVED: record final V00_L02 architecture and closure review PASS and freeze the lesson as `COMPLETE / FROZEN / READ-ONLY`; User-owned Git publication remains pending and V00_L03 is not activated. |
| 1.15 | 2026-08-27 | FROZEN | APPROVED: reconcile current V00_L01 and V00_L02 publication metadata to User-verified published commits `7d52ebf` and `53e9b9f`; no lesson is active, V00_L03 remains uncreated, and A01_L10 remains prohibited. |
| 1.16 | 2026-08-27 | FROZEN | APPROVED: activate prepared V00_L03 as the sole `IN_PROGRESS / EDITABLE` lesson after clean inheritance, User-verified Java 17 baseline build, architecture audit PASS, and approved Design Lock; implementation remains unauthorized, while V00_L01 and V00_L02 remain published and frozen. |
| 1.17 | 2026-08-27 | FROZEN | APPROVED: record V00_L03 implementation verification, test-oracle clarification, documentation completion, and final read-only architecture audit PASS; V00_L03 remains `IN_PROGRESS / EDITABLE` pending ChatGPT closure review and freeze, with Git publication User-owned and pending. |
| 1.18 | 2026-08-27 | FROZEN | APPROVED: record V00_L03 final closure review PASS and freeze the lesson as `COMPLETE / FROZEN / READ-ONLY`; no lesson is active, User-owned Git publication remains pending, V00_L04 is not started, and A01_L10 remains prohibited. |
| 1.19 | 2026-08-27 | FROZEN | APPROVED: reconcile User-confirmed V00_L03 publication at `cc20d62` and record V00_L04 as a prepared inherited copy with User-verified Java 17 baseline build PASS; V00_L04 is not activated, not editable, and implementation remains unauthorized; no lesson is active. |
| 1.20 | 2026-08-28 | FROZEN | APPROVED: activate V00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson after inheritance and architecture PASS and Architect approval of the refined Design Lock; implementation is not started and remains pending separate authorization. |
| 1.21 | 2026-08-28 | FROZEN | APPROVED: record the exact two-file V00_L04 implementation, authoritative User verification PASS, post-implementation architecture review PASS, artifact cleanup PASS, and documentation reconciliation; V00_L04 remains the sole `IN_PROGRESS / EDITABLE` lesson pending final closure review, freeze, and User-owned Git publication. |
| 1.22 | 2026-08-28 | FROZEN | APPROVED: close and freeze V00_L04 as `COMPLETE / FROZEN / READ-ONLY` after final architecture/documentation review PASS; no V00 lesson remains active, V00_L05 is not created, and publication remains `PENDING USER GIT`. |
| 1.23 | 2026-08-28 | FROZEN | APPROVED: reconcile User-confirmed V00_L04 publication at `5461555`; V00_L04 remains `COMPLETE / FROZEN / READ-ONLY`, no V00 lesson is active, and publication is recorded as `PUBLISHED @ 5461555 / USER VERIFIED`. |
| 1.24 | 2026-08-28 | FROZEN | APPROVED: activate the prepared ADR-locked V00_L05 identity as the sole `IN_PROGRESS / EDITABLE` lesson and record the Architect-approved pure pose-candidate Design Lock; implementation remains `NOT STARTED / NOT AUTHORIZED`, predecessor lessons remain frozen, and User-owned Git operations remain pending. |
| 1.25 | 2026-08-28 | FROZEN | APPROVED: record the authorized V00_L05 implementation, test-only Java 17 compatibility and noncommutativity-fixture repairs, API reflection hardening, authoritative User verification PASS, and architecture/documentation PASS; V00_L05 remains `IN_PROGRESS / EDITABLE` pending final closure authorization and User-owned publication. |
| 1.26 | 2026-08-28 | FROZEN | APPROVED: close and freeze V00_L05 as `COMPLETE / FROZEN / READ-ONLY` after final architecture, verification, and documentation PASS; V00_L06 remains inactive/not created, and User Git publication remained pending at that historical closure point. |
| 1.27 | 2026-08-28 | FROZEN | APPROVED: reconcile User-confirmed V00_L05 publication at `6482160`; retain `COMPLETE / FROZEN / READ-ONLY`, distinguish the actual lesson publication from the future metadata-reconciliation commit, and keep V00_L06 inactive. |
| 1.28 | 2026-08-29 | FROZEN | APPROVED: activate the VERIFIED Markdown mirror reading policy while preserving authoritative English PDF precedence, integrity verification, direct-PDF fallback, and poster visual-reference requirements. |
| 1.29 | 2026-08-30 | FROZEN | APPROVED: reconcile User-confirmed V00_L06 publication at `1327bf4` and lesson-local metadata reconciliation at `49c4286`; record V00_L01-L06 as published/frozen, no active V00 lesson, and V00_L07 as a prepared inherited pre-activation candidate. |
| 1.30 | 2026-08-30 | FROZEN | APPROVED: reconcile repository current lifecycle through User-published V00_L07 at `d58bef0` and lesson-local metadata reconciliation at `618dd09`; record no active V00 lesson and keep V00_L08 `NOT STARTED / NOT ACTIVATED / NOT IMPLEMENTED / NOT PUBLISHED`. |
| 1.31 | 2026-08-31 | FROZEN | APPROVED: record the documentation-only exceptional V00_L07 reopen for exactly R1/R2/R3; V00_L07 is the sole `REOPENED / IN_PROGRESS / EDITABLE` lesson, implementation remains unauthorized, V00_L08 remains unactivated, and the original `d58bef0` publication remains historical. |
| 1.32 | 2026-09-07 | FROZEN | APPROVED: reconcile later User-verified Teleop/Autonomous usability, retain the unresolved BL quantitative anomaly as `KNOWN / DEFERRED HARDWARE MAINTENANCE` without a quantitative drivetrain PASS claim, and record V00_L07 as closure-ready while it remains `REOPENED / IN_PROGRESS / EDITABLE` pending final read-only closure review, explicit re-freeze approval, and User-owned corrected repair publication; V00_L08 remains untouched and unactivated. |
| 1.33 | 2026-09-07 | FROZEN | APPROVED: record final V00_L07 closure review PASS and re-freeze the repaired lesson as `COMPLETE / FROZEN / READ-ONLY`; retain historical `d58bef0` as pre-repair provenance, keep corrected publication `PENDING USER PUBLICATION`, record no active V00 lesson, and leave stale unactivated V00_L08 untouched. |
| 1.34 | 2026-09-07 | FROZEN | APPROVED: reconcile User-confirmed corrected V00_L07 publication at `4704cfc`; retain historical `d58bef0`, record `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`, verify HEAD == origin/main, and leave V00_L08 untouched and unactivated. |
| 1.35 | 2026-09-07 | FROZEN | APPROVED: supersede the prospective reconstruction-only procedure for the exact existing V00_L08 candidate with a one-time preservation-based reconciliation; preserve the exact seven-file donor boundary, mandatory checkpoint and fresh verification, and keep L08 unactivated/read-only until post-reconciliation review. |
| 1.36 | 2026-09-09 | FROZEN | APPROVED: record the User-authorized V00_L08 activation and bounded repair scope, preserve the observation-only runtime boundary and private Limelight schema boundary, and keep V00_L09/fusion/drivetrain/autonomous/configuration changes excluded. This entry does not approve an implementation-selected freshness recipe or declare runtime readiness. |
| 1.37 | 2026-09-10 | FROZEN | APPROVED: clarify that the bounded V00_L08 repair does not promote implementation-selected freshness, threshold, recovery, or coherence behavior into global governance and remains verification-gated. |
| 1.38 | 2026-09-10 | FROZEN | APPROVED: record final V00_L08 architecture/closure PASS and freeze the lesson as `COMPLETE / FROZEN / READ-ONLY`; retain H1 and NetworkTables qualifications, keep V00_L09/fusion out of scope, and leave Git publication pending User commit/push. |
| 1.39 | 2026-09-10 | FROZEN | APPROVED: reconcile User-confirmed V00_L08 publication at `f34b210`, record message `Complete V00_L08 real vision adapter integration` and push PASS to `origin/main`, while preserving the frozen lesson state and V00_L09 boundary. |
| 1.40 | 2026-09-11 | FROZEN | APPROVED: perform the documentation-only controlled activation of the prepared ADR-locked V00_L09 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve V00_L08 frozen protection and keep implementation, verification, closure, and User Git publication pending. |
| 1.41 | 2026-09-12 | FROZEN | APPROVED: reconcile completed V00_L09 implementation, User-verified automated and Simulation evidence, Driver Station / Glass PASS, and the narrow test-only failure-boundary fixture repair; retain L09 as the sole `IN_PROGRESS / EDITABLE` lesson pending real Limelight timing/result, real estimator-fusion, final architecture/documentation closure, freeze, and User publication gates. |
| 1.42 | 2026-09-15 | FROZEN | APPROVED: record final V00_L09 documentation review and explicit `PASS_V00_L09_FINAL_FREEZE_AUTHORIZATION`; transition L09 to `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserve V00_L08 frozen protection, and leave User Git commit, push, and publication pending. |
| 1.43 | 2026-09-15 | FROZEN | APPROVED: reconcile User-verified V00_L09 implementation publication at `6548c98`, record `origin/main = 6548c98`, `origin/HEAD = 6548c98`, push `COMPLETE / VERIFIED`, and `PASS_V00_L09_REMOTE_PUBLICATION_VERIFIED`; preserve the distinct metadata-reconciliation commit as `PENDING USER COMMIT`. |
| 1.44 | 2026-09-15 | FROZEN | APPROVED: record V00 final closure through metadata reconciliation `5d36529` and `PASS_V00_MODULE_FINAL_CLOSURE_CONFIRMED`; register the locked 16-lesson M00 ADR and `PASS_M00_GOVERNANCE_PREPARATION_AUTHORIZED`, exact M00_L01 identity, predecessor, future destination, baseline command, bilingual/evidence rules, active lesson count `0`, and `NOT ACTIVE / NOT YET CREATED` with no implementation authorization. |
| 1.45 | 2026-09-15 | FROZEN | APPROVED: consume `PASS_M00_L01_FINAL_DESIGN_LOCK` and record documentation-only controlled activation of M00_L01 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve V00_L09 frozen protection and retain implementation and production-code authorization as `NONE`. |
| 1.46 | 2026-09-15 | FROZEN | APPROVED: reconcile completed bilingual documentation, the preserved Constants-authority HOLD and repair, independent rereview PASS, User final inherited clean build/regression PASS, final closure review PASS, and technical/content readiness PASS while retaining M00_L01 as `IN_PROGRESS / EDITABLE`, active lesson count `1`, freeze authorization pending, and User Git publication pending. |
| 1.47 | 2026-09-15 | FROZEN | APPROVED: consume `PASS_M00_L01_FINAL_FREEZE_AUTHORIZATION` and record M00_L01 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`; preserve the documentation-only technical boundary, keep M00_L02 inactive and uncreated, and leave User Git publication and remote verification pending. |
| 1.48 | 2026-09-15 | FROZEN | APPROVED: reconcile User-verified M00_L01 lesson publication at `83907ab` with `PASS_M00_L01_REMOTE_PUBLICATION_VERIFIED`; record `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, preserve active lesson count `0` and inactive/uncreated M00_L02, and leave the distinct publication-metadata commit, push, and final remote verification pending. |
| 1.49 | 2026-09-16 | FROZEN | APPROVED: consume `PASS_M00_L02_FINAL_DESIGN_LOCK` and record the documentation-only controlled activation of M00_L02 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve M00_L01 frozen publication, keep all technical implementation unauthorized, and leave student documentation pending separate Architect authorization. |
| 1.50 | 2026-09-16 | FROZEN | APPROVED: reconcile M00_L02 documentation authorization and implementation, preserve the initial documentation HOLD and bounded repair, record independent rereview PASS, User final build/regression PASS, final closure review PASS, and `PASS_M00_L02_DOCUMENTATION_RECONCILED_READY_FOR_INDEPENDENT_REVIEW`; retain `IN_PROGRESS / EDITABLE`, active lesson count `1`, pending independent reconciliation review and freeze authorization, no technical changes, and inactive/uncreated M00_L03. |
| 1.51 | 2026-09-16 | FROZEN | APPROVED: consume `PASS_M00_L02_FINAL_FREEZE_AUTHORIZATION` after independent reconciliation review PASS; record M00_L02 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserve the documentation-only technical boundary and M00_L01 frozen publication, leave M00_L03 inactive/uncreated, and retain User Git publication and publication metadata reconciliation as pending. |
| 1.52 | 2026-09-16 | FROZEN | APPROVED: reconcile User-confirmed M00_L02 primary publication at `65a92a4a5806fd5134e0114e851c4e4cc093c58e` and primary push PASS; retain `COMPLETE / FROZEN / READ-ONLY`, record publication metadata reconciliation complete, and leave the separate metadata commit, metadata push, final remote verification, and final publication completion pending. |
| 1.53 | 2026-09-16 | FROZEN | APPROVED: consume `PASS_M00_L03_FINAL_DESIGN_LOCK` and record the prepared M00_L03 candidate as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`; preserve the lesson-local AGENTS script-defect history, M00_L02 final publication, the vendor-neutral Intake Foundation boundary, and all implementation authorizations as separately pending. |
| 1.54 | 2026-09-16 | FROZEN | APPROVED: reconcile M00_L03 production/test authorization, completed bounded Intake implementation, preserved initial focused-test HOLD and minimal test-only repair, focused retest PASS, full clean regression PASS, bounded Simulation PASS, independent implementation review PASS, and authorized bilingual student-documentation implementation; retain M00_L03 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, independent documentation review and all closure/freeze/publication gates pending, and M00_L04 inactive/uncreated. |
| 1.55 | 2026-09-17 | FROZEN | APPROVED: record the M00_L03 transition-guide terminology HOLD and one-line repair, independent documentation rereview PASS, final User closure build PASS, final closure review PASS, and completed documentation/lifecycle reconciliation; retain M00_L03 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, freeze authorization and publication pending, and M00_L04 inactive/uncreated. |
| 1.56 | 2026-09-17 | FROZEN | APPROVED: consume `PASS_M00_L03_ARCHITECT_FREEZE_AUTHORIZATION` after independent reconciliation review PASS; record M00_L03 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, preserve all accepted evidence and protected technical content, keep M00_L04 inactive/uncreated, and leave User-owned publication pending. |
| 1.57 | 2026-09-17 | FROZEN | APPROVED: reconcile accepted M00_L03 primary publication at `3d94dc6e8249135eaa37b71e9fa6e0a9f5cd6af3`, primary push and remote alignment PASS; record publication metadata reconciliation complete while leaving metadata Git publication and final publication verification pending; preserve frozen M00_L03 and inactive/uncreated M00_L04. |
| 1.58 | 2026-09-18 | FROZEN | APPROVED: record M00_L03 final two-commit publication as `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; consume `PASS_M00_L04_FINAL_DESIGN_LOCK` after preparation and architecture/inheritance PASS; activate M00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, locked Right Bumper `whileTrue` command-ownership scope, implementation authorization pending, and M00_L05 inactive/uncreated. |
| 1.59 | 2026-09-18 | FROZEN | APPROVED: reconcile the authorized M00_L04 command/binding implementation, 14/14 focused tests, full clean regression, bounded Simulation and Driver Station evidence, independent implementation review PASS, and paired student-guide creation; retain M00_L04 as the sole `IN_PROGRESS / EDITABLE` lesson pending independent documentation review and later closure/freeze/publication gates, with M00_L05 inactive/uncreated. |
| 1.60 | 2026-09-18 | FROZEN | APPROVED: consume `PASS_M00_L04_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_FINAL_LIFECYCLE_RECONCILIATION_AND_FREEZE_PREPARATION`; preserve both resolved HOLD histories, record M00_L04 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, retain bounded software evidence and `REAL HARDWARE DEFERRED`, keep M00_L05 inactive/uncreated, and leave User-owned Git publication pending. |
| 1.61 | 2026-09-18 | FROZEN | APPROVED: reconcile accepted M00_L04 primary publication at `5c86be3` with primary remote alignment PASS; record publication metadata reconciliation complete in the working tree while leaving the separate metadata commit/push and final publication verification pending; preserve active lesson count `0`, deferred real hardware, and inactive/uncreated M00_L05. |
| 1.62 | 2026-09-18 | FROZEN | APPROVED: record M00_L04 final two-commit publication at primary `5c86be3` and metadata `24738e6`; consume `PASS_M00_L05_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`; activate M00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson with active lesson count `1`, FeederIONoop-only foundation scope, implementation authorization pending, deferred real hardware, and M00_L06 inactive/uncreated. |
| 1.63 | 2026-09-19 | FROZEN | APPROVED: reconcile the authorized M00_L05 Feeder implementation, both bounded test-defect repairs, focused-test PASS, 682-test full clean regression PASS, bounded Simulation and HALSIM Driver Station evidence, independent implementation review PASS, and paired student-guide implementation; retain M00_L05 as the sole `IN_PROGRESS / EDITABLE` lesson pending independent documentation review and later closure/freeze/publication gates, with M00_L06 inactive/uncreated. |
| 1.64 | 2026-09-19 | FROZEN | APPROVED: consume `PASS_M00_L05_FINAL_CLOSURE_REVIEW_ACCEPTED_READY_FOR_LIFECYCLE_RECONCILIATION_AND_FREEZE`; preserve the resolved documentation HOLD and repair history, independent documentation rereview PASS, final closure build PASS, and final closure review PASS; record M00_L05 as `COMPLETE / FROZEN / READ-ONLY` with active lesson count `0`, keep M00_L06 inactive/uncreated, and leave all User-owned publication stages pending. |
| 1.65 | 2026-09-19 | FROZEN | APPROVED: reconcile User-owned M00_L05 primary publication at `5709f1d74b3318303bcc56779315b243dd81770b` with subject `Complete M00_L05 feeder foundation`, push and primary remote alignment PASS; record publication metadata reconciliation complete while leaving metadata publication and final publication verification pending; preserve active lesson count `0` and inactive/uncreated M00_L06. |
| 1.66 | 2026-09-19 | FROZEN | APPROVED: record final M00_L05 metadata publication at `1d6fadeec57fbfd3be245746b21d06e58b79518f` and `PASS_M00_L05_FINAL_PUBLICATION_COMPLETE`; consume `PASS_M00_L06_FINAL_DESIGN_LOCK_READY_FOR_CONTROLLED_ACTIVATION`; activate M00_L06 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, locked `RunFeederCommand` plus Left Bumper `whileTrue` scope, implementation pending separate authorization, deferred real hardware, and protected M00_L14-L16 scope. |
| 1.67 | 2026-09-20 | FROZEN | APPROVED: consume `PASS_M00_L06_INDEPENDENT_CLOSURE_REREVIEW_ACCEPTED` and `AUTHORIZED_FOR_FREEZE` after final independent closure rereview `READY_FOR_FREEZE` with no remaining findings; record M00_L06 as `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, no active M00 lesson, publication pending, deferred real hardware, and inactive/uncreated M00_L07. |
| 1.68 | 2026-09-20 | FROZEN | APPROVED: reconcile accepted M00_L06 primary publication at `f102a5e662877f8cb49eb63f2cfd888ac356bea4` with subject `Complete M00_L06 Feeder command ownership`, primary push PASS, and remote alignment PASS; record publication metadata reconciliation complete/prepared for User commit while leaving metadata Git publication and final publication verification pending; preserve frozen M00_L06 and inactive/uncreated M00_L07. |
| 1.69 | 2026-09-20 | FROZEN | APPROVED: consume `PASS_M00_L07_FINAL_DESIGN_LOCK` after accepted M00_L06 final publication, M00_L07 preparation, 306-of-306 byte-identical inheritance audit, and inherited baseline PASS; activate M00_L07 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, implementation pending separate authorization, `FlywheelIONoop`-only future runtime, CAN 50-54 planning-only, deferred real hardware, and inactive/uncreated M00_L08. |
| 1.70 | 2026-09-20 | FROZEN | APPROVED: reconcile accepted M00_L07 governance adjudication, activation rereview, bounded implementation, preserved static-review HOLD and two-stage test-only repair history, final static rereview PASS, User focused-test PASS, clean full-regression PASS, bounded Simulation PASS, and documentation completion; retain M00_L07 as the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson with active lesson count `1`, evidence `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`, Independent Closure Review next, freeze/publication unclaimed, and M00_L08 inactive/uncreated. |
| 1.71 | 2026-09-20 | FROZEN | APPROVED: consume `PASS_M00_L07_INDEPENDENT_CLOSURE_REVIEW` after `CLOSURE_REVIEW_PASS`, `READY_FOR_FREEZE_AUTHORIZATION`, and no remaining findings; record M00_L07 as `COMPLETE / FROZEN / READ-ONLY`, active lesson count `0`, no active M00 lesson, preserved theory/Simulation/deferred-hardware evidence, publication pending/not yet published, and M00_L08 inactive/uncreated. |
| 1.72 | 2026-09-20 | FROZEN | APPROVED: reconcile accepted M00_L07 primary publication at `50e5f440bb0c9d96bdcd57eed533651d8d59ca93` with subject `Complete M00_L07 Flywheel foundation`, primary push PASS, and remote alignment PASS; record publication metadata reconciliation complete/prepared for User commit while leaving the metadata commit/push and final publication verification pending; preserve frozen M00_L07, active lesson count `0`, no active M00 lesson, and inactive/uncreated M00_L08. |
| 1.73 | 2026-09-23 | FROZEN | Record documentation-only M00_L13 Controlled Activation after accepted Architecture / Inheritance Audit and Final Design Lock; preserve M00_L12 publication and freeze, make M00_L13 the sole active lesson, keep implementation unauthorized, and leave M00_L14 inactive/uncreated. |
| 1.74 | 2026-09-24 | FROZEN | Reconcile accepted M00_L13 implementation, repaired test guards, final static review, User focused and clean-regression PASS, and bounded Simulation PASS; retain M00_L13 as the sole IN_PROGRESS lesson with THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, Independent Closure Review next, freeze/publication pending, and M00_L14 inactive/uncreated. |
| 1.75 | 2026-09-24 | FROZEN | Reconcile accepted PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW and complete Freeze Reconciliation; preserve the stale-status Closure Review HOLD and documentation repair history; record M00_L13 as COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED, Active Lesson Count 0, no current active M00 lesson, Independent Freeze Review pending, publication User-owned/pending, and M00_L14 inactive/uncreated. |
| 1.76 | 2026-09-24 | FROZEN | Reconcile the accepted M00_L13 Independent Freeze Review and User-owned primary frozen snapshot commit `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704`; preserve both safe publication-script HOLDs as script defects; prepare metadata publication for User commit while leaving its SHA, push, and final publication verification pending; keep M00_L13 frozen, Active Lesson Count 0, no active M00 lesson, and M00_L14 inactive/uncreated. |
| 1.77 | 2026-09-25 | FROZEN | Reconcile the bounded M00_L13 final-publication metadata documentation repair after `HOLD_M00_L13_FINAL_PUBLICATION_VERIFICATION_STALE_CURRENT_PUBLICATION_STATE`; record COMPLETE / FROZEN / READ-ONLY / PUBLISHED with external final verification pending, keep the metadata SHA external and the two-commit model unchanged, and preserve active lesson count 0 and inactive/uncreated M00_L14. |
| 1.78 | 2026-09-25 | FROZEN | Record documentation-only M00_L14 Controlled Activation after accepted untouched-copy baseline, Architecture / Inheritance Audit, and Final Design Lock; preserve verified frozen M00_L13, make M00_L14 the sole ACTIVE / IN_PROGRESS lesson with active count 1, encode ShootCommand coordination and its exception-safe cleanup contract, and leave implementation authorization pending with M00_L15/L16 protected. |
| 1.79 | 2026-09-25 | FROZEN | Reconcile accepted M00_L14 implementation, final independent static review, User focused tests, clean regression, and bounded Simulation; preserve the five test-only architecture-guard HOLD/repair cycles without attributing a production defect, retain ACTIVE / IN_PROGRESS with THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED, and set Independent Closure Review as next gate while freeze/publication remain pending. |
| 1.80 | 2026-09-26 | FROZEN | Reconcile `PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW` after the documentation-only Closure Review HOLD and bounded repair; record M00_L14 as COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED, Active Lesson Count 0, Current Active M00 Lesson NONE, unchanged theory/Simulation/deferred-hardware evidence, Independent Freeze Review pending, and M00_L15/L16 inactive. |
| 1.81 | 2026-09-26 | FROZEN | Record `PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW` and User-owned primary frozen snapshot commit `fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`; prepare bounded metadata publication for User Commit 2 without claiming it exists, keep its SHA external, Final Publication Verification pending, Active Lesson Count 0, and M00_L15/L16 inactive. |
| 1.82 | 2026-09-26 | FROZEN | Resolve `HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA` with the required documentation delta; prepare the M00_L13 Commit-2 publication state `COMPLETE / FROZEN / READ-ONLY / PUBLISHED`, keep the metadata commit identity external, Final Publication Verification pending, Active Lesson Count 0, and M00_L15/L16 inactive. |
| 1.83 | 2026-09-26 | FROZEN | Record documentation-only Controlled Activation of prepared M00_L15 after accepted baseline, inheritance audit, and Final Design Lock; preserve verified published M00_L14, set M00_L15 as sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson, record the scheduler tests #19-22, keep implementation not started and unauthorized pending Independent Activation Review, and retain M00_L16 inactive/uncreated. |
| 1.84 | 2026-09-27 | FROZEN | Reconcile M00_L15 after accepted independent closure rereview; record `COMPLETE / FROZEN / READ-ONLY`, Active Lesson Count 0 and no active M00 lesson, preserve `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`, and leave independent freeze review, publication, and final publication verification pending with M00_L16 future/inactive/uncreated. |
| 1.85 | 2026-09-27 | FROZEN | Reconcile M00_L15 publication metadata after accepted independent freeze rereview and primary snapshot commit; record the accepted primary gate and SHA, mark metadata reconciliation complete, and leave Metadata Commit 2, push, and external final publication verification pending under the two-commit Historical Snapshot model. Keep M00_L15 COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED, Active Lesson Count 0, no active M00 lesson, unchanged evidence, and M00_L16 future/inactive/uncreated. |
| 1.86 | 2026-09-27 | FROZEN | Record accepted M00_L15 final publication verification and metadata SHA, then perform documentation-only M00_L16 Controlled Activation after the accepted baseline, inheritance audit, and Final Design Lock. Set M00_L16 as the sole IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK lesson, active count 1, and leave independent activation review and implementation authorization pending. |
| 1.87 | 2026-09-27 | FROZEN | Reconcile accepted M00_L16 implementation, bounded architecture-guard repair and static rereview, User focused tests, clean regression, and bounded Simulation. Record documentation reconciliation complete with THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED; keep M00_L16 sole IN_PROGRESS lesson, independent closure, freeze, and publication pending, and no M00_L17. |
---

### M00_L08 Controlled Activation — 2026-09-20

The accepted M00_L08 preparation baseline, Architecture / Inheritance Audit,
and Final Design Lock are recorded as PASS. The User-prepared candidate was
copied from canonical M00_L07, generated artifacts were removed, and the
untouched-copy baseline passed. Inheritance remains 103/103 production and
96/96 test files byte-identical, with the accepted overall non-generated
comparison of 711/711 identical.

M00_L08 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson. Active lesson count is `1`, and the current active M00 lesson is
`M00_L08`. Its one new concept is vendor-neutral Flywheel closed-loop velocity
control through one validated semantic RPM request while preserving
measurement-only Observation and explicit safe stop.

The locked semantic request is `void requestVelocity(double targetRpm)` in
finite, nonnegative Flywheel mechanism RPM. Exactly `0.0` is the canonical
safe-stop request. Invalid values fail closed; `requestSpin()` is
`REMOVED / SUPERSEDED`; requested states are `STOPPED` and
`VELOCITY_REQUESTED`. Observation remains measurement-only. Runtime remains
`FlywheelIONoop` only, with no physical adapter, hardware configuration, gain,
target RPM, or CAN assignment authorized.

The Architect Simulation clarification is preserved: test doubles and focused
unit tests are not WPILib runtime Simulation evidence. Future bounded runtime
Simulation may claim only deterministic Noop composition, telemetry and
measurement state, STOPPED idle behavior, no automatic Teleop request, and
Disabled → Teleop → Disabled persistence. Physical regulation, convergence,
tuning, sensor fidelity, CAN, RPM accuracy, and physical stop behavior remain
unverified; real hardware remains deferred.

Implementation is `PENDING SEPARATE AUTHORIZATION`; verification is pending.
Only the four locked Flywheel production files and six named inherited
Flywheel-focused tests may later be modified. M00_L07 remains
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. M00_L09 remains
`INACTIVE / NOT CREATED`. This activation changed lifecycle and documentation
only and performed no production or test implementation.

### M00_L08 Controlled Activation Documentation Repair — 2026-09-20

The Independent Activation Review HOLD was limited to documentation
completeness. The following clarification is binding for the future,
separately authorized implementation; it does not implement production code,
tests, or runtime verification.

The exact future `FlywheelIO` contract is:

- `FlywheelIOInputs` fields: `boolean available`, `boolean connected`,
  `boolean velocityValid`, and `double velocityRpm`;
- `void updateInputs(FlywheelIOInputs inputs)`;
- `void requestVelocity(double targetRpm)`; and
- `void stop()`.

`requestSpin()` is `REMOVED / SUPERSEDED`. No vendor type, vendor control
object, gain parameter, or hardware-configuration parameter is permitted.

For a valid positive request, future behavior is ordered as: validate,
record `requestedVelocityRpm`, set `VELOCITY_REQUESTED`, replace the immutable
Observation, and forward exactly one `flywheelIO.requestVelocity(targetRpm)`.
If forwarding throws, the recorded target, state, and Observation remain,
the exception propagates, no rollback occurs, and `periodic()` does not retry.
For `0.0`, the target is zero, the state is `STOPPED`, the Observation is
replaced, and exactly one `flywheelIO.stop()` is forwarded.

Invalid values are NaN, positive infinity, negative infinity, and negative
finite values. Invalid handling records zero and `STOPPED`, replaces the
Observation, attempts `flywheelIO.stop()`, then throws
`IllegalArgumentException`; no invalid target is forwarded. If stop throws,
the IllegalArgumentException remains primary with the stop failure suppressed,
and zero/STOPPED software intent remains recorded.

Explicit stop sets zero, sets `STOPPED`, replaces the Observation, and calls
`flywheelIO.stop()` unconditionally. If it throws, zero/STOPPED state and the
updated Observation remain and the exception propagates. There is no rollback,
automatic restart, or periodic output reissue.

`FlywheelIONoop.updateInputs()` must set exactly
`available = false`, `connected = false`, `velocityValid = false`, and
`velocityRpm = 0.0`. Its `requestVelocity(double)` and `stop()` are safe
deterministic no-ops with no convergence or physical-velocity model.
`velocityRpm = 0.0` while invalid is not measured physical zero RPM.

`RobotContainer` remains unchanged and its exact runtime composition remains
`new FlywheelSubsystem(new FlywheelIONoop())`. It adds no Flywheel command,
binding, default command, direct `requestVelocity(...)`, direct `stop()`,
autonomous registration, NamedCommands, event markers, Feeder/Flywheel
coordination, physical hardware selection, or Flywheel-specific
`RobotBase.isReal()` branch.

Future test reconciliation is limited to these six existing files:

1. `src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`
2. `src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`
3. `src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`
4. `src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`
5. `src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`
6. `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`

No new test file or unrelated inherited test change is authorized. M00_L08
remains `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, active
lesson count `1`, implementation and verification pending, and M00_L09
inactive/not created.

### M00_L08 Post-Verification Documentation Reconciliation — 2026-09-20

The accepted implementation and User-owned verification evidence are now
reconciled into the lifecycle record. This entry is documentation-only and
does not authorize closure, freeze, publication, or M00_L09 activation.

The final implemented concept is exactly one new concept: vendor-neutral
Flywheel closed-loop velocity control through one validated semantic
mechanism-RPM request while preserving measurement-only Observation and
explicit safe stop. The final API is
`void requestVelocity(double targetRpm)`; `requestSpin()` is
`REMOVED / SUPERSEDED`; requested states are exactly `STOPPED` and
`VELOCITY_REQUESTED`.

The final `FlywheelIOInputs` fields are exactly `boolean available`,
`boolean connected`, `boolean velocityValid`, and `double velocityRpm`. The
exact methods are `void updateInputs(FlywheelIOInputs inputs)`,
`void requestVelocity(double targetRpm)`, and `void stop()`. No vendor types,
gains, vendor control object, or hardware-specific configuration is present.
Valid targets are finite, nonnegative mechanism RPM. A positive finite target
records the target, enters `VELOCITY_REQUESTED`, replaces the immutable
Observation, and forwards exactly one IO request. `+0.0` and `-0.0` are
canonical safe-stop behavior. NaN, positive infinity, negative infinity, and
negative finite values fail closed to zero/`STOPPED`, update Observation,
attempt IO stop, throw `IllegalArgumentException`, and suppress any stop
failure onto that primary exception; no invalid value is forwarded.

Safe-stop ordering is: set `requestedVelocityRpm = 0.0`, set
`requestedState = STOPPED`, update immutable Observation, then unconditionally
call `flywheelIO.stop()`. If IO stop throws, zero/`STOPPED`/Observation remain
recorded, the original exception propagates, and there is no rollback or
automatic restart. `periodic()` only calls `updateInputs()` and rebuilds the
immutable Observation; it does not request velocity, stop automatically,
reissue a target, implement PID, or implement readiness. Observation remains
exactly `available`, `connected`, `velocityValid`, `velocityRpm`, and
`requestedState`; target RPM remains subsystem control intent and is not
Observation or telemetry data.

Runtime remains `FlywheelIONoop` only. It deterministically reports
`available=false`, `connected=false`, `velocityValid=false`, and
`velocityRpm=0.0`; request and stop are deterministic no-ops with no
convergence or hardware model. CAN 50–54 is planning reservation only and
real hardware remains deferred.

Relative to frozen M00_L07, production integrity is `Compared: 103`,
`Byte-identical: 99`, `Changed: 4`, `Missing: 0`, `Added: 0`. The changed
production files are exactly `src/main/java/frc/robot/io/flywheel/FlywheelIO.java`,
`src/main/java/frc/robot/io/flywheel/FlywheelIONoop.java`,
`src/main/java/frc/robot/observation/flywheel/FlywheelObservation.java`, and
`src/main/java/frc/robot/subsystems/FlywheelSubsystem.java`; no production
file was added. Test integrity is `Compared: 96`, `Byte-identical: 90`,
`Changed: 6`, `Missing: 0`, `Added: 0`. The changed tests are exactly
`src/test/java/frc/robot/FlywheelArchitectureBoundaryTest.java`,
`src/test/java/frc/robot/io/flywheel/FlywheelIONoopTest.java`,
`src/test/java/frc/robot/observation/flywheel/FlywheelObservationTest.java`,
`src/test/java/frc/robot/subsystems/FlywheelSubsystemTest.java`,
`src/test/java/frc/robot/telemetry/flywheel/FlywheelTelemetryFacadeTest.java`,
and `src/test/java/frc/robot/RobotContainerFlywheelCompositionTest.java`; no
test file was added. `Constants.java`, `RobotContainer.java`,
`FlywheelTelemetryFacade.java`, `RobotTelemetry.java`, Gradle, vendordeps,
deploy, and the frozen M00_L07 predecessor remain unchanged.

The static-review chronology is preserved: the initial Independent Static
Review was `HOLD` for (1) stale `requestedVelocityRpm` after stop, (2) weak
Noop post-request measurement assertions, (3) missing explicit negative-zero
coverage, and (4) brittle regex comment stripping in the structural test.
The bounded repair is `COMPLETE`; the final Independent Static Re-review is
`PASS`, gate `PASS_M00_L08_FINAL_INDEPENDENT_STATIC_REREVIEW`.

Accepted User focused-test evidence is: six authorized focused test classes,
`BUILD SUCCESSFUL in 16s`, `4 actionable tasks: 3 executed, 1 up-to-date`,
and `FOCUSED TESTS: PASS` (`PASS_M00_L08_USER_FOCUSED_TESTS`). Accepted clean
regression evidence is `BUILD SUCCESSFUL in 26s`, `7 actionable tasks: 7
executed`, and `CLEAN REGRESSION: PASS`
(`PASS_M00_L08_CLEAN_FULL_REGRESSION`).

Accepted bounded WPILib Simulation evidence is:

1. `PASS_M00_L08_SIMULATION_CHECKPOINT_1_DISABLED`: Disabled; Available=false,
   Connected=false, RequestedState=STOPPED, VelocityRpm=0.0,
   VelocityValid=false.
2. `PASS_M00_L08_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`: Teleoperated enabled
   with no driver action; Robot Enabled=Yes; Available=false,
   Connected=false, RequestedState=STOPPED, VelocityRpm=0.0,
   VelocityValid=false.
3. `PASS_M00_L08_SIMULATION_CHECKPOINT_3_DISABLED`: returned Disabled; FMS
   Robot Enabled=No; Available=false, Connected=false,
   RequestedState=STOPPED, VelocityRpm=0.0, VelocityValid=false.

Overall gate is `PASS_M00_L08_BOUNDED_SIMULATION`. `VelocityRpm=0.0` while
`VelocityValid=false` is the canonical invalid-Noop representation, not
measured physical zero RPM. Simulation verifies only Noop composition,
telemetry presence, deterministic unavailable/disconnected/invalid state,
STOPPED idle behavior, no automatic Teleop request, and Disabled → Teleop →
Disabled persistence. It does not verify runtime exercise of
`requestVelocity()`, physical control, convergence, gains, feedforward,
sensor fidelity, RPM accuracy, direction, CAN, or physical stop behavior.
In particular, **requestVelocity runtime exercise was NOT claimed**; focused
tests, not runtime Simulation, verified request semantics.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
and `REAL HARDWARE DEFERRED`. M00_L08 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, active lesson
count `1`, current active M00 lesson `M00_L08`, implementation `COMPLETE`,
Independent Static Re-review `PASS`, focused tests `PASS`, clean regression
`PASS`, and bounded Simulation `PASS`. Independent Closure Review is
`PENDING`; Freeze is `NOT AUTHORIZED`; Publication is `NOT AUTHORIZED`.
M00_L09 Flywheel Ready-at-Speed remains `INACTIVE / NOT CREATED`, and later
command, shooting, feeder coordination, autonomous mechanism, readiness,
convergence, tolerance, dwell, and debounce scope remains protected.

### M00_L08 Controlled Freeze Transition — 2026-09-20

The accepted `PASS_M00_L08_INDEPENDENT_CLOSURE_REVIEW` verdict is
`READY_FOR_FREEZE_AUTHORIZATION`, and the Architect decision is `FREEZE
AUTHORIZED`. The controlled freeze transition changes lifecycle state only;
it does not change production source, tests, configuration, dependencies,
deployment content, verification evidence, or publication state.

M00_L08 is now exactly:

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

The final implemented concept remains exactly one vendor-neutral Flywheel
closed-loop velocity concept through `void requestVelocity(double targetRpm)`;
`requestSpin()` is `REMOVED / SUPERSEDED`; requested states remain
`STOPPED` and `VELOCITY_REQUESTED`; and the exact IO, validation, safe-stop,
Observation, periodic, Noop, telemetry, and RobotContainer boundaries remain
unchanged. Evidence remains exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
and `REAL HARDWARE DEFERRED`.

The accepted integrity and verification evidence remains unchanged:
production `103 / 99 / 4 / 0 / 0`, tests `96 / 90 / 6 / 0 / 0`, final static
re-review PASS, focused tests PASS, clean regression PASS, and bounded
Simulation PASS. Simulation remains Noop/lifecycle evidence only; physical
Flywheel behavior and runtime `requestVelocity` exercise are not claimed.

M00_L07 remains `COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED` under
the accepted primary publication `50e5f440bb0c9d96bdcd57eed533651d8d59ca93`,
metadata publication `62199c3ecd1ac7940e188dbef3d28de784c8da2c`, and final
`PUBLICATION_VERIFIED` evidence. M00_L07 is not modified. M00_L09 remains
inactive and uncreated; no later lesson or mechanism scope is activated.

### M00_L08 Primary Publication and Metadata Reconciliation — 2026-09-20

The accepted gate `PASS_M00_L08_PRIMARY_PUBLICATION` records the User-owned
primary publication of the frozen lesson. M00_L08 remains exactly
`COMPLETE / FROZEN / READ-ONLY`. The primary publication commit is
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` with subject
`Complete M00_L08 Flywheel closed-loop velocity`. Primary push is `PASS`, and
local HEAD and `origin/main` both align to that same SHA; primary remote
alignment is `PASS`.

The publication metadata reconciliation is now the next bounded publication
stage and remains `PENDING METADATA COMMIT`. No metadata commit SHA, metadata
push, or final `PUBLICATION_VERIFIED` verdict is claimed. Final independent
publication verification remains `PENDING / NOT YET PERFORMED`. Active lesson
count remains `0`, the current active M00 lesson remains `NONE`, and M00_L09
remains `INACTIVE / NOT CREATED`.

This record follows the resolved Historical Snapshot Model: the frozen
lesson-local snapshot is preserved unchanged, including historical pending
publication wording; no third publication commit is required merely to record
later independent verification. Evidence remains exactly `THEORY VERIFIED`,
`SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`. The bounded Simulation did
not claim runtime `requestVelocity` exercise or physical Flywheel behavior.

### M00_L09 Controlled Activation — 2026-09-21

The canonical M00_L08 predecessor is consumed as
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. The accepted
preparation baseline is `PASS_M00_L09_PREPARATION_BASELINE` with
`BUILD SUCCESSFUL in 34s` and six actionable tasks, all six executed. The
accepted architecture and design gates are
`PASS_M00_L09_ARCHITECTURE_INHERITANCE_AUDIT` and
`PASS_M00_L09_FINAL_DESIGN_LOCK`, verdict `READY_FOR_CONTROLLED_ACTIVATION`.

Controlled Activation is complete for exactly one new concept: vendor-neutral
instantaneous Flywheel Ready-at-Speed classification. `FlywheelSubsystem`
owns one private deterministic side-effect-free helper; the immutable
Observation adds exactly `readyAtSpeed`; telemetry adds exactly `ReadyAtSpeed`;
IO, Noop, RobotTelemetry, RobotContainer, commands, coordination, autonomous,
and hardware remain outside scope. Readiness is true only for positive finite
velocity intent, valid connected measurement, finite measurement, and
inclusive symmetric error within
`Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm`, exactly `50.0`
mechanism RPM. This is a provisional software-policy acceptance tolerance,
not hardware-tuned and not real-robot validated. Dwell, debounce, hysteresis,
history, state-machine policy, and automatic action are excluded.

M00_L09 is now the sole `IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN
LOCK` lesson; Active Lesson Count is `1`, Current Active M00 Lesson is
`M00_L09`, implementation is `NOT STARTED`, and Independent Activation Review
is `PENDING`. M00_L10 is `INACTIVE / NOT CREATED`. No production or test
source was changed, no build/test/Simulation/closure/freeze/publication/Git
evidence is claimed, and M00_L08 remains untouched.

### M00_L09 Activation Documentation Repair — 2026-09-21

The accepted Architect gate is `PASS_M00_L09_CONTROLLED_ACTIVATION`. The
bounded Controlled Activation engineer verdict is
`CONTROLLED_ACTIVATION_COMPLETE_READY_FOR_INDEPENDENT_ACTIVATION_REVIEW`.
These are distinct and both are recorded. The current review remains
`HOLD_M00_L09_INDEPENDENT_ACTIVATION_REVIEW` pending independent re-review.

The canonical external M00_L08 predecessor state is
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`. Its primary
publication is `5daecd970ff95fb906d6de9d5bc22a5cb094877d` (subject
`Complete M00_L08 Flywheel closed-loop velocity`); its metadata publication is
`a76dc33c2b485b4988e7058cbfed0fa3362cc560`, whose parent is the primary; the
final remote-aligned published HEAD is the metadata SHA and the independent
verdict is `PUBLICATION_VERIFIED`. No third publication commit was required.
The two-commit Historical Snapshot Model is PASS. Frozen M00_L08 local records
may retain historical pending-publication wording; that wording is not the
canonical external lifecycle and the frozen lesson was not edited.

The M00_L09 Design Lock is preserved exactly. The readiness truth rule remains
the positive finite requested target, available/connected/valid finite
measurement, and inclusive `Math.abs(measured - target) <=
Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm` comparison with the
provisional `50.0` RPM policy tolerance. A measured velocity of `0.0` RPM does
not, by itself, establish Ready-at-Speed; this is clarification only and is not
an additional formula condition. Ready-at-Speed remains observation-only and
cannot feed a game piece, run Feeder, fire, stage, stop Flywheel, schedule a
command, advance another subsystem, or trigger autonomous behavior.

The future telemetry contract is exactly `Available`, `Connected`,
`VelocityValid`, `VelocityRpm`, `RequestedState`, and `ReadyAtSpeed`; only
`ReadyAtSpeed` is new, sourced from `FlywheelObservation.readyAtSpeed()`.
`TargetRpm`, `VelocityErrorRpm`, `ToleranceRpm`, `Dwell`, `Debounce`, and
`ReadinessDuration` are excluded, and `RobotTelemetry.java` remains unchanged
and read-only. A positive request forwarding exception preserves the new
target/state/derived Observation, propagates the original IO exception
unchanged, and performs no rollback, retry, or readiness-specific masking.
Ready-at-Speed classifies cached requested intent plus cached measurement; it
does not certify successful delivery of the newest IO request.

The eventual evidence plan is `THEORY VERIFIED`, `SIMULATION VERIFIED`, and
`REAL HARDWARE DEFERRED`; current M00_L09 verification remains PENDING. The
full hardware boundary remains unknown/deferred (motor/controller, vendor, CAN
ID/bus, count/topology, sensor, ratio, inversion/direction, current/voltage
limits, neutral/ramping, physical target/max RPM, hardware tolerance, PID/PIDF,
feedforward, convergence, and real-hardware readiness). CAN 50–54 is planning
reservation only and 50.0 RPM is provisional software policy, not a physical
threshold. Production/test boundaries and the M00_L01–M00_L16 roadmap remain
unchanged; M00_L10 is inactive/not created.

### M00_L09 Implementation, Verification, and Documentation Reconciliation — 2026-09-21

The canonical predecessor M00_L08 remains `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED` with primary publication
`5daecd970ff95fb906d6de9d5bc22a5cb094877d` and metadata publication
`a76dc33c2b485b4988e7058cbfed0fa3362cc560`. This reconciliation did not edit
M00_L08.

M00_L09 implemented exactly one locked concept: instantaneous, immutable,
vendor-neutral Flywheel Ready-at-Speed classification. The positive finite
requested target, available/connected/valid finite measurement, and inclusive
`Math.abs(measured - requested) <=
Constants.FlywheelConstants.kReadyAtSpeedToleranceRpm` rule remain the exact
predicate; the 50 RPM tolerance remains provisional software policy only.
The implementation gate `PASS_M00_L09_IMPLEMENTATION_REPORT_ACCEPTED` was
accepted. The initial Independent Static Review `HOLD` is preserved as
history; the bounded repair passed and the final independent static re-review
passed.

The implementation integrity record is production `Compared=103,
ByteIdentical=99, Changed=4, Missing=0, Added=0`, with changes limited to
`Constants.java`, `FlywheelObservation.java`, `FlywheelSubsystem.java`, and
`FlywheelTelemetryFacade.java`. Tests are `Compared=96, ByteIdentical=92,
Changed=4, Missing=0, Added=0`, with changes limited to the four authorized
Ready-at-Speed test files. Deployment/configuration is `Compared=4,
ByteIdentical=4, Changed=0, Missing=0, Added=0`; no new files or unrelated
changes were introduced. IO, Noop, RobotTelemetry, RobotContainer, commands,
autonomous, configuration, and frozen lessons remain protected.

User evidence accepted the focused gate
`PASS_M00_L09_USER_FOCUSED_TESTS` (`BUILD SUCCESSFUL in 22s`; four actionable
tasks, three executed and one up-to-date) and the clean gate
`PASS_M00_L09_CLEAN_FULL_REGRESSION` (`BUILD SUCCESSFUL`; seven actionable
tasks, all seven executed). User Simulation evidence accepted
`PASS_M00_L09_SIMULATION_CHECKPOINT_1_DISABLED`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_2_TELEOP_IDLE`,
`PASS_M00_L09_SIMULATION_CHECKPOINT_3_DISABLED`, and
`PASS_M00_L09_BOUNDED_SIMULATION`. The three checkpoints retained
`Available=false`, `Connected=false`, `ReadyAtSpeed=false`,
`RequestedState=STOPPED`, `VelocityRpm=0.0`, and `VelocityValid=false`.
Simulation is bounded to Noop composition, read-only telemetry, fail-safe
idle, no automatic request, no readiness-triggered actuation, and
Disabled→Teleop→Disabled persistence. It does not claim runtime 50-RPM
boundaries, physical convergence, sensor fidelity, CAN behavior, or physical
hardware.

Evidence classification is exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`,
`REAL HARDWARE DEFERRED`. Hardware readiness, CAN 50–54, topology, sensor,
gearing, limits, tuning, convergence, and physical thresholds remain unknown
or deferred; CAN 50–54 is planning only. M00_L09 remains
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`; implementation,
static review, focused tests, clean regression, bounded Simulation, and
Documentation Reconciliation are complete. Independent Closure Review remains
pending, Freeze is not authorized, and Publication is not authorized. Active
Lesson Count is `1`, current active M00 lesson is M00_L09, and M00_L10 is
inactive/not created.

### M00_L09 Controlled Freeze Transition — 2026-09-21

The final independent closure re-review passed with
`PASS_M00_L09_FINAL_INDEPENDENT_CLOSURE_REVIEW`, and Architect freeze
authorization is accepted. M00_L09 is now `COMPLETE / FROZEN / READ-ONLY`.
The technical contract remains unchanged: one vendor-neutral instantaneous
Ready-at-Speed concept, the immutable six-field Flywheel Observation, the
inclusive symmetric 50.0 RPM provisional software-policy tolerance, unchanged
IO/Noop/RobotTelemetry/RobotContainer boundaries, and no automatic action.

Production integrity remains `103 / 99 / 4 / 0 / 0`; test integrity remains
`96 / 92 / 4 / 0 / 0`; full deploy/config/support remains
`24 / 24 / 0 / 0 / 0`. Focused evidence remains one invocation selecting all
six classes with `BUILD SUCCESSFUL in 22s`, four actionable tasks, three
executed and one up-to-date. Clean regression, all three Simulation
checkpoints, bounded Simulation, and the exact evidence classification remain
accepted. Real hardware remains deferred and CAN 50–54 remains planning only.

Independent Freeze Review is `PENDING`. Publication is `PENDING / NOT YET
PUBLISHED`; no publication SHA or Git event is claimed. Active Lesson Count is
`0`, Current Active M00 Lesson is `NONE`, and M00_L10 remains
`INACTIVE / NOT CREATED`. The freeze chronology stops before Independent Freeze
Review, publication, and successor activation.

### M00_L09 Primary Publication and Metadata Reconciliation — 2026-09-21

The accepted Independent Freeze Review gate is
`PASS_M00_L09_INDEPENDENT_FREEZE_REVIEW`. M00_L09 remains exactly
`COMPLETE / FROZEN / READ-ONLY`; its frozen lesson-local tree is unchanged.
The accepted primary snapshot gate is
`PASS_M00_L09_PRIMARY_FROZEN_SNAPSHOT_COMMIT`.

Primary publication identity:

- SHA: `3c822a1e3956850c9d0ba9954c5b163d83b801b9`
- Subject: `Complete M00_L09 Flywheel ready-at-speed`

The primary frozen snapshot commit exists locally. It has not been pushed, so
no primary remote alignment, origin state, or remote publication verification
is claimed. The canonical publication phase is `PENDING METADATA COMMIT`.
No metadata commit SHA is available, no metadata push has occurred, and no
`PUBLICATION_VERIFIED` verdict is claimed.

This follows the established two-commit Historical Snapshot model: frozen
lesson snapshot primary commit, repository-level metadata commit, and later
external final publication verification. No third commit is required merely to
record final verification. Frozen lesson-local pending wording remains
historical and was not rewritten.

Evidence remains exactly `THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL
HARDWARE DEFERRED`. Production integrity is `103 / 99 / 4 / 0 / 0`, test
integrity is `96 / 92 / 4 / 0 / 0`, and full deploy/config/support integrity is
`24 / 24 / 0 / 0 / 0`. Active Lesson Count remains `0`, the current active M00
lesson remains `NONE`, and M00_L10 remains `INACTIVE / NOT CREATED`. The
M00_L01–M00_L16 roadmap is unchanged.

### M00_L10 Controlled Activation — 2026-09-21

The accepted M00_L09 predecessor is `COMPLETE / FROZEN / READ-ONLY /
PUBLISHED / VERIFIED`, with primary SHA
`3c822a1e3956850c9d0ba9954c5b163d83b801b9`, metadata SHA
`249100db23262430ce2557eaa5e67d70b7b0a79c`, and final verdict
`PUBLICATION_VERIFIED`. M00_L10 is now the sole active lesson:
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK`, Active Lesson Count
`1`, Current Active M00 Lesson `M00_L10`, implementation `NOT STARTED`, and
Independent Activation Review `PENDING`. Freeze and Publication are not
authorized; M00_L11 is inactive/not created.

The accepted M00_L10 preparation baseline, architecture/inheritance audit, and
final design lock recorded zero non-generated source, test, lesson-document,
deploy/configuration, or support drift from M00_L09. This is a documentation-
only activation. No source, test, build, Simulation, Git, frozen predecessor,
or roadmap change is claimed. The exact position/reference contract and
future bounded file lists are recorded in the candidate transition guide
`real_robot_programming/module_M00/M00_L10_ElevatorFoundationAndPositionReferenceSemantics/docs/M00_L09_to_M00_L10_Step_by_Step.md`.

### M00_L10 Primary Publication and Metadata Reconciliation — 2026-09-22

The accepted Independent Freeze Review gate is
`PASS_M00_L10_INDEPENDENT_FREEZE_REVIEW`. M00_L10 remains exactly
`COMPLETE / FROZEN / READ-ONLY`; its frozen lesson-local tree is unchanged.
The accepted primary snapshot gate is
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
final verification. The frozen M00_L10 lesson-local records remain unchanged.

M00_L10 introduces exactly one concept: vendor-neutral Elevator position
observation and reference semantics. Position control remains M00_L11 scope,
homing and trusted-reference establishment remain M00_L12 scope, and
travel-limit enforcement remains M00_L13 scope. Evidence remains exactly
`THEORY VERIFIED`, `SIMULATION VERIFIED`, and `REAL HARDWARE DEFERRED`.
Active Lesson Count remains `0`, Current Active M00 Lesson remains `NONE`,
M00_L11 remains `INACTIVE / NOT CREATED`, and the locked M00_L01–M00_L16
roadmap remains unchanged with no M00_L17.

### M00_L11 Controlled Activation — 2026-09-22

The accepted predecessor is M00_L10 in canonical state
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`, with primary SHA
`531bceabddf53f194b1edaabbd972ee6865e9ff0`, metadata SHA
`cb8c125da0bbc7e2bf0aeebe40163c1a72909445`, and final gate
`PASS_M00_L10_FINAL_PUBLICATION_VERIFICATION`. M00_L10 remains untouched.

The accepted M00_L11 gates are `PASS_M00_L11_PREPARATION_BASELINE`,
`PASS_M00_L11_ARCHITECTURE_INHERITANCE_AUDIT`,
`READY_FOR_M00_L11_FINAL_DESIGN_LOCK`, and
`PASS_M00_L11_FINAL_DESIGN_LOCK`. Inheritance is exact: production
`108 / 108 / 0 / 0 / 0`, tests `102 / 102 / 0 / 0 / 0`, deploy/config/support
`24 / 24 / 0 / 0 / 0`, and lesson-local documentation `98 / 98 / 0 / 0 / 0`
for Compared / Byte-identical / Changed / Missing / Added. Unexpected
substantive drift is `NONE`.

M00_L11 is now the sole active lesson:

```text
STATUS: IN_PROGRESS
ACTIVE STATE: ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L11
IMPLEMENTATION: NOT STARTED
INDEPENDENT ACTIVATION REVIEW: PENDING
M00_L10: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L12: INACTIVE / NOT CREATED
```

M00_L11 introduces exactly one concept: vendor-neutral Elevator closed-loop
position request semantics in the M00_L10 logical position frame. The locked
future IO request is `requestPositionMeters(double)` beside `updateInputs(...)`
and `stop()`. The exact requested states are `STOPPED` and
`POSITION_REQUESTED`. The future observation adds requested state, target
position, and derived position error to the inherited five members. Requests
require finite target, valid position, and trusted reference; finite negative
targets remain valid and no physical range clamp exists. Valid requests record
intent, rebuild the immutable observation, and forward once. Invalid requests
make no mutation or IO call. `periodic()` does not reissue. Stop records
stopped/target `0.0`, rebuilds, and forwards once.

The future write boundary is one requested-state production file and five
existing Elevator production files, with five existing focused Elevator tests
modifiable. RobotContainer, RobotTelemetry, Constants, commands, adapters,
and ElevatorIOSim remain unchanged. M00_L12 owns homing/reference
establishment; M00_L13 owns travel-limit safety and target clamping. Runtime is
Noop-only. Current M00_L11 implementation, build/test, and Simulation evidence
are `NOT YET ESTABLISHED FOR M00_L11`; real hardware is `DEFERRED`. The eventual
closure target is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED`, explicitly planned rather than achieved. The activation record is
`real_robot_programming/module_M00/M00_L11_ElevatorClosedLoopPosition/docs/M00_L10_to_M00_L11_Step_by_Step.md`.
No implementation, test, build, Simulation, closure, freeze, publication, or
Git result is claimed.

### M00_L11 Implementation, Verification, and Documentation Reconciliation — 2026-09-22

The preceding M00_L11 activation record is historical. The current lesson is
`IN_PROGRESS / ACTIVE / IMPLEMENTATION COMPLETE`, with accepted User gates
`PASS_M00_L11_USER_FOCUSED_TESTS`, `PASS_M00_L11_USER_CLEAN_REGRESSION`, and
`PASS_M00_L11_BOUNDED_SIMULATION_VERIFICATION`. Documentation Reconciliation
is complete; Independent Closure Review remains pending; Freeze and Publication
are not authorized.

The first compile failure and the later observation equality failure remain
preserved as test defects with no production causality. The implementation and
test deltas remain bounded to the authorized M00_L11 scope. Simulation evidence
is limited to Noop composition, exact telemetry, safe idle, and
Disabled → Teleop Enabled → Disabled persistence. The classification is exactly
`THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.

M00_L12 retains homing/reference establishment, M00_L13 retains travel-limit
safety and target clamping, and real hardware remains deferred. No closure,
freeze, publication, or Git result is claimed by this reconciliation.

### M00_L11 Controlled Freeze — 2026-09-22

The preceding implementation and reconciliation section is historical. The accepted closure gate is `PASS_M00_L11_INDEPENDENT_CLOSURE_REVIEW` with verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`. The reconciled implementation, focused tests, clean regression, and bounded Simulation evidence remain unchanged.

The authoritative current M00_L11 lifecycle is:

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

The locked one-concept boundary, Frozen Backbone, predecessor integrity, both historical test defects and their bounded repairs, and all verification limits remain preserved. No M00_L12 activation or creation, roadmap movement, publication, remote verification, or hardware verification is authorized by this freeze record. Earlier active-state text in this file remains historical chronology.

### M00_L11 Publication Phase 1 Primary Frozen Snapshot — 2026-09-22

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

The current authoritative M00_L12 lifecycle record follows the accepted Independent Closure Review: `PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW`, verdict `CLOSURE_REVIEW_PASS_READY_FOR_FREEZE`, with remaining legitimate closure findings `NONE`. Earlier activation, implementation, verification, and documentation-reconciliation records remain historical chronology.

Accepted implementation and verification gates remain `PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW`, `PASS_M00_L12_USER_FOCUSED_TESTS`, `PASS_M00_L12_USER_CLEAN_REGRESSION`, and `PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION`. The test-only architecture repair chronology and exact production, test, and deploy/config/support deltas remain recorded in the transition guide. The accepted closure review did not modify files or run Git, Gradle, tests, build, or Simulation.

M00_L12 preserves one concept: a bounded, scheduler-managed Elevator homing lifecycle that requests vendor-neutral reference acquisition and recognizes success only from normalized IO-reported `positionReferenced`. Zero position alone does not establish home. The evidence remains `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`; Simulation is bounded software/runtime and truthful Noop evidence only. Physical homing, motion, sensor activation, calibration, reference accuracy, and hardware convergence are not established.

The transition to `COMPLETE / FROZEN / READ-ONLY` is documentation/lifecycle reconciliation only. Publication has not occurred; no M00_L12 publication SHA or remote verification is recorded. M00_L13 remains inactive and not created. The canonical chronology is `real_robot_programming/module_M00/M00_L12_ElevatorHoming/docs/M00_L11_to_M00_L12_Step_by_Step.md`.

```text
ACTIVE LESSON COUNT: 0
CURRENT ACTIVE M00 LESSON: NONE
M00_L12: COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED
IMPLEMENTATION: COMPLETE
STATIC REVIEW: PASS_M00_L12_FINAL_INDEPENDENT_STATIC_REREVIEW
FOCUSED TESTS: PASS_M00_L12_USER_FOCUSED_TESTS
CLEAN REGRESSION: PASS_M00_L12_USER_CLEAN_REGRESSION
SIMULATION: PASS_M00_L12_BOUNDED_SIMULATION_VERIFICATION / BOUNDED SOFTWARE-NOOP EVIDENCE
EVIDENCE: THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED
INDEPENDENT CLOSURE REVIEW: PASS_M00_L12_INDEPENDENT_CLOSURE_REVIEW / CLOSURE_REVIEW_PASS_READY_FOR_FREEZE
FREEZE RECONCILIATION: COMPLETE
INDEPENDENT FREEZE REVIEW: PENDING / NEXT GATE
PUBLICATION: NOT PUBLISHED / PENDING / USER-OWNED
PUBLICATION SHA: NONE / NOT YET ESTABLISHED
REAL HARDWARE: DEFERRED
M00_L13: INACTIVE / NOT CREATED
```

Earlier M00_L12 active-state text in this file remains historical chronology. The lesson remains in its normal repository location; no filesystem permissions were changed.

### M00_L12 Metadata Publication Reconciliation — 2026-09-23

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

The following block records activation-point state only. Its implementation and verification pending statements describe that historical date and are superseded by the current reconciliation below.

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

## Historical M00_L13 Freeze and Metadata Publication Records — through 2026-09-24

Accepted gates include PASS_M00_L13_IMPLEMENTATION_AUTHORIZATION; PASS_M00_L13_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW; PASS_M00_L13_FINAL_INDEPENDENT_STATIC_REREVIEW; PASS_M00_L13_USER_FOCUSED_TESTS; PASS_M00_L13_USER_CLEAN_REGRESSION; PASS_M00_L13_BOUNDED_SIMULATION_VERIFICATION; PASS_M00_L13_DOCUMENTATION_RECONCILIATION; PASS_M00_L13_CLOSURE_DOCUMENTATION_REPAIR; and PASS_M00_L13_INDEPENDENT_CLOSURE_REREVIEW with verdict CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE. The earlier Closure Review HOLD_M00_L13_CLOSURE_REVIEW_STALE_AGENTS_IMPLEMENTATION_STATUS was limited to the stale current authorization sentence and is preserved as resolved history. Freeze Reconciliation is complete; Independent Freeze Review is next.

Production delta versus frozen M00_L12 is common 110 / identical 109 / changed 1 / missing 0 / added 1: ElevatorSubsystem.java changed and ElevatorTravelLimits.java added. Test delta is common 103 / identical 101 / changed 2 / missing 0 / added 1: ElevatorSubsystemTest.java and ElevatorArchitectureBoundaryTest.java changed; ElevatorTravelLimitsTest.java added. The initial static-review HOLD was limited to test/architecture guards; the accepted repair changed only those two test files, no production defect or Design Lock change occurred, and final static re-review passed.

Focused tests passed with BUILD SUCCESSFUL in 42s and 4 actionable tasks executed. Clean regression passed with BUILD SUCCESSFUL in 25s, 5 actionable tasks executed, and GRADLE_EXIT_CODE=0. The earlier PowerShell NativeCommandError caused by the WPILib joystick stderr warning was an evidence-capture issue, not a test failure. Bounded Simulation passed for Disabled -> Teleop Enabled -> Disabled using the unchanged truthful Noop composition; it does not establish configured or physical travel-limit behavior. Evidence is THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

M00_L13 is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED / NOT YET PUBLICATION-VERIFIED. Active Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L12 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14 remains INACTIVE / NOT CREATED; M00_L15/L16 remain future scope. Independent Freeze Review is PENDING. Primary frozen snapshot publication, metadata publication, User push, and final publication verification remain PENDING / USER-OWNED; no M00_L13 publication SHA is established. Physical travel and hardware facts remain UNKNOWN / DEFERRED.

The metadata reconciliation record below was written before the User completed and pushed canonical Commit 2. Its then-pending statements are historical and superseded by the current publication state that follows.

## Historical M00_L13 Metadata Publication Reconciliation — 2026-09-24

Accepted gates: PASS_M00_L13_INDEPENDENT_FREEZE_REVIEW with verdict FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION; PASS_M00_L13_PRIMARY_FROZEN_SNAPSHOT_PUBLICATION_COMMIT; and PASS_M00_L13_METADATA_PUBLICATION_RECONCILIATION. The User-owned primary frozen snapshot commit is complete with SHA `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704` and subject `Complete M00_L13 Elevator travel-limit safety`.

Two earlier primary-publication staging attempts stopped safely: HOLD_M00_L13_PRIMARY_PUBLICATION_SCRIPT_BOUNDARY_MISMATCH and HOLD_M00_L13_PRIMARY_PUBLICATION_GIT_ADD_PATHSPEC_DEFECT. Both were PUBLICATION_SCRIPT_DEFECT findings, with NO_LESSON_DEFECT, NO_SOURCE_DEFECT, NO_TEST_DEFECT, and NO_FREEZE_DEFECT. The corrected User-owned staging flow produced the accepted primary commit. User evidence states that after that commit no M00_L13 authored path remained dirty; only previously identified unrelated working-tree paths remained and are protected.

M00_L13 was COMPLETE / FROZEN / READ-ONLY with its primary snapshot committed. Metadata publication reconciliation was prepared, while the User-owned metadata commit, push, and final verification had not yet occurred. This was the state at that time.

Active Lesson Count was 0; Current Active M00 Lesson was NONE. M00_L14 was INACTIVE / NOT CREATED; M00_L15/L16 remained future scope. Technical contracts, source/test deltas, executable evidence, Simulation evidence, and THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED classification were unchanged. Frozen M00_L12 and generated/runtime classifications remained protected.

## Historical M00_L13 Publication State before M00_L14 Activation — 2026-09-25

This is an earlier publication-state snapshot. Its then-pending final verification statements are historical and are superseded by the accepted external M00_L13 verification recorded in the current M00_L14 activation state below.


M00_L13: COMPLETE / FROZEN / READ-ONLY / PUBLISHED

Primary Frozen Snapshot Commit: COMPLETED; SHA `5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704`; subject `Complete M00_L13 Elevator travel-limit safety`.

Metadata Publication: ESTABLISHED BY COMMIT 2 OF THE TWO-COMMIT HISTORICAL SNAPSHOT MODEL. Its own SHA and matching remote-main identity are established by external publication evidence and are not self-embedded. Final Publication Verification remains PENDING / EXTERNAL.

Active Lesson Count is 0; Current Active M00 Lesson is NONE. M00_L14 is INACTIVE / NOT CREATED; M00_L15/L16 remain future scope. The canonical publication remains exactly two commits; amending the existing metadata commit does not create a third verification-only commit.

The prior independent final publication reviews recorded `HOLD_M00_L13_FINAL_PUBLICATION_VERIFICATION_STALE_CURRENT_PUBLICATION_STATE` and `HOLD_M00_L13_FINAL_PUBLICATION_REREVIEW_STALE_POST_AMEND_CHRONOLOGY`. Both are historical publication metadata/documentation findings. The latter is addressed by this chronology repair. Classification: `PUBLICATION_METADATA_CHRONOLOGY_DEFECT`; `NO_LESSON_DEFECT`; `NO_PRODUCTION_DEFECT`; `NO_TEST_DEFECT`; `NO_ARCHITECTURE_DEFECT`; `NO_SIMULATION_DEFECT`; `NO_FREEZE_DEFECT`; `NO_PUBLICATION_IDENTITY_DEFECT`; `NO_TWO_COMMIT_MODEL_CHANGE`. Technical contracts and verification evidence remain unchanged.


## Historical M00_L14 Controlled Activation Snapshot — 2026-09-25

This records the activation-time state. The accepted post-implementation
reconciliation below supersedes its pending implementation and verification
statements.

Accepted predecessor: M00_L13 is COMPLETE / FROZEN / READ-ONLY / PUBLISHED /
VERIFIED. Primary frozen snapshot SHA: 5e89225fba85f0a6b0dbb5c4a58ee6ac710a1704.
Canonical metadata SHA: 658d1e44c417763df3689b9b52e409161446c593. Evidence:
THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED.

Accepted M00_L14 preparation and design gates:
PASS_M00_L14_UNTOUCHED_COPY_BASELINE_BUILD (6 actionable tasks, 6 executed,
BASELINE_BUILD_EXIT_CODE=0); PASS_M00_L14_ARCHITECTURE_INHERITANCE_AUDIT; and
PASS_M00_L14_FINAL_DESIGN_LOCK. The independent audit found 339/339 authored
files identical to M00_L13 before activation. Controlled Activation is
documentation-only.

Current M00 lifecycle:

M00_L13: COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED
M00_L14: ACTIVE / IN_PROGRESS / NOT COMPLETE / NOT FROZEN / NOT PUBLISHED
ACTIVE LESSON COUNT: 1
CURRENT ACTIVE M00 LESSON: M00_L14 — Shoot Coordination
INDEPENDENT ACTIVATION REVIEW: PENDING
IMPLEMENTATION AUTHORIZATION: NOT AUTHORIZED / PENDING
M00_L15: FUTURE / INACTIVE / NOT CREATED
M00_L16: FUTURE / INACTIVE / NOT CREATED

The sole locked concept is one scheduler-managed
frc.robot.commands.ShootCommand coordinating existing
FlywheelObservation.readyAtSpeed() semantics with Feeder request semantics. It
requires exactly FlywheelSubsystem and FeederSubsystem. Feed admission requires
ready-at-speed and Feeder available and connected. Feeder requestedState() is
software request state only, not physical game-piece transport evidence. No
command-local feedingRequested state is permitted.

The constructor accepts non-null Flywheel and Feeder subsystems and a finite,
strictly positive caller-supplied targetVelocityRpm. The target is semantic
configuration, not an authoritative hardware shooting RPM. Constants,
RobotContainer, IO, Observation, telemetry, vendor adapters, and deploy files
remain unchanged. Expected future production delta is one added
ShootCommand.java; no source, tests, or implementation have been authorized.
Invalid numeric targets throw IllegalArgumentException, and construction
performs no output or subsystem mutation. There is no L14 driver binding; the
inherited Left Bumper manual Feeder command remains, with scheduler requirements
providing Feeder mutual exclusion.

Normal initialization stops Feeder once, then requests Flywheel velocity once.
Normal execute uses Feeder Observation state for transition-only feed/stop
requests and never reissues the Flywheel velocity request. The command is
hold-style (isFinished() == false). Every terminal
end() attempts Feeder stop then Flywheel stop, attempting both even if the
first throws; if both terminal stops throw, Feeder remains primary and the
Flywheel exception is suppressed. If the initial Feeder stop throws during
initialize(), Flywheel velocity is not requested; Flywheel stop is attempted
once and any cleanup failure is suppressed on the original Feeder exception.
If the Flywheel velocity request throws, the completed Feeder baseline stop is
not repeated; Flywheel stop is attempted once and any cleanup failure is
suppressed on the original request exception. If requestFeed() throws during
execute(), Feeder stop and Flywheel stop are both attempted once, including the
Flywheel stop if Feeder cleanup throws; cleanup failures are suppressed on the
original request exception. If Feeder stop throws during readiness/admission
loss in execute(), Feeder stop is not retried in that failing call and Flywheel
stop is attempted once. In every path, the original RuntimeException remains
primary and is rethrown; there is no retry loop, silent recovery, clamp,
rewrite, fallback, or routine java.lang.Error recovery.
No timeout, shot completion, Elevator/Intake coordination, vision aiming, or
autonomous mechanism integration is included.

Evidence at activation was THEORY VERIFICATION IN PROGRESS /
SIMULATION NOT TESTED / REAL HARDWARE DEFERRED. Runtime Flywheel and Feeder
adapters are Noop; no physical shot is claimed. M00_L15 owns Intake-to-Feeder
Coordination; M00_L16 owns Mechanism Autonomous Event Integration.

## Historical M00_L14 Post-Verification Reconciliation — 2026-09-25

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14
is the sole ACTIVE / IN_PROGRESS lesson, NOT COMPLETE / NOT FROZEN / NOT
PUBLISHED; Active Lesson Count is 1. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED. Independent Closure Review is the next gate.

Accepted M00_L14 gates are PASS_M00_L14_IMPLEMENTATION_AUTHORIZATION,
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
ShootCommand binding, so Simulation verified startup, mode transitions,
scheduler stability, safe semantic state, and clean exit; it did not execute
ShootCommand through a RobotContainer binding or demonstrate physical shooting.
The accepted evidence classification is THEORY VERIFIED / SIMULATION VERIFIED
/ REAL HARDWARE DEFERRED.

Five rounds of independent static HOLD and bounded repair addressed test-only
architecture-guard defects. They did not identify a production defect. The
final static review passed. Flywheel and Feeder adapters remain Noop for this
lesson's runtime composition; hardware shooting values and behavior remain
unverified. Documentation reconciliation is current; closure review, freeze,
and publication remain pending and User-owned where applicable.

## Historical M00_L14 Freeze Reconciliation — 2026-09-26

The initial Independent Closure Review returned
`HOLD_M00_L14_INDEPENDENT_CLOSURE_REVIEW_DOCUMENTATION_PROOF_RECONCILIATION_REQUIRED`
for documentation and evidence wording only. No production, architecture,
runtime, test, or Simulation defect was found. The bounded repair passed as
`PASS_M00_L14_DOCUMENTATION_PROOF_RECONCILIATION`. Independent Closure Rereview
passed as `PASS_M00_L14_INDEPENDENT_CLOSURE_REREVIEW` with verdict
`INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION`.
Freeze Reconciliation is complete.

M00_L13 remains COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. M00_L14
is COMPLETE / FROZEN / READ-ONLY / NOT PUBLISHED. Active Lesson Count is 0;
Current Active M00 Lesson is NONE. M00_L15 and M00_L16 remain FUTURE /
INACTIVE / NOT CREATED. The evidence remains THEORY VERIFIED / SIMULATION
VERIFIED / REAL HARDWARE DEFERRED. The accepted focused tests, clean regression,
and bounded Simulation results remain unchanged.

RobotContainer has no ShootCommand binding, and Simulation did not directly
schedule this command or demonstrate physical shooting. Direct unit-test
invocation of `end(true)` covers interrupted-end cleanup semantics; actual
CommandScheduler cancellation integration was not tested. Independent Freeze
Review is PENDING. User-owned publication is NOT PUBLISHED / PENDING, and Final
Publication Verification is PENDING.

## Historical M00_L14 Metadata Publication Preparation — 2026-09-26

The accepted Independent Freeze Review is
`PASS_M00_L14_INDEPENDENT_FREEZE_REVIEW` with verdict
`INDEPENDENT_FREEZE_REVIEW_PASS_READY_FOR_PUBLICATION`. The User completed
`PASS_M00_L14_PRIMARY_FROZEN_SNAPSHOT_COMMIT` at primary SHA
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`. This is Commit 1 of the
two-commit Historical Snapshot model. M00_L14 remains COMPLETE / FROZEN /
READ-ONLY and NOT ACTIVE. Active Lesson Count is 0; Current Active M00 Lesson
is NONE. M00_L15 and M00_L16 remain FUTURE / INACTIVE / NOT CREATED.

This earlier preparation record predates the accepted no-delta HOLD and its
repair below. At that point the metadata Commit 2 target state had not yet been
reconciled. No Git action or new build, test, Simulation, or hardware
verification was claimed by that preparation.

Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE
DEFERRED. RobotContainer has no ShootCommand binding. Bounded Simulation did
not schedule ShootCommand or prove physical shooting. Direct unit invocation
of `end(true)` established interrupted-end cleanup method semantics, not
CommandScheduler cancellation integration. The prior static and Closure HOLD
chronology, repair gates, and Freeze Reconciliation remain preserved in the
lesson records.

## Historical M00_L14 Metadata Publication Reconciliation Repair — pre-Commit-2 state

This is the accepted pre-Commit-2 preparation snapshot. Its pending Commit 2
language records the state at that time and is superseded by the current
post-amend publication record below.

The accepted HOLD `HOLD_M00_L14_METADATA_PUBLICATION_RECONCILIATION_NO_METADATA_DELTA`
identified that the prior preparation produced no post-Commit-1 publication
metadata delta. This bounded repair added the needed documentation delta and
applied the M00_L13 publication semantics. At that historical point, the
intended state recorded by User-owned Commit 2 was `COMPLETE / FROZEN /
READ-ONLY / PUBLISHED`; the worktree still awaited the User's Commit 2 action,
and neither that commit nor its push was claimed as complete.

Primary Frozen Snapshot Commit 1 remains
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`. The Metadata Publication Commit's
own SHA and matching remote identity are external evidence and are not
self-embedded. Final Publication Verification was PENDING / EXTERNAL; no
third verification-only commit was part of the model. M00_L14 was NOT
ACTIVE; Active Lesson Count was 0 and Current Active M00 Lesson was NONE.
M00_L15/L16 remain FUTURE / INACTIVE / NOT CREATED. Evidence remains THEORY
VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED. RobotContainer has no
ShootCommand binding; Simulation did not schedule the command or prove physical
shooting. Direct `end(true)` unit invocation proves cleanup method semantics,
not scheduler-driven cancellation. No production or test change is part of
this metadata repair.

## Historical M00_L14 Post-Amend Publication State — 2026-09-26 (before M00_L15 activation)

Accepted User evidence establishes the two-commit Historical Snapshot chain:
Primary Frozen Snapshot Commit 1 is
`fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`, followed by the completed
Metadata Publication Commit 2. Its amendment is COMPLETED by accepted User
evidence, including `PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION` and
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND`. The canonical identity is
external User Git evidence and is not embedded. Publication remains exactly
two commits.

Accepted chronology includes
`PASS_M00_L14_DOCUMENTATION_RECONCILIATION`,
`PASS_M00_L14_FREEZE_RECONCILIATION`,
`PASS_M00_L14_METADATA_PUBLICATION_RECONCILIATION`,
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT`,
`PASS_M00_L14_METADATA_COMMIT_AMENDMENT_PREPARATION`, and
`PASS_M00_L14_METADATA_PUBLICATION_COMMIT_AMEND`. M00_L14 is COMPLETE / FROZEN /
READ-ONLY / PUBLISHED / NOT ACTIVE; Active Lesson Count is 0 and Current
Active M00 Lesson is NONE. M00_L15 and M00_L16 remain FUTURE / INACTIVE / NOT
CREATED. Evidence remains THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED.

Remote push remains PENDING / USER-OWNED because no push evidence is supplied.
The prior `HOLD_M00_L14_FINAL_PUBLICATION_VERIFICATION_METADATA_COMMIT_STATE_RECONCILIATION_REQUIRED`
was resolved by the accepted User amendment. The subsequent
`HOLD_M00_L14_FINAL_PUBLICATION_REVERIFICATION_POST_AMEND_STATE_RECONCILIATION_REQUIRED`
identified stale current-state chronology and is addressed by this
documentation reconciliation. Final Publication Verification remains PENDING /
EXTERNAL; no final-verification PASS was claimed in this historical state, and
no third verification-only commit was part of the model. Technical scope,
accepted build/test/Simulation evidence, Simulation limitations, and the
direct `end(true)` versus scheduler-cancellation distinction remain unchanged.
This is documentation-only post-amend publication state reconciliation; no
production, test, deploy, or configuration content was changed.

## Historical M00_L15 Controlled Activation — 2026-09-26

Accepted User evidence and independent review gates establish M00_L14 as
COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED. Its Primary Frozen
Snapshot SHA is `fa34556a3f1b7ef52b2c678a39c1083392d7c8d3`; its canonical
Metadata Publication Commit SHA is `1a85c0827ee91ba7f70a595d94c49ad16a6df9cb`.
Accepted final publication and push gates are
`PASS_M00_L14_FINAL_PUBLICATION_VERIFICATION` and
`PASS_M00_L14_PUBLICATION_PUSH`.

M00_L15 preparation gates are `PASS_M00_L15_UNTOUCHED_COPY_BASELINE_BUILD`
(BUILD SUCCESSFUL in 53s; 6 actionable tasks, all executed; exit code 0),
`PASS_M00_L15_ARCHITECTURE_INHERITANCE_AUDIT` (341/341 authored files
identical to M00_L14; no unexpected authored differences), and
`PASS_M00_L15_FINAL_DESIGN_LOCK`. The initial Final Design Lock HOLD,
`HOLD_M00_L15_FINAL_DESIGN_LOCK_SCHEDULER_TEST_COVERAGE_REQUIRED`, was
resolved by the accepted re-review requiring four actual CommandScheduler
tests in addition to the 18 direct-contract cases.

M00_L15 — Intake-to-Feeder Coordination is the sole
`IN_PROGRESS / ACTIVE / EDITABLE WITHIN FINAL DESIGN LOCK` lesson. Active M00
lesson count is 1 and the current active lesson is M00_L15. Its one concept is
scheduler-managed coordination of existing Intake and Feeder semantic
behaviors under one command lifecycle. The locked command is
`frc.robot.commands.IntakeToFeederCommand`, requiring exactly
`IntakeSubsystem` and `FeederSubsystem`. The test plan contains 18 direct
contract cases and four scheduler cases: startup, cancellation, Intake
contention, and Feeder contention. RobotContainer receives no new binding;
M00_L16 retains Mechanism Autonomous Event Integration.

Controlled Activation updates documentation only. Production and test
implementation are NOT STARTED and NOT AUTHORIZED. Independent Activation
Review is PENDING. Freeze and publication are NOT AUTHORIZED. Simulation and
real hardware verification for M00_L15 have not been run; real hardware remains
DEFERRED. M00_L14 and earlier frozen lessons remained read-only; M00_L16
remained FUTURE / INACTIVE / NOT CREATED. No L17 was authorized.

## Historical M00_L15 Implementation and Verification Reconciliation — 2026-09-27 (pre-freeze state)

At that pre-freeze point, this record superseded the preceding Controlled
Activation snapshot. Its accepted lifecycle was IN_PROGRESS / ACTIVE / EDITABLE
WITHIN FINAL DESIGN LOCK. M00_L14 was COMPLETE / FROZEN / READ-ONLY / PUBLISHED
/ VERIFIED; Active M00 Lesson Count was 1. Independent Closure Review was
pending. The current freeze state is recorded in the following section.

Accepted gates include `PASS_M00_L15_UNTOUCHED_COPY_BASELINE_BUILD`,
`PASS_M00_L15_ARCHITECTURE_INHERITANCE_AUDIT`,
`PASS_M00_L15_FINAL_DESIGN_LOCK`,
`PASS_M00_L15_CONTROLLED_ACTIVATION`,
`PASS_M00_L15_INDEPENDENT_ACTIVATION_REREVIEW`,
`PASS_M00_L15_IMPLEMENTATION_AUTHORIZATION`,
`PASS_M00_L15_IMPLEMENTATION_HANDOFF_TO_STATIC_REVIEW`, and
`PASS_M00_L15_FINAL_INDEPENDENT_STATIC_REVIEW`. The one concept remains
scheduler-managed coordination of existing Intake and Feeder semantic
behaviors under one command lifecycle.

The sole production addition relative to M00_L14 is
`src/main/java/frc/robot/commands/IntakeToFeederCommand.java`; 112 shared
production Java files are identical. RobotContainer, RunIntakeCommand,
RunFeederCommand, ShootCommand, IntakeSubsystem, and FeederSubsystem remain
unchanged. The command requires exactly IntakeSubsystem and FeederSubsystem.
RobotContainer retains Right Bumper to RunIntakeCommand and Left Bumper to
RunFeederCommand and has no IntakeToFeederCommand binding.

`PASS_M00_L15_USER_FOCUSED_TESTS` passed all 22 tests with zero failures,
errors, or skips. The initial 830-test clean regression had two failures;
`PASS_M00_L15_CLEAN_REGRESSION_FAILURE_DIAGNOSIS` classified both as
`EXPECTED_INHERITED_TEST_CONTRACT_EVOLUTION`, not production defects.
`PASS_M00_L15_INHERITED_ARCHITECTURE_TEST_RECONCILIATION` reconciled only the
current lesson's inherited test copies. The Intake and Feeder filename guards
use normalized relative paths and closed sets. The Feeder owner guard uses the
JDK Java parser / syntax tree. Architecture scan robustness repair, parser
repair, and independent parser review passed without production changes.

`PASS_M00_L15_USER_CLEAN_REGRESSION` passed: BUILD SUCCESSFUL, 830 tests, 830
PASS, zero failures, errors, or skips, `BUILD_EXIT_CODE=0`.
`PASS_M00_L15_BOUNDED_SIMULATION_DS_VERIFICATION` passed for Simulation
startup and Driver Station attachment. Disabled had Robot Enabled=No and Intake,
Feeder, and Flywheel RequestedState=STOPPED. Teleoperated had Robot Enabled=Yes,
DS Attached=Yes, and Intake and Feeder STOPPED. On return to Disabled, Robot
Enabled=No and Intake and Feeder remained STOPPED, with no unexpected mechanism
state. No fatal runtime or scheduler error occurred; termination was BUILD
SUCCESSFUL with `SIMULATION_EXIT_CODE=0`. The unavailable Joystick Button 6
warning on port 0 was EXPECTED / NON-BLOCKING because the controller was
unassigned or unplugged.
Simulation did not schedule IntakeToFeederCommand because RobotContainer has no
binding; direct scheduler evidence comes from the focused tests.

Evidence is `THEORY VERIFIED / SIMULATION VERIFIED / REAL HARDWARE DEFERRED`.
No physical Intake or Feeder behavior, game-piece transfer, mechanism timing,
sensor behavior, or shooting is claimed. Documentation Reconciliation was
complete at this pre-freeze point, and Independent Closure Review was then the
next gate. This paragraph records the state at that time.

## Historical M00_L15 Freeze Reconciliation — 2026-09-27

The accepted independent closure rereview gate is
`PASS_M00_L15_INDEPENDENT_CLOSURE_REREVIEW`, with verdict
`M00_L15_INDEPENDENT_CLOSURE_REREVIEW_PASS_READY_FOR_FREEZE_RECONCILIATION`.
The rereview reported no findings. Freeze reconciliation is complete, and
M00_L15 is now `COMPLETE / FROZEN / READ-ONLY`.

M00_L15 evidence remains exactly `THEORY VERIFIED / SIMULATION VERIFIED / REAL
HARDWARE DEFERRED`. Focused tests remain 22 / 22 PASS; the final clean
regression remains 830 / 830 PASS with zero failures, errors, or skips and
`BUILD_EXIT_CODE=0`; bounded Simulation / Driver Station remains PASS with
`SIMULATION_EXIT_CODE=0`. Simulation did not schedule
`IntakeToFeederCommand`; its scheduler behavior is established by focused
tests. The sole production addition remains
`IntakeToFeederCommand.java`, with 112 shared production Java files identical
to M00_L14. No production or test file changed during freeze reconciliation.

At this freeze-reconciliation point, M00_L14 remained
`COMPLETE / FROZEN / READ-ONLY / PUBLISHED / VERIFIED`; M00_L15 publication,
final publication verification, and Independent Freeze Review were pending.
That state is superseded by the current metadata reconciliation below. Active
Lesson Count was `0`, Current Active M00 Lesson was `NONE`, and M00_L16 was
`FUTURE / INACTIVE / NOT CREATED`. No M00_L17 is authorized.

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
