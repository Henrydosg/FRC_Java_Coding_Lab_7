# ADR - A01_L08 Autonomous Safety and Robustness Reopen

- Decision: `APPROVED`
- Date: `2026-08-24`
- Decision owners: Architect and User
- Change type: exceptional safety/robustness governance amendment
- Scope amendment: `APPROVED 2026-08-24 - terminal ownership and scheduler-native composition`
- Initial terminal-ownership implementation authorization: `NO` - governance
  scope only on 2026-08-24; superseded by the separately recorded final repair
  authorization and closure addendum below.

## Context and Authority

The ordinary lesson lifecycle protects every `COMPLETE` lesson as a frozen,
read-only historical snapshot and permits only one `IN_PROGRESS` lesson to be
editable. That rule remains authoritative for normal development.

New real-robot evidence obtained after A01_L08 was frozen invalidates the
assumption that its autonomous preparation and readiness lifecycle is reliably
recoverable and deterministic. The Architect and User explicitly approve a
narrow exceptional reopen of A01_L08 for safety/robustness repair. This ADR
supplements the A01 and V00 roadmap ADRs without reordering, renaming, adding,
or removing any lesson. The original L08 closure evidence remains historical
evidence that was valid when recorded; it is not erased or retroactively
reclassified.

Document A and Document B require formal architecture/change review. Document
C normally directs work into the next editable lesson. AGENTS.md has the
highest repository authority and now defines this exceptional path for a
material post-freeze defect. The exception is intentionally narrower than the
ordinary lifecycle and does not weaken frozen-lesson protection generally.

## Post-Freeze Safety / Robustness Evidence

The following is `POST-FREEZE SAFETY / ROBUSTNESS EVIDENCE`, not a feature:

- `ONE_METER_PATH` could be chooser-selected and chooser-active while the robot
  remained stationary in Autonomous Enabled.
- Autonomous Enabled and chooser-active telemetry did not prove that the path
  command passed readiness, was constructed, or was scheduled.
- Blue sometimes recovered after Robot Code restart, while Red later failed;
  restart or reboot outcomes were nondeterministic.
- Returning to Blue did not guarantee recovery, and the normal BACK then Reset
  Known Starting Pose sequence did not always recover operation.
- Readiness is one-shot, and reset/readiness lifecycle coupling is fragile.
- Pose-context validation uses a `1e-9` metre/radian tolerance despite estimator
  sensor updates after reset and heading wrap at `+/-pi`.
- Transient preparation failures can latch the adapter for the process lifetime.
- `firstFaultReason()` is not operator-visible.
- Disabled preparation actions can conflict for the Swerve scheduler
  requirement.
- Restart is not an acceptable operational recovery mechanism.

## Decision

1. Add exactly one exceptional lifecycle status: `SUSPENDED`, qualified as
   `SUSPENDED / READ-ONLY`.
2. Preserve `REOPENED` as provenance, not another generic lifecycle status.
3. Change A01_L08 from `COMPLETE / FROZEN / READ-ONLY` to Status
   `IN_PROGRESS`, Active State `REOPENED / IN_PROGRESS / EDITABLE`.
4. Change V00_L02 from `IN_PROGRESS / EDITABLE` to Status `SUSPENDED`, Active
   State `SUSPENDED / READ-ONLY`.
5. Preserve A01_L01-L07, A01_L09, and V00_L01 as
   `COMPLETE / FROZEN / READ-ONLY`.
6. A01_L08 is the repository's sole editable lesson.
7. This amendment authorizes governance registration only. It does not
   authorize repair implementation.

## SUSPENDED / READ-ONLY Definition

`SUSPENDED / READ-ONLY`:

- preserves unfinished engineering work exactly as-is;
- is neither `COMPLETE` nor `FROZEN`;
- is not editable and does not count as the active editable lesson;
- prohibits production, test, documentation, configuration, dependency,
  asset, and feature changes while suspended;
- may resume only through explicit governance approval;
- resumes from the exact preserved state unless a separately approved
  reconciliation is required; and
- is reserved for exceptional higher-priority safety or robustness work.

The lifecycle documentation changes made by this decision establish the
suspension. After registration, V00_L02 is read-only.

## Exceptional Frozen Reopen Rule

