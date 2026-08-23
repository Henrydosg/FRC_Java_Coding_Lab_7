# A01_L08 - Autonomous Routine Selection and Safe Composition - Plan and Design Lock

## Activation State

- Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`.
- Title: `A01_L08 - Autonomous Routine Selection and Safe Composition`.
- Previous lesson: `A01_L07_AutoBuilderContractIntegration - COMPLETE / FROZEN / READ-ONLY`.
- Status: `COMPLETE / FROZEN / READ-ONLY`.
- User-owned inheritance: `PASS`.
- User baseline: `PASS` - compileJava, compileTestJava, tests, and clean build.
- Production implementation: `PASS` within the approved two-file boundary.
- Architecture audit and design lock: `PASS`.
- compileJava: `PASS` under bundled WPILib JDK 17.
- Initial user evidence: `430 tests / 419 passed / 11 failed`; all 11 were
  independently reproduced and classified before repair.
- Minimal test-contract repair: `PASS`; ten inherited semantic migrations and
  one focused Swerve-requirement fixture mismatch were corrected without
  changing production behavior or weakening assertions.
- Focused and full source-complete JUnit verification: `PASS` - 430/430 with
  zero failures, errors, or skips.
- Post-repair clean build: `PASS - USER VERIFIED` - WPILib VS Code displayed
  `BUILD SUCCESSFUL in 1s` and `6 actionable tasks: 1 executed, 5 up-to-date`.
  This supersedes the prior direct-Gradle classpath-resolution environment
  hold.
- Simulation: `PASS / USER-CONFIRMED`.
- Driver Station / Glass: `PASS / USER-CONFIRMED chooser/runtime observation`.
- Real Robot: `PASS / USER-CONFIRMED`.

The user-owned inheritance preserved `.wpilib`, including team 10951, project
year 2026, and Java language. The user repaired a settings.gradle encoding
failure before supplying the accepted baseline; this history is preserved.

## One New Concept

“One concept: selecting and composing autonomous routines with explicit
cancellation, requirements, and failure behavior.”

Path, trajectory, path-following command, autonomous routine, routine selection,
and safe composition remain distinct concepts. L08 selects scheduler-owned
routine commands; L09 owns NamedCommands and event markers.

## Minimum Routine Set

1. `SAFE_STOP` - default bounded non-driving safety hold.
2. `ONE_METER_PATH` - fresh command for the existing known one-meter AutoBuilder
   path through the frozen L07 adapter.

This is the minimum set required to demonstrate meaningful selection and the
ADR's at-least-two-routine Simulation boundary. No scoring, mechanisms, or
competition strategy is added.

## Selection and Composition Design

`RobotContainer` constructs and publishes one `SendableChooser` containing
immutable routine identities. It sets `SAFE_STOP` as the default and adds
`ONE_METER_PATH`. `Robot.autonomousInit()` already calls
`getAutonomousCommand()`, so L08 samples the chooser once there and delegates to
a fresh `AutonomousRoutineFactory` command. Selection changes after scheduling
cannot replace or restart the current autonomous command.

The factory creates fresh commands rather than storing reusable Command
instances. Every selection first consumes the inherited accepted start context
once. A missing context or invalid selection returns `SAFE_STOP`; only a valid
context can create `ONE_METER_PATH`.

## Locked Safety and Ownership Contracts

- L04 owns the exactly-one alliance transform.
- Frozen L07 settings remain `shouldFlipPath = false` and
  `preventFlipping = true`.
- `SwerveSubsystem` owns drivetrain requirements and centralized `stop()`.
- Scheduler-managed commands, not manual locks, enforce requirements.
- Normal completion, interruption, cancellation, Disable/mode loss, invalid
  readiness/alliance, missing path, construction failure, and command faults
  stop safely.
- Null/unknown chooser values never select a driving routine.
- No automatic restart is permitted.

## Implemented Production Delta

- Add `src/main/java/frc/robot/commands/AutonomousRoutineFactory.java`.
- Modify `src/main/java/frc/robot/RobotContainer.java` for chooser ownership,
  SmartDashboard publication, one-time snapshot, and factory delegation.
- Leave `Robot.java` unchanged; it already samples at autonomous initialization.
- Leave Constants, IO, SwerveSubsystem, RobotConfig, assets, CTRE/CAN,
  telemetry, and frozen predecessors unchanged.

## Implemented Test Delta

- Add `AutonomousRoutineFactoryTest` for identities, fresh instances, fallback,
  readiness, requirements, and stop behavior.
- Add `RobotContainerAutonomousRoutineSelectionTest` for chooser/default,
  snapshot semantics, in-run selection changes, no restart, Blue/Red transform,
  and inherited L07 settings.
- Preserve inherited L01-L07 regression unchanged.

## Verification Reconciliation

### Simulation

The user verified chooser visibility/default, explicit SAFE_STOP and
ONE_METER_PATH execution, Blue/Red behavior, exactly-one L04 transform,
Disable/mode-loss stop, no restart without fresh readiness, and safe fallback.
The UI did not permit changing to ONE_METER_PATH while Autonomous was already
enabled; no manual runtime-change result is claimed. Selection snapshot behavior
remains covered by automated tests and implementation review.

### Real Robot

The user confirmed A01_L08 real-robot verification PASS. No endpoint precision,
final PID/feedforward tuning, physical-model characterization, or
competition-readiness claim is part of this lesson.

## Final Closure Boundary

L08 is `COMPLETE / FROZEN / READ-ONLY` and is the frozen inheritance source for
A01_L09. No production Java outside the approved factory and RobotContainer
wiring, no test outside the focused L08 additions, and no asset/configuration
change was made during documentation closure. No runtime/hardware operation was
performed by Codex; the user's supplied build, Simulation, and Real Robot
evidence is recorded above. `A01_L09` remains `NOT CREATED / NOT STARTED`.
The inherited L08 test fixtures were minimally migrated only where they
expressed the obsolete pre-L08 implicit-selection or one-shot-stop contract;
no assertions were weakened and no tests were disabled or removed.

Exact endpoint accuracy, final PID/feedforward tuning, and final physical
characterization remain explicitly unclaimed.
