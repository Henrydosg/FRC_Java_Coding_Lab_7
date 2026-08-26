# A01_L08 - Autonomous Routine Selection and Safe Composition - Plan and Design Lock

## Activation State

- Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`.
- Title: `A01_L08 - Autonomous Routine Selection and Safe Composition`.
- Previous lesson: `A01_L07_AutoBuilderContractIntegration - COMPLETE / FROZEN / READ-ONLY`.
- Status: `COMPLETE / FROZEN / READ-ONLY` after final re-freeze on 2026-08-26.
- Previous status: `COMPLETE / FROZEN / READ-ONLY` - preserved historically.
- Governance reopen: `APPROVED` by Architect and User.
- Preparation/readiness repair: `AUTHORIZED / IMPLEMENTED / LOCAL REVIEW GATES PASS`.
- Terminal-ownership scope amendment: `APPROVED`.
- Terminal-ownership implementation: `AUTHORIZED / IMPLEMENTED / ARCHITECTURE HOLD`.
- Single editable lesson during repair: `A01_L08`; V00_L02 is
  `SUSPENDED / READ-ONLY`.
- User-owned inheritance: `PASS`.
- User baseline: `PASS` - compileJava, compileTestJava, tests, and clean build.
- Production implementation: `PASS` within the approved repair boundary.
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
- Simulation: `PASS / USER VERIFIED / FINAL REPAIR EVIDENCE`.
- Driver Station / Glass: `PASS / USER VERIFIED`.
- Real Robot: `PASS / USER VERIFIED / FINAL REPAIR EVIDENCE`.

The original closure evidence remains preserved as historical evidence. The
final repair evidence and re-freeze decision are recorded below.

## Approved Repair Planning Boundary

The separately approved preparation/readiness repair could address only:
autonomous preparation and readiness-context lifecycle; physically meaningful
pose/context validation with wrapped heading comparison; recoverable versus
fatal fault classification and recovery without restart; Blue/Red and selected
routine preparation provenance; scheduler-safe Disabled preparation; path
preflight and AutoBuilder prerequisite diagnostics; an immutable preparation
observation with read-only telemetry; an operator Prepare Autonomous workflow;
focused/inherited regression tests; and required documentation.

New autonomous features or paths, tuning, mechanisms, Vision/V00 work, S00,
A01_L01-L07, A01_L09, vendor architecture, Frozen Backbone changes, and
unrelated refactors remain excluded. That earlier implementation authorization
applied only to the preparation/readiness repair boundary and does not
authorize the terminal-ownership amendment.

The user-owned inheritance preserved `.wpilib`, including team 10951, project
year 2026, and Java language. The user repaired a settings.gradle encoding
failure before supplying the accepted baseline; this history is preserved.

## Approved Terminal-Ownership Amendment

Later user evidence records successful one-metre path execution, a stop near
the expected endpoint, and one visible terminal steer correction without
sustained oscillation. Source review established that path completion reaches
centralized stop and then releases Swerve, allowing the default
`FieldRelativeTeleopDriveCommand` to reacquire it while Autonomous may still be
enabled. That command has no independent Teleop-enabled production-output gate.
The current custom preparation wrapper also manually delegates child lifecycle
methods, conflicting with the A01 scheduler-native composition contract.

Architect and User approved Option D, followed by explicit implementation authorization:

1. append or compose an explicit scheduler-native autonomous terminal hold
   that retains Swerve until Autonomous exits;
2. make SAFE_STOP retain and stoppably own Swerve for the active Autonomous
   session;
3. defensively prevent controller-derived drivetrain intent unless
   `DriverStation.isTeleopEnabled()` is true;
4. replace the affected manual child lifecycle delegation with WPILib-native
   composition; and
5. add exactly one `HOLDING` lifecycle state if required.

The implemented target lifecycle is
`CONSUMED -> RUNNING -> HOLDING -> COMPLETE`. The production delta is limited
to `AutonomousSafetyHoldCommand.java`,
`AutonomousRoutineFactory.java`, `AutonomousPreparationCoordinator.java`,
`AutonomousPreparationObservation.java`, and
`FieldRelativeTeleopDriveCommand.java`. `SwerveSubsystem`, CTRE/other IO,
PID/feedforward, calibration, CANcoder offsets, PathPlanner assets, Gradle,
vendordeps, and RobotContainer absent separate review remain excluded.

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

## Historical Closure and Re-Freeze Boundary

L08's original `COMPLETE / FROZEN / READ-ONLY` closure remains preserved. L08
was temporarily `REOPENED / IN_PROGRESS / EDITABLE` for the approved repair.
A01_L09 is a separate
frozen successor and is not modified. No production Java outside the approved factory and RobotContainer
wiring, no test outside the focused L08 additions, and no asset/configuration
change was made during documentation closure. No runtime/hardware operation was
performed by Codex; the user's supplied build, Simulation, and Real Robot
evidence is recorded above and in the final closure section. The reopened repair
implementation is complete.
The inherited L08 test fixtures were minimally migrated only where they
expressed the obsolete pre-L08 implicit-selection or one-shot-stop contract;
no assertions were weakened and no tests were disabled or removed.

Exact endpoint accuracy, final PID/feedforward tuning, and final physical
characterization remain explicitly unclaimed.

The final re-freeze gates required architecture and interface preservation
review, focused and inherited tests, full suite, clean build, Blue/Red/recovery
Simulation, user-owned real-robot verification, recovery without Robot Code
restart for recoverable mistakes, fail-closed fatal behavior, finalized repair
documentation, changed-file audit, and explicit Architect/User approval. All
gates passed. V00_L02 did not resume.

## Implemented Reopened Repair Plan

1. `AutonomousPreparationCoordinator` owns the monotonic attempt lifecycle,
   preparation provenance, recoverable/fatal classification, READY preview,
   atomic claim, and execution-state observation.
2. `PrepareAutonomousCommand` is the sole production operator action. It starts
   heading/context validation in Disabled, waits one scheduler refresh, then
   resets and preflights. It requires Swerve and cannot run while Enabled.
3. `AutoBuilderContractAdapter` exposes typed preflight/creation results,
   validates the current pose with `0.03 m` and `2.0 degree` provisional
   tolerances using wrapped heading, preserves first fatal reason, and keeps
   transient prerequisites recoverable.
4. `AutonomousRoutineFactory` implements Option 3: preview, fresh construction,
   atomic claim, lifecycle wrap. SAFE_STOP does not consume driving READY.
5. Active driving commands use `kCancelIncoming`; Disable/mode loss terminate
   through centralized Swerve stop with no automatic restart.
6. Immutable autonomous-preparation observations flow to a read-only telemetry
   facade; RobotContainer remains construction/injection/binding only.
7. Local verification: compileJava PASS, compileTestJava PASS, focused and
   integration tests 45/45 PASS, full suite 445/445 PASS, clean build PASS.
8. Historical remaining gates: Architect review, user Simulation, user real
   robot, documentation/evidence reconciliation, and explicit re-freeze. These
   gates are complete in the final closure below.

## Implemented Terminal-Ownership Repair Plan

1. `AutonomousSafetyHoldCommand` is session-long rather than timer-bounded: it
   owns Swerve, stops on initialize/end, submits no motion, rejects incoming
   requirement conflicts, and finishes when Autonomous Enabled ends.
2. `AutonomousRoutineFactory` creates a fresh hold for every SAFE_STOP/fallback
   and appends a fresh hold to every accepted ONE_METER_PATH command.
3. `AutonomousPreparationCoordinator` uses only WPILib-native sequence and
   decorators for RUNNING, path execution, HOLDING, terminal ownership, final
   classification, and the Autonomous-session lifetime guard.
4. `AutonomousPreparationObservation` adds exactly one state, `HOLDING`.
5. `FieldRelativeTeleopDriveCommand` preserves normal Teleop behavior and
   stops/returns before controller-derived work in every non-Teleop mode.
6. Local verification: compileJava PASS, compileTestJava PASS, focused tests
   32/32 PASS, preparation regression 12/12 PASS, autonomous scheduling 29/29
   PASS, full suite 442/442 PASS, and clean build PASS.
7. No tuning, calibration, SwerveSubsystem, CTRE/IO, RobotContainer,
   AutoBuilder adapter, PathPlanner asset, Gradle, vendordep, frozen lesson, or
   downstream change was made.
8. Historical remaining gates: Architect review, user Simulation, user real
   robot, and explicit re-freeze. Runtime and automated gates are complete, but
   the architecture gate is HOLD because the active AutoBuilder adapter still
   manually delegates child lifecycle callbacks. Later forward-port
   reconciliation remains separately required and was not performed.

## Final Closure Decision - 2026-08-25

The final user-owned Simulation evidence passed Blue and Red execution, SAFE_STOP,
Prepare -> READY, recoverable reset rejection and reprepare without restart,
terminal ownership while Autonomous remained Enabled, no simulated joystick
movement after path completion, Autonomous -> Disabled -> Teleop, normal Teleop
recovery, no-restart recovery, and no automatic restart. The final pose was
approximately `1.005 m` in the verified Blue run.

The final user-owned Real Robot evidence passed repaired-code deployment, Teleop
sanity, preparation telemetry, Blue and Red Prepare -> READY and execution,
repeat Blue execution, SAFE_STOP, Blue -> Red transition, recoverable
preparation without restart, Disable/mode-loss stop, no automatic restart, and
normal Teleop recovery. Steering twitch was present before repair and absent
after repair as a user observation consistent with the ownership repair. No
PID/FF change, CANcoder recalibration, or hardware defect was established.

Final changed-file audit: `PASS`; final architecture review: `HOLD` because the
active AutoBuilder adapter still manually delegates child lifecycle callbacks.
Configuration,
PathPlanner assets, vendordeps, frozen lessons, suspended V00_L02, and generated
artifacts are excluded or unmodified. Documentation closure and transition
guide: `HOLD`. A01_L08 final verdict: `HOLD`; final status:
`REOPENED / IN_PROGRESS / EDITABLE`. Git remains user-owned.

## Final Scheduler-Native Exception Boundary Implementation — 2026-08-25

The final Architect/User action authorized the exact scheduler-native exception
boundary repair. This does not re-freeze the lesson.

1. Removed `SafeAutoBuilderCommand` manual child lifecycle delegation.
2. Composed `AutoBuilder.followPath(executionPath)` with WPILib-native
   scheduler-owned timeout, mode-loss, race, and final-stop behavior.
3. Added the minimum coordinator scheduler-fatal entry, the narrow
   `RobotContainer` safety bridge, and the Robot-level scheduler exception
   boundary.
4. Preserved centralized Swerve stop, first-fault latching, immutable
   `FAULTED`, no autonomous restart, terminal `HOLDING`, SAFE_STOP ownership,
   the Teleop output gate, and the PathPlanner/alliance-transform contracts.

Implementation production scope was limited to the four authorized files:
`AutoBuilderContractAdapter.java`, `AutonomousPreparationCoordinator.java`,
`RobotContainer.java`, and `Robot.java`. Implementation test scope changed only
`AutonomousPreparationCoordinatorTest.java` and added
`RobotSchedulerExceptionBoundaryTest.java`.

`compileJava` passed with WPILib Java 17. `compileTestJava` remains an
environment hold because Windows Gradle/Javac test compilation cannot resolve
the project main output on the existing classpath, including in a short-path
copy. No Gradle workaround was introduced. Focused tests, full suite, and clean
build are therefore not claimed. Simulation, Driver Station / Glass, and real
robot verification remain user gates and were not rerun.

## Final Plan Disposition — 2026-08-26

The environment HOLD immediately above is retained as historical evidence.
Later verification passed `compileJava`, `compileTestJava`,
`RobotSchedulerExceptionBoundaryTest`, the 449/449 full suite, and clean build.
User-owned Simulation and final real-robot retests passed. The accepted
remaining observation is a `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`, accepted
for this lesson and deferred for future drivetrain/path-following tuning because
its exact root cause is not fully proven and no current production change is
justified.

All authorized repair and verification objectives are complete. Architecture,
automated verification, Simulation, real-robot verification, changed-scope
review, transition-guide finalization, and documentation closure are `PASS`.
A01_L08 is `COMPLETE / FROZEN / READ-ONLY`. Historical HOLD records remain the
chronology of the reopen and do not describe the final state. V00_L02 remains
`SUSPENDED / READ-ONLY`; no resume or forward-port is authorized here.