A frozen lesson may be reopened only when new post-freeze evidence identifies
a material safety, correctness, architecture, hardware-runtime, or verification
defect that invalidates a frozen assumption. Every reopen requires:

1. explicit Architect approval;
2. explicit User approval;
3. written evidence;
4. an exact repair scope;
5. preservation of the historical frozen evidence;
6. exactly one editable lesson;
7. focused and inherited regression gates;
8. Simulation verification when applicable;
9. real-robot verification when applicable;
10. an explicit re-freeze decision; and
11. no unrelated features or refactors.

## Approved Repair Scope

Only these A01_L08 areas may proceed after separate implementation approval:

1. autonomous preparation lifecycle;
2. readiness-context lifecycle;
3. physically meaningful pose/context validation;
4. wrapped heading comparison;
5. recoverable-versus-fatal fault classification;
6. recovery without restart for recoverable preparation failures;
7. Blue/Red preparation provenance and invalidation;
8. selected-routine preparation provenance and invalidation;
9. scheduler-safe Disabled preparation actions;
10. path preflight;
11. AutoBuilder prerequisite diagnostics;
12. immutable preparation observation;
13. read-only preparation telemetry;
14. an operator `Prepare Autonomous` workflow;
15. focused and inherited regression tests; and
16. required repair documentation.

## Post-Reopen Terminal-Ownership Evidence

The separately authorized preparation/readiness repair was subsequently
implemented and passed its
local compile, focused-test, full-regression, and clean-build review gates. Its
Simulation and real-robot repair gates remain user-owned. The following later
evidence is separate from that preparation/readiness history and expands the
governed repair scope without erasing any earlier result.

User real-robot observation:

- `ONE_METER_PATH` executes successfully and stops near the expected endpoint;
- one visible steer correction occurs at the terminal transition;
- the correction is not sustained oscillation; and
- Teleop otherwise operates normally.

Forensic source review established this terminal chain:

```text
PathPlanner terminal zero output
    -> SafeAutoBuilderCommand.end()
    -> SwerveSubsystem.stop()
    -> Swerve requirement release
    -> FieldRelativeTeleopDriveCommand may reacquire Swerve
       while Driver Station remains Autonomous Enabled
```

`FieldRelativeTeleopDriveCommand` has no independent production-output gate
requiring `DriverStation.isTeleopEnabled()`. Nonzero Xbox input can therefore
theoretically produce drivetrain intent after path completion while the Driver
Station remains Autonomous Enabled. This is a safety and mode-ownership defect,
not evidence authorizing steer tuning, calibration, CTRE, IO, or PathPlanner
asset changes.

The source review also established that the current custom
`PreparationLifecycleCommand` manually invokes child-command lifecycle methods.
That conflicts with the A01 scheduler-native command-composition contract,
which prohibits manual child-command lifecycle delegation.

## Terminal-Ownership Scope Amendment Decision

Architect approval: `APPROVED` for governance scope expansion only.

User approval: `APPROVED` for governance scope expansion only.

The approved architecture is Option D. After a separate implementation
authorization, the repair may:

1. add an explicit scheduler-native autonomous terminal hold that retains the
   Swerve requirement after path completion while Autonomous remains enabled;
2. make `SAFE_STOP` retain and stoppably own Swerve for the active Autonomous
   session;
3. add the minimum defensive Teleop-mode gate so controller-derived drivetrain
   intent is impossible unless `DriverStation.isTeleopEnabled()` is true;
4. replace manual `PreparationLifecycleCommand` child lifecycle delegation
   only where necessary with WPILib-native composition; and
5. add exactly one preparation lifecycle state, `HOLDING`, if required.

This amendment authorizes no implementation. The exact implementation remains
subject to a separate architecture/implementation review.

## Authorized Terminal Contract

The target lifecycle is:

```text
CONSUMED -> RUNNING -> HOLDING -> COMPLETE
```

`HOLDING` means path motion has completed, centralized Swerve stop has
occurred, the autonomous session remains active, Swerve remains required, the
default Teleop drive cannot reacquire it, and no autonomous motion restarts.

The target `ONE_METER_PATH` terminal chain is:

```text
SafeAutoBuilderCommand -> centralized stop -> terminal safety hold
    -> retain Swerve until Autonomous exits
```

