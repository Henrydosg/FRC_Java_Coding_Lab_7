# S00_L24 Pose Estimation and Autonomous Readiness - Lesson Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Finalization state: `COMPLETE / FROZEN / READ-ONLY`  
Source: `S00_L23_OdometryAndPoseVisualization` - `COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Inheritance

- [x] AGENTS.md, repository README, authoritative Documents A/B/C, Frozen Backbone, and Frozen Interface Contract reviewed.
- [x] L23 confirmed `COMPLETE / FROZEN / READ-ONLY`.
- [x] L23 inherited as the independent L24 project.
- [x] Generated L24 artifacts cleaned during inheritance.
- [x] L22 and L23 source/configuration/documentation remain untouched.
- [x] RobotContainer remains composition root only.
- [x] Frozen Backbone, IO contracts, observation boundary, and telemetry direction preserved.

## Increment Completion

- [x] Increment 0: L24 identity, inheritance baseline, and architecture lock recorded.
- [x] Increment 1: estimator ownership, coexistence, validity, covariance, and timestamp contract designed.
- [x] Increment 2: subsystem-owned `SwerveDrivePoseEstimator` implemented and tested.
- [x] Increment 3A: immutable additive `EstimatedPoseObservation` implemented and tested.
- [x] Increment 3B: estimated-pose NT4 telemetry implemented and tested.
- [x] Increment 4A: known-field-pose reset contract designed.
- [x] Increment 4B: atomic subsystem reset foundation implemented and tested.
- [x] Increment 4C: Disabled-only reset command implemented and tested.
- [x] Increment 4D: provisional `Pose2d.kZero` learning pose and `Reset Known Starting Pose` dashboard trigger implemented and tested.
- [x] Increment 5A: autonomous-readiness drivetrain contract audited.
- [x] Increment 5B: measured robot-relative `ChassisSpeeds` API implemented and tested.
- [x] Increment 5C: null/nonfinite robot-relative and field-relative request hardening implemented and tested.
- [x] Increment 5D: autonomous command lifecycle/safety contract audited without adding an autonomous command.
- [x] Increment 5E: Disabled-transition stale-intent disarm implemented and regression-tested.
- [x] Reset command lifecycle defect fixed: persistent dashboard command is reusable once per schedule.
- [x] Nine deprecated test-only `Command.schedule()` calls replaced with `CommandScheduler` scheduling.

## Core Architecture Acceptance

- [x] L23 `currentPose()` retains its odometry meaning.
- [x] L24 estimated pose is separate subsystem-owned state.
- [x] Odometry and estimator use validated fixed-order FL/FR/BL/BR measurements.
- [x] Invalid/unhealthy/nonfinite samples hold localization safely and recover without integrating unverified gap motion.
- [x] `SwerveObservation` remains immutable, primitive-only, vendor-neutral, and additive for estimated pose.
- [x] Telemetry consumes observations only.
- [x] Existing `/Swerve/Pose/...` and L23 odometry Field2d meaning remain unchanged.
- [x] Estimated-pose telemetry uses a separate `/Swerve/EstimatedPose/...` namespace.
- [x] Known-pose reset does not reset physical sensors.
- [x] Reset command is Disabled-only and requires `SwerveSubsystem`.
- [x] `getMeasuredRobotRelativeSpeeds()` is measurement-derived, validated, defensive, and gyro/field-heading independent.
- [x] Null/nonfinite actuation requests fail closed without stale intent.
- [x] Disabled clears armed production intent and requires a fresh valid request after re-enable.
- [x] `stop()` remains the centralized all-module stop authority.

## Java Verification

- [x] Focused Java 17 regression: PASS - user supplied.
- [x] Full Java 17 regression: PASS - user supplied.
- [x] Java 17 clean build: PASS - all tasks executed from clean state.
- [x] Deprecated `Command.schedule()` compiler warnings removed.
- [x] No production warning cleanup or behavior refactor was introduced.

## Simulation and Glass Verification

- [x] Pose availability and validity verified.
- [x] EstimatedPose availability and validity verified.
- [x] Pose and EstimatedPose agree without vision.
- [x] `Reset Known Starting Pose` reset #1 verified.
- [x] Same persistent dashboard command performs reset #2 after additional motion.
- [x] Raw sensor positions remain accumulated after pose reset.
- [x] Post-reset localization continuity verified.
- [x] Disabled -> Teleoperated with neutral input does not resume stale motion.
- [x] Fresh valid joystick input after re-enable resumes motion normally.

## Real-Robot Gate

- [ ] L24 real-robot estimator verification - HOLD.
- [ ] L24 real-robot known-field-pose reset verification - HOLD.
- [ ] L24 real-robot Disabled-transition safety verification - HOLD.
- [x] No real-robot L24 PASS is claimed without user-supplied hardware evidence.

## Deferred Technical Debt

- [x] Final drive PID/feedforward optimization is explicitly deferred; L23 provisional gains remain unchanged.
- [x] Normal-drive Phoenix `setControl` status handling is recorded as deferred robustness debt.
- [x] Retained CTRE diagnostic signals, connection-history fields, and dashboard ownership fields are recorded as safe/deferred IDE warning debt.
- [x] Vision/AprilTag, timestamp/latency, and estimator uncertainty tuning are future work.

## Explicitly Out of Scope

- [x] No autonomous routine.
- [x] No PathPlanner or AutoBuilder.
- [x] No trajectory generation or path following.
- [x] No vision or AprilTag integration.
- [x] No alliance transforms or field mirroring.
- [x] No official competition starting-pose database.
- [x] No estimator-specific Field2d replacement.
- [x] No hardware sensor reset, calibration changes, or final drive-gain tuning.

## Finalization Gates

- [x] README, plan, checklist, status, and transition guide reconciled with implemented source and supplied evidence.
- [x] Transition guide records the complete L23 -> L24 evolution chronologically.
- [x] Final architecture review completed.
- [x] L24 marked `COMPLETE / FROZEN`.
- [ ] User Git commit.
- [ ] User Git push.
