# A00_L03 Bounded Robot-Relative Autonomous Motion - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Architecture review: `PASS`  
Freeze state: `FROZEN`  
Previous lesson: `A00_L02_AutonomousModeScheduling - COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Inheritance

- [x] AGENTS.md, README, Documents A/B/C, Frozen Backbone, and Frozen Interface Contract reviewed.
- [x] A00 roadmap authorization and locked lesson order reviewed.
- [x] A00_L03 inherited directly from frozen A00_L02.
- [x] Generated artifacts were removed before the inherited baseline build.
- [x] A00_L01, A00_L02, and S00 remain frozen and unchanged.
- [x] RobotContainer remains composition root only.
- [x] IO, observation, telemetry, and centralized stop contracts are preserved.

## Single Concept

- [x] One bounded nonzero robot-relative autonomous motion concept was added.
- [x] A00_L03 is recorded as the first A00 lesson permitted nonzero autonomous motion.
- [x] No second autonomous behavior concept was introduced.

## Command Contract

- [x] `BoundedRobotRelativeAutonomousDriveCommand` requires `SwerveSubsystem`.
- [x] The command validates and defensively copies `ChassisSpeeds`.
- [x] Duration is finite and positive.
- [x] A monotonic clock is injected for deterministic timing.
- [x] The command submits exactly one robot-relative request.
- [x] Field-relative conversion is not used.
- [x] Invalid, nonfinite, backward, or throwing clock behavior fails closed.
- [x] `runsWhenDisabled()` is false.
- [x] Normal and interrupted termination call centralized `stop()`.
- [x] The command has no direct IO, telemetry, pose, or estimator logic.

## Autonomous Ownership

- [x] `RobotContainer` composes bounded motion with `AutonomousSafetyHoldCommand.repeatedly()`.
- [x] The repeating hold retains Swerve ownership after bounded motion completes.
- [x] The repeating hold does not restart nonzero motion.
- [x] Default Teleop cannot regain Swerve during active Autonomous ownership.
- [x] `Robot.java` remains unchanged.

## Simulation Learning Baseline

- [x] Forward request is named `+0.30 m/s`.
- [x] Lateral request is named `0.00 m/s`.
- [x] Angular request is named `0.00 rad/s`.
- [x] Duration is named `1.0 s`.
- [x] These values are documented as Simulation-only learning baselines.

## Automated and Java Verification

- [x] Java 17 verification: PASS per user-supplied evidence.
- [x] Focused bounded-motion command coverage added.
- [x] Autonomous composition/scheduler coverage updated.
- [x] Deterministic clocks are used in new tests.
- [x] No `Thread.sleep` was added to new tests.

## Simulation / Driver Station Verification

- [x] Disabled baseline remained stationary.
- [x] Bounded robot-relative motion moved at approximately `+0.30 m/s`.
- [x] Motion stopped automatically after approximately `1.0 s`.
- [x] Drive position changed and final output/velocity returned to zero.
- [x] Repeating safety hold did not restart motion.
- [x] Joystick input during Autonomous did not move Swerve.
- [x] Autonomous to Disabled performed a safe stop.
- [x] Teleop fresh-input recovery worked.

## Real-Robot Gate

- [x] Disabled baseline: PASS.
- [x] Autonomous bounded real drivetrain motion on the floor: PASS; the
  command completed, stopped, and did not restart while Autonomous remained
  enabled.
- [x] Autonomous -> Disabled interruption: PASS; drivetrain stopped with no
  stale output.
- [x] Autonomous -> Teleop transition: PASS; autonomous ownership cleared and
  fresh Teleop control recovered normally.
- [x] Autonomous -> Test transition: PASS; no stale or restarted autonomous
  output appeared.
- [x] A temporary E-Stop was followed by a robot reboot; Case 3 was rerun
  successfully and the event is not classified as an A00_L03 defect.
- [x] Real-robot PASS is limited to user-supplied A00_L03 bounded-motion and
  lifecycle-transition evidence.
- [x] No claim is made for PathPlanner, AutoBuilder, localization, autonomous
  path following, or competition readiness.

## Explicitly Out of Scope

- [x] No A00_L04 Test-mode/global motion-gating implementation.
- [x] No PathPlanner or AutoBuilder.
- [x] No trajectories or path following.
- [x] No pose-targeted autonomous behavior.
- [x] No field/alliance transforms.
- [x] No vision or AprilTag integration.
- [x] No multi-step autonomous routines.
- [x] No hardware calibration or gain tuning.
- [x] No IO, observation, telemetry, Robot.java, A00_L02, or S00 changes.

## Documentation and Review Gates

- [x] Inherited A00_L02 identity was normalized to A00_L03.
- [x] `docs/A00_L02_to_A00_L03_Step_by_Step.md` was finalized as `FINAL / PASS`.
- [x] Final architecture review: PASS.
- [x] Transition guide finalized and marked FINAL / PASS.
- [x] A00_L03 marked COMPLETE / FROZEN / READ-ONLY.
- [ ] User Git commit.
- [ ] User Git push.

## Deferred / Non-Blocking Items

- [x] Broader real-robot capability remains outside this lesson.
- [x] Test-mode/global motion gating is deferred to A00_L04.
- [x] Final drivetrain tuning remains outside this lesson.
- [x] Inherited commissioning timing tests remain technical debt.
- [x] No new Glass-specific behavior or evidence was introduced; separate Glass evidence remains NOT TESTED.
