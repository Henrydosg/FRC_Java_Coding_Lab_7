# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`
- Title: `A01_L08 - Autonomous Routine Selection and Safe Composition`
- Previous Lesson: `A01_L07_AutoBuilderContractIntegration`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `RE-FROZEN 2026-08-26 - original closure and reopen history preserved`
- Lesson Goal: select and safely compose autonomous routines with explicit
  cancellation, requirements, and failure behavior.

## Governance and Inheritance Gates

- Reopen Governance: `PASS / APPROVED` - Architect and User approved the
  documentation-only exceptional safety/robustness reopen under
  `ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`.
- Preparation/Readiness Repair Implementation: `AUTHORIZED / IMPLEMENTED / LOCAL REVIEW GATES PASS`.
- Terminal-Ownership Scope Amendment: `PASS / APPROVED`.
- Terminal-Ownership Repair Implementation Authorization: `YES / IMPLEMENTED / ARCHITECTURE HOLD`.
- Single Editable Lesson During Reopen: `PASS / HISTORICAL` - A01_L08 was the
  sole editable lesson; V00_L02 remained SUSPENDED / READ-ONLY.
- Original Governance: `PASS / HISTORICAL` - required governance, A01 ADR,
  authoritative English Documents A/B/C, frozen L01-L07, and inherited L08
  were reviewed for the original lesson.
- Original Architecture Review: `PASS / HISTORICAL` - chooser ownership, one-time selection
  snapshot, fresh command construction, readiness, alliance, requirement, and
  centralized-stop contracts remain preserved.
- Source Lesson: `PASS` - L07 is COMPLETE / FROZEN / READ-ONLY.
- Directory Identity: `PASS` - `A01_L08_AutonomousRoutineSelectionAndSafeComposition`.
- User-Owned Inheritance: `PASS` - user copied and renamed frozen L07.
- Settings Encoding Repair: `PASS / USER-REPORTED` - the identity rewrite
  encoding problem was repaired; current settings.gradle is valid UTF-8 without
  a BOM.
- WPILib Metadata: `PASS` - `.wpilib/wpilib_preferences.json` is preserved with
  team 10951, project year 2026, and Java language.
- Inherited Project Structure: `PASS` - `.vscode`, `src`, `vendordeps`,
  `gradle`, build files, wrappers, documentation, tests, and PathPlanner assets
  are present.
- Generated Artifacts: `PASS / USER-OWNED` - generated artifacts were removed
  from L08 during inheritance; the accepted baseline may recreate build outputs.
- Baseline Build: `PASS / USER-CONFIRMED` - compileJava, compileTestJava, tests,
  and clean build. Codex does not claim to have run this baseline.
- Build: `PASS / USER-VERIFIED / HISTORICAL` - original post-repair WPILib VS
  Code clean build result is preserved below; it does not satisfy the reopened
  repair gate.

## Historical Implementation and Verification Evidence

- Original Production Implementation: `PASS / HISTORICAL` - factory and chooser
  snapshot wiring only.
- Initial User Failure Evidence: `430 total / 419 passed / 11 failed`.
- Root-Cause Audit: `PASS` - all 11 failures reproduced in their individual
  classes; no full-suite-only state leakage was found.
- Focused L08 Tests: `PASS` - factory 4/4, chooser 2/2, autonomous-mode 17/17,
  and PathPlanner integration 9/9 after the minimal test-contract migration.
- L07 Focused Regression: `PASS` - PathPlanner execution-path and trajectory
  adapter tests 14/14.
- Full Regression After L08 Delta: `PASS` - 430/430 tests passed with zero
  failures, errors, or skips in the source-complete local JUnit execution.
- Build After L08 Delta: `PASS / USER-VERIFIED` - post-repair WPILib VS Code
  build displayed `BUILD SUCCESSFUL in 1s` with `6 actionable tasks: 1
  executed, 5 up-to-date`. This supersedes the prior direct-Gradle
  classpath-resolution environment hold.
- Simulation: `PASS / USER-CONFIRMED` - chooser, SAFE_STOP, ONE_METER_PATH,
  Blue/Red execution, Disable/mode-loss stop, cancellation, and no-restart
  behavior verified. Runtime chooser change while already Autonomous enabled
  was not manually possible and is not claimed.
