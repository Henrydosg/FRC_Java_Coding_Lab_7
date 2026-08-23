# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`
- Title: `A01_L09 - PathPlanner NamedCommands and Event Markers`
- Previous Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: controlled event-marker dispatch using NamedCommands and a
  fresh, observable, non-mechanism WPILib Command.

## Governance and Architecture Gates

- Governance: `PASS` - AGENTS.md, root README, authoritative English Documents
  A/B/C, Frozen Backbone, Frozen Interface Contract, A01 ADR and amendment,
  frozen L01-L08, active L09 documentation, production, tests, and PathPlanner
  assets were reviewed.
- ADR Real-Robot HOLD Reconciliation: `PASS` - historical HOLD was the
  user-owned pre-verification gate. The ADR states that HOLD remains until the
  user supplies applicable hardware evidence; the supplied real-robot PASS
  satisfies that gate. No ADR amendment is required.
- Architecture Review: `PASS` - all locked L09 and inherited L08 contracts are
  preserved.
- Source Lesson: `PASS` - L08 remains COMPLETE / FROZEN / READ-ONLY.
- Directory Identity: `PASS`.
- ADR Amendment: `PASS / RECORDED` - demonstration binding is authorized; D01
  remains outside the A01 compile and ownership boundary.

## Build and Automated Verification

- Baseline Build: `PASS / USER-CONFIRMED`.
- compileJava: `PASS` - repository evidence and user-verified final build.
- compileTestJava: `PASS` - repository evidence and user-verified final build.
- Focused L09 Tests: `PASS` - event binding, lifecycle, registration,
  telemetry, PathPlanner event preservation, routine selection, and composition.
- Inherited Regression: `PASS` - 384 tests across 41 unchanged inherited test
  classes passed.
- Full Test Suite: `PASS` - 446/446, zero failures, errors, or skips.
- Clean Build: `PASS` - isolated clean-output verification is recorded. The
  historical Windows lock on the default Gradle problems report is not a
  production, test, architecture, or project-configuration defect.
- Build: `PASS / USER-VERIFIED`.

## Runtime Verification

- Simulation: `PASS / USER-VERIFIED`.
- Blue Simulation: `PASS`.
- Red Simulation: `PASS`.
- ONE_METER_WITH_EVENT: `PASS`.
- Event Dispatch: `PASS` - one `LEARNING_EVENT` dispatch observed.
- Event Telemetry: `PASS` - `Active=false`, `DispatchCount=1`,
  `LastEvent="LEARNING_EVENT"`, `State="COMPLETED"`.
- Path/Event Concurrency: `PASS` - path execution continued while the event
  command executed.
- Disable / Mode-Loss Stop: `PASS` - robot stopped when Disabled during
  Autonomous.
- No Automatic Restart: `PASS` - re-enabling Autonomous without a fresh
  readiness/reset sequence did not restart the previous routine.
- Driver Station / Glass: `PASS / USER-VERIFIED EVENT TELEMETRY`.
- Real Robot: `PASS / USER-VERIFIED` - current L09 ran on the real Swerve robot.
- Real-Robot Evidence Owner: `USER`.

## Locked Architecture

- Runtime chain: PathPlanner path -> event marker -> NamedCommands -> typed
  binding -> `Commands.defer(...)` -> fresh WPILib Command.
- Stable event: `LEARNING_EVENT` only.
- Demonstration event: deterministic, bounded, observable, non-mechanism,
  hardware-free, and without direct NetworkTables access.
- RobotContainer: composition root only.
- L04: sole alliance-transform owner.
- AutoBuilder flipping: `shouldFlipPath = false`.
- Execution paths: `preventFlipping = true`.
- `SAFE_STOP`: chooser default.
- `ONE_METER_PATH`: preserved.
- `ONE_METER_WITH_EVENT`: explicit non-default routine.
- Drivetrain requirement and stop owner: `SwerveSubsystem`.
- Event requirement: does not require `SwerveSubsystem`.
- Fail-closed, Disable/mode-loss stop, cancellation, and no-restart contracts:
  preserved.
- D01 mechanism architecture, mechanism IO, vendor APIs, mechanism contracts,
  and fake mechanism implementations: absent.

## Documentation and Publication

- Transition Guide: `FINAL / PASS` -
  `docs/A01_L08_to_A01_L09_Step_by_Step.md`.
- English Learning Guide: `FINAL / PASS`.
- Vietnamese Learning Guide: `FINAL / PASS`.
- Git Commit: `NOT TESTED` - user-owned; Codex ran no Git operations.
- Git Push: `NOT TESTED` - user-owned; Codex ran no Git operations.

## Known Issues and Unclaimed Scope

- No unresolved L09 architecture, implementation, build, test, Simulation, or
  real-robot blocker remains.
- Exact endpoint accuracy is not claimed.
- Final PID/feedforward tuning is not claimed.
- Final physical characterization and competition readiness are not claimed.
- `LEARNING_EVENT` is not real Intake, Feeder, Flywheel, or other mechanism
  integration.
- Vision, AprilTags, dynamic replanning, obstacle avoidance, competition
  strategy, and new mechanism architecture remain outside A01.

## Final Closure

- Final lesson state: `COMPLETE / FROZEN / READ-ONLY`.
- A01 roadmap completion point: `A01_L09`.
- A01_L10: `NOT CREATED / NOT STARTED / NOT AUTHORIZED`.
- Next lesson/module: `NOT CREATED / NOT STARTED`; requires separate governance.
