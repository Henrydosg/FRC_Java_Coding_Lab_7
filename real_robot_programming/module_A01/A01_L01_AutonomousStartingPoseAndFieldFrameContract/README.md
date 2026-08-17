# A01_L01 - Autonomous Starting-Pose and Field-Frame Contract

## Lesson State

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
- Real Robot: `PASS` for the supplied A01_L01 starting-pose and field/reference-frame evidence only
- Transition Guide: `FINAL / PASS`
- Known Issues: `NON-BLOCKING FOLLOW-UP - temporary E-Stop and CommandScheduler loop-overrun observations; no A01_L01 defect established`
- Git: user-owned; not run by Codex

A01_L01 is the first lesson in the approved A01 roadmap. It inherits the
published, frozen A00_L04 project and introduces one new architectural concept
under the approved A01 ADR.

## Authorized Lesson Concept

Authoritative autonomous reference-frame initialization from a validated
starting pose and heading.

The lesson establishes the contract needed before autonomous motion may rely
on a known field pose. A usable starting pose must be available and valid
before autonomous motion is permitted to begin.

The inherited A00_L04 safety invariant remains authoritative:

> Nonzero autonomous drivetrain motion is permitted only while
> `DriverStation.isAutonomousEnabled() == true`. Otherwise autonomous motion
> must fail closed through centralized drivetrain stop.

## Inherited Architecture

The Frozen Backbone remains:

```text
XboxController -> controls -> commands -> SwerveSubsystem -> SwerveModuleIO -> hardware
```

The inherited observation path remains:

```text
hardware or simulation IOInputs
-> SwerveSubsystem / estimator
-> immutable observation
-> telemetry
-> NT4 / Glass / log
```

`SwerveSubsystem` remains the owner of localization and actuation.
`SwerveSubsystem.stop()` remains the centralized stop authority. `RobotContainer`
remains the composition root only. Frozen A00_L04, S00_L24, the Frozen
Backbone, and the Frozen Interface Contract remain unchanged.

## Scope Boundary

A01_L01 is limited to the starting-pose and field-frame contract. Its
governed verification must cover valid and invalid pose availability, the
Disabled-only reset boundary, and refusal to begin without a usable pose.

The following are outside A01_L01:

- pose-targeted autonomous motion;
- trajectory generation or sampling;
- holonomic trajectory following;
- alliance mirroring or transforms;
- PathPlanner or AutoBuilder;
- vision or AprilTags;
- mechanism events or multi-step routines;
- final competition autonomous architecture or tuning.

No A01_L02 or later capability is claimed by this identity normalization.

## Real-Robot Verification Evidence

The reset command was exposed as `ResetKnownFieldPoseCommand`. The
user-supplied A01_L01 real-robot evidence is recorded as `PASS` for the
validated autonomous starting-pose and field/reference-frame contract only:

1. **Disabled baseline:** The drivetrain produced zero output and hardware was
   connected and healthy. Localization was initially unavailable after reboot
   until the field-reference/reset procedure was completed.
2. **Autonomous Enabled without a fresh accepted reset:** No autonomous motion
   occurred for approximately 7.6 seconds. Fail-closed behavior was confirmed.
3. **Disabled known starting-pose reset:** The reset established available and
   valid Pose and EstimatedPose at approximately X=0.0 m, Y=0.0 m, and heading
   approximately 0 degrees. Raw gyro yaw remained around 39.8 degrees,
   confirming field-frame/reference separation. The drivetrain did not move.
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

The final runtime verification passed:

- Disabled runtime telemetry was observable.
- Pose and EstimatedPose were observable and valid.
- Runtime pose updated after movement.
- Disabled starting-pose reset was observable in Glass.
- The drivetrain remained safe and at zero output when expected.

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

The current source and tests are unchanged by this documentation amendment.

## Current State

A01_L01 is the completed frozen lesson:

`COMPLETE / FROZEN / READ-ONLY`

The lesson is now frozen and read-only.
