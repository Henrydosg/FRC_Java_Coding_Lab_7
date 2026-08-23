# A01_L08 - Autonomous Routine Selection and Safe Composition

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`
- Title: `A01_L08 - Autonomous Routine Selection and Safe Composition`
- Previous lesson: `A01_L07_AutoBuilderContractIntegration - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- User inheritance: `PASS` - the user copied and renamed frozen L07.
- Baseline Build: `PASS / USER-CONFIRMED` - compileJava, compileTestJava,
  tests, and clean build.
- Production Implementation: `PASS` - routine factory and chooser snapshot
  wiring implemented; no AutoBuilder implementation was added.
- Simulation: `PASS / USER-CONFIRMED`
- Driver Station / Glass: `PASS / USER-CONFIRMED chooser/runtime observation`
- Real Robot: `PASS / USER-CONFIRMED`
- Git Commit / Push: `NOT TESTED` - user-owned; Codex does not run Git.

## Authoritative Objective

“One concept: selecting and composing autonomous routines with explicit
cancellation, requirements, and failure behavior.”

L08 is the routine-selection boundary after frozen L07. L09 owns PathPlanner
NamedCommands and event markers; L08 does not introduce marker dispatch,
mechanism-event coordination, or competition strategy.

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

## Final Closure and Implementation Boundary

L08 is `COMPLETE / FROZEN / READ-ONLY`. The implementation remains limited to
routine selection and safe composition. The user verified the post-repair
WPILib VS Code build: `BUILD SUCCESSFUL in 1s` with `6 actionable tasks: 1
executed, 5 up-to-date`. The accepted source-complete result is 430/430 tests
with zero failures, errors, or skips; Simulation and Real Robot are also
`PASS / USER-CONFIRMED`.

The prior direct-Gradle classpath-resolution hold is superseded by that
authoritative user-owned build evidence. L08 is the frozen inheritance source
for L09, while `A01_L09` remains `NOT CREATED / NOT STARTED`. Exact endpoint
accuracy, final PID/feedforward tuning, final RobotConfig characterization,
and final physical characterization remain explicitly unclaimed.
