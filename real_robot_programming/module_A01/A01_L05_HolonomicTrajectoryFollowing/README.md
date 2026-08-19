# A01_L05 - Holonomic Trajectory Following

## Lesson State

- Module: A01 - Autonomous Navigation and Path Following
- Previous lesson: A01_L04_FieldAndAllianceTransformContract - COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE
- Active State: COMPLETE / FROZEN / READ-ONLY
- Freeze State: FROZEN
- Inheritance baseline: PASS - user verified L05 source byte-identical to frozen L04 and baseline build successful.
- Architecture Audit: PASS.
- Design-Lock Blocker Resolution: PASS.
- Architecture Review: PASS.
- Implementation: COMPLETE.
- Verification: PASS - focused L05 tests, inherited L01-L04 regression, full
  regression, and clean build passed under WPILib Java 17.
- Simulation: PASS - user-supplied Blue and Red autonomous evidence.
- Real Robot: PASS - user-supplied USB/radio health, both-alliance reset,
  robot-on-blocks, Disable-stop, and Blue/Red real-field trajectory evidence.
- Transition Guide: FINAL / PASS.
- Git Commit / Push: NOT TESTED - user-owned; Git was not run by Codex.

## Locked Concept

```text
canonical L03 trajectory
        -> one L04 FieldAllianceTransform
        -> execution trajectory and execution start pose
        -> Disabled-only accepted AutonomousStartContext
        -> one-shot autonomous readiness consumption
        -> HolonomicDriveController
        -> robot-relative ChassisSpeeds
        -> SwerveSubsystem.acceptChassisSpeeds(...)
```

`AutonomousStartContext` binds the explicit `FieldVariant`, definite
`Alliance`, and transformed execution start `Pose2d`. Unknown alliance creates
no context and therefore no autonomous motion. The transform utility remains
pure and is applied exactly once under RobotContainer composition ownership.

The learning path uses fixed canonical holonomic heading `0°`; that heading is
transformed exactly once with the trajectory for Red. A trajectory state pose
rotation remains path tangent/path geometry, not the robot's holonomic-heading
target.

## Preserved and Deferred Scope

L01 field-heading and Disabled known-pose reset; L02 estimator-validity,
one-shot readiness, Disable stop, centralized stop, and no-restart semantics;
L03 generation/sampling; and L04 canonical-frame/transform ownership remain
preserved.

L05 excludes PathPlanner, AutoBuilder, replanning, vision, AprilTags,
NamedCommands, event markers, mechanisms, hardware/IO changes, telemetry
changes, drivetrain retuning, and Frozen Backbone/interface changes.

## Final Verification Record

- Focused L05 tests: 32/32 passed.
- Focused inherited L01-L04 tests: 57/57 passed.
- Full regression: 401/401 passed; zero failures, errors, or skips.
- Clean build: PASS; all seven reported tasks executed.
- Authoritative installed drive ratio: `6.75:1`, established by repeated
  physical 20-motor-rotation / wheel-rotation tests and used by Constants and
  the CTRE drive conversion.
- Selected execution contract: `REBUILT_WELDED`; Blue start/end
  `(0.000, 0.000, 0 deg)` / `(1.000, 0.000, 0 deg)`; Red start/end
  `(16.541, 8.069, 180 deg)` / `(15.541, 8.069, 180 deg)`; generated duration
  `2.140401179598 s`; hard timeout `5.140401179598 s`.

A01_L05 is complete, frozen, and read-only. A01_L06 has not been started.
