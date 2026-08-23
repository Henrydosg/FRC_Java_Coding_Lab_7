# V00_L01 - Vision Coordinate Frames and Camera Extrinsics - Plan

## Current State

- Status: `COMPLETE / FROZEN / READ-ONLY`.
- Source: `A01_L09_PathPlannerNamedCommandsAndEventMarkers - COMPLETE / FROZEN / READ-ONLY`.
- Baseline build and inherited tests: `PASS / USER-VERIFIED`.
- Architecture audit and design lock: `PASS`.
- Production implementation: `IMPLEMENTED AS APPROVED`.
- Git/GitHub: user-owned; not run by Codex.

## One Learning Objective

Define one unambiguous, vendor-neutral SE(3) frame contract for the canonical
field, robot, camera, and AprilTag frames, centered on the immutable
`robotToCamera` mounting transform. L01 does not acquire images, identify tags,
load a field layout, estimate robot pose, evaluate measurements, model timing,
simulate vision, integrate a real camera, or fuse vision.

## Frame and Transform Design Lock

All geometry is normalized to WPILib NWU:

- +X forward, +Y left, +Z up.
- Roll, pitch, and yaw rotate about +X, +Y, and +Z respectively.
- Positive rotation follows the right-hand rule; viewed along a positive axis
  toward the origin, counter-clockwise is positive.
- Translation is stored in meters and rotation in radians. Degrees may be used
  only at a documented human-input boundary and must be converted explicitly.

Frame meanings:

- `field`: the single canonical, always-blue WPILib field frame. The exact
  official tag layout and tag identities belong to V00_L02.
- `robot`: the existing Swerve localization reference at the drivetrain center
  of rotation; axes rotate with the robot.
- `camera`: the camera body frame normalized to WPILib NWU; a future real
  adapter must convert any vendor optical convention before data leaves IO.
- `tag`: an NWU frame centered on the AprilTag; +X is the outward face normal,
  +Y is tag-left when looking in +X, and +Z is tag-up. Its field pose belongs
  to V00_L02.

Names always identify direction. `aToB` maps a pose at frame A to frame B when
applied in A's pose frame:

```text
fieldToCamera = fieldToRobot transformBy robotToCamera
cameraToRobot = inverse(robotToCamera)
fieldToTag = fieldToCamera transformBy cameraToTarget
```

`robotToCamera` therefore contains the camera origin expressed from the robot
origin and the camera orientation relative to the robot. Its inverse is not
interchangeable with it.

## Camera Mounting Configuration

The smallest permanent configuration location is one immutable
`Transform3d kRobotToCamera` in `Constants.VisionConstants`. It shall be added
only after the user supplies validated X/Y/Z, roll/pitch/yaw measurements. L01
must not install identity, zeros, guesses, or example values as production
calibration.

Before physical measurements exist, future L01 frame-math code must accept an
explicit `robotToCamera` parameter. Synthetic values are permitted only in
tests and must be labeled as test geometry.

## Implemented Production Delta

The approved L01 delta is one
vendor-neutral, non-instantiable pure helper named
`frc.robot.observation.vision.VisionFrameTransform` that:

1. accepts explicit immutable WPILib `Pose3d`/`Transform3d` values;
2. validates null and nonfinite translation/rotation components;
3. exposes only named composition/inversion operations required to prove the
   locked directions; and
4. reads no clocks, hardware, DriverStation, NetworkTables, subsystem, or
   mutable global state.

The physical `Constants.VisionConstants.kRobotToCamera` value is a separate
calibration input and remains deferred while measurements are TBD. No
RobotContainer, SwerveSubsystem, autonomous, IO, telemetry, deploy asset, or
vendor change is required for L01 frame-math implementation.

## Implemented Test Delta

One focused `VisionFrameTransformTest` class covers:

1. `robotToCamera` direction and `cameraToRobot` inversion;
2. `fieldToRobot` plus `robotToCamera` composition;
3. identity and nontrivial translation/roll/pitch/yaw cases;
4. inverse round-trip and composition-order noncommutativity;
5. NWU axis signs, meters/radians, null, NaN, and infinity rejection; and
6. deterministic repeatability and caller-input immutability with no vendor
   imports.

Camera-target to field-tag composition remains deferred because L01 has no
target observation contract and does not need another public method.

The full inherited regression and clean build remain required after any future
implementation.

## Preserved Ownership

- `RobotContainer`: construction, implementation selection, injection, and
  bindings only.
- `SwerveSubsystem`: sole estimator/localization owner and future fusion entry.
- Autonomous/AutoBuilder: `getEstimatedPose()` consumer only.
- L04: sole alliance transform owner.
- Telemetry: immutable observation consumer only.
- No vendor selection before V00_L08.

## Locked Future Lesson Boundaries

- V00_L02: official AprilTag IDs and poses in the canonical field layout.
- V00_L03: vendor-neutral VisionIO, VisionIOInputs, and immutable observation.
- V00_L04: deterministic vision simulation with independent ground truth.
- V00_L05: derive a canonical field-relative robot-pose candidate.
- V00_L06: measurement quality and uncertainty decisions.
- V00_L07: capture timestamp, latency, freshness, ordering, and duplicates.
- V00_L08: one real adapter only after the compatibility/vendor gate.
- V00_L09: accepted timestamped measurement fusion at the Swerve-owned
  `addVisionMeasurement(...)` boundary.

## Final Closure and Inheritance

All L01 implementation, automated verification, user WPILib VS Code build,
architecture review, changed-file audit, and documentation gates are complete.
V00_L01 is `COMPLETE / FROZEN / READ-ONLY` and must not be modified.

The missing physical extrinsic measurements remain explicitly TBD and must not
be guessed. V00_L01 is the inheritance source for
`V00_L02_AprilTagFieldLayoutContract`, which remains
`NOT CREATED / NOT STARTED` and requires a separate activation workflow.
