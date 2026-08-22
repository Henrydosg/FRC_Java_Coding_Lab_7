# Lesson Status

## Identity

- Module: `A01 - Autonomous Navigation and Path Following`
- Lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`
- Title: `A01_L06 - PathPlanner Path and Runtime Integration`
- Previous Lesson: `A01_L05_HolonomicTrajectoryFollowing`
- Previous Lesson State: `COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE`
- Active State: `COMPLETE / FROZEN / READ-ONLY`
- Freeze State: `FROZEN`
- Lesson Goal: load one deterministic PathPlanner path and integrate its validated trajectory data through the frozen L05 follower and safety contracts.

## Gates and Evidence

- Baseline Build: `PASS` - L06 was copied from frozen L05; the inherited
  baseline was user-verified `BUILD SUCCESSFUL`.
- Directory Identity: `PASS` - `A01_L06_PathPlannerPathAndRuntimeIntegration`
  matches the approved A01 lesson identity.
- Architecture Review: `PASS` - approved A01 ADR and Final Design-Lock.
- Design-Lock Amendment: `PASS` - the provisional learning-only RobotConfig
  baseline is explicitly authorized for Simulation/learning scope.
- Final Design-Lock: `PASS` - the approved L06 implementation boundary remains
  unchanged; separate implementation authorization was subsequently supplied.
- PathPlanner Compatibility Gate: `PASS` - user supplied OpenJDK 17.0.16 and
  official PathPlannerLib `2026.1.2` vendordep resolution evidence.
- `compileJava`: `PASS` - user supplied Java 17 production compile result for
  the current L06 implementation.
- `compileTestJava`: `PASS` - user supplied Java 17 test compile result for the
  current L06 tests.
- Compatibility-Gate Tests: `PASS` - user supplied clean test result with
  `BUILD SUCCESSFUL`.
- Implementation: `PASS` - the separately authorized minimum L06 production,
  test, and path-asset delta is present; frozen L01-L05 source remains
  unchanged.
- Focused L06 Tests: `PASS` - user supplied `18/18` for
  `PathPlannerTrajectoryAdapterTest` and
  `RobotContainerPathPlannerIntegrationTest`.
- Inherited L01-L05 Regression: `PASS` - user directly executed the required
  inherited regression tests and supplied PASS for
  `RobotContainerAutonomousModeSchedulingTest`,
  `AllianceAwareAutonomousStartPoseResetCommandTest`,
  `HolonomicTrajectoryFollowingCommandTest`,
  `SwerveSimulationIntegrationTest`, and `FieldAllianceTransformTest`.
- Full Test Suite: `PASS` - user supplied full-suite PASS; no aggregate count is
  asserted here because it was not repeated in the current evidence.
- Build: `PASS` - user supplied the successful Java 17 build result for the
  implemented L06 scope.
- Clean Build: `PASS` - user supplied clean-build PASS.
- Post-Recalibration Swerve Configuration Tests: `PASS` - existing generated
  evidence records `41/41` tests with zero failures after the current offsets
  and their direct fixtures were compiled.
- Simulation: `PASS` - user supplied Blue canonical approximately `+1.0 m`
  motion, the alliance-transformed approximately `1.0 m` opposite-direction
  motion, Glass Known Starting Pose reset, and valid EstimatedPose evidence;
  Simulation was rerun successfully after the latest zero-offset recalibration.
- Driver Station / Glass: `PASS` for the supplied Simulation/Glass reset and
  EstimatedPose evidence; this is not real-robot autonomous evidence.
- CAN Hardware Blocker: `CLOSED` - user supplied closure after physical
  repair/replacement.
- Real Robot: `PASS / USER-CONFIRMED` for the post-recalibration Blue and Red
  one-meter autonomous executions supplied by the user.
- Real-Robot Execution Before Recalibration: `PASS / USER-CONFIRMED` - the L06
  one-meter PathPlanner autonomous was physically run on both Blue and Red and
  the user reported the one-meter behavior working; no unrecorded measurement
  or centimeter-level accuracy is inferred.
- Post-Recalibration Real-Robot Verification: `PASS / USER-CONFIRMED` - the
  user physically executed the one-meter autonomous after the latest Swerve
  zero-offset recalibration on both Blue and Red.
- Transition Guide: `FINAL / PASS` -
  `docs/A01_L05_to_A01_L06_Step_by_Step.md` records the complete transition and
  closure evidence.
- Git Commit: `NOT TESTED` - user-owned; Codex does not run Git.
- Git Push: `NOT TESTED` - user-owned; Codex does not run Git.
- Known Issues: the four learning RobotConfig values remain explicitly
  provisional, not measured, and not final. The Blue run showed slight endpoint
  overshoot followed by a small closed-loop reverse correction and settling near
  the intended endpoint. Exact endpoint accuracy is not formally characterized;
  final PID/FF and physical-model tuning remain deferred. The configured mass
  `45.0 kg` is known to be higher than the user's current real-robot mass
  estimate, but no single provisional value is proven to cause the behavior.
  Mass, MOI, COF, maximum velocity, follower PID/feedforward, trajectory
  acceleration constraints, wheel traction, estimator behavior, and other
  physical/control factors remain candidates for later characterization.

## Preserved Architecture

- The Frozen Backbone and Frozen Interface Contract remain unchanged.
- `RobotContainer` remains the composition root only.
- `SwerveSubsystem` retains localization, actuation, mechanism state, and
  centralized `stop()` ownership. IO, observation, and telemetry boundaries are
  unchanged.
- L01/L02 starting-pose readiness, Autonomous+Enabled gating, one-shot
  consumption, fail-closed mode loss, and no automatic restart remain required.
- L03 trajectory/state semantics, L04 field/alliance ownership, and the L05
  holonomic follower remain frozen predecessor contracts.

## Approved L06 Scope

The single implemented asset is `A01_L06_OneMeter_Forward.path` in
`src/main/deploy/pathplanner/paths/`, with runtime key
`A01_L06_OneMeter_Forward`. It is canonical Blue-frame data from
`(0.000, 0.000, 0 deg)` to `(1.000, 0.000, 0 deg)` using the selected
`REBUILT_WELDED` field variant. The locked constraints are `0.50 m/s`, `1.00
m/s^2`, `0.75 rad/s`, and `1.50 rad/s^2`. Activation itself created no asset;
the asset was added during the later authorized implementation.

A01/L04 owns the exactly-one Blue/Red transform. PathPlanner flipping or
mirroring is not used. The narrow adapter validates PathPlanner trajectory
states and feeds a finite equivalent WPILib trajectory to the unchanged L05
follower. PathPlanner does not own localization, actuation, safety, alliance
policy, or command requirements; AutoBuilder is excluded.

## RobotConfig Classification

Verified inherited inputs: wheel radius `0.0508 m`, drive gearing `6.75:1`,
Kraken X60 drive motor, one drive motor per module, drive current limit `70 A`,
wheelbase `0.5461 m`, trackwidth `0.5461 m`, module offsets `+/-0.27305 m`, and
FOC disabled in the CTRE runtime path.

**PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL**

- mass `45.0 kg`
- MOI `5.0 kg·m²`
- maximum drive velocity `4.0 m/s`
- wheel COF `1.0`

No source or future documentation may describe these four values as verified,
measured, calibrated, final, or competition-authoritative.

## Safety and Exclusions

Missing/malformed assets, trajectory-generation failure, null/nonfinite states
or timing, incompatible runtime/API, invalid mode, cancellation, and timeout
must fail closed to centralized `SwerveSubsystem.stop()` with no automatic
restart. Focused tests, inherited regression, full tests, clean build,
Simulation, and post-recalibration Blue and Red one-meter autonomous execution
are PASS from user-supplied evidence. Exact physical endpoint accuracy is not
claimed.

AutoBuilder, chooser/multiple autos, NamedCommands, event markers, dynamic
replanning, pathfinding, vision/AprilTags, mechanism coordination, subsystem
or IO redesign, telemetry changes, and L01-L05 modification are forbidden.
The official `vendordeps/PathplannerLib.json` version `2026.1.2` is preserved.

L06 is `COMPLETE / FROZEN / READ-ONLY`. The latest Swerve offsets are
FL `+0.068603515625`, FR `+0.014404296875`, BL `+0.46240234375`, and
BR `-0.057373046875` rotations; the drive ratio remains exactly `6.75:1`.
The post-recalibration Blue/Red physical executions are recorded as functional
validation, while the observed Blue endpoint overshoot/reverse correction is
retained as an observation only. No exact `1.000 m` claim is made. Final
PID/feedforward and physical-model tuning remain deferred; no source, test, or
configuration file was changed by this evidence reconciliation.
