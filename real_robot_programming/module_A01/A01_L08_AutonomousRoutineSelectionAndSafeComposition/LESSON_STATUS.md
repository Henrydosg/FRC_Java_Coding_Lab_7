# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`
- Title: `A01_L08 - Autonomous Routine Selection and Safe Composition`
- Previous Lesson: `A01_L07_AutoBuilderContractIntegration`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: select and safely compose autonomous routines with explicit
  cancellation, requirements, and failure behavior.

## Governance and Inheritance Gates

- Governance: `PASS` - required governance, A01 ADR, authoritative English
  Documents A/B/C, frozen L01-L07, and inherited L08 were reviewed.
- Architecture Review: `PASS` - chooser ownership, one-time selection
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
- Build: `PASS / USER-VERIFIED` - post-repair WPILib VS Code clean build result
  is recorded below.

## Implementation and Verification Gates

- Production Implementation: `PASS` - factory and chooser snapshot wiring only.
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
- Real Robot: `PASS / USER-CONFIRMED`.
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

- L08 is COMPLETE / FROZEN / READ-ONLY and is the frozen inheritance source
  for A01_L09.
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
- A01_L09 is not created or started.

## Final Closure

- Final lesson state: `COMPLETE / FROZEN / READ-ONLY`.
- Frozen inheritance source for A01_L09: `YES`.
- Endpoint precision, final PID/feedforward tuning, and final physical
  characterization: explicitly unclaimed.
- Documentation-only closure: `PASS`; no production Java, tests, configuration,
  assets, or frozen L01-L07 files were modified during closure.

## Failure Classification Record

- Category B (valid inherited-test migration): ten failures relied on implicit
  routine selection, a command snapshot created before readiness, or the old
  one-shot stop lifecycle. The tests now select `ONE_METER_PATH` explicitly,
  request fresh commands after accepted readiness, and assert bounded
  `SAFE_STOP` for fail-closed cases.
- Category F (focused fixture contract mismatch): one factory test asserted no
  Swerve requirement while its delegated command intentionally required Swerve.
  The assertion now verifies the locked requirement owner.
- Production defect: none found.
- Assertions weakened: no. Tests disabled/deleted/ignored/skipped: no.
- Frozen L07 modified: no.
