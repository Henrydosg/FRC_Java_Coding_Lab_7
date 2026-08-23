# A01_L07 - AutoBuilder Contract Integration - Plan and Activation Record

## Activation State

- Lesson: `A01_L07_AutoBuilderContractIntegration`.
- Title: `A01_L07 - AutoBuilder Contract Integration`.
- Previous lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration - COMPLETE / FROZEN / READ-ONLY`.
- Status: `COMPLETE / FROZEN / READ-ONLY`.
- Strict inheritance: `PASS` - copied from frozen L06.
- User baseline: `PASS` - compileJava, compileTestJava, tests, and clean build.
- Architecture Review: `PASS`.
- Alliance-transform Design Lock: `PASS`.
- Production implementation: `PASS` - approved contract adapter, execution-path
  factory, trajectory-adapter exposure, and RobotContainer wiring are complete.
- Simulation: `PASS / USER-SUPPLIED` - Blue/Red execution, exactly-one transform,
  pose validity, heading stability, disable stop, and no automatic restart.
- Real Robot: `PASS / USER-CONFIRMED` - the user confirmed physical execution
  of the current L07 AutoBuilder Contract Integration lesson.

Activation and the approved implementation boundary are complete. No chooser,
multiple routines, NamedCommands, event markers, asset, IO, hardware,
SwerveSubsystem, CTRE, or frozen predecessor change was made.

## One New Concept

Configure AutoBuilder against the repository's existing pose, reset,
measured-speed, output, PathFollowingController, RobotConfig, requirement,
alliance, and fail-closed lifecycle contracts. AutoBuilder is an integration
boundary and does not become the owner of drivetrain, localization, hardware,
IO, telemetry, or alliance transformation.

## Locked Alliance and Execution Contract

`A01/L04 FieldAllianceTransform` remains the sole alliance-transform owner.
Automatic AutoBuilder flipping is disabled: `shouldFlipPath = false` and the
fresh execution path has `preventFlipping = true`.

```text
canonical Blue PathPlannerPath
    -> L04 transformation exactly once
    -> fresh transformed execution PathPlannerPath
    -> AutoBuilder follows directly with vendor flipping disabled
```

The canonical path is never mutated. A second transformation is forbidden.

## Preserved Pre-Activation Design Contracts

The copied pre-activation record remains the detailed design reference. L07
must preserve:

1. the `Optional<Pose2d>` and `Optional<ChassisSpeeds>` to vendor callback
   mismatch through explicit, fail-closed bridges;
2. finite pose reset and robot-relative output validation;
3. measured robot-relative speed ownership by `SwerveSubsystem`;
4. exactly-once process-session AutoBuilder configuration;
5. named Constants-owned RobotConfig reuse, including provisional-value labels;
6. the existing `SwerveSubsystem` scheduler requirement;
7. a monotonic session fault latch;
8. unconditional terminal stop on success, interruption, cancellation, and
   fault; and
9. no automatic restart after mode loss or cancellation.

## Current Swerve Authority

- Drive ratio: `6.75:1`.
- FL CANcoder offset: `+0.068603515625`.
- FR CANcoder offset: `+0.014404296875`.
- BL CANcoder offset: `+0.46240234375`.
- BR CANcoder offset: `-0.057373046875`.

No CAN IDs, steer ratio, wheel radius, wheelbase, trackwidth, inversion,
CANcoder direction, current limits, PID/FF, FOC, IO, or SwerveSubsystem values
may change in the activation phase.

## Implementation Result

The approved implementation added only the single AutoBuilder contract concept:

1. `AutoBuilderContractAdapter` configures the verified Consumer overload exactly
   once and owns the fail-closed callback bridges.
2. `PathPlannerExecutionPathFactory` validates the canonical Blue path and
   creates a fresh exactly-once L04-transformed execution path with
   `preventFlipping = true`.
3. `RobotContainer` remains the composition root and obtains the path command
   through `AutoBuilder.followPath(...)`; `SwerveSubsystem` remains the
   requirement and centralized-stop owner.
4. Focused L07 tests, the full 424-test regression, and a clean build passed.

The implementation preserves the finite pose/reset/speed/output contracts,
monotonic fault latch, terminal stop, no automatic restart, shared RobotConfig,
and no-second-flip policy. It does not add chooser, multiple routines,
NamedCommands, event markers, mechanism coordination, vision, AprilTags,
pathfinding, or replanning.

Simulation passes from user-supplied evidence, including the Red geometry and
mode-loss checks. The user-owned real-robot gate is now also confirmed PASS.
L07 is COMPLETE / FROZEN / READ-ONLY. No exact endpoint-accuracy, final
PID/feedforward, or final physical-model characterization claim is made.

## Simulation Evidence Reconciliation

The user supplied the following evidence for the current L07 implementation:

1. Blue autonomous: PASS. Disabled heading reference and known starting-pose
   readiness were established, and the known one-meter path executed.
2. Red autonomous: PASS. Final EstimatedPose was `(15.535553 m, 8.069000 m,
   -180.000000 deg)`, consistent with the single L04 transform for the
   `REBUILT_WELDED` field.
3. Disable/mode-loss stop: PASS. Disabling during Blue motion stopped the
   robot near `(0.400765 m, 0 m, 0 deg)`.
4. No automatic restart: PASS. Re-enable without BACK, another reset, or fresh
   readiness did not restart motion.

This is Simulation and telemetry evidence, not real-robot evidence. Exact
endpoint accuracy remains unclaimed; the small Red X difference is not a
precision characterization.

## Final Closure

- User-confirmed L07 Real Robot: `PASS`.
- All A01_L07 completion gates: `PASS`.
- Transition Guide: `FINAL / PASS`.
- L07 state: `COMPLETE / FROZEN / READ-ONLY`.
- A01_L08 remains not created; no next-lesson implementation was started.
