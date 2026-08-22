# A01_L06 - PathPlanner Path and Runtime Integration

## Lesson State

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`
- Previous lesson: `A01_L05_HolonomicTrajectoryFollowing - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Inheritance baseline: `PASS` - the L06 project was copied from frozen L05; the
  inherited baseline build was user-verified `BUILD SUCCESSFUL`.
- PathPlanner compatibility gate: `PASS` - user supplied OpenJDK 17.0.16,
  PathPlannerLib `2026.1.2`, vendordep detection, clean, `compileJava`,
  `compileTestJava`, tests, and `BUILD SUCCESSFUL` evidence.
- Architecture Review: `PASS` - approved A01 ADR and Final Design-Lock.
- Final Design-Lock: `PASS` - the approved implementation boundary remains
  unchanged; separate implementation authorization was subsequently supplied.
- Implementation: `PASS` - the authorized minimum production, test, and path
  asset delta is present; the frozen L01-L05 source remains unchanged.
- Focused L06 tests: `PASS` - user supplied `18/18`.
- Inherited L01-L05 regression: `PASS` - user supplied PASS for the required
  direct regression set.
- Full test suite: `PASS` - user supplied PASS; no aggregate count is asserted
  here because it was not repeated in the current evidence.
- Clean build: `PASS` - user supplied PASS.
- Simulation: `PASS` - user supplied Blue canonical approximately `+1.0 m`
  motion and alliance-transformed approximately `1.0 m` opposite-direction
  motion.
- Driver Station / Glass: `PASS` for the supplied Simulation/Glass Known
  Starting Pose reset and valid EstimatedPose evidence.
- CAN hardware blocker: `CLOSED` - user supplied closure after physical
  repair/replacement.
- Real-Robot Execution Before Recalibration: `PASS / USER-CONFIRMED` - the L06
  one-meter PathPlanner autonomous was physically run on both Blue and Red and
  the user reported the one-meter behavior working; no unrecorded numerical
  accuracy is inferred.
- Post-Recalibration Real-Robot Verification: `PASS / USER-CONFIRMED` - after the
  latest Swerve zero-offset recalibration, the user physically executed the
  one-meter autonomous on both Blue and Red.
- Transition Guide: `FINAL / PASS` - the complete inheritance, implementation,
  verification, evidence-reconciliation, and closure history is recorded in
  `docs/A01_L05_to_A01_L06_Step_by_Step.md`.
- Git Commit / Push: `NOT TESTED` - user-owned; Codex does not run Git.
- Known Issues: the four RobotConfig learning values remain provisional and are
  not measured or final. The Blue run showed slight endpoint overshoot followed
  by a small closed-loop reverse correction and settling near the intended
  endpoint. Exact endpoint accuracy is not formally characterized; final PID/FF
  and physical-model tuning remain deferred.

## Lesson Purpose

Introduce one narrow PathPlanner path/runtime boundary by loading one known
`.path` asset and adapting its validated trajectory data into the frozen L05
holonomic follower and safety contract. L06 does not replace localization,
drivetrain ownership, the follower, or centralized stop behavior.

## Inherited L01-L05 Contracts

- The Frozen Backbone, Frozen Interface Contract, package responsibilities, and
  observation flow remain unchanged.
- `SwerveSubsystem` owns localization, actuation, mechanism state, and
  centralized `stop()` authority. IO and telemetry ownership remain unchanged.
- `RobotContainer` remains the composition root only.
- L01/L02 Disabled-only accepted starting-pose readiness, one-shot consumption,
  Autonomous+Enabled gating, fail-closed mode loss, and no automatic restart
  remain mandatory.
- L03 trajectory/state semantics, L04 canonical Blue frame and field variant,
  and L05 follower behavior remain frozen reference contracts.

## Locked L06 Boundary

The implemented learning path is one deterministic asset:

- Filename: `A01_L06_OneMeter_Forward.path`
- Deploy location: `src/main/deploy/pathplanner/paths/`
- Runtime key: `A01_L06_OneMeter_Forward`
- Frame: canonical Blue frame; start `(0.000 m, 0.000 m, 0 deg)` and end
  `(1.000 m, 0.000 m, 0 deg)` under the selected `REBUILT_WELDED` field variant.