`SAFE_STOP` must likewise retain and stoppably own Swerve for the active
Autonomous session and must not fall through to controller-driven default
behavior. Requirements, interruption, and composition remain scheduler-owned;
the final repair architecture may not manually call child `initialize()`,
`execute()`, or `end()` methods.

## Authorized File Boundary for a Future Implementation

Production changes may be considered only where proven necessary among:

- `AutonomousSafetyHoldCommand.java`;
- `AutonomousRoutineFactory.java`;
- `AutonomousPreparationCoordinator.java`;
- `AutonomousPreparationObservation.java`; and
- `FieldRelativeTeleopDriveCommand.java`.

Tests directly owning those behaviors may be changed only after separate
implementation authorization. `RobotContainer.java` is not authorized without
new evidence and a separate review.

## Excluded Scope

The reopen excludes new autonomous features, unrelated paths, autonomous or
drivetrain tuning, mechanism work, Vision/V00 feature work, S00, A01_L01-L07,
A01_L09, V00_L01, V00_L02, vendor-architecture changes, Frozen Backbone
changes, and unrelated refactors. Changes to `SwerveSubsystem`,
`SwerveModuleIOCTRE`, any other IO, Constants tuning/calibration, CANcoder
offsets, mechanical calibration, PathPlanner assets, Gradle, and vendordeps are
not authorized. The observed steer correction does not justify PID/feedforward
tuning or recalibration. No production or test change is authorized by this
documentation amendment.

## Preserved Architecture

The Frozen Backbone and Frozen Interface Contract remain unchanged.
`RobotContainer` remains the composition root only. L04 remains the sole
alliance-transform owner. Swerve requirement ownership and centralized
`SwerveSubsystem.stop()` remain authoritative. Telemetry remains read-only.
The repair must fail closed and may not weaken safety or architecture tests.

## Rollback Contract

If architecture review, tests, Simulation, or real-robot verification fails,
A01_L08 remains incomplete and must not be re-frozen. The original frozen
state remains preserved in repository history; evidence must not be destroyed.
V00_L02 does not resume automatically. The Architect and User must explicitly
choose whether to revise, revert, or abandon the repair.

## A01_L08 Re-Freeze Gates

Historical PASS results do not satisfy the reopened repair gates. Re-freeze
requires new evidence for every applicable gate:

1. Governance scope authorized.
2. Architecture review PASS.
3. No manual child-command lifecycle delegation in the repaired path.
4. Terminal Swerve ownership deterministic.
5. `SAFE_STOP` retains safe ownership.
6. Teleop command cannot produce drivetrain intent outside Teleop.
7. Prepare cannot cancel running auto or terminal hold.
8. No automatic autonomous restart.
9. Blue Simulation PASS.
10. Red Simulation PASS.
11. `SAFE_STOP` Simulation PASS.
12. With Autonomous kept Enabled after path completion, drivetrain remains
    stopped.
13. Simulated nonzero Xbox input after completion leaves the drivetrain
    stopped.
14. Autonomous-to-Disabled-to-Teleop transition PASS.
15. Teleop resumes normally.
16. Recoverable preparation failure and reprepare PASS.
17. Focused tests PASS.
18. Full inherited regression PASS.
19. Clean build PASS.
20. User real-robot verification PASS.
21. No-restart repeated autonomous PASS.
22. Blue-to-Red no-restart PASS.
23. Changed-file audit PASS.
24. Documentation reconciliation PASS.
25. Explicit Architect/User re-freeze approval.

User-owned Git publication is separate and is not performed by Codex.

## V00_L02 Resume Gates

V00_L02 may resume only after A01_L08 is complete and explicitly re-frozen and
after a separate governance reconciliation confirms:

- the L08 repair is accepted and published by the User;
- V00_L02 engineering work remains intact;
- V00 assumptions remain valid;
- no V00_L02 file changed while suspended; and
- any required downstream reconciliation is explicitly approved.

The L08 repair does not automatically propagate through frozen A01_L09,
V00_L01, or the already inherited V00_L02 project. Before later autonomous
operation from the V00 lineage, resume reconciliation must explicitly decide
whether and how to forward-port the accepted repair. No forward port may occur
while V00_L02 is suspended.

## Initial Final Decision - 2026-08-24

