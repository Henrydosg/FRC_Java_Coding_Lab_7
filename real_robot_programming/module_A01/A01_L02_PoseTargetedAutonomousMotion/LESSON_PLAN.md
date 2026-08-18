# A01_L02 Pose-Targeted Autonomous Motion - Lesson Plan

## Completion State

- Lesson: `A01_L02_PoseTargetedAutonomousMotion`
- Previous lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture Audit and Design Lock: `PASS`
- Implementation, tests, Simulation, Driver Station / Glass, and Real Robot: `PASS`
- Transition Guide: `FINAL / PASS`
- Git commit and push: user-owned; `NOT TESTED`

## Completed Single Concept

L02 added one scheduler-managed `PoseTargetedAutonomousMotionCommand` for
closed-loop travel toward one finite field-relative target pose. It reads
`SwerveSubsystem.getEstimatedPose()`, computes X/Y/wrapped-heading error,
uses bounded proportional control, and calls
`acceptFieldRelativeChassisSpeeds(...)`. Frame conversion remains in
`SwerveSubsystem`.

The command uses per-cycle translation and heading suppression, completes only
when both tolerances are satisfied, applies a 4 second timeout, and fails
closed on invalid runtime state or Autonomous-mode loss. Every terminal path
uses centralized stop. The inherited one-shot accepted-reset readiness gate
remains mandatory and prevents automatic restart.

## Completed Configuration and Verification

The implemented target is `(0.40 m, 0.00 m, 0 deg)` from `(0,0,0)`, with
translation/heading kP `1.0 s^-1`, translation limit `0.20 m/s`, angular
limit `0.35 rad/s`, translation tolerance `0.030 m`, heading tolerance
`2.0 deg`, and timeout `4.0 s`.

The recorded evidence is 10/10 focused command tests, 17/17 scheduler tests,
2/2 simulation integration tests, 373/373 full regression tests, and a clean
build. Simulation and real-robot verification confirmed normal completion near
X=0.370 m, which is valid at the configured 0.030 m tolerance, safe disable,
one-shot readiness, no automatic restart, and repeatability.

## Preserved and Deferred Scope

L01 localization/reference initialization and all frozen subsystem, IO,
observation, telemetry, hardware, and drivetrain contracts were preserved.
L03+ work remains deferred: trajectories, PathPlanner, AutoBuilder, alliance
transforms, vision/AprilTags, multi-waypoint logic, and mechanism events are
not L02 capabilities.
