# A01_L01 Autonomous Starting-Pose and Field-Frame Contract - Lesson Plan

## Lesson Metadata

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract`
- Previous lesson: `A00_L04_AutonomousMotionSafetyGating` - `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active state: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Architecture Review: `PASS`
- Implementation: `COMPLETE`
- Baseline Build: `PASS - Java 17 inherited baseline verified`
- Build: `PASS - clean build`
- Java verification: `VERIFIED - Java 17`
- Full tests: `PASS`
- Full build: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `PASS`
- Real robot: `PASS` for the supplied A01_L01 starting-pose and field/reference-frame evidence only
- Transition Guide: `FINAL / PASS`
- Known Issues: `NON-BLOCKING FOLLOW-UP - temporary E-Stop and CommandScheduler loop-overrun observations; no A01_L01 defect established`
- Git: user-owned; not run by Codex

## Single Learning Concept

Authoritative autonomous reference-frame initialization from a validated
starting pose and heading.

## Prerequisite and Ownership

A01_L01 inherits frozen A00_L04 and the S00_L24 pose/localization contract.
The autonomous composition layer owns the starting-pose procedure. The
subsystem remains responsible for localization state and centralized stop.
`RobotContainer` may construct and compose the approved commands but remains
free of hardware logic and autonomous business logic.

## Locked Safety Boundary

The A00_L04 invariant remains mandatory: nonzero autonomous drivetrain motion
is permitted only while `DriverStation.isAutonomousEnabled() == true`.
Invalid, unavailable, or rejected starting-pose state must not authorize
autonomous motion. Failure remains centralized through `SwerveSubsystem.stop()`.

## A01_L01 Scope

The lesson establishes valid pose availability, field-frame/reference-frame
initialization, the Disabled-only reset boundary, and refusal to begin without
a usable starting pose.

It does not add pose-target control, trajectories, alliance transforms,
PathPlanner, AutoBuilder, vision, mechanism events, multi-step routines, or
competition tuning.

## Real-Robot Verification Evidence

The reset command was exposed as `ResetKnownFieldPoseCommand`. The seven
supplied A01_L01 real-robot cases passed within the starting-pose and
field/reference-frame contract:

1. **Disabled baseline:** zero drivetrain output; hardware connected and
   healthy; localization initially unavailable after reboot until the
   field-reference/reset procedure completed.
2. **Autonomous Enabled without a fresh accepted reset:** no autonomous motion
   for approximately 7.6 seconds; fail-closed behavior confirmed.
3. **Disabled known starting-pose reset:** Pose and EstimatedPose became
   available/valid at approximately X=0.0 m, Y=0.0 m, heading approximately
   0 degrees; raw gyro yaw remained around 39.8 degrees; no drivetrain motion.
4. **Fresh accepted reset -> Autonomous:** one bounded run; final EstimatedPose
   approximately X=+0.369 m, Y=+0.003 m, heading approximately +0.08 degrees;
   motion stopped and did not restart while Autonomous remained enabled to
   approximately 10.6 seconds.
5. **Second Autonomous enable without another reset:** no second motion; pose
   remained approximately X=+0.369 m; one-shot authorization/consumption
   confirmed.
6. **New Disabled reset -> new Autonomous session:** one fresh reset
   authorized one new bounded run; motion completed without restart while
   Autonomous remained enabled.
7. **Reset attempt while Teleoperated Enabled:** reset was safely
   rejected/blocked; Pose and EstimatedPose remained Available=true and
   MeasurementSampleValid=true, X/Y remained approximately 0, heading showed
   only tiny normal drift, and the robot remained stationary.

Field visualization updated consistently, and transition through robot modes
was tested. Gyro and module
connectivity/configuration health remained valid.

Driver Station / Glass verification also passed: disabled runtime telemetry,
valid observable Pose and EstimatedPose, runtime pose update after movement,
observable Disabled starting-pose reset, and safe zero drivetrain output when
expected.

Test-context observations are retained: a temporary Driver Station Spacebar
E-Stop was followed by a reboot and successful resumption, and
CommandScheduler loop-overrun observations require follow-up evidence. Neither
is classified as an A01_L01 defect.

This evidence proves only the A01_L01 validated starting-pose and
field/reference-frame initialization scope. It does not claim A01_L02 or
later, trajectory/path following, PathPlanner, AutoBuilder, vision
localization, or competition readiness.

The current source and tests remain untouched by this documentation
finalization.