`APPROVED - GOVERNANCE SCOPE EXPANSION ONLY`. At that time A01_L08 remained
`REOPENED / IN_PROGRESS / EDITABLE`; V00_L02 remains
`SUSPENDED / READ-ONLY`. Option D, the target `HOLDING` lifecycle, defensive
Teleop gate, `SAFE_STOP` ownership repair, and scheduler-native composition
repair are authorized repair areas. Production and test implementation remained
unauthorized pending a separate implementation review and approval.

## Final Re-Freeze Audit Hold Addendum - 2026-08-25

The Architect and User separately authorized implementation within the exact
Option D boundary recorded above. The preparation/readiness repair and terminal
ownership repair were implemented in A01_L08 only. The final user-owned runtime
and automated evidence was supplied, but the re-freeze audit is `HOLD` because
the active `AutoBuilderContractAdapter.SafeAutoBuilderCommand` still manually
invokes its delegate's `initialize()`, `execute()`, `isFinished()`, and `end()`
callbacks. This conflicts with the approved scheduler-native no-manual-lifecycle
gate. The original reopen evidence and governance-only decision remain intact.

### Final Architecture Review

`HOLD`. RobotContainer remains the composition root; SwerveSubsystem remains the
sole drivetrain, stop, and localization owner; the Frozen Backbone and Frozen
Interface Contract are preserved; A01_L04 remains the sole alliance-transform
owner; `shouldFlipPath=false` and `preventFlipping=true` remain unchanged; the
active autonomous composition owns Swerve through terminal HOLDING and SAFE_STOP;
the Teleop command produces no controller-derived drivetrain intent outside
Teleop; WPILib-native composition owns the repaired preparation lifecycle; and
automatic restart is forbidden and not observed. The active adapter wrapper's
manual child lifecycle delegation fails the re-freeze gate.

### Final Automated Evidence

- `compileJava`: PASS.
- `compileTestJava`: PASS.
- Focused terminal/Teleop tests: `32/32 PASS`.
- Preparation regression: `12/12 PASS`.
- Autonomous scheduling regression: `29/29 PASS`.
- Full suite: `442/442 PASS`, zero failures/errors.
- Clean build: `BUILD SUCCESSFUL in 29s`, zero failures/errors.

### Final User-Owned Runtime Evidence

Simulation is `USER VERIFIED / PASS` for Blue and Red `ONE_METER_PATH`,
`Prepare -> READY`, recoverable `RESET_REJECTED -> second Prepare READY`
without Robot Code restart, final pose approximately `1.005 m`, terminal hold
while Autonomous remains Enabled, simulated joystick input producing no
drivetrain movement after path completion, Autonomous -> Disabled -> Teleop,
normal Teleop recovery, SAFE_STOP, no-restart Blue/Red/recovery behavior, and no
automatic autonomous restart.

Real Robot is `USER VERIFIED / PASS` for the repaired code deployment, Teleop
sanity, visible AutonomousPreparation telemetry, Blue and Red Prepare -> READY
and execution, repeated Blue execution, SAFE_STOP, no-restart recovery and
alliance transition, Disable/mode-loss stop, no automatic restart, normal
Teleop after Autonomous, and absence of the previously observed terminal
steering twitch after the repair. No PID/feedforward change, CANcoder
recalibration, or hardware defect was established.

### Final Scope and Status

The changed-file audit is `PASS`: only the approved A01_L08 repair production
and test files plus reconciled documentation changed; configuration,
PathPlanner assets, vendordeps, frozen predecessor/successor lessons, and V00
files are unmodified. Documentation reconciliation is `HOLD` because the
architecture gate above is unresolved.

`A01_L08 FINAL VERDICT: HOLD`.

`A01_L08 FINAL STATUS: REOPENED / IN_PROGRESS / EDITABLE`.

V00_L02 remains `SUSPENDED / READ-ONLY`. No forward-port or resume was
performed. User Git publication remains separate and user-owned.

## Scheduler Exception Boundary Governance Amendment — 2026-08-25

### Reason and New Evidence

The final architecture hold identified a second, narrower defect in the active
AutoBuilder adapter. `SafeAutoBuilderCommand` manually delegates its child
command's `initialize()`, `execute()`, `isFinished()`, and `end()` callbacks.
That delegation violates the A01 scheduler-native composition contract.