- Driver Station / Glass: `PASS / USER-CONFIRMED chooser/runtime observation`.
- Real Robot: `PASS / USER-CONFIRMED / HISTORICAL` - superseded as a closure
  gate by the post-freeze safety/robustness evidence.
- Transition Guide: `PASS` - final reconciliation and freeze decision are
  recorded in `docs/A01_L07_to_A01_L08_Step_by_Step.md`.
- Learning Guides: `PASS` - English and Vietnamese L08 guides created and
  reconciled with the verified runtime contracts.
- Git Commit: `NOT TESTED` - user-owned; Codex does not run Git.
- Git Push: `NOT TESTED` - user-owned; Codex does not run Git.

## Authoritative Design Lock

- Objective: routine selection and safe composition only.
- Minimum routines: `SAFE_STOP` default and `ONE_METER_PATH`.
- Selection owner: `RobotContainer` composition root.
- Selection surface: one `SendableChooser<AutonomousRoutineId>` exposed through
  SmartDashboard/NetworkTables.
- Selection timing: sampled once by `getAutonomousCommand()` during
  `Robot.autonomousInit()`; later chooser changes cannot affect the active run.
- Factory policy: fresh command instance per autonomous start; no persistent
  Command chooser entries.
- Safe default: non-driving `SAFE_STOP` for null/unknown selection, missing or
  invalid readiness/alliance, missing path, construction failure, or fault.
- Readiness: shared accepted-start-context consumption remains mandatory and
  one-shot; invalid readiness fails closed.
- Alliance: L04 remains the exactly-one transform owner.
- AutoBuilder flipping: `shouldFlipPath = false`; execution path
  `preventFlipping = true`.
- Requirements: every selected command uses scheduler-managed
  `SwerveSubsystem` ownership; no manual locking or parallel drive branches.
- Termination: normal completion, interruption, cancellation, Disable/mode loss,
  invalid state, and faults call centralized `SwerveSubsystem.stop()`.
- Automatic restart: forbidden.

## Implemented Delta

- Production: added `AutonomousRoutineFactory.java`; modified
  `RobotContainer.java` only for chooser construction, snapshot, and delegation.
- Tests: added focused factory and RobotContainer routine-selection tests; kept
  inherited L01-L07 regression source unchanged.
- No Robot.java, Constants, IO, SwerveSubsystem, RobotConfig, asset, CTRE, CAN,
  telemetry, or frozen predecessor changes are proposed.

## Known Issues and Deferred Scope

- L08 is `REOPENED / IN_PROGRESS / EDITABLE` for the approved
  safety/robustness scope. The original COMPLETE / FROZEN / READ-ONLY state and
  evidence remain historical; A01_L09 remains independently frozen.
- The prior local direct-Gradle `compileTestJava` classpath-resolution hold is
  superseded by the user-verified post-repair WPILib VS Code build PASS recorded
  above; no unresolved build gate remains.
- User-confirmed Simulation and Real Robot evidence is recorded above. No exact
  endpoint, precision, tuning, physical-model, or competition-readiness claim
  is made.
- NamedCommands, event markers, mechanism coordination, vision, AprilTags,
  replanning, pathfinding, competition strategy, drivetrain redesign, CTRE/CAN
  changes, calibration changes, PID/FF tuning, and RobotConfig characterization
  remain excluded.
- A01_L09 is `COMPLETE / FROZEN / READ-ONLY` and remains unmodified.
- New user real-robot evidence records successful `ONE_METER_PATH` execution,
  a stop near the expected endpoint, and one visible terminal steer correction
  without sustained oscillation. Forensic source review shows that after
  terminal zero output and centralized stop, the Swerve requirement may be
  released and `FieldRelativeTeleopDriveCommand` may reacquire it while
  Autonomous remains enabled. Because that command has no independent
  Teleop-enabled production-output gate, controller-derived drivetrain intent
  was theoretically possible outside Teleop. This safety/mode-ownership defect
  is now repaired within the approved production boundary.
- The former custom `PreparationLifecycleCommand` manually delegated child
  lifecycle calls. It has been removed and replaced by WPILib-native command
  composition under scheduler lifecycle ownership.

## Reopen and Re-Freeze Contract

