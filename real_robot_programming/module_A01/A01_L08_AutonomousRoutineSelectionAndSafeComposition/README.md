# A01_L08 - Autonomous Routine Selection and Safe Composition

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`
- Title: `A01_L08 - Autonomous Routine Selection and Safe Composition`
- Previous lesson: `A01_L07_AutoBuilderContractIntegration - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `RE-FROZEN 2026-08-26 - reopen and original closure history preserved`
- Preparation/readiness repair: `AUTHORIZED / IMPLEMENTED / LOCAL REVIEW GATES PASS`
- Terminal-ownership repair: `AUTHORIZED / IMPLEMENTED / VERIFIED / PASS`
- Terminal-ownership implementation: `AUTHORIZED / IMPLEMENTED / PASS`
- Final scheduler exception-boundary repair: `AUTHORIZED / IMPLEMENTED /
  VERIFIED / PASS`.
- User inheritance: `PASS` - the user copied and renamed frozen L07.
- Baseline Build: `PASS / USER-CONFIRMED` - compileJava, compileTestJava,
  tests, and clean build.
- Production Implementation: `PASS / HISTORICAL` - routine factory and chooser
  snapshot wiring. The final scheduler-native exception-boundary implementation
  is recorded below.
- Simulation: `PASS / USER VERIFIED / FINAL REPAIR EVIDENCE`
- Driver Station / Glass: `PASS / USER VERIFIED`
- Real Robot: `PASS / USER VERIFIED / FINAL REPAIR EVIDENCE`
- Git Commit / Push: `NOT TESTED` - user-owned; Codex does not run Git.

## Exceptional Safety / Robustness Reopen

The Architect and User approved a narrow governance reopen after new
real-robot evidence showed that a chooser-selected `ONE_METER_PATH` could leave
the robot stationary, recovery could depend nondeterministically on Robot Code
restart, and normal preparation did not always recover Blue/Red operation.
Readiness is one-shot; reset context is compared with an unrealistically strict
`1e-9` metre/radian tolerance; heading wrap, estimator updates, process-latched
transient faults, hidden `firstFaultReason()`, and Disabled preparation
requirement conflicts require formal repair design.

This is `POST-FREEZE SAFETY / ROBUSTNESS EVIDENCE`, not a feature request. The
original closure and all original PASS evidence remain preserved as historical
facts. A01_L08 was the sole editable lesson during the repair. V00_L02 is
`SUSPENDED / READ-ONLY`; A01_L01-L07, A01_L09, and V00_L01 remain frozen. A later
separate implementation authorization covered the exact terminal scope; no
forward-port or Git operation was performed.

## Authoritative Objective

“One concept: selecting and composing autonomous routines with explicit
cancellation, requirements, and failure behavior.”

L08 is the routine-selection boundary after frozen L07. L09 owns PathPlanner
NamedCommands and event markers; L08 does not introduce marker dispatch,
mechanism-event coordination, or competition strategy.

## Terminal Ownership Governance Amendment

New user real-robot evidence records that `ONE_METER_PATH` executes, stops near
the expected endpoint, and shows one visible terminal steer correction without
sustained oscillation; Teleop otherwise operates normally. The forensic source
chain is PathPlanner terminal zero output, `SafeAutoBuilderCommand.end()`,
centralized `SwerveSubsystem.stop()`, Swerve requirement release, then possible
default `FieldRelativeTeleopDriveCommand` reacquisition while Autonomous
remains enabled. Because the Teleop command has no independent
`DriverStation.isTeleopEnabled()` production-output gate, nonzero controller
input could theoretically submit drivetrain intent outside Teleop.

Architect and User approved Option D as a documentation-only scope expansion:
a scheduler-native autonomous terminal hold retaining Swerve, session-long
SAFE_STOP ownership, a minimum defensive Teleop-enabled output gate,
WPILib-native replacement of affected manual child lifecycle delegation, and
exactly one `HOLDING` state if required. The target lifecycle is
`CONSUMED -> RUNNING -> HOLDING -> COMPLETE`. During `HOLDING`, motion is
complete and centralized stop has occurred, but Swerve remains owned until
Autonomous exits; the default Teleop drive cannot reacquire it and autonomous
motion cannot restart.

