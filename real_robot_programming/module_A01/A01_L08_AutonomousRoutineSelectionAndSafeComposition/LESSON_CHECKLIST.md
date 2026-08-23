# A01_L08 - Autonomous Routine Selection and Safe Composition - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Previous lesson: `A01_L07_AutoBuilderContractIntegration - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

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
- [x] L08 is complete and frozen as the read-only inheritance source for L09.
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
- [x] L08 marked COMPLETE / FROZEN / READ-ONLY.
- [x] A01_L09 remains NOT CREATED / NOT STARTED.

## Exclusions

NamedCommands, event markers, mechanism coordination, vision, AprilTags,
replanning, pathfinding, competition strategy, drivetrain/Swerve IO redesign,
CTRE/CAN changes, calibration changes, PID/FF tuning, RobotConfig physical
characterization, and unnecessary framework abstraction remain excluded.

Final closure keeps exact endpoint accuracy, final PID/feedforward tuning, and
final physical characterization explicitly unclaimed. Git commit and push remain
user-owned and were not run by Codex.
