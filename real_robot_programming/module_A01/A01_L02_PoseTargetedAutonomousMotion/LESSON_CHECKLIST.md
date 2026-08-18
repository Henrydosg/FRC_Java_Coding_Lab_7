# A01_L02 Pose-Targeted Autonomous Motion - Checklist

Status: `COMPLETE / FROZEN / READ-ONLY`  
Freeze State: `FROZEN`  
Architecture Review / Audit / Design Lock: `PASS`  
Previous lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract` - `COMPLETE / FROZEN / READ-ONLY`  
Git: user-owned; not run by Codex

## Governance and Architecture

- [x] A01 ADR and lesson order reviewed.
- [x] Frozen A01_L01 inheritance baseline preserved.
- [x] Frozen Backbone and Frozen Interface Contract preserved.
- [x] `RobotContainer` remains the composition root.
- [x] `SwerveSubsystem` retains localization, field-relative conversion,
  actuation, and centralized stop ownership.
- [x] L02 added one concept only: a finite pose-target command.

## Implemented L02 Contract

- [x] `PoseTargetedAutonomousMotionCommand` requires `SwerveSubsystem`.
- [x] Feedback uses `getEstimatedPose()`.
- [x] Field-frame X/Y/wrapped-heading error is used.
- [x] Translation is magnitude-limited; heading output is independently clamped.
- [x] Output uses `acceptFieldRelativeChassisSpeeds(...)` only.
- [x] Translation and heading suppression are evaluated every control cycle.
- [x] Completion requires simultaneous translation and heading tolerance.
- [x] Immutable target/configuration values are validated before runtime.
- [x] Invalid pose/observation/time, timeout, and mode loss fail closed.
- [x] `end(...)` and fail-closed paths use centralized `stop()`.
- [x] One-shot accepted-reset readiness and no automatic restart are preserved.

## Verification

- [x] Production compile and `compileTestJava` passed in the user's WPILib Java 17 environment.
- [x] `PoseTargetedAutonomousMotionCommandTest`: 10/10 passed.
- [x] `RobotContainerAutonomousModeSchedulingTest`: 17/17 passed.
- [x] `SwerveSimulationIntegrationTest`: 2/2 passed.
- [x] Full regression: 373 tests passed; 0 failures, errors, or skips.
- [x] Clean build passed.
- [x] Simulation verified heading capture, pose validity, accepted reset,
  production dispatch, tolerance completion, immediate disable stop, no
  restart, and fresh-reset repeatability.
- [x] Driver Station / Glass verified NT4 telemetry and Field2d.
- [x] Real robot verified accepted-reset motion, tolerance completion, safe
  disable, no-reset refusal, no automatic restart, and repeatability.
- [x] Transition guide finalized.
- [ ] User Git commit.
- [ ] User Git push.

## Completion Interpretation and Exclusions

- [x] Target is `(0.40 m, 0.00 m, 0 deg)` and tolerance is `0.030 m`.
- [x] Stopping near X=`0.370 m` is within the locked translation tolerance,
  not a 3 cm accuracy defect.
- [x] No trajectory generation/sampling/following entered L02.
- [x] No PathPlanner, AutoBuilder, alliance transform, vision/AprilTag,
  multi-waypoint, or mechanism-event capability entered L02.
- [x] No drivetrain retuning, hardware, IO, telemetry, observation,
  kinematics/output-pipeline, Gradle, or vendordep change entered L02.
- [x] A01_L03 and later remain unstarted and unauthorized.
