# A01_L05 - Holonomic Trajectory Following - Final Plan Record

## Activation State

- Lesson: A01_L05_HolonomicTrajectoryFollowing
- Previous Lesson: A01_L04_FieldAndAllianceTransformContract - COMPLETE / FROZEN / READ-ONLY
- Status: COMPLETE / FROZEN / READ-ONLY
- Inheritance baseline, directory identity, Architecture Audit, and Design-Lock Blocker Resolution: PASS.
- Implementation: COMPLETE.
- Verification: PASS - 32 focused L05 tests, 57 focused inherited L01-L04
  tests, 401 full-regression tests, and the final clean build passed.
- Runtime validation: PASS - user-supplied both-alliance Simulation,
  robot-on-blocks, communication/health, reset, Disable-stop, and both-alliance
  real-field evidence.

## Locked Single Concept

Follow one L03-generated, L04-transformed execution trajectory using WPILib
`HolonomicDriveController` and existing estimator feedback. The command emits
bounded robot-relative `ChassisSpeeds` only through
`SwerveSubsystem.acceptChassisSpeeds(...)`.

Before that follower may run, a Disabled-only alliance-aware reset creates one
accepted immutable `AutonomousStartContext` containing the selected
`FieldVariant`, definite `Alliance`, and transformed execution-start `Pose2d`.
Unknown alliance produces no context and fails closed.

## Locked Control and Safety Contract

- Canonical trajectory and fixed canonical holonomic heading `0°` are transformed exactly once under RobotContainer ownership.
- The trajectory state rotation is path tangent, not the holonomic-heading target.
- Translation is vector-magnitude bounded; omega is independently bounded.
- Completion requires trajectory time elapsed and simultaneous translation/heading tolerance satisfaction.
- Hard timeout is trajectory total duration plus `3.0 s`.
- `end(...)` always invokes centralized `SwerveSubsystem.stop()`.
- Invalid pose/observation/time/output, mode loss, missing context, unknown alliance, or failed provenance/start-pose validation stop safely.
- L01/L02 one-shot readiness and no-automatic-restart behavior remain preserved.

## Implemented Minimum Delta

- `Constants.java`: L05 configuration and one explicit immutable selected field variant.
- `RobotContainer.java`: L05 composition only; no subsystem logic.
- `commands/AutonomousStartContext.java`: immutable readiness provenance.
- `commands/AllianceAwareAutonomousStartPoseResetCommand.java`: Disabled-only reset/readiness command.
- `commands/HolonomicTrajectoryFollowingCommand.java`: bounded follower.
- `commands/KnownFieldPoseResetDashboard.java`: accept the new reset command as a `Command` while preserving the dashboard action.
- Focused command/reset tests; scheduler test update; simulation integration extension.

No SwerveSubsystem, IO, observation, telemetry, hardware, dependency, PathPlanner, AutoBuilder, or Frozen Lesson change belongs to this plan.

## Closure

All locked control, safety, provenance, verification, documentation, and
real-robot acceptance gates are satisfied. The previous Real Robot HOLD is
cleared by the supplied authorized evidence. This plan is final; L05 is frozen
and read-only, and L06 is not started.