Source review further established that a single adapter-only replacement cannot
preserve equivalent fail-closed exception safety. WPILib 2026.2.1
`CommandScheduler` has no supported project lifecycle exception handler around
child callbacks, PathPlanner 2026.1.2 has no suitable lifecycle boundary for the
project's fault semantics, and `finallyDo`/decorators do not catch arbitrary
lifecycle exceptions. The fault boundary therefore must be broader than the
adapter while remaining narrowly scoped to this A01_L08 repair.

### Governance Decision

Architect approval: `APPROVED` for governance scope expansion only.

User approval: `APPROVED` for governance scope expansion only.

The approved design is Option F:

1. scheduler-native AutoBuilder command composition;
2. existing narrow callback and output protections;
3. a Robot-level scheduler `RuntimeException` boundary;
4. a coordinator/adapter fault bridge;
5. centralized Swerve stop;
6. immutable `FAULTED` observation; and
7. no automatic autonomous restart.

This amendment records architecture and change-control scope only. Production and
test implementation remain unauthorized until a separate implementation action is
approved.

### Exact Future Implementation Boundary

After separate implementation authorization, production changes for this scheduler
exception-boundary repair may be made only in:

1. `src/main/java/frc/robot/commands/AutoBuilderContractAdapter.java`;
2. `src/main/java/frc/robot/commands/AutonomousPreparationCoordinator.java`;
3. `src/main/java/frc/robot/RobotContainer.java`; and
4. `src/main/java/frc/robot/Robot.java`.

The directly authorized test scope is limited to:

1. `src/test/java/frc/robot/RobotContainerPathPlannerIntegrationTest.java`;
2. `src/test/java/frc/robot/commands/AutonomousRoutineFactoryTest.java`;
3. `src/test/java/frc/robot/commands/AutonomousPreparationCoordinatorTest.java`;
4. new `src/test/java/frc/robot/RobotSchedulerExceptionBoundaryTest.java`.

Existing related safety tests may be rerun unchanged. No other production or test
file is authorized by this amendment. The earlier terminal-ownership repair
evidence and its historical file list remain preserved; they are not expanded by
this entry.

### Required Fault and Ownership Contract

The future repair must preserve the following safety equivalence:

```text
unexpected autonomous scheduler/lifecycle exception
    -> Swerve fail closed
    -> centralized stop
    -> future autonomous output rejected
    -> first fatal reason latched
    -> immutable FAULTED observation published
    -> no automatic restart
```

The terminal autonomous contract remains
`CONSUMED -> RUNNING -> HOLDING -> COMPLETE`. While `HOLDING`, path motion is
complete, centralized Swerve stop has occurred, the Autonomous session remains
active, Swerve remains required, default Teleop drive cannot reacquire it, and no
autonomous motion restarts.

Diagnostic equivalence is partial and acceptable: the broader scheduler boundary
may replace the former narrow fault label, but the original exception type and
message must be retained where possible. Native cancellation is best-effort only
where supported by the source; Swerve must be stopped independently through the
approved bridge. Autonomous restart remains blocked for the process/session after
a fatal scheduler failure. Teleop recovery requires an explicit mode transition
and successful safe scheduler recovery.

### Additional Re-Freeze Gates

In addition to the historical and terminal-ownership gates above, this amendment
requires new evidence that:

1. manual child lifecycle delegation is removed;
2. the scheduler owns the child lifecycle;
3. the Robot scheduler `RuntimeException` behavior is implemented;
4. initialize failure fails closed;
5. execute failure fails closed;
6. isFinished failure fails closed;
7. end/finalization failure fails closed;
8. output failure fails closed;
9. the first fatal reason is preserved;
10. immutable `FAULTED` is published;
11. no autonomous restart occurs;
12. terminal `HOLDING` is preserved where applicable;
13. `SAFE_STOP` remains fail-closed and owned;
14. the defensive Teleop output gate remains active;
15. focused scheduler-exception tests pass;
16. the full inherited regression passes;
17. a clean build passes;
18. Simulation re-verification passes;
19. user real-robot re-verification passes;
20. the changed-file audit passes;
21. documentation closure passes; and
22. explicit Architect/User re-freeze approval is recorded.

### Scope Exclusions and Current State

