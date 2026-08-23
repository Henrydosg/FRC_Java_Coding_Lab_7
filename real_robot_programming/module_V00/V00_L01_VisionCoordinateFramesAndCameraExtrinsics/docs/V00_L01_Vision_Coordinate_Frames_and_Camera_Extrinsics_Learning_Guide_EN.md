# V00_L01 Learning Guide: Vision Coordinate Frames and Camera Extrinsics

Status: `FINAL / PASS` - English normative learning guide  
Lesson: `V00_L01_VisionCoordinateFramesAndCameraExtrinsics`

## 1. Learning Goal

This lesson establishes the geometry language used by every later V00 lesson.
It answers two questions:

1. Which coordinate frame gives meaning to a position and orientation?
2. In which direction does the fixed camera mounting transform point?

L01 does not detect AprilTags, choose a camera vendor, estimate robot pose, or
fuse measurements. Those tasks require this contract first.

## 2. Pose3d and Transform3d Are Different

A `Pose3d` says where one frame is located and oriented relative to a reference
frame. For example, `fieldToRobot` is the robot pose expressed in the canonical
field frame.

A `Transform3d` says how to move from one pose/frame to another. For example,
`robotToCamera` is the camera mounting offset and orientation relative to the
robot.

```text
Pose3d:       "Where is this frame?"
Transform3d:  "How do I move from A to B?"
```

The names encode direction. Reversing `robotToCamera` requires an inverse; it
is not a harmless rename.

## 3. WPILib NWU Convention

L01 uses WPILib's north-west-up convention everywhere:

```text
             +Z up
               |
               |
               o------ +X forward
              /
           +Y left
```

- Translation uses meters.
- Rotation uses radians.
- Roll rotates about +X.
- Pitch rotates about +Y.
- Yaw rotates about +Z.
- Positive rotation follows the right-hand rule.

There is no hidden inches-to-meters or degrees-to-radians conversion inside
`VisionFrameTransform`.

## 4. The Four Frames

### Field

The field frame is WPILib's single canonical, always-blue field frame. Vision
measurements remain in this frame for both alliances and are never alliance
flipped. V00_L02 will own the official AprilTag field layout.

### Robot

The robot frame is fixed to the chassis at the existing Swerve localization
reference and center of rotation. +X is robot-forward, +Y is robot-left, and
+Z is up.

### Camera

The camera frame is fixed to the camera body and normalized to WPILib NWU:
+X camera-forward, +Y camera-left, +Z camera-up. A future real adapter must
convert any vendor optical convention before data leaves IO.

### AprilTag

The AprilTag frame is centered on the tag. +X points outward from its face,
+Y is tag-left when looking along +X, and +Z is tag-up. L01 defines the frame;
V00_L02 will define official tag identities and field poses.

## 5. robotToCamera Is the Mounting Extrinsic

`robotToCamera` is fixed while the physical mount remains unchanged:

```text
FIELD
  | fieldToRobot
  v
ROBOT
  | robotToCamera
  v
CAMERA
```

Its translation is the camera origin relative to the robot origin, expressed
in robot axes. Its rotation is the camera orientation relative to the robot.

The forward composition is:

```java
Pose3d fieldToCamera = fieldToRobot.transformBy(robotToCamera);
```

The inverse mount is:

```java
Transform3d cameraToRobot = robotToCamera.inverse();
```

Therefore recovery from an already known camera field pose is:

```java
Pose3d fieldToRobot = fieldToCamera.transformBy(cameraToRobot);
```

## 6. Why Composition Order Matters

A transform's translation is applied in the starting pose's axes. If the robot
has a +90-degree yaw, a camera mounted one meter robot-forward appears one
meter in the field's +Y direction, not field +X.

```text
Robot yaw = +90 degrees
robot +X forward  -> field +Y
```

Applying `robotToCamera` twice, applying it backward without inversion, or
adding translations directly in field axes produces a different pose.

## 7. Implemented API

`frc.robot.observation.vision.VisionFrameTransform` exposes only:

```java
fieldToCamera(Pose3d fieldToRobot, Transform3d robotToCamera)
cameraToRobot(Transform3d robotToCamera)
fieldToRobotFromCamera(Pose3d fieldToCamera, Transform3d robotToCamera)
```

The class is stateless and non-instantiable. It rejects:

- null poses or transforms;
- nonfinite X, Y, or Z translation; and
- nonfinite quaternion components representing rotation.

WPILib geometry objects are treated as immutable inputs. Each operation is
deterministic and does not mutate caller-owned values.

## 8. Physical Camera Values Remain Unknown

Production calibration eventually needs six measured values: X, Y, Z, roll,
pitch, and yaw of the camera relative to the robot. They remain
`TBD / USER MEASUREMENT REQUIRED`.

No identity transform, example value, vendor default, or guess is stored in
`Constants`. Tests use synthetic geometry only. After measurement and review,
the future smallest authority may be one immutable
`Constants.VisionConstants.kRobotToCamera`.

## 9. Deferred Work

`cameraToTarget` and field-to-tag composition are intentionally absent. L01
has no target observation contract. The remaining roadmap introduces the field
layout, VisionIO/Observation contract, simulation, pose estimation, quality,
timing, one reviewed real adapter, and finally Swerve-owned fusion.

The future observation direction will be:

```text
CAMERA
  | cameraToTarget
  v
TAG
```

Acquiring `cameraToTarget` belongs to future lessons; this diagram defines
direction only and does not claim detection or camera behavior in L01.

The following boundaries remain frozen:

- `RobotContainer` is composition root only.
- `SwerveSubsystem` alone owns `SwerveDrivePoseEstimator` and future fusion.
- Autonomous consumes only `getEstimatedPose()`.
- A01_L04 alone owns alliance transformation.
- Telemetry is read-only.
- No vendor is selected before V00_L08.

## 10. Verification Summary

- Focused L01 tests: 18/18 PASS.
- Frozen inherited regression: 446/446 PASS.
- Full suite: 464/464 PASS.
- Clean build: PASS - `BUILD SUCCESSFUL in 29s`.
- WPILib VS Code Build Robot Code: PASS / USER-VERIFIED.
- Simulation: not required; no L01 runtime vision behavior exists.
- Real robot: not required; no physical camera or adapter exists.

Exact physical mounting values, detection accuracy, pose accuracy, tuning,
latency, quality thresholds, and fusion behavior are not claimed.

V00_L01 is `COMPLETE / FROZEN / READ-ONLY` and is the frozen inheritance
source for V00_L02. V00_L02 remains `NOT CREATED / NOT STARTED`.