- Previous lesson state: `COMPLETE / FROZEN / READ-ONLY`.
- Current lesson state: `REOPENED / IN_PROGRESS / EDITABLE`.
- Approved repair scope: preparation/readiness lifecycle, physically meaningful
  wrapped pose validation, recoverable/fatal fault handling, Blue/Red and
  selected-routine provenance, scheduler-safe Disabled preparation, path and
  AutoBuilder preflight diagnostics, immutable preparation observation,
  read-only telemetry, operator Prepare Autonomous workflow, tests, and docs.
- Approved terminal scope: scheduler-native Swerve-owning terminal hold,
  session-long SAFE_STOP ownership, defensive Teleop-enabled output gate,
  WPILib-native replacement of affected manual lifecycle delegation, and one
  `HOLDING` state if required. Target lifecycle:
  `CONSUMED -> RUNNING -> HOLDING -> COMPLETE`.
- Terminal implementation touched only the five production files named by the
  supplemental ADR. `SwerveSubsystem`, CTRE/IO,
  tuning, calibration, CANcoder offsets, assets, Gradle, vendordeps,
  RobotContainer absent separate review, and downstream lessons remain
  unauthorized.
- Re-freeze requires all 25 acceptance gates in the supplemental ADR, including
  deterministic terminal ownership, SAFE_STOP ownership, no Teleop output
  outside Teleop, no manual child lifecycle delegation, Blue/Red/SAFE_STOP and
  terminal-hold Simulation, transition tests, focused/full regression, clean
  build, user real-robot evidence, changed-file/documentation audits, and
  explicit Architect/User approval.
- Historical PASS evidence cannot substitute for those new gates.
- Preparation/readiness repair implementation: `AUTHORIZED / IMPLEMENTED`;
  local compile, test, and clean-build gates pass. Its Simulation and
  real-robot repair gates remain user-owned and not run. Terminal-ownership
  implementation is authorized, implemented, and locally verified.

## Current Reopened Repair Evidence

- Production repair: `PASS / IMPLEMENTED` - deterministic preparation
  coordinator, two-phase Disabled Prepare command, typed preflight/fault
  results, Option 3 readiness claim, lifecycle wrapper, immutable observation,
  and read-only telemetry.
- Preparation tolerances: `0.03 m` translation and `2.0 degrees` heading,
  provisional preparation gates only; heading uses
  `MathUtil.angleModulus(...)`.
- Scheduler safety: `PASS / TESTED` - active autonomous uses
  `kCancelIncoming`; incoming Prepare and legacy reset/heading commands cannot
  cancel it; mode loss/Disable stop and do not restart.
- compileJava: `PASS / CODEX LOCAL / WPILIB JAVA 17`.
- compileTestJava: `PASS / CODEX LOCAL / WPILIB JAVA 17` using an external
  temporary short-path init script for the known long-path javac environment
  defect; repository Gradle configuration is unchanged.
- Focused repair/integration tests: `PASS - 45/45`.
- Full test suite: `PASS - 445/445`, zero failures, errors, or skips.
- Clean build: `PASS - BUILD SUCCESSFUL in 1m 14s`, 7 actionable tasks
  executed.
- Simulation repair gate: `USER VERIFIED / PASS` - final evidence is recorded
  below.
- Real Robot repair gate: `USER VERIFIED / PASS` - final evidence is recorded
  below.
- This intermediate status record is superseded by the final closure evidence
  below; the lesson is now COMPLETE / FROZEN / READ-ONLY.
- Endpoint precision, final PID/feedforward tuning, and final physical
  characterization: explicitly unclaimed.
- This governance registration modified documentation only; no production Java,
  tests, configuration, assets, or frozen L01-L07/L09 files were modified.

## Failure Classification Record

- Category B (valid inherited-test migration): ten failures relied on implicit
  routine selection, a command snapshot created before readiness, or the old
  one-shot stop lifecycle. The tests now select `ONE_METER_PATH` explicitly,
  request fresh commands after accepted readiness, and assert bounded
  `SAFE_STOP` for fail-closed cases.
- Category F (focused fixture contract mismatch): one factory test asserted no
  Swerve requirement while its delegated command intentionally required Swerve.
  The assertion now verifies the locked requirement owner.
- Production defect in the original 11-failing-test audit: none found. This
  historical result does not negate the later source-proven terminal
  mode-ownership defect.