This amendment does not authorize implementation. It does not authorize
changes to SwerveSubsystem, CTRE or other IO, tuning, calibration, CANcoder
offsets, PathPlanner assets, Gradle, vendordeps, RobotContainer without a
separate review, frozen A01_L01-L07/L09 or V00_L01, or suspended V00_L02. The
one-time steer correction is not evidence authorizing tuning or recalibration.

## Inheritance and Metadata Audit

- Directory identity: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`.
- `settings.gradle`: present, repaired by the user to valid UTF-8 without a BOM.
- `.wpilib/wpilib_preferences.json`: preserved with `teamNumber=10951`,
  `projectYear=2026`, and `currentLanguage=java`.
- `.vscode/`, `src/`, `vendordeps/`, `gradle/`, Gradle wrapper files, build files,
  documentation, tests, and PathPlanner assets are preserved.
- The user-owned PowerShell identity rewrite caused an encoding failure; the
  user repaired it before the accepted baseline. This history is retained in
  the transition record.
- The accepted baseline is user evidence; Codex does not claim to have run it.

## Routine Vocabulary

- **Path:** a geometric/runtime PathPlanner path asset.
- **Trajectory:** a time-parameterized motion representation.
- **Path-following command:** a scheduler-owned command that executes a path or
  trajectory.
- **Autonomous routine:** one or more safe scheduler-owned command steps that
  form one autonomous behavior under the repository readiness and lifecycle
  contracts.
- **Routine selection:** choosing the approved routine for the next autonomous
  start.
- **Safe composition:** combining command steps while preserving requirements,
  cancellation, mode-loss behavior, centralized stop, readiness consumption,
  and fail-closed semantics.

## Minimum Routine Set

The smallest useful L08 set is exactly two routine identities:

1. `SAFE_STOP` - the default non-driving routine, implemented with the existing
   bounded `AutonomousSafetyHoldCommand` and the existing safety-stop constant.
2. `ONE_METER_PATH` - the known one-meter AutoBuilder routine using the frozen
   L07 adapter and execution-path contract.

No scoring behavior, mechanism action, competition strategy, extra path, or
multi-event routine is added. These two choices prove selection semantics while
keeping the lesson boundary small.

## Selection Ownership and Snapshot Lock

`RobotContainer` remains the composition root. It will construct one
`SendableChooser<AutonomousRoutineId>`, set `SAFE_STOP` as the default, add
`ONE_METER_PATH`, and expose the chooser through SmartDashboard/NetworkTables.
The chooser contains immutable routine identities, not persistent Command
instances.

`Robot.autonomousInit()` already requests `RobotContainer.getAutonomousCommand()`.
L08 will sample the chooser exactly once during that request, create a fresh
command through the routine factory, and schedule that snapshot. Selection
changes after autonomous starts cannot replace, restart, or mutate the active
command. A later autonomous start obtains a new snapshot and a fresh command.

## Routine Factory and Safe Fallback

The proposed `AutonomousRoutineFactory` owns the two routine identities and
fresh command construction. It does not access IO, hardware, telemetry, or
alliance mathematics. `RobotContainer` supplies the existing
`SwerveSubsystem`, `AutoBuilderContractAdapter`, and accepted start context.

All selections pass through the shared readiness gate and consume the accepted
start context once. `ONE_METER_PATH` requires a present, valid context. A null
or unknown chooser value, missing readiness, invalid alliance, missing path,
factory exception, or malformed routine returns the non-driving `SAFE_STOP`
fallback and calls the centralized stop authority. No driving routine is
silently substituted.

## Readiness and Alliance Contract

L08 does not bypass the inherited Disabled-only heading/start-pose procedure,
accepted pose reset, alliance validation, field-frame authority, one-shot
readiness consumption, or Autonomous+Enabled gating.

`A01/L04 FieldAllianceTransform` remains the exactly-one transform owner.
Canonical Blue path ownership remains unchanged. AutoBuilder vendor flipping
stays disabled (`shouldFlipPath = false`), and any execution path continues to
use `preventFlipping = true`. L08 selection does not create separate Blue and
Red routine copies or add a second transform.

## Requirement and Termination Contract

Every driving routine ultimately requires the existing `SwerveSubsystem`
through scheduler-managed commands. `SAFE_STOP` also retains the subsystem
requirement so it owns the same safe terminal boundary. No manual requirement
locking or parallel drive-owning branches are permitted.

Normal completion, interruption, cancellation, Disabled transition, mode loss,
invalid readiness/alliance, missing selection/path, construction failure, and
command fault must terminate through centralized `SwerveSubsystem.stop()`.
No automatic restart is allowed. The chooser is not read during execution.

## Implemented Production Delta

- Added `src/main/java/frc/robot/commands/AutonomousRoutineFactory.java` for
  the two immutable routine identities, fresh command factories, readiness-aware
  selection, and safe fallback.
- Modified `src/main/java/frc/robot/RobotContainer.java` only to construct and
  publish the chooser, snapshot the selected identity, and delegate fresh
  command construction.
- No `Robot.java` change is required because it already samples
  `getAutonomousCommand()` in `autonomousInit()`.
- No Constants, IO, SwerveSubsystem, RobotConfig, PathPlanner asset, CTRE, CAN,
  telemetry, or frozen predecessor change is proposed.

## Implemented Test Delta

- Added `src/test/java/frc/robot/commands/AutonomousRoutineFactoryTest.java` for
  the two identities, fresh command instances, safe default/fallback,
  readiness failure, requirement ownership, and terminal stop behavior.
- Added `src/test/java/frc/robot/RobotContainerAutonomousRoutineSelectionTest.java`
  for chooser visibility/default, one-time snapshot semantics, selection changes
  during active execution, no automatic restart, Blue canonical behavior, Red
  exactly-one L04 transform, and inherited L07 adapter settings.
- Preserved inherited L01-L07 production source and tests unchanged.

The focused matrix also covers missing-path and invalid-alliance fallback,
missing-readiness fallback, normal completion, cancellation/interruption,
Disable/mode loss, terminal stop, no automatic restart, and rejection of any
second AutoBuilder flip.

## User-Owned Verification Evidence

### Simulation

The user verified chooser visibility, SAFE_STOP as the safe default, explicit
ONE_METER_PATH selection, successful Blue and Red execution, Disable and
mode-loss stopping, cancellation, no automatic restart, and no restart after
re-enable without fresh readiness. The UI did not permit changing to
ONE_METER_PATH while Autonomous was already enabled; no manual runtime-change
result is claimed. The selection/snapshot contract remains covered by
automated tests and implementation review.

### Real Robot

The user confirmed A01_L08 real-robot verification PASS. No endpoint precision,
final PID/feedforward, RobotConfig characterization, mass/MOI/COF result, or
competition-readiness claim is made.

## Exclusions

NamedCommands, event markers, marker callbacks, mechanism coordination, vision,
AprilTags, dynamic replanning, obstacle avoidance, pathfinding, competition
strategy, drivetrain or Swerve IO redesign, CTRE/CAN changes, Swerve offsets,
drive ratio, PID/feedforward tuning, RobotConfig physical characterization, and
unnecessary catalog/framework abstraction remain outside L08.

## Post-Implementation Failure Audit

The user supplied the initial L08 result as 430 tests with 419 passing and 11
failing. The four failing classes were then run independently, proving that the
failures reproduced outside the full-suite order and were not state leakage.
Ten inherited failures were Category B migrations from the old implicit,
persistent autonomous-session assumptions: they now select `ONE_METER_PATH`
explicitly and request the command after readiness is accepted. The bounded
`SAFE_STOP` fallback is asserted for missing readiness, invalid alliance, and
adapter failure. The eleventh failure was Category F: a focused fixture
asserted that a delegated path command must not require Swerve even though the
locked contract requires that ownership; the assertion now checks the required
Swerve ownership.

No production defect was found. No assertion was weakened, and no test was
deleted, disabled, ignored, or skipped. The repaired source-complete JUnit run
reported 430/430 tests passing with zero failures, errors, or skips, including
the factory, chooser, autonomous-mode, PathPlanner integration, inherited
autonomous, and frozen-L07 PathPlanner regression classes.

## Historical Closure and Current Repair Boundary

L08 was `COMPLETE / FROZEN / READ-ONLY`; that original closure remains
historical. L08 was then `REOPENED / IN_PROGRESS / EDITABLE` for the exact
safety/robustness scope registered in the supplemental ADR. The original implementation remains
limited to routine selection and safe composition. The user verified the original post-repair
WPILib VS Code build: `BUILD SUCCESSFUL in 1s` with `6 actionable tasks: 1
executed, 5 up-to-date`. The accepted source-complete result is 430/430 tests
with zero failures, errors, or skips; Simulation and Real Robot are also
`PASS / USER-CONFIRMED`.

The prior direct-Gradle classpath-resolution hold is superseded by that
authoritative user-owned build evidence for the original closure. A01_L09 now
exists and remains `COMPLETE / FROZEN / READ-ONLY`; it was not modified by the
reopen. The later repair and final closure evidence are recorded below. Exact
endpoint accuracy, final PID/feedforward tuning, final RobotConfig
characterization, and final physical characterization remain explicitly
unclaimed.

## Reopened Safety / Robustness Repair Implementation

The current repair replaces the fragile implicit preparation token with one
`AutonomousPreparationCoordinator` lifecycle:
`UNPREPARED -> VALIDATING -> NOT_READY/READY -> STALE/CONSUMED -> RUNNING ->
COMPLETE/INTERRUPTED/FAULTED`. Every attempt has a monotonic ID. Driving READY
binds alliance, routine, field variant, expected start pose, heading-capture
attempt, and path identity. The factory previews READY, constructs a fresh
command, then atomically claims that same attempt. SAFE_STOP requires and
consumes no driving readiness.

The operator workflow is now one Disabled-only `Prepare Autonomous` command.
It captures the heading reference, allows one scheduler/subsystem refresh,
resets the known start pose, validates pose and measured speeds, and performs
static PathPlanner/AutoBuilder preflight without calling `followPath` or
scheduling motion. The separate production BACK/View and dashboard reset paths
were removed; the legacy commands remain only for inherited regression tests.

Preparation pose validation uses provisional named tolerances of `0.03 m` and
`2.0 degrees`. Heading error uses the wrapped shortest-angle difference through
`MathUtil.angleModulus`, so `+pi` and `-pi` are equivalent. These values are
preparation gates only and do not claim endpoint accuracy, PID/feedforward
tuning, drivetrain tuning, or final physical characterization.

Temporary pose/speed absence, finite pose mismatch, reset rejection, changed
alliance/routine/provenance, consumed readiness, Prepare outside Disabled, mode
loss, and timeout remain recoverable. Configuration exceptions, nonfinite
inputs/outputs, delegate lifecycle exceptions, impossible requirements, and
confirmed static path/configuration defects latch the first fatal reason for
the process lifetime and fail closed.

An immutable vendor-neutral `AutonomousPreparationObservation` is published by
a read-only telemetry facade. It reports lifecycle state, reason, attempt,
provenance, pose errors, speed/path availability, AutoBuilder/fatal state,
first fatal reason, returned command classification, and running state.

Historical local WPILib Java 17 verification was complete: `compileJava` PASS,
`compileTestJava` PASS, 45/45 focused/integration tests PASS, 445/445 full tests
PASS with zero failures/errors/skips, and `clean build` PASS (`BUILD SUCCESSFUL
in 1m 14s`, 7 executed tasks). A temporary external short-path Gradle init
script and a `subst` drive were required only to avoid the already documented
Windows javac resolution defect on this lesson's long absolute path; no Gradle
or repository configuration was changed. Simulation and real-robot repair
verification were pending at this intermediate checkpoint; the final
user-owned gates are recorded below.

## Terminal Ownership Safety Repair

The authorized Option D repair is implemented. `ONE_METER_PATH` now uses a
scheduler-native sequence for RUNNING, the fresh path command, HOLDING, and a
fresh autonomous-session safety hold. An outer Autonomous-enabled lifetime
guard ends the complete composition promptly on mode exit, and
`kCancelIncoming` preserves uninterrupted Swerve ownership while Autonomous
remains enabled. The former custom preparation wrapper no longer calls child
command lifecycle methods directly.

Every `SAFE_STOP` result is a fresh `AutonomousSafetyHoldCommand`. It requires
Swerve, calls centralized `SwerveSubsystem.stop()` on initialize and end,
submits no motion request, retains the requirement throughout Autonomous
Enabled, and releases safely when the Autonomous session ends. The lifecycle
contains exactly one added state, `HOLDING`, meaning path motion has completed
while stopped Swerve ownership remains active.

`FieldRelativeTeleopDriveCommand.execute()` now stops and returns before
reading, processing, publishing, or submitting controller intent unless
`DriverStation.isTeleopEnabled()` is true. Existing Teleop-enabled behavior is
unchanged. No SwerveSubsystem, CTRE/IO, PID/feedforward, CANcoder calibration,
RobotConfig, PathPlanner asset, Gradle, or vendordep change was made. A residual
one-time physical steering transient remains possible and is not claimed
eliminated.

Historical local WPILib Java 17 evidence for this terminal repair is: compileJava PASS,
compileTestJava PASS, focused terminal/Teleop tests 32/32 PASS, preparation
regression 12/12 PASS, autonomous scheduling regression 29/29 PASS, full suite
442/442 PASS, and clean build PASS (`BUILD SUCCESSFUL in 29s`, six executed
tasks and one up-to-date). Verification used a temporary short-path copy to avoid the documented
Windows javac path-resolution defect; repository build configuration was not
changed. The final user-owned Simulation and real-robot gates are recorded in
the closure section below.

## Final Documentation Closure Audit - HOLD

### Final Verdict

- A01_L08 final verdict: `HOLD`.
- A01_L08 final status: `REOPENED / IN_PROGRESS / EDITABLE`.
- Transition Guide: `FINAL / HOLD` pending architecture resolution.
- V00_L02: `SUSPENDED / READ-ONLY / UNMODIFIED`.
- Forward-port: required later, not performed.
- A01_L09 and V00_L01: preserved and unmodified.

### Final Automated Verification

- `compileJava`: PASS.
- `compileTestJava`: PASS.
- Focused terminal/Teleop tests: `32/32 PASS`.
- Preparation regression: `12/12 PASS`.
- Autonomous scheduling regression: `29/29 PASS`.
- Full test suite: `442/442 PASS`, zero failures/errors.
- Clean build: `BUILD SUCCESSFUL in 29s`, zero failures/errors.

### Final Simulation Evidence - USER VERIFIED

- Blue `ONE_METER_PATH` Prepare -> READY: PASS.
- Recoverable first-attempt `RESET_REJECTED` -> second Prepare READY without
  Robot Code restart: PASS.
- Blue execution: PASS; final pose approximately `1.005 m`: PASS.
- Terminal ownership hold while Autonomous remains Enabled: PASS.
- Simulated joystick input after path completion while Autonomous remains
  Enabled produced no drivetrain movement: PASS.
- Autonomous -> Disabled -> Teleop transition: PASS; Teleop resumed normally:
  PASS.
- SAFE_STOP: PASS.
- Red `ONE_METER_PATH`: PASS.
- No Robot Code restart between Blue/Red/recovery tests: PASS.
- No automatic autonomous restart: PASS.

### Final Real-Robot Evidence - USER VERIFIED

- Correct repaired A01_L08 deployed: PASS.
- Teleop sanity test and AutonomousPreparation telemetry visibility: PASS.
- Blue Prepare -> READY and execution: PASS.
- Repeat Blue execution without Robot Code restart: PASS.
- SAFE_STOP: PASS.
- Red Prepare -> READY and execution: PASS.
- Blue -> Red transition without Robot Code restart: PASS.
- Recoverable preparation without restart, Disable/mode-loss stop, and no
  automatic restart: PASS.
- Teleop after Autonomous transition remained normal: PASS.
- Steering terminal twitch before repair: PRESENT.
- Steering terminal twitch after repair: ABSENT - USER VERIFIED.
- No PID/FF change, CANcoder recalibration, or hardware defect established:
  CONFIRMED / NOT ESTABLISHED.

The steering observation is recorded as user-observed validation consistent with
the ownership repair; it does not prove every possible physical cause.

### Blocking Architecture Finding

The active `AutoBuilderContractAdapter.SafeAutoBuilderCommand` still manually
invokes its delegate's `initialize()`, `execute()`, `isFinished()`, and `end()`
methods. This conflicts with the approved scheduler-native no-manual-child-
lifecycle re-freeze gate. The implementation is therefore not eligible for
final PASS or re-freeze under the current no-production-change task. The issue
is recorded only; no production or test change was made.

### Final Architecture Review

`HOLD` for final architecture review. RobotContainer composition-root ownership,
SwerveSubsystem sole
drivetrain/stop/localization ownership, Frozen Backbone and Frozen Interface
Contract preservation, A01_L04 sole alliance transform, `shouldFlipPath=false`,
`preventFlipping=true`, Prepare protection of running autonomous and terminal
hold, SAFE_STOP ownership, and the defensive Teleop-enabled output gate pass.
WPILib-native composition owns the repaired preparation lifecycle, but manual
child lifecycle delegation in the active adapter wrapper remains unresolved.
No tuning, calibration, hardware, asset, dependency, or unrelated feature scope
was introduced.

### Final Changed-File Audit

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

Documentation files are reconciled in the final closure audit. Configuration,
PathPlanner assets, vendordeps, generated/IDE files, frozen lessons, and V00
files are `NONE`. Unexpected files: `NONE`.

Git/GitHub operations: `NONE - USER OWNED`. Ready for user GitHub publication:
`NO - final architecture gate is HOLD`.

## Final Scheduler-Native Exception Boundary Implementation — 2026-08-25

This latest implementation record supersedes the earlier closure finding that
the active AutoBuilder adapter still manually delegated child lifecycle. The
separate final Architect/User action authorized implementation within the exact
approved A01_L08 boundary.

- `SafeAutoBuilderCommand` was removed. `AutoBuilder.followPath(executionPath)`
  is now composed with WPILib-native timeout, mode-loss, race, and `finallyDo`
  commands; the CommandScheduler owns the child lifecycle.
- `AutonomousPreparationCoordinator` has the minimum scheduler-fatal entry and
  retains terminal `HOLDING` ownership. `RobotContainer` provides one narrow
  safety bridge. `Robot` catches scheduler-boundary `RuntimeException` and
  preserves telemetry execution in `finally`.
- Fail-closed safety equivalence is preserved: centralized stop, first fatal
  reason, future autonomous-output rejection, immutable `FAULTED`, and no
  automatic restart. Diagnostic equivalence is partial because the first
  diagnostic now includes exception type/message where available.
- Authorized production files modified: `AutoBuilderContractAdapter.java`,
  `AutonomousPreparationCoordinator.java`, `RobotContainer.java`, and
  `Robot.java`. Authorized test files modified: the coordinator test and new
  `RobotSchedulerExceptionBoundaryTest.java`; no other test file was changed.
- `compileJava`: `PASS` under WPILib Java 17. `compileTestJava`: `FAIL / ENVIRONMENT
  HOLD` because Windows Gradle/Javac test compilation cannot resolve the main
  output on the existing classpath; Gradle, vendordeps, and the project source
  were not changed to work around it. No focused/full test or clean-build result
  is claimed for this implementation.
- Simulation and real-robot re-verification: `NOT RERUN / USER GATE`.

A01_L08 remains `REOPENED / IN_PROGRESS / EDITABLE`; re-freeze remains `HOLD`.
V00_L02 remains `SUSPENDED / READ-ONLY`. No Git/GitHub operation was performed.

## Final Documentation Closure and Re-Freeze — 2026-08-26

The implementation record immediately above is historical. Later verification
passed `compileJava`, `compileTestJava`,
`RobotSchedulerExceptionBoundaryTest`, the full 449/449 suite, and the clean
build. User-verified Simulation passed Blue and Red ONE_METER_PATH, terminal
hold, joystick blocking while Autonomous remained enabled, Autonomous ->
Disabled -> Teleop recovery, SAFE_STOP, and no-restart behavior. The final
user-owned real-robot retest passed ONE_METER_PATH at approximately one metre,
Blue behavior, the previously verified Red behavior, preparation/recovery,
terminal ownership, no automatic restart, and Teleop recovery.

One brief steer event near completion is classified as:

- `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`
- `ACCEPTED FOR CURRENT LESSON`
- `DEFERRED FOR FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`

Its exact physical root cause is not fully proven. No sustained oscillation or
uncontrolled drivetrain motion was observed, and no PID/feedforward, CANcoder,
CTRE, PathPlanner, Swerve, or asset change is justified. The earlier
approximately 5.9 ms desktop periodic sample is not roboRIO proof; no blocking
CAN wait or production performance defect was found, so it is not a closure
blocker.

Architecture review and documentation closure are `PASS`. The Frozen Backbone,
Frozen Interface Contract, scheduler-native child lifecycle, deterministic
terminal Swerve ownership, SAFE_STOP, Teleop gate, no automatic restart,
A01_L04 alliance-transform authority, `shouldFlipPath = false`, and
`preventFlipping = true` are preserved. A01_L08 is `COMPLETE / FROZEN /
READ-ONLY`. V00_L02 remains `SUSPENDED / READ-ONLY / UNMODIFIED` and is not
automatically resumed. Git/GitHub remains user-owned.

Ready for user publication: `YES`; publication itself remains user-owned.
