# A01_L08 - Autonomous Routine Selection and Safe Composition - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY` - re-frozen 2026-08-26
Previous status: `COMPLETE / FROZEN / READ-ONLY` - historical evidence preserved
Previous lesson: `A01_L07_AutoBuilderContractIntegration - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Exceptional Reopen Governance

- [x] Architect approval recorded.
- [x] User approval recorded.
- [x] Post-freeze safety/robustness evidence recorded in the supplemental ADR.
- [x] Exact repair and excluded scope recorded.
- [x] Original frozen closure evidence preserved as historical evidence.
- [x] A01_L08 established as the sole editable lesson.
- [x] V00_L02 established as SUSPENDED / READ-ONLY with work preserved.
- [x] A01_L01-L07, A01_L09, and V00_L01 remain frozen/read-only.
- [x] Governance-only reopen was recorded before implementation authorization.
- [x] ChatGPT governance and repair-design review completed.
- [x] Preparation/readiness repair implementation authorized and completed locally.
- [x] Focused repair and inherited integration regression tests pass - 45/45.
- [x] Full suite and clean build pass with new evidence - 445/445 and clean
      build PASS under WPILib Java 17.
- [x] Blue, Red, and recovery Simulation pass with new evidence.
- [x] User real-robot verification proves recovery without restart and fatal
      fail-closed behavior.
- [x] Preparation/readiness repair documentation reconciled for its implementation review.
- [x] Architect/User explicitly re-freeze A01_L08.

## Terminal Ownership Governance Amendment

- [x] New real-robot terminal observation recorded without converting it into
      a tuning or calibration claim.
- [x] Source-proven Swerve requirement-release/default-command risk recorded as
      a safety and mode-ownership defect.
- [x] Manual `PreparationLifecycleCommand` child lifecycle delegation recorded
      as conflicting with the A01 scheduler-native composition contract.
- [x] Architect approval recorded for Option D governance scope expansion.
- [x] User approval recorded for Option D governance scope expansion.
- [x] Target lifecycle recorded as
      `CONSUMED -> RUNNING -> HOLDING -> COMPLETE`.
- [x] Future production file boundary and hardware/tuning exclusions recorded.
- [x] Terminal-ownership implementation separately authorized and implemented.

### Updated Re-Freeze Gates

1. [x] Governance scope authorized.
2. [x] Local architecture review PASS.
3. [ ] No manual child-command lifecycle delegation in repaired path - HOLD:
   `SafeAutoBuilderCommand` still delegates child lifecycle callbacks.
4. [x] Terminal Swerve ownership deterministic in automated tests.
5. [x] SAFE_STOP retains safe ownership in automated tests.
6. [x] Teleop command cannot produce drivetrain intent outside Teleop.
7. [x] Incoming Swerve-owning commands cannot cancel running auto or hold.
8. [x] No automatic autonomous restart in automated tests.
9. [x] Blue Simulation PASS.
10. [x] Red Simulation PASS.
11. [x] SAFE_STOP Simulation PASS.
12. [x] Keep Autonomous Enabled after path completion: drivetrain remains stopped.
13. [x] Simulated nonzero Xbox after completion: drivetrain remains stopped.
14. [x] Autonomous-to-Disabled-to-Teleop transition PASS.
15. [x] Teleop resumes normally.
16. [x] Recoverable preparation failure/reprepare PASS in automated tests.
17. [x] Focused tests PASS - 32/32 terminal/Teleop, 12/12 preparation.
18. [x] Full inherited regression PASS - 442/442.
19. [x] Clean build PASS - `BUILD SUCCESSFUL in 29s`.
20. [x] User real-robot verification PASS.
21. [x] No-restart repeated autonomous PASS.
22. [x] Blue-to-Red no-restart PASS.
23. [x] Changed-file audit PASS; only approved L08 production/tests/docs changed.
24. [x] Documentation reconciliation PASS for local implementation evidence.
25. [ ] Explicit Architect/User re-freeze approval - blocked by the architecture
    gate above.

The original checked implementation and verification entries below are
historical. The final repair gates above are now also complete.

## Governance and Inheritance

- [x] AGENTS.md, README, A01 ADR, authoritative English Documents A/B/C, and
      frozen L01-L07 were reviewed.
- [x] L07 is COMPLETE / FROZEN / READ-ONLY.
- [x] User created the L07-to-L08 inheritance copy and correct directory.
- [x] `.wpilib/wpilib_preferences.json` was preserved.
- [x] `.wpilib` metadata is team 10951, project year 2026, current language Java.
- [x] `.vscode`, source, tests, vendordeps, Gradle, wrappers, docs, and assets
      are present.
- [x] The settings.gradle encoding problem and UTF-8/no-BOM repair are recorded.
- [x] User-confirmed compileJava, compileTestJava, tests, and clean build PASS.
- [x] L08 was complete and frozen as the historical inheritance source for L09.
- [x] L08 implementation remains within the approved production boundary.
- [x] Transition document `docs/A01_L07_to_A01_L08_Step_by_Step.md` exists.
- [x] English and Vietnamese L08 learning guides exist and explain the locked
      chooser, readiness, alliance, safety, and verification contracts.

## Routine Design Lock

- [x] Objective is limited to routine selection and safe composition.
- [x] PATH, TRAJECTORY, PATH-FOLLOWING COMMAND, AUTONOMOUS ROUTINE, ROUTINE
      SELECTION, and SAFE COMPOSITION are explicitly distinguished.
- [x] Minimum set is exactly `SAFE_STOP` and `ONE_METER_PATH`.
- [x] L09 NamedCommands/event markers are excluded.
- [x] `RobotContainer` owns chooser construction and publication.
- [x] `SAFE_STOP` is the default non-driving fallback.
- [x] Selection is sampled once at autonomous initialization.
- [x] Chooser entries are immutable identities/factories, not persistent commands.
- [x] Fresh command construction is required for each autonomous start.
- [x] Null/unknown selection and all invalid prerequisites fail closed to stop.

## Readiness, Alliance, and Scheduler Safety

- [x] Accepted start context and one-shot readiness consumption remain mandatory.
- [x] L04 remains the exactly-one alliance-transform owner.
- [x] `shouldFlipPath = false` remains locked.
- [x] Execution paths retain `preventFlipping = true`.
- [x] Every driving routine requires the existing `SwerveSubsystem` through the
      scheduler.
- [x] Centralized `SwerveSubsystem.stop()` remains terminal authority.
- [x] Normal completion, cancellation, interruption, Disable/mode loss, and
      faults have explicit safe termination requirements.
- [x] No automatic restart is allowed.

## Implementation and Verification Gates

- [x] Add `AutonomousRoutineFactory`.
- [x] Add chooser and snapshot wiring in `RobotContainer`.
- [x] Add focused L08 factory and selection tests.
- [x] Audit all initial failures independently before editing; 10 inherited
      semantic migrations and 1 focused requirement-fixture mismatch classified.
- [x] Run focused factory/chooser/autonomous-mode/PathPlanner tests - 32/32
      PASS after the minimal test-contract migration.
- [x] Run inherited autonomous and frozen-L07 PathPlanner regression - PASS.
- [x] Run the full source-complete JUnit suite - 430/430 PASS, zero failures,
      errors, or skips.
- [x] Post-repair WPILib VS Code clean build - PASS / USER VERIFIED:
      `BUILD SUCCESSFUL in 1s`; `6 actionable tasks: 1 executed, 5 up-to-date`.
      This supersedes the prior direct-Gradle classpath-resolution hold.
- [x] Run post-implementation `compileJava` - PASS under WPILib JDK 17.
- [x] Run post-implementation clean build - PASS / USER VERIFIED (WPILib VS
      Code build result recorded above).
- [x] Obtain user-owned Simulation evidence - PASS; runtime chooser changes
      while already Autonomous enabled were not manually possible and are not
      claimed.
- [x] Record user-owned chooser/runtime observation evidence.
- [x] Obtain user-owned Real Robot evidence - PASS.
- [x] Final documentation reconciliation recorded in the transition guide.
- [x] L08 was marked COMPLETE / FROZEN / READ-ONLY at original closure.
- [x] A01_L09 was not created at original L08 closure; it now remains
      COMPLETE / FROZEN / READ-ONLY and is outside the reopen scope.

## Exclusions

NamedCommands, event markers, mechanism coordination, vision, AprilTags,
replanning, pathfinding, competition strategy, drivetrain/Swerve IO redesign,
CTRE/CAN changes, calibration changes, PID/FF tuning, RobotConfig physical
characterization, and unnecessary framework abstraction remain excluded.

Final closure keeps exact endpoint accuracy, final PID/feedforward tuning, and
final physical characterization explicitly unclaimed. Git commit and push remain
user-owned and were not run by Codex.

## Reopened Repair Implementation Checklist

- [x] Named provisional preparation tolerances: `0.03 m`, `2.0 degrees`.
- [x] Wrapped shortest-angle comparison uses `MathUtil.angleModulus`.
- [x] Monotonic attempt IDs and full lifecycle states implemented.
- [x] READY binds alliance, routine, field, expected pose, heading attempt, and
      path identity.
- [x] SAFE_STOP does not consume driving readiness.
- [x] Fresh command construction precedes atomic readiness claim.
- [x] Recoverable preparation faults can reach READY without process restart.
- [x] Fatal faults remain process-latched and preserve the first reason.
- [x] Unified Disabled `Prepare Autonomous` workflow implemented.
- [x] One subsystem refresh occurs between heading capture and pose reset.
- [x] Independent production BACK/View and reset paths removed.
- [x] Active autonomous rejects incoming Swerve-owning preparation/reset
      commands through scheduler semantics.
- [x] Path preflight validates static construction without scheduling or hidden
      `followPath` execution.
- [x] Immutable vendor-neutral observation and read-only telemetry implemented.
- [x] RobotContainer remains the composition root only.
- [x] L04 exactly-one transform, `shouldFlipPath=false`, and
      `preventFlipping=true` preserved.
- [x] A01_L01-L07, A01_L09, V00_L01, and suspended V00_L02 unmodified.
- [x] No deployment, Git, or forward-port operation run; final Simulation and
      real-robot evidence was supplied by the User.

## Terminal-Ownership Implementation Checklist

- [x] ONE_METER_PATH retains Swerve after path completion while Autonomous is enabled.
- [x] Default Teleop command cannot acquire Swerve during terminal HOLDING.
- [x] SAFE_STOP is fresh, stop-only, readiness-neutral, and session-long.
- [x] Exactly one lifecycle state, `HOLDING`, was added.
- [x] Custom preparation child lifecycle delegation was removed.
- [x] WPILib scheduler-native sequence/decorators own child lifecycle.
- [x] `kCancelIncoming` protects the active autonomous composition.
- [x] Non-Teleop execution reads no controller sample and submits no drive intent.
- [x] Teleop-enabled behavior and Autonomous-to-Teleop recovery remain tested.
- [x] Disable/mode loss stops, releases safely, and never restarts automatically.
- [x] Centralized `SwerveSubsystem.stop()` remains authoritative.
- [x] No PID/FF, CANcoder, CTRE/IO, SwerveSubsystem, RobotConfig, or asset change.
- [x] compileJava and compileTestJava PASS under WPILib Java 17.
- [x] Focused terminal/Teleop tests PASS - 32/32.
- [x] Preparation regression PASS - 12/12.
- [x] Autonomous scheduling regression PASS - 29/29.
- [x] Full suite PASS - 442/442; zero failures or errors.
- [x] Clean build PASS - `BUILD SUCCESSFUL in 29s`, six executed tasks and one up-to-date.
- [x] Repair Simulation - USER VERIFIED / PASS.
- [x] Repair real robot - USER VERIFIED / PASS.
- [ ] Explicit Architect/User re-freeze approval - blocked by the active adapter
      manual lifecycle delegation finding.

## Final Closure Record - 2026-08-25

- Final verdict: `HOLD`.
- Final status: `REOPENED / IN_PROGRESS / EDITABLE`.
- Simulation: `USER VERIFIED / PASS` for Blue and Red execution, SAFE_STOP,
  Prepare -> READY, recoverable `RESET_REJECTED` reprepare without restart,
  approximately `1.005 m` final Blue pose, terminal ownership, no simulated
  joystick movement after path completion, mode transition, normal Teleop, and
  no automatic restart.
- Real Robot: `USER VERIFIED / PASS` for repaired deployment, Teleop sanity,
  preparation telemetry, Blue/Red preparation and execution, repeated Blue,
  SAFE_STOP, Blue -> Red without restart, recoverable preparation, mode-loss
  stop, no automatic restart, and normal Teleop recovery.
- Steering twitch: present before repair; absent after repair as user-verified
  ownership-repair validation only. No PID/FF change, CANcoder recalibration, or
  hardware defect was established.
- Architecture review: `HOLD` - active adapter manually delegates child
  lifecycle callbacks.
- Documentation closure: `HOLD`.
- Configuration, PathPlanner assets, vendordeps, unexpected files: `NONE`.
- V00_L02 remains `SUSPENDED / READ-ONLY`; no forward-port or resume occurred.
- Git commit/push: `NOT TESTED - USER OWNED`.

## Final Scheduler-Native Exception Boundary Implementation â€” 2026-08-25

- [x] Final Architect/User implementation authorization recorded.
- [x] `SafeAutoBuilderCommand` removed.
- [x] Manual child lifecycle delegation removed from the repaired production
      path; CommandScheduler owns `followPath` child lifecycle.
- [x] Robot-level scheduler `RuntimeException` boundary and narrow
      RobotContainer safety bridge implemented.
- [x] Coordinator fatal entry preserves first fatal reason, centralized stop,
      immutable `FAULTED`, and no automatic autonomous restart.
- [x] Terminal `HOLDING`, SAFE_STOP ownership, Teleop gate, and PathPlanner
      alliance-transform contracts remain source-preserved.
- [x] Production scope limited to the four authorized files.
- [x] Test scope limited to the coordinator test plus new scheduler boundary
      test.
- [x] `compileJava` passed under WPILib Java 17.
- [ ] `compileTestJava` - ENVIRONMENT HOLD: Windows Gradle/Javac classpath
      resolution still omits the project main output, including in a short-path
      copy. No Gradle workaround was introduced.
- [ ] Focused tests, full suite, and clean build - not run because test
      compilation did not pass.
- [ ] Simulation and real-robot re-verification - USER GATE / NOT RERUN.
- [ ] Explicit Architect/User re-freeze approval - remains HOLD.

The earlier closure checklist and supplied user runtime evidence remain
historical. This latest implementation record does not mark A01_L08 COMPLETE
or FROZEN, and V00_L02 remains SUSPENDED / READ-ONLY.

## Final Re-Freeze Checklist â€” 2026-08-26

- [x] Historical original closure, reopen, HOLD, and repair evidence preserved.
- [x] `compileJava` PASS.
- [x] `compileTestJava` PASS.
- [x] `RobotSchedulerExceptionBoundaryTest` PASS.
- [x] Full test suite 449/449 PASS.
- [x] Clean build PASS.
- [x] Scheduler-native PathPlanner lifecycle PASS; manual child delegation none.
- [x] Robot-level scheduler exception boundary and fail-closed behavior PASS.
- [x] Terminal `HOLDING`, SAFE_STOP, Teleop gate, and no restart PASS.
- [x] Blue/Red PathPlanner and alliance-transform contract PASS.
- [x] Simulation regression USER VERIFIED / PASS.
- [x] Real Robot retest USER VERIFIED / PASS.
- [x] Terminal event classified as `KNOWN / BOUNDED TERMINAL STEER TRANSIENT`.
- [x] Transient accepted for this lesson; future drivetrain/path tuning deferred.
- [x] Exact transient root cause recorded as not fully proven.
- [x] Desktop approximately 5.9 ms periodic sample recorded as non-blocking and
      not representative proof of roboRIO performance.
- [x] Frozen Backbone and Frozen Interface Contract preserved.
- [x] A01_L01-L07, A01_L09, and V00_L01 preserved.
- [x] V00_L02 remains SUSPENDED / READ-ONLY / UNMODIFIED.
- [x] Transition guide and lesson documentation finalized.
- [x] Explicit Architect/User re-freeze evidence recorded.
- [x] A01_L08 set to COMPLETE / FROZEN / READ-ONLY.
- [x] Documentation is ready for user-owned publication.

Final architecture review: `PASS`. Documentation closure: `PASS`. Git and
GitHub remain user-owned and were not run by Codex.
