# A01_L09 - PathPlanner NamedCommands and Event Markers - Final Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Previous lesson: `A01_L08_AutonomousRoutineSelectionAndSafeComposition - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Inheritance

- [x] AGENTS.md and root README reviewed.
- [x] Authoritative English Documents A/B/C reviewed.
- [x] Frozen Backbone and Frozen Interface Contract reviewed and preserved.
- [x] A01 ADR and approved L09 amendment reviewed.
- [x] Historical Real Robot HOLD reconciled as a satisfied pre-verification gate.
- [x] Frozen A01_L01-L08 reviewed and left unchanged.
- [x] L09 identity and strict inheritance verified.
- [x] L09 is the authorized A01 roadmap completion point.

## Event Architecture

- [x] Objective remains NamedCommands/event-marker dispatch only.
- [x] Runtime chain uses typed binding and `Commands.defer(...)`.
- [x] Every dispatch constructs a fresh Command.
- [x] Stable event identifier is `LEARNING_EVENT`.
- [x] Demonstration command is deterministic, bounded, observable, and hardware-free.
- [x] Immutable Observation -> telemetry facade flow is preserved.
- [x] Command does not call NetworkTables directly.
- [x] Duplicate, missing, invalid, or mismatched binding fails closed.
- [x] Learning event does not require `SwerveSubsystem`.
- [x] No provider framework or fake mechanism was introduced.

## Frozen L08 and Safety Contracts

- [x] `SAFE_STOP` remains chooser default.
- [x] `ONE_METER_PATH` remains preserved.
- [x] `ONE_METER_WITH_EVENT` remains explicit and non-default.
- [x] Selection snapshot and fresh autonomous construction remain preserved.
- [x] One-shot readiness remains preserved.
- [x] L04 remains the sole alliance-transform owner.
- [x] `shouldFlipPath = false` remains preserved.
- [x] Execution paths use `preventFlipping = true`.
- [x] `SwerveSubsystem` remains drivetrain requirement owner.
- [x] Centralized `SwerveSubsystem.stop()` remains preserved.
- [x] Fail-closed behavior remains preserved.
- [x] Disable/mode-loss stop remains preserved.
- [x] No automatic restart remains preserved.
- [x] No D01 mechanism architecture or fake mechanism implementation exists.

## Automated Verification

- [x] compileJava PASS.
- [x] compileTestJava PASS.
- [x] Focused L09 event tests PASS.
- [x] PathPlanner event and transform tests PASS.
- [x] Routine selection and integration tests PASS.
- [x] 384 tests from 41 unchanged inherited test classes PASS.
- [x] Full suite 446/446 PASS with zero failures, errors, or skips.
- [x] Isolated clean build PASS.
- [x] Historical default-output Windows report lock is documented as non-defect.

## User-Owned Runtime Verification

- [x] Simulation PASS.
- [x] Blue Simulation PASS.
- [x] Red Simulation PASS.
- [x] ONE_METER_WITH_EVENT PASS.
- [x] LEARNING_EVENT dispatched exactly once.
- [x] Telemetry observed: Active=false, DispatchCount=1,
      LastEvent="LEARNING_EVENT", State="COMPLETED".
- [x] Path continued while LEARNING_EVENT executed.
- [x] Disable/mode-loss stop PASS.
- [x] No automatic restart PASS.
- [x] Real Robot PASS / USER-VERIFIED.

## Documentation and Closure

- [x] Root README reconciled.
- [x] L09 README reconciled.
- [x] LESSON_STATUS reconciled.
- [x] LESSON_PLAN finalized.
- [x] LESSON_CHECKLIST finalized.
- [x] Transition guide finalized.
- [x] English learning guide finalized.
- [x] Vietnamese learning guide finalized.
- [x] Exact endpoint accuracy remains unclaimed.
- [x] Final PID/feedforward tuning remains unclaimed.
- [x] Final physical characterization remains unclaimed.
- [x] No production Java modified during closure.
- [x] No tests modified during closure.
- [x] No PathPlanner assets modified during closure.
- [x] No frozen L01-L08 files modified during closure.
- [x] No Git/GitHub operations performed.
- [x] A01_L09 is COMPLETE / FROZEN / READ-ONLY.
- [x] A01_L10 and any next lesson/module remain NOT CREATED / NOT STARTED.