Changes to `SwerveSubsystem`, CTRE or other IO, CANcoder offsets, calibration,
PID/feedforward, Constants tuning, PathPlanner assets, RobotConfig, Gradle,
vendordeps, frozen A01_L01-L07/A01_L09, V00_L01, or suspended V00_L02 remain
unauthorized. RobotContainer is authorized only for the narrow composition-root
fault bridge described above; it may not own business-fault classification,
Swerve stop policy, fault state, scheduler lifecycle, or vendor APIs.

A01_L08 remains `REOPENED / IN_PROGRESS / EDITABLE` and its final architecture
status remains `HOLD`. V00_L02 remains `SUSPENDED / READ-ONLY`. A01_L08 remains
the sole editable lesson. No implementation, build, Simulation, Driver Station,
Glass, or real-robot result is created by this governance amendment.

## Final Scheduler-Native Exception Boundary Implementation Authorization — 2026-08-25

The Architect/User final implementation action separately authorized the exact
repair boundary above. A01_L08 remains `REOPENED / IN_PROGRESS / EDITABLE` and
V00_L02 remains `SUSPENDED / READ-ONLY`.

The authorized implementation removes `SafeAutoBuilderCommand` manual child
lifecycle delegation, composes `AutoBuilder.followPath(executionPath)` with
WPILib-native scheduler commands, adds the minimum coordinator scheduler-fatal
entry, adds the narrow RobotContainer safety bridge, and adds the Robot-level
scheduler `RuntimeException` boundary. The four authorized production files are
the only production implementation boundary. The named focused tests and the
new scheduler exception-boundary test are the only authorized test boundary.

The local implementation preserves the Frozen Backbone, centralized
`SwerveSubsystem.stop()` ownership, the `CONSUMED -> RUNNING -> HOLDING ->
COMPLETE` terminal lifecycle, SAFE_STOP ownership, the defensive Teleop gate,
the PathPlanner flipping contract, and no automatic autonomous restart.
Diagnostic equivalence is partial: the first fatal diagnostic retains the
exception type and message where available.

This authorization does not re-freeze A01_L08. The local Java production
compile passed under WPILib Java 17. Test compilation remains blocked by the
existing Windows Gradle/Javac classpath-resolution failure; Simulation and
real-robot re-verification remain user gates and were not rerun by Codex.

## Final Documentation Closure and Re-Freeze — 2026-08-26

The preceding authorization and environment HOLD remain preserved as historical
evidence. Later authoritative verification established `compileJava` PASS,
`compileTestJava` PASS, `RobotSchedulerExceptionBoundaryTest` PASS, 449/449 full
suite PASS, and clean build PASS. The user then supplied Simulation PASS and
final real-robot retest PASS for ONE_METER_PATH, Blue/Red behavior, preparation
and recovery, terminal ownership, SAFE_STOP, Teleop recovery, and no automatic
autonomous restart.

The real robot exhibited a brief one-time steer event near path completion. It
is recorded exactly as `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`, `ACCEPTED FOR
CURRENT LESSON`, and `DEFERRED FOR FUTURE DRIVETRAIN / PATH-FOLLOWING TUNING`.
The exact physical root cause is not fully proven. No sustained oscillation,
ownership gap, uncontrolled drivetrain translation, PID instability, CANcoder
defect, hardware defect, PathPlanner defect, or Swerve architecture defect was
verified; therefore no PID/feedforward, CANcoder, CTRE, PathPlanner, Swerve,
configuration, or asset change is authorized by this closure.

The approximately 5.9 ms `SwerveSubsystem.periodic()` observation was one
desktop sample, is not proven representative of roboRIO timing, revealed no
blocking CAN wait, and does not prove a production performance defect. Optional
future target-hardware measurement is outside this closure.

Final decision: `APPROVED`. The Frozen Backbone and Frozen Interface Contract
remain preserved; RobotContainer remains the composition root; SwerveSubsystem
remains the drivetrain/output/localization owner; the PathPlanner child
lifecycle is scheduler-owned; terminal ownership, SAFE_STOP, the Teleop gate,
no automatic restart, A01_L04 alliance-transform ownership,
`shouldFlipPath = false`, and `preventFlipping = true` remain intact. A01_L08 is
re-frozen as `COMPLETE / FROZEN / READ-ONLY`. V00_L02 remains `SUSPENDED /
READ-ONLY / UNMODIFIED`; resumption requires a separate governance decision.