- Assertions weakened: no. Tests disabled/deleted/ignored/skipped: no.
- Frozen L07 modified: no.

## Terminal-Ownership Repair Evidence

- Design lock: `PASS` - Option D only.
- Terminal hold: `PASS / IMPLEMENTED` - fresh ONE_METER_PATH and SAFE_STOP
  commands retain the Swerve requirement throughout Autonomous Enabled.
- Lifecycle: `CONSUMED -> RUNNING -> HOLDING -> COMPLETE`; exactly one new
  state, `HOLDING`, was added.
- Preparation scheduler lifecycle: `PASS` - WPILib-native sequence/decorators
  own the repaired preparation composition; the former custom preparation
  wrapper's manual delegation was removed. The separate active AutoBuilder
  adapter wrapper remains the final architecture HOLD noted below.
- Mode exit: `PASS / TESTED` - the outer Autonomous-enabled lifetime guard
  ends the complete composition on Disable, Teleop, or Test transition.
- Teleop defensive gate: `PASS / TESTED` - outside Teleop Enabled the default
  command stops and returns without controller acquisition, telemetry publish,
  or drivetrain intent submission.
- Centralized stop: `PRESERVED`; no zero-speed chassis request was introduced.
- Production boundaries preserved: SwerveSubsystem, CTRE/IO, PID/feedforward,
  CANcoder calibration, RobotConfig, PathPlanner assets, Gradle, and vendordeps
  are unmodified.
- compileJava: `PASS / CODEX LOCAL / WPILIB JAVA 17`.
- compileTestJava: `PASS / CODEX LOCAL / WPILIB JAVA 17`.
- Focused terminal/Teleop tests: `PASS - 32/32`.
- Preparation regression tests: `PASS - 12/12`.
- Autonomous scheduling tests: `PASS - 29/29`.
- Full test suite: `PASS - 442/442`, zero failures or errors.
- Clean build: `PASS - BUILD SUCCESSFUL in 29s`, six executed tasks and one up-to-date.
- Environment note: verification used a temporary short-path copy solely to
  avoid the documented Windows javac path-resolution defect; repository build
  configuration is unchanged.
- Simulation: `USER VERIFIED / PASS` - final evidence is recorded below.
- Real Robot: `USER VERIFIED / PASS` - final evidence is recorded below.
- Final status is recorded below as REOPENED / IN_PROGRESS / EDITABLE because
  the architecture gate is HOLD.

## Final Closure Reconciliation - 2026-08-25

### Governance

- Governance: `PASS`.
- A01_L08: `REOPENED / IN_PROGRESS / EDITABLE`.
- V00_L02: `SUSPENDED / READ-ONLY / UNMODIFIED`.
- Single editable lesson: `A01_L08`; V00_L02 remains read-only.
- A01_L01-L07, A01_L09, and V00_L01: `COMPLETE / FROZEN / READ-ONLY / UNMODIFIED`.
- Forward-port: `REQUIRED LATER / NOT PERFORMED`.

Required status fields:

- Architecture Review: `HOLD` - active AutoBuilder adapter still manually
  delegates child lifecycle callbacks.
- Baseline Build: `PASS / USER-CONFIRMED / HISTORICAL INHERITED BASELINE`.
- Build: `PASS / USER VERIFIED` - `BUILD SUCCESSFUL in 29s` final repair build.
- Simulation: `PASS / USER VERIFIED`.
- Driver Station / Glass: `PASS / USER VERIFIED`.
- Real Robot: `PASS / USER VERIFIED`.
- Transition Guide: `FINAL / HOLD` pending architecture resolution.
- Git Commit: `NOT TESTED - USER OWNED`.
- Git Push: `NOT TESTED - USER OWNED`.
- Known Issues: exact endpoint accuracy, final PID/feedforward tuning, final
  physical characterization, and all forward-port work remain unclaimed or
  deferred.

### Authoritative Documents Read

AGENTS.md, repository README.md, Document A Frozen Backbone and ES-06 Frozen
Interface Contract, all English Document B engineering/workflow/coding/
architecture/module standards, all English Document C observation standards,
the A01 roadmap ADR, the A01_L08 reopen ADR, the active lesson records, and the
active L08 source/test boundary were reviewed before closure reconciliation.

### Final Automated Verification

