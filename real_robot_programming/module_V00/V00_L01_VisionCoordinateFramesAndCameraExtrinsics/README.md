# V00_L01 - Vision Coordinate Frames and Camera Extrinsics

## Lesson State

- Module: `V00 - AprilTag Vision Observation and Pose Fusion`
- Lesson: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`
- Previous lesson: `A01_L09_PathPlannerNamedCommandsAndEventMarkers - COMPLETE / FROZEN / READ-ONLY`
- Status: `COMPLETE / FROZEN / READ-ONLY`
- Architecture audit: `PASS`
- Design lock: `PASS / IMPLEMENTED AS APPROVED`
- Baseline build: `PASS / USER-VERIFIED`
- Baseline tests: `PASS / USER-VERIFIED`; inherited reports show `446/446 PASS`
- Production implementation: `PASS` - one pure vendor-neutral frame helper
- Focused tests: `18/18 PASS / CODEX-VERIFIED`
- Inherited regression: `446/446 PASS / CODEX-VERIFIED`
- Full test suite: `464/464 PASS / CODEX-VERIFIED`
- Clean build: `PASS / CODEX-VERIFIED`
- WPILib VS Code Build Robot Code: `PASS / USER-VERIFIED`; `BUILD SUCCESSFUL`
- Simulation / Driver Station / Real Robot: `NOT APPLICABLE` for V00_L01
  because no runtime Vision/camera behavior, real adapter, or physical camera
  calibration exists
- Git Commit / Push: `NOT TESTED` - user-owned; Codex ran no Git operations

## Authoritative Objective

V00_L01 teaches one concept: the coordinate-frame and immutable camera-mounting
contract needed before any AprilTag vision observation or pose estimation can
exist. It defines the field, robot, camera, and tag frames and fixes the exact
meaning of `robotToCamera`.

It does not select a camera, import a vision vendor library, acquire data,
identify a tag, load the 2026 field layout, estimate robot pose, evaluate a
measurement, model latency, simulate vision, integrate real hardware, or fuse a
measurement.

## WPILib Frame Convention

All L01 geometry uses WPILib NWU coordinates:

- +X forward
- +Y left
- +Z up
- roll about +X
- pitch about +Y
- yaw about +Z
- positive rotation by the right-hand rule
- meters for translation
- radians for stored/computed rotation

When looking along a positive axis toward the origin, counter-clockwise is
positive. From above the robot, positive yaw turns +X toward +Y.

## Four Frames

### Field frame

The single canonical, always-blue WPILib field frame is fixed to the field.
Its origin is at the right side of the blue alliance wall, +X points toward the
red end, +Y points left when viewed from the blue end, and +Z points up. Vision
poses remain in this frame for both alliances and are never alliance-flipped.
V00_L02 owns the official AprilTag field layout.

### Robot frame

The robot frame is attached to the chassis at the existing Swerve localization
reference and drivetrain center of rotation. +X is robot-forward, +Y is
robot-left, and +Z is up. The frame moves and rotates with the robot.

### Camera frame

The camera frame is attached to the camera mounting body and normalized to the
same WPILib NWU axes: +X camera-forward, +Y camera-left, and +Z camera-up. Any
future vendor optical convention must be converted inside the real VisionIO
adapter before vendor-neutral data leaves IO.

### AprilTag frame

The AprilTag frame is centered on the tag. +X is normal to and outward from the
tag face, +Y is tag-left when looking in the +X direction, and +Z is tag-up.
The frame rotates with the physical tag. L01 defines only this geometric
meaning; V00_L02 owns tag IDs and canonical field poses.

## Transform Direction Contract

An `aToB` name means the transform that maps a pose at A to the corresponding
pose at B when applied relative to A's pose frame. WPILib applies the transform
translation in the starting pose frame and then applies its rotation.

`robotToCamera` is the immutable camera mounting extrinsic:

```text
Robot frame
    |
    | robotToCamera
    v
