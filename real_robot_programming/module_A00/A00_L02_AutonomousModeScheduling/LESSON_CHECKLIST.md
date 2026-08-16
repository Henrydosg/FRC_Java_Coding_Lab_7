# A00_L02 Autonomous Mode Scheduling - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Architecture review: `PASS`  
Freeze state: `FROZEN`  
Previous lesson: `A00_L01_AutonomousCommandLifecycleFoundation` - `COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Inheritance

- [x] AGENTS.md, repository README, authoritative Documents A/B/C, Frozen Backbone, and Frozen Interface Contract reviewed.
- [x] A00 roadmap authorization and locked lesson order reviewed.
- [x] A00_L01 inherited directly as the frozen source lesson.
- [x] Generated build artifacts were removed during inheritance.
- [x] Java 17 baseline build before implementation: PASS.
- [x] A00_L01 and all S00 source, tests, configuration, and historical guides remain unchanged.
- [x] RobotContainer remains the composition root.
- [x] Frozen Backbone, IO contracts, observation boundary, and telemetry direction are preserved.

## Single Concept

- [x] The one new concept is autonomous mode composition and scheduler requirement ownership.
- [x] No second autonomous behavior concept was introduced.

## Composition and Ownership Contract

- [x] The inherited `AutonomousSafetyHoldCommand` remains unchanged.
- [x] `RobotContainer` constructs the safety hold with `SwerveSubsystem`.
- [x] `Timer::getFPGATimestamp` is injected.
- [x] The named lifecycle interval is `Constants.AutonomousConstants.kSafetyHoldLifecycleDurationSeconds`.
- [x] `getAutonomousCommand()` returns the repeating zero-motion composition.
- [x] `.repeatedly()` retains the `SwerveSubsystem` requirement until external cancellation/interruption.
- [x] The 1.0 second value is documented as a repeat interval, not an ownership limit.
- [x] The default teleop command cannot regain Swerve ownership while the autonomous composition is scheduled.
- [x] `Robot.java` remains unchanged.

## Zero-Motion Boundary

- [x] A00_L02 issues no nonzero autonomous `acceptChassisSpeeds(...)` request.
- [x] Repeated lifecycle intervals remain zero-motion.
- [x] Autonomous cancellation preserves centralized `SwerveSubsystem.stop()` semantics.
- [x] A00_L01 and A00_L02 remain zero-motion.
- [x] A00_L03 is recorded as the first lesson permitted to issue nonzero autonomous motion.
- [x] Test-mode global motion gating remains outside this lesson.

## Automated Verification

- [x] `RobotContainerAutonomousModeSchedulingTest`: PASS.
- [x] Inherited `AutonomousSafetyHoldCommandTest`: PASS.
- [x] Full Java 17 regression: PASS.
- [x] Final clean Java 17 build: PASS.
- [x] Repeated-hold interval ownership test passes beyond multiple intervals.
- [x] The test fixture uses the current per-mechanism module health fields.
- [x] No deprecated scheduler API was introduced.

## Simulation / Driver Station Verification

- [x] Simulation Disabled baseline remains stationary.
- [x] Autonomous Enabled remains zero-motion.
- [x] Nonzero joystick input during Autonomous does not move Swerve.
- [x] Autonomous to Disabled performs a safe stop.
- [x] Autonomous to Teleop permits fresh-input recovery.
- [x] Autonomous to Test cancels the safety composition.

## Real-Robot Gate

- [ ] Real-robot A00_L02 verification - HOLD.
- [x] No real-robot PASS is claimed without user-supplied hardware evidence.

## Explicitly Out of Scope

- [x] No nonzero autonomous motion.
- [x] No PathPlanner or AutoBuilder.
- [x] No trajectories or path following.
- [x] No pose targeting.
- [x] No field/alliance transforms.
- [x] No vision or AprilTag integration.
- [x] No multi-step autonomous routines.
- [x] No Test-mode global motion-gating redesign.
- [x] No hardware calibration or gain tuning.
- [x] No Robot.java changes.
- [x] No changes to frozen A00_L01, S00, L22, or Frozen contracts.

## Documentation and Review Gates

- [x] Inherited A00_L01 identity/status was normalized to A00_L02.
- [x] Implementation summary and supplied verification evidence are recorded.
- [x] `docs/A00_L01_to_A00_L02_Step_by_Step.md` was created and records the complete transition.
- [x] Final architecture review: PASS.
- [x] Transition guide finalized and marked FINAL / PASS.
- [x] A00_L02 marked `COMPLETE / FROZEN / READ-ONLY`.
- [ ] User Git commit.
- [ ] User Git push.

## Non-Blocking Technical Debt

- [x] Inherited commissioning tests using `Thread.sleep` remain deferred.
- [x] Optional stronger default-command precondition assertion remains deferred.
- [x] Real-robot verification remains HOLD.
- [x] Test-mode global motion gating remains deferred.