- `compileJava`: PASS.
- `compileTestJava`: PASS.
- Focused terminal/Teleop tests: `32/32 PASS`.
- Preparation regression: `12/12 PASS`.
- Autonomous scheduling regression: `29/29 PASS`.
- Full test suite: `442/442 PASS`, zero failures/errors.
- Clean build: `BUILD SUCCESSFUL in 29s`, zero failures/errors.

### Final Simulation - USER VERIFIED

- Blue `ONE_METER_PATH` Prepare -> READY: PASS.
- Recoverable first-attempt `RESET_REJECTED` -> second Prepare READY without
  Robot Code restart: PASS.
- Blue execution: PASS.
- Final pose approximately `1.005 m`: PASS.
- Terminal ownership hold while Autonomous remains Enabled: PASS.
- Simulated joystick input after path completion while Autonomous remains
  Enabled produced no drivetrain movement: PASS.
- Autonomous -> Disabled -> Teleop: PASS.
- Teleop resumes normally: PASS.
- SAFE_STOP: PASS.
- Red `ONE_METER_PATH`: PASS.
- No Robot Code restart between Blue/Red/recovery tests: PASS.
- No automatic autonomous restart: PASS.

### Final Real Robot - USER VERIFIED

- Correct repaired A01_L08 deployed: PASS.
- Teleop sanity test: PASS.
- AutonomousPreparation telemetry visible: PASS.
- Blue `ONE_METER_PATH` Prepare -> READY: PASS.
- Blue `ONE_METER_PATH` execution: PASS.
- Repeat Blue execution without Robot Code restart: PASS.
- SAFE_STOP: PASS.
- Red `ONE_METER_PATH` Prepare -> READY: PASS.
- Red `ONE_METER_PATH` execution: PASS.
- Blue -> Red transition without Robot Code restart: PASS.
- Recoverable preparation without restart: PASS.
- Disable/mode-loss stop: PASS.
- No automatic restart: PASS.
- Steering terminal twitch before repair: `PRESENT`.
- Steering terminal twitch after repaired terminal ownership: `ABSENT - USER VERIFIED`.
- Teleop after Autonomous transition remains normal: PASS.
- No PID/FF change required: CONFIRMED.
- No CANcoder recalibration required: CONFIRMED.
- No hardware defect established: NOT ESTABLISHED.

### Architecture Review

- Architecture review: `HOLD`.
- RobotContainer composition-root contract: PASS.
- SwerveSubsystem sole drivetrain/stop/localization owner: PASS.
- Frozen Backbone: PRESERVED.
- Frozen Interface Contract: PRESERVED.
- A01_L04 sole alliance-transform owner: PASS.
- `shouldFlipPath=false`: PRESERVED.
- `preventFlipping=true`: PRESERVED.
- Prepare cannot cancel running autonomous or terminal hold: PASS.
- SAFE_STOP retains ownership and cannot fall through to Teleop default drive: PASS.
- Teleop mode gate prevents output outside Teleop: PASS.
- Manual child lifecycle delegation: `HOLD` - `SafeAutoBuilderCommand` still
  invokes delegate `initialize()`, `execute()`, `isFinished()`, and `end()`.
- Scheduler-native composition: `HOLD` for the active adapter wrapper; the
  repaired preparation composition itself uses WPILib-native sequencing.
- No automatic autonomous restart: PASS.
- No hardware/tuning/calibration/asset/dependency scope creep: PASS.

### Changed-File Audit

Production files modified:

- `src/main/java/frc/robot/commands/AutoBuilderContractAdapter.java`
- `src/main/java/frc/robot/commands/AutonomousPreparationCoordinator.java`
- `src/main/java/frc/robot/commands/AutonomousRoutineFactory.java`
- `src/main/java/frc/robot/commands/AutonomousSafetyHoldCommand.java`
- `src/main/java/frc/robot/commands/FieldRelativeTeleopDriveCommand.java`
- `src/main/java/frc/robot/commands/PrepareAutonomousCommand.java`
- `src/main/java/frc/robot/Constants.java`
- `src/main/java/frc/robot/RobotContainer.java`
- `src/main/java/frc/robot/RobotTelemetry.java`
- `src/main/java/frc/robot/observation/autonomous/AutonomousPreparationObservation.java`
- `src/main/java/frc/robot/telemetry/autonomous/AutonomousPreparationTelemetryFacade.java`