Camera frame
```

Its translation is the camera origin relative to the robot origin, expressed
in robot axes. Its rotation is the camera orientation relative to the robot.
`cameraToRobot` is exactly `robotToCamera.inverse()` and must never be
substituted without inversion.

Use composition in this order:

```text
fieldToCamera = fieldToRobot.transformBy(robotToCamera)
fieldToTag = fieldToCamera.transformBy(cameraToTarget)
cameraToRobot = robotToCamera.inverse()
```

The named transforms are not interchangeable:

- `fieldToRobot`: robot pose in the canonical field frame.
- `robotToCamera`: fixed camera mount relative to the robot.
- `fieldToCamera`: camera pose in the canonical field frame.
- `cameraToTarget`: observed target relative to the camera; future scope.
- `fieldToTag`: tag pose in the canonical field frame; V00_L02 authority.
- `cameraToRobot`: inverse camera mounting transform.

## Physical Camera Extrinsics

The future production mounting transform requires six measured inputs:

1. X position in meters: forward positive from robot origin.
2. Y position in meters: left positive from robot origin.
3. Z position in meters: up positive from robot origin.
4. Roll in radians about +X.
5. Pitch in radians about +Y.
6. Yaw in radians about +Z.

All six values are `TBD / USER MEASUREMENT REQUIRED`. No identity transform,
zero vector, estimate, example, or vendor default may masquerade as production
calibration.

Once measured and validated, the smallest permanent authority is one immutable
`Transform3d kRobotToCamera` inside `Constants.VisionConstants`. Until then,
pure frame-math code must receive the transform explicitly and tests may use
clearly labeled synthetic geometry only.

## Implemented L01 API

`frc.robot.observation.vision.VisionFrameTransform` is a stateless,
non-instantiable helper with exactly three public operations:

```java
Pose3d fieldToCamera(Pose3d fieldToRobot, Transform3d robotToCamera)
Transform3d cameraToRobot(Transform3d robotToCamera)
Pose3d fieldToRobotFromCamera(Pose3d fieldToCamera, Transform3d robotToCamera)
```

Every input and computed result is checked for null and nonfinite translation
or rotation components. The helper reads no camera, vendor API, subsystem,
clock, Driver Station, NetworkTables, telemetry, configuration, or mutable
state. Target observations and `cameraToTarget` composition remain deferred.

## Preserved Architecture

- The Frozen Backbone and immutable observation flow remain unchanged.
- `RobotContainer` remains composition root only.
- `SwerveSubsystem` remains sole owner of `SwerveDrivePoseEstimator`,
  localization state, EstimatedPose, and the future fusion entry point.
- Autonomous and AutoBuilder continue consuming only `getEstimatedPose()`.
- A01_L04 remains the sole alliance-transform owner.
- Telemetry remains read-only.
- No vendor is selected in V00_L01-L07; V00_L08 owns the compatibility gate.

## Deferred Roadmap Boundaries

- V00_L02: official AprilTag field layout.
- V00_L03: VisionIO and immutable observation.
- V00_L04: deterministic vision simulation.
- V00_L05: AprilTag robot-pose estimation.
- V00_L06: measurement-quality contract.
- V00_L07: timestamp and latency contract.
- V00_L08: one reviewed real vision adapter.
- V00_L09: accepted timestamped measurement fusion through the Swerve-owned
  `addVisionMeasurement(...)` boundary.

## Final Lesson State

V00_L01 is `COMPLETE / FROZEN / READ-ONLY`. The approved pure
frame-transform delta, focused tests, inherited regression, full suite, clean
build, user-verified WPILib VS Code build, architecture review, and lesson
documentation gates are reconciled and PASS.

V00_L01 is the frozen inheritance source for
`V00_L02_AprilTagFieldLayoutContract`. V00_L02 remains
`NOT CREATED / NOT STARTED`; this closure does not authorize its creation.
