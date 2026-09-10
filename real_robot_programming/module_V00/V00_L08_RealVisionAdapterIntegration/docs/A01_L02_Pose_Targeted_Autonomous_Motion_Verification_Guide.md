# A01_L02 Pose-Targeted Autonomous Motion - Verification Guide

## Purpose and State

This guide records the completed L02 learning and verification procedure. L02
is `COMPLETE / FROZEN / READ-ONLY`; it is not a trajectory, PathPlanner, or
competition-autonomous lesson.

## A. Architecture

```text
Autonomous session
  -> one-shot accepted-reset readiness gate
  -> PoseTargetedAutonomousMotionCommand
  -> SwerveSubsystem
  -> field-relative to robot-relative conversion
  -> SwerveModuleIO
  -> hardware or simulation IO
```

`RobotContainer` composes the command tree. The pose-target command requires
`SwerveSubsystem`, reads `getEstimatedPose()`, and sends only field-relative
`ChassisSpeeds`. `SwerveSubsystem` owns localization, conversion, final module
states, IO dispatch, and centralized `stop()`.

## B. Why the Known-Pose Reset Is Required

The command must not move from an unknown or stale autonomous pose. A Disabled
accepted `Reset Known Starting Pose` request resets both subsystem-owned pose
trackers to the finite learning start pose and creates one readiness token.
At autonomous dispatch, that token is consumed. Without it, the composition
uses its stop-only branch and produces no autonomous motion.

## C. Four Different Concepts

| Concept | Meaning |
|---|---|
| Field-heading capture | Xbox Back/View captures the current valid raw gyro yaw as field-heading zero while Disabled. |
| Known-pose reset | SmartDashboard/Glass `Reset Known Starting Pose` requests the finite `(0,0,0 deg)` localization reset while Disabled. |
| Autonomous readiness | A successful reset creates one consumable authorization for one autonomous session. It is not pose telemetry. |
| Estimated-pose feedback | The L02 controller reads the valid subsystem estimate to calculate field-frame target error every cycle. |

## D. Simulation Procedure

1. Start the L02 simulation and keep Driver Station Disabled.
2. Wait for a complete gyro/module refresh.
3. Point the simulated robot at the desired field-zero direction.
4. Press Xbox Back/View once to capture the field-heading reference.
5. Confirm `Swerve/Pose/Available`, `Swerve/EstimatedPose/Available`, and
   both measurement-valid fields are true.
6. While still Disabled, invoke `Reset Known Starting Pose` in SmartDashboard
   or Glass.
7. Confirm the displayed pose and estimate are approximately `(0,0,0 deg)`.
8. Select Autonomous and enable. Confirm a single run toward X=`0.40 m`.
9. Confirm stop near X=`0.370 m`, Y near `0`, and heading near `0 deg`.
10. Disable during a separate run; confirm immediate zero drivetrain output.
11. Re-enable Autonomous without another accepted reset; confirm no restart.
12. Return to Disabled, perform a fresh accepted reset, and confirm one new
    session is permitted.

## E. Real-Robot Procedure

1. Establish a safe clear area and immediate Disable readiness.
2. Connect Driver Station/Glass and confirm gyro/module health plus full NT4
   telemetry and Field2d visibility.
3. Keep the robot Disabled, capture the intended field-heading zero with
   Xbox Back/View, and confirm pose/estimated-pose validity.
4. Invoke `Reset Known Starting Pose` while Disabled and confirm the reset is
   accepted before enabling Autonomous.
5. Run one autonomous target session, observe only the intended forward
   movement, and confirm completion near X=`0.370 m`.
6. Repeat the disable-interrupt, no-restart, no-reset-refusal, and fresh-reset
   cases from the Simulation procedure.

The user supplied repeatable real-robot evidence for this complete procedure:
no unexpected strafe, rotation, unsafe restart, or required drivetrain retune
was reported.

## F. Safety Behavior

- Disable or Autonomous-mode loss fails closed and stops immediately.
- The readiness token is one-shot; it is consumed by autonomous dispatch.
- No accepted fresh reset means no autonomous motion.
- Re-enabling Autonomous does not restart a completed or interrupted session.
- Invalid/nonfinite feedback, invalid time, timeout, and invalid control output
  also stop centrally.

## G. Target and Tolerance Interpretation

The target X is `0.400 m`; the configured translation tolerance is `0.030 m`.
The controller may suppress translation and complete when the translation error
is less than or equal to that tolerance. A final X near `0.370 m` therefore
leaves approximately `0.030 m` error and is valid by design. It is not a 3 cm
accuracy defect.

## H. What L02 Teaches and What Remains Deferred

L02 teaches a single bounded field-relative pose-target primitive: estimate
feedback, pose error, P control, vector limiting, tolerance completion,
timeout, scheduler ownership, and fail-closed stop.

L03+ remains responsible for trajectory generation/sampling/following,
alliance transforms, PathPlanner, AutoBuilder, routine selection, event
markers, vision/AprilTags, and broader autonomous architecture. L02 does not
change drivetrain gains, IO, hardware, observation/telemetry contracts, or the
Frozen Backbone/interface.