Test files modified:

- `src/test/java/frc/robot/commands/AutoBuilderContractAdapterRecoveryTest.java`
- `src/test/java/frc/robot/commands/AutonomousPreparationCoordinatorTest.java`
- `src/test/java/frc/robot/commands/AutonomousRoutineFactoryTest.java`
- `src/test/java/frc/robot/commands/AutonomousSafetyHoldCommandTest.java`
- `src/test/java/frc/robot/commands/FieldRelativeTeleopDriveCommandTest.java`
- `src/test/java/frc/robot/commands/FieldRelativeTeleopProductionPathTest.java`
- `src/test/java/frc/robot/commands/PrepareAutonomousCommandTest.java`
- `src/test/java/frc/robot/RobotContainerAutonomousModeSchedulingTest.java`
- `src/test/java/frc/robot/RobotContainerAutonomousRoutineSelectionTest.java`
- `src/test/java/frc/robot/RobotContainerPathPlannerIntegrationTest.java`
- `src/test/java/frc/robot/telemetry/autonomous/AutonomousPreparationTelemetryFacadeTest.java`

Documentation files modified in this closure:

- `AGENTS.md`
- `README.md`
- `docs/architecture_decisions/ADR_A01_L08_Autonomous_Safety_Robustness_Reopen.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/README.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/LESSON_STATUS.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/LESSON_PLAN.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/LESSON_CHECKLIST.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/docs/A01_L07_to_A01_L08_Step_by_Step.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/docs/A01_L08_Autonomous_Routine_Selection_and_Safe_Composition_Learning_Guide_EN.md`
- `real_robot_programming/module_A01/A01_L08_AutonomousRoutineSelectionAndSafeComposition/docs/A01_L08_Autonomous_Routine_Selection_and_Safe_Composition_Learning_Guide_VI.md`

Configuration files: `NONE`.

PathPlanner assets: `NONE`.

Vendordep files: `NONE`.

Unexpected files: `NONE`.

Generated/IDE files: excluded from the audit.

### Documentation and Git

- Documentation closure: `HOLD` - architecture gate unresolved.
- Transition Guide final: `HOLD` pending architecture resolution.
- Git commit: `NOT TESTED - USER OWNED`.
- Git push: `NOT TESTED - USER OWNED`.
- Ready for user GitHub publication: `NO - final architecture gate is HOLD`.

## Scheduler Exception Boundary Governance Amendment - 2026-08-25

- Governance amendment: `PASS / ARCHITECT APPROVED FOR GOVERNANCE SCOPE ONLY`.
- New evidence: active `SafeAutoBuilderCommand` manually delegates child
  lifecycle callbacks, and a single adapter-only boundary cannot preserve the
  required fail-closed exception semantics.
- Approved future design: Option F - scheduler-native AutoBuilder composition,
  existing narrow protections, Robot-level scheduler `RuntimeException` boundary,
  coordinator/adapter fault bridge, centralized Swerve stop, immutable
  `FAULTED`, and no automatic restart.
- Authorized future production files: `AutoBuilderContractAdapter.java`,
  `AutonomousPreparationCoordinator.java`, `RobotContainer.java`, and `Robot.java`.
- Authorized future test files: `RobotContainerPathPlannerIntegrationTest.java`,
  `AutonomousRoutineFactoryTest.java`, `AutonomousPreparationCoordinatorTest.java`,
  and new `RobotSchedulerExceptionBoundaryTest.java`; related safety tests may be
  rerun unchanged.
- Implementation authorization: `NO` - this amendment is documentation and
  change-control only.
- Current architecture: `HOLD` - scheduler-native lifecycle and Robot-level
  exception-boundary gates remain unimplemented.
- Production files modified by this amendment: `NONE`.
- Test files modified by this amendment: `NONE`.
- Configuration, assets, dependencies, frozen lessons, and V00_L02: `UNMODIFIED`.

## Final Decision

`A01_L08 FINAL VERDICT: HOLD`.

`A01_L08 FINAL STATUS: REOPENED / IN_PROGRESS / EDITABLE`.

Do not create or activate A01_L09. Do not resume V00_L02. V00_L02 resumption
requires separate downstream governance reconciliation and forward-port approval.