- Constraints: maximum velocity `0.50 m/s`, maximum acceleration `1.00 m/s^2`,
  maximum angular velocity `0.75 rad/s`, and maximum angular acceleration
  `1.50 rad/s^2`.
- Activation itself created no asset; the asset was added during the later
  authorized implementation. It has no event markers,
  rotation targets, constraint zones, point-towards zones, replanning, or
  multiple routines.

PathPlanner is limited to path asset loading and trajectory generation/data
production. The narrow adapter validates PathPlanner states and passes an
equivalent finite WPILib trajectory to the unchanged L05
`HolonomicTrajectoryFollowingCommand`. PathPlanner does not own
`SwerveSubsystem`, localization, drivetrain actuation, safety, alliance policy,
or command requirements. AutoBuilder is not used.

### Exactly-One Alliance Transform

The path remains canonical Blue-frame. A01/L04 owns the Blue/Red
`FieldAllianceTransform` exactly once, including execution pose, path geometry,
velocity, and holonomic heading as required by the frozen contract. PathPlanner
flipping/mirroring is absent or disabled; no second transform is permitted in
the adapter, follower, IO, SwerveSubsystem, or telemetry.

## RobotConfig Classification

The following inherited hardware/configuration values are verified and are used
as the authoritative inputs by the current implementation:

- wheel radius `0.0508 m`
- drive gearing `6.75:1`
- drive motor `Kraken X60`
- one drive motor per module
- drive current limit `70 A`
- wheelbase `0.5461 m` and trackwidth `0.5461 m`
- module offsets `+/-0.27305 m` in the four module positions
- FOC disabled in the CTRE runtime path

The following exact label applies to every use of these four values:

**PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL**

- robot mass `45.0 kg`
- MOI `5.0 kg·m²`
- maximum drive velocity `4.0 m/s`
- wheel COF `1.0`

These values must never be described as verified, measured, calibrated, final,
or competition-authoritative. They require re-measurement/revalidation before
competition use.

## Fail-Closed and Verification Scope

Missing or malformed assets, trajectory-generation failure, null/nonfinite
states or timing, incompatible runtime/API, invalid mode, cancellation, and
timeout must fail closed. Every motion failure path ultimately preserves
centralized `SwerveSubsystem.stop()` authority and does not automatically
restart.

The focused tests, inherited regression, full test suite, clean build, and
Simulation gates are PASS from user-supplied evidence. Post-recalibration
Swerve configuration evidence is also PASS (`41/41`, zero failures). The
post-recalibration Blue and Red one-meter autonomous executions are also PASS
from user-supplied physical evidence. Exact endpoint accuracy is not claimed.

## Real-Robot Evidence Boundary

After the latest Swerve zero-offset recalibration, the user physically executed
the one-meter autonomous on both Blue and Red; both are recorded as PASS.
During the Blue run the robot appeared to travel slightly past the nominal
endpoint, then issued a small reverse correction and settled near the intended
endpoint. This is an observed behavior, not a proven architecture failure or
root-cause attribution.

Exact physical endpoint accuracy (including exact `1.000 m`) is not formally
measured and is not claimed. Final PID/feedforward and physical-model tuning are
deferred. RobotConfig mass `45.0 kg`, MOI `5.0 kg*m^2`, maximum drive velocity
`4.0 m/s`, and wheel COF `1.0` remain provisional learning/simulation values;
the configured mass is known to be higher than the user's current real-robot
mass estimate, but no single provisional value is proven to cause the observed
behavior. Eventual characterization may also involve measured MOI, wheel COF,
maximum velocity, follower PID/feedforward, trajectory acceleration constraints,
wheel traction, estimator behavior, and other physical/control factors; final
precision tuning is deferred to dedicated commissioning.

## Explicit Exclusions

AutoBuilder, chooser/multiple autos, NamedCommands, event markers, dynamic
replanning, pathfinding, vision/AprilTags, mechanism coordination, subsystem
or IO redesign, telemetry architecture changes, and any L01-L05 modification
are forbidden in L06. The existing official
`vendordeps/PathplannerLib.json` at version `2026.1.2` is preserved; activation
does not replace or upgrade it.

L06 is `COMPLETE / FROZEN / READ-ONLY`. Its production architecture is limited
to the approved narrow PathPlanner adapter and inherited L05 execution
boundary. Future L07 must inherit from this final L06 state; this README does
not activate L07 or authorize AutoBuilder implementation.
