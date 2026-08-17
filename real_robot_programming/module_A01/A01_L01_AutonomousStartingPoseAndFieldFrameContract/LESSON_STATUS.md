# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L01_AutonomousStartingPoseAndFieldFrameContract`
- Previous Lesson: `A00_L04_AutonomousMotionSafetyGating`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: establish the validated autonomous starting-pose and field-frame contract.
- Architecture Review: `PASS`
- Implementation: `COMPLETE`
- Baseline Build: `PASS - Java 17 inherited baseline verified`
- Build: `PASS - clean build`
- Java Verification: `VERIFIED - Java 17`
- Full Tests: `PASS`
- Full Build: `PASS`
- Simulation: `PASS`
- Driver Station / Glass: `PASS`
- Real Robot: `PASS` - supplied A01_L01 starting-pose and field/reference-frame evidence
- Transition Guide: `FINAL / PASS`
- Known Issues: `NON-BLOCKING FOLLOW-UP - temporary Driver Station Spacebar E-Stop and CommandScheduler loop-overrun observations; no A01_L01 defect established`
- Git Commit: `NOT TESTED` - user-owned; Git not run by Codex
- Git Push: `NOT TESTED` - user-owned; Git not run by Codex

## Authorized Concept

Authoritative autonomous reference-frame initialization from a validated
starting pose and heading.

The prerequisite is frozen A00_L04 and inherited S00_L24 pose infrastructure.
The A00_L04 Autonomous+Enabled safety invariant and centralized
`SwerveSubsystem.stop()` authority remain mandatory.

## Inherited Boundary Preservation

- Frozen A00_L04 remains unchanged and read-only.
- The Frozen Backbone and Frozen Interface Contract remain unchanged.
- `RobotContainer` remains the composition root only.
- Localization and actuation remain owned by `SwerveSubsystem`.
- No A01_L02 or later capability is included.

## Verification Record

| Gate | Status | Evidence |
|---|---|---|
| A01 ADR and lesson identity | PASS | Approved A01 roadmap |
| Frozen predecessor | PASS | A00_L04 governance record |
| Inherited baseline | PASS | Java 17 baseline verified |
| Java verification | VERIFIED | Java 17 evidence supplied |
| Focused deterministic tests | PASS | Focused tests passed |
| Full regression and clean build | PASS | Full tests and clean build passed |
| Simulation | PASS | Simulation evidence supplied |
| Driver Station / Glass | PASS | Disabled telemetry, valid poses, reset visibility, runtime pose update, and safe zero output observed |
| Real Robot | PASS | Supplied A01_L01 starting-pose and field/reference-frame evidence |
| Transition Guide | FINAL / PASS | A00_L04 -> A01_L01 guide finalized |

## Real-Robot Verification Evidence

The reset command was exposed as `ResetKnownFieldPoseCommand`. The seven
supplied A01_L01 real-robot cases passed within the validated autonomous
starting-pose and field/reference-frame contract:

1. **Disabled baseline:** The drivetrain produced zero output and hardware was
   connected and healthy. Localization was initially unavailable after reboot
   until the field-reference/reset procedure was completed.
2. **Autonomous Enabled without a fresh accepted reset:** No autonomous motion
   occurred for approximately 7.6 seconds. Fail-closed behavior was confirmed.
3. **Disabled known starting-pose reset:** Pose and EstimatedPose became
   available/valid at approximately X=0.0 m, Y=0.0 m, and heading approximately
   0 degrees. Raw gyro yaw remained around 39.8 degrees, confirming
   field-frame/reference separation. The drivetrain did not move.
4. **Fresh accepted reset -> Autonomous:** One bounded autonomous run
   occurred. Final EstimatedPose was approximately X=+0.369 m, Y=+0.003 m,
   heading approximately +0.08 degrees. Motion completed, the drivetrain
   stopped, and motion did not restart while Autonomous remained enabled to
   approximately 10.6 seconds.
5. **Second Autonomous enable without another reset:** No second motion
   occurred. Pose remained approximately X=+0.369 m. One-shot
   authorization/consumption behavior was confirmed.
6. **New Disabled reset -> new Autonomous session:** A fresh reset authorized
   one new bounded autonomous run. Motion completed and did not restart while
   Autonomous remained enabled.
7. **Reset attempt while Teleoperated Enabled:** Driver Station was visibly
   Teleoperated Enabled. The reset invocation did not corrupt or jump
   localization. Pose and EstimatedPose remained Available=true and
   MeasurementSampleValid=true; X/Y remained approximately 0 and heading
   showed only tiny normal drift. The robot remained stationary. The reset was
   safely rejected/blocked while Enabled.

Field visualization updated consistently during the verified reset and mode
transitions, and transition through robot modes was tested. Gyro
connection/configuration and module
connectivity/configuration health remained valid during testing.

## Driver Station / Glass Verification Evidence

- Disabled runtime telemetry: PASS.
- Pose and EstimatedPose observable and valid: PASS.
- Runtime pose update after movement: PASS.
- Disabled starting-pose reset observable in Glass: PASS.
- Drivetrain safe and zero when expected: PASS.

### Test-Context Observations

- A temporary Driver Station Spacebar E-Stop occurred during testing. The
  robot was rebooted and testing resumed successfully. This is not classified
  as an A01_L01 defect.
- CommandScheduler loop-overrun observations occurred during testing. They
  remain follow-up observations only and are not classified as an A01_L01
  defect without further evidence.

This evidence proves only the A01_L01 validated autonomous starting-pose and
field/reference-frame initialization scope. It does not prove A01_L02 or later,
trajectory/path following, PathPlanner, AutoBuilder, vision localization, or
autonomous competition readiness.

## Scope Exclusions

Pose-targeted motion, trajectory generation, trajectory following, alliance
transforms, PathPlanner, AutoBuilder, vision, AprilTags, mechanism events,
multi-step routines, and competition readiness remain outside A01_L01.

## Current State

A01_L01 is `COMPLETE / FROZEN / READ-ONLY`. The lesson is frozen and read-only.