## Final Scheduler-Native Exception Boundary Implementation — 2026-08-25

- Implementation authorization: `YES / FINAL ARCHITECT-USER ACTION`.
- Active lesson: `REOPENED / IN_PROGRESS / EDITABLE`; V00_L02 remains
  `SUSPENDED / READ-ONLY`.
- `SafeAutoBuilderCommand` and its manual child `initialize`, `execute`,
  `isFinished`, and `end` delegation were removed. The AutoBuilder path now
  uses WPILib-native scheduler composition for follow-path execution, timeout,
  mode loss, and final stop; terminal `HOLDING` remains coordinator-owned.
- `Robot` now provides the top-level scheduler `RuntimeException` boundary;
  `RobotContainer` provides the narrow safety bridge; and the coordinator
  latches the adapter/coordinator fatal state, invokes independent centralized
  stop, publishes immutable `FAULTED`, rejects future autonomous output, and
  does not restart autonomous. No unconditional scheduler requirement-release
  claim is made.
- Safety equivalence: `PASS` by source review. Diagnostic equivalence: `PARTIAL`;
  the first fatal diagnostic retains exception type/message where available.
- Authorized production files changed by this implementation only:
  `src/main/java/frc/robot/commands/AutoBuilderContractAdapter.java`,
  `src/main/java/frc/robot/commands/AutonomousPreparationCoordinator.java`,
  `src/main/java/frc/robot/RobotContainer.java`, and
  `src/main/java/frc/robot/Robot.java`.
- Authorized test files changed by this implementation only:
  `src/test/java/frc/robot/commands/AutonomousPreparationCoordinatorTest.java`
  and new `src/test/java/frc/robot/RobotSchedulerExceptionBoundaryTest.java`.
- `compileJava`: `PASS` under WPILib Java 17 after a clean compile.
- `compileTestJava`: `FAIL / ENVIRONMENT HOLD`; both normal and short-path
  attempts retained the existing Windows Gradle/Javac classpath-resolution
  failure, with main project classes unresolved from test compilation. Gradle
  and vendordeps were not changed. Focused tests, full suite, and clean build
  were not run for this implementation because the required test compilation
  gate did not pass.
- Simulation: `NOT RERUN / USER GATE`.
- Driver Station / Glass: `NOT RERUN / USER GATE`.
- Real Robot: `NOT RERUN / USER GATE`.
- Re-freeze: `HOLD`; this implementation record does not mark the lesson
  `COMPLETE` or `FROZEN`.

## Final Re-Freeze Status — 2026-08-26

- Previous active state: `REOPENED / IN_PROGRESS / EDITABLE`.
- Automated verification: `PASS` - `compileJava`, `compileTestJava`,
  `RobotSchedulerExceptionBoundaryTest`, 449/449 full suite, and clean build.
- Simulation: `USER VERIFIED / PASS` - Blue and Red ONE_METER_PATH, terminal
  hold, Autonomous joystick blocking, mode-transition Teleop recovery,
  SAFE_STOP, and no-restart behavior.
- Driver Station / Glass: `USER VERIFIED / PASS`.
- Real Robot: `USER VERIFIED / PASS` - approximately one-metre path, Blue and
  previously verified Red behavior, preparation/recovery, terminal ownership,
  no automatic restart, and Teleop recovery.
- Terminal steer: `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`; `ACCEPTED FOR
  CURRENT LESSON`; `DEFERRED FOR FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`.
  Root cause is not fully proven; no sustained oscillation or uncontrolled
  drivetrain motion was observed.
- Performance note: one approximately 5.9 ms desktop periodic sample is not
  roboRIO proof; no blocking CAN wait or production defect was proven; future
  target-hardware measurement is optional and not a closure blocker.
- Architecture Review: `PASS`.
- Transition Guide: `PASS / FINAL`.
- Documentation: `PASS / FINAL`.
- Known Issues: accepted bounded terminal steer transient; final drivetrain and
  path-following tuning remain future work.
- Final Verdict: `PASS`.
- Final Status: `COMPLETE / FROZEN / READ-ONLY`.
- V00_L02: `SUSPENDED / READ-ONLY / UNMODIFIED`; no automatic resume.
- Ready for Publication: `YES - USER OWNED`.
- Git Commit / Push: `NOT TESTED - USER OWNED`.
