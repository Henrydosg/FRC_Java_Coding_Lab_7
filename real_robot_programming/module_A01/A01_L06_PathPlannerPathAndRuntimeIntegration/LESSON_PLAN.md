# A01_L06 - PathPlanner Path and Runtime Integration - Plan and Verification Record

## Activation State

- Lesson: `A01_L06_PathPlannerPathAndRuntimeIntegration`
- Title: `A01_L06 - PathPlanner Path and Runtime Integration`
- Previous lesson: `A01_L05_HolonomicTrajectoryFollowing - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture Review: `PASS`
- Design-Lock Amendment: `PASS`
- Final Design-Lock: `PASS`
- Implementation authorization: `PASS` - separate authorization was supplied
  after activation for the locked minimum delta.
- Implementation: `PASS` - current production, focused-test, and path-asset
  delta is present.
- Focused L06 tests: `PASS` - user supplied `18/18`.
- Inherited L01-L05 regression: `PASS` - user supplied PASS for the required
  direct regression set.
- Full test suite: `PASS` - user supplied PASS.
- Clean build: `PASS` - user supplied PASS.
- Simulation: `PASS` - user supplied Blue approximately `+1.0 m` motion,
  alliance-transformed approximately `1.0 m` opposite-direction motion, Glass
  reset verification, and valid EstimatedPose evidence.
- CAN hardware blocker: `CLOSED` - user supplied closure after physical
  repair/replacement.
- Real-Robot Execution Before Recalibration: `PASS / USER-CONFIRMED` for the
  one-meter PathPlanner autonomous on both Blue and Red.
- Post-Recalibration Real-Robot Verification: `PASS / USER-CONFIRMED` - the
  user physically executed the one-meter autonomous after the latest Swerve
  zero-offset recalibration on both Blue and Red.

The project was inherited from frozen L05. Activation established the single
editable lesson and approved scope; it did not itself implement the scope.
The later separately authorized implementation and supplied evidence are
recorded below. L06 is now `COMPLETE / FROZEN / READ-ONLY`.

## One New Concept

Load one PathPlanner `.path` asset, validate and adapt its trajectory data, and
execute it through the already-understood L05 holonomic follower and safety
contracts. PathPlanner is a path-authoring/runtime data boundary, not an owner
of localization, actuation, safety, alliance policy, or command requirements.

## Locked Learning Path

- Asset: `A01_L06_OneMeter_Forward.path`
- Deploy path: `src/main/deploy/pathplanner/paths/`
- Runtime key: `A01_L06_OneMeter_Forward`
- Canonical frame: Blue origin.
- Start pose: `(0.000 m, 0.000 m, 0 deg)`.
- Goal pose: `(1.000 m, 0.000 m, 0 deg)`.
- Selected field variant: `REBUILT_WELDED` (`16.541 m x 8.069 m`).
- Constraints: `0.50 m/s` maximum velocity, `1.00 m/s^2` maximum acceleration,
  `0.75 rad/s` maximum angular velocity, and `1.50 rad/s^2` maximum angular
  acceleration.
- No markers, rotation targets, zones, replanning, pathfinding, or multiple
  autonomous routines.

The asset was intentionally absent at activation. It was added only after the
separate implementation authorization and is now present at the locked deploy
location.

## Adapter and Ownership Contract

The current narrow adapter loads the asset with the approved
PathPlannerLib `2026.1.2` API, generates/inspects finite trajectory states using
the approved provisional RobotConfig, validates every state and timestamp, and
constructs the finite trajectory representation consumed by the unchanged L05
follower. The L05 follower remains the runtime sampling and drivetrain-command
boundary.

A01/L04 owns the exactly-one Blue/Red transformation. The asset remains
canonical Blue-frame and PathPlanner flipping/mirroring is absent or disabled.
The adapter, follower, IO, SwerveSubsystem, and telemetry must not apply a
second transform.

## RobotConfig Classification

Verified inherited values used by the current implementation:

- wheel radius `0.0508 m`
- drive gearing `6.75:1`
- Kraken X60 drive motor
- one drive motor per module
- drive current limit `70 A`
- wheelbase `0.5461 m`
- trackwidth `0.5461 m`
- module offsets `+/-0.27305 m`
- FOC disabled in the CTRE runtime path

**PROVISIONAL — LEARNING/SIMULATION ONLY — NOT MEASURED — NOT FINAL**

- mass `45.0 kg`
- MOI `5.0 kg·m²`
- maximum drive velocity `4.0 m/s`
- wheel COF `1.0`

The four provisional values are allowed only for conservative learning and
Simulation. They must not be called verified, measured, calibrated, final, or
competition-authoritative.

## Required Fail-Closed Behavior

Missing or malformed asset, trajectory-generation failure, null/nonfinite
state, nonfinite timing, incompatible runtime/API, mode loss, cancellation,
timeout, invalid pose/observation/output, or failed submission must stop
through centralized `SwerveSubsystem.stop()` and must not automatically
restart. Motion remains permitted only in Autonomous+Enabled mode.

## Verification Plan

The authorized implementation and verification record is:

1. Focused adapter/path-loading/state-validation tests: `PASS`, `18/18`.
2. Exactly-once Blue/Red transform and no-double-transform tests: `PASS` within
   the focused L06 result.
3. Requirement ownership, cancellation, timeout, mode-loss, centralized-stop,
   and no-restart tests: `PASS` within the focused L06 result.
4. Inherited regression and clean build: `PASS` from user-supplied evidence.
5. Full test suite: `PASS` from user-supplied evidence.
6. Simulation with the known path from accepted Blue and Red starting contexts:
   `PASS` from user-supplied evidence.
7. Post-recalibration Swerve configuration tests: `PASS`, `41/41`, zero
   failures, from existing generated evidence after the current offsets were
   compiled.
8. Documentation reconciliation and lesson closure: `PASS`.

## Real-Robot Evidence Reconciliation

Real-robot L06 one-meter autonomous execution was user-verified on both Blue
and Red after the latest Swerve zero-offset recalibration. Both executions are
recorded as functional PASS evidence; no exact endpoint measurement is
invented.

The latest offsets are FL `+0.068603515625`, FR `+0.014404296875`,
BL `+0.46240234375`, and BR `-0.057373046875` rotations. Simulation was rerun
successfully after this recalibration.

The Blue run showed slight endpoint overshoot followed by a small closed-loop
reverse correction and settling near the intended endpoint. This is an observed
behavior only; no architecture failure or root cause is proven. Exact physical
endpoint accuracy, including exact `1.000 m`, is not formally characterized or
claimed. Final PID/feedforward and physical-model tuning remain deferred. The
RobotConfig values mass `45.0 kg`, MOI `5.0 kg*m^2`, maximum drive velocity
`4.0 m/s`, and wheel COF `1.0` remain provisional; the configured mass is known
to exceed the user's current real-robot mass estimate, but no single provisional
value is proven to cause the behavior. No L01-L05 source, test, architecture,
SwerveSubsystem, IO, telemetry, or frozen contract was changed.

## Explicit Exclusions

AutoBuilder, chooser/multiple autos, NamedCommands, event markers, dynamic
replanning, pathfinding, vision/AprilTags, mechanism coordination, and broader
competition autonomous architecture are deferred to later authorized scope.
The official `vendordeps/PathplannerLib.json` at version `2026.1.2` is
preserved and shall not be replaced or upgraded. AutoBuilder, chooser/multiple
autos, NamedCommands, event markers, dynamic replanning, pathfinding,
vision/AprilTags, mechanism coordination, and broader competition autonomous
architecture remain excluded.
