# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers`
- Title: `A01_L09 - PathPlanner NamedCommands and Event Markers`
- Previous Lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Authoritative Parent: `A01_L08 @ 135272c`
- Lesson Goal: controlled `LEARNING_EVENT` dispatch using NamedCommands and a
  fresh, observable, non-mechanism WPILib Command.
- Event Feature Status: `IMPLEMENTED / VERIFIED`

## Governance

- Phase 2A baseline reconstruction: `PASS` by user-authoritative evidence.
- Phase 2A user verification: `compileJava PASS`, `compileTestJava PASS`, full
  inherited test suite `PASS`, and clean build `PASS`.
- Phase 2A final baseline audit: `PASS`.
- Phase 2B implementation: authorized by the Phase 2B implementation record.
- A01_L08: `COMPLETE / FROZEN / READ-ONLY`.
- Historical L09 event material: preserved through existing history and docs.
- No editable lesson remains in A01; A01_L09 is frozen after final closure.
- V00_L02: `SUSPENDED / READ-ONLY / UNMODIFIED`.
- Frozen Backbone: `PRESERVED`.
- Frozen Interface Contract: `PRESERVED`.
- Phase 1 event architecture: `PASS / LOCKED`.

## Phase 2B Implementation

- Event ID: `frc.robot.autonomous.AutonomousEventId`.
- Stable PathPlanner name: `LEARNING_EVENT`.
- Registration uses scheduler-native `Commands.defer(...)` with an immutable
  requirement set and a fresh command supplier per dispatch.
- The demonstration event has no Swerve or mechanism requirement and publishes
  immutable STARTED, ACTIVE, COMPLETED, CANCELLED, and FACTORY_FAILURE states.
- Read-only event telemetry publishes `LastEvent`, `State`, `Active`, and
  `DispatchCount` through the central RobotTelemetry boundary.
- `ONE_METER_WITH_EVENT` uses the inherited final-L08 preparation, claim,
  scheduler-native path execution, terminal HOLDING, SAFE_STOP, and no-restart
  architecture.
- `A01_L09_OneMeter_With_Learning_Event.path` is unchanged and retains one
  `LEARNING_EVENT` marker at relative position `0.5`.

## Inheritance and Scope Audit

- Final-L08-equivalent production files: `63` from the Phase 2A baseline;
  final-L08 safety files remain hash-identical after Phase 2B.
- Final-L08-equivalent test files: `52` from the Phase 2A baseline.
- Authorized production merge files modified: `9`.
- New production event files: `6`.
- Authorized test merge files modified: `6`.
- New event test files: `4`.
- `PrepareAutonomousCommand.java`: inherited exactly; not modified.
- `SafeAutoBuilderCommand`: absent.
- Manual child lifecycle delegation: absent.
- Gradle, vendordeps, Swerve, CTRE, CANcoder, PID/feedforward, gyro IO,
  PathPlanner assets, and V00_L02: unmodified by this implementation. `.Glass`
  remains an operator-view configuration artifact outside the production/test
  architecture boundary.

## Historical Verification Hold

The original Phase 2B checkpoint recorded a Windows Gradle/Javac
`compileTestJava` classpath hold. The remaining automated and user-owned gates
were not claimed at that intermediate checkpoint. That history remains
preserved in the Phase 2B implementation record and is not the current result.

## Verification

- Architecture Review: `PASS`.
- Baseline Build: `PASS` by Phase 2A user-authoritative evidence.
- Build: `PASS` - User-owned Java 17 re-verification on 2026-08-31 passed
  `gradlew clean`, `gradlew test --rerun-tasks`, and `gradlew clean build`.
- `compileJava`: `PASS` under the WPILib Java 17 runtime.
- `compileTestJava`: `PASS`.
- Focused L09 event, path, routine, integration, observation, and telemetry
  tests: `PASS`.
- Inherited safety regressions: `PASS`.
- Current full test suite: `460/460 PASS` - JUnit XML records `0` failures,
  `0` errors, and `0` skipped.
- Simulation: `PASS` for Blue and Red event/path behavior, telemetry,
  coexistence, mode-loss stop, no automatic restart, and Teleop recovery.
- Driver Station / Glass: `PASS` for `/AutonomousEvent` and
  `/AutonomousPreparation` inspection.
- Real Robot: `PASS` for SAFE_STOP, `ONE_METER_PATH`,
  `ONE_METER_WITH_EVENT`, LEARNING_EVENT behavior, terminal HOLDING, no
  automatic restart, Disabled-to-Teleop recovery, marker preservation, and
  event/path coexistence on Blue and Red.

## Documentation

- Phase 2B implementation record: maintained in `docs/`.
- Transition Guide: `FINAL / PASS`; the guide establishes the final-L08 to L09
  inheritance chain and records the verified event feature.
- Documentation reconciliation: `PASS`.
- Final Architecture Review: `PASS`.
- Final Closure Review: `PASS`.
- Git Commit: `PASS / USER-PUBLISHED @ 6b243bb` - `Complete reconstructed
  A01_L09 named commands and event markers`.
- Git Push: `PASS / USER-VERIFIED` - repository publication metadata and the
  remote-main reflog record publication of `6b243bb`.

## Known Issues

- Exact endpoint accuracy, final PID/feedforward tuning, and final physical
  characterization remain explicitly unclaimed.
- A01_L09 is `COMPLETE / FROZEN / READ-ONLY` after final closure review PASS.
  Git publication is complete at `6b243bb` by User-owned commit/push.
