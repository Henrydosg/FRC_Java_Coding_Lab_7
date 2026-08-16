# A00_L01 Autonomous Command Lifecycle Foundation - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Architecture review: `PASS`  
Previous lesson: `S00_L24_PoseEstimationAndAutonomousReadiness` -
`COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Inheritance

- [x] AGENTS.md, repository README, authoritative Documents A/B/C, Frozen Backbone, and Frozen Interface Contract reviewed.
- [x] A00 roadmap authorization and locked lesson order reviewed.
- [x] S00_L24 inherited directly as the frozen source lesson.
- [x] Generated build artifacts were removed during inheritance.
- [x] Java 17 baseline build before implementation: PASS.
- [x] S00_L24/L22 source, tests, configuration, and historical transition guides remain unchanged.
- [x] RobotContainer remains the composition root.
- [x] Frozen Backbone, IO contracts, observation boundary, and telemetry direction are preserved.

## Single Concept

- [x] The one new concept is autonomous command lifecycle and stop ownership.

## AutonomousSafetyHoldCommand Contract

- [x] Requires `SwerveSubsystem`.
- [x] Uses an explicit finite positive duration.
- [x] Uses an injected monotonic clock.
- [x] `initialize()` captures the start time and calls `stop()`.
- [x] `execute()` issues no drivetrain request.
- [x] `execute()` accesses no IO and performs no telemetry/business logic.
- [x] Bounded expiry finishes the command.
- [x] Invalid, backward, nonfinite, or throwing clock behavior fails closed.
- [x] `runsWhenDisabled() == false`.
- [x] `end(false)` calls `stop()`.
- [x] `end(true)` calls `stop()`.
- [x] No nonzero `acceptChassisSpeeds(...)` request exists in A00_L01.
- [x] No direct IO, estimator decision, or vendor API access exists in the command.

## Zero-Motion Boundary

- [x] A00_L01 remains zero-motion.
- [x] A00_L02 remains zero-motion.
- [x] A00_L03 is recorded as the first lesson permitted to issue nonzero
  autonomous drivetrain motion.
- [x] AutonomousSafetyHoldCommand is intentionally not wired into Robot
  autonomous selection in A00_L01.
- [x] Robot.java, RobotContainer.java, SwerveSubsystem.java, and IO were not
  changed for this lesson.

## Automated Verification

- [x] Focused `AutonomousSafetyHoldCommandTest`: PASS.
- [x] Full Java 17 regression: PASS.
- [x] Final Java 17 clean build: PASS; user supplied.
- [x] No Thread.sleep timing tests were introduced.

## Simulation / Driver Station Verification

- [x] Simulation Disabled baseline remains stationary.
- [x] Autonomous Enabled behavior is zero-motion and non-regressive.
- [x] Teleop fresh-input recovery after Autonomous/Disabled transition passes.
- [x] No dashboard or Glass command registration was introduced.

## Real-Robot Gate

- [ ] Real-robot A00_L01 verification - HOLD.
- [x] No real-robot PASS is claimed without user-supplied hardware evidence.

## Explicitly Out of Scope

- [x] No nonzero autonomous motion.
- [x] No PathPlanner or AutoBuilder.
- [x] No trajectory generation or path following.
- [x] No pose-target autonomous behavior.
- [x] No field/alliance transforms.
- [x] No vision or AprilTag integration.
- [x] No multi-step autonomous routines.
- [x] No hardware calibration or gain tuning.
- [x] No changes to frozen S00_L24 or earlier lessons.

## Final Review Gates

- [x] Source implementation and focused tests are complete for the locked
  scope.
- [x] Supplied Java and Simulation evidence is recorded accurately.
- [x] A00_L01 documentation is normalized from inherited L24 identity.
- [x] `docs/S00_L24_to_A00_L01_Step_by_Step.md` is final and marked FINAL/PASS.
- [x] Final architecture review: PASS.
- [x] A00_L01 marked `COMPLETE / FROZEN / READ-ONLY`.
- [ ] User Git commit.
- [ ] User Git push.
