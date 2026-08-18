# A01_L02 - Pose-Targeted Autonomous Motion

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Previous lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture Review, Architecture Audit, and Design Lock: `PASS`
- Implementation, tests, Simulation, Driver Station / Glass, and Real Robot: `PASS`
- Git commit and push: user-owned; `NOT TESTED`

## What L02 Adds

L02 adds one finite, field-relative pose-target motion primitive. The command
uses the inherited estimated pose, computes field-frame X/Y and wrapped-heading
error, applies bounded proportional control, and sends field-relative chassis
speeds to the existing `SwerveSubsystem` boundary. The subsystem owns frame
conversion, localization, hardware actuation, and safe stop.

The command runs only while Autonomous Enabled, has a 4 second timeout, and
fails closed for invalid runtime feedback/time or mode loss. It stops on every
terminal path. Translation and heading tolerance suppression is per cycle;
completion requires both tolerances together.

The L01 Disabled-only field-heading capture and accepted known-pose reset
remain prerequisites. Their readiness token permits one autonomous session
only. A reset is required for another session; re-enabling Autonomous does not
restart motion.

## Configuration and Result

The learning target is `(0.40 m, 0.00 m, 0 deg)` from `(0,0,0)`, with a
translation tolerance of `0.030 m`. Verified Simulation and real-robot runs
stopped near X=`0.370 m`, which is expected and within the completion contract.

See [the transition guide](docs/A01_L01_to_A01_L02_Step_by_Step.md) and
[verification guide](docs/A01_L02_Pose_Targeted_Autonomous_Motion_Verification_Guide.md).

## Deferred Scope

Trajectory generation/following, PathPlanner, AutoBuilder, alliance
transforms, vision/AprilTags, multi-waypoint routines, mechanism events,
drivetrain retuning, and new localization architecture are outside L02 and
remain deferred to later authorized lessons.
