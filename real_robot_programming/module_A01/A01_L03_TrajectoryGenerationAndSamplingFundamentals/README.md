# A01_L03 - Trajectory Generation and Sampling Fundamentals

## Lesson State

- Module: A01 - Autonomous Navigation and Path Following
- Previous lesson: A01_L02_PoseTargetedAutonomousMotion - COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Freeze State: FROZEN / READ-ONLY
- Architecture Audit and Design Lock: PASS
- Implementation: COMPLETE
- Local verification: PASS - user-supplied final result: PASS - A01_L03 FULL LOCAL VERIFICATION COMPLETE.
- Git commit and push: user-owned; NOT TESTED

## What L03 Adds

L03 adds one pure WPILib trajectory learning foundation:

Start Pose + interior Translation2d waypoint(s) + Goal Pose
→ TrajectoryGenerator
→ time-parameterized Trajectory
→ trajectory.sample(t)
→ Trajectory.State

LearningTrajectoryFactory creates a fresh native Trajectory from the existing L01
learning start (0.00 m, 0.00 m, 0 deg), interior waypoint (0.50 m, 0.25 m),
goal (1.00 m, 0.00 m, 0 deg), maximum velocity 1.0 m/s, and maximum
acceleration 1.0 m/s².

The factory validates unusable generated output and returns the native
Trajectory. Trajectory.State pose rotation describes path geometry; it is not a
holonomic robot-heading profile.

## Verification and Hold State

User-supplied local verification reports compileTestJava PASS,
LearningTrajectoryFactoryTest PASS, full regression PASS, and clean build PASS
with BUILD SUCCESSFUL in 44s and 7 actionable tasks executed. No test count is
claimed because none was supplied.

The L03 simulation gate is satisfied by non-actuating local deterministic
trajectory generation and sampling verification. Driver Station / Glass is not
applicable because L03 adds no runtime telemetry or mode behavior. Real Robot
remains HOLD under the A01 ADR: L03 authorizes no physical actuation or
real-robot test.

## Explicit Exclusions

L03 adds no trajectory follower, holonomic controller, ChassisSpeeds,
drivetrain motion, RobotContainer or SwerveSubsystem change, PathPlanner,
AutoBuilder, alliance transform, vision, replanning, event marker, mechanism
event, telemetry, IO, hardware change, drivetrain tuning, or Frozen
Backbone/interface change.

See docs/A01_L02_to_A01_L03_Step_by_Step.md for the final transition record.
